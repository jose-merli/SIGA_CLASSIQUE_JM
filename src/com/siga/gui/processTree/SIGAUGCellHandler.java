package com.siga.gui.processTree;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

class SIGAUGCellHandler extends javax.swing.tree.DefaultTreeCellRenderer
{
  	private JApplet parentApplet;
  	ImageIcon imageicon;
  	String URLIcon=null;

  	public SIGAUGCellHandler()
  	{
    	setLayout(null);
  	}

  	public Component getTreeCellRendererComponent  (JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  	{
    	super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);

    	Font noConnectedFont = new Font("Arial", Font.BOLD+Font.ITALIC, 11);
    	Font connectedFont = new Font("Arial", Font.BOLD, 11);

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