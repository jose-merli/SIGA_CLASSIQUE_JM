package com.siga.gui.processTree;

import java.awt.*;
import javax.swing.*;

class SIGAUGTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer
{
  	ImageIcon imageicon;
  	String URLIcon=null;

  	public SIGAUGTableCellRenderer()
  	{
//    setLayout(null);
  	}

  	public Component getTableCellRendererComponent(JTable table, Object value, boolean sel, boolean expanded, int row, int column)
  	{
    	super.getTableCellRendererComponent(table, value,sel, expanded, row, column);

    	SIGAUGTableObj obj=(SIGAUGTableObj)value;
    	ImageIcon imageicon=obj.getImageIcon();
    	setIcon(new ImageIcon(imageicon.getImage()));

    	if (row==0)
    	{
    		this.setForeground(new Color(248,52,52));
    	}
    	
    	else
    	{
    		this.setForeground(new Color(56,172,20));
    	}
    	
    	this.setFont(obj.getFont());
    	this.setText(obj.toString());
    	this.setMinimumSize(new Dimension(200,60));
    	
    	return this;
  	}
}