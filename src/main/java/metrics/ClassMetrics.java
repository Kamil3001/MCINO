package metrics;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import visitors.FieldCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassMetrics {
    private CompilationUnit cu;
    private int classLength;
    private int numOfFields;
    private int numOfPublicFields;
    private int numOfMethods;
    private int numOfPublicMethods;
    private HashMap<String, MethodMetrics> methodsMetrics;

    public ClassMetrics(CompilationUnit cu){
        this.cu = cu;
        methodsMetrics = new HashMap<>();
        extractMetrics();
    }

    private void extractMetrics(){
        extractClassLength();
        extractNumOfFields();
        extractMethodsMetrics();
    }

    private void extractClassLength(){
        classLength = cu.getTokenRange().get().getEnd().getRange().get().begin.line;
    }

    private void extractNumOfFields(){
        List<FieldDeclaration> fields = new ArrayList<>();
        VoidVisitor<List<FieldDeclaration>> fieldCollector = new FieldCollector();
        fieldCollector.visit(cu, fields);

        for(FieldDeclaration f : fields){
            numOfFields++;
            if(f.isPublic())
                numOfPublicFields++;
        }
    }

    private void extractMethodsMetrics(){
        for(TypeDeclaration td : cu.getTypes()){
            List<MethodDeclaration> mds = td.getMethods();

            for(MethodDeclaration md : mds){
                numOfMethods++;
                if(md.isPublic())
                    numOfPublicMethods++;

                String methodName = md.getName().toString();
                methodsMetrics.put(methodName, new MethodMetrics(md));
            }
        }
    }


    /* GETTERS */
    public int getClassLength() {
        return classLength;
    }

    public int getNumOfFields() {
        return numOfFields;
    }

    public int getNumOfPublicFields() {
        return numOfPublicFields;
    }

    public int getNumOfMethods() {
        return numOfMethods;
    }

    public int getNumOfPublicMethods() {
        return numOfPublicMethods;
    }

    public HashMap<String, MethodMetrics> getMethodsMetrics() {
        return methodsMetrics;
    }

    public CompilationUnit getCompilationUnit() {
        return cu;
    }
}
