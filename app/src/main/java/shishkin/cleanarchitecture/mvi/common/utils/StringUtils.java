package shishkin.cleanarchitecture.mvi.common.utils;

import androidx.annotation.NonNull;
import android.util.Base64;
import android.util.SparseArray;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class StringUtils {
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    private static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static Pattern pattern;

    private StringUtils() {
    }

    public static boolean isNullOrEmpty(final CharSequence cs) {
        return (cs == null || cs.toString().trim().length() < 1);
    }

    public static String byteArrayToString(byte[] array) {
        if (array == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append((char) array[i]);
        }
        return sb.toString();
    }

    public static String removeTrailingSpaces(byte[] string) {
        String result = EMPTY;
        try {
            result = (new String(string, "UTF-8")).replaceAll(" *$", "");
        } catch (UnsupportedEncodingException e) {
        }
        return result;
    }

    public static int pos(final String initialString, final String which,
                          final int start) {
        if (initialString == null || which == null) {
            return -1;
        }
        if (start >= initialString.length()) {
            return -1;
        }
        return initialString.indexOf(which, start);
    }

    /**
     * Method int Pos(String string1, String string2) finds one string within
     * another string.
     *
     * @param initialString - The string in which you want to find string2<br>
     * @param which         - The string you want to find in string1<br>
     * @return returns a integer whose value is the starting position of the
     * first occurrence of string2 in string1. Start searching position
     * is 0. If string2 is not found in string1 or if start is not
     * within string1, Pos returns -1
     */
    public static int pos(final String initialString, final String which) {
        if (initialString == null || which == null) {
            return -1;
        }
        return initialString.indexOf(which);
    }

    /**
     * Method String Replace(String string1, int start, int n, String string2)
     * replaces a portion of one string with another
     *
     * @param initialString - The string in which you want to replace characters with
     *                      string2.<br>
     * @param start         - A integer whose value is the number of the first character
     *                      you want replaced. (The first character in the string is
     *                      number 0.)<br>
     * @param n             - A integer whose value is the number of characters you want
     *                      to replace.<br>
     * @param replaceWith   - The string that will replace characters in string1. The
     *                      number of characters in string2 can be greater than, equal to,
     *                      or less<br>
     *                      than the number of characters you are replacing.<br>
     * @return returns a integer whose value is the starting position of the
     * first occurrence of string2 in string1. Start searching position
     * is 0. If string2 is not found in string1 or if start is not
     * within string1, Pos returns 0
     */
    public static String replace(final String initialString, final int start,
                                 int n, final String replaceWith) {
        String s = EMPTY;

        if (initialString == null || replaceWith == null) {
            return null;
        }
        if (start > 0) {
            s = mid(initialString, 0, start);
        }

        if (n == 0) {
            n = replaceWith.length();
        }

        s += mid(replaceWith + fill(SPACE, n), 0, n);

        if (initialString.length() > s.length()) {
            s += initialString.substring(s.length());
        }
        return s;
    }

    /**
     * Method String Replace(String as_initial_string, String as_replace_it,
     * String as_replace_with) replaces a one string with another
     *
     * @param initialString - The string in which you want to replace characters with
     *                      as_replace_with.<br>
     * @param replaceString - The string is replaced with string as_replace_with<br>
     * @param replaceWith   - replacement string<br>
     * @return returns string in which string as_replace_it replaced with string
     * as_replace_with
     */
    public static String replace(final String initialString,
                                 final String replaceString, final String replaceWith) {
        // Verify parameters
        if (initialString == null || replaceString == null
                || replaceWith == null) {
            return EMPTY;
        }

        // Return intial string if what to replae and replace with are equals
        if (replaceString.equals(replaceWith)) {
            return initialString;
        }

        // Keep the value
        String temp = initialString;

        // Set initial value
        int startPos = 0;

        // Find the first occurrence of as_replace_it
        startPos = temp.indexOf(replaceString, startPos);

        // Only enter the loop if you find as_replace_it
        while (startPos >= 0) {
            // Replace as_replace_it with as_replace_with
            temp = temp.substring(0, startPos)
                    + replaceWith
                    + temp.substring(startPos + replaceString.length(),
                    temp.length());

            // Find the next occurrence of as_replace_it
            startPos = temp.indexOf(replaceString,
                    startPos + replaceWith.length());
        }

        return temp;
    }

    /**
     * Method String fill(String string1, int n) Builds a string by repeating
     * the specified string.
     *
     * @param str - A string whose value will be repeated to fill the return
     *            string<br>
     * @param n   - A integer whose value is the length of the string you want
     *            returned<br>
     * @return Returns a string n*string1.length characters filled with the
     * string in the argument string1.
     */
    public static String fill(final String str, final int n) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Method int Len(String string1) Reports the length of a string
     *
     * @param str - The string for which you want the length in number of
     *            characters<br>
     * @return Returns a integer whose value is the length of string1.
     */
    public static int len(final String str) {
        if (str == null) {
            return 0;
        }
        return str.length();
    }

    /**
     * Method String upper(String string1) Converts all the characters in a
     * string to uppercase
     *
     * @param str - The string you want to convert to uppercase letters<br>
     * @return Returns string with lowercase letters changed to uppercase
     */
    public static String upper(final String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase(Locale.getDefault());
    }

    /**
     * Method boolean Match(String string1, String pattern) Determines whether a
     * string's value contains a particular pattern of characters
     *
     * @param str     - The string in which you want to look for a pattern of
     *                characters<br>
     * @param pattern - A string whose value is the text pattern<br>
     * @return Returns true if string matches pattern and false if it does not
     */
    public static boolean match(final String str, final String pattern) {
        return str.matches(pattern);
    }

    /**
     * Method String mid(String string1, int first, int len) Obtains a specified
     * number of characters from a specified position in a string.
     *
     * @param str   - The string from which you want characters returned.<br>
     * @param first - A integer specifying the position of the first character you
     *              want returned.<br>
     * @param len   - A integer whose value is the number of characters you want
     *              returned.<br>
     * @return Returns characters specified in length of string starting at
     * character first.
     */
    public static String mid(final String str, final int first, int len) {
        if (str == null) {
            return EMPTY;
        }
        if (str.length() == 0) {
            return EMPTY;
        }
        if (first > str.length()) {
            return EMPTY;
        }
        if (first + len > str.length()) {
            len = str.length() - first;
        }
        return str.substring(first, first + len);
    }

    /**
     * Method String mid(String string1, int first) Obtains a specified number
     * of characters from a specified position in a string.
     *
     * @param str   - The string from which you want characters returned.<br>
     * @param first - A integer specifying the position of the first character you
     *              want returned.<br>
     * @return Returns characters specified in length of string starting at
     * character first.
     */
    public static String mid(final String str, final int first) {
        if (str == null) {
            return EMPTY;
        }
        if (str.length() == 0) {
            return EMPTY;
        }
        if (first > str.length()) {
            return EMPTY;
        }
        return str.substring(first, str.length());
    }

    /**
     * Method String token(String as_1, String as_2, int ai_1) Obtains a
     * specified token from a string.
     *
     * @param initialString - The string from which you want token returned.<br>
     * @param delimiters    - The string which contains delimiters of token.<br>
     * @param tokenNumber   - A integer specifying the number of the token in string as_1.<br>
     * @return Returns specifying token from string as_1.
     */
    public static String token(final String initialString, String delimiters,
                               final int tokenNumber) {
        if (initialString == null) {
            return EMPTY;
        }
        if (initialString.length() == 0) {
            return EMPTY;
        }
        if (delimiters == null) {
            return EMPTY;
        }
        if (delimiters.length() == 0) {
            return EMPTY;
        }

        if (delimiters.equals("(")) {
            delimiters = "\\(";
        }
        if (delimiters.equals(")")) {
            delimiters = "\\)";
        }

        String[] arr = initialString.split(delimiters, -1);
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (!isNullOrEmpty(arr[i])) {
                count++;
                if (count == tokenNumber) {
                    return arr[i];
                }
            }
        }
        return EMPTY;
    }

    /**
     * Method String tokenfirst(String as_1, String as_2) Obtains a first token
     * from a string.
     *
     * @param initialString - The string from which you want token returned.<br>
     * @param delimiters    - The string which contains delimiters of token.<br>
     * @return Returns specifying token from string as_1.
     */
    public static String tokenFirst(String initialString,
                                    final String delimiters) {
        if (initialString == null) {
            return EMPTY;
        }
        if (initialString.length() == 0) {
            return EMPTY;
        }
        if (delimiters == null) {
            return EMPTY;
        }
        if (delimiters.length() == 0) {
            return EMPTY;
        }

        while (true) {
            final int i = initialString.indexOf(delimiters);
            if (i < 0) {
                return initialString;
            } else if (i > 0) {
                return initialString.substring(0, i);
            } else if (i == 0) {
                initialString = mid(initialString, 1);
                if (initialString.length() == 0) {
                    return EMPTY;
                }
            }
        }
    }

    /**
     * Method int numtoken(String as_1, String as_2) Obtains a count tokens in a
     * string.
     *
     * @param initialString - The string from which you want count tokens returned.<br>
     * @param delimiters    - The string which contains delimiters of token.<br>
     * @return Returns count tokens in string as_1.
     */
    public static int numToken(final String initialString,
                               final String delimiters) {
        if (initialString == null) {
            return 0;
        }
        if (initialString.length() == 0) {
            return 0;
        }
        if (delimiters == null) {
            return 0;
        }
        if (delimiters.length() == 0) {
            return 0;
        }

        String[] arr = initialString.split(delimiters, -1);
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (!isNullOrEmpty(arr[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method String Replace(String as_1, int ai_1, String as_2) replaces a one
     * string with another
     *
     * @param initialString - The string in which you want to replace characters with as_2<br>
     * @param start         - A integer whose value is the number of the first character
     *                      you want replaced. (The first character in the string is
     *                      number 0.)<br>
     * @param replaceString - The string that will replace characters in as_1.<br>
     * @return returns string in which characters at position ai_1 are replaced
     * with string as_2
     */
    public static String replace(final String initialString, final int start,
                                 final String replaceString) {
        if (initialString == null) {
            return EMPTY;
        }
        if (initialString.length() == 0) {
            return EMPTY;
        }
        if (replaceString == null) {
            return EMPTY;
        }
        if (replaceString.length() == 0) {
            return EMPTY;
        }

        String s;
        if (start == 0) {
            s = replaceString + initialString.substring(1);
        } else {
            s = initialString.substring(0, start) + replaceString
                    + initialString.substring(start + replaceString.length());
        }
        return s;
    }

    /**
     * Method String atReplace(String as_1, int ai_1, int ai_2, String as_2)
     * replaces a one string with another at position
     *
     * @param initialString - The string in which you want to replace characters with as_2<br>
     * @param start         - A integer whose value is the number of the first character
     *                      you want replaced. (The first character in the string is
     *                      number 0.)<br>
     * @param length        - A integer whose value is the count of the characters you
     *                      want replaced.<br>
     * @param replaceString - The string that will replace characters in as_1.<br>
     * @return returns string in which characters at position ai_1 with count
     * equal ai_2 are replaced with string as_2
     */
    public static String replaceAt(final String initialString, final int start,
                                   final int length, final String replaceString) {
        if (initialString == null) {
            return EMPTY;
        }
        if (initialString.length() == 0) {
            return EMPTY;
        }
        if (replaceString == null) {
            return EMPTY;
        }
        if (replaceString.length() == 0) {
            return EMPTY;
        }

        String s;
        if (start == 0) {
            s = replaceString + initialString.substring(length);
        } else {
            s = initialString.substring(0, start) + replaceString
                    + initialString.substring(start + length);
        }
        return s;
    }

    /**
     * Method String Left(String s, int l) Obtains a specified number of
     * characters from the beginning of a string
     *
     * @param str    - The string you want to search.<br>
     * @param length - A integer specifying the number of characters you want to
     *               return.<br>
     * @return Returns the leftmost n characters in string
     */
    public static String left(final String str, final int length) {
        if (str == null) {
            return EMPTY;
        }
        if (length >= str.length()) {
            return str;
        }
        return str.substring(0, length);
    }

    /**
     * Method String Right(String s, int l) Obtains a specified number of
     * characters from the end of a string
     *
     * @param str - The string you want to search.<br>
     * @param len - A integer specifying the number of characters you want to
     *            return.<br>
     * @return Returns the rightmost n characters in string
     */
    public static String right(final String str, final int len) {
        if (str == null) {
            return EMPTY;
        }
        if (len >= str.length()) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * Method String Padr(String s, int l) Returns a string padded with spaces
     * to a specified length on the right side
     *
     * @param str    - The string you want to padd.<br>
     * @param length - Specifies the total number of characters after it is padded.<br>
     * @return Returns string padded with space on the right side which length
     * equal l
     */
    public static String padRight(String str, final int length) {
        if (str == null) {
            str = EMPTY;
        }
        return left(str + fill(SPACE, length), length);
    }

    /**
     * Method String Padr(String s, int l, s1) Returns a string padded with
     * string s1 to a specified length on the right side
     *
     * @param str     - The string you want to padd.<br>
     * @param length  - Specifies the total number of characters after it is padded.<br>
     * @param padChar - Specifies padded char.<br>
     * @return Returns string padded with space on the right side which length
     * equal l
     */
    public static String padRight(String str, final int length,
                                  final String padChar) {
        if (str == null) {
            str = EMPTY;
        }
        return left(str + fill(padChar, length), length);
    }

    /**
     * Method String Padl(String s, int l) Returns a string padded with spaces
     * to a specified length on the left side
     *
     * @param str    - The string you want to padd.<br>
     * @param length - Specifies the total number of characters after it is padded.<br>
     * @return Returns string padded with space on the left side which length
     * equal l
     */
    public static String padLeft(String str, final int length) {
        if (str == null) {
            str = EMPTY;
        }
        return right(fill(SPACE, length) + str, length);
    }

    public static String padLeft(String str, final int length, final String padChar) {
        if (str == null) {
            str = EMPTY;
        }
        return right(fill(padChar, length) + str, length);
    }

    public static String insert(final String str, final int start,
                                final String insertString) {
        if (str == null || insertString == null) {
            return null;
        }

        final StringBuilder s = new StringBuilder();
        if (start > 0) {
            s.append(mid(str, 0, start));
        }

        s.append(insertString);
        s.append(str.substring(start));
        return s.toString();
    }

    public static int countOccurrences(final String container,
                                       final String content) {
        int currIndex = 0;
        int occurrences = 0;
        while (true) {
            final int lastIndex = container.indexOf(content, currIndex);
            if (lastIndex == -1) {
                break;
            }
            currIndex = lastIndex + content.length();
            occurrences++;
        }
        return occurrences;
    }

    public static String clearNewLines(String str) {
        str = replace(str, "\n", ".");
        str = replace(str, "\r", "");
        return str;
    }

    public static int lastPos(final String str, final String findString) {
        if (str == null) {
            return -1;
        }
        if (findString == null) {
            return -1;
        }
        return str.lastIndexOf(findString);
    }

    /**
     * It takes "s" string, where elements are separated by delimiters
     * ("delimiter"), and returns ArrayList<String> with the elements
     *
     * @param s         - initial string with elements
     * @param delimiter - the delimiter
     * @return
     */
    public static List<String> toList(final String s,
                                      final String delimiter) {
        if (isNullOrEmpty(s)) {
            return new ArrayList<>(0);
        }

        final ArrayList<String> list = new ArrayList<>();
        if (isNullOrEmpty(delimiter)) {
            list.add(s);
            return list;
        }

        final String[] strArray = s.split(delimiter);
        if (strArray.length <= 0) {
            return new ArrayList<>(0);
        }

        Collections.addAll(list, strArray);
        return list;
    }

    /**
     * It takes "s" string, where elements are separated by delimiters
     * ("delimiter"), and returns ArrayList<String> with the elements
     *
     * @param s - initial string with elements
     * @return
     */
    public static List<String> toList(final String s) {
        if (isNullOrEmpty(s)) {
            return new ArrayList<>(0);
        }

        final String[] strArray = s.split("[\r\n]");
        if (strArray.length <= 0) {
            return new ArrayList<>(0);
        }

        final ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, strArray);
        return list;
    }

    /**
     * get "string" - return "%string%"
     */
    public static String surroundWithPercent(final String toSearch) {
        return "%" + toSearch + "%";
    }

    /**
     * @param string
     * @return "string" as "[string]"
     */
    public static String getStringWithBrackets(final String string) {
        return "[" + string + "]";
    }

    public static String isNull(final String str, final String defaultString) {
        return (isNullOrEmpty(str) ? defaultString : str);
    }

    public static String getNullIfEmpty(final String str) {
        return (isNullOrEmpty(str) ? null : str);
    }

    public static String getEmptyIfNull(final String str) {
        return (str == null ? EMPTY : str);
    }

    public static String join(final String separator, final Object... array) {
        if (array == null) {
            return EMPTY;
        }
        if (array.length == 1) {
            return array[0].toString();
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String allTrim(final String s) {
        if (isNullOrEmpty(s)) {
            return EMPTY;
        }
        return s.trim();
    }

    public static boolean containsValue(final SparseArray<String> array,
                                        final String value) {
        if (array == null || value == null) {
            return false;
        }
        for (int i = 0; i < array.size(); i++) {
            if (array.get(array.keyAt(i)).equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        if (src == null || what == null) {
            return false;
        }

        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static String byteArrayToHex(byte[] a) {
        final StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static String getHash(final String s) {
        final String ss = getEmptyIfNull(s);
        MessageDigest md = null;
        String base64 = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte byteData[] = md.digest(ss.getBytes(UTF_8));
            base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
        } catch (Exception e) {
        }
        return base64;
    }

    public static String getDigits(final String s) {
        if (isNullOrEmpty(s)) {
            return s;
        }
        return s.replaceAll("\\D+", "");
    }

    public static String getNumbers(final String s) {
        if (isNullOrEmpty(s)) {
            return s;
        }
        return s.replaceAll("[^\\.0123456789\\+]", "");
    }

    public static long toLong(final String s) {
        long l = 0;
        if (!isNullOrEmpty(s)) {
            try {
                l = Long.parseLong(s);
            } catch (Exception e) {
            }
        }
        return l;
    }

    public static int toInt(final String s) {
        int i = 0;
        if (!isNullOrEmpty(s)) {
            try {
                i = Integer.parseInt(s);
            } catch (Exception e) {
            }
        }
        return i;
    }

    public static double toDouble(String s) {
        double d = 0;
        if (!isNullOrEmpty(s)) {
            s = replace(s, ",", ".");
            try {
                d = Double.parseDouble(s);
            } catch (Exception e) {
            }
        }
        return d;
    }

    public static float toFloat(String s) {
        float f = 0;
        if (!isNullOrEmpty(s)) {
            s = replace(s, ",", ".");
            try {
                f = Float.parseFloat(s);
            } catch (Exception e) {
            }
        }
        return f;
    }

    public static String formatPhone(final String phone) {
        if (isNullOrEmpty(phone)) {
            return EMPTY;
        }

        int cnt = 11;
        final StringBuilder sb = new StringBuilder();

        boolean isRussian = false;

        int pos = 0;
        for (int i = 0; i < phone.length(); i++) {
            if (pos >= cnt) {
                break;
            }

            final String s = mid(phone, i, 1);
            if (!isDigit(s)) {
                continue;
            }

            if (!isRussian && pos > 0) {
                sb.append(s);
                pos++;
                continue;
            }

            if (pos == 0) {
                sb.append("+");
                if (s.equals("8") || s.equals("7") || s.equals("9")) {
                    isRussian = true;
                    sb.append("7 (");
                    if (s.equals("9")) {
                        sb.append("9");
                        pos++;
                    }
                } else {
                    sb.append(s);
                }
            } else if (pos == 3) {
                sb.append(s);
                sb.append(") ");
            } else if (pos == 6) {
                sb.append(s);
                sb.append("-");
            } else if (pos == 8) {
                sb.append(s);
                sb.append("-");
            } else {
                sb.append(s);
            }
            pos++;
        }
        return sb.toString();
    }

    public static boolean isDigit(final String s) {
        return (!isNullOrEmpty(getDigits(s)));
    }

    public static boolean startsWith(final String s, final String with) {
        final int cnt = numToken(s, " ");
        final String withs = with.toLowerCase();
        for (int i = 1; i <= cnt; i++) {
            String ss = token(s, " ", i).toLowerCase();
            if (ss.startsWith(withs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWith(final String s, final String with) {
        final int cnt = numToken(s, " ");
        final String withs = with.toLowerCase();
        for (int i = 1; i <= cnt; i++) {
            String ss = token(s, " ", i).toLowerCase();
            if (ss.endsWith(withs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate hex with regular expression
     *
     * @param email hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean isEmailValid(@NonNull final String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }

        boolean isValid = false;
        if (pattern == null) {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }
        isValid = pattern.matcher(email).matches();
        return isValid;
    }

    public static String getFirstCapitalize(@NonNull String text) {
        if (isNullOrEmpty(text)) {
            return null;
        }

        final StringBuilder textCapitalize = new StringBuilder();
        textCapitalize.append(text.substring(0, 1).toUpperCase()).append(text.substring(1, text.length()));
        return textCapitalize.toString();
    }

    public static String getAllFirstCapitalize(@NonNull String text) {
        if (isNullOrEmpty(text)) {
            return null;
        }

        final StringBuilder textCapitalize = new StringBuilder();
        final int cnt = numToken(text, " ");
        for (int i = 1; i <= cnt; i++) {
            textCapitalize.append(getFirstCapitalize(token(text, " ", i)));
            if (i < cnt) {
                textCapitalize.append(" ");
            }
        }
        return textCapitalize.toString();
    }

    public static List<String> arrayToList(String... strings) {
        final List<String> list = new ArrayList<>();
        for (String s : strings) {
            if (!isNullOrEmpty(s)) {
                list.add(s);
            }
        }
        return list;
    }

    public static List<String> arrayToList(List<String> list, String... strings) {
        if (list == null) {
            list = new ArrayList<>();
        }

        for (String s : strings) {
            if (!isNullOrEmpty(s)) {
                list.add(s);
            }
        }
        return list;
    }

    public static String last(final String s, final String delimiter) {
        if (isNullOrEmpty(s) || isNullOrEmpty(delimiter)) {
            return null;
        }

        final String[] array = s.split(delimiter);
        return array[array.length - 1];
    }

    public static long toDate(final String dateString, final String format) {
        final SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return formatter.parse(dateString).getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    public static String formatDate(final long dateLong) {
        final Date date = new Date(dateLong);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formatDateShort(final long dateLong) {
        final Date date = new Date(dateLong);
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formatDateShortRu(final long dateLong) {
        final Date date = new Date(dateLong);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formatDateTime(final long dateLong) {
        final Date date = new Date(dateLong);
        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formatDateDay(final long dateLong) {
        final Date date = new Date(dateLong);
        final Date current = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        final Date yesterday = cal.getTime();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        if (formatter.format(date).equals(formatter.format(current))) {
            return "Сегодня";
        } else if (formatter.format(date).equals(formatter.format(yesterday))) {
            return "Вчера";
        } else {
            final SimpleDateFormat day_formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return day_formatter.format(date);
        }
    }

    public static String formatDate(final long dateLong, final String format) {
        if (isNullOrEmpty(format)) {
            return null;
        }

        final Date date = new Date(dateLong);
        final SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }

}
