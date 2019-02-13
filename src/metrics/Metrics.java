package metrics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class Metrics {

    private Class cls;
    private int classLength;
    private int numOfFields;
    private int numOfPublicFields;
    private int numOfMethods;
    private int numOfPublicMethods;
    private HashMap<Method,Integer> methodLengths;
    private HashMap<Method, Integer> paramsPerMethod;

    public Metrics(Class cls){
        this.cls = cls;
        methodLengths = new HashMap<>();
        paramsPerMethod = new HashMap<>();
        extractMetrics();
    }

    private void extractMetrics(){
        extractClassLength();
        extractNumOfFields();
        extractMethodMetrics();
    }

    private void extractClassLength(){
        //use parser here
    }

    private void extractNumOfFields(){
        for(Field f : cls.getDeclaredFields()){
            numOfFields++;
            if(Modifier.isPublic(f.getModifiers()))
                numOfPublicFields++;
        }
    }

    private void extractMethodMetrics(){
        for(Method m : cls.getDeclaredMethods()){
            numOfMethods++;
            paramsPerMethod.put(m, m.getParameterCount());

            //get method length here with parser

            if(Modifier.isPublic(m.getModifiers()))
                numOfPublicMethods++;
        }
    }

    public int getNumOfFields(){
        return numOfFields;
    }

    public int getNumOfPublicFields() {
        return numOfPublicFields;
    }

    public int getNumOfMethods(){
        return numOfMethods;
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
