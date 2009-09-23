
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

import java.util.Vector;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_INCOMPATIBILIDADGUARDIAS
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 * @version david.sanchezp 18/01/2005: para incluir nuevos métodos: 
 * 		selectGenerico,buscarGuardiasIncompatibles
 * @version david.sanchezp 18/01/2005: para incluir nuevos métodos: 
 * 		buscarGuardiasModal,getCamposBean,beanToHashTable,hashTableToBean
 * @version adrian.ayala 18/03/2008: revision global de incompatibilidades
 * @version adrian.ayala 14/05/2008: sustitucion del campo tipodias
 */

public class ScsIncompatibilidadGuardiasAdm extends MasterBeanAdministrador
{
	//////////////////// CONSTRUCTOR ////////////////////
	/**
	 * Constructor de la clase
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsIncompatibilidadGuardiasAdm (UsrBean usuario) {
		super( ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA, usuario);
	}
	
	
	
	//////////////////// METODOS BASICOS DEL ADMINISTRADOR ////////////////////
	/** 
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean () {
		String[] campos =
		{
			ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION,
			ScsIncompatibilidadGuardiasBean.C_IDTURNO,
			ScsIncompatibilidadGuardiasBean.C_IDGUARDIA,
			ScsIncompatibilidadGuardiasBean.C_FECHAMODIFICACION,
			ScsIncompatibilidadGuardiasBean.C_USUMODIFICACION,
			ScsIncompatibilidadGuardiasBean.C_MOTIVOS,
			ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE,
			ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE,
			ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS
		};
		return campos;
	} //getCamposBean ()
	
	/** 
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean ()
	{
		String[] campos =
		{
			ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION,
			ScsIncompatibilidadGuardiasBean.C_IDTURNO,
			ScsIncompatibilidadGuardiasBean.C_IDGUARDIA,
			ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE,
			ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE
		};
		return campos;
	} //getClavesBean ()
	
	/** 
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	protected MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		ScsIncompatibilidadGuardiasBean bean = null;
		try
		{
			bean = new ScsIncompatibilidadGuardiasBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsIncompatibilidadGuardiasBean.C_IDTURNO));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsIncompatibilidadGuardiasBean.C_IDGUARDIA));
			bean.setMotivos(UtilidadesHash.getString(hash,ScsIncompatibilidadGuardiasBean.C_MOTIVOS));
			bean.setIdTurnoIncompatible(UtilidadesHash.getInteger(hash,ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE));
			bean.setIdGuardiaIncompatible(UtilidadesHash.getInteger(hash,ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE));
			bean.setDiasseparacionguardias(UtilidadesHash.getInteger(hash,ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS));
		}
		catch(Exception e) {
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
	protected Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			ScsIncompatibilidadGuardiasBean b = (ScsIncompatibilidadGuardiasBean) bean;
			hash.put(ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			hash.put(ScsIncompatibilidadGuardiasBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			hash.put(ScsIncompatibilidadGuardiasBean.C_IDGUARDIA, String.valueOf(b.getIdGuardia()));
			hash.put(ScsIncompatibilidadGuardiasBean.C_MOTIVOS, String.valueOf(b.getMotivos()));
			hash.put(ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE, String.valueOf(b.getIdTurnoIncompatible()));
			hash.put(ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE, String.valueOf(b.getIdGuardiaIncompatible()));
			hash.put(ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS, String.valueOf(b.getDiasseparacionguardias()));
		}
		catch (Exception e) {
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
	protected String[] getOrdenCampos () {
		return null;
	}
	
	
	
	//////////////////// OTROS METODOS ////////////////////
	/** 
	 * Devuelve un String con la consulta SQL que devuelve las posibles guardias incompatibles a escoger en la modal.
	 * @param Hashtable hash: tabla hash con los datos necesarios para la busqueda
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarGuardiasModal (Hashtable hash)
			throws ClsExceptions
	{
		String consulta = "";
		String idinstitucion="",abreviatura="",turno="", zona="", subzona="", area="", materia="";
		String idinstitucion_pestanha="",idturno_pestanha="",idguardia_pestanha="";
		
		try
		{
			//tomando valores para la pestanha
			idinstitucion_pestanha = (String)hash.get("IDINSTITUCIONPESTAÑA");
			idturno_pestanha = (String)hash.get("IDTURNOPESTAÑA");
			idguardia_pestanha = (String)hash.get("IDGUARDIAPESTAÑA");
			
			//tomando valores del Hash
			abreviatura = (String)hash.get("ABREVIATURA");
			turno = (String)hash.get("TURNO");
			zona = (String)hash.get("ZONA");
			subzona = (String)hash.get("SUBZONA");
			area = (String)hash.get("AREA");
			materia = (String)hash.get("MATERIA");
			idinstitucion = (String)hash.get("IDINSTITUCION");
			
			consulta =
				"SELECT TURNO," +
				"       GUARDIA," +
				"       SELECCIONLABORABLES," +
				"       SELECCIONFESTIVOS," +
				"       IDTURNO_INCOMPATIBLE," +
				"       IDGUARDIA_INCOMPATIBLE" +
				"" +
				"  FROM" +
				"       (SELECT turnos."+ScsTurnoBean.C_NOMBRE+" AS TURNO," +
				"               guardi."+ScsGuardiasTurnoBean.C_NOMBRE+" AS GUARDIA," +
				"               guardi."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+" AS SELECCIONLABORABLES," +
				"               guardi."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+" AS SELECCIONFESTIVOS," +
				"               turnos."+ScsTurnoBean.C_IDTURNO+" AS IDTURNO_INCOMPATIBLE," +
				"               guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" AS IDGUARDIA_INCOMPATIBLE" +
				"" +
				"          FROM "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"               "+ScsTurnoBean.T_NOMBRETABLA+" turnos," +
				"               "+ScsMateriaBean.T_NOMBRETABLA+" materi," +
				"               "+ScsAreaBean.T_NOMBRETABLA+" area," +
				"               "+ScsSubzonaBean.T_NOMBRETABLA+" subzon," +
				"               "+ScsZonaBean.T_NOMBRETABLA+" zona," +
				"               "+CenInfluenciaBean.T_NOMBRETABLA+" influ" +
				"" +
				"         WHERE turnos."+ScsTurnoBean.C_IDINSTITUCION+" = guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION +
				"           AND turnos."+ScsTurnoBean.C_IDTURNO+" = guardi."+ScsGuardiasTurnoBean.C_IDTURNO +
				"           AND materi."+ScsMateriaBean.C_IDINSTITUCION+" = turnos."+ScsTurnoBean.C_IDINSTITUCION +
				"           AND materi."+ScsMateriaBean.C_IDAREA+" = turnos."+ScsTurnoBean.C_IDAREA +
				"           AND materi."+ScsMateriaBean.C_IDMATERIA+" = turnos."+ScsTurnoBean.C_IDMATERIA +
				"           AND area."+ScsAreaBean.C_IDINSTITUCION+" = materi."+ScsMateriaBean.C_IDINSTITUCION +
				"           AND area."+ScsAreaBean.C_IDAREA+" = materi."+ScsMateriaBean.C_IDAREA +
				"           AND subzon."+ScsSubzonaBean.C_IDINSTITUCION+"(+) = turnos."+ScsTurnoBean.C_IDINSTITUCION +
				"           AND subzon."+ScsSubzonaBean.C_IDZONA+"(+) = turnos."+ScsTurnoBean.C_IDZONA +
				"           AND subzon."+ScsSubzonaBean.C_IDSUBZONA+"(+) = turnos."+ScsTurnoBean.C_IDSUBZONA +
				"           AND zona."+ScsZonaBean.C_IDINSTITUCION+" = turnos."+ScsTurnoBean.C_IDINSTITUCION +
				"           AND zona."+ScsZonaBean.C_IDZONA+" = turnos."+ScsTurnoBean.C_IDZONA +
				"           AND guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = "+idinstitucion;
			
			if (!abreviatura.equalsIgnoreCase(""))
				consulta +=
				"           AND "+ComodinBusquedas.prepararSentenciaCompleta(abreviatura.trim(),"turnos."+ScsTurnoBean.C_ABREVIATURA);
			if (!turno.equalsIgnoreCase(""))
				consulta +=
				"           AND "+ComodinBusquedas.prepararSentenciaCompleta(turno.trim(),"turnos."+ScsTurnoBean.C_NOMBRE);
			if (!zona.equalsIgnoreCase(""))
				consulta +=
				"           AND "+ComodinBusquedas.prepararSentenciaCompleta(zona.trim(),"zona."+ScsZonaBean.C_NOMBRE);
			if (!subzona.equalsIgnoreCase(""))
				consulta +=
				"           AND "+ComodinBusquedas.prepararSentenciaCompleta(subzona.trim(),"subzon."+ScsSubzonaBean.C_NOMBRE);
			if (!area.equalsIgnoreCase(""))
				consulta +=
				"           AND "+ComodinBusquedas.prepararSentenciaCompleta(area.trim(),"area."+ScsAreaBean.C_NOMBRE);
			if (!materia.equalsIgnoreCase(""))
				consulta +=
				"           AND "+ComodinBusquedas.prepararSentenciaCompleta(materia.trim(),"materi."+ScsMateriaBean.C_NOMBRE);
			
			consulta += 
				"           AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+"<>"+idguardia_pestanha +
			
// PROVISIONAL
				"           AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA +" NOT IN" +
				"               (SELECT incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA +
				"                  FROM "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" incomp," +
				"                       "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"                       "+ScsTurnoBean.T_NOMBRETABLA+" turnos" +
				"                 WHERE guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"                   AND guardi."+ScsGuardiasTurnoBean.C_IDTURNO+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE +
				"                   AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE +
				"                   AND turnos."+ScsTurnoBean.C_IDINSTITUCION+" = guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION +
				"                   AND turnos."+ScsTurnoBean.C_IDTURNO+" = guardi."+ScsGuardiasTurnoBean.C_IDTURNO +
				"                   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idinstitucion +
				"                   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = "+idturno_pestanha +
				"                   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+"="+idguardia_pestanha +
				"                UNION " +
				"                SELECT incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA +
				"                  FROM "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" incomp," +
				"                       "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"                       "+ScsTurnoBean.T_NOMBRETABLA+" turnos" +
				"                 WHERE guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"                   AND guardi."+ScsGuardiasTurnoBean.C_IDTURNO+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO +
				"                   AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA +
				"                   AND turnos."+ScsTurnoBean.C_IDINSTITUCION+" = guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION +
				"                   AND turnos."+ScsTurnoBean.C_IDTURNO+" = guardi."+ScsGuardiasTurnoBean.C_IDTURNO +
				"                   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idinstitucion +
				"                   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = "+idturno_pestanha +
				"                   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = "+idguardia_pestanha +
				"               )" +
				"" +
				"        MINUS" +
				"" +
				"        SELECT turnos."+ScsTurnoBean.C_NOMBRE+" AS TURNO," +
				"               guardi."+ScsGuardiasTurnoBean.C_NOMBRE+" AS GUARDIA," +
				"               guardi."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+" AS SELECCIONLABORABLES," +
				"               guardi."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+" AS SELECCIONFESTIVOS," +
				"               incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" AS IDTURNO_INCOMPATIBLE," +
				"               incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" AS IDGUARDIA_INCOMPATIBLE" +
				"          FROM "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" incomp," +
				"               "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"               "+ScsTurnoBean.T_NOMBRETABLA+" turnos" +
				"         WHERE guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"           AND guardi."+ScsGuardiasTurnoBean.C_IDTURNO+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE +
				"           AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE +
				"           AND turnos."+ScsTurnoBean.C_IDINSTITUCION+" = guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION +
				"           AND turnos."+ScsTurnoBean.C_IDTURNO+" = guardi."+ScsGuardiasTurnoBean.C_IDTURNO +
				"           AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idinstitucion_pestanha +
				"           AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = "+idturno_pestanha +
				"           AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = "+idguardia_pestanha +
				"" +
				"       ) " +
				"" +
				" ORDER BY 1,2";
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ScsIncompatibilidadGuardiasAdm.buscarGuardiasModal(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	}		
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve un registro
	 *   a partir de los identificadores de la tabla usados.
	 * @param String idinstitucion
	 * @param String idturno
	 * @param String idguardia
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String buscarIncompatibilidades (String idinstitucion,
											String idturno1, String idguardia1,
											String idturno2, String idguardia2)
		throws ClsExceptions
	{
		String consulta = "";
		
		try {
			consulta +=
				" select nvl((select 1 " +
			                  " from "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" " +
			                 " where "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = g1."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" " +
			                   " and (("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+" and " +
			                       " "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" and " +
			                       " "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+") or " +
			                       " ("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+" and " +
			                       " "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" and " +
			                       " "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+")) " +
			                   " and rownum=1), '') as EXISTE, " +
			           " (select "+ScsTurnoBean.C_NOMBRE+" " +
			              " from "+ScsTurnoBean.T_NOMBRETABLA+" " +
			             " where "+ScsTurnoBean.C_IDTURNO+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" " +
			               " and "+ScsTurnoBean.C_IDINSTITUCION+" = g1."+ScsGuardiasTurnoBean.C_IDINSTITUCION+") as TURNO, " +
			           " g1."+ScsGuardiasTurnoBean.C_IDTURNO+" as IDTURNO, " +
			           " g1."+ScsGuardiasTurnoBean.C_NOMBRE+" as GUARDIA, " +
			           " g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+" as IDGUARDIA, " +
			           " (select "+ScsTurnoBean.C_NOMBRE+" " +
			              " from "+ScsTurnoBean.T_NOMBRETABLA+" " +
			             " where "+ScsTurnoBean.C_IDTURNO+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" " +
			               " and "+ScsTurnoBean.C_IDINSTITUCION+" = g2."+ScsGuardiasTurnoBean.C_IDINSTITUCION+") as TURNO_INCOMPATIBLE, " +
			           " g2."+ScsGuardiasTurnoBean.C_IDTURNO+" as IDTURNO_INCOMPATIBLE, " +
			           " g2."+ScsGuardiasTurnoBean.C_NOMBRE+" as GUARDIA_INCOMPATIBLE, " +
			           " g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+" as IDGUARDIA_INCOMPATIBLE, " +
			           " nvl((select "+ScsIncompatibilidadGuardiasBean.C_MOTIVOS+" " +
			              " from "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" " +
			             " where "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = g1."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" " +
			               " and (("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+") or " +
			                   " ("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+")) " +
			               " and rownum=1), '') as MOTIVOS, " +
			           " nvl((select "+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+" " +
			              " from "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" " +
			             " where "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = g1."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" " +
			               " and (("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+") or " +
			                   " ("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = g2."+ScsGuardiasTurnoBean.C_IDTURNO+" and "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = g1."+ScsGuardiasTurnoBean.C_IDTURNO+" and " +
			                   " "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+")) " +
			               " and rownum=1), '') as DIASSEPARACIONGUARDIAS " +

			    " from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" G1, "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" G2 " +
			   " where g1."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = g2."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" " +
			     " and (g1."+ScsGuardiasTurnoBean.C_IDTURNO+" <> g2."+ScsGuardiasTurnoBean.C_IDTURNO+" or g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+" <> g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+") " +
			     " and g1."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = " + idinstitucion + " ";
			
			if (! idturno1.equalsIgnoreCase ("")) {
			     consulta += " and g1."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + idturno1;
			     if (! idguardia1.equalsIgnoreCase (""))
			    	 consulta += " and g1."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = " + idguardia1;
			}
			if (! idturno2.equalsIgnoreCase ("")) {
			     consulta += " and g2."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + idturno2;
			     if (! idguardia2.equalsIgnoreCase (""))
			    	 consulta += " and g2."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = " + idguardia2;
			}
			
			consulta += " order by turno, guardia, turno_incompatible, guardia_incompatible ";
		}
		catch (Exception e) {
			throw new ClsExceptions
				(e, "Excepcion en ScsIncompatibilidadGuardiasAdm." + 
					"buscarIncompatibilidades(). Consulta SQL:" + consulta);
		}
		
		return consulta;
	} //buscarIncompatibilidades ()
	
	/** 
	 * Devuelve un String con la consulta SQL que devuelve un registro 
	 * a partir de los identificadores de la tabla usados
	 * 
	 * @param String idinstitucion
	 * @param String idturno
	 * @param String idguardia
	 * @return String con la consulta SQL
	 * @throws ClsExceptions
	 */	
	public String buscarGuardiasIncompatibles (String idinstitucion,
											   String idturno,
											   String idguardia)
			throws ClsExceptions
	{
		String consulta = "";
		
		try
		{
			consulta = 
				"SELECT turnos."+ScsTurnoBean.C_NOMBRE+" AS TURNO," +
				"       guardi."+ScsGuardiasTurnoBean.C_NOMBRE+" AS GUARDIA," +
				"       guardi."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+"," +
				"       guardi."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+"," +
				"       incomp."+ScsIncompatibilidadGuardiasBean.C_MOTIVOS+"," +
				"       incomp."+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+"," +
				"       incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"  FROM "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" incomp," +
				"       "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"       "+ScsTurnoBean.T_NOMBRETABLA+" turnos" +
				" WHERE guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"   AND guardi."+ScsGuardiasTurnoBean.C_IDTURNO+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE +
				"   AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE +
				"   AND turnos."+ScsTurnoBean.C_IDINSTITUCION+" = guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION +
				"   AND turnos."+ScsTurnoBean.C_IDTURNO+" = guardi."+ScsGuardiasTurnoBean.C_IDTURNO +
				"   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idinstitucion +
				"   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO+" = "+idturno +
				"   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+" = "+idguardia +
				"" +
				" UNION " +
				"" +
				"SELECT turnos."+ScsTurnoBean.C_NOMBRE+" AS TURNO," +
				"       guardi."+ScsGuardiasTurnoBean.C_NOMBRE+" AS GUARDIA," +
				"       guardi."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+"," +
				"       guardi."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+"," +
				"       incomp."+ScsIncompatibilidadGuardiasBean.C_MOTIVOS+"," +
				"       incomp."+ScsIncompatibilidadGuardiasBean.C_DIASSEPARACIONGUARDIAS+"," +
				"       incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"  FROM "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" incomp," +
				"       "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" guardi," +
				"       "+ScsTurnoBean.T_NOMBRETABLA+" turnos" +
				" WHERE guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION +
				"   AND guardi."+ScsGuardiasTurnoBean.C_IDTURNO+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO +
				"   AND guardi."+ScsGuardiasTurnoBean.C_IDGUARDIA+" = incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA +
				"   AND turnos."+ScsTurnoBean.C_IDINSTITUCION+" = guardi."+ScsGuardiasTurnoBean.C_IDINSTITUCION +
				"   AND turnos."+ScsTurnoBean.C_IDTURNO+" = guardi."+ScsGuardiasTurnoBean.C_IDTURNO +
				"   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+" = "+idinstitucion +
				"   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+" = "+idturno +
				"   AND incomp."+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" = "+idguardia;
		}
		catch (Exception e){
			throw new ClsExceptions(e,
					"Excepcion en ScsIncompatibilidadGuardiasAdm." +
					"buscarGuardiasIncompatibles(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	} //buscarGuardiasIncompatibles ()
	
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
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsIncompatibilidadGuardiasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		
	/**
	 * Se hace llamada a este metodo cuando en la busqueda se ignoran las mayusculas, minusculas, acentos...
	 * @param consulta
	 * @return
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
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsIncompatibilidadGuardiasAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}		
	
	/**
	 * Borra las incompatibilidades dada una lista de ellas
	 * @param String idinstitucion: codigo de la institucion 
	 * @param String lista: lista formada por cuadruplas (idturno, idguardia, 
	 * idturno_incompatible, idguardia_incompatible) y separadas por comas
	 * @return void
	 */
	public void deleteIncompatibilidadesGuardias (String idinstitucion,
												  String lista)
			throws ClsExceptions
	{
		String consulta = "";
		try {
			consulta += " delete from "+ScsIncompatibilidadGuardiasBean.T_NOMBRETABLA+" ";
			consulta += " where "+ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION+"="+idinstitucion+" ";
			consulta += "   and ("+ScsIncompatibilidadGuardiasBean.C_IDTURNO+", ";
			consulta += "        "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA+", ";
			consulta += "        "+ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE+", ";
			consulta += "        "+ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE+" ";
			consulta += "       ) in ("+lista+")";
			
			ClsMngBBDD.executeUpdate (consulta);
		} catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsIncompatibilidadGuardiasAdm.selectGenerico(). Consulta SQL:" + consulta);
		}
	} //deleteIncompatibilidadesGuardias ()
	
}