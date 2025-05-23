package org.example.algorithm;

import java.util.ArrayList;
import java.util.List;

public class MTF {

    public static byte[] mtf_encode (byte[] byte_string){
        int n = byte_string.length;
        List<Integer> alphabet = new ArrayList<>();
        for (int i = 0; i < 256; i++) alphabet.add(i);
        byte[] result = new byte[n];

        for (int i = 0; i < n; i++){
            int curByte = byte_string[i] & 0xFF;
            int index = alphabet.indexOf(curByte);

            result[i] = (byte) index;
            alphabet.remove(index);
            alphabet.add(0, curByte);
        }
        return result;
    }

    public static byte[] mtf_decode(byte[] encoded_string){
        int n = encoded_string.length;
        List<Integer> alphabet = new ArrayList<>();
        for (int i = 0; i < 256; i++) alphabet.add(i);
        byte[] result = new byte[n];

        for (int i = 0; i < n; i++){
            int index = encoded_string[i] & 0xFF;
            int curByte = alphabet.get(index);
            result[i] = (byte) curByte;
            alphabet.remove(index);
            alphabet.add(0, curByte);
        }
        return result;
    }
}
