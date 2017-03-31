/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static model.ConstantsManager.CLASS_DECLARATION;
import static model.ConstantsManager.METHOD_DECLARATION;
import static model.ConstantsManager.PACKAGE_DECLARATION;

/**
 * This class handles everything related to the creation of elements ofg.
 * @author victor
 */
public class OFGconverter {
    public static boolean cicleBreak = false;
    public static String[] typesOfLine = {"packageDeclaration", "classDeclaration", "methodDeclaration", "objectInstantiation", "objectVariableAssignation", "objectMethodInvocation", "methodInvocation"};
    
    /**
     * Organize the line of code depending on the type to which it belongs.
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
     * It takes a linesContainer and defines from an ofg element within the 4 types available.
     * @param linesContainer
     * @param line
     * @param rootNode 
     * @param file 
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
     * Defines whether the line of code matches any of the methods invocation lines from objects belonging 
     * to the linesContainer and calls createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @param file
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
     * Defines whether the line of code matches any of the lines invoking methods
     * belonging to the linesContainer and calls createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @param file
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
     * Defines whether the line of code matches any of the instantiation lines of objects 
     * belonging to the linesContainer and calls createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @param file
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
     * Defines whether the line of code matches any of the lines of assignment of variables to objects
     * belonging to the linesContainer and calls createOFGElement.
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param ofgElement
     * @param file
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
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(lineOfCode);
            if (matcher.find()){
                found = matcher.group(groupIndex);
            }
        return found;
    }
}
