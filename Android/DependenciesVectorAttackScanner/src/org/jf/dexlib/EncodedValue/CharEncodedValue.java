/*
 * [The "BSD licence"]
 * Copyright (c) 2009 Ben Gruver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib.EncodedValue;

import org.jf.dexlib.Util.Input;
import org.jf.dexlib.Util.EncodedValueUtils;
import org.jf.dexlib.Util.AnnotatedOutput;

public class CharEncodedValue extends EncodedValue {
    public final char value;

    /**
     * Constructs a new <code>CharEncodedValue</code> by reading the value from the given <code>Input</code> object.
     * The <code>Input</code>'s cursor should be set to the 2nd byte of the encoded value, and the high 3 bits of
     * the first byte should be passed as the valueArg parameter
     * @param in The <code>Input</code> object to read from
     * @param valueArg The high 3 bits of the first byte of this encoded value
     */
    protected CharEncodedValue(Input in, byte valueArg) {
        value = (char)EncodedValueUtils.decodeUnsignedIntegralValue(in.readBytes(valueArg+1));
    }

    /**
     * Constructs a new <code>CharEncodedValue</code> with the given value
     * @param value The value
     */
    public CharEncodedValue(char value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    public void writeValue(AnnotatedOutput out) {
        byte[] bytes = EncodedValueUtils.encodeUnsignedIntegralValue(value);

        if (out.annotates()) {
            out.annotate(1, "value_type=" + ValueType.VALUE_CHAR.name() + ",value_arg=" + (bytes.length - 1));
            char[] c = Character.toChars(value);
            assert c.length > 0;
            out.annotate(bytes.length, "value: 0x" + Integer.toHexString(value) + " '" + c[0] + "'");
        }

        out.writeByte(ValueType.VALUE_CHAR.value | ((bytes.length - 1) << 5));
        out.write(bytes);
    }

    /** {@inheritDoc} */
    public int placeValue(int offset) {
        return offset + EncodedValueUtils.getRequiredBytesForUnsignedIntegralValue(value) + 1;
    }

    /** {@inheritDoc} */
    protected int compareValue(EncodedValue o) {
        CharEncodedValue other = (CharEncodedValue)o;

         return (value<other.value?-1:(value>other.value?1:0));
    }

    /** {@inheritDoc} */
    public ValueType getValueType() {
        return ValueType.VALUE_CHAR;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
