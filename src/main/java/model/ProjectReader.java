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
import static model.ConstantsManager.METHOD_DECLARATION;
import static model.ConstantsManager.OBJECT_METHOD_INVOCATION;
import static model.ConstantsManager.PACKAGE_DECLARATION;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author victor
 */
public class ProjectReader {
    private LineReader lineReader = new LineReader();
    ArrayList<File> javaFiles = new ArrayList<File>();
    ArrayList<File> directoryFiles = new ArrayList<File>();
    ArrayList<JavaFileAnalized> javaFilesAnalized = new ArrayList<JavaFileAnalized>();
    private boolean methodFound = false; //indica si se encontro el metodo buscando dentro de la misma clase, paquete o en un paquete diferente.
    
    
    /**
     * Toma el archivo y la sentencia escogida para crear el grafo de flujo de objetos 
     * @param file
     * @param root
     * @param lastFileSelected
     * @return
     * @throws FileNotFoundException 
     */
    public OFGelement analizeAllSourceCode(Line root, File lastFileSelected) throws FileNotFoundException, IOException{
        String packageName = "";
        File lastFileSelectedParent = lastFileSelected.getParentFile();
        JavaFileAnalized javaFileAnalized = readJavaFile(lastFileSelected); //obtiene el archivo java analizado que contiene el primer nodo del ofg.
        javaFilesAnalized.add(javaFileAnalized); //Añade el ultimo archivo java analizado a la lista de archivos analizados.
        LinesContainer linesContainer = organizeLinesOfCode(javaFileAnalized); //obtiene el objeto linesContainer, el cual posee las lineas de codigo clasificadas segun su tipo.
        OFGelement ofgRoot = OFGconverter.defineTypeOfOFGElement(linesContainer, root, true, lastFileSelected);//Aqui debemos pasar el objeto a un metodo para determinar que clase estamos buscando y realizar el mismo proceso de nuevo hasta que no halla mas elementos del ofg.
        return  createOfg(ofgRoot, linesContainer, lastFileSelectedParent, packageName);
    }
    
    
    public OFGelement createOfg(OFGelement ofgElement, LinesContainer linesContainer, File file, String packageName) throws FileNotFoundException, IOException{
    String methodName = OFGconverter.findPattern(ofgElement.getLine().getLineOfCode(),OBJECT_METHOD_INVOCATION, 3);
        //De aqui en adelante pueden existir 3 casos: 
        findChildrensInJavaFile(linesContainer, methodName, ofgElement, file);//1. Que el metodo llamado se encuentre dentro de la misma clase.
        
        if(!methodFound){//2. Que el metodo llamado se encuentre en otra clase dentro del mismo paquete.
            if(file != null){
            navigateFolders(file);
            packageName = OFGconverter.findPattern(linesContainer.getPackageLine().getLineOfCode(), PACKAGE_DECLARATION, 1);
                if(!javaFiles.isEmpty()){
                    for (File javaFile : javaFiles) {
                        JavaFileAnalized auxJavaFileAnalized = readJavaFile(javaFile); //obtiene el archivo java analizado que contiene el primer nodo del ofg.
                        LinesContainer auxLinesContainer = organizeLinesOfCode(auxJavaFileAnalized); //obtiene el objeto linesContainer, el cual posee las lineas de codigo clasificadas segun su tipo.
                        findChildrensInJavaFile(auxLinesContainer, methodName, ofgElement, javaFile);
                    }
                }
            }
        } 
        
        if(!methodFound){//3. Que el metodo llamado se encuentre en otra clase fuera del paquete de la clase.
            boolean treeFlag = true;
            ArrayList<File> newDirectoryFiles; //nuevo array de folders que va a recibir al global.
            ArrayList<File> replaceDirectoryFiles; //Este se va a usar para el reemplazo en el ciclo.
            while(treeFlag){
                newDirectoryFiles = directoryFiles; //pasamos los valores del array global de folders a uno local
                directoryFiles.clear(); //Borramos los elementos del del array global, ya que fueron usados para el caso 2.
                if(!newDirectoryFiles.isEmpty()){
                    for(File directoryFile: newDirectoryFiles){
                        navigateFolders(directoryFile); //Tomamos las carpetas usadas en el caso 2, pero en vez de usarlas para encontrar archivos, las usamos para encontrar sus carpetas.
                        replaceDirectoryFiles = directoryFiles;
                        directoryFiles.clear();
                        findJavaFiles(packageName, methodName, ofgElement, replaceDirectoryFiles);
                    }
                } else { treeFlag = false; }
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
     * @param methodRootName
     * @param ofgRoot 
     */
    public void findChildrensInJavaFile(LinesContainer linesContainer, String methodRootName, OFGelement ofgRoot, File file) throws IOException{
        for(int i = 0; i < linesContainer.getMethodsLines().size(); i++){ //Buscamos todas las lineas de metodos de la clase.
            Line methodLine = linesContainer.getMethodsLines().get(i);
            String methodName = OFGconverter.findPattern(methodLine.getLineOfCode(),METHOD_DECLARATION, 4); //Sacamos el nombre del metodo que estamos comparando.
            if(methodName.equals(methodRootName)){ //comparamos si coinciden
                /*Aqui debemos colocar la variable para indicar que se cumplio este caso.*/
                if(i<linesContainer.getMethodsLines().size()-1){//En caso de que i no sea el ultimo elemento del array
                    findChildrensBetweenMethods(ofgRoot, linesContainer, methodLine, i, file);
                } else {//En caso de que i sea el ultimo elemento del array
                    findChildrensAfterLastMethod(ofgRoot, linesContainer, methodLine, file);
                }
                methodFound = true;
            }
        }
        if(methodFound){
            methodFound = false;
            for(OFGelement ofgElement: ofgRoot.getChildren()){
                createOfg(ofgElement, linesContainer, file, ofgElement.getPackageName());
            }
        }
    }
    
    
    public void findJavaFiles(String packageName, String methodRootName, OFGelement ofgElement, ArrayList<File> directoryFiles) throws FileNotFoundException, IOException{
       
        ArrayList<File> directoryJavaFiles = new ArrayList<File>() ;
        if(!directoryFiles.isEmpty()){
            for (File directoryFile : directoryFiles) { //Recorre todos los directorios existentes en la ruta del archivo principal.
                directoryJavaFiles = new ArrayList<File>();
                String path = directoryFile.getAbsolutePath();
                String folderNameDirectoryFile = path.substring(path.lastIndexOf("\\") + 1, path.length());
                if(folderNameDirectoryFile.equals(packageName)){
                    /*Version modificada de navigateFolders*/
                    String[] names = directoryFile.list(); //obtiene todas las carpetas hijo de directoryFile.
                    for (String name : names) { //recorre cada una de las carpetas hijo.
                        String ext = FilenameUtils.getExtension(directoryFile.getAbsolutePath() + "\\" + name);
                        File subDirectoryFile = new File(directoryFile.getAbsolutePath() + "\\" + name);
                        if(!subDirectoryFile.isDirectory() && "java".equals(ext)){
                            directoryJavaFiles.add(subDirectoryFile);
                        }
                    }
                }
            }
            if(!directoryJavaFiles.isEmpty()){
                for (File directoryJavaFile : directoryJavaFiles) {
                    JavaFileAnalized directoryJavaFileAnalized = readJavaFile(directoryJavaFile); //obtiene el archivo java analizado que contiene el primer nodo del ofg.
                    LinesContainer directoryLinesContainer = organizeLinesOfCode(directoryJavaFileAnalized); //obtiene el objeto linesContainer, el cual posee las lineas de codigo clasificadas segun su tipo.
                    findChildrensInJavaFile(directoryLinesContainer, methodRootName, ofgElement, directoryJavaFile);
                }
                methodFound = true;
            } else { 
                System.out.println("No existen otras clases dentro del paquete para referenciar a los nuevos nodos del ofg.");
            }
            
        } else { 
            System.out.println("No existen otros paquetes en la ruta del archivo principal.");
        }
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
     */
    public void navigateFolders(File file){
        if(file.isDirectory()){
            String[] names = file.list(); //obtiene todas las carpetas hijo de file.
            for(String name: names){ //recorre cada una de las carpetas hijo.
                String ext = FilenameUtils.getExtension(file.getAbsolutePath() + "\\" + name);
                File subFile = new File(file.getAbsolutePath() + "\\" + name);
                if(!subFile.isDirectory() && "java".equals(ext)){
                    javaFiles.add(subFile);
                } else if(subFile.isDirectory()){
                    directoryFiles.add(subFile);
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
