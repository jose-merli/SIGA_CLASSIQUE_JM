
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;


/**
 * Bean de administracion de la tabla SCS_CABECERAGUARDIAS
 * 
 * @author cristina.santos
 * @since 14/03/2006
 */

public class ScsCabeceraGuardiasAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor del bean de administracion de la tabla.
	 * @param Integer usuario: usuario.
	 */
	public ScsCabeceraGuardiasAdm (UsrBean usuario) {
		super( ScsCabeceraGuardiasBean.T_NOMBRETABLA, usuario);
	}
	
	protected String[] getCamposBean() {
		String[] campos = {	
				ScsCabeceraGuardiasBean.C_IDINSTITUCION,			
				ScsCabeceraGuardiasBean.C_IDTURNO,			
				ScsCabeceraGuardiasBean.C_IDGUARDIA,	
				ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS,		
				ScsCabeceraGuardiasBean.C_IDPERSONA,
				ScsCabeceraGuardiasBean.C_FECHA_INICIO	,		
				ScsCabeceraGuardiasBean.C_FECHA_FIN	,	
				ScsCabeceraGuardiasBean.C_SUSTITUTO	,
				ScsCabeceraGuardiasBean.C_FACTURADO,	
				ScsCabeceraGuardiasBean.C_PAGADO,
				ScsCabeceraGuardiasBean.C_VALIDADO,
				ScsCabeceraGuardiasBean.C_FECHAMODIFICACION	,		
				ScsCabeceraGuardiasBean.C_USUMODIFICACION,
				ScsCabeceraGuardiasBean.C_LETRADOSUSTITUIDO,	
				ScsCabeceraGuardiasBean.C_FECHASUSTITUCION,
				ScsCabeceraGuardiasBean.C_COMENSUSTITUCION,
				ScsCabeceraGuardiasBean.C_FECHAALTA,
				ScsCabeceraGuardiasBean.C_FECHAVALIDACION,
				ScsCabeceraGuardiasBean.C_USUALTA,
				ScsCabeceraGuardiasBean.C_POSICION,
				ScsCabeceraGuardiasBean.C_NUMEROGRUPO,
				ScsCabeceraGuardiasBean.C_OBSERVACIONESANULACION
		};
		return campos;
	}
	
	protected String[] getClavesBean() {
		String[] campos = {	
				ScsCabeceraGuardiasBean.C_IDINSTITUCION, 
				ScsCabeceraGuardiasBean.C_IDTURNO,
				ScsCabeceraGuardiasBean.C_IDGUARDIA, 
				ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS, 
				ScsCabeceraGuardiasBean.C_IDPERSONA, 
				ScsCabeceraGuardiasBean.C_FECHA_INICIO};
		return campos;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsCabeceraGuardiasBean bean = null;
		try{
			bean = new ScsCabeceraGuardiasBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsCabeceraGuardiasBean.C_IDINSTITUCION));			
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsCabeceraGuardiasBean.C_IDTURNO));			
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsCabeceraGuardiasBean.C_IDGUARDIA));			
			bean.setIdCalendario(UtilidadesHash.getInteger(hash,ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS));			
			bean.setIdPersona(UtilidadesHash.getLong(hash,ScsCabeceraGuardiasBean.C_IDPERSONA));			
			bean.setFechaInicio(UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FECHA_INICIO));
			bean.setFechaFin(UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FECHA_FIN));
			bean.setSustituto(UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_SUSTITUTO));
			bean.setFacturado(UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FACTURADO));
			bean.setPagado(UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_PAGADO));
			bean.setValidado(UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_VALIDADO));
			bean.setFechaSustitucion	(UtilidadesHash.getString (hash, ScsCabeceraGuardiasBean.C_FECHASUSTITUCION));
			bean.setComenSustitucion 	(UtilidadesHash.getString (hash, ScsCabeceraGuardiasBean.C_COMENSUSTITUCION));
			bean.setLetradoSustituido	(UtilidadesHash.getLong(hash, ScsCabeceraGuardiasBean.C_LETRADOSUSTITUIDO));
			bean.setFechaAlta(UtilidadesHash.getString (hash, ScsCabeceraGuardiasBean.C_FECHAALTA));
			bean.setFechaValidacion(UtilidadesHash.getString (hash, ScsCabeceraGuardiasBean.C_FECHAVALIDACION));
			bean.setUsuAlta(UtilidadesHash.getInteger (hash, ScsCabeceraGuardiasBean.C_USUALTA));
			bean.setPosicion(UtilidadesHash.getInteger(hash, ScsCabeceraGuardiasBean.C_POSICION));
			bean.setNumeroGrupo(UtilidadesHash.getInteger(hash, ScsCabeceraGuardiasBean.C_NUMEROGRUPO));
			bean.setObservacionesAnulacion(UtilidadesHash.getString(hash, ScsCabeceraGuardiasBean.C_OBSERVACIONESANULACION));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;	
	}
	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsCabeceraGuardiasBean b = (ScsCabeceraGuardiasBean) bean;
			hash.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsCabeceraGuardiasBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsCabeceraGuardiasBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS, String.valueOf(b.getIdCalendario()));
			hash.put(ScsCabeceraGuardiasBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			
			hash.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO, b.getFechaInicio());
			hash.put(ScsCabeceraGuardiasBean.C_FECHA_FIN, b.getFechaFin());
			hash.put(ScsCabeceraGuardiasBean.C_SUSTITUTO, b.getSustituto());
			hash.put(ScsCabeceraGuardiasBean.C_FACTURADO, b.getFacturado());
			hash.put(ScsCabeceraGuardiasBean.C_PAGADO, b.getPagado());
			hash.put(ScsCabeceraGuardiasBean.C_VALIDADO, b.getValidado());
			
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_LETRADOSUSTITUIDO, b.getLetradoSustituido());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_FECHASUSTITUCION, b.getFechaSustitucion());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_COMENSUSTITUCION, b.getComenSustitucion());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_FECHAALTA, b.getFechaAlta());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_FECHAVALIDACION, b.getFechaValidacion());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_USUALTA, b.getUsuAlta());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_POSICION, b.getPosicion());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_NUMEROGRUPO, b.getNumeroGrupo());
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_FECHAMODIFICACION, "SYSDATE");
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_USUMODIFICACION, usuModificacion);
			UtilidadesHash.set(hash, ScsCabeceraGuardiasBean.C_OBSERVACIONESANULACION, b.getObservacionesAnulacion());
			
		} catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	
	protected String[] getOrdenCampos() {
		return null;
	}
	
	private String[] getOrderSelectBuscarCabeceraGuardias(){
		return null;
	}
		
	/**
	 * Ejecuta un delete por 4 campos ID: IDINSTITUCION,IDTURNO,IDGUARDIA,IDCALENDARIOGUARDIAS
	 * @param Hashtable hash: tabla hash con los campos: 
	 * - String idinstitucion
	 * - String idcalendarioguardias
	 * - String idturno
	 * - String idguardia  
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deleteCabecerasGuardiasCalendario(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();
		
		try {
			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			

			sql.append(" delete from "+ScsCabeceraGuardiasBean.T_NOMBRETABLA);
			sql.append(" where "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia);
			deleteSQL(sql.toString());
//			ClsMngBBDD.executeUpdate(sql.toString());
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
	public String buscarOtrosColegiados(Hashtable miHash, boolean guardiasPasadas) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="", idpersona="";
		
		try {
			idinstitucion = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idguardia = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idturno = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idcalendarioguardias = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idpersona = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDPERSONA);

			consulta = "SELECT ";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+",";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN+",";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+",";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDTURNO+",";
 			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+",";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+",";			
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,";
			//consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO+",";
			//Esto esta mal ya que no tiene en cuenata que si es colegiado comunitario el numero de 
			//colegiado viene en otro campo. Se deberia añadir esta linea DEBAJO  
			//y eliminar toda referencia a CEN_COLEGIADO
			consulta += " F_SIGA_CALCULONCOLEGIADO(guard."+CenColegiadoBean.C_IDINSTITUCION+","+"guard."+CenColegiadoBean.C_IDPERSONA+") as "+CenColegiadoBean.C_NCOLEGIADO+", ";
			
			//FUNCION DE PERMUTAS: calculo su valor 
			consulta += " F_SIGA_NUMEROPERMUTAGUARDIAS(";
			consulta +=       " guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+", guard."+ScsCabeceraGuardiasBean.C_IDTURNO+", guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+",";
			consulta += 	  " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+", guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO;
			consulta+= 		  ") AS FUNCIONPERMUTAS";
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso";
			//consulta += ","+CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";			
			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"<>'"+idpersona+"'";			
			//JOIN
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			//PDM se anyade esta restriccion para que no se muestren colegiados con fecha de inicio de guardia anterior
			// al dia actual
			if (!guardiasPasadas) {
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" >= sysdate";
			}
			// PDM solo mostramos aquellas personas con las que no se hayan hecho niguna permuta.
			consulta += " AND F_SIGA_NUMEROPERMUTAGUARDIAS(";
			consulta +=       " guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+", guard."+ScsCabeceraGuardiasBean.C_IDTURNO+", guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+",";
			consulta += 	  " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+", guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO;
			if (!guardiasPasadas) {
			    consulta+= 		  ") IN (3,5)";
			} else {
			    consulta+= 		  ") IN (1,5)";
			}
			//ORDEN
			consulta += " ORDER BY guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.buscarOtrosColegiados(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
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
			idinstitucion = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idguardia = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idturno = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idcalendarioguardias = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idpersona = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDPERSONA);

			consulta = "SELECT guard.* ,";
			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,";
			consulta += " F_SIGA_CALCULONCOLEGIADO(guard.IDINSTITUCION, guard.IDPERSONA) as "+CenColegiadoBean.C_NCOLEGIADO;
			//consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO;
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso ";
			//consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idpersona;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
			//JOIN
			//consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//ORDEN
			consulta += " ORDER BY guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getDatosColegiado(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	
	
	/**
	 * Ejecuta un delete por 4 campos ID: IDINSTITUCION,IDTURNO,IDGUARDIA,IDCALENDARIOGUARDIAS
	 * @param Hashtable hash: tabla hash con los campos: 
	 * - String idinstitucion
	 * - String idcalendarioguardias
	 * - String idturno
	 * - String idguardia  
	 * - String idPersona
	 * @return boolean: true si ha ido todo bien.
	 * @throws ClsExceptions
	 */
	public boolean deleteCabeceraGuardiasCalendario(Hashtable hash) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaInicio="", idPersona="";
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

			sql.append(" delete from "+ScsCabeceraGuardiasBean.T_NOMBRETABLA);
			sql.append(" where "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idPersona);
			sql.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+")=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
			
			ClsMngBBDD.executeUpdate(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}	

	public boolean deleteCabeceraGuardias(Hashtable hashCabecera) throws ClsExceptions {
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaInicio="", idPersona="";
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

			sql.append(" delete from "+ScsCabeceraGuardiasBean.T_NOMBRETABLA);
			sql.append(" where "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia);
			sql.append(" and "+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idPersona);
			sql.append(" and trunc("+ScsGuardiasColegiadoBean.C_FECHAINICIO+")=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
			
			ClsMngBBDD.executeUpdate(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}	
	
	//Comprueba si puede borrar la cabecera y las guardias colegiado al borrar la asistencia
	public boolean esGuardiaPresencialEliminable(Hashtable hashCabecera) throws ClsExceptions{
		boolean ok = false;
		
		try {
			String sustituto = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_SUSTITUTO);
			
			//Obtenter si esta facturada la cabecera Guardia (CabeceraGuardia.estaFacturada)
			boolean estaFacturada = this.estaFacturada(hashCabecera);
				
			//Obtener Num Asistencias de la CabeceraGuardia (cabeceraGuardia.getNumAsistencias)
			int numAsistencias = this.getNumAsistencias(hashCabecera);
				
			//Si cabeceraGuardia.sustituto = 1 Y cabeceraGuardia.getNumAsistencias == 0 Y NO cabeceraGuardia o.estaFacturada
	        if (sustituto!=null && sustituto.equals("1") && numAsistencias==0 && !estaFacturada)
	        	ok = true;
		} catch(Exception e) {
			throw new ClsExceptions(e,"Excepcion en esGuardiaPresencialEliminable()");
		}
		return ok;
	}
	
	//Obtiene el numero de asistencias para la cabecera
	private int getNumAsistencias(Hashtable hashCabecera) throws ClsExceptions{
		int num = 0;
	
		String consulta = "";
		String idinstitucion="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		
		try {
			idinstitucion = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idturno = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idguardia = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idPersona = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
			//Fechas del periodo:
			fechaInicio = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
			fechaFin = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_FIN), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
			
			consulta = "SELECT count(*) ";
			consulta += " FROM "+ScsAsistenciasBean.T_NOMBRETABLA;
			consulta += " WHERE ";			
			consulta += ScsAsistenciasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND "+ScsAsistenciasBean.C_IDTURNO+"="+idturno;
			consulta += " AND "+ScsAsistenciasBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND "+ScsAsistenciasBean.C_IDPERSONACOLEGIADO+"='"+idPersona+"'";
			consulta += " and trunc("+ScsAsistenciasBean.C_FECHAHORA+") between TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			consulta += " and TO_DATE('"+fechaFin+"','DD/MM/YYYY')";

			Vector v = this.selectGenerico(consulta);
			if (v!=null && !v.isEmpty()) {
				Hashtable hash = (Hashtable)v.firstElement();
				String snum = (String)hash.get("NUM");
				if (snum!=null && !snum.equals(""))
					num = Integer.parseInt(snum);
			}
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.buscarOtrosColegiados(). Consulta SQL:"+consulta);
		}
		return num;
	}
	
	//Comprueba si esta facturada la cabecera
	private boolean estaFacturada(Hashtable hashCabecera) throws ClsExceptions {
		boolean ok = false;
		
		String consulta = "";
		String idinstitucion="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		
		try {
			idinstitucion = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idturno = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idguardia = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idPersona = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
			//Fechas del periodo:
			fechaInicio = UtilidadesString.formatoFecha((String)hashCabecera.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
						
			consulta = "SELECT count(1) AS TOTAL ";
			consulta += " FROM "+FcsFactApunteBean.T_NOMBRETABLA;
			consulta += " WHERE ";			
			consulta += FcsFactApunteBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND "+FcsFactApunteBean.C_IDTURNO+"="+idturno;
			consulta += " AND "+FcsFactApunteBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND "+FcsFactApunteBean.C_IDPERSONA+"='"+idPersona+"'";
			consulta += " AND trunc("+FcsFactApunteBean.C_FECHAINICIO+")=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";

			Vector v = this.selectGenerico(consulta);
			if (v!=null && !v.isEmpty()) {
				String total = (String)((Hashtable)v.firstElement()).get("TOTAL");
				if (total.equals("1"))
					ok = true;
				else
					ok = false;
			}
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.estaFacturada(). Consulta SQL:"+consulta);
		}
		return ok;
	}
	
	
	public void actualizarValidacionCabecera(String idInstitucion,String anio, String numero,  boolean actValidada) throws ClsExceptions {
		String consulta = "";
		
		try {
			GenParametrosAdm admPar = new GenParametrosAdm(this.usrbean);
			String param = admPar.getValor(this.usrbean.getLocation(), "SCS","VALIDAR_VOLANTE","N");
			if (param.equals("S")) {
				validacionCabeceraAsistencia(new Integer(idInstitucion), new Integer(anio), new Integer(numero), actValidada);
			}

		}
		catch (Exception e){
			throw new ClsExceptions(e,"Error al actualizar la validacion de cabecera de guardias");
		}
		
	}
	public void validacionCabeceraAsistencia(Integer idInstitucion,Integer anio, Integer numero,  boolean actValidada) throws ClsExceptions {
		String consulta = "";
		
		try {
			
			    // solamente actualizamos la validacion de la cabecera cuando el paraemtro dice que es necesario validarlas
			    // buscamos la cabecera de la actuacion
			    consulta = " SELECT CG.*, F_SIGA_TIENE_ACTS_VALIDADAS(CG.IDINSTITUCION,CG.IDTURNO,CG.IDGUARDIA,CG.IDCALENDARIOGUARDIAS,CG.IDPERSONA,CG.FECHAINICIO) AS ACTUACIONESVALIDADAS " +
			        " FROM   SCS_CABECERAGUARDIAS CG, " +
			        "         SCS_GUARDIASCOLEGIADO UG, " +
			        "         SCS_ASISTENCIA ASI " +
			        " WHERE  CG.IDINSTITUCION = UG.IDINSTITUCION " +
			        " AND    CG.IDTURNO = UG.IDTURNO " +
			        " AND    CG.IDGUARDIA = UG.IDGUARDIA " +
			        " AND    CG.IDPERSONA = UG.IDPERSONA " +
			        " AND    CG.FECHAINICIO = UG.FECHAINICIO " +
			        " AND    UG.IDINSTITUCION = ASI.IDINSTITUCION " +
			        " AND    UG.IDTURNO = ASI.IDTURNO " +
			        " AND    UG.IDGUARDIA = ASI.IDGUARDIA " +
			        " AND    UG.IDPERSONA = ASI.IDPERSONACOLEGIADO " +
			        " AND    trunc(ASI.FECHAHORA) BETWEEN trunc(UG.FECHAINICIO) AND trunc(UG.FECHAFIN) " +
			        " AND    ASI.IDINSTITUCION = " + idInstitucion +
			        " AND    ASI.ANIO = " + anio +
			        " AND    ASI.NUMERO = " + numero;
			    
				String claves[] = {"IDINSTITUCION","IDTURNO","IDGUARDIA","IDPERSONA","FECHAINICIO"};
				String campos[] = {"VALIDADO"};
				
				Vector v = this.selectGenerico(consulta);
				Hashtable cabecera = new Hashtable();
				if (v!=null && !v.isEmpty()) {
					cabecera = (Hashtable)v.firstElement();
				}
				
			    if (actValidada) {
			        // hay que validarla este como este la cabecera
			        cabecera.put("VALIDADO",ClsConstants.DB_TRUE);
			        this.updateDirect(cabecera,claves,campos);
			    } else {
			        // hay que invalidarla si no tiene mas actuaciones validadas
			    	if (cabecera!=null && cabecera.size()!=0){
				        int numeroActsValidadas = (new Integer((String)cabecera.get("ACTUACIONESVALIDADAS")).intValue())-1; // le quito uno porque no quiero contar esta misma actuacion
				        if (numeroActsValidadas<1) {
					        cabecera.put("VALIDADO",ClsConstants.DB_FALSE);
					        this.updateDirect(cabecera,claves,campos);
				        }
			    	}
			    }
			

		}
		catch (Exception e){
			throw new ClsExceptions(e,"Error validar la cabecera de guardias");
		}
		
	}
	
	
	/** 
	 * Devuelve un registro con los datos del colegiado de guardia o de reserva.
	 * 
	 * @param Hashtable miHash: identificadores de busqueda de la tabla SCS_GUARDIASCOLEGIADO
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getDatosColegiadoFormateadoNombre(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="", idpersona="";
		
		try {
			idinstitucion = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idguardia = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idturno = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idcalendarioguardias = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idpersona = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDPERSONA);

			consulta = "SELECT guard.* ,";
			consulta += " perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" || ', ' || perso."+CenPersonaBean.C_NOMBRE+" NOMBRE,";
			consulta += " F_SIGA_CALCULONCOLEGIADO(guard.IDINSTITUCION, guard.IDPERSONA) as "+CenColegiadoBean.C_NCOLEGIADO;
			//consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO;
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso ";
			//consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			consulta += " WHERE ";
			consulta += " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idpersona;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia;
			consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
			//JOIN
			//consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			//ORDEN
			consulta += " ORDER BY guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getDatosColegiado(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	// Incluyo en los datos del sustituto, los de la anulacion para aprovechar la sentencia que el igual pero con un campo mas devuelto
	public String getDatosSustituto(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="", idpersona="";
		String fechainicio="";
		
		try {
			idinstitucion = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idguardia = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idturno = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idcalendarioguardias = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
			idpersona = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
			fechainicio=(String)miHash.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
			consulta ="SELECT C.COMENSUSTITUCION as COMENSUSTITUCION, " +
							" trunc(C.FECHASUSTITUCION) as FECHASUSTITUCION, " +
							" C.LETRADOSUSTITUIDO as LETRADOSUSTITUIDO, " +
							" C.OBSERVACIONESANULACION AS OBSERVACIONESANULACION " +
						" FROM SCS_CABECERAGUARDIAS C" +
						" WHERE IDPERSONA = "+idpersona+
							" AND FECHAINICIO = TO_DATE('" + fechainicio + "', 'DD/MM/YYYY')" +
							" AND IDGUARDIA = " + idguardia +
							" AND IDTURNO = " + idturno +
							" AND IDINSTITUCION = " + idinstitucion +
							" AND IDCALENDARIOGUARDIAS = " + idcalendarioguardias +
						" ORDER BY IDINSTITUCION, " +
							" IDTURNO, " +
							" IDGUARDIA, " +
							" IDCALENDARIOGUARDIAS, " +
							" IDPERSONA, " +
							" FECHAINICIO";
			
			} catch (Exception e){
				throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getDatosColegiado(). Consulta SQL:"+consulta);
			}
	
			return consulta;
		}
	
	  public String getnombresustituto(String sustituto,String idIstititucion) throws ClsExceptions{
		  
		  String consulta="";
		  try{
		   consulta="select p.apellidos1||' '||p.apellidos2||', '|| p.nombre as NOMBRE, decode(c.comunitario,'1',c.ncomunitario,c.ncolegiado) as NCOLEGIADOSUSTITUTO from cen_persona p, cen_colegiado c where p.idpersona='"+sustituto+"'";
		   
			 consulta+= "and p.idpersona=c.idpersona and c.idinstitucion="+idIstititucion;	
		  }
		  catch (Exception e){
			  throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getDatosColegiado(). Consulta SQL:"+consulta);
			}
	
			return consulta;
			  
		  }
	
	  
	  public String getRangopermutas(Hashtable miHash) throws ClsExceptions{
			String consulta = "";
			String idinstitucion="", idguardia="", idturno="", fechainicio="", idcalendarioguardias="", reserva="", idpersona="";
			
			try {
				idinstitucion = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
				idguardia = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
				idturno = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
				idcalendarioguardias = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
				idpersona = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
				fechainicio= (String)miHash.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
				consulta = "SELECT guard."+ ScsCabeceraGuardiasBean.C_FECHA_INICIO+","+ ScsCabeceraGuardiasBean.C_FECHA_FIN;
				consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard";					
				consulta += " WHERE ";
				consulta += " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idpersona;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+"= TO_DATE('"+fechainicio+"','DD/MM/YYYY')";
				

				
			}
			catch (Exception e){
				throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getDatosColegiado(). Consulta SQL:"+consulta);
			}
			
			return consulta;
		}

	

	
	

	  public Integer getPosicion(String idinstitucion,String idguardia,String idturno,String fechainicio,String idpersona) throws ClsExceptions{
			String consulta = "";
			Integer posicion = null;
		
			try {

				consulta = "SELECT guard."+ ScsCabeceraGuardiasBean.C_POSICION;
				consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";					
				consulta += " (  select fechainicio"; 
				consulta += " from scs_guardiascolegiado ";
				consulta += "  where idinstitucion = '"+idinstitucion;
				consulta += "' and idturno = '"+idturno;
				consulta += "' and idguardia = '"+idguardia;
				consulta += "' and idpersona = '"+idpersona;
				consulta += "' and fechafin = TO_DATE('"+fechainicio+"', 'DD/MM/YYYY')) g  ";
				consulta += " WHERE ";
				consulta += " guard."+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idpersona;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia;
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" = g.fechainicio";
				
				Vector v = this.selectGenerico(consulta);
				Hashtable cabecera = new Hashtable();
				if (v!=null && !v.isEmpty()) {
					cabecera = (Hashtable)v.firstElement();
					posicion =(Integer) cabecera.get(0);
				}
			}
			catch (Exception e){
				throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getDatosColegiado(). Consulta SQL:"+consulta);
			}
			
			return posicion;
		}



	

	
	@Override
	public boolean update(MasterBean bean) throws ClsExceptions {
		// TODO Auto-generated method stub
		return super.update(bean);
	}

	public Integer getMaximaPosicionCabecera(Integer idInstitucion, Integer idTurno, Integer idGuardia, Integer idCalendario,String fecha) throws ClsExceptions{
		Integer posicion = null;
		try {
			Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
			int contador = 0;	
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append("SELECT MAX(POSICION) POSICION FROM SCS_CABECERAGUARDIAS CG ");
			sql.append("WHERE CG.IDTURNO = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idTurno);
			sql.append("AND CG.IDGUARDIA = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idGuardia);
			sql.append("AND CG.IDINSTITUCION = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idInstitucion);
			sql.append("AND CG.IDCALENDARIOGUARDIAS = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idCalendario);
			sql.append("AND TO_DATE(:");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),fecha);
			sql.append(") BETWEEN TO_DATE(TRUNC(CG.FECHAINICIO)) AND TO_DATE(TRUNC(CG.FECHA_FIN)) ");

			Vector v = this.selectGenericoBind(sql.toString(),htCodigos);
			if (v!=null && !v.isEmpty()) {
				Hashtable hash = (Hashtable)v.firstElement();
				String pos = (String)hash.get("POSICION");
				if (pos!=null && !pos.equals(""))
					posicion = Integer.parseInt(pos);
			}
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.getMaximaPoasicionCabecera().");
		}
		return posicion;
	}

	
	public boolean validaGuardiaLetradoPeriodo(Integer idInstitucion, Integer idTurno, Integer idGuardia, Long idPersona, String fechaInicio, String fechaFin) throws ClsExceptions{
		
		boolean existeGuardiaLetrado=false;	
		String consulta = "";
		
		try {
			consulta = "SELECT NVL(COUNT(*),0) EXISTEGUARDIA ";
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA ;					
			consulta += " WHERE ";
			consulta += ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idPersona;
			consulta += " AND "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idInstitucion;
			consulta += " AND "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idTurno;
			consulta += " AND "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idGuardia;
			consulta += " AND TRUNC( "+ScsCabeceraGuardiasBean.C_FECHA_INICIO+") >= '"+fechaInicio+"'";
			consulta += " AND TRUNC( "+ScsCabeceraGuardiasBean.C_FECHA_FIN+") <= '"+fechaFin+"'";
			
			
			Vector v = this.selectGenerico(consulta);
			Hashtable cabecera = new Hashtable();
			if (v!=null && !v.isEmpty()) {
				cabecera = (Hashtable)v.firstElement();
				
				if(cabecera.get("EXISTEGUARDIA").equals("0"))
					existeGuardiaLetrado=false;
				else
					existeGuardiaLetrado=true;
			}
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCabeceraGuardiasAdm.validaGuardiaLetradoPeriodo().");
		}
		return existeGuardiaLetrado;
	}

	public String buscarColegiados(Hashtable miHash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idguardia="", idturno="", idcalendarioguardias="", reserva="";
		
		try {
			idinstitucion = (String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idguardia = (miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA)==null || miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA).equals(""))?"guard.IDGUARDIA":(String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			idturno = (miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO)==null || miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO).equals(""))?"guard.IDTURNO":(String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			idcalendarioguardias = (miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS)==null || miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS).equals(""))?"guard.IDCALENDARIOGUARDIAS":(String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);

			consulta = " SELECT guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta+= " ,guard.rowid AS ROWIND";
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_VALIDADO;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_FECHAVALIDACION;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDTURNO;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_POSICION;
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_NUMEROGRUPO;
			consulta+= " ,(SELECT T.NOMBRE FROM SCS_TURNO T WHERE T.IDINSTITUCION=guard.IDINSTITUCION AND  T.IDTURNO=guard.IDTURNO) AS NOMTURNO";
			consulta+= " ,(SELECT G.NOMBRE FROM SCS_GUARDIASTURNO G WHERE G.IDINSTITUCION=guard.IDINSTITUCION AND  G.IDTURNO=guard.IDTURNO AND  G.IDGUARDIA=guard.IDGUARDIA) AS NOMGUARDIA";
			consulta+= " ,guard."+ScsCabeceraGuardiasBean.C_FECHA_INICIO+" as FECHA_INICIO_PK,guard."+ScsCabeceraGuardiasBean.C_FECHA_FIN;    
			consulta += " ," + " perso." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || perso." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || perso." + CenPersonaBean.C_NOMBRE + " NOMBRE";
			consulta += " ,guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS;
			consulta += " FROM "+ScsCabeceraGuardiasBean.T_NOMBRETABLA+" guard,";
			consulta += CenPersonaBean.T_NOMBRETABLA+" perso,";
			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg";
			
			consulta += " WHERE coleg."+CenColegiadoBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=guard."+ScsCabeceraGuardiasBean.C_IDPERSONA;
			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=coleg."+CenColegiadoBean.C_IDPERSONA;
			   
			if (miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION)!=null) 
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+(String)miHash.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			if (miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO)!=null   && !((String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO)).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDTURNO+"="+(String)miHash.get(ScsCabeceraGuardiasBean.C_IDTURNO);
			if (miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA)!=null  && !((String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA)).trim().equals(""))
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+(String)miHash.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
			if (miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS)!=null)
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+(String)miHash.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);

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
			    consulta += " AND guard."+ScsCabeceraGuardiasBean.C_VALIDADO+"='"+(String)miHash.get("PENDIENTEVALIDAR")+"'";

			//ORDEN
			consulta += " ORDER BY FECHA_INICIO_PK, POSICION"; 
			// RGG esta tonteria del rowid es muy importante para el orden dentro de una misma guardia
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsGuardiasColegiadoAdm.buscarColegiados(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}

	
	//Comprueba antes de borrar un CALENDARIO de guardias que no exista ninguna guardia realizada. 
	public boolean validarBorradoGuardias(Integer idInstitucion, Integer idCalendarioGuardias, Integer idTurno, Integer idGuardia) {
		boolean correcto = false;
		StringBuffer consulta = new StringBuffer();
		
		consulta.append("select count(*) AS TOTAL from "+ScsCabeceraGuardiasBean.T_NOMBRETABLA);
		consulta.append(" where "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idInstitucion);
		consulta.append(" and "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idTurno);
		consulta.append(" and "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idGuardia);
		consulta.append(" and "+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idCalendarioGuardias);
		consulta.append(" and trunc("+ScsCabeceraGuardiasBean.C_FECHA_FIN+") < trunc(sysdate)");

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
	
	public void aplicarSustitucion(Integer idInstitucion, Integer idTurno, 
			Integer idGuardia, Long idPersonaSaliente, Long idPersonaEntrante, 
			String fechaFin, UsrBean usr) throws SIGAException,ClsExceptions
			{

		Vector vGuardias;
		Hashtable hash = new Hashtable();
		ScsGuardiasColegiadoAdm guarcoladm = new ScsGuardiasColegiadoAdm(this.usrbean);
		fechaFin = GstDate.getApplicationFormatDate("", fechaFin);

		// Obtenemos la cabecera
		hash = new Hashtable();
		UtilidadesHash.set (hash, ScsGuardiasColegiadoBean.C_IDPERSONA, idPersonaSaliente);
		UtilidadesHash.set (hash, ScsGuardiasColegiadoBean.C_FECHAFIN, fechaFin);
		UtilidadesHash.set (hash, ScsGuardiasColegiadoBean.C_IDTURNO, idTurno);
		UtilidadesHash.set (hash, ScsGuardiasColegiadoBean.C_IDGUARDIA, idGuardia);
		UtilidadesHash.set (hash, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
		vGuardias = guarcoladm.select(hash);
		if (vGuardias != null && vGuardias.size() != 1) {
			throw new SIGAException("gratuita.volantesExpres.error.guardiaConVariosCalendarios");
		}
		ScsGuardiasColegiadoBean guardiaBean = (ScsGuardiasColegiadoBean) vGuardias.get(0);
		String fechaInicio = GstDate.getFormatedDateLong("", guardiaBean.getFechaInicio()); 
		
		// Obtenemos el idCalendario
		hash = new Hashtable();
		UtilidadesHash.set (hash, ScsCabeceraGuardiasBean.C_IDPERSONA, idPersonaSaliente);
		UtilidadesHash.set (hash, ScsCabeceraGuardiasBean.C_FECHA_INICIO, fechaInicio);
		UtilidadesHash.set (hash, ScsCabeceraGuardiasBean.C_IDTURNO, idTurno);
		UtilidadesHash.set (hash, ScsCabeceraGuardiasBean.C_IDGUARDIA, idGuardia);
		UtilidadesHash.set (hash, ScsCabeceraGuardiasBean.C_IDINSTITUCION, idInstitucion);
		vGuardias = this.select(hash);
		if (vGuardias != null && vGuardias.size() != 1) {
			throw new SIGAException("gratuita.volantesExpres.error.guardiaConVariosCalendarios");
		}
		ScsCabeceraGuardiasBean cabBean = (ScsCabeceraGuardiasBean) vGuardias.get(0);
		String idCalendarioGuardias = ""+cabBean.getIdCalendario();
		
		
		String salto = null; 			// No creamos salto
		String compensacion = null; 	// No creamos compensacion
		validarColegiadoEntrante(usr,idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), fechaInicio,fechaFin,idPersonaEntrante.toString());
		guarcoladm.sustitucionLetradoGuardiaPuntual(usr, null, idInstitucion.toString(), idTurno.toString(),idGuardia.toString(),idCalendarioGuardias,idPersonaSaliente.toString(),fechaInicio,fechaFin,idPersonaEntrante.toString(), salto, compensacion,"si","");
	}
	public void validarColegiadoEntrante(UsrBean usr, String idInstitucion, String idTurno, String idGuardia, String fechaInicio,String fechaFin,String idPersonaEntrante) throws SIGAException,ClsExceptions
	{
		Hashtable clavesGuardiaColegiado = new Hashtable();
		
		clavesGuardiaColegiado.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,idInstitucion);
		clavesGuardiaColegiado.put(ScsCabeceraGuardiasBean.C_IDTURNO,idTurno);
		clavesGuardiaColegiado.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,idGuardia);
		clavesGuardiaColegiado.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idPersonaEntrante);
		clavesGuardiaColegiado.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),fechaInicio));
		
		Vector v = this.select(clavesGuardiaColegiado);
		if(v != null && v.size() > 0)
		{
			throw new SIGAException("gratuita.volantesExpres.mensaje.letradoSustituyeConGuardia");
		}
		
	}


}