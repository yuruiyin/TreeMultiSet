# TreeMultiSet
基于TreeMap实现的支持可重复元素的TreeSet
[github地址，欢迎star](https://github.com/yuruiyin/TreeMultiSet)

## 为什么要开发这个数据结构
搞过java的人应该都知道`TreeSet`，但是`TreeSet`是不支持重复元素的。有人会说，那用`ArrayList`或`LinkedList`不就可以了吗？
确实，`ArrayList`或`LinkedList`天然不去重，可以满足支持重复元素的需求。但是，我不仅需要支持可重复元素，而且需要数据实时保持有序。
这里有人又会说了，有序不很简单么？我把数据插入到`ArrayList`或`LinkedList`中之后，调用`sort`不就完事了吗？
嗯，确实，如果你的列表不经常发生变化（`sort`的次数非常非常少），那么你使用`List+sort`当然没问题咯。但是，如果数据是不断发生变化的怎么办呢？
如果数据不断发生变化，而且又需要保证数据实时有序（比如有实时查询最小和最大的需求），如果你使用`List+sort`的方式这样的时间复杂度非常高：
大概是插入排序的思路，这样的单次插入时间复杂度是$O(n)$，如果有n个元素，则需要$O(n^2)$。因此呢，如果我们要降低时间复杂度，就不能使用`List`。
然后，接下来又有人说了，我直接用`TreeMap`似乎可以啊。`TreeMap`的`key`是元素，`value`是`key`对应元素出现的次数。这样就可以满足插入可重复且有序的需求了。
的的确确，这个确实可以大致满足可重复且有序的需求。但是，我这里举几个使用`TreeMap`来满足可重复且有序需求的缺点：

1. 如果我们需要知道可重复元素集合的个数（重复元素算多个），使用`TreeMap`不能立马且在$O(1)$时间复杂度之内获取到，
而需要`for`循环所有`key`，然后计算所有`value`值之和。大致代码如下：
```java
    int size = 0;
    for (Integer num : treeMap.keySet()) {
        size += treeMap.get(num);
    }
```
这样看起来是不是有点麻烦？获取集合个数我们期望的应该是下面这样这样简洁（`TreeMultiSet`就能达到，下面会介绍）：
```java
    set.size();
```

2. 如果我们需要遍历集合中的所有元素，重复元素需要遍历多次（类似`list`的遍历）。那么使用TreeMap来实现需要使用二重循环，不太直观。如下所示：
```java
    for (Integer num : treeMap.keySet()) {
        int count = treeMap.get(num);
        while ((count--) > 0) {
            // 需要使用循环将num输出count次
            System.out.println(num);
        }
    }
```
同样，我们期望的应该是一重循环搞定，像下面这样：
```java
    for (Integer num : set) {
        System.out.println(num);
    }
```

3. 如果我们需要删除集合中指定个数的某个元素。举个例子，集合`[1, 2, 2, 3, 3, 3]`。我只想删除两个`3`，`TreeMap`需要这么做：
```java
    if (treeMap.containsKey(3)) {
        treeMap.put(2, treeMap.get(3) - 2);
    }
```
同样，我们期望的应该是像下面这样：
```java
    set.remove(3, 2); // 第二个参数代表需要删除的元素的个数
```

4. 如果我们需要往集合中添加指定数量的元素。举个例子，集合`[1, 2, 2, 2, 4]`，我们需要添加两个`4`，`TreeMap`需要这么做：
```java
    treeMap.put(4, treeMap.getOrDefault(4, 0) + 2);
```
同样，我们期望的应该像下面这样：
```java
    set.add(4, 2); // 第二个参数代表需要插入的元素的个数
```

除此之外，`TreeMap`来实现可重复`TreeSet`的功能还有一些不太优雅的地方，这里就不一一列举了。

因此，基于以上原因，`TreeMultiSet`诞生了。（不过声明一下：不是重复造轮子，而是站在巨人的肩膀上，`TreeMultiSet`大部分是参考`TreeSet`来实现的，
其实都是基于`TreeMap`，而`TreeMap`底层是通过红黑树`(Red-Black-Tree)`来实现的，这也正是`TreeSet`的`remove`操作可达到`O(logN)`时间复杂度的本质原因，有兴趣的同学可以去研究一下）

## 如何使用
### gradle

Step 1. Add the JitPack repository in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency in your app build.gradle:
```
dependencies {
	implementation 'com.github.yuruiyin:TreeMultiSet:1.0.1'
}
```

### 功能列表
功能描述|对应方法
:---|:---
无参构造函数 | TreeMultiSet()
带比较器参数的构造函数 | TreeMultiSet(Comparator<? super E> comparator)
带集合参数构造函数 | TreeMultiSet(Collection<? extends E> c)
带SortedSet参数构造函数 | TreeMultiSet(SortedSet<E> s)
返回所有元素（重复元素要next多次）的正向迭代器 | Iterator<E> iterator()
返回所有元素（重复元素要next多次）的反向迭代器 | Iterator<E> descendingIterator()
返回所有不相同元素的正向迭代器 | Iterator<E> diffIterator()
返回所有不相同元素的反向迭代器 | Iterator<E> diffDescendingIterator()
返回逆序Set | NavigableSet<E> descendingSet()
返回指定头尾元素的连续子集 | NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
返回头部连续子集 | NavigableSet<E> headSet(E toElement, boolean inclusive)
返回尾部连续子集 | NavigableSet<E> tailSet(E fromElement, boolean inclusive)
返回比较器 | Comparator<? super E> comparator()
返回总的元素个数（重复算多个） | int size()
返回不同的元素个数 | int diffElementSize()
获取第一个元素 | E first()
获取最后一个元素 | E last()
判断是否包含某个元素 | boolean contains(Object o)
清空所有元素 | void clear()
添加指定元素(1个) | boolean add(E e)
添加指定个数的特定元素 | boolean add(E e, int count)
设置指定元素的数量 | void setCount(E e, int count)
获取指定元素的个数 | int count(E e)
删除1个指定元素 | boolean remove(Object e)
删除count个指定元素 | boolean remove(E e, int count)
删除所有的指定元素(不同于clear()) | boolean removeAll(Object e)
返回比给定元素严格小的最大元素 | E lower(E e)
返回小于或等于给定元素的最大元素 | E floor(E e)
返回比给定元素严格大的最小元素 | E higher(E e)
返回大于或等于给定元素的最小元素 | E ceiling(E e)
删除指定count的第一个元素 | E pollFirst(int count)
删除1个第一个元素 | E pollFirst()
删除所有count的第一个元素 | E pollFirstAll()
删除1个最后一个元素 | E pollLast()
删除指定count的最后一个元素 | E pollLast(int count)
删除所有count的最后一个元素 | E pollLastAll()
TreeMultiSet浅拷贝 | Object clone()

### 代码演示
这里举几个觉得常用的一些方法的调用方式（当然也可以直接阅读[TreeMultiSet源码](https://github.com/yuruiyin/TreeMultiSet/blob/master/treemultiset/src/main/java/TreeMultiSet.java), 里头有详细的注释)。
#### 1) 新建一个自定义比较器的TreeMultiSet
```java
    TreeMultiSet<Integer> set = new TreeMultiSet<>((o1, o2) -> o2 - o1); // 传一个从大到小的比较器
```

#### 2) foreach遍历所有元素(包含重复元素)
```java
    for (Integer num : set) {
        // TODO, 这里可输出类似2, 2, 2, 3这样的序列
    }
```

#### 3) 获取所有元素(包含重复元素)的正向迭代器
```java
    Iterator<Integer> iterator = set.iterator();
    while (iterator.hasNext()) {
        arr[i++] = iterator.next();
    }
```

#### 4) 获取不同元素的正向迭代器
```java
    Iterator<Integer> diffIterator = set.diffIterator();
    while (diffIterator.hasNext()) {
        arr[i++] = diffIterator.next();
    }
```

#### 5) 获取所有元素的总个数(包含重复元素)
```java
    set.size();
```

#### 6) 获取不同元素的个数
```java
    set.diffElementSize();
```

#### 7) 获取第一个元素和最后一个元素
```java
    set.first();
    set.last();
```

#### 8) 添加指定数量的元素
```java
    set.add(2, 3); //往集合里头添加3个2
```

#### 9) 设置指定元素的数量
```java
    set.setCount(2, 3); //设置集合里头2的个数为3个
```

#### 10) 获取指定元素的个数
```java
    set.count(2); //获取2在集合中的个数
```

#### 11) 删除指定数量的元素
```java
    set.remove(2, 3); //删除3个2
```

#### 12) 删除指定数量的第一个元素
```java
    set.pollFirst(2); //删除2个第一个元素。如集合[1,1,1,2,2,3]，执行这行代码之后变成[1,2,2,3]
```

#### 13) 删除指定数量的最后一个元素
```java
    set.pollLast(1); //删除1个最后一个元素。如集合[1,1,1,2,3,3]，执行这行代码之后变成[1,1,1,2,2,3]
```

### 单元测试
已经对`TreeMultiSet`的所有方法（包括构造函数）都进行了单元测试[TreeMultiSetTest](https://github.com/yuruiyin/TreeMultiSet/blob/master/treemultiset/src/test/java/TreeMultiSetTest.java)，测试覆盖率达到100%。

![Coverage](https://pic.leetcode-cn.com/093de4dc4743258dcc835e3e11f43f1ea22b31ae0e0e2fa66d07e64798918211-file_1586603359898)

### 各种集合对比
说明，以下列举的复杂度均指**时间复杂度**。并且以下插入删除操作均指对中间元素的操作。同时，计算`LinkedList`的插入和删除时间复杂度的时候考虑了查询到要插入或删除的位置的时间。

集合 | 是否可重复 | 是否有序 | 插入复杂度 | 删除复杂度 | 获取最大最小复杂度
:---|:---|:---|:---|:---|:---
ArrayList | 是 | 否 | $O(n)$ | $O(n)$ | $O(n)$
LinkedList | 是 | 否 | $O(n)$ | $O(n)$ | $O(n)$
HashSet | 否 | 否 | $O(1)$ | $O(1)$ | $O(n)$
TreeSet | 否 | 是 | $O(log n)$ | $O(log n)$ | $O(log n)$
PriorityQueue | 是 | 是 | $O(log n)$ | $O(n)$ | $O(log n)$
TreeMultiSet | 是 | 是 | $O(log n)$ | $O(log n)$ | $O(log n)$

由上表可以发现本库实现的`TreeMultiSet`功能最强大，在保证可重复有序的情况下，持频繁插入删除操作的时间复杂度都可以达到$O(log n)$的级别。当然，应该具体问题具体分析。比如，没有`remove`中间某个元素且需要可重复元素有序的情况下，`PriorityQueue`(底层实现是堆)的性能最佳。

## 最后
觉得还不错的话，就点个star支持一下咯~
