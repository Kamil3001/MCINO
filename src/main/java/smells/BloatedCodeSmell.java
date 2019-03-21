package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;

import java.util.List;
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
        avgMethodLength /= metrics.getNumOfMethods();

        if(metrics.getNumOfMethods() > 30){
            if(avgMethodLength > 30 || metrics.getClassLengths().get(0) > 1000){
                //bloated code is a problem
            }
        }
        else{
            //bloated code is not a problem
        }


    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public List<Integer> getOccurrences() {
        //todo
        return null;
    }

    @Override
    public int getSeverity() {
        //todo
        return 0;
    }
}
