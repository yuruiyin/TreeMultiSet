import java.util.Random;

/**
 * PerformanceTest
 *
 * @author: yry
 * @date: 2020/3/16
 */
public class PerformanceTest {

    private static final int MAXN = 1000000;

    public static void main(String[] args) {
        Random random = new Random();
        TreeMultiSet<Integer> treeMultiSet = new TreeMultiSet<>();
        for (int i = 0; i < MAXN; i++) {
            treeMultiSet.add(i);
        }

        System.out.println("size: " + treeMultiSet.size());
        System.out.println("diffElementSize: " + treeMultiSet.diffElementSize());
    }

}
