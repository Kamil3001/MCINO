
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

public class Setup {

    private File folder;

    Setup(String smellyCodeDir){
        this.folder = copyDir(smellyCodeDir, System.getProperty("user.dir")+"\\src\\TestSubject");
    }

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
                if(getFileExtension(f).equals(".java"))
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

    private File copyDir(String src, String dest) {

        File srcDir = new File(src);
        File destDir = new File(dest);
        FileFilter filter = pathname -> {
            if(pathname.toString().contains(".java"))
                return true;
           return false;
        };
        try {
            FileUtils.copyDirectory(srcDir, destDir, filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(dest);
    }

   public Class instantiateClass(String className) {

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
            else if(Class.forName(className).isInterface())
            {
                System.out.println(className + " is an interface, cannot be instantiated.");
                return Class.forName(className);
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
    }
}
