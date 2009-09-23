package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.util.*;

public class SIGAGrupoObj extends SIGABaseNode implements Serializable
{
  	private static final String CLASSNAME = SIGAGrupoObj.class.getName();
  	private String idPerfil = "";
  	private String idInstitucion = "";
  	
  	public String getIdPerfil()
  	{
  		return idPerfil;
  	}
  	
  	public void setIdPerfil(String idPerfil)
  	{
  	    this.idPerfil=idPerfil;
  	}
  	
  	public String getIdInstitucion()
  	{
  		return idInstitucion;
  	}
  	
  	public void setIdInstitucion(String idInstitucion)
  	{
  	    this.idInstitucion=idInstitucion;
  	}
  	
  	public SIGAGrupoObj() {  }
  
  	public Vector dndMovement(SIGABaseNode destino) throws Exception
  	{
    	Vector vec=new Vector();
    	vec.add("OK");
    	
    	return vec;
  	}

  	public Color getForeground()
  	{
	    if (modified)
	    {
	    	return new Color(254, 184, 6);
	    }
	    
	    else if (removed)
	    {
	    	return new Color(248, 52, 52);
	    }

	    else if (added)
	    {
	    	return new Color(56, 172, 20);
	    }
	    
	    else
	    {
	    	return new Color(52, 36, 79);
	    }
  	}
}