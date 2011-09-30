/*
 * Created on 17/09/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author fernando.gomez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgRemesaAdm extends MasterBeanAdministrador {

	public CajgRemesaAdm (UsrBean usu) {
		super (CajgRemesaBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgRemesaBean.C_IDINSTITUCION, CajgRemesaBean.C_IDREMESA,
							CajgRemesaBean.C_PREFIJO, 		CajgRemesaBean.C_NUMERO,
							CajgRemesaBean.C_SUFIJO,		CajgRemesaBean.C_DESCRIPCION,
							CajgRemesaBean.C_IDINTERCAMBIO,
							CajgRemesaBean.C_FECHAMODIFICACION,	CajgRemesaBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgRemesaBean.C_IDINSTITUCION, 
							CajgRemesaBean.C_IDREMESA};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgRemesaBean bean = null;
		try{
			bean = new CajgRemesaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_IDINSTITUCION));
			bean.setIdRemesa(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_IDREMESA));
			bean.setPrefijo(UtilidadesHash.getString(hash, CajgRemesaBean.C_PREFIJO));
			bean.setNumero(UtilidadesHash.getString(hash,CajgRemesaBean.C_NUMERO));
			bean.setSufijo(UtilidadesHash.getString(hash, CajgRemesaBean.C_SUFIJO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CajgRemesaBean.C_DESCRIPCION));
			bean.setIdIntercambio(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_IDINTERCAMBIO));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgRemesaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_USUMODIFICACION));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgRemesaBean b = (CajgRemesaBean) bean;
			
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDREMESA, b.getIdRemesa());
			UtilidadesHash.set(hash, CajgRemesaBean.C_PREFIJO, b.getPrefijo());
			UtilidadesHash.set(hash, CajgRemesaBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(hash, CajgRemesaBean.C_SUFIJO, b.getSufijo());
			UtilidadesHash.set(hash, CajgRemesaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, CajgRemesaBean.C_IDINTERCAMBIO, b.getIdIntercambio());
			
			UtilidadesHash.set(hash, CajgRemesaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgRemesaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	public String SeleccionarMaximo(String  idinstitucion) throws ClsExceptions 
	{
		RowsContainer rc = null;		
		String numeroMaximo = null;		
		
		try { 
			rc = new RowsContainer();		
			
			
			String sql ="SELECT (MAX("+ CajgRemesaBean.C_IDREMESA + ") + 1) AS IDREMESA FROM " + nombreTabla + " WHERE IDINSTITUCION = " + idinstitucion;			 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDREMESA").equals("")) {
					numeroMaximo = "1";
				}
				else numeroMaximo = prueba.get("IDREMESA").toString();				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE NUMERO");
		};
		
		return numeroMaximo;
	}
	
	
}