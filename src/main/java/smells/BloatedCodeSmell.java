package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;

import java.util.Map;

// long class/methods/ids (use in tandem with cyclomatic complexity to get a more accurate view of the files)
public class BloatedCodeSmell extends AbstractCodeSmell {
    private final static String smellName = "Bloated Code";

    @Override
    public void detectSmell(FileMetrics metrics) {
        //avg lines per 30 (excl. comments and spacing)
        //max 1000 lines of code per class (excl. method declarations)
        //max 30 methods

        int avgMethodLength=0;
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            avgMethodLength += entry.getValue().getNumOfLines(); //extract number of lines from MethodMetrics of the file
        }
        avgMethodLength = (metrics.getNumOfMethods() >0) ?  avgMethodLength/metrics.getNumOfMethods() : 0;


        //depending on number of methods we use the corresponding average method lengths and/or total class lengths to set severities
        int methodsNum = metrics.getNumOfMethods();
        int mainClassLength = metrics.getClassLengths().get(0);

        if(methodsNum > 40){
            if(avgMethodLength > 40 || mainClassLength > 1150){
                severity = 3;
            }
            else if(avgMethodLength > 30 || mainClassLength > 1000){
                severity = 2;
            }
            else if(avgMethodLength > 20 || mainClassLength > 850) {
                severity = 1;
            }
        }
        else if(methodsNum > 30){
            if(avgMethodLength > 50 || mainClassLength > 1300){
                severity = 3;
            }
            else if(avgMethodLength > 40 || mainClassLength > 1150){
                severity = 2;
            }
            else if(avgMethodLength > 30 || mainClassLength > 1000) {
                severity = 1;
            }
        }
        else if(!metrics.getClassLengths().isEmpty()){
            if(avgMethodLength > 60 || mainClassLength > 1450){
                severity = 3;
            }
            else if(avgMethodLength > 50 || mainClassLength > 1300){
                severity = 2;
            }
            else if(avgMethodLength > 40 || mainClassLength > 1150) {
                severity = 1;
            }
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
