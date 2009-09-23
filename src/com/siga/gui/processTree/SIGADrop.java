package com.siga.gui.processTree;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.dnd.*;
import javax.swing.tree.*;
import java.awt.datatransfer.*;

public class SIGADrop implements DropTargetListener
{
	private JApplet applet=null;
	private DropTarget dropTj=null;
	JComponent comp=null;

  	protected SIGADrop(JComponent tree2, int action, JApplet _applet)
  	{
    	applet=_applet;
    	comp=tree2;
    	dropTj = new DropTarget(tree2,action,this);
  	}

	public void dragExit(DropTargetEvent dte) { }
	public void dropActionChanged(DropTargetDragEvent dtde) { }
	public void dragOver(DropTargetDragEvent dtde) { }
	public void dragEnter(DropTargetDragEvent dtde) { }

  	public final void drop(DropTargetDropEvent dtde)
  	{
    	try
    	{
      		JComponent component=(JComponent)dtde.getDropTargetContext().getComponent();
      		callToExecuteDrop(dtde,component);
    	}
    	
    	catch (Exception e)
    	{
      		e.printStackTrace();

      		dtde.rejectDrop();
      		dtde.dropComplete(false);
    	}
  	}

  	private void callToExecuteDrop(DropTargetDropEvent dtde,JComponent tree) throws Exception
  	{
    	int action = dtde.getDropAction();
    	Transferable transferable = dtde.getTransferable();
    	
    	try
    	{
      		if (transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR) instanceof SIGATableObj && tree instanceof JTree)
      		{
        		if (doSIGATable(transferable,dtde,(JTree)tree,action))
        		{
          			return;
          		}
        	}
        	
        	else if (transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR) instanceof DefaultMutableTreeNode && tree instanceof JTree)
        	{
          		if (doTreeNode(transferable,dtde,(JTree)tree,action))
          		{
            		return;
            	}
          	}
          	
          	else if (transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR) instanceof DefaultMutableTreeNode && tree instanceof JTable)
          	{
            	Vector vec=new Vector();
            	vec.add(transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR));
            	SIGAPTransferableNode transNode=new SIGAPTransferableNode(vec,transferable.getTransferData(SIGAPTransferableNode.SOURCE_FLAVOR));
            	
            	if (doVector(transNode,dtde,(JTable)tree,action))
            	{
              		return;
              	}
            
            }
            
            else if (transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR) instanceof Vector && tree instanceof JTable)
            {
            	if (doVector(transferable,dtde,(JTable)tree,action))
            	{
                	return;
                }
            }
            
            dtde.rejectDrop();
            dtde.dropComplete(false);
		}
		
		catch (Exception e)
		{
      		dtde.rejectDrop();
      		dtde.dropComplete(false);
      		String msg=(String)((SIGAAppletBase)applet).literales.get(e.getMessage());
      		
      		if (msg==null)
      		{
      			msg=(String)e.getMessage();
      		}
      		
      		if (!"-1".equals(msg))
      		{
        		JOptionPane.showMessageDialog(applet, msg, "Exception",JOptionPane.WARNING_MESSAGE);
            }
		}
	}

  	public boolean updateNode(DefaultMutableTreeNode padre, Hashtable has) throws Exception
  	{
    	SIGAAccessObj obj=(SIGAAccessObj)padre.getUserObject();
    
    	if (obj==null)
    	{
      		return false;
      	}
    
    	SIGAAccessObj objNew = (SIGAAccessObj)has.get(obj.getID());
    	
    	if (objNew==null)
    	{
      		return false;
      	}

    	obj.put(SIGAAccessObj.ACCESS, objNew.get(SIGAAccessObj.ACCESS));
    	Enumeration a=padre.children();
    	
    	while (a.hasMoreElements())
    	{
      		updateNode((DefaultMutableTreeNode)a.nextElement(),has);
    	}

		return true;
	}

	public boolean canPerformAction(JComponent target,Object draggedNode,int action, Point location) throws Exception
	{
    	if (target instanceof JTree && draggedNode instanceof DefaultMutableTreeNode)
    	{
      		TreePath pathTarget = ((JTree)target).getPathForLocation(location.x, location.y);
      		
      		if (pathTarget == null)
      		{
        		((JTree)target).setSelectionPath(null);
        		
        		return false;
      		}
      		
      		((JTree)target).setSelectionPath(pathTarget);
      		DefaultMutableTreeNode parentNode =(DefaultMutableTreeNode)pathTarget.getLastPathComponent();
      		
      		if (((DefaultMutableTreeNode)draggedNode).isRoot() ||
                  parentNode == ((DefaultMutableTreeNode)draggedNode).getParent() ||
                ((DefaultMutableTreeNode)draggedNode).isNodeDescendant(parentNode))
            {
        		return false;
      		}
      		
      		else
      		{
        		return true;
      		}
		}
		
		else if (target instanceof JTree && draggedNode instanceof SIGABaseNode)
		{
      		return true;
    	}
    	
		return true;
	}

	public boolean executeDrop(JTree target, DefaultMutableTreeNode draggedNode,DefaultMutableTreeNode targetNode, int action) throws Exception
	{
    	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)draggedNode.getParent();
    	DefaultMutableTreeNode rootLocation = (DefaultMutableTreeNode)parentNode.getParent();
    	
    	if ((action & DnDConstants.ACTION_COPY_OR_MOVE)!=0)
    	{
      		SIGABaseNode objMoved=(SIGABaseNode)draggedNode.getUserObject();
      		SIGABaseNode objTo=(SIGABaseNode)targetNode.getUserObject();
      		Vector vec=objMoved.dndMovement(objTo);
      		
      		if (vec==null || vec.size()==0)
      		{
        		return false;
      		}
      		
      		if (!"OK".equalsIgnoreCase((String)vec.elementAt(0)))
      		{
        		return false;
      		}

      		DefaultMutableTreeNode rootnode=(DefaultMutableTreeNode)((DefaultTreeModel)target.getModel()).getRoot();
      		DefaultMutableTreeNode node=findObject((DefaultTreeModel)target.getModel(),rootnode,draggedNode);
      		
      		if (node==null)
      		{
        		return false;
      		}
      		
      		node.removeFromParent();
      		((SIGABaseNode)draggedNode.getUserObject()).modified=true;
      		((SIGABaseNode)node.getUserObject()).modified=true;

      		DefaultMutableTreeNode paternode=findObject((DefaultTreeModel)target.getModel(),rootnode,targetNode);

      		paternode.setAllowsChildren(true);

      		((DefaultTreeModel)target.getModel()).insertNodeInto(node,paternode,paternode.getChildCount());

      		TreePath treePath = new TreePath(node.getPath());
      		target.scrollPathToVisible(treePath);
      		target.setSelectionPath(treePath);
      		
      		return true;
		}
	
	return false;
	}

	protected DefaultMutableTreeNode findObject(DefaultTreeModel model,DefaultMutableTreeNode rootNode,DefaultMutableTreeNode nodeWanted) throws Exception
	{
    	if (((SIGABaseNode)rootNode.getUserObject()).getID().equals(((SIGABaseNode)nodeWanted.getUserObject()).getID()))
    	{
      		return rootNode;
      	}

    	Enumeration e=rootNode.children();
    	
    	while (e.hasMoreElements())
    	{
      		DefaultMutableTreeNode node=findObject(model,(DefaultMutableTreeNode )e.nextElement(),nodeWanted);
      		
      		if (node!=null)
      		{
        		return node;
        	}
    	}
    	
    	return null;
	}

	private boolean doSIGATable(Transferable transferable,DropTargetDropEvent dtde,JTree tree,int action) throws Exception
	{
    	boolean ret=true;
    	SIGATableObj onnn = (SIGATableObj)transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR);
    	Point pt = dtde.getLocation();
    	TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
    	DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();

    	DefaultMutableTreeNode padre=(DefaultMutableTreeNode)targetNode.getParent();

    	int accesso=0;
    	String access=onnn.getID();
    
    	if (access.equals(SIGABaseNode.ACCESS_DENY))
    	{
      		accesso=1;
    	}
    	
    	else if (access.equals(SIGABaseNode.ACCESS_READ))
    	{
      		accesso=2;
    	}
    	
    	else if (access.equals(SIGABaseNode.ACCESS_FULL))
    	{
      		accesso=3;
    	}
    	
    	else
    	{
      		accesso=0;
		}

		if (padre!=null)
		{
      		String accessParent=(String)((SIGABaseNode)padre.getUserObject()).get(SIGABaseNode.ACCESS);
      		int accessoP=0;
      		
      		if (accessParent.equals(SIGABaseNode.ACCESS_DENY))
      		{
        		accessoP=1;
      		}
      		
      		else if (accessParent.equals(SIGABaseNode.ACCESS_READ))
      		{
        		accessoP=2;
      		}
      		
      		else if (accessParent.equals(SIGABaseNode.ACCESS_FULL))
      		{
        		accessoP=3;
      		}
      		
      		else
      		{
        		accessoP=0;
      		}

      		if (accesso>accessoP)
      		{
        		throw new Exception(SIGAPTConstants.PATER_RESTRIC);
      		}
		}

    	Vector vec=null;
    	vec=onnn.dndMovement((SIGABaseNode)targetNode.getUserObject());

    	Enumeration children=targetNode.children();
    	
    	while (children.hasMoreElements())
    	{
      		modifyChildren((DefaultMutableTreeNode) children.nextElement(),access,accesso);
    	}
    	
    	dtde.acceptDrop(action);
    	dtde.dropComplete(true);
    
    	if (comp instanceof JTree)
    	{
      		((JTree)comp).update(((JTree)comp).getGraphics());
    	}
    	
    	return ret;
	}

	private boolean doTreeNode(Transferable transferable,DropTargetDropEvent dtde,JTree tree,int action) throws Exception
	{
    	DefaultMutableTreeNode draggedNode = (DefaultMutableTreeNode)transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR);
    	Point pt = dtde.getLocation();
    	
    	if (transferable.isDataFlavorSupported(SIGAPTransferableNode.NODE_FLAVOR) && canPerformAction(tree, draggedNode, action, pt))
    	{
      		TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
      		DefaultMutableTreeNode node = (DefaultMutableTreeNode)transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR);
      		DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode)node.getParent();
      		DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode)
          	pathTarget.getLastPathComponent();
      		
      		if (executeDrop(tree, node, targetNode, action))
      		{
        		dtde.acceptDrop(action);
        		dtde.dropComplete(true);
        		tree.updateUI();
        		tree.update(tree.getGraphics());
        		
        		return true;
      		}
    	}
    	
    	return false;
	}
  
	private boolean doVector(Transferable transferable,DropTargetDropEvent dtde,JTable tree,int action) throws Exception
	{
    	boolean e400=false;
    	Vector vec=(Vector)transferable.getTransferData(SIGAPTransferableNode.NODE_FLAVOR);
    	Object comp=transferable.getTransferData(SIGAPTransferableNode.SOURCE_FLAVOR);
    	Point pt = dtde.getLocation();
    	SIGATableObj obj=null;
    	
    	try
    	{
      		int row = tree.rowAtPoint(pt);
      		int column = tree.columnAtPoint(pt);
      		obj = (SIGATableObj) tree.getValueAt(row, column);
      		
      		if (obj == null)
      		{
        		return false;
      		}
    	}
    	
    	catch (Exception e)
    	{
      		return false;
    	}

    	for (int u=0;u<vec.size();u++)
    	{
      		DefaultMutableTreeNode draggedNode = (DefaultMutableTreeNode)vec.elementAt(u);
      		DefaultMutableTreeNode pater=(DefaultMutableTreeNode)draggedNode.getParent();
      		int accesso=0;
      		String access=obj.getID();
      		
      		if (access.equals(SIGABaseNode.ACCESS_DENY))
      		{
        		accesso=1;
      		}
      		
      		else if (access.equals(SIGABaseNode.ACCESS_READ))
      		{
        		accesso=2;
      		}
      		
      		else if (access.equals(SIGABaseNode.ACCESS_FULL))
      		{
        		accesso=3;
      		}
      		
      		else
      		{
        		accesso=0;
      		}

      		if (pater!=null)
      		{
        		String accessParent=(String)((SIGABaseNode)pater.getUserObject()).get(SIGABaseNode.ACCESS);
        		int accessoP=0;
        
        		if (accessParent.equals(SIGABaseNode.ACCESS_DENY))
        		{
          			accessoP=1;
        		}
        		
        		else if (accessParent.equals(SIGABaseNode.ACCESS_READ))
        		{
          			accessoP=2;
        		}
        		
        		else if (accessParent.equals(SIGABaseNode.ACCESS_FULL))
        		{
          			accessoP=3;
        		}
        		
        		else
        		{
          			accessoP=0;
        		}

        		if (accesso>accessoP)
        		{
          			e400=true;
          			
          			continue;
        		}
      		}
      		
      		if (transferable.isDataFlavorSupported(SIGAPTransferableNode.NODE_FLAVOR) && canPerformAction(tree, draggedNode, action, pt))
      		{
        		SIGAAccessObj objdes=(SIGAAccessObj)draggedNode.getUserObject();
        		objdes.put(SIGAAccessObj.ACCESS,obj.getID());
        		objdes.modified=true;
        		Enumeration children=draggedNode.children();
        		
        		while (children.hasMoreElements())
        		{
          			modifyChildren((DefaultMutableTreeNode) children.nextElement(),access,accesso);
        		}
      		}
    	}

    	dtde.acceptDrop(action);
    	dtde.dropComplete(true);
    	
    	if (comp instanceof JTree)
    	{
      		((JTree)comp).update(((JTree)comp).getGraphics());
    	}

    	if (e400)
    	{
      		throw new Exception(SIGAPTConstants.PARTIAL_PATER_RESTRIC);
    	}
    	
    	return true;
	}

	protected void modifyChildren(DefaultMutableTreeNode node, String access, int ac)
	{
    	int accesso=0;
    	SIGAAccessObj objdes=(SIGAAccessObj)node.getUserObject();
    	String nodeaccess=(String)objdes.get(SIGAAccessObj.ACCESS);
    	objdes.setChangeState(true);

    	if (nodeaccess.equals(SIGABaseNode.ACCESS_DENY))
    	{
      		accesso=1;
    	}
    	
    	else if (nodeaccess.equals(SIGABaseNode.ACCESS_READ))
    	{
      		accesso=2;
    	}
    	
    	else if (nodeaccess.equals(SIGABaseNode.ACCESS_FULL))
    	{
      		accesso=3;
    	}
    	
    	else
    	{
      		accesso=4; //para no dejar hijos sin crear
    	}
    	
    	if (ac<accesso)
    	{
      		objdes.put(SIGAAccessObj.ACCESS,access);
      		objdes.modified=true;
    	}

    	Enumeration children=node.children();
    	
    	while (children.hasMoreElements())
    	{
      		modifyChildren((DefaultMutableTreeNode) children.nextElement(),access,ac);
    	}
  	}
}