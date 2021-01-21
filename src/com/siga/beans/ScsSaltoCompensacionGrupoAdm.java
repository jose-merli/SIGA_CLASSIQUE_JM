
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_SALTOCOMPENSACIONGRUPO
 * @since 1/11/2004 
 */
public class ScsSaltoCompensacionGrupoAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicaci�n. De tipo "Integer".  
	 */
	public ScsSaltoCompensacionGrupoAdm(UsrBean usuario) {
		super(ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA, usuario);
	}

	/** 
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean() {
		String[] campos = {	
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
			ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION,
			ScsSaltosCompensacionesBean.C_TIPOMANUAL
		};
		return campos;
	}

	/**
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO};
		return campos;
	}
	
	/**
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la informaci�n de la hashtable
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
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
			bean.setTipoManual(UtilidadesHash.getInteger(hash, ScsSaltoCompensacionGrupoBean.C_TIPOMANUAL));
			
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
	 *  @return hashtable con la informaci�n del bean
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
			UtilidadesHash.set(hash, ScsSaltoCompensacionGrupoBean.C_TIPOMANUAL, String.valueOf(b.getTipoManual()));
			
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
	 *  @return String[] conjunto de valores con los campos por los que se deber� ordenar la select
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
	 * @param compensaciones2 
	 * @return
	 * @throws ClsExceptions
	 */
	public ArrayList<ScsSaltoCompensacionGrupoBean> getSaltosCompensacionesPendientesGuardia(Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fechaGuardia,
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
			ArrayList<LetradoInscripcion> letradoInscripciones = InscripcionGuardia.getLetradosGrupo(idInstitucion, idTurno, idGuardia,
					compensacion.getIdGrupoGuardia(), saltoOcompensacion, 
					compensacion.getIdSaltoCompensacionGrupo().toString(), fechaGuardia, this.usrbean);
			//Si no hay ningun letrado inscrito del grupo que hay que compensar continuamos
			if(letradoInscripciones==null)
				continue;
			compensacion.setLetrados(letradoInscripciones);
			resultado.add(compensacion);
		}

		return resultado;
	} // getSaltosCompensacionesPendientesGuardia()
	
	
	//BNS: INC_07349_SIGA
	public void crearSaltoCompensacion(String idGrupoGuardia,
			String fecha,
			String motivo,
			String idInstitucion,
			String idTurno,
			String idGuardia,
			String idCalendarioGuardias,
			String saltoCompensacion) throws ClsExceptions{
		crearSaltoCompensacion(idGrupoGuardia, fecha, motivo, idInstitucion, idTurno, idGuardia, idCalendarioGuardias, saltoCompensacion, null);
	}
	
	public void crearSaltoCompensacion(String idGrupoGuardia,
			String fecha,
			String motivo,
			String idInstitucion,
			String idTurno,
			String idGuardia,
			String idCalendarioGuardias,
			String saltoCompensacion,
			String idCalendarioGuardiasCreacion) throws ClsExceptions
	{
		fecha = UtilidadesFecha.getFechaApruebaDeFormato(fecha);
		
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
		//BNS: INC_07349_SIGA
		if (idCalendarioGuardiasCreacion != null)
			hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION, idCalendarioGuardias);
		
		this.insert(hash);
	}
	
	public void crearSaltoBT(String idGrupoGuardia,
			String fecha,
			String motivo,
			String idInstitucion,
			String idTurno,
			String idGuardia,
			String idCalendarioGuardias,			
			CenBajasTemporalesBean btBean) throws ClsExceptions{
		crearSaltoBT(idGrupoGuardia, fecha, motivo, idInstitucion, idTurno, idGuardia, idCalendarioGuardias, null, btBean);
	}
	
	public void crearSaltoBT(String idGrupoGuardia,
			String fecha,
			String motivo,
			String idInstitucion,
			String idTurno,
			String idGuardia,
			String idCalendarioGuardias,
			String idCalendarioGuardiasCreacion,
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
		motivo = motivoBT + ": " + motivo;
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, getNuevoIdSaltoCompensacionGrupo());
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA, idGrupoGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, ClsConstants.SALTOS);
		 
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHA, GstDate.getApplicationFormatDate("", fecha));
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVO, motivo);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO, idTurno);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA, idGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS, idCalendarioGuardias);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO, GstDate.getApplicationFormatDate("", fecha));
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO, motivo);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI, idInstitucion);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI, idTurno);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI, idGuardia);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI, idCalendarioGuardias);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACREACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUCREACION, this.usrbean.getUserName());
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION, this.usrbean.getUserName());

		if (idCalendarioGuardiasCreacion != null){
			hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIASCREACION, idCalendarioGuardiasCreacion);
		}
		
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
		original = new Hashtable<String, String>();
		original.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, idSaltoCompensacionGrupo);
		hash = (Hashtable<String, String>) original.clone();

		// guardando datos nuevos
		fechaCumplimiento = UtilidadesFecha.getFechaApruebaDeFormato(fechaCumplimiento);
		
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO, fechaCumplimiento);
		hash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO, motivoCumplimiento);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI, idInstitucion_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI, idTurno_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI, idGuardia_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI, idCalendarioGuardias_Cumpli);
		hash.put(ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION, "sysdate");
		hash.put(ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION, this.usrbean.getUserName());
		this.update(hash, original);
	}
	
	/**
	 * Quita el cumplimiento de saltos y compensaciones del calendario pasado como parametro
	 */
	public boolean updateSaltosCompensacionesCumplidos(Hashtable hash) throws ClsExceptions
	{
		String idinstitucion = "", idcalendarioguardias = "", idturno = "", idguardia = "";
		boolean salida = false;
		StringBuffer sql = new StringBuffer();

		try {
			idinstitucion = (String) hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			idturno = (String) hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			idguardia = (String) hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);
			idcalendarioguardias = (String) hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);

			sql.append(" update " + ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
			sql.append("    set " + ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO + " = null");
			sql.append("      , " + ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI + " = null");
			sql.append("      , " + ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI + " = null");
			sql.append("      , " + ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI + " = null");
			sql.append("      , " + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI + " = null");
			sql.append("      , " + ScsSaltoCompensacionGrupoBean.C_FECHAMODIFICACION + "= SYSDATE");
			sql.append("      , " + ScsSaltoCompensacionGrupoBean.C_USUMODIFICACION + "=" + this.usuModificacion);
			sql.append("      , "+ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO+"=REGEXP_REPLACE(");
			sql.append(ScsSaltoCompensacionGrupoBean.C_MOTIVOCUMPLIMIENTO+", ':id=:"+idcalendarioguardias+".*:finid="+idcalendarioguardias+":',' ')");
			sql.append("  where " + ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION_CUMPLI + "=" + idinstitucion);
			sql.append("    and " + ScsSaltoCompensacionGrupoBean.C_IDTURNO_CUMPLI + "=" + idturno);
			sql.append("    and " + ScsSaltoCompensacionGrupoBean.C_IDGUARDIA_CUMPLI + "=" + idguardia);
			sql.append("    and " + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI + "=" + idcalendarioguardias);

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
	public boolean deleteSaltosCompensacionesCreadosEnCalendario(Hashtable hash) throws ClsExceptions
	{
		boolean salida;

		try {
			String idinstitucion = (String) hash.get(ScsCalendarioGuardiasBean.C_IDINSTITUCION);
			String idcalendarioguardias = (String) hash.get(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS);
			String idturno = (String) hash.get(ScsCalendarioGuardiasBean.C_IDTURNO);
			String idguardia = (String) hash.get(ScsCalendarioGuardiasBean.C_IDGUARDIA);

			StringBuffer sql = new StringBuffer();
			sql.append(" delete from " + ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
			sql.append("  where " + ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION + "=" + idinstitucion);
			sql.append("    and " + ScsSaltoCompensacionGrupoBean.C_IDTURNO + "=" + idturno);
			sql.append("    and " + ScsSaltoCompensacionGrupoBean.C_IDGUARDIA + "=" + idguardia);
			sql.append("    and " + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS + "=" + idcalendarioguardias);
			sql.append("    and (" + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI + "=" + idcalendarioguardias);
			sql.append("     or  " + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI + " is null)");

			deleteSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}
	
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

			sql.append("delete from " + ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA + " SC");
			sql.append(" where SC." + ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION + "=" + idinstitucion);
			sql.append("   and SC." + ScsSaltoCompensacionGrupoBean.C_IDTURNO + "=" + idturno);
			sql.append("   and SC." + ScsSaltoCompensacionGrupoBean.C_IDGUARDIA + "=" + idguardia);
			sql.append("   and SC." + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS + " is not null");
			sql.append("   and (SC." + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI + " is null");
			sql.append("        or  " + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS_CUMPLI);
			sql.append("           = SC." + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS + ")");
			sql.append("   and not exists (select 1 from " + ScsCalendarioGuardiasBean.T_NOMBRETABLA + " CG");
			sql.append("                    where CG." + ScsCalendarioGuardiasBean.C_IDINSTITUCION + "=" + idinstitucion);
			sql.append("                      and CG." + ScsCalendarioGuardiasBean.C_IDTURNO + "=" + idturno);
			sql.append("                      and CG." + ScsCalendarioGuardiasBean.C_IDGUARDIA + "=" + idguardia);
			sql.append("                      and CG." + ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS + "=");
			sql.append("                          SC." + ScsSaltoCompensacionGrupoBean.C_IDCALENDARIOGUARDIAS + ")");

			this.deleteSQL(sql.toString());
			salida = true;
		} catch (Exception e) {
			salida = false;
		}
		return salida;
	}

	private String buildConsultaBusquedaSaltosCompensacionesGrupo(String filtrosWhere, boolean ordenar) {
		StringBuilder consulta = new StringBuilder();
		
		//select
		consulta.append("SELECT ");
		consulta.append(" decode(saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
		consulta.append(",'S','SG','C','CG') ");
		consulta.append(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_IDINSTITUCION);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_IDTURNO);
		consulta.append(" , null as ");
		consulta.append(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_FECHA);
		consulta.append(" , turno.");
		consulta.append(ScsTurnoBean.C_NOMBRE);
		consulta.append(" AS NOMBRETURNO ");
		consulta.append(" , guardia.");
		consulta.append(ScsGuardiasTurnoBean.C_NOMBRE);
		consulta.append(" AS NOMBREGUARDIA ");
		consulta.append(" , saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
		consulta.append(" , '' || grupo.");
		consulta.append(ScsGrupoGuardiaBean.C_NUMEROGRUPO);
		consulta.append(" AS NUMERO ");
		
		consulta.append(" , LISTAGG(CASE to_number(coleg.");
		consulta.append(CenColegiadoBean.C_COMUNITARIO);
		consulta.append("            ) WHEN 1 THEN coleg.");
		consulta.append(CenColegiadoBean.C_NCOMUNITARIO);
		consulta.append("            ELSE coleg.");
		consulta.append(CenColegiadoBean.C_NCOLEGIADO);
		consulta.append("            END || ");
		consulta.append("           ' - ' || perso.");
		consulta.append(CenPersonaBean.C_APELLIDOS1);
		consulta.append("           || nvl2(perso.");
		consulta.append(CenPersonaBean.C_APELLIDOS2);
		consulta.append("           , ' ' || perso.");
		consulta.append(CenPersonaBean.C_APELLIDOS2);
		consulta.append("           , NULL ) || ");
		consulta.append("           ', ' || perso.");
		consulta.append(CenPersonaBean.C_NOMBRE);
		consulta.append("           , '; ') ");
		consulta.append("  WITHIN GROUP(ORDER BY perso.");
		consulta.append(CenPersonaBean.C_APELLIDOS1);
		consulta.append("  ) AS LETRADO ");
		
		//Campos para mostrar en la ventana de cola guardia
		consulta.append(" , turno.");
		consulta.append(ScsGrupoGuardiaBean.C_IDINSTITUCION);
		consulta.append(" AS ID1, turno.");
		consulta.append(ScsGrupoGuardiaBean.C_IDTURNO);
		consulta.append(" AS ID2, saltos.");
		consulta.append(ScsGrupoGuardiaBean.C_IDGUARDIA);
		consulta.append(" AS ID3, grupo.");
		consulta.append(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
		consulta.append(" AS ID4 ");
		
		//from
		consulta.append("  FROM ");
		consulta.append(ScsSaltoCompensacionGrupoBean.T_NOMBRETABLA);
		consulta.append(" saltos, ");
		consulta.append(ScsTurnoBean.T_NOMBRETABLA);
		consulta.append(" turno, ");
		consulta.append(ScsGuardiasTurnoBean.T_NOMBRETABLA);
		consulta.append(" guardia, ");
		consulta.append(ScsGrupoGuardiaBean.T_NOMBRETABLA);
		consulta.append(" grupo, ");
		consulta.append(ScsGrupoGuardiaColegiadoBean.T_NOMBRETABLA);
		consulta.append(" grucol, "); 
		consulta.append(CenPersonaBean.T_NOMBRETABLA);
		consulta.append(" perso, ");
		consulta.append(CenColegiadoBean.T_NOMBRETABLA);
		consulta.append(" coleg ");
		consulta.append(" WHERE ");
		
		//joins
		consulta.append("   grupo.");
		consulta.append(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
		consulta.append("       = saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
		
		consulta.append("   AND turno.");
		consulta.append(ScsTurnoBean.C_IDINSTITUCION);
		consulta.append("       = saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION);
		consulta.append("   AND turno.");
		consulta.append(ScsTurnoBean.C_IDTURNO);
		consulta.append("       = saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDTURNO);
		consulta.append("   AND guardia.");
		consulta.append(ScsGuardiasTurnoBean.C_IDINSTITUCION);
		consulta.append("   (+) = saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION);
		consulta.append("   AND guardia.");
		consulta.append(ScsGuardiasTurnoBean.C_IDTURNO);
		consulta.append("   (+) = saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDTURNO);
		consulta.append("   AND guardia.");
		consulta.append(ScsGuardiasTurnoBean.C_IDGUARDIA);
		consulta.append("   (+) = saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA);
		
		consulta.append("   AND grupo.");
		consulta.append(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
		consulta.append("       = grucol.");
		consulta.append(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO);

		consulta.append("   AND perso.");
		consulta.append(CenPersonaBean.C_IDPERSONA);
		consulta.append("       = grucol.");
		consulta.append(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA);
		consulta.append("   AND coleg.");
		consulta.append(CenColegiadoBean.C_IDPERSONA);
		consulta.append("       = grucol.");
		consulta.append(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA);
		consulta.append("   AND coleg.");
		consulta.append(CenColegiadoBean.C_IDINSTITUCION);
		consulta.append("       = grucol.");
		consulta.append(ScsGrupoGuardiaColegiadoBean.C_IDINSTITUCION);
		
		//where
		consulta.append("   AND ");
		consulta.append(filtrosWhere);
		
		//group by
		consulta.append("GROUP BY ");
		consulta.append(" saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_IDINSTITUCION);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_IDTURNO);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO);
		consulta.append(" , saltos.");
		consulta.append(ScsSaltosCompensacionesBean.C_FECHA);
		consulta.append(" , grupo.");
		consulta.append(ScsGrupoGuardiaBean.C_NUMEROGRUPO);
		consulta.append(" , turno.");
		consulta.append(ScsTurnoBean.C_NOMBRE);
		consulta.append(" , guardia.");
		consulta.append(ScsGuardiasTurnoBean.C_NOMBRE);
		consulta.append(" , turno.");
		consulta.append(ScsTurnoBean.C_IDINSTITUCION);
		consulta.append(" , turno.");
		consulta.append(ScsTurnoBean.C_IDTURNO);
		consulta.append(" , saltos.");
		consulta.append(ScsGrupoGuardiaBean.C_IDGUARDIA);
		consulta.append(" , grupo.");
		consulta.append(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
		
		//order
		if (ordenar) {
			consulta.append(" ORDER BY saltos.");
			consulta.append(ScsSaltoCompensacionGrupoBean.C_FECHA);
			consulta.append(" desc");
		}
		
		return consulta.toString();
	}
	
	public Hashtable getSqlYContadorBusquedaSaltosCompensacionesGrupoBind(Hashtable datosEntrada, int contador, Hashtable<Integer, String> codigosBind) 
			throws ClsExceptions
	{
		Hashtable sqlYcontador = new Hashtable();
		StringBuilder where = new StringBuilder();
		
		try { 
			//obteniendo datos desde el Hash
			String idInstitucion = UtilidadesHash.getString(datosEntrada,"IDINSTITUCION");
			String fechaDesde = UtilidadesHash.getString(datosEntrada,"FECHADESDE"); 
			String fechaHasta = UtilidadesHash.getString(datosEntrada,"FECHAHASTA");
			String idTurno = UtilidadesHash.getString(datosEntrada,"IDTURNO");
			String idGuardia = UtilidadesHash.getString(datosEntrada,"IDGUARDIA");
			String idPersona = UtilidadesHash.getString(datosEntrada,"IDPERSONA");
			String idGrupoGuardia = UtilidadesHash.getString(datosEntrada,"IDGRUPOGUARDIA");
			String salto = UtilidadesHash.getString(datosEntrada,"SALTO");
			String compensado = UtilidadesHash.getString(datosEntrada,"COMPENSADO");
			
			//generando where
			where.append(" saltos.");
			where.append(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION);
			where.append("=:");
			contador ++;
			codigosBind.put(new Integer(contador), idInstitucion);
			where.append(contador);
			if (salto!=null) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION);
				where.append("=:");
				contador ++;
				codigosBind.put(new Integer(contador), salto);
				where.append(contador);
			}
			if (!idTurno.equals("")) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_IDTURNO);
				where.append("=:");
				contador ++;
				codigosBind.put(new Integer(contador), idTurno);
				where.append(contador);
			}
			if (!idGuardia.equals("")) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA);
				where.append("=:");
				contador ++;
				codigosBind.put(new Integer(contador), idGuardia);
				where.append(contador);
			}
			if (idGrupoGuardia!=null && !idGrupoGuardia.equals("")) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
				where.append("=:");
				contador ++;
				codigosBind.put(new Integer(contador), idGrupoGuardia);
				where.append(contador);
			}
			if (compensado!=null) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO);
				where.append(compensado.equals("S") ? " > TO_DATE('01/01/2001','DD/MM/YYYY')" : " is null ");
			}
			if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
				where.append(" AND ");
				if (!fechaDesde.equals(""))
					fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
				if (!fechaHasta.equals(""))
					fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);
				Vector contadorYsql = GstDate.dateBetweenDesdeAndHastaBind("saltos."+ScsSaltoCompensacionGrupoBean.C_FECHA, fechaDesde, fechaHasta, contador, codigosBind);
				contador = ((Integer)contadorYsql.get(0)).intValue();
				where.append(contadorYsql.get(1));
			}
			if (idPersona!=null && !idPersona.equals("")) {
				where.append(" AND grupo.");
				where.append(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
				where.append(" in (select col2.");
				where.append(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO);
				where.append("       from ");
				where.append(ScsGrupoGuardiaColegiadoBean.T_NOMBRETABLA);
				where.append(" col2 where col2.");
				where.append(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA);
				where.append("            = ");
				contador ++;
				codigosBind.put(new Integer(contador), idPersona);
				where.append(":");
				where.append(contador);
				where.append(")");
			}
			
			sqlYcontador.put("CODIGOS", contador);
			sqlYcontador.put("SQL", buildConsultaBusquedaSaltosCompensacionesGrupo(where.toString(), false));
		} catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesGrupoAdm.getPaginadorBusquedaSaltosCompensacionesGrupo() en la consulta:"+where.toString());
		}
		return sqlYcontador;
	}
	/** 
	 * Devuelve la consulta SQL de la b�squeda de Turnos, Guardias y Letrados con saltos o compensaciones.
	 * 
	 * @param Hashtable registros: tabla hash con los datos de la pantalla para realizar la busqueda.
	 * @return String: tiene la consulta SQL a ejecutar
	 * @throws ClsExceptions
	 */
	public String getConsultaBusquedaSaltosCompensacionesGrupo(Hashtable datosEntrada) throws ClsExceptions {
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();

		try { 
			//obteniendo datos desde el Hash
			String idInstitucion = UtilidadesHash.getString(datosEntrada,"IDINSTITUCION");
			String fechaDesde = UtilidadesHash.getString(datosEntrada,"FECHADESDE"); 
			String fechaHasta = UtilidadesHash.getString(datosEntrada,"FECHAHASTA");
			String idTurno = UtilidadesHash.getString(datosEntrada,"IDTURNO");
			String idGuardia = UtilidadesHash.getString(datosEntrada,"IDGUARDIA");
			String idPersona = UtilidadesHash.getString(datosEntrada,"IDPERSONA");
			String idGrupoGuardia = UtilidadesHash.getString(datosEntrada,"IDGRUPOGUARDIA");
			String salto = UtilidadesHash.getString(datosEntrada,"SALTO");
			String compensado = UtilidadesHash.getString(datosEntrada,"COMPENSADO");
			
			//generando where
			where.append(" saltos.");
			where.append(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION);
			where.append("=");
			where.append(idInstitucion);
			if (salto!=null) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION);
				where.append("='");
				where.append(salto);
				where.append("'");
			}
			if (!idTurno.equals("")) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_IDTURNO);
				where.append("=");
				where.append(idTurno);
			}
			if (!idGuardia.equals("")) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA);
				where.append("=");
				where.append(idGuardia);
			}
			if (idGrupoGuardia!=null && !idGrupoGuardia.equals("")) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
				where.append("=");
				where.append(idGrupoGuardia);
			}
			if (compensado!=null) {
				where.append(" AND saltos.");
				where.append(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO);
				where.append(compensado.equals("S") ? " > TO_DATE('01/01/2001','DD/MM/YYYY')" : " is null ");
			}
			if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
				where.append(" AND ");
				if (!fechaDesde.equals(""))
					fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
				if (!fechaHasta.equals(""))
					fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);
				where.append(GstDate.dateBetweenDesdeAndHasta("saltos."+ScsSaltoCompensacionGrupoBean.C_FECHA, fechaDesde, fechaHasta));
			}
			if (idPersona!=null && !idPersona.equals("")) {
				where.append(" AND grupo.");
				where.append(ScsGrupoGuardiaBean.C_IDGRUPOGUARDIA);
				where.append(" in (select col2.");
				where.append(ScsGrupoGuardiaColegiadoBean.C_IDGRUPO);
				where.append("       from ");
				where.append(ScsGrupoGuardiaColegiadoBean.T_NOMBRETABLA);
				where.append(" col2 where col2.");
				where.append(ScsGrupoGuardiaColegiadoBean.C_IDPERSONA);
				where.append("            = ");
				where.append(idPersona);
				where.append("    )");
			}
			sql.append(buildConsultaBusquedaSaltosCompensacionesGrupo(where.toString(), true));
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesGrupoAdm.getConsultaBusquedaSaltosCompensacionesGrupo() en la consulta:"+where.toString());
		}
		return sql.toString();
	}

	public String selectSaltosCompensaciones(Hashtable registros) throws ClsExceptions {
		String consulta = "";
		try{
			consulta  = "SELECT ID1, ID2, ID3, ID4, NUMERO , COUNT(1) REP from (";
			consulta +=  this.getConsultaBusquedaSaltosCompensacionesGrupo(registros);
			consulta += ")";
			consulta += "  GROUP BY numero, ID1, ID2, ID3, ID4";
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsSaltosCompensacionesGrupoAdm.buscar() en la consulta:"+consulta);
		}
		return consulta;
	}	
	
	public Vector selectDatosColaGuardiaSYC(String consulta) throws ClsExceptions {
		Vector datos = new Vector();

		// Acceso a BBDD
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					if (registro != null){
						String idInstitucion =  (String) registro.get("ID1");
						String idTurno = (String) registro.get("ID2"); 
						String idGuardia = (String) registro.get("ID3"); 
						String idGrupoGuardia = (String) registro.get("ID4");
						registro.put("LETRADO",getInfoLetradosGrupoGuardia(idInstitucion, idTurno, idGuardia, idGrupoGuardia));
						datos.add(registro);
					}
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en ScsSaltoCompensacionGrupoAdm.selectGenerico(). Consulta SQL:"
					+ consulta);
		}
		return datos;
	}	
	
	public String getInfoLetradosGrupoGuardia(String idInstitucion, String idTurno, String idGuardia, String idGrupoGuardia) throws ClsExceptions {
		Vector datos = new Vector();
		String letrados = "";
		String consulta = "   SELECT (DECODE(col.COMUNITARIO,'1',col.NCOMUNITARIO, col.NCOLEGIADO) || ' - ' || perso.APELLIDOS1 || ' ' || perso.APELLIDOS2 || ', ' || perso.NOMBRE) AS LETRADO   "+
						  "   FROM cen_persona perso,cen_colegiado col, scs_grupoguardiacolegiado ggc   "+
						  "   WHERE perso.idpersona = ggc.idpersona   "+
						  "     AND col.idpersona = perso.idpersona   "+
						  "     AND col.idinstitucion = ggc.idinstitucion   "+
						  "     AND ggc.idInstitucion = " + idInstitucion +
						  "     AND ggc.idturno = " + idTurno +
						  "     AND ggc.idguardia = " + idGuardia +
						  "     AND ggc.idgrupoguardia = " + idGrupoGuardia +
						  "   ORDER BY perso.APELLIDOS1 ";
								
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
			throw new ClsExceptions(e, "Excepcion en ScsSaltoCompensacionGrupoAdm.getLetrados. Consulta SQL:"
					+ consulta);
		}
		
		for(int i = 0; i<datos.size(); i++){
			Hashtable reg = (Hashtable) datos.get(i);
			letrados = letrados + reg.get("LETRADO") + "; \n";
		}
		
		return letrados;
	}
	
	
}