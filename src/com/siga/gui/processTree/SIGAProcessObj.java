package com.siga.gui.processTree;

import java.io.*;
import java.awt.*;
import java.util.*;

public class SIGAProcessObj extends SIGABaseNode implements Serializable
{
  	private static final String CLASSNAME = SIGAProcessObj.class.getName();
  	
  	public SIGAProcessObj() {  }
  
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
	    	return new Color(254,184,6);
	    }
	    
	    else
	    {
	    	return new Color(52,36,79);
	    }
  	}
}