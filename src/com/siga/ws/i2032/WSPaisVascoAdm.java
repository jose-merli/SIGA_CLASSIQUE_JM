/**
 * 
 */
package com.siga.ws.i2032;

import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.siga.ws.WSAdm;

/**
 * @author angelcpe
 *
 */
public class WSPaisVascoAdm extends WSAdm{
	

	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDtExpedientes(int idInstitucion, int idRemesa, String idLenguaje) throws ClsExceptions {
		String sql = SELECT + "*" +
				FROM + PCAJGConstantes.V_WS_2032_EJG +
				WHERE + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				AND + PCAJGConstantes.IDREMESA + " = " + idRemesa +
				AND + PCAJGConstantes.IDLENGUAJE + " = " + idLenguaje;
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
		String sql = SELECT + "*" +
				FROM + PCAJGConstantes.V_WS_2032_SOLICITANTES +
				WHERE + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				AND + PCAJGConstantes.IDREMESA + " = " + idRemesa;
		return select(sql);
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	public List<Hashtable<String, String>> getDtProfesionalesDesignados(int idInstitucion, int idRemesa) throws ClsExceptions {
		String sql = SELECT + "*" +
				FROM + PCAJGConstantes.V_WS_2032_PROFDESIG +
				WHERE + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				AND + PCAJGConstantes.IDREMESA + " = " + idRemesa;
		return select(sql);
	}
	
	

}
