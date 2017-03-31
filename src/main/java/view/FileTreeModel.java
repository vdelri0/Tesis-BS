/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

class FileTreeModel implements TreeModel {
    private final ArrayList<TreeModelListener>  mListeners  = new ArrayList();
    private final MyFile                        mFile;

    public FileTreeModel(final MyFile pFile) {
        mFile = pFile;
    }
    @Override public Object getRoot() {
        return mFile;
    }
    @Override public Object getChild(final Object pParent, final int pIndex) {
        return ((MyFile) pParent).listFiles()[pIndex];
    }
    @Override public int getChildCount(final Object pParent) {
        return ((MyFile) pParent).listFiles().length;
    }
    @Override public boolean isLeaf(final Object pNode) {
        return !((MyFile) pNode).isDirectory();
    }
    @Override public void valueForPathChanged(final TreePath pPath, final Object pNewValue) {
        final MyFile oldTmp = (MyFile) pPath.getLastPathComponent();
        final File oldFile = oldTmp.getFile();
        final String newName = (String) pNewValue;
        final File newFile = new File(oldFile.getParentFile(), newName);
        oldFile.renameTo(newFile);
        System.out.println("Renamed '" + oldFile + "' to '" + newFile + "'.");
        reload();
    }
    @Override public int getIndexOfChild(final Object pParent, final Object pChild) {
        final MyFile[] files = ((MyFile) pParent).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i] == pChild) return i;
        }
        return -1;
    }
    @Override public void addTreeModelListener(final TreeModelListener pL) {
        mListeners.add(pL);
    }
    @Override public void removeTreeModelListener(final TreeModelListener pL) {
        mListeners.remove(pL);
    }


    public void reload() {
        final int n = getChildCount(getRoot());
        final int[] childIdx = new int[n];
        final Object[] children = new Object[n];

        for (int i = 0; i < n; i++) {
            childIdx[i] = i;
            children[i] = getChild(getRoot(), i);
        }

        fireTreeStructureChanged(this, new Object[] { getRoot() }, childIdx, children);
    }

    protected void fireTreeStructureChanged(final Object source, final Object[] path, final int[] childIndices, final Object[] children) {
        final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (final TreeModelListener l : mListeners) {
            l.treeStructureChanged(event);
        }
    }
}

