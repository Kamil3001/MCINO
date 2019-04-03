package smells;


import metrics.FileMetrics;

import java.util.List;

public class FeatureEnvySmell extends AbstractCodeSmell{
    private final static String smellName = "Feature Envy";
    private static String[] resultComments = {
            "No signs of feature envy. Carry on with your day.",
            "Signs of feature envy. Maybe approach the problem differently.",
            "Heavy feature envy detected. Investigate.",
            "Feature envy detected in multiple lines of the code. Things have to be changed here immediately."
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
