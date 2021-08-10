* 结构思维，归类思想
* 做笔记：简单的思路提示
* 复习！！！
* 精益求精：看看别人写的好的代码
* 有意识培养自己bug free的能力，写完了就不要有一些数组越界什么的问题改来改去
* 锻炼自己的代码熟练度！有思路快速实现（进阶
* **要先有结题思路再写，不要写着看也不要背代码！！！**



### Trust the process, make it simple

* 每道题要先想出思路，简单模拟一下，然后再开始编写代码

* 先想一想能不能暴力的解决一下，之后再想如何进行优化

  主要的优化方法：

  1. 查找的情况：二分查找

* 每次写完之后先简单检查一遍，看看有没有低级拼写错误

  可能的拼写错误有：

  1. 单词拼写错误：return，length
  2. 数组错误：包括数组创建语句错误，数组的长度取值错误
  3. HashMap，ArrayList等泛型定义错误



#### 实现单例模式

> 思考导航项目中的naviData，naviPath对象为什么都要设计成单例模式

1. 第一个思考方向是对象内容复杂，创建和销毁的性能消耗巨大
2. 

##### 写法1：

```JAVA
public class Singleton {
  private Singleton() {}
  private static Singleton instance = null;
  public static Singleton getInstance() {
    if (instance == null) {
      instance = new Singleton();
    }
    return instance;
  }
}
/*
需要注意，这里的getIntance方法和instance对象都需要设置为static的，因为在使用getInstance方法时如果没有实例化对象就不能调用方法
*/
```

这种方法被称为懒汉式，

这样会有线程安全的问题，因为如果在判断instance == null之后但是在创建instance对象之前，有其他线程抢先创建了单例对象，那就不能保证是单例的

所以可以暴力地使用

### 数组

#### [剑指 Offer 03. 数组中重复的数字](https://leetcode-cn.com/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof/)

```java
class Solution {
    public int findRepeatNumber(int[] nums) {
        if (nums == null || nums.length <= 0) return -1;
      // 因为题目中所写的，n长的数组里存放n-1个数，所以每个数字都应该有自己的位置，遍历为每个数字找到位置，如果那个位置被占用了就证明重复了
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
          // 这里要注意，如果数字本身就在它自己的位置上，不加这句就会出错
            if (cur == i) {
                continue;
            }
          // 这里就是判断，目前遍历的数字目标的位置是不是已经被占了，被占就证明重复了
            if (nums[cur] == cur) {
                return cur;
            }
          // 如果没有被占那么就要把当前数字放到它应该在的位置
            nums[i] = nums[cur];
            nums[cur] = cur;
        }        
        return -1;
    }
}
```



#### [面试题 10.02. 变位词组](https://leetcode-cn.com/problems/group-anagrams-lcci/)

我自己想到的可能是计数的方法，因为题干中说只会出现小写字母，所以使用26长的数组就可以计数

但是超级慢，感觉思路太乱了最后也没改得很精简，过了就算了

```java
public static List<List<String>> groupAnagrams(String[] strs) {
        // 没想到什么办法，先暴力
        List<List<String>> result = new ArrayList();
        for (String str : strs) {
            boolean found = true;  // 找到队伍
            // 扫描列表中所有的String
            for (int i = 0; i < result.size(); i++) {
                found = true;
                String firstStr = result.get(i).get(0);
                int[] count = new int[26];
                // 如果长度都不一样，那么一定是不一样所以判断下一组
                if (firstStr.length() != str.length()) {
                    found = false;
                    continue;
                }
                // 为那一组的单词计数
                for (int k = 0; k < firstStr.length(); k++) {
                    char c = firstStr.charAt(k);
                    count[c - 'a']++;
                }
                // 判断是否有目标
                for (int k = 0; k < str.length(); k++) {
                    char c = str.charAt(k);
                    if (count[c - 'a'] == 0) {
                        found = false;
                        break;
                    } else {
                        count[c - 'a']--;
                    }
                }
                if (found == true) {
                    result.get(i).add(str);
                    break;
                }
            }
            if (result.size() == 0 || found == false) {
                List<String> tmp = new ArrayList();
                tmp.add(str);
                result.add(tmp);
            }
        }
        return result;
    }
```

更好的方法：用排序 + HashMap存储

```java
public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList();
        if (strs == null || strs.length == 0) return result;
        HashMap<String, Integer> collection = new HashMap();
        for (String str : strs) {
            // 将单词按字母排序后，出现过的单词肯定在map中
            char[] strArr = str.toCharArray();
            Arrays.sort(strArr);
            String key = String.valueOf(strArr);
            if (collection.containsKey(key)) {
                int index = collection.get(key);
                List<String> tmp = result.get(index);
                tmp.add(str);
            } else {
                collection.put(key, result.size());
                List<String> tmp = new ArrayList();
                tmp.add(str);
                result.add(tmp);
            }
        }
        return result;
    }
```

### 链表

#### [剑指 Offer 35. 复杂链表的复制](https://leetcode-cn.com/problems/fu-za-lian-biao-de-fu-zhi-lcof/)

```java
class Solution {
    public Node copyRandomList(Node head) {
        if (head == null) return null;
        // 1.创建一个复制的链表，并将新链表与旧链表相连
        Node p = head;
        while (p != null) {
            Node copyNode = new Node(p.val);
            copyNode.next = p.next;
            p.next = copyNode;
            p = copyNode.next;
        }
        // 2.复制random指针
        p = head;
        while (p != null) {
            Node nextNode = p.next;
            Node random = null;
            if (p.random != null) {
                random = p.random.next;
                nextNode.random = random;
            }
            p = nextNode.next;
        }
        // 3.摘出来复制的链表
        p = head;
        Node newHead = head.next;
        while (p != null) {
            Node nextNode = p.next;
            p.next = nextNode.next;
            if (nextNode.next == null) {
                nextNode.next = null;
            } else {
                nextNode.next = nextNode.next.next;
            }
            p = p.next;
        }
        return newHead;
    }
}
```



### 栈与队列

### 散列表

### 树

#### [1104. 二叉树寻路](https://leetcode-cn.com/problems/path-in-zigzag-labelled-binary-tree/)

不用重建二叉树，使用二叉树节点的序号和层数的关系来推算。

### 堆与优先队列

#### 堆与堆排序

##### 什么是堆？

堆是一种特殊的树，满足一下两点的树可以被称为堆：

1. 堆是一个完全二叉树
2. 堆中的每一个节点的值都大于等于（或小于等于）其子树中每个节点的值

##### 如何实现一个堆

因为是一个完全二叉树可以通过数组进行存储

```java
		int[] a;    // 数组，存储堆元素
    final int n;    // 可以存储的最大数量
    int count;  // 目前存储的数量

    public Heap(int capacity) {
        a = new int[capacity];
        n = capacity;
        count = 0;
    }
```



##### 堆的操作1：往堆中插入元素

向数组的最后一个位置进行插入，并且沿着最后一个位置的路径从下向上进行堆化（比较交换）

```java
public void insert(int num) {
        if (count == n) {
            System.out.println("堆满了不能再加元素了");
        }
        // 添加元素
        int index = count;
        a[index] = num;
        count++;

        // 重新调整为大顶堆：如果元素比它的父节点小就进行交换
        while (true) {
            if (a[index] > a[index / 2]) {
                swap(a, index, index / 2);
                index = index / 2;
            } else {
                break;
            }
        }
        print();
    }
```

##### 堆的操作2：删除堆顶元素

```java
public void removeTop() {
        // 如果堆里面没有元素就没办法移除堆顶元素
        if(count <= 0) {
            System.out.println("堆为空");
        }
        // 首先将堆顶元素与最后一个元素进行交换，
        //我们的count每次添加一个元素就加一，所以最后一个元素的下标是count - 1
        swap(a, 0, count - 1);
        a[count - 1] = 0;
        count--;
        // 重新堆化
        heapify();
    }

    /*
    从上往下堆化
     */
    public void heapify() {
        int i = 0;
        while (true) {
            int maxIndex = i;
            // 如果已经遍历到最后一个，就结束堆化
            if (i == count) {
                break;
            }
            // 找到i的子节点中更大的那一个，如果没有就结束循环，如果找到就交换
            if (i * 2 + 1 < count && a[i * 2 + 1] > a[i]) maxIndex = i * 2 + 1;
            if (i * 2 + 2 < count && a[i * 2 + 2] > a[i * 2 + 1]) maxIndex = i * 2 + 2;
            if (maxIndex == i) {
                break;
            }
            swap(a, i, maxIndex);
            i = maxIndex;
        }
        print();
    }
```

##### 堆排序

首先是对数组中的元素进行建堆，这是一个原地的操作，有两种办法：

1. 从下向上堆化，从第一个遍历到最后一个将数组中每一个元素插入到原有的堆中
2. 自上而下堆化，因为叶子节点不用堆化，从第一个不是叶子节点的开始堆化

![image-20210723133814473](/Users/xiaogengen/Library/Application Support/typora-user-images/image-20210723133814473.png)

![image-20210723133632757](/Users/xiaogengen/Library/Application Support/typora-user-images/image-20210723133632757.png)

但是我有一个问题？？？第一种方法算不算是一种原地的操作呢？？？

```java
/* 代码要对上面从上而下的堆化进行一些修改，从特定位置开始进行堆化 */
```

其次是排序操作，由于是大顶堆，所以每次我们把堆顶元素与**最后一个未排序位置**进行交换，然后对剩余位置重新进行堆化，为了原地不然的话可以直接用另外一个数组接收

最后：跌跌撞撞地写出了堆排序的代码

```java
public static void heapSort(int[] arr) {
        // 首先要找到数组中第一个不是叶子节点的位置，对其进行堆化，变成一个大顶堆
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            heapify(arr, i, arr.length - 1);
        }
        printHeap(arr);
        // 接下来就是将大顶堆的堆顶元素与最后一个未排序位置进行交换
        //之后对未排序位置进行重新的堆化
        for (int i = 0; i < arr.length; i++) {
            int end = arr.length - i - 1;
            Heap.swap(arr, 0, end);	// 交换堆顶和最后一位
            heapify(arr, 0, end - 1);	// 对未排序部分进行堆化
        }
        printHeap(arr);
        printArr(arr);
    }

    /**
     * 对数组的一部分进行堆化
     * @param arr
     * @param start: 根节点在数组中的位置
     * @param end: 结束点的位置
     */
    public static void heapify(int[] arr, int start, int end) {
        int i = start;
        while (true) {
            if (i >= end) break;
            int left = i * 2 + 1;
            int right = i * 2 + 2;
            int maxIndex = i;
            if (left <= end && arr[left] > arr[i]) maxIndex = left;
            if (right <= end && arr[right] > arr[left]) maxIndex = right;
            if (i == maxIndex) break;
            Heap.swap(arr, i, maxIndex);
            i = maxIndex;
        }
    }
```

##### 堆排序的时间复杂度和稳定性

堆排序中，建堆操作时间复杂度为On，每次堆化复杂度为Ologn，但是要堆化n次所以整体的时间复杂度为Onlogn

堆排序不是稳定的排序算法，**想象一个场景：数组中所有的元素都是相同的**，那么交换首尾时就会交换两个相同数字的位置，不稳定

#### 实现一个小顶堆、大顶堆、优先级队列

#### 实现堆排序



#### [218. 天际线问题](https://leetcode-cn.com/problems/the-skyline-problem/)

#### PriorityQueue解析

Java中的PriorityQueue通过完全二叉树实现小顶堆，每个父节点都比自己的左右子节点小。

PriorityQueue保证每次取出来的元素是其中权值最小的。这个权值可以通过重写Comparator构造器进行设置。

```java
/**
* 使用comparable接口来实现大顶堆
*/
PriorityQueue maxHeap = new PriorityQueue(11, new Comparator<Integer>{
    @Override
    public int compare(Integer i1, Integer i2) {
        return i2 - i1;
    }
})
```



![image-20210713163439389](/Users/xiaogengen/Library/Application Support/typora-user-images/image-20210713163439389.png)

priorityQueue可以直接使用数组进行存储，因为是完全二叉树，所以父节点与子节点的元素位置序号是可以直接计算的。

priority的peek和element操作都是常数时间复杂度，add、offer、无参数的remove、poll都是O(log(N))的。

1. add和offer的功能相同，不同点在于二者对于插入失败的处理方法不同，add是抛出异常，offer是返回false。由于涉及到插入之后重新堆化的过程，所以需要O(log(N))的时间复杂度；
2. element和peek的功能相同，不同点也是在于对于错误的处理方法，element是抛出异常，peek是返回null。由于返回堆顶元素就是数组中0位置的元素，所以时间是O(1)
3. remove与poll功能相同，不同点是remove失败后抛出异常，poll返回null。由于需要去除掉堆顶元素涉及到堆化，所以需要O(log(N))
4. remove(Object o)：删除某一个与o相等的元素，如果有多个就删一个。这个分为要删除最后一个元素还是其他元素。如果是要删除最后一个元素就很快，如果不是的话那需要一个堆化的过程。

![截屏2021-07-13 下午4.56.02](/Users/xiaogengen/Library/Application Support/typora-user-images/截屏2021-07-13 下午4.56.02.png)

#### 利用优先级队列合并k个有序数组

#### [703. 数据流中的第 K 大元素](https://leetcode-cn.com/problems/kth-largest-element-in-a-stream/)

#### 一个元问题：求数组中第k大的数

#### [239. 滑动窗口最大值](https://leetcode-cn.com/problems/sliding-window-maximum/)

#### [313. 超级丑数](https://leetcode-cn.com/problems/super-ugly-number/)









### 图

#### 深度优先搜索DFS

#### 广度优先搜索BFS

#### [802. 找到最终的安全状态](https://leetcode-cn.com/problems/find-eventual-safe-states/)

重要，这个是检测图中是否有环的问题，其中三色标记法是Java识别垃圾的重要算法

```java
class Solution {
    public List<Integer> eventualSafeNodes(int[][] graph) {
        List<Integer> result = new ArrayList<Integer>();
        int n = graph.length;
        if (n == 0) return result;
        int[] color = new int[n];
        for (int i = 0; i < n; i++) {
            if (isSafe(graph, color, i)) {
                result.add(i);
            }
        }
        return result;
    }

    public boolean isSafe(int[][] graph, int[] color, int i) {
        if (color[i] == 2) {
            return true;
        }
        else if (color[i] == 1) {
            return false;
        }
        else {
            for (int j : graph[i]) {
                color[i] = 1;
                if (!isSafe(graph, color, j)) {
                    return false;
                }
            }
        }
        color[i] = 2;
        return true;
    }
}
```

#### 拓扑排序：如何确定代码源文件的编译依赖关系？









### 排序

### 二分查找

#### [剑指 Offer 04. 二维数组中的查找](https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof/)

```java
class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;
        boolean result = false;
        for (int i = 0; i < matrix.length; i++) {
            if (binarySearch(matrix[i], target)) {
                return true;
            }
        }
        return false;
    }

  // 基础的二分查找
    public boolean binarySearch(int[] arr, int target) {
        int n = arr.length;
        if (target < arr[0] || target > arr[n - 1]) {
            return false;
        }
        int left = 0;
        int right = n - 1;
        while(left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return true;
            else if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return false;
    }
}
```

#### [1818. 绝对差值和](https://leetcode-cn.com/problems/minimum-absolute-sum-difference/)

二分查找降低复杂度的神奇例子：二分查找可以查找出最后一个小于等于目标元素的位置，和第一个大于等于目标元素的位置

```java
class Solution {
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        /*
        题解方法：
        根据题解：
        对于每个位置i，都需要找出替换这个位置的最佳数字，每次只需找到与位置i最接近的那个数字就可以了，这个查找可以使用二分查找 
        */
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0) return -1;
        int n = nums1.length;
        int[] nums1Copy = new int[n];
        for (int i = 0; i < nums1Copy.length; i++) {
            nums1Copy[i] = nums1[i];
        } 
        Arrays.sort(nums1Copy);
        long result = 0;
        for (int i = 0; i < n; i++) {
            result += Math.abs(nums1[i] - nums2[i]);
        }
        long maxChange = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            // 二分查找能够替换i位置nums1[i]的元素，与nums2[i]的距离最小
            int index1 = binarySearchLastSmaller(nums1Copy, nums2[i]);
            int index2 = binarySearchFirstLarger(nums1Copy, nums2[i]);
            int diff = Math.max((Math.abs(nums1[i] - nums2[i]) - Math.abs(nums1Copy[index1] - nums2[i])), (Math.abs(nums1[i] - nums2[i]) - Math.abs(nums1Copy[index2] - nums2[i])));
            if (diff > maxChange) {
                maxChange = diff;
            }
        }
        return (int)((result - maxChange) % (1000000000 + 7));
    }

    /**
    二分查找：第一个大于等于目标元素的元素位置
     */
    public int binarySearchFirstLarger(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] >= target) {
                if (mid == 0 || arr[mid - 1] < target) {
                    return mid;
                } else {
                    right = mid - 1;
                }
            } else {
                left = mid + 1;
            }
        }
        return 0;
    }

    /**
    二分查找，返回最后一个小于等于target的位置
     */
    public int binarySearchLastSmaller (int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] <= target) {
                if (mid == arr.length - 1 || arr[mid + 1] > target) {
                    return mid;
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }
        return arr.length - 1;
    }
}
```

#### [剑指 Offer 53 - I. 在排序数组中查找数字 I](https://leetcode-cn.com/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/)

二分查找很不错的例子!

需要注意的是两个边界问题: 

1. 查找的是最后一个小于，和第一个大于目标值的数字位置，没有等于
2. 需要注意如果没有找到对应的位置需要特殊处理

```java
class Solution {
    public int search(int[] nums, int target) {
        /**
        查找到数组中，最后一个小于目标元素的位置，和第一个大于目标元素的位置，返回b - a - 1
         */
        if (nums == null || nums.length == 0) return 0;
        int a = lastSmaller(nums, target);
        int b = firstBigger(nums, target);
        if (a == -1) {
            if (nums[0] == target) {
                a = -1;
            } else {
                return 0;
            }
        }
        if (b == -1) {
            if (nums[nums.length - 1] == target) {
                b = nums.length;
            } else {
                return 0;
            }
        }
        return b - a - 1;
    }

    public int lastSmaller(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                if (mid == nums.length - 1 || nums[mid + 1] >= target) {
                    return mid;
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public int firstBigger(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] > target) {
                if (mid == 0 || nums[mid - 1] <= target) {
                    return mid;
                } else {
                    right = mid - 1;
                }
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }


}

```

#### [611. 有效三角形的个数](https://leetcode-cn.com/problems/valid-triangle-number/)

本来以为是全排列的变形，回溯查找不同的全排列符合三角形三条边的三元组

但是写不出来全排列哈哈哈哈暂时找着题解里二分查找的方法

排序后通过二分查找后面数组中满足小于e1 + e2的元素个数，那就找第一个大于等于e1 + e2的位置

```java
class Solution {
    public int triangleNumber(int[] nums) {
        /**
        先排序，然后搜索每一个可能解 */
        int n = nums.length;
        Arrays.sort(nums);
        int result = 0;
        for (int left = 0; left < n - 2; left++) {
            for (int right = left + 1; right < n - 1; right++) {
                result += binary(nums, left, right);
            }
        }
        return result;
    }

    public int binary(int[] nums, int left, int right) {
        // 因为e3肯定比e1，e2大，所以只需要e3 < e1 + e2
        // 二分查找第一个大于等于e1 + e2的值
        int e1 = nums[left];
        int e2 = nums[right];
        int start = right + 1;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + ((end - start) >> 1);
            if (nums[mid] >= e1 + e2) {
                if (mid == start || nums[mid - 1] < e1 + e2) {
                    return (mid - right - 1);
                } else {
                    end = mid - 1;
                }
            } else {
                start = mid + 1;
            }
        }
        return nums.length - right - 1;    
    }
}
```













### 深度/广度优先搜索

### 字符串匹配算法

### Trie树

### AC自动机





### 贪心算法

### 分治算法

### 回溯算法

#### [863. 二叉树中所有距离为 K 的结点](https://leetcode-cn.com/problems/all-nodes-distance-k-in-binary-tree/)

我自己是采用先保存每个节点的父节点，然后从目标节点开始回溯地深度搜索（不知道这个是不是一种回溯，感觉像是）

```java
class Solution {
    List<Integer> result = new ArrayList();
    HashMap<TreeNode, TreeNode> parent = new HashMap();
    HashSet<TreeNode> road = new HashSet();
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        // 首先将父节点存储在parent map中
        storeParent(root);
        // 然后从target节点开始遍历，每次遍历结点的两个子节点和一个父节点
        visit(target, k);
        return result;
    }

    public void storeParent(TreeNode root) {
        if (root.left != null) {
            parent.put(root.left, root);
            storeParent(root.left);
        }
        if (root.right != null) {
            parent.put(root.right, root);
            storeParent(root.right);
        }   
    }

    public void visit(TreeNode root, int k) {
        if (root == null) return;
        if (road.contains(root)) {
            return;
        }
        if (road.size() == k) {
            result.add(root.val);
            return;
        }
        road.add(root);
        visit(root.left, k);
        visit(root.right, k);
        visit(parent.get(root), k);
        road.remove(root);
        return;
    }
}
```

#### [79. 单词搜索](https://leetcode-cn.com/problems/word-search/)

```java
class Solution {

    static int[] dx = new int[]{0, 0, 1, -1};
    static int[] dy = new int[]{1, -1, 0, 0};

    public boolean exist(char[][] board, String word) {
        char[] arr = word.toCharArray();
        if (arr.length == 0) return true;
        int m = board.length;
        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == arr[0]) {
                  // 查找开始位置，然后进行深度搜索，但是不能直接返回深度搜索结果，直接返回的话就是一次搜索的结果，如果有多个匹配的单词头就会报错，只有一次机会，所以要遍历了所有开始位置之后再返回（找到一个可以返回）
                    if (dfs(board, arr, 0, i, j, visited)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dfs(char[][] board, char[] arr, int index, int i, int j, boolean[][] visited) {
      // 回溯算法开始的条件判断，基本思路是先分发，然后让每个分发的任务自己先去判断条件合不合适，不合适直接返回
        if (index == arr.length) return true;
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) return false;
        if (visited[i][j] || board[i][j] != arr[index]) return false;
        visited[i][j] = true;
        boolean result = false;
        for (int k = 0; k < 4; k++) {
            result = result || dfs(board, arr, index + 1, i + dx[k], j + dy[k], visited);
        }
        if (result == false) {
            visited[i][j] = false;
            return false;
        }
        return true;
    }
}
```



### 动态规划

# 布隆过滤器

Filter的特点：使用Filter判断一个元素不在数据集中，那这个数据肯定不在；

如果一个元素在数据集中，那么有可能在有可能不在，需要再进行查询。

理解一下Cache和Filter互补的作用

![截屏2021-08-10 上午11.22.30](/Users/xiaogengen/Desktop/秋招/MyRoadMap/Algorithm.assets/截屏2021-08-10 上午11.22.30-8565773.png)

* 主要构成：一个**很长的二进制向量**（比如一个Integer是32位的二进制向量），和一个映射函数。
* 用于检索一个元素是否在一个集合中
  * 确定一个数据不存在，就一定不存在
  * 确定一个数据存在，那不一定在，有误识别
* 优点：空间效率和查询效率都远远优于其他算法
* 缺点：有误识别率和删除困难

![截屏2021-08-10 上午11.27.50](/Users/xiaogengen/Desktop/秋招/MyRoadMap/Algorithm.assets/截屏2021-08-10 上午11.27.50-8566084.png)

![截屏2021-08-10 上午11.29.48](/Users/xiaogengen/Desktop/秋招/MyRoadMap/Algorithm.assets/截屏2021-08-10 上午11.29.48-8566201.png)
