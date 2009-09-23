
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import java.util.Vector;


/**
* Bean de administracion de la tabla SCS_LISTAGUARDIAS
* 
* @author david.sanchezp
* @since 27/12/2004
*/

public class ScsListaGuardiasAdm extends MasterBeanAdministrador {


	/**
 	 * Constructor del bean de administracion de la tabla.
	 * @param Integer usuario: usuario.
	 */
	public ScsListaGuardiasAdm (UsrBean usuario) {
		super( ScsListaGuardiasBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String[] campos = {	ScsListaGuardiasBean.C_IDINSTITUCION, 			ScsListaGuardiasBean.C_IDLISTA,
							ScsListaGuardiasBean.C_NOMBRE,					ScsListaGuardiasBean.C_LUGAR,
							ScsListaGuardiasBean.C_OBSERVACIONES,			ScsListaGuardiasBean.C_FECHAMODIFICACION,		
							ScsListaGuardiasBean.C_USUMODIFICACION};
		return campos;
	}
	
	protected String[] getClavesBean() {
		String[] campos = {	ScsListaGuardiasBean.C_IDINSTITUCION, ScsListaGuardiasBean.C_IDLISTA};
		return campos;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsListaGuardiasBean bean = null;
		try{
			bean = new ScsListaGuardiasBean();
			bean.setIdLista(UtilidadesHash.getInteger(hash,ScsListaGuardiasBean.C_IDLISTA));			
			bean.setNombre(UtilidadesHash.getString(hash,ScsListaGuardiasBean.C_NOMBRE));
			bean.setLugar(UtilidadesHash.getString(hash,ScsListaGuardiasBean.C_LUGAR));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsListaGuardiasBean.C_OBSERVACIONES));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsListaGuardiasBean.C_IDINSTITUCION));															
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;	
	}
	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			String aux="";
			ScsListaGuardiasBean b = (ScsListaGuardiasBean) bean;
			hash.put(ScsListaGuardiasBean.C_IDLISTA, String.valueOf(b.getIdLista()));
			hash.put(ScsListaGuardiasBean.C_NOMBRE, b.getNombre());
			hash.put(ScsListaGuardiasBean.C_LUGAR, b.getLugar());
			hash.put(ScsListaGuardiasBean.C_OBSERVACIONES, String.valueOf(b.getObservaciones()));
			hash.put(ScsListaGuardiasBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

		
	protected String[] getOrdenCampos() {
		return null;
	}

	private String[] getOrderSelectBuscarListaGuardias(){
		String[] campos = { "listas."+ScsListaGuardiasBean.C_NOMBRE,
							"listas."+ScsListaGuardiasBean.C_LUGAR};
		return campos;
	}

	/** 
	 * Devuelve un String con la consulta SQL para saber las listas de guardias
	 * El select devuelve las columnas de la tabla SCS_LISTAGUARDIAS.
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarListaGuardias(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String institucion="", nombre="", lugar="", idpartido="", partido="", zona="", subzona="";
		boolean c_nombre=false, c_lugar=false, c_zona=false, c_subzona=false, c_partido=false;
		
		try {
			//Datos de la consulta obtenidos de una tabla hash
			institucion = (String)hash.get("INSTITUCION");
			nombre = (String)hash.get("LISTAGUARDIAS");
			lugar = (String)hash.get("LUGAR");
			idpartido = (String)hash.get("IDPARTIDO");
			zona = (String)hash.get("ZONA");
			subzona = (String)hash.get("SUBZONA");
			//partido = (String)hash.get("PARTIDO");
			
			//Condiciones de la consulta
			c_nombre = !nombre.equals("");
			c_lugar = !lugar.equals("");
			c_zona = !zona.equals("");
			c_subzona = !subzona.equals("");
			//c_partido = !partido.equals("");

			consulta = "SELECT distinct listas.* from ";
			consulta += " "+ScsListaGuardiasBean.T_NOMBRETABLA+" listas";
//			if (c_zona || c_subzona || c_partido) {
			if (c_zona || c_subzona) {
				consulta += " ,"+ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+" inclus";
				consulta += " ,"+ScsTurnoBean.T_NOMBRETABLA+" turnos";
			}
//			if (c_subzona || c_partido) consulta += " ,"+ScsSubzonaBean.T_NOMBRETABLA+" subzon";
			if (c_subzona) consulta += " ,"+ScsSubzonaBean.T_NOMBRETABLA+" subzon";
			if (c_zona)    consulta += " ,"+ScsZonaBean.T_NOMBRETABLA+" zona";
//			if (c_partido) consulta += " ,"+CenPartidoJudicialBean.T_NOMBRETABLA+" partid";
			consulta += " WHERE ";
//			if (c_zona || c_subzona || c_partido) {
			if (c_zona || c_subzona) {
				consulta += " listas."+ScsListaGuardiasBean.C_IDINSTITUCION+"= inclus."+ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION;
				consulta += " AND listas."+ScsListaGuardiasBean.C_IDLISTA+" = inclus."+ScsInclusionGuardiasEnListasBean.C_IDLISTA;
				consulta += " AND turnos."+ScsTurnoBean.C_IDINSTITUCION+" = inclus."+ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION;
				consulta += " AND turnos."+ScsTurnoBean.C_IDTURNO+" = inclus."+ScsInclusionGuardiasEnListasBean.C_IDTURNO+" AND";
			}
//			if (c_subzona || c_partido) {
			if (c_subzona) {
				consulta += " subzon."+ScsSubzonaBean.C_IDINSTITUCION+" = turnos."+ScsTurnoBean.C_IDINSTITUCION;
				consulta += " AND subzon."+ScsSubzonaBean.C_IDZONA+" = turnos."+ScsTurnoBean.C_IDZONA;
				consulta += " AND subzon."+ScsSubzonaBean.C_IDSUBZONA+" = turnos."+ScsTurnoBean.C_IDSUBZONA+" AND";
			}
			if (c_subzona)
				  consulta +=  ComodinBusquedas.prepararSentenciaCompleta(subzona.trim(),"subzon."+ScsSubzonaBean.C_NOMBRE)+" AND";
			if (c_zona) {
				consulta += " zona."+ScsZonaBean.C_IDINSTITUCION+" = turnos."+ScsTurnoBean.C_IDINSTITUCION;
				consulta += " AND zona."+ScsZonaBean.C_IDZONA+" = turnos."+ScsTurnoBean.C_IDZONA;
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(zona.trim(),"zona."+ScsZonaBean.C_NOMBRE)+" AND";
				
			}
			consulta += " listas.IDINSTITUCION = "+institucion;
			if (c_nombre) consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"listas."+ScsListaGuardiasBean.C_NOMBRE);
			if (c_lugar)  consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(lugar.trim(),"listas."+ScsListaGuardiasBean.C_LUGAR);
//			
			
			//ORDEN DE LA CONSULTA
			consulta += UtilidadesBDAdm.sqlOrderBy(this.getOrderSelectBuscarListaGuardias());
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsListaGuardiasAdm.buscarListaGuardias(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	private String[] getOrderbuscarGuardiasEnListaGuardias(){
		String[] campos = { "inclus."+ScsInclusionGuardiasEnListasBean.C_ORDEN, "turnos."+ScsTurnoBean.C_NOMBRE,
							"guardi."+ScsGuardiasTurnoBean.C_NOMBRE};
		return campos;
	}
	
	/** 
	 * Devuelve un String con la consulta SQL para saber
	 * las guardias incluidas en la listas de guardias
	 * El select devuelve las columnas ORDEN, TURNO, GUARDIA y 
	 * SELECCIONLABORABLES, SELECCIONFESTIVOS.
	 * 
	 * @param String idInstitucion: id de la Institucion 
	 * @param String idLista: id de la Lista
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarGuardiasEnListaGuardias (String idInstitucion, 
												 String idLista)
			throws ClsExceptions
	{
		String consulta = "";
		
		try
		{
			consulta = 
				"SELECT inclus."+ScsInclusionGuardiasEnListasBean.C_ORDEN+"," +
				"       turnos."+ScsTurnoBean.C_NOMBRE+" AS TURNO," +
				"       guardi."+ScsGuardiasTurnoBean.C_NOMBRE+" AS GUARDIA," +
				"       guardi."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+"," +
				"       guardi."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+"," +
				"       inclus."+ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION+"," +
				"       inclus."+ScsInclusionGuardiasEnListasBean.C_IDLISTA+"," +
				"       inclus."+ScsInclusionGuardiasEnListasBean.C_IDGUARDIA+"," +
				"       inclus."+ScsInclusionGuardiasEnListasBean.C_IDTURNO +
				"" +
				"  FROM "+ScsListaGuardiasBean.T_NOMBRETABLA+" listas," +
				"       "+ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+" inclus," +
				"       "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"       "+ScsTurnoBean.T_NOMBRETABLA+" turnos" +
				"" +
				" WHERE listas.IDINSTITUCION = inclus.IDINSTITUCION" +
				"   AND listas.IDLISTA = inclus.IDLISTA" +
				"   AND guardi.IDINSTITUCION = inclus.IDINSTITUCION" +
				"   AND guardi.IDTURNO = inclus.IDTURNO" +
				"   AND guardi.IDGUARDIA = inclus.IDGUARDIA" +
				"   AND turnos.IDINSTITUCION = guardi.IDINSTITUCION" +
				"   AND turnos.IDTURNO = guardi.IDTURNO" +
				"   AND inclus.IDINSTITUCION = "+idInstitucion +
				"   AND inclus.IDLISTA = "+idLista;			
			
			//ORDEN DE LA CONSULTA
			consulta += UtilidadesBDAdm.sqlOrderBy(this.getOrderbuscarGuardiasEnListaGuardias());
		}
		catch (Exception e){
			throw new ClsExceptions(e,
					"Excepcion en ScsListaGuardiasAdm.buscarListaGuardias(). " +
					"Consulta SQL:"+consulta);
		}
		
		return consulta;
	} //buscarGuardiasEnListaGuardias ()

	/** 
	 * Devuelve un String con la consulta SQL para saber las guardias a partir de los datos:<br>
	 * AbrebiaturaTurno,Turno,Zona,Subzona,Area,Materia y PartidoJudicial.
	 * El select devuelve las columnas TURNO Y GUARDIA.
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarGuardias(Hashtable hash) throws ClsExceptions{
		String consulta = "";
		String idinstitucion="", idlista="", abreviatura="", turno="", partido="", zona="", subzona="", area="", materia="", idpartido="";
		boolean c_abreviatura=false, c_turno=false, c_partido=false, c_area=false, c_materia=false, c_zona=false, c_subzona=false;
		
		try {
			//Datos de la consulta obtenidos de una tabla hash
			idinstitucion = (String)hash.get("IDINSTITUCION");
			idlista = (String)hash.get("IDLISTA");
			abreviatura = (String)hash.get("ABREVIATURA");
			turno = (String)hash.get("TURNO");
			//partido = (String)hash.get("PARTIDO");
			zona = (String)hash.get("ZONA");
			subzona = (String)hash.get("SUBZONA");
			area = (String)hash.get("AREA");
			materia = (String)hash.get("MATERIA");
			
			//Condiciones de la consulta
			c_abreviatura = !abreviatura.equals("");
			c_turno = !turno.equals("");
			//c_partido = !partido.equals("");
			c_area = !area.equals("");
			c_materia = !materia.equals("");
			c_zona = !zona.equals("");
			c_subzona = !subzona.equals("");
			

			consulta = "SELECT TURNO, GUARDIA , IDTURNO, IDGUARDIA FROM ";
			consulta += "(";
			
			consulta += " SELECT ";
			consulta += " turnos."+ScsTurnoBean.C_NOMBRE+" TURNO,guardi."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,";
			consulta += " turnos."+ScsTurnoBean.C_IDTURNO+" IDTURNO,guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA";
			consulta += " FROM ";
			consulta += ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi,"+ScsTurnoBean.T_NOMBRETABLA+" turnos,";
			consulta += ScsMateriaBean.T_NOMBRETABLA+" materi,"+ScsAreaBean.T_NOMBRETABLA+" area,";
			consulta += ScsSubzonaBean.T_NOMBRETABLA+" subzon,"+ScsZonaBean.T_NOMBRETABLA+" zona ";			
			//consulta += CenPartidoJudicialBean.T_NOMBRETABLA+" partid ";
			consulta += " WHERE ";
			consulta += " turnos."+ScsTurnoBean.C_IDINSTITUCION+"=guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION;
			consulta += " AND turnos."+ScsTurnoBean.C_IDTURNO+"=guardi."+ScsGuardiasTurnoBean.C_IDTURNO;
			consulta += " AND materi."+ScsMateriaBean.C_IDINSTITUCION+"=turnos."+ScsTurnoBean.C_IDINSTITUCION;
			consulta += " AND materi."+ScsMateriaBean.C_IDAREA+"=turnos."+ScsTurnoBean.C_IDAREA;
			consulta += " AND materi."+ScsMateriaBean.C_IDMATERIA+"=turnos."+ScsTurnoBean.C_IDMATERIA;
			consulta += " AND area."+ScsAreaBean.C_IDINSTITUCION+"=materi."+ScsMateriaBean.C_IDINSTITUCION;
			consulta += " AND area."+ScsAreaBean.C_IDAREA+"=materi."+ScsMateriaBean.C_IDAREA;
			consulta += " AND subzon."+ScsSubzonaBean.C_IDINSTITUCION+"(+)=turnos."+ScsTurnoBean.C_IDINSTITUCION;
			consulta += " AND subzon."+ScsSubzonaBean.C_IDZONA+"(+)=turnos."+ScsTurnoBean.C_IDZONA;
			consulta += " AND subzon."+ScsSubzonaBean.C_IDSUBZONA+"(+)=turnos."+ScsTurnoBean.C_IDSUBZONA;
			//consulta += " AND partid."+CenPartidoJudicialBean.C_IDPARTIDO+"(+)=subzon."+ScsSubzonaBean.C_IDPARTIDO;
			consulta += " AND zona."+ScsZonaBean.C_IDINSTITUCION+"=turnos."+ScsTurnoBean.C_IDINSTITUCION;
			consulta += " AND zona."+ScsZonaBean.C_IDZONA+"=turnos."+ScsTurnoBean.C_IDZONA;
			consulta += " AND guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idinstitucion;
			if (c_abreviatura)
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(abreviatura.trim(),"turnos."+ScsTurnoBean.C_ABREVIATURA);
			if (c_turno)
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(turno.trim(),"turnos."+ScsTurnoBean.C_NOMBRE);
			if (c_zona)
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(zona.trim(),"zona."+ScsZonaBean.C_NOMBRE);
			if (c_subzona)
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(subzona.trim(),"subzon."+ScsSubzonaBean.C_NOMBRE);
			if (c_area)
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(area.trim(),"area."+ScsAreaBean.C_NOMBRE);
			if (c_materia)
				consulta += " AND "+ComodinBusquedas.prepararSentenciaCompleta(materia.trim(),"materi."+ScsMateriaBean.C_NOMBRE);
			
			
			consulta += " MINUS ";
			
			consulta += " SELECT ";
			consulta += " turnos."+ScsTurnoBean.C_NOMBRE+",guardi."+ScsGuardiasTurnoBean.C_NOMBRE+",";
			consulta += " turnos."+ScsTurnoBean.C_IDTURNO+" IDTURNO,guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA";
			consulta += " FROM ";
			consulta += ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA+" inclus,";
			consulta += ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi,"+ScsTurnoBean.T_NOMBRETABLA+" turnos";
			consulta += " WHERE ";
			consulta += " guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+"=inclus."+ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION;
			consulta += " AND guardi."+ScsGuardiasTurnoBean.C_IDTURNO+"=inclus."+ScsInclusionGuardiasEnListasBean.C_IDTURNO;
			consulta += " AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+"=inclus."+ScsInclusionGuardiasEnListasBean.C_IDGUARDIA;
			consulta += " AND turnos."+ScsTurnoBean.C_IDINSTITUCION+"=guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION;
			consulta += " AND turnos."+ScsTurnoBean.C_IDTURNO+"=guardi."+ScsGuardiasTurnoBean.C_IDTURNO;
			consulta += " AND inclus."+ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND inclus."+ScsInclusionGuardiasEnListasBean.C_IDLISTA+"="+idlista;			
			
			consulta += ")";
			//ORDEN DE LA CONSULTA
			consulta += " ORDER BY 1,2";
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsListaGuardiasAdm.buscarGuardias(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve un registro de una columna llamada IDLISTA con el <br>
	 * nuevo ID.
	 * @param String idinstitucion
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getIdLista(String idinstitucion) throws ClsExceptions{
		String consulta = "";
		
		try {
			consulta = "SELECT MAX("+ScsListaGuardiasBean.C_IDLISTA+") + 1 AS IDLISTA FROM "+ScsListaGuardiasBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsListaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;		
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsListaGuardiasAdm.getIdLista(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}	
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve un registro a partir de los identificadores de la tabla usados.
	 * @param String idinstitucion
	 * @param String idlista
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getFechaYUsu(String idinstitucion, String idlista) throws ClsExceptions{
		String consulta = "";
		
		try {
			consulta = "SELECT * ";
			consulta += " FROM "+ScsListaGuardiasBean.T_NOMBRETABLA+" lista";
			consulta += " WHERE ";
			consulta += " lista."+ScsListaGuardiasBean.C_IDINSTITUCION+"="+idinstitucion;
			consulta += " AND lista."+ScsListaGuardiasBean.C_IDLISTA+"="+idlista;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsListaGuardiasAdm.getIdLista(). Consulta SQL:"+consulta);
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
			throw new ClsExceptions (e, "Excepcion en ScsListaGuardiasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	
	
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL con pool NLS.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenericoNLS(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.queryNLS(consulta)) {
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
			throw new ClsExceptions (e, "Excepcion en ScsListaGuardiasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}
}