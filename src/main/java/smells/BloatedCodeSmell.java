package smells;

// long class/methods/ids
public class BloatedCodeSmell extends AbstractCodeSmell {
    @Override
    public String detectSmell() {
        System.out.println("HIIIII"); return null;
    }

    @Override
    public double getScore() {
        return 0;
    }
}
