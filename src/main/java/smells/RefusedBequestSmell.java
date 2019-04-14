package smells;


import metrics.FileMetrics;


/* When inheritance doesn't make sense, i.e. superclass and subclass
are completely different

https://www.academia.edu/7927301/Identification_of_Refused_Bequest_Code_Smells
todo check for these when superclass not abstract or an interface
- high severity => no overriding, no subclass method usage, no invocation of super
- medium severity => no overriding, some subclass method usage, no invocation of super
- low severity => some overriding, some subclass method usage
- none => otherwise
 */
public class RefusedBequestSmell extends AbstractCodeSmell {
    private final static String smellName = "Refused Bequest";

    @Override
    public void detectSmell(FileMetrics metrics) {
        //todo
    }

    @Override
    public String getSmellName() {
        return smellName;
    }
}
