package smells;

import metrics.FileMetrics;
import visitors.CyclomaticComplexityVisitor;

//count num of ifs, else ifs, switch cases and loops
public class CyclomaticComplexitySmell extends AbstractCodeSmell {
    int cyclomaticComplexity;

    @Override
    public void detectSmell(FileMetrics m) {
        CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor(m.getCompilationUnit());
        cyclomaticComplexity = ccv.getComplexity();
    }

    @Override
    public double getScore() {
        //todo
        return 0;
    }
}
