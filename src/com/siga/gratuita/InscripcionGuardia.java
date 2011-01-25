package com.siga.gratuita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.ScsGrupoGuardiaColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

public class InscripcionGuardia
{
	////////////////////
	// ATRIBUTOS
	////////////////////
	private ScsInscripcionGuardiaBean bean = null;
	
	private static SimpleDateFormat formatoFecha = 
		new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
	
	
	////////////////////
	// CONSTRUCTORES
	////////////////////
	/**
	 * Constructor
	 */
	private InscripcionGuardia (Integer idInstitucion,
								Integer idTurno,
								Integer idGuardia,
								Long idPersona,
								String fechaSolicitudAlta)
	{
		this.bean = new ScsInscripcionGuardiaBean();
		this.bean.setIdInstitucion(idInstitucion);
		this.bean.setIdTurno(idTurno);
		this.bean.setIdGuardia(idGuardia);
		this.bean.setIdPersona(idPersona);
		this.bean.setFechaSuscripcion(fechaSolicitudAlta);
	} //InscripcionGuardia ()
	
	public void setBajas(String obsSolicitudBaja,String fechaSolicitudBaja,
			String fechaBaja,String observacionesValBaja){
		this.bean.setObservacionesBaja(obsSolicitudBaja);
		this.bean.setObservacionesValBaja(observacionesValBaja);
		this.bean.setFechaSolicitudBaja(fechaSolicitudBaja);
		this.bean.setFechaBaja(fechaBaja);
		
	}
	public void setDenegacion(String obsDenegacion,String fechaDenegacion){
		this.bean.setObservacionesDenegacion(obsDenegacion);
		this.bean.setFechaDenegacion(fechaDenegacion);
		
	}
	public void setAltas(String obsSolicitud,String fechaValidacion,
			String observacionesValidacion){
		this.bean.setObservacionesSuscripcion(obsSolicitud);
		this.bean.setFechaValidacion(fechaValidacion);
		this.bean.setObservacionesValidacion(observacionesValidacion);
		
	}
	public void setDatosGrupo(String numeroGrupo, Integer ordenGrupo){
		this.bean.setNumeroGrupo(numeroGrupo);
		this.bean.setOrdenGrupo(ordenGrupo);
		
	}
	
	
	private InscripcionGuardia (ScsInscripcionGuardiaBean bean)
	{
		this.bean = bean;
	} //InscripcionGuardia ()
	
	
	////////////////////
	// GETTERS Y SETTERS
	////////////////////
	public ScsInscripcionGuardiaBean getBean() { return bean; }
	public void setBean(ScsInscripcionGuardiaBean bean) { this.bean = bean; }
	
	
	////////////////////
	// METODOS DE CLASE
	////////////////////
	/**
	 * Devuelve un objeto que representa la inscripcion de un colegiado a una guardia. 
	 * Este metodo comprueba si existe realmente la inscripcion. 
	 * Sin embargo, es posible evitar la comprobacion con el parametro comprobarQueExiste=false
	 */
	public static InscripcionGuardia getInscripcionGuardia (Integer idInstitucion,
															Integer idTurno,
															Integer idGuardia,
															Long idPersona,
															String fechaSolicitudAlta,
															UsrBean usr,
															boolean comprobarQueExiste)
		throws ClsExceptions 
	{
		InscripcionGuardia inscripcion = null;
		
		if (! comprobarQueExiste){
			inscripcion = new InscripcionGuardia (
					idInstitucion, idTurno, idGuardia, idPersona, fechaSolicitudAlta);
		}
		else {
			//preparando datos
			Hashtable<String, Object> hash = new Hashtable<String, Object>();
			hash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION, idInstitucion);
			hash.put(ScsInscripcionGuardiaBean.C_IDPERSONA, idPersona);
			hash.put(ScsInscripcionGuardiaBean.C_IDTURNO, idTurno);
			hash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA, idGuardia);
			if(fechaSolicitudAlta.equalsIgnoreCase("sysdate"))
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, fechaSolicitudAlta);
			else
				hash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION, formatoFecha.format(fechaSolicitudAlta));
			
			//obteniendo datos
			Vector<ScsInscripcionGuardiaBean> inscripciones = new ScsInscripcionGuardiaAdm(usr).select(hash);
			if (inscripciones == null)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion a la guardia");
			if (inscripciones.size() < 1)
				throw new ClsExceptions("Error: no se ha encontrado la inscripcion a la guardia");
			if (inscripciones.size() > 1)
				throw new ClsExceptions("Error: se ha encontrado mas de una inscripcion a la guardia");
			
			inscripcion = new InscripcionGuardia ((ScsInscripcionGuardiaBean) inscripciones.get(0));
		}
		
		return inscripcion;
	} //getInscripcionGuardia ()
	
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
	public static ArrayList<LetradoGuardia> getColaGuardia(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fechaInicio,
			String fechaFin,
			UsrBean usr) throws ClsExceptions
	{
		// Variables para navegacion por la cola
		ScsInscripcionGuardiaBean ultimoAnterior;
		ScsInscripcionGuardiaBean punteroInscripciones;
		boolean foundUltimo;
		
		// Variables de la guardia
		ScsGuardiasTurnoBean beanGuardia;
		Integer idOrdenacionColas;
		String orden;
		boolean porGrupos;
		Long idPersonaUltimo;
		String fechaSuscripcionUltimo;

		// Controles
		ScsGuardiasTurnoAdm guaadm = new ScsGuardiasTurnoAdm(usr);
		ScsInscripcionGuardiaAdm insadm = new ScsInscripcionGuardiaAdm(usr);
		//TODO Descomentar la siguiente linea (ya que se comento solo para entregar incidencia INC_07825_SIGA urgente:
		// ScsOrdenacionColasAdm ordadm = new ScsOrdenacionColasAdm(usr);
		CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);
		ArrayList<LetradoGuardia> colaLetrados = new ArrayList<LetradoGuardia>();

		// obteniendo la guardia
		Hashtable hashGuardia = new Hashtable();
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
		Vector vGuardia = guaadm.select(hashGuardia);
		beanGuardia = (ScsGuardiasTurnoBean) vGuardia.get(0);
		porGrupos = beanGuardia.getPorGrupos().equals(ClsConstants.DB_TRUE);
		idOrdenacionColas = beanGuardia.getIdOrdenacionColas();
		idPersonaUltimo = beanGuardia.getIdPersona_Ultimo();
		fechaSuscripcionUltimo = beanGuardia.getFechaSuscripcion_Ultimo();
		
		// obteniendo ordenacion de la guardia
		if (idOrdenacionColas == null)
			throw new ClsExceptions("messages.general.error");
		//TODO Descomentar la siguiente linea y borrar lo anyadido (ya que se comento solo para entregar incidencia INC_07825_SIGA urgente):
		orden = porGrupos ? " numeroGrupo, ordengrupo" : /* Anyadido: */ getOrderBy(idOrdenacionColas.toString(), usr); // ordadm.getOrderBy(idOrdenacionColas.toString(), usr);

		// obteniendo ultimo apuntado de la guardia
		if (idPersonaUltimo == null)
			ultimoAnterior = null;
		else
			ultimoAnterior = new ScsInscripcionGuardiaBean(idInstitucion, idTurno, idGuardia, idPersonaUltimo, fechaSuscripcionUltimo);
		
		// obteniendo lista de letrados (ordenada)
		Vector<ScsInscripcionGuardiaBean> listaLetrados = insadm.getColaGuardia(idInstitucion.toString(), 
				idTurno.toString(), idGuardia.toString(), fechaInicio, fechaFin, porGrupos, orden);
		if (listaLetrados == null || listaLetrados.size() == 0)
			return colaLetrados;

		if (ultimoAnterior == null) {
			// si no existe ultimo colegiado, se empieza la cola desde el primero en la lista
			for (int i = 0; i < listaLetrados.size(); i++) {
				punteroInscripciones = (ScsInscripcionGuardiaBean) listaLetrados.get(i);
				if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE))
					colaLetrados.add(new LetradoGuardia(punteroInscripciones, null));
			}
		}
		else {
			// ordenando la cola en funcion del idPersonaUltimo guardado
			Vector<LetradoGuardia> colaAuxiliar = new Vector<LetradoGuardia>();
			foundUltimo = false;
			for (int i = 0; i < listaLetrados.size(); i++) {
				punteroInscripciones = listaLetrados.get(i);

				// insertando en la cola si la inscripcion esta activa
				if (punteroInscripciones.getEstado().equals(ClsConstants.DB_TRUE)) {
					// El primero que se anyade es el siguiente al ultimo
					if (foundUltimo) {
						colaLetrados.add(new LetradoGuardia(punteroInscripciones, null));
					} else {
						colaAuxiliar.add(new LetradoGuardia(punteroInscripciones, null));
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
	} // getColaGuardia()
	
	public static ArrayList<LetradoGuardia> getLetradosGrupo(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			Long idGrupoGuardia,
			String saltoCompensacion,
			String idSaltoCompensacionGrupo,
			UsrBean usr) throws ClsExceptions
	{
		// Controles
		ScsGuardiasTurnoAdm guaadm = new ScsGuardiasTurnoAdm(usr);
		ScsInscripcionGuardiaAdm insadm = new ScsInscripcionGuardiaAdm(usr);
		CenBajasTemporalesAdm bajasAdm = new CenBajasTemporalesAdm(usr);

		// Variables
		Vector<ScsInscripcionGuardiaBean> vectorLetrados;
		ArrayList<LetradoGuardia> listaLetrados = new ArrayList<LetradoGuardia>();
		LetradoGuardia letradoGuardia;
		ScsInscripcionGuardiaBean inscripcionGuardia;

		// obteniendo la guardia
		Hashtable hashGuardia = new Hashtable();
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
		Vector vGuardia = guaadm.select(hashGuardia);
		ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) vGuardia.get(0);

		// obteniendo lista de letrados
		vectorLetrados = insadm.getLetradosGrupo(idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), idGrupoGuardia.toString());
		if (vectorLetrados == null || vectorLetrados.size() == 0)
			return null;

		// obteniendo LetradoGuardia's
		listaLetrados = new ArrayList<LetradoGuardia>();
		for (int i = 0; i < vectorLetrados.size(); i++) {
			inscripcionGuardia = (ScsInscripcionGuardiaBean) vectorLetrados.get(i);
			letradoGuardia = new LetradoGuardia(inscripcionGuardia, null);
			if (saltoCompensacion != null) {
				letradoGuardia.setSaltoCompensacion(saltoCompensacion);
				letradoGuardia.setIdSaltoCompensacionGrupo(idSaltoCompensacionGrupo);
			}
			listaLetrados.add(letradoGuardia);
		}

		return listaLetrados;
	} // getLetradosGrupo()
	
	public static boolean existeGrupoSinMinimo(ScsGuardiasTurnoBean beanGuardia, UsrBean usr) throws ClsExceptions
	{
		int minimoLetrados = beanGuardia.getNumeroLetradosGuardia();
		ScsInscripcionGuardiaAdm insadm = new ScsInscripcionGuardiaAdm(usr);
		Vector<Hashtable> v = insadm.getNumLetradosGrupos(beanGuardia.getIdInstitucion().toString(),
				beanGuardia.getIdTurno().toString(), beanGuardia.getIdGuardia().toString());

		boolean existe = false;
		for (Hashtable hashGrupo : v) {
			if (((Integer) hashGrupo.get("NUMERO")).intValue() < minimoLetrados)
				existe = true;
		}
		return existe;
	}
	
	//TODO Borrar el siguiente metodo (ya que se comento solo para entregar incidencia INC_07825_SIGA urgente):
	/**
	 * Obtiene la clausula order by para el select de inscripciones
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param usr
	 * @return Order by sin el "order by"
	 * @throws ClsExceptions
	 */
	public static String getOrderBy(String idOrdenacionColas, UsrBean usr)
			throws ClsExceptions
	{
		ScsOrdenacionColasAdm ordadm = new ScsOrdenacionColasAdm(usr);
		Hashtable hashOrden = new Hashtable();
		
		// obteniendo el orden
		hashOrden.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS, idOrdenacionColas);
		Vector vOrden = ordadm.select(hashOrden);
		ScsOrdenacionColasBean ordenBean = (ScsOrdenacionColasBean) vOrden.get(0);
		Integer apellidos = ordenBean.getAlfabeticoApellidos();
		Integer antiguedad = ordenBean.getAntiguedadCola();
		Integer fechanacimiento = ordenBean.getFechaNacimiento();
		Integer numerocolegiado = ordenBean.getNumeroColegiado();
		
		// calculando order by
		String orden = "";
		for (int i=4; i>0; i--) {
			if (Math.abs(apellidos) == i) {
				orden += ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS;
				if (Math.abs(apellidos) != apellidos) orden += " desc";
				orden += ", ";
			}
			if (Math.abs(antiguedad) == i) {
				orden += ScsOrdenacionColasBean.C_ANTIGUEDADCOLA;
				if (Math.abs(antiguedad) != antiguedad) orden += " desc";
				orden += ", ";
			}
			if (Math.abs(fechanacimiento) == i) {
				orden += ScsOrdenacionColasBean.C_FECHANACIMIENTO;
				if (Math.abs(fechanacimiento) != fechanacimiento) orden += " desc";
				orden += ", ";
			}
			if (Math.abs(numerocolegiado) == i) {
				orden += ScsOrdenacionColasBean.C_NUMEROCOLEGIADO;
				if (Math.abs(numerocolegiado) != numerocolegiado) orden += " desc";
				orden += ", ";
			}
		}
		if (orden.length() > 0)
			orden = orden.substring(0, orden.length()-2);
		
		return orden;
	} //getOrderBy()
	
	////////////////////
	// METODOS
	////////////////////
	/**
	 * Da de alta la inscripcion de un colegiado a una guardia.
	 * 
	 * @TODO falta implementar. Actualmente no esta aqui
	 */
	public void solicitarAlta (UsrBean usr)throws ClsExceptions
	{
		UserTransaction tx = null;
		try {
			
			tx = usr.getTransaction(); 
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
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
			
			
			
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechSubscripcion);
			laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION,observacionesSuscripcion);
			if(fechaValidacion!=null&&!fechaValidacion.equals("")){
				if(!fechaValidacion.equalsIgnoreCase("sysdate"))
					laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
				else
					laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,fechaValidacion);
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,obsValidacion);
				
			}
			
			if(fechaBaja!=null&&!fechaBaja.equals("")){
				laHash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,"sysdate");
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
				
			}
			
			
			tx.begin();
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			if(idGuardia==null){
				ScsTurnoAdm turno = new ScsTurnoAdm(usr);
				
				
				
				StringBuffer sql=  new StringBuffer("SELECT IDGUARDIA ");
				sql.append(" FROM SCS_GUARDIASTURNO  ");
		        sql.append(" WHERE IDINSTITUCION = ");
		        sql.append(this.bean.getIdInstitucion());
		        sql.append(" AND IDTURNO = ");
		        sql.append(this.bean.getIdTurno());
		        
		        Vector vTurno =  turno.ejecutaSelect(sql.toString());
				ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
				for(int x=0;x<vTurno.size();x++)
				{
					Hashtable htGuardia = (Hashtable)vTurno.get(x);
					laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,new Integer((String)htGuardia.get("IDGUARDIA")));
					insguardia.insert(laHash);
					
				}
				
			}else{
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
				insguardia.insert(laHash);
				ScsInscripcionGuardiaBean inscripcionInsertada = (ScsInscripcionGuardiaBean) insguardia.getUltimaInscripcionInsertada(laHash);
				if(this.getBean().getNumeroGrupo()!=null){
					ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);
					admGrupoColegiado.insertaGrupoGuardiaColegiado(this.getBean().getIdInstitucion(),this.getBean().getIdTurno()
					,this.getBean().getIdGuardia(),this.getBean().getIdPersona(), inscripcionInsertada.getFechaSuscripcion(), this.getBean().getNumeroGrupo(), this.getBean().getOrdenGrupo().toString(), null);
					
				}
				
			}
			tx.commit();
		} catch (ClsExceptions e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw e;
		}catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		
	}
	
	
	
	/*
	 * Este procedimiento no hace nada, 
	 * porque no existe la validacion de alta de guardia, todavia
	public void validarAlta (String fechaAlta, String observacionesAlta, UsrBean usr)
		throws ClsExceptions
	{
	} //validarAlta ()
	 */
	
	public void validarAlta (UsrBean usr)throws ClsExceptions{
		UserTransaction tx = null;
		
		try {
			tx = usr.getTransaction();
			tx.begin();
			
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();
			String fechaValidacion = this.bean.getFechaValidacion();
			String observacionesValidacion = this.bean.getObservacionesValidacion();
					Hashtable laHash = new Hashtable();
					
					
					// solamente para el caso en el que se solicita y valida el alta de una guardia
					//en fecha con inscripcon en turno con fecha de baja
					String fechaBaja = this.bean.getFechaBaja();
					
					
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechaSuscripcion);
			if(fechaValidacion.equals("sysdate")){
				laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,fechaValidacion);
			}else{
				laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			}
//			laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,observacionesValidacion);
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_IDGUARDIA,
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}else{
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			
				
			}
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			if(fechaBaja!=null&&!fechaBaja.equals("")){
				laHash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,"sysdate");
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
				String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,ScsInscripcionGuardiaBean.C_FECHABAJA,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
				insguardia.updateDirect(laHash,claves,campos);
				
			}else{
				String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
				insguardia.updateDirect(laHash,claves,campos);
			}
			if(this.getBean().getNumeroGrupo()!=null){
				ScsGrupoGuardiaColegiadoAdm admGrupoColegiado = new ScsGrupoGuardiaColegiadoAdm(usr);
								
				admGrupoColegiado.insertaGrupoGuardiaColegiado(this.getBean().getIdInstitucion(),this.getBean().getIdTurno()
				,this.getBean().getIdGuardia(),this.getBean().getIdPersona(), fechaSuscripcion, this.getBean().getNumeroGrupo(), this.getBean().getOrdenGrupo().toString(), null);
				
			}
			
			
			tx.commit();
		}
		catch (Exception e) {
			try {
				tx.rollback();	
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			throw new ClsExceptions (e, "Error al validar la inscripcion de la guardia");
		}
	}
	public void modificarFechaValidacion (UsrBean usr)throws ClsExceptions{
		try {
			Integer idInstitucion = this.bean.getIdInstitucion();
			Integer idTurno = this.bean.getIdTurno();
			Integer idGuardia = this.bean.getIdGuardia();
			Long idPersona = this.bean.getIdPersona();
			String fechaSuscripcion = this.bean.getFechaSuscripcion();
			String fechaValidacion = this.bean.getFechaValidacion();
			String observacionesValidacion = this.bean.getObservacionesValidacion();
					Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechaSuscripcion);
			
			laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			
//			laHash.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaValidacion));
			laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,observacionesValidacion);
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_IDGUARDIA,
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}else{
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			
				
			}
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al modificar la fecha de validacion de la inscripcion a la guardia");
		}
	} 
	public void modificarFechaBaja (UsrBean usr)throws ClsExceptions{
		try {
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

			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechaSuscripcion);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));			
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_IDGUARDIA,
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}else{
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			
				
			}
	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la baja en la guardia");
		}
	} 
	
	public void solicitarBaja (UsrBean usr)throws ClsExceptions
	{
		solicitaBaja(usr);
		
		if(this.bean.getFechaBaja()!=null&&!this.bean.getFechaBaja().equals("")){
			if(this.bean.getIdGuardia()==null){
				ScsTurnoAdm turno = new ScsTurnoAdm(usr);
				StringBuffer sql=  new StringBuffer("SELECT I.IDGUARDIA,I.FECHASUSCRIPCION ");
		        sql.append(" FROM SCS_INSCRIPCIONGUARDIA I ");
		        sql.append(" WHERE I.IDINSTITUCION = ");
		        sql.append(this.bean.getIdInstitucion());
		        sql.append(" AND I.IDTURNO = ");
		        sql.append(this.bean.getIdTurno());
		        sql.append(" AND I.IDPERSONA = ");
		        sql.append(this.bean.getIdPersona());
		        sql.append(" AND FECHABAJA IS NULL");
		        Vector vTurno =  turno.ejecutaSelect(sql.toString());
				ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
				for(int x=0;x<vTurno.size();x++)
				{
					Hashtable htGuardia = (Hashtable)vTurno.get(x);
					this.bean.setIdGuardia(new Integer((String)htGuardia.get("IDGUARDIA")));
					this.bean.setFechaSuscripcion((String)htGuardia.get("FECHASUSCRIPCION"));
					validarBaja(usr);
				}
			}else{
				validarBaja(usr);
			}
		}
		
	}
	
	private void solicitaBaja (UsrBean usr)throws ClsExceptions
	{
		Integer idInstitucion = this.bean.getIdInstitucion();
		Integer idTurno = this.bean.getIdTurno();
		Integer idGuardia = this.bean.getIdGuardia();
		Long idPersona = this.bean.getIdPersona();
		String fechSubscripcion = this.bean.getFechaSuscripcion();
		String fechSolicitudBaja = this.bean.getFechaSolicitudBaja();
		String observacionesSolicitudBaja = this.bean.getObservacionesBaja();
		Hashtable laHash = new Hashtable();
		laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
		laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
		if(idGuardia!=null)
			laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
		laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
		laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechSubscripcion);
		
		
		String[] claves = null;
		if(idGuardia!=null){
			claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};	
		}else{
			claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			
		}

		ScsInscripcionGuardiaAdm inscripcionAdm = new ScsInscripcionGuardiaAdm(usr);
		Vector<ScsInscripcionGuardiaBean> v = inscripcionAdm.select(laHash);
		
		laHash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,fechSolicitudBaja);
		
		boolean tieneBajaDenegada = false;
		if (v != null && v.size() >0 ) {
			ScsInscripcionGuardiaBean inscripcionBean = (ScsInscripcionGuardiaBean) v.get(0);
			
				if(inscripcionBean.getFechaDenegacion()!=null && !inscripcionBean.getFechaDenegacion().equals("")){
					tieneBajaDenegada = true;
					String observacionDenegacion = inscripcionBean.getObservacionesDenegacion()==null?"":inscripcionBean.getObservacionesDenegacion();
					observacionesSolicitudBaja += "[Fecha denegación previa:"+GstDate.getFormatedDateShort("", inscripcionBean.getFechaDenegacion()) +"]";
					observacionesSolicitudBaja += "[Observaciones denegación previa:"+observacionDenegacion+"]";
					
				}
				String observacionesPrevias = inscripcionBean.getObservacionesBaja()==null?"":"{"+inscripcionBean.getObservacionesBaja()+"}";
				inscripcionBean.setObservacionesBaja(observacionesSolicitudBaja+observacionesPrevias);
				laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,observacionesSolicitudBaja+observacionesPrevias);
			
			
		}
		if(tieneBajaDenegada){
			this.bean.setFechaDenegacion(null);
			this.bean.setObservacionesDenegacion(null);
			denegarBajaGuardia(usr);
		}
		
		String[] campos = {ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
		ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
		insguardia.updateDirect(laHash,claves,campos);
	} 
	public void denegarAltaGuardia(UsrBean usr)
	throws ClsExceptions
{
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
	

		Hashtable laHash = new Hashtable();
		laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
		laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
		if(idGuardia!=null)
			laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
		laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
		laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechaSuscripcion);
		if(!fechaDenegacion.equalsIgnoreCase("sysdate")){
//			laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
			laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
		}
		else{
//			laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,fechaDenegacion);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION,fechaDenegacion);
		}
		laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,observacionesDenegacion);
		
		String[] claves = null;
		if(idGuardia!=null){
			claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
		}else{
			claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
		
			
		}


		String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA,ScsInscripcionGuardiaBean.C_FECHADENEGACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
		ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
		insguardia.updateDirect(laHash,claves,campos);
		
	}
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al denegar la guardia");
	}
} 
	public void denegarBajaGuardia(UsrBean usr)
	throws ClsExceptions
{
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
		laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
		laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
		if(idGuardia!=null)
			laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
		laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
		laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechaSuscripcion);
		if(fechaDenegacion!=null){
			if(!fechaDenegacion.equalsIgnoreCase("sysdate")){
				laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDenegacion));
			}
			else{
				laHash.put(ScsInscripcionGuardiaBean.C_FECHADENEGACION,fechaDenegacion);
			}
			laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,observacionesDenegacion);
		}
		
		String[] claves = null;
		if(idGuardia!=null){
			claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_IDGUARDIA,
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
		}else{
			claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					ScsInscripcionGuardiaBean.C_IDPERSONA, 
					ScsInscripcionGuardiaBean.C_IDTURNO, 
					ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
		
			
		}


		String[] campos = {ScsInscripcionGuardiaBean.C_FECHADENEGACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
		ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
		insguardia.updateDirect(laHash,claves,campos);
		
	}
	catch (Exception e) {
		throw new ClsExceptions (e, "Error al denegar la guardia");
	}
} 
	
	/**
	 * Da de baja la inscripcion actual del colegiado en la guardia
	 */
	public void validarBaja (UsrBean usr)
		throws ClsExceptions
	{
		// Controles
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(usr);
		
		try {
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
			String fechaSuscripcion_Ultimo = null;
			if(vGuardias!=null && vGuardias.size()>0) {
				ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean)vGuardias.get(0);
				idPersonaUltimoGuardia = beanGuardia.getIdPersona_Ultimo();
				fechaSuscripcion_Ultimo = beanGuardia.getFechaSuscripcion_Ultimo();
			}
			
			//cambiando el ultimo de la cola si es necesario
		
			
			if (idPersonaUltimoGuardia!=null && idPersonaUltimoGuardia.equals(idPersona)) {
				ArrayList<LetradoGuardia> colaLetrados = InscripcionGuardia.getColaGuardia(
						idInstitucion,  idTurno, idGuardia,"sysdate","sysdate",usr);
				if (colaLetrados.get(0) != null && !colaLetrados.isEmpty()) {
					int tamanyoCola = colaLetrados.size();
					Long ultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-1)).getIdPersona();
					//el letrado a dar de baja es el primero en la cola:
					//asi que hay que poner al anterior como ultimo en la cola
					Long penultimoDeLaCola;
					String fechaSusc_penultimo;
					if (tamanyoCola == 1) {
						penultimoDeLaCola = null;
						fechaSusc_penultimo = null;
					}
					else {
						penultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-2)).getIdPersona();
						fechaSusc_penultimo = ((LetradoGuardia) colaLetrados.get(tamanyoCola-2)).getInscripcionGuardia().getFechaSuscripcion();
					}
					
					guardiaAdm.cambiarUltimoCola (idInstitucion, idTurno, idGuardia, penultimoDeLaCola, fechaSusc_penultimo);
				}
			}
			

			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,idInstitucion);
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idTurno);
			if(idGuardia!=null)
				laHash.put(ScsInscripcionGuardiaBean.C_IDGUARDIA			,idGuardia);
			laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,idPersona);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION,fechaSuscripcion);
			if(!fechaBaja.equalsIgnoreCase("sysdate"))
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaBaja));
			else
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA,fechaBaja);
			
			laHash.put(ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA,this.bean.getObservacionesValBaja());
			
			String[] claves = null;
			if(idGuardia!=null){
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_IDGUARDIA,
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			}else{
				claves = new String[]{ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO, 
						ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION};
			
				
			}

	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA,ScsInscripcionGuardiaBean.C_FECHAMODIFICACION,ScsInscripcionGuardiaBean.C_USUMODIFICACION};
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la baja en la guardia");
		}
	} 

}
