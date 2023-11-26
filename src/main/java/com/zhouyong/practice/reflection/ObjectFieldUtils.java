package com.zhouyong.practice.reflection;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyong
 * @date 2023/11/26 8:07 下午
 */
public class ObjectFieldUtils {

    /**
     * 根据属性名获取对象的属性值
     * @param obj 对象实例
     * @param propertyNamePath
     * 属性名的全路径，多层对象嵌套属性支持链式操作、数组下标的访问方式
     *   eg1: "a" 返回属性a的值
     *   eg2: "a[0]" 属性a为数组或者List，返回a的第一个元素值
     *   eg3: "a.b.c" 多层嵌套属性的访问方式，对象a包含对象b，对象b又包含c，通过该方式直接访问属性c的值
     *   eg4: "a.b.c[0]" 属性c为数组或者List，返回c的第一个元素值
     *
     * @return
     */
    public static <T> T getFieldValue(Object obj, String propertyNamePath){

        if(obj==null || propertyNamePath==null){
            return null;
        }

        T fieldValue = null;
        Object iteratorObj = obj;

        String[] fieldNames = propertyNamePath.split("\\.");
        List<String> fieldNameList = Stream.of(fieldNames).collect(Collectors.toList());
        Iterator<String> fieldNameIterator = fieldNameList.iterator();

        while(fieldNameIterator.hasNext()){
            String fieldName = fieldNameIterator.next();
            fieldValue = getValue(iteratorObj, fieldName);
            if(fieldValue!=null){
                if(fieldNameIterator.hasNext()){
                    iteratorObj = fieldValue;
                }
            }
        }

        return fieldValue;
    }

    /**
     * 根据属性名获取属性值
     * @param obj
     * @param propertyName
     * @return
     */
    private static <T> T getValue(Object obj, String propertyName){
        if(obj==null || propertyName==null){
            return null;
        }

        Class objClass = obj.getClass();

        T value = null;
        String fieldName = parseFieldName(propertyName);

        //如果是Map
        if(Map.class.isAssignableFrom(objClass)){
            Object valueFromMap = (T)((Map) obj).get(fieldName);
            value = getElementIfArray(valueFromMap, parseIndex(propertyName));
        }
        //否则通过反射获取字段值
        else{
            Field field = getField(objClass, fieldName);
            if(field!=null){
                boolean accessible = field.isAccessible();
                if(!accessible){
                    field.setAccessible(true);
                }
                try {
                    Object valueFromField = field.get(obj);
                    value = getElementIfArray(valueFromField, parseIndex(propertyName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }finally {
                    if(!accessible){
                        field.setAccessible(false);
                    }
                }
            }
        }

        return value;
    }

    /**
     * 数组或List的特殊处理
     * @param value
     * @param index
     * @return
     */
    private static <T> T getElementIfArray(Object value, Integer index) {
        if(value!=null && index!=null){
            Class valueType = value.getClass();

            //如果是List, 包含下标则返回对应的元素值
            if(List.class.isAssignableFrom(valueType)){
                List list = (List) value;
                if(index<list.size()){
                    value = list.get(index);
                }
            }

            //如果是数组，包含下标则返回对应的元素值
            else if(valueType.isArray()){
                //8种基本类型需要单独转换为对应基本类型的数组，直接转为Object[]会抛强制转换异常
                if(char[].class.equals(valueType)){
                    char[] array = (char[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(byte[].class.equals(valueType)){
                    byte[] array = (byte[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(short[].class.equals(valueType)){
                    short[] array = (short[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(int[].class.equals(valueType)){
                    int[] array = (int[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(long[].class.equals(valueType)){
                    long[] array = (long[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(float[].class.equals(valueType)){
                    float[] array = (float[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(double[].class.equals(valueType)){
                    double[] array = (double[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }else if(boolean[].class.equals(valueType)){
                    boolean[] array = (boolean[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }
                //非基本类型的数组可以直接强制转换为Object[]
                else{
                    Object[] array = (Object[]) value;
                    if(index<array.length){
                        value = array[index];
                    }
                }

            }

        }
        return (T) value;
    }

    /**
     * 根据字段名获取字段
     * @param objClass
     * @param fieldName
     * @return
     */
    private static Field getField(Class objClass, String fieldName){
        if(objClass==null || fieldName==null){
            return null;
        }
        Class superClass = objClass;
        Field field = null;
        do{
            try {
                field = superClass.getDeclaredField(fieldName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            superClass = superClass.getSuperclass();
        }while(!Object.class.equals(superClass) && field==null);

        return field;
    }

    /**
     * 如果属性名带下标，则去掉下标解析出属性名
     * @param propertyName
     * @return
     */
    private static String parseFieldName(String propertyName) {
        int index = propertyName.indexOf("[");
        if(index>0){
            propertyName = propertyName.substring(0,index);

        }
        return propertyName;
    }

    /**
     * 如果属性名带下标，则解析下标
     * @param propertyName
     * @return
     */
    private static Integer parseIndex(String propertyName) {
        Integer index = null;
        int startIdx = propertyName.indexOf("[");
        if(startIdx>0 && propertyName.endsWith("]")){
            try {
                index = Integer.valueOf(propertyName.substring(startIdx+1, propertyName.length() - 1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return index;
    }

}
