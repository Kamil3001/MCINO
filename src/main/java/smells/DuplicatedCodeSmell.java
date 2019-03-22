package smells;


import metrics.FileMetrics;

import java.util.List;

public class DuplicatedCodeSmell extends AbstractCodeSmell {
    private final static String smellName = "Duplicated Code";

    @Override
    public void detectSmell(FileMetrics metrics) {

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
