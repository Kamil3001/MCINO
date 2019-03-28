package smells;


import metrics.FileMetrics;

public class RefusedBequestSmell extends AbstractCodeSmell {
    private final static String smellName = "Refused Bequest";
    private static String[] resultComments = {
            "",
            "",
            "",
            ""
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
