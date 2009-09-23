package com.siga.gui.processTree;

import javax.swing.tree.*;

public class SIGAProcessNode extends DefaultMutableTreeNode
{
  	private static final String CLASSNAME = SIGAProcessNode.class.getName();
  	
  	public SIGAProcessNode() { }

  	public SIGAProcessNode(Object userObject,boolean allowsChildren)
  	{
    	super(userObject,allowsChildren);
  	}
  	
  	public static DefaultMutableTreeNode newSIGATreeNode(Object userObject,boolean allowsChildren)
  	{
    	SIGAProcessNode ntn = new SIGAProcessNode(userObject,allowsChildren);
    	
    	return ntn;
  	}
}