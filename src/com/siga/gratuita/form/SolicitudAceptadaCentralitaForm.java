package com.siga.gratuita.form;


import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;
/***
 * 
 * @author jorgeta 
 * @date   28/11/2014
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class SolicitudAceptadaCentralitaForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Campos para la tabla de turnos
	String idInstitucion = null;
	String idPersona = null;
	String idTurno = null;
	String idGuardia = null;
	String nombreGuardia = null;
	String idEstado;
	String descripcionEstado;
	String nombreCentroDetencion;
	String idCentroDetencion;
	String descripcionColegiado;
	String colegiadoNumero;
	String colegiadoNombre;
	String fechaDesde;
	String fechaHasta;
	String fechaRecepcion;
	String solicitanteIdTipoPersona;
	String solicitanteIdTipoIdentificacion;
	String solicitanteNumeroIdentificacion;
	String solicitanteNombre;
	String solicitanteApellido1;
	String solicitanteApellido2;
	String solicitanteTipoVia;
	String solicitanteDireccion;
	String solicitantePoblacion;
	String solicitanteProvincia;
	String solicitanteNumero;
	String solicitanteEscalera;
	String solicitantePiso;
	String solicitantePuerta;
	String solicitanteCodPostal;
	String solicitanteCorreoElectronico;
	String solicitanteTelefono;
	String solicitanteFax;
	String solicitantePoblacionExt;
	String solicitantePais;
	String sexo;
	
	String idTipoAsistenciaColegio;
	
	String solicitanteDescripcion;
	String asistenciaAnio;
	String asistenciaNumero;
	String asistenciaDescripcion;
	
	String fechaGuardia;
	
	String fechaLlamadaHoras;
	
	String numAvisoCV;
	
	
	/**
	 * @return the fechaLlamadaHoras
	 */
	public String getFechaLlamadaHoras() {
		return fechaLlamadaHoras;
	}


	/**
	 * @param fechaLlamadaHoras the fechaLlamadaHoras to set
	 */
	public void setFechaLlamadaHoras(String fechaLlamadaHoras) {
		this.fechaLlamadaHoras = fechaLlamadaHoras;
	}


	/**
	 * @return the fechaGuardia
	 */
	public String getFechaGuardia() {
		return fechaGuardia;
	}


	/**
	 * @param fechaGuardia the fechaGuardia to set
	 */
	public void setFechaGuardia(String fechaGuardia) {
		this.fechaGuardia = fechaGuardia;
	}


	/**
	 * @return the solicitanteDescripcion
	 */
	public String getSolicitanteDescripcion() {
		return solicitanteDescripcion;
	}


	/**
	 * @param solicitanteDescripcion the solicitanteDescripcion to set
	 */
	public void setSolicitanteDescripcion(String solicitanteDescripcion) {
		this.solicitanteDescripcion = solicitanteDescripcion;
	}


	/**
	 * @return the asistenciaAnio
	 */
	public String getAsistenciaAnio() {
		return asistenciaAnio;
	}


	/**
	 * @param asistenciaAnio the asistenciaAnio to set
	 */
	public void setAsistenciaAnio(String asistenciaAnio) {
		this.asistenciaAnio = asistenciaAnio;
	}


	/**
	 * @return the asistenciaNumero
	 */
	public String getAsistenciaNumero() {
		return asistenciaNumero;
	}


	/**
	 * @param asistenciaNumero the asistenciaNumero to set
	 */
	public void setAsistenciaNumero(String asistenciaNumero) {
		this.asistenciaNumero = asistenciaNumero;
	}


	/**
	 * @return the asistenciaDescripcion
	 */
	public String getAsistenciaDescripcion() {
		return asistenciaDescripcion;
	}


	/**
	 * @param asistenciaDescripcion the asistenciaDescripcion to set
	 */
	public void setAsistenciaDescripcion(String asistenciaDescripcion) {
		this.asistenciaDescripcion = asistenciaDescripcion;
	}


	/**
	 * @return the idTipoAsistenciaColegio
	 */
	public String getIdTipoAsistenciaColegio() {
		return idTipoAsistenciaColegio;
	}


	/**
	 * @param idTipoAsistenciaColegio the idTipoAsistenciaColegio to set
	 */
	public void setIdTipoAsistenciaColegio(String idTipoAsistenciaColegio) {
		this.idTipoAsistenciaColegio = idTipoAsistenciaColegio;
	}


	/**
	 * @return the solicitantePoblacionExt
	 */
	public String getSolicitantePoblacionExt() {
		return solicitantePoblacionExt;
	}


	/**
	 * @param solicitantePoblacionExt the solicitantePoblacionExt to set
	 */
	public void setSolicitantePoblacionExt(String solicitantePoblacionExt) {
		this.solicitantePoblacionExt = solicitantePoblacionExt;
	}


	/**
	 * @return the solicitantePais
	 */
	public String getSolicitantePais() {
		return solicitantePais;
	}


	/**
	 * @param solicitantePais the solicitantePais to set
	 */
	public void setSolicitantePais(String solicitantePais) {
		this.solicitantePais = solicitantePais;
	}


	/**
	 * @return the solicitanteCorreoElectronico
	 */
	public String getSolicitanteCorreoElectronico() {
		return solicitanteCorreoElectronico;
	}


	/**
	 * @param solicitanteCorreoElectronico the solicitanteCorreoElectronico to set
	 */
	public void setSolicitanteCorreoElectronico(String solicitanteCorreoElectronico) {
		this.solicitanteCorreoElectronico = solicitanteCorreoElectronico;
	}


	/**
	 * @return the solicitanteTelefono
	 */
	public String getSolicitanteTelefono() {
		return solicitanteTelefono;
	}


	/**
	 * @param solicitanteTelefono the solicitanteTelefono to set
	 */
	public void setSolicitanteTelefono(String solicitanteTelefono) {
		this.solicitanteTelefono = solicitanteTelefono;
	}


	/**
	 * @return the solicitanteFax
	 */
	public String getSolicitanteFax() {
		return solicitanteFax;
	}


	/**
	 * @param solicitanteFax the solicitanteFax to set
	 */
	public void setSolicitanteFax(String solicitanteFax) {
		this.solicitanteFax = solicitanteFax;
	}


	/**
	 * @return the solicitanteTipoVia
	 */
	public String getSolicitanteTipoVia() {
		return solicitanteTipoVia;
	}


	/**
	 * @param solicitanteTipoVia the solicitanteTipoVia to set
	 */
	public void setSolicitanteTipoVia(String solicitanteTipoVia) {
		this.solicitanteTipoVia = solicitanteTipoVia;
	}


	/**
	 * @return the solicitanteDireccion
	 */
	public String getSolicitanteDireccion() {
		return solicitanteDireccion;
	}


	/**
	 * @param solicitanteDireccion the solicitanteDireccion to set
	 */
	public void setSolicitanteDireccion(String solicitanteDireccion) {
		this.solicitanteDireccion = solicitanteDireccion;
	}


	/**
	 * @return the solicitantePoblacion
	 */
	public String getSolicitantePoblacion() {
		return solicitantePoblacion;
	}


	/**
	 * @param solicitantePoblacion the solicitantePoblacion to set
	 */
	public void setSolicitantePoblacion(String solicitantePoblacion) {
		this.solicitantePoblacion = solicitantePoblacion;
	}


	/**
	 * @return the solicitanteNumero
	 */
	public String getSolicitanteNumero() {
		return solicitanteNumero;
	}


	/**
	 * @param solicitanteNumero the solicitanteNumero to set
	 */
	public void setSolicitanteNumero(String solicitanteNumero) {
		this.solicitanteNumero = solicitanteNumero;
	}


	/**
	 * @return the solicitanteEscalera
	 */
	public String getSolicitanteEscalera() {
		return solicitanteEscalera;
	}


	/**
	 * @param solicitanteEscalera the solicitanteEscalera to set
	 */
	public void setSolicitanteEscalera(String solicitanteEscalera) {
		this.solicitanteEscalera = solicitanteEscalera;
	}


	/**
	 * @return the solicitantePiso
	 */
	public String getSolicitantePiso() {
		return solicitantePiso;
	}


	/**
	 * @param solicitantePiso the solicitantePiso to set
	 */
	public void setSolicitantePiso(String solicitantePiso) {
		this.solicitantePiso = solicitantePiso;
	}


	/**
	 * @return the solicitantePuerta
	 */
	public String getSolicitantePuerta() {
		return solicitantePuerta;
	}


	/**
	 * @param solicitantePuerta the solicitantePuerta to set
	 */
	public void setSolicitantePuerta(String solicitantePuerta) {
		this.solicitantePuerta = solicitantePuerta;
	}


	/**
	 * @return the solicitanteCodPostal
	 */
	public String getSolicitanteCodPostal() {
		return solicitanteCodPostal;
	}


	/**
	 * @param solicitanteCodPostal the solicitanteCodPostal to set
	 */
	public void setSolicitanteCodPostal(String solicitanteCodPostal) {
		this.solicitanteCodPostal = solicitanteCodPostal;
	}


	/**
	 * @return the solicitanteProvincia
	 */
	public String getSolicitanteProvincia() {
		return solicitanteProvincia;
	}


	/**
	 * @param solicitanteProvincia the solicitanteProvincia to set
	 */
	public void setSolicitanteProvincia(String solicitanteProvincia) {
		this.solicitanteProvincia = solicitanteProvincia;
	}


	/**
	 * @return the solicitanteIdTipoPersona
	 */
	public String getSolicitanteIdTipoPersona() {
		return solicitanteIdTipoPersona;
	}


	/**
	 * @param solicitanteIdTipoPersona the solicitanteIdTipoPersona to set
	 */
	public void setSolicitanteIdTipoPersona(String solicitanteIdTipoPersona) {
		this.solicitanteIdTipoPersona = solicitanteIdTipoPersona;
	}


	/**
	 * @return the solicitanteIdTipoIdentificacion
	 */
	public String getSolicitanteIdTipoIdentificacion() {
		return solicitanteIdTipoIdentificacion;
	}


	/**
	 * @param solicitanteIdTipoIdentificacion the solicitanteIdTipoIdentificacion to set
	 */
	public void setSolicitanteIdTipoIdentificacion(
			String solicitanteIdTipoIdentificacion) {
		this.solicitanteIdTipoIdentificacion = solicitanteIdTipoIdentificacion;
	}


	/**
	 * @return the solicitanteNumeroIdentificacion
	 */
	public String getSolicitanteNumeroIdentificacion() {
		return solicitanteNumeroIdentificacion;
	}


	/**
	 * @param solicitanteNumeroIdentificacion the solicitanteNumeroIdentificacion to set
	 */
	public void setSolicitanteNumeroIdentificacion(
			String solicitanteNumeroIdentificacion) {
		this.solicitanteNumeroIdentificacion = solicitanteNumeroIdentificacion;
	}


	/**
	 * @return the solicitanteNombre
	 */
	public String getSolicitanteNombre() {
		return solicitanteNombre;
	}


	/**
	 * @param solicitanteNombre the solicitanteNombre to set
	 */
	public void setSolicitanteNombre(String solicitanteNombre) {
		this.solicitanteNombre = solicitanteNombre;
	}
	


	public String getSexo() {
		return sexo;
	}


	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	/**
	 * @return the solicitanteApellido1
	 */
	public String getSolicitanteApellido1() {
		return solicitanteApellido1;
	}


	/**
	 * @param solicitanteApellido1 the solicitanteApellido1 to set
	 */
	public void setSolicitanteApellido1(String solicitanteApellido1) {
		this.solicitanteApellido1 = solicitanteApellido1;
	}


	/**
	 * @return the solicitanteApellido2
	 */
	public String getSolicitanteApellido2() {
		return solicitanteApellido2;
	}


	/**
	 * @param solicitanteApellido2 the solicitanteApellido2 to set
	 */
	public void setSolicitanteApellido2(String solicitanteApellido2) {
		this.solicitanteApellido2 = solicitanteApellido2;
	}


	/**
	 * @return the fechaRecepcion
	 */
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}


	/**
	 * @param fechaRecepcion the fechaRecepcion to set
	 */
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	String idComisaria;
	String idJuzgado;
	String idSolicitudAceptada;
	String idLlamada;

	String datosMasivos;
	
	/**
	 * @return the datosMasivos
	 */
	public String getDatosMasivos() {
		return datosMasivos;
	}


	/**
	 * @param datosMasivos the datosMasivos to set
	 */
	public void setDatosMasivos(String datosMasivos) {
		this.datosMasivos = datosMasivos;
	}
	FilaExtElement[] elementosFila;
	

	/**
	 * @return the descripcionColegiado
	 */
	public String getDescripcionColegiado() {
		return descripcionColegiado;
	}


	/**
	 * @param descripcionColegiado the descripcionColegiado to set
	 */
	public void setDescripcionColegiado(String descripcionColegiado) {
		this.descripcionColegiado = descripcionColegiado;
	}


	

	/**
	 * @return the nombreCentroDetencion
	 */
	public String getNombreCentroDetencion() {
		return nombreCentroDetencion;
	}


	/**
	 * @param nombreCentroDetencion the nombreCentroDetencion to set
	 */
	public void setNombreCentroDetencion(String nombreCentroDetencion) {
		this.nombreCentroDetencion = nombreCentroDetencion;
	}


	/**
	 * @return the idCentroDetencion
	 */
	public String getIdCentroDetencion() {
		return idCentroDetencion;
	}


	/**
	 * @param idCentroDetencion the idCentroDetencion to set
	 */
	public void setIdCentroDetencion(String idCentroDetencion) {
		this.idCentroDetencion = idCentroDetencion;
	}


	
	
	

	/**
	 * @return the idInstitucion
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}


	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}


	/**
	 * @return the idPersona
	 */
	public String getIdPersona() {
		return idPersona;
	}


	/**
	 * @param idPersona the idPersona to set
	 */
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}


	/**
	 * @return the idTurno
	 */
	public String getIdTurno() {
		return idTurno;
	}


	/**
	 * @param idTurno the idTurno to set
	 */
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}


	/**
	 * @return the idGuardia
	 */
	public String getIdGuardia() {
		return idGuardia;
	}


	/**
	 * @param idGuardia the idGuardia to set
	 */
	public void setIdGuardia(String idGuardia) {
		this.idGuardia = idGuardia;
	}


	/**
	 * @return the nombreGuardia
	 */
	public String getNombreGuardia() {
		return nombreGuardia;
	}


	/**
	 * @param nombreGuardia the nombreGuardia to set
	 */
	public void setNombreGuardia(String nombreGuardia) {
		this.nombreGuardia = nombreGuardia;
	}


	/**
	 * @return the idEstado
	 */
	public String getIdEstado() {
		return idEstado;
	}


	/**
	 * @param idEstado the idEstado to set
	 */
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}


	
	


	

	/**
	 * @return the descripcionEstado
	 */
	public String getDescripcionEstado() {
		return descripcionEstado;
	}


	/**
	 * @param descripcionEstado the descripcionEstado to set
	 */
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}


	/**
	 * @return the colegiadoNumero
	 */
	public String getColegiadoNumero() {
		return colegiadoNumero;
	}


	/**
	 * @param colegiadoNumero the colegiadoNumero to set
	 */
	public void setColegiadoNumero(String colegiadoNumero) {
		this.colegiadoNumero = colegiadoNumero;
	}


	/**
	 * @return the colegiadoNombre
	 */
	public String getColegiadoNombre() {
		return colegiadoNombre;
	}


	/**
	 * @param colegiadoNombre the colegiadoNombre to set
	 */
	public void setColegiadoNombre(String colegiadoNombre) {
		this.colegiadoNombre = colegiadoNombre;
	}


	/**
	 * @return the fechaDesde
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}


	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}


	/**
	 * @return the fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}


	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}


	/**
	 * @return the idComisaria
	 */
	public String getIdComisaria() {
		return idComisaria;
	}


	/**
	 * @param idComisaria the idComisaria to set
	 */
	public void setIdComisaria(String idComisaria) {
		this.idComisaria = idComisaria;
	}


	/**
	 * @return the idJuzgado
	 */
	public String getIdJuzgado() {
		return idJuzgado;
	}


	/**
	 * @param idJuzgado the idJuzgado to set
	 */
	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}


	/**
	 * @return the idSolicitudAceptada
	 */
	public String getIdSolicitudAceptada() {
		return idSolicitudAceptada;
	}


	/**
	 * @param idSolicitudAceptada the idSolicitudAceptada to set
	 */
	public void setIdSolicitudAceptada(String idSolicitudAceptada) {
		this.idSolicitudAceptada = idSolicitudAceptada;
	}


	
	
	
	
	
	
	
	
	
	
	






	



	

	public FilaExtElement[] getElementosFila() {
		//si las guardia no son obligatorias
		
		switch (Integer.valueOf(idEstado)) {
			case 0:
				elementosFila =  new FilaExtElement[3];
				elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
				elementosFila[1]=new FilaExtElement("confirmar","confirmar",SIGAConstants.ACCESS_FULL);
				elementosFila[2]=new FilaExtElement("denegar","denegar",SIGAConstants.ACCESS_FULL);
			break;
			case 1:
				elementosFila =  new FilaExtElement[2];
				elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
				elementosFila[1]=new FilaExtElement("confirmar","confirmar",SIGAConstants.ACCESS_FULL);
				break;
			case 2:
				elementosFila =  new FilaExtElement[2];
				elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
				elementosFila[1]=new FilaExtElement("solicitaralta","activar",SIGAConstants.ACCESS_FULL);
				break;

			default:
				elementosFila =  new FilaExtElement[1];
				elementosFila[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
		}
		
			
		
		return elementosFila;
	}
	
	
	public void setElementosFila(FilaExtElement[]elementosFila ) {
		this.elementosFila = elementosFila;
		
		
			
		
	}
	public void clear() {
		idSolicitudAceptada = null;
		idLlamada = null;
		idPersona = null;
		idTurno = null;
		idGuardia = null;
		numAvisoCV = null;
		colegiadoNumero = null;
		colegiadoNombre = null;
		idCentroDetencion = null;
		idJuzgado = null;
		idPersona  = null;
		fechaDesde = null;
		fechaHasta = null;
		idEstado = null;
		fechaGuardia = null;

	}
	public SolicitudAceptadaCentralitaForm clone() {
		SolicitudAceptadaCentralitaForm miForm = new SolicitudAceptadaCentralitaForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
        miForm.setIdSolicitudAceptada(this.getIdSolicitudAceptada());
        miForm.setIdLlamada(this.getIdLlamada());
        miForm.setNumAvisoCV(this.getNumAvisoCV());
        miForm.setIdEstado(this.getIdEstado());
        miForm.setIdTurno(this.getIdTurno());
        miForm.setIdGuardia(this.getIdGuardia());
        miForm.setFechaDesde(this.getFechaDesde());
        miForm.setFechaHasta(this.getFechaHasta());
        miForm.setIdComisaria(this.getIdComisaria());
        miForm.setIdJuzgado(this.getIdJuzgado());
        miForm.setIdPersona(this.getIdPersona());
        miForm.setColegiadoNombre(this.getColegiadoNombre());
        miForm.setColegiadoNumero(this.getColegiadoNumero());
        miForm.setDatosPaginador(this.getDatosPaginador());
		
		
		
		return miForm;
		
	}


	/**
	 * @return the idLlamada
	 */
	public String getIdLlamada() {
		return idLlamada;
	}


	/**
	 * @param idLlamada the idLlamada to set
	 */
	public void setIdLlamada(String idLlamada) {
		this.idLlamada = idLlamada;
	}


	public String getNumAvisoCV() {
		return numAvisoCV;
	}


	public void setNumAvisoCV(String numAvisoCV) {
		this.numAvisoCV = numAvisoCV;
	}


	
	
}