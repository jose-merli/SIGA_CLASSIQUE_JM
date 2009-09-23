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
public class ScsDocumentoEJGAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public ScsDocumentoEJGAdm(UsrBean usu){
		super(ScsDocumentoEJGBean.T_NOMBRETABLA, usu);
		
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {ScsDocumentoEJGBean.C_ABREVIATURA,ScsDocumentoEJGBean.C_DESCRIPCION,ScsDocumentoEJGBean.C_IDINSTITUCION,ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,ScsDocumentoEJGBean.C_IDDOCUMENTOEJG};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {ScsDocumentoEJGBean.C_IDINSTITUCION,ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG,ScsDocumentoEJGBean.C_IDDOCUMENTOEJG};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
//		return this.getClavesBean();
	    String [] aux = {"ROWID"};
		return aux;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ScsDocumentoEJGBean bean = null;
		
		try {
			bean = new ScsDocumentoEJGBean();
			bean.setAbreviatura((String)hash.get(ScsDocumentoEJGBean.C_ABREVIATURA));			
			bean.setDescripcion((String)hash.get(ScsDocumentoEJGBean.C_DESCRIPCION));		
			bean.setIdInstitucion((String)hash.get(ScsDocumentoEJGBean.C_IDINSTITUCION));		
			bean.setIdTipoDocumentoEJG((String)hash.get(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG));
			bean.setIdDocumentoEJG((String)hash.get(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG));
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
			ScsDocumentoEJGBean b = (ScsDocumentoEJGBean) bean;
			htData.put(ScsDocumentoEJGBean.C_ABREVIATURA, b.getAbreviatura());		
			htData.put(ScsDocumentoEJGBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG, b.getIdTipoDocumentoEJG());
			htData.put(ScsDocumentoEJGBean.C_IDINSTITUCION, b.getIdInstitucion());
			htData.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG, b.getIdDocumentoEJG());
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
  			String sql ="SELECT (MAX("+ScsDocumentoEJGBean.C_IDDOCUMENTOEJG+") + 1) AS IDDOCUMENTOEJG" +
  					" FROM "+ScsDocumentoEJGBean.T_NOMBRETABLA+" WHERE " +
  					ScsDocumentoEJGBean.C_IDINSTITUCION+" = "+ entrada.get(ScsDocumentoEJGBean.C_IDINSTITUCION)+
					" and "+ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG+" = "+entrada.get(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG);
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDDOCUMENTOEJG").equals("")) {
					entrada.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,"1");
				}
				else entrada.put(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG,(String)prueba.get("IDDOCUMENTOEJG"));				
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
	public ScsDocumentoEJGBean getDocumento(Hashtable htPkTabl,String lenguaje) throws ClsExceptions {
		ScsDocumentoEJGBean beanDocumento = null;
		
		StringBuffer sql = new StringBuffer();
		Hashtable htCodigos = new Hashtable();
		int contador = 0;
		String idInstitucion = (String)htPkTabl.get(ScsDocumentoEJGBean.C_IDINSTITUCION);
		String idTipoDoc = (String)htPkTabl.get(ScsDocumentoEJGBean.C_IDTIPODOCUMENTOEJG);
		String idDoc = (String)htPkTabl.get(ScsDocumentoEJGBean.C_IDDOCUMENTOEJG);
		sql.append(" SELECT ABREVIATURA, ");
		sql.append(" F_SIGA_GETRECURSO(");
		sql.append(ScsDocumentoEJGBean.C_DESCRIPCION);
		sql.append(",");
		sql.append(lenguaje);
		sql.append(") ");
		sql.append(ScsDocumentoEJGBean.C_DESCRIPCION);
		sql.append(" , IDINSTITUCION, IDTIPODOCUMENTOEJG "); 
		sql.append(" FROM SCS_DOCUMENTOEJG  WHERE ");
		sql.append(" IDDOCUMENTOEJG = :");
		contador++;
		htCodigos.put(new Integer(contador),idDoc );
		sql.append(contador);
		
		sql.append(" AND IDTIPODOCUMENTOEJG = :");
		contador++;
		htCodigos.put(new Integer(contador), idTipoDoc);
		sql.append(contador);
		sql.append(" AND IDINSTITUCION = :");
		contador++;
		htCodigos.put(new Integer(contador),idInstitucion );
		sql.append(contador);
		//sql.append(" ORDER BY IDINSTITUCION, IDTIPODOCUMENTOEJG ");   
		try {
						
			
			Vector datos = this.selectGenericoBind(sql.toString(),htCodigos);
			//como es por PK
			Hashtable row = (Hashtable)datos.get(0);
			String descripcion = (String)row.get(ScsTipoDocumentoEJGBean.C_DESCRIPCION);
			String abreviatura = (String)row.get(ScsTipoDocumentoEJGBean.C_ABREVIATURA);
			beanDocumento = new ScsDocumentoEJGBean();
			beanDocumento.setIdDocumentoEJG(idDoc);
			beanDocumento.setIdTipoDocumentoEJG(idTipoDoc);
			beanDocumento.setIdInstitucion(idInstitucion);
			beanDocumento.setDescripcion(descripcion);
			beanDocumento.setAbreviatura(abreviatura);
			
			
			

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return beanDocumento;
	}
	
}
