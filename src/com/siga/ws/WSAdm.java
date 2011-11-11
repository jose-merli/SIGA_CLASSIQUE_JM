package com.siga.ws;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

public class WSAdm {
	
	
	protected static String SELECT = "SELECT ";
	protected static String FROM = " FROM ";
	protected static String WHERE = " WHERE ";
	protected static String AND = " AND ";
	
	/**
	 * 
	 * @param sql
	 * @return
	 * @throws ClsExceptions
	 */
	protected List<Hashtable<String, String>> select(String sql) throws ClsExceptions {
		
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

}
