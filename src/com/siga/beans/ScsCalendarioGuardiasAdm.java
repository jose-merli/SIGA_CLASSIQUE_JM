
package com.siga.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.general.SIGAException;
import com.siga.gratuita.beans.ScsHcoConfProgCalendariosBean;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;
import com.siga.gratuita.form.DefinirTurnosForm;
import com.siga.tlds.FilaExtElement;
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_CALENDARIGUARDIAS
 * Modificado por david.sanchezp el 19-1-2005 para incluir nuevos métodos: getDatosCalendario(),<br>
 * getIdCalendarioGuardias(),selectGenerico().
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 * @version 22/03/2006: david.sanchezp: modificacion para poner el idpersona como Long y el texto de idcalendario por idcalendario guardias.
 */

public class ScsCalendarioGuardiasAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsCalendarioGuardiasAdm (UsrBean usuario) {
		super( ScsCalendarioGuardiasBean.T_NOMBRETABLA, usuario);
	}

	/** 
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean() {
		String[] campos =
		{
				ScsCalendarioGuardiasBean.C_IDINSTITUCION,
				ScsCalendarioGuardiasBean.C_IDTURNO,
				ScsCalendarioGuardiasBean.C_IDGUARDIA,
				ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS,
				ScsCalendarioGuardiasBean.C_FECHAFIN,
				ScsCalendarioGuardiasBean.C_FECHAINICIO,
				ScsCalendarioGuardiasBean.C_OBSERVACIONES,
				ScsCalendarioGuardiasBean.C_FECHAMODIFICACION,
				ScsCalendarioGuardiasBean.C_USUMODIFICACION,
				ScsCalendarioGuardiasBean.C_IDPERSONA_ULTIMOANTERIOR,
				ScsCalendarioGuardiasBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMOANTERIOR,
				ScsCalendarioGuardiasBean.C_FECHASUSC_ULTIMOANTERIOR,
				ScsCalendarioGuardiasBean.C_IDTURNO_PRINCIPAL,
				ScsCalendarioGuardiasBean.C_IDGUARDIA_PRINCIPAL,
				ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS_PRINCIPAL
		};
		return campos;
	}
	
	/** 
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos =
		{
				ScsCalendarioGuardiasBean.C_IDINSTITUCION,
				ScsCalendarioGuardiasBean.C_IDTURNO,
				ScsCalendarioGuardiasBean.C_IDGUARDIA,
				ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS
		};
		return campos;
	}
	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsCalendarioGuardiasBean bean = null;
		try{
			bean = new ScsCalendarioGuardiasBean();
			bean.setFechaFin			(UtilidadesHash.getString (hash, ScsCalendarioGuardiasBean.C_FECHAFIN));
			bean.setFechaInicio			(UtilidadesHash.getString (hash, ScsCalendarioGuardiasBean.C_FECHAINICIO));
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdGuardia			(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDGUARDIA));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDINSTITUCION));
			bean.setIdTurno				(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDTURNO));
			bean.setObservaciones		(UtilidadesHash.getString (hash, ScsCalendarioGuardiasBean.C_OBSERVACIONES));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_USUMODIFICACION));
			bean.setFechaMod			(UtilidadesHash.getString (hash, ScsCalendarioGuardiasBean.C_FECHAMODIFICACION));
			bean.setIdPersonaUltimoAnterior(UtilidadesHash.getLong(hash, ScsCalendarioGuardiasBean.C_IDPERSONA_ULTIMOANTERIOR));
			bean.setIdGrupoGuardiaColegiadoAnterior(UtilidadesHash.getLong(hash, ScsCalendarioGuardiasBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMOANTERIOR));
			bean.setFechaSuscUltimoAnterior(UtilidadesHash.getString(hash, ScsCalendarioGuardiasBean.C_FECHASUSC_ULTIMOANTERIOR));
			bean.setIdTurnoPrincipal(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDTURNO_PRINCIPAL));
			bean.setIdGuardiaPrincipal(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDGUARDIA_PRINCIPAL));
			bean.setIdCalendarioGuardiasPrincipal(UtilidadesHash.getInteger(hash, ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS_PRINCIPAL));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsCalendarioGuardiasBean b = (ScsCalendarioGuardiasBean) bean;
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_FECHAFIN, b.getFechaFin());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_FECHAINICIO, b.getFechaInicio());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS, b.getIdCalendarioGuardias());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDGUARDIA, b.getIdGuardia());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDTURNO, b.getIdTurno());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDPERSONA_ULTIMOANTERIOR, b.getIdPersonaUltimoAnterior());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMOANTERIOR, b.getIdGrupoGuardiaColegiadoAnterior());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_FECHASUSC_ULTIMOANTERIOR, b.getFechaSuscUltimoAnterior());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDTURNO_PRINCIPAL, b.getIdTurnoPrincipal());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDGUARDIA_PRINCIPAL, b.getIdGuardiaPrincipal());
			UtilidadesHash.set(hash, ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS_PRINCIPAL, b.getIdCalendarioGuardiasPrincipal());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}

	/** 
	 * Devuelve un String con la consulta SQL que devuelve registros con los datos de todos los calendarios laborales
	 * 
	 * @param String idinstitucion_pestanha de la pestanha
	 * @param String idturno_pestanha de la pestanha
	 * @param String idguardia_pestanha de la pestanha
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getCalendarios(String idinstitucion_pestanha, String idturno_pestanha, String idguardia_pestanha) throws ClsExceptions{
		String consulta = "";
		
		try {
			consulta = "SELECT S.*, ";
			consulta += " ((select count(*) ";
			consulta += "	  from SCS_GUARDIASCOLEGIADO ";
			consulta += "	 where IDINSTITUCION = S.IDINSTITUCION ";
			consulta += "	   and IDTURNO = S.IDTURNO ";
			consulta += "	   and IDGUARDIA = S.IDGUARDIA ";
			consulta += "      and IDCALENDARIOGUARDIAS = S.idcalendarioguardias ";
			consulta += "	   and trunc(FECHAFIN) < trunc(sysdate))) as guardias ";
			consulta += " FROM "+ScsCalendarioGuardiasBean.T_NOMBRETABLA + " S ";
			consulta += "WHERE S."+ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion_pestanha;
			consulta += "  AND S."+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idturno_pestanha;
			consulta += "  AND S."+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idguardia_pestanha;
			/** Modificado por PDM: Se añade la ordenacion por fecha inicio */
			consulta += " ORDER BY S."+ ScsCalendarioGuardiasBean.C_FECHAINICIO+" asc";
			/**/
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioGuardiasAdm.getDatosCalendario(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	

	/** 
	 * Devuelve un String con la consulta SQL que devuelve registros con los datos de un calendario laboral
	 * 
	 * @param String idinstitucion_pestanha de la pestanha
	 * @param String idturno_pestanha de la pestanha
	 * @param String idguardia_pestanha de la pestanha
	 * @param String idcalendario identificador del calendario
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getDatosCalendario(String idinstitucion_pestanha, String idturno_pestanha, String idguardia_pestanha, String idcalendario) throws ClsExceptions{
		String consulta = "";
		
		try {
			consulta = "SELECT * FROM "+ScsCalendarioGuardiasBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idturno_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idguardia_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendario;
			
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioGuardiasAdm.getDatosCalendario(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}		
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve 1 registro que contiene el id nuevo calculado para <br>
	 * el IDCALENDARIOGUARDIAS.
	 * 
	 * @param String idinstitucion_pestanha de la pestanha
	 * @param String idturno_pestanha de la pestanha
	 * @param String idguardia_pestanha de la pestanha
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getIdCalendarioGuardias(Integer idinstitucion_pestanha, Integer idturno_pestanha, Integer idguardia_pestanha) throws ClsExceptions{
		String consulta = "";
		
		try {
			consulta = "SELECT MAX("+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+") + 1 AS IDCALENDARIOGUARDIAS FROM "+ScsCalendarioGuardiasBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idturno_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idguardia_pestanha;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioGuardiasAdm.getIdCalendarioGuardias(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					//Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsCalendarioGuardiasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		

	/**
	 * Valida si una fecha es laborable a partir de la institucion y el dia de inicio.
	 * Devuelve true si es Laborable y false si es Festivo.
	 * 
	 * @param String fecha: fecha a validar.
	 * @param String idinstitucion: idinstitucion.
	 * @return boolean True si es una fecha laborable. False si es festivo. 
	 */
	public boolean esFechaLaborablePorInstitucion(String fecha, String idinstitucion) {
		boolean laborable = false;		
		Vector registros = new Vector();
		
		try {
			//Recupero del String fechaInicial con formato dd/mm/yyyy la fecha como Date
			String jsdf = ClsConstants.DATE_FORMAT_SHORT_SPANISH;//"dd/MM/yyyy";//Java Short Date Format
			SimpleDateFormat formateo = new SimpleDateFormat(jsdf);
			Date date = new Date();
			date = formateo.parse(fecha);
	
			//Calendario
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(date);
	
			//Compruebo que no sea Sabado o Domingo
			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				laborable = false;
			else {
				//Vemos si en scs_calendariolaboral tiene un registro con idpartido=NULL
				registros = selectGenerico(ScsCalendarioLaboralAdm.busquedaCalendario1(fecha,idinstitucion)); 
				if (registros.size() > 0)
					laborable = false;
				else 
					laborable = true;
			}
		}
		catch (Exception e) {
			laborable = false;
		}
		return laborable;
	}

	/**
	 * Devuelve la fecha Fin a partir de la institucion, el dia de inicio y los dias de margen.
	 * Devuelve true si es Laborable y false si es Festivo.
	 * 
	 * @param String fechaInicio: Fecha de Inicio en el formato DD/MM/YYYY.
	 * @param String dias: dias de margen entre la Fecha de Inicio y la Fecha Fin. Si es 0 devuelve la Fecha de Inicio.
	 * @param String idinstitucion: idinstitucion.
	 * @return String con la Fecha Fin en el formato DD/MM/YYYY. 
	 */
	public  String obtenerFechaFinLaborable(String fechaInicio, String dias, String idinstitucion) {
		String fechaFin="";  
		int diasRestantes = 0; 
		
		fechaFin = fechaInicio;
		
		//Busco la fecha fin a partir de la fecha inicio seleccionada
		//Sigo buscando mientras tenga diasRestantes > 0
		diasRestantes = Integer.parseInt(dias);
		while (diasRestantes > 0) {				
				fechaFin = UtilidadesFecha.sumarDias(fechaFin,1);
				if (esFechaLaborablePorInstitucion(fechaFin,idinstitucion)) 
					diasRestantes--;				
		}
		
		return fechaFin;
	}

	public boolean validarBorradoCalendario(Integer idInstitucion,
			Integer idCalendarioGuardias,
			Integer idTurno,
			Integer idGuardia)
	{
		boolean correcto = false;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) AS TOTAL ");
		sql.append("  FROM " + ScsCalendarioGuardiasBean.T_NOMBRETABLA);
		sql.append(" WHERE " + ScsCalendarioGuardiasBean.C_IDINSTITUCION + "=" + idInstitucion);
		sql.append("   AND " + ScsCalendarioGuardiasBean.C_IDTURNO + "=" + idTurno);
		sql.append("   AND " + ScsCalendarioGuardiasBean.C_IDGUARDIA + "=" + idGuardia);
		sql.append("   AND " + ScsCalendarioGuardiasBean.C_FECHAFIN + " > ");
		sql.append("      (SELECT " + ScsCalendarioGuardiasBean.C_FECHAFIN);
		sql.append("         FROM " + ScsCalendarioGuardiasBean.T_NOMBRETABLA);
		sql.append("        WHERE " + ScsCalendarioGuardiasBean.C_IDINSTITUCION + "=" + idInstitucion);
		sql.append("          AND " + ScsCalendarioGuardiasBean.C_IDTURNO + "=" + idTurno);
		sql.append("          AND " + ScsCalendarioGuardiasBean.C_IDGUARDIA + "=" + idGuardia);
		sql.append("          AND " + ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS + "=" + idCalendarioGuardias);
		sql.append("      ) ");
		try {
			Vector v = this.selectGenerico(sql.toString());
			String total = "";
			if (!v.isEmpty()) {
				total = (String) ((Hashtable) v.get(0)).get("TOTAL");
				// Si devuelve 0 la validacion es true. En otro caso es falso
				if (total != null && total.equals("0"))
					correcto = true;
			}
		} catch (Exception e) {
			correcto = false;
		}
		return correcto;
	}
	
	/**
	 * Actualiza la guardia con el ultimo de la cola guardado en el calendario
	 * @param hash con la clave del calendario
	 * @return true si todo fue bien
	 */
	public boolean actualizarGuardia(Hashtable hash)
	{
		boolean ok = false;

		try {
			Vector v = this.select(hash);
			ScsCalendarioGuardiasBean calendarioBean = (ScsCalendarioGuardiasBean) v.get(0);
			ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(usrbean);
			guardiaAdm.cambiarUltimoCola(calendarioBean.getIdInstitucion(), 
					calendarioBean.getIdTurno(), calendarioBean.getIdGuardia(), 
					calendarioBean.getIdPersonaUltimoAnterior(), calendarioBean.getFechaSuscUltimoAnterior()
					,calendarioBean.getIdGrupoGuardiaColegiadoAnterior());

			ok = true;
		} catch (Exception e) {
			ok = false;
		}
		return ok;
	}

	
	
	public ScsCalendarioGuardiasBean getCalendarioGuardias(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fecha) throws ClsExceptions, SIGAException
	{
		
		ScsCalendarioGuardiasBean  calendarioGuardiasBean = null;
		try {
			calendarioGuardiasBean = getCalendarioGuardiasGenerado(idInstitucion,
					idTurno, idGuardia, fecha);	
		} catch (SIGAException e) {
			 calendarioGuardiasBean = getCalendarioGuardiasNoGenerado(idInstitucion,
						idTurno, idGuardia, fecha);	
			
		}
		return calendarioGuardiasBean;
	}
	
	
	 private ScsCalendarioGuardiasBean getCalendarioGuardiasGenerado(Integer idInstitucion,Integer idTurno,Integer idGuardia, String fechaGuardia)throws ClsExceptions, SIGAException{
			Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
			int contador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT CG.IDINSTITUCION,CG.IDTURNO,    CG.IDGUARDIA,     CG.IDCALENDARIOGUARDIAS ");
			sql.append(" FROM SCS_CALENDARIOGUARDIAS CG ");
			sql.append(" WHERE CG.IDINSTITUCION = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idInstitucion);
			sql.append(" AND CG.IDTURNO = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idTurno);
			sql.append(" AND CG.IDGUARDIA = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idGuardia);
			sql.append(" AND CG.IDCALENDARIOGUARDIAS = ");
			sql.append(" ( SELECT MAX(CAB.IDCALENDARIOGUARDIAS) FROM SCS_CABECERAGUARDIAS CAB ");
			sql.append(" WHERE CAB.IDINSTITUCION = CG.IDINSTITUCION ");  
			sql.append(" AND CAB.IDTURNO = CG.IDTURNO AND CAB.IDGUARDIA = CG.IDGUARDIA AND :");
			contador ++;
			sql.append(contador);
//			String truncFechaGuardia = GstDate.getFormatedDateShort("", fechaGuardia);
			htCodigos.put(new Integer(contador),fechaGuardia);
			sql.append(" BETWEEN TRUNC(CAB.FECHAINICIO) AND TRUNC(CAB.FECHA_FIN) ) ");
			sql.append(" ORDER BY IDINSTITUCION, IDTURNO, IDGUARDIA, IDCALENDARIOGUARDIAS ");
				
			
			ScsCalendarioGuardiasBean guardiaBean = null;
			try {
				RowsContainer rc = new RowsContainer(); 
													
	            if (rc.findBind(sql.toString(),htCodigos)) {
	        		if(rc.size()>0){
		            	Row fila = (Row) rc.get(0);
		        		Hashtable<String, Object> htFila=fila.getRow(); 
		            	guardiaBean = new ScsCalendarioGuardiasBean();
		           		guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDTURNO));
		           		guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDGUARDIA));
		           		guardiaBean.setIdCalendarioGuardias(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS));
	        		}else{
	        			throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
	        			
	        		}
	            	
	            }else{
	            	throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
	            	
	            }
			}catch (SIGAException e) {
	            	throw e;
	         
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return guardiaBean;
			
			
		}
		
	
	
	private ScsCalendarioGuardiasBean getCalendarioGuardiasNoGenerado(Integer idInstitucion,Integer idTurno,Integer idGuardia, String fechaGuardia)throws ClsExceptions, SIGAException{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
        
		sql.append(" SELECT * FROM SCS_CALENDARIOGUARDIAS GC ");
		sql.append(" WHERE GC.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" AND  GC.IDTURNO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTurno);
		sql.append(" AND  GC.IDGUARDIA = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idGuardia);
		sql.append(" AND :");
		contador ++;
		sql.append(contador);
//		String truncFechaGuardia = GstDate.getFormatedDateShort("", fechaGuardia);
		htCodigos.put(new Integer(contador),fechaGuardia);
		sql.append(" BETWEEN TRUNC(GC.FECHAINICIO) AND ");
		sql.append(" TRUNC(GC.FECHAFIN) ");	
		ScsCalendarioGuardiasBean guardiaBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
        		if(rc.size()>0){
	            	Row fila = (Row) rc.get(0);
	        		Hashtable<String, Object> htFila=fila.getRow(); 
	            	guardiaBean = new ScsCalendarioGuardiasBean();
	           		guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDTURNO));
	           		guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDGUARDIA));
	           		guardiaBean.setIdCalendarioGuardias(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS));
        		}else{
        			throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
        			
        		}
            	
            }else{
            	throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
            	
            }
		}catch (ClsExceptions e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return guardiaBean;
		
		
	}
	

	/** 
	 * Devuelve un String con la consulta SQL que devuelve registros con los datos de los calendarios de las guardias vinculadas
	 * 
	 * @param String idinstitucion_pestanha de la pestanha
	 * @param String idturno_pestanha de la pestanha
	 * @param String idguardia_pestanha de la pestanha
	 * @param String idcalendario identificador del calendario
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getDatosCalendarioVinculadas(Integer idinstitucion_pestanha, Integer idturno_pestanha, Integer idguardia_pestanha, Integer idcalendario) throws ClsExceptions{
		String consulta = "";
		
		try {
			consulta = "SELECT * FROM "+ScsCalendarioGuardiasBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDTURNO_PRINCIPAL+"="+idturno_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDGUARDIA_PRINCIPAL+"="+idguardia_pestanha;
			consulta += " AND "+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS_PRINCIPAL+"="+idcalendario;
			
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioGuardiasAdm.getDatosCalendarioVinculadas(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}		
	public List<ScsCalendarioGuardiasBean> getCalendariosProgramados(
			ScsProgCalendariosBean progCalendariosBean) throws ClsExceptions {
		Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT CG.FECHAINICIO,CG.FECHAFIN,CG.IDCALENDARIOGUARDIAS,CG.IDTURNO,CG.IDGUARDIA,CG.IDINSTITUCION ");
		sql.append(" FROM SCS_PROG_CALENDARIOS PG,SCS_HCO_CONF_PROG_CALENDARIOS HP,SCS_CALENDARIOGUARDIAS CG ");
		sql.append(" WHERE  ");
		sql.append(" TRUNC(PG.FECHACALINICIO) = TRUNC(CG.FECHAINICIO) ");
		sql.append(" AND TRUNC(PG.FECHACALFIN) = TRUNC(CG.FECHAFIN) ");
		sql.append(" AND HP.IDTURNO = CG.IDTURNO ");
		sql.append(" AND HP.IDGUARDIA = CG.IDGUARDIA ");
		sql.append(" AND HP.IDINSTITUCION = CG.IDINSTITUCION ");
		sql.append(" AND PG.IDPROGCALENDARIO = HP.IDPROGCALENDARIO "); 
		sql.append(" AND PG.IDPROGCALENDARIO =:");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progCalendariosBean.getIdProgrCalendario());
		sql.append(" AND PG.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		codigosHashtable.put(new Integer(contador),progCalendariosBean.getIdInstitucion());
		sql.append(" ORDER  BY HP.ORDEN DESC ");

    	List<ScsCalendarioGuardiasBean> calendarioGuardiasBeans = null;
    	try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				calendarioGuardiasBeans = new ArrayList<ScsCalendarioGuardiasBean>();
				ScsCalendarioGuardiasBean calendarioGuardiasBean = null;
				
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					calendarioGuardiasBean =  (ScsCalendarioGuardiasBean)this.hashTableToBean(htFila);
					
					calendarioGuardiasBeans.add(calendarioGuardiasBean);
					
				}
			}else{
				calendarioGuardiasBeans = new ArrayList<ScsCalendarioGuardiasBean>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return calendarioGuardiasBeans;
	}
	public List<DefinirCalendarioGuardiaForm> getCalendarios(DefinirCalendarioGuardiaForm definirCalendarioGuardiaFiltroForm) throws ClsExceptions {
		Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
//    	static public final Short estadoProgramado = new Short("0");
//    	static public final Short estadoProcesando = new Short("1");
//    	static public final Short estadoError = new Short("2");
//    	static public final Short estadoGenerado = new Short("3");
//        static public final Short estadoCancelado = new Short("4");
//        static public final Short estadoPteManual = new Short("5");
    	
		StringBuffer sql = new StringBuffer();
		definirCalendarioGuardiaFiltroForm.getEstado().equals("");
		
		sql.append("SELECT * FROM (");
		if(definirCalendarioGuardiaFiltroForm.getEstado().equals("")||definirCalendarioGuardiaFiltroForm.getEstado().equals(ScsCalendarioGuardiasBean.estadoGenerado)||definirCalendarioGuardiaFiltroForm.getEstado().equals(ScsCalendarioGuardiasBean.estadoPteManual)){
			
			sql.append(" SELECT T.NOMBRE NOMBRETURNO, ");
			sql.append(" GT.NOMBRE NOMBREGUARDIA, ");
			sql.append(" CG.FECHAINICIO, ");
			sql.append(" CG.FECHAFIN, ");
			sql.append(" CG.OBSERVACIONES, ");
			sql.append(" DECODE((SELECT count(1) TOTAL ");
			sql.append(" 	                 FROM SCS_CABECERAGUARDIAS CAG ");
			sql.append(" WHERE CAG.IDINSTITUCION = CG.IDINSTITUCION ");
			sql.append(" AND CAG.IDTURNO = CG.IDTURNO ");
			sql.append(" AND CAG.IDGUARDIA = CG.IDGUARDIA ");
			sql.append(" AND CAG.IDCALENDARIOGUARDIAS = CG.IDCALENDARIOGUARDIAS) , 0, ");
			sql.append(ScsCalendarioGuardiasBean.estadoError);
			sql.append(", ");
			sql.append(ScsCalendarioGuardiasBean.estadoGenerado);
			sql.append(" ) ESTADO");
			sql.append(",CG.IDTURNO ");
			sql.append(",CG.IDGUARDIA ");
			sql.append(",CG.IDCALENDARIOGUARDIAS ");
			sql.append(",CG.IDINSTITUCION ");
			sql.append(" FROM SCS_CALENDARIOGUARDIAS CG, SCS_GUARDIASTURNO GT, SCS_TURNO T ");
			sql.append(" WHERE CG.IDINSTITUCION = GT.IDINSTITUCION ");
			sql.append(" AND CG.IDTURNO = GT.IDTURNO ");
			sql.append(" AND CG.IDGUARDIA = GT.IDGUARDIA ");
			sql.append(" AND GT.IDINSTITUCION = T.IDINSTITUCION ");
			sql.append(" AND GT.IDTURNO = T.IDTURNO ");
			sql.append(" AND CG.IDINSTITUCION = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getIdInstitucion());
			
			if(!definirCalendarioGuardiaFiltroForm.getFechaInicio().equals("")){
				sql.append(" AND TRUNC(CG.FECHAINICIO) = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getFechaInicio());
				
				
			}
			if(!definirCalendarioGuardiaFiltroForm.getFechaFin().equals("")){
				sql.append(" AND TRUNC(CG.FECHAFIN) = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getFechaFin());
				
				
			}
			if(!definirCalendarioGuardiaFiltroForm.getIdTurnoCalendario().equals("")&&!definirCalendarioGuardiaFiltroForm.getIdTurnoCalendario().equals("-1")){
				sql.append(" AND T.IDTURNO = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getIdTurnoCalendario());
				
				
			}
			if(!definirCalendarioGuardiaFiltroForm.getIdGuardiaCalendario().equals("")&&!definirCalendarioGuardiaFiltroForm.getIdGuardiaCalendario().equals("-1")){
				sql.append(" AND GT.IDGUARDIA = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getIdGuardiaCalendario());
				
				
			}
				
			
			
		}
		if(definirCalendarioGuardiaFiltroForm.getEstado().equals(""))
			sql.append(" UNION ");
			
		
		if(definirCalendarioGuardiaFiltroForm.getEstado().equals("")||(!definirCalendarioGuardiaFiltroForm.getEstado().equals(ScsCalendarioGuardiasBean.estadoGenerado)&&!definirCalendarioGuardiaFiltroForm.getEstado().equals(ScsCalendarioGuardiasBean.estadoPteManual))){
			String etiquetaConjuntoGuardias = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.calendarios.programacion.conjuntoGuardias");
		
			sql.append(" SELECT T.NOMBRE NOMBRETURNO,  GT.NOMBRE NOMBREGUARDIA,PC.FECHACALINICIO FECHAINICIO,PC.FECHACALFIN FECHAFIN , ");
			sql.append("'");
			sql.append(etiquetaConjuntoGuardias);
			sql.append(": '||CG.DESCRIPCION OBSERVACIONES, ");
			sql.append(" NVL(HPC.ESTADO, ");				
			sql.append(ScsCalendarioGuardiasBean.estadoProgramado);		
			sql.append(") ESTADO ");
			sql.append(",null IDTURNO ");
			sql.append(",null IDGUARDIA ");
			sql.append(",null IDCALENDARIOGUARDIAS ");
			sql.append(",PC.IDINSTITUCION ");
		
			
			
			sql.append(" FROM SCS_PROG_CALENDARIOS          PC, ");
			sql.append(" SCS_HCO_CONF_PROG_CALENDARIOS HPC, ");
			sql.append(" SCS_CONJUNTOGUARDIAS          CG, ");
			sql.append(" SCS_GUARDIASTURNO             GT, ");
			sql.append(" SCS_TURNO                     T ");
			sql.append(" WHERE PC.IDCONJUNTOGUARDIA = CG.IDCONJUNTOGUARDIA ");
			sql.append(" AND PC.IDINSTITUCION = CG.IDINSTITUCION ");			

			sql.append(" AND HPC.IDINSTITUCION = GT.IDINSTITUCION ");
			sql.append(" AND HPC.IDTURNO = GT.IDTURNO ");
			sql.append(" AND HPC.IDGUARDIA = GT.IDGUARDIA ");
			
			sql.append(" AND GT.IDINSTITUCION = T.IDINSTITUCION ");
			sql.append(" AND GT.IDTURNO = T.IDTURNO ");

			sql.append(" AND HPC.IDINSTITUCION = PC.IDINSTITUCION ");
			sql.append(" AND HPC.IDPROGCALENDARIO = PC.IDPROGCALENDARIO ");
			
			sql.append(" AND not exists (select * from SCS_CALENDARIOGUARDIAS CAL where HPC.IDTURNO = CAL.IDTURNO AND HPC.IDGUARDIA = CAL.IDGUARDIA AND HPC.IDINSTITUCION = CAL.IDINSTITUCION AND PC.Fechacalinicio = CAL.Fechainicio AND PC.Fechacalfin = CAL.Fechafin) ");
			
			sql.append(" AND HPC.IDINSTITUCION = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getIdInstitucion());
			
			if(!definirCalendarioGuardiaFiltroForm.getFechaInicio().equals("")){
				sql.append(" AND TRUNC(PC.FECHACALINICIO) = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getFechaInicio());
				
				
			}
			if(!definirCalendarioGuardiaFiltroForm.getFechaFin().equals("")){
				sql.append(" AND TRUNC(PC.FECHACALFIN) = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getFechaFin());
				
				
			}
			if(!definirCalendarioGuardiaFiltroForm.getIdTurnoCalendario().equals("")&&!definirCalendarioGuardiaFiltroForm.getIdTurnoCalendario().equals("-1")){
				sql.append(" AND T.IDTURNO = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getIdTurnoCalendario());
				
				
			}
			if(!definirCalendarioGuardiaFiltroForm.getIdGuardiaCalendario().equals("")&&!definirCalendarioGuardiaFiltroForm.getIdGuardiaCalendario().equals("-1")){
				sql.append(" AND GT.IDGUARDIA = :");
				contador++;
				sql.append(contador);
				codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getIdGuardiaCalendario());
				
				
			}
			
			

		}
		sql.append(" )");
		if(!definirCalendarioGuardiaFiltroForm.getEstado().equals("")){
			sql.append(" WHERE ESTADO = :");
			contador++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),definirCalendarioGuardiaFiltroForm.getEstado());
			
			
		}
		sql.append("ORDER BY NOMBRETURNO, NOMBREGUARDIA,FECHAINICIO,FECHAFIN");
	
		

    	List<DefinirCalendarioGuardiaForm> calendarioGuardiasForms = null;
//    	DefinirCalendarioGuardiaForm
    	
    	try {
			RowsContainer rc = new RowsContainer(); 
			
			if (rc.findBind(sql.toString(),codigosHashtable)) {
				ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String directorioLogCalendarios = rp.returnProperty("sjcs.directorioFisicoGeneracionCalendarios");
				calendarioGuardiasForms = new ArrayList<DefinirCalendarioGuardiaForm>();
				ScsCalendarioGuardiasBean calendarioGuardiasBean = null;
				DefinirGuardiasTurnosForm guardia;
				DefinirTurnosForm turno;
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					calendarioGuardiasBean =  (ScsCalendarioGuardiasBean)this.hashTableToBean(htFila);
					DefinirCalendarioGuardiaForm definirCalendarioGuardiaForm = calendarioGuardiasBean.getDefinirCalendarioGuardiaForm(); 
					calendarioGuardiasForms.add(definirCalendarioGuardiaForm);
					guardia = new DefinirGuardiasTurnosForm();
					turno = new DefinirTurnosForm();
					guardia.setNombreGuardia(UtilidadesHash.getString(htFila, "NOMBREGUARDIA"));
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					definirCalendarioGuardiaForm.setGuardia(guardia);
					definirCalendarioGuardiaForm.setTurno(turno);
					definirCalendarioGuardiaForm.setEstado(UtilidadesHash.getString(htFila, "ESTADO"));
					//BNS INC_10626_SIGA Incluimos todos los que tienen log.
					//if(definirCalendarioGuardiaForm.getEstado().equals(ScsCalendarioGuardiasBean.estadoGenerado)||definirCalendarioGuardiaForm.getEstado().equals(ScsCalendarioGuardiasBean.estadoPteManual)){
						StringBuffer sFicheroLog = new StringBuffer(directorioLogCalendarios);
						sFicheroLog.append(File.separator);
						sFicheroLog.append(calendarioGuardiasBean.getIdInstitucion());
						sFicheroLog.append(File.separator);
						sFicheroLog.append(calendarioGuardiasBean.getIdTurno());
						sFicheroLog.append(".");
						sFicheroLog.append(calendarioGuardiasBean.getIdGuardia());
						sFicheroLog.append(".");
						sFicheroLog.append(calendarioGuardiasBean.getIdCalendarioGuardias());
						sFicheroLog.append("-");
						sFicheroLog.append(GstDate.getFormatedDateShort(usrbean.getLanguage(),calendarioGuardiasBean.getFechaInicio()).replace('/', '.'));
						sFicheroLog.append("-");
						sFicheroLog.append(GstDate.getFormatedDateShort(usrbean.getLanguage(),calendarioGuardiasBean.getFechaFin()).replace('/', '.'));
						sFicheroLog.append(".log.xls");
					
						File fichero = new File(sFicheroLog.toString());
						FilaExtElement[] elementosFila=new FilaExtElement[1];
						if(fichero!=null && fichero.exists()){
							//Boton de descarga del envio:
							elementosFila[0]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
						} else {
							elementosFila[0] = null;
						}
						definirCalendarioGuardiaForm.setElementosFila(elementosFila);
					
						
					//}
					
					
				}
			}else{
				calendarioGuardiasForms = new ArrayList<DefinirCalendarioGuardiaForm>();
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}

    	return calendarioGuardiasForms;
	}
	public Integer getIdCalendarioGuardias(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fechaInicioCalendario,String fechaFinCalendario)
	{
		Hashtable codigosHashtable = new Hashtable();
    	int contador = 0;
		StringBuffer where = new StringBuffer();
		where.append(" Where Idinstitucion =:");
		contador++;
		where.append(contador);
		codigosHashtable.put(new Integer(contador),idInstitucion);
		where.append(" And Idturno =:");
		contador++;
		where.append(contador);
		codigosHashtable.put(new Integer(contador),idTurno);
		where.append(" And Idguardia = :");
		contador++;
		where.append(contador);
		codigosHashtable.put(new Integer(contador),idGuardia);
		where.append(" And trunc(Fechainicio)=:");
		contador++;
		where.append(contador);
		codigosHashtable.put(new Integer(contador),fechaInicioCalendario);
 
		where.append("And trunc(Fechafin)=:");
		contador++;
		where.append(contador);
		codigosHashtable.put(new Integer(contador),fechaFinCalendario);

		try {
			Vector resultado = this.selectBind(where.toString(),codigosHashtable);
			if (resultado != null && resultado.size() > 0)
				return ((ScsCalendarioGuardiasBean) resultado.get(0)).getIdCalendarioGuardias();
			else
				return null;
		} catch (ClsExceptions e) {
			return null;
		}
	}

	
	
}