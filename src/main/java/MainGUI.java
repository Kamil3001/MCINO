import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import utils.Setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainGUI {

    public static void main(String args[]) {
        Setup setup = new Setup(getPathFromUser());
        CompilationUnit[] cUnit = setup.run();
        String[] fileNames = setup.getFileNames();

        for(CompilationUnit cu : cUnit) {
            for (TypeDeclaration t : cu.getTypes()) {
                List<MethodDeclaration> mds = t.getMethods();
                for(MethodDeclaration md : mds){
                    System.out.println(md.getName());
                }
                //System.out.println(t.getName());
            }
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
