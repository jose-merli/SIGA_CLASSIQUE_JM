/**
 * 
 */
package com.siga.ws.i2055;

import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.siga.ws.WSAdm;

/**
 * @author angelcpe
 *
 */
public class WSPamplonaAdm extends WSAdm{
	

	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDtExpedientes(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
				" FROM V_WS_2055_EJG" +
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
	public List<Hashtable<String, String>> getDtPersonas(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
		" FROM V_WS_2055_PERSONA" +
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
	public List<Hashtable<String, String>> getDtArchivo(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
		" FROM V_WS_2055_ARCHIVO" +
		" WHERE IDINSTITUCION = " + idInstitucion +
		" AND IDREMESA = " + idRemesa;
		return select(sql);
	}
	
	public List<Hashtable<String, String>> getPrestaciones(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = "SELECT *" +
		" FROM V_WS_2055_PRESTACIONES" +
		" WHERE IDINSTITUCION = " + idInstitucion +
		" AND IDREMESA = " + idRemesa;
		return select(sql);
	}

}
