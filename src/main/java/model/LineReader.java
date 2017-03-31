
package model;

import controller.Coordinator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * This class alow to read a line from a text format (txt)
 * @author victor del rio
 */
public class LineReader {
    private String sourceCode;
    private Scanner scanner;
    private KBConnector kbconnector;

    public LineReader() {
        kbconnector = new KBConnector();
    }

    /**
     * It allows to read all the File source code
     * @param path
     * @return 
     * @throws java.io.FileNotFoundException
     */
    public String readFileCode(File path) throws FileNotFoundException{
        sourceCode = new Scanner(path).useDelimiter("\\Z").next();
        return sourceCode;
    }
    
    public String[] splitFileCode(String code){
        String[] codeSplit = code.split("\n");
        return codeSplit;
    }
    
    public Line analyzeLineOfCode(String lineOfCode){
        Line line = new Line();
        line.setLineOfCode(lineOfCode);
        line = (Line) kbconnector.enterObjectInKnowledgeBase(line);
        kbconnector.deleteObjectFromKnowledgeBase(line);
        return line;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

}
