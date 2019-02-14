package visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassLengthVisitor extends VoidVisitorAdapter<Integer> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Integer arg) {
        super.visit(n, arg);
        arg = n.toString().length();
    }
}
