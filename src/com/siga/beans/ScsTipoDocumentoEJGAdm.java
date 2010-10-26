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

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsTipoDocumentoEJGAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public ScsTipoDocumentoEJGAdm(UsrBean usu){
		super(ScsTipoDocumentoEJGBean.T_NOMBRETABLA, usu);
		
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {ScsTipoDocumentoEJGBean.C_ABREVIATURA,
				            ScsTipoDocumentoEJGBean.C_DESCRIPCION,
				            ScsTipoDocumentoEJGBean.C_IDINSTITUCION,
				            ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,
				            ScsTipoDocumentoEJGBean.C_CODIGOEXT};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {ScsTipoDocumentoEJGBean.C_IDINSTITUCION,ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG};
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

		ScsTipoDocumentoEJGBean bean = null;
		
		try {
			bean = new ScsTipoDocumentoEJGBean();
			bean.setAbreviatura((String)hash.get(ScsTipoDocumentoEJGBean.C_ABREVIATURA));			
			bean.setDescripcion((String)hash.get(ScsTipoDocumentoEJGBean.C_DESCRIPCION));		
			bean.setIdInstitucion((String)hash.get(ScsTipoDocumentoEJGBean.C_IDINSTITUCION));		
			bean.setIdTipoDocumentoEJG((String)hash.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG));
			bean.setIdTipoDocumentoEJG((String)hash.get(ScsTipoDocumentoEJGBean.C_CODIGOEXT));
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
			ScsTipoDocumentoEJGBean b = (ScsTipoDocumentoEJGBean) bean;
			htData.put(ScsTipoDocumentoEJGBean.C_ABREVIATURA, b.getAbreviatura());		
			htData.put(ScsTipoDocumentoEJGBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, b.getIdTipoDocumentoEJG());
			htData.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION, b.getIdInstitucion());
			htData.put(ScsTipoDocumentoEJGBean.C_IDINSTITUCION, b.getCodigoExt());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;		
		int contador = 0;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
  			String sql ="SELECT (MAX("+ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG+") + 1) AS IDTIPODOCUMENTOEJG" +
  					" FROM "+ScsTipoDocumentoEJGBean.T_NOMBRETABLA+" WHERE " +
  					ScsTipoDocumentoEJGBean.C_IDINSTITUCION+" = "+ entrada.get(ScsTipoDocumentoEJGBean.C_IDINSTITUCION);
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDTIPODOCUMENTOEJG").equals("")) {
					entrada.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,"1");
				}
				else entrada.put(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,(String)prueba.get("IDTIPODOCUMENTOEJG"));				
			}
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDDOCUMENTOEJG");
		};
		
		return entrada;
	}	
	
	public ScsTipoDocumentoEJGBean getTipoDocumento(Hashtable htPkTabl,String lenguaje) throws ClsExceptions {
		ScsTipoDocumentoEJGBean beanTipoDocumento = null;
		
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador = 0;
		String idInstitucion = (String)htPkTabl.get(ScsTipoDocumentoEJGBean.C_IDINSTITUCION);
		String idTipoDoc = (String)htPkTabl.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG);
		sql.append(" SELECT ABREVIATURA, ");
		sql.append(" F_SIGA_GETRECURSO(");
		sql.append(ScsTipoDocumentoEJGBean.C_DESCRIPCION);
		sql.append(",");
		sql.append(lenguaje);
		sql.append(") ");
		sql.append(ScsTipoDocumentoEJGBean.C_DESCRIPCION);
		sql.append(" , IDINSTITUCION, IDTIPODOCUMENTOEJG, ");
		sql.append(ScsTipoDocumentoEJGBean.C_CODIGOEXT);		
		sql.append(" FROM SCS_TIPODOCUMENTOEJG  WHERE IDTIPODOCUMENTOEJG = :");
		contador++;
		htCodigos.put(new Integer(contador), idTipoDoc);
		sql.append(contador);
		sql.append(" AND IDINSTITUCION = :");
		contador++;
		htCodigos.put(new Integer(contador),idInstitucion );
		sql.append(contador);
		sql.append(" ORDER BY IDINSTITUCION, IDTIPODOCUMENTOEJG ");   
		try {
						
			
			Vector datos = this.selectGenericoBind(sql.toString(),htCodigos);
			//como es por PK
			Hashtable row = (Hashtable)datos.get(0);
			String descripcion = (String)row.get(ScsTipoDocumentoEJGBean.C_DESCRIPCION);
			String abreviatura = (String)row.get(ScsTipoDocumentoEJGBean.C_ABREVIATURA);
			String codigoext = (String)row.get(ScsTipoDocumentoEJGBean.C_CODIGOEXT);
			String idtipoDocumentoejg = (String)row.get(ScsTipoDocumentoEJGBean.C_IDTIPODOCUMENTOEJG);
			beanTipoDocumento = new ScsTipoDocumentoEJGBean();
			beanTipoDocumento.setIdTipoDocumentoEJG(idTipoDoc);
			beanTipoDocumento.setIdInstitucion(idInstitucion);
			beanTipoDocumento.setDescripcion(descripcion);
			beanTipoDocumento.setAbreviatura(abreviatura);
			beanTipoDocumento.setCodigoExt(codigoext);
			beanTipoDocumento.setIdTipoDocumentoEJG(idtipoDocumentoejg);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return beanTipoDocumento;
	}
	
	
}
