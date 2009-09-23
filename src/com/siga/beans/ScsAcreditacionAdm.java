/*
 * Created on 01-feb-2006
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
 * @author s230298
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsAcreditacionAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public ScsAcreditacionAdm(UsrBean usuario) {
		super(ScsAcreditacionBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsAcreditacionBean.C_DESCRIPCION,
							ScsAcreditacionBean.C_FECHAMODIFICACION,
							ScsAcreditacionBean.C_IDACREDITACION,
							ScsAcreditacionBean.C_IDTIPOACREDITACION,							
							ScsAcreditacionBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsAcreditacionBean.C_IDACREDITACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsAcreditacionBean bean = null;
		try{
			bean = new ScsAcreditacionBean();
			bean.setDescripcion(UtilidadesHash.getString(hash, ScsAcreditacionBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsAcreditacionBean.C_FECHAMODIFICACION));
			bean.setIdAcreditacion(UtilidadesHash.getInteger(hash, ScsAcreditacionBean.C_IDACREDITACION));
			bean.setIdTipoAcreditacion(UtilidadesHash.getInteger(hash, ScsAcreditacionBean.C_IDTIPOACREDITACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ScsAcreditacionBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsAcreditacionBean b = (ScsAcreditacionBean) bean;
			UtilidadesHash.set(hash, ScsAcreditacionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsAcreditacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsAcreditacionBean.C_IDACREDITACION, b.getIdAcreditacion());
			UtilidadesHash.set(hash, ScsAcreditacionBean.C_IDTIPOACREDITACION, b.getIdTipoAcreditacion());
			UtilidadesHash.set(hash, ScsAcreditacionBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	public Vector getAcreditacionDeProcedimiento(Integer idInstitucion , String idProcedimiento)throws ClsExceptions, SIGAException{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			String sql = " SELECT * " + 
						 " FROM "  + ScsAcreditacionBean.T_NOMBRETABLA + ", " +  ScsAcreditacionProcedimientoBean.T_NOMBRETABLA +
						 " WHERE " + ScsAcreditacionBean.T_NOMBRETABLA +"." + ScsAcreditacionBean.C_IDACREDITACION + " = " + ScsAcreditacionProcedimientoBean.T_NOMBRETABLA + "." + ScsAcreditacionProcedimientoBean.C_IDACREDITACION;
			
			if (idInstitucion != null) {
				sql += " AND " + ScsAcreditacionProcedimientoBean.T_NOMBRETABLA + "." + ScsAcreditacionProcedimientoBean.C_IDINSTITUCION + " = " + idInstitucion;
			}
			if ((idProcedimiento != null) && (!idProcedimiento.equals(""))){ 
				 sql += " AND " + ScsAcreditacionProcedimientoBean.T_NOMBRETABLA + "." + ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO + " = " + idProcedimiento;
			}
				
            rc = this.find(sql);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) {
//						ScsAcreditacionBean b = (ScsAcreditacionBean) this.hashTableToBean(registro); 
//						if (b != null) 
						v.add(registro);
					}
				}
			}
		}

		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getDatosPersonales");
		}
		return v;
	}

}
