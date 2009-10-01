/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpRolesAdm extends MasterBeanAdministrador {

	public ExpRolesAdm(UsrBean usuario)
	{
	    super(ExpRolesBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    public String[] getCamposBean() {
        String[] campos = {
                ExpRolesBean.C_IDROL,
				ExpRolesBean.C_NOMBRE,
				ExpRolesBean.C_IDINSTITUCION,
				ExpRolesBean.C_IDTIPOEXPEDIENTE,
				ExpRolesBean.C_FECHAMODIFICACION,
				ExpRolesBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    public String[] getClavesBean() {
        
        String[] claves = {ExpRolesBean.C_IDINSTITUCION, ExpRolesBean.C_IDROL, ExpRolesBean.C_IDTIPOEXPEDIENTE};
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
        ExpRolesBean bean = null;

		try
		{
			bean = new ExpRolesBean();
			
			bean.setIdRol(UtilidadesHash.getInteger(hash, ExpRolesBean.C_IDROL));
			bean.setNombre(UtilidadesHash.getString(hash, ExpRolesBean.C_NOMBRE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpRolesBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpRolesBean.C_IDTIPOEXPEDIENTE));
			
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

			ExpRolesBean b = (ExpRolesBean) bean;

			UtilidadesHash.set(htData, ExpRolesBean.C_IDROL, b.getIdRol());
			UtilidadesHash.set(htData, ExpRolesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpRolesBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpRolesBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdRol(String _idtipoexpediente, UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpRolesBean.C_IDROL + 
			") AS MAXVALOR FROM " + ExpRolesBean.T_NOMBRETABLA + 
			" WHERE " + ExpRolesBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
			" AND " + ExpRolesBean.C_IDINSTITUCION + "="+ _usr.getLocation();
        
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
