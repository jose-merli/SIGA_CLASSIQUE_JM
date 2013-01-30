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
public class GenTipoCampoAdm extends MasterBeanAdministrador {

	public GenTipoCampoAdm(UsrBean usuario)
	{
	    super(GenTipoCampoBean.T_NOMBRETABLA, usuario);
	}
    
    protected String[] getCamposBean() {
        
        String[] campos = {
				GenTipoCampoBean.C_IDTIPOCAMPO,
				GenTipoCampoBean.C_DESCRIPCION,
				GenTipoCampoBean.C_FECHAMODIFICACION,
            	GenTipoCampoBean.C_USUMODIFICACION,
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {GenTipoCampoBean.C_IDTIPOCAMPO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        String[] orden = {GenTipoCampoBean.C_DESCRIPCION};
        return orden;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        GenTipoCampoBean bean = null;

		try {
			bean = new GenTipoCampoBean(this.usrbean);
			
			bean.setIdTipoCampo(UtilidadesHash.getInteger(hash, GenTipoCampoBean.C_IDTIPOCAMPO));
			bean.setDescripcion(UtilidadesHash.getString(hash, GenTipoCampoBean.C_DESCRIPCION));
		} catch (Exception e) {
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

		try {
			htData = new Hashtable();

			GenTipoCampoBean b = (GenTipoCampoBean) bean;
			
			UtilidadesHash.set(htData, GenTipoCampoBean.C_IDTIPOCAMPO, b.getIdTipoCampo());
			UtilidadesHash.set(htData, GenTipoCampoBean.C_DESCRIPCION, b.getDescripcion());
		} catch (Exception e) {
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public GenTipoCampoBean getTipoCampoById(int idTipoCampo) throws ClsExceptions{
    	GenTipoCampoBean tipoCampo = null;
    	try{
	    	Hashtable hash = new Hashtable();
	    	hash.put(GenTipoCampoBean.C_IDTIPOCAMPO, idTipoCampo);
	    	Vector v = select(hash);
	    	if (v != null && v.size() > 0)
	    		tipoCampo = (GenTipoCampoBean) v.get(0);
    	} catch (Exception e){
    		throw new ClsExceptions (e, "Error al obtener el tipo de campo con id="+idTipoCampo);
    	}
    	return tipoCampo;
    }

}
