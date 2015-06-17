package com.mapia.api;

/**
 * Created by daehyun on 15. 6. 13..
 */

public class Base64
{
    private static final char[] INT_TO_BASE64;

    static {
        INT_TO_BASE64 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
    }

    public static String encodeBase64(final byte[] array) {
        final int length = array.length;
        final int n = length / 3;
        final int n2 = length - n * 3;
        final StringBuffer sb = new StringBuffer((length + 2) / 3 * 4);
        final char[] int_TO_BASE64 = Base64.INT_TO_BASE64;
        int i = 0;
        int n3 = 0;
        while (i < n) {
            final int n4 = n3 + 1;
            final int n5 = array[n3] & 0xFF;
            final int n6 = n4 + 1;
            final int n7 = array[n4] & 0xFF;
            final int n8 = array[n6] & 0xFF;
            sb.append(int_TO_BASE64[n5 >> 2]);
            sb.append(int_TO_BASE64[(n5 << 4 & 0x3F) | n7 >> 4]);
            sb.append(int_TO_BASE64[(n7 << 2 & 0x3F) | n8 >> 6]);
            sb.append(int_TO_BASE64[n8 & 0x3F]);
            ++i;
            n3 = n6 + 1;
        }
        if (n2 != 0) {
            final int n9 = n3 + 1;
            final int n10 = array[n3] & 0xFF;
            sb.append(int_TO_BASE64[n10 >> 2]);
            if (n2 == 1) {
                sb.append(int_TO_BASE64[n10 << 4 & 0x3F]);
                sb.append("==");
            }
            else {
                final int n11 = array[n9] & 0xFF;
                sb.append(int_TO_BASE64[(n10 << 4 & 0x3F) | n11 >> 4]);
                sb.append(int_TO_BASE64[n11 << 2 & 0x3F]);
                sb.append('=');
            }
        }
        return sb.toString();
    }
}