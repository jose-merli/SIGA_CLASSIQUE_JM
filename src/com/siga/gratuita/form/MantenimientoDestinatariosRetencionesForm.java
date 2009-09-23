//VERSIONES:
//jose.barrientos 29/01/2009 
//

package com.siga.gratuita.form;


import com.siga.Utilidades.*;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso MANTENIMIENTO RETENCIONES JUDICIALES
* @author jose.barrientos JBD 29/01/2009
*/
public class MantenimientoDestinatariosRetencionesForm extends MasterForm {
	
	// Metodos Set
	public void setIdInstitucion (String dato) {UtilidadesHash.set(this.datos,"IDINSTITUCION", dato);} 
	public void setIdDestinatario(String dato) {UtilidadesHash.set(this.datos,"IDDESTINATARIO", dato);}
	public void setNombre (String dato) {UtilidadesHash.set(this.datos,"NOMBRE", dato);}
	public void setNombreBuscado (String dato) {UtilidadesHash.set(this.datos,"NOMBREBUSCADO", dato);}
	public void setCuentaContable (String dato) {UtilidadesHash.set(this.datos,"CUENTACONTABLE", dato);}
	public void setOrden (String dato) {UtilidadesHash.set(this.datos,"ORDEN", dato);}
	public void setCodigoExt (String dato) {UtilidadesHash.set(this.datos,"CODIGOEXT", dato);}
	public void setNombreObjetoDestino(String dato) {UtilidadesHash.set(this.datos, "NOMBRE_OBJETO_DESTINOP" , dato);
	}

	// Metodos Get
	public String getIdInstitucion () 	{return UtilidadesHash.getString(this.datos, "IDINSTITUCION");}
	public String getIdDestinatario () {return UtilidadesHash.getString(this.datos, "IDDESTINATARIO");}
	public String getNombre () {return UtilidadesHash.getString(this.datos, "NOMBRE");}
	public String getNombreBuscado() {return UtilidadesHash.getString(this.datos, "NOMBREBUSCADO");}
	public String getCuentaContable () {return UtilidadesHash.getString(this.datos, "CUENTACONTABLE");}
	public String getOrden () {return UtilidadesHash.getString(this.datos, "ORDEN");}
	public String getCodigoExt () {return UtilidadesHash.getString(this.datos, "CODIGOEXT");}
	public String getNombreObjetoDestino() {return UtilidadesHash.getString(this.datos, "NOMBRE_OBJETO_DESTINOP");
	}
	
}