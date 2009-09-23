package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

//Clase: ScsCalendarioLaboralAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 22/12/2004
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * 
 */
public class ScsCalendarioLaboralAdm extends MasterBeanAdministrador {

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsCalendarioLaboralAdm(UsrBean usuario) {
		super(ScsCalendarioLaboralBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_CALENDARIOLABORAL con los datos de selección intoducidos. 
	 * 
	 * @param miHash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */
	public Vector select(Hashtable hash) throws ClsExceptions {

		Vector datos = new Vector(); 
		
		try {		
			String where = " WHERE ";
			where += ScsCalendarioLaboralBean.C_FECHA + " >= TO_DATE ('01/01/" + hash.get(ScsCalendarioLaboralBean.C_FECHA) + "', 'DD/MM/YYYY') AND "  + 
					 ScsCalendarioLaboralBean.C_FECHA + " <= TO_DATE ('31/12/" + hash.get(ScsCalendarioLaboralBean.C_FECHA) + "', 'DD/MM/YYYY') AND ("  +
					 ScsCalendarioLaboralBean.C_IDINSTITUCION + " = " + hash.get(ScsCalendarioLaboralBean.C_IDINSTITUCION) 	 +
					 " OR " + ScsCalendarioLaboralBean.C_IDINSTITUCION + " IS NULL)";		
		
			datos = this.select(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR");
		}
		return datos;	
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_CALENDARIOLABORAL con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsCalendarioLaboralBean.C_IDENTIFICATIVO + " = " + hash.get(ScsCalendarioLaboralBean.C_IDENTIFICATIVO);		
			datos = this.select(where);
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
	 * Obtiene la clave principal de la tabla SCS_CALENDARIOLABORAL y rellene una hash con las claves para hacer un
	 * selectPorClave()  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados de la búsqueda
	 */	
	public Vector selectByPK(Hashtable hash) throws ClsExceptions
	{
		try {
			String [] campos = this.getCamposBean();
			String [] claves = this.getClavesBean();
			
			Hashtable aux = new Hashtable();
			for (int i = 0; i < claves.length; i++) {
				aux.put(claves[i], hash.get(claves[i]));
			}
			return selectPorClave(aux);
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR PRIMARY KEY");
		}
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
			String sql ="SELECT (MAX("+ ScsCalendarioLaboralBean.C_IDENTIFICATIVO + ") + 1) AS IDENTIFICATIVO FROM " + nombreTabla;	
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDENTIFICATIVO").equals("")) {
					entrada.put(ScsCalendarioLaboralBean.C_IDENTIFICATIVO,"1");
				}
				else entrada.put(ScsCalendarioLaboralBean.C_IDENTIFICATIVO,(String)prueba.get("IDENTIFICATIVO"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDENTIFICATIVO");
		};
		// Si el IDPARTIDO es 0 quiere decir que no se ha seleccionado nada en el combo y por tanto afecta a todo el colegio.
		// Por ello en la hash no metemos nada, para que a la hora de hacer el insert almacene NULL
		if(entrada.containsKey(ScsCalendarioLaboralBean.C_IDPARTIDO) && ((String)entrada.get(ScsCalendarioLaboralBean.C_IDPARTIDO)).equals("0")) {
			entrada.put(ScsCalendarioLaboralBean.C_IDPARTIDO,"");
		}
		
		return entrada;
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
		
		String[] campos= {	ScsCalendarioLaboralBean.C_IDENTIFICATIVO, 
							ScsCalendarioLaboralBean.C_IDINSTITUCION,
							ScsCalendarioLaboralBean.C_IDPARTIDO,
							ScsCalendarioLaboralBean.C_NOMBREFIESTA,
							ScsCalendarioLaboralBean.C_FECHA,
							ScsCalendarioLaboralBean.C_FECHAMODIFICACION,
							ScsCalendarioLaboralBean.C_USUMODIFICACION};

		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		
		String[] campos= {ScsCalendarioLaboralBean.C_IDENTIFICATIVO};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsCalendarioLaboralBean bean = null;
		
		try {
			bean = new ScsCalendarioLaboralBean();
			
			bean.setIdentificativo(UtilidadesHash.getInteger(hash,ScsCalendarioLaboralBean.C_IDENTIFICATIVO));
			
			if ((String)hash.get(ScsCalendarioLaboralBean.C_IDINSTITUCION)!="") {
				bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsCalendarioLaboralBean.C_IDINSTITUCION));
			}
			if ((String)hash.get(ScsCalendarioLaboralBean.C_IDPARTIDO)!="") {
				bean.setIdPartido(UtilidadesHash.getInteger(hash,ScsCalendarioLaboralBean.C_IDPARTIDO));
			}
			bean.setNombreFiesta(UtilidadesHash.getString(hash,ScsCalendarioLaboralBean.C_NOMBREFIESTA));
			bean.setFecha(UtilidadesHash.getString(hash,ScsCalendarioLaboralBean.C_FECHA));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsCalendarioLaboralBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsCalendarioLaboralBean.C_USUMODIFICACION));		
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
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsCalendarioLaboralBean b = (ScsCalendarioLaboralBean) bean;
			htData.put(ScsCalendarioLaboralBean.C_IDENTIFICATIVO, String.valueOf(b.getIdentificativo()));
			htData.put(ScsCalendarioLaboralBean.C_FECHA, String.valueOf(b.getFecha()));
			htData.put(ScsCalendarioLaboralBean.C_IDINSTITUCION, b.getIdInstitucion());
			htData.put(ScsCalendarioLaboralBean.C_IDPARTIDO, b.getIdPartido());
			htData.put(ScsCalendarioLaboralBean.C_NOMBREFIESTA, b.getNombreFiesta().toUpperCase());
			htData.put(ScsCalendarioLaboralBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(ScsCalendarioLaboralBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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

		String[] campos= {ScsCalendarioLaboralBean.C_FECHA};
		return campos;
	}

	/** 
	 * Devuelve un String con la consulta SQL que devuelve la fecha de permuta y la fecha de inicio del confirmador si existe la permuta.
	 * @param String fecha: fecha seleccionada 
	 * @param Hashtable hash con todos los datos necesarios
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String busquedaCalendario3(String fecha, Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idpartido="";
		try {
			idinstitucion = (String)hash.get(ScsCalendarioLaboralBean.C_IDINSTITUCION);
			idpartido = (String)hash.get(ScsCalendarioLaboralBean.C_IDPARTIDO);
			
			consulta = "SELECT * FROM "+ScsCalendarioLaboralBean.T_NOMBRETABLA+" cal ";
			consulta += " WHERE ";
			consulta += " cal."+ScsCalendarioLaboralBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND cal."+ScsCalendarioLaboralBean.C_FECHA+" = TO_DATE ('"+fecha+"','DD/MM/YYYY')";
			consulta += " AND cal."+ScsCalendarioLaboralBean.C_IDPARTIDO+"="+idpartido;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioLaboralAdm.busquedaCalendario3(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}		
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve la fecha de permuta y la fecha de inicio del confirmador si existe la permuta.
	 * @param String fecha: fecha seleccionada 
	 * @param Hashtable hash con todos los datos necesarios
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String busquedaCalendario2(String fecha, Hashtable hash) throws ClsExceptions{
		String idpartido = "";
		String consulta1="", consulta2="";
		String idinstitucion="", idturno="", idguardia="", idpersona="", idcalendario="";
		Vector registros = new Vector();
		
		try {
			idinstitucion = (String)hash.get(ScsTurnoBean.C_IDINSTITUCION);
			idturno = (String)hash.get(ScsTurnoBean.C_IDTURNO);
			
			//Consulta 1 para ver si tiene idzona, idsubzona
			consulta1 = "SELECT turno."+ScsTurnoBean.C_IDZONA+", turno."+ScsTurnoBean.C_IDSUBZONA;
			consulta1 += " FROM "+ScsTurnoBean.T_NOMBRETABLA+" turno";
			consulta1 += " WHERE ";
			consulta1 += " turno."+ScsTurnoBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta1 += " AND turno."+ScsTurnoBean.C_IDTURNO+"="+idturno;
			//Ejecuto la consulta:
			registros = this.selectGenerico(consulta1);			
			
			//Si no encontramos registros no busco mas
			if (registros.size()==0 || ((String)((Hashtable)registros.get(0)).get("IDSUBZONA")).equals("")) 
				idpartido="";
			//Si encontramos un registro miro si tiene asociado un idpartido
			else {
				String idzona = (String)((Hashtable)registros.get(0)).get("IDZONA");
				String idsubzona = (String)((Hashtable)registros.get(0)).get("IDSUBZONA");
				//Consulta 2 para ver si tiene idzona, idsubzona
				consulta2 = "SELECT subzona."+ScsSubzonaBean.C_IDPARTIDO;
				consulta2 += " FROM "+ScsSubzonaBean.T_NOMBRETABLA+" subzona";
				consulta2 += " WHERE ";
				consulta2 += " subzona."+ScsTurnoBean.C_IDINSTITUCION+"="+idinstitucion;
				consulta2 += " AND subzona."+ScsTurnoBean.C_IDZONA+"="+idzona;
				consulta2 += " AND subzona."+ScsTurnoBean.C_IDSUBZONA+"="+idsubzona;
				//Ejecuto la segunda consulta:
				registros.clear();
				registros = this.selectGenerico(consulta2);
				//Si encuentro el idpartido devuelvo el mismo, sino String vacio			
				if (registros.size()!=0)
					idpartido = (String)((Hashtable)registros.get(0)).get("IDPARTIDO");
				else
					idpartido = "";
			}
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioLaboralAdm.busquedaCalendario2(). Consulta SQL:"+consulta1);
		}
		return idpartido;
	}		
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve la fecha de permuta y la fecha de inicio del confirmador si existe la permuta.
	 * @param String fecha: fecha seleccionada 
	 * @param Hashtable hash con todos los datos necesarios
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public static String busquedaCalendario1(String fecha, String idinstitucion) throws ClsExceptions{
		String consulta = "";

		try {
			consulta = "SELECT * FROM "+ScsCalendarioLaboralBean.T_NOMBRETABLA+" cal ";
			consulta += " WHERE ";
			consulta += " cal."+ScsCalendarioLaboralBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND cal."+ScsCalendarioLaboralBean.C_FECHA+" = TO_DATE ('"+fecha+"','DD/MM/YYYY')";
			consulta += " AND cal."+ScsCalendarioLaboralBean.C_IDPARTIDO+" IS NULL";
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsCalendarioLaboralAdm.busquedaCalendario1(). Consulta SQL:"+consulta);
		}
		
		return consulta;
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
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsCalendarioLaboralAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		

	public Vector obtenerFestivosTurno(Integer idInstitucion, Integer idTurno, String fechaInicio, String fechaFin) {
		Vector vFestivos = new Vector();
		String consulta = null;
		
		try {		
			String fechaInicioFormateada = GstDate.getFormatedDateShort("ES",fechaInicio);
			String fechaFinFormateada = GstDate.getFormatedDateShort("ES",fechaFin);
			
			consulta =" SELECT cal."+ScsCalendarioLaboralBean.C_FECHA+
					  " FROM "+ScsCalendarioLaboralBean.T_NOMBRETABLA+" cal "+
					  " WHERE cal."+ScsCalendarioLaboralBean.C_IDINSTITUCION+" = "+idInstitucion+ 
					  " AND cal."+ScsCalendarioLaboralBean.C_FECHA+" >= TO_DATE('"+fechaInicioFormateada+"', 'DD/MM/YYYY') "+
					  " AND cal."+ScsCalendarioLaboralBean.C_FECHA+" <= TO_DATE('"+fechaFinFormateada+"', 'DD/MM/YYYY') "+
					  " AND (    cal."+ScsCalendarioLaboralBean.C_IDPARTIDO+" IS NULL "+ 
					  "       OR cal."+ScsCalendarioLaboralBean.C_IDPARTIDO+" IN  "+
					  "         ( SELECT p."+ScsSubZonaPartidoBean.C_IDPARTIDO+
					  "   		  FROM "+ScsTurnoBean.T_NOMBRETABLA+" t, "+
					  "      		   "+ScsSubZonaPartidoBean.T_NOMBRETABLA+" p "+
					  "	  		  WHERE p."+ScsSubZonaPartidoBean.C_IDINSTITUCION+" = t."+ScsTurnoBean.C_IDINSTITUCION+
					  "     	    AND p."+ScsSubZonaPartidoBean.C_IDZONA+" = t."+ScsTurnoBean.C_IDZONA+
					  "     		AND p."+ScsSubZonaPartidoBean.C_IDSUBZONA+" = t."+ScsTurnoBean.C_IDSUBZONA+
					  "     		AND t."+ScsTurnoBean.C_IDINSTITUCION+" = "+idInstitucion+
					  "     		AND t."+ScsTurnoBean.C_IDTURNO+" = "+idTurno+
					  " 		)"+
					  "     )";
		
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					//Anhado al vector un String con la fecha en formato corto:
					if (registro != null) 
						vFestivos.add(GstDate.getFormatedDateShort("ES",(String)registro.get(ScsCalendarioLaboralBean.C_FECHA)));
				}
			} 
		} catch (Exception e){
			vFestivos.clear();
		}		
		return vFestivos;
	}
	
}
