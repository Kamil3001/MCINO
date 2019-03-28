package smells;


import metrics.FileMetrics;
import visitors.CyclomaticComplexityVisitor;

import java.util.List;

//count num of ifs, else ifs, switch cases and loops
public class CyclomaticComplexitySmell extends AbstractCodeSmell {
    private final static String smellName = "Cyclomatic Complexity";
    private static String[] resultComments = {
            "",
            "",
            "",
            ""
    };

    @Override
    public void detectSmell(FileMetrics metrics) {
        CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor(metrics.getCompilationUnit());
        int cyclomaticComplexity = ccv.getComplexity();

        if(cyclomaticComplexity > 20) //high
            severity = 3;
        else if(cyclomaticComplexity > 15) //medium
            severity = 2;
        else if(cyclomaticComplexity > 10) //low
            severity = 1;
        else
            severity = 0;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public String getResultComment() {
        return null;
    }
}
