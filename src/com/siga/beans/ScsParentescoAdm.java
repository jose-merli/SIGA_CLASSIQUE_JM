/*
 * Created on Dec 1, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;


import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author jorgeta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsParentescoAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public ScsParentescoAdm(UsrBean usu){
		super(ScsParentescoBean.T_NOMBRETABLA, usu);
		
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {ScsParentescoBean.C_IDINSTITUCION,ScsParentescoBean.C_IDPARENTESCO,ScsParentescoBean.C_BLOQUEADO,ScsParentescoBean.C_CODIGOEXT,ScsParentescoBean.C_DESCRIPCION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {ScsParentescoBean.C_IDINSTITUCION,ScsParentescoBean.C_IDPARENTESCO};
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
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ScsParentescoBean bean = null;
		
		try {
			bean = new ScsParentescoBean();
			bean.setIdParentesco(UtilidadesHash.getInteger(hash,ScsParentescoBean.C_IDPARENTESCO));			
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsParentescoBean.C_DESCRIPCION));		
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsParentescoBean.C_IDINSTITUCION));		
			bean.setBloqueado(UtilidadesHash.getString(hash,ScsParentescoBean.C_BLOQUEADO));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsParentescoBean.C_CODIGOEXT));
		}
		catch (Exception e) { 
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
		try {
			htData = new Hashtable();
			ScsParentescoBean b = (ScsParentescoBean) bean;
			UtilidadesHash.set(htData, ScsParentescoBean.C_IDPARENTESCO, b.getIdParentesco());
			UtilidadesHash.set(htData, ScsParentescoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ScsParentescoBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ScsParentescoBean.C_BLOQUEADO, b.getBloqueado());
			UtilidadesHash.set(htData, ScsParentescoBean.C_CODIGOEXT, b.getCodigoExt());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	
	
	public ScsParentescoBean getParentesco(Hashtable htPkTabl,String lenguaje) throws ClsExceptions {
		ScsParentescoBean beanParentesco = null;
		
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador = 0;
		String idInstitucion = (String)htPkTabl.get(ScsParentescoBean.C_IDINSTITUCION);
		Integer idParentesco = (Integer)htPkTabl.get(ScsParentescoBean.C_IDPARENTESCO);
		sql.append(" SELECT  ");
		sql.append(" F_SIGA_GETRECURSO(");
		sql.append(ScsParentescoBean.C_DESCRIPCION);
		sql.append(",");
		sql.append(lenguaje);
		sql.append(") ");
		sql.append(ScsParentescoBean.C_DESCRIPCION);
		sql.append(" , IDINSTITUCION,IDPARENTESCO  "); 
		sql.append(" FROM SCS_PARENTESCO  WHERE IDPARENTESCO = :");
		contador++;
		htCodigos.put(new Integer(contador), idParentesco);
		sql.append(contador);
		sql.append(" AND IDINSTITUCION = :");
		contador++;
		htCodigos.put(new Integer(contador),idInstitucion );
		sql.append(contador);
		try {
						
			
			Vector datos = this.selectGenericoBind(sql.toString(),htCodigos);
			//como es por PK
			Hashtable row = (Hashtable)datos.get(0);
			String descripcion = (String)row.get(ScsParentescoBean.C_DESCRIPCION);
			beanParentesco = new ScsParentescoBean();
			beanParentesco.setIdParentesco(idParentesco);
			beanParentesco.setIdInstitucion(new Integer(idInstitucion));
			beanParentesco.setDescripcion(descripcion);
			

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return beanParentesco;
	}
	
}
