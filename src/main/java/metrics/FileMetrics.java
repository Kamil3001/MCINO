package metrics;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitor;

import visitors.ClassLengthVisitor;
import visitors.FieldCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileMetrics {
    private CompilationUnit cu;
    private List<String> classNames;
    private List<ClassOrInterfaceDeclaration> subClasses;
    private List<ConstructorDeclaration> classConstructors;
    private List<Integer> classLengths;
    private List<Comment> classComments;
    private int numOfFields;
    private int numOfPublicFields;
    private int numOfMethods;
    private int numOfPublicMethods;
    private HashMap<String, MethodMetrics> methodsMetrics;
    private HashMap<String, List<MethodDeclaration>> subClassMethods;

    public FileMetrics(CompilationUnit cu){
        this.cu = cu;
        methodsMetrics = new HashMap<>();
        subClassMethods = new HashMap<>();
        classNames = new ArrayList<>();
        subClasses = new ArrayList<>();
        classConstructors = new ArrayList<>();
        classLengths = new ArrayList<>();
        classComments = new ArrayList<>();
        extractMetrics();
    }

    private void extractMetrics(){
        extractClassNames();
        extractClassLengths();
        extractNumOfFields();
        extractMethodsMetrics();
        extractComments();
    }

    private void extractClassLengths(){
        VoidVisitor<List<Integer>> classVisitor = new ClassLengthVisitor();
        classVisitor.visit(cu, classLengths);
    }

    private void extractClassNames(){
        for(TypeDeclaration t : cu.getTypes()){
            if(t.isClassOrInterfaceDeclaration()) {
                classNames.add(t.getName().toString());
                // Find Inner Classes
                for (int i = 0; i < t.getMembers().size(); i++) {
                    BodyDeclaration body = t.getMember(i);
                    // below line will add subclasses to className but code needs to be altered to work with it
                    if (body.isClassOrInterfaceDeclaration())  subClasses.add(body.asClassOrInterfaceDeclaration());
                }
            }
        }
    }


    private void extractNumOfFields(){
        List<FieldDeclaration> fields = new ArrayList<>();
        VoidVisitor<List<FieldDeclaration>> fieldCollector = new FieldCollector();
        fieldCollector.visit(cu, fields);

        for(FieldDeclaration f : fields){
            numOfFields++;

            if(f.isPublic())
                numOfPublicFields++;
        }
    }

    /* This method sorts through the files members sorting them into
    * Types: ClassOrInterfaceDeclaration, MethodDeclaration & ConstructorDeclaration
    * and adds them to their respective List or HashMap  */
    private void extractMethodsMetrics(){
        for (TypeDeclaration td : cu.getTypes()) {
            List list = td.getMembers();
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i) instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) list.get(i);
                    subClassMethods.put(clazz.getName().asString(), clazz.getMethods());
                }

                else if(list.get(i) instanceof MethodDeclaration){
                    MethodDeclaration md = (MethodDeclaration) list.get(i);
                    System.out.println(md.getName().toString());
                    numOfMethods++;
                    if (md.isPublic())
                        numOfPublicMethods++;

                    String methodName = md.getName().toString();
                    methodsMetrics.put(methodName, new MethodMetrics(md));
                }

                else if(list.get(i) instanceof ConstructorDeclaration){
                    ConstructorDeclaration cd = (ConstructorDeclaration) list.get(i);
                    classConstructors.add(cd);
                }
            }
        }
    }

    private void extractComments(){

        classComments.addAll(cu.getComments());

    }


    /* GETTERS */
    public List<Integer> getClassLengths() {
        return classLengths;
    }

    public int getNumOfFields() {
        return numOfFields;
    }

    public int getNumOfPublicFields() {
        return numOfPublicFields;
    }

    public int getNumOfMethods() {
        return numOfMethods;
    }

    public int getNumOfPublicMethods() {
        return numOfPublicMethods;
    }

    public List<String> getClassNames(){
        return classNames;
    }

    public List<Comment> getClassComments() {
        return classComments;
    }

    public HashMap<String, MethodMetrics> getMethodsMetrics() {
        return (HashMap<String, MethodMetrics>) methodsMetrics.clone();
    }

    public CompilationUnit getCompilationUnit() {
        return cu;
    }
    public List<ClassOrInterfaceDeclaration> getSubClasses() {
        return subClasses;
    }

    public List<ConstructorDeclaration> getClassConstructors() {
        return classConstructors;
    }

    public HashMap<String, List<MethodDeclaration>> getSubClassMethods() {
        return subClassMethods;
    }

}


//TODO Extract private class Listener from Collector