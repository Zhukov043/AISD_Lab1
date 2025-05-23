package org.example.algorithm;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class RLE {

    public static byte[] rle_encode(byte[] byte_string){
        int n = byte_string.length;
        List<Byte> result = new ArrayList<>();
        int count = 1;
        boolean repeat = false;

        for (int i = 1; i < n; i++){
            if (byte_string[i] == byte_string[i - 1]
                    && count < 255){
                count++;
                repeat = true;
            }
            else if(repeat){
                result.add((byte) 0x7F);    //flag for repeat
                result.add((byte) count);
                result.add(byte_string[i - 1]);
                count = 1;
                repeat = false;
            }
            else result.add(byte_string[i - 1]);
        }
        result.add(byte_string[byte_string.length - 1]);
        byte[] coded = new byte[result.size()];
        for (int i = 0; i < result.size(); i++){
            coded[i] = result.get(i);
        }
        return coded;
    }

    public static byte[] rle_decode(byte[] encoded_string){
        List<Byte> decoded = new ArrayList<>();

        for(int i = 0; i < encoded_string.length; i++){
            if(encoded_string[i] == '\u007F'){
                int size = encoded_string[i + 1] & 0xFF;
                for(int j = 0; j < size; j++){
                    decoded.add(encoded_string[i + 2]);
                }
                i += 2;
            }
            else decoded.add(encoded_string[i]);
        }
        byte[] decoded_string = new byte[decoded.size()];
        for (int i = 0; i < decoded.size(); i++){
            decoded_string[i] = decoded.get(i);
        }
        return decoded_string;
    }

}
