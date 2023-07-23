package com.zhouyong.practice.validator.provider;

import com.zhouyong.practice.validator.bean.People2Bean;
import com.zhouyong.practice.validator.group.Group;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author join
 */
public class PeopleGroupSequenceProvider implements DefaultGroupSequenceProvider<People2Bean> {

    @Override
    public List<Class<?>> getValidationGroups(People2Bean bean) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        // 这一步不能省,否则Default分组会抛异常，这个地方还没太弄明白，后面有时间再研究一下
        defaultGroupSequence.add(People2Bean.class);

        if (bean != null) {
            Boolean isMarried=bean.getIsMarried();
            ///System.err.println("是否已婚：" + isMarried + "，执行对应校验逻辑");
            if(isMarried!=null){
                if(isMarried){
                    System.err.println("是否已婚：" + isMarried + "，groups: "+Group.Married.class);
                    defaultGroupSequence.add(Group.Married.class);
                }else{
                    System.err.println("是否已婚：" + isMarried + "，groups: "+Group.UnMarried.class);
                    defaultGroupSequence.add(Group.UnMarried.class);
                }

            }else {
                System.err.println("isMarried is null");
                defaultGroupSequence.add(Group.Married.class);
                defaultGroupSequence.add(Group.UnMarried.class);
            }

        }else{
            System.err.println("bean is null");
        }
        return defaultGroupSequence;
    }
}
