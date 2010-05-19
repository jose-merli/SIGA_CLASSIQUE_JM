
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.gratuita.vos.VolantesExpressVo;


/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_GUARDIASTURNO
 * 
 * @author ruben.fernandez
 * 
 * Modificado por david.sanchezp el 28-12-2004 para incluir el metodo etiquetaTipoDia().
 * Modificado por david.sanchezp el 20-1-2005 para incluir la opcion 4 del swicth del metodo getCamposTabla() <br>
 * y el método getDatosCalendario().
 * Modificado por david.sanchezp el 26-1-2005 para incluir el método estático obtenerTipoDia.
 * 
 * @since 1/11/2004 
 * 
 * @version 08/03/2006 david.sanchezp: nuevos campos
 * @version 12/05/2008 adrian.ayala: modificacion de la configuracion de tipos de dias y mas
 */


public class ScsGuardiasTurnoAdm extends MasterBeanAdministrador
{
	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsGuardiasTurnoAdm (UsrBean usuario) {
		super (ScsGuardiasTurnoBean.T_NOMBRETABLA, usuario);
	}
	
	/** 
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean ()
	{
		String[] campos = {
				ScsGuardiasTurnoBean.C_IDINSTITUCION,
				ScsGuardiasTurnoBean.C_IDTURNO,
				ScsGuardiasTurnoBean.C_IDGUARDIA,
				ScsGuardiasTurnoBean.C_NOMBRE,
				ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA,
				ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA,
				ScsGuardiasTurnoBean.C_DIASGUARDIA,
				ScsGuardiasTurnoBean.C_DIASPAGADOS,
				ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES,
				ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS,
				ScsGuardiasTurnoBean.C_NUMEROASISTENCIAS,
				ScsGuardiasTurnoBean.C_NUMEROACTUACIONES,
				ScsGuardiasTurnoBean.C_DESCRIPCION,
				ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION,
				ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO,
				ScsGuardiasTurnoBean.C_IDORDENACIONCOLAS,
				ScsGuardiasTurnoBean.C_IDPARTIDAPRESUPUESTARIA,
				ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO,
				ScsGuardiasTurnoBean.C_TIPODIASGUARDIA,
				ScsGuardiasTurnoBean.C_DIASPERIODO,
				ScsGuardiasTurnoBean.C_TIPODIASPERIODO,
				ScsGuardiasTurnoBean.C_FECHAMODIFICACION,
				ScsGuardiasTurnoBean.C_USUMODIFICACION,
				ScsGuardiasTurnoBean.C_SELECCIONLABORABLES,
				ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS,
				ScsGuardiasTurnoBean.C_IDGUARDIASUSTITUCION,
				ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO,
				ScsGuardiasTurnoBean.C_IDTURNOSUSTITUCION
				
		};
		return campos;
	} //getCamposBean ()
	
	/** 
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean ()
	{
		String[] campos = {
				ScsGuardiasTurnoBean.C_IDINSTITUCION,
				ScsGuardiasTurnoBean.C_IDTURNO,
				ScsGuardiasTurnoBean.C_IDGUARDIA
		};
		return campos;
	} //getClavesBean ()
	
	/** 
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	public MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		ScsGuardiasTurnoBean bean = null;
		try
		{
			bean = new ScsGuardiasTurnoBean ();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDTURNO));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDGUARDIA));
			bean.setNombre(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_NOMBRE));
			bean.setNumeroLetradosGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
			bean.setNumeroSustitutosGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
			bean.setDiasGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASGUARDIA));
			bean.setDiasPagados(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASPAGADOS));
			bean.setValidarJustificaciones(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES));
			bean.setDiasSeparacionGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS));
			bean.setNumeroAsistencias(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROASISTENCIAS));
			bean.setNumeroActuaciones(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROACTUACIONES));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_DESCRIPCION));
			bean.setDescripcionFacturacion(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION));
			bean.setDescripcionPago(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO));
			bean.setIdOrdenacionColas(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDORDENACIONCOLAS));
			bean.setIdPartidaPresupuestaria(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDPARTIDAPRESUPUESTARIA));
			bean.setIdPersona_Ultimo(UtilidadesHash.getLong(hash, ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO));
			bean.setTipodiasGuardia(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_TIPODIASGUARDIA));
			bean.setDiasPeriodo(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASPERIODO));
			bean.setTipoDiasPeriodo(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_TIPODIASPERIODO));
			bean.setSeleccionLaborables(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_SELECCIONLABORABLES));
			bean.setEsViolenciaGenero(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO));
			bean.setSeleccionFestivos(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS));
			bean.setIdGuardiaSustitucion(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDGUARDIASUSTITUCION));
			bean.setIdTurnoSustitucion(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDTURNOSUSTITUCION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;	
	} //hashTableToBean ()
	
	/** 
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	public Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			ScsGuardiasTurnoBean b = (ScsGuardiasTurnoBean) bean;
			
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDTURNO, b.getIdTurno());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDGUARDIA, b.getIdGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA, b.getNumeroLetradosGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA, b.getNumeroSustitutosGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASGUARDIA, b.getDiasGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASPAGADOS, b.getDiasPagados());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES, b.getValidarJustificaciones());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS, b.getDiasSeparacionGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROASISTENCIAS, b.getNumeroAsistencias());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROACTUACIONES, b.getNumeroActuaciones());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION, b.getDescripcionFacturacion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO, b.getDescripcionPago());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDORDENACIONCOLAS, b.getIdOrdenacionColas());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDPARTIDAPRESUPUESTARIA, b.getIdPartidaPresupuestaria());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO, b.getIdPersona_Ultimo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_TIPODIASGUARDIA, b.getTipodiasGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASPERIODO, b.getDiasPeriodo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_TIPODIASPERIODO, b.getTipoDiasPeriodo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_SELECCIONLABORABLES, b.getSeleccionLaborables());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS, b.getSeleccionFestivos());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO, b.getEsViolenciaGenero());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDGUARDIASUSTITUCION, b.getIdGuardiaSustitucion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDTURNOSUSTITUCION, b.getIdTurnoSustitucion());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	} //beanToHashTable ()
		
	/**
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos ()
	{
		return null;
	}
	
	/** 
	 * Funcion ejecutaSelect(String select)
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
	 */
	public Vector ejecutaSelect (String select)
			throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try
		{ 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
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
	} //ejecutaSelect ()
	
	/**
	 * Devuelve los posibles campos que queremos recuperar desde el select
	 * Los posibles valores son los 3 posibles select que se le pedirán a esta clase,
	 * para rellenar las tablas de las páginas "listarGuardias.jsp" y "listarGuardiasTurnos.jsp",
	 * 
	 * Modificado por david.sanchezp para incluir la opcion 4
	 * Modificado por adrian.ayala para incluir nueva configuracion de tipos de dias
	 * 
	 * @param valor int selector de los campos a mostrar
	 * @return String campos que recupera desde la select
	 */
	public String getCamposTabla (int valor)
	{
		String campos = "";
		switch (valor) {
			case 1:
				campos =
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTURNO,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DESCRIPCION+" DESCRIPCION,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" IDINSTITUCION,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS+" OBLIGATORIEDAD,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DURACION,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA+" NUMEROLETRADOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA+" NUMEROSUSTITUTOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASPAGADOS+" DIASPAGADOS,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARJUSTIFICACIONES+" VALIDARJUSTIFICACIONES,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARINSCRIPCIONES+" VALIDARINSCRIPCIONES,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASPERIODO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS;
				break;
				
			case 2:
				campos =
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE+" TURNO,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTURNO,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" IDINSTITUCION,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS+" OBLIGATORIEDAD,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DURACION,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION+" FECHAINSCRIPCION,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHABAJA+" FECHABAJA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASPERIODO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDPERSONA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS;
				break;
				
			case 3:
				campos =
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTURNO,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS+" OBLIGATORIEDAD,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DURACION,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA+" NUMEROLETRADOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA+" NUMEROSUSTITUTOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASPERIODO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS;
				break;
				
			case 4:
				campos =
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASPAGADOS+" DIASACOBRAR,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA+" NUMEROLETRADOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA+" NUMEROSUSTITUTOS, "+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS+" DIASSEPARACIONGUARDIAS, "+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA"+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_ESVIOLENCIAGENERO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS;
				break;
				
			default:
				break;
		}
		 
		return campos;
	} //getCamposTabla ()
	
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la guardia que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)
			throws ClsExceptions 
	{
		RowsContainer rc = null;
		try					{ rc = new RowsContainer(); }
		catch (Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDGUARDIA) + 1) AS IDGUARDIA FROM " + nombreTabla;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDGUARDIA").equals(""))
					entrada.put(ScsGuardiasTurnoBean.C_IDGUARDIA,
							"1");
				else
					entrada.put(ScsGuardiasTurnoBean.C_IDGUARDIA,
							(String)prueba.get("IDGUARDIA"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return entrada;
	} //prepararInsert ()
	
	/**
	 * Hay que cambiar los valores de los campos recogidos en el formulario jsp.
	 * 
	 * @param hash con los datos que vamos a querer meter en la BBDD
	 * @return hash con los datos como deben ir en BBDD
	 * @throws ClsExceptions
	 */
	public Hashtable prepararHash (Hashtable hash)
			throws ClsExceptions
	{
		try{
			String aux = UtilidadesHash.getString(hash,"VALIDARJUSTIFICACIONES");
			if (aux!=null)UtilidadesHash.set(hash,"VALIDARJUSTIFICACIONES","S");
			else UtilidadesHash.set(hash,"VALIDARJUSTIFICACIONES","N");
		}catch(Exception e){
			UtilidadesHash.set(hash,"VALIDARJUSTIFICACIONES","N");
		}
		return hash;
	} //prepararHash ()
	
	/**
	 * Funcion insert (MasterBean bean)
	 * @param hash con los datos a insertar
	 * @return true si todo va bien OK, false si hay algun error 
	 */
	public boolean insert (Hashtable hash)
			throws ClsExceptions
	{
		try {
			return super.insert(this.prepararInsert(this.prepararHash(hash)));
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el 'iNsert' en B.D.");
		}
	} //insert ()
	
	/**
	 * Devuelve la descripcion en texto de la seleccion de tipo de dia
	 * en el idioma del usuario<BR>
	 * La diferencia con el metodo etiquetaTipoDia (), 
	 * es que este último no es estatico
	 * 
	 * @param laborables
	 * @param festivos
	 * @param usr
	 * @return String
	 */
	public static String obtenerTipoDia (String laborables, String festivos, UsrBean usr)
	{
		String seleccionLaborables	= UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionLaborables");
		String seleccionFestivos	= UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionFestivos");
		String L = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Lunes");
		String M = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Martes");
		String X = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Miercoles");
		String J = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Jueves");
		String V = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Viernes");
		String S = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Sabado");
		String D = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Domingo");
		
		String seleccion = "";
		
		//sustituyendo por las letras de cada idioma
		laborables.replaceAll("L", L);
		laborables.replaceAll("M", M);
		laborables.replaceAll("X", X);
		laborables.replaceAll("J", J);
		laborables.replaceAll("V", V);
		laborables.replaceAll("S", S);
		festivos.replaceAll("L", L);
		festivos.replaceAll("M", M);
		festivos.replaceAll("X", X);
		festivos.replaceAll("J", J);
		festivos.replaceAll("V", V);
		festivos.replaceAll("S", S);
		festivos.replaceAll("D", D);
		
		//escribiendo los laborables
		String selTemp = laborables;
		
		if (selTemp.equals(L+M+X+J+V))			seleccion += seleccionLaborables + " " + L + "-" + V;
		else if (selTemp.equals(L+M+X+J+V+S))	seleccion += seleccionLaborables + " " + L + "-" + S;
		else if (! (selTemp.length() == 0))		seleccion += seleccionLaborables + " " + selTemp;
		
		//escribiendo los festivos
		selTemp = festivos;
		
		if (seleccion.length() > 0 && selTemp.length() > 0) seleccion += ", ";
		
		if (selTemp.equals(L+M+X+J+V))			seleccion += seleccionFestivos + " " + L + "-" + V;
		else if (selTemp.equals(L+M+X+J+V+S))	seleccion += seleccionFestivos + " " + L + "-" + S;
		else if (selTemp.equals(L+M+X+J+V+S+D))	seleccion += seleccionFestivos + " " + L + "-" + D;
		else if (! (selTemp.length() == 0))		seleccion += seleccionFestivos + " " + selTemp;
		
		//mostrando en pantalla
		return seleccion;
	} //obtenerTipoDia ()
	
	/**
	 * Devuelve un String con los campos por los que debe ser ordenado el listado de los letrados inscritos a la guardia
	 * 
	 * Depende del valor del idOrdenacionColas 
	 * @param int idOrdenacion: int don el identificador de la ordenación de cola para esa guardia 
	 */
	public String getOrdenacionLetradosInscritos (int idOrdenacion)
			throws ClsExceptions
	{
		String resultado="";
		ScsOrdenacionColasAdm colaAdm = new ScsOrdenacionColasAdm(this.usrbean);
		Hashtable hash = new Hashtable();
		hash.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,String.valueOf(idOrdenacion));
		try{
			ScsOrdenacionColasBean cola = (ScsOrdenacionColasBean)((Vector)colaAdm.select(hash)).get(0);
			int maximo=0;
			int elegido=0;
			for (int cont=1; cont<=4; cont++){
				maximo = 0;
				elegido = 0;
				if (maximo<Math.abs(cola.getAlfabeticoApellidos().intValue())){
					elegido=1;
					maximo = Math.abs(cola.getAlfabeticoApellidos().intValue());
				}
				if (maximo < Math.abs(cola.getFechaNacimiento().intValue())){
					elegido=2;
					maximo = Math.abs(cola.getFechaNacimiento().intValue());
				}
				if (maximo < Math.abs(cola.getAntiguedadCola().intValue())){
					elegido =3;
					maximo = Math.abs(cola.getAntiguedadCola().intValue());
				}
				if (maximo < Math.abs(cola.getNumeroColegiado().intValue())){
					elegido = 4;
					maximo = Math.abs(cola.getNumeroColegiado().intValue());
				}
				switch (elegido){
					case 1: if (cont>1)resultado+=",B.ALFABETICOAPELLIDOS";
							else resultado+="B.ALFABETICOAPELLIDOS";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					case 2: if (cont>1)resultado+=",B.FECHANACIMIENTO";
							else resultado+="B.FECHANACIMIENTO";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					case 3: if (cont>1)resultado+=",B.ANTIGUEDADCOLA";
							else resultado+="B.ANTIGUEDADCOLA";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					case 4: if (cont>1)resultado+=",B.NUMEROCOLEGIADO";
							else resultado+="B.NUMEROCOLEGIADO";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					default:break; 
				}
			}
			
		}catch(Exception e){
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return resultado;
	} //getOrdenacionLetradosInscritos ()

	/** 
	 * Devuelve un String con la consulta SQL que devuelve 1 registro que contiene los datos: Tipo Dias, Nº Letrados de<br>
	 * Guardia, Nº Letrados de Reserva y los Días de Guardia.  
	 * el IDCALENDARIOGUARDIAS.
	 * 
	 * @param String idinstitucion_pestanha de la pestanha
	 * @param String idturno_pestanha de la pestanha
	 * @param String idguardia_pestanha de la pestanha
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getDatosCalendario (String idinstitucion_pestanha,
									  String idturno_pestanha,
									  String idguardia_pestanha)
			throws ClsExceptions
	{
		String consulta = "";
		
		try {
			consulta = "SELECT "+this.getCamposTabla(4);
			consulta += " FROM "+ScsGuardiasTurnoBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idinstitucion_pestanha;
			consulta += " AND "+ScsGuardiasTurnoBean.C_IDTURNO+"="+idturno_pestanha;
			consulta += " AND "+ScsGuardiasTurnoBean.C_IDGUARDIA+"="+idguardia_pestanha;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en " +
					"ScsGuardiasTurnoAdm.getDatosCalendario(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	} //getDatosCalendario ()
	
	/** 
	 * Recoge la informacion necesaria para formalizar un informe sobre listas de guardias <br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno	 	  
	 * @param  guardia - identificador de la guardia
	 * @param  fechaInicio - fecha de inicio del periodo
	 * @param  fechaFin - fecha de finalizacion del periodo
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosListaGuardias (String institucion,String idlista,
										 Vector guardias,
										 String fechaInicio,
										 String fechaFin)
			throws ClsExceptions,SIGAException
	{
		   Vector datos=new Vector();
		   String idcalendarioguardias="",  idturno="", idguardia="", idpersona="";
		   HelperInformesAdm helperInformes = new HelperInformesAdm();
		   TreeMap tmListaGuardias = new TreeMap();
	       try {
	            RowsContainer rc = new RowsContainer();
	            
	            
	            	            
	            /*String sql =" SELECT GUARDIA, LETRADO, MAX(OFICINA1), MAX(OFICINA2), MAX(MOVIL), MAX(FAX1), FECHA_FIN" +*/
	            /** Cambio realizado por PDM, se cambia el campo de salida FECHA_FIN por FECHA_INICIO para que asi se
	             * ordene por el y muestre la misma informacion que cuando se accede a las guardias a traves del 
	             * calendario INC-02348
	             */
	            
	            Hashtable codigos = new Hashtable();
			    int contador=0;
				
	            
	            String sql =" SELECT " +ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+ " AS IDGUARDIA, "+
	                        "(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
									CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || " +
									CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") AS LETRADO," +
					    			ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIA," +
	                                " guardias2.idpersona IDPERSONA,  guardias2.idcalendarioguardias IDCALENDARIOGUARDIAS,  "+ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE+"	 TURNO, "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDTURNO+" IDTURNO, " +
	            					" decode(cen_colegiado.ncolegiado,null,cen_colegiado.ncomunitario,cen_colegiado.ncolegiado) NCOLEGIADO, " ;
	            //guardias2.fechainicio FECHA_INICIO, guardias2.fecha_fin FECHA_FIN,
					    			contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					//contador++;
	            					//codigos.put(new Integer(contador),new Integer(com.atos.utils.ClsConstants.TIPO_DIRECCION_GUARDIA).toString());
	            					sql += "f_siga_getdireccioncliente(:"+contador+", guardias2.idpersona,6,11) AS OFICINA1, ";

					    			contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					sql += " f_siga_getdireccioncliente(:"+contador+", guardias2.idpersona, 6,12) AS OFICINA2, ";
	            					contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					sql += " f_siga_getdireccioncliente(:"+contador+", guardias2.idpersona, 1,11) AS RESIDENCIA, ";
					    			contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					sql += " f_siga_getdireccioncliente(:"+contador+", guardias2.idpersona,6,13) AS MOVIL, ";
					    			contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					sql += " f_siga_getdireccioncliente(:"+contador+", guardias2.idpersona,6,14) AS FAX1, ";
					    			contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					sql +=" f_siga_getdireccioncliente(:"+contador+", guardias2.idpersona, 6,15) AS FAX2, " ;
	            					contador++;
	            					codigos.put(new Integer(contador),institucion);
	            				
	            		     sql += "decode(F_SIGA_FECHAINISOLICITANTE(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
         					contador++;
        					codigos.put(new Integer(contador),institucion);
        					
        					sql += " '', decode(F_SIGA_FECHAINICONFIRMADOR(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
        					
        					sql += " '', guardias2.fechainicio, F_SIGA_FECHAINICONFIRMADOR(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
        					
        					sql += " F_SIGA_FECHAINISOLICITANTE(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)) AS FECHA_INICIO, ";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
							sql += " to_char(decode(F_SIGA_FECHAINISOLICITANTE(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
         					contador++;
        					codigos.put(new Integer(contador),institucion);
        					sql += " '', decode(F_SIGA_FECHAINICONFIRMADOR(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
        					sql += " '', guardias2.fechainicio, F_SIGA_FECHAINICONFIRMADOR(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
        					sql += " F_SIGA_FECHAINISOLICITANTE(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)), 'Day') AS DIA_FECHA_INICIO, ";
        					sql+=	ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE+"	 TURNO, ";
							
        					contador++;
        					codigos.put(new Integer(contador),institucion);
	            					
	            		     sql += "decode(F_SIGA_FECHAFINSOLICITANTE(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
         					contador++;
        					codigos.put(new Integer(contador),institucion);
        					
        					sql += " '', decode(F_SIGA_FECHAFINCONFIRMADOR(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
        					
        					sql += " '', guardias2.fecha_fin, F_SIGA_FECHAFINCONFIRMADOR(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
        					contador++;
        					codigos.put(new Integer(contador),institucion);
        					
        					sql += " F_SIGA_FECHAFINSOLICITANTE(:"+contador+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO+","+ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA+","+
	                                         "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)) AS FECHA_FIN,  SCS_INCLUSIONGUARDIASENLISTAS.ORDEN";	
											
					   
	                                         
							sql+=		" FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + "," +  
											  	   ScsCabeceraGuardiasBean.T_NOMBRETABLA + " guardias2, " +ScsTurnoBean.T_NOMBRETABLA+ 
											  	   ", "+CenColegiadoBean.T_NOMBRETABLA+ ","+ ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+
											   
									" WHERE " +
									ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDINSTITUCION + "=guardias2." +ScsCabeceraGuardiasBean.C_IDINSTITUCION +
									" AND " +
									ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDTURNO + "=guardias2." + ScsCabeceraGuardiasBean.C_IDTURNO +
									" AND " +
									ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDGUARDIA +
									" AND "+
									" guardias2."+ScsCabeceraGuardiasBean.C_IDINSTITUCION + "=" +ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDINSTITUCION+
									 " AND "+
									 " guardias2."+ScsCabeceraGuardiasBean.C_IDTURNO + "=" +ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDTURNO+
									
									" AND " +
									CenColegiadoBean.T_NOMBRETABLA+"."+ CenColegiadoBean.C_IDPERSONA+"="+ CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA +
									" AND "+CenColegiadoBean.T_NOMBRETABLA+"."+ CenColegiadoBean.C_IDINSTITUCION+"=guardias2."+ ScsCabeceraGuardiasBean.C_IDINSTITUCION +
									" AND "+CenColegiadoBean.T_NOMBRETABLA+"."+ CenColegiadoBean.C_IDPERSONA+"=guardias2."+ ScsCabeceraGuardiasBean.C_IDPERSONA +
									" AND " ;
	            					contador++;
	            					codigos.put(new Integer(contador),fechaInicio);
	            					
	            		     sql += "((guardias2."+ ScsCabeceraGuardiasBean.C_FECHA_INICIO+" >= TO_DATE(:" + contador+ ",'DD/MM/YYYY')" +
											" AND " ;
         					contador++;
        					codigos.put(new Integer(contador),fechaFin);
        					
        					sql += "guardias2."+ ScsCabeceraGuardiasBean.C_FECHA_INICIO+" < TO_DATE(:" + contador+ ",'DD/MM/YYYY')+1)" +
										" OR " ;
        					contador++;
        					codigos.put(new Integer(contador),fechaInicio);
        					
        					sql += "(guardias2."+ ScsCabeceraGuardiasBean.C_FECHA_INICIO+" < TO_DATE(:" + contador + ",'DD/MM/YYYY')+1" +
											" AND " ;
        					contador++;
        					codigos.put(new Integer(contador),fechaInicio);
        					
        					sql += "guardias2."+ ScsCabeceraGuardiasBean.C_FECHA_FIN+" >= TO_DATE(:" + contador + ",'DD/MM/YYYY')))" +							
									" AND " ;
	            					contador++;
	            					codigos.put(new Integer(contador),institucion);
	            					
	            		     sql += ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDINSTITUCION + "=:" + contador;
									
	            		    	
	         	            if (guardias!=null && guardias.size()>0) {
	         	               sql += " AND " +
								"("+ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDTURNO +","+ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDGUARDIA +") in (";
	         	               String aux = "";
	         	               for (int i=0;i<guardias.size();i++) {
	         	                    Vector v = (Vector) guardias.get(i);
	         	                    contador++;
	            					codigos.put(new Integer(contador),(String)v.get(0));
	         	                    aux += "(:"+contador;
	         	                    contador++;
	            					codigos.put(new Integer(contador),(String)v.get(1));
	         	                    aux += ",:"+contador+"),";
	         	               }
	         	               if (aux.length()>0) {
	         	                   aux = aux.substring(0,aux.length()-1);
	         	               }
	         	               sql += aux + ")";
	         	            }
	         	            sql += " AND " +ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDINSTITUCION +"="+ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+"."+ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION+
	         	             " AND " +ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDTURNO+"="+ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+"."+ScsInclusionGuardiasEnListasBean.C_IDTURNO+
	         	             " AND " +ScsGuardiasTurnoBean.T_NOMBRETABLA +"."+ ScsGuardiasTurnoBean.C_IDGUARDIA+"="+ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+"."+ScsInclusionGuardiasEnListasBean.C_IDGUARDIA+	         	          
	         	             " AND SCS_INCLUSIONGUARDIASENLISTAS.IDLISTA ="+idlista;
	         						
							/*  sql += "GROUP BY TURNO, GUARDIA, LETRADO, FECHA_INICIO,FECHA_FIN" +
							  		",OFICINA1, OFICINA2, RESIDENCIA, MOVIL,  FAX1,  FAX2 " +
							  " ORDER BY FECHA_INICIO,FECHA_FIN, TURNO, GUARDIA, LETRADO";*/
							
			    if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	
	                   
						idturno = (String)resultado.get("IDTURNO");
						String turno = (String)resultado.get("TURNO");
						String guardia = (String)resultado.get("GUARDIA");
						String letrado = (String)resultado.get("LETRADO");
						idguardia = (String)resultado.get(ScsGuardiasTurnoBean.C_IDGUARDIA);
						idpersona = (String)resultado.get("IDPERSONA");
						idcalendarioguardias = (String)resultado.get("IDCALENDARIOGUARDIAS");
						String orden = (String)resultado.get("ORDEN"); 
						String fechaInicioPK = (String)resultado.get("FECHA_INICIO");
						String fechaFinPK = (String)resultado.get("FECHA_FIN");

						/*Hashtable htCodigoDireccion = new Hashtable();
						htCodigoDireccion.put(new Integer(1), institucion);
						htCodigoDireccion.put(new Integer(2), idpersona);
						htCodigoDireccion.put(new Integer(3), "6");
						htCodigoDireccion.put(new Integer(4), "11");
						
						
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigoDireccion, "f_siga_getdireccioncliente", "OFICINA1"));
						
						htCodigoDireccion = new Hashtable();
						htCodigoDireccion.put(new Integer(1), institucion);
						htCodigoDireccion.put(new Integer(2), idpersona);
						htCodigoDireccion.put(new Integer(3), "6");
						htCodigoDireccion.put(new Integer(4), "12");
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigoDireccion, "f_siga_getdireccioncliente", "OFICINA2"));
						
						htCodigoDireccion = new Hashtable();
						htCodigoDireccion.put(new Integer(1), institucion);
						htCodigoDireccion.put(new Integer(2), idpersona);
						htCodigoDireccion.put(new Integer(3), "1");
						htCodigoDireccion.put(new Integer(4), "11");
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigoDireccion, "f_siga_getdireccioncliente", "RESIDENCIA"));
						
						htCodigoDireccion = new Hashtable();
						htCodigoDireccion.put(new Integer(1), institucion);
						htCodigoDireccion.put(new Integer(2), idpersona);
						htCodigoDireccion.put(new Integer(3), "6");
						htCodigoDireccion.put(new Integer(4), "13");
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigoDireccion, "f_siga_getdireccioncliente", "MOVIL"));
						
						htCodigoDireccion = new Hashtable();
						htCodigoDireccion.put(new Integer(1), institucion);
						htCodigoDireccion.put(new Integer(2), idpersona);
						htCodigoDireccion.put(new Integer(3), "6");
						htCodigoDireccion.put(new Integer(4), "14");
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigoDireccion, "f_siga_getdireccioncliente", "FAX1"));
						
						htCodigoDireccion = new Hashtable();
						htCodigoDireccion.put(new Integer(1), institucion);
						htCodigoDireccion.put(new Integer(2), idpersona);
						htCodigoDireccion.put(new Integer(3), "6");
						htCodigoDireccion.put(new Integer(4), "15");
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigoDireccion, "f_siga_getdireccioncliente", "FAX2"));*/
						
						/*Hashtable htCodigo = new Hashtable();
						htCodigo.put(new Integer(1), institucion);
						htCodigo.put(new Integer(2), idturno);
						htCodigo.put(new Integer(3), idguardia);
						htCodigo.put(new Integer(4), idpersona);
						htCodigo.put(new Integer(5), fechaInicio);
						htCodigo.put(new Integer(6), idcalendarioguardias); 
						
						//FECHAINICIO
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAINISOLICITANTE", "FECHA_INICIO"));
						String fInicio = (String)resultado.get("FECHA_INICIO");
						//Si la fecha de inicio del solicitante es nula miramos  la fecha de inicio del confirmador
						if(fInicio==null||fInicio.trim().equals("")){
							helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
									htCodigo, "F_SIGA_FECHAINICONFIRMADOR", "FECHA_INICIO"));
							fInicio = (String)resultado.get("FECHA_INICIO");
							//Si la fecha de inicio del confirmador es nula ponemos como fecha de inicio de la permuta 
							//la fecha de inicio real
							if(fInicio==null||fInicio.trim().equals("")){
								fInicio = fechaInicioPK;
								
							}
							
						}
						resultado.put("FECHA_INICIO", fInicio);
						//FECHAFINPERMUTA
						helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAFINSOLICITANTE", "FECHA_FIN"));
						String fFin = (String)resultado.get("FECHA_FIN");
						//Si la fecha de fin del solicitante es nula miramos  la fecha de fin del confirmador
						if(fFin==null||fFin.trim().equals("")){
							helperInformes.completarHashSalida(resultado,helperInformes.ejecutaFuncionSalida(
									htCodigo, "F_SIGA_FECHAFINCONFIRMADOR", "FECHA_FIN"));
							fFin = (String)resultado.get("FECHAFINPERMUTA");
							//Si la fecha de fin del confirmador es nula ponemos como fecha de fin de la permuta 
							//la fecha de fin real
							if(fFin==null||fFin.trim().equals("")){
								fFin = fechaFinPK;
								
							}
							
						}
						resultado.put("FECHA_FIN", fFin);*/
				  
						
						String keyTreeMap = fechaInicioPK+orden+turno+guardia+letrado.toLowerCase();
						tmListaGuardias.put(keyTreeMap, resultado);
													                 
	               }
	               Iterator iteLista = tmListaGuardias.keySet().iterator();
					while (iteLista.hasNext()) {
						String key = (String) iteLista.next();
		        		Hashtable listaGuardiasOrdenadas = (Hashtable) tmListaGuardias.get(key);
		        		datos.add(listaGuardiasOrdenadas);
					}
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre listas de guardias.");
	       }
	       return datos;                        
	} //getDatosListaGuardias ()
	
	public Vector getDatosPlantillas (String idinstitucion,
									  String anio,
									  String idturno,
									  String numero,
									  String lenguaje, boolean isASolicitantes,String idPersonaJG, String codigo)
			throws ClsExceptions,SIGAException
	{
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
			    
	            Hashtable codigos = new Hashtable();
			    int contador=0;
			    
	            String query = "SELECT  DISTINCT COL.NCOLEGIADO AS NCOLEGIADO_LETRADO," +
	            		"         COL.NOMBRE || ' ' || COL.APELLIDOS1 || ' ' || COL.APELLIDOS2 AS NOMBRE_LETRADO," +
	            		"         DECODE(COL.SEXO,null,null,'M'," ;
    					contador++;
    					codigos.put(new Integer(contador),lenguaje);
    					
    			    query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:"+contador+")," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
					query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:"+contador+")) AS SEXO_LETRADO," +
//	            		//"         COL.SEXO AS SEXO_LETRADO," +
//	            		"         DIR2.DOMICILIO AS DOMICILIO_LETRADO," +
//	            		"         DIR2.CODIGOPOSTAL AS CP_LETRADO," +
//	            		"         DIR2.NOMBRE_PROBLACION AS POBLACION_LETRADO," +
//	            		"         DIR2.TELEFONO1 AS TELEFONODESPACHO_LET," +
//	            		"         DIR2.CORREOELECTRONICO AS EMAIL_LETRADO," +
//	            		"         DIR2.FAX1 AS FAX_LETRADO," +
//	            		"         DIR6.TELEFONO1 AS TELEFONO1_LETRADO," +
//	            		"         DIR6.TELEFONO2 AS TELEFONO2_LETRADO," +
//	            		"         DIR6.MOVIL AS MOVIL_LETRADO," +
	            		//"         COL.SEXO AS SEXO_LETRADO," +
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,1)  AS DOMICILIO_LETRADO,"+
	            		"	      f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,2)  AS CP_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,3)  AS POBLACION_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,4)  AS PROVINCIA_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,11)  AS TELEFONODESPACHO_LET,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,16)  AS EMAIL_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,14)  AS FAX_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,6,11)  AS TELEFONO1_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,6,12)  AS TELEFONO2_LETRADO,"+
	            		"		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,6,13)  AS MOVIL_LETRADO,"+
	            		"         DES.OBSERVACIONES AS OBSERVACIONES," +
	            		"         DES.NUMPROCEDIMIENTO AS AUTOS," +
	            		"         TO_CHAR(DES.FECHAJUICIO, 'dd/MM/yyyy') AS FECHA_JUICIO," +
	            		"         TO_CHAR(DES.FECHAJUICIO, 'HH24:MM') AS HORA_JUICIO," +
	            		"         JUZ.NOMBRE AS JUZGADO," +
	            		"         JUZ.DOMICILIO AS DIR_JUZGADO," +
	            		"         JUZ.CODIGOPOSTAL AS CP_JUZGADO," +
	            		"         JUZ.POBLACION_NOMBRE AS POBLACION_JUZGADO," +
	            		"         F_SIGA_GETCONTRARIOS_DESIGNA(DES.IDINSTITUCION," +
	            		"                                      DES.IDTURNO," +
	            		"                                      DES.ANIO," +
	            		"                                      DES.NUMERO) AS CONTRARIOS," +
	            		"         F_SIGA_GETPROCURADORCONT_DESIG(DES.IDINSTITUCION," +
	            		"                                        DES.IDTURNO," +
	            		"                                        DES.ANIO," +
	            		"                                        DES.NUMERO) AS PROCURADOR_CONTRARIOS," +
	            		"         DES.ANIO AS ANIO_DESIGNA," +
	            		"         DES.CODIGO AS CODIGO," +
	            		"         DES.RESUMENASUNTO AS ASUNTO," +
	            		"         DES.ANIO || '/' || DES.CODIGO AS NOFICIO," +
	            		"         PROC.NOMBRE || ' ' || PROC.APELLIDOS1 || ' ' || PROC.APELLIDOS2 AS PROCURADOR," +
	            		"         (select POB.NOMBRE "+
	            		"	       from cen_poblaciones pob "+
	            		"	       where pob.idpoblacion=PROC.IDPOBLACION) AS POBLACION_PROCURADOR, "+
	            		"	       (select prov.NOMBRE "+
	            		"	       from cen_provincias  prov "+
	            		"	       where prov.idprovincia=PROC.IDPROVINCIA) AS PROVINCIA_PROCURADOR, "+
	            		"	      PROC.DOMICILIO AS DOMICILIO_PROCURADOR, "+
	            		"	      PROC.CODIGOPOSTAL AS CP_PROCURADOR, "+
	            		"	      PROC.TELEFONO1 AS TELEFONO1_PROCURADOR, ";
    					contador++;
    					codigos.put(new Integer(contador),lenguaje);
    					
    		     query += "         F_SIGA_GETDELITOS_DESIGNA(DES.IDINSTITUCION, DES.NUMERO, DES.IDTURNO, DES.ANIO,:"+contador+") AS DELITOS," +
	            		"         TO_CHAR(DES.FECHAENTRADA, 'dd-mm-yyyy') AS FECHA_DESIGNA," +
	            		"         DES.TURNO_DESCRIPCION AS DESCRIPCION_TURNO," +
	            		"		  DES.ABREV_TURNO AS ABREV_TURNO, "+
	            		"         F_SIGA_NOMBRE_PARTIDO(DES.IDTURNO,DES.IDINSTITUCION) as NOMBRE_PARTIDO," +
	            		
	            		//" 		  DES.NOMBRE_PROCEDIMIENTO AS PROCEDIMIENTO,";
	            		
	            		/*"         DECODE(ACT.ANIO,NULL,NULL,ACT.ANIO || '/' || ACT.NUMERO || '/' || ACT.NUMEROASUNTO) AS NACTUACION," +
	            		"         DECODE(ACT.NOMBRE_PROCEDIMIENTO, NULL, '', ACT.NOMBRE_PROCEDIMIENTO || ' (' || ACT.PORCENTAJE_ACREDITACION || '%)') AS PROCEDIMIENTO," +
	            		"         TO_CHAR(ACT.FECHA, 'dd-mm-yyyy') AS FECHA_ACTUACION," +
	            		"         TO_CHAR(ACT.FECHA, 'hh:MM:ss') AS HORA_ACTUACION," +
	            		"         DECODE(ACT.IDPRISION, NULL, (SELECT J.NOMBRE" +
	            		"                                       FROM SCS_JUZGADO J" +
	            		"                                       WHERE J.IDINSTITUCION = DES.IDINSTITUCION_JUZG" +
	            		"                                         AND J.IDJUZGADO = DES.IDJUZGADO)," +
	            		"                                     (SELECT P.NOMBRE" +
	            		"                                        FROM SCS_PRISION P" +
	            		"                                       WHERE P.IDINSTITUCION = ACT.IDINSTITUCION_PRIS" +
	            		"                                         AND P.IDPRISION = ACT.IDPRISION)) AS LUGAR," ;
	            		*/
	            		" (select (DECODE(VACT1.ANIO, NULL, null, " +
	            		" VACT1.ANIO || '/' || VACT1.NUMERO || '/' || " +
	            		" VACT1.NUMEROASUNTO)) " +
	            		" from  V_SIGA_ACTUACION_DESIGNA VACT1 " +
	            		"  where DES.IDINSTITUCION = VACT1.IDINSTITUCION " +
	            		" AND DES.IDTURNO = VACT1.IDTURNO " +
	            		" AND DES.ANIO = VACT1.ANIO " +
	            		"     AND DES.NUMERO = VACT1.NUMERO " +
	            		"        AND ROWNUM < 2) AS NACTUACION, " +
	            		" nvl((select VACT2.NOMBRE_PROCEDIMIENTO " +
	                              
	            		"      from V_SIGA_ACTUACION_DESIGNA VACT2 " +
	            		"       where DES.IDINSTITUCION = VACT2.IDINSTITUCION " +
	            		"            AND DES.IDTURNO = VACT2.IDTURNO " +
	            		"            AND DES.ANIO = VACT2.ANIO " +
	            		"           AND DES.NUMERO = VACT2.NUMERO " +
	            		"            AND ROWNUM < 2),DES.NOMBRE_PROCEDIMIENTO) AS PROCEDIMIENTO, " +
	            		"        (select TO_CHAR(VACT3.FECHA, 'dd-mm-yyyy') " +
	            		"            from V_SIGA_ACTUACION_DESIGNA VACT3 " +
	            		"            where DES.IDINSTITUCION = VACT3.IDINSTITUCION " +
	            		"              AND DES.IDTURNO = VACT3.IDTURNO " +
	            		"              AND DES.ANIO = VACT3.ANIO " +
	            		"              AND DES.NUMERO = VACT3.NUMERO " +
	            		"                AND ROWNUM <2 ) AS FECHA_ACTUACION, " +
	            		"          (select DECODE(vact3.IDPRISION, " +
	            		"                        NULL, " +
	                           "                        (SELECT J.NOMBRE " +
	            		"                            FROM SCS_JUZGADO J " +
	                              "                           WHERE J.IDINSTITUCION = DES.IDINSTITUCION_JUZG " +
	                             "                            AND J.IDJUZGADO = DES.IDJUZGADO), " +
	            		"                           (SELECT P.NOMBRE " +
	                        		   "                           FROM SCS_PRISION P " +
	            		"                       WHERE P.IDINSTITUCION = vact3.IDINSTITUCION_PRIS " +
	            		"                           AND P.IDPRISION = vact3.IDPRISION)) " +
	            		"             from V_SIGA_ACTUACION_DESIGNA vact3 " +
	            		"           where DES.IDINSTITUCION = vact3.IDINSTITUCION " +
	            		"       AND DES.IDTURNO = vact3.IDTURNO " +
	            		"         AND DES.ANIO = vact3.ANIO " +
	            		"           AND DES.NUMERO = vact3.NUMERO " +
	            		"           AND ROWNUM < 2) AS LUGAR,";
	            		
	            		
	            		
	            		//if(isASolicitantes){
	            			//query += "         DECODE(INTERESADO.ANIOEJG,NULL,NULL,INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG) AS NUMERO_EJG," +
	            query += " DECODE( " +
	            	"       (select count(EJGDES1.idinstitucion) from SCS_EJGDESIGNA EJGDES1 where EJGDES1.IDINSTITUCION = DES.IDINSTITUCION " +
	            	"             and EJGDES1.ANIODESIGNA = DES.ANIO " +
	            	"            and EJGDES1.IDTURNO = DES.IDTURNO " +
	            	"             and EJGDES1.NUMERODESIGNA = DES.NUMERO), " +
	            	"       '1', " +
	            	"        (select EJGDES.ANIOEJG || '/' || ejg.NUMEJG from scs_ejg ejg,scs_ejgdesigna ejgdes " +
	            	"               where ejg.anio =ejgdes.anioejg " +
	            	"            and ejg.numero = ejgdes.numeroejg " +
	            	"            and ejg.idinstitucion = ejgdes.idinstitucion " +
	            	"             and ejg.idtipoejg = ejgdes.idtipoejg " +
	            	"             AND ejgdes.IDINSTITUCION = DES.IDINSTITUCION " +
	            	"             and ejgdes.ANIODESIGNA = DES.ANIO " +
	            	"            and ejgdes.IDTURNO = DES.IDTURNO " +
	            	"             and ejgdes.NUMERODESIGNA = DES.NUMERO), " +
	                    
	                    
	             
	             
	            	"    DECODE(INTERESADO.ANIOEJG,NULL,'0',INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG)) " +
	            	"     AS NUMERO_EJG, " +
	            
	            
	            		"         INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' || INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO, " +
	            		"         INTERESADO.DIRECCION AS DOMICILIO_DEFENDIDO," +
	            		"         INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO," +
	            		"         INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO, " +
	            		"         INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO," +
	            		"         INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO," +
	            		"         DECODE(INTERESADO.SEXO,null,null,'M'," ;
    					contador++;
    					codigos.put(new Integer(contador),lenguaje);
    					
    		     query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:"+contador+")," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
				 query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:"+contador+")) AS SEXO_DEFENDIDO," +

	            		
	           // 		"         INTERESADO.SEXO AS SEXO_DEFENDIDO," +
	            		"         INTERESADO.IDLENGUAJE AS IDLENGUAJE_DEFENDIDO," +
	            		"         DECODE(INTERESADO.CALIDAD,null,'','D'," ;
    					contador++;
    					codigos.put(new Integer(contador),lenguaje);
    					
    		     query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandante',:"+contador+")," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
		         query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandado',:"+contador+")) AS CALIDAD_DEFENDIDO," +
	            		"         INTERESADO.OBSERVACIONES AS OBS_INTERESADO, "+
	            		"		  F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE, ";
        		
	            	//	}else{
	            			query += "F_SIGA_GETINTERESADOSDESIGNA(DES.IDINSTITUCION,DES.ANIO,DES.IDTURNO,DES.NUMERO,0) LISTA_INTERESADOS_DESIGNA,";
	            			
	            			query += "F_SIGA_GETACTUACIONESDESIGNA(DES.IDINSTITUCION,DES.ANIO,DES.IDTURNO,DES.NUMERO) LISTA_ACTUACIONES_DESIGNA,";
	            		//}
	            		query +=" TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL," +
	            		"         TO_CHAR(SYSDATE, 'MONTH', 'NLS_DATE_LANGUAGE = SPANISH') AS MES_ACTUAL," +
	            		"         TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL," +
	            		
						"         EJGD.FECHARESOLUCIONCAJG AS FECHARESOLUCIONCAJG "+
						",   F_SIGA_GETFIRSTASISDESIGNA(DES.IDINSTITUCION,DES.ANIO, DES.IDTURNO, DES.NUMERO) AS FECHA_ASISTENCIA "+
						
	            		"   FROM V_SIGA_DESIGNACIONES DES," +
	            		"        V_SIGA_EJG_DESIGNA EJGD," +
	            		"        V_SIGA_DATOSLETRADO_COLEGIADO COL," +
// en version	            		"        V_SIGA_DIRECCIONES_OFICIO DIR2," +
//	            		"        V_SIGA_DIRECCIONES_OFICIO DIR6," +
//	            		"        V_SIGA_JUZGADOS JUZ," ;
//	            		//if(isASolicitantes){
//	            			query +="        V_SIGA_INTERESADOS_DESIGNA INTERESADO," ;
//	            		//}
//	            		query +="        V_SIGA_PROCURADOR_DESIGNA PROC" +
//	            	//	"        ,V_SIGA_ACTUACION_DESIGNA ACT" +
	            		"        V_SIGA_JUZGADOS JUZ," +
	            		"        V_SIGA_INTERESADOS_DESIGNA INTERESADO," +
	            		"        V_SIGA_PROCURADOR_DESIGNA PROC" +
	            		"  WHERE DES.IDINSTITUCION = COL.IDINSTITUCION(+)" +
	            		"    AND DES.IDPERSONA_DESIGNADA = COL.IDPERSONA(+)" +
	            		"    AND DES.IDINSTITUCION = EJGD.IDINSTITUCION (+)" +
	            		"    AND DES.IDTURNO = EJGD.IDTURNO_DES (+)" +
	            		"    AND DES.ANIO = EJGD.ANIO_DES (+)" +
	            		"    AND DES.NUMERO = EJGD.NUMERO_DES (+)" +
	            		"    AND DES.IDINSTITUCION_JUZG = JUZ.IDINSTITUCION (+)" +
	            		"    AND DES.IDJUZGADO = JUZ.IDJUZGADO (+)" ;
	            		//if(isASolicitantes){
	            			query +="    AND DES.IDINSTITUCION = INTERESADO.IDINSTITUCION (+)" +
	            		"    AND DES.IDTURNO = INTERESADO.IDTURNO (+)" +
	            		"    AND DES.ANIO = INTERESADO.ANIO (+)" +
	            		"    AND DES.NUMERO = INTERESADO.NUMERO (+)" ;
	            		//}
	            		query +="    AND DES.IDINSTITUCION = PROC.IDINSTITUCION (+)" +
	            		"    AND DES.IDTURNO = PROC.IDTURNO (+)" +
	            		"    AND DES.ANIO = PROC.ANIO (+)" +
	            		"    AND DES.NUMERO = PROC.NUMERO (+)" ;
    					contador++;
    					codigos.put(new Integer(contador),idinstitucion);
    					
    		     query += "   and des.IDINSTITUCION =:"+contador+"" ;

    		     // rgg CAMBIO DESESPERADO
    		     contador++;
				 codigos.put(new Integer(contador),codigo);
					
				 query += "   and des.CODIGO =:"+contador+"" ;

    		     
//    		     contador++;
//					codigos.put(new Integer(contador),anio);
//					
//				query += "   and des.ANIO =:"+contador+"" ;
//				contador++;
//				codigos.put(new Integer(contador),idturno);
//				
//				query += "   and des.IDTURNO =:"+contador+"" ;
//				contador++;
//				codigos.put(new Integer(contador),numero);
//				
//				query += "   and des.NUMERO =:"+contador+"" ;

				if(idPersonaJG!=null && !idPersonaJG.equalsIgnoreCase("")){
	            		    
	    					contador++;
	    					codigos.put(new Integer(contador),idPersonaJG);
	    					query +="   and INTERESADO.IDPERSONAJG =:"+contador+"" ;
	            			
	            }
	            		/*"  ORDER BY DES.IDTURNO," +
	            		"           DES.ANIO," +
	            		"           DES.NUMERO," +
	            		"           DES.IDPERSONA_DESIGNADA"*/
	           

	    		//Hashtable htBind=new Hashtable();  
	    		//htBind.put(new Integer(1),lenguaje.toString());	  
	    		//htBind.put(new Integer(2),lenguaje.toString());
	    		//htBind.put(new Integer(3),lenguaje.toString());	 
	    		//htBind.put(new Integer(1),idinstitucion);	  
	    		//htBind.put(new Integer(2),idturno);	
	    		//htBind.put(new Integer(3),anio);	  
	    		//htBind.put(new Integer(4),numero);	  

	    		if (rc.findBind(query,codigos)) {
	    			for (int i = 0; i < rc.size(); i++){
	    				Row fila = (Row) rc.get(i);
	    				Hashtable resultado=fila.getRow();
	    				datos.add(resultado);
	    				if(!isASolicitantes){
	    					break;
	    				}
	    					
	    			}
    			} 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, " Error al obtener la informacion sobre listas de guardias.");
	       }
	       return datos;                        
	} //getDatosPlantillas ()
	
	/**
	 * Efectúa un SELECT en la tabla SCS_TURNO con los datos introducidos. 
	 * 
	 * @param sql. Consulta a realizar
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectLetradosEnCola (String institucion, String turno, String guardia)
	{
		Vector vResult = null;
		try
		{
			//Ejecucion del PL
			String [] resultadoPl = EjecucionPLs.ejecutarPL_OrdenaColegiadosGuardia (
					Integer.valueOf(institucion), Integer.valueOf(turno),
					Integer.valueOf(guardia), new Integer(0));
			//Resultado del PL
			String contador = resultadoPl[0];
			
			//Consulta en la tabla temporal la posicion para el letrado
			String consultaTemp =
				"select T."+GenClientesTemporalBean.C_IDPERSONA+", " +
				"       decode(C."+CenColegiadoBean.C_COMUNITARIO+", '"+ClsConstants.DB_TRUE+"', " +
				"              C."+CenColegiadoBean.C_NCOMUNITARIO+", " +
				"              C."+CenColegiadoBean.C_NCOLEGIADO+") " + CenColegiadoBean.C_NCOLEGIADO+", " +
				"       P."+CenPersonaBean.C_IDPERSONA+", " +
				"       P."+CenPersonaBean.C_NOMBRE+", " +
				"       P."+CenPersonaBean.C_APELLIDOS1+", " +
				"       P."+CenPersonaBean.C_APELLIDOS2+" " +
				"  from "+GenClientesTemporalBean.T_NOMBRETABLA+" T, " +
				"       "+CenColegiadoBean.T_NOMBRETABLA+" C, " +
				"       "+CenPersonaBean.T_NOMBRETABLA+" P " +
				" where T."+GenClientesTemporalBean.C_IDINSTITUCION+" = C."+CenColegiadoBean.C_IDINSTITUCION+" " +
				"   and T."+GenClientesTemporalBean.C_IDPERSONA+" = C."+CenColegiadoBean.C_IDPERSONA+" " +
				"   and T."+GenClientesTemporalBean.C_IDPERSONA+" = P."+CenPersonaBean.C_IDPERSONA+" " +
				"   and T."+GenClientesTemporalBean.C_CONTADOR+" = "+contador+" " +
				"   and T."+GenClientesTemporalBean.C_SALTO+" <> 'S'" +
				" order by T."+GenClientesTemporalBean.C_POSICION;
			
			vResult = this.find(consultaTemp).getAll();
			
			//Borrar de la tabla temporal por el campo contador
			String deleteTemp =
				"delete "+GenClientesTemporalBean.T_NOMBRETABLA+
				" where "+GenClientesTemporalBean.C_CONTADOR+"="+contador;
			ClsMngBBDD.executeUpdate(deleteTemp);
		}
		catch (ClsExceptions e)
		{
			e.printStackTrace();
		}
		
		return vResult;
	} //selectLetradosEnCola ()


	public static String getNombreGuardiaJSP (String institucion, String idturno, String idguardia) throws ClsExceptions,SIGAException {
		   String datos="";
	       try {
	            RowsContainer rc = new RowsContainer(); 

			    if (institucion!=null && !institucion.equals("") && idturno!=null && !idturno.equals("") && idguardia!=null && !idguardia.equals("")) {
			        
		            Hashtable codigos = new Hashtable();
				    int contador=0;
				    
		            String sql ="select NOMBRE " +
	                       " from SCS_GUARDIASTURNO ";
	         	        
	                contador++;
	   				codigos.put(new Integer(contador),institucion);
	   	            sql += " WHERE IDINSTITUCION =:"+contador;
	   	            
	   	            contador++;
	 				codigos.put(new Integer(contador),idturno);
	 	            sql += " AND IDTURNO =:"+contador;
														
	   	            contador++;
	 				codigos.put(new Integer(contador),idguardia);
	 	            sql += " AND IDGUARDIA =:"+contador;
															
		            if (rc.findBind(sql,codigos)) {
		               for (int i = 0; i < rc.size(); i++){
		                  Row fila = (Row) rc.get(i);
		                  Hashtable resultado=fila.getRow();	                  
		                  datos = (String)resultado.get("NOMBRE");
		               }
		            }
	       		}
	       } catch (Exception e) {
	    	   
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return datos;                        
	    }	
	
	
	
	
	public List<ScsGuardiasTurnoBean> getGuardiasTurnos(VolantesExpressVo volanteExpres)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
				
		if(volanteExpres.getFechaGuardia()!=null || volanteExpres.getIdColegiado()!=null){
//		if(false){
			sql.append(" SELECT GUA.IDGUARDIA, GUA.NOMBRE, GUA.IDTURNO, GUA.IDINSTITUCION ");
			sql.append(" FROM SCS_GUARDIASTURNO GUA,SCS_CALENDARIOGUARDIAS GC ");
			sql.append(" WHERE GUA.IDINSTITUCION = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
			sql.append(" AND GUA.IDTURNO = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdTurno());
			sql.append(" AND  GC.IDINSTITUCION = GUA.IDINSTITUCION ");
			sql.append(" AND GC.IDTURNO = GUA.IDTURNO ");
			sql.append(" AND GC.IDGUARDIA = GUA.IDGUARDIA ");
			sql.append(" AND :"); 
			contador ++;
			String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
		    htCodigos.put(new Integer(contador),truncFechaGuardia);
		    sql.append(contador);
			sql.append(" BETWEEN TRUNC(GC.FECHAINICIO) AND ");
			sql.append(" TRUNC(GC.FECHAFIN) ");
			sql.append(" ORDER BY GUA.NOMBRE ");
			
		}else{
			sql.append(" SELECT IDGUARDIA, NOMBRE,IDTURNO,IDINSTITUCION FROM SCS_GUARDIASTURNO ");
			sql.append(" WHERE IDINSTITUCION =:");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion()); 
			sql.append(" AND IDTURNO = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdTurno());
			 sql.append(" ORDER BY NOMBRE ");
			
			
			
		}
		List<ScsGuardiasTurnoBean> alGuardias = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
            	ScsGuardiasTurnoBean guardiaBean = null;
            	if(rc.size()>1){
	            	guardiaBean = new ScsGuardiasTurnoBean();
	            	guardiaBean.setIdGuardia(new Integer(-1));
	        		guardiaBean.setNombre(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(), "general.combo.seleccionar"));
	    			alGuardias.add(guardiaBean);
            	}
            	for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		guardiaBean = new ScsGuardiasTurnoBean();
            		guardiaBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsGuardiasTurnoBean.C_IDINSTITUCION));
            		guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsGuardiasTurnoBean.C_IDTURNO));
            		guardiaBean.setNombre(UtilidadesHash.getString(htFila,ScsGuardiasTurnoBean.C_NOMBRE));
            		guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila,ScsGuardiasTurnoBean.C_IDGUARDIA));
            		alGuardias.add(guardiaBean);
            	}
            } 
       } catch (Exception e) {
    	   ClsLogging.writeFileLog("VOLANTES EXPRESS:Error Select Guardias"+e.toString(), 10);
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alGuardias;
		
		
	} 
		

}