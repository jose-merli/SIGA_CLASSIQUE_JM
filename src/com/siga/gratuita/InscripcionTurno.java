package com.siga.gratuita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.MasterBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsRetencionesIRPFAdm;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.gratuita.form.InscripcionTGForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

public class InscripcionTurno {
	////////////////////
	// ATRIBUTOS
	////////////////////
	private ScsInscripcionTurnoBean bean = null;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
	
	////////////////////
	// CONSTRUCTORES
	////////////////////
	/**
	 * Constructor 
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param fechaSolicitudAlta
	 */
	private InscripcionTurno (Integer idInstitucion, Integer idTurno, Long idPersona,  String fechaSolicitudAlta) {
		this.bean = new ScsInscripcionTurnoBean();
		this.bean.setIdInstitucion(idInstitucion);
		this.bean.setIdTurno(idTurno);
		this.bean.setIdPersona(idPersona);
		this.bean.setFechaSolicitud(fechaSolicitudAlta);
	} //InscripcionTurno ()
	
	/**
	 * Constructor
	 * 
	 * @param bean
	 */
	public InscripcionTurno (ScsInscripcionTurnoBean bean) {
		this.bean = bean;
	} //InscripcionTurno ()
		
	////////////////////
	// GETTERS Y SETTERS
	////////////////////
	public ScsInscripcionTurnoBean getBean() { return bean; }
	public void setBean(ScsInscripcionTurnoBean bean) { this.bean = bean; }
	
	////////////////////
	// METODOS DE CLASE
	////////////////////
	
	/**
	 * Solicita el alta de una inscripcion de turno de un colegiado
	 * 
	 * @param form
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void solicitarAlta(InscripcionTGForm form, UsrBean usr) throws ClsExceptions {
		this.solicitarAlta(
			new Integer(form.getIdInstitucion()),
			new Integer(form.getIdTurno()),
			new Long(form.getIdPersona()),
			form.getObservacionesSolicitud(),
			form.getFechaValidacion(),
			form.getObservacionesValidacion(),
			new Integer(form.getTipoGuardias()),
			form.getGuardiasSel(),
			form.getIdRetencion(),
			usr);
	}
	
	/**
	 * Obtiene la cola de turno dada una fecha
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param fecha
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	public static List<LetradoInscripcion> getColaTurno(Integer idInstitucion, Integer idTurno, String fecha, boolean quitarSaltos, UsrBean usr) throws ClsExceptions {
		try {
			ScsTurnoAdm turadm = new ScsTurnoAdm(usr);
			Hashtable hashTurno = new Hashtable();
			ArrayList<LetradoInscripcion> colaLetrados = new ArrayList<LetradoInscripcion>();
			ScsInscripcionTurnoAdm insadm = new ScsInscripcionTurnoAdm(usr);
			ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
			CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);
			
			
			// obteniendo la guardia
			hashTurno.put(ScsTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
			hashTurno.put(ScsTurnoBean.C_IDTURNO, idTurno.toString());
			Vector vTurno= turadm.select(hashTurno);
			
			ScsTurnoBean beanTurno = (ScsTurnoBean) vTurno.get(0);
			
			Integer idOrdenacionColas = beanTurno.getIdOrdenacionColas();
			if(idOrdenacionColas==null)
				throw new ClsExceptions("messages.general.error");
			
			Long idPersonaUltimo = beanTurno.getIdPersonaUltimo();
			String fechaUltimo = beanTurno.getFechaSolicitudUltimo();
			ScsOrdenacionColasAdm ordenacionColasAdm = new ScsOrdenacionColasAdm(usr);
			// obteniendo ordenacion de la guardia
			String orden = ordenacionColasAdm.getOrderBy(idOrdenacionColas.toString(), usr);
			
			ScsInscripcionTurnoBean ultimoAnterior;
			
			// obteniendo ultimo apuntado de la guardia
			if (idPersonaUltimo == null)
				ultimoAnterior = null;
			else
				ultimoAnterior = new ScsInscripcionTurnoBean(idInstitucion, idTurno, idPersonaUltimo, fechaUltimo);
			
	//		obteniendo lista de letrados (ordenada)
			Vector<ScsInscripcionTurnoBean> listaLetrados = insadm.getColaTurno(idInstitucion.toString(), idTurno.toString(), fecha, orden);
			if (listaLetrados == null || listaLetrados.size()==0)
				return colaLetrados;
			
			/* FORMA ANTIGUA DE RELLENAR LA COLA DE TURNOS */
			 
			/*ScsInscripcionTurnoBean inscripcionTurno = null;
			boolean foundUltimo = false;
			Vector<LetradoInscripcion> colaAuxiliar = new Vector<LetradoInscripcion>();
			LetradoInscripcion letradoTurno;
			if(idPersonaUltimo!=null){
				for (int i = 0; i < listaLetrados.size(); i++) {
					inscripcionTurno = (ScsInscripcionTurnoBean) listaLetrados.get(i);
					letradoTurno = new LetradoInscripcion(inscripcionTurno);
					if(foundUltimo){
						//El primero que se añade es el ultimo. Depues habra que moverlo
						colaLetrados.add(letradoTurno);
					}else{
						colaAuxiliar.add(letradoTurno);
					}
					if(!foundUltimo && inscripcionTurno.getIdPersona().compareTo(idPersonaUltimo)==0){
						foundUltimo = true;
						//indexUltimo = i;
					}
				}
				colaLetrados.addAll(colaAuxiliar);
				}else{
			
			   // el idpersona ultimo es nulo con el orden que traian
				for (int i = 0; i < listaLetrados.size(); i++) {
					inscripcionTurno = (ScsInscripcionTurnoBean) listaLetrados.get(i);
					letradoTurno = new LetradoInscripcion(inscripcionTurno);
					colaLetrados.add(letradoTurno);
				}
			}*/
			
			ScsInscripcionTurnoBean punteroInscripciones = null;
			boolean foundUltimo;
			LetradoInscripcion letradoTurno;
			if (ultimoAnterior == null) {
				// si no existe ultimo colegiado, se empieza la cola desde el
				// primero en la lista
				for (int i = 0; i < listaLetrados.size(); i++) {
					punteroInscripciones = (ScsInscripcionTurnoBean) listaLetrados.get(i);
					if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE))
						colaLetrados.add(new LetradoInscripcion(punteroInscripciones));
				}
			} else {
				// ordenando la cola en funcion del idPersonaUltimo guardado
				Vector<LetradoInscripcion> colaAuxiliar = new Vector<LetradoInscripcion>();
				foundUltimo = false;
				for (int i = 0; i < listaLetrados.size(); i++) {
					punteroInscripciones = listaLetrados.get(i);
	
					// insertando en la cola si la inscripcion esta activa
					if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE)) {
						// El primero que se anyade es el siguiente al ultimo
						if (foundUltimo) {
							colaLetrados.add(new LetradoInscripcion(punteroInscripciones));
						} else {
							colaAuxiliar.add(new LetradoInscripcion(punteroInscripciones));
						}
					}
	
					// revisando si se encontro ya al ultimo
					if (!foundUltimo && punteroInscripciones.equals(ultimoAnterior))
						foundUltimo = true;
				}
				colaLetrados.addAll(colaAuxiliar);
			}
			
			
			// quitando letrados de la cola si tienen saltos
			if (quitarSaltos) {
				HashMap<Long, ArrayList<LetradoInscripcion>> personasConSaltos = saladm.getSaltos(idInstitucion, idTurno, null);
				ArrayList<LetradoInscripcion> alSaltos;
				for (Iterator iter = colaLetrados.iterator(); iter.hasNext(); ) {
					letradoTurno = (LetradoInscripcion) iter.next();
					if ( (alSaltos = personasConSaltos.get(letradoTurno.getIdPersona())) != null )
						iter.remove();
				}
			}
			
			return colaLetrados;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getColaTurno()");
		}			
	} //getColaTurno()
	
	/**
	 * Solicita el alta de una inscripcion de turno de un colegiado
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idPersona
	 * @param observacionesSolicitudAlta
	 * @param fechaValidacion
	 * @param observacionesValidacion
	 * @param tipoGuardias
	 * @param guardiasSeleccionadas
	 * @param idRetencion
	 * @param usr
	 * @throws ClsExceptions
	 */
	private void solicitarAlta (Integer idInstitucion, Integer idTurno, Long idPersona, String observacionesSolicitudAlta, String fechaValidacion, String observacionesValidacion, 
			Integer tipoGuardias, String guardiasSeleccionadas, String idRetencion, UsrBean usr) throws ClsExceptions {
		
		try {
			if (guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("") && guardiasSeleccionadas.indexOf("@")>=0){
				guardiasSeleccionadas=guardiasSeleccionadas.substring(0,guardiasSeleccionadas.lastIndexOf("@"));
			}  
			
			String guardiasSel[];
			Hashtable miHash = new Hashtable();
	
			// Preparamos la hash
			miHash.put(ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);  
			miHash.put(ScsInscripcionTurnoBean.C_IDINSTITUCION, usr.getLocation());
			miHash.put(ScsInscripcionTurnoBean.C_IDTURNO, idTurno.toString());
			miHash.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD, "sysdate");
			
			if (observacionesSolicitudAlta!=null) {
				miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, observacionesSolicitudAlta);
				
			} else {
				miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, "");
			}			
			
			if (fechaValidacion!=null && !fechaValidacion.equals("")) {
				if(!fechaValidacion.equalsIgnoreCase("sysdate") && fechaValidacion.length()==10) {
					miHash.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
				} else {
					miHash.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION, fechaValidacion);
				}			
				
				if(observacionesValidacion!=null) {
					miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
					
				} else {
					miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, "");
				}
			}
	
			// Realiza el alta de la inscripcion de turno
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);		
			boolean result = inscripcionTurnoAdm.insert(miHash);
			
			// Comprueba que se ha realizado el alta de turno y tiene guardias
			if (result && tipoGuardias!=null) {
				
				// Indica que son guardias obligatorias
				if ( tipoGuardias.intValue()==0){
						
					// Obtiene las guardias del turno
					ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(usr);
					List<ScsGuardiasTurnoBean> lGuardias = admGuardiasTurno.getGuardiasTurnos(idTurno, idInstitucion, false);
					
					// Recorre las guardias
					if (lGuardias!=null && lGuardias.size()>0){
						for (int x=0; x<lGuardias.size(); x++) {						
							ScsGuardiasTurnoBean beanGuardia = lGuardias.get(x);
							
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
							ScsInscripcionGuardiaBean beanInscripcionGuardia = new ScsInscripcionGuardiaBean();
								beanInscripcionGuardia.setIdInstitucion(new Integer(idInstitucion));					
								beanInscripcionGuardia.setIdTurno(new Integer(idTurno));					
								beanInscripcionGuardia.setIdGuardia(beanGuardia.getIdGuardia());					
								beanInscripcionGuardia.setIdPersona(Long.valueOf(idPersona));
								beanInscripcionGuardia.setFechaSuscripcion("sysdate");
							
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 
							InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);		
							
							// Introduce los datos del alta
							if (!fechaValidacion.equals("")) {
								inscripcionGuardia.setAltas(
									(String) miHash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD),
									(String) miHash.get(ScsInscripcionTurnoBean.C_FECHAVALIDACION), 
									(String) miHash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION));
								
							} else {
								inscripcionGuardia.setAltas(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, null, null);															
							}
							
							// Realiza el alta de la inscripcion de guardia
							inscripcionGuardia.solicitarAlta(usr);
						}
					}
					
				// Si no son obligatorias	
				} else {
					
					// Comprueba que tenga guardias seleccionadas
					if( guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")){
						guardiasSel=guardiasSeleccionadas.split("@");
	
						Date date = new Date();
						String fechaInscripcion= formatoFecha.format(date);
	
						// Recorre las guardias seleccionadas
						for (int i=0; i<guardiasSel.length;i++){
							try {
								fechaInscripcion = formatoFecha.format(formatoFecha.parse(fechaInscripcion));							
							} catch (Exception e) {
								fechaInscripcion = "sysdate";
							} 
							
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
							ScsInscripcionGuardiaBean beanInscripcionGuardia = new ScsInscripcionGuardiaBean();
								beanInscripcionGuardia.setIdInstitucion(new Integer(idInstitucion));					
								beanInscripcionGuardia.setIdTurno(new Integer(idTurno));					
								beanInscripcionGuardia.setIdGuardia(new Integer(guardiasSel[i]));					
								beanInscripcionGuardia.setIdPersona(Long.valueOf(idPersona));
								beanInscripcionGuardia.setFechaSuscripcion(fechaInscripcion);
							
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 
							InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);		
									
							// Introduce los datos del alta
							if (!fechaValidacion.equals("")) {
								inscripcionGuardia.setAltas(
									(String) miHash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD),
									(String) miHash.get(ScsInscripcionTurnoBean.C_FECHAVALIDACION), 
									(String) miHash.get(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION));
								
							} else {
								inscripcionGuardia.setAltas(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD, null, null);													
							}
									
							inscripcionGuardia.solicitarAlta(usr);
						}
					}
				}
			}
				
			// Si se ha incluido retencion, se da de alta.
			if(idRetencion != null) {				
				miHash = new Hashtable();
					miHash.put("IDINSTITUCION", usr.getLocation());
					miHash.put("IDPERSONA", idPersona);
					miHash.put("IDRETENCION", idRetencion);
					miHash.put("DESCRIPCION", ".");
					
					miHash.put("FECHAINICIO", "sysdate");
					// Antes estaba esta linea de codigo con una fecha fija: miHash.put("FECHAINICIO", GstDate.getApplicationFormatDate(usr.getLanguage(),"01/01/2005"));
					
				ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(usr);
				result = scsRetencionesIRPFAdm.insert(miHash);
				idRetencion=null;
			}
				
			if (!result) {
				throw new ClsExceptions("messages.inserted.error");
			}		
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitarAlta()");
		}				
	} //solicitarAlta ()
	
	/**
	 * Valida la inscripcion de un colegiado a un turno
	 * 
	 * @param fechaValidacion
	 * @param observacionesValidacion
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void validarAlta (String fechaValidacion, String observacionesValidacion, UsrBean usr) throws ClsExceptions {
		try {
			String[] clavesTurno = {ScsInscripcionTurnoBean.C_IDINSTITUCION, 
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO,
				ScsInscripcionTurnoBean.C_FECHASOLICITUD};
			
			String[] camposTurno = {ScsInscripcionTurnoBean.C_FECHAVALIDACION,
				ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			// Preparamos la hash
			Hashtable htInscTurno = new Hashtable();
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA, this.bean.getIdPersona());  
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION, this.bean.getIdInstitucion());
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDTURNO, this.bean.getIdTurno());
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			
			if (observacionesValidacion!=null) {
				htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
				
			} else {
				htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, "");
			}
			
			if(!fechaValidacion.equalsIgnoreCase("sysdate") && fechaValidacion.length()==10) {
				htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
			} else {
				htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION, fechaValidacion);
			}			
									
			// Actualiza el turno
			ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
			scsInscripcionTurnoAdm.updateDirect(htInscTurno, clavesTurno, camposTurno);
			
			
			/* ... VALIDAMOS LAS GUARDIAS ... */
			
			// Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
			List vGuardiasTurno =  (List) admInscripcionGuardia.getInscripcionesGuardias(
				this.bean.getIdInstitucion(), 
				this.bean.getIdTurno(),
				this.bean.getIdPersona(),
				null);			
			
			// Obtiene los datos de la inscripcion de turno y del turno
			ScsInscripcionTurnoBean inscripcionTurno = scsInscripcionTurnoAdm.getInscripcionTurno(
				this.bean.getIdInstitucion(),
				this.bean.getIdTurno(), 
				this.bean.getIdPersona(), 
				this.bean.getFechaSolicitud(),
				true);							
			Integer iTipoGuardias = inscripcionTurno.getTurno().getGuardias();
			
			/* Esta variable la utilizaremos para los turnos configurados como todas o ninguna
			 * Si tiene la variable a true tendra que insertar todas las guardias del turno
			 */			
			boolean bTieneAlgunaInscripcionGuardia = false;
			
			// JPT: Esto es generico para todos los tipos de guardias
			// Actualiza las guardias del turno
			if(vGuardiasTurno!=null) {
				for(int x=0; x<vGuardiasTurno.size(); x++) {
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud + FechaValidacion + ObservacionesValidacion
					InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);							
					inscripcionGuardia.setAltas(null, 
						(String) htInscTurno.get(ScsInscripcionTurnoBean.C_FECHAVALIDACION), 
						(String) htInscTurno.get(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION));
					
					// Valida la inscripcion de guardia
					inscripcionGuardia.validarAlta(usr);
					
					bTieneAlgunaInscripcionGuardia = true;
				}
			}			
			
			// Realizo este tratamiento para las guardias obligatorias (0), o las guardias todas o ninguna (1) que tengan alguna inscripcion de guardia sin estar de baja o cancelada
			if (iTipoGuardias==0 || 
					(iTipoGuardias == 1 && bTieneAlgunaInscripcionGuardia)) {
				
				// Obtiene las guardias del turno
				ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(usr);
				List<ScsGuardiasTurnoBean> lGuardias = admGuardiasTurno.getGuardiasTurnos(this.bean.getIdTurno(), this.bean.getIdInstitucion(), false);
				
				// Recorre las guardias
				if (lGuardias!=null && lGuardias.size()>0){
					for (int g=0; g<lGuardias.size(); g++) {						
						ScsGuardiasTurnoBean beanGuardia = lGuardias.get(g);
						
						// Busco si tiene inscripcion de guardia
						boolean bTieneInscripcionGuardia = false;
						if (vGuardiasTurno!=null) {
							for (int ig=0; ig<vGuardiasTurno.size() && !bTieneInscripcionGuardia; ig++) {
								
								// Obtengo la inscripcion de guardia
								ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean) vGuardiasTurno.get(ig);
								
								// Comparo si es la misma que la guardia del turno
								if (beanInscripcionGuardia.getIdGuardia().equals(beanGuardia.getIdGuardia())) {
									bTieneInscripcionGuardia = true;
								}
							}
						}
						
						// Si no tiene inscripcion de guardia la creo
						if (!bTieneInscripcionGuardia) {
						
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
							ScsInscripcionGuardiaBean beanInscripcionGuardia = new ScsInscripcionGuardiaBean();
								beanInscripcionGuardia.setIdInstitucion(this.bean.getIdInstitucion());					
								beanInscripcionGuardia.setIdTurno(this.bean.getIdTurno());					
								beanInscripcionGuardia.setIdGuardia(beanGuardia.getIdGuardia());					
								beanInscripcionGuardia.setIdPersona(this.bean.getIdPersona());
								beanInscripcionGuardia.setFechaSuscripcion(this.bean.getFechaSolicitud());
							
							// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 
							InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);
							
							// Realiza el alta de la inscripcion de guardia
							inscripcionGuardia.setAltas(
								inscripcionTurno.getObservacionesSolicitud(), 
								(String) htInscTurno.get(ScsInscripcionTurnoBean.C_FECHAVALIDACION), 
								(String) htInscTurno.get(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION));
							
							// Solicita y valida la inscripcion de guardia
							inscripcionGuardia.solicitarAlta(usr);
						}
					}
				}			
			}
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarAlta()");
		}				
	} 
	
	/**
	 * Modificar la fecha de validacion de un turno, por la fecha efectiva
	 * 
	 * @param fechaValidacion
	 * @param observacionesValidacion
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void modificarFechaValidacion(String fechaValidacion, String observacionesValidacion, UsrBean usr) throws ClsExceptions {
		try {
			// Preparamos los datos para la inscripcion de turno
			String[] clavesTurno = {ScsInscripcionTurnoBean.C_IDINSTITUCION,
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO,
				ScsInscripcionTurnoBean.C_FECHASOLICITUD};
			
			String[] camposTurno = {ScsInscripcionTurnoBean.C_FECHAVALIDACION,
				ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			Hashtable htInscTurno = new Hashtable();
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA, this.bean.getIdPersona());  
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION, this.bean.getIdInstitucion());
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDTURNO, this.bean.getIdTurno());
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());		
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
			
			if (observacionesValidacion!=null) {
				htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
				
			} else {
				htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION, "");
			}
			
			// Obtenemos las inscripciones de guardias de una inscripcion de turno, que se tienen que modificar al cambiar la fecha efectiva de validacion de una inscripcion de turno
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);		
			List vGuardiasTurno = (List) admInscripcionGuardia.getInscripcionesGuardiasTurnoFechaEfectiva(
					this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),
					this.bean.getIdPersona(),
					this.bean.getFechaValidacion(),
					true);	
									
			// Actualiza la inscripcio de turno
			ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
			scsInscripcionTurnoAdm.updateDirect(htInscTurno, clavesTurno, camposTurno);
			
			// Recorre y actualiza las guardias
			if(vGuardiasTurno!=null) {
				
				// Preparamos los datos para la inscripcion de guardia
				String[] clavesGuardia = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO,
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
				String[] camposGuardia = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,
					ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,
					MasterBean.C_USUMODIFICACION,
					MasterBean.C_FECHAMODIFICACION};
				
				Hashtable htInscGuardia = new Hashtable();
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, this.bean.getIdInstitucion());
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA, this.bean.getIdPersona());
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDTURNO, this.bean.getIdTurno());		
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
				
				if (observacionesValidacion!=null) {
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
					
				} else {
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, "");
				}							
				
				for(int x=0; x<vGuardiasTurno.size(); x++) {
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, beanInscripcionGuardia.getIdGuardia());	
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, beanInscripcionGuardia.getFechaSuscripcion());	
				
					ScsInscripcionGuardiaAdm scsInscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(usr);
					scsInscripcionGuardiaAdm.updateDirect(htInscGuardia, clavesGuardia, camposGuardia);
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar modificarFechaValidacion()");
		}				
	} 
	
	/**
	 * Solicita la baja de una inscripcion de turno de un colegiado
	 * 
	 * @param fechaSolicitudBaja
	 * @param observacionesSolicitudBaja
	 * @param usr
	 * @param tieneFechaBaja
	 * @throws ClsExceptions
	 */
	private void solicitarBaja (String fechaSolicitudBaja, String observacionesSolicitudBaja, UsrBean usr, boolean tieneFechaBaja) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
			
			//insertando solicitud de baja
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(usr);
			Vector<ScsInscripcionTurnoBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
			
			boolean tieneBajaDenegada = false;
			if (v != null && v.size() == 1) {
				ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
	
				if(inscripcionBean.getFechaDenegacion()!=null && !inscripcionBean.getFechaDenegacion().equals("")){
					tieneBajaDenegada = true;
					String observacionDenegacion = inscripcionBean.getObservacionesDenegacion()==null?"":inscripcionBean.getObservacionesDenegacion();
					observacionesSolicitudBaja += "[Fecha denegación previa:"+GstDate.getFormatedDateShort("", inscripcionBean.getFechaDenegacion()) +"]";
					observacionesSolicitudBaja += "[Observaciones denegación previa:"+observacionDenegacion+"]";						
				}
				
				inscripcionBean.setFechaSolicitudBaja(fechaSolicitudBaja);							
				String observacionesPrevias = inscripcionBean.getObservacionesBaja()==null || inscripcionBean.getObservacionesBaja().equals("") ? "" : "{" + inscripcionBean.getObservacionesBaja() + "}";
				inscripcionBean.setObservacionesBaja(observacionesSolicitudBaja+observacionesPrevias);
					
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al realizar la solicitud de baja en el turno");
				}
			}
			
			// Anula la denegacion del turno
			if(tieneBajaDenegada)
				denegarBajaInscripcionTurno(null, null, usr);
			
			// Solo hacemos la solicitud de baja de las guardias del turno activas, si tiene fecha de baja
			if (tieneFechaBaja) {
				
				// Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja
				ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
				List vGuardiasTurno =  (List) admInscripcionGuardia.getInscripcionesGuardias(
					this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),
					this.bean.getIdPersona(),
					null);
				
				if(vGuardiasTurno!=null && vGuardiasTurno.size()>0) {
					for(int x=0; x<vGuardiasTurno.size(); x++) {
						ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
						
						// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud + fechaSolicitudBaja + observacionesFechaSolicitudBaja
						InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);						
						inscripcionGuardia.setBajas(observacionesSolicitudBaja, fechaSolicitudBaja, null, null);
						
						inscripcionGuardia.solicitarBaja(usr, null);
					}
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitarBaja()");
		}				
	} //solicitarBaja ()
	
	/**
	 * Solicita la baja de una inscripcion de turno de un colegiado
	 * @param fechaSolicitudBaja
	 * @param observacionesSolicitudBaja
	 * @param fechaBaja
	 * @param observacionesValBaja
	 * @param fechaValidacion
	 * @param cancelarSyC
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void solicitarBaja (String fechaSolicitudBaja, String observacionesSolicitudBaja, String fechaBaja, String observacionesValBaja, String fechaValidacion, String cancelarSyC, UsrBean usr) throws ClsExceptions {
		try {
			
			//el proceso de solicitar baja valida las que no es necesario la validacion de las inscripciones
			if(fechaBaja!=null && !fechaBaja.equals("")) {
				this.solicitarBaja(fechaSolicitudBaja, observacionesSolicitudBaja, usr, true);
				this.validarBaja(fechaBaja, fechaValidacion, observacionesValBaja, null, usr);
				
			} else {
				this.solicitarBaja(fechaSolicitudBaja, observacionesSolicitudBaja, usr, false);
			}
			
			if(cancelarSyC!=null && cancelarSyC.equalsIgnoreCase("T")){
				//String actuali = miForm.getTipoActualizacionSyC();
					
				//saltos y compensaciones
				ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
				//ArrayList<ScsSaltosCompensacionesBean> vSaldosyCompensTurno =  (ArrayList<ScsSaltosCompensacionesBean>) admSaltosYCompensacions.getSaltosCompensaciones(this.bean.getIdInstitucion(), 
				//this.bean.getIdTurno(),this.bean.getIdPersona(), null);
				saladm.updateSaltosCompensacionesBajaTurno(
					this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),
					this.bean.getIdPersona(), 
					null, 
					cancelarSyC);
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitarBaja()");
		}				
	} //solicitarBaja ()

	/**
	 * Deniega la inscripcion de un colegiado a un turno
	 * 
	 * @param fechaDenegacion
	 * @param observacionesDenegacion
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void denegarInscripcionTurno (String fechaDenegacion, String observacionesDenegacion, UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
			
			//insertando baja
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			
			ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(usr);
			Vector<ScsInscripcionTurnoBean> v = inscripcionAdm.selectByPKForUpdate(inscripcionHash);
			
			if (v != null && v.size() == 1) {
				ScsInscripcionTurnoBean inscripcionBean = (ScsInscripcionTurnoBean) v.get(0);
				
				if(!fechaDenegacion.equalsIgnoreCase("sysdate") && fechaDenegacion.length()==10) {
					inscripcionBean.setFechaDenegacion(GstDate.getApplicationFormatDate(usr.getLanguage(), fechaDenegacion));
				} else {
					inscripcionBean.setFechaDenegacion(fechaDenegacion);
				}					
				
				inscripcionBean.setObservacionesDenegacion(observacionesDenegacion);
				
				if (! inscripcionAdm.update(inscripcionBean)) {
					throw new ClsExceptions("Error al denegar el alta en el turno");
				}
				
				
				// Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja
				ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
				List vGuardiasTurno =  (List) admInscripcionGuardia.getInscripcionesGuardias(
					this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),
					this.bean.getIdPersona(),
					null);
					
				if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
					for(int x=0; x<vGuardiasTurno.size(); x++) {
						ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean) vGuardiasTurno.get(x);
						
						// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud + Observaciones Denegacion + FechaDenegacion
						InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);							
						inscripcionGuardia.setDenegacion(observacionesDenegacion, fechaDenegacion);
						
						inscripcionGuardia.denegarAltaGuardia(usr);
					}
				}				
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar denegarInscripcionTurno()");
		}				
	} //denegarInscripcionTurno ()
	
	/**
	 * Deniega la baja de la inscripcion del colegiado en el turno
	 * 
	 * @param fechaDenegacion
	 * @param observacionesDenegacion
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void denegarBajaInscripcionTurno (String fechaDenegacion, String observacionesDenegacion, UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
			
			//denegando baja turno
			String[] claves = {ScsInscripcionTurnoBean.C_IDINSTITUCION,
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO, 
				ScsInscripcionTurnoBean.C_FECHASOLICITUD};
			
			String[] campos = {
				ScsInscripcionTurnoBean.C_FECHADENEGACION, 
				ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};			
							
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			
			if(fechaDenegacion!=null){
				if(!fechaDenegacion.equalsIgnoreCase("sysdate") && fechaDenegacion.length()==10) {
					UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHADENEGACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaDenegacion));
				} else {
					UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHADENEGACION, fechaDenegacion);
				}	
				
				if (observacionesDenegacion!=null) {
					UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION, observacionesDenegacion);
					
				} else {
					UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_OBSERVACIONESDENEGACION, "");
				}	
			}			
			
			ScsInscripcionTurnoAdm insturno = new ScsInscripcionTurnoAdm(usr);
			insturno.updateDirect(inscripcionHash,claves,campos);
			
			/*
			 * JPT: Al denegar una baja pendiente de turno, no se deniegan las guardias del turno 

			// Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
			List vGuardiasTurno =  (List) admInscripcionGuardia.getInscripcionesGuardias(
				this.bean.getIdInstitucion(), 
				this.bean.getIdTurno(),
				this.bean.getIdPersona(),
				null);
	
			if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
				for(int x=0; x<vGuardiasTurno.size(); x++) {
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 
					InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);	
					
					inscripcionGuardia.setDenegacion(observacionesDenegacion,fechaDenegacion);
					inscripcionGuardia.denegarBajaGuardia(usr);
				}
			}
			*/
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar denegarBajaInscripcionTurno()");
		}			
	} //denegarBajaInscripcionTurno ()
	
	/**
	 * Valida la baja de la inscripcion del colegiado en el turno
	 * 
	 * @param fechaBaja
	 * @param fechaValidacion
	 * @param obsValBaja
	 * @param bajaSyC
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void validarBaja (String fechaBaja, String fechaValidacion, String obsValBaja, String bajaSyC, UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Long idPersona = this.bean.getIdPersona();
			
			//comprobando si es el siguiente de la cola
			//Solo se comprueba lo de la cola cuando no sea una denegacion de alta, esto es cuando no haya estado validado
			if(fechaValidacion!=null){
				Hashtable<String, Object> hashTurno = new Hashtable<String, Object>();
				hashTurno.put(ScsTurnoBean.C_IDINSTITUCION, idInstitucion);
				hashTurno.put(ScsTurnoBean.C_IDTURNO, idTurno);
				Vector vTurnos = new ScsTurnoAdm(usr).select(hashTurno);
				Long idPersonaUltimoTurno = null;
				
				if(vTurnos!=null && vTurnos.size()>0)
					idPersonaUltimoTurno = ((ScsTurnoBean)vTurnos.get(0)).getIdPersonaUltimo();				
					
				if (idPersonaUltimoTurno!=null &&  idPersonaUltimoTurno.equals(idPersona)) {
					List<LetradoInscripcion> colaLetrados = InscripcionTurno.getColaTurno(idInstitucion,idTurno,"sysdate",false,usr);
					
					if (colaLetrados.get(0) != null && !colaLetrados.isEmpty()) {
						int tamanyoCola = colaLetrados.size();
						//Long ultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-1)).getIdPersona();
						//el letrado a dar de baja es el primero en la cola:
						//asi que hay que poner al anterior como ultimo en la cola
						Long penultimoDeLaCola;
						String fechaSusc_penultimo;
						
						if (tamanyoCola == 1){
							penultimoDeLaCola = null;
							fechaSusc_penultimo = null;
							
						} else{
							penultimoDeLaCola = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getIdPersona();
							fechaSusc_penultimo = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getInscripcionTurno().getFechaSolicitud();
						}
						ScsTurnoAdm turnoAdm = new ScsTurnoAdm(usr);
						turnoAdm.cambiarUltimoCola(idInstitucion, idTurno, penultimoDeLaCola, fechaSusc_penultimo);
					}
				}
			}
			
			//insertando baja
			Hashtable<String, Object> inscripcionHash = new Hashtable<String, Object>();
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_IDPERSONA, idPersona);			
			UtilidadesHash.set(inscripcionHash, ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());
			
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
			Vector<ScsInscripcionTurnoBean> v = inscripcionTurnoAdm.selectByPKForUpdate(inscripcionHash);
			
			if (v != null && v.size() == 1) {
				ScsInscripcionTurnoBean inscripcionTurno = (ScsInscripcionTurnoBean) v.get(0);
				
				if(!fechaBaja.equalsIgnoreCase("sysdate") && fechaBaja.length()==10) {
					inscripcionTurno.setFechaBaja(GstDate.getApplicationFormatDate(usr.getLanguage(), fechaBaja));
				} else {
					inscripcionTurno.setFechaBaja(fechaBaja);
				}
				
				inscripcionTurno.setObservacionesValBaja(obsValBaja);
				
				if (!inscripcionTurnoAdm.update(inscripcionTurno)) {
					throw new ClsExceptions("Error al realizar la baja en el turno");
				}
				
				
				// Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja
				ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
				List vGuardiasTurno =  (List) admInscripcionGuardia.getInscripcionesGuardias(
						this.bean.getIdInstitucion(), 
						this.bean.getIdTurno(), 
						this.bean.getIdPersona(), 
						null);			
				
				if(vGuardiasTurno!=null && vGuardiasTurno.size()>0){
					for(int x=0; x<vGuardiasTurno.size(); x++) {
						ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
						
						// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud + fechaBaja + observacionesBaja
						InscripcionGuardia inscripcionGuardia = new InscripcionGuardia(beanInscripcionGuardia);																
						
						if (beanInscripcionGuardia.getFechaSolicitudBaja() != null && !beanInscripcionGuardia.getFechaSolicitudBaja().equals("")) {
							inscripcionGuardia.setBajas(null, null, fechaBaja, obsValBaja);
							inscripcionGuardia.validarBaja(usr, null);
							
						} else {
							inscripcionGuardia.setBajas(inscripcionTurno.getObservacionesBaja(), inscripcionTurno.getFechaSolicitudBaja(), fechaBaja, obsValBaja);						
							inscripcionGuardia.solicitarBaja(usr, null);
						}											
					}
				}				
			}
	
			if(bajaSyC!=null && bajaSyC.equalsIgnoreCase("T")){
				//String actuali = miForm.getTipoActualizacionSyC();
				
				//saltos y compensaciones
				ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
				//ArrayList<ScsSaltosCompensacionesBean> vSaldosyCompensTurno =  (ArrayList<ScsSaltosCompensacionesBean>) admSaltosYCompensacions.getSaltosCompensaciones(this.bean.getIdInstitucion(), 
				//this.bean.getIdTurno(),this.bean.getIdPersona(), null);
				saladm.updateSaltosCompensacionesBajaTurno(this.bean.getIdInstitucion(), this.bean.getIdTurno(),this.bean.getIdPersona(), null, bajaSyC);
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarBaja()");
		}			
	} //validarBaja ()
	
	/**
	 * Modificar la fecha de baja de un turno, por la fecha efectiva
	 * @param fechaBaja
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void modificarFechaBaja (String fechaBaja, String observacionesBaja, UsrBean usr) throws ClsExceptions {
		try {
			// Preparamos los datos para la inscripcion de turno
			String[] clavesTurno = {ScsInscripcionTurnoBean.C_IDINSTITUCION,
				ScsInscripcionTurnoBean.C_IDPERSONA, 
				ScsInscripcionTurnoBean.C_IDTURNO,
				ScsInscripcionTurnoBean.C_FECHASOLICITUD};
			
			String[] camposTurno = {ScsInscripcionTurnoBean.C_FECHABAJA,
				ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			Hashtable htInscTurno = new Hashtable();
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDPERSONA, this.bean.getIdPersona());  
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDINSTITUCION, this.bean.getIdInstitucion());
			htInscTurno.put(ScsInscripcionTurnoBean.C_IDTURNO, this.bean.getIdTurno());
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHASOLICITUD, this.bean.getFechaSolicitud());		
			htInscTurno.put(ScsInscripcionTurnoBean.C_FECHABAJA, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaBaja));
			
			if (observacionesBaja!=null) {
				htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA, observacionesBaja);
				
			} else {
				htInscTurno.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALBAJA, "");
			}
			
			// Obtenemos las inscripciones de guardias de una inscripcion de turno, que se tienen que modificar al cambiar la fecha efectiva de validacion de una inscripcion de turno
			ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);		
			List vGuardiasTurno = (List) admInscripcionGuardia.getInscripcionesGuardiasTurnoFechaEfectiva(
					this.bean.getIdInstitucion(), 
					this.bean.getIdTurno(),
					this.bean.getIdPersona(),
					this.bean.getFechaBaja(),
					false);					
			
			// Actualiza la inscripcio de turno
			ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
			scsInscripcionTurnoAdm.updateDirect(htInscTurno, clavesTurno, camposTurno);
			
			// Recorre y actualiza las guardias
			if(vGuardiasTurno!=null) {
				
				// Preparamos los datos para la inscripcion de guardia
				String[] clavesGuardia = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO,
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
				String[] camposGuardia = {ScsInscripcionGuardiaBean.C_FECHABAJA,
					ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA,
					MasterBean.C_USUMODIFICACION,
					MasterBean.C_FECHAMODIFICACION};
				
				Hashtable htInscGuardia = new Hashtable();
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, this.bean.getIdInstitucion());
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA, this.bean.getIdPersona());
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDTURNO, this.bean.getIdTurno());		
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHABAJA, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaBaja));
				
				if (observacionesBaja!=null) {
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, observacionesBaja);
				
				} else {
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, "");
				}
				
				ScsInscripcionGuardiaAdm scsInscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(usr);
				
				for(int x=0; x<vGuardiasTurno.size(); x++) {
					ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean)vGuardiasTurno.get(x);
					
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, beanInscripcionGuardia.getIdGuardia());	
					htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, beanInscripcionGuardia.getFechaSuscripcion());	
					
					scsInscripcionGuardiaAdm.updateDirect(htInscGuardia, clavesGuardia, camposGuardia);
				}
			}
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar modificarFechaBaja()");
		}			
	}
}