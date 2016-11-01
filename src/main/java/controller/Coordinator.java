/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import model.KBConnector;
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
    KBConnector kbconnector;
    
    public void connect(){
        kbconnector.connect();
    }

    public String readFileCode(File file) throws FileNotFoundException{
        return lineReader.readFileCode(file);
    }
    
    public Line analyzeLineOfCode(String lineOfCode){
       return lineReader.analyzeLineOfCode(lineOfCode);
    }
    
    public OFGelement analyzeAllSourceCode(File currentFile, Line root) throws FileNotFoundException {
       return projectReader.analizeAllSourceCode(currentFile, root);
    }
    
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public LineReader getLineReader() {
        return lineReader;
    }

    public void setLineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public ProjectReader getProjectReader() {
        return projectReader;
    }

    public void setProjectReader(ProjectReader projectReader) {
        this.projectReader = projectReader;
    }

    public XmlConversor getXmlConversor() {
        return xmlConversor;
    }

    public void setXmlConversor(XmlConversor xmlConversor) {
        this.xmlConversor = xmlConversor;
    }

    public KBConnector getKbconnector() {
        return kbconnector;
    }

    public void setKbconnector(KBConnector kbconnector) {
        this.kbconnector = kbconnector;
    }
    
}
