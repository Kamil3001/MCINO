package smells;

import metrics.ClassMetrics;

public abstract class AbstractCodeSmell implements Scorable {
    protected int occurrenceCount;
    protected String[] occurrences;

    protected abstract String detectSmell();

    public String[] getOccurrences(){
        return occurrences;
    }

    public int getCount(){
        return occurrenceCount;
    }
}
