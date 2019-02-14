package metrics;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodMetrics {
    private int numOfParams;
    private int numOfLines;
    private MethodDeclaration md;

    MethodMetrics(MethodDeclaration md){
        this.md = md;
        computeMetrics();
    }

    private void computeMetrics(){
        numOfLines = md.getTokenRange().get().getEnd().getRange().get().begin.line;
        numOfParams = md.getParameters().size();
    }

    public int getNumOfLines() {
        return numOfLines;
    }

    public int getNumOfParams() {
        return numOfParams;
    }
}
