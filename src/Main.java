import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BTree tree = new BTree();
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for ( int i=0; i<100; i++) {
            Integer key = random.nextInt(999);
            tree.insertKey(key);
        }
        BTree tree1 = tree.copy();
        BTree tree2 = BTree.fromList(tree.toList());
        // 속성을 제귀를 이용하여 동일성 체크
        System.out.println(tree.isSameTree(tree1));
        System.out.println(tree.isSameTree(tree2));
        System.out.println(tree1.isSameTree(tree2));
        // 트리의 리스트 표현을 이용하여 동일성 체크
        System.out.println(tree.equals(tree1));
        System.out.println(tree.equals(tree2));
        System.out.println(tree1.equals(tree2));
        // 트리를 파일로 읽고 쓰기
        tree.toFile("tree.dat");
        BTree tree3 = BTree.fromFile("tree.dat");
        System.out.println(tree.equals(tree3));
    }
}
