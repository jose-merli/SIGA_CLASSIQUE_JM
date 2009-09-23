/*
 * Created on Mar 15, 2005
 * @author jtacosta
*/
package com.siga.beans;
 
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvInformesGenericosAdm extends MasterBeanAdministrador {


	public EnvInformesGenericosAdm(UsrBean usuario)
	{
	    super(EnvInformesGenericosBean.T_NOMBRETABLA, usuario);
	}

	
	
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvInformesGenericosBean.C_IDINSTITUCION,
                EnvInformesGenericosBean.C_IDENVIO,
                EnvInformesGenericosBean.C_IDPROGRAM,
                EnvInformesGenericosBean.C_IDPLANTILLA,
            	EnvInformesGenericosBean.C_FECHAMODIFICACION,
            	EnvInformesGenericosBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvInformesGenericosBean.C_IDPROGRAM,EnvInformesGenericosBean.C_IDINSTITUCION, EnvInformesGenericosBean.C_IDENVIO,EnvInformesGenericosBean.C_IDPLANTILLA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvInformesGenericosBean.C_IDPROGRAM};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvInformesGenericosBean bean = null;

		try
		{
			bean = new EnvInformesGenericosBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvInformesGenericosBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvInformesGenericosBean.C_IDENVIO));
			bean.setIdProgram(UtilidadesHash.getInteger(hash, EnvInformesGenericosBean.C_IDPROGRAM));
			bean.setIdPlantilla(UtilidadesHash.getString(hash, EnvInformesGenericosBean.C_IDPLANTILLA));

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
    public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
        Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			EnvInformesGenericosBean b = (EnvInformesGenericosBean) bean;

			UtilidadesHash.set(htData, EnvInformesGenericosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvInformesGenericosBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvInformesGenericosBean.C_IDPROGRAM, b.getIdProgram());
			UtilidadesHash.set(htData, EnvInformesGenericosBean.C_IDPLANTILLA, b.getIdPlantilla());

			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

  
    /**
     * 
     * @param estado 1 ó 0. Sera uno cuando se quieran los pagos ya enviados.0 con los pagos pendienmtes de enviar
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */

    public Vector getPlantillasInformesGenericosProgramados(EnvProgramInformesBean programInformes)
			throws ClsExceptions {

    	Vector vSalida = null;
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer select = new StringBuffer();
			select.append(" ");
			select.append(" SELECT I.* ");
			select.append(" FROM ENV_INFORMESGENERICOS T, ADM_INFORME I ");
			select.append(" WHERE T.IDINSTITUCION = I.IDINSTITUCION ");
			select.append(" AND T.IDPLANTILLA = I.IDPLANTILLA ");

			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdEnvio());
			select.append(" AND T.IDENVIO = :");
			select.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdProgram());
			select.append(" AND T.IDPROGRAM = :");
			select.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdInstitucion());
			select.append(" AND T.IDINSTITUCION = :");
			select.append(keyContador);
			
			
			
			
			select.append(" UNION (SELECT I.* ");
			select.append(" FROM ENV_INFORMESGENERICOS T, ADM_INFORME I ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), new Integer(0));
			select.append(" WHERE I.IDINSTITUCION = :");
			select.append(keyContador);
			
			select.append(" AND T.IDPLANTILLA = I.IDPLANTILLA ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdEnvio());
			select.append(" AND T.IDENVIO = :");
			select.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdProgram());
			select.append(" AND T.IDPROGRAM = :");
			select.append(keyContador);
			
			select.append(" AND (T.IDINSTITUCION, T.IDPLANTILLA) NOT IN ");
			select.append(" (SELECT TP.IDINSTITUCION, TP.IDPLANTILLA ");
			select.append(" FROM ENV_INFORMESGENERICOS TP, ADM_INFORME TI ");
			select.append(" WHERE TP.IDINSTITUCION = TI.IDINSTITUCION ");
			select.append(" AND TP.IDPLANTILLA = TI.IDPLANTILLA ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdEnvio());
			select.append(" AND TP.IDENVIO = :"); 
			select.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdProgram());
			select.append(" AND TP.IDPROGRAM = :");
			select.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), programInformes.getIdInstitucion());
			select.append(" AND TP.IDINSTITUCION = :");
			select.append(keyContador);
			
			
			select.append(")) ");
			
				
			
			Vector datos = this.selectGenericoBind(select.toString(), htCodigos);
			if (datos==null || datos.size()==0) {
				throw new ClsExceptions ("No existe el informe que se busca. ID=");
			}
			else {
				AdmInformeBean salida = null;
				vSalida = new  Vector();
				for (int i = 0; i < datos.size(); i++) {
					Hashtable ht = (Hashtable) datos.get(i);
					salida = new AdmInformeBean();
					salida.setAlias				((String)ht.get(AdmInformeBean.C_ALIAS));
					salida.setDescripcion		((String)ht.get(AdmInformeBean.C_DESCRIPCION));
					salida.setDirectorio		((String)ht.get(AdmInformeBean.C_DIRECTORIO));
					salida.setIdInstitucion		(new Integer((String)ht.get(AdmInformeBean.C_IDINSTITUCION)));
					salida.setIdPlantilla		((String)ht.get(AdmInformeBean.C_IDPLANTILLA));
					salida.setIdTipoInforme		((String)ht.get(AdmInformeBean.C_IDTIPOINFORME));
					salida.setNombreFisico		((String)ht.get(AdmInformeBean.C_NOMBREFISICO));
					salida.setNombreSalida		((String)ht.get(AdmInformeBean.C_NOMBRESALIDA));
					salida.setPreseleccionado	((String)ht.get(AdmInformeBean.C_PRESELECCIONADO));
					salida.setVisible			((String)ht.get(AdmInformeBean.C_VISIBLE));
					salida.setASolicitantes		((String)ht.get(AdmInformeBean.C_ASOLICITANTES));	
					vSalida.add(salida);
				}
				
			}
		}
		catch (ClsExceptions e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la plantilla: "+ e.toString());
		}
		return vSalida;
    	
    	
    	
		
	}
   


}