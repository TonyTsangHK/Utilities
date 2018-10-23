package testng

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import utils.hash.CRC

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2018-10-23
 * Time: 17:18
 */
class TestCRC {
    private val testBytes = "This is a common String for testing CRC.".toByteArray()
    private val expectedHexResult: MutableMap<CRC.PredefinedParameter, String> = HashMap()
    
    init {
        expectedHexResult[CRC.PredefinedParameter.CRC8_DEFAULT] = "0B"
        expectedHexResult[CRC.PredefinedParameter.CRC8_SAE_J1850] = "B6"
        expectedHexResult[CRC.PredefinedParameter.CRC8_SAE_J1850_ZERO] = "65"
        expectedHexResult[CRC.PredefinedParameter.CRC8_8H2F] = "8F"
        expectedHexResult[CRC.PredefinedParameter.CRC8_CDMA2000] = "6D"
        expectedHexResult[CRC.PredefinedParameter.CRC8_DARC] = "5B"
        expectedHexResult[CRC.PredefinedParameter.CRC8_DVB_S2] = "E5"
        expectedHexResult[CRC.PredefinedParameter.CRC8_EBU] = "9C"
        expectedHexResult[CRC.PredefinedParameter.CRC8_ICODE] = "28"
        expectedHexResult[CRC.PredefinedParameter.CRC8_ITU] = "5E"
        expectedHexResult[CRC.PredefinedParameter.CRC8_MAXIM] = "83"
        expectedHexResult[CRC.PredefinedParameter.CRC8_ROHC] = "CE"
        expectedHexResult[CRC.PredefinedParameter.CRC8_WCDMA] = "15"
        expectedHexResult[CRC.PredefinedParameter.CRC16_CCITT_ZERO] = "6139"
        expectedHexResult[CRC.PredefinedParameter.CRC16_ARC] = "354F"
        expectedHexResult[CRC.PredefinedParameter.CRC16_AUG_CCITT] = "DF00"
        expectedHexResult[CRC.PredefinedParameter.CRC16_BUYPASS] = "137D"
        expectedHexResult[CRC.PredefinedParameter.CRC16_CCITT_FALSE] = "E4E0"
        expectedHexResult[CRC.PredefinedParameter.CRC16_CDMA2000] = "95F5"
        expectedHexResult[CRC.PredefinedParameter.CRC16_DDS_110] = "E0BD"
        expectedHexResult[CRC.PredefinedParameter.CRC16_DECT_R] = "B7EB"
        expectedHexResult[CRC.PredefinedParameter.CRC16_DECT_X] = "B7EA"
        expectedHexResult[CRC.PredefinedParameter.CRC16_DNP] = "864D"
        expectedHexResult[CRC.PredefinedParameter.CRC16_EN_13757] = "A620"
        expectedHexResult[CRC.PredefinedParameter.CRC16_GENIBUS] = "1B1F"
        expectedHexResult[CRC.PredefinedParameter.CRC16_MAXIM] = "CAB0"
        expectedHexResult[CRC.PredefinedParameter.CRC16_MCRF4XX] = "F8A6"
        expectedHexResult[CRC.PredefinedParameter.CRC16_RIELLO] = "7FE7"
        expectedHexResult[CRC.PredefinedParameter.CRC16_T10_DIF] = "4E6A"
        expectedHexResult[CRC.PredefinedParameter.CRC16_TELEDISK] = "61D7"
        expectedHexResult[CRC.PredefinedParameter.CRC16_TMS37157] = "CC8C"
        expectedHexResult[CRC.PredefinedParameter.CRC16_USB] = "CFA4"
        expectedHexResult[CRC.PredefinedParameter.CRC16_A] = "C872"
        expectedHexResult[CRC.PredefinedParameter.CRC16_KERMIT] = "6307"
        expectedHexResult[CRC.PredefinedParameter.CRC16_MODBUS] = "305B"
        expectedHexResult[CRC.PredefinedParameter.CRC16_X_25] = "0759"
        expectedHexResult[CRC.PredefinedParameter.CRC16_XMODEM] = "6139"
        expectedHexResult[CRC.PredefinedParameter.CRC32_DEFAULT] = "48B80773"
        expectedHexResult[CRC.PredefinedParameter.CRC32_BZIP2] = "EF860B22"
        expectedHexResult[CRC.PredefinedParameter.CRC32_C] = "FC1E18C1"
        expectedHexResult[CRC.PredefinedParameter.CRC32_D] = "9F4B380A"
        expectedHexResult[CRC.PredefinedParameter.CRC32_MPEG2] = "1079F4DD"
        expectedHexResult[CRC.PredefinedParameter.CRC32_POSIX] = "9DC5C34A"
        expectedHexResult[CRC.PredefinedParameter.CRC32_Q] = "B158A15B"
        expectedHexResult[CRC.PredefinedParameter.CRC32_JAMCRC] = "B747F88C"
        expectedHexResult[CRC.PredefinedParameter.CRC32_XFER] = "24EA935F"
    }
    
    @Test
    fun testCRC() {
        for (key in expectedHexResult.keys) {
            val crc = CRC(key)
            
            crc.update(testBytes, 0, testBytes.size)
            
            crc.digest()
            
            assertEquals(crc.crcHexExpression, expectedHexResult[key], "CRC HEX result does not match!")

            val verifyPass = crc.verify(testBytes, crc.crc)
            
            assertTrue(verifyPass, "CRC verify does not pass!")
        }
    }
}