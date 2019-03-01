package org.web3j.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Byte array utility functions.
 */
public class Bytes {

    private Bytes() {}

    public static byte[] trimLeadingBytes(byte[] bytes, byte b) {
        int offset = 0;
        for (; offset < bytes.length - 1; offset++) {
            if (bytes[offset] != b) {
                break;
            }
        }
        return Arrays.copyOfRange(bytes, offset, bytes.length);
    }

    public static byte[] trimLeadingZeroes(byte[] bytes) {
        return trimLeadingBytes(bytes, (byte) 0);
    }

    public static byte[] toByteArray(Long value) {
        return ByteBuffer.allocate(Long.BYTES).putLong(value.longValue()).array();
    }
}
