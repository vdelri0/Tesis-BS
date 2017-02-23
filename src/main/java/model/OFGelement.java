/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author victor
 */
public class OFGelement {
    private boolean root;
    private Line line;
    private String packageName;
    private String className;
    private String methodName;
    private ArrayList<OFGelement> children;
    private LinesContainer linesContainer;
    private File file;

    public OFGelement(boolean root, Line line, String packageName, String className, String methodName, LinesContainer linesContainer, File file) {
        this.root = root;
        this.line = line;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.children = new ArrayList<OFGelement>();
        this.linesContainer =  linesContainer;
        this.file = file;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ArrayList<OFGelement> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<OFGelement> children) {
        this.children = children;
    }

    public LinesContainer getLinesContainer() {
        return linesContainer;
    }

    public void setLinesContainer(LinesContainer linesContainer) {
        this.linesContainer = linesContainer;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
}
