package com.siga.gratuita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.MasterBean;
import com.siga.beans.ScsGrupoGuardiaColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

public class InscripcionGuardia {
	////////////////////
	// ATRIBUTOS
	////////////////////
	private ScsInscripcionGuardiaBean bean = null;	
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");	
	
	////////////////////
	// CONSTRUCTORES
	////////////////////
	/**
	 * Constructor 
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idPersona
	 * @param fechaSolicitudAlta
	 */
	private InscripcionGuardia (Integer idInstitucion, Integer idTurno, Integer idGuardia, Long idPersona, String fechaSolicitudAlta) {
		this.bean = new ScsInscripcionGuardiaBean();
		this.bean.setIdInstitucion(idInstitucion);
		this.bean.setIdTurno(idTurno);
		this.bean.setIdGuardia(idGuardia);
		this.bean.setIdPersona(idPersona);
		this.bean.setFechaSuscripcion(fechaSolicitudAlta);
	} //InscripcionGuardia ()
	
	/**
	 * Constructor
	 * 
	 * @param bean
	 */
	public InscripcionGuardia (ScsInscripcionGuardiaBean bean) {
		this.bean = bean;
	} //InscripcionGuardia ()	
	
	////////////////////
	// GETTERS Y SETTERS
	////////////////////
	
	public ScsInscripcionGuardiaBean getBean() { 
		return bean; 
	}
	
	public void setBean(ScsInscripcionGuardiaBean bean) { 
		this.bean = bean; 
	}
	
	public void setBajas(String obsSolicitudBaja, String fechaSolicitudBaja, String fechaBaja, String observacionesValBaja){
		this.bean.setObservacionesBaja(obsSolicitudBaja);
		this.bean.setObservacionesValBaja(observacionesValBaja);
		this.bean.setFechaSolicitudBaja(fechaSolicitudBaja);
		this.bean.setFechaBaja(fechaBaja);		
	}
	
	public void setDenegacion(String obsDenegacion, String fechaDenegacion) {
		this.bean.setObservacionesDenegacion(obsDenegacion);
		this.bean.setFechaDenegacion(fechaDenegacion);		
	}
	
	public void setAltas(String obsSolicitud, String fechaValidacion, String observacionesValidacion) {
		this.bean.setObservacionesSuscripcion(obsSolicitud);
		this.bean.setFechaValidacion(fechaValidacion);
		this.bean.setObservacionesValidacion(observacionesValidacion);		
	}
	
	public void setDatosGrupo(String numeroGrupo, Integer ordenGrupo) {
		this.bean.setNumeroGrupo(numeroGrupo);
		this.bean.setOrdenGrupo(ordenGrupo);		
	}		
	
	////////////////////
	// METODOS DE CLASE
	////////////////////
	
	/**
	 * Obtiene la cola de una guardia
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param fechaInicio
	 * @param fechaFin
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	public static ArrayList<LetradoInscripcion> getColaGuardia(Integer idInstitucion, Integer idTurno, Integer idGuardia, String fechaInicio, String fechaFin, UsrBean usr) throws ClsExceptions {
		try {
			// Variables para navegacion por la cola
			ScsInscripcionGuardiaBean ultimoAnterior;
			ScsInscripcionGuardiaBean punteroInscripciones;
			boolean foundUltimo;
			
			// Variables de la guardia
			ScsGuardiasTurnoBean beanGuardia;
			Integer idOrdenacionColas;
			String orden;
			boolean porGrupos;
			Long idPersonaUltimo, idGrupoGuardiaUltimo;
			String fechaSuscripcionUltimo;
	
			// Controles
			ScsGuardiasTurnoAdm guaadm = new ScsGuardiasTurnoAdm(usr);
			ScsInscripcionGuardiaAdm insadm = new ScsInscripcionGuardiaAdm(usr);
			//TODO Descomentar la siguiente linea (ya que se comento solo para entregar incidencia INC_07825_SIGA urgente:
			// ScsOrdenacionColasAdm ordadm = new ScsOrdenacionColasAdm(usr);
			CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);
			ArrayList<LetradoInscripcion> colaLetrados = new ArrayList<LetradoInscripcion>();
	
			//Actualizar cola guardia
			ScsGrupoGuardiaColegiadoAdm admGrupoGuardia = new ScsGrupoGuardiaColegiadoAdm(usr);
			admGrupoGuardia.actualizarColaGuardia(idInstitucion, idTurno, idGuardia);
			
			// obteniendo la guardia
			Hashtable hashGuardia = new Hashtable();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
			if (idGuardia!=null)
				hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
			Vector vGuardia = guaadm.select(hashGuardia);
			beanGuardia = (ScsGuardiasTurnoBean) vGuardia.get(0);
			porGrupos = beanGuardia.getPorGrupos().equals(ClsConstants.DB_TRUE);
			idOrdenacionColas = beanGuardia.getIdOrdenacionColas();
			idPersonaUltimo = beanGuardia.getIdPersona_Ultimo();
			idGrupoGuardiaUltimo = beanGuardia.getIdGrupoGuardiaColegiado_Ultimo();
			fechaSuscripcionUltimo = beanGuardia.getFechaSuscripcion_Ultimo();
			
			// obteniendo ordenacion de la guardia
			if (idOrdenacionColas == null)
				throw new ClsExceptions("messages.general.error");
			ScsOrdenacionColasAdm ordenacionColasAdm = new ScsOrdenacionColasAdm(usr);
			//TODO Descomentar la siguiente linea y borrar lo anyadido (ya que se comento solo para entregar incidencia INC_07825_SIGA urgente):
			orden = porGrupos ? " numeroGrupo, ordengrupo" : /* Anyadido: */ ordenacionColasAdm.getOrderBy(idOrdenacionColas.toString(), usr); // ordadm.getOrderBy(idOrdenacionColas.toString(), usr);
	
			// obteniendo ultimo apuntado de la guardia
			if (idPersonaUltimo == null)
				ultimoAnterior = null;
			else
				ultimoAnterior = new ScsInscripcionGuardiaBean(idInstitucion, idTurno, idGuardia, idPersonaUltimo, fechaSuscripcionUltimo,idGrupoGuardiaUltimo);
	
			// obteniendo lista de letrados (ordenada)
			Vector<ScsInscripcionGuardiaBean> listaLetrados = null;
			if(idGuardia!=null) {
				 listaLetrados = insadm.getColaGuardia(idInstitucion.toString(), 
					idTurno.toString(), idGuardia.toString(), fechaInicio, fechaFin, porGrupos, orden);
			}else{
				 listaLetrados = insadm.getColaGuardia(idInstitucion.toString(), 
						idTurno.toString(), null, fechaInicio, fechaFin, porGrupos, orden);
			}	
			if (listaLetrados == null || listaLetrados.size() == 0)
				return colaLetrados;
			
			if (ultimoAnterior == null) {
				// si no existe ultimo colegiado, se empieza la cola desde el primero en la lista
				for (int i = 0; i < listaLetrados.size(); i++) {
					punteroInscripciones = (ScsInscripcionGuardiaBean) listaLetrados.get(i);
					if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE))
						colaLetrados.add(new LetradoInscripcion(punteroInscripciones));
				}
			}
			else {
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
	
			// usando saltos si es necesario (en guardias no)
	
			return colaLetrados;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getColaGuardia()");
		}			
	} // getColaGuardia()

	/**
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idGrupoGuardia
	 * @param saltoCompensacion
	 * @param idSaltoCompensacionGrupo
	 * @param fechaGuardia
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	public static ArrayList<LetradoInscripcion> getLetradosGrupo(Integer idInstitucion, Integer idTurno, Integer idGuardia, Long idGrupoGuardia, String saltoCompensacion, String idSaltoCompensacionGrupo, String fechaGuardia, UsrBean usr) throws ClsExceptions {
		try {
			// Controles
			ScsGuardiasTurnoAdm guaadm = new ScsGuardiasTurnoAdm(usr);
			ScsInscripcionGuardiaAdm insadm = new ScsInscripcionGuardiaAdm(usr);
			CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);
	
			// Variables
			Vector<ScsInscripcionGuardiaBean> vectorLetrados;
			ArrayList<LetradoInscripcion> listaLetrados = new ArrayList<LetradoInscripcion>();
			LetradoInscripcion letradoGuardia;
			ScsInscripcionGuardiaBean inscripcionGuardia;
	
			// obteniendo la guardia
			Hashtable hashGuardia = new Hashtable();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
			Vector vGuardia = guaadm.select(hashGuardia);
			ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) vGuardia.get(0);
	
			// obteniendo lista de letrados
			vectorLetrados = insadm.getLetradosGrupo(idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), idGrupoGuardia.toString(),fechaGuardia);
			if (vectorLetrados == null || vectorLetrados.size() == 0)
				return null;
	
			// obteniendo LetradoGuardia's
			listaLetrados = new ArrayList<LetradoInscripcion>();
			for (int i = 0; i < vectorLetrados.size(); i++) {
				inscripcionGuardia = (ScsInscripcionGuardiaBean) vectorLetrados.get(i);
				letradoGuardia = new LetradoInscripcion(inscripcionGuardia);
				if (saltoCompensacion != null) {
					letradoGuardia.setSaltoCompensacion(saltoCompensacion);
					letradoGuardia.setIdSaltoCompensacionGrupo(idSaltoCompensacionGrupo);
				}
				listaLetrados.add(letradoGuardia);
			}
	
			return listaLetrados;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar getLetradosGrupo()");
		}				
	} // getLetradosGrupo()
	
	/**
	 * Solicita el alta de una inscripcion de guardia de un colegiado
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void solicitarAlta (UsrBean usr) throws ClsExceptions {		
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechSubscripcion = this.bean.getFechaSuscripcion();
			String observacionesSuscripcion = this.bean.getObservacionesSuscripcion();
			String fechaValidacion = this.bean.getFechaValidacion();
			String obsValidacion = this.bean.getObservacionesValidacion();
			// solamente para el caso en el que se solicita y valida el alta de una guardia
			//en fecha con inscripcon en turno con fecha de baja
			String fechaBaja = this.bean.getFechaBaja();
			
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
						
			if(fechSubscripcion.equalsIgnoreCase("sysdate")) {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, GstDate.getHoyJava());
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechSubscripcion);				
			}	
			
			if (observacionesSuscripcion!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION, observacionesSuscripcion);
			else
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION, "");
			
			if(fechaValidacion!=null && !fechaValidacion.equals("")){
				if(!fechaValidacion.equalsIgnoreCase("sysdate") && fechaValidacion.length()==10) {
					laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
				} else {
					laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, fechaValidacion);
				}					
				
				if (obsValidacion!=null)
					laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, obsValidacion);
				else
					laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, "");
			}		
			
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			
			
			// JPT: Siempre viene idGuardia a este metodo, pero lo dejo por si se necesita en el futuro
			if(idGuardia==null){
				
				// Obtiene las guardias del turno
				ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(usr);
				List<ScsGuardiasTurnoBean> lGuardias = admGuardiasTurno.getGuardiasTurnos(idTurno, idInstitucion, false);
				
				// Recorre las guardias
				if (lGuardias!=null && lGuardias.size()>0){
					for (int g=0; g<lGuardias.size(); g++) {						
						ScsGuardiasTurnoBean beanGuardia = lGuardias.get(g);
						
						Hashtable htGuardia = admGuardiasTurno.beanToHashTable(beanGuardia);
						laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, new Integer((String)htGuardia.get(ScsInscripcionGuardiaBean.C_IDGUARDIA)));
						insguardia.insert(laHash);
					}
				}
				
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
				insguardia.insert(laHash);
				
				if(this.getBean().getNumeroGrupo()!=null){
					ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);
					admGrupoColegiado.insertaGrupoGuardiaColegiado(
						this.getBean().getIdInstitucion(),
						this.getBean().getIdTurno(),
						this.getBean().getIdGuardia(),
						this.getBean().getIdPersona(), 
						(String) laHash.get(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION), 
						this.getBean().getNumeroGrupo(), 
						this.getBean().getOrdenGrupo().toString(), 
						null);
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitarAlta()");
		}				
	}
	
	/**
	 * Valida la inscripcion de un colegiado a una guardia
	 * 
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void validarAlta (UsrBean usr) throws ClsExceptions {	
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();
			String fechaValidacion = this.bean.getFechaValidacion();
			String observacionesValidacion = this.bean.getObservacionesValidacion();					
					
			// solamente para el caso en el que se solicita y valida el alta de una guardia en fecha con inscripcon en turno con fecha de baja
			String fechaBaja = this.bean.getFechaBaja();
				
			Hashtable laHash = new Hashtable();		
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);					
			
			if (observacionesValidacion!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
			else
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, "");
			
			if(!fechaValidacion.equalsIgnoreCase("sysdate") && fechaValidacion.length()==10) {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, fechaValidacion);
			}				
		
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}
	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
			if(this.getBean().getNumeroGrupo()!=null){
				ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);								
				admGrupoColegiado.insertaGrupoGuardiaColegiado(
					this.getBean().getIdInstitucion(),
					this.getBean().getIdTurno(),
					this.getBean().getIdGuardia(),
					this.getBean().getIdPersona(), 
					fechaSuscripcion, 
					this.getBean().getNumeroGrupo(), 
					this.getBean().getOrdenGrupo().toString(), 
					null);
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarAlta()");
		}				
	}
	
	/**
	 * Modificar la fecha de validacion de una guardia, por la fecha efectiva
	 * 
	 * @param fechaValidacion
	 * @param observacionesValidacion
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void modificarFechaValidacion (String fechaValidacion, String observacionesValidacion, UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();			
			
			// Preparamos los datos para la inscripcion de guardia
			String[] clavesGuardia = null;
			
			if(idGuardia!=null){
				clavesGuardia = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				clavesGuardia = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,				
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};		
			}
			
			String[] camposGuardia = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};		
			
			Hashtable htInscGuardia = new Hashtable();
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);
			
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			
			if (observacionesValidacion!=null) {
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
			} else {
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, "");
			}
	
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(htInscGuardia, clavesGuardia, camposGuardia);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar modificarFechaValidacion()");
		}				
	} 
		
	/**
	 * Modificar la fecha de baja de una guardia, por la fecha efectiva
	 * 
	 * @param fechaValidacion
	 * @param observacionesValidacion
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void modificarFechaBaja (String fechaBaja, String observacionesBaja, UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();	
			
			// Preparamos los datos para la inscripcion de guardia
			String[] clavesGuardia = null;
			
			if(idGuardia!=null){
				clavesGuardia = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				clavesGuardia = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,				
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};		
			}	
			
			String[] camposGuardia = {ScsInscripcionGuardiaBean.C_FECHABAJA,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};				
			
			Hashtable htInscGuardia = new Hashtable();
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);
			
			htInscGuardia.put(ScsInscripcionGuardiaBean.C_FECHABAJA, GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
			
			if (observacionesBaja!=null) {
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, observacionesBaja);
			} else {
				htInscGuardia.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, "");
			}			
				
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(htInscGuardia, clavesGuardia, camposGuardia);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar modificarFechaBaja()");
		}			
	} 
	
	/**
	 * Solicita la baja de una inscripcion de guardia de un colegiado
	 * 
	 * @param usr
	 * @param bajaSyC
	 * @throws ClsExceptions
	 */
	public void solicitarBaja (UsrBean usr, String bajaSyC) throws ClsExceptions {
		try {
			this.solicitaBaja(usr);
			
			if (this.bean.getFechaBaja()!=null && !this.bean.getFechaBaja().equals("")){
				
				if (this.bean.getIdGuardia()==null){
					
					// Obtiene las inscripciones de guardia de una inscripcion de turno que no estan de baja
					ScsInscripcionGuardiaAdm admInscripcionGuardia = new ScsInscripcionGuardiaAdm(usr);
					List<ScsInscripcionGuardiaBean> vGuardiasTurno = admInscripcionGuardia.getInscripcionesGuardias(
						this.bean.getIdInstitucion(), 
						this.bean.getIdTurno(),
						this.bean.getIdPersona(),
						null);	
					
					// Recorre las guardias
					if (vGuardiasTurno!=null && vGuardiasTurno.size()>0){
						for (int g=0; g<vGuardiasTurno.size(); g++) {						
							ScsInscripcionGuardiaBean beanInscripcionGuardia = (ScsInscripcionGuardiaBean) vGuardiasTurno.get(g);
							
							this.bean.setIdGuardia(beanInscripcionGuardia.getIdGuardia());
							this.bean.setFechaSuscripcion(beanInscripcionGuardia.getFechaSuscripcion());
							
							validarBaja(usr, bajaSyC);
						}
					}
					
				} else {
					validarBaja(usr, bajaSyC);
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitarBaja()");
		}					
	}
	
	/**
	 * Solicita la baja de una inscripcion de guardia de un colegiado
	 * @param usr
	 * @throws ClsExceptions
	 */
	private void solicitaBaja (UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechSubscripcion = this.bean.getFechaSuscripcion();
			String fechSolicitudBaja = this.bean.getFechaSolicitudBaja();
			String observacionesSolicitudBaja = this.bean.getObservacionesBaja();
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechSubscripcion);						
			
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};	
				
			} else {
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};			
			}
			
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};			
			
			ScsInscripcionGuardiaAdm inscripcionAdm = new ScsInscripcionGuardiaAdm(usr);		
			Vector<ScsInscripcionGuardiaBean> v = inscripcionAdm.select(laHash);
			
			if(fechSolicitudBaja.equalsIgnoreCase("sysdate")) {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA, GstDate.getHoyJava());
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA, fechSolicitudBaja);				
			}	
			
			boolean tieneBajaDenegada = false;
			if (v != null && v.size() >0 ) {
				ScsInscripcionGuardiaBean inscripcionBean = (ScsInscripcionGuardiaBean) v.get(0);
				
				if (inscripcionBean.getFechaDenegacion()!=null && !inscripcionBean.getFechaDenegacion().equals("")){
					tieneBajaDenegada = true;
					String observacionDenegacion = inscripcionBean.getObservacionesDenegacion()==null?"":inscripcionBean.getObservacionesDenegacion();
					observacionesSolicitudBaja += "[Fecha denegación previa:"+GstDate.getFormatedDateShort("", inscripcionBean.getFechaDenegacion()) +"]";
					observacionesSolicitudBaja += "[Observaciones denegación previa:"+observacionDenegacion+"]";					
				}
					
				String observacionesPrevias = inscripcionBean.getObservacionesBaja()==null ? "" : "{" + inscripcionBean.getObservacionesBaja() + "}";
				inscripcionBean.setObservacionesBaja(observacionesSolicitudBaja+observacionesPrevias);
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,observacionesSolicitudBaja+observacionesPrevias);
			}
			
			if(tieneBajaDenegada){
				this.bean.setFechaDenegacion(null);
				this.bean.setObservacionesDenegacion(null);
				this.denegarBajaGuardia(usr);
			}
			
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitaBaja()");
		}				
	} 
	
	/**
	 * Deniega la inscripcion de un colegiado a una guardia
	 * 
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void denegarAltaGuardia(UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();
			String fechaDenegacion = this.bean.getFechaDenegacion();
			String observacionesDenegacion = this.bean.getObservacionesDenegacion();
	
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);
			
			if(!fechaDenegacion.equalsIgnoreCase("sysdate") && fechaDenegacion.length()==10) {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaDenegacion));
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION, fechaDenegacion);
			}				
			
			if (observacionesDenegacion!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION, observacionesDenegacion);
			else
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION, "");
			
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}
	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHADENEGACION,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar denegarAltaGuardia()");
		}				
	} 
	
	/**
	 * Deniega la baja de una inscripcion pendiente de una guardia
	 * 
	 * @param usr
	 * @throws ClsExceptions
	 */
	public void denegarBajaGuardia(UsrBean usr) throws ClsExceptions {
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();
			String fechaDenegacion = this.bean.getFechaDenegacion();
			String observacionesDenegacion = this.bean.getObservacionesDenegacion();
			
			//obteniendo ultimo indicado en la cola
			Hashtable<String, Object> hashGuardia = new Hashtable<String, Object>();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion);
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia);
			
			//cambiando el ultimo de la cola si es necesario
		
	
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);
			
			if(fechaDenegacion!=null){
				if(!fechaDenegacion.equalsIgnoreCase("sysdate") && fechaDenegacion.length()==10) {
					laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaDenegacion));
				} else {
					laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION, fechaDenegacion);
				}					
				
				if (observacionesDenegacion!=null)
					laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION, observacionesDenegacion);
				else
					laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION, "");
			}
			
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			}else{
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}
	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHADENEGACION,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar denegarBajaGuardia()");
		}					
	} 
	
	/**
	 * Valida la baja de la inscripcion del colegiado en la guardia
	 * @param usr
	 * @param bajaSyC
	 * @throws ClsExceptions
	 */
	public void validarBaja (UsrBean usr, String bajaSyC) throws ClsExceptions {
		try {
			// Controles
			ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(usr);
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();
			String fechaBaja = this.bean.getFechaBaja();
			
			//obteniendo ultimo indicado en la cola
			Hashtable<String, Object> hashGuardia = new Hashtable<String, Object>();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion);
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia);
			Vector vGuardias = guardiaAdm.select(hashGuardia);
			Long idPersonaUltimoGuardia = null;
			Long idGrupoUltimoGuardia = null;
			String fechaSuscripcion_Ultimo = null;
			if(vGuardias!=null && vGuardias.size()>0) {
				ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean)vGuardias.get(0);
				idPersonaUltimoGuardia = beanGuardia.getIdPersona_Ultimo();
				idGrupoUltimoGuardia = beanGuardia.getIdGrupoGuardiaColegiado_Ultimo();
				fechaSuscripcion_Ultimo = beanGuardia.getFechaSuscripcion_Ultimo();
			}
			
			//cambiando el ultimo de la cola si es necesario
			if (idPersonaUltimoGuardia!=null && idPersonaUltimoGuardia.equals(idPersona)) {
				ArrayList<LetradoInscripcion> colaLetrados = InscripcionGuardia.getColaGuardia(idInstitucion,  idTurno, idGuardia,"sysdate","sysdate",usr);
				
				if (!colaLetrados.isEmpty() && colaLetrados.get(0) != null) {
					int tamanyoCola = colaLetrados.size();
					Long ultimoDeLaCola = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-1)).getIdPersona();
					//el letrado a dar de baja es el primero en la cola:
					//asi que hay que poner al anterior como ultimo en la cola
					Long penultimoDeLaCola, idGrupoGuardiaPenultimo;
					String fechaSusc_penultimo;
					if (tamanyoCola == 1) {
						penultimoDeLaCola = null;
						fechaSusc_penultimo = null;
						idGrupoGuardiaPenultimo= null;
						
					} else {
						penultimoDeLaCola = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getIdPersona();
						idGrupoGuardiaPenultimo = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getIdGrupoGuardiaColegiado();
						fechaSusc_penultimo = ((LetradoInscripcion) colaLetrados.get(tamanyoCola-2)).getInscripcionGuardia().getFechaSuscripcion();
					}
					
					guardiaAdm.cambiarUltimoCola (idInstitucion, idTurno, idGuardia, penultimoDeLaCola, fechaSusc_penultimo,idGrupoGuardiaPenultimo);
				}
			}
	
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);							
			
			if(!fechaBaja.equalsIgnoreCase("sysdate") && fechaBaja.length()==10)
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaBaja));
			else
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA, fechaBaja);	
			
			laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, this.bean.getObservacionesValBaja());
			
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}
	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};
			
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
	
			// Saltos y Compensaciones
			if(bajaSyC!=null && bajaSyC.equalsIgnoreCase("G")){
				ScsSaltosCompensacionesAdm saladm = new ScsSaltosCompensacionesAdm(usr);
				//ArrayList<ScsSaltosCompensacionesBean> vSaldosyCompensTurno =  (ArrayList<ScsSaltosCompensacionesBean>) admSaltosYCompensacions.getSaltosCompensaciones(this.bean.getIdInstitucion(), 
				//this.bean.getIdTurno(),this.bean.getIdPersona(), null);
				if(idGuardia!=null)
					saladm.updateSaltosCompensacionesBajaTurno(idInstitucion, idTurno, idPersona, idGuardia, bajaSyC);
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar validarBaja()");
		}			
	} 
}