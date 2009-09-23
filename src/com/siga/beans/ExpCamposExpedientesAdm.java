/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpCamposExpedientesAdm extends MasterBeanAdministrador {

	public ExpCamposExpedientesAdm(UsrBean usuario)
	{
	    super(ExpCamposExpedientesBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpCamposExpedientesBean.C_IDCAMPO,
                ExpCamposExpedientesBean.C_NOMBRE,
				ExpCamposExpedientesBean.C_TIPOCAMPO,
				ExpCamposExpedientesBean.C_FECHAMODIFICACION,
				ExpCamposExpedientesBean.C_USUMODIFICACION				
				};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpCamposExpedientesBean.C_IDCAMPO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpCamposExpedientesBean bean = null;

		try
		{
			bean = new ExpCamposExpedientesBean();
			
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ExpCamposExpedientesBean.C_IDCAMPO));
			bean.setNombre(UtilidadesHash.getString(hash, ExpCamposExpedientesBean.C_NOMBRE));
			bean.setTipoCampo(UtilidadesHash.getString(hash, ExpCamposExpedientesBean.C_TIPOCAMPO));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpCamposExpedientesBean.C_FECHAMODIFICACION));
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpCamposExpedientesBean.C_USUMODIFICACION));
			bean.setProceso(UtilidadesHash.getString(hash, ExpCamposExpedientesBean.C_PROCESO));
			
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
     */
    protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpCamposExpedientesBean b = (ExpCamposExpedientesBean) bean;

			UtilidadesHash.set(htData, ExpCamposExpedientesBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ExpCamposExpedientesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpCamposExpedientesBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, ExpCamposExpedientesBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpCamposExpedientesBean.C_USUMODIFICACION, b.getUsuModificacion());
			UtilidadesHash.set(htData, ExpCamposExpedientesBean.C_PROCESO, b.getProceso());		
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Vector selectCampos()throws ClsExceptions {        
        String where = ExpCamposExpedientesBean.C_TIPOCAMPO + "= C";
        Vector resultado = new Vector();
        try {
            resultado = this.select(where);            
        } catch (ClsExceptions e) {
            throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D.");            
        }
        return resultado;
    }
    
    public Vector selectPestanas()throws ClsExceptions {        
        String where = ExpCamposExpedientesBean.C_TIPOCAMPO + "= P";
        Vector resultado = new Vector();
        try {
            resultado = this.select(where);            
        } catch (ClsExceptions e) {
            throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D.");            
        }
        return resultado;
    }
    
}
