
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.general.SIGAException;

import java.util.Vector;
import com.atos.utils.*;

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
				ScsTurnoBean.C_VALIDARJUSTIFICACIONES,	
				ScsTurnoBean.C_DESIGNADIRECTA,			ScsTurnoBean.C_VALIDARINSCRIPCIONES,	
				ScsTurnoBean.C_IDMATERIA,				ScsTurnoBean.C_IDZONA,
				ScsTurnoBean.C_IDSUBZONA,				ScsTurnoBean.C_IDORDENADIONCOLAS,
				ScsTurnoBean.C_IDGRUPOFACTURACION,		ScsTurnoBean.C_REQUISITOS,
				ScsTurnoBean.C_IDAREA,					ScsTurnoBean.C_IDPERSONAULTIMO,
				ScsTurnoBean.C_IDPARTIDAPRESUPUESTARIA,	ScsTurnoBean.C_USUMODIFICACION,
				ScsTurnoBean.C_FECHAMODIFICACION, 		ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT,
				ScsTurnoBean.C_LETRADOACTUACIONES,		ScsTurnoBean.C_LETRADOASISTENCIAS};
		return campos;
	}
	/**
	 * Devuelve los tablas que intervienen en la consulta, el campo from
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con las tablas del select, campo from
	 */
	
	protected String getTablasSelect(String entrada){
		String campos = " SCS_TURNO,SCS_PARTIDAPRESUPUESTARIA,SCS_GRUPOFACTURACION,SCS_MATERIA,SCS_AREA,SCS_SUBZONA,SCS_ZONA,CEN_PARTIDOJUDICIAL,SCS_INSCRIPCIONTURNO,SCS_ORDENACIONCOLAS "; 
		if (entrada =="1")campos="SCS_TURNO   turnos, SCS_PARTIDAPRESUPUESTARIA partid,SCS_GRUPOFACTURACION  grupof,SCS_MATERIA       materi,SCS_AREA    area,SCS_SUBZONA    subzon,SCS_ZONA    zona,CEN_PARTIDOJUDICIAL   parjud,SCS_ORDENACIONCOLAS";	
		return campos;
	}
	/**
	 * Devuelve los campos de en la consulta
	 * @param entrada indicador del permiso del usuario logado
	 * @return String con los campos del select
	 */
	
	protected String[] getCamposSelect(String entrada){
		if (entrada.equalsIgnoreCase("1")){
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
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+" IDORDENACIONCOLAS",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+" ALFABETICOAPELLIDOS",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_FECHANACIMIENTO+" EDAD",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+" ANTIGUEDAD",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+" ANTIGUEDADENCOLA"};
			return campos;
		}else{ 
			String[] campos = {	ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDTURNO													+ " IDTURNO",								
//					"substr(" + ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE + ",1,10)" 							+ " NOMBRE",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE + " NOMBRE",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_ABREVIATURA												+ " ABREVIATURA",
					ScsAreaBean.T_NOMBRETABLA+"."+ScsAreaBean.C_NOMBRE														+ " AREA",
					ScsAreaBean.T_NOMBRETABLA+"."+ScsAreaBean.C_IDAREA														+ " IDAREA",
					ScsMateriaBean.T_NOMBRETABLA+"."+ScsMateriaBean.C_NOMBRE												+" MATERIA",
					ScsMateriaBean.T_NOMBRETABLA+"."+ScsMateriaBean.C_IDMATERIA												+" IDMATERIA",
					ScsZonaBean.T_NOMBRETABLA+"."+ScsZonaBean.C_NOMBRE														+" ZONA",
					ScsZonaBean.T_NOMBRETABLA+"."+ScsZonaBean.C_IDZONA														+" IDZONA",
					ScsSubzonaBean.T_NOMBRETABLA+"."+ScsSubzonaBean.C_NOMBRE												+" SUBZONA",
					ScsSubzonaBean.T_NOMBRETABLA+"."+ScsSubzonaBean.C_IDSUBZONA												+" IDSUBZONA",
					CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_NOMBRE								+" PARTIDOJUDICIAL",
					CenPartidoJudicialBean.T_NOMBRETABLA+"."+CenPartidoJudicialBean.C_IDPARTIDO								+" IDPARTIDOJUDICIAL",
					ScsPartidaPresupuestariaBean.T_NOMBRETABLA+"."+ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA				+" PARTIDAPRESUPUESTARIA",
					ScsPartidaPresupuestariaBean.T_NOMBRETABLA+"."+ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA	+" IDPARTIDAPRESUPUESTARIA",
					UtilidadesMultidioma.getCampoMultidiomaSimple(ScsGrupoFacturacionBean.T_NOMBRETABLA+"."+ScsGrupoFacturacionBean.C_NOMBRE,this.usrbean.getLanguage())								+" GRUPOFACTURACION",
					ScsGrupoFacturacionBean.T_NOMBRETABLA+"."+ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION					+" IDGRUPOFACTURACION",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS													+ " GUARDIAS",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARJUSTIFICACIONES									+ " VALIDARJUSTIFICACIONES",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARINSCRIPCIONES										+" VALIDARINSCRIPCIONES",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_DESIGNADIRECTA											+" DESIGNADIRECTA",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_DESCRIPCION												+" DESCRIPCION",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_REQUISITOS												+" REQUISITOS",
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDPERSONAULTIMO											+" IDPERSONAULTIMO",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS						+" IDORDENACIONCOLAS",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS					+" ALFABETICOAPELLIDOS",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_FECHANACIMIENTO						+" EDAD",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_NUMEROCOLEGIADO						+" ANTIGUEDAD",
					ScsOrdenacionColasBean.T_NOMBRETABLA+"."+ScsOrdenacionColasBean.C_ANTIGUEDADCOLA						+" ANTIGUEDADENCOLA",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD				+" OBSERVACIONESSOLICITUD",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION				+" OBSERVACIONESVALIDACION",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA+" OBSERVACIONESBAJA",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD+" FECHASOLICITUD",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA+" FECHASOLICITUDBAJA",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+" FECHAVALIDACION",
					ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHABAJA+" FECHABAJA",
					// rgg CAMBIO PARA METER PARTIDOS JUDICIALES
					" Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES(SCS_SUBZONA.idinstitucion,SCS_SUBZONA.IDSUBZONA,SCS_ZONA.IDZONA) PARTIDOS ",					
					" DECODE(FECHAVALIDACION, NULL, TO_CHAR(FECHASOLICITUD, 'YYYYMMDD'), '00000000') ||"+
					" DECODE(FECHABAJA, NULL, TO_CHAR(FECHASOLICITUD, 'YYYYMMDD'), '00000000') ||"+
			" DECODE(FECHABAJA, NULL, '99999999', TO_CHAR(FECHABAJA, 'YYYYMMDD')) ORDEN"};
			return campos;
		}
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
			bean.setIdPersonaUltimo(UtilidadesHash.getInteger(hash,ScsTurnoBean.C_IDPERSONAULTIMO));
			bean.setActivarRestriccionAcreditacion(UtilidadesHash.getString(hash,ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT));
			bean.setLetradoActuaciones(UtilidadesHash.getString(hash,ScsTurnoBean.C_LETRADOACTUACIONES));
			bean.setLetradoAsistencias(UtilidadesHash.getString(hash,ScsTurnoBean.C_LETRADOASISTENCIAS));
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
			UtilidadesHash.set(hash, ScsTurnoBean.C_LETRADOACTUACIONES, b.getLetradoActuaciones());
			UtilidadesHash.set(hash, ScsTurnoBean.C_LETRADOASISTENCIAS, b.getLetradoAsistencias());
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
	public Vector selectTabla(String where, String entrada){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasSelect(entrada), this.getCamposSelect(entrada));
			sql += where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getOrdenSelect(entrada));
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
	protected String[] getOrdenSelect(String entrada){
		if (entrada.equalsIgnoreCase("1")){
			String[] campos={"turnos."+ScsTurnoBean.C_ABREVIATURA};
			return campos;
		}
		else if(entrada.equalsIgnoreCase("2")){String[] campos={ " ORDEN DESC"};
		return campos;
		}
		else{
			String[] campos = null;
			campos[0] = "";
			return campos;
		}
	}
	
	public Vector getUltimoLetradoInscrito(String institucion, int ultimo){
		Vector v = new Vector();
		CenColegiadoAdm colegiados = new CenColegiadoAdm(this.usrbean);
		try {
			String where=" where "+
			CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION+"="+institucion+" and "+
			CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA+"="+ultimo+" ";
			v= colegiados.select(where);
		}catch(Exception e){
			e.printStackTrace();
		}
		return v;
	}
	
	public Vector getLetradosInscritos(int institucion, int turno){
		Vector vLetrados = new Vector();
		Vector v = new Vector();
		Vector vResult = new Vector();
		CenColegiadoAdm colegiados = new CenColegiadoAdm(this.usrbean);
		ScsInscripcionTurnoAdm inscripciones = new ScsInscripcionTurnoAdm(this.usrbean);
		CenPersonaAdm persona = new CenPersonaAdm(this.usrbean);
		try {
			try{
				String where=" where "+
				ScsInscripcionTurnoBean.C_IDINSTITUCION+"="+institucion+" and "+
				ScsInscripcionTurnoBean.C_IDTURNO+"="+turno+" and "+
				ScsInscripcionTurnoBean.C_FECHAVALIDACION+" is not null and "+
				ScsInscripcionTurnoBean.C_FECHABAJA+" is null ";
				vLetrados = inscripciones.select(where);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			for (int ind=0;ind < vLetrados.size();ind++){
				try{
					ScsInscripcionTurnoBean fila = (ScsInscripcionTurnoBean)vLetrados.get(ind);
					String where =" where "+CenColegiadoBean.C_IDPERSONA+"="+fila.getIdPersona()+" ";
					v=colegiados.select(where);
					CenColegiadoBean personaBean = (CenColegiadoBean)v.get(0);
					where = " where "+CenPersonaBean.C_IDPERSONA+"="+(personaBean.getIdPersona()).toString()+ " ";
					vResult.add((persona.select(where)).get(0));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return v;
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
	
	/**
	 * Efectúa un SELECT en la tabla SCS_TURNO con los datos introducidos. 
	 * 
	 * @param sql. Consulta a realizar
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectTabla(String sql){
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			rc = new RowsContainer();
			String contador = "";
			if (rc.query(sql)) {
				String institucion="";
				GenClientesTemporalAdm temporalAdm = new GenClientesTemporalAdm(this.usrbean);
				GenClientesTemporalBean temporalBean = new GenClientesTemporalBean();
				Hashtable miHash = new Hashtable();
				String consultaTemp ="";
				Object[] param_in = new Object[3];
				String resultadoPl[] = new String[3];
				int cont=0;
				
				//Por cada uno de los registros que se seleccionan se debe ejecutar el PL que da la posición del letrado dentro del turno
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					
					//Parametros de entrada del PL
					institucion=(String)registro.get("INSTITUCION");
					param_in[0] = institucion;			
					param_in[1] = (String)registro.get("IDTURNO");
					param_in[2] = "1"; // con saltos y compensaciones
					//Ejecucion del PL
					resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_ORDENACION.ORDENA_COLEGIADOS_TURNO (?,?,?,?,?,?)}", 3, param_in);
					//Resultado del PL
					contador = resultadoPl[0];
					
					//Consulta en la tabla temporal la posicion para el letrado
					consultaTemp=	" where "+
					GenClientesTemporalBean.T_NOMBRETABLA+"."+GenClientesTemporalBean.C_IDPERSONA+"="+(String)registro.get("IDPERSONA")+" and "+
					GenClientesTemporalBean.T_NOMBRETABLA+"."+GenClientesTemporalBean.C_CONTADOR+"="+contador+" and "+
					GenClientesTemporalBean.T_NOMBRETABLA+"."+GenClientesTemporalBean.C_SALTO+"<> 'S' ";
					
					try{
						temporalBean = (GenClientesTemporalBean)((Vector)temporalAdm.select(consultaTemp)).get(0);
						
						//Anhadir al registro que se devolverá el campo posición del letrado dentro del turno
						registro.put("POSICION",temporalBean.getPosicion().toString());
						
						//Anhadir el registro a v
						if (registro != null) 
							v.add(cont,registro);
						
						//Borrar de la tabla temporal por el campo contador
						if (!contador.equals("0")) {
							miHash.put("CONTADOR",contador);
							temporalAdm.delete(miHash);
						}
						cont++;
					}catch(Exception e){}
					
				}
			}
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return v;
	}	
	/**
	 * Efectúa un SELECT en la tabla SCS_TURNO con los datos introducidos. 
	 * @param institucion Codigo institucion seleccionada
	 * @param turno Codigo turno seleccionado
	 * @return Vector de Hashtable con los registros que cumplan la sentencia sql 
	 */
	public Vector selectLetradosEnCola(String institucion, String turno){
		Vector vResult = null;
		//Parametros de entrada del PL
		Object[] param_in = new Object[]{institucion,turno,"0"};// sin saltos ni compensaciones
		String resultadoPl[] = new String[3];
		try{
			
			//Ejecucion del PL
			resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_ORDENACION.ORDENA_COLEGIADOS_TURNO (?,?,?,?,?,?)}", 3, param_in);
			//Resultado del PL
			String contador = resultadoPl[0];
			
			//Consulta en la tabla temporal la posicion para el letrado
			String consultaTemp =
				"select "+
				"T."+GenClientesTemporalBean.C_IDPERSONA+", "+
				"decode(C."+CenColegiadoBean.C_COMUNITARIO+",'"+ClsConstants.DB_TRUE+"',"+"C."+CenColegiadoBean.C_NCOMUNITARIO+","+CenColegiadoBean.C_NCOLEGIADO+") "+CenColegiadoBean.C_NCOLEGIADO+", "+
				"P."+CenPersonaBean.C_NOMBRE     + ", " +
				"P."+CenPersonaBean.C_APELLIDOS1 + ", " +
				"P."+CenPersonaBean.C_APELLIDOS2 + ", " +
				"P."+CenPersonaBean.C_IDPERSONA  +
				" from "+
				GenClientesTemporalBean.T_NOMBRETABLA + " T, "+
				CenColegiadoBean.T_NOMBRETABLA+" C, "+
				CenPersonaBean.T_NOMBRETABLA+" P "+
				" where "+
				"T."+GenClientesTemporalBean.C_IDINSTITUCION+"=C."+CenColegiadoBean.C_IDINSTITUCION+" and "+
				"T."+GenClientesTemporalBean.C_IDPERSONA+"=C."+CenColegiadoBean.C_IDPERSONA+" and "+
				"T."+GenClientesTemporalBean.C_IDPERSONA+"=P."+CenPersonaBean.C_IDPERSONA+" and "+
				"T."+GenClientesTemporalBean.C_CONTADOR+"="+contador+" and "+
				"T."+GenClientesTemporalBean.C_SALTO+"<> 'S'"+
				" order by T."+GenClientesTemporalBean.C_POSICION;
			vResult=this.find(consultaTemp).getAll();
			
			//Borrar de la tabla temporal por el campo contador
			String deleteTemp =
				"delete "+GenClientesTemporalBean.T_NOMBRETABLA+
				" where "+GenClientesTemporalBean.C_CONTADOR+"="+contador;
			ClsMngBBDD.executeUpdate(deleteTemp);
		}
		catch(ClsExceptions e){
			e.printStackTrace();
		}
		return vResult;
	}	
	
	public Hashtable getDatosTurno(String idInstitucion, String idTurno) {
		String consulta =	" select turno.nombre nombre, turno.abreviatura abreviatura, turno.idarea idarea, turno.idmateria idmateria, turno.idzona idzona, "+
		" turno.idpartidapresupuestaria idpartidapresupuestaria, turno.idgrupofacturacion idgrupofacturacion, turno.guardias guardias, turno.descripcion descripcion,"+
		" turno.requisitos requisitos, turno.idordenacioncolas idordenacioncolas, turno.idpersona_ultimo idpersona_ultimo, turno.idsubzona idsubzona, area.nombre area,"+
		" materia.nombre materia, zona.nombre zona, subzona.nombre subzona, partida.nombrepartida partidapresupuestaria, turno.idordenacioncolas idordenacioncolas, turno.idturno idturno, turno.validarjustificaciones validarjustificaciones, turno.validarinscripciones validarinscripciones,"+
		" turno.designadirecta designadirecta, subzona.idpartido idpartidojudicial, "+
		"  PKG_SIGA_SJCS.FUN_SJ_PARTIDOSJUDICIALES(turno.idinstitucion, turno.idsubzona, turno.idzona) partidojudicial, " +
		" turno." + ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT + " " + ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT + ", " + 
		" turno." + ScsTurnoBean.C_LETRADOACTUACIONES + " " + ScsTurnoBean.C_LETRADOACTUACIONES + ", " + 
		" turno." + ScsTurnoBean.C_LETRADOASISTENCIAS + " " + ScsTurnoBean.C_LETRADOASISTENCIAS +  
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
	
	public Vector getTurnosLetrado (String idInstitucion, String idPersona) 
	{
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
						" AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL "+
						" AND SCS_INSCRIPCIONTURNO.idpersona = "+idPersona+" ";
		return this.selectTabla(where,"2");
	}
	
	/**
	 * Metodo que crea la select para usar con el paginador de turnos en la pestaña sjcs de la ficha colegial
	 * @param idInstitucion
	 * @param idPersona
	 * @param historico
	 * @return
	 * @throws ClsExceptions
	 */
	public String getSelectTurnosLetrado (String idInstitucion, String idPersona, boolean historico) throws ClsExceptions 
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
							where += " AND SCS_INSCRIPCIONTURNO.FECHABAJA IS NULL ";
						}
		sql += where;
		sql += " ORDER BY NOMBRE, FECHABAJA DESC";
		return sql;
	}

	
	
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
		
	public PaginadorBind getTurnosClientePaginador (Integer idInstitucion, Long idPersona, boolean historico) throws ClsExceptions, SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigos = new Hashtable();
			String select = getSelectTurnosLetrado(idInstitucion.toString(), idPersona.toString(), historico);
			paginador = new PaginadorBind(select,codigos);

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getServiciosSolicitadosPaginador.");
		}
		return paginador;  
		
	}
	
}