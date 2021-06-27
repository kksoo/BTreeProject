import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {

    int n = 0;
    private boolean isLeaf = true;
    List<Integer> keys = new ArrayList<>();
    List<Node> children = new ArrayList<>();

    public boolean isFull() {
        return this.n == (BTree.DEGREE * 2 - 1);
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public boolean isNonLeaf() {
        return this.isLeaf == false;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer key(int idx) {
        return keys.get(idx);
    }

    public List<Integer> key(int from, int to) {
        return new ArrayList<>(keys.subList(from, to));
    }

    public Node child(int idx) {
        return children.get(idx);
    }

    public List<Node> child(int from, int to) {
        return new ArrayList<>(children.subList(from, to));
    }

    void sortKeys() {
        Collections.sort(this.keys);
    }

    public Node copy() {
        Node node = new Node();
        node.n = this.n;
        node.isLeaf = this.isLeaf;
        node.keys = new ArrayList<>(this.keys);
        node.children = new ArrayList<>();
        for (Node child : this.children) {
            node.children.add(child.copy());
        }
        return node;
    }

    public List<Integer> toList() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(keys.size());
        for (Integer key : keys) {
            list.add(key);
        }
        list.add(children.size());
        for (Node child : this.children) {
            list.addAll(child.toList());
        }
        return list;
    }

    static class Loader {

        int index = 0;

        public Node fromList(List<Integer> list) {
            index = 0;
            return fromList(0, list);
        }

        private Node fromList(int startIdx, List<Integer> list) {
            Node node = new Node();
            index = startIdx;
            int keyCnt = list.get(index++);
            for (int i = 0; i < keyCnt; i++) {
                Integer key = list.get(index++);
                node.keys.add(key);
            }
            int childCnt = list.get(index++);
            node.isLeaf = (childCnt == 0);
            for (int i = 0; i < childCnt; i++) {
                Node child = fromList(index, list);
                node.children.add(child);
            }
            return node;
        }
    }

    public boolean isSameNode(Node node) {
        return (isSameKeys(node) && this.isLeaf == node.isLeaf && isSameChildren(node));
    }

    private boolean isSameKeys(Node node) {
        if (this.keys.size() == node.keys.size()) {
            for (int i = 0; i < this.keys.size(); i++) {
                if (this.key(i) != node.key(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isSameChildren(Node node) {
        if (this.children.size() == node.children.size()) {
            for (int i = 0; i < this.children.size(); i++) {
                if (!this.child(i).isSameNode(node.child(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return this.toList().equals(node.toList());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toList().hashCode();
    }
}
