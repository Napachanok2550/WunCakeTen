package com.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Entry list type
    private static final Type ENTRY_LIST_TYPE = new TypeToken<List<Entry>>() {}.getType();

    // Question list type
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>() {}.getType();

    // Paths
    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), ".ask_yourself");
    private static final Path ENTRIES_FILE = APP_DIR.resolve("entries.json");
    private static final Path QUESTIONS_FILE = APP_DIR.resolve("questions_order.json");

    // -------------------------
    // 📘 Entries (Journal)
    // -------------------------

    public static List<Entry> loadEntries() {
        try {
            if (!Files.exists(ENTRIES_FILE)) return new ArrayList<>();

            String json = Files.readString(ENTRIES_FILE, StandardCharsets.UTF_8);
            List<Entry> list = GSON.fromJson(json, ENTRY_LIST_TYPE);

            return list == null ? new ArrayList<>() : new ArrayList<>(list);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveEntries(List<Entry> entries) {
        try {
            if (!Files.exists(APP_DIR)) Files.createDirectories(APP_DIR);

            String json = GSON.toJson(entries, ENTRY_LIST_TYPE);

            Files.writeString(
                    ENTRIES_FILE,
                    json,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            throw new RuntimeException("Cannot save entries", e);
        }
    }

    //Questions (No Repeat) //

    public static List<String> loadShuffledQuestions(List<String> original) {
        try {
            // ถ้ามีไฟล์อยู่แล้ว → ใช้ของเดิม
            if (Files.exists(QUESTIONS_FILE)) {
                String json = Files.readString(QUESTIONS_FILE, StandardCharsets.UTF_8);
                List<String> list = GSON.fromJson(json, STRING_LIST_TYPE);

                if (list != null && !list.isEmpty()) {
                    return list;
                }
            }

            // ยังไม่มี → shuffle แล้ว save
            List<String> shuffled = new ArrayList<>(original);
            Collections.shuffle(shuffled);

            if (!Files.exists(APP_DIR)) Files.createDirectories(APP_DIR);

            String json = GSON.toJson(shuffled, STRING_LIST_TYPE);

            Files.writeString(
                    QUESTIONS_FILE,
                    json,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return shuffled;

        } catch (Exception e) {
            e.printStackTrace();
            return original; // fallback
        }
    }
}