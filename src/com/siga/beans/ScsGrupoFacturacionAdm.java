
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_GRUPOFACTURACION
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */

public class ScsGrupoFacturacionAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsGrupoFacturacionAdm (UsrBean usuario) {
		super( ScsGrupoFacturacionBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION,		
				ScsGrupoFacturacionBean.C_NOMBRE,
				ScsGrupoFacturacionBean.C_CODIGOEXT,
							ScsGrupoFacturacionBean.C_IDINSTITUCION,			
							ScsGrupoFacturacionBean.C_USUMODIFICACION,			
							ScsGrupoFacturacionBean.C_FECHAMODIFICACION};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsGrupoFacturacionBean.C_IDINSTITUCION, ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsGrupoFacturacionBean bean = null;
		try{
			bean = new ScsGrupoFacturacionBean();
			bean.setIdGrupoFacturacion(UtilidadesHash.getInteger(hash,ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsGrupoFacturacionBean.C_IDINSTITUCION));
			bean.setNombre(UtilidadesHash.getString(hash,ScsGrupoFacturacionBean.C_NOMBRE));			
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsGrupoFacturacionBean.C_CODIGOEXT));			
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsGrupoFacturacionBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsGrupoFacturacionBean.C_USUMODIFICACION));			
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
			ScsGrupoFacturacionBean b = (ScsGrupoFacturacionBean) bean;
			hash.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION, String.valueOf(b.getIdGrupoFacturacion()));
			hash.put(ScsGrupoFacturacionBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			hash.put(ScsGrupoFacturacionBean.C_NOMBRE,String.valueOf(b.getNombre()));			
			hash.put(ScsGrupoFacturacionBean.C_CODIGOEXT,String.valueOf(b.getCodigoExt()));			
			hash.put(ScsGrupoFacturacionBean.C_FECHAMODIFICACION,String.valueOf(b.getFechaMod()));			
			hash.put(ScsGrupoFacturacionBean.C_USUMODIFICACION,String.valueOf(b.getUsuMod()));			
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
		return this.getClavesBean();
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_GRUPOFACTURACION con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
				
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsGrupoFacturacionBean.C_IDINSTITUCION + " = " + hash.get(ScsGrupoFacturacionBean.C_IDINSTITUCION) + " AND " + 
						   ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + " = " + hash.get(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION);		
			datos = this.selectAll(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
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
			String sql = "SELECT IDGRUPOFACTURACION, " +UtilidadesMultidioma.getCampoMultidioma("NOMBRE", this.usrbean.getLanguage()) +", IDINSTITUCION FROM " + ScsGrupoFacturacionBean.T_NOMBRETABLA + where;			
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
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la retencion que se va a insertar.
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;		
		int contador = 0;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + ") + 1) AS IDGRUPOFACTURACION FROM " + nombreTabla;
			sql += " WHERE " + ScsGrupoFacturacionBean.C_IDINSTITUCION + " = " + entrada.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDGRUPOFACTURACION").equals("")) {
					entrada.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION,"1");
				}
				else entrada.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION,(String)prueba.get("IDGRUPOFACTURACION"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDGRUPOFACTURACION");
		};		
		
		return entrada;
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

			if (rc.query(consulta)) {
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
	
	/**
	 * 
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsGrupoFacturacionBean> getTurnosInformes(String idInstitucion) throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		
		String sql = " SELECT " + ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION + ", " +
				" F_SIGA_GETRECURSO(" + ScsGrupoFacturacionBean.C_NOMBRE + ", " + this.usrbean.getLanguage() + ") AS " + ScsGrupoFacturacionBean.C_NOMBRE +
			" FROM " + ScsGrupoFacturacionBean.T_NOMBRETABLA +
			" WHERE " + ScsGrupoFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion +
			" ORDER BY 2";
		
		List<ScsGrupoFacturacionBean> alTurnos = new ArrayList<ScsGrupoFacturacionBean>();
		try {
			RowsContainer rc = new RowsContainer();
			
			if (rc.query(sql)) {
			
				ScsGrupoFacturacionBean turnoBean = new ScsGrupoFacturacionBean();            		
	    		turnoBean.setIdGrupoFacturacion(-1);
	    		turnoBean.setNombre(UtilidadesString.getMensajeIdioma(this.usrbean, "consultas.recuperarconsulta.literal.todos"));
	    		alTurnos.add(turnoBean);												
            
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		turnoBean = new ScsGrupoFacturacionBean();            		
            		turnoBean.setIdGrupoFacturacion(UtilidadesHash.getInteger(htFila, ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION));
            		turnoBean.setNombre(UtilidadesHash.getString(htFila, ScsTurnoBean.C_NOMBRE));
            		alTurnos.add(turnoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
       return alTurnos;		
	}		
}