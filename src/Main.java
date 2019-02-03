import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {


        /* CHANGE THE DIRECTORY TO SOME PROJECTS DIRECTORY WITH .CLASS FILES*/
        Setup test = new Setup("D:\\University\\Stage 3\\Semester 2\\Software Engineering\\Assignment1\\out\\production\\Assignment1");

        ArrayList<String> classNames = test.getClassNames();

        Class[] instances = new Class[classNames.size()];

        int index = 0;
        for(String cls: classNames)
        {
            instances[index] = test.instantiateClass(cls);
            index++;
        }

        System.out.println(Arrays.toString(instances));
    }
}
