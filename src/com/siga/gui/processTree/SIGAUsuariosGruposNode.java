package com.siga.gui.processTree;

import javax.swing.tree.*;

public class SIGAUsuariosGruposNode extends DefaultMutableTreeNode
{
  	private static final String CLASSNAME = SIGAUsuariosGruposNode.class.getName();
  	
  	public SIGAUsuariosGruposNode() { }

  	public SIGAUsuariosGruposNode(Object userObject,boolean allowsChildren)
  	{
    	super(userObject,allowsChildren);
  	}
  	
  	public static DefaultMutableTreeNode newSIGATreeNode(Object userObject,boolean allowsChildren)
  	{
    	SIGAUsuariosGruposNode ntn = new SIGAUsuariosGruposNode(userObject,allowsChildren);
    	
    	return ntn;
  	}
}