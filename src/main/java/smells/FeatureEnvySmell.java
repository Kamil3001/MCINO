package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;
import visitors.FeatureEnvyVisitor;

import java.util.Map;

public class FeatureEnvySmell extends AbstractCodeSmell{
    private final static String smellName = "Feature Envy";

    @Override
    public void detectSmell(FileMetrics metrics) {
        int numOfProblematicMethods = 0;
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            FeatureEnvyVisitor v = new FeatureEnvyVisitor(entry.getValue().getMethodDeclaration());
            if(v.getNumOfFieldAccesses() > 5){
                numOfProblematicMethods++;
                //todo add to occurrences here
            }

        }

        //if more than 3 methods perform computations on outside data or average number of problematic methods is greater than 50%
        if(numOfProblematicMethods > 3 || numOfProblematicMethods*1.0/metrics.getNumOfMethods() > 0.5){
            severity = 3;
        }
        //if more than 1 method perform computations on outside data or average number of problematic methods is greater than 30%
        else if(numOfProblematicMethods > 1 || numOfProblematicMethods*1.0/metrics.getNumOfMethods() > 0.3){
            severity = 2;
        }
        //if 1 method performs computatations on outside data
        else if(numOfProblematicMethods > 0){
            severity = 1;
        }


    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
