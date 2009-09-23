package com.siga.gui.processTree;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class SIGAProcessHier  implements Serializable
{
  	String idProcess=null;
  	String parent=null;
  	Vector hijos=new Vector();
  	
  	public SIGAProcessHier(String _idProcess, String _parent)
  	{
    	idProcess = _idProcess;
    	parent = _parent;
  	}

  	public boolean addChild(SIGAProcessHier child)
  	{
    	if (idProcess.equals(child.getParent()))
    	{
      		hijos.add(child);
      		
      		return true;
    	}
    	
    	boolean ret=false;
    	
    	for (int h=0;h<hijos.size() && !ret;h++)
    	{
      		ret=((SIGAProcessHier)hijos.elementAt(h)).addChild(child);
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
    	
    	for (int h=0;h<hijos.size();h++)
    	{
      		((SIGAProcessHier)hijos.elementAt(h)).sort(hashorder);
    	}

    	boolean changes = true;
    	
    	while (changes)
    	{
      		changes = false;
      		
      		for (int h=0;h<hijos.size()-1;h++)
      		{
        		SIGAProcessHier ele1=(SIGAProcessHier)hijos.elementAt(h);
        		SIGAProcessHier ele2=(SIGAProcessHier)hijos.elementAt(h+1);
        		SIGABaseNode ele1bn=(SIGABaseNode)hashorder.get(ele1.getId());
        		SIGABaseNode ele2bn=(SIGABaseNode)hashorder.get(ele2.getId());
        		String uno=ele1bn.toString();
        		String dos=ele2bn.toString();
        		int value = (uno.length()>dos.length()?dos.length():uno.length());
        		uno=uno.substring(0,value);
        		dos=dos.substring(0,value);
        		
        		if (uno.compareTo(dos)>0)
        		{
          			hijos.set(h,ele2);
          			hijos.set(h+1,ele1);
          			changes = true;
        		}
      		}
    	}
	}

  	public SIGAProcessHier getChild(String id)
  	{
    	for (int h=0;h<hijos.size();h++)
    	{
      		if (id.equals(((SIGAProcessHier)hijos.elementAt(h)).getId()))
      		{
        		return (SIGAProcessHier)hijos.elementAt(h);
        	}
    	}
    	
    	return null;
  	}

  	public String getId()
  	{
    	return idProcess;
  	}

  	public String getParent()
  	{
    	return parent;
  	}
  	
  	public String getHier(String base)
  	{
    	String toRet=base+"| \n";
    	toRet+=(base + " _ " + idProcess + "\n");
    	
    	for (int h=0;h<hijos.size();h++)
    	{
      		toRet+=((SIGAProcessHier)hijos.elementAt(h)).getHier(base+"     ");
    	}
    	
    	return toRet;
  	}

  	public DefaultMutableTreeNode createTree(Hashtable elements, String urlIcons, String urlServlet, JApplet _applet)
  	{
    	SIGABaseNode myElement=(SIGABaseNode) elements.get(getId());
    	myElement.setImageIcon("process.gif", urlIcons);
    	myElement.setIcon();
    	myElement.setUrlServlet(urlServlet);
    	myElement.setApplet(_applet);
  	    
    	DefaultMutableTreeNode ret = SIGAProcessNode.newSIGATreeNode(myElement,true);

    	for (int h=0;h<hijos.size();h++)
    	{
      		SIGAProcessHier hier=(SIGAProcessHier)hijos.elementAt(h);
      		DefaultMutableTreeNode ret9=hier.createTree(elements,urlIcons,urlServlet,_applet);
      		ret.add(ret9);
    	}
    	
    	if (hijos.size()==0)
    	{
      		ret.setAllowsChildren(false);
    	}
    	
    	return ret;
  	}

  	public boolean compareTree(SIGAProcessHier yo,SIGAProcessHier hier)
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
      		SIGAProcessHier hijo=(SIGAProcessHier)yo.getChilds().elementAt(h);
      		SIGAProcessHier suhijo=hier.getChild(hijo.getId());
      		
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

  	public boolean comparePath(SIGAProcessHier hier, String id)
  	{
    	SIGAProcessHier myPath=getPath(id);
    	SIGAProcessHier suPath=hier.getPath(id);
    	
    	if (myPath==null || suPath==null)
    	{
    		return false;
    	}
    	
    	return compareTree(myPath,suPath);
  	}

  	public SIGAProcessHier getPath(String id)
  	{
    	SIGAProcessHier thisElement=SIGAProcessHier.getElement(this,id);
    	
    	if (thisElement==null)
    	{
    		return null;
    	}
    	
    	String parent=thisElement.getParent();

    	while (parent!=null && !parent.equals(""))
    	{
      		SIGAProcessHier padre=getElement(this,parent);
      		SIGAProcessHier pivote=new SIGAProcessHier(padre.getId(),padre.getParent());
      		pivote.addChild(thisElement);
      		thisElement=pivote;
      		parent=thisElement.getParent();
    	}
    	
    	return thisElement;
  	}

  	public static SIGAProcessHier getElement(SIGAProcessHier base,String id)
  	{
    	if (base.getId().equals(id))
    	{
      		return base;
      	}

    	for (int h=0;h<base.getChilds().size();h++)
    	{
      		SIGAProcessHier toRet=SIGAProcessHier.getElement((SIGAProcessHier)base.getChilds().elementAt(h),id);
      		
      		if (toRet!=null)
      		{
        		return toRet;
        	}
    	}
    	
    	return null;
  	}
}