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

    // From https://github.com/MinecraftForge/Installer/blob/fe18a164b5ebb15b5f8f33f6a149cc224f446dc2/src/main/java/net/minecraftforge/installer/actions/PostProcessors.java#L287-L303
    public static ClassLoader getParentClassLoader() {
        if (!System.getProperty("java.version").startsWith("1.")) {
            try {
                return (ClassLoader) ClassLoader.class.getDeclaredMethod("getPlatformClassLoader").invoke(null);
            } catch (Throwable t) {
                LogWrapper.warning("No platform classloader: " + System.getProperty("java.version"));
            }
        }
        return null;
    }
}
