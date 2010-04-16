/*
 * Created on Mar 15, 2005
 * @author jmgrau
*/
package com.siga.beans;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvEnvioProgramadoAdm extends MasterBeanAdministrador {

  public final static int ESTADO_INICIAL = 1;
  public final static int ESTADO_PROCESADO = 2;
  public final static int ESTADO_PROCESADO_ERRORES= 3;
  public final static int ESTADO_PENDIENTE_AUTOMATICO = 4;

  public final static String NO_GENERAR = "N";
  public final static String FECHA_CREACION = "C";
  public final static String FECHA_PROGRAMADA = "P";

	public EnvEnvioProgramadoAdm(UsrBean usuario)
	{
	    super(EnvEnvioProgramadoBean.T_NOMBRETABLA, usuario);
	}

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvEnvioProgramadoBean.C_IDINSTITUCION,
                EnvEnvioProgramadoBean.C_IDENVIO,
                EnvEnvioProgramadoBean.C_IDTIPOENVIOS,
                EnvEnvioProgramadoBean.C_IDPLANTILLAENVIOS,
                EnvEnvioProgramadoBean.C_IDPLANTILLA,
                EnvEnvioProgramadoBean.C_ESTADO,
                EnvEnvioProgramadoBean.C_NOMBRE,
                EnvEnvioProgramadoBean.C_FECHAPROGRAMADA,
            	EnvEnvioProgramadoBean.C_FECHAMODIFICACION,
            	EnvEnvioProgramadoBean.C_USUMODIFICACION
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvEnvioProgramadoBean.C_IDINSTITUCION, EnvEnvioProgramadoBean.C_IDENVIO};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvEnvioProgramadoBean.C_FECHAPROGRAMADA};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvEnvioProgramadoBean bean = null;

		try
		{
			bean = new EnvEnvioProgramadoBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvEnvioProgramadoBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvEnvioProgramadoBean.C_IDENVIO));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvEnvioProgramadoBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvEnvioProgramadoBean.C_IDPLANTILLAENVIOS));
			bean.setIdPlantilla(UtilidadesHash.getInteger(hash, EnvEnvioProgramadoBean.C_IDPLANTILLA));
			bean.setEstado(UtilidadesHash.getString(hash, EnvEnvioProgramadoBean.C_ESTADO));
			bean.setNombre(UtilidadesHash.getString(hash, EnvEnvioProgramadoBean.C_NOMBRE));
			bean.setFechaProgramada(UtilidadesHash.getString(hash, EnvEnvioProgramadoBean.C_FECHAPROGRAMADA));
			
			
			

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

			EnvEnvioProgramadoBean b = (EnvEnvioProgramadoBean) bean;

			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_ESTADO, b.getEstado());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvEnvioProgramadoBean.C_FECHAPROGRAMADA, b.getFechaProgramada());
			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    public Integer getNewIdEnvio(String idInstitucion) throws ClsExceptions{
    	
        Long idEnvio = getSecuenciaNextVal(EnvEnvioProgramadoBean.SEQ_ENV_ENVIOPROGRAMADO);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        return new Integer(formato.format(new Date())+idEnvio);
    }

    public Integer getNewIdEnvio(UsrBean usrBean) throws ClsExceptions{
    	return getNewIdEnvio(usrBean.getLocation());
    }
    
    
    


}