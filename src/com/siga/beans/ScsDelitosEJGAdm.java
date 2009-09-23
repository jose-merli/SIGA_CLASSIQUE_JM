package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DELITOSEJG
 * 
 * @author david.sanchezp
 * @since 24/01/2006
 */
public class ScsDelitosEJGAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDelitosEJGAdm (UsrBean usuario) {
		super( ScsDelitosEJGBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsDelitosEJGBean.C_IDINSTITUCION, ScsDelitosEJGBean.C_ANIO,
							ScsDelitosEJGBean.C_NUMERO, ScsDelitosEJGBean.C_IDTIPOEJG,
							ScsDelitosEJGBean.C_IDDELITO,
							ScsDelitosEJGBean.C_USUMODIFICACION, ScsDelitosEJGBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsDelitosEJGBean.C_IDINSTITUCION, ScsDelitosEJGBean.C_ANIO,
							ScsDelitosEJGBean.C_NUMERO, ScsDelitosEJGBean.C_IDTIPOEJG,
							ScsDelitosEJGBean.C_IDDELITO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDelitosEJGBean bean = null;
		try{
			bean = new ScsDelitosEJGBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDelitosEJGBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDelitosEJGBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDelitosEJGBean.C_NUMERO));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,ScsDelitosEJGBean.C_IDTIPOEJG));
			bean.setIdDelito(UtilidadesHash.getInteger(hash,ScsDelitosEJGBean.C_IDDELITO));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsDelitosEJGBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDelitosEJGBean.C_USUMODIFICACION));
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
			ScsDelitosEJGBean b = (ScsDelitosEJGBean) bean;
			hash.put(ScsDelitosEJGBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsDelitosEJGBean.C_ANIO, String.valueOf(b.getAnio()));
			hash.put(ScsDelitosEJGBean.C_NUMERO, String.valueOf(b.getNumero()));
			hash.put(ScsDelitosEJGBean.C_IDTIPOEJG, String.valueOf(b.getIdTipoEJG()));
			hash.put(ScsDelitosEJGBean.C_IDDELITO, String.valueOf(b.getIdDelito()));

			hash.put(ScsDelitosEJGBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsDelitosEJGBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] campos = {	ScsDelitosEJGBean.C_IDINSTITUCION, ScsDelitosEJGBean.C_ANIO,
				ScsDelitosEJGBean.C_NUMERO, ScsDelitosEJGBean.C_IDTIPOEJG,
				ScsDelitosEJGBean.C_IDDELITO};
		return campos;
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
			throw new ClsExceptions (e, "Excepcion en ScsDelitosEJGAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdDelitosEJG(Integer idInstitucion, Integer anio, Integer numero, Integer idTipoEJG) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsDelitosEJGBean.C_IDDELITO+")+1 AS ID FROM "+ScsDelitosEJGBean.T_NOMBRETABLA+
					  " WHERE "+ScsDelitosEJGBean.C_IDINSTITUCION+"="+idInstitucion+
					  " AND "+ScsDelitosEJGBean.C_ANIO+"="+anio+
					  " AND "+ScsDelitosEJGBean.C_NUMERO+"="+numero+
					  " AND "+ScsDelitosEJGBean.C_IDTIPOEJG+"="+idTipoEJG;
			
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

	public Vector getDelitosEJG(Integer idInstitucion, Integer anio, Integer numero, Integer idTipoEJG, String idioma) throws ClsExceptions {
		Vector vDelitos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT F_SIGA_GETRECURSO(delito." + ScsDelitoBean.C_DESCRIPCION + ", " + idioma  + ")" + ScsDelitoBean.C_DESCRIPCION +
					  " ,delito."+ScsDelitoBean.C_IDDELITO+
					  " FROM "+ScsDelitosEJGBean.T_NOMBRETABLA+" delitosEJG"+
					  ","+ScsDelitoBean.T_NOMBRETABLA+" delito "+
					  " WHERE delitosEJG."+ScsDelitosEJGBean.C_IDINSTITUCION+"="+idInstitucion+
					  " AND delitosEJG."+ScsDelitosEJGBean.C_ANIO+"="+anio+
					  " AND delitosEJG."+ScsDelitosEJGBean.C_NUMERO+"="+numero+
					  " AND delitosEJG."+ScsDelitosEJGBean.C_IDTIPOEJG+"="+idTipoEJG+
					  " AND delitosEJG."+ScsDelitosEJGBean.C_IDDELITO+"=delito."+ScsDelitoBean.C_IDDELITO+
					  " AND delitosEJG."+ScsDelitosEJGBean.C_IDINSTITUCION+"=delito."+ScsDelitoBean.C_IDINSTITUCION+
					  " ORDER BY delito."+ScsDelitoBean.C_DESCRIPCION;			
			
			vDelitos = this.selectGenerico(select);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return vDelitos;
	}	
	
}