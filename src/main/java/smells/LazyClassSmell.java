package smells;

import metrics.FileMetrics;

// Class doesn't do enough implying it should be deleted
// Severity in this case rates how likely the class is to be a lazy class
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
}
