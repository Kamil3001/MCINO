package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import visitors.FeatureEnvyVisitor;

import java.util.Map;

public class FeatureEnvySmell extends AbstractCodeSmell{
    private final static String smellName = "Feature Envy";

    @Override
    public void detectSmell(FileMetrics metrics) {
        System.out.println("-----------------------");
        System.out.println(metrics.getClassNames().get(0));

        int numOfProblematicMethods = 0;
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            FeatureEnvyVisitor v = new FeatureEnvyVisitor(entry.getValue().getMethodDeclaration());
            if(v.getNumOfFieldAccesses() > 5){
                numOfProblematicMethods++;
            }


            //todo add to occurences (method declaration or body)
        }

        //todo decide on way to set severity

    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
