
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
import com.siga.gratuita.form.AsistenciaForm;
import com.siga.gratuita.form.AsistenciasForm;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;
import com.siga.gratuita.form.DefinirTurnosForm;
import com.siga.gratuita.form.PersonaJGForm;
import com.siga.gratuita.vos.VolantesExpressVo;


/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
* @author carlos.vidal
* @since 26/12/2004 
*/

public class ScsAsistenciasAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsAsistenciasAdm (UsrBean usuario) {
		super( ScsAsistenciasBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_ASISTENCIAS con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	protected String[] getCamposBean() {
		String[] campos = {	
				ScsAsistenciasBean.C_IDINSTITUCION,          
				ScsAsistenciasBean.C_ANIO,                   
				ScsAsistenciasBean.C_NUMERO,                 
				ScsAsistenciasBean.C_FECHAHORA,              
				ScsAsistenciasBean.C_OBSERVACIONES,          
				ScsAsistenciasBean.C_INCIDENCIAS,            
				ScsAsistenciasBean.C_FECHAANULACION,         
				ScsAsistenciasBean.C_MOTIVOSANULACION,       
				ScsAsistenciasBean.C_DELITOSIMPUTADOS,       
				ScsAsistenciasBean.C_CONTRARIOS,             
				ScsAsistenciasBean.C_DATOSDEFENSAJURIDICA,   
				ScsAsistenciasBean.C_FECHACIERRE,            
				ScsAsistenciasBean.C_IDTIPOASISTENCIA,       
				ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO,
				ScsAsistenciasBean.C_IDTURNO,                
				ScsAsistenciasBean.C_IDGUARDIA,              
				ScsAsistenciasBean.C_IDPERSONACOLEGIADO,     
				ScsAsistenciasBean.C_IDPERSONAJG,            
				ScsAsistenciasBean.C_FECHAMODIFICACION,      
				ScsAsistenciasBean.C_USUMODIFICACION,
				ScsAsistenciasBean.C_DESIGNA_ANIO,
				ScsAsistenciasBean.C_DESIGNA_NUMERO,
				ScsAsistenciasBean.C_DESIGNA_TURNO,
				ScsAsistenciasBean.C_FACTURADO,
				ScsAsistenciasBean.C_PAGADO,
				ScsAsistenciasBean.C_IDFACTURACION,
				ScsAsistenciasBean.C_NUMERODILIGENCIA,
				ScsAsistenciasBean.C_COMISARIA,
				ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION,
				ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO,
				ScsAsistenciasBean.C_JUZGADO,
				ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION,
				ScsAsistenciasBean.C_IDESTADOASISTENCIA,
				ScsAsistenciasBean.C_EJGIDTIPOEJG,
				ScsAsistenciasBean.C_EJGANIO,
				ScsAsistenciasBean.C_EJGNUMERO,
				ScsAsistenciasBean.C_NIG,
				ScsAsistenciasBean.C_IDPRETENSION,
				ScsAsistenciasBean.C_FECHAESTADOASISTENCIA
				};
		
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		String[] campos = {	ScsAsistenciasBean.C_IDINSTITUCION, ScsAsistenciasBean.C_ANIO,ScsAsistenciasBean.C_NUMERO};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean, con nomenclatura Tabla.Campo
	 * */
	protected String[] getClavesSelect(){
		String[] campos = { ScsAsistenciasBean.C_IDINSTITUCION,
				ScsAsistenciasBean.C_ANIO,
				ScsAsistenciasBean.C_NUMERO};
		return campos;
	}
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsAsistenciasBean bean = null;
		try{
			bean = new ScsAsistenciasBean();
			bean.setIdInstitucion          (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDINSTITUCION        ));
			bean.setAnio                   (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_ANIO                 ));
			bean.setDesignaAnio            (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_DESIGNA_ANIO         ));
			bean.setDesignaNumero          (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_DESIGNA_NUMERO       ));
			bean.setDesignaTurno		   (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_DESIGNA_TURNO        ));
			bean.setNumero                 (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_NUMERO               ));
			bean.setFechaHora              (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_FECHAHORA             ));
			bean.setObservaciones          (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_OBSERVACIONES         ));
			bean.setIncidencias            (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_INCIDENCIAS           ));
			bean.setFechaAnulacion         (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_FECHAANULACION        ));
			bean.setMotivosAnulacion       (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_MOTIVOSANULACION      ));
			bean.setDelitosImputados       (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_DELITOSIMPUTADOS      ));
			bean.setContrarios             (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_CONTRARIOS            ));
			bean.setDatosDefensaJuridica   (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_DATOSDEFENSAJURIDICA  ));
			bean.setFechaCierre            (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_FECHACIERRE           ));
			bean.setIdTipoAsistencia       (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDTIPOASISTENCIA     ));
			bean.setIdTipoAsistenciaColegio(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO));
			bean.setIdturno                (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDTURNO              ));
			bean.setIdguardia              (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDGUARDIA            ));
			bean.setIdPersonaColegiado     (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDPERSONACOLEGIADO   ));
			bean.setIdPersonaJG            (UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDPERSONAJG          ));
			bean.setFechaModificacion      (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_FECHAMODIFICACION     ));
			bean.setUsuModificacion        (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_USUMODIFICACION		 ));
			//bean.setFacturado		       (UtilidadesHash.getString(hash,ScsAsistenciasBean.C_FACTURADO			 ));

			bean.setJuzgado(UtilidadesHash.getLong(hash,ScsAsistenciasBean.C_JUZGADO));
			bean.setJuzgadoIdInstitucion(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION));
			bean.setComisaria(UtilidadesHash.getLong(hash,ScsAsistenciasBean.C_COMISARIA));
			bean.setComisariaIdInstitucion(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION));
			bean.setNumeroDiligencia(UtilidadesHash.getString(hash,ScsAsistenciasBean.C_NUMERODILIGENCIA));
			bean.setNumeroProcedimiento(UtilidadesHash.getString(hash,ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO));
			bean.setIdEstadoAsistencia(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDESTADOASISTENCIA));
			bean.setFechaEstadoAsistencia(UtilidadesHash.getString(hash,ScsAsistenciasBean.C_FECHAESTADOASISTENCIA));

			bean.setEjgIdTipoEjg(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_EJGIDTIPOEJG));
			bean.setEjgAnio(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_EJGANIO));
			bean.setEjgNumero(UtilidadesHash.getLong(hash,ScsAsistenciasBean.C_EJGNUMERO));
			bean.setNIG(UtilidadesHash.getString(hash,ScsAsistenciasBean.C_NIG));
			bean.setIdPretension(UtilidadesHash.getInteger(hash,ScsAsistenciasBean.C_IDPRETENSION));
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
	 * */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsAsistenciasBean b = (ScsAsistenciasBean) bean;

			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDINSTITUCION          , b.getIdInstitucion          ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_ANIO                   , b.getAnio                   ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_DESIGNA_ANIO           , b.getDesignaAnio());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_DESIGNA_NUMERO         , b.getDesignaNumero());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_DESIGNA_TURNO			, b.getDesignaTurno());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_NUMERO                 , b.getNumero                 ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_FECHAHORA              , b.getFechaHora              ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_OBSERVACIONES          , b.getObservaciones          ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_INCIDENCIAS            , b.getIncidencias            ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_FECHAANULACION         , b.getFechaAnulacion         ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_MOTIVOSANULACION       , b.getMotivosAnulacion       ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_DELITOSIMPUTADOS       , b.getDelitosImputados       ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_CONTRARIOS             , b.getContrarios             ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_DATOSDEFENSAJURIDICA   , b.getDatosDefensaJuridica   ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_FECHACIERRE            , b.getFechaCierre            ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDTIPOASISTENCIA       , b.getIdTipoAsistencia       ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO, b.getIdTipoAsistenciaColegio());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDTURNO                , b.getIdTurno                ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDGUARDIA              , b.getIdGuardia              ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDPERSONACOLEGIADO     , b.getIdPersonaColegiado     ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_IDPERSONAJG            , b.getIdPersonaJG            ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_FECHAMODIFICACION      , b.getFechaModificacion      ());
			UtilidadesHash.set(hash,ScsAsistenciasBean.C_USUMODIFICACION		 , b.getUsuModificacion        ());
			//UtilidadesHash.set(hash,ScsAsistenciasBean.C_FACTURADO				 , b.getFacturado		      ());

			UtilidadesHash.set(hash, ScsAsistenciasBean.C_JUZGADO, b.getJuzgado());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION, b.getJuzgadoIdInstitucion());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_COMISARIA, b.getComisaria());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION, b.getComisariaIdInstitucion());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_NUMERODILIGENCIA, b.getNumeroDiligencia());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO, b.getNumeroProcedimiento());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_IDESTADOASISTENCIA, b.getIdEstadoAsistencia());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_FECHAESTADOASISTENCIA, b.getFechaEstadoAsistencia());

			UtilidadesHash.set(hash, ScsAsistenciasBean.C_EJGIDTIPOEJG, b.getEjgIdTipoEjg());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_EJGANIO, b.getEjgAnio());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_EJGNUMERO, b.getEjgNumero());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_NIG, b.getNIG());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_IDPRETENSION, b.getIdPretension());
		
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/* esta select debe sacar diferente ordenacion dependiendo
	 * del usuario que está dentro de la aplicación. Como todavía no hay 
	 * entrada desde el menú, la entrada se le pasa al select con
	 * un String, valores 1 o 2
	 */
	/**
	 * Efectúa un SELECT en la tabla SCS_ASISTENCIAS con los datos introducidos. 
	 * 
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	
	public Vector select(Hashtable hash) throws ClsExceptions
	{
		Vector vector = new Vector();
		
		try {
			String where = UtilidadesBDAdm.sqlWhere(this.nombreTabla, hash, this.getClavesBean());
			vector = this.select(where);
		}
		catch (Exception e) {
			vector = null;		
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
		return vector;
	}

	/**
	 * Efectúa un SELECT en la tabla SCS_ASISTENCIA con los datos introducidos. 
	 * 
	 * @param where String la clausula where del select 
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector selectTabla(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(ScsActuacionAsistenciaBean.T_NOMBRETABLA, this.getCamposSelect());
			sql += where;
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
	
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		boolean salida = false;
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			
			salida = rc.query(consulta);
			if (salida) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsAsistenciasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	

	}
	
	public String getNumeroAsistencia(String idInstitucion, int anio) throws ClsExceptions
	{
		String numero = "";
		String select = "SELECT nvl(MAX(NUMERO)+1,1) NUMERO FROM "+this.nombreTabla + " WHERE IDINSTITUCION = "+idInstitucion + " AND ANIO = " + anio;
		try{
			Vector v = selectGenerico(select);
			numero = (String) ((Hashtable) v.get(0)).get("NUMERO");
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsAsistenciasAdm.getNumeroAsistencia(). "+e.getMessage());
		}
		
		return numero;
	}
	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * @param entrada String identificador de l permiso del usuario logado
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenSelect(String entrada){
		return null;
	}
	
	/**
	 * Efectúa un DELETE en la tabla SCS_TURNO del registro seleccionado 
	 * 
	 * @param hash Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return boleano que indica si la inserción fue correcta o no. 
	 */
	public boolean delete(Hashtable hash) throws ClsExceptions{
		
		try {
			Row row = new Row();	
			row.load(hash);

			String [] campos = this.getClavesBean();
			
			if (row.delete(this.nombreTabla, campos) == 1) {
				return true;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "Error al realizar el 'delete' en B.D.");
		}
		return false;
	}
	/** Funcion update (Hashtable hash)
	 *  @param bean con las parejas campo-valor para realizar un where en el update 
	 *  @return true -> OK false -> Error 
	 * */
	public boolean delete(MasterBean bean) throws ClsExceptions{
		try {
			return this.delete(this.beanToHashTable(bean));
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el 'delete' en B.D.");
		}
	}
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryNLS(select)) {
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
	 * Devuelve los campos de en la consulta
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con los campos del select
	 */
	
	protected String[] getCamposSelect(){
			String[] campos = {	
					"IDINSTITUCION",          
					"ANIO",                   
					"NUMERO",                 
					"FECHAHORA",              
					"IDTIPOASISTENCIA",       
					"IDTURNO",                
					"IDGUARDIA",              
					"FECHAMODIFICACION",      
					"USUMODIFICACION",        
					"IDPERSONACOLEGIADO",     
					"OBSERVACIONES",          
					"INCIDENCIAS",            
					"FECHAANULACION",         
					"MOTIVOSANULACION",       
					"DELITOSIMPUTADOS",       
					"CONTRARIOS",             
					"DATOSDEFENSAJURIDICA",   
					"FECHACIERRE",            
					"IDTIPOASISTENCIACOLEGIO",
					"IDPERSONA_REPRESENTANTE",
					"DESIGNA_ANIO",           
					"DESIGNA_NUMERO",         
					"EJGIDTIPOEJG",         
					"EJGANIO",         
					"EJGNUMERO",         
					"IDPERSONAJG"};
					//"FACTURADO"};
			return campos;
	}
	
	/** 
	 * Recoge informacion la informacion necesaria para rellenar la carta a los interesados de asistencia<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @param  actuacion - identificador de la actuacion
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosCartaAsistencia (String institucion, String epoca, String numero, String actuacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql ="SELECT NUMERO_ASISTENCIA,NUMERO_EXPEDIENTE,FECHAHORA,PARTIDO_JUDICIAL,JUZGADO,TURNO," +
	            			"DATOS_INTERESADO,DIRECCION_INTERESADO,MAX(NUMERO_TELEFONO),NUMERO_COLEGIADO,DATOS_COLEGIADO," +
							"DIRECCION_COLEGIADO,DELITOSIMPUTADOS,CONTRARIOS,OBSERVACIONES" +
							" FROM ( SELECT " +
							"(" + "asistencia_ejg." + ScsAsistenciasBean.C_NUMERO + " || '/' || " +
							"asistencia_ejg." + ScsAsistenciasBean.C_ANIO + ") AS NUMERO_ASISTENCIA, " +
							"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_NUMERO + " || '/' || " +
							ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_ANIO + ") AS NUMERO_EXPEDIENTE, " +
			    			"asistencia_ejg." + ScsAsistenciasBean.C_FECHAHORA + " AS FECHAHORA," +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_LUGAR + " AS JUZGADO," +
			    			ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_DESCRIPCION + " AS TURNO," +
							"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NIF + " || ' ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO2 + ") AS DATOS_INTERESADO, " +
							"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_DIRECCION + " || ' ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_CODIGOPOSTAL + " || ' ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_INTERESADO, " +
							ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_NUMEROTELEFONO + " AS NUMERO_TELEFONO," +
			    			" DECODE(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_COMUNITARIO + ",'0','"+ CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO + "','1','" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + "') AS NUMERO_COLEGIADO," +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + /*" || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + */ ") AS DATOS_COLEGIADO, " +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ' ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_COLEGIADO, " +
			    			"asistencia_ejg." + ScsAsistenciasBean.C_DELITOSIMPUTADOS + " AS DELITOSIMPUTADOS," +
			    			"asistencia_ejg." + ScsAsistenciasBean.C_CONTRARIOS + " AS CONTRARIOS," +
			    			"asistencia_ejg." + ScsAsistenciasBean.C_OBSERVACIONES + " AS OBSERVACIONES" +
							" FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + "," + 
									   ScsAsistenciasBean.T_NOMBRETABLA + " asistencia_personajg" +
									   " LEFT JOIN " + ScsPersonaJGBean.T_NOMBRETABLA + " ON " +
													   "asistencia_personajg."+ ScsAsistenciasBean.C_IDPERSONAJG + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA +
													   " AND " +
													   "asistencia_personajg."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION +
													   " LEFT JOIN " + ScsTelefonosPersonaBean.T_NOMBRETABLA + " ON " +
																	   ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDINSTITUCION + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION +
																	   " AND " +
																	   ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDPERSONA + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA +
																	   " AND " +
																	   ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDTELEFONO + " IS NOT NULL," +
									   ScsAsistenciasBean.T_NOMBRETABLA + " asistencia_ejg" +
									   " LEFT JOIN " + ScsEJGBean.T_NOMBRETABLA + " ON " +
													   ScsEJGBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "= asistencia_ejg."+ ScsAsistenciasBean.C_ANIO +
													   " AND " +
													   ScsEJGBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "= asistencia_ejg."+ ScsAsistenciasBean.C_NUMERO +
													   " AND " +
													   ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "= asistencia_ejg."+ ScsAsistenciasBean.C_IDINSTITUCION + "," +
									   ScsTurnoBean.T_NOMBRETABLA +			   
									   " LEFT JOIN " + ScsSubzonaBean.T_NOMBRETABLA + " ON " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDINSTITUCION +
													   " AND " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDZONA +
													   " AND " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDSUBZONA +
													   " LEFT JOIN " + CenPartidoJudicialBean.T_NOMBRETABLA + " ON " +
																	   ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA +"."+ CenPartidoJudicialBean.C_IDPARTIDO + "," +
									   CenColegiadoBean.T_NOMBRETABLA +			   
									   " LEFT JOIN " + CenDireccionesBean.T_NOMBRETABLA + " ON " +
													   CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION +
													   " AND " +
													   CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA +
													   " LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA + " ON " +
																	   CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
							" WHERE " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDTURNO +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_ANIO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_ANIO +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_NUMERO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_NUMERO +
							" AND " +
							ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDACTUACION + "=" + actuacion +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDINSTITUCION + "= asistencia_personajg."+ ScsAsistenciasBean.C_IDINSTITUCION + 
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_ANIO + "= asistencia_personajg."+ ScsAsistenciasBean.C_ANIO + 
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_NUMERO + "= asistencia_personajg."+ ScsAsistenciasBean.C_NUMERO + 
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_ANIO + "=" + epoca +
							" AND " +
							"asistencia_ejg."+ ScsAsistenciasBean.C_NUMERO + "=" + numero + ")" + 
						" GROUP BY NUMERO_ASISTENCIA,NUMERO_EXPEDIENTE,FECHAHORA,PARTIDO_JUDICIAL,JUZGADO,TURNO," +
	            				  "DATOS_INTERESADO,DIRECCION_INTERESADO,NUMERO_COLEGIADO,DATOS_COLEGIADO," +
								  "DIRECCION_COLEGIADO,DELITOSIMPUTADOS,CONTRARIOS,OBSERVACIONES";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las asistencias.");
	       }
	       return datos;                        
	    }
	
//	/** 
//	 * Recoge informacion la informacion necesaria para rellenar la carta a los interesados de asistencia<br/> 
//	 * @param  institucion - identificador de la institucion	 	  
//	 * @param  epoca - anho del expediente	 	  
//	 * @param  numero - numero de expediente
//	 * @param  actuacion - identificador de la actuacion
//	 * @return  Vector - Filas seleccionadas  
//	 * @exception  ClsExceptions  En cualquier caso de error
//	 */
//	public Vector getDatosCartaAsistencia (String institucion, String epoca, String numero, String actuacion) throws ClsExceptions,SIGAException {
//		   Vector datos=new Vector();
//	       try {
//	            RowsContainer rc = new RowsContainer();
//	            	            
//	            String sql ="SELECT " +
//							"(" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_NUMERO + " || '/' || " +
//							ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_ANIO + ") AS NUMERO_ASISTENCIA, " +
//							"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_NUMERO + " || '/' || " +
//							ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_ANIO + ") AS NUMERO_EXPEDIENTE, " +
//			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_FECHAHORA + "," +
//			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
//			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_LUGAR + " AS JUZGADO," +
//			    			ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_DESCRIPCION + " AS TURNO," +
//							"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NIF + " || ' ' || " +
//							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || " +
//							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
//							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO2 + ") AS DATOS_INTERESADO, " +
//							"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_DIRECCION + " || ' ' || " +
//							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_CODIGOPOSTAL + " || ' ' || " +
//							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_INTERESADO, " +
//							ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_NUMEROTELEFONO + " AS NUMERO_TELEFONO," +
//			    			" DECODE(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_COMUNITARIO + ",'0','"+ CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO + "','1','" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + "') AS NUMERO_COLEGIADO," +
//							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + /*" || ' ' || " +
//							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
//							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + */ ") AS DATOS_COLEGIADO, " +
//							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
//							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ' ' || " +
//							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_COLEGIADO, " +
//			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_DELITOSIMPUTADOS + "," +
//			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_CONTRARIOS + "," +
//			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_OBSERVACIONES +
//							" FROM " + ScsPersonaJGBean.T_NOMBRETABLA + "," + ScsEJGBean.T_NOMBRETABLA + "," +
//									   CenColegiadoBean.T_NOMBRETABLA + "," + CenPoblacionesBean.T_NOMBRETABLA + "," +
//									   ScsAsistenciasBean.T_NOMBRETABLA + "," + ScsActuacionAsistenciaBean.T_NOMBRETABLA + "," +
//									   CenPartidoJudicialBean.T_NOMBRETABLA + "," + ScsTurnoBean.T_NOMBRETABLA + "," +
//									   CenPersonaBean.T_NOMBRETABLA + "," + CenDireccionesBean.T_NOMBRETABLA + "," + 
//									   ScsSubzonaBean.T_NOMBRETABLA + "," + ScsTelefonosPersonaBean.T_NOMBRETABLA +
//							" WHERE " +
//							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ASISTENCIA_ANIO + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO +
//							" AND " +
//							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ASISTENCIA_NUMERO + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO +
//							" AND " +
//							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDTURNO +
//							" AND " +
//							ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDINSTITUCION +
//							" AND " +
//							ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDZONA +
//							" AND " +
//							ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDSUBZONA +
//							" AND " +
//							ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA +"."+ CenPartidoJudicialBean.C_IDPARTIDO +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDINSTITUCION +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_ANIO +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_NUMERO +
//							" AND " +
//							ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDACTUACION + "=" + actuacion +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONAJG + "(+)=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "(+)=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION +
//							" AND " +
//							ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDINSTITUCION + "=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION +
//							" AND " +
//							ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDPERSONA + "=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONAJG +
//							" AND " +
//							ScsTelefonosPersonaBean.T_NOMBRETABLA +"."+ ScsTelefonosPersonaBean.C_IDTELEFONO + 
//								"=" +
//								"(SELECT MIN(" + ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_IDTELEFONO + ")" +
//									" FROM " + ScsTelefonosPersonaBean.T_NOMBRETABLA + "," + ScsAsistenciasBean.T_NOMBRETABLA +
//									" WHERE " + 
//									ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_IDINSTITUCION + "=" + institucion +
//									" AND " +
//									ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_IDPERSONA + "=" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDPERSONAJG +
//								")" +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
//							" AND " +
//							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA +
//							" AND " +
//							CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "(+)=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
//							" AND " +
//							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION +
//							" AND " +
//							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + institucion +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + epoca +
//							" AND " +
//							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + numero;
//														
//	            if (rc.find(sql)) {
//	               for (int i = 0; i < rc.size(); i++){
//	                  Row fila = (Row) rc.get(i);
//	                  Hashtable resultado=fila.getRow();	                  
//	                  datos.add(resultado);
//	               }
//	            } 
//	       }
//	       catch (Exception e) {
//	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre el declarante.");
//	       }
//	       return datos;                        
//	    }
	
	
	/** 
	 * Recoge informacion la informacion necesaria para rellenar la carta a los interesados de asistencia<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @param  actuacion - identificador de la actuacion
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosEJGRelativosAsistencia (String institucion, String epoca, String numero, String actuacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql ="SELECT " +
							"(" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_NUMERO + " || '/' || " +
							ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_ANIO + ") AS NUMERO," +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_LUGAR + " AS JUZGADO," +
			    			ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_DESCRIPCION + " AS TURNO," +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_DELITOSIMPUTADOS + " AS DELITO," +
							" DECODE(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_SEXO + ", 'M', 'FEMENINO', 'MASCULINO') AS SEXO_LETRADO," +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ") AS DATOS_LETRADO," +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AS IDPERSONA," +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ' ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_LETRADO," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + " AS TELEFONO_LETRADO" +
							" FROM " + CenClienteBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + "," +  
									   ScsActuacionAsistenciaBean.T_NOMBRETABLA + "," + CenColegiadoBean.T_NOMBRETABLA + "," +
									   ScsTurnoBean.T_NOMBRETABLA + 
									   " LEFT JOIN " + ScsSubzonaBean.T_NOMBRETABLA + " ON " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDINSTITUCION +
													   " AND " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDZONA +
													   " AND " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDSUBZONA +
													   " LEFT JOIN " + CenPartidoJudicialBean.T_NOMBRETABLA + " ON " +
																	   ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA +"."+ CenPartidoJudicialBean.C_IDPARTIDO + "," +
									   ScsAsistenciasBean.T_NOMBRETABLA +
									   " LEFT JOIN " + CenDireccionesBean.T_NOMBRETABLA + " ON " +
													   CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION +
													   " AND " +
													   CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO +
													   " LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA + " ON " +
																	   CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
							" WHERE " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDTURNO +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA +							
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_ANIO +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_NUMERO +
							" AND " +
							ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDACTUACION + "=" + actuacion +							
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + numero;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una asistencia EJG.");
	       }
	       return datos;                        
	    }
	
	/**
	 * 
	 * @param  institucion - identificador de la institucion
	 * @param  anio - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return numero de expediente del EJG
	 * @throws ClsExceptions
	 */	
	public String getNumeroAnioEJGAsistencia(Integer institucion, Integer anio, Integer numero) throws ClsExceptions {
		String ejg=null;
		String sql = 
			"select ejgnumero||' / '||ejganio NA from scs_asistencia "+
			"where idinstitucion="+institucion+
			"  and anio="+anio+
			"  and numero="+numero;
		
		RowsContainer rc = new RowsContainer();
		if(rc.find(sql)){
			ejg=((Row)rc.get(0)).getString("NA");
		}
		
		return ejg;
	}
	
	
	/** 
	 * Recoge informacion sobre las actuaciones relacionadas con una asistencia 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getActuacionesAsistencia (String institucion, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + "," +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + "," +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + "," +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDACTUACION +
							" FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA +
							" INNER JOIN " + ScsAsistenciasBean.T_NOMBRETABLA +
								" ON " + ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + "=" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDINSTITUCION +
										 " AND " +
										 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + "=" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_ANIO +
										 " AND " +
										 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + "=" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_NUMERO +
							" WHERE " +			 
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + numero;
	            
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las actuaciosnes de una asistencia.");
	       }
	       return datos;                        
	    }
	
	public void insertarNuevaAsistencia(String idInstitucion, String anio, String numero, String fecha, String idTurno, String idGuardia, String idTipoAsistencia, String idTipoAsistenciaColegio, String idPersona, String estadoAsistencia) throws ClsExceptions
	{
		
			Hashtable hash = new Hashtable();
			hash.put(ScsAsistenciasBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ScsAsistenciasBean.C_ANIO,anio);
			hash.put(ScsAsistenciasBean.C_NUMERO,numero);
			hash.put(ScsAsistenciasBean.C_FECHAHORA,fecha);
			hash.put(ScsAsistenciasBean.C_IDTURNO,idTurno);
			hash.put(ScsAsistenciasBean.C_IDGUARDIA,idGuardia);
			hash.put(ScsAsistenciasBean.C_IDTIPOASISTENCIA,idTipoAsistencia);
			hash.put(ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO,idTipoAsistenciaColegio);
			hash.put(ScsAsistenciasBean.C_IDPERSONACOLEGIADO,idPersona);
			hash.put(ScsAsistenciasBean.C_IDESTADOASISTENCIA,estadoAsistencia);
			hash.put(ScsAsistenciasBean.C_FECHAESTADOASISTENCIA,fecha);
			
			if(!this.insert(hash))
				throw new ClsExceptions(this.getError());
		
	}

	public Hashtable getCabeceraGuardia(Hashtable hashAsistencia) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   Hashtable resultado = null;
		   
			String idinstitucion="", idcalendarioguardias="", idturno="", idguardia="", fechaHora="", idPersona="", fechaFin="";
			boolean salida = false;
			StringBuffer sql = new StringBuffer();
			
		    try {
	            RowsContainer rc = new RowsContainer();
	            	            	            
				idinstitucion = (String)hashAsistencia.get(ScsAsistenciasBean.C_IDINSTITUCION);
				idturno = (String)hashAsistencia.get(ScsAsistenciasBean.C_IDTURNO);
				idguardia = (String)hashAsistencia.get(ScsAsistenciasBean.C_IDGUARDIA);
				idPersona = (String)hashAsistencia.get(ScsAsistenciasBean.C_IDPERSONACOLEGIADO);
				fechaHora = UtilidadesString.formatoFecha((String)hashAsistencia.get(ScsAsistenciasBean.C_FECHAHORA), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");

				sql.append(" SELECT * from "+ScsCabeceraGuardiasBean.T_NOMBRETABLA);
				sql.append(" where "+ScsCabeceraGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
				sql.append(" and "+ScsCabeceraGuardiasBean.C_IDTURNO+"="+idturno);
				sql.append(" and "+ScsCabeceraGuardiasBean.C_IDGUARDIA+"="+idguardia);
				sql.append(" and "+ScsCabeceraGuardiasBean.C_IDPERSONA+"="+idPersona);
				sql.append(" and TO_DATE('"+fechaHora+"','DD/MM/YYYY') between trunc("+ScsCabeceraGuardiasBean.C_FECHA_INICIO+")");
				sql.append(" and trunc("+ScsCabeceraGuardiasBean.C_FECHA_FIN+")");
				
	            if (rc.find(sql.toString())) {
	               if (rc.size() == 1) {
	                  Row fila = (Row) rc.get(0);
	                  resultado=fila.getRow();	                  
	               }
	            } 
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al obtener la informacion sobre una asistencia EJG.");
	       }
	       return resultado;                        
	    }

	public Hashtable getAsistencia (String anio, String numero, String idInstitucion) throws ClsExceptions,SIGAException {
		   Hashtable datos = new Hashtable();
		   
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql = "SELECT * FROM "+ScsAsistenciasBean.T_NOMBRETABLA+
							 " WHERE "+ScsAsistenciasBean.C_ANIO+"="+anio+
							 " AND "+ScsAsistenciasBean.C_NUMERO+"="+numero+
							 " AND "+ScsAsistenciasBean.C_IDINSTITUCION+"="+idInstitucion;
									
	            if (rc.find(sql)) {
	               if (rc.size()== 1){
	                  Row fila = (Row) rc.get(0);
	                  datos = fila.getRow();	                  
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una asistencia EJG.");
	       }
	       return datos;                        
	 }

	public Hashtable getTituloPantallaAsistencia (String idInstitucion, String anio, String numero) 
	{
		try {
			String sql = 	"select " + ScsPersonaJGBean.C_NOMBRE + "," + 
			                            ScsPersonaJGBean.C_APELLIDO1 + ", " + 
										ScsPersonaJGBean.C_APELLIDO2 + ", " + 
										ScsAsistenciasBean.C_ANIO + ", " +
										ScsAsistenciasBean.C_NUMERO +
							" from " + ScsPersonaJGBean.T_NOMBRETABLA + " p, " + ScsAsistenciasBean.T_NOMBRETABLA + " a " + 
							" where a." + ScsAsistenciasBean.C_IDINSTITUCION + " = " + idInstitucion +  
							  " and a." + ScsAsistenciasBean.C_ANIO + " = " + anio +
							  " and a." + ScsAsistenciasBean.C_NUMERO  + " = " + numero +
							  " and a." + ScsAsistenciasBean.C_IDPERSONAJG + " = p." + ScsPersonaJGBean.C_IDPERSONA + "(+)" +
							  " and a." + ScsAsistenciasBean.C_IDINSTITUCION+ " = p." + ScsPersonaJGBean.C_IDINSTITUCION + "(+)";
	
			Vector v = this.selectGenerico(sql);
			if (v!=null && v.size()>0) {
				return (Hashtable) v.get(0);
			}
		} 
		catch (ClsExceptions e) {
			e.printStackTrace();
		}
		return new Hashtable();
	}
	
	public Hashtable existeLetradoAsistencia(String institucion,String numero, String anio){
		
		//ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		
		Vector v=new Vector();
		Hashtable h=new Hashtable();
		
		String sql="select a.idpersonacolegiado as IDPERSONA, a.idturno as TURNO,c.ncolegiado AS NCOLEGIADO ,p.nombre||' '||p.apellidos1||' '||p.apellidos2 as NOMCOLEGIADO" +
				"  from scs_asistencia a,cen_colegiado c,cen_persona p" +
				"  where a.anio = "+anio+
				"   and a.numero = "+ numero +
				"   and a.idinstitucion = "+institucion+
				"  and c.idinstitucion = a.idinstitucion"+
				"  and a.idpersonacolegiado=c.idpersona"+
				"  and c.idpersona=p.idpersona";

		try {
			v=super.selectGenerico(sql);
			if(v!=null){
				h=(Hashtable)v.get(0);
			}
		} catch (ClsExceptions e) {
			e.printStackTrace();
		}
		catch (SIGAException e1) {
			e1.printStackTrace();
		}
		
		return h;
	}	
	
	
	public Vector getAsistenciasVolantesExpres(Integer idInstitucion,
			String letrado, String diaGuardia, Integer idTurno,
			Integer idGuardia, boolean isJuzgado,String tipoAsistencia, String tipoAsistenciaColegio) 
	{
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			
			String camposOrigen[] = this.getCamposBean();
			String campos[] = new String [camposOrigen.length+6];
			campos[this.getCamposBean().length]   = "TO_CHAR(" + ScsAsistenciasBean.C_FECHAHORA + ", 'hh24') HORA";
			campos[this.getCamposBean().length+1] = "TO_CHAR(" + ScsAsistenciasBean.C_FECHAHORA + ", 'mi') MINUTO";
			campos[this.getCamposBean().length+2] = ScsPersonaJGBean.C_NOMBRE;
			campos[this.getCamposBean().length+3] = ScsPersonaJGBean.C_APELLIDO1;
			campos[this.getCamposBean().length+4] = ScsPersonaJGBean.C_APELLIDO2;
			campos[this.getCamposBean().length+5] = ScsPersonaJGBean.C_NIF;
			for (int i = 0; i < camposOrigen.length; campos[i] = ScsAsistenciasBean.T_NOMBRETABLA + "." + camposOrigen[i], i++);

			String sql = UtilidadesBDAdm.sqlSelect(ScsAsistenciasBean.T_NOMBRETABLA, campos);
			
			sql += ", " + ScsPersonaJGBean.T_NOMBRETABLA + " P ";
			
			String where = " WHERE " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDINSTITUCION + " = " + idInstitucion + 
				             " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDTURNO + " = " + idTurno +
				             " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDGUARDIA + " = " + idGuardia +
				             " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDPERSONACOLEGIADO + " = " + letrado +
				             " AND trunc(" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_FECHAHORA + ") = trunc(TO_DATE('" + diaGuardia + "', '" + ClsConstants.DATE_FORMAT_SQL + "'))" +
							 " AND P." + ScsPersonaJGBean.C_IDPERSONA + " = " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDPERSONAJG +	
							 " AND P." + ScsPersonaJGBean.C_IDINSTITUCION + " = " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDINSTITUCION ;
							 if(isJuzgado){
								 where += " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_JUZGADO + " IS NOT NULL " ;
							 }else{
								 where += " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_COMISARIA + " IS NOT NULL " ;
								 
							 }
							 if(tipoAsistencia != null && !tipoAsistencia.equals("")){
								 where += " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDTIPOASISTENCIA + " = "+ tipoAsistencia;
							 }
							 if(tipoAsistenciaColegio!=null && !tipoAsistenciaColegio.equals("")){
								 where += " AND " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO + " = "+ tipoAsistenciaColegio;
							 }
							 
						   //" ORDER BY HORA, MINUTO";
							 where += " ORDER BY "+ ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_ANIO+" asc, "+ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_NUMERO+" ASC";//ordenamos por orden de inserccion en base de datos

			sql += where;
			ScsEJGAdm ejgAdm = new ScsEJGAdm(this.usrbean);
			ScsDelitosAsistenciaAdm delitoAsisAdm = new ScsDelitosAsistenciaAdm(this.usrbean);
			
			if (rc.query(sql)) {
				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null){
						Vector vDelitos = delitoAsisAdm.getDelitosAsitencia(
								new Integer((String)registro.get(ScsAsistenciasBean.C_IDINSTITUCION))
								,new Integer((String)registro.get(ScsAsistenciasBean.C_ANIO))
								,new Integer((String)registro.get(ScsAsistenciasBean.C_NUMERO)),null);
						if(vDelitos!=null && vDelitos.size()>0){
							Hashtable htDelitoAsistencia = (Hashtable)vDelitos.get(0);
							registro.put("IDDELITO",(String)htDelitoAsistencia.get("IDDELITO"));
						}else{
							registro.put("IDDELITO","");
							
						}
						if(registro.get(ScsAsistenciasBean.C_EJGANIO)!=null && !((String)registro.get(ScsAsistenciasBean.C_EJGANIO)).equals("")){
							Hashtable htEjg = new Hashtable();
							htEjg.put(ScsEJGBean.C_IDINSTITUCION, (String)registro.get(ScsAsistenciasBean.C_IDINSTITUCION));
							htEjg.put(ScsEJGBean.C_IDTIPOEJG, (String)registro.get(ScsAsistenciasBean.C_EJGIDTIPOEJG));
							htEjg.put(ScsEJGBean.C_ANIO, (String)registro.get(ScsAsistenciasBean.C_EJGANIO));
							htEjg.put(ScsEJGBean.C_NUMERO, (String)registro.get(ScsAsistenciasBean.C_EJGNUMERO));
							Vector vEjg =  ejgAdm.selectByPK(htEjg);
							ScsEJGBean beanEjg = (ScsEJGBean)vEjg.get(0);
							registro.put(ScsEJGBean.C_NUMEJG, beanEjg.getNumEJG());
						}
						v.add(registro);
					}
					
					
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}
	
	
	
	public PaginadorBind getBusquedaAsistencias(AsistenciasForm miForm, String idPersona)throws ClsExceptions,SIGAException {
	    PaginadorBind paginador=null;
   try {
        RowsContainer rc = new RowsContainer(); 

	    Hashtable codigos = new Hashtable();
	    int contador=0;
	    
		  //obtengo datos de la consulta 			
		  Vector datos = null;

		  boolean isFichaColegial = idPersona!=null && !idPersona.trim().equals("");
		 
	  
    String sql =
		"SELECT A.IDINSTITUCION, A.ANIO ANIO, A.NUMERO NUMERO, "+
		" A.FECHAHORA FECHAHORA, " +
		" A.IDPERSONACOLEGIADO as IDPERSONA, " +
		" D.NOMBRE||' '||D.APELLIDO1||' '||D.APELLIDO2 NOMBRE, " +
		" A.IDTURNO IDTURNO, A.IDGUARDIA IDGUARDIA, A.FACTURADO FACTURADO, A.IDFACTURACION IDFACTURACION, "+
		//" F_SIGA_ACTASIST_NOVALIDAR(a.idinstitucion,a.anio,a.numero) actnovalidadas,"+
		" A.IDESTADOASISTENCIA as ESTADO ";
		if(isFichaColegial){
			sql+=" ,B.LETRADOASISTENCIAS as LETRADOASISTENCIAS ";
		}
//		
		
		//" P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' || P.APELLIDOS2 LETRADO"+
		sql+=" FROM SCS_ASISTENCIA A, SCS_PERSONAJG D ";
		if(isFichaColegial){
			sql+=" , SCS_GUARDIASTURNO C,SCS_TURNO B ";
		}
		sql+=" WHERE "+
		//" B.IDTURNO = C.IDTURNO AND B.IDINSTITUCION = C.IDINSTITUCION AND "+
		//" A.IDINSTITUCION = C.IDINSTITUCION AND A.IDTURNO = C.IDTURNO AND A.IDGUARDIA = C.IDGUARDIA AND "+
		" A.IDINSTITUCION = D.IDINSTITUCION(+) AND A.IDPERSONAJG = D.IDPERSONA(+) ";
		if(isFichaColegial){
			sql+=" AND A.IDTURNO = C.IDTURNO AND A.IDGUARDIA = C.IDGUARDIA AND A.IDINSTITUCION = C.IDINSTITUCION" +
					" AND B.IDTURNO = C.IDTURNO AND B.IDINSTITUCION = C.IDINSTITUCION " ;
					
		}
		
		//" AND A.IDPERSONACOLEGIADO = E.IDPERSONA AND A.IDINSTITUCION = E.IDINSTITUCION "+
		//"    AND A.IDPERSONACOLEGIADO = P.IDPERSONA ";
      
      contador ++;
      codigos.put(new Integer(contador),this.usrbean.getLocation());
	  sql += " AND A.IDINSTITUCION = :" + contador;
	  
	  if (idPersona!=null && !idPersona.trim().equals("")) {
	      Long idPers = new Long(idPersona);
	      contador ++;
	      codigos.put(new Integer(contador),idPers.toString());
	      sql += " AND " + ScsAsistenciasBean.C_IDPERSONACOLEGIADO + " = :" + contador ;
    	  String anio = miForm.getAnio();
			if(!String.valueOf(anio).equals("")) 	{
			    contador ++;
			    codigos.put(new Integer(contador),anio);
			    sql+=" AND A.ANIO = :"+contador;
			}
	      sql += " ORDER BY " + ScsAsistenciasBean.C_FECHAHORA+" desc, "+ ScsAsistenciasBean.C_IDINSTITUCION+" desc, " + ScsAsistenciasBean.C_ANIO+" desc, "+ScsAsistenciasBean.C_NUMERO+" desc";	

	  }else {
		
		    //Obtenemos los parametros del form
			String idioma 				= this.usrbean.getLanguage();
			String anio 				= miForm.getAnio();
			String numero 				= miForm.getNumero();
			String fechaDesde 			= miForm.getFechaDesde();
			String fechaHasta 			= miForm.getFechaHasta();
			String idTurno 				= miForm.getIdTurno();
			String idGuardia 			= miForm.getIdGuardia();
			String nColegiado 			= miForm.getColegiado(); //Es el idPersona no el nColegiado
			String tAsistencia 			= miForm.getIdTipoAsistencia();
			String tAsistenciaColegio 	= miForm.getIdTipoAsistenciaColegio();
			String nif 					= miForm.getNif();
			String nombre 				= miForm.getNombre();
			String apellido1 			= miForm.getApellido1();
			String apellido2 			= miForm.getApellido2();
			String estado				= miForm.getEstado();
			String actuacionesPendientes =miForm.getActuacionesPendientes();
			String actuacionValidada 	= miForm.getActuacionValidada();
			String comisaria			= miForm.getComisaria();
			String tipoActuacion		= miForm.getTipoActuacion();
			String comisariaInstitucionAsi = "";
			String procedimiento		= miForm.getProcedimiento();
			String nig					= miForm.getNig2();
			
			if(!String.valueOf(comisaria).equals("")){
				String a[]=comisaria.split(",");
				comisaria=a[0].trim();
				comisariaInstitucionAsi			= a[1].trim();
			}
			
			String juzgado				= miForm.getJuzgado();
			String asunto				= miForm.getAsunto();
			// Me guardo los datos de la busqueda para el boton volver
			
			
			
			// Preparamos el where de la consulta.
			if(!String.valueOf(anio).equals("")) 	{
			    contador ++;
			    codigos.put(new Integer(contador),anio);
			    sql+=" AND A.ANIO = :"+contador;
			}
			if(!String.valueOf(estado).equals("")) 	{
			    contador ++;
			    codigos.put(new Integer(contador),estado);
			    sql+=" AND A.IDESTADOASISTENCIA = :"+contador;
			}
			if(!String.valueOf(numero).equals("")) 	{
			    contador ++;
			    codigos.put(new Integer(contador),numero);
			    sql+=" AND A.NUMERO = :"+contador;
			}
			if(!String.valueOf(idTurno).equals("")) {
			    contador ++;
			    codigos.put(new Integer(contador),idTurno);
			    sql+=" AND A.IDTURNO = :"+contador;
			}
			if(!String.valueOf(idGuardia).equals("")) {
			    contador ++;
			    codigos.put(new Integer(contador),idGuardia);
			    sql+=" AND A.IDGUARDIA = :"+contador;
			}
			if(!String.valueOf(nColegiado).equals("")) {
			    contador ++;
			    codigos.put(new Integer(contador),nColegiado);
			    sql+=" AND A.IDPERSONACOLEGIADO = :"+contador; //Es el idPersona no el nColegiado
			}
			if(!String.valueOf(tAsistencia).equals("")) {
			    contador ++;
			    codigos.put(new Integer(contador),tAsistencia);
			    sql+=" AND A.IDTIPOASISTENCIA = :"+contador;
			}
			if(!String.valueOf(tAsistenciaColegio).equals("")){
			    contador ++;
			    codigos.put(new Integer(contador),tAsistenciaColegio);
			    sql+=" AND A.IDTIPOASISTENCIACOLEGIO = :"+contador;
			}
			if(!String.valueOf(asunto).equals("")) {
			    contador ++;
			    codigos.put(new Integer(contador),this.usrbean.getLocation());
			    sql +=" and (select count(1) "+
	                    " from scs_actuacionasistencia ac " +
			    		" where ac.idinstitucion= :"+contador;
			    contador ++;
			    codigos.put(new Integer(contador),asunto.trim());
	            sql +=  " and ac.anio=a.anio "+
	                    " and ac.numero=a.numero "+
	                    " and ac.numeroasunto=:"+contador+")>0";
			}
			
			if(!String.valueOf(procedimiento).equals("")){
			    contador ++;
			    codigos.put(new Integer(contador),procedimiento);
			    sql+=" AND A.numeroprocedimiento = :"+contador;
			}
			
			if(!String.valueOf(nig).equals("")){
			    contador ++;
			    codigos.put(new Integer(contador),nig);
			    sql+=" AND A.NIG = :"+contador;
			}			
					
			if(!String.valueOf(comisaria).equals("")){
			    contador ++;
			    codigos.put(new Integer(contador),this.usrbean.getLocation());
			    sql +=" and (select count(1) "+
	                  " from scs_actuacionasistencia ac "+
	                  " where ac.idinstitucion=:"+contador;
			    contador ++;
			    codigos.put(new Integer(contador),comisaria);
			    sql +=" and ac.anio=a.anio "+
	                  " and ac.numero=a.numero "+
	                  " and ac.idcomisaria=:"+contador;
			    contador ++;
			    codigos.put(new Integer(contador),comisariaInstitucionAsi);
			    sql += " and ac.idinstitucion_comis=:"+contador+")>0";
			}
			
			if(!String.valueOf(juzgado).equals("")){
				String b[]=juzgado.split(",");
			    contador ++;
			    codigos.put(new Integer(contador),this.usrbean.getLocation());
			    sql +=" and (select count(1) "+
	          " from scs_actuacionasistencia ac "+
	          " where ac.idinstitucion=:"+contador;
				contador ++;
			    codigos.put(new Integer(contador),b[0].trim());
			    sql +=" and ac.anio=a.anio "+
	          " and ac.numero=a.numero "+
	          " and ac.IDJUZGADO=:"+contador;
			    contador ++;
			    codigos.put(new Integer(contador),b[1].trim());
			    sql += " and ac.idinstitucion_juzg=:"+contador+")>0";
			}
			
			if(!String.valueOf(tipoActuacion).equals("")){
				String c[]=tipoActuacion.split(",");
			    contador ++;
			    codigos.put(new Integer(contador),this.usrbean.getLocation());
			    sql +=" and (select count(1) "+
		                " from scs_actuacionasistencia ac "+
		                " where ac.idinstitucion=:"+contador;
				contador ++;
			    codigos.put(new Integer(contador),c[0].trim());
			    sql +=  " and ac.anio=a.anio "+
		                " and ac.numero=a.numero "+
		                " and ac.idtipoactuacion=:"+contador+")>0";
			}
			
			if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
				if (!fechaDesde.equals(""))
					fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
				if (!fechaHasta.equals(""))
					fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);

		         // -------------- dateBetweenDesdeAndHastaBind
		         Vector v = GstDate.dateBetweenDesdeAndHastaBind("FECHAHORA", fechaDesde, fechaHasta,contador, codigos);
		         Integer in = (Integer)v.get(0);
		         String st = (String)v.get(1);
		         contador = in.intValue();
		         // --------------

				sql += " AND " + st;
			}
	
			if(!String.valueOf(nif).equals("")){
				if (ComodinBusquedas.hasComodin(nif.toUpperCase())){	
				    contador ++;
				    sql+=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(nif.trim(),"D.NIF", contador, codigos);
				}else{
				    contador ++;
				    sql +=" AND "+ComodinBusquedas.prepararSentenciaNIFBind(nif.toUpperCase(),"D.NIF",contador, codigos);
//				    codigos.put(new Integer(contador),nif.toUpperCase()+"%");
//				    sql +=" AND LTRIM(UPPER(D.NIF),'0') LIKE LTRIM(:"+contador+",'0') ";
			        
				}
			}
	
			if(ComodinBusquedas.hasComodin(nombre)){
				contador++;
			    sql +=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(nombre.trim(),"D.NOMBRE", contador, codigos);
			} else {
				if(!String.valueOf(nombre).equals("")) {
				    contador ++;
				    sql+=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(nombre.trim(),"D.NOMBRE",contador, codigos);
				}
			}
			
			if(ComodinBusquedas.hasComodin(apellido1)) {
			    contador++;
				sql +=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(apellido1.trim(),"D.APELLIDO1",contador, codigos);
		    }else {
				if(!String.valueOf(apellido1).equals("")){ 	
				    contador++;
				    sql+=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(apellido1.trim(),"D.APELLIDO1",contador, codigos);
				}
			}
			
			if(ComodinBusquedas.hasComodin(apellido2)) { 
			    contador++;
			    sql +=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(apellido2.trim(),"D.APELLIDO2",contador, codigos);
			} else {
				if(!String.valueOf(apellido2).equals("")) {
				    contador++;
				    sql+=" AND "+ComodinBusquedas.prepararSentenciaCompletaBind(apellido2.trim(),"D.APELLIDO2",contador, codigos);
				}
			}
			
			if (actuacionesPendientes!= null && !actuacionesPendientes.equalsIgnoreCase("")&& (actuacionesPendientes.equalsIgnoreCase("NO")||actuacionesPendientes.equalsIgnoreCase("SI")||actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES"))) {
				if(actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES")){
					actuacionesPendientes="";
					// contador ++;
				    // codigos.put(new Integer(contador),actuacionesPendientes.trim());
				    sql += " and upper(F_SIGA_ACTUACIONESASIST(a.idinstitucion,a.anio,a.numero)) is null";
				}else{
			    contador ++;
			    codigos.put(new Integer(contador),actuacionesPendientes.trim());
				    sql += " and upper(F_SIGA_ACTUACIONESASIST(a.idinstitucion,a.anio,a.numero))=upper(:" + contador + ")";
			}
			}
					  		
			sql += " ORDER BY " + ScsAsistenciasBean.C_IDINSTITUCION+" desc, " + ScsAsistenciasBean.C_ANIO+" desc, "+ScsAsistenciasBean.C_NUMERO+" desc";
		
	  }
	  
      paginador = new PaginadorBind(sql,codigos);	
        
         
   } catch (Exception e) {
   		throw new ClsExceptions (e, "Error al ejecutar consulta.");
   }
   return paginador;                        
}		

	public static String obtenerActuacionesPendientesValidarJSP(String idInstitucion, String anio, String numero) throws ClsExceptions, SIGAException{
		Vector vPersonas = null;
		String valor = "";
		try{
		    RowsContainer rc = new RowsContainer(); 

		    Hashtable codigos = new Hashtable();
		    int contador=0;
		    
			// String sql = "SELECT F_SIGA_ACTASIST_NOVALIDAR";
		    String sql = "SELECT F_SIGA_ACTUACIONESASIST";
			contador ++;
			codigos.put(new Integer(contador),idInstitucion);
			sql += "(:"+contador+" ";
			contador ++; 
			codigos.put(new Integer(contador),anio);
			sql += ",:"+contador+" ";
			contador ++;
			codigos.put(new Integer(contador),numero);
			sql += ",:"+contador+" ) as VALOR from dual ";
			
			

			if (rc.findBind(sql,codigos)) {
               for (int i = 0; i < rc.size(); i++){
                    Row fila = (Row) rc.get(i);
                    Hashtable resultado=fila.getRow();
                  
					valor = ((String)resultado.get("VALOR"));
					
               }
		    }

		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener el nombre y apellidos");
		}
		return valor;
	}

	
	/**
	 * Devuelve la asistencia (si existe) que esta relacionada con una determinada designa pero sin tener relacion previa con un ejg
	 * @param anio
	 * @param numero
	 * @param idInstitucion
	 * @param turno
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable getRelacionadoDesigna (String anio, String numero, String idInstitucion, String turno) throws ClsExceptions,SIGAException {
		   Hashtable datos = null;
		   
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql = "SELECT * FROM "+ScsAsistenciasBean.T_NOMBRETABLA+
							 " WHERE "+ScsAsistenciasBean.C_EJGNUMERO+" is null"+
							 " AND "+ScsAsistenciasBean.C_DESIGNA_ANIO+"="+anio+
							 " AND "+ScsAsistenciasBean.C_DESIGNA_NUMERO+"="+numero+
							 " AND "+ScsAsistenciasBean.C_IDINSTITUCION+"="+idInstitucion+
							 " AND "+ScsAsistenciasBean.C_DESIGNA_TURNO+"="+turno;
									
	            if (rc.find(sql)) {
	               if (rc.size()== 1){
	                  Row fila = (Row) rc.get(0);
	                  datos = fila.getRow();	                  
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una asistencia EJG.");
	       }
	       return datos;                        
	 }
	
	/**
	 * Devuelve la asistencia (si existe) que esta relacionada con un determinado EJG pero sin tener relacion previa con una designacion
	 * @param anio
	 * @param numero
	 * @param idInstitucion
	 * @param idTipo
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable getRelacionadoEJG (String anio, String numero, String idInstitucion, String idTipo) throws ClsExceptions,SIGAException {
		   Hashtable datos = null;
		   
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql = "SELECT * FROM "+ScsAsistenciasBean.T_NOMBRETABLA+
							 " WHERE "+ScsAsistenciasBean.C_DESIGNA_NUMERO+" is null"+
							 " AND "+ScsAsistenciasBean.C_EJGANIO+"="+anio+
							 " AND "+ScsAsistenciasBean.C_EJGNUMERO+"="+numero+
							 " AND "+ScsAsistenciasBean.C_IDINSTITUCION+"="+idInstitucion+
							 " AND "+ScsAsistenciasBean.C_EJGIDTIPOEJG+"="+idTipo;
									
	            if (rc.find(sql)) {
	               if (rc.size()== 1){
	                  Row fila = (Row) rc.get(0);
	                  datos = fila.getRow();	                  
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una asistencia EJG.");
	       }
	       return datos;                        
	 }

	
public  List<ScsAsistenciasBean> getAsistenciasVolantesExpres(VolantesExpressVo volanteExpres) 
		throws ClsExceptions{
			
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		
			
			String camposOrigen[] = this.getCamposBean();
			String campos[] = new String [camposOrigen.length+6];
			campos[this.getCamposBean().length]   = "TO_CHAR(" + ScsAsistenciasBean.C_FECHAHORA + ", 'hh24') HORA";
			campos[this.getCamposBean().length+1] = "TO_CHAR(" + ScsAsistenciasBean.C_FECHAHORA + ", 'mi') MINUTO";
			campos[this.getCamposBean().length+2] = ScsPersonaJGBean.C_NOMBRE;
			campos[this.getCamposBean().length+3] = ScsPersonaJGBean.C_APELLIDO1;
			campos[this.getCamposBean().length+4] = ScsPersonaJGBean.C_APELLIDO2;
			campos[this.getCamposBean().length+5] = ScsPersonaJGBean.C_NIF;
			
			String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
			for (int i = 0; i < camposOrigen.length; campos[i] = ScsAsistenciasBean.T_NOMBRETABLA + "." + camposOrigen[i], i++);

			

			StringBuffer sql =  new StringBuffer();
			sql.append(", "); 
			sql.append(ScsPersonaJGBean.T_NOMBRETABLA); 
			sql.append( " P ");
			sql.append(" WHERE ");
			sql.append(ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append( "."); 
			sql.append( ScsAsistenciasBean.C_IDINSTITUCION); 
			sql.append( " = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
			sql.append(" AND " );
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append( "."); 
			sql.append( ScsAsistenciasBean.C_IDTURNO); 
			sql.append( " = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdTurno());
			sql.append(" AND "); 
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append( "."); 
			sql.append( ScsAsistenciasBean.C_IDGUARDIA); 
			sql.append( " = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdGuardia());
			sql.append(" AND "); 
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append("."); 
			sql.append( ScsAsistenciasBean.C_IDPERSONACOLEGIADO); 
			sql.append( " = :");
			contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdColegiado());

		
			
			sql.append(" AND trunc("); 
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append( ".");
			sql.append(ScsAsistenciasBean.C_FECHAHORA); 
			sql.append( ") = :");
			contador ++;
		    //htCodigos.put(new Integer(contador),volanteExpres.getFechaGuardia());
			htCodigos.put(new Integer(contador),truncFechaGuardia);
			
		    sql.append(contador);
			
			sql.append(" AND P."); 
			sql.append( ScsPersonaJGBean.C_IDPERSONA); 
			sql.append( " = "); 
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append( ".");
			sql.append( ScsAsistenciasBean.C_IDPERSONAJG);	
			sql.append(" AND P."); 
			sql.append( ScsPersonaJGBean.C_IDINSTITUCION); 
			sql.append( " = "); 
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
			sql.append( "."); 
			sql.append( ScsAsistenciasBean.C_IDINSTITUCION); 
			if(!volanteExpres.getLugar().equals("centro")){
				
				sql.append(" AND EXISTS  (SELECT 1 FROM SCS_ACTUACIONASISTENCIA AA ");
				sql.append(" WHERE AA.IDINSTITUCION=SCS_ASISTENCIA.IDINSTITUCION ");
				sql.append(" AND AA.ANIO = SCS_ASISTENCIA.ANIO ");
				sql.append(" AND AA.NUMERO= SCS_ASISTENCIA.NUMERO ");
				sql.append(" AND AA.IDJUZGADO IS NOT NULL ");
				sql.append(" ) ");
				
				
//				sql.append(" AND "); 
//				sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
//				sql.append( "."); 
//				sql.append( ScsAsistenciasBean.C_JUZGADO); 
//				sql.append( " IS NOT NULL ");
			}else{
				sql.append(" AND EXISTS  (SELECT 1 FROM SCS_ACTUACIONASISTENCIA AA ");
				sql.append(" WHERE AA.IDINSTITUCION=SCS_ASISTENCIA.IDINSTITUCION ");
				sql.append(" AND AA.ANIO = SCS_ASISTENCIA.ANIO ");
				sql.append(" AND AA.NUMERO= SCS_ASISTENCIA.NUMERO ");
				sql.append(" AND AA.IDCOMISARIA IS NOT NULL ");
				sql.append(" ) ");
				
//				sql.append(" AND "); 
//				sql.append( ScsAsistenciasBean.T_NOMBRETABLA); 
//				sql.append( "."); 
//				sql.append( ScsAsistenciasBean.C_COMISARIA); 
//				sql.append( " IS NOT NULL ");

			}
			if(volanteExpres.getIdTipoAsistencia() != null){
				sql.append(" AND "); 
				sql.append( ScsAsistenciasBean.T_NOMBRETABLA);
				sql.append( "."); 
				sql.append( ScsAsistenciasBean.C_IDTIPOASISTENCIA); 
				sql.append( " = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),volanteExpres.getIdTipoAsistencia());
			}
			if(volanteExpres.getIdTipoAsistenciaColegio()!=null){
				sql.append(" AND " + ScsAsistenciasBean.T_NOMBRETABLA); 
				sql.append( "."); 
				sql.append( ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO); 
				sql.append(" = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),volanteExpres.getIdTipoAsistenciaColegio());
			}


			sql.append(" ORDER 	BY ");
			sql.append( ScsAsistenciasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(ScsAsistenciasBean.C_ANIO);
			sql.append(" asc, ");
			sql.append(ScsAsistenciasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(ScsAsistenciasBean.C_NUMERO); 
			sql.append(" ASC");//ordenamos por orden de inserccion en base de datos
				
			
			ScsEJGAdm ejgAdm = new ScsEJGAdm(this.usrbean);
			ScsDelitosAsistenciaAdm delitoAsisAdm = new ScsDelitosAsistenciaAdm(this.usrbean);
			List<ScsAsistenciasBean> alAsistencias = null;
			try {
				String sqlCampo = UtilidadesBDAdm.sqlSelect(ScsAsistenciasBean.T_NOMBRETABLA, campos);
				sql.insert(0,sqlCampo);
				RowsContainer rc = new RowsContainer(); 
													
	            if (rc.findBind(sql.toString(),htCodigos)) {
	            	alAsistencias = new ArrayList<ScsAsistenciasBean>();
	            	ScsAsistenciasBean asistenciaBean = null;
	    			for (int i = 0; i < rc.size(); i++){
	            		Row fila = (Row) rc.get(i);
	            		Hashtable<String, Object> htFila=fila.getRow();
	            		
	            		
	            		asistenciaBean = (ScsAsistenciasBean)this.hashTableToBean(htFila) ;
	            		asistenciaBean.setHora((String)htFila.get("HORA"));
	            		asistenciaBean.setMinuto((String)htFila.get("MINUTO"));
	            		asistenciaBean.setAsistidoNif((String)htFila.get("NIF"));
	            		asistenciaBean.setAsistidoNombre((String)htFila.get("NOMBRE"));
	            		asistenciaBean.setAsistidoApellido1((String)htFila.get("APELLIDO1"));
	            		asistenciaBean.setAsistidoApellido2((String)htFila.get("APELLIDO2"));
	            		asistenciaBean.setObservaciones((String)htFila.get("OBSERVACIONES"));
	            		asistenciaBean.setDelitosImputados((String)htFila.get("DELITOSIMPUTADOS"));
	            		Vector vDelitos = delitoAsisAdm.getDelitosAsitencia(asistenciaBean.getIdInstitucion()
								,asistenciaBean.getAnio(),asistenciaBean.getNumero(),null);
						if(vDelitos!=null && vDelitos.size()>0){
							Hashtable htDelitoAsistencia = (Hashtable)vDelitos.get(0);
							asistenciaBean.setIdDelito(new Integer((String)htDelitoAsistencia.get("IDDELITO")));
							
						}else{
							asistenciaBean.setIdDelito(null);
							
						}
						
						if(htFila.get(ScsAsistenciasBean.C_EJGANIO)!=null && !((String)htFila.get(ScsAsistenciasBean.C_EJGANIO)).equals("")){
							Hashtable htEjg = new Hashtable();
							htEjg.put(ScsEJGBean.C_IDINSTITUCION, (String)htFila.get(ScsAsistenciasBean.C_IDINSTITUCION));
							htEjg.put(ScsEJGBean.C_IDTIPOEJG, (String)htFila.get(ScsAsistenciasBean.C_EJGIDTIPOEJG));
							htEjg.put(ScsEJGBean.C_ANIO, (String)htFila.get(ScsAsistenciasBean.C_EJGANIO));
							htEjg.put(ScsEJGBean.C_NUMERO, (String)htFila.get(ScsAsistenciasBean.C_EJGNUMERO));
							Vector vEjg =  ejgAdm.selectByPK(htEjg);
							ScsEJGBean beanEjg = (ScsEJGBean)vEjg.get(0);
							asistenciaBean.setEjgNumEjg(beanEjg.getAnio()+"/"+beanEjg.getNumEJG());
							
							
						}
						
						
						
	            		alAsistencias.add(asistenciaBean);
	            	}
	            }else{
	            	alAsistencias = new ArrayList<ScsAsistenciasBean>();
	            } 
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
			
		
		return alAsistencias;
	}
	public void insertarAsistenciasVolanteExpress(VolantesExpressVo volantesExpressVo)throws ClsExceptions{
		ScsAsistenciasBean asistencia = null;
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = null;
		ScsActuacionAsistenciaAdm actAdm = null;
		boolean isInsertar = false;
		List<ScsAsistenciasBean> alAsistencias = (List<ScsAsistenciasBean>) volantesExpressVo.getAsistencias(); 
		int size = alAsistencias.size();
		ScsAsistenciasBean asistenciaBBDD = null;
		for (int i = 0; i < size; i++ ) {
			
			if(actAdm==null)
				actAdm = new ScsActuacionAsistenciaAdm (volantesExpressVo.getUsrBean());
			
			asistencia = (ScsAsistenciasBean)alAsistencias.get(i);
			isInsertar = asistencia.getAnio()==null;
			// Recuperamos los datos de la asistencia e inserta al asistido si no esta
			
			if (asistencia.getIdPersonaJG() == null) { 
				ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm (volantesExpressVo.getUsrBean());
				ScsPersonaJGBean p = new ScsPersonaJGBean();
				p.setApellido1(asistencia.getAsistidoApellido1());
				p.setApellido2(asistencia.getAsistidoApellido2());
				p.setIdInstitucion(new Integer(asistencia.getIdInstitucion()));
				p.setNif(asistencia.getAsistidoNif());
				p.setNombre(asistencia.getAsistidoNombre());
				p.setTipo(ClsConstants.TIPO_PERSONA_FISICA); 						// persona fisica
				//Comento esto. Esta validacion es correcta. si se quiere identificar el tipo de identificacion
				//dado un numero
				//El metodo en Validaciones tambien esta comentado. si se quiere uar descomentarlo
				// int tipoIdentificacion = Validaciones.getTipoIdentificacion(asistencia.getAsistidoNif());
				//p.setTipoIdentificacion(""+tipoIdentificacion);	// otro
				p.setTipoIdentificacion(""+ClsConstants.TIPO_IDENTIFICACION_OTRO);
				personaAdm.prepararInsert(p);
				personaAdm.insert(p);
				asistencia.setIdPersonaJG(p.getIdPersona());
			}
			
			boolean isAsistenciaModificada = false;
			if (isInsertar){
				String truncFechaGuardia = GstDate.getFormatedDateShort("",volantesExpressVo.getFechaGuardia());
				String anio = truncFechaGuardia.split("/")[2];
				asistencia.setAnio(new Integer(anio));
				asistencia.setNumero(new Integer(this.getNumeroAsistencia(asistencia.getIdInstitucion().toString(), Integer.parseInt(anio))));
				this.insert(asistencia);
				if(cabeceraGuardiasAdm==null)
					cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm (volantesExpressVo.getUsrBean());
				//Validamos todas la cabecera de guardia a instancias de LP
				cabeceraGuardiasAdm.validacionCabeceraAsistencia(asistencia.getIdInstitucion(), asistencia.getAnio(), asistencia.getNumero(), true);
			}
			else {
				Hashtable htAsistencia = this.beanToHashTable(asistencia);
				asistenciaBBDD = (ScsAsistenciasBean) ((Vector)selectByPK(htAsistencia)).get(0);
				isAsistenciaModificada = isAsistenciaVolanteExpressModificada(asistencia,asistenciaBBDD);
				if(isAsistenciaModificada){
					updateAsistenciaVolanteExpress(asistencia);
				}
			}
			ScsDelitosAsistenciaBean delitoAsistencia = null;
			if(asistencia.getIdDelito()!=null){
				delitoAsistencia = new ScsDelitosAsistenciaBean();
				delitoAsistencia.setIdDelito(asistencia.getIdDelito());
				delitoAsistencia.setIdInstitucion(asistencia.getIdInstitucion());
				delitoAsistencia.setNumero(asistencia.getNumero());
				delitoAsistencia.setAnio(asistencia.getAnio());
			}
			ScsDelitosAsistenciaAdm delAsisAdm =  null;
			if (isInsertar){
				//Insertamos el delito
				if(delitoAsistencia!=null){
					delAsisAdm =  new ScsDelitosAsistenciaAdm(volantesExpressVo.getUsrBean());
					delAsisAdm.insert(delitoAsistencia);
				}
				
			}else{
				if(isAsistenciaModificada){
					delAsisAdm =  new ScsDelitosAsistenciaAdm(volantesExpressVo.getUsrBean());
					//Borramos los delitos que tuviera
					delAsisAdm.borrarDelitosAsitencia(asistencia.getIdInstitucion(), asistencia.getAnio(), asistencia.getNumero());
					//si tiene delito lo insertamos
					if(delitoAsistencia!=null){
						
						delAsisAdm.insert(delitoAsistencia);
					}
				}
				
				
			}
			
			ScsActuacionAsistenciaBean act = new ScsActuacionAsistenciaBean ();
			
			
			
			act.setAcuerdoExtrajudicial(new Integer(0));
			act.setAnio(asistencia.getAnio());
			act.setDescripcionBreve("("+UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(), "menu.justiciaGratuita.volantesExpres")+")");
			act.setDiaDespues("N");
			act.setFecha(asistencia.getFechaHora());
			act.setFechaJustificacion(volantesExpressVo.getFechaJustificacion());
			if(volantesExpressVo.getFechaJustificacion()!=null && !volantesExpressVo.getFechaJustificacion().equals(""))
				act.setValidada("1"); // validada si
			else
				act.setValidada("0"); // validada no
			if(isInsertar)
				act.setIdActuacion(new Long(1));
			act.setIdInstitucion(asistencia.getIdInstitucion());
			act.setIdTipoAsistencia(asistencia.getIdTipoAsistenciaColegio());
			act.setNumero(new Long(""+asistencia.getNumero()));
			
			
			act.setObservaciones(asistencia.getObservaciones());
			 

			if (asistencia.getJuzgado()!=null) {
				act.setIdJuzgado(new Integer(""+asistencia.getJuzgado()));
				act.setIdInstitucionJuzgado(new Long(""+asistencia.getJuzgadoIdInstitucion()));
				act.setIdTipoActuacion(new Integer(2));
				act.setNumeroAsunto(asistencia.getNumeroProcedimiento());
			} 
			else {
				act.setNumeroAsunto(asistencia.getNumeroDiligencia());
				act.setIdComisaria(new Integer(""+asistencia.getComisaria()));
				act.setIdInstitucionComisaria(new Long(""+asistencia.getComisariaIdInstitucion()));
				act.setIdTipoActuacion(new Integer(1));
			}
			
			if (isInsertar){
				actAdm.insert(act);
			}
			else {
				
				try {
					if(isActuacionVolanteExpressModificada(asistencia,asistenciaBBDD,act,actAdm,volantesExpressVo.getLugar()))
						actAdm.updateActuacionVolanteExpress(act);
				} catch (SIGAException e) {
					
					
					String msg = volantesExpressVo.getMsgAviso()!=null?volantesExpressVo.getMsgAviso():UtilidadesString.getMensajeIdioma(volantesExpressVo.getUsrBean(),"gratuita.volantesExpres.mensaje.aviso.actuacionesNoActualizadas");
					msg += "\\n"+asistencia.getAnio()+"/"+asistencia.getNumero();
					volantesExpressVo.setMsgAviso(msg);
				}	
			}
		}
		
	}
	
	public void  borrarAsistenciasVolanteExpres(List alAsistenciasBorrar,UsrBean usr) throws ClsExceptions{
		ScsAsistenciasBean asistencia = null;
		if(alAsistenciasBorrar!=null){
			for (int i = 0; i < alAsistenciasBorrar.size(); i++ ) {
				asistencia = (ScsAsistenciasBean) alAsistenciasBorrar.get(i);
				borrarAsistenciaVolanteExpress(asistencia.getAnio(), asistencia.getNumero(), asistencia.getIdInstitucion(),usr);
				
			}
		}
	}
	public ScsAsistenciasBean getAsistenciaVolanteExpress (String anio, String numero, String idInstitucion) throws ClsExceptions,SIGAException {
		ScsAsistenciasBean beanAsistencia = null;
	       try {
	    	   Hashtable miHash = new Hashtable();
	   			miHash.put("IDINSTITUCION",idInstitucion);
	   			miHash.put("ANIO",anio);
	   			miHash.put("NUMERO",numero);
	    	    Vector resultadoObj = this.selectByPK(miHash);
	   		    beanAsistencia = (ScsAsistenciasBean)resultadoObj.get(0);
	   		    miHash.clear();
	   		    miHash.put("IDINSTITUCION",idInstitucion);
	   		    miHash.put("IDTURNO",beanAsistencia.getIdTurno());
	   		    ScsTurnoAdm admTurno = new ScsTurnoAdm(usrbean);
	   		    resultadoObj = admTurno.selectByPK(miHash);
	   		    ScsTurnoBean turno = (ScsTurnoBean)resultadoObj.get(0);
	   		    beanAsistencia.setTurno(turno);
	   		    miHash.clear();
	   		    miHash.put("IDINSTITUCION",idInstitucion);
	   		    miHash.put("IDTURNO",beanAsistencia.getIdTurno());
	   		    miHash.put("IDGUARDIA",beanAsistencia.getIdGuardia());
	   		    ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usrbean);
	   		    resultadoObj = admGuardias.selectByPK(miHash);
	   		    ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean)resultadoObj.get(0);
	   		    beanAsistencia.setGuardia(guardia);
	   		    miHash.clear();
	   		    miHash.put("IDINSTITUCION",idInstitucion);
	   		    miHash.put("IDPERSONA",beanAsistencia.getIdPersonaJG());
	   		    ScsPersonaJGAdm admPersonaJG = new ScsPersonaJGAdm(usrbean);
	   		    resultadoObj = admPersonaJG.selectByPK(miHash);
	   		    ScsPersonaJGBean asistido = (ScsPersonaJGBean)resultadoObj.get(0);
	   		    beanAsistencia.setAsistido(asistido);
//	   		    miHash.clear();
//	   		    miHash.put("IDINSTITUCION",idInstitucion);
//	   		    miHash.put("IDPERSONA",beanAsistencia.getIdPersonaColegiado());
	   		    CenPersonaAdm admPersona = new CenPersonaAdm(usrbean);
	   		    CenPersonaBean colegiado =  admPersona.getPersonaColegiado(new Long(beanAsistencia.getIdPersonaColegiado().toString()),new Integer(idInstitucion));
	   		    beanAsistencia.setPersonaColegiado(colegiado);
	   		    
	   		    if(beanAsistencia.getDesignaAnio()!=null ){
	   		    	miHash.clear();
		   		    miHash.put("IDINSTITUCION",idInstitucion);
		   		    miHash.put("IDTURNO",beanAsistencia.getDesignaTurno());
		   		    miHash.put("NUMERO",beanAsistencia.getDesignaNumero());
		   		    miHash.put("ANIO",beanAsistencia.getDesignaAnio());
		   		    ScsDesignaAdm admDesigna = new ScsDesignaAdm(usrbean);
		   		    resultadoObj = admDesigna.selectByPK(miHash);
		   		    ScsDesignaBean designa = (ScsDesignaBean)resultadoObj.get(0);
		   		    beanAsistencia.setDesigna(designa);
		   		    miHash.clear();
		   		    miHash.put("IDINSTITUCION",idInstitucion);
		   		    miHash.put("IDTURNO",designa.getIdTurno());
		   		    resultadoObj = admTurno.selectByPK(miHash);
		   		    ScsTurnoBean turnoDesigna = (ScsTurnoBean)resultadoObj.get(0);
		   		    designa.setTurno(turno);
		   		    String idPersonacolegiadoDesignado = admDesigna.getIDLetradoDesig(idInstitucion, designa.getIdTurno().toString(), designa.getAnio().toString(), designa.getNumero().toString());
		   		    CenPersonaBean colegiadoDesignado =  admPersona.getPersonaColegiado(new Long(idPersonacolegiadoDesignado),new Integer(idInstitucion));
		   		    designa.setColegiadoDesignado(colegiadoDesignado);
		   		    
	   		    }
	   		    if(beanAsistencia.getEjgAnio()!=null ){
	   		    	miHash.clear();
		   		    miHash.put("IDINSTITUCION",idInstitucion);
		   		    miHash.put("IDTIPOEJG",beanAsistencia.getEjgIdTipoEjg());
		   		    miHash.put("NUMERO",beanAsistencia.getEjgNumero());
		   		    miHash.put("ANIO",beanAsistencia.getEjgAnio());
		   		    ScsEJGAdm admEJG = new ScsEJGAdm(usrbean);
		   		    resultadoObj = admEJG.selectByPK(miHash);
		   		    ScsEJGBean ejg = (ScsEJGBean)resultadoObj.get(0);
		   		    beanAsistencia.setEjg(ejg);
		   		    miHash.clear();
		   		    miHash.put("IDINSTITUCION",idInstitucion);
		   		    miHash.put("IDPERSONA",ejg.getIdPersonaJG());
		   		    resultadoObj = admPersonaJG.selectByPK(miHash);
		   		    ScsPersonaJGBean solicitante = (ScsPersonaJGBean)resultadoObj.get(0);
		   		    ejg.setSolicitante(solicitante);
		   		    ScsTipoEJGBean tipoEjg =  admEJG.getTipoEjg(ejg.getIdTipoEJG().toString());
		   		    ejg.setTipoEjg(tipoEjg);
		   		    
	   		    }
	   		    
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una asistencia EJG.");
	       }
	       return beanAsistencia;                        
	 }
	private void borrarAsistenciaVolanteExpress(Integer anio, Integer numero, Integer idInstitucion, UsrBean usr) throws ClsExceptions 
	{
		Hashtable<String,Object> claves = new Hashtable<String, Object> ();
		UtilidadesHash.set (claves, ScsAsistenciasBean.C_ANIO, anio);
		UtilidadesHash.set (claves, ScsAsistenciasBean.C_NUMERO, numero);
		UtilidadesHash.set (claves, ScsAsistenciasBean.C_IDINSTITUCION, idInstitucion);
		String campos [] = {ScsAsistenciasBean.C_ANIO, ScsAsistenciasBean.C_NUMERO, ScsAsistenciasBean.C_IDINSTITUCION};
		ScsActuacionAsistCosteFijoAdm costeActuacionAdm = new ScsActuacionAsistCosteFijoAdm (usr);
		costeActuacionAdm.deleteDirect(claves, campos);
		ScsActuacionAsistenciaAdm actuacionAdm = new ScsActuacionAsistenciaAdm (usr);
		actuacionAdm.deleteDirect(claves, campos);
		ScsDelitosAsistenciaAdm delitosAsistenciaAdm = new ScsDelitosAsistenciaAdm (usr);
		delitosAsistenciaAdm.borrarDelitosAsitencia(idInstitucion,anio,numero);
		this.delete(claves);
	}
	private boolean isAsistenciaVolanteExpressModificada(ScsAsistenciasBean asistencia,ScsAsistenciasBean asistenciaBBDD){
		
		String fechaHora = asistencia.getFechaHora()==null ? "" : asistencia.getFechaHora();
		boolean isAsistenciaModificada = !((fechaHora==null && asistenciaBBDD.getFechaHora()==null)
		|| (fechaHora!=null && asistenciaBBDD.getFechaHora()!=null && fechaHora.trim().equalsIgnoreCase(asistenciaBBDD.getFechaHora().trim())));

		if(asistencia.getComisaria()!=null){
		if(isAsistenciaModificada) return true;
			isAsistenciaModificada = !((asistencia.getComisaria()==null && asistenciaBBDD.getComisaria()==null)
					|| (asistencia.getComisaria()!=null && asistencia.getComisaria()!=null && asistencia.getComisaria().toString().trim().equalsIgnoreCase(asistenciaBBDD.getComisaria().toString().trim())));
			if(isAsistenciaModificada) return true;
			String numDiligencia = asistencia.getNumeroDiligencia()==null ? "" : asistencia.getNumeroDiligencia();
			isAsistenciaModificada = !((numDiligencia==null && asistenciaBBDD.getNumeroDiligencia()==null)
					|| (numDiligencia!=null && asistenciaBBDD.getNumeroDiligencia()!=null && numDiligencia.trim().equalsIgnoreCase(asistenciaBBDD.getNumeroDiligencia().trim())));
			
			//mhg - INC_09104_SIGA
			String idDelito = asistencia.getIdDelito()==null ? "" : asistencia.getIdDelito().toString();
			isAsistenciaModificada = !((idDelito==null && asistenciaBBDD.getIdDelito()==null)
					|| (idDelito!=null && asistenciaBBDD.getIdDelito()!=null));
			
			if(isAsistenciaModificada) return true;
		}else{
		
			isAsistenciaModificada = !((asistencia.getJuzgado()==null && asistenciaBBDD.getJuzgado()==null)
					|| (asistencia.getJuzgado()!=null && asistenciaBBDD.getJuzgado()!=null && asistencia.getJuzgado().toString().trim().equalsIgnoreCase(asistenciaBBDD.getJuzgado().toString().trim())));
			if(isAsistenciaModificada) return true;
			String numProcedimiento = asistencia.getNumeroProcedimiento()==null ? "" : asistencia.getNumeroProcedimiento();
			isAsistenciaModificada = !((numProcedimiento==null && asistenciaBBDD.getNumeroProcedimiento()==null)
					|| (numProcedimiento!=null && asistenciaBBDD.getNumeroProcedimiento()!=null && numProcedimiento.trim().equalsIgnoreCase(asistenciaBBDD.getNumeroProcedimiento().trim())));
			if(isAsistenciaModificada) return true;
		}
		
		
		

		isAsistenciaModificada = !((asistencia.getIdPersonaJG()==null && asistenciaBBDD.getIdPersonaJG()==null)
				|| (asistencia.getIdPersonaJG()!=null && asistenciaBBDD.getIdPersonaJG()!=null && asistencia.getIdPersonaJG().toString().trim().equalsIgnoreCase(asistenciaBBDD.getIdPersonaJG().toString().trim())));
		if(isAsistenciaModificada) return true;
	
		
		return isAsistenciaModificada;
		
		
	}
	private void updateAsistenciaVolanteExpress(ScsAsistenciasBean asistencia) throws ClsExceptions 
	{
		String claves [] ={ScsAsistenciasBean.C_ANIO,ScsAsistenciasBean.C_NUMERO, ScsAsistenciasBean.C_IDINSTITUCION};
		String campos [] = new String[6];
		campos[0] = ScsAsistenciasBean.C_FECHAHORA;
		campos[1] =  ScsAsistenciasBean.C_JUZGADO;
		campos[2] =  ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION;
		campos[3] =  ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO;
		campos[4] =  ScsAsistenciasBean.C_DELITOSIMPUTADOS;
		campos[5] =  ScsAsistenciasBean.C_IDPERSONAJG;
		if(asistencia.getComisaria()!=null){
			campos[1] =  ScsAsistenciasBean.C_COMISARIA;
			campos[2] =  ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION;
			campos[3] =  ScsAsistenciasBean.C_NUMERODILIGENCIA;
		}
			
		this.updateDirect(asistencia,claves,campos);
	}
	private boolean isActuacionVolanteExpressModificada(ScsAsistenciasBean asistencia,ScsAsistenciasBean asistenciaBBDD,ScsActuacionAsistenciaBean actuacion, ScsActuacionAsistenciaAdm actAdm, String lugar) throws SIGAException, ClsExceptions{
		boolean isActuacionModificada = false;
		
		//Hashtable htActuacion = actAdm.beanToHashTable(actuacion);
		List<ScsActuacionAsistenciaBean> listActuacionesBBDD = actAdm.getActuacionesAsistenciaVolantesExpres(asistencia,lugar);
		//Si hay ms de una actuacion para esta asistencia de l tipo de lugar del formulario lanzamos excepcion,
		//para mostrar mensaje de aiso de que no e han podido modificar
		
		ScsActuacionAsistenciaBean actuacionBBDD = null;
		if(listActuacionesBBDD!=null&&listActuacionesBBDD.size()>1)
				throw new SIGAException("Hay mas de una actuacion de ese tipo, no se modifica ninguna");
		actuacionBBDD = listActuacionesBBDD.get(0);
		actuacion.setIdActuacion(actuacionBBDD.getIdActuacion());
		isActuacionModificada = !((actuacion.getFechaJustificacion()==null && actuacionBBDD.getFechaJustificacion()==null)
				|| (actuacion.getFechaJustificacion()!=null && actuacionBBDD.getFechaJustificacion()!=null && actuacion.getFechaJustificacion().trim().equalsIgnoreCase(actuacionBBDD.getFechaJustificacion().trim())));
		if(isActuacionModificada) return true;
		
		
		if(asistencia.getComisaria()!=null){
		
			isActuacionModificada = !((asistencia.getComisaria()==null && asistenciaBBDD.getComisaria()==null)
					|| (asistencia.getComisaria()!=null && asistenciaBBDD.getComisaria()!=null && asistencia.getComisaria().toString().trim().equalsIgnoreCase(asistenciaBBDD.getComisaria().toString().trim())));
			if(isActuacionModificada) return true;
		}else{
			isActuacionModificada = !((asistencia.getJuzgado()==null && asistenciaBBDD.getJuzgado()==null)
					|| (asistencia.getJuzgado()!=null && asistenciaBBDD.getJuzgado()!=null && asistencia.getJuzgado().toString().trim().equalsIgnoreCase(asistenciaBBDD.getJuzgado().toString().trim())));
			if(isActuacionModificada) return true;
		}
		
		String numDiligencia = asistencia.getNumeroDiligencia()==null ? "" : asistencia.getNumeroDiligencia();
		isActuacionModificada = !((numDiligencia==null && asistenciaBBDD.getNumeroDiligencia()==null)
				|| (numDiligencia!=null && asistenciaBBDD.getNumeroDiligencia()!=null && numDiligencia.trim().equalsIgnoreCase(asistenciaBBDD.getNumeroDiligencia().trim())));
		if(isActuacionModificada) return true;
		
		
		
		return isActuacionModificada;
		
		
	}
	
	public void updateAsistenciaDesdeActuacion(ScsAsistenciasBean asistencia) throws ClsExceptions 
	{
		String claves [] ={ScsAsistenciasBean.C_ANIO,ScsAsistenciasBean.C_NUMERO, ScsAsistenciasBean.C_IDINSTITUCION};
		ArrayList<String> alCampos = new ArrayList<String>();
		if(asistencia.getComisaria()!=null){
			 alCampos.add(ScsAsistenciasBean.C_COMISARIA);
			 alCampos.add(ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION);
			 alCampos.add(ScsAsistenciasBean.C_NUMERODILIGENCIA);
			
		}
		if(asistencia.getJuzgado()!=null){
			alCampos.add(ScsAsistenciasBean.C_JUZGADO);
			alCampos.add(ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION);
			alCampos.add(ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO);
			
			
		}
		
		if(asistencia.getNIG()!=null){
			alCampos.add(ScsAsistenciasBean.C_NIG);
			//Se comprueba que no este ya relleno para no sobreescribirlo
			Vector v = this.select(this.beanToHashTable(asistencia));
			ScsAsistenciasBean bean = (ScsAsistenciasBean) v.get(0);
			if(bean != null){
				if(bean.getNIG() != null && !bean.getNIG().equals("")){
					asistencia.setNIG(bean.getNIG());
				}
			}
		}

		this.updateDirect(asistencia,claves,alCampos.toArray(new String[alCampos.size()]));
	}
	
	
	public AsistenciaForm getDatosAsistencia(ScsAsistenciasBean asistencia) throws ClsExceptions 
	{
		StringBuffer sql =  new StringBuffer();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		sql.append(" SELECT AA.ANIO||'/'||AA.NUMERO||DECODE(AA.IDPERSONAJG,null,null,' - '||PJG.NOMBRE ||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2) DESCRIPCIONASISTENCIA ");
		sql.append(" ,AA.FECHAANULACION, AA.IDTIPOASISTENCIACOLEGIO,AA.IDTURNO,AA.FECHAHORA ");
		sql.append(" ,AA.NUMERODILIGENCIA, AA.NUMEROPROCEDIMIENTO ");
		sql.append(" ,AA.COMISARIA, AA.JUZGADO, AA.NIG ");
		
		sql.append(",TU.LETRADOACTUACIONES,TU.VALIDARJUSTIFICACIONES ");
		sql.append(",TU.NOMBRE NOMBRETURNO, ");
		sql.append("GU.NOMBRE NOMBREGUARDIA, ");
		
		
		sql.append("PJG.NIF PJGNIF, ");
		sql.append("PJG.NOMBRE PJGNOMBRE, ");
		sql.append("PJG.APELLIDO1 PJGAPELLIDO1, ");
		sql.append("PJG.APELLIDO2 PJGAPELLIDO2, ");
		sql.append("COL.NCOLEGIADO CNCOLEGIADO, ");
		sql.append("PER.NOMBRE PNOMBRE, ");
		sql.append("PER.APELLIDOS1 PAPELLIDOS1, ");
		sql.append("PER.APELLIDOS2 PAPELLIDOS2 ");

		sql.append(" FROM "); 
		sql.append(" SCS_ASISTENCIA AA, SCS_TURNO TU,SCS_GUARDIASTURNO GU, SCS_PERSONAJG PJG,CEN_PERSONA PER, CEN_COLEGIADO COL ");
		sql.append(" WHERE " );
		sql.append(" AA.IDINSTITUCION = COL.IDINSTITUCION " );
		sql.append(" AND AA.IDPERSONACOLEGIADO = COL.IDPERSONA " );
		sql.append(" AND COL.IDPERSONA = PER.IDPERSONA " );
		sql.append(" AND GU.IDTURNO(+) = AA.IDTURNO " );
		sql.append(" AND GU.IDGUARDIA(+) = AA.IDGUARDIA " );

		sql.append(" AND PJG.IDPERSONA(+) = AA.IDPERSONAJG ");
		sql.append(" AND PJG.IDINSTITUCION(+) = AA.IDINSTITUCION ");
		sql.append(" AND TU.IDTURNO(+) = AA.IDTURNO ");
		sql.append(" AND TU.IDINSTITUCION(+) = AA.IDINSTITUCION ");
		sql.append("AND  AA.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getIdInstitucion());
		sql.append(" AND AA.ANIO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getAnio());
		sql.append(" AND AA.NUMERO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),asistencia.getNumero());

		AsistenciaForm	asistenciaForm = null;
		
		try {
			RowsContainer rc = new RowsContainer(); 
			if (rc.findBind(sql.toString(),htCodigos)) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					Hashtable<String, Object> htFila=fila.getRow();
					 
					asistenciaForm = asistencia.getAsistenciaForm();
					DefinirTurnosForm turno = new DefinirTurnosForm();
					DefinirGuardiasTurnosForm guardia = new DefinirGuardiasTurnosForm();
					PersonaJGForm personaJG = new PersonaJGForm();
					CenPersonaBean persona = new CenPersonaBean();
					CenColegiadoBean colegiado = new CenColegiadoBean();
					persona.setColegiado(colegiado);
//					asistenciaForm.setIdInstitucion(idInstitucion);
//					asistenciaForm.setAnio(idInstitucion);
//					asistenciaForm.setNumero(idInstitucion);
					asistenciaForm.setTurno(turno);
					asistenciaForm.setGuardia(guardia);
					asistenciaForm.setPersonaColegiado(persona);
					asistenciaForm.setPersonaJG(personaJG);
					asistenciaForm.setIdTipoAsistenciaColegio(UtilidadesHash.getString(htFila, "IDTIPOASISTENCIACOLEGIO"));
					asistenciaForm.setIdTurno(UtilidadesHash.getString(htFila, "IDTURNO"));
					asistenciaForm.setDescripcion(UtilidadesHash.getString(htFila, "DESCRIPCIONASISTENCIA"));
					asistenciaForm.setFechaAnulacion(UtilidadesHash.getString(htFila, "FECHAANULACION"));
					asistenciaForm.setLetradoActuaciones(UtilidadesHash.getString(htFila, "LETRADOACTUACIONES"));
					asistenciaForm.setFechaHora(GstDate.getFormatedDateShort("", UtilidadesHash.getString(htFila, "FECHAHORA")) );
					asistenciaForm.setNumeroDiligencia(UtilidadesHash.getString(htFila, "NUMERODILIGENCIA"));
					asistenciaForm.setNumeroProcedimiento(UtilidadesHash.getString(htFila, "NUMEROPROCEDIMIENTO"));
					asistenciaForm.setComisaria(UtilidadesHash.getString(htFila, "COMISARIA"));
					asistenciaForm.setJuzgado(UtilidadesHash.getString(htFila, "JUZGADO"));
					asistenciaForm.setNig(UtilidadesHash.getString(htFila, "NIG"));
					asistenciaForm.setValidarJustificaciones(UtilidadesHash.getString(htFila, "VALIDARJUSTIFICACIONES"));
					turno.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					turno.setActivarActuacionesLetrado(UtilidadesHash.getString(htFila, "LETRADOACTUACIONES"));
					turno.setValidarJustificaciones(UtilidadesHash.getString(htFila, "VALIDARJUSTIFICACIONES"));
					guardia.setNombreGuardia(UtilidadesHash.getString(htFila, "NOMBREGUARDIA"));
					personaJG.setNIdentificacion(UtilidadesHash.getString(htFila, "PJGNIF"));
					personaJG.setApellido1(UtilidadesHash.getString(htFila, "PJGAPELLIDO1"));
					personaJG.setApellido2(UtilidadesHash.getString(htFila, "PJGAPELLIDO2"));
					personaJG.setNombre(UtilidadesHash.getString(htFila, "PJGNOMBRE"));

					persona.setApellido1(UtilidadesHash.getString(htFila, "PAPELLIDOS1"));
					persona.setApellido2(UtilidadesHash.getString(htFila, "PAPELLIDOS2"));
					persona.setNombre(UtilidadesHash.getString(htFila, "PNOMBRE"));
					colegiado.setNColegiado(UtilidadesHash.getString(htFila, "CNCOLEGIADO"));
					
					
					
					
				}

			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getDatosAsistencia.");
		}

		return asistenciaForm;
	}

	
}