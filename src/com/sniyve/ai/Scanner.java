package com.sniyve.ai;

import android.content.Context;
import android.util.Log;

import com.sniyve.ai.annotation.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * 扫描Controller
 */
public class Scanner {

    private static Field dexField;

    public static List<Class<?>> scan(Context context,String basePath) {
        List<Class<?>> results = new ArrayList<Class<?>>();
        try {

            String path = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), 0).sourceDir;
            DexFile dexfile = new DexFile(path);
            Enumeration<String> entries = dexfile.entries();
            PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if(!name.startsWith(basePath))
                    continue;
                Log.v("info","AppInterface扫描Controller:"+name);
                Class<?> entryClass = dexfile.loadClass(name, classLoader);
                if (entryClass != null) {
                    Controller annotation = entryClass.getAnnotation(Controller.class);
                    if (annotation != null ) {
                        results.add(entryClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}  