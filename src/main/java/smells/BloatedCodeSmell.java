package smells;

import metrics.FileMetrics;

// long class/methods/ids
public class BloatedCodeSmell extends AbstractCodeSmell {

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {

    }
}
