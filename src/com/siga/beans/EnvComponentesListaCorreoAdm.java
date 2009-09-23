/*
 * Created on Mar 07, 2005
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
import com.siga.general.SIGAException;


public class EnvComponentesListaCorreoAdm extends MasterBeanAdministrador {

	public EnvComponentesListaCorreoAdm(UsrBean usuario)
	{
	    super(EnvComponentesListaCorreoBean.T_NOMBRETABLA, usuario);
	}
    
    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
     */
    protected String[] getCamposBean() {
        String[] campos = {
                EnvComponentesListaCorreoBean.C_IDLISTACORREO,
                EnvComponentesListaCorreoBean.C_IDINSTITUCION,
                EnvComponentesListaCorreoBean.C_IDPERSONA,
                EnvComponentesListaCorreoBean.C_FECHAMODIFICACION,
                EnvComponentesListaCorreoBean.C_USUMODIFICACION				
				};

		return campos;
    }

    /* (non-Javadoc)
     * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
     */
    protected String[] getClavesBean() {
        
        String[] claves = {EnvComponentesListaCorreoBean.C_IDINSTITUCION, 
                		   EnvComponentesListaCorreoBean.C_IDLISTACORREO,
                		   EnvComponentesListaCorreoBean.C_IDPERSONA};
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
        EnvComponentesListaCorreoBean bean = null;

		try
		{
			bean = new EnvComponentesListaCorreoBean();
			
			bean.setIdListaCorreo(UtilidadesHash.getInteger(hash, EnvComponentesListaCorreoBean.C_IDLISTACORREO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvComponentesListaCorreoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash, EnvComponentesListaCorreoBean.C_IDPERSONA));
			
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

			EnvComponentesListaCorreoBean b = (EnvComponentesListaCorreoBean) bean;
			
			UtilidadesHash.set(htData, EnvComponentesListaCorreoBean.C_IDLISTACORREO, b.getIdListaCorreo());
			UtilidadesHash.set(htData, EnvComponentesListaCorreoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvComponentesListaCorreoBean.C_IDPERSONA, b.getIdPersona());
			
		} catch (Exception e)
		{
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
    
    public Vector obtenerComponentesLista(Integer idInstitucion, Integer idLista) throws ClsExceptions{
        Vector datos = new Vector();
		
		//NOMBRES TABLAS PARA LA JOIN
		String T_ENV_COMPONENTESLISTACORREO = EnvComponentesListaCorreoBean.T_NOMBRETABLA + " CO";
		String T_CEN_PERSONA = CenPersonaBean.T_NOMBRETABLA + " P";
		String T_CEN_COLEGIADO = CenColegiadoBean.T_NOMBRETABLA + " CG";
		
		//Tabla env_componentesListaCorreo
		String CO_IDPERSONA = "CO." + EnvComponentesListaCorreoBean.C_IDPERSONA;
		String CO_IDINSTITUCION = "CO." + EnvComponentesListaCorreoBean.C_IDINSTITUCION;
		String CO_IDLISTACORREO = "CO." + EnvComponentesListaCorreoBean.C_IDLISTACORREO;
		
		//Tabla cen_persona
		String P_NOMBRE = "P." + CenPersonaBean.C_NOMBRE;
		String P_APELLIDOS1 = "P." + CenPersonaBean.C_APELLIDOS1;
		String P_APELLIDOS2 = "P." + CenPersonaBean.C_APELLIDOS2;
		String P_NIFCIF = "P." + CenPersonaBean.C_NIFCIF;
		String P_IDPERSONA = "P." + CenPersonaBean.C_IDPERSONA;
		
		//Tabla cen_colegiado
		String CG_NCOLEGIADO = "CG." + CenColegiadoBean.C_NCOLEGIADO;
		String CG_IDINSTITUCION = "CG." + CenColegiadoBean.C_IDINSTITUCION;
		String CG_IDPERSONA = "CG." + CenColegiadoBean.C_IDPERSONA;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
			String sql = "SELECT (";	        
		    
		    sql += P_NOMBRE + "||' '|| " + P_APELLIDOS1 + "||' '|| " + P_APELLIDOS2 + ") AS NOMBREYAPELLIDOS, ";	
		    sql += P_NIFCIF + ", ";	
		    sql += CG_NCOLEGIADO + ", ";		    
		    sql += CO_IDINSTITUCION + ", " + CO_IDLISTACORREO + ", " + CO_IDPERSONA;
		        
			sql += " FROM ";
		    sql += T_ENV_COMPONENTESLISTACORREO + ", " + 
		    	   T_CEN_PERSONA + ", " + 
		    	   T_CEN_COLEGIADO;
			
		    sql += " WHERE ";
		    sql += CO_IDINSTITUCION + " = " + idInstitucion.toString();
		    sql += " AND " + CO_IDLISTACORREO + " = " + idLista.toString();
			sql += " AND " + CO_IDPERSONA + " = " + P_IDPERSONA;
			sql += " AND " + CO_IDINSTITUCION + " = " + CG_IDINSTITUCION + "(+)";
			sql += " AND " + CO_IDPERSONA + " = " + CG_IDPERSONA + "(+)";

			ClsLogging.writeFileLog("EnvComponentesListaCorreoAdm -> QUERY: "+sql,3);
	        
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
    
    
	/** 
	 * Comprueba si el componente ya ha sido insertado en la lista
	 * @param  idListaCorreo id de la lista
	 * @param  idInstitucion id de la institucion
	 * @param  idPersona id de la persona
	 * @return  boolean con el resultado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean existeComponente(String idLista, 
									String idInstitucion, 
									String idPersona) 
		throws ClsExceptions, SIGAException{
		try {
			Vector v = this.select(" WHERE " + EnvComponentesListaCorreoBean.C_IDINSTITUCION + " = " + idInstitucion +
			        				 " AND " + EnvComponentesListaCorreoBean.C_IDLISTACORREO + " = " + idLista +
			        				 " AND " + EnvComponentesListaCorreoBean.C_IDPERSONA + " = " + idPersona);
		    
			if ((v != null) && (v.size()>0)) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}
    
}
