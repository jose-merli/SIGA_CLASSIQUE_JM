package com.siga.general;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.GenCatalogosMultiidiomaAdm;
import com.siga.beans.GenCatalogosMultiidiomaBean;

public class MultidiomaGenCatalogosMultidioma 
{
	private static Hashtable hDatos = null; 
	
	private static void init () 
	{
		try {
			hDatos = new Hashtable();
			GenCatalogosMultiidiomaAdm adm = new GenCatalogosMultiidiomaAdm (new UsrBean());
			Vector v = adm.select();
			for (int i = 0; i < v.size(); i++) {
				GenCatalogosMultiidiomaBean bean = (GenCatalogosMultiidiomaBean) v.get(i);
				String key = bean.getNombreTabla() + "_" + bean.getCampoTabla();
				Integer value = bean.getCodigo();
				UtilidadesHash.set(hDatos, key.toUpperCase(), value);
			}
		}
		catch (Exception e) {
			hDatos = null;
		}
	}
	
	public static Integer getCodigo (String tabla, String campo) 
	{
		if (hDatos == null) {
			MultidiomaGenCatalogosMultidioma.init();
		}
		String key = tabla + "_" + campo;
		return UtilidadesHash.getInteger(hDatos, key.toUpperCase());
	}
}
