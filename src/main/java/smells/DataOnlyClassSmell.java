package smells;

import metrics.FileMetrics;

// Class stores data only
public class DataOnlyClassSmell extends AbstractCodeSmell{

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {

    }
}
