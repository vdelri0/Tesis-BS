/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import model.Line;
import model.LineReader;
import model.OFGelement;
import model.ProjectReader;
import model.XmlConversor;
import view.MainWindow;

/**
 *
 * @author victor del rio
 */
public class Coordinator {
    MainWindow mainWindow;
    LineReader lineReader;
    ProjectReader projectReader;
    XmlConversor xmlConversor;
    
 

    /**
     * 
     * @param file
     * @return
     * @throws FileNotFoundException 
     */
    public String readFileCode(File file) throws FileNotFoundException{
        return lineReader.readFileCode(file);
    }
    
    /**
     * 
     * @param lineOfCode
     * @return 
     */
    public Line analyzeLineOfCode(String lineOfCode){
       return lineReader.analyzeLineOfCode(lineOfCode);
    }
    
    /**
     * 
     * @param root
     * @param lastFileSelected
     * @param projectFile
     * @return
     * @throws FileNotFoundException 
     */
    public OFGelement analyzeAllSourceCode( Line root, File lastFileSelected, File projectFile) throws FileNotFoundException, IOException {
       return projectReader.analizeAllSourceCode(root, lastFileSelected, projectFile);
    }
    
    /**
     * 
     * @return 
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * 
     * @param mainWindow 
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * 
     * @return 
     */
    public LineReader getLineReader() {
        return lineReader;
    }

    /**
     * 
     * @param lineReader 
     */
    public void setLineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    /**
     * 
     * @return 
     */
    public ProjectReader getProjectReader() {
        return projectReader;
    }

    /**
     * 
     * @param projectReader 
     */
    public void setProjectReader(ProjectReader projectReader) {
        this.projectReader = projectReader;
    }

    /**
     * 
     * @return 
     */
    public XmlConversor getXmlConversor() {
        return xmlConversor;
    }

    /**
     * 
     * @param xmlConversor 
     */
    public void setXmlConversor(XmlConversor xmlConversor) {
        this.xmlConversor = xmlConversor;
    }


    
}
