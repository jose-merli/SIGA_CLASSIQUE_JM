/*
 * Created on 22-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenProvinciaAdm extends MasterBeanAdministrador{

	public CenProvinciaAdm(UsrBean usu) {
		super(CenProvinciaBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenProvinciaBean.C_FECHAMODIFICACION, 	CenProvinciaBean.C_IDPROVINCIA,CenProvinciaBean.C_CODIGOEXT,
							CenProvinciaBean.C_NOMBRE,				CenProvinciaBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenProvinciaBean.C_IDPROVINCIA};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenProvinciaBean bean = null;
		
		try {
			bean = new CenProvinciaBean();
			bean.setFechaMod((String)hash.get(CenProvinciaBean.C_FECHAMODIFICACION));
			bean.setidProvincia((String)hash.get(CenProvinciaBean.C_IDPROVINCIA));
			bean.setNombre((String)hash.get(CenProvinciaBean.C_NOMBRE));
			bean.setCodigoExt((String)hash.get(CenProvinciaBean.C_CODIGOEXT));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenProvinciaBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenProvinciaBean b = (CenProvinciaBean) bean;
			htData.put(CenProvinciaBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenProvinciaBean.C_IDPROVINCIA, b.getIdProvincia());
			htData.put(CenProvinciaBean.C_NOMBRE, b.getNombre());
			htData.put(CenProvinciaBean.C_CODIGOEXT, b.getCodigoExt());
			htData.put(CenProvinciaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		String [] camposOrden = {CenProvinciaBean.C_IDPROVINCIA};
		return camposOrden;
	}
	public String getDescripcion(String id) throws ClsExceptions,SIGAException {
		String salida="";
		
		// Acceso a BBDD	
		try {
			Hashtable ht = new Hashtable();
			ht.put(CenProvinciaBean.C_IDPROVINCIA,id);
			Vector v = this.selectByPK(ht);
			if (v!=null && v.size()>0) {
				CenProvinciaBean b = (CenProvinciaBean) v.get(0);
				salida = b.getNombre();
			}			
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en getDescripcion()");
		}
		return salida;	
	}
	public List<CenProvinciaBean> getProvincias(String idPais)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT IDPROVINCIA, NOMBRE ");
		sql.append(" FROM CEN_PROVINCIAS  ");
		sql.append(" WHERE IDPAIS = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idPais);
		sql.append(" ORDER BY NOMBRE ");
		
		List<CenProvinciaBean> provinciasList = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	provinciasList = new ArrayList<CenProvinciaBean>();
            	CenProvinciaBean provinciasBean = null;
            	
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		provinciasBean = new CenProvinciaBean();
            		provinciasBean.setidProvincia(UtilidadesHash.getString(htFila,CenProvinciaBean.C_IDPROVINCIA));
            		provinciasBean.setNombre(UtilidadesHash.getString(htFila,CenProvinciaBean.C_NOMBRE));
            		provinciasList.add(provinciasBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta getProvincias.");
       }
       return provinciasList;
		
	}	
	
}
