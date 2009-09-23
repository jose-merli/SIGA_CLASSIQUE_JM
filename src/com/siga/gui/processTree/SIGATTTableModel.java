package com.siga.gui.processTree;

import javax.swing.*;
import javax.swing.table.*;

public class SIGATTTableModel extends AbstractTableModel
{
	protected static int ROWS = 4;
	protected static int COLUMNS = 1;
	protected SIGATableObj[][] values=null;
	protected String urlIcon=null;

  	public SIGATTTableModel(String urlServlet,String _urlIcon,JApplet _applet,String icons)
  	{
    	super();
    	String name="";
    	urlIcon=icons;
    	values = new SIGATableObj[ROWS][COLUMNS];
		values[0][0]=new SIGATableObj(urlIcon,"accessFull.gif");
		values[0][0].setActionMove(SIGAPTConstants.MODIFYACCESS);
		values[0][0].setID(SIGAPTConstants.ACCESS_FULL);
		values[0][0].setUrlServlet(urlServlet);
		values[0][0].setApplet(_applet);
		name=(String)((SIGAAppletBase)_applet).literales.get(SIGAPTConstants.ACCESS_FULL);
    	
    	if (name!=null)
    	{
      		values[0][0].put("value",name);
      	}
      	
    	else
    	{
      		values[0][0].put("value","");
      	}

		values[1][0]=new SIGATableObj(urlIcon,"accessRead.gif");
		values[1][0].setActionMove(SIGAPTConstants.MODIFYACCESS);
		values[1][0].setID(SIGAPTConstants.ACCESS_READ);
		values[1][0].setUrlServlet(urlServlet);
		values[1][0].setApplet(_applet);
		name=(String)((SIGAAppletBase)_applet).literales.get(SIGAPTConstants.ACCESS_READ);
    
    	if (name!=null)
    	{
      		values[1][0].put("value",name);
      	}
      	
    	else
    	{
      		values[1][0].put("value","");
      	}

		values[2][0]=new SIGATableObj(urlIcon,"accessDeny.gif");
		values[2][0].setActionMove(SIGAPTConstants.MODIFYACCESS);
		values[2][0].setID(SIGAPTConstants.ACCESS_DENY);
		values[2][0].setUrlServlet(urlServlet);
		values[2][0].setApplet(_applet);
		name=(String)((SIGAAppletBase)_applet).literales.get(SIGAPTConstants.ACCESS_DENY);
    
    	if (name!=null)
    	{
      		values[2][0].put("value",name);
      	}
      	
    	else
    	{
      		values[2][0].put("value","");
      	}

		values[3][0]=new SIGATableObj(urlIcon,"accessNone.gif");
		values[3][0].setActionMove(SIGAPTConstants.MODIFYACCESS);
		values[3][0].setID(SIGAPTConstants.ACCESS_NONE);
		values[3][0].setUrlServlet(urlServlet);
		values[3][0].setApplet(_applet);
		name=(String)((SIGAAppletBase)_applet).literales.get(SIGAPTConstants.ACCESS_NONE);
    
    	if (name!=null)
    	{
      		values[3][0].put("value",name);
      	}
      	
    	else
    	{
      		values[3][0].put("value","");
      	}

  	}
  
  	public int getColumnCount()
  	{
  		return COLUMNS;
  	}
  	
  	public int getRowCount()
  	{
  		return ROWS;
  	}
  	
  	public Object getValueAt(int row, int col)
  	{
    	return values[row][col];
  	}
}