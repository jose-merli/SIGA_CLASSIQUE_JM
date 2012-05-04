package com.siga.censo.form;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.atos.utils.ClsConstants;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenSolicitudAlterMutuaBean;
import com.siga.general.MasterForm;
import com.siga.ws.alterMutua.WSAsegurado;
import com.siga.ws.alterMutua.WSDireccion;
import com.siga.ws.alterMutua.WSPersona;
import com.siga.ws.alterMutua.WSAsegurado;
import com.siga.ws.alterMutua.WSPropuesta;

/**
 * 
 * @author josebd
 * 
 */
public class AlterMutuaForm extends MasterForm {

	private boolean error;
	private boolean publicidad;
	private boolean quiereCertificado;
	private String idSolicitud;
	
	private String apellidos;
	private String codigoPostal;
	private String comunicacionPreferente;
	private String correoElectronico;
	
	private String cboCodigo;
	private String codigoSucursal;
	private String numeroCuenta;
	private String digitoControl;
	
	private String codigoInstitucion;
	private String domicilio;
	private String idTipoEjercicio;
	private String email;
	private String idEstadoCivil;
	private String estadoCivil;
	private String fax;
	private String fechaNacimiento;
	private String identificador;
	private String idInstitucion;
	private String idioma;
	private String idPais;
	private String idPaisCuenta;
	private String tipoDireccion;
	private String idPoblacion;
	private String idProvincia;
	private String movil;
	private String msgRespuesta;
	private String nombre;
	private String observaciones;
	private String parentesco;
	private String poblacion;
	private String sexo;
	private String telefono1;
	private String telefono2;
	private String tipoEjercicio;
	private String tipoIdentificacion;
	private String idTipoIdentificacion;
	private String titular;
	private String idPaquete;
	private String iban;
	private String swift;
	private String tarifaPropuesta;
	private String propuestasSel;
	private String colegio;
	private String idPersona;


	private String brevePropuesta;
	private boolean requiereFamiliares;
	private boolean requiereBeneficiarios;
	
	private WSAsegurado asegurado;
	private WSDireccion direccion;
	private String herederos;
	private String familiares;
	private int tipoComunicacion;
	private int tipoIdentificador;
	private int propuesta;
	private WSPropuesta[] propuestas;
	
	private String nombreWSPersona;
    private String apellidosWSPersona;
    private String tipoIdentificadorWSPersona;
    private String identificadorWSPersona;
    private String fechaNacimientoWSPersona;
    private int parentescoWSPersona;
	private int sexoWSPersona;
	
	private ArrayList tiposDireccion;
	private ArrayList idiomas;
	private ArrayList sexos;
	private ArrayList tiposComunicacion;
	private ArrayList tiposIdentificacion;
	private ArrayList tiposParentesco;
	private ArrayList paises;
	private ArrayList provincias;
	private String provincia;
	private String pais;
	private String idSolicitudalter;
	private String idPropuesta;
	private ArrayList estadosCiviles;
	private String idSexo;
	private String selectBeneficiarios;
	private int numeroPropuestas;
	
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public boolean getAdmitePublicidad() {
		return publicidad;
	}
	public void setAdmitePublicidad(boolean publicidad) {
		this.publicidad = publicidad;
	}
	public boolean quiereCertificado() {
		return quiereCertificado;
	}
	public void setQuiereCertificado(boolean quiereCertificado) {
		this.quiereCertificado = quiereCertificado;
	}
	public String getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
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
	public String getComunicacionPreferente() {
		return comunicacionPreferente;
	}
	public void setComunicacionPreferente(String comunicacionPreferente) {
		this.comunicacionPreferente = comunicacionPreferente;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String cuentaBancaria) {
		this.numeroCuenta = cuentaBancaria;
	}
	public String getDigitoControl() {
		return digitoControl;
	}
	public void setDigitoControl(String digitoControl) {
		this.digitoControl = digitoControl;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getIdTipoEjercicio() {
		return idTipoEjercicio;
	}
	public void setIdTipoEjercicio(String idTipoEjercicio) {
		this.idTipoEjercicio = idTipoEjercicio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getIdPais() {
		return idPais;
	}
	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}
	public String getIdPaisCuenta() {
		return idPaisCuenta;
	}
	public void setIdPaisCuenta(String idPais) {
		this.idPaisCuenta = idPais;
	}
	public String getTipoDireccion() {
		return tipoDireccion;
	}
	public void setTipoDireccion(String tDir) {
		this.tipoDireccion = tDir;
	}
	public String getIdPoblacion() {
		return idPoblacion;
	}
	public void setIdPoblacion(String idPoblacion) {
		this.idPoblacion = idPoblacion;
	}
	public String getIdProvincia() {
		return idProvincia;
	}
	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}
	public String getMovil() {
		return movil;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	public String getMsgRespuesta() {
		return msgRespuesta;
	}
	public void setMsgRespuesta(String msgRespuesta) {
		this.msgRespuesta = msgRespuesta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getTelefono1() {
		return telefono1;
	}
	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}
	public String getTelefono2() {
		return telefono2;
	}
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	public String getTipoEjercicio() {
		return tipoEjercicio;
	}
	public void setTipoEjercicio(String tipoEjercicio) {
		this.tipoEjercicio = tipoEjercicio;
	}
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public WSAsegurado getAsegurado() {
		return asegurado;
	}
	public void setAsegurado(WSAsegurado asegurado) {
		this.asegurado = asegurado;
	}
	public String getColegio() {
		return colegio;
	}
	public void setColegio(String colegio) {
		this.colegio = colegio;
	}
	public WSDireccion getDireccion() {
		return direccion;
	}
	public void setDireccion(WSDireccion direccion) {
		this.direccion = direccion;
	}
	public String getHerederos() {
		return herederos;
	}
	public void setHerederos(String herederos) {
		this.herederos = herederos;
	}
	public int getTipoComunicacion() {
		return tipoComunicacion;
	}
	public void setTipoComunicacion(int tipoComunicacion) {
		this.tipoComunicacion = tipoComunicacion;
	}
	public int getTipoIdentificador() {
		return tipoIdentificador;
	}
	public void setTipoIdentificador(int tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}
	public int getPropuesta() {
		return propuesta;
	}
	public void setPropuesta(int propuesta) {
		this.propuesta = propuesta;
	}
	public WSPropuesta[] getPropuestas() {
		return propuestas;
	}
	public void setPropuestas(WSPropuesta[] propuestas) {
		this.propuestas = propuestas;
	}
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
	public int getSexoWSPersona() {
		return sexoWSPersona;
	}
	public void setSexoWSPersona(int sexoWSPersona) {
		this.sexoWSPersona = sexoWSPersona;
	}
	public String getFechaNacimientoWSPersona() {
		return fechaNacimientoWSPersona;
	}
	public void setFechaNacimientoWSPersona(String fechaNacimientoWSPersona) {
		this.fechaNacimientoWSPersona = fechaNacimientoWSPersona;
	}
	public boolean isPublicidad() {
		return publicidad;
	}
	public boolean isQuiereCertificado() {
		return quiereCertificado;
	}
	public void setTiposDireccion(ArrayList tiposDireccionList) {
		this.tiposDireccion = tiposDireccionList;
	}
	public ArrayList getTiposDireccion(){
		return this.tiposDireccion;
	}
	public void setIdiomas(ArrayList idiomasList) {
		this.idiomas = idiomasList;
	}
	public ArrayList getIdiomas(){
		return this.idiomas;
	}
	public void setTiposComunicacion(ArrayList list) {
		this.tiposComunicacion = list;
	}
	public ArrayList getTiposComunicacion(){
		return this.tiposComunicacion;
	}
	public ArrayList getSexos() {
		return sexos;
	}
	public void setSexos(ArrayList sexos) {
		this.sexos = sexos;
	}
	public ArrayList getTiposIdentificacion() {
		return tiposIdentificacion;
	}
	public void setTiposIdentificacion(ArrayList tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}
	public ArrayList getTiposParentesco() {
		return tiposParentesco;
	}
	public void setTiposParentesco(ArrayList tiposParentesco) {
		this.tiposParentesco = tiposParentesco;
	}
	
	// Para el heredero
	public String getNombreWSPersona() {
		return nombreWSPersona;
	}
	public void setNombreWSPersona(String nombreWSPersona) {
		this.nombreWSPersona = nombreWSPersona;
	}
	public String getApellidosWSPersona() {
		return apellidosWSPersona;
	}
	public void setApellidosWSPersona(String apellidosWSPersona) {
		this.apellidosWSPersona = apellidosWSPersona;
	}
	public String getTipoIdentificadorWSPersona() {
		return tipoIdentificadorWSPersona;
	}
	public void setTipoIdentificadorWSPersona(String tipoIdentificadorWSPersona) {
		this.tipoIdentificadorWSPersona = tipoIdentificadorWSPersona;
	}
	public String getIdentificadorWSPersona() {
		return identificadorWSPersona;
	}
	public void setIdentificadorWSPersona(String identificadorWSPersona) {
		this.identificadorWSPersona = identificadorWSPersona;
	}
	public String getIdPaquete() {
		return idPaquete;
	}
	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}
	public boolean isRequiereFamiliares() {
		return requiereFamiliares;
	}
	public void setRequiereFamiliares(boolean requiereFamiliares) {
		this.requiereFamiliares = requiereFamiliares;
	}
	public boolean isRequiereBeneficiarios() {
		return requiereBeneficiarios;
	}
	public void setRequiereBeneficiarios(boolean requiereBeneficiarios) {
		this.requiereBeneficiarios = requiereBeneficiarios;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public void setPaises(ArrayList paises) {
		this.paises = paises;
	}
	public ArrayList getPaises() {
		return paises;
	}
	public void setProvincias(ArrayList provincias) {
		this.provincias = provincias;
	}
	public ArrayList getProvincias() {
		return provincias;
	}
	public void setEstadosCiviles(ArrayList estadoCivilList) {
		estadosCiviles = estadoCivilList;
	}
	public ArrayList getEstadosCiviles() {
		return estadosCiviles;
	}		
	public String getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}
	public void setIdTipoIdentificacion(String idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}
	public String getTarifaPropuesta() {
		return tarifaPropuesta;
	}
	public void setTarifaPropuesta(String tarifaPropuesta) {
		this.tarifaPropuesta = tarifaPropuesta;
	}
	public String getBrevePropuesta() {
		return brevePropuesta;
	}
	public void setBrevePropuesta(String brevePropuesta) {
		this.brevePropuesta = brevePropuesta;
	}
	public String getFamiliares() {
		return familiares;
	}
	public void setFamiliares(String familiares) {
		this.familiares = familiares;
	}
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	public String getIdEstadoCivil() {
		return idEstadoCivil;
	}
	public void setIdEstadoCivil(String idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
		
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public void setIdSolicitudAlter(String idSolicitudalter) {
		this.idSolicitudalter = idSolicitudalter;
	}
	public int getParentescoWSPersona() {
		return parentescoWSPersona;
	}
	public void setParentescoWSPersona(int parentescoWSPersona) {
		this.parentescoWSPersona = parentescoWSPersona;
	}
	public String getIdSolicitudalter() {
		return idSolicitudalter;
	}
	public void setIdSolicitudalter(String idSolicitudalter) {
		this.idSolicitudalter = idSolicitudalter;
	}
	public String getProvincia() {
		return provincia;
	}
	public String getPais() {
		return pais;
	}
	public CenSolicitudAlterMutuaBean getSolicitudAlterMutuaBean(){
		CenSolicitudAlterMutuaBean bean = new CenSolicitudAlterMutuaBean();
		bean.setApellidos(this.apellidos);              
		bean.setCodigoPostal(this.codigoPostal);           
		bean.setCorreoElectronico(this.correoElectronico);      
		bean.setDomicilio(this.domicilio);              
		bean.setFax(this.fax);     
		bean.setIdEstadoCivil(this.idEstadoCivil);          
		bean.setIdInstitucion(this.idInstitucion);          
		bean.setIdPais(this.idPais);                 
		bean.setIdProvincia(this.idProvincia);            
		bean.setIdSolicitudAlter(this.idSolicitudalter);
		if(this.idSolicitud!=null && !this.idSolicitud.equalsIgnoreCase("")){
			bean.setIdSolicitud(this.idSolicitud);
		}else if (this.idPersona!=null){
			bean.setIdSolicitud(this.idPersona);
			bean.setIdPersona(this.idPersona);
		}
		bean.setIdTipoIdentificacion(this.idTipoIdentificacion);   
		bean.setMovil(this.movil);                  
		bean.setNombre(this.nombre);                 
		bean.setIdentificador(this.identificador);    
		bean.setTelefono1(this.telefono1);              
		bean.setTelefono2(this.telefono2);              
		bean.setIdSexo(this.idSexo);                 
		bean.setCboCodigo(this.cboCodigo);              
		bean.setCodigoSucursal(this.codigoSucursal);         
		bean.setDigitoControl(this.digitoControl);          
		bean.setNumeroCuenta(this.numeroCuenta);           
		bean.setIban(this.iban);                   
		bean.setPais(this.idPaisCuenta);                   
		bean.setProvincia(this.provincia);              
		bean.setPoblacion(this.poblacion);              
		bean.setIdPaquete(this.idPaquete);              
		bean.setIdTipoEjercicio(this.idTipoEjercicio);              
		try {
			bean.setPropuesta(String.valueOf(this.propuesta));              
			bean.setFechaNacimiento(UtilidadesString.formatoFecha(this.fechaNacimiento,ClsConstants.DATE_FORMAT_SHORT_SPANISH, ClsConstants.DATE_FORMAT_JAVA));        
		} catch (Exception e) {
			// Si da error no se almacena la fecha de nacimiento
		}
		return bean;
	}

	public String getPropuestasSel() {
		return propuestasSel;
	}
	public void setPropuestasSel(String propuestasSel) {
		this.propuestasSel = propuestasSel;
	}
	public ArrayList getPaquetesSeleccionados() {
		ArrayList paquetes = new ArrayList();
		if(idPaquete!=null && !idPaquete.equalsIgnoreCase("")){
			paquetes.add(idPaquete);
		}
		if(propuestasSel!=null){
			String[] pSel = propuestasSel.split("-");
			for (int i = 0; i < pSel.length; i++) {
				paquetes.add(pSel[i]);
			}
		}
		return paquetes;
	}
	
	public String getIdSexo() {
		return idSexo;
	}
	public void setIdSexo(String idSexo) {
		this.idSexo = idSexo;
	}
	public String getSelectBeneficiarios() {
		return selectBeneficiarios;
	}
	public void setSelectBeneficiarios(String selectBeneficiarios) {
		this.selectBeneficiarios = selectBeneficiarios;
	}
	public String getIdPropuesta() {
		return idPropuesta;
	}
	public void setIdPropuesta(String idPropuesta) {
		this.idPropuesta = idPropuesta;
	}
	public void setNumeroPropuestas(int length) {
		this.numeroPropuestas = length;
	}
	public int getNumeroPropuestas() {
		return this.numeroPropuestas;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	
	
}