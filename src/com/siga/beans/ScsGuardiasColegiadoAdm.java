
package com.siga.beans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsConstants;
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
import com.siga.general.SIGAException;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;
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
	 * @param usuario Usuario "logado" en la aplicaci�n. De tipo "Integer".  
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
			ScsGuardiasColegiadoBean.C_IDPERSONA,
			ScsGuardiasColegiadoBean.C_FECHAINICIO,
			ScsGuardiasColegiadoBean.C_FECHAFIN
		};
		return campos;
	} //getClavesBean ()
	
	/** 
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la informaci�n de la hashtable
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
	 * @return hashtable con la informaci�n del bean
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
	 * @return String[] conjunto de valores con los campos por los que se deber� ordenar la select
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
			sql.append(" where ("+ScsGuardiasColegiadoBean.C_IDINSTITUCION + ",");
			sql.append(" "+ScsGuardiasColegiadoBean.C_IDTURNO + ",");
			sql.append(" "+ScsGuardiasColegiadoBean.C_IDGUARDIA + ",");
			sql.append(" "+ScsGuardiasColegiadoBean.C_IDPERSONA + ",");
			sql.append(" "+ScsGuardiasColegiadoBean.C_FECHAINICIO);

			sql.append(" ) in (select ");
			sql.append(" "+ScsCabeceraGuardiasBean.C_IDINSTITUCION + ",");
			sql.append(" "+ScsCabeceraGuardiasBean.C_IDTURNO + ",");
			sql.append(" "+ScsCabeceraGuardiasBean.C_IDGUARDIA + ",");
			sql.append(" "+ScsCabeceraGuardiasBean.C_IDPERSONA + ",");
			sql.append(" "+ScsCabeceraGuardiasBean.C_FECHA_INICIO);
			sql.append(" from "+ScsCabeceraGuardiasBean.T_NOMBRETABLA);
			sql.append(" where "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia);
			sql.append(" ) ");

			deleteSQL(sql.toString());		
//			ClsMngBBDD.executeUpdate(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}

	/**
	 * Ejecuta un delete
	 * @param Hashtable hash: tabla hash copn los campos: 
	 * - String idinstitucion
	 * - String idturno
	 * - String idguardia
	 * - String idPersona    
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deleteGuardiaCalendario(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idturno="", idguardia="", fechaInicio="", idPersona="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);
			idPersona = (String)hash.get("IDPERSONA");
			fechaInicio = (String)hash.get(ScsCalendarioGuardiasBean.C_FECHAINICIO);

			sql.append(" delete from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sql.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
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
	 * Validacion: Consulta para buscar los dias de separacion entre guardias y validarlos
	 * Devuelve 0 si no encuentra separaciones incorrectas
	 * 
	 * La Hash debe tener los siguientes datos:
	 * -String "IDPERSONA"
	 * -String "IDINSTITUCION"
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
		String idinstitucion="", idguardia="", idturno="", idpersona="";
		String fechaPeriodoUltimoDia="", fechaPeriodoPrimerDia="", fechaPeriodoPrimerDiaOriginal="";
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

	/**
	 * Valida que la separacion de dias para una guardia de un calendario es correcta.
	 * La Hash debe tener los siguientes datos:
	 * -String "IDPERSONA"
	 * -String "IDINSTITUCION"
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
	 * Obtiene si tiene guardias el dia y es posterior al dia actual (SCS_GUARDIASCOLEGIADO)
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param idPersona
	 * @param fechaInicio
	 * @return
	 */
	public boolean existeGuardiaParaBorrar(String idInstitucion, String idTurno, String idGuardia, String idPersona, String fechaInicio) {
		Vector<Hashtable<String,Object>> vGuardias = new Vector<Hashtable<String,Object>>();
		StringBuffer consulta = new StringBuffer();
		consulta.append("SELECT 1 FROM ");
		consulta.append(ScsGuardiasColegiadoBean.T_NOMBRETABLA);
		consulta.append(" WHERE ");
		consulta.append(ScsGuardiasColegiadoBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion);
		consulta.append(" AND ");
		consulta.append(ScsGuardiasColegiadoBean.C_IDTURNO);
		consulta.append(" = ");
		consulta.append(idTurno);
		consulta.append(" AND ");
		consulta.append(ScsGuardiasColegiadoBean.C_IDGUARDIA);
		consulta.append(" = ");
		consulta.append(idGuardia);
	    consulta.append(" AND ");
	    consulta.append(ScsGuardiasColegiadoBean.C_IDPERSONA);
	    consulta.append(" = ");
	    consulta.append(idPersona);		
		consulta.append(" AND TRUNC(");
		consulta.append(ScsGuardiasColegiadoBean.C_FECHAINICIO);
		consulta.append(") = TO_DATE('");
		consulta.append(fechaInicio);
		consulta.append("','DD/MM/YYYY') AND TRUNC("); // JPT: Solo se puede borrar si la fecha es posterior al dia actual
		consulta.append(ScsGuardiasColegiadoBean.C_FECHAINICIO);
		consulta.append(") > TRUNC(SYSDATE)");
		
		try {
			vGuardias = this.selectGenerico(consulta.toString());
		} catch (Exception e) {
			return false;
		}
					
		return vGuardias.size() > 0;		
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
		consulta.append ("         UNION "); //OJO: es necesario mirar ambas, para que funcione igual que se muestra
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
		StringBuffer sql = null; 
		try {
			Iterator iterDiasGuardia = arrayDiasGuardia.iterator ();
			String fechaGuardia;
			while (iterDiasGuardia.hasNext () && !encontrado) {
				sql = new StringBuffer(consulta);
				fechaGuardia = (String) iterDiasGuardia.next ();
				sql.append (" and abs (gc."+ScsGuardiasColegiadoBean.C_FECHAFIN+" - to_date('"+fechaGuardia+"', 'DD/MM/YYYY')) ");
				sql.append (" <= g_incompatibles."+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+" ");
				vFechaFin = this.selectGenerico (sql.toString ());
				if (! vFechaFin.isEmpty ())
					encontrado = true;
			}
		} catch (Exception e) {
			encontrado = true;
		}
		return !encontrado;
	}
	
	
	public void sustitucionLetradoGuardiaPuntual(UsrBean usr,HttpServletRequest request,String idInstitucion, String idTurno,String idGuardia,String idCalendarioGuardias,String idPersonaSaliente,String fechaInicio,String fechaFin,String idPersonaEntrante,String salto,String compensacion,String sustituta, String comenSustitucion) throws ClsExceptions, SIGAException
	{
		//Hashtables
		Hashtable clavesPermuta = new Hashtable();
		Hashtable clavesCabecera = new Hashtable();
		Hashtable clavesGuardiaColegiado = new Hashtable();
		
		// Administradores

		ScsPermutaGuardiasAdm admPermutas = new ScsPermutaGuardiasAdm(this.usrbean);
		ScsPermutaCabeceraAdm admPermutasCabeceras = new ScsPermutaCabeceraAdm(this.usrbean);
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(this.usrbean);
		ScsSaltosCompensacionesAdm saltosCompAdm = new ScsSaltosCompensacionesAdm(this.usrbean);


		
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
		//  Comprobamos si el letrado entrante cumple los criteriosde separaci�n de guardias i de incompatibilidades
		//------------------------------------------------------------------------------------------------------
		
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
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idPersonaSaliente);
		clavesCabecera.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		cabeceraGuarSaliente = cabeceraGuardiasAdm.selectByPK(clavesCabecera);
		
		clavesGuardiaColegiado.clear();
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
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
		
		ScsPermutaCabeceraBean beanPermutaCabecera = new ScsPermutaCabeceraBean();
		beanPermutaCabecera.setIdInstitucion(new Integer(idInstitucion));
		beanPermutaCabecera.setIdTurno(new Integer(idTurno));
		beanPermutaCabecera.setIdGuardia(new Integer(idGuardia));
		beanPermutaCabecera.setIdCalendarioGuardias(new Integer(idCalendarioGuardias));
		beanPermutaCabecera.setIdPersona(new Integer(idPersonaSaliente));
		beanPermutaCabecera.setFecha(GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		// Realiza los cambios previos a la sustitucion de una guardia para SCS_PERMUTA_CABECERA
		if (!admPermutasCabeceras.sustituirPrevioPermutasCalendario(beanPermutaCabecera))
			throw new ClsExceptions(admPermutasCabeceras.getError());
		
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
			//cabeceraGuarSal.setComenSustitucion(UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.literal.letrado.a�adido.sustitucion"));
			  cabeceraGuarSal.setComenSustitucion(comenSustitucion);
		}else{
			//cabeceraGuarSal.setComenSustitucion(UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.literal.letrado.guardia.sustitucion"));
			cabeceraGuarSal.setComenSustitucion(comenSustitucion);
		}
		cabeceraGuarSal.setFechaAlta("SYSDATE");
		cabeceraGuarSal.setUsuAlta(usuModificacion);
		
		//Antes de insertar el registro se comprueba si el letrado ya tiene una guardia en ese turno y periodo
		if(cabeceraGuardiasAdm.validaGuardiaLetradoPeriodo(cabeceraGuarSal.getIdInstitucion(), cabeceraGuarSal.getIdTurno(), cabeceraGuarSal.getIdGuardia(), cabeceraGuarSal.getIdPersona(), fechaInicio, fechaFin))
			throw new SIGAException("gratuita.calendarios.guardias.mensaje.existe");
		
		
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
		// solicitante cambiando le idpersona por el del letrado entrante.S�lo insertamos aquellos regstros
		// cuya fecha de confirmaci�n no sea null (las peticiones de permuta las desechamos, solo interesan permutas confirmadas)
		//---------------------------------------------------------------------------------------------------
		
		// Realiza los cambios posteriores a la sustituacion de una guardia para SCS_PERMUTA_CABECERA
		beanPermutaCabecera.setIdPersona(new Integer(idPersonaEntrante));
		if (!admPermutasCabeceras.sustituirPosteriorPermutasCalendario(beanPermutaCabecera))
				throw new ClsExceptions(admPermutasCabeceras.getError());
		
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
			if (salto != null&&(salto.equals("on") || salto.equals("1"))) 
				saltosCompAdm.crearSaltoCompensacion(idInstitucion,idTurno,idGuardia,idPersonaEntrante, motivo, idCalendarioGuardias, ClsConstants.SALTOS);
		}
		catch (Exception e){
			throw new ClsExceptions(e.getMessage());
		}
		
		try{
			
			if (compensacion != null&&(compensacion.equals("on") || compensacion.equals("1"))) 
				saltosCompAdm.crearSaltoCompensacion(idInstitucion,idTurno,idGuardia,idPersonaSaliente, motivo, idCalendarioGuardias, ClsConstants.COMPENSACIONES);
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
							
					ClsMngBBDD.executeUpdate(update);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e.getMessage());
		}
	}
	
	public String validacionesSustitucionGuardia(UsrBean usr, String idInstitucion, String idTurno, String idGuardia, String fechaInicio,String fechaFin,String idPersonaEntrante, String idPersonaSaliente) throws ClsExceptions
	{
		Hashtable temporalHash = new Hashtable();
		Hashtable clavesGuardiaColegiado = new Hashtable();
		
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		temporalHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,fechaInicio);//DD/MM/YYYY
		temporalHash.put(ScsGuardiasColegiadoBean.C_FECHAFIN,fechaFin);//DD/MM/YYYY
		temporalHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaEntrante);
		
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
		clavesGuardiaColegiado.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
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
	
	//Borra las guardias para una cabecera de guardias y una persona
	public boolean deleteGuardiasColegiado(Hashtable hashCabecera) throws ClsExceptions {
		String idinstitucion="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idturno = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idguardia = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			
			idPersona = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
			//Fechas del periodo:
			fechaInicio = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
			fechaFin = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_FIN), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");

			sql.append(" delete from "+ScsGuardiasColegiadoBean.T_NOMBRETABLA);
			sql.append(" where "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idinstitucion);
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
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param fecha
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> getColegiadosGuardiaDia(String idInstitucion, String idTurno, String idGuardia, String fecha) throws ClsExceptions {
		Vector<Hashtable<String,Object>> salida = new Vector<Hashtable<String,Object>>();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;	
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT P.IDPERSONA AS ID, ");
		sql.append(" P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' || NVL(P.APELLIDOS2, '') AS DESCRIPCION, ");
		sql.append(" DECODE(COL.COMUNITARIO, '1', COL.NCOMUNITARIO, COL.NCOLEGIADO) AS NCOLEGIADO ");
		sql.append(" FROM SCS_GUARDIASCOLEGIADO GC, ");
		sql.append(" SCS_CABECERAGUARDIAS CG, ");
		sql.append(" CEN_PERSONA P, ");
		sql.append(" CEN_COLEGIADO COL ");
		sql.append(" WHERE GC.IDINSTITUCION = CG.IDINSTITUCION ");
		sql.append(" AND GC.IDTURNO = CG.IDTURNO ");
		sql.append(" AND GC.IDGUARDIA = CG.IDGUARDIA ");
		sql.append(" AND GC.FECHAINICIO = CG.FECHAINICIO ");
		sql.append(" AND GC.IDPERSONA = CG.IDPERSONA ");
		sql.append(" AND GC.IDPERSONA = P.IDPERSONA ");
		sql.append(" AND P.IDPERSONA = COL.IDPERSONA "); 
		sql.append(" AND GC.IDINSTITUCION = COL.IDINSTITUCION ");
		
		sql.append(" AND GC.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idInstitucion);
		
		sql.append(" AND GC.IDTURNO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idTurno);
		
		sql.append(" AND GC.IDGUARDIA = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idGuardia);
		
		sql.append(" AND TRUNC(GC.FECHAFIN) = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), fecha);
		
		sql.append(" ORDER BY CG.POSICION");
		
		try {
			salida = this.selectGenericoBind(sql.toString(), htCodigos);
			
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
		sql.append(" ,CG.POSICION POSICION");
		
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
		sql.append(" FROM SCS_GUARDIASCOLEGIADO GC,SCS_CABECERAGUARDIAS CG,CEN_PERSONA PER ");
		sql.append(" WHERE ");
		sql.append("GC.IDINSTITUCION = CG.IDINSTITUCION ");
		sql.append("AND GC.IDTURNO = CG.IDTURNO ");
		sql.append("AND GC.IDGUARDIA = CG.IDGUARDIA ");
		sql.append("AND GC.FECHAINICIO = CG.FECHAINICIO ");
		sql.append("AND GC.IDPERSONA = CG.IDPERSONA ");
		
		sql.append("AND GC.IDINSTITUCION = :");
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
		//sql.append(" ORDER BY NOMBRE ");
		sql.append(" ORDER BY POSICION, NOMBRE ");
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
	
	/**
	 * Crea una guardia (cabecera y dias) dada una fecha.<br>
	 * Este metodo se utiliza por Volantes Expres y en Calendario > Nuevo Letrado
	 */
	public void insertarGuardiaManual(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idPersonaEntrante,
			Integer indicePeriodo,
			String fechaInicio,
			String fechaFin,
			UsrBean usr) throws ClsExceptions, SIGAException {
		insertarGuardiaManual(idInstitucion, idTurno, idGuardia, idPersonaEntrante, null, indicePeriodo, fechaInicio, fechaFin, usr);
	}
	public void insertarGuardiaManual(String idInstitucion,
			String idTurno,
			String idGuardia,
			String idPersonaEntrante,
			String sIdCalendarioGuardias,
			Integer indicePeriodo,
			String fechaInicio,
			String fechaFin,
			UsrBean usr) throws ClsExceptions, SIGAException
	{
		// Controles
		ScsCalendarioGuardiasAdm calAdm = new ScsCalendarioGuardiasAdm(usr);
		CenPersonaAdm perAdm = new CenPersonaAdm(usr);
		ScsCabeceraGuardiasAdm cabeceraAdm = new ScsCabeceraGuardiasAdm(usr);
		
		// Variables
		Long idPersona = new Long(idPersonaEntrante);// miForm.getIdPersona();
		Integer idCalendarioGuardias = null;// miForm.getIdCalendarioGuardias();
		if (sIdCalendarioGuardias != null && !sIdCalendarioGuardias.equals("")){
			try{
				idCalendarioGuardias = Integer.valueOf(sIdCalendarioGuardias);
			} catch (Exception e) {
				idCalendarioGuardias = null;
			}
		}
		String fechaInicioCalendario;
		String fechaFinCalendario;
		int indexPeriodo;// Integer.parseInt(miForm.getIndicePeriodo());

		// obteniendo calendario donde se insertara la guardia
		if (idCalendarioGuardias == null){
			ScsCalendarioGuardiasBean calendarioGuardiasBean = calAdm.getCalendarioGuardias(new Integer(idInstitucion), new Integer(idTurno), new Integer(idGuardia), (fechaInicio == null) ? fechaFin : fechaInicio); 
			if (calendarioGuardiasBean == null)
				throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
			idCalendarioGuardias = calendarioGuardiasBean.getIdCalendarioGuardias();
		}

		// calculando los periodos de guardias
		CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
		calendarioSJCS.inicializaParaMatriz(new Integer(idInstitucion), new Integer(idTurno),
				new Integer(idGuardia), idCalendarioGuardias, null, usr, null);
		calendarioSJCS.calcularMatrizPeriodosDiasGuardia();
		// Nota: El array arrayPeriodosSJCS es un array periodos y cada periodo es un array de dias
		ArrayList arrayPeriodosSJCS = calendarioSJCS.getArrayPeriodosDiasGuardiaSJCS();
		List lDiasASeparar = calendarioSJCS.getDiasASeparar(new Integer(idInstitucion), new Integer(idTurno),
				new Integer(idGuardia), usr);

		// seleccionando el periodo de la lista de periodos
		ArrayList auxArrayPeriodoSeleccionado;
		String fecha;
		boolean findIt = false;
		if (indicePeriodo == null) {
			indexPeriodo = 0;
			fechaInicioCalendario = "";
			fechaFinCalendario = "";
			
			for (int i = 0; i < arrayPeriodosSJCS.size(); i++) {
				
				auxArrayPeriodoSeleccionado = (ArrayList) arrayPeriodosSJCS.get(i);
				for (int j = 0; j < auxArrayPeriodoSeleccionado.size(); j++) {
					fecha = (String) auxArrayPeriodoSeleccionado.get(j);
					if (fecha.equals(fechaFin)) {
						fechaInicioCalendario = (String) auxArrayPeriodoSeleccionado.get(0);
						fechaFinCalendario = (String) auxArrayPeriodoSeleccionado.get(auxArrayPeriodoSeleccionado.size() - 1);
						findIt = true;
						break;
					}
				}
				if (findIt) {
					indexPeriodo = i;
					break;
				}
			}
			if(!findIt)
				throw new SIGAException("gratuita.asistencias.mensaje.diaSinGuardia");
		} else {
			fechaInicioCalendario = fechaInicio;
			fechaFinCalendario = fechaFin;
			indexPeriodo = indicePeriodo.intValue();
		}
		
		ArrayList arrayPeriodoSeleccionado = (ArrayList) arrayPeriodosSJCS.get(indexPeriodo);
		
		// obteniendo el letrado
		LetradoInscripcion letrado = new LetradoInscripcion(perAdm.getPersonaPorId(idPersona.toString()), new Integer(
				idInstitucion), new Integer(idTurno), new Integer(idGuardia), null);

		Integer posicionLetradoGuardia = cabeceraAdm.getMaximaPosicionCabecera(letrado.getIdInstitucion(), letrado
				.getIdTurno(), letrado.getIdGuardia(), idCalendarioGuardias, fechaInicioCalendario);
		letrado.setPosicion((posicionLetradoGuardia == null) ? 0 : posicionLetradoGuardia + 1);

		// Validaciones: PENDIENTE DE DEFINIR
		Hashtable miHash = new Hashtable();
		miHash.put("IDPERSONA", idPersona);
		miHash.put("IDINSTITUCION", idInstitucion);
		miHash.put("IDCALENDARIOGUARDIAS", idCalendarioGuardias);
		miHash.put("IDTURNO", idTurno);
		miHash.put("IDGUARDIA", idGuardia);
		miHash.put("FECHAINICIO", fechaInicioCalendario); // Del periodo
		miHash.put("FECHAFIN", fechaFinCalendario); // Del periodo

		try {
			// almacenando en BBDD la cabecera y las guardias colegiado para este letrado
			calendarioSJCS.almacenarAsignacionGuardiaLetrado(letrado, arrayPeriodoSeleccionado, lDiasASeparar);
		} catch (ClsExceptions e) {
			throw e;
		}
	} // insertarGuardiaManual()
	
	
	public Map<Long,CenPersonaBean> getColegiadosDeGuardia(Integer idInstitucion, String fechaDesde,String fechaHasta)throws ClsExceptions{
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
        
		sql.append(" SELECT DISTINCT GC.IDPERSONA,");
		sql.append(" DECODE(COL.COMUNITARIO, '1', COL.NCOMUNITARIO,COL.NCOLEGIADO) NCOLEGIADO, ");
		sql.append(" PER.NOMBRE,  PER.APELLIDOS1,  PER.APELLIDOS2 ");
		sql.append(" FROM SCS_GUARDIASCOLEGIADO GC,CEN_COLEGIADO COL, CEN_PERSONA PER ");
		sql.append(" WHERE  ");

		sql.append(" COL.IDPERSONA = PER.IDPERSONA  ");
		sql.append(" AND GC.IDINSTITUCION = COL.IDINSTITUCION ");
		sql.append(" AND GC.IDPERSONA = PER.IDPERSONA ");
		sql.append(" AND GC.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);		
		sql.append(" AND GC. FECHAFIN BETWEEN :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),GstDate.getFormatedDateShort("",fechaDesde));
		sql.append(" AND :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),GstDate.getFormatedDateShort("",fechaHasta));
		
		Map<Long,CenPersonaBean> tmPersonas = null;
		CenPersonaBean persona = null;
		CenColegiadoBean colegiado = null;
		try {
			Vector datos = this.selectGenericoBind(sql.toString(), htCodigos);
			
			tmPersonas = new  TreeMap<Long, CenPersonaBean>();
			for (int i = 0; i < datos.size(); i++) {
				Hashtable htFila = (Hashtable) datos.get(i);
				persona = new CenPersonaBean();
				colegiado = new CenColegiadoBean();
				persona.setColegiado(colegiado);
				colegiado.setIdInstitucion(UtilidadesHash.getInteger(htFila,CenColegiadoBean.C_IDINSTITUCION));
				colegiado.setNColegiado(UtilidadesHash.getString(htFila,CenColegiadoBean.C_NCOLEGIADO));
				persona.setIdPersona(UtilidadesHash.getLong(htFila,CenPersonaBean.C_IDPERSONA));
				persona.setNombre(UtilidadesHash.getString(htFila,CenPersonaBean.C_NOMBRE));
				persona.setApellido1(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS1));
				persona.setApellido2(UtilidadesHash.getString(htFila,CenPersonaBean.C_APELLIDOS2));
				
				tmPersonas.put(persona.getIdPersona(),persona);
			}
 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }finally{
    	   if(tmPersonas==null)
    		   tmPersonas = new  TreeMap<Long, CenPersonaBean>();
       }
       return tmPersonas;
		
		
	}
	
	/**
	 * Nos da el numero de guardias que hay para un calendario
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public int getNumeroGuardias(String idInstitucion, String idTurno, String idGuardia, String idCalendario) throws ClsExceptions {
		Vector salida = new Vector();
		StringBuffer sql = new StringBuffer();
		int total = 0;

		sql.append(" SELECT count(1) TOTAL ");
		sql.append("   FROM SCS_CABECERAGUARDIAS guard ");
		sql.append("  WHERE guard.IDINSTITUCION = " + idInstitucion);
		sql.append("    AND guard.IDTURNO = " + idTurno);
		sql.append("    AND guard.IDGUARDIA = " + idGuardia);
		sql.append("    AND guard.IDCALENDARIOGUARDIAS = " + idCalendario);
	
		try {
			salida = this.selectGenerico(sql.toString());
			if(salida!=null && salida.size()>0){
				Hashtable result = (Hashtable) salida.get(0);
				total = Integer.valueOf((String)result.get("TOTAL"));
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error en consulta de getNumeroGuardias");
		}
		return total;
	}

	
}