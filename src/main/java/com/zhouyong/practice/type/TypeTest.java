package com.zhouyong.practice.type;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * 三种获取泛型参数的方式
 *
 * @author join
 * @date 2023/7/9 9:17 下午
 */
public class TypeTest {

    /**
     * 获取类上的泛型参数的实际类型
     */
    @Test
    public void testClassGenericActualType(){
        //该类实现了两个接口分别是：IMessageSender<String, List<Integer>> 和 Serializable
        Type genericInterface1 = SmsMessageImpl.class.getGenericInterfaces()[0];
        Type genericInterface2 = SmsMessageImpl.class.getGenericInterfaces()[1];

        //第一个是带泛型参数的接口：IMessageSender<String, List<Integer>>，
        //因此是ParameterizedType类型:sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
        System.out.println(genericInterface1.getClass());
        Assert.assertEquals(sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl.class, genericInterface1.getClass());

        //第二个是不带泛型参数的接口，因此是普通的Class类型:class java.lang.Class
        System.out.println(genericInterface2.getClass());
        Assert.assertEquals(java.lang.Class.class, genericInterface2.getClass());

        //获取泛化的接口参数的实际类型
        Type[] actualTypeArguments = ((ParameterizedType) genericInterface1).getActualTypeArguments();
        //分别获取该接口上的两个泛型参数类型：String 和 List<Integer>
        System.out.println(actualTypeArguments[0] + " , "+ actualTypeArguments[1]);
        //其中List<Integer>可以继续获取其元素泛型的类型：Integer
        System.out.println(((ParameterizedType)actualTypeArguments[1]).getActualTypeArguments()[0]);

    }

    /**
     * 获取方法参数泛型的实际类型
     */
    @Test
    public void testMethodGenericActualType() throws NoSuchMethodException {
        Method method = TypeTest.class.getMethod("verify", Map.class);
        Parameter parameter = method.getParameters()[0];
        Type type = parameter.getParameterizedType();

        //由于参数是带泛型的，因此type的实际类型是ParameterizedType，
        //可以强制转换，然后获取参数Map中的两个泛型参数: <String,Integer>
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        System.out.println(actualTypeArguments[0] + " , " + actualTypeArguments[1]);

    }

    /**
     * 获取字段上泛型的实际类型
     */
    @Test
    public void testFieldGenericActualType() throws NoSuchFieldException {
        Field field = TypeTest.class.getDeclaredField("list");
        Type genericType = field.getGenericType();
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        //得到字段List<String>泛型中的实际类型String
        System.out.println(actualTypeArguments[0]);
    }

    private List<String> list;

    public void verify(Map<String,Integer> map){
        //测试备用
    }




}
