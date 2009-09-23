package com.siga.gui.processTree;

import javax.swing.tree.*;

public class SIGAUsuarioRolNode extends DefaultMutableTreeNode
{
  	private static final String CLASSNAME = SIGAUsuarioRolNode.class.getName();
  	
  	public SIGAUsuarioRolNode() { }
  	
  	public SIGAUsuarioRolNode(Object userObject)
  	{
  	    super(userObject);
  	}

  	public SIGAUsuarioRolNode(Object userObject, boolean allowsChildren)
  	{
    	super(userObject, allowsChildren);
  	}
  	
  	public static DefaultMutableTreeNode newSIGATreeNode(Object userObject, boolean allowsChildren)
  	{
    	return new SIGAUsuarioRolNode(userObject, allowsChildren);
  	}
}