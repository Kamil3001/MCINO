package metrics;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;


public class MethodMetrics {
    private int numOfParams;
    private int numOfLines;
    private int modifiers;
    private int startLine;
    private int endLine;
    private MethodDeclaration md;
    private BlockStmt body;

    MethodMetrics(MethodDeclaration md){
        this.md = md;
        computeMetrics(md);
    }

    //Extracting number of lines and parameters of the method from MethodDeclaration
    private void computeMetrics(MethodDeclaration md){
        if(md.getBody().isPresent()){
            startLine = md.getBegin().isPresent() ?  md.getBegin().get().line : -1;
            endLine = md.getEnd().isPresent() ? md.getEnd().get().line : -1;
            body = md.getBody().get();
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

    public BlockStmt getBody() {
        return body;
    }

    public NodeList<Statement> getStatements(){
        return md.getBody().isPresent() ? md.getBody().get().getStatements() : null;
    }

    public int getNumOfStatements() {
        return md.getBody().isPresent() ? md.getBody().get().getStatements().size() : 0;
    }

    public int getNumOfParams() {
        return numOfParams;
    }

    public int getModifiers() {
        return modifiers;
    }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public MethodDeclaration getMethodDeclaration() {
        return md;
    }

    @Override
    public String toString() {
        return body.toString();
    }
}