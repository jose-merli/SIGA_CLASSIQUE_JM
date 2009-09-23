/*
 * Created on Mar 29, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class EnvListaCorreosEnviosAdm extends MasterBeanAdministrador {

	public EnvListaCorreosEnviosAdm(UsrBean usuario)
	{
	    super(EnvListaCorreosEnviosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
				EnvListaCorreosEnviosBean.C_IDLISTACORREO,
				EnvListaCorreosEnviosBean.C_IDENVIO,
				EnvListaCorreosEnviosBean.C_IDINSTITUCION,
				EnvListaCorreosEnviosBean.C_FECHAMODIFICACION,
				EnvListaCorreosEnviosBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvListaCorreosEnviosBean.C_IDINSTITUCION, 
                		   EnvListaCorreosEnviosBean.C_IDLISTACORREO,
                		   EnvListaCorreosEnviosBean.C_IDENVIO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvListaCorreosEnviosBean.C_IDLISTACORREO};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvListaCorreosEnviosBean bean = null;

		try
		{
			bean = new EnvListaCorreosEnviosBean();
			
			bean.setIdListaCorreo(UtilidadesHash.getInteger(hash, EnvListaCorreosEnviosBean.C_IDLISTACORREO));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvListaCorreosEnviosBean.C_IDENVIO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvListaCorreosEnviosBean.C_IDINSTITUCION));
			
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

			EnvListaCorreosEnviosBean b = (EnvListaCorreosEnviosBean) bean;
			
			UtilidadesHash.set(htData, EnvListaCorreosEnviosBean.C_IDLISTACORREO, b.getIdListaCorreo());
			UtilidadesHash.set(htData, EnvListaCorreosEnviosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvListaCorreosEnviosBean.C_IDINSTITUCION, b.getIdInstitucion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
	/** 
	 * Comprueba si la lista ya ha sido insertada en la tabla de listaCorreosEnvios
	 * @param  idEnvio id del envio
	 * @param  idInstitucion id de la institucion
	 * @param  idListaCorreo id de la lista de correo
	 * @return  boolean con el resultado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean existeLista(String idEnvio, 
								String idInstitucion, 
								String idListaCorreo) 
		throws ClsExceptions, SIGAException{
		try {
			Vector v = this.select(" WHERE " + EnvListaCorreosEnviosBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + EnvListaCorreosEnviosBean.C_IDENVIO + " = " + idEnvio +
			        				 " AND " + EnvListaCorreosEnviosBean.C_IDLISTACORREO + " = " + idListaCorreo);
		    
			if ((v != null) && (v.size()>0)) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
    
}
