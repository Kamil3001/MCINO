package smells;

public abstract class AbstractCodeSmell implements Scorable {
    protected int occurrenceCount;
    protected String[] occurrences;

    public abstract String detectSmell();

    public String[] getOccurrences(){
        return occurrences;
    }

    public int getCount(){
        return occurrenceCount;
    }
}
