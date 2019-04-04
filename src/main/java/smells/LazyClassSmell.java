package smells;


import metrics.FileMetrics;

import java.util.List;

// Class doesn't do enough
public class LazyClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Lazy Class";
    private static String[] resultComments = {
            "No signs of being a lazy class. All good.",
            "Signs of being a lazy class. Check code carefully.",
            "The problem is getting worse.",
            "Your class is lazy. Area Red, must be fixed!"
    };

    @Override
    public void detectSmell(FileMetrics metrics) {

        //todo
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public String getResultComment() {
        return resultComments[severity];
    }
}
