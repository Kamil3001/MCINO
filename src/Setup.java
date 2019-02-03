import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

public class Setup {

    private String directory;
    public Setup(String dir){ this.directory = dir;}


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public ArrayList<String> getClassNames() {

        File folder = new File(this.directory);
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

    public Class instantiateClass(String className){

        try {
            Constructor<?>[] constructor = Class.forName(className).getConstructors();
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
