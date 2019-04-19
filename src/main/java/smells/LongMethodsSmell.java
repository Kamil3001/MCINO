package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;

import java.util.Map;

public class LongMethodsSmell extends AbstractCodeSmell {
    private final static String smellName = "Long Methods";

    @Override
    public void detectSmell(FileMetrics metrics) {
        float avgMethodLength = 0;

        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            int methodLength = entry.getValue().getNumOfLines();
            if(methodLength > 30){
                occurrences.add(new Occurrence(entry.getValue().getStartLine(), entry.getValue().getEndLine()));
            }

            avgMethodLength += methodLength;
        }
        avgMethodLength = (metrics.getNumOfMethods() > 0) ? avgMethodLength/metrics.getNumOfMethods() : 0;

        if(avgMethodLength > 50)
            severity = 3;
        else if(avgMethodLength > 40)
            severity = 2;
        else if(avgMethodLength > 30)
            severity = 1;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
