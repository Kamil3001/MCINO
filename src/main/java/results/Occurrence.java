package results;

public class Occurrence {
    private final String smellName;
    private String className;
    private String methodName;
    private int lineNumber;
    public Occurrence(String smellName, String className, String methodName, int lineNumber){
        this.smellName = smellName;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
    }

    public String getSmellName() {
        return smellName;
    }

    public String getClassName() {
        return className;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMethodName(){ return methodName;}
}
