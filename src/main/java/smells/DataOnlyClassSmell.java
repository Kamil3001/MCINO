package smells;

// Class stores data only
public class DataOnlyClassSmell extends AbstractCodeSmell{
    @Override
    protected String detectSmell() {
        return null;
    }

    @Override
    public double getScore() {
        return 0;
    }
}
