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


public class EnvTipoEnviosAdm extends MasterBeanAdministrador {

	public static final String K_CORREO_ELECTRONICO = "1";
	public static final String K_CORREO_ORDINARIO = "2";
	public static final String K_FAX = "3";
	public static final String K_SMS = "4";
	public static final String K_BUROSMS = "5";
	static public final String CONS_TIPOENVIO="%%TIPOENVIO%%";
	
	public EnvTipoEnviosAdm(UsrBean usuario)
	{
	    super(EnvTipoEnviosBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvTipoEnviosBean.C_NOMBRE,
            	EnvTipoEnviosBean.C_IDTIPOENVIOS,
            	EnvTipoEnviosBean.C_FECHAMODIFICACION,
            	EnvTipoEnviosBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvTipoEnviosBean.C_IDTIPOENVIOS};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        
        String[] campos = {EnvTipoEnviosBean.C_NOMBRE};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvTipoEnviosBean bean = null;

		try
		{
			bean = new EnvTipoEnviosBean();
			
			bean.setNombre(UtilidadesHash.getString(hash, EnvTipoEnviosBean.C_NOMBRE));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvTipoEnviosBean.C_IDTIPOENVIOS));
			
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

			EnvTipoEnviosBean b = (EnvTipoEnviosBean) bean;
			
			UtilidadesHash.set(htData, EnvTipoEnviosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvTipoEnviosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdTIPOENVIOS(UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvTipoEnviosBean.C_IDTIPOENVIOS + 
        		") AS MAXVALOR FROM " + EnvTipoEnviosBean.T_NOMBRETABLA;
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
