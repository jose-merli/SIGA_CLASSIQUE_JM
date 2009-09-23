package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DELITO
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsDeclaracionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDeclaracionAdm (UsrBean usuario) {
		super( ScsDeclaracionBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsDeclaracionBean.C_IDDECLARACION, ScsDeclaracionBean.C_DESCRIPCION,
							ScsDeclaracionBean.C_USUMODIFICACION, ScsDeclaracionBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsDeclaracionBean.C_IDDECLARACION};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDeclaracionBean bean = null;
		try{
			bean = new ScsDeclaracionBean();
			bean.setIdDeclaracion(UtilidadesHash.getInteger(hash,ScsDeclaracionBean.C_IDDECLARACION));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsDeclaracionBean.C_DESCRIPCION));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsDeclaracionBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDeclaracionBean.C_USUMODIFICACION));
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
			ScsDeclaracionBean b = (ScsDeclaracionBean) bean;
			hash.put(ScsDeclaracionBean.C_IDDECLARACION, String.valueOf(b.getIdDeclaracion()));
			hash.put(ScsDeclaracionBean.C_DESCRIPCION, b.getDescripcion());

			hash.put(ScsDeclaracionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsDeclaracionBean.C_FECHAMODIFICACION, b.getFechaMod());
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
	public String[] getOrdenCampos() {
		String[] vector = {ScsDeclaracionBean.C_IDDECLARACION,ScsDeclaracionBean.C_IDDECLARACION};
		return vector;
	}


	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsDeclaracionAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdDeclaracion() throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsDeclaracionBean.C_IDDECLARACION+")+1 AS ID FROM "+ScsDeclaracionBean.T_NOMBRETABLA;					  			
			
			datos = this.selectGenerico(select);
			String id = (String)((Hashtable)datos.get(0)).get("ID");
			
			if ( (datos == null) || (id!= null && id.equals("")) )
				nuevoId = new Integer("0");
			else
				nuevoId = new Integer(id);

		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return nuevoId;
	}
	
}