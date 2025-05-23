package org.example.algorithm;

import java.util.*;

public class LZ78 {

    public static byte[] StrToArr(StringBuilder encoded){
        StringBuilder buf = new StringBuilder();
        List<Byte> encode = new ArrayList<>();
        Integer num;
        for(int i = 0; i < encoded.length(); i++){
            try{
                String s = String.valueOf(encoded.charAt(i));
                num = Integer.parseInt(s);
                buf.append(encoded.charAt(i));
            }catch (NumberFormatException e){
                if (!buf.isEmpty()) {
                    num = Integer.parseInt(buf.toString());
                    encode.add(num.byteValue());
                    buf.delete(0, buf.length());
                }
                encode.add((byte) encoded.charAt(i));
            }
        }
        byte[] encoded_message = new byte[encode.size()];
        for (int i = 0; i < encode.size(); i++){
            encoded_message[i] = encode.get(i);
        }
        return encoded_message;
    }

    private static byte[] ListToArr(List<Byte> s){
        byte[] string = new byte[s.size()];
        for (int i = 0; i < s.size(); i++) string[i] = s.get(i);
        return string;
    }

    public static byte[] lz78_encode(byte[] byte_string){
        StringBuilder buffer = new StringBuilder();
        StringBuilder encoded = new StringBuilder();
        Map<String, Integer> dict = new HashMap<>();

        for (byte b : byte_string) {
            buffer.append((char) b);
            if (!dict.containsKey(buffer.toString())) {
                if (dict.size() < 1000) {
                    dict.put(buffer.toString(), dict.size() + 1);
                }
                buffer.deleteCharAt(buffer.length() - 1);
                if (dict.containsKey(buffer.toString())) {
                    encoded.append(dict.get(buffer.toString()))
                            .append((char) b);
                } else {
                    encoded.append('0');
                    encoded.append((char) b);
                }
                buffer.delete(0, buffer.length());
            }
        }
        if(!buffer.isEmpty()){
            char last = buffer.charAt(buffer.length() - 1);
            if (buffer.length() > 1) {
                encoded.append(dict.get(buffer.toString()))
                        .append(last);
            }
            else encoded.append('0').append(last);
        }
        return StrToArr(encoded);
    }

    public static byte[] lz78_decode(byte[] encoded_string){
        List <byte[]> dict = new ArrayList<>();
        List<Byte> decoded = new ArrayList<>();
        List<Byte> word = new ArrayList<>();
        dict.add(ListToArr(word));
        for(int i = 0; i < encoded_string.length; i++){
            if(!dict.isEmpty()){
                byte[] s = dict.get(encoded_string[i]);
                for (byte b : s) word.add(b);
            }
            word.add(encoded_string[++i]);
            decoded.addAll(word);
            dict.add(ListToArr(word));
            word.clear();
        }
        byte[] decoded_message = new byte[decoded.size()];
        for (int i = 0; i < decoded.size(); i++){
            decoded_message[i] = decoded.get(i);
        }
        return decoded_message;
    }
}
