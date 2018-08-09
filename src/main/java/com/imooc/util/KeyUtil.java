package com.imooc.util;

import com.imooc.dataobject.OrderDetail;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Description 主键生成工具类
 * @Author GuanHuizhen
 * @Date 2018/8/2
 */
public class KeyUtil {

    public static synchronized String getUniqueKey() {
        Random random = new Random();
        Integer a = random.nextInt(900000) + 100000;
        System.out.println(a);
        return  ""+System.currentTimeMillis()+ a;//特别注意，这个""要放最前面，否则时间+a会变成数学计算，而不是字符串的拼接，或者将a用String.valueOf(a)转换成String
    }

    public static void main(String[] args) {

        List<Integer> a1 = new ArrayList<>();
        List<Integer> a2 = new LinkedList<>();
        for(int i=1;i<1000000;i++) {
            a1.add(i);
            a2.add(i);
        }
        long begin = System.currentTimeMillis();
        int index=1000;//插入的位置
        int num = 1;//插入的数量
        for(int j=0;j<num;j++)
         a1.add(index,j);
        long end = System.currentTimeMillis();
        System.out.println(end-begin);
        for(int z=0;z<num;z++)
            a2.add(index,z);
        long end2 = System.currentTimeMillis();
        System.out.println(end2-end);

        /*Random random = new Random();
        while (true) {
            Integer a = random.nextInt(900000) + 100000;
            System.out.println(a);
        }*/

       /*List<Integer> nums =  Arrays.asList(1,2,3,4,5);
       nums.forEach(e -> {
           System.out.println(e);
       });

       List<OrderDetail> orderList = nums.stream().map(e -> new OrderDetail(e)).collect(Collectors.toList());
       orderList.forEach(e -> System.out.println(e.getProductQuantity()));
        System.out.println(getUniqueKey());*/

      /* Person person = new Person("guan",12);
       System.out.println(person.name);
        int finalCount = 124;
        MyInterface i = new MyInterface() {
           @Override
           public void saySomething() {
               System.out.println("name="+person.name+",age="+person.age+",基础类型"+ finalCount);
           }
       };
       person.name="haha";
       //finalCount = 188;
       System.out.println(person.name+",count="+finalCount);
       i.saySomething();*/

    }

}
/*
interface MyInterface {
    public void saySomething() ;
}

class Person {
    String name;
    int age;
    Person(String name,int age) {
        this.name=name;
        this.age=age;
    }
}*/
