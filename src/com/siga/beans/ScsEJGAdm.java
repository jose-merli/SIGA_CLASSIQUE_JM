package com.siga.beans;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.redabogacia.sigaservices.app.AppConstants.ESTADOS_EJG;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.octetstring.vde.EntrySet;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;

//Clase: ScsEJGAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005 
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * 
 */ 
public class ScsEJGAdm extends MasterBeanAdministrador {
	
	public static enum TipoVentana {
		BUSQUEDA_EJG,
		BUSQUEDA_PREPARACION_CAJG,
		BUSQUEDA_ANIADIR_REMESA		
	}
	
	private final String keyBindConsulta="keyBindConsulta";
	private final String keyBindCodigos="keyBindCodigos";
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsEJGAdm(UsrBean usuario) {
		super(ScsEJGBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Consulta a la base de datos para obtener el número de EJG más alto más uno (para insertar en la base de datos posteriormente)
	 * 
	 * @param  
	 * @return Integer
	 */
	public String calcularNumeroMaximoEJG ()throws ClsExceptions 
	{		
		RowsContainer rc = null;		
		String numeroMaximo = null;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsEJGBean.C_NUMERO + ") + 1) AS NUMERO FROM " + nombreTabla;			 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("NUMERO").equals("")) {
					numeroMaximo = "1";
				}
				else numeroMaximo = prueba.get("NUMERO").toString();				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE NUMERO");
		}
		
		return numeroMaximo;
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

	public Hashtable getTituloPantallaEJG (String idInstitucion, String anio, String numero, String idTipoEJG) 
	{
		try {
			String sql = 	"select " + ScsPersonaJGBean.C_NOMBRE + "," + 
			                            ScsPersonaJGBean.C_APELLIDO1 + ", " + 
										ScsPersonaJGBean.C_APELLIDO2 + ", " + 
										ScsEJGBean.C_ANIO + ", " +
										ScsEJGBean.C_NUMEJG +", "+
										ScsEJGBean.C_IDTIPODICTAMENEJG+","+
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
	
	
	
	public Hashtable getDatosEjg (String idInstitucion, String anio, String numero, String idTipoEJG) 
	{
		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT SUFIJO, NUMEJG AS CODIGO, ");
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
	 * Efectúa un SELECT en la tabla SCS_EJG con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
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
		
		
		// Primero calculamos el NUMERO del EJG que se obtiene a partir del NUMERO máximo de EJG almacenado en la base de datos
		try {

			
			
			rc = new RowsContainer();		
			rc1 = new RowsContainer();
			// Se prepara la sentencia SQL para hacer el select

//			String anio = "";
//			anio = (String) entrada.get("SOJ_ANIO");
//			if(anio == null || anio.equals(""))	anio = (String) entrada.get("ASISTENCIA_ANIO");
//			if(anio == null || anio.equals(""))	anio = (String) entrada.get("DESIGNA_ANIO");
//			if(anio == null || anio.equals(""))	anio = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			
			// Ahora hay que introducir el ANIO, que se obtiene parseando la fecha de apertura (sólo interesa el anho).
			String anio = entrada.get(ScsEJGBean.C_FECHAAPERTURA).toString();
			// El formado te la fecha es DD/MM/AAAA por eso sólo nos interesa a partir de la posición 6 del string
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
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE NUMERO");
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
		
		// Por último ponemos la FECHAAPERTURA en el formato correcto de la base de datos.
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
							ScsEJGBean.C_IDPONENTE,					ScsEJGBean.C_IDORIGENCAJG,
							ScsEJGBean.C_OBSERVACIONIMPUGNACION,	ScsEJGBean.C_FECHAPUBLICACION,
							ScsEJGBean.C_NUMERORESOLUCION,			ScsEJGBean.C_ANIORESOLUCION,
							ScsEJGBean.C_BISRESOLUCION,				ScsEJGBean.C_IDACTA,
							ScsEJGBean.C_IDINSTITUCIONACTA,			ScsEJGBean.C_ANIOACTA,
							ScsEJGBean.C_IDECOMCOLA,				ScsEJGBean.C_REQUIERENOTIFICARPROC,
							ScsEJGBean.C_USUCREACION,				ScsEJGBean.C_FECHACREACION,
							ScsEJGBean.C_FECHAPRESENTACIONPONENTE,	ScsEJGBean.C_ANIOPROCEDIMIENTO};
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
							ScsEJGBean.C_IDPONENTE,					ScsEJGBean.C_IDORIGENCAJG,
							ScsEJGBean.C_OBSERVACIONIMPUGNACION,	ScsEJGBean.C_FECHAPUBLICACION,
							ScsEJGBean.C_NUMERORESOLUCION,			ScsEJGBean.C_ANIORESOLUCION,
							ScsEJGBean.C_BISRESOLUCION,				ScsEJGBean.C_IDACTA,
							ScsEJGBean.C_IDINSTITUCIONACTA,			ScsEJGBean.C_ANIOACTA,
							ScsEJGBean.C_REQUIERENOTIFICARPROC,		ScsEJGBean.C_IDECOMCOLA,
							ScsEJGBean.C_FECHAPRESENTACIONPONENTE,	ScsEJGBean.C_ANIOPROCEDIMIENTO};
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
	 *  @return bean con la información de la hashtable
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
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDPERSONA));
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

		String[] campos= {ScsEJGBean.C_IDINSTITUCION, ScsEJGBean.C_IDTIPOEJG, ScsEJGBean.C_ANIO, ScsEJGBean.C_NUMERO};
		return campos;
	}	
	
	
	/** 
	 * Recoge informacion sobre el declarante para rellenar la solicitud de asistencia<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  tipoEJG - identificador del tipo EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosDeclarante (String institucion, String tipoEJG, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
							"(persona." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || " +
							"persona." + ScsPersonaJGBean.C_APELLIDO2 + " || ', ' || " +
							"persona." + ScsPersonaJGBean.C_NOMBRE + ") AS DECLARANTE, " +
			    			"persona." + ScsPersonaJGBean.C_NIF + "," +
			    			"persona." + ScsPersonaJGBean.C_FECHANACIMIENTO + "," +
			    			UtilidadesMultidioma.getCampoMultidiomaSimple(ScsProfesionBean.T_NOMBRETABLA + "." + ScsProfesionBean.C_DESCRIPCION,this.usrbean.getLanguage()) + " AS PROFESION," +
			    			UtilidadesMultidioma.getCampoMultidiomaSimple(CenEstadoCivilBean.T_NOMBRETABLA + "." + CenEstadoCivilBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS ESTADO_CIVIL," +
			    			" DECODE(persona." + ScsPersonaJGBean.C_REGIMENCONYUGAL + ",'S','SEPARACION DE BIENES','G','GANANCIALES', 'I', 'INDETERMINADO') AS REGIMEN_CONYUGAL," +
			    			"persona." + ScsPersonaJGBean.C_DIRECCION + "," +
			    			CenPaisBean.T_NOMBRETABLA + "." + CenPaisBean.C_NOMBRE + " AS PAIS," +
			    			CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_NOMBRE + " AS PROVINCIA," +
			    			CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + " AS POBLACION," +
			    			"persona." + ScsPersonaJGBean.C_CODIGOPOSTAL + "," +
							ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_NUMEROTELEFONO + " AS NUMERO_TELEFONO" +  
							" FROM " + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + "," + 
									   ScsPersonaJGBean.T_NOMBRETABLA + " persona LEFT JOIN " + ScsTelefonosPersonaBean.T_NOMBRETABLA + " ON " +
																								ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_IDINSTITUCION + "=persona." + ScsPersonaJGBean.C_IDINSTITUCION +
																								" AND " +
																								ScsTelefonosPersonaBean.T_NOMBRETABLA + "." + ScsTelefonosPersonaBean.C_IDPERSONA + "=persona." + ScsPersonaJGBean.C_IDPERSONA + "," +									   
									   ScsPersonaJGBean.T_NOMBRETABLA + " persona_pais LEFT JOIN " + CenPaisBean.T_NOMBRETABLA + " ON " +
																									 CenPaisBean.T_NOMBRETABLA + "." + CenPaisBean.C_IDPAIS + "=persona_pais." + ScsPersonaJGBean.C_IDPAIS + "," +
									   ScsPersonaJGBean.T_NOMBRETABLA + " persona_provincia LEFT JOIN " + CenProvinciaBean.T_NOMBRETABLA + " ON " +
																									      CenProvinciaBean.T_NOMBRETABLA + "." + CenProvinciaBean.C_IDPROVINCIA + "=persona_provincia." + ScsPersonaJGBean.C_IDPROVINCIA + "," +
									   ScsPersonaJGBean.T_NOMBRETABLA + " persona_poblacion LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA + " ON " +
																										  CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_IDPOBLACION + "=persona_poblacion." + ScsPersonaJGBean.C_IDPOBLACION + "," +
									   ScsPersonaJGBean.T_NOMBRETABLA + " persona_estado_civil LEFT JOIN " + CenEstadoCivilBean.T_NOMBRETABLA + " ON " +
																										 	 CenEstadoCivilBean.T_NOMBRETABLA + "." + CenEstadoCivilBean.C_IDESTADO + "=persona_estado_civil." + ScsPersonaJGBean.C_ESTADOCIVIL + "," +
									   ScsPersonaJGBean.T_NOMBRETABLA + " persona_profesion LEFT JOIN " + ScsProfesionBean.T_NOMBRETABLA + " ON " +
																										  ScsProfesionBean.T_NOMBRETABLA + "." + ScsProfesionBean.C_IDPROFESION + "=persona_profesion." + ScsPersonaJGBean.C_IDPROFESION + 
							" WHERE " +
							"persona."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							"persona."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							"persona_pais."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							"persona_pais."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							"persona_provincia."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							"persona_provincia."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							"persona_poblacion."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							"persona_poblacion."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							"persona_estado_civil."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							"persona_estado_civil."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							"persona_profesion."+ ScsPersonaJGBean.C_IDINSTITUCION + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION +
							" AND " +
							"persona_profesion."+ ScsPersonaJGBean.C_IDPERSONA + "=" + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDPERSONA +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_NUMERO + "=" + numero +
							" AND " +
							ScsUnidadFamiliarEJGBean.T_NOMBRETABLA +"."+ ScsUnidadFamiliarEJGBean.C_SOLICITANTE + "=" + ClsConstants.DB_TRUE;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	            
	            //Si no hay datos anhado una hash con Strings vacios:
	            if (datos.isEmpty()) {
	            	Hashtable hash = new Hashtable();
	            	hash.put("DECLARANTE","");
	            	hash.put(ScsPersonaJGBean.C_NIF,"");	            	
	            	hash.put(ScsPersonaJGBean.C_FECHANACIMIENTO,"");
	            	hash.put("PROFESION","");
	            	hash.put("ESTADO_CIVIL","");
	            	hash.put("REGIMEN_CONYUGAL","");
	            	hash.put(ScsPersonaJGBean.C_DIRECCION,"");
	            	hash.put("PAIS","");
	            	hash.put("PROVINCIA","");
	            	hash.put("POBLACION","");
	            	hash.put(ScsPersonaJGBean.C_CODIGOPOSTAL,"");
	            	hash.put("NUMERO_TELEFONO","");
	            	datos.add(hash);
	            }
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al obtener la informacion sobre el declarante.");
	       }
	       return datos;                        
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
	 * Recoge informacion sobre los datos de la defensa juridica 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosDefensaJuridica (String institucion, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") AS LETRADO, " +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ', ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION, " +
			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_CONTRARIOS + "," +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_NUMERO +
							" FROM " + ScsAsistenciasBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + "," + CenDireccionesBean.T_NOMBRETABLA + "," +
									   ScsActuacionAsistenciaBean.T_NOMBRETABLA + "," + CenColegiadoBean.T_NOMBRETABLA + "," + CenPoblacionesBean.T_NOMBRETABLA + "," +
									   ScsTurnoBean.T_NOMBRETABLA + "," + ScsSubzonaBean.T_NOMBRETABLA + "," + CenPartidoJudicialBean.T_NOMBRETABLA +
							" WHERE " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + 
							" AND " +
							CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "(+)=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "(+)=" + ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO +
							" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_ANIO +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_NUMERO +
							" AND " +
							ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDACTUACION + 
								"=" +
								"(SELECT " +
								ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDACTUACION +
									" FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA +
									" WHERE " + 
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + "=" + institucion +
									" AND " +
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + "=" + epoca +
									" AND " +
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + "=" + numero +
									" AND " +
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_FECHA + 
										"=" + 
										"(SELECT MIN(" + ScsActuacionAsistenciaBean.C_FECHA + ")" +
												 " FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA +
												 " WHERE " + 
												 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + 
												 	"=" +
												 institucion +
												 " AND " +
												 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + 
												 	"=" +
												 epoca +
												 " AND " +
												 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + 
												 	"=" +
												 numero +
										")" +
								")" +
							" AND " +
							ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDINSTITUCION +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDZONA +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDSUBZONA +
							" AND " +
							ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_IDPARTIDO +
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
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los datos de la defensa juridica.");
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
	
	public Vector getContrariosEjg(String idInstitucion, String  tipoEjg, String anioEjg, String numeroEjg, String idContrario) throws ClsExceptions {	
  		Vector datos=new Vector();   
  		RowsContainer rc = new RowsContainer();
  		StringBuffer sqlBuffer = new StringBuffer();
 		 
  		
  		sqlBuffer.append("SELECT PER.IDPERSONA IDPERSONA_PJG, ");
  		sqlBuffer.append("DECODE(PER.DIRECCION, null, null, 1) IDDIRECCION_PJG, ");
  		sqlBuffer.append("NVL(PER.NOMBRE, '') NOMBRE_PJG, ");
  		sqlBuffer.append("NVL(PER.APELLIDO1, '') APELLIDO1_PJG, ");
  		sqlBuffer.append("NVL(PER.APELLIDO2, '') APELLIDO2_PJG, ");
  		sqlBuffer.append("NVL2(VIA.DESCRIPCION, F_SIGA_GETRECURSO(VIA.DESCRIPCION, "+this.usrbean.getLanguage()+"), '') || ");
  		sqlBuffer.append("NVL2(PER.DIRECCION, ' ' || PER.DIRECCION, '') || ");
  		sqlBuffer.append("NVL2(PER.NUMERODIR, ' ' || PER.NUMERODIR, '') || ");
  		sqlBuffer.append("NVL2(PER.ESCALERADIR, ' ' || PER.ESCALERADIR, '') || ");
  		sqlBuffer.append("NVL2(PER.PISODIR, ' ' || PER.PISODIR, '') || ");
  		sqlBuffer.append("NVL2(PER.PUERTADIR, ' ' || PER.PUERTADIR, '') AS DOMICILIO_PJG, ");
  		sqlBuffer.append("NVL(PER.CODIGOPOSTAL, '') AS CP_PJG, ");
  		sqlBuffer.append("NVL(POBL.NOMBRE, '') AS POBLACION_PJG, ");
  		sqlBuffer.append("NVL(PROV.NOMBRE, '') AS PROVINCIA_PJG, ");
  		sqlBuffer.append("DECODE(PER.SEXO,  null,    null,   'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_PJG, ");
  		sqlBuffer.append("NVL2(PER.SEXO, DECODE(PER.SEXO, 'H', 'o', 'a'), '') AS O_A_PJG, ");
  		sqlBuffer.append("DECODE(PER.SEXO, 'H', 'el', 'la') as EL_LA_PJG, ");
  		sqlBuffer.append("NVL(PER.NIF, '') AS NIF_PJG, ");
        
  		sqlBuffer.append("(SELECT ST.NUMEROTELEFONO ");
  		sqlBuffer.append("FROM SCS_TELEFONOSPERSONA ST ");
  		sqlBuffer.append("WHERE ST.IDINSTITUCION = PER.IDINSTITUCION ");
  		sqlBuffer.append("AND ST.IDPERSONA = PER.IDPERSONA ");
  		sqlBuffer.append("AND ST.IDTELEFONO = 1) TELEFONO1_PJG, ");
  		sqlBuffer.append("(SELECT ST.NUMEROTELEFONO ");
  		sqlBuffer.append("FROM SCS_TELEFONOSPERSONA ST ");
  		sqlBuffer.append("WHERE ST.IDINSTITUCION = PER.IDINSTITUCION ");
  		sqlBuffer.append("AND ST.IDPERSONA = PER.IDPERSONA ");
  		sqlBuffer.append("AND ST.IDTELEFONO = 2) TELEFONO2_PJG, ");
        
  		sqlBuffer.append("(SELECT MAX(ST.NUMEROTELEFONO) ");
  		sqlBuffer.append("FROM SCS_TELEFONOSPERSONA ST ");
  		sqlBuffer.append("WHERE ST.IDINSTITUCION = PER.IDINSTITUCION ");
  		sqlBuffer.append("AND ST.IDPERSONA = PER.IDPERSONA ");
  		sqlBuffer.append("AND ST.PREFERENTESMS = 1) MOVIL_PJG, ");
  		sqlBuffer.append("PER.Fax FAX_PJG, ");
  		sqlBuffer.append("PER.CORREOELECTRONICO CORREOELECTRONICO_PJG ");
  		sqlBuffer.append(",  PER.IDREPRESENTANTEJG,   PER.IDINSTITUCION ");

  		sqlBuffer.append(",con.Nombreabogadocontrarioejg ABOGADO_CONTRARIO, ");
        sqlBuffer.append("con.Nombrerepresentanteejg REPRESENTANTE_CONTRARIO ");
        sqlBuffer.append(", NVL(PRO.NOMBRE, '') || NVL2(PRO.APELLIDOS1, ' ' || PRO.APELLIDOS1, '') || ");
        sqlBuffer.append("NVL2(PRO.APELLIDOS2, ' ' || PRO.APELLIDOS2, '') AS PROCURADOR_CONTRARIO ");
  		
  		sqlBuffer.append("FROM  SCS_CONTRARIOSEJG CON,SCS_PERSONAJG   PER, ");
  		sqlBuffer.append("CEN_TIPOVIA     VIA, ");
  		sqlBuffer.append("CEN_POBLACIONES POBL, ");
  		sqlBuffer.append("CEN_PROVINCIAS  PROV ");
  		sqlBuffer.append(",     SCS_PROCURADOR PRO ");
  		sqlBuffer.append("WHERE ");
  		sqlBuffer.append("CON.IDINSTITUCION = PER.IDINSTITUCION ");
  		sqlBuffer.append("AND CON.IDPERSONA = PER.IDPERSONA ");
  		sqlBuffer.append("AND VIA.IDINSTITUCION(+) = PER.IDINSTITUCION ");
  		sqlBuffer.append("AND VIA.IDTIPOVIA(+) = PER.IDTIPOVIA ");
  		sqlBuffer.append("AND POBL.IDPOBLACION(+) = PER.IDPOBLACION ");
  		sqlBuffer.append("AND PROV.IDPROVINCIA(+) = PER.IDPROVINCIA ");
  		sqlBuffer.append("AND CON.IDPROCURADOR = PRO.IDPROCURADOR(+) ");
  		sqlBuffer.append("AND CON.IDINSTITUCION_PROCU = PRO.IDINSTITUCION(+) ");
  		
  		
  		sqlBuffer.append("AND CON.IDINSTITUCION =  ");
  		sqlBuffer.append(idInstitucion);
  		sqlBuffer.append("AND CON.IDTIPOEJG =  ");
  		sqlBuffer.append(tipoEjg);
  		sqlBuffer.append("AND CON.ANIO =  ");
  		sqlBuffer.append(anioEjg);
  		sqlBuffer.append("AND CON.NUMERO =  ");
  		sqlBuffer.append(numeroEjg);
  				  				
  				
  		if(idContrario!=null){
  			sqlBuffer.append(" AND CON.IDPERSONA =  ");
  			sqlBuffer.append(idContrario);
  		}
  				
        try {    
        	if (rc.find(sqlBuffer.toString())) {
        		for (int i = 0; i < rc.size(); i++){
        			Row fila = (Row) rc.get(i);
        			Hashtable resultado=fila.getRow();	                  
		            datos.add(resultado);
        		}
        	} 
        } catch (Exception e) {
        	throw new ClsExceptions (e, "Error al obtener la informacion sobre el tipo ejg colegio de una designa.");
        }
       
        return datos;      
	}	
	
	
	
	/** 
	 * Recoge informacion sobre los datos de la defensa juridica 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosDefensaJuridica (String institucion, String epoca, String tipoEJG, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	 /*           String sql ="SELECT " +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") AS LETRADO, " +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ', ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION, " +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_NUMERO +
							" FROM " + ScsEJGBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + "," + CenDireccionesBean.T_NOMBRETABLA + "," +
									   CenColegiadoBean.T_NOMBRETABLA + "," + CenPoblacionesBean.T_NOMBRETABLA + "," +ScsTurnoBean.T_NOMBRETABLA + "," + 
									   ScsSubzonaBean.T_NOMBRETABLA + "," + CenPartidoJudicialBean.T_NOMBRETABLA +
							" WHERE " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + 
							" AND " +
							CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "(+)=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "(+)=" + ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "(+)=" + ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDPERSONA +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO +
							" AND " +
							// Modificacion MAV 12/7/05 a peticion de JG
							ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDINSTITUCION + "(+)=" + ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDINSTITUCION +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDZONA + "(+)=" + ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDZONA +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDSUBZONA + "(+)=" + ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDSUBZONA +
							" AND " +
							ScsSubzonaBean.T_NOMBRETABLA + "." + ScsSubzonaBean.C_IDPARTIDO + "(+)=" + CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_IDPARTIDO +
							// Fin modificacion
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_NUMERO + "=" + numero;
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
		*/
	            String sql =
	            	"SELECT " +
	            	"(p.APELLIDOS1 || ' ' || p.APELLIDOS2 || ', ' || p.NOMBRE) AS LETRADO," +
	            	"(d.DOMICILIO || ' ' || d.CODIGOPOSTAL || ', ' || pb.NOMBRE) AS DIRECCION "+  
	            	"FROM " +
	            	"SCS_EJG ejg, CEN_PERSONA p, " +
	            	"CEN_DIRECCIONES d, Cen_Direccion_Tipodireccion td, " +
	            	"CEN_POBLACIONES pb "+
	            	"WHERE ejg.IDINSTITUCION = " + institucion +
	            	" AND ejg.IDINSTITUCION = d.IDINSTITUCION(+) " +
	            	" AND ejg.IDINSTITUCION = td.idinstitucion " +
	            	" AND ejg.IDTIPOEJG = " + tipoEJG +
	            	" AND ejg.ANIO =" + epoca +
	            	" AND ejg.NUMERO = " + numero +
	            	" AND ejg.IDPERSONA = td.idpersona " +
	            	" AND ejg.IDPERSONA = p.IDPERSONA " +
	            	" AND ejg.IDPERSONA = d.IDPERSONA(+) " +
	            	" AND td.iddireccion = d.iddireccion " +
	            	" AND td.idtipodireccion = 2 " +
	            	" AND d.IDPOBLACION = pb.IDPOBLACION(+)"+
	            	" AND d.fechabaja is null ";
	            
	            if (rc.find(sql)) {
		               for (int i = 0; i < rc.size(); i++){
		                  Row fila = (Row) rc.get(i);
		                  Hashtable resultado=fila.getRow();	                  
		                  datos.add(resultado);
		               }
		            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los datos de la defensa juridica.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge informacion sobre los datos de asistencia al detenido 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosAsistenciaDetenido (String institucion, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") AS ABOGADO, " +
			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_DELITOSIMPUTADOS + "," +
			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_FECHAHORA + " AS FECHA_ASISTENCIA," +
			    			ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_FECHASOLICITUD + " AS FECHA_SOLICITUD," +
			    			ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_LUGAR + "," +
							"(" + ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + " || '/' || " +
							ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + ") AS NUMERO_DILIGENCIA" +
							" FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA + "," + ScsAsistenciasBean.T_NOMBRETABLA + "," + 
									   CenPersonaBean.T_NOMBRETABLA + "," + CenColegiadoBean.T_NOMBRETABLA + 
							" WHERE " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDPERSONACOLEGIADO + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + 
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_IDINSTITUCION + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDINSTITUCION +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_ANIO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_ANIO +
							" AND " +
							ScsAsistenciasBean.T_NOMBRETABLA +"."+ ScsAsistenciasBean.C_NUMERO + "=" + ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_NUMERO +
							" AND " +
							ScsActuacionAsistenciaBean.T_NOMBRETABLA +"."+ ScsActuacionAsistenciaBean.C_IDACTUACION + 
								"=" +
								"(SELECT MIN(" +
								ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDACTUACION + ")" +
									" FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA +
									" WHERE " + 
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + "=" + institucion +
									" AND " +
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + "=" + epoca +
									" AND " +
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + "=" + numero +
									// Modificacion MAV 12/7/05 a peticion de JG
									/*
									" AND " +
									ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_FECHA + 
										"=" + 
										"(SELECT MIN(" + ScsActuacionAsistenciaBean.C_FECHA + ")" +
												 " FROM " + ScsActuacionAsistenciaBean.T_NOMBRETABLA +
												 " WHERE " + 
												 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_IDINSTITUCION + 
												 	"=" +
												 institucion +
												 " AND " +
												 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_ANIO + 
												 	"=" +
												 epoca +
												 " AND " +
												 ScsActuacionAsistenciaBean.T_NOMBRETABLA + "." + ScsActuacionAsistenciaBean.C_NUMERO + 
												 	"=" +
												 numero +
										")" +
									*/	
								")" +
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
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los datos de la asistencia al detenido.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge informacion sobre las EJGs para las cartas de interesados de EJG 
	 * @param  institucion - identificador de la institucion
	 * @param  tipoEJG - tipo de EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosCartaEJG (String institucion, String tipoEJG, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
			    			"(" + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_NUMERO + " || '/' || " +	ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_ANIO + ") AS ASISTENCIA," +
			    			"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGDESIGNABean.C_NUMERODESIGNA + " || '/' || " + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGDESIGNABean.C_ANIODESIGNA + " || '/' || " + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGDESIGNABean.C_IDTURNO + ") AS DESIGNA," +
			    			"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_NUMERO + " || '/' || " + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_ANIO + ") AS NUMERO_EXPEDIENTE," +
			    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_IDPERSONA + " AS PERSONA," +
			    			ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_FECHAAPERTURA + " AS FECHA_APERTURA," +
			    			ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_OBSERVACIONES + " AS OBSERVACIONES" +
							" FROM " + ScsEJGBean.T_NOMBRETABLA + ","+ScsEJGDESIGNABean.T_NOMBRETABLA+
							" LEFT JOIN " + ScsPersonaJGBean.T_NOMBRETABLA + " ON " +
										    ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION +
											" AND " +
											ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDPERSONAJG + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA +
							" WHERE " +			 
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_NUMERO + "=" + numero+
	            
				            " AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_NUMERO + "=" + ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_EJGNUMERO+
				            " AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ANIO + "=" + ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_EJGANIO+
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDTIPOEJG + "=" + ScsAsistenciasBean.T_NOMBRETABLA+"."+ScsAsistenciasBean.C_EJGIDTIPOEJG+
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ANIO + "=" + ScsEJGDESIGNABean.T_NOMBRETABLA+"."+ScsEJGDESIGNABean.C_ANIOEJG+
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_NUMERO + "=" + ScsEJGDESIGNABean.T_NOMBRETABLA+"."+ScsEJGDESIGNABean.C_NUMEROEJG+
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDTIPOEJG + "=" + ScsEJGDESIGNABean.T_NOMBRETABLA+"."+ScsEJGDESIGNABean.C_IDTIPOEJG+
							" AND " +
							ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + ScsEJGDESIGNABean.T_NOMBRETABLA+"."+ScsEJGDESIGNABean.C_IDINSTITUCION
							;
	            
	    

//	            String sql ="SELECT " +
//    			"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_ASISTENCIA_NUMERO + " || '/' || " +	ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_ASISTENCIA_ANIO + ") AS ASISTENCIA," +
//    			"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_DESIGNA_NUMERO + " || '/' || " + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_DESIGNA_ANIO + " || '/' || " + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_DESIGNA_IDTURNO + ") AS DESIGNA," +
//    			"(" + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_NUMERO + " || '/' || " + ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_ANIO + ") AS NUMERO_EXPEDIENTE," +
//    			ScsPersonaJGBean.T_NOMBRETABLA + "." + ScsPersonaJGBean.C_IDPERSONA + " AS PERSONA," +
//    			ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_FECHAAPERTURA + " AS FECHA_APERTURA," +
//    			ScsEJGBean.T_NOMBRETABLA + "." + ScsEJGBean.C_OBSERVACIONES + " AS OBSERVACIONES" +
//				" FROM " + ScsEJGBean.T_NOMBRETABLA + 
//				" LEFT JOIN " + ScsPersonaJGBean.T_NOMBRETABLA + " ON " +
//							    ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDINSTITUCION +
//								" AND " +
//								ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDPERSONAJG + "=" + ScsPersonaJGBean.T_NOMBRETABLA +"."+ ScsPersonaJGBean.C_IDPERSONA +
//				" WHERE " +			 
//				ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDINSTITUCION + "=" + institucion +
//				" AND " +
//				ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_IDTIPOEJG + "=" + tipoEJG +
//				" AND " +
//				ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_ANIO + "=" + epoca +
//				" AND " +
//				ScsEJGBean.T_NOMBRETABLA +"."+ ScsEJGBean.C_NUMERO + "=" + numero;	            
	            
	            
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las actuaciosnes de una designa.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * 	 * Recoge informacion sobre las actuaciones relacionadas con una designa 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getActuacionesDesignas (String institucion, String epoca, String numero) throws ClsExceptions,SIGAException {
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
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las actuaciosnes de una designa.");
	       }
	       return datos;                        
	    }
	
	
	/** 
	 * Recoge la informacion relacionada con EJG para la carta a los interesados de EJG 
	 * @param  datos - conjunto de filtros aplicados
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosCartaEJG (Hashtable datos) throws ClsExceptions{
			String consulta="";
			
			/* Construimos la primera parte de la consulta, donde escogemos los campos a recuperar y las tablas necesarias */

			
			
			consulta = "select "+
			   "ejg."+ScsEJGBean.C_IDINSTITUCION +","+
			   "ejg."+ScsEJGBean.C_IDTIPOEJG+","+
			   "ejg."+ScsEJGBean.C_NUMERO+","+
			   "ejg."+ScsEJGBean.C_ANIO+","+
		       "t."+ScsTurnoBean.C_NOMBRE +" TURNO,"+
		       "g."+ScsGuardiasTurnoBean.C_NOMBRE +" GUARDIA,"+
		       "to_char(ejg."+ScsEJGBean.C_FECHAAPERTURA+",'DD/MM/YYYY') "+ScsEJGBean.C_FECHAAPERTURA+","+
			   
//		       "ejg."+ScsEJGBean.C_ASISTENCIA_NUMERO+","+
//		       "ejg."+ScsEJGBean.C_ASISTENCIA_ANIO+","+
		       "asis."+ScsAsistenciasBean.C_NUMERO+","+
			   "asis."+ScsAsistenciasBean.C_ANIO+","+

		       "ejgdesig."+ScsEJGDESIGNABean.C_NUMERODESIGNA+","+
		       "ejgdesig."+ScsEJGDESIGNABean.C_ANIODESIGNA+","+
		       "ejg."+ScsEJGBean.C_OBSERVACIONES+","+
		       "ejg."+ScsEJGBean.C_IDPERSONAJG+" IDINTERESADO,"+
		       "ejg."+ScsEJGBean.C_IDPERSONA+" IDLETRADO, "+
		       "p." + ScsPersonaJGBean.C_NOMBRE+" NOMBRE, "+
		       "p." + ScsPersonaJGBean.C_APELLIDO1+" APELLIDO1, "+
		       "p." + ScsPersonaJGBean.C_APELLIDO2+" APELLIDO2, "+
		       "p." + ScsPersonaJGBean.C_NIF + " || ' ' || "+
		       "p." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || " +
		       "p." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || "+
		       "p." + ScsPersonaJGBean.C_APELLIDO2 + " DATOS_INTERESADO," +
		       "p." + ScsPersonaJGBean.C_DIRECCION + " || ' ' || "+
		       "p." + ScsPersonaJGBean.C_CODIGOPOSTAL + " || ' ' || " +
		       "pb." + CenPoblacionesBean.C_NOMBRE + " DIRECCION_INTERESADO " +
			   
			   "from " + 
			   ScsEJGBean.T_NOMBRETABLA + " ejg, " +
			   ScsTurnoBean.T_NOMBRETABLA + " t," + 
			   ScsGuardiasTurnoBean.T_NOMBRETABLA + " g," + 
			   ScsPersonaJGBean.T_NOMBRETABLA + " p," +  
			   ScsTipoEJGBean.T_NOMBRETABLA + " tejg," + 
			   CenPoblacionesBean.T_NOMBRETABLA + " pb,"+
			   CenColegiadoBean.T_NOMBRETABLA + " c, "+
			   ScsAsistenciasBean.T_NOMBRETABLA+ " asis"+
			   ScsEJGDESIGNABean.T_NOMBRETABLA+ "ejgdesig"+
			   
			   " where asis."+ScsAsistenciasBean.C_EJGNUMERO+"(+)=ejg."+ScsEJGBean.C_NUMERO+
			   " and asis."+ScsAsistenciasBean.C_EJGANIO+"(+)=ejg."+ScsEJGBean.C_ANIO+
			   " and asis."+ScsAsistenciasBean.C_EJGIDTIPOEJG+"(+)=ejg."+ScsEJGBean.C_IDTIPOEJG+
			   " and ejgdesig."+ScsEJGDESIGNABean.C_NUMEROEJG+"(+)=ejg."+ScsEJGBean.C_NUMERO+
			   " and ejgdesig."+ScsEJGDESIGNABean.C_ANIOEJG+"(+)=ejg."+ScsEJGBean.C_ANIO+
			   " and ejgdesig."+ScsEJGDESIGNABean.C_IDTIPOEJG+"(+)=ejg."+ScsEJGBean.C_IDTIPOEJG+
			   " and ejgdesig."+ScsEJGDESIGNABean.C_IDINSTITUCION+"(+)=ejg."+ScsEJGBean.C_IDINSTITUCION;
			   
			
			
			
//			consulta = "select "+
//					   "ejg."+ScsEJGBean.C_IDINSTITUCION +","+
//					   "ejg."+ScsEJGBean.C_IDTIPOEJG+","+
//					   "ejg."+ScsEJGBean.C_NUMERO+","+
//					   "ejg."+ScsEJGBean.C_ANIO+","+
//				       "t."+ScsTurnoBean.C_NOMBRE +" TURNO,"+
//				       "g."+ScsGuardiasTurnoBean.C_NOMBRE +" GUARDIA,"+
//				       "to_char(ejg."+ScsEJGBean.C_FECHAAPERTURA+",'DD/MM/YYYY') "+ScsEJGBean.C_FECHAAPERTURA+","+
//				       "ejg."+ScsEJGBean.C_ASISTENCIA_NUMERO+","+
//				       "ejg."+ScsEJGBean.C_ASISTENCIA_ANIO+","+
//				       "ejg."+ScsEJGBean.C_DESIGNA_NUMERO+","+
//				       "ejg."+ScsEJGBean.C_DESIGNA_ANIO+","+
//				       "ejg."+ScsEJGBean.C_OBSERVACIONES+","+
//				       "ejg."+ScsEJGBean.C_IDPERSONAJG+" IDINTERESADO,"+
//				       "ejg."+ScsEJGBean.C_IDPERSONA+" IDLETRADO, "+
//				       "p." + ScsPersonaJGBean.C_NIF + " || ' ' || "+
//				       "p." + ScsPersonaJGBean.C_NOMBRE + " || ' ' || " +
//				       "p." + ScsPersonaJGBean.C_APELLIDO1 + " || ' ' || "+
//				       "p." + ScsPersonaJGBean.C_APELLIDO2 + " DATOS_INTERESADO," +
//				       "p." + ScsPersonaJGBean.C_DIRECCION + " || ' ' || "+
//				       "p." + ScsPersonaJGBean.C_CODIGOPOSTAL + " || ' ' || " +
//				       "pb." + CenPoblacionesBean.C_NOMBRE + " DIRECCION_INTERESADO " +
//					   "from " + 
//					   ScsEJGBean.T_NOMBRETABLA + " ejg, " +
//					   ScsTurnoBean.T_NOMBRETABLA + " t," + 
//					   ScsGuardiasTurnoBean.T_NOMBRETABLA + " g," + 
//					   ScsPersonaJGBean.T_NOMBRETABLA + " p," +  
//					   ScsTipoEJGBean.T_NOMBRETABLA + " tejg," + 
//					   CenPoblacionesBean.T_NOMBRETABLA + " pb,"+
//					   CenColegiadoBean.T_NOMBRETABLA + " c";
				       
			
			/* realizamos la join con de las tablas que necesitamos */
			consulta += " and ejg." + ScsEJGBean.C_IDINSTITUCION + " = t." + ScsTurnoBean.C_IDINSTITUCION + "(+) and " + 
					    " ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + " = t." + ScsTurnoBean.C_IDTURNO + "(+) and " + 
					    " ejg." + ScsEJGBean.C_IDTIPOEJG + " = tejg." + ScsTipoEJGBean.C_IDTIPOEJG + " and " + 
					    " ejg." + ScsEJGBean.C_IDINSTITUCION + " = g." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "(+) and " + 
					    " ejg." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA + " = g." + ScsGuardiasTurnoBean.C_IDGUARDIA + "(+) and " +
					    " ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + " = g." + ScsGuardiasTurnoBean.C_IDTURNO + "(+) and " +
					    " p." + ScsPersonaJGBean.C_IDINSTITUCION + " (+)= ejg." + ScsEJGBean.C_IDINSTITUCION + " and " + 
					    " p." + ScsPersonaJGBean.C_IDPERSONA + " (+)= ejg." + ScsEJGBean.C_IDPERSONAJG + " and " + 
					    " p." + ScsPersonaJGBean.C_IDPOBLACION + "= pb." + CenPoblacionesBean.C_IDPOBLACION + "(+) and " + 
					    " ejg." + ScsEJGBean.C_IDINSTITUCION + " (+)= c." + CenColegiadoBean.C_IDINSTITUCION + " and " +
					    " ejg." + ScsEJGBean.C_IDPERSONA + " (+)= c." + CenColegiadoBean.C_IDPERSONA;					    
			
			// Y ahora concatenamos los criterios de búsqueda
			
			if ((datos.containsKey("FECHAAPERTURA")) && (!datos.get("FECHAAPERTURA").toString().equals("")))
				consulta += " and " + GstDate.dateBetweenDesdeAndHasta("ejg.FECHAAPERTURA",GstDate.getApplicationFormatDate("",datos.get("FECHAAPERTURA").toString()),GstDate.getApplicationFormatDate("",datos.get("FECHAAPERTURA").toString()));					
			if ((datos.containsKey("ANIO")) && (!datos.get("ANIO").toString().equals(""))) consulta += " and ejg.ANIO = " + datos.get("ANIO");
			if ((datos.containsKey("CREADODESDE")) && (!datos.get("CREADODESDE").toString().equals(""))) {
				if (datos.get("CREADODESDE").toString().equalsIgnoreCase("A")) consulta += " and ejg.ASISTENCIA_NUMERO IS NOT NULL";
				else if (datos.get("CREADODESDE").toString().equalsIgnoreCase("D")) consulta += " and ejg.DESIGNA_NUMERO IS NOT NULL";				
				else if (datos.get("CREADODESDE").toString().equalsIgnoreCase("S")) consulta += " and ejg.SOJ_NUMERO IS NOT NULL";
				else consulta+= " and ejg.ASISTENCIA_NUMERO IS NULL and EJG.DESIGNA_NUMERO IS NULL and ejg.SOJ_NUMERO IS NULL"; 
			};
			if ((datos.containsKey("GUARDIATURNO_IDTURNO")) && (!datos.get("GUARDIATURNO_IDTURNO").toString().equals(""))) consulta += " and ejg.GUARDIATURNO_IDTURNO = " + datos.get("GUARDIATURNO_IDTURNO");
			if ((datos.containsKey("GUARDIATURNO_IDGUARDIA")) && (!datos.get("GUARDIATURNO_IDGUARDIA").toString().equals(""))) consulta += " and ejg.GUARDIATURNO_IDGUARDIA = " + datos.get("GUARDIATURNO_IDGUARDIA");
			if ((datos.containsKey("IDPERSONA")) && (!datos.get("IDPERSONA").toString().equals(""))) consulta += " and c.NCOLEGIADO = " + datos.get("IDPERSONA");
			if ((datos.containsKey("NUMERO")) && (!datos.get("NUMERO").toString().equals(""))) consulta += " and ejg.NUMERO = " + datos.get("NUMERO");
			if ((datos.containsKey("IDTIPOEJG")) && (!datos.get("IDTIPOEJG").toString().equals(""))) consulta += " and ejg.IDTIPOEJG = " + datos.get("IDTIPOEJG");
			if ((datos.containsKey("IDTIPOEJGCOLEGIO")) && (!datos.get("IDTIPOEJGCOLEGIO").toString().equals(""))) consulta += " and ejg.IDTIPOEJGCOLEGIO = " + datos.get("IDTIPOEJGCOLEGIO");
			if ((datos.containsKey("IDINSTITUCION")) && (!datos.get("IDINSTITUCION").toString().equals(""))) consulta += " and ejg.IDINSTITUCION = " + datos.get("IDINSTITUCION");			
			if ((datos.containsKey("NIF")) && (!datos.get("NIF").toString().equals(""))) consulta += " and UPPER(p.NIF) = '" + datos.get("NIF").toString().toUpperCase() +"'";			
			
			if ((datos.containsKey("NOMBRE")) && (!datos.get("NOMBRE").toString().equals(""))) consulta += " and "+ComodinBusquedas.prepararSentenciaCompleta(((String)datos.get("NOMBRE")).trim(),"p.NOMBRE");
			
			if ((datos.containsKey("APELLIDO1")) && (!datos.get("APELLIDO1").toString().equals(""))) consulta += " and "+ComodinBusquedas.prepararSentenciaCompleta(((String)datos.get("APELLIDO1")).trim(),"p.APELLIDO1");
			
			if ((datos.containsKey("APELLIDO2")) && (!datos.get("APELLIDO2").toString().equals(""))) consulta += " and "+ComodinBusquedas.prepararSentenciaCompleta(((String)datos.get("APELLIDO2")).trim(),"p.APELLIDO2");
			
			if ((datos.containsKey("ESTADOEJG")) && (!datos.get("ESTADOEJG").toString().equals(""))) {
				
				/*consulta = "SELECT " +  ScsEJGBean.C_ANIO + ", " + ScsEJGBean.C_IDINSTITUCION + ", " + ScsEJGBean.C_IDTIPOEJG + ", TIPOEJG," +
				ScsEJGBean.C_NUMERO + ", " + ScsEJGBean.C_FECHAAPERTURA + ", TURNO, GUARDIA, ESTADO, " + ScsPersonaJGBean.C_NOMBRE + ", " + 
				ScsPersonaJGBean.C_APELLIDO1 + ", " + ScsPersonaJGBean.C_APELLIDO2 + " from (" + consulta + ") where estado = '" + datos.get("DESCRIPCIONESTADO") + "'";*/
				consulta+=" and (select F_SIGA_GETRECURSO(maestroes.DESCRIPCION, 1)  "+ 
					" from SCS_ESTADOEJG         estadoejg, "+ 
					"       SCS_MAESTROESTADOSEJG maestroes "+ 
					" WHERE estadoejg.IDINSTITUCION = ejg.IDINSTITUCION "+ 
					"     and estadoejg.IDTIPOEJG = ejg.IDTIPOEJG "+ 
					"    and estadoejg.ANIO = ejg.ANIO "+ 
					"     and estadoejg.NUMERO = ejg.NUMERO "+ 
					"     and maestroes.IDESTADOEJG = estadoejg.IDESTADOEJG "+ 
					"     and estadoejg.FECHAINICIO = "+ 
					"         (SELECT MAX(ultimoestado.FECHAINICIO) "+ 
					"           from SCS_ESTADOEJG ultimoestado "+ 
		            "           where ultimoestado.IDINSTITUCION = "+ 
					"                estadoejg.IDINSTITUCION "+ 
					"              and ultimoestado.IDTIPOEJG = estadoejg.IDTIPOEJG "+ 
					"               and ultimoestado.ANIO = estadoejg.ANIO "+ 
					"               and ultimoestado.NUMERO = estadoejg.NUMERO) "+ 
					"        and rownum = 1)='"+datos.get("DESCRIPCIONESTADO")+"'";
				 
			}
			
			// Criterio de ordenación
			consulta += " ORDER BY ejg." + ScsEJGBean.C_ANIO + ", ejg."+ ScsEJGBean.C_NUMERO;

			return this.selectGenerico(consulta);
		}


		/** 
	 * Recoge informacion sobre las EJGs para las cartas de interesados de EJG 
	 * @param  institucion - identificador de la institucion
	 * @param  tipoEJG - tipo de EJG
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Row getEJGdeSOJ(String institucion, String anio, String tipo, String numero) throws ClsExceptions,SIGAException {
        RowsContainer rc = new RowsContainer();
        Row fila = null;
	       try {

            String sql ="select "+
            "A."+ScsEJGBean.C_ANIO+","+
            "A."+ScsEJGBean.C_NUMERO+","+
            "A."+ScsEJGBean.C_IDTIPOEJG+","+
            UtilidadesMultidioma.getCampoMultidioma("B."+ScsTipoEJGBean.C_DESCRIPCION,this.usrbean.getLanguage())+","+
            "A."+ScsEJGBean.C_FECHAAPERTURA+","+
			"A."+ScsEJGBean.C_NUMEJG+
            " from "+ScsEJGBean.T_NOMBRETABLA +" A,"+ScsTipoEJGBean.T_NOMBRETABLA+" B,"+ScsSOJBean.T_NOMBRETABLA+" C"+
            " where c."+ScsSOJBean.C_IDINSTITUCION+"="+institucion+
            "   and C."+ScsSOJBean.C_ANIO+"="+anio+
            "   and C."+ScsSOJBean.C_IDTIPOSOJ+"="+tipo+
            "   and C."+ScsSOJBean.C_NUMERO+"="+numero+
			
			"   and A."+ScsEJGBean.C_ANIO+"=C."+ScsSOJBean.C_EJGANIO +
			"   and A."+ScsEJGBean.C_NUMERO+"=C."+ScsSOJBean.C_EJGNUMERO +
			"   and A."+ScsEJGBean.C_IDTIPOEJG+"=C."+ScsSOJBean.C_EJGIDTIPOEJG +
			
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
	
	public Vector getIdiomaInteresado (String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT A.CODIGOEXT AS LENGUAJE,P.IDPERSONA AS IDPERSONAJG" +
	            		" FROM SCS_PERSONAJG P, SCS_EJG EJG, ADM_LENGUAJES A" +
	            		" WHERE P.IDPERSONA=EJG.IDPERSONAJG" +
	            		" AND EJG.ANIO="+anio+""+
	            		" AND EJG.NUMERO="+numero+""+
	            		" AND EJG.IDTIPOEJG="+tipoEJG+""+
						" AND P.IDINSTITUCION=EJG.IDINSTITUCION"+
						" AND EJG.IDINSTITUCION="+institucion+"" +
						" AND P.IDLENGUAJE=A.IDLENGUAJE";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	            
   
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al obtener la informacion del idioma del interesado.");
	       }
	       return datos;                        
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
	
	public static String getEstadoEJGJSP (String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions,SIGAException {
		   String datos="";
	       try {
	            RowsContainer rc = new RowsContainer(); 

			    Hashtable codigos = new Hashtable();
			    int contador=0;
			    
	            String sql ="select maestroes.DESCRIPCION " +
                          " from SCS_ESTADOEJG         estadoejg, " +
                          "      SCS_MAESTROESTADOSEJG maestroes " ;
          	            contador++;
        				codigos.put(new Integer(contador),institucion);
        	            sql += " WHERE estadoejg.IDINSTITUCION =:"+contador;
        	            contador++;
        				codigos.put(new Integer(contador),tipoEJG);
        	            sql += "  and estadoejg.IDTIPOEJG =:" +contador;
        	            contador++;
        				codigos.put(new Integer(contador),anio);
        	            sql += "  and estadoejg.ANIO =:" + contador;
        	            contador++;
        				codigos.put(new Integer(contador),numero);
        	            sql += "  and estadoejg.NUMERO =:" + contador +
                          "  and maestroes.IDESTADOEJG = estadoejg.IDESTADOEJG " +
                          "  and estadoejg.IDESTADOPOREJG = " +
                          "      (SELECT MAX(ultimoestado.IDESTADOPOREJG) " +
                          "         from SCS_ESTADOEJG ultimoestado " +
                          "        where ultimoestado.IDINSTITUCION = " +
                          "              estadoejg.IDINSTITUCION " +
                          "          and ultimoestado.IDTIPOEJG = " +
                          "              estadoejg.IDTIPOEJG " +
                          "          and ultimoestado.ANIO = estadoejg.ANIO " +
                          "          and ultimoestado.NUMERO = estadoejg.NUMERO) " ;
          	            contador++;
        				codigos.put(new Integer(contador),"1");
        	            sql += "  and rownum =:"+contador;
														
	            if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos = (String)resultado.get("DESCRIPCION");
	               }
	            } 
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return datos;                        
	    }	
	
	/*
	public PaginadorBind getBusquedaMantenimientoEJG(Hashtable miHash, DefinirEJGForm miForm)throws ClsExceptions,SIGAException {
	    	    PaginadorBind paginador=null;
	       try {
	            RowsContainer rc = new RowsContainer(); 

			    Hashtable codigos = new Hashtable();
			    int contador=0;
			    
			    //contador++;
				//codigos.put(new Integer(contador),institucion);
				
				
						
	            paginador = new PaginadorBind(consulta, codigos);	
	            
	             
	       } catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al obtener la informacion del idioma del interesado.");
	       }
	       return paginador;                        

	}		*/
	
	public PaginadorBind getPaginadorBusquedaMantenimientoEJG(Hashtable miHash, DefinirEJGForm miForm, String idInstitucion)throws ClsExceptions,SIGAException {
	    	    PaginadorBind paginador=null;
	       try {
	            Hashtable htConsultaBind  = getBindBusquedaMantenimientoEJG(miHash,  miForm, TipoVentana.BUSQUEDA_EJG, idInstitucion);
	            String consulta = (String) htConsultaBind.get(keyBindConsulta);
	            Hashtable codigos = (Hashtable) htConsultaBind.get(keyBindCodigos);
						
	            paginador = new PaginadorBind(consulta, codigos);	
	            
	             
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
	        vDatos = this.selectGenericoBind(consulta, codigos); 
	        	
	         
	   } catch (Exception e) {
	   		throw new ClsExceptions (e, "Error en BBDD");
	   }
	
		return vDatos;
	}
	
	public Hashtable getBindBusquedaMantenimientoEJG(Hashtable miHash, DefinirEJGForm miForm, TipoVentana tipoVentana, String idInstitucion) throws ClsExceptions,SIGAException{
		
		// A raiz de la INC_07042_SIGA se revisan los criterios de busqueda eliminado el codigo comentado.
		// Estamos a 08/04/2010 version 1.4.2.3 del CVS

		Hashtable hashReturn = new Hashtable(); 

		Hashtable codigos = new Hashtable();
		int contador=0;
		
		boolean esComision=(miHash.containsKey("ESCOMISION") && UtilidadesString.stringToBoolean(miHash.get("ESCOMISION").toString()));
		
		//aalg: INC_0644_SIGA. Modificación de la query por los estados ejg
		// Estos son los campos que devuelve la select
		String consulta = "SELECT EJG." + ScsEJGBean.C_ANIO + ", " + 
			" EJG." + ScsEJGBean.C_IDINSTITUCION + ", " + 
			" EJG." + ScsEJGBean.C_IDTIPOEJG + ", " + 
			" EJG." + ScsEJGBean.C_IDFACTURACION + ", " +
			" EJG." + ScsEJGBean.C_FECHARATIFICACION + ", " + 
			" TIPOEJG." + ScsTipoEJGBean.C_DESCRIPCION +  " as TIPOEJG, " +
			" EJG." + ScsEJGBean.C_NUMEJG + ", " +
			" EJG." + ScsEJGBean.C_FECHAAPERTURA + ", " + 
			" EJG." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + ", " +
			" EJG." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA + ", " +
			" '' AS APELLIDO1, " +
			" '' AS APELLIDO2, " +
			" NVL(MEE." + ScsMaestroEstadosEJGBean.C_DESCRIPCION + ", '') AS DESC_ESTADO, " + 
			" EJG." + ScsEJGBean.C_NUMERO + ", " +
			" EJG." + ScsEJGBean.C_FECHAMODIFICACION + ", " +
			" EJG." + ScsEJGBean.C_SUFIJO;

		// Metemos las tablas implicadas en la select 
		consulta += " FROM " + ScsEJGBean.T_NOMBRETABLA + " EJG, "  + 
			ScsTipoEJGBean.T_NOMBRETABLA + " TIPOEJG, " + 
			CenColegiadoBean.T_NOMBRETABLA + " COLEGIADO, " + 
			ScsEstadoEJGBean.T_NOMBRETABLA + " ESTADO, " +
			ScsMaestroEstadosEJGBean.T_NOMBRETABLA + " MEE ";
		
		// Si se filtra por acta necesitamos la tabla
		if (((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals(""))) || 
			((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals("")))){
			consulta += "," +  ScsActaComisionBean.T_NOMBRETABLA + " ACTA "; 
		}

		// Se filtra por renuncia
		if (miForm.getIdRenuncia()!=null && !miForm.getIdRenuncia().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getIdRenuncia());
			consulta+=	", SCS_RENUNCIA  RENUNCIA "+
				" WHERE RENUNCIA.idrenuncia = EJG.Idrenuncia " +
					" AND RENUNCIA.idrenuncia = :" + contador +
					" AND EJG." + ScsEJGBean.C_IDTIPOEJG + " = TIPOEJG." + ScsTipoEJGBean.C_IDTIPOEJG + 
					" AND EJG." + ScsEJGBean.C_IDINSTITUCION + " = COLEGIADO." + CenColegiadoBean.C_IDINSTITUCION + "(+) " +
					" AND EJG." + ScsEJGBean.C_IDPERSONA + " = COLEGIADO." + CenColegiadoBean.C_IDPERSONA + "(+) ";   
              
		}else{	
			// 	Comenzamos a cruzar tablas
			consulta += " WHERE EJG." + ScsEJGBean.C_IDTIPOEJG + " = TIPOEJG." + ScsTipoEJGBean.C_IDTIPOEJG + 
				" AND EJG." + ScsEJGBean.C_IDINSTITUCION + " = COLEGIADO." + CenColegiadoBean.C_IDINSTITUCION + "(+) " +
				" AND EJG." + ScsEJGBean.C_IDPERSONA + " = COLEGIADO." + CenColegiadoBean.C_IDPERSONA + "(+) ";   
		}
		
		if(esComision){
			// jbd // La consulta es la misma para comision y no comision, pero en la comision forzamos a que exista un estado
			consulta += " AND EJG." + ScsEJGBean.C_IDINSTITUCION + " = ESTADO." + ScsEstadoEJGBean.C_IDINSTITUCION +
			" AND EJG." + ScsEJGBean.C_IDTIPOEJG + " = ESTADO." + ScsEstadoEJGBean.C_IDTIPOEJG +
		   	" AND EJG." + ScsEJGBean.C_NUMERO + " = ESTADO." + ScsEstadoEJGBean.C_NUMERO +
		   	" AND EJG." + ScsEJGBean.C_ANIO + " = ESTADO." + ScsEstadoEJGBean.C_ANIO +
		   	" AND " +
		   		" ( " + 
		   			" ESTADO.IDESTADOPOREJG IS NULL " + 
		   			" OR ESTADO.IDESTADOPOREJG = " +
			   			" F_SIGA_GET_ULTIMOESTADOPOREJG ( " +
			   				"  ESTADO." + ScsEstadoEJGBean.C_IDINSTITUCION + 
			   				", ESTADO." + ScsEstadoEJGBean.C_IDTIPOEJG +
			   				", ESTADO." + ScsEstadoEJGBean.C_ANIO +
			   				", ESTADO." + ScsEstadoEJGBean.C_NUMERO + 
			   			" ) " +
		   		" ) ";
			//aalg: INC_0644_SIGA. Modificación de la query por los estados ejg
			consulta += " and MEE."+ ScsMaestroEstadosEJGBean.C_IDESTADOEJG +"= ESTADO."+ ScsEstadoEJGBean.C_IDESTADOEJG;
		}else{
			//aalg: INC_0644_SIGA. Modificación de la query por los estados ejg
			consulta += " AND EJG." + ScsEJGBean.C_IDINSTITUCION + " = ESTADO." + ScsEstadoEJGBean.C_IDINSTITUCION + "(+) " +
					" AND EJG." + ScsEJGBean.C_IDTIPOEJG + " = ESTADO." + ScsEstadoEJGBean.C_IDTIPOEJG + "(+) " +
					" AND EJG." + ScsEJGBean.C_NUMERO + " = ESTADO." + ScsEstadoEJGBean.C_NUMERO + "(+) " +
					" AND EJG." + ScsEJGBean.C_ANIO + " = ESTADO." + ScsEstadoEJGBean.C_ANIO + "(+) " +
					" AND " +
					" ( " + 
					" ESTADO.IDESTADOPOREJG IS NULL " + 
					" OR ESTADO.IDESTADOPOREJG = " +
					" F_SIGA_GET_ULTIMOESTADOPOREJG ( " +
					"  ESTADO." + ScsEstadoEJGBean.C_IDINSTITUCION + 
					", ESTADO." + ScsEstadoEJGBean.C_IDTIPOEJG +
					", ESTADO." + ScsEstadoEJGBean.C_ANIO +
					", ESTADO." + ScsEstadoEJGBean.C_NUMERO + 
					" ) " +
					" ) ";
			//aalg: INC_0644_SIGA. Modificación de la query por los estados ejg
			consulta += " and MEE."+ ScsMaestroEstadosEJGBean.C_IDESTADOEJG +"(+) = ESTADO."+ ScsEstadoEJGBean.C_IDESTADOEJG;
		}
		// Parametros para poder reutilizar la busqueda EJG para busquedas CAJG
		if(TipoVentana.BUSQUEDA_PREPARACION_CAJG.equals(tipoVentana)){
			consulta += " AND (ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " NOT IN (7, 8, 9, 10, 11) OR ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " IS NULL) ";
			
		} else if (TipoVentana.BUSQUEDA_ANIADIR_REMESA.equals(tipoVentana)) {
			consulta += " AND ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " IN (" + ESTADOS_EJG.LISTO_COMISION.getCodigo() + ", " + ESTADOS_EJG.ESTADO_LISTO_COMISION_ACTUALIZAR_DESIGNACION.getCodigo() + ") ";
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


		// Y ahora concatenamos los criterios de búsqueda
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
			
			// Cruzamos la tabla de actas
			consulta += " AND ACTA." + ScsActaComisionBean.C_ANIOACTA + " = EJG." + ScsEJGBean.C_ANIOACTA;
			consulta += " AND ACTA." + ScsActaComisionBean.C_IDACTA + " = EJG." + ScsEJGBean.C_IDACTA;
			consulta += " AND ACTA." + ScsActaComisionBean.C_IDINSTITUCION + " = EJG." + ScsEJGBean.C_IDINSTITUCIONACTA;
			
			if ((miHash.containsKey("ANIOACTA")) && (!miHash.get("ANIOACTA").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIOACTA"));
				consulta += " AND ACTA." + ScsActaComisionBean.C_ANIOACTA + " = :" + contador;
			}
			
			if ((miHash.containsKey("NUMEROACTA")) && (!miHash.get("NUMEROACTA").toString().equals(""))) {
				contador++;
				codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"NUMEROACTA"));
				consulta += " AND ACTA." + ScsActaComisionBean.C_NUMEROACTA+ " = :"+contador;
			}
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
			consulta += " AND COLEGIADO.IDPERSONA = :" + contador;
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
				
			} else {
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
		
		// jbd // Consulta para el interesado
		// Hasta ahora se estaba haciendo una consulta independiente para cada campo del nombre
		// Lo optimo es meter todo en la misma consulta
		if (((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals("")))||
			((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals("")))||
			((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals("")))||
			((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals("")))){
			
			consulta += " AND (SELECT COUNT(1) " + 
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
				codigos.put(new Integer(contador), ((String)miHash.get("NIF")).trim() + "%");
				consulta += " AND LTRIM(UPPER(PJG.NIF), '0') LIKE LTRIM(UPPER(:"+contador+"), '0') ";						 
			}
			
			if ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals(""))){
				contador++;
    			consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("NOMBRE")).trim(), "UPPER(PJG.NOMBRE)", contador, codigos);
			}
			
			if ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals(""))){
				contador++; 
				consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO1")).trim(), "UPPER(PJG.apellido1)", contador, codigos);
			}
			
			if ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals(""))){
				contador++;
				consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO2")).trim(), "UPPER(PJG.apellido2)", contador, codigos);
			}
			
			consulta += ") >0 ";
		}

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
		}
		
		//aalg: INC_0644_SIGA. Modificación de la query por los estados ejg 
		if ((miHash.containsKey("ESTADOEJG")) && (!miHash.get("ESTADOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "ESTADOEJG"));
			consulta += " AND ESTADO." + ScsEstadoEJGBean.C_IDESTADOEJG + " = :" + contador;
			
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
			
		} else if ((miHash.containsKey("DESCRIPCIONESTADO")) && (!miHash.get("DESCRIPCIONESTADO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLanguage());
			consulta += " AND " +
				" ( " +
					" SELECT F_SIGA_GETRECURSO(maestroes.DESCRIPCION, :" + contador + ") DESCRIPCION " + 
					" FROM SCS_ESTADOEJG ESTADOEJG, " + 
						" SCS_MAESTROESTADOSEJG MAESTROES " + 
					" WHERE ESTADOEJG.IDINSTITUCION = EJG.IDINSTITUCION " + 
						" AND ESTADOEJG.IDTIPOEJG = EJG.IDTIPOEJG " + 
						" AND ESTADOEJG.ANIO = EJG.ANIO " + 
						" AND ESTADOEJG.NUMERO = EJG.NUMERO " + 
						" AND MAESTROES.IDESTADOEJG = ESTADOEJG.IDESTADOEJG " + 
						" AND ESTADOEJG.IDESTADOPOREJG = " +
							" ( " +
								" SELECT MAX(ULTIMOESTADO.IDESTADOPOREJG) " + 
								" FROM SCS_ESTADOEJG ULTIMOESTADO " + 
								" WHERE ULTIMOESTADO.IDINSTITUCION = ESTADOEJG.IDINSTITUCION " + 
									" AND ULTIMOESTADO.IDTIPOEJG =  ESTADOEJG.IDTIPOEJG " + 
									" AND ULTIMOESTADO.ANIO = ESTADOEJG.ANIO " + 
									" AND ULTIMOESTADO.NUMERO = ESTADOEJG.NUMERO" +
							" ) " +
						" AND ROWNUM = 1 " +
					" ) ";
			
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "DESCRIPCIONESTADO"));
			consulta += " = :" + contador;
			
		} else if(esComision) {
				// Si la comision deja vacio el estado le mostramos todos los que pueden ver ellos
				consulta += " AND MEE.VISIBLECOMISION = '1' ";
		}

		consulta += " ORDER BY TO_NUMBER(" + ScsEJGBean.C_ANIO + ") DESC, " +
			" TO_NUMBER(" + ScsEJGBean.C_NUMEJG + ") DESC ";

		hashReturn.put(keyBindConsulta,consulta);
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
			String anio, String numero,String idioma) throws ClsExceptions  
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
			
			sql.append(" nvl(ejg.numejg || '/' || substr(ejg.anio, 3, 2), '-') as NUM_SOLICITUD, ");
			sql.append(" ejg.numejg as NUMERO_EJG, ejg.anio as ANIO_EJG, ");
			
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
	public Vector getSolicitanteCalificacionEjgSalida (String idInstitucion, String anioEjg, String tipoEjg, String numeroEjg,
			String idPersonaJG,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select f_siga_getrecurso(Tgl.Descripcion,decode(Pjg.Idlenguaje,");			
			sql.append("null, "+idioma+ ",Pjg.Idlenguaje)) SITUACION_LABORAL,");
			sql.append(" nvl(pjg.nombre || ' ' || pjg.apellido1 || ' ' || pjg.apellido2, '-') as NOMBRE_APE_SOLIC, ");
			sql.append(" pjg.nif as NIFCIF_SOLIC, ");
			sql.append(" DECODE(pjg.DIRECCION, NULL, '-', ((SELECT (UPPER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+idioma+"), 1, 1))) || (LOWER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+idioma+"),2))) FROM CEN_TIPOVIA TV WHERE TV.IDTIPOVIA = pjg.IDTIPOVIA AND TV.IDINSTITUCION = fam.IDINSTITUCION) || ' ' || pjg.DIRECCION || ' ' || pjg.NUMERODIR || ' ' || pjg.ESCALERADIR || ' ' || pjg.PISODIR || ' ' || pjg.PUERTADIR)) as DIR_SOLIC, ");
			sql.append(" nvl(pjg.codigopostal, '-') as CP_SOLIC, ");
			sql.append(" nvl((SELECT F_SIGA_GETRECURSO(Nombre,decode(Pjg.Idlenguaje,null,"+idioma+",Pjg.Idlenguaje)) ");
			sql.append(" FROM CEN_POBLACIONES ");			
			sql.append(" WHERE IDPOBLACION = pjg.idpoblacion), ");
			sql.append(" '-') as POB_SOLIC, ");
			sql.append(" nvl((SELECT F_SIGA_GETRECURSO(Nombre,decode(Pjg.Idlenguaje,null,"+idioma+",Pjg.Idlenguaje)) ");
			sql.append("  From Cen_Provincias ");
			sql.append("  Where Idprovincia = Pjg.Idprovincia),'-')As Prov_Solic, ");         
			sql.append(" decode(Pjg.Idlenguaje,null,"+idioma+",Pjg.Idlenguaje) as IDLENGUAJE,");
			sql.append(" f_Siga_Getcodidioma(decode(Pjg.Idlenguaje,null,"+idioma+",Pjg.Idlenguaje)) As CODIGOLENGUAJE ");
			sql.append(" FROM scs_unidadfamiliarejg fam,scs_personajg pjg,SCS_TIPOGRUPOLABORAL tgl ");
			sql.append(" WHERE ");			
			sql.append(" fam.idinstitucion = tgl.idinstitucion(+) ");
			sql.append(" AND  fam.idtipogrupolab = tgl.idtipogrupolab(+) ");
			sql.append(" and pjg.idinstitucion = fam.idinstitucion ");
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
			
		}
		catch (Exception e) {
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
			
            sql.append(" des.idinstitucion = ejgd.idinstitucion ");
            sql.append(" and des.idturno = ejgd.idturno ");
            sql.append("  and des.anio = ejgd.aniodesigna ");
            sql.append(" and des.numero = ejgd.numerodesigna ");


			
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
			sql.append(" pro.Codigopostal AS PROCURADOR_CODIGOPOSTAL_D_J, ");
			sql.append(" (Select Provincia.Nombre ");
			sql.append("  From Cen_Provincias Provincia ");
			sql.append(" Where Provincia.Idprovincia = Pro.Idprovincia) As PROCURADOR_PROVINCIA_D_J, ");
			sql.append("  (Select Poblacion.Nombre ");
			sql.append("  From Cen_Poblaciones Poblacion, Cen_Provincias Provincia ");
			sql.append("  Where Poblacion.Idprovincia = Provincia.Idprovincia ");
			sql.append("  And Pro.Idprovincia = Poblacion.Idprovincia ");
			sql.append(" And Pro.Idpoblacion = Poblacion.Idpoblacion) As PROCURADOR_POBLACION_D_J ");
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
	public Vector getDatosInformeCalificacion (String idInstitucion, String tipoEjg,
			String anioEjg, String numeroEjg,String idioma, Hashtable registro, Vector vSalida, Vector infosolicitante) throws ClsExceptions  
	{	 
		
		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		try {			
			
			//Añadimos la descripcion de el tipo de colegio
			String idTipoColegio = (String)registro.get("IDTIPOEJGCOLEGIO");
			helperInformes.completarHashSalida(registro,getTipoColegioSalida(idInstitucion,idTipoColegio,"TIPOEJGCOLEGIO",idioma));
			
			//Añadimos los ingresos
			helperInformes.completarHashSalida(registro,getIngresosAnualesUnidadFamiliarEjgSalida(idInstitucion, 
					tipoEjg,anioEjg,numeroEjg,"INGRESOS"));
			
			//Añadimos la lista de solicitantes separados por ,
			helperInformes.completarHashSalida(registro,getSolicitantes(idInstitucion, 
					tipoEjg,anioEjg,numeroEjg,"TODOS_SOLICITANTES"));
			
			//Añadimos el nombre del turno de la guardia
			String idTurno = (String)registro.get("GUARDIATURNO_IDTURNO");
			helperInformes.completarHashSalida(registro,getTurnoEjgSalidaOficio(idInstitucion, 
					idTurno,"TURNO","ABREV_TURNO"));
			
			//Añadimos el nombre de la guardia
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
			helperInformes.completarHashSalida(registro,getDesignaCalificacionEjgSalida(idInstitucion, 
					tipoEjg,anioEjg,numeroEjg));
			
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
				helperInformes.completarHashSalida(registro,getColegiadoDesignaCalificacionEjgSalida(idInstitucionDesigna, 
					idTurnoDesigna,anioDesigna,numeroDesigna));
			}else{
				
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
			if(idComisaria!=null && !idComisaria.trim().equals("") && idInstitucion!=null && !idInstitucion.trim().equals("")) { 
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
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosInformeCalificacion");
		}
		return vSalida;
		
		
		
	}
	
	public Vector getEjgSalida (String idInstitucion, String tipoEjg, String anio, String numero) throws ClsExceptions {
			RowsContainer rc = new RowsContainer();
			Vector datos = new Vector();
			
			String sql = " SELECT EJGD.IDINSTITUCION DES_INSTITUCION, " +
				" EJGD.IDTURNO DES_IDTURNO, " +
				" EJGD.ANIODESIGNA DES_ANIO, " + 
				" EJGD.NUMERODESIGNA DES_NUMERO, " + 
				" F_SIGA_GETIDLETRADO_DESIGNA(d.idinstitucion,d.idturno,d.anio  , d.numero) IDPERSONADESIGNADO,"+
				" EJG.IDPERSONA IDPERSONATRAMITADOR, " +
				" EJG.idfundamentocalif, " +
				" EJG.IDPROCURADOR, " + 
				" EJG.IDINSTITUCION_PROC, "+ 
				" EJG.JUZGADO AS IDJUZGADO_DJ, " +
				" EJG.JUZGADOIDINSTITUCION AS JUZGADOIDINSTITUCION_DJ, " +
				" EJG.COMISARIA, " +
				" EJG.COMISARIAIDINSTITUCION, " + 
				" EJG.ANIO AS ANIO_EJG, " +
				" TO_CHAR(EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA_EJG, " +
				" EJG.OBSERVACIONES AS OBSERVACIONES, " +
				" EJG.OBSERVACIONES AS ASUNTO_EJG, " +
				" (SELECT observaciones FROM scs_designa des WHERE des.IDINSTITUCION = EJGD.Idinstitucion AND des.IDTURNO = EJGD.Idturno AND des.ANIO = ejgd.aniodesigna AND des.NUMERO = EJGD.Numerodesigna) as OBSERVACIONES_DESIGNA, " +			
				" TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL, " + 
				" TO_CHAR(SYSDATE, 'dd/mm/yyyy') AS MESACTUAL, " +
				" TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL, " +
				" EJG.IDTIPOEJG, " +
				" EJG.ANIO, " +
				" EJG.NUMEJG as NUMERO, " +
				" (EJG.ANIO || '/' || EJG.NUMEJG) as NUMERO_EJG, " +
				" EJG.IDPERSONA, " +			
				" EJG.CALIDAD, " +			
				" (SELECT Descripcion FROM Scs_Tipoencalidad WHERE Idinstitucion = Ejg.Idinstitucion AND Idtipoencalidad = Ejg.Idtipoencalidad) as CALIDAD_DJ_DESCRIPCION, " +  			
				" EJG.OBSERVACIONES AS ASUNTO_DEFENSA_JURIDICA, " +
				" EJG.DELITOS AS COM_DEL_DEFENSA_JURIDICA, " +
				" EJG.NUMERO_CAJG AS NUMERO_CAJG_DEFENSA_JURIDICA, " + 
				" EJG.ANIOCAJG AS ANIO_CAJG_DEFENSA_JURIDICA, " + 
				" EJG.NUMERODILIGENCIA AS NUMDILIGENCIA_DEFENSA_JURIDICA, " +
				" EJG.NUMEROPROCEDIMIENTO AS NUMPROCED_DEFENSA_JURIDICA, " +
				" EJG.ANIOPROCEDIMIENTO AS ANIOPROCED_DEFENSA_JURIDICA, " +
				" EJG.NUMEROPROCEDIMIENTO AS NUM_PROCEDIMIENTO_EJG, " +
				" Decode(EJG.ANIOPROCEDIMIENTO, null, EJG.NUMEROPROCEDIMIENTO, (EJG.NUMEROPROCEDIMIENTO || '/' || EJG.ANIOPROCEDIMIENTO))  AS NUMANIOPROCED_DEFENSA_JURIDICA, " +				
				" EJG.NIG, " + 
				" TO_CHAR(EJG.FECHA_DES_PROC,'dd-mm-yyyy') AS  FECHAEJG_PROCURADOR, " +
				" EJG.NUMERODESIGNAPROC AS NUMDESIGNA_PROCURADOR, " + 
				" EJG.idtipodictamenejg, " + 
				" TO_CHAR(EJG.fechadictamen,'dd-mm-yyyy') AS fechadictamen, " + 
				" EJG.dictamen, " + 
				" TO_CHAR(EJG.fecharesolucioncajg,'dd-mm-yyyy') AS fecharesolucioncajg, " + 
				" EJG.idtiporatificacionejg, " + 
				" TO_CHAR(EJG.fechanotificacion,'dd-mm-yyyy') AS fechanotificacion, " + 
				" EJG.refauto, " +
				" EJG.ratificaciondictamen, " +
				" TO_CHAR(EJG.fechaauto,'dd-mm-yyyy') AS fechaauto, " +
				" EJG.idtiporesolauto, " +
				" EJG.idtiposentidoauto, " + 
				" (SELECT pon.nombre FROM SCS_PONENTE pon WHERE pon.idPonente = EJG.idPONENTE AND pon.idInstitucion = EJG.IDINSTITUCION) as PONENTE, " + 			
				" (SELECT DESCRIPCION FROM SCS_TIPOEJGCOLEGIO TEC WHERE tec.IDINSTITUCION = EJG.IDINSTITUCION AND TEC.IDTIPOEJGCOLEGIO=EJG.IDTIPOEJGCOLEGIO) AS DESCRIPCIONTIPOEJGCOL, " +			
				" TO_CHAR(EJG.Fecharatificacion, 'dd-mm-yyyy') AS Fecharatificacion, " +			
				" TO_CHAR(EJG.FECHAPRESENTACION, 'dd-mm-yyyy') as FECHAPRESENTACION, " +
				" TO_CHAR(EJG.FECHALIMITEPRESENTACION, 'dd-mm-yyyy') as FECHALIMITEPRESENTACION, " +
				// Campos necesarios para las comucioncaciones de la comision
				// Nos quedamos con los digitos para saber la cantidad que se reduce
				" REGEXP_REPLACE((SELECT F_SIGA_GETRECURSO(r.descripcion, 1) FROM Scs_Tiporesolucion r WHERE r.Idtiporesolucion=ejg.idtiporatificacionejg),'[^[:digit:]]','') as REDUCCION, " +
				// Las fechas en letra
				" TO_CHAR(EJG.Fecharatificacion, 'dd/mm/yyyy') AS FECHARATIFICACIONLETRA, " + 
				" TO_CHAR(EJG.FECHAPRESENTACION, 'dd/mm/yyyy') AS FECHAPRESENTACIONLETRA, " + 
				" TO_CHAR(EJG.FECHALIMITEPRESENTACION,'dd/mm/yyyy') AS FECHALIMITEPRESENTACIONLETRA, " +
				" TO_CHAR(EJG.fechaauto,'dd/mm/yyyy') AS FECHAAUTO_LETRA, " +
				" TO_CHAR(EJG.fechanotificacion,'dd/mm/yyyy') AS FECHANOTIFICACIONLETRA, " +
				" TO_CHAR(EJG.fecharesolucioncajg,'dd/mm/yyyy') AS FECHARESOLUCIONCAJGLETRA, " +
				" TO_CHAR(EJG.FECHAAPERTURA,'dd/mm/yyyy') AS FECHAAPERTURA_EJGLETRA, " + 
				" TO_CHAR(SYSDATE,'dd/mm/yyyy') AS FECHAACTUALLETRA, " + 
				" FUND.TEXTOPLANTILLA, " +
				" FUND.TEXTOPLANTILLA2, " +
				" FUND.TEXTOPLANTILLA3, " +
				" FUND.TEXTOPLANTILLA4, " +
				" FUND.TEXTOPLANTILLA4, " +
				" FUND.DESCRIPCION AS FUNDAMENTO_JURIDICO_DESCR " +
			" FROM SCS_EJG EJG, " +
				" SCS_EJGDESIGNA EJGD, " +
				" SCS_TIPOFUNDAMENTOS FUND, scs_designa d " +
			" WHERE EJG.IDINSTITUCION = EJGD.IDINSTITUCION(+) " +
				" AND EJG.IDTIPOEJG = EJGD.IDTIPOEJG(+) " + 
				" AND EJG.ANIO = EJGD.ANIOEJG(+) " + 
				" AND EJG.NUMERO = EJGD.NUMEROEJG (+) " +
				" and  EJGD.IDINSTITUCION = d.idinstitucion(+) " +
				" AND  EJGD.Aniodesigna =  d.anio(+) " +
				" AND  EJGD.Idturno = d.idturno(+) " +
				" AND  EJGD.NUMERODESIGNA = d.numero(+) " +
				
				
				" AND fund.idfundamento(+)=ejg.idfundamentojuridico " + 
				" AND fund.idinstitucion(+)=ejg.idinstitucion " + 
				" AND EJG.idinstitucion = " + idInstitucion +
				" AND EJG.idtipoejg = " + tipoEjg +
				" AND EJG.anio = " + anio +
				" AND EJG.numero = " + numero;
			
	       try{    	   	    	   			
				 rc = this.find(sql);
	 			if (rc!=null){
					for (int i = 0; i < rc.size(); i++)	{
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable)fila.getRow(); 
						if (registro != null) 
							datos.add(registro);
					}
				}		       
			} catch (Exception e) {
				throw new ClsExceptions (e, "Error ScsEJGAdm.getEjgSalida.");	
			} 
			
			return datos;			
	}			
	
	private Vector getAsistenciaEjgSalida (String idInstitucion, String tipoEjg,
			String anio, String numero) throws ClsExceptions  
	{
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
			sql.append(" PROC.TELEFONO1 AS PROCURADOR_TELEFONO1 ");
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
	
	
	
	public Vector getInteresadosEjgSalida (String idInstitucion, String tipoEjg,
			String anio, String numero,String idioma, String idPersonaJG) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			/*htCodigos.put(new Integer(1), idInstitucion);
			htCodigos.put(new Integer(2), anio);
			htCodigos.put(new Integer(3), numero);
			htCodigos.put(new Integer(4), tipoEjg);
			keyContador = 4;*/
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			
			
			
			
			
			
			
			
			
			sql.append(" SELECT  ");
			
			sql.append(" INTERESADO.NOMBRE NOMBRE_D,INTERESADO.APELLIDO1 APELLIDO1_D,INTERESADO.APELLIDO2 APELLIDO2_D, ");
			
			sql.append(" INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' || ");
			sql.append("  INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO, ");
			sql.append(" NVL2(VIA.DESCRIPCION, F_SIGA_GETRECURSO(VIA.DESCRIPCION, "+idioma+"), '') || ");
			sql.append(" NVL2(PERSONAJG.DIRECCION, ' ' || PERSONAJG.DIRECCION, '') || ");
			sql.append(" NVL2(PERSONAJG.NUMERODIR, ' ' || PERSONAJG.NUMERODIR, '') || ");
			sql.append(" NVL2(PERSONAJG.ESCALERADIR, ' ' || PERSONAJG.ESCALERADIR, '') || ");
			sql.append(" NVL2(PERSONAJG.PISODIR, ' ' || PERSONAJG.PISODIR, '') || ");
			sql.append(" NVL2(PERSONAJG.PUERTADIR, ' ' || PERSONAJG.PUERTADIR, '') AS DOMICILIO_DEFENDIDO, ");
			
			
			sql.append(" INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO, ");
			sql.append(" INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO, ");
			sql.append(" INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO, ");
			sql.append(" INTERESADO.NIF AS NIF_DEFENDIDO, ");
			sql.append(" INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO, ");
			sql.append(" (SELECT tg.descripcion FROM Scs_Tipogrupolaboral tg  WHERE tg.idinstitucion = "+idInstitucion+" ");
			sql.append(" AND tg.idtipogrupolab = Interesado.IDTIPOGRUPOLAB) AS GRUPOLABORAL_DEFENDIDO, ");
			sql.append(" DECODE(INTERESADO.SEXO, null, null, 'M', ");
			sql.append(" 'gratuita.personaEJG.sexo.mujer', ");
			sql.append(" 'gratuita.personaEJG.sexo.hombre' ");
			sql.append(") AS SEXOINTERESADO, ");
			sql.append("	DECODE(INTERESADO.SEXO,'H','o','a') as O_A_INTERESADO,");
			sql.append("	DECODE(INTERESADO.SEXO,'H','el','la') as EL_LA_INTERESADO,");
			sql.append(" INTERESADO.IDLENGUAJE AS LENGUAJE_INTERESADO, ");			
			sql.append(" (Select  Descripcion " );
			sql.append("    From Scs_Tipoencalidad  ");
			sql.append("    Where Idinstitucion = INTERESADO.Idinstitucion ");
			sql.append("    And Idtipoencalidad = INTERESADO.Idtipoencalidad) as CALIDADINTERESADO,");  			
			
			
			
			sql.append(" DECODE(INTERESADO.ANIOEJG, NULL, NULL, INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG) AS NUMERO_EJG, ");
			sql.append(" F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE, ");
			sql.append(" INTERESADO.IDLENGUAJE AS IDLENGUAJE, ");
			sql.append(" ESTADOCIVIL.DESCRIPCION  as ESTADOCIVILDEFENDIDO, ");
			sql.append(" to_char(PERSONAJG.FECHANACIMIENTO, 'DD/MM/YYYY')  FECHANAC_DEFENDIDO, ");
			sql.append("  DECODE(regimen_conyugal,'G', 'gratuita.personaJG.regimen.literal.gananciales',");
            sql.append(" 'I', 'gratuita.personaJG.regimen.literal.indeterminado',");
            sql.append(" 'S', 'gratuita.personaJG.regimen.literal.separacion') as REGIMENCONYUGALDEFENDIDO,");
            sql.append(" PROFESION.DESCRIPCION PROFESIONDEFENDIDO,");
            
			
            sql.append(" PERSONAJG.IDPERSONA IDPERSONA ");
            sql.append(",PERSONAJG.IDREPRESENTANTEJG ");
            sql.append(",PERSONAJG.IDINSTITUCION, ");
            
            
            
            sql.append("DECODE(PERSONAJG.DIRECCION, null, null, 1) IDDIRECCION_PJG, ");
            
            sql.append("NVL(PERSONAJG.NOMBRE, '') NOMBRE_PJG, ");
            sql.append("NVL(PERSONAJG.APELLIDO1, '') APELLIDO1_PJG, ");
            sql.append("NVL(PERSONAJG.APELLIDO2, '') APELLIDO2_PJG, ");
            sql.append("(SELECT ST.NUMEROTELEFONO ");
            sql.append("FROM SCS_TELEFONOSPERSONA ST ");
            sql.append("WHERE ST.IDINSTITUCION = PERSONAJG.IDINSTITUCION ");
            sql.append("AND ST.IDPERSONA = PERSONAJG.IDPERSONA ");
            sql.append("AND ST.IDTELEFONO = 1) TELEFONO1_PJG, ");
            sql.append("(SELECT ST.NUMEROTELEFONO ");
            sql.append("FROM SCS_TELEFONOSPERSONA ST ");
            sql.append("WHERE ST.IDINSTITUCION = PERSONAJG.IDINSTITUCION ");
            sql.append("AND ST.IDPERSONA = PERSONAJG.IDPERSONA ");
            sql.append("AND ST.IDTELEFONO = 2) TELEFONO2_PJG, ");
            
            sql.append("(SELECT MAX(ST.NUMEROTELEFONO) ");
            sql.append("FROM SCS_TELEFONOSPERSONA ST ");
            sql.append("WHERE ST.IDINSTITUCION = PERSONAJG.IDINSTITUCION ");
            sql.append("AND ST.IDPERSONA = PERSONAJG.IDPERSONA ");
            sql.append("AND ST.PREFERENTESMS = 1) MOVIL_PJG, ");
            
            sql.append("PERSONAJG.Fax               FAX_PJG, ");
            sql.append("PERSONAJG.CORREOELECTRONICO CORREOELECTRONICO_PJG ");
            
            
			sql.append(" FROM V_SIGA_INTERESADOS_EJG    INTERESADO, SCS_PERSONAJG PERSONAJG, ");
			sql.append(" CEN_ESTADOCIVIL ESTADOCIVIL, SCS_PROFESION PROFESION , CEN_TIPOVIA VIA");
  
			sql.append(" WHERE ");
			
			sql.append(" VIA.IDINSTITUCION(+) = PERSONAJG.IDINSTITUCION ");
			sql.append(" AND VIA.IDTIPOVIA(+) = PERSONAJG.IDTIPOVIA ");
			
			
			sql.append(" AND PERSONAJG.IDINSTITUCION = INTERESADO.IDINSTITUCION ");
			sql.append(" AND PERSONAJG.IDPERSONA=INTERESADO.IDPERSONAJG ");	
			sql.append(" AND ESTADOCIVIL.IDESTADOCIVIL(+)=PERSONAJG.IDESTADOCIVIL ");
			sql.append(" AND PERSONAJG.IDPROFESION= PROFESION.IDPROFESION(+) ");
			
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
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInteresadosEjgSalida.");
		}
	}
	public Vector getInteresadosEjg(String idInstitucion, String tipoEjg,
			String anio, String numero) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			/*htCodigos.put(new Integer(1), idInstitucion);
			htCodigos.put(new Integer(2), anio);
			htCodigos.put(new Integer(3), numero);
			htCodigos.put(new Integer(4), tipoEjg);
			keyContador = 4;*/
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			
			sql.append(" SELECT  ");
            sql.append(" INTERESADO.IDPERSONAJG IDPERSONA ");
            sql.append(",INTERESADO.IDINSTITUCION ");
			sql.append(" FROM V_SIGA_INTERESADOS_EJG    INTERESADO ");
			sql.append(" WHERE ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" INTERESADO.IDINSTITUCION = :");
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
			
           
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInteresadosEjg.");
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
	
	public Vector getDatosRegionConyuge(String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg,String idioma) throws ClsExceptions {	 

		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), tipoEjg);
		h.put(new Integer(3), anioEjg);
		h.put(new Integer(4), numeroEjg);
//		String textoNoConsta = UtilidadesMultidioma.getCampoMultidioma("informes.sjcs.parentesco.noConsta",idioma);
		String textoSolicitante = UtilidadesMultidioma.getDatoMaestroIdioma("gratuita.busquedaSOJ.literal.solicitante",idioma);
		StringBuffer sql = new StringBuffer();		
		sql.append(" SELECT decode(PER2.APELLIDO2, null,PER2.APELLIDO1,PER2.APELLIDO1 || ' ' || PER2.APELLIDO2) || ', ' || ");
		sql.append(" 	PER2.NOMBRE as CONYUGE_UF,PER2.NIF as NIF_CONYUGE_UF,(select f_siga_getrecurso(descripcion, "+idioma+") from scs_parentesco paren where paren.idinstitucion = ufa.idinstitucion and paren.idparentesco = UFA.Idparentesco) as PARENTESCO_UF, ");
		sql.append(" 	trunc(months_between(sysdate, PER2.FECHANACIMIENTO) / 12) as EDAD_UF,  UFA.IDINSTITUCION, UFA.IDTIPOEJG, UFA.ANIO,UFA.NUMERO, UFA.IDPERSONA IDPERSONAJG, EJG3.CALIDAD AS CALIDAD, ");
		sql.append("    EJG3.IDTIPOENCALIDAD AS IDTIPOENCALIDAD,EJG3.CALIDADIDINSTITUCION AS CALIDADIDINSTITUCION,");
		sql.append("    decode(UFA.SOLICITANTE,1, '"+textoSolicitante+"', null) AS SOLICITANTE, ");
		sql.append("	PER2.NOMBRE, PER2.APELLIDO1,PER2.APELLIDO2, PER2.DIRECCION, PER2.CODIGOPOSTAL, POB.NOMBRE AS NOMBRE_POB,");
		sql.append("    PROV.NOMBRE AS NOMBRE_PROV, PAIS.NOMBRE AS NOMBRE_PAIS, EJG3.ANIO AS ANIOEJG, EJG3.NUMEJG, PER2.SEXO, PER2.IDLENGUAJE,");
		sql.append("    (SELECT TEL2.NUMEROTELEFONO FROM SCS_TELEFONOSPERSONA TEL2 WHERE TEL2.IDINSTITUCION = UFA.IDINSTITUCION AND TEL2.IDPERSONA = UFA.IDPERSONA AND ROWNUM < 2) AS TELEFONO, PER2.OBSERVACIONES");
		sql.append(" FROM SCS_UNIDADFAMILIAREJG UFA, SCS_PERSONAJG PER2, CEN_POBLACIONES POB, CEN_PROVINCIAS PROV, CEN_PAIS PAIS, SCS_EJG EJG3 ");
		sql.append(" WHERE UFA.IDINSTITUCION = PER2.IDINSTITUCION");
		sql.append(" 	AND UFA.idpersona <> ejg3.idpersonajg");
		sql.append(" 	AND UFA.idparentesco = "+ClsConstants.TIPO_CONYUGE);
		sql.append("  	AND UFA.IDPERSONA = PER2.IDPERSONA");
		sql.append("  	AND UFA.IDINSTITUCION = EJG3.IDINSTITUCION");
		sql.append("  	AND UFA.IDTIPOEJG = EJG3.IDTIPOEJG");
		sql.append("  	AND UFA.ANIO = EJG3.ANIO");
		sql.append("  	AND UFA.NUMERO = EJG3.NUMERO");
		sql.append("  	AND PER2.IDPOBLACION = POB.IDPOBLACION(+)");
		sql.append("  	AND PER2.IDPROVINCIA = PROV.IDPROVINCIA(+)");
		sql.append(" 	AND PER2.IDPAIS = PAIS.IDPAIS(+) ");
		sql.append("    AND UFA.IDINSTITUCION = :1");
		sql.append("    AND UFA.IDTIPOEJG = :2");
		sql.append("    AND UFA.ANIO = :3");
		sql.append("    AND UFA.NUMERO = :4");
		sql.append(" ORDER BY IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO, IDPERSONAJG ");

		try {
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			return helperInformes.ejecutaConsultaBind(sql.toString(), h);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion en getDatosRegionConyuge");
		}
	}
	
		public Vector getDatosRegionUF(String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg,String idioma) throws ClsExceptions {	 

		RowsContainer rc = new RowsContainer();
		Vector datos = new Vector();
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), tipoEjg);
		h.put(new Integer(3), anioEjg);
		h.put(new Integer(4), numeroEjg);
			
		String textoNoConsta = UtilidadesMultidioma.getDatoMaestroIdioma("informes.sjcs.parentesco.noConsta",idioma);
		String textoSolicitante = UtilidadesMultidioma.getDatoMaestroIdioma("gratuita.busquedaSOJ.literal.solicitante",idioma);
		
		
		StringBuffer sql = new StringBuffer();		
		sql.append(" SELECT decode(PER2.APELLIDO2, null,PER2.APELLIDO1,PER2.APELLIDO1 || ' ' || PER2.APELLIDO2) || ', ' || ");
		sql.append("    PER2.NOMBRE as NOMBRE_UF, PER2.NIF as NIF_UF, decode(ufa.idparentesco, null, '"+textoNoConsta+"',(select f_siga_getrecurso(descripcion, "+idioma+") from scs_parentesco paren where paren.idinstitucion = ufa.idinstitucion and paren.idparentesco = UFA.Idparentesco)) as PARENTESCO_UF,");
		sql.append("    trunc(months_between(sysdate, PER2.FECHANACIMIENTO) / 12) as EDAD_UF,  UFA.IDINSTITUCION, UFA.IDTIPOEJG, UFA.ANIO,UFA.NUMERO, UFA.IDPERSONA IDPERSONAJG, EJG3.CALIDAD AS CALIDAD, ");
		sql.append("    EJG3.IDTIPOENCALIDAD AS IDTIPOENCALIDAD,EJG3.CALIDADIDINSTITUCION AS CALIDADIDINSTITUCION, ");
		sql.append("    decode(UFA.SOLICITANTE,1, '"+textoSolicitante+"', null) AS SOLICITANTE, ");
		sql.append("    PER2.NOMBRE, PER2.APELLIDO1,PER2.APELLIDO2, PER2.DIRECCION, PER2.CODIGOPOSTAL, POB.NOMBRE AS NOMBRE_POB,");
		sql.append("    PROV.NOMBRE AS NOMBRE_PROV, PAIS.NOMBRE AS NOMBRE_PAIS, EJG3.ANIO AS ANIOEJG, EJG3.NUMEJG, PER2.SEXO, PER2.IDLENGUAJE,");
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
			throw new ClsExceptions(e, "Error al obtener la informacion en getDatosRegionConyuge");
		}
	}

	public Vector getDatosInformeEjg (String idInstitucion, String tipoEjg, String anioEjg, String numeroEjg, boolean isSolicitantes,boolean isAcontrarios, String idDestinatario,String tipoDestinatario,boolean generarInformeSinDireccion, String tipoDestinatarioInforme, boolean agregarEtiqDesigna) throws ClsExceptions {	 
		Vector vSalida = null;		
		Hashtable htFuncion = new Hashtable();
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		ScsUnidadFamiliarEJGAdm admUniFam = new ScsUnidadFamiliarEJGAdm(this.usrbean);
		ScsPersonaJGAdm scsPersonaJGAdm = null; 
		String idPersonaJG = null;
		String idContrario = null;

		boolean isAlgunInformeNoGenerado = false;
		boolean isAlgunRepresentanteLegal =false;
		if(tipoDestinatario!=null){
			if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
				idPersonaJG = idDestinatario;
			}else if(tipoDestinatario.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSCONTRARIOSJG)){
				idContrario = idDestinatario;
			} 
			
			// la siguiente linea es un parche: desde SMSs no viene el tipoDestinatarioInforme
			tipoDestinatarioInforme = (tipoDestinatarioInforme == null) ? tipoDestinatario : tipoDestinatarioInforme;
		}

		try {

			vSalida = new Vector();
			Vector vEjg = getEjgSalida(idInstitucion, tipoEjg,anioEjg, numeroEjg); 
			String idiomainforme="ES";
			for (int j = 0; j < vEjg.size(); j++) {
				Hashtable registro = (Hashtable) vEjg.get(j);
				String idioma = "1";
			
				//Ini Mod MJM se incluyen las etiquetas del informe de designas si el
                //EJG tiene designas relacionadas
                //Se agregan las etiquetas del informe de designas al informe de EJG
                //Si se está invocando al método desde designas o desde EJG envío telemático
                //no hay que sacar estas etiquetas
           
                if((String)registro.get("DES_NUMERO")!=null && (!((String)registro.get("DES_NUMERO")).trim().equalsIgnoreCase(""))){

                    if(agregarEtiqDesigna==true)
                    {
                        ScsDesignaAdm scsDesignaAdm = new ScsDesignaAdm(this.usrbean);
                        boolean agregarEtiqEJG=false;
                       
                        Vector designasRelVector = scsDesignaAdm.getDatosSalidaOficio((String)registro.get("DES_INSTITUCION"),(String)registro.get("DES_IDTURNO"),(String)registro.get("DES_ANIO"),(String)registro.get("DES_NUMERO"),null,false,idPersonaJG,this.usrbean.getLanguage(),this.usrbean.getLanguageExt(),tipoDestinatarioInforme, agregarEtiqEJG);
                       
                        Hashtable designasRel = new Hashtable();
                       
                        if(designasRelVector.size()>0){
                           
                            for (int dv = 0; dv < designasRelVector.size(); dv++) {
                           
                                Hashtable designasRelAux = (Hashtable) designasRelVector.get(dv);
                               
                                Enumeration e = designasRelAux.keys();
                               
                                while (e.hasMoreElements())
                                {
                                    String keyEjgRel =  (String) e.nextElement();
                                   
                                    String claveNew = "DESIGNARELEJG_"+ keyEjgRel;
                                   
                                    if(designasRelAux.get(keyEjgRel) instanceof String){
                                   
                                        String valor = (String)designasRelAux.get(keyEjgRel);
                                        designasRel.put(claveNew, valor);
       
                                   
                                    //Si es un area del informe
                                    }else if(designasRelAux.get(keyEjgRel) instanceof Vector){
                                       
                                        Vector areasRenombrada = new Vector();
                                        Vector areaVector = (Vector) designasRelAux.get(keyEjgRel);
                                       
                                       
                                       
                                        if(areaVector.size()>0){
                                           
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

				if(tipoDestinatarioInforme.equals(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)){
					if(registro.get("DES_ANIO")!=null && !((String)registro.get("DES_ANIO")).equals("") ){
						Vector colegiadoDestinatarios = getLetradoDesignadoEjg((String)registro.get("DES_INSTITUCION"), (String)registro.get("DES_IDTURNO"), (String)registro.get("DES_ANIO"), (String)registro.get("DES_NUMERO"));
						if(colegiadoDestinatarios!=null && colegiadoDestinatarios.size()>0){
							Hashtable colegiadoDestinatario = (Hashtable)colegiadoDestinatarios.get(0);
							registro.putAll(colegiadoDestinatario);
							String idPersonaDesignada = (String)colegiadoDestinatario.get("IDPERSONA_DESIGNA");
							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							helperInformesAdm.setIdiomaInforme((String)registro.get("DES_INSTITUCION"), idPersonaDesignada, AdmInformeBean.TIPODESTINATARIO_CENPERSONA,registro, usrbean);
							idioma = (String)registro.get("idioma");
							idiomainforme= (String)registro.get("idiomaExt");

							if(idPersonaDesignada!=null && !idPersonaDesignada.trim().equalsIgnoreCase("")){
								helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucion, idPersonaDesignada,"LETRADO_DESIGNADO"));
								String sexoLetradoEjg  = (String)registro.get("SEXO_ST_LETRADO_DESIGNADO");
								sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);					
								String o_a = (String)registro.get("O_A");
								String el_la = (String)registro.get("EL_LA");
								registro.put("SEXO_LETRADO_DESIGNADO", sexoLetradoEjg);
								registro.put("O_A_LETRADO_DESIGNADO", o_a);
								registro.put("EL_LA_LETRADO_DESIGNADO", el_la);
								Vector dirCorreo = getDireccionLetradoSalidaCorreo(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO");
								if(dirCorreo.size() > 0 && ((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")!=null &&!((String)((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")).trim().equals("") ){
									helperInformes.completarHashSalida(registro,dirCorreo);
								}else{
									helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));
								}

								helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));			

								String pobLetradoEjg = (String)registro.get("POBLACION_LETRADO_DESIGNADO");
								if(pobLetradoEjg==null ||pobLetradoEjg.trim().equalsIgnoreCase("")){
									String idPobLetradoEjg = (String)registro.get("ID_POBLACION_LETRADO_DESIGNADO");
									helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetradoEjg,"POBLACION_LETRADO_DESIGNADO"));
									String idProvLetradoEjg = (String)registro.get("ID_PROVINCIA_LETRADO_DESIGNADO");
									if(idProvLetradoEjg!=null && !idProvLetradoEjg.trim().equalsIgnoreCase(""))
										helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetradoEjg,"PROVINCIA_LETRADO_DESIGNADO"));
									else
										UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
								}else{
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

								registro.put("CORREOELECTRONICO_DEST", (String) registro.get("EMAIL_LETRADO_DESIGNADO"));
								registro.put("PAGINAWEB_DEST", (String) registro.get("PAGINAWEB_LETRADO_DESIGNADO"));
								registro.put("POBLACIONEXTRANJERA_DEST", (String) registro.get("PEXTRANJERA_LETRADO_DESIGNADO"));
								registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("POBLACION_LETRADO_DESIGNADO"));
								registro.put("NOMBRE_PROVINCIA_DEST", (String) registro.get("PROVINCIA_LETRADO_DESIGNADO"));


								////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							}
						}
					}else{
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

					Vector vDefendidos = getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
					Vector contrariosEjgVector = getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);

					if(isSolicitantes || isAcontrarios){
						if(isSolicitantes){
							registro.put("NOMBRE_CONTRARIO", "");
							registro.put("DOMICILIO_CONTRARIO", "");
							registro.put("CP_CONTRARIO", "");
							registro.put("POBLACION_CONTRARIO", "");
							registro.put("PROVINCIA_CONTRARIO", "");
							registro.put("O_A_CONTRARIO", "");
							registro.put("NIF_CONTRARIO", "");
							registro.put("TELEFONO1_CONTRARIO", "");
							if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
								registro.put("contrarios", contrariosEjgVector);
							}	

							if(vDefendidos!=null && vDefendidos.size()>0){
								if(((String)( ((Hashtable) vDefendidos.get(0)).get("IDPERSONA"))).trim().equalsIgnoreCase(""))continue;
								for (int k = 0; k < vDefendidos.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);
									registro.putAll(registroDefendido);
									String Idpersona=(String)registroDefendido.get("IDPERSONA");		


									registroDefendido  = getregistrodatosEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,registro,agregarEtiqDesigna);

									// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
									try{   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
										if (Idpersona!=null&&(!Idpersona.trim().equals(""))){								
											Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,Idpersona);
											if(vDestinatario!=null && vDestinatario.size()>0){
												Hashtable destinatario = (Hashtable) vDestinatario.get(0);
												clone.putAll(destinatario);										
											}
										}

									}catch (Exception e) {

									}
									clone.putAll(registroDefendido);
									vSalida.add(clone);

								}  // END FOR

							}else{
								continue;
							}	
						}
						if(isAcontrarios){
							actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
							if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
								for (int k = 0; k < contrariosEjgVector.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable registroContrario = (Hashtable) contrariosEjgVector.get(k);
									clone.putAll(registroContrario);
									vSalida.add(clone);

								}  // END FOR

							}else{
								continue;
							}	
						}

					}else{

						/**Idiomas del Solicitante principal si lo tiene**/
						Hashtable hastejg = new Hashtable();
						hastejg.put(ScsEJGBean.C_IDINSTITUCION, idInstitucion);
						hastejg.put(ScsEJGBean.C_IDTIPOEJG,tipoEjg);
						hastejg.put(ScsEJGBean.C_ANIO,anioEjg);
						hastejg.put(ScsEJGBean.C_NUMERO,numeroEjg);					
						if(scsPersonaJGAdm==null)
							scsPersonaJGAdm = new ScsPersonaJGAdm(this.usrbean);	

						
						actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
						
						if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
							registro.put("contrarios", contrariosEjgVector);
						}else{
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
						registro.put("NIF_DEFENDIDO", "");
						registro.put("NOMBRE_DEFENDIDO", "");
						registro.put("FECHANAC_DEFENDIDO", "");
						registro.put("ESTADOCIVIL_DEFENDIDO", "");
						registro.put("DOMICILIO_DEFENDIDO", "");
						registro.put("CP_DEFENDIDO", "");
						registro.put("POBLACION_DEFENDIDO", "");
						registro.put("TELEFONO1_DEFENDIDO", "");
						registro.put("PROVINCIA_DEFENDIDO", "");
						registro.put("SEXO_INTERESADO", "");
						registro.put("LENGUAJE_INTERESADO", "");
						registro.put("CALIDAD_INTERESADO", "");
						//registro.put("CODIGOLENGUAJE", "");
						registro.put("PROFESION_DEFENDIDO", "");
						registro.put("REGIMENCONYUGAL_DEFENDIDO", "");

						registro.put("NOMBRE_CONTRARIO", "");
						registro.put("DOMICILIO_CONTRARIO", "");
						registro.put("CP_CONTRARIO", "");
						registro.put("POBLACION_CONTRARIO", "");
						registro.put("PROVINCIA_CONTRARIO", "");
						registro.put("O_A_CONTRARIO", "");
						registro.put("NIF_CONTRARIO", "");
						registro.put("TELEFONO1_CONTRARIO", "");
						if(generarInformeSinDireccion ||(registro.get("IDDIRECCION_DEST")!=null&& !((String)registro.get("IDDIRECCION_DEST")).equals("")) )
							vSalida.add(registro);
						else
							isAlgunInformeNoGenerado = true;
					}
				}else if(tipoDestinatarioInforme.equals("S") || tipoDestinatarioInforme.equals(EnvDestinatariosBean.TIPODESTINATARIO_SCSPERSONAJG)){
					//					Sacamos los interesados el EJG  y los recorremos

					Vector vDefendidos = getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
					Vector contrariosEjgVector = getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);
					registro.put("NOMBRE_CONTRARIO", "");
					registro.put("DOMICILIO_CONTRARIO", "");
					registro.put("CP_CONTRARIO", "");
					registro.put("POBLACION_CONTRARIO", "");
					registro.put("PROVINCIA_CONTRARIO", "");
					registro.put("O_A_CONTRARIO", "");
					registro.put("NIF_CONTRARIO", "");
					registro.put("TELEFONO1_CONTRARIO", "");

					if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
						registro.put("contrarios", contrariosEjgVector);
					}	

					if(vDefendidos!=null && vDefendidos.size()>0){
						for (int k = 0; k < vDefendidos.size(); k++) {

							Hashtable clone = (Hashtable) registro.clone();
							Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);

							HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
							idPersonaJG=(String)registroDefendido.get("IDPERSONA");
							helperInformesAdm.setIdiomaInforme(idInstitucion,idPersonaJG, AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG,clone, usrbean);
							idioma = (String)clone.get("idioma");
							idiomainforme= (String)clone.get("idiomaExt");
							Vector defendidoVector = getInteresadosEjgSalida(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, idPersonaJG);
							//solo hay uno ya que filtramos por PK
							Hashtable registroDefendidoConDatos = (Hashtable) defendidoVector.get(0);
							clone.putAll(registroDefendidoConDatos);
							registroDefendido  = getregistrodatosEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,registro,agregarEtiqDesigna);


							// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
							try{   // aunque no tenga datos y puede dar error al comunicar a un NO defendido

								Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
								if(vDestinatario!=null && vDestinatario.size()>0){
									Hashtable destinatario = (Hashtable) vDestinatario.get(0);
									clone.putAll(destinatario);										
								}


							}catch (Exception e) {

							}




							clone.putAll(registroDefendido);
							///////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
							if(registroDefendido.get("IDREPRESENTANTEJG")!=null && !registroDefendido.get("IDREPRESENTANTEJG").equals("")){
								isAlgunRepresentanteLegal = true;
								if(scsPersonaJGAdm==null)
									scsPersonaJGAdm = new ScsPersonaJGAdm(this.usrbean);
								Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG((String)clone.get("IDREPRESENTANTEJG"),(String)clone.get("IDINSTITUCION"));
								clone.put("NOMBRE_DEST", (String) representanteLegalDefendido.get("NOMBRE_PJG"));
								clone.put("NOMBRE_DEST_MAYUS", ((String) representanteLegalDefendido.get("NOMBRE_PJG")).toUpperCase());
								clone.put("APELLIDO1_DEST", (String) representanteLegalDefendido.get("APELLIDO1_PJG"));
								clone.put("APELLIDO1_DEST_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO1_PJG")).toUpperCase());
								clone.put("APELLIDO2_DEST", (String) representanteLegalDefendido.get("APELLIDO2_PJG"));
								clone.put("APELLIDO2_DEST_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO2_PJG")).toUpperCase());
								clone.put("SEXO_DEST", (String) representanteLegalDefendido.get("SEXO_PJG"));
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
							
								clone.put("CORREOELECTRONICO_DEST", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
							
								clone.put("NOMBRE_POBLACION_DEST", (String) representanteLegalDefendido.get("POBLACION_PJG"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) representanteLegalDefendido.get("PROVINCIA_PJG"));

							}else{

								clone.put("NOMBRE_DEST", (String) clone.get("NOMBRE_PJG"));
								clone.put("NOMBRE_DEST_MAYUS", ((String) clone.get("NOMBRE_PJG")).toUpperCase());
								clone.put("APELLIDO1_DEST", (String) clone.get("APELLIDO1_PJG"));
								clone.put("APELLIDO1_DEST_MAYUS", ((String) clone.get("APELLIDO1_PJG")).toUpperCase());
								clone.put("APELLIDO2_DEST", (String) clone.get("APELLIDO2_PJG"));
								clone.put("APELLIDO2_DEST_MAYUS", ((String) clone.get("APELLIDO2_PJG")).toUpperCase());
								clone.put("SEXO_DEST", (String) clone.get("SEXOINTERESADO"));
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
								//									clone.put("FAX2_DEST", (String) representanteLegalDefendido.get("FAX_PJG"));
								clone.put("CORREOELECTRONICO_DEST", (String) clone.get("CORREOELECTRONICO_PJG"));
								//								clone.put("PAGINAWEB_DEST", (String) clone.get("PAGINAWEB"));
								//									clone.put("POBLACIONEXTRANJERA_DEST", (String) clone.get("POBLACIONEXTRANJERA"));
								clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("POBLACION_DEFENDIDO"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) clone.get("PROVINCIA_DEFENDIDO"));
								//								clone.put("NOMBRE_PAIS_DEST", (String) clone.get("NOMBRE_PAIS"));


							}
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							if(generarInformeSinDireccion ||(clone.get("IDDIRECCION_DEST")!=null&& !((String)clone.get("IDDIRECCION_DEST")).equals("")) ){
								actualizarDatosFundamentoJuridico(idioma, clone);
								//Aniadimos los datos del colegiado tramitador del ejg				
								String idLetradoTramitadorEjg  = (String)registro.get("IDPERSONATRAMITADOR");
								actualizarDatosLetradoTramitador(idLetradoTramitadorEjg,idInstitucion,clone);

								actualizarDatosFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);
								actualizarDatosFechaReunionActa(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);



								vSalida.add(clone);
							}
							else
								isAlgunInformeNoGenerado = true;
						}  // END FOR

					}else{

						continue;

					}	

				}else if(tipoDestinatarioInforme.equals("X")){

					Vector contrariosEjgVector = getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);
					registro.put("PARRAFO_LETRADO_PROCURADOR", "");
					registro.put("CODIGOLENGUAJE", idiomainforme);
					registro.put("NIF_DEFENDIDO", "");
					registro.put("NOMBRE_DEFENDIDO", "");
					registro.put("FECHANAC_DEFENDIDO", "");
					registro.put("ESTADOCIVIL_DEFENDIDO", "");
					registro.put("DOMICILIO_DEFENDIDO", "");
					registro.put("CP_DEFENDIDO", "");
					registro.put("POBLACION_DEFENDIDO", "");
					registro.put("TELEFONO1_DEFENDIDO", "");
					registro.put("PROVINCIA_DEFENDIDO", "");
					registro.put("SEXO_INTERESADO", "");
					registro.put("LENGUAJE_INTERESADO", "");
					registro.put("CALIDAD_INTERESADO", "");
					registro.put("PROFESION_DEFENDIDO", "");
					registro.put("REGIMENCONYUGAL_DEFENDIDO", "");

					if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
						for (int k = 0; k < contrariosEjgVector.size(); k++) {
							Hashtable clone = (Hashtable) registro.clone();
							Hashtable registroContrario = (Hashtable) contrariosEjgVector.get(k);
							clone.putAll(registroContrario);
							if(registroContrario.get("IDREPRESENTANTEJG")!=null && !registroContrario.get("IDREPRESENTANTEJG").equals("")){
								isAlgunRepresentanteLegal = true;
								if(scsPersonaJGAdm==null)
									scsPersonaJGAdm = new ScsPersonaJGAdm(this.usrbean);
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
								clone.put("SEXO_DEST", (String) representanteLegalContrario.get("SEXO_PJG"));
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
								clone.put("FAX2_DEST", (String) representanteLegalContrario.get("FAX_PJG"));
								clone.put("CORREOELECTRONICO_DEST", (String) representanteLegalContrario.get("CORREOELECTRONICO_PJG"));
								clone.put("NOMBRE_POBLACION_DEST", (String) representanteLegalContrario.get("POBLACION_PJG"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) representanteLegalContrario.get("PROVINCIA_PJG"));

							}else{
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
								clone.put("SEXO_DEST", (String) clone.get("SEXO_PJG"));
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
								clone.put("CORREOELECTRONICO_DEST", (String) clone.get("CORREOELECTRONICO_PJG"));
								clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("POBLACION_PJG"));
								clone.put("NOMBRE_PROVINCIA_DEST", (String) clone.get("PROVINCIA_PJG"));

							}
							Vector vDefendidos = getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
							
							actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, clone);
							
							actualizarDatosFundamentoJuridico(idioma, clone);

							//Aniadimos los datos del colegiado tramitador del ejg				
							String idLetradoTramitadorEjg  = (String)registro.get("IDPERSONATRAMITADOR");
							actualizarDatosLetradoTramitador(idLetradoTramitadorEjg,idInstitucion,clone);
							actualizarDatosFechaRemitidoComision(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);
							actualizarDatosFechaReunionActa(idInstitucion, tipoEjg, anioEjg, numeroEjg, idioma, registro);
							
							if(generarInformeSinDireccion ||(clone.get("IDDIRECCION_DEST")!=null&& !((String)clone.get("IDDIRECCION_DEST")).equals("")) )
								vSalida.add(clone);
							else
								isAlgunInformeNoGenerado = true;
						}  // END FOR

					}else{
						continue;
					}	



					//					Sacamos los CONTRARIOS y los recorremos
				}else if(tipoDestinatarioInforme.equals("J") || tipoDestinatarioInforme.equals("P")){
					//					Sacamos el Juzgado

					HelperInformesAdm helperInformesAdm = new HelperInformesAdm();
					helperInformesAdm.setIdiomaInforme(idInstitucion, null, AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO,registro, usrbean);
					idioma = (String)registro.get("idioma");
					idiomainforme= (String)registro.get("idiomaExt");


					if(registro.get("DES_ANIO")!=null && !((String)registro.get("DES_ANIO")).equals("") ){
						Vector colegiadoDestinatarios = getLetradoDesignadoEjg((String)registro.get("DES_INSTITUCION"), (String)registro.get("DES_IDTURNO"), (String)registro.get("DES_ANIO"), (String)registro.get("DES_NUMERO"));
						if(colegiadoDestinatarios!=null && colegiadoDestinatarios.size()>0){
							Hashtable colegiadoDestinatario = (Hashtable)colegiadoDestinatarios.get(0);
							registro.putAll(colegiadoDestinatario);
							String idPersonaDesignada = (String)colegiadoDestinatario.get("IDPERSONA_DESIGNA");


							if(idPersonaDesignada!=null && !idPersonaDesignada.trim().equalsIgnoreCase("")){
								helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucion, idPersonaDesignada,"LETRADO_DESIGNADO"));
								String sexoLetradoEjg  = (String)registro.get("SEXO_ST_LETRADO_DESIGNADO");
								sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);					
								String o_a = (String)registro.get("O_A");
								String el_la = (String)registro.get("EL_LA");
								registro.put("SEXO_LETRADO_DESIGNADO", sexoLetradoEjg);
								registro.put("O_A_LETRADO_DESIGNADO", o_a);
								registro.put("EL_LA_LETRADO_DESIGNADO", el_la);
								Vector dirCorreo = getDireccionLetradoSalidaCorreo(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO");
								if(dirCorreo.size() > 0 && ((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")!=null &&!((String)((Hashtable)dirCorreo.get(0)).get("IDDIRECCION_LETRADO_DESIGNADO")).trim().equals("") ){
									helperInformes.completarHashSalida(registro,dirCorreo);
								}else{
									helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));
								}

								helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idPersonaDesignada,idInstitucion,"LETRADO_DESIGNADO"));			

								String pobLetradoEjg = (String)registro.get("POBLACION_LETRADO_DESIGNADO");
								if(pobLetradoEjg==null ||pobLetradoEjg.trim().equalsIgnoreCase("")){
									String idPobLetradoEjg = (String)registro.get("ID_POBLACION_LETRADO_DESIGNADO");
									helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetradoEjg,"POBLACION_LETRADO_DESIGNADO"));
									String idProvLetradoEjg = (String)registro.get("ID_PROVINCIA_LETRADO_DESIGNADO");
									if(idProvLetradoEjg!=null && !idProvLetradoEjg.trim().equalsIgnoreCase(""))
										helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetradoEjg,"PROVINCIA_LETRADO_DESIGNADO"));
									else
										UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
								}else{
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

					Vector vDefendidos = getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
					Vector contrariosEjgVector = getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,idContrario);

					if(isSolicitantes || isAcontrarios){
						if(isSolicitantes){
							registro.put("NOMBRE_CONTRARIO", "");
							registro.put("DOMICILIO_CONTRARIO", "");
							registro.put("CP_CONTRARIO", "");
							registro.put("POBLACION_CONTRARIO", "");
							registro.put("PROVINCIA_CONTRARIO", "");
							registro.put("O_A_CONTRARIO", "");
							registro.put("NIF_CONTRARIO", "");
							registro.put("TELEFONO1_CONTRARIO", "");
							if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
								registro.put("contrarios", contrariosEjgVector);
							}	

							if(vDefendidos!=null && vDefendidos.size()>0){
								if(((String)( ((Hashtable) vDefendidos.get(0)).get("IDPERSONA"))).trim().equalsIgnoreCase(""))continue;
								for (int k = 0; k < vDefendidos.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);
									registro.putAll(registroDefendido);
									String Idpersona=(String)registroDefendido.get("IDPERSONA");		


									registroDefendido  = getregistrodatosEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,registro,agregarEtiqDesigna);

									// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
									try{   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
										if (Idpersona!=null&&(!Idpersona.trim().equals(""))){								
											Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,Idpersona);
											if(vDestinatario!=null && vDestinatario.size()>0){
												Hashtable destinatario = (Hashtable) vDestinatario.get(0);
												clone.putAll(destinatario);										
											}
										}

									}catch (Exception e) {

									}
									clone.putAll(registroDefendido);
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
									clone.put("CORREOELECTRONICO_DEST", (String) clone.get("EMAIL_JUZGADO"));
									clone.put("PAGINAWEB_DEST", "");
									clone.put("POBLACIONEXTRANJERA_DEST","");
									clone.put("NOMBRE_POBLACION_DEST", (String) clone.get("POBLACION_JUZGADO"));
									clone.put("NOMBRE_PROVINCIA_DEST", "");

									if(generarInformeSinDireccion ||(clone.get("DOMICILIO_DEST")!=null&& !((String)clone.get("DOMICILIO_DEST")).equals("")) )
										vSalida.add(clone);

								}  // END FOR

							}
						}
						if(isAcontrarios){
							actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);

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

							registro.put("CORREOELECTRONICO_DEST", (String) registro.get("EMAIL_JUZGADO"));
							registro.put("PAGINAWEB_DEST", "");
							registro.put("POBLACIONEXTRANJERA_DEST","");
							registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("POBLACION_JUZGADO"));
							registro.put("NOMBRE_PROVINCIA_DEST", "");



							if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
								for (int k = 0; k < contrariosEjgVector.size(); k++) {
									Hashtable clone = (Hashtable) registro.clone();
									Hashtable registroContrario = (Hashtable) contrariosEjgVector.get(k);
									clone.putAll(registroContrario);
									if(generarInformeSinDireccion ||(clone.get("DOMICILIO_DEST")!=null&& !((String)clone.get("DOMICILIO_DEST")).equals("")) )
										vSalida.add(clone);
									else
										isAlgunInformeNoGenerado = true;


								}  // END FOR

							}else{
								continue;
							}	
						}

					}else{
						actualizarDefendidos(idInstitucion, tipoEjg, anioEjg, numeroEjg, idPersonaJG, vDefendidos, idioma, idiomainforme, registro);
						
						if(contrariosEjgVector!=null && contrariosEjgVector.size()>0){
							registro.put("contrarios", contrariosEjgVector);
						}

						registro.put("PARRAFO_LETRADO_PROCURADOR", "");

						registro.put("CODIGOLENGUAJE", idiomainforme);
						registro.put("NIF_DEFENDIDO", "");
						registro.put("NOMBRE_DEFENDIDO", "");
						registro.put("FECHANAC_DEFENDIDO", "");
						registro.put("ESTADOCIVIL_DEFENDIDO", "");
						registro.put("DOMICILIO_DEFENDIDO", "");
						registro.put("CP_DEFENDIDO", "");
						registro.put("POBLACION_DEFENDIDO", "");
						registro.put("TELEFONO1_DEFENDIDO", "");
						registro.put("PROVINCIA_DEFENDIDO", "");
						registro.put("SEXO_INTERESADO", "");
						registro.put("LENGUAJE_INTERESADO", "");
						registro.put("CALIDAD_INTERESADO", "");
						//registro.put("CODIGOLENGUAJE", "");
						registro.put("PROFESION_DEFENDIDO", "");
						registro.put("REGIMENCONYUGAL_DEFENDIDO", "");

						registro.put("NOMBRE_CONTRARIO", "");
						registro.put("DOMICILIO_CONTRARIO", "");
						registro.put("CP_CONTRARIO", "");
						registro.put("POBLACION_CONTRARIO", "");
						registro.put("PROVINCIA_CONTRARIO", "");
						registro.put("O_A_CONTRARIO", "");
						registro.put("NIF_CONTRARIO", "");
						registro.put("TELEFONO1_CONTRARIO", "");

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

						registro.put("CORREOELECTRONICO_DEST", (String) registro.get("EMAIL_JUZGADO"));
						registro.put("PAGINAWEB_DEST", "");
						registro.put("POBLACIONEXTRANJERA_DEST","");
						registro.put("NOMBRE_POBLACION_DEST", (String) registro.get("POBLACION_JUZGADO"));
						registro.put("NOMBRE_PROVINCIA_DEST", "");





						if(generarInformeSinDireccion ||(registro.get("DOMICILIO_DEST")!=null&& !((String)registro.get("DOMICILIO_DEST")).equals("")) )
							vSalida.add(registro);
						else
							isAlgunInformeNoGenerado = true;
					}

				}

			}//fin del for.
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosInformeEjg");
		}
		if(vSalida!=null && vSalida.size()>0){
			Hashtable primerRegistro = (Hashtable) vSalida.get(0);
			primerRegistro.put("isAlgunRepresentanteLegal", isAlgunRepresentanteLegal);
			primerRegistro.put("isAlgunInformeNoGenerado", isAlgunInformeNoGenerado);

		}

		return vSalida;
	}
	private void actualizarDefendidos(String idInstitucion,String tipoEjg,String anioEjg,String numeroEjg,String idPersonaJG,Vector vDefendidos, String idioma, String idiomaInforme, Hashtable registro) throws ClsExceptions{

		if(vDefendidos!=null && vDefendidos.size()>0){
			for (int k = 0; k < vDefendidos.size(); k++) {
				Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);
				if(((String)( ((Hashtable) vDefendidos.get(0)).get("IDPERSONA"))).equalsIgnoreCase(""))continue;
				String idPersona=(String)registroDefendido.get("IDPERSONA");						
	
				registroDefendido  = getregistrodatosEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG,registro,false);
				/**Para saaber en que idioma se tiene que imprimer la carta de oficio**/
				registroDefendido.put("CODIGOLENGUAJE", idiomaInforme);
	
				try{   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
					if (idPersona!=null&&(!idPersona.trim().equals(""))){
						ScsUnidadFamiliarEJGAdm admUniFam = new ScsUnidadFamiliarEJGAdm(this.usrbean);
						Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersona);
						if(vDestinatario!=null && vDestinatario.size()>0){
							Hashtable destinatario = (Hashtable) vDestinatario.get(0);
							registroDefendido.putAll(destinatario);										
						}
					}								
				}catch (Exception e) {
	
				}							
			}
	
			registro.put("defendido", vDefendidos);
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
			UtilidadesHash.set(registro, "SEXO_LETRADO", "");
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

	public Hashtable getregistrodatosEjg(String idInstitucion, String tipoEjg,
		String anioEjg, String numeroEjg,String idioma,String idPersonaJG, Hashtable registro, boolean agregarEtiqDesigna) throws ClsExceptions {
	
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
			/**Etiquetas de información de defendidos que dependen del idioma que se le pase **/
			estadoCivilDefendido = (String)registro.get("ESTADOCIVILDEFENDIDO");		
			if (estadoCivilDefendido!=null && !estadoCivilDefendido.trim().equals("")){
				htFuncion.put(new Integer(1), estadoCivilDefendido);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "ESTADOCIVIL_DEFENDIDO"));
			}else{
				registro.put("ESTADOCIVIL_DEFENDIDO", "");
			}
			
			sexoInteresado = (String)registro.get("SEXOINTERESADO");		
			if (sexoInteresado!=null && !sexoInteresado.trim().equals("")){
				htFuncion.put(new Integer(1), sexoInteresado);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO_ETIQUETA", "SEXO_INTERESADO"));
			}else{
				registro.put("SEXO_INTERESADO", "");
				registro.put("O_A_INTERESADO", "o");
				registro.put("EL_LA_INTERESADO", "el");
			}
			
			calidadInteresado = (String)registro.get("CALIDADINTERESADO");		
			if (calidadInteresado!=null && !calidadInteresado.trim().equals("")){
				htFuncion.put(new Integer(1), calidadInteresado);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "CALIDAD_INTERESADO"));
			}else{
				registro.put("CALIDAD_INTERESADO", "");
			}
			
			regimenConyugalInteresado = (String)registro.get("REGIMENCONYUGALDEFENDIDO");		
			if (regimenConyugalInteresado!=null && !regimenConyugalInteresado.trim().equals("")){
				htFuncion.put(new Integer(1), regimenConyugalInteresado);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO_ETIQUETA", "REGIMEN_CONYUGALDEFENDIDO"));
			}else{
				registro.put("REGIMEN_CONYUGALDEFENDIDO", "");
			}
			
			tipoGrupoLaboral = (String)registro.get("GRUPOLABORAL_DEFENDIDO");		
			if (tipoGrupoLaboral!=null && !tipoGrupoLaboral.trim().equals("")){
				htFuncion.put(new Integer(1), tipoGrupoLaboral);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "GRUPOLABORAL_DEFENDIDO"));
			}else{
				registro.put("GRUPOLABORAL_DEFENDIDO", "");
			}
			
			 profesionDefendido = (String)registro.get("PROFESIONDEFENDIDO");		
			if (profesionDefendido!=null && !profesionDefendido.trim().equals("")){
				htFuncion.put(new Integer(1), profesionDefendido);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "PROFESION_DEFENDIDO"));
			}else{
				registro.put("PROFESION_DEFENDIDO", "");
			}						
			/**Fin de Etiquetas de información de defendidos**/	
						
			/**Calidad defensa juridica en el idioma del letrado cuando no hay interesados o solicitantes**/
			 descripcionCalidad  = (String)registro.get("CALIDAD_DJ_DESCRIPCION");		
			if (descripcionCalidad!=null && !descripcionCalidad.trim().equals("")){
				htFuncion.put(new Integer(1), descripcionCalidad);
				htFuncion.put(new Integer(2), idioma);				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "CALIDAD_DEFENSA_JURIDICA"));
			}else{
				registro.put("CALIDAD_DEFENSA_JURIDICA", "");
			}
		
			/**Calidad TIPO_EJG_COLEGIO que depende del idioma **/
			descripcionTipoEjgcol  = (String)registro.get("DESCRIPCIONTIPOEJGCOL");	
			if (descripcionTipoEjgcol!=null && !descripcionTipoEjgcol.trim().equals("")){
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), descripcionTipoEjgcol);
				htFuncion.put(new Integer(2), idioma);							
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "F_SIGA_GETRECURSO", "TIPO_EJG_COLEGIO"));
			
			}else {
					registro.put("TIPO_EJG_COLEGIO", "");
			}
		
			/**Fechas en Letras para que aparezcan en el idioma que se le pasa**/							
			fecharatificacion  = (String)registro.get("FECHARATIFICACIONLETRA");
			if (fecharatificacion!=null && !fecharatificacion.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fecharatificacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHARATIFICACION_LETRA"));
			}else {
					registro.put("FECHARATIFICACION_LETRA", "");
			}
	
	
			fechaPresentacion  = (String)registro.get("FECHAPRESENTACIONLETRA");
			if (fechaPresentacion!=null && !fechaPresentacion.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaPresentacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAPRESENTACION_LETRA"));
			}else {
					registro.put("FECHAPRESENTACION_LETRA", "");
			}
			
			fechaLimitePresentacion  = (String)registro.get("FECHALIMITEPRESENTACIONLETRA");							
				if (fechaLimitePresentacion!=null && !fechaLimitePresentacion.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaLimitePresentacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHALIMITEPRESENTACION_LETRA"));
			}else {
					registro.put("FECHALIMITEPRESENTACION_LETRA", "");
			}
	
			
				
			/*if (fechaAsistenciaLetra!=null && !fechaAsistenciaLetra.trim().equals("")){						
									htFuncion =  new Hashtable();
									htFuncion.put(new Integer(1), fechaAsistenciaLetra);
									htFuncion.put(new Integer(2), "m");
									htFuncion.put(new Integer(3), idioma);								
									helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHA_ASISTENCIA_LETRA"));
								}else {
										registro.put("FECHA_ASISTENCIA_LETRA", "");
								}*/
				
			fechaAutoLetra  = (String)registro.get("FECHAAUTOLETRA");					
			if (fechaAutoLetra!=null && !fechaAutoLetra.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaAutoLetra);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAAUTO_LETRA"));
			}else {
					registro.put("FECHAAUTO_LETRA", "");
			}
				
			fechaNotificacion  = (String)registro.get("FECHANOTIFICACIONLETRA");							
			if (fechaNotificacion!=null && !fechaNotificacion.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaNotificacion);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHANOTIFICACION_LETRA"));
			}else {
					registro.put("FECHANOTIFICACION_LETRA", "");
			}
				
			fechaResolucionCajg  = (String)registro.get("FECHARESOLUCIONCAJGLETRA");							
			if (fechaResolucionCajg!=null && !fechaResolucionCajg.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaResolucionCajg);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHARESOLUCIONCAJG_LETRA"));
			}else {
					registro.put("FECHARESOLUCIONCAJG_LETRA", "");
			}
				
			fechaAperturaEjg  = (String)registro.get("FECHAAPERTURA_EJGLETRA");					
			if (fechaAperturaEjg!=null && !fechaAperturaEjg.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaAperturaEjg);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAAPERTURA_EJG_LETRA"));
				}else {
					registro.put("FECHAAPERTURA_EJG_LETRA", "");
				}
				
			fechaActual  = (String)registro.get("FECHAACTUALLETRA");							
			if (fechaActual!=null && !fechaActual.trim().equals("")){						
				htFuncion =  new Hashtable();
				htFuncion.put(new Integer(1), fechaActual);
				htFuncion.put(new Integer(2), "m");
				htFuncion.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htFuncion, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHAACTUAL_LETRA"));
			}else {
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
			Vector contrariosEjg = getContrariosEjg(idInstitucion, tipoEjg, anioEjg, numeroEjg,null);		

			String listaNombreContrarios="";	
			String listaDomicilioContrarios="";
			String listaCpContrarios="";
			String listaPoblacionContrarios="";
			String listaProvinciaContrarios="";
			String listaOaContrarios="";
			String listaNifContrarios="";
			String listaTelefono1Contrarios="";
			
			for (int i = 0; i < contrariosEjg.size(); i++) {					
				Hashtable registroContrariosEjg = (Hashtable) contrariosEjg.get(i);
							    
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
				
				if (domicilioContrario != null) {
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
				if(poblacionLetradoAsistencia==null ||poblacionLetradoAsistencia.trim().equalsIgnoreCase("")){
					
					String idPoblacionLetradoAsistencia = (String)registro.get("ID_POBLACION_LET_ASIST");
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPoblacionLetradoAsistencia,"POBLACION_LET_ASIST"));
					
					String idProvinciaLetradoAsistencia = (String)registro.get("ID_PROVINCIA_LET_ASIST");
					if(idProvinciaLetradoAsistencia!=null && !idProvinciaLetradoAsistencia.trim().equalsIgnoreCase(""))
						helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvinciaLetradoAsistencia,"PROVINCIA_LET_ASIST"));
					else
						UtilidadesHash.set(registro, "PROVINCIA_LET_ASIST", "");									
				}else{
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
				
				if(procurador!=null && !procurador.trim().equalsIgnoreCase(""))						
					 registro.put("PROCURADOR",procurador);
				 else
						UtilidadesHash.set(registro, "PROCURADOR", "");	
				
				if(ProProcurador!=null && !ProProcurador.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_PROVINCIA",ProProcurador);
				else
					UtilidadesHash.set(registro, "PROCURADOR_PROVINCIA", "");
					
				if(PobProcurador!=null && !PobProcurador.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_POBLACION", PobProcurador);
				else
					UtilidadesHash.set(registro, "PROCURADOR_POBLACION", "");
				
				if(codigopostal!=null && !codigopostal.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_CP", codigopostal);
				else
					UtilidadesHash.set(registro, "PROCURADOR_CP", "");
					
				if(domicilioProcurador!=null && !domicilioProcurador.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DOMICILIO", domicilioProcurador);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DOMICILIO", "");
				
				if(telefonoProcurador!=null && !telefonoProcurador.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_TELEFONO1", telefonoProcurador);
				else
					UtilidadesHash.set(registro, "PROCURADOR_TELEFONO1", "");
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
					
				if(procuradordj!=null && !procuradordj.trim().equalsIgnoreCase(""))						
					registro.put("PROCURADOR_DEFENSA_JURIDICA",procuradordj);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DEFENSA_JURIDICA", "");	
					
				if(ncolegiado!=null && !ncolegiado.trim().equalsIgnoreCase(""))						
					registro.put("PROCURADOR_DJ_NCOLEGIADO",ncolegiado);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DJ_NCOLEGIADO", "");										
					
				if(Procuradordjtel1!=null && !Procuradordjtel1.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DJ_TELEFONO1",Procuradordjtel1);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DJ_TELEFONO1", "");
					
				if(Procuradordjtel2!=null && !Procuradordjtel2.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DJ_TELEFONO2", Procuradordjtel2);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DJ_TELEFONO2", "");
					
				if(domiciliodj!=null && !domiciliodj.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_DOMICILIO_D_J", domiciliodj);
				else
					UtilidadesHash.set(registro, "PROCURADOR_DOMICILIO_D_J", "");
				
				if(codigopostalprocuradorejg!=null && !codigopostalprocuradorejg.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_CODIGOPOSTAL_D_J", codigopostalprocuradorejg);
				else
					UtilidadesHash.set(registro, "PROCURADOR_CODIGOPOSTAL_D_J", "");
					
				if(provincia!=null && !provincia.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_PROVINCIA_D_J", provincia);
				else
					UtilidadesHash.set(registro, "PROCURADOR_PROVINCIA_D_J", "");
				
				if(poblacion!=null && !poblacion.trim().equalsIgnoreCase(""))
					registro.put("PROCURADOR_POBLACION_D_J", poblacion);
				else
					UtilidadesHash.set(registro, "PROCURADOR_POBLACION_D_J", "");
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
	
			if (datosprocuradorContrariodj.size()==0){
				UtilidadesHash.set(registro, "PROCURADOR_DJ_CONTRARIO", "");	
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_DOMICI_D_J", "");	
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_PROVIN_D_J", "");
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_POBLA_D_J", "");
				UtilidadesHash.set(registro, "PROCURADOR_CONTRA_CP_D_J", "");
			}else{			
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
				
					if(procuradorcontrariodj!=null && !procuradorcontrariodj.trim().equalsIgnoreCase(""))						
						registro.put("PROCURADOR_DJ_CONTRARIO",procuradorcontrariodj);
					else
						UtilidadesHash.set(registro, "PROCURADOR_DJ_CONTRARIO", "");	
				
					if(domiciliocontrio!=null && !domiciliocontrio.trim().equalsIgnoreCase(""))						
					 registro.put("PROCURADOR_CONTRA_DOMICI_D_J",domiciliocontrio);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_DOMICI_D_J", "");						
					
					if(provinciacontrario!=null && !provinciacontrario.trim().equalsIgnoreCase(""))
						registro.put("PROCURADOR_CONTRA_PROVIN_D_J", provinciacontrario);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_PROVIN_D_J", "");
					
					if(poblacioncontrario!=null && !poblacioncontrario.trim().equalsIgnoreCase(""))
						registro.put("PROCURADOR_CONTRA_POBLA_D_J", poblacioncontrario);
					else
						UtilidadesHash.set(registro, "PROCURADOR_CONTRA_POBLA_D_J", "");
					
					if(codigopostalcontrario!=null && !codigopostalcontrario.trim().equalsIgnoreCase(""))
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
			if(idJuzgadoEjg!=null && !idJuzgadoEjg.trim().equals("")){
				helperInformes.completarHashSalida(registro,helperInformes.getJuzgadoSalida(idInstitucionJuzgadoEjg, idJuzgadoEjg,"D_J"));
				//Hacemos este cambio ya que anteriormente la descripcion del juzgado era JUZGADO_DEFENSA_JURIDICA
				String juzgadoEjg = (String)registro.get("JUZGADO_D_J");
				if(juzgadoEjg!=null && !juzgadoEjg.trim().equals("")){
					registro.put("JUZGADO_DEFENSA_JURIDICA", juzgadoEjg);
				}else{
					registro.put("JUZGADO_DEFENSA_JURIDICA", " ");
				}
				
				if(registro.containsKey("ID_POBLACION_JUZGADO_D_J") && registro.get("ID_POBLACION_JUZGADO_D_J")!=null && !((String)registro.get("ID_POBLACION_JUZGADO_D_J")).trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida((String)registro.get("ID_POBLACION_JUZGADO_D_J"), "POBLACION_JUZGADO_D_J"));
				} else {
					registro.put("POBLACION_JUZGADO_D_J", " ");
				}
				if(registro.containsKey("ID_PROVINCIA_JUZGADO_D_J") && registro.get("ID_PROVINCIA_JUZGADO_D_J")!=null && !((String)registro.get("ID_PROVINCIA_JUZGADO_D_J")).trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida((String)registro.get("ID_PROVINCIA_JUZGADO_D_J"), "PROVINCIA_JUZGADO_D_J"));
				} else {
					registro.put("PROVINCIA_JUZGADO_D_J", " ");
				}
			}else{
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
			if(idTipoResolAuto!=null && !idTipoResolAuto.trim().equals("")){
				helperInformes.completarHashSalida(registro,helperInformes.getTipoResolucionAutomatico(idTipoResolAuto,idioma));									
			}else{
				registro.put("DESC_TIPORESOLAUTO", " ");
			}							
			
			idTipoSentidoAuto = (String)registro.get("IDTIPOSENTIDOAUTO");
			if(idTipoSentidoAuto!=null && !idTipoSentidoAuto.trim().equals("")){
				helperInformes.completarHashSalida(registro,helperInformes.getTipoSentidoAutomatico(idTipoSentidoAuto,idioma));					
			}else{
				registro.put("DESC_TIPOSENTIDOAUTO", " ");										
			}
			
			idTipoDictamenEjg = (String)registro.get("IDTIPODICTAMENEJG");
			if(idTipoDictamenEjg!=null && !idTipoDictamenEjg.trim().equals("")){
				helperInformes.completarHashSalida(registro,helperInformes.getTipoDictamenEjg(idInstitucion,idTipoDictamenEjg,idioma));	
				
			}else{
				registro.put("DESC_TIPODICTAMENEJG", " ");
			}							
			
			idTipoRatificacionEjg = (String)registro.get("IDTIPORATIFICACIONEJG");
			if(idTipoRatificacionEjg!=null && !idTipoRatificacionEjg.trim().equals("")){
				helperInformes.completarHashSalida(registro,helperInformes.getTipoRatificacionEjg(idTipoRatificacionEjg, idioma));
			}else{
				registro.put("DESC_TIPORATIFICACIONEJG", " ");
			}				
	
			// Agregamos la comisaria del ejg
			String idComisariaEjg = (String)registro.get("COMISARIA");
			String idInstitucionComisariaEjg = (String)registro.get("COMISARIAIDINSTITUCION");
			
			if(idComisariaEjg!=null && !idComisariaEjg.trim().equals("") && idInstitucionComisariaEjg!=null && !idInstitucionComisariaEjg.trim().equals("")) { 
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
			
			
			if(idComisariaEjg!=null && !idComisariaEjg.trim().equalsIgnoreCase("")){
				registro.put("LUGAR", registro.get("COMISARIA_DEFENSA_JURIDICA"));
				
			}else if(idJuzgadoEjg!=null && !idJuzgadoEjg.trim().equalsIgnoreCase("")){
				registro.put("LUGAR", registro.get("JUZGADO_DEFENSA_JURIDICA"));
				
			}else{			
				registro.put("LUGAR", "");
			} 		
	
			/**Datos de la desingacion asociada al Ejg**/
			String numeroDesigna = (String)registro.get("DES_NUMERO");
			String anioDesigna = (String)registro.get("DES_ANIO");
			String idTurnoDesigna  = (String)registro.get("DES_IDTURNO");
			String idInstitucionDesigna  = (String)registro.get("DES_INSTITUCION");
			
			if(numeroDesigna!=null && !numeroDesigna.trim().equalsIgnoreCase("")){
				helperInformes.completarHashSalida(registro,getDesignaEjgSalida(idInstitucionDesigna, 	idTurnoDesigna,anioDesigna,numeroDesigna,idioma));								
				helperInformes.completarHashSalida(registro,helperInformes.getTurnoSalida(idInstitucion,idTurnoDesigna));
				
				
				
				String idProcedimiento = (String)registro.get("IDPROCEDIMIENTO");
				if(idProcedimiento==null || idProcedimiento.trim().equalsIgnoreCase("")){
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
				if(idLetradoDesigna!=null && !idLetradoDesigna.trim().equals("")){
					helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucionLetradoDesigna, idLetradoDesigna,"LETRADO_DESIGNADO"));	
					
					String sexoLetrado  = (String)registro.get("SEXO_ST_LETRADO_DESIGNADO");
					sexoLetrado = UtilidadesString.getMensajeIdioma(usrbean, sexoLetrado);
					registro.put("SEXO_LETRADO_DESIGNADO", sexoLetrado);
					helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idLetradoDesigna,idInstitucionLetradoDesigna,"LETRADO_DESIGNADO"));									
					helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idLetradoDesigna,idInstitucionLetradoDesigna,"LETRADO_DESIGNADO"));									
					String telefonoDespacho = (String)registro.get("TELDESPACHO_LETRADO_DESIGNADO");
					if(telefonoDespacho!=null)
						UtilidadesHash.set(registro, "TELEFONODESPACHO_LET_DESIGNADO", telefonoDespacho);
					else
						UtilidadesHash.set(registro, "TELEFONODESPACHO_LET_DESIGNADO", "");								
	
					String pobLetrado = (String)registro.get("POBLACION_LETRADO_DESIGNADO");
					if(pobLetrado==null ||pobLetrado.trim().equalsIgnoreCase("")){
						String idPobLetrado = (String)registro.get("ID_POBLACION_LETRADO_DESIGNADO");
						helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetrado,"POBLACION_LETRADO_DESIGNADO"));
						String idProvLetrado = (String)registro.get("ID_PROVINCIA_LETRADO_DESIGNADO");
						if(idProvLetrado!=null && !idProvLetrado.trim().equalsIgnoreCase(""))
							helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetrado,"PROVINCIA_LETRADO_DESIGNADO"));
						else
							UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");									
					}else{
						UtilidadesHash.set(registro, "PROVINCIA_LETRADO_DESIGNADO", "");
							
						
					}
				}else{
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
				if(idJuzgadoDesigna!=null && !idJuzgadoDesigna.trim().equals(""))
					helperInformes.completarHashSalida(registro,getJuzgadoDesignaEjgSalida(idInstitucionJuzgadoDesigna, 
						idJuzgadoDesigna));
				else{
					//Hay tanto lio que voy a comprobar que no existe antes de machacarlos
					if((String)registro.get("JUZGADO")==null || ((String)registro.get("JUZGADO")).trim().equals("")){
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
			}else{
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
			if(idPersonaJG!=null && !idPersonaJG.equalsIgnoreCase("")){
				Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
				if(vDestinatario!=null && vDestinatario.size()>0){
					Hashtable clone = (Hashtable) registro.clone();
					Hashtable destinatario = (Hashtable) vDestinatario.get(0);
					registro.putAll(destinatario);
				}
			}					
			
			String parrafoLetrado = "";
			if (registro.containsKey("NOMBRE_LETRADO_DESIGNADO")  && registro.get("NOMBRE_LETRADO_DESIGNADO")!=null && !((String)registro.get("NOMBRE_LETRADO_DESIGNADO")).trim().equals("") ) {
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
			
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getregistrodatosEjg");
		}
		
		return registro;		
	}
	
	public Long getIdJuzgadoEjg(String institucion, String anio, String numero, String idTipoEJG) throws ClsExceptions,SIGAException {
		Long idJuzgado = null;
		try {
			
			StringBuffer sql = new StringBuffer();
			sql.append(" select e.juzgado  ");
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
				if(fila.getString("JUZGADO") != null && !fila.getString("JUZGADO").equals("")){
					idJuzgado = Long.parseLong((String)fila.getString("JUZGADO"));
				}
			} 
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre el idjuzgado de un ejg.");
		}
		return idJuzgado;                        
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
	public Hashtable getFechaReunionActaEjg(String idInstitucion, String idTipoEjg, String anio, String numero, String idioma) throws ClsExceptions  {
		Hashtable salida = new Hashtable();
		
		try {
			String sql = "SELECT TO_CHAR(ACTA.FECHAREUNION, 'dd/mm/yyyy') AS FECHAREUNION_ACTA, " +
					" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(ACTA.FECHAREUNION , 'DMA', " + idioma + ") AS FECHAREUNION_ACTA_LETRA " +
				" FROM SCS_EJG EJG, SCS_ACTACOMISION ACTA " +				
				" WHERE EJG.ANIO = " + anio +
					" AND EJG.NUMERO = " + numero +
					" AND EJG.IDINSTITUCION = " + idInstitucion + 
					" AND EJG.IDTIPOEJG = " + idTipoEjg +
					" AND EJG.IDACTA =  ACTA.IDACTA " +
					" AND EJG.IDINSTITUCION = ACTA.IDINSTITUCION " +
					" AND EJG.ANIOACTA = ACTA.ANIOACTA";
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					salida = fila.getRow();	                  
				}
			} 
				
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion de la fecha de reunión");
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
				" ORDER BY ESTADOS.FECHAMODIFICACION DESC ";
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql)) {
				if (rc.size() > 0) {
					Row fila = (Row) rc.get(0);
					salida = fila.getRow();	                  
				}
			} 
				
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion de la fecha de remitido de comisión");
		}
		
		return salida;
	}	
}