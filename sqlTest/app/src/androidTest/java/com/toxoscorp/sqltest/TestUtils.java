package com.toxoscorp.sqltest;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

public class TestUtils {
    public static void test() {
        System.out.println("TestUtils.test");
    }

    public static User createUser(int id) {
        User user = new User();
        user.uid = id;
        return user;
    }
}
