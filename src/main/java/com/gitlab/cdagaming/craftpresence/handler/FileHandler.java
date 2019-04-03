package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileHandler {
    private static Gson GSON = new GsonBuilder().create();

    public static <T> T getJSONFromFile(File file, Class<T> classObj) throws Exception {
        return getJSONFromFile(fileToString(file), classObj);
    }

    public static void downloadFile(final String urlString, final File file) {
        try {
            Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.download.init", file.getName(), file.getAbsolutePath(), urlString));
            final URL url = new URL(urlString);
            if (file.exists()) {
                final boolean fileDeleted = file.delete();
                if (!fileDeleted) {
                    Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.delete.file", file.getName()));
                }
            }

            FileUtils.copyURLToFile(url, file);
            Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.download.loaded", file.getName(), file.getAbsolutePath(), urlString));
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.download", file.getName(), urlString, file.getAbsolutePath()));
            ex.printStackTrace();
        }
    }

    public static void loadFileAsDLL(final File file) {
        try {
            Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.dll.init", file.getName()));
            boolean isPermsSet = file.setReadable(true) && file.setWritable(true);
            if (isPermsSet) {
                System.load(file.getAbsolutePath());
            }
            Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.dll.loaded", file.getName()));
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.dll", file.getName()));
            ex.printStackTrace();
        }
    }

    public static <T> T getJSONFromFile(String file, Class<T> clazz) {
        return GSON.fromJson(file, clazz);
    }

    public static String fileToString(File file) throws Exception {
        return FileUtils.readFileToString(file, Charset.forName("UTF-8"));
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public static int getModCount() {
        int modCount = 0;
        final File[] mods = new File(Constants.modsDir).listFiles();

        if (mods != null) {
            for (File modFile : mods) {
                if (getFileExtension(modFile).equals(".jar")) {
                    modCount++;
                }
            }
        }
        return modCount;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static List<Class> getClassNamesMatchingSuperType(final List<Class> searchList, final String... sourcePackages) {
        final List<Class> matchingClasses = Lists.newArrayList(), availableClassList = Lists.newArrayList();
        final List<ClassPath.ClassInfo> classList = Lists.newArrayList();

        // Attempt to Get Top Level Classes from the JVM Class Loader
        try {
            classList.addAll(ClassPath.from(Constants.CLASS_LOADER).getTopLevelClasses());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (ClassPath.ClassInfo classInfo : classList) {
            for (String startString : sourcePackages) {
                // Attempt to Add Classes Matching any of the Source Packages
                if (classInfo.getName().startsWith(startString) && (!classInfo.getName().contains("FMLServerHandler"))) {
                    try {
                        Class classObj = Class.forName(classInfo.getName());
                        availableClassList.add(classObj);
                        for (Class subClassObj : classObj.getClasses()) {
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

        for (Class classObj : availableClassList) {
            Class currentClassObj = classObj;
            List<Class> superClassList = Lists.newArrayList();

            // Add All SuperClasses of this Class to a List
            while (currentClassObj.getSuperclass() != null && !searchList.contains(currentClassObj.getSuperclass())) {
                superClassList.add(currentClassObj.getSuperclass());
                currentClassObj = currentClassObj.getSuperclass();
            }

            // If Match is Found, add original Class to final List, and add all Super Classes to returning List
            if (currentClassObj.getSuperclass() != null && searchList.contains(currentClassObj.getSuperclass())) {
                matchingClasses.add(classObj);
                matchingClasses.addAll(superClassList);
            }
        }

        // Attempt to Retrieve Mod Classes
        for (String modClassString : getModClassNames()) {
            Class modClassObj, currentClassObj;
            List<Class> superClassList = Lists.newArrayList();

            try {
                modClassObj = Class.forName(modClassString);
                currentClassObj = modClassObj;

                if (modClassObj != null) {
                    // Add all SuperClasses of Mod Class to a List
                    while (currentClassObj.getSuperclass() != null && !searchList.contains(currentClassObj.getSuperclass())) {
                        superClassList.add(currentClassObj.getSuperclass());
                        currentClassObj = currentClassObj.getSuperclass();
                    }

                    // If Match is Found, add original Class to final List, and add all Super Classes to returning List
                    if (currentClassObj.getSuperclass() != null && searchList.contains(currentClassObj.getSuperclass())) {
                        matchingClasses.add(modClassObj);
                        matchingClasses.addAll(superClassList);
                    }
                }
            } catch (Exception ignored) {
                // Ignore this Exception and Continue
            } catch (Error ignored) {
                // Ignore this Error and Continue
            }
        }
        return matchingClasses;
    }

    public static List<Class> getClassNamesMatchingSuperType(final Class searchTarget, final String... sourcePackages) {
        return getClassNamesMatchingSuperType(Collections.singletonList(searchTarget), sourcePackages);
    }

    public static List<String> getModClassNames() {
        List<String> classNames = Lists.newArrayList();
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
                        jarFile.close();
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
