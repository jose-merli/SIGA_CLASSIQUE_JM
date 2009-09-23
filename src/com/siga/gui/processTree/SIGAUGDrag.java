package com.siga.gui.processTree;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.dnd.*;
import javax.swing.tree.*;

public class SIGAUGDrag implements DragGestureListener, DragSourceListener
{
  	private boolean encurso=false;
  	protected DragSource sourc=null;
  	
  	protected SIGAUGDrag(JComponent tree2, int action, boolean drawIcon)
  	{
   		sourc=new DragSource();
   		sourc.createDefaultDragGestureRecognizer(tree2, action, this);
  	}
  	
  	public final void dragExit(DragSourceEvent dse)
  	{
    	DragSourceContext ctx=dse.getDragSourceContext();
    	ctx.setCursor(DragSource.DefaultCopyNoDrop);
  	}

  	public final void dragEnter(DragSourceDragEvent dsde)
  	{
    	DragSourceContext ctx=dsde.getDragSourceContext();
    	ctx.setCursor(DragSource.DefaultCopyDrop);
  	}

  	public final void dragGestureRecognized(DragGestureEvent dge)
  	{
    	try
    	{
      		SIGAUGTransferableNode transf=null;
      		encurso=true;
      		Component component=dge.getComponent();
      		
      		if (component instanceof JTree)
      		{
        		JTree tree=(JTree)component;
        		
        		if (tree.getSelectionCount()!=1)
        		{
          			TreePath[] paths = tree.getSelectionPaths();
          			Vector vec=new Vector();
          			
          			if (paths!=null)
          			{
          				int iPaths = paths.length;
          				
            			for (int j=0;j<iPaths;j++)
            			{
              				vec.add((DefaultMutableTreeNode)paths[j].getLastPathComponent());
            			}
            			
            			transf=new SIGAUGTransferableNode(vec, component);
          			}
        		}
        		
        		else
        		{
          			TreePath path = tree.getSelectionPath();
          			
          			if (path != null)
          			{
            			DefaultMutableTreeNode draggedNode =(DefaultMutableTreeNode)path.getLastPathComponent();

            			transf=new SIGAUGTransferableNode(draggedNode, component);
          			}
        		}
			}
			
			else if (component instanceof JTable)
			{
        		int column=((JTable)component).getSelectedColumn();
        		int row=((JTable)component).getSelectedRow();
        		Object dd=(((JTable)component).getModel()).getValueAt(row, column);

        		transf=new SIGAUGTransferableNode(dd, component);
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
  	
  	public final void dragOver(DragSourceDragEvent dsde) { }
 	public final void dragDropEnd(DragSourceDropEvent dsde) { }
  	public final void dropActionChanged(DragSourceDragEvent dsde) { }
}