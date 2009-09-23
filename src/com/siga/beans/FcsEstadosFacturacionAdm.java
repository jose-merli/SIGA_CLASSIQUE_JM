//VERSIONES:
//raul.ggonzalez 08-03-2005 creacion 

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
* Administrador de Facturacion de justicia gratuita
* @author AtosOrigin 08-03-2005
*/
public class FcsEstadosFacturacionAdm extends MasterBeanAdministrador {

	
	public FcsEstadosFacturacionAdm(UsrBean usuario) {
		super(FcsEstadosFacturacionBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsEstadosFacturacionBean.C_IDESTADOFACTURACION,				
							FcsEstadosFacturacionBean.C_DESCRIPCION,
							FcsEstadosFacturacionBean.C_FECHAMODIFICACION,
							FcsEstadosFacturacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsEstadosFacturacionBean.C_IDESTADOFACTURACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsEstadosFacturacionBean bean = null;
		
		try {
			bean = new FcsEstadosFacturacionBean();
			bean.setIdEstadoFacturacion(UtilidadesHash.getInteger(hash,FcsEstadosFacturacionBean.C_IDESTADOFACTURACION));
			bean.setDescripcion(UtilidadesHash.getString(hash,FcsEstadosFacturacionBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsEstadosFacturacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsEstadosFacturacionBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FcsEstadosFacturacionBean b = (FcsEstadosFacturacionBean) bean;
			UtilidadesHash.set(htData, FcsEstadosFacturacionBean.C_IDESTADOFACTURACION, 		b.getIdEstadoFacturacion());
			UtilidadesHash.set(htData, FcsEstadosFacturacionBean.C_DESCRIPCION, 	b.getDescripcion());
			UtilidadesHash.set(htData, FcsEstadosFacturacionBean.C_FECHAMODIFICACION, 		b.getFechaMod());
			UtilidadesHash.set(htData, FcsEstadosFacturacionBean.C_USUMODIFICACION, 		b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/**
	 * Obtengo el nombre del estado de una Facturacion 
	 * 
	 * @param idEstadoFacturacion
	 * @return String con el nombre del Estado
	 */
	public String getNombreEstadoFacturacion (String idEstadoFacturacion){
		String where=null, nombreEstado=null;
		
		try {
			
			
			where = " WHERE "+FcsEstadosFacturacionBean.C_IDESTADOFACTURACION+"="+idEstadoFacturacion;					
			nombreEstado = ((FcsEstadosFacturacionBean)this.select(where).get(0)).getDescripcion();
			
		} catch (Exception e){
			return null;
		}
		return nombreEstado;
	}
	
	/**
	 * Obtengo el nombre del estado de una Facturacion 
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return String con el nombre del Estado
	 */
	public String getNombreEstadoFacturacion (String idInstitucion, String idFacturacion){
		String where=null, nombreEstado=null, idEstado=null;
		FcsFactEstadosFacturacionAdm admEstadoFact = new FcsFactEstadosFacturacionAdm(this.usrbean);
		
		try {
			idEstado = admEstadoFact.getIdEstadoFacturacion(idInstitucion, idFacturacion);
			nombreEstado = this.getNombreEstadoFacturacion(idEstado);
		} catch (Exception e){
			return null;
		}
		return nombreEstado;
	}
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			//String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			String sql =" SELECT "+UtilidadesMultidioma.getCampoMultidiomaSimple(FcsEstadosFacturacionBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+FcsEstadosFacturacionBean.C_FECHAMODIFICACION+", "+FcsEstadosFacturacionBean.C_IDESTADOFACTURACION+", "+FcsEstadosFacturacionBean.C_USUMODIFICACION  +" FROM "+FcsEstadosFacturacionBean.T_NOMBRETABLA;
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
			String sql =" SELECT "+UtilidadesMultidioma.getCampoMultidiomaSimple(FcsEstadosFacturacionBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+FcsEstadosFacturacionBean.C_FECHAMODIFICACION+", "+FcsEstadosFacturacionBean.C_IDESTADOFACTURACION+", "+FcsEstadosFacturacionBean.C_USUMODIFICACION  +" FROM "+FcsEstadosFacturacionBean.T_NOMBRETABLA;
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
