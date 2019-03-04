package smells;

import metrics.FileMetrics;

public abstract class AbstractCodeSmell implements Scorable {
    protected int occurrenceCount;
    protected String[] occurrences;

    public abstract void detectSmell(FileMetrics metrics);

    public String[] getOccurrences(){
        return occurrences;
    }

    public int getCount(){
        return occurrenceCount;
    }
}
