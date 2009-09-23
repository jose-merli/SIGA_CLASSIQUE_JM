/*
 * Created on 22-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 * Modificado el 21-12-2004 por david.sanchezp
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenPoblacionesAdm extends MasterBeanAdministrador {

	public CenPoblacionesAdm(UsrBean usu) {
		super(CenPoblacionesBean.T_NOMBRETABLA, usu);
	}

	public String[] getCamposBean() {
		String [] campos = {CenPoblacionesBean.C_FECHAMODIFICACION, CenPoblacionesBean.C_IDPARTIDO,CenPoblacionesBean.C_CODIGOEXT,
							CenPoblacionesBean.C_IDPOBLACION, 		CenPoblacionesBean.C_IDPROVINCIA,
							CenPoblacionesBean.C_NOMBRE,			CenPoblacionesBean.C_USUMODIFICACION};
		return campos;
	}

	public String[] getClavesBean() {
		String [] claves = {CenPoblacionesBean.C_IDPOBLACION};
		return claves;
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenPoblacionesBean bean = null;
		
		try {
			bean = new CenPoblacionesBean();
			bean.setFechaMod(UtilidadesHash.getString(hash, CenPoblacionesBean.C_FECHAMODIFICACION));
			bean.setIdPartido(UtilidadesHash.getInteger(hash, CenPoblacionesBean.C_IDPARTIDO));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, CenPoblacionesBean.C_IDPOBLACION));
			bean.setidProvincia(UtilidadesHash.getString(hash, CenPoblacionesBean.C_IDPROVINCIA));
			bean.setNombre(UtilidadesHash.getString(hash, CenPoblacionesBean.C_NOMBRE));
			bean.setIne(UtilidadesHash.getString(hash, CenPoblacionesBean.C_INE));
			bean.setIdPoblacionMunicipio(UtilidadesHash.getString(hash, CenPoblacionesBean.C_IDPOBLACIONMUNICIPIO));
			bean.setCodigoExt(UtilidadesHash.getString(hash, CenPoblacionesBean.C_CODIGOEXT));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenPoblacionesBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenPoblacionesBean b = (CenPoblacionesBean) bean;
			UtilidadesHash.set(htData, CenPoblacionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPARTIDO, String.valueOf(b.getIdPartido()));
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_INE, b.getIne());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPOBLACIONMUNICIPIO, b.getIdPoblacionMunicipio());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/**
	 * Devuelve la consulta SQL para obtener a partir de un id su fecha y usuario de modificacion. 
	 *
	 * @param String id: identidicador de la tabla.
	 * 
	 * @return String con la consulta SQL. 
	 */
	public String getFechaYUsu(String id){
		String sql = "select "+CenPoblacionesBean.C_FECHAMODIFICACION+","+CenPoblacionesBean.C_USUMODIFICACION+" FROM "+CenPoblacionesBean.T_NOMBRETABLA+" WHERE "+CenPoblacionesBean.C_IDPOBLACION+" = '"+id+"'";		
		return sql;
	}
	public Vector selectGenerico(String consulta) throws ClsExceptions,SIGAException {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try {
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}			
		} 
		catch (ClsExceptions e) {
			throw e;		
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenPoblacionesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}

	public String getDescripcion(String id) throws ClsExceptions,SIGAException {
		String salida = "";
		
		// Acceso a BBDD	
		try {
			Hashtable ht = new Hashtable();
			ht.put(CenPoblacionesBean.C_IDPOBLACION,id);
			Vector v = this.selectByPK(ht);
			if (v!=null && v.size()>0) {
				CenPoblacionesBean b = (CenPoblacionesBean) v.get(0);
				salida = b.getNombre();
			}			
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en getDescripcion()");
		}
		return salida;	
	}
}
