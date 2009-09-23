/*
 * VERSIONES:
 * 
 * miguel.villegas - 28-12-2004 - Creacion
 *	
 */

/**
 * Recoge y establece los campos del formulario de mantenimiento de los datos colegiales generales <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.siga.general.MasterForm;
import com.siga.beans.*;
	

public class DatosColegialesForm extends MasterForm{
	private static final long serialVersionUID = 7503641391569060315L;

	private String idPersona=""; 		// Identificador persona
	private String idInstitucion="";	// Identificador institucion
	private String motivo="";			// Motivo (para el historico)
	private String urlDocumentacionDS="";
	
	// Metodos set
	
	public void setIdPersona(String id){
		this.idPersona=id;
	}
	
	public void setIdInstitucion(String id){
		this.idInstitucion=id;
	}		
	
	public void setMotivo(String mot){
		this.motivo=mot;
	}	
	
	// Formulario Consulta Datos Colegiales	
	
	public void setNumColegiado(String num){
		datos.put(CenColegiadoBean.C_NCOLEGIADO,num);
	}
	
	public void setFechaPresentacion(String fechaP){
		datos.put(CenColegiadoBean.C_FECHAPRESENTACION,fechaP);
	}	
	
	public void setFechaIncorporacion(String fechaP){
		datos.put(CenColegiadoBean.C_FECHAINCORPORACION,fechaP);
	}	
	
	public void setFechaJura(String fechaP){
		datos.put(CenColegiadoBean.C_FECHAJURA,fechaP);
	}	
	
	public void setComunitario(String com){
		datos.put(CenColegiadoBean.C_COMUNITARIO,com);
	}
	
	public void setIndTitulacion(String com){
		datos.put(CenColegiadoBean.C_INDTITULACION,com);
	}
	
	public void setFechaTitulacion(String fechaP){
		datos.put(CenColegiadoBean.C_FECHATITULACION,fechaP);
	}	
	
	public void setOtrosColegios(String col){
		datos.put(CenColegiadoBean.C_OTROSCOLEGIOS,col);
	}	
	
	public void setFechaDeontologia(String fechaP){
		datos.put(CenColegiadoBean.C_FECHADEONTOLOGIA,fechaP);
	}	
	
	public void setJubilacionCuota(String cuo){
		datos.put(CenColegiadoBean.C_JUBILACIONCUOTA,cuo);
	}	
	
	public void setSituacionResidente(String sit){
		datos.put(CenColegiadoBean.C_SITUACIONRESIDENTE,sit);
	}	
	
	public void setSituacionEjercicio(String sit){
		datos.put(CenColegiadoBean.C_SITUACIONEJERCICIO,sit);
	}	

	public void setSituacionEmpresa(String sit){
		datos.put(CenColegiadoBean.C_SITUACIONEMPRESA,sit);
	}	
	
	public void setCmbTipoSeguro(String sit){
		datos.put(CenColegiadoBean.C_IDTIPOSSEGURO,sit);
	}	
	public void setCuentaContableSJCS(String cuenta){
		datos.put(CenColegiadoBean.C_CUENTACONTABLESJCS,cuenta);
	}
	// Formulario Consulta Datos Estado	
			
	public void setObservaciones(String obs){
		datos.put(CenDatosColegialesEstadoBean.C_OBSERVACIONES,obs);
	}	
	
	public void setFechaEstado(String fecha){
		datos.put(CenDatosColegialesEstadoBean.C_FECHAESTADO,fecha);
	}	
	
	public void setCmbEstadoColegial(String id){
		datos.put(CenDatosColegialesEstadoBean.C_IDESTADO,id);
	}	
		
	
	// Metodos get	
	
	public String getIdPersona(){
		return this.idPersona;
	}
	
	public String getIdInstitucion(){
		return this.idInstitucion;
	}
	
	public String getMotivo(){
		return this.motivo;
	}		
	
	
	// Formulario Consulta Datos Colegiales
	
	public String getNumColegiado(){
		return (String)datos.get(CenColegiadoBean.C_NCOLEGIADO);
	}
	
	public String getFechaPresentacion(){
		return (String)datos.get(CenColegiadoBean.C_FECHAPRESENTACION);
	}	

	public String getFechaIncorporacion(){
		return (String)datos.get(CenColegiadoBean.C_FECHAINCORPORACION);
	}	
	
	public String getFechaJura(){
		return (String)datos.get(CenColegiadoBean.C_FECHAJURA);
	}	

	public String getComunitario(){
		return (String)datos.get(CenColegiadoBean.C_COMUNITARIO);
	}	
	
	public String getIndTitulacion(){
		return (String)datos.get(CenColegiadoBean.C_INDTITULACION);
	}	
	
	public String getFechaTitulacion(){
		return (String)datos.get(CenColegiadoBean.C_FECHATITULACION);
	}
	
	public String getOtrosColegios(){
		return (String)datos.get(CenColegiadoBean.C_OTROSCOLEGIOS);
	}	
	
	public String getFechaDeontologia(){
		return (String)datos.get(CenColegiadoBean.C_FECHADEONTOLOGIA);
	}	
	
	public String getJubilacionCuota(){
		return (String)datos.get(CenColegiadoBean.C_JUBILACIONCUOTA);
	}	
	
	public String getSituacionResidente(){
		return (String)datos.get(CenColegiadoBean.C_SITUACIONRESIDENTE);
	}
	
	public String getSituacionEjercicio(){
		return (String)datos.get(CenColegiadoBean.C_SITUACIONEJERCICIO);
	}	
	
	public String getSituacionEmpresa(){
		return (String)datos.get(CenColegiadoBean.C_SITUACIONEMPRESA);
	}	

	public String getCmbTipoSeguro(){
		return (String)datos.get(CenColegiadoBean.C_IDTIPOSSEGURO);
	}
	public String getCuentaBancariaSJCS(){
		return (String)datos.get(CenColegiadoBean.C_CUENTACONTABLESJCS);
	}
	
	// Formulario Consulta Datos Estado	
	
	public String getObservaciones(){
		return (String)datos.get(CenDatosColegialesEstadoBean.C_OBSERVACIONES);
	}	

	public String getFechaEstado(){
		return (String)datos.get(CenDatosColegialesEstadoBean.C_FECHAESTADO);
	}	

	public String getCmbEstadoColegial(){
		return (String)datos.get(CenDatosColegialesEstadoBean.C_IDESTADO);
	}	

	public String getUrlDocumentacionDS(){
		return urlDocumentacionDS;
	}	
	public void setUrlDocumentacionDS(String urlDocumentacionDS){
		this.urlDocumentacionDS=urlDocumentacionDS;
	}
}
