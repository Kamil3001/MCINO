import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup test = new Setup(getDirPath());
        ArrayList<String> classNames = test.getClassNames();
        System.out.println("The classes which were found are :" + classNames);

        Class[] components = new Class[classNames.size()];

        int index = 0;
        for(String cls: classNames)
        {
            components[index] = test.instantiateClass(cls);
            index++;
        }

        System.out.println(Arrays.toString(components));

        for(Class clazz: components)
        {
            if(clazz.getDeclaredMethods().length == 0)
            {
               System.out.println("Useless component: " + clazz.getName() + " is an interface with no methods");
            }
            else
                System.out.println(Arrays.toString(clazz.getDeclaredMethods()));
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
