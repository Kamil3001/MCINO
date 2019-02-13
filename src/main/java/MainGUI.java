import com.github.javaparser.ast.CompilationUnit;
import utils.Setup;

import java.util.Scanner;

public class MainGUI {

    public static void main(String args[]) {
        Setup setup = new Setup(getPathFromUser());

        CompilationUnit[] cUnit = setup.run();
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
