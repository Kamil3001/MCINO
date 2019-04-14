package results;

public class Occurrence {
    private final String smellName;
    private String className;
    private String methodName;
    private Range occurenceRange;

    public Occurrence(String smellName, String className, String methodName, int startLine, int endLine){
        this.smellName = smellName;
        this.className = className;
        this.methodName = methodName;
        occurenceRange = new Range(startLine, endLine);
    }

    public String getSmellName() {
        return smellName;
    }

    public String getClassName() {
        return className;
    }

    public Range getRange() {
        return occurenceRange;
    }

    public String getMethodName(){ return methodName;}
}