package smells;

import metrics.FileMetrics;

// Class doesn't do enough
public class LazyClassSmell extends AbstractCodeSmell {

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {

    }
}
