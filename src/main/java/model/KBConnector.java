/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Coordinator;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 *
 * @author victor del rio
 */
public class KBConnector {
    
    KnowledgeBuilder kbuilder;
    KnowledgeBase kbase;
    StatefulKnowledgeSession ksession;
    ExpressionLibrary library;
    Coordinator coordinator;
    
    public void connect(){
        
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("rules/lexicalAnalyzer.drl", getClass()), ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }
        
        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        ksession = kbase.newStatefulKnowledgeSession();
        createNewLibrary();
    }
    
    public void createNewLibrary(){
        library = new ExpressionLibrary();
        library.declareLibrary();
        enterObjectInKnowledgeBase(library);
    }
    
    public Object enterObjectInKnowledgeBase(Object object){
        ksession.insert(object);
        ksession.fireAllRules();
        return object;
    }
    
    public KnowledgeBuilder getKbuilder() {
        return kbuilder;
    }

    public void setKbuilder(KnowledgeBuilder kbuilder) {
        this.kbuilder = kbuilder;
    }

    public KnowledgeBase getKbase() {
        return kbase;
    }

    public void setKbase(KnowledgeBase kbase) {
        this.kbase = kbase;
    }

    public StatefulKnowledgeSession getKsession() {
        return ksession;
    }

    public void setKsession(StatefulKnowledgeSession ksession) {
        this.ksession = ksession;
    }

    public ExpressionLibrary getLibrary() {
        return library;
    }

    public void setLibrary(ExpressionLibrary library) {
        this.library = library;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
    
}
