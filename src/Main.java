import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup test = new Setup("D:\\University\\Stage 3\\Semester 2\\Software Engineering\\Assignment1\\");
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
}
