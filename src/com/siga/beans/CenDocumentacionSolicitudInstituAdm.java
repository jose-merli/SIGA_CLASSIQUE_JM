/* VERSIONES:
 * daniel.campos 18-10-2004 creacion
 * raul.ggonzalez 14-02-2005 anhado funciones de busqueda de documentos e insercion de documentos
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.*;
import com.siga.general.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
 * @author atosOrigin 18-10-2004
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenDocumentacionSolicitudInstituAdm extends MasterBeanAdministrador {
	
	public CenDocumentacionSolicitudInstituAdm (UsrBean usu) {
		super(CenDocumentacionSolicitudInstituBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenDocumentacionSolicitudInstituBean.C_FECHAMODIFICACION,
							CenDocumentacionSolicitudInstituBean.C_IDDOCUMSOLIINSTITU,
							CenDocumentacionSolicitudInstituBean.C_IDINSTITUCION,
							CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION,
							CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD,
							CenDocumentacionSolicitudInstituBean.C_USUMODIFICACION,
							CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenDocumentacionSolicitudInstituBean.C_IDDOCUMSOLIINSTITU,
							CenDocumentacionSolicitudInstituBean.C_IDINSTITUCION,
							CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION,
							CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD,
							CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions{

		CenDocumentacionSolicitudInstituBean bean = null;
		
		try {
			bean = new CenDocumentacionSolicitudInstituBean();
			bean.setFechaMod(UtilidadesHash.getString(hash, CenDocumentacionSolicitudInstituBean.C_FECHAMODIFICACION));
			bean.setIdDocumentacionSolicitudInstitucion(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudInstituBean.C_IDDOCUMSOLIINSTITU));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudInstituBean.C_IDINSTITUCION));
			bean.setIdTipoColegiacion(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION));
			bean.setIdTipoSolicitud(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudInstituBean.C_USUMODIFICACION));
			bean.setIdModalidad(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD));
			
//			bean.getDocumentacionSolicitud().setDescripcion((UtilidadesHash.getString(hash, CenDocumentacionSolicitudBean.C_DESCRIPCION)));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions{

		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenDocumentacionSolicitudInstituBean b = (CenDocumentacionSolicitudInstituBean) bean;
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_IDDOCUMSOLIINSTITU, 	b.getIdDocumentacionSolicitudInstitucion());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_IDINSTITUCION,	 	b.getIdInstitucion());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION, 	b.getIdTipoColegiacion());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD, 		b.getIdTipoSolicitud());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_USUMODIFICACION, 		b.getUsuMod());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD, 			b.getIdModalidad());
//			UtilidadesHash.set(htData, CenDocumentacionSolicitudBean.C_DESCRIPCION, 				b.getDocumentacionSolicitud().getDescripcion());
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
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = null;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.query(sql)) {
				datos = new Vector();
				CenDocumentacionSolicitudAdm adm =  new CenDocumentacionSolicitudAdm(this.usrbean);
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					CenDocumentacionSolicitudInstituBean registro = (CenDocumentacionSolicitudInstituBean) this.hashTableToBean(fila.getRow());
			
					Hashtable hash = new Hashtable();
					UtilidadesHash.set (hash, CenDocumentacionSolicitudBean.C_IDDOCUMENTACION, registro.getIdDocumentacionSolicitudInstitucion());
					Vector v = adm.selectByPK(hash);
					if (v.size() == 1) {
						registro.setDocumentacionSolicitud((CenDocumentacionSolicitudBean)v.get(0));
					}
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (Exception e) { 
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				datos = new Vector();
				CenDocumentacionSolicitudAdm adm =  new CenDocumentacionSolicitudAdm(this.usrbean);
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					CenDocumentacionSolicitudInstituBean registro = (CenDocumentacionSolicitudInstituBean) this.hashTableToBean(fila.getRow());
					
							Hashtable hash = new Hashtable();
							UtilidadesHash.set (hash, CenDocumentacionSolicitudBean.C_IDDOCUMENTACION, registro.getIdDocumentacionSolicitudInstitucion());
							Vector v = adm.selectByPK(hash);
							if (v.size() == 1) {
								registro.setDocumentacionSolicitud((CenDocumentacionSolicitudBean)v.get(0));
							}
							if (registro != null) 
								datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}
	/** 
	 * Obtiene los documentos a presentar para los criterios establecidos	
	 * @param datos  Hashtable con los criterios de busqueda  
	 * @param idInstitucion   
	 * @return  Vector de CenDocumentacionSolicitudBean  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getDocumentacion(Hashtable datos, String idInstitucion)throws ClsExceptions,SIGAException {
		String sql;
		RowsContainer rc = null;
		Vector salida = new Vector();
		try { 
			rc = new RowsContainer(); 
			
			sql = " select " + UtilidadesMultidioma.getCampoMultidioma("a." + CenDocumentacionSolicitudBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", nvl(a."+CenDocumentacionSolicitudBean.C_IDDOCUMENTACION+",0) as  "+CenDocumentacionSolicitudBean.C_IDDOCUMENTACION+" " +
					" from "+CenDocumentacionSolicitudBean.T_NOMBRETABLA+" a , "+CenDocumentacionSolicitudInstituBean.T_NOMBRETABLA+" b " +
				   " where a."+CenDocumentacionSolicitudBean.C_IDDOCUMENTACION+"  = b."+CenDocumentacionSolicitudInstituBean.C_IDDOCUMSOLIINSTITU+" (+) " + // esto es un outer join
					 " and b."+CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD+"= " +  (String) datos.get(CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD) + 
					 " and b."+CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION+" = " +  (String) datos.get(CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION) +
					 " and b."+CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD+" = " +  (String) datos.get(CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD) +
					 " and b."+CenDocumentacionSolicitudInstituBean.C_IDINSTITUCION+" = " +  idInstitucion.toString() +
				   " ORDER BY a."+CenDocumentacionSolicitudBean.C_DESCRIPCION;
		
			if (rc.find(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					CenDocumentacionSolicitudBean docBean = new CenDocumentacionSolicitudBean();
					String aux = (String)fila.getRow().get(CenDocumentacionSolicitudBean.C_DESCRIPCION);
					if (aux!=null) {
						docBean.setDescripcion(aux);
					}
					aux = (String)fila.getRow().get(CenDocumentacionSolicitudBean.C_IDDOCUMENTACION);
					if (aux!=null) {
						docBean.setIdDocumentacion(new Integer(aux));
					}
					salida.add(docBean);
				}
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al consultar datos.");		
		}		
		return salida;
	}

	
	/** 
	 * Obtiene los documentos a presentar para los criterios establecidos	
	 * @param datos  Hashtable con los datos a insertar  
	 * @param documentos Array de String con los ids de los documentos a insertar  
	 * @param idIntitucion con la institucion para insertar  
	 * @return  boolean resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Caso de error de aplicacion
	 */	
	public boolean insertDocumentacion(Hashtable datos,String[] documentos, String idInstitucion)throws SIGAException, ClsExceptions {

		try { 
			// borro los anteriores
			String idTipoColegiacion = (String) datos.get(CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION);
			String idTipoSolicitud   = (String) datos.get(CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD);
			String idModalidad       = (String) datos.get(CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD);
			
			String where = " WHERE " + CenDocumentacionSolicitudInstituBean.C_IDTIPOCOLEGIACION + " = " + idTipoColegiacion + 
			                 " AND " + CenDocumentacionSolicitudInstituBean.C_IDTIPOSOLICITUD   + " = " + idTipoSolicitud +
			                 " AND " + CenDocumentacionSolicitudInstituBean.C_IDMODALIDAD       + " = " + idModalidad;
			
			Vector aBorrar = this.select(where);
			if (aBorrar!=null && aBorrar.size()>0) {
				for (int j=0;j<aBorrar.size();j++) {
					CenDocumentacionSolicitudInstituBean b = (CenDocumentacionSolicitudInstituBean) aBorrar.get(j);
					if (!this.delete(b)) {
						throw new SIGAException(this.getError());
					}
				}
			}
	
			//inserto los nuevos
			if (documentos!=null && documentos.length>0) {
				for (int i=0;i<documentos.length;i++) {
					CenDocumentacionSolicitudInstituBean docBean = new CenDocumentacionSolicitudInstituBean();
		
					docBean.setIdDocumentacionSolicitudInstitucion(new Integer(documentos[i]));
					
					docBean.setIdInstitucion(new Integer(idInstitucion));
					docBean.setIdTipoColegiacion(new Integer(idTipoColegiacion));
					docBean.setIdTipoSolicitud(new Integer(idTipoSolicitud));
					docBean.setIdModalidad(new Integer(idModalidad));
					docBean.setFechaMod("SYSDATE");
					docBean.setUsuMod(this.usuModificacion);
					
					if (!this.insert(docBean)) {
						throw new SIGAException(this.getError());
					}
				}
			}
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions ("Error al insertar la documentacion");
		}
		return true;
	}
}
