/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author victor
 */
public class TreeNodeOFG<T> implements Iterable<TreeNodeOFG<T>> {

    private T data;
    private TreeNodeOFG<T> parent;
    private List<TreeNodeOFG<T>> children;

    public TreeNodeOFG(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNodeOFG<T>>();
    }

    public TreeNodeOFG<T> addChild(T child) {
        TreeNodeOFG<T> childNode = new TreeNodeOFG<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public Iterator<TreeNodeOFG<T>> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNodeOFG<T> getParent() {
        return parent;
    }

    public void setParent(TreeNodeOFG<T> parent) {
        this.parent = parent;
    }

    public List<TreeNodeOFG<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeOFG<T>> children) {
        this.children = children;
    }
    
    

}
