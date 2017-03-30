
package model;

import controller.Coordinator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class alow to read a line from a text format (txt)
 * @author victor del rio
 */
public class LineReader {
    private String sourceCode;//It is the source code that can be readed
    private Scanner scanner;//It is the object that helps to read the source code
    private Coordinator coordinator;
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
//        line = (Line) kbconnector.enterObjectInKnowledgeBase(line);
//        kbconnector.deleteObjectFromKnowledgeBase(line);
        line = analize(line);

        return line;
    }
    
    public Line analize(Line line){
        ExpressionLibrary library = new ExpressionLibrary();
        library.declareLibrary();
        for(Expression expression: library.getExpressions().values()){
            Pattern pattern = Pattern.compile(expression.getRegularExpression());
            Matcher matcher = pattern.matcher(line.getLineOfCode());
            if(matcher.find() && line.isState()==false){
                line.setType(expression.getExpressionName());
//                System.err.printf(line.getLineOfCode());
//                System.out.printf(line.getType());
                line.setState(true);
            }
        }
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
