/*
 * Created on Mar 15, 2005
 * @author jtacosta
*/
package com.siga.beans;
 
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class EnvDestProgramInformesAdm extends MasterBeanAdministrador {

  
	public EnvDestProgramInformesAdm(UsrBean usuario)
	{
	    super(EnvDestProgramInformesBean.T_NOMBRETABLA, usuario);
	}

	
	
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvDestProgramInformesBean.C_IDINSTITUCION,
                EnvDestProgramInformesBean.C_IDENVIO,
                EnvDestProgramInformesBean.C_IDPROGRAM,
                EnvDestProgramInformesBean.C_IDPERSONA,
                EnvDestProgramInformesBean.C_IDINSTITUCION_PERSONA,
                
            	EnvDestProgramInformesBean.C_FECHAMODIFICACION,
            	EnvDestProgramInformesBean.C_USUMODIFICACION
				};
        
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {

        String[] claves = {EnvDestProgramInformesBean.C_IDPROGRAM,EnvDestProgramInformesBean.C_IDINSTITUCION, EnvDestProgramInformesBean.C_IDENVIO,EnvDestProgramInformesBean.C_IDPERSONA,EnvDestProgramInformesBean.C_IDINSTITUCION_PERSONA};
		return claves;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
     */
    protected String[] getOrdenCampos() {

        String[] campos = {EnvDestProgramInformesBean.C_IDPROGRAM};
		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
     */
    protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
        EnvDestProgramInformesBean bean = null;

		try
		{
			bean = new EnvDestProgramInformesBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvDestProgramInformesBean.C_IDINSTITUCION));
			bean.setIdEnvio(UtilidadesHash.getInteger(hash, EnvDestProgramInformesBean.C_IDENVIO));
			bean.setIdProgram(UtilidadesHash.getInteger(hash, EnvDestProgramInformesBean.C_IDPROGRAM));
			bean.setIdPersona(UtilidadesHash.getLong(hash, EnvDestProgramInformesBean.C_IDPERSONA));
			bean.setIdInstitucionPersona(UtilidadesHash.getInteger(hash, EnvDestProgramInformesBean.C_IDINSTITUCION_PERSONA));
			

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

			EnvDestProgramInformesBean b = (EnvDestProgramInformesBean) bean;

			UtilidadesHash.set(htData, EnvDestProgramInformesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvDestProgramInformesBean.C_IDENVIO, b.getIdEnvio());
			UtilidadesHash.set(htData, EnvDestProgramInformesBean.C_IDPROGRAM, b.getIdProgram());
			UtilidadesHash.set(htData, EnvDestProgramInformesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, EnvDestProgramInformesBean.C_IDINSTITUCION_PERSONA, b.getIdInstitucionPersona());
						

			

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    public Integer getNewIdProgramInformes(UsrBean usr) throws ClsExceptions{
    	return getNewIdProgramInformes(usr.getLocation());
    }

    public Integer getNewIdProgramInformes(String idInstitucion) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + EnvDestProgramInformesBean.C_IDPROGRAM +
        		") AS MAXVALOR FROM " + EnvDestProgramInformesBean.T_NOMBRETABLA +
        		" WHERE " + EnvDestProgramInformesBean.C_IDINSTITUCION + "="+ idInstitucion;
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
    /**
     * 
     * @param estado 1 ó 0. Sera uno cuando se quieran los pagos ya enviados.0 con los pagos pendienmtes de enviar
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */

    public Vector getDestinatariosInformesGenericosProgramados(EnvProgramInformesBean programInformesBean)
			throws ClsExceptions {

		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT EPI.IDPROGRAM,EPI.IDENVIO,EPI.IDINSTITUCION, ");
		sql.append(" EPI.IDPERSONA, EPI.IDINSTITUCION_PERSONA   ");
		
		sql.append(" FROM ENV_DESTPROGRAMINFORMES EPI  ");
		Hashtable htCodigos = new Hashtable();
		int keyContador = 0;
		
		
		sql.append(" WHERE EPI.IDINSTITUCION = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), programInformesBean.getIdInstitucion());
		sql.append(keyContador);
		sql.append(" AND EPI.IDENVIO = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), programInformesBean.getIdEnvio());
		sql.append(keyContador);
		sql.append(" AND EPI.IDPROGRAM = :");
		keyContador++;
		htCodigos.put(new Integer(keyContador), programInformesBean.getIdProgram());
		sql.append(keyContador);
		
		
		// Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try {
			rc = new RowsContainer();
			if (rc.queryBind(sql.toString(),htCodigos)) {
				EnvDestProgramInformesBean destInfGenerico = null;
				EnvValorCampoClaveAdm valorCampoClaveAdm = new EnvValorCampoClaveAdm(usrbean);
				for (int i = 0; i < rc.size(); i++) {
					
					Row row = (Row) rc.get(i);
					Hashtable htFila = row.getRow(); 
					destInfGenerico = new EnvDestProgramInformesBean();
					destInfGenerico.setIdInstitucion(UtilidadesHash.getInteger(htFila, EnvDestProgramInformesBean.C_IDINSTITUCION));
					destInfGenerico.setIdEnvio(UtilidadesHash.getInteger(htFila, EnvDestProgramInformesBean.C_IDENVIO));
					destInfGenerico.setIdProgram(UtilidadesHash.getInteger(htFila, EnvDestProgramInformesBean.C_IDPROGRAM));
					
					
					destInfGenerico.setIdPersona(UtilidadesHash.getLong(htFila, EnvDestProgramInformesBean.C_IDPERSONA));
					destInfGenerico.setIdInstitucionPersona(UtilidadesHash.getInteger(htFila, EnvDestProgramInformesBean.C_IDINSTITUCION_PERSONA));
					ArrayList alValores = (ArrayList) valorCampoClaveAdm.getValoresClavesCampos(destInfGenerico);
					destInfGenerico.setClavesDestinatario(alValores);
					
					
					datos.add(destInfGenerico);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.getDestinatariosInformesGenericosProgramados");
		}
		return datos;
	}
   


}