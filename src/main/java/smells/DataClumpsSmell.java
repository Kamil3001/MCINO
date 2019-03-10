package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;

import java.util.Map;

// Data Clumps are a group of parameters that are passed around together (this is a smell which should be refactored by the programmer)
public class DataClumpsSmell extends AbstractCodeSmell{

    @Override
    public void detectSmell(FileMetrics metrics) {

        //todo this is a hard smell to detect accurately because of the potential variety in parameters passed in
        //Workaround: Assume that high number of parameters to a method is a sign data clumps are possible
        //  Thus if average number of parameters is high, they can likely be grouped into objects

        int avgNumOfParams = 0;
        int maxNumOfParams = 0;
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            int numOfParams = entry.getValue().getNumOfParams();
            if(numOfParams > maxNumOfParams)
                maxNumOfParams = numOfParams;

            avgNumOfParams += numOfParams;
        }
        avgNumOfParams /= metrics.getNumOfMethods();

        if(avgNumOfParams > 8){
            //Data clumps are extremly likely to be a problem. Likeliness of methods having 8 parameters on average and not be interdependent is slim
        }
        else if(avgNumOfParams > 5){
            //A high chance data clumps are a problem
        }
        else if(maxNumOfParams > 8){
            //data clumps are not a problem throughout the entire file given small average but select methods may smell of data clumps
        }
        else{
            //data clumps not a problem
        }

    }
}
