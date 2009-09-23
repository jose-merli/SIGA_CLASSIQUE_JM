//VERSIONES:
//Creacion: david.sanchezp 18-03-2005

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
* Administrador de Estado del Estado de Pagos de Justicia Gratuita de la tabla FCS_PAGOS_ESTADOSPAGOS
* 
* @author david.sanchezp
*/
public class FcsEstadosPagosAdm extends MasterBeanAdministrador {

	
	public FcsEstadosPagosAdm(UsrBean usuario) {
		super(FcsEstadosPagosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsEstadosPagosBean.C_IDESTADOPAGOSJG, FcsEstadosPagosBean.C_DESCRIPCION,
							FcsEstadosPagosBean.C_FECHAMODIFICACION, FcsEstadosPagosBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsEstadosPagosBean.C_IDESTADOPAGOSJG};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsEstadosPagosBean bean = null;
		
		try {
			bean = new FcsEstadosPagosBean();
			bean.setIdEstadoPagosJG(UtilidadesHash.getInteger(hash,FcsEstadosPagosBean.C_IDESTADOPAGOSJG));
			bean.setDescripcion(UtilidadesHash.getString(hash,FcsEstadosPagosBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsEstadosPagosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsEstadosPagosBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		FcsEstadosPagosBean beanJG = (FcsEstadosPagosBean) bean;
		try {
			htData = new Hashtable();			
			UtilidadesHash.set(htData, FcsEstadosPagosBean.C_IDESTADOPAGOSJG,	beanJG.getIdEstadoPagosJG());
			UtilidadesHash.set(htData, FcsEstadosPagosBean.C_DESCRIPCION, beanJG.getDescripcion());
			UtilidadesHash.set(htData, FcsEstadosPagosBean.C_FECHAMODIFICACION, beanJG.getFechaMod());
			UtilidadesHash.set(htData, FcsEstadosPagosBean.C_USUMODIFICACION, beanJG.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public String getNuevoId(String idInstitucion, String idPagosJG) throws ClsExceptions{
		String id = null;
		
		String consulta = " SELECT (MAX(" + FcsEstadosPagosBean.C_IDESTADOPAGOSJG + ") + 1) AS "+ FcsEstadosPagosBean.C_IDESTADOPAGOSJG + 
						  " FROM " + FcsEstadosPagosBean.T_NOMBRETABLA;
		
		Vector v = this.selectGenerico(consulta);
		if (v.size()==0)
			id = "1";
		else
			id = (String)((Hashtable)v.get(0)).get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG);
		return id;
	}
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
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
			throw new ClsExceptions (e, "Excepcion en FcsPagosEstadoPagosAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}

	/**
	 * Obtengo el nombre del estado de un Pago
	 * 
	 * @param idEstadoPago
	 * @return String con el nombre del Estado
	 */
	public String getNombreEstadoPago (String idEstadoPago){
		String where=null, nombreEstado=null;
		
		try {
			where = " WHERE "+FcsEstadosPagosBean.C_IDESTADOPAGOSJG+"="+idEstadoPago;					
			nombreEstado = ((FcsEstadosPagosBean)this.select(where).get(0)).getDescripcion();
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
			
			String sql = " SELECT "+UtilidadesMultidioma.getCampoMultidioma(FcsEstadosPagosBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+
			               FcsEstadosPagosBean.C_FECHAMODIFICACION+", "+
						   FcsEstadosPagosBean.C_IDESTADOPAGOSJG+", "+
						   FcsEstadosPagosBean.C_USUMODIFICACION+
						 " FROM "+FcsEstadosPagosBean.T_NOMBRETABLA;
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
			String sql = " SELECT "+UtilidadesMultidioma.getCampoMultidioma(FcsEstadosPagosBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+
            FcsEstadosPagosBean.C_FECHAMODIFICACION+", "+
			   FcsEstadosPagosBean.C_IDESTADOPAGOSJG+", "+
			   FcsEstadosPagosBean.C_USUMODIFICACION+
			 " FROM "+FcsEstadosPagosBean.T_NOMBRETABLA;
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
