package utils.string;

import java.awt.Color;

import java.io.IOException;
import java.io.StringReader;

import java.nio.ByteBuffer;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.data.DoubleMap;
import utils.math.MathUtil;

public class StringUtil {
    public static final String DefaultRegex = "\t|\n|\r|\f";
    public static final char[]
         CHARS = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        },
        UPPER_CHARS = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        },
        LOWER_CHARS = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        },
        ALPHABETS = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        },
        HEX_CHARS   = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'},
        DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    
    // Half & full with character code point difference, except space which is 12256
    // Half width character code point range:    33 ..   126
    // Full width character code point range: 65281 .. 65374
    public static final int HALF_TO_FULL_CODE_POINT_DIFF = 65248, 
        HALF_WIDTH_CODE_POINT_LOWER_BOUND =    33, HALF_WIDTH_CODE_POINT_UPPER_BOUND =   126,
        FULL_WIDTH_CODE_POINT_LOWER_BOUND = 65281, FULL_WIDTH_CODE_POINT_UPPER_BOUND = 65374;
    
    private StringUtil() {}
    
    /**
     * Check whether the input string is empty (null or equals to "")
     * 
     * @param str input string
     * 
     * @return check result
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.equals("");
    }

    /**
     * Check whether the input string is not empty (not null and not empty string: "")
     * Simply the opposite of isEmpty(String)
     * 
     * @param str input string
     * 
     * @return check result
     */
    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    /**
     * Check if all the input strings are empty (null or equals to "")
     * 
     * @param strs input strings
     * @return check result
     */
    public static boolean isAllEmptyString(String ... strs) {
        for (String str : strs) {
            if (!isEmptyString(str)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if any of the input strings is empty (null or equals to "")
     *
     * @param strs input strings
     * @return check result
     */
    public static boolean hasEmptyString(String ... strs) {
        for (String str : strs) {
            if (isEmptyString(str)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Get a space string with specified size
     * 
     * @param size target size
     * 
     * @return space string with specified size
     */
    public static String getSpacesString(int size) {
        return getCharacterString(size, ' ');
    }
    
    /**
     * Get a string with specified character & size
     * 
     * @param size target size
     * @param c target character
     * 
     * @return string with specified character & size
     */
    public static String getCharacterString(int size, char c) {
        char[] spaces = new char[size];
        Arrays.fill(spaces, c);
        return new String(spaces);
    }
    
    /**
     * Check whether a character is a full scale character (not precise)
     * 
     * @param c character to be checked
     * 
     * @return check result
     */
    public static boolean isDoubleByte(char c) {
        return c >= 0x800;
    }
    
    /**
     * Determine the scale size of a string
     * 
     * @param str target string
     * 
     * @return scale size
     */
    public static int getStringSize(String str) {
        if (str == null) {
            return 0;
        } else {
            int l = 0;
            for (int i = 0; i < str.length(); i++) {
                l += (isDoubleByte(str.charAt(i)))? 2 : 1;
            }
            return l;
        }
    }
    
    /**
     * Check whether two object's toString are equals (null safe, if both are null return true)
     * 
     * @param s1 target string 1
     * @param s2 target string 2
     * 
     * @return check result
     */
    public static boolean equals(Object s1, Object s2) {
        if (s1 == null) {
            return s2 == null || s2.toString().equals("");
        } else if (s2 == null) {
            return s1.toString().equals("");
        } else {
            return s1.equals(s2);
        }
    }
    
    /**
     * Get a part of a string after splitting
     * 
     * @param str string to split
     * @param splitStr split string (not regex)
     * @param index part index
     * 
     * @return string part, null if not found
     */
    public static String getPartOfStr(String str, String splitStr, int index) {
        List<String> strs = splitStringToList(str, Pattern.quote(splitStr));
        if (index >= 0 && index < strs.size()) {
            // Ensure new string instance
            return cloneString(strs.get(index));
        } else {
            return null;
        }
    }
    
    /**
     * Truncate a string to first occurrence of [find string]
     * 
     * @param str string to truncate
     * @param findStr find string
     * 
     * @return truncate result, if not found return original string
     */
    public static String truncateToFirstOccurrence(String str, String findStr) {
        if (str != null) {
            if (findStr == null || findStr.length() == 0 || str.length() < findStr.length()) {
                return str;
            } else {
                int index = str.indexOf(findStr);
                if (index > 0) {
                    return str.substring(0, index);
                } else {
                    return str;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Truncate a string to first occurrence of specified strings
     *
     * @param str string truncate
     * @param findStrs find strings
     *
     * @return truncate result, if not found return original string
     */
    public static String truncateToFirstOccurrence(String str, String ... findStrs) {
        if (str != null) {
            if (findStrs == null || findStrs.length == 0) {
                return str;
            } else {
                int minMatchIndex = -1;

                for (String findStr : findStrs) {
                    int index = str.indexOf(findStr);

                    if (minMatchIndex == -1 || index < minMatchIndex) {
                        minMatchIndex = index;
                    }
                }

                if (minMatchIndex != -1) {
                    return str.substring(0, minMatchIndex);
                } else {
                    return str;
                }
            }
        } else {
            return null;
        }
    }
    
    /**
     * Check whether two string are equals, null safe
     * 
     * @param s1 target string 1
     * @param s2 target string 2
     * 
     * @return check result
     */
    public static boolean equalsNullSafe(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }
    
    /**
     * Check a string matches any of the specified strings
     * 
     * @param str target string
     * @param strs strings to match
     * 
     * @return match result
     */
    public static boolean stringMatchOnce(String str, String ... strs) {
        return stringMatchOnce(str, false, strs);
    }
    
    /**
     * Check a string matches any of the specified strings
     * 
     * @param str target string
     * @param strs strings to match
     * 
     * @return match result
     */
    public static boolean stringMatchOnceIgnoreCase(String str, String ... strs) {
        return stringMatchOnce(str, true, strs);
    }
    
    /**
     * Check a string matches any of the specified strings 
     * 
     * @param str target string
     * @param ignoreCase ignore case flag
     * @param strs strings to match
     * 
     * @return match result
     */
    public static boolean stringMatchOnce(String str, boolean ignoreCase, String ... strs) {
        if (str != null) {
            for (int i = 0; i < strs.length; i++) {
                String s = strs[i];
                boolean equal = (ignoreCase)? str.equalsIgnoreCase(s) : str.equals(s);
                if (equal) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check a string matches any of the specified strings
     * 
     * @param str target string
     * @param strs strings to match
     *  
     * @return match result
     */
    public static boolean stringMatchOnce(String str, Collection<String> strs) {
        return stringMatchOnce(str, false, strs);
    }
    
    /**
     * Check a string matches any of the specified strings, case insensitive
     * 
     * @param str target string
     * @param strs strings to match
     * 
     * @return match result
     */
    public static boolean stringMatchOnceIgnoreCase(String str, Collection<String> strs) {
        return stringMatchOnce(str, true, strs);
    }
    
    /**
     * Check a string matches any of the specified strings
     * 
     * @param str target string
     * @param ignoreCase ignore case flag
     * @param strs strings to match
     * 
     * @return match result
     */
    public static boolean stringMatchOnce(String str, boolean ignoreCase, Collection<String> strs) {
        if (str != null) {
            for (String s : strs) {
                boolean equal = (ignoreCase)? str.equalsIgnoreCase(s) : str.equals(s);
                if (equal) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check two string are equal, null will be treated as empty string: ""
     * 
     * @param str1 target string 1
     * @param str2 target string 2
     * 
     * @return check result
     */
    public static boolean stringEquals(String str1, String str2) {
        return stringEquals(str1, str2, false);
    }
    
    /**
     * Check two string are equal, null will be treated as empty string: ""
     * 
     * @param str1 target string 1
     * @param str2 target string 2
     * 
     * @return check result
     */
    public static boolean stringEqualsIgnoreCase(String str1, String str2) {
        return stringEquals(str1, str2, true);
    }

    /**
     * Check two strings' equality with ignore case & ignore width flags (ignore half / full width character), null string will be treated as empty string
     * 
     * @param str1 target string 1
     * @param str2 target string 2
     * @param ignoreCase ignore case flag
     * @param ignoreWidth ignore width flag
     * @return check result
     */
    public static boolean stringEquals(String str1, String str2, boolean ignoreCase, boolean ignoreWidth) {
        return stringCompare(str1, str2, ignoreCase, ignoreWidth) == 0;
    }

    /**
     * Compare two string with ignore case & ignore width (ignore half / full width character), null string will be treated as empty string
     * 
     * @param str1 target string 1
     * @param str2 target string 2
     * @param ignoreCase ignore case flag
     * @param ignoreWidth ignore width flag
     * @return compare result
     */
    public static int stringCompare(String str1, String str2, boolean ignoreCase, boolean ignoreWidth) {
        if (str1 == null) {
            str1 = "";
        }
        
        if (str2 == null) {
            str2 = "";
        }
        
        int len1 = str1.length(), len2 = str2.length(), lmt = Math.min(len1, len2);

        for (int i = 0; i < lmt; i++) {
            char ch1 = str1.charAt(i), ch2 = str2.charAt(i);

            if (ignoreWidth) {
                ch1 = convertFullWidthCharacterToHalfWidthCharacter(ch1);
                ch2 = convertFullWidthCharacterToHalfWidthCharacter(ch2);
            }

            if (ignoreCase) {
                ch1 = Character.toLowerCase(ch1);
                ch2 = Character.toLowerCase(ch2);
            }

            if (ch1 != ch2) {
                return ch1 - ch2;
            }
        }

        return len1 - len2;
    }
    
    /**
     * Check two string equal, null will be treated as empty string: ""
     * 
     * @param str1 target string 1
     * @param str2 target string 2
     * @param ignoreCase ignore case flag
     * 
     * @return check result
     */
    public static boolean stringEquals(String str1, String str2, boolean ignoreCase) {
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        return (ignoreCase)? str1.equalsIgnoreCase(str2) : str1.equals(str2);
    }

    /**
     * Build a string filled with the specified character and length
     *
     * @param ch target character
     * @param length target length
     * @return string filled with the specified character and length
     */
    public static String buildString(char ch, int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(ch);
        }

        return builder.toString();
    }
    
    /**
     * Build a string from the specified character array
     * 
     * @param cs source character array
     * @param s start index
     * @param e end index (exclusive)
     * 
     * @return result string
     */
    public static String buildString(char[] cs, int s, int e) {
        return buildString(cs, s, e, false, true);
    }
    
    /**
     * Build a string from the specified character array
     * 
     * @param cs source character array
     * @param s start index
     * @param e end index (exclusive)
     * @param trim trim flag, true to trim the result string
     * 
     * @return result string
     */
    public static String buildString(char[] cs, int s, int e, boolean trim) {
        return buildString(cs, s, e, trim, true);
    }
    
    /**
     * Build a string from the specified character array
     * 
     * @param cs source character array
     * @param s start index
     * @param e end index (exclusive)
     * @param trim trim flag, true to trim result string
     * @param containsSpecialEntity special entity flag, true to convert any special entity
     * 
     * @return result string
     */
    public static String buildString(char[] cs, int s, int e, boolean trim, boolean containsSpecialEntity) {
        String str = new String(cs, s, e - s);
        if (containsSpecialEntity) {
            str = convertSpecialEntityString(str);
        }
        return (trim)? str.trim() : str;
    }
    
    /**
     * Construct a representation string of the collection: [element, element, ...]
     * 
     * @param c target collection
     * 
     * @return representation string
     */
    public static String getCollectionRepresentString(Collection<Object> c) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Object o : c) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            if (o != null) {
                sb.append(o.toString());
            }
        }
        sb.append(']');
        return sb.toString();
    }
    
    /**
     * Count lines of the input string
     * 
     * @param str input string
     * 
     * @return line count
     */
    public static int countLines(String str) {
        if (str == null) {
            return 1;
        }
        int counts = 1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                counts++;
            }
        }
        return counts;
    }

    /**
     * Returns a new string that is a substring of [str].
     *
     * @param str original string
     * @param start start index
     * @param endExclusive end index (exclusive), if exceeds str's length return substring from start till the end.
     * @return substring of original string
     */
    public static String safeSubstring(String str, int start, int endExclusive) {
        if (start >= str.length() || endExclusive <= start) {
            return "";
        }

        if (endExclusive > str.length()) {
            return cloneString(str.substring(start));
        } else {
            return cloneString(str.substring(start, endExclusive));
        }
    }
    
    /**
     * Split a string with regex delimiter
     *
     * @param inStr input string
     * @param regex regex delimiter
     * @param ensureNewString ensure each substring is a new string instance (avoid substring holding reference to original string)
     *
     * @return splitted strings in array form
     */
    public static String[] splitString(String inStr, Pattern regex, boolean ensureNewString, boolean ignoreEmpty) {
        List<String> list = splitStringToList(inStr, regex, ensureNewString, ignoreEmpty);
        String[] strs = new String[list.size()];
        list.toArray(strs);
        return strs;
    }
     
    /**
     * Split a string with regex delimiter
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * @param ensureNewString ensure each substring is a new string instance (avoid substring holding reference to original string)
     *
     * @return splitted strings in array form
     */
    public static String[] splitString(String inStr, String regex, boolean ensureNewString, boolean ignoreEmpty) {
        return splitString(inStr, Pattern.compile(regex), ensureNewString, ignoreEmpty);
    }

    /**
     * Split a string with regex delimiter
     * (optimization apply but substring will hold reference of original string's char[])
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * 
     * @return splitted strings in array form
     */
    public static String[] splitString(String inStr, Pattern regex) {
        return splitString(inStr, regex, false, true);
    }
     
    /**
     * Split a string with regex delimiter
     * (optimization apply but substring will hold reference of original string's char[])
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * 
     * @return splitted strings in array form
     */
    public static String[] splitString(String inStr, String regex) {
        return splitString(inStr, regex, false, true);
    }
     
    /**
     * Split a string with regex delimiter
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * @param ensureNewString ensure each substring is a new string instance (avoid substring holding reference to original string)
     * @param ignoreEmpty skip empty entries
     *
     * @return splitted strings in list form
     */
    public static List<String> splitStringToList(String inStr, Pattern regex, boolean ensureNewString, boolean ignoreEmpty) {
        Matcher matcher = regex.matcher(inStr);
        
        List<String> list = new ArrayList<String>();
         
        int i = 0;
        while (matcher.find()) {
            String s = inStr.substring(i, matcher.start());

            String newS = (ensureNewString)? cloneString(s) : s;

            if (!StringUtil.isEmptyString(newS)) {
                list.add(newS);
            }

            i = matcher.end();
        }
         
        if (i < inStr.length()) {
            list.add((ensureNewString)? cloneString(inStr.substring(i)) : inStr.substring(i));
        }

        return list;
    }
     
    /**
     * Split a string with regex delimiter
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * @param ensureNewString ensure each substring is a new string instance (avoid substring holding reference to original string)
     * @param ignoreEmpty ignore empty entries
     *
     * @return splitted strings in list form
     */
    public static List<String> splitStringToList(String inStr, String regex, boolean ensureNewString, boolean ignoreEmpty) {
        return splitStringToList(inStr, Pattern.compile(regex), ensureNewString, ignoreEmpty);
    }
     
    /**
     * Split a string with regex delimiter
     * (optimization apply but substring will hold reference of original string's char[])
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * 
     * @return splitted strings in list form
     */
    public static List<String> splitStringToList(String inStr, String regex) {
        return splitStringToList(inStr, Pattern.compile(regex), false, true);
    }
     
    /**
     * Split a string with regex delimiter
     * (optimization apply but substring will hold reference of original string's char[])
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * 
     * @return splitted strings in list form
     */
    public static List<String> splitStringToList(String inStr, Pattern regex) {
        return splitStringToList(inStr, regex, false, true);
    }

    /**
     * Split a string with fixed length interval
     *
     * @param inStr input string
     * @param len interval length
     *
     * @return splitted strings in list form
     */
    public static List<String> splitStringToList(String inStr, int len) {
        return splitStringToList(inStr, len, false);
    }

    /**
     * Split a string with fixed length interval
     *
     * @param inStr input string
     * @param len interval length
     * @param ensureNewString ensure each string part is an independent new string to avoid substring holding reference to original string
     *
     * @return splitted strings in list form
     */
    public static List<String> splitStringToList(String inStr, int len, boolean ensureNewString) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < inStr.length(); i+=2) {
            if (i + 2 < inStr.length()) {
                result.add((ensureNewString)? cloneString(inStr.substring(i, i+2)) : inStr.substring(i, i+2));
            } else {
                result.add((ensureNewString)? cloneString(inStr.substring(i)) : inStr.substring(i));
            }
        }

        return result;
    }

    /**
     * Clone target string, this ensure a string instance is created
     *
     * @param str string to clone
     * @return new instance of string object
     */
    public static String cloneString(String str) {
        return new String(str.toCharArray());
    }

    /**
     * Split a string with regex delimiter, into an integer array
     * If splitted string can't convert to integer result will be set to null instead of partial array.
     * 
     * @param inStr input string
     * @param regex regex delimiter
     * 
     * @return splitted strings in integer array
     */
    public static int[] splitInteger(String inStr, String regex) {
        if (inStr == null) {
            return null;
        } else {
            List<String> parts = splitStringToList(inStr, regex);
            int[] result = new int[parts.size()];
            for (int i = 0; i < parts.size(); i++) {
                try {
                    result[i] = Integer.parseInt(parts.get(i));
                } catch (NumberFormatException nfe) {
                    return null;
                }
            }
            return result;
        }
    }
    
    /**
     * Construct a random String from the specified character source
     * 
     * @param length target length
     * @param characterArray source character array
     * 
     * @return constructed random string
     */
    public static String getRandomString(int length, char[] characterArray) {
        return new String(getRandomChars(length, characterArray));
    }
    
    /**
     * Construct a random string with default character source: [a-zA-Z0-9]
     * 
     * @param length target length
     * 
     * @return constructed random string
     */
    public static String getRandomString(int length) {
        return getRandomString(length, CHARS);
    }

    /**
     * Construct a random char array with default character source: [a-zA-Z0-9]
     *
     * @param length target array length
     * @return constructed random char array
     */
    public static char[] getRandomChars(int length) {
        return getRandomChars(length, CHARS);
    }

    /**
     * Construct a random char array with the specified character source
     *
     * @param length target array length
     * @param characterArray character source array
     * @return constructed random char array
     */
    public static char[] getRandomChars(int length, char[] characterArray) {
        if (length <= 0) {
            return new char[0];
        } else{
            int minIndex= 0, maxIndex = characterArray.length - 1;
            char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = characterArray[MathUtil.randomInteger(minIndex, maxIndex)];
            }
            return chars;
        }
    }
     
     /**
      * Construct random strings with default character source: [a-zA-Z0-9]
      * 
      * @param stringLength target string length
      * @param numOfStrings target number of strings 
      * 
      * @return random strings
      */
     public static String[] getRandomStrings(int stringLength, int numOfStrings) {
         return getRandomStrings(stringLength, numOfStrings, false);
     }
     
     /**
      * Construct random strings with default character: [a-zA-Z0-9]
      * 
      * @param stringLength target string length
      * @param numOfStrings target number of strings
      * @param unique unique flag, if true each constructed string will be unique
      *  
      * @return random strings
      */
     public static String[] getRandomStrings(int stringLength, int numOfStrings, boolean unique) {
         return getRandomStrings(stringLength, numOfStrings, unique, CHARS);
     }
     
     /**
      * Construct random strings with specified character source
      * 
      * @param stringLength target string length
      * @param numOfStrings target number of strings
      * @param unique unique flag, if true each constructed string will be unique
      * @param characterArray source character array
      * 
      * @return random strings
      */
     public static String[] getRandomStrings(
             int stringLength, int numOfStrings, boolean unique, char[] characterArray
     ) {
         if (numOfStrings <= 0) {
             return new String[0];
         } else {
             String[] results = null;
             if (unique) {
                 Set<String> resultSet = new HashSet<String>();
                 while (resultSet.size() < numOfStrings) {
                     resultSet.add(getRandomString(stringLength, characterArray));
                 }
                 results = new String[resultSet.size()];
                 resultSet.toArray(results);
             } else {
                 results = new String[numOfStrings];
                 for (int i = 0; i < results.length; i++) {
                     results[i] = getRandomString(stringLength, characterArray);
                 }
             }
             return results;
         }
     }
     
     /**
      * null safe toString of an object
      * 
      * @param obj target object
      * 
      * @return object's toString, if null return ""
      */
     public static String toString(Object obj) {
         return toString(obj, false);
     }
     
     /**
      * Replace single quote will repeated single quote
      * 
      * @param sqlString target string
      * 
      * @return result string
      */
     public static String sqlAddQuote(String sqlString) {
         return sqlString.replaceAll("'", "''");
     }
     
     /**
      * Get a hex rgb string from a color (RRGGBB)
      * 
      * @param c target color
      * 
      * @return hex rgb string
      */
     public static String getColorHexString(Color c) {
         int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
         String hex = Integer.toHexString((r<<16)+(g<<8)+b);
         while (hex.length() < 6) {
             hex = "0" + hex;
         }
         return hex;
     }
     
     /**
      * null toString of an object
      * 
      * @param obj target object
      * @param nullable nullable flag, if true, null will be returned for a null object, if false, "" will be returned for null object
      * 
      * @return object's toString or null/"" for null object
      */
     public static String toString(Object obj, boolean nullable) {
         if (obj != null) {
             return obj.toString();
         } else {
             return (nullable)? null : "";
         }
     }
     
     /**
      * Get formated string
      * The result will be the same as new Formatter().format(formatStr, params).toString();
      * 
      * @param formatStr format string, e.g. "%s%d"
      * @param params input parameters
      * 
      * @return formatted string
      */
     public static String getFormatString(String formatStr, Object ... params) {
         return new Formatter().format(formatStr, params).toString();
     }
     
     /**
      * Check whether the input string is a valid java identifier
      * 
      * @param str input string
      * 
      * @return check result
      */
     public static boolean isValidJavaIdentifier(String str) {
         if (str.length() > 0) {
             if (Character.isJavaIdentifierStart(str.charAt(0))) {
                 boolean validPart = true;
                 for (int i = 1; i < str.length(); i++) {
                     if (!Character.isJavaIdentifierPart(str.charAt(i))) {
                         validPart = false;
                         break;
                     }
                 }
                 return validPart;
             } else {
                 return false;
             }
         } else {
             return false;
         }
     }
    
    /**
     * Convert special entity character within a string
     * 
     * @param input target string
     * 
     * @return converted string
     */
    public static String convertSpecialEntityString(String input) {
        Pattern pattern = Pattern.compile("&#[0-9]+;");
        Matcher matcher = pattern.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String integerPart = input.substring(matcher.start()+2, matcher.end()-1);
            matcher.appendReplacement(buffer, Character.toString((char) Integer.parseInt(integerPart)));
        }
        matcher.appendTail(buffer);
        if (buffer.length() > 0) {
            return buffer.toString();
        } else {
            return input;
        }
    }
    
    /**
     * Convert unicode character entities
     * 
     * @param input string
     * 
     * @return converted string
     */
    public static String convertUnicodeCharacterString(String input) {
        Pattern pattern = Pattern.compile("\\\\u[0-9A-Fa-f]+");
        Matcher matcher = pattern.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String integerPart = input.substring(matcher.start()+2, matcher.end());
            matcher.appendReplacement(buffer, Character.toString((char) Integer.parseInt(integerPart, 16)));
        }
        matcher.appendTail(buffer);
        if (buffer.length() > 0) {
            return buffer.toString();
        } else {
            return input;
        }
    }

    /**
     * Convert non ascii character into unicode representation
     *
     * @param str target string
     * @return converted string
     */
    public static String convertToUnicodeRepresentation(String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.BASIC_LATIN) {
                builder.append("\\u").append(Integer.toHexString(ch));
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }
    
    /**
     * Read next hex value from the string reader
     * 
     * @param reader string reader
     * 
     * @return next hex value
     */
    private static int nextHex(StringReader reader) {
        try {
            int byt = reader.read();
            
            while (byt != -1) {
                if (byt >= '0' && byt <= '9') {
                    return byt - 48;
                } else if (byt >= 'A' && byt <= 'F') {
                    return byt - 55;
                } else if (byt >= 'a' && byt <= 'f') {
                    return byt - 87;
                } else {
                    byt = reader.read();
                }
            }
            
            return -1;
        } catch (IOException iox) {
            // Not expected exception, dummy catch block, return -1 anyway
            return -1;
        }
    }
    
    /**
     * Read next hex byte from the string reader
     * 
     * @param reader string reader
     * 
     * @return next hex byte
     */
    private static int nextHexByte(StringReader reader) {
        int v1 = nextHex(reader), v2 = nextHex(reader);

        if (v1 == -1 || v2 == -1) {
            return -1;
        } else {
            return (((v1 << 4) & 0xF0) | (v2 & 0xF));
        }
    }
    
    /**
     * Convert a hex string to byte array
     * 
     * @param hexString hex string
     * 
     * @return result byte array
     */
    public static byte[] hexToBytes(String hexString) {
        StringReader reader = new StringReader(hexString);
        
        List<Byte> byteList = new ArrayList<Byte>(hexString.length() / 2);
        
        int byt = nextHexByte(reader);
        
        while (byt != -1) {
            byteList.add((byte) (byt & 0xFF));
            byt = nextHexByte(reader);
        }
        
        byte[] bytes = new byte[byteList.size()];
        
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        
        return bytes;
    }
    
    /**
     * Format a byte array to hex string
     * 
     * @param bytes byte array
     * @param bytesPerRow byte count for each row, 0 for unlimited
     * 
     * @return resulted hex string
     */
    public static String formatBytesIntoHex(byte[] bytes, int bytesPerRow) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < bytes.length; i++) {
            if (bytesPerRow > 0 && i > 0 && i % bytesPerRow == 0) {
                builder.append('\n');
            }
            byte byt = bytes[i];
            builder.append(HEX_CHARS[byt >>> 4 & 0xF]);
            builder.append(HEX_CHARS[byt & 0xF]);
        }
        
        return builder.toString();
    }

    /**
     * Format a byte buffer into hex string
     *
     * @param buf byte buffer
     * @param bytesPerRow byte count for each row, 0 for unlimited
     *
     * @return resulted hex string
     */
    public static String formatBytesIntoHex(ByteBuffer buf, int bytesPerRow) {
        if (buf == null || !buf.hasRemaining()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();

            int c = 0;
            while (buf.hasRemaining()) {
                if (bytesPerRow > 0 && c == bytesPerRow) {
                    builder.append('\n');
                    c = 0;
                }
                byte byt = buf.get();
                builder.append(HEX_CHARS[byt >>> 4 & 0xF]);
                builder.append(HEX_CHARS[byt & 0xF]);
                c++;
            }

            return builder.toString();
        }
    }
    
    /**
     * Format a byte array to hex string
     * 
     * @param bytes byte array
     * 
     * @return resulted hex string
     */
    public static String getHexStringFromBytes(byte[] bytes) {
        return formatBytesIntoHex(bytes, 0);
    }

    /**
     * Format byte buffer to hex string
     *
     * @param buf byte buffer
     *
     * @return resulted hex string
     */
    public static String getHexStringFromBytes(ByteBuffer buf) {
        return formatBytesIntoHex(buf, 0);
    }
    
    /**
     * Extract underlying string from attributed string
     * 
     * @param attrStr attributed string
     * 
     * @return extracted string
     */
    public static String extractUnderlyingStringFromAttributedString(AttributedString attrStr) {
        AttributedCharacterIterator attrIter = attrStr.getIterator();
        
        StringBuilder builder = new StringBuilder();
        char nextChar = attrIter.current();
        while (nextChar != CharacterIterator.DONE) {
            builder.append(nextChar);
            nextChar = attrIter.next();
        }
        return builder.toString();
    }
    
    /**
     * Split attributed string
     * 
     * @param attrStr attributed string
     * @param regex split regex
     * 
     * @return splitted attributed strings
     */
    public static List<AttributedString> splitAttributedString(AttributedString attrStr, String regex) {
        AttributedCharacterIterator attrIter = attrStr.getIterator();
        String t = extractUnderlyingStringFromAttributedString(attrStr);
        ArrayList<AttributedString> newAttrStrs = new ArrayList<AttributedString>();
        Matcher matcher = Pattern.compile(regex).matcher(t);
        int currentIndex = 0;
        while (matcher.find()) {
            int sIndex = matcher.start(), eIndex = matcher.end();
            newAttrStrs.add(new AttributedString(attrIter, currentIndex, sIndex));
            currentIndex = eIndex;
        }
        if (currentIndex < t.length()) {
            newAttrStrs.add(new AttributedString(attrIter, currentIndex, t.length()));
        }
        return newAttrStrs;
    }

    /**
     * Join specified parts with specified join word into one string
     *
     * @param joinWord join word
     * @param parts parts to be joined
     * @return joined string
     */
    public static String join(String joinWord, Object ... parts) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                builder.append(joinWord);
            }
            builder.append(String.valueOf(parts[i]));
        }
        return builder.toString();
    }

    /**
     * Join array into one string
     *
     * @param arr array data
     * @return joined string
     */
    public static <T> String join(T[] arr) {
        return join(arr, ',');
    }

    /**
     * Join array with specified character into one string
     *
     * @param arr array data
     * @param ch join character
     * @return joined string
     */
    public static <T> String join(T[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        appendArrayData(builder, arr, ch);

        return builder.toString();
    }

    /**
     * Join array with specified join word into tone string
     *
     * @param arr array data
     * @param str join word
     *
     * @return joined string
     */
    public static <T> String join(T[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        appendArrayData(builder, arr, str);

        return builder.toString();
    }

    private static <T> void appendArrayData(StringBuilder builder, T[] arr, Object joinWord) {
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(String.valueOf(joinWord));
            }
            if (arr[i] == null) {
                builder.append("null");
            } else {
                builder.append(arr[i].toString());
            }
        }
    }

    /**
     * Join integer array into one string, separated with comma
     *
     * @param arr integer array
     * @return joined string
     */
    public static String join(int[] arr) {
        return join(arr, ',');
    }

    /**
     * Join integer array with specified join character
     *
     * @param arr integer array
     * @param ch join character
     * @return joined string
     */
    public static String join(int[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join integer array with specified join word
     *
     * @param arr integer array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(int[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join double array with comma
     *
     * @param arr double array
     * @return joined string
     */
    public static String join(double[] arr) {
        return join(arr, ',');
    }

    /**
     * Join double array with specified character
     *
     * @param arr double array
     * @param ch join character
     *
     * @return joined string
     */
    public static String join(double[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join double array with specified join word
     *
     * @param arr double array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(double[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join float array with comma
     *
     * @param arr float array
     *
     * @return joined string
     */
    public static String join(float[] arr) {
        return join(arr, ',');
    }

    /**
     * Join float array with specified join character
     *
     * @param arr float array
     * @param ch join character
     *
     * @return joined string
     */
    public static String join(float[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join float array with specified join word
     *
     * @param arr float array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(float[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join long(primitive type) array with comma
     *
     * @param arr long(primitive type) array
     *
     * @return joined string
     */
    public static String join(long[] arr) {
        return join(arr, ',');
    }

    /**
     * Join long(primitive type) array with specified join character
     *
     * @param arr long (primitive type) array
     * @param ch join character
     * @return joined string
     */
    public static String join(long[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join long (primitive type) array with specified join word
     *
     * @param arr long (primitive type) array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(long[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join short (primitive type) array with comma
     *
     * @param arr short (primitive type) array
     *
     * @return join string
     */
    public static String join(short[] arr) {
        return join(arr, ',');
    }

    /**
     * Join short (primitive type) array with specified character
     *
     * @param arr short (primitive type) array
     * @param ch join character
     *
     * @return joined string
     */
    public static String join(short[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join short (primitive type) array with specified join word
     *
     * @param arr short (primitive type) array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(short[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join boolean array with comma
     *
     * @param arr boolean array
     *
     * @return joined string
     */
    public static String join(boolean[] arr) {
        return join(arr, ',');
    }

    /**
     * Join boolean array with specified join character
     *
     * @param arr boolean array
     * @param ch join character
     *
     * @return joined string
     */
    public static String join(boolean[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join boolean array with specified join word
     *
     * @param arr boolean array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(boolean[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join character array with comma
     *
     * @param arr character array
     *
     * @return joined string
     */
    public static String join(char[] arr) {
        return join(arr, ',');
    }

    /**
     * Join character array with specified join character
     *
     * @param arr character array
     * @param ch join character
     *
     * @return joined string
     */
    public static String join(char[] arr, char ch) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(ch);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join character array with specified join word
     *
     * @param arr character array
     * @param str join word
     *
     * @return joined string
     */
    public static String join(char[] arr, String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(str);
            }
            builder.append(arr[i]);
        }

        return builder.toString();
    }

    /**
     * Join a collection with comma
     *
     * @param datas collection data
     *
     * @return joined string
     */
    public static <T> String join(Collection<T> datas) {
        return join(datas, ',');
    }

    /**
     * Join a collection with specified join character
     *
     * @param datas collection data
     * @param ch join character
     *
     * @return joined string
     */
    public static <T> String join(Collection<T> datas, char ch) {
        StringBuilder builder = new StringBuilder();

        appendDataCollection(builder, datas, ch);

        return builder.toString();
    }

    /**
     * Join a collection with specified join word
     *
     * @param datas collection data
     * @param str join word
     *
     * @return joined string
     */
    public static <T> String join(Collection<T> datas, String str) {
        StringBuilder builder = new StringBuilder();

        appendDataCollection(builder, datas, str);

        return builder.toString();
    }

    private static <T> void appendDataCollection(StringBuilder builder, Collection<T> datas, Object joinWord) {
        for (T data : datas) {
            if (builder.length() > 0) {
                builder.append(String.valueOf(joinWord));
            }
            if (data != null) {
                builder.append(data.toString());
            }
        }
    }

    /**
     * Append specified prefix to a string until targeted length
     *
     * @param v reference string
     * @param prefix prefix
     * @param targetLength target length
     *
     * @return resulted string
     */
    public static String fillPrefix(String v, String prefix, int targetLength) {
        if (v.length() >= targetLength) {
            return v;
        } else {
            StringBuilder builder = new StringBuilder(prefix);

            while (builder.length() < targetLength-v.length()) {
                builder.append(prefix);
            }

            return builder.append(v).toString();
        }
    }

    /**
     * Check whether a string starts with a pattern
     *
     * @param v string to check
     * @param pattern pattern to check
     *
     * @return check result
     */
    public static boolean startsWith(String v, String pattern) {
        return startsWith(v, pattern, false);
    }

    /**
     * Check whether a string starts with a pattern with ignore case control flag
     *
     * @param v string to check
     * @param pattern pattern to check
     * @param ignoreCase ignore case flag
     *
     * @return check result
     */
    public static boolean startsWith(String v, String pattern, boolean ignoreCase) {
        if (ignoreCase) {
            return v.toLowerCase().startsWith(pattern.toLowerCase());
        } else {
            return v.startsWith(pattern);
        }
    }

    /**
     * Check whether a string ends with a pattern
     *
     * @param v string to check
     * @param pattern pattern to check
     *
     * @return check result
     */
    public static boolean endsWith(String v, String pattern) {
        return endsWith(v, pattern, false);
    }

    /**
     * Check whether a string ends with a pattern with ignore case control flag
     *
     * @param v string to check
     * @param pattern pattern to check
     * @param ignoreCase ignore case flag
     *
     * @return check result
     */
    public static boolean endsWith(String v, String pattern, boolean ignoreCase) {
        if (ignoreCase) {
            return v.toLowerCase().endsWith(pattern.toLowerCase());
        } else {
            return v.endsWith(pattern);
        }
    }

    /**
     * Trim a string to last occurrence of a pattern
     *
     * @param v string to trim
     * @param postfix postfix pattern
     *
     * @return resulted string
     */
    public static String trimPostfix(String v, String postfix) {
        return trimPostfix(v, postfix, false);
    }

    /**
     * Trim a string to last occurrence of a pattern with ignore case control flag
     *
     * @param v string to trim
     * @param postfix postfix pattern
     *
     * @return resulted string
     */
    public static String trimPostfix(String v, String postfix, boolean ignoreCase) {
        if (endsWith(v, postfix, ignoreCase)) {
            return v.substring(0, v.length()-postfix.length());
        } else {
            return v;
        }
    }

    /**
     * Convert half with character to full width character
     * 
     * @param ch
     * @return
     */
    public static char convertHalfWidthCharacterToFullWidthCharacter(char ch) {
        if (ch == ' '){
            return '　';
        } else if (ch >= HALF_WIDTH_CODE_POINT_LOWER_BOUND && ch <= HALF_WIDTH_CODE_POINT_UPPER_BOUND) {
            return (char)(ch+HALF_TO_FULL_CODE_POINT_DIFF);
        } else {
            return ch;
        }
    }

    /**
     * Convert full width character to half width character
     * 
     * @param ch input character
     * @return converted character
     */
    public static char convertFullWidthCharacterToHalfWidthCharacter(char ch) {
        if (ch == '　') {
            return ' ';
        } else if (ch >= FULL_WIDTH_CODE_POINT_LOWER_BOUND && ch <= FULL_WIDTH_CODE_POINT_UPPER_BOUND) {
            return (char)(ch - HALF_TO_FULL_CODE_POINT_DIFF);
        } else {
            return ch;
        }
    }

    /**
     * Convert any half with character to full width character
     * 
     * @param str input string
     * @return converted string
     */
    public static String convertHalfWidthCharacterToFullWidthCharacter(String str) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            
            builder.append(convertHalfWidthCharacterToFullWidthCharacter(ch));
        }
        
        return builder.toString();
    }

    /**
     * Convert any full width character to half with character
     * 
     * @param str input string
     * @return converted string
     */
    public static String convertFullWidthCharacterToHalfWidthCharacter(String str) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            
            builder.append(convertFullWidthCharacterToHalfWidthCharacter(ch));
        }
        
        return builder.toString();
    }
}
