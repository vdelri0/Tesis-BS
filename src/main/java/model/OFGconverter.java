/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import static model.ConstantsManager.CLASS_DECLARATION;
import static model.ConstantsManager.METHOD_DECLARATION;
import static model.ConstantsManager.PACKAGE_DECLARATION;

/**
 *
 * @author victor
 */
public class OFGconverter {
    public static boolean cicleBreak = false;
    public static String[] typesOfLine = {"packageDeclaration", "classDeclaration", "methodDeclaration", "objectInstantiation", "objectVariableAssignation", "objectMethodInvocation", "methodInvocation"};
    
    /**
     * Organiza la linea de codigo dependiendo del tipo al que pertenezca.
     * @param linesContainer
     * @param line 
     */
    public static void sortOutLineOfCode(LinesContainer linesContainer, Line line){
        if((typesOfLine[0]).equals(line.getType())){
            linesContainer.setPackageLine(line);
        } else if((typesOfLine[1]).equals(line.getType())){
            linesContainer.setClassLine(line);
        } else if((typesOfLine[2]).equals(line.getType())){
            linesContainer.getMethodsLines().add(line);
        } else if((typesOfLine[3]).equals(line.getType())){
            linesContainer.getObjectInstantiationLines().add(line);
        } else if((typesOfLine[4]).equals(line.getType())){
            linesContainer.getObjectVariableAssignationLines().add(line);
        } else if((typesOfLine[5]).equals(line.getType())){
            linesContainer.getObjectMethodInvocationLines().add(line);
        } else if((typesOfLine[6]).equals(line.getType())){
            linesContainer.getMethodInvocationLines().add(line);
        }
    }
    
    /**
     * Toma el un linesContainer y define a partir de el un elemento del ofg dentro de los 4 tipos disponibles.
     * @param linesContainer
     * @param line
     * @param rootNode 
     * @return 
     */
    public static OFGelement defineTypeOfOFGElement(LinesContainer linesContainer, Line line, boolean rootNode, File file){
        OFGelement ofgElement = null;
            ofgElement = defineObjectMethodInvocationElement(linesContainer, line, rootNode, ofgElement, file);
            ofgElement = defineMethodInvocationElement(linesContainer, line, rootNode, ofgElement, file);
            ofgElement = defineObjectInstantiationElement(linesContainer, line, rootNode, ofgElement, file);
            ofgElement = defineObjectVariableAssignationsElement(linesContainer, line, rootNode, ofgElement, file);
        return ofgElement;
    }
    
    /**
     * Define si la linea de codigo coincide con alguna de las lineas de invocacion de
     * metodos a partir de objetos pertenecientes al linesContainer y llama a createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @return 
     */
    public static OFGelement defineObjectMethodInvocationElement(LinesContainer linesContainer, Line line, boolean rootNode, OFGelement ofgElement, File file){
        for (Line ObjectLine : linesContainer.getObjectMethodInvocationLines()) {
            if(ObjectLine.getLineOfCode().equals(line.getLineOfCode())){
                ofgElement = createOFGElement(linesContainer, line, rootNode, file);
            }
        }
        return ofgElement;
    }
    
    /**
     * Define si la linea de codigo coincide con alguna de las lineas de invocacion de
     * metodos pertenecientes al linesContainer y llama a createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @return 
     */
    public static OFGelement defineMethodInvocationElement(LinesContainer linesContainer, Line line, boolean rootNode, OFGelement ofgElement, File file){
        for (Line ObjectLine : linesContainer.getMethodInvocationLines()) {
            if(ObjectLine.getLineOfCode().equals(line.getLineOfCode())){
                ofgElement = createOFGElement(linesContainer, line, rootNode, file);
            }
        }
        return ofgElement;
    }
    
    /**
     * Define si la linea de codigo coincide con alguna de las lineas de instanciacion
     * de objetos pertenecientes al linesContainer y llama a createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @return 
     */
    public static OFGelement defineObjectInstantiationElement(LinesContainer linesContainer, Line line, boolean rootNode, OFGelement ofgElement, File file){
        for (Line ObjectLine : linesContainer.getObjectInstantiationLines()) {
            if(ObjectLine.getLineOfCode().equals(line.getLineOfCode())){
                ofgElement = createOFGElement(linesContainer, line, rootNode, file);
            }
        }
        return ofgElement;
    }
    
    /**
     * Define si la linea de codigo coincide con alguna de las lineas de asignacion de variables
     * a objetos pertenecientes al linesContainer y llama a createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @return 
     */
    public static OFGelement defineObjectVariableAssignationsElement(LinesContainer linesContainer, Line line, boolean rootNode, OFGelement ofgElement, File file){
        for (Line ObjectLine : linesContainer.getObjectVariableAssignationLines()) {
            if(ObjectLine.getLineOfCode().equals(line.getLineOfCode())){
                ofgElement = createOFGElement(linesContainer, line, rootNode, file);
            }
        }
        return ofgElement;
    }
    
    /**
     * Creates a new ofg element
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param file
     * @return 
     */
    public static OFGelement createOFGElement(LinesContainer linesContainer, Line line, boolean rootNode, File file){
        String methodName = "";
        String packageName = findPattern(linesContainer.getPackageLine().getLineOfCode(), PACKAGE_DECLARATION, 1);
        if(linesContainer.getClassLine() == null){
            System.err.println(linesContainer.getFile().getAbsolutePath());
        }
        String className = findPattern(linesContainer.getClassLine().getLineOfCode(), CLASS_DECLARATION, 3);
        
        
        int lastIndex = 0;
        for (Line methodLine : linesContainer.getMethodsLines()) {
            if(methodLine.getIndex()>lastIndex && methodLine.getIndex()<line.getIndex()){
                lastIndex = methodLine.getIndex();
                methodName = findPattern(methodLine.getLineOfCode(), METHOD_DECLARATION, 3);
            } 
        }
        OFGelement ofgElement = new OFGelement(true, line, packageName, className, methodName, linesContainer, file);
        linesContainer.setAnalyzed(true);
        return ofgElement;
    }
   

    /**
     * Find if the pattenr given in the line of code matches with the regular expression.
     * @param lineOfCode
     * @param regex
     * @param groupIndex
     * @return 
     */
    public static String findPattern(String lineOfCode, String regex, int groupIndex){
        String found = "";
//        try {
//            if("    public int size() {\r".equals(lineOfCode)){
//                System.err.println("We are in modafoca");
//            }
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(lineOfCode);
            if (matcher.find()){
                found = matcher.group(groupIndex);
            }
//        } catch (StackOverflowError e) {
//            System.out.println(lineOfCode);
//            System.out.println(regex);
//            System.out.println(groupIndex);
//            System.err.println("--------------------------------------------Nivel de recursion reportado: " + e.getStackTrace().length);
//        }
            
        return found;
    }
}
