package visitors;


import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassLengthVisitor extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<Integer> arg) {
        super.visit(n, arg);
        String body = n.toString();
        arg.add(body.length() - body.replace("\n", "").length());
    }
}
