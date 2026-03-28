package com.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Type ENTRY_LIST_TYPE = new TypeToken<List<Entry>>() {
    }.getType();
    private static final Type USER_LIST_TYPE = new TypeToken<List<User>>() {
    }.getType();

    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), ".ask_yourself");
    private static final Path USERS_FILE = APP_DIR.resolve("users.json");

    // -------------------------
    // Entries
    // -------------------------
    public static List<Entry> loadEntries(String username) {
        try {
            Path file = APP_DIR.resolve("entries_" + username + ".json");
            if (!Files.exists(file))
                return new ArrayList<>();

            String json = Files.readString(file, StandardCharsets.UTF_8);
            List<Entry> list = GSON.fromJson(json, ENTRY_LIST_TYPE);

            return list == null ? new ArrayList<>() : new ArrayList<>(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveEntries(String username, List<Entry> entries) {
        try {
            if (!Files.exists(APP_DIR))
                Files.createDirectories(APP_DIR);

            String json = GSON.toJson(entries, ENTRY_LIST_TYPE);
            Path file = APP_DIR.resolve("entries_" + username + ".json");

            Files.writeString(file, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Cannot save entries", e);
        }
    }

    // -------------------------
    // Users
    // -------------------------
    public static List<User> loadUsers() {
        try {
            if (!Files.exists(USERS_FILE))
                return new ArrayList<>();

            String json = Files.readString(USERS_FILE, StandardCharsets.UTF_8);
            List<User> list = GSON.fromJson(json, USER_LIST_TYPE);

            return list == null ? new ArrayList<>() : new ArrayList<>(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) {
        try {
            if (!Files.exists(APP_DIR))
                Files.createDirectories(APP_DIR);

            String json = GSON.toJson(users, USER_LIST_TYPE);
            Files.writeString(USERS_FILE, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Cannot save users", e);
        }
    }

    // -------------------------
    // Hash password
    // -------------------------
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot hash password", e);
        }
    }
}