package com.siga.gui.processTree;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import java.awt.dnd.*;
import javax.swing.tree.*;
import java.awt.datatransfer.*;

public class SIGAUGDrop implements DropTargetListener
{
	private JApplet applet=null;
	private DropTarget dropTj=null;
	JComponent comp=null;

  	protected SIGAUGDrop(JComponent tree2, int action, JApplet _applet)
  	{
    	applet=_applet;
    	comp=tree2;
    	dropTj = new DropTarget(tree2, action, this);
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
      		callToExecuteDrop(dtde, component);
    	}
    	
    	catch (Exception e)
    	{
      		e.printStackTrace();

      		dtde.rejectDrop();
      		dtde.dropComplete(false);
    	}
  	}

  	private void callToExecuteDrop(DropTargetDropEvent dtde, JComponent componente) throws Exception
  	{
    	int action = dtde.getDropAction();
    	Transferable transferable = dtde.getTransferable();

    	try
    	{
    	    boolean bOK = true;
    	    
    	    JTree MiArbol = null;
    	    
    	    if (transferable.getTransferData(SIGAUGTransferableNode.NODE_FLAVOR) instanceof SIGAUGTableObj && componente instanceof JTree)
      		{
    	        MiArbol = (JTree)componente;

    	        bOK = doSIGATable(transferable, dtde, MiArbol, (JTable)transferable.getTransferData(SIGAUGTransferableNode.SOURCE_FLAVOR), action);
        	}
        	
          	else if (transferable.getTransferData(SIGAUGTransferableNode.NODE_FLAVOR) instanceof DefaultMutableTreeNode && componente instanceof JTable)
          	{
          	    MiArbol = (JTree)transferable.getTransferData(SIGAUGTransferableNode.SOURCE_FLAVOR); 
          	    
          	    bOK = doSIGATree(transferable, dtde, (JTable)componente, MiArbol, action);
            }
          	
          	else if (transferable.getTransferData(SIGAUGTransferableNode.NODE_FLAVOR) instanceof Vector && componente instanceof JTable)
          	{
          	    MiArbol = (JTree)transferable.getTransferData(SIGAUGTransferableNode.SOURCE_FLAVOR);
          	    
          	    bOK = doSIGATreeVector((Vector)transferable.getTransferData(SIGAUGTransferableNode.NODE_FLAVOR), dtde, (JTable)componente, MiArbol, action);
          	}
          	
          	if (bOK)
          	{
		    	dtde.acceptDrop(action);
		    	dtde.dropComplete(true);

		    	MiArbol.update(MiArbol.getGraphics());
          	}
          	
          	else
          	{
	            dtde.rejectDrop();
	            dtde.dropComplete(false);
          	}
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
      		dtde.rejectDrop();
      		dtde.dropComplete(false);

      		String msg=(String)((SIGAAppletBase)applet).literales.get(e.getMessage());
      		
      		if (msg==null)
      		{
      			if (msg==null)
      			{
      				msg=(String)e.getMessage();
      			}
      		}
      		
      		if (!"-1".equals(msg))
      		{
        		JOptionPane.showMessageDialog(applet, msg, "Exception", JOptionPane.WARNING_MESSAGE);
            }
		}
	}

	private boolean doSIGATable(Transferable transferable, DropTargetDropEvent dtde, JTree arbol, JTable tabla, int action) throws Exception
	{
    	SIGAUGTableObj nodoTabla = (SIGAUGTableObj)transferable.getTransferData(SIGAUGTransferableNode.NODE_FLAVOR);
    	Point pt = dtde.getLocation();
    	TreePath pathTarget = arbol.getPathForLocation(pt.x, pt.y);
    	DefaultMutableTreeNode nodoArbol = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
    	DefaultMutableTreeNode padre=(DefaultMutableTreeNode)nodoArbol.getRoot();
    	
    	boolean bOK = doSIGAArrastrar(nodoTabla, nodoArbol, dtde, tabla, arbol, action, true, padre);
    	
    	if (bOK)
    	{
    		arbol.setModel(new DefaultTreeModel(padre));
    	}
    	
    	return bOK;
	}
	
	private boolean doSIGATree(Transferable transferable, DropTargetDropEvent dtde, JTable tabla, JTree arbol, int action) throws Exception
	{
	    DefaultMutableTreeNode nodoArbol = (DefaultMutableTreeNode)transferable.getTransferData(SIGAUGTransferableNode.NODE_FLAVOR);
    	Point pt = dtde.getLocation();
    	SIGAUGTableObj nodoTabla = (SIGAUGTableObj)tabla.getModel().getValueAt(tabla.rowAtPoint(pt),0);
		DefaultMutableTreeNode padre=(DefaultMutableTreeNode)nodoArbol.getRoot();
		
    	boolean bOK = doSIGAArrastrar(nodoTabla, nodoArbol, dtde, tabla, arbol, action, true, padre);
    	
    	if (bOK)
    	{
    		arbol.setModel(new DefaultTreeModel(padre));
    	}
    	
    	return bOK;
	}

	private boolean doSIGATreeVector(Vector vector, DropTargetDropEvent dtde, JTable tabla, JTree arbol, int action) throws Exception
	{
    	Point pt = dtde.getLocation();
    	SIGAUGTableObj nodoTabla = (SIGAUGTableObj)tabla.getModel().getValueAt(tabla.rowAtPoint(pt),0);
    	
    	DefaultMutableTreeNode nodoArbol = null;
    	DefaultMutableTreeNode padre = null;
    	
    	int iVector = vector.size();
    	
	    for (int i=0; i<iVector; i++)
	    {
			long seg_first = 0;
			long seg_last =0;

	        nodoArbol = (DefaultMutableTreeNode)vector.elementAt(i);
	        
	        if (i==0)
	        {
	        	padre=(DefaultMutableTreeNode)nodoArbol.getRoot();
	    	}
	    	
	        doSIGAArrastrar(nodoTabla, nodoArbol, dtde, tabla, arbol, action, false, padre);
	    }
	    
	    arbol.setModel(new DefaultTreeModel(padre));

	    return true;
	}

	private boolean doSIGAArrastrar(SIGAUGTableObj nodoTabla, DefaultMutableTreeNode nodoArbol, DropTargetDropEvent dtde, JTable tabla, JTree arbol, int action, boolean bMostrarMensajeError, DefaultMutableTreeNode padre) throws Exception
	{
		String title=(String)((SIGAAppletBase)applet).literales.get(SIGAPTConstants.warning);
		
	    if (!nodoArbol.isRoot())
	    {
	    	if (nodoArbol.getUserObject() instanceof SIGAGrupoObj && nodoTabla.removed)
	    	{
	    	    ((SIGAGrupoObj)nodoArbol.getUserObject()).setRemoved(true);

		    	nodoTabla.dndMovement2((SIGABaseNode)((DefaultMutableTreeNode)nodoArbol.getParent()).getUserObject());
		    	//arbol.setModel(new DefaultTreeModel(padre));
	    	}

	        else if (nodoArbol.getUserObject() instanceof SIGAUsuarioRolObj && nodoTabla.removed)
    	    {
    	        Enumeration enumHijos = nodoArbol.children();

    	        while (enumHijos.hasMoreElements())
    	        {
    	            ((SIGAGrupoObj)((SIGAUsuarioRolNode)enumHijos.nextElement()).getUserObject()).setRemoved(true);
    	        }

		        nodoTabla.dndMovement2((SIGABaseNode)(nodoArbol).getUserObject());
		        //arbol.setModel(new DefaultTreeModel(padre));
    	    }

	    	else if (nodoArbol.getUserObject() instanceof SIGAUsuarioRolObj && nodoTabla.added)
	    	{
	    	    SIGAGrupoObj sgo = new SIGAGrupoObj();

	    	    sgo.put(SIGABaseNode.NAME, nodoTabla.toString());
	    	    //sgo.setAdded(true);
	    	    //sgo.setActionMove(SIGAPTConstants.SAVE_USUARIOS_GRUPO);
	    	    //sgo.setIdPerfil((String)nodoTabla.get(SIGAPTConstants.PERFIL_IDPERFIL));
	    	    //sgo.setIdInstitucion((String)nodoTabla.get(SIGAPTConstants.PERFIL_IDINSTITUCION));

	    	    SIGAUsuarioRolNode nuevoHijo = new SIGAUsuarioRolNode(sgo);
			    nuevoHijo.setAllowsChildren(false);

			    Enumeration enumHijos = nodoArbol.children();

			    boolean bAgregar=true;
			    
			    String nuevoHijoString = nuevoHijo.toString();
			    DefaultMutableTreeNode dmtnAux = null;
			    SIGAGrupoObj grupoObjObject = null;

			    while (bAgregar && enumHijos.hasMoreElements())
			    {
			        dmtnAux = (DefaultMutableTreeNode)enumHijos.nextElement();

			        if (dmtnAux.toString().equals(nuevoHijoString))
			        {
			        	grupoObjObject = (SIGAGrupoObj)dmtnAux.getUserObject();
			        	
			            if (grupoObjObject.removed)
			            {
			                grupoObjObject.setRemoved(false);
			                
			                bMostrarMensajeError=false;
			            }

			            bAgregar=false;
			        }
			    }

			    if (bAgregar)
			    {
   		    	    sgo.setAdded(true);
	    	    	sgo.setActionMove(SIGAPTConstants.SAVE_USUARIOS_GRUPO);
	    	    	sgo.setIdPerfil((String)nodoTabla.get(SIGAPTConstants.PERFIL_IDPERFIL));
	    	    	sgo.setIdInstitucion((String)nodoTabla.get(SIGAPTConstants.PERFIL_IDINSTITUCION));

			        nodoArbol.setAllowsChildren(true);
			        nodoArbol.add(nuevoHijo);

			        nodoTabla.dndMovement2((SIGABaseNode)nodoArbol.getUserObject());
			        //arbol.setModel(new DefaultTreeModel(padre));
			    }
			    
			    else if (bMostrarMensajeError)
			    {
			        Toolkit.getDefaultToolkit().beep();
			        //JOptionPane.showMessageDialog(null, "El nodo ya existe.", "Aviso", JOptionPane.WARNING_MESSAGE);
			        String msg=(String)((SIGAAppletBase)applet).literales.get(SIGAPTConstants.USUARIO_YA_ASIGNADO);
			        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
			        
			        return false;
			    }
	    	}
	    	
	    	else if (bMostrarMensajeError)
	    	{
		        Toolkit.getDefaultToolkit().beep();
		        //JOptionPane.showMessageDialog(null, "Movimiento no válido.", title, JOptionPane.WARNING_MESSAGE);
	    	    String msg=(String)((SIGAAppletBase)applet).literales.get(SIGAPTConstants.MOVIMIENTO_NO_VALIDO);
	    	    JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
	    	    
		        return false;
	    	}
	    }
	    
	    else if (bMostrarMensajeError)
	    {
	        Toolkit.getDefaultToolkit().beep();
	        //JOptionPane.showMessageDialog(null, "El nodo raíz no admite hijos.", "Aviso", JOptionPane.WARNING_MESSAGE);
	        String msg=(String)((SIGAAppletBase)applet).literales.get(SIGAPTConstants.ASIGNAR_USUARIO_GRUPO);
	        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
    	    
	        return false;
	    }

	    return true;
	}

	protected void modifyChildren(DefaultMutableTreeNode node, String access, int ac)
	{
    	int accesso=0;

		SIGAUsuariosGruposObj objdes=(SIGAUsuariosGruposObj)node.getUserObject();
    	String nodeaccess=(String)objdes.get(SIGAUsuariosGruposObj.ACCESS);
    	objdes.setChangeState(true);
    	objdes.setAdded(true);
  	}
}