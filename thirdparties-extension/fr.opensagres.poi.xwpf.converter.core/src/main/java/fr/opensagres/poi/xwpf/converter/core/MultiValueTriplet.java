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
package fr.opensagres.poi.xwpf.converter.core;

/**
 * This class is used for a Multi Value Triplet.
 */
public class MultiValueTriplet<V1, V2, V3>
{
    private V1 firstValue;

    private V2 secondValue;

    private V3 thirdValue;

    /**
     * Constructs a Multi Value Triplet.
     *
     * @param firstValue a first value (can be null)
     * @param secondValue a second value (can be null)
     * @param thirdValue a third value (can be null)
     */
    public MultiValueTriplet( V1 firstValue, V2 secondValue, V3 thirdValue )
    {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
    }

    /** @return the first value (can be null) */
    public V1 getFirstValue()
    {
        return firstValue;
    }

    /**
     * Sets the first value.
     *
     * @param value a value (can be null)
     */
    public void setFirstValue( V1 value )
    {
        firstValue = value;
    }

    /** @return the second value (can be null) */
    public V2 getSecondValue()
    {
        return secondValue;
    }

    /**
     * Sets the second value.
     *
     * @param value a value (can be null)
     */
    public void setSecondValue( V2 value )
    {
        secondValue = value;
    }

    /** @return the third value (can be null) */
    public V3 getThirdValue()
    {
        return thirdValue;
    }

    /**
     * Sets the third value.
     *
     * @param value a value (can be null)
     */
    public void setThirdValue( V3 value )
    {
        thirdValue = value;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( object instanceof MultiValueTriplet )
        {
            MultiValueTriplet<?, ?, ?> multiValueTriplet = (MultiValueTriplet<?, ?, ?>) object;
            return ( ( firstValue == null ) ? multiValueTriplet.firstValue == null
                            : firstValue.equals( multiValueTriplet.firstValue ) )
                && ( ( secondValue == null ) ? multiValueTriplet.secondValue == null
                                : secondValue.equals( multiValueTriplet.secondValue ) )
                && ( ( thirdValue == null ) ? multiValueTriplet.thirdValue == null
                                : thirdValue.equals( multiValueTriplet.thirdValue ) );
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return ( ( firstValue == null ) ? 0 : firstValue.hashCode() )
            + ( ( secondValue == null ) ? 0 : 2 * secondValue.hashCode() )
            + ( ( thirdValue == null ) ? 0 : 3 * thirdValue.hashCode() );
    }
}