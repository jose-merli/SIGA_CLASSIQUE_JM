
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.vos.VolantesExpressVo;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * @author ruben.fernandez
 * @since 26/12/2004 
 */

public class ScsTurnoAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsTurnoAdm (UsrBean usuario) {
		super( ScsTurnoBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_RETENCIONES con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	protected String[] getCamposBean() {
		String[] campos = {	ScsTurnoBean.C_IDINSTITUCION,			ScsTurnoBean.C_IDTURNO, 
				ScsTurnoBean.C_NOMBRE,					ScsTurnoBean.C_ABREVIATURA,
				ScsTurnoBean.C_DESCRIPCION,				ScsTurnoBean.C_GUARDIAS,
				ScsTurnoBean.C_VALIDARJUSTIFICACIONES,	ScsTurnoBean.C_VISIBILIDAD,
				ScsTurnoBean.C_DESIGNADIRECTA,			ScsTurnoBean.C_VALIDARINSCRIPCIONES,	
				ScsTurnoBean.C_IDMATERIA,				ScsTurnoBean.C_IDZONA,
				ScsTurnoBean.C_IDSUBZONA,				ScsTurnoBean.C_IDORDENADIONCOLAS,
				ScsTurnoBean.C_IDGRUPOFACTURACION,		ScsTurnoBean.C_REQUISITOS,
				ScsTurnoBean.C_IDAREA,					ScsTurnoBean.C_IDPERSONAULTIMO,
				ScsTurnoBean.C_IDPARTIDAPRESUPUESTARIA,	ScsTurnoBean.C_USUMODIFICACION,
				ScsTurnoBean.C_FECHAMODIFICACION, 		ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT,
				ScsTurnoBean.C_LETRADOACTUACIONES,		ScsTurnoBean.C_LETRADOASISTENCIAS, 
				ScsTurnoBean.C_CODIGOEXT,				ScsTurnoBean.C_FECHASOLICITUD_ULTIMO,
				ScsTurnoBean.C_IDTIPOTURNO};
		return campos;
	}
	/**
	 * Devuelve los tablas que intervienen en la consulta, el campo from
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con las tablas del select, campo from
	 */
	
	protected String getTablasTurnos(){
		String campos=" SCS_TURNO   turnos, SCS_PARTIDAPRESUPUESTARIA partid,SCS_GRUPOFACTURACION  grupof,SCS_MATERIA       materi,SCS_AREA    area,SCS_SUBZONA    subzon,SCS_ZONA    zona,CEN_PARTIDOJUDICIAL   parjud,SCS_ORDENACIONCOLAS";
		return campos;
	}
	/**
	 * Devuelve los campos de en la consulta
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con los campos del select
	 */
	
	private String[] getCamposTurnos(){
			String[] campos = {	"turnos."+ScsTurnoBean.C_IDTURNO+" IDTURNO",								
					//"substr(turnos."+ScsTurnoBean.C_NOMBRE+ ",1,10) NOMBRE",
					"turnos."+ScsTurnoBean.C_NOMBRE+ " NOMBRE",
					"turnos."+ScsTurnoBean.C_ABREVIATURA+ " ABREVIATURA",
					"area."+ScsAreaBean.C_NOMBRE+ " AREA",
					"area."+ScsAreaBean.C_IDAREA+ " IDAREA",
					"materi."+ScsMateriaBean.C_NOMBRE+" MATERIA",
					"materi."+ScsMateriaBean.C_IDMATERIA+" IDMATERIA",
					"zona."+ScsZonaBean.C_NOMBRE+" ZONA",
					"zona."+ScsZonaBean.C_IDZONA+" IDZONA",
					"subzon."+ScsSubzonaBean.C_NOMBRE+" SUBZONA",
					"subzon."+ScsSubzonaBean.C_IDSUBZONA+" IDSUBZONA",
					"parjud."+CenPartidoJudicialBean.C_NOMBRE+" PARTIDOJUDICIAL",
					"parjud."+CenPartidoJudicialBean.C_IDPARTIDO+" IDPARTIDOJUDICIAL",
					"partid."+ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA+" PARTIDAPRESUPUESTARIA",
					"partid."+ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA+" IDPARTIDAPRESUPUESTARIA",
					UtilidadesMultidioma.getCampoMultidiomaSimple("grupof."+ScsGrupoFacturacionBean.C_NOMBRE,this.usrbean.getLanguage())+" GRUPOFACTURACION",
					"grupof."+ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION+" IDGRUPOFACTURACION",
					"turnos."+ScsTurnoBean.C_GUARDIAS+ " GUARDIAS",
					"turnos."+ScsTurnoBean.C_VALIDARJUSTIFICACIONES+ " VALIDARJUSTIFICACIONES",
					"turnos."+ScsTurnoBean.C_VALIDARINSCRIPCIONES+" VALIDARINSCRIPCIONES",
					"turnos."+ScsTurnoBean.C_DESIGNADIRECTA+" DESIGNADIRECTA",
					"turnos."+ScsTurnoBean.C_DESCRIPCION+" DESCRIPCION",
					"turnos."+ScsTurnoBean.C_REQUISITOS+" REQUISITOS",
					"turnos."+ScsTurnoBean.C_IDPERSONAULTIMO+" IDPERSONAULTIMO",
					"turnos."+ScsTurnoBean.C_FECHASOLICITUD_ULTIMO+" FECHASOLICTUDULTIMO",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+" IDORDENACIONCOLAS",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+" ALFABETICOAPELLIDOS",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_FECHANACIMIENTO+" EDAD",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+" ANTIGUEDAD",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+" ANTIGUEDADENCOLA",
					"decode(turnos."+ScsTurnoBean.C_VISIBILIDAD+", '1','Alta','Baja') AS ESTADO",
					"turnos."+ScsTurnoBean.C_IDTIPOTURNO +" IDTIPOTURNO",
					//nuevo campo para contar el numero de letrados en cola
					"(select count(*) from SCS_INSCRIPCIONTURNO INS where ins.idinstitucion=turnos.idinstitucion and ins.idturno=turnos.idturno and " +
					"(ins.FECHABAJA IS NULL OR TRUNC(ins.FECHABAJA)>TRUNC(SYSDATE))			AND (ins.FECHAVALIDACION IS NOT NULL AND TRUNC(ins.FECHAVALIDACION)<=TRUNC(SYSDATE))) " +
					"as nLetrados"};
			
			 
			return campos;
		
	}
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del turno que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDTURNO) + 1) AS IDTURNO FROM " + nombreTabla;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDTURNO").equals("")) {
					entrada.put(ScsTurnoBean.C_IDTURNO,"1");
				}
				else entrada.put(ScsTurnoBean.C_IDTURNO,(String)prueba.get("IDTURNO"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return entrada;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		String[] campos = {	ScsTurnoBean.C_IDINSTITUCION, ScsTurnoBean.C_IDTURNO};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean, con nomenclatura Tabla.Campo
	 * */
	protected String[] getClavesSelect(){
		String[] campos = { ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDINSTITUCION,
				ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDTURNO};
		return campos;
	}
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsTurnoBean bean = null;
		try{
			bean = new ScsTurnoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDTURNO));
			bean.setNombre(UtilidadesHash.getString(hash,ScsTurnoBean.C_NOMBRE));
			bean.setAbreviatura(UtilidadesHash.getString(hash,ScsTurnoBean.C_ABREVIATURA));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsTurnoBean.C_DESCRIPCION));
			bean.setGuardias(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_GUARDIAS));
			bean.setValidarJustificaciones(UtilidadesHash.getString(hash,ScsTurnoBean.C_VALIDARJUSTIFICACIONES));
			bean.setDesignaDirecta(UtilidadesHash.getString(hash,ScsTurnoBean.C_DESIGNADIRECTA));
			bean.setValidarInscripciones(UtilidadesHash.getString(hash,ScsTurnoBean.C_VALIDARINSCRIPCIONES));
			bean.setIdArea(Integer.valueOf(UtilidadesHash.getString(hash,ScsTurnoBean.C_IDAREA)));
			bean.setIdMateria(Integer.valueOf(UtilidadesHash.getString(hash,ScsTurnoBean.C_IDMATERIA)));
			bean.setIdZona(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDZONA));
			bean.setIdSubzona(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDSUBZONA));
			bean.setIdOrdenacionColas(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDORDENADIONCOLAS));
			bean.setIdPartidaPresupuestaria(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDPARTIDAPRESUPUESTARIA));
			bean.setIdGrupoFacturacion(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDGRUPOFACTURACION));
			bean.setRequisitos(UtilidadesHash.getString(hash,ScsTurnoBean.C_REQUISITOS));
			bean.setIdPersonaUltimo(UtilidadesHash.getLong(hash,ScsTurnoBean.C_IDPERSONAULTIMO));
			bean.setActivarRestriccionAcreditacion(UtilidadesHash.getString(hash,ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT));
			bean.setLetradoActuaciones(UtilidadesHash.getString(hash,ScsTurnoBean.C_LETRADOACTUACIONES));
			bean.setLetradoAsistencias(UtilidadesHash.getString(hash,ScsTurnoBean.C_LETRADOASISTENCIAS));
			bean.setCodigoExt(UtilidadesHash.getString(hash,ScsTurnoBean.C_CODIGOEXT));
			bean.setFechaSolicitudUltimo(UtilidadesHash.getString(hash,ScsTurnoBean.C_FECHASOLICITUD_ULTIMO));
			bean.setVisibilidad(UtilidadesHash.getString(hash,ScsTurnoBean.C_VISIBILIDAD));
			bean.setIdTipoTurno(UtilidadesHash.getString(hash,ScsTurnoBean.C_IDTIPOTURNO));
			
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			String aux="";
			ScsTurnoBean b = (ScsTurnoBean) bean;
			hash.put(ScsTurnoBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsTurnoBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsTurnoBean.C_NOMBRE, b.getNombre());
			hash.put(ScsTurnoBean.C_ABREVIATURA, b.getAbreviatura());
			aux = b.getDescripcion().toString();
			if (aux==null)aux="";
			UtilidadesHash.set(hash,ScsTurnoBean.C_DESCRIPCION, aux);
			hash.put(ScsTurnoBean.C_GUARDIAS, String.valueOf(b.getGuardias()));
			hash.put(ScsTurnoBean.C_VALIDARJUSTIFICACIONES, b.getValidarJustificaciones());
			hash.put(ScsTurnoBean.C_DESIGNADIRECTA,  b.getDesignaDirecta());
			hash.put(ScsTurnoBean.C_VALIDARINSCRIPCIONES,  b.getValidarInscripciones());
			hash.put(ScsTurnoBean.C_IDAREA,  String.valueOf(b.getIdArea()));
			hash.put(ScsTurnoBean.C_IDMATERIA,  String.valueOf(b.getIdMateria()));
			hash.put(ScsTurnoBean.C_IDZONA,  String.valueOf(b.getIdZona()));
			aux = String.valueOf(b.getIdSubzona());
			if (aux==null)aux="";
			hash.put(ScsTurnoBean.C_IDSUBZONA, aux);
			hash.put(ScsTurnoBean.C_IDORDENADIONCOLAS,  String.valueOf(b.getIdOrdenacionColas()));
			aux = String.valueOf(b.getIdPartidaPresupuestaria());
			if (aux==null)aux="";
			hash.put(ScsTurnoBean.C_IDPARTIDAPRESUPUESTARIA, aux);
			hash.put(ScsTurnoBean.C_IDGRUPOFACTURACION,  String.valueOf(b.getIdGrupoFacturacion()));
			aux = b.getRequisitos();
			if (aux==null)aux="";
			UtilidadesHash.set(hash,ScsTurnoBean.C_REQUISITOS, aux);
			aux = String.valueOf(b.getIdPersonaUltimo());
			if (aux==null)aux="";
			hash.put(ScsTurnoBean.C_IDPERSONAULTIMO, aux);
			UtilidadesHash.set(hash, ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, b.getActivarRestriccionAcreditacion());
			UtilidadesHash.set(hash, ScsTurnoBean.C_VISIBILIDAD, b.getVisibilidad());
			UtilidadesHash.set(hash, ScsTurnoBean.C_IDTIPOTURNO, b.getIdTipoTurno());
			UtilidadesHash.set(hash, ScsTurnoBean.C_LETRADOACTUACIONES, b.getLetradoActuaciones());
			UtilidadesHash.set(hash, ScsTurnoBean.C_LETRADOASISTENCIAS, b.getLetradoAsistencias());
			UtilidadesHash.set(hash, ScsTurnoBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(hash, ScsTurnoBean.C_FECHASOLICITUD_ULTIMO, b.getFechaSolicitudUltimo());
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
	 * Efectúa un SELECT en la tabla SCS_TURNO con los datos introducidos. 
	 * 
	 * @param hash Hashtable con los campos a insertar. De tipo "Hashtable". 
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
	 * Efectúa un SELECT en la tabla SCS_TURNO con los datos introducidos. 
	 * 
	 * @param where String la clausula where del select 
	 * @param entrada String dependiendo de este campo, el select devuelve unos campos u otros. Los posibles valores son:
	 * 		1=abreviatura,nombre, area, materia, zona, subzona, partidoJudicial,partidaPresupuestaria, grupoFacturacion 
	 * 		2=abreviatura,nombre, area, materia, zona, subzona, partidoJudicial, fechaAlta, fechaValidacion, fechaCancelacion
	 * @return Vector de Hashtable con los registros que cumplan con la clausula where 
	 */
	public Vector selectTurnos(String where){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasTurnos(), this.getCamposTurnos());
			sql += where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getOrdenSelect());
			if (rc.queryNLS(sql)) {
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
	
	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {
		String[] campos = {	ScsTurnoBean.C_NOMBRE};
		return campos;	}
	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * @param entrada String identificador de l permiso del usuario logado
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenSelect(){
		
			String[] campos={"turnos."+ScsTurnoBean.C_ABREVIATURA};
			return campos;
		
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
	}
	

	
	public Hashtable getDatosTurno(String idInstitucion, String idTurno) {
		String consulta =	" select turno.nombre nombre, turno.abreviatura abreviatura, turno.idarea idarea, turno.idmateria idmateria, turno.idzona idzona, "+
		" turno.idpartidapresupuestaria idpartidapresupuestaria, turno.idgrupofacturacion idgrupofacturacion, turno.guardias guardias, turno.descripcion descripcion,"+
		" turno.requisitos requisitos, turno.idordenacioncolas idordenacioncolas,turno.visibilidad visibilidad, turno.idtipoturno, "+
		" (SELECT f_siga_getrecurso(descripcion,"+usrbean.getLanguage()+") FROM scs_tipoturno tt WHERE turno.idtipoturno = tt.idtipoturno) TIPOTURNO, turno.idpersona_ultimo idpersona_ultimo,turno.FECHASOLICITUD_ULTIMO FECHASOLICITUD_ULTIMO, turno.idsubzona idsubzona, area.nombre area,"+
		" materia.nombre materia, zona.nombre zona, subzona.nombre subzona, partida.nombrepartida partidapresupuestaria, turno.idordenacioncolas idordenacioncolas, turno.idturno idturno, turno.validarjustificaciones validarjustificaciones, turno.validarinscripciones validarinscripciones,"+
		" turno.designadirecta designadirecta, subzona.idpartido idpartidojudicial, "+
		"  PKG_SIGA_SJCS.FUN_SJ_PARTIDOSJUDICIALES(turno.idinstitucion, turno.idsubzona, turno.idzona) partidojudicial, " +
		" turno." + ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT + " " + ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT + ", " + 
		" turno." + ScsTurnoBean.C_LETRADOACTUACIONES + " " + ScsTurnoBean.C_LETRADOACTUACIONES + ", " + 
		" turno." + ScsTurnoBean.C_LETRADOASISTENCIAS + " " + ScsTurnoBean.C_LETRADOASISTENCIAS+","+
		" turno." + ScsTurnoBean.C_CODIGOEXT + " " + ScsTurnoBean.C_CODIGOEXT +  
		" from scs_turno turno, scs_area area, scs_materia materia, scs_zona zona, scs_subzona subzona, scs_partidapresupuestaria partida, cen_partidojudicial partido"+
		" where area.idinstitucion = turno.idinstitucion"+
		" and area.idarea = turno.idarea"+
		" and partida.idinstitucion = turno.idinstitucion"+
		" and partida.idpartidapresupuestaria = turno.idpartidapresupuestaria"+
		" and materia.idinstitucion = turno.idinstitucion"+
		" and materia.idmateria = turno.idmateria"+
		" and materia.idarea = turno.idarea"+
		" and zona.idinstitucion = turno.idinstitucion"+
		" and zona.idzona = turno.idzona"+
		" and subzona.idinstitucion (+)= turno.idinstitucion"+
		" and subzona.idzona (+)= turno.idzona"+
		" and subzona.idsubzona (+)= turno.idsubzona"+
		" and partido.idpartido (+)= subzona.idpartido"+
		" and turno.idinstitucion ="+idInstitucion+
		" AND turno.idturno ="+idTurno;

		Hashtable miTurno = null;
		
		try {
			miTurno = (Hashtable)((Vector)this.ejecutaSelect(consulta)).get(0);
		} catch (Exception e){
			miTurno = new Hashtable();
		}
		return miTurno;
	}
	
	
	
	/*
	public String getSelectTurnosLetrado (String idInstitucion, String idPersona, boolean historico,String fecha) throws ClsExceptions 
	{
		String sql = UtilidadesBDAdm.sqlSelect(this.getTablasSelect("2"), this.getCamposSelect("2"));
		
		String where=	" WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion (+)= SCS_TURNO.idinstitucion"+            
						" AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria (+)= SCS_TURNO.idpartidapresupuestaria"+  
						" AND SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion"+
						" AND SCS_GRUPOFACTURACION.idgrupofacturacion = SCS_TURNO.idgrupofacturacion"+
						" AND SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion"+
						" AND SCS_MATERIA.idarea = SCS_TURNO.idarea"+
						" AND SCS_MATERIA.idmateria = SCS_TURNO.idmateria"+
						" AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion"+
						" AND SCS_AREA.idarea = SCS_MATERIA.idarea"+
						" AND SCS_SUBZONA.idinstitucion (+)= SCS_TURNO.idinstitucion"+
						" AND SCS_SUBZONA.idzona (+)= SCS_TURNO.idzona"+
						" AND SCS_SUBZONA.idsubzona (+)= SCS_TURNO.idsubzona"+
						" AND SCS_ZONA.idinstitucion (+)= SCS_TURNO.idinstitucion"+
						" AND SCS_ZONA.idzona (+)= SCS_TURNO.idzona"+
						" AND CEN_PARTIDOJUDICIAL.idpartido (+)= SCS_SUBZONA.idpartido"+
						" and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas "+
						" AND SCS_TURNO.idinstitucion ="+idInstitucion+
						" AND SCS_INSCRIPCIONTURNO.idinstitucion = "+idInstitucion+
						" AND SCS_INSCRIPCIONTURNO.idturno = SCS_TURNO.idturno"+
						
						" AND SCS_INSCRIPCIONTURNO.idpersona = "+idPersona+" ";
						if (!historico){
							if(fecha!=null){
								//String fechaFmt =GstDate.getApplicationFormatDate("", fecha);
								if(fecha.equalsIgnoreCase("sysdate"))
									fecha = "trunc(sysdate)";
								else
									fecha = "'"+fecha+"'";
							}else
								fecha = "trunc(sysdate)";
								
								//NO SACAR LAS CANCELADAS
								where += " AND ( ";
//								where += "   (SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NOT NULL ";
//								where += " AND TO_CHAR(SCS_INSCRIPCIONTURNO.FECHAVALIDACION, 'DD/MM/YYYY') <> ";
//								where += " NVL(TO_CHAR(SCS_INSCRIPCIONTURNO.FECHABAJA, 'DD/MM/YYYY'), '0')) ";
//								where += " OR ";
								
								   //PENDIENTES DE ALTA
								where += " (SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL AND SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NULL ";
								where += " AND SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NULL) ";
								     
								where += " OR ";
								     //VALIDADOS DE ALTA
								where += " (SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL AND ";
								where += " TRUNC(SCS_INSCRIPCIONTURNO.FECHAVALIDACION) <= "+fecha.trim()+" "; 
								where += " AND (SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL ";
								where += " OR (SCS_INSCRIPCIONTURNO.FECHABAJA IS NOT NULL AND ";
								where += " TRUNC(SCS_INSCRIPCIONTURNO.FECHABAJA) > "+fecha.trim()+")))" ;
							  
								
								
								
								where += " OR ";
								     // PENDIENTES DE BAJA
								where += " (SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NOT NULL AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL AND SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL) ";
								       
								       //BAJA FUTURA
								//where += " OR TRUNC(SCS_INSCRIPCIONTURNO.FECHABAJA) >"+fecha.trim()+" ";
								       
								       // BAJA DENEGADA
								where += " OR "; 
								where += " (SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NOT NULL AND SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NOT NULL) ";
								where += " ) ";
							
						}
		sql += where;
		sql += " ORDER BY NOMBRE,FECHASOLICITUD";
		return sql;
	}

	*/
	
	public static String getNombreTurnoJSP (String institucion, String idturno) throws ClsExceptions,SIGAException {
		   String datos="";
	       try {
	            RowsContainer rc = new RowsContainer(); 

			    Hashtable codigos = new Hashtable();
			    int contador=0;
			    
	            String sql ="select NOMBRE " +
                          " from SCS_TURNO ";
            	        
                          contador++;
          				codigos.put(new Integer(contador),institucion);
          	            sql += " WHERE IDINSTITUCION =:"+contador;
          	            
          	            contador++;
        				codigos.put(new Integer(contador),idturno);
        	            sql += " AND IDTURNO =:"+contador;
														
	            if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos = (String)resultado.get("NOMBRE");
	               }
	            } 
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return datos;                        
	    }	
		
	
	public List<ScsTurnoBean> getTurnos(VolantesExpressVo volanteExpres)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		if(volanteExpres.getFechaGuardia()!=null){
			//if(volanteExpres.getIdColegiado()!=null){
			if(false){
				sql.append(" SELECT DISTINCT TURNO.IDINSTITUCION, TURNO.IDTURNO, TURNO.NOMBRE ");
				sql.append(" FROM SCS_TURNO TURNO, SCS_CALENDARIOGUARDIAS GC,SCS_CABECERAGUARDIAS CG ");
				sql.append(" WHERE TURNO.IDINSTITUCION = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
				      
				sql.append(" AND GC.IDINSTITUCION = TURNO.IDINSTITUCION ");
				sql.append(" AND GC.IDTURNO = TURNO.IDTURNO ");
				
				
				sql.append(" AND GC.IDINSTITUCION = CG.IDINSTITUCION ");
				sql.append(" AND GC.IDTURNO = CG.IDTURNO ");
				sql.append(" AND GC.IDGUARDIA = CG.IDGUARDIA ");
				sql.append(" AND GC.IDCALENDARIOGUARDIAS = CG.IDCALENDARIOGUARDIAS ");
				sql.append(" AND CG.IDPERSONA = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),volanteExpres.getIdColegiado());
				
				
				sql.append(" AND TO_DATE(:");
				String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
				contador ++;
			    htCodigos.put(new Integer(contador),truncFechaGuardia);
			    sql.append(contador);
				sql.append(",'dd/MM/yyyy') BETWEEN TRUNC(GC.FECHAINICIO) AND TRUNC(GC.FECHAFIN) ");
				sql.append(" ORDER BY TURNO.NOMBRE ");
			}else{
				sql.append(" SELECT DISTINCT TURNO.IDINSTITUCION, TURNO.IDTURNO, TURNO.NOMBRE ");
				sql.append(" FROM SCS_TURNO TURNO, SCS_CALENDARIOGUARDIAS GC ");
				sql.append(" WHERE TURNO.IDINSTITUCION = :");
				contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
				      
				sql.append(" AND GC.IDINSTITUCION = TURNO.IDINSTITUCION ");
				sql.append(" AND GC.IDTURNO = TURNO.IDTURNO ");
				sql.append(" AND TO_DATE(:");
				String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
				contador ++;
			    htCodigos.put(new Integer(contador),truncFechaGuardia);
			    sql.append(contador);
				sql.append(",'dd/MM/yyyy') BETWEEN TRUNC(GC.FECHAINICIO) AND TRUNC(GC.FECHAFIN) ");
				sql.append(" ORDER BY TURNO.NOMBRE ");
			}
		}else{
			sql.append(" SELECT IDINSTITUCION , IDTURNO ");
			sql.append(" , NOMBRE FROM SCS_TURNO TURNO  ");
			sql.append(" WHERE TURNO.IDINSTITUCION = :");
		    contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),volanteExpres.getIdInstitucion());
			
			sql.append(" ORDER BY TURNO.NOMBRE ");
		}
		List<ScsTurnoBean> alTurnos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alTurnos = new ArrayList<ScsTurnoBean>();
            	ScsTurnoBean turnoBean = null;
            	if(rc.size()>1){
            		turnoBean = new ScsTurnoBean();
	    			turnoBean.setNombre(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(), "general.combo.seleccionar"));
	    			turnoBean.setIdTurno(new Integer(-1));
	            	alTurnos.add(turnoBean);
            	}
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		turnoBean = new ScsTurnoBean();
            		turnoBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDINSTITUCION));
            		turnoBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDTURNO));
        			turnoBean.setNombre(UtilidadesHash.getString(htFila,ScsTurnoBean.C_NOMBRE));
            		alTurnos.add(turnoBean);
            	}
            } 
       } catch (Exception e) {
    	   ClsLogging.writeFileLog("VOLANTES EXPRESS:Error Select Turnos"+e.toString(), 10);
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTurnos;
		
	}
	
	
	
	
	
	public List<ScsTurnoBean> getTurnos(String idInstitucion)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT IDINSTITUCION , IDTURNO ");
		sql.append(" , NOMBRE FROM SCS_TURNO TURNO  ");
		sql.append(" WHERE TURNO.IDINSTITUCION = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		sql.append(" ORDER BY TURNO.NOMBRE ");
		
		List<ScsTurnoBean> alTurnos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alTurnos = new ArrayList<ScsTurnoBean>();
            	ScsTurnoBean turnoBean = null;
            	if(rc.size()>1){
            		turnoBean = new ScsTurnoBean();
	    			turnoBean.setNombre(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));
	    			turnoBean.setIdTurno(new Integer(-1));
	            	alTurnos.add(turnoBean);
            	}
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		turnoBean = new ScsTurnoBean();
            		turnoBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDINSTITUCION));
            		turnoBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDTURNO));
        			turnoBean.setNombre(UtilidadesHash.getString(htFila,ScsTurnoBean.C_NOMBRE));
            		alTurnos.add(turnoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTurnos;
		
		
		
	}
	public ScsTurnoBean getTurnoInscripcion(Integer idInstitucion,Integer idTurno)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append(" T.NOMBRE NOMBRETURNO, ");
		sql.append(" T.DESCRIPCION DESCRIPCIONTURNO,  T.ABREVIATURA ABREVIATURATURNO, ");
		sql.append(" T.GUARDIAS GUARDIAS, T.VALIDARJUSTIFICACIONES , ");
		sql.append(" T.VALIDARINSCRIPCIONES , T.DESIGNADIRECTA , ");
		sql.append(" T.REQUISITOS,  T.IDPERSONA_ULTIMO IDPERSONAULTIMO,T.FECHASOLICITUD_ULTIMO  FECHASOLICITUD_ULTIMO,");
		sql.append(" T.ACTIVARRETRICCIONACREDIT, T.LETRADOACTUACIONES, T.LETRADOASISTENCIAS, ");
		sql.append(" A.NOMBRE AREA,    A.IDAREA IDAREA,   M.NOMBRE MATERIA, ");
		sql.append(" M.IDMATERIA IDMATERIA,   Z.NOMBRE ZONA,  Z.IDZONA IDZONA, ");
		sql.append(" SZ.NOMBRE SUBZONA,  SZ.IDSUBZONA IDSUBZONA,  ");
		sql.append(" PKG_SIGA_SJCS.FUN_SJ_PARTIDOSJUDICIALES(SZ.IDINSTITUCION,SZ.IDSUBZONA,Z.IDZONA) PARTIDOJUDICIAL, ");
		sql.append(" PP.NOMBREPARTIDA PARTIDAPRESUPUESTARIA, ");
		sql.append(" PP.IDPARTIDAPRESUPUESTARIA IDPARTIDAPRESUPUESTARIA, F_SIGA_GETRECURSO(GF.NOMBRE, 1) GRUPOFACTURACION, ");
		sql.append(" GF.IDGRUPOFACTURACION IDGRUPOFACTURACION, ");
		sql.append(" OC.IDORDENACIONCOLAS, OC.ALFABETICOAPELLIDOS, ");
		sql.append(" OC.FECHANACIMIENTO, OC.NUMEROCOLEGIADO ,   OC.ANTIGUEDADCOLA , ");
		sql.append(" T.IDINSTITUCION,   T.IDTURNO ");
		
		sql.append(" FROM SCS_TURNO T, ");
		sql.append(" SCS_ORDENACIONCOLAS OC,SCS_PARTIDAPRESUPUESTARIA PP, SCS_GRUPOFACTURACION GF, ");
		sql.append(" SCS_MATERIA M,   SCS_AREA A,    SCS_SUBZONA SZ, ");
		sql.append(" SCS_ZONA Z ");
		sql.append(" WHERE OC.IDORDENACIONCOLAS = T.IDORDENACIONCOLAS ");
		
		sql.append(" AND PP.IDINSTITUCION(+) = T.IDINSTITUCION ");
		sql.append(" AND PP.IDPARTIDAPRESUPUESTARIA(+) =       T.IDPARTIDAPRESUPUESTARIA ");
		sql.append(" AND GF.IDINSTITUCION = T.IDINSTITUCION ");
		sql.append(" AND GF.IDGRUPOFACTURACION =		       T.IDGRUPOFACTURACION ");
		sql.append(" AND M.IDINSTITUCION = T.IDINSTITUCION ");
		sql.append(" AND M.IDAREA = T.IDAREA		   AND M.IDMATERIA = T.IDMATERIA ");
		sql.append(" AND A.IDINSTITUCION = M.IDINSTITUCION   AND A.IDAREA = M.IDAREA ");
		sql.append(" AND SZ.IDINSTITUCION(+) = T.IDINSTITUCION   AND SZ.IDZONA(+) = T.IDZONA ");
		sql.append(" AND SZ.IDSUBZONA(+) = T.IDSUBZONA   AND Z.IDINSTITUCION(+) = T.IDINSTITUCION ");
		sql.append(" AND Z.IDZONA(+) = T.IDZONA ");

		
		
		
		sql.append(" AND T.IDINSTITUCION = ");
		sql.append(idInstitucion);

		sql.append(" AND T.IDTURNO = ");
		sql.append(idTurno);


		ScsTurnoBean turno = null;
		try {
			RowsContainer rc = new RowsContainer(); 

			if (rc.find(sql.toString())) {
				ScsPartidaPresupuestariaBean partidaPresupuestaria =null;
				ScsMateriaBean materia = null;
				ScsAreaBean area = null;
				ScsZonaBean zona = null;
				ScsSubzonaBean subZona =null;
				CenPartidoJudicialBean partidoJudicial = null;
				ScsGrupoFacturacionBean grupoFacturacion = null;
				ScsOrdenacionColasBean ordenacionColas = null;

				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila=fila.getRow();
					turno = new ScsTurnoBean();


					turno.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDINSTITUCION));
					turno.setIdTurno(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDTURNO));
					turno.setNombre(UtilidadesHash.getString(htFila,"NOMBRETURNO"));
					turno.setDescripcion(UtilidadesHash.getString(htFila,"DESCRIPCIONTURNO"));
					turno.setAbreviatura(UtilidadesHash.getString(htFila,"ABREVIATURATURNO"));
					turno.setGuardias(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_GUARDIAS));
					turno.setValidarJustificaciones(UtilidadesHash.getString(htFila,ScsTurnoBean.C_VALIDARJUSTIFICACIONES));
					turno.setValidarInscripciones(UtilidadesHash.getString(htFila,ScsTurnoBean.C_VALIDARINSCRIPCIONES));
					turno.setDesignaDirecta(UtilidadesHash.getString(htFila,ScsTurnoBean.C_DESIGNADIRECTA));
					turno.setRequisitos(UtilidadesHash.getString(htFila,ScsTurnoBean.C_REQUISITOS));
					turno.setIdPersonaUltimo(UtilidadesHash.getLong(htFila,ScsTurnoBean.C_IDPERSONAULTIMO));
					turno.setFechaSolicitudUltimo(UtilidadesHash.getString(htFila,ScsTurnoBean.C_FECHASOLICITUD_ULTIMO));
					turno.setActivarRestriccionAcreditacion(UtilidadesHash.getString(htFila,ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT));
					turno.setVisibilidad(UtilidadesHash.getString(htFila,ScsTurnoBean.C_VISIBILIDAD));
					turno.setIdTipoTurno(UtilidadesHash.getString(htFila,ScsTurnoBean.C_IDTIPOTURNO));
					turno.setLetradoActuaciones(UtilidadesHash.getString(htFila,ScsTurnoBean.C_LETRADOACTUACIONES));
					turno.setLetradoAsistencias(UtilidadesHash.getString(htFila,ScsTurnoBean.C_LETRADOASISTENCIAS));
					
					
					ordenacionColas = new ScsOrdenacionColasBean();
					ordenacionColas.setAlfabeticoApellidos(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS));
					ordenacionColas.setAntiguedadCola(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_ANTIGUEDADCOLA));
					ordenacionColas.setFechaNacimiento(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_FECHANACIMIENTO));
					ordenacionColas.setNumeroColegiado(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_NUMEROCOLEGIADO));
					ordenacionColas.setIdOrdenacionColas(UtilidadesHash.getInteger(htFila,ScsOrdenacionColasBean.C_IDORDENACIONCOLAS));
					partidaPresupuestaria = new ScsPartidaPresupuestariaBean();
					partidaPresupuestaria.setIdPartidaPresupuestaria(UtilidadesHash.getInteger(htFila,ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA));
					partidaPresupuestaria.setNombrePartida(UtilidadesHash.getString(htFila,"PARTIDAPRESUPUESTARIA"));
					materia = new ScsMateriaBean();
					materia.setIdMateria(UtilidadesHash.getInteger(htFila,ScsMateriaBean.C_IDMATERIA));
					materia.setNombre(UtilidadesHash.getString(htFila,"MATERIA"));
					area = new ScsAreaBean();
					area.setIdArea(UtilidadesHash.getInteger(htFila,ScsAreaBean.C_IDAREA));
					area.setNombre(UtilidadesHash.getString(htFila,"AREA"));
					zona = new ScsZonaBean();
					zona.setIdZona(UtilidadesHash.getInteger(htFila,ScsZonaBean.C_IDZONA));
					zona.setNombre(UtilidadesHash.getString(htFila,"ZONA"));
					subZona = new ScsSubzonaBean();
					subZona.setIdSubzona(UtilidadesHash.getInteger(htFila,ScsSubzonaBean.C_IDSUBZONA));
					subZona.setNombre(UtilidadesHash.getString(htFila,"SUBZONA"));
					partidoJudicial = new CenPartidoJudicialBean();
//					partidoJudicial.setIdPartido(UtilidadesHash.getInteger(htFila,CenPartidoJudicialBean.C_IDPARTIDO));
					partidoJudicial.setNombre(UtilidadesHash.getString(htFila,"PARTIDOJUDICIAL"));
					grupoFacturacion = new ScsGrupoFacturacionBean();
					grupoFacturacion.setIdGrupoFacturacion(UtilidadesHash.getInteger(htFila,ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION));
					grupoFacturacion.setNombre(UtilidadesHash.getString(htFila,"GRUPOFACTURACION"));
					turno.setPartidaPresupuestaria(partidaPresupuestaria);
					turno.setMateria(materia);
					turno.setArea(area);
					turno.setZona(zona);
					turno.setSubZona(subZona);
					turno.setPartidoJudicial(partidoJudicial);
					turno.setGrupoFacturacion(grupoFacturacion);
					turno.setOrdenacionColas(ordenacionColas);
					
				}
			} 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta.");
		}
		return turno;

	}
	
	public Vector getTurnosDisponibles(Hashtable hashFiltro,Long idPersona,Integer idInstitucion) throws ClsExceptions{

		


			String sql = 
				 " SELECT SCS_TURNO.IDTURNO IDTURNO, SCS_TURNO.NOMBRE NOMBRE, SCS_TURNO.ABREVIATURA ABREVIATURA,  SCS_AREA.NOMBRE AREA,   "+
				 " SCS_AREA.IDAREA IDAREA, SCS_MATERIA.NOMBRE MATERIA, SCS_MATERIA.IDMATERIA IDMATERIA,  SCS_ZONA.NOMBRE ZONA,   "+
				 " SCS_ZONA.IDZONA IDZONA, SCS_SUBZONA.NOMBRE SUBZONA, SCS_SUBZONA.IDSUBZONA IDSUBZONA,    "+
				 " Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES("+idInstitucion+",SCS_SUBZONA.IDSUBZONA,SCS_ZONA.IDZONA) PARTIDOS,CEN_PARTIDOJUDICIAL.NOMBRE PARTIDOJUDICIAL, CEN_PARTIDOJUDICIAL.IDPARTIDO IDPARTIDOJUDICIAL,    "+
				 " SCS_PARTIDAPRESUPUESTARIA.NOMBREPARTIDA PARTIDAPRESUPUESTARIA,    "+
				 " SCS_PARTIDAPRESUPUESTARIA.IDPARTIDAPRESUPUESTARIA IDPARTIDAPRESUPUESTARIA,    "+
				 UtilidadesMultidioma.getCampoMultidiomaSimple("SCS_GRUPOFACTURACION.NOMBRE", usrbean.getLanguage())+" GRUPOFACTURACION, SCS_GRUPOFACTURACION.IDGRUPOFACTURACION IDGRUPOFACTURACION,    "+
				 " SCS_TURNO.GUARDIAS GUARDIAS, SCS_TURNO.VALIDARJUSTIFICACIONES VALIDARJUSTIFICACIONES,    "+
				 " SCS_TURNO.VALIDARINSCRIPCIONES VALIDARINSCRIPCIONES, SCS_TURNO.DESIGNADIRECTA DESIGNADIRECTA,    "+
				 " SCS_TURNO.DESCRIPCION DESCRIPCION, SCS_TURNO.REQUISITOS REQUISITOS,    "+
				 " SCS_TURNO.IDPERSONA_ULTIMO IDPERSONAULTIMO,SCS_TURNO.FECHASOLICITUD_ULTIMO FECHASOLICITUD_ULTIMO, SCS_TURNO.IDORDENACIONCOLAS IDORDENACIONCOLAS,    "+
				 " SCS_ORDENACIONCOLAS.ALFABETICOAPELLIDOS A8LFABETICOAPELLIDOS, SCS_ORDENACIONCOLAS.FECHANACIMIENTO EDAD,    "+
				 " SCS_ORDENACIONCOLAS.NUMEROCOLEGIADO ANTIGUEDAD, SCS_ORDENACIONCOLAS.ANTIGUEDADCOLA ANTIGUEDADENCOLA,   "+
				 " (select count(1) "+
				 "  from scs_guardiasturno g "+
				 "  where g.idinstitucion=SCS_TURNO.Idinstitucion "+
				 "     and g.idturno=scs_turno.idturno) NGUARDIAS "+
				 "   ,SCS_TURNO.IDINSTITUCION "+
				 " FROM  SCS_TURNO,SCS_PARTIDAPRESUPUESTARIA,SCS_GRUPOFACTURACION,SCS_MATERIA,SCS_AREA,SCS_SUBZONA,SCS_ZONA, CEN_PARTIDOJUDICIAL,SCS_ORDENACIONCOLAS  " +
				 
				 " WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion (+)= SCS_TURNO.idinstitucion    "+
				 " AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria (+)= SCS_TURNO.idpartidapresupuestaria   "+
				 " AND  SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion   "+
				 " AND  SCS_GRUPOFACTURACION.idgrupofacturacion = SCS_TURNO.idgrupofacturacion   "+
				 " AND  SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion " +
				 " AND  SCS_MATERIA.Idmateria = SCS_TURNO.Idmateria " +
				 " AND SCS_Area.idarea = SCS_TURNO.idarea   "+
				 " AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion   "+
				 " AND SCS_AREA.idarea = SCS_MATERIA.idarea " + 
				 " AND SCS_SUBZONA.idinstitucion = SCS_TURNO.idinstitucion    "+
				 " AND SCS_SUBZONA.IDSUBZONA= SCS_TURNO.IDSUBZONA    "+
				 " AND SCS_ZONA.idinstitucion = SCS_TURNO.idinstitucion"+
				 " AND SCS_ZONA.idzona = SCS_TURNO.idzona     "+
				 " and scs_zona.idzona=scs_subzona.idzona "+
				 " AND CEN_PARTIDOJUDICIAL.idpartido (+)= SCS_SUBZONA.idpartido  "+
				 " and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas  " +
				 " AND SCS_TURNO.idinstitucion = "+idInstitucion+
				 " AND SCS_TURNO.IDTURNO NOT IN (select idturno from scs_inscripcionturno t where t.idpersona = "+idPersona+
				 " and t.idinstitucion = "+idInstitucion+" and t.FECHABAJA IS NULL 	AND  FECHADENEGACION IS  NULL)";

			try{
				Integer.parseInt((String)hashFiltro.get("IDPARTIDOJUDICIAL"));
			}catch(Exception e){hashFiltro.put("IDPARTIDOJUDICIAL","-1");}
							
			if(!((String)hashFiltro.get("ABREVIATURA")).equalsIgnoreCase("")){
				sql += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hashFiltro.get("ABREVIATURA")).trim(),"SCS_TURNO."+ScsTurnoBean.C_ABREVIATURA);
			}
			if(!((String)hashFiltro.get("NOMBRE")).equalsIgnoreCase("")){
				sql += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hashFiltro.get("NOMBRE")).trim(),"SCS_TURNO."+ScsTurnoBean.C_NOMBRE);
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDAREA"))>0))where+=" and ";
			if(Integer.parseInt((String)hashFiltro.get("IDAREA"))>0){
				sql+=	" AND SCS_AREA.idarea = "+(String)hashFiltro.get("IDAREA");
					//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDMATERIA"))>0))where+=" and ";
			try{
				if(Integer.parseInt((String)hashFiltro.get("IDMATERIA"))>0){
				sql+=	" AND SCS_MATERIA.idmateria ="+(String)hashFiltro.get("IDMATERIA");
				// ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
				}
			}catch(Exception e){}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDZONA"))>0))where+=" and ";
			String idzon = "";
			if (hashFiltro.get("IDZONA")!=null && !hashFiltro.get("IDZONA").equals("0")&& !hashFiltro.get("IDZONA").equals("")) {
				idzon=(String)hashFiltro.get("IDZONA");
				//idzon=idzon.substring(idzon.indexOf(","),idzon.length());
				if(Integer.parseInt(idzon)>0){
					sql+=	" AND SCS_ZONA.idzona ="+idzon;
					//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDZONA+" = "+((String)hash.get("IDZONA")).toUpperCase();
				}
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDSUBZONA"))>0))where+=" and ";
			try{
				if(Integer.parseInt((String)hashFiltro.get("IDSUBZONA"))>0){
				sql+=	" AND SCS_SUBZONA.idsubzona = "+(String)hashFiltro.get("IDSUBZONA");
				}
			}catch(Exception e){}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0))where+=" and ";
			//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDSUBZONA+"="+form.getSubzona()+" and "+
			sql+=" order by SCS_TURNO.NOMBRE";
			
			Vector vTurno;
			
			vTurno = this.ejecutaSelect(sql);
			
		
		return vTurno;
}
	
	
	
	public Vector getTurnosDisponiblesBaja(Hashtable hashFiltro,Long idPersona,Integer idInstitucion) throws ClsExceptions{

		


		String sql = 
			 " SELECT SCS_TURNO.IDTURNO IDTURNO, SCS_TURNO.NOMBRE NOMBRE, SCS_TURNO.ABREVIATURA ABREVIATURA,  SCS_AREA.NOMBRE AREA,   "+
			 " SCS_AREA.IDAREA IDAREA, SCS_MATERIA.NOMBRE MATERIA, SCS_MATERIA.IDMATERIA IDMATERIA,  SCS_ZONA.NOMBRE ZONA,   "+
			 " SCS_ZONA.IDZONA IDZONA, SCS_SUBZONA.NOMBRE SUBZONA, SCS_SUBZONA.IDSUBZONA IDSUBZONA,    "+
			 " Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES("+idInstitucion+",SCS_SUBZONA.IDSUBZONA,SCS_ZONA.IDZONA) PARTIDOS,CEN_PARTIDOJUDICIAL.NOMBRE PARTIDOJUDICIAL, CEN_PARTIDOJUDICIAL.IDPARTIDO IDPARTIDOJUDICIAL,    "+
			 " SCS_PARTIDAPRESUPUESTARIA.NOMBREPARTIDA PARTIDAPRESUPUESTARIA,    "+
			 " SCS_PARTIDAPRESUPUESTARIA.IDPARTIDAPRESUPUESTARIA IDPARTIDAPRESUPUESTARIA,    "+
			 UtilidadesMultidioma.getCampoMultidiomaSimple("SCS_GRUPOFACTURACION.NOMBRE", usrbean.getLanguage())+" GRUPOFACTURACION, SCS_GRUPOFACTURACION.IDGRUPOFACTURACION IDGRUPOFACTURACION,    "+
			 " SCS_TURNO.GUARDIAS GUARDIAS, SCS_TURNO.VALIDARJUSTIFICACIONES VALIDARJUSTIFICACIONES,    "+
			 " SCS_TURNO.VALIDARINSCRIPCIONES VALIDARINSCRIPCIONES, SCS_TURNO.DESIGNADIRECTA DESIGNADIRECTA,    "+
			 " SCS_TURNO.DESCRIPCION DESCRIPCION, SCS_TURNO.REQUISITOS REQUISITOS,    "+
			 " SCS_TURNO.IDPERSONA_ULTIMO IDPERSONAULTIMO,SCS_TURNO.FECHASOLICITUD_ULTIMO FECHASOLICITUD_ULTIMO, SCS_TURNO.IDORDENACIONCOLAS IDORDENACIONCOLAS,    "+
			 " SCS_ORDENACIONCOLAS.ALFABETICOAPELLIDOS A8LFABETICOAPELLIDOS, SCS_ORDENACIONCOLAS.FECHANACIMIENTO EDAD,    "+
			 " SCS_ORDENACIONCOLAS.NUMEROCOLEGIADO ANTIGUEDAD, SCS_ORDENACIONCOLAS.ANTIGUEDADCOLA ANTIGUEDADENCOLA,   "+
			 " (select count(1) "+
			 "  from scs_guardiasturno g "+
			 "  where g.idinstitucion=SCS_TURNO.Idinstitucion "+
			 "     and g.idturno=scs_turno.idturno) NGUARDIAS "+
			 "   ,SCS_TURNO.IDINSTITUCION, SCS_INSCRIPCIONTURNO.Fechasolicitud "+
			 "  FROM SCS_TURNO,                       "+                                         
			 "       SCS_PARTIDAPRESUPUESTARIA,       "+                                         
			 "       SCS_GRUPOFACTURACION,            "+                                         
			 "       SCS_MATERIA,                     "+                                         
			 "       SCS_AREA,                        "+                                         
			 "       SCS_SUBZONA,                     "+ 
			 "       SCS_ZONA,                        "+ 
			 "       CEN_PARTIDOJUDICIAL,             "+ 
			 "       SCS_INSCRIPCIONTURNO,   "+ 
			 "       SCS_ORDENACIONCOLAS     "+ 
			 " WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion(+) = SCS_TURNO.idinstitucion"+ 
			 "   AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria(+) =              "+ 
			 "       SCS_TURNO.idpartidapresupuestaria                                   "+ 
			 "   AND SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion        "+ 
			 "   AND SCS_GRUPOFACTURACION.idgrupofacturacion =                           "+ 
			 "       SCS_TURNO.idgrupofacturacion                                        "+ 
			 "   AND SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion                 "+ 
			 "   AND SCS_MATERIA.idarea = SCS_TURNO.idarea                               "+ 
			 "   AND SCS_MATERIA.idmateria = SCS_TURNO.idmateria                          "+ 
			 "   AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion                   "+ 
			 "   AND SCS_AREA.idarea = SCS_MATERIA.idarea                                 "+ 
			 "   AND SCS_SUBZONA.idinstitucion(+) = SCS_TURNO.idinstitucion               "+ 
			 "   AND SCS_SUBZONA.idzona(+) = SCS_TURNO.idzona                             "+ 
			 "   AND SCS_SUBZONA.idsubzona(+) = SCS_TURNO.idsubzona                       "+ 
			 "   AND SCS_ZONA.idinstitucion(+) = SCS_TURNO.idinstitucion                  "+ 
			 "   AND SCS_ZONA.idzona(+) = SCS_TURNO.idzona                                "+ 
			 "   AND CEN_PARTIDOJUDICIAL.idpartido(+) = SCS_SUBZONA.idpartido             "+ 
			 "   and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas  "+ 
			 "   AND SCS_TURNO.idinstitucion = "+idInstitucion+
			 "   AND SCS_INSCRIPCIONTURNO.idinstitucion = "+idInstitucion+
			 "   AND SCS_INSCRIPCIONTURNO.idturno = SCS_TURNO.idturno                     "+ 
			 "   AND SCS_INSCRIPCIONTURNO.idpersona = "+idPersona+ 
			 //"   --altas pendientes de validar                                            "+ 
			 "   AND ((SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NULL and                   "+ 
			 "        SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NULL AND                 "+ 
			 "        scs_inscripcionturno.fechabaja is null and                          "+ 
			 "        SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NULL                        "+ 
			 "        ) OR                                                                "+ 
			 "                                                                            "+ 
			 //"        --altas validadas                                                   "+ 
			 "        (SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NOT NULL AND               "+ 
			 "        SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NULL AND                 "+ 
			 "        scs_inscripcionturno.fechabaja is null and                "+ 
			 "        Scs_Inscripcionturno.Fechadenegacion is null) OR          "+ 
			 "                                                                  "+ 
			 //"        --bajas denegadas                                         "+ 
			 "        (scs_inscripcionturno.fechavalidacion is not null and     "+ 
			 "        SCS_INSCRIPCIONTURNO.FECHASOLICITUDBAJA IS NOT NULL and   "+ 
			 "        scs_inscripcionturno.fechabaja is null and                "+ 
			 "        SCS_INSCRIPCIONTURNO.FECHADENEGACION IS NOT NULL))        ";

		try{
			Integer.parseInt((String)hashFiltro.get("IDPARTIDOJUDICIAL"));
		}catch(Exception e){hashFiltro.put("IDPARTIDOJUDICIAL","-1");}
						
		if(!((String)hashFiltro.get("ABREVIATURA")).equalsIgnoreCase("")){
			sql += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hashFiltro.get("ABREVIATURA")).trim(),"SCS_TURNO."+ScsTurnoBean.C_ABREVIATURA);
		}
		if(!((String)hashFiltro.get("NOMBRE")).equalsIgnoreCase("")){
			sql += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hashFiltro.get("NOMBRE")).trim(),"SCS_TURNO."+ScsTurnoBean.C_NOMBRE);
		}
		//if((ant)&&(Integer.parseInt((String)hash.get("IDAREA"))>0))where+=" and ";
		if(Integer.parseInt((String)hashFiltro.get("IDAREA"))>0){
			sql+=	" AND SCS_AREA.idarea = "+(String)hashFiltro.get("IDAREA");
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
		}
		//if((ant)&&(Integer.parseInt((String)hash.get("IDMATERIA"))>0))where+=" and ";
		try{
			if(Integer.parseInt((String)hashFiltro.get("IDMATERIA"))>0){
			sql+=	" AND SCS_MATERIA.idmateria ="+(String)hashFiltro.get("IDMATERIA");
			// ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
			}
		}catch(Exception e){}
		//if((ant)&&(Integer.parseInt((String)hash.get("IDZONA"))>0))where+=" and ";
		String idzon = "";
		if (hashFiltro.get("IDZONA")!=null && !hashFiltro.get("IDZONA").equals("0")&& !hashFiltro.get("IDZONA").equals("")) {
			idzon=(String)hashFiltro.get("IDZONA");
			//idzon=idzon.substring(idzon.indexOf(","),idzon.length());
			if(Integer.parseInt(idzon)>0){
				sql+=	" AND SCS_ZONA.idzona ="+idzon;
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDZONA+" = "+((String)hash.get("IDZONA")).toUpperCase();
			}
		}
		//if((ant)&&(Integer.parseInt((String)hash.get("IDSUBZONA"))>0))where+=" and ";
		try{
			if(Integer.parseInt((String)hashFiltro.get("IDSUBZONA"))>0){
			sql+=	" AND SCS_SUBZONA.idsubzona = "+(String)hashFiltro.get("IDSUBZONA");
			}
		}catch(Exception e){}
		//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0))where+=" and ";
		//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDSUBZONA+"="+form.getSubzona()+" and "+
		sql+=" ORDER BY NOMBRE, FECHASOLICITUD ";
		
		Vector vTurno;
		
		vTurno = this.ejecutaSelect(sql);
		
	
	return vTurno;
}

	
	public void cambiarUltimoCola (Integer idInstitucion,
			Integer idTurno,
			Long idPersonaUltimo,
			String fechaSolicitudUltimo)
	throws ClsExceptions
	{
		String sIdinstitucion = idInstitucion.toString();
		String sIdTurno = idTurno.toString();
		String sIdpersona = (idPersonaUltimo == null) ? "null" : idPersonaUltimo.toString();
		String sFechaSolicitudUltimo = (fechaSolicitudUltimo == null || fechaSolicitudUltimo.equals("")) ?
				"null" : fechaSolicitudUltimo.toString();

		String[] campos = 
		{
				ScsTurnoBean.C_IDPERSONAULTIMO,
				ScsTurnoBean.C_FECHASOLICITUD_ULTIMO,
				ScsTurnoBean.C_USUMODIFICACION,
				ScsTurnoBean.C_FECHAMODIFICACION
		};

		Hashtable hash = new Hashtable();
		hash.put(ScsTurnoBean.C_IDINSTITUCION, sIdinstitucion);
		hash.put(ScsTurnoBean.C_IDTURNO, sIdTurno);
		hash.put(ScsTurnoBean.C_IDPERSONAULTIMO, sIdpersona);
		hash.put(ScsTurnoBean.C_FECHASOLICITUD_ULTIMO, sFechaSolicitudUltimo);
		hash.put(ScsTurnoBean.C_USUMODIFICACION, this.usrbean.getUserName());
		hash.put(ScsTurnoBean.C_FECHAMODIFICACION, "SYSDATE");

		this.updateDirect(hash, this.getClavesBean(), campos);
	} // cambiarUltimoCola()
	
	public List<ScsTurnoBean> getTurnosConTipo(String idInstitucion, String idTipoTurno)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT IDINSTITUCION , IDTURNO ");
		sql.append(" , NOMBRE FROM SCS_TURNO TURNO  ");
		sql.append(" WHERE TURNO.IDINSTITUCION = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idInstitucion);
		
		if(idTipoTurno!=null && !idTipoTurno.equals("")){
			sql.append(" AND NVL(TURNO.IDTIPOTURNO,0) <> :");
		    contador ++;
			sql.append(contador);
			htCodigos.put(new Integer(contador),idTipoTurno);
		}
		sql.append(" AND TURNO.VISIBILIDAD = '1' ");
		sql.append(" ORDER BY TURNO.NOMBRE ");
		
		List<ScsTurnoBean> alTurnos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alTurnos = new ArrayList<ScsTurnoBean>();
            	ScsTurnoBean turnoBean = null;
            	if(rc.size()>1){
            		turnoBean = new ScsTurnoBean();
	    			turnoBean.setNombre(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));
	    			turnoBean.setIdTurno(new Integer(-1));
	            	alTurnos.add(turnoBean);
            	}
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		turnoBean = new ScsTurnoBean();
            		turnoBean.setIdInstitucion(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDINSTITUCION));
            		turnoBean.setIdTurno(UtilidadesHash.getInteger(htFila,ScsTurnoBean.C_IDTURNO));
        			turnoBean.setNombre(UtilidadesHash.getString(htFila,ScsTurnoBean.C_NOMBRE));
            		alTurnos.add(turnoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTurnos;
		
	}	
	
}