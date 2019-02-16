import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import utils.Setup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainGUI extends JPanel {

    static private final String newline = "\n";

    private JButton findDir;
    private JTextArea log;
    private JFileChooser fc;


    private MainGUI() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        log = new JTextArea(5,40);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        log.append("\t ------ Welcome to MCINO v1.0 ------" +newline);
        log.append("\t              ~~~~ NOSEJOB ~~~~" +newline);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        findDir = new JButton("Find Directory...");
        findDir.addActionListener(new Listener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(findDir);

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    private static void launchGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("NoseJob");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new MainGUI());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        launchGUI();
    }

    private class Listener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == findDir) {
                int returnVal = fc.showOpenDialog(MainGUI.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    log.append("Directory chosen: " + file.getName() + newline);
                    Setup setup = new Setup(file.getAbsolutePath());
                    CompilationUnit[] cUnit = setup.run();

                    for(CompilationUnit cu: cUnit) {
                        FileMetrics classMetricsclass = new FileMetrics(cu);
                        log.append("------------------" + newline);
                        log.append("Class name: " + classMetricsclass.getClassNames().toString() + newline);
                        log.append("Class length: " + classMetricsclass.getClassLengths().toString() + newline);
                        log.append("Num fields: " + classMetricsclass.getNumOfFields() + newline);
                        log.append("Num public fields: " + classMetricsclass.getNumOfPublicFields() + newline);
                        log.append("Num methods: " + classMetricsclass.getNumOfMethods() + newline);
                        log.append("Num public methods: " + classMetricsclass.getNumOfPublicMethods() + newline);
                        log.append(newline);
                    }
                }
                else
                    log.append("Open command cancelled by user." + newline);

                log.setCaretPosition(log.getDocument().getLength());
            }
        }
    }


}
