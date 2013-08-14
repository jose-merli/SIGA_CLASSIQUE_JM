package com.siga.censo.ws.form;

import java.util.List;

import com.siga.general.MasterForm;


public class EdicionColegiadoForm extends MasterForm {
	private Long idcensodatos = null;
	
	private String publicarcolegiado = null;  
	private String ncolegiado = null;  
	private String numsolicitudcolegiacion = null;  
	private String nombre = null;  
	private String apellido1 = null;  
	private String apellido2 = null;  
	private String sexo = null;  
	private String fechanacimiento = null;  
	private String idcensotipoidentificacion = null;  
	private String numdocumento = null;  
	
	private String idcensodireccion = null;

	private String publicartelefono = null;  
	private String telefono = null;  
	private String publicartelefonomovil = null;  
	private String telefonomovil = null;  
	private String publicarfax = null;  
	private String fax = null;  
	private String publicaremail = null;  
	private String email = null;  
	private String idecomcensosituacionejer = null;  
	private String fechasituacion = null;  
	private String fechamodifrecibida = null;  
	private String residente = null;  
	private String idestadocolegiado = null;  

	private String publicardireccion = null;  
	private String desctipovia = null;  
	private String domicilio = null;  
	private String codigopostal = null;  
	private String codigopoblacion = null;  
	private String descripcionpoblacion = null;  
	private String codigoprovincia = null;  
	private String descripcionprovincia = null;  
	private String descripcionpoblacionextranj = null;  
	private String codigopaisextranj = null;  
	private String descripcionpaisextranj = null;
	
	private String fechaCambio = null;
	private List<String> incidencias = null;
	
	private boolean historico = false;
	private Long idcensodatosPadre = null;
	
	
	public String getPublicarcolegiado() {
		return publicarcolegiado;
	}
	public void setPublicarcolegiado(String publicarcolegiado) {
		this.publicarcolegiado = publicarcolegiado;
	}
	public String getNcolegiado() {
		return ncolegiado;
	}
	public void setNcolegiado(String ncolegiado) {
		this.ncolegiado = ncolegiado;
	}
	public String getNumsolicitudcolegiacion() {
		return numsolicitudcolegiacion;
	}
	public void setNumsolicitudcolegiacion(String numsolicitudcolegiacion) {
		this.numsolicitudcolegiacion = numsolicitudcolegiacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getFechanacimiento() {
		return fechanacimiento;
	}
	public void setFechanacimiento(String fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}
	public String getIdcensotipoidentificacion() {
		return idcensotipoidentificacion;
	}
	public void setIdcensotipoidentificacion(String idcensotipoidentificacion) {
		this.idcensotipoidentificacion = idcensotipoidentificacion;
	}
	public String getNumdocumento() {
		return numdocumento;
	}
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}
	public String getIdcensodireccion() {
		return idcensodireccion;
	}
	public void setIdcensodireccion(String idcensodireccion) {
		this.idcensodireccion = idcensodireccion;
	}
	public String getPublicartelefono() {
		return publicartelefono;
	}
	public void setPublicartelefono(String publicartelefono) {
		this.publicartelefono = publicartelefono;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getPublicartelefonomovil() {
		return publicartelefonomovil;
	}
	public void setPublicartelefonomovil(String publicartelefonomovil) {
		this.publicartelefonomovil = publicartelefonomovil;
	}
	public String getTelefonomovil() {
		return telefonomovil;
	}
	public void setTelefonomovil(String telefonomovil) {
		this.telefonomovil = telefonomovil;
	}
	public String getPublicarfax() {
		return publicarfax;
	}
	public void setPublicarfax(String publicarfax) {
		this.publicarfax = publicarfax;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPublicaremail() {
		return publicaremail;
	}
	public void setPublicaremail(String publicaremail) {
		this.publicaremail = publicaremail;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdecomcensosituacionejer() {
		return idecomcensosituacionejer;
	}
	public void setIdecomcensosituacionejer(String idecomcensosituacionejer) {
		this.idecomcensosituacionejer = idecomcensosituacionejer;
	}
	public String getFechasituacion() {
		return fechasituacion;
	}
	public void setFechasituacion(String fechasituacion) {
		this.fechasituacion = fechasituacion;
	}
	public String getFechamodifrecibida() {
		return fechamodifrecibida;
	}
	public void setFechamodifrecibida(String fechamodifrecibida) {
		this.fechamodifrecibida = fechamodifrecibida;
	}
	public String getResidente() {
		return residente;
	}
	public void setResidente(String residente) {
		this.residente = residente;
	}
	public String getIdestadocolegiado() {
		return idestadocolegiado;
	}
	public void setIdestadocolegiado(String idestadocolegiado) {
		this.idestadocolegiado = idestadocolegiado;
	}
	public String getPublicardireccion() {
		return publicardireccion;
	}
	public void setPublicardireccion(String publicardireccion) {
		this.publicardireccion = publicardireccion;
	}
	public String getDesctipovia() {
		return desctipovia;
	}
	public void setDesctipovia(String desctipovia) {
		this.desctipovia = desctipovia;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getCodigopostal() {
		return codigopostal;
	}
	public void setCodigopostal(String codigopostal) {
		this.codigopostal = codigopostal;
	}
	public String getCodigopoblacion() {
		return codigopoblacion;
	}
	public void setCodigopoblacion(String codigopoblacion) {
		this.codigopoblacion = codigopoblacion;
	}
	public String getDescripcionpoblacion() {
		return descripcionpoblacion;
	}
	public void setDescripcionpoblacion(String descripcionpoblacion) {
		this.descripcionpoblacion = descripcionpoblacion;
	}
	public String getCodigoprovincia() {
		return codigoprovincia;
	}
	public void setCodigoprovincia(String codigoprovincia) {
		this.codigoprovincia = codigoprovincia;
	}
	public String getDescripcionprovincia() {
		return descripcionprovincia;
	}
	public void setDescripcionprovincia(String descripcionprovincia) {
		this.descripcionprovincia = descripcionprovincia;
	}
	public String getDescripcionpoblacionextranj() {
		return descripcionpoblacionextranj;
	}
	public void setDescripcionpoblacionextranj(String descripcionpoblacionextranj) {
		this.descripcionpoblacionextranj = descripcionpoblacionextranj;
	}
	public String getCodigopaisextranj() {
		return codigopaisextranj;
	}
	public void setCodigopaisextranj(String codigopaisextranj) {
		this.codigopaisextranj = codigopaisextranj;
	}
	public String getDescripcionpaisextranj() {
		return descripcionpaisextranj;
	}
	public void setDescripcionpaisextranj(String descripcionpaisextranj) {
		this.descripcionpaisextranj = descripcionpaisextranj;
	}
	public Long getIdcensodatos() {
		return idcensodatos;
	}
	public void setIdcensodatos(Long idcensodatos) {
		this.idcensodatos = idcensodatos;
	}
	public List<String> getIncidencias() {
		return incidencias;
	}
	public void setIncidencias(List<String> incidencias) {
		this.incidencias = incidencias;
	}
	public String getFechaCambio() {
		return fechaCambio;
	}
	public void setFechaCambio(String fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
	public boolean isHistorico() {
		return historico;
	}
	public void setHistorico(boolean historico) {
		this.historico = historico;
	}
	public Long getIdcensodatosPadre() {
		return idcensodatosPadre;
	}
	public void setIdcensodatosPadre(Long idcensodatosPadre) {
		this.idcensodatosPadre = idcensodatosPadre;
	}
		
}
