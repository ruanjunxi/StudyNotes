## Mybatis

### 1 Mybatis概述

#### 1.1 Mybtatis是什么

- MyBatis框架是一个开源的数据持久层框架。
- 它的内部封装了通过JDBC访问数据库的操作，支持普通的SQL查询、存储过程和高级映射，几乎消除了所有的JDBC代码和参数的手工设置以及结果集的检索。
- MyBatis作为持久层框架，其主要思想是将程序中的大量SQL语句剥离出来，配置在配置文件当中，实现SQL的灵活配置。
- 这样做的好处是将SQL与程序代码分离，可以在不修改代码的情况下，直接在配置文件当中修改SQL

#### 1.2 什么是ORM

ORM（Object/Relational Mapping）即对象关系映射，是一种数据持久化技术。它在对象模型和关系型数据库直接建立起对应关系，并且提供一种机制，通过JavaBean对象去操作数据库表的数据。 MyBatis通过简单的XML或者注解的方式进行配置和原始映射，将实体类和SQL语句之间建立映射关系，是一种半自动（之所以说是半自动，因为我们要自己写SQL）的ORM实现

#### 1.3 Mybatis的优点

- 将SQL语句写在XML文件里，与java程序分离，降低耦合。
- 避免了编写JDBC冗余的代码，也不需要手动开关连接。
- 提供映射标签，支持对象与数据库的ORM字段关系映射；提供对象关系映射标签，支持对象关系组件维护。

### 2 MyBatis的解析和运行原理

#### 2.1 请说说MyBatis的工作原理

[【精选】MyBatis面试题（2022版）-CSDN博客](https://blog.csdn.net/qq_43061290/article/details/124468306)

#### 2.2 为什么需要预编译

**定义：**SQL 预编译指的是数据库驱动在发送 SQL 语句和参数给 DBMS 之前对 SQL 语句进行编译，这样 DBMS 执行 SQL 时，就不需要重新编译。

**为什么需要预编译：**预编译后的sql语句多数情况下可以直接执行。预编译阶段可以将多次操作合并为一个操作。同时，预编译好的语句可以把他缓存下来，对于同一个sql语句，可以直接使用预编译好的对象。Mybatis默认情况下，将对所有的 SQL 进行预编译。

#### 2.3 Mybatis都有哪些Executor执行器？它们之间的区别是什么？

**Mybatis有三种基本的Executor执行器，SimpleExecutor、ReuseExecutor、BatchExecutor。**

- **SimpleExecutor**：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。
- **ReuseExecutor**：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map<String, Statement>内，供下一次使用。简言之，就是重复使用Statement对象。
- **BatchExecutor：**执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。

#### 2.4 Mybatis的延迟加载



### 3 映射器

#### 3.1  #{}和${}的区别

1. `#{}` 是预编译处理，`${}`是字符串替换。
2. Mybatis在处理`#{}`时，会将sql中的`#{}`替换为?号，调用PreparedStatement的set方法来赋值；
3. Mybatis在处理`${}`时，就是把`${}`替换成变量的值，从而可能导致sql注入问题。
4. 使用`#{}`可以有效的防止SQL注入，提高系统安全性。

#### 3.2 模糊查询like语句该怎么写

- **'%${question}%'** 可能引起SQL注入，不推荐

- **"%"#{question}"%"**,注意：因为#{…}解析成sql语句时候，会在变量外侧自动加单引号’ '，所以这里 % 需要使用双引号" "，不能使用单引号 ’ '，不然会查不到任何结果。

- CONCAT(’%’,#{question},’%’) 使用CONCAT()函数，推荐

- 使用bind标签

  ```xml
  <select id="listUserLikeUsername" resultType="com.jourwon.pojo.User">
  　　<bind name="pattern" value="'%' + username + '%'" />
  　　select id,sex,age,username,password from person where username LIKE #{pattern}
  </select>
  ```

#### 3.3 在mapper中如何传递多个参数

- **顺序传参**

```xml
public User selectUser(String name, int deptId);
<select id="selectUser" resultMap="UserResultMap">
    select * from user
    where user_name = #{0} and dept_id = #{1}
</select>
```

- **@Param注解传参法**

```xml
public User selectUser(@Param("userName") String name, int @Param("deptId") deptId);
<select id="selectUser" resultMap="UserResultMap">
    select * from user
    where user_name = #{userName} and dept_id = #{deptId}
</select> 
```

- **Map传参法**

```xml
public User selectUser(Map<String, Object> params);
<select id="selectUser" parameterType="java.util.Map" resultMap="UserResultMap">
    select * from user
    where user_name = #{userName} and dept_id = #{deptId}
</select>
```

参数名称就是map的key

- **Java Bean传参法**

```xml
public User selectUser(User user);
<select id="selectUser" parameterType="com.jourwon.pojo.User" resultMap="UserResultMap">
    select * from user
    where user_name = #{userName} and dept_id = #{deptId}
</select>
```

#### 3.4 MyBatis的接口绑定

就是将接口与sql语句绑定，我们只需要调用接口即可。

**接口绑定有两种实现方式**

- 通过注解绑定，就是在接口的方法上面加上 @Select、@Update等注解，里面包含Sql语句来绑定；
- 通过xml里面写SQL来绑定， 在这种情况下，要指定xml映射文件里面的namespace必须为接口的全路径名。当Sql语句比较简单时候，用注解绑定， 当SQL语句比较复杂时候，用xml绑定，一般用xml绑定的比较多。

#### 3.5 使用MyBatis的mapper接口调用时有哪些要求？
- Mapper接口方法名和mapper.xml中定义的每个sql的id相同。

- Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同。

- Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同。

- Mapper.xml文件中的namespace即是mapper接口的类路径。

#### 3.6 最佳实践中，通常一个Xml映射文件，都会写一个Dao接口与之对应，请问，这个Dao接口的工作原理是什么？Dao接口里的方法，参数不同时，方法能重载吗

Dao接口，就是人们常说的Mapper接口，接口的全限名，就是映射文件中的namespace的值；接口的方法名，就是映射文件中MappedStatement的id值；接口方法内的参数，就是传递给sql的参数。Mapper接口是没有实现类的，当调用接口方法时，接口全限名+方法名拼接字符串作为key值（也就是namespace+ID），可唯一定位一个MappedStatement。

**Dao接口里的方法，是不能重载的，因为是全限名+方法名的保存和寻找策略。**

**Dao接口的工作原理**是JDK动态代理，Mybatis运行时会使用JDK动态代理为Dao接口生成代理proxy对象，代理对象proxy会拦截接口方法，转而执行MappedStatement所代表的sql，然后将sql执行结果返回。

#### 3.7 Mybatis的Xml映射文件中，不同的Xml映射文件，id是否可以重复？

只要配置了namespace，id就可以重复。因为是使用**namespace+id**作为Map<String, MappedStatement>的key。

#### 3.8 Mybatis是如何将sql执行结果封装为目标对象并返回的？都有哪些映射形式？

- 使用标签，`<resultMap>` 来映射数据库列名和实体类的属性；
- 使用sql列的别名功能，将列别名书写为对象属性名，比如T_NAME AS NAME，对象属性名一般是name，小写，但是列名不区分大小写，Mybatis会忽略列名大小写，智能找到与之对应对象属性名，你甚至可以写成T_NAME AS NaMe，Mybatis一样可以正常工作。

有了列名与属性名的映射关系后，Mybatis通过反射创建对象，同时使用反射给对象的属性逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的。

### 4 其他

#### 4.1 Mybatis是如何进行分页的？分页插件的原理是什么？

使用**PageHelper**来实现，低层还是使用limit进行物理分页。

```java
    @GetMapping("/page")
    public Object page() {
        //查询第三页，每页三条
        PageHelper.startPage(3 , 3);
        List<Temperature> temperatures = temperatureDao.selectByExample(null);
        //得到分页的结果对象
        PageInfo<Temperature> resPage = new PageInfo<>(temperatures);
        return resPage;
    }
```

底层实现：

```java
@Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            //省略内容，省略内容，省略内容
            List resultList;
            //步骤1：调用方法判断是否需要进行分页，如果不需要，直接返回结果
            if (!dialect.skip(ms, parameter, rowBounds)) {
                //判断是否需要进行 count 查询
                if (dialect.beforeCount(ms, parameter, rowBounds)) {
                    //步骤2：查询总条数
                    Long count = count(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                    //处理查询总数，返回 true 时继续分页查询，false 时直接返回
                    //步骤3：保存总条数
                    if (!dialect.afterCount(count, parameter, rowBounds)) {
                        //当查询总数为 0 时，直接返回空的结果
                        return dialect.afterPage(new ArrayList(), parameter, rowBounds);
                    }
                }
               //步骤4：执行分页查询
                resultList = ExecutorUtil.pageQuery(dialect, executor,
                        ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
            } else {
                //rowBounds用参数值，不使用分页插件处理时，仍然支持默认的内存分页
                resultList = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            }
            //步骤5：封装结果
            return dialect.afterPage(resultList, parameter, rowBounds);
        } finally {
            if(dialect != null){
                dialect.afterAll();
            }
        }
    }

```

**分页过程总结**
首先会把分页参数封装成Page对象放到ThreadLocal中，然后根据SQL进行拼接转换(select * from table where a) ->(select count(“0”)from table where a)和(select * from table where a limit ,)，有了total总条数、pageNum当前第几页、pageSize每页大小和当前页的数据，就可以算出分页的其他非必要信息（是否为首页，是否为尾页，总页数）

#### 4.1 Mybatis的一级、二级缓存

mybatis通过设置一级和二级缓存来提高查询的效率。注：一级缓存的作用范围是单个SqlSession，二级缓存是跨多个SqlSession的。

- 一级缓存是Mybatis默认开启的缓存机制，在同一个SqlSession中，如果执行相同的查询语句，Mybatis会将查询的结果缓存起来，后续如果有相同的查询语句命中缓存，那么会直接从缓存中返回查询结果集。一级缓存的作用域是单个SqlSession，也就是说如果SqlSession关闭，那么也就会清空缓存。
- 二级缓存是跨 SqlSession 的缓存机制，可以被多个 SqlSession 共享。当一个 SqlSession 执行查询之后，查询结果会被存储在二级缓存中，在后续的查询中，如果其他 SqlSession 需要相同的数据，MyBatis会先检查二级缓存，如果找到对应的数据，则直接返回，避免了对数据库的重复查询。二级缓存的作用范围是 Mapper 级别的，即同一个 Mapper 接口的多个 SqlSession 共享一个二级缓存。
- 需要注意的是：二级缓存在分布式场景下可能会出现数据不一致的问题，从而出现脏读。因此，在使用二级缓存时，需要确保缓存的数据是稳定的、不经常变动的，并且在更新数据时要及时清空相应的缓存。