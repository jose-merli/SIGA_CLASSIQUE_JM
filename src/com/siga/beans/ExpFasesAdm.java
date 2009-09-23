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
public class ExpFasesAdm extends MasterBeanAdministrador {

	public ExpFasesAdm(UsrBean usuario)
	{
	    super(ExpFasesBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpFasesBean.C_IDFASE,
				ExpFasesBean.C_NOMBRE,
				ExpFasesBean.C_DESCRIPCION,
				ExpFasesBean.C_FECHAMODIFICACION,
				ExpFasesBean.C_USUMODIFICACION,
				ExpFasesBean.C_IDINSTITUCION,
				ExpFasesBean.C_DIASANTELACION,
				ExpFasesBean.C_DIASVENCIMIENTO,
				ExpFasesBean.C_IDTIPOEXPEDIENTE
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpFasesBean.C_IDINSTITUCION, ExpFasesBean.C_IDFASE,ExpFasesBean.C_IDTIPOEXPEDIENTE};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        // TODO Auto-generated method stub
    	String[] orden = {ExpFasesBean.C_NOMBRE};
        return orden;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpFasesBean bean = null;

		try
		{
			bean = new ExpFasesBean();
			
			bean.setIdFase(UtilidadesHash.getInteger(hash, ExpFasesBean.C_IDFASE));
			bean.setNombre(UtilidadesHash.getString(hash, ExpFasesBean.C_NOMBRE));
			bean.setDescripcion(UtilidadesHash.getString(hash, ExpFasesBean.C_DESCRIPCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpFasesBean.C_IDINSTITUCION));
			bean.setDiasAntelacion(UtilidadesHash.getInteger(hash, ExpFasesBean.C_DIASANTELACION));
			bean.setDiasVencimiento(UtilidadesHash.getInteger(hash, ExpFasesBean.C_DIASVENCIMIENTO));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpFasesBean.C_IDTIPOEXPEDIENTE));
			
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

			ExpFasesBean b = (ExpFasesBean) bean;

			UtilidadesHash.set(htData, ExpFasesBean.C_IDFASE, b.getIdFase());
			UtilidadesHash.set(htData, ExpFasesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ExpFasesBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ExpFasesBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpFasesBean.C_DIASANTELACION, b.getDiasAntelacion());			
			UtilidadesHash.set(htData, ExpFasesBean.C_DIASVENCIMIENTO, b.getDiasVencimiento());			
			UtilidadesHash.set(htData, ExpFasesBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    public Integer getNewIdFase(String _idtipoexpediente, UsrBean _usr) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpFasesBean.C_IDFASE + 
        		") AS MAXVALOR FROM " + ExpFasesBean.T_NOMBRETABLA + 
        		" WHERE " + ExpFasesBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
        		" AND " + ExpFasesBean.C_IDINSTITUCION + "="+ _usr.getLocation(); 
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
