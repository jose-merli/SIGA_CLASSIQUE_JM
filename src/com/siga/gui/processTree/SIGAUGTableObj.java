package com.siga.gui.processTree;

import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

public class SIGAUGTableObj extends SIGABaseNode implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4674761251883765043L;
	private static final String CLASSNAME = SIGAGrupoObj.class.getName();
  	String imagen;
  	ImageIcon imagn;
  	
  	public SIGAUGTableObj() { }

  	public String toString()
  	{
    	String name=(String)get("value");
    	
    	if (name!=null)
    	{
      		return name;
      	}
    	
    	return "";
  	}

  	public SIGAUGTableObj(String _urlIcon, String _imagen)
  	{
    	imagen=_imagen;
    	urlIcon=_urlIcon;
    	loadImageIcons();
  	}

  	protected void loadImageIcons()
  	{
    	java.net.URL url=null;
    	
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

  	public Vector dndMovement(SIGABaseNode destino)
  	{
    	//destino.put(ACCESS, getID());
    	destino.modified=true;
    	Vector vec=new Vector();
    	vec.add("OK");
    	
    	return vec;
  	}
  	
  	public void dndMovement2 (SIGABaseNode destino)
  	{
  	    destino.modified=true;
  	}
}