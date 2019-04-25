package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class HTMLUtil {

    private static HTMLUtil cache = null;
    private HashMap<String,String> tabNameToHtmlFile = new HashMap<>(){
        {
            put("Project","project.html");
            put("Long Method", "long-method.html");
            put("Long Class","long-class.html");
            put("Cyclomatic Complexity","cyclomatic-complexity.html");
            put("Data Clumps","data-clumps.html");
            put("Duplicated Code","duplicated-code.html");
            put("Feature Envy","feature-envy.html");
            put("Heavy Commenting","heavy-commenting.html");
            put("Lazy Class","lazy-class.html");
            put("Refused Bequest","refused-bequest.html");

        }};



    private HTMLUtil(){
    }

    private String extractHTML(File html) {

        StringBuilder htmlBuilder = new StringBuilder();
        Scanner sc = null;
        try {
            sc = new Scanner(html);
            while(sc.hasNextLine()){
                htmlBuilder.append(sc.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return htmlBuilder.toString();
    }


    public String getHtml(String tabName){
        File htmlFile = new File("src/main/java/resources/html/"+tabNameToHtmlFile.get(tabName));
        String htmlCode = extractHTML(htmlFile);
        return htmlCode;
    }

    public static synchronized HTMLUtil getHTMLUtil(){
        if(cache == null)
            cache = new HTMLUtil();
        return cache;
    }
}
