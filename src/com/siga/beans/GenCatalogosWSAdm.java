
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.comun.vos.ValueKeyVO;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla GEN_RECURSOS_CATALOGOS
 */
public class GenCatalogosWSAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public GenCatalogosWSAdm (UsrBean usuario) {
		super( GenCatalogosWSBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposTabla ()
	 * @return String campos que recupera desde la select
	 */
	protected String[] getCamposBean(){
		String[] campos = { GenCatalogosWSBean.C_CATALOGO,	
							GenCatalogosWSBean.C_CONJUNTO,	
							GenCatalogosWSBean.C_VALOR,
							GenCatalogosWSBean.C_IDINTERNO,	
							GenCatalogosWSBean.C_IDEXTERNO,	
							GenCatalogosWSBean.C_REFINTERNA,
							GenCatalogosWSBean.C_RECURSO};
		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean() {
		String[] campos = {	GenCatalogosWSBean.C_CATALOGO,	
							GenCatalogosWSBean.C_CONJUNTO,	
							GenCatalogosWSBean.C_VALOR };
		return campos;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos(){
		return this.getClavesBean();
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
		GenCatalogosWSBean bean = null;
		try {
			bean = new GenCatalogosWSBean();
			bean.setCatalogo(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_CATALOGO));
			bean.setConjunto(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_CONJUNTO));
			bean.setIdInterno(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_IDINTERNO));
			bean.setIdExterno(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_IDEXTERNO));
			bean.setRecurso(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_RECURSO));
			bean.setRefInterna(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_REFINTERNA));
			bean.setValor(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_VALOR));
			bean.setFechaMod(UtilidadesHash.getString(hash, GenCatalogosWSBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, GenCatalogosWSBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			GenCatalogosWSBean b = (GenCatalogosWSBean) bean;
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_CATALOGO,	b.getCatalogo());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_CONJUNTO,	b.getConjunto());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_IDINTERNO,	b.getIdInterno());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_IDEXTERNO,	b.getIdExterno());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_RECURSO,	b.getRecurso());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_REFINTERNA,	b.getRefInterna());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_VALOR,	b.getValor());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_FECHAMODIFICACION,	b.getFechaMod());
			UtilidadesHash.set(hash, GenCatalogosWSBean.C_USUMODIFICACION,	b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	public ArrayList<ValueKeyVO> getValores(String catalogo, String conjunto) throws ClsExceptions{
		
		StringBuffer sql = new StringBuffer();
		ArrayList<ValueKeyVO> valKey = new ArrayList();
		
		sql.append( "SELECT " + GenCatalogosWSBean.C_IDEXTERNO + " as KEY,");
		sql.append(" F_SIGA_GETRECURSO_ETIQUETA(" + GenCatalogosWSBean.C_RECURSO + "," + this.usrbean.getLanguage() + ") AS VALUE");   
		sql.append(" FROM " + GenCatalogosWSBean.T_NOMBRETABLA );
		sql.append(" WHERE " + GenCatalogosWSBean.C_CATALOGO + " = '" + catalogo + "'");
		sql.append(" AND " + GenCatalogosWSBean.C_CONJUNTO + " = '" + conjunto + "'");
		sql.append(" ORDER BY " + GenCatalogosWSBean.C_IDEXTERNO + " ASC");
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					valKey.add(new ValueKeyVO(UtilidadesHash.getString(htFila,"KEY"),UtilidadesHash.getString(htFila,"VALUE")));
				}
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return valKey;
	}

	public ArrayList getPaises() throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		ArrayList<ValueKeyVO> valKey = new ArrayList();
		
		sql.append( "SELECT " + CenPaisBean.C_CODIGOEXT + " as KEY,");
		sql.append(" F_SIGA_GETRECURSO(" + CenPaisBean.C_NOMBRE + "," + this.usrbean.getLanguage() + ") AS VALUE");   
		sql.append(" FROM " + CenPaisBean.T_NOMBRETABLA );
		sql.append(" ORDER BY VALUE ASC");
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				valKey.add(new ValueKeyVO("00",""));
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					valKey.add(new ValueKeyVO(UtilidadesHash.getString(htFila,"KEY"),UtilidadesHash.getString(htFila,"VALUE")));
				}
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return valKey;
	}
	
	public ArrayList getProvincias() throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		ArrayList<ValueKeyVO> valKey = new ArrayList();
		
		sql.append( "SELECT " + CenProvinciaBean.C_CODIGOEXT + " as KEY,");
		sql.append(" F_SIGA_GETRECURSO_ETIQUETA(" + CenProvinciaBean.C_NOMBRE + "," + this.usrbean.getLanguage() + ") AS VALUE");   
		sql.append(" FROM " + CenProvinciaBean.T_NOMBRETABLA );
		sql.append(" ORDER BY VALUE ASC");
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				valKey.add(new ValueKeyVO("00",""));
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					valKey.add(new ValueKeyVO(UtilidadesHash.getString(htFila,"KEY"),UtilidadesHash.getString(htFila,"VALUE")));
				}
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return valKey;
	}
	
	public String getValor(String catalogo, String conjunto, String idInterno) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		String value = "";
		
		sql.append( "SELECT F_SIGA_GETRECURSO_ETIQUETA(" + GenCatalogosWSBean.C_RECURSO + "," + this.usrbean.getLanguage() + ") AS VALUE");   
		sql.append(" FROM " + GenCatalogosWSBean.T_NOMBRETABLA );
		sql.append(" WHERE " + GenCatalogosWSBean.C_CATALOGO + " = '" + catalogo + "'");
		sql.append(" AND " + GenCatalogosWSBean.C_CONJUNTO + " = '" + conjunto + "'");
		sql.append(" AND " + GenCatalogosWSBean.C_IDINTERNO + " = '" + idInterno + "'");
		sql.append(" ORDER BY " + GenCatalogosWSBean.C_IDEXTERNO + " ASC");
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null && rc.size()==1) {
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila = fila.getRow();
				value = UtilidadesHash.getString(htFila,"VALUE");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return value;
	}
	
	public String getValorIdExt(String catalogo, String conjunto, String idExterno) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		String value = "";
		
		sql.append( "SELECT F_SIGA_GETRECURSO_ETIQUETA(" + GenCatalogosWSBean.C_RECURSO + "," + this.usrbean.getLanguage() + ") AS VALUE");   
		sql.append(" FROM " + GenCatalogosWSBean.T_NOMBRETABLA );
		sql.append(" WHERE " + GenCatalogosWSBean.C_CATALOGO + " = '" + catalogo + "'");
		sql.append(" AND " + GenCatalogosWSBean.C_CONJUNTO + " = '" + conjunto + "'");
		sql.append(" AND " + GenCatalogosWSBean.C_IDEXTERNO + " = '" + idExterno + "'");
		sql.append(" ORDER BY " + GenCatalogosWSBean.C_IDEXTERNO + " ASC");
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null && rc.size()==1) {
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila = fila.getRow();
				value = UtilidadesHash.getString(htFila,"VALUE");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return value;
	}
	
	public String getIdExternoPais(String idInterno) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		String value = "";
		
		sql.append( "SELECT " + CenPaisBean.C_CODIGOEXT + " as ID");
		sql.append(" FROM " + CenPaisBean.T_NOMBRETABLA );
		sql.append(" WHERE " + CenPaisBean.C_IDPAIS + "="+idInterno);
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila = fila.getRow();
				value = UtilidadesHash.getString(htFila,"ID");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return value;
	}
	
	public String getIdExternoProvincia(String idInterno) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		String value = "";
		
		sql.append( "SELECT " + CenProvinciaBean.C_CODIGOEXT + " as ID");
		sql.append(" FROM " + CenProvinciaBean.T_NOMBRETABLA );
		sql.append(" WHERE " + CenProvinciaBean.C_IDPROVINCIA + "="+idInterno);
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila = fila.getRow();
				value = UtilidadesHash.getString(htFila,"ID");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return value;
	}
	
	public String getNombrePoblacion(String idInterno) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		String value = "";
		
		sql.append( "SELECT " + CenPoblacionesBean.C_NOMBRE + " as VALUE");
		sql.append(" FROM " + CenPoblacionesBean.T_NOMBRETABLA );
		sql.append(" WHERE " + CenPoblacionesBean.C_IDPOBLACION+ "="+idInterno);
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila = fila.getRow();
				value = UtilidadesHash.getString(htFila,"VALUE");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return value;
	}
	
	public String getCodigoExtInstitucion(String idInstitucion) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		String value = "";
		
		sql.append( "SELECT CODIGOEXT as VALUE");
		sql.append(" FROM " + CenInstitucionBean.T_NOMBRETABLA );
		sql.append(" WHERE " + CenInstitucionBean.C_IDINSTITUCION+ "="+idInstitucion);
		
		RowsContainer rc = null;
		try {
			rc = this.find(sql.toString());
			if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable<String, Object> htFila = fila.getRow();
				value = UtilidadesHash.getString(htFila,"VALUE");
			}
		} catch (ClsExceptions e) {
			throw new ClsExceptions (e, "Error al recuperar datos");	
		}
		
		return value;
	}
}