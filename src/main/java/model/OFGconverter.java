/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static model.ConstantsManager.CLASS_DECLARATION;
import static model.ConstantsManager.METHOD_DECLARATION;
import static model.ConstantsManager.OBJECT_METHOD_INVOCATION;
import static model.ConstantsManager.PACKAGE_DECLARATION;

/**
 *
 * @author victor
 */
public class OFGconverter {
    public static boolean cicleBreak = false;
    
    public static OFGelement createOFG(ArrayList<JavaFileAnalized> javaFilesAnalized, Line root) {
        ArrayList<LinesContainer> linesContainers = new ArrayList<LinesContainer>();
        for (JavaFileAnalized javaFileAnalized : javaFilesAnalized) {
            LinesContainer linesContainer = new LinesContainer();
            int numberOfLines = 0;
            for (Line line : javaFileAnalized.getLines()) {
                if(line.isState()){
                    organizeLinesOfCode(linesContainer, line);
                }
                numberOfLines++;
            }
            linesContainer.setNumberOfLines(numberOfLines);
            linesContainers.add(linesContainer);
        }
        return defineOFGElements(linesContainers, root);
    }
    
    public static void organizeLinesOfCode(LinesContainer linesContainer, Line line){
        if(("packageDeclaration").equals(line.getType())){
            linesContainer.setPackageLine(line);
        } else if(("classDeclaration").equals(line.getType())){
            linesContainer.setClassLine(line);
        } else if(("methodDeclaration").equals(line.getType())){
            linesContainer.getMethodsLines().add(line);
        } else if(("objectInstantiation").equals(line.getType())){
            linesContainer.getObjectInstatiationLines().add(line);
        } else if(("objectVariableAssignation").equals(line.getType())){
            linesContainer.getObjectVariableAssignationLines().add(line);
        } else if(("objectMethodInvocation").equals(line.getType())){
            linesContainer.getObjectMethodInvocationLines().add(line);
        } else if(("methodInvocation").equals(line.getType())){
            linesContainer.getMethodInvocationLines().add(line);
        }
    }
    //colocar un booleano para determinar que la expresion  ya fue utilizada.
    //O hacerlo a travez de una clase para la cual ya se realizo el proceso.
    public static OFGelement defineOFGElements(ArrayList<LinesContainer> linesContainers, Line root){
        OFGelement ofgRoot = defineOFGRoot(linesContainers, root);
        String methodRootName = findPattern(ofgRoot.getLine().getLineOfCode(),OBJECT_METHOD_INVOCATION, 3);
        ArrayList<OFGelement> children = new ArrayList<OFGelement>();
        ArrayList<OFGelement> subChildren;
        findChildren(linesContainers, methodRootName, children);
        ofgRoot.setChildren(children);
        if(!children.isEmpty()){
            while (!cicleBreak) {                    
                subChildren = defineOFGSubChildren(linesContainers, children);
                children = new ArrayList<OFGelement>();
                if(!subChildren.isEmpty()){
                    insertSubchildren(children, subChildren);
                }
            }
        }
        return ofgRoot;
    }
    
    public static void insertSubchildren(ArrayList<OFGelement> children, ArrayList<OFGelement> subChildren){
        if(!subChildren.isEmpty()){
            for (OFGelement subChildren1 : subChildren) {
                children.add(subChildren1);
            }
        }
    }
    
    /**
     * ayuda a encontrar a los children
     */
    public static ArrayList<OFGelement> defineOFGSubChildren(ArrayList<LinesContainer> linesContainers, ArrayList<OFGelement> children){
        ArrayList<OFGelement> subChildren = new ArrayList<OFGelement>();
        cicleBreak = true;
            for (OFGelement child : children) {
                if(("objectMethodInvocation").equals(child.getLine().getType())){
                    String methodChildName = findPattern(child.getLine().getLineOfCode(), OBJECT_METHOD_INVOCATION, 3);
                    findChildren(linesContainers, methodChildName, subChildren);
                    if(!subChildren.isEmpty()){
                        child.setChildren(subChildren);//mucho cuidado aqui, cuando se añade el subChildren, se hace con todos, incluso los que no pertenecen al child, sino a otro.
                        //Pero arriba, se necesita el subChildren completo para realizar el salto del arbol a la siguiente fila
                        cicleBreak = false;
                    }
                }
            }
        return subChildren;
    }
    
    public static void findChildren(ArrayList<LinesContainer> linesContainers, String methodRootName, ArrayList<OFGelement> children){
        for (LinesContainer linesContainer : linesContainers) {
            if(!linesContainer.isAnalyzed()){
                for (int i = 0; i < linesContainer.getMethodsLines().size(); i++) {
                    Line methodLine = linesContainer.getMethodsLines().get(i);
                    String methodLineName = findPattern(methodLine.getLineOfCode(), METHOD_DECLARATION, 4);
                    if(methodRootName.equals(methodLineName)){
                        int bottomIndex = methodLine.getIndex();
                        int topIndex = 0;
                        if(i + 1 < linesContainer.getMethodsLines().size()){
                            Line methodLineMaximun = linesContainer.getMethodsLines().get(i + 1);
                            topIndex = methodLineMaximun.getIndex();
                        } else {
                            topIndex = linesContainer.getNumberOfLines();
                        }
                        defineOFGChildren(linesContainer, children, bottomIndex, topIndex, methodLine);//reutilizar para seguir conformando el ofg
                        linesContainer.setAnalyzed(true);
                        //break;
                    }
                }
            }
        }
    }
    
    public static OFGelement defineOFGRoot(ArrayList<LinesContainer> linesContainers, Line root){
        String packageName = "";
        String className = "";
        String methodName = "";
        OFGelement ofgElement = null;
        for (LinesContainer linesContainer : linesContainers) {
            for (Line ObjectLine : linesContainer.getObjectMethodInvocationLines()) {
                if(ObjectLine.getLineOfCode().equals(root.getLineOfCode())){
                    packageName = findPattern(linesContainer.getPackageLine().getLineOfCode(), PACKAGE_DECLARATION, 1);
                    className = findPattern(linesContainer.getClassLine().getLineOfCode(), CLASS_DECLARATION, 2);
                    int lastIndex = 0;
                    for (Line methodLine : linesContainer.getMethodsLines()) {
                        if(methodLine.getIndex()>lastIndex && methodLine.getIndex()<root.getIndex()){
                            lastIndex = methodLine.getIndex();
                            methodName = findPattern(methodLine.getLineOfCode(), METHOD_DECLARATION, 4);
                        } 
                    }
                    ofgElement = new OFGelement(true, root, packageName, className, methodName);
                    linesContainer.setAnalyzed(true);
                }
            }
        }
        return ofgElement;
    }
    
    public static void defineOFGChildren(LinesContainer linesContainer, ArrayList<OFGelement> children, int bottomIndex, int topIndex, Line methodLine){
        String packageName = findPattern(linesContainer.getPackageLine().getLineOfCode(), PACKAGE_DECLARATION, 1);
        String className = findPattern(linesContainer.getClassLine().getLineOfCode(), CLASS_DECLARATION, 2);
        String methodName = findPattern(methodLine.getLineOfCode(), METHOD_DECLARATION, 4);
        insertChildren(children, linesContainer.getMethodInvocationLines(), bottomIndex, topIndex, packageName, className, methodName);
        insertChildren(children, linesContainer.getObjectInstatiationLines(), bottomIndex, topIndex, packageName, className, methodName);
        insertChildren(children, linesContainer.getObjectVariableAssignationLines(), bottomIndex, topIndex, packageName, className, methodName);
        insertChildren(children, linesContainer.getObjectMethodInvocationLines(), bottomIndex, topIndex, packageName, className, methodName);
   }
    
    public static void insertChildren(ArrayList<OFGelement> children, ArrayList<Line> lines, int bottomIndex, int topIndex, String packageName, String className, String methodName){
        if(!lines.isEmpty()){
            for (Line line : lines) {
                if(line.getIndex()>bottomIndex && line.getIndex()<topIndex){
                    OFGelement child = new OFGelement(true, line, packageName, className, methodName);
                    children.add(child);
                }
            }
        }
    }
    
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
