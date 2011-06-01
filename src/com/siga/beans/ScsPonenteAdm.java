package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_JUZGADO
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsPonenteAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsPonenteAdm (UsrBean usuario) {
		super( ScsPonenteBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsPonenteBean.C_IDINSTITUCION, ScsPonenteBean.C_IDPONENTE,	
							ScsPonenteBean.C_NOMBRE,
							ScsPonenteBean.C_USUMODIFICACION, ScsPonenteBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsPonenteBean.C_IDINSTITUCION, ScsPonenteBean.C_IDPONENTE};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsPonenteBean bean = null;
		try{
			bean = new ScsPonenteBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsPonenteBean.C_IDINSTITUCION));
			bean.setIdPonente(UtilidadesHash.getInteger(hash,ScsPonenteBean.C_IDPONENTE));
			bean.setNombre(UtilidadesHash.getString(hash,ScsPonenteBean.C_NOMBRE));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsPonenteBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsPonenteBean.C_USUMODIFICACION));
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
			ScsPonenteBean b = (ScsPonenteBean) bean;
			hash.put(ScsPonenteBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsPonenteBean.C_IDPONENTE, String.valueOf(b.getIdPonente()));
			hash.put(ScsPonenteBean.C_NOMBRE, b.getNombre());
			hash.put(ScsPonenteBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsPonenteBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] vector = {ScsPonenteBean.C_IDPONENTE,ScsPonenteBean.C_IDINSTITUCION};
		return vector;
	}
	
	/** Funcion getOrdenPorNombre ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenPorNombre() {		
		String[] vector = {"UPPER ("+ScsPonenteBean.T_NOMBRETABLA+"." + ScsPonenteBean.C_NOMBRE + ")"};
		return vector;
	}


	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.queryNLS(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsPonenteAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdJuzgado(String idInstitucion) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsPonenteBean.C_IDPONENTE+")+1 AS ID FROM "+ScsPonenteBean.T_NOMBRETABLA+
					  " WHERE "+ScsPonenteBean.C_IDINSTITUCION+"="+idInstitucion;			
			
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