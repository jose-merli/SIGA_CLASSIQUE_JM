/*
 * Created on Mar 15, 2005
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;


public class EnvEstadoEnvioAdm extends MasterBeanAdministrador {
	public static String K_ESTADOENVIO_PENDIENTE_MANUAL="1";
	public static String K_ESTADOENVIO_PROCESADO="2";
	public static String K_ESTADOENVIO_PROCESADOCONERRORES="3";
	public static String K_ESTADOENVIO_PENDIENTE_AUTOMATICO="4";
	public static Integer K_ESTADOENVIO_PROCESANDO = new Integer(5);
	public static String K_ESTADOENVIO_ARCHIVADO="6";
	
	public EnvEstadoEnvioAdm(UsrBean usuario)
	{
	    super(EnvEstadoEnvioBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvEstadoEnvioBean.C_NOMBRE,
            	EnvEstadoEnvioBean.C_IDESTADO,
            	EnvEstadoEnvioBean.C_FECHAMODIFICACION,
            	EnvEstadoEnvioBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvEstadoEnvioBean.C_IDESTADO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvEstadoEnvioBean.C_NOMBRE};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvEstadoEnvioBean bean = null;

		try
		{
			bean = new EnvEstadoEnvioBean();
			
			bean.setNombre(UtilidadesHash.getString(hash, EnvEstadoEnvioBean.C_NOMBRE));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, EnvEstadoEnvioBean.C_IDESTADO));
			
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

			EnvEstadoEnvioBean b = (EnvEstadoEnvioBean) bean;
			
			UtilidadesHash.set(htData, EnvEstadoEnvioBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvEstadoEnvioBean.C_IDESTADO, b.getIdEstado());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdEstado(UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvEstadoEnvioBean.C_IDESTADO + 
        		") AS MAXVALOR FROM " + EnvEstadoEnvioBean.T_NOMBRETABLA;
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
