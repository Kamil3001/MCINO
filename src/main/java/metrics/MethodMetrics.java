package metrics;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodMetrics {
    private int numOfParams;
    private int numOfLines;

    MethodMetrics(MethodDeclaration md){
        computeMetrics(md);
    }

    //Exracting number of lines and parameters of the method from MethodDeclaration
    private void computeMetrics(MethodDeclaration md){
        String body = md.getBody().toString();
        numOfLines = body.length() - body.replace("\n", "").length();

        numOfParams = md.getParameters().size();
    }

    public int getNumOfLines() {
        return numOfLines;
    }

    public int getNumOfParams() {
        return numOfParams;
    }
}