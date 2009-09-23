/*
 * Created on Mar 07, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class EnvListaCorreosAdm extends MasterBeanAdministrador {

	public EnvListaCorreosAdm(UsrBean usuario)
	{
	    super(EnvListaCorreosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
				EnvListaCorreosBean.C_IDLISTACORREO,
            	EnvListaCorreosBean.C_NOMBRE,
            	EnvListaCorreosBean.C_IDINSTITUCION,
            	EnvListaCorreosBean.C_DESCRIPCION,
            	EnvListaCorreosBean.C_DINAMICA,
            	EnvListaCorreosBean.C_FECHAMODIFICACION,
            	EnvListaCorreosBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvListaCorreosBean.C_IDINSTITUCION, EnvListaCorreosBean.C_IDLISTACORREO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvListaCorreosBean.C_NOMBRE};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvListaCorreosBean bean = null;

		try
		{
			bean = new EnvListaCorreosBean();
			
			bean.setIdListaCorreo(UtilidadesHash.getInteger(hash, EnvListaCorreosBean.C_IDLISTACORREO));
			bean.setNombre(UtilidadesHash.getString(hash, EnvListaCorreosBean.C_NOMBRE));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvListaCorreosBean.C_DESCRIPCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvListaCorreosBean.C_IDINSTITUCION));
			bean.setDinamica(UtilidadesHash.getString(hash, EnvListaCorreosBean.C_DINAMICA));
			
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

			EnvListaCorreosBean b = (EnvListaCorreosBean) bean;
			
			UtilidadesHash.set(htData, EnvListaCorreosBean.C_IDLISTACORREO, b.getIdListaCorreo());
			UtilidadesHash.set(htData, EnvListaCorreosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvListaCorreosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, EnvListaCorreosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvListaCorreosBean.C_DINAMICA, b.getDinamica());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdListaCorreos(UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvListaCorreosBean.C_IDLISTACORREO + 
        		") AS MAXVALOR FROM " + EnvListaCorreosBean.T_NOMBRETABLA + 
        		" WHERE " + EnvListaCorreosBean.C_IDINSTITUCION + "="+ _usr.getLocation(); 
        int valor=1; // Si no hay registros, es el valor que tomará
        if(rows.find(sql)){
            Hashtable htRow=((Row)rows.get(0)).getRow();
            // El valor devuelto será "" Si no hay registros
            if(!((String)htRow.get("MAXVALOR")).equals("")) {
                Integer valorInt=Integer.valueOf((String)htRow.get("MAXVALOR"));
                valor=valorInt.intValue();
                valor++;
            }            
        }
        return new Integer(valor);        
    }
}
