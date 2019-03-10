package smells;

import metrics.FileMetrics;
import visitors.CyclomaticComplexityVisitor;

//count num of ifs, else ifs, switch cases and loops
public class CyclomaticComplexitySmell extends AbstractCodeSmell {
    private int cyclomaticComplexity;

    @Override
    public void detectSmell(FileMetrics m) {
        CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor(m.getCompilationUnit());
        cyclomaticComplexity = ccv.getComplexity();

        //if complexity > 20, if > 15 give a warning and otherwise just provide the info
    }

}
