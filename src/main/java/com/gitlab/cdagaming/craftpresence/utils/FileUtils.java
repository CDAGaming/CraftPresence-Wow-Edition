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
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
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
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.guava.ClassPath;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.InputStream;
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

            final InputStream stream = UrlUtils.getURLStream(url);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file);
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
     * @return The List of found class names from the search
     */
    public static List<Class<?>> getClassNamesMatchingSuperType(final List<Class<?>> searchList, final boolean includeExtraClasses, final String... sourcePackages) {
        final List<Class<?>> matchingClasses = Lists.newArrayList();
        final List<ClassPath.ClassInfo> classList = Lists.newArrayList();
        final List<String> sourceData = Lists.newArrayList(sourcePackages);

        if (includeExtraClasses) {
            sourceData.addAll(getModClassNames());
        }

        // Attempt to get all possible classes from the JVM Class Loader
        try {
            classList.addAll(ClassPath.from(ModUtils.CLASS_LOADER).getAllClasses());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (String startString : sourceData) {
            boolean found = false;
            Class<?> loadedInstance = null;

            try {
                for (ClassPath.ClassInfo classInfo : classList) {
                    // Attempt to Add Classes Matching any of the Source Packages
                    if (classInfo.getName().startsWith(startString)) {
                        found = true;
                        loadedInstance = classInfo.load();
                    }
                }

                if (!found && includeExtraClasses) {
                    final Class<?> extraClass = Class.forName(startString, false, ModUtils.CLASS_LOADER);
                    found = true;
                    loadedInstance = extraClass;
                }
            } catch (Exception | Error ex) {
                if (ModUtils.IS_VERBOSE) {
                    ex.printStackTrace();
                }
            } finally {
                if (found && loadedInstance != null) {
                    Pair<Boolean, List<Class<?>>> subClassData = new Pair<>(false, Lists.newArrayList());

                    for (Class<?> searchClass : searchList) {
                        subClassData = isSubclassOf(loadedInstance, searchClass, subClassData.getSecond());

                        if (subClassData.getFirst()) {
                            // If superclass data was found, add the scanned classes
                            // as well as the original class
                            matchingClasses.add(loadedInstance);
                            matchingClasses.addAll(subClassData.getSecond());

                            break;
                        } else {
                            // If no superclass data found, reset for next data
                            subClassData = new Pair<>(false, Lists.newArrayList());
                        }
                    }
                }
            }
        }

        return matchingClasses;
    }

    /**
     * Retrieves sub/super class data for the specified data
     *
     * @param clazz The original class to scan for the specified sub/super-class
     * @param superClass The sub/super-class target to locate
     * @param scannedClasses The class hierarchy of scanned data
     * @return A pair with the format of isSubclassOf:scannedClasses
     */
    protected static Pair<Boolean, List<Class<?>>> isSubclassOf(Class<?> clazz, Class<?> superClass, List<Class<?>> scannedClasses) {
        if (superClass.equals(Object.class)) {
            // Every class is an Object.
            return new Pair<>(true, scannedClasses);
        }
        if (clazz.equals(superClass)) {
            return new Pair<>(true, scannedClasses);
        } else {
            clazz = clazz.getSuperclass();
            // every class is Object, but superClass is below Object
            if (clazz == null || clazz.equals(Object.class)) {
                // we've reached the top of the hierarchy, but superClass couldn't be found.
                return new Pair<>(false, scannedClasses);
            }
            // try the next level up the hierarchy and add this class to scanned history.
            scannedClasses.add(clazz);
            return isSubclassOf(clazz, superClass, scannedClasses);
        }
    }

    /**
     * Retrieve a List of Classes that extend or implement anything in the search list
     *
     * @param searchTarget   The Super Type Class to look for within the source packages specified
     * @param sourcePackages The root package directories to search within
     * @return The List of found classes from the search
     */
    public static List<Class<?>> getClassNamesMatchingSuperType(final Class<?> searchTarget, final boolean includeExtraClasses, final String... sourcePackages) {
        return getClassNamesMatchingSuperType(Lists.newArrayList(searchTarget), includeExtraClasses, sourcePackages);
    }

    /**
     * Retrieves a List of all readable Class Names for the active mods
     *
     * @return The list of viewable Mod Class Names
     */
    public static List<String> getModClassNames() {
        final List<String> classNames = Lists.newArrayList();
        final File[] mods = new File(ModUtils.modsDir).listFiles();

        if (mods != null) {
            for (File modFile : mods) {
                if (getFileExtension(modFile).equals(".jar")) {
                    try {
                        final JarFile jarFile = new JarFile(modFile.getAbsolutePath());
                        final Enumeration<JarEntry> allEntries = jarFile.entries();
                        while (allEntries.hasMoreElements()) {
                            final JarEntry entry = allEntries.nextElement();
                            final String file = entry.getName();
                            if (file.endsWith(".class")) {
                                final String className = file.replace('/', '.').substring(0, file.length() - 6);
                                classNames.add(className);
                            }
                        }
                        jarFile.close();
                    } catch (Exception | Error ex) {
                        if (ModUtils.IS_VERBOSE) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            return classNames;
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * Attempts to Retrieve the Specified Resource as an InputStream
     *
     * @param fallbackClass Alternative Class Loader to Use to Locate the Resource
     * @param pathToSearch  The File Path to search for
     * @return The InputStream for the specified resource, if successful
     */
    public static InputStream getResourceAsStream(final Class<?> fallbackClass, final String pathToSearch) {
        InputStream in = null;
        boolean useFallback = false;

        try {
            in = ModUtils.CLASS_LOADER.getResourceAsStream(pathToSearch);
        } catch (Exception ex) {
            useFallback = true;
        }

        if (useFallback || in == null) {
            in = fallbackClass.getResourceAsStream(pathToSearch);
        }
        return in;
    }
}
