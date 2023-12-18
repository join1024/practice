package com.zhouyong.practice.ognl;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author zhouyong
 * @date 2023/11/26 8:07 下午
 */
public class OgnlParserTest {

    @Test
    public void testByMap(){
        Map order = new HashMap(5);
        Map user = new HashMap(5);
        Map address = new HashMap(5);

        order.put("orderId", "order001");
        order.put("user", user);
        order.put("all", new Map[]{user,address});

        user.put("name","张三");
        user.put("sex", "男");
        user.put("numbers", new int[]{1,2,3});
        user.put("address", address);

        address.put("province", "广东省");
        address.put("city", "深圳市");

        Assert.assertEquals("order001", OgnlParser.getFieldValue(order, "orderId"));
        Assert.assertEquals(1, (int) OgnlParser.getFieldValue(order, "user.numbers[0]"));
        Assert.assertEquals("广东省", OgnlParser.getFieldValue(order, "user.address.province"));

        Assert.assertEquals("张三", OgnlParser.getFieldValue(order, "user.name"));
        Assert.assertEquals("张三", OgnlParser.getFieldValue(order, "all[0].name"));
        Assert.assertEquals("张三", OgnlParser.getFieldValue(user, "name"));
    }

    @Test
    public void testByObject(){
        Address address1 = new Address("广东省","深圳市", "罗湖区菜围街道");
        Address address2 = new Address("湖南省","长沙市", "岳麓区梅溪湖街道");
        List<Address> addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);

        User user = new User("user001", "张三", addressList, new String[]{"zhangsan@qq.com","zhangsan@163.com"});

        Map otherInfo = new HashMap(4);
        otherInfo.put("status", "已发货");
        otherInfo.put("user001", user);
        otherInfo.put("addressList", addressList);
        otherInfo.put("emails", user.emails);
        Order order = new Order("order001", new Date(), user, otherInfo);

        Assert.assertEquals(order.orderId, OgnlParser.getFieldValue(order, "orderId"));
        Assert.assertEquals(order.createdDate, OgnlParser.getFieldValue(order, "createdDate"));
        Assert.assertEquals(order.otherInfo.get("status"), OgnlParser.getFieldValue(order, "otherInfo.status"));

        Assert.assertEquals(order.users, OgnlParser.getFieldValue(order, "users"));
        Assert.assertEquals(order.users[0], OgnlParser.getFieldValue(order, "users[0]"));

        Assert.assertEquals(order.sequences, OgnlParser.getFieldValue(order, "sequences"));
        int sequence0 = OgnlParser.getFieldValue(order, "sequences[0]");
        Assert.assertEquals(order.sequences[0], sequence0);

        Assert.assertEquals(user.userId, OgnlParser.getFieldValue(order, "otherInfo.user001.userId"));
        Assert.assertEquals(user.userName, OgnlParser.getFieldValue(order, "otherInfo.user001.userName"));

        String[] emails = OgnlParser.getFieldValue(order, "otherInfo.emails");
        Assert.assertEquals(user.emails, emails);
        String emails0 = OgnlParser.getFieldValue(order, "otherInfo.emails[0]");
        Assert.assertEquals(user.emails[0], emails0);
        Assert.assertEquals(user.emails[1], OgnlParser.getFieldValue(order, "otherInfo.emails[1]"));

        Assert.assertEquals(user.emails, OgnlParser.getFieldValue(order, "otherInfo.user001.emails"));
        Assert.assertEquals(user.emails[0], OgnlParser.getFieldValue(order, "otherInfo.user001.emails[0]"));
        Assert.assertEquals(user.emails[1], OgnlParser.getFieldValue(order, "otherInfo.user001.emails[1]"));

        Assert.assertEquals(user.addressList, OgnlParser.getFieldValue(order, "otherInfo.addressList"));
        Assert.assertEquals(user.addressList.get(0), OgnlParser.getFieldValue(order, "otherInfo.addressList[0]"));
        Assert.assertEquals(user.addressList.get(1), OgnlParser.getFieldValue(order, "otherInfo.addressList[1]"));
        Assert.assertEquals(user.addressList.get(1).city, OgnlParser.getFieldValue(order, "otherInfo.addressList[1].city"));
        Assert.assertEquals(user.addressList.get(1).province, OgnlParser.getFieldValue(order, "otherInfo.addressList[1].province"));
        Assert.assertEquals(user.addressList.get(1).detail, OgnlParser.getFieldValue(order, "otherInfo.addressList[1].detail"));

        Assert.assertEquals(user.addressList, OgnlParser.getFieldValue(order, "otherInfo.user001.addressList"));
        Assert.assertEquals(user.addressList.get(0), OgnlParser.getFieldValue(order, "otherInfo.user001.addressList[0]"));
        Assert.assertEquals(user.addressList.get(1), OgnlParser.getFieldValue(order, "otherInfo.user001.addressList[1]"));
        Assert.assertEquals(user.addressList.get(1).city, OgnlParser.getFieldValue(order, "otherInfo.user001.addressList[1].city"));
        Assert.assertEquals(user.addressList.get(1).province, OgnlParser.getFieldValue(order, "otherInfo.user001.addressList[1].province"));
        Assert.assertEquals(user.addressList.get(1).detail, OgnlParser.getFieldValue(order, "otherInfo.user001.addressList[1].detail"));

        Assert.assertEquals(order.user.userId, OgnlParser.getFieldValue(order, "user.userId"));
        Assert.assertEquals(order.user.userName, OgnlParser.getFieldValue(order, "user.userName"));

        Assert.assertEquals(user.emails, OgnlParser.getFieldValue(order, "user.emails"));
        Assert.assertEquals(user.emails[0], OgnlParser.getFieldValue(order, "user.emails[0]"));
        Assert.assertEquals(user.emails[1], OgnlParser.getFieldValue(order, "user.emails[1]"));

        Assert.assertEquals(user.addressList, OgnlParser.getFieldValue(order, "user.addressList"));
        Assert.assertEquals(user.addressList.get(0), OgnlParser.getFieldValue(order, "user.addressList[0]"));
        Assert.assertEquals(user.addressList.get(1), OgnlParser.getFieldValue(order, "user.addressList[1]"));
        Assert.assertEquals(user.addressList.get(1).city, OgnlParser.getFieldValue(order, "user.addressList[1].city"));
        Assert.assertEquals(user.addressList.get(1).province, OgnlParser.getFieldValue(order, "user.addressList[1].province"));
        Assert.assertEquals(user.addressList.get(1).detail, OgnlParser.getFieldValue(order, "user.addressList[1].detail"));
    }

    class Order{
        private String orderId;
        private Date createdDate;
        private User user;
        private User[] users;
        private Map otherInfo;
        private int[] sequences = new int[]{1,2,3};

        public Order(String orderId, Date createdDate, User user, Map otherInfo) {
            this.orderId = orderId;
            this.createdDate = createdDate;
            this.user = user;
            this.otherInfo = otherInfo;
            this.users = new User[]{user};
        }
    }

    class User{
        private String userId;
        private String userName;
        private List<Address> addressList;
        private String[] emails;

        public User(String userId, String userName, List<Address> addressList, String[] emails) {
            this.userId = userId;
            this.userName = userName;
            this.addressList = addressList;
            this.emails = emails;
        }
    }

    class Address{
        private String province;
        private String city;
        private String detail;

        public Address(String province, String city, String detail) {
            this.province = province;
            this.city = city;
            this.detail = detail;
        }
    }

}


