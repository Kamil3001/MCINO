package smells;


import metrics.FileMetrics;

import java.util.List;

public class FeatureEnvySmell extends AbstractCodeSmell{
    private final static String smellName = "Feature Envy";

    @Override
    public void detectSmell(FileMetrics metrics) {

    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
