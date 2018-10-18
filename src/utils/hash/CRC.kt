package utils.hash

import java.io.InputStream

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2018-10-16
 * Time: 17:51
 */
/**
 * CRC calculator with custom parameters handling
 */
class CRC(
    // CRC width, only accept 8 / 16 / 32
    val capacity: Int,
    // Initial register value
    val initialValue: Int,
    // CRC polynomial
    polynomial: Int, 
    // Final XOR value
    private val finalXorValue: Int = 0,
    inputReflect: Boolean = false,
    resultReflect: Boolean = false
) {
    // crc register
    private val register: Register
    // head of polynomial binaries
    private val polynomialHead: Register.BitNode

    // input reflect parameter
    private var inputReflect = false
    // result reflect parameter
    private var resultReflect = false

    // HEX expression of CRC hash
    val crcHexExpression: String
        get() = if (register.done) {
            register.getCrcHexExpression(resultReflect)
        } else {
            throw RuntimeException("CRC computation not finished, call digest() first!")
        }

    // Binary expression of CRC hash
    val crcBinaryExpression: String
        get() = if (register.done) {
            register.getCrcBinaryExpression(resultReflect)
        } else {
            throw RuntimeException("CRC computation not finished, call digest() first!")
        }

    // CRC hash
    val crc: Int
        get() = if (register.done) {
            register.getCrc(resultReflect)
        } else {
            throw RuntimeException("CRC computation not finished, call digest() first!")
        }
    
    // Constructor with predefined parameter
    constructor(predefinedParameter: PredefinedParameter): this(predefinedParameter.capacity, predefinedParameter.initialValue, predefinedParameter.polynomial, predefinedParameter.finalXorValue, predefinedParameter.inputReflect, predefinedParameter.resultReflect)

    init {
        // Restricting capacity to 8 / 16 / 32
        if (capacity != 8 && capacity != 16 && capacity != 32) {
            throw UnsupportedOperationException("Only support CRC8, CRC16 & CRC32")
        }
        this.inputReflect = inputReflect
        this.resultReflect = resultReflect
        polynomialHead = Register.BitNode(true)

        var p = polynomialHead

        for (i in capacity - 1 downTo 0) {
            val n: Register.BitNode
            if (i == 0) {
                n = Register.BitNode(polynomial and 1 == 1)
            } else {
                n = Register.BitNode(polynomial shr i and 1 == 1)
            }
            p.next = n
            n.previous = p

            p = n
        }

        register = Register(capacity, initialValue, polynomialHead)
    }

    /**
     * Append single byte
     * 
     * @param byt byte value
     */
    fun appendByte(byt: Int) {
        register.append(byt, inputReflect)
    }

    /**
     * Do final digest, this operation cannot be reversed
     */
    fun digest() {
        for (i in 0 until register.capacity) {
            register.append(false)
        }

        if (finalXorValue != 0) {
            register.doFinalXor(finalXorValue, resultReflect)
        }

        register.done()
    }

    /**
     * Verify CRC hash
     * 
     * @param inputStream data input stream
     * @param crcDigest CRC hash
     * @return verify result
     */
    fun verify(inputStream: InputStream, crcDigest: Int): Boolean {
        val verifyRegister = Register(register.capacity, initialValue, polynomialHead)
        
        val bytes =  ByteArray(5)
        
        var len = inputStream.read(bytes)
        
        while (len != -1) {
            verifyRegister.append(bytes, 0, len, inputReflect)
            len = inputStream.read(bytes)
        }
        
        return verify(verifyRegister, crcDigest)
    }

    /**
     * Verify CRC hash
     * 
     * @param bytes data bytes
     * @param crcDigest CRC hash
     * @return verify result
     */
    fun verify(bytes: ByteArray, crcDigest: Int): Boolean {
        val verifyRegister = Register(register.capacity, initialValue, polynomialHead)
        
        for (byt in bytes) {
            verifyRegister.append(byt.toInt(), inputReflect)
        }
        
        return verify(verifyRegister, crcDigest)
    }

    /**
     * actual verify logic after data feeding, only for internal use 
     */
    private fun verify(verifyRegister: Register, crcDigest: Int): Boolean {
        val digest: Int

        if (finalXorValue != 0x0) {
            // revert the final xor value
            digest = crcDigest.xor(finalXorValue)
        } else {
            digest = crcDigest
        }

        if (capacity == 8) {
            verifyRegister.append(digest.and(0x000000FF), resultReflect)
        } else if (capacity == 16) {
            if (resultReflect) {
                verifyRegister.append(digest.and(0x000000FF), resultReflect)
                verifyRegister.append(digest.and(0x0000FF00).shr(8), resultReflect)
            } else {
                verifyRegister.append(digest.and(0x0000FF00).shr(8))
                verifyRegister.append(digest.and(0x000000FF))
            }
        } else {
            // CRC32
            if (resultReflect) {
                verifyRegister.append(digest.and(0x00FF), resultReflect)
                verifyRegister.append(digest.and(0x0000FF00).shr(8), resultReflect)
                verifyRegister.append(digest.and(0x00FF0000).shr(16), resultReflect)
                verifyRegister.append(digest.shr(24), resultReflect)
            } else {
                verifyRegister.append(digest.shr(24))
                verifyRegister.append(digest.and(0x00FF0000).shr(16))
                verifyRegister.append(digest.and(0x0000FF00).shr(8))
                verifyRegister.append(digest.and(0x00FF))
            }
        }

        verifyRegister.done()

        return verifyRegister.getCrc(false) == 0
    }

    /**
     * CRC register
     */
    internal class Register(val capacity: Int, polynomialHead: BitNode) {
        private var head: BitNode? = null
        private var tail: BitNode? = null
        private var polynomialHead: BitNode? = null
        private var appendBitNode: BitNode? = null

        var size = 0
            private set

        // Operation done flag, once set to true, all data will be rejected
        var done = false
            private set

        val isEmpty: Boolean
            get() = head == null

        val crcBinaryExpression: String
            get() = getCrcBinaryExpression(false)

        val crcHexExpression: String
            get() = getCrcHexExpression(false)

        val crc: Int
            get() = getCrc(false)

        init {
            this.polynomialHead = polynomialHead
        }

        constructor(capacity: Int, initialValue: Int, polynomialHead: BitNode) : this(capacity, polynomialHead) {
            if (capacity == 8 || capacity == 16 || capacity == 32) {
                for (i in capacity - 1 downTo 0) {
                    if (i == 0) {
                        append(initialValue and 1 == 1)
                    } else {
                        append(initialValue shr i and 1 == 1)
                    }
                }
            }

            appendBitNode = head
        }
        
        fun done() {
            this.done = true
        }

        fun getHead(): Boolean {
            return if (head != null) {
                head!!.value
            } else {
                throw NullPointerException("Head is null")
            }
        }

        fun getTail(): Boolean {
            return if (tail != null) {
                tail!!.value
            } else {
                throw NullPointerException("Tail is null")
            }
        }

        fun pop(): Boolean {
            val oldHead = head

            if (head === tail) {
                tail = null
                head = tail
            } else {
                head = head!!.next
                head!!.previous = null

                oldHead!!.next = null
            }

            if (size > 0) {
                size--
            }

            return oldHead!!.value
        }

        fun popLeadingZeros() {
            while (head != null && !head!!.value) {
                pop()
            }
        }

        /**
         * Append single boolean value
         * 
         * @param value boolean value
         */
        fun append(value: Boolean) {
            if (done) {
                throw RuntimeException("Register closed input data rejected")
            }

            val newTail = BitNode(value)

            if (appendBitNode != null) {
                appendBitNode!!.xor(value)
                appendBitNode = appendBitNode!!.next
            } else {

                if (tail == null) {
                    tail = newTail
                    head = tail
                } else {
                    tail!!.next = newTail
                    newTail.previous = tail
                    tail = newTail
                }

                size++
            }

            // process during append
            if (polynomialHead != null && size == capacity + 1) {
                if (head!!.value) {
                    doXor(polynomialHead!!)
                } else {
                    pop()
                }
            }
        }

        /**
         * Append single byte
         * 
         * @param byt data byte
         * @param reflect input reflect parameter
         */
        @JvmOverloads
        fun append(byt: Int, reflect: Boolean = false) {
            if (reflect) {
                append(byt and 1 == 1)
                append(byt shr 1 and 1 == 1)
                append(byt shr 2 and 1 == 1)
                append(byt shr 3 and 1 == 1)
                append(byt shr 4 and 1 == 1)
                append(byt shr 5 and 1 == 1)
                append(byt shr 6 and 1 == 1)
                append(byt shr 7 and 1 == 1)
            } else {
                append(byt shr 7 and 1 == 1)
                append(byt shr 6 and 1 == 1)
                append(byt shr 5 and 1 == 1)
                append(byt shr 4 and 1 == 1)
                append(byt shr 3 and 1 == 1)
                append(byt shr 2 and 1 == 1)
                append(byt shr 1 and 1 == 1)
                append(byt and 1 == 1)
            }
        }

        /**
         * Append bytes
         * 
         * @param byteArr data byte array
         * @param offset start offset
         * @param length length of byte to read
         * @param reflect input reflect parameter
         */
        @JvmOverloads
        fun append(byteArr: ByteArray, offset: Int, length: Int, reflect: Boolean = false) {
            for (i in offset until offset + length) {
                append(byteArr[i].toInt(), reflect)
            }
        }

        fun getCrcBinaryExpression(reflect: Boolean): String {
            if (head == null) {
                return ""
            } else {
                val builder = StringBuilder()
                var n = head

                while (n != null) {
                    if (reflect) {
                        builder.insert(0, if (n.value) '1' else '0')
                    } else {
                        builder.append(if (n.value) '1' else '0')
                    }
                    n = n.next
                }
                return builder.toString()
            }
        }

        fun getCrcHexExpression(reflect: Boolean): String {
            if (head == null) {
                return ""
            } else {
                val builder = StringBuilder()
                var b = if (reflect) 8 else 1
                var r = 0
                var c = 0
                var n = tail

                while (n != null) {
                    if (n.value) {
                        r += b
                    }
                    if (reflect) {
                        b /= 2
                    } else {
                        b *= 2
                    }
                    n = n.previous
                    c++

                    if (c == 4) {
                        if (reflect) {
                            builder.append(Character.toUpperCase(Character.forDigit(r, 16)))
                        } else {
                            builder.insert(0, Character.toUpperCase(Character.forDigit(r, 16)))
                        }

                        c = 0
                        b = if (reflect) 8 else 1
                        r = 0
                    }
                }

                if (c > 0) {
                    if (reflect) {
                        builder.append(Character.toUpperCase(Character.forDigit(r, 16)))
                    } else {
                        builder.insert(0, Character.toUpperCase(Character.forDigit(r, 16)))
                    }
                }

                return builder.toString()
            }
        }

        fun getCrc(reflect: Boolean): Int {
            var result = 0
            var mask = if (reflect) 1 shl capacity - 1 else 1

            var n = tail

            while (n != null) {
                if (n.value) {
                    result = result or mask
                }

                if (reflect) {
                    mask = mask shr 1
                    if (n === tail) {
                        // Ensure sign bit is not one during right shifting
                        mask = mask and 0x7FFFFFFF
                    }
                } else {
                    mask = mask shl 1
                }
                n = n.previous
            }

            return result
        }

        /**
         * Perform the final XOR operation with final XOR value
         */
        @JvmOverloads
        fun doFinalXor(finalXorValue: Int, reflect: Boolean = false) {
            var n = tail
            var v = finalXorValue

            if (reflect) {
                var shift = capacity - 1
                while (n != null) {
                    n.xor(v shr shift and 1 == 1)

                    shift--
                    n = n.previous
                }
            } else {
                while (n != null && v != 0) {
                    n.xor(v and 1 == 1)

                    v = v shr 1
                    n = n.previous
                }
            }
        }

        /**
         * Perform XOR operation with polynomial
         */
        internal fun doXor(polynomialHead: BitNode) {
            if (size >= capacity + 1) {
                // Skip MSB = 0 and xor operation for 1^1=0
                if (head!!.value) {
                    var rn = head!!.next
                    var n = polynomialHead.next

                    while (n != null) {
                        rn!!.xor(n.value)

                        rn = rn.next
                        n = n.next
                    }
                }

                pop()
            }
        }

        /**
         * Linked node of boolean values
         */
        internal class BitNode(value: Boolean) {
            var value: Boolean
                private set
            
            var previous: BitNode? = null
            var next: BitNode? = null

            init {
                this.value = value
            }
            
            fun xor(value: Boolean) {
                this.value = this.value != value
            }

            fun or(value: Boolean) {
                this.value = this.value or value
            }

            fun and(value: Boolean) {
                this.value = this.value and value
            }
        }
    }

    /**
     * Predefined CRC parameter
     */
    enum class PredefinedParameter(val capacity: Int, val polynomial: Int, val initialValue: Int, val finalXorValue: Int, val inputReflect: Boolean, val resultReflect: Boolean) {
        // CRC8
        CRC8_DEFAULT(8, 0x07, 0x00, 0x00, false, false),
        CRC8_SAE_J1850(8, 0x1D, 0xFF, 0xFF, false, false),
        CRC8_SAE_J1850_ZERO(8, 0x1D, 0x00, 0x00, false, false),
        CRC8_8H2F(8, 0x2F, 0xFF, 0xFF, false, false),
        CRC8_CDMA2000(8, 0x9B, 0xFF, 0x00, false, false),
        CRC8_DARC(8, 0x39, 0x00, 0x00, true, true),
        CRC8_DVB_S2(8, 0xD5, 0x00, 0x00, false, false),
        CRC8_EBU(8, 0x1D, 0xFF, 0x00, true, true),
        CRC8_ICODE(8, 0x1D, 0xFD, 0x00, false, false),
        CRC8_ITU(8, 0x07, 0x00, 0x55, false, false),
        CRC8_MAXIM(8, 0x31, 0x00, 0x00, true, true),
        CRC8_ROHC(8, 0x07, 0xFF, 0x00, true, true),
        CRC8_WCDMA(8, 0x9B, 0x00, 0x00, true, true),

        // CRC16
        CRC16_CCITT_ZERO(16, 0x1021, 0x0000, 0x0000, false, false),
        CRC16_ARC(16, 0x8005, 0x0000, 0x0000, true, true),
        CRC16_AUG_CCITT(16, 0x1021, 0x1D0F, 0x0000, false, false),
        CRC16_BUYPASS(16, 0x8005, 0x0000, 0x0000, false, false),
        CRC16_CCITT_FALSE(16, 0x1021, 0xFFFF, 0x0000, false, false),
        CRC16_CDMA2000(16, 0xC867, 0xFFFF, 0x0000, false, false),
        CRC16_DDS_110(16, 0x8005, 0x800D, 0x0000, false, false),
        CRC16_DECT_R(16, 0x0589, 0x0000, 0x0001, false, false),
        CRC16_DECT_X(16, 0x0589, 0x0000, 0x0000, false, false),
        CRC16_DNP(16, 0x3D65, 0x0000, 0xFFFF, true, true),
        CRC16_EN_13757(16, 0x3D65, 0x0000, 0xFFFF, false, false),
        CRC16_GENIBUS(16, 0x1021, 0xFFFF, 0xFFFF, false, false),
        CRC16_MAXIM(16, 0x8005, 0x0000, 0xFFFF, true, true),
        CRC16_MCRF4XX(16, 0x1021, 0xFFFF, 0x0000, true, true),
        CRC16_RIELLO(16, 0x1021, 0xB2AA, 0x0000, true, true),
        CRC16_T10_DIF(16, 0x8BB7, 0x0000, 0x0000, false, false),
        CRC16_TELEDISK(16, 0xA097, 0x0000, 0x0000, false, false),
        CRC16_TMS37157(16, 0x1021, 0x89EC, 0x0000, true, true),
        CRC16_USB(16, 0x8005, 0xFFFF, 0xFFFF, true, true),
        CRC16_A(16, 0x1021, 0xC6C6, 0x0000, true, true),
        CRC16_KERMIT(16, 0x1021, 0x0000, 0x0000, true, true),
        CRC16_MODBUS(16, 0x8005, 0xFFFF, 0x0000, true, true),
        CRC16_X_25(16, 0x1021, 0xFFFF, 0xFFFF, true, true),
        CRC16_XMODEM(16, 0x1021, 0x0000, 0x0000, false, false),

        // CRC32
        // -1 = 0xFFFFFFFF, toInt() just to work around kotlin's inability to deal with negative integer literal ...
        CRC32_DEFAULT(32, 0x04C11DB7, -1, -1, true, true),
        CRC32_BZIP2(32, 0x04C11DB7, -1, -1, false, false),
        CRC32_C(32, 0x1EDC6F41, -1, -1, true, true),
        CRC32_D(32, 0xA833982B.toInt(), -1, -1, true, true),
        CRC32_MPEG2(32, 0x04C11DB7, -1, 0x00000000, false, false),
        CRC32_POSIX(32, 0x04C11DB7, 0x00000000, -1, false, false),
        CRC32_Q(32, 0x814141AB.toInt(), 0x00000000, 0x00000000, false, false),
        CRC32_JAMCRC(32, 0x04C11DB7, -1, 0x00000000, true, true),
        CRC32_XFER(32, 0x000000AF, 0x00000000, 0x00000000, false, false)
    }
}