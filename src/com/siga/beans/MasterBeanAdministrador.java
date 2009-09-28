/*
 * Created on 20-oct-2004
 *
 */
package com.siga.beans;


import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 * 
 * MasterBeanAdministrador
 * Clase maestra para el manejo de la clase MasterBean. Esta clase sera la encargada de realizar las sentencias sql.
 *
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 

 */
public abstract class MasterBeanAdministrador {

	private   String  error = "";
	protected String  nombreTabla = "";
	protected UsrBean usrbean = null;
	protected Integer usuModificacion;

	MasterBeanAdministrador (String _tabla, UsrBean _usrBean) {
		try {
		    if (_usrBean!=null) {
			    if (_usrBean.getUserName()==null || _usrBean.getUserName().trim().equals("")) {
			        _usrBean.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
			    }
				this.usrbean = _usrBean;
				this.usuModificacion =  new Integer (this.usrbean.getUserName());
		    }
			this.nombreTabla = _tabla;
		    // esto es que si no me meten el userben, entonces va a fallar cada vez que actualice 
		    // y cada vez que quiera usar el lenguaje. De tal modo que controlemos cuando es necesario.
		    // Raul y Daniel, a dia 11/10/2007
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	protected Integer getIDInstitucion() {
		return new Integer(this.usrbean.getLocation());
	}
	
	protected String getLenguaje() {
		return (String) this.usrbean.getLanguage();
	}
	
	
	/** Funcion select
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector select() throws ClsExceptions {
		return this.select("");		
	}
	
	
	
	/** Funcion select
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectForUpdate() throws ClsExceptions {
		return this.selectForUpdate("");		
	}
	
	/** Funcion select (String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
	
	public Vector selectBind(String where, Hashtable codigos ) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
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
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
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
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
	
	public Vector findGenericoBind(String sql, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
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
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
	
	public Vector selectNLSBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryNLSBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
	
	
	private Vector selectForUpdateBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryForUpdateBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
	
	

	/** Funcion selectNLS Ignora mayusculas, acentos...(String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectNLS(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}
	public Vector selectGenericoNLS(String sql) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			if (rc.queryNLS(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, e.getMessage()); 
		}
		return datos;
	}


	
	/** Funcion select (String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectForUpdate(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
//			if (rc.query(sql)) {
			if (rc.findForUpdate(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e,  e.getMessage()); 
		}
		return datos;
	}

	
	/** Funcion select (Hashtable hash)
	 * @param hashTable con las parejas campo-valor para realizar un where en el select
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector select(Hashtable hash) throws ClsExceptions
	{
	    return selectBind(hash);
	    /*
	    Vector vector = new Vector();
		
		try {
			int i = 0;
			String campos[] = new String[hash.size()];
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				campos[i] = (String) e.nextElement();
				i++;
			}
			
			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, campos);
//			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, this.getClavesBean());
			vector = this.select(where);
		}
		catch (Exception e) {
			vector = null;		
			throw new ClsExceptions(e, "Error al ejecutar el \"select\" en B.D.");
		}
		return vector;
		*/
	}
	
	private Vector selectBind(Hashtable hash) throws ClsExceptions
	{
		Vector vector = new Vector();
		
		try {
			int i = 0;
			String campos[] = new String[hash.size()];
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				campos[i] = (String) e.nextElement();
				i++;
			}
			
			Vector resultado = UtilidadesBDAdm.sqlWhereBind(this.nombreTabla, hash, campos);
//			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, this.getClavesBean());
			vector = this.selectGenericaBind((String)resultado.get(0),(Hashtable)resultado.get(1));
		}
		catch (Exception e) {
			vector = null;		
			throw new ClsExceptions(e, e.getMessage());
		}
		return vector;
	}
	
	private Vector selectNLSBind(Hashtable hash) throws ClsExceptions
	{
		Vector vector = new Vector();
		
		try {
			int i = 0;
			String campos[] = new String[hash.size()];
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				campos[i] = (String) e.nextElement();
				i++;
			}
			
			Vector resultado = UtilidadesBDAdm.sqlWhereBind(this.nombreTabla, hash, campos);
//			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, this.getClavesBean());
			vector = this.selectGenericaBind((String)resultado.get(0),(Hashtable)resultado.get(1));
		}
		catch (Exception e) {
			vector = null;		
			throw new ClsExceptions(e,  e.getMessage());
		}
		return vector;
	}

	public Vector selectNLS(Hashtable hash) throws ClsExceptions	{
	    return selectNLSBind(hash);
	}

	/** Funcion select (Hashtable hash)
	 * @param hashTable con las parejas campo-valor para realizar un where en el select
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectForUpdate(Hashtable hash) throws ClsExceptions	{
	    return selectForUpdateBind(hash);
		/*
	    Vector vector = new Vector();
		
		try {
			int i = 0;
			String campos[] = new String[hash.size()];
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				campos[i] = (String) e.nextElement();
				i++;
			}
			
			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, campos);
//			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, this.getClavesBean());
			vector = this.selectForUpdate(where);
		}
		catch (Exception e) {
			vector = null;		
			throw new ClsExceptions(e, "Error al ejecutar el \"select\" en B.D.");
		}
		return vector;
		*/
	}

	private Vector selectForUpdateBind(Hashtable hash) throws ClsExceptions	{
		Vector vector = new Vector();
		
		try {
			int i = 0;
			String campos[] = new String[hash.size()];
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				campos[i] = (String) e.nextElement();
				i++;
			}
			
			Vector resultado = UtilidadesBDAdm.sqlWhereBind(this.nombreTabla, hash, campos);
//			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, this.getClavesBean());
			vector = this.selectForUpdateBind((String)resultado.get(0),(Hashtable)resultado.get(1));
		}
		catch (Exception e) {
			vector = null;		
			throw new ClsExceptions(e, e.getMessage());
		}
		return vector;
	}

	/** Funcion selectByPK (Hashtable hash)
	 * @param hashTable con las parejas campo-valor para realizar un where en el select por todas las claves primarias
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectByPK(Hashtable hash) throws ClsExceptions
	{
	    return selectByPKBind(hash);
	    /*
		try {
			String [] claves = this.getClavesBean();
			
			Hashtable aux = new Hashtable();
			for (int i = 0; i < claves.length; i++) {
				aux.put((String)claves[i], hash.get((String)claves[i]));
			}
			return this.select(aux);
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el \"select\" en B.D.");
		}
		*/
	}	
	
	private Vector selectByPKBind(Hashtable hash) throws ClsExceptions
	{
		try {
			String [] claves = this.getClavesBean();
			
			Hashtable aux = new Hashtable();
			for (int i = 0; i < claves.length; i++) {
				aux.put((String)claves[i], hash.get((String)claves[i]));
			}
			return this.selectBind(aux);
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
	}	
	
	
	/** Funcion selectByPK (Hashtable hash)
	 * @param hashTable con las parejas campo-valor para realizar un where en el select por todas las claves primarias
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectByPKForUpdate(Hashtable hash) throws ClsExceptions
	{
	    return selectByPKForUpdateBind(hash);
		/*
	    try {
			String [] claves = this.getClavesBean();
			
			Hashtable aux = new Hashtable();
			for (int i = 0; i < claves.length; i++) {
				aux.put((String)claves[i], hash.get((String)claves[i]));
			}
			return this.selectForUpdate(aux);
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el \"select\" en B.D.");
		}
		*/
	}
	
	private Vector selectByPKForUpdateBind(Hashtable hash) throws ClsExceptions
	{
		try {
			String [] claves = this.getClavesBean();
			
			Hashtable aux = new Hashtable();
			for (int i = 0; i < claves.length; i++) {
				aux.put((String)claves[i], hash.get((String)claves[i]));
			}
			return this.selectForUpdateBind(aux);
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param select
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector selectGenerico(String select) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage() + "Consulta SQL:"+select);
		}
		return datos;	
	}
	
	public Vector selectGenericoBind(String select, Hashtable codigos) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage() + " Consulta SQL:"+select);
		}
		return datos;	
	}
	public Vector selectGenericoNLSBind(String select, Hashtable codigos) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryNLSBind(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage() + " Consulta SQL:"+select);
		}
		return datos;	
	}
	
	public Vector selectGenericoBindHashVacio(String select, Hashtable codigos) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.findBindHashVacio(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage()+" Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/** Funcion insert (MasterBean bean)
	 *  @param bean a insertar
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insert(MasterBean bean) throws ClsExceptions{
		try {
			return this.insert(this.beanToHashTable(bean));
		}
		catch (Exception e)	{
			throw new ClsExceptions (e,  e.getMessage());
		}
	}

	/** Funcion insert (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el insert 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insert(Hashtable hash) throws ClsExceptions{
		try
		{
			// Establecemos las datos de insercion
			this.setModificacion(hash);

			Row row = new Row();
			row.load(hash);

			//String [] campos = this.getCamposBean();
			String [] campos = this.getCamposActualizablesBean();
			
			if (row.add(this.nombreTabla, campos) == 1) {
				return true;
			}
			else {
				this.error = "Error al insertar el elemento en BD";
			}
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}
	
	/** Funcion update (MasterBean bean)
	 *  @param bean a actualizar
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean update(MasterBean bean) throws ClsExceptions{
		try {
			if(bean.getOriginalHash() == null) {
				return false;
			}
			return this.update(this.beanToHashTable(bean), bean.getOriginalHash());
		}
		catch (Exception e)	{
			throw new ClsExceptions (e,  e.getMessage());
		}
	}

	/** Funcion update (MasterBean beanNew, MasterBean beanOld)
	 *  @param bean a actualizar
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean update(MasterBean beanNew, MasterBean beanOld) throws ClsExceptions{
		try {
			return this.update(this.beanToHashTable(beanNew), this.beanToHashTable(beanOld));
		}
		catch (Exception e)	{
			throw new ClsExceptions (e,  e.getMessage());
		}
	}

	/** Funcion update (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el update 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	
	public boolean updateDirect(Hashtable hash, String[] claves, String [] campos) throws ClsExceptions {
		try {

//			String [] campos = this.getCamposActualizablesBean();
//			String [] claves = this.getClavesBean();
			if (claves==null) {
				claves = this.getClavesBean();
			}
			if (campos==null)  {
				campos = this.getCamposActualizablesBean();
//			} else {
//				campos = this.setModificacion(campos);				
			}
			Row row = new Row();	
			
			// Establecemos las datos de modificacion
			this.setModificacion(hash);

			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hash);
			
			if (row.updateDirect(this.nombreTabla, claves, campos) >= 0) {				
				return true;
			}
			else {
				this.error = "Error al actualizar el elemento en BD";
			}

		}
		catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());	
		}
		return false;	}


	/** Funcion update (MasterBean bean)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el update 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	
	public boolean updateDirect(MasterBean bean, String[] claves, String [] campos) throws ClsExceptions {
		try {

			Hashtable hash = this.beanToHashTable(bean);
//			String [] campos = this.getCamposActualizablesBean();
//			String [] claves = this.getClavesBean();
			if (claves==null) {
				claves = this.getClavesBean();
			}
			if (campos==null)  {
				campos = this.getCamposActualizablesBean();
			}
			Row row = new Row();	
			
			// Establecemos las datos de modificacion
			this.setModificacion(hash);

			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hash);
			
			if (row.updateDirect(this.nombreTabla, claves, campos) >= 0) {				
				return true;
			}
			else {
				this.error = "Error al actualizar el elemento en BD";
			}

		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());	
		}
		return false;	
	}
	
	public boolean updateDirect(MasterBean bean) throws ClsExceptions {
		try {
			return updateDirect (bean, this.getClavesBean(), this.getCamposBean());	
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, e.getMessage());
		}
		
	}

	/** Funcion update (Hashtable hashDataNew, Hashtable hashDataOld)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el update 
	 *  @return true -> OK false -> Error 
	 * */
	
	public boolean update(Hashtable hashDataNew, Hashtable hashDataOld) throws ClsExceptions {

		try {

			String [] campos = this.getCamposActualizablesBean();
			String [] claves = this.getClavesBean();

			Row row = new Row();	
			
			// Establecemos las datos de modificacion
			this.setModificacion(hashDataNew);

			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hashDataNew);
			
			row.setCompareData(hashDataOld);
			
			if (row.update(this.nombreTabla, claves, campos) == 1) {				
				return true;
			}
			else {
				this.error = "Error al actualizar el elemento en BD";
			}

/*			// Cargamos el registro nuevo el que tiene las modificaciones
			row.load(hash); 
			
			if (row.updateDirect(this.nombreTabla, claves, campos) == 1) {				
				return true;
			}
			else {
				this.error = "Error al actualizar el elemento en BD";
			}
*/
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());	
		}
		return false;
	}
	
	/** Funcion delete (MasterBean bean)
	 *  @param bean a borrar
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean delete(MasterBean bean) throws ClsExceptions{
		try {
			return this.delete(this.beanToHashTable(bean));
		}
		catch (Exception e)	{
		    this.error=e.toString();
			throw new ClsExceptions (e, "Error al ejecutar el \"delete\" en B.D.");
		}
	}

	/** Funcion delete (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el delete 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean delete(Hashtable hash) throws ClsExceptions{

		try {
			Row row = new Row();	
			row.load(hash);

			String [] claves = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, claves) == 1) {
				return true;
			}
			else{
				this.error = "Error al borrar el elemento en BD";
			}
		}
		catch (Exception e) {
		    this.error=e.toString();
			e.printStackTrace();
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}

	/** Funcion delete (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el delete 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean deleteDirect(Hashtable hash, String[] claves) throws ClsExceptions{

		try {
			Row row = new Row();	
			row.load(hash);
			if (claves==null) {
				claves = this.getClavesBean();
			}
			
			if (row.delete(this.nombreTabla, claves) >= 0) {
				return true;
			}
			else{
				this.error = "No se ha encontrado el elemento a borrar";
			}
		}
		catch (Exception e) {
		    this.error=e.toString();
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}

	public boolean deleteSQL(String sql) throws ClsExceptions{

		try {
			Row row = new Row();	
			
			if (row.deleteSQL(sql) >= 0) {
				return true;
			}
			else{
				this.error = "Error al borrar el elemento en BD";
			}
		}
		catch (Exception e) {
		    this.error = e.toString();
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}
	public boolean updateSQL(String sql) throws ClsExceptions{

		try {
			Row row = new Row();	
			
			if (row.updateSQL(sql) >= 0) {
				return true;
			}
			else{
				this.error = "Error al modificar el elemento en BD";
			}
		}
		catch (Exception e) {
		    this.error = e.toString();
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}

	public boolean 	insertSQL(String sql) throws ClsExceptions{

		try {
			Row row = new Row();	
			
			row.insertSQL(sql);
		}
		catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
		return true;
	}

	public boolean 	insertSQLBind(String sql, Hashtable codigos) throws ClsExceptions{

		try {
			Row row = new Row();	
			
			row.insertSQLBind(sql,codigos);
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
		return true;
	}

	
	/** 
	 *  Funcion setModificacion 
	 *  Funcion que fija los parametros de modificacion, usuario y fecha
	 **/
	private String[] setModificacion(String[] campos) throws ClsExceptions {		
		String[] camposNew = null;
		
		if (campos != null) {
			
			List listaCampos = new ArrayList();
			boolean tieneUsu = false;
			boolean tieneFecha = false; 
			
			int i = 0;
			for (i = 0; i < campos.length; i++) {
				listaCampos.add(campos[i]); //copiamos los campos del array origen
				if (campos[i] != null) {
					if (campos[i].trim().equals(MasterBean.C_USUMODIFICACION)){
						tieneUsu = true;
					} else if (campos[i].trim().equals(MasterBean.C_FECHAMODIFICACION)){
						tieneFecha = true;
					}
				}
			}
			
			if (!tieneUsu) {
				listaCampos.add(MasterBean.C_USUMODIFICACION);
			}
			
			if (!tieneFecha) {
				listaCampos.add(MasterBean.C_FECHAMODIFICACION);
			}
			
			camposNew = new String[listaCampos.size()];
			for (i = 0; i < listaCampos.size(); i++) {
				camposNew[i] = (String)listaCampos.get(i);
			}
						
		}
		return camposNew;
	}
	
	
	/** Funcion setModificacion 
	 *  Funcion que fija los parametros de modificacion, usuario y fecha
	 * */
	private void setModificacion(Hashtable hash) throws ClsExceptions {
		try {
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
	}
	
	/** Funcion getError()
	 *  @return el error si existe 
	 * */
	public String getError() {
		return this.error;
	}
	
	/** Funcion setError()
	 *  @param mensaje de error 
	 * */
	public void setError(String mensaje) {
		this.error=mensaje;
	}
	
	/** Funcion getTipoDatosTabla()
	 *  @return Un hashTAble con el tipo de dato de los campos del bean 
	 * */
	private Hashtable getTipoDatosTabla (String tabla) {
		Hashtable tipos = new Hashtable();
		return tipos;		
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable que representa el bean
	 * */
	protected MasterBean hashTableToBeanInicial (Hashtable hash) throws ClsExceptions {
		MasterBean bean=hashTableToBean(hash);
		bean.setOriginalHash(hash);
		return bean;
	}

	/* ********************************************************************************** */
	/*            Metodos abstractos que hay que definir en el clases hijas               */
	/* ********************************************************************************** */

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	protected abstract String[] getCamposBean ();

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected abstract String[] getClavesBean ();

	/** Funcion getCamposActualizablesBean ()
	 *  @return conjunto de datos con los nombres de los campos actualizables
	 * */
	protected String[] getCamposActualizablesBean ()
	{
	    return getCamposBean();
	}

	/** Funcion getOrdenCampos ()
	 *  @return conjunto de datos con los nombres de campos para los criterios de ordenacion
	 * */
	protected abstract String[] getOrdenCampos ();

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable que representa el bean
	 * */
	protected abstract MasterBean hashTableToBean (Hashtable hash) throws ClsExceptions;

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param hashtable para crear el bean asociado
	 *  @return bean que representa el hashtable
	 * */
	protected abstract Hashtable beanToHashTable (MasterBean bean) throws ClsExceptions;

	/** 
	 * realiza una consulta libre. Si se ha instanciado la clase en modo visibilidad primero modifica la query
	 * @param sql con la consulta en el formato adecuado
	 * @return RowsContainer con el resultado 
	 */
	public RowsContainer find(String sql) throws ClsExceptions	{
		
		RowsContainer rc = new RowsContainer(); 
        rc.find(sql);
       	return rc;
	}

	public RowsContainer findBind(String sql, Hashtable codigos) throws ClsExceptions	{
		
		RowsContainer rc = new RowsContainer(); 
        rc.findBind(sql, codigos);
       	return rc;
	}

	/** 
	 * realiza una consulta libre FOR UPDATE. Si se ha instanciado la clase en modo visibilidad primero modifica la query
	 * @param sql con la consulta en el formato adecuado
	 * @return RowsContainer con el resultado 
	 */
	public RowsContainer findForUpdate(String sql) throws ClsExceptions	{
		
		RowsContainer rc = new RowsContainer(); 
        rc.findForUpdate(sql);
       	return rc;
	}

	public RowsContainer findNLS(String sql) throws ClsExceptions	{
		
		RowsContainer rc = new RowsContainer(); 
        rc.findNLS(sql);
       	return rc;
	}


}
