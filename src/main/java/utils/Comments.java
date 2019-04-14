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

    private HashMap<String, String[]>  solutions = new HashMap<>() {{
        put("Long Method", new String[]{
                "To reduce the length of a method body, use Extract Method.",
                "If local variables and parameters interfere with extracting a method, use Replace Temp with Query, Introduce Parameter Object or Preserve Whole Object.",
                "Try moving the entire method to a separate object via Replace Method with Method Object.",
                "Conditional operators and loops are a good clue that code can be moved to a separate method. For conditionals, use Decompose Conditional. If loops are in the way, try Extract Method."
        });
        put("Long Class", new String[]{
                "Extract Class helps if part of the behavior of the large class can be spun off into a separate component.",
                "Extract Interface helps if it’s necessary to have a list of the operations and behaviors that the client can use.",
                "If a large class is responsible for the graphical interface, you may try to move some of its data and behavior to a separate domain object. In doing so, it may be necessary to store copies of some data in two places and keep the data consistent. Duplicate Observed Data offers a way to do this.",
                "Extract Subclass helps if part of the behavior of the large class can be implemented in different ways or is used in rare cases."
        });
        put("Cyclomatic Complexity", new String[]{
                "Avoid use of switch/case statements in your code. Use Factory or Strategy design patterns instead.",
                "Reduce IF expressions in your code. Use the Single Responsibility principle for extracting a piece of code to other methods and make the method for just one responsibility."
        });
        put("Data Clumps", new String[]{
                "If repeating data comprises the fields of a class, use Extract Class to move the fields to their own class.",
                "If the same data clumps are passed in the parameters of methods, use Introduce Parameter Object to set them off as a class.",
                "If some of the data is passed to other methods, think about passing the entire data object to the method instead of just individual fields. Preserve Whole Object will help with this.",
        });
        put("Duplicated Code", new String[]{
                "If a large number of conditional expressions are present and perform the same code (differing only in their conditions), merge these operators into a single condition using Consolidate Conditional Expression and use Extract Method to place the condition in a separate method with an easy-to-understand name.",
                "If the same code is performed in all branches of a conditional expression: place the identical code outside of the condition tree by using Consolidate Duplicate Conditional Fragments.",
                "If the duplicate code is similar but not completely identical, use Form Template Method.",
                "If two methods do the same thing but use different algorithms, select the best algorithm and apply Substitute Algorithm.",
                "If the duplicate code is inside a constructor, use Pull Up Constructor Body."
        });
        put("Feature Envy", new String[]{
                "If a method clearly should be moved to another place, use Move Method.",
                "If only part of a method accesses the data of another object, use Extract Method to move the part in question.",
                "If a method uses functions from several other classes, first determine which class contains most of the data used. Then place the method in this class along with the other data. Alternatively, use Extract Method to split the method into several parts that can be placed in different places in different classes.",
        });
        put("Heavy Commenting", new String[]{
                "If a comment is intended to explain a complex expression, the expression should be split into understandable subexpressions using Extract Variable.",
                "If a comment explains a section of code, this section can be turned into a separate method via Extract Method. The name of the new method can be taken from the comment text itself, most likely.",
                "If a method has already been extracted, but comments are still necessary to explain what the method does, give the method a self-explanatory name. Use Rename Method for this.",
                "If you need to assert rules about a state that’s necessary for the system to work, use Introduce Assertion."
        });
        put("Lazy Class", new String[]{
                "Components that are near-useless should be given the Inline Class treatment.",
                "For subclasses with few functions, try Collapse Hierarchy."
        });
        put("Refused Bequest", new String[]{
                "If inheritance makes no sense and the subclass really does have nothing in common with the superclass, eliminate inheritance in favor of Replace Inheritance with Delegation.",
                "If inheritance is appropriate, get rid of unneeded fields and methods in the subclass. Extract all fields and methods needed by the subclass from the parent class, put them in a new subclass, and set both classes to inherit from it"
        });
    }};

    private HashMap<String, String[]>  results = new HashMap<>() {{
        put("Long Method", new String[]{
                "The longer a method or function is, the harder it becomes to understand and maintain it.",
                "Long methods offer the perfect hiding place for unwanted duplicate code."
        });
        put("Long Class", new String[]{
               "Refactoring of these classes spares developers from needing to remember a large number of attributes for a class.",
                "In many cases, splitting large classes into parts avoids duplication of code and functionality."
        });
        put("Cyclomatic Complexity", new String[]{
                "Improve maintainability for your code."
        });
        put("Data Clumps", new String[]{
              "Improves understanding and organization of code. Operations on particular data are now gathered in a single place, instead of haphazardly throughout the code.",
                "Reduces code size."
        });
        put("Duplicated Code", new String[]{
               "Merging duplicate code simplifies the structure of your code and makes it shorter.",
                "Simplification + shortness = code that’s easier to simplify and cheaper to support."
        });
        put("Feature Envy", new String[]{
               "Less code duplication (if the data handling code is put in a central place).",
                "Better code organization (methods for handling data are next to the actual data)."
        });
        put("Heavy Commenting", new String[]{
                "Code becomes more intuitive and obvious."
        });
        put("Lazy Class", new String[]{
                "Reduced code size.",
                "Easier maintenance."
        });
        put("Refused Bequest", new String[]{
               "Improves code clarity and organization. You will no longer have to wonder why the Dog class is inherited from the Chair class (even though they both have 4 legs)."
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

    public String[] getSolutions(String smellName) {return solutions.get(smellName);}

    public String[] getResults(String smellName) {return results.get(smellName);}
}
