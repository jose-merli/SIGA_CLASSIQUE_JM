package com.siga.gui.processTree;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

class SIGAPTCellHandler extends javax.swing.tree.DefaultTreeCellRenderer
{
  	/**
	 * 
	 */
	private static final long serialVersionUID = 3883623320506117652L;
  	ImageIcon imageicon;
  	String URLIcon=null;

  	public SIGAPTCellHandler()
  	{
    	setLayout(null);
  	}

  	public Component getTreeCellRendererComponent  (JTree tree, Object value,boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  	{
    	super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);

    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    	SIGABaseNode obj=(SIGABaseNode)node.getUserObject();

    	this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	this.setToolTipText(node.toString());

    	ImageIcon imageicon=obj.getImageIcon();

    	setIcon(new ImageIcon(imageicon.getImage().getScaledInstance(15,15,1)));

		this.setBackground(obj.getBackground());
		this.setBackgroundNonSelectionColor(obj.getBackgroundNonSelectionColor());
		this.setBackgroundSelectionColor(obj.getBackgroundSelectionColor());
		this.setForeground(obj.getForeground());
		//this.setForeground(Color.RED);
		this.setFont(obj.getFont());
		this.setBorderSelectionColor(obj.getBorderSelectionColor());

		return this;
  	}
}