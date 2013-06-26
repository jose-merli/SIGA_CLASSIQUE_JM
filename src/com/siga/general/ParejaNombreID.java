package com.siga.general;

import java.io.Serializable;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder.PropertyReader;

public class ParejaNombreID implements Serializable, PropertyReader {

	// Atributos
	private String nombre, idNombre;
	
	// Constructor
	public ParejaNombreID () {
		this.idNombre = "";
		this.nombre = "";
	}

	public ParejaNombreID (String id, String nombre) {
		this.idNombre = id;
		this.nombre = nombre;
	}

	// Metodos Get
	public String getNombre		() 		{ return this.nombre;	}
	public String getIdNombre	() 		{ return this.idNombre;	}
	
	// Metodos Set
	public void setNombre 	(String s) 	{ this.nombre = s;		}
	public void setIdNombre (String i) 	{ this.idNombre = i;	}
	public void setIdNombre (Integer i) { this.idNombre = i.toString();	}

	@Override
	public String getName() {
		return getNombre();
	}

	@Override
	public String getValue() {
		return getIdNombre();
	}

	@Override
	public boolean isCData() {
		return false;
	}
	
	public static String getHtmlOption(ParejaNombreID option, boolean selected){
		return "<option "+(selected?"selected":"")+" value='"+option.getValue()+"'>"+option.getName()+"</option>";
	}
}
