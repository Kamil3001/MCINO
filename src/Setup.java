
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

public class Setup {

    private String smellyCodeDir;
    private ArrayList<String> classNames;
    private String classDir;

    Setup(String smellyCodeDir){
        this.smellyCodeDir = smellyCodeDir;
        this.classNames = new ArrayList<>();
    }


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    private void getFiles(String directoryName)
    {
        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();
           if(fileList != null) {
               for (File file : fileList) {
                   if (file.isFile() && getFileExtension(file).equals(".class")) {
                       this.classDir = file.getAbsolutePath().substring(0,file.getAbsolutePath().indexOf(file.getName()));
                       String className = file.getName().substring(0, file.getName().indexOf(".")); //removing .java extension from file name
                       this.classNames.add(className);
                   } else if (file.isDirectory()) {
                       getFiles(file.getAbsolutePath());
                   }
               }

           }
    }


    public ArrayList<String> getClassNames(){
        getFiles(this.smellyCodeDir);
        return this.classNames;
    }


   public Class instantiateClass(String className) {

        try {
            //convert the folder to URL format
            File folder = new File(this.classDir);
            URL url = folder.toURI().toURL();
            URL[] urls = new URL[]{url};

            URLClassLoader cl = new URLClassLoader(urls);
            Constructor<?>[] constructor = cl.loadClass(className).getConstructors();

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
            else if(cl.loadClass(className).isInterface())
            {
                System.out.println(className + " is an interface, cannot be instantiated.");
                return Class.forName(className);
            }
            else{
                System.out.println(className + " is not a class, skipping instantiation..");
            }
        }catch(ClassNotFoundException e)
        {
            //e.printStackTrace();
            System.out.println(className + " is not a valid class name");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("Could not create new instance of class: " + className);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       return null;
    }

}
