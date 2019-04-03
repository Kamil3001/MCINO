package smells;


import metrics.FileMetrics;

import java.util.List;

public class DuplicatedCodeSmell extends AbstractCodeSmell {
    private final static String smellName = "Duplicated Code";
    private static String[] resultComments = {
            "No duplicated code here. Run along.",
            "Showing signs of duplicated code. Investigate.",
            "Code heavily duplicated. Fix as soon as possible.",
            "Duplicated code everywhere. Solve immediately."
    };

    @Override
    public void detectSmell(FileMetrics metrics) {

    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public String getResultComment() {
        return resultComments[severity];
    }
}
