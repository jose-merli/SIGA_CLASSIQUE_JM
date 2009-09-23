/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
 * Administrador del Bean de la tabla ConOperacionConsulta
 */
public class ConOperacionConsultaAdm extends MasterBeanAdministrador {

	public ConOperacionConsultaAdm(UsrBean usuario)
	{
	    super(ConOperacionConsultaBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConOperacionConsultaBean.C_IDOPERACION,
				ConOperacionConsultaBean.C_DESCRIPCION,
				ConOperacionConsultaBean.C_TIPOOPERADOR,
				ConOperacionConsultaBean.C_SIMBOLO,
				ConOperacionConsultaBean.C_FECHAMODIFICACION,
				ConOperacionConsultaBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConOperacionConsultaBean.C_IDOPERACION};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ConOperacionConsultaBean bean = null;

		try
		{
			bean = new ConOperacionConsultaBean();
						
			bean.setIdOperacion(UtilidadesHash.getInteger(hash, ConOperacionConsultaBean.C_IDOPERACION));
			bean.setDescripcion(UtilidadesHash.getString(hash, ConOperacionConsultaBean.C_DESCRIPCION));
			bean.setTipoOperador(UtilidadesHash.getString(hash, ConOperacionConsultaBean.C_TIPOOPERADOR));
			bean.setSimbolo(UtilidadesHash.getString(hash, ConOperacionConsultaBean.C_SIMBOLO));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ConOperacionConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ConOperacionConsultaBean.C_USUMODIFICACION));
		
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ConOperacionConsultaBean b = (ConOperacionConsultaBean) bean;

			UtilidadesHash.set(htData, ConOperacionConsultaBean.C_IDOPERACION, b.getIdOperacion());
			UtilidadesHash.set(htData, ConOperacionConsultaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ConOperacionConsultaBean.C_TIPOOPERADOR, b.getTipoOperador());
			UtilidadesHash.set(htData, ConOperacionConsultaBean.C_SIMBOLO, b.getSimbolo());
			UtilidadesHash.set(htData, ConOperacionConsultaBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ConOperacionConsultaBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
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
			throw new ClsExceptions (e, "Excepcion en ConOperacionConsultaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/**
	 * Devuelve los values separados por "-" de los simbolos equivalentes al simbolo IS NULL SQL.
	 * @return values separados por "-".
	 */
	public String getvaluesSimboloEstaIgual() {
		String salida = "";
		String select = " SELECT "+ConOperacionConsultaBean.C_IDOPERACION+
						" FROM "+ConOperacionConsultaBean.T_NOMBRETABLA+
						" WHERE "+ConOperacionConsultaBean.C_SIMBOLO+ " like 'is%'";
		try {
			Vector v = this.selectGenerico(select);
			for (int i=0; i< v.size(); i++){
				salida += ((Hashtable)v.elementAt(i)).get(ConOperacionConsultaBean.C_IDOPERACION)+"-";
			}
		} catch (Exception e){
			salida = "";
		}
		
		return salida;
	}
	
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			//String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			String sql=" SELECT "+UtilidadesMultidioma.getCampoMultidioma(ConOperacionConsultaBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+ConOperacionConsultaBean.C_FECHAMODIFICACION+", "+ConOperacionConsultaBean.C_IDOPERACION+
			           ", "+ConOperacionConsultaBean.C_SIMBOLO+", "+ConOperacionConsultaBean.C_TIPOOPERADOR+", "+ConOperacionConsultaBean.C_USUMODIFICACION+
					   " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
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
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
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
			String sql=" SELECT "+UtilidadesMultidioma.getCampoMultidioma(ConOperacionConsultaBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+ConOperacionConsultaBean.C_FECHAMODIFICACION+", "+ConOperacionConsultaBean.C_IDOPERACION+
	           ", "+ConOperacionConsultaBean.C_SIMBOLO+", "+ConOperacionConsultaBean.C_TIPOOPERADOR+", "+ConOperacionConsultaBean.C_USUMODIFICACION+
			   " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;
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
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}
	
	
}
