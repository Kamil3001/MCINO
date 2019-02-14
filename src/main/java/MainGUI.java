import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import utils.Setup;
import java.util.Scanner;

public class MainGUI {

    public static void main(String args[]) {
        Setup setup = new Setup(getPathFromUser());
        CompilationUnit[] cUnit = setup.run();
        String[] fileNames = setup.getFileNames();

        FileMetrics classMetricsclass = new FileMetrics(cUnit[3]);
        System.out.println("------------------");
        System.out.println(classMetricsclass.getClassNames());
        System.out.println(classMetricsclass.getClassLengths());
        System.out.println(classMetricsclass.getNumOfFields());
        System.out.println(classMetricsclass.getNumOfPublicFields());
        System.out.println(classMetricsclass.getNumOfMethods());
        System.out.println(classMetricsclass.getNumOfPublicMethods());

        /*for(CompilationUnit cu : cUnit) {
            for (TypeDeclaration t : cu.getTypes()) {
                List<MethodDeclaration> mds = t.getMethods();
                for(MethodDeclaration md : mds){
                    System.out.println(md.getName());
                }
                //System.out.println(t.getName());
            }
        }*/
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
