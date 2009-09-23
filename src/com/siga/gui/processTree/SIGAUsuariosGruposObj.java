package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class SIGAUsuariosGruposObj extends SIGABaseNode implements Serializable, Comparator
{
	private static final String CLASSNAME = SIGAGrupoObj.class.getName();
	static protected ImageIcon imageiconLic=null;

	public SIGAUsuariosGruposObj() { }

	public void restoreState()
	{
    	put(ACCESS,oldState);
    	changestate=false;
	}

	public String toString()
	{
    	return ((String)get(NAME));
	}

	public Color getForeground()
	{
    	return new Color(52,36,79);
	}

	public Font getFont()
	{
    	if (changestate || modified)
    	{
      		return new Font("Arial", Font.ITALIC|Font.BOLD, 11);
    	}
    	
    	else
    	{
      		return super.getFont();
    	}
	}

	protected void loadImageIcons()
	{
    	java.net.URL url=null;//the url to show the gif for each node
    	java.net.URL urlGeneric=null;//to show a generic gif if the specific one doesn't exist
    	
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