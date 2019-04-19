package smells;

import metrics.FileMetrics;

public class LongClassSmell extends AbstractCodeSmell {
    private final static String smellName = "Long Class";

    @Override
    public void detectSmell(FileMetrics metrics) {
        int mainClassLength = metrics.getClassLengths().get(0);

        if(mainClassLength > 1300)
            severity = 3;
        else if(mainClassLength > 1150)
            severity = 2;
        else if(mainClassLength > 1000)
            severity = 1;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
