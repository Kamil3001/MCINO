package metrics;

import com.github.javaparser.ast.CompilationUnit;

import java.util.HashMap;

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
        //TODO
    }

    private void extractClassLength(){
        //TODO
    }

    private void extractNumOfFields(){
        //TODO
    }

    private void extractMethodsMetrics(){
        //TODO
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
}
