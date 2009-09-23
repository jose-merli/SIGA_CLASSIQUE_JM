/*
 * Created on Jan 24, 2005
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de Denunciantes de un expediente
 */
public class ExpDenuncianteAdm extends MasterBeanAdministrador {
	
	public ExpDenuncianteAdm(UsrBean usuario)
	{
	    super(ExpDenuncianteBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpDenuncianteBean.C_IDINSTITUCION,
				ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpDenuncianteBean.C_IDTIPOEXPEDIENTE,
				ExpDenuncianteBean.C_NUMEROEXPEDIENTE,
				ExpDenuncianteBean.C_ANIOEXPEDIENTE,
				ExpDenuncianteBean.C_IDDENUNCIANTE,
				ExpDenuncianteBean.C_IDPERSONA,
				ExpDenuncianteBean.C_IDDIRECCION,
				ExpDenuncianteBean.C_FECHAMODIFICACION,
				ExpDenuncianteBean.C_USUMODIFICACION};
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ExpDenuncianteBean.C_IDINSTITUCION,
				ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,
				ExpDenuncianteBean.C_IDTIPOEXPEDIENTE,
				ExpDenuncianteBean.C_NUMEROEXPEDIENTE,
				ExpDenuncianteBean.C_ANIOEXPEDIENTE,
				ExpDenuncianteBean.C_IDDENUNCIANTE};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
		ExpDenuncianteBean bean = null;
		try {
			bean = new ExpDenuncianteBean();			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_IDINSTITUCION));
			bean.setIdInstitucion_TipoExpediente(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			bean.setIdTipoExpediente(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_IDTIPOEXPEDIENTE));
			bean.setNumeroExpediente(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_NUMEROEXPEDIENTE));
			bean.setAnioExpediente(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_ANIOEXPEDIENTE));
			bean.setIdDenunciante(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_IDDENUNCIANTE));
			bean.setIdPersona(UtilidadesHash.getLong(hash, ExpDenuncianteBean.C_IDPERSONA));
			bean.setIdDireccion(UtilidadesHash.getLong(hash, ExpDenuncianteBean.C_IDDIRECCION));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpDenuncianteBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpDenuncianteBean.C_USUMODIFICACION));
		
		}
		catch (Exception e)	{
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
			ExpDenuncianteBean b = (ExpDenuncianteBean) bean;
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, b.getIdInstitucion_TipoExpediente());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDTIPOEXPEDIENTE, b.getIdTipoExpediente());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_NUMEROEXPEDIENTE, b.getNumeroExpediente());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_ANIOEXPEDIENTE, b.getAnioExpediente());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDDENUNCIANTE, b.getIdDenunciante());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_IDDIRECCION, b.getIdDireccion());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpDenuncianteBean.C_USUMODIFICACION, b.getUsuModificacion());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;
	}
	
	/** Funcion getNewIdDenunciante (Hashtable hash)
	 * Genera el id de un nuevo Denunciante
	 * @param bean con la clave primaria sin el idDenunciante
	 * @return nuevo idDenunciante
	 * */
	public Integer getNewIdDenunciante(ExpDenuncianteBean bean) throws ClsExceptions 
	{		
		int nuevoIdDenunciante = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT MAX("+ExpDenuncianteBean.C_IDDENUNCIANTE+") AS ULTIMODENUNCIANTE ";
	        
			sql += " FROM "+ExpDenuncianteBean.T_NOMBRETABLA;
		    		    		
			sql += " WHERE ";
			sql += ExpDenuncianteBean.C_IDINSTITUCION + " = " + bean.getIdInstitucion() + " AND ";
			sql += ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = " + bean.getIdInstitucion_TipoExpediente() + " AND ";
			sql += ExpDenuncianteBean.C_IDTIPOEXPEDIENTE + " = " + bean.getIdTipoExpediente() + " AND ";
			sql += ExpDenuncianteBean.C_NUMEROEXPEDIENTE + " = " + bean.getNumeroExpediente() + " AND ";
			sql += ExpDenuncianteBean.C_ANIOEXPEDIENTE + " = " + bean.getAnioExpediente();
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("ULTIMODENUNCIANTE")).equals("")) {
					Integer ultimoDenuncianteInt = Integer.valueOf((String)htRow.get("ULTIMODENUNCIANTE"));
					int ultimoDenunciante=ultimoDenuncianteInt.intValue();
					ultimoDenunciante++;
					nuevoIdDenunciante = ultimoDenunciante;
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return new Integer (nuevoIdDenunciante);
	}
}
