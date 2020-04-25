/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * File Utilities for interpreting Files and Class Objects
 *
 * @author CDAGaming
 */
public class FileUtils {
    /**
     * A GSON Json Builder Instance
     */
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Retrieves File Data and Converts it into a Parsed Json Syntax
     *
     * @param file     The File to access
     * @param classObj The target class to base the output on
     * @param <T>      The Result and Class Type
     * @return The Parsed Json as the Class Type's Syntax
     * @throws Exception If Unable to read the File
     */
    public static <T> T getJSONFromFile(File file, Class<T> classObj) throws Exception {
        return getJSONFromFile(fileToString(file, "UTF-8"), classObj);
    }

    /**
     * Retrieves File Data and Converts it into a Parsed Json Syntax
     *
     * @param file     The file data to access, as a string
     * @param classObj The target class to base the output on
     * @param <T>      The Result and Class Type
     * @return The Parsed Json as the Class Type's Syntax
     */
    public static <T> T getJSONFromFile(String file, Class<T> classObj) {
        return GSON.fromJson(file, classObj);
    }

    /**
     * Parses inputted raw json into a valid JsonObject
     *
     * @param json The raw Input Json String
     * @return A Parsed and Valid JsonObject
     */
    public static JsonObject parseJson(String json) {
        if (!StringUtils.isNullOrEmpty(json)) {
            final JsonParser dataParser = new JsonParser();
            return dataParser.parse(json).getAsJsonObject();
        } else {
            return new JsonObject();
        }
    }

    /**
     * Downloads a File from a Url, then stores it at the target location
     *
     * @param urlString The Download Link
     * @param file      The destination and filename to store the download as
     */
    public static void downloadFile(final String urlString, final File file) {
        try {
            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.download.init", file.getName(), file.getAbsolutePath(), urlString));
            final URL url = new URL(urlString);
            if (file.exists()) {
                final boolean fileDeleted = file.delete();
                if (!fileDeleted) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.delete.file", file.getName()));
                }
            }

            org.apache.commons.io.FileUtils.copyURLToFile(url, file);
            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.download.loaded", file.getName(), file.getAbsolutePath(), urlString));
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.download", file.getName(), urlString, file.getAbsolutePath()));
            ex.printStackTrace();
        }
    }

    /**
     * Attempts to load the specified file as a DLL
     *
     * @param file The file to attempt to load
     */
    public static void loadFileAsDLL(final File file) {
        try {
            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.dll.init", file.getName()));
            boolean isPermsSet = file.setReadable(true) && file.setWritable(true);
            if (isPermsSet) {
                System.load(file.getAbsolutePath());
            }
            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.dll.loaded", file.getName()));
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.dll", file.getName()));
            ex.printStackTrace();
        }
    }

    /**
     * Attempts to convert a File's data into a readable String
     *
     * @param file     The file to access
     * @param encoding The encoding to parse the file as
     * @return The file's data as a String
     * @throws Exception If Unable to read the file
     */
    public static String fileToString(File file, String encoding) throws Exception {
        return org.apache.commons.io.FileUtils.readFileToString(file, Charset.forName(encoding));
    }

    /**
     * Gets the File Extension of a File (Ex: txt)
     *
     * @param file The file to access
     * @return The file's extension String
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    /**
     * Retrieve the Amount of Active Mods in the {@link ModUtils#modsDir}
     *
     * @return The Mods that are active in the directory
     */
    public static int getModCount() {
        // Mod is within ClassLoader if in a Dev Environment
        // and is thus automatically counted if this is the case
        int modCount = ModUtils.IS_DEV ? 1 : 0;
        final File[] mods = new File(ModUtils.modsDir).listFiles();

        if (mods != null) {
            for (File modFile : mods) {
                if (getFileExtension(modFile).equals(".jar")) {
                    modCount++;
                }
            }
        }
        return modCount;
    }

    /**
     * Retrieve a List of Classes that extend or implement anything in the search list
     *
     * @param searchList     The Super Type Classes to look for within the source packages specified
     * @param sourcePackages The root package directories to search within
     * @return The List of found classes from the search
     */
    @SuppressWarnings("UnstableApiUsage")
    public static List<Class<?>> getClassNamesMatchingSuperType(final List<Class<?>> searchList, final String... sourcePackages) {
        final List<Class<?>> matchingClasses = Lists.newArrayList(), availableClassList = Lists.newArrayList();
        final List<ClassPath.ClassInfo> classList = Lists.newArrayList();

        // Attempt to Get Top Level Classes from the JVM Class Loader
        try {
            classList.addAll(ClassPath.from(ModUtils.CLASS_LOADER).getTopLevelClasses());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (ClassPath.ClassInfo classInfo : classList) {
            for (String startString : sourcePackages) {
                // Attempt to Add Classes Matching any of the Source Packages
                if (classInfo.getName().startsWith(startString) && (!classInfo.getName().contains("FMLServerHandler") && !classInfo.getName().toLowerCase().contains("mixin"))) {
                    try {
                        Class<?> classObj = Class.forName(classInfo.getName());
                        availableClassList.add(classObj);
                        for (Class<?> subClassObj : classObj.getClasses()) {
                            if (!availableClassList.contains(subClassObj)) {
                                availableClassList.add(subClassObj);
                            }
                        }
                    } catch (Exception ignored) {
                        // Ignore this Exception and Continue
                    } catch (Error ignored) {
                        // Ignore this Error and Continue
                    }
                }
            }
        }

        for (Class<?> classObj : availableClassList) {
            // Add All SuperClasses of this Class to a List
            matchingClasses.addAll(getMatchedClassesAfter(searchList, classObj));
        }

        // Attempt to Retrieve Mod Classes
        for (String modClassString : getModClassNames()) {
            if (!modClassString.toLowerCase().contains("mixin")) {
                try {
                    // Add all SuperClasses of Mod Class to a List
                    matchingClasses.addAll(getMatchedClassesAfter(searchList, Class.forName(modClassString)));
                } catch (Exception ignored) {
                    // Ignore this Exception and Continue
                } catch (Error ignored) {
                    // Ignore this Error and Continue
                }
            }
        }
        return matchingClasses;
    }

    /**
     * Retrieves adjusted matching classes after iterating through a Class + Extensions
     *
     * @param searchList The Current Search List for Class Locations to match against
     * @param currentClassObj The Current (Original) Class Object
     * @return Adjusted matching classes after iterating through a Class + Extensions
     */
    private static List<Class<?>> getMatchedClassesAfter(final List<Class<?>> searchList, Class<?> currentClassObj) {
        final Class<?> classObj = currentClassObj;
        final List<Class<?>> superClassList = Lists.newArrayList(), matchingClasses = Lists.newArrayList();

        while (currentClassObj.getSuperclass() != null && !searchList.contains(currentClassObj.getSuperclass())) {
            superClassList.add(currentClassObj.getSuperclass());
            currentClassObj = currentClassObj.getSuperclass();
        }

        // If Match is Found, add original Class to final List, and add all Super Classes to returning List
        if (currentClassObj.getSuperclass() != null && searchList.contains(currentClassObj.getSuperclass())) {
            matchingClasses.add(classObj);
            matchingClasses.addAll(superClassList);
        }

        return matchingClasses;
    }

    /**
     * Retrieve a List of Classes that extend or implement anything in the search list
     *
     * @param searchTarget   The Super Type Class to look for within the source packages specified
     * @param sourcePackages The root package directories to search within
     * @return The List of found classes from the search
     */
    public static List<Class<?>> getClassNamesMatchingSuperType(final Class<?> searchTarget, final String... sourcePackages) {
        return getClassNamesMatchingSuperType(Lists.newArrayList(searchTarget), sourcePackages);
    }

    /**
     * Retrieves a List of all readable Class Names for the active mods
     *
     * @return The list of viewable Mod Class Names
     */
    public static List<String> getModClassNames() {
        List<String> classNames = Lists.newArrayList();
        final File[] mods = new File(ModUtils.modsDir).listFiles();

        if (mods != null) {
            for (File modFile : mods) {
                if (getFileExtension(modFile).equals(".jar")) {
                    try {
                        JarFile jarFile = new JarFile(modFile.getAbsolutePath());
                        Enumeration<?> allEntries = jarFile.entries();
                        while (allEntries.hasMoreElements()) {
                            JarEntry entry = (JarEntry) allEntries.nextElement();
                            String file = entry.getName();
                            if (file.endsWith(".class")) {
                                String className = file.replace('/', '.').substring(0, file.length() - 6);
                                classNames.add(className);
                            }
                        }
                        jarFile.close();
                    } catch (Exception ignored) {
                    }
                }
            }
            return classNames;
        } else {
            return Lists.newArrayList();
        }
    }
}
