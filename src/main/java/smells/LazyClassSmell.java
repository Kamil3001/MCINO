package smells;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import metrics.FileMetrics;
import metrics.MethodMetrics;
import visitors.MethodDependentVarsVisitor;

import java.util.*;

/*
Class is not lazy if:
- It's a super class to at least one other class
- It's an Associate Class (i.e. as a field)
- It's depended upon by methods of other class

ONLY severity 3 or 0 makes sense as class can't be kind of lazy
 */
public class LazyClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Lazy Class";
    private List<String> lazyClasses;
    private FileMetrics[] allMetrics;

    public LazyClassSmell(FileMetrics[] allMetrics){
        lazyClasses = new ArrayList<>();
        this.allMetrics = allMetrics;
        findLazyClasses();
    }

    //todo UI & test classes seen as lazy
    private void findLazyClasses(){
        Set<String> projectClassSet = new HashSet<>();
        Set<String> superClassSet = new HashSet<>();
        Set<String> associateClassSet = new HashSet<>();
        Set<String> dependentClassSet = new HashSet<>();
        for(FileMetrics fm : allMetrics) {
            projectClassSet.addAll(fm.getClassNames());

            for (ClassOrInterfaceType cit : fm.getExtendedTypes()) {
                superClassSet.add(cit.asString());
            }

            for (FieldDeclaration field : fm.getFields()) {
                if (!field.getElementType().isPrimitiveType())
                    associateClassSet.add(field.getElementType().asString());
            }

            for (Map.Entry<String, MethodMetrics> entry : fm.getMethodsMetrics().entrySet()) {
                if(entry.getValue().getMethodDeclaration().getNameAsString().equals("main")){
                    dependentClassSet.addAll(fm.getClassNames()); //main method file isnt lazy obviously
                }
                MethodDependentVarsVisitor visitor = new MethodDependentVarsVisitor(fm.getCompilationUnit());
                dependentClassSet.addAll(visitor.getTypes());
            }
        }
        projectClassSet.removeAll(superClassSet);
        projectClassSet.removeAll(associateClassSet);
        projectClassSet.removeAll(dependentClassSet);
        lazyClasses.addAll(projectClassSet);
    }

    @Override
    public void detectSmell(FileMetrics metrics) {
        for(String className : metrics.getClassNames()){
            if(lazyClasses.contains(className)){
                severity = 3;
                System.out.println("3: " + metrics.getClassNames().get(0));
                break;
            }
        }
        System.out.println("0: " + metrics.getClassNames().get(0));
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
