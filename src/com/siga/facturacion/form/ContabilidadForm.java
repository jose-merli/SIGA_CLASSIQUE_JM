/*
 * VERSIONES:
 * carlos.vidal - 10-03-2005 - Creación
 */

/**
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */
package com.siga.facturacion.form;

import com.siga.general.MasterForm;
public class ContabilidadForm extends MasterForm{
	private String fechaDesde            ="fechaDesde";
	private String fechaHasta            ="fechaHasta";
	private String esConfirmacion        ="esConfirmacion";
	private String fichero        		 ="fichero";
	/*
	 * Metodos SET y GET
	 */
	public void setFechaDesde(String	valor)	{ this.datos.put(this.fechaDesde, valor);}
	public void setFechaHasta(String	valor)	{ this.datos.put(this.fechaHasta, valor);}
	public void setEsConfirmacion(String	valor)	{ this.datos.put(this.esConfirmacion, valor);}
	public void setFichero(String	valor)	{ this.datos.put(this.fichero, valor);}

	public String getFechaDesde()	 {return (String)this.datos.get(this.fechaDesde);}
	public String getFechaHasta()	 {return (String)this.datos.get(this.fechaHasta);}
	public String getEsConfirmacion(){return (String)this.datos.get(this.esConfirmacion);}
	public String getFichero(){return (String)this.datos.get(this.fichero);}
}
