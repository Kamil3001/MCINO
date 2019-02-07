package smells;

public abstract class AbstractCodeSmell implements Scorable {
    protected int occurrenceCount;
    protected String[] occurrences;

    protected abstract String detectSmells(Class[] classes);

    public String[] getOccurrences(){
        return occurrences;
    }

    public int getCount(){
        return occurrenceCount;
    }
}
