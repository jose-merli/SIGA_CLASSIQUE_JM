
package com.siga.beans;

import com.atos.utils.GstDate;
import com.siga.censo.form.MutualidadForm;
import com.siga.general.MasterForm;

/**
 * 
 * @author jorgeta
 *
 */

public class CenSolicitudMutualidadBean extends MasterBean {


	static public String SERVICIO_NOSOLICITADO = "Servicio no solicitado";
	static public int ESTADO_INICIAL = 0; 
	static public String ESTADO_PTERESPUESTA = "Pendiente Respuesta";
	static public int ESTADO_SOLICITADO = 1;
	static public String TIPOSOLICITUD_PLANPROFESIONAL = "P";
	static public String TIPOSOLICITUD_SEGUROUNIVERSAL = "S";

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICITUDMUTUALIDAD";
	static public final String SEQ_CEN_SOLICITUDMUTUALIDAD = "SEQ_CENSOLICITUDMUTUALIDAD";
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD = "IDSOLICITUD";
	static public final String C_IDSOLICITUDINCORPORACION = "IDSOLICITUDINCORPORACION";
	static public final String C_FECHASOLICITUD="FECHASOLICITUD";
	static public final String C_IDTRATAMIENTO = "IDTRATAMIENTO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_APELLIDO1 = "APELLIDO1";
	static public final String C_APELLIDO2 = "APELLIDO2";
	static public final String C_NUMEROIDENTIFICADOR = "NUMEROIDENTIFICADOR";
	static public final String C_DOMICILIO = "DOMICILIO";
	static public final String C_CODIGOPOSTAL = "CODIGOPOSTAL";
	static public final String C_TELEFONO1 = "TELEFONO1";
	static public final String C_CORREOELECTRONICO = "CORREOELECTRONICO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDESTADO = "IDESTADO";
	static public final String C_IDTIPOSOLICITUD = "IDTIPOSOLICITUD";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_FECHANACIMIENTO = "FECHANACIMIENTO";
	static public final String C_IDTIPOIDENTIFICACION = "IDTIPOIDENTIFICACION";
	static public final String C_IDPROVINCIA = "IDPROVINCIA";
	static public final String C_IDPOBLACION = "IDPOBLACION";
	static public final String C_FECHAESTADO = "FECHAESTADO";
	static public final String C_TELEFONO2 = "TELEFONO2";
	static public final String C_MOVIL = "MOVIL";
	static public final String C_FAX1 = "FAX1";
	static public final String C_FAX2 = "FAX2";
	static public final String C_IDESTADOCIVIL = "IDESTADOCIVIL";
	static public final String C_IDPAIS = "IDPAIS";
	static public final String C_NATURALDE = "NATURALDE";
	static public final String C_IDSEXO = "IDSEXO";
	static public final String C_POBLACIONEXTRANJERA = "POBLACIONEXTRANJERA";
	static public final String C_TITULAR = "TITULAR";
	static public final String C_CODIGOSUCURSAL = "CODIGOSUCURSAL";
	static public final String C_CBO_CODIGO = "CBO_CODIGO";
	static public final String C_DIGITOCONTROL = "DIGITOCONTROL";
	static public final String C_NUMEROCUENTA = "NUMEROCUENTA";
	static public final String C_IBAN = "IBAN";
	static public final String C_IDPERIODICIDADPAGO = "IDPERIODICIDADPAGO";
	static public final String C_IDCOBERTURA = "IDCOBERTURA";
	static public final String C_IDBENEFICIARIO = "IDBENEFICIARIO";
	static public final String C_OTROSBENEFICIARIOS = "OTROSBENEFICIARIOS";
	static public final String C_IDASISTENCIASANITARIA = "IDASISTENCIASANITARIA";
	static public final String C_FECHANACIMIENTOCONYUGE = "FECHANACIMIENTOCONYUGE";
	static public final String C_NUMEROHIJOS = "NUMEROHIJOS";
	static public final String C_EDADHIJO1 = "EDADHIJO1";
	static public final String C_EDADHIJO2 = "EDADHIJO2";
	static public final String C_EDADHIJO3 = "EDADHIJO3";
	static public final String C_EDADHIJO4 = "EDADHIJO4";
	static public final String C_EDADHIJO5 = "EDADHIJO5";
	static public final String C_EDADHIJO6 = "EDADHIJO6";
	static public final String C_EDADHIJO7 = "EDADHIJO7";
	static public final String C_EDADHIJO8 = "EDADHIJO8";
	static public final String C_EDADHIJO9 = "EDADHIJO9";
	static public final String C_EDADHIJO10 = "EDADHIJO10";
	
	static public final String C_SWIFT = "SWIFT";
	static public final String C_PERIODICIDADPAGO = "PERIODICIDADPAGO";
	static public final String C_COBERTURA = "COBERTURA";
	static public final String C_BENEFICIARIO = "BENEFICIARIO";
	static public final String C_ASISTENCIASANITARIA = "ASISTENCIASANITARIA";
	static public final String C_CUOTACOBERTURA = "CUOTACOBERTURA";
	static public final String C_CAPITALCOBERTURA = "CAPITALCOBERTURA";
	static public final String C_IDSOLICITUDACEPTADA = "IDSOLICITUDACEPTADA";
	
	static public final String C_PAIS = "PAIS";
	static public final String C_POBLACION = "POBLACION";
	static public final String C_PROVINCIA = "PROVINCIA";
	
	static public final String C_ESTADO = "ESTADO";
	static public final String C_ESTADOMUTUALISTA = "ESTADOMUTUALISTA";
	
	
	
	private Long idSolicitud;
	private Long idSolicitudIncorporacion;
	private String idTipoSolicitud;
	private Integer idInstitucion;
	private Integer idTipoIdentificacion;
	private String numeroIdentificacion;
	private String idSexo;
	private Integer idTratamiento;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String naturalDe;
	private String fechaNacimiento;
	private String idEstadoCivil;
	private String idPais;
	private String idProvincia;
	private String idPoblacion;
	private String poblacionExtranjera;
	private String domicilio;
	private String codigoPostal;
	private String telef1;
	private String telef2;
	private String movil;
	private String fax1;
	private String fax2;
	private String correoElectronico;
	private String titular;
	private String cboCodigo;
	private String codigoSucursal;
	private String digitoControl;
	private String numeroCuenta;
	private String iban;
	private String idPeriodicidadPago;
	private String idCobertura;
	private String idBeneficiario;
	private String otrosBeneficiarios;
	private String idAsistenciaSanitaria;
	private String fechaNacimientoConyuge;
	private Integer numeroHijos;
	private String edadHijo1;
	private String edadHijo2;
	private String edadHijo3;
	private String edadHijo4;
	private String edadHijo5;
	private String edadHijo6;
	private String edadHijo7;
	private String edadHijo8;
	private String edadHijo9;
	private String edadHijo10;
	private String fechaEstado;
	private Integer idEstado;
	private String fechaSolicitud;
	
	private String swift;
	private String periodicidadPago;
	private String cobertura;
	private String beneficiario;
	private String asistenciaSanitaria;
	
	private String cuotaCobertura;
	private String capitalCobertura;
	private Long idSolicitudAceptada;
	
	private String pais;
	private String provincia;
	private String poblacion;
	
	private String estado;
	private String estadoMutualista;
	private String pdf;
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}
	public void setIdTipoIdentificacion(Integer idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	
	public Integer getIdTratamiento() {
		return idTratamiento;
	}
	public void setIdTratamiento(Integer idTratamiento) {
		this.idTratamiento = idTratamiento;
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
	public String getNaturalDe() {
		return naturalDe;
	}
	public void setNaturalDe(String naturalDe) {
		this.naturalDe = naturalDe;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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
	public String getIdProvincia() {
		return idProvincia;
	}
	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}
	public String getIdPoblacion() {
		return idPoblacion;
	}
	public void setIdPoblacion(String idPoblacion) {
		this.idPoblacion = idPoblacion;
	}
	public String getPoblacionExtranjera() {
		return poblacionExtranjera;
	}
	public void setPoblacionExtranjera(String poblacionExtranjera) {
		this.poblacionExtranjera = poblacionExtranjera;
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
	public String getTelef1() {
		return telef1;
	}
	public void setTelef1(String telef1) {
		this.telef1 = telef1;
	}
	public String getTelef2() {
		return telef2;
	}
	public void setTelef2(String telef2) {
		this.telef2 = telef2;
	}
	public String getMovil() {
		return movil;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	public String getFax1() {
		return fax1;
	}
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	public String getFax2() {
		return fax2;
	}
	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	
	public String getCboCodigo() {
		return cboCodigo;
	}
	public void setCboCodigo(String cboCodigo) {
		this.cboCodigo = cboCodigo;
	}
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
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
	public String getIdPeriodicidadPago() {
		return idPeriodicidadPago;
	}
	public void setIdPeriodicidadPago(String idPeriodicidadPago) {
		this.idPeriodicidadPago = idPeriodicidadPago;
	}
	public String getIdCobertura() {
		return idCobertura;
	}
	public void setIdCobertura(String idCobertura) {
		this.idCobertura = idCobertura;
	}
	public String getIdBeneficiario() {
		return idBeneficiario;
	}
	public void setIdBeneficiario(String idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}
	public String getOtrosBeneficiarios() {
		return otrosBeneficiarios;
	}
	public void setOtrosBeneficiarios(String otrosBeneficiarios) {
		this.otrosBeneficiarios = otrosBeneficiarios;
	}
	public String getIdAsistenciaSanitaria() {
		return idAsistenciaSanitaria;
	}
	public void setIdAsistenciaSanitaria(String idAsistenciaSanitaria) {
		this.idAsistenciaSanitaria = idAsistenciaSanitaria;
	}
	public String getFechaNacimientoConyuge() {
		return fechaNacimientoConyuge;
	}
	public void setFechaNacimientoConyuge(String fechaNacimientoConyuge) {
		this.fechaNacimientoConyuge = fechaNacimientoConyuge;
	}
	public Integer getNumeroHijos() {
		return numeroHijos;
	}
	public void setNumeroHijos(Integer numeroHijos) {
		this.numeroHijos = numeroHijos;
	}
	public String getEdadHijo1() {
		return edadHijo1;
	}
	public void setEdadHijo1(String edadHijo1) {
		this.edadHijo1 = edadHijo1;
	}
	public String getEdadHijo2() {
		return edadHijo2;
	}
	public void setEdadHijo2(String edadHijo2) {
		this.edadHijo2 = edadHijo2;
	}
	public String getEdadHijo3() {
		return edadHijo3;
	}
	public void setEdadHijo3(String edadHijo3) {
		this.edadHijo3 = edadHijo3;
	}
	public String getEdadHijo4() {
		return edadHijo4;
	}
	public void setEdadHijo4(String edadHijo4) {
		this.edadHijo4 = edadHijo4;
	}
	public String getEdadHijo5() {
		return edadHijo5;
	}
	public void setEdadHijo5(String edadHijo5) {
		this.edadHijo5 = edadHijo5;
	}
	public String getEdadHijo6() {
		return edadHijo6;
	}
	public void setEdadHijo6(String edadHijo6) {
		this.edadHijo6 = edadHijo6;
	}
	public String getEdadHijo7() {
		return edadHijo7;
	}
	public void setEdadHijo7(String edadHijo7) {
		this.edadHijo7 = edadHijo7;
	}
	public String getEdadHijo8() {
		return edadHijo8;
	}
	public void setEdadHijo8(String edadHijo8) {
		this.edadHijo8 = edadHijo8;
	}
	public String getEdadHijo9() {
		return edadHijo9;
	}
	public void setEdadHijo9(String edadHijo9) {
		this.edadHijo9 = edadHijo9;
	}
	public String getEdadHijo10() {
		return edadHijo10;
	}
	public void setEdadHijo10(String edadHijo10) {
		this.edadHijo10 = edadHijo10;
	}
	public Long getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public Integer getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public Long getIdSolicitudIncorporacion() {
		return idSolicitudIncorporacion;
	}
	public void setIdSolicitudIncorporacion(Long idSolicitudIncorporacion) {
		this.idSolicitudIncorporacion = idSolicitudIncorporacion;
	}
	public MutualidadForm getMutualidadForm(MutualidadForm mutualidadForm) {
		mutualidadForm.setApellido1(this.apellido1);
		mutualidadForm.setApellido2(this.apellido2);
		mutualidadForm.setCodigoPostal(this.codigoPostal);
		mutualidadForm.setCorreoElectronico(this.correoElectronico);
		mutualidadForm.setDomicilio(this.domicilio);
		mutualidadForm.setFax1(this.fax1);
		mutualidadForm.setFax2(this.fax2);

		if(idEstado!=null)
			mutualidadForm.setIdEstado(this.idEstado.toString());
		mutualidadForm.setEstado(this.estado);
		mutualidadForm.setEstadoMutualista(this.estadoMutualista);
		mutualidadForm.setIdEstadoCivil(this.idEstadoCivil);
		if(idInstitucion!=null)
			mutualidadForm.setIdInstitucion(this.idInstitucion.toString());
		mutualidadForm.setIdPais(this.idPais);
		mutualidadForm.setIdPoblacion(this.idPoblacion);
		mutualidadForm.setPoblacionExtranjera(this.poblacionExtranjera);
		mutualidadForm.setIdProvincia(this.idProvincia);
		if(idSolicitud!=null)
			mutualidadForm.setIdSolicitud(this.idSolicitud.toString());
		if(idSolicitudAceptada!=null)
			mutualidadForm.setIdSolicitudAceptada(idSolicitudAceptada.toString());
		if(idSolicitudIncorporacion!=null)
			mutualidadForm.setIdSolicitudIncorporacion(this.idSolicitudIncorporacion.toString());
		if(idTipoIdentificacion!=null)
			mutualidadForm.setIdTipoIdentificacion(this.idTipoIdentificacion.toString());
		if(idTipoSolicitud!=null)
			mutualidadForm.setIdTipoSolicitud(this.idTipoSolicitud.toString());
		if(idTratamiento!=null)
			mutualidadForm.setIdTratamiento(this.idTratamiento.toString());
		mutualidadForm.setMovil(this.movil);
		mutualidadForm.setNaturalDe(this.naturalDe);
		mutualidadForm.setNombre(this.nombre);
		mutualidadForm.setNumeroIdentificacion(this.numeroIdentificacion);
		mutualidadForm.setTelef1(this.telef1);
		mutualidadForm.setTelef2(this.telef2);
		if(idSexo!=null)
			mutualidadForm.setIdSexo(this.idSexo.toString());

		mutualidadForm.setCboCodigo(this.cboCodigo);
		mutualidadForm.setIdBanco(this.cboCodigo);
		mutualidadForm.setCodigoSucursal(this.codigoSucursal);
		mutualidadForm.setDigitoControl(this.digitoControl);
		mutualidadForm.setNumeroCuenta(this.numeroCuenta);
		mutualidadForm.setTitular(this.titular);
		mutualidadForm.setIban(this.iban);
		mutualidadForm.setIdPeriodicidadPago(this.idPeriodicidadPago);
		mutualidadForm.setIdCobertura(this.idCobertura);
		mutualidadForm.setIdBeneficiario(this.idBeneficiario);
		mutualidadForm.setOtrosBeneficiarios(this.otrosBeneficiarios);
		mutualidadForm.setIdAsistenciaSanitaria(this.idAsistenciaSanitaria);
		
		mutualidadForm.setSwift(this.swift);
		mutualidadForm.setPeriodicidadPago(this.periodicidadPago);
		mutualidadForm.setCobertura(this.cobertura);
		mutualidadForm.setAsistenciaSanitaria(this.asistenciaSanitaria);
		mutualidadForm.setBeneficiario(this.beneficiario);
		mutualidadForm.setCapitalCobertura(this.capitalCobertura);
		mutualidadForm.setCuotaCobertura(this.cuotaCobertura);
		mutualidadForm.setPDF(this.pdf);
		
		if(edadHijo1!=null)
			mutualidadForm.setEdadHijo1(this.edadHijo1);
		if(edadHijo2!=null)
			mutualidadForm.setEdadHijo2(this.edadHijo2);
		if(edadHijo3!=null)
			mutualidadForm.setEdadHijo3(this.edadHijo3);
		if(edadHijo4!=null)
			mutualidadForm.setEdadHijo4(this.edadHijo4);
		if(edadHijo5!=null)
			mutualidadForm.setEdadHijo5(this.edadHijo5);
		if(edadHijo6!=null)
			mutualidadForm.setEdadHijo6(this.edadHijo6);
		if(edadHijo7!=null)
			mutualidadForm.setEdadHijo7(this.edadHijo7);
		if(edadHijo8!=null)
			mutualidadForm.setEdadHijo8(this.edadHijo8);
		if(edadHijo9!=null)
			mutualidadForm.setEdadHijo9(this.edadHijo9);
		if(edadHijo10!=null)
			mutualidadForm.setEdadHijo10(this.edadHijo10);
		
		if(numeroHijos!=null)
			mutualidadForm.setNumeroHijos(this.numeroHijos.toString());
		
		mutualidadForm.setPais(this.pais);
		mutualidadForm.setProvincia(this.provincia);
		mutualidadForm.setPoblacion(this.poblacion);

		try {
			mutualidadForm.setFechaEstado(GstDate.getFormatedDateShort("", this.fechaEstado));
			mutualidadForm.setFechaNacimiento(GstDate.getFormatedDateShort("",this.fechaNacimiento));
			mutualidadForm.setFechaSolicitud(GstDate.getFormatedDateShort("",this.fechaSolicitud));
			mutualidadForm.setFechaNacimientoConyuge(GstDate.getFormatedDateShort("",this.fechaNacimientoConyuge));
			

		} catch (Exception e) {
		}
		return mutualidadForm;
		
	}
	
	public MutualidadForm getMutualidadForm() {
		MutualidadForm mutualidadForm = new MutualidadForm();
		mutualidadForm=getMutualidadForm(mutualidadForm);

		
		return mutualidadForm;
		
	}
	public String getIdTipoSolicitud() {
		return idTipoSolicitud;
	}
	public void setIdTipoSolicitud(String idTipoSolicitud) {
		this.idTipoSolicitud = idTipoSolicitud;
	}
	public String getIdSexo() {
		return idSexo;
	}
	public void setIdSexo(String idSexo) {
		this.idSexo = idSexo;
	}
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public String getPeriodicidadPago() {
		return periodicidadPago;
	}
	public void setPeriodicidadPago(String periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
	}
	public String getCobertura() {
		return cobertura;
	}
	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}
	public String getBeneficiario() {
		return beneficiario;
	}
	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}
	public String getAsistenciaSanitaria() {
		return asistenciaSanitaria;
	}
	public void setAsistenciaSanitaria(String asistenciaSanitaria) {
		this.asistenciaSanitaria = asistenciaSanitaria;
	}
	public String getCuotaCobertura() {
		return cuotaCobertura;
	}
	public void setCuotaCobertura(String cuotaCobertura) {
		this.cuotaCobertura = cuotaCobertura;
	}
	public String getCapitalCobertura() {
		return capitalCobertura;
	}
	public void setCapitalCobertura(String capitalCobertura) {
		this.capitalCobertura = capitalCobertura;
	}
	public Long getIdSolicitudAceptada() {
		return idSolicitudAceptada;
	}
	public void setIdSolicitudAceptada(Long idSolicitudAceptada) {
		this.idSolicitudAceptada = idSolicitudAceptada;
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
	public String getEstadoMutualista() {
		return estadoMutualista;
	}
	public void setEstadoMutualista(String estadoMutualista) {
		this.estadoMutualista = estadoMutualista;
	}
	public void setPDF(String rutaPDF) {
		this.pdf = rutaPDF;
	}
	
	
	
}