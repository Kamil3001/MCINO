
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

public class Setup {

    private File folder;
    Setup(String classDir){ folder = new File(classDir); }


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public ArrayList<String> getClassNames() {

        if(folder.isDirectory())
        {
            File[] files = folder.listFiles();
            ArrayList<String> classNames = new ArrayList<>();

            for(File f: files)
            {
                if(getFileExtension(f).equals(".class"))
                {
                    String className = f.getName().substring(0,f.getName().indexOf(".")); //removing .java extension from file name
                    classNames.add(className);
                }
            }
            System.out.println("Given directory has the following classes: " + classNames);
            return classNames;
        }
        else{
            try {
                throw new NotDirectoryException("Given directory does not exists");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public Class instantiateClass(String className) {
        try {

            //convert the folder to URL format
            URL url = folder.toURI().toURL();
            URL[] urls = new URL[]{url};

            URLClassLoader cl = new URLClassLoader(urls);

            //load the current class, and execute constructor
            Constructor<?>[] constructor = cl.loadClass(className).getConstructors();
            if (constructor.length > 0) {
                Constructor<?> c = constructor[0];
                Class<?>[] types = c.getParameterTypes();
                Object[] arguments = new Object[c.getParameterCount()];

                int index = 0;
                // Declaring variables required for the constructor
                for (Class type : types) {
                    if (type.isPrimitive()) {
                        arguments[index] = 0;
                    } else
                        arguments[index] = null;
                    index++;
                }

                System.out.println(className + " has been instantiated successfully.");

                return c.newInstance(arguments).getClass();
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


   /* public Class instantiateClass(String className) {

        try {
            Constructor<?>[] constructor = Class.forName(className).getConstructors();
            if(constructor.length>0) {
                Constructor<?> c = constructor[0];
                Class<?>[] types = c.getParameterTypes();
                Object[] arguments = new Object[c.getParameterCount()];

                int index = 0;
                // Declaring variables required for the constructor
                for (Class type : types) {
                    if (type.isPrimitive()) {
                        arguments[index] = 0;
                    } else
                        arguments[index] = null;
                    index++;
                }

                Class cls = c.newInstance(arguments).getClass();
                System.out.println(className + " has been instantiated successfully.");

                return cls;
            }
            else{
                System.out.println(className + " is not a class, skipping instantiation..");
            }
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println(className + " is not a valid class name");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("Could not create new instance of class: " + className);
        }
        return null;
    }*/
}
