package org.apache.poi.xwpf.converter.core.utils;

/**
 * This class can produce String combinations representing a number.
 * "a" to "z" represent 1 to 26, "AA" represents 27, "AB" represents 28,
 * and so on; "ZZ" is followed by "AAA".
 */
public class RomanAlphabetFactory {

    /**
     * Translates a positive integer (not equal to zero)
     * into a String using the letters 'a' to 'z';
     * 1 = a, 2 = b, ..., 26 = z, 27 = aa, 28 = ab,...
     */
    public static final String getString(int index) {
        if (index < 1) throw new NumberFormatException(
                "You can't translate a negative number into an alphabetical value.");
        
        index--;
        int bytes = 1;
        int start = 0;
        int symbols = 26;  
        while(index >= symbols + start) {
            bytes++;
            start += symbols;
            symbols *= 26;
        }
              
        int c = index - start;
        char[] value = new char[bytes];
        while(bytes > 0) {
            value[--bytes] = (char)( 'a' + (c % 26));
            c /= 26;
        }
        
        return new String(value);
    }
    
    /**
     * Translates a positive integer (not equal to zero)
     * into a String using the letters 'a' to 'z';
     * 1 = a, 2 = b, ..., 26 = z, 27 = aa, 28 = ab,...
     */
    public static final String getLowerCaseString(int index) {
        return getString(index);        
    }
    
    /**
     * Translates a positive integer (not equal to zero)
     * into a String using the letters 'A' to 'Z';
     * 1 = A, 2 = B, ..., 26 = Z, 27 = AA, 28 = AB,...
     */
    public static final String getUpperCaseString(int index) {
        return getString(index).toUpperCase();      
    }

    
    /**
     * Translates a positive integer (not equal to zero)
     * into a String using the letters 'a' to 'z'
     * (a = 1, b = 2, ..., z = 26, aa = 27, ab = 28,...).
     */
    public static final String getString(int index, boolean lowercase) {
        if (lowercase) {
            return getLowerCaseString(index);
        }
        else {
            return getUpperCaseString(index);
        }
    }
    
    /**
     * Test this class using this main method.
     */
    public static void main(String[] args) {
        for (int i = 1; i < 32000; i++) {
            System.out.println(getString(i));
        }
    }
}
