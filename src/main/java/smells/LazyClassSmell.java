package smells;

import metrics.FileMetrics;

// Class doesn't do enough implying it should be deleted
/*
Class is not lazy if:
- It's a super class to at least one other class
- It's an Associate Class (i.e. as a field)
- It's depended upon by methods of other class
 */
public class LazyClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Lazy Class";

    @Override
    public void detectSmell(FileMetrics metrics) {

    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
