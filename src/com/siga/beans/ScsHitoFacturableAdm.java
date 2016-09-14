
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_HITOFACTURABLE
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsHitoFacturableAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsHitoFacturableAdm (UsrBean usuario) {
		super( ScsHitoFacturableBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsHitoFacturableBean.C_APLICABLEA,			ScsHitoFacturableBean.C_DESCRIPCION,
							ScsHitoFacturableBean.C_FECHAMODIFICACION,	ScsHitoFacturableBean.C_IDHITO,
							ScsHitoFacturableBean.C_NOMBRE,				ScsHitoFacturableBean.C_SQLSELECCION,
							ScsHitoFacturableBean.C_SQLUPDATE,			ScsHitoFacturableBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsHitoFacturableBean.C_IDHITO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsHitoFacturableBean bean = null;
		try{
			bean = new ScsHitoFacturableBean();
			bean.setAplicableA(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_APLICABLEA));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_DESCRIPCION));
			bean.setIdHito(UtilidadesHash.getInteger(hash,ScsHitoFacturableBean.C_IDHITO));
			bean.setNombre(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_NOMBRE));
			bean.setSqlSeleccion(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_SQLSELECCION));
			bean.setSqlUpdate(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_SQLUPDATE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsHitoFacturableBean.C_USUMODIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsHitoFacturableBean.C_FECHAMODIFICACION));
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
		try{
			hash = new Hashtable();
			ScsHitoFacturableBean b = (ScsHitoFacturableBean) bean;
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_APLICABLEA, b.getAplicableA());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_IDHITO, b.getIdHito());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_SQLSELECCION, b.getSqlSeleccion());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_SQLUPDATE, b.getSqlUpdate());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(hash, ScsHitoFacturableBean.C_FECHAMODIFICACION, b.getFechaMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return null;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}

	/**
	 * Obtiene los hitos (SCS_HITOFACTURABLE) de una guardia con la configuración actual (SCS_HITOFACTURABLEGUARDIA)
	 * @param sInstitucion
	 * @param sIdTurno
	 * @param sIdGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerHitosActual (String sInstitucion, String sIdTurno, String sIdGuardia) throws ClsExceptions {
		Vector<Hashtable<String,Object>> vHitos = new Vector<Hashtable<String,Object>>();
       try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append(UtilidadesMultidioma.getCampoMultidioma(ScsHitoFacturableBean.T_NOMBRETABLA + "." + ScsHitoFacturableBean.C_DESCRIPCION, this.usrbean.getLanguage()));
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_PRECIOHITO);
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_AGRUPAR);
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDINSTITUCION);
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDTURNO);
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDGUARDIA);
            sql.append(",");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDHITO);
            sql.append(" FROM ");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(",");
            sql.append(ScsHitoFacturableBean.T_NOMBRETABLA);
            sql.append(" WHERE ");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDINSTITUCION);
            sql.append("=");
            sql.append(sInstitucion);
            sql.append(" AND ");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDTURNO);
            sql.append("=");
            sql.append(sIdTurno);
            sql.append(" AND ");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDGUARDIA);
            sql.append("=");
            sql.append(sIdGuardia);
            sql.append(" AND ");
            sql.append(ScsHitoFacturableBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableBean.C_IDHITO);
            sql.append("=");
            sql.append(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableGuardiaBean.C_IDHITO);
            
            vHitos = this.selectGenerico(sql.toString());
            
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al invocar obtenerHitoActual.");
       }
       
       return vHitos;                        
    }			
	
	/**
	 * Obtiene los hitos (SCS_HITOFACTURABLE) de una guardia con la configuración histórica (FCS_HISTORICO_HITOFACT)
	 * @param sInstitucion
	 * @param sIdTurno
	 * @param sIdGuardia
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerHitosHistorico (String sInstitucion, String sIdTurno, String sIdGuardia, String sIdFacturacion) throws ClsExceptions {
		Vector<Hashtable<String,Object>> vHitos = new Vector<Hashtable<String,Object>>();
       try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append(UtilidadesMultidioma.getCampoMultidioma(ScsHitoFacturableBean.T_NOMBRETABLA + "." + ScsHitoFacturableBean.C_DESCRIPCION, this.usrbean.getLanguage()));
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_PRECIOHITO);
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_DIASAPLICABLES);
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_AGRUPAR);
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDINSTITUCION);
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDTURNO);
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDGUARDIA);
            sql.append(",");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDHITO);
            sql.append(" FROM ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(",");
            sql.append(ScsHitoFacturableBean.T_NOMBRETABLA);
            sql.append(" WHERE ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDINSTITUCION);
            sql.append("=");
            sql.append(sInstitucion);
            sql.append(" AND ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDTURNO);
            sql.append("=");
            sql.append(sIdTurno);
            sql.append(" AND ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDGUARDIA);
            sql.append("=");
            sql.append(sIdGuardia);
            sql.append(" AND ");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDFACTURACION);
            sql.append("=");
            sql.append(sIdFacturacion);            
            sql.append(" AND ");
            sql.append(ScsHitoFacturableBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(ScsHitoFacturableBean.C_IDHITO);
            sql.append("=");
            sql.append(FcsHistoricoHitoFactBean.T_NOMBRETABLA);
            sql.append(".");
            sql.append(FcsHistoricoHitoFactBean.C_IDHITO);            
            
            vHitos = this.selectGenerico(sql.toString());
            
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al invocar obtenerHitoActual.");
       }
       
       return vHitos;                        
    }	
}