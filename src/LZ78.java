import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LZ78 {
    private static List<String> lastDictionary;

    public static byte[] decompressFromArray(byte[] compressed) throws Exception {

        List<Pair<Integer, Character>> data = new ArrayList<>();
        for (int i = 0; i < compressed.length; i += 2) {
            data.add(new Pair<>((int) compressed[i], (char) compressed[i + 1]));
        }

        char[] ret = decompress(data);
        byte[] byteArray = new byte[ret.length];
        for (int i = 0; i < ret.length; i++) {
            byteArray[i] = (byte) ret[i];
        }
        return byteArray;
    }

    public static byte[] compressToArray(byte[] message) {
        char[] charArray = new char[message.length];
        for (int i = 0; i < message.length; i++) {
            charArray[i] = (char) message[i];
        }
        //CharBuffer charBuffer = ByteBuffer.wrap(message).asCharBuffer();
        //char[] charArray = new char[charBuffer.remaining()];
        //charBuffer.get(charArray);
        List<Pair<Integer, Character>> ret = compress(charArray);
        List<Byte> data = new ArrayList<>();
        for (int i = 0; i < ret.size(); i++) {
            data.add(ret.get(i).getKey().byteValue());
            Character res = ret.get(i).getValue();
            if (res == null) res = '\0';
            data.add((byte) res.charValue());
        }
        byte[] arr = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            arr[i] = data.get(i);
        }
        return arr;
    }

    public static char[] decompress(List<Pair<Integer, Character>> compressed) throws Exception {
        if (lastDictionary == null) lastDictionary = new ArrayList<>();
        else lastDictionary.clear();

        StringBuilder message = new StringBuilder();

        for (Pair<Integer, Character> p : compressed) {
            String entry = "";
            if (p.getKey() != 0) {
                if (lastDictionary.size() < p.getKey() || p.getKey() < 1) {
                    //Just the worst possible errors message.
                    throw new Exception("Compressed data contains invalid pointer to non-existing entries in the dynamically generated dictionary.");
                }
                entry = lastDictionary.get(p.getKey() - 1);
                message.append(entry);
            }
            if (p.getValue() != null) {
                entry += p.getValue();
                message.append(p.getValue());
            }
            lastDictionary.add(entry);
        }
        return message.toString().toCharArray();
    }

    public static List<Pair<Integer, Character>> compress(char[] message) {
        if (lastDictionary == null) lastDictionary = new ArrayList<>();
        else lastDictionary.clear();
        HashMap<String, Integer> dictionary = new HashMap<>();

        List<Pair<Integer, Character>> compressed = new ArrayList<>();
        StringBuilder window = new StringBuilder();
        int matchIdx = 0;
        int directoryIdx = 1;
        for (char c : message) {
            window.append(c);
            int found = dictionary.getOrDefault(window.toString(), 0);
            if (found != 0) {
                matchIdx = found;
            } else {
                compressed.add(new Pair<>(matchIdx, c));
                matchIdx = 0;
                dictionary.put(window.toString(), directoryIdx++);
                //for logging purposed
                lastDictionary.add(window.toString());

                window.setLength(0);
            }
        }
        //if last character in message is part of a match
        if (window.length() > 0)
            compressed.add(new Pair<>(matchIdx, null));

        return compressed;
    }

    public static List<String> getLastDictionary() {
        return lastDictionary;
    }
}
