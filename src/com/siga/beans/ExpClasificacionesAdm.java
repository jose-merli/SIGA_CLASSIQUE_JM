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
public class ExpClasificacionesAdm extends MasterBeanAdministrador {

	public ExpClasificacionesAdm(UsrBean usuario)
	{
	    super(ExpClasificacionesBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpClasificacionesBean.C_IDCLASIFICACION,
				ExpClasificacionesBean.C_NOMBRE,
				ExpClasificacionesBean.C_FECHAMODIFICACION,
				ExpClasificacionesBean.C_USUMODIFICACION,
				ExpClasificacionesBean.C_IDINSTITUCION,
				ExpClasificacionesBean.C_IDTIPOEXPEDIENTE
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpClasificacionesBean.C_IDINSTITUCION, ExpClasificacionesBean.C_IDCLASIFICACION,ExpClasificacionesBean.C_IDTIPOEXPEDIENTE};
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
        ExpClasificacionesBean bean = null;

		try
		{
			bean = new ExpClasificacionesBean();
			
			bean.setIdClasificacion(UtilidadesHash.getInteger(hash, ExpClasificacionesBean.C_IDCLASIFICACION));
			bean.setNombre(UtilidadesHash.getString(hash, ExpClasificacionesBean.C_NOMBRE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpClasificacionesBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpClasificacionesBean.C_IDTIPOEXPEDIENTE));
			
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

			ExpClasificacionesBean b = (ExpClasificacionesBean) bean;

			UtilidadesHash.set(htData, ExpClasificacionesBean.C_IDCLASIFICACION, b.getIdClasificacion());
			UtilidadesHash.set(htData, ExpClasificacionesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpClasificacionesBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpClasificacionesBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdClasificacion(String _idtipoexpediente, UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpClasificacionesBean.C_IDCLASIFICACION + 
			") AS MAXVALOR FROM " + ExpClasificacionesBean.T_NOMBRETABLA + 
			" WHERE " + ExpClasificacionesBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
			" AND " + ExpClasificacionesBean.C_IDINSTITUCION + "="+ _usr.getLocation();
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
