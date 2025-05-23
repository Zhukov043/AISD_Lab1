package org.example.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class LZ77 {
    static class result{
        int offset;
        int lenght;
        byte next;
        byte nextRus;

        public result(int offset, int lenght, byte next){
            this.lenght = lenght;
            this.offset = offset;
            this.next = next;
        }
        public result(byte nextRus){
            this.nextRus = nextRus;
        }
    }

    private static int find(byte[] subArr, byte[] arr){
        if (subArr.length > arr.length) return -1;
        byte[] temp = new byte[subArr.length];
        for (int i = 0; i <= arr.length - subArr.length; i++){
            System.arraycopy(arr, i, temp, 0, subArr.length);
            if (Arrays.equals(temp, subArr)) return i;
        }
        return -1;
    }

    public static byte[] lz77_encoded(byte[] byte_string){
        List<result> res = new ArrayList<>();
        int buffer_size = 512;
        int string_size = 512;
        int N = byte_string.length;
        int i = 0;
        byte[] buffer;

        while (i < N){
            buffer = Arrays.copyOfRange(byte_string, max(0, i - buffer_size), i);
            int new_buffer_size = buffer.length;
            int shift;
            for (int j = string_size; j > -1; j--){
                byte[] subArr = Arrays.copyOfRange(byte_string, i, min((i+j), N - 1));
                if ((shift = find(subArr, buffer)) > - 1){
                    result rep;
                    if (subArr.length == 0){
                        rep = new result(0,0,byte_string[i]);
                    }
                    else {
                        rep = new result(new_buffer_size - shift, subArr.length, byte_string[max(0, i + subArr.length)]);
                    }
                    res.add(rep);
                    i += subArr.length + 1;   //мб +1
                    break;
                }
            }
        }
        i = -1;
        byte[] encoded = new byte[res.size() * 3];
        for (result elem : res) {
            encoded[++i] = (byte) elem.offset;
            encoded[++i] = (byte) elem.lenght;
            encoded[++i] = elem.next;
        }
        return encoded;
    }

    public static byte[] lz77_decoded(byte[] coded_string){
        List<Byte> decoded = new ArrayList<>();
        for(int i = 0; i < coded_string.length; i += 3){
            if((int)coded_string[i + 1] > 0){
                int start = decoded.size() - coded_string[i];
                for(int j = 0; j < coded_string[i + 1]; j++){
                    decoded.add(decoded.get(start + j));
                }
            }
            decoded.add(coded_string[i + 2]);
        }
        byte[] decoded_message = new byte[decoded.size()];
        for (int i = 0; i < decoded.size(); i++){
            decoded_message[i] = decoded.get(i);
        }
        return decoded_message;
    }
}
