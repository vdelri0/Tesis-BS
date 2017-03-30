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
 *
 * @author victor
 */
public class ProjectReader {
    private LineReader lineReader = new LineReader();
    ArrayList<File> javaFiles = new ArrayList<File>();
    ArrayList<File> directoryFiles = new ArrayList<File>();
    CopyOnWriteArrayList<LinesContainer> containersOfAnalized = new CopyOnWriteArrayList<LinesContainer>(); //Contiene todos los archivos desglozados
    CopyOnWriteArrayList<File> filesAnalized = new CopyOnWriteArrayList<File>();
    
    
    /**
     * Toma el archivo y la sentencia escogida para crear el grafo de flujo de objetos 
     * @param file
     * @param root
     * @param lastFileSelected
     * @return
     * @throws FileNotFoundException 
     */
    public OFGelement analizeAllSourceCode(Line root, File lastFileSelected, File projectFile) throws FileNotFoundException, IOException{
        String packageName = "";
        File lastFileSelectedParent = lastFileSelected.getParentFile();
        JavaFileAnalized javaFileAnalized = readJavaFile(lastFileSelected); //obtiene el archivo java analizado que contiene el primer nodo del ofg.
        LinesContainer linesContainer = organizeLinesOfCode(javaFileAnalized); //obtiene el objeto linesContainer, el cual posee las lineas de codigo clasificadas segun su tipo.
        filesAnalized.add(lastFileSelected);
        linesContainer.setFile(lastFileSelected);
        containersOfAnalized.add(linesContainer);
        OFGelement ofgRoot = OFGconverter.defineTypeOfOFGElement(linesContainer, root, true, lastFileSelected);//Aqui debemos pasar el objeto a un metodo para determinar que clase estamos buscando y realizar el mismo proceso de nuevo hasta que no halla mas elementos del ofg.
        navigateFolders(projectFile, true);
        ofgRoot = createOfg(ofgRoot, linesContainer, lastFileSelectedParent, projectFile, packageName);
        javaFiles.clear();
        filesAnalized.clear();
        containersOfAnalized.clear();
        directoryFiles.clear();
        return  ofgRoot;
    }
    
    
    public OFGelement createOfg(OFGelement ofgElement, LinesContainer linesContainer, File lastFileSelected, File projectFile, String packageName) throws FileNotFoundException, IOException{
        
        boolean flag = false;
//        if("C:\\Users\\Lenovo\\Documents\\NetBeansProjects\\JHotDraw\\src\\draw\\AbstractCompositeFigure.java".equals(lastFileSelected.getAbsolutePath())){
//            System.err.println("C:\\Users\\Lenovo\\Documents\\NetBeansProjects\\JHotDraw\\src\\draw\\AbstractCompositeFigure.java");
//        }
//        System.err.println(lastFileSelected.getAbsolutePath());
//        System.out.println(ofgElement.getLine().getLineOfCode());
//        System.err.println(ofgElement.getLine().getType());
        flag = findChildrensInJavaFile(linesContainer, ofgElement.getLine().getLineOfCode(), ofgElement, lastFileSelected, projectFile);//1. Que el metodo llamado se encuentre dentro de la misma clase.
        if(!flag){
            if(!javaFiles.isEmpty()){
                for(File javaFile: javaFiles){
                    if(!filesAnalized.contains(javaFile)){
                    JavaFileAnalized auxJavaFileAnalized = readJavaFile(javaFile); //obtiene el archivo java analizado que contiene el primer nodo del ofg.
                    LinesContainer auxLinesContainer = organizeLinesOfCode(auxJavaFileAnalized); //obtiene el objeto linesContainer, el cual posee las lineas de codigo clasificadas segun su tipo.
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
     * Este metodo busca expresiones que sean validas para agregar al ofg, las cuales se encuentren entre dos metodos.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine
     * @param index
     */
    public void findChildrensBetweenMethods(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, int index, File file){
        Line nextMethodLine = linesContainer.getMethodsLines().get(index+1);
        objectMethodInvocationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
        methodInvocationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
        objectInstantiationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
        objectVariableAssignationsBetweenMethods(linesContainer, methodLine, nextMethodLine, ofgRoot, file);
    }
    
    /**
     * Este metodo busca todas las invocaciones de objetos a metodos, que se encuentran entre metodos
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     */
    public void objectMethodInvocationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line objectMethodInvocationLine: linesContainer.getObjectMethodInvocationLines()){
            if(objectMethodInvocationLine.getIndex() > methodLine.getIndex() && objectMethodInvocationLine.getIndex() < nextMethodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, objectMethodInvocationLine, false, file)); //Definimos el elemento del ofg y lo añadimos al elemento root.
            }
        }
    }
    
    /**
     * Este metodo busca todas las invocaciones de metodos, que se encuentran entre metodos.
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     */
    public void methodInvocationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line methodInvocationLine: linesContainer.getMethodInvocationLines()){ //Buscamos entre todas las invocaciones de methodos.
            if(methodInvocationLine.getIndex() > methodLine.getIndex() && methodInvocationLine.getIndex() < nextMethodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, methodInvocationLine, false, file));
            }
        }
    }
    
    /**
     * Este metodo busca todas las instanciaciones de objetos, que se encuentran entre metodos.
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     */
    public void objectInstantiationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line objectInstantiationLine: linesContainer.getObjectInstantiationLines()){
            if(objectInstantiationLine.getIndex() > methodLine.getIndex() && objectInstantiationLine.getIndex() < nextMethodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, objectInstantiationLine, false, file));
            }
        }
    }
    
    /**
     * Este metodo busca todas las asignaciones de variables a objetos, que se encuentran entre metodos.
     * @param linesContainer
     * @param methodLine
     * @param nextMethodLine
     * @param ofgRoot 
     */
    public void objectVariableAssignationsBetweenMethods(LinesContainer linesContainer, Line methodLine, Line nextMethodLine, OFGelement ofgRoot, File file){
        for(Line ObjectVariableAssignationLine: linesContainer.getObjectVariableAssignationLines()){
            if(ObjectVariableAssignationLine.getIndex() > methodLine.getIndex() && ObjectVariableAssignationLine.getIndex() < nextMethodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, ObjectVariableAssignationLine, false, file));
            }
        }
    }
    
    /**
     * Este metodo busca expresiones que sean validas para agregar al ofg, en el ultimo metodo.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine
     */
    public void findChildrensAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        objectMethodInvocationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        methodInvocationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        objectInstantiationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        objectVariableAssignationAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
        
    }
    
    /**
     * Este metodo busca todas las invocaciones de objetos a metodos, que se encuentran despues del ultimo metodo.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     */
    public void objectMethodInvocationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line objectMethodInvocationLine: linesContainer.getObjectMethodInvocationLines()){
            if(objectMethodInvocationLine.getIndex() > methodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, objectMethodInvocationLine, false, file));
            }
        }
    }
    
    /**
     * Este metodo busca todas las invocaciones de metodos, que se encuentran despues del ultimo metodo.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     */
    public void methodInvocationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line methodInvocationLine: linesContainer.getMethodInvocationLines()){
            if(methodInvocationLine.getIndex() > methodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, methodInvocationLine, false, file));
            }
        }
    }
    
    /**
     * Este metodo busca todas las instanciaciones de objetos, que se encuentran despues del ultimo metodo.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     */
    public void objectInstantiationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line objectInstantiationLine: linesContainer.getObjectInstantiationLines()){
            if(objectInstantiationLine.getIndex() > methodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, objectInstantiationLine, false, file));
            }
        }
    }
    
    /**
     * Este metodo busca todas las asignaciones de variables a objetos, que se encuentran despues del ultimo metodo.
     * @param ofgRoot
     * @param linesContainer
     * @param methodLine 
     */
    public void objectVariableAssignationAfterLastMethod(OFGelement ofgRoot, LinesContainer linesContainer, Line methodLine, File file){
        for(Line ObjectVariableAssignationLine: linesContainer.getObjectVariableAssignationLines()){
            if(ObjectVariableAssignationLine.getIndex() > methodLine.getIndex()){
                ofgRoot.getChildren().add(OFGconverter.defineTypeOfOFGElement(linesContainer, ObjectVariableAssignationLine, false, file));
            }
        }
    }
    
    /**
     * Busca todos los hijos que se encuentran dentro de un archivo java. (ya desglozado en un LinesContainer)
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
        String methodRootName = OFGconverter.findPattern(objectMethodInvocation,OBJECT_METHOD_INVOCATION, 2);
        for(int i = 0; i < linesContainer.getMethodsLines().size(); i++){ //Buscamos todas las lineas de metodos de la clase.
            Line methodLine = linesContainer.getMethodsLines().get(i);
            String methodName = OFGconverter.findPattern(methodLine.getLineOfCode(),METHOD_DECLARATION, 3); //Sacamos el nombre del metodo que estamos comparando.
            if(methodName.equals(methodRootName)){ //comparamos si coinciden
                /*Aqui debemos colocar la variable para indicar que se cumplio este caso.*/
                if(i<linesContainer.getMethodsLines().size()-1){//En caso de que i no sea el ultimo elemento del array
                    findChildrensBetweenMethods(ofgRoot, linesContainer, methodLine, i, lastFileSelected);
                } else {//En caso de que i sea el ultimo elemento del array
                    findChildrensAfterLastMethod(ofgRoot, linesContainer, methodLine, lastFileSelected);
                }
                Iterator<OFGelement> iterator = ofgRoot.getChildren().iterator();
                while(iterator.hasNext()){
                    OFGelement ofgElementIterator = iterator.next();
                    String iteratorLineOfCode = ofgElementIterator.getLine().getLineOfCode();
                    String iteratorTypeOfLine = ofgElementIterator.getLine().getType();
                    String iteratorMethodName = ofgElementIterator.getMethodName();
                    String pattern = OFGconverter.findPattern(iteratorLineOfCode,OBJECT_METHOD_INVOCATION, 2);
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
                createOfg(ofgElement, linesContainer, lastFileSelected, projectFile, ofgElement.getPackageName());
            }
        }
        return flag;
    }
    
    /**
     * Desgloza el objeto javaFileAnalized, tomando unicamente las lineas que pertenecen a un tipo, 
     * para convertirlo en un objeto de clase la LinesContainer.
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
     * Toma un archivo de codigo java y lo desglosa analizando cada una de sus lineas
     * para convertirlo en un objeto JavaFileAnalized.
     * @return
     * @throws FileNotFoundException 
     */
    private JavaFileAnalized readJavaFile(File file) throws FileNotFoundException {
//        System.out.println(file.getAbsolutePath());
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
