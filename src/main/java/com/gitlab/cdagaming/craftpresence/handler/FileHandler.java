package com.gitlab.cdagaming.craftpresence.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class FileHandler {
    private static Gson GSON = new GsonBuilder().create();

    public static <T> T getJSONFromFile(File file, Class<T> clazz) throws Exception {
        return getJSONFromFile(fileToString(file), clazz);
    }

    public static <T> T getJSONFromFile(String file, Class<T> clazz) {
        return GSON.fromJson(file, clazz);
    }

    public static String fileToString(File file) throws Exception {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
