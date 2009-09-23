/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;

public class CenGruposCriteriosAdm extends MasterBeanAdministrador {
	
	public CenGruposCriteriosAdm (UsrBean usu) {
		super (CenGruposCriteriosBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {CenGruposCriteriosBean.C_IDINSTITUCION, 		
				    		CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS,
							CenGruposCriteriosBean.C_NOMBRE,
							CenGruposCriteriosBean.C_SENTENCIA,
							CenGruposCriteriosBean.C_IDCONSULTA,
							CenGruposCriteriosBean.C_FECHAMODIFICACION,
							CenGruposCriteriosBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenGruposCriteriosBean.C_IDINSTITUCION, CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenGruposCriteriosBean bean = null;
		
		try {
			bean = new CenGruposCriteriosBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, CenGruposCriteriosBean.C_IDINSTITUCION));
			bean.setIdGruposCriterios	(UtilidadesHash.getInteger(hash, CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS));
			bean.setNombre				(UtilidadesHash.getString(hash, CenGruposCriteriosBean.C_NOMBRE));
			bean.setSentencia			(UtilidadesHash.getString(hash, CenGruposCriteriosBean.C_SENTENCIA));
			bean.setIdConsulta			(UtilidadesHash.getInteger(hash, CenGruposCriteriosBean.C_IDCONSULTA));
			bean.setFechaMod			(UtilidadesHash.getString(hash, CenGruposCriteriosBean.C_FECHAMODIFICACION));			
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, CenGruposCriteriosBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenGruposCriteriosBean b = (CenGruposCriteriosBean) bean;
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS, b.getIdGruposCriterios());
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_SENTENCIA, b.getSentencia());
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenGruposCriteriosBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/** Funcion getNewIdGruposCriterios (String idInstitucion)
	 * Genera el id de un nuevo grupo de criterios
	 * @param String institucion
	 * @return nuevo idGruposCriterios
	 * */
	public Integer getNewIdGruposCriterios(String idInstitucion) throws ClsExceptions 
	{		
		int nuevoIdGrupo = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS+") AS ULTIMOGRUPO ";
	        
			sql += " FROM "+CenGruposCriteriosBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += CenGruposCriteriosBean.C_IDINSTITUCION+" = "+idInstitucion;			
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMOGRUPO")).equals("")) {
					Integer ultimoGrupoInt = Integer.valueOf((String)htRow.get("ULTIMOGRUPO"));
					int ultimoGrupo=ultimoGrupoInt.intValue();
					ultimoGrupo++;
					nuevoIdGrupo = ultimoGrupo;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdGrupo);
	}
}
