package com.siga.gui.processTree;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.dnd.*;
import javax.swing.tree.*;

public class SIGADrag implements DragGestureListener,DragSourceListener
{
  	private boolean encurso=false;
  	protected DragSource sourc=null;
  	
  	protected SIGADrag(JComponent tree2, int action, boolean drawIcon)
  	{
   		sourc=new DragSource();
   		sourc.createDefaultDragGestureRecognizer(tree2, action, this);
  	}

  	public final void dragOver(DragSourceDragEvent dsde) { }
  	
  	public final void dragDropEnd(DragSourceDropEvent dsde) { }
  	
  	public final void dragExit(DragSourceEvent dse)
  	{
    	DragSourceContext ctx=dse.getDragSourceContext();
    	ctx.setCursor(DragSource.DefaultCopyNoDrop);
  	}
  	
  	public final void dropActionChanged(DragSourceDragEvent dsde) { }
  	public final void dragEnter(DragSourceDragEvent dsde)
  	{
    	DragSourceContext ctx=dsde.getDragSourceContext();
    	ctx.setCursor(DragSource.DefaultCopyDrop);
  	}

  	public final void dragGestureRecognized(DragGestureEvent dge)
  	{
    	try
    	{
      		SIGAPTransferableNode transf=null;
      		encurso=true;
      		Component component=dge.getComponent();
      		
      		if (component instanceof JTree)
      		{
        		JTree tree=(JTree)component;
        		
        		if (tree.getSelectionCount()!=1 )
        		{
          			TreePath[] paths = tree.getSelectionPaths();
          			Vector vec=new Vector();
          			
          			if (paths!=null)
          			{
            			for (int j=0;j<paths.length;j++)
            			{
              				vec.add((DefaultMutableTreeNode)paths[j].getLastPathComponent());
            			}
            			
            			transf=new SIGAPTransferableNode(vec,component);
          			}
        		}
        		
        		else
        		{
          			TreePath path = tree.getSelectionPath();
          			
          			if (path != null)
          			{
            			DefaultMutableTreeNode draggedNode =(DefaultMutableTreeNode)path.getLastPathComponent();
            			transf=new SIGAPTransferableNode(draggedNode,component);
          			}
        		}
			}
			
			else if (component instanceof JTable)
			{
        		int column=((JTable)component).getSelectedColumn();
        		int row=((JTable)component).getSelectedRow();
        		Object dd=(((JTable)component).getModel()).getValueAt(row,column);
        		transf=new SIGAPTransferableNode(dd,component);
			}
			
			if (transf!=null)
			{
         		sourc.startDrag(dge, DragSource.DefaultCopyNoDrop , transf, this);
      		}

		}
		
		catch (Exception e)
		{
      		e.printStackTrace();
    	}
  	}
}