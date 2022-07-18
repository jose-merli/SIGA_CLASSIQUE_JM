package com.siga.gratuita.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.MasterBean;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.service.VolantesExpressService;
import com.siga.gratuita.vos.VolantesExpressVo;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosVolantesExpressService extends JtaBusinessServiceTemplate 
	implements VolantesExpressService {

	public AtosVolantesExpressService(BusinessManager businessManager) {
		super(businessManager);
	}

	
	/**
	 * Inserta y Borra las asistencias
	 * 
	 * @param Object... params
	 * 0 -->VolantesExpressVo
	 * @return Devuelve un arrayList con las asistencias 
	 * @throws BusinessException
	 * @throws SIGAException 
	 * @throws SIGAException
	 * @throws ClsExceptions
	 * @throws Exception
	 */	
	public Object executeService(VolantesExpressVo volantesExpressVo ) throws SIGAException,ClsExceptions {
		
//		VolantesExpressVo volantesExpressVo = (VolantesExpressVo)params[0];
		List<ScsAsistenciasBean> alAsistencias = null;
		try {
			
			ArrayList<ScsAsistenciasBean> alAsistenciasOld = (ArrayList<ScsAsistenciasBean>) volantesExpressVo.getAsistencias();
			alAsistencias = (List<ScsAsistenciasBean>)getAsistencias(volantesExpressVo);
			volantesExpressVo.setAsistencias(alAsistencias);
			List<ScsAsistenciasBean> alAsistenciasBorrar = (List<ScsAsistenciasBean>) getAsistenciasABorrar(alAsistencias,alAsistenciasOld);
			
			ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(volantesExpressVo.getUsrBean());
			ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(volantesExpressVo.getUsrBean());
			String truncFechaGuardia = GstDate.getFormatedDateShort("", volantesExpressVo.getFechaGuardia());
			if (volantesExpressVo.getIdColegiadoSustituido()!=null) {
				CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(volantesExpressVo.getUsrBean());
				//comprobamos que el letrado no esta de vacaciones 
				Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(volantesExpressVo.getIdColegiado(), volantesExpressVo.getIdInstitucion());
				if( mBajasTemporales.containsKey(truncFechaGuardia))
					throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
				
				
				//Si el colegiado sustituye a otro colegiado de la guardia
				cabeceraGuardiasAdm.aplicarSustitucion(volantesExpressVo.getIdInstitucion(), volantesExpressVo.getIdTurno(),
							volantesExpressVo.getIdGuardia(), volantesExpressVo.getIdColegiadoSustituido(), 
							volantesExpressVo.getIdColegiado(), truncFechaGuardia, volantesExpressVo.getUsrBean());
			}else{
				Hashtable<String, Object> h = new Hashtable<String, Object>();
				UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, volantesExpressVo.getIdColegiado());
				UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, GstDate.getApplicationFormatDate("", truncFechaGuardia));
				UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, volantesExpressVo.getIdInstitucion());
				if (volantesExpressVo.getIdTurno() != null) {
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO,volantesExpressVo.getIdTurno());
				}
				if (volantesExpressVo.getIdGuardia() != null) {
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA,volantesExpressVo.getIdGuardia());
				}
				//Si no sustutuye a nadie se compruebs si tiene guardia ese dia.
				
				Vector<MasterBean> vGuardias = guardiasColegiadoAdm.selectParaVolantes(h);
				//Si no tiene guardia ese dia insertaremos en el calendario.
				//si no existe calendario saltara la excepcion y no insertara
				
				if (vGuardias == null || vGuardias.size()<1 ){

						CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(volantesExpressVo.getUsrBean());
						//comprobamos que el letrado no esta de vacaciones 
						Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(volantesExpressVo.getIdColegiado(), volantesExpressVo.getIdInstitucion());
						if( mBajasTemporales.containsKey(truncFechaGuardia))
							throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
						
						
						guardiasColegiadoAdm.insertarGuardiaManual(volantesExpressVo.getIdInstitucion().toString(), volantesExpressVo.getIdTurno().toString(),
								volantesExpressVo.getIdGuardia().toString(), volantesExpressVo.getIdColegiado().toString(),  
								null,null,truncFechaGuardia, volantesExpressVo.getUsrBean());
					
				}
			}
			ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm(volantesExpressVo.getUsrBean());
			//Se actualizan los datos(se insertan las nueva y se modifican las viejas)
			asistenciaAdm.insertarAsistenciasVolanteExpress(volantesExpressVo);
			//Eliminamos las que sobran
			asistenciaAdm.borrarAsistenciasVolanteExpres(alAsistenciasBorrar, volantesExpressVo.getUsrBean());
			
		} catch (SIGAException e) {
			throw e;
		} catch (ClsExceptions e) {
			throw e;
		}
		return alAsistencias;
	}
	private List<ScsAsistenciasBean> getAsistencias(VolantesExpressVo volanteExpressVo) throws SIGAException, ClsExceptions {
		if(volanteExpressVo.getDatosAsistencias()==null)
			return new ArrayList<ScsAsistenciasBean>();
		GstStringTokenizer st1 = new GstStringTokenizer(volanteExpressVo.getDatosAsistencias(), "%%%");
		ScsAsistenciasBean asistencia = null;
		List<ScsAsistenciasBean> alAsistencias = new ArrayList<ScsAsistenciasBean>();
		
		while (st1.hasMoreTokens()) {
			
			String fila = st1.nextToken();
			
			if (fila != null && !fila.equals("")) {
				//datosEnvios.append(registro);
				asistencia = new ScsAsistenciasBean();
				
				StringTokenizer celdas = new StringTokenizer(fila, ",");
				for (int j = 0; celdas.hasMoreElements(); j++) {
					String celda = celdas.nextToken();
					String[] registro = celda.split("=");
					String key = registro[0];
					String value = null;
					if(registro.length==2)
						value = registro[1];
					
					if(key.equals("claveAnio")){
						
						if(value!=null)
							asistencia.setAnio(new Integer(value));
					
						
					}else if(key.equals("claveNumero")){
						
						if(value!=null)
							asistencia.setNumero(new Integer(value));
					}else if(key.equals("claveIdInstitucion")){
						if(value!=null)
							asistencia.setIdInstitucion(new Integer(value));	
					}else if(key.equals("hora")){
						if(value!=null)
							asistencia.setHora(value);
						else
							asistencia.setHora("00");
					}else if(key.equals("minuto")){
						if(value!=null)
							asistencia.setMinuto(value);
						else
							asistencia.setMinuto("00");
					}else if(key.equals("comisaria")){
						if(value!=null)
							asistencia.setComisaria(new Long(value));
						
					}else if(key.equals("juzgado")){
						if(value!=null)
							asistencia.setJuzgado(new Long(value));
							
						
					}else if(key.equals("idPersona")){
						if(value!=null && !value.equals("nuevo"))
							asistencia.setIdPersonaJG(new Integer(value));
					}else if(key.equals("dni")){
						if(value!=null){
							value = UtilidadesString.replaceAllIgnoreCase(value, "~",",");
							value = UtilidadesString.replaceAllIgnoreCase(value,  "�","=");
							asistencia.setAsistidoNif(value);
						}else{
							asistencia.setAsistidoNif("");
						}
					}else if(key.equals("nombre")){
						if(value!=null){
							value = UtilidadesString.replaceAllIgnoreCase(value, "~",",");
							value = UtilidadesString.replaceAllIgnoreCase(value,  "�","=");
							asistencia.setAsistidoNombre(value);
						}
					}else if(key.equals("apellido1")){
						if(value!=null){
							value = UtilidadesString.replaceAllIgnoreCase(value, "~",",");
							value = UtilidadesString.replaceAllIgnoreCase(value,  "�","=");
							asistencia.setAsistidoApellido1(value);
						}
					}else if(key.equals("apellido2")){
						if(value!=null){
							value = UtilidadesString.replaceAllIgnoreCase(value, "~",",");
							value = UtilidadesString.replaceAllIgnoreCase(value,  "�","=");
							asistencia.setAsistidoApellido2(value);
						}
					}else if(key.equals("sexo")){
						if(value!=null)
							asistencia.setSexo(value);	
					}
					else if(key.equals("diligencia")){
						if(value!=null){
							value = UtilidadesString.replaceAllIgnoreCase(value, "~",",");
							value = UtilidadesString.replaceAllIgnoreCase(value,  "�","=");
							asistencia.setNumeroDiligencia(value);
						}
					}else if(key.equals("observaciones")){
						if(value!=null){
							value = UtilidadesString.replaceAllIgnoreCase(value, "~",",");
							value = UtilidadesString.replaceAllIgnoreCase(value,  "�","=");
							asistencia.setObservaciones(value);
						}
					}else if(key.equals("delitosImputados")){
						if(value!=null)
							asistencia.setDelitosImputados(value);
					}else if(key.equals("idDelito")){
						if(value!=null)
							asistencia.setIdDelito(new Integer(value));
					}else if(key.equals("ejgNumero")){
						if(value!=null)
							asistencia.setEjgNumero(new Long(value));
					}else if(key.equals("ejgAnio")){
						if(value!=null)
							asistencia.setEjgAnio(new Integer(value));
					}else if(key.equals("ejgTipo")){
						if(value!=null)
							asistencia.setEjgIdTipoEjg(new Integer(value));
					}
				}
				asistencia.setIdInstitucion(volanteExpressVo.getIdInstitucion());
				String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpressVo.getFechaGuardia());
				asistencia.setFechaGuardia(truncFechaGuardia);
				asistencia.setIdturno(volanteExpressVo.getIdTurno());
				asistencia.setIdguardia(volanteExpressVo.getIdGuardia());
				asistencia.setIdPersonaColegiado(volanteExpressVo.getIdColegiado().longValue());
				asistencia.setIdTipoAsistencia(volanteExpressVo.getIdTipoAsistencia());
				asistencia.setIdTipoAsistenciaColegio(volanteExpressVo.getIdTipoAsistenciaColegio());
				String fechaAsistencia;
				try {
					fechaAsistencia = GstDate.getApplicationFormatDate("", truncFechaGuardia);
				} catch (ClsExceptions e) {
					throw e;
				}
				fechaAsistencia = fechaAsistencia.substring(0,fechaAsistencia.indexOf(" ")) + " " + asistencia.getHora() + ":" + asistencia.getMinuto() + ":00";
				asistencia.setFechaEstadoAsistencia("sysdate");
				asistencia.setFechaHora(fechaAsistencia);
				asistencia.setIdEstadoAsistencia(new Integer(1));
				//Atencio en el campo diligencia se guarda, 
				//DILIGENCIA si es centro de detencio y numero de PRECEDIMIENTO si es juzgado
				if(asistencia.getJuzgado()!=null){
					asistencia.setJuzgadoIdInstitucion(asistencia.getIdInstitucion());
					asistencia.setNumeroProcedimiento(asistencia.getNumeroDiligencia());
					asistencia.setNumeroDiligencia("");
				}
				if(asistencia.getComisaria()!=null)
					asistencia.setComisariaIdInstitucion(asistencia.getIdInstitucion());
				alAsistencias.add(asistencia);
			}
		}

		return alAsistencias;
	}
	
	private List<ScsAsistenciasBean> getAsistenciasABorrar(List<ScsAsistenciasBean> alAsistencias, List<ScsAsistenciasBean> alAsistenciasOld){
		
		Map<String, ScsAsistenciasBean> hmAsistenciasOld = new HashMap<String, ScsAsistenciasBean>();
		if(alAsistenciasOld!=null && alAsistenciasOld.size()>0){
			for (int i = 0; i < alAsistenciasOld.size(); i++) {
				ScsAsistenciasBean asistenciaOld = (ScsAsistenciasBean)alAsistenciasOld.get(i);
				if(asistenciaOld.getNumero()!=null&&asistenciaOld.getNumero().intValue()!=-1){
					StringBuffer key = new StringBuffer();
					key.append(asistenciaOld.getAnio());
					key.append("#");
					key.append(asistenciaOld.getNumero());
					key.append("#");
					key.append(asistenciaOld.getIdInstitucion());
					key.append("#");
					hmAsistenciasOld.put(key.toString(), asistenciaOld);
				}
				
			}
		}
		if(alAsistencias!=null && alAsistencias.size()>0){
			
			for (int i = 0; i < alAsistencias.size(); i++) {
				ScsAsistenciasBean asistencia = (ScsAsistenciasBean)alAsistencias.get(i);
				if(asistencia.getAnio()!=null){
					StringBuffer key = new StringBuffer();
					key.append(asistencia.getAnio());
					key.append("#");
					key.append(asistencia.getNumero());
					key.append("#");
					key.append(asistencia.getIdInstitucion());
					key.append("#");
					if(hmAsistenciasOld.containsKey(key.toString())){
						hmAsistenciasOld.remove(key.toString());
					}
				}
			}
		}
		ArrayList<ScsAsistenciasBean> alAsistenciasBorrar = null;
		if(hmAsistenciasOld.values()!=null){
			alAsistenciasBorrar = new ArrayList<ScsAsistenciasBean>();
			alAsistenciasBorrar.addAll(hmAsistenciasOld.values());
		}
		return alAsistenciasBorrar;
	}


	public Object executeService(Object... parameters) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

}
