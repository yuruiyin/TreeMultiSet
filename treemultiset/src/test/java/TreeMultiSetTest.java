import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class TreeMultiSetTest {

    /**
     * 测试是否成功new了一个TreeMap。（因为TreeMultiSet需要基于TreeMap的）
     *     public TreeMultiSet() {
     *         this(new TreeMap<E, Integer>());
     *     }
     */
    @Test
    public void constructor1() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        try {
            Class<?> clazz = Class.forName(TreeMultiSet.class.getName());
            Field treeMapField = clazz.getDeclaredField("treeMap");
            treeMapField.setAccessible(true);
            TreeMap treeMap = (TreeMap) treeMapField.get(set);
            assertNotNull("构造函数有误，没有new TreeMap", treeMap);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试传入的比较器是否生效
     *     TreeMultiSet(Comparator<? super E> comparator) {
     *         this(new TreeMap<>(comparator));
     *     }
     */
    @Test
    public void constructor2() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>((o1, o2) -> o2 - o1); // 传一个从大到小的比较器
        set.add(2);
        set.add(1);
        set.add(2);

        int[] expectedRes = new int[]{2, 2, 1};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * 测试传入的Collection是否生效
     *     TreeMultiSet(Collection<? extends E> c) {
     *         this();
     *         addAll(c);
     *     }
     */
    @Test
    public void constructor3() {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(2);
        TreeMultiSet<Integer> set = new TreeMultiSet<>(list); // 直接将已有部分元素的list传入

        int[] expectedRes = new int[]{1, 2, 2};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * 测试传入的SortedSet是否生效, 如传入treeSet, 或者TreeMultiSet本身
     *     TreeMultiSet(SortedSet<E> s) {
     *         this(s.comparator());
     *         addAll(s);
     *     }
     */
    @Test
    public void constructor4() {
        SortedSet<Integer> multiSet = new TreeMultiSet<>();
        multiSet.add(1);
        multiSet.add(2);
        multiSet.add(1);
//        Set<Integer> treeSet = new TreeSet<>();
//        treeSet.add(2);
//        treeSet.add(1);
        TreeMultiSet<Integer> set = new TreeMultiSet<>(multiSet); // 直接将已有部分元素的list传入

        int[] expectedRes = new int[]{1, 1, 2};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void diffIterator() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        Iterator<Integer> diffIterator = set.diffIterator();
        int[] expectedRes = new int[]{1, 2, 3};
        int[] actualRes = new int[3];
        int i = 0;
        while (diffIterator.hasNext()) {
            actualRes[i++] = diffIterator.next();
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void diffDescendingIterator() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        Iterator<Integer> diffDescendingIterator = set.diffDescendingIterator();
        int[] expectedRes = new int[]{3, 2, 1};
        int[] actualRes = new int[3];
        int i = 0;
        while (diffDescendingIterator.hasNext()) {
            actualRes[i++] = diffDescendingIterator.next();
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void iteratorNextException() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        try {
            iterator.next();
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchElementException);
        }
    }

    @Test
    public void iterator() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        Iterator<Integer> iterator = set.iterator();
        int[] expectedRes = new int[]{1, 2, 2, 3};
        int[] actualRes = new int[4];
        int i = 0;
        while (iterator.hasNext()) {
            actualRes[i++] = iterator.next();
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void descendingIterator() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        Iterator<Integer> descendingIterator = set.descendingIterator();
        int[] expectedRes = new int[]{3, 2, 2, 1};
        int[] actualRes = new int[4];
        int i = 0;
        while (descendingIterator.hasNext()) {
            actualRes[i++] = descendingIterator.next();
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void descendingSet() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> descendingSet = (TreeMultiSet<Integer>) set.descendingSet();
        int[] expectedRes = new int[]{3, 2, 2, 1};
        int[] actualRes = new int[4];
        int i = 0;
        for (Integer num : descendingSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     *     public SortedSet<E> subSet(E fromElement, E toElement) {
     *         return subSet(fromElement, true, toElement, false);
     *     }
     */
    @Test
    public void subSetReturnSortedSet() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> subSet = (TreeMultiSet<Integer>) set.subSet(2, 4); // 不包含4
        int[] expectedRes = new int[]{2, 2, 3};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : subSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
     * fromInclusive = true, toInclusive = true
     */
    @Test
    public void subSet1() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> subSet = (TreeMultiSet<Integer>) set.subSet(2, true, 3, true);
        int[] expectedRes = new int[]{2, 2, 3};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : subSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
     * fromInclusive = true, toInclusive = false
     */
    @Test
    public void subSet2() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> subSet = (TreeMultiSet<Integer>) set.subSet(2, true, 3, false);
        int[] expectedRes = new int[]{2, 2};
        int[] actualRes = new int[2];
        int i = 0;
        for (Integer num : subSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
     * fromInclusive = false, toInclusive = true
     */
    @Test
    public void subSet3() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> subSet = (TreeMultiSet<Integer>) set.subSet(2, false, 3, true);
        int[] expectedRes = new int[]{3};
        int[] actualRes = new int[1];
        int i = 0;
        for (Integer num : subSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
     * fromInclusive = false, toInclusive = false
     */
    @Test
    public void subSet4() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> subSet = (TreeMultiSet<Integer>) set.subSet(1, false, 6, false);
        int[] expectedRes = new int[]{2, 2, 3, 4, 4};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : subSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void headSet1() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> headSet = (TreeMultiSet<Integer>) set.headSet(3, true);
        int[] expectedRes = new int[]{1, 2, 2, 3};
        int[] actualRes = new int[4];
        int i = 0;
        for (Integer num : headSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     *     public SortedSet<E> headSet(E toElement) {
     *         return headSet(toElement, false);
     *     }
     */
    @Test
    public void headSetReturnSortedSet() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> headSet = (TreeMultiSet<Integer>) set.headSet(3);
        int[] expectedRes = new int[]{1, 2, 2};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : headSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void headSet2() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> headSet = (TreeMultiSet<Integer>) set.headSet(3, false);
        int[] expectedRes = new int[]{1, 2, 2};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : headSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void tailSeReturnSortedSet() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> tailSet = (TreeMultiSet<Integer>) set.tailSet(2);
        int[] expectedRes = new int[]{2, 2, 3, 4, 4};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : tailSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void tailSe1() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> tailSet = (TreeMultiSet<Integer>) set.tailSet(2, true);
        int[] expectedRes = new int[]{2, 2, 3, 4, 4};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : tailSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void tailSe2() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(4);
        set.add(4);
        set.add(2);
        set.add(1);
        set.add(2);
        set.add(3);

        TreeMultiSet<Integer> tailSet = (TreeMultiSet<Integer>) set.tailSet(2, false);
        int[] expectedRes = new int[]{3, 4, 4};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : tailSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void comparator() {
        TreeMultiSet<Integer> treeMultiSet = new TreeMultiSet<>((o1, o2) -> o2 - o1);
        Comparator comparator = treeMultiSet.comparator();
        Comparator newCmp = comparator.reversed(); // 修改方向，原本倒序，reverse之后又变成正序, 注意不是原地修改
        TreeMultiSet<Integer> treeMultiSet1 = new TreeMultiSet<>(newCmp);
        treeMultiSet1.add(2);
        treeMultiSet1.add(1);

        int[] expectedRes = new int[]{1, 2};
        int[] actualRes = new int[2];
        int i = 0;
        for (Integer num : treeMultiSet1) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void size() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int expectedSize = 5;
        int actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void diffElementSize() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int expectedSize = 3;
        int actualSize = set.diffElementSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void firstNull() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        assertNull(set.first());
    }

    @Test
    public void first1() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int expectedFirst = 1;
        int actualFirst = set.first();
        assertEquals(expectedFirst, actualFirst);
    }

    @Test
    public void first2() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>((o1, o2) -> o2 - o1);
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int expectedFirst = 3;
        int actualFirst = set.first();
        assertEquals(expectedFirst, actualFirst);
    }

    @Test
    public void lasdtNull() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        assertNull(set.last());
    }

    @Test
    public void last1() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int expectedLast = 3;
        int actualLast = set.last();
        assertEquals(expectedLast, actualLast);
    }

    @Test
    public void last2() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>((o1, o2) -> o2 - o1);
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int expectedLast = 1;
        int actualLast = set.last();
        assertEquals(expectedLast, actualLast);
    }

    @Test
    public void contains() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>((o1, o2) -> o2 - o1);
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

//        boolean expectedRes = false;
//        boolean actualRes = set.contains(4);
        boolean expectedRes = true;
        boolean actualRes = set.contains(3);
        assertEquals(expectedRes, actualRes);
    }

    @Test
    public void clear() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>((o1, o2) -> o2 - o1);
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        System.out.println("clear之前");
        for (Integer num : set) {
            System.out.print(num + " ");
        }
        System.out.println();

        boolean expectedRes = true;
        set.clear();
        boolean actualRes = set.size() == 0 && set.diffElementSize() == 0 && set.isEmpty();
        System.out.println("clear之后");
        for (Integer num : set) {
            System.out.println(num);
        }
        assertEquals(expectedRes, actualRes);
    }

    @Test
    public void addOneCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        int[] expectedRes = new int[]{1, 1, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void addSomeCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2, 3);
        set.add(1, 1);
        set.add(3, 0);
        set.add(3, 2);

        int[] expectedRes = new int[]{1, 2, 2, 2, 3, 3};
        int[] actualRes = new int[6];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void setCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.setCount(2, 3);
        set.add(3, 2);

        int[] expectedRes = new int[]{2, 2, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void count() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.setCount(2, 3);
        set.add(3);
        set.add(2);

        int expectedCount = 4;
        int actualCount = set.count(2);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void removeOneCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        set.remove(3);
        int[] expectedRes = new int[]{1, 1, 2, 3};
        int[] actualRes = new int[4];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void removeSomeCountNotOk() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        boolean expectedIsOk = false; // 删除的数量超过已有的数量
        boolean actualIsOk = set.remove(3, 3);
        assertEquals(expectedIsOk, actualIsOk);
        int[] expectedRes = new int[]{1, 1, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void removeSomeCountOk() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        boolean expectedIsOk = true; // 删除的数量超过已有的数量
        boolean actualIsOk = set.remove(3, 1);
        assertEquals(expectedIsOk, actualIsOk);
        int[] expectedRes = new int[]{1, 1, 2, 3};
        int[] actualRes = new int[4];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void removeZeroCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        boolean expectedIsOk = false; // 删除的数量超过已有的数量
        boolean actualIsOk = set.remove(3, 0);
        assertEquals(expectedIsOk, actualIsOk);
        int[] expectedRes = new int[]{1, 1, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void removeNotContainElement() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        boolean expectedIsOk = false; // 删除的数量超过已有的数量
        boolean actualIsOk = set.remove(5, 1);
        assertEquals(expectedIsOk, actualIsOk);
        int[] expectedRes = new int[]{1, 1, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }


    /**
     * 删除所有的指定元素，如2有三个，则把3个2全删了, 执行的就是treeMap的remove key的操作
     */
    @Test
    public void removeAllCountOk() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        boolean expectedIsOk = true;
        boolean actualIsOk = set.removeAll(1);
        assertEquals(expectedIsOk, actualIsOk);

        int[] expectedRes = new int[]{2, 3, 3};
        int[] actualRes = new int[3];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    /**
     * 删除所有的指定元素，如2有三个，则把3个2全删了, 执行的就是treeMap的remove key的操作
     */
    @Test
    public void removeAllCountNotOk() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        boolean expectedIsOk = false;
        boolean actualIsOk = set.removeAll(4);
        assertEquals(expectedIsOk, actualIsOk);

        int[] expectedRes = new int[]{1, 1, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : set) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }

    @Test
    public void lower() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        Integer expectedLower = 2;
        Integer actualLower = set.lower(3);
        assertNotNull(actualLower);
        assertEquals(expectedLower, actualLower);

        Integer expectedLower1 = 3;
        Integer actualLower1 = set.lower(4);
        assertNotNull(actualLower1);
        assertEquals(expectedLower1, actualLower1);

        Integer expectedLower2 = null;
        Integer actualLower2 = set.lower(1);
        assertEquals(expectedLower2, actualLower2);
    }

    @Test
    public void floor() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        Integer expectedFloor = 2;
        Integer actualFloor = set.floor(2);
        assertNotNull(actualFloor);
        assertEquals(expectedFloor, actualFloor);

        Integer expectedFloor1 = 1;
        Integer actualFloor1 = set.floor(1);
        assertNotNull(actualFloor1);
        assertEquals(expectedFloor1, actualFloor1);

        Integer expectedFloor2 = null;
        Integer actualFloor2 = set.floor(0);
        assertEquals(expectedFloor2, actualFloor2);
    }

    @Test
    public void higher() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        Integer expectedHigher = 2;
        Integer actualHigher = set.higher(1);
        assertNotNull(actualHigher);
        assertEquals(expectedHigher, actualHigher);

        Integer expectedHigher1 = 1;
        Integer actualHigher1 = set.higher(0);
        assertNotNull(actualHigher1);
        assertEquals(expectedHigher1, actualHigher1);

        Integer expectedHigher2 = null;
        Integer actualHigher2 = set.higher(4);
        assertEquals(expectedHigher2, actualHigher2);
    }

    @Test
    public void ceiling() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        Integer expectedCeiling = 2;
        Integer actualCeiling = set.ceiling(2);
        assertNotNull(actualCeiling);
        assertEquals(expectedCeiling, actualCeiling);

        Integer expectedCeiling1 = 3;
        Integer actualCeiling1 = set.ceiling(3);
        assertNotNull(actualCeiling);
        assertEquals(expectedCeiling1, actualCeiling1);

        Integer expectedCeiling2 = null;
        Integer actualCeiling2 = set.ceiling(4);
        assertEquals(expectedCeiling2, actualCeiling2);
    }

    @Test
    public void pollFirstSomeCountEmptySet() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();

        // 当前first 有3个1
        Integer expectedRes = null;
        Integer actualRes = set.pollFirst(3);
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 0;
        Integer actualLeftCount = set.count(1);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 0;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollFirstSomeCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);
        set.add(1);

        // 当前first 有3个1
        Integer expectedRes = 1;
        Integer actualRes = set.pollFirst(2);
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 1;
        Integer actualLeftCount = set.count(1);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 4;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollFirstOneCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);
        set.add(1);

        // 当前first 有3个1
        Integer expectedRes = 1;
        Integer actualRes = set.pollFirst();
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 2;
        Integer actualLeftCount = set.count(1);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 5;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollFirstAllCountNull() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        assertNull(set.pollFirstAll());
    }

    @Test
    public void pollFirstAllCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);
        set.add(1);

        // 当前first 有3个1
        Integer expectedRes = 1;
        Integer actualRes = set.pollFirstAll();
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 0;
        Integer actualLeftCount = set.count(1);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 3;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollLastSomeCountEmptySet() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();

        // 当前first 有3个1
        Integer expectedRes = null;
        Integer actualRes = set.pollLast(3);
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 0;
        Integer actualLeftCount = set.count(1);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 0;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollLastSomeCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);
        set.add(1);

        // 当前first 有3个1
        Integer expectedRes = 3;
        Integer actualRes = set.pollLast(2);
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 0;
        Integer actualLeftCount = set.count(3);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 4;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollLastOneCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);
        set.add(1);

        // 当前first 有3个1
        Integer expectedRes = 3;
        Integer actualRes = set.pollLast();
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 1;
        Integer actualLeftCount = set.count(3);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 5;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void pollLastAllCountNull() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        assertNull(set.pollLastAll());
    }

    @Test
    public void pollLastAllCount() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);
        set.add(1);

        // 当前first 有3个1
        Integer expectedRes = 3;
        Integer actualRes = set.pollLastAll();
        assertEquals(expectedRes, actualRes);

        Integer expectedLeftCount = 0;
        Integer actualLeftCount = set.count(3);
        assertEquals(expectedLeftCount, actualLeftCount);

        Integer expectedSize = 4;
        Integer actualSize = set.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testClone() {
        TreeMultiSet<Integer> set = new TreeMultiSet<>();
        set.add(2);
        set.add(3);
        set.add(1);
        set.add(3);
        set.add(1);

        TreeMultiSet<Integer> cloneSet = null;
        try {
            cloneSet = (TreeMultiSet<Integer>) set.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        int[] expectedRes = new int[]{1, 1, 2, 3, 3};
        int[] actualRes = new int[5];
        int i = 0;
        for (Integer num : cloneSet) {
            actualRes[i++] = num;
        }
        assertArrayEquals(expectedRes, actualRes);
    }
}