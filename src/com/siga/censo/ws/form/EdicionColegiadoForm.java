package com.siga.censo.ws.form;

import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;

import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterForm;


public class EdicionColegiadoForm extends MasterForm {
	private Long idcensodatos = null;
	
	private boolean publicarcolegiado = false;  
	private String ncolegiado = null;  
	private String numsolicitudcolegiacion = null;  
	private String nombre = null;  
	private String apellido1 = null;  
	private String apellido2 = null;  
	private String sexo = null;  
	private String fechanacimiento = null;  
	private Short idcensotipoidentificacion = null;  
	private String numdocumento = null;  
	
	private Long idcensodireccion = null;

	private boolean publicartelefono = false;  
	private String telefono = null;  
	private boolean publicartelefonomovil = false;  
	private String telefonomovil = null;  
	private boolean publicarfax = false;  
	private String fax = null;  
	private boolean publicaremail = false;  
	private String email = null;  
	private Short idecomcensosituacionejer = null;  
	private String fechasituacion = null;  
	private String fechamodifrecibida = null;  
	private boolean residente = false;  
	private Short idestadocolegiado = null;  

	private boolean publicardireccion = false;  
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
	
	private Long idpersona = null;
	private Short idinstitucion = null;
	
	private boolean historico = false;
	private Long idcensodatosPadre = null;
	private String accionPadre = null;
		
	private List<ValueKeyVO> tiposIdentificacion;
	private List<ValueKeyVO> situacionesEjerciente;
	private List<ValueKeyVO> sexos;
	
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
	public Short getIdcensotipoidentificacion() {
		return idcensotipoidentificacion;
	}
	public void setIdcensotipoidentificacion(Short idcensotipoidentificacion) {
		this.idcensotipoidentificacion = idcensotipoidentificacion;
	}
	public String getNumdocumento() {
		return numdocumento;
	}
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}
	public Long getIdcensodireccion() {
		return idcensodireccion;
	}
	public void setIdcensodireccion(Long idcensodireccion) {
		this.idcensodireccion = idcensodireccion;
	}
	
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getTelefonomovil() {
		return telefonomovil;
	}
	public void setTelefonomovil(String telefonomovil) {
		this.telefonomovil = telefonomovil;
	}
	
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Short getIdecomcensosituacionejer() {
		return idecomcensosituacionejer;
	}
	public void setIdecomcensosituacionejer(Short idecomcensosituacionejer) {
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
	
	public Short getIdestadocolegiado() {
		return idestadocolegiado;
	}
	public void setIdestadocolegiado(Short idestadocolegiado) {
		this.idestadocolegiado = idestadocolegiado;
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
	public boolean isPublicarcolegiado() {
		return publicarcolegiado;
	}
	public void setPublicarcolegiado(boolean publicarcolegiado) {
		this.publicarcolegiado = publicarcolegiado;
	}
	public boolean isPublicartelefono() {
		return publicartelefono;
	}
	public void setPublicartelefono(boolean publicartelefono) {
		this.publicartelefono = publicartelefono;
	}
	public boolean isPublicartelefonomovil() {
		return publicartelefonomovil;
	}
	public void setPublicartelefonomovil(boolean publicartelefonomovil) {
		this.publicartelefonomovil = publicartelefonomovil;
	}
	public boolean isPublicarfax() {
		return publicarfax;
	}
	public void setPublicarfax(boolean publicarfax) {
		this.publicarfax = publicarfax;
	}
	public boolean isPublicaremail() {
		return publicaremail;
	}
	public void setPublicaremail(boolean publicaremail) {
		this.publicaremail = publicaremail;
	}
	public boolean isPublicardireccion() {
		return publicardireccion;
	}
	public void setPublicardireccion(boolean publicardireccion) {
		this.publicardireccion = publicardireccion;
	}
	public boolean isResidente() {
		return residente;
	}
	public void setResidente(boolean residente) {
		this.residente = residente;
	}
	public List<ValueKeyVO> getTiposIdentificacion() {
		return tiposIdentificacion;
	}
	public void setTiposIdentificacion(List<ValueKeyVO> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}
	public List<ValueKeyVO> getSituacionesEjerciente() {
		return situacionesEjerciente;
	}
	public void setSituacionesEjerciente(List<ValueKeyVO> situacionesEjerciente) {
		this.situacionesEjerciente = situacionesEjerciente;
	}
	public boolean isColegiadoEditable() {
		if (idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.INCIDENCIAS_DATOS_COLEGIADO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ERROR_ALTA_COLEGIADO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ERROR_ALTA_PERSONA_COLEGIADO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ERROR_ACTUALIZACION_COLEGIADO.getCodigo()) {
			return true;
		} else {
			return false;
		}
	}
	public Long getIdpersona() {
		return idpersona;
	}
	public void setIdpersona(Long idpersona) {
		this.idpersona = idpersona;
	}
	public Short getIdinstitucion() {
		return idinstitucion;
	}
	public void setIdinstitucion(Short idinstitucion) {
		this.idinstitucion = idinstitucion;
	}
	public List<ValueKeyVO> getSexos() {
		return sexos;
	}
	public void setSexos(List<ValueKeyVO> sexos) {
		this.sexos = sexos;
	}
	public String getAccionPadre() {
		return accionPadre;
	}
	public void setAccionPadre(String accionPadre) {
		this.accionPadre = accionPadre;
	}
	
		
}
