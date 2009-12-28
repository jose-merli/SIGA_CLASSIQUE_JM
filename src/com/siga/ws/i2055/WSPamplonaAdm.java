/**
 * 
 */
package com.siga.ws.i2055;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

/**
 * @author angelcpe
 *
 */
public class WSPamplonaAdm {
	
	/**
	 * 
	 * @param sql
	 * @return
	 * @throws ClsExceptions
	 */
	private List<Hashtable<String, String>> select(String sql) throws ClsExceptions {
		
		List<Hashtable<String, String>> list = new ArrayList<Hashtable<String,String>>();		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) { 
						list.add(registro);
					}
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage() + "Consulta SQL:"+sql);
		}
		return list;	
	}
	
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

}
