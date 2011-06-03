
/*
 * Created on 14-sep-2007
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenTiposCVAdm extends MasterBeanAdministrador 
{
	/**
	 * @param tabla
	 */

	public CenTiposCVAdm(UsrBean usu) {
		super(CenTiposCVBean.T_NOMBRETABLA, usu);
	}


	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenTiposCVBean.C_CODIGOEXT,          CenTiposCVBean.C_DESCRIPCION, 
				            CenTiposCVBean.C_FECHAMODIFICACION,  CenTiposCVBean.C_IDTIPOCV, 
							CenTiposCVBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenTiposCVBean.C_IDTIPOCV};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  MasterBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenTiposCVBean bean = null;
		
		try {
			bean = new CenTiposCVBean();
			bean.setCodigoExt(UtilidadesHash.getString(hash,CenTiposCVBean.C_CODIGOEXT));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenTiposCVBean.C_DESCRIPCION));
			bean.setIdTipoCV(UtilidadesHash.getInteger(hash,CenTiposCVBean.C_IDTIPOCV));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenTiposCVBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenTiposCVBean.C_USUMODIFICACION));			
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenTiposCVBean b = (CenTiposCVBean) bean;
			UtilidadesHash.set(htData,CenTiposCVBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData,CenTiposCVBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,CenTiposCVBean.C_IDTIPOCV ,b.getIdTipoCV());
			UtilidadesHash.set(htData,CenTiposCVBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,CenTiposCVBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenTiposCVBean.C_CODIGOEXT + ", " +
									  UtilidadesMultidioma.getCampoMultidioma(CenTiposCVBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
									  CenTiposCVBean.C_FECHAMODIFICACION + ", " +
									  CenTiposCVBean.C_IDTIPOCV + ", " +
									  CenTiposCVBean.C_USUMODIFICACION +
							" FROM " + CenTiposCVBean.T_NOMBRETABLA;
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenTiposCVBean.C_CODIGOEXT + ", " +
			  UtilidadesMultidioma.getCampoMultidioma(CenTiposCVBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
			  CenTiposCVBean.C_FECHAMODIFICACION + ", " +
			  CenTiposCVBean.C_IDTIPOCV + ", " +
			  CenTiposCVBean.C_USUMODIFICACION +
			  	" FROM " + CenTiposCVBean.T_NOMBRETABLA;
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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

	public Vector getRestoDatosCV(int idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String sql = " SELECT   tip.IDTIPOCV,  sub1.IDTIPOCVSUBTIPO1,  sub2.IDTIPOCVSUBTIPO2,  '' TIPOAPUNTE,  '' IDCV, '' IDINSTITUCION, " +
					 "			'' IDPERSONA,  '' FECHAINICIO,'' FECHAFIN,  '' DESCRIPCION, '' VERIFICADO,  '' CREDITOS, '' IDINSTITUCION_SUBT1, "+
					 "			'' IDINSTITUCION_SUBT2,  '' FECHAVERIFICACION,'' FECHABAJA, '' DESCSUBTIPO1, '' DESCSUBTIPO2 "+
					 " FROM 	cen_tiposcv tip, cen_tiposcvsubtipo1 sub1, cen_tiposcvsubtipo2 sub2 "+
					 " WHERE    tip.idtipocv = sub1.idtipocv(+) "+
					 " 		  	and tip.idtipocv = sub2.idtipocv(+) "+
					 //"	 	  	and sub1.idInstitucion in (2000, "+idInstitucion+") "+
					 " ORDER BY tip.idtipocv ";
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				if (rc != null) {
					for (int i = 0; i < rc.size(); i++) {
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable) fila.getRow();
						if (registro != null)
							datos.add(registro);
					}
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}
	
}
