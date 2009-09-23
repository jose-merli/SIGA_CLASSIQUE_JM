package com.siga.beans;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

//Clase: ScsEJGAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * 
 */
public class ScsEJGDESIGNAAdm extends MasterBeanAdministrador {
	private   String  error = "";
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsEJGDESIGNAAdm(UsrBean usuario) {
		super(ScsEJGDESIGNABean.T_NOMBRETABLA, usuario);
	}
	
	
	
	/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.queryNLS(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	
	
	
	
	/** Funcion select (String where). Devuele todos los campos de los registros que cumplan los criterios.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectAll(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT * FROM " + ScsEJGBean.T_NOMBRETABLA + " " + where ;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'selectAll' en B.D."); 
		}		
		return datos;
	}
	
	
	
	
	/** Funcion update (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el update 
	 *  @return true -> OK false -> Error 
	 * */
	public boolean update(Hashtable hash) throws ClsExceptions {
		try {
			return this.update(hash,null);
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el 'update' en B.D. " + e.getMessage());
		}
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	protected String[] getCamposBean() {
		
		String[] campos= {	ScsEJGDESIGNABean.C_IDINSTITUCION,		ScsEJGDESIGNABean.C_ANIOEJG,
							ScsEJGDESIGNABean.C_NUMEROEJG,			ScsEJGDESIGNABean.C_IDTIPOEJG,
							ScsEJGDESIGNABean.C_IDTURNO,			ScsEJGDESIGNABean.C_ANIODESIGNA,
							ScsEJGDESIGNABean.C_NUMERODESIGNA,		ScsEJGDESIGNABean.C_FECHAMODIFICACION,
							ScsEJGDESIGNABean.C_USUMODIFICACION };
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		
		String[] campos= {ScsEJGDESIGNABean.C_IDINSTITUCION, ScsEJGDESIGNABean.C_IDTIPOEJG, ScsEJGDESIGNABean.C_ANIOEJG, ScsEJGDESIGNABean.C_NUMEROEJG,
						  ScsEJGDESIGNABean.C_IDTURNO,ScsEJGDESIGNABean.C_ANIODESIGNA,ScsEJGDESIGNABean.C_NUMERODESIGNA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsEJGDESIGNABean bean = null;
		
		try {
			bean = new ScsEJGDESIGNABean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsEJGDESIGNABean.C_IDINSTITUCION));
			bean.setAnioEJG((UtilidadesHash.getInteger(hash,ScsEJGDESIGNABean.C_ANIOEJG)));
			bean.setNumeroEJG((UtilidadesHash.getInteger(hash,ScsEJGDESIGNABean.C_NUMEROEJG)));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,ScsEJGDESIGNABean.C_IDTIPOEJG));
			bean.setIdTurno((UtilidadesHash.getInteger(hash, ScsEJGDESIGNABean.C_IDTURNO)));
			bean.setAnioDesigna((UtilidadesHash.getInteger(hash, ScsEJGDESIGNABean.C_ANIODESIGNA)));
			bean.setNumeroDesigna((UtilidadesHash.getInteger(hash, ScsEJGDESIGNABean.C_NUMERODESIGNA)));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsEJGDESIGNABean.C_USUMODIFICACION));		
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsEJGDESIGNABean.C_FECHAMODIFICACION));
			
			
		}
		catch (Exception e){
			throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}		
		return bean;

	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsEJGDESIGNABean b = (ScsEJGDESIGNABean) bean;
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_ANIOEJG, b.getAnioEJG());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_NUMEROEJG, b.getNumeroEJG());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_IDTIPOEJG, b.getIdTipoEJG());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_IDTURNO, b.getIdTurno());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_ANIODESIGNA, b.getAnioDesigna());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_NUMERODESIGNA, b.getNumeroDesigna());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,ScsEJGDESIGNABean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}
	
	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {

		String[] campos= {ScsEJGDESIGNABean.C_IDINSTITUCION, ScsEJGDESIGNABean.C_IDTIPOEJG, ScsEJGDESIGNABean.C_ANIOEJG, ScsEJGDESIGNABean.C_NUMEROEJG,
				  ScsEJGDESIGNABean.C_IDTURNO,ScsEJGDESIGNABean.C_ANIODESIGNA,ScsEJGDESIGNABean.C_NUMERODESIGNA};
		return campos;
	}
	
	public boolean insert(Hashtable hash) throws ClsExceptions{
		try
		{
			// Establecemos las datos de insercion
			this.setModificacion(hash);

			Row row = new Row();
			row.load(hash);

			//String [] campos = this.getCamposBean();
			String [] campos = this.getCamposActualizablesBean();
			
			if (row.add(this.nombreTabla, campos) == 1) {
				return true;
			}
			else {
				this.error = "Error al insertar el elemento en BD";
			}
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el \"insert\" en B.D");
		}
		return false;
	}
	private void setModificacion(Hashtable hash) throws ClsExceptions {
		try {
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al insertar el usuario de modificacion");
		}
	}
	public String getError() {
		return this.error;
	}
	
	/** Funcion setError()
	 *  @param mensaje de error 
	 * */
	public void setError(String mensaje) {
		this.error=mensaje;
	}
	public boolean delete(Hashtable hash) throws ClsExceptions{

		try {
			Row row = new Row();	
			row.load(hash);

			String [] claves = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, claves) == 1) {
				return true;
			}
			else{
				this.error = "Error al borrar el elemento en BD";
			}
		}
		catch (Exception e) {
		    this.error=e.toString();
			e.printStackTrace();
			throw new ClsExceptions(e, "Error al realizar el \"delete\" en B.D.");
		}
		return false;
	}


	
}
