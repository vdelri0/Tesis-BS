/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import static model.ConstantsManager.METHOD_DECLARATION;
import static model.ConstantsManager.OBJECT_METHOD_INVOCATION;
import org.apache.commons.io.FilenameUtils;

/**
 * This class performs all the processes of analysis of the structure of the project.
 * @author victor
 */
public class ProjectReader {
    private LineReader lineReader = new LineReader();
    private ArrayList<File> javaFiles = new ArrayList<File>();
    private ArrayList<File> directoryFiles = new ArrayList<File>();
    private CopyOnWriteArrayList<LinesContainer> containersOfAnalized = new CopyOnWriteArrayList<LinesContainer>(); //Contiene todos los archivos desglozados
    private CopyOnWriteArrayList<File> filesAnalized = new CopyOnWriteArrayList<File>();
    
    
    /**
     * Take the file and the chosen statement to create the object flow graph.
     * @param root
     * @param lastFileSelected
     * @param projectFile
     * @return
     * @throws FileNotFoundException 
     */
    public OFGelement analizeAllSourceCode(Line root, File lastFileSelected, File projectFile) throws FileNotFoundException, IOException{
        String packageName = "";
        File lastFileSelectedParent = lastFileSelected.getParentFile();
        JavaFileAnalized javaFileAnalized = readJavaFile(lastFileSelected); 
        LinesContainer linesContainer = organizeLinesOfCode(javaFileAnalized); 
        filesAnalized.add(lastFileSelected);
        linesContainer.setFile(lastFileSelected);
        containersOfAnalized.add(linesContainer);
        OFGelement ofgRoot = OFGconverter.defineTypeOfOFGElement(linesContainer, root, true, lastFileSelected);
        navigateFolders(projectFile, true);
        ofgRoot = createOfg(ofgRoot, linesContainer, lastFileSelectedParent, projectFile, packageName);
        javaFiles.clear();
        filesAnalized.clear();
        containersOfAnalized.clear();
        directoryFiles.clear();
        return  ofgRoot;
    }
    
    /**
     * It breaks the project into files to which it performs a process to reach the ofg.
     * @param ofgElement
     * @param linesContainer
     * @param lastFileSelected
     * @param projectFile
     * @param packageName
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public OFGelement createOfg(OFGelement ofgElement, LinesContainer linesContainer, File lastFileSelected, File projectFile, String packageName) throws FileNotFoundException, IOException{
        boolean flag;
        flag = findChildrensInJavaFile(linesContainer, ofgElement.getLine().getLineOfCode(), ofgElement, lastFileSelected, projectFile);
        if(!flag){
            if(!javaFiles.isEmpty()){
                for(File javaFile: javaFiles){
                    if(!filesAnalized.contains(javaFile)){
                    JavaFileAnalized auxJavaFileAnalized = readJavaFile(javaFile); 
                    LinesContainer auxLinesContainer = organizeLinesOfCode(auxJavaFileAnalized); 
                    auxLinesContainer.setFile(javaFile);
                    findChildrensInJavaFile(auxLinesContainer, ofgElement.getLine().getLineOfCode(), ofgElement, javaFile, projectFile);
                    filesAnalized.add(javaFile);
                    containersOfAnalized.add(auxLinesContainer);
                    } else {
                        for(LinesContainer auxLinesContainer: containersOfAnalized){
                            if(auxLinesContainer.getFile().equals(javaFile)){
                                findChildrensInJavaFile(auxLinesContainer, ofgElement.getLine().getLineOfCode(), ofgElement, javaFile, projectFile);
                            }
                        }
                    }
                }
            } 
        }
        return ofgElement;
    }
    
    
    /**
     * This method looks for expressions that are valid to add to the ofg, which are between two methods.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine
     * @param index
     * @param file
     */
    public void findChildrensBetweenMethods(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, int index, File file){
        Line nextMethodLine = linesContainer.getMethodsLines().get(index+1);
        objectMethodInvocationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
        methodInvocationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
        objectInstantiationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
        objectVariableAssignationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
    }
    
    /**
     * This method looks for all invocations of objects to methods, which are between methods
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     * @param file 
     */
    public void objectMethodInvocationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line objectMethodInvocationLine: linesContainer.getObjectMethodInvocationLines()){
            if(objectMethodInvocationLine.getIndex() > methodLine.getIndex() && objectMethodInvocationLine.getIndex() < nextMethodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, objectMethodInvocationLine, false, file);
            }
        }
    }
    
    /**
     * This method looks for all invocations of methods, which are between methods.
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     * @param file 
     */
    public void methodInvocationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line methodInvocationLine: linesContainer.getMethodInvocationLines()){ 
            if(methodInvocationLine.getIndex() > methodLine.getIndex() && methodInvocationLine.getIndex() < nextMethodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, methodInvocationLine, false, file);
            }
        }
    }
    
    /**
     * This method looks for all instantiations of objects, which are between methods.
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     * @param file 
     */
    public void objectInstantiationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line objectInstantiationLine: linesContainer.getObjectInstantiationLines()){
            if(objectInstantiationLine.getIndex() > methodLine.getIndex() && objectInstantiationLine.getIndex() < nextMethodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, objectInstantiationLine, false, file);
            }
        }
    }
    
    /**
     * This method looks for all assignments of variables to objects, which are between methods.
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     * @param file 
     */
    public void objectVariableAssignationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line ObjectVariableAssignationLine: linesContainer.getObjectVariableAssignationLines()){
            if(ObjectVariableAssignationLine.getIndex() > methodLine.getIndex() && ObjectVariableAssignationLine.getIndex() < nextMethodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, ObjectVariableAssignationLine, false, file);
            }
        }
    }
    
    /**
     * This method looks for expressions that are valid to add to the ofg, in the last method.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine
     * @param file
     */
    public void findChildrensAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        objectMethodInvocationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        methodInvocationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        objectInstantiationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        objectVariableAssignationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        
    }
    
    /**
     * This method looks for all invocations of objects to methods, which are found after the last method.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     * @param file 
     */
    public void objectMethodInvocationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line objectMethodInvocationLine: linesContainer.getObjectMethodInvocationLines()){
            if(objectMethodInvocationLine.getIndex() > methodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, objectMethodInvocationLine, false, file);
            }
        }
    }
    
    /**
     * This method searches for all invocations of methods, which are found after the last method.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     * @param file 
     */
    public void methodInvocationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line methodInvocationLine: linesContainer.getMethodInvocationLines()){
            if(methodInvocationLine.getIndex() > methodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, methodInvocationLine, false, file);
            }
        }
    }
    
    /**
     * This method looks for all instantiations of objects, which are found after the last method.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     * @param file 
     */
    public void objectInstantiationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line objectInstantiationLine: linesContainer.getObjectInstantiationLines()){
            if(objectInstantiationLine.getIndex() > methodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, objectInstantiationLine, false, file);
            }
        }
    }
    
    /**
     * This method looks for all assignments of variables to objects, which are found after the last method.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     * @param file 
     */
    public void objectVariableAssignationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line ObjectVariableAssignationLine: linesContainer.getObjectVariableAssignationLines()){
            if(ObjectVariableAssignationLine.getIndex() > methodLine.getIndex()){
                assignChild(ofgRoot, linesContainer, ObjectVariableAssignationLine, false, file);
            }
        }
    }
    
    /**
     * This method takes two ofgelements and validates than 
     * @param ofgRoot
     * @param ofgElement
     * @return 
     */
    public boolean validateDuplicity(OFGelement ofgRoot, OFGelement ofgElement){
        boolean validate = false;
        if(!ofgRoot.getChildren().isEmpty()){
            for(OFGelement rootChildren: ofgRoot.getChildren()){
                if(rootChildren.getLine().getLineOfCode().equals(ofgElement.getLine().getLineOfCode()) && rootChildren.getMethodName().equals(ofgElement.getMethodName()) && rootChildren.getClassName().equals(ofgElement.getClassName()) && rootChildren.getPackageName().equals(ofgElement.getPackageName())){
                    validate = true;
                }
            }
        }
        return validate;
    }
    
    /**
     * This method assigns the child ofgElement to the root ofgElement after confirming it's not duplicated.
     * @param ofgRoot
     * @param linesContainer
     * @param line
     * @param rootNode
     * @param file
     */
    public void assignChild(OFGelement ofgRoot,LinesContainer linesContainer, Line line, boolean rootNode, File file){
        OFGelement rootChild = OFGconverter.defineTypeOfOFGElement(linesContainer, line, false, file);
        if(!validateDuplicity(ofgRoot, rootChild)){
            ofgRoot.getChildren().add(rootChild);
        }
    }
    
    /**
     * Find all the children that are inside a java file. (Already broken in a LinesContainer)
     * @param linesContainer
     * @param objectMethodInvocation
     * @param lastFileSelected
     * @param projectFile
     * @param ofgRoot 
     * @return  
     * @throws java.io.IOException 
     */
    public boolean findChildrensInJavaFile(LinesContainer linesContainer, String objectMethodInvocation, OFGelement ofgRoot, File lastFileSelected, File projectFile) throws IOException{
        boolean flag = false;
        boolean childrenFound = false;
        String methodRootName = OFGconverter.findPattern(objectMethodInvocation,OBJECT_METHOD_INVOCATION, 3);
        for(int i = 0; i < linesContainer.getMethodsLines().size(); i++){ 
            Line methodLine = linesContainer.getMethodsLines().get(i);
            String methodName = OFGconverter.findPattern(methodLine.getLineOfCode(),METHOD_DECLARATION, 3); 
            if(methodName.equals(methodRootName)){ 
                if(i<linesContainer.getMethodsLines().size()-1){
                    findChildrensBetweenMethods(ofgRoot, linesContainer, methodLine, i, lastFileSelected);
                } else {
                    findChildrensAfterLastMethod(ofgRoot, linesContainer, methodLine, lastFileSelected);
                }
                Iterator<OFGelement> iterator = ofgRoot.getChildren().iterator();
                while(iterator.hasNext()){
                    OFGelement ofgElementIterator = iterator.next();
                    String iteratorLineOfCode = ofgElementIterator.getLine().getLineOfCode();
                    String iteratorTypeOfLine = ofgElementIterator.getLine().getType();
                    String iteratorMethodName = ofgElementIterator.getMethodName();
                    String pattern = OFGconverter.findPattern(iteratorLineOfCode,OBJECT_METHOD_INVOCATION, 3);
                    if(objectMethodInvocation.equals(iteratorLineOfCode)){
                        iterator.remove();
                    } else if(pattern != null && "objectMethodInvocation".equals(iteratorTypeOfLine)){
                        if(iteratorMethodName.equals(pattern)){
                            iterator.remove();
                        }
                    }
                }
                childrenFound = true;
            }

        }
        if(!ofgRoot.getChildren().isEmpty() && childrenFound){
            flag = true;
            for(OFGelement ofgElement: ofgRoot.getChildren()){
                if("objectMethodInvocation".equals(ofgElement.getLine().getType())){
                    createOfg(ofgElement, linesContainer, lastFileSelected, projectFile, ofgElement.getPackageName());
                }
            }
        }
        return flag;
    }
    
    /**
     * Unbind the javaFileAnalized object, taking only the lines belonging to a type, to make it a class object
     * in the LinesContainer.
     * @param javaFileAnalized
     * @return 
     */
    public LinesContainer organizeLinesOfCode(JavaFileAnalized javaFileAnalized){
        LinesContainer linesContainer = new LinesContainer();
            int numberOfLines = 0;
            for (Line line : javaFileAnalized.getLines()) {
                if(line.isState()){
                    OFGconverter.sortOutLineOfCode(linesContainer, line);
                }
                numberOfLines++;
            }
            linesContainer.setNumberOfLines(numberOfLines);
            return linesContainer;
    }
    
    

    
    /**
     * Takes a file and goes over his folders finding java files and other directories, adding them to global arrays.
     * @param file 
     * @param cycle 
     */
    public void navigateFolders(File file, boolean cycle){
        if(file.isDirectory()){
            String[] names = file.list(); //obtiene todas las carpetas hijo de file.
            for(String name: names){ //recorre cada una de las carpetas hijo.
                String ext = FilenameUtils.getExtension(file.getAbsolutePath() + "\\" + name);
                File subFile = new File(file.getAbsolutePath() + "\\" + name);
                if(!subFile.isDirectory() && "java".equals(ext)){
                    javaFiles.add(subFile);
                } else if(subFile.isDirectory()){
                    directoryFiles.add(subFile);
                    if(cycle){
                        navigateFolders(subFile, cycle);
                    }
                }
            }
        }
    }
    
    /**
     * Take a java code file and break it down by analyzing each of its lines to make it a JavaFileAnalized object.
     * @return
     * @throws FileNotFoundException 
     */
    private JavaFileAnalized readJavaFile(File file) throws FileNotFoundException {
        Line line;
        ArrayList<Line> lines;
        JavaFileAnalized javaFileAnalized = new JavaFileAnalized();
        lines = new ArrayList<Line>();
        javaFileAnalized.setFile(file);
        String code = lineReader.readFileCode(file);
        String[] codeSplit = lineReader.splitFileCode(code);
        for (int i = 0; i < codeSplit.length; i++) {
           line = lineReader.analyzeLineOfCode(codeSplit[i]);
           line.setIndex(i);
           lines.add(line);
        }
        javaFileAnalized.setLines(lines);
        
        return javaFileAnalized;
    }
    
}
