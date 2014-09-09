package com.siga.gratuita;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
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
			ScsOrdenacionColasAdm ordadm = new ScsOrdenacionColasAdm(usr);
			CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);
			ArrayList<LetradoInscripcion> colaLetrados = new ArrayList<LetradoInscripcion>();
	
			//Actualizar cola guardia para evitar que el ultimo grupo quede a caballo
			ScsGrupoGuardiaColegiadoAdm admGrupoGuardia = new ScsGrupoGuardiaColegiadoAdm(usr);
			admGrupoGuardia.actualizarColaGuardiaConUltimoColegiadoPorGrupo(idInstitucion, idTurno, idGuardia);
			
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
			orden = porGrupos ? " numeroGrupo, ordengrupo" : ordadm.getOrderBy(idOrdenacionColas.toString());
	
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
						
						
						
						// JPT: Comprobar que no exista ninguna guardia abierta (asi controlamos que no se pueda insertar dos veces)
						if (this.comprobarNoGuardiaActiva(laHash)) {
							insguardia.insert(laHash);
							if(this.getBean().getNumeroGrupo()!=null){

								
								ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);
								admGrupoColegiado.insertaGrupoGuardiaColegiado(
									this.getBean().getIdInstitucion(),
									this.getBean().getIdTurno(),
									beanGuardia.getIdGuardia(),
									this.getBean().getIdPersona(), 
									(String) laHash.get(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION), 
									this.getBean().getNumeroGrupo(), 
									this.getBean().getOrdenGrupo().toString(), 
									null);
							}
							
						} else {
							throw new ClsExceptions("messages.inserted.error");
						}
					}
				}
				
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
				
				// JPT: Comprobar que no exista ninguna guardia abierta (asi controlamos que no se pueda insertar dos veces)
				if (this.comprobarNoGuardiaActiva(laHash)) {
					insguardia.insert(laHash);
				} else {
					throw new ClsExceptions("messages.inserted.error");
				}
				
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
//					HAY QUE VALIDAR SI EL NUMERO GRUPO / ORDEN CUMPLE LAS VALIDACIONES.
//					El tema es que no esta en transaccion y no se puede anular la insercion de la guardia- 
//					Habia pensado borrar el grupo en caso que falle la validacion, pero para eso habria que informar
//					al usuario por pantalla, ¿cómo? ya veremos 
//					ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);
//					if(insguardia.getGrupoGuardia(idInstitucion.toString(),idTurno.toString(),idGuardia.toString(),fechaValidacion)){
//						String idGrupoGuardiaColegiado = admGrupoColegiado.getUltimoGrupo(idInstitucion);
//						Hashtable hash=new Hashtable();
//						String[] claves = {ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO};
//						hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO,idGrupoGuardiaColegiado);
//						admGrupoColegiado.deleteDirect(hash, claves);
//						throw new SIGAException ("messages.grupoguardiacolegiado.existepersonagrupo");
//					}
//					
//					if(insguardia.getOrdenGuardia(idInstitucion.toString(),idTurno.toString(),idGuardia.toString(),fechaValidacion)){
//						String idGrupoGuardiaColegiado = admGrupoColegiado.getUltimoGrupo(idInstitucion);
//						Hashtable hash=new Hashtable();
//						String[] claves = {ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO};
//						hash.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO,idGrupoGuardiaColegiado);
//						admGrupoColegiado.deleteDirect(hash, claves);
//						throw new SIGAException ("messages.grupoguardiacolegiado.existeordengrupo");
//					}
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar solicitarAlta()");
		}				
	}
	
	/**
	 * JPT: Comprueba que no exista ninguna inscripcion de guardia activa, antes de insertar la nueva inscripcion de guardia
	 * @param hash
	 * @return
	 */
	private boolean comprobarNoGuardiaActiva(Hashtable hash) {
		boolean resultado = true;
		RowsContainer rc = new RowsContainer(); 	
		
		try {
			String idInstitucion = hash.get(ScsInscripcionGuardiaBean.C_IDINSTITUCION).toString();
			String idTurno = hash.get(ScsInscripcionGuardiaBean.C_IDTURNO).toString();
			String idGuardia = hash.get(ScsInscripcionGuardiaBean.C_IDGUARDIA).toString();
			String idPersona = hash.get(ScsInscripcionGuardiaBean.C_IDPERSONA).toString();
			
			String sql = "SELECT * " +
				" FROM " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + 
				" WHERE " + ScsInscripcionGuardiaBean.C_IDINSTITUCION + " = " + idInstitucion +
					" AND " + ScsInscripcionGuardiaBean.C_IDTURNO + " = " + idTurno + 
					" AND " + ScsInscripcionGuardiaBean.C_IDGUARDIA + " = " + idGuardia +
					" AND " + ScsInscripcionGuardiaBean.C_IDPERSONA + " = " + idPersona +
					" AND " + ScsInscripcionGuardiaBean.C_FECHABAJA + " IS NULL " +
					" AND (" + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + " IS NOT NULL OR " + ScsInscripcionGuardiaBean.C_FECHADENEGACION + " IS NULL) ";
			
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					resultado = false;
				}
			}
	
		} catch (Exception e) {
			resultado = false;
		}				
		
		return resultado;
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
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {						
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "validarAlta");
			}
			
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
			String fechaValidacionPrevia = this.bean.getFechaValidacion();
			
			// Preparamos los datos para la inscripcion de guardia
			String[] claves = null;			
			if(idGuardia!=null){
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,				
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};		
			}
			
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};		
			
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);
			
			laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, GstDate.getApplicationFormatDate(usr.getLanguage(), fechaValidacion));
			
			if (observacionesValidacion!=null) {
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, observacionesValidacion);
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION, "");
			}
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION + "PREVIA", fechaValidacionPrevia);
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "modificarFechaValidacion");
			}
			
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
			String fechaBajaPrevia = this.bean.getFechaBaja();
			
			// Preparamos los datos para la inscripcion de guardia
			String[] claves = null;			
			if(idGuardia!=null){
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
				
			} else {
				claves = new String[] {ScsInscripcionGuardiaBean.C_IDINSTITUCION,				
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDPERSONA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};		
			}	
			
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA,
				ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA,
				MasterBean.C_USUMODIFICACION,
				MasterBean.C_FECHAMODIFICACION};				
			
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSuscripcion);
			
			laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA, GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
			
			if (observacionesBaja!=null) {
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, observacionesBaja);
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA, "");
			}			
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA + "PREVIA", fechaBajaPrevia);
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "modificarFechaBaja");
			}
			
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
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {						
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "solicitaBaja");
			}
			
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
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {						
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "denegarAltaGuardia");
			}
			
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
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {						
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "denegarBajaGuardia");
			}
			
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
			
			if(idGuardia!=null){
				ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
				insguardia.updateDirect(laHash, claves, campos);
				
			} else {						
				this.actualizarInscripcionesGuardias(laHash, claves, campos, usr, "validarBaja");
			}
	
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
	
	/**
	 * Actualiza las inscripciones de guardia
	 * 
	 * No podemos incluir la fecha de suscripcion, porque antes de realizar un gran cambio en las inscripciones de turno y guardia (mayo 2013)
	 * no había control de las fechas. Por lo tanto, puede haber varias inscripciones con diferentes fechas de suscripcion.
	 * Ademas, se realizan cambios a mano directamente en base de datos sin control, con lo que no nos podemos fiar de que esa fecha contenga todas las inscripciones de guardia que deberia.
	 * 
	 * @param hash
	 * @param claves
	 * @param campos
	 * @param usr
	 * @param accion
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean actualizarInscripcionesGuardias(Hashtable hash, String[] claves, String [] campos, UsrBean usr, String accion) throws ClsExceptions {
		hash.put(MasterBean.C_USUMODIFICACION, usr.getUserName()); 
		hash.put(MasterBean.C_FECHAMODIFICACION, "sysdate");
		
		try {
			Connection con = ClsMngBBDD.getConnection();
			Hashtable dataTypes = ClsMngBBDD.tableDataTypesAsString(con, ScsInscripcionGuardiaBean.T_NOMBRETABLA);
			
			String sql = " UPDATE " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + " SET ";
			
			String sqlSet = "";
			for (int i = 0; i < campos.length; i++) {
				
				String valor = "";		
				if (hash.get(campos[i]) == null || hash.get(campos[i]).toString().trim().equals("") || hash.get(campos[i]).toString().equalsIgnoreCase("NULL") || "NULL".equalsIgnoreCase("" + campos[i])) {
					valor = " NULL ";				
				
				} else if (dataTypes.get(campos[i]).equals("STRING")) {
					valor = " '" + hash.get(campos[i]).toString() + "' ";
					
				} else if (dataTypes.get(campos[i]).equals("NUMBER")) {
					valor = " " + hash.get(campos[i]) + " ";
					
				} else if (dataTypes.get(campos[i]).equals("DATE")) {
					if (hash.get(campos[i]).toString().equalsIgnoreCase("sysdate")) {
						valor = " SYSTIMESTAMP ";
					} else {
						valor = " TO_DATE('" + hash.get(campos[i]).toString() + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
					}
					
				} else {
					valor = " '" + hash.get(campos[i]).toString() + "' ";
				}
				
				if (sqlSet.equals("")) {				
					sqlSet += campos[i] + " = " + valor;					
				} else {
					sqlSet += ", " + campos[i] + " = " + valor;
				}
			}
			
			sql += sqlSet + " WHERE ";
			
			String sqlWhere = "";
			for (int i = 0; i < claves.length; i++) {
				if (!ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION.equals(claves[i])) {

					String valor = "";				
					if (dataTypes.get(claves[i]).equals("STRING")) {
						valor = " '" + hash.get(claves[i]).toString() + "' ";
						
					} else if (dataTypes.get(claves[i]).equals("NUMBER")) {
						valor = " " + hash.get(claves[i]) + " ";
						
					} else if (dataTypes.get(claves[i]).equals("DATE")) {
						if (hash.get(claves[i]).toString().equalsIgnoreCase("sysdate")) {
							valor = " SYSTIMESTAMP ";
						} else {
							valor = " TO_DATE('" + hash.get(claves[i]).toString() + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
						}
						
					} else {
						valor = " '" + hash.get(claves[i]).toString() + "' ";
					}				
				
					if (sqlWhere.equals("")) {				
						sqlWhere += claves[i] + " = " + valor;					
					} else {
						sqlWhere += " AND " + claves[i] + " = " + valor;
					}
				}
			}				
			
			if (accion.equalsIgnoreCase("validarAlta") || accion.equalsIgnoreCase("denegarAltaGuardia")) {
				sqlWhere += " AND " + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + " IS NULL " +
					" AND " + ScsInscripcionGuardiaBean.C_FECHADENEGACION + " IS NULL ";
				
			} else if (accion.equalsIgnoreCase("solicitaBaja") || accion.equalsIgnoreCase("validarBaja")) {
				sqlWhere += " AND (" + ScsInscripcionGuardiaBean.C_FECHADENEGACION + " IS NULL " +
					" OR " + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + " IS NOT NULL) " +
					" AND " + ScsInscripcionGuardiaBean.C_FECHABAJA + " IS NULL ";
				
			} else if (accion.equalsIgnoreCase("denegarAltaGuardia")) {
				sqlWhere += " AND " + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + " IS NOT NULL " +
					" AND " + ScsInscripcionGuardiaBean.C_FECHADENEGACION + " IS NULL " + 
					" AND " + ScsInscripcionGuardiaBean.C_FECHABAJA + " IS NULL ";
				
			} else if (accion.equalsIgnoreCase("modificarFechaValidacion")) {
				sqlWhere += " AND " + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + " IS NOT NULL " +
					" AND " + ScsInscripcionGuardiaBean.C_FECHABAJA + " IS NULL " + 
					" AND TRUNC(" + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + ") = TRUNC(TO_DATE('" + hash.get(ScsInscripcionGuardiaBean.C_FECHAVALIDACION + "PREVIA").toString() + "', '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "')) ";;
					
			} else if (accion.equalsIgnoreCase("modificarFechaBaja")) {
				sqlWhere += " AND " + ScsInscripcionGuardiaBean.C_FECHABAJA + " IS NOT NULL " + 
					" AND TRUNC(" + ScsInscripcionGuardiaBean.C_FECHABAJA + ") = TRUNC(TO_DATE('" + hash.get(ScsInscripcionGuardiaBean.C_FECHABAJA + "PREVIA").toString() + "', '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "')) ";;		
			}									
			
			sql += sqlWhere;

			Row row = new Row();				
			int valor = row.updateSQL(sql);
			return valor > 0;

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar actualizarInscripcionesGuardias()");
		}		       
	}		
}