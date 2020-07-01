package net.minecraft.launchwrapper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderUtils {
    public static LaunchClassLoader getClassLoader(ClassLoader parent) {
        if (parent instanceof URLClassLoader) {
            final URLClassLoader ucl = (URLClassLoader) parent;
            return new LaunchClassLoader(ucl.getURLs());
        } else {
            return new LaunchClassLoader(getURLs());
        }
    }

    public static URL[] getURLs() {
        String cp = System.getProperty("java.class.path");
        String[] elements = cp.split(File.pathSeparator);
        if (elements.length == 0) {
            elements = new String[]{""};
        }
        URL[] urls = new URL[elements.length];
        for (int i = 0; i < elements.length; i++) {
            try {
                URL url = new File(elements[i]).toURI().toURL();
                urls[i] = url;
            } catch (MalformedURLException ignore) {
                // malformed file string or class path element does not exist
            }
        }
        return urls;
    }
}
