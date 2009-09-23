/**
 * VERSIONES:
 * 
 * jose.barrientos - 26-02-2008 - Inicio
 */
package com.siga.facturacionSJCS.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfiguracionAbonosForm extends MasterForm{
	
	public void setIdInstitucion (String id) {
		UtilidadesHash.set(datos, "IDINSTITUCION", id);
 	}
	
	public void setConcepto(String concepto){
		UtilidadesHash.set(this.datos, "CONCEPTO", concepto);
	}
	
	public void setBancosCodigo(String bancosCodigo){
		UtilidadesHash.set(this.datos, "BANCOS_CODIGO", bancosCodigo);
	}
	
	public void setIdPagosJG(String idPagosJG) 	{ 
		UtilidadesHash.set(this.datos, "IDPAGOSJG", idPagosJG);
	}
	
	public void setCuenta(String cuenta) 	{ 
		UtilidadesHash.set(this.datos, "cuenta", cuenta);
	}
	
	public String getConcepto() 	{ 
		return UtilidadesHash.getString(this.datos, "CONCEPTO");		
	}
	
	public String getBancosCodigo() 	{ 
		return UtilidadesHash.getString(this.datos, "BANCOS_CODIGO");		
	}
	
	public String getIdPagosJG() 	{ 
		return UtilidadesHash.getString(this.datos, "IDPAGOSJG");		
	}
	
	public String getIdInstitucion	() {
 		return UtilidadesHash.getString(datos, "IDINSTITUCION");
 	}
	
	public String getCuenta() 	{ 
		return UtilidadesHash.getString(this.datos, "cuenta");		
	}
}
