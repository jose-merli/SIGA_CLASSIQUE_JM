package com.siga.gui.processTree;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class SIGATableObj extends SIGABaseNode implements Serializable
{
	private static final String CLASSNAME = SIGAProcessObj.class.getName();
  	String imagen;
  	ImageIcon imagn;

  	public SIGATableObj() { }

  	public String toString()
  	{
    	String name=(String)get("value");
    	
    	if (name!=null)
    	{
      		return name;
      	}
    	
    	return "";
  	}

  	public SIGATableObj(String _urlIcon,String _imagen)
  	{
    	imagen=_imagen;
    	urlIcon=_urlIcon;
    	loadImageIcons();
  	}

  	protected void loadImageIcons()
  	{
    	java.net.URL url=null;//the url to show the gif for each node
    	java.net.URL urlGeneric=null;//to show a generic gif if the specific one doesn't exist
    	
    	try
    	{
      		url = new java.net.URL(urlIcon+imagen);
      		ImageIcon icon =new ImageIcon(url);
      		Object ob=url.getContent();
      		imagn= icon;
    	}
    	
    	catch (Exception mue)
    	{
      		mue.printStackTrace();
    	}
  	}

  	public ImageIcon getImageIcon()
  	{
		return imagn;
  	}

  	public Vector dndMovement(SIGABaseNode destino) throws Exception
  	{
    	destino.put(ACCESS,getID());
    	destino.modified=true;
    	Vector vec=new Vector();
    	vec.add("OK");
    	
    	return vec;
  	}
}