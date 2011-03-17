/**
 * 
 */
package com.siga.ws.i2064;

import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.siga.ws.WSAdm;

/**
 * @author angelcpe
 *
 */
public class WSSantiagoAdm extends WSAdm{
	

	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDtExpedientes(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
				" FROM V_WS_2064_EJG" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return select(sql);
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDtUnidadFamiliar(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
				" FROM V_WS_2064_PERSONA" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return select(sql);
	}	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDtParteContraria(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
				" FROM V_WS_2064_CONTRARIOS" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return select(sql);
	}	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDocumentosAdjuntos(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
				" FROM V_WS_2064_DOCUMENTO" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return select(sql);
	}
	
}
