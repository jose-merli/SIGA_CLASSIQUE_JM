/*
 * Created on May 19, 2005
 * @author emilio.grau
 *
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
 * Administrador del bean EnvListaCorreoConsultaBean
 */
public class EnvListaCorreoConsultaAdm extends MasterBeanAdministrador {

	public EnvListaCorreoConsultaAdm(UsrBean usuario)
	{
	    super(EnvListaCorreoConsultaBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
        		EnvListaCorreoConsultaBean.C_IDINSTITUCION,
            	EnvListaCorreoConsultaBean.C_IDCONSULTA,
				EnvListaCorreoConsultaBean.C_IDLISTACORREO,
	      		EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON
	      	  			};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {
        		EnvListaCorreoConsultaBean.C_IDINSTITUCION,
            	EnvListaCorreoConsultaBean.C_IDCONSULTA,
				EnvListaCorreoConsultaBean.C_IDLISTACORREO,
	      		EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON				
				};
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
        EnvListaCorreoConsultaBean bean = null;

		try
		{
			bean = new EnvListaCorreoConsultaBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvListaCorreoConsultaBean.C_IDINSTITUCION));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, EnvListaCorreoConsultaBean.C_IDCONSULTA));
			bean.setIdListaCorreo(UtilidadesHash.getInteger(hash, EnvListaCorreoConsultaBean.C_IDLISTACORREO));
			bean.setIdInstitucionCon(UtilidadesHash.getInteger(hash, EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON));
			
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

			EnvListaCorreoConsultaBean b = (EnvListaCorreoConsultaBean) bean;
			
			UtilidadesHash.set(htData, EnvListaCorreoConsultaBean.C_IDINSTITUCION, b.getIdInstitucion());			
			UtilidadesHash.set(htData, EnvListaCorreoConsultaBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, EnvListaCorreoConsultaBean.C_IDLISTACORREO, b.getIdListaCorreo());
			UtilidadesHash.set(htData, EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON, b.getIdInstitucionCon());			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    
	/** Funcion selectConsultasListas (String idInstitucion, String idListaCorreo)
	 * Devuelve las consultas correspondientes a listas de correo
	 * @param  
	 * @return vector con los datos de la consulta  
	 * */
	public Vector selectConsultasListas(String idInstitucion, String idListaCorreo) throws ClsExceptions 
	{
		Vector datos = new Vector();
				
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String sql = "SELECT ";	
		    sql += "E."+EnvListaCorreoConsultaBean.C_IDINSTITUCION+", ";
		    sql += "E."+EnvListaCorreoConsultaBean.C_IDCONSULTA+", ";
		    sql += "E."+EnvListaCorreoConsultaBean.C_IDLISTACORREO+", ";
		    sql += "E."+EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON+", ";
		    sql += "C."+ConConsultaBean.C_DESCRIPCION;
		    
			sql += " FROM ";
		    sql += EnvListaCorreoConsultaBean.T_NOMBRETABLA+" E, "+ConConsultaBean.T_NOMBRETABLA+" C";
		    		    		
			sql += " WHERE ";
			sql += "C."+ConConsultaBean.C_IDCONSULTA+"=E."+EnvListaCorreoConsultaBean.C_IDCONSULTA+" AND ";
			sql += "C."+ConConsultaBean.C_IDINSTITUCION+"=E."+EnvListaCorreoConsultaBean.C_IDINSTITUCION_CON+" AND ";
			sql += "E."+EnvListaCorreoConsultaBean.C_IDINSTITUCION+"="+idInstitucion+" AND ";
			sql += "E."+EnvListaCorreoConsultaBean.C_IDLISTACORREO+"="+idListaCorreo;
			
			sql += " ORDER BY C."+ConConsultaBean.C_DESCRIPCION;
			
			ClsLogging.writeFileLog("ConConsultaAdm, sql: "+sql,3);
						
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
}
