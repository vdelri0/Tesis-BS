/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.KBConnector;
import model.LineReader;
import model.ProjectReader;
import model.XmlConversor;
import view.MainWindow;

/**
 *
 * @author victor del rio
 */
public class Principal {
    private MainWindow window;
    private LineReader lineReader;
    private ProjectReader projectReader;
    private XmlConversor xmlConversor;
    private Coordinator coordinator;
    private KBConnector kbconnector;
    
    public static void main(String[] args) {
        Principal principal = new Principal();
        principal.start();
    }
    
    public void start(){
        /*the objects get instanciated*/
        window = new MainWindow();
        lineReader = new LineReader();
        projectReader = new ProjectReader();
        xmlConversor = new XmlConversor();
        kbconnector = new KBConnector();
        coordinator = new Coordinator();
        
        /*We define the relations between classes*/
        window.setCoordinator(coordinator);
        lineReader.setCoordinator(coordinator);
        projectReader.setCoordinator(coordinator);
        xmlConversor.setCoordinator(coordinator);
        kbconnector.setCoordinator(coordinator);
        
        /*we define the relations with the class Coordinator*/
        coordinator.setMainWindow(window);
        coordinator.setLineReader(lineReader);
        coordinator.setProjectReader(projectReader);
        coordinator.setXmlConversor(xmlConversor);
        coordinator.setKbconnector(kbconnector);
        
        window.setVisible(true);
        coordinator.connect();
    }
}
