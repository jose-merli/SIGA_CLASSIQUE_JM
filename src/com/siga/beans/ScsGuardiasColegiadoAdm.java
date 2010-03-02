
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.PCAJGListadoTablasMaestrasForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;
import com.siga.gratuita.vos.VolantesExpressVo;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_GUARDIASCOLEGIADO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 * @version david.sanchezp 21/01/2005: para incluir los metodos: 
 * 		buscarColegiados(), deleteCalendario(), <br>
 * 		selectGenerico().
 * @version david.sanchezp 24/01/2005: para modificar todo los relacionado 
 * 		con el campo IDFACTURACION para <br>
 * 		sustituirlo por el campo RESERVA.
 * @version david.sanchezp 17/02/2005: para incluir nuevos metodos: 
 * 		getDatodColegiado(), buscarOtrosColegiados(),<br>
 * 		ejecutarPLGuardias(). Tambien incluyo las validaciones del calendario.
 * @version david.sanchezp 04/04/2006: para incluir modificaciones para 
 * 		el nuevo calendario de guardias colegiado.
 * @version adrian.ayala 14/05/2008:
 * 		sustitucion del campo C_TIPODIAS por 
 * 		C_GUARDIA_ SELECCIONLABORABLES y C_GUARDIA_SELECCIONFESTIVOS
 */

public class ScsGuardiasColegiadoAdm extends MasterBeanAdministrador
{
	//////////////////// CONSTRUCTOR ////////////////////
	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsGuardiasColegiadoAdm (UsrBean usuario) {
		super( ScsGuardiasColegiadoBean.T_NOMBRETABLA, usuario);
	}
	
	
	
	//////////////////// METODOS BASICOS DEL ADMINISTRADOR ////////////////////
	/**
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean ()
	{
		String[] campos =
		{
			ScsGuardiasColegiadoBean.C_DIASACOBRAR,
			ScsGuardiasColegiadoBean.C_DIASGUARDIA,
			ScsGuardiasColegiadoBean.C_FECHAFIN,
			ScsGuardiasColegiadoBean.C_FECHAINICIO,
			ScsGuardiasColegiadoBean.C_FECHAMODIFICACION,
			ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,
			ScsGuardiasColegiadoBean.C_RESERVA,
			ScsGuardiasColegiadoBean.C_IDGUARDIA,
			ScsGuardiasColegiadoBean.C_IDINSTITUCION,
			ScsGuardiasColegiadoBean.C_IDPERSONA,
			ScsGuardiasColegiadoBean.C_IDTURNO,
			ScsGuardiasColegiadoBean.C_OBSERVACIONES,
			ScsGuardiasColegiadoBean.C_USUMODIFICACION,
			ScsGuardiasColegiadoBean.C_FACTURADO,
			ScsGuardiasColegiadoBean.C_PAGADO,
			ScsGuardiasColegiadoBean.C_IDFACTURACION
		};
		return campos;
	} //getCamposBean ()
	
	/** 
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean ()
	{
		String[] campos =
		{
			ScsGuardiasColegiadoBean.C_IDINSTITUCION,
			ScsGuardiasColegiadoBean.C_IDTURNO,
			ScsGuardiasColegiadoBean.C_IDGUARDIA,
			ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,
			ScsGuardiasColegiadoBean.C_IDPERSONA,
			ScsGuardiasColegiadoBean.C_FECHAINICIO,
			ScsGuardiasColegiadoBean.C_FECHAFIN
		};
		return campos;
	} //getClavesBean ()
	
	/** 
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	public MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		ScsGuardiasColegiadoBean bean = null;
		try
		{
			bean = new ScsGuardiasColegiadoBean();
			bean.setDiasACobrar					(UtilidadesHash.getLong(hash, ScsGuardiasColegiadoBean.C_DIASACOBRAR));
			bean.setDiasGuardia					(UtilidadesHash.getLong(hash, ScsGuardiasColegiadoBean.C_DIASGUARDIA));
			bean.setFechaFin					(UtilidadesHash.getString (hash, ScsGuardiasColegiadoBean.C_FECHAFIN));
			bean.setFechaInicio					(UtilidadesHash.getString (hash, ScsGuardiasColegiadoBean.C_FECHAINICIO));
			bean.setFechaMod					(UtilidadesHash.getString (hash, ScsGuardiasColegiadoBean.C_FECHAMODIFICACION));
			bean.setIdCalendarioGuardias		(UtilidadesHash.getInteger(hash, ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS));
			bean.setReserva						(UtilidadesHash.getString (hash, ScsGuardiasColegiadoBean.C_RESERVA));
			bean.setIdGuardia					(UtilidadesHash.getInteger(hash, ScsGuardiasColegiadoBean.C_IDGUARDIA));
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, ScsGuardiasColegiadoBean.C_IDINSTITUCION));
			bean.setIdPersona					(UtilidadesHash.getLong(hash, ScsGuardiasColegiadoBean.C_IDPERSONA));
			bean.setIdTurno						(UtilidadesHash.getInteger(hash, ScsGuardiasColegiadoBean.C_IDTURNO));
			bean.setObservaciones				(UtilidadesHash.getString (hash, ScsGuardiasColegiadoBean.C_OBSERVACIONES));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, ScsGuardiasColegiadoBean.C_USUMODIFICACION));			
			bean.setFacturado					(UtilidadesHash.getString(hash, ScsGuardiasColegiadoBean.C_FACTURADO));
			bean.setPagado						(UtilidadesHash.getString(hash, ScsGuardiasColegiadoBean.C_PAGADO));
			bean.setIdFacturacion				(UtilidadesHash.getInteger(hash, ScsGuardiasColegiadoBean.C_IDFACTURACION));
			bean.setGuardia_seleccionLaborables	(UtilidadesHash.getString(hash, ScsGuardiasColegiadoBean.C_GUARDIA_SELECCIONLABORABLES));
			bean.setGuardia_seleccionFestivos	(UtilidadesHash.getString(hash, ScsGuardiasColegiadoBean.C_GUARDIA_SELECCIONFESTIVOS));
		}
		catch(Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean ()

	/** 
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	public Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) bean;
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_DIASACOBRAR, String.valueOf(b.getDiasACobrar()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_DIASGUARDIA, String.valueOf(b.getDiasGuardia()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_FECHAFIN, b.getFechaFin());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_FECHAINICIO, b.getFechaInicio());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS, String.valueOf(b.getIdCalendarioGuardias()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_RESERVA, b.getReserva());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));		
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_FACTURADO, b.getFacturado());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_PAGADO, b.getPagado());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_IDFACTURACION, String.valueOf(b.getIdFacturacion()));
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_GUARDIA_SELECCIONLABORABLES, b.getGuardia_seleccionLaborables());
			UtilidadesHash.set(hash, ScsGuardiasColegiadoBean.C_GUARDIA_SELECCIONFESTIVOS, b.getGuardia_seleccionFestivos());
		}
		catch (Exception e) {
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	} //beanToHashTable ()
	
	/** 
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	
	
	
	//////////////////// OTROS METODOS ////////////////////
	/**
	 * Ejecuta un delete por 4 campos ID: IDINSTITUCION,IDTURNO,IDGUARDIA,IDCALENDARIOGUARDIAS
	 * @param Hashtable hash: tabla hash copn los campos: 
	 * - String idinstitucion
	 * - String idcalendarioguardias
	 * - String idturno
	 * - String idguardia  
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deleteGuardiasCalendario(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			

			sql.append(" delete from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sql.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia);
			deleteSQL(sql.toString());		
//			ClsMngBBDD.executeUpdate(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}

	/**
	 * Ejecuta un delete por 4 campos ID: IDINSTITUCION,IDTURNO,IDGUARDIA,IDCALENDARIOGUARDIAS
	 * @param Hashtable hash: tabla hash copn los campos: 
	 * - String idinstitucion
	 * - String idcalendarioguardias
	 * - String idturno
	 * - String idguardia
	 * - String idPersona    
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deleteGuardiaCalendario(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);
			idPersona = (String)hash.get("IDPERSONA");
			//Fechas del periodo:
			fechaInicio = (String)hash.get(ScsCalendarioGuardiasBean.C_FECHAINICIO);
			fechaFin = (String)hash.get(ScsCalendarioGuardiasBean.C_FECHAFIN);

			sql.append(" delete from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sql.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idPersona);
			sql.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+")=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
					
			ClsMngBBDD.executeUpdate(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}	
	
	/** 
	 * Devuelve los registros con los colegiado inscritos en un calendario excepto el registro de un colegiado del que tenemos su idpersona.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_GUARDIASCOLEGIADO
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarOtrosColegiados(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="", idpersona="";
		
		try {
			idinstitucion = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			idguardia = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA);
			idturno = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
			idcalendarioguardias = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);
			reserva = (String)miHash.get(ScsGuardiasColegiadoBean.C_RESERVA);
			idpersona = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDPERSONA);

			consulta = "SELECT ";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_FECHAINICIO+",";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_FECHAFIN+",";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+",";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDTURNO+",";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDGUARDIA+",";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDPERSONA+",";			
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,";
			consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO+",";
			//FUNCION DE PERMUTAS: calculo su valor 
			consulta += " F_SIGA_NUMEROPERMUTAGUARDIAS(";
			consulta +=       " guard."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+", guard."+ScsGuardiasColegiadoBean.C_IDTURNO+", guard."+ScsGuardiasColegiadoBean.C_IDGUARDIA+",";
			consulta += 	  " guard."+ScsGuardiasColegiadoBean.C_IDPERSONA+", guard."+ScsGuardiasColegiadoBean.C_FECHAINICIO;
			consulta+= 		  ") AS FUNCIONPERMUTAS";
			consulta += " FROM "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";			
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_RESERVA+"='"+reserva+"'";
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDPERSONA+"<>'"+idpersona+"'";			
			//JOIN
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsGuardiasColegiadoBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsGuardiasColegiadoBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsGuardiasColegiadoBean.C_IDINSTITUCION;
			//ORDEN
			consulta += " ORDER BY guard."+ScsGuardiasColegiadoBean.C_FECHAINICIO;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.buscarOtrosColegiados(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	/** 
	 * Devuelve los registros con los datos del colegiado de guardia o de reserva.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_GUARDIASCOLEGIADO
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarColegiadosOld(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="";
		
		try {
			idinstitucion = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			idguardia = (miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA)==null || miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA).equals(""))?"guard.IDGUARDIA":(String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA);
			idturno = (miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO)==null || miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO).equals(""))?"guard.IDTURNO":(String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
			idcalendarioguardias = (miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS)==null || miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS).equals(""))?"guard.IDCALENDARIOGUARDIAS":(String)miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);

			/** PDM Modificacion de la consulta para que se muestren los resultados ordenados por fecha de inicio
			 *  INC_02348
			 */
			consulta = " SELECT guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_VALIDADO;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDTURNO;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta+= " ,F_SIGA_TIENE_ACTS_VALIDADAS(guard.IDINSTITUCION,guard.IDTURNO,guard.IDGUARDIA,guard.IDCALENDARIOGUARDIAS,guard.IDPERSONA,guard.FECHAINICIO) AS ACT_VALIDADAS";
			consulta+= " ,(SELECT T.NOMBRE FROM SCS_TURNO T WHERE T.IDINSTITUCION=guard.IDINSTITUCION AND  T.IDTURNO=guard.IDTURNO) AS NOMTURNO";
			consulta+= " ,(SELECT G.NOMBRE FROM SCS_GUARDIASTURNO G WHERE G.IDINSTITUCION=guard.IDINSTITUCION AND  G.IDTURNO=guard.IDTURNO AND  G.IDGUARDIA=guard.IDGUARDIA) AS NOMGUARDIA";
			consulta+= " ,decode(F_SIGA_FECHAINISOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',"+
                       "  decode(F_SIGA_FECHAINICONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+",F_SIGA_FECHAINICONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")),F_SIGA_FECHAINISOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")) FECHAINICIOPERMUTA"+
                       " ,decode(F_SIGA_FECHAFINSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',"+
                       "  decode(F_SIGA_FECHAFINCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",F_SIGA_FECHAFINCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")),F_SIGA_FECHAFINSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")) FECHAFINPERMUTA"+
                       " ,decode(F_SIGA_FECHAORIGSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',"+
                       "  decode(F_SIGA_FECHAORIGCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+",F_SIGA_FECHAORIGCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")),F_SIGA_FECHAORIGSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")) FECHAINICIO"+
                       " ,guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" as FECHA_INICIO_PK,guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN;    
			consulta += " ," + " perso." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || perso." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || perso." + CenPersonaBean.C_NOMBRE + " NOMBRE";
			consulta += " ,F_SIGA_CALCULONCOLEGIADO(coleg."+CenColegiadoBean.C_IDINSTITUCION+","+"coleg."+CenColegiadoBean.C_IDPERSONA+") as "+CenColegiadoBean.C_NCOLEGIADO;			
			consulta += " ,F_SIGA_ES_MODIFICABLE_GUARDIAS(guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+",guard."+ScsCabeceraGuardiasBean.C_IDTURNO+",guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+",guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+") as ESMODIFICABLE";			
			consulta += " ,guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS;
			consulta += " ,F_SIGA_NUMERO("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+") NUMEROPERMUTA";
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			// JOIN
			consulta += " WHERE coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION)!=null) 
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO)!=null   && !((String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO)).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA)!=null  && !((String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA)).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA);
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS)!=null)
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);

			if (miHash.get("IDPERSONA")!=null && !((String)miHash.get("IDPERSONA")).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+(String)miHash.get("IDPERSONA");
			if (miHash.get("NUMCOLEGIADO")!=null && !((String)miHash.get("NUMCOLEGIADO")).trim().equals(""))
			    consulta += " AND F_SIGA_CALCULONCOLEGIADO(coleg."+CenColegiadoBean.C_IDINSTITUCION+","+"coleg."+CenColegiadoBean.C_IDPERSONA+")='"+(String)miHash.get("NUMCOLEGIADO")+"'";
			
			boolean BuscarFechaDesde = (miHash.get("BUSCARFECHADESDE")!=null && 
					!((String)miHash.get("BUSCARFECHADESDE")).trim().equals(""));
			boolean BuscarFechaHasta = (miHash.get("BUSCARFECHAHASTA")!=null && 
					!((String)miHash.get("BUSCARFECHAHASTA")).trim().equals(""));
			if (BuscarFechaDesde) {
			    consulta += " AND (guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+">='"+(String)miHash.get("BUSCARFECHADESDE")+"'";
			    consulta += " OR guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+">='"+(String)miHash.get("BUSCARFECHADESDE")+"')";
			}
			if (BuscarFechaHasta) {
			    consulta += " AND (guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"<='"+(String)miHash.get("BUSCARFECHAHASTA")+"'";
			    consulta += " OR guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"<='"+(String)miHash.get("BUSCARFECHAHASTA")+"')";
			}
		
			if ((miHash.get("FECHA_INICIO")!=null )&& (miHash.get("FECHA_FIN")==null)){
				 consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+">='"+(String)miHash.get("FECHA_INICIO")+"'";
			}
			if (miHash.get("FECHA_INICIO")==null && (miHash.get("FECHA_FIN")!=null)){
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"<='"+(String)miHash.get("FECHA_FIN")+"'";
			}
			if (((miHash.get("FECHA_INICIO")!=null)) && 
				((miHash.get("FECHA_FIN")!=null))){
				 consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+">='"+(String)miHash.get("FECHA_INICIO")+"'";
				 consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"<='"+(String)miHash.get("FECHA_FIN")+"'";
			}
			if (miHash.get("PENDIENTEVALIDAR")!=null && !((String)miHash.get("PENDIENTEVALIDAR")).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_VALIDADO+"='0'";

			//ORDEN
			consulta += " ORDER BY FECHAINICIOPERMUTA , guard.rowid"; 
			// RGG esta tonteria del rowid es muy importante para el orden dentro de una misma guardia
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.buscarColegiados(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	public String buscarColegiados(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="";
		
		try {
			idinstitucion = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			idguardia = (miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA)==null || miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA).equals(""))?"guard.IDGUARDIA":(String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA);
			idturno = (miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO)==null || miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO).equals(""))?"guard.IDTURNO":(String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
			idcalendarioguardias = (miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS)==null || miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS).equals(""))?"guard.IDCALENDARIOGUARDIAS":(String)miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);

			/** PDM Modificacion de la consulta para que se muestren los resultados ordenados por fecha de inicio
			 *  INC_02348
			 */
			consulta = " SELECT guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta+= " ,guard.rowid AS ROWIND";
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_VALIDADO;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDTURNO;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//consulta+= " ,F_SIGA_TIENE_ACTS_VALIDADAS(guard.IDINSTITUCION,guard.IDTURNO,guard.IDGUARDIA,guard.IDCALENDARIOGUARDIAS,guard.IDPERSONA,guard.FECHAINICIO) AS ACT_VALIDADAS";
			consulta+= " ,(SELECT T.NOMBRE FROM SCS_TURNO T WHERE T.IDINSTITUCION=guard.IDINSTITUCION AND  T.IDTURNO=guard.IDTURNO) AS NOMTURNO";
			consulta+= " ,(SELECT G.NOMBRE FROM SCS_GUARDIASTURNO G WHERE G.IDINSTITUCION=guard.IDINSTITUCION AND  G.IDTURNO=guard.IDTURNO AND  G.IDGUARDIA=guard.IDGUARDIA) AS NOMGUARDIA";
			//consulta+= " ,decode(F_SIGA_FECHAINISOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',";
			//consulta+= "  decode(F_SIGA_FECHAINICONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+",F_SIGA_FECHAINICONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")),F_SIGA_FECHAINISOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")) FECHAINICIOPERMUTA";
			//consulta+= " ,decode(F_SIGA_FECHAFINSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',";
			//consulta+= "  decode(F_SIGA_FECHAFINCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",F_SIGA_FECHAFINCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")),F_SIGA_FECHAFINSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")) FECHAFINPERMUTA";
			//consulta+= " ,decode(F_SIGA_FECHAORIGSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',";
			//consulta+= "  decode(F_SIGA_FECHAORIGCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+"),'',guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+",F_SIGA_FECHAORIGCONFIRMADOR("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")),F_SIGA_FECHAORIGSOLICITANTE("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" ,"+idcalendarioguardias+")) FECHAINICIO";
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" as FECHA_INICIO_PK,guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN;    
			consulta += " ," + " perso." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || perso." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || perso." + CenPersonaBean.C_NOMBRE + " NOMBRE";
			//consulta += " ,F_SIGA_CALCULONCOLEGIADO(coleg."+CenColegiadoBean.C_IDINSTITUCION+","+"coleg."+CenColegiadoBean.C_IDPERSONA+") as "+CenColegiadoBean.C_NCOLEGIADO;			
			//consulta += " ,F_SIGA_ES_MODIFICABLE_GUARDIAS(guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+",guard."+ScsCabeceraGuardiasBean.C_IDTURNO+",guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+",guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+") as ESMODIFICABLE";			
			consulta += " ,guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS;
			//consulta += " ,F_SIGA_NUMERO("+idinstitucion+","+idturno+","+idguardia+",guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+idcalendarioguardias+")   ERMUTA";
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			// JOIN
			consulta += " WHERE coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=coleg."+CenColegiadoBean.C_IDPERSONA;
			   
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION)!=null) 
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO)!=null   && !((String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO)).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA)!=null  && !((String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA)).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA);
			if (miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS)!=null)
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+(String)miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);

			if (miHash.get("IDPERSONA")!=null && !((String)miHash.get("IDPERSONA")).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+(String)miHash.get("IDPERSONA");
			if (miHash.get("NUMCOLEGIADO")!=null && !((String)miHash.get("NUMCOLEGIADO")).trim().equals(""))
			    consulta += " AND F_SIGA_CALCULONCOLEGIADO(coleg."+CenColegiadoBean.C_IDINSTITUCION+","+"coleg."+CenColegiadoBean.C_IDPERSONA+")='"+(String)miHash.get("NUMCOLEGIADO")+"'";
			
			boolean BuscarFechaDesde = (miHash.get("BUSCARFECHADESDE")!=null && 
					!((String)miHash.get("BUSCARFECHADESDE")).trim().equals(""));
			boolean BuscarFechaHasta = (miHash.get("BUSCARFECHAHASTA")!=null && 
					!((String)miHash.get("BUSCARFECHAHASTA")).trim().equals(""));
			if (BuscarFechaDesde) {
			    consulta += " AND (guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+">='"+(String)miHash.get("BUSCARFECHADESDE")+"'";
			    consulta += " OR guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+">='"+(String)miHash.get("BUSCARFECHADESDE")+"')";
			}
			if (BuscarFechaHasta) {
			    consulta += " AND (guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"<='"+(String)miHash.get("BUSCARFECHAHASTA")+"'";
			    consulta += " OR guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"<='"+(String)miHash.get("BUSCARFECHAHASTA")+"')";
			}
		
			if ((miHash.get("FECHA_INICIO")!=null )&& (miHash.get("FECHA_FIN")==null)){
				 consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+">='"+(String)miHash.get("FECHA_INICIO")+"'";
			}
			if (miHash.get("FECHA_INICIO")==null && (miHash.get("FECHA_FIN")!=null)){
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"<='"+(String)miHash.get("FECHA_FIN")+"'";
			}
			if (((miHash.get("FECHA_INICIO")!=null)) && 
				((miHash.get("FECHA_FIN")!=null))){
				 consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+">='"+(String)miHash.get("FECHA_INICIO")+"'";
				 consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+"<='"+(String)miHash.get("FECHA_FIN")+"'";
			}
			if (miHash.get("PENDIENTEVALIDAR")!=null && !((String)miHash.get("PENDIENTEVALIDAR")).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_VALIDADO+"='0'";

			//ORDEN
			consulta += " ORDER BY FECHA_INICIO_PK, nombre"; 
			// RGG esta tonteria del rowid es muy importante para el orden dentro de una misma guardia
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.buscarColegiados(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}

	


	/** 
	 * Devuelve un registro con los datos del colegiado de guardia o de reserva.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_GUARDIASCOLEGIADO
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getDatosColegiado(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="", idpersona="";
		
		try {
			idinstitucion = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
			idguardia = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDGUARDIA);
			idturno = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDTURNO);
			idcalendarioguardias = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS);
			reserva = (String)miHash.get(ScsGuardiasColegiadoBean.C_RESERVA);
			idpersona = (String)miHash.get(ScsGuardiasColegiadoBean.C_IDPERSONA);

			consulta = "SELECT guard.* ,";
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,";
			consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO;
			consulta += " FROM "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idpersona;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_RESERVA+"='"+reserva+"'";
			//JOIN
			consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsGuardiasColegiadoBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsGuardiasColegiadoBean.C_IDINSTITUCION;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsGuardiasColegiadoBean.C_IDPERSONA;
			//ORDEN
			consulta += " ORDER BY guard."+ScsGuardiasColegiadoBean.C_FECHAINICIO;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.getDatosColegiado(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	/** 
	 * Validacion: Devuelve el numero de letrados para una guardia de un turno y un dia concreto.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_GUARDIASCOLEGIADO
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String numeroLetrados(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", fechaInicio="", fechaFin="";
		
		try {
			idinstitucion = (String)miHash.get("IDINSTITUCION");
			idguardia = (String)miHash.get("IDGUARDIA");
			idturno = (String)miHash.get("IDTURNO");
			fechaInicio = (String)miHash.get("FECHAINICIO");
			fechaFin = (String)miHash.get("FECHAFIN");
			idcalendarioguardias = (String)miHash.get("IDCALENDARIOGUARDIAS");
				
			consulta = "SELECT COUNT(distinct guard."+ScsGuardiasColegiadoBean.C_IDPERSONA+") AS TOTAL";
			consulta += " FROM "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" guard";
			consulta += " WHERE ";
			consulta += " guard."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_FECHAINICIO+"=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_FECHAFIN+">=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " AND guard."+ScsGuardiasColegiadoBean.C_FECHAFIN+"<=TO_DATE('"+fechaFin+"','DD/MM/YYYY')";
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.buscarColegiados(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	

	/** 
	 * Validacion: Consulta para buscar los dias de separacion entre guardias y validarlos
	 * Devuelve 0 si no encuentra separaciones incorrectas
	 * 
	 * La Hash debe tener los siguientes datos:
	 * -String "IDPERSONA"
	 * -String "IDINSTITUCION"
	 * -String "IDCALENDARIOGUARDIAS"
	 * -String "IDTURNO"
	 * -String "IDGUARDIA"
	 * -String "FECHAINICIO" del periodo de esta guardia
	 * -String "FECHAFIN" del periodo de esta guardia
	 * Parametros opcionales (claves opcionales):
	 * -String "FECHAINICIO_ORIGINAL" del periodo original para esta guardia. Puede no venir en la hash (solo se rellena en las permutas)
	 * -String "FECHAFIN_ORIGINAL" del periodo original para esta guardia. Puede no venir en la hash (solo se rellena en las permutas)
	 * 
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String validacionSeparacionGuardias(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idpersona="", idcalendario="";
		String fechaPeriodoUltimoDia="", fechaPeriodoPrimerDia="", fechaPeriodoUltimoDiaOriginal="", fechaPeriodoPrimerDiaOriginal="";
		StringBuffer sBuffer; 
		Vector vResultados; 
		String fechaMIN="", fechaMAX="";

		try {
			idinstitucion = (String)miHash.get("IDINSTITUCION");
			idguardia = (String)miHash.get("IDGUARDIA");
			idturno = (String)miHash.get("IDTURNO");
			idpersona = (String)miHash.get("IDPERSONA");
			fechaPeriodoPrimerDia = (String)miHash.get("FECHAINICIO"); //Fecha del periodo de la primera guardia
			fechaPeriodoUltimoDia = (String)miHash.get("FECHAFIN"); //Fecha del periodo	de la ultima guardia
			fechaPeriodoPrimerDiaOriginal = (String)miHash.get("FECHAINICIO_ORIGINAL"); //Fecha del periodo de la primera guardia
			fechaPeriodoUltimoDiaOriginal = (String)miHash.get("FECHAFIN_ORIGINAL"); //Fecha del periodo	de la ultima guardia	
			idcalendario = (String)miHash.get("IDCALENDARIOGUARDIAS");
			
			//Si tenemos fechas originales (venimos de permutas) es true.
			boolean esPermuta = miHash.containsKey("FECHAFIN_ORIGINAL") && miHash.containsKey("FECHAINICIO_ORIGINAL");
			
			//Consulto la maxima fecha inicio para el periodo en la cabecera de guardias:
			sBuffer = new StringBuffer();
			sBuffer.append("SELECT MAX(trunc("+ScsGuardiasColegiadoBean.C_FECHAFIN+")) AS MAXIMA");
			sBuffer.append(" FROM ");
			sBuffer.append(ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sBuffer.append(" WHERE ");
			sBuffer.append(ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idpersona);
			sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
			sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno);
			sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia);
			//sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendario);
			if (esPermuta)
				sBuffer.append(" AND trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+") <> TO_DATE('"+fechaPeriodoPrimerDiaOriginal+"','DD/MM/YYYY')");
			sBuffer.append(" AND trunc("+ScsGuardiasColegiadoBean.C_FECHAFIN+") <= TO_DATE('"+fechaPeriodoPrimerDia+"','DD/MM/YYYY')");
			vResultados = new Vector();
			vResultados = this.selectGenerico(sBuffer.toString());
			if (!vResultados.isEmpty()) {
				fechaMAX = ((String) ((Hashtable)vResultados.firstElement()).get("MAXIMA")).trim();
				fechaMAX = GstDate.getFormatedDateShort("ES",fechaMAX);
			}
			
			//Consulto la minima fecha inicio para el periodo en la cabecera de guardias:
			sBuffer = new StringBuffer();
			sBuffer.append("SELECT MIN(trunc("+ScsGuardiasColegiadoBean.C_FECHAFIN+")) AS MINIMA");
			sBuffer.append(" FROM ");
			sBuffer.append(ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sBuffer.append(" WHERE ");
			sBuffer.append(ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idpersona);
			sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
			sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno);
			sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia);
			//sBuffer.append(" AND "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendario);			
			if (esPermuta)
				sBuffer.append(" AND trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+") <> TO_DATE('"+fechaPeriodoPrimerDiaOriginal+"','DD/MM/YYYY')");
			sBuffer.append(" AND trunc("+ScsGuardiasColegiadoBean.C_FECHAFIN+") >= TO_DATE('"+fechaPeriodoUltimoDia+"','DD/MM/YYYY')");
			vResultados.clear();
			vResultados = this.selectGenerico(sBuffer.toString());
			if (!vResultados.isEmpty()) {
				fechaMIN = ((String) ((Hashtable)vResultados.firstElement()).get("MINIMA")).trim();
				fechaMIN = GstDate.getFormatedDateShort("ES",fechaMIN);
			}
			
			//Consulta para buscar los dias de separacion entre guardias y validarlos:
			//Devuelve 0 si no encuentra separaciones incorrectas
			consulta = "SELECT COUNT(coleg."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+") AS TOTAL";
			consulta += " FROM ";
			consulta += ScsGuardiasColegiadoBean.T_NOMBRETABLA+" coleg ,";
			consulta += ScsGuardiasTurnoBean.T_NOMBRETABLA+ " guard "; 
			consulta += " WHERE ";
			consulta += " coleg."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"=guard."+ScsGuardiasTurnoBean.C_IDINSTITUCION;
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_IDTURNO+"=guard."+ScsGuardiasTurnoBean.C_IDTURNO;
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"=guard."+ScsGuardiasTurnoBean.C_IDGUARDIA;			
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idpersona;
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno;
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND coleg."+ScsGuardiasColegiadoBean.C_RESERVA+"='N'";
			if (!fechaMIN.equals("") && !fechaMAX.equals("")) {
				consulta += " AND ( ";
				consulta += "       (abs(TO_DATE('"+fechaMAX+"','DD/MM/YYYY') - TO_DATE('"+fechaPeriodoPrimerDia+"','DD/MM/YYYY')) <= guard."+ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS+")";
				consulta += "       OR ";
				consulta += "       (abs(TO_DATE('"+fechaMIN+"','DD/MM/YYYY') - TO_DATE('"+fechaPeriodoUltimoDia+"','DD/MM/YYYY')) <= guard."+ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS+")";
				consulta += " ) ";
			} else {
				if (fechaMIN.equals("") && !fechaMAX.equals(""))
					consulta += " AND (abs(TO_DATE('"+fechaMAX+"','DD/MM/YYYY') - TO_DATE('"+fechaPeriodoPrimerDia+"','DD/MM/YYYY')) <= guard."+ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS+")";
				else if (!fechaMIN.equals("") && fechaMAX.equals(""))
						consulta += " AND (abs(TO_DATE('"+fechaMIN+"','DD/MM/YYYY') - TO_DATE('"+fechaPeriodoUltimoDia+"','DD/MM/YYYY')) <= guard."+ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS+")";
			}
		} catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.validacionIncompatibilidad(). Consulta SQL:"+consulta);
		}
		return consulta;
	}	

	
	//
	// VALIDACIONES (1-5) ANTES DE INSERTAR UN REGISTRO DE UN COLEGIADO EN EL CALENDARIO
	//

	/** 
	 * Valida si fecha1 es posterior o igual que fecha2
	 * 
	 * @param String fecha1: fecha a validar
	 * @param String fecha1: fecha fin
	 * @return boolean: true si la fecha fin es correcta.
	 */	
	public boolean validarFechaFin(String fecha1, String fecha2) {
		boolean salida = false;
		//Recupero del String fechaInicial con formato dd/mm/yyyy la fecha como Date
		String jsdf = "dd/MM/yyyy";//Java Short Date Format
		SimpleDateFormat formateo = new SimpleDateFormat(jsdf);
		Date date1 = new Date();
		Date date2 = new Date();

		try {
			date1 = formateo.parse(fecha1);
			date2 = formateo.parse(fecha2);
			salida = date1.equals(date2) || date1.after(date2);
		}
		catch (Exception e) {
		}
		return salida;
	}
	
	/** 
	 * Valida el Numero de letrados: si supera el contador de letrados el permitido no puedo insertar.
	 * 
	 * La Hash debe tener los siguientes datos:
	 * -String "IDPERSONA"
	 * -String "IDINSTITUCION"
	 * -String "IDCALENDARIOGUARDIAS"
	 * -String "IDTURNO"
	 * -String "IDGUARDIA"
	 * -String "FECHAINICIO" del periodo de esta guardia
	 * -String "FECHAFIN" del periodo de esta guardia
	 * @return boolean: true si el número de letrados es correcto
	 * @throws ClsExceptions
	 */	
	public boolean validarNumeroLetrados(Hashtable miHash) throws ClsExceptions { 
		Vector registros = new Vector();
		String maxLetradosGuardia = "";
		int letradosInsertados = 0;
		boolean salida = false;
		StringBuffer where = new StringBuffer();		
		Hashtable hash = new Hashtable();
		
		try {
			//Consulto el numero de letrados maximo:
			where.append(" select * from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" where ");
			where.append(ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+(String)miHash.get("IDINSTITUCION"));
			where.append(" AND "+ScsGuardiasTurnoBean.C_IDTURNO+"="+(String)miHash.get("IDTURNO"));
			where.append(" AND "+ScsGuardiasTurnoBean.C_IDGUARDIA+"="+(String)miHash.get("IDGUARDIA"));
			registros = this.selectGenerico(where.toString());
			
			if (!registros.isEmpty()) {
				hash = (Hashtable)registros.get(0);
				maxLetradosGuardia = (String)hash.get(ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA);
				
				//Consulto el numero de letrados insertados:
				registros.clear();
				registros = this.selectGenerico(this.numeroLetrados(miHash));
				if (!registros.isEmpty()) {					
					letradosInsertados = Integer.parseInt((String)((Hashtable)registros.get(0)).get("TOTAL"));
					if (letradosInsertados < Integer.parseInt(maxLetradosGuardia)) 
						salida = true;
					else
						salida = false;
				}
			} else
				salida = false;
		} catch (ClsExceptions e) {
	        throw e;
		}
		return salida;
	}
	
	/**
	 * Valida que la separacion de dias para una guardia de un calendario es correcta.
	 * La Hash debe tener los siguientes datos:
	 * -String "IDPERSONA"
	 * -String "IDINSTITUCION"
	 * -String "IDCALENDARIOGUARDIAS"
	 * -String "IDTURNO"
	 * -String "IDGUARDIA"
	 * -String "FECHAINICIO" del periodo de esta guardia
	 * -String "FECHAFIN" del periodo de esta guardia
	 * -String "FECHAINICIO_NUEVA" del nuevo periodo para esta guardia
	 * -String "FECHAFIN_NUEVA" del nuevo periodo para esta guardia
	 *
	 * @param miHash
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean validarSeparacionGuardias(Hashtable miHash) throws ClsExceptions {
		Vector registros = new Vector();
		int numeroSepacionesIncorrectas = 0;
		boolean salida = false;

		try {
			registros = this.selectGenerico(this.validacionSeparacionGuardias(miHash));

			if (registros!=null && !registros.isEmpty()) {
				numeroSepacionesIncorrectas = Integer.parseInt((String)((Hashtable)registros.get(0)).get("TOTAL"));
				if (numeroSepacionesIncorrectas == 0) 
					salida = true;
				else
					salida = false;			
			}
		} catch (ClsExceptions e) {
	        throw e;
		}
		return salida;
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		return selectGenerico(consulta, false);
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @param boolean bRW: true si usamos el pool de Lectura/Escritura.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta, boolean bRW) throws ClsExceptions {
		Vector datos = new Vector();
		boolean salida = false;
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			
			//Si uso el pool de lectura/escritura
			if (bRW)
				salida = rc.findForUpdate(consulta);
			else
				salida = rc.query(consulta);
			if (salida) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsGuardiasColegiadoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		

	/**
	 * Ejecuta el PL para obtener un contador con el que consultar las guardias de forma ordenada 
	 * @param String idinstitucion
	 * @param String idturno
	 * @param String idguardia
	 * @return String: contador con el número necesario para la búsqueda sobre la tabla temporal con el que consultar las guardias de forma ordenada.
	 * @throws ClsExceptions
	 */
	public String ejecutarPLGuardias(String idinstitucion, String idturno, String idguardia) {
		return EjecucionPLs.ejecutarPL_OrdenaColegiadosGuardia(
				Integer.valueOf(idinstitucion), 
				Integer.valueOf(idturno), 
				Integer.valueOf(idguardia), 
				new Integer(0))[0];
	}

	//Comprueba antes de borrar un CALENDARIO de guardias que no exista ninguna guardia realizada. 
	public boolean validarBorradoGuardias(String idInstitucion, String idCalendarioGuardias, String idTurno, String idGuardia) {
		boolean correcto = false;
		StringBuffer consulta = new StringBuffer();
		
		consulta.append("select count(*) AS TOTAL from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
		consulta.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idInstitucion);
		consulta.append(" and "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idTurno);
		consulta.append(" and "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idGuardia);
		consulta.append(" and "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idCalendarioGuardias);
		consulta.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAFIN+") < trunc(sysdate)");

		Vector vLetrados = new Vector();
		int totalLetrados = 0;
		try {
			vLetrados = this.selectGenerico(consulta.toString());
			if (!vLetrados.isEmpty()) {
				totalLetrados = Integer.parseInt((String)((Hashtable)vLetrados.get(0)).get("TOTAL"));
				if (totalLetrados == 0)
					correcto = true;
			}
		} catch (Exception e) {
			correcto = false;
		}
		return correcto;
	}

	//Comprueba antes de borrar UNA guardia no este realizada.
	//Nota: las fechas de inicio y fin son del periodo.
	public boolean validarBorradoGuardia(String idInstitucion, String idCalendarioGuardias, String idTurno, String idGuardia, String fechaInicio, String fechaFin) {
		boolean correcto = false;
		StringBuffer consulta = new StringBuffer();
		
		consulta.append("select count(*) AS TOTAL from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
		consulta.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idInstitucion);
		consulta.append(" and "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idTurno);
		consulta.append(" and "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idGuardia);
		consulta.append(" and "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idCalendarioGuardias);
		consulta.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+")=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
		consulta.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAFIN+") < trunc(sysdate)");

		Vector vLetrados = new Vector();
		int totalLetrados = 0;
		try {
			vLetrados = this.selectGenerico(consulta.toString());
			if (!vLetrados.isEmpty()) {
				totalLetrados = Integer.parseInt((String)((Hashtable)vLetrados.get(0)).get("TOTAL"));
				if (totalLetrados == 0)
					correcto = true;
			}
		} catch (Exception e) {
			correcto = false;
		}
		return correcto;
	}
	
	//Comprueba si hay incompatibilidades de guardia en el calendario
	public boolean validarIncompatibilidadGuardia(String idInstitucion, String idTurno, String idGuardia, ArrayList arrayDiasGuardia, String idPersona) {
		StringBuffer consulta = new StringBuffer();
		
		consulta.append (" select gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+" ");
/*		consulta.append ("        + g_incompatibles."+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+" ");
		consulta.append ("        as FECHAFIN ");
*/		consulta.append ("   from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA+" gc, ");
		consulta.append ("        (select "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+", ");
		consulta.append ("                "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+", ");
		consulta.append ("                "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+", ");
		consulta.append ("                "+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+" ");
		consulta.append ("           from "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" ");
		consulta.append ("          where "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idInstitucion);
		consulta.append ("            and "+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = "+idTurno);
		consulta.append ("            and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = "+idGuardia);
		consulta.append ("         UNION ");
		consulta.append ("         select "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+", ");
		consulta.append ("                "+ScsIncompatibilidadGuardiasBean.C_IDTURNO+", ");
		consulta.append ("                "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+", ");
		consulta.append ("                "+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+" ");
		consulta.append ("           from "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" ");
		consulta.append ("          where "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idInstitucion);
		consulta.append ("            and "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = "+idTurno);
		consulta.append ("            and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = "+idGuardia);
		consulta.append ("        ) g_incompatibles ");
		consulta.append ("  where gc."+ScsGuardiasColegiadoBean.C_IDINSTITUCION+" = g_incompatibles."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" ");
		consulta.append ("    and gc."+ScsGuardiasColegiadoBean.C_IDTURNO+" = g_incompatibles."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" ");
		consulta.append ("    and gc."+ScsGuardiasColegiadoBean.C_IDGUARDIA+" = g_incompatibles."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" ");
		consulta.append ("    and gc."+ScsGuardiasColegiadoBean.C_IDPERSONA+" = "+idPersona);
		
		Vector vFechaFin = new Vector();
		boolean encontrado = false;
		
		try {
			Iterator iterDiasGuardia = arrayDiasGuardia.iterator ();
			String fechaGuardia;
			while (iterDiasGuardia.hasNext () && !encontrado) {
				fechaGuardia = (String) iterDiasGuardia.next ();
				consulta.append (" and abs (gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+" - to_date('"+fechaGuardia+"', 'DD/MM/YYYY')) ");
				consulta.append (" <= g_incompatibles."+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+" ");
				
				vFechaFin = this.selectGenerico (consulta.toString ());
				if (! vFechaFin.isEmpty ())
					encontrado = true;
			}
		} catch (Exception e) {
			encontrado = true;
		}
		return !encontrado;
	}
	public void insertarCabeceraYGuardia(Integer idInstitucion,	Integer idTurno,Integer idGuardia
			,Integer idCalendarioGuardia,Long idPersona,String fecha,UsrBean usrBean) throws SIGAException,ClsExceptions{
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usrBean);
		ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(usrBean);
		ScsCabeceraGuardiasBean cabeceraGuardiaBean = new ScsCabeceraGuardiasBean();
		ScsGuardiasColegiadoBean guardiaColegiadoBean = new ScsGuardiasColegiadoBean();
		
		cabeceraGuardiaBean.setIdInstitucion(idInstitucion);
		cabeceraGuardiaBean.setIdTurno(idTurno);
		cabeceraGuardiaBean.setIdGuardia(idGuardia);
		cabeceraGuardiaBean.setIdCalendario(idCalendarioGuardia);
		cabeceraGuardiaBean.setIdPersona(idPersona);
		
		cabeceraGuardiaBean.setFechaInicio(fecha);
		cabeceraGuardiaBean.setFechaFin(fecha);
		cabeceraGuardiaBean.setSustituto("N");
		cabeceraGuardiaBean.setFacturado("0");
		cabeceraGuardiaBean.setPagado("0");
		cabeceraGuardiaBean.setValidado("0");
		
		guardiaColegiadoBean.setIdInstitucion(idInstitucion);
		guardiaColegiadoBean.setIdTurno(idTurno);
		guardiaColegiadoBean.setIdGuardia(idGuardia);
		guardiaColegiadoBean.setIdCalendarioGuardias(idCalendarioGuardia);
		guardiaColegiadoBean.setIdPersona(idPersona);
		guardiaColegiadoBean.setFechaInicio(fecha);
		guardiaColegiadoBean.setFechaFin(fecha);
		guardiaColegiadoBean.setDiasGuardia(new Long(1));
		guardiaColegiadoBean.setDiasACobrar(new Long(1));
		guardiaColegiadoBean.setReserva("N");
		guardiaColegiadoBean.setFacturado("0");
		guardiaColegiadoBean.setPagado("0");
		
		
		try {
			cabeceraGuardiasAdm.insert(cabeceraGuardiaBean);
			guardiasColegiadoAdm.insert(guardiaColegiadoBean);
			
		} catch (Exception e) {
			
			throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
		}
		
		
		
	}
	
	public void sustitucionLetradoGuardiaPuntual(UsrBean usr,HttpServletRequest request,String idInstitucion, String idTurno,String idGuardia,String idCalendarioGuardias,String idPersonaSaliente,String fechaInicio,String fechaFin,String idPersonaEntrante,String salto,String compensacion,String sustituta, String comenSustitucion) throws ClsExceptions
	{
		String mensaje = "OK";
				 
		//Hashtables
		Hashtable temporalHash = new Hashtable();
		Hashtable clavesPermuta = new Hashtable();
		Hashtable clavesCabecera = new Hashtable();
		Hashtable clavesGuardiaColegiado = new Hashtable();
		Hashtable saltosCompens = new Hashtable();
		
		// Administradores
		ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(this.usrbean);
		ScsPermutaGuardiasAdm admPermutas = new ScsPermutaGuardiasAdm(this.usrbean);
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(this.usrbean);
		ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(this.usrbean);
		ScsSaltosCompensacionesAdm saltosCompAdm = new ScsSaltosCompensacionesAdm(this.usrbean);
		ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm(this.usrbean);
		BusquedaClientesFiltrosAdm admFiltros = new BusquedaClientesFiltrosAdm(this.usrbean);

		
		// vectores
		
		Vector permutasComoSolicitante = new Vector(); // Guarda todas las permutas del saliente en las que
													   // actua como solicitante. Cada permuta es un bean
		
		Vector permutasComoConfirmador = new Vector(); // Guarda todas las permutas del saliente en las que
		   											   // actua como confirmador. Cada permuta es un bean
		
		Vector cabeceraGuarSaliente    = new Vector();  // Guarda el registro de la tabla SCS_CABECERAGUARDIAS correspondiente
													   // al letrado saliente
		
		Vector guardiasColegSaliente = new Vector();   // Guardia los registros de la tabla SCS_GUARDIASCOLEGIADO correspondientes 
													   //al letrado saliente
		
		
		//-----------------------------------------------------------------------------------------------------
		//  Comprobamos si el letrado entrante cumple los criteriosde separación de guardias i de incompatibilidades
		//------------------------------------------------------------------------------------------------------
		
		boolean poolRW = false;	
		
		
		//-----------------------------------------------------------------------------------------------------
		// Recuperamos todas las permutas en las que el saliente sea o confirmador o solicitante. Posteriormente
		// borraremos dichas permutas y volveremos a insertar aquellas para las que fecha de confirmacion sea 
		// distinta de null
		//-----------------------------------------------------------------------------------------------------
		
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDINSTITUCION,idInstitucion);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE,idGuardia);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE,idTurno);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN,idCalendarioGuardias);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));//DD/MM/YYYY
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE,idPersonaSaliente);
		
		/* permutas en las que el saliente es solicitante */
		permutasComoSolicitante = admPermutas.select(clavesPermuta);
		
		clavesPermuta.clear();
		
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDINSTITUCION,idInstitucion);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR,idGuardia);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR,idTurno);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD,idCalendarioGuardias);
		clavesPermuta.put(ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));//DD/MM/YYYY
		clavesPermuta.put(ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR,idPersonaSaliente);
		
		/* permutas en las qu el saliente es confirmador */
		permutasComoConfirmador = admPermutas.select(clavesPermuta);
		
		//----------------------------------------------------------------------------------------------
		// guardamos los registros de cabecera de guardias y guardias colegiado para el colegiado saliente
		//
		//----------------------------------------------------------------------------------------------
		
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,idInstitucion);
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_IDTURNO,idTurno);
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,idGuardia);
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardias);
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idPersonaSaliente);
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		cabeceraGuarSaliente = cabeceraGuardiasAdm.selectByPK(clavesCabecera);
		
		clavesGuardiaColegiado.clear();
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardias);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaSaliente);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		guardiasColegSaliente = this.select(clavesGuardiaColegiado);
		
		
		//-----------------------------------------------------------------------------
		//  Borramos los registros de las permutas obtenidas para el saliente tanto aquellas en las que actua como
		// solicitante como las que actua como confirmador
		//-----------------------------------------------------------------------------
		
		if(permutasComoSolicitante != null && permutasComoSolicitante.size() > 0){
			for(int i = 0; i < permutasComoSolicitante.size(); i++){
				ScsPermutaGuardiasBean beanPermutaComoSolic = (ScsPermutaGuardiasBean)(permutasComoSolicitante.elementAt(i));
				if(!admPermutas.delete(beanPermutaComoSolic))
					throw new ClsExceptions(admPermutas.getError());
			}
		}
		
		if(permutasComoConfirmador != null && permutasComoConfirmador.size() > 0){
			for(int i = 0; i < permutasComoConfirmador.size(); i++){
				ScsPermutaGuardiasBean beanPermutaComoConfir = (ScsPermutaGuardiasBean)(permutasComoConfirmador.elementAt(i));
				if(!admPermutas.delete(beanPermutaComoConfir))
					throw new ClsExceptions(admPermutas.getError());
			}
		}
		//----------------------------------------------------------------------------------------------------
		// Borramos los registros de la tabla SCS_GUARDIASCOLEGIADO para el letrado saliente
		//-----------------------------------------------------------------------------------------------------
		
		if(guardiasColegSaliente != null && guardiasColegSaliente.size() > 0){
			for(int i=0;i<guardiasColegSaliente.size();i++){
				ScsGuardiasColegiadoBean guardiasColSalBean = (ScsGuardiasColegiadoBean)(guardiasColegSaliente.elementAt(i));
				if(!this.delete(guardiasColSalBean))
					throw new ClsExceptions(this.getError());
			}
		}
		
		//----------------------------------------------------------------------------------------------------
		// Borramos el registro de la tabla SCS_CABECERAGUARDIAS para el letrado saliente
		//-----------------------------------------------------------------------------------------------------
		
		ScsCabeceraGuardiasBean cabeceraGuarSal = (ScsCabeceraGuardiasBean)(cabeceraGuarSaliente.elementAt(0));
		if(!cabeceraGuardiasAdm.delete(cabeceraGuarSal))
			throw new ClsExceptions(cabeceraGuardiasAdm.getError());
		
		
		//---------------------------------------------------------------------------------------------------
		// Insertamos el registro antes obtenido de la tabla SCS_CABECERAGUARDIAS cambiando el idpersona
		// (correspondiente al saliente) por le idepersona del entrante
		//---------------------------------------------------------------------------------------------------
		
		cabeceraGuarSal.setIdPersona(Long.valueOf(idPersonaEntrante));
		cabeceraGuarSal.setLetradoSustituido(Long.valueOf(idPersonaSaliente));
		cabeceraGuarSal.setFechaSustitucion(GstDate.getApplicationFormatDate(usr.getLanguage(),UtilidadesBDAdm.getFechaBD(usr.getLanguage())));
		if(sustituta.equalsIgnoreCase("NO")){
			//cabeceraGuarSal.setComenSustitucion(UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.literal.letrado.añadido.sustitucion"));
			  cabeceraGuarSal.setComenSustitucion(comenSustitucion);
		}else{
			//cabeceraGuarSal.setComenSustitucion(UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.literal.letrado.guardia.sustitucion"));
			cabeceraGuarSal.setComenSustitucion(comenSustitucion);
		}
		if(!cabeceraGuardiasAdm.insert((cabeceraGuarSal)))
			throw new ClsExceptions(cabeceraGuardiasAdm.getError());
		
		//---------------------------------------------------------------------------------------------------
		// Insertamos los registros antes obtenidos de la tabla SCS_GUARDIASCOLEGIADO cambiando el idpersona
		// (correspondiente al saliente) por el idepersona del entrante
		//---------------------------------------------------------------------------------------------------
		
		
		if(guardiasColegSaliente != null && guardiasColegSaliente.size() > 0){
			for(int i=0;i<guardiasColegSaliente.size();i++){
				ScsGuardiasColegiadoBean guardiasColSalBean = (ScsGuardiasColegiadoBean)(guardiasColegSaliente.elementAt(i));
				guardiasColSalBean.setIdPersona(new Long(idPersonaEntrante));
				if(!this.insert(guardiasColSalBean))
					throw new ClsExceptions(this.getError());
			}
		}
		
		//----------------------------------------------------------------------------------------------------
		// Insertamos los registros de permutas obtenidos anteriormente para el saliente como confirmador y
		// solicitante cambiando le idpersona por el del letrado entrante.Sólo insertamos aquellos regstros
		// cuya fecha de confirmación no sea null (las peticiones de permuta las desechamos, solo interesan permutas confirmadas)
		//---------------------------------------------------------------------------------------------------
		
		if(permutasComoSolicitante != null && permutasComoSolicitante.size() > 0){
			for(int i = 0; i < permutasComoSolicitante.size(); i++){
				ScsPermutaGuardiasBean beanPermutaComoSolic = (ScsPermutaGuardiasBean)(permutasComoSolicitante.elementAt(i));
				if(beanPermutaComoSolic.getFechaConfirmacion() != null && !beanPermutaComoSolic.getFechaConfirmacion().equals("")){
					beanPermutaComoSolic.setIdPersonaSolicitante(new Integer(idPersonaEntrante));
					if(!admPermutas.insert(beanPermutaComoSolic))
						throw new ClsExceptions(admPermutas.getError());
				}
			}
		}
		
		if(permutasComoConfirmador != null && permutasComoConfirmador.size() > 0){
			for(int i = 0; i < permutasComoConfirmador.size(); i++){
				ScsPermutaGuardiasBean beanPermutaComoConf = (ScsPermutaGuardiasBean)(permutasComoConfirmador.elementAt(i));
				if(beanPermutaComoConf.getFechaConfirmacion() != null && !beanPermutaComoConf.getFechaConfirmacion().equals("")){
					beanPermutaComoConf.setIdPersonaConfirmador(new Integer(idPersonaEntrante));
					if(!admPermutas.insert(beanPermutaComoConf))
						throw new ClsExceptions(admPermutas.getError());
				}
			}
		}
		//--------------------------------------------------------------------------------------------------
		// Incluimos saltos (al entrante) y compensaciones  (al saliente) en funcion de los checks correspondientes
		//--------------------------------------------------------------------------------------------------
		/*saltosCompens.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,idInstitucion);
		saltosCompens.put(ScsSaltosCompensacionesBean.C_IDTURNO,idTurno);
		saltosCompens.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,saltosCompAdm.getNuevoIdSaltosTurno(idInstitucion,idTurno));
		saltosCompens.put(ScsSaltosCompensacionesBean.C_MOTIVOS,"SUSTITUCION");
		saltosCompens.put(ScsSaltosCompensacionesBean.C_FECHA,"SYSDATE");
		saltosCompens.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,"");
		saltosCompens.put(ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardias);
		saltosCompens.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,idGuardia);
		
		
		if(salto.equalsIgnoreCase("S"))
		{
			saltosCompens.put(ScsSaltosCompensacionesBean.C_IDPERSONA,idPersonaEntrante);
			saltosCompens.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,"S");
		}
		if(compensacion.equalsIgnoreCase("S"))
		{
			saltosCompens.put(ScsSaltosCompensacionesBean.C_IDPERSONA,idPersonaSaliente);
			saltosCompens.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,"C");
		}
		if(!saltosCompAdm.insert(saltosCompens))
			throw new ClsExceptions(saltosCompAdm.getError());*/
		String motivo = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.sustitucionLetradoGuardia");
		
		try{
			admFiltros.crearSalto(idInstitucion,idTurno,idGuardia,idPersonaEntrante,salto, motivo);
		}
		catch (Exception e){
			throw new ClsExceptions(e.getMessage());
		}
		
		try{
			admFiltros.crearCompensacion(idInstitucion,idTurno,idGuardia,idPersonaSaliente,compensacion, motivo);
		}
		catch (Exception e){
			throw new ClsExceptions(e.getMessage());
		}
			
		
		//-------------------------------------------------------------------------------------------------
		// Actualizamos la tabla de asistencias cambiando en dicha tabla el idpersona del saliente por el  
		//idpersona del entrante. Actualizamos aquellas asistencias para las que la fecha de realizacion sea
		// igual al campo fechafin de cada uno de los registros de la tabla gusrdiascolegiado
		//-------------------------------------------------------------------------------------------------
		
		try {
			String update="";
			if(guardiasColegSaliente != null && guardiasColegSaliente.size() > 0){
				for(int i=0;i<guardiasColegSaliente.size();i++){
					ScsGuardiasColegiadoBean guardiasColSalBean = (ScsGuardiasColegiadoBean)(guardiasColegSaliente.elementAt(i));
					//String fecha = guardiasColSalBean.getFechaFin();
					String fecha = UtilidadesString.formatoFecha(guardiasColSalBean.getFechaFin(), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
					
					update = "UPDATE " + ScsAsistenciasBean.T_NOMBRETABLA + " SET " 
							+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + idPersonaEntrante 
							+ " WHERE " + ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + idPersonaSaliente
							+ "   AND " + ScsAsistenciasBean.C_IDINSTITUCION + "=" + idInstitucion
							+ "   AND TRUNC(" + ScsAsistenciasBean.C_FECHAHORA + ")= TRUNC(TO_DATE('" + fecha + "', 'DD/MM/YYYY'))";
							
					int a = ClsMngBBDD.executeUpdate(update);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e.getMessage());
		}
		
		
		
		
	}
	
	public String validacionesSustitucionGuardia(UsrBean usr, String idInstitucion, String idTurno, String idGuardia, String idCalendarioGuardias, String fechaInicio,String fechaFin,String idPersonaEntrante, String idPersonaSaliente) throws ClsExceptions
	{
		Hashtable temporalHash = new Hashtable();
		Hashtable clavesGuardiaColegiado = new Hashtable();
		
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardias);
		temporalHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,fechaInicio);//DD/MM/YYYY
		temporalHash.put(ScsGuardiasColegiadoBean.C_FECHAFIN,fechaFin);//DD/MM/YYYY
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaEntrante);
		
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardias);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaSaliente);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		Vector v = this.select(clavesGuardiaColegiado);
		ArrayList a = new ArrayList();
		if(v != null && v.size() > 0)
		{
			for(int i=0;i<v.size();i++)
			{
				ScsGuardiasColegiadoBean bean = (ScsGuardiasColegiadoBean)v.elementAt(i);
				a.add(GstDate.getFormatedDateShort(usr.getLanguage(),bean.getFechaFin()));
			}
		}
//El siguiente bloque de codigo se comenta de forma temporal para cuando 
//tengamos tiempo de revisar y arreglar estas comprobaciones
//		//2.Validacion de que para ese dia el Letrado entrante no haga una guardia incompatible:
//		if (!this.validarIncompatibilidadGuardia(idInstitucion, idTurno, idGuardia, a, idPersonaEntrante)) 
//			return "gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorIncompatibilidad";
//		//3.Validacion de la separacion entre guardias para el letrado entrante:
//		if (!this.validarSeparacionGuardias(temporalHash)) 
//			return "gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorSeparacion";
		
		return "OK";
	
	}
	public void validarColegiadoEntrante(UsrBean usr, String idInstitucion, String idTurno, String idGuardia, String idCalendarioGuardias, String fechaInicio,String fechaFin,String idPersonaEntrante) throws SIGAException,ClsExceptions
	{
		
		Hashtable clavesGuardiaColegiado = new Hashtable();
		
		
		
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardias);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaEntrante);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		
		Vector v = this.select(clavesGuardiaColegiado);
		if(v != null && v.size() > 0)
		{
			throw new SIGAException("gratuita.volantesExpres.mensaje.letradoSustituyeConGuardia");
		}
		
		
		

		
	
	
	}
	
	
	//Borra las guardias para una cabecera de guardias y una persona
	public boolean deleteGuardiasColegiado(Hashtable hashCabecera) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idturno = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idguardia = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			
			idPersona = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
			//Fechas del periodo:
			fechaInicio = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
			fechaFin = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_FIN), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");

			sql.append(" delete from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sql.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDGUARDIA+"="+idguardia);
			sql.append(" and "+ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idPersona);
			sql.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+") between TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
			sql.append(" and TO_DATE('"+fechaFin+"','DD/MM/YYYY')");
					
			deleteSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}	
	
	public Vector getColegiadosGuardiaDia(String idInstitucion, String idTurno, String idGuardia, String fecha) throws ClsExceptions {
		Vector salida = new Vector();
		String consulta = new String();
		consulta = "select distinct idpersona as ID, (select cen_persona.nombre || ' ' || cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 from cen_persona where cen_persona.idpersona=scs_guardiascolegiado.idpersona) as DESCRIPCION, (select f_siga_calculoncolegiado(scs_guardiascolegiado.idinstitucion,scs_guardiascolegiado.idpersona) from cen_colegiado where cen_colegiado.idinstitucion=scs_guardiascolegiado.idinstitucion and cen_colegiado.idpersona=scs_guardiascolegiado.idpersona) as NCOLEGIADO  " +
				   " from scs_guardiascolegiado where " +
		           " idinstitucion="+idInstitucion+" and idturno="+idTurno+" and idguardia="+idGuardia+" and trunc(to_date('"+fecha+"','DD/MM/YYYY')) between trunc(fechainicio) and trunc(fechafin) " +
		           " order by DESCRIPCION";
		try {
			salida = this.selectGenerico(consulta.toString());
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error en consulta de getColegiadosGuardiaDia");
		}
		return salida;
	}
	public List<CenPersonaBean> getColegiadosGuardia(VolantesExpressVo volanteExpres,boolean withAsistencias)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		String truncFechaGuardia = null;
		if(!withAsistencias){
			sql.append(" SELECT * from ( ");
		}
		
		
		sql.append(" SELECT DISTINCT PER.IDPERSONA,PER.NOMBRE || ' ' || ");
		sql.append(" PER.APELLIDOS1 || ' ' || ");
		sql.append(" PER.APELLIDOS2 AS NOMBRE");
		if(!withAsistencias){
			if(volanteExpres.getFechaGuardia()!=null){
				truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
				sql.append(" ,( SELECT COUNT(*) FROM SCS_ASISTENCIA A WHERE A.IDPERSONACOLEGIADO=GC.IDPERSONA ");
				sql.append(" AND A.IDINSTITUCION = GC.IDINSTITUCION ");
				sql.append(" AND A.IDTURNO = GC.IDTURNO ");
				sql.append(" AND A.IDGUARDIA = GC.IDGUARDIA ");
				sql.append(" AND TRUNC(A.FECHAHORA) = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),truncFechaGuardia);
				sql.append(") NUMASISTENCIAS ");
				
				
			}
		}
		sql.append(" FROM SCS_GUARDIASCOLEGIADO GC,CEN_PERSONA PER ");
		sql.append(" WHERE GC.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
		sql.append(" AND GC.IDTURNO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdTurno());
		sql.append(" AND GC.IDGUARDIA = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdGuardia());
	
		if(volanteExpres.getFechaGuardia()!=null){
			if(truncFechaGuardia==null)
				truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
			sql.append(" AND :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),truncFechaGuardia);
			sql.append(" BETWEEN ");
			sql.append(" TRUNC(GC.FECHAINICIO) AND TRUNC(GC.FECHAFIN) ");
		}
		sql.append(" AND PER.IDPERSONA = GC.IDPERSONA ");
		sql.append(" ORDER BY NOMBRE ");
		if(!withAsistencias){
			sql.append(" ) where NUMASISTENCIAS=0");
		}
		List<CenPersonaBean> alColegiadosGuardias = null;
		CenPersonaBean personaBean = null;
		
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alColegiadosGuardias = new ArrayList<CenPersonaBean>();
            	personaBean = new CenPersonaBean();
            	personaBean.setIdPersona(new Long(-1));
            	personaBean.setNombre(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(), "general.combo.seleccionar"));
            	alColegiadosGuardias.add(personaBean);
            	for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		personaBean = new CenPersonaBean();
            		personaBean.setNombre(UtilidadesHash.getString(htFila,CenPersonaBean.C_NOMBRE));
            		personaBean.setIdPersona(UtilidadesHash.getLong(htFila,CenPersonaBean.C_IDPERSONA));
            		alColegiadosGuardias.add(personaBean);
            	}
            } 
       } catch (Exception e) {
    	   ClsLogging.writeFileLog("VOLANTES EXPRESS:Error Select Colegiados"+e.toString(), 10);
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alColegiadosGuardias;
		}
			
	public Vector selectParaVolantes (Hashtable h) 
	{
		try {
			int i = 0;
			String campos[] = new String[h.size()];
			Enumeration e = h.keys();
			while (e.hasMoreElements()) {
				campos[i] = (String) e.nextElement();
				i++;
			}
			
			String where = UtilidadesBDAdm.sqlWhere(ScsGuardiasColegiadoBean.T_NOMBRETABLA, h, campos); 
			String sql = " select c.* " +
					       " from (select c.* " +
					               " from scs_guardiascolegiado c " + where + ") c, scs_guardiasturno g " +
					      " where g.idinstitucion = c.idinstitucion " +
					        " and g.idturno = c.idturno " +
					        " and g.idguardia = c.idguardia " +
					        " and (g.idguardiasustitucion is null " +
					               " or (g.idguardiasustitucion is not null " +
					               " and g.idguardiasustitucion not in (select c.idguardia " +
														               " from scs_guardiascolegiado c " + where + " )))";
			
			
			Vector vSalida = new Vector ();
			Vector v = this.selectGenerico(sql);
			
			if (v != null) {
				for (int j = 0; j < v.size(); j++) {
					vSalida.add(this.hashTableToBean((Hashtable)v.get(j)));
				}
			}
			
			return vSalida; 
		} 
		catch (Exception e) {
			return null;
		}
		
	}
	
	static public String getNombreGuardia (String idinstitucion,String idTurno, String idGuardia)  throws ClsExceptions,SIGAException 
	{
		
		Vector v;
		Hashtable codigos = new Hashtable();
		String consulta="";
		try {
		    codigos.put(new Integer(1),idinstitucion);
		    codigos.put(new Integer(2),idTurno);
		    codigos.put(new Integer(3),idGuardia);
		    if (idTurno!=null && !idTurno.equals("")){
		    consulta="select nombre NOMBRE_GUARDIA from scs_guardiasTurno where idinstitucion=:1 and idturno=:2 and idguardia=:3";
		    RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(consulta, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "NOMBRE_GUARDIA");
				return num;
			}
		   }
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el nombre de la guardia.");
	   			}
	   		}	
	    }
		return "";
	}
	public void aplicarSustitucion(Integer idInstitucion, Integer idTurno, 
			Integer idGuardia, Long idPersonaSaliente, Long idPersonaEntrante, 
			String fechaFin, UsrBean usr) throws SIGAException,ClsExceptions
			{


		fechaFin = GstDate.getApplicationFormatDate("", fechaFin);

		// Obtenemos el idCalendario
		Hashtable h = new Hashtable();
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, idPersonaSaliente);
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, fechaFin);
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, idTurno);
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, idGuardia);
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
		ScsGuardiasColegiadoAdm admAux = new ScsGuardiasColegiadoAdm (usr);
		Vector vGuardias = admAux.select(h);
		if (vGuardias != null && vGuardias.size() != 1) {
			throw new SIGAException("gratuita.volantesExpres.error.guardiaConVariosCalendarios");
		}
		ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) vGuardias.get(0);
		String idCalendarioGuardias = ""+b.getIdCalendarioGuardias();
		String fechaInicio = GstDate.getFormatedDateLong("", b.getFechaInicio()); 
		String salto = null; 			// No creamos salto
		String compensacion = null; 	// No creamos compensacion



		String mensaje = validacionesSustitucionGuardia(usr,idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), idCalendarioGuardias, fechaInicio,fechaFin,idPersonaEntrante.toString(), idPersonaSaliente.toString());

		if(!mensaje.equalsIgnoreCase("OK")) {
			throw new SIGAException("messages.general.errorExcepcion");
		}
		else {
			validarColegiadoEntrante(usr,idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), idCalendarioGuardias, fechaInicio,fechaFin,idPersonaEntrante.toString());
			sustitucionLetradoGuardiaPuntual(usr, null, idInstitucion.toString(), idTurno.toString(),idGuardia.toString(),idCalendarioGuardias,idPersonaSaliente.toString(),fechaInicio,fechaFin,idPersonaEntrante.toString(), salto, compensacion,"si","");
		}



	}
	
	public void insertarGuardiaManual(String idInstitucion, String idTurno, 
			String idGuardia, String idPersonaEntrante, 
			String fechaFin, UsrBean usr) throws ClsExceptions, SIGAException{
			String idCalendarioGuardias = null;//miForm.getIdCalendarioGuardias();
			String idPersona =idPersonaEntrante;// miForm.getIdPersona();
			
			//Periodo:
			int indicePeriodo = 0;// Integer.parseInt(miForm.getIndicePeriodo());
			Hashtable h = new Hashtable();
			//Hacemos un recorrido por todas los calendarios que tenemos. 
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, GstDate.getApplicationFormatDate("", fechaFin));
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, idGuardia);
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
			Vector vGuardias = this.select(h);
			if(vGuardias!=null &&vGuardias.size()>0){
				
				ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) vGuardias.get(0);
				idCalendarioGuardias = ""+b.getIdCalendarioGuardias();
				//Calculo los periodos de guardias:
				CalendarioSJCS calendarioSJCS = new CalendarioSJCS
					(new Integer(idInstitucion), new Integer(idTurno),
					new Integer(idGuardia), new Integer(idCalendarioGuardias),
					usr);
				calendarioSJCS.calcularMatrizPeriodosDiasGuardia();
				
				//Nota: El array arrayPeriodosSJCS es un array periodos y cada periodo es un array de dias
				ArrayList arrayPeriodosSJCS = calendarioSJCS.getArrayPeriodosDiasGuardiaSJCS();
				
				//Obtenemos los dias a Agrupar
				List lDiasASeparar = calendarioSJCS.getDiasASeparar(new Integer(idInstitucion), new Integer(idTurno), new Integer(idGuardia) , usr);
				
				//Selecciono el periodo de la lista de periodos:
				String fechaInicioCalendario ="";
				String fechaFinCalendario ="";
				boolean findIt = false;
				for (int i = 0; i < arrayPeriodosSJCS.size(); i++) {
					ArrayList auxArrayPeriodoSeleccionado = (ArrayList)arrayPeriodosSJCS.get(i);
					for (int j = 0; j < auxArrayPeriodoSeleccionado.size(); j++) {
						String fecha = (String)auxArrayPeriodoSeleccionado.get(j);
						if(fecha.equals(fechaFin)){
							fechaInicioCalendario = (String)auxArrayPeriodoSeleccionado.get(0);
							fechaFinCalendario = (String)auxArrayPeriodoSeleccionado.get(auxArrayPeriodoSeleccionado.size()-1);
							findIt = true;
							break;
						}
					}
					if(findIt){
						indicePeriodo = i;
						break;
					}
				}
				ArrayList arrayPeriodoSeleccionado = (ArrayList)arrayPeriodosSJCS.get(indicePeriodo);
				//Creo el Letrado:
				LetradoGuardia letrado = new LetradoGuardia
					(new Long(idPersona), new Integer(idInstitucion),
					new Integer(idTurno), new Integer(idGuardia));			
				
				//VALIDACIONES:
				//Relleno una hash con los datos necesarios para validar:
				Hashtable miHash = new Hashtable ();
				miHash.put("IDPERSONA",idPersona);
				miHash.put("IDINSTITUCION",idInstitucion);
				miHash.put("IDCALENDARIOGUARDIAS",idCalendarioGuardias);
				miHash.put("IDTURNO",idTurno);
				miHash.put("IDGUARDIA",idGuardia);
				miHash.put("FECHAINICIO",fechaInicioCalendario); //Del periodo
				miHash.put("FECHAFIN",fechaFinCalendario); //Del periodo
				//	METER VALIDACIONES TODAVIA NO DEFINIDAS
					//INSERT (INICIO TRANSACCION)
				try {
					//Almaceno en BBDD la cabecera y las guardias colegiado para este letrado:
					calendarioSJCS.almacenarAsignacionGuardiaLetrado(letrado,arrayPeriodoSeleccionado,lDiasASeparar);
				} catch (ClsExceptions e) {
					throw e;
				}
			}else{
				throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
				
			}
		}
	public ScsGuardiasColegiadoBean getGuardiaSinCabecera(VolantesExpressVo volanteExpres)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
        
		sql.append(" SELECT * FROM SCS_CALENDARIOGUARDIAS GC ");
		sql.append(" WHERE GC.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
		sql.append(" AND  GC.IDTURNO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdTurno());
		sql.append(" AND  GC.IDGUARDIA = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),volanteExpres.getIdGuardia());
		sql.append(" AND :");
		contador ++;
		sql.append(contador);
		String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
		htCodigos.put(new Integer(contador),truncFechaGuardia);
		sql.append(" BETWEEN TRUNC(GC.FECHAINICIO) AND ");
		sql.append(" TRUNC(GC.FECHAFIN) ");	
		

			
			
		
		ScsGuardiasColegiadoBean guardiaBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
        		if(rc.size()>0){
	            	Row fila = (Row) rc.get(0);
	        		Hashtable<String, Object> htFila=fila.getRow(); 
	            	guardiaBean = new ScsGuardiasColegiadoBean();
	           		guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDTURNO));
	           		guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDGUARDIA));
	           		guardiaBean.setIdCalendarioGuardias(UtilidadesHash.getInteger(htFila,ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS));
        		}
            	
            }
 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return guardiaBean;
		
		
	} 
	
	
}