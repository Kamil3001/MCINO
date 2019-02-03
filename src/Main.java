import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {


        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup test = new Setup(getPathFromUser());
        ArrayList<String> classNames = test.getClassNames();

        Class[] instances = new Class[classNames.size()];

        int index = 0;
        for (String cls : classNames) {
            instances[index] = test.instantiateClass(cls);
            index++;
        }

        System.out.println(Arrays.toString(instances));


    }

    public static String getPathFromUser(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Path: ");
        String path = sc.nextLine();
        sc.close();
        return path;
    }
}
