package com.zhouyong.practice.stream;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author join
 * @date 2022/2/19 2:57 下午
 */

public class StreamTest {

    @Test
    public void arrayToStream() {
        int sum= Arrays.stream(new int[]{1,2,3,4,5})
                .sum();
        System.out.println(sum);
    }

    @Test
    public void fileToStream(){
        String path="/Users/join/code/java/practice/src/main/java/com/join/practice/stream/file.txt";
        //Files.lines 将每一行转换为一个数组然后包装为Stream
        try(Stream lines = Files.lines(Paths.get(path), Charset.defaultCharset())){
            lines.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void iterateStream(){
        /*Stream.iterate(0,n->n+2)
                .limit(20)
                .forEach(System.out::println);*/

        //斐波那契数
        Stream.iterate(new int[]{0,1}, arr->new int[]{arr[1],arr[0]+arr[1]})
                .map(arr->arr[0])
                .limit(10)
                .forEach(System.out::println);

    }

    @Test
    public void generateStream(){
        //生成20个随机值
        //Stream.generate(Math::random).limit(20).forEach(System.out::println);
        //生成5个固定值
        //IntStream.generate(()->1).limit(5).forEach(System.out::println);

        //有内部状态的方式生成斐波那契数
        IntStream.generate(new IntSupplier() {
                    //有内部值
                    public int previous=0;
                    public int current=1;
                    @Override
                    public int getAsInt() {
                        //System.out.println(this.previous+","+this.current);
                        int oldPrevious=this.previous;
                        int nextValue=this.previous+this.current;
                        this.previous=this.current;
                        this.current=nextValue;
                        return oldPrevious;
                    }

                    public int getPrevious() {
                        return previous;
                    }

                    public int getCurrent() {
                        return current;
                    }
                })
                .limit(10)
                .forEach(i->System.out.println(i));

    }

    @Test
    public void filterStream(){
        //该操作停不下来，虽然用filter做了大小过滤的限制，
        // 但是filter不是一个短路操作，
        // filter的作用是过滤满足条件的数据，但是不满足条件不会停下来，
        // 只会在继续判断下一个元素是否满足条件，知道所有的元素判断完毕为止
        //limit、takeWhile（java9)则是短路操作，不满足条件会停下来。
        IntStream.iterate(0,i->i+1).filter(i->i<10).forEach(System.out::println);
    }

    @Test
    public void filterStream2(){
        //对如下数组去重
        Order[] arr = new Order[]{
                new Order("N001","P002","C002")
                ,new Order("N002","P002","C002")
                ,new Order("N003","P004","C004")
                ,new Order("N004","P004","C004")
                ,new Order("N004","P004","C004")
        };

        //嘴贱但的去重可以使用distinct, distinct是基于hashcode和equals的去重
        Stream.of(arr).distinct().forEach(System.out::println);
        System.out.println("=====");

        //如果要根据prdCode去重，如何实现呢：通过filter来实现
        //需要一个外部变量来保存状态，在filter中根据这个状态值来判断。
        Set outSet = new HashSet(arr.length);
        Stream.of(arr)
                .filter(o->outSet.add(o.getPrdCode()))
                .forEach(System.out::println);
        System.out.println("========");

        //也可以定义一个lambda的内部状态
        Stream.of(arr)
                .filter(new Predicate<Order>() {
                    //内部状态对象
                    Set innerSet = new HashSet(arr.length);
                    @Override
                    public boolean test(Order order) {
                        return innerSet.add(order.getPrdCode());
                    }
                })
                .forEach(System.out::println);

        //注意：这种需要需要改变内部状态或外部状态值的并不适应并发ParallelStream
    }

    public static class Order{
        //订单编号
        private String orderNo;
        //产品编号
        private String prdCode;
        //客户id
        private String custId;

        public Order(String orderNo, String prdCode, String custId) {
            this.orderNo = orderNo;
            this.prdCode = prdCode;
            this.custId = custId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order order = (Order) o;
            return orderNo.equals(order.orderNo) &&
                    prdCode.equals(order.prdCode) &&
                    custId.equals(order.custId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderNo, prdCode, custId);
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderNo='" + orderNo + '\'' +
                    ", prdCode='" + prdCode + '\'' +
                    ", custId='" + custId + '\'' +
                    '}';
        }

        public String getOrderNo() {
            return orderNo;
        }

        public String getPrdCode() {
            return prdCode;
        }

        public String getCustId() {
            return custId;
        }
    }
}
