import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTree {

    private Node root;

    final static int DEGREE = 2;

    public BTree() {
        root = new Node();
    }

    public Node getRoot() {
        return this.root;
    }

    public BTree copy() {
        BTree tree = new BTree();
        tree.root = this.root.copy();
        return tree;
    }

    void splitChild(Node x, int i) {
        Node z = new Node();
        Node y = x.child(i);
        z.isLeaf = y.isLeaf;
        z.n = DEGREE - 1;
        z.keys = y.key(DEGREE, DEGREE * 2 - 1);
        if (y.isNonLeaf()) {
            z.children = y.child(DEGREE, DEGREE * 2);
        }
        x.children.add(i + 1, z);
        x.keys.add(i, y.key(DEGREE - 1));
        x.n++;
        y.n = DEGREE - 1;
        y.keys = y.key(0, DEGREE - 1);
        if (y.isNonLeaf()) {
            y.children = y.child(0, DEGREE);
        }
    }

    public void insertKey(Integer key) {
        Node r = this.root;
        if (r.isFull()) {
            Node s = new Node();
            s.isLeaf = false;
            s.children.add(r);
            this.root = s;
            splitChild(s, 0);
            insertNonFull(s, key);
        } else {
            insertNonFull(r, key);
        }
    }

    public void insertNonFull(Node node, Integer key) {
        if (node.isLeaf()) {
            node.n++;
            node.keys.add(key);
            Collections.sort(node.keys);
        } else {
            int idx = node.n - 1;
            while (idx >= 0 && node.key(idx) > key) {
                idx--;
            }
            idx += 1;
            if (node.child(idx).isFull()) {
                splitChild(node, idx);
                if (key > node.key(idx)) {
                    idx += 1;
                }
            }
            insertNonFull(node.child(idx), key);
        }
    }

    public static BTree fromList(List<Integer> list) {
        BTree tree = new BTree();
        tree.root = new Node.Loader().fromList(list);
        return tree;
    }

    public List<Integer> toList() {
        return this.root.toList();
    }

    public boolean isSameTree(BTree tree) {
        return this.getRoot().isSameNode(tree.getRoot());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BTree) {
            BTree tree = (BTree) obj;
            return this.getRoot().equals(tree.getRoot());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toList().hashCode();
    }

    public void toFile(String fname) {
        File file = new File(fname);
        if (file.exists()) file.delete();
        toFile(file);
    }

    public void toFile(File file) {
        try (
            FileOutputStream os = new FileOutputStream(file, false);
            DataOutputStream dout = new DataOutputStream(os);
        ) {
            List<Integer> list = this.toList();
            dout.writeInt(list.size());
            for (Integer i : this.toList()) dout.writeInt(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BTree fromFile(String fname) {
        return fromFile(new File(fname));
    }

    private static BTree fromFile(File file) {
        if (file.exists()) {
            try (
                FileInputStream is = new FileInputStream(file);
                DataInputStream din = new DataInputStream(is);
            ) {
                int size = din.readInt();
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(din.readInt());
                }
                return BTree.fromList(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("파일이 존재하지 않습니다.");
        }
        throw new RuntimeException("파일 로딩에 실패하였습니다.");
    }


}
