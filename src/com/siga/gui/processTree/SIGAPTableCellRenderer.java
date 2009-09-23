package com.siga.gui.processTree;

import java.awt.*;
import javax.swing.*;

class SIGAPTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer
{
  	ImageIcon imageicon;
  	String URLIcon=null;

  	public SIGAPTableCellRenderer()
  	{
//    setLayout(null);
  	}

  	public Component getTableCellRendererComponent(JTable table, Object value,boolean sel, boolean expanded, int row, int column)
  	{
    	super.getTableCellRendererComponent(table, value,sel, expanded, row, column);

    	SIGATableObj obj=(SIGATableObj)value;
    	ImageIcon imageicon=obj.getImageIcon();
    	setIcon(new ImageIcon(imageicon.getImage()));
                          //.getScaledInstance(27,27,1)));
   		this.setForeground(new Color(52,36,79));
    	
    	this.setFont(obj.getFont());
    	this.setText(obj.toString());
    	this.setMinimumSize(new Dimension(200,60));
    	
    	return this;
  	}
}