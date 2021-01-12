package main.java;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * JAVA进阶训练营1期 3班
 * @author YanKaiFei
 * @date 20210110
 */
public class MyHelloClassLoad extends ClassLoader{

    public static void main(String[] args) {
        FileInputStream fileInputStream = null;
        File file = null;
        byte[] b = new byte[0];
        try {
        //s1
//            System.out.println(System.getProperty("user.dir"));
            String dir = "D:\\git-works\\JAVA-01_Y\\Hello\\Hello.xlass";
            String dir1 = MyHelloClassLoad.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            file = new File(dir);
            String fileName = file.getName().substring(0, file.getName().indexOf("."));
            fileInputStream = new FileInputStream(file);
            b = new byte[fileInputStream.available()];

            fileInputStream.read(b);
            fileInputStream.close();

            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) (255- b[i]);
            }
            Class clazz = new MyHelloClassLoad().myDefineClass(fileName, b);
            Method method = clazz.getMethod("hello");
            method.invoke(clazz.newInstance());
            //s2
            MyHelloClassLoad classLoad = new MyHelloClassLoad(dir1);
            Class klazz = classLoad.myLoadClass("Hello", true);
            Method hello2 = klazz.getMethod("hello");
            hello2.invoke(klazz.newInstance());

            //s3
            dir1 = dir1 + "main/java";
            URLClassLoader classLoader = (URLClassLoader) MyHelloClassLoad.class.getClassLoader();
            Method uRLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            uRLMethod.setAccessible(true);
            uRLMethod.invoke(classLoader, new File(dir1).toURL());

            Class klass = Class.forName("Hello", true, classLoader);
            Object obj = klass.newInstance();
            Method hello = klass.getMethod("hello");
            hello.invoke(obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String classPath;
    private  String filesux = ".xlass";//文件后缀
    public MyHelloClassLoad() {
    }

    public MyHelloClassLoad(String classPath) {
        this.classPath = classPath;
    }


    private byte[] loadByte(String fileName) throws IOException {
        fileName = fileName.replaceAll("\\.", "/");
        String filePath = "";
        File file = new File(classPath);
        boolean pass = true;
        int index = 0;
        while (pass) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    file = files[i];
                } else {
                    if (file1.getName().equals(fileName + filesux)) {
                        filePath = file1.getAbsolutePath();
                    }
                    pass = false;
                }
            }
        }

        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] b = new byte[fileInputStream.available()];

        fileInputStream.read(b);

        fileInputStream.close();
        return b;
    }

    protected final Class<?> myLoadClass(String name, boolean decode) {
        byte[] b = new byte[0];
        try {
            b = loadByte(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (decode) {
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) (255- b[i]);
            }
        }
        return defineClass(name, b, 0, b.length);
    }


    protected final Class<?> myDefineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
