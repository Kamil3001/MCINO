import metrics.Metrics;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup setup = new Setup(getPathFromUser());
        Class[] classes = setup.run();

        for(Class c : classes){
            Metrics m = new Metrics(c);
            System.out.println(c.getName());
            System.out.println(m.getNumOfFields());
            System.out.println(m.getNumOfPublicFields());
            System.out.println(m.getNumOfMethods());
            System.out.println(m.getNumOfPublicMethods());
            System.out.println("---------------");
            System.out.println();
        }

        System.out.println("The .class files found are :" + setup.getClassNames() + "\n");

    }

    private static String getPathFromUser(){
        Scanner sc = new Scanner(System.in);

        System.out.print("Input path of directory or type 'self' to perform self analysis: ");
        String path = sc.nextLine();

        if(path.equalsIgnoreCase("self")){
            path = "./";
        }

        sc.close();
        return path;
    }
}
