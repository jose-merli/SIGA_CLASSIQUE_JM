package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class SIGAAccessObj extends SIGABaseNode implements Serializable, Comparator
{
	private static final String CLASSNAME = SIGAProcessObj.class.getName();
	static protected ImageIcon imageiconFull=null;
	static protected ImageIcon imageiconRead=null;
	static protected ImageIcon imageiconDeny=null;
	static protected ImageIcon imageiconNone=null;

	public SIGAAccessObj() { }

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
    	String access = (String)get(ACCESS);
    	
		if (ACCESS_DENY.equals(access))
		{
			return new Color(248,52,52);
		}
    
		if (ACCESS_READ.equals(access))
		{
			return new Color(254,184,6);
		}
    
		if (ACCESS_FULL.equals(access))
		{
			return new Color(56,172,20);
		}
    
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
      		url = new java.net.URL(urlIcon+"accessFull.gif");
      		imageiconFull= new ImageIcon(url);
      		Object ob=url.getContent();
    	}
    	
    	catch (Exception mue)
    	{
      		mue.printStackTrace();
    	}

    	try
    	{
      		url = new java.net.URL(urlIcon+"accessRead.gif");
      		imageiconRead= new ImageIcon(url);
      		Object ob=url.getContent();
    	}
    	
    	catch (Exception mue)
    	{
    		mue.printStackTrace();
    	}

		try
		{
      		url = new java.net.URL(urlIcon+"accessDeny.gif");
      		imageiconDeny= new ImageIcon(url);
      		Object ob=url.getContent();
		}
		
		catch (Exception mue)
		{
			mue.printStackTrace();
    	}

    	try
    	{
      		url = new java.net.URL(urlIcon+"accessNone.gif");
      		imageiconNone= new ImageIcon(url);
      		Object ob=url.getContent();
    	}
    	
    	catch (Exception mue) 
    	{
    		mue.printStackTrace();
    	}
	}

	public ImageIcon getImageIcon()
	{
    	if (imageiconFull==null)
    	{
      		loadImageIcons();
    	}
    
    	String access = (String)get(ACCESS);
    
    	if (ACCESS_DENY.equals(access))
    	{
      		return imageiconDeny;
      	}
      	
    	if (ACCESS_READ.equals(access))
    	{
      		return imageiconRead;
      	}
      	
    	if (ACCESS_FULL.equals(access))
    	{
      		return imageiconFull;
      	}
      	
    	if (ACCESS_NONE.equals(access))
    	{
      		return imageiconNone;
      	}
    	
    	return imageiconNone;
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