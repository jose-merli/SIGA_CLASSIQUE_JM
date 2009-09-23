package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;

public class SIGAAppletProcess extends SIGAAppletBase
{
	private JTree treeSIGA=null;
	private JTree treeSIGA2=null;
	private TableModel tableModel=null;
	private JTable table=null;
	private DefaultTreeModel model=null;
	
	protected int numberOfParams=0;
	
	public String[] params=null;
	protected String process;
	
	private JScrollPane JScrollPane1Localizaciones = null;
	private JScrollPane JScrollPane1SIGA = new javax.swing.JScrollPane();
	private JSplitPane splitPane = new JSplitPane();
	private BorderLayout layout = new BorderLayout();
	private JPanel panel=null;

	public JTree returnTreeSIGA()
	{
		return this.treeSIGA;
	}

	public JTree getTreeSIGA(int _dnd)
  	{
    	JTree tree = new SIGAProcessTree(SIGANodeProcessModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix, initAction,params,this,iconos),urlPrefix,_dnd,this,icontree);
    	
    	return tree;
	}

	protected void  createObjects()
	{
    	super.createObjects();
    	panel=new JPanel(new BorderLayout());
    	
    	if ("FULL".equalsIgnoreCase(access))
    	{
      		treeSIGA2=getTreeSIGA(SIGAPTConstants.DROP);
      		JScrollPane1Localizaciones = new javax.swing.JScrollPane(treeSIGA2);
      		treeSIGA=getTreeSIGA(SIGAPTConstants.DRAG);
    	}
    	
    	else
    	{
      		treeSIGA2=getTreeSIGA(0);
      		JScrollPane1Localizaciones = new javax.swing.JScrollPane(treeSIGA2);
    	}
	}

	public void init()
	{
    	readParameters();

    	createObjects();
    	String lit=(String)literales.get("LITPROCESS");
    	
    	if (lit==null)
    	{
    		lit="No hay lit";
    	}
    	
	    JLabel lab=new JLabel(" " + lit + " ");
	    lab.setFont(new Font("Arial", Font.BOLD, 18));
	    JPanel pan2=new JPanel();

    	if ("FULL".equalsIgnoreCase(access))
    	{
      		pan2.add(guardar,BorderLayout.WEST);
      		pan2.add(cancelar,BorderLayout.CENTER);
      		
      		panel.add(pan2,BorderLayout.CENTER);
    	}
    	
    	else
    	{
      		panel.add(lab,BorderLayout.CENTER);
		}

    	getContentPane().setLayout(layout);
    	getContentPane().setBackground(new java.awt.Color(238,237,243));

    	getContentPane().add(panel,BorderLayout.NORTH);
    	
    	if ("FULL".equalsIgnoreCase(access))
    	{
      		getContentPane().add(splitPane);
      		splitPane.setBounds(0,100,230,600);
      		splitPane.setBottomComponent(JScrollPane1SIGA);
      		splitPane.setTopComponent(JScrollPane1Localizaciones);
      		splitPane.setOneTouchExpandable(false);
      		splitPane.setDividerLocation(385);
      		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
      		splitPane.setDividerSize(3);
    	}
    	
    	else
    	{
      		getContentPane().add(JScrollPane1Localizaciones);
    	}
    	
    	int appletWidth = this.getWidth();

	    JScrollPane1Localizaciones.setAutoscrolls(true);
	    JScrollPane1Localizaciones.setOpaque(true);
	    JScrollPane1Localizaciones.setDoubleBuffered(true);
	    JScrollPane1Localizaciones.getViewport().setBackground(new java.awt.Color(238,237,243));
	
	    JScrollPane1SIGA.setAutoscrolls(true);
	    //JScrollPane1SIGA.setNextFocusableComponent(treeSIGA);
	    JScrollPane1SIGA.setOpaque(true);
	    JScrollPane1SIGA.setDoubleBuffered(true);
	    JScrollPane1SIGA.getViewport().setBackground(new java.awt.Color(238,237,243));
	    JScrollPane1SIGA.getViewport().add(treeSIGA);
	    initTree();
	}

	void initTree()
	{
    	if (treeSIGA!=null)
    	{
      		treeSIGA.setCellRenderer(new SIGAPTCellHandler());
      		treeSIGA.putClientProperty("JTree.lineStyle", "Angled");
      		treeSIGA.putClientProperty("JTree.lineStyle", "Angled");
      		treeSIGA.setBackground(new java.awt.Color(238,237,243));
      		treeSIGA.getSelectionModel().
          	setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	}
    	
    	if (treeSIGA2!=null)
    	{
      		treeSIGA2.setCellRenderer(new SIGAPTCellHandler());
      		treeSIGA2.putClientProperty("JTree.lineStyle", "Angled");
      		treeSIGA2.putClientProperty("JTree.lineStyle", "Angled");
      		treeSIGA2.setBackground(new java.awt.Color(238,237,243));
      		treeSIGA2.setSelectionModel(null);
    	}
	}

	public void guardar()
	{
	    ObjectInputStream input=null;
	    
    	try
    	{
    		String msg=(String)literales.get(SIGAPTConstants.CONFIRM_SAVE);
    		String title=(String)literales.get(SIGAPTConstants.warning);
    		
    		if (JOptionPane.showConfirmDialog(getContentPane(), msg, title, JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
    		{
    			return;
    		}

		    Vector vec=new Vector();
		    DefaultTreeModel model=(DefaultTreeModel)treeSIGA2.getModel();
		    DefaultMutableTreeNode node=(DefaultMutableTreeNode)model.getRoot();
		    String actionMove=((SIGABaseNode)node.getUserObject()).actionMove;
		    String oidmoved=SIGAPTConstants.OIDMOVED;
		    String urlServlet=((SIGABaseNode)node.getUserObject()).urlServlet;
		    String servFunction=SIGAPTConstants.SERVLET_FUNCTION;
		    vec=fillModified(model,node, vec);
	    
	    	if (vec.size()==0)
	    	{
	      		return;
	      	}
	      	
	    	String fun=URLEncoder.encode(actionMove,"ISO-8859-1");
	    	String sUrl = urlServlet+"?"+servFunction+"="+fun;
	
	    	for (int y=0;y<vec.size();y++)
	    	{
	      		sUrl+=("&"+oidmoved+"="+URLEncoder.encode((String)vec.elementAt(y),"ISO-8859-1"));
	    	}

      		URL direction = new URL(sUrl);
			URLConnection conexion = direction.openConnection();
			conexion.setUseCaches (false);
			conexion.connect();
			input = new ObjectInputStream (conexion.getInputStream());
			Object o = input.readObject();
			input.close();
			Vector ret=(Vector) o;
			

			if ("OK".equalsIgnoreCase((String)ret.elementAt(0)))
			{
				Vector vec2=(Vector)ret.elementAt(1);
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

        		if (!checkStructure(padre, hier) && JOptionPane.showConfirmDialog(getContentPane(),msg,title,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION)
              	{
            		DefaultTreeModel newmodel=SIGANodeProcessModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix,initAction,params,this,hier,has,iconos);
            		treeSIGA.setModel(newmodel);
            		DefaultTreeModel newmodel2=SIGANodeProcessModel.newSIGANodeTreeModelSIGA(URLservlet,urlPrefix,initAction,params,this,hier,has,iconos);
            		treeSIGA2.setModel(newmodel2);
        		}
        		
        		else
        		{
					DefaultTreeModel modelOr=(DefaultTreeModel)treeSIGA2.getModel();
					DefaultMutableTreeNode nodeOr=(DefaultMutableTreeNode)modelOr.getRoot();
					DefaultTreeModel modelFin=(DefaultTreeModel)treeSIGA.getModel();
					DefaultMutableTreeNode nodeFin=(DefaultMutableTreeNode)model.getRoot();
					while (restoreObjects(modelFin,nodeFin,modelOr,(DefaultMutableTreeNode)modelOr.getRoot())!=0);
					clearObjects(treeSIGA2, modelOr,nodeOr,true);
					clearObjects(treeSIGA, model,node,false);
					treeSIGA.update(treeSIGA.getGraphics());
					treeSIGA2.update(treeSIGA2.getGraphics());
				}
			}
			
			else
			{
        		msg=(String)literales.get(ret.elementAt(1));
        		
        		if (msg==null)
        		{
        			msg=(String)ret.elementAt(1);
        		}
        		
        		JOptionPane.showMessageDialog(this, msg, (String)ret.elementAt(0),JOptionPane.WARNING_MESSAGE);
			}
		}
		
		catch (Exception e)
		{
      		e.printStackTrace();
    	}
    	
    	finally
    	{
      		try
      		{
        		if (input!=null)
        		{
        			input.close();
        		}
        	}
        	
        	catch (Exception ed)
        	{
        		ed.printStackTrace();
        	}
		}
	}

	public void cancelar()
	{
    	try
    	{
			DefaultTreeModel modelOr=(DefaultTreeModel)treeSIGA.getModel();
			DefaultMutableTreeNode nodeOr=(DefaultMutableTreeNode)modelOr.getRoot();
			clearObjects(treeSIGA, modelOr,nodeOr,true);
			DefaultTreeModel model=(DefaultTreeModel)treeSIGA2.getModel();
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)model.getRoot();

      		while (restoreObjects(model,node,modelOr,(DefaultMutableTreeNode)modelOr.getRoot())!=0);
      
      		treeSIGA.updateUI();
      		treeSIGA2.updateUI();
		}
		
		catch (Exception e)
		{
      		e.printStackTrace();
    	}
	}

	protected String getDataModified(SIGABaseNode nod, DefaultMutableTreeNode node)
	{
    	SIGABaseNode pater=(SIGABaseNode)((DefaultMutableTreeNode)node.getParent()).getUserObject();
    	
    	return (nod.getID()+"-"+pater.getID());
	}

	protected void clearObject(SIGABaseNode nod, DefaultMutableTreeNode node,boolean cancelar)
	{
    	nod.modified=false;
	}

	protected int restoreObjects(DefaultTreeModel model,DefaultMutableTreeNode node,DefaultTreeModel modelOrig,DefaultMutableTreeNode nodeRootOr) throws Exception
	{
    	SIGABaseNode obj=(SIGABaseNode)node.getUserObject();
    	int ret=0;
    	
    	if (obj.modified)
    	{
      		DefaultMutableTreeNode nodeOrig=findObject(modelOrig,nodeRootOr,node);
      		DefaultMutableTreeNode nodePater=(DefaultMutableTreeNode)nodeOrig.getParent();
      		DefaultMutableTreeNode pater=findObject(model,(DefaultMutableTreeNode)model.getRoot(),nodePater);
      		node.removeFromParent();
      		pater.setAllowsChildren(true);
      		model.insertNodeInto(node,pater,pater.getChildCount());
      		clearObject(obj,node,true);
      		ret++;
		}
    
    	Enumeration e=node.children();
    	
    	while (e.hasMoreElements())
    	{
      		ret+=restoreObjects(model,(DefaultMutableTreeNode )e.nextElement(),modelOrig,nodeRootOr);
    	}
    	
    	return ret;
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
}