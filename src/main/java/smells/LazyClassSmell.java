package smells;

import metrics.FileMetrics;

import java.util.List;

// Class doesn't do enough
public class LazyClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Lazy Class";

    @Override
    public void detectSmell(FileMetrics metrics) {

        //todo
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public List<Integer> getOccurrences() {
        return null;
    }

    @Override
    public int getSeverity() {
        return 0;
    }
}
