import java.util.LinkedList;
import java.util.Queue;

public class HuffmanTree {
    public static final int N_SHORTCODE_BITS = 8;
    public static final int N_SYMBOLS = 1 << N_SHORTCODE_BITS;


    private HuffmanNode[] characters = new HuffmanNode[N_SYMBOLS];
    private HuffmanNode nyt = new HuffmanNode();
    private HuffmanNode root = nyt;

    boolean isFirstOccurrence(char c) {
        return (characters[(int) c] == null);
    }

    String getNYTNodeCode() {
        return getNodeCodeHelper(nyt);
    }

    String getNodeCode(char c) {
        return getNodeCodeHelper(characters[(int) c]);
    }

    private HuffmanNode findSwapNode(HuffmanNode u) {
        Queue<HuffmanNode> bfs = new LinkedList<>();
        bfs.add(root);
        while (!bfs.isEmpty()) {
            HuffmanNode v = bfs.poll();
            if (v == u) return null;
            if (v != u.getParent() && v.getWeight() <= u.getWeight())
                return v;
            if (v.getRightChild() != null) bfs.add(v.getRightChild());
            if (v.getLeftChild() != null) bfs.add(v.getLeftChild());
        }
        return null;
    }

    private void swapNodes(HuffmanNode u, HuffmanNode v) {
        HuffmanNode rightChildTemp = u.getRightChild();
        HuffmanNode leftChildTemp = u.getLeftChild();
        u.setChildren(v.getLeftChild(), v.getRightChild());
        v.setChildren(leftChildTemp, rightChildTemp);

        int intTemp = u.getWeight();
        u.setWeight(v.getWeight());
        v.setWeight(intTemp);

        Character charTemp = u.getSymbol();
        u.setcSymbol(v.getSymbol());
        v.setcSymbol(charTemp);

        if (u.getSymbol() != null)
            characters[(int) u.getSymbol()] = u;
        if (v.getSymbol() != null)
            characters[(int) v.getSymbol()] = v;
    }

    void updateTree(char c) {
        HuffmanNode curr;
        if (isFirstOccurrence(c)) {
            nyt.split(c);
            characters[(int) c] = nyt.getRightChild();
            curr = nyt.getParent(); //parent of old nyt.
            nyt = nyt.getLeftChild(); //new nyt
        } else {
            curr = characters[(int) c];
        }
        while (curr != null) {
            HuffmanNode v = findSwapNode(curr);
            if (v != null) {
                swapNodes(curr, v);
                curr = v;
            }
            curr.incrementWeight();
            curr = curr.getParent();
        }
    }


    private static String getNodeCodeHelper(HuffmanNode u) {
        StringBuilder ret = new StringBuilder();
        while (u.getParent() != null) {
            ret.append(u.getRepresentingBit());
            u = u.getParent();
        }
        return ret.reverse().toString();
    }

    static String getShortCode(char c) {
        return Integer.toBinaryString(N_SYMBOLS | c).substring(1);
    }

    class Decoder {
        private HuffmanNode current = root;

        boolean decode(boolean bit) {
            if (bit) current = current.getRightChild();
            else current = current.getLeftChild();
            return current.isLeaf();
        }

        boolean decode(char bit) {
            return decode(bit == '1');
        }

        void reset() {
            current = root;
        }

        boolean isNYT() {
            return (current.getSymbol() == null);
        }

        char getRepresentingSymbol() {
            return current.getSymbol();
        }
    }
}