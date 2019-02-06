import java.util.ArrayList;
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

    }

    private static String getDirPath(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Input path of directory:");
        String path = sc.nextLine();

        sc.close();
        return path;
    }
}

