/**
 * 
 */
package com.siga.ws.i2064;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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

	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions 
	 */
	public Hashtable<String,String> getFacturacion(String idInstitucion, String idFacturacion) throws ClsExceptions {
		String sql = "SELECT *" +
				" FROM V_WS_JE_2064" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDFACTURACION = " + idFacturacion;
		List<Hashtable<String, String>> lista = select(sql);
		if (lista != null && lista.size() == 1) {
			return lista.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getFacturacionAsistencias(String idInstitucion, String idFacturacion) throws ClsExceptions {
		String sql = "SELECT *" +
			" FROM V_WS_JE_2064_ASIS" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDFACTURACION = " + idFacturacion;
		return select(sql);
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getFacturacionTurnoOficio(String idInstitucion, String idFacturacion) throws ClsExceptions {
		String sql = "SELECT *" +
			" FROM V_WS_JE_2064_DESIGNA" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDFACTURACION = " + idFacturacion;
		return select(sql);
	}
	
}
