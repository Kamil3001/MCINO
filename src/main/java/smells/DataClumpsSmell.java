package smells;

import metrics.FileMetrics;

// When the list of parameters passed to method is too long
public class DataClumpsSmell extends AbstractCodeSmell{


    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {

    }
}
