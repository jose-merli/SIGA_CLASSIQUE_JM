package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre la base de datos de la tabla FCS_PAGO_GRUPOFACT_HITO
 * 
 * @author david.sanchezp
 * @since 31/03/2005 
 */
public class FcsPagoGrupoFactHitoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public FcsPagoGrupoFactHitoAdm (UsrBean usuario) {
		super( FcsPagoGrupoFactHitoBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	FcsPagoGrupoFactHitoBean.C_FECHAMODIFICACION,	FcsPagoGrupoFactHitoBean.C_IDPAGOSJG,
							FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION,	FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL,	FcsPagoGrupoFactHitoBean.C_IDINSTITUCION,
							FcsPagoGrupoFactHitoBean.C_USUMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	FcsPagoGrupoFactHitoBean.C_IDINSTITUCION,		FcsPagoGrupoFactHitoBean.C_IDPAGOSJG,
							FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION,FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		FcsPagoGrupoFactHitoBean bean = null;
		try{
			bean = new FcsPagoGrupoFactHitoBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,FcsPagoGrupoFactHitoBean.C_FECHAMODIFICACION));
			bean.setIdPagosJG(UtilidadesHash.getInteger(hash,FcsPagoGrupoFactHitoBean.C_IDPAGOSJG));
			bean.setIdGrupoFacturacion(UtilidadesHash.getInteger(hash,FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FcsPagoGrupoFactHitoBean.C_IDINSTITUCION));
			bean.setIdHitoGeneral(UtilidadesHash.getInteger(hash,FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FcsPagoGrupoFactHitoBean.C_USUMODIFICACION));
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
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			FcsPagoGrupoFactHitoBean b = (FcsPagoGrupoFactHitoBean) bean;
			hash.put(FcsPagoGrupoFactHitoBean.C_FECHAMODIFICACION,	String.valueOf(b.getFechaMod()));
			hash.put(FcsPagoGrupoFactHitoBean.C_IDPAGOSJG, 		String.valueOf(b.getIdPagosJG()));
			hash.put(FcsPagoGrupoFactHitoBean.C_IDGRUPOFACTURACION, 	String.valueOf(b.getIdGrupoFacturacion()));
			hash.put(FcsPagoGrupoFactHitoBean.C_IDINSTITUCION, 		String.valueOf(b.getIdInstitucion()));
			hash.put(FcsPagoGrupoFactHitoBean.C_IDHITOGENERAL, 		String.valueOf(b.getIdHitoGeneral()));
			hash.put(FcsPagoGrupoFactHitoBean.C_USUMODIFICACION,		String.valueOf(b.getUsuMod()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return null;
	}

		
	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
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
			throw new ClsExceptions (e, "Excepcion en FcsPagoGrupoFactHitoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
	
	/**
	 * Obtener una lista los registros de la tabla FCS_PAGO_GRUPOFACT_HITO que tengan el valo de la institución
	 * y del identificador del pago que se les ha pasado por parámetro.
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */	
	
   	public Vector selectPagoGrupoFactHito (String idinstitucion,
			  String idPago)
   		throws ClsExceptions {
   		
   		String consulta = "";
   		
   		Vector resultado = new Vector();
   		
   		try {
   			consulta += " select * from "+FcsPagoGrupoFactHitoBean.T_NOMBRETABLA+" ";
   			consulta += " where ("+FcsPagoGrupoFactHitoBean.C_IDINSTITUCION+"="+idinstitucion+") ";
   			consulta += "   and ("+FcsPagoGrupoFactHitoBean.C_IDPAGOSJG+"="+idPago+") ";

   			resultado = (Vector)this.selectGenerico(consulta.toString());
   			
   		} catch (Exception e) {
   			throw new ClsExceptions (e, "Excepcion en FcsPagoGrupoFactHitoAdm.selectPagoGrupoFactHito(). Consulta SQL:" + consulta);
   		}
   		
   		return resultado;
   		
   	} //selectPagoGrupoFactHito ()		
	
	/**
	 * Elimina los registros de la tabla FCS_PAGO_GRUPOFACT_HITO que tengan el valo de la institución
	 * y del identificador del pago que se les ha pasado por parámetro.
	 *  
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */	
	
   	public void deletePagoGrupoFactHito (String idinstitucion,
			  String idPago)
   		throws ClsExceptions {
   		
   		String consulta = "";
   		try {
   			consulta += " delete from "+FcsPagoGrupoFactHitoBean.T_NOMBRETABLA+" ";
   			consulta += " where ("+FcsPagoGrupoFactHitoBean.C_IDINSTITUCION+"="+idinstitucion+") ";
   			consulta += "   and ("+FcsPagoGrupoFactHitoBean.C_IDPAGOSJG+"="+idPago+") ";

   			ClsMngBBDD.executeUpdate (consulta);
   		} catch (Exception e) {
   			throw new ClsExceptions (e, "Excepcion en FcsPagoGrupoFactHitoAdm.deletePagoGrupoFactHito(). Consulta SQL:" + consulta);
   		}
   	} //deletePagoGrupoFactHito ()		
}