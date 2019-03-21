package smells;

import metrics.FileMetrics;
import visitors.CyclomaticComplexityVisitor;

import java.util.List;

//count num of ifs, else ifs, switch cases and loops
public class CyclomaticComplexitySmell extends AbstractCodeSmell {
    private final static String smellName = "Cyclomatic Complexity";
    private int cyclomaticComplexity;

    @Override
    public void detectSmell(FileMetrics metrics) {
        CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor(metrics.getCompilationUnit());
        cyclomaticComplexity = ccv.getComplexity();

        //if complexity > 20, if > 15 give a warning and otherwise just provide the info
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public List<Integer> getOccurrences() {
        //todo
        return null;
    }

    @Override
    public int getSeverity() {
        //todo
        return 0;
    }
}
