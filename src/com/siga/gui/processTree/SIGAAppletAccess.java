package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import java.awt.dnd.*;
import javax.swing.tree.*;
import javax.swing.table.*;

public class SIGAAppletAccess extends SIGAAppletBase
{
	private JTree treeSIGA=null;
	private TableModel tableModel=null;
	private JTable table=null;
	private DefaultTreeModel model=null;
	private JScrollPane  JScrollPane1Localizaciones = null;
	private JScrollPane  JScrollPane1SIGA = new javax.swing.JScrollPane();
	private BorderLayout layout  = new BorderLayout();
	private JPanel panel=null;

	public JTree returnTreeSIGA()
	{
		return this.treeSIGA;
	}
	
	public JTree getTreeSIGA(int _dnd)
	{
		JTree tree = new SIGAProcessTree(SIGANodeProcessModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix, initAction,params,this,  iconos),urlPrefix,_dnd,this,icontree);
		
		return tree;
	}

	protected void createObjects()
	{
    	super.createObjects();

		//if ("FULL".equalsIgnoreCase(access))
		if (access.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL))
    	{
			table = new JTable(new SIGATTTableModel(URLservlet,urlPrefix,this,iconos));
      		table.setBackground(new Color(238,237,243));
      		table.setShowGrid(false);
      		table.setTableHeader (null);
      		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.setRowHeight(60);
			table.setRowSelectionAllowed(false);
			table.setDefaultRenderer(table.getColumnClass(0), new SIGAPTableCellRenderer());
			new SIGADrag(table, DnDConstants.ACTION_MOVE,true);
			new SIGADrop(table, DnDConstants.ACTION_MOVE,this);
			TableColumnModel Tablecolumnm=table.getColumnModel();
			TableColumn Tablecolumn=Tablecolumnm.getColumn(0);
			Tablecolumn.setMinWidth(180);
			treeSIGA=getTreeSIGA(SIGAPTConstants.DROP|SIGAPTConstants.DRAG);
		}
		
		else
		{
      		treeSIGA=getTreeSIGA(0);
    	}
    
		JScrollPane1SIGA.setAutoscrolls(true);
		//JScrollPane1SIGA.setNextFocusableComponent(treeSIGA);
		JScrollPane1SIGA.setOpaque(true);
		JScrollPane1SIGA.setDoubleBuffered(true);
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

    	String lit=(String)literales.get("LITACCESS");
    	
    	if (lit==null)
    	{
    		lit="No hay literal";
    	}
    
    	JLabel lab=new JLabel(" " + lit + " ");
    	lab.setFont(new Font("Arial", Font.BOLD, 18));
    	JPanel pan2=new JPanel();
    	JPanel pan3=new JPanel();
    	
    	//if ("FULL".equalsIgnoreCase(access))
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
    	
    	//if ("FULL".equalsIgnoreCase(access))
    	if (access.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL))
    	{
      		getContentPane().add(table,BorderLayout.EAST);
    	}

    	getContentPane().add(JScrollPane1SIGA,BorderLayout.CENTER);

    	getContentPane().add(panel,BorderLayout.NORTH);
    	
    	initTree();
	}

	void initTree()
	{
    	if (treeSIGA!=null)
    	{
      		treeSIGA.setCellRenderer(new SIGAPTCellHandler());
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

      		Vector vec=new Vector();
      		DefaultTreeModel model=(DefaultTreeModel)treeSIGA.getModel();
      		DefaultMutableTreeNode node=(DefaultMutableTreeNode)model.getRoot();
			String actionMove=((SIGABaseNode)node.getUserObject()).actionMove;
			String oidmoved=SIGAPTConstants.OIDMOVED;
			String urlServlet=((SIGABaseNode)node.getUserObject()).urlServlet;
			String servFunction=SIGAPTConstants.SERVLET_FUNCTION;
			String profFunc=SIGAPTConstants.PROFILE;
			String profile=(String)((SIGABaseNode)node.getUserObject()).get(SIGAPTConstants.PROFILE);
			vec=fillModified(model,node, vec);
      		
      		if (vec.size()==0)
      		{
        		return;
        	}

			String fun=URLEncoder.encode(actionMove, "ISO-8859-1");
			//String sUrl = urlServlet+"?"+servFunction+"="+fun+"&"+profFunc+"="+URLEncoder.encode(profile,"ISO-8859-1");
			String data = servFunction+"="+fun+"&"+profFunc+"="+URLEncoder.encode(profile,"ISO-8859-1");

      		for (int j=0;j<vec.size();j++)
      		{
//        		sUrl+=("&"+oidmoved+"="+URLEncoder.encode((String)vec.elementAt(j),"ISO-8859-1"));
        		data+=("&"+oidmoved+"="+URLEncoder.encode((String)vec.elementAt(j),"ISO-8859-1"));
      		}

//      		URL direction = new URL(sUrl);
      		URL direction = new URL(urlServlet);

      		
			URLConnection conexion = direction.openConnection();
			
			// Esto envia datos en PUT
			conexion.setDoOutput(true);
			
			conexion.setUseCaches (false);
			
			// RGG
			OutputStreamWriter wr = new OutputStreamWriter(conexion.getOutputStream());
	        wr.write(data);
	        wr.flush();
	        wr.close();

			
			/*JInternalFrame fEspera = new JInternalFrame("Esperando", false, false, false);
			getContentPane().add(fEspera);
			fEspera.setVisible(true);*/


			
			// RGG conexion.connect();
	        
			ObjectInputStream input = new ObjectInputStream (conexion.getInputStream());
			Object o = input.readObject();
			input.close();
			Vector vecRet=(Vector) o;
			

      		if ("OK".equalsIgnoreCase((String)vecRet.elementAt(0)))
      		{
				Vector vec2=(Vector)vecRet.elementAt(1);
				Hashtable has=(Hashtable)vec2.elementAt(1);
				SIGAProcessHier hier=(SIGAProcessHier)vec2.elementAt(0);
				model=(DefaultTreeModel)treeSIGA.getModel();
				DefaultMutableTreeNode padre=(DefaultMutableTreeNode)model.getRoot();
				
				msg=(String)literales.get(SIGAPTConstants.STRUC_CHANGED);

        		if (msg==null)
        		{
        			msg=SIGAPTConstants.STRUC_CHANGED;
        		}

        		title=(String)literales.get(SIGAPTConstants.warning);
        		
        		if (title==null)
        		{
        			title=SIGAPTConstants.warning;
        		}

        		//if (!checkStructure(padre, hier) && JOptionPane.showConfirmDialog(getContentPane(),msg,title,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION)
        		JOptionPane.showMessageDialog(getContentPane(),msg,title,JOptionPane.INFORMATION_MESSAGE);
//        		if (JOptionPane.showConfirmDialog(getContentPane(),msg,title,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION)
//        		{
        			DefaultTreeModel newmodel=SIGANodeProcessModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix,initAction,params,this,hier,has,iconos);
            		treeSIGA.setModel(newmodel);
/*
      			}
        		else
        		{
          			model=(DefaultTreeModel)treeSIGA.getModel();
          			node=(DefaultMutableTreeNode)model.getRoot();
          			clearObjects(treeSIGA, model,node,false);
          			treeSIGA.update(treeSIGA.getGraphics());
        		}
*/        		
      		}
      		
      		else
      		{
        		msg=(String)literales.get(vecRet.elementAt(1));
        		
        		if (msg==null)
        		{
        			msg=(String)vecRet.elementAt(1);
        		}
        		
        		JOptionPane.showMessageDialog(this, msg, (String)vecRet.elementAt(0),JOptionPane.WARNING_MESSAGE);
      		}
    	}
    	
    	catch (Exception e)
    	{
      		e.printStackTrace();
    	}
	}

	public void cancelar()
	{
    	model=(DefaultTreeModel)treeSIGA.getModel();
    	DefaultMutableTreeNode node=(DefaultMutableTreeNode)model.getRoot();
    	clearObjects(treeSIGA, model,node,true);
    	treeSIGA.update(treeSIGA.getGraphics());
	}

  	protected String getDataModified(SIGABaseNode nod, DefaultMutableTreeNode node)
  	{
    	return (nod.getID()+"-"+nod.get(SIGAPTConstants.ACCESS));
	}

	protected void clearObject(SIGABaseNode nod, DefaultMutableTreeNode node,boolean cancelar)
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