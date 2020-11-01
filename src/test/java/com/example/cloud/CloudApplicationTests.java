package com.example.cloud;

import com.example.cloud.domain.User;
import com.example.cloud.service.MailSendService;
import com.example.cloud.util.FileTypeUtil;
import com.example.cloud.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@SpringBootTest
class CloudApplicationTests {



    @Test
    void contextLoads() {
        System.out.println(FileTypeUtil.getFileType(".mp3"));
/*        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Date d = new Date();
        System.out.println(sdf.format(d));*/
/*        User user = new User();
        user.setAccount("aa");
        System.out.println(user.getAccount());
        String s = "123456";

        ArrayList<Integer> a = new ArrayList<Integer>();*/
    }



}
