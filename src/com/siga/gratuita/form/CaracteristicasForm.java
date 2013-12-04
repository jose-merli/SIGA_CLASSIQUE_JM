package com.siga.gratuita.form;

import java.util.List;

import com.siga.beans.ScsJuzgadoBean;
import com.siga.general.MasterForm;

public class CaracteristicasForm extends MasterForm
{
	/**
	 *  Variables 
	 * 
	 * */ 	
	private String		idInstitucion;
	private String		anio;
	private String		numero;
	private String		idInstitucionJuzgado;		
	private String		idOrigenContacto;
	private	String		descripcionContacto;
	private String		otroDescripcionOrigenContacto;
	private String		contraLibertadSexual;
	private String		victimaMenorAbusoMaltrato;
	private String		judicial;
	private String		civil;
	private String		penal;
	private String		interposicionDenuncia;
	private String		solicitudMedidasCautelares;
	private String		asistenciaDeclaracion;
	private String		medidasProvisionales;
	private String		ordenProteccion;
	private String		otras;
	private	String		otrasDescripcion;
	private String		asesoramiento;	
	private String		derivaActuacionesJudiciales;
	private String		interposicionMinistFiscal;
	private String		intervencionMedicoForense;
	private String		derechosJusticiaGratuita;
	private String		obligadaDesalojoDomicilio;
	private String		entrevistaLetradoDemandante;
	private	String		letradoDesignadoContiActuJudi;
	private	String		civilesPenales;
	private	String		victimaLetradoAnterioridad;
	private	String		idPersona;
	private String		numeroProcedimiento;
	private	String		idJuzgado;
	private	String		descripcionJuzgado;
	private	String		nig;	
	private String		idPretension;
	private	String		descripcionPretension;
	private String		colegiado;
	private String 		nombrePersona;
	private String 		violenciaGenero;
	private String 		violenciaDomestica;
	
	private String colegiadoNumero;
	private String colegiadoNombre;
	
	private String 		nombre;
	private String 		apellido1;
	private String 		apellido2;
	
	private List<ScsJuzgadoBean> juzgados; 
	
	public List<ScsJuzgadoBean> getJuzgados() {
		return juzgados;
	}
	public void setJuzgados(List<ScsJuzgadoBean> juzgados) {
		this.juzgados = juzgados;
	}
	
	
	public String getColegiadoNumero() {
		return colegiadoNumero;
	}
	public void setColegiadoNumero(String colegiadoNumero) {
		this.colegiadoNumero = colegiadoNumero;
	}
	public String getColegiadoNombre() {
		return colegiadoNombre;
	}
	public void setColegiadoNombre(String colegiadoNombre) {
		this.colegiadoNombre = colegiadoNombre;
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
	public String getNombrePersona() {
		return nombrePersona;
	}
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
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
	public String getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	public void setIdInstitucionJuzgado(String idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	public String getIdOrigenContacto() {
		return idOrigenContacto;
	}
	public void setIdOrigenContacto(String idOrigenContacto) {
		this.idOrigenContacto = idOrigenContacto;
	}
	public String getOtroDescripcionOrigenContacto() {
		return otroDescripcionOrigenContacto;
	}
	public void setOtroDescripcionOrigenContacto(
			String otroDescripcionOrigenContacto) {
		this.otroDescripcionOrigenContacto = otroDescripcionOrigenContacto;
	}
	public String getContraLibertadSexual() {
		return contraLibertadSexual;
	}
	public String getVictimaMenorAbusoMaltrato() {
		return victimaMenorAbusoMaltrato;
	}	
	public void setContraLibertadSexual(String contraLibertadSexual) {
		this.contraLibertadSexual = contraLibertadSexual;
	}
	public void setVictimaMenorAbusoMaltrato(String victimaMenorAbusoMaltrato) {
		this.victimaMenorAbusoMaltrato = victimaMenorAbusoMaltrato;
	}	
	public String getCivil() {
		return civil;
	}
	public void setCivil(String civil) {
		this.civil = civil;
	}
	public String getPenal() {
		return penal;
	}
	public void setPenal(String penal) {
		this.penal = penal;
	}
	public String getInterposicionDenuncia() {
		return interposicionDenuncia;
	}
	public void setInterposicionDenuncia(String interposicionDenuncia) {
		this.interposicionDenuncia = interposicionDenuncia;
	}
	public String getSolicitudMedidasCautelares() {
		return solicitudMedidasCautelares;
	}
	public void setSolicitudMedidasCautelares(String solicitudMedidasCautelares) {
		this.solicitudMedidasCautelares = solicitudMedidasCautelares;
	}
	public String getAsistenciaDeclaracion() {
		return asistenciaDeclaracion;
	}
	public void setAsistenciaDeclaracion(String asistenciaDeclaracion) {
		this.asistenciaDeclaracion = asistenciaDeclaracion;
	}
	public String getMedidasProvisionales() {
		return medidasProvisionales;
	}
	public void setMedidasProvisionales(String medidasProvisionales) {
		this.medidasProvisionales = medidasProvisionales;
	}
	public String getOrdenProteccion() {
		return ordenProteccion;
	}
	public void setOrdenProteccion(String ordenProteccion) {
		this.ordenProteccion = ordenProteccion;
	}
	public String getOtras() {
		return otras;
	}
	public void setOtras(String otras) {
		this.otras = otras;
	}
	public String getOtrasDescripcion() {
		return otrasDescripcion;
	}
	public void setOtrasDescripcion(String otrasDescripcion) {
		this.otrasDescripcion = otrasDescripcion;
	}
	public String getDerivaActuacionesJudiciales() {
		return derivaActuacionesJudiciales;
	}
	public void setDerivaActuacionesJudiciales(String derivaActuacionesJudiciales) {
		this.derivaActuacionesJudiciales = derivaActuacionesJudiciales;
	}
	public String getInterposicionMinistFiscal() {
		return interposicionMinistFiscal;
	}
	public void setInterposicionMinistFiscal(String interposicionMinistFiscal) {
		this.interposicionMinistFiscal = interposicionMinistFiscal;
	}
	public String getIntervencionMedicoForense() {
		return intervencionMedicoForense;
	}
	public void setIntervencionMedicoForense(String intervencionMedicoForense) {
		this.intervencionMedicoForense = intervencionMedicoForense;
	}
	public String getDerechosJusticiaGratuita() {
		return derechosJusticiaGratuita;
	}
	public void setDerechosJusticiaGratuita(String derechosJusticiaGratuita) {
		this.derechosJusticiaGratuita = derechosJusticiaGratuita;
	}
	public String getObligadaDesalojoDomicilio() {
		return obligadaDesalojoDomicilio;
	}
	public void setObligadaDesalojoDomicilio(String obligadaDesalojoDomicilio) {
		this.obligadaDesalojoDomicilio = obligadaDesalojoDomicilio;
	}
	public String getEntrevistaLetradoDemandante() {
		return entrevistaLetradoDemandante;
	}
	public void setEntrevistaLetradoDemandante(String entrevistaLetradoDemandante) {
		this.entrevistaLetradoDemandante = entrevistaLetradoDemandante;
	}
	public String getLetradoDesignadoContiActuJudi() {
		return letradoDesignadoContiActuJudi;
	}
	public void setLetradoDesignadoContiActuJudi(
			String letradoDesignadoContiActuJudi) {
		this.letradoDesignadoContiActuJudi = letradoDesignadoContiActuJudi;
	}
	public String getCivilesPenales() {
		return civilesPenales;
	}
	public void setCivilesPenales(String civilesPenales) {
		this.civilesPenales = civilesPenales;
	}
	public String getVictimaLetradoAnterioridad() {
		return victimaLetradoAnterioridad;
	}
	public void setVictimaLetradoAnterioridad(String victimaLetradoAnterioridad) {
		this.victimaLetradoAnterioridad = victimaLetradoAnterioridad;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	public String getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public String getNig() {
		return nig;
	}
	public void setNig(String nig) {
		this.nig = nig;
	}
	public String getIdPretension() {
		return idPretension;
	}
	public void setIdPretension(String idPretension) {
		this.idPretension = idPretension;
	}
	
	public String getJudicial() {
		return judicial;
	}
	public void setJudicial(String judicial) {
		this.judicial = judicial;
	}
	public String getAsesoramiento() {
		return asesoramiento;
	}
	public void setAsesoramiento(String asesoramiento) {
		this.asesoramiento = asesoramiento;
	}
	
	public String getColegiado() {
		return colegiado;
	}
	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}
	public String getDescripcionContacto() {
		return descripcionContacto;
	}
	public void setDescripcionContacto(String descripcionContacto) {
		this.descripcionContacto = descripcionContacto;
	}
	public String getDescripcionJuzgado() {
		return descripcionJuzgado;
	}
	public void setDescripcionJuzgado(String descripcionJuzgado) {
		this.descripcionJuzgado = descripcionJuzgado;
	}
	public String getDescripcionPretension() {
		return descripcionPretension;
	}
	public void setDescripcionPretension(String descripcionPretension) {
		this.descripcionPretension = descripcionPretension;
	}
	public String getViolenciaGenero() {
		return violenciaGenero;
	}
	public void setViolenciaGenero(String violenciaGenero) {
		this.violenciaGenero = violenciaGenero;
	}
	public String getViolenciaDomestica() {
		return violenciaDomestica;
	}
	public void setViolenciaDomestica(String violenciaDomestica) {
		this.violenciaDomestica = violenciaDomestica;
	}
	
}
