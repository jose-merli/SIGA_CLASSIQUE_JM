/*
 * VERSIONES:
 * 		miguel.villegas - 3-2-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento de los servicios <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.productos.form;

import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;


public class MantenimientoServiciosForm extends MasterForm{
	

	private String servicio="";
	private String tipoServicio="";	
	private String categoriaServicio="";
	private String pagoServicio="";
	private String idServInst="";
	private String refresco="";
	private String campo="";
	private String estado="";
	private String tipoCampo = "";
	private String criterios = "";
	private String fechaBaja = "";
	private String[] formaPagoInternet;
	private String[] formaPagoSecretaria;
	private String radioAlta="";
	private String solBaja="0";
	private String fecha = "";
	private String noPondera = "";

	
	// Metodos set
	
	// Formulario Busqueda servicios	
	
	
	
	public void setBusquedaServicio(String v){
		this.servicio=v;
	}
	
	public void setBusquedaCategoria(String v){
		this.categoriaServicio=v;		
	}
	
	public void setBusquedaTipo(String v){
		this.tipoServicio=v;		
	}
	
	public void setBusquedaPago(String v){
		this.pagoServicio=v;		
	}
	
	public void setBusquedaEstado(String v){
		this.estado=v;		
	}
	
	public void setRefresco(String v){
		this.refresco=v;		
	}
	
	public void setNoPondera(String v){
		this.noPondera=v;		
	}
	
	/**
	 * @return Returns the campo.
	 */
	public String getCampo() {
		return this.campo;
	}
	/**
	 * @return Returns the tipoCampo.
	 */
	public String getTipoCampo() {
		return this.tipoCampo;
	}
	/**
	 * @param campo The campo to set.
	 */
	public void setCampo(String campo) {
		try{
			this.campo = campo.substring(0,campo.indexOf(","));
			this.tipoCampo = campo.substring(campo.indexOf(",")+1,campo.lastIndexOf(","));
		}catch(Exception e){
			this.campo="";
			this.tipoCampo="";
		}
	}
	// Formulario Mantenimiento servicios	
	
	public void setIdInstitucion(String idInst){
		datos.put(PysServiciosInstitucionBean.C_IDINSTITUCION,idInst);
	}

	public void setIdTipoServicios(String idTipoServ){
		datos.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,idTipoServ);
	}

	public void setIdServicio(String idServ){
		datos.put(PysServiciosInstitucionBean.C_IDSERVICIO,idServ);
	}

	public void setIdServiciosInstitucion(String idServInst){
		datos.put(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION,idServInst);
	}

	public void setNombre(String v){
		datos.put(PysServiciosInstitucionBean.C_DESCRIPCION,v);
	}

	public void setCuentaContable(String v){
		datos.put(PysServiciosInstitucionBean.C_CUENTACONTABLE,v);
	}

	public void setIva(float v){		
		datos.put(PysServiciosInstitucionBean.C_PORCENTAJEIVA,Float.toString(v));
	}

	public void setAltaInternet(String v){
		datos.put(PysServiciosInstitucionBean.C_SOLICITARALTA,v);
	}

	public void setBajaInternet(String v){
		datos.put(PysServiciosInstitucionBean.C_SOLICITARBAJA,v);
	}

	public void setAutomatico(String v){
		datos.put(PysServiciosInstitucionBean.C_AUTOMATICO,v);
	}
	
	public void setComprobarCondicion (Boolean v) {
		UtilidadesHash.set(datos, "comprobarCondicion", v);
	}
	
	public void setComprobarCondicionBaja (Boolean v) {
		UtilidadesHash.set(datos, "comprobarCondicionBaja", v);
	}
	
	public void setCargo(String v){
		datos.put(PysServiciosInstitucionBean.C_MOMENTOCARGO,v);
	}
	
	public void setCambioSituacion(String v){
		datos.put(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO,v);
	}	
	
	public void setFechaMod(String fechaMod){ 
		this.datos.put(PysServiciosInstitucionBean.C_FECHAMODIFICACION,fechaMod);
	}
	
	public void setUsuMod(int usuMod){ 
		this.datos.put(PysServiciosInstitucionBean.C_USUMODIFICACION,Integer.toString(usuMod));	
	}	
		
	public void setServicio(String serv){
		datos.put(PysServiciosInstitucionBean.C_IDSERVICIO,serv);
	}
	
	public void setTipoServicio(String tipo){
		datos.put(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS,tipo);		
	}	
	
	public void setFormaPagoSecretaria(String[] v){
		this.formaPagoSecretaria=v;
	}	
	
	public void setFormaPagoInternet(String[] v){
		this.formaPagoInternet=v;
	}	

	public void setIdServInst(String v){
		this.idServInst=v;		
	}
	
	// Formulario criterior cliente	
	
	public void setPrecio(String v){
		datos.put(PysPreciosServiciosBean.C_VALOR,v);
	}
	
	public void setPeriodicidad(String v){
		datos.put(PysPreciosServiciosBean.C_IDPERIODICIDAD,v);		
	}	
	
	public void setFechaBaja(String v){
		datos.put(PysServiciosInstitucionBean.C_FECHABAJA,v);		
	}	
	
	public void setDescripcion(String v){
		datos.put(PysPreciosServiciosBean.C_DESCRIPCION,v);		
	}		
	
	// Metodos get	
	
	// Formulario Busqueda Servicios
	
	public String getBusquedaServicio(){
		return this.servicio;
	}
	
	public String getBusquedaCategoria(){
		return this.categoriaServicio;		
	}
	
	public String getBusquedaTipo(){
		return this.tipoServicio;		
	}
	
	public String getBusquedaPago(){
		return this.pagoServicio;		
	}		
	
	public String getBusquedaEstado(){
		return this.estado;		
	}		

	public String getRefresco(){
		return this.refresco;		
	}
	
	public String getNoPondera(){
		return this.noPondera;		
	}
	
	// Formulario Mantenimiento Servicios	
	
	public String getIdInstitucion(){
		return (String)datos.get(PysServiciosInstitucionBean.C_IDINSTITUCION);
	}

	public String getIdTipoServicios(){
		return (String)datos.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);
	}

	public String getIdServicio(){
		return (String)datos.get(PysServiciosInstitucionBean.C_IDSERVICIO);
	}

	public String getIdServiciosInstitucion(){
		return (String)datos.get(PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION);
	}

	public String getNombre(){
		return (String)datos.get(PysServiciosInstitucionBean.C_DESCRIPCION);				
	}

	public String getCuentaContable(){
		return (String)datos.get(PysServiciosInstitucionBean.C_CUENTACONTABLE);
	}

	public float getIva(){
		return Float.parseFloat((String)this.datos.get(PysServiciosInstitucionBean.C_PORCENTAJEIVA));
	}

	public String getAltaInternet(){
		return (String)datos.get(PysServiciosInstitucionBean.C_SOLICITARALTA);
	}

	public String getBajaInternet(){
		return (String)datos.get(PysServiciosInstitucionBean.C_SOLICITARBAJA);
	}
	
	public String getAutomatico(){
		return (String)datos.get(PysServiciosInstitucionBean.C_AUTOMATICO);
	}
	
	public Boolean getComprobarCondicion(){
		return UtilidadesHash.getBoolean(datos, "comprobarCondicion");
	}
	
	public Boolean getComprobarCondicionBaja(){
		return UtilidadesHash.getBoolean(datos, "comprobarCondicionBaja");
	}
	
	
	public String getCargo(){
		return (String)datos.get(PysServiciosInstitucionBean.C_MOMENTOCARGO);
	}
	
	public String getCambioSituacion(){
		return (String)datos.get(PysServiciosInstitucionBean.C_INICIOFINALPONDERADO);
	}
	
	public String getFechaMod(String fechaMod){
		return (String)this.datos.get(PysServiciosInstitucionBean.C_FECHAMODIFICACION);
	}
	
	public int getUsuMod(){
		return Integer.parseInt((String)this.datos.get(PysServiciosInstitucionBean.C_USUMODIFICACION));	
	}	

	public String getServicio(){
		return (String)datos.get(PysServiciosInstitucionBean.C_IDSERVICIO);
	}
	
	public String getTipoServicio(){
		return (String)datos.get(PysServiciosInstitucionBean.C_IDTIPOSERVICIOS);		
	}	
	
	public String[] getFormaPagoInternet(){
		return formaPagoInternet;
	}
	
	public String[] getFormaPagoSecretaria(){
		return formaPagoSecretaria;
	}	

	public String getIdServInst(){
		return idServInst;		
	}
	
	// Formulario criterior cliente	
	
	public String getPrecio(){
		return (String)datos.get(PysPreciosServiciosBean.C_VALOR);
	}
	
	public String getPeriodicidad(){
		return (String)datos.get(PysPreciosServiciosBean.C_IDPERIODICIDAD);		
	}	
	
	public String getFechaBaja(){
		return (String)datos.get(PysServiciosInstitucionBean.C_FECHABAJA);		
	}	

	public String getDescripcion(){
		return (String)datos.get(PysPreciosServiciosBean.C_DESCRIPCION);		
	}		
	
	/**
	 * @return Returns the criterios.
	 */
	public String getCriterios() {
		return criterios;
	}
	/**
	 * @param criterios The criterios to set.
	 */
	public void setCriterios(String criterios) {
		this.criterios = criterios;
	}
	 public String getRadioAlta(){
		  return radioAlta;
	 }
	 public void setRadioAlta(String valor){
		 radioAlta=valor;
	 }
	 public String  getChkSolicitudBaja(){
		  return solBaja;
	 }
	 public void setChkSolicitudBaja(String valor){
	 	solBaja=valor;
	 }
	 public String getFechaAlta(){
		  return fecha;
	 }
	 public void setFechaAlta(String valor){
	 	fecha=valor;
	 }
	 
}