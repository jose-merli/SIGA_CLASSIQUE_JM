
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_GRUPOFACTURACION
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 * 
 * Modificado `por david.sanchezp para incluir el metodo "consultarCriteriosFacturacion()" el 31/03/2005.
 */

public class FcsFactGrupoFactHitoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public FcsFactGrupoFactHitoAdm (UsrBean usuario) {
		super( FcsFactGrupoFactHitoBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	FcsFactGrupoFactHitoBean.C_FECHAMODIFICACION,	FcsFactGrupoFactHitoBean.C_IDFACTURACION,
							FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION,	FcsFactGrupoFactHitoBean.C_IDHITOGENERAL,	FcsFactGrupoFactHitoBean.C_IDINSTITUCION,
							FcsFactGrupoFactHitoBean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	FcsFactGrupoFactHitoBean.C_IDINSTITUCION,		FcsFactGrupoFactHitoBean.C_IDFACTURACION,
							FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION,FcsFactGrupoFactHitoBean.C_IDHITOGENERAL};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsFactGrupoFactHitoBean bean = null;
		try{
			bean = new FcsFactGrupoFactHitoBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsFactGrupoFactHitoBean.C_FECHAMODIFICACION));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,FcsFactGrupoFactHitoBean.C_IDFACTURACION));
			bean.setIdGrupoFacturacion(UtilidadesHash.getInteger(hash,FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsFactGrupoFactHitoBean.C_IDINSTITUCION));
			bean.setIdHitoGeneral(UtilidadesHash.getInteger(hash,FcsFactGrupoFactHitoBean.C_IDHITOGENERAL));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsFactGrupoFactHitoBean.C_USUMODIFICACION));
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
	 * 
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			FcsFactGrupoFactHitoBean b = (FcsFactGrupoFactHitoBean) bean;
			hash.put(FcsFactGrupoFactHitoBean.C_FECHAMODIFICACION,	String.valueOf(b.getFechaMod()));
			hash.put(FcsFactGrupoFactHitoBean.C_IDFACTURACION, 		String.valueOf(b.getIdFacturacion()));
			hash.put(FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION, String.valueOf(b.getIdGrupoFacturacion()));
			hash.put(FcsFactGrupoFactHitoBean.C_IDINSTITUCION, 		String.valueOf(b.getIdInstitucion()));
			hash.put(FcsFactGrupoFactHitoBean.C_IDHITOGENERAL, 		String.valueOf(b.getIdHitoGeneral()));
			hash.put(FcsFactGrupoFactHitoBean.C_USUMODIFICACION,	String.valueOf(b.getUsuMod()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

		
	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}
	
	public Vector consultarCriteriosFacturacion(String idInstitucion, String idFacturacion, String idPagosJG, UsrBean usr) throws ClsExceptions {
		Vector vResultados = new Vector();
		String consulta = null;

		consulta = "SELECT "+
				   " fact."+FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION+","+
				   " fact."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL+","+
				   " (select " + UtilidadesMultidioma.getCampoMultidiomaSimple("descripcion", this.usrbean.getLanguage()) + " descripcion" +
				   "  from "+FcsHitoGeneralBean.T_NOMBRETABLA+
			       "  where "+FcsHitoGeneralBean.C_IDHITOGENERAL+" = fact."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL+") AS HITOGENERAL, "+
			       " (select " + UtilidadesMultidioma.getCampoMultidiomaSimple("NOMBRE",this.usrbean.getLanguage())+ " NOMBRE" +
			       "  from "+ScsGrupoFacturacionBean.T_NOMBRETABLA+
			       "  where "+ScsGrupoFacturacionBean.C_IDINSTITUCION+" = fact."+FcsFactGrupoFactHitoBean.C_IDINSTITUCION+ 
			       "  AND "+ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION+" = fact."+FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION+") AS GRUPOFACTURACION, "+    
			       " (select count (idpagosjg) "+
			       "  from "+FcsPagoGrupoFactHitoBean.T_NOMBRETABLA+
			       "  where "+FcsPagoGrupoFactHitoBean.C_IDINSTITUCION+" = fact."+FcsFactGrupoFactHitoBean.C_IDINSTITUCION+
			       " 	and "+FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION+" = fact."+FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION+ 
			       " 	and "+FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL+" = fact."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL+
			       " 	and "+FcsPagoGrupoFactHitoBean.C_IDPAGOSJG+" = "+idPagosJG+") as CHEQUEAR "+
				   " FROM "+
				   FcsFactGrupoFactHitoBean.T_NOMBRETABLA+" fact "+
 				   " WHERE fact."+FcsFactGrupoFactHitoBean.C_IDINSTITUCION+"="+idInstitucion+
				   "   AND fact."+FcsFactGrupoFactHitoBean.C_IDFACTURACION+"="+idFacturacion+
				   " ORDER BY fact."+FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION+", fact."+FcsFactGrupoFactHitoBean.C_IDHITOGENERAL;

		//Consulto en base de datos:
		RowsContainer rc = new RowsContainer();
		if (rc.query(consulta)) {
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				CriteriosPagosBean bean = new CriteriosPagosBean();
				bean.setIdGrupoFacturacion(fila.getString(FcsFactGrupoFactHitoBean.C_IDGRUPOFACTURACION));
				bean.setIdHitoGeneral(fila.getString(FcsFactGrupoFactHitoBean.C_IDHITOGENERAL));
				bean.setGrupoFacturacion(fila.getString("GRUPOFACTURACION"));
				bean.setHitoGeneral(UtilidadesString.getMensajeIdioma(usr, fila.getString("HITOGENERAL")));
				bean.setIdPagosJG(idPagosJG);
				if (fila.getString("CHEQUEAR")!=null && fila.getString("CHEQUEAR").equalsIgnoreCase("1"))
					bean.setCheckCriterio("SI");
				else
					bean.setCheckCriterio("NO");
				vResultados.add(bean);					
			}
		}
		
		return vResultados;
	}
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		return selectGenerico(consulta, false);
	}

	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query 
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @param boolean bRW: true si usamos el pool de Lectura/Escritura.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta, boolean bRW) throws ClsExceptions {
		Vector datos = new Vector();
		boolean salida = false;
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			
			//Si uso el pool de lectura/escritura
			if (bRW)
				salida = rc.findForUpdate(consulta);
			else
				salida = rc.query(consulta);
			if (salida) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsFactGrupoFactHitoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}

}