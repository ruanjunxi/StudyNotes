---

---

## MYSQL学习记录

### 一、 UNION

UNION 操作符，是将多个查询结果，按行纵向合并。

**基本语法：**

```mysql
SELECT  <字段名>  FROM  <表名>
UNION
SELECT  <字段名>  FROM  <表名>
```

#### 1.1 单表联合查询

UNION 合并结果集的时候，如果合并的结果集中有重复行，只会保留其中一行。合并结果集的时候，如果合并的结果集中有重复行，只会保留其中一行。如果要保留重复行，可以使用**UNION ALL**。当确定表中不会重复时，建议使用UNION ALL，这样执行效率更高，因为不需要去重操作。

#### 1.2 不同表联合查询

UNION 操作符上下两个结果集的列数必须相等，否则会报错。

#### 1.3 语句执行顺序

如果后面跟了ORDER BY、LIMIT子句，这些子句不是只作用于最后一个查询语句，而是会等UNION将结果集合并之后再执行。

### 二.、IF函数

基本语法：

```mysql
IF(条件表达式, 值1, 值2)
```

返回值可以是任何值，比如：数值、文本、日期、空值NULL、数学表达式、函数等。

#### 1. IF函数

**实例：**在Students表中，将1995年及之前出生的学生划分为1班，1995年以后出生的学生划分为2班，最后显示Sname,Sage,所在班级这3列。

```mysql
SELECT
  Sname,
  Sage,
  IF(
    YEAR(Sage) <=1995, "1班","2班"
  ) AS "所在班级"
FROM Students;
```

#### 2. IF函数嵌套

当分类大于两种时，可以将IF函数嵌套实现。

**实例：**在Students表中，学生编号Sid小于等于3的学生，属于1班；Sid为4到6的学生，属于2班；Sid大于等于7的学生属于3班。最后显示Sname,Sage,所在班级这3列。

```mysql
SELECT
  Sid,
  Sname,
  IF(Sid<=3,"1班",
    IF(Sid>=7,'3班','2班')
  ) AS "所在班级"
FROM Students
```

嵌套函数看起来还是有点麻烦的，如果分类大于两种，可以用CASE关键词代替IF函数嵌套。

#### 3. IF+聚合函数

**实例：**集合Teacher表和Students，计算班主任所带学生的人数，大于等于5的显示“5人及以上”，人数小于5的显示“5人以下”。

```mysql
SELECT
  t.Tname,
  COUNT(*) AS "学生人数",
  IF(COUNT(*)>=5,"5人及以上","5人以下") AS "分类"
FROM Teachers t
JOIN Students s
ON t.Tid = s.Tid
GROUP BY t.Tname;

```

### 三、CASE函数

当分类多于两种时，可以用我们上节课讲的IF函数嵌套，也可以用CASE函数，语句可读性会更好。

基本语法：

```mysql
CASE
    WHEN <条件表达式> THEN <值>
    WHEN <条件表达式> THEN <值>
    WHEN <条件表达式> THEN <值>
    ……
    [ELSE <值>] （可选子句）
END
```

**实例：**在Students表中，学生编号Sid小于等于3的学生，属于1班；Sid为4到6的学生，属于2班；Sid大于等于7的学生属于3班。最后显示Sname,Sage,所在班级这3列。

IF函数是这样实现的：

```mysql
SELECT
  Sid,
  Sname,
  IF(Sid<=3,"1班",
    IF(Sid>=7,'3班','2班')
  ) AS "所在班级"
FROM Students
```

用Case函数可以这样来写：

```mysql
SELECT
  Sid,
  Sname,
  CASE
    WHEN Sid<=3 THEN "1班"
    WHEN Sid BETWEEN 4 AND 6 THEN "2班"
    ELSE "3班"
  END AS "所在班级"
FROM Students
```

在select中使用count需要判断时，可以这样写：

```mysql
count(distinct case when year(act_day)=2021 then act_day else null end)as act_days_2021,
```



### 四、函数

#### 4.1 文本函数

```mysql
1. LOCATE(substr , str )：返回子串 substr 在字符串 str 中第一次出现的位置，如果字符substr在字符串str中不存在，则返回0；
2. POSITION(substr IN str )：返回子串 substr 在字符串 str 中第一次出现的位置，如果字符substr在字符串str中不存在，与LOCATE函数作用相同；
3. LEFT(str, length)：从左边开始截取str，length是截取的长度；
4. RIGHT(str, length)：从右边开始截取str，length是截取的长度；
5. SUBSTRING_INDEX(str ,substr ,n)：返回字符substr在str中第n次出现位置之前的字符串;
6. SUBSTRING(str ,n ,m)：返回字符串str从第n个字符截取到第m个字符(n从1开始)；
7. REPLACE(str, n, m)：将字符串str中的n字符替换成m字符；
8. LENGTH(str)：计算字符串str的长度。
9. CONCAT_WS:将多个字符串连接成一个字符串，但是可以一次性指定分隔符。concat_ws(separator, str1, str2, …)说明：第一个参数指定分隔符。需要注意的是分隔符不能为null，如果为null，则返回结果为null。
10. INSTR(str,substr):获取子串第一次出现的索引，如果没有找到，则返回0（从1开始）。可以用来判断str中是否与substr相等。
11. GROUP_CONCAT(DISTINCT expression 将组中的字符串连接成为具有各种选项的单个字符串。
    ORDER BY expression
    SEPARATOR sep);
```


详细介绍一下第五个，substring_index(str,delim,count)：str:要处理的字符串 ，delim:分隔符， count:计数

**实例**：str=www.wikibt.com

- substring_index(str,'.',1)，结果是：www

- substring_index(str,'.',2)，结果是：www.wikibt

- 如果count是正数，那么就是从左往右数，第N个分隔符的左边的所有内容；如果count是负数，那么就是从右往左数，第N个分隔符的右边的所有内容

-  substring_index(str,'.',-2)，结果为：wikibt.com。

- 如果我要中间的的wikibt怎么办？答：从右数第二个分隔符的右边全部，再从左数的第一个分隔符的左边：

  substring_index(substring_index(str,'.',-2),'.',1);

#### 4.2 窗口函数

[参考链接](https://www.zhihu.com/tardis/bd/art/92654574?source_id=1001)

在日常工作中，经常会遇到需要**在每组内排名**，比如下面的业务需求：

- 排名问题：每个部门按业绩来排名
- topN问题：找出每个部门排名前N的员工进行奖励
- 窗口函数是在group by以及having之后才执行的，窗口函数所操作的表实际上是group by和having之后的临时表。[例题](https://www.nowcoder.com/practice/ebff819fd38c46db8a42dfe43ca7b33a?tpId=240&tags=&title=&difficulty=0&judgeStatus=0&rp=0&sourceUrl=%2Fexam%2Foj%3Fpage%3D1%26tab%3DSQL%25E7%25AF%2587%26topicId%3D341)

常用窗口函数如下：

1. NTH_VALUE ():NTH_VALUE 返回 analytic_clause 定义的窗口中第 n 行的 measure_expr 值。返回的值具有 measure_expr 的数据类型。[例题](https://www.nowcoder.com/practice/b1e2864271c14b63b0df9fc08b559166?tpId=240&tags=&title=&difficulty=0&judgeStatus=0&rp=0&sourceUrl=%2Fexam%2Foj%3Fpage%3D1%26tab%3DSQL%25E7%25AF%2587%26topicId%3D341)

```mysql
NTH_VALUE (measure_expr, n) [ FROM { FIRST | LAST } ][ { RESPECT | IGNORE } NULLS ] OVER (analytic_clause)
```

2. rank()排序相同时会重复，总数不变，即会出现1、1、3这样的排序结果；[例题]([每类试卷得分前3名_牛客题霸_牛客网 (nowcoder.com)](https://www.nowcoder.com/practice/255aa1863fe14aa88694c09ebbc1dbca?tpId=240&tags=&title=&difficulty=0&judgeStatus=0&rp=0&sourceUrl=%2Fexam%2Foj%3Fpage%3D1%26tab%3DSQL%E7%AF%87%26topicId%3D341))
3. dense_rank()排序相同时会重复，总数会减少，即会出现1、1、2这样的排序结果；
4. row_number()排序相同时不会重复，会根据顺序排序。
5. PERCENT_RANk()返回排名百分比。

```mysql
PERCENT_RANK()
    OVER (
        PARTITION BY expr,...
        ORDER BY expr [ASC|DESC],...
    ) 
```

	6. CAST函数用于将某种数据类型的表达式显式转换为另一种数据类型。CAST()函数的参数是一个表达式，它包括用AS关键字分隔的源值和目标数据类型。用途：MySQL处理两个整数（INT）相减的时候，如果其中有一个是UNSIGNED INT类型的，那么结果就被当做是UNSIGNED的，因此如果结果为负数会报错，可以使用CAST函数转换数据类型。语法：CAST (expression AS data_type)，cast(exam_cnt_rank_21 as signed)
	6. min(),max()；窗口函数分组求最大值。[例题]([对试卷得分做min-max归一化_牛客题霸_牛客网 (nowcoder.com)](https://www.nowcoder.com/practice/2b7acdc7d1b9435bac377c1dcb3085d6?tpId=240&tags=&title=&difficulty=0&judgeStatus=0&rp=0&sourceUrl=%2Fexam%2Foj%3Fpage%3D1%26tab%3DSQL%E7%AF%87%26topicId%3D341))

```sql
max(score) over(partition by exam_id) as maxscore, 
min(score) over(partition by exam_id) minscore
```

8. count() 窗口函数用于求某个分组数。聚合窗口函数中，over()的括号中有order by 时，即为计算到当前时间为止的累计数量.[例题]([每份试卷每月作答数和截止当月的作答总数。_牛客题霸_牛客网 (nowcoder.com)](https://www.nowcoder.com/practice/5f1cbe74c682485aa73e4c2b30f04a62?tpId=240&tags=&title=&difficulty=0&judgeStatus=0&rp=0&sourceUrl=%2Fexam%2Foj%3Fpage%3D1%26tab%3DSQL%E7%AF%87%26topicId%3D341))

```sql
count(start_time) over(partition by exam_id,DATE_FORMAT(start_time,'%Y%m')) as month_cnt,
count(start_time) over(partition by exam_id order by DATE_FORMAT(start_time,'%Y%m')) as cum_exam_cnt
```

9. 窗口函数的窗口选项（frame_clause）,只统计窗口内的数据，如需要统计前一周的数据，相当于只统计当前行以及前6行的数据。
    ```sql
    { ROWS | RANGE } frame_start
    { ROWS | RANGE } BETWEEN frame_start AND frame_end
    --例子
    -- dt升序排序，向前6行
    partition by tag order by dt rows 6 preceding 
    partition by tag order by dt rows between 6 preceding and CURRENT row
    -- dt降序排列，当前行往后6行
    partition by tag order by dt desc rows between  CURRENT row and 6 following
    ```
    
#### 4.3 日期函数
  1. 时间差函数 timestampdiff():
      ```sql
        语法：timestampdiff(interval, datetime1,datetime2)
        结果：返回（时间2-时间1）的时间差，结果单位由interval参数给出.
        second 秒,minute 分钟,day 天,year 年,quarter 季度
        SELECT TIMESTAMPDIFF(SECOND,'2012-10-01','2013-01-13'); # 8985600
        SELECT TIMESTAMPDIFF(MINUTE,'2012-10-01','2013-01-13'); # 149760
        SELECT TIMESTAMPDIFF(HOUR,'2012-10-01','2013-01-13'); # 2496
        SELECT TIMESTAMPDIFF(DAY,'2012-10-01','2013-01-13'); # 104
        SELECT TIMESTAMPDIFF(WEEK,'2012-10-01','2013-01-13'); # 14
        SELECT TIMESTAMPDIFF(MONTH,'2012-10-01','2013-01-13'); # 3
        SELECT TIMESTAMPDIFF(QUARTER,'2012-10-01','2013-01-13'); # 1
        SELECT TIMESTAMPDIFF(YEAR,'2012-10-01','2013-01-13'); # 0
      ```
  2. 时间差函数：datediff():
     ```sql
      语法：传入两个日期参数，比较DAY天数，第一个参数减去第二个参数的天数值。
      SELECT DATEDIFF('2013-01-13','2012-10-01'); # 104
     ```
  3. 时间差函数：timediff():
      ```sql
      语法：timediff(time1,time2)
      结果：返回两个时间相减得到的差值，time1-time2
      SELECT TIMEDIFF('2018-05-21 14:51:43','2018-05-19 12:54:43');# 49:57:00
      ```
  4. 其他日期函数：
      ```sql
        # 其他日期函数
        SELECT NOW(); # 2018-05-21 14:41:00
        SELECT CURDATE(); # 2018-05-21
        SELECT CURTIME(); # 14:41:38
        SELECT DATE(NOW()); # 2018-05-21
        SELECT SYSDATE(); # 2018-05-21 14:47:11
        SELECT CURRENT_TIME(); # 14:51:30
        SELECT CURRENT_TIMESTAMP; # 2018-05-21 14:51:37
        SELECT CURRENT_TIMESTAMP(); # 2018-05-21 14:51:43
      ```
### 五、HAVING字句

HAVING 也是条件筛选语句，放在GROUP BY的后面。

**基本语法：**

```mysql
SELECT  <字段名> 
FROM  <表名>  
GROUP BY <字段名>
HAVING  <筛选条件>;
```

#### 5.1 HAVING与WHERE的区别

- WHERE是在GROUP BY分组之前进行条件筛选，后面不可以跟聚合函数。

- HAVING是在GROUP BY分组之后进行条件筛选，后面可以直接跟聚合函数。

**实例：**在Students表中，找出学生编号Sid小于8的记录，并查找每个班主任带的男女学生数量，最后输出数量大于2的记录。

```mysql
SELECT Tid,Ssex,COUNT(*)
FROM Students
GROUP BY Tid,Ssex
HAVING Sid<8 AND COUNT(*) > 2
```

### 六、SQL执行顺序

#### 6.1 书写顺序

```mysql
SELECT <字段名> 
FROM <表名>
JOIN <表名> 
ON <连接条件>
WHERE <筛选条件>
GROUP BY <字段名>
HAVING <筛选条件>
UNION
ORDER BY <字段名>
LIMIT <限制行数>;
```

#### 6.2 执行顺序

1. **FORM**：选择from后面跟的表，产生虚拟表1。
2. **ON**：ON是JOIN的连接条件，符合连接条件的行会被记录在虚拟表2中。
3. **JOIN**：如果指定了LEFT JOIN，那么保留表中未匹配的行就会作为外部行添加到虚拟表2中，产生虚拟表3。如果有多个JOIN链接，会重复执行步骤1~3，直到处理完所有表。
4. **WHERE**：对虚拟表3进行WHERE条件过滤，符合条件的记录会被插入到虚拟表4中。
5. **GROUP BY**：根据GROUP BY子句中的列，对虚拟表2中的记录进行分组操作，产生虚拟表5。
6. **HAVING**：对虚拟表5进行HAVING过滤，符合条件的记录会被插入到虚拟表6中。
7. **SELECT**：SELECT到一步才执行，选择指定的列，插入到虚拟表7中。
8. **UNION**：UNION连接的两个SELECT查询语句，会重复执行步骤1~7，产生两个虚拟表7，UNION会将这些记录合并到虚拟表8中。
9. **ORDER BY**: 将虚拟表8中的记录进行排序，虚拟表9。
10. **LIMIT**：取出指定行的记录，返回结果集。

```tex
注：
SELECT语句总是写在最前面，但在大部分语句之后才执行。所以在SQL语句中，我们不能在WHERE、GROUP BY、 HAVING语句中使用在 SELECT 中设定的别名。但是MYSQL有个特性，在GROUP BY、 HAVING语句中，可以使用 SELECT 中设定的别名。这不是因为MYSQL中会提前执行SELECT，而是因为在GROUP BY这一步返回了游标。
```

### 七、子查询和相关子查询

#### 7.1 子查询

可以分为三类：

- **子查询+WHERE子句**: 以子查询结果作为条件

  - ```mysql
    SELECT *
    FROM Students
    WHERE Sage > (
      SELECT Sage
      FROM Students
      WHERE Sname = "宁一"
    )
    ```

- **子查询 + SELECT 语句**：子查询作为新的列

  - ```mysql
    SELECT
      Sname,
      Sage,
      (SELECT Max(Sage) FROM Students) AS Maxage
    FROM Students
    ```

- **子查询 + FROM语句**：子查询作为查询的表

  - ```mysql
    SELECT Sid,status
    FROM (
      SELECT
        Sid,
        'student' AS status
      FROM Students
      WHERE Sid <= 5
    ) AS s -- 必须加别名
    WHERE Sid > 2
    ```

#### 7.2 相关子查询

在相关子查询中，子查询是在主查询每一条记录层面上依次进行的，子查询依赖主查询。因此相关子查询比非关联查询执行起来慢一些。

**实例：**选出Scores成绩表中，学生编号在1~6之间的记录，找出那些超过相同科目平均成绩的学生成绩。

```mysql
SELECT *
FROM Scores s  -- 将主查询的字段加上别名
WHERE score > (
    SELECT AVG(score)
    FROM Scores
    WHERE Cid = s.Cid  -- 获取当前主查询的的课程编号Cid
)
AND Sid BETWEEN 1 AND 6;
```

### 八、连接和外连接

1. 内连接：只返回两个表中连接字段相等的行。inner join(等值连接) 只返回两个表中联结字段相等的数据
2. 左(外)连接：返回包括左表中的所有记录和右表中连接字段相等的记录。
3. 右(外)连接：返回包括右表中的所有记录和左表中连接字段相等的记录。
4. 全外连接：返回包括右表中的所有记录和左表中连接字段相等的记录。

​	<img src="./ref\连接.png" alt="连接" style="zoom:100%;" />



### 九、工作遇到的一些问题

#### 9.1 WITH ROLLUP

​	当你需要新增一行，用于统计某些字段的总和时，可以使用with rollup来替换union all。

   使用with rollup，可以在group by的时候额外返回分组信息，避免多次查询统计，如：

```mysql
select ifnull(e.banktype,'合计') as yh,
  		 sum(e.GLZH) as glzh,
  		 sum(e.HZZH) as hzzh,
  		 concat(round(sum(e.GLZB),2),'%') as glzb,
  		 concat(round((1-sum(e.HZZH)/sum(e.GLZH))*100,2),'%') as jtbl
	  from 
	  (select (select note from T_ACCOUNT_CONFIG where id=b.bank_type) banktype,
			 a.CURR_FUND_BALANCE/100000000 GLZH,
			 (a.CURR_FUND_BALANCE-ifnull(c.Provisions_rate,0))/100000000 HZZH,
			 a.CURR_FUND_BALANCE/v_ZB*100 GLZB,
			 1-(a.CURR_FUND_BALANCE-ifnull(c.Provisions_rate,0))/a.CURR_FUND_BALANCE JTBL
	  from 
	  (select a.ACCT_CODE ,sum(a.CURR_FUND_BALANCE) CURR_FUND_BALANCE
	  from txykmye_temp a
	  where a.SETTLE_DATE =I_DAY and instr(a.ACCT_CODE ,v_zjdm)>0 
	  group by a.ACCT_CODE) a, T_YXZHDM b left join (select mon,bank,Provisions_rate 
													from t_get_set_provisions t 
													where t.mon=substr(I_DAY,1,6) and t.zjxz=I_ZJXZ) c 
												   on c.bank=b.bank_type
	  where a.ACCT_CODE =b.GLZHDM
	  group by banktype 
	  order by a.CURR_FUND_BALANCE desc
	  ) e 
 	group by e.banktype  with rollup
```

   with rollup不支持使用order by语句，因此如果需要排序，则在外面套一层select语句，再使用order by。

#### 9.2 查找某个表被哪些过程所使用

```mysql
SELECT * FROM mysql.proc 
WHERE type = 'PROCEDURE' 
AND Body LIKE '%entity_zhzlyjyscdyb%';
```
### 面试题

1. [MySQL 的 delete、truncate、drop 有什么区别?](https://zhuanlan.zhihu.com/p/270331768)

​	可以这么理解，一本书，delete是把目录撕了，truncate是把书的内容撕下来烧了，drop是把书烧了。
