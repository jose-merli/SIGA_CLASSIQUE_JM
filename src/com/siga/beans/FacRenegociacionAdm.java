/*
 * Created on 23-mar-2005
 *
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class FacRenegociacionAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public FacRenegociacionAdm(UsrBean usuario) {
		super(FacRenegociacionBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {FacRenegociacionBean.C_COMENTARIO, 		
							FacRenegociacionBean.C_FECHAMODIFICACION,
							FacRenegociacionBean.C_FECHARENEGOCIACION, 	
							FacRenegociacionBean.C_IDCUENTA,
							FacRenegociacionBean.C_IDFACTURA,	
							FacRenegociacionBean.C_IDINSTITUCION,	
							FacRenegociacionBean.C_IDPERSONA,	
							FacRenegociacionBean.C_IDRENEGOCIACION,	
							FacRenegociacionBean.C_IMPORTE,	
							FacRenegociacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacRenegociacionBean.C_IDFACTURA, 
							FacRenegociacionBean.C_IDINSTITUCION, 
							FacRenegociacionBean.C_IDRENEGOCIACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacRenegociacionBean bean = null;
		
		try {
			bean = new FacRenegociacionBean();
			bean.setComentario(UtilidadesHash.getString(hash, FacRenegociacionBean.C_COMENTARIO));
			bean.setFechaMod(UtilidadesHash.getString(hash, FacRenegociacionBean.C_FECHAMODIFICACION));
			bean.setFechaRenegociacion(UtilidadesHash.getString(hash, FacRenegociacionBean.C_FECHARENEGOCIACION));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash, FacRenegociacionBean.C_IDCUENTA));
			bean.setIdFactura(UtilidadesHash.getString(hash, FacRenegociacionBean.C_IDFACTURA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, FacRenegociacionBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, FacRenegociacionBean.C_IDPERSONA));
			bean.setIdRenegociacion(UtilidadesHash.getInteger(hash, FacRenegociacionBean.C_IDRENEGOCIACION));
			bean.setImporte(UtilidadesHash.getDouble(hash, FacRenegociacionBean.C_IMPORTE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, FacRenegociacionBean.C_USUMODIFICACION));
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
			FacRenegociacionBean b = (FacRenegociacionBean) bean;
			UtilidadesHash.set(htData, FacRenegociacionBean.C_COMENTARIO, b.getComentario());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_FECHARENEGOCIACION, b.getFechaRenegociacion());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_IDCUENTA, b.getIdCuenta());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_IDFACTURA, b.getIdFactura());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_IDRENEGOCIACION, b.getIdRenegociacion());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_IMPORTE, b.getImporte());
			UtilidadesHash.set(htData, FacRenegociacionBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Obtiene un nuevo IDRenegociacion para una factura de una institucion
	 * @param idInstitucion
	 * @param idFactura
	 * @return el nuevo id generado
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Integer getNuevoID (Integer idInstitucion, String idFactura) throws ClsExceptions, SIGAException 
	{
		Integer id = null;
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql = " SELECT NVL(MAX(" + FacRenegociacionBean.C_IDRENEGOCIACION + "),0) + 1 AS " + FacRenegociacionBean.C_IDRENEGOCIACION +
				  " FROM " + this.nombreTabla + 
				  " WHERE " + FacRenegociacionBean.C_IDINSTITUCION + " = " + idInstitucion + 
				  " AND " + FacRenegociacionBean.C_IDFACTURA + " = '" + idFactura + "'";
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(FacRenegociacionBean.C_IDRENEGOCIACION).equals("")) {
					id = new Integer(1);
				}
				else {
					id = UtilidadesHash.getInteger(prueba, FacRenegociacionBean.C_IDRENEGOCIACION);
				}
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return id;
	}
}
