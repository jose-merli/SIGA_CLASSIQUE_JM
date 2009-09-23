package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.dnd.*;
import javax.swing.tree.*;
import javax.swing.table.*;

public class SIGAAppletUsuariosGrupos extends SIGAAppletBase
{
	private JTree treeSIGA = null;
	private TableModel tableModel = null;
	private JTable table = null;
	private DefaultTreeModel model = null;
	private JScrollPane JScrollPane1Grupos = new JScrollPane();
	private JScrollPane JScrollPane1SIGA = new JScrollPane();
	
	private BorderLayout layout = new BorderLayout();
	private JPanel panel = null;

	public JTree returnTreeSIGA()
	{
		return this.treeSIGA;
	}
	
	public JTree getTreeSIGA(int _dnd)
	{
		JTree tree = new SIGAUsuariosGruposTree(SIGANodeUsuariosGrupoModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix, initAction,params,this,  iconos),urlPrefix,_dnd,this,icontree);
		
		return tree;
	}

	protected void createObjects()
	{
    	super.createObjects();

		if (access.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL))
    	{
			table = new JTable(new SIGAUGTableModel(URLservlet, urlPrefix, this, iconos));
      		table.setBackground(new Color(238,237,243));
      		table.setShowGrid(false);
      		table.setTableHeader (null);
      		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

			table.setRowHeight(30);
			table.setRowSelectionAllowed(false);
			table.setDefaultRenderer(table.getColumnClass(0), new SIGAUGTableCellRenderer());
			new SIGAUGDrag(table, DnDConstants.ACTION_MOVE,true);
			new SIGAUGDrop(table, DnDConstants.ACTION_MOVE,this);
			TableColumnModel Tablecolumnm=table.getColumnModel();
			TableColumn Tablecolumn=Tablecolumnm.getColumn(0);
			Tablecolumn.setMinWidth(180);
			
			JScrollPane1Grupos.setAutoscrolls(true);
			JScrollPane1Grupos.setOpaque(true);
			//JScrollPane1Grupos.setDoubleBuffered(true);
			JScrollPane1Grupos.getViewport().setBackground(new java.awt.Color(238,237,243));
			JScrollPane1Grupos.getViewport().add(table);
			
			treeSIGA=getTreeSIGA(SIGAPTConstants.DROP|SIGAPTConstants.DRAG);
		}
		
		else
		{
      		treeSIGA=getTreeSIGA(0);
    	}
    	
    	treeSIGA.setDoubleBuffered(true);
    
		JScrollPane1SIGA.setAutoscrolls(true);
		JScrollPane1SIGA.setOpaque(true);
		//JScrollPane1SIGA.setDoubleBuffered(true);
		JScrollPane1SIGA.getViewport().setBackground(new java.awt.Color(238,237,243));
		
		JScrollPane1SIGA.getViewport().add(treeSIGA);
		panel=new JPanel(new BorderLayout());
	}

	public void init()
	{
    	readParameters();
    	createObjects();

	    getContentPane().setLayout(layout);
	    getContentPane().setBackground(new java.awt.Color(238,237,243));

    	String lit=(String)literales.get("LITASIGNARUSUARIOSGRUPO");
    	
    	if (lit==null)
    	{
    		lit="No hay literal";
    	}
    
    	JLabel lab=new JLabel(" " + lit + " ");
    	lab.setFont(new Font("Arial", Font.BOLD, 18));
    	lab.setHorizontalAlignment(SwingConstants.CENTER);
    	JPanel pan2=new JPanel();
    	JPanel pan3=new JPanel();
    	
    	if (access.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL))
    	{
      		pan3.add(lab,BorderLayout.WEST);
      		pan2.add(guardar,BorderLayout.WEST);
      		pan2.add(cancelar,BorderLayout.CENTER);
      		panel.add(pan3,BorderLayout.NORTH);
      		panel.add(pan2,BorderLayout.CENTER);
    	}
    	
    	else
    	{
      		panel.add(lab,BorderLayout.CENTER);
    	}
    	
    	if (access.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL))
    	{
    	    getContentPane().add(JScrollPane1Grupos,BorderLayout.EAST);
    	}

    	getContentPane().add(JScrollPane1SIGA,BorderLayout.CENTER);

    	getContentPane().add(panel,BorderLayout.NORTH);
    	
    	initTree();
	}

	void initTree()
	{
    	if (treeSIGA!=null)
    	{
      		treeSIGA.setCellRenderer(new SIGAUGCellHandler());
      		treeSIGA.putClientProperty("JTree.lineStyle", "Angled");
      		treeSIGA.setBackground(new java.awt.Color(238,237,243));
      		DefaultTreeSelectionModel selModel=new DefaultTreeSelectionModel();
      		treeSIGA.setSelectionModel(selModel);
      		treeSIGA.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		}
	}

	public void guardar()
	{
    	try
    	{
    		String msg=(String)literales.get(SIGAPTConstants.CONFIRM_SAVE);
    		String title=(String)literales.get(SIGAPTConstants.warning);
    		
    		if (JOptionPane.showConfirmDialog(getContentPane(), msg, title, JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
    		{
    			return;
    		}
    		
    		Vector vec = new Vector();
    		
      		DefaultTreeModel model=(DefaultTreeModel)treeSIGA.getModel();
      		DefaultMutableTreeNode nodoRaiz=(DefaultMutableTreeNode)model.getRoot();

      		String urlServlet=((SIGAUsuarioRolObj)((SIGAUsuarioRolNode)nodoRaiz.getChildAt(0)).getUserObject()).urlServlet;
			String sUrl = urlServlet+"?"+SIGAPTConstants.SERVLET_FUNCTION+"="+SIGAPTConstants.SAVE_USUARIOS_GRUPO;

      		Enumeration enumHijosUsuarioRol = nodoRaiz.children();
      		
      		boolean bEnviar=false;
      		
      		while (enumHijosUsuarioRol.hasMoreElements())
      		{
      		    DefaultMutableTreeNode hijoUsuarioRolNodo = (DefaultMutableTreeNode)enumHijosUsuarioRol.nextElement();
      		    
      		    SIGAUsuarioRolObj hijoUsuarioRolObj = (SIGAUsuarioRolObj)hijoUsuarioRolNodo.getUserObject();
      		    
      		    String idUsuario = hijoUsuarioRolObj.getIdUsuario();
      		    String idRol = hijoUsuarioRolObj.getIdRol();
      		    
      		    Enumeration enumHijosGrupo = hijoUsuarioRolNodo.children();
      		    
      		    while (enumHijosGrupo.hasMoreElements())
      		    {
      		        DefaultMutableTreeNode hijoGrupoNodo = (DefaultMutableTreeNode)enumHijosGrupo.nextElement();
      		        
      		        SIGAGrupoObj hijoGrupoObj = (SIGAGrupoObj)hijoGrupoNodo.getUserObject();
      		        
      		        String idPerfil = hijoGrupoObj.getIdPerfil();
      		        String idInstitucion = hijoGrupoObj.getIdInstitucion();
      		        
      		        String sGrupoFinal = idUsuario + "*" + idInstitucion + "*" + idRol + "*" + idPerfil;      		        
      		        
      		        if (hijoGrupoObj.added && !hijoGrupoObj.removed)
      		        {
      		            sUrl += "&" + SIGAPTConstants.CREAR_USUARIO_GRUPO + "=" + sGrupoFinal;
      		            
      		            bEnviar=true;
      		        }
      		        
      		        else if (hijoGrupoObj.removed && !hijoGrupoObj.added)
      		        {
      		            sUrl += "&" + SIGAPTConstants.BORRAR_USUARIO_GRUPO + "=" + sGrupoFinal;
      		            
      		            bEnviar=true;
      		        }
      		    }
      		}
      		
      		if (!bEnviar)
      		{
      			return;
      		}
      		
  		    URL direction = new URL(sUrl);

			URLConnection conexion = direction.openConnection();
			conexion.setUseCaches (false);
			
			conexion.connect();

			ObjectInputStream input = new ObjectInputStream (conexion.getInputStream());
			Object o =  input.readObject();
			input.close();
			Vector vecRet=(Vector)o;

      		if ("OK".equalsIgnoreCase((String)vecRet.elementAt(0)))
      		{
				Vector vec2=(Vector)vecRet.elementAt(1);
				Hashtable has=(Hashtable)vec2.elementAt(1);
				SIGAUsuariosGruposHier hier=(SIGAUsuariosGruposHier)vec2.elementAt(0);
				model=(DefaultTreeModel)treeSIGA.getModel();
				DefaultMutableTreeNode padre=(DefaultMutableTreeNode)model.getRoot();
				
// RGG 				msg=(String)literales.get(SIGAPTConstants.STRUC_CHANGED);
				msg=SIGAPTConstants.STRUC_CHANGED;

        		if (msg==null)
        		{
        			msg=SIGAPTConstants.STRUC_CHANGED_reload;
        		}

        		title=(String)literales.get(SIGAPTConstants.warning);
        		
        		if (title==null)
        		{
        			title=SIGAPTConstants.warning;
        		}

        		JOptionPane.showMessageDialog(getContentPane(), msg, title, JOptionPane.INFORMATION_MESSAGE);
        		
        		DefaultTreeModel newmodel=SIGANodeUsuariosGrupoModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix,initAction,params,this,hier,has,iconos);
        		treeSIGA.setModel(newmodel);

        		/*if (JOptionPane.showConfirmDialog(getContentPane(),msg,title,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION)
              	{
            		DefaultTreeModel newmodel=SIGANodeUsuariosGrupoModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix,initAction,params,this,hier,has,iconos);
            		treeSIGA.setModel(newmodel);
        		}
        		
        		else
        		{
          			model=(DefaultTreeModel)treeSIGA.getModel();
          			nodoRaiz=(DefaultMutableTreeNode)model.getRoot();
          			clearObjects(treeSIGA, model, nodoRaiz, false);
          			treeSIGA.update(treeSIGA.getGraphics());
        		}*/
      		}
      		
      		else
      		{
        		msg=(String)literales.get(vecRet.elementAt(1));
        		
        		if (msg==null)
        		{
        			msg=(String)vecRet.elementAt(1);
        		}
        		
        		JOptionPane.showMessageDialog(this, msg, (String)vecRet.elementAt(0), JOptionPane.WARNING_MESSAGE);
      		}

			
			
			
      		/*Vector vec=new Vector();
      		DefaultTreeModel model=(DefaultTreeModel)treeSIGA.getModel();
      		DefaultMutableTreeNode node=(DefaultMutableTreeNode)model.getRoot();
			String actionMove=((SIGABaseNode)node.getUserObject()).actionMove;
			String oidmoved=SIGAPTConstants.OIDMOVED;
			String urlServlet=((SIGABaseNode)node.getUserObject()).urlServlet;
			String servFunction=SIGAPTConstants.SERVLET_FUNCTION;
			String profFunc=SIGAPTConstants.PROFILE;
			String profile=(String)((SIGABaseNode)node.getUserObject()).get(SIGAPTConstants.PROFILE);
			vec=fillModified(model, node, vec);
      		
      		if (vec.size()==0)
      		{
        		return;
        	}

			String fun=URLEncoder.encode(actionMove, "ISO-8859-1");
			String sUrl = urlServlet+"?"+servFunction+"="+fun+"&"+profFunc+"="+URLEncoder.encode(profile,"ISO-8859-1");

      		for (int j=0;j<vec.size();j++)
      		{
        		sUrl+=("&"+oidmoved+"="+URLEncoder.encode((String)vec.elementAt(j),"ISO-8859-1"));
      		}

      		URL direction = new URL(sUrl);

			URLConnection conexion = direction.openConnection();
			conexion.setUseCaches (false);
			
			conexion.connect();
			ObjectInputStream input = new ObjectInputStream (conexion.getInputStream());
			Vector vecRet=(Vector) input.readObject();
			input.close();

      		if ("OK".equalsIgnoreCase((String)vecRet.elementAt(0)))
      		{
				Vector vec2=(Vector)vecRet.elementAt(1);
				Hashtable has=(Hashtable)vec2.elementAt(1);
				SIGAUsuariosGruposHier hier=(SIGAUsuariosGruposHier)vec2.elementAt(0);
				model=(DefaultTreeModel)treeSIGA.getModel();
				DefaultMutableTreeNode padre=(DefaultMutableTreeNode)model.getRoot();
				
				String msg=(String)literales.get(SIGAPTConstants.STRUC_CHANGED_reload);

        		if (msg==null)
        		{
        			msg=SIGAPTConstants.STRUC_CHANGED_reload;
        		}

        		String title=(String)literales.get(SIGAPTConstants.warning);
        		
        		if (title==null)
        		{
        			title=SIGAPTConstants.warning;
        		}

        		if (JOptionPane.showConfirmDialog(getContentPane(),msg,title,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION)
              	{
            		DefaultTreeModel newmodel=SIGANodeUsuariosGrupoModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix,initAction,params,this,hier,has,iconos);
            		treeSIGA.setModel(newmodel);
        		}
        		
        		else
        		{
          			model=(DefaultTreeModel)treeSIGA.getModel();
          			node=(DefaultMutableTreeNode)model.getRoot();
          			clearObjects(treeSIGA, model,node,false);
          			treeSIGA.update(treeSIGA.getGraphics());
        		}
      		}
      		
      		else
      		{
        		String msg=(String)literales.get(vecRet.elementAt(1));
        		
        		if (msg==null)
        		{
        			msg=(String)vecRet.elementAt(1);
        		}
        		
        		JOptionPane.showMessageDialog(this, msg, (String)vecRet.elementAt(0),JOptionPane.WARNING_MESSAGE);
      		}*/
    	}
    	
    	catch (Exception e)
    	{
      		e.printStackTrace();
    	}
	}

	public void cancelar()
	{
    	model=(DefaultTreeModel)treeSIGA.getModel();
    	DefaultMutableTreeNode nodoRaiz=(DefaultMutableTreeNode)model.getRoot();
    	//clearObjects(treeSIGA, model, nodoRaiz, true);
    	clearObjects(treeSIGA, model, nodoRaiz);
    	
    	treeSIGA.setModel(model);
    	treeSIGA.update(treeSIGA.getGraphics());
	}

  	protected String getDataModified(SIGABaseNode nod, DefaultMutableTreeNode node)
  	{
    	return (nod.getID()+"-"+nod.get(SIGAPTConstants.ACCESS));
	}

	protected void clearObject(SIGABaseNode nod, DefaultMutableTreeNode node)
	{
	    nod.changestate=false;
	    nod.modified=false;
	    nod.added=false;
	    nod.removed=false;
	}
	
	protected void clearObject(SIGABaseNode nod, DefaultMutableTreeNode node, boolean cancelar)
	{
    	nod.modified=false;
    
    	if (cancelar)
    	{
      		nod.restoreState();
      	}
    	
    	else
    	{
      		nod.changestate=false;
      		nod.setOldState(nod.getID());
		}
	}

}