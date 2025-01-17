package com.siga.beans;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ESTADOS_EJG;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.envios.service.SalidaEnviosService;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinicionRemesas_CAJG_Form;
import com.siga.gratuita.form.DefinirEJGForm;

import es.satec.businessManager.BusinessManager;

//Clase: ScsEJGAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificaci�n: 17/02/2005 
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * 
 */ 
public class ScsEJGAdm extends MasterBeanAdministrador {
	private static final Logger log = Logger.getLogger(ScsEJGAdm.class);
	public static enum TipoVentana {
		BUSQUEDA_EJG,
		BUSQUEDA_PREPARACION_CAJG,
		BUSQUEDA_ANIADIR_REMESA,	
		BUSQUEDA_ANIADIR_REMESARECONOMICA
	}
	
	private final String keyBindConsulta="keyBindConsulta";
	private final String keyBindCodigos="keyBindCodigos";
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicaci�n. De tipo "Integer".  
	 */
	public ScsEJGAdm(UsrBean usuario) {
		super(ScsEJGBean.T_NOMBRETABLA, usuario);
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

			if (rc.queryNLS(consulta)) {
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

	public Hashtable getTituloPantallaEJG (String idInstitucion, String anio, String numero, String idTipoEJG, String longitudNumEjg ) 
	{
		try {
			String sql = 	"select " + ScsPersonaJGBean.C_NOMBRE + "," + 
			                            ScsPersonaJGBean.C_APELLIDO1 + ", " + 
										ScsPersonaJGBean.C_APELLIDO2 + ", " + 
										ScsEJGBean.C_ANIO + ", " +
										ScsPersonaJGBean.C_ASISTIDOAUTORIZAEEJG + ", " +
										ScsPersonaJGBean.C_ASISTIDOSOLICITAJG + ", " +
										ScsPersonaJGBean.C_AUTORIZAAVISOTELEMATICO + ", " +
										ScsPersonaJGBean.C_NOTIFICACIONTELEMATICA + ", " ;
										if(longitudNumEjg!=null)
											sql += "LPAD( a." + ScsEJGBean.C_NUMEJG + ", "+longitudNumEjg+",0)" + ScsEJGBean.C_NUMEJG + ", " ;
										else
											sql += " a." + ScsEJGBean.C_NUMEJG + ", " ;

										sql+=ScsEJGBean.C_IDTIPODICTAMENEJG+","+
										"TO_CHAR("+ScsEJGBean.C_FECHADICTAMEN+", 'DD/MM/YYYY') AS FECHADICTAMEN, "+
										ScsEJGBean.C_SUFIJO+
																				
							" from " + ScsPersonaJGBean.T_NOMBRETABLA + " p, " + ScsEJGBean.T_NOMBRETABLA + " a " + 
							" where a." + ScsEJGBean.C_IDINSTITUCION + " = " + idInstitucion +  
							  " and a." + ScsEJGBean.C_ANIO + " = " + anio +
							  " and a." + ScsEJGBean.C_NUMERO  + " = " + numero +
							  " and a." + ScsEJGBean.C_IDTIPOEJG  + " = " + idTipoEJG +
							  " and a." + ScsEJGBean.C_IDPERSONAJG + " = p." + ScsPersonaJGBean.C_IDPERSONA+"(+)" +
							  " and a." + ScsEJGBean.C_IDINSTITUCION+ " = p." + ScsPersonaJGBean.C_IDINSTITUCION+"(+)";
	
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
	
	
	
	public Hashtable getDatosEjg (String idInstitucion, String anio, String numero, String idTipoEJG, String longitudNumEjg) 
	{
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT SUFIJO, lpad(NUMEJG,");
			sql.append(longitudNumEjg);
			sql.append(",0) AS CODIGO, ");
			sql.append("PJG.NIF,PJG.NOMBRE,PJG.APELLIDO1,PJG.APELLIDO2 ");
			sql.append("FROM SCS_EJG EJG,SCS_PERSONAJG PJG ");
			sql.append("WHERE "); 
			sql.append("EJG.IDPERSONAJG = PJG.IDPERSONA(+) ");
			sql.append(" AND EJG.IDINSTITUCION = PJG.IDINSTITUCION(+) ");
			sql.append(" AND EJG.IDINSTITUCION = :1 ");
//			sql.append(idInstitucion);
			sql.append(" AND EJG.ANIO = :2 ");
//			sql.append(anio);
			sql.append(" AND EJG.NUMERO = :3");
//			sql.append(numero);
			sql.append(" AND EJG.IDTIPOEJG = :4 ");
//			sql.append(idTipoEJG);
			Hashtable htCodigos = new Hashtable();
			htCodigos.put(new Integer(1), idInstitucion);
			htCodigos.put(new Integer(2), anio);
			htCodigos.put(new Integer(3), numero);
			htCodigos.put(new Integer(4), idTipoEJG);
			Vector v = this.selectGenericoBind(sql.toString(),htCodigos );
			if (v!=null && v.size()>0) {
				return (Hashtable) v.get(0);
			}
		} 
		catch (ClsExceptions e) {
			e.printStackTrace();
		} catch (SIGAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Hashtable();
	}
	
	
	public Vector getDatosEjgResolucionFavorable (String idInstitucion, String anio, String numero, String idTurno) 
	{
		try {
			StringBuffer sql = new StringBuffer();
			StringBuffer sql2 = new StringBuffer();
			Vector v;
			
			Hashtable h1 = new Hashtable();
			h1.put(new Integer(1), idInstitucion);
			h1.put(new Integer(2), idTurno);
			h1.put(new Integer(3), anio);
			h1.put(new Integer(4), numero);
			
			Hashtable h2 = new Hashtable();
			h2.put(new Integer(1), idInstitucion);
			h2.put(new Integer(2), idTurno);
			h2.put(new Integer(3), anio);
			h2.put(new Integer(4), numero);
			
			h2.put(new Integer(5), idInstitucion);
			h2.put(new Integer(6), idTurno);
			h2.put(new Integer(7), anio);
			h2.put(new Integer(8), numero);
			
			sql2.append("	 Select Per.Nombre, Per.Apellido1, Nvl(Per.Apellido2, '') Apellido2 ");
			sql2.append("  From Scs_Defendidosdesigna Def, Scs_Personajg Per ");
			sql2.append(" Where Def.Idinstitucion = Per.Idinstitucion ");
			sql2.append("   And Def.Idpersona = Per.Idpersona ");
			sql2.append("   And Def.Idinstitucion =:1 ");
			sql2.append("   And Def.Idturno =:2 ");
			sql2.append("   And Def.Anio =:3 ");
			sql2.append("    And Def.Numero =:4 ");
			sql2.append(" Order By Apellido1, Apellido2, Nombre ");
			
			v = this.selectGenericoBind(sql2.toString(),h1);
			if(v == null || v.size()<1){
			
					sql.append(" Select Per.Nombre, Per.Apellido1, Nvl(Per.Apellido2, '') Apellido2 ");
					sql.append("  From Scs_Ejgdesigna        Ejgdes, ");
					sql.append("       Scs_Unidadfamiliarejg Uniejg, ");
					sql.append("       Scs_Personajg         Per, ");
					sql.append("       Scs_Ejg Ejg ");
					sql.append("  Where Ejgdes.Idinstitucion = Uniejg.Idinstitucion ");
					sql.append("   And Ejgdes.Idtipoejg = Uniejg.Idtipoejg ");
					sql.append("   And Ejgdes.Anioejg = Uniejg.Anio ");
					sql.append("   And Ejgdes.Numeroejg = Uniejg.Numero ");
					sql.append("   And Uniejg.Idinstitucion = Per.Idinstitucion ");
					sql.append("   And Uniejg.Idpersona = Per.Idpersona ");
					sql.append("   And Ejgdes.Idinstitucion =:1");
					sql.append("   And Ejgdes.Idturno =:2");
					sql.append("   And Ejgdes.Aniodesigna =:3");
					sql.append("   And Ejgdes.Numerodesigna =:4");
					sql.append("   and  Ejgdes.Idinstitucion = Ejg.Idinstitucion ");
					sql.append("   And Ejgdes.Idtipoejg = Ejg.Idtipoejg ");
					sql.append("   And Ejgdes.Anioejg = Ejg.Anio ");
					sql.append("   And Ejgdes.Numeroejg = Ejg.Numero ");
					sql.append(" and ( (EJG.Fecharesolucioncajg is not null and EJG.Idtiporatificacionejg in (1, 2, 8, 10, 9, 11)) ");
					sql.append("  OR (EJG.Idtiporatificacionejg is null and EJG.Fecharesolucioncajg is null)) ");
				    
					sql.append(" Union ");
				    
					sql.append(" Select Per.Nombre, Per.Apellido1, Nvl(Per.Apellido2, '') Apellido2 ");
					sql.append("  From Scs_Ejgdesigna Ejgdes, Scs_Ejg Ejg, Scs_Personajg Per ");
					sql.append(" Where Ejgdes.Idinstitucion = Ejg.Idinstitucion ");
					sql.append("   And Ejgdes.Idtipoejg = Ejg.Idtipoejg ");
					sql.append("   And Ejgdes.Anioejg = Ejg.Anio ");
					sql.append("   And Ejgdes.Numeroejg = Ejg.Numero ");
					sql.append("   And Ejg.Idinstitucion = Per.Idinstitucion ");
					sql.append("   And Ejg.Idpersonajg = Per.Idpersona ");
					sql.append("   And Ejgdes.Idinstitucion =:5");
					sql.append("   And Ejgdes.Idturno =:6");
					sql.append("   And Ejgdes.Aniodesigna=:7");
					sql.append("   And Ejgdes.Numerodesigna  =:8");
					sql.append(" and ( (EJG.Fecharesolucioncajg is not null and EJG.Idtiporatificacionejg in (1, 2, 8, 10, 9, 11)) ");
					sql.append("  OR (EJG.Idtiporatificacionejg is null and EJG.Fecharesolucioncajg is null)) ");
					sql.append(" Order By Apellido1, Apellido2, Nombre ");
		
					 v = this.selectGenericoBind(sql.toString(),h2);
					
			}
			if(v != null && v.size()>0){
				return v;
			}else{
				return null;
			}
			
			
		}catch (SIGAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClsExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new Vector();
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
			String sql = "SELECT * FROM " + ScsEJGBean.T_NOMBRETABLA + " " + where ;
			
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
	 * Efect�a un SELECT en la tabla SCS_EJG con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de b�squeda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + hash.get(ScsEJGBean.C_IDINSTITUCION) + " AND " + ScsEJGBean.C_IDTIPOEJG + " = " + hash.get(ScsEJGBean.C_IDTIPOEJG) + 
						   " AND " + ScsEJGBean.C_ANIO + " = " + hash.get(ScsEJGBean.C_ANIO) + " AND " + ScsEJGBean.C_NUMERO + " = " + hash.get(ScsEJGBean.C_NUMERO);		
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
	
	
	/** Funcion prepararInsert (Hashtable entrada). 
	 *  @return hashtable con los datos listos para insertarse 
	 * */
	public Hashtable prepararInsert(Hashtable entrada) throws ClsExceptions 
	{
		RowsContainer rc = null;		
		RowsContainer rc1 = null;
		String numeroMaximo = null;		
		String codigo="";
		String idTipoEJGColegioDefecto = null;
		
		
		// Primero calculamos el NUMERO del EJG que se obtiene a partir del NUMERO m�ximo de EJG almacenado en la base de datos
		try {

			
			
			rc = new RowsContainer();		
			rc1 = new RowsContainer();
			// Se prepara la sentencia sql para hacer el select

//			String anio = "";
//			anio = (String) entrada.get("SOJ_ANIO");
//			if(anio == null || anio.equals(""))	anio = (String) entrada.get("ASISTENCIA_ANIO");
//			if(anio == null || anio.equals(""))	anio = (String) entrada.get("DESIGNA_ANIO");
//			if(anio == null || anio.equals(""))	anio = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			
			// Ahora hay que introducir el ANIO, que se obtiene parseando la fecha de apertura (s�lo interesa el anho).
			String anio = entrada.get(ScsEJGBean.C_FECHAAPERTURA).toString();
			// El formado te la fecha es DD/MM/AAAA por eso s�lo nos interesa a partir de la posici�n 6 del string
			anio = anio.substring(6);
			entrada.put(ScsEJGBean.C_ANIO, anio);
			
			String sql ="SELECT (MAX("+ ScsEJGBean.C_NUMERO + ") + 1) AS NUMERO FROM " + nombreTabla + " WHERE IDINSTITUCION = " + entrada.get("IDINSTITUCION") + " AND ANIO = " + anio + " AND IDTIPOEJG = " + entrada.get("IDTIPOEJG");			 
			
//			PDM INC-4774
			String sqlNumeroEjg ="SELECT (MAX(to_number(NUMEJG)) + 1) AS NUMEROEJG FROM " + nombreTabla + 
			" WHERE IDINSTITUCION =" + entrada.get("IDINSTITUCION") +
			" AND ANIO =" + anio;//Obtenemos el max(codigo)+1 por institucion y anio 
			                               // y asi poder crear una UK del campo Codigo por institucion y anio.
			GenParametrosAdm paramAdm = new GenParametrosAdm (this.usrbean);
			String longitudEJG = paramAdm.getValor (this.usrbean.getLocation (), "SCS", "LONGITUD_CODEJG", "");
			idTipoEJGColegioDefecto = paramAdm.getValor (this.usrbean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_TIPO_EJG_COLEGIO, "");				
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("NUMERO").equals("")) {
					numeroMaximo = "1";
				}
				else numeroMaximo = prueba.get("NUMERO").toString();				
			}						
			
			if (rc1.query(sqlNumeroEjg)) {
				Row fila1 = (Row) rc1.get(0);
				Hashtable prueba1 = fila1.getRow();			
				if (prueba1.get("NUMEROEJG").equals("")) {
					codigo="1";
					codigo=UtilidadesString.formatea(codigo,Integer.parseInt(longitudEJG),true);
					entrada.put(ScsEJGBean.C_NUMEJG,codigo);
		}	
				else{
					codigo=(String)prueba1.get("NUMEROEJG");
					codigo=UtilidadesString.formatea(codigo,Integer.parseInt(longitudEJG),true);
					entrada.put(ScsEJGBean.C_NUMEJG,codigo);								
				}
			}
			

			
			
			
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCI�N. C�LCULO DE NUMERO");
		};
		
		if(entrada.get("PROCEDIMIENTO")!=null&& !(entrada.get("PROCEDIMIENTO")).equals("")) {
			String procedimiento=(String)entrada.get("PROCEDIMIENTO");
			entrada.put(ScsEJGBean.C_NUMEROPROCEDIMIENTO, procedimiento);
		}
		if(entrada.get("DILIGENCIA")!=null&& !(entrada.get("DILIGENCIA")).equals("")) {
			String diligencia=(String)entrada.get("DILIGENCIA");
			entrada.put(ScsEJGBean.C_NUMERODILIGENCIA, diligencia);
		}
		if(entrada.get("COMISARIA")!=null&& !(entrada.get("COMISARIA")).equals("")) {
			String comisaria=(String)entrada.get("COMISARIA");
			String comisaria2= comisaria.substring(0, comisaria.indexOf(","));
			entrada.put(ScsEJGBean.C_COMISARIA, comisaria2);
			String comisaria3= comisaria.substring(comisaria.indexOf(",")+1);
			entrada.put(ScsEJGBean.C_COMISARIAIDINSTITUCION, comisaria3);
		}
		if (entrada.get("JUZGADO")!=null && !(entrada.get("JUZGADO")).equals("")) {
			String juzgado = (String)entrada.get("JUZGADO");
			String idJuzgado = juzgado.substring(0, juzgado.indexOf(","));
			String idInstitucionJuzgado = juzgado.substring(juzgado.indexOf(",")+1);
			entrada.put(ScsEJGBean.C_JUZGADO, idJuzgado);			
			entrada.put(ScsEJGBean.C_JUZGADOIDINSTITUCION, idInstitucionJuzgado);				
		}
		if (entrada.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA)!=null && (entrada.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA)).equals("-1")) {
			entrada.put(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA, "");						
		}
		if (entrada.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO)!=null && (entrada.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO)).equals("-1")) {
			entrada.put(ScsEJGBean.C_GUARDIATURNO_IDTURNO, "");						
		}
		
		entrada.put(ScsEJGBean.C_NUMERO,numeroMaximo);
		
		// Por �ltimo ponemos la FECHAAPERTURA en el formato correcto de la base de datos.
		entrada.put(ScsEJGBean.C_FECHAAPERTURA,GstDate.getApplicationFormatDate("",entrada.get(ScsEJGBean.C_FECHAAPERTURA).toString()));		
		
		if(idTipoEJGColegioDefecto!=null && !idTipoEJGColegioDefecto.trim().equals(""))
			entrada.put(ScsEJGBean.C_IDTIPOEJGCOLEGIO,idTipoEJGColegioDefecto);
			
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
		
		String[] campos= {	ScsEJGBean.C_ANIO,						ScsEJGBean.C_NUMERO,
							ScsEJGBean.C_FECHAAPERTURA,				ScsEJGBean.C_ORIGENAPERTURA,
							ScsEJGBean.C_FECHALIMITEPRESENTACION,	ScsEJGBean.C_FECHAPRESENTACION,
							ScsEJGBean.C_PROCURADORNECESARIO,		ScsEJGBean.C_CALIDAD,
							ScsEJGBean.C_TIPOLETRADO,				ScsEJGBean.C_OBSERVACIONES,
							ScsEJGBean.C_DELITOS,					ScsEJGBean.C_DICTAMEN,
							ScsEJGBean.C_FECHADICTAMEN,				
							ScsEJGBean.C_RATIFICACIONDICTAMEN,		ScsEJGBean.C_FECHARATIFICACION,							
							ScsEJGBean.C_IDPERSONA,					ScsEJGBean.C_IDINSTITUCION,
							ScsEJGBean.C_IDTIPOEJG,					ScsEJGBean.C_GUARDIATURNO_IDGUARDIA,
							ScsEJGBean.C_GUARDIATURNO_IDTURNO,		ScsEJGBean.C_IDTIPOEJGCOLEGIO,
							ScsEJGBean.C_IDPERSONAJG,				ScsEJGBean.C_USUMODIFICACION,			
							ScsEJGBean.C_FECHAMODIFICACION,			ScsEJGBean.C_IDTIPODICTAMENEJG,																	
							ScsEJGBean.C_FACTURADO,		    		ScsEJGBean.C_PAGADO,
							ScsEJGBean.C_IDFACTURACION,				ScsEJGBean.C_IDFUNDAMENTOCALIF,
							ScsEJGBean.C_IDPROCURADOR,				ScsEJGBean.C_IDINSTITUCIONPROCURADOR,
							ScsEJGBean.C_IDTIPORATIFICACIONEJG,     ScsEJGBean.C_NUMERO_CAJG,
							ScsEJGBean.C_FECHAAUTO,					ScsEJGBean.C_FECHANOTIFICACION,
							ScsEJGBean.C_FECHARESOLUCIONCAJG,		ScsEJGBean.C_IDFUNDAMENTOJURIDICO,
							ScsEJGBean.C_IDTIPORESOLAUTO,			ScsEJGBean.C_IDTIPOSENTIDOAUTO,
							ScsEJGBean.C_TURNADOAUTO,				ScsEJGBean.C_TURNADORATIFICACION,
							ScsEJGBean.C_ANIO_CAJG,					ScsEJGBean.C_NUMEJG,
							ScsEJGBean.C_NUMERODILIGENCIA,			ScsEJGBean.C_NUMEROPROCEDIMIENTO,
							ScsEJGBean.C_COMISARIA,					ScsEJGBean.C_COMISARIAIDINSTITUCION,
							ScsEJGBean.C_JUZGADO,					ScsEJGBean.C_JUZGADOIDINSTITUCION,
							ScsEJGBean.C_IDPRETENSION,				
							ScsEJGBean.C_IDDICTAMEN,				ScsEJGBean.C_REFAUTO,
							ScsEJGBean.C_FECHADESIGPROC,			ScsEJGBean.C_IDENTIFICADORDS,
							ScsEJGBean.C_SITUACION,					ScsEJGBean.C_IDTIPOENCALIDAD,
							ScsEJGBean.C_CALIDADIDINSTITUCION,		ScsEJGBean.C_NUMERODESIGNAPROC,
							ScsEJGBean.C_DOCRESOLUCION,				ScsEJGBean.C_NIG,
							ScsEJGBean.C_IDPONENTE,ScsEJGBean.C_IDINSTITUCIONPONENTE,						ScsEJGBean.C_IDORIGENCAJG,
							ScsEJGBean.C_OBSERVACIONIMPUGNACION,	ScsEJGBean.C_FECHAPUBLICACION,
							ScsEJGBean.C_NUMERORESOLUCION,			ScsEJGBean.C_ANIORESOLUCION,
							ScsEJGBean.C_BISRESOLUCION,				ScsEJGBean.C_IDACTA,
							ScsEJGBean.C_IDINSTITUCIONACTA,			ScsEJGBean.C_ANIOACTA,
							ScsEJGBean.C_IDECOMCOLA,				ScsEJGBean.C_REQUIERENOTIFICARPROC,
							ScsEJGBean.C_USUCREACION,				ScsEJGBean.C_FECHACREACION,
							ScsEJGBean.C_FECHAPRESENTACIONPONENTE,	ScsEJGBean.C_ANIOPROCEDIMIENTO,	ScsEJGBean.C_IDEXPEDIENTEEXT};
							//,ScsEJGBean.C_DESIGNA_IDTURNO,	ScsEJGBean.C_DESIGNA_ANIO, ScsEJGBean.C_DESIGNA_NUMERO
		return campos;
	}
	
	protected String[] getCamposActualizablesBean() {
		
		String[] campos= {	ScsEJGBean.C_ANIO,						ScsEJGBean.C_NUMERO,
							ScsEJGBean.C_FECHAAPERTURA,				ScsEJGBean.C_ORIGENAPERTURA,
							ScsEJGBean.C_FECHALIMITEPRESENTACION,	ScsEJGBean.C_FECHAPRESENTACION,
							ScsEJGBean.C_PROCURADORNECESARIO,		ScsEJGBean.C_CALIDAD,
							ScsEJGBean.C_TIPOLETRADO,				ScsEJGBean.C_OBSERVACIONES,
							ScsEJGBean.C_DELITOS,					ScsEJGBean.C_DICTAMEN,
							ScsEJGBean.C_FECHADICTAMEN,				
							ScsEJGBean.C_RATIFICACIONDICTAMEN,		ScsEJGBean.C_FECHARATIFICACION,							
							ScsEJGBean.C_IDPERSONA,					ScsEJGBean.C_IDINSTITUCION,
							ScsEJGBean.C_IDTIPOEJG,					ScsEJGBean.C_GUARDIATURNO_IDGUARDIA,
							ScsEJGBean.C_GUARDIATURNO_IDTURNO,		ScsEJGBean.C_IDTIPOEJGCOLEGIO,
							ScsEJGBean.C_IDPERSONAJG,				ScsEJGBean.C_USUMODIFICACION,			
							ScsEJGBean.C_FECHAMODIFICACION,			ScsEJGBean.C_IDTIPODICTAMENEJG,																	
							ScsEJGBean.C_FACTURADO,		    		ScsEJGBean.C_PAGADO,
							ScsEJGBean.C_IDFACTURACION,				ScsEJGBean.C_IDFUNDAMENTOCALIF,
							ScsEJGBean.C_IDPROCURADOR,				ScsEJGBean.C_IDINSTITUCIONPROCURADOR,
							ScsEJGBean.C_IDTIPORATIFICACIONEJG,     ScsEJGBean.C_NUMERO_CAJG,
							ScsEJGBean.C_FECHAAUTO,					ScsEJGBean.C_FECHANOTIFICACION,
							ScsEJGBean.C_FECHARESOLUCIONCAJG,		ScsEJGBean.C_IDFUNDAMENTOJURIDICO,
							ScsEJGBean.C_IDTIPORESOLAUTO,			ScsEJGBean.C_IDTIPOSENTIDOAUTO,
							ScsEJGBean.C_TURNADOAUTO,				ScsEJGBean.C_TURNADORATIFICACION,
							ScsEJGBean.C_ANIO_CAJG,					ScsEJGBean.C_NUMEJG,
							ScsEJGBean.C_NUMERODILIGENCIA,			ScsEJGBean.C_NUMEROPROCEDIMIENTO,
							ScsEJGBean.C_COMISARIA,					ScsEJGBean.C_COMISARIAIDINSTITUCION,
							ScsEJGBean.C_JUZGADO,					ScsEJGBean.C_JUZGADOIDINSTITUCION,
							ScsEJGBean.C_IDPRETENSION,				
							ScsEJGBean.C_IDDICTAMEN,				ScsEJGBean.C_REFAUTO,
							ScsEJGBean.C_FECHADESIGPROC,			ScsEJGBean.C_IDENTIFICADORDS,
							ScsEJGBean.C_SITUACION,					ScsEJGBean.C_IDTIPOENCALIDAD,
							ScsEJGBean.C_CALIDADIDINSTITUCION,		ScsEJGBean.C_NUMERODESIGNAPROC,
							ScsEJGBean.C_DOCRESOLUCION,				ScsEJGBean.C_NIG,
							ScsEJGBean.C_IDPONENTE,	ScsEJGBean.C_IDINSTITUCIONPONENTE,										ScsEJGBean.C_IDORIGENCAJG,
							ScsEJGBean.C_OBSERVACIONIMPUGNACION,	ScsEJGBean.C_FECHAPUBLICACION,
							ScsEJGBean.C_NUMERORESOLUCION,			ScsEJGBean.C_ANIORESOLUCION,
							ScsEJGBean.C_BISRESOLUCION,				ScsEJGBean.C_IDACTA,
							ScsEJGBean.C_IDINSTITUCIONACTA,			ScsEJGBean.C_ANIOACTA,
							ScsEJGBean.C_REQUIERENOTIFICARPROC,		ScsEJGBean.C_IDECOMCOLA,
							ScsEJGBean.C_FECHAPRESENTACIONPONENTE,	ScsEJGBean.C_ANIOPROCEDIMIENTO,	ScsEJGBean.C_IDEXPEDIENTEEXT};
							//,ScsEJGBean.C_USUCREACION,				ScsEJGBean.C_FECHACREACION
							//,ScsEJGBean.C_DESIGNA_IDTURNO,	ScsEJGBean.C_DESIGNA_ANIO, ScsEJGBean.C_DESIGNA_NUMERO
		return campos;
	}	
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		
		String[] campos= {ScsEJGBean.C_IDINSTITUCION, ScsEJGBean.C_IDTIPOEJG, ScsEJGBean.C_ANIO, ScsEJGBean.C_NUMERO};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la informaci�n de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsEJGBean bean = null;
		
		try {
			bean = new ScsEJGBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDINSTITUCION));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPOEJG));
			bean.setAnio((UtilidadesHash.getInteger(hash,ScsEJGBean.C_ANIO)));	
			bean.setNumEJG((UtilidadesHash.getString(hash,ScsEJGBean.C_NUMEJG)));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsEJGBean.C_NUMERO));
 			bean.setGuardiaTurno_idTurno(UtilidadesHash.getInteger(hash,ScsEJGBean.C_GUARDIATURNO_IDTURNO));
			bean.setGuardiaTurno_idGuardia(UtilidadesHash.getInteger(hash,ScsEJGBean.C_GUARDIATURNO_IDGUARDIA));
			bean.setIdTipoEJGColegio(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPOEJGCOLEGIO));
			bean.setIdPersona(UtilidadesHash.getLong(hash,ScsEJGBean.C_IDPERSONA));
			bean.setIdPersonaJG(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDPERSONAJG));
			bean.setIdTipoDictamenEJG(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPODICTAMENEJG));			
			bean.setFechaApertura(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAAPERTURA));			
			bean.setOrigenApertura(UtilidadesHash.getString(hash,ScsEJGBean.C_ORIGENAPERTURA));
			bean.setFechaLimitePresentacion(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHALIMITEPRESENTACION));
			bean.setFechaPresentacion(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAPRESENTACION));
			bean.setProcuradorNecesario(UtilidadesHash.getInteger(hash,ScsEJGBean.C_PROCURADORNECESARIO));
			bean.setCalidad(UtilidadesHash.getString(hash,ScsEJGBean.C_CALIDAD));
			bean.setTipoLetrado(UtilidadesHash.getString(hash,ScsEJGBean.C_TIPOLETRADO));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsEJGBean.C_OBSERVACIONES));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsEJGBean.C_OBSERVACIONES));
			bean.setDelitos(UtilidadesHash.getString(hash,ScsEJGBean.C_DELITOS));
			bean.setDictamen(UtilidadesHash.getString(hash,ScsEJGBean.C_DICTAMEN));
			bean.setFechaDictamen(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHADICTAMEN));
			bean.setRatificacionDictamen(UtilidadesHash.getString(hash,ScsEJGBean.C_RATIFICACIONDICTAMEN));
			bean.setFechaRatificacion(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHARATIFICACION));
//			bean.setAsistencia_anio(UtilidadesHash.getInteger(hash,ScsEJGBean.C_ASISTENCIA_ANIO));
//			bean.setAsistencia_numero(UtilidadesHash.getInteger(hash,ScsEJGBean.C_ASISTENCIA_NUMERO));
//			bean.setSOJ_idTipoSOJ(UtilidadesHash.getInteger(hash,ScsEJGBean.C_SOJ_IDTIPOSOJ));
//			bean.setSOJ_numero(UtilidadesHash.getInteger(hash,ScsEJGBean.C_SOJ_NUMERO));
//			bean.setSOJ_anio(UtilidadesHash.getInteger(hash,ScsEJGBean.C_SOJ_ANIO));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsEJGBean.C_USUMODIFICACION));		
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAMODIFICACION));
			//bean.setDesignaIdTurno(UtilidadesHash.getInteger(hash,ScsEJGBean.C_DESIGNA_IDTURNO));
			//bean.setDesignaNumero(UtilidadesHash.getInteger(hash,ScsEJGBean.C_DESIGNA_NUMERO));
			//bean.setDesignaAnio(UtilidadesHash.getInteger(hash,ScsEJGBean.C_DESIGNA_ANIO));
			bean.setIdInstitucionProcurador(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDINSTITUCIONPROCURADOR));
			bean.setIdProcurador(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDPROCURADOR));
			bean.setIdTipoRatificacionEJG(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPORATIFICACIONEJG));
			bean.setNumeroCAJG(UtilidadesHash.getString(hash,ScsEJGBean.C_NUMERO_CAJG));
			bean.setAnioCAJG(UtilidadesHash.getString(hash,ScsEJGBean.C_ANIO_CAJG));
			bean.setIdFundamentoCalif(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDFUNDAMENTOCALIF));

			bean.setFechaAuto(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAAUTO));
			bean.setFechaNotificacion(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHANOTIFICACION));
			bean.setFechaResolucionCAJG(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHARESOLUCIONCAJG));
			bean.setIdFundamentoJuridico(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDFUNDAMENTOJURIDICO));
			bean.setIdTipoResolAuto(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPORESOLAUTO));
			bean.setIdTipoSentidoAuto(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPOSENTIDOAUTO));
			bean.setTurnadoAuto(UtilidadesHash.getString(hash,ScsEJGBean.C_TURNADOAUTO));
			bean.setTurnadoRatificacion(UtilidadesHash.getString(hash,ScsEJGBean.C_TURNADORATIFICACION));
			bean.setRequiereNotificarProc(UtilidadesHash.getString(hash,ScsEJGBean.C_REQUIERENOTIFICARPROC));
			bean.setFechaPresentacionPonente(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAPRESENTACIONPONENTE));
			
			bean.setIdPretension(UtilidadesHash.getString(hash,ScsEJGBean.C_IDPRETENSION));
			
			bean.setJuzgado(UtilidadesHash.getLong(hash,ScsEJGBean.C_JUZGADO));
			bean.setComisaria(UtilidadesHash.getLong(hash,ScsEJGBean.C_COMISARIA));
			bean.setNumeroDiligencia(UtilidadesHash.getString(hash,ScsEJGBean.C_NUMERODILIGENCIA));
			bean.setNumeroProcedimiento(UtilidadesHash.getString(hash,ScsEJGBean.C_NUMEROPROCEDIMIENTO));
			bean.setJuzgadoInstitucion(UtilidadesHash.getInteger(hash,ScsEJGBean.C_JUZGADOIDINSTITUCION));
			bean.setComisariaInstitucion(UtilidadesHash.getInteger(hash,ScsEJGBean.C_COMISARIAIDINSTITUCION));
			bean.setIdDictamen(UtilidadesHash.getString(hash,ScsEJGBean.C_IDDICTAMEN));
			bean.setRefAuto(UtilidadesHash.getString(hash,ScsEJGBean.C_REFAUTO));
			bean.setFechaProc(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHADESIGPROC));
			bean.setIdentificadorDS(UtilidadesHash.getString(hash,ScsEJGBean.C_IDENTIFICADORDS));
			bean.setIdSituacion(UtilidadesHash.getString(hash,ScsEJGBean.C_SITUACION));			
			bean.setIdTipoenCalidad(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPOENCALIDAD));
			bean.setCalidadidinstitucion(UtilidadesHash.getInteger(hash,ScsEJGBean.C_CALIDADIDINSTITUCION));
			bean.setNumeroDesignaProc(UtilidadesHash.getString(hash,ScsEJGBean.C_NUMERODESIGNAPROC));
			bean.setDocResolucion(UtilidadesHash.getString(hash,ScsEJGBean.C_DOCRESOLUCION));
			bean.setNIG(UtilidadesHash.getString(hash,ScsEJGBean.C_NIG));
			bean.setIdOrigenCAJG(UtilidadesHash.getString(hash,ScsEJGBean.C_IDORIGENCAJG));
			bean.setIdPonente(UtilidadesHash.getLong(hash,ScsEJGBean.C_IDPONENTE));
			bean.setIdInstitucionPonente(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDINSTITUCIONPONENTE));
			//David
			bean.setObservacionImpugnacion(UtilidadesHash.getString(hash,ScsEJGBean.C_OBSERVACIONIMPUGNACION));
			bean.setFechaPublicacion(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAPUBLICACION));
			bean.setNumeroResolucion(UtilidadesHash.getString(hash,ScsEJGBean.C_NUMERORESOLUCION));
			bean.setAnioResolucion(UtilidadesHash.getString(hash,ScsEJGBean.C_ANIORESOLUCION));
			bean.setBisResolucion(UtilidadesHash.getString(hash,ScsEJGBean.C_BISRESOLUCION));
			
			bean.setIdActa(UtilidadesHash.getString(hash,ScsEJGBean.C_IDACTA));
			bean.setIdInstitucionActa(UtilidadesHash.getString(hash,ScsEJGBean.C_IDINSTITUCIONACTA));
			bean.setAnioActa(UtilidadesHash.getString(hash,ScsEJGBean.C_ANIOACTA));
			bean.setIdEcomCola(UtilidadesHash.getLong(hash,ScsEJGBean.C_IDECOMCOLA));
			bean.setAnioProcedimiento(UtilidadesHash.getInteger(hash,ScsEJGBean.C_ANIOPROCEDIMIENTO));
			bean.setIdExpedienteExt(UtilidadesHash.getString(hash,ScsEJGBean.C_IDEXPEDIENTEEXT));
		}
		catch (Exception e){
			throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}		
		return bean;

	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la informaci�n del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsEJGBean b = (ScsEJGBean) bean;
			
			UtilidadesHash.set(htData,ScsEJGBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPOEJG, b.getIdTipoEJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(htData,ScsEJGBean.C_NUMEJG, b.getNumEJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(htData,ScsEJGBean.C_GUARDIATURNO_IDTURNO, b.getGuardiaTurno_idTurno());
			UtilidadesHash.set(htData,ScsEJGBean.C_GUARDIATURNO_IDGUARDIA, b.getGuardiaTurno_idGuardia());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPOEJGCOLEGIO, b.getIdTipoEJGColegio());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPERSONAJG, b.getIdPersonaJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPODICTAMENEJG, b.getIdTipoDictamenEJG());			
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAAPERTURA, b.getFechaApertura());
			UtilidadesHash.set(htData,ScsEJGBean.C_ORIGENAPERTURA, b.getOrigenApertura());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHALIMITEPRESENTACION, b.getFechaLimitePresentacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAPRESENTACION, b.getFechaPresentacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_PROCURADORNECESARIO, b.getProcuradorNecesario());
			UtilidadesHash.set(htData,ScsEJGBean.C_CALIDAD, b.getCalidad());
			UtilidadesHash.set(htData,ScsEJGBean.C_TIPOLETRADO, b.getTipoLetrado());
			UtilidadesHash.set(htData,ScsEJGBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(htData,ScsEJGBean.C_DELITOS, b.getDelitos());
			UtilidadesHash.set(htData,ScsEJGBean.C_DICTAMEN, b.getDictamen());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHADICTAMEN, b.getFechaDictamen());
			UtilidadesHash.set(htData,ScsEJGBean.C_RATIFICACIONDICTAMEN, b.getRatificacionDictamen());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHARATIFICACION, b.getFechaRatificacion());
//			UtilidadesHash.set(htData,ScsEJGBean.C_ASISTENCIA_ANIO, b.getAsistencia_anio());
//			UtilidadesHash.set(htData,ScsEJGBean.C_ASISTENCIA_NUMERO, b.getAsistencia_numero());
//			UtilidadesHash.set(htData,ScsEJGBean.C_SOJ_IDTIPOSOJ, b.getSOJ_idTipoSOJ());
//			UtilidadesHash.set(htData,ScsEJGBean.C_SOJ_NUMERO, b.getSOJ_numero());			
//			UtilidadesHash.set(htData,ScsEJGBean.C_SOJ_ANIO, b.getSOJ_anio());	
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,ScsEJGBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			//UtilidadesHash.set(htData,ScsEJGBean.C_DESIGNA_IDTURNO, b.getDesignaIdTurno());
			//UtilidadesHash.set(htData,ScsEJGBean.C_DESIGNA_NUMERO, b.getDesignaNumero());
			//UtilidadesHash.set(htData,ScsEJGBean.C_DESIGNA_ANIO, b.getDesignaAnio());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDINSTITUCIONPROCURADOR, b.getIdInstitucionProcurador());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPROCURADOR, b.getIdProcurador());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPORATIFICACIONEJG, b.getIdTipoRatificacionEJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_NUMERO_CAJG, b.getNumeroCAJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_ANIO_CAJG, b.getAnioCAJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDFUNDAMENTOCALIF,b.getIdFundamentoCalif());

			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAAUTO,b.getFechaAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHANOTIFICACION,b.getFechaNotificacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHARESOLUCIONCAJG,b.getFechaResolucionCAJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDFUNDAMENTOJURIDICO,b.getIdFundamentoJuridico());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPORESOLAUTO,b.getIdTipoResolAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPOSENTIDOAUTO,b.getIdTipoSentidoAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_TURNADOAUTO,b.getTurnadoAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_TURNADORATIFICACION,b.getTurnadoRatificacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_REQUIERENOTIFICARPROC,b.getRequiereNotificarProc());
			
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPRETENSION,b.getIdPretension());
			
			UtilidadesHash.set(htData, ScsEJGBean.C_JUZGADO, b.getJuzgado());
			UtilidadesHash.set(htData, ScsEJGBean.C_COMISARIA, b.getComisaria());
			UtilidadesHash.set(htData, ScsEJGBean.C_NUMERODILIGENCIA, b.getNumeroDiligencia());
			UtilidadesHash.set(htData, ScsEJGBean.C_NUMEROPROCEDIMIENTO, b.getNumeroProcedimiento());
			UtilidadesHash.set(htData, ScsEJGBean.C_JUZGADOIDINSTITUCION, b.getJuzgadoInstitucion());
			UtilidadesHash.set(htData, ScsEJGBean.C_COMISARIAIDINSTITUCION, b.getComisariaInstitucion());
			UtilidadesHash.set(htData, ScsEJGBean.C_IDDICTAMEN, b.getIdDictamen());
			UtilidadesHash.set(htData, ScsEJGBean.C_REFAUTO, b.getRefAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHADESIGPROC, b.getFechaProc());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDENTIFICADORDS, b.getIdentificadorDS());
			UtilidadesHash.set(htData,ScsEJGBean.C_SITUACION, b.getIdSituacion());			
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPOENCALIDAD, b.getIdTipoenCalidad());
			UtilidadesHash.set(htData,ScsEJGBean.C_CALIDADIDINSTITUCION, b.getCalidadidinstitucion());
			UtilidadesHash.set(htData,ScsEJGBean.C_NUMERODESIGNAPROC, b.getNumeroDesignaProc());
			UtilidadesHash.set(htData,ScsEJGBean.C_DOCRESOLUCION, b.getDocResolucion());
			UtilidadesHash.set(htData,ScsEJGBean.C_NIG, b.getNIG());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDORIGENCAJG, b.getIdOrigenCAJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPONENTE, b.getIdPonente());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDINSTITUCIONPONENTE, b.getIdInstitucionPonente());
			//David
			UtilidadesHash.set(htData,ScsEJGBean.C_OBSERVACIONIMPUGNACION, b.getObservacionImpugnacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAPUBLICACION, b.getFechaPublicacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_NUMERORESOLUCION, b.getNumeroResolucion());
			UtilidadesHash.set(htData,ScsEJGBean.C_ANIORESOLUCION, b.getAnioResolucion());
			UtilidadesHash.set(htData,ScsEJGBean.C_BISRESOLUCION, b.getBisResolucion());
			
			UtilidadesHash.set(htData,ScsEJGBean.C_IDACTA, b.getIdActa());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDINSTITUCIONACTA, b.getIdInstitucionActa());			
			UtilidadesHash.set(htData,ScsEJGBean.C_ANIOACTA, b.getAnioActa());			
			UtilidadesHash.set(htData,ScsEJGBean.C_IDECOMCOLA, b.getIdEcomCola());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAPRESENTACIONPONENTE, b.getFechaPresentacionPonente());
			UtilidadesHash.set(htData, ScsEJGBean.C_ANIOPROCEDIMIENTO, b.getAnioProcedimiento());
			UtilidadesHash.set(htData, ScsEJGBean.C_IDEXPEDIENTEEXT, b.getIdExpedienteExt());
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}

	/**
	 * Obtiene el tipo de ordenaci�n con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenaci�n.
	 */
	protected String[] getOrdenCampos() {

		String[] campos= {ScsEJGBean.C_IDINSTITUCION, ScsEJGBean.C_IDTIPOEJG, ScsEJGBean.C_ANIO, ScsEJGBean.C_NUMERO};
		return campos;
	}	
	
	/** 
	 * Recoge informacion sobre los miembros de la unidad familiar 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  tipoEJG - identificador del tipo EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getMiembrosUnidadFamiliar (String institucion, String tipoEJG, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NIF+","+
							"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO2 + " || ', ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NOMBRE + ") AS MIEMBRO, " +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_ENCALIDADDE + " AS PARENTESCO," +
							"(SELECT ROUND((SYSDATE - " + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_FECHANACIMIENTO + ")/365)" +
								" FROM " + ScsPersonaJGBean.T_NOMBRETABLA +
								" WHERE " + 
								ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_IDPERSONA +
								" AND " +
								ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_IDINSTITUCION + "=" + institucion +
							") AS EDAD," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_OBSERVACIONES +
							" FROM " + ScsPersonaJGBean.T_NOMBRETABLA + "," + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + 
							" WHERE " +
							ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_NUMERO + "=" + numero ;
							//Hay un problema en los datos, todos estan a true
							//" AND " +
							//ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_SOLICITANTE + "=" + ClsConstants.DB_FALSE;
	            
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los restantentes miembros de la unidad familiar.");
	       }
	       return datos;                        
	    }
	
	
	/** 
	 * Recoge informacion sobre los datos economicos de la unidad familiar 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  tipoEJG - identificador del tipo EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosEconomicosUnidadFamiliar (String institucion, String tipoEJG, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
							"(" + ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_APELLIDO2 + " || ', ' || " +
							ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_NOMBRE + ") AS PERCEPTOR, " +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES + "," +
			    			ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "." + ScsUnidadFamiliarEJGBean.C_OTROSBIENES +
							" FROM " + ScsPersonaJGBean.T_NOMBRETABLA + "," + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + 
							" WHERE " +
							ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_NUMERO + "=" + numero;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los datos economicos de la unidad familiar.");
	       }
	       return datos;                        
	    }
	
	/**
	 * Obtiene los contrarios del EJG
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anioEjg
	 * @param numeroEjg
	 * @return
	 * @throws ClsExceptions
	 */	
	private Vector<Hashtable<String,Object>> getContrariosEjg(String idInstitucion, String  tipoEjg, String anioEjg, String numeroEjg, String idContrario) throws ClsExceptions {	
		Vector<Hashtable<String,Object>> datos = new Vector<Hashtable<String,Object>>();   
  		RowsContainer rc = new RowsContainer();
  		StringBuffer sqlBuffer = new StringBuffer();
  		
  		sqlBuffer.append("SELECT PER.IDPERSONA AS IDPERSONA_PJG, ");
  		sqlBuffer.append(" DECODE(PER.DIRECCION, null, null, 1) AS IDDIRECCION_PJG, ");
  		sqlBuffer.append(" NVL(PER.NOMBRE, '') AS NOMBRE_PJG, ");
  		sqlBuffer.append(" NVL(PER.APELLIDO1, '') AS APELLIDO1_PJG, ");
  		sqlBuffer.append(" NVL(PER.APELLIDO2, '') AS APELLIDO2_PJG, ");
  		sqlBuffer.append(" NVL(PER.CODIGOPOSTAL, '') AS CP_PJG, ");
  		sqlBuffer.append(" NVL(POBL.NOMBRE, '') AS POBLACION_PJG, ");
  		sqlBuffer.append(" NVL(PROV.NOMBRE, '') AS PROVINCIA_PJG, ");
  		
  		sqlBuffer.append(" NVL2(VIA.DESCRIPCION, F_SIGA_GETRECURSO(VIA.DESCRIPCION, ");
  		sqlBuffer.append(this.usrbean.getLanguage());
  		sqlBuffer.append("), '') || ");
  		sqlBuffer.append(" NVL2(PER.DIRECCION, ' ' || PER.DIRECCION, '') || ");
  		sqlBuffer.append(" NVL2(PER.NUMERODIR, ' ' || PER.NUMERODIR, '') || ");
  		sqlBuffer.append(" NVL2(PER.ESCALERADIR, ' ' || PER.ESCALERADIR, '') || ");
  		sqlBuffer.append(" NVL2(PER.PISODIR, ' ' || PER.PISODIR, '') || ");
  		sqlBuffer.append(" NVL2(PER.PUERTADIR, ' ' || PER.PUERTADIR, '') AS DOMICILIO_PJG, ");
  		
  		sqlBuffer.append(" DECODE(PER.SEXO, null, null, 'M', 'gratuita.personaEJG.sexo.mujer', 'gratuita.personaEJG.sexo.hombre') AS SEXO_PJG, ");
  		sqlBuffer.append(" NVL2(PER.SEXO, DECODE(PER.SEXO, 'H', 'o', 'a'), '') AS O_A_PJG, ");
  		sqlBuffer.append(" DECODE(PER.SEXO, 'H', 'el', 'la') AS EL_LA_PJG, ");
  		sqlBuffer.append(" NVL(PER.NIF, '') AS NIF_PJG, ");
        
  		sqlBuffer.append(" (SELECT ST.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA ST WHERE ST.IDINSTITUCION = PER.IDINSTITUCION AND ST.IDPERSONA = PER.IDPERSONA AND ST.IDTELEFONO = 1) TELEFONO1_PJG, ");
  		sqlBuffer.append(" (SELECT ST.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA ST WHERE ST.IDINSTITUCION = PER.IDINSTITUCION AND ST.IDPERSONA = PER.IDPERSONA AND ST.IDTELEFONO = 2) TELEFONO2_PJG, ");        
  		sqlBuffer.append(" (SELECT MAX(ST.NUMEROTELEFONO) FROM SCS_TELEFONOSPERSONA ST WHERE ST.IDINSTITUCION = PER.IDINSTITUCION AND ST.IDPERSONA = PER.IDPERSONA AND ST.PREFERENTESMS = 1) MOVIL_PJG, ");
  		
  		sqlBuffer.append(" PER.Fax AS FAX_PJG, ");
  		sqlBuffer.append(" PER.CORREOELECTRONICO AS CORREOELECTRONICO_PJG, ");
  		sqlBuffer.append(" PER.IDREPRESENTANTEJG, ");
  		sqlBuffer.append(" PER.IDINSTITUCION, ");

  		sqlBuffer.append(" CON.Nombreabogadocontrarioejg AS ABOGADO_CONTRARIO, ");
        sqlBuffer.append(" CON.Nombrerepresentanteejg AS REPRESENTANTE_CONTRARIO, ");
        sqlBuffer.append(" NVL(PRO.NOMBRE, '') || NVL2(PRO.APELLIDOS1, ' ' || PRO.APELLIDOS1, '') || NVL2(PRO.APELLIDOS2, ' ' || PRO.APELLIDOS2, '') AS PROCURADOR_CONTRARIO ");
  		
  		sqlBuffer.append(" FROM SCS_CONTRARIOSEJG CON, ");
  		sqlBuffer.append(" SCS_PERSONAJG PER, ");
  		sqlBuffer.append(" CEN_TIPOVIA VIA, ");
  		sqlBuffer.append(" CEN_POBLACIONES POBL, ");
  		sqlBuffer.append(" CEN_PROVINCIAS PROV, ");
  		sqlBuffer.append(" SCS_PROCURADOR PRO ");
  		
  		sqlBuffer.append(" WHERE CON.IDINSTITUCION = PER.IDINSTITUCION ");
  		sqlBuffer.append(" AND CON.IDPERSONA = PER.IDPERSONA ");
  		sqlBuffer.append(" AND VIA.IDINSTITUCION(+) = PER.IDINSTITUCION ");
  		sqlBuffer.append(" AND VIA.IDTIPOVIA(+) = PER.IDTIPOVIA ");
  		sqlBuffer.append(" AND POBL.IDPOBLACION(+) = PER.IDPOBLACION ");
  		sqlBuffer.append(" AND PROV.IDPROVINCIA(+) = PER.IDPROVINCIA ");
  		sqlBuffer.append(" AND CON.IDPROCURADOR = PRO.IDPROCURADOR(+) ");
  		sqlBuffer.append(" AND CON.IDINSTITUCION_PROCU = PRO.IDINSTITUCION(+) ");
  		
  		sqlBuffer.append(" AND CON.IDINSTITUCION = ");
  		sqlBuffer.append(idInstitucion);
  		
  		sqlBuffer.append(" AND CON.IDTIPOEJG = ");
  		sqlBuffer.append(tipoEjg);
  		
  		sqlBuffer.append(" AND CON.ANIO = ");
  		sqlBuffer.append(anioEjg);
  		
  		sqlBuffer.append(" AND CON.NUMERO = ");
  		sqlBuffer.append(numeroEjg);
  				
  		if (idContrario!=null) {
  			sqlBuffer.append(" AND CON.IDPERSONA = ");
  			sqlBuffer.append(idContrario);
  		}
  				
        try {    
        	if (rc.find(sqlBuffer.toString())) {
        		for (int i = 0; i < rc.size(); i++){
        			Row fila = (Row) rc.get(i);
        			Hashtable<String,Object> resultado = fila.getRow();	                  
		            datos.add(resultado);
        		}
        	} 
        	
        } catch (Exception e) {
        	throw new ClsExceptions (e, "Error al obtener la informacion sobre el tipo ejg colegio de una designa.");
        }
       
        return datos;      
	}	
	
	/** 
	 * Recoge informacion sobre las EJGs para las cartas de interesados de EJG 
	 * @param  institucion - identificador de la institucion
	 * @param  tipoEJG - tipo de EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
		 * @param longitudNumEjg 
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Row getEJGdeSOJ(String institucion, String anio, String tipo, String numero, String longitudNumEjg) throws ClsExceptions,SIGAException {
        RowsContainer rc = new RowsContainer();
        Row fila = null;
	       try {

            String sql ="select "+
            "A."+ScsEJGBean.C_ANIO+","+
            "A."+ScsEJGBean.C_NUMERO+","+
            "A."+ScsEJGBean.C_IDTIPOEJG+","+
            UtilidadesMultidioma.getCampoMultidioma("B."+ScsTipoEJGBean.C_DESCRIPCION,this.usrbean.getLanguage())+","+
            "A."+ScsEJGBean.C_FECHAAPERTURA+","+
            " lpad(A.numejg,"+longitudNumEjg+",0) "+ScsEJGBean.C_NUMEJG+
            
			
            " from "+ScsEJGBean.T_NOMBRETABLA +" A,"+ScsTipoEJGBean.T_NOMBRETABLA+" B,"+ScsSOJBean.T_NOMBRETABLA+" C"+
            " where c."+ScsSOJBean.C_IDINSTITUCION+"="+institucion+
            "   and C."+ScsSOJBean.C_ANIO+"="+anio+
            "   and C."+ScsSOJBean.C_IDTIPOSOJ+"="+tipo+
            "   and C."+ScsSOJBean.C_NUMERO+"="+numero+
			
			"   and A."+ScsEJGBean.C_ANIO+"=C."+ScsSOJBean.C_EJGANIO +
			"   and A."+ScsEJGBean.C_NUMERO+"=C."+ScsSOJBean.C_EJGNUMERO +
			"   and A."+ScsEJGBean.C_IDTIPOEJG+"=C."+ScsSOJBean.C_EJGIDTIPOEJG +
            "   and A."+ScsEJGBean.C_IDINSTITUCION+"=C."+ScsSOJBean.C_IDINSTITUCION +
			
            "   and C." + ScsSOJBean.C_EJGIDTIPOEJG + "=B." + ScsTipoEJGBean.C_IDTIPOEJG;
	     
//	       		String sql ="select "+
//	            "A."+ScsEJGBean.C_ANIO+","+
//	            "A."+ScsEJGBean.C_NUMERO+","+
//	            "A."+ScsEJGBean.C_IDTIPOEJG+","+
//	            UtilidadesMultidioma.getCampoMultidioma("B."+ScsTipoEJGBean.C_DESCRIPCION,this.usrbean.getLanguage())+","+
//	            "A."+ScsEJGBean.C_FECHAAPERTURA+","+
//				"A."+ScsEJGBean.C_NUMEJG+
//	            " from "+ScsEJGBean.T_NOMBRETABLA +" A,"+ScsTipoEJGBean.T_NOMBRETABLA+" B"+
//	            " where A."+ScsEJGBean.C_IDINSTITUCION+"="+institucion+
//	            "   and A."+ScsEJGBean.C_SOJ_ANIO+"="+anio+
//	            "   and A."+ScsEJGBean.C_SOJ_IDTIPOSOJ+"="+tipo+
//	            "   and A."+ScsEJGBean.C_SOJ_NUMERO+"="+numero+
//	            "   and A."+ScsEJGBean.C_IDTIPOEJG+"=B."+ScsTipoEJGBean.C_IDTIPOEJG;
	            
	            if(rc.find(sql)){
	                  fila = (Row) rc.get(0);
	            }
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las actuaciosnes de una designa.");
	       }
	       return fila;                        
	    }

	public Vector getRelacionadoCon (String institucion, String anio, String numero, String idTipo) throws ClsExceptions,SIGAException 
	{
		try {
	            	            
	       	String sql = " SELECT * FROM ( " +
			
							" SELECT TRIM('ASISTENCIA') SJCS, " + 
									 ScsAsistenciasBean.C_IDINSTITUCION + " IDINSTITUCION, " + 
									 ScsAsistenciasBean.C_ANIO + " ANIO, " + 
									 ScsAsistenciasBean.C_NUMERO + " NUMERO, " +
									 ScsAsistenciasBean.C_IDPERSONACOLEGIADO + " IDLETRADO, " + 
									 "TO_CHAR("+ScsAsistenciasBean.C_IDTURNO + ") IDTURNO, " +
									 "TO_CHAR("+ScsAsistenciasBean.C_IDTIPOASISTENCIA + ") IDTIPO, " +
									 "TO_CHAR("+ScsAsistenciasBean.C_NUMERO + ") CODIGO, " +
									 
									 "(SELECT " + ScsTurnoBean.C_ABREVIATURA + " FROM " + ScsTurnoBean.T_NOMBRETABLA + 
									 " WHERE " + ScsTurnoBean.C_IDTURNO + " = " + ScsAsistenciasBean.T_NOMBRETABLA + " ." + ScsAsistenciasBean.C_IDTURNO + 
									 " AND " + ScsTurnoBean.C_IDINSTITUCION + " = " + ScsAsistenciasBean.T_NOMBRETABLA + " ." + ScsAsistenciasBean.C_IDINSTITUCION + ") DES_TURNO, " +
									 "(SELECT f_siga_getRecurso(s.descripcion, " + this.usrbean.getLanguage() + ") FROM "+ ScsTipoAsistenciaColegioBean.T_NOMBRETABLA + " s " +  
									 " WHERE  " + ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_IDINSTITUCION + " = s." +ScsTipoAsistenciaColegioBean.C_IDINSTITUCION +
									 " AND " + ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO + " = s."+ ScsTipoAsistenciaColegioBean.C_IDTIPOASISTENCIACOLEGIO+ " ) DES_TIPO "+									 
							 " FROM " + ScsAsistenciasBean.T_NOMBRETABLA +
							" WHERE " + ScsAsistenciasBean.C_EJGANIO + " = " + anio + 
							  " AND " + ScsAsistenciasBean.C_EJGNUMERO + " = " + numero +
							  " AND " + ScsAsistenciasBean.C_EJGIDTIPOEJG + " = " + idTipo +
							  " AND " + ScsAsistenciasBean.C_IDINSTITUCION + " = " + institucion +
							
							" UNION " +
			
							" SELECT TRIM('SOJ') SJCS, " + 
							 		 ScsSOJBean.C_IDINSTITUCION + " IDINSTITUCION, " + 
							         ScsSOJBean.C_ANIO + " ANIO, " + 
									 ScsSOJBean.C_NUMERO + " NUMERO, " +
									 ScsSOJBean.C_IDPERSONA + " IDLETRADO, " + 
									 "TO_CHAR(" + ScsSOJBean.C_IDTURNO + ") IDTURNO, " +
									 "TO_CHAR(" + ScsSOJBean.C_IDTIPOSOJ + ") IDTIPO, " +
									 ScsSOJBean.C_NUMSOJ + " CODIGO, " +

									 "(SELECT " + ScsTurnoBean.C_ABREVIATURA + " FROM " + ScsTurnoBean.T_NOMBRETABLA + 
									 " WHERE " + ScsTurnoBean.C_IDTURNO + " = " + ScsSOJBean.T_NOMBRETABLA + " ." + ScsSOJBean.C_IDTURNO + 
									 " AND " + ScsTurnoBean.C_IDINSTITUCION + " = " + ScsSOJBean.T_NOMBRETABLA + " ." + ScsSOJBean.C_IDINSTITUCION + ") DES_TURNO, " +

									 "( Select F_SIGA_GETRECURSO(DESCRIPCION, " + this.usrbean.getLanguage() + ") as DESCRIPCION From SCS_TIPOSOJ " +
									 " WHERE SCS_TIPOSOJ.idtiposoj = " + ScsSOJBean.T_NOMBRETABLA + "." + ScsSOJBean.C_IDTIPOSOJ + " ) DES_TIPO " +
							  " FROM " + ScsSOJBean.T_NOMBRETABLA +
							 " WHERE " + ScsSOJBean.C_EJGANIO + " = " + anio + 
							   " AND " + ScsSOJBean.C_EJGNUMERO + " = " + numero +
							   " AND " + ScsSOJBean.C_EJGIDTIPOEJG + " = " + idTipo +
							   " AND " + ScsSOJBean.C_IDINSTITUCION + " = " + institucion +
							
							" UNION " +
							
							" SELECT TRIM('DESIGNA') SJCS, " + 
									 "ejg."+ScsEJGBean.C_IDINSTITUCION + " IDINSTITUCION, " + 
									 "ejgd."+ScsEJGDESIGNABean.C_ANIODESIGNA  + " ANIO, " + 
									 "ejgd."+ScsEJGDESIGNABean.C_NUMERODESIGNA + " NUMERO, " +
									 "ejg."+ScsEJGBean.C_IDPERSONA + " IDLETRADO, " +
									 "TO_CHAR(ejgd."+ScsEJGDESIGNABean.C_IDTURNO + ") IDTURNO, " +
									 
									" (SELECT TO_CHAR(" + ScsDesignaBean.C_IDTIPODESIGNACOLEGIO + ") " + 
									   " FROM " + ScsDesignaBean.T_NOMBRETABLA + 
									  " WHERE " + ScsDesignaBean.C_ANIO + " = ejgd." + ScsEJGDESIGNABean.C_ANIODESIGNA  +
									    " AND " + ScsDesignaBean.C_NUMERO + " = ejgd." + ScsEJGDESIGNABean.C_NUMERODESIGNA +
										" AND " + ScsDesignaBean.C_IDTURNO + " = ejgd." + ScsEJGDESIGNABean.C_IDTURNO + "" +
						                " AND " + ScsDesignaBean.C_IDINSTITUCION + " = ejgd." + ScsEJGDESIGNABean.C_IDINSTITUCION + ") IDTIPO, " +
									 
									" (SELECT " + ScsDesignaBean.C_CODIGO + 
									   " FROM " + ScsDesignaBean.T_NOMBRETABLA + 
									  " WHERE " + ScsDesignaBean.C_ANIO + " = ejgd." + ScsEJGDESIGNABean.C_ANIODESIGNA  +
									    " AND " + ScsDesignaBean.C_NUMERO + " = ejgd." + ScsEJGDESIGNABean.C_NUMERODESIGNA +
										" AND " + ScsDesignaBean.C_IDTURNO + " = ejgd." + ScsEJGDESIGNABean.C_IDTURNO + "" +
										" AND " + ScsDesignaBean.C_IDINSTITUCION + " = ejgd." + ScsEJGDESIGNABean.C_IDINSTITUCION + ") CODIGO, " +
										
									 "(SELECT " + ScsTurnoBean.C_ABREVIATURA + " FROM " + ScsTurnoBean.T_NOMBRETABLA + 
									 " WHERE " + ScsTurnoBean.C_IDTURNO + " = ejgd." + ScsEJGDESIGNABean.C_IDTURNO + 
									 " AND " + ScsTurnoBean.C_IDINSTITUCION + " = ejg." + ScsEJGBean.C_IDINSTITUCION + ") DES_TURNO, " +

									" (SELECT f_siga_getrecurso(" + ScsTipoDesignaColegioBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " + 
									   " FROM " + ScsDesignaBean.T_NOMBRETABLA + " a, " + ScsTipoDesignaColegioBean.T_NOMBRETABLA + " b " + 
									  " WHERE a." + ScsDesignaBean.C_ANIO + " = ejgd." + ScsEJGDESIGNABean.C_ANIODESIGNA  +
									    " AND a." + ScsDesignaBean.C_NUMERO + " = ejgd." + ScsEJGDESIGNABean.C_NUMERODESIGNA +
										" AND a." + ScsDesignaBean.C_IDTURNO + " = ejgd." + ScsEJGDESIGNABean.C_IDTURNO + 
										" AND a." + ScsDesignaBean.C_IDINSTITUCION + " = " + institucion + 
										" AND a." + ScsDesignaBean.C_IDINSTITUCION + " = b." + ScsTipoDesignaColegioBean.C_IDINSTITUCION + 
										" AND a." + ScsDesignaBean.C_IDTIPODESIGNACOLEGIO + " = b." + ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO + 	") DES_TIPO " +
							  " FROM " + ScsEJGBean.T_NOMBRETABLA +" ejg,"+ScsEJGDESIGNABean.T_NOMBRETABLA+" ejgd"+
							 " WHERE ejg." + ScsEJGBean.C_ANIO + " = " + anio +
							   " AND ejg." + ScsEJGBean.C_NUMERO + " = " + numero +
							   " AND ejg." + ScsEJGBean.C_IDTIPOEJG + " = " + idTipo +
							   " AND ejg." + ScsEJGBean.C_IDINSTITUCION + " = " + institucion + 
							 
							   " AND ejgd." + ScsEJGDESIGNABean.C_ANIOEJG    + " = ejg." +ScsEJGBean.C_ANIO +
							   " AND ejgd." + ScsEJGDESIGNABean.C_NUMEROEJG  + " = ejg." +ScsEJGBean.C_NUMERO +
							   " AND ejgd." + ScsEJGDESIGNABean.C_IDTIPOEJG + " = ejg." +ScsEJGBean.C_IDTIPOEJG +
							   " AND ejgd." + ScsEJGDESIGNABean.C_IDINSTITUCION + " = ejg." +ScsEJGBean.C_IDINSTITUCION +
							
							" ) " +
						 " ORDER BY SJCS, IDINSTITUCION, ANIO, CODIGO ";

	       	return this.selectGenerico(sql);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de un ejg.");
		}
	}

	
	
	/*Devuelve la clave de la asistencia en caso de que este creado o relacionado con una asistencia*/
	public Hashtable procedeDeAsistencia(String tipo,String numero, String anio){
		
		//ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		
		Vector v=new Vector();
		Hashtable h=new Hashtable();
		
		String sql="select a.anio as ASIANIO, a.numero as ASINUMERO" +
				"  from scs_asistencia a" +
				" where a.ejganio = "+anio+
				"   and a.ejgnumero = "+ numero +
				"   and a.ejgidtipoejg ="+ tipo +
				"   and a.idinstitucion = "+ this.usrbean.getLocation();

		try {
			v=super.selectGenerico(sql);
			if(v.size()>0)
				h=(Hashtable)v.get(0);
		} catch (ClsExceptions e) {
			e.printStackTrace();
		}
		catch (SIGAException e1) {
			e1.printStackTrace();
		}
		
		return h;
	}
	
	public static String getUnidadEJG (String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions,SIGAException {
		   String datos="";
	       try {
	            RowsContainer rc = new RowsContainer(); 

			    Hashtable codigos = new Hashtable();
			    int contador=0;
			    
			    contador++;
				codigos.put(new Integer(contador),institucion);
	            String sql ="select f_siga_getunidadejg(:"+contador+", ";
	            contador++;
				codigos.put(new Integer(contador),anio);
	            sql += "                    :"+contador+", ";
	            contador++;
				codigos.put(new Integer(contador),numero);
	            sql += "                    :"+contador+", ";
	            contador++;
				codigos.put(new Integer(contador),tipoEJG);
	            sql += "                    :"+contador+") AS NOMBRE from dual";
														
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
	
	/**
	 * @param institucion
	 * @param tipoEJG
	 * @param anio
	 * @param numero
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getFechaAperturaEJG(String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions,SIGAException {
		String fechaApertura = "";
		try {
            RowsContainer rc = new RowsContainer(); 

		    Hashtable codigos = new Hashtable();
		    int contador=0;
		    
            String sql ="SELECT ejg." + ScsEJGBean.C_FECHAAPERTURA +
                      " FROM SCS_EJG ejg ";
      	            contador++;
    				codigos.put(new Integer(contador),institucion);
    	            sql += " WHERE ejg.IDINSTITUCION =:"+contador;
    	            contador++;
    				codigos.put(new Integer(contador),tipoEJG);
    	            sql += "  and ejg.IDTIPOEJG =:" +contador;
    	            contador++;
    				codigos.put(new Integer(contador),anio);
    	            sql += "  and ejg.ANIO =:" + contador;
    	            contador++;
    				codigos.put(new Integer(contador),numero);
    	            sql += "  and ejg.NUMERO =:" + contador ;
													
            if (rc.findBind(sql,codigos)) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  Hashtable resultado=fila.getRow();	                  
                  fechaApertura = (String)resultado.get("FECHAAPERTURA");
               }
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		return fechaApertura;
	}
	
	public Hashtable getBindEjgRemesas(Hashtable miHash, DefinicionRemesas_CAJG_Form miForm, String idInstitucion, String longitudNumEjg) throws ClsExceptions, SIGAException {

		// A raiz de la INC_07042_SIGA se revisan los criterios de busqueda eliminado el codigo comentado.
		// Estamos a 08/04/2010 version 1.4.2.3 del CVS

		Hashtable hashReturn = new Hashtable();

		Hashtable codigos = new Hashtable();
		int contador = 0;

		String consulta = "";

		String cuentaErrores = "SELECT COUNT(1) FROM " + CajgRespuestaEJGRemesaBean.T_NOMBRETABLA + " ER" + " WHERE ER." + CajgRespuestaEJGRemesaBean.C_IDEJGREMESA + " = ejgremesa." + CajgEJGRemesaBean.C_IDEJGREMESA;

		String filtrado = "";
		String idIncidenciasEnvio = miForm.getIdIncidenciasEnvio();
		//A�adiremos el campo de salida de permiso de envio de informe economicao si es Alcala y si no se filtra por: errores y envio infoprmacion economica solicitada
		
		if (idIncidenciasEnvio != null && !idIncidenciasEnvio.trim().equals("")) {

			if (idIncidenciasEnvio.equals(AppConstants.DB_TRUE)) {// con errores
				filtrado = " and (" + cuentaErrores + ") > 0";
			} else if (idIncidenciasEnvio.equals("2")) {// sin errores
				filtrado = " and (" + cuentaErrores + ") = 0";
			} else if (idIncidenciasEnvio.equals("3")) {// con errores antes del env�o a comisi�n
				filtrado = " and (" + cuentaErrores + " AND ER." + CajgRespuestaEJGRemesaBean.C_IDTIPORESPUESTA + " = " + CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA + ") > 0";
			} else if (idIncidenciasEnvio.equals("4")) {// con errores despu�s del env�o a comisi�n
				filtrado = " and (" + cuentaErrores + " AND ER." + CajgRespuestaEJGRemesaBean.C_IDTIPORESPUESTA + " = " + CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_COMISION + ") > 0";
			} else if (idIncidenciasEnvio.equals("5")) {// con errores no en nueva remesa
				
				filtrado = " and (" + cuentaErrores + ") > 0 AND 0 = (SELECT COUNT(1)        FROM CAJG_EJGREMESA ER2,CAJG_REMESA REM2   WHERE ";
				filtrado += "ER2.IDINSTITUCION = REM2.IDINSTITUCION AND ER2.IDREMESA = REM2.IDREMESA AND REM2.IDTIPOREMESA = remesa.IDTIPOREMESA ";
				filtrado += "AND ER2.IDINSTITUCION = EJGREMESA.IDINSTITUCION AND ER2.ANIO = EJGREMESA.ANIO AND ER2.NUMERO = EJGREMESA.NUMERO AND ER2.IDTIPOEJG = EJGREMESA.IDTIPOEJG ";
				filtrado += "AND ER2.IDREMESA > EJGREMESA.IDREMESA) ";
				
				 
				
				
			} 

		}
		
		

		
		/*
		 * Construimos la primera parte de la consulta, donde escogemos los campos a recuperar y las tablas necesarias
		 */
		consulta = "select ejgremesa.idejgremesa, ejg." + ScsEJGBean.C_ANIO + ", ejg." + ScsEJGBean.C_IDINSTITUCION + ", ejg." + ScsEJGBean.C_IDTIPOEJG + ", ejg." + ScsEJGBean.C_IDFACTURACION + ", ejg. " + ScsEJGBean.C_FECHARATIFICACION + ",  " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejg." + ScsTipoEJGBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " as TIPOEJG," + " lpad( ejg." + ScsEJGBean.C_NUMEJG + "," + longitudNumEjg + ",0) as " + ScsEJGBean.C_NUMEJG + ", ejg." + ScsEJGBean.C_FECHAAPERTURA + ", f_siga_getnombreturno( ejg." + ScsEJGBean.C_IDINSTITUCION + ", ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + ") as TURNO, ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + ", guardia." + ScsGuardiasTurnoBean.C_NOMBRE + " as GUARDIA, f_siga_getunidadejg(ejg.idinstitucion,ejg.anio,ejg.numero,ejg.idtipoejg) AS NOMBRE, '' AS APELLIDO1,'' AS APELLIDO2,";
		consulta += " F_SIGA_GETRECURSO(f_siga_get_ultimoestadoejg(ejg.idinstitucion,ejg.idtipoejg, ejg.anio, ejg.numero), " + this.usrbean.getLanguage() + ") as estado" + ", ejg." + ScsEJGBean.C_NUMERO + ", (" + cuentaErrores + ") AS ERRORES" + " , (SELECT COUNT(1) FROM CAJG_EJGREMESA ER2 WHERE ER2.IDINSTITUCION = EJGREMESA.IDINSTITUCION AND ER2.ANIO = EJGREMESA.ANIO AND ER2.NUMERO = EJGREMESA.NUMERO AND ER2.IDTIPOEJG = EJGREMESA.IDTIPOEJG AND ER2.IDREMESA > EJGREMESA.IDREMESA) EN_NUEVA_REMESA" + " " ;
		//A�adiremos en campo para ver si esta permitido comunicar el informe economico cuando sea Alcala y cuando
				
		
		consulta += ",EJGREMESA.RECIBIDA IDESTADOEJGREMESA , 0 PERMITIRSOLINFECONOMICO,EJGREMESA.NUMEROINTERCAMBIO ";
			
		
		consulta +=" from " + ScsEJGBean.T_NOMBRETABLA + " ejg," + ScsGuardiasTurnoBean.T_NOMBRETABLA + " guardia," + ScsTipoEJGBean.T_NOMBRETABLA + " tipoejg," + CenColegiadoBean.T_NOMBRETABLA + " colegiado, " + CajgEJGRemesaBean.T_NOMBRETABLA + " ejgremesa";
		consulta +=" , CAJG_REMESA remesa ";
		/* realizamos la join con de las tablas que necesitamos */
		consulta += " where ejg." + ScsEJGBean.C_IDTIPOEJG + " = tipoejg." + ScsTipoEJGBean.C_IDTIPOEJG + " and " + " ejg." + ScsEJGBean.C_IDINSTITUCION + " = guardia." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "(+) and " + " ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + " = guardia." + ScsGuardiasTurnoBean.C_IDTURNO + "(+) and " + " ejg." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA + " = guardia." + ScsGuardiasTurnoBean.C_IDGUARDIA + "(+) and " + " ejg." + ScsEJGBean.C_IDINSTITUCION + " = colegiado." + CenColegiadoBean.C_IDINSTITUCION + "(+) and " + " ejg." + ScsEJGBean.C_IDPERSONA + " = colegiado." + CenColegiadoBean.C_IDPERSONA + "(+)  and ejg.idinstitucion=ejgremesa.idinstitucion and ejg.anio=ejgremesa.anio  and ejg.numero=ejgremesa.numero and ejg.idtipoejg=ejgremesa.idtipoejg ";
		consulta += " and ejgremesa.idremesa = remesa.idremesa and ejgremesa.idinstitucion = remesa.idinstitucion ";
		consulta += " and ejgremesa.idremesa=";
		contador++;
		codigos.put(new Integer(contador), miForm.getIdRemesa());
		consulta += ":" + contador;
		consulta += " and ejgremesa.idinstitucion=";
		contador++;
		codigos.put(new Integer(contador), idInstitucion);
		consulta += ":" + contador + " ";
		consulta += filtrado;

		// Y ahora concatenamos los criterios de b�squeda
		consulta += " ORDER BY " + ScsEJGBean.C_ANIO + ", to_number(" + ScsEJGBean.C_NUMEJG + ") desc";

		hashReturn.put(keyBindConsulta, consulta);
		hashReturn.put(keyBindCodigos, codigos);

		return hashReturn;
	}
	
	
	public com.siga.Utilidades.paginadores.PaginadorBind getPaginadorEJGRemesas(Hashtable miHash, DefinicionRemesas_CAJG_Form miForm, String idInstitucion,String longitudNumEjg)throws ClsExceptions,SIGAException {
	    com.siga.Utilidades.paginadores.PaginadorBind paginador=null;
	   try {
	        Hashtable htConsultaBind  = getBindEjgRemesas(miHash,  miForm, idInstitucion,longitudNumEjg);
	        String consulta = (String) htConsultaBind.get(keyBindConsulta);
	        Hashtable codigos = (Hashtable) htConsultaBind.get(keyBindCodigos);
					
	        paginador = new com.siga.Utilidades.paginadores.PaginadorBind(consulta, codigos);	
	        
	         
	   } catch (Exception e) {
	   		throw new ClsExceptions (e, "Error en BBDD");
	   }
	   return paginador;                        

	}
	
	
	public PaginadorBind getPaginadorBusquedaMantenimientoEJG(Hashtable miHash, DefinirEJGForm miForm, String idInstitucion,String longitudNumEjg)throws ClsExceptions,SIGAException {
	    	    PaginadorBind paginador=null;
	       try {
	            Hashtable htConsultaBind  = getBindBusquedaMantenimientoEJG(miHash,  miForm, TipoVentana.BUSQUEDA_EJG, idInstitucion,longitudNumEjg);
	            if(htConsultaBind!=null) {
		            String consulta = (String) htConsultaBind.get(keyBindConsulta);
		            Hashtable codigos = (Hashtable) htConsultaBind.get(keyBindCodigos);
							
		            paginador = new PaginadorBind(consulta, codigos);
	            }else {
	            	return null;
	            	
	            }
	            
	             
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error en BBDD");
	       }
	       return paginador;                        

	}
	public PaginadorBind getPaginadorBusquedaMantenimientoEJG(Hashtable htConsultaBind)throws ClsExceptions,SIGAException {
	    PaginadorBind paginador=null;
	   try {
	        String consulta = (String) htConsultaBind.get(keyBindConsulta);
	        Hashtable codigos = (Hashtable) htConsultaBind.get(keyBindCodigos);
	        paginador = new PaginadorBind(consulta, codigos);	
	         
	   } catch (Exception e) {
	   		throw new ClsExceptions (e, "Error en BBDD");
	   }
	   return paginador;                        
	
	}
	
	
	public Vector getBusquedaMantenimientoEJG (Hashtable htConsultaBind)  throws ClsExceptions, SIGAException 
	{
		Vector vDatos = null;
		try {
	        String consulta = (String) htConsultaBind.get(keyBindConsulta);
	        Hashtable codigos = (Hashtable) htConsultaBind.get(keyBindCodigos);
	        vDatos = this.selectGenericoNLSBind(consulta, codigos); 
	        	
	         
	   } catch (Exception e) {
	   		throw new ClsExceptions (e, "Error en BBDD");
	   }
	
		return vDatos;
	}
	public String addQueryWhereExpedientesByJusticiable(Hashtable miHash,Hashtable codigos, boolean isBusquedaExactaSolicitante,
			Short idInstitucion) {
		StringBuilder consulta = new StringBuilder();	
		consulta.append("SELECT ");
		consulta.append("UNIDAD.IDINSTITUCION, ");
		consulta.append("UNIDAD.ANIO, ");
		consulta.append("UNIDAD.IDTIPOEJG, ");
		consulta.append("UNIDAD.NUMERO ");
		consulta.append("FROM ");
		consulta.append("SCS_UNIDADFAMILIAREJG UNIDAD, ");
		consulta.append("SCS_PERSONAJG PJG ");
		consulta.append("WHERE ");
		consulta.append("UNIDAD.IDINSTITUCION = PJG.IDINSTITUCION ");
		consulta.append("AND UNIDAD.IDPERSONA = PJG.IDPERSONA ");
		consulta.append("AND UNIDAD.SOLICITANTE = '1' ");
		consulta.append("AND PJG.IDINSTITUCION = ");
		consulta.append(idInstitucion);
		int contador =0;
		
		
		if ((miHash.containsKey("ANIO")) && (!miHash.get("ANIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIO"));
			consulta.append(" AND UNIDAD.ANIO = :");
			consulta.append(contador);
			
		}
		if ((miHash.containsKey("IDTIPOEJG")) && (!miHash.get("IDTIPOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "IDTIPOEJG"));
			consulta.append(" AND UNIDAD.IDTIPOEJG = :");
			consulta.append(contador);
		}
		
		if ((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals(""))) {
			contador++;

			if (isBusquedaExactaSolicitante) {
				codigos.put(new Integer(contador), ((String) miHash.get("NIF")).trim());
				consulta.append(" AND LTRIM(UPPER(PJG.NIF), '0') = LTRIM(UPPER(:");
				consulta.append(contador);
				consulta.append("), '0')");
			} else {
				codigos.put(new Integer(contador), ((String) miHash.get("NIF")).trim() + "%");
				consulta.append(" AND LTRIM(UPPER(PJG.NIF), '0') LIKE LTRIM(UPPER(:");
				consulta.append(contador);
				consulta.append("), '0')");
			}
		}

		if ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals(""))) {
			contador++;
			if (isBusquedaExactaSolicitante) {
				codigos.put(new Integer(contador), ((String) miHash.get("NOMBRE")).trim());
				consulta.append(" AND UPPER(PJG.NOMBRE) = UPPER(:");
				consulta.append(contador);
				consulta.append(")");
			} else {
				consulta.append(" AND ");
				consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
						((String) miHash.get("NOMBRE")).trim(), "UPPER(PJG.NOMBRE)", contador, codigos));
			}
		}

		if ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals(""))) {
			contador++;
			if (isBusquedaExactaSolicitante) {
				codigos.put(new Integer(contador), ((String) miHash.get("APELLIDO1")).trim());
				consulta.append(" AND UPPER(PJG.APELLIDO1) = UPPER(:");
				consulta.append(contador);
				consulta.append(")");
			} else {
				consulta.append(" AND ");
				consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
						((String) miHash.get("APELLIDO1")).trim(), "UPPER(PJG.APELLIDO1)", contador, codigos));
			}
		}

		if ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals(""))) {
			contador++;
			if (isBusquedaExactaSolicitante) {
				codigos.put(new Integer(contador), ((String) miHash.get("APELLIDO2")).trim());
				consulta.append(" AND UPPER(PJG.APELLIDO2) = UPPER(:");
				consulta.append(contador);
				consulta.append(")");
			} else {
				consulta.append(" AND ");
				consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
						((String) miHash.get("APELLIDO2")).trim(), "UPPER(PJG.APELLIDO2)", contador, codigos));
			}
		}
		return consulta.toString();
		
	}
	
	public String getQueryExpedientesByJusticiable(Hashtable miHash,Hashtable codigos, boolean isBusquedaExactaSolicitante,
			Short idInstitucion) {
		StringBuilder consulta = new StringBuilder();
		int contador =0;
		if (((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals("")))
				|| ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals("")))
				|| ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals("")))
				|| ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals("")))) {
			
			consulta.append("SELECT 1 ");
			
			consulta.append("FROM  ");
			consulta.append("SCS_UNIDADFAMILIAREJG UNIDAD, SCS_PERSONAJG PJG ");
			consulta.append("WHERE ");
			consulta.append("UNIDAD.IDINSTITUCION = PJG.IDINSTITUCION AND UNIDAD.IDPERSONA = PJG.IDPERSONA ");
			consulta.append("AND UNIDAD.SOLICITANTE = '1' ");
			consulta.append("AND PJG.IDINSTITUCION = ");
			consulta.append(idInstitucion);
			
			if ((miHash.containsKey("ANIO")) && (!miHash.get("ANIO").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIO"));
				consulta.append(" AND UNIDAD.ANIO = :");
				consulta.append(contador);
				
			}
			if ((miHash.containsKey("IDTIPOEJG")) && (!miHash.get("IDTIPOEJG").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "IDTIPOEJG"));
				consulta.append(" AND UNIDAD.IDTIPOEJG = :");
				consulta.append(contador);
			}
			

			if ((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals(""))) {
				contador++;

				if (isBusquedaExactaSolicitante) {
					codigos.put(new Integer(contador), ((String) miHash.get("NIF")).trim());
					consulta.append(" AND LTRIM(UPPER(PJG.NIF), '0') = LTRIM(UPPER(:");
					consulta.append(contador);
					consulta.append("), '0')");
				} else {
					codigos.put(new Integer(contador), ((String) miHash.get("NIF")).trim() + "%");
					consulta.append(" AND LTRIM(UPPER(PJG.NIF), '0') LIKE LTRIM(UPPER(:");
					consulta.append(contador);
					consulta.append("), '0')");
				}
			}

			if ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals(""))) {
				contador++;
				if (isBusquedaExactaSolicitante) {
					codigos.put(new Integer(contador), ((String) miHash.get("NOMBRE")).trim());
					consulta.append(" AND UPPER(PJG.NOMBRE) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
				} else {
					consulta.append(" AND ");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							((String) miHash.get("NOMBRE")).trim(), "UPPER(PJG.NOMBRE)", contador, codigos));
				}
			}

			if ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals(""))) {
				contador++;
				if (isBusquedaExactaSolicitante) {
					codigos.put(new Integer(contador), ((String) miHash.get("APELLIDO1")).trim());
					consulta.append(" AND UPPER(PJG.APELLIDO1) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
				} else {
					consulta.append(" AND ");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							((String) miHash.get("APELLIDO1")).trim(), "UPPER(PJG.APELLIDO1)", contador, codigos));
				}
			}

			if ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals(""))) {
				contador++;
				if (isBusquedaExactaSolicitante) {
					codigos.put(new Integer(contador), ((String) miHash.get("APELLIDO2")).trim());
					consulta.append(" AND UPPER(PJG.APELLIDO2) = UPPER(:");
					consulta.append(contador);
					consulta.append(")");
				} else {
					consulta.append(" AND ");
					consulta.append(ComodinBusquedas.prepararSentenciaCompletaTranslateUpperBind(
							((String) miHash.get("APELLIDO2")).trim(), "UPPER(PJG.APELLIDO2)", contador, codigos));
				}
			}

		}
		return consulta.toString();
	}
	
	public boolean existenExpedientesByJusticiable(Hashtable miHash, boolean isBusquedaExactaSolicitante,
			Short idInstitucion) throws BusinessException {
		Hashtable codigos = new Hashtable();
		String consulta = getQueryExpedientesByJusticiable(miHash, codigos, isBusquedaExactaSolicitante, idInstitucion);
		boolean existeExpedientesByJusticiable = false;
		 try {
			 Vector datos = this.selectGenericoBind(consulta.toString(), codigos);
			 existeExpedientesByJusticiable =  datos.size()>0;
			 
		} catch (Exception e) {
			log.error("Error no controlado al obtener los EJGS de un justiciable",e);
			throw new BusinessException("Error no controlado al obtener los EJGS de un justiciable");
		}
			return existeExpedientesByJusticiable;

	}
	
	public Hashtable getBindBusquedaMantenimientoEJG(Hashtable miHash, DefinirEJGForm miForm, TipoVentana tipoVentana, String idInstitucion,String longitudNumEjg) throws ClsExceptions,SIGAException{
		
		// A raiz de la INC_07042_SIGA se revisan los criterios de busqueda eliminado el codigo comentado.
		// Estamos a 08/04/2010 version 1.4.2.3 del CVS
		
			
		Hashtable hashReturn = new Hashtable(); 

		Hashtable codigos = new Hashtable();
		
		boolean isBusquedaExactaSolicitante = miForm.getValorBusquedaExactaSolicitante()!=null && miForm.getValorBusquedaExactaSolicitante().equals(ClsConstants.DB_TRUE);
		miHash.put("chkBusquedaExactaSolicitante",isBusquedaExactaSolicitante);
		
		
		
		boolean esComision=(miHash.containsKey("ESCOMISION") && UtilidadesString.stringToBoolean(miHash.get("ESCOMISION").toString()));
		boolean buscarPorRemesa=false;
		boolean isA�adirJoinEstados = TipoVentana.BUSQUEDA_PREPARACION_CAJG.equals(tipoVentana) ||TipoVentana.BUSQUEDA_ANIADIR_REMESA.equals(tipoVentana)
				||(miHash.containsKey("ESTADOEJG")) && (!miHash.get("ESTADOEJG").toString().equals(""))
				||(miHash.containsKey("DESCRIPCIONESTADO")) && (!miHash.get("DESCRIPCIONESTADO").toString().equals(""));
		Short[] idInstitucionesComision = usrbean.getInstitucionesComision();
		
		// Estos son los campos que devuelve la select
		String consulta = "SELECT EJG." + ScsEJGBean.C_ANIO + ", " + 
			" EJG." + ScsEJGBean.C_IDINSTITUCION + ", " + 
			" EJG." + ScsEJGBean.C_IDTIPOEJG + ", " + 
			" EJG." + ScsEJGBean.C_IDFACTURACION + ", " +
			" EJG." + ScsEJGBean.C_FECHARATIFICACION + ", " + 
			" TIPOEJG." + ScsTipoEJGBean.C_DESCRIPCION +  " as TIPOEJG, " ;
			if(longitudNumEjg!=null)
				consulta += "LPAD( EJG." + ScsEJGBean.C_NUMEJG + ", "+longitudNumEjg+",0)" + ScsEJGBean.C_NUMEJG + ", " ;
			else
				consulta += " EJG." + ScsEJGBean.C_NUMEJG + ", " ;
			consulta +=" EJG." + ScsEJGBean.C_FECHAAPERTURA + ", " + 
			" EJG." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + ", " +
			" EJG." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA + ", " +
			" '' AS APELLIDO1, " +
			" '' AS APELLIDO2, " ;
			if(isA�adirJoinEstados){
				consulta += " NVL(MEE." + ScsMaestroEstadosEJGBean.C_DESCRIPCION + ", '') AS DESC_ESTADO, "; 
			}else{
				consulta += " NVL(F_SIGA_GET_ULTIMOESTADOEJG(EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO), '') DESC_ESTADO,";
			}
			consulta += " EJG." + ScsEJGBean.C_NUMERO + ", " ;
			consulta +=" EJG." + ScsEJGBean.C_FECHAMODIFICACION + ", " +
			" EJG." + ScsEJGBean.C_SUFIJO;
		if(idInstitucionesComision!=null && idInstitucionesComision.length>0){
			consulta +=  ",(SELECT INT.ABREVIATURA FROM CEN_INSTITUCION INT WHERE INT.IDINSTITUCION = EJG.IDINSTITUCION) INST_ABREV ";
		}
		
		// Metemos las tablas implicadas en la select 
		consulta += " FROM " + ScsEJGBean.T_NOMBRETABLA + " EJG, "+ScsTipoEJGBean.T_NOMBRETABLA + " TIPOEJG ";
		if(isA�adirJoinEstados){
			consulta += ","+ScsEstadoEJGBean.T_NOMBRETABLA + " ESTADO, " +
			ScsMaestroEstadosEJGBean.T_NOMBRETABLA + " MEE ";
		}
		// Mete las tablas para filtrar por remesa
		if((miForm.getNumeroRemesa()!=null && !miForm.getNumeroRemesa().trim().equalsIgnoreCase(""))||
				(miForm.getPrefijoRemesa()!=null && !miForm.getPrefijoRemesa().trim().equalsIgnoreCase(""))||
				(miForm.getSufijoRemesa()!=null && !miForm.getSufijoRemesa().trim().equalsIgnoreCase(""))){
			consulta += ", "+CajgRemesaBean.T_NOMBRETABLA + " rem, " + CajgEJGRemesaBean.T_NOMBRETABLA + " ejgrem ";
			buscarPorRemesa=true;
		}
//		if (TipoVentana.BUSQUEDA_ANIADIR_REMESARECONOMICA.equals(tipoVentana)) {
//			consulta += ",SCS_EEJG_PETICIONES P, SCS_EEJG_XML X ";
//		}
		// Si se filtra por acta necesitamos la tabla
//		if (((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals(""))) || 
//			((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals("")))){
//			consulta += "," +  ScsActaComisionBean.T_NOMBRETABLA + " ACTA "; 
//		}

		// Se filtra por renuncia
			
		// 	Comenzamos a cruzar tablas
		consulta += " WHERE EJG." + ScsEJGBean.C_IDTIPOEJG + " = TIPOEJG." + ScsTipoEJGBean.C_IDTIPOEJG +" ";
		
		//Filtamos solamente por los expedientes que tienen esos justiciables 
		int contador = 0;
		boolean existeFiltroJusticiable = false;
		if (((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals("")))
				|| ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals("")))
				|| ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals("")))
				|| ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals("")))) {
			existeFiltroJusticiable = true;
		}
		if(existeFiltroJusticiable) {
			boolean existeExpedienteByJusticiable =  existenExpedientesByJusticiable(miHash, isBusquedaExactaSolicitante, Short.parseShort(idInstitucion));
			
			if(existeExpedienteByJusticiable) {
				
				String filtroJusticiable = addQueryWhereExpedientesByJusticiable(miHash, codigos, isBusquedaExactaSolicitante, Short.parseShort(idInstitucion));
				
				
				consulta += " AND (EJG.IDINSTITUCION,			EJG.ANIO,			EJG.IDTIPOEJG,			EJG.NUMERO) IN (";
				consulta += filtroJusticiable;
				consulta +=")";
				contador = codigos.size(); 
				
				
			}else {
				//como no hay datos que devolver no seguimos
				return null;
				
			}
		}
		
		
		if (miForm.getIdRenuncia()!=null && !miForm.getIdRenuncia().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getIdRenuncia());
			consulta+=	" AND ejg.idrenuncia = :" + contador +" ";
					   
              
		}
		if(isA�adirJoinEstados){
			
			consulta += " AND EJG." + ScsEJGBean.C_IDINSTITUCION + " = ESTADO." + ScsEstadoEJGBean.C_IDINSTITUCION + "(+) " +
					" AND EJG." + ScsEJGBean.C_IDTIPOEJG + " = ESTADO." + ScsEstadoEJGBean.C_IDTIPOEJG + "(+) " +
					" AND EJG." + ScsEJGBean.C_NUMERO + " = ESTADO." + ScsEstadoEJGBean.C_NUMERO + "(+) " +
					" AND EJG." + ScsEJGBean.C_ANIO + " = ESTADO." + ScsEstadoEJGBean.C_ANIO + "(+) " +
					" AND " +
					 
					" ESTADO.IDESTADOPOREJG = " +
					" F_SIGA_GET_ULTIMOESTADOPOREJG ( " +
					"  ESTADO." + ScsEstadoEJGBean.C_IDINSTITUCION + 
					", ESTADO." + ScsEstadoEJGBean.C_IDTIPOEJG +
					", ESTADO." + ScsEstadoEJGBean.C_ANIO +
					", ESTADO." + ScsEstadoEJGBean.C_NUMERO + 
					
					" ) ";
			//aalg: INC_0644_SIGA. Modificaci�n de la query por los estados ejg
			consulta += " and MEE."+ ScsMaestroEstadosEJGBean.C_IDESTADOEJG +"(+) = ESTADO."+ ScsEstadoEJGBean.C_IDESTADOEJG;
			if(esComision) {
				// Si la comision deja vacio el estado le mostramos todos los que pueden ver ellos
				consulta += " AND MEE.VISIBLECOMISION = '1' ";
			 }
		}
		if(esComision ){
			
			
			consulta += " And "+AppConstants.ESTADOS_EJG.DEVUELTO_AL_COLEGIO.getCodigo()+" <> f_Siga_Get_idUltimoestadocajg(Ejg.Idinstitucion, Ejg.Idtipoejg, Ejg.Anio, Ejg.Numero, "+AppConstants.ESTADOS_EJG.DEVUELTO_AL_COLEGIO.getCodigo()+") ";
			
			
			if(miForm.getIdInstitucionComision()!=null && !miForm.getIdInstitucionComision().equals("")){
				contador++;
				codigos.put(new Integer(contador),miForm.getIdInstitucionComision());
				consulta += " AND EJG.IDINSTITUCION = :"+ contador;
			}else if(idInstitucionesComision!=null && idInstitucionesComision.length>0){
				consulta += " AND EJG.IDINSTITUCION IN (";
				for (int i = 0; i < idInstitucionesComision.length; i++) {
					contador++;
					codigos.put(new Integer(contador),idInstitucionesComision[i]);
					consulta += "  :"+ contador;
					consulta += ",";
				}
				consulta = consulta.substring(0,consulta.length()-1);
				consulta += ")";
			}else{
//				Esto por asi acaso para qu efuncione como antes
				if ((miHash.containsKey("IDINSTITUCION")) && (!miHash.get("IDINSTITUCION").toString().equals(""))) {
					contador++;
					codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDINSTITUCION"));
					consulta += " AND EJG.IDINSTITUCION = :"+ contador;
					
				}else{	
					idInstitucion="";
					if (idInstitucion.equals("")){			
					  throw new ClsExceptions("messages.comprueba.noidInstitucion");
					  
					} else {			
						consulta += " AND EJG.IDINSTITUCION =" + idInstitucion;
					}
				}		
				
			}
			
			
		}else{
			//aalg: INC_0644_SIGA. Modificaci�n de la query por los estados ejg
			
			 
			if ((miHash.containsKey("IDINSTITUCION")) && (!miHash.get("IDINSTITUCION").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDINSTITUCION"));
				consulta += " AND EJG.IDINSTITUCION = :"+ contador;
				
			}else{	
				idInstitucion="";
				if (idInstitucion.equals("")){			
				  throw new ClsExceptions("messages.comprueba.noidInstitucion");
				  
				} else {			
					consulta += " AND EJG.IDINSTITUCION =" + idInstitucion;
				}
			}				
			
			
		}
		// Parametros para poder reutilizar la busqueda EJG para busquedas CAJG
		if(TipoVentana.BUSQUEDA_PREPARACION_CAJG.equals(tipoVentana)){
			if ((miHash.containsKey("ESTADOEJG")) && (!miHash.get("ESTADOEJG").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "ESTADOEJG"));
				consulta += " AND ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " = :" + contador;
			}else{
				consulta += " AND (ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " NOT IN (" + ESTADOS_EJG.LISTO_REMITIR_COMISION.getCodigo() + ", " + ESTADOS_EJG.GENERADO_EN_REMESA.getCodigo() + ", " + ESTADOS_EJG.REMITIDO_COMISION.getCodigo() + ", " + ESTADOS_EJG.RESUELTO_COMISION.getCodigo() + ", " + ESTADOS_EJG.IMPUGNADO.getCodigo() + "))  ";
			}
			
		} else if (TipoVentana.BUSQUEDA_ANIADIR_REMESA.equals(tipoVentana)) {
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			boolean isColegiozonacomun =  ejgService.isColegioZonaComun(Short.valueOf(usrbean.getLocation()));
			boolean isColegioConfiguradoEnvioWS =  ejgService.isColegioConfiguradoEnvioPericles(Short.valueOf(usrbean.getLocation()));
			if(!isColegiozonacomun || (isColegiozonacomun && !isColegioConfiguradoEnvioWS ))
				consulta += " AND ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " IN (" + ESTADOS_EJG.LISTO_REMITIR_COMISION.getCodigo() + ", " + ESTADOS_EJG.LISTO_REMITIR_COMISION_ACT_DESIGNACION.getCodigo() + ") ";
		} else if (TipoVentana.BUSQUEDA_ANIADIR_REMESARECONOMICA.equals(tipoVentana)) {
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String idPeticionMinimoConDatos = rp.returnProperty("cajg.idPeticion_SCS_EEJG_PETICIONES_DondeEmpezoAFuncionarDatosCompletos");
		// QUITAMOS LOS QUE YA ESTAN INCLUIDOS EN UNA REMESA
			consulta += " and F_SIGA_GET_IDULTIMOESTADOEJG(EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO)<>"+ESTADOS_EJG.GENERADO_EN_REMESA.getCodigo()+" ";
			//y que tengan informe economico
			consulta += " AND ( EXISTS (SELECT 1 FROM SCS_EEJG_PETICIONES P, SCS_EEJG_XML X "; 
			consulta += " WHERE  P.IDINSTITUCION = EJG.IDINSTITUCION ";
			consulta += " AND P.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += " AND P.ANIO = EJG.ANIO ";
			consulta += " AND P.NUMERO = EJG.NUMERO ";
			consulta += " AND P.IDPETICION = X.IDPETICION ";
			consulta += " AND P.IDXML = X.IDXML ";
			consulta += " AND P.ESTADO = 30 ";
			consulta += " AND P.CSV IS NOT NULL ";
			consulta += " AND P.IDPETICION >= " + idPeticionMinimoConDatos;//eSTO LO PONEMOS YA QUE EN ESE REGISTRO ES DONDE HA EMPEZADO A FUNCIONAR LOS DATOS COMPLETOS
			consulta += " AND X.XML IS NOT NULL  )";
//			O QUE TENGA ALGUN FICHERO QUE ENVIAR
			consulta += " AND EXISTS ";
			consulta += "(SELECT	1	FROM	SCS_ESTADOEJG ES ";
			consulta += "WHERE	ES.IDINSTITUCION = EJG.IDINSTITUCION ";
			consulta += "AND ES.ANIO = EJG.ANIO ";
			consulta += "AND ES.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += "AND ES.NUMERO = EJG.NUMERO ";
			consulta += "AND ES.IDESTADOEJG = 9 ";
			consulta += "AND ES.FECHABAJA IS NULL) ";
			
			
			
			consulta += " 	) ";
			
			
			//Sacamos los  ejgs que hyan sido remitidos a comision perro que no tienen un estado posterior devuelto al colegio
			/*
			consulta += " AND ((SELECT COUNT(1) ";
			consulta += " FROM SCS_ESTADOEJG EREM ";
			consulta += " WHERE EREM.IDINSTITUCION = EJG.IDINSTITUCION ";
			consulta += " AND EREM.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += " AND EREM.ANIO = EJG.ANIO ";
			consulta += " AND EREM.NUMERO = EJG.NUMERO ";
			consulta += " AND EREM.FECHABAJA IS NULL ";
			consulta += " AND EREM.IDESTADOEJG = "+ESTADOS_EJG.REMITIDO_COMISION.getCodigo()+") > ";
			consulta += " (SELECT COUNT(1) ";
			consulta += " FROM SCS_ESTADOEJG EREM ";
			consulta += " WHERE EREM.IDINSTITUCION = EJG.IDINSTITUCION ";
			consulta += " AND EREM.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += " AND EREM.ANIO = EJG.ANIO ";
			consulta += " AND EREM.NUMERO = EJG.NUMERO ";
			consulta += " AND EREM.FECHABAJA IS NULL ";
			consulta += " AND EREM.IDESTADOEJG = "+ESTADOS_EJG.DEVUELTO_AL_COLEGIO.getCodigo()+") "; 
			consulta += " OR ";
			
			
			consulta += "  "+ESTADOS_EJG.REMITIDO_COMISION.getCodigo()+" = ";
			consulta += " nvl((SELECT DISTINCT FIRST_VALUE(EST.IDESTADOEJG) OVER(ORDER BY EST.FECHAINICIO DESC, EST.IDESTADOPOREJG DESC) ";
			consulta += " FROM SCS_ESTADOEJG EST ";
			consulta += " WHERE EST.IDINSTITUCION = EJG.IDINSTITUCION ";
			consulta += " AND EST.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += " AND EST.ANIO = EJG.ANIO ";
			consulta += " AND EST.NUMERO = EJG.NUMERO ";
			consulta += " AND EST.FECHABAJA IS NULL ";
			consulta += " AND EST.IDESTADOEJG IN ("+ESTADOS_EJG.REMITIDO_COMISION.getCodigo()+", "+ESTADOS_EJG.DEVUELTO_AL_COLEGIO.getCodigo()+")),9)) ";
	      
			
			
			consulta += " AND EXISTS (SELECT 1 ";
			consulta += " FROM SCS_ESTADOEJG EREM ";
			consulta += " WHERE EREM.IDINSTITUCION = EJG.IDINSTITUCION ";
	              
			consulta += " AND EREM.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += " AND EREM.ANIO = EJG.ANIO ";
			consulta += " AND EREM.NUMERO = EJG.NUMERO ";
			consulta += " AND EREM.FECHABAJA IS NULL ";
			consulta += " AND EREM.IDESTADOEJG = "+ESTADOS_EJG.REMITIDO_COMISION.getCodigo()+" ) ";
			
			
			consulta += " AND NOT EXISTS ";
			consulta += " (SELECT 1 ";
			consulta += " FROM CAJG_EJGREMESA EJ, CAJG_REMESA R ";
			consulta += " WHERE EJ.IDREMESA = R.IDREMESA ";
			consulta += " AND EJ.IDINSTITUCION = R.IDINSTITUCION ";
			consulta += " AND R.IDTIPOREMESA = 1 ";
			consulta += " AND EJ.IDINSTITUCION =  EJG.IDINSTITUCION ";
			consulta += " AND EJ.NUMERO =  EJG.NUMERO ";
			consulta += " AND EJ.IDTIPOEJG =  EJG.IDTIPOEJG ";
			consulta += " AND EJ.ANIO =  EJG.ANIO ";
			consulta += " AND NOT EXISTS (SELECT 1 ";
			consulta += " FROM CAJG_RESPUESTA_EJGREMESA RER ";
			consulta += " WHERE RER.IDEJGREMESA = EJ.IDEJGREMESA) "; 
			consulta += " ) ";
			
			*/
		}
		
		// Se filtra por numero cajg
		if (miForm.getNumeroCAJG()!=null && !miForm.getNumeroCAJG().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getNumeroCAJG());
			consulta += " AND LTRIM(EJG.Numero_Cajg, '0')  = LTRIM(:" + contador + ", '0') ";
		}
		
		// Se filtra por anio cajg
		if (miForm.getAnioCAJG()!=null && !miForm.getAnioCAJG().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getAnioCAJG());
			consulta += " AND EJG.Aniocajg = :" + contador;
		}
		
		// Se filtra por remesa
		if(buscarPorRemesa){
			consulta += " and ejg.anio = ejgrem.anio ";
			consulta += " and ejg.idtipoejg = ejgrem.idtipoejg ";
			consulta += " and ejg.numero = ejgrem.numero ";
			consulta += " and ejg.idinstitucion = ejgrem.idinstitucion ";
			consulta += " and rem.idremesa=ejgrem.idremesa ";
			consulta += " and rem.idinstitucion=ejgrem.idinstitucionremesa ";
			
			if(miForm.getNumeroRemesa()!=null && !miForm.getNumeroRemesa().trim().equalsIgnoreCase("")){
				contador++;
				codigos.put(new Integer(contador), miForm.getNumeroRemesa().trim());
				consulta += " and ltrim(rem.numero,'0') = ltrim(:" + contador+",'0')";
			}
			if(miForm.getSufijoRemesa()!=null && !miForm.getSufijoRemesa().trim().equalsIgnoreCase("")){
				contador++;
				codigos.put(new Integer(contador), miForm.getSufijoRemesa().trim());
				consulta += " and trim(rem.sufijo) = trim(:" + contador +")";
			}
			if(miForm.getPrefijoRemesa()!=null && !miForm.getPrefijoRemesa().trim().equalsIgnoreCase("")){
				contador++;
				codigos.put(new Integer(contador), miForm.getPrefijoRemesa().trim());
				consulta += " and trim(rem.prefijo) = trim(:" + contador + ")";
			}		
		}

		// Y ahora concatenamos los criterios de b�squeda
		if ((miForm.getFechaAperturaDesde() != null && !miForm.getFechaAperturaDesde().equals("")) ||
			(miForm.getFechaAperturaHasta() != null && !miForm.getFechaAperturaHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG.FECHAAPERTURA", GstDate.getApplicationFormatDate("",miForm.getFechaAperturaDesde()), GstDate.getApplicationFormatDate("", miForm.getFechaAperturaHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " AND " + st;                    
		}

		if ((miForm. getfechaDictamenDesde() != null && !miForm.getfechaDictamenDesde().equals("")) ||
			(miForm.getfechaDictamenHasta() != null && !miForm.getfechaDictamenHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG." + ScsEJGBean.C_FECHADICTAMEN, GstDate.getApplicationFormatDate("", miForm.getfechaDictamenDesde()), GstDate.getApplicationFormatDate("", miForm.getfechaDictamenHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " AND " + st;                    
		}
					
		if ((miForm.getFechaLimitePresentacionDesde() != null && !miForm.getFechaLimitePresentacionDesde().equals("")) ||
			(miForm.getFechaLimitePresentacionHasta() != null && !miForm.getFechaLimitePresentacionHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG."+ScsEJGBean.C_FECHALIMITEPRESENTACION, GstDate.getApplicationFormatDate("", miForm.getFechaLimitePresentacionDesde()), GstDate.getApplicationFormatDate("", miForm.getFechaLimitePresentacionHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " AND " + st;                    
		}

		if ((miHash.containsKey("IDTIPOEJG")) && (!miHash.get("IDTIPOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "IDTIPOEJG"));
			consulta += " AND EJG. " + ScsEJGBean.C_IDTIPOEJG + " = :" + contador;
		}

		if ((miHash.containsKey("ANIO")) && (!miHash.get("ANIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIO"));
			consulta += " and EJG.ANIO = :" + contador;
		}
		if (((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals("")))|| 
			((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals("")))){
			
			
			consulta += " AND EXISTS (SELECT 1 FROM SCS_EJG_ACTA EJGACTA , SCS_ACTACOMISION AC ";
			consulta += " WHERE " ;
			consulta += " EJGACTA.IDINSTITUCIONACTA = AC.IDINSTITUCION " ;
			consulta += " AND EJGACTA.IDACTA = AC.IDACTA " ;
			consulta += " AND EJGACTA.ANIOACTA = AC.ANIOACTA " ;

			consulta +=	" AND EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION  ";
			consulta += " AND EJGACTA.ANIOEJG = EJG.ANIO " ;
			consulta += " AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG ";
			consulta += " AND EJGACTA.NUMEROEJG = EJG.NUMERO ";
			
			contador++;
			codigos.put(new Integer(contador),usrbean.getIdInstitucionComision());
			consulta += " AND AC.IDINSTITUCION = :" + contador;
			
			
			if ((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIOACTA"));
				consulta += " AND AC.ANIOACTA = :" + contador;
			}
			
			if ((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"NUMEROACTA"));
				consulta += " AND AC.NUMEROACTA = :"+contador;
			}
			
			consulta += " )";
			
			
						        
	
			
			
			
			
		}

		if ((miHash.containsKey("CREADODESDE")) && (!miHash.get("CREADODESDE").toString().equals(""))) {
			if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("A")) {
				consulta += " AND (" +
					" SELECT COUNT(*) " +
					" FROM SCS_ASISTENCIA ASIST " + 
					" WHERE ASIST.IDINSTITUCION = EJG.IDINSTITUCION " +
						" AND ASIST.EJGNUMERO = EJG.NUMERO " +
						" AND ASIST.EJGANIO = EJG.ANIO " +
						" AND ASIST.EJGIDTIPOEJG = EJG.IDTIPOEJG) > 0 ";
			
			} else if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("D")){			
				consulta += " AND (SELECT count(1) " +
					" FROM SCS_EJGDESIGNA EDES " +
					" WHERE EJG.IDINSTITUCION = EDES.IDINSTITUCION " + 
						" AND EJG.NUMERO = EDES.NUMEROEJG " +
						" AND EJG.ANIO = EDES.ANIOEJG " +
						" AND EJG.IDTIPOEJG = EDES.IDTIPOEJG) > 0 ";
				
			} else if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("S")) {
				consulta += " AND (SELECT COUNT(*) " +
					" FROM SCS_SOJ SOJ " +
					" WHERE SOJ.IDINSTITUCION = EJG.IDINSTITUCION " +  
						" AND SOJ.EJGNUMERO = EJG.NUMERO " +
						" AND SOJ.EJGANIO = EJG.ANIO " +
						" AND SOJ.EJGIDTIPOEJG = EJG.IDTIPOEJG) > 0 ";
				
			} else {
				consulta+= " AND (SELECT COUNT(*) " +
						" FROM SCS_ASISTENCIA ASIST " +
						" WHERE ASIST.IDINSTITUCION = EJG.IDINSTITUCION " +
							" AND ASIST.EJGNUMERO IS NULL) > 0 " +
						
					" AND (SELECT COUNT(*) " +
						" FROM SCS_SOJ SOJ " +
						" WHERE SOJ.IDINSTITUCION = EJG.IDINSTITUCION " +
							" AND SOJ.EJGNUMERO IS NULL) > 0"; 
			}
		}

		if ((miHash.containsKey("GUARDIATURNO_IDTURNO")) && (!miHash.get("GUARDIATURNO_IDTURNO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"GUARDIATURNO_IDTURNO"));
			consulta += " AND EJG.GUARDIATURNO_IDTURNO = :" + contador;
		}

		if ((miHash.containsKey("GUARDIATURNO_IDGUARDIA")) && (!miHash.get("GUARDIATURNO_IDGUARDIA").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"GUARDIATURNO_IDGUARDIA"));
			consulta += " AND EJG.GUARDIATURNO_IDGUARDIA = :" + contador;
		}
		
		if ((miHash.containsKey("IDPERSONA")) && (!miHash.get("IDPERSONA").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"IDPERSONA"));
			consulta += " AND EJG.IDPERSONA = :" + contador;
		}
		
		if ((miHash.containsKey("DICTAMINADO")) && (!miHash.get("DICTAMINADO").toString().equals(""))) {
			if (miHash.get("DICTAMINADO").toString().equalsIgnoreCase("S")) {
				consulta += " AND EJG.FECHADICTAMEN IS NOT NULL";
			}
			else if (miHash.get("DICTAMINADO").toString().equalsIgnoreCase("N")) {
				consulta += " AND EJG.FECHADICTAMEN IS NULL";
			}
		}
		
		if ((miHash.containsKey("IDTIPODICTAMENEJG")) && (!miHash.get("IDTIPODICTAMENEJG").toString().equals(""))){
			contador++;
			codigos.put(new Integer(contador),(String)UtilidadesHash.getString(miHash, "IDTIPODICTAMENEJG"));
			consulta += " AND EJG.IDTIPODICTAMENEJG = :" + contador;
		}	
		
		
		
		if(miForm.getIdTipoResolucion()!=null && !miForm.getIdTipoResolucion().equals("")){
			
			//si contiene -1 es que quiere sacar lo que no tienen resolucion 
			int resolucionesNulas = miForm.getIdTipoResolucion().indexOf("-1");
//			Si no lo encuentra
			miHash.put("tiposResolucionBusqueda", miForm.getIdTipoResolucion());
			
			if(resolucionesNulas==-1){
				consulta += " AND EJG.IDTIPORATIFICACIONEJG in  ("+miForm.getIdTipoResolucion()+") ";	
			}else{
				if(miForm.getIdTipoResolucion().substring(resolucionesNulas+2).length()>0)
					consulta += " AND (EJG.IDTIPORATIFICACIONEJG IS NULL OR EJG.IDTIPORATIFICACIONEJG in  ("+miForm.getIdTipoResolucion().substring(resolucionesNulas+3)+") ) ";
				else
					consulta += " AND EJG.IDTIPORATIFICACIONEJG IS NULL ";
				
			}
			if(miForm.getIdTipoFundamento()!=null && !miForm.getIdTipoFundamento().equals("")){
				contador++;
				codigos.put(new Integer(contador),miForm.getIdTipoFundamento());
				consulta += " AND EJG.IDFUNDAMENTOJURIDICO = :" + contador;
				
				
			}
			
		}else{
			miHash.put("tiposResolucionBusqueda", "");
			if ((miHash.containsKey("IDTIPORATIFICACIONEJG")) && (!miHash.get("IDTIPORATIFICACIONEJG").toString().equals(""))){
				contador++;
				String ratificacion[] = UtilidadesHash.getString(miHash, "IDTIPORATIFICACIONEJG").split(",");
				codigos.put(new Integer(contador),ratificacion[0]);
				consulta += " AND EJG.IDTIPORATIFICACIONEJG = :" + contador;
				
				if ((miHash.containsKey("IDFUNDAMENTOJURIDICO")) && (!miHash.get("IDFUNDAMENTOJURIDICO").toString().equals(""))){
					contador++;
					codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDFUNDAMENTOJURIDICO"));
					consulta += " AND EJG.IDFUNDAMENTOJURIDICO = :" + contador;
					
					
				}
			}
			
		}

		if (UtilidadesHash.getString(miHash,"NUMEJG") != null && !UtilidadesHash.getString(miHash,"NUMEJG").equalsIgnoreCase("")) {
			if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash, "NUMEJG"))) {
				contador++;
				consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)UtilidadesHash.getString(miHash, "NUMEJG")).trim(), "EJG.NUMEJG", contador, codigos);
				
			}else if (ComodinBusquedas.hasComa(UtilidadesHash.getString(miHash, "NUMEJG")) || ComodinBusquedas.hasGuion(UtilidadesHash.getString(miHash, "NUMEJG"))) {
				contador++;
				
				ComodinBusquedas comodinBusquedas = new ComodinBusquedas();
				consulta += " AND " + comodinBusquedas.prepararSentenciaCompletaEJGBind(((String)UtilidadesHash.getString(miHash, "NUMEJG")).trim(), "EJG.NUMEJG", contador, codigos);
				contador =  codigos.size();
			}else{
				contador++;
			    codigos.put(new Integer(contador), (String)UtilidadesHash.getString(miHash, "NUMEJG").trim());
				consulta += " AND LTRIM(EJG.NUMEJG, '0')  = LTRIM(:" + contador + ", '0') ";
			}
		}

		if ((miHash.containsKey("IDTIPOEJGCOLEGIO")) && (!miHash.get("IDTIPOEJGCOLEGIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDTIPOEJGCOLEGIO"));
			consulta += " AND EJG.IDTIPOEJGCOLEGIO = :" + contador;
		}
//			FIXME REVSION DE QUERYS PESADAS	
		
		// jbd // Consulta para el interesado
		// Hasta ahora se estaba haciendo una consulta independiente para cada campo del nombre
		// Lo optimo es meter todo en la misma consulta
		

		if ((miHash.containsKey("JUZGADO")) && (!miHash.get("JUZGADO").toString().equals(""))) {
			/*String a[]=((String)miHash.get("JUZGADO")).split(",");
			contador++;
			consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(a[0].trim(),"ejg.JUZGADO", contador, codigos);
			*/
			String a[]=((String)miHash.get("JUZGADO")).split(",");
			contador++;
			codigos.put(new Integer(contador), a[0]);
			consulta += " AND EJG.JUZGADO = :" + contador;	
		}

		//aalg: INC_08086_SIGA
		if ((miHash.containsKey("ASUNTO")) && (!miHash.get("ASUNTO").toString().equals(""))) {
			contador++;
			consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("ASUNTO")).trim(), "EJG.OBSERVACIONES", contador, codigos);
		}

		if ((miHash.containsKey("PROCEDIMIENTO")) && (!miHash.get("PROCEDIMIENTO").toString().equals(""))) {
			contador++;
			consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("PROCEDIMIENTO")).trim(), "EJG.NUMEROPROCEDIMIENTO", contador, codigos);
		}
		// jbd // Filtramos por preceptivo
		if ((miHash.containsKey(ScsEJGBean.C_PRECEPTIVO)) && (!miHash.get(ScsEJGBean.C_PRECEPTIVO).toString().equals(""))) {
			contador++;
			consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get(ScsEJGBean.C_PRECEPTIVO)).trim(), "EJG."+ScsEJGBean.C_PRECEPTIVO, contador, codigos);
		}
		
		if ((miHash.containsKey("NIG")) && (!miHash.get("NIG").toString().equals(""))) {
			contador++;
			consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("NIG")).trim(), "EJG.NIG", contador, codigos);
		}		

		/*if (miForm.getCalidad()!=null && !miForm.getCalidad().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getCalidad());		
			 consulta += " and ejg.Idtipoencalidad = :" + contador;
		}*/
		
		if ((miForm.getFechaPresentacionPonenteDesde() != null && !miForm.getFechaPresentacionPonenteDesde().equals("")) ||
				(miForm.getFechaPresentacionPonenteHasta() != null && !miForm.getFechaPresentacionPonenteHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG.FECHAPRESENTACIONPONENTE", GstDate.getApplicationFormatDate("", miForm.getFechaPresentacionPonenteDesde()), GstDate.getApplicationFormatDate("", miForm.getFechaPresentacionPonenteHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " AND " + st;                    
		}
				
		if ((miHash.containsKey("IDPONENTE")) && (!miHash.get("IDPONENTE").toString().equals(""))){
			contador++;
			codigos.put(new Integer(contador), miHash.get("IDPONENTE").toString());
			consulta += " AND " +  ScsEJGBean.C_IDPONENTE + " = :" + contador;
			contador++;
			codigos.put(new Integer(contador), usrbean.getIdInstitucionComision());
			consulta += " AND " +  ScsEJGBean.C_IDINSTITUCIONPONENTE + " = :" + contador;
			
			
		}
		
		//aalg: INC_0644_SIGA. Modificaci�n de la query por los estados ejg 
		if ((miHash.containsKey("ESTADOEJG")) && (!miHash.get("ESTADOEJG").toString().equals(""))) {
			
			if(!TipoVentana.BUSQUEDA_PREPARACION_CAJG.equals(tipoVentana)){
				contador++;
				codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "ESTADOEJG"));
				consulta += " AND ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " = :" + contador;
			}
			
			if ((miForm.getfechaEstadoDesde() != null && !miForm.getfechaEstadoDesde().equals("")) ||
				(miForm.getfechaEstadoHasta() != null && !miForm.getfechaEstadoHasta().equals(""))) {
			  
			        if(miForm.getfechaEstadoDesde() != null && !miForm.getfechaEstadoDesde().equals("")){
			        	contador++;
			        	codigos.put(new Integer(contador), miForm.getfechaEstadoDesde());
			        	consulta += " AND TRUNC(ESTADO."+ ScsEstadoEJGBean.C_FECHAINICIO + ") >= TRUNC(TO_DATE( :" + contador+"))";
			        }
			        
			        if(miForm.getfechaEstadoHasta() != null && !miForm.getfechaEstadoHasta().equals("")){
			        	contador++;
			        	codigos.put(new Integer(contador), miForm.getfechaEstadoHasta());
			        	consulta += " AND TRUNC(ESTADO."+ ScsEstadoEJGBean.C_FECHAINICIO + ") <= TRUNC(TO_DATE( :" + contador+"))";
			        }
			} 
			
		} 

		consulta += " ORDER BY " + ScsEJGBean.C_ANIO + " DESC, " +
			" TO_NUMBER(" + ScsEJGBean.C_NUMEJG + ") DESC ";

		hashReturn.put(keyBindConsulta,consulta);
		hashReturn.put(keyBindCodigos,codigos);

		return hashReturn;
	}
	

	
	public Vector getEJGPtesEnviar(BusinessManager businessManager, Hashtable miHash, DefinirEJGForm miForm) throws BusinessException{
		Vector datos = new Vector();
		try {
			Hashtable htConsultaBind = getBindWhereEJG(miHash, miForm);
			String consulta = (String) htConsultaBind.get(keyBindConsulta);
			Hashtable codigos = (Hashtable) htConsultaBind.get(keyBindCodigos);
			
			
			// Acceso a BBDD
			RowsContainer rc = null;
			rc = new RowsContainer();
			if (rc.queryBind(consulta, codigos)) {
				
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					String idInstitucion =  (String) registro.get("IDINSTITUCION");
					String idTipoEjg =  (String) registro.get("IDTIPOEJG");
					String anio =  (String) registro.get("ANIO");
					String numero =  (String) registro.get("NUMERO");
					boolean isExpedientePteEnviarCAJG =  isExpedientePteEnviarCAJG(businessManager, idInstitucion, idTipoEjg, anio, numero);
					if (isExpedientePteEnviarCAJG)
						datos.add(registro);
				}
			}
		} catch (Exception e) {
			throw new BusinessException("Error al ejecutar el 'select' en B.D."
					+ e.toString());
		}
		return datos;

	}

public Hashtable getBindWhereEJG(Hashtable miHash, DefinirEJGForm miForm) throws ClsExceptions,SIGAException{
		
		Hashtable hashReturn = new Hashtable(); 

		Hashtable codigos = new Hashtable();
		int contador=0;
		boolean isBusquedaExactaSolicitante = miForm.getValorBusquedaExactaSolicitante()!=null && miForm.getValorBusquedaExactaSolicitante().equals(ClsConstants.DB_TRUE);
		miHash.put("chkBusquedaExactaSolicitante",isBusquedaExactaSolicitante);
		
		String where = "";
		
			where+= " SELECT EJG.ANIO,	       EJG.NUMERO,     EJG.IDINSTITUCION, ";
			where+=	" EJG.IDTIPOEJG, EJG.FECHAMODIFICACION,  F_SIGA_GET_IDULTIMOESTADOEJG(EJG.IDINSTITUCION, ";
			where+=	" EJG.IDTIPOEJG, ";
			where+=	" EJG.ANIO, ";
			where+=	" EJG.NUMERO) IDESTADO, ";
			where+=	" F_SIGA_GET_ULTIMOESTADOEJG(EJG.IDINSTITUCION, ";
			where+=	" EJG.IDTIPOEJG, ";
			where+=	" EJG.ANIO, ";
			where+=	" EJG.NUMERO) DESC_ESTADO ";
			where+=	" ,EJG.GUARDIATURNO_IDTURNO,EJG.GUARDIATURNO_IDGUARDIA " ;
			where+=	" ,EJG.NUMEJG ";
			where+=	" ,EJG.FECHAAPERTURA ";
			where+=	" ,TEJ.DESCRIPCION TIPOEJG";
		
		
	                                    
		where+=	" FROM SCS_DESIGNA DESIGNA, SCS_EJGDESIGNA EJGDES, SCS_EJG EJG, SCS_TIPOEJG TEJ ";
		where+=	" WHERE DESIGNA.IDINSTITUCION = EJGDES.IDINSTITUCION ";
		where+=	" AND DESIGNA.IDTURNO = EJGDES.IDTURNO ";
		where+=	" AND DESIGNA.ANIO = EJGDES.ANIODESIGNA ";
		where+=	" AND DESIGNA.NUMERO = EJGDES.NUMERODESIGNA ";
		where+=	" AND EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ";
		where+=	" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ";
		where+=	" AND EJGDES.ANIOEJG = EJG.ANIO ";
		where+=	" AND EJGDES.NUMEROEJG = EJG.NUMERO ";
		where+=	" AND EJG.IDTIPOEJG = TEJ.IDTIPOEJG ";
		
		where+=	" AND EXISTS (SELECT * ";
		where+=	" FROM SCS_ACTUACIONDESIGNA ACT ";
		where+=	" WHERE ACT.IDINSTITUCION = DESIGNA.IDINSTITUCION ";
		where+=	" AND ACT.IDTURNO = DESIGNA.IDTURNO ";
		where+=	" AND ACT.ANIO = DESIGNA.ANIO ";
		where+=	" AND ACT.NUMERO = DESIGNA.NUMERO ";
		where+=	" AND ACT.VALIDADA = '1' ";
		where+=	" AND ACT.FACTURADO IS NULL) ";
			   
		if (miForm.getIdRenuncia()!=null && !miForm.getIdRenuncia().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getIdRenuncia());
			where+=	" AND ejg.idrenuncia = :" + contador +" ";
					   
              
		}
		
			
			 
			if ((miHash.containsKey("IDINSTITUCION")) && (!miHash.get("IDINSTITUCION").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDINSTITUCION"));
				where += " AND EJG.IDINSTITUCION = :"+ contador;
			}else{
				throw new ClsExceptions("messages.comprueba.noidInstitucion");
				
			}				
		// Parametros para poder reutilizar la busqueda EJG para busquedas CAJG
		 
		
		// Se filtra por numero cajg
		if (miForm.getNumeroCAJG()!=null && !miForm.getNumeroCAJG().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getNumeroCAJG());
			where += " AND LTRIM(EJG.Numero_Cajg, '0')  = LTRIM(:" + contador + ", '0') ";
		}
		
		// Se filtra por anio cajg
		if (miForm.getAnioCAJG()!=null && !miForm.getAnioCAJG().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getAnioCAJG());
			where += " AND EJG.Aniocajg = :" + contador;
		}


		// Y ahora concatenamos los criterios de b�squeda
		if ((miForm.getFechaAperturaDesde() != null && !miForm.getFechaAperturaDesde().equals("")) ||
			(miForm.getFechaAperturaHasta() != null && !miForm.getFechaAperturaHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG.FECHAAPERTURA", GstDate.getApplicationFormatDate("",miForm.getFechaAperturaDesde()), GstDate.getApplicationFormatDate("", miForm.getFechaAperturaHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			where += " AND " + st;                    
		}

		if ((miForm. getfechaDictamenDesde() != null && !miForm.getfechaDictamenDesde().equals("")) ||
			(miForm.getfechaDictamenHasta() != null && !miForm.getfechaDictamenHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG." + ScsEJGBean.C_FECHADICTAMEN, GstDate.getApplicationFormatDate("", miForm.getfechaDictamenDesde()), GstDate.getApplicationFormatDate("", miForm.getfechaDictamenHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			where += " AND " + st;                    
		}
					
		if ((miForm.getFechaLimitePresentacionDesde() != null && !miForm.getFechaLimitePresentacionDesde().equals("")) ||
			(miForm.getFechaLimitePresentacionHasta() != null && !miForm.getFechaLimitePresentacionHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG."+ScsEJGBean.C_FECHALIMITEPRESENTACION, GstDate.getApplicationFormatDate("", miForm.getFechaLimitePresentacionDesde()), GstDate.getApplicationFormatDate("", miForm.getFechaLimitePresentacionHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			where += " AND " + st;                    
		}

		if ((miHash.containsKey("IDTIPOEJG")) && (!miHash.get("IDTIPOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "IDTIPOEJG"));
			where += " AND EJG. " + ScsEJGBean.C_IDTIPOEJG + " = :" + contador;
		}

		if ((miHash.containsKey("ANIO")) && (!miHash.get("ANIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIO"));
			where += " and EJG.ANIO = :" + contador;
		}
		if (((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals("")))|| 
			((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals("")))){
			
			
			where += " AND EXISTS (SELECT 1 FROM SCS_EJG_ACTA EJGACTA , SCS_ACTACOMISION AC ";
			where += " WHERE " ;
			where += " EJGACTA.IDINSTITUCIONACTA = AC.IDINSTITUCION " ;
			where += " AND EJGACTA.IDACTA = AC.IDACTA " ;
			where += " AND EJGACTA.ANIOACTA = AC.ANIOACTA " ;

			where +=	" AND EJGACTA.IDINSTITUCIONEJG = EJG.IDINSTITUCION  ";
			where += " AND EJGACTA.ANIOEJG = EJG.ANIO " ;
			where += " AND EJGACTA.IDTIPOEJG = EJG.IDTIPOEJG ";
			where += " AND EJGACTA.NUMEROEJG = EJG.NUMERO ";
			
			contador++;
			codigos.put(new Integer(contador),usrbean.getIdInstitucionComision());
			where += " AND AC.IDINSTITUCION = :" + contador;
			
			
			if ((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIOACTA"));
				where += " AND AC.ANIOACTA = :" + contador;
			}
			
			if ((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"NUMEROACTA"));
				where += " AND AC.NUMEROACTA = :"+contador;
			}
			
			where += " )";
			
			
						        
	
			
			
			
			
		}

		if ((miHash.containsKey("CREADODESDE")) && (!miHash.get("CREADODESDE").toString().equals(""))) {
			if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("A")) {
				where += " AND (" +
					" SELECT COUNT(*) " +
					" FROM SCS_ASISTENCIA ASIST " + 
					" WHERE ASIST.IDINSTITUCION = EJG.IDINSTITUCION " +
						" AND ASIST.EJGNUMERO = EJG.NUMERO " +
						" AND ASIST.EJGANIO = EJG.ANIO " +
						" AND ASIST.EJGIDTIPOEJG = EJG.IDTIPOEJG) > 0 ";
			
			} else if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("D")){			
				where += " AND (SELECT count(1) " +
					" FROM SCS_EJGDESIGNA EDES " +
					" WHERE EJG.IDINSTITUCION = EDES.IDINSTITUCION " + 
						" AND EJG.NUMERO = EDES.NUMEROEJG " +
						" AND EJG.ANIO = EDES.ANIOEJG " +
						" AND EJG.IDTIPOEJG = EDES.IDTIPOEJG) > 0 ";
				
			} else if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("S")) {
				where += " AND (SELECT COUNT(*) " +
					" FROM SCS_SOJ SOJ " +
					" WHERE SOJ.IDINSTITUCION = EJG.IDINSTITUCION " +  
						" AND SOJ.EJGNUMERO = EJG.NUMERO " +
						" AND SOJ.EJGANIO = EJG.ANIO " +
						" AND SOJ.EJGIDTIPOEJG = EJG.IDTIPOEJG) > 0 ";
				
			} else {
				where+= " AND (SELECT COUNT(*) " +
						" FROM SCS_ASISTENCIA ASIST " +
						" WHERE ASIST.IDINSTITUCION = EJG.IDINSTITUCION " +
							" AND ASIST.EJGNUMERO IS NULL) > 0 " +
						
					" AND (SELECT COUNT(*) " +
						" FROM SCS_SOJ SOJ " +
						" WHERE SOJ.IDINSTITUCION = EJG.IDINSTITUCION " +
							" AND SOJ.EJGNUMERO IS NULL) > 0"; 
			}
		}

		if ((miHash.containsKey("GUARDIATURNO_IDTURNO")) && (!miHash.get("GUARDIATURNO_IDTURNO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"GUARDIATURNO_IDTURNO"));
			where += " AND EJG.GUARDIATURNO_IDTURNO = :" + contador;
		}

		if ((miHash.containsKey("GUARDIATURNO_IDGUARDIA")) && (!miHash.get("GUARDIATURNO_IDGUARDIA").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"GUARDIATURNO_IDGUARDIA"));
			where += " AND EJG.GUARDIATURNO_IDGUARDIA = :" + contador;
		}
		
		if ((miHash.containsKey("IDPERSONA")) && (!miHash.get("IDPERSONA").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"IDPERSONA"));
			where += " AND EJG.IDPERSONA = :" + contador;
		}
		
		if ((miHash.containsKey("DICTAMINADO")) && (!miHash.get("DICTAMINADO").toString().equals(""))) {
			if (miHash.get("DICTAMINADO").toString().equalsIgnoreCase("S")) {
				where += " AND EJG.FECHADICTAMEN IS NOT NULL";
			}
			else if (miHash.get("DICTAMINADO").toString().equalsIgnoreCase("N")) {
				where += " AND EJG.FECHADICTAMEN IS NULL";
			}
		}
		
		if ((miHash.containsKey("IDTIPODICTAMENEJG")) && (!miHash.get("IDTIPODICTAMENEJG").toString().equals(""))){
			contador++;
			codigos.put(new Integer(contador),(String)UtilidadesHash.getString(miHash, "IDTIPODICTAMENEJG"));
			where += " AND EJG.IDTIPODICTAMENEJG = :" + contador;
		}	
		
		
		
		if(miForm.getIdTipoResolucion()!=null && !miForm.getIdTipoResolucion().equals("")){
			
			//si contiene -1 es que quiere sacar lo que no tienen resolucion 
			int resolucionesNulas = miForm.getIdTipoResolucion().indexOf("-1");
//			Si no lo encuentra
			miHash.put("tiposResolucionBusqueda", miForm.getIdTipoResolucion());
			
			if(resolucionesNulas==-1){
				where += " AND EJG.IDTIPORATIFICACIONEJG in  ("+miForm.getIdTipoResolucion()+") ";	
			}else{
				if(miForm.getIdTipoResolucion().substring(resolucionesNulas+2).length()>0)
					where += " AND (EJG.IDTIPORATIFICACIONEJG IS NULL OR EJG.IDTIPORATIFICACIONEJG in  ("+miForm.getIdTipoResolucion().substring(resolucionesNulas+3)+") ) ";
				else
					where += " AND EJG.IDTIPORATIFICACIONEJG IS NULL ";
				
			}
			if(miForm.getIdTipoFundamento()!=null && !miForm.getIdTipoFundamento().equals("")){
				contador++;
				codigos.put(new Integer(contador),miForm.getIdTipoFundamento());
				where += " AND EJG.IDFUNDAMENTOJURIDICO = :" + contador;
				
				
			}
			
		}else{
			miHash.put("tiposResolucionBusqueda", "");
			if ((miHash.containsKey("IDTIPORATIFICACIONEJG")) && (!miHash.get("IDTIPORATIFICACIONEJG").toString().equals(""))){
				contador++;
				String ratificacion[] = UtilidadesHash.getString(miHash, "IDTIPORATIFICACIONEJG").split(",");
				codigos.put(new Integer(contador),ratificacion[0]);
				where += " AND EJG.IDTIPORATIFICACIONEJG = :" + contador;
				
				if ((miHash.containsKey("IDFUNDAMENTOJURIDICO")) && (!miHash.get("IDFUNDAMENTOJURIDICO").toString().equals(""))){
					contador++;
					codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDFUNDAMENTOJURIDICO"));
					where += " AND EJG.IDFUNDAMENTOJURIDICO = :" + contador;
					
					
				}
			}
			
		}

		if (UtilidadesHash.getString(miHash,"NUMEJG") != null && !UtilidadesHash.getString(miHash,"NUMEJG").equalsIgnoreCase("")) {
			if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash, "NUMEJG"))) {
				contador++;
				where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)UtilidadesHash.getString(miHash, "NUMEJG")).trim(), "EJG.NUMEJG", contador, codigos);
				
			}else if (ComodinBusquedas.hasComa(UtilidadesHash.getString(miHash, "NUMEJG")) || ComodinBusquedas.hasGuion(UtilidadesHash.getString(miHash, "NUMEJG"))) {
				contador++;
				
				ComodinBusquedas comodinBusquedas = new ComodinBusquedas();
				where += " AND " + comodinBusquedas.prepararSentenciaCompletaEJGBind(((String)UtilidadesHash.getString(miHash, "NUMEJG")).trim(), "EJG.NUMEJG", contador, codigos);
				contador =  codigos.size();
			}else{
				contador++;
			    codigos.put(new Integer(contador), (String)UtilidadesHash.getString(miHash, "NUMEJG").trim());
				where += " AND LTRIM(EJG.NUMEJG, '0')  = LTRIM(:" + contador + ", '0') ";
			}
		}

		if ((miHash.containsKey("IDTIPOEJGCOLEGIO")) && (!miHash.get("IDTIPOEJGCOLEGIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash, "IDTIPOEJGCOLEGIO"));
			where += " AND EJG.IDTIPOEJGCOLEGIO = :" + contador;
		}
				
		
		// jbd // Consulta para el interesado
		// Hasta ahora se estaba haciendo una consulta independiente para cada campo del nombre
		// Lo optimo es meter todo en la misma consulta
		if (((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals("")))||
			((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals("")))||
			((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals("")))||
			((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals("")))){
			
			where += " AND (SELECT COUNT(1) " + 
					" FROM SCS_UNIDADFAMILIAREJG UNIDAD, " +
						" SCS_EJG EJG2, " +
						" SCS_PERSONAJG PJG " + 
					" WHERE UNIDAD.IDINSTITUCION = PJG.IDINSTITUCION " +
						" AND UNIDAD.IDPERSONA = PJG.IDPERSONA " +
						" AND EJG2.IDINSTITUCION = UNIDAD.IDINSTITUCION(+) " +
						" AND EJG2.ANIO = UNIDAD.ANIO(+) " +
						" AND EJG2.NUMERO = UNIDAD.NUMERO(+) " +
						" AND EJG2.IDTIPOEJG = UNIDAD.IDTIPOEJG(+) " +
						" AND UNIDAD.SOLICITANTE(+) = '1' " +
						" AND EJG2.IDINSTITUCION = EJG.IDINSTITUCION " +
						" AND EJG2.ANIO = EJG.ANIO " + 
						" AND EJG2.NUMERO = EJG.NUMERO " + 
						" AND EJG2.IDTIPOEJG = EJG.IDTIPOEJG ";
			
			if ((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals(""))){
				contador++;
				
				if(isBusquedaExactaSolicitante){
					codigos.put(new Integer(contador), ((String)miHash.get("NIF")).trim());
					where += " AND LTRIM(UPPER(PJG.NIF), '0') = LTRIM(UPPER(:"+contador+"), '0') ";
				}
				else{
					codigos.put(new Integer(contador), ((String)miHash.get("NIF")).trim() + "%");
					where += " AND LTRIM(UPPER(PJG.NIF), '0') LIKE LTRIM(UPPER(:"+contador+"), '0') ";
				}
			}
			
			if ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals(""))){
				contador++;
				if(isBusquedaExactaSolicitante){
					codigos.put(new Integer(contador), ((String)miHash.get("NOMBRE")).trim());
					where += " AND UPPER(PJG.NOMBRE) = :"+contador+" "; 
				}
				else
					where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("NOMBRE")).trim(), "UPPER(PJG.NOMBRE)", contador, codigos);
			}
			
			if ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals(""))){
				contador++; 
				if(isBusquedaExactaSolicitante){
					codigos.put(new Integer(contador), ((String)miHash.get("APELLIDO1")).trim());
					where += " AND UPPER(PJG.apellido1) = :"+contador+" "; 
				}
				else
					where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO1")).trim(), "UPPER(PJG.apellido1)", contador, codigos);
			}
			
			if ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals(""))){
				contador++;
				if(isBusquedaExactaSolicitante){
					codigos.put(new Integer(contador), ((String)miHash.get("APELLIDO2")).trim());
					where += " AND UPPER(PJG.apellido2) = :"+contador+" "; 
				}
				else
					where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO2")).trim(), "UPPER(PJG.apellido2)", contador, codigos);
			}
			
			where += ") >0 ";
		}

		if ((miHash.containsKey("JUZGADO")) && (!miHash.get("JUZGADO").toString().equals(""))) {
			/*String a[]=((String)miHash.get("JUZGADO")).split(",");
			contador++;
			consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(a[0].trim(),"ejg.JUZGADO", contador, codigos);
			*/
			String a[]=((String)miHash.get("JUZGADO")).split(",");
			contador++;
			codigos.put(new Integer(contador), a[0]);
			where += " AND EJG.JUZGADO = :" + contador;	
		}

		//aalg: INC_08086_SIGA
		if ((miHash.containsKey("ASUNTO")) && (!miHash.get("ASUNTO").toString().equals(""))) {
			contador++;
			where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("ASUNTO")).trim(), "EJG.OBSERVACIONES", contador, codigos);
		}

		if ((miHash.containsKey("PROCEDIMIENTO")) && (!miHash.get("PROCEDIMIENTO").toString().equals(""))) {
			contador++;
			where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("PROCEDIMIENTO")).trim(), "EJG.NUMEROPROCEDIMIENTO", contador, codigos);
		}
		// jbd // Filtramos por preceptivo
		if ((miHash.containsKey(ScsEJGBean.C_PRECEPTIVO)) && (!miHash.get(ScsEJGBean.C_PRECEPTIVO).toString().equals(""))) {
			contador++;
			where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get(ScsEJGBean.C_PRECEPTIVO)).trim(), "EJG."+ScsEJGBean.C_PRECEPTIVO, contador, codigos);
		}
		
		if ((miHash.containsKey("NIG")) && (!miHash.get("NIG").toString().equals(""))) {
			contador++;
			where += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("NIG")).trim(), "EJG.NIG", contador, codigos);
		}		

		/*if (miForm.getCalidad()!=null && !miForm.getCalidad().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getCalidad());		
			 consulta += " and ejg.Idtipoencalidad = :" + contador;
		}*/
		
		if ((miForm.getFechaPresentacionPonenteDesde() != null && !miForm.getFechaPresentacionPonenteDesde().equals("")) ||
				(miForm.getFechaPresentacionPonenteHasta() != null && !miForm.getFechaPresentacionPonenteHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("EJG.FECHAPRESENTACIONPONENTE", GstDate.getApplicationFormatDate("", miForm.getFechaPresentacionPonenteDesde()), GstDate.getApplicationFormatDate("", miForm.getFechaPresentacionPonenteHasta()), contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			where += " AND " + st;                    
		}
				
		if ((miHash.containsKey("IDPONENTE")) && (!miHash.get("IDPONENTE").toString().equals(""))){
			contador++;
			codigos.put(new Integer(contador), miHash.get("IDPONENTE").toString());
			where += " AND " +  ScsEJGBean.C_IDPONENTE + " = :" + contador;
			contador++;
			codigos.put(new Integer(contador), usrbean.getIdInstitucionComision());
			where += " AND " +  ScsEJGBean.C_IDINSTITUCIONPONENTE + " = :" + contador;
			
			
		}
//		where += ")";
		//aalg: INC_0644_SIGA. Modificaci�n de la query por los estados ejg 
		if ((miHash.containsKey("ESTADOEJG")) && (!miHash.get("ESTADOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "ESTADOEJG"));
			where += " AND F_SIGA_GET_IDULTIMOESTADOEJG(EJG.IDINSTITUCION, ";
			where+=	" EJG.IDTIPOEJG, ";
			where+=	" EJG.ANIO, ";
			where+=	" EJG.NUMERO)=:"+contador;
			
			
			
		}else{
				where += " AND (F_SIGA_GET_IDULTIMOESTADOEJG(EJG.IDINSTITUCION,  EJG.IDTIPOEJG, EJG.ANIO,EJG.NUMERO) ";
				where+=	"  NOT IN (" + ESTADOS_EJG.LISTO_REMITIR_COMISION.getCodigo() + ", " + ESTADOS_EJG.GENERADO_EN_REMESA.getCodigo() + ", " + ESTADOS_EJG.RESUELTO_COMISION.getCodigo() + ", " + ESTADOS_EJG.LISTO_REMITIR_COMISION_ACT_DESIGNACION.getCodigo() + ", "  + ESTADOS_EJG.IMPUGNADO.getCodigo() + "))  ";
			
			
		}
//			where += " ORDER BY " + ScsEJGBean.C_ANIO + " DESC,  TO_NUMBER(NUMERO) DESC ";

		hashReturn.put(keyBindConsulta,where);
		hashReturn.put(keyBindCodigos,codigos);

		return hashReturn;
	}
	
	
	private Vector getTurnoEjgSalidaOficio (String idInstitucion, String idTurno,
			String salidaNombre,String salidaAbrev) throws ClsExceptions  
	{
		try {
			//sql.append(" f_siga_getabrevturno(ejg.idinstitucion, ejg.guardiaturno_idturno) AS ABREV_TURNO, ");
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			htCodigos.put(new Integer(1), idInstitucion);
			htCodigos.put(new Integer(2), idTurno);
			StringBuffer sql = new StringBuffer();
			sql.append("select nvl(NOMBRE,'-') as ");
			sql.append(salidaNombre);
			sql.append(" ,t.abreviatura as ");
			sql.append(salidaAbrev);
			sql.append(" from scs_turno t ");
			sql.append(" where ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTurno);
			sql.append(" AND idturno = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de procuradores.");
		}
	}
	
	public Vector getCalificacionEjgSalida (String idInstitucion, String tipoEjg,
			String anio, String numero,String idioma, String longitudNumEjg) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select ejg.idpersonajg IDPERSONAJG,");
			sql.append(" ejg.idpersona IDPERSONA,EJG.JUZGADO IDJUZGADO,EJG.COMISARIA IDCOMISARIA, ");
			sql.append(" ejg.idfundamentocalif IDFUNDAMENTOCALIF,ejg.idtipodictamenejg IDTIPODICTAMENEJG, ");
			sql.append(" ejg.idtipoejgcolegio IDTIPOEJGCOLEGIO, ");
			sql.append(" ejg.guardiaturno_idturno GUARDIATURNO_IDTURNO, ");
			sql.append(" ejg.guardiaturno_idguardia GUARDIATURNO_IDGUARDIA, ");
			sql.append(" ejg.idtiporatificacionejg IDTIPORATIFICACIONEJG, ");
			
			sql.append(" ejg.idinstitucion_proc IDINSTITUCIONPROC, ");
			sql.append(" ejg.idprocurador IDPROCURADOR, ");
			
			sql.append(" nvl(lpad( ejg.numejg,"+longitudNumEjg+",0) || '/' || substr(ejg.anio, 3, 2), '-') as NUM_SOLICITUD, ");
			sql.append(" lpad( ejg.numejg,"+longitudNumEjg+",0) as NUMERO_EJG, ejg.anio as ANIO_EJG, ");
			
			sql.append(" nvl(to_char(ejg.fechaapertura, 'DD/mm/YYYY'), '-') as FECHA_SOLICITUD, ");
			//sql.append(" ' ' as SITUACION_LABORAL, ");
			
			sql.append(" pkg_siga_fecha_en_letra.f_siga_fechacompletaenletra(nvl(ejg.fechadictamen, SYSDATE), 'M', :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append(" ) as FECHA_DICTAMEN, ");
			sql.append(" nvl(ejg.dictamen, '-') as DICTAMEN_TEXTO, ");
			sql.append(" ejg.idfundamentocalif as IDFUNDAMENTO, ");
			sql.append(" nvl(ejg.dictamen, '-') as OBSERVACIONES_DICTAMEN, ");
			sql.append(" ejg.observaciones AS OBSERVACIONES, ");
			
			sql.append(" ejg.fecharesolucioncajg FECHA_RES_CAJG, ");
			sql.append(" EJG.OBSERVACIONES AS ASUNTO_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMERODILIGENCIA AS NUMDILIGENCIA_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMEROPROCEDIMIENTO AS NUMPROCED_DEFENSA_JURIDICA ");
			sql.append(" , EJG.ANIOPROCEDIMIENTO AS ANIOPROCED_DEFENSA_JURIDICA ");
			sql.append(" , Decode(EJG.ANIOPROCEDIMIENTO, null, EJG.NUMEROPROCEDIMIENTO, (EJG.NUMEROPROCEDIMIENTO || '/' || EJG.ANIOPROCEDIMIENTO))  AS NUMANIOPROCED_DEFENSA_JURIDICA ");			
			sql.append(" ,TO_CHAR(EJG.FECHAPRESENTACION, 'dd-mm-yyyy') as FECHAPRESENTACION");
			sql.append(" ,TO_CHAR(EJG.FECHALIMITEPRESENTACION, 'dd-mm-yyyy') as FECHALIMITEPRESENTACION, ");
			
			sql.append(" ejg.idpretension AS PRETENSION ");			
			
		       
			
			sql.append(" from scs_ejg  ejg ");
			sql.append(" where  ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" ejg.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and ejg.idtipoejg = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(" and ejg.anio = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(" and ejg.numero = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getCalificacionEjgSalida.");
		}
	}
	/**
	 * Saca el solicitante principal. Atencion porque deberia ser una lista ya que puede haber varios
	 * 
	 * @param idInstitucion
     * @param idPersonaJG scs_personajg scs_ejg.IDPERSONAJG
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getSolicitanteCalificacionEjgSalida (String idInstitucion, String anioEjg, String tipoEjg, String numeroEjg, String idPersonaJG,String idioma) throws ClsExceptions {
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" select F_SIGA_GETRECURSO(Tgl.Descripcion, decode(Pjg.Idlenguaje, NULL, IDIOMAS.IDIOMA, Pjg.Idlenguaje)) AS SITUACION_LABORAL,");
			
			sql.append(" NVL(pjg.nombre || ' ' || pjg.apellido1 || ' ' || pjg.apellido2, '-') AS NOMBRE_APE_SOLIC, ");
			
			sql.append(" pjg.nif AS NIFCIF_SOLIC, ");
			sql.append(" pjg.IDREPRESENTANTEJG, ");
			
			sql.append(" DECODE(pjg.DIRECCION, NULL, '-', (SELECT UPPER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION, IDIOMAS.IDIOMA), 1, 1)) ");
			sql.append(" || LOWER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION, IDIOMAS.IDIOMA), 2)) FROM CEN_TIPOVIA TV WHERE TV.IDTIPOVIA = pjg.IDTIPOVIA AND TV.IDINSTITUCION = fam.IDINSTITUCION) ");
			sql.append(" || ' ' || pjg.DIRECCION || ' ' || pjg.NUMERODIR || ' ' || pjg.ESCALERADIR || ' ' || pjg.PISODIR || ' ' || pjg.PUERTADIR) AS DIR_SOLIC, ");
			
			sql.append(" NVL(pjg.codigopostal, '-') AS  CP_SOLIC, ");
			
			sql.append(" NVL((SELECT F_SIGA_GETRECURSO(Nombre, DECODE(Pjg.Idlenguaje, NULL, IDIOMAS.IDIOMA, Pjg.Idlenguaje)) FROM CEN_POBLACIONES WHERE IDPOBLACION = pjg.idpoblacion), '-') AS POB_SOLIC, ");
			
			sql.append(" NVL((SELECT F_SIGA_GETRECURSO(Nombre, DECODE(Pjg.Idlenguaje, NULL, IDIOMAS.IDIOMA, Pjg.Idlenguaje)) FROM CEN_PROVINCIAS WHERE Idprovincia = Pjg.Idprovincia), '-') AS PROV_SOLIC, ");
			
			sql.append(" DECODE(Pjg.Idlenguaje, NULL, IDIOMAS.IDIOMA, Pjg.Idlenguaje) AS IDLENGUAJE,");
			
			sql.append(" f_Siga_Getcodidioma(DECODE(Pjg.Idlenguaje, NULL, IDIOMAS.IDIOMA, Pjg.Idlenguaje)) AS CODIGOLENGUAJE ");
			
			sql.append(" FROM scs_unidadfamiliarejg fam, ");
			sql.append(" scs_personajg pjg, ");
			sql.append(" SCS_TIPOGRUPOLABORAL tgl, ");
			sql.append(" (SELECT ");
			sql.append(idioma);
			sql.append(" AS IDIOMA FROM DUAL) IDIOMAS ");
			
			sql.append(" WHERE fam.idinstitucion = tgl.idinstitucion(+) ");
			sql.append(" AND fam.idtipogrupolab = tgl.idtipogrupolab(+) ");
			sql.append(" AND pjg.idinstitucion = fam.idinstitucion ");
			sql.append(" AND pjg.idpersona = fam.idpersona ");		
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersonaJG);
			sql.append(" AND fam.idpersona = :");			
			sql.append(keyContador);			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" AND fam.idtipoejg = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioEjg);
			sql.append(" AND fam.anio = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroEjg);
			sql.append(" AND fam.numero = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND fam.idinstitucion = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getSolicitanteCalificacionEjgSalida");
		}
	}
	
	private Vector getDesignaCalificacionEjgSalida (String idInstitucion, String tipoEjg,
			String anioEjg, String numeroEjg) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select ");  
			sql.append(" des.idinstitucion DES_INSTITUCION,des.idturno DES_IDTURNO,des.anio DES_ANIO, des.numero DES_NUMERO, ");
			sql.append(" nvl(des.resumenasunto, '-') as ASUNTO, ");
			sql.append(" nvl((SELECT j.NOMBRE || ' (' || substr(p.nombre, 0, 30) || ')' ");
			sql.append(" FROM SCS_JUZGADO j, cen_poblaciones p ");
			sql.append(" where j.idpoblacion = p.idpoblacion(+) ");
			sql.append(" and IDINSTITUCION = des.idinstitucion_juzg ");
			sql.append(" and IDJUZGADO = des.idjuzgado),'-') AS JUZGADO, ");
			sql.append(" nvl(des.numprocedimiento, '-') as AUTO, ");
			sql.append(" nvl(to_char(des.fechaentrada, 'DD/mm/YYYY'), '-') AS FECHA_DESIGNA, ");
			sql.append(" nvl(to_char(des.anio), '-') as ANIO_OFICIO, ");
			sql.append(" nvl(des.codigo, '-') as NUM_OFICIO ");
			sql.append(" from scs_designa             des , scs_ejgdesigna          ejgd");
			
			sql.append(" where  ");
			
            sql.append("     des.idinstitucion = ejgd.idinstitucion ");
            sql.append(" and des.idturno = ejgd.idturno ");
            sql.append(" and des.anio = ejgd.aniodesigna ");
            sql.append(" and des.numero = ejgd.numerodesigna ");

            sql.append(" And Des.Estado <> 'A' ");

			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" and ejgd.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and ejgd.idtipoejg = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioEjg);
			sql.append(" and ejgd.anioejg  = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroEjg);
			sql.append(" and ejgd.numeroejg = :");
			sql.append(keyContador);
			
			// ordenar para que la primera designacion (la que se cogera mas tarde) 
			// sea la mas moderna y en estado Validada, si se puede elegir
			sql.append(" Order By Case Estado  When 'V' Then  1  When 'F' Then  2  End, ");
			sql.append("          Des.Fechaentrada Desc ");
            
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getDesignaCalificacionEjgSalida");
		}
	}
	
	private Vector getColegiadoDesignaCalificacionEjgSalida (String idInstitucion, String idTurno,
			String anioDesigna, String numeroDesigna) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT "); 
			sql.append(" nvl(per.nombre || ' ' || per.apellidos1 || ' ' || per.apellidos2 || '  ' || ");
			sql.append(" f_siga_calculoncolegiado(cli.idinstitucion, cli.idpersona) || '', ");
			sql.append(" '-') as NOMBRE_COLEGIADO, ");
			sql.append(" f_siga_getdireccioncliente(cli.idinstitucion, cli.idpersona, 2, 11) AS TELEFONO1_DESPACHO ");
			sql.append(" FROM cen_persona per, cen_cliente cli, scs_designasletrado descol ");
			sql.append(" where  ");
			sql.append(" cli.idpersona = per.idpersona ");
			sql.append(" and cli.idpersona = per.idpersona ");
			sql.append(" and (descol.fechadesigna = ");
			sql.append(" (select max(s.fechadesigna) ");
			sql.append(" from scs_designasletrado s ");
			sql.append(" where s.idinstitucion = descol.idinstitucion ");
			sql.append(" and s.idturno = descol.idturno ");
			sql.append(" and s.anio = descol.anio ");
			sql.append(" and s.numero = descol.numero ");
			sql.append(" and s.fecharenuncia is null) or descol.fechadesigna is null) ");
			sql.append(" and descol.idinstitucion = cli.idinstitucion ");
			sql.append(" and descol.idpersona = cli.idpersona ");

			sql.append(" AND  ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" descol.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTurno);
			sql.append(" and descol.idturno = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioDesigna);
			sql.append(" and descol.anio  = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroDesigna);
			sql.append(" and descol.numero= :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getColegiadoDesignaCalificacionEjgSalida");
		}
	}
	/**
	 * Saca el solicitante principal. Atencion porque deberia ser una lista ya que puede haber varios
	 * 
	 * @param idInstitucion
     * @param idPersona scs_ejg.IDPERSONA
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getColegiadoEjgCalificacionEjgSalida (String idInstitucion, String idPersona) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select nvl(f_siga_calculoncolegiado(cli2.idinstitucion, cli2.idpersona), ");
			sql.append(" '-') as NCOLEGIADO ");
			sql.append(" from  ");
			sql.append(" cen_cliente             cli2,cen_persona             per2 ");

			sql.append(" WHERE ");
			sql.append(" cli2.idpersona = per2.idpersona ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND cli2.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" AND cli2.idpersona  = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getColegiadoEjgCalificacionEjgSalida");
		}
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idProcurador ejg.idprocurador
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getProcuradorCalificacionEjgSalida (String idInstitucion, String idProcurador,String campoSalida) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select nvl(pro.nombre || ' ' || pro.apellidos1 || ' ' || pro.apellidos2 || '  ' || ");
			sql.append(" pro.ncolegiado || '', ");
			sql.append(" '-') as ");
			sql.append(campoSalida);
			sql.append(" from  ");
			sql.append(" scs_procurador          pro ");

			sql.append(" WHERE ");
			
		
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" pro.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idProcurador);
			sql.append(" AND pro.idprocurador  = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getProcuradorCalificacionEjgSalida");
		}
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idProcurador ejg.idprocurador
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getDatosProcuradorEjgSalida (String idInstitucion, String idProcurador) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select nvl(pro.nombre || ' ' || pro.apellidos1 || ' ' || pro.apellidos2 || '  ' || ");
			sql.append(" pro.ncolegiado || '', ");
			sql.append(" '-') as PROCURADOR_DEFENSA_JURIDICA,");
			sql.append(" pro.ncolegiado as PROCURADOR_DJ_NCOLEGIADO, ");
			sql.append(" pro.telefono1 as PROCURADOR_DJ_TELEFONO1, ");
			sql.append(" pro.telefono2 as PROCURADOR_DJ_TELEFONO2, ");
			sql.append(" pro.Domicilio AS PROCURADOR_DOMICILIO_D_J, ");		
			sql.append(" pro.EMAIL AS PROCURADOR_EMAIL, ");
			
			sql.append(" pro.Codigopostal AS PROCURADOR_CODIGOPOSTAL_D_J, ");
			sql.append(" (Select Provincia.Nombre ");
			sql.append("  From Cen_Provincias Provincia ");
			sql.append(" Where Provincia.Idprovincia = Pro.Idprovincia) As PROCURADOR_PROVINCIA_D_J, ");
			sql.append("  (Select Poblacion.Nombre ");
			sql.append("  From Cen_Poblaciones Poblacion, Cen_Provincias Provincia ");
			sql.append("  Where Poblacion.Idprovincia = Provincia.Idprovincia ");
			sql.append("  And Pro.Idprovincia = Poblacion.Idprovincia ");
			sql.append(" And Pro.Idpoblacion = Poblacion.Idpoblacion) As PROCURADOR_POBLACION_D_J ");
			sql.append(" ,CP.NOMBRE AS COLPROCURADORES_NOMBRE, ");
			sql.append(" CP.DIRECCION AS COLPROCURADORES_DIRECCION, ");
			sql.append(" (SELECT F_SIGA_GETRECURSO(TIPOVIA.DESCRIPCION, 1) ");
			sql.append(" FROM CEN_TIPOVIA TIPOVIA ");
			sql.append(" WHERE TIPOVIA.IDTIPOVIA = CP.IDTIPOVIA ");
			sql.append(" AND TIPOVIA.IDINSTITUCION = 2000) AS COLPROCURADORES_TIPOVIA, ");
			sql.append(" CP.NUMERODIR AS COLPROCURADORES_NUMERODIR, ");
			sql.append(" CP.ESCALERADIR AS COLPROCURADORES_ESCALERADIR, ");
			
			sql.append(" CP.PISODIR AS COLPROCURADORES_PISODIR, ");
			sql.append(" CP.PUERTADIR AS COLPROCURADORES_PUERTADIR, ");
			sql.append(" CP.CODIGOPOSTAL AS COLPROCURADORES_CODPOSTAL, ");
			sql.append(" (SELECT POBLACION.NOMBRE ");
			sql.append(" FROM CEN_POBLACIONES POBLACION, CEN_PROVINCIAS PROVINCIA ");
			sql.append(" WHERE POBLACION.IDPROVINCIA = PROVINCIA.IDPROVINCIA ");
			sql.append(" AND CP.IDPROVINCIA = POBLACION.IDPROVINCIA ");
			sql.append(" AND CP.IDPOBLACION = POBLACION.IDPOBLACION) AS COLPROCURADORES_POBLACION, ");
			sql.append(" (SELECT PROVINCIA.NOMBRE ");
			sql.append(" FROM CEN_PROVINCIAS PROVINCIA ");
			sql.append(" WHERE PROVINCIA.IDPROVINCIA = CP.IDPROVINCIA) AS COLPROCURADORES_PROVINCIA ");
			
			sql.append(" from  ");
			sql.append(" scs_procurador          pro ");
			sql.append(" , CEN_COLEGIOPROCURADOR CP ");
			sql.append(" WHERE ");
			sql.append("  PRO.IDCOLPROCURADOR = CP.IDCOLPROCURADOR(+) AND ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" pro.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idProcurador);
			sql.append(" AND pro.idprocurador  = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getProcuradorCalificacionEjgSalida");
		}
	}
	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idFundamento ejg.idfundamentocalif
	 * @return
	 * @throws ClsExceptions
	 */
	
	private Vector getFundamentoEjgSalida (String idInstitucion, String idFundamento,String idioma) throws ClsExceptions {			
		try {
			Hashtable htCodigos = new Hashtable();
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			
			String sql = "SELECT F_SIGA_GETRECURSO(NVL(TFC.DESCRIPCION, '-'), :1) AS FUNDAMENTO, "+
					" TEXTOPLANTILLA AS TEXTO_FUNDAMENTO_CALIFICACION "+
					" FROM SCS_TIPOFUNDAMENTOCALIF TFC "+
					" WHERE TFC.IDINSTITUCION = :2 "+
					" AND TFC.IDFUNDAMENTOCALIF = :3";
			
			htCodigos.put(1, idioma);
			htCodigos.put(2, idInstitucion);
			htCodigos.put(3, idFundamento);
						
			return helperInformes.ejecutaConsultaBind(sql, htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getFundamentoEjgSalida");
		}
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idDictamen ejg.idtipodictamenejg
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getDictamenCalificacionEjgSalida (String idInstitucion, String idDictamen,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select nvl(f_siga_getrecurso(dic.descripcion, :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append("), '-') as DICTAMEN, ");
			sql.append(" UPPER(f_siga_getrecurso(nvl(dic.descripcion, '-'), :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append(" )) as RESOLUCION ");
			sql.append(" from scs_tipodictamenejg     dic");
			sql.append(" WHERE ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" dic.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idDictamen);
			sql.append(" AND dic.idtipodictamenejg  = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getDictamenCalificacionEjgSalida");
		}
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idJuzgado EJG.JUZGADO
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getJuzgadoEjgSalida (String idInstitucion, String idJuzgado) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" select JUZDF.NOMBRE AS JUZGADO_DEFENSA_JURIDICA, POB.NOMBRE AS POBLACION_JUZGADO, PROV.NOMBRE AS PROVINCIA_JUZGADO ");
			sql.append(" from SCS_JUZGADO JUZDF, CEN_POBLACIONES POB, CEN_PROVINCIAS PROV ");
			sql.append(" WHERE ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" JUZDF.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idJuzgado);
			sql.append(" AND JUZDF.IDJUZGADO  = :");
			sql.append(keyContador);
	
			sql.append(" AND JUZDF.Idpoblacion = pob.idpoblacion ");
			sql.append(" AND JUZDF.Idprovincia = prov.Idprovincia ");
		
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getJuzgadoEjgSalida");
		}
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idComisaria EJG.COMISARIA
	 * @return
	 * @throws ClsExceptions
	 */
	
	private Vector getComisariaEjgSalida (String idInstitucion, String idComisaria) throws ClsExceptions {
		try {			
			String sql = "SELECT COMISARIA.NOMBRE AS COMISARIA_DEFENSA_JURIDICA, " +
					" COMISARIA.NOMBRE AS COMISARIA_D_J, " +
					" COMISARIA.DOMICILIO AS DIR_COMISARIA_D_J, " +
					" COMISARIA.CODIGOPOSTAL AS CP_COMISARIA_D_J, " +
					" POBL.NOMBRE AS POBLACION_COMISARIA_D_J, " +
					" PROV.NOMBRE AS PROVINCIA_COMISARIA_D_J " +
					
				" FROM SCS_COMISARIA COMISARIA " +
					" LEFT OUTER JOIN CEN_POBLACIONES POBL on COMISARIA.IDPOBLACION = POBL.IDPOBLACION " +
					" LEFT OUTER JOIN CEN_PROVINCIAS PROV on COMISARIA.IDPROVINCIA = PROV.IDPROVINCIA " +
				
				" WHERE COMISARIA.IDINSTITUCION = " + idInstitucion +
					" AND COMISARIA.IDCOMISARIA = " + idComisaria;
			
			Vector vComisaria = new Vector();
			RowsContainer rc = new RowsContainer();
			
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					Hashtable hDatos = fila.getRow();
					vComisaria.add(hDatos);					
				}
			} 
	
			return vComisaria;
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getComisariaEjgSalida");
		}
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param idTipoColegio
	 * @param campoSalida Este el campo de salida para el informe. me lo llevare al HelperInformes
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getTipoColegioSalida (String idInstitucion, String idTipoColegio,
			String campoSalida,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select f_siga_getrecurso(descripcion, :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			
			sql.append(" )  as ");
			sql.append(campoSalida);
			sql.append(" from scs_tipoejgcolegio ");
			sql.append(" WHERE ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTipoColegio);
			sql.append("  and idtipoejgcolegio = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getTipoColegioSalida");
		}
	}
	/**
	 * 
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anioEjg
	 * @param numeroEjg
	 * @param salida Este el campo de salida para el informe. me lo llevare al HelperInformes
	 * @return
	 * @throws ClsExceptions
	 */
	
	private Vector getIngresosAnualesUnidadFamiliarEjgSalida (String idInstitucion, String tipoEjg,
			String anioEjg, String numeroEjg, String salida) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT decode(SUM(nvl(IMPORTEINGRESOSANUALES, '0')), ");
			sql.append(" 0, ");
			sql.append(" '-', ");
			sql.append(" f_siga_formatonumero(SUM(nvl(IMPORTEINGRESOSANUALES, ");
			sql.append(" '0')), ");
			sql.append("                  2)) AS ");
			sql.append(salida);
			sql.append(" FROM SCS_UNIDADFAMILIAREJG ");
			
			sql.append(" where  ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and IDTIPOEJG = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioEjg);
			sql.append(" and anio  = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroEjg);
			sql.append(" and numero = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getIngresosAnualesUnidadFamiliarEjgSalida");
		}
	}
	private Vector getSolicitantes (String idInstitucion, String tipoEjg,
			String anioEjg, String numeroEjg, String salida) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			//int keyContador = 0;
			
			htCodigos.put(new Integer(1), idInstitucion);
			htCodigos.put(new Integer(2), anioEjg);
			htCodigos.put(new Integer(3), numeroEjg);
			htCodigos.put(new Integer(4), tipoEjg);
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT f_siga_getunidadejg(:1,:2,:3,:4) AS ");
			sql.append(salida);
			sql.append(" FROM dual ");
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getSolicitantes");
		}
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anioEjg
	 * @param numeroEjg
	 * @param idioma
	 * @param registro
	 * @param vSalida
	 * @param infosolicitante
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getDatosInformeCalificacion (String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg, String idioma, Hashtable registro, Vector vSalida, Vector infosolicitante) throws ClsExceptions {	 
		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		try {			
			//A�adimos la descripcion de el tipo de colegio
			String idTipoColegio = (String)registro.get("IDTIPOEJGCOLEGIO");
			helperInformes.completarHashSalida(registro,getTipoColegioSalida(idInstitucion,idTipoColegio,"TIPOEJGCOLEGIO",idioma));
			
			//A�adimos los ingresos
			helperInformes.completarHashSalida(registro,getIngresosAnualesUnidadFamiliarEjgSalida(idInstitucion, tipoEjg,anioEjg,numeroEjg,"INGRESOS"));
			
			//A�adimos la lista de solicitantes separados por ,
			helperInformes.completarHashSalida(registro,getSolicitantes(idInstitucion, tipoEjg,anioEjg,numeroEjg,"TODOS_SOLICITANTES"));
			
			//A�adimos el nombre del turno de la guardia
			String idTurno = (String)registro.get("GUARDIATURNO_IDTURNO");
			helperInformes.completarHashSalida(registro,getTurnoEjgSalidaOficio(idInstitucion, idTurno,"TURNO","ABREV_TURNO"));
			
			//A�adimos el nombre de la guardia
			String idGuardia = (String)registro.get("GUARDIATURNO_IDGUARDIA");
			Hashtable htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), idInstitucion);
			htFuncion.put(new Integer(2), idTurno);
			htFuncion.put(new Integer(3), idGuardia);
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "f_siga_getnombreguardia", "NOMBRE_GUARDIA"));
			
			//Aniadimoms la resolucion cajg
			String idTipoRatificacion = (String)registro.get("IDTIPORATIFICACIONEJG");
			htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), idTipoRatificacion);
			htFuncion.put(new Integer(2), "1");
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRATIFICACION", "RESOLUCION_CAJG"));
			
			//Aniadimos el solicitante del ejg
			helperInformes.completarHashSalida(registro,infosolicitante);
			
			//Aniadimos los datos de la designa asociada al ejg
			helperInformes.completarHashSalida(registro,getDesignaCalificacionEjgSalida(idInstitucion, tipoEjg,anioEjg,numeroEjg));
			
			//Aniadimos el Juzgado
			String idJuzgado = (String)registro.get("IDJUZGADO");
			helperInformes.completarHashSalida(registro,getJuzgadoEjgSalida(idInstitucion, idJuzgado));
	
			String numeroDesigna = (String)registro.get("DES_NUMERO");
			String anioDesigna = (String)registro.get("DES_ANIO");
			String idTurnoDesigna  = (String)registro.get("DES_IDTURNO");
			String idInstitucionDesigna  = (String)registro.get("DES_INSTITUCION");
			
			if(numeroDesigna!=null &&!numeroDesigna.trim().equals("")){
			//Aniadimos los datos del colegiado de la designa
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucionDesigna);
				htFuncion.put(new Integer(2), idTurnoDesigna);
				htFuncion.put(new Integer(3), anioDesigna);
				htFuncion.put(new Integer(4), numeroDesigna);
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "f_siga_getncolletrado_designa", "NCOLEGIADO_DESIGNADO"));
				
				//Aniadimos otros datos del colegiado de la designa...Esto estaba asi . Se ha preguntado y dicen que lo dejemos tal como estaba
				helperInformes.completarHashSalida(registro,getColegiadoDesignaCalificacionEjgSalida(idInstitucionDesigna, idTurnoDesigna,anioDesigna,numeroDesigna));
			} else {
				
				UtilidadesHash.set(registro, "TELEFONO1_DESPACHO", "");
				UtilidadesHash.set(registro, "NOMBRE_COLEGIADO", "");
				UtilidadesHash.set(registro, "NCOLEGIADO_DESIGNADO", "");
				
				//INC_05795_SIGA Requerimineto Baleares. Si el ejg no tiene designacion asociada
				//ponemos el numero de auto como el numero de procedimiento de la pestana de defensa juridica
				//idem juzgado
				//idem asunto
				
				String numProcedDJ = (String)registro.get("NUMPROCED_DEFENSA_JURIDICA");
				String anioProcedDJ = (String)registro.get("ANIOPROCED_DEFENSA_JURIDICA");
				if(numProcedDJ!=null && !numProcedDJ.trim().equals("")){
					if(anioProcedDJ!=null && !anioProcedDJ.trim().equals("")){
						registro.put("AUTO", numProcedDJ+"/"+anioProcedDJ);
					}else{
						registro.put("AUTO", numProcedDJ);
					}
				}
				String juzgadoDJ = (String)registro.get("JUZGADO_DEFENSA_JURIDICA");
				registro.put("JUZGADO", juzgadoDJ);
				
				String asuntoDJ = (String)registro.get("ASUNTO_DEFENSA_JURIDICA");
				registro.put("ASUNTO", asuntoDJ);				
			}
			
			//Aniadimos los datos del colegiado del ejg
			String idPersona = (String)registro.get("IDPERSONA");
			helperInformes.completarHashSalida(registro,getColegiadoEjgCalificacionEjgSalida(idInstitucion, idPersona));
			
			//Aniadimos los datos del procurador
			String idProcurador = (String)registro.get("IDPROCURADOR");
			String idInstitucionProc = (String)registro.get("IDINSTITUCIONPROC");
			helperInformes.completarHashSalida(registro,getProcuradorCalificacionEjgSalida(idInstitucionProc, idProcurador,"PROCURADOR"));
			
			// Aniadimos el fundamento
			String idFundamento = (String)registro.get("IDFUNDAMENTOCALIF");
			helperInformes.completarHashSalida(registro,getFundamentoEjgSalida(idInstitucion, idFundamento,idioma));
			
			//Aniadimos el Dictamen
			String idDictamen = (String)registro.get("IDTIPODICTAMENEJG");
			helperInformes.completarHashSalida(registro,getDictamenCalificacionEjgSalida(idInstitucion,idDictamen,idioma));
						
			// Agregamos la comisaria del ejg
			String idComisaria = (String)registro.get("IDCOMISARIA");			
			if (idComisaria!=null && !idComisaria.trim().equals("") && idInstitucion!=null && !idInstitucion.trim().equals("")) { 
				helperInformes.completarHashSalida(registro, getComisariaEjgSalida(idInstitucion, idComisaria));
				
				// Comprobamos nulos
				if (!registro.containsKey("COMISARIA_D_J") || registro.get("COMISARIA_D_J")==null) {
					registro.put("COMISARIA_D_J", " ");
				}
				if (!registro.containsKey("COMISARIA_DEFENSA_JURIDICA") || registro.get("COMISARIA_DEFENSA_JURIDICA")==null) {
					registro.put("COMISARIA_DEFENSA_JURIDICA", " ");
				}
				if (!registro.containsKey("CP_COMISARIA_D_J") || registro.get("CP_COMISARIA_D_J")==null) {
					registro.put("CP_COMISARIA_D_J", " ");
				}
				if (!registro.containsKey("DIR_COMISARIA_D_J") || registro.get("DIR_COMISARIA_D_J")==null) {
					registro.put("DIR_COMISARIA_D_J", " ");
				}
				if (!registro.containsKey("POBLACION_COMISARIA_D_J") || registro.get("POBLACION_COMISARIA_D_J")==null) {
					registro.put("POBLACION_COMISARIA_D_J", " ");
				}
				if (!registro.containsKey("PROVINCIA_COMISARIA_D_J") || registro.get("PROVINCIA_COMISARIA_D_J")==null) {
					registro.put("PROVINCIA_COMISARIA_D_J", " ");
				}				
				
			} else {				
				registro.put("COMISARIA_D_J", " ");
				registro.put("COMISARIA_DEFENSA_JURIDICA", " ");
				registro.put("CP_COMISARIA_D_J", " ");
				registro.put("DIR_COMISARIA_D_J", " ");
				registro.put("POBLACION_COMISARIA_D_J", " ");
				registro.put("PROVINCIA_COMISARIA_D_J", " ");
			}			
			
			//Aniadimos la pretension
			String idPretension = (String)registro.get("PRETENSION");
			helperInformes.completarHashSalida(registro,getPretension(idPretension, idInstitucion, idioma));
			
			// Obtengo el representante legal
			String idRepresentanteJG = UtilidadesHash.getString(registro, "IDREPRESENTANTEJG");
			if (idRepresentanteJG!=null && !idRepresentanteJG.equals("")) {
				
				ScsPersonaJGAdm scsPersonaJGAdm=new ScsPersonaJGAdm(this.usrbean);
				Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(idRepresentanteJG, idInstitucion);
				registro.put("NOMBRE_REPR_DEF", (String) representanteLegalDefendido.get("NOMBRE_PJG"));
				registro.put("NOMBRE_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("NOMBRE_PJG")).toUpperCase());
				registro.put("APELLIDO1_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO1_PJG"));
				registro.put("APELLIDO1_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO1_PJG")).toUpperCase());
				registro.put("APELLIDO2_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO2_PJG"));
				registro.put("APELLIDO2_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO2_PJG")).toUpperCase());
				String sexoRepr_def = UtilidadesString.getMensajeIdioma(usrbean, (String) representanteLegalDefendido.get("SEXO_PJG"));		
				registro.put("SEXO_REPR_DEF",sexoRepr_def);
				registro.put("O_A_REPR_DEF", (String) representanteLegalDefendido.get("O_A_PJG"));
				registro.put("EL_LA_REPR_DEF", (String) representanteLegalDefendido.get("EL_LA_PJG"));
				registro.put("NIFCIF_REPR_DEF", (String) representanteLegalDefendido.get("NIF_PJG"));
				registro.put("IDPERSONA_REPR_DEF", (String) representanteLegalDefendido.get("IDPERSONA_PJG"));
				registro.put("IDDIRECCION_REPR_DEF", (String) representanteLegalDefendido.get("IDDIRECCION_PJG"));
				registro.put("DOMICILIO_REPR_DEF", (String) representanteLegalDefendido.get("DOMICILIO_PJG"));
				registro.put("CODIGOPOSTAL_REPR_DEF", (String) representanteLegalDefendido.get("CP_PJG"));
				registro.put("TELEFONO1_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO1_PJG"));
				registro.put("TELEFONO2_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO2_PJG"));
				registro.put("MOVIL_REPR_DEF", (String) representanteLegalDefendido.get("MOVIL_PJG"));
				registro.put("LISTA_TELEFONOS_REPR_DEF", (String) representanteLegalDefendido.get("LISTA_TELEFONOS_REPR"));
				registro.put("FAX1_REPR_DEF", (String) representanteLegalDefendido.get("FAX_PJG"));
				registro.put("CORREOELECTRONICO_REPR_DEF", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
				registro.put("NOMBRE_POBLACION_REPR_DEF", (String) representanteLegalDefendido.get("POBLACION_PJG"));
				registro.put("NOMBRE_PROVINCIA_REPR_DEF", (String) representanteLegalDefendido.get("PROVINCIA_PJG"));
				
			} else {
				registro.put("NOMBRE_REPR_DEF", "");
				registro.put("NOMBRE_REPR_DEF_MAYUS", "");
				registro.put("APELLIDO1_REPR_DEF", "");
				registro.put("APELLIDO1_REPR_DEF_MAYUS","");
				registro.put("APELLIDO2_REPR_DEF", "");
				registro.put("APELLIDO2_REPR_DEF_MAYUS", "");
				registro.put("SEXO_REPR_DEF", "");
				registro.put("O_A_REPR_DEF", "");
				registro.put("EL_LA_REPR_DEF", "");
				registro.put("NIFCIF_REPR_DEF", "");
				registro.put("IDPERSONA_REPR_DEF", "");
				registro.put("IDDIRECCION_REPR_DEF", "");
				registro.put("DOMICILIO_REPR_DEF", "");
				registro.put("CODIGOPOSTAL_REPR_DEF","");
				registro.put("TELEFONO1_REPR_DEF", "");
				registro.put("TELEFONO2_REPR_DEF", "");
				registro.put("MOVIL_REPR_DEF", "");
				registro.put("LISTA_TELEFONOS_REPR_DEF", "");
				registro.put("FAX1_REPR_DEF", "");
				registro.put("CORREOELECTRONICO_REPR_DEF", "");
				registro.put("NOMBRE_POBLACION_REPR_DEF","");
				registro.put("NOMBRE_PROVINCIA_REPR_DEF", "");
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosInformeCalificacion");
		}
		
		return vSalida;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anio
	 * @param numero
	 * @param longitudNumEjg
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector getEjgSalida (String idInstitucion, String tipoEjg, String anio, String numero, String longitudNumEjg) throws ClsExceptions {
			RowsContainer rc = new RowsContainer();
			Vector datos = new Vector();
			
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT EJGD.IDINSTITUCION DES_INSTITUCION, ");
			sql.append(" EJGD.IDTURNO DES_IDTURNO, ");
			sql.append(" EJGD.ANIODESIGNA DES_ANIO, "); 
			sql.append(" EJGD.NUMERODESIGNA DES_NUMERO, "); 
			sql.append(" F_SIGA_GETIDLETRADO_DESIGNA(d.idinstitucion,d.idturno,d.anio  , d.numero) IDPERSONADESIGNADO, ");
			sql.append(" EJG.IDPERSONA IDPERSONATRAMITADOR, ");
			sql.append(" EJG.idfundamentocalif, ");
			sql.append(" EJG.IDPROCURADOR, "); 
			sql.append(" EJG.IDINSTITUCION_PROC, "); 
			sql.append(" EJG.JUZGADO AS IDJUZGADO_DJ, ");
			sql.append(" EJG.JUZGADOIDINSTITUCION AS JUZGADOIDINSTITUCION_DJ, ");
			sql.append(" EJG.COMISARIA, ");
			sql.append(" EJG.COMISARIAIDINSTITUCION, "); 
			sql.append(" EJG.ANIO AS ANIO_EJG, ");
			sql.append(" TO_CHAR(EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA_EJG, ");
			sql.append(" EJG.OBSERVACIONES AS OBSERVACIONES, ");
			sql.append(" EJG.OBSERVACIONES AS ASUNTO_EJG, ");
			sql.append(" (SELECT observaciones FROM scs_designa des WHERE des.IDINSTITUCION = EJGD.Idinstitucion AND des.IDTURNO = EJGD.Idturno AND des.ANIO = ejgd.aniodesigna AND des.NUMERO = EJGD.Numerodesigna) as OBSERVACIONES_DESIGNA, ");			
			sql.append(" TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL, "); 
			sql.append(" TO_CHAR(SYSDATE, 'dd/mm/yyyy') AS MESACTUAL, ");
			sql.append(" TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL, ");
			sql.append(" EJG.IDTIPOEJG, ");
			sql.append(" EJG.ANIO, ");
			sql.append(" lpad(EJG.NUMEJG,");
			sql.append(longitudNumEjg);
			sql.append(",0) as NUMERO, ");
			sql.append(" (EJG.ANIO || '/' || lpad(EJG.NUMEJG,");
			sql.append(longitudNumEjg);
			sql.append(",0)) as NUMERO_EJG, ");			
			sql.append(" EJG.IDPERSONA, ");		
			sql.append(" EJG.IDPERSONAJG AS IDSOLICITANTEPRINCIPAL, ");
			sql.append(" EJG.CALIDAD, ");			
			sql.append(" (SELECT Descripcion FROM Scs_Tipoencalidad WHERE Idinstitucion = Ejg.Idinstitucion AND Idtipoencalidad = Ejg.Idtipoencalidad) as CALIDAD_DJ_DESCRIPCION, ");  			
			sql.append(" EJG.OBSERVACIONES AS ASUNTO_DEFENSA_JURIDICA, ");
			sql.append(" EJG.DELITOS AS COM_DEL_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMERO_CAJG AS NUMERO_CAJG_DEFENSA_JURIDICA, "); 
			sql.append(" EJG.ANIOCAJG AS ANIO_CAJG_DEFENSA_JURIDICA, "); 
			sql.append(" EJG.NUMERODILIGENCIA AS NUMDILIGENCIA_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMEROPROCEDIMIENTO AS NUMPROCED_DEFENSA_JURIDICA, ");
			sql.append(" EJG.ANIOPROCEDIMIENTO AS ANIOPROCED_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMEROPROCEDIMIENTO AS NUM_PROCEDIMIENTO_EJG, ");
			sql.append(" Decode(EJG.ANIOPROCEDIMIENTO, null, EJG.NUMEROPROCEDIMIENTO, (EJG.NUMEROPROCEDIMIENTO || '/' || EJG.ANIOPROCEDIMIENTO))  AS NUMANIOPROCED_DEFENSA_JURIDICA, ");				
			sql.append(" EJG.NIG, "); 
			sql.append(" TO_CHAR(EJG.FECHA_DES_PROC,'dd-mm-yyyy') AS  FECHAEJG_PROCURADOR, ");
			sql.append(" EJG.NUMERODESIGNAPROC AS NUMDESIGNA_PROCURADOR, "); 
			sql.append(" EJG.idtipodictamenejg, "); 
			sql.append(" TO_CHAR(EJG.fechadictamen,'dd-mm-yyyy') AS fechadictamen, "); 
			sql.append(" EJG.dictamen, "); 
			sql.append(" TO_CHAR(EJG.fecharesolucioncajg,'dd-mm-yyyy') AS fecharesolucioncajg, "); 
			sql.append(" EJG.idtiporatificacionejg, "); 
			sql.append(" TO_CHAR(EJG.fechanotificacion,'dd-mm-yyyy') AS fechanotificacion, "); 
			sql.append(" EJG.refauto, ");
			sql.append(" EJG.ratificaciondictamen, ");
			sql.append(" TO_CHAR(EJG.fechaauto,'dd-mm-yyyy') AS fechaauto, ");
			sql.append(" EJG.idtiporesolauto, ");
			sql.append(" EJG.idtiposentidoauto, "); 
			sql.append(" (SELECT pon.nombre FROM SCS_PONENTE pon WHERE pon.idPonente = EJG.idPONENTE AND pon.idInstitucion = EJG.IDINSTITUCIONPONENTE) as PONENTE, "); 			
			sql.append(" (SELECT DESCRIPCION FROM SCS_TIPOEJGCOLEGIO TEC WHERE tec.IDINSTITUCION = EJG.IDINSTITUCION AND TEC.IDTIPOEJGCOLEGIO=EJG.IDTIPOEJGCOLEGIO) AS DESCRIPCIONTIPOEJGCOL, ");
			sql.append(" TO_CHAR(EJG.Fecharatificacion, 'dd-mm-yyyy') AS Fecharatificacion, ");			
			sql.append(" TO_CHAR(EJG.FECHAPRESENTACION, 'dd-mm-yyyy') as FECHAPRESENTACION, ");
			sql.append(" TO_CHAR(EJG.FECHALIMITEPRESENTACION, 'dd-mm-yyyy') as FECHALIMITEPRESENTACION, ");
				// Campos necesarios para las comucioncaciones de la comision
				// Nos quedamos con los digitos para saber la cantidad que se reduce
			sql.append(" REGEXP_REPLACE((SELECT F_SIGA_GETRECURSO(r.descripcion, ");
			sql.append(this.usrbean.getLanguage());
			sql.append(") FROM Scs_Tiporesolucion r WHERE r.Idtiporesolucion=ejg.idtiporatificacionejg),'[^[:digit:]]','') as REDUCCION, ");
				// Las fechas en letra
			sql.append(" TO_CHAR(EJG.Fecharatificacion, 'dd/mm/yyyy') AS FECHARATIFICACIONLETRA, "); 
			sql.append(" TO_CHAR(EJG.FECHAPRESENTACION, 'dd/mm/yyyy') AS FECHAPRESENTACIONLETRA, "); 
			sql.append(" TO_CHAR(EJG.FECHALIMITEPRESENTACION,'dd/mm/yyyy') AS FECHALIMITEPRESENTACIONLETRA, ");
			sql.append(" TO_CHAR(EJG.fechaauto,'dd/mm/yyyy') AS FECHAAUTO_LETRA, ");
			sql.append(" TO_CHAR(EJG.fechanotificacion,'dd/mm/yyyy') AS FECHANOTIFICACIONLETRA, ");
			sql.append(" TO_CHAR(EJG.fecharesolucioncajg,'dd/mm/yyyy') AS FECHARESOLUCIONCAJGLETRA, ");
			sql.append(" TO_CHAR(EJG.FECHAAPERTURA,'dd/mm/yyyy') AS FECHAAPERTURA_EJGLETRA, "); 
			sql.append(" TO_CHAR(SYSDATE,'dd/mm/yyyy') AS FECHAACTUALLETRA, "); 
			sql.append(" FUND.TEXTOPLANTILLA, ");
			sql.append(" FUND.TEXTOPLANTILLA2, ");
			sql.append(" FUND.TEXTOPLANTILLA3, ");
			sql.append(" FUND.TEXTOPLANTILLA4, ");
			sql.append(" FUND.TEXTOPLANTILLA4, ");
			sql.append(" FUND.DESCRIPCION AS FUNDAMENTO_JURIDICO_DESCR, ");
			sql.append(" d.ESTADO, ");
			
			sql.append(" (SELECT F_SIGA_GETRECURSO(DESCRIPCION, ");
			sql.append(this.usrbean.getLanguage());
			sql.append(") FROM SCS_SITUACION WHERE IDSITUACION = EJG.IDSITUACION) AS SITUACIONPROCEDIMIENTO_DJ ");
			
			
			sql.append(" ,DECODE(NVL(D.ESTADO,'A'),'V',1,'F',2,'A',3) ORDEN ");
			
			sql.append(" FROM SCS_EJG EJG, ");
			sql.append(" SCS_EJGDESIGNA EJGD, ");
			sql.append(" SCS_TIPOFUNDAMENTOS FUND,");
			sql.append(" scs_designa d ");
			
			sql.append(" WHERE EJG.IDINSTITUCION = EJGD.IDINSTITUCION(+) ");
			sql.append(" AND EJG.IDTIPOEJG = EJGD.IDTIPOEJG(+) "); 
			sql.append(" AND EJG.ANIO = EJGD.ANIOEJG(+) "); 
			sql.append(" AND EJG.NUMERO = EJGD.NUMEROEJG(+) ");
			sql.append(" and  EJGD.IDINSTITUCION = d.idinstitucion(+) ");
			sql.append(" AND  EJGD.Aniodesigna =  d.anio(+) ");
			sql.append(" AND  EJGD.Idturno = d.idturno(+) ");
			sql.append(" AND  EJGD.NUMERODESIGNA = d.numero(+) ");
			//	sql.append(" AND (d.idinstitucion is null or d.ESTADO <> 'A' ) ");
			sql.append(" AND fund.idfundamento(+) = ejg.idfundamentojuridico "); 
			sql.append(" AND fund.idinstitucion(+) = ejg.idinstitucion "); 
			sql.append(" AND EJG.idinstitucion = ");
			sql.append(idInstitucion);
			sql.append(" AND EJG.idtipoejg = ");
			sql.append(tipoEjg);
			sql.append(" AND EJG.anio = ");
			sql.append(anio);
			sql.append(" AND EJG.numero = ");
			sql.append(numero);
			
			sql.append("	ORDER BY ORDEN,EJGD.FECHAMODIFICACION DESC");
			
			try {    	   	    	   			
				rc = this.find(sql.toString());
				if (rc!=null){
					GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
					CenInstitucionAdm cenInstitucionAdm = new CenInstitucionAdm (usrbean);
					int numeroRegistros = rc.size();
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable)fila.getRow(); 
				 
						if (registro != null){
							//Lo a�adimos.
							// Si solo hay un registro--> Siempre
							// Si hay mas de un registro -->Cuando no este anulada la designacion
							if(numeroRegistros==1 || ( registro.get("ESTADO")!=null && !((String)registro.get("ESTADO")).equals("A"))) {
								String prefijoExpedienteCajg =  paramAdm.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
								registro.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucion);
								Vector institucionVector =  cenInstitucionAdm.selectByPK(registro);
								String abreviaturaColegio =  ((CenInstitucionBean)institucionVector.get(0)).getAbreviatura();
								registro.put("PREFIJO_EXPEDIENTES_CAJG", prefijoExpedienteCajg);
								registro.put("ABREVIATURA_COLEGIO", UtilidadesString.getPrimeraMayuscula(abreviaturaColegio));
			
								datos.add(registro);
							}
						}
					}
				}
				
			} catch (Exception e) {
				throw new ClsExceptions (e, "Error ScsEJGAdm.getEjgSalida.");	
			} 
			
			return datos;			
	}			
	
	private Vector getAsistenciaEjgSalida (String idInstitucion, String tipoEjg, String anio, String numero) throws ClsExceptions {
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT A.*,FECHA_ASISTENCIA_LETRA AS FECHA_ASISTENCIALETRA,ROWNUM FROM ( ");
			sql.append(" (SELECT  ");
			 
			sql.append(" TO_CHAR(ASI.FECHAHORA,'DD/MM/YYYY') FECHA_ASISTENCIA, ");
			sql.append(" TO_CHAR(ASI.FECHASOLICITUD, 'DD/MM/YYYY hh:mm') FECHA_SOLICITUD, ");
			sql.append(" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(ASI.FECHAHORA ,'m',1) FECHA_ASISTENCIA_LETRA, ");
			sql.append(" DECODE(ASI.NUMERODILIGENCIA, NULL, ASI.NUMEROPROCEDIMIENTO, ASI.NUMERODILIGENCIA) AS ASUNTODILIGENCIA, ");
			sql.append(" DECODE(ASI.COMISARIA, NULL, "+
					" (SELECT J.NOMBRE FROM SCS_JUZGADO J WHERE J.IDINSTITUCION = ASI.JUZGADOIDINSTITUCION AND J.IDJUZGADO = ASI.JUZGADO), "+
					" (SELECT C.NOMBRE FROM SCS_COMISARIA C WHERE C.IDINSTITUCION = ASI.COMISARIAIDINSTITUCION AND C.IDCOMISARIA = ASI.COMISARIA)) AS COMISARIAJUZGADO, ");
			sql.append(" GT.NOMBRE NOMBRE_GUARDIA_ASISTENCIA, ");
			sql.append(" PER.nombre || ' ' || PER.apellidos1 || ' ' || PER.apellidos2 AS NOMBRE_LETRADO_ASISTENCIA, ");
			sql.append(" DECODE(COL.COMUNITARIO,'1',COL.NCOMUNITARIO, COL.NCOLEGIADO) NCOLEGIADO_LETRADO_ASISTENCIA, ");
			sql.append(" ASI.IDPERSONACOLEGIADO AS IDPERSONA_LET_ASIST, ");
			sql.append(" ASI.IDINSTITUCION AS IDINSTITUCION_LET_ASIST");

			sql.append(" FROM SCS_ASISTENCIA ASI, SCS_GUARDIASTURNO GT,CEN_COLEGIADO COL, CEN_PERSONA PER ");
			sql.append(" WHERE  ");
			sql.append(" ASI.IDINSTITUCION = GT.IDINSTITUCION ");
			sql.append(" AND ASI.IDTURNO = GT.IDTURNO ");
			sql.append(" AND ASI.IDGUARDIA = GT.IDGUARDIA ");
			sql.append(" AND ASI.IDPERSONACOLEGIADO = COL.IDPERSONA ");
			sql.append(" AND ASI.IDINSTITUCION = COL.IDINSTITUCION ");
			sql.append(" AND ASI.IDPERSONACOLEGIADO = PER.IDPERSONA ");
			sql.append(" AND ASI.IDINSTITUCION = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(keyContador);
			sql.append(" AND ASI.EJGIDTIPOEJG = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(keyContador);
			sql.append(" AND ASI.EJGANIO = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(keyContador);
			sql.append(" AND ASI.EJGNUMERO =  :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(keyContador);
			sql.append(" ORDER BY ASI.FECHAHORA DESC ) A ");
			sql.append(" ) ");
	   
	   
			sql.append(" WHERE ROWNUM = 1 ");
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getAsistenciaEjgSalida.");
		}
	}
	
	
	private Vector getProcuradorEjgSalida (String idInstitucion, String tipoEjg, String anio, String numero, String anioDesigna, String turnoDesigna, String numeroDesigna) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT  ");
			sql.append(" PROC.NOMBRE || ' ' || PROC.APELLIDOS1 || ' ' || PROC.APELLIDOS2 AS PROCURADOR, ");
			sql.append(" PROC.Domicilio AS PROCURADOR_DOMICILIO, ");
			sql.append(" PROC.Codigopostal AS PROCURADOR_CP, ");
			sql.append(" PROC.Provincia AS PROCURADOR_PROVINCIA, ");
			sql.append(" PROC.Poblacion AS PROCURADOR_POBLACION, ");
			sql.append(" PROC.Idpretencion AS IDPRETENCION, ");
			sql.append(" PROC.TELEFONO1 AS PROCURADOR_TELEFONO1, ");
			sql.append(" COLPROCURADORES_NOMBRE, ");
			sql.append(" COLPROCURADORES_DIRECCION, ");
			sql.append(" COLPROCURADORES_TIPOVIA, ");
			sql.append(" COLPROCURADORES_NUMERODIR, ");
			sql.append(" COLPROCURADORES_ESCALERADIR, ");
			
			sql.append(" COLPROCURADORES_PISODIR, ");
			sql.append(" COLPROCURADORES_PUERTADIR, ");
			sql.append(" COLPROCURADORES_CODPOSTAL, ");
			sql.append(" COLPROCURADORES_POBLACION, ");
			sql.append(" COLPROCURADORES_PROVINCIA ");
			
			sql.append(" FROM V_SIGA_PROCURADOR_EJG PROC ");					
			sql.append(" WHERE "); 	
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" PROC.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and PROC.IDTIPOEJG = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(" and PROC.ANIO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(" and PROC.NUMERO = :");
			sql.append(keyContador);
			
			if (anioDesigna!=null && !anioDesigna.trim().equalsIgnoreCase("") && turnoDesigna!=null && !turnoDesigna.trim().equalsIgnoreCase("") && numeroDesigna!=null && !numeroDesigna.trim().equalsIgnoreCase("")) {
				keyContador++;
				htCodigos.put(new Integer(keyContador), anioDesigna);
				sql.append(" and PROC.ANIODESIGNA = :");
				sql.append(keyContador);
				
				keyContador++;
				htCodigos.put(new Integer(keyContador), turnoDesigna);
				sql.append(" and PROC.IDTURNO = :");
				sql.append(keyContador);
				
				keyContador++;
				htCodigos.put(new Integer(keyContador), numeroDesigna);
				sql.append(" and PROC.NUMERODESIGNA = :");
				sql.append(keyContador);
			}
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getProcuradorEjgSalida.");
		}
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anio
	 * @param numero
	 * @param idioma
	 * @param idPersonaJG
	 * @param longitudNumEjg
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector<Hashtable<String,Object>> getInteresadosEjgSalida (String idInstitucion, String tipoEjg, String anio, String numero,String idioma, String idPersonaJG, String longitudNumEjg) throws ClsExceptions {
		try {			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT INTERESADO.NOMBRE AS NOMBRE_D, ");
			sql.append(" INTERESADO.APELLIDO1 AS APELLIDO1_D, ");
			sql.append(" INTERESADO.APELLIDO2 AS APELLIDO2_D, ");			
			sql.append(" INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' || INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO, ");
			sql.append(" INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO, ");
			sql.append(" INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO, ");
			sql.append(" INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO, ");
			sql.append(" INTERESADO.TELEFONOLIST AS LISTA_TELEFONOS_INTERESADO, ");
			sql.append(" INTERESADO.NIF AS NIF_DEFENDIDO, ");
			sql.append(" INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO, ");
			sql.append(" INTERESADO.IDLENGUAJE AS LENGUAJE_INTERESADO, ");
			sql.append(" INTERESADO.IDLENGUAJE, ");
			sql.append(" F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE, ");
			sql.append(" ESTADOCIVIL.DESCRIPCION AS ESTADOCIVILDEFENDIDO, ");
			sql.append(" PROFESION.DESCRIPCION AS PROFESIONDEFENDIDO, ");
			sql.append(" PERSONAJG.IDPERSONA, ");
            sql.append(" PERSONAJG.IDREPRESENTANTEJG, ");
            sql.append(" PERSONAJG.IDINSTITUCION, ");
            sql.append(" NVL(PERSONAJG.NOMBRE, '') AS NOMBRE_PJG, ");
            sql.append(" NVL(PERSONAJG.APELLIDO1, '') AS APELLIDO1_PJG, ");
            sql.append(" NVL(PERSONAJG.APELLIDO2, '') AS APELLIDO2_PJG, ");
            sql.append(" TO_CHAR(PERSONAJG.FECHANACIMIENTO, 'DD/MM/YYYY') AS FECHANAC_DEFENDIDO, ");
            sql.append(" TRUNC(MONTHS_BETWEEN(SYSDATE, PERSONAJG.FECHANACIMIENTO) / 12) as EDAD_DEFENDIDO, ");
            sql.append(" PERSONAJG.Fax AS FAX_PJG, ");
            sql.append(" PERSONAJG.CORREOELECTRONICO AS CORREOELECTRONICO_PJG, ");
			
			sql.append(" NVL2(VIA.DESCRIPCION, F_SIGA_GETRECURSO(VIA.DESCRIPCION, ");
			sql.append(idioma);
			sql.append("), '') || ");
			sql.append(" NVL2(PERSONAJG.DIRECCION, ' ' || PERSONAJG.DIRECCION, '') || ");
			sql.append(" NVL2(PERSONAJG.NUMERODIR, ' ' || PERSONAJG.NUMERODIR, '') || ");
			sql.append(" NVL2(PERSONAJG.ESCALERADIR, ' ' || PERSONAJG.ESCALERADIR, '') || ");
			sql.append(" NVL2(PERSONAJG.PISODIR, ' ' || PERSONAJG.PISODIR, '') || ");
			sql.append(" NVL2(PERSONAJG.PUERTADIR, ' ' || PERSONAJG.PUERTADIR, '') AS DOMICILIO_DEFENDIDO, ");
			
			sql.append(" (SELECT tg.descripcion FROM Scs_Tipogrupolaboral tg  WHERE tg.idinstitucion = ");
			sql.append(idInstitucion);
			sql.append(" AND tg.idtipogrupolab = Interesado.IDTIPOGRUPOLAB) AS GRUPOLABORAL_DEFENDIDO, ");
			
			sql.append(" DECODE(INTERESADO.SEXO, null, null, 'M', 'gratuita.personaEJG.sexo.mujer', 'gratuita.personaEJG.sexo.hombre') AS SEXOINTERESADO, ");			
			sql.append(" DECODE(INTERESADO.SEXO, 'H', 'o', 'a') AS O_A_INTERESADO, ");			
			sql.append(" DECODE(INTERESADO.SEXO, 'H' , 'el', 'la') AS EL_LA_INTERESADO, ");
						
			sql.append(" (SELECT  Descripcion FROM Scs_Tipoencalidad WHERE Idinstitucion = INTERESADO.Idinstitucion AND Idtipoencalidad = INTERESADO.Idtipoencalidad) AS CALIDADINTERESADO, ");  			
			
			sql.append(" DECODE(INTERESADO.ANIOEJG, NULL, NULL, INTERESADO.ANIOEJG || '/' || LPAD(INTERESADO.NUMEJG, ");
			sql.append(longitudNumEjg);
			sql.append(", 0)) AS NUMERO_EJG, ");				
			
			sql.append(" DECODE(regimen_conyugal, 'G', 'gratuita.personaJG.regimen.literal.gananciales', ");
            sql.append(" 'I', 'gratuita.personaJG.regimen.literal.indeterminado', ");
            sql.append(" 'S', 'gratuita.personaJG.regimen.literal.separacion') AS REGIMENCONYUGALDEFENDIDO, ");
            
            sql.append(" DECODE(PERSONAJG.DIRECCION, null, null, 1) AS IDDIRECCION_PJG, ");
            
            sql.append(" (SELECT ST.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA ST WHERE ST.IDINSTITUCION = PERSONAJG.IDINSTITUCION AND ST.IDPERSONA = PERSONAJG.IDPERSONA AND ST.IDTELEFONO = 1) AS TELEFONO1_PJG, ");            
            sql.append(" (SELECT ST.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA ST WHERE ST.IDINSTITUCION = PERSONAJG.IDINSTITUCION AND ST.IDPERSONA = PERSONAJG.IDPERSONA AND ST.IDTELEFONO = 2) AS TELEFONO2_PJG, ");            
            sql.append(" (SELECT MAX(ST.NUMEROTELEFONO) FROM SCS_TELEFONOSPERSONA ST WHERE ST.IDINSTITUCION = PERSONAJG.IDINSTITUCION AND ST.IDPERSONA = PERSONAJG.IDPERSONA AND ST.PREFERENTESMS = 1) AS MOVIL_PJG, ");
            
            sql.append(" (SELECT F_SIGA_GETRECURSO(NOMBRE, ");
			sql.append(this.usrbean.getLanguage());
			sql.append(") FROM CEN_PAIS WHERE IDPAIS = PERSONAJG.IDPAIS) AS NACIONALIDAD_DEFENDIDO, ");
			
			sql.append(" (SELECT F_SIGA_GETRECURSO(DESCRIPCION, ");
			sql.append(this.usrbean.getLanguage());
			sql.append(") FROM CEN_TIPOIDENTIFICACION WHERE IDTIPOIDENTIFICACION = PERSONAJG.IDTIPOIDENTIFICACION) AS TIPOIDENTIFICACION_DEFENDIDO, ");
			
			sql.append(" F_SIGA_GETRECURSO_ETIQUETA(DECODE(PERSONAJG.TIPOPERSONAJG, 'J', 'gratuita.personaJG.literal.tipoJuridica', 'F', 'gratuita.personaJG.literal.tipoFisica','O', 'gratuita.personaJG.literal.otra'), ");
			sql.append(this.usrbean.getLanguage());
			sql.append(") AS TIPOPERSONA_DEFENDIDO "); 
            
			sql.append(" FROM V_SIGA_INTERESADOS_EJG INTERESADO, ");
			sql.append(" SCS_PERSONAJG PERSONAJG, ");
			sql.append(" CEN_ESTADOCIVIL ESTADOCIVIL, ");
			sql.append(" SCS_PROFESION PROFESION, ");
			sql.append(" CEN_TIPOVIA VIA ");
  
			sql.append(" WHERE VIA.IDINSTITUCION(+) = PERSONAJG.IDINSTITUCION ");
			sql.append(" AND VIA.IDTIPOVIA(+) = PERSONAJG.IDTIPOVIA ");
			sql.append(" AND PERSONAJG.IDINSTITUCION = INTERESADO.IDINSTITUCION ");
			sql.append(" AND PERSONAJG.IDPERSONA = INTERESADO.IDPERSONAJG ");	
			sql.append(" AND ESTADOCIVIL.IDESTADOCIVIL(+) = PERSONAJG.IDESTADOCIVIL ");
			sql.append(" AND PERSONAJG.IDPROFESION = PROFESION.IDPROFESION(+) ");
			
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND INTERESADO.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and INTERESADO.IDTIPOEJG = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(" and INTERESADO.ANIO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(" and INTERESADO.NUMERO = :");
			sql.append(keyContador);
			if(idPersonaJG!=null && !idPersonaJG.trim().equalsIgnoreCase("")){
				keyContador++;
				htCodigos.put(new Integer(keyContador), idPersonaJG);
				sql.append(" and INTERESADO.IDPERSONAJG = :");
				sql.append(keyContador);
				
			}
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInteresadosEjgSalida.");
		}
	}
	
	private Vector getDesignaEjgSalida (String idInstitucion, String idTurno,
			String anioDesigna, String numeroDesigna,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			
			sql.append(" SELECT ");
			
			sql.append(" let.Idpersona IDPERSONA_DESIGNA,let.IDINSTITUCION IDINSTITUCION_LETDESIGNA ,let.IDINSTITUCIONORIGEN IDINSTITUCIONORIGEN_LETDESIGNA , ");
			sql.append(" DES.IDINSTITUCION_JUZG IDINSTITUCION_JUZGDESIGNA ,DES.IDJUZGADO IDJUZGADODESIGNA, ");
			sql.append(" DES.NUMPROCEDIMIENTO AS AUTOS, "); 
			sql.append(" TO_CHAR(DES.FECHAJUICIO, 'dd/MM/yyyy') AS FECHA_JUICIO, ");
			sql.append(" TO_CHAR(DES.FECHAJUICIO, 'HH24:MI') AS HORA_JUICIO, ");
			sql.append(" DES.IDPROCEDIMIENTO  AS IDPROCEDIMIENTO,");
		   
			
			sql.append(" DES.ANIO AS ANIO_DESIGNA, DES.CODIGO AS CODIGO, ");
			sql.append(" DES.RESUMENASUNTO AS ASUNTO, ");
			sql.append(" DES.ANIO || '/' || DES.CODIGO AS NOFICIO, ");
		   
		   // --DES.NOMBRE_PROCEDIMIENTO AS PROCEDIMIENTO,
			
			
			sql.append(" TO_CHAR(DES.FECHAENTRADA, 'dd-mm-yyyy') AS FECHA_DESIGNA, ");
			sql.append(" TO_CHAR(DES.FECHAFIN, 'dd-mm-yyyy') AS FECHA_CIERRE, ");
			sql.append(" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(DES.FECHAFIN, 'M', "+idioma+") FECHA_CIERRE_LETRA ");		
		 
			
		   
			sql.append(" FROM SCS_DESIGNA DES, SCS_DESIGNASLETRADO LET ");
			sql.append(" WHERE  ");
			sql.append(" des.IDINSTITUCION = LET.IDINSTITUCION(+) ");
			sql.append(" AND DES.IDTURNO = LET.IDTURNO(+) ");
			sql.append(" AND DES.ANIO = LET.ANIO(+) ");
			sql.append(" AND DES.NUMERO = LET.NUMERO(+) ");
			sql.append(" AND (LET.FECHADESIGNA IS NULL OR ");
			sql.append(" LET.FECHADESIGNA = (SELECT MAX(LET2.FECHADESIGNA) ");
			sql.append(" FROM SCS_DESIGNASLETRADO LET2 ");
			sql.append(" WHERE LET2.IDINSTITUCION = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(keyContador);
			
			sql.append(" AND LET2.IDTURNO =:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTurno);
			sql.append(keyContador);
			sql.append(" AND LET2.ANIO = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioDesigna);
			sql.append(keyContador);
			
			sql.append(" AND LET2.NUMERO = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroDesigna);
			sql.append(keyContador);
			
			sql.append(" AND TRUNC(LET2.FECHADESIGNA) <= TRUNC(SYSDATE))) ");

		
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" and DES.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTurno);
			sql.append(" and DES.IDTURNO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioDesigna);
			sql.append(" and DES.ANIO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroDesigna);
			sql.append(" and DES.NUMERO = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getDesignaEjgSalida.");
		}
	}
	private Vector getLetradoDesignadoEjg (String idInstitucion, String idTurno,
			String anioDesigna, String numeroDesigna) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT ");
			sql.append(" let.Idpersona IDPERSONA_DESIGNA,let.IDINSTITUCION IDINSTITUCION_LETDESIGNA  ");
			sql.append(" ,des.anio||'/'||des.codigo NUMERO_DESIGNA ");
			sql.append(" FROM SCS_DESIGNA DES, SCS_DESIGNASLETRADO LET ");
			sql.append(" WHERE  ");
			sql.append(" des.IDINSTITUCION = LET.IDINSTITUCION(+) ");
			sql.append(" AND DES.IDTURNO = LET.IDTURNO(+) ");
			sql.append(" AND DES.ANIO = LET.ANIO(+) ");
			sql.append(" AND DES.NUMERO = LET.NUMERO(+) ");
			sql.append(" AND (LET.FECHADESIGNA IS NULL OR ");
			sql.append(" LET.FECHADESIGNA = (SELECT MAX(LET2.FECHADESIGNA) ");
			sql.append(" FROM SCS_DESIGNASLETRADO LET2 ");
			sql.append(" WHERE LET2.IDINSTITUCION = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(keyContador);
			
			sql.append(" AND LET2.IDTURNO =:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTurno);
			sql.append(keyContador);
			sql.append(" AND LET2.ANIO = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioDesigna);
			sql.append(keyContador);
			
			sql.append(" AND LET2.NUMERO = :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroDesigna);
			sql.append(keyContador);
			sql.append(" AND TRUNC(LET2.FECHADESIGNA) <= TRUNC(SYSDATE))) ");

			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" and DES.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idTurno);
			sql.append(" and DES.IDTURNO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anioDesigna);
			sql.append(" and DES.ANIO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numeroDesigna);
			sql.append(" and DES.NUMERO = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getLetradoDesignadoEjg.");
		}
	}
	
	
	
	
	public Vector getColegiadoSalida (String idInstitucionDesigna, String idPersonaDesigna,
			String aliasSalida) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT COLDES.NCOLEGIADO AS NCOLEGIADO");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,COLDES.NOMBRE AS NOMBRE_D");
			
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,COLDES.APELLIDOS1 AS APELLIDOS1_D");
			
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,COLDES.APELLIDOS2 AS APELLIDOS2_D");
			
			sql.append("_");
			sql.append(aliasSalida);
			
			sql.append(" ,COLDES.NOMBRE || ' ' || COLDES.APELLIDOS1 || ' ' ||COLDES.APELLIDOS2 AS NOMBRE");
			
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,substr(COLDES.NOMBRE || ' ' || COLDES.APELLIDOS1,1,31)  AS N_APELLI_1");
			
			sql.append("_");
			
			
			sql.append(aliasSalida);
			
			sql.append(" ,substr(COLDES.NOMBRE || ' ' || COLDES.APELLIDOS1|| ' ' || COLDES.APELLIDOS2,1,31)  AS N_APEL_1_2");
			
			sql.append("_");
			
			
			sql.append(aliasSalida);
			
			sql.append(" ,substr(COLDES.APELLIDOS1|| ' ' || COLDES.APELLIDOS2 || ', ' ||COLDES.NOMBRE  ,1,31)  AS APEL_1_2_N");
			
			sql.append("_");
			
			
			sql.append(aliasSalida);
			
			sql.append(" ,DECODE(COLDES.SEXO, null, null,'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_ST");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append("  ,DECODE(COLDES.SEXO,'H','o','a') as O_A");	
			sql.append("  ,DECODE(COLDES.SEXO,'H','el','la') as EL_LA");			
			sql.append(",COLDES.NIFCIF AS NIFCIF");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" FROM V_SIGA_DATOSLETRADO_COLEGIADO COLDES ");
			sql.append(" WHERE "); 
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucionDesigna);
			sql.append(" COLDES.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersonaDesigna);
			sql.append(" and COLDES.IDPERSONA = :");
			sql.append(keyContador);
			
		
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getColegiadoSalida.");
		}
	}
	
	public Vector getDireccionLetradoSalida(String idPersona,String idInstitucion,String aliasSalida)
	throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ");
			sql.append(" DIR.Domicilio DOMICILIO");
			sql.append("_");
			sql.append(aliasSalida);
			
			sql.append(",dir.codigopostal CP");
			sql.append("_");
			sql.append(aliasSalida); 
			sql.append(",dir.poblacionextranjera POBLACION");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.idpoblacion ID_POBLACION");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.idprovincia ID_PROVINCIA");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.telefono1 TELDESPACHO");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.fax1 FAX");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.correoelectronico EMAIL");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.IDDIRECCION IDDIRECCION");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.POBLACIONEXTRANJERA PEXTRANJERA");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.fax2 FAX2");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.PAGINAWEB PAGINAWEB");
			sql.append("_");
			sql.append(aliasSalida);
			
			sql.append(" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP " );
			sql.append(" where dir.idinstitucion = tip.idinstitucion ");
			sql.append(" and dir.idpersona = tip.idpersona  " );
			sql.append(" and dir.iddireccion = tip.iddireccion " );
			sql.append(" and tip.idtipodireccion = 2 " );
			sql.append(" and dir.fechabaja is null ");
			sql.append(" and dir.idinstitucion = :1 ");
			sql.append(" and dir.idpersona = :2 ");
			sql.append(" and rownum = 1 ");

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
	}
	
	public Vector getDireccionLetradoSalidaCorreo(String idPersona,String idInstitucion,String aliasSalida) throws ClsExceptions {
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ");
			sql.append(" DIR.Domicilio DOMICILIO");
			sql.append("_");
			sql.append(aliasSalida);
			
			sql.append(",dir.codigopostal CP");
			sql.append("_");
			sql.append(aliasSalida); 
			sql.append(",dir.poblacionextranjera POBLACION");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.idpoblacion ID_POBLACION");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.idprovincia ID_PROVINCIA");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.telefono1 TELDESPACHO");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.fax1 FAX");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.correoelectronico EMAIL");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.IDDIRECCION IDDIRECCION");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.POBLACIONEXTRANJERA PEXTRANJERA");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.fax2 FAX2");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(",dir.PAGINAWEB PAGINAWEB");
			sql.append("_");
			sql.append(aliasSalida);
			
			
			sql.append(" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP " );
			sql.append(" where dir.idinstitucion = tip.idinstitucion ");
			sql.append(" and dir.idpersona = tip.idpersona  " );
			sql.append(" and dir.iddireccion = tip.iddireccion " );
			sql.append(" and dir.preferente like '%C%' ");
			sql.append(" and tip.idtipodireccion = 2 " );
			sql.append(" and dir.fechabaja is null ");
			sql.append(" and dir.idinstitucion = :1 ");
			sql.append(" and dir.idpersona = :2 ");
			sql.append(" and rownum = 1 ");

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
	}
	
	public Vector getDireccionPersonalLetradoSalida(String idPersona,String idInstitucion,String aliasSalida) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			StringBuffer sql = new StringBuffer("SELECT ");
			sql.append(" dir.telefono1 TELEFONO1");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,dir.telefono2 TELEFONO2");
			sql.append("_");
			sql.append(aliasSalida);
	
			sql.append(" ,dir.movil MOVIL");
			sql.append("_");
			sql.append(aliasSalida);
	
			sql.append(" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP  ");
			sql.append("  where dir.idinstitucion = tip.idinstitucion  ");
			sql.append("  and dir.idpersona = tip.idpersona   ");
			sql.append("  and dir.iddireccion = tip.iddireccion  ");
			sql.append("  and tip.idtipodireccion = 6 "  );
			sql.append("  and dir.fechabaja is null ");
			sql.append("  and dir.idinstitucion = :1 ");
			sql.append("  and dir.idpersona = :2 ");
			sql.append("  and rownum = 1 ");

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las getDireccionPersonalLetrado");
		}
	}
	private Vector getJuzgadoDesignaEjgSalida (String idInstitucionDesigna, String idJuzgadoDesigna
			) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT JUZ.NOMBRE AS JUZGADO, ");
			sql.append(" JUZ.DOMICILIO AS DIR_JUZGADO, ");
			sql.append(" JUZ.CODIGOPOSTAL AS CP_JUZGADO, ");
			sql.append(" JUZ.POBLACION_NOMBRE AS POBLACION_JUZGADO ");
			sql.append("  ,JUZ.FAX1 AS FAX1_JUZGADO" );
			sql.append(" ,JUZ.TELEFONO1 AS TELEFONO1_JUZGADO");
			sql.append(" ,JUZ.EMAIL AS EMAIL_JUZGADO");
			sql.append(" ,JUZ.MOVIL AS MOVIL_JUZGADO");		    			
                                       
			sql.append(" FROM V_SIGA_JUZGADOS JUZ ");
			sql.append(" WHERE "); 
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucionDesigna);
			sql.append(" JUZ.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idJuzgadoDesigna);
			sql.append(" and JUZ.IDJUZGADO = :");
			sql.append(keyContador);
			
		
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getJuzgadoDesignaEjgSalida.");
		}
	}
	
	public Vector getDatosRegionConyuge(String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg,String idioma, String longitudNumEjg) throws ClsExceptions {	 

		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), tipoEjg);
		h.put(new Integer(3), anioEjg);
		h.put(new Integer(4), numeroEjg);
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT decode(PER2.APELLIDO2, null, PER2.APELLIDO1, PER2.APELLIDO1 || ' ' || PER2.APELLIDO2) || ', ' || PER2.NOMBRE as CONYUGE_UF, ");
		sql.append(" PER2.NIF as NIF_CONYUGE_UF, ");
		sql.append(" (select f_siga_getrecurso(descripcion, ");
		sql.append(idioma);
		sql.append(") from scs_parentesco paren where paren.idinstitucion = ufa.idinstitucion and paren.idparentesco = UFA.Idparentesco) as PARENTESCO_UF, ");
		sql.append(" trunc(months_between(sysdate, PER2.FECHANACIMIENTO) / 12) as EDAD_UF, ");
		sql.append(" UFA.IDINSTITUCION, ");
		sql.append(" UFA.IDTIPOEJG, "); 
		sql.append(" UFA.ANIO, ");
		sql.append(" UFA.NUMERO, ");
		sql.append(" UFA.IDPERSONA AS IDPERSONAJG, ");
		sql.append(" EJG3.CALIDAD, ");
		sql.append(" EJG3.IDTIPOENCALIDAD, ");
		sql.append(" EJG3.CALIDADIDINSTITUCION, ");
		sql.append(" decode(UFA.SOLICITANTE, 1, f_siga_getrecurso_etiqueta('gratuita.busquedaSOJ.literal.solicitante', ");
		sql.append(idioma);
		sql.append("), null) AS SOLICITANTE, ");
		sql.append(" PER2.NOMBRE, ");
		sql.append(" PER2.APELLIDO1, ");
		sql.append(" PER2.APELLIDO2, "); 
		sql.append(" PER2.DIRECCION, ");
		sql.append(" PER2.CODIGOPOSTAL, ");
		sql.append(" POB.NOMBRE AS NOMBRE_POB, ");
		sql.append(" PROV.NOMBRE AS NOMBRE_PROV, ");
		sql.append(" PAIS.NOMBRE AS NOMBRE_PAIS, ");
		sql.append(" EJG3.ANIO AS ANIOEJG, ");
		sql.append(" lpad(EJG3.NUMEJG, ");
		sql.append(longitudNumEjg);
		sql.append(",0) as NUMEJG, ");
		sql.append(" PER2.SEXO, ");
		sql.append(" PER2.IDLENGUAJE, ");
		sql.append(" (SELECT TEL2.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA TEL2 WHERE TEL2.IDINSTITUCION = UFA.IDINSTITUCION AND TEL2.IDPERSONA = UFA.IDPERSONA AND ROWNUM = 1) AS TELEFONO, ");
		
		sql.append(" (SELECT F_SIGA_GETRECURSO(DESCRIPCION, ");
		sql.append(idioma);
		sql.append(") FROM SCS_PROFESION WHERE IDPROFESION = PER2.IDPROFESION) AS PROFESION_CONYUGE_UF, ");
		
		sql.append(" PER2.OBSERVACIONES");
		
		sql.append(" FROM SCS_UNIDADFAMILIAREJG UFA, ");
		sql.append(" SCS_PERSONAJG PER2, ");
		sql.append(" CEN_POBLACIONES POB, ");
		sql.append(" CEN_PROVINCIAS PROV, ");
		sql.append(" CEN_PAIS PAIS, ");
		sql.append(" SCS_EJG EJG3 ");
		
		sql.append(" WHERE UFA.IDINSTITUCION = PER2.IDINSTITUCION ");
		sql.append(" AND UFA.idpersona <> ejg3.idpersonajg ");
		sql.append(" AND UFA.idparentesco = ");
		sql.append(ClsConstants.TIPO_CONYUGE);
		sql.append(" AND UFA.IDPERSONA = PER2.IDPERSONA");
		sql.append(" AND UFA.IDINSTITUCION = EJG3.IDINSTITUCION ");
		sql.append(" AND UFA.IDTIPOEJG = EJG3.IDTIPOEJG ");
		sql.append(" AND UFA.ANIO = EJG3.ANIO ");
		sql.append(" AND UFA.NUMERO = EJG3.NUMERO ");
		sql.append(" AND PER2.IDPOBLACION = POB.IDPOBLACION(+) ");
		sql.append(" AND PER2.IDPROVINCIA = PROV.IDPROVINCIA(+) ");
		sql.append(" AND PER2.IDPAIS = PAIS.IDPAIS(+) ");
		sql.append(" AND UFA.IDINSTITUCION = :1 ");
		sql.append(" AND UFA.IDTIPOEJG = :2 ");
		sql.append(" AND UFA.ANIO = :3 ");
		sql.append(" AND UFA.NUMERO = :4 ");
		sql.append(" ORDER BY IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO, IDPERSONAJG ");

		try {
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			return helperInformes.ejecutaConsultaBind(sql.toString(), h);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion en getDatosRegionConyuge");
		}
	}
	
		public Vector getDatosRegionUF(String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg,String idioma, String longitudNumEjg) throws ClsExceptions {	 

		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), tipoEjg);
		h.put(new Integer(3), anioEjg);
		h.put(new Integer(4), numeroEjg);
			
		StringBuffer sql = new StringBuffer();		
		sql.append(" SELECT decode(PER2.APELLIDO2, null,PER2.APELLIDO1,PER2.APELLIDO1 || ' ' || PER2.APELLIDO2) || ', ' || ");
		sql.append("    PER2.NOMBRE as NOMBRE_UF, PER2.NIF as NIF_UF, decode(ufa.idparentesco, null, f_siga_getrecurso_etiqueta('informes.sjcs.parentesco.noConsta',"+idioma+"),(select f_siga_getrecurso(descripcion, "+idioma+") from scs_parentesco paren where paren.idinstitucion = ufa.idinstitucion and paren.idparentesco = UFA.Idparentesco)) as PARENTESCO_UF,");
		sql.append("    trunc(months_between(sysdate, PER2.FECHANACIMIENTO) / 12) as EDAD_UF,  UFA.IDINSTITUCION, UFA.IDTIPOEJG, UFA.ANIO,UFA.NUMERO, UFA.IDPERSONA IDPERSONAJG, EJG3.CALIDAD AS CALIDAD, ");
		sql.append("    EJG3.IDTIPOENCALIDAD AS IDTIPOENCALIDAD,EJG3.CALIDADIDINSTITUCION AS CALIDADIDINSTITUCION, ");
		sql.append("    decode(UFA.SOLICITANTE,1, f_siga_getrecurso_etiqueta('gratuita.busquedaSOJ.literal.solicitante',"+idioma+"), null) AS SOLICITANTE, ");
		sql.append("    PER2.NOMBRE, PER2.APELLIDO1,PER2.APELLIDO2, PER2.DIRECCION, PER2.CODIGOPOSTAL, POB.NOMBRE AS NOMBRE_POB,");
		sql.append("    PROV.NOMBRE AS NOMBRE_PROV, PAIS.NOMBRE AS NOMBRE_PAIS, EJG3.ANIO AS ANIOEJG,lpad(EJG3.NUMEJG,"+longitudNumEjg+",0) as NUMEJG, PER2.SEXO, PER2.IDLENGUAJE,");
		sql.append("    (SELECT TEL2.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA TEL2 WHERE TEL2.IDINSTITUCION = UFA.IDINSTITUCION AND TEL2.IDPERSONA = UFA.IDPERSONA AND ROWNUM < 2) AS TELEFONO, PER2.OBSERVACIONES");
		sql.append(" FROM SCS_UNIDADFAMILIAREJG UFA, SCS_PERSONAJG PER2, CEN_POBLACIONES POB, CEN_PROVINCIAS PROV, CEN_PAIS PAIS, SCS_EJG EJG3 ");
		sql.append(" WHERE UFA.IDINSTITUCION = PER2.IDINSTITUCION");
		sql.append("  AND UFA.IDPERSONA <> EJG3.IDPERSONAJG");
		sql.append("  AND (UFA.idparentesco <> "+ClsConstants.TIPO_CONYUGE+" or UFA.idparentesco is null)");
		sql.append("  AND UFA.IDPERSONA = PER2.IDPERSONA");
		sql.append("  AND UFA.IDINSTITUCION = EJG3.IDINSTITUCION");
		sql.append("  AND UFA.IDTIPOEJG = EJG3.IDTIPOEJG");
		sql.append("  AND UFA.ANIO = EJG3.ANIO");
		sql.append("  AND UFA.NUMERO = EJG3.NUMERO");
		sql.append("  AND PER2.IDPOBLACION = POB.IDPOBLACION(+)");
		sql.append("  AND PER2.IDPROVINCIA = PROV.IDPROVINCIA(+)");
		sql.append("  AND PER2.IDPAIS = PAIS.IDPAIS(+) ");
		sql.append("  AND UFA.IDINSTITUCION = :1");
		sql.append("  AND UFA.IDTIPOEJG = :2");
		sql.append("  AND UFA.ANIO = :3");
		sql.append("  AND UFA.NUMERO = :4");
		sql.append(" ORDER BY IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO, IDPERSONAJG ");

		try {
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			return helperInformes.ejecutaConsultaBind(sql.toString(), h);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion en getDatosRegionUF");
		}
	}

	public Vector getDatosInformeEjg (String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg, boolean isSolicitantes,boolean isAcontrarios, String idDestinatario,String tipoDestinatario,boolean generarInformeSinDireccion, String tipoDestinatarioInforme, boolean agregarEtiqDesigna, String longitudNumEjg) throws ClsExceptions, SIGAException {	 
		Vector vSalida = null;		
		Hashtable htFuncion = new Hashtable();
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		ScsUnidadFamiliarEJGAdm admUniFam = new ScsUnidadFamiliarEJGAdm(this.usrbean);
		ScsPersonaJGAdm scsPersonaJGAdm = new ScsPersonaJGAdm(this.usrbean);
		String idPersonaJG = null;
		String idContrario = null;

		boolean isAlgunInformeNoGenerado = false;
		boolean isAlgunRepresentanteLegal =false;
		if (tipoDestinatario!=null) {
			if (tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) {
				idPersonaJG = idDestinatario;
			}else if (tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {
				idContrario = idDestinatario;
			} 
			
			// la siguiente linea es un parche: desde SMSs no viene el tipoDestinatarioInforme
			tipoDestinatarioInforme = (tipoDestinatarioInforme == null ? tipoDestinatario : tipoDestinatarioInforme);	
		}

		try {
			vSalida = new Vector();
			Vector vEjg = this.getEjgSalida(idInstitucion, tipoEjg,anioEjg, numeroEjg, longitudNumEjg); 
			String idiomainforme="ES";
			for (int j = 0; j < vEjg.size(); j++) {
				Hashtable<String,Object> registro = (Hashtable<String,Object>) vEjg.get(j);
				String idioma = "1";
			
				//Ini Mod MJM se incluyen las etiquetas del informe de designas si el
                //EJG tiene designas relacionadas
                //Se agregan las etiquetas del informe de designas al informe de EJG
                //Si se est� invocando al m�todo desde designas o desde EJG env�o telem�tico
                //no hay que sacar estas etiquetas
           
                if ((String)registro.get("DES_NUMERO")!=null && !((String)registro.get("DES_NUMERO")).trim().equalsIgnoreCase("")){

                    if (agregarEtiqDesigna) {
                        ScsDesignaAdm scsDesignaAdm = new ScsDesignaAdm(this.usrbean);
                        boolean agregarEtiqEJG = false;
                       
                        Vector designasRelVector = scsDesignaAdm.getDatosSalidaOficio((String)registro.get("DES_INSTITUCION"),(String)registro.get("DES_IDTURNO"),(String)registro.get("DES_ANIO"),(String)registro.get("DES_NUMERO"),null,isSolicitantes,isAcontrarios,idPersonaJG,this.usrbean.getLanguage(),this.usrbean.getLanguageExt(),tipoDestinatarioInforme, agregarEtiqEJG,longitudNumEjg);
                       
                        Hashtable designasRel = new Hashtable();
                       
                        if (designasRelVector.size()>0) {
                           
                            for (int dv = 0; dv < designasRelVector.size(); dv++) {
                           
                                Hashtable designasRelAux = (Hashtable) designasRelVector.get(dv);
                               
                                Enumeration e = designasRelAux.keys();
                               
                                while (e.hasMoreElements()) {
                                    String keyEjgRel =  (String) e.nextElement();
                                   
                                    String claveNew = "DESIGNARELEJG_"+ keyEjgRel;
                                   
                                    if(designasRelAux.get(keyEjgRel) instanceof String){
                                   
                                        String valor = (String)designasRelAux.get(keyEjgRel);
                                        designasRel.put(claveNew, valor);
       
                                    //Si es un area del informe
                                    } else if(designasRelAux.get(keyEjgRel) instanceof Vector){
                                       
                                        Vector areasRenombrada = new Vector();
                                        Vector areaVector = (Vector) designasRelAux.get(keyEjgRel);
                                       
                                        if (areaVector.size()>0) {
                                           
                                            for (int av = 0; av < areaVector.size(); av++){
                                               
                                                Hashtable area = (Hashtable) areaVector.get(av);
                                                Enumeration e2 = area.keys();
                                                Hashtable areaHashtableRenombrado = new Hashtable();
                                                while (e2.hasMoreElements()){
                                                    Object element2 =  e2.nextElement();
                                                    String claveNewArea = claveNew+"_"+element2;
                                                    String valorArea = (String)area.get((String) element2);
                                                    areaHashtableRenombrado.put(claveNewArea,valorArea);
                                                   
                                                }   
                                                areasRenombrada.add(areaHashtableRenombrado);
                                            }
                                        }
                                        designasRel.put(claveNew, areasRenombrada);
                                    }   
                                }
                            }
                        }
                       
                        if(designasRel!=null)
                            registro.putAll(designasRel);
                    }//Fin agregar etiquetas designa
                }//Fin si el EJG tiene relacionada alguna designa
                //Fin Mod MJM se incluyen las etiquetas del informe de designas

				if (tipoDestinatarioInforme==null || tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)) {
					if (registro.get("DES_ANIO")!=null && !((String)registro.get("DES_ANIO")).equals("")) {
						Vector colegiadoDestinatarios = getLetradoDesignadoEjg((String)registro.get("DES_INSTITUCION"), (String)registro.get("DES_IDTURNO"), (String)registro.get("DES_ANIO"), (String)registro.get("DES_NUMERO"));
						
						if (colegiadoDestinatarios!=null && colegiadoDestinatarios.size()>0) {
							Hashtable colegiadoDestinatario = (Hashtable)colegiadoDestinatarios.get(0);
							registro.putAll(colegiadoDestinatario);
							String idPersonaDesignada = (String)colegiadoDestinatario.get("IDPERSONA_DESIGNA");
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme((String)registro.get("DES_INSTITUCION"), idPersonaDesignada, AdmInformeBean.TIPODESTINATARIO_CENPERSONA,registro, usrbean);
							idioma = (String)registro.get("idioma");
							idiomainforme= (String)registro.get("idiomaExt");

							if (idPersonaDesignada!=null && !idPersonaDesignada.trim().equalsIgnoreCase("")) {
								helperInformes.completarHashSalida(registro,getDatosInformeColegiadoSalida(idInstitucion, idPersonaDesignada,"LETRADO_DESIGNADO"));
								String sexoLetradoEjg  = (String)registro.get("SEXO_ST_LETRADO_DESIGNADO");
								sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);					
								String o_a = (String)registro.get("O_A");
								String el_la = (String)registro.get("EL_LA");
								registro.put("SEXO_LETRADO_DESIGNADO", sexoLetradoEjg);
								registro.put("O_A_LETRADO_DESIGNADO", o_a);
								registro.put("EL_LA_LETRADO_DESIGNADO", el_la);
								Vector dirCorreo = getDireccionLetradoSalidaCorreo(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO");
								if (dirCorreo.size()>0 && ((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")!=null && !((String)((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")).trim().equals("")) {
									helperInformes.completarHashSalida(registro,dirCorreo);
								} else { 
									helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));
								}

								helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));			

								String pobLetradoEjg = (String)registro.get("POBLACION_LETRADO_DESIGNADO");
								if (pobLetradoEjg==null || pobLetradoEjg.trim().equalsIgnoreCase("")) {
									String idPobLetradoEjg = (String)registro.get("ID_POBLACION_LETRADO_DESIGNADO");
									helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetradoEjg,"POBLACION_LETRADO_DESIGNADO"));
									String idProvLetradoEjg = (String)registro.get("ID_PROVINCIA_LETRADO_DESIGNADO");
									if (idProvLetradoEjg!=null && !idProvLetradoEjg.trim().equalsIgnoreCase(""))
										helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetradoEjg,"PROVINCIA_LETRADO_DESIGNADO"));
									else
										UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
								} else {
									UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
								}

								///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
								registro.put("NOMBRE_DEST", (String) registro.get("NOMBRE_D_LETRADO_DESIGNADO"));
								registro.put("NOMBRE_DEST_MAYUS", ((String) registro.get("NOMBRE_D_LETRADO_DESIGNADO")).toUpperCase());

								registro.put("APELLIDO1_DEST", (String) registro.get("APELLIDOS1_D_LETRADO_DESIGNADO"));
								registro.put("APELLIDO1_DEST_MAYUS", ((String) registro.get("APELLIDOS1_D_LETRADO_DESIGNADO")).toUpperCase());

								registro.put("APELLIDO2_DEST", (String) registro.get("APELLIDOS2_D_LETRADO_DESIGNADO"));
								registro.put("APELLIDO2_DEST_MAYUS", ((String) registro.get("APELLIDOS2_D_LETRADO_DESIGNADO")).toUpperCase());
								registro.put("SEXO_DEST", (String) registro.get("SEXO_LETRADO_DESIGNADO"));
								registro.put("O_A_DEST", (String) registro.get("O_A_LETRADO_DESIGNADO"));
								registro.put("EL_LA_DEST", (String) registro.get("EL_LA_LETRADO_DESIGNADO"));

								registro.put("NIFCIF_DEST", (String) registro.get("NIFCIF_LETRADO_DESIGNADO"));
								registro.put("IDPERSONA_DEST", idPersonaDesignada);
								registro.put("IDDIRECCION_DEST", (String) registro.get("IDDIRECCION_LETRADO_DESIGNADO"));

								registro.put("DOMICILIO_DEST", (String) registro.get("DOMICILIO_LETRADO_DESIGNADO"));
								registro.put("CODIGOPOSTAL_DEST", (String) registro.get("CP_LETRADO_DESIGNADO"));
								registro.put("TELEFONO1_DEST", (String) registro.get("TELEFONO1_LETRADO_DESIGNADO"));
								registro.put("TELEFONO2_DEST", (String) registro.get("TELEFONO2_LETRADO_DESIGNADO"));
								registro.put("MOVIL_DEST", (String) registro.get("MOVIL_LETRADO_DESIGNADO"));
								registro.put("FAX1_DEST", (String) registro.get("FAX_LETRADO_DESIGNADO"));
								registro.put("FAX2_DEST", (String) registro.get("FAX2_LETRADO_DESIGNADO"));
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");

								registro.put("CORREOELECTRONICO_DEST", (String) registro.get("EMAIL_LETRADO_DESIGNADO"));
								registro.put("PAGINAWEB_DEST", (String) registro.get("PAGINAWEB_LETRADO_DESIGNADO"));
								registro.put("POBLACIONEXTRANJERA_DEST", (String) registro.get("PEXTRANJERA_LETRADO_DESIGNADO"));
								registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("POBLACION_LETRADO_DESIGNADO"));
								registro.put("NOMBRE_PROVINCIA_DEST", (String) registro.get("PROVINCIA_LETRADO_DESIGNADO"));
								////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							}
						}
						
					} else {
						idioma = this.usrbean.getLanguageInstitucion();
						String idiomaInstitucionExt = "ES";
						registro.put("idioma", idioma);
						
						switch (Integer.parseInt(idioma)) {
							case 1:  idiomaInstitucionExt="ES"; break;
							case 2:  idiomaInstitucionExt="CA"; break;
							case 3:  idiomaInstitucionExt="EU"; break;
							case 4:  idiomaInstitucionExt="GL"; break;	
						}
						registro.put("idiomaExt",idiomaInstitucionExt);
					}
						
					actualizarDatosFundamentoJuridico(idioma, registro);
					//Aniadimos los datos del colegiado tramitador del ejg				
					String idLetradoTramitadorEjg  = (String)registro.get("IDPERSONATRAMITADOR");
					actualizarDatosLetradoTramitador(idLetradoTramitadorEjg,idInstitucion,registro);
					actualizarDatosFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);
					actualizarDatosFechaReunionActa(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);

					Vector<Hashtable<String,Object>> vDefendidos = this.getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,longitudNumEjg);
					Vector<Hashtable<String,Object>> contrariosEjgVector = this.getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);

					if (isSolicitantes || isAcontrarios) {
						if (isSolicitantes) {
							registro.put("NOMBRE_CONTRARIO", "");
							registro.put("DOMICILIO_CONTRARIO", "");
							registro.put("CP_CONTRARIO", "");
							registro.put("POBLACION_CONTRARIO", "");
							registro.put("PROVINCIA_CONTRARIO", "");
							registro.put("O_A_CONTRARIO", "");
							registro.put("NIF_CONTRARIO", "");
							registro.put("TELEFONO1_CONTRARIO", "");
							if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
								registro.put("contrarios", contrariosEjgVector);
							}

							if (vDefendidos!=null && vDefendidos.size()>0) {
								Hashtable<String,Object> hDefendido0 = (Hashtable<String,Object>) vDefendidos.get(0);
								String sIdPersona0 = UtilidadesHash.getString(hDefendido0, "IDPERSONA");
								if (sIdPersona0==null || sIdPersona0.trim().equalsIgnoreCase("")) {
									continue;
								}
								
								for (int k = 0; k < vDefendidos.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable<String,Object> registroDefendido = (Hashtable<String,Object>) vDefendidos.get(k);
									registro.putAll(registroDefendido);
									String Idpersona = UtilidadesHash.getString(registroDefendido, "IDPERSONA"); 

									registroDefendido = this.getregistrodatosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG, registro, agregarEtiqDesigna);

									// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
									try {   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
										if (Idpersona!=null && !Idpersona.trim().equals("")) {								
											Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,Idpersona);
											if (vDestinatario!=null && vDestinatario.size()>0) {
												Hashtable destinatario = (Hashtable) vDestinatario.get(0);
												clone.putAll(destinatario);										
											}
										}

									} catch (Exception e) {
									}																
									
									clone.putAll(registroDefendido);
									
									String idRepresentanteJg = (String) registroDefendido.get("IDREPRESENTANTEJG");
									if (idRepresentanteJg!=null && !idRepresentanteJg.equals("")) {
										Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(idRepresentanteJg, (String)registroDefendido.get("IDINSTITUCION"));
										clone.put("CORREOELECTRONICO_SOLICITANTE", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
									} else {
										clone.put("CORREOELECTRONICO_SOLICITANTE", (String) clone.get("CORREOELECTRONICO_PJG"));
									}										
									
									vSalida.add(clone);
								}  // END FOR

							} else {
								continue;
							}	
						}
						
						if (isAcontrarios) {
							this.actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
							if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
								for (int k = 0; k < contrariosEjgVector.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable<String,Object> registroContrario = (Hashtable<String,Object>) contrariosEjgVector.get(k);
									clone.putAll(registroContrario);
									vSalida.add(clone);
								}  // END FOR

							} else {
								if (isSolicitantes) {
									continue;
								} else {
									registro.put("NOMBRE_CONTRARIO", "");
									registro.put("DOMICILIO_CONTRARIO", "");
									registro.put("CP_CONTRARIO", "");
									registro.put("POBLACION_CONTRARIO", "");
									registro.put("PROVINCIA_CONTRARIO", "");
									registro.put("O_A_CONTRARIO", "");
									registro.put("NIF_CONTRARIO", "");
									registro.put("TELEFONO1_CONTRARIO", "");
									vSalida.add(registro);
								}
							}	
						}

					} else {
						/**Idiomas del Solicitante principal si lo tiene**/
						Hashtable hastejg = new Hashtable();
						hastejg.put(ScsEJGBean.C_IDINSTITUCION, idInstitucion);
						hastejg.put(ScsEJGBean.C_IDTIPOEJG,tipoEjg);
						hastejg.put(ScsEJGBean.C_ANIO,anioEjg);
						hastejg.put(ScsEJGBean.C_NUMERO,numeroEjg);					
						
						this.actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
						
						if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
							registro.put("contrarios", contrariosEjgVector);
						} else {
							registro.put("NOMBRE_CONTRARIO", "");
							registro.put("DOMICILIO_CONTRARIO", "");
							registro.put("CP_CONTRARIO", "");
							registro.put("POBLACION_CONTRARIO", "");
							registro.put("PROVINCIA_CONTRARIO", "");
							registro.put("O_A_CONTRARIO", "");
							registro.put("NIF_CONTRARIO", "");
							registro.put("TELEFONO1_CONTRARIO", "");
						}

						registro.put("PARRAFO_LETRADO_PROCURADOR", "");

						registro.put("CODIGOLENGUAJE", idiomainforme);
//						registro.put("NIF_DEFENDIDO", "");
//						registro.put("NOMBRE_DEFENDIDO", "");
//						registro.put("FECHANAC_DEFENDIDO", "");
//						registro.put("ESTADOCIVIL_DEFENDIDO", "");
//						registro.put("DOMICILIO_DEFENDIDO", "");
//						registro.put("CP_DEFENDIDO", "");
//						registro.put("POBLACION_DEFENDIDO", "");
//						registro.put("TELEFONO1_DEFENDIDO", "");
//						registro.put("PROVINCIA_DEFENDIDO", "");
//						registro.put("SEXO_INTERESADO", "");
//						registro.put("LENGUAJE_INTERESADO", "");
//						registro.put("CALIDAD_INTERESADO", "");
//						registro.put("PROFESION_DEFENDIDO", "");
//						registro.put("REGIMENCONYUGAL_DEFENDIDO", "");

						registro.put("NOMBRE_CONTRARIO", "");
						registro.put("DOMICILIO_CONTRARIO", "");
						registro.put("CP_CONTRARIO", "");
						registro.put("POBLACION_CONTRARIO", "");
						registro.put("PROVINCIA_CONTRARIO", "");
						registro.put("O_A_CONTRARIO", "");
						registro.put("NIF_CONTRARIO", "");
						registro.put("TELEFONO1_CONTRARIO", "");
						if (generarInformeSinDireccion || (registro.get("IDDIRECCION_DEST")!=null && !((String)registro.get("IDDIRECCION_DEST")).equals(""))) {
							vSalida.add(registro);
						} else {
							isAlgunInformeNoGenerado = true;
						}
					}
					
				} else if (tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG) || tipoDestinatarioInforme.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)) { 
					//					Sacamos los interesados el EJG  y los recorremos
					Vector<Hashtable<String,Object>> vDefendidos = this.getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,longitudNumEjg);
					Vector<Hashtable<String,Object>> contrariosEjgVector = this.getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);
					registro.put("NOMBRE_CONTRARIO", "");
					registro.put("DOMICILIO_CONTRARIO", "");
					registro.put("CP_CONTRARIO", "");
					registro.put("POBLACION_CONTRARIO", "");
					registro.put("PROVINCIA_CONTRARIO", "");
					registro.put("O_A_CONTRARIO", "");
					registro.put("NIF_CONTRARIO", "");
					registro.put("TELEFONO1_CONTRARIO", "");

					if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
						registro.put("contrarios", contrariosEjgVector);
					}	

					if (vDefendidos!=null && vDefendidos.size()>0) {
						for (int k = 0; k < vDefendidos.size(); k++) {

							Hashtable clone = (Hashtable) registro.clone();
							Hashtable<String,Object> registroDefendido = (Hashtable<String,Object>) vDefendidos.get(k);
							idPersonaJG = UtilidadesHash.getString(registroDefendido, "IDPERSONA");

							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme(idInstitucion,idPersonaJG, AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG,clone, usrbean);
							idioma = (String)clone.get("idioma");
							idiomainforme= (String)clone.get("idiomaExt");
							Vector<Hashtable<String,Object>> defendidoVector = this.getInteresadosEjgSalida(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG,longitudNumEjg);
							//solo hay uno ya que filtramos por PK
							Hashtable<String,Object> registroDefendidoConDatos = (Hashtable<String,Object>) defendidoVector.get(0);
							registro.putAll(registroDefendido);
							clone.putAll(registroDefendidoConDatos);
							
							registroDefendido = this.getregistrodatosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG, registro, agregarEtiqDesigna);

							// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
							try {   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
								Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
								if (vDestinatario!=null && vDestinatario.size()>0) {
									Hashtable destinatario = (Hashtable) vDestinatario.get(0);
									clone.putAll(destinatario);										
								}

							} catch (Exception e) {
							}

							clone.putAll(registroDefendido);
							
							///////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
							if (registroDefendido.get("IDREPRESENTANTEJG")!=null && !registroDefendido.get("IDREPRESENTANTEJG").equals("")) {
								isAlgunRepresentanteLegal = true;
								Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG((String)clone.get("IDREPRESENTANTEJG"),(String)clone.get("IDINSTITUCION"));
								clone.put("NOMBRE_DEST", (String) representanteLegalDefendido.get("NOMBRE_PJG"));
								clone.put("NOMBRE_DEST_MAYUS", ((String) representanteLegalDefendido.get("NOMBRE_PJG")).toUpperCase());
								clone.put("APELLIDO1_DEST", (String) representanteLegalDefendido.get("APELLIDO1_PJG"));
								clone.put("APELLIDO1_DEST_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO1_PJG")).toUpperCase());
								clone.put("APELLIDO2_DEST", (String) representanteLegalDefendido.get("APELLIDO2_PJG"));
								clone.put("APELLIDO2_DEST_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO2_PJG")).toUpperCase());
								if (representanteLegalDefendido.get("SEXO_PJG")!=null) {
									clone.put("SEXO_DEST", UtilidadesString.getMensajeIdioma(idioma, (String) representanteLegalDefendido.get("SEXO_PJG")) );
								} else {
									clone.put("SEXO_DEST", "");
								}
								
								clone.put("O_A_DEST", (String) representanteLegalDefendido.get("O_A_PJG"));
								clone.put("EL_LA_DEST", (String) representanteLegalDefendido.get("EL_LA_PJG"));
								clone.put("NIFCIF_DEST", (String) representanteLegalDefendido.get("NIF_PJG"));
								clone.put("IDPERSONA_DEST", (String) representanteLegalDefendido.get("IDPERSONA_PJG"));
								clone.put("IDDIRECCION_DEST", (String) representanteLegalDefendido.get("IDDIRECCION_PJG"));
								clone.put("DOMICILIO_DEST", (String) representanteLegalDefendido.get("DOMICILIO_PJG"));
								clone.put("CODIGOPOSTAL_DEST", (String) representanteLegalDefendido.get("CP_PJG"));
								clone.put("TELEFONO1_DEST", (String) representanteLegalDefendido.get("TELEFONO1_PJG"));
								clone.put("TELEFONO2_DEST", (String) representanteLegalDefendido.get("TELEFONO2_PJG"));
								clone.put("MOVIL_DEST", (String) representanteLegalDefendido.get("MOVIL_PJG"));
								clone.put("FAX1_DEST", (String) representanteLegalDefendido.get("FAX_PJG"));
								registro.put("FAX2_DEST", "");
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");
							
								clone.put("CORREOELECTRONICO_DEST", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
								clone.put("CORREOELECTRONICO_SOLICITANTE", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
							
								clone.put("NOMBRE_POBLACION_DEST", (String) representanteLegalDefendido.get("POBLACION_PJG"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) representanteLegalDefendido.get("PROVINCIA_PJG"));

							} else {
								clone.put("NOMBRE_DEST", (String) clone.get("NOMBRE_PJG"));
								clone.put("NOMBRE_DEST_MAYUS", ((String) clone.get("NOMBRE_PJG")).toUpperCase());
								clone.put("APELLIDO1_DEST", (String) clone.get("APELLIDO1_PJG"));
								clone.put("APELLIDO1_DEST_MAYUS", ((String) clone.get("APELLIDO1_PJG")).toUpperCase());
								clone.put("APELLIDO2_DEST", (String) clone.get("APELLIDO2_PJG"));
								clone.put("APELLIDO2_DEST_MAYUS", ((String) clone.get("APELLIDO2_PJG")).toUpperCase());
								
								if (clone.get("SEXOINTERESADO")!=null) {
									clone.put("SEXO_DEST", UtilidadesString.getMensajeIdioma(idioma, (String) clone.get("SEXOINTERESADO")) );
								} else {
									clone.put("SEXO_DEST", "");
								}
								
								clone.put("O_A_DEST", (String) clone.get("O_A_INTERESADO"));
								clone.put("EL_LA_DEST", (String) clone.get("EL_LA_INTERESADO"));
								clone.put("NIFCIF_DEST", (String) clone.get("NIF_DEFENDIDO"));
								clone.put("IDPERSONA_DEST", (String) clone.get("IDPERSONA"));
								clone.put("IDDIRECCION_DEST", (String) clone.get("IDDIRECCION_PJG"));
								clone.put("DOMICILIO_DEST", (String) clone.get("DOMICILIO_DEFENDIDO"));
								clone.put("CODIGOPOSTAL_DEST", (String) clone.get("CP_DEFENDIDO"));
								clone.put("TELEFONO1_DEST", (String) clone.get("TELEFONO1_PJG"));
								clone.put("TELEFONO2_DEST", (String) clone.get("TELEFONO2_PJG"));
								clone.put("MOVIL_DEST", (String) clone.get("MOVIL_PJG"));
								clone.put("FAX1_DEST", (String) clone.get("FAX_PJG"));
								registro.put("FAX2_DEST", "");
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");
								clone.put("CORREOELECTRONICO_DEST", (String) clone.get("CORREOELECTRONICO_PJG"));
								clone.put("CORREOELECTRONICO_SOLICITANTE", (String) clone.get("CORREOELECTRONICO_PJG"));
								//								clone.put("PAGINAWEB_DEST", (String) clone.get("PAGINAWEB"));
								//									clone.put("POBLACIONEXTRANJERA_DEST", (String) clone.get("POBLACIONEXTRANJERA"));
								clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("POBLACION_DEFENDIDO"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) clone.get("PROVINCIA_DEFENDIDO"));
								//								clone.put("NOMBRE_PAIS_DEST", (String) clone.get("NOMBRE_PAIS"));
							}
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							
							if (generarInformeSinDireccion || (clone.get("IDDIRECCION_DEST")!=null && !((String)clone.get("IDDIRECCION_DEST")).equals(""))) {
								actualizarDatosFundamentoJuridico(idioma, clone);
								//Aniadimos los datos del colegiado tramitador del ejg				
								String idLetradoTramitadorEjg  = (String)registro.get("IDPERSONATRAMITADOR");
								actualizarDatosLetradoTramitador(idLetradoTramitadorEjg,idInstitucion,clone);

								actualizarDatosFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, clone);
								actualizarDatosFechaReunionActa(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, clone);

								vSalida.add(clone);
								
							} else {
								isAlgunInformeNoGenerado = true;
							}
						}  // END FOR

					} else {
						continue;
					}	

				} else if (tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSCONTRARIOSJG)) {

					Vector<Hashtable<String,Object>> contrariosEjgVector = this.getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);
					registro.put("PARRAFO_LETRADO_PROCURADOR", "");
					registro.put("CODIGOLENGUAJE", idiomainforme);
//					registro.put("NIF_DEFENDIDO", "");
//					registro.put("NOMBRE_DEFENDIDO", "");
//					registro.put("FECHANAC_DEFENDIDO", "");
//					registro.put("ESTADOCIVIL_DEFENDIDO", "");
//					registro.put("DOMICILIO_DEFENDIDO", "");
//					registro.put("CP_DEFENDIDO", "");
//					registro.put("POBLACION_DEFENDIDO", "");
//					registro.put("TELEFONO1_DEFENDIDO", "");
//					registro.put("PROVINCIA_DEFENDIDO", "");
//					registro.put("SEXO_INTERESADO", "");
//					registro.put("LENGUAJE_INTERESADO", "");
//					registro.put("CALIDAD_INTERESADO", "");
//					registro.put("PROFESION_DEFENDIDO", "");
//					registro.put("REGIMENCONYUGAL_DEFENDIDO", "");

					if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
						for (int k = 0; k < contrariosEjgVector.size(); k++) {
							Hashtable<String,Object> clone = (Hashtable<String,Object>) registro.clone();
							Hashtable<String,Object> registroContrario = (Hashtable<String,Object>) contrariosEjgVector.get(k);
							clone.putAll(registroContrario);
							if (registroContrario.get("IDREPRESENTANTEJG")!=null && !registroContrario.get("IDREPRESENTANTEJG").equals("")) {
								isAlgunRepresentanteLegal = true;
								Hashtable representanteLegalContrario = scsPersonaJGAdm.getDatosPersonaJG((String)registroContrario.get("IDREPRESENTANTEJG"),(String)registroContrario.get("IDINSTITUCION"));
								HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
								helperInformesAdm.setIdiomaInforme(idInstitucion, (String)registroContrario.get("IDREPRESENTANTEJG"), AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG,clone, usrbean);
								idioma = (String)clone.get("idioma");
								idiomainforme= (String)clone.get("idiomaExt");

								clone.put("NOMBRE_DEST", (String) representanteLegalContrario.get("NOMBRE_PJG"));
								clone.put("NOMBRE_DEST_MAYUS", ((String) representanteLegalContrario.get("NOMBRE_PJG")).toUpperCase());
								clone.put("APELLIDO1_DEST", (String) representanteLegalContrario.get("APELLIDO1_PJG"));
								clone.put("APELLIDO1_DEST_MAYUS", ((String) representanteLegalContrario.get("APELLIDO1_PJG")).toUpperCase());
								clone.put("APELLIDO2_DEST", (String) representanteLegalContrario.get("APELLIDO2_PJG"));
								clone.put("APELLIDO2_DEST_MAYUS", ((String) representanteLegalContrario.get("APELLIDO2_PJG")).toUpperCase());
								
								if (representanteLegalContrario.get("SEXO_PJG")!=null) {
									clone.put("SEXO_DEST", UtilidadesString.getMensajeIdioma(idioma, (String) representanteLegalContrario.get("SEXO_PJG")) );
								} else {
									clone.put("SEXO_DEST", "");
								}
								
								clone.put("O_A_DEST", (String) representanteLegalContrario.get("O_A_PJG"));
								clone.put("EL_LA_DEST", (String) representanteLegalContrario.get("EL_LA_PJG"));
								clone.put("NIFCIF_DEST", (String) representanteLegalContrario.get("NIF_PJG"));
								clone.put("IDPERSONA_DEST", (String) representanteLegalContrario.get("IDPERSONA_PJG"));
								clone.put("IDDIRECCION_DEST", (String) representanteLegalContrario.get("IDDIRECCION_PJG"));
								clone.put("DOMICILIO_DEST", (String) representanteLegalContrario.get("DOMICILIO_PJG"));
								clone.put("CODIGOPOSTAL_DEST", (String) representanteLegalContrario.get("CP_PJG"));
								clone.put("TELEFONO1_DEST", (String) representanteLegalContrario.get("TELEFONO1_PJG"));
								clone.put("TELEFONO2_DEST", (String) representanteLegalContrario.get("TELEFONO2_PJG"));
								clone.put("MOVIL_DEST", (String) representanteLegalContrario.get("MOVIL_PJG"));
								clone.put("FAX1_DEST", (String) representanteLegalContrario.get("FAX_PJG"));
								registro.put("FAX2_DEST", "");
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");
								clone.put("CORREOELECTRONICO_DEST", (String) representanteLegalContrario.get("CORREOELECTRONICO_PJG"));
								clone.put("NOMBRE_POBLACION_DEST", (String) representanteLegalContrario.get("POBLACION_PJG"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) representanteLegalContrario.get("PROVINCIA_PJG"));

							} else {
								HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
								helperInformesAdm.setIdiomaInforme(idInstitucion, (String)clone.get("IDPERSONA_PJG"), AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG,clone, usrbean);
								idioma = (String)clone.get("idioma");
								idiomainforme= (String)clone.get("idiomaExt");

								clone.put("NOMBRE_DEST", (String) clone.get("NOMBRE_PJG"));
								clone.put("NOMBRE_DEST_MAYUS", ((String) clone.get("NOMBRE_PJG")).toUpperCase());
								clone.put("APELLIDO1_DEST", (String) clone.get("APELLIDO1_PJG"));
								clone.put("APELLIDO1_DEST_MAYUS", ((String) clone.get("APELLIDO1_PJG")).toUpperCase());
								clone.put("APELLIDO2_DEST", (String) clone.get("APELLIDO2_PJG"));
								clone.put("APELLIDO2_DEST_MAYUS", ((String) clone.get("APELLIDO2_PJG")).toUpperCase());
								
								if (clone.get("SEXO_PJG")!=null) {
									clone.put("SEXO_DEST", UtilidadesString.getMensajeIdioma(idioma, (String) clone.get("SEXO_PJG")) );
								} else {
									clone.put("SEXO_DEST", "");
								}
								
								clone.put("O_A_DEST", (String) clone.get("O_A_PJG"));
								clone.put("EL_LA_DEST", (String) clone.get("EL_LA_PJG"));
								clone.put("NIFCIF_DEST", (String) clone.get("NIF_PJG"));
								clone.put("IDPERSONA_DEST", (String) clone.get("IDPERSONA_PJG"));
								clone.put("IDDIRECCION_DEST", (String) clone.get("IDDIRECCION_PJG"));
								clone.put("DOMICILIO_DEST", (String) clone.get("DOMICILIO_PJG"));
								clone.put("CODIGOPOSTAL_DEST", (String) clone.get("CP_PJG"));
								clone.put("TELEFONO1_DEST", (String) clone.get("TELEFONO1_PJG"));
								clone.put("TELEFONO2_DEST", (String) clone.get("TELEFONO2_PJG"));
								clone.put("MOVIL_DEST", (String) clone.get("MOVIL_PJG"));
								clone.put("FAX1_DEST", (String) clone.get("FAX_PJG"));
								registro.put("FAX2_DEST", "");
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");
								clone.put("CORREOELECTRONICO_DEST", (String) clone.get("CORREOELECTRONICO_PJG"));
								clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("POBLACION_PJG"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) clone.get("PROVINCIA_PJG"));
							}
							
							Vector<Hashtable<String,Object>> vDefendidos = this.getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,longitudNumEjg);
							
							this.actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, clone);
							
							actualizarDatosFundamentoJuridico(idioma, clone);

							//Aniadimos los datos del colegiado tramitador del ejg				
							String idLetradoTramitadorEjg  = (String)registro.get("IDPERSONATRAMITADOR");
							actualizarDatosLetradoTramitador(idLetradoTramitadorEjg,idInstitucion,clone);
							actualizarDatosFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, clone);
							actualizarDatosFechaReunionActa(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, clone);
							
							if (generarInformeSinDireccion || (clone.get("IDDIRECCION_DEST")!=null && !((String)clone.get("IDDIRECCION_DEST")).equals(""))) {
								vSalida.add(clone);
							} else {
								isAlgunInformeNoGenerado = true;
							}
						}  // END FOR

					} else {
						continue;
					}	

					//					Sacamos los CONTRARIOS y los recorremos
				}else if (tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO) || tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)) {
					//					Sacamos el Juzgado

					HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
					helperInformesAdm.setIdiomaInforme(idInstitucion, null, AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO,registro, usrbean);
					idioma = (String)registro.get("idioma");
					idiomainforme= (String)registro.get("idiomaExt");

					if (registro.get("DES_ANIO")!=null && !((String)registro.get("DES_ANIO")).equals("")) {
						Vector colegiadoDestinatarios = getLetradoDesignadoEjg((String)registro.get("DES_INSTITUCION"), (String)registro.get("DES_IDTURNO"), (String)registro.get("DES_ANIO"), (String)registro.get("DES_NUMERO"));
						if (colegiadoDestinatarios!=null && colegiadoDestinatarios.size()>0) {
							Hashtable colegiadoDestinatario = (Hashtable)colegiadoDestinatarios.get(0);
							registro.putAll(colegiadoDestinatario);
							String idPersonaDesignada = (String)colegiadoDestinatario.get("IDPERSONA_DESIGNA");

							if (idPersonaDesignada!=null && !idPersonaDesignada.trim().equalsIgnoreCase("")) {
								helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucion, idPersonaDesignada,"LETRADO_DESIGNADO"));
								String sexoLetradoEjg  = (String)registro.get("SEXO_ST_LETRADO_DESIGNADO");
								sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);					
								String o_a = (String)registro.get("O_A");
								String el_la = (String)registro.get("EL_LA");
								registro.put("SEXO_LETRADO_DESIGNADO", sexoLetradoEjg);
								registro.put("O_A_LETRADO_DESIGNADO", o_a);
								registro.put("EL_LA_LETRADO_DESIGNADO", el_la);
								Vector dirCorreo = getDireccionLetradoSalidaCorreo(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO");
								if (dirCorreo.size()>0 && ((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")!=null && !((String)((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")).trim().equals("")) {
									helperInformes.completarHashSalida(registro,dirCorreo);
								} else {
									helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));
								}

								helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));			

								String pobLetradoEjg = (String)registro.get("POBLACION_LETRADO_DESIGNADO");
								if (pobLetradoEjg==null || pobLetradoEjg.trim().equalsIgnoreCase("")) {
									String idPobLetradoEjg = (String)registro.get("ID_POBLACION_LETRADO_DESIGNADO");
									helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetradoEjg,"POBLACION_LETRADO_DESIGNADO"));
									String idProvLetradoEjg = (String)registro.get("ID_PROVINCIA_LETRADO_DESIGNADO");
									if (idProvLetradoEjg!=null && !idProvLetradoEjg.trim().equalsIgnoreCase("")) {
										helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetradoEjg,"PROVINCIA_LETRADO_DESIGNADO"));
									} else {
										UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
									}
								} else {
									UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
								}
							}
						}
					}
				
					actualizarDatosFundamentoJuridico(idioma, registro);
					//Aniadimos los datos del colegiado tramitador del ejg				
					String idLetradoTramitadorEjg  = (String)registro.get("IDPERSONATRAMITADOR");
					actualizarDatosLetradoTramitador(idLetradoTramitadorEjg,idInstitucion,registro);
					actualizarDatosFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);
					actualizarDatosFechaReunionActa(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);

					Vector<Hashtable<String,Object>> vDefendidos = this.getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,longitudNumEjg);
					Vector<Hashtable<String,Object>> contrariosEjgVector = this.getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);

					if (isSolicitantes || isAcontrarios) {
						if (isSolicitantes) {
							registro.put("NOMBRE_CONTRARIO", "");
							registro.put("DOMICILIO_CONTRARIO", "");
							registro.put("CP_CONTRARIO", "");
							registro.put("POBLACION_CONTRARIO", "");
							registro.put("PROVINCIA_CONTRARIO", "");
							registro.put("O_A_CONTRARIO", "");
							registro.put("NIF_CONTRARIO", "");
							registro.put("TELEFONO1_CONTRARIO", "");
							if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
								registro.put("contrarios", contrariosEjgVector);
							}

							if (vDefendidos!=null && vDefendidos.size()>0) {
								Hashtable<String,Object> hDefendido0 = (Hashtable<String,Object>) vDefendidos.get(0);
								String sIdPersona0 = UtilidadesHash.getString(hDefendido0, "IDPERSONA");
								if (sIdPersona0==null || sIdPersona0.trim().equalsIgnoreCase("")) {
									continue;
								}
								
								for (int k = 0; k < vDefendidos.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable<String,Object> registroDefendido = (Hashtable<String,Object>) vDefendidos.get(k);
									String Idpersona = UtilidadesHash.getString(registroDefendido, "IDPERSONA");
									registro.putAll(registroDefendido);

									registroDefendido = this.getregistrodatosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG, registro, agregarEtiqDesigna);

									// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
									try {   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
										if (Idpersona!=null && !Idpersona.trim().equals("")) {								
											Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,Idpersona);
											if (vDestinatario!=null && vDestinatario.size()>0) {
												Hashtable<String,Object> destinatario = (Hashtable<String,Object>) vDestinatario.get(0);
												clone.putAll(destinatario);										
											}
										}

									} catch (Exception e) {
									}
									
									clone.putAll(registroDefendido);
									if (tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO) ) {
									
										clone.put("NOMBRE_DEST", (String) clone.get("JUZGADO"));
										clone.put("NOMBRE_DEST_MAYUS", ((String) clone.get("JUZGADO")).toUpperCase());
										clone.put("APELLIDO1_DEST", "");
										clone.put("APELLIDO1_DEST_MAYUS","");
										clone.put("APELLIDO2_DEST","");
										clone.put("APELLIDO2_DEST_MAYUS", "");
										
										clone.put("SEXO_DEST","");
										clone.put("O_A_DEST", "");
										clone.put("EL_LA_DEST", "");
										clone.put("NIFCIF_DEST", "");
										clone.put("IDDIRECCION_DEST", "");
										clone.put("DOMICILIO_DEST", (String) clone.get("DIR_JUZGADO"));
										clone.put("CODIGOPOSTAL_DEST", (String) clone.get("CP_JUZGADO"));
										clone.put("TELEFONO1_DEST", (String) clone.get("TELEFONO1_JUZGADO"));
										clone.put("TELEFONO2_DEST", "");
										clone.put("MOVIL_DEST", (String) clone.get("MOVIL_JUZGADO"));
										clone.put("FAX1_DEST", (String) clone.get("FAX1_JUZGADO"));
										clone.put("FAX2_DEST", "");
										clone.put("NOMBRE_PAIS_DEST", "");
										clone.put("TRATAMIENTO_DEST","");
										clone.put("CORREOELECTRONICO_DEST", (String) clone.get("EMAIL_JUZGADO"));
										clone.put("PAGINAWEB_DEST", "");
										clone.put("POBLACIONEXTRANJERA_DEST","");
										clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("POBLACION_JUZGADO"));
										clone.put("NOMBRE_PROVINCIA_DEST", "");
									}else{//Es procurador
										clone.put("NOMBRE_DEST", (String) clone.get("PROCURADOR_DEFENSA_JURIDICA"));
										clone.put("NOMBRE_DEST_MAYUS", ((String) clone.get("PROCURADOR_DEFENSA_JURIDICA")).toUpperCase());
										clone.put("APELLIDO1_DEST", "");
										clone.put("APELLIDO1_DEST_MAYUS","");
										clone.put("APELLIDO2_DEST","");
										clone.put("APELLIDO2_DEST_MAYUS", "");
										
										clone.put("SEXO_DEST","");
										clone.put("O_A_DEST", "");
										clone.put("EL_LA_DEST", "");
										clone.put("NIFCIF_DEST", "");
										clone.put("IDDIRECCION_DEST", "");
										clone.put("DOMICILIO_DEST", (String) clone.get("PROCURADOR_DOMICILIO_D_J"));
										clone.put("CODIGOPOSTAL_DEST", (String) clone.get("PROCURADOR_CODIGOPOSTAL_D_J"));
										clone.put("TELEFONO1_DEST", (String) clone.get("PROCURADOR_DJ_TELEFONO1"));
										clone.put("TELEFONO2_DEST", "");
										clone.put("MOVIL_DEST", "");
										clone.put("FAX1_DEST", "");
										clone.put("FAX2_DEST", "");
										clone.put("NOMBRE_PAIS_DEST", "");
										clone.put("TRATAMIENTO_DEST","");
										clone.put("CORREOELECTRONICO_DEST",""+(clone.get("PROCURADOR_EMAIL")!=null?clone.get("PROCURADOR_EMAIL"):""));
										clone.put("PAGINAWEB_DEST", "");
										clone.put("POBLACIONEXTRANJERA_DEST","");
										clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("PROCURADOR_POBLACION_D_J"));
										clone.put("NOMBRE_PROVINCIA_DEST",(String) clone.get("PROCURADOR_PROVINCIA_D_J") );
										
									}
									String idRepresentanteJg = (String) registroDefendido.get("IDREPRESENTANTEJG");
									if (idRepresentanteJg!=null && !idRepresentanteJg.equals("")) {
										Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(idRepresentanteJg, (String)registroDefendido.get("IDINSTITUCION"));
										clone.put("CORREOELECTRONICO_SOLICITANTE", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
									} else {
										clone.put("CORREOELECTRONICO_SOLICITANTE", (String) clone.get("CORREOELECTRONICO_PJG"));
									}										

									if (generarInformeSinDireccion || (clone.get("DOMICILIO_DEST")!=null && !((String)clone.get("DOMICILIO_DEST")).equals(""))) {
										vSalida.add(clone);
									}
								}  // END FOR
							}
						}
						
						if (isAcontrarios) {
							this.actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
							if(registro.get("JUZGADO")==null)
								registro = this.getregistrodatosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG, registro, false);
							
							
							if (tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO) ) {
								registro.put("NOMBRE_DEST", (String) registro.get("JUZGADO"));
								registro.put("NOMBRE_DEST_MAYUS", ((String) registro.get("JUZGADO")).toUpperCase());
	
								registro.put("APELLIDO1_DEST", "");
								registro.put("APELLIDO1_DEST_MAYUS","");
	
								registro.put("APELLIDO2_DEST","");
								registro.put("APELLIDO2_DEST_MAYUS", "");
	
								registro.put("SEXO_DEST","");
								registro.put("O_A_DEST", "");
								registro.put("EL_LA_DEST", "");
	
								registro.put("NIFCIF_DEST", "");
								registro.put("IDDIRECCION_DEST", "");
	
								registro.put("DOMICILIO_DEST", (String) registro.get("DIR_JUZGADO"));
								registro.put("CODIGOPOSTAL_DEST", (String) registro.get("CP_JUZGADO"));
								registro.put("TELEFONO1_DEST", (String) registro.get("TELEFONO1_JUZGADO"));
								registro.put("TELEFONO2_DEST", "");
								registro.put("MOVIL_DEST", (String) registro.get("MOVIL_JUZGADO"));
								registro.put("FAX1_DEST", (String) registro.get("FAX1_JUZGADO"));
								registro.put("FAX2_DEST", "");
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");
	
								registro.put("CORREOELECTRONICO_DEST", (String) registro.get("EMAIL_JUZGADO"));
								registro.put("PAGINAWEB_DEST", "");
								registro.put("POBLACIONEXTRANJERA_DEST","");
								registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("POBLACION_JUZGADO"));
								registro.put("NOMBRE_PROVINCIA_DEST", "");
							}else{
								registro.put("NOMBRE_DEST", (String) registro.get("PROCURADOR_DEFENSA_JURIDICA"));
								registro.put("NOMBRE_DEST_MAYUS", ((String) registro.get("PROCURADOR_DEFENSA_JURIDICA")).toUpperCase());
								registro.put("APELLIDO1_DEST", "");
								registro.put("APELLIDO1_DEST_MAYUS","");
								registro.put("APELLIDO2_DEST","");
								registro.put("APELLIDO2_DEST_MAYUS", "");
								
								registro.put("SEXO_DEST","");
								registro.put("O_A_DEST", "");
								registro.put("EL_LA_DEST", "");
								registro.put("NIFCIF_DEST", "");
								registro.put("IDDIRECCION_DEST", "");
								registro.put("DOMICILIO_DEST", (String) registro.get("PROCURADOR_DOMICILIO_D_J"));
								registro.put("CODIGOPOSTAL_DEST", (String) registro.get("PROCURADOR_CODIGOPOSTAL_D_J"));
								registro.put("TELEFONO1_DEST", (String) registro.get("PROCURADOR_DJ_TELEFONO1"));
								registro.put("TELEFONO2_DEST", "");
								registro.put("MOVIL_DEST", "");
								registro.put("FAX1_DEST", "");
								registro.put("FAX2_DEST", "");
								registro.put("NOMBRE_PAIS_DEST", "");
								registro.put("TRATAMIENTO_DEST","");
								registro.put("CORREOELECTRONICO_DEST",""+(registro.get("PROCURADOR_EMAIL")!=null?registro.get("PROCURADOR_EMAIL"):""));
								registro.put("PAGINAWEB_DEST", "");
								registro.put("POBLACIONEXTRANJERA_DEST","");
								registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("PROCURADOR_POBLACION_D_J"));
								registro.put("NOMBRE_PROVINCIA_DEST",(String) registro.get("PROCURADOR_PROVINCIA_D_J") );
							}

							if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
								for (int k = 0; k < contrariosEjgVector.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable registroContrario = (Hashtable) contrariosEjgVector.get(k);
									clone.putAll(registroContrario);
									if (generarInformeSinDireccion || (clone.get("DOMICILIO_DEST")!=null && !((String)clone.get("DOMICILIO_DEST")).equals(""))) {
										vSalida.add(clone);
									} else {
										isAlgunInformeNoGenerado = true;
									}
								}  // END FOR

							} else {
								continue;
							}	
						}

					} else {
						this.actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
						if(registro.get("JUZGADO")==null)
							registro = this.getregistrodatosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG, registro, false);
						
						if (contrariosEjgVector!=null && contrariosEjgVector.size()>0) {
							registro.put("contrarios", contrariosEjgVector);
						}	

						registro.put("PARRAFO_LETRADO_PROCURADOR", "");

						registro.put("CODIGOLENGUAJE", idiomainforme);
//						registro.put("NIF_DEFENDIDO", "");
//						registro.put("NOMBRE_DEFENDIDO", "");
//						registro.put("FECHANAC_DEFENDIDO", "");
//						registro.put("ESTADOCIVIL_DEFENDIDO", "");
//						registro.put("DOMICILIO_DEFENDIDO", "");
//						registro.put("CP_DEFENDIDO", "");
//						registro.put("POBLACION_DEFENDIDO", "");
//						registro.put("TELEFONO1_DEFENDIDO", "");
//						registro.put("PROVINCIA_DEFENDIDO", "");
//						registro.put("SEXO_INTERESADO", "");
//						registro.put("LENGUAJE_INTERESADO", "");
//						registro.put("CALIDAD_INTERESADO", "");
//						//registro.put("CODIGOLENGUAJE", "");
//						registro.put("PROFESION_DEFENDIDO", "");
//						registro.put("REGIMENCONYUGAL_DEFENDIDO", "");

						registro.put("NOMBRE_CONTRARIO", "");
						registro.put("DOMICILIO_CONTRARIO", "");
						registro.put("CP_CONTRARIO", "");
						registro.put("POBLACION_CONTRARIO", "");
						registro.put("PROVINCIA_CONTRARIO", "");
						registro.put("O_A_CONTRARIO", "");
						registro.put("NIF_CONTRARIO", "");

						registro.put("TELEFONO1_CONTRARIO", "");
						if (tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO) ) {
							registro.put("NOMBRE_DEST", (String) registro.get("JUZGADO"));
							registro.put("NOMBRE_DEST_MAYUS", ((String) registro.get("JUZGADO")).toUpperCase());
	
							registro.put("APELLIDO1_DEST", "");
							registro.put("APELLIDO1_DEST_MAYUS","");
	
							registro.put("APELLIDO2_DEST","");
							registro.put("APELLIDO2_DEST_MAYUS", "");
	
							registro.put("SEXO_DEST","");
							registro.put("O_A_DEST", "");
							registro.put("EL_LA_DEST", "");
	
							registro.put("NIFCIF_DEST", "");
							registro.put("IDDIRECCION_DEST", "");
	
							registro.put("DOMICILIO_DEST", (String) registro.get("DIR_JUZGADO"));
							registro.put("CODIGOPOSTAL_DEST", (String) registro.get("CP_JUZGADO"));
							registro.put("TELEFONO1_DEST", (String) registro.get("TELEFONO1_JUZGADO"));
							registro.put("TELEFONO2_DEST", "");
							registro.put("MOVIL_DEST", (String) registro.get("MOVIL_JUZGADO"));
							registro.put("FAX1_DEST", (String) registro.get("FAX1_JUZGADO"));
							registro.put("FAX2_DEST", "");
							registro.put("NOMBRE_PAIS_DEST", "");
							registro.put("TRATAMIENTO_DEST","");
	
							registro.put("CORREOELECTRONICO_DEST", (String) registro.get("EMAIL_JUZGADO"));
							registro.put("PAGINAWEB_DEST", "");
							registro.put("POBLACIONEXTRANJERA_DEST","");
							registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("POBLACION_JUZGADO"));
							registro.put("NOMBRE_PROVINCIA_DEST", "");
						}else{
							registro.put("NOMBRE_DEST", (String) registro.get("PROCURADOR_DEFENSA_JURIDICA"));
							registro.put("NOMBRE_DEST_MAYUS", ((String) registro.get("PROCURADOR_DEFENSA_JURIDICA")).toUpperCase());
							registro.put("APELLIDO1_DEST", "");
							registro.put("APELLIDO1_DEST_MAYUS","");
							registro.put("APELLIDO2_DEST","");
							registro.put("APELLIDO2_DEST_MAYUS", "");
							
							registro.put("SEXO_DEST","");
							registro.put("O_A_DEST", "");
							registro.put("EL_LA_DEST", "");
							registro.put("NIFCIF_DEST", "");
							registro.put("IDDIRECCION_DEST", "");
							registro.put("DOMICILIO_DEST", (String) registro.get("PROCURADOR_DOMICILIO_D_J"));
							registro.put("CODIGOPOSTAL_DEST", (String) registro.get("PROCURADOR_CODIGOPOSTAL_D_J"));
							registro.put("TELEFONO1_DEST", (String) registro.get("PROCURADOR_DJ_TELEFONO1"));
							registro.put("TELEFONO2_DEST", "");
							registro.put("MOVIL_DEST", "");
							registro.put("FAX1_DEST", "");
							registro.put("FAX2_DEST", "");
							registro.put("NOMBRE_PAIS_DEST", "");
							registro.put("TRATAMIENTO_DEST","");
							registro.put("CORREOELECTRONICO_DEST",""+(registro.get("PROCURADOR_EMAIL")!=null?registro.get("PROCURADOR_EMAIL"):""));
							registro.put("PAGINAWEB_DEST", "");
							registro.put("POBLACIONEXTRANJERA_DEST","");
							registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("PROCURADOR_POBLACION_D_J"));
							registro.put("NOMBRE_PROVINCIA_DEST",(String) registro.get("PROCURADOR_PROVINCIA_D_J") );
						}

						if (generarInformeSinDireccion || (registro.get("DOMICILIO_DEST")!=null && !((String)registro.get("DOMICILIO_DEST")).equals(""))) {
							vSalida.add(registro);
						} else {
							isAlgunInformeNoGenerado = true;
						}
					}
				} else {
					throw new ClsExceptions ("Error al obtener la informacion en getDatosInformeEjg: no encontramos el tipo destinatario");
				}
			}//fin del for.
			
		} catch (SIGAException se) {
			throw se;	
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosInformeEjg");
		}
		
		if (vSalida!=null && vSalida.size()>0) {
			Hashtable primerRegistro = (Hashtable) vSalida.get(0);
			primerRegistro.put("isAlgunRepresentanteLegal", isAlgunRepresentanteLegal);
			primerRegistro.put("isAlgunInformeNoGenerado", isAlgunInformeNoGenerado);

		}

		return vSalida;
	}
	
	/**
	 * @param idInstitucion
	 * @param idPersonaDesignada
	 * @param string
	 * @return
	 * @throws ClsExceptions 
	 */
	private Vector getDatosInformeColegiadoSalida(String idInstitucion, String idPersonaDesignada, String string) throws ClsExceptions {
		Vector salidaColegiado = null;
		try {
			salidaColegiado = getColegiadoSalida(idInstitucion, idPersonaDesignada, "LETRADO_DESIGNADO");
			if (salidaColegiado == null || salidaColegiado.size() <= 0 || ((Hashtable) salidaColegiado.get(0)) == null || ((String) ((Hashtable) salidaColegiado.get(0)).get("NOMBRE_LETRADO_DESIGNADO")).trim().equals("")) {
				CenPersonaAdm perAdm = new CenPersonaAdm(this.usrbean);
				salidaColegiado = perAdm.getColegiadoSalidaArticulo27(idPersonaDesignada, "LETRADO_DESIGNADO");
			}

		} catch (ClsExceptions e) {
			throw new ClsExceptions(e, "Error ScsEJGAdm.getColegiadoSalida.");
		}

		return salidaColegiado;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anioEjg
	 * @param numeroEjg
	 * @param idPersonaJG
	 * @param vDefendidos
	 * @param idioma
	 * @param idiomaInforme
	 * @param registro
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private void actualizarDefendidos(String idInstitucion,String tipoEjg,String anioEjg,String numeroEjg,String idPersonaJG,Vector vDefendidos, String idioma, String idiomaInforme, Hashtable<String,Object> registro) throws ClsExceptions, SIGAException {

		if (vDefendidos!=null && vDefendidos.size()>0) {
			for (int k = 0; k < vDefendidos.size(); k++) {
				Hashtable<String,Object> registroDefendido = (Hashtable<String,Object>) vDefendidos.get(k);
				if (registroDefendido.get("IDPERSONA")!=null && registro.get("IDSOLICITANTEPRINCIPAL")!=null && ((String)registroDefendido.get("IDPERSONA")).equals((String)registro.get("IDSOLICITANTEPRINCIPAL"))) {
					registro.putAll(registroDefendido);
				}
				
				Hashtable<String,Object> hDefendido0 = (Hashtable<String,Object>) vDefendidos.get(0);
				String sIdPersona0 = UtilidadesHash.getString(hDefendido0, "IDPERSONA");
				if (sIdPersona0==null || sIdPersona0.trim().equalsIgnoreCase("")) {
					continue;
				}
				
				String idPersona=(String)registroDefendido.get("IDPERSONA");						
	
				registroDefendido = this.getregistrodatosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG, registro, false);
				/**Para saaber en que idioma se tiene que imprimer la carta de oficio**/
				registroDefendido.put("CODIGOLENGUAJE", idiomaInforme);
				
				try {   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
					if (idPersona!=null && !idPersona.trim().equals("")) {
						ScsUnidadFamiliarEJGAdm admUniFam = new ScsUnidadFamiliarEJGAdm(this.usrbean);
						Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersona);
						if (vDestinatario!=null && vDestinatario.size()>0){
							Hashtable destinatario = (Hashtable) vDestinatario.get(0);
							registroDefendido.putAll(destinatario);										
						}
					}
					
				} catch (Exception e) {
				}		
				
				if (k == 0) {
					String idRepresentanteJg = (String) registroDefendido.get("IDREPRESENTANTEJG");
					if (idRepresentanteJg!=null && !idRepresentanteJg.equals("")) {
						ScsPersonaJGAdm scsPersonaJGAdm = new ScsPersonaJGAdm(this.usrbean);
						Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(idRepresentanteJg, (String)registroDefendido.get("IDINSTITUCION"));
						registro.put("CORREOELECTRONICO_SOLICITANTE", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
	
					} else {
						if(registroDefendido.get("CORREOELECTRONICO_PJG")!=null)
							registro.put("CORREOELECTRONICO_SOLICITANTE", (String) registroDefendido.get("CORREOELECTRONICO_PJG"));
						else
							registro.put("CORREOELECTRONICO_SOLICITANTE","");
					}
				}
			}
	
			registro.put("defendido", vDefendidos);
			
		} else {
			registro.put("CORREOELECTRONICO_SOLICITANTE", "");
		}
	}
	
	private void actualizarDatosFundamentoJuridico(String idioma, Hashtable registro) throws ClsExceptions{
		
		registro.put("FUNDAMENTO_JURIDICO", "");
		if(registro.get("FUNDAMENTO_JURIDICO_DESCR")!=null && !((String)registro.get("FUNDAMENTO_JURIDICO_DESCR")).equals("")){
			Hashtable htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), (String)registro.get("FUNDAMENTO_JURIDICO_DESCR"));
			htFuncion.put(new Integer(2), idioma);
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "FUNDAMENTO_JURIDICO"));
		}
	}
	
	private void actualizarDatosFechaRemitidoComision(String idInstitucion,String tipoEjg,String anioEjg,String numeroEjg,String idioma, Hashtable registro) throws ClsExceptions{
		// JPT: OBTIENE LA ULTIMA FECHA CON ESTADO 'Remitido Comision': FECHAREMITIDO_COMISION y FECHAREMITIDO_COMISION_LETRA
		Hashtable hFechaRemitidoComision = getFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma);
		registro.put("FECHAREMITIDO_COMISION", "");
		registro.put("FECHAREMITIDO_COMISION_LETRA", "");
		registro.putAll(hFechaRemitidoComision);


		
	}
	private void actualizarDatosFechaReunionActa(String idInstitucion,String tipoEjg,String anioEjg,String numeroEjg,String idioma, Hashtable registro) throws ClsExceptions{
		// JPT: OBTIENE LA FECHA DE REUNION DEL ACTA DEL EJG: FECHAREUNION_ACTA y FECHAREUNION_ACTA_LETRA
		Hashtable hFechaReunion = getFechaReunionActaEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma);
		registro.put("FECHAREUNION_ACTA", "");
		registro.put("FECHAREUNION_ACTA_LETRA", "");
		registro.putAll(hFechaReunion);
		
	}
	
	private void actualizarDatosLetradoTramitador(String idLetradoTramitador,String idInstitucion, Hashtable registro) throws ClsExceptions{
		if(idLetradoTramitador!=null && !idLetradoTramitador.trim().equalsIgnoreCase("")){
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucion, idLetradoTramitador,"LETRADO"));
			String sexoLetradoEjg  = (String)registro.get("SEXO_ST_LETRADO");
			sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);					
			String o_a = (String)registro.get("O_A");
			String el_la = (String)registro.get("EL_LA");
			registro.put("SEXO_LETRADO", sexoLetradoEjg);
			registro.put("O_A_LETRADO", o_a);
			registro.put("EL_LA_LETRADO", el_la);
			Vector dirCorreo = getDireccionLetradoSalidaCorreo(idLetradoTramitador,idInstitucion,"LETRADO");
			if(dirCorreo.size() > 0 && ((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO")!=null &&!((String)((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO")).trim().equals("") ){
				helperInformes.completarHashSalida(registro,dirCorreo);
			}else{
				helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idLetradoTramitador,idInstitucion,"LETRADO"));
			}
			
			helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idLetradoTramitador,idInstitucion,"LETRADO"));			
			String telefonoDespacho = (String)registro.get("TELDESPACHO_LETRADO");
			if(telefonoDespacho!=null)
				UtilidadesHash.set(registro, "TELEFONODESPACHO_LET", telefonoDespacho);
			else
				UtilidadesHash.set(registro, "TELEFONODESPACHO_LET", "");
			
			String pobLetradoEjg = (String)registro.get("POBLACION_LETRADO");
			if(pobLetradoEjg==null ||pobLetradoEjg.trim().equalsIgnoreCase("")){
				String idPobLetradoEjg = (String)registro.get("ID_POBLACION_LETRADO");
				helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetradoEjg,"POBLACION_LETRADO"));
				String idProvLetradoEjg = (String)registro.get("ID_PROVINCIA_LETRADO");
				if(idProvLetradoEjg!=null && !idProvLetradoEjg.trim().equalsIgnoreCase(""))
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetradoEjg,"PROVINCIA_LETRADO"));
				else
					UtilidadesHash.set(registro, "PROVINCIA_LETRADO", "");
			}else{
				UtilidadesHash.set(registro, "PROVINCIA_LETRADO", "");
				
			}
		}else{
			UtilidadesHash.set(registro, "NCOLEGIADO_LETRADO", "");
			UtilidadesHash.set(registro, "NOMBRE_LETRADO", "");
			UtilidadesHash.set(registro, "NIFCIF_LETRADO", "");
			UtilidadesHash.set(registro, "DOMICILIO_LETRADO", "");
			UtilidadesHash.set(registro, "CP_LETRADO", "");
			UtilidadesHash.set(registro, "POBLACION_LETRADO", "");
			UtilidadesHash.set(registro, "PROVINCIA_LETRADO", "");
			UtilidadesHash.set(registro, "TELEFONODESPACHO_LET", "");
			UtilidadesHash.set(registro, "EMAIL_LETRADO", "");
			UtilidadesHash.set(registro, "FAX_LETRADO", "");
			UtilidadesHash.set(registro, "TELEFONO1_LETRADO", "");
			UtilidadesHash.set(registro, "TELEFONO2_LETRADO", "");
			UtilidadesHash.set(registro, "MOVIL_LETRADO", "");
			UtilidadesHash.set(registro, "SEXO_LETRADO", "");
			UtilidadesHash.set(registro, "O_A_LETRADO", "");
			UtilidadesHash.set(registro, "EL_LA_LETRADO", "");
						
		}
	}
	
	public Vector getFisrtAsistencia (Hashtable htCodigos,String salida) throws ClsExceptions  
	{
		try {
			
			String sql = "select TO_CHAR(F_SIGA_GETFIRSTACTDESIGNA(:1, :2, :3, :4 ),:5) AS "+salida+"" +
					"	FROM DUAL";
				
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getFisrtAsistencia.");
		}
	}


	/**
	 * 
	 * @param idInstitucion
	 * @param idComisaria EJG.COMISARIA
	 * @return
	 * @throws ClsExceptions
	 */

	public static Vector getPretension (String idPretension, String idInstitucion,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
	
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select f_siga_getrecurso (DESCRIPCION,"+idioma+") as PRETENSION ");
			sql.append(" from  SCS_PRETENSION PRET");
	
			sql.append(" WHERE ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPretension);
			sql.append(" PRET.IDPRETENSION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append("  and PRET.IDINSTITUCION = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getPretension");
		}
	}
	
	
	public static Vector getPretensiondj (String idInstitucion, String tipoEjg,String anio, String numero, String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT  ");
			sql.append(" f_siga_getrecurso (pret.DESCRIPCION,"+idioma+") as DESCPRETENCION from scs_ejg e, scs_pretension pret ");						
			sql.append(" WHERE "); 	
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" e.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and e.IDTIPOEJG = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(" and e.ANIO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(" and e.NUMERO = :");
			sql.append(keyContador);
			sql.append(" and pret.idinstitucion=e.idinstitucion(+) ");
            sql.append(" and pret.idpretension=e.idpretension(+) ");			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getProcuradorEjgSalida.");
		}
	}
	
	
	public ScsTipoEJGBean getTipoEjg (String idTipoEjg) throws ClsExceptions  
	{
		ScsTipoEJGBean tipoEjg = null;
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idTipoEjg);
			String sql = "SELECT te.CODIGOEXT,te.BLOQUEADO,f_siga_getrecurso(te.DESCRIPCION,1) AS DESCRIPCION "
				+" FROM scs_tipoejg  te"
				+" WHERE " 
				
				+" te.idtipoejg = :1";
			
				
			Vector resultadoObj =  this.selectGenericoBind(sql, h);
			Hashtable registro = (Hashtable) resultadoObj.get(0);
			
			tipoEjg = new ScsTipoEJGBean();
			tipoEjg.setIdTipoEJG(new Integer(idTipoEjg));
			tipoEjg.setDescripcion((String)registro.get("DESCRIPCION"));
			tipoEjg.setCodigoExt((String)registro.get(ScsTipoEJGBean.C_CODIGOEXT));
			tipoEjg.setCodigoExt((String)registro.get(ScsTipoEJGBean.C_BLOQUEADO));
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getTipoEjg.");
		}
		return tipoEjg;
	}
	
	/**
	 * nombre: getDatosProcuradorContrarioDJ
	 * valores: se pasa un hashtable que contiene idinstitucion, idtipoejg,anio,numero
	 * return: retorna un vector, con los datos del procurador contrario de la defensa juridica de un ejg.
	 * fecha: 05/10/2010    
	 * **/
	public  Vector getDatosProcuradorContrarioDJ (Hashtable entrada) throws ClsExceptions 
	{
	
			String idInstitucion= (String)entrada.get("IDINSTITUCION");
			String tipoEjg= (String)entrada.get("IDTIPOEJG");
			String anioEjg= (String)entrada.get("ANIO");
			String numeroEjg= (String)entrada.get("NUMERO");
			 RowsContainer rc = new RowsContainer(); 
			 Vector datos=new Vector();
			
			StringBuffer sql = new StringBuffer();		
			sql.append(" Select  (pro.Nombre || ' ' || pro.Apellidos1 || ' ' || pro.Apellidos2) as PROCURADOR_DJ_CONTRARIO, ");
			sql.append(" pro.domicilio as PROCURADOR_CONTRA_DOMICI_D_J,pro.codigopostal AS PROCURADOR_CONTRA_CP_D_J, ");
			sql.append(" (Select Provincia.Nombre ");
			sql.append(" From Cen_Provincias Provincia ");						
			sql.append(" Where Provincia.Idprovincia = Pro.Idprovincia) AS PROCURADOR_CONTRA_PROVIN_D_J, ");
            sql.append(" (Select Poblacion.Nombre ");
            sql.append(" From Cen_Poblaciones Poblacion, Cen_Provincias Provincia ");
            sql.append(" Where Poblacion.Idprovincia = Provincia.Idprovincia ");
            sql.append(" And Pro.Idprovincia = Poblacion.Idprovincia");
            sql.append(" And Pro.Idpoblacion = Poblacion.Idpoblacion) As PROCURADOR_CONTRA_POBLA_D_J ");
            sql.append(" From Scs_Contrariosejg, Scs_Personajg p, Scs_Procurador Pro ");
            sql.append(" Where Scs_Contrariosejg.Idinstitucion = p.Idinstitucion ");
            sql.append(" And Scs_Contrariosejg.Idpersona = p.Idpersona ");
            sql.append(" And Scs_Contrariosejg.Idinstitucion  = ");
            sql.append( idInstitucion);
            sql.append(" And Scs_Contrariosejg.Idtipoejg = ");
            sql.append(tipoEjg);
            sql.append(" And Scs_Contrariosejg.Anio = ");
            sql.append(anioEjg);
            sql.append(" And Scs_Contrariosejg.Numero = ");
            sql.append(numeroEjg);
            sql.append(" And Pro.Idinstitucion = Scs_Contrariosejg.Idinstitucion_Procu");
            sql.append(" And Pro.Idprocurador = Scs_Contrariosejg.Idprocurador ");
         
       try{    	   
    	   			
			 rc = this.find(sql.toString());
 			if (rc!=null){
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
	       
		}
		catch (Exception e) 
		{
			throw new ClsExceptions (e, "getDatosProcuradorContrarioDJ");	
		} 
		return datos;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param tipoEjg
	 * @param anioEjg
	 * @param numeroEjg
	 * @param idioma
	 * @param idPersonaJG
	 * @param registro
	 * @param agregarEtiqDesigna
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Hashtable<String, Object> getregistrodatosEjg(String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg,String idioma,String idPersonaJG, Hashtable<String, Object> registro, boolean agregarEtiqDesigna) throws ClsExceptions, SIGAException {
		Hashtable vsalida=new Hashtable();		
		Hashtable htFuncion = new Hashtable();
		UsrBean usrBean = new UsrBean();
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		String estadoCivilDefendido="";
		String sexoInteresado="";
		String calidadInteresado="";
		String regimenConyugalInteresado="";
		String profesionDefendido="";
		String descripcionCalidad ="";
		String descripcionTipoEjgcol="";
		String fecharatificacion="";
		String fechaPresentacion="";
		String fechaLimitePresentacion="";
		String fechaAutoLetra="";
		String fechaNotificacion="";
		String fechaResolucionCajg ="";
		String fechaAperturaEjg="";
		String fechaActual="";
		Vector vpretenciones=new Vector();
		Vector vprocuradorEjg=new Vector();
		String idProcurador = "";
		String idInstitucionProc ="";
		Vector vprocuradorDjEjg = new Vector();		
		String idTipoResolAuto ="";
		String idTipoSentidoAuto="";
		String idTipoDictamenEjg ="";
		String tipoGrupoLaboral ="";
		String idTipoRatificacionEjg="";
	
		try {		
			/**Etiquetas de informaci�n de defendidos que dependen del idioma que se le pase **/
			estadoCivilDefendido = (String)registro.get("ESTADOCIVILDEFENDIDO");		
			if (estadoCivilDefendido!=null && !estadoCivilDefendido.trim().equals("")) {
				htFuncion.put(new Integer(1), estadoCivilDefendido);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "ESTADOCIVIL_DEFENDIDO"));
			} else {
				registro.put("ESTADOCIVIL_DEFENDIDO", "");
			}
			
			sexoInteresado = (String)registro.get("SEXOINTERESADO");		
			if (sexoInteresado!=null && !sexoInteresado.trim().equals("")) {
				htFuncion.put(new Integer(1), sexoInteresado);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO_ETIQUETA", "SEXO_INTERESADO"));
			} else {
				registro.put("SEXO_INTERESADO", "");
				registro.put("O_A_INTERESADO", "o");
				registro.put("EL_LA_INTERESADO", "el");
			}
			
			calidadInteresado = (String)registro.get("CALIDADINTERESADO");		
			if (calidadInteresado!=null && !calidadInteresado.trim().equals("")) {
				htFuncion.put(new Integer(1), calidadInteresado);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "CALIDAD_INTERESADO"));
			} else {
				registro.put("CALIDAD_INTERESADO", "");
			}
			
			regimenConyugalInteresado = (String)registro.get("REGIMENCONYUGALDEFENDIDO");		
			if (regimenConyugalInteresado!=null && !regimenConyugalInteresado.trim().equals("")) {
				htFuncion.put(new Integer(1), regimenConyugalInteresado);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO_ETIQUETA", "REGIMEN_CONYUGALDEFENDIDO"));
			} else {
				registro.put("REGIMEN_CONYUGALDEFENDIDO", "");
			}
			
			tipoGrupoLaboral = (String)registro.get("GRUPOLABORAL_DEFENDIDO");		
			if (tipoGrupoLaboral!=null && !tipoGrupoLaboral.trim().equals("")) {
				htFuncion.put(new Integer(1), tipoGrupoLaboral);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "GRUPOLABORAL_DEFENDIDO"));
			} else {
				registro.put("GRUPOLABORAL_DEFENDIDO", "");
			}
			
			 profesionDefendido = (String)registro.get("PROFESIONDEFENDIDO");		
			if (profesionDefendido!=null && !profesionDefendido.trim().equals("")) {
				htFuncion.put(new Integer(1), profesionDefendido);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "PROFESION_DEFENDIDO"));
			} else {
				registro.put("PROFESION_DEFENDIDO", "");
			}						
			/**Fin de Etiquetas de informaci�n de defendidos**/	
						
			/**Calidad defensa juridica en el idioma del letrado cuando no hay interesados o solicitantes**/
			 descripcionCalidad = (String)registro.get("CALIDAD_DJ_DESCRIPCION");		
			if (descripcionCalidad!=null && !descripcionCalidad.trim().equals("")) {
				htFuncion.put(new Integer(1), descripcionCalidad);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "CALIDAD_DEFENSA_JURIDICA"));
			} else {
				registro.put("CALIDAD_DEFENSA_JURIDICA", "");
			}
		
			/**Calidad TIPO_EJG_COLEGIO que depende del idioma **/
			descripcionTipoEjgcol = (String)registro.get("DESCRIPCIONTIPOEJGCOL");	
			if (descripcionTipoEjgcol!=null && !descripcionTipoEjgcol.trim().equals("")) {
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), descripcionTipoEjgcol);
				htFuncion.put(new Integer(2), idioma);							
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "TIPO_EJG_COLEGIO"));
			} else {
					registro.put("TIPO_EJG_COLEGIO", "");
			}
		
			/**Fechas en Letras para que aparezcan en el idioma que se le pasa**/							
			fecharatificacion = (String)registro.get("FECHARATIFICACIONLETRA");
			if (fecharatificacion!=null && !fecharatificacion.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fecharatificacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHARATIFICACION_LETRA"));
			} else {
					registro.put("FECHARATIFICACION_LETRA", "");
			}
	
			fechaPresentacion = (String)registro.get("FECHAPRESENTACIONLETRA");
			if (fechaPresentacion!=null && !fechaPresentacion.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fechaPresentacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAPRESENTACION_LETRA"));
			} else {
					registro.put("FECHAPRESENTACION_LETRA", "");
			}
			
			fechaLimitePresentacion = (String)registro.get("FECHALIMITEPRESENTACIONLETRA");							
				if (fechaLimitePresentacion!=null && !fechaLimitePresentacion.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fechaLimitePresentacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHALIMITEPRESENTACION_LETRA"));
			} else {
					registro.put("FECHALIMITEPRESENTACION_LETRA", "");
			}
				
			fechaAutoLetra = (String)registro.get("FECHAAUTOLETRA");					
			if (fechaAutoLetra!=null && !fechaAutoLetra.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fechaAutoLetra);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAAUTO_LETRA"));
			} else {
					registro.put("FECHAAUTO_LETRA", "");
			}
				
			fechaNotificacion = (String)registro.get("FECHANOTIFICACIONLETRA");							
			if (fechaNotificacion!=null && !fechaNotificacion.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fechaNotificacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHANOTIFICACION_LETRA"));
			} else {
					registro.put("FECHANOTIFICACION_LETRA", "");
			}
				
			fechaResolucionCajg = (String)registro.get("FECHARESOLUCIONCAJGLETRA");							
			if (fechaResolucionCajg!=null && !fechaResolucionCajg.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fechaResolucionCajg);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHARESOLUCIONCAJG_LETRA"));
			} else {
					registro.put("FECHARESOLUCIONCAJG_LETRA", "");
			}
				
			fechaAperturaEjg = (String)registro.get("FECHAAPERTURA_EJGLETRA");					
			if (fechaAperturaEjg!=null && !fechaAperturaEjg.trim().equals("")) {						
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), fechaAperturaEjg);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAAPERTURA_EJG_LETRA"));
				} else {
					registro.put("FECHAAPERTURA_EJG_LETRA", "");
				}
				
			fechaActual = (String)registro.get("FECHAACTUALLETRA");							
			if (fechaActual!=null && !fechaActual.trim().equals("")) {						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaActual);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAACTUAL_LETRA"));
			} else {
					registro.put("FECHAACTUAL_LETRA", "");
			}  
			/**Fin de fechas en letras**/	
			
			/**se muestra el mes actual en letra**/
			htFuncion = new Hashtable();
	 		htFuncion.put(new Integer(1), (String)registro.get("MESACTUAL"));
			htFuncion.put(new Integer(2), "m");
			htFuncion.put(new Integer(3), idioma);
	
	 		helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHAENLETRA", "MES_ACTUAL"));
	
	 		registro.put("MES_ACTUAL", registro.get("MES_ACTUAL").toString().toUpperCase());
	 		registro.put("MES_ACTUAL_MINUS", registro.get("MES_ACTUAL").toString().toLowerCase());
	 					
			//Aniadimos los contrarios de la defensa juridica
			htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), idInstitucion);
			htFuncion.put(new Integer(2), tipoEjg);
			htFuncion.put(new Integer(3), anioEjg);
			htFuncion.put(new Integer(4), numeroEjg);
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETCONTRARIOS_EJG", "CONTRARIOS_DEFENSA_JURIDICA"));
			
			// Obtendo los contrarios del ejg
			Vector<Hashtable<String,Object>> contrariosEjgVector = this.getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,null);		

			String listaNombreContrarios="";	
			String listaDomicilioContrarios="";
			String listaCpContrarios="";
			String listaPoblacionContrarios="";
			String listaProvinciaContrarios="";
			String listaOaContrarios="";
			String listaNifContrarios="";
			String listaTelefono1Contrarios="";
			
			for (int i = 0; i < contrariosEjgVector.size(); i++) {					
				Hashtable<String,Object> registroContrariosEjg = (Hashtable<String,Object>) contrariosEjgVector.get(i);
							    
			    String nombreContrario = (String)registroContrariosEjg.get("NOMBRE_PJG");
			    String domicilioContrario = (String)registroContrariosEjg.get("DOMICILIO_PJG");	
			    String cpContrario = (String)registroContrariosEjg.get("CP_PJG"); 			    
			    String poblacionContrario = (String)registroContrariosEjg.get("POBLACION_PJG");
			    String provinciaContrario = (String)registroContrariosEjg.get("PROVINCIA_PJG");
			    String oaContrario = (String)registroContrariosEjg.get("O_A_PJG");
			    String nifContrario  = (String)registroContrariosEjg.get("NIF_PJG");
			    String telefono1Contrario = (String)registroContrariosEjg.get("TELEFONO1_PJG");
				
				if (nombreContrario != null) {
					if (listaNombreContrarios.equals("")) {
						listaNombreContrarios = nombreContrario;
					} else {
						listaNombreContrarios += "," + nombreContrario;
					}						
				}
				
				if (domicilioContrario!=null) {
					if (listaDomicilioContrarios.equals("")) {
						listaDomicilioContrarios = domicilioContrario;
					} else {
						listaDomicilioContrarios += "," + domicilioContrario;
					}						
				}
				
				if (cpContrario != null) {
					if (listaCpContrarios.equals("")) {
						listaCpContrarios = cpContrario;
					} else {
						listaCpContrarios += "," + cpContrario;
					}						
				}
				
				if (poblacionContrario != null) {
					if (listaPoblacionContrarios.equals("")) {
						listaPoblacionContrarios = poblacionContrario;
					} else {
						listaPoblacionContrarios += "," + poblacionContrario;
					}						
				}
				
				if (provinciaContrario != null) {
					if (listaProvinciaContrarios.equals("")) {
						listaProvinciaContrarios = provinciaContrario;
					} else {
						listaProvinciaContrarios += "," + provinciaContrario;
					}						
				}
				
				if (oaContrario != null) {
					if (listaOaContrarios.equals("")) {
						listaOaContrarios = oaContrario;
					} else {
						listaOaContrarios += "," + oaContrario;
					}						
				}
				
				if (nifContrario != null) {
					if (listaNifContrarios.equals("")) {
						listaNifContrarios = nifContrario;
					} else {
						listaNifContrarios += "," + nifContrario;
					}						
				}
				
				if (telefono1Contrario != null) {
					if (listaTelefono1Contrarios.equals("")) {
						listaTelefono1Contrarios = telefono1Contrario;
					} else {
						listaTelefono1Contrarios += "," + telefono1Contrario;
					}						
				}							
			}//FIN FOR			
			
			UtilidadesHash.set(registro, "NOMBRE_CONTRARIO", listaNombreContrarios);
			UtilidadesHash.set(registro, "DOMICILIO_CONTRARIO", listaDomicilioContrarios);
			UtilidadesHash.set(registro, "CP_CONTRARIO", listaCpContrarios);
			UtilidadesHash.set(registro, "POBLACION_CONTRARIO", listaPoblacionContrarios);
			UtilidadesHash.set(registro, "PROVINCIA_CONTRARIO", listaProvinciaContrarios);
			UtilidadesHash.set(registro, "O_A_CONTRARIO", listaOaContrarios);
			UtilidadesHash.set(registro, "NIF_CONTRARIO", listaNifContrarios);
			UtilidadesHash.set(registro, "TELEFONO1_CONTRARIO", listaTelefono1Contrarios);
				
			//Aniadimos los delitos de la defensa juridica
			htFuncion.put(new Integer(5), idioma);
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETDELITOS_EJG", "DELITOS_DEFENSA_JURIDICA"));
				
			htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), idInstitucion);
			htFuncion.put(new Integer(2), anioEjg);
			htFuncion.put(new Integer(3), numeroEjg);
			htFuncion.put(new Integer(4), tipoEjg);								
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "f_siga_getunidadejg", "TOTAL_SOLICITANTE"));
			
			//Aniadimos los datos del procurador de la designa asociada a un EJG
			helperInformes.completarHashSalida(registro,getAsistenciaEjgSalida(idInstitucion, tipoEjg,anioEjg,numeroEjg));
			
			if (registro.get("ROWNUM").equals("1")) {
				String idPersonaLetradoAsistencia  = (String)registro.get("IDPERSONA_LET_ASIST");
				String idInstitucionLetradoAsistencia  = (String)registro.get("IDINSTITUCION_LET_ASIST");							
				helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idPersonaLetradoAsistencia,idInstitucionLetradoAsistencia,"LET_ASIST"));
				
				String poblacionLetradoAsistencia = (String)registro.get("POBLACION_LET_ASIST");
				if (poblacionLetradoAsistencia==null ||poblacionLetradoAsistencia.trim().equalsIgnoreCase("")){
					
					String idPoblacionLetradoAsistencia = (String)registro.get("ID_POBLACION_LET_ASIST");
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPoblacionLetradoAsistencia,"POBLACION_LET_ASIST"));
					
					String idProvinciaLetradoAsistencia = (String)registro.get("ID_PROVINCIA_LET_ASIST");
					if (idProvinciaLetradoAsistencia!=null && !idProvinciaLetradoAsistencia.trim().equalsIgnoreCase("")) {
						helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvinciaLetradoAsistencia,"PROVINCIA_LET_ASIST"));
					} else {
						UtilidadesHash.set(registro, "PROVINCIA_LET_ASIST", "");
					}
					
				} else {
					UtilidadesHash.set(registro, "PROVINCIA_LET_ASIST", "");
				}
				
			} else {
				UtilidadesHash.set(registro, "DOMICILIO_LET_ASIST", "");
				UtilidadesHash.set(registro, "CP_LET_ASIST", "");
				UtilidadesHash.set(registro, "POBLACION_LET_ASIST", "");
				UtilidadesHash.set(registro, "PROVINCIA_LET_ASIST", "");
			}
				
				/**sacamos el campo pretenciones de Defensa Juridica de  Ejg ***/					
			vpretenciones=getPretensiondj(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma);
	
			for (int l = 0; l < vpretenciones.size(); l++) {
					Hashtable registropretencion = (Hashtable) vpretenciones.get(l);							
					String procedimientodj = (String)registropretencion.get("DESCPRETENCION");
					if(procedimientodj!=null && !procedimientodj.trim().equalsIgnoreCase(""))
						registro.put("PROCEDIMIENTO_D_J", procedimientodj);
					else
						UtilidadesHash.set(registro, "PROCEDIMIENTO_D_J", "");
			}									
				
			// procuradorde la designa relacionada con Ejg
			vprocuradorEjg = getProcuradorEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg, (String)registro.get("DES_ANIO"), (String)registro.get("DES_IDTURNO"), (String)registro.get("DES_NUMERO"));	
			String procurador = "";
			String ProProcurador = "";
			String PobProcurador = "";							
			String codigopostal= "";
			String domicilioProcurador= "";
			String idpretencion= ""	;
			String telefonoProcurador = "";
			for (int l = 0; l < vprocuradorEjg.size(); l++) {
				Hashtable registroprocurador = (Hashtable) vprocuradorEjg.get(l);							
				procurador = (String)registroprocurador.get("PROCURADOR");
				ProProcurador = (String)registroprocurador.get("PROCURADOR_PROVINCIA");
				PobProcurador = (String)registroprocurador.get("PROCURADOR_POBLACION");							
				codigopostal= (String)registroprocurador.get("PROCURADOR_CP");
				domicilioProcurador= (String)registroprocurador.get("PROCURADOR_DOMICILIO");
				idpretencion= (String)registroprocurador.get("IDPRETENCION");
				telefonoProcurador= (String)registroprocurador.get("PROCURADOR_TELEFONO1");
				
				if (procurador!=null && !procurador.trim().equalsIgnoreCase("")) {						
					 registro.put("PROCURADOR",procurador);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR", "");
				}
				
				if (ProProcurador!=null && !ProProcurador.trim().equalsIgnoreCase("")) {
					registro.put("PROCURADOR_PROVINCIA",ProProcurador);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR_PROVINCIA", "");
				}
					
				if (PobProcurador!=null && !PobProcurador.trim().equalsIgnoreCase("")) {
					registro.put("PROCURADOR_POBLACION", PobProcurador);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR_POBLACION", "");
				}
				
				if (codigopostal!=null && !codigopostal.trim().equalsIgnoreCase("")) {
					registro.put("PROCURADOR_CP", codigopostal);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR_CP", "");
				}
					
				if (domicilioProcurador!=null && !domicilioProcurador.trim().equalsIgnoreCase("")) {
					registro.put("PROCURADOR_DOMICILIO", domicilioProcurador);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR_DOMICILIO", "");
				}
				
				if (telefonoProcurador!=null && !telefonoProcurador.trim().equalsIgnoreCase("")) {
					registro.put("PROCURADOR_TELEFONO1", telefonoProcurador);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR_TELEFONO1", "");
				}
				
				UtilidadesHash.set(registro,"PROCURADOR_COL_NOMBRE", registroprocurador.get("COLPROCURADORES_NOMBRE")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_NOMBRE" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_DIRECCION", registroprocurador.get("COLPROCURADORES_DIRECCION")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_DIRECCION" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_TIPOVIA", registroprocurador.get("COLPROCURADORES_TIPOVIA")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_TIPOVIA" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_NUMERODIR", registroprocurador.get("COLPROCURADORES_NUMERODIR")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_ESCALERADIR" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_ESCALERADIR", registroprocurador.get("COLPROCURADORES_ESCALERADIR")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_NUMERODIR" ):"");
				
				UtilidadesHash.set(registro,"PROCURADOR_COL_PISODIR", registroprocurador.get("COLPROCURADORES_PISODIR")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_PISODIR" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_PUERTADIR", registroprocurador.get("COLPROCURADORES_PUERTADIR")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_PUERTADIR" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_CODPOSTAL", registroprocurador.get("COLPROCURADORES_CODPOSTAL")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_CODPOSTAL" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_POBLACION", registroprocurador.get("COLPROCURADORES_POBLACION")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_POBLACION" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_COL_PROVINCIA", registroprocurador.get("COLPROCURADORES_PROVINCIA")!=null?UtilidadesHash.getString(registroprocurador,"COLPROCURADORES_PROVINCIA" ):"");
			}//Fin for procurador de la designa relacionada con Ejg
	
	      	//Aniadimos los datos del procurador del ejg
			idProcurador = (String)registro.get("IDPROCURADOR");
			idInstitucionProc = (String)registro.get("IDINSTITUCION_PROC");
			vprocuradorDjEjg = getDatosProcuradorEjgSalida(idInstitucionProc, idProcurador);
			Hashtable registroprocuradorDJ = new 	Hashtable();
			String procuradordj ="";
			String ncolegiado = "";							
			String Procuradordjtel1 = "";
			String Procuradordjtel2 = "";							
			String domiciliodj= "";
			String provincia= "";
			String poblacion= "";
			String codigopostalprocuradorejg="";
			String email = "";
			for (int l = 0; l < vprocuradorDjEjg.size(); l++) {
				registroprocuradorDJ = (Hashtable) vprocuradorDjEjg.get(l);	
				procuradordj = (String)registroprocuradorDJ.get("PROCURADOR_DEFENSA_JURIDICA");
				ncolegiado = (String)registroprocuradorDJ.get("PROCURADOR_DJ_NCOLEGIADO");							
				Procuradordjtel1 = (String)registroprocuradorDJ.get("PROCURADOR_DJ_TELEFONO1");
				Procuradordjtel2 = (String)registroprocuradorDJ.get("PROCURADOR_DJ_TELEFONO2");							
				domiciliodj= (String)registroprocuradorDJ.get("PROCURADOR_DOMICILIO_D_J");
				provincia= (String)registroprocuradorDJ.get("PROCURADOR_PROVINCIA_D_J");
				poblacion= (String)registroprocuradorDJ.get("PROCURADOR_POBLACION_D_J");
				codigopostalprocuradorejg= (String)registroprocuradorDJ.get("PROCURADOR_CODIGOPOSTAL_D_J");	
				email = (String)registroprocuradorDJ.get("PROCURADOR_EMAIL");
					
				if (procuradordj!=null && !procuradordj.trim().equalsIgnoreCase("")) {						
					registro.put("PROCURADOR_DEFENSA_JURIDICA",procuradordj);
				} else {
					UtilidadesHash.set(registro, "PROCURADOR_DEFENSA_JURIDICA", "");
				}
					
				if (ncolegiado!=null && !ncolegiado.trim().equalsIgnoreCase(""))						
					registro.put("PROCURADOR_DJ_NCOLEGIADO",ncolegiado);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DJ_NCOLEGIADO", "");										
					
				if (Procuradordjtel1!=null && !Procuradordjtel1.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DJ_TELEFONO1",Procuradordjtel1);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DJ_TELEFONO1", "");
					
				if (Procuradordjtel2!=null && !Procuradordjtel2.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DJ_TELEFONO2", Procuradordjtel2);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DJ_TELEFONO2", "");
					
				if (domiciliodj!=null && !domiciliodj.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DOMICILIO_D_J", domiciliodj);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DOMICILIO_D_J", "");
				
				if (codigopostalprocuradorejg!=null && !codigopostalprocuradorejg.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_CODIGOPOSTAL_D_J", codigopostalprocuradorejg);
				else
					UtilidadesHash.set(registro, "PROCURADOR_CODIGOPOSTAL_D_J", "");
					
				if (provincia!=null && !provincia.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_PROVINCIA_D_J", provincia);
				else
					UtilidadesHash.set(registro, "PROCURADOR_PROVINCIA_D_J", "");
				
				if (poblacion!=null && !poblacion.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_POBLACION_D_J", poblacion);
				else
					UtilidadesHash.set(registro, "PROCURADOR_POBLACION_D_J", "");
				
				if (email!=null && !email.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_EMAIL", email);
				else
					UtilidadesHash.set(registro, "PROCURADOR_EMAIL", "");
				
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_NOMBRE", registroprocuradorDJ.get("COLPROCURADORES_NOMBRE")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_NOMBRE" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_DIRECCION", registroprocuradorDJ.get("COLPROCURADORES_DIRECCION")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_DIRECCION" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_TIPOVIA", registroprocuradorDJ.get("COLPROCURADORES_TIPOVIA")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_TIPOVIA" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_NUMERODIR", registroprocuradorDJ.get("COLPROCURADORES_NUMERODIR")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_NUMERODIR" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_ESCALERADIR", registroprocuradorDJ.get("COLPROCURADORES_ESCALERADIR")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_ESCALERADIR" ):"");
				
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_PISODIR", registroprocuradorDJ.get("COLPROCURADORES_PISODIR")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_PISODIR" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_PUERTADIR", registroprocuradorDJ.get("COLPROCURADORES_PUERTADIR")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_PUERTADIR" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_CODPOSTAL", registroprocuradorDJ.get("COLPROCURADORES_CODPOSTAL")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_CODPOSTAL" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_POBLACION", registroprocuradorDJ.get("COLPROCURADORES_POBLACION")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_POBLACION" ):"");
				UtilidadesHash.set(registro,"PROCURADOR_DJ_COL_PROVINCIA", registroprocuradorDJ.get("COLPROCURADORES_PROVINCIA")!=null?UtilidadesHash.getString(registroprocuradorDJ,"COLPROCURADORES_PROVINCIA" ):"");
			}//Fin del for donde recuperamos los datos del procurador del ejg
			
			// Aniadimos el fundamento del ejg
			String idFundamento = (String)registro.get("IDFUNDAMENTOCALIF");
			helperInformes.completarHashSalida(registro,getFundamentoEjgSalida(idInstitucion,idFundamento,idioma));
			
			//Datos del procurador contrario de defensa juridica de un EJG
			ScsProcuradorAdm procuradoradm = new ScsProcuradorAdm(usrBean );								
			Hashtable hastprocurador = new Hashtable();
			hastprocurador.put(ScsContrariosEJGBean.C_IDINSTITUCION, idInstitucion);
			hastprocurador.put(ScsContrariosEJGBean.C_IDTIPOEJG,tipoEjg);
			hastprocurador.put(ScsContrariosEJGBean.C_ANIO,anioEjg);
			hastprocurador.put(ScsContrariosEJGBean.C_NUMERO,numeroEjg);				
		
			Vector datosprocuradorContrariodj=this.getDatosProcuradorContrarioDJ(hastprocurador);
	
			if (datosprocuradorContrariodj.size()==0) {
				UtilidadesHash.set(registro, "PROCURADOR_DJ_CONTRARIO", "");	
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_DOMICI_D_J", "");	
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_PROVIN_D_J", "");
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_POBLA_D_J", "");
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_CP_D_J", "");
			} else {			
					Hashtable datosprocuradorContrario = new Hashtable();	
					String procuradorcontrariodj = "";														
					String domiciliocontrio= "";
					String provinciacontrario= "";
					String poblacioncontrario= "";
					String codigopostalcontrario= "";										
				for (int l = 0; l < datosprocuradorContrariodj.size(); l++) {
					datosprocuradorContrario = (Hashtable) datosprocuradorContrariodj.get(l);	
					procuradorcontrariodj = (String)datosprocuradorContrario.get("PROCURADOR_DJ_CONTRARIO");														
					domiciliocontrio= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_DOMICI_D_J");
					provinciacontrario= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_PROVIN_D_J");
					poblacioncontrario= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_POBLA_D_J");
					codigopostalcontrario= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_CP_D_J");	
				
					if (procuradorcontrariodj!=null && !procuradorcontrariodj.trim().equalsIgnoreCase(""))						
						registro.put("PROCURADOR_DJ_CONTRARIO",procuradorcontrariodj);
					else
						UtilidadesHash.set(registro, "PROCURADOR_DJ_CONTRARIO", "");	
				
					if (domiciliocontrio!=null && !domiciliocontrio.trim().equalsIgnoreCase(""))						
						registro.put("PROCURADOR_CONTRA_DOMICI_D_J",domiciliocontrio);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_DOMICI_D_J", "");						
					
					if (provinciacontrario!=null && !provinciacontrario.trim().equalsIgnoreCase(""))
						registro.put("PROCURADOR_CONTRA_PROVIN_D_J", provinciacontrario);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_PROVIN_D_J", "");
					
					if (poblacioncontrario!=null && !poblacioncontrario.trim().equalsIgnoreCase(""))
						registro.put("PROCURADOR_CONTRA_POBLA_D_J", poblacioncontrario);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_POBLA_D_J", "");
					
					if (codigopostalcontrario!=null && !codigopostalcontrario.trim().equalsIgnoreCase(""))
						registro.put("PROCURADOR_CONTRA_CP_D_J",codigopostalcontrario);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_CP_D_J", "");
				}
			}//Fin de la recuperacion de los datos del procurador contrario.
			 
			helperInformes.completarHashSalida(registro,datosprocuradorContrariodj);
	
			//Inicio de etiquetas de abogado y representante de defensa juridica
	    	htFuncion = new Hashtable();
	    	htFuncion.put(new Integer(1), idInstitucion);
	    	htFuncion.put(new Integer(2), tipoEjg);
	    	htFuncion.put(new Integer(3), anioEjg);
	    	htFuncion.put(new Integer(4), numeroEjg);						
		
	    	helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETABOGADOCONTRARIO_EJG", "ABOGADOSDEFENSA_JURIDICA"));				
	    	helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETREPRESENTANTE_EJG", "REPRESENTANTESDEFENSA_JURIDICA"));					    	
	    	//Fin de etiquetas de abogado y representante de defensa juridica
			
			//Aniadimos el Juzgado del ejg
			String idJuzgadoEjg = (String)registro.get("IDJUZGADO_DJ");
			String idInstitucionJuzgadoEjg = (String)registro.get("JUZGADOIDINSTITUCION_DJ");
			if (idJuzgadoEjg!=null && !idJuzgadoEjg.trim().equals("")) {
				helperInformes.completarHashSalida(registro,helperInformes.getJuzgadoSalida(idInstitucionJuzgadoEjg, idJuzgadoEjg,"D_J"));
				//Hacemos este cambio ya que anteriormente la descripcion del juzgado era JUZGADO_DEFENSA_JURIDICA
				String juzgadoEjg = (String)registro.get("JUZGADO_D_J");
				if (juzgadoEjg!=null && !juzgadoEjg.trim().equals("")) {
					registro.put("JUZGADO_DEFENSA_JURIDICA", juzgadoEjg);
				} else {
					registro.put("JUZGADO_DEFENSA_JURIDICA", " ");
				}
				
				if (registro.containsKey("ID_POBLACION_JUZGADO_D_J") && registro.get("ID_POBLACION_JUZGADO_D_J")!=null && !((String)registro.get("ID_POBLACION_JUZGADO_D_J")).trim().equals("")) {
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida((String)registro.get("ID_POBLACION_JUZGADO_D_J"), "POBLACION_JUZGADO_D_J"));
				} else {
					registro.put("POBLACION_JUZGADO_D_J", " ");
				}
				
				if (registro.containsKey("ID_PROVINCIA_JUZGADO_D_J") && registro.get("ID_PROVINCIA_JUZGADO_D_J")!=null && !((String)registro.get("ID_PROVINCIA_JUZGADO_D_J")).trim().equals("")) {
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida((String)registro.get("ID_PROVINCIA_JUZGADO_D_J"), "PROVINCIA_JUZGADO_D_J"));
				} else {
					registro.put("PROVINCIA_JUZGADO_D_J", " ");
				}
				
			} else {
				registro.put("JUZGADO_DEFENSA_JURIDICA", " ");
				registro.put("POBLACION_JUZGADO_D_J", " ");
				registro.put("PROVINCIA_JUZGADO_D_J", " ");
				registro.put("DIR_JUZGADO_D_J", " ");
				registro.put("CP_JUZGADO_D_J", " ");
				registro.put("FAX1_JUZGADO_D_J", " ");
				registro.put("TELEFONO1_JUZGADO_D_J", " ");
				registro.put("EMAIL_JUZGADO_D_J", " ");
				registro.put("MOVIL_JUZGADO_D_J", " ");
				registro.put("ID_PROVINCIA_JUZGADO_D_J", " ");
				registro.put("ID_POBLACION_JUZGADO_D_J", " ");
				registro.put("JUZGADO_D_J", " ");
			}
						
			//nombre GuardiaAsistencia	relacionada con ejg										
			idTipoResolAuto = (String)registro.get("IDTIPORESOLAUTO");
			if (idTipoResolAuto!=null && !idTipoResolAuto.trim().equals("")) {
				helperInformes.completarHashSalida(registro,helperInformes.getTipoResolucionAutomatico(idTipoResolAuto,idioma));									
			} else {
				registro.put("DESC_TIPORESOLAUTO", " ");
			}							
			
			idTipoSentidoAuto = (String)registro.get("IDTIPOSENTIDOAUTO");
			if (idTipoSentidoAuto!=null && !idTipoSentidoAuto.trim().equals("")) {
				helperInformes.completarHashSalida(registro,helperInformes.getTipoSentidoAutomatico(idTipoSentidoAuto,idioma));					
			} else {
				registro.put("DESC_TIPOSENTIDOAUTO", " ");										
			}
			
			idTipoDictamenEjg = (String)registro.get("IDTIPODICTAMENEJG");
			if (idTipoDictamenEjg!=null && !idTipoDictamenEjg.trim().equals("")) {
				helperInformes.completarHashSalida(registro,helperInformes.getTipoDictamenEjg(idInstitucion,idTipoDictamenEjg,idioma));	
			} else {
				registro.put("DESC_TIPODICTAMENEJG", " ");
			}							
			
			idTipoRatificacionEjg = (String)registro.get("IDTIPORATIFICACIONEJG");
			if (idTipoRatificacionEjg!=null && !idTipoRatificacionEjg.trim().equals("")) {
				helperInformes.completarHashSalida(registro,helperInformes.getTipoRatificacionEjg(idTipoRatificacionEjg, idioma));
			} else {
				registro.put("DESC_TIPORATIFICACIONEJG", " ");
			}				
	
			// Agregamos la comisaria del ejg
			String idComisariaEjg = (String)registro.get("COMISARIA");
			String idInstitucionComisariaEjg = (String)registro.get("COMISARIAIDINSTITUCION");
			if (idComisariaEjg!=null && !idComisariaEjg.trim().equals("") && idInstitucionComisariaEjg!=null && !idInstitucionComisariaEjg.trim().equals("")) { 
				helperInformes.completarHashSalida(registro,getComisariaEjgSalida(idInstitucionComisariaEjg, idComisariaEjg));
				
				// Comprobamos nulos
				if (!registro.containsKey("COMISARIA_D_J") || registro.get("COMISARIA_D_J")==null) {
					registro.put("COMISARIA_D_J", " ");
				}
				
				if (!registro.containsKey("COMISARIA_DEFENSA_JURIDICA") || registro.get("COMISARIA_DEFENSA_JURIDICA")==null) {
					registro.put("COMISARIA_DEFENSA_JURIDICA", " ");
				}
				
				if (!registro.containsKey("CP_COMISARIA_D_J") || registro.get("CP_COMISARIA_D_J")==null) {
					registro.put("CP_COMISARIA_D_J", " ");
				}
				
				if (!registro.containsKey("DIR_COMISARIA_D_J") || registro.get("DIR_COMISARIA_D_J")==null) {
					registro.put("DIR_COMISARIA_D_J", " ");
				}
				
				if (!registro.containsKey("POBLACION_COMISARIA_D_J") || registro.get("POBLACION_COMISARIA_D_J")==null) {
					registro.put("POBLACION_COMISARIA_D_J", " ");
				}
				
				if (!registro.containsKey("PROVINCIA_COMISARIA_D_J") || registro.get("PROVINCIA_COMISARIA_D_J")==null) {
					registro.put("PROVINCIA_COMISARIA_D_J", " ");
				}				
				
			} else {				
				registro.put("COMISARIA_D_J", " ");
				registro.put("COMISARIA_DEFENSA_JURIDICA", " ");
				registro.put("CP_COMISARIA_D_J", " ");
				registro.put("DIR_COMISARIA_D_J", " ");
				registro.put("POBLACION_COMISARIA_D_J", " ");
				registro.put("PROVINCIA_COMISARIA_D_J", " ");
			}
			
			if (idComisariaEjg!=null && !idComisariaEjg.trim().equalsIgnoreCase("")) {
				registro.put("LUGAR", registro.get("COMISARIA_DEFENSA_JURIDICA"));
			} else if(idJuzgadoEjg!=null && !idJuzgadoEjg.trim().equalsIgnoreCase("")) {
				registro.put("LUGAR", registro.get("JUZGADO_DEFENSA_JURIDICA"));
			} else {			
				registro.put("LUGAR", "");
			} 		
	
			/**Datos de la desingacion asociada al Ejg**/
			String numeroDesigna = (String)registro.get("DES_NUMERO");
			String anioDesigna = (String)registro.get("DES_ANIO");
			String idTurnoDesigna  = (String)registro.get("DES_IDTURNO");
			String idInstitucionDesigna  = (String)registro.get("DES_INSTITUCION");
			if (numeroDesigna!=null && !numeroDesigna.trim().equalsIgnoreCase("")) {
				helperInformes.completarHashSalida(registro,getDesignaEjgSalida(idInstitucionDesigna, 	idTurnoDesigna,anioDesigna,numeroDesigna,idioma));								
				helperInformes.completarHashSalida(registro,helperInformes.getTurnoSalida(idInstitucion,idTurnoDesigna));
				
				String idProcedimiento = (String)registro.get("IDPROCEDIMIENTO");
				if (idProcedimiento==null || idProcedimiento.trim().equalsIgnoreCase("")) {
				    idProcedimiento="-33"; // forzamos que no encuentre datos, en lugar de dar error
				}
				helperInformes.completarHashSalida(registro,helperInformes.getProcedimientoSalida(idInstitucion,idProcedimiento,""));								
	
				htFuncion = new Hashtable(); 
				htFuncion.put(new Integer(1), idTurnoDesigna);
				htFuncion.put(new Integer(2), idInstitucionDesigna);
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_NOMBRE_PARTIDO", "NOMBRE_PARTIDO"));				
	
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucionDesigna);
				htFuncion.put(new Integer(2), idTurnoDesigna);
				htFuncion.put(new Integer(3), anioDesigna);
				htFuncion.put(new Integer(4), numeroDesigna);
	
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETCONTRARIOS_DESIGNA", "CONTRARIOS"));
	
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETPROCURADORCONT_DESIG", "PROCURADOR_CONTRARIOS"));
	
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucionDesigna);
				htFuncion.put(new Integer(2), anioDesigna);
				htFuncion.put(new Integer(3), idTurnoDesigna);
				htFuncion.put(new Integer(4), numeroDesigna);
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETACTUACIONESDESIGNA", "LISTA_ACTUACIONES_DESIGNA"));			
				htFuncion.put(new Integer(5), "dd-mm-yyyy");					
				helperInformes.completarHashSalida(registro,getFisrtAsistencia(htFuncion,"FECHA_ACTUACION"));				
				UtilidadesHash.set(registro, "FECHA_ACTUACION", (String)registro.get("FECHA_ACTUACION")); 								
				htFuncion.put(new Integer(5), "hh:MI:ss");				
				helperInformes.completarHashSalida(registro,getFisrtAsistencia(htFuncion,"HORA_ACTUACION"));	
				htFuncion.put(new Integer(5), "0");
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETINTERESADOSDESIGNA", "LISTA_INTERESADOS_DESIGNA"));				
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucionDesigna);
				htFuncion.put(new Integer(2), numeroDesigna);
				htFuncion.put(new Integer(3), idTurnoDesigna);
				htFuncion.put(new Integer(4), anioDesigna);
				htFuncion.put(new Integer(5), idioma);
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETDELITOS_DESIGNA", "DELITOS"));
				
				if(registro.get("IDINSTITUCIONORIGEN_LETDESIGNA")!=null && !((String)registro.get("IDINSTITUCIONORIGEN_LETDESIGNA")).equals("")){
					registro.put("IDINSTITUCION_LETDESIGNA", (String)registro.get("IDINSTITUCIONORIGEN_LETDESIGNA"));
				}
				
				String idInstitucionLetradoDesigna  = (String)registro.get("IDINSTITUCION_LETDESIGNA");
				String idLetradoDesigna  = (String)registro.get("IDPERSONA_DESIGNA");								 
				if (idLetradoDesigna!=null && !idLetradoDesigna.trim().equals("")) {
					if (!registro.containsKey("NOMBRE_LETRADO_DESIGNADO") || registro.get("NOMBRE_LETRADO_DESIGNADO") == null || ((String) registro.get("NOMBRE_LETRADO_DESIGNADO")).trim().equals("")) {
						helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucionLetradoDesigna, idLetradoDesigna,"LETRADO_DESIGNADO"));
					}	
					
					String sexoLetrado  = (String)registro.get("SEXO_ST_LETRADO_DESIGNADO");
					sexoLetrado = UtilidadesString.getMensajeIdioma(usrbean, sexoLetrado);
					registro.put("SEXO_LETRADO_DESIGNADO", sexoLetrado);
					helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idLetradoDesigna,idInstitucionLetradoDesigna,"LETRADO_DESIGNADO"));									
					helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idLetradoDesigna,idInstitucionLetradoDesigna,"LETRADO_DESIGNADO"));									
					String telefonoDespacho = (String)registro.get("TELDESPACHO_LETRADO_DESIGNADO");
					if (telefonoDespacho!=null)
						UtilidadesHash.set(registro, "TELEFONODESPACHO_LET_DESIGNADO", telefonoDespacho);
					else
						UtilidadesHash.set(registro, "TELEFONODESPACHO_LET_DESIGNADO", "");								
	
					String pobLetrado = (String)registro.get("POBLACION_LETRADO_DESIGNADO");
					if (pobLetrado==null ||pobLetrado.trim().equalsIgnoreCase("")) {
						String idPobLetrado = (String)registro.get("ID_POBLACION_LETRADO_DESIGNADO");
						helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetrado,"POBLACION_LETRADO_DESIGNADO"));
						String idProvLetrado = (String)registro.get("ID_PROVINCIA_LETRADO_DESIGNADO");
						if (idProvLetrado!=null && !idProvLetrado.trim().equalsIgnoreCase(""))
							helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetrado,"PROVINCIA_LETRADO_DESIGNADO"));
						else
							UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");									
					} else {
						UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
					}
					
				} else {
					UtilidadesHash.set(registro, "NCOLEGIADO_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "NOMBRE_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "SEXO_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "NIFCIF_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "DOMICILIO_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "CP_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "POBLACION_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "TELEFONODESPACHO_LET_DESIGNADO", "");
					UtilidadesHash.set(registro, "FAX_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "EMAIL_LETRADO_DESIGNADO", "");									 
					UtilidadesHash.set(registro, "TELEFONO1_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "TELEFONO2_LETRADO_DESIGNADO", "");
					UtilidadesHash.set(registro, "MOVIL_LETRADO_DESIGNADO", "");									
				}
				
				String idJuzgadoDesigna  = (String)registro.get("IDJUZGADODESIGNA");
				String idInstitucionJuzgadoDesigna  = (String)registro.get("IDINSTITUCION_JUZGDESIGNA");
				if (idJuzgadoDesigna!=null && !idJuzgadoDesigna.trim().equals(""))
					helperInformes.completarHashSalida(registro,getJuzgadoDesignaEjgSalida(idInstitucionJuzgadoDesigna, idJuzgadoDesigna));
				else {
					//Hay tanto lio que voy a comprobar que no existe antes de machacarlos
					if ((String)registro.get("JUZGADO")==null || ((String)registro.get("JUZGADO")).trim().equals("")) {
						UtilidadesHash.set(registro, "JUZGADO", "");
						UtilidadesHash.set(registro, "DIR_JUZGADO", "");
						UtilidadesHash.set(registro, "CP_JUZGADO", "");
						UtilidadesHash.set(registro, "FAX1_JUZGADO", "");
						UtilidadesHash.set(registro, "TELEFONO1_JUZGADO", "");
						UtilidadesHash.set(registro, "EMAIL_JUZGADO", "");
						UtilidadesHash.set(registro, "MOVIL_JUZGADO", "");
						UtilidadesHash.set(registro, "POBLACION_JUZGADO", "");
					}
				}
				
			} else {
				//Sino hay designa completamos los campos de la designa con " "
				UtilidadesHash.set(registro, "AUTOS", "");
				UtilidadesHash.set(registro, "FECHA_JUICIO", "");
				UtilidadesHash.set(registro, "HORA_JUICIO", "");
				UtilidadesHash.set(registro, "JUZGADO", "");
				UtilidadesHash.set(registro, "DIR_JUZGADO", "");
				UtilidadesHash.set(registro, "CP_JUZGADO", "");
				UtilidadesHash.set(registro, "FAX1_JUZGADO", "");
				UtilidadesHash.set(registro, "TELEFONO1_JUZGADO", "");
				UtilidadesHash.set(registro, "EMAIL_JUZGADO", "");
				UtilidadesHash.set(registro, "MOVIL_JUZGADO", "");
				UtilidadesHash.set(registro, "POBLACION_JUZGADO", "");
				UtilidadesHash.set(registro, "CONTRARIOS", "");
				UtilidadesHash.set(registro, "PROCURADOR_CONTRARIOS", "");
				UtilidadesHash.set(registro, "ANIO_DESIGNA", "");
				UtilidadesHash.set(registro, "CODIGO", "");
				UtilidadesHash.set(registro, "ASUNTO", "");
				UtilidadesHash.set(registro, "NOFICIO", "");
				UtilidadesHash.set(registro, "PROCEDIMIENTO", "");
				UtilidadesHash.set(registro, "DELITOS", "");
				UtilidadesHash.set(registro, "FECHA_DESIGNA", "");
				UtilidadesHash.set(registro, "DESCRIPCION_TURNO", "");
				UtilidadesHash.set(registro, "NOMBRE_TURNO", "");
				UtilidadesHash.set(registro, "ABREV_TURNO", "");
				UtilidadesHash.set(registro, "NOMBRE_PARTIDO", "");								
				UtilidadesHash.set(registro, "NCOLEGIADO_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "NOMBRE_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "SEXO_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "NIFCIF_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "DOMICILIO_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "CP_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "POBLACION_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "TELEFONODESPACHO_LET_DESIGNADO", "");
				UtilidadesHash.set(registro, "FAX_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "EMAIL_LETRADO_DESIGNADO", "");								 
				UtilidadesHash.set(registro, "TELEFONO1_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "TELEFONO2_LETRADO_DESIGNADO", "");
				UtilidadesHash.set(registro, "MOVIL_LETRADO_DESIGNADO", "");			
				UtilidadesHash.set(registro, "LISTA_ACTUACIONES_DESIGNA", "");
				UtilidadesHash.set(registro, "FECHA_ACTUACION", "");
				UtilidadesHash.set(registro, "HORA_ACTUACION", "");
				UtilidadesHash.set(registro, "LISTA_INTERESADOS_DESIGNA", "");			
			}
			
			if(registro.get("N_APELLI_1_LETRADO_DESIGNADO")==null)
				registro.put("N_APELLI_1_LETRADO_DESIGNADO","");
			
			if(registro.get("N_APEL_1_2_LETRADO_DESIGNADO")==null)
				registro.put("N_APEL_1_2_LETRADO_DESIGNADO","");
			
			if(registro.get("APEL_1_2_N_LETRADO_DESIGNADO")==null)
				registro.put("APEL_1_2_N_LETRADO_DESIGNADO","");
			/**Fin de Datos de la desingacion asociada al Ejg**/
			
			// Aqui sacaremos la informacion de la persona a la que va dirigida la carta
			ScsUnidadFamiliarEJGAdm admUniFam = new ScsUnidadFamiliarEJGAdm(this.usrbean);
			if (idPersonaJG!=null && !idPersonaJG.equalsIgnoreCase("")) {
				Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
				if (vDestinatario!=null && vDestinatario.size()>0) {
					Hashtable clone = (Hashtable) registro.clone();
					Hashtable destinatario = (Hashtable) vDestinatario.get(0);
					registro.putAll(destinatario);
				}
			}					
			
			String parrafoLetrado = "";
			if (registro.containsKey("NOMBRE_LETRADO_DESIGNADO") && registro.get("NOMBRE_LETRADO_DESIGNADO")!=null && !((String)registro.get("NOMBRE_LETRADO_DESIGNADO")).trim().equals("") ) {
				parrafoLetrado = UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.letrado") + " " + (String)registro.get("NOMBRE_LETRADO_DESIGNADO");
				
				if (registro.containsKey("TELEFONO1_LETRADO_DESIGNADO")  && registro.get("TELEFONO1_LETRADO_DESIGNADO")!=null && !((String)registro.get("TELEFONO1_LETRADO_DESIGNADO")).trim().equals("") ) {
					parrafoLetrado += " " + UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.telefono")  + " " + (String)registro.get("TELEFONO1_LETRADO_DESIGNADO");
				}
			}	
			//registro.put("PARRAFO_LETRADO", parrafoLetrado);			
			
			String parrafoProcurador = "";
			if (registro.containsKey("PROCURADOR")  && registro.get("PROCURADOR")!=null && !((String)registro.get("PROCURADOR")).trim().equals("") ) {
				parrafoProcurador = UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.procurador") + " " + (String)registro.get("PROCURADOR");
				
				if (registro.containsKey("PROCURADOR_TELEFONO1")  && registro.get("PROCURADOR_TELEFONO1")!=null && !((String)registro.get("PROCURADOR_TELEFONO1")).trim().equals("") ) {
					parrafoProcurador +=  " " + UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.telefono")  + " " + (String)registro.get("PROCURADOR_TELEFONO1");
				}
			}				
			
			//registro.put("PARRAFO_PROCURADOR", parrafoProcurador);								
			registro.put("PARRAFO_LETRADO_PROCURADOR", parrafoLetrado+"\r"+parrafoProcurador);
			
			if ((registro.get("IDREPRESENTANTEJG")!=null)&&(!registro.get("IDREPRESENTANTEJG").toString().isEmpty())) {
									
				String IdpersonaRepresentante=(String)registro.get("IDREPRESENTANTEJG");
				ScsPersonaJGAdm scsPersonaJGAdm=new ScsPersonaJGAdm(this.usrbean);
				Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(IdpersonaRepresentante,idInstitucion);
				registro.put("NOMBRE_REPR_DEF", (String) representanteLegalDefendido.get("NOMBRE_PJG"));
				registro.put("NOMBRE_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("NOMBRE_PJG")).toUpperCase());
				registro.put("APELLIDO1_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO1_PJG"));
				registro.put("APELLIDO1_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO1_PJG")).toUpperCase());
				registro.put("APELLIDO2_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO2_PJG"));
				registro.put("APELLIDO2_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO2_PJG")).toUpperCase());
				String sexoRepr_def = UtilidadesString.getMensajeIdioma(usrbean, (String) representanteLegalDefendido.get("SEXO_PJG"));		
				registro.put("SEXO_REPR_DEF",sexoRepr_def);
				registro.put("O_A_REPR_DEF", (String) representanteLegalDefendido.get("O_A_PJG"));
				registro.put("EL_LA_REPR_DEF", (String) representanteLegalDefendido.get("EL_LA_PJG"));
				registro.put("NIFCIF_REPR_DEF", (String) representanteLegalDefendido.get("NIF_PJG"));
				registro.put("IDPERSONA_REPR_DEF", (String) representanteLegalDefendido.get("IDPERSONA_PJG"));
				registro.put("IDDIRECCION_REPR_DEF", (String) representanteLegalDefendido.get("IDDIRECCION_PJG"));
				registro.put("DOMICILIO_REPR_DEF", (String) representanteLegalDefendido.get("DOMICILIO_PJG"));
				registro.put("CODIGOPOSTAL_REPR_DEF", (String) representanteLegalDefendido.get("CP_PJG"));
				registro.put("TELEFONO1_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO1_PJG"));
				registro.put("TELEFONO2_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO2_PJG"));
				registro.put("MOVIL_REPR_DEF", (String) representanteLegalDefendido.get("MOVIL_PJG"));
				registro.put("LISTA_TELEFONOS_REPR_DEF", (String) representanteLegalDefendido.get("LISTA_TELEFONOS_REPR"));
				registro.put("FAX1_REPR_DEF", (String) representanteLegalDefendido.get("FAX_PJG"));
				registro.put("CORREOELECTRONICO_REPR_DEF", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
				registro.put("NOMBRE_POBLACION_REPR_DEF", (String) representanteLegalDefendido.get("POBLACION_PJG"));
				registro.put("NOMBRE_PROVINCIA_REPR_DEF", (String) representanteLegalDefendido.get("PROVINCIA_PJG"));
				
			} else {
				registro.put("NOMBRE_REPR_DEF", "");
				registro.put("NOMBRE_REPR_DEF_MAYUS", "");
				registro.put("APELLIDO1_REPR_DEF", "");
				registro.put("APELLIDO1_REPR_DEF_MAYUS","");
				registro.put("APELLIDO2_REPR_DEF", "");
				registro.put("APELLIDO2_REPR_DEF_MAYUS", "");
				registro.put("SEXO_REPR_DEF", "");
				registro.put("O_A_REPR_DEF", "");
				registro.put("EL_LA_REPR_DEF", "");
				registro.put("NIFCIF_REPR_DEF", "");
				registro.put("IDPERSONA_REPR_DEF", "");
				registro.put("IDDIRECCION_REPR_DEF", "");
				registro.put("DOMICILIO_REPR_DEF", "");
				registro.put("CODIGOPOSTAL_REPR_DEF","");
				registro.put("TELEFONO1_REPR_DEF", "");
				registro.put("TELEFONO2_REPR_DEF", "");
				registro.put("MOVIL_REPR_DEF", "");
				registro.put("LISTA_TELEFONOS_REPR_DEF", "");
				registro.put("FAX1_REPR_DEF", "");
				registro.put("CORREOELECTRONICO_REPR_DEF", "");
				registro.put("NOMBRE_POBLACION_REPR_DEF","");
				registro.put("NOMBRE_PROVINCIA_REPR_DEF", "");
			}

		} catch (SIGAException se) {
			throw se;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getregistrodatosEjg");
		}
		
		return registro;		
	}
	
	public Hashtable getJuzgadoProcuradorEjg(String institucion, String anio, String numero, String idTipoEJG) throws ClsExceptions,SIGAException {
		Hashtable htRegistro = null;
		try {
			
			StringBuffer sql = new StringBuffer();
			sql.append(" select e.juzgado  ");
			sql.append(" , e.IDPROCURADOR  ");
			sql.append(" from SCS_EJG e  ");
 
			sql.append(" WHERE e.idinstitucion = ");
			sql.append(institucion);
			sql.append(" AND e.ANIO = ");
			sql.append(anio);
			sql.append(" and e.NUMERO =  ");
			sql.append(numero);
			
			sql.append(" and e.IDTIPOEJG = ");
			sql.append(idTipoEJG);
			RowsContainer rc = new RowsContainer(); 
			
			if (rc.find(sql.toString())) {
				Row fila = (Row) rc.get(0);
				htRegistro = fila.getRow();
				
			} 
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre el idjuzgado de un ejg.");
		}
		return htRegistro;                        
	}

	/** Funcion insert (MasterBean bean)
	 *  @param bean a insertar
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insert(MasterBean bean) throws ClsExceptions {
		try {
			return this.insert(this.beanToHashTable(bean));
		}
		catch (Exception e)	{
			throw new ClsExceptions (e,  e.getMessage());
		}
	}

	/** Funcion insert (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el insert 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insert(Hashtable hash) throws ClsExceptions {
		try {			
			if (!hash.containsKey(ScsEJGBean.C_USUCREACION)) {
				UtilidadesHash.set(hash, ScsEJGBean.C_USUCREACION, this.usuModificacion);
			}
			if (!hash.containsKey(ScsEJGBean.C_FECHACREACION)) {
				UtilidadesHash.set(hash, ScsEJGBean.C_FECHACREACION, "sysdate");
			}
			if (!hash.containsKey(ScsEJGBean.C_USUMODIFICACION)) {
				UtilidadesHash.set(hash, ScsEJGBean.C_USUMODIFICACION, this.usuModificacion);
			}
			if (!hash.containsKey(ScsEJGBean.C_FECHAMODIFICACION)) {
				UtilidadesHash.set(hash, ScsEJGBean.C_FECHAMODIFICACION, "sysdate");
			}

			Row row = new Row();
			row.load(hash);

			String [] campos = this.getCamposBean();
			
			if (row.add(this.nombreTabla, campos) == 1) {
				return true;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}			
	
	public boolean updateDirect(MasterBean bean) throws ClsExceptions {
		try {
			return updateDirect (bean, this.getClavesBean(), this.getCamposActualizablesBean());	
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, e.getMessage());
		}
		
	}	
	
	/**
	 * JPT: OBTIENE LA FECHA DE REUNION DEL ACTA DEL EJG: FECHAREUNION_ACTA y FECHAREUNION_ACTA_LETRA
	 * @param idInstitucion
	 * @param idTipoEjg
	 * @param anio
	 * @param numero
	 * @param idioma
	 * @return
	 * @throws ClsExceptions
	 */
	public Hashtable getFechaReunionActaEjg(String idInstitucion, String idTipoEjg, String anio, String numero, String idioma) throws ClsExceptions {
		Hashtable salida = new Hashtable();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT * ");
			sql.append(" FROM (SELECT ACTA.*,TO_CHAR(ACTA.FECHAREUNION, 'dd/mm/yyyy') AS FECHAREUNION_ACTA, ");
			sql.append(" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(ACTA.FECHAREUNION,  'DMA', 1) AS FECHAREUNION_ACTA_LETRA, ");
			sql.append(" (select F_SIGA_GETRECURSO(NOMBRE, ");
			sql.append(idioma);
			sql.append(") from SCS_PONENTE where IDINSTITUCION = ACTA.IDINSTITUCION  AND IDPONENTE = ACTA.IDPRESIDENTE) PRESIDENTECOMISION, ");
			sql.append(" (select F_SIGA_GETRECURSO(NOMBRE, ");
			sql.append(idioma);
			sql.append(") from SCS_PONENTE where IDINSTITUCION = ACTA.IDINSTITUCION AND IDPONENTE = ACTA.IDSECRETARIO) SECRETARIOCOMISION ");

			sql.append(" FROM SCS_EJG_ACTA EJGACTA, SCS_ACTACOMISION ACTA ");
			sql.append(" WHERE EJGACTA.ANIOEJG = ");
			sql.append(anio);
			sql.append(" AND EJGACTA.NUMEROEJG =  ");
			sql.append(numero);
			sql.append(" AND EJGACTA.IDINSTITUCIONEJG = ");
			sql.append(idInstitucion);
			sql.append(" AND EJGACTA.IDTIPOEJG = ");
			sql.append(idTipoEjg);
			sql.append(" AND EJGACTA.IDACTA = ACTA.IDACTA ");
			sql.append(" AND EJGACTA.IDINSTITUCIONACTA = ACTA.IDINSTITUCION ");
			sql.append(" AND EJGACTA.ANIOACTA = ACTA.ANIOACTA ");
			sql.append(" ORDER BY ACTA.FECHAREUNION desc) ");
			sql.append(" WHERE ROWNUM = 1 ");
			RowsContainer rc = new RowsContainer();
			if (rc.find(sql.toString())) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					salida = fila.getRow();
				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion de la fecha de reuni�n");
		}

		return salida;
	}
	
	/**
	 * JPT: OBTIENE LA ULTIMA FECHA CON ESTADO 'Remitido Comision': FECHAREMITIDO_COMISION y FECHAREMITIDO_COMISION_LETRA
	 * @param idInstitucion
	 * @param idTipoEjg
	 * @param anio
	 * @param numero
	 * @param idioma
	 * @return
	 * @throws ClsExceptions
	 */
	public Hashtable getFechaRemitidoComision(String idInstitucion, String idTipoEjg, String anio, String numero, String idioma) throws ClsExceptions  {
		Hashtable salida = new Hashtable();
		
		try {
			String sql = "SELECT TO_CHAR(ESTADOS.FECHAINICIO, 'dd/mm/yyyy') AS FECHAREMITIDO_COMISION, " +
					" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(ESTADOS.FECHAINICIO , 'DMA', " + idioma + ") AS FECHAREMITIDO_COMISION_LETRA " +
				" FROM SCS_ESTADOEJG ESTADOS " +
				" WHERE ESTADOS.IDINSTITUCION = " + idInstitucion + 
					" AND ESTADOS.IDTIPOEJG = " + idTipoEjg +
					" AND ESTADOS.ANIO = " + anio + 
					" AND ESTADOS.NUMERO = " + numero +
					" AND ESTADOS.IDESTADOEJG = " + ESTADOS_EJG.REMITIDO_COMISION.getCodigo() +
					" AND ESTADOS.fechabaja is null "+
				" ORDER BY ESTADOS.FECHAMODIFICACION DESC ";
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					salida = fila.getRow();	                  
				}
			} 
				
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion de la fecha de remitido de comisi�n");
		}
		
		return salida;
	}	
	
	public boolean actualizarProcuradoresEJG(Vector ejs, String idProcurador, String idInstProcurador, String fCambio, String numeroDesigna) throws ClsExceptions {
		boolean ok = true;
		try {			
			for (int  j = 0; j < ejs.size(); j++){				
				Hashtable ejgProc = (Hashtable) ejs.get(j);		
				
				// HASH DE INSERCION para el nuevo
				Hashtable hash = new Hashtable();
				hash.put(ScsEJGBean.C_IDINSTITUCION,ejgProc.get(ScsEJGBean.C_IDINSTITUCION));
				hash.put(ScsEJGBean.C_NUMERO,ejgProc.get(ScsEJGBean.C_NUMERO));
				hash.put(ScsEJGBean.C_ANIO,ejgProc.get(ScsEJGBean.C_ANIO));
				hash.put(ScsEJGBean.C_IDTIPOEJG,ejgProc.get(ScsEJGBean.C_IDTIPOEJG));
				hash.put(ScsEJGBean.C_IDPROCURADOR,idProcurador);
				hash.put(ScsEJGBean.C_IDINSTITUCIONPROCURADOR,idInstProcurador);
				hash.put(ScsEJGBean.C_NUMERODESIGNAPROC,numeroDesigna);
				hash.put(ScsEJGBean.C_FECHADESIGPROC,fCambio);
				
				String[] campos= {ScsEJGBean.C_IDPROCURADOR, ScsEJGBean.C_IDINSTITUCIONPROCURADOR,ScsEJGBean.C_NUMERODESIGNAPROC,ScsEJGBean.C_FECHADESIGPROC};
				
				ok= this.updateDirect(hash, this.getClavesBean(), campos);
				if (!ok) throw new ClsExceptions(this.getError());			
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return ok;
	}
	public Hashtable getDatosDefensaJuridica(Short idInstitucion,
			Short idTipoEJG, Short anio, Integer numero) throws ClsExceptions {
		Vector datos = new Vector();
		RowsContainer rc = new RowsContainer();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT F_SIGA_GETRECURSO(P.DESCRIPCION, ");
		sqlBuffer.append(  this.usrbean.getLanguage() );
		sqlBuffer.append( ") PRETENSION, ");
		sqlBuffer.append("(SELECT J.CODIGOEJIS FROM SCS_JURISDICCION J ");
		sqlBuffer.append("WHERE J.IDJURISDICCION = P.IDJURISDICCION) JURISDICCION, ");
		sqlBuffer.append("DECODE(EJG.ANIOPROCEDIMIENTO, ");
		sqlBuffer.append("NULL, ");
		sqlBuffer.append("EJG.NUMEROPROCEDIMIENTO, ");
		sqlBuffer.append("EJG.NUMEROPROCEDIMIENTO || EJG.ANIOPROCEDIMIENTO) NUMPROCEDIMINETO, ");
		sqlBuffer.append("(SELECT DECODE(JUZ.CODIGOEJIS,'','',JUZ.CODIGOEJIS||'-')|| JUZ.NOMBRE || ' (' || P.NOMBRE || ')' ");
		sqlBuffer.append("FROM SCS_JUZGADO JUZ, CEN_POBLACIONES P ");
		sqlBuffer.append("WHERE JUZ.IDPOBLACION = P.IDPOBLACION(+) ");
		sqlBuffer.append("AND JUZ.IDJUZGADO = EJG.JUZGADO ");
		sqlBuffer.append("AND JUZ.IDINSTITUCION = EJG.JUZGADOIDINSTITUCION) JUZGADO, ");
		sqlBuffer.append("(SELECT SIT.CODIGOEJIS ");
		sqlBuffer.append("FROM SCS_SITUACION SIT ");
		sqlBuffer.append("WHERE SIT.IDSITUACION = EJG.IDSITUACION) SITUACION, ");

		sqlBuffer.append("DECODE(EJG.IDTIPOENCALIDAD,NULL,'',0,1,2) DECLARANTE ");
		sqlBuffer.append("FROM SCS_EJG EJG, SCS_PRETENSION P ");
		sqlBuffer.append("WHERE P.IDPRETENSION(+) = EJG.IDPRETENSION ");
		sqlBuffer.append(" AND P.IDINSTITUCION(+) = EJG.IDINSTITUCION ");
		sqlBuffer.append(" AND EJG.IDTIPOEJG =  ");
		sqlBuffer.append(idTipoEJG);
		sqlBuffer.append(" AND EJG.IDINSTITUCION =  ");
		sqlBuffer.append(idInstitucion);
		sqlBuffer.append(" AND EJG.ANIO =  ");
		sqlBuffer.append(anio);
		sqlBuffer.append(" AND EJG.NUMERO =  ");
		sqlBuffer.append(numero);

		Hashtable resultado = null;

		try {

			if (rc.find(sqlBuffer.toString())) {

				Row fila = (Row) rc.get(0);
				resultado = fila.getRow();

			} else {
				throw new SIGAException("No se ha encontrado los getDatosDefensaJuridica ");
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,
					"Error al obtener la informacion. ScsPersonaJG.getDatosDefensaJuridica.");
		}

		return resultado;
	}
	
	
	public Hashtable getHistoricoActaEjg(Short idInstitucion, Short idTipoEJG, Short anio, Integer numero, Short idInstitucionActa, Short anioActa, Integer idActa) throws ClsExceptions {
		Vector datos = new Vector();
		RowsContainer rc = new RowsContainer();
		StringBuilder sqlBuffer = new StringBuilder();

		sqlBuffer.append("SELECT EJGACTA.IDINSTITUCIONACTA, ");
		sqlBuffer.append("COM.NUMEROACTA, ");
		sqlBuffer.append("EJGACTA.IDACTA, ");
		sqlBuffer.append("EJGACTA.ANIOACTA, ");
		sqlBuffer.append("EJGACTA.IDTIPORATIFICACIONEJG, ");
		sqlBuffer.append("EJGACTA.IDFUNDAMENTOJURIDICO, ");
		sqlBuffer.append("COM.FECHARESOLUCION ");
		sqlBuffer.append("FROM SCS_EJG_ACTA EJGACTA, SCS_ACTACOMISION COM ");
		sqlBuffer.append("WHERE EJGACTA.IDINSTITUCIONACTA = COM.IDINSTITUCION ");
		sqlBuffer.append("AND EJGACTA.IDACTA = COM.IDACTA ");
		sqlBuffer.append("AND EJGACTA.ANIOACTA = COM.ANIOACTA ");
		sqlBuffer.append("AND EJGACTA.IDINSTITUCIONACTA = ");
		sqlBuffer.append(idInstitucionActa);
		sqlBuffer.append("AND EJGACTA.ANIOACTA =  ");
		sqlBuffer.append(anioActa);
		sqlBuffer.append("AND EJGACTA.IDACTA =  ");
		sqlBuffer.append(idActa);
		sqlBuffer.append("AND EJGACTA.IDINSTITUCIONEJG = ");
		sqlBuffer.append(idInstitucion);
		sqlBuffer.append("AND EJGACTA.IDTIPOEJG = ");
		sqlBuffer.append(idTipoEJG);
		sqlBuffer.append("AND EJGACTA.ANIOEJG =  ");
		sqlBuffer.append(anio);
		sqlBuffer.append("AND EJGACTA.NUMEROEJG =  ");
		sqlBuffer.append(numero);

		Hashtable resultado = null;

		try {

			if (rc.find(sqlBuffer.toString())) {

				Row fila = (Row) rc.get(0);
				resultado = fila.getRow();

			} else {
				throw new SIGAException("No se ha encontrado los getHistoricoActaEjg ");
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion. ScsPersonaJG.getHistoricoActaEjg.");
		}

		return resultado;
	}
	public void actalizaActaEjgSinDatoActa(Hashtable ejgHashtable,Integer idInstitucion,Integer idActa, Integer anioActa ) throws ClsExceptions{
		StringBuilder ejgActaBuilder = new StringBuilder();
		ejgActaBuilder.append(" UPDATE SCS_EJG SET ");
		ejgActaBuilder.append(" IDINSTITUCIONACTA = ");
		ejgActaBuilder.append(idInstitucion);
		ejgActaBuilder.append(",ANIOACTA= ");
		ejgActaBuilder.append(anioActa);
		ejgActaBuilder.append(",IDACTA= ");
		ejgActaBuilder.append(idActa);
		ejgActaBuilder.append(",FECHAMODIFICACION = SYSDATE ");
		ejgActaBuilder.append(" WHERE ");
		ejgActaBuilder.append(" IDINSTITUCION  = ");
		ejgActaBuilder.append(ejgHashtable.get(ScsEJGBean.C_IDINSTITUCION));
		ejgActaBuilder.append(" AND ANIO = ");
		ejgActaBuilder.append(ejgHashtable.get(ScsEJGBean.C_ANIO));
		ejgActaBuilder.append(" AND IDTIPOEJG = ");
		ejgActaBuilder.append(ejgHashtable.get(ScsEJGBean.C_IDTIPOEJG));
		ejgActaBuilder.append(" AND NUMERO =  ");
		ejgActaBuilder.append(ejgHashtable.get(ScsEJGBean.C_NUMERO));
		this.updateSQL(ejgActaBuilder.toString());
		
		
	}
	
	public  ScsEJGBean getEjg(String anio, String numExpediente,String idIntitucion)throws BusinessException  {
		StringBuilder builder = new StringBuilder();
		builder.append(" "); 
		builder.append("SELECT IDINSTITUCION,IDTIPOEJG, ANIO, NUMERO ");
		builder.append("FROM SCS_EJG WHERE ");
		builder.append("IDINSTITUCION = ");
		builder.append(idIntitucion);
		builder.append("AND ANIO =");
		builder.append(anio);
		builder.append("AND NUMEJG =");
		builder.append(numExpediente);
		Vector vHistoricoEJG = null;
		try {
			vHistoricoEJG = selectSQL(builder.toString());
		} catch (ClsExceptions e) {
			throw new BusinessException("Error en SQL"+e.toString());
		}
		
		return vHistoricoEJG!=null && vHistoricoEJG.size() > 0 ?(ScsEJGBean)vHistoricoEJG.get(0):null;
			
		
		
	}
	public boolean isExpedientePteEnviarCAJG(BusinessManager businessManager, String idInstitucion, String idTipoEjg, String anio,
			String numero)throws BusinessException
			{

		SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager
				.getService(SalidaEnviosService.class);

		return salidaEnviosService.isExpedientePteEnviarCAJG(new Short(
				idInstitucion), new Short(idTipoEjg), new Short(anio),
				new Long(numero));

	}
	
	
}