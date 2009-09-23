/*
 * VERSIONES:
 * jose.barrientos- 24-02-2009 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacBancoInstitucionAbonosAdm extends MasterBeanAdministrador {
	
	public FacBancoInstitucionAbonosAdm (UsrBean usu) {
		super (FacBancoInstitucionAbonosBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacBancoInstitucionAbonosBean.C_IDINSTITUCION, 		
						    FacBancoInstitucionAbonosBean.C_BANCOS_CODIGO,
							FacBancoInstitucionAbonosBean.C_CONCEPTO,							
							FacBancoInstitucionAbonosBean.C_IDPAGOSJG,							
							FacBancoInstitucionAbonosBean.C_USUMODIFICACION,
							FacBancoInstitucionAbonosBean.C_FECHAMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacBancoInstitucionAbonosBean.C_IDINSTITUCION, FacBancoInstitucionAbonosBean.C_BANCOS_CODIGO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacBancoInstitucionAbonosBean bean = null;
		
		try {
			bean = new FacBancoInstitucionAbonosBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacBancoInstitucionAbonosBean.C_IDINSTITUCION));
			bean.setBancosCodigo				(UtilidadesHash.getString(hash, FacBancoInstitucionAbonosBean.C_BANCOS_CODIGO));
			bean.setConcepto					(UtilidadesHash.getString(hash, FacBancoInstitucionAbonosBean.C_CONCEPTO));
			bean.setIdPagosJG					(UtilidadesHash.getInteger(hash, FacBancoInstitucionAbonosBean.C_IDPAGOSJG));
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacBancoInstitucionAbonosBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacBancoInstitucionAbonosBean.C_USUMODIFICACION));
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
			FacBancoInstitucionAbonosBean b = (FacBancoInstitucionAbonosBean) bean;
			UtilidadesHash.set(htData, FacBancoInstitucionAbonosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacBancoInstitucionAbonosBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData, FacBancoInstitucionAbonosBean.C_CONCEPTO, b.getConcepto());
			UtilidadesHash.set(htData, FacBancoInstitucionAbonosBean.C_IDPAGOSJG, b.getIdPagosJG());
			UtilidadesHash.set(htData, FacBancoInstitucionAbonosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacBancoInstitucionAbonosBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	public Hashtable getDatosAbono (String idInstitucion, String idPagosJG) throws ClsExceptions, SIGAException {
		Hashtable datos=new Hashtable();
	    try {
	         RowsContainer rc = new RowsContainer(); 
	         String sql ="SELECT " +
							FacBancoInstitucionAbonosBean.T_NOMBRETABLA + "." + FacBancoInstitucionAbonosBean.C_CONCEPTO + "," +
							FacBancoInstitucionAbonosBean.T_NOMBRETABLA + "." + FacBancoInstitucionAbonosBean.C_BANCOS_CODIGO +
							" FROM " + FacBancoInstitucionAbonosBean.T_NOMBRETABLA + 
							" WHERE " +
							FacBancoInstitucionAbonosBean.T_NOMBRETABLA +"."+ FacBancoInstitucionAbonosBean.C_IDPAGOSJG + "=" + idPagosJG +
							" AND " +
							FacBancoInstitucionAbonosBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDINSTITUCION + "=" + idInstitucion ;							
							
	         rc = this.find(sql);
	         if (rc!=null) {
	            for (int i = 0; i < rc.size(); i++){
	               Row fila = (Row) rc.get(i);
	               datos=fila.getRow();
	            }
	         } 
	    }
	    catch (Exception e) {
	    	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla cuentas bancarias.");
	    }
	    return datos;  
	}

}
