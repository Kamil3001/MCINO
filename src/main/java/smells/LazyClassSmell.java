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

    //Look for all lazy classes upon instantiation
    public LazyClassSmell(FileMetrics[] allMetrics){
        lazyClasses = new ArrayList<>();
        this.allMetrics = allMetrics;
        findLazyClasses();
    }

    //todo try and fix UI and test classes being seen as Lazy
    private void findLazyClasses(){
        Set<String> projectClassSet = new HashSet<>();
        Set<String> superClassSet = new HashSet<>();
        Set<String> associateClassSet = new HashSet<>();
        Set<String> dependentClassSet = new HashSet<>();

        for(FileMetrics fm : allMetrics) {
            projectClassSet.addAll(fm.getClassNames());

            for(ClassOrInterfaceType cit : fm.getImplementedTypes()){
                if(cit.getNameAsString().equals("Initializable")){
                    associateClassSet.addAll(fm.getClassNames());
                }
            }

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

    //this method simply returns severity 3 if class is deemed to be Lazy Class and 0 otherwise
    //Using the lazyClasses hashmap to check if a given class is contained in the set
    @Override
    public void detectSmell(FileMetrics metrics) {
        severity = 0;
        for(String className : metrics.getClassNames()){
            if(lazyClasses.contains(className)){
                severity = 3;
                break;
            }
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
