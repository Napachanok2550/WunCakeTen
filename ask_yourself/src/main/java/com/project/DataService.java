package com.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<Entry>>() {}.getType();

    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), ".ask_yourself");
    private static final Path ENTRIES_FILE = APP_DIR.resolve("entries.json");

    public static List<Entry> loadEntries() {
        try {
            if (!Files.exists(ENTRIES_FILE)) return new ArrayList<>();
            String json = Files.readString(ENTRIES_FILE, StandardCharsets.UTF_8);
            List<Entry> list = GSON.fromJson(json, LIST_TYPE);
            return list == null ? new ArrayList<>() : new ArrayList<>(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveEntries(List<Entry> entries) {
        try {
            if (!Files.exists(APP_DIR)) Files.createDirectories(APP_DIR);
            String json = GSON.toJson(entries, LIST_TYPE);
            Files.writeString(ENTRIES_FILE, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Cannot save entries", e);
        }
    }
}