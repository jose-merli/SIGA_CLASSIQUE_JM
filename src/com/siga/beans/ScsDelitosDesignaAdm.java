package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DELITOSDESIGNA
 * 
 * @author david.sanchezp
 * @since 24/01/2006
 */
public class ScsDelitosDesignaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDelitosDesignaAdm (UsrBean usuario) {
		super( ScsDelitosDesignaBean.T_NOMBRETABLA, usuario);
	}
  

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	
	protected String[] getCamposBean() {
		String[] campos = {	ScsDelitosDesignaBean.C_IDINSTITUCION, ScsDelitosDesignaBean.C_ANIO,
							ScsDelitosDesignaBean.C_NUMERO, ScsDelitosDesignaBean.C_IDTURNO,
							ScsDelitosDesignaBean.C_IDDELITO,
							ScsDelitosDesignaBean.C_USUMODIFICACION, ScsDelitosDesignaBean.C_FECHAMODIFICACION};
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsDelitosDesignaBean.C_IDINSTITUCION, ScsDelitosDesignaBean.C_ANIO,
							ScsDelitosDesignaBean.C_NUMERO, ScsDelitosDesignaBean.C_IDTURNO,
							ScsDelitosDesignaBean.C_IDDELITO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDelitosDesignaBean bean = null;
		try{
			bean = new ScsDelitosDesignaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDelitosDesignaBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDelitosDesignaBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDelitosDesignaBean.C_NUMERO));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDelitosDesignaBean.C_IDTURNO));
			bean.setIdDelito(UtilidadesHash.getInteger(hash,ScsDelitosDesignaBean.C_IDDELITO));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsDelitosDesignaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDelitosDesignaBean.C_USUMODIFICACION));
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
			ScsDelitosDesignaBean b = (ScsDelitosDesignaBean) bean;
			hash.put(ScsDelitosDesignaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsDelitosDesignaBean.C_ANIO, String.valueOf(b.getAnio()));
			hash.put(ScsDelitosDesignaBean.C_NUMERO, String.valueOf(b.getNumero()));
			hash.put(ScsDelitosDesignaBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsDelitosDesignaBean.C_IDDELITO, String.valueOf(b.getIdDelito()));

			hash.put(ScsDelitosDesignaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			hash.put(ScsDelitosDesignaBean.C_FECHAMODIFICACION, b.getFechaMod());
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
		String[] campos = {	ScsDelitosDesignaBean.C_IDINSTITUCION, ScsDelitosDesignaBean.C_ANIO,
				ScsDelitosDesignaBean.C_NUMERO, ScsDelitosDesignaBean.C_IDTURNO,
				ScsDelitosDesignaBean.C_IDDELITO};
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
			throw new ClsExceptions (e, "Excepcion en ScsDelitosDesignaAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	

	public Integer getNuevoIdDelitosDesigna(Integer idInstitucion, Integer anio, Integer numero, Integer idTurno) throws ClsExceptions {
		Vector datos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT MAX("+ScsDelitosDesignaBean.C_IDDELITO+")+1 AS ID FROM "+ScsDelitosDesignaBean.T_NOMBRETABLA+
					  " WHERE "+ScsDelitosDesignaBean.C_IDINSTITUCION+"="+idInstitucion+
					  " AND "+ScsDelitosDesignaBean.C_ANIO+"="+anio+
					  " AND "+ScsDelitosDesignaBean.C_NUMERO+"="+numero+
					  " AND "+ScsDelitosDesignaBean.C_IDTURNO+"="+idTurno;
			
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

	public Vector getDelitosDesigna(Integer idInstitucion, Integer anio, Integer numero, Integer idTurno, String idioma) throws ClsExceptions {
		Vector vDelitos = new Vector();
		String select = null;
		Integer nuevoId;
		
		try {
			select  = "SELECT F_SIGA_GETRECURSO(delito." + ScsDelitoBean.C_DESCRIPCION + ", " + idioma  + ")" + ScsDelitoBean.C_DESCRIPCION +
					  " ,delito."+ScsDelitoBean.C_IDDELITO+
					  " FROM "+ScsDelitosDesignaBean.T_NOMBRETABLA+" delitoDesigna"+
					  ","+ScsDelitoBean.T_NOMBRETABLA+" delito "+
					  " WHERE delitoDesigna."+ScsDelitosDesignaBean.C_IDINSTITUCION+"="+idInstitucion+
					  " AND delitoDesigna."+ScsDelitosDesignaBean.C_ANIO+"="+anio+
					  " AND delitoDesigna."+ScsDelitosDesignaBean.C_NUMERO+"="+numero+
					  " AND delitoDesigna."+ScsDelitosDesignaBean.C_IDTURNO+"="+idTurno+
					  " AND delitoDesigna."+ScsDelitosDesignaBean.C_IDDELITO+"=delito."+ScsDelitoBean.C_IDDELITO+
					  " AND delitoDesigna."+ScsDelitosDesignaBean.C_IDINSTITUCION+"=delito."+ScsDelitoBean.C_IDINSTITUCION+
					  " ORDER BY  delito."+ScsDelitoBean.C_DESCRIPCION;
			
			vDelitos = this.selectGenerico(select);
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return vDelitos;
	}	
	
}