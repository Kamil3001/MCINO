package smells;


import metrics.FileMetrics;
import visitors.CyclomaticComplexityVisitor;

//count num of ifs, else ifs, switch cases and loops
public class CyclomaticComplexitySmell extends AbstractCodeSmell {
    private final static String smellName = "Cyclomatic Complexity";

    @Override
    public void detectSmell(FileMetrics metrics) {
        //the visitor does the counting for us
        CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor(metrics.getCompilationUnit());
        int cyclomaticComplexity = ccv.getComplexity();

        if(cyclomaticComplexity > 20) //high
            severity = 3;
        else if(cyclomaticComplexity > 15) //medium
            severity = 2;
        else if(cyclomaticComplexity > 10) //low
            severity = 1;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
