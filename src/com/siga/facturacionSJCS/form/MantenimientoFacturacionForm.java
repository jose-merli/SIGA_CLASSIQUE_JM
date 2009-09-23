//VERSIONES
//raul.ggonzalez 07-03-2005 Creacion
//

package com.siga.facturacionSJCS.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso BUSCAR FACTURACION
* @author AtosOrigin 07-03-2005
*/
public class MantenimientoFacturacionForm extends MasterForm {
	
	/**
	 * @return Returns the desdeRegularizacion.
	 */
	public String getDesdeRegularizacion() {
		return UtilidadesHash.getString(this.datos, "DESDE_REGULARIZACION");
	}
	/**
	 * @param desdeRegularizacion The desdeRegularizacion to set.
	 */
	public void setDesdeRegularizacion(String desdeRegularizacion) {
		UtilidadesHash.set(this.datos, "DESDE_REGULARIZACION", desdeRegularizacion);
	}
	/**
	 * @return Returns the idFacturacion.
	 */
	public Integer getIdFacturacion() {
		return UtilidadesHash.getInteger(this.datos, "IDFACTURACION");
	}
	/**
	 * @param idFacturacion The idFacturacion to set.
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		UtilidadesHash.set(this.datos, "IDFACTURACION", idFacturacion);
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return UtilidadesHash.getInteger(this.datos, "IDINSTITUCION");
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		UtilidadesHash.set(this.datos, "IDINSTITUCION", idInstitucion);
	}
	
	
	// Metodos Set (Formulario (*.jsp))

	public void setNombreInstitucion (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"nombreInstitucion", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setEstado(String dato) { 
		try {
			UtilidadesHash.set(this.datos,"estado", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setFechaIni (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"fechaIni", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setFechaFin (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"fechaFin", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setNombre (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"nombre", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setHitos (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"hitos", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}
	

	// Metodos Get 1 por campo Formulario (*.jsp)
	
	public String getNombreInstitucion	() 	{ 
		return UtilidadesHash.getString(this.datos, "nombreInstitucion");		
	}

	public String getEstado	() 	{ 
		return UtilidadesHash.getString(this.datos, "estado");		
	}

	public String getFechaIni () 	{ 
		return UtilidadesHash.getString(this.datos, "fechaIni");		
	}

	public String getFechaFin	() 	{ 
		return UtilidadesHash.getString(this.datos, "fechaFin");		
	}

	public String getNombre	() 	{ 
		return UtilidadesHash.getString(this.datos, "nombre");		
	}

	public String getHitos	() 	{ 
		return UtilidadesHash.getString(this.datos, "hitos");		
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