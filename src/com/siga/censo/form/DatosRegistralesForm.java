/*
 * Created on marzo 07, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
 * @author fernando.gomez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DatosRegistralesForm extends MasterForm{

//	metodos set de los campos del formulario
	public void setIdPersona (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDPERSONA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	public void setIdPersonaNotario (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenNoColegiadoBean.C_IDPERSONANOTARIO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	public void setFechaConstitucion(String fecha_constitucion) {
		UtilidadesHash.set(this.datos, CenNoColegiadoBean.C_FECHA_CONSTITUCION, fecha_constitucion);
	}
	
	public void setfecha_fin(String fechafin) {
		UtilidadesHash.set(this.datos, CenNoColegiadoBean.C_FECHAFIN, fechafin);
	}
	public void setResena(String resenia) {
		UtilidadesHash.set(this.datos, CenNoColegiadoBean.C_RESENA, resenia);
	}
	public void setIdInstitucion(String idInstitucion) {
		UtilidadesHash.set(this.datos, CenNoColegiadoBean.C_IDINSTITUCION, idInstitucion);
	}
	public void setObjetoSocial(String objetoSocial) {
		UtilidadesHash.set(this.datos, CenNoColegiadoBean.C_OBJETOSOCIAL, objetoSocial);
	}
	public void setNombre (String dato) { 
		UtilidadesHash.set(this.datos,CenPersonaBean.C_NOMBRE, dato);
 	}

 	public void setApellido1 (String dato) { 
		UtilidadesHash.set(this.datos,CenPersonaBean.C_APELLIDOS1, dato);
	}

 	public void setApellido2 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_APELLIDOS2, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

	
	public void setNumIdentificacion (String dato) { 
 		try {      
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_NIFCIF, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

	public void setTipoIdentificacion (String dato) { 
 		try {      
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDTIPOIDENTIFICACION, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

//	metodos get de los campos del formulario
	
	public String getAccion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "accion");		
 	}
	public String getIdInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenNoColegiadoBean.C_IDINSTITUCION);		
 	}

 	public String getIdPersona	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDPERSONA);		
 	}
 	public String getIdPersonaNotario	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenNoColegiadoBean.C_IDPERSONANOTARIO);		
 	}
	public String getFechaConstitucion() {
		return UtilidadesHash.getString(this.datos, CenNoColegiadoBean.C_FECHA_CONSTITUCION);
	}
	public String getfecha_fin() {
		return UtilidadesHash.getString(this.datos, CenNoColegiadoBean.C_FECHAFIN);
	}
	public String getResena() {
		return UtilidadesHash.getString(this.datos, CenNoColegiadoBean.C_RESENA);
	}
	public String getObjetoSocial() {
		return UtilidadesHash.getString(this.datos, CenNoColegiadoBean.C_OBJETOSOCIAL);
	}
	public String getNombre () 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NOMBRE);		
 	}

 	public String getApellido1	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_APELLIDOS1);		
 	}

 	public String getApellido2	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_APELLIDOS2);		
 	}

 	public String getCliente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "cliente");		
 	}

 	public String getTipoIdentificacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDTIPOIDENTIFICACION);		
 	}
 	                  
 	public String getNumIdentificacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_NIFCIF);		
 	}
 	public void setIdInstitucionProf (String dato) { 
		UtilidadesHash.set(this.datos,"IDINSTITUCIONPROF", dato);
	}	
	public String getIdInstitucionProf	() 	{ 
		return UtilidadesHash.getString(this.datos, "IDINSTITUCIONPROF");		
	}
	public void setActividadProfesional (String actividadProfesional) { 
		UtilidadesHash.set(this.datos,"ACTIVIDADPROSESIONAL", actividadProfesional);
	}	
	public String getActividadProfesional	() 	{ 
		return UtilidadesHash.getString(this.datos, "ACTIVIDADPROSESIONAL");		
	}
	public void setIdprofesion (String dato) { 
		UtilidadesHash.set(this.datos,"IDPROFESION", dato);
	}	
	public String getIdProfesion() 	{ 
		return UtilidadesHash.getString(this.datos, "IDPROFESION");		
	}
	public void setNoPoliza (String dato) { 
		UtilidadesHash.set(this.datos,"NOPOLIZA", dato);
	}	
	public String getNoPoliza() 	{ 
		return UtilidadesHash.getString(this.datos, "NOPOLIZA");		
	}
	public void setCompaniaSeg (String dato) { 
		UtilidadesHash.set(this.datos,"COMPANIASEG", dato);
	}	
	public String getCompaniaSeg() 	{ 
		return UtilidadesHash.getString(this.datos, "COMPANIASEG");		
	}
	
 	//otras funciones
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