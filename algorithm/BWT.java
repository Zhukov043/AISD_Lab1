package org.example.algorithm;
import java.util.*;

public class BWT {

    static class Suffix implements Comparable<Suffix>{
        int index;
        byte[] suffix;

        public Suffix(int index, byte[] suffix){
            this.index = index;
            this.suffix = suffix;
        }

        @Override
        public int compareTo(Suffix other){
            int minLen = Math.min(this.suffix.length, other.suffix.length);
            for (int i = 0; i < minLen; i++) {
                if (this.suffix[i] != other.suffix[i]) {
                    return Byte.compare(this.suffix[i], other.suffix[i]);
                }
            }
            return Integer.compare(this.suffix.length, other.suffix.length);
        }
    }

    public static byte[] bwt_encode (byte[] byte_string){
        byte[] byte_string2 = new byte[byte_string.length + 1];
        System.arraycopy(byte_string, 0, byte_string2, 0, byte_string.length);
        byte_string2[byte_string.length] = (byte) 0x7F;
        int n = byte_string2.length;
        Suffix[] suffixies = new Suffix[n];
        for (int i = 0; i < n; i++){
            byte[] suf = Arrays.copyOfRange(byte_string2, i, n);
            suffixies[i] = new Suffix(i, suf);
        }

        Arrays.sort(suffixies);

        byte[] lastColumn = new byte[n];
        for (int i = 0; i < n; i++){
            int index = (suffixies[i].index + n - 1) % n;
            lastColumn[i] = byte_string2[index];
        }
        return lastColumn;
    }

    private static int[] counting_sort(byte[] byte_string){
        int[] count = new int[256];
        Arrays.fill(count, 0);

        for (byte b : byte_string) {
            count[b & 0xFF]++;
        }

        for (int i = 1; i < 256; i++) {
            count[i] += count[i - 1];
        }

        int[] inverse = new int[byte_string.length];
        for (int i = byte_string.length - 1; i > - 1; i--){
            byte b = byte_string[i];
            count[b & 0xFF] -= 1;
            inverse[count[b & 0xFF]] = i;
        }
        return inverse;

    }

    public static byte[] bwt_decode(byte[] byte_string){
        int n = byte_string.length;
        int end = 0;
        for (int i = 0; i < n; i++){
            if (byte_string[i] == (byte) 0x7F) {
                end = i;
                break;
            }
        }
        int[] inverse = counting_sort(byte_string);
        byte[] s = new byte[n];

        for (int i = 0; i < n; i++) {
            end = inverse[end];
            s[i] = byte_string[end];
        }
        while (n > 0 && s[n - 1] == 0x7F) {
            n--;
        }

        return Arrays.copyOf(s, n);
    }
}
