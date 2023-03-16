package com.bocchi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test1 {

    @Autowired
    private App app;

    @Test
    void Tb(){
        List<User> users = app.selectList(null);
        System.out.println(users);
    }
}
