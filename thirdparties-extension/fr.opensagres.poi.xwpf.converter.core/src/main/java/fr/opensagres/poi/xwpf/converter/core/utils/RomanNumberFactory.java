/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.poi.xwpf.converter.core.utils;


/**
 * This class can produce String combinations representing a roman number.
 */
public class RomanNumberFactory {
    /**
     * Helper class for Roman Digits
     */
    private static class RomanDigit {

        /** part of a roman number */
        public char digit;

        /** value of the roman digit */
        public int value;

        /** can the digit be used as a prefix */
        public boolean pre;

        /**
         * Constructs a roman digit
         * @param digit the roman digit
         * @param value the value
         * @param pre can it be used as a prefix
         */
        RomanDigit(char digit, int value, boolean pre) {
            this.digit = digit;
            this.value = value;
            this.pre = pre;
        }
    }
    
    /**
     * Array with Roman digits.
     */
    private static final RomanDigit[] roman = {
        new RomanDigit('m', 1000, false),
        new RomanDigit('d', 500, false),
        new RomanDigit('c', 100, true),
        new RomanDigit('l', 50, false),
        new RomanDigit('x', 10, true),
        new RomanDigit('v', 5, false),
        new RomanDigit('i', 1, true)
    };
    
    /** 
     * Changes an int into a lower case roman number.
     * @param index the original number
     * @return the roman number (lower case)
     */
    public static final String getString(int index) {
        StringBuffer buf = new StringBuffer();

        // lower than 0 ? Add minus
        if (index < 0) {
            buf.append('-');
            index = -index;
        }

        // greater than 3000
        if (index > 3000) {
            buf.append('|');
            buf.append(getString(index / 1000));
            buf.append('|');
            // remainder
            index = index - (index / 1000) * 1000;
        }

        // number between 1 and 3000
        int pos = 0;
        while (true) {
            // loop over the array with values for m-d-c-l-x-v-i
            RomanDigit dig = roman[pos];
            // adding as many digits as we can
            while (index >= dig.value) {
                buf.append(dig.digit);
                index -= dig.value;
            }
            // we have the complete number
            if (index <= 0) {
                break;
            }
            // look for the next digit that can be used in a special way
            int j = pos;
            while (!roman[++j].pre);

            // does the special notation apply?
            if (index + roman[j].value >= dig.value) {
                buf.append(roman[j].digit).append(dig.digit);
                index -= dig.value - roman[j].value;
            }
            pos++;
        }
        return buf.toString();
    }
    
    /** 
     * Changes an int into a lower case roman number.
     * @param index the original number
     * @return the roman number (lower case)
     */
    public static final String getLowerCaseString(int index) {
        return getString(index);        
    }
    
    /** 
     * Changes an int into an upper case roman number.
     * @param index the original number
     * @return the roman number (lower case)
     */
    public static final String getUpperCaseString(int index) {
        return getString(index).toUpperCase();      
    }

    /** 
     * Changes an int into a roman number.
     * @param index the original number
     * @return the roman number (lower case)
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
        for (int i = 1; i < 2000; i++) {
            System.out.println(getString(i));
        }
    }
}
