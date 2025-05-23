package org.example.algorithm;

import java.io.*;
import java.util.*;


public class HA {

    private static class Node{
        private int counter;
        private byte symbol;
        private Node left;
        private Node right;
        private Node parent;

        public Node(byte symbol, int counter){
            this.counter = counter;
            this.symbol = symbol;
        }
        public Node(int counter, Node left, Node right){
            this.counter = counter;
            this.left = left;
            this.right = right;
        }
        public int getCounter(){
            return counter;
        }
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    private static int[] count_symb(byte[] byte_string){
        int[] counter = new int[256];
        for (byte b : byte_string) {
            counter[b & 0xFF] += 1;
        }
        return counter;
    }

    private static byte[] bitsToBytes(String bits) {
        int length = bits.length();
        int byteCount = (length + 7) / 8;
        byte[] bytes = new byte[byteCount];

        for (int i = 0; i < length; i++) {
            if (bits.charAt(i) == '1') {
                bytes[i / 8] |= (1 << (7 - (i % 8)));
            }
        }

        return bytes;
    }

    public static class HuffmanResult {
        public final byte[] encodedData;
        public final HashMap<Byte, String> codes;
        public final int size;

        public HuffmanResult(byte[] encodedData, HashMap<Byte, String> codes, int size) {
            this.encodedData = encodedData;
            this.codes = codes;
            this.size = size;
        }
        public byte[] message(){ return encodedData;}
        public HashMap codes(){ return codes;}
    }

    public static HuffmanResult ha_encode(byte[] byte_string){
        int[] counter = count_symb(byte_string);
        ArrayList<Node> leafs_list = new ArrayList<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getCounter));
        for (int i = 0; i < counter.length; i++){
            if (counter[i] != 0){
                Node leaf = new Node((byte) i, counter[i]);
                leafs_list.add(leaf);
                queue.add(leaf);
            }
        }
        Node left_node;
        Node right_node;
        while (queue.size() >= 2){
            left_node = queue.poll();
            right_node = queue.poll();
            Node parent = new Node(left_node.counter + right_node.counter, left_node, right_node);
            left_node.parent = parent;
            right_node.parent = parent;
            queue.add(parent);
        }
        HashMap<Byte, String> codes = new HashMap<>();
        for (Node leaf: leafs_list){
            Node node = leaf;
            StringBuilder code = new StringBuilder();
            while (node.parent != null){
                if (node.parent.left == node) code.append("0");
                if (node.parent.right == node) code.append("1");
                node = node.parent;
            }
            code.reverse();
            codes.put(leaf.symbol, code.toString());
        }
        StringBuilder s = new StringBuilder();
        int i = 0;
        for (byte b: byte_string){
            s.append(codes.get(b));
        }
        byte[] encoded_message = bitsToBytes(s.toString());

        return new HuffmanResult(encoded_message, codes, byte_string.length);
    }

    public static byte[] ha_decode(HuffmanResult Hr) {
        byte[] encodedBytes = Hr.encodedData;
        HashMap<Byte, String> huffmanCodes = Hr.codes;

        // Строим дерево декодирования из кодов
        Node root = buildDecodingTree(huffmanCodes);

        // Преобразуем байты обратно в битовую строку
        StringBuilder bits = new StringBuilder();
        for (byte b : encodedBytes) {
            for (int i = 7; i >= 0; i--) {
                bits.append((b & (1 << i)) != 0 ? '1' : '0');
            }
        }

        // Декодируем битовую строку
        List<Byte> decoded = new ArrayList<>();
        Node current = root;

        for (int i = 0; i < bits.length(); i++) {
            char bit = bits.charAt(i);
            if (bit == '0') {
                current = current.left;
            } else if (bit == '1') {
                current = current.right;
            }

            if (current.isLeaf()) {
                decoded.add(current.symbol);
                current = root;
            }
        }

        // Преобразуем список в массив байтов
        byte[] result = new byte[Hr.size];
        for (int i = 0; i < Hr.size; i++) {
            result[i] = decoded.get(i);
        }

        return result;
    }

    private static Node buildDecodingTree(Map<Byte, String> huffmanCodes) {
        Node root = new Node(0, null, null);

        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            Node current = root;
            String code = entry.getValue();

            for (int i = 0; i < code.length(); i++) {
                char bit = code.charAt(i);

                if (bit == '0') {
                    if (current.left == null) {
                        current.left = new Node(0, null, null);
                    }
                    current = current.left;
                } else if (bit == '1') {
                    if (current.right == null) {
                        current.right = new Node(0, null, null);
                    }
                    current = current.right;
                }
            }

            current.symbol = entry.getKey();
        }

        return root;
    }

    public static void saveCompressedData(String outputPath, HuffmanResult Hr) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(outputPath)))) {

            oos.writeObject(Hr.codes);
            oos.writeInt(Hr.size);
            oos.write(Hr.encodedData);
        } catch (IOException e){
            System.out.println("Ошибка записи");
        }
    }
}
;