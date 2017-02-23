/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author victor
 */
public class LinesContainer {
    private Line packageLine;
    private Line classLine;
    private ArrayList<Line> methodsLines = new ArrayList<Line>();
    private ArrayList<Line> objectInstatiationLines = new ArrayList<Line>();
    private ArrayList<Line> objectVariableAssignationLines = new ArrayList<Line>();
    private ArrayList<Line> objectMethodInvocationLines = new ArrayList<Line>();
    private ArrayList<Line> methodInvocationLines = new ArrayList<Line>();
    private boolean analyzed;
    private int numberOfLines;

    public Line getPackageLine() {
        return packageLine;
    }

    public void setPackageLine(Line packageLine) {
        this.packageLine = packageLine;
    }

    public Line getClassLine() {
        return classLine;
    }

    public void setClassLine(Line classLine) {
        this.classLine = classLine;
    }

    public ArrayList<Line> getMethodsLines() {
        return methodsLines;
    }

    public void setMethodsLines(ArrayList<Line> methodsLines) {
        this.methodsLines = methodsLines;
    }

    public ArrayList<Line> getObjectInstantiationLines() {
        return objectInstatiationLines;
    }

    public void setObjectInstantiationLines(ArrayList<Line> objectInstatiationLines) {
        this.objectInstatiationLines = objectInstatiationLines;
    }

    public ArrayList<Line> getObjectVariableAssignationLines() {
        return objectVariableAssignationLines;
    }

    public void setObjectVariableAssignationLines(ArrayList<Line> objectVariableAssignationLines) {
        this.objectVariableAssignationLines = objectVariableAssignationLines;
    }

    public ArrayList<Line> getObjectMethodInvocationLines() {
        return objectMethodInvocationLines;
    }

    public void setObjectMethodInvocationLines(ArrayList<Line> objectMethodInvocationLines) {
        this.objectMethodInvocationLines = objectMethodInvocationLines;
    }

    public ArrayList<Line> getMethodInvocationLines() {
        return methodInvocationLines;
    }

    public void setMethodInvocationLines(ArrayList<Line> methodInvocationLines) {
        this.methodInvocationLines = methodInvocationLines;
    }

    public boolean isAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }
    
}
