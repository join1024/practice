package com.join.practice.type;

import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.List;

/**
 * lambda表达式中的泛型获取
 *
 * @author join
 * @date 2023/7/9 9:17 下午
 */
public class LambdaTypeTest {

    @Test
    public void test() throws Exception {
        System.out.println("测试1");
        lambdaTest((List<String> a, Integer b)->a.size()==b);
        System.out.println();

        System.out.println("测试2");
        lambdaTest(this::abc);
        System.out.println();
    }

    public Boolean abc(String a, Integer b){
        return a.equals(String.valueOf(b));
    }

    public <T,S,R> void lambdaTest(LambdaInterface<T,S,R> lambda) throws Exception {
        System.out.println(lambda.getClass());

        //方式1：通过反射获取其方法上的参数类型，结论是泛型被擦除，无法获取
        Method[] declaredMethods = lambda.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.getName().equals("apply")){
                //找到apply方法，并且打印两个参数和返回值的类型
                for (Class<?> parameterType : declaredMethod.getParameterTypes()) {
                    //结论：每个参数打印结果都是Object，说明参数的泛型已经被擦除了，因此无法获取具体的类型
                    System.out.println("参数类型："+parameterType);
                }
                //结论：打印结果为Object，说明返回值的泛型参数也被擦除了
                System.out.println("返回值类型："+declaredMethod.getReturnType());
            }
        }

        //方式二，通过SerializedLambda获取
        //由于LambdaInterface接口实现了序列化，因此会有writeReplace方法，这时虚拟机生成的用于序列化lambda表达式的方式
        Method method = lambda.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);

        //获取lambda表达式中真实方法的类的限定名：包名是斜杠"/"分割的，替换成点号"."即可得到类的限定名
        String implClassName = serializedLambda.getImplClass().replace("/",".");
        System.out.println(implClassName);
        //通过限定名获取类
        Class implClass = Class.forName(implClassName);

        //获取lambda表达式中真实的调用方法名
        String implMethodName = serializedLambda.getImplMethodName();
        System.out.println(serializedLambda.getImplClass());

        //获取lambda实现的真实的方法
        Method implMethod = null;
        for (Method m : implClass.getDeclaredMethods()) {
            if(m.getName().equals(implMethodName)){
                implMethod = m;
            }
        }

        Parameter[] parameters = implMethod.getParameters();
        System.out.println("\nlambda表达式方法上泛型参数的真实类型：");
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Type parameType = parameter.getParameterizedType();
            System.out.println("\t参数"+i+": "+parameType.getTypeName());
            if(parameType instanceof ParameterizedType){
                //如果参数自身包含泛型，如：List<String> ，则继续得到其泛型的实际类型String
                Type[] actualTypeArguments = ((ParameterizedType) parameType).getActualTypeArguments();
                for (int j = 0; j < actualTypeArguments.length; j++) {
                    //当然还可以继续递归解析，判断actualTypeArguments[j]是不是ParameterizedType的实例，为了简化不去递归了
                    System.out.println("\t\t"+actualTypeArguments[j].getTypeName());
                }
            }
        }

        System.out.println();

        /**
         * 方式二：通过解析方法签名获取泛型参数的真实类型
         * TODO
         */
        //得到方法签名，返回的是字节码形式的描述符：(Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Boolean;
        String implMethodSignature = serializedLambda.getImplMethodSignature();
        System.out.println("方法签名："+implMethodSignature);

    }


}
