
package com.siga.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_SALTOSCOMPENSACIONES
 * 
 * Modificado el 8/02/2005 por david.sanchez para incluir nuevoIdSaltosTurnos(), selectGenerico().
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsSaltosCompensacionesAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsSaltosCompensacionesAdm (UsrBean usuario) {
		super( ScsSaltosCompensacionesBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	
			ScsSaltosCompensacionesBean.C_FECHA,				
			ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,
			ScsSaltosCompensacionesBean.C_FECHAMODIFICACION,	
			ScsSaltosCompensacionesBean.C_IDGUARDIA,
			ScsSaltosCompensacionesBean.C_IDINSTITUCION,		
			ScsSaltosCompensacionesBean.C_IDPERSONA,
			ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,		
			ScsSaltosCompensacionesBean.C_IDTURNO,
			ScsSaltosCompensacionesBean.C_MOTIVOS,				
			ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,
			ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS, 
			ScsSaltosCompensacionesBean.C_USUMODIFICACION,
			ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION, 
			ScsSaltosCompensacionesBean.C_TIPOMANUAL
		};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsSaltosCompensacionesBean.C_IDINSTITUCION,		
							ScsSaltosCompensacionesBean.C_IDTURNO,
							ScsSaltosCompensacionesBean.C_IDSALTOSTURNO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsSaltosCompensacionesBean bean = null;
		try{
			bean = new ScsSaltosCompensacionesBean();
			bean.setFecha				(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_FECHA));
			bean.setFechaCumplimiento	(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO));			
			bean.setIdGuardia			(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDGUARDIA));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getLong(hash, ScsSaltosCompensacionesBean.C_IDPERSONA));
			bean.setIdSaltosTurno		(UtilidadesHash.getLong(hash, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO));
			bean.setIdTurno				(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDTURNO));
			bean.setMotivos				(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_MOTIVOS));
			bean.setSaltoCompensacion	(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION));			
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdCalendarioGuardiasCreacion(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION));
			bean.setTipoManual(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_TIPOMANUAL));
			
			bean.setFechaMod			(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_USUMODIFICACION));
			
		} catch(Exception e){
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
		try {
			hash = new Hashtable();
			ScsSaltosCompensacionesBean b = (ScsSaltosCompensacionesBean) bean;
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, b.getFechaCumplimiento());			
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, String.valueOf(b.getIdSaltosTurno()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_MOTIVOS, b.getMotivos());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, b.getSaltoCompensacion());			
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS, String.valueOf(b.getIdCalendarioGuardias()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION, String.valueOf(b.getIdCalendarioGuardiasCreacion()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_TIPOMANUAL, String.valueOf(b.getTipoManual()));
			
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_USUMODIFICACION, b.getUsuMod());
			
		} catch (Exception e){
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
	 * Calcula un nuevo id IDSALTOSTURNOS para la tabla.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_SALTOSCOMPENSACIONES
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String nuevoIdSaltosTurnos(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idturno="";
		
		try {
			idinstitucion = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			idturno = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
				
			consulta = "SELECT MAX(saltos."+ScsSaltosCompensacionesBean.C_IDSALTOSTURNO+") +1 AS "+ScsSaltosCompensacionesBean.C_IDSALTOSTURNO;
			consulta += " FROM "+ScsSaltosCompensacionesBean.T_NOMBRETABLA+" saltos";
			consulta += " WHERE ";
			consulta += " saltos."+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsSaltosCompensacionesAdm.nuevoIdSaltosTurnos(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	

	/** 
	 * Calcula un nuevo id IDSALTOSTURNOS para la tabla.
	 * 
	 * @param String idInstitucion
	 * @param String idTurno
	 * @return String con el nuevo identificador.
	 * @throws ClsExceptions
	 */	
	public String getNuevoIdSaltosTurno(String idInstitucion, String idTurno) throws ClsExceptions {
		String nuevoId = "";
		Hashtable miHash = new Hashtable();
		
		try {
			miHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
			miHash.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
			nuevoId = this.getNuevoId(miHash);
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en ScsSaltosCompensacionesAdm.getNuevoIdSaltosTurno().");
		}
		return nuevoId;		
	}
	
	/** 
	 * Calcula un nuevo id IDSALTOSTURNOS para la tabla.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_SALTOSCOMPENSACIONES
	 * @return String con el nuevo identificador.
	 * @throws ClsExceptions
	 */	
	public String getNuevoId(Hashtable miHash)throws ClsExceptions {
		Vector registros = new Vector();
		String nuevoId = "";
		
		try {
			registros = this.selectGenerico(this.nuevoIdSaltosTurnos(miHash));
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en ScsSaltosCompensacionesAdm.getNuevoId().");
		}
		nuevoId = (String)((Hashtable)registros.get(0)).get(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);
		if (nuevoId == null||nuevoId.equalsIgnoreCase(""))
			return "1";
		else 
			return nuevoId;		
	}
	
	/**
	 * Inserta en un vector cada fila como una tabla hash del resultado de ejecutar la query
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
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	
	
	/** 
	 * Devuelve la consulta SQL de la búsqueda de Turnos, Guardias y Letrados con saltos o compensaciones.
	 * 
	 * IMPORTANTE: Con los parametros pasados a este metodo, no es posible obtener saltos/compensaciones que sean de Turno exclusivamente.
	 * Esto es porque cuando el idguardia=null, no sabemos si lo que se quiere es sacar:
	 *  a. Todos los saltos/compensaciones, o bien
	 *  b. Solo los saltos/compensaciones para Turno/Designaciones.
	 * 
	 * @param Hashtable registros: tabla hash con los datos de la pantalla para realizar la busqueda.
	 * @return String: tiene la consulta SQL a ejecutar
	 * @throws ClsExceptions
	 */	
	public String buscar(Hashtable registros) throws ClsExceptions {
		String consulta = "";
		String fechaDesde="", fechaHasta="", idTurno="", idGuardia="", idPersona="", salto="", compensado="";		

		try { 
			//Datos iniciales:
			fechaDesde = UtilidadesHash.getString(registros,"FECHADESDE"); 
			fechaHasta = UtilidadesHash.getString(registros,"FECHAHASTA");
			idTurno = UtilidadesHash.getString(registros,"IDTURNO");
			idGuardia = UtilidadesHash.getString(registros,"IDGUARDIA");
			idPersona = UtilidadesHash.getString(registros,"IDPERSONA");
			salto = UtilidadesHash.getString(registros,"SALTO");
			compensado = UtilidadesHash.getString(registros,"COMPENSADO");
			
			//Consulta:
			consulta  = "SELECT ";
			consulta += " saltos.*,";
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" AS LETRADO,";
			consulta += " DECODE(coleg.COMUNITARIO,'1',coleg.NCOMUNITARIO, coleg.NCOLEGIADO) AS NUMERO, ";
			consulta += " turno."+ScsTurnoBean.C_NOMBRE+" AS NOMBRETURNO,";
			consulta += " guardia."+ScsGuardiasTurnoBean.C_NOMBRE+" AS NOMBREGUARDIA ";
			consulta += " FROM "+ScsSaltosCompensacionesBean.T_NOMBRETABLA+" saltos, ";
			consulta += ScsTurnoBean.T_NOMBRETABLA+" turno, ";
			consulta += ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardia, ";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso, ";
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg ";
			consulta += " WHERE ";
			consulta += " saltos."+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+UtilidadesHash.getString(registros,"IDINSTITUCION");
			if(salto!=null)
				consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+"='"+salto+"'";
			if (!idTurno.equals(""))
				consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idTurno;
			if (!idGuardia.equals(""))
				consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idGuardia;
			if (!idPersona.equals(""))
				consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_IDPERSONA+"="+idPersona;
			if (compensado!=null && compensado.equals("S"))
				consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" > TO_DATE('01/01/2001','DD/MM/YYYY')";
			if (compensado!=null && compensado.equals("N"))
				consulta += " AND saltos."+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" is null ";
			
			if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
				if (!fechaDesde.equals(""))
					fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
				if (!fechaHasta.equals(""))
					fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);
				consulta += " AND " + GstDate.dateBetweenDesdeAndHasta("saltos."+ScsSaltosCompensacionesBean.C_FECHA, fechaDesde, fechaHasta);
			}
			
			//JOINS
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=saltos."+ScsSaltosCompensacionesBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=saltos."+ScsSaltosCompensacionesBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=saltos."+ScsSaltosCompensacionesBean.C_IDINSTITUCION;			
			consulta += " AND turno."+ScsTurnoBean.C_IDINSTITUCION+"=saltos."+ScsSaltosCompensacionesBean.C_IDINSTITUCION;
			consulta += " AND turno."+ScsTurnoBean.C_IDTURNO+"=saltos."+ScsSaltosCompensacionesBean.C_IDTURNO;
			consulta += " AND guardia."+ScsGuardiasTurnoBean.C_IDINSTITUCION+"(+)=saltos."+ScsSaltosCompensacionesBean.C_IDINSTITUCION;
			consulta += " AND guardia."+ScsGuardiasTurnoBean.C_IDTURNO+"(+)=saltos."+ScsSaltosCompensacionesBean.C_IDTURNO;
			consulta += " AND guardia."+ScsGuardiasTurnoBean.C_IDGUARDIA+"(+)=saltos."+ScsSaltosCompensacionesBean.C_IDGUARDIA;
			//ORDENACION
			consulta += " ORDER BY saltos."+ScsSaltosCompensacionesBean.C_FECHA+" desc";
			consulta += "        , saltos."+ScsSaltosCompensacionesBean.C_IDSALTOSTURNO+" desc";
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesAdm.buscar() en la consulta:"+consulta);
		}
		return consulta;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_SALTOSCOMPENSACIONES con los datos introducidos.
	 *  
	 * Devuelve los colegiados del colegio que tengan saltos/compensaciones no cumplidos en:
	 *  a. algun turno (no en guardias): si idTurno=null y idGuardia=null
	 *  b. el turno pasado (no en guardias): si idTurno!=null y idGuardia=null
	 *  c. la guardia pasada: si idTurno!=null y idGuardia!=null
	 *  
	 * @param institucion Codigo institucion seleccionada
	 * @param idTurno Codigo del Turno seleccionado
	 * @param idGuardia Codigo guardia seleccionada
	 * @param soc Indica si se va aconsultar sobre saltos o compensaciones
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectSaltosCompensaciones(String institucion, String idTurno,String idGuardia, String soc){
		Vector vResult = null;
		try{
			String consulta =
				"select decode(C."+CenColegiadoBean.C_COMUNITARIO+",'"+ClsConstants.DB_TRUE+"',"+"C."+CenColegiadoBean.C_NCOMUNITARIO+","+CenColegiadoBean.C_NCOLEGIADO+") "+CenColegiadoBean.C_NCOLEGIADO+", "+
				"P."+CenPersonaBean.C_NOMBRE+", "+
				"P."+CenPersonaBean.C_APELLIDOS1+", "+
				"P."+CenPersonaBean.C_APELLIDOS2+", "+
				"SC.NUMERO "+
				" from "+
				CenColegiadoBean.T_NOMBRETABLA+" C, "+
				CenPersonaBean.T_NOMBRETABLA+" P, "+
				"(select "+ScsSaltosCompensacionesBean.C_IDPERSONA+", count(1) NUMERO, ";
				
			// Los siguientes minimos se usan para ordenar en la lista de saltos/compensaciones (SCs). 
			// el orden principal es por cantidad de saltos/compensaciones de forma descendente.
			// Sin embargo, como ayuda, también se añade el orden por fecha y orden de creacion. Pero este orden solo será válido si cada colegiado tiene 1 unico SC. En caso contrario, el orden no se garantiza.
			consulta += "min("+ScsSaltosCompensacionesBean.C_FECHA+") MINFECHA, min("+ScsSaltosCompensacionesBean.C_IDSALTOSTURNO+") MINSALTO";
			
			consulta += " from "  +ScsSaltosCompensacionesBean.T_NOMBRETABLA;
			consulta += getWhereSaltosCompensacionesNocumplidos(institucion, idTurno, idGuardia, null, soc.charAt(0));
			consulta +=" group by "+ScsSaltosCompensacionesBean.C_IDPERSONA+") SC"+
			" where SC."+ScsSaltosCompensacionesBean.C_IDPERSONA+"=C."+CenColegiadoBean.C_IDPERSONA+
			" and   SC."+ScsSaltosCompensacionesBean.C_IDPERSONA+"=P."+CenPersonaBean.C_IDPERSONA+
			" and   C."+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+institucion;
			
			// El orden principal es por cantidad. Los siguientes campos de orden son solo orientativos (ver origen arriba) 
			consulta += " order by SC.NUMERO desc, SC.MINFECHA, SC.MINSALTO";
						
			vResult=this.find(consulta).getAll();
			
		}catch(ClsExceptions e){
			e.printStackTrace();
		}
		return vResult;
	}
	
	
	/**
	 * Quita el cumplimiento de saltos y compensaciones del calendario pasado como parametro
	 * Da error si no se pasa idinstitucion o idturno o idguardia o idcalendarioguardias.
	 */
	public boolean updateSaltosCompensacionesCumplidos(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			

			sql.append(" update "+ScsSaltosCompensacionesBean.T_NOMBRETABLA);
			sql.append("    set "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+"= NULL");
			sql.append("      , "+ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS+"= NULL");
			sql.append("      , "+ScsSaltosCompensacionesBean.C_FECHAMODIFICACION+"= SYSDATE");
			sql.append("      , "+ScsSaltosCompensacionesBean.C_USUMODIFICACION+"="+this.usuModificacion);
			sql.append("      , "+ScsSaltosCompensacionesBean.C_MOTIVOS+"=REGEXP_REPLACE(");
			sql.append(ScsSaltosCompensacionesBean.C_MOTIVOS+", ':id="+idcalendarioguardias+":.*:finid="+idcalendarioguardias+":',' ')");			
			sql.append("  where "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append("    and "+ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append("    and "+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno);
			sql.append("    and "+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idguardia);
			
			updateSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}

	/**
	 * Marca el cumplimiento de saltos y compensaciones ya que se ha dado de baja en el turno SRL
	 * Si se da de baja de un turno, da de baja los saltos y compensaciones del turno y de todas las guardias del turno.
	 * Si se da de baja de una guardia, da de baja los saltos y compensacion de la guardia.
	 */
	
	public boolean updateSaltosCompensacionesBajaTurno(Integer idinstitucion,Integer idturno,Long idpersona, Integer idguardia, String tipoSyC) throws ClsExceptions {
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
				sql.append(" update "+ScsSaltosCompensacionesBean.T_NOMBRETABLA);
				sql.append("    set "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+"= SYSDATE");
				sql.append("      , "+ScsSaltosCompensacionesBean.C_FECHAMODIFICACION+"= SYSDATE");
				sql.append("      , "+ScsSaltosCompensacionesBean.C_USUMODIFICACION+"="+this.usuModificacion);
				sql.append("  where "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+ idinstitucion);
				sql.append("    and "+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno);
				sql.append("    and "+ScsSaltosCompensacionesBean.C_IDPERSONA+"="+idpersona);
				sql.append("    and "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" is null");
				if (idguardia!=null)
					sql.append("    and "+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idguardia);
				if (tipoSyC.equalsIgnoreCase("G"))// && idguardia==null)
					sql.append("    and "+ScsSaltosCompensacionesBean.C_IDGUARDIA+" is not null");
				
				updateSQL(sql.toString());
				
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}
	
	
	/**
	 * Borra los saltos y compensaciones creados en el caledario pasado como parametro
	 */
	public boolean deleteSaltosCompensacionesCreadosEnCalendario(Hashtable hash) throws ClsExceptions {
		boolean salida;
		
		try {
			String idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			String idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			String idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			String idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
			
			StringBuffer sql = new StringBuffer();
			sql.append(" delete from "+ScsSaltosCompensacionesBean.T_NOMBRETABLA);
			sql.append("  where "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append("    and "+ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION+"="+idcalendarioguardias);
			sql.append("    and ("+ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append("     or  "+ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS+" is null)");
			sql.append("    and "+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno);
			sql.append("    and "+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idguardia);
			
			deleteSQL(sql.toString());		
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	} //deleteSaltosCreadosEnCalendario()
	
	/**
	 * Elimina saltos y compensaciones de calendarios que ya no existen en la guardia.
     *
	 * @param Hashtable hash: tabla hash con los campos: 
	 * - String idinstitucion
	 * - String idcalendarioguardias
	 * - String idturno
	 * - String idguardia  
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deleteSaltosCompensacionesCalendariosInexistentes(Hashtable hash) throws ClsExceptions
	{
		String idinstitucion = "", idturno = "", idguardia = "";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();

		try {
			idinstitucion = (String) hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idturno = (String) hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String) hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);

			sql.append(" delete from " + ScsSaltosCompensacionesBean.T_NOMBRETABLA + " SC");
			sql.append(" where SC." + ScsSaltosCompensacionesBean.C_IDINSTITUCION + "=" + idinstitucion);
			sql.append(" and SC." + ScsSaltosCompensacionesBean.C_IDTURNO + "=" + idturno);
			sql.append(" and SC." + ScsSaltosCompensacionesBean.C_IDGUARDIA + "=" + idguardia);
			sql.append(" and SC." + ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS + " is null");
			sql.append(" and SC." + ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION + " is not null");
			sql.append(" and not exists (");
			sql.append(" select 1 from " + ScsCalendarioGuardiasBean.T_NOMBRETABLA + " CG");
			sql.append(" where CG." + ScsCalendarioGuardiasBean.C_IDINSTITUCION + "=" + idinstitucion);
			sql.append(" and CG." + ScsCalendarioGuardiasBean.C_IDTURNO + "=" + idturno);
			sql.append(" and CG." + ScsCalendarioGuardiasBean.C_IDGUARDIA + "=" + idguardia);
			sql.append(" and CG." + ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS + "= SC."
					+ ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION);
			sql.append(")");

			this.deleteSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}
	
	public void insertarSaltoCompensacion(ScsSaltosCompensacionesBean salto) throws ClsExceptions{
		
		salto.setUsuMod(this.usuModificacion);
		salto.setFechaMod("sysdate");
		
		Long idSaltosTurno = Long.valueOf(getNuevoIdSaltosTurno(salto.getIdInstitucion().toString(),salto.getIdTurno().toString()));
		salto.setIdSaltosTurno(idSaltosTurno);
		this.insert(salto);
		
		
	}
	
	/**
	 * Construye el where para consultar los saltos/compensaciones no cumplidos.
	 * - En funcion del parametro saltoocompensacion:
	 *   a. 'S': buscara saltos
	 *   b. 'C': buscara compensaciones
	 * - En funcion del parametro idguardia:
	 *   a. null:    buscara saltos/compensaciones de Turno
	 *   b. != null: buscara saltos/compensaciones de la guardia indicada
	 *   
	 * @param idinstitucion
	 * @param idturno
	 * @param idguardia
	 * @param idpersona
	 * @param saltoocompensacion
	 * @return
	 */
	private String getWhereSaltosCompensacionesNocumplidos(String idinstitucion, String idturno, String idguardia, String idpersona, char saltoocompensacion) {
		StringBuilder where = new StringBuilder();
		
		where.append(" WHERE ");
		where.append(ScsSaltosCompensacionesBean.C_IDINSTITUCION);
		where.append("=");
		where.append(idinstitucion);
		if (idturno != null && !idturno.equals("")) {
			where.append("   AND ");
			where.append(ScsSaltosCompensacionesBean.C_IDTURNO);
			where.append("=");
			where.append(idturno);
		}
		where.append("   AND ");
		where.append(ScsSaltosCompensacionesBean.C_IDGUARDIA);
		if (idguardia != null && !idguardia.equals("")) {
			where.append("=");
			where.append(idguardia);
		} else {
			where.append(" IS NULL ");
		}
		if (idpersona != null && !idpersona.equals("")) {
			where.append("   AND ");
			where.append(ScsSaltosCompensacionesBean.C_IDPERSONA);
			where.append("=");
			where.append(idpersona);
		}
		if (saltoocompensacion != ' ') {
			where.append("   AND ");
			where.append(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
			where.append("= '");
			where.append(saltoocompensacion);
			where.append("'");
		}
		where.append("   AND ");
		where.append(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
		where.append(" IS NULL ");
		
		return where.toString();
	}

	/**
	 * Cumple un salto/compensacion dado.
	 * Cuidado: si no se indica turno/guardia, se pueden cumplir muchos a la vez.
	 * 
	 * @param saltoCompensacion
	 * @throws ClsExceptions
	 */
	public void marcarSaltoCompensacion(ScsSaltosCompensacionesBean saltoCompensacion) throws ClsExceptions
	{
		try {
			String s_idinstitucion = saltoCompensacion.getIdInstitucion().toString();
			String s_idturno = null;
			if (saltoCompensacion.getIdTurno() != null) {
				s_idturno = saltoCompensacion.getIdTurno().toString();
			}
			String s_idguardia = null;
			if (saltoCompensacion.getIdGuardia() != null) {
				s_idguardia = saltoCompensacion.getIdGuardia().toString();
			}
			String s_idpersona = null;
			if (saltoCompensacion.getIdPersona() != null) {
				s_idpersona = saltoCompensacion.getIdPersona().toString();
			}
			String s_saltocompensacion = null;
			if (saltoCompensacion.getSaltoCompensacion() != null) {
				s_saltocompensacion = saltoCompensacion.getSaltoCompensacion();
				if (s_saltocompensacion.equals(""))
					s_saltocompensacion = " ";
			}

			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE ");
			sql.append(ScsSaltosCompensacionesBean.T_NOMBRETABLA);
			sql.append(" SET ");
			sql.append(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
			sql.append(" = '");
			sql.append(saltoCompensacion.getFechaCumplimiento());
			sql.append("', ");
			sql.append(ScsSaltosCompensacionesBean.C_USUMODIFICACION);
			sql.append(" = ");
			sql.append(this.usrbean.getUserName());
			sql.append(",");
			sql.append(ScsSaltosCompensacionesBean.C_FECHAMODIFICACION);
			sql.append(" = SYSDATE  ");
			if (saltoCompensacion.getIdGuardia() != null) {
				sql.append(" , ");
				sql.append(ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS);
				sql.append(" = ");
				sql.append(saltoCompensacion.getIdCalendarioGuardias());
				sql.append(" ");
			}
			if (saltoCompensacion.getMotivos() != null && !saltoCompensacion.getMotivos().equals("")) {
				sql.append(" , ");
				sql.append(ScsSaltosCompensacionesBean.C_MOTIVOS);
				sql.append(" = ");
				sql.append(ScsSaltosCompensacionesBean.C_MOTIVOS);
				sql.append(" || '");
				sql.append(saltoCompensacion.getMotivos());
				sql.append("' ");
			}

			sql.append(getWhereSaltosCompensacionesNocumplidos(s_idinstitucion, s_idturno, s_idguardia, s_idpersona,
					s_saltocompensacion.charAt(0)));

			if (saltoCompensacion.getIdSaltosTurno() != null) {
				sql.append(" AND ");
				sql.append(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);
				sql.append(" = '");
				sql.append(saltoCompensacion.getIdSaltosTurno());
			}

			sql.append(" AND rownum=1");

			this.updateSQL(sql.toString());

		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en marcarSaltoCompensacionBBDD." + e.toString());
		}
	} //marcarSaltoCompensacion()
	
	private String getQuerySaltosCompensacionesActivos(String tipo,
			Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia)
	{
		String s_idinstitucion = idInstitucion.toString();
		String s_idturno = null;
		if (idTurno != null) {
			s_idturno = idTurno.toString();
		}
		String s_idguardia = null;
		if (idGuardia != null) {
			s_idguardia = idGuardia.toString();
		}
		String s_saltocompensacion = null;
		if (tipo != null) {
			s_saltocompensacion = tipo;
			if (s_saltocompensacion.equals(""))
				s_saltocompensacion = " ";
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select * ");
		sql.append("   from ");
		sql.append(ScsSaltosCompensacionesBean.T_NOMBRETABLA);
		sql.append(getWhereSaltosCompensacionesNocumplidos(s_idinstitucion, s_idturno, s_idguardia, null, s_saltocompensacion.charAt(0)));
		sql.append(" order by ");
		sql.append(ScsSaltosCompensacionesBean.C_FECHA);
		sql.append(", ");
		sql.append(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);

		return sql.toString();
	} //getQuerySaltosCompensacionesActivos()
	

	/**
	 * Obtiene todos los saltos asociados a una guardia
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public HashMap<Long, ArrayList<LetradoInscripcion>> getSaltos(Integer idInstitucion, Integer idTurno, Integer idGuardia) throws ClsExceptions
	{
		// Controles
		CenPersonaAdm perAdm = new CenPersonaAdm(this.usrbean);

		// Variables
		HashMap<Long, ArrayList<LetradoInscripcion>> hmPersonasConSaltos;
		ArrayList<LetradoInscripcion> alLetradosSaltados;
		LetradoInscripcion letradoSeleccionado;
		Hashtable<String, Object> htFila;
		Long idPersona;
		
		// obteniendo query de saltos
		String sql = getQuerySaltosCompensacionesActivos(ClsConstants.SALTOS, idInstitucion, idTurno, idGuardia);
		
		try {
			RowsContainer rc = this.find(sql);
			if (rc == null) {
				hmPersonasConSaltos = null;
			}
			else {
				hmPersonasConSaltos = new HashMap<Long, ArrayList<LetradoInscripcion>>();
				for (int i = 0; i < rc.size(); i++) {
					htFila = ((Row) rc.get(i)).getRow();
	
					idPersona = Long.valueOf((String) htFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA));
					letradoSeleccionado = new LetradoInscripcion(perAdm.getPersonaPorId(idPersona.toString()), idInstitucion,
							idTurno, idGuardia, ClsConstants.SALTOS);
	
					if (hmPersonasConSaltos.containsKey(idPersona))
						alLetradosSaltados = hmPersonasConSaltos.get(idPersona);
					else
						alLetradosSaltados = new ArrayList<LetradoInscripcion>();
	
					alLetradosSaltados.add(letradoSeleccionado);
					hmPersonasConSaltos.put(idPersona, alLetradosSaltados);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al comporbar si hay salto en BD.");
		}

		return hmPersonasConSaltos;
	}

	/**
	 * Obtiene todas las compensaciones asociadas a una guardia
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public ArrayList<LetradoInscripcion> getCompensaciones(Integer idInstitucion, Integer idTurno, Integer idGuardia,String fecha) throws ClsExceptions
	{
		// Controles
		// Variables
		ArrayList<LetradoInscripcion> alLetradosCompensados;
		LetradoInscripcion letradoSeleccionado;
		Hashtable<String, Object> htFila;
		String idPersona;
		
		
		// obteniendo query de compensaciones
		String sql = getQuerySaltosCompensacionesActivos(ClsConstants.COMPENSACIONES, idInstitucion, idTurno,
				idGuardia);

		try {
			// obtiene las compesaciones de letrados que no esten de baja en la guardia
			RowsContainer rc = this.find(sql);
			if (rc == null) {
				alLetradosCompensados = null;
			}
			else {
				alLetradosCompensados = new ArrayList<LetradoInscripcion>();
				for (int i = 0; i < rc.size(); i++) {
					htFila = ((Row) rc.get(i)).getRow();
	
					idPersona = (String) htFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
					if( idGuardia!=null ){
						ScsInscripcionGuardiaAdm inscripcionAdm = new ScsInscripcionGuardiaAdm(this.usrbean);
						ScsInscripcionGuardiaBean inscripcionGuardia; inscripcionGuardia = inscripcionAdm.getInscripcionGuardiaActiva(idInstitucion.toString(), idTurno.toString(),
								idGuardia.toString(), idPersona, fecha);
						if(inscripcionGuardia == null)
							continue;
						letradoSeleccionado = new LetradoInscripcion(inscripcionGuardia);
						letradoSeleccionado.setSaltoCompensacion("C");
						alLetradosCompensados.add(letradoSeleccionado);
					}else{
						ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(this.usrbean);
						ScsInscripcionTurnoBean inscripcionTurno = inscripcionAdm.getInscripcionTurnoActiva(idInstitucion.toString(), idTurno.toString(), idPersona, fecha);
						if(inscripcionTurno == null)
							continue;
						letradoSeleccionado = new LetradoInscripcion(inscripcionTurno);
						letradoSeleccionado.setSaltoCompensacion("C");
						alLetradosCompensados.add(letradoSeleccionado);
						
					}
					
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener letrados compensados  en BD.");
		}

		return alLetradosCompensados;
	}

	/**
	 * Obtiene las compensaciones asociadas a un turno
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<LetradoInscripcion> getLetradosSaltosCompensacionesTurno(String idInstitucion, String idTurno,String fecha) throws ClsExceptions
	{
		// Controles
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(this.usrbean);
		ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(this.usrbean);

		// Variables
		Vector<LetradoInscripcion> alLetradosCompensados;
		LetradoInscripcion letradoSeleccionado;
		Hashtable<String, Object> htFila;
		String idPersona;
		ScsInscripcionTurnoBean inscripcionTurno;

		// obteniendo query de saltos
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select p." + CenPersonaBean.C_IDPERSONA + ", ");
		sBuffer.append("       p." + CenPersonaBean.C_NIFCIF + ", ");
		sBuffer.append("       c." + CenColegiadoBean.C_NCOLEGIADO + ", ");
		sBuffer.append("       p." + CenPersonaBean.C_NOMBRE + ", ");
		sBuffer.append("       p." + CenPersonaBean.C_APELLIDOS1 + ", ");
		sBuffer.append("       p." + CenPersonaBean.C_APELLIDOS2 + ", ");
		sBuffer.append("       sc." + ScsSaltosCompensacionesBean.C_IDSALTOSTURNO + ", ");
		sBuffer.append("       sc." + ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + " ");
		sBuffer.append("  from " + ScsSaltosCompensacionesBean.T_NOMBRETABLA + " sc, ");
		sBuffer.append("       " + CenPersonaBean.T_NOMBRETABLA + " p, ");
		sBuffer.append("       " + CenColegiadoBean.T_NOMBRETABLA + " c ");
		sBuffer.append(" where sc." + ScsSaltosCompensacionesBean.C_IDPERSONA + " = p." + CenPersonaBean.C_IDPERSONA);
		sBuffer.append("   and sc." + ScsSaltosCompensacionesBean.C_IDPERSONA + " = c." + CenColegiadoBean.C_IDPERSONA);
		sBuffer.append("   and sc." + ScsSaltosCompensacionesBean.C_IDINSTITUCION + " = " + idInstitucion);
		sBuffer.append("   and sc." + ScsSaltosCompensacionesBean.C_IDINSTITUCION + " = c." + CenColegiadoBean.C_IDINSTITUCION);
		sBuffer.append("   and sc." + ScsSaltosCompensacionesBean.C_IDTURNO + " = " + idTurno);
		sBuffer.append("   and sc." + ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " is null ");
		sBuffer.append("   and sc." + ScsSaltosCompensacionesBean.C_IDGUARDIA + " is null ");
		sBuffer.append(" order by sc." + ScsSaltosCompensacionesBean.C_FECHA);
		sBuffer.append("        , sc." + ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);

		try {
			RowsContainer rc = this.find(sBuffer.toString());
			if (rc == null) {
				alLetradosCompensados = null;
			} else {
				alLetradosCompensados = new Vector<LetradoInscripcion>();
				for (int i = 0; i < rc.size(); i++) {
					htFila = ((Row) rc.get(i)).getRow();

					idPersona = (String) htFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
					inscripcionTurno = inscripcionAdm.getInscripcionTurnoActiva(idInstitucion.toString(),
							idTurno.toString(), idPersona, fecha);
					if(inscripcionTurno==null)
						continue;
					letradoSeleccionado = new LetradoInscripcion(inscripcionTurno);
					letradoSeleccionado.setSaltoCompensacion(UtilidadesHash.getString(htFila,
							ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION));
					letradoSeleccionado.setIdSaltoCompensacion(UtilidadesHash.getString(htFila,
							ScsSaltosCompensacionesBean.C_IDSALTOSTURNO));
					alLetradosCompensados.add(letradoSeleccionado);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}

		return alLetradosCompensados;
	} // getLetradosSaltosCompensacionesTurno()
	
	/**
	 * Crea salto o compensacion según los parámetros obtenidos en la busqueda SJCS
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idPersona
	 * @param checkSalto
	 * @param motivo
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void crearSaltoCompensacion(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idPersona,
			
			String motivo,String tipoSaltaCompensacion) throws ClsExceptions, SIGAException
	{
		crearSaltoCompensacion(idInstitucion, idTurno, idGuardia, idPersona, motivo, null, tipoSaltaCompensacion);
	}
	public void crearSaltoCompensacion(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idPersona,			
			String motivo,
			String idCalendarioGuardiasCreacion,
			String tipoSaltaCompensacion) throws ClsExceptions, SIGAException
	{

		
				// cuando hay que insertar salto
				Hashtable hash = new Hashtable();
				hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, idInstitucion);
				hash.put(ScsSaltosCompensacionesBean.C_IDTURNO, idTurno);
				if (idGuardia != null) {
					hash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA, idGuardia);
				}
				hash.put(ScsSaltosCompensacionesBean.C_MOTIVOS, motivo);
				hash.put(ScsSaltosCompensacionesBean.C_IDPERSONA, idPersona);
				hash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, tipoSaltaCompensacion);
				hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, getNuevoIdSaltosTurno	(idInstitucion,idTurno));
				if (idCalendarioGuardiasCreacion != null)
					hash.put(ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION, idCalendarioGuardiasCreacion);
				hash.put(ScsSaltosCompensacionesBean.C_FECHA, "SYSDATE");
				if (!insert(hash)) {
					throw new ClsExceptions("Error insertando salto: " + getError());
				}
			
	}

	public void cumplirCompensacionDesdeEjg(Hashtable miHash) throws ClsExceptions
	{
		// cumpliendo la compensacion si existe y el tipo de letrado es correcto
		if (miHash.get(ScsEJGBean.C_TIPOLETRADO).toString().equalsIgnoreCase("P")) {

			// buscando si tiene compensaciones
			String where = this.getWhereSaltosCompensacionesNocumplidos(
					(String) miHash.get(ScsEJGBean.C_IDINSTITUCION),
					(String) miHash.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO),
					(String) miHash.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA),
					(String) miHash.get(ScsEJGBean.C_IDPERSONA), 'C');
			Vector registros = this.selectForUpdate(where); // Si hay compensacion

			if (registros.size() > 0) {
				Hashtable<String, String> hashTemporal = new Hashtable<String, String>();
				String idsaltosturno = (((ScsSaltosCompensacionesBean) registros.get(0)).getIdSaltosTurno()).toString();

				// anotando la fecha de cumplimiento
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, (String)miHash.get(ScsEJGBean.C_IDINSTITUCION));
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, (String)idsaltosturno);
				hashTemporal.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, (String)miHash.get(ScsEJGBean.C_FECHAAPERTURA));
				String claves[] = { ScsSaltosCompensacionesBean.C_IDINSTITUCION,
						ScsSaltosCompensacionesBean.C_IDSALTOSTURNO };
				String campos[] = { ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO };
				// Actualizo la fecha de cumplimiento
				this.updateDirect(hashTemporal, claves, campos);
			}
		}

	}

	public void crearSaltoDesdeEjg(DefinirEJGForm miForm, String checkSalto) throws ClsExceptions, SIGAException
	{
		// creando salto si se pidio asi desde la pantalla
		if (checkSalto != null && (checkSalto.equals("on") || checkSalto.equals("1"))) {
			String idInstitucionSJCS = this.usrbean.getLocation();
			String idTurnoSJCS = miForm.getGuardiaTurnoIdTurno();
			String idGuardiaSJCS = miForm.getGuardiaTurnoIdGuardia();
			String idPersonaSJCS = miForm.getIdPersona();
			String motivoSaltoSJCS = 
					UtilidadesString.getMensajeIdioma(this.usrbean, "gratuita.literal.insertarSaltoPor")
					+ " "
					+ UtilidadesString.getMensajeIdioma(this.usrbean, "general.boton.crearEJG");
			
			this.crearSaltoCompensacion(idInstitucionSJCS, idTurnoSJCS, idGuardiaSJCS, idPersonaSJCS, motivoSaltoSJCS,
					ClsConstants.SALTOS);
		}
	}
	
}