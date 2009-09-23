package com.siga.productos.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;

public class CargaProductosForm extends MasterForm {

	protected FormFile fichero;
	
 	public void setFichero(FormFile valor){
 		this.fichero = valor;
 	}
	
 	public FormFile getFichero	() 	{ 
 		return this.fichero;		
 	}	
}
