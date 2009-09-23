package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;

public abstract class SIGAAppletBase extends JApplet
{
	public Hashtable literales=null;
	public Vector vGrupos=null;
  	protected String access="DENY";
  	public String urlPrefix;
  	public String URLjspsTree;
  	public String URLservlet;
  	public String iconos;
  	public String icontree;
  	public String initAction=null;
  	protected int numberOfParams=0;
  	public String[] params=null;
  	protected String process;
  	protected JButton guardar=null;
  	protected JButton cancelar=null;

  	protected void createObjects()
  	{
    	guardar=new JButton((String)literales.get("GUARDAR"));
    	cancelar=new JButton((String)literales.get("CANCELAR"));

		guardar.setBackground(new java.awt.Color(255, 199, 59));
		guardar.setForeground(new java.awt.Color(72, 36, 79));
		guardar.setDoubleBuffered(true);
		guardar.setFont(new Font("Dialog", Font.BOLD, 11));

		Border border1 = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(247, 140, 0));
		
		guardar.setBackground(new Color(247, 218, 151));
		guardar.setBorder(border1);

    	cancelar.setBackground(new java.awt.Color(255, 199, 59));
    	cancelar.setForeground(new java.awt.Color(72, 36, 79));
    	cancelar.setDoubleBuffered(true);
    	cancelar.setFont(new Font("Dialog", Font.BOLD, 11));

    	cancelar.setBackground(new Color(247, 218, 151));
    	cancelar.setBorder(border1);

    	guardar.addActionListener(new ActionListener()
    	{
      		public void actionPerformed(ActionEvent e)
      		{
        		guardar();
      		}
    	});

		cancelar.addActionListener(new ActionListener()
		{
      		public void actionPerformed(ActionEvent e)
      		{
        		cancelar();
      		}
    	});
	}

	protected void readParameters()
	{
    	try
    	{
      		urlPrefix = getParameter("urlPrefix");
      		URLjspsTree = getParameter("URLjspsTree");
      		URLservlet = getParameter("URLservlet");
      		access = getParameter("access");
      		initAction = getParameter("initAction");
      		process = getParameter("process");
      		iconos = getParameter("iconPrefix");
      		String number = getParameter("numberofparams");
      		icontree = getParameter("icontree");
      		
      		if (number==null || number.length()==0)
      		{
        		numberOfParams=0;
        	}
      		
      		else
      		{
      			try
      			{
        			numberOfParams=Integer.parseInt(number);
        		}
      	  		
        		catch (Exception ec)
        		{
          			numberOfParams=0;
        		}
      		}
      		
      		params=new String[numberOfParams];
      		
      		for (int i=0;i<numberOfParams;i++)
      		{
        		params[i]=getParameter("param"+i);
      		}

      		getLiterales();

			if (initAction.equals(SIGAPTConstants.LOAD_USUARIOS_GRUPO))
			{
      			getGrupos();
      		}
      	}
      	
      	catch (Exception e)
      	{
      		e.printStackTrace();
      	}
	}
	
	protected Vector fillModified(DefaultTreeModel model, DefaultMutableTreeNode node, Vector vec)
	{
    	SIGABaseNode obj=(SIGABaseNode)node.getUserObject();
    	
    	if (obj.modified)
    	{
      		vec.add(getDataModified(obj, node));
    	}
    	
    	Enumeration e=node.children();
    	
    	while (e.hasMoreElements())
    	{
      		vec=fillModified(model, (DefaultMutableTreeNode )e.nextElement(), vec);
    	}
    	
    	return vec;
	}

	protected void clearObjects(JTree arbol, DefaultTreeModel model, DefaultMutableTreeNode node)
	{
	    SIGABaseNode obj=(SIGABaseNode)node.getUserObject();

	    if (obj.added)
		{
		    model.removeNodeFromParent(node);
		    //arbol.setModel(model);
		}
	    
	    else if (obj.removed)
	    {
            obj.setRemoved(false);
	            
            clearObject(obj, node);
	    }
		
		else
		{
	    	if (obj.modified || obj.changestate)
	    	{
	      		clearObject(obj, node);
	    	}
	    	
	    	Enumeration e=node.children();
	    	
	    	for (int j =node.getChildCount(); j>0; j--)
	    	{
	    	    clearObjects(arbol, model, (DefaultMutableTreeNode)node.getChildAt(j-1));
	    	}
	    }
	    
	}
	
	protected void clearObjects(JTree arbol, DefaultTreeModel model, DefaultMutableTreeNode node, boolean cancelar)
	{
	    //model = (DefaultTreeModel)arbol.getModel();
	    
	    SIGABaseNode obj=(SIGABaseNode)node.getUserObject();

	    if (obj.added)
		{
		    model.removeNodeFromParent(node);
		    
		    //arbol.setModel(model);
		}
	    
	    else if (obj.removed)
	    {
	        /*if (obj.added)
	        {
			    model.removeNodeFromParent(node);
			    
			    arbol.setModel(model);
	        }
	        
	        else
	        {*/
	            obj.setRemoved(false);
	            
	            clearObject(obj, node, cancelar);
	        //}
	    }
		
		else
		{
	    	if (obj.modified || obj.changestate)
	    	{
	      		clearObject(obj, node, cancelar);
	    	}
	    	
	    	Enumeration e=node.children();
	    	
	    	for (int j =node.getChildCount(); j>0; j--)
	    	{
	    	    clearObjects(arbol, model, (DefaultMutableTreeNode)node.getChildAt(j-1), cancelar);
	    	}
	    }
	}

	protected abstract String getDataModified(SIGABaseNode nod, DefaultMutableTreeNode node);
	protected abstract void clearObject(SIGABaseNode nod, DefaultMutableTreeNode node, boolean cancelar);
	protected void clearObject(SIGABaseNode nod, DefaultMutableTreeNode node) {}

	public boolean checkStructure(DefaultMutableTreeNode padre, SIGAProcessHier hier) throws Exception
	{
    	SIGABaseNode obj=(SIGABaseNode)padre.getUserObject();
    
    	if (obj==null || hier==null)
    	{
      		return false;
      	}

    	if (!hier.getId().equals(obj.getID()))
    	{
      		return false;
    	}

    	Enumeration a=padre.children();
    	
    	while (a.hasMoreElements())
    	{
      		DefaultMutableTreeNode node=(DefaultMutableTreeNode)a.nextElement();
      		SIGABaseNode objchild=(SIGABaseNode)node.getUserObject();
      		
      		if (!checkStructure(node, hier.getChild(objchild.getID())))
      		{
        		return false;
        	}
    	}
    	
    	return true;
	}

	protected void getLiterales() throws Exception
	{
    	String servFunction=SIGAPTConstants.SERVLET_FUNCTION;
    	String fun = URLEncoder.encode(SIGAPTConstants.LITERALES, "ISO-8859-1");
    	String sUrl = URLservlet+"?"+servFunction+"="+fun;

    	try
    	{
      		URL direction = new URL(sUrl);
      		URLConnection conexion = direction.openConnection();
      		conexion.setUseCaches (false);
      		conexion.connect();
      		ObjectInputStream input = new ObjectInputStream (conexion.getInputStream());
      		Object o = input.readObject();;
      		input.close();
      		literales=(Hashtable)o; 
    	}
    	
    	catch (Exception e)
    	{
      		e.printStackTrace();
    	}
	}

	protected void getGrupos() throws Exception
	{
    	String servFunction=SIGAPTConstants.SERVLET_FUNCTION;
    	String fun = URLEncoder.encode(SIGAPTConstants.LOAD_GRUPOS, "ISO-8859-1");
    	String sUrl = URLservlet+"?"+servFunction+"="+fun;

    	try
    	{
      		URL direction = new URL(sUrl);
      		URLConnection conexion = direction.openConnection();
      		conexion.setUseCaches (false);
      		conexion.connect();
      		ObjectInputStream input = new ObjectInputStream (conexion.getInputStream());
      		Object o = input.readObject();;
      		input.close();
      		vGrupos=(Vector) o;
      		
    	}
    	
    	catch (Exception e)
    	{
      		e.printStackTrace();
    	}
	}

	abstract public void cancelar();
	abstract public void guardar();
}