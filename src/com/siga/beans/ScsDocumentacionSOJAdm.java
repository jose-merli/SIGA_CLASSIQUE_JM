package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.*;

//Clase: ScsDocumentacionSOJAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 03/02/2005
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * 
 */
public class ScsDocumentacionSOJAdm extends MasterBeanAdministrador {

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDocumentacionSOJAdm(UsrBean usuario) {
		super(ScsDocumentacionSOJBean.T_NOMBRETABLA, usuario);
	}
		
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la fiesta que se va a insertar.
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
  			String sql ="SELECT (MAX("+ ScsDocumentacionSOJBean.C_IDDOCUMENTACION + ") + 1) AS IDDOCUMENTACION FROM " + nombreTabla + 
						" where " + ScsDocumentacionSOJBean.C_IDINSTITUCION + " = " + entrada.get(ScsDocumentacionSOJBean.C_IDINSTITUCION) +
						" and " + ScsDocumentacionSOJBean.C_IDTIPOSOJ + " = " + entrada.get(ScsDocumentacionSOJBean.C_IDTIPOSOJ) +
						" and " + ScsDocumentacionSOJBean.C_ANIO + " = " + entrada.get(ScsDocumentacionSOJBean.C_ANIO) + 
						" and " + ScsDocumentacionSOJBean.C_NUMERO + " = " + entrada.get(ScsDocumentacionSOJBean.C_NUMERO);
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDDOCUMENTACION").equals("")) {
					entrada.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION,"1");
				}
				else entrada.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION,(String)prueba.get("IDDOCUMENTACION"));				
			}
			//Ahora ponemos las fechas en el formato adecuado			
			if ((entrada.containsKey(ScsDocumentacionEJGBean.C_FECHAENTREGA)) && (!((String)entrada.get(ScsDocumentacionEJGBean.C_FECHAENTREGA)).equals(""))) entrada.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,GstDate.getApplicationFormatDate("",entrada.get(ScsDocumentacionEJGBean.C_FECHAENTREGA).toString()));
			entrada.put(ScsDocumentacionSOJBean.C_FECHALIMITE,GstDate.getApplicationFormatDate("",entrada.get(ScsDocumentacionSOJBean.C_FECHALIMITE).toString()));
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDDOCUMENTACION");
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
			String sql = "SELECT * FROM " + ScsDocumentacionSOJBean.T_NOMBRETABLA + " " + where ;
			
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
	
	/**
	 * Efectúa un SELECT en la tabla SCS_EJG con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsDocumentacionSOJBean.C_IDINSTITUCION + " = " + hash.get(ScsDocumentacionSOJBean.C_IDINSTITUCION) + " AND " + ScsSOJBean.C_IDTIPOSOJ + " = " + hash.get(ScsDocumentacionSOJBean.C_IDTIPOSOJ) + 
						   " AND " + ScsDocumentacionSOJBean.C_ANIO + " = " + hash.get(ScsDocumentacionSOJBean.C_ANIO) + " AND " + ScsDocumentacionSOJBean.C_NUMERO + " = " + hash.get(ScsDocumentacionSOJBean.C_NUMERO) + " AND " + ScsDocumentacionSOJBean.C_IDDOCUMENTACION + " = " + hash.get(ScsDocumentacionSOJBean.C_IDDOCUMENTACION);
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
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	protected String[] getCamposBean() {
		
		String[] campos= {	ScsDocumentacionSOJBean.C_IDINSTITUCION,		ScsDocumentacionSOJBean.C_IDTIPOSOJ,
							ScsDocumentacionSOJBean.C_ANIO,					ScsDocumentacionSOJBean.C_NUMERO,
							ScsDocumentacionSOJBean.C_IDDOCUMENTACION,		ScsDocumentacionSOJBean.C_FECHALIMITE,
							ScsDocumentacionSOJBean.C_DOCUMENTACION,		ScsDocumentacionSOJBean.C_FECHAENTREGA,
							ScsDocumentacionSOJBean.C_FECHAMODIFICACION,	ScsDocumentacionSOJBean.C_USUMODIFICACION,
							ScsDocumentacionSOJBean.C_REGENTRADA,			ScsDocumentacionSOJBean.C_REGSALIDA		
						 };

		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		
		String[] campos= {ScsDocumentacionSOJBean.C_IDINSTITUCION, ScsDocumentacionSOJBean.C_IDTIPOSOJ, ScsDocumentacionSOJBean.C_ANIO, ScsDocumentacionSOJBean.C_NUMERO, ScsDocumentacionSOJBean.C_IDDOCUMENTACION};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDocumentacionSOJBean bean = null;
		
		try {
			bean = new ScsDocumentacionSOJBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_IDINSTITUCION));
			
			if (hash.get(ScsDocumentacionSOJBean.C_IDINSTITUCION).toString()!="") {
				bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_IDINSTITUCION));
			}
			if (hash.get(ScsDocumentacionSOJBean.C_IDTIPOSOJ).toString()!="") {
				bean.setIdTipoSOJ(UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_IDTIPOSOJ));
			}
			bean.setAnio((UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_ANIO)));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_NUMERO));
			bean.setIdDocumentacion(UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_IDDOCUMENTACION));
			bean.setFechaLimite(UtilidadesHash.getString(hash,ScsDocumentacionSOJBean.C_FECHALIMITE));
			bean.setFechaEntrega(UtilidadesHash.getString(hash,ScsDocumentacionSOJBean.C_FECHAENTREGA));
			bean.setDocumentacion(UtilidadesHash.getString(hash,ScsDocumentacionSOJBean.C_DOCUMENTACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDocumentacionSOJBean.C_USUMODIFICACION));		
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDocumentacionSOJBean.C_FECHAMODIFICACION));
			bean.setRegEntrada(UtilidadesHash.getString(hash,ScsDocumentacionSOJBean.C_REGENTRADA));		
			bean.setRegSalida(UtilidadesHash.getString(hash,ScsDocumentacionSOJBean.C_REGSALIDA));
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
			ScsDocumentacionSOJBean b = (ScsDocumentacionSOJBean) bean;
			htData.put(ScsDocumentacionSOJBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			htData.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ, b.getIdTipoSOJ());
			htData.put(ScsDocumentacionSOJBean.C_ANIO, b.getAnio());
			htData.put(ScsDocumentacionSOJBean.C_NUMERO, b.getNumero());
			htData.put(ScsDocumentacionSOJBean.C_FECHAENTREGA, String.valueOf(b.getFechaEntrega()));
			htData.put(ScsDocumentacionSOJBean.C_FECHALIMITE, String.valueOf(b.getFechaLimite()));
			htData.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION, b.getIdDocumentacion());
			htData.put(ScsDocumentacionSOJBean.C_DOCUMENTACION, b.getDocumentacion());			
			htData.put(ScsDocumentacionSOJBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(ScsDocumentacionSOJBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			htData.put(ScsDocumentacionSOJBean.C_REGENTRADA, String.valueOf(b.getRegEntrada()));
			htData.put(ScsDocumentacionSOJBean.C_REGSALIDA, String.valueOf(b.getRegSalida()));
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

		String[] campos= {ScsDocumentacionSOJBean.C_IDINSTITUCION, ScsDocumentacionSOJBean.C_IDTIPOSOJ, ScsDocumentacionSOJBean.C_ANIO, ScsDocumentacionSOJBean.C_NUMERO};
		return campos;
	}	
}
