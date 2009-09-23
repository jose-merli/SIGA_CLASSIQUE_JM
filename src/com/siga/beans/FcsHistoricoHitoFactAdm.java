//VERSIONES:
//Creacion: julio.vicente 15-04-2005

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;



/**
* Administrador de Histórico Hitos Facturables de la tabla FCS_HISTORICO_HITOFACT
* 
* @author david.sanchezp
*/
public class FcsHistoricoHitoFactAdm extends MasterBeanAdministrador {
	
	public FcsHistoricoHitoFactAdm(UsrBean usuario) {
		super(FcsHistoricoHitoFactBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsHistoricoHitoFactBean.C_IDFACTURACION, 		FcsHistoricoHitoFactBean.C_IDGUARDIA,
							FcsHistoricoHitoFactBean.C_IDTURNO,		 		FcsHistoricoHitoFactBean.C_IDHITO,
							FcsHistoricoHitoFactBean.C_PAGOFACTURACION, 	FcsHistoricoHitoFactBean.C_PRECIOHITO,
							FcsHistoricoHitoFactBean.C_FECHAMODIFICACION,	FcsHistoricoHitoFactBean.C_USUMODIFICACION, 
							FcsHistoricoHitoFactBean.C_IDINSTITUCION 
						   };
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsHistoricoHitoFactBean.C_IDINSTITUCION, FcsHistoricoHitoFactBean.C_IDFACTURACION, FcsHistoricoHitoFactBean.C_IDGUARDIA, FcsHistoricoHitoFactBean.C_IDTURNO, FcsHistoricoHitoFactBean.C_IDHITO, FcsHistoricoHitoFactBean.C_PAGOFACTURACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}


	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsHistoricoHitoFactBean bean = null;
		
		try {
			bean = new FcsHistoricoHitoFactBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsHistoricoHitoFactBean.C_IDINSTITUCION));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,FcsHistoricoHitoFactBean.C_IDFACTURACION));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,FcsHistoricoHitoFactBean.C_IDGUARDIA));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,FcsHistoricoHitoFactBean.C_IDTURNO));
			bean.setIdHito(UtilidadesHash.getInteger(hash,FcsHistoricoHitoFactBean.C_IDHITO));
			bean.setPagoFacturacion(UtilidadesHash.getString(hash,FcsHistoricoHitoFactBean.C_PAGOFACTURACION));
			bean.setPrecioHito(UtilidadesHash.getDouble(hash,FcsHistoricoHitoFactBean.C_PRECIOHITO));			
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsHistoricoHitoFactBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsHistoricoHitoFactBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		FcsHistoricoHitoFactBean miBean = (FcsHistoricoHitoFactBean) bean;
		try {
			htData = new Hashtable();			
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_IDINSTITUCION, miBean.getIdInstitucion());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_IDFACTURACION, miBean.getIdFacturacion());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_IDGUARDIA, miBean.getIdGuardia());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_IDTURNO, miBean.getIdTurno());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_IDHITO, miBean.getIdHito());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_PAGOFACTURACION, miBean.getPagoFacturacion());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_PRECIOHITO, miBean.getPrecioHito());			
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_FECHAMODIFICACION, miBean.getFechaMod());
			UtilidadesHash.set(htData, FcsHistoricoHitoFactBean.C_USUMODIFICACION, miBean.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
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
}