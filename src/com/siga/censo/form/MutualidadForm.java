package com.siga.censo.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.CenSolicitudMutualidadBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterForm;
/**
 * 
 * @author jorgeta
 */
 public class MutualidadForm extends MasterForm {
 	
	 
	private String idSolicitud;
	private String idSolicitudAceptada;
	private String idTipoSolicitud;
	private String idInstitucion;

	private String tipoIdentificacion;
	private String idTipoIdentificacion;
	private String numeroIdentificacion;
	private String idSexo;
	private String sexo;
	private String tratamiento;
	private String idTratamiento;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String nacionalidad;
	private String fechaNacimiento;
	private String estadoCivil;
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
	private String idBanco;
	
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
	
	private String swift;
	private String periodicidadPago;
	private String cobertura;
	private String beneficiario;
	private String asistenciaSanitaria;
	
	private String origenSolicitud;
	
	
	List<ValueKeyVO> periodicidadesPago;
	List<ValueKeyVO> opcionesCobertura;
	List<ValueKeyVO> beneficiarios;
	List<ValueKeyVO> asistenciasSanitarias;
	
//	Map<String, String> periodicidadesPago;
//	Map<String, String> opcionesCobertura;
//	Map<String, String> beneficiarios;
//	Map<String, String> asistenciasSanitarias;
	
	
	
	private String fechaNacimientoConyuge;
	private String numeroHijos;
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
	private String estado;
	
	private String estadoMutualista;
	
	private String idEstado;
	private String fechaSolicitud;
	private String idSolicitudIncorporacion;
	private String idPersona;
	
	private String cuotaCobertura;
	private String capitalCobertura;
	
	private String pais;
	private String provincia;
	private String poblacion;
	
	List<CenPoblacionesBean> poblaciones;
	List<CenProvinciaBean> provincias;
	private String pdf;
	
	public String getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getTratamiento() {
		return tratamiento;
	}
	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
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
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
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
	
	public String getFechaNacimientoConyuge() {
		return fechaNacimientoConyuge;
	}
	public void setFechaNacimientoConyuge(String fechaNacimientoConyuge) {
		this.fechaNacimientoConyuge = fechaNacimientoConyuge;
	}
	public String getNumeroHijos() {
		return numeroHijos;
	}
	public void setNumeroHijos(String numeroHijos) {
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
	public String getPoblacionExtranjera() {
		return poblacionExtranjera;
	}
	public void setPoblacionExtranjera(String poblacionExtranjera) {
		this.poblacionExtranjera = poblacionExtranjera;
	}
	public String getCboCodigo() {
		return cboCodigo;
	}
	public void setCboCodigo(String cboCodigo) {
		this.cboCodigo = cboCodigo;
	}
	public String getIdBanco() {
		return idBanco;
	}
	public void setIdBanco(String idBanco) {
		this.idBanco = idBanco;
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
	public String getIdBeneficiario() {
		return idBeneficiario;
	}
	public void setIdBeneficiario(String idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
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
	public String getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}
	public void setIdTipoIdentificacion(String idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}
	public String getIdSexo() {
		return idSexo;
	}
	public void setIdSexo(String idSexo) {
		this.idSexo = idSexo;
	}
	public String getIdTratamiento() {
		return idTratamiento;
	}
	public void setIdTratamiento(String idTratamiento) {
		this.idTratamiento = idTratamiento;
	}
	public String getIdEstadoCivil() {
		return idEstadoCivil;
	}
	public void setIdEstadoCivil(String idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}
	public String getIdTipoSolicitud() {
		return idTipoSolicitud;
	}
	public void setIdTipoSolicitud(String idTipoSolicitud) {
		this.idTipoSolicitud = idTipoSolicitud;
	}
	public String getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public String getIdSolicitudIncorporacion() {
		return idSolicitudIncorporacion;
	}
	public void setIdSolicitudIncorporacion(String idSolicitudIncorporacion) {
		this.idSolicitudIncorporacion = idSolicitudIncorporacion;
	}
	public CenSolicitudMutualidadBean getMutualidadBean() {
		CenSolicitudMutualidadBean solicitudMutualidadBean = new CenSolicitudMutualidadBean();

		solicitudMutualidadBean.setApellido1(this.apellido1);
		solicitudMutualidadBean.setApellido2(this.apellido2);
		solicitudMutualidadBean.setCodigoPostal(this.codigoPostal);
		solicitudMutualidadBean.setCorreoElectronico(this.correoElectronico);
		solicitudMutualidadBean.setDomicilio(this.domicilio);
		solicitudMutualidadBean.setFax1(this.fax1);
		solicitudMutualidadBean.setFax2(this.fax2);



		solicitudMutualidadBean.setIdEstadoCivil(this.idEstadoCivil);
		if(idInstitucion!=null&& !idInstitucion.equals(""))
			solicitudMutualidadBean.setIdInstitucion(new Integer(this.idInstitucion));
		if(idSolicitudAceptada!=null && !idSolicitudAceptada.equals(""))
			solicitudMutualidadBean.setIdSolicitudAceptada(new Long(idSolicitudAceptada));
		
		solicitudMutualidadBean.setIdPais(this.idPais);
		solicitudMutualidadBean.setIdPoblacion(this.idPoblacion);
		solicitudMutualidadBean.setPoblacionExtranjera(this.poblacionExtranjera);
		solicitudMutualidadBean.setIdProvincia(this.idProvincia);
		if(idSolicitud!=null&& !idSolicitud.equals(""))
			solicitudMutualidadBean.setIdSolicitud(new Long(this.idSolicitud));
		if(idSolicitudIncorporacion!=null&& !idSolicitudIncorporacion.equals(""))
			solicitudMutualidadBean.setIdSolicitudIncorporacion(new Long(this.idSolicitudIncorporacion));
		if(idEstado!=null&& !idEstado.equals(""))
			solicitudMutualidadBean.setIdEstado(new Integer(idEstado));
		solicitudMutualidadBean.setEstado(estado);
		solicitudMutualidadBean.setEstadoMutualista(this.estadoMutualista);
		
		if(idTipoIdentificacion!=null&& !idTipoIdentificacion.equals(""))
			solicitudMutualidadBean.setIdTipoIdentificacion(new Integer(this.idTipoIdentificacion));
		solicitudMutualidadBean.setIdTipoSolicitud(this.idTipoSolicitud);
		if(idTratamiento!=null&& !idTratamiento.equals(""))
			solicitudMutualidadBean.setIdTratamiento(new Integer(this.idTratamiento));
		solicitudMutualidadBean.setMovil(this.movil);
		solicitudMutualidadBean.setNaturalDe(this.nacionalidad);
		solicitudMutualidadBean.setNombre(this.nombre);
		solicitudMutualidadBean.setNumeroIdentificacion(this.numeroIdentificacion);
		solicitudMutualidadBean.setTelef1(this.telef1);
		solicitudMutualidadBean.setTelef2(this.telef2);
		solicitudMutualidadBean.setIdSexo(this.idSexo);

		solicitudMutualidadBean.setCboCodigo(this.cboCodigo);
		solicitudMutualidadBean.setCodigoSucursal(this.codigoSucursal);
		solicitudMutualidadBean.setDigitoControl(this.digitoControl);
		solicitudMutualidadBean.setNumeroCuenta(this.numeroCuenta);
		solicitudMutualidadBean.setTitular(this.titular);
		solicitudMutualidadBean.setIban(this.iban);
		solicitudMutualidadBean.setIdPeriodicidadPago(this.idPeriodicidadPago);
		solicitudMutualidadBean.setIdCobertura(this.idCobertura);
		solicitudMutualidadBean.setIdBeneficiario(this.idBeneficiario);
		solicitudMutualidadBean.setOtrosBeneficiarios(this.otrosBeneficiarios);
		solicitudMutualidadBean.setIdAsistenciaSanitaria(this.idAsistenciaSanitaria);
		
		solicitudMutualidadBean.setSwift(this.swift);
		solicitudMutualidadBean.setPeriodicidadPago(this.periodicidadPago);
		solicitudMutualidadBean.setCobertura(this.cobertura);
		solicitudMutualidadBean.setAsistenciaSanitaria(this.asistenciaSanitaria);
		solicitudMutualidadBean.setBeneficiario(this.beneficiario);
		solicitudMutualidadBean.setCapitalCobertura(this.capitalCobertura);
		solicitudMutualidadBean.setCuotaCobertura(this.cuotaCobertura);
		
		solicitudMutualidadBean.setPais(this.pais);
		solicitudMutualidadBean.setProvincia(this.provincia);
		solicitudMutualidadBean.setPoblacion(this.poblacion);
		solicitudMutualidadBean.setEstado(this.estado);
		
		
		
		
	
		
		if(numeroHijos!=null&&!numeroHijos.equals(""))
			solicitudMutualidadBean.setNumeroHijos(new Integer(this.numeroHijos));

		try {
			solicitudMutualidadBean.setFechaEstado(GstDate.getApplicationFormatDate("", this.fechaEstado));
			solicitudMutualidadBean.setFechaNacimiento(GstDate.getApplicationFormatDate("",this.fechaNacimiento));
			if(this.fechaSolicitud!=null && !this.fechaSolicitud.equals("SYSDATE"))
				solicitudMutualidadBean.setFechaSolicitud(GstDate.getApplicationFormatDate("",this.fechaSolicitud));
			solicitudMutualidadBean.setFechaNacimientoConyuge(GstDate.getApplicationFormatDate("",this.fechaNacimientoConyuge));
			solicitudMutualidadBean.setEdadHijo1(this.edadHijo1);
			solicitudMutualidadBean.setEdadHijo2(this.edadHijo2);
			solicitudMutualidadBean.setEdadHijo3(this.edadHijo3);
			solicitudMutualidadBean.setEdadHijo4(this.edadHijo4);
			solicitudMutualidadBean.setEdadHijo5(this.edadHijo5);
			solicitudMutualidadBean.setEdadHijo6(this.edadHijo6);
			solicitudMutualidadBean.setEdadHijo7(this.edadHijo7);
			solicitudMutualidadBean.setEdadHijo8(this.edadHijo8);
			solicitudMutualidadBean.setEdadHijo9(this.edadHijo9);
			solicitudMutualidadBean.setEdadHijo10(this.edadHijo10);

		} catch (Exception e) {
		}
		return solicitudMutualidadBean;
		
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
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
	public List<ValueKeyVO> getPeriodicidadesPago() {
		return periodicidadesPago;
	}
	public void setPeriodicidadesPago(List<ValueKeyVO> list) {
		this.periodicidadesPago = list;
	}
	public List<ValueKeyVO> getOpcionesCobertura() {
		return opcionesCobertura;
	}
	public void setOpcionesCobertura(List<ValueKeyVO> list) {
		this.opcionesCobertura = list;
	}
	public List<ValueKeyVO> getBeneficiarios() {
		return beneficiarios;
	}
	public void setBeneficiarios(List<ValueKeyVO> list) {
		this.beneficiarios = list;
	}
	public List<ValueKeyVO> getAsistenciasSanitarias() {
		return asistenciasSanitarias;
	}
	public void setAsistenciasSanitarias(List<ValueKeyVO> list) {
		this.asistenciasSanitarias = list;
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
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public String getIdSolicitudAceptada() {
		return idSolicitudAceptada;
	}
	public void setIdSolicitudAceptada(String idSolicitudAceptada) {
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
	public List<CenPoblacionesBean> getPoblaciones() {
		return poblaciones;
	}
	public void setPoblaciones(List<CenPoblacionesBean> poblaciones) {
		this.poblaciones = poblaciones;
	}
	public List<CenProvinciaBean> getProvincias() {
		return provincias;
	}
	public void setProvincias(List<CenProvinciaBean> provincias) {
		this.provincias = provincias;
	}
	public String getEstadoMutualista() {
		return estadoMutualista;
	}
	public void setEstadoMutualista(String estadoMutualista) {
		this.estadoMutualista = estadoMutualista;
	}
	public String getPDF(){
		return this.pdf;
	}
	public void setPDF(String pdf) {
		this.pdf = pdf;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getOrigenSolicitud() {
		return origenSolicitud;
	}
	public void setOrigenSolicitud(String origenSolicitud) {
		this.origenSolicitud = origenSolicitud;
	}
	
	
	
}