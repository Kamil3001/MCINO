import metrics.Metrics;
import utils.Setup;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup setup = new Setup(getPathFromUser());
        Class[] classes = setup.run();


        for(Class c : classes){

                Metrics m = new Metrics(c);
                System.out.println("Name: " + c.getName());
                System.out.println("Number of fields: " + m.getNumOfFields());
                System.out.println("Number of public fields: " + m.getNumOfPublicFields());
                System.out.println("Number of methods: " + m.getNumOfMethods());
                System.out.println("Number of public methods: " + m.getNumOfPublicMethods());
                System.out.println("---------------");
                System.out.println();
        }
    }

    private static String getPathFromUser(){
        Scanner sc = new Scanner(System.in);

        System.out.print("Input path of directory or type 'self' to perform self analysis: ");
        String path = sc.nextLine();

        if(path.equalsIgnoreCase("self")){
            path = System.getProperty("user.dir");
        }

        sc.close();
        return path;
    }
}
