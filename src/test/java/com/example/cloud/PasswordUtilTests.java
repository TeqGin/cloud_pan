package com.example.cloud;

import com.example.cloud.service.MailSendService;
import com.example.cloud.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class PasswordUtilTests {

    @Test
    void Md5(){
        String str = "123456";
        String after = PasswordUtil.encodePassword(str);
        System.out.println(after);
    }

    @Test
    void verifyCode(){
        for (int i = 0;i < 5; i++)
            System.out.println(PasswordUtil.getVerifyCode());
    }

}
