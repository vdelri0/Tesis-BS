/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.OFGelement;
import java.awt.Point;
import java.util.Random;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author victor
 */
public class OFGView {
    private OFGelement ofg;
    private int idCounter;
    
    /**
     * Initializes the graph scene creating all the nodes and edges.
     */
    public void initGraph(){

        OFGraphScene scene = new OFGraphScene ();
        idCounter = 1;
        String id = "Node:"+ofg.getLine().getLineOfCode()+" ("+Integer.toString(idCounter)+")";
        int rootX = 100;
        int rootY = 100;
        int x = 400;
        int y = 0;
        
        Widget root = createOFGelement(ofg, scene, id);
        defineLocation(root, rootX, rootY);
        createChildrensInOfg( ofg, rootX, rootY, x, y, scene, id);
        
        SceneSupport.show(scene.createView());
    }
    
    public void createChildrensInOfg(OFGelement ofgElement,int rootX, int rootY, int x, int y, OFGraphScene scene, String fatherId){
        if(!ofgElement.getChildren().isEmpty()){
            x = x + 400;
            y = y - rootY;
            for (OFGelement element : ofgElement.getChildren()) {
                idCounter = idCounter + 1;
                String id = "Node:"+element.getLine().getLineOfCode()+" ("+Integer.toString(idCounter)+")";
                Widget child = createOFGelement(element, scene, id);
                
                defineLocation(child, x, y);
                createRelation(scene, fatherId, id);
                y = y + rootY;
                createChildrensInOfg(element, rootX, rootY, x, y, scene, id);
            }
        }
    }
    
    public Widget createOFGelement(OFGelement element, OFGraphScene scene, String id){
        Widget widget = scene.addNode (id);
        widget.setToolTipText("-Package:"+element.getPackageName()+"   -Class:"+element.getClassName()+"   -Method:"+element.getMethodName());
        return widget;
    }
    
    public void createRelation(OFGraphScene scene, String source, String target){
        String edge = getRandomString();
        scene.addEdge(edge);
        scene.setEdgeSource(edge, source);
        scene.setEdgeTarget(edge, target);
    }
    
    public void defineLocation(Widget widget, int x, int y){
        widget.setPreferredLocation(new Point(x, y));
    }
    
    public String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    
    public OFGelement getOfg() {
        return ofg;
    }

    public void setOfg(OFGelement ofg) {
        this.ofg = ofg;
    }

    /**
     * Ejemplos que se pueden usar:
     * -Demo2 //Cuando tocas el label las letras y el fondo cambian de color.
     * -Demo4 //muestra dos label interconectados pero con una linea de orientacion!!
     * -GraphLayout //Lo mismo que huge pero con clases
     * -Huge //presenta un gran conjunto de nodos anidados a un solo punto
     * -Label //se puede hacer zoom sobre los elementos sin alterar su posicion
     * -Multiview //Permite una vista general del grafo
     * -Order //genera el grafo que necesitamos
     * -Serialization //permite crear nodos a gusto con un click
     * -tool //con click derecho abre un menu de opciones
     * -view //muestra un tooltip cuando se pasa el mouse sobre el elemento.
     * -Visible //muestra un mensaje en la parte de arriba cuando se selecciona un elemento.
     */
}
