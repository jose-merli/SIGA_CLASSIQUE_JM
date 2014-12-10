package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @since 24/1/2005
 */



public class BusquedaDesignasForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8029335205674765444L;
	//Metodos set de los campos del formulario
	public void setOrden (String valor){		this.datos.put("ORDEN", valor);	}
	public void setColegiado (String valor){	this.datos.put("COLEGIADO", valor);	}
 	public void setNombreColegiadoPestanha (String valor)			{ this.datos.put("NOMBRECOLEGIADOPESTA�A",valor); 	 					}
 	public void setNumeroColegiadoPestanha (String valor)			{ this.datos.put("NUMEROCOLEGIADOPESTA�A",valor); 	 					}
 	public void setAnio (String valor)			{ this.datos.put("ANIO",valor); 	 					}

	
	//	Metodos get de los campos del formulario
	public String getOrden (){ 		return (String)this.datos.get("ORDEN");	}	
	public String getColegiado (){		return (String)this.datos.get("COLEGIADO");	}	
 	public String getNombreColegiadoPestanha ()		{ return ((String)this.datos.get("NOMBRECOLEGIADOPESTA�A"));			} 		
 	public String getNumeroColegiadoPestanha ()		{ return ((String)this.datos.get("NUMEROCOLEGIADOPESTA�A")); 			}
	public String getAnio (){ 		return (String)this.datos.get("ANIO");	}	
	
}