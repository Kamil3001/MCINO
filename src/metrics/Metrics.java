package metrics;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Metrics {
    private Class cls;
    private int numOfPublicFields;
    private int numOfPublicMethods;
    private int classLength;
    private HashMap<Method,Integer> methodLengths;
    private HashMap<Method, Integer> paramsPerMethod;

    public Metrics(Class cls){
        this.cls = cls;
        extractMetrics();
    }

    private void extractMetrics(){

    }

    public int getNumOfPublicFields() {
        return numOfPublicFields;
    }

    public int getNumOfPublicMethods() {
        return numOfPublicMethods;
    }

    public int getClassLength() {
        return classLength;
    }

    public HashMap<Method, Integer> getMethodLengths() {
        return methodLengths;
    }

    public HashMap<Method, Integer> getParamsPerMethod() {
        return paramsPerMethod;
    }
}
