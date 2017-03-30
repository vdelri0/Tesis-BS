/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Coordinator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class will help to converse the OFG into a XML format
 * @author victor del rio
 */
public class XmlConversor {
    
    
    public Coordinator coordinator;

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
    
    public static void createXmlDocument(OFGelement ofgRoot){
        String xmlString = createXMLOFG(ofgRoot);
        String[] xmlSplit = xmlString.split("\n");
        for (int i = 0; i < xmlSplit.length; i++) {
            System.out.println("#"+i+xmlSplit[i]);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document document = builder.parse( new InputSource( new StringReader( xmlString ) ) );  
            DOMSource source = new DOMSource(document);
            FileWriter writer = new FileWriter(new File("tmp/output.xml"));
            StreamResult result = new StreamResult(writer);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {  
            e.printStackTrace();  
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * Este metodo crea un String xml a partir de un OFGelement.
     * @param ofgRoot
     * @return 
     */
    public static String createXMLOFG(OFGelement ofgRoot){
       String lineJump = "\n";
       String tab = "\t";
       String doubleTab = "\t\t";
        
        
        String ofg = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n<!--comentario-->";
        ofg = ofg + tab + lineJump;
        ofg = ofg + tab + "<OfgElement>" + lineJump;
        ofg = ofg + doubleTab + "<Line>"+ofgRoot.getLine().getLineOfCode().replaceAll("(\\r|\\n)", "")+"</Line>" + lineJump ;
        ofg = ofg + doubleTab + "<TypeOfLine>"+ofgRoot.getLine().getType()+"</TypeOfLine>" + lineJump ;
        ofg = ofg + doubleTab + "<Package>"+ofgRoot.getPackageName()+"</Package>" + lineJump;
        ofg = ofg + doubleTab + "<Class>"+ofgRoot.getClassName()+"</Class>" + lineJump;
        ofg = ofg + doubleTab + "<Method>"+ofgRoot.getMethodName()+"</Method>" + lineJump;
        ofg = ofg + doubleTab + "<Children>" + lineJump;
        
        ofg = ofg + createChildrensXML(ofgRoot, tab, doubleTab, lineJump);
        
        ofg = ofg + doubleTab + "</Children>" + lineJump;
        ofg = ofg + tab + "</OfgElement>";
        return ofg;
    }
    
    /**
     * Este metodo se llama de forma recursiva para generar a los hijos del xml.
     * @param ofgElement
     * @param tab
     * @param doubleTab
     * @param lineJump
     * @return 
     */
    public static String createChildrensXML(OFGelement ofgElement, String tab, String doubleTab, String lineJump){
        String childrens = "";
        tab = tab + "\t";
        doubleTab = doubleTab + "\t\t";
        if(!ofgElement.getChildren().isEmpty()){
            for (OFGelement element : ofgElement.getChildren()) {
                childrens = childrens + tab + doubleTab + "<OfgElement>" + lineJump;
                childrens = childrens + doubleTab + doubleTab + "<Line>"+element.getLine().getLineOfCode().replaceAll("(\\r|\\n)", "")+"</Line>" + lineJump;
                childrens = childrens + doubleTab + doubleTab + "<TypeOfLine>"+element.getLine().getType()+"</TypeOfLine>" + lineJump ;
                childrens = childrens + doubleTab + doubleTab + "<Package>"+element.getPackageName()+"</Package>" + lineJump;
                childrens = childrens + doubleTab + doubleTab + "<Class>"+element.getClassName()+"</Class>" + lineJump;
                childrens = childrens + doubleTab + doubleTab + "<Method>"+element.getMethodName()+"</Method>" + lineJump;
                childrens = childrens + doubleTab + doubleTab + "<Children>" + lineJump;
                childrens = childrens + createChildrensXML(element, tab, doubleTab, lineJump);
                childrens = childrens + doubleTab + doubleTab + "</Children>" + lineJump;
                childrens = childrens + tab + doubleTab + "</OfgElement>" + lineJump;
            }
        }
        
        return childrens;
    }
}
