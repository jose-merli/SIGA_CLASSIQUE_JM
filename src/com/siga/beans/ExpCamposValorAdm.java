/*
 * Created on Dec 27, 2004
 * @author jmgrau 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.expedientes.form.PestanaConfigurableForm;
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpCamposValorAdm extends MasterBeanAdministrador {

	public ExpCamposValorAdm(UsrBean usuario)
	{
	    super(ExpCamposValorBean.T_NOMBRETABLA, usuario);
	}
    
    protected String[] getCamposBean() {
        
        String[] campos = {
				ExpCamposValorBean.C_IDINSTITUCION,
				ExpCamposValorBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpCamposValorBean.C_IDTIPOEXPEDIENTE,
				ExpCamposValorBean.C_IDCAMPO,
				ExpCamposValorBean.C_NUMEROEXPEDIENTE,
				ExpCamposValorBean.C_ANIOEXPEDIENTE,
				ExpCamposValorBean.C_IDPESTANACONF,
				ExpCamposValorBean.C_IDCAMPOCONF,
				ExpCamposValorBean.C_VALOR
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpCamposValorBean.C_IDINSTITUCION,
				ExpCamposValorBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpCamposValorBean.C_IDTIPOEXPEDIENTE,
				ExpCamposValorBean.C_NUMEROEXPEDIENTE,
				ExpCamposValorBean.C_ANIOEXPEDIENTE,
				ExpCamposValorBean.C_IDPESTANACONF,
				ExpCamposValorBean.C_IDCAMPOCONF};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        ExpCamposValorBean bean = null;

		try
		{
			bean = new ExpCamposValorBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_IDINSTITUCION));
			bean.setIdInstitucion_TipoExpediente(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_IDTIPOEXPEDIENTE));
			bean.setIdCampo(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_IDCAMPO));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_ANIOEXPEDIENTE));
			bean.setIdPestanaConf(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_IDPESTANACONF));
			bean.setIdCampoConf(UtilidadesHash.getInteger(hash, ExpCamposValorBean.C_IDCAMPOCONF));
			bean.setValor(UtilidadesHash.getString(hash, ExpCamposValorBean.C_VALOR));
			
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

			ExpCamposValorBean b = (ExpCamposValorBean) bean;
			
			UtilidadesHash.set(htData, ExpCamposValorBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_TipoExpediente());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_IDPESTANACONF, b.getIdPestanaConf());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_IDCAMPOCONF, b.getIdCampoConf());
			UtilidadesHash.set(htData, ExpCamposValorBean.C_VALOR, b.getValor());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    public void guardarValor(PestanaConfigurableForm form, String idCampoConf, String valor) throws ClsExceptions {
        // comprobamos si existe. SI es asi modificamos, si no lo creamos.
        Vector salida = new Vector();
		Hashtable codigos = new  Hashtable();
		codigos.put(new Integer(1),this.usrbean.getLocation());
		codigos.put(new Integer(2),form.getIdInstitucion());
		codigos.put(new Integer(3),form.getIdTipoExpediente());
		codigos.put(new Integer(4),form.getNumeroExpediente());
		codigos.put(new Integer(5),form.getAnioExpediente());
		codigos.put(new Integer(6),form.getIdCampo());
		codigos.put(new Integer(7),form.getIdPestanaConf());
		codigos.put(new Integer(8),idCampoConf);
		
		
		try {		
			String sql = " where idinstitucion=:1 " + 
			    "       and   idinstitucion_tipoexpediente=:2 " + 
			    "       and   idtipoexpediente=:3 " + 
			    "       and   numeroexpediente=:4 " + 
			    "       and   anioexpediente=:5 " + 
			    "       and   idcampo=:6 " + 
			    "       and   idpestanaconf=:7 " + 
			    "       and   idcampoconf=:8";

			// RGG cambio visibilidad
			Vector v = this.selectBind(sql,codigos);
			if (v!=null && v.size()>0) {
				ExpCamposValorBean bean = (ExpCamposValorBean) v.get(0);
				bean.setValor(valor.trim());
				if (!this.updateDirect(bean)) {
				    throw new ClsExceptions("Error al actualizar campo valor. "+this.getError());
				}
				
			} else {
			    // hay que insertarlo.
			    ExpCamposValorBean bean = new ExpCamposValorBean();
			    bean.setIdInstitucion(new Integer(this.usrbean.getLocation()));
			    bean.setIdInstitucion_TipoExpediente(new Integer(form.getIdInstitucion()));
			    bean.setIdTipoExpediente(new Integer(form.getIdTipoExpediente()));
			    bean.setNumeroExpediente(new Integer(form.getNumeroExpediente()));
			    bean.setAnioExpediente(new Integer(form.getAnioExpediente()));
			    bean.setIdCampo(new Integer(form.getIdCampo()));
			    bean.setIdPestanaConf(new Integer(form.getIdPestanaConf()));
			    bean.setIdCampoConf(new Integer(idCampoConf));
			    bean.setValor(valor);
				if (!this.insert(bean)) {
				    throw new ClsExceptions("Error al insertar campo valor. "+this.getError());
				}
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'guardarValor' en B.D.");		
		}
		
    }


}
