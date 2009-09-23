package com.siga.gui.processTree;

import javax.swing.*;
import java.awt.dnd.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.*;

public class SIGAUsuariosGruposTree extends JTree
{
  	JApplet applet=null;
  	
  	public SIGAUsuariosGruposTree(DefaultTreeModel model, String urlPrefix, int dnd, JApplet _applet, String icontree)
  	{
    	super(model);
    	applet=_applet;
    	java.net.URL urlPlus=null;
    	java.net.URL urlMin=null;

    	String UrlIcons= urlPrefix + icontree;

    	try
    	{
      		urlPlus = new java.net.URL(UrlIcons + "PlusHot.GIF");
      		urlMin= new java.net.URL(UrlIcons + "MinHot.GIF");
    	}
    	
    	catch (java.net.MalformedURLException mue)
    	{
    		mue.printStackTrace();
    	}

		ImageIcon imageiconPlus = new ImageIcon(urlPlus);
		ImageIcon imageiconMin = new ImageIcon(urlMin);
		
		BasicTreeUI treeUi=(BasicTreeUI)this.getUI();
		
		treeUi.setExpandedIcon(imageiconMin);
		treeUi.setCollapsedIcon(imageiconPlus);

    	if ((SIGAPTConstants.DRAG&dnd)!=0)
    	{
      		new SIGAUGDrag(this, DnDConstants.ACTION_COPY, true);
    	}
    	
    	if ((SIGAPTConstants.DROP&dnd)!=0)
    	{
      		new SIGAUGDrop(this, DnDConstants.ACTION_COPY, applet);
    	}
  	}
}