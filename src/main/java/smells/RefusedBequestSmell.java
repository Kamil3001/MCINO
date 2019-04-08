package smells;


import metrics.FileMetrics;

public class RefusedBequestSmell extends AbstractCodeSmell {
    private final static String smellName = "Refused Bequest";
    private static String[] resultComments = {
            "No signs of this. We good.",
            "Showing signs of refused bequest. Watch out.",
            "Refused bequest is evident here. Have a close look at your code",
            "The contract of the base class is not honoured by the derived class. Violation of coding principles. Must fix!"
    };

    @Override
    public void detectSmell(FileMetrics metrics) {
        
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
