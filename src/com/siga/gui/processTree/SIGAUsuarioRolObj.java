package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class SIGAUsuarioRolObj extends SIGABaseNode implements Serializable, Comparator
{
	private static final String CLASSNAME = SIGAGrupoObj.class.getName();
	static protected ImageIcon imageiconLic=null;
	
	private String idUsuario = "";
	private String idRol = "";
	
	public String getIdUsuario()
	{
		return idUsuario;
	}
	
	public void setIdUsuario(String idUsuario)
	{
	    this.idUsuario=idUsuario;
	}
	
	public String getIdRol()
	{
		return idRol;
	}
	
	public void setIdRol(String idRol)
	{
	    this.idRol=idRol;
	}

	public SIGAUsuarioRolObj() { }

	public void restoreState()
	{
    	//put(ACCESS,oldState);
    	changestate=false;
	}

	public String toString()
	{
    	return ((String)get(NAME));
	}

	public Color getForeground()
	{
    	if (modified)
    	{
    		return new Color(254,184,6);
    	}
    	
    	else
    	{
    		return new Color(52,36,79);
    	}
	}

	protected void loadImageIcons()
	{
    	java.net.URL url=null;
    	java.net.URL urlGeneric=null;
    	
    	try
    	{
      		url = new java.net.URL(urlIcon+"lic.gif");
      		imageiconLic= new ImageIcon(url);
      		Object ob=url.getContent();
    	}
    	
    	catch (Exception mue)
    	{
      		mue.printStackTrace();
    	}
	}

	public ImageIcon getImageIcon()
	{
    	if (imageiconLic==null)
    	{
      		loadImageIcons();
    	}
    	
    	return imageiconLic;
	}

	public Vector dndMovement(SIGABaseNode destino) throws Exception
	{
    	Vector vec=new Vector();
    	String fun=URLEncoder.encode(actionMove,"ISO-8859-1");
    	String oIDmovido=URLEncoder.encode((String)getID(),"ISO-8859-1");
    	String oIDdestino=URLEncoder.encode((String)destino.getID(),"ISO-8859-1");
    	String sUrl = urlServlet+"?"+SERVLET_FUNCTION+"="+fun+"&"+OIDMOVED+"="+oIDmovido+"&"+OIDTARGET+"="+oIDdestino;
    	URL direction = new URL(sUrl);
    	
    	return vec;
	}
}