package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gratuita.vos.VolantesExpressVo;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DELITO
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsDelitoAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDelitoAdm (UsrBean usuario) {
		super( ScsDelitoBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsDelitoBean.C_IDDELITO, ScsDelitoBean.C_DESCRIPCION, ScsDelitoBean.C_CODIGOEXT,
							ScsDelitoBean.C_USUMODIFICACION, ScsDelitoBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsDelitoBean.C_IDDELITO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDelitoBean bean = null;
		try{
			bean = new ScsDelitoBean();
			bean.setIdDelito(UtilidadesHash.getInteger(hash,ScsDelitoBean.C_IDDELITO));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsDelitoBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsDelitoBean.C_CODIGOEXT));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsDelitoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDelitoBean.C_USUMODIFICACION));
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
			ScsDelitoBean b = (ScsDelitoBean) bean;
			hash.put(ScsDelitoBean.C_IDDELITO, String.valueOf(b.getIdDelito()));
			hash.put(ScsDelitoBean.C_DESCRIPCION, b.getDescripcion());
			hash.put(ScsDelitoBean.C_CODIGOEXT, b.getCodigoExt());

			hash.put(ScsDelitoBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsDelitoBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] vector = {ScsDelitoBean.C_IDDELITO,ScsDelitoBean.C_IDDELITO};
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
			throw new ClsExceptions (e, "Excepcion en ScsDelitoAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdDelito() throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsDelitoBean.C_IDDELITO+")+1 AS ID FROM "+ScsDelitoBean.T_NOMBRETABLA;					  			
			
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
	public List<ScsDelitoBean> getDelitos(VolantesExpressVo volanteExpres)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT IDINSTITUCION,IDDELITO, F_SIGA_GETRECURSO(DESCRIPCION, 1) AS DESCRIPCION ");
		sql.append(" FROM SCS_DELITO ");
		sql.append(" WHERE IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		sql.append(" AND FECHABAJA IS NULL");
		htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
		sql.append(" ORDER BY DESCRIPCION ");
		
		
		
		
		
		List<ScsDelitoBean> alDelitos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alDelitos = new ArrayList<ScsDelitoBean>();
            	ScsDelitoBean delitoBean = new ScsDelitoBean();
            	delitoBean.setDescripcion(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(), "general.combo.seleccionar"));
            	delitoBean.setIdDelito(new Integer(-1));
    			alDelitos.add(delitoBean);
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		delitoBean = new ScsDelitoBean();
            		delitoBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsDelitoBean.C_IDINSTITUCION));
            		delitoBean.setIdDelito(UtilidadesHash.getInteger(htFila,ScsDelitoBean.C_IDDELITO));
            		delitoBean.setDescripcion(UtilidadesHash.getString(htFila,ScsDelitoBean.C_DESCRIPCION));
            		alDelitos.add(delitoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alDelitos;
		
	} 
	
	 
	
}