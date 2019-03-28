package smells;


import metrics.FileMetrics;

import java.util.List;

public class DuplicatedCodeSmell extends AbstractCodeSmell {
    private final static String smellName = "Duplicated Code";
    private static String[] resultComments = {
            "",
            "",
            "",
            ""
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
