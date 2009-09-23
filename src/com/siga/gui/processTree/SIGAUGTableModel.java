package com.siga.gui.processTree;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class SIGAUGTableModel extends AbstractTableModel
{
	//protected static int ROWS = 4;
    protected int ROWS = 1;
	protected static int COLUMNS = 1;
	protected SIGAUGTableObj[][] values=null;
	protected String urlIcon=null;

  	public SIGAUGTableModel(String urlServlet, String _urlIcon, JApplet _applet, String icons)
  	{
  	    super();
  	    
  	    try
  	    {
	    	String name="";
	    	urlIcon=icons;
	    	
	    	Vector vGrupos = ((SIGAAppletBase)_applet).vGrupos;
	    	
	    	ROWS = vGrupos.size();

	    	values = new SIGAUGTableObj[ROWS][COLUMNS];
	    	
	    	for (int i=0; i<ROWS; i++)
	    	{
	    	    Hashtable htAux = (Hashtable)vGrupos.elementAt(i);
	    	    
				values[i][0]=new SIGAUGTableObj(urlIcon,"lic.gif");
				values[i][0].setActionMove(SIGAPTConstants.SAVE_USUARIOS_GRUPO);
				//values[i][0].setID((String)htAux.get(SIGAPTConstants.PERFIL_IDPERFIL));
				values[i][0].put(SIGAPTConstants.PERFIL_IDPERFIL, (String)htAux.get(SIGAPTConstants.PERFIL_IDPERFIL));
				values[i][0].put(SIGAPTConstants.PERFIL_IDINSTITUCION, (String)htAux.get(SIGAPTConstants.PERFIL_IDINSTITUCION));
				values[i][0].setUrlServlet(urlServlet);
				values[i][0].setApplet(_applet);
				name=(String)htAux.get(SIGAPTConstants.PERFIL_DESCRIPCION);
				values[i][0].put("value",name);
				
				if (i==0)
				{
					values[i][0].removed=true;
				}
				
				else
				{
					values[i][0].added=true;
				}
	    	}
  	    }
  	    
  	    catch(Exception e)
  	    {
  	        e.printStackTrace();
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