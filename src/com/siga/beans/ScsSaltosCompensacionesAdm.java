
package com.siga.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.EjecucionPLs;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

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
		String[] campos = {	ScsSaltosCompensacionesBean.C_FECHA,				ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,
							ScsSaltosCompensacionesBean.C_FECHAMODIFICACION,	ScsSaltosCompensacionesBean.C_IDGUARDIA,
							ScsSaltosCompensacionesBean.C_IDINSTITUCION,		ScsSaltosCompensacionesBean.C_IDPERSONA,
							ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,		ScsSaltosCompensacionesBean.C_IDTURNO,
							ScsSaltosCompensacionesBean.C_MOTIVOS,				ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,
							ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS, ScsSaltosCompensacionesBean.C_USUMODIFICACION,
							ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsSaltosCompensacionesBean.C_IDINSTITUCION,		ScsSaltosCompensacionesBean.C_IDTURNO,
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
			bean.setFechaMod			(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_FECHAMODIFICACION));
			bean.setIdGuardia			(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDGUARDIA));
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDINSTITUCION));
			bean.setIdPersona			(UtilidadesHash.getLong(hash, ScsSaltosCompensacionesBean.C_IDPERSONA));
			bean.setIdSaltosTurno		(UtilidadesHash.getLong(hash, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO));
			bean.setIdTurno				(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDTURNO));
			bean.setMotivos				(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_MOTIVOS));
			bean.setSaltoCompensacion	(UtilidadesHash.getString (hash, ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_USUMODIFICACION));
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdCalendarioGuardiasCreacion(UtilidadesHash.getInteger(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION));
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
			ScsSaltosCompensacionesBean b = (ScsSaltosCompensacionesBean) bean;
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, b.getFechaCumplimiento());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, String.valueOf(b.getIdSaltosTurno()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_MOTIVOS, b.getMotivos());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, b.getSaltoCompensacion());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS, String.valueOf(b.getIdCalendarioGuardias()));
			UtilidadesHash.set(hash, ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION, String.valueOf(b.getIdCalendarioGuardiasCreacion()));
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
	 * Actualiza en base de datos la fecha de cumplimiento.
	 * 
	 * @param String idinstitucion
	 * @param String idturno
	 * @param String idguardia
	 * @param String idpersona
	 * @return void
	 * @throws ClsExceptions
	 */	
	public void anotarFechaCumplimiento(String idinstitucion, String idturno, String idguardia, String idpersona) throws ClsExceptions {
		Vector registros = new Vector();
		Hashtable hashTemporal = new Hashtable();
		String idsaltosturno = "";
		
		try {
			//Consulta para ver si tiene compensaciones
			String where = " WHERE "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+idinstitucion;
			where += " AND "+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno;
			where += " AND "+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idguardia;
			where += " AND "+ScsSaltosCompensacionesBean.C_IDPERSONA+"="+idpersona;
			where += " AND "+ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+"='S'";
			where += " AND "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" IS NULL ";
			registros = this.select(where);
			
			//Si tengo compensacion actualizo
			if (registros.size() > 0) {
				idsaltosturno = ((ScsSaltosCompensacionesBean)registros.get(0)).getIdSaltosTurno().toString();
				//Si la fecha de cumplimiento es nula: Anoto la fecha de cumplimiento
				hashTemporal.clear();
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,idinstitucion);
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDTURNO,idturno);
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,idguardia);
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDPERSONA,idpersona);
				hashTemporal.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,"S");
				hashTemporal.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,idsaltosturno);
				hashTemporal.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,"sysdate");
				String claves[] = {ScsSaltosCompensacionesBean.C_IDINSTITUCION,ScsSaltosCompensacionesBean.C_IDTURNO,
								   ScsSaltosCompensacionesBean.C_IDGUARDIA,ScsSaltosCompensacionesBean.C_IDPERSONA,
								   ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,ScsSaltosCompensacionesBean.C_IDSALTOSTURNO};
				String campos[] = {ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO};
				this.updateDirect(hashTemporal,claves,campos);
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesAdm.anotarFechaCumplimiento().");
		}
	}

	/** 
	 * Actualiza en base de datos la fecha de cumplimiento.
	 * 
	 * @param String idinstitucion
	 * @param String idturno
	 * @param String idsaltosturno
	 * @return void
	 * @throws ClsExceptions
	 */	
	public void setFechaCumplimiento(String idinstitucion, String idturno, String idsaltosturno) throws ClsExceptions {
		Hashtable hashTemporal = new Hashtable();
		
		try {
			//Anoto la fecha de cumplimiento
			hashTemporal.clear();
			hashTemporal.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,idinstitucion);
			hashTemporal.put(ScsSaltosCompensacionesBean.C_IDTURNO,idturno);
			hashTemporal.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,idsaltosturno);
			hashTemporal.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,"sysdate");
			String claves[] = {ScsSaltosCompensacionesBean.C_IDINSTITUCION,
							   ScsSaltosCompensacionesBean.C_IDTURNO,
					   		   ScsSaltosCompensacionesBean.C_IDSALTOSTURNO};
			String campos[] = {ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO};

			//Actualizo la fecha de cumplimiento 
			this.updateDirect(hashTemporal,claves,campos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesAdm.setFechaCumplimiento().");
		}
	}

	/** 
	 * Consulta si hay compensacion para un registro. Devuelve el idsaltosturno si existe la compensacion o vacio.
	 * 
	 * @param boolean RW: true si usamos el pool de lectura escritura.
	 * @param String idinstitucion
	 * @param String idturno
	 * @param String idguardia
	 * @param String idpersona
	 * @return String idsaltosturno: devuelve "" si no hay compensacion. Devuelve el idsaltosturno si hay compensacion
	 * @throws ClsExceptions
	 */	
	public String hayCompensacion(boolean RW, String idinstitucion, String idturno, String idguardia, String idpersona) throws ClsExceptions {
		String where = "", idsaltosturno="";
		Vector registros = new Vector();

		//Consulta para ver si tiene compensaciones
		where = " WHERE ";
		where += ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+idinstitucion;
		where += " AND "+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno;
		where += " AND "+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idguardia;
		where += " AND "+ScsSaltosCompensacionesBean.C_IDPERSONA+"="+idpersona;
		where += " AND "+ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+"='C'";
		where += " AND "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" IS NULL ";

		try { 
			registros.clear();
			if (RW)
				registros = this.selectForUpdate(where);
			else 
				registros = this.select(where);

			if (registros.size() > 0)  
				idsaltosturno = (((ScsSaltosCompensacionesBean)registros.get(0)).getIdSaltosTurno()).toString();
			else idsaltosturno = "";
			
			return idsaltosturno;	
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesAdm.hayCompensacion().");
		}
	}
	
	/** 
	 * Devuelve la consulta SQL de la búsqueda de Turnos, Guardias y Letrados con saltos o compensaciones.
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
			consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO+" AS NUMERO,";
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
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesAdm.buscar() en la consulta:"+consulta);
		}
		return consulta;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_SALTOSCOMPENSACIONES con los datos introducidos. 
	 * @param institucion Codigo institucion seleccionada
	 * @param inTurno Codigo del Turno seleccionado
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
				"(select "+ScsSaltosCompensacionesBean.C_IDPERSONA+", count(1) NUMERO"+
				" from "  +ScsSaltosCompensacionesBean.T_NOMBRETABLA+
				" where " +ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+institucion+
				" and   "+ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+"='"+soc+"'"+
				" and   "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" is null"+
			    " and   "+ScsSaltosCompensacionesBean.C_IDTURNO +"=" + idTurno ;
			if(idGuardia==null || idGuardia.trim().equals("")){
				consulta +=" and   "+ScsSaltosCompensacionesBean.C_IDGUARDIA+" is null";
			}else{
				consulta +=" and   "+ScsSaltosCompensacionesBean.C_IDGUARDIA+"="+idGuardia;
			}
			consulta +=" group by "+ScsSaltosCompensacionesBean.C_IDPERSONA+") SC"+
			" where SC."+ScsSaltosCompensacionesBean.C_IDPERSONA+"=C."+CenColegiadoBean.C_IDPERSONA+
			" and   SC."+ScsSaltosCompensacionesBean.C_IDPERSONA+"=P."+CenPersonaBean.C_IDPERSONA+
			" and   C."+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+institucion+
			" order by SC.NUMERO desc";
						
			vResult=this.find(consulta).getAll();
			
		}catch(ClsExceptions e){
			e.printStackTrace();
		}
		return vResult;
	}
	
	
	/**
	 * Quita el cumplimiento de saltos y compensaciones del calendario pasado como parametro
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
	
	/**
	 * A la hora de dar de baja de un turno se debe dar fecha de uso a los saltos y compensaciones
	 * pendientes para que no se asignen 
     *
	 * @param Hashtable hash: tabla hash con los campos: 
	 * - String idinstitucion
	 * - String idpersona
	 * - String idturno
	 * - String fechaCumplimiento
     * - String motivos  
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean updateCompensacionesSaltos(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", fecha = "", idpersona, comentario;
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsSaltosCompensacionesBean.C_IDINSTITUCION);
			idturno = (String)hash.get(ScsSaltosCompensacionesBean.C_IDTURNO);
			fecha = (String)hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
			idpersona = (String)hash.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
			comentario = (String)hash.get(ScsSaltosCompensacionesBean.C_MOTIVOS);

			sql.append(" update "+ScsSaltosCompensacionesBean.T_NOMBRETABLA);
			sql.append(" set "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+"= trunc(to_date('" + fecha + "', 'DD/MM/YYYY'))");
			sql.append(" , "+ScsSaltosCompensacionesBean.C_MOTIVOS+"='"+comentario+"'");
			sql.append(" , "+ScsSaltosCompensacionesBean.C_FECHAMODIFICACION+"= SYSDATE");
			sql.append(" , "+ScsSaltosCompensacionesBean.C_USUMODIFICACION+"="+this.usuModificacion);
			sql.append(" where "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsSaltosCompensacionesBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsSaltosCompensacionesBean.C_IDPERSONA+"="+idpersona);
			sql.append(" and "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" IS NULL");
					
			this.updateSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}
	public void insertarSaltoPorBajaTemporal(CenBajasTemporalesBean bajaTemporaBean,ScsSaltosCompensacionesBean salto) throws ClsExceptions{
		StringBuffer descripcion = new StringBuffer();
		if(bajaTemporaBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_VACACION)){
			descripcion.append(UtilidadesString.getMensajeIdioma(this.usrbean, CenBajasTemporalesBean.TIPO_DESC_VACACION));
			
		}else if(bajaTemporaBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_BAJA)){
			descripcion.append(UtilidadesString.getMensajeIdioma(this.usrbean, CenBajasTemporalesBean.TIPO_DESC_BAJA));
			
		}
		descripcion.append(" ");
		descripcion.append(bajaTemporaBean.getDescripcion());
		salto.setMotivos(descripcion.toString());
		salto.setUsuMod(this.usuModificacion);
		salto.setFechaMod("sysdate");
		
		Long idSaltosTurno = Long.valueOf(getNuevoIdSaltosTurno(salto.getIdInstitucion().toString(),salto.getIdTurno().toString()));
		salto.setIdSaltosTurno(idSaltosTurno);
		this.insert(salto);
		
		
	}
	public void insertarSaltoCompensacion(ScsSaltosCompensacionesBean salto) throws ClsExceptions{
		
		salto.setUsuMod(this.usuModificacion);
		salto.setFechaMod("sysdate");
		
		Long idSaltosTurno = Long.valueOf(getNuevoIdSaltosTurno(salto.getIdInstitucion().toString(),salto.getIdTurno().toString()));
		salto.setIdSaltosTurno(idSaltosTurno);
		this.insert(salto);
		
		
	}

	public void marcarSaltoCompensacionGuardia(ScsSaltosCompensacionesBean saltoCompensacion)
			throws ClsExceptions
	{
		try {
			String sql = 
				" UPDATE "+ScsSaltosCompensacionesBean.T_NOMBRETABLA+ " " +
				"    SET "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" = '"+saltoCompensacion.getFechaCumplimiento()+"', " +
				"        "+ScsSaltosCompensacionesBean.C_USUMODIFICACION+" = "+this.usrbean.getUserName()+"," +
				"        "+ScsSaltosCompensacionesBean.C_FECHAMODIFICACION+" = SYSDATE , " +
				"        "+ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS+" = "+saltoCompensacion.getIdCalendarioGuardias()+" ";
			if (saltoCompensacion.getMotivos() != null && !saltoCompensacion.getMotivos().equals(""))
				sql += " , " +
						""+ScsSaltosCompensacionesBean.C_MOTIVOS+" = "+ScsSaltosCompensacionesBean.C_MOTIVOS+" || '"+saltoCompensacion.getMotivos()+"' ";
			sql += 
				"  WHERE "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+" = "+saltoCompensacion.getIdInstitucion()+" " +
				"    AND "+ScsSaltosCompensacionesBean.C_IDTURNO+" = "+saltoCompensacion.getIdTurno()+" " +
				"    AND "+ScsSaltosCompensacionesBean.C_IDGUARDIA+" = "+saltoCompensacion.getIdGuardia()+" " +
				"    AND "+ScsSaltosCompensacionesBean.C_IDPERSONA+" = "+saltoCompensacion.getIdPersona()+" " +
				"    AND "+ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+" = '"+saltoCompensacion.getSaltoCompensacion()+"' " +
				"    AND "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" IS NULL " +
				"    AND rownum=1";
		
			this.updateSQL(sql);

		} catch (Exception e) {
			throw new ClsExceptions(e,
					"Excepcion en marcarSaltoCompensacionBBDD." + e.toString());
		}
	}
	
	private String getQuerySaltosCompensacionesActivos(String tipo,
			Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia)
	{
		StringBuffer sql = new StringBuffer();

		sql.append(" select * ");
		sql.append("   from scs_saltoscompensaciones s ");
		sql.append("  where s.idinstitucion = " + idInstitucion);
		sql.append("    and s.idturno = " + idTurno);
		sql.append("    and s.idguardia = " + idGuardia);
		sql.append("    and s.saltoocompensacion = '" + tipo + "' ");
		sql.append("    and s.fechacumplimiento is null ");
		sql.append("  order by s.fecha ");

		return sql.toString();
	}

	/**
	 * Obtiene todos los saltos asociados a una guardia
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public HashMap<Long, ArrayList<LetradoGuardia>> getSaltos(Integer idInstitucion, Integer idTurno, Integer idGuardia) throws ClsExceptions
	{
		// Controles
		CenPersonaAdm perAdm = new CenPersonaAdm(this.usrbean);

		// Variables
		HashMap<Long, ArrayList<LetradoGuardia>> hmPersonasConSaltos;
		ArrayList<LetradoGuardia> alLetradosSaltados;
		LetradoGuardia letradoSeleccionado;
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
				hmPersonasConSaltos = new HashMap<Long, ArrayList<LetradoGuardia>>();
				for (int i = 0; i < rc.size(); i++) {
					htFila = ((Row) rc.get(i)).getRow();
	
					idPersona = Long.valueOf((String) htFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA));
					letradoSeleccionado = new LetradoGuardia(perAdm.getPersonaPorId(idPersona.toString()), idInstitucion,
							idTurno, idGuardia, ClsConstants.SALTOS);
	
					if (hmPersonasConSaltos.containsKey(idPersona))
						alLetradosSaltados = hmPersonasConSaltos.get(idPersona);
					else
						alLetradosSaltados = new ArrayList<LetradoGuardia>();
	
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
	public ArrayList<LetradoGuardia> getCompensaciones(Integer idInstitucion, Integer idTurno, Integer idGuardia) throws ClsExceptions
	{
		// Controles
		ScsInscripcionGuardiaAdm inscripcionAdm = new ScsInscripcionGuardiaAdm(this.usrbean);
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(this.usrbean);

		// Variables
		ArrayList<LetradoGuardia> alLetradosCompensados;
		LetradoGuardia letradoSeleccionado;
		Hashtable<String, Object> htFila;
		String idPersona;
		ScsInscripcionGuardiaBean inscripcionGuardia;
		Map<String, CenBajasTemporalesBean> mBajasTemporales;
		
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
				alLetradosCompensados = new ArrayList<LetradoGuardia>();
				for (int i = 0; i < rc.size(); i++) {
					htFila = ((Row) rc.get(i)).getRow();
	
					idPersona = (String) htFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
					inscripcionGuardia = inscripcionAdm.getInscripcionActiva(idInstitucion.toString(), idTurno.toString(),
							idGuardia.toString(), idPersona, "sysdate");
					mBajasTemporales = btAdm.getDiasBajaTemporal(new Long(idPersona), idInstitucion);
					letradoSeleccionado = new LetradoGuardia(inscripcionGuardia, mBajasTemporales);
					letradoSeleccionado.setSaltoCompensacion("C");
					alLetradosCompensados.add(letradoSeleccionado);
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
	public Vector<LetradoGuardia> getLetradosSaltosCompensacionesTurno(String idInstitucion, String idTurno) throws ClsExceptions
	{
		// Controles
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(this.usrbean);
		ScsInscripcionTurnoAdm inscripcionAdm = new ScsInscripcionTurnoAdm(this.usrbean);

		// Variables
		Vector<LetradoGuardia> alLetradosCompensados;
		LetradoGuardia letradoSeleccionado;
		Hashtable<String, Object> htFila;
		String idPersona;
		ScsInscripcionTurnoBean inscripcionTurno;
		Map<String, CenBajasTemporalesBean> mBajasTemporales;

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

		try {
			RowsContainer rc = this.find(sBuffer.toString());
			if (rc == null) {
				alLetradosCompensados = null;
			} else {
				alLetradosCompensados = new Vector<LetradoGuardia>();
				for (int i = 0; i < rc.size(); i++) {
					htFila = ((Row) rc.get(i)).getRow();

					idPersona = (String) htFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
					inscripcionTurno = inscripcionAdm.getInscripcionActiva(idInstitucion.toString(),
							idTurno.toString(), idPersona, "sysdate");
					mBajasTemporales = btAdm.getDiasBajaTemporal(new Long(idPersona), new Integer(idInstitucion));
					letradoSeleccionado = new LetradoGuardia(inscripcionTurno, mBajasTemporales);
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
	
}