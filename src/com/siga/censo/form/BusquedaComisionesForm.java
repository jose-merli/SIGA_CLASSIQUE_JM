

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento de los productos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import java.util.List;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenDatosCVBean;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.general.MasterForm;

public class BusquedaComisionesForm extends MasterForm{
	

	private String comision="";
	private String cargos="";	
	private String fechaCargo="";
	private String idInstitucion;
	private String idInstitucionCargo;	
	String idPersona;	
	String datosCargos;
	String numeroN;	
	String multiple;		

	List<CenDatosCVBean> comisiones;
	// Formulario Busqueda Comisiones	
	String idColegiado;
	String numeroColegiado;
	String nombreColegiado;
	String numeroColegiadoN;
	String msgError;
	String msgAviso;
	
	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgAviso() {
		return msgAviso;
	}

	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public String getApellidosColegiadoN() {
		return apellidosColegiadoN;
	}

	public void setApellidosColegiadoN(String apellidosColegiadoN) {
		this.apellidosColegiadoN = apellidosColegiadoN;
	}
	String apellidosColegiadoN;
	String nombreColegiadoN;
	String idPersonaN;	
	public String getNumeroColegiadoN() {
		return numeroColegiadoN;
	}

	public void setNumeroColegiadoN(String numeroColegiadoN) {
		this.numeroColegiadoN = numeroColegiadoN;
	}

	public String getNombreColegiadoN() {
		return nombreColegiadoN;
	}

	public void setNombreColegiadoN(String nombreColegiadoN) {
		this.nombreColegiadoN = nombreColegiadoN;
	}

	public String getIdPersonaN() {
		return idPersonaN;
	}

	public void setIdPersonaN(String idPersonaN) {
		this.idPersonaN = idPersonaN;
	}


	// Metodos get y set
	public String getNumeroN() {
		return numeroN;
	}

	public void setNumeroN(String numeroN) {
		this.numeroN = numeroN;
	}
	public List<CenDatosCVBean> getComisiones() {
		return comisiones;
	}

	public void setComisiones(List<CenDatosCVBean> comisiones) {
		this.comisiones = comisiones;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getDatosCargos() {
		return datosCargos;
	}

	public void setDatosCargos(String datosCargos) {
		this.datosCargos = datosCargos;
	}

	public String getIdInstitucionCargo() {
		return idInstitucionCargo;
	}

	public void setIdInstitucionCargo(String idInstitucionCargo) {
		this.idInstitucionCargo = idInstitucionCargo;
	}


	public String getNumeroColegiado() {
		return numeroColegiado;
	}

	public void setNumeroColegiado(String numeroColegiado) {
		this.numeroColegiado = numeroColegiado;
	}

	public String getNombreColegiado() {
		return nombreColegiado;
	}

	public void setNombreColegiado(String nombreColegiado) {
		this.nombreColegiado = nombreColegiado;
	}

	public String getIdColegiado() {
		return idColegiado;
	}

	public void setIdColegiado(String idColegiado) {
		this.idColegiado = idColegiado;
	}
	

	/**
	 * @return Returns the cargos.
	 */
	public String getCargos() {
		return cargos;
	}
	/**
	 * @param cargos The cargos to set.
	 */
	public void setCargos(String cargos) {
		this.cargos = cargos;
	}
	/**
	 * @return Returns the comision.
	 */
	public String getComision() {
		return comision;
	}
	/**
	 * @param comision The comision to set.
	 */
	public void setComision(String comision) {
		this.comision = comision;
	}
	/**
	 * @return Returns the fechaCargo.
	 */
	public String getFechaCargo() {
		return fechaCargo;
	}
	/**
	 * @param fechaCargo The fechaCargo to set.
	 */
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
	/**
	 * @param fechaCargo The Colegiado to set.
	 */
	public void setColegiado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Colegiado", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	/**
	 * @return Returns the Colegiado.
	 */
	public String getColegiado	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Colegiado");		
 	}
	
	 public void setChkBusqueda(String valor){
		 	UtilidadesHash.set(this.datos, "ChkBusqueda", valor);
		 }
	 
	 public String  getChkBusqueda(){
	 		return UtilidadesHash.getString(this.datos, "ChkBusqueda");
	 	}
	
	 public String getIdInstitucion() {
			return idInstitucion;
		}
		public void setIdInstitucion(String idInstitucion) {
			this.idInstitucion = idInstitucion;
		}
}