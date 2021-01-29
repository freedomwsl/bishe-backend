package com.wangyueyu.bishe;

import com.wangyueyu.bishe.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author 25721 2021/01/11 9:23
 */
@Slf4j
public class Jiashuju {
    public static void main(String[] args) {
        try{
            double i=10/0;
        }catch (Exception e){
//            e.printStackTrace();
//            log.error("",e);
            throw new RuntimeException("xxx",e);
        }
        System.out.println("111");
        System.out.println("111");
        System.out.println("111");
    }



}
