package com.siga.gratuita.form.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.vo.scs.SolicitudAceptadaCentralitaVo;

import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.VoUiService;
import com.siga.gratuita.form.SolicitudAceptadaCentralitaForm;

/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class SolicitudAceptadaCentralitaVoService implements VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> {
	
	public List<SolicitudAceptadaCentralitaForm> getVo2FormList(
			List<SolicitudAceptadaCentralitaVo> voList) {
		List<SolicitudAceptadaCentralitaForm> SolicitudAceptadaCentralitaForms = new ArrayList<SolicitudAceptadaCentralitaForm>();
		SolicitudAceptadaCentralitaForm SolicitudAceptadaCentralitaForm   = null;
		for (SolicitudAceptadaCentralitaVo objectVo : voList) {
			SolicitudAceptadaCentralitaForm = getVo2Form(objectVo);
			SolicitudAceptadaCentralitaForms.add(SolicitudAceptadaCentralitaForm);
			
		}
		return SolicitudAceptadaCentralitaForms;
	}

	@Override
	public SolicitudAceptadaCentralitaVo getForm2Vo(SolicitudAceptadaCentralitaForm objectForm) {
		
		SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVo = new SolicitudAceptadaCentralitaVo();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals(""))
			solicitudAceptadaCentralitaVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		if(objectForm.getColegiadoNumero()!=null && !objectForm.getColegiadoNumero().equals(""))
			solicitudAceptadaCentralitaVo.setNumerocolegiado(objectForm.getColegiadoNumero());
		if(objectForm.getIdPersona()!=null && !objectForm.getIdPersona().equals(""))
			solicitudAceptadaCentralitaVo.setIdPersona(Long.valueOf(objectForm.getIdPersona()));
		
		if(objectForm.getFechaDesde()!=null && !objectForm.getFechaDesde().equals(""))
			try {
				solicitudAceptadaCentralitaVo.setFechaDesde(sdf.parse(objectForm.getFechaDesde()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(objectForm.getFechaHasta()!=null && !objectForm.getFechaHasta().equals(""))
			try {
				solicitudAceptadaCentralitaVo.setFechaHasta(sdf.parse(objectForm.getFechaHasta()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(objectForm.getIdGuardia()!=null && !objectForm.getIdGuardia().equals(""))
			solicitudAceptadaCentralitaVo.setIdguardia(Integer.valueOf(objectForm.getIdGuardia()));
		if(objectForm.getIdTurno()!=null && !objectForm.getIdTurno().equals("")){
			
			HashMap<String, String> hashMap = null;
			try {
				hashMap = UtilidadesString.createJsonMap(objectForm.getIdTurno());
				solicitudAceptadaCentralitaVo.setIdTurno(Integer.valueOf((String)hashMap.get("idturno")));
			} catch (Exception e) {
				solicitudAceptadaCentralitaVo.setIdTurno(Integer.valueOf(objectForm.getIdTurno()));
				
			}
			
		}
		if(objectForm.getIdSolicitudAceptada()!=null && !objectForm.getIdSolicitudAceptada().equals(""))
			solicitudAceptadaCentralitaVo.setIdsolicitud(Integer.valueOf(objectForm.getIdSolicitudAceptada()));
		if(objectForm.getIdLlamada()!=null && !objectForm.getIdLlamada().equals(""))
			solicitudAceptadaCentralitaVo.setIdllamada(Long.valueOf(objectForm.getIdLlamada()));
		
		if(objectForm.getIdComisaria()!=null && !objectForm.getIdComisaria().equals("")){
			solicitudAceptadaCentralitaVo.setIdcentrodetencion(objectForm.getIdComisaria().split(",")[0]);
			solicitudAceptadaCentralitaVo.setIdtipocentrodetencion(String.valueOf(AppConstants.LUGARACTUACION_CENTRODETENCION));
			solicitudAceptadaCentralitaVo.setNombrecentrodetencion(objectForm.getNombreCentroDetencion());
		}
		if(objectForm.getIdJuzgado()!=null && !objectForm.getIdJuzgado().equals("")){
			HashMap<String, String> hashMap = null;
			try {
				hashMap = UtilidadesString.createJsonMap(objectForm.getIdJuzgado());
				solicitudAceptadaCentralitaVo.setIdcentrodetencion((String)hashMap.get("idjuzgado"));
			} catch (Exception e) {
				solicitudAceptadaCentralitaVo.setIdTurno(Integer.valueOf(objectForm.getIdTurno()));
				
			}
			
			
			solicitudAceptadaCentralitaVo.setIdtipocentrodetencion(String.valueOf(AppConstants.LUGARACTUACION_JUZGADO));
			solicitudAceptadaCentralitaVo.setNombrecentrodetencion(objectForm.getNombreCentroDetencion());
			
		}
		
		if(objectForm.getIdEstado()!=null && !objectForm.getIdEstado().equals(""))
			solicitudAceptadaCentralitaVo.setEstado(Short.valueOf(objectForm.getIdEstado()));
		if(objectForm.getIdTipoAsistenciaColegio()!=null && !objectForm.getIdTipoAsistenciaColegio().equals(""))
			solicitudAceptadaCentralitaVo.setIdTipoAsistenciaColegio(objectForm.getIdTipoAsistenciaColegio());
		if(objectForm.getFechaGuardia()!=null && !objectForm.getFechaGuardia().equals(""))
			try {
//				solicitudAceptadaCentralitaVo.setFechallamada(sdf.parse(objectForm.getFechaGuardia()));
				solicitudAceptadaCentralitaVo.setFechaguardia(sdf.parse(objectForm.getFechaGuardia()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		solicitudAceptadaCentralitaVo.setOrigenAsistencia(AppConstants.ORIGENASISTENCIA.CENTRALITAGUARDIAS.getCodigo());
		solicitudAceptadaCentralitaVo.setObservacionesOrigen(AppConstants.ORIGENASISTENCIA.CENTRALITAGUARDIAS.getRecursoObservaciones());
		
		solicitudAceptadaCentralitaVo.setSolicitanteIdTipoIdentificacion(objectForm.getSolicitanteIdTipoIdentificacion());
		solicitudAceptadaCentralitaVo.setSolicitanteNumeroIdentificacion(objectForm.getSolicitanteNumeroIdentificacion());
		solicitudAceptadaCentralitaVo.setSolicitanteNombre(objectForm.getSolicitanteNombre());
		solicitudAceptadaCentralitaVo.setSolicitanteApellido1(objectForm.getSolicitanteApellido1());
		solicitudAceptadaCentralitaVo.setSolicitanteApellido2(objectForm.getSolicitanteApellido2());
		solicitudAceptadaCentralitaVo.setSolicitanteTipoVia(objectForm.getSolicitanteTipoVia());
		solicitudAceptadaCentralitaVo.setSolicitanteDireccion(objectForm.getSolicitanteDireccion());
		solicitudAceptadaCentralitaVo.setSolicitanteNumero(objectForm.getSolicitanteNumero());
		solicitudAceptadaCentralitaVo.setSolicitantePuerta(objectForm.getSolicitantePuerta());
		solicitudAceptadaCentralitaVo.setSolicitanteEscalera(objectForm.getSolicitanteEscalera());
		solicitudAceptadaCentralitaVo.setSolicitantePiso(objectForm.getSolicitantePiso());
		solicitudAceptadaCentralitaVo.setSolicitantePais(objectForm.getSolicitantePais());
		solicitudAceptadaCentralitaVo.setSolicitanteProvincia(objectForm.getSolicitanteProvincia());
		if(objectForm.getSolicitantePoblacionExt()!=null && !objectForm.getSolicitantePoblacionExt().equals(""))
			solicitudAceptadaCentralitaVo.setSolicitantePoblacionExt(objectForm.getSolicitantePoblacionExt());
		else
			solicitudAceptadaCentralitaVo.setSolicitantePoblacion(objectForm.getSolicitantePoblacion());
		solicitudAceptadaCentralitaVo.setSolicitanteCodPostal(objectForm.getSolicitanteCodPostal());
		solicitudAceptadaCentralitaVo.setSolicitanteTelefono(objectForm.getSolicitanteTelefono());
		solicitudAceptadaCentralitaVo.setSolicitanteCorreoElectronico(objectForm.getSolicitanteCorreoElectronico());
		solicitudAceptadaCentralitaVo.setSolicitanteFax(objectForm.getSolicitanteFax());
		
		
		return solicitudAceptadaCentralitaVo;
	}

	
	
	public SolicitudAceptadaCentralitaForm getVo2Form(SolicitudAceptadaCentralitaVo objectVo) {
		return getVo2Form(objectVo,new SolicitudAceptadaCentralitaForm());
	}
	public SolicitudAceptadaCentralitaForm getVo2Form(SolicitudAceptadaCentralitaVo objectVo, SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm) {
		SimpleDateFormat sdfddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfddmmyyyyhhmm = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		if(objectVo.getIdinstitucion()!=null)
			solicitudAceptadaCentralitaForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
		if(objectVo.getNumerocolegiado()!=null && !objectVo.getNumerocolegiado().equals("")){
			StringBuffer descripcionColegiado = new StringBuffer("");
			if(objectVo.getColegiadoNombre()!=null){
				descripcionColegiado.append(objectVo.getColegiadoNombre());
				descripcionColegiado.append(" ");
			}
			if(objectVo.getColegiadoApellido1()!=null){
				descripcionColegiado.append(" ");
				descripcionColegiado.append(objectVo.getColegiadoApellido1());
			}
			if(objectVo.getColegiadoApellido2()!=null){
				descripcionColegiado.append(" ");
				descripcionColegiado.append(objectVo.getColegiadoApellido2());
			}
			solicitudAceptadaCentralitaForm.setColegiadoNombre(descripcionColegiado.toString());
			descripcionColegiado.insert(0,objectVo.getNumerocolegiado()+" ");
			solicitudAceptadaCentralitaForm.setDescripcionColegiado(descripcionColegiado.toString());
			solicitudAceptadaCentralitaForm.setColegiadoNumero(objectVo.getNumerocolegiado());
			if(objectVo.getIdPersona()!=null ){
				solicitudAceptadaCentralitaForm.setIdPersona(objectVo.getIdPersona().toString());
			}
		}
		if(objectVo.getFechallamada()!=null && !objectVo.getFechallamada().equals("")){
			
				solicitudAceptadaCentralitaForm.setFechaLlamadaHoras(sdfddmmyyyyhhmm.format(objectVo.getFechallamada()));
		}
		
		if(objectVo.getFechaguardia()!=null && !objectVo.getFechaguardia().equals("")){
			
			solicitudAceptadaCentralitaForm.setFechaGuardia(sdfddmmyyyy.format(objectVo.getFechaguardia()));
		}
		if(objectVo.getFecharecepcion()!=null && !objectVo.getFecharecepcion().equals(""))
			solicitudAceptadaCentralitaForm.setFechaRecepcion(sdfddmmyyyyhhmm.format(objectVo.getFecharecepcion()));
		if(objectVo.getIdguardia()!=null && !objectVo.getIdguardia().equals("")){
			solicitudAceptadaCentralitaForm.setIdGuardia(objectVo.getIdguardia().toString());
		}
		if(objectVo.getNombreguardia()!=null && !objectVo.getNombreguardia().equals(""))
			solicitudAceptadaCentralitaForm.setNombreGuardia(objectVo.getNombreguardia());
		
		if(objectVo.getIdTurno()!=null && !objectVo.getIdTurno().equals("")){
			solicitudAceptadaCentralitaForm.setIdTurno(objectVo.getIdTurno().toString());
		}
		if(objectVo.getIdsolicitud()!=null && !objectVo.getIdsolicitud().equals(""))
			solicitudAceptadaCentralitaForm.setIdSolicitudAceptada(objectVo.getIdsolicitud().toString());
		if(objectVo.getIdllamada()!=null && !objectVo.getIdllamada().equals(""))
			solicitudAceptadaCentralitaForm.setIdLlamada(objectVo.getIdllamada().toString());
		
		if(objectVo.getIdtipocentrodetencion()!=null && objectVo.getIdtipocentrodetencion().equals(String.valueOf(AppConstants.LUGARACTUACION_CENTRODETENCION))){
			if(objectVo.getIdcentrodetencion()!=null && !objectVo.getIdcentrodetencion().equals("")){
				solicitudAceptadaCentralitaForm.setIdComisaria(objectVo.getIdcentrodetencion()+","+objectVo.getIdinstitucion());
			}	
		}else if(objectVo.getIdtipocentrodetencion()!=null && objectVo.getIdtipocentrodetencion().equals(String.valueOf(AppConstants.LUGARACTUACION_JUZGADO))){
			if(objectVo.getIdcentrodetencion()!=null && !objectVo.getIdcentrodetencion().equals("")){
				solicitudAceptadaCentralitaForm.setIdJuzgado(objectVo.getIdcentrodetencion()+","+objectVo.getIdinstitucion());
				
			}
		}
		if(objectVo.getNombrecentrodetencion()!=null && !objectVo.getNombrecentrodetencion().equals("")){
			solicitudAceptadaCentralitaForm.setNombreCentroDetencion(objectVo.getNombrecentrodetencion());
		}
		
		if(objectVo.getEstado()!=null && !objectVo.getEstado().equals(""))
			solicitudAceptadaCentralitaForm.setIdEstado(objectVo.getEstado().toString());
		if(objectVo.getDescripcionEstado()!=null && !objectVo.getDescripcionEstado().equals(""))
			solicitudAceptadaCentralitaForm.setDescripcionEstado(objectVo.getDescripcionEstado().toString());
		
		solicitudAceptadaCentralitaForm.setAsistenciaAnio(objectVo.getAsistenciaAnio());
		solicitudAceptadaCentralitaForm.setAsistenciaNumero(objectVo.getAsistenciaNumero());
		if(objectVo.getAsistenciaAnio()!=null){
			solicitudAceptadaCentralitaForm.setAsistenciaDescripcion(objectVo.getAsistenciaAnio()+"/"+objectVo.getAsistenciaNumero());
		}
		if(objectVo.getSolicitanteNombre()!=null && !objectVo.getSolicitanteNombre().equals("")){
			StringBuffer descripcionSolicitante = new StringBuffer();
			if(objectVo.getSolicitanteNombre()!=null){
				descripcionSolicitante.append(objectVo.getSolicitanteNombre());
				descripcionSolicitante.append(" ");
			}
			if(objectVo.getSolicitanteApellido1()!=null){
				descripcionSolicitante.append(" ");
				descripcionSolicitante.append(objectVo.getSolicitanteApellido1());
			}
			if(objectVo.getSolicitanteApellido2()!=null){
				descripcionSolicitante.append(" ");
				descripcionSolicitante.append(objectVo.getSolicitanteApellido2());
			}
			solicitudAceptadaCentralitaForm.setSolicitanteDescripcion(descripcionSolicitante.toString());
			
		}
		
		
		
		
		solicitudAceptadaCentralitaForm.setSolicitanteIdTipoIdentificacion(objectVo.getSolicitanteIdTipoIdentificacion());
		solicitudAceptadaCentralitaForm.setSolicitanteNumeroIdentificacion(objectVo.getSolicitanteNumeroIdentificacion());
		solicitudAceptadaCentralitaForm.setSolicitanteNombre(objectVo.getSolicitanteNombre());
		solicitudAceptadaCentralitaForm.setSolicitanteApellido1(objectVo.getSolicitanteApellido1());
		solicitudAceptadaCentralitaForm.setSolicitanteApellido2(objectVo.getSolicitanteApellido2());
		solicitudAceptadaCentralitaForm.setSolicitanteTipoVia(objectVo.getSolicitanteTipoVia());
		solicitudAceptadaCentralitaForm.setSolicitanteDireccion(objectVo.getSolicitanteDireccion());
		solicitudAceptadaCentralitaForm.setSolicitanteNumero(objectVo.getSolicitanteNumero());
		solicitudAceptadaCentralitaForm.setSolicitantePuerta(objectVo.getSolicitantePuerta());
		solicitudAceptadaCentralitaForm.setSolicitanteEscalera(objectVo.getSolicitanteEscalera());
		solicitudAceptadaCentralitaForm.setSolicitantePiso(objectVo.getSolicitantePiso());
		solicitudAceptadaCentralitaForm.setSolicitantePais(objectVo.getSolicitantePais());
		solicitudAceptadaCentralitaForm.setSolicitanteProvincia(objectVo.getSolicitanteProvincia());
		if(objectVo.getSolicitantePoblacionExt()!=null && !objectVo.getSolicitantePoblacionExt().equals(""))
			solicitudAceptadaCentralitaForm.setSolicitantePoblacionExt(objectVo.getSolicitantePoblacionExt());
		else
			solicitudAceptadaCentralitaForm.setSolicitantePoblacion(objectVo.getSolicitantePoblacion());
		solicitudAceptadaCentralitaForm.setSolicitanteTelefono(objectVo.getSolicitanteTelefono());
		solicitudAceptadaCentralitaForm.setSolicitanteCorreoElectronico(objectVo.getSolicitanteCorreoElectronico());
		solicitudAceptadaCentralitaForm.setSolicitanteFax(objectVo.getSolicitanteFax());
		
		solicitudAceptadaCentralitaForm.setAsistenciaAnio(objectVo.getAsistenciaAnio());
		solicitudAceptadaCentralitaForm.setAsistenciaNumero(objectVo.getAsistenciaNumero());
		
		
		
		return solicitudAceptadaCentralitaForm;
	}




	

	
	
	

}
