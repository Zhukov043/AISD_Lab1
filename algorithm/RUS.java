package org.example.algorithm;

public class RUS {

    public static byte[] isRus(byte[] text){
        int len = text.length;
        int count = 0;
        for (byte b : text){
            if (firstRusChar(b)) count++;
        }
        byte[] new_text = new byte[len - count];
        int new_index = 0;
        for (byte b : text){
            if (!firstRusChar(b)){
                new_text[new_index++] = b;
            }
        }
        return new_text;
    }


    public static byte[] isRus_reverse(byte[] text){
        int len = text.length;
        byte byt = text[0];
        int count = 0;
        if (!secondRusChar(byt)) return text;
        for (byte b : text){
            if (secondRusChar(b)) count++;
        }
        byte[] new_text = new byte[len + count];
        int new_index = 0;
        for (byte b : text){
            if (secondRusChar(b)){
                if(is209(b)) new_text[new_index++] = (byte) 0xD1;
                else new_text[new_index++] = (byte) 0xD0;
                new_text[new_index++] = b;
            }
            else new_text[new_index++] = b;
        }
        return new_text;
    }

    private static boolean firstRusChar(byte b) {
        return (b & 0xFF) == 0xD0 || (b & 0xFF) == 0xD1;
    }

    private static boolean secondRusChar(byte b) {
        return (b & 0xFF) >= 0x80 && (b & 0xFF) <= 0xBF;
    }

    private static boolean is209(byte b) {
        return (b & 0xFF) >= 0x80 && (b & 0xFF) <= 0x8F;
    }
}
