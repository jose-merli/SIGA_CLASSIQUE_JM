package com.siga.gratuita.form;

/**
 * @author jorgeta
 */
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.general.MasterForm;

public class AsistenciaForm extends MasterForm 
{
	  String idInstitucion;          
	  String anio;                   
	  String numero;                  
	  String fechaHora;               
	  String idTipoAsistencia;        
	  String idTurno;                 
	  String idGuardia;               
	           
	  String idPersonaColegiado;      
	  String observaciones;           
	  String incidencias;             
	  String fechaAnulacion;         
	  String motivosAnulacion;        
	  String delitosImputados;        
	  String contrarios;              
	  String datosDefensaJuridica;    
	  String fechaCierre;            
	  String idTipoAsistenciaColegio;
	  String designaAnio;            
	  String designaNumero;         
	  String idPersonaJG;            
	  String facturado;               
	  String pagado;                  
	  String idFacturacion;          
	  String designaTurno;          
	  String numeroDiligencia;        
	  String comisaria;              
	  String numeroProcedimiento;     
	  String juzgado;                
	  String idInstitucionComisaria;  
	  String idInstitucionJuzgado;    
	  String idEstadoAsistencia;     
	  String fechaEstadoAsistencia;  
	  String ejgIdTipoEjg;           
	  String ejgAnio;                 
	  String ejgNumero;              
	  String codigo;                
	  String fichaColegial = "";
	  ScsAsistenciasBean asistenciaBean;
	  String botonesDetalle;
	  String letradoActuaciones;
	  String validarJustificaciones;
	  String descripcion;
	  
	  DefinirTurnosForm turno;
	  DefinirGuardiasTurnosForm guardia;
	  PersonaJGForm personaJG;
	  CenPersonaBean personaColegiado;
	  private String modoPestanha=null;
	  	private String nig;
	  
	  
	
	
	public ScsAsistenciasBean getAsistenciaVO(){
		if(asistenciaBean==null){
			asistenciaBean = new ScsAsistenciasBean();
		}
		if(idInstitucion!=null && !idInstitucion.equals(""))
			asistenciaBean.setIdInstitucion(new Integer(idInstitucion));
		if(anio!=null && !anio.equals(""))
			asistenciaBean.setAnio(new Integer(anio));
		if(numero!=null && !numero.equals(""))
			asistenciaBean.setNumero(new Integer(numero));
		if(idTurno!=null && !idTurno.equals(""))
			asistenciaBean.setIdturno(new Integer(idTurno));
		if(idTipoAsistenciaColegio!=null && !idTipoAsistenciaColegio.equals(""))
			asistenciaBean.setIdTipoAsistenciaColegio(new Integer(idTipoAsistenciaColegio));
		
		asistenciaBean.setFichaColegial(fichaColegial);
		return asistenciaBean;
		
	}
	public String getBotonesDetalle() {
		
		
			if(fichaColegial!=null && fichaColegial.equals("0")){
				if((fechaAnulacion!=null && !fechaAnulacion.equals("")) || (modoPestanha!=null && modoPestanha.equalsIgnoreCase("ver")))
					botonesDetalle = "V";
				else
					botonesDetalle = "V,N";
					
					
			}else{
				if(fechaAnulacion!=null && !fechaAnulacion.equals(""))
					botonesDetalle = "";
				else{
					if(letradoActuaciones!=null && letradoActuaciones.equals("1") && modoPestanha!=null && !modoPestanha.equalsIgnoreCase("ver"))
						botonesDetalle = "N";
						
					else
						botonesDetalle = "";
				}
			}
		
		return botonesDetalle;
	}
	public String getLetradoActuaciones() {
		return letradoActuaciones;
	}
	public void setLetradoActuaciones(String letradoActuaciones) {
		this.letradoActuaciones = letradoActuaciones;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getIdTipoAsistencia() {
		return idTipoAsistencia;
	}
	public void setIdTipoAsistencia(String idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}
	public String getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}
	public String getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(String idGuardia) {
		this.idGuardia = idGuardia;
	}
	public String getIdPersonaColegiado() {
		return idPersonaColegiado;
	}
	public void setIdPersonaColegiado(String idPersonaColegiado) {
		this.idPersonaColegiado = idPersonaColegiado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getIncidencias() {
		return incidencias;
	}
	public void setIncidencias(String incidencias) {
		this.incidencias = incidencias;
	}
	public String getFechaAnulacion() {
		return fechaAnulacion;
	}
	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	public String getMotivosAnulacion() {
		return motivosAnulacion;
	}
	public void setMotivosAnulacion(String motivosAnulacion) {
		this.motivosAnulacion = motivosAnulacion;
	}
	public String getDelitosImputados() {
		return delitosImputados;
	}
	public void setDelitosImputados(String delitosImputados) {
		this.delitosImputados = delitosImputados;
	}
	public String getContrarios() {
		return contrarios;
	}
	public void setContrarios(String contrarios) {
		this.contrarios = contrarios;
	}
	public String getDatosDefensaJuridica() {
		return datosDefensaJuridica;
	}
	public void setDatosDefensaJuridica(String datosDefensaJuridica) {
		this.datosDefensaJuridica = datosDefensaJuridica;
	}
	public String getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public String getIdTipoAsistenciaColegio() {
		return idTipoAsistenciaColegio;
	}
	public void setIdTipoAsistenciaColegio(String idTipoAsistenciaColegio) {
		this.idTipoAsistenciaColegio = idTipoAsistenciaColegio;
	}
	public String getDesignaAnio() {
		return designaAnio;
	}
	public void setDesignaAnio(String designaAnio) {
		this.designaAnio = designaAnio;
	}
	public String getDesignaNumero() {
		return designaNumero;
	}
	public void setDesignaNumero(String designaNumero) {
		this.designaNumero = designaNumero;
	}
	public String getIdPersonaJG() {
		return idPersonaJG;
	}
	public void setIdPersonaJG(String idPersonaJG) {
		this.idPersonaJG = idPersonaJG;
	}
	public String getFacturado() {
		return facturado;
	}
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}
	public String getPagado() {
		return pagado;
	}
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}
	public String getIdFacturacion() {
		return idFacturacion;
	}
	public void setIdFacturacion(String idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public String getDesignaTurno() {
		return designaTurno;
	}
	public void setDesignaTurno(String designaTurno) {
		this.designaTurno = designaTurno;
	}
	public String getNumeroDiligencia() {
		return numeroDiligencia;
	}
	public void setNumeroDiligencia(String numeroDiligencia) {
		this.numeroDiligencia = numeroDiligencia;
	}
	public String getComisaria() {
		return comisaria;
	}
	public void setComisaria(String comisaria) {
		this.comisaria = comisaria;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public String getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}
	public String getIdInstitucionComisaria() {
		return idInstitucionComisaria;
	}
	public void setIdInstitucionComisaria(String idInstitucionComisaria) {
		this.idInstitucionComisaria = idInstitucionComisaria;
	}
	public String getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	public void setIdInstitucionJuzgado(String idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	public String getIdEstadoAsistencia() {
		return idEstadoAsistencia;
	}
	public void setIdEstadoAsistencia(String idEstadoAsistencia) {
		this.idEstadoAsistencia = idEstadoAsistencia;
	}
	public String getFechaEstadoAsistencia() {
		return fechaEstadoAsistencia;
	}
	public void setFechaEstadoAsistencia(String fechaEstadoAsistencia) {
		this.fechaEstadoAsistencia = fechaEstadoAsistencia;
	}
	public String getEjgIdTipoEjg() {
		return ejgIdTipoEjg;
	}
	public void setEjgIdTipoEjg(String ejgIdTipoEjg) {
		this.ejgIdTipoEjg = ejgIdTipoEjg;
	}
	public String getEjgAnio() {
		return ejgAnio;
	}
	public void setEjgAnio(String ejgAnio) {
		this.ejgAnio = ejgAnio;
	}
	public String getEjgNumero() {
		return ejgNumero;
	}
	public void setEjgNumero(String ejgNumero) {
		this.ejgNumero = ejgNumero;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getFichaColegial() {
		return fichaColegial;
	}
	public void setFichaColegial(String fichaColegial) {
		this.fichaColegial = fichaColegial;
	}
	public void setBotonesDetalle(String botonesDetalle) {
		this.botonesDetalle = botonesDetalle;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public DefinirTurnosForm getTurno() {
		return turno;
	}
	public void setTurno(DefinirTurnosForm turno) {
		this.turno = turno;
	}
	public DefinirGuardiasTurnosForm getGuardia() {
		return guardia;
	}
	public void setGuardia(DefinirGuardiasTurnosForm guardia) {
		this.guardia = guardia;
	}
	public PersonaJGForm getPersonaJG() {
		return personaJG;
	}
	public void setPersonaJG(PersonaJGForm personaJG) {
		this.personaJG = personaJG;
	}
	public CenPersonaBean getPersonaColegiado() {
		return personaColegiado;
	}
	public void setPersonaColegiado(CenPersonaBean personaColegiado) {
		this.personaColegiado = personaColegiado;
	}
	public String getValidarJustificaciones() {
		return validarJustificaciones;
	}
	public void setValidarJustificaciones(String validarJustificaciones) {
		this.validarJustificaciones = validarJustificaciones;
	}
	public String getModoPestanha() {
		return modoPestanha;
	}
	public void setModoPestanha(String modoPestanha) {
		this.modoPestanha = modoPestanha;
	}

	public String getNig() {
		return nig;
	}
	public void setNig(String nig) {
		this.nig = nig;
	}
	
}