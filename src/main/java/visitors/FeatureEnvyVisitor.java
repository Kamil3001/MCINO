package visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FeatureEnvyVisitor extends VoidVisitorAdapter<Object> {

    private int count = 0;

    public FeatureEnvyVisitor(MethodDeclaration md){
        visit(md, new Object());
    }

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        super.visit(n, arg);
        if(!n.toString().startsWith("this.") && !n.toString().startsWith("System.")){
            count++;
        }
    }

    public int getNumOfFieldAccesses(){
        return count;
    }
}
