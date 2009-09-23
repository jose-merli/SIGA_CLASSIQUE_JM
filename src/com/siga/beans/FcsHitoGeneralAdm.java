//VERSIONES:
//ruben.fernandez 09-03-2005 creacion 

package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
* Administrador de Facturacion de justicia gratuita
* @author AtosOrigin 08-03-2005
*/
public class FcsHitoGeneralAdm extends MasterBeanAdministrador {

	
	public FcsHitoGeneralAdm(UsrBean usuario) {
		super(FcsHitoGeneralBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FcsHitoGeneralBean.C_DESCRIPCION,		FcsHitoGeneralBean.C_IDHITOGENERAL,
							FcsHitoGeneralBean.C_FECHAMODIFICACION,			FcsHitoGeneralBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FcsHitoGeneralBean.C_IDHITOGENERAL};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsHitoGeneralBean bean = null;
		
		try {
			bean = new FcsHitoGeneralBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsHitoGeneralBean.C_FECHAMODIFICACION));
			bean.setIdHitoGeneral(UtilidadesHash.getInteger(hash,FcsHitoGeneralBean.C_IDHITOGENERAL));
			bean.setDescripcion(UtilidadesHash.getString(hash,FcsHitoGeneralBean.C_DESCRIPCION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsHitoGeneralBean.C_USUMODIFICACION));
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
			FcsHitoGeneralBean b = (FcsHitoGeneralBean) bean;
			UtilidadesHash.set(htData, FcsHitoGeneralBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, FcsHitoGeneralBean.C_DESCRIPCION, 		b.getDescripcion());
			UtilidadesHash.set(htData, FcsHitoGeneralBean.C_IDHITOGENERAL, 		b.getIdHitoGeneral());
			UtilidadesHash.set(htData, FcsHitoGeneralBean.C_USUMODIFICACION, 	b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + FcsHitoGeneralBean.C_IDHITOGENERAL+ ", " + 
		      UtilidadesMultidioma.getCampoMultidioma(FcsHitoGeneralBean.C_DESCRIPCION, this.usrbean.getLanguage()) +" , " +
			  AdmLenguajesBean.C_FECHAMODIFICACION +", " +
			  AdmLenguajesBean.C_USUMODIFICACION +
			  " FROM " + FcsHitoGeneralBean.T_NOMBRETABLA + " ";			
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}
	

	
	}
