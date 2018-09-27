package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public static List<String> getModClassNames() {
        List<String> classNames = new ArrayList<>();
        final File[] mods = new File(Constants.modsDir).listFiles();

        if (mods != null) {
            for (File modFile : mods) {
                if (getFileExtension(modFile).equals(".jar")) {
                    try {
                        JarFile jarFile = new JarFile(modFile.getAbsolutePath());
                        Enumeration allEntries = jarFile.entries();
                        while (allEntries.hasMoreElements()) {
                            JarEntry entry = (JarEntry) allEntries.nextElement();
                            String file = entry.getName();
                            if (file.endsWith(".class")) {
                                String className = file.replace('/', '.').substring(0, file.length() - 6);
                                classNames.add(className);
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            return classNames;
        } else {
            return Collections.emptyList();
        }
    }
}
