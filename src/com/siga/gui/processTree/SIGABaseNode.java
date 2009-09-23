package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

abstract public class SIGABaseNode extends Hashtable implements Serializable,SIGAPTConstants
{
	protected String nodeIconName = "";
	protected String urlIcon = "";
	static protected ImageIcon imageicon=null;
	protected String urlServlet=null;
	protected String actionMove=null;
	protected JApplet applet;
	public boolean modified=false;
	public boolean changestate=false;
	public boolean added=false;
	public boolean removed=false;
	static Color defaultColor = null;
	static Font connectedFont = null;
	protected String oldState=null;
	
	private Color colorForeground=null;

	public SIGABaseNode() { }
	
	public void setURLIcon(String sURL)
	{
	    urlIcon = sURL;
	}

	public void restoreState()
	{
    	changestate=false;
	}
	
	public void setOldState(String _oldState)
	{
    	oldState=_oldState;
  	}

	public String getOldState()
	{
    	return oldState;
  	}

	public void setChangeState(boolean _changestate)
	{
    	changestate=_changestate;
    	//modified=changestate;
  	}

  	public boolean getChangeState() 
  	{
    	return changestate;
  	}

  	public int compare(Object o1, Object o2)
  	{
    	return ((SIGABaseNode)o1).getID().compareTo(((SIGABaseNode)o2).getID());
  	}

  	public boolean equals(Object obj)
  	{
    	return getID().equals(((SIGABaseNode)obj).getID());
  	}
  	
  	public void setUrlServlet(String _urlServlet)
  	{
    	urlServlet=_urlServlet;
  	}

  	public void setActionMove(String _actionMove)
  	{
    	actionMove=_actionMove;
  	}

  	public void setApplet(JApplet _applet)
  	{
    	applet=_applet;
  	}

  	public JApplet setID()
  	{
    	return applet;
  	}

  	public String getID()
  	{
    	return (String)get(OID);
  	}

  	public void setID(String id)
  	{
    	put(OID,id);
  	}

  	public String toString()
  	{
    	return (String)get(NAME);
  	}

	public void setIcon()
	{
    	java.net.URL url=null;//the url to show the gif for each node
    	//java.net.URL urlGeneric=null;//to show a generic gif if the specific one doesn't exist
    	
    	try
    	{
      		url = new java.net.URL(urlIcon +nodeIconName);
      		//urlGeneric= new java.net.URL(urlIcon + "Bola.gif");
    	}
    	
    	catch (java.net.MalformedURLException mue)
    	{
    		mue.printStackTrace();
    	}
    	
    	ImageIcon imageicon2 = new ImageIcon(url);
    	
    	try
    	{
      		if (url != null)
      		{
        		//Object ob=url.getContent();
      		}
    	}
    	
    	catch(Exception e)
    	{
      		try
      		{
      			imageicon2 = new ImageIcon(new java.net.URL(urlIcon + "Bola.gif"));
      		}
      		
      		catch(Exception ex)
      		{
      			ex.printStackTrace();
      		}
    	}
    	
    	imageicon=imageicon2;
	}

	public ImageIcon getImageIcon()
	{
    	if (imageicon==null)
    	{
      		setIcon();
      	}
      	
    	return SIGABaseNode.imageicon;
  	}

	public void setImageIcon( String iconName, String URLicon)
	{
    	nodeIconName = iconName;
    	urlIcon = URLicon;
  	}

	public String getNodeIconName()
	{
    	return nodeIconName;
  	}

  	public void setNodeIconName(String a_NodeIconName)
  	{
    	nodeIconName = a_NodeIconName;
  	}

	abstract public Vector dndMovement(SIGABaseNode destino) throws Exception;

	public Color getBackground()
	{
    	if (defaultColor==null)
    	{
      		defaultColor = new Color(238,237,243);
      	}
    	
    	return defaultColor;
  	}
  	
	public Color getBackgroundNonSelectionColor()
	{
    	if (defaultColor==null)
    	{
      		defaultColor = new Color(238,237,243);
      	}
    	
    	return defaultColor;
	}

	public Color getBackgroundSelectionColor()
	{
    	return Color.white;
  	}
  	
  	public Color getForeground()
  	{
    	return Color.black;
  	}
  	
  	public Font getFont()
  	{
    	if (connectedFont==null)
    	{
      		connectedFont = new Font("Arial", Font.BOLD, 11);
      	}
    	
    	return connectedFont;
  	}
  	
  	public Color getBorderSelectionColor()
  	{
    	return Color.black;
  	}
  	
  	public void setAdded(boolean added)
  	{
  		this.added=added;
  	}
  	
  	public boolean getAdded()
  	{
  		return added;
  	}

  	public void setRemoved(boolean removed)
  	{
  		this.removed=removed;
  	}
  	
  	public boolean getRemoved()
  	{
  		return removed;
  	}
}