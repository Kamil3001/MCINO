import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup test = new Setup(getDirPath());
        ArrayList<String> classNames = test.getClassNames();
        System.out.println("The .class files found are :" + classNames + "\n");

        Class[] components = new Class[classNames.size()];

        int index = 0;
        for(String cls: classNames)
        {
            components[index] = test.instantiateClass(cls);
            System.out.println("Name: " + components[index].getName());

            String methods = "[";
            for(Method m : components[index].getDeclaredMethods())
                methods += m.getName() + ", ";

            if(methods.equals("["))
                methods += "]";
            else{
                methods = methods.substring(0, methods.length()-2);
                methods += "]";
            }

            System.out.println("Methods: " + methods + "\n");
            index++;
        }

    }

    private static String getDirPath(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Input path of directory:");
        String path = sc.nextLine();

        sc.close();
        return path;
    }
}

