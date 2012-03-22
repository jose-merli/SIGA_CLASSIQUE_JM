
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;


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
				ScsCabeceraGuardiasBean.C_NUMEROGRUPO
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
			
			
		}
		catch (Exception e){
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
			//(@FEMI)Permitimos que se pueda permutar por calendarios diferentes al inicial
			//consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
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
			    consulta+= 		  ")=5";
			} else {
			    consulta+= 		  ") in (1,5)";
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
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		
		try {
			idinstitucion = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
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
		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaInicio="", fechaFin="", idPersona="";
		
		try {
			idinstitucion = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDINSTITUCION);
			idcalendarioguardias = (String)hashCabecera.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
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
			consulta += " AND "+FcsFactApunteBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias;
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
			        " AND    CG.IDCALENDARIOGUARDIAS = UG.IDCALENDARIOGUARDIAS " +
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
			consulta ="SELECT C.COMENSUSTITUCION as COMENSUSTITUCION,trunc(C.FECHASUSTITUCION) as FECHASUSTITUCION,C.LETRADOSUSTITUIDO as LETRADOSUSTITUIDO FROM SCS_CABECERAGUARDIAS C" +
					  " WHERE IDPERSONA = "+idpersona+
					  "   AND FECHAINICIO =" +
					  "       TO_DATE('"+fechainicio+"', 'DD/MM/YYYY')" +
					  "   AND IDGUARDIA = "+idguardia+
					  "   AND IDTURNO = "+idturno+
					  "   AND IDINSTITUCION ="+idinstitucion+
					  "   and IDCALENDARIOGUARDIAS="+idcalendarioguardias+
					  " ORDER BY IDINSTITUCION," +
					  "          IDTURNO," +
					  "          IDGUARDIA," +
					  "          IDCALENDARIOGUARDIAS," +
					  "          IDPERSONA," +
					  "          FECHAINICIO";
			}
			catch (Exception e){
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
				consulta += " (  select fechainicio, idcalendarioguardias"; 
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
				consulta += " AND guard."+ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS+" = g.idcalendarioguardias";
				
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

	
	

	
	
	
}