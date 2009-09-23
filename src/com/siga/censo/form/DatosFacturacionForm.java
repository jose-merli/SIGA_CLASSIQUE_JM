//VERSIONES
//raul.ggonzalez 07-02-2005 Creacion
//

package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;
import com.siga.beans.*;


/**
* Clase action form del caso de uso DATOS FACTURACION
* @author AtosOrigin 07-02-2005
*/
public class DatosFacturacionForm extends MasterForm {
	
	private String incluirRegistrosConBajaLogica;


	// Metodos Set (Formulario (*.jsp))

	public void setIdPersona (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDPERSONA, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setFechaEfectiva (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,"FechaEfectiva", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setIdInstitucion (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenClienteBean.C_IDINSTITUCION, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setCuentaBancaria (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenSolModiFacturacionServicioBean.C_IDCUENTA, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setMotivo (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,CenSolModiFacturacionServicioBean.C_MOTIVO, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setPos (String dato) { //Producto o Servicio
		try {
			UtilidadesHash.set(this.datos,"pos", dato);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setAccion (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,"accion", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setIdFormaPago (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,PysServiciosSolicitadosBean.C_IDFORMAPAGO, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setIdCuenta (String dato) { 
		try {
			// lo meto en una clave auxiliar para luego tratarlo
			// puesto que va a ir a un bean o a otro en funcion de otro valor
			UtilidadesHash.set(this.datos,PysServiciosSolicitadosBean.C_IDCUENTA, dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	
	// BLOQUE PARA EL FORMULARIO DE DATOS GENERALES 

	public String getIdPersona	() 	{ 
		return UtilidadesHash.getString(this.datos, CenSolModiFacturacionServicioBean.C_IDPERSONA);		
	}

	public String getFechaEfectiva	() 	{ 
		return UtilidadesHash.getString(this.datos, "FechaEfectiva");		
	}

	public String getIdInstitucion	() 	{ 
		return UtilidadesHash.getString(this.datos, CenSolModiFacturacionServicioBean.C_IDINSTITUCION);		
	}

	public String getCuentaBancaria	() 	{ 
		return UtilidadesHash.getString(this.datos, CenSolModiFacturacionServicioBean.C_IDCUENTA);		
	}

	public String getMotivo	() 	{ 
		return UtilidadesHash.getString(this.datos, CenSolModiFacturacionServicioBean.C_MOTIVO);		
	}

	public String getPos	() 	{ //Producto o Servicio
		return UtilidadesHash.getString(this.datos, "pos");		
	}
	
	public String getAccion	() 	{ 
		return UtilidadesHash.getString(this.datos, "accion");		
	}

	public String getIdFormaPago	() 	{ 
		return UtilidadesHash.getString(this.datos, PysServiciosSolicitadosBean.C_IDFORMAPAGO);		
	}

	public String getIdCuenta	() 	{ 
		return UtilidadesHash.getString(this.datos, PysServiciosSolicitadosBean.C_IDCUENTA);		
	}
	
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}


	// OTRAS FUNCIONES 

	/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			// resetea el formulario
			super.reset(mapping, request);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}