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

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpPermisosTiposExpedientesAdm extends MasterBeanAdministrador {

	public ExpPermisosTiposExpedientesAdm(UsrBean usuario)
	{
	    super(ExpPermisosTiposExpedientesBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                ExpPermisosTiposExpedientesBean.C_IDPERFIL,
				ExpPermisosTiposExpedientesBean.C_IDINSTITUCION,
				ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE,
				ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE,
				ExpPermisosTiposExpedientesBean.C_DERECHOACCESO,
				ExpPermisosTiposExpedientesBean.C_FECHAMODIFICACION,
				ExpPermisosTiposExpedientesBean.C_USUMODIFICACION				
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {ExpPermisosTiposExpedientesBean.C_IDINSTITUCION, ExpPermisosTiposExpedientesBean.C_IDPERFIL,ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE,ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE};
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
        ExpPermisosTiposExpedientesBean bean = null;

		try
		{
			bean = new ExpPermisosTiposExpedientesBean();
			
			bean.setIdPerfil(UtilidadesHash.getString(hash, ExpPermisosTiposExpedientesBean.C_IDPERFIL));
			bean.setIdInstitucionTipoExpediente(UtilidadesHash.getInteger(hash, ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpPermisosTiposExpedientesBean.C_IDINSTITUCION));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE));
			bean.setDerechoAcceso(UtilidadesHash.getInteger(hash, ExpPermisosTiposExpedientesBean.C_DERECHOACCESO));
			
		} catch (Exception e)
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

			ExpPermisosTiposExpedientesBean b = (ExpPermisosTiposExpedientesBean) bean;

			UtilidadesHash.set(htData, ExpPermisosTiposExpedientesBean.C_IDPERFIL, b.getIdPerfil());
			UtilidadesHash.set(htData, ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE, b.getIdInstitucionTipoExpediente());
			UtilidadesHash.set(htData, ExpPermisosTiposExpedientesBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpPermisosTiposExpedientesBean.C_DERECHOACCESO, b.getDerechoAcceso());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Integer getNewIdPerfil(String _idtipoexpediente, String _idinstitucion, String _idinstituciontipoexpediente) throws ClsExceptions{
        RowsContainer rows=new RowsContainer();
        String sql="SELECT MAX(" + ExpPermisosTiposExpedientesBean.C_IDPERFIL + 
			") AS MAXVALOR FROM " + ExpPermisosTiposExpedientesBean.T_NOMBRETABLA + 
			" WHERE " + ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE + "=" +_idtipoexpediente +
			" AND " + ExpPermisosTiposExpedientesBean.C_IDINSTITUCION + "=" + _idinstitucion + 
			" AND " + ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE + "=" + _idinstituciontipoexpediente;
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
    
    public Vector selectPerfiles(String idInstitucionTipoExpediente, String idTipoExpediente, String idInstitucion) throws ClsExceptions 
	{
        Vector vDatos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_EXP_PERMISOS = ExpPermisosTiposExpedientesBean.T_NOMBRETABLA + " PM";
		String T_ADM_PERFIL = AdmPerfilBean.T_NOMBRETABLA + " P";
		
		//NOMBRES COLUMNAS PARA LA JOIN
		
		//Tabla exp_permisos
		String M_IDPERFIL="PM." + ExpPermisosTiposExpedientesBean.C_IDPERFIL;
		String M_IDINSTITUCION = "PM." + ExpPermisosTiposExpedientesBean.C_IDINSTITUCION;
		String M_IDINSTITUCIONTIPOEXPEDIENTE = "PM." + ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
		String M_DERECHOACCESO = "PM." + ExpPermisosTiposExpedientesBean.C_DERECHOACCESO;
		String M_IDTIPOEXPEDIENTE="PM." + ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE;
		
		
		//Tabla adm_perfiles
		String F_IDPERFIL="P." + AdmPerfilBean.C_IDPERFIL;
		String F_IDINSTITUCION = "P." + AdmPerfilBean.C_IDINSTITUCION;
		String F_PERFIL="P." + AdmPerfilBean.C_DESCRIPCION;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String subselect = "SELECT ";	
			subselect += M_DERECHOACCESO;
			subselect += " FROM ";
			subselect += T_EXP_PERMISOS;
			subselect += " WHERE ";
			subselect += M_IDINSTITUCION + " = " + F_IDINSTITUCION + " AND ";
			subselect += M_IDPERFIL + " = " + F_IDPERFIL + " AND ";
			subselect += M_IDINSTITUCIONTIPOEXPEDIENTE + " = " + idInstitucionTipoExpediente + " AND ";
			subselect += M_IDTIPOEXPEDIENTE + " = " + idTipoExpediente;
			
	        String sql = "SELECT ";	        
	        
		    sql += F_PERFIL+" AS PERFIL, ";
		    sql += F_IDPERFIL+", ";
		    sql += "nvl((" + subselect + "),'0') AS DERECHOACCESO";		    
		    
			sql += " FROM " + T_ADM_PERFIL;
		    		    		
			sql += " WHERE " + F_IDINSTITUCION + " = " + idInstitucion;
			sql += " ORDER BY " + F_PERFIL;

			//ClsLogging.writeFileLog("ExpPermisosTiposExpedientesAdm -> QUERY: "+sql,3);
	        
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					ExpPerfilBean bean = new ExpPerfilBean();
					bean.setIdPerfil(fila.getString("IDPERFIL"));
					bean.setNombrePerfil(fila.getString("PERFIL"));
					bean.setDerechoAcceso(fila.getString("DERECHOACCESO"));
					vDatos.add(bean);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return vDatos;
	}
    
}
