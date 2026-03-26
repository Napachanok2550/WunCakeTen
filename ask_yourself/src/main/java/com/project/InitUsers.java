package com.project;

import java.util.List;

public class InitUsers {
    public static void createDefaultUser() {
        List<User> users = DataService.loadUsers();
        if (users.isEmpty()) {
            users.add(new User("admin", DataService.hashPassword("1234")));
            DataService.saveUsers(users);
            System.out.println("สร้าง user เริ่มต้น: admin / 1234");
        }
    }
}