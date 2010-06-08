/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TELEFONOSPERSONAJG
 * 
 * Creado: julio.vicente 07/02/2005
 *  
 */

package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class ScsTelefonosPersonaJGAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTelefonosPersonaJGAdm (UsrBean usuario) {
		super( ScsTelefonosPersonaJGBean.T_NOMBRETABLA, usuario);
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
			String sql = "SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + " " + where ;
			
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
	 * Efectúa un SELECT en la tabla SCS_TELEFONOSPERSONAJG con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + hash.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + hash.get(ScsTelefonosPersonaJGBean.C_IDPERSONA) + " AND " + ScsTelefonosPersonaJGBean.C_IDTELEFONO + " = " + hash.get(ScsTelefonosPersonaJGBean.C_IDTELEFONO);
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
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del teléfono que se va a insertar.
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
			String sql ="SELECT (MAX("+ ScsTelefonosPersonaJGBean.C_IDTELEFONO + ") + 1) AS IDTELEFONO FROM " + nombreTabla;
			sql += " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + entrada.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION) + " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + entrada.get(ScsTelefonosPersonaJGBean.C_IDPERSONA); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDTELEFONO").equals("")) {
					entrada.put(ScsTelefonosPersonaJGBean.C_IDTELEFONO,"1");
				}
				else entrada.put(ScsTelefonosPersonaJGBean.C_IDTELEFONO,(String)prueba.get("IDTELEFONO"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDTELEFONO");
		};		
		
		return entrada;
	}
	
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	public String[] getCamposBean() {
		String[] campos = {	ScsTelefonosPersonaJGBean.C_IDINSTITUCION,		ScsTelefonosPersonaJGBean.C_IDPERSONA,
							ScsTelefonosPersonaJGBean.C_IDTELEFONO,			ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO,
							ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO, 	ScsTelefonosPersonaJGBean.C_FECHAMODIFICACION,
							ScsTelefonosPersonaJGBean.C_USUMODIFICACION,    ScsTelefonosPersonaJGBean.C_PREFERENTESMS							
						  };

		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTelefonosPersonaJGBean.C_IDINSTITUCION,	ScsTelefonosPersonaJGBean.C_IDPERSONA, ScsTelefonosPersonaJGBean.C_IDTELEFONO};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTelefonosPersonaJGBean bean = null;
		try{
			bean = new ScsTelefonosPersonaJGBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsTelefonosPersonaJGBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsTelefonosPersonaJGBean.C_IDPERSONA));
			bean.setIdTelefono(UtilidadesHash.getInteger(hash,ScsTelefonosPersonaJGBean.C_IDTELEFONO));
			bean.setNumeroTelefono(UtilidadesHash.getString(hash,ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO));
			bean.setNombreTelefono(UtilidadesHash.getString(hash,ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO));
			bean.setpreferenteSms(UtilidadesHash.getString(hash,ScsTelefonosPersonaJGBean.C_PREFERENTESMS));
									
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
		Hashtable hash = new Hashtable();
		
		try{
			ScsTelefonosPersonaJGBean miBean = (ScsTelefonosPersonaJGBean) bean;			
			if (miBean.getIdInstitucion() != null) UtilidadesHash.set(hash,ScsTelefonosPersonaJGBean.C_IDINSTITUCION,miBean.getIdInstitucion().toString());
			UtilidadesHash.set(hash,ScsTelefonosPersonaJGBean.C_IDPERSONA,miBean.getIdPersona().toString());
			UtilidadesHash.set(hash,ScsTelefonosPersonaJGBean.C_IDTELEFONO,miBean.getIdTelefono().toString());
			UtilidadesHash.set(hash,ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO,miBean.getNumeroTelefono());
			UtilidadesHash.set(hash,ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO,miBean.getNombreTelefono());						 
			UtilidadesHash.set(hash,ScsTelefonosPersonaJGBean.C_PREFERENTESMS,miBean.getpreferenteSms());
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
		String[] orden = {ScsTelefonosPersonaJGBean.C_IDINSTITUCION, ScsTelefonosPersonaJGBean.C_IDPERSONA, ScsTelefonosPersonaJGBean.C_IDTELEFONO};
		return orden;
	}
	
	/** Funcion getCamposActualizablesBean ()
	 *  @return conjunto de datos con los nombres de los campos actualizables
	 * */
	protected String[] getCamposActualizablesBean ()
	{
	    return getCamposBean();
	}
	
	/** 
	 * Recoge el numero de telefono asociado 
	 * @param  institucion - identificador de la institucion
	 * @param  idPersona - numero de expediente
	 * @return  String - numero telefono  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String getNumeroTelefono (String institucion, String idPersona) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   String numero="";
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
			    			ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_NUMEROTELEFONO + " AS NUMERO_TELEFONO" +
							" FROM " + ScsTelefonosPersonaBean.T_NOMBRETABLA +
							" WHERE " +
							ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDTELEFONO + 
								"=(" + 
								" SELECT MIN(" + ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDTELEFONO + ")" +
										 " FROM " + ScsTelefonosPersonaBean.T_NOMBRETABLA +
										 " WHERE " +
										 ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDINSTITUCION + "=" + institucion +
										 " AND " +
										 ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDPERSONA + "=" + idPersona + ")";

	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado.get("NUMERO_TELEFONO"));
	               }
	            }
	            if (!datos.isEmpty()){
	            	numero=datos.firstElement().toString();
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener los numeros de telefonos.");
	       }
	       return numero;                        
	    }
	
	
		public List<ScsTelefonosPersonaJGBean> getListadoTelefonosPersonaJG(String idPersonaJG,String idInstitucion) throws ClsExceptions  
	{
			StringBuffer sql = new StringBuffer();			
			Hashtable h = new Hashtable();	
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersonaJG);
			
		    sql.append(" SELECT * FROM SCS_TELEFONOSPERSONA st WHERE st.IDINSTITUCION = :1 AND st.IDPERSONA = :2");
			
			List<ScsTelefonosPersonaJGBean> telefonos = null;			
			try {
				RowsContainer rc = new RowsContainer(); 
				if (rc.findBind(sql.toString(),h)) {
					 telefonos = new ArrayList<ScsTelefonosPersonaJGBean>();
					 	ScsTelefonosPersonaJGBean telefonosBean = null;
					 
					 	for (int i = 0; i < rc.size(); i++){
					 		Row fila = (Row) rc.get(i);
					 		Hashtable<String, Object> htFila=fila.getRow();           		
					 		telefonosBean = new ScsTelefonosPersonaJGBean();
		            		telefonosBean.setNombreTelefono(UtilidadesHash.getString(htFila,ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO));
		            		telefonosBean.setNumeroTelefono(UtilidadesHash.getString(htFila,ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO));
		            		telefonosBean.setpreferenteSms(UtilidadesHash.getString(htFila,ScsTelefonosPersonaJGBean.C_PREFERENTESMS));
		            		telefonos.add(telefonosBean);
            	}
			}
		} catch (Exception e) {
    	   ClsLogging.writeFileLog("Telefonos :Error Select getListadoTelefonosPersonaJG"+e.toString(), 10);
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }			
		return telefonos;
	
	}
		
		
		
		public String deleteTelefonos(Hashtable entrada) throws ClsExceptions,SIGAException {
		    StringBuffer sqlInsertTelefono= new StringBuffer();			
			
	       try {	    	   	    	   
	    	   sqlInsertTelefono.append(" DELETE SCS_TELEFONOSPERSONA");
	    	   sqlInsertTelefono.append(" WHERE IDINSTITUCION ="+entrada.get(ScsTelefonosPersonaJGBean.C_IDINSTITUCION)+"  AND IDPERSONA = "+entrada.get(ScsTelefonosPersonaJGBean.C_IDPERSONA));	    	   
	    	   
	    	  
			
			} catch (Exception e) {
				throw new ClsExceptions(e, "Excepcion en insertTelefono.");
			}			
			 return sqlInsertTelefono.toString();

		}
		
		
		
	
	
}