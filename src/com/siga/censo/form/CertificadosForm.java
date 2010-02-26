/*
 * Created on Jan 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;

import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author nuria.rgonzalez
 * @author josebd 17/02/2010 
 * Esto estaba vacio y lo he rellenado para usar con las jsps asignarPlantillaCertificadoPredefinido y 2 en vez del DummyForm
 */
public class CertificadosForm  extends MasterForm{
	
	// Metodos Set (Formulario (*.jsp))
	public void setIdProductoCertificado (String id) {
		UtilidadesHash.set(datos, "IDPRODUCTOCERTIFICADO", id);
 	}
	
	public void setIdInstitucionPresentador (Long id) { 
		UtilidadesHash.set(datos, "IDINSTITUCIONPRESENTADOR", id);
  	}
	
	public void setFechaSolicitud (String fecha) {
 		UtilidadesHash.set(datos, "FECHASOLICITUD", fecha);
 	}
 	
 	public void setMetodoSolicitud (String metodo) { 
 		UtilidadesHash.set(datos, "METODOSOLICITUD", metodo);
 	}
 	
 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public String getIdProductoCertificado() {
 		return UtilidadesHash.getString(datos, "IDPRODUCTOCERTIFICADO");
 	}
 	
 	public String getIdInstitucionPresentador	() {
 		return UtilidadesHash.getString(datos, "IDINSTITUCIONPRESENTADOR");
 	}
 	
 	public String getFechaSolicitud	() {
 		return UtilidadesHash.getString(datos, "FECHASOLICITUD");	
 	}
 	
 	public String getMetodoSolicitud() 	{ 
 		return UtilidadesHash.getString(datos, "METODOSOLICITUD");
 	}

}
