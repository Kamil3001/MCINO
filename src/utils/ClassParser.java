package utils;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ClassParser {
    private String javaFile;

    public ClassParser(String javaFile) {
        this.javaFile = javaFile;
    }

    public HashMap<Method, Integer> computeMethodLengths(Method[] methods) {
        HashMap<Method, Integer> methodLengths = new HashMap<>();

        for(Method m : methods){
            methodLengths.put(m, getMethodLength(m));
        }

        return methodLengths;
    }

    public int getMethodLength(Method m){
        return 0;
    }

}
