package com.wangyueyu.bishe;

import lombok.Data;

/**
 * description
 *
 * @author yueyu.wang@hand-china.com 2021/03/09 17:44
 */
@Data
public class Person {
    private String name;
    private String sex;
    public Person(String str1,String str2){
        this.name=str1;
        this.sex=str2;
    }
    public Person(String str1){
        this.name=str1;
    }
}
