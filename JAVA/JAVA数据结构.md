# JAVA数据结构常用方法记录
### PriorityQueue（优先队列）
在java中，PriorityQueue是由二叉堆来实现的，具体源码可参考[https://www.yuque.com/snailclimb/gepg7u/udk0vsc9ahl1ltmq]。PriorityQueue是一个有序队列，按照有序性可分为大根堆和小根堆。
#### 创建PriorityQueue
``` java
PriorityQueue<Integer> numbers = new PriorityQueue<>();
```
这里，我们创建了一个没有任何参数的优先级队列。在这种情况下，优先级队列的头是队列中最小的元素（小根堆）。元素将按升序从队列中移除。
#### 将元素插入PriorityQueue
- add() - 将指定的元素插入队列。如果队列已满，则会引发异常。
- offer() - 将指定的元素插入队列。如果队列已满，则返回false。
#### 访问PriorityQueue元素
- peek()- 此方法返回队列的头部。
- poll() - 返回并删除队列的开头。
#### 删除PriorityQueue元素
- remove() - 从队列中删除指定的元素。
#### 遍历PriorityQueue
要遍历优先级队列的元素，我们可以使用iterator()方法。
``` java
//使用iterator()方法
Iterator<Integer> iterate = numbers.iterator();
while(iterate.hasNext()) {
    System.out.print(iterate.next());
    System.out.print(", ");
}
```
#### PriorityQueue其他方法
- contains(element) - 在优先级队列中搜索指定的元素。如果找到该元素，则返回true，否则返回false。
- size() - 返回优先级队列的长度。
- toArray() - 将优先级队列转换为数组，并返回它。
#### 重写比较器
``` java
//1. a-b 大于0则交换：即a>b则交换，即将小的元素往上移，即构建小根堆
PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> a - b);

//2.大根堆
PriorityQueue<Integer> numbers = new PriorityQueue<>(new CustomComparator());
class CustomComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer number1, Integer number2) {
        // value = number1-number2
        int value =  number1.compareTo(number2);
        //元素以相反的顺序排序
        if (value > 0) {
            return -1;
        }
        // 即number1<number2 返回正数：即交换；即将大的数往上移；构成大根堆
        else if (value < 0) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
```
### TreeMap
可以理解为有序的HashMap。
#### 创建一个TreeMap
``` java
TreeMap<Key, Value> numbers = new TreeMap<>();
```
#### 将元素插入TreeMap
- put() - 将指定的键/值映射（条目）插入到映射中。
- putAll() - 将指定映射中的所有条目插入到此映射中。
- putIfAbsent() - 如果映射中不存在指定的键，则将指定的键/值映射插入到map中。

#### 访问TreeMap元素
1. 访问集合
   - entrySet() - 返回TreeMap的所有键/值映射（条目）的集合。
   - keySet() - 返回TreeMap的所有键的集合。
   - values() - 返回TreeMap的所有值的集合。
2. 访问元素
   - get() - 返回与指定键关联的值。如果找不到键，则返回null。
   - getOrDefault() - 返回与指定键关联的值。如果找不到键，则返回指定的默认值。
#### 删除TeeMap元素
- remove(key) - 返回并从TreeMap中删除与指定键关联的条目。
- remove(key, value) -仅当指定键与指定值相关联时才从映射中删除条目，并返回布尔值。
#### 替换TreeMap元素
- replace(key, value)-用key新的替换指定映射的值value
- replace(key, old, new) -仅当旧值已与指定键关联时，才用新值替换旧值
- replaceAll(function) -用指定的结果替换map的每个值 function:numbers.replaceAll((key, oldValue) -> oldValue + 2);
#### 导航方法
1. 第一个和最后一个方法
   - firstKey() - 返回map的第一个键
   - firstEntry() - 返回映射的第一个键的键/值映射
   - lastKey() - 返回map的最后一个键
   - lastEntry() - 返回映射的最后一个键的键/值映射
2. 向上，向下，上下限方法
   - HigherKey()/HigherEntry() - 返回严格大于指定键的那些键中的最小的键/相关的条目 。
   - lowerKey()/lowerEntry() - 返回严格所有小于指定键的最大键/相关的条目。
   - ceilingKey()/ceilingEntry() - 返回大于等于指定键的那些键中的最小的键/相关的条目 。
   - floorKey()/floorEntry() - 返回小于等于指定键的那些键中最大的键/相关的条目。
#### 其他方法
- containsKey()，clone()，containsValue()，size()，clear()

### TreeSet

### HashMap