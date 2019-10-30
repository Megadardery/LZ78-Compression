public class AdaptiveHuffman{


    public static String compress(String message) {
        StringBuilder sCompressed = new StringBuilder();
        HuffmanTree tree = new HuffmanTree();

        for (char c : message.toCharArray()) {
            if (tree.isFirstOccurrence(c)) {
                sCompressed.append(tree.getNYTNodeCode());
                sCompressed.append(" ");
                sCompressed.append(HuffmanTree.getShortCode(c));
                sCompressed.append(" ");
            } else {
                sCompressed.append(tree.getNodeCode(c));
                sCompressed.append(" ");
            }
            tree.updateTree(c);

        }
        return sCompressed.substring(1);
    }

    public static String decompress(String message) {
        message = message.replaceAll(" *", "");
        StringBuilder sDecompressed = new StringBuilder();
        HuffmanTree tree = new HuffmanTree();
        HuffmanTree.Decoder decoder = tree.new Decoder();

        for (int idx = -1; idx < message.length(); ++idx) {
            boolean isReady;
            if (idx == -1) isReady = true;
            else isReady = decoder.decode(message.charAt(idx));
            if (isReady) {
                char append;
                if (decoder.isNYT()) {
                    String shortcode = message.substring(idx + 1, idx + 1 + HuffmanTree.N_SHORTCODE_BITS);
                    append = (char) Integer.parseInt(shortcode, 2);
                    idx += HuffmanTree.N_SHORTCODE_BITS;
                } else {
                    append = decoder.getRepresentingSymbol();
                }
                sDecompressed.append(append);
                tree.updateTree(append);
                decoder.reset();
            }
        }

        return sDecompressed.toString();
    }
}
