
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;

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
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
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
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
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
		return null;
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
	public String getIdCalendarioGuardias(String idinstitucion_pestanha, String idturno_pestanha, String idguardia_pestanha) throws ClsExceptions{
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

	public boolean validarBorradoCalendario(String idInstitucion,
			String idCalendarioGuardias,
			String idTurno,
			String idGuardia)
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

	public Integer getIdCalendarioPorFecha(String idInstitucion,
			String idTurno,
			String idGuardia,
			String fecha)
	{
		StringBuffer where = new StringBuffer();
		where.append(" Where Idinstitucion = " + idInstitucion + " ");
		where.append("   And Idturno = " + idTurno + " ");
		where.append("   And Idguardia = " + idGuardia + " ");
		where.append("   And '" + fecha + "' Between Fechainicio And Fechafin ");

		try {
			Vector resultado = this.select(where.toString());
			if (resultado != null && resultado.size() > 0)
				return ((ScsCalendarioGuardiasBean) resultado.get(0)).getIdCalendarioGuardias();
			else
				return null;
		} catch (ClsExceptions e) {
			return null;
		}
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
	public String getDatosCalendarioVinculadas(String idinstitucion_pestanha, String idturno_pestanha, String idguardia_pestanha, String idcalendario) throws ClsExceptions{
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
	
}