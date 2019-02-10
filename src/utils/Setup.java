package utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class Setup {

    private ArrayList<String> classNames;
    private String classDir;
    private Class[] classes;

    public Setup(String dirPath){
        this.classNames = new ArrayList<>();
        findClassNames(dirPath);
        classes = new Class[classNames.size()];
    }


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return ""; // empty extension
        }

        return name.substring(lastIndexOf);
    }

    private void findClassNames(String smellyCodeDir) {
        if(getDirectory(smellyCodeDir)){
            getFiles();
        }
    }

    private void getFiles() {
        File directory = new File(this.classDir);
        File[] fileList = directory.listFiles();

        for (File file : fileList) {
            if (getFileExtension(file).equals(".class")) {
                String className = file.getName().substring(0, file.getName().indexOf(".")); //removing .class extension from file name
                this.classNames.add(className);
            }
        }
    }

    private boolean getDirectory(String directoryName)
    {
        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();

        boolean found = false;

        int i = 0;

        if(fileList.length > 0) {
            while (i < fileList.length && !found) {
                if (fileList[i].isDirectory()) {
                    found = getDirectory(fileList[i].getAbsolutePath());
                }
                if(getFileExtension(fileList[i]).equals(".class"))
                {
                    this.classDir = fileList[i].getAbsolutePath().substring(0,fileList[i].getAbsolutePath().indexOf(fileList[i].getName()));
                    found = true;
                }
                i++;
            }
            return found;
        }
        return false;
    }


    private Class instantiateClass(String className) {

        try {
            //convert the folder to URL format
            File folder = new File(this.classDir);
            URL url = folder.toURI().toURL();
            URL[] urls = new URL[]{url};

            URLClassLoader cl = new URLClassLoader(urls);
            Constructor<?>[] constructor = cl.loadClass(className).getConstructors();

            if(constructor.length > 0) {
                Constructor<?> c = constructor[0];
                Class<?>[] types = c.getParameterTypes();
                Object[] arguments = new Object[c.getParameterCount()];

                int index = 0;
                // Declaring variables required for the constructor
                for (Class type : types) {
                    if (type.isPrimitive()) {
                        arguments[index] = 0;
                    }
                    else
                        arguments[index] = null;
                    index++;
                }

                Class cls = c.newInstance(arguments).getClass();
                System.out.println(className + " has been instantiated successfully.");

                return cls;
            }
            else
            {
                System.out.println(className + " has been instantiated successfully.");
                return cl.loadClass(className);
            }

        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println(className + " is not a valid class name");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("Could not create new instance of class: " + className);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class[] run()
    {
        int index = 0;
        for(String clazz: classNames)
        {
            classes[index] = instantiateClass(clazz);
            System.out.println("Name: " + classes[index].getName());
            index++;
        }

        return this.classes;
    }

    public ArrayList<String> getClassNames()
    {
        return this.classNames;
    }

    public String getClassDir()
    {
        return this.classDir;
    }
}