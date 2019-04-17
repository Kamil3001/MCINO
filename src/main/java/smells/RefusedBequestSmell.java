package smells;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;
import visitors.MethodCallVisitor;

import java.util.Map;


/* When inheritance doesn't make sense, i.e. superclass and subclass
are completely different */
public class RefusedBequestSmell extends AbstractCodeSmell {
    private final static String smellName = "Refused Bequest";
    private final FileMetrics[] allMetrics;
    private CompilationUnit[] CUs;


    public RefusedBequestSmell(CompilationUnit[] CUs, FileMetrics[] allMetrics){
        this.CUs = CUs;
        this.allMetrics = allMetrics;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {
        /*
        if class doesn't inherit from anything refused bequest isn't a problem
        and if a class extends an abstract class or implements interfaces it is assumed that the goal is polymorphism and thus
        refused bequest is not a problem in this case either
         */
        if((!metrics.getExtendedTypes().isEmpty() || !metrics.getImplementedTypes().isEmpty()) && !extendsAbstractOrInterface(metrics)){
            int numOfOverrides = 0;
            boolean someMethodsUsed = false;

            //for each method check if it overrides superclass methods and if they are used throughout the code
            for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
                MethodDeclaration md = entry.getValue().getMethodDeclaration();
                if(md.isAnnotationPresent("Override")) //check for overrides
                    numOfOverrides++;

                if(!someMethodsUsed && timesMethodUsed(md, metrics) > 3) { //assume that method is used if there are more than 3 uses of method with same name
                    someMethodsUsed = true;
                }
                else{
                    occurrences.add(new Occurrence(entry.getValue().getStartLine(), entry.getValue().getEndLine())); //otherwise assume method is unused
                }
            }

            if(someMethodsUsed){
                if(numOfOverrides == 0)
                    severity = 2;
                else
                    severity = 1; //this severity is likely to pop up as it serves to remind user of their motives
            }
            else if(numOfOverrides == 0){
                severity = 3;
            }
            else{
                severity = 2;
            }

        }
    }

    private boolean extendsAbstractOrInterface(FileMetrics metrics){
        //ugly looking nested loops but their computation is not nearly as large as it appears
        for(CompilationUnit cu : CUs){ //number of files
            for(TypeDeclaration t : cu.getTypes()){ //number of classes within a file (usually 1)
                if(t.isClassOrInterfaceDeclaration()){
                    for(ClassOrInterfaceType c : metrics.getExtendedTypes()){ //if file is a class iteration will be 1, if interface, it can be more
                        if(t.getName().asString().contains(c.getName().asString())){
                            if(((ClassOrInterfaceDeclaration)t).isAbstract() || ((ClassOrInterfaceDeclaration)t).isInterface())
                                return true;
                        }
                    }
                    for(ClassOrInterfaceType c : metrics.getImplementedTypes()){ //if file is a class iteration will be 1, if interface, it can be more
                        if(t.getName().asString().contains(c.getName().asString())){
                            if(((ClassOrInterfaceDeclaration)t).isAbstract() || ((ClassOrInterfaceDeclaration)t).isInterface())
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //this method returns the use of a method with same name so not necessarily confirming it
    //Assume high usageCount implies at least one usage of our method
    private int timesMethodUsed(MethodDeclaration md, FileMetrics metrics){
        MethodCallVisitor visitor;
        int usageCount = 0;
        for(FileMetrics f : allMetrics){
            if(f == metrics)
                continue;

            for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
                entry.getValue().getMethodDeclaration().accept((visitor = new MethodCallVisitor(md.getNameAsString())), null);
                usageCount += visitor.getCount();
            }
        }

        return usageCount;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
