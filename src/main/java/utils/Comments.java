package utils;

import java.util.HashMap;

public class Comments {

    private static Comments cache = null;
    private HashMap<String, String[]>  comments = new HashMap<>() {{
        put("Bloated Code", new String[]{
                "Code not bloated. Nothing to be concerned about here. ",
                "Code showing signs of bloating. A little concerning. ",
                "Code has a substantial amount of bloating. Very concerning, should fix as soon as possible. ",
                "Code completely bloated (may see a doctor). Area red, must be fixed immediately!"
        });
        put("Cyclomatic Complexity", new String[]{
                "This file has a low Cyclomatic complexity. All clear. ",
                "Complexity is increasing. Keep a close eye here.",
                "High complexity. Should look into fixing this issue.",
                "High Cyclomatic Complexity right here. This is an emergency, fix immediately."
        });
        put("Data Clumps", new String[]{
                "No data clumps here. Carry on.",
                "Showing signs of data clumping, maybe try to optimize the code.",
                "Substantial amount of data clumping. Investigate how to solve this problem",
                "Data clumping left,right & centre. Area red, fix immediately."
        });
        put("Duplicated Code", new String[]{
                "No duplicated code here. Run along.",
                "Showing signs of duplicated code. Investigate.",
                "Code heavily duplicated. Fix as soon as possible.",
                "Duplicated code everywhere. Solve immediately."
        });
        put("Feature Envy", new String[]{
                "No signs of feature envy. Carry on with your day.",
                "Signs of feature envy. Maybe approach the problem differently.",
                "Heavy feature envy detected. Investigate.",
                "Feature envy detected in multiple lines of the code. Things have to be changed here immediately."
        });
        put("Heavy Commenting", new String[]{
                "No heavy commenting here. All clear",
                "Traces of heavy commenting,relax.",
                "A bit too much commenting going on. Investigate.",
                "Comments everywhere ,unnecessary, fix immediately."
        });
        put("Lazy Class", new String[]{
                "No signs of being a lazy class. All good.",
                "Signs of being a lazy class. Check code carefully.",
                "The problem is getting worse.",
                "Your class is lazy. Area Red, must be fixed!"
        });
        put("Refused Bequest", new String[]{
                "No signs of this. We good.",
                "Showing signs of refused bequest. Watch out.",
                "Refused bequest is evident here. Have a close look at your code",
                "The contract of the base class is not honoured by the derived class. Violation of coding principles. Must fix!"
        });
    }};

    private Comments(){
    }

    public static synchronized Comments getCommentsClass(){
        if(cache == null)
            cache = new Comments();
        return cache;
    }

    public String getComment(String smellName, int severity){
        return comments.get(smellName)[severity];
    }

}
