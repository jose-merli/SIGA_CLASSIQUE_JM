package com.siga.gui.processTree;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class SIGAUsuariosGruposHier  implements Serializable
{
  	//String idPerfil=null;
    String idUsuario=null;
  	String parent=null;
  	Vector hijos=new Vector();
  	
  	//public SIGAUsuariosGruposHier(String _idPerfil, String _parent)
  	public SIGAUsuariosGruposHier(String _idUsuario, String _parent)
  	{
    	//idPerfil = _idPerfil;
  	    idUsuario = _idUsuario;
    	parent = _parent;
  	}

  	public boolean addChild(SIGAUsuariosGruposHier child)
  	{
    	//if (idPerfil.equals(child.getParent()))
  	    if (idUsuario.equals(child.getParent()))
    	{
      		hijos.add(child);
      		
      		return true;
    	}
    	
    	boolean ret=false;
    	
    	int iHijos = hijos.size();
    	
    	for (int h=0;h<iHijos && !ret;h++)
    	{
      		ret=((SIGAUsuariosGruposHier)hijos.elementAt(h)).addChild(child);
    	}
    	
    	return ret;
  	}
  
  	public Vector getChilds()
  	{
		return hijos;
  	}

  	public void sort(Hashtable hashorder)
  	{
    	if (hijos.size()==0)
    	{
      		return;
      	}
    	
    	Hashtable has = new Hashtable();
    	
    	int iHijos=hijos.size();
    	
    	for (int h=0; h<iHijos; h++)
    	{
      		((SIGAUsuariosGruposHier)hijos.elementAt(h)).sort(hashorder);
    	}

    	boolean changes = true;
    	
    	while (changes)
    	{
      		changes = false;
      		
      		int iHijos2 = iHijos-1;
      		
      		for (int h=0; h<iHijos2; h++)
      		{
      			int h2 = h+1;
        		SIGAUsuariosGruposHier ele1=(SIGAUsuariosGruposHier)hijos.elementAt(h);
        		SIGAUsuariosGruposHier ele2=(SIGAUsuariosGruposHier)hijos.elementAt(h2);
        		SIGABaseNode ele1bn=(SIGABaseNode)hashorder.get(ele1.getId());
        		SIGABaseNode ele2bn=(SIGABaseNode)hashorder.get(ele2.getId());
        		String uno=ele1bn.toString();
        		String dos=ele2bn.toString();
        		int lengthuno = uno.length();
        		int lengthdos = dos.length();
        		int value = (lengthuno>lengthdos ? lengthdos : lengthuno);
        		uno=uno.substring(0, value);
        		dos=dos.substring(0, value);
        		
        		if (uno.compareTo(dos)>0)
        		{
          			hijos.set(h, ele2);
          			hijos.set(h2, ele1);
          			changes = true;
        		}
      		}
    	}
	}

  	public SIGAUsuariosGruposHier getChild(String id)
  	{
  		int iHijos = hijos.size();
  		
    	for (int h=0;h<iHijos;h++)
    	{
      		if (id.equals(((SIGAUsuariosGruposHier)hijos.elementAt(h)).getId()))
      		{
        		return (SIGAUsuariosGruposHier)hijos.elementAt(h);
        	}
    	}
    	
    	return null;
  	}

  	public String getId()
  	{
    	//return idPerfil;
  	    return idUsuario;
  	}

  	public String getParent()
  	{
    	return parent;
  	}
  	
  	public String getHier(String base)
  	{
    	String toRet=base+"| \n";
    	//toRet+=(base + " _ " + idPerfil + "\n");
    	toRet+=(base + " _ " + idUsuario + "\n");
    	
    	for (int h=0;h<hijos.size();h++)
    	{
      		toRet+=((SIGAUsuariosGruposHier)hijos.elementAt(h)).getHier(base+"     ");
    	}
    	
    	return toRet;
  	}

  	public DefaultMutableTreeNode createTree(Hashtable elements, String urlIcons, String urlServlet, JApplet _applet)
  	{
    	SIGABaseNode myElement=(SIGABaseNode) elements.get(getId());
    	myElement.setImageIcon("lic.gif", urlIcons);
    	myElement.setIcon();
    	myElement.setUrlServlet(urlServlet);
    	myElement.setApplet(_applet);
  	    
    	DefaultMutableTreeNode ret = SIGAUsuarioRolNode.newSIGATreeNode(myElement, true);
    	
    	int iHijos = hijos.size();

		SIGAUsuariosGruposHier hier=null;
		DefaultMutableTreeNode ret9=null;
		
    	for (int h=0; h<iHijos; h++)
    	{
      		hier=(SIGAUsuariosGruposHier)hijos.elementAt(h);
      		ret9=hier.createTree(elements, urlIcons, urlServlet, _applet);
      		ret.add(ret9);
    	}

    	if (iHijos==0)
    	{
      		ret.setAllowsChildren(false);
    	}

    	return ret;
  	}

  	public boolean compareTree(SIGAUsuariosGruposHier yo,SIGAUsuariosGruposHier hier)
  	{
    	if (!yo.getId().equals(hier.getId()))
    	{
      		return false;
    	}
    	
    	if (yo.getParent()==null && hier.getParent()!=null)
    	{
      		return false;
    	}
    	
    	if (yo.getParent()!=null)
    	{
      		if (!yo.getParent().equals(hier.getParent()))
      		{
        		return false;
      		}
    	}
    	
    	else if (hier.getParent()!=null)
    	{
			return false;
    	}
    	
    	if (yo.getChilds().size()!=hier.getChilds().size())
    	{
      		return false;
    	}

    	for (int h=0;h<yo.getChilds().size();h++)
    	{
      		SIGAUsuariosGruposHier hijo=(SIGAUsuariosGruposHier)yo.getChilds().elementAt(h);
      		SIGAUsuariosGruposHier suhijo=hier.getChild(hijo.getId());
      		
      		if (suhijo==null)
      		{
      			return false;
      		}
      		
      		boolean ret=compareTree(hijo, suhijo);
      		
      		if (!ret)
      		{
      			return false;
      		}
    	}
    	
    	return true;
  	}

  	public boolean comparePath(SIGAUsuariosGruposHier hier, String id)
  	{
    	SIGAUsuariosGruposHier myPath=getPath(id);
    	SIGAUsuariosGruposHier suPath=hier.getPath(id);
    	
    	if (myPath==null || suPath==null)
    	{
    		return false;
    	}
    	
    	return compareTree(myPath,suPath);
  	}

  	public SIGAUsuariosGruposHier getPath(String id)
  	{
    	SIGAUsuariosGruposHier thisElement=SIGAUsuariosGruposHier.getElement(this,id);
    	
    	if (thisElement==null)
    	{
    		return null;
    	}
    	
    	String parent=thisElement.getParent();

    	while (parent!=null && !parent.equals(""))
    	{
      		SIGAUsuariosGruposHier padre=getElement(this,parent);
      		SIGAUsuariosGruposHier pivote=new SIGAUsuariosGruposHier(padre.getId(),padre.getParent());
      		pivote.addChild(thisElement);
      		thisElement=pivote;
      		parent=thisElement.getParent();
    	}
    	
    	return thisElement;
  	}

  	public static SIGAUsuariosGruposHier getElement(SIGAUsuariosGruposHier base,String id)
  	{
    	if (base.getId().equals(id))
    	{
      		return base;
      	}

    	for (int h=0;h<base.getChilds().size();h++)
    	{
      		SIGAUsuariosGruposHier toRet=SIGAUsuariosGruposHier.getElement((SIGAUsuariosGruposHier)base.getChilds().elementAt(h),id);
      		
      		if (toRet!=null)
      		{
        		return toRet;
        	}
    	}
    	
    	return null;
  	}
}