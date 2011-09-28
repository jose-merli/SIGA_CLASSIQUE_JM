//VERSIONES:
//raul.ggonzalez 08-03-2005 creacion 

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
* Administrador de Facturacion de justicia gratuita
* @author AtosOrigin 08-03-2005
*/
public class FcsFactEstadosFacturacionAdm extends MasterBeanAdministrador {

	
	public FcsFactEstadosFacturacionAdm(UsrBean usuario) {
		super(FcsFactEstadosFacturacionBean.T_NOMBRETABLA, usuario);
	}





	protected String[] getCamposBean() {
		String [] campos = {FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION,				
							FcsFactEstadosFacturacionBean.C_IDFACTURACION,
							FcsFactEstadosFacturacionBean.C_IDINSTITUCION,
							FcsFactEstadosFacturacionBean.C_FECHAMODIFICACION,
							FcsFactEstadosFacturacionBean.C_USUMODIFICACION,
							FcsFactEstadosFacturacionBean.C_FECHAESTADO,
							FcsFactEstadosFacturacionBean.C_IDORDENESTADO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION,FcsFactEstadosFacturacionBean.C_IDFACTURACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactEstadosFacturacionBean bean = null;
		
		try {
			bean = new FcsFactEstadosFacturacionBean();
			bean.setIdEstadoFacturacion(UtilidadesHash.getInteger(hash,FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsFactEstadosFacturacionBean.C_IDINSTITUCION));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,FcsFactEstadosFacturacionBean.C_IDFACTURACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsFactEstadosFacturacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsFactEstadosFacturacionBean.C_USUMODIFICACION));
			bean.setIdOrdenEstado(UtilidadesHash.getInteger(hash,FcsFactEstadosFacturacionBean.C_IDORDENESTADO));
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
			FcsFactEstadosFacturacionBean b = (FcsFactEstadosFacturacionBean) bean;
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION, 		b.getIdEstadoFacturacion());
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_IDFACTURACION, 		b.getIdFacturacion());
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_FECHAMODIFICACION, 		b.getFechaMod());
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_FECHAESTADO, 		b.getFechaEstado());
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_USUMODIFICACION, 		b.getUsuMod());
			UtilidadesHash.set(htData, FcsFactEstadosFacturacionBean.C_IDORDENESTADO, 		b.getIdOrdenEstado());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/**
	 * Obtengo el idEstado de una Facturacion  (es el que tenga la Fecha del Estado mas reciente)
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return String con el idEstado
	 */
	public String getIdEstadoFacturacion (String idInstitucion, String idFacturacion){
		String consulta=null, idEstado=null;
		
		try {
			consulta = " SELECT "+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+
 					   " FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+
					   " WHERE "+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
					   " AND "+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+idFacturacion+
					   " AND " + FcsFactEstadosFacturacionBean.C_IDORDENESTADO + " = (SELECT MAX(" + FcsFactEstadosFacturacionBean.C_IDORDENESTADO + ")" +
					   		" FROM " + FcsFactEstadosFacturacionBean.T_NOMBRETABLA +
					   		" WHERE " + FcsFactEstadosFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND " + FcsFactEstadosFacturacionBean.C_IDFACTURACION + " = "+idFacturacion + ")";
			
			idEstado = (String)((Hashtable)this.selectGenerico(consulta).get(0)).get(FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION);
		} catch (Exception e){
			return null;
		}
		return idEstado;
	}
	
	/**
	 * Obtengo la fecha del Estado de esa Facturacion (es la mas reciente)
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idEstado
	 * @return String con la fechaEstado
	 */
	public String getFechaEstadoFacturacion (String idInstitucion, String idFacturacion, String idEstado){
		String consulta=null, fechaEstado=null;
		
		try {
			//Obtengo la fecha mas reciente:
			consulta = " SELECT "+FcsFactEstadosFacturacionBean.C_FECHAESTADO+
					" FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+
					" WHERE "+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
					" AND "+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+idFacturacion+
					" AND "+FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION+"="+idEstado+
					" ORDER BY "+FcsFactEstadosFacturacionBean.C_FECHAESTADO+" DESC ";
			fechaEstado = (String)((Hashtable)this.selectGenerico(consulta).get(0)).get(FcsFactEstadosFacturacionBean.C_FECHAESTADO);
		} catch (Exception e){
			return null;
		}
		return fechaEstado;
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
			throw new ClsExceptions (e, "Excepcion en FcsFactEstadosFacturacionAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	public String getIdordenestadoMaximo (String idInstitucion, String idFacturacion) throws ClsExceptions {
		
		String consulta=null; 
		String idordenmax=null;
		RowsContainer rc = null;
		int contador = 0;
		
		try {
			//Obtengo la fecha mas reciente:
			rc = new RowsContainer();
			consulta = " Select max("+FcsFactEstadosFacturacionBean.C_IDORDENESTADO+")+1 as IDORDENESTADO"+
					" FROM "+FcsFactEstadosFacturacionBean.T_NOMBRETABLA+
					" WHERE "+FcsFactEstadosFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
					" AND "+FcsFactEstadosFacturacionBean.C_IDFACTURACION+"="+idFacturacion;	
			
			if (rc.query(consulta)) {
				Row fila = (Row) rc.get(0);										
				idordenmax = fila.getString(FcsFactEstadosFacturacionBean.C_IDORDENESTADO);	
			}
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en la funcion getIdordenestadoMaximo() en B.D."); 
		}		
		return idordenmax;		
	
	}
}
