package unitTests;


import model.ConstantsManager;
import model.KBConnector;
import model.Line;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

/**
 *
 * @author victor del rio
 */
public class KBConnectorTest {
    /**
     * Create a new knowledge base 
     */
    KBConnector conector;
    
    @Before
    public void setUp(){
        conector = new KBConnector();
        conector.connect();
    }
    
    
    @Test
    public void testPackageDeclaration() {
        String expression = "package conector;" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("packageDeclaration",analyzeLine(expression).getType()); 
        expression = "package conector.conector1;" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("packageDeclaration",analyzeLine(expression).getType()); 
    }
    
    @Test
    public void testClassDeclaration() {
        String expression = "public class conector{" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("classDeclaration",analyzeLine(expression).getType()); 
        expression = "public class conector implements superconector{" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("classDeclaration",analyzeLine(expression).getType());
        expression = "public class conector implements superconector,superconector1{" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("classDeclaration",analyzeLine(expression).getType());
        expression = "public class conector extends superconector{" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("classDeclaration",analyzeLine(expression).getType());
    }
    
    @Test
    public void testMethodDeclaration() {
        String expression = "public void testObjectInstantiation(){" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodDeclaration",analyzeLine(expression).getType()); 
        expression = "public static void testObjectInstantiation(int i){" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodDeclaration",analyzeLine(expression).getType()); 
        expression = "public static void testObjectInstantiation(int i, int b){" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodDeclaration",analyzeLine(expression).getType()); 
        expression = "public static void testObjectInstantiation(int i, int b)" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodDeclaration",analyzeLine(expression).getType());
        expression = "    public int size() {\r";
        Assert.assertEquals("methodDeclaration",analyzeLine(expression).getType());
           
    }
    
    @Test
    public void testObjectInstantiation() {
        String expression = "Conector conector = new Conector();" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectInstantiation",analyzeLine(expression).getType()); 
        expression = "Conector conector = new Conector(ofg);" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectInstantiation",analyzeLine(expression).getType()); 
        expression = "Conector conector = new Conector(ofg,ofg2);" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectInstantiation",analyzeLine(expression).getType()); 
    }
    
    @Test
    public void testObjectVariableAssignation() {
        String expression = "conector = conector2;" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectVariableAssignation",analyzeLine(expression).getType()); 
    }
    
    @Test
    public void testObjectMethodInvocation() {
        String expression = "conector.creadorDeConectores();" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType()); 
        expression = "conector.creadorDeConectores(ofg);" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType()); 
        expression = "conector.creadorDeConectores(ofg,ofg2);" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType()); 
        expression = "conector.creadorDeConectores(new ofg());" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType()); 
        expression = "conector.creadorDeConectores(new ofg(parameter1), new ofg2());" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType()); 
        expression = "conector.creadorDeConectores(\"SDFSDFSDF\", new ofg2());" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType()); 
        expression = "conector.creadorDeConectores(\"SDFSDFSDF\", new ofg2(),\"SDFLSKJDFLSKD\");" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType());
        expression = "conector.creadorDeConectores(\"SDFSDFSDF\", objeto.metodo());" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType());
        expression = "conector.creadorDeConectores(\"SDFSDFSDF\", metodo());" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocation",analyzeLine(expression).getType());
    }
    
    @Test
    public void testObjectMethodInvocationListener(){
        String expression = "editor.addPropertyChangeListener(new PropertyChangeListener() {" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("objectMethodInvocationListener", analyzeLine(expression).getType());
    }
    
    @Test
    public void testMethodInvocation() {
        String expression = "creadorDeConectores();" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodInvocation",analyzeLine(expression).getType());
        expression = "creadorDeConectores(ofg);" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodInvocation",analyzeLine(expression).getType());
        expression = "creadorDeConectores(ofg,ofg2);" + ConstantsManager.LINE_ENDING;
        Assert.assertEquals("methodInvocation",analyzeLine(expression).getType());
    }
    
    public Line analyzeLine(String lineOfCode){
        Line line = new Line();
        line.setLineOfCode(lineOfCode);
        line = (Line) conector.enterObjectInKnowledgeBase(line);
        return line;
    }
}
