package com.join.practice.lambda;

import org.springframework.beans.BeanUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyBeanUtils {

    public static <T> void copyProperties(Object source, Object target, MyFunctional<T> ... ignoreProperties){

        String[] ignorePropertieNames=null;

        if(ignoreProperties!=null && ignoreProperties.length>0){
            ignorePropertieNames=new String[ignoreProperties.length];
            for (int i = 0; i < ignoreProperties.length; i++) {
                MyFunctional fn=ignoreProperties[i];
                ignorePropertieNames[i]=getPropertyName(fn);
            }
        }

        BeanUtils.copyProperties(source,target,ignorePropertieNames);
    }

    public static <T> String getPropertyName(MyFunctional<T> lambda) {
        try {
            Class lambdaClass=lambda.getClass();
            System.out.println("-------------分割线1-----------");
            //打印类名：
            System.out.print("类名：");
            System.out.println(lambdaClass.getName());
            //打印接口名：
            System.out.print("接口名：");
            Arrays.stream(lambdaClass.getInterfaces()).forEach(System.out::print);
            System.out.println();
            //打印方法名：
            System.out.print("方法名：");
            for (Method method : lambdaClass.getDeclaredMethods()) {
                System.out.print(method.getName()+"  ");
            }

            System.out.println();
            System.out.println("-------------分割线2-----------");
            System.out.println();

            Method method = lambdaClass.getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String getterMethod = serializedLambda.getImplMethodName();
            System.out.println("lambda表达式调用的方法名："+getterMethod);
            String fieldName = Introspector.decapitalize(getterMethod.replace("get", ""));
            System.out.println("根据方法名得到的字段名："+fieldName);

            System.out.println();
            System.out.println("-------------分割线3-----------");
            System.out.println();
            System.out.println("SerializedLambda中的所有方法：");
            for (Method declaredMethod : serializedLambda.getClass().getDeclaredMethods()) {
                if(declaredMethod.getParameterCount()==0){
                    declaredMethod.setAccessible(Boolean.TRUE);
                    System.out.println("调用方法： "+declaredMethod.getName()+": "+declaredMethod.invoke(serializedLambda));
                }else{
                    System.out.println("方法声明："+declaredMethod.getName()+"("+ Arrays.stream(declaredMethod.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", "))+")");
                }

            }

            return fieldName;

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
