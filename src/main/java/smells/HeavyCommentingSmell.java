package smells;

import metrics.FileMetrics;

public class HeavyCommentingSmell extends AbstractCodeSmell{

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {

    }
}
