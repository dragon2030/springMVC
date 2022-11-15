package com.bigDragon.javase.java8;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Stream API
 *
 * 一、Stream API概念：
 *     Stream是Java8中处理集合的关键抽象概念，它可以指定你希望对集合进行的操作，可以执行非常复杂的查找、
 *  过滤和映射数据等操作。使用Stream API对集合数据进行操作，就类似于使用SQL执行的数据库查询。也可以使用
 *  Stream API来并行执行操作。简言之，Stream API提供了一种高效且易于使用的处理数据的方式。
 *
 * 二、Stream和Collection集合的区别：
 *      Collection是一种静态的内存数据结构，而Stream是有关计算的。前者是主要面向内存，存储在内存中，
 *  后者主要是面向CPU，通过CPU实现计算。
 *
 *  注意:
 *      >Stream自己不会存储元素
 *      >Stream不会改变源对象。相反，他们会返回一个持有结果的新的Stream
 *      >Stream操作是延迟执行的。这意味着他们会等到需要结果的时候才执行
 *  三、Stream的操作三个步骤
 *      1.创建Stream：一个数据源（如：集合、数组），获取一个流
 *      2.中间操作：一个中间操作链，对数据源的数据进行处理
 *      3.终止操作(终端操作)：一旦执行终止操作，就执行中间操作链，并产生结果。之后，不会再使用。
 *  四、创建Stream方式
 *      >创建Stream方式一：通过集合
 *      >创建Stream方式二：通过数组
 *      >创建Stream方式三：通过Stream的of()
 *  五、Stream的中间操作
 *      概念：多个中间操作可以连接起来形成一个流水线，除非流水线上出出现终止操作，否则中间操作捕获执行任何的
 *      处理！而在终止操作时一次性全部处理，称为“惰性求值”。
 *      >筛选与切片
 *          Stream<T> filter(Predicate<? super T> predicate)——接收lambda，从流中排除某些元素
 *          Stream<T> limit(long maxSize)——截断流，使其元素不超过给定数量
 *          Stream<T> skip(long n)——跳过元素，返回一个扔掉了前n个元素的流。若流中数据不足n个，
 *          Stream<T> distinct()——筛选，通过流所生成元素的hasCode()和equals()去除重复元素
 *      >映射
 *          <R> Stream<R> map(Function<? super T, ? extends R> mapper)——接收一个参数作为参数，将元素转换成其他形式或提取信息，该函数会被应用到每个元素并被映射成一个新的元素
 *          <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
 *      >排序
 *          sorted()——自然排序
 *          sorted(Comparator com)——定制排序
 *  六、Stream的终止操作
 *      终端操作会从流的流水线生成结果。其结果可以是任何不是流的值，例如：List、Integer，甚至是void
 *      流进行了终止操作后，不能再次使用
 *          >匹配与查找
 *              boolean allMatch(Predicate<? super T> predicate)——检验是否匹配所有元素
 *              boolean anyMatch(Predicate<? super T> predicate)——检查是否至少匹配一个元素
 *              boolean noneMatch(Predicate<? super T> predicate)——检查是否没有匹配的元素
 *              Optional<T> findFirst()——返回当前第一个元素
 *              Optional<T> findAny()——返回当前流中的任意元素
 *              long count()——返回流中元素的总个数
 *              Optional<T> max(Comparator<? super T> comparator)——返回流中最大值
 *              Optional<T> min(Comparator<? super T> comparator)——返回流中最小值
 *              void forEach(Consumer<? super T> action)——内部迭代
 *          >归约
 *              T reduce(T identity, BinaryOperator<T> accumulator)——可以将流中元素反复结合起来，得到一个值。
 *              Optional<T> reduce(BinaryOperator<T> accumulator)——可以将流中元素反复结合起来，得到一个值，返回Optional<T>
 *          >收集
 *              <R, A> R collect(Collector<? super T, A, R> collector)——将流转换为其他形式。接收一个Collector接口的实现，
 *
 *
 * @author bigDragon
 * @create 2020-12-17 20:32
 */
public class StreamApiTest {
    public static void main(String[] args) {
        StreamApiTest streamApiTest = new StreamApiTest();
        //创建Stream方式
        //创建Stream方式一：通过集合
        streamApiTest.test1();
        //创建Stream方式二：通过数组
        streamApiTest.test2();
        //创建Stream方式三：通过Stream的of()
        streamApiTest.test3();

        //Stream的中间操作
        //筛选与切片
        streamApiTest.test5();
        //映射
        streamApiTest.test6();
        //排序
        streamApiTest.test7();

        //Stream的终止操作
        //匹配查找
        streamApiTest.test8();
        //归约
        streamApiTest.test9();
        //收集
        streamApiTest.test10();

        //测试用ArrayList集合
        streamApiTest.getList();
    }
    /*
    创建Stream方式一：通过集合
     */
    @Test
    public void test1(){
        List<Person> list = getList();
        //default Stream<E> stream():返回一个顺序流
        Stream<Person> stream = list.stream();
        //default Stream<E> parallelStream():返回一个并行流（多线程同时去集合中数据）
        Stream<Person> parallelStream = list.parallelStream();
    }
    /*
    测试用ArrayList集合
     */
    public List<Person> getList(){
        List<Person> arrayList = new ArrayList<>();
        arrayList.add(new Person(1,"Mike",22));
        arrayList.add(new Person(2,"Sam",24));
        arrayList.add(new Person(3,"John",31));
        arrayList.add(new Person(4,"Bob",28));
        arrayList.add(new Person(5,"Zom",40));
        arrayList.add(new Person(6,"Jack",34));
        System.out.println("创建ArrayList<Person>完成"+arrayList);
        return arrayList;
    }
    /*
    创建Stream方式二：通过数组
     */
    @Test
    public void test2(){
        int[] arr =new  int[]{1,2,3,4};
        IntStream stream = Arrays.stream(arr);
        //调用Arrays类的static <T> Stream<T> stream(T[] array):返回一个流
        Person person = new Person("Mike", 22);
        Person person2 = new Person("Sam", 33);
        Person[] personArray = new Person[]{person,person2};
        Stream<Person> stream1 = Arrays.stream(personArray);
    }
    /*
    创建Stream方式三：通过Stream的of()
     */
    @Test
    public void test3(){
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5);
    }
    /*
    创建Stream方式四：创建无限流
     */
    @Test
    public void test4(){
        //迭代
        //public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f)
        Stream.iterate(0, t -> t+2 ).limit(10).forEach(System.out::println);
        //生成
        //public static<T> Stream<T> generate(Supplier<T> s)
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }
    /*
    *   Stream的中间操作
    *   1.筛选与切片
    *          Stream<T> filter(Predicate<? super T> predicate)——接收lambda，从流中排除某些元素
    *          Stream<T> limit(long maxSize)——截断流，使其元素不超过给定数量
    *          Stream<T> skip(long n)——跳过元素，返回一个扔掉了前n个元素的流。若流中数据不足n个，
    *          Stream<T> distinct()——筛选，通过流所生成元素的hasCode()和equals()去除重复元素
     */
    @Test
    public void test5(){
        LocalDateTime now = LocalDateTime.now();
        List<Person> list = getList();
        Stream<Person> stream = list.parallelStream().sorted(Comparator.comparing(Person::getAge));
        //Stream<T> filter(Predicate<? super T> predicate)——接收lambda，从流中排除某些元素
        stream.filter(p -> p.getAge() > 25).forEach(System.out::println);
        System.out.println("*******************************");
        //Stream<T> limit(long maxSize)——截断流，使其元素不超过给定数量
        list.stream().limit(2).forEach(System.out::println);
        System.out.println("*******************************");
        //Stream<T> skip(long n)——跳过元素，返回一个扔掉了前n个元素的流。若流中数据不足n个，
        // 则返回一个空流，与(long maxSize)互补
        list.stream().skip(2).forEach(System.out::println);
        System.out.println("*******************************");
        //Stream<T> distinct()——筛选，通过流所生成元素的hasCode()和equals()去除重复元素
        list.stream().distinct().forEach(System.out::println);
    }
    /*
    * Stream的中间操作
    * 2.映射
    *       <R> Stream<R> map(Function<? super T, ? extends R> mapper)——接收一个参数作为参数，将元素转换成其他形式或提取信息，该函数会被应用到每个元素并被映射成一个新的元素
    *       <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
     */
    @Test
    public void test6(){
        List<String> list = Arrays.asList("aa", "bb", "cc", "dd");
        /*<R> Stream<R> map(Function<? super T, ? extends R> mapper)——接收一个参数作为参数，
        将元素转换成其他形式或提取信息，该函数会被应用到每个元素并被映射成一个新的元素
         */
        list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
        System.out.println("*******************************");
        //练习：获取名字长度大于3的员工姓名
        List<Person> personList = getList();
        personList.stream().forEach(System.out::println);
        personList.stream().map(Person::getName).forEach(System.out::println);
        //personList.stream().map(Person::getName).filter(name -> name.length()>3).forEach(System.out::println);
        System.out.println("*******************************");
        // <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
        // 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有的流连接成一个流
        Stream<Character> characterStream = list.stream().flatMap(StreamApiTest::fromStringToStream);
        characterStream.forEach(System.out::println);
        System.out.println("*******************************");
        //练习2：对比map和flatMap的差异
        Stream<Stream<Character>> streamStream = list.stream().map(StreamApiTest::fromStringToStream);
        streamStream.forEach(s -> {
            s.forEach(System.out::println);
        });
    }
    /*
    Stream的中间操作
    3.排序
     */
    @Test
    public void test7(){
        //sorted()——自然排序
        List<String> list = Arrays.asList("12", "34", "76", "-7","0");
        list.stream().sorted().forEach(System.out::println);
        //倒序
        list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
        //sorted(Comparator com)——定制排序
        List<Person> getList = getList();
        getList.stream().sorted((e1,e2) -> Integer.compare(e1.getAge(),e2.getAge())).forEach(System.out::println);
        System.out.println("*******");
        getList.stream().sorted(Comparator.comparingInt(Person::getAge)).forEach(System.out::println);
        System.out.println("*******");
        //倒序
        getList.stream().sorted((e1,e2) -> -Integer.compare(e1.getAge(),e2.getAge())).forEach(System.out::println);
        System.out.println("*******");
        getList.stream().sorted(Comparator.comparingInt(Person::getAge).reversed()).forEach(System.out::println);

    }

    /*
    Stream的终止操作
    1.匹配查找
     */
    @Test
    public void test8(){
        List<Person> list = getList();
        //boolean allMatch(Predicate<? super T> predicate)——检验是否匹配所有元素
        //练习：是否所有的员工年龄都大于30
        boolean allMatch = list.stream().allMatch(person -> person.getAge() > 30);
        System.out.println(allMatch);
        //boolean anyMatch(Predicate<? super T> predicate)——检查是否至少匹配一个元素
        //练习：是否存在员工年龄都大于30
        boolean anyMatch = list.stream().anyMatch(person -> person.getAge() > 30);
        System.out.println(anyMatch);
        //boolean noneMatch(Predicate<? super T> predicate)——检查是否没有匹配的元素
        //练习：是否存在员工姓"雷"
        boolean noneMatch = list.stream().noneMatch(person -> person.getName().startsWith("雷"));
        System.out.println(noneMatch);
        //Optional<T> findFirst()——返回当前第一个元素
        //练习：按照年龄排序后取最小值
        Optional<Person> first = list.stream().sorted((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge())).findFirst();
        System.out.println(first);
        //Optional<T> findAny()——返回当前流中的任意元素
        Optional<Person> any = list.parallelStream().findAny();
        System.out.println(any);
        //long count()——返回流中元素的总个数
        //练习：返回年龄大于30的元素个数
        long count = list.stream().filter(person -> person.getAge() > 30).count();
        System.out.println(count);
        //Optional<T> max(Comparator<? super T> comparator)——返回流中最大值
        //练习：返回年龄年龄的员工年龄/员工信息
        Optional<Integer> max = list.stream().map(person -> person.getAge()).max(Integer::compareTo);//方法一
        System.out.println(max);
        System.out.println(max.get());//输出值
        Optional<Person> max1 = list.stream().max((person1, person2) -> Integer.compare(person1.getAge(), person2.getAge()));//方法二
        System.out.println(max1);
        //Optional<T> min(Comparator<? super T> comparator)——返回流中最小值
        //练习返回最低年龄的员工信息
        Optional<Person> min = list.stream().min((person1, person2) -> Integer.compare(person1.getAge(), person2.getAge()));//方法二
        System.out.println(min);
        System.out.println("**********************************");
        //void forEach(Consumer<? super T> action)——内部迭代
        //说明使用Collect接口需要去做迭代，称为外部迭代。相反，Stream API使用内部迭代——它帮你吧迭代做了
        list.stream().forEach(System.out::println);
        System.out.println("**********************************");
        //使用集合的遍历——外部迭代
        list.forEach(System.out::println);
    }
    /*
    Stream的终止操作
    2.归约
     */
    @Test
    public void test9(){
        //T reduce(T identity, BinaryOperator<T> accumulator)——可以将流中元素反复结合起来，得到一个值。
        //联系：计算1-10的自然数的和
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer reduce = list.stream().reduce(0, Integer::sum);
        System.out.println(reduce);
        //Optional<T> reduce(BinaryOperator<T> accumulator)——可以将流中元素反复结合起来，得到一个值，返回Optional<T>
        //练习：计算公司所有员工年龄总和
        Optional<Integer> reduce1 = getList().stream().map(person -> person.getAge()).reduce(Integer::sum);
        System.out.println(reduce1);
        Optional<Integer> reduce2 = getList().stream().map(person -> person.getAge()).reduce((i1,i2) -> i1+i2);
        System.out.println(reduce2);
    }

    //归约测试，BigDecimal
    @Test
    public void case_20221012(){
        ArrayList<BigDecimal> bigDecimals = new ArrayList<>();
        bigDecimals.add(new BigDecimal("1"));
        bigDecimals.add(new BigDecimal("2"));
        bigDecimals.add(new BigDecimal("3"));
        BigDecimal bigDecimal = bigDecimals.stream().reduce(BigDecimal.ZERO,(i1, i2) -> i1.add(i2));
        System.out.println(bigDecimal);
        Optional<BigDecimal> reduce = bigDecimals.stream().reduce((i1, i2) -> i1.add(i2));
        System.out.println(reduce);
        BigDecimal bigDecimal1 = reduce.get();
        System.out.println(bigDecimal1);
    }

    /*
    Stream的终止操作
    3.收集
     */
    @Test
    public void test10(){
        List<Person> list = getList();
        //<R, A> R collect(Collector<? super T, A, R> collector)——将流转换为其他形式。接收一个Collector接口的实现，
        //用于给Stream中元素做汇总的方法
        //练习获取年龄大于30的集合,结果返回一个List或Set
        System.out.println("****************************************************");
        List<Person> collect = list.stream().filter(person -> person.getAge() > 30).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("****************************************************");
        Set<Person> collect2 = list.stream().filter(person -> person.getAge() > 30).collect(Collectors.toSet());
        collect2.forEach(System.out::println);
        System.out.println("****************************************************");
        /**
         * Collectors.toMap有三个重载方法
         * toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper);
         * toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction);
         * toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction, Supplier<M> mapSupplier);
         * 参数解释:
         *
         * keyMapper：Key 的映射函数
         * valueMapper：Value 的映射函数
         * mergeFunction：当 Key 冲突时，调用的合并方法
         * mapSupplier：Map 构造器，在需要返回特定的 Map 时使用
         *
         * 链接：https://www.jianshu.com/p/b2d78544df64
         */
        //key为id，value为name
        Map<Object,Object> map1 = list.stream().collect(Collectors.toMap(Person::getId,Person::getName));
        System.out.println(map1);
        //key为id，value当前对象————得到 Map的value为对象本身时，t -> t或Function.identity()
        Map<Object,Object> map2 = list.stream().collect(Collectors.toMap(Person::getId,t -> t));
        System.out.println(map2);
        Map<Object,Object> map3 = list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
        System.out.println(map3);
        //如果 List 中 userId 有相同的，使用上面的写法会抛异常这时就需要调用第二个重载方法，传入合并函数
        list.add(new Person(1,"Tom",19));
        //list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
        //java.lang.IllegalStateException: Duplicate key Person{name='Mike', age=22}
        Map<Object,Object> map4 = list.stream().collect(Collectors.toMap(Person::getId, t -> t, (v1, v2) -> v1));
        System.out.println(map4);
        Map<Object,Object> map5 = list.stream().collect(Collectors.toMap(Person::getId, t -> t, (v1, v2) -> v1));
        System.out.println(map5);
        //第四个参数mapSupplier用于返回一个任意类型的Map实例，比如我们希望返回的Map是根据 Key 排序的
        Map<Object,Object> map6 = list.stream().collect(Collectors.toMap(Person::getAge, t -> t, (v1, v2) -> v1, TreeMap::new));
        System.out.println(map6);
    }

    //将字符串中的多个字符构成的机会转换为对应的Stream的实例
    public static Stream<Character> fromStringToStream(String str){
        List<Character> list = new ArrayList<>();
        for(Character c : str.toCharArray()){
            list.add(c);
        }
        return list.stream();
    }

    @Test
    public void test100(){
        List<Integer> list = new ArrayList<>();
        System.out.println(list==null);
        System.out.println(list.size());
        List<Integer> collect = list.stream().collect(Collectors.toList());
        System.out.println(collect==null);
        System.out.println(collect.size());
    }

    //groupingBy
    //https://www.jianshu.com/p/077108043a77
    @Test
    public void case_20220815(){
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 1, 2, 3);
        Map<Integer, List<Integer>> collect = intList.stream().collect(Collectors.groupingBy(e -> e%2));
        System.out.println(collect);
    }

    //Peek
    //https://blog.csdn.net/weixin_42218169/article/details/117357054
    @Test
    public void case_20220815_2(){
        List<Integer> list = Arrays.asList(4, 7, 9, 11, 12);
        list.stream()
                .peek(x -> System.out.println("stream: " + x))
                .map(x -> x + 2)
                .peek(x -> System.out.println("map: " + x))
                .filter(x -> x % 2 != 0)
                .peek(x -> System.out.println("filter: " + x))
                .limit(2)
                .peek(x -> System.out.println("limit: " + x))
                .collect(Collectors.toList());
    }

//    获取重复数据
    @Test
    public void case_20221027(){
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        List<String> collect = strings.stream().collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream() // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
                .map(entry -> entry.getKey()) // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList());// 转化为 List
        System.out.println(collect);
    }
}
