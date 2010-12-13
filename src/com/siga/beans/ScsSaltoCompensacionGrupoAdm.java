
package com.siga.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import sun.text.CompactShortArray.Iterator;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.EjecucionPLs;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_SALTOCOMPENSACIONGRUPO
 * @since 1/11/2004 
 */
public class ScsSaltoCompensacionGrupoAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsSaltoCompensacionGrupoAdm(UsrBean usuario)
	{
		super(ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA, usuario);
	}

	/** 
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean()
	{
		String[] campos =
		{
				ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO,
				ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA,
				ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION,
				ScsSaltoCompensacionGrupoBean.C_FECHA,
				ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO,
				ScsSaltoCompensacionGrupoBean.C_MOTIVO,
				ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO,
				ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,
				ScsSaltoCompensacionGrupoBean.C_IDTURNO,
				ScsSaltoCompensacionGrupoBean.C_IDGUARDIA,
				ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS,
				ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI,
				ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI,
				ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI,
				ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI,
				ScsSaltoCompensacionGrupoBean.C_FECHACREACION,
				ScsSaltoCompensacionGrupoBean.C_USUCREACION,
				ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION,
				ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION
		};
		return campos;
	}

	/**
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean()
	{
		String[] campos = {ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO};
		return campos;
	}
	
	/**
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		ScsSaltoCompensacionGrupoBean bean = null;
		
		try {
			bean = new ScsSaltoCompensacionGrupoBean();
			bean.setIdSaltoCompensacionGrupo(UtilidadesHash.getLong(hash, ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO));
			bean.setIdGrupoGuardia(UtilidadesHash.getLong(hash, ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA));
			bean.setSaltoCompensacion(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION));
			bean.setFecha(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_FECHA));
			bean.setFechaCumplimiento(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO));
			bean.setMotivo(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_MOTIVO));
			bean.setMotivoCumplimiento(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDTURNO));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDGUARDIA));
			bean.setIdCalendarioGuardias(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS));
			bean.setIdInstitucion_Cumpli(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI));
			bean.setIdTurno_Cumpli(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI));
			bean.setIdGuardia_Cumpli(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI));
			bean.setIdCalendarioGuardias_Cumpli(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI));
			
			bean.setFechaCreacion(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_FECHACREACION));
			bean.setUsuCreacion(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_USUCREACION));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION));
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/**
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable hash = null;
		
		try {
			hash = new Hashtable();
			ScsSaltoCompensacionGrupoBean b = (ScsSaltoCompensacionGrupoBean) bean;
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, String.valueOf(b.getIdSaltoCompensacionGrupo()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA, String.valueOf(b.getIdGrupoGuardia()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, b.getSaltoCompensacion());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO, b.getFechaCumplimiento());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_MOTIVO, b.getMotivo());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO, b.getMotivoCumplimiento());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS, String.valueOf(b.getIdCalendarioGuardias()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI, String.valueOf(b.getIdInstitucion_Cumpli()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI, String.valueOf(b.getIdTurno_Cumpli()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI, String.valueOf(b.getIdGuardia_Cumpli()));
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI, String.valueOf(b.getIdCalendarioGuardias_Cumpli()));
			
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_FECHACREACION, b.getFechaCreacion());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_USUCREACION, b.getUsuCreacion());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION, b.getUsuMod());
		} catch (Exception e) {
			hash = null;
			throw new ClsExceptions(e, "Error al construir el hashTable a partir del bean");
		}
		
		return hash;
	}
	
	/** 
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		String[] campos = {ScsSaltoCompensacionGrupoBean.C_FECHA};
		return campos;
	}
	
	/**
	 * Inserta en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions
	{
		Vector datos = new Vector();

		// Acceso a BBDD
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					if (registro != null)
						datos.add(registro);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en ScsSaltoCompensacionGrupoAdm.selectGenerico(). Consulta SQL:"
					+ consulta);
		}
		return datos;
	}
	
	/** 
	 * Calcula un nuevo idSaltoCompensacionGrupo para la tabla
	 * 
	 * @return String con el nuevo identificador.
	 * @throws ClsExceptions
	 */	
	public String getNuevoIdSaltoCompensacionGrupo() throws ClsExceptions
	{
		Vector registros;
		String nuevoId = "";

		try {
			registros = this.selectGenerico("select sq_scs_saltocompensaciongrupo.Nextval as ID from dual");
			nuevoId = (String) ((Hashtable) registros.get(0)).get("ID");
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener nuevo idsaltocompensaciongrupo");
		}

		return nuevoId;
	} // getNuevoIdSaltoCompensacionGrupo()

	/**
	 * Obtiene los saltos o las compensaciones pendientes dada una guardia.
	 * Para cada salto o compensacion, anyade al bean los letrados del grupo correspondiente.
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param saltoOcompensacion
	 * @return
	 * @throws ClsExceptions
	 */
	public ArrayList<ScsSaltoCompensacionGrupoBean> getSaltosCompensacionesPendientesGuardia(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String saltoOcompensacion) throws ClsExceptions
	{
		// Variables
		Vector compensaciones;
		ArrayList<ScsSaltoCompensacionGrupoBean> resultado;
		ScsSaltoCompensacionGrupoBean compensacion;

		// obteniendo las compensaciones
		String where = 
			" where "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+" = '"+saltoOcompensacion+"' " +
			"   and "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" is null " +
			"   and "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+" = "+idInstitucion.toString()+" " +
			"   and "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+" = "+idTurno.toString()+" " +
			"   and "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+" = "+idGuardia.toString()+" ";
		compensaciones = this.select(where);

		// obteniendo los letrados de cada grupo que tiene compensacion
		resultado = new ArrayList<ScsSaltoCompensacionGrupoBean>();
		ListIterator iter = compensaciones.listIterator();
		while (iter.hasNext()) {
			compensacion = (ScsSaltoCompensacionGrupoBean) iter.next();
			compensacion.setLetrados(InscripcionGuardia.getLetradosGrupo(idInstitucion, idTurno, idGuardia,
					compensacion.getIdGrupoGuardia(), this.usrbean));
			resultado.add(compensacion);
		}

		return resultado;
	} // getSaltosCompensacionesPendientesGuardia()
	
	public void crearSaltoCompensacion(String idGrupoGuardia,
			String fecha,
			String motivo,
			String idInstitucion,
			String idTurno,
			String idGuardia,
			String idCalendarioGuardias,
			String saltoCompensacion) throws ClsExceptions
	{
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, getNuevoIdSaltoCompensacionGrupo());
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA, idGrupoGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, saltoCompensacion);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHA, fecha);
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVO, motivo);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO, idTurno);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA, idGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS, idCalendarioGuardias);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACREACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUCREACION, this.usrbean.getUserName());
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION, this.usrbean.getUserName());

		this.insert(hash);
	}
	
	public void crearSaltoBT(String idGrupoGuardia,
			String fecha,
			String motivo,
			String idInstitucion,
			String idTurno,
			String idGuardia,
			String idCalendarioGuardias,
			CenBajasTemporalesBean btBean) throws ClsExceptions
	{
		// obtencion de la descripcion de la Baja temporal
		StringBuffer motivoBT = new StringBuffer();
		if (btBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_VACACION))
			motivoBT.append(UtilidadesString.getMensajeIdioma(this.usrbean, CenBajasTemporalesBean.TIPO_DESC_VACACION));
		else if (btBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_BAJA))
			motivoBT.append(UtilidadesString.getMensajeIdioma(this.usrbean, CenBajasTemporalesBean.TIPO_DESC_BAJA));
		motivoBT.append(" ");
		motivoBT.append(btBean.getDescripcion());
		motivo += ": " + motivoBT;
		
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, getNuevoIdSaltoCompensacionGrupo());
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA, idGrupoGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, ClsConstants.SALTOS);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHA, fecha);
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVO, motivo);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO, idTurno);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA, idGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS, idCalendarioGuardias);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO, fecha);
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO, motivo);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI, idInstitucion);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI, idTurno);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI, idGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI, idCalendarioGuardias);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACREACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUCREACION, this.usrbean.getUserName());
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION, this.usrbean.getUserName());

		this.insert(hash);
	}
	
	public void cumplirSaltoCompensacion(String idSaltoCompensacionGrupo,
			String fechaCumplimiento,
			String motivoCumplimiento,
			String idInstitucion_Cumpli,
			String idTurno_Cumpli,
			String idGuardia_Cumpli,
			String idCalendarioGuardias_Cumpli) throws ClsExceptions
	{
		Hashtable<String, String> hash, original;

		// obteniendo datos originales
		hash = new Hashtable<String, String>();
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, getNuevoIdSaltoCompensacionGrupo());
		Vector v = this.selectForUpdate(hash);
		original = this.beanToHashTable((ScsSaltoCompensacionGrupoBean) v.get(0));

		// guardando datos nuevos
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO, fechaCumplimiento);
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO, motivoCumplimiento);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI, idInstitucion_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI, idTurno_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI, idGuardia_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI, idCalendarioGuardias_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION, this.usrbean.getUserName());
		this.update(original, hash);
	}
	
	/*public void crearCompensacion(Integer idInstitucion, Integer idTurno, Integer idGuardia, Long idGrupoGuardia)
	public void crearSaltoBT()
	public void cumplirSalto()
	public void cumplirCompensacion()
	public void cumplirSalto()
	public void cumplirCompensacion()*/
//	
//	/** 
//	 * Actualiza en base de datos la fecha de cumplimiento.
//	 * 
//	 * @param String idinstitucion
//	 * @param String idturno
//	 * @param String idguardia
//	 * @param String idpersona
//	 * @return void
//	 * @throws ClsExceptions
//	 */	
//	public void anotarFechaCumplimiento(String idinstitucion, String idturno, String idguardia, String idpersona) throws ClsExceptions {
//		Vector registros = new Vector();
//		Hashtable hashTemporal = new Hashtable();
//		String idsaltosturno = "";
//		
//		try {
//			//Consulta para ver si tiene compensaciones
//			String where = " WHERE "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion;
//			where += " AND "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno;
//			where += " AND "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia;
//			where += " AND "+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+"="+idpersona;
//			where += " AND "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"='S'";
//			where += " AND "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" IS NULL ";
//			registros = this.select(where);
//			
//			//Si tengo compensacion actualizo
//			if (registros.size() > 0) {
//				idsaltosturno = ((ScsSaltoCompensacionGrupoBean)registros.get(0)).getIdSaltosTurno().toString();
//				//Si la fecha de cumplimiento es nula: Anoto la fecha de cumplimiento
//				hashTemporal.clear();
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,idinstitucion);
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO,idturno);
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA,idguardia);
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDPERSONA,idpersona);
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION,"S");
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOSTURNO,idsaltosturno);
//				hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO,"sysdate");
//				String claves[] = {ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,ScsSaltoCompensacionGrupoBean.C_IDTURNO,
//								   ScsSaltoCompensacionGrupoBean.C_IDGUARDIA,ScsSaltoCompensacionGrupoBean.C_IDPERSONA,
//								   ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION,ScsSaltoCompensacionGrupoBean.C_IDSALTOSTURNO};
//				String campos[] = {ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO};
//				this.updateDirect(hashTemporal,claves,campos);
//			}
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Excepcion en ScsSaltoCompensacionGrupoAdm.anotarFechaCumplimiento().");
//		}
//	}
//
//	/** 
//	 * Actualiza en base de datos la fecha de cumplimiento.
//	 * 
//	 * @param String idinstitucion
//	 * @param String idturno
//	 * @param String idsaltosturno
//	 * @return void
//	 * @throws ClsExceptions
//	 */	
//	public void setFechaCumplimiento(String idinstitucion, String idturno, String idsaltosturno) throws ClsExceptions {
//		Hashtable hashTemporal = new Hashtable();
//		
//		try {
//			//Anoto la fecha de cumplimiento
//			hashTemporal.clear();
//			hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,idinstitucion);
//			hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO,idturno);
//			hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOSTURNO,idsaltosturno);
//			hashTemporal.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO,"sysdate");
//			String claves[] = {ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,
//							   ScsSaltoCompensacionGrupoBean.C_IDTURNO,
//					   		   ScsSaltoCompensacionGrupoBean.C_IDSALTOSTURNO};
//			String campos[] = {ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO};
//
//			//Actualizo la fecha de cumplimiento 
//			this.updateDirect(hashTemporal,claves,campos);
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Excepcion en ScsSaltoCompensacionGrupoAdm.setFechaCumplimiento().");
//		}
//	}
//
//	/** 
//	 * Consulta si hay compensacion para un registro. Devuelve el idsaltosturno si existe la compensacion o vacio.
//	 * 
//	 * @param boolean RW: true si usamos el pool de lectura escritura.
//	 * @param String idinstitucion
//	 * @param String idturno
//	 * @param String idguardia
//	 * @param String idpersona
//	 * @return String idsaltosturno: devuelve "" si no hay compensacion. Devuelve el idsaltosturno si hay compensacion
//	 * @throws ClsExceptions
//	 */	
//	public String hayCompensacion(boolean RW, String idinstitucion, String idturno, String idguardia, String idpersona) throws ClsExceptions {
//		String where = "", idsaltosturno="";
//		Vector registros = new Vector();
//
//		//Consulta para ver si tiene compensaciones
//		where = " WHERE ";
//		where += ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion;
//		where += " AND "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno;
//		where += " AND "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia;
//		where += " AND "+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+"="+idpersona;
//		where += " AND "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"='C'";
//		where += " AND "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" IS NULL ";
//
//		try { 
//			registros.clear();
//			if (RW)
//				registros = this.selectForUpdate(where);
//			else 
//				registros = this.select(where);
//
//			if (registros.size() > 0)  
//				idsaltosturno = (((ScsSaltoCompensacionGrupoBean)registros.get(0)).getIdSaltosTurno()).toString();
//			else idsaltosturno = "";
//			
//			return idsaltosturno;	
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Excepcion en ScsSaltoCompensacionGrupoAdm.hayCompensacion().");
//		}
//	}
//	
//	/** 
//	 * Devuelve la consulta SQL de la búsqueda de Turnos, Guardias y Letrados con saltos o compensaciones.
//	 * 
//	 * @param Hashtable registros: tabla hash con los datos de la pantalla para realizar la busqueda.
//	 * @return String: tiene la consulta SQL a ejecutar
//	 * @throws ClsExceptions
//	 */	
//	public String buscar(Hashtable registros) throws ClsExceptions {
//		String consulta = "";
//		String fechaDesde="", fechaHasta="", idTurno="", idGuardia="", idPersona="", salto="", compensado="";		
//
//		try { 
//			//Datos iniciales:
//			fechaDesde = UtilidadesHash.getString(registros,"FECHADESDE"); 
//			fechaHasta = UtilidadesHash.getString(registros,"FECHAHASTA");
//			idTurno = UtilidadesHash.getString(registros,"IDTURNO");
//			idGuardia = UtilidadesHash.getString(registros,"IDGUARDIA");
//			idPersona = UtilidadesHash.getString(registros,"IDPERSONA");
//			salto = UtilidadesHash.getString(registros,"SALTO");
//			compensado = UtilidadesHash.getString(registros,"COMPENSADO");
//			
//			//Consulta:
//			consulta  = "SELECT ";
//			consulta += " saltos.*,";
//			consulta += " perso."+CenPersonaBean.C_NOMBRE+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS1+" || ' ' || perso."+CenPersonaBean.C_APELLIDOS2+" AS LETRADO,";
//			consulta += " coleg."+CenColegiadoBean.C_NCOLEGIADO+" AS NUMERO,";
//			consulta += " turno."+ScsTurnoBean.C_NOMBRE+" AS NOMBRETURNO,";
//			consulta += " guardia."+ScsGuardiasTurnoBean.C_NOMBRE+" AS NOMBREGUARDIA ";
//			consulta += " FROM "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA+" saltos, ";
//			consulta += ScsTurnoBean.T_NOMBRETABLA+" turno, ";
//			consulta += ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardia, ";
//			consulta += CenPersonaBean.T_NOMBRETABLA+" perso, ";
//			consulta += CenColegiadoBean.T_NOMBRETABLA+" coleg ";
//			consulta += " WHERE ";
//			consulta += " saltos."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+UtilidadesHash.getString(registros,"IDINSTITUCION");
//			if(salto!=null)
//				consulta += " AND saltos."+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"='"+salto+"'";
//			if (!idTurno.equals(""))
//				consulta += " AND saltos."+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idTurno;
//			if (!idGuardia.equals(""))
//				consulta += " AND saltos."+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idGuardia;
//			if (!idPersona.equals(""))
//				consulta += " AND saltos."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+"="+idPersona;
//			if (compensado!=null && compensado.equals("S"))
//				consulta += " AND saltos."+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" > TO_DATE('01/01/2001','DD/MM/YYYY')";
//			if (compensado!=null && compensado.equals("N"))
//				consulta += " AND saltos."+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" is null ";
//			
//			if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
//				if (!fechaDesde.equals(""))
//					fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
//				if (!fechaHasta.equals(""))
//					fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);
//				consulta += " AND " + GstDate.dateBetweenDesdeAndHasta("saltos."+ScsSaltoCompensacionGrupoBean.C_FECHA, fechaDesde, fechaHasta);
//			}
//			
//			//JOINS
//			consulta += " AND perso."+CenPersonaBean.C_IDPERSONA+"=saltos."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA;
//			consulta += " AND coleg."+CenColegiadoBean.C_IDPERSONA+"=saltos."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA;
//			consulta += " AND coleg."+CenColegiadoBean.C_IDINSTITUCION+"=saltos."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION;			
//			consulta += " AND turno."+ScsTurnoBean.C_IDINSTITUCION+"=saltos."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION;
//			consulta += " AND turno."+ScsTurnoBean.C_IDTURNO+"=saltos."+ScsSaltoCompensacionGrupoBean.C_IDTURNO;
//			consulta += " AND guardia."+ScsGuardiasTurnoBean.C_IDINSTITUCION+"(+)=saltos."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION;
//			consulta += " AND guardia."+ScsGuardiasTurnoBean.C_IDTURNO+"(+)=saltos."+ScsSaltoCompensacionGrupoBean.C_IDTURNO;
//			consulta += " AND guardia."+ScsGuardiasTurnoBean.C_IDGUARDIA+"(+)=saltos."+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA;
//			//ORDENACION
//			consulta += " ORDER BY saltos."+ScsSaltoCompensacionGrupoBean.C_FECHA+" desc";
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Excepcion en ScsSaltoCompensacionGrupoAdm.buscar() en la consulta:"+consulta);
//		}
//		return consulta;
//	}
//
//	/**
//	 * Efectúa un SELECT en la tabla SCS_SALTOSCOMPENSACIONES con los datos introducidos. 
//	 * @param institucion Codigo institucion seleccionada
//	 * @param inTurno Codigo del Turno seleccionado
//	 * @param idGuardia Codigo guardia seleccionada
//	 * @param soc Indica si se va aconsultar sobre saltos o compensaciones
//	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
//	 */
//	public Vector selectSaltosCompensaciones(String institucion, String idTurno,String idGuardia, String soc){
//		Vector vResult = null;
//		try{
//			String consulta =
//				"select decode(C."+CenColegiadoBean.C_COMUNITARIO+",'"+ClsConstants.DB_TRUE+"',"+"C."+CenColegiadoBean.C_NCOMUNITARIO+","+CenColegiadoBean.C_NCOLEGIADO+") "+CenColegiadoBean.C_NCOLEGIADO+", "+
//				"P."+CenPersonaBean.C_NOMBRE+", "+
//				"P."+CenPersonaBean.C_APELLIDOS1+", "+
//				"P."+CenPersonaBean.C_APELLIDOS2+", "+
//				"SC.NUMERO "+
//				" from "+
//				CenColegiadoBean.T_NOMBRETABLA+" C, "+
//				CenPersonaBean.T_NOMBRETABLA+" P, "+
//				"(select "+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+", count(1) NUMERO"+
//				" from "  +ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA+
//				" where " +ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+institucion+
//				" and   "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"='"+soc+"'"+
//				" and   "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" is null"+
//			    " and   "+ScsSaltoCompensacionGrupoBean.C_IDTURNO +"=" + idTurno ;
//			if(idGuardia==null || idGuardia.trim().equals("")){
//				consulta +=" and   "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+" is null";
//			}else{
//				consulta +=" and   "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idGuardia;
//			}
//			consulta +=" group by "+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+") SC"+
//			" where SC."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+"=C."+CenColegiadoBean.C_IDPERSONA+
//			" and   SC."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+"=P."+CenPersonaBean.C_IDPERSONA+
//			" and   C."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+institucion+
//			" order by SC.NUMERO desc";
//						
//			vResult=this.find(consulta).getAll();
//			
//		}catch(ClsExceptions e){
//			e.printStackTrace();
//		}
//		return vResult;
//	}
//	
//	
//	
//	/**
//	 * A la hora de borrar un calendario hay 3 pasos posteriores:
//	 * Paso1: Descumplimientar saltos cumplidas en este calendario.
//	 * Los saltos no se crean al generar un calendario, en cambio sí que la
//	 * generación de calendarios puede cumplir saltos. Al borrar el calendario
//	 * se deben "descumplimentar dichos saltos" 
//     *
//	 * @param Hashtable hash: tabla hash con los campos: 
//	 * - String idinstitucion
//	 * - String idcalendarioguardias
//	 * - String idturno
//	 * - String idguardia  
//	 * @return boolean: true si ha ido todo bien.
//	 * @throws ClsExceptions
//	 */
//	public boolean updateSaltosCumplidos(Hashtable hash) throws ClsExceptions {
//		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
//		boolean salida = false;
//		StringBuffer sql = new StringBuffer();
//		
//		try {
//			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
//			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
//			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
//			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
//
//			sql.append(" update "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
//			sql.append("    set "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+"= NULL");
//			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+"= NULL");
//			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION+"= SYSDATE");
//			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION+"="+this.usuModificacion);
//			sql.append("  where "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"= 'S'");
//			
//			updateSQL(sql.toString());
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	}
//	
//	/**
//	 * A la hora de borrar un calendario hay 3 pasos posteriores:
//	 * Paso2: Descumplimentar compensaciones cumplidas en este calendario.
//	 * Las compensaciones pudieron crearse al generar otro calendario o manulamente.
//	 * -Si se crearon manualmente basta con descumplimentarlas para restaurar la situación anterior a la generación del calendario.
//	 * -Si se crearon por otro calendario, no podrá realizarse la asociación al calendario
//	 *  anterior, por tanto tendrá que dejarse sin asociar al anterior y se procederá 
//	 *  como en el caso anterior.
//     *
//	 * @param Hashtable hash: tabla hash con los campos: 
//	 * - String idinstitucion
//	 * - String idcalendarioguardias
//	 * - String idturno
//	 * - String idguardia  
//	 * @return boolean: true si ha ido todo bien.
//	 * @throws ClsExceptions
//	 */
//	public boolean updateCompensacionesCumplidas(Hashtable hash) throws ClsExceptions {
//		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
//		boolean salida = false;
//		StringBuffer sql = new StringBuffer();
//		
//		try {
//			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
//			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
//			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
//			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
//
//			sql.append(" update "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
//			sql.append("    set "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+"= NULL");
//			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+"= NULL");
//			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION+"= SYSDATE");
//			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION+"="+this.usuModificacion);
//			sql.append("  where "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"= 'C'");
//			
//			updateSQL(sql.toString());		
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	}
//	
//	/**
//	 * Borra los saltos creados en el caledario pasado como parametro
//	 */
//	public boolean deleteSaltosCreadosEnCalendario(Hashtable hash) throws ClsExceptions {
//		boolean salida;
//		
//		try {
//			String idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
//			String idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
//			String idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
//			String idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
//			
//			StringBuffer sql = new StringBuffer();
//			sql.append(" delete from "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
//			sql.append("  where "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION+"="+idcalendarioguardias);
//			sql.append("    and ("+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
//			sql.append("     or  "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+" is null)");
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"= 'S'");
//			
//			deleteSQL(sql.toString());		
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	} //deleteSaltosCreadosEnCalendario()
//	
//	/**
//	 * A la hora de borrar un calendario hay 3 pasos posteriores:
//	 * Paso3: Eliminar compensaciones NO cumplidas en este calendario.
//	 * Las compensaciones creadas en este calendario que aún no están cumplidas o 
//	 * o ejecutadas por este calendario, han de borrarse.
//     *
//	 * @param Hashtable hash: tabla hash con los campos: 
//	 * - String idinstitucion
//	 * - String idcalendarioguardias
//	 * - String idturno
//	 * - String idguardia  
//	 * @return boolean: true si ha ido todo bien.
//	 * @throws ClsExceptions
//	 */
//	public boolean deleteCompensacionesNoCumplidas(Hashtable hash) throws ClsExceptions {
//		String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="";
//		boolean salida = false;
//		StringBuffer sql = new StringBuffer();
//		
//		try {
//			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
//			idcalendarioguardias = (String)hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
//			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
//			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
//			
//			sql.append(" delete from "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
//			sql.append("  where "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION+"="+idcalendarioguardias);
//			sql.append("    and ("+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
//			sql.append("     or  "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+" is null)");
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia);
//			sql.append("    and "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"= 'C'");
//			
//			deleteSQL(sql.toString());		
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	}	
//	
//	/**
//	 * Elimina compensaciones de calendarios que ya no existen en la guardia.
//     *
//	 * @param Hashtable hash: tabla hash con los campos: 
//	 * - String idinstitucion
//	 * - String idcalendarioguardias
//	 * - String idturno
//	 * - String idguardia  
//	 * @return boolean: true si ha ido todo bien.
//	 * @throws ClsExceptions
//	 */
//	public boolean deleteCompensacionesCalendariosInexistentes(Hashtable hash) throws ClsExceptions {
//		String idinstitucion="", idturno="", idguardia="";
//		boolean salida = false;
//		StringBuffer sql = new StringBuffer();
//		
//		try {
//			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
//			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
//			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
//			
//			sql.append(" delete from "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA+" SC");
//			sql.append(" where SC."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia);
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"= 'C'");
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+" is null");
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION+" is not null");
//			sql.append(" and not exists (");
//			sql.append(" select 1 from "+ScsCalendarioGuardiasBean.T_NOMBRETABLA+" CG");
//			sql.append(" where CG."+ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append(" and CG."+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idturno);
//			sql.append(" and CG."+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idguardia);
//			sql.append(" and CG."+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+"= SC."+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION);
//			sql.append(")");
//
//					
//			this.deleteSQL(sql.toString());
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	}	
//	
//	/**
//	 * Elimina saltos de calendarios que ya no existen en la guardia.
//     *
//	 * @param Hashtable hash: tabla hash con los campos: 
//	 * - String idinstitucion
//	 * - String idcalendarioguardias
//	 * - String idturno
//	 * - String idguardia  
//	 * @return boolean: true si ha ido todo bien.
//	 * @throws ClsExceptions
//	 */
//	public boolean deleteSaltosCalendariosInexistentes(Hashtable hash) throws ClsExceptions {
//		String idinstitucion="", idturno="", idguardia="";
//		boolean salida = false;
//		StringBuffer sql = new StringBuffer();
//		
//		try {
//			idinstitucion = (String)hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
//			idturno = (String)hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
//			idguardia = (String)hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);			
//			
//			sql.append(" delete from "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA+" SC");
//			sql.append(" where SC."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+"="+idguardia);
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+"= 'S'");
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+" is null");
//			sql.append(" and SC."+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION+" is not null");
//			sql.append(" and not exists (");
//			sql.append(" select 1 from "+ScsCalendarioGuardiasBean.T_NOMBRETABLA+" CG");
//			sql.append(" where CG."+ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append(" and CG."+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idturno);
//			sql.append(" and CG."+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idguardia);
//			sql.append(" and CG."+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+"= SC."+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION);
//			sql.append(")");
//
//					
//			this.deleteSQL(sql.toString());
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	}	
//	
//	/**
//	 * A la hora de dar de baja de un turno se debe dar fecha de uso a los saltos y compensaciones
//	 * pendientes para que no se asignen 
//     *
//	 * @param Hashtable hash: tabla hash con los campos: 
//	 * - String idinstitucion
//	 * - String idpersona
//	 * - String idturno
//	 * - String fechaCumplimiento
//     * - String motivos  
//	 * @return boolean: true si ha ido todo bien.
//	 * @throws ClsExceptions
//	 */
//	public boolean updateCompensacionesSaltos(Hashtable hash) throws ClsExceptions {
//		String idinstitucion="", idcalendarioguardias="", idturno="", fecha = "", idpersona, comentario;
//		boolean salida = false;
//		StringBuffer sql = new StringBuffer();
//		
//		try {
//			idinstitucion = (String)hash.get(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION);
//			idturno = (String)hash.get(ScsSaltoCompensacionGrupoBean.C_IDTURNO);
//			fecha = (String)hash.get(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO);
//			idpersona = (String)hash.get(ScsSaltoCompensacionGrupoBean.C_IDPERSONA);
//			comentario = (String)hash.get(ScsSaltoCompensacionGrupoBean.C_MOTIVOS);
//
//			sql.append(" update "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
//			sql.append(" set "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+"= trunc(to_date('" + fecha + "', 'DD/MM/YYYY'))");
//			sql.append(" , "+ScsSaltoCompensacionGrupoBean.C_MOTIVOS+"='"+comentario+"'");
//			sql.append(" , "+ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION+"= SYSDATE");
//			sql.append(" , "+ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION+"="+this.usuModificacion);
//			sql.append(" where "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+"="+idinstitucion);
//			sql.append(" and "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+"="+idturno);
//			sql.append(" and "+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+"="+idpersona);
//			sql.append(" and "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" IS NULL");
//					
//			this.updateSQL(sql.toString());
//			salida = true;
//		} catch (Exception e) {
//			salida = false;
//		}
//		return salida;
//	}
//	public void insertarSaltoPorBajaTemporal(CenBajasTemporalesBean bajaTemporaBean,ScsSaltoCompensacionGrupoBean salto) throws ClsExceptions{
//		StringBuffer descripcion = new StringBuffer();
//		if(bajaTemporaBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_VACACION)){
//			descripcion.append(UtilidadesString.getMensajeIdioma(this.usrbean, CenBajasTemporalesBean.TIPO_DESC_VACACION));
//			
//		}else if(bajaTemporaBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_BAJA)){
//			descripcion.append(UtilidadesString.getMensajeIdioma(this.usrbean, CenBajasTemporalesBean.TIPO_DESC_BAJA));
//			
//		}
//		descripcion.append(" ");
//		descripcion.append(bajaTemporaBean.getDescripcion());
//		salto.setMotivos(descripcion.toString());
//		salto.setUsuMod(this.usuModificacion);
//		salto.setFechaMod("sysdate");
//		
//		Long idSaltosTurno = Long.valueOf(getNuevoIdSaltosTurno(salto.getIdInstitucion().toString(),salto.getIdTurno().toString()));
//		salto.setIdSaltosTurno(idSaltosTurno);
//		this.insert(salto);
//		
//		
//	}
//	public void insertarSaltoCompensacion(ScsSaltoCompensacionGrupoBean salto) throws ClsExceptions{
//		
//		salto.setUsuMod(this.usuModificacion);
//		salto.setFechaMod("sysdate");
//		
//		Long idSaltosTurno = Long.valueOf(getNuevoIdSaltosTurno(salto.getIdInstitucion().toString(),salto.getIdTurno().toString()));
//		salto.setIdSaltosTurno(idSaltosTurno);
//		this.insert(salto);
//		
//		
//	}
//
//	public void marcarSaltoCompensacionGuardia(ScsSaltoCompensacionGrupoBean saltoCompensacion)
//			throws ClsExceptions
//	{
//		try {
//			String sql = 
//				" UPDATE "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA+ " " +
//				"    SET "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" = '"+saltoCompensacion.getFechaCumplimiento()+"', " +
//				"        "+ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION+" = "+this.usrbean.getUserName()+"," +
//				"        "+ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION+" = SYSDATE , " +
//				"        "+ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS+" = "+saltoCompensacion.getIdCalendarioGuardias()+" ";
//			if (saltoCompensacion.getMotivos() != null && !saltoCompensacion.getMotivos().equals(""))
//				sql += " , " +
//						""+ScsSaltoCompensacionGrupoBean.C_MOTIVOS+" = "+ScsSaltoCompensacionGrupoBean.C_MOTIVOS+" || '"+saltoCompensacion.getMotivos()+"' ";
//			sql += 
//				"  WHERE "+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION+" = "+saltoCompensacion.getIdInstitucion()+" " +
//				"    AND "+ScsSaltoCompensacionGrupoBean.C_IDTURNO+" = "+saltoCompensacion.getIdTurno()+" " +
//				"    AND "+ScsSaltoCompensacionGrupoBean.C_IDGUARDIA+" = "+saltoCompensacion.getIdGuardia()+" " +
//				"    AND "+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+" = "+saltoCompensacion.getIdPersona()+" " +
//				"    AND "+ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION+" = '"+saltoCompensacion.getSaltoCompensacion()+"' " +
//				"    AND "+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" IS NULL " +
//				"    AND rownum=1";
//		
//			this.updateSQL(sql);
//
//		} catch (Exception e) {
//			throw new ClsExceptions(e,
//					"Excepcion en marcarSaltoCompensacionBBDD." + e.toString());
//		}
//	}
//	
//	public HashMap<Long, List<LetradoGuardia>> getSaltos (Integer idInstitucion,Integer idTurno,Integer idGuardia) throws ClsExceptions {
//	    RowsContainer rc = new RowsContainer();
//	    LetradoGuardia letradoSeleccionado = null;
//	    HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos = null;
//		// voy a comprobar si existe un salto en base de datos
//		try {
//
//			String sql =  getQuerySaltosCompensacionesActivos(ClsConstants.SALTOS,idInstitucion,idTurno,idGuardia);
//	
//			rc = find(sql);
//			hmPersonasConSaltos = new HashMap<Long, List<LetradoGuardia>>();
//			List<LetradoGuardia> alLetradosSaltados = null; 
//			for (int i = 0; i < rc.size(); i++){
//			    letradoSeleccionado = new LetradoGuardia();
//			    letradoSeleccionado.setIdInstitucion(idInstitucion);
//			    letradoSeleccionado.setIdTurno(idTurno);
//			    letradoSeleccionado.setIdGuardia(idGuardia);
//			    letradoSeleccionado.setSaltoCompensacion("C");
//			    
//			    Row fila = (Row) rc.get(i);
//				Hashtable hFila = fila.getRow();
//				Long idPersona = new Long((String)hFila.get(ScsSaltoCompensacionGrupoBean.C_IDPERSONA));
//				letradoSeleccionado.setIdPersona(idPersona);
//				if(hmPersonasConSaltos.containsKey(idPersona)){
//					alLetradosSaltados = (List)hmPersonasConSaltos.get(idPersona);
//					
//				}else{
//					alLetradosSaltados = new ArrayList<LetradoGuardia>();
//					
//				}
//				
//				alLetradosSaltados.add(letradoSeleccionado);
//				hmPersonasConSaltos.put(idPersona, alLetradosSaltados);
//			    //letradoSeleccionado
//			}
//		} catch (Exception e) {
//		    throw new ClsExceptions(e,"Error al comporbar si hay salto en BD.");
//		}
//		
//		return hmPersonasConSaltos;
//	}
//
//	private String getQuerySaltosCompensacionesActivos(String tipo,
//													   Integer idInstitucion,
//													   Integer idTurno,
//													   Integer idGuardia)
//	{
//		StringBuffer sql = new StringBuffer();
//		
//		sql.append(" select * ");
//		sql.append("   from scs_saltoscompensaciones s ");
//		sql.append("  where s.idinstitucion = "+idInstitucion);
//		sql.append("    and s.idturno = "+idTurno);
//		sql.append("    and s.idguardia = "+idGuardia);
//		sql.append("    and s.saltoocompensacion = '");
//		sql.append(tipo);
//		sql.append("'    and s.fechacumplimiento is null ");
//		sql.append("  order by s.fecha ");
//
//		return sql.toString();
//	}
//	public Vector<LetradoGuardia> getLetradosSaltosCompensacionesTurno(String idInstitucion, String idTurno) throws ClsExceptions {
//		
//		String sql = "select p." + CenPersonaBean.C_IDPERSONA +
//				", p." + CenPersonaBean.C_NIFCIF +
//				", c." + CenColegiadoBean.C_NCOLEGIADO +
//				", p." + CenPersonaBean.C_NOMBRE +
//				", p." + CenPersonaBean.C_APELLIDOS1 +
//				", p." + CenPersonaBean.C_APELLIDOS2 +
//				", sc." + ScsSaltoCompensacionGrupoBean.C_IDSALTOSTURNO +
//				", sc." + ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION + 
//				" from "+ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA+" sc, "+CenPersonaBean.T_NOMBRETABLA+" p, " + CenColegiadoBean.T_NOMBRETABLA + " c" +
//				" where sc."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+" = p." + CenPersonaBean.C_IDPERSONA +
//				" and sc."+ScsSaltoCompensacionGrupoBean.C_IDPERSONA+" = c." + CenColegiadoBean.C_IDPERSONA +
//				" and sc."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION + " = " + idInstitucion +
//				" and sc."+ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION + " = c." + CenColegiadoBean.C_IDINSTITUCION +
//				" and sc."+ScsSaltoCompensacionGrupoBean.C_IDTURNO+" = " + idTurno +
//				" and sc."+ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO+" is null" +
//				" and sc." + ScsSaltoCompensacionGrupoBean.C_IDGUARDIA + " is null" +
//				" order by sc."+ScsSaltoCompensacionGrupoBean.C_FECHA;
//		Vector<LetradoGuardia> datosVector = null;
//		try {
//			RowsContainer rc = new RowsContainer(); 
//            if (rc.find(sql)) {
//            	datosVector = new Vector<LetradoGuardia>();
//    			for (int i = 0; i < rc.size(); i++){
//            		Row fila = (Row) rc.get(i);
//            		Hashtable<String, Object> htFila=fila.getRow();
//            		
//            		LetradoGuardia letradoGuardia = new LetradoGuardia();
//            		letradoGuardia.setSaltoCompensacion(UtilidadesHash.getString(htFila, ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION));
//            		letradoGuardia.setIdSaltoCompensacion(UtilidadesHash.getString(htFila, ScsSaltoCompensacionGrupoBean.C_IDSALTOSTURNO));
//            		
//            		letradoGuardia.setIdPersona(UtilidadesHash.getLong(htFila, ScsInscripcionGuardiaBean.C_IDPERSONA));
//            		CenPersonaBean personaBean = new CenPersonaBean(
//            				letradoGuardia.getIdPersona(),(String)htFila.get(CenPersonaBean.C_NOMBRE),(String)htFila.get(CenPersonaBean.C_APELLIDOS1),
//            				(String)htFila.get(CenPersonaBean.C_APELLIDOS2),(String)htFila.get(CenColegiadoBean.C_NCOLEGIADO));
//            		letradoGuardia.setPersona(personaBean);
//            		datosVector.add(letradoGuardia);
//            	}
//            } 
//       } catch (Exception e) {
//    	   throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
//       }
//		return datosVector;
//	}
	
	
}