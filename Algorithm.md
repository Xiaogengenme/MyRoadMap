# 《剑指Offer》

## （一）面试流程

## （二）基础知识

### 2.1、2.2 基础知识

#### 1

是一个C++基础题

#### 2. 实现单例模式

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

### 2.3 数据结构

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

##### ⭐️数组中的二分查找

