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
	
	/**
	 * Obtiene las facturaciones SJCS historicas de una guardia
	 * @param sInstitucion
	 * @param sIdTurno
	 * @param sIdGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerFacturacionesSJCSGuardia (String sInstitucion, String sIdTurno, String sIdGuardia) throws ClsExceptions {
		Vector<Hashtable<String,Object>> vFacturacionesSJCS = new Vector<Hashtable<String,Object>>();
       try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_IDFACTURACION);
            sql.append(", ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_NOMBRE);
            sql.append(", ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_FECHADESDE);
            sql.append(", ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_FECHAHASTA);
            sql.append(" FROM ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(", ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(" WHERE ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDINSTITUCION);
            sql.append(" = ");
            sql.append(sInstitucion);
            sql.append(" AND ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDTURNO);
            sql.append(" = ");
            sql.append(sIdTurno);
            sql.append(" AND ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDGUARDIA);
            sql.append(" = ");
            sql.append(sIdGuardia);
            sql.append(" AND ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_IDINSTITUCION);
            sql.append(" = ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDINSTITUCION);
            sql.append(" AND ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_IDFACTURACION);
            sql.append(" = ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDFACTURACION);
            sql.append(" ORDER BY ");
            sql.append(FcsFacturacionJGBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsFacturacionJGBean.C_FECHADESDE);
            sql.append(" DESC");
            
            vFacturacionesSJCS = this.selectGenerico(sql.toString());
            
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al invocar obtenerFacturacionesSJCSGuardia.");
       }
       
       return vFacturacionesSJCS;                        
    }			
}