//VERSIONES:
//Creacion: david.sanchezp 18-03-2005

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
* Administrador de Estado del Estado de Pagos de Justicia Gratuita de la tabla FCS_PAGOS_ESTADOSPAGOS
* 
* @author david.sanchezp
*/
public class FcsPagosEstadosPagosAdm extends MasterBeanAdministrador {

	
	public FcsPagosEstadosPagosAdm(UsrBean usuario) {
		super(FcsPagosEstadosPagosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsPagosEstadosPagosBean.C_IDINSTITUCION, FcsPagosEstadosPagosBean.C_IDPAGOSJG,				
							FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG, FcsPagosEstadosPagosBean.C_FECHAESTADO,
							FcsPagosEstadosPagosBean.C_FECHAMODIFICACION, FcsPagosEstadosPagosBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsPagosEstadosPagosBean.C_IDINSTITUCION, FcsPagosEstadosPagosBean.C_IDPAGOSJG, 
							FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagosEstadosPagosBean bean = null;
		
		try {
			bean = new FcsPagosEstadosPagosBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsPagosEstadosPagosBean.C_IDINSTITUCION));
			bean.setIdPagosJG(UtilidadesHash.getInteger(hash,FcsPagosEstadosPagosBean.C_IDPAGOSJG));
			bean.setIdEstadoPagosJG(UtilidadesHash.getInteger(hash,FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG));
			bean.setFechaEstado(UtilidadesHash.getString(hash,FcsPagosEstadosPagosBean.C_FECHAESTADO));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsPagosEstadosPagosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsPagosEstadosPagosBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		FcsPagosEstadosPagosBean beanJG = (FcsPagosEstadosPagosBean) bean;
		try {
			htData = new Hashtable();			
			UtilidadesHash.set(htData, FcsPagosEstadosPagosBean.C_IDINSTITUCION,	beanJG.getIdInstitucion());
			UtilidadesHash.set(htData, FcsPagosEstadosPagosBean.C_IDPAGOSJG,	beanJG.getIdPagosJG());
			UtilidadesHash.set(htData, FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG,	beanJG.getIdEstadoPagosJG());
			UtilidadesHash.set(htData, FcsPagosEstadosPagosBean.C_FECHAESTADO, beanJG.getFechaEstado());
			UtilidadesHash.set(htData, FcsPagosEstadosPagosBean.C_FECHAMODIFICACION, beanJG.getFechaMod());
			UtilidadesHash.set(htData, FcsPagosEstadosPagosBean.C_USUMODIFICACION, beanJG.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public String getNuevoId(String idInstitucion, String idPagosJG) throws ClsExceptions{
		String id = null;
		
		String consulta = " SELECT (MAX(" + FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG + ") + 1) AS "+ FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG + 
						  " FROM " + FcsPagosEstadosPagosBean.T_NOMBRETABLA +
					      " WHERE " + FcsPagosEstadosPagosBean.C_IDINSTITUCION + " = " + idInstitucion +
						  " AND "+ FcsPagosEstadosPagosBean.C_IDPAGOSJG +"="+ idPagosJG;
		
		
		Vector v = this.selectGenerico(consulta);
		if (v.size()==0)
			id = "1";
		else
			id = (String)((Hashtable)v.get(0)).get(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG);
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
	 * @param idInstitucion
	 * @param idPago
	 * @return String con el nombre del Estado
	 */
	public String getNombreEstadoPago (String idInstitucion, String idPago){
		String where=null, nombreEstado=null, idEstado=null;		
		FcsEstadosPagosAdm admEstadoPago = new FcsEstadosPagosAdm(this.usrbean); 
		
		try {
			idEstado = this.getIdEstadoPago(idInstitucion, idPago);
			nombreEstado = admEstadoPago.getNombreEstadoPago(idEstado);
		} catch (Exception e){
			return null;
		}
		return nombreEstado;
	}
	
	/**
	 * Obtengo el idEstado de un Pago (es el que tenga la Fecha del Estado mas reciente)
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @return String con el idEstado
	 */
	public String getIdEstadoPago (String idInstitucion, String idPago){
		String consulta=null, idEstado=null;
		
		try {
			consulta = " SELECT "+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+
 					   " FROM "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+
					   " WHERE "+FcsPagosEstadosPagosBean.C_IDINSTITUCION+"="+idInstitucion+
					   " AND "+FcsPagosEstadosPagosBean.C_IDPAGOSJG+"="+idPago+
					   " ORDER BY "+FcsPagosEstadosPagosBean.C_FECHAESTADO+" DESC ";
			idEstado = (String)((Hashtable)this.selectGenerico(consulta).get(0)).get(FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG);
		} catch (Exception e){
			return null;
		}
		return idEstado;
	}
	
	/**
	 * Obtengo la fecha del Estado de ese Pago (es la mas reciente)
	 * @param idInstitucion
	 * @param idPago
	 * @param idEstado
	 * @return String con la fechaEstado
	 */
	public String getFechaEstadoPago (String idInstitucion, String idPago, String idEstado){
		String consulta=null, fechaEstado=null;
		
		try {
			//Obtengo la fecha mas reciente:
			consulta = " SELECT "+FcsPagosEstadosPagosBean.C_FECHAESTADO+
					" FROM "+FcsPagosEstadosPagosBean.T_NOMBRETABLA+
					" WHERE "+FcsPagosEstadosPagosBean.C_IDINSTITUCION+"="+idInstitucion+
					" AND "+FcsPagosEstadosPagosBean.C_IDPAGOSJG+"="+idPago+
					" AND "+FcsPagosEstadosPagosBean.C_IDESTADOPAGOSJG+"="+idEstado+
					" ORDER BY "+FcsPagosEstadosPagosBean.C_FECHAESTADO+" DESC ";
			fechaEstado = (String)((Hashtable)this.selectGenerico(consulta).get(0)).get(FcsPagosEstadosPagosBean.C_FECHAESTADO);
		} catch (Exception e){
			return null;
		}
		return fechaEstado;
	}
	
}
