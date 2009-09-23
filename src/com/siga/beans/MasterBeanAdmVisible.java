/*
 * Created on 11-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.general.*;


/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class MasterBeanAdmVisible extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	
//	protected UsrBean usrbean=null;
	protected int idInstitucionCliente=-1;
	protected long idPersonaCliente=-1;
	
	public MasterBeanAdmVisible(String tabla, Integer usuario, UsrBean _usrbean, int _idInstitucionCliente, long _idPersonaCliente) {
		super(tabla, _usrbean);
//		usrbean=_usrbean;
		idInstitucionCliente=_idInstitucionCliente;
		idPersonaCliente=_idPersonaCliente;
	}
	
	
	public MasterBeanAdmVisible(String tabla, UsrBean _usrbean) {
		super(tabla, _usrbean);
		//usrbean=_usrbean;
		idInstitucionCliente=new Integer((_usrbean!=null && _usrbean.getLocation()!=null)?_usrbean.getLocation():"-1").intValue();
		idPersonaCliente=new Long((_usrbean!=null && _usrbean.getUserName()!=null)?_usrbean.getUserName():"-1").longValue();
	}  
	
	/** Funcion select (String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector select(String where) throws ClsExceptions	{
		if (this.usrbean==null) {
			return super.select(where);
		}
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);
			
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
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;

	}

	public Vector selectBind(String where,Hashtable codigos) throws ClsExceptions	{
		if (this.usrbean==null) {
			return super.selectBind(where,codigos);
		}
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);
			
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
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;

	}

	public Vector selectNLSBind(String where,Hashtable codigos) throws ClsExceptions	{
		if (this.usrbean==null) {
			return super.selectBind(where,codigos);
		}
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);
			
			if (rc.queryNLSBind(sql,codigos)) {
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

	/** Funcion select (String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectForUpdate(String where) throws ClsExceptions	{
		if (this.usrbean==null) {
			return super.selectForUpdate(where);
		}
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);			
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
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}

	/** 
	 * realiza una consulta libre. Si se ha instanciado la clase en modo visibilidad primero modifica la query
	 * @param sql con la consulta en el formato adecuado
	 * @return RowsContainer con el resultado 
	 */
	public RowsContainer find(String sql) throws ClsExceptions	{
		
		if (this.usrbean!=null) {
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);
		}
		RowsContainer rc = new RowsContainer(); 
        rc.find(sql);
       	return rc;
	}

	public RowsContainer findNLS(String sql) throws ClsExceptions	{
		
		if (this.usrbean!=null) {
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);
		}
		RowsContainer rc = new RowsContainer(); 
        rc.findNLS(sql);
       	return rc;
	}

	/** 
	 * Comprueba la visibilidad para un campo s de uns tabla, de manera puntual
	 * @param tabla Con el nombre de la tabla
	 * @param campo Con el nombre del campo
	 * @return boolean con el resultado (true si se puede mostrar)
	 */
	public boolean compruebaVisibilidadCampo(String tabla, String campo) throws ClsExceptions	{
		boolean salida = true;
		if (this.usrbean!=null) {

			if (usrbean.getLocation()!=null &&  usrbean.getLocation().equalsIgnoreCase(new Integer(idInstitucionCliente).toString())) return salida;
			
			CenClienteAdm cenClienteAdm=new CenClienteAdm(this.usrbean);
			Vector vec=cenClienteAdm.select(" where " + CenClienteBean.C_IDINSTITUCION +"=" + idInstitucionCliente +
					" AND " + CenClienteBean.C_IDPERSONA + "=" + idPersonaCliente);
			
			if (vec!=null && vec.size()!=0) {
				CenClienteBean clienteBean=(CenClienteBean)vec.elementAt(0);
				String caracter=clienteBean.getCaracter();
				if (caracter==ClsConstants.TIPO_CARACTER_PRIVADO) {
					throw new ClsExceptions("MasterBeanAdmVisible::modificarQuery -> HAS LLEGADO A UN PUNTO AL CUAL NO PODIAS LLEGAR... REVISAR EL CODIGO");
				}

				salida = CenVisibilidad.esVisibleCampo(tabla, campo, caracter);	

			}
		}
       	return salida;
	}


	/** 
	 * realiza una consulta libre for update. Si se ha instanciado la clase en modo visibilidad primero modifica la query
	 * @param sql con la consulta en el formato adecuado
	 * @return RowsContainer con el resultado 
	 */
	public RowsContainer findForUpdate(String sql) throws ClsExceptions	{
		
		if (this.usrbean!=null) {
			sql=modificarQuery(usrbean.getLocation(), ""+idInstitucionCliente,""+idPersonaCliente,sql);
		}
		RowsContainer rc = new RowsContainer(); 
		
		rc.findForUpdate(sql);
       	return rc;
	}
	
	protected String modificarQuery(String idInstitucion, String idInstitucionCliente,String idPersonaCliente,
			String sql) throws ClsExceptions {
		
		if (idInstitucion!=null &&  idInstitucion.equalsIgnoreCase(idInstitucionCliente)) return sql;
		CenClienteAdm cenClienteAdm=new CenClienteAdm(this.usrbean);
		Vector vec=cenClienteAdm.select(" where " + CenClienteBean.C_IDINSTITUCION +"=" + idInstitucionCliente +
				" AND " + CenClienteBean.C_IDPERSONA + "=" + idPersonaCliente);
		
		if (vec!=null && vec.size()!=0) {
			CenClienteBean clienteBean=(CenClienteBean)vec.elementAt(0);
			String caracter=clienteBean.getCaracter();
			if (caracter==ClsConstants.TIPO_CARACTER_PRIVADO) {
				throw new ClsExceptions("MasterBeanAdmVisible::modificarQuery -> HAS LLEGADO A UN PUNTO AL CUAL NO PODIAS LLEGAR... REVISAR EL CODIGO");
			}

			
			// RGG 17-02-2004 CAMBIO PARA PROCESAR LAS UNIONES CADA UNA INDEPENDIENTEMENTE
			String enQuery = "";
			String uniones[] = sql.split("UNION");
			if (uniones!=null) {
				for (int i=0;i<uniones.length;i++) {
					if (i>0) {
						enQuery += " UNION ";
					}
					enQuery += CenVisibilidad.parseSql(uniones[i],caracter);
				}
			}
			// RGG 17-02-2005 CAMBIO: String enQuery=CenVisibilidad.parseSql(sql,caracter);
			//System.out.println("QUERY FILTRADA ------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			//System.out.println(enQuery);
			//System.out.println("QUERY FILTRADA <<<<<<<<<<<<<<<<<<<-------------------------------");
			return enQuery;
		}
		throw new ClsExceptions("MasterBeanAdmVisible::modificarQuery -> Cliente/Institucion no existe " + idPersonaCliente +"/"+idInstitucionCliente);
	}

}
