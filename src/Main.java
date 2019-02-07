import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        /* CHANGE THE DIRECTORY WHERE YOU WOULD LIKE TO CHECK FOR SMELLY CODE */
        Setup setup = new Setup(getDirPath());
        Class[] classes = setup.run();
        System.out.println("The .class files found are :" + setup.getClassNames() + "\n");

    }

    private static String getDirPath(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Input path of directory:");
        String path = sc.nextLine();

        sc.close();
        return path;
    }
}
