package utils.extensions.string

import utils.string.StringUtil

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-21
 * Time: 11:19
 */

// Null safe empty string check, null means empty
fun String?.isEmptyString(): Boolean {
    return this == null || "".equals(this)
}

// Null safe string size getter
fun String?.stringSize(): Int {
    if (this === null) {
        return 0
    } else {
        return this.length
    }
}

fun String?.matchOnce(vararg strs: String?): Boolean {
    if (this == null) {
        return false
    } else {
        strs.forEach {
            if (it.equals(this)) {
                return true
            }
        }
        
        return false
    }
}

fun String?.matchOnceIgnoreCase(vararg strs: String?): Boolean {
    if (this === null) {
        return false
    } else {
        strs.forEach{
            if (it.equals(this, true)) {
                return true
            }
        }
        
        return false
    }
}

fun String.splitToList(pattern: String, ensureNewString: Boolean = false, ignoreCase: Boolean = false): List<String> {
    return StringUtil.splitStringToList(this, pattern, ensureNewString, ignoreCase)
}

fun String.splitToList(pattern: Regex, ensureNewString: Boolean = false, ignoreCase: Boolean = false): List<String> {
    return StringUtil.splitStringToList(this, pattern.toPattern(), ensureNewString, ignoreCase)
}

fun String.getPartOf(pattern: String, index: Int): String {
    return StringUtil.getPartOfStr(this, pattern, index)
}

fun String.truncateToFirstOccurrence(vararg patterns: String): String {
    return StringUtil.truncateToFirstOccurrence(this, *patterns)
}

fun String.countLines(): Int {
    return StringUtil.countLines(this)
}

// same as String.substring but the result is guaranteed to be a new string instance.
// this function also guaranteed return, if provided index is out of bounds empty string is returned without exception.
fun String.safeSubstring(start: Int, endExclusive: Int): String {
    return StringUtil.safeSubstring(this, start, endExclusive)
}

fun String.convertSpecialEntity(): String {
    return StringUtil.convertSpecialEntityString(this)
}

fun String.convertUnicodeCharacter(): String {
    return StringUtil.convertUnicodeCharacterString(this)
}

fun String.fillPrefix(prefix: String, length: Int): String {
    return StringUtil.fillPrefix(this, prefix, length)
}

fun String.trimPostfix(postfix: String, ignoreCase: Boolean = false): String {
    return StringUtil.trimPostfix(this, postfix, ignoreCase)
}