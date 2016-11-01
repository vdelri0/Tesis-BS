/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Coordinator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author victor
 */
public class ProjectReader {
    private Line root;
    private Coordinator coordinator;
    private LineReader lineReader = new LineReader();
    ArrayList<File> javaFiles = new ArrayList<File>();
    ArrayList<File> directoryFiles = new ArrayList<File>();
    
    public OFGelement analizeAllSourceCode(File file, Line root) throws FileNotFoundException{
        this.root = root;
        String[] names = file.list();
        for(String name: names){
            if("src".equals(name)){
                navigateFolders(new File(file.getAbsolutePath() + "\\" + name));
                if(!directoryFiles.isEmpty()){
                    navigateAgain();
                }
            } 
        }
        return readJavaFiles();
    }
    
    /**
     * Analizar si es pertinente, crear una clase contenedor que guarde registro de todas las classes que le pertenecen
     * al igual que los paquetes.!!!!!!!!!!!!!!!!
     * @param file
     * @param javaFiles 
     */
    public void navigateFolders(File file){
        String[] names = file.list();
        for(String name: names){
            String ext = FilenameUtils.getExtension(file.getAbsolutePath() + "\\" + name);
            File subFile = new File(file.getAbsolutePath() + "\\" + name);
            if(!subFile.isDirectory() && "java".equals(ext)){
                javaFiles.add(subFile);
            } else if(subFile.isDirectory()){
                directoryFiles.add(subFile);
            }
        }
    }
    //cambiar el arraylist, por que en algun punto el it va a quedar vacio mientras que el no.
    public void navigateAgain(){
        Iterator<File> it = directoryFiles.iterator();
        while(it.hasNext()){
            navigateFolders(it.next());
            it.remove();
        }
    }

    private OFGelement readJavaFiles() throws FileNotFoundException {
        Line line;
        ArrayList<Line> lines;
        JavaFileAnalized javaFileAnalized;
        ArrayList<JavaFileAnalized> javaFilesAnalized = new ArrayList<JavaFileAnalized>();
        for (File javaFile : javaFiles) {
            lines = new ArrayList<Line>();
            javaFileAnalized = new JavaFileAnalized();
            javaFileAnalized.setFile(javaFile);
            String code = lineReader.readFileCode(javaFile);
            String[] codeSplit = lineReader.splitFileCode(code);
            for (int i = 0; i < codeSplit.length; i++) {
               line = coordinator.analyzeLineOfCode(codeSplit[i]);
               line.setIndex(i);
               lines.add(line);
            }
            javaFileAnalized.setLines(lines);
            javaFilesAnalized.add(javaFileAnalized);
        }
        return OFGconverter.createOFG(javaFilesAnalized, root);
    }
    
    
    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
    
}
