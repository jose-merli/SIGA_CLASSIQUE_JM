/*
 * Created on Mar 10, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de Consultas-Perfil
 */
public class ConConsultaPerfilAdm extends MasterBeanAdministrador {
	
	public ConConsultaPerfilAdm(UsrBean usuario)
	{
	    super(ConConsultaPerfilBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConConsultaPerfilBean.C_IDINSTITUCION,
				ConConsultaPerfilBean.C_IDPERFIL,
				ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA,
				ConConsultaPerfilBean.C_IDCONSULTA,
				ConConsultaPerfilBean.C_FECHAMODIFICACION,
				ConConsultaPerfilBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConConsultaPerfilBean.C_IDINSTITUCION,
				ConConsultaPerfilBean.C_IDPERFIL,
				ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA,
				ConConsultaPerfilBean.C_IDCONSULTA};

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
		
		ConConsultaPerfilBean bean = null;

		try
		{
			bean = new ConConsultaPerfilBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConConsultaPerfilBean.C_IDINSTITUCION));
			bean.setIdConsulta(UtilidadesHash.getInteger(hash, ConConsultaPerfilBean.C_IDCONSULTA));
			bean.setIdPerfil(UtilidadesHash.getString(hash, ConConsultaPerfilBean.C_IDPERFIL));
			bean.setIdInstitucion_Consulta(UtilidadesHash.getInteger(hash, ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ConConsultaPerfilBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ConConsultaPerfilBean.C_USUMODIFICACION));
		
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

			ConConsultaPerfilBean b = (ConConsultaPerfilBean) bean;

			UtilidadesHash.set(htData, ConConsultaPerfilBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConConsultaPerfilBean.C_IDPERFIL, b.getIdPerfil());
			UtilidadesHash.set(htData, ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA, b.getIdInstitucion_Consulta());
			UtilidadesHash.set(htData, ConConsultaPerfilBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConConsultaPerfilBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ConConsultaPerfilBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion selectPerfilesConsulta (UsrBean user, String idInstitucion_Consulta, String idConsulta)
	 * Búsqueda sobre la tabla de consultas-perfil, para permisos sobre consultas
	 * @param user
	 * @param idInstitucion_Consulta
	 * @param idConsulta
	 * @return vector con los registros encontrados. 
	 * @exception ClsExceptions 
	 * */
	/*public Vector selectPerfilesConsulta(UsrBean user, String idInstitucion_Consulta, String idConsulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			
	        String sql = "SELECT ";
	        
		    sql += "CP."+ConConsultaPerfilBean.C_IDPERFIL+", ";	
		    sql += "P."+AdmPerfilBean.C_DESCRIPCION;
		    
			sql += " FROM ";
		    sql += ConConsultaPerfilBean.T_NOMBRETABLA+" CP, "+AdmPerfilBean.T_NOMBRETABLA+" P";
		    		    		
			sql += " WHERE ";
			sql += "CP."+ConConsultaPerfilBean.C_IDINSTITUCION+"=P."+AdmPerfilBean.C_IDINSTITUCION+" AND ";
			sql += "CP."+ConConsultaPerfilBean.C_IDPERFIL+"=P."+AdmPerfilBean.C_IDPERFIL+" AND ";
			sql += "CP."+ConConsultaPerfilBean.C_IDINSTITUCION+"="+user.getLocation()+" AND ";
			sql += "CP."+ConConsultaPerfilBean.C_IDINSTITUCION_CONSULTA+" = "+idInstitucion_Consulta+" AND ";
			sql += "CP."+ConConsultaPerfilBean.C_IDCONSULTA+" = "+idConsulta;
			
			sql += " ORDER BY 2";		
			
			ClsLogging.writeFileLog("ConConsultaPerfilAdm, sql: "+sql,3);
			
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
	}*/

}
