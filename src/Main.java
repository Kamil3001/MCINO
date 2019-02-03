import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args)
    {
        Setup test = new Setup("D:\\University\\Stage 3\\Semester 2\\Software Engineering\\Assignment1\\src");
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
