
package com.siga.beans;

import com.atos.utils.GstDate;
import com.siga.censo.form.AlterMutuaForm;

/**
 * 
 * @author josebd
 *
 */

public class CenSolicitudAlterMutuaBean extends MasterBean {
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICITUDALTER";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD                = "IDSOLICITUD";
	static public final String C_IDSOLICITUDALTER			= "IDSOLICITUDALTER";
	static public final String C_NOMBRE                     = "NOMBRE";
	static public final String C_APELLIDOS                  = "APELLIDOS";
	static public final String C_NUMEROIDENTIFICADOR        = "NUMEROIDENTIFICADOR";
	static public final String C_DOMICILIO                  = "DOMICILIO";
	static public final String C_CODIGOPOSTAL               = "CODIGOPOSTAL";
	static public final String C_TELEFONO1                  = "TELEFONO1";
	static public final String C_CORREOELECTRONICO          = "CORREOELECTRONICO";
	static public final String C_IDINSTITUCION              = "IDINSTITUCION";
	static public final String C_FECHAMODIFICACION          = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION            = "USUMODIFICACION";
	static public final String C_FECHANACIMIENTO            = "FECHANACIMIENTO";
	static public final String C_IDTIPOIDENTIFICACION       = "IDTIPOIDENTIFICACION";
	static public final String C_IDPROVINCIA                = "IDPROVINCIA";
	static public final String C_TELEFONO2                  = "TELEFONO2";
	static public final String C_MOVIL                      = "MOVIL";
	static public final String C_FAX                        = "FAX";
	static public final String C_IDESTADOCIVIL              = "IDESTADOCIVIL";
	static public final String C_IDPAIS                     = "IDPAIS";
	static public final String C_IDSEXO                     = "IDSEXO";
	static public final String C_CODIGOSUCURSAL             = "CODIGOSUCURSAL";
	static public final String C_CBO_CODIGO                 = "CBO_CODIGO";
	static public final String C_DIGITOCONTROL              = "DIGITOCONTROL";
	static public final String C_NUMEROCUENTA               = "NUMEROCUENTA";
	static public final String C_IBAN                       = "IBAN";
	static public final String C_IDCOBERTURA                = "IDCOBERTURA";
	static public final String C_OTROSBENEFICIARIOS         = "OTROSBENEFICIARIOS";
	static public final String C_SWIFT                      = "SWIFT";
	static public final String C_PAIS						= "PAIS";
	static public final String C_PROVINCIA                  = "PROVINCIA";
	static public final String C_POBLACION                  = "POBLACION";
	static public final String C_ESTADO                     = "ESTADO";
	static public final String C_PROPUESTA                  = "PROPUESTA";
	static public final String C_IDPAQUETE                  = "IDPAQUETE";
	static public final String C_IDTIPOEJERCICIO            = "IDTIPOEJERCICIO";
	static public final String C_IDPERSONA            		= "IDPERSONA";
	static public final String C_FAMILIARES            		= "FAMILIARES";
	static public final String C_BENEFICIARIOS         		= "BENEFICIARIOS";
	static public final String C_BREVEPAQUETE         		= "BREVEPAQUETE";
	static public final String C_TARIFAPAQUETE         		= "TARIFAPAQUETE";
	static public final String C_DESCRIPCIONPAQUETE    		= "DESCRIPCIONPAQUETE";
	static public final String C_NOMBREPAQUETE    			= "NOMBREPAQUETE";
	
	private String idSolicitud;
	private String idSolicitudalter;
	private String nombre;
	private String apellidos;
	private String identificador;
	private String domicilio;
	private String codigoPostal;
	private String telefono1;
	private String correoElectronico;
	private String idInstitucion;
	private String fechaModificacion;
	private String usuModificacion;
	private String fechaNacimiento;
	private String idTipoIdentificacion;
	private String idProvincia;
	private String telefono2;
	private String movil;
	private String fax;
	private String idEstadoCivil;
	private String idPais;
	private String idSexo;
	private String codigoSucursal;
	private String cboCodigo;
	private String digitoControl;
	private String numeroCuenta;
	private String iban;
	private String idCobertura;
	private String otrosBeneficiarios;
	private String swift;
	private String pais;
	private String provincia;
	private String poblacion;
	private String estado;	
	private String propuesta;	
	private String idPaquete;	
	private String tipoEjercicio;	
	private String idTipoEjercicio;	
	private String idPersona;	
	private String familiares;	
	private String beneficiarios;	
	private String brevePaquete;
	private String tarifaPaquete;
	private String descripcionPaquete;
	private String nombrePaquete;
	
	

	public String getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getIdSolicitudAlter() {
		return idSolicitudalter;
	}

	public void setIdSolicitudAlter(String idSolicitudalter) {
		this.idSolicitudalter = idSolicitudalter;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String numeroIdentificador) {
		this.identificador = numeroIdentificador;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuModificacion() {
		return usuModificacion;
	}

	public void setUsuModificacion(String usuModificacion) {
		this.usuModificacion = usuModificacion;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}

	public void setIdTipoIdentificacion(String idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}

	public String getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(String idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public String getIdPais() {
		return idPais;
	}

	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	public String getIdSexo() {
		return idSexo;
	}

	public void setIdSexo(String idSexo) {
		this.idSexo = idSexo;
	}

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public String getCboCodigo() {
		return cboCodigo;
	}

	public void setCboCodigo(String cboCodigo) {
		this.cboCodigo = cboCodigo;
	}

	public String getDigitoControl() {
		return digitoControl;
	}

	public void setDigitoControl(String digitoControl) {
		this.digitoControl = digitoControl;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIdCobertura() {
		return idCobertura;
	}

	public void setIdCobertura(String idCobertura) {
		this.idCobertura = idCobertura;
	}

	public String getOtrosBeneficiarios() {
		return otrosBeneficiarios;
	}

	public void setOtrosBeneficiarios(String otrosBeneficiarios) {
		this.otrosBeneficiarios = otrosBeneficiarios;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPropuesta() {
		return propuesta;
	}

	public void setPropuesta(String propuesta) {
		this.propuesta = propuesta;
	}
	
	

	public String getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}
	

	public String getTipoEjercicio() {
		return tipoEjercicio;
	}

	public void setTipoEjercicio(String tipoEjercicio) {
		this.tipoEjercicio = tipoEjercicio;
	}

	public String getIdTipoEjercicio() {
		return idTipoEjercicio;
	}

	public void setIdTipoEjercicio(String idTipoEjercicio) {
		this.idTipoEjercicio = idTipoEjercicio;
	}
	
	public String getFamiliares() {
		return familiares;
	}

	public void setFamiliares(String familiares) {
		this.familiares = familiares;
	}

	public String getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(String beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	
	public String getBrevePaquete() {
		return brevePaquete;
	}

	public void setBrevePaquete(String brevePaquete) {
		this.brevePaquete = brevePaquete;
	}

	public String getTarifaPaquete() {
		return tarifaPaquete;
	}

	public void setTarifaPaquete(String tarifaPaquete) {
		this.tarifaPaquete = tarifaPaquete;
	}

	public String getDescripcionPaquete() {
		return descripcionPaquete;
	}

	public void setDescripcionPaquete(String descripcionPaquete) {
		this.descripcionPaquete = descripcionPaquete;
	}

	public String getNombrePaquete() {
		return nombrePaquete;
	}

	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}

	public AlterMutuaForm getAlterMutuaForm(AlterMutuaForm alterMutuaForm) {
		alterMutuaForm.setApellidos(this.apellidos);
		alterMutuaForm.setCodigoPostal(this.codigoPostal);
		alterMutuaForm.setCorreoElectronico(this.correoElectronico);
		alterMutuaForm.setDomicilio(this.domicilio);
		alterMutuaForm.setFax(this.fax);
		alterMutuaForm.setIdEstadoCivil(this.idEstadoCivil);
		alterMutuaForm.setIdInstitucion(this.idInstitucion.toString());
		alterMutuaForm.setIdPais(this.idPais);
		alterMutuaForm.setIdProvincia(this.idProvincia);
		alterMutuaForm.setIdSolicitudAlter(this.idSolicitudalter);
		alterMutuaForm.setIdSolicitud(this.idSolicitud);
		alterMutuaForm.setIdTipoIdentificacion(this.idTipoIdentificacion.toString());
		alterMutuaForm.setMovil(this.movil);
		alterMutuaForm.setNombre(this.nombre);
		alterMutuaForm.setIdentificador(this.identificador);
		alterMutuaForm.setTelefono1(this.telefono1);
		alterMutuaForm.setTelefono2(this.telefono2);
		alterMutuaForm.setSexo(this.idSexo.toString());
		alterMutuaForm.setCboCodigo(this.cboCodigo);
		alterMutuaForm.setCodigoSucursal(this.codigoSucursal);
		alterMutuaForm.setDigitoControl(this.digitoControl);
		alterMutuaForm.setNumeroCuenta(this.numeroCuenta);
		alterMutuaForm.setIban(this.iban);
		alterMutuaForm.setSwift(this.swift);
		alterMutuaForm.setIdPaisCuenta(this.pais);
		alterMutuaForm.setProvincia(this.provincia);
		alterMutuaForm.setPoblacion(this.poblacion);
		alterMutuaForm.setIdPaquete(this.idPaquete);
		alterMutuaForm.setIdTipoEjercicio(this.idTipoEjercicio);
		alterMutuaForm.setTipoEjercicio(this.tipoEjercicio);
		alterMutuaForm.setIdPersona(this.idPersona);
		alterMutuaForm.setFamiliares(this.familiares);
		alterMutuaForm.setHerederos(this.beneficiarios);
		
		alterMutuaForm.setDescripcionPaquete(this.descripcionPaquete);
		alterMutuaForm.setBrevePaquete(this.brevePaquete);
		alterMutuaForm.setTarifaPaquete(this.tarifaPaquete);
		alterMutuaForm.setNombrePaquete(this.nombrePaquete);
		
		try {
			alterMutuaForm.setPropuesta(Integer.parseInt(this.propuesta));
			alterMutuaForm.setFechaNacimiento(GstDate.getFormatedDateShort("",this.fechaNacimiento));
		} catch (Exception e) {
		}
		return alterMutuaForm;
		
	}
	
	public AlterMutuaForm getAlterMutuaForm() {
		AlterMutuaForm alterMutuaForm = new AlterMutuaForm();
		alterMutuaForm=getAlterMutuaForm(alterMutuaForm);
		return alterMutuaForm;
		
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	
}