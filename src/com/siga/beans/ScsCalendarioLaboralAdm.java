package com.siga.beans;

import java.util.*;

import com.atos.utils.ClsConstants;
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
	 * Obtiene la lista de festivos para un colegio, dado un anyo.
	 * - Hace caso omiso al partido al que pertenezcan los festivos, devuelve todos.
	 * - Se incluyen tambien los festivos generales, es decir los introducidos en CGAE.
	 * 
	 * El uso correcto de este metodo es para obtener los festivos configurados en un colegio 
	 * y no para saber si un dia concreto es festivo, 
	 * ya que para esto habria que revisar el partido al que pertenece el festivo.
	 * 
	 * @return Vector con los resultados
	 */
	public Vector getFestivosAnyo(String idinstitucion, String anyo) throws ClsExceptions
	{
		Vector datos;

		if (anyo == null || anyo.length() != 4)
			return null;

		StringBuilder where = new StringBuilder();
		where.append(" WHERE to_char( "+ScsCalendarioLaboralBean.C_FECHA+", 'yyyy') = " +anyo+ " ");
		where.append("   AND " +ScsCalendarioLaboralBean.C_IDINSTITUCION+ " in (" +idinstitucion+ ", " +ClsConstants.INSTITUCION_CGAE+ ")");

		datos = new Vector();
		try {
			datos = this.select(where.toString());
		} catch (Exception e) {
			throw new ClsExceptions(e, "EXCEPCION EN BUSCAR");
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
	 * Obtiene la lista de festivos configuradas en un colegio, dados un turno (de donde se sacaran los
	 * partidos judiciales) y un periodo de fechas.
	 * 
	 * - Se devolveran los festivos globales (establecidos como Nacionales en el Consejo), 
	 * los festivos configurados globales para el colegio 
	 * y los festivos locales que pertenezcan a un partido configurado dentro del turno indicado.
	 * 
	 * - Si no se indica turno, no se devolvera ningun festivo asociado a partido judicial.
	 * 
	 * @param idInstitucion
	 * @param idTurno
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 * @throws ClsExceptions 
	 */
	public Vector obtenerFestivosTurno(Integer idInstitucion, Integer idTurno, String fechaInicio, Date fechaFin) throws ClsExceptions
	{
		Vector vFestivos = new Vector();

		String fechaInicioFormateada = GstDate.getFormatedDateShort("ES", fechaInicio);
		String fechaFinFormateada = GstDate.getFormatedDateShort(fechaFin);

		StringBuilder consulta = new StringBuilder();
		consulta.append(" SELECT cal." + ScsCalendarioLaboralBean.C_FECHA);
		consulta.append("   FROM " + ScsCalendarioLaboralBean.T_NOMBRETABLA + " cal ");
		consulta.append("  WHERE cal." + ScsCalendarioLaboralBean.C_FECHA);
		consulta.append("             between TO_DATE('");
		consulta.append(fechaInicioFormateada);
		consulta.append("', 'DD/MM/YYYY') AND TO_DATE('");
		consulta.append(fechaFinFormateada);
		consulta.append("', 'DD/MM/YYYY') ");
		consulta.append("    AND (cal." + ScsCalendarioLaboralBean.C_IDINSTITUCION + " = ");
		consulta.append(idInstitucion);
		consulta.append("         OR cal." + ScsCalendarioLaboralBean.C_IDINSTITUCION + " = ");
		consulta.append(ClsConstants.INSTITUCION_CGAE);
		consulta.append("        ) ");
		consulta.append("    AND (cal." + ScsCalendarioLaboralBean.C_IDPARTIDO + " IS NULL ");
		consulta.append("         OR cal." + ScsCalendarioLaboralBean.C_IDPARTIDO + " IN  ");
		consulta.append("               (SELECT p." + ScsSubZonaPartidoBean.C_IDPARTIDO);
		consulta.append("                  FROM " + ScsTurnoBean.T_NOMBRETABLA + " t, ");
		consulta.append("      	                " + ScsSubZonaPartidoBean.T_NOMBRETABLA + " p ");
		consulta.append("                 WHERE p." + ScsSubZonaPartidoBean.C_IDINSTITUCION + " = t." + ScsTurnoBean.C_IDINSTITUCION);
		consulta.append("                   AND p." + ScsSubZonaPartidoBean.C_IDZONA + " = t." + ScsTurnoBean.C_IDZONA);
		consulta.append("                   AND p." + ScsSubZonaPartidoBean.C_IDSUBZONA + " = t." + ScsTurnoBean.C_IDSUBZONA);
		consulta.append("                   AND t." + ScsTurnoBean.C_IDINSTITUCION + " = " + idInstitucion);
		consulta.append("                   AND t." + ScsTurnoBean.C_IDTURNO + " = " + idTurno);
		consulta.append("               )");
		consulta.append("        )");

		RowsContainer rc = new RowsContainer();
		Row fila;
		Hashtable registro;
		if (rc.query(consulta.toString())) {
			for (int i = 0; i < rc.size(); i++) {
				fila = (Row) rc.get(i);
				registro = (Hashtable) fila.getRow();
				// anyadiendo la fecha en formato corto
				if (registro != null)
					vFestivos.add(GstDate.getFormatedDateShort("ES", (String) registro.get(ScsCalendarioLaboralBean.C_FECHA)));
			}
		}
		
		return vFestivos;
	}
	
}
