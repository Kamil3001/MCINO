package smells;


import metrics.FileMetrics;
import results.Resultable;

public abstract class AbstractCodeSmell implements Resultable {

    public abstract void detectSmell(FileMetrics metrics);

    public abstract String getSmellName();
}
