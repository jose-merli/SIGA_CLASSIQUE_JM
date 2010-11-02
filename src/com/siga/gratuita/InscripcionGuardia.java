package com.siga.gratuita;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
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
			String fechaBaja){
		this.bean.setObservacionesBaja(obsSolicitudBaja);
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
	
	
	public static ArrayList<LetradoGuardia> getColaGuardia(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fechaInicio,
			String fechaFin,
			UsrBean usr)
			throws ClsExceptions
			{
		ScsGuardiasTurnoAdm guaadm = new ScsGuardiasTurnoAdm(usr);
		Hashtable hashGuardia = new Hashtable();
		ArrayList<LetradoGuardia> colaLetrados = new ArrayList<LetradoGuardia>();
		ScsInscripcionGuardiaAdm insadm = new ScsInscripcionGuardiaAdm(usr);

//		obteniendo la guardia
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion.toString());
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno.toString());
		hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia.toString());
		Vector vGuardia = guaadm.select(hashGuardia);
		ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) vGuardia.get(0);
		Integer idOrdenacionColas = beanGuardia.getIdOrdenacionColas();
		if(idOrdenacionColas==null)
			throw new ClsExceptions("messages.general.error");
		Long idPersonaUltimo = beanGuardia.getIdPersona_Ultimo();


//		obteniendo ordenacion de la guardia
		String orden = getOrderBy(idOrdenacionColas.toString(), usr);

//		obteniendo lista de letrados (ordenada)
		Vector<ScsInscripcionGuardiaBean> listaLetrados = insadm.getColaGuardia(idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), fechaInicio,fechaFin, orden);
		if (listaLetrados == null || listaLetrados.size()==0)
			return colaLetrados;

//		ordenando, poniendo como primero: el siguiente al ultimo

		LetradoGuardia letradoGuardia;
		ScsInscripcionGuardiaBean inscripcionGuardia = null;
		//int indexUltimo = 0;
		boolean foundUltimo = false;
		Vector<LetradoGuardia> colaAuxiliar = new Vector<LetradoGuardia>();
		if(idPersonaUltimo!=null){
			for (int i = 0; i < listaLetrados.size(); i++) {
				inscripcionGuardia = (ScsInscripcionGuardiaBean) listaLetrados.get(i);
				letradoGuardia = new LetradoGuardia(inscripcionGuardia.getIdPersona(),
						inscripcionGuardia.getIdInstitucion(),
						inscripcionGuardia.getIdTurno(),
						inscripcionGuardia.getIdGuardia());
				letradoGuardia.setPersona(inscripcionGuardia.getPersona());
				letradoGuardia.setFechaBaja(inscripcionGuardia.getFechaBaja());
				letradoGuardia.setFechaValidacion(inscripcionGuardia.getFechaValidacion());
				
				if(foundUltimo){
					//El primero que se añade es el ultimo. Depues habra que moverlo
					colaLetrados.add(letradoGuardia);
				}else{
					colaAuxiliar.add(letradoGuardia);
				}
				if(!foundUltimo && inscripcionGuardia.getIdPersona().compareTo(idPersonaUltimo)==0){
					foundUltimo = true;
					//indexUltimo = i;
				}
			}
			colaLetrados.addAll(colaAuxiliar);
			}else{
		
//			si el idpersona ultimo es nullo con el orden que traian
			for (int i = 0; i < listaLetrados.size(); i++) {
				inscripcionGuardia = (ScsInscripcionGuardiaBean) listaLetrados.get(i);
				letradoGuardia = new LetradoGuardia(inscripcionGuardia.getIdPersona(),
						inscripcionGuardia.getIdInstitucion(),
						inscripcionGuardia.getIdTurno(),
						inscripcionGuardia.getIdGuardia());
				letradoGuardia.setPersona(inscripcionGuardia.getPersona());
				letradoGuardia.setFechaBaja(inscripcionGuardia.getFechaBaja());
				letradoGuardia.setFechaValidacion(inscripcionGuardia.getFechaValidacion());
				colaLetrados.add(letradoGuardia);
			}
		}

		
//		usando saltos si es necesario (en guardias no)

		return colaLetrados;
			} //getColaGuardia()
	
	
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
		try {
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
				String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION,ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,ScsInscripcionGuardiaBean.C_FECHABAJA};
				insguardia.updateDirect(laHash,claves,campos);
				
			}else{
				String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION};
				insguardia.updateDirect(laHash,claves,campos);
			}
			
			
			
		}
		catch (Exception e) {
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
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHAVALIDACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION};
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
	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA};
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
		laHash.put(ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,fechSolicitudBaja);
		
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
		Vector<ScsInscripcionGuardiaBean> v = inscripcionAdm.selectByPKForUpdate(laHash);
		
		boolean tieneBajaDenegada = false;
		if (v != null && v.size() == 1) {
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
		
		String[] campos = {ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA,ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA};
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


		String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA,ScsInscripcionGuardiaBean.C_FECHADENEGACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION,};
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


		String[] campos = {ScsInscripcionGuardiaBean.C_FECHADENEGACION,ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION};
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
			Vector vGuardias = new ScsGuardiasTurnoAdm(usr).select(hashGuardia);
			Long idPersonaUltimoGuardia = null;
			if(vGuardias!=null && vGuardias.size()>0)
				idPersonaUltimoGuardia = ((ScsGuardiasTurnoBean)vGuardias.get(0) 
					).getIdPersona_Ultimo();
			
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
					if (tamanyoCola == 1)
						penultimoDeLaCola = null;
					else
						penultimoDeLaCola = ((LetradoGuardia) colaLetrados.get(tamanyoCola-2)).getIdPersona();
					
					cambiarUltimoCola (idInstitucion, idTurno, idGuardia, penultimoDeLaCola, usr);
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

	
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA};
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(usr);
			insguardia.updateDirect(laHash,claves,campos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al solicitar la baja en la guardia");
		}
	} 


	/**
	 * Cambia el ultimo letrado de la cola de la guardia indicada
	 * por el nuevo que se ha solicitado
	 * 
	 * @TODO Este metodo y tambien las comprobaciones de cola que se hacen desde esta misma clase
	 * podrian ir en una nueva clase de logica de negocio "ColaGuardia" 
	 */
	public void cambiarUltimoCola (Integer idInstitucion,
								   Integer idTurno,
								   Integer idGuardia,
								   Long idPersona,
								   UsrBean usr)
		throws ClsExceptions
	{
		String sql = 
			" update "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" " +
			"    set "+ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO+" = "+(idPersona==null?"null":idPersona)+", " +
			"        "+ScsGuardiasTurnoBean.C_FECHAMODIFICACION+" = sysdate, " +
			"        "+ScsGuardiasTurnoBean.C_USUMODIFICACION+" = "+usr.getUserName()+" " +
			"  where "+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = "+idInstitucion+" " +
			"    and "+ScsGuardiasTurnoBean.C_IDTURNO+" = "+idTurno+" " +
			"    and "+ScsGuardiasTurnoBean.C_IDGUARDIA+" = "+idGuardia+" ";
		
		if (! new ScsGuardiasTurnoAdm(usr).updateSQL(sql)) {
			throw new ClsExceptions("Error al cambiar ultimo letrado de cola de guardia");
		}
	} //cambiarUltimoCola()
	
}
