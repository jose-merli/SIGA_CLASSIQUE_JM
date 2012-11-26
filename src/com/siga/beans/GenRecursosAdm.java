
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla GEN_RECURSOS
 * 
 * @author carlos.vidal
 * @since 1/11/2004 
 */

public class GenRecursosAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public GenRecursosAdm (UsrBean usuario) {
		super( GenRecursosBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposTabla ()
	 * 
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposTabla(){
		String[] campos = {	GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDRECURSO+" "+GenRecursosBean.C_IDRECURSO,
							GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_DESCRIPCION+" "+GenRecursosBean.C_DESCRIPCION,
							GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_ERROR+" "+GenRecursosBean.C_ERROR,					
							GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDLENGUAJE+" "+GenRecursosBean.C_IDLENGUAJE,
							GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDPROPIEDAD+" "+GenRecursosBean.C_IDPROPIEDAD,
							GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDPROPIEDAD+" "+GenRecursosBean.C_FECHAMODIFICACION,
							GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDPROPIEDAD+" "+GenRecursosBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	GenRecursosBean.C_IDRECURSO,	GenRecursosBean.C_DESCRIPCION,
							GenRecursosBean.C_ERROR,		GenRecursosBean.C_IDLENGUAJE,
							GenRecursosBean.C_IDPROPIEDAD,	GenRecursosBean.C_FECHAMODIFICACION,
							GenRecursosBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	GenRecursosBean.C_IDRECURSO, GenRecursosBean.C_IDLENGUAJE};
		return campos;
	}
	
	/** Funcion getClavesTabla ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 *  con formato "NombreTabla.NombreCampo"
	 * 
	 */
	protected String[] getClavesTabla() {
		String[] campos = {	GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDRECURSO, 	
				GenRecursosBean.T_NOMBRETABLA+"."+GenRecursosBean.C_IDLENGUAJE};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenRecursosBean bean = null;
		try{
			bean = new GenRecursosBean();
			bean.setIdRecurso((String)hash.get(GenRecursosBean.C_IDRECURSO));
			bean.setDescripcion((String)hash.get(GenRecursosBean.C_DESCRIPCION));
			bean.setError(Integer.valueOf((String)hash.get(GenRecursosBean.C_ERROR)));
			bean.setIdLenguaje((String)hash.get(GenRecursosBean.C_IDLENGUAJE));
			bean.setIdPropiedad((String)hash.get(GenRecursosBean.C_IDPROPIEDAD));
			bean.setFechaMod((String)hash.get(GenRecursosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(Integer.valueOf((String)hash.get(GenRecursosBean.C_USUMODIFICACION)));
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
		try
		{
			hash = new Hashtable();
			GenRecursosBean b = (GenRecursosBean) bean;
			hash.put(GenRecursosBean.C_IDRECURSO	,String.valueOf(b.getIdRecurso()));
			hash.put(GenRecursosBean.C_DESCRIPCION	,String.valueOf(b.getDescripcion()));
			hash.put(GenRecursosBean.C_ERROR		,String.valueOf(b.getError()));
			hash.put(GenRecursosBean.C_IDLENGUAJE	,String.valueOf(b.getIdLenguaje()));
			hash.put(GenRecursosBean.C_IDPROPIEDAD	,String.valueOf(b.getIdPropiedad()));
			hash.put(GenRecursosBean.C_FECHAMODIFICACION,String.valueOf(b.getFechaMod()));
			hash.put(GenRecursosBean.C_USUMODIFICACION	,String.valueOf(b.getUsuMod()));
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
	protected String[] getOrdenCampos(){
		return null;
	}
	
	/** Funcion select(String where)
	 *	@param where clausula "where" de la sentencia "select" que queremos ejecutar
	 *  @return Vector todos los registros que se seleccionen en BBDD 
	 *  
	 *
	 */
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposTabla());
			sql += where;
	
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}
	
	/**
	 * Efectúa un SELECT contra la tabla 
	 * 
	 * @param sql. Consulta a realizar
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectTabla(String sql){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
	
	public Vector selectTablaBind(String sql, Hashtable codigos){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			if (rc.queryBind(sql,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
	
	public Vector selectPorDescripcion (String descripcionBusqueda, String idiomaATraducir, String idiomaUsuario) throws ClsExceptions 
	{
		Vector v = new Vector ();
		try {
			int numMaxRegistros = 0;
			try {
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	        	//ReadProperties rp = new ReadProperties("SIGA.properties");
	        	String numMaxReg = rp.returnProperty("certificados.numMaxRegistros");
	        	numMaxRegistros = Integer.parseInt(numMaxReg);
			}
			catch (Exception e) {}

			String sql0 = " SELECT COUNT(*) TAM FROM " + GenRecursosBean.T_NOMBRETABLA + " R " + 
					     " WHERE R." + GenRecursosBean.C_IDLENGUAJE + " = '" + idiomaUsuario + "'";
			
			if (descripcionBusqueda != null && !descripcionBusqueda.equals("")) {
				sql0 += " AND " + ComodinBusquedas.prepararSentenciaCompleta(descripcionBusqueda, "R." + GenRecursosBean.C_DESCRIPCION);  
			}

			RowsContainer rc0 = this.findNLS(sql0);
			if (rc0.size() > 0) {
				Row fila = (Row) rc0.get(0);
				Hashtable h = fila.getRow(); 
				if (h != null){ 
					Integer tam = UtilidadesHash.getInteger(h, "TAM");
					if (tam.intValue() > numMaxRegistros) {
						throw new ClsExceptions ("messages.certificados.numMaxReg");
					}
				}
			}

			String sql = " SELECT " + GenRecursosBean.C_IDRECURSO + ", " + 
									  GenRecursosBean.C_DESCRIPCION + ", " +
					                "(SELECT " + GenRecursosBean.C_DESCRIPCION + 
									  " FROM " + GenRecursosBean.T_NOMBRETABLA + 
									 " WHERE " + GenRecursosBean.C_IDRECURSO + " = R." + GenRecursosBean.C_IDRECURSO + 
									   " AND " + GenRecursosBean.C_IDLENGUAJE + " = TRIM('" + idiomaATraducir + "') ) DESCRIPCION_TRADUCIR, " +
									 " TRIM('" + idiomaUsuario + "') IDIOMA, " + 
									 " TRIM('" +idiomaATraducir + "') IDIOMA_TRADUCIR, " +
									 GenRecursosBean.C_IDRECURSO + " AYUDA " + 
						   " FROM " + GenRecursosBean.T_NOMBRETABLA + " R " + 
						  " WHERE R." + GenRecursosBean.C_IDLENGUAJE + " = '" + idiomaUsuario + "'";
			
			if (descripcionBusqueda != null && !descripcionBusqueda.equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(descripcionBusqueda, "R." + GenRecursosBean.C_DESCRIPCION);  
			}
			
			sql += " ORDER BY R." + GenRecursosBean.C_DESCRIPCION + ", " + GenRecursosBean.C_IDRECURSO;
			
			RowsContainer rc = this.findNLS(sql);
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				Hashtable h = fila.getRow(); 
				if (h != null){ 
					v.add(h);
				}
			}
		}
		
		catch (ClsExceptions e1) {
			throw e1;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
	
	public boolean updateDirect(MasterBean bean, String[] claves, String [] campos) throws ClsExceptions {
		try {

			Hashtable hash = this.beanToHashTable(bean);
			if (claves==null) {
				claves = this.getClavesBean();
			}
			if (campos==null)  {
				campos = this.getCamposActualizablesBean();
			}
			Row row = new Row();	
			
			// Establecemos las datos de modificacion
			// Establecemos las datos de modificacion
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");

			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hash);
			
			int filas = row.updateDirect(this.nombreTabla, claves, campos);
			if (filas > 0) {
				return true;
			}
			if (filas < 0) {
				return false;
			}
			
			// filas == 0
			// Si no esta el registro lo insertamos
			String where = " WHERE " + GenRecursosBean.C_IDRECURSO + " = '" + ((GenRecursosBean)bean).getIdRecurso() + "'" +
						   " AND rownum = 1 ";
			Vector v = this.select(where);
			if (v == null || v.size() < 1) {
				return false;
			}
			GenRecursosBean bInsert = (GenRecursosBean) v.get(0);
			bInsert.setDescripcion  (((GenRecursosBean)bean).getDescripcion());
			bInsert.setIdLenguaje   (((GenRecursosBean)bean).getIdLenguaje());
			return this.insert(bInsert);

		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el \"update\" en B.D.");	
		}
	}
}