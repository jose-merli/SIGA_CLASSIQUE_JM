package com.siga.gui.processTree;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

class SIGANodeUsuariosGrupoModel extends javax.swing.tree.DefaultTreeModel
{
	JApplet parentApplet;
  	protected String funcion;

  	public SIGANodeUsuariosGrupoModel(TreeNode root)
  	{
    	super(root);
  	}

  	public SIGANodeUsuariosGrupoModel(TreeNode root, boolean asksAllowsChildren, JApplet _applet)
  	{
    	super(root, asksAllowsChildren);
    	parentApplet=_applet;
  	}

  	public static DefaultTreeModel newSIGANodeTreeModelSIGA(String UrlServlet, String urlPrefix, String _funcion, String[] params, JApplet _applet, String iconos)
  	{
    	String UrlIcons= iconos;
    	Vector vecRet=null;

    	try
    	{
      		String fun=URLEncoder.encode(_funcion, "ISO-8859-1");
      		String sUrl = UrlServlet+"?"+SIGAPTConstants.SERVLET_FUNCTION+"="+fun;

      		if (params!=null)
      		{
      			int iParams = params.length;
      			
        		for (int i=0;i<iParams;i++)
        		{
          			sUrl+="&"+URLEncoder.encode(params[i++], "ISO-8859-1") +"="+URLEncoder.encode(params[i], "ISO-8859-1");
        		}
      		}
      		
      		URL direction = new URL(sUrl);
      		URLConnection conexion = direction.openConnection();
      		conexion.setUseCaches (false);
      		conexion.connect();
      		ObjectInputStream inputAF = new ObjectInputStream (conexion.getInputStream());
      		Object o =  inputAF.readObject();
      		inputAF.close();
      		vecRet=(Vector)o;
      		
    	}
    	
    	catch(Exception e)
    	{
      		e.printStackTrace();
    	}
    	
    	String URLiconsAf = urlPrefix;
    	SIGAUsuariosGruposHier hier = null;
    	Hashtable elements = null;
    	DefaultMutableTreeNode root= null;

    	if (vecRet != null)
    	{
      		hier=(SIGAUsuariosGruposHier)vecRet.elementAt(0);
      		elements =(Hashtable)vecRet.elementAt(1);
    	}
    	
    	if (hier != null)
    	{
      		root=hier.createTree(elements, UrlIcons, UrlServlet, _applet);
    	}
    	
    	return new SIGANodeUsuariosGrupoModel(root, true, _applet);
  	}

  	public static DefaultTreeModel newSIGANodeTreeModelSIGA(String UrlServlet, String urlPrefix, String _funcion, String[] params, JApplet _applet, SIGAUsuariosGruposHier hier, Hashtable elements, String iconos)
	{
     	String UrlIcons= iconos;
     	Vector vecRet=null;
     	String URLiconsAf = urlPrefix;
     	DefaultMutableTreeNode root=hier.createTree(elements, UrlIcons, UrlServlet, _applet);
     	
     	return new SIGANodeUsuariosGrupoModel(root, true, _applet);
  	}

  	static private void setAllowsChildresAll(DefaultMutableTreeNode node, boolean value)
  	{
    	node.setAllowsChildren(value);
    	Enumeration e=node.children();
    	
    	while (e.hasMoreElements())
    	{
      		setAllowsChildresAll((DefaultMutableTreeNode )e.nextElement(), value);
      	}
  	}
}