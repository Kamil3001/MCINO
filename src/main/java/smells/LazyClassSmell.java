package smells;

// Class doesn't do enough
public class LazyClassSmell extends AbstractCodeSmell {
    @Override
    protected String detectSmell() {
        return null;
    }

    @Override
    public double getScore() {
        return 0;
    }
}
