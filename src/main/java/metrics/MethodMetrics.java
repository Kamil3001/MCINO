package metrics;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;


public class MethodMetrics {
    private int numOfParams;
    private int numOfLines;
    private int modifiers;
    private MethodDeclaration md;

    MethodMetrics(MethodDeclaration md){
        this.md = md;
        computeMetrics(md);
    }

    //Extracting number of lines and parameters of the method from MethodDeclaration
    private void computeMetrics(MethodDeclaration md){
        if(md.getBody().isPresent()){
            BlockStmt body = md.getBody().get();
            numOfLines = body.toString().length() - body.toString().replace("\n", "").length();

        }
        else{
            //Abstract method
            modifiers += 1024;
            numOfLines = 0;
        }
        numOfParams = md.getParameters().size();
    }

    public int getNumOfLines() {
        return numOfLines;
    }

    public int getNumOfParams() {
        return numOfParams;
    }

    public int getModifiers() {
        return modifiers;
    }

    public MethodDeclaration getMethodDeclaration() { return md; }
}