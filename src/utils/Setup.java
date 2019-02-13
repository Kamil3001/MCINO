package utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

public class Setup {

    private ArrayList<String> classNames;
    private String dirPath;
    private String classDir;
    private Class[] classes;
    private File[] files;
    private URL[] urls = new URL[7];
    private int ind = 0;


    public Setup(String dirPath) throws MalformedURLException {
        this.classNames = new ArrayList<>();
        this.dirPath = dirPath;
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

    private void findClassNames(String smellyCodeDir) throws MalformedURLException {
        if(getDirectory(smellyCodeDir)){
            getFiles();
            for(File f: this.files)
            {
                if(f.isDirectory()) {
                    findClassNames(f.getAbsolutePath());

                }
            }
        }
    }

    private void getFiles() throws MalformedURLException {
        File directory = new File(this.classDir);
        this.files = directory.listFiles();

        for (File file : this.files) {
            if (isClass(file)) {
                urls[ind] = file.getParentFile().toURI().toURL();
                ind++;
                String className = file.getName().substring(0, file.getName().indexOf(".")); //removing .class extension from file name
                this.classNames.add(className);
            }
        }
    }

    private boolean isClass(File file)
    {
        return getFileExtension(file).equals(".class");
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
                else if(isClass(fileList[i]))
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
            URLClassLoader cl = new URLClassLoader(this.urls,Thread.currentThread().getContextClassLoader());
            String dir = cl.findResource(className+".class").toString();
            String pckg = dir.substring(0,dir.lastIndexOf("/"));
            pckg = pckg.substring(pckg.lastIndexOf("/")+1);

            String userDir = this.dirPath;
            String project = userDir.substring(userDir.lastIndexOf("\\")+1);


            Constructor<?>[] constructor;
            if(pckg.equals(project))
            {
                constructor = cl.loadClass(className).getDeclaredConstructors();

            }
            else {
                constructor = cl.loadClass(pckg + "." + className).getDeclaredConstructors();
            }

            if(constructor.length > 1) {
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
                // If its main or an abstract class
                System.out.println(className + " has been instantiated successfully.");

                if(pckg.equals(project))
                {
                    return cl.loadClass(className);

                }else
                    return cl.loadClass(pckg+"."+className);
            }

        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println(className + " is not a valid class name");
        } catch (IllegalAccessException  e){
            e.printStackTrace();
            System.out.println("Could not create new instance of class: " + className);
        } catch (InvocationTargetException e) {
            e.getTargetException();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class[] run()
    {
        System.out.println("\nClasses found in " + this.dirPath + ":");
        System.out.println(classNames + "\n");
        int index = 0;
        for(String clazz: classNames)
        {
            classes[index] = instantiateClass(clazz);
            index++;
        }

        System.out.println();


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