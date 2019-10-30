public class HuffmanNode {
    private HuffmanNode leftChild;
    private HuffmanNode rightChild;
    private HuffmanNode parent;
    private Character cSymbol;
    private boolean isRightChild;
    private int nWeight;

    public HuffmanNode() {
        this(null);
    }

    public HuffmanNode(HuffmanNode parent) {
        this.parent = parent;
        this.isRightChild = false; //NYT is always the left child
        this.setcSymbol(null);
        this.nWeight = 0;
    }

    public HuffmanNode(HuffmanNode parent, char symbol) {
        this.parent = parent;
        this.leftChild = this.rightChild = null;
        this.setcSymbol(symbol);
        this.isRightChild = true; //non-NYT are always the right children
        this.nWeight = 0;
    }

    public void split(char c) {
        this.rightChild = new HuffmanNode(this, c);
        this.leftChild = new HuffmanNode(this);

        this.getRightChild().nWeight = this.nWeight = 1;

    }

    public boolean isLeaf() {
        return this.getRightChild() == null;
    }

    public void setChildren(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        if (!this.isLeaf())
            this.getRightChild().parent = this.getLeftChild().parent = this;
    }

    public HuffmanNode getLeftChild() {
        return leftChild;
    }

    public HuffmanNode getRightChild() {
        return rightChild;
    }

    public HuffmanNode getParent() {
        return parent;
    }

    public Character getSymbol() {
        return getcSymbol();
    }

    public int getWeight() {
        return nWeight;
    }

    public void setWeight(int weight) {
        nWeight = weight;
    }

    public void incrementWeight() {
        ++nWeight;
    }

    public Character getcSymbol() {
        return cSymbol;
    }

    public void setcSymbol(Character cSymbol) {
        this.cSymbol = cSymbol;
    }

    public char getRepresentingBit() {
        return this.isRightChild ? '1' : '0';
    }
}