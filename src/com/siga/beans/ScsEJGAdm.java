package com.siga.beans;
import java.util.Hashtable;
import java.util.Vector;

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
import com.siga.Utilidades.UtilidadesNumero;
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
		
		entrada.put(ScsEJGBean.C_NUMERO,numeroMaximo);
		
		// Por último ponemos la FECHAAPERTURA en el formato correcto de la base de datos.
		entrada.put(ScsEJGBean.C_FECHAAPERTURA,GstDate.getApplicationFormatDate("",entrada.get(ScsEJGBean.C_FECHAAPERTURA).toString()));		
		
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
							ScsEJGBean.C_FECHADICTAMEN,				ScsEJGBean.C_PROCURADOR,
							ScsEJGBean.C_RATIFICACIONDICTAMEN,		ScsEJGBean.C_FECHARATIFICACION,							
							ScsEJGBean.C_IDPERSONA,					ScsEJGBean.C_IDINSTITUCION,
							ScsEJGBean.C_IDTIPOEJG,					ScsEJGBean.C_GUARDIATURNO_IDGUARDIA,
							ScsEJGBean.C_GUARDIATURNO_IDTURNO,		ScsEJGBean.C_IDTIPOEJGCOLEGIO,
							ScsEJGBean.C_IDPERSONAJG,				
							ScsEJGBean.C_USUMODIFICACION,			ScsEJGBean.C_FECHAMODIFICACION,
							//ScsEJGBean.C_DESIGNA_IDTURNO,			ScsEJGBean.C_DESIGNA_ANIO,
							//ScsEJGBean.C_DESIGNA_NUMERO,			
							ScsEJGBean.C_IDTIPODICTAMENEJG,
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
							ScsEJGBean.C_IDPRETENSION,				ScsEJGBean.C_IDPRETENSIONINSTITUCION,
							ScsEJGBean.C_IDDICTAMEN,				ScsEJGBean.C_REFAUTO,
							ScsEJGBean.C_FECHADESIGPROC,			ScsEJGBean.C_IDENTIFICADORDS,
							ScsEJGBean.C_SITUACION,					ScsEJGBean.C_IDTIPOENCALIDAD,
							ScsEJGBean.C_CALIDADIDINSTITUCION,		ScsEJGBean.C_NUMERODESIGNAPROC};
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
			bean.setProcurador(UtilidadesHash.getString(hash,ScsEJGBean.C_PROCURADOR));
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
			bean.setIdFundamentoCalificacion(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDFUNDAMENTOCALIF));

			bean.setFechaAuto(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHAAUTO));
			bean.setFechaNotificacion(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHANOTIFICACION));
			bean.setFechaResolucionCAJG(UtilidadesHash.getString(hash,ScsEJGBean.C_FECHARESOLUCIONCAJG));
			bean.setIdFundamentoJuridico(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDFUNDAMENTOJURIDICO));
			bean.setIdTipoResolAuto(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPORESOLAUTO));
			bean.setIdTipoSentidoAuto(UtilidadesHash.getInteger(hash,ScsEJGBean.C_IDTIPOSENTIDOAUTO));
			bean.setTurnadoAuto(UtilidadesHash.getString(hash,ScsEJGBean.C_TURNADOAUTO));
			bean.setTurnadoRatificacion(UtilidadesHash.getString(hash,ScsEJGBean.C_TURNADORATIFICACION));
			
			bean.setIdPretension(UtilidadesHash.getString(hash,ScsEJGBean.C_IDPRETENSION));
			bean.setIdPretensionInstitucion(UtilidadesHash.getString(hash,ScsEJGBean.C_IDPRETENSIONINSTITUCION));
			
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
			UtilidadesHash.set(htData,ScsEJGBean.C_PROCURADOR, b.getProcurador());
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
			UtilidadesHash.set(htData,ScsEJGBean.C_IDFUNDAMENTOCALIF,b.getIdFundamentoCalificacion());

			UtilidadesHash.set(htData,ScsEJGBean.C_FECHAAUTO,b.getFechaAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHANOTIFICACION,b.getFechaNotificacion());
			UtilidadesHash.set(htData,ScsEJGBean.C_FECHARESOLUCIONCAJG,b.getFechaResolucionCAJG());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDFUNDAMENTOJURIDICO,b.getIdFundamentoJuridico());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPORESOLAUTO,b.getIdTipoResolAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDTIPOSENTIDOAUTO,b.getIdTipoSentidoAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_TURNADOAUTO,b.getTurnadoAuto());
			UtilidadesHash.set(htData,ScsEJGBean.C_TURNADORATIFICACION,b.getTurnadoRatificacion());
			
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPRETENSION,b.getIdPretension());
			UtilidadesHash.set(htData,ScsEJGBean.C_IDPRETENSIONINSTITUCION,b.getIdPretensionInstitucion());
			
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
	/*
	public Vector comunesEjg (String idinstitucion,
			  String anio,
			  String idTipoEJG,
			  String numero,
			  String lenguaje, boolean isASolicitantes,String idPersonaJG)
		throws ClsExceptions,SIGAException
		{
		Vector datos=new Vector();
		try {
		RowsContainer rc = new RowsContainer();
		
		//ESTA CONSULTA HAY QUE CAMBIARLA CUANDO ESTE LISTA LA CONSULTA QUE VENDRÁ AQUI
	    Hashtable codigos = new Hashtable();
	    int contador=0;
		
		String query = "SELECT  DISTINCT COL.NCOLEGIADO AS NCOLEGIADO_LETRADO,      " +
				"   COL.NOMBRE || ' ' || COL.APELLIDOS1 || ' ' || COL.APELLIDOS2 AS NOMBRE_LETRADO," +
				"DECODE(COL.SEXO,null,null,'M'," ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
	     query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:"+contador+")," ;
			contador++;
			codigos.put(new Integer(contador),lenguaje);
			
			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:"+contador+")) AS SEXO_LETRADO," +
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
				"         COLDES.NCOLEGIADO AS NCOLEGIADO_LETRADO_DESIGNADO," +
				"         COLDES.NOMBRE || ' ' || COLDES.APELLIDOS1 || ' ' || COLDES.APELLIDOS2 AS NOMBRE_LETRADO_DESIGNADO," +
				"DECODE(COLDES.SEXO,null,null,'M'," ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:"+contador+")," ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:"+contador+")) AS SEXO_LETRADO_DESIGNADO," +
				"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,1)  AS DOMICILIO_LETRADO_DESIGNADO,"+
        		"	      f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,2)  AS CP_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,3)  AS POBLACION_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,4)  AS PROVINCIA_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,11)  AS TELEFONODESPACHO_LET_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,16)  AS EMAIL_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,2,14)  AS FAX_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,6,11)  AS TELEFONO1_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,6,12)  AS TELEFONO2_LETRADO_DESIGNADO,"+
        		"		  f_siga_getdireccioncliente(COLDES.IDINSTITUCION,COLDES.IDPERSONA,6,13)  AS MOVIL_LETRADO_DESIGNADO,";
				
				//if(isASolicitantes){
					query+= "         INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' || INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO," +
					"         INTERESADO.DIRECCION AS DOMICILIO_DEFENDIDO," +
					"         INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO," +
					"         INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO," +
					"         INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO," +
					"         INTERESADO.NIF AS NIF_DEFENDIDO," +
					"         INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO," +
					"DECODE(INTERESADO.SEXO,null,null,'M'," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
					query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:"+contador+")," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
					query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:"+contador+")) AS SEXO_INTERESADO," +
					"         INTERESADO.IDLENGUAJE  AS LENGUAJE_INTERESADO," +
					"         DECODE(INTERESADO.CALIDAD,null,'','D'," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
					query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandante',:"+contador+")," ;
					contador++;
					codigos.put(new Integer(contador),lenguaje);
					
					query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandado',:"+contador+")) AS CALIDAD_INTERESADO," +
					"         DECODE(INTERESADO.ANIOEJG,NULL,NULL,INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG) AS NUMERO_EJG,"+
					"		  F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE, ";
				//}
				query+="         DES.NUMPROCEDIMIENTO AS AUTOS," +
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
				"         DES.NOMBRE_PROCEDIMIENTO AS PROCEDIMIENTO," +
				"         F_SIGA_GETDELITOS_DESIGNA(DES.IDINSTITUCION, DES.NUMERO, DES.IDTURNO, DES.ANIO, 1) AS DELITOS," +
				"         TO_CHAR(DES.FECHAENTRADA, 'dd-mm-yyyy') AS FECHA_DESIGNA," +
				"         DES.TURNO_DESCRIPCION AS DESCRIPCION_TURNO," +
				"		  DES.ABREV_TURNO AS ABREV_TURNO, "+
				"         F_SIGA_NOMBRE_PARTIDO(DES.IDTURNO,DES.IDINSTITUCION) as NOMBRE_PARTIDO," +
				"         DECODE(EJG.COMISARIA, NULL, (SELECT J.NOMBRE" +
				"                                        FROM SCS_JUZGADO J" +
				"                                       WHERE J.IDINSTITUCION = EJG.JUZGADOIDINSTITUCION" +
				"                                         AND J.IDJUZGADO = EJG.JUZGADO)," +
				"                                     (SELECT C.NOMBRE" +
				"                                        FROM SCS_COMISARIA C" +
				"                                       WHERE C.IDINSTITUCION = EJG.COMISARIAIDINSTITUCION" +
				"                                         AND C.IDCOMISARIA = EJG.COMISARIA)) AS LUGAR," +
				"         EJG.ANIO AS ANIO_EJG," ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "         PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(EJG.FECHAAPERTURA,'m',:"+contador+") AS FECHAAPERTURA_EJG," +
				"         EJG.OBSERVACIONES AS OBSERVACIONES" +
				"         ,ASI.ASUNTODILIGENCIA AS ASUNTODILIGENCIA" +
				"         ,ASI.COMISARIAJUZGADO AS COMISARIAJUZGADO" +
				 ",F_SIGA_GETINTERESADOSDESIGNA(DES.IDINSTITUCION,DES.ANIO,DES.IDTURNO,DES.NUMERO,0) LISTA_INTERESADOS_DESIGNA"+
    			
    			 ",F_SIGA_GETACTUACIONESDESIGNA(DES.IDINSTITUCION,DES.ANIO,DES.IDTURNO,DES.NUMERO) LISTA_ACTUACIONES_DESIGNA"+
				
				
    			"        ,TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL" +
				"        , TO_CHAR(SYSDATE, 'MONTH', 'NLS_DATE_LANGUAGE = SPANISH') AS MES_ACTUAL" +
				"        , TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL" ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "         ,F_SIGA_GETRECURSO(TF.descripcion,:"+contador+") AS FUNDAMENTO "+
				"  ,TO_CHAR(F_SIGA_GETFIRSTACTDESIGNA(DES.IDINSTITUCION,DES.ANIO, DES.IDTURNO, DES.NUMERO), 'dd-mm-yyyy') AS FECHA_ACTUACION,"+
				"  TO_CHAR(F_SIGA_GETFIRSTACTDESIGNA(DES.IDINSTITUCION,DES.ANIO, DES.IDTURNO, DES.NUMERO), 'hh:MM:ss') AS HORA_ACTUACION,"+
				"  TO_CHAR(F_SIGA_GETFIRSTASISDESIGNA(DES.IDINSTITUCION,DES.ANIO, DES.IDTURNO, DES.NUMERO), 'dd-mm-yyyy') AS FECHA_ASISTENCIA,"+
				"  EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO, EJG.IDPERSONA "+
				"         ,DECODE(ejg.CALIDAD,null,'','D'," ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandante',:"+contador+")," ;
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandado',:"+contador+")) AS CALIDAD_DEFENSA_JURIDICA," +
				" EJG.OBSERVACIONES AS ASUNTO_DEFENSA_JURIDICA, "+
				" EJG.DELITOS AS COM_DEL_DEFENSA_JURIDICA, "+
				" EJG.NUMERO_CAJG AS NUMERO_CAJG_DEFENSA_JURIDICA, "+
				" EJG.ANIOCAJG AS ANIO_CAJG_DEFENSA_JURIDICA, "+
				" EJG.NUMERODILIGENCIA AS NUMDILIGENCIA_DEFENSA_JURIDICA, "+
				" EJG.NUMEROPROCEDIMIENTO AS NUMPROCED_DEFENSA_JURIDICA "+
				" ,PROCDF.NOMBRE PROCURADOR_DEFENSA_JURIDICA, "+
				" JUZDF.NOMBRE AS JUZGADO_DEFENSA_JURIDICA, "+
				"  COMDF.NOMBRE AS COMISARIA_DEFENSA_JURIDICA ";
				contador++;
				codigos.put(new Integer(contador),lenguaje);
				
				query += "  ,F_SIGA_GETDELITOS_EJG(EJG.IDINSTITUCION,EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO,:"+contador+") DELITOS_DEFENSA_JURIDICA "+
				"  ,F_SIGA_GETCONTRARIOS_EJG(EJG.IDINSTITUCION,EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO) CONTRARIOS_DEFENSA_JURIDICA "+
				
				
				
				
				"         FROM SCS_EJG EJG," +
				"       V_SIGA_EJG_DESIGNA EJGD," +
				"        V_SIGA_DESIGNACIONES DES," +
				"        V_SIGA_DATOSLETRADO_COLEGIADO COL," +
				"        V_SIGA_DATOSLETRADO_COLEGIADO COLDES," +
				"        V_SIGA_JUZGADOS JUZ"+
				//if(isASolicitantes)
				     ",V_SIGA_INTERESADOS_EJG INTERESADO"+
				"        ,V_SIGA_PROCURADOR_EJG PROC" +
				"        ,V_SIGA_ASISTENCIA_EJG ASI " +
				"        ,scs_tipofundamentocalif  TF "+
				" ,SCS_PROCURADOR         PROCDF, "+
				" SCS_JUZGADO JUZDF, "+
				"  SCS_COMISARIA COMDF "+
		
				"  WHERE EJG.IDINSTITUCION = COL.IDINSTITUCION(+)" +
				"    AND EJG.IDPERSONA = COL.IDPERSONA(+)" +
				"    AND DES.IDINSTITUCION = COLDES.IDINSTITUCION(+)" +
				"    AND DES.IDPERSONA_DESIGNADA = COLDES.IDPERSONA(+)" +
				"  AND EJG.IDINSTITUCION = EJGD.IDINSTITUCION(+)" +
				"  AND EJG.IDTIPOEJG = EJGD.IDTIPOEJG(+)" +
				"  AND EJG.ANIO = EJGD.ANIO(+)" +
				"  AND EJG.NUMERO = EJGD.NUMERO (+)" +
				"  AND EJG.IDINSTITUCION = ASI.EJG_IDINSTITUCION(+)" +
				"  AND EJG.IDTIPOEJG = ASI.EJG_IDTIPOEJG(+)" +
				"  AND EJG.ANIO = ASI.EJG_ANIO(+)" +
				"  AND EJG.NUMERO = ASI.EJG_NUMERO(+)" +
				
				"  AND EJGD.IDINSTITUCION = DES.IDINSTITUCION(+)" +
				"  AND EJGD.IDTURNO_DES = DES.IDTURNO(+)" +
				"  AND EJGD.ANIO_DES = DES.ANIO(+)" +
				"  AND EJGD.NUMERO_DES = DES.NUMERO(+)" +
				   
				"  AND DES.IDINSTITUCION_JUZG = JUZ.IDINSTITUCION(+)" +
				"  AND DES.IDJUZGADO = JUZ.IDJUZGADO(+)" +
				"  AND EJG.IDINSTITUCION = INTERESADO.IDINSTITUCION(+)" +
				"  AND EJG.IDTIPOEJG = INTERESADO.IDTIPOEJG(+)" +
				"  AND EJG.ANIO = INTERESADO.ANIO(+)" +
				"  AND EJG.NUMERO = INTERESADO.NUMERO(+)" +
				"  AND EJG.IDINSTITUCION = PROC.IDINSTITUCION(+)" +
				"  AND EJG.IDTIPOEJG = PROC.IDTIPOEJG(+)" +
				"  AND EJG.ANIO = PROC.ANIO(+)" +
				"  AND EJG.NUMERO = PROC.NUMERO(+)" +
				"    and EJG.idfundamentocalif = TF.idfundamentocalif(+)"+
				"    and EJG.idinstitucion = TF.idinstitucion(+)"+
				
				" AND EJG.IDPROCURADOR = PROCDF.IDPROCURADOR(+) " +
				"  AND EJG.IDINSTITUCION_PROC = PROCDF.IDINSTITUCION(+) " +
				"  and EJG.JUZGADO = JUZDF.IDJUZGADO(+) " +
				"  and EJG.JUZGADOIDINSTITUCION = JUZDF.IDINSTITUCION(+) " +
				"  and EJG.COMISARIA = COMDF.IDCOMISARIA(+) " +
				"  and EJG.COMISARIAIDINSTITUCION = COMDF.IDINSTITUCION(+) " ;
				contador++;
				codigos.put(new Integer(contador),idinstitucion);
				
				query += "   and EJG.IDINSTITUCION =:"+contador;
				contador++;
				codigos.put(new Integer(contador),idTipoEJG);
				
				query += "   and EJG.IDTIPOEJG =:" +contador;
				contador++;
				codigos.put(new Integer(contador),anio);
				
				query += "   and EJG.ANIO = :"+contador;
				contador++;
				codigos.put(new Integer(contador),numero);
				
				query += "   and EJG.NUMERO =:"+contador ;
				if(idPersonaJG!=null && !idPersonaJG.equalsIgnoreCase("")){
				    contador++;
					codigos.put(new Integer(contador),idPersonaJG);
					query +="   and INTERESADO.IDPERSONAJG =  :"+ contador;
        			
        		}
		        query +="  ORDER BY EJG.IDPERSONA ";
//		        query +="  ORDER BY EJG.IDTIPOEJG," +
//				"           EJG.ANIO," +
//				"           EJG.NUMERO," +
//				"           EJG.IDPERSONA";
           

	
		

		
		
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
	
	*/
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
		
		// Estos son los campos que devuelve la select
		String consulta = 
			"select ejg." + ScsEJGBean.C_ANIO + ", ejg." + ScsEJGBean.C_IDINSTITUCION + ", ejg." + ScsEJGBean.C_IDTIPOEJG + ", ejg." + ScsEJGBean.C_IDFACTURACION +", ejg. " + ScsEJGBean.C_FECHARATIFICACION +
			", tipoejg." + ScsTipoEJGBean.C_DESCRIPCION +  " as TIPOEJG " +
			", ejg." + ScsEJGBean.C_NUMEJG + ", ejg." + ScsEJGBean.C_FECHAAPERTURA + 
			", ejg." + ScsEJGBean.C_GUARDIATURNO_IDTURNO+", ejg." + ScsEJGBean.C_GUARDIATURNO_IDGUARDIA +
			", '' AS APELLIDO1,'' AS APELLIDO2" +
			", F_SIGA_GET_ULTIMOESTADOEJG(ejg." + ScsEJGBean.C_IDINSTITUCION + ", ejg." + ScsEJGBean.C_IDTIPOEJG + "" +
			", ejg." + ScsEJGBean.C_ANIO + ", ejg." + ScsEJGBean.C_NUMERO + ") AS DESC_ESTADO " + 
			", ejg."+ScsEJGBean.C_NUMERO+",ejg."+ScsEJGBean.C_FECHAMODIFICACION+", ejg."+ScsEJGBean.C_SUFIJO ;

		// Metemos las tablas implicadas en la select 
		consulta += " from " + 
			ScsEJGBean.T_NOMBRETABLA + " ejg,"  + 
			ScsTipoEJGBean.T_NOMBRETABLA + " tipoejg," + 
			CenColegiadoBean.T_NOMBRETABLA + " colegiado" ;
		
		// Comenzamos a cruzar tablas
		consulta +=
			" where ejg." + ScsEJGBean.C_IDTIPOEJG + " = tipoejg." + ScsTipoEJGBean.C_IDTIPOEJG + " and " + 
			" ejg." + ScsEJGBean.C_IDINSTITUCION + " = colegiado." + CenColegiadoBean.C_IDINSTITUCION + "(+) and " +
			" ejg." + ScsEJGBean.C_IDPERSONA + " = colegiado." + CenColegiadoBean.C_IDPERSONA+"(+)";   
		
		// Parametros para poder reutilizar la busqueda EJG para busquedas CAJG
		if(TipoVentana.BUSQUEDA_PREPARACION_CAJG.equals(tipoVentana)){
			consulta +=" AND (f_siga_get_idultimoestadoejg(ejg.idinstitucion,ejg.idtipoejg, ejg.anio, ejg.numero)" +
			" NOT IN (7, 8, 9, 10, 11) OR f_siga_get_idultimoestadoejg(ejg.idinstitucion,ejg.idtipoejg, ejg.anio, ejg.numero) IS NULL) ";			
		} else if (TipoVentana.BUSQUEDA_ANIADIR_REMESA.equals(tipoVentana)) {
			consulta += " AND " + ClsConstants.ESTADO_LISTO_COMISION + " = F_SIGA_GET_IDULTIMOESTADOEJG(ejg.IDINSTITUCION, ejg.IDTIPOEJG, ejg.ANIO, ejg.NUMERO)";
		}
		
		// Se filtra por numero cajg
		if (miForm.getNumeroCAJG()!=null && !miForm.getNumeroCAJG().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getNumeroCAJG());
			consulta += " and ejg.Numero_Cajg = :" + contador;
		}
		
		// Se filtra por anio cajg
		if (miForm.getAnioCAJG()!=null && !miForm.getAnioCAJG().trim().equalsIgnoreCase("")) {
			contador++;
			codigos.put(new Integer(contador),miForm.getAnioCAJG());
			consulta += " and ejg.Aniocajg = :" + contador;
		}


		// Y ahora concatenamos los criterios de búsqueda
		if ((miForm.getFechaAperturaDesde() != null && !miForm.getFechaAperturaDesde().equals("")) ||
				(miForm.getFechaAperturaHasta() != null && !miForm.getFechaAperturaHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("ejg.FECHAAPERTURA", GstDate.getApplicationFormatDate("",miForm.getFechaAperturaDesde()),GstDate.getApplicationFormatDate("",miForm.getFechaAperturaHasta()),contador, codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " and " + st;                    
		}

		if ((miForm. getfechaDictamenDesde() != null && !miForm.getfechaDictamenDesde().equals("")) ||
				(miForm.getfechaDictamenHasta() != null && !miForm.getfechaDictamenHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("ejg."+ScsEJGBean.C_FECHADICTAMEN, GstDate.getApplicationFormatDate("",miForm.getfechaDictamenDesde()),GstDate.getApplicationFormatDate("",miForm.getfechaDictamenHasta()),contador,codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " and " + st;                    
		}
					
		if ((miForm.getFechaLimitePresentacionDesde() != null && !miForm.getFechaLimitePresentacionDesde().equals("")) ||
				(miForm.getFechaLimitePresentacionHasta() != null && !miForm.getFechaLimitePresentacionHasta().equals(""))) {
			Vector v = GstDate.dateBetweenDesdeAndHastaBind("ejg."+ScsEJGBean.C_FECHALIMITEPRESENTACION, GstDate.getApplicationFormatDate("",miForm.getFechaLimitePresentacionDesde()),GstDate.getApplicationFormatDate("",miForm.getFechaLimitePresentacionHasta()),contador,codigos);
			Integer in = (Integer)v.get(0);
			String st = (String)v.get(1);
			contador = in.intValue();
			consulta += " and " + st;                    
		}

		if ((miHash.containsKey("IDTIPOEJG")) && (!miHash.get("IDTIPOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"IDTIPOEJG"));
			consulta += " and ejg. " + ScsEJGBean.C_IDTIPOEJG + " = :"+contador;
		}

		if ((miHash.containsKey("ANIO")) && (!miHash.get("ANIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"ANIO"));
			consulta += " and ejg.ANIO = :" + contador;
		}

		if ((miHash.containsKey("CREADODESDE")) && (!miHash.get("CREADODESDE").toString().equals(""))) {
			if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("A")) 
				consulta += "  and (0<(SELECT COUNT(*) FROM SCS_ASISTENCIA WHERE IDINSTITUCION=EJG.IDINSTITUCION AND EJGNUMERO=EJG.NUMERO AND EJGANIO=EJG.ANIO AND ejgidtipoejg=EJG.IDTIPOEJG))";
			else if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("D")){			
				consulta += " and (select count(1)";
				consulta += " from scs_ejgdesigna edes";
				consulta += " where ejg.idinstitucion = edes.idinstitucion";
				consulta += " and ejg.numero = edes.numeroejg";
				consulta +=  " and ejg.anio = edes.anioejg";
				consulta += " and ejg.idtipoejg = edes.idtipoejg) > 0";
			}     
			else if (miHash.get("CREADODESDE").toString().equalsIgnoreCase("S")) {
				consulta += " and (0<(SELECT COUNT(*) FROM SCS_SOJ WHERE IDINSTITUCION=EJG.IDINSTITUCION AND EJGNUMERO=EJG.NUMERO AND EJGANIO=EJG.ANIO AND ejgidtipoejg=EJG.IDTIPOEJG))";
			}
			else {
				consulta+= " and (0<(SELECT COUNT(*) FROM SCS_ASISTENCIA WHERE IDINSTITUCION=EJG.IDINSTITUCION AND EJGNUMERO IS NULL)) and (0<(SELECT COUNT(*) FROM SCS_SOJ WHERE IDINSTITUCION=EJG.IDINSTITUCION AND EJGNUMERO IS NULL))"; 
			}
		}

		if ((miHash.containsKey("GUARDIATURNO_IDTURNO")) && (!miHash.get("GUARDIATURNO_IDTURNO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"GUARDIATURNO_IDTURNO"));
			consulta += " and ejg.GUARDIATURNO_IDTURNO = :" + contador;
		}

		if ((miHash.containsKey("GUARDIATURNO_IDGUARDIA")) && (!miHash.get("GUARDIATURNO_IDGUARDIA").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"GUARDIATURNO_IDGUARDIA"));
			consulta += " and ejg.GUARDIATURNO_IDGUARDIA = :" + contador;
		}
		
		if ((miHash.containsKey("IDPERSONA")) && (!miHash.get("IDPERSONA").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"IDPERSONA"));
			consulta += " and colegiado.idpersona = :" + contador;
		}
		
		if ((miHash.containsKey("DICTAMINADO")) && (!miHash.get("DICTAMINADO").toString().equals(""))) {
			if (miHash.get("DICTAMINADO").toString().equalsIgnoreCase("S")) {
				consulta += " and ejg.FECHADICTAMEN IS NOT NULL";
			}
			else if (miHash.get("DICTAMINADO").toString().equalsIgnoreCase("N")) {
				consulta += " and ejg.FECHADICTAMEN IS NULL";
			}
		}
		
		if ((miHash.containsKey("IDTIPODICTAMENEJG")) && (!miHash.get("IDTIPODICTAMENEJG").toString().equals(""))){
			contador++;
			codigos.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"IDTIPODICTAMENEJG"));
			consulta += " and ejg.idtipodictamenejg = :" + contador;
		}

		if (UtilidadesHash.getString(miHash,"NUMEJG") != null && !UtilidadesHash.getString(miHash,"NUMEJG").equalsIgnoreCase("")) {
			if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash,"NUMEJG"))) {
				contador++;
				consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)UtilidadesHash.getString(miHash,"NUMEJG")).trim(),"ejg.NUMEJG", contador, codigos );
			}else {
				contador++;
			    codigos.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"NUMEJG").trim());
				consulta += " AND LTRIM(ejg.NUMEJG,'0')  = LTRIM(:" + contador + ",'0') ";
			}
		}

		if ((miHash.containsKey("IDTIPOEJGCOLEGIO")) && (!miHash.get("IDTIPOEJGCOLEGIO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"IDTIPOEJGCOLEGIO"));
			consulta += " and ejg.IDTIPOEJGCOLEGIO = :" + contador;
		}
				
		if ((miHash.containsKey("IDINSTITUCION")) && (!miHash.get("IDINSTITUCION").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"IDINSTITUCION"));
			consulta += " and ejg.IDINSTITUCION = :"+ contador;
		}else{	
			idInstitucion="";
			if (idInstitucion.equals("")){			
			  throw new ClsExceptions("messages.comprueba.noidInstitucion");			
			}else			
				consulta += " and ejg.IDINSTITUCION =" + idInstitucion;			
		   }				
		
		if ((miHash.containsKey("NIF")) && (!miHash.get("NIF").toString().equals(""))){
			contador++;
			codigos.put(new Integer(contador),((String)miHash.get("NIF")).trim()+"%");
			consulta += " and ( select COUNT(1) " + 
			"	from SCS_UNIDADFAMILIAREJG unidad, SCS_EJG EJG2, scs_personajg PJG " + 
			"	where  unidad.idinstitucion = pjg.idinstitucion " +
			"	and unidad.idpersona = pjg.idpersona " +
			"	and ejg2.IDINSTITUCION = unidad.IDINSTITUCION(+) " +
			"	and ejg2.ANIO = unidad.ANIO(+) " +
			"	and ejg2.NUMERO = unidad.NUMERO(+) " +
			"	and ejg2.IDTIPOEJG = unidad.IDTIPOEJG(+) " +
			"	and unidad.SOLICITANTE(+) = '1' " +
			"	and ejg2.IDINSTITUCION = ejg.IDINSTITUCION " +
			"	and ejg2.ANIO = ejg.ANIO " + 
			"	and ejg2.NUMERO = ejg.NUMERO " +
			"	and ejg2.IDTIPOEJG = ejg.IDTIPOEJG" +
			"	AND LTRIM(upper(pjg.nif),'0') LIKE LTRIM(UPPER(:"+contador+"),'0')) > 0";						 
		}

		if ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals(""))||
			(miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals(""))||
			(miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals(""))){

			String vDatosSolicitante="";

			if ((miHash.containsKey("NOMBRE")) && (!miHash.get("NOMBRE").toString().equals(""))){
				contador++;
				consulta += " and ( select COUNT(1) " + 
				"	from SCS_UNIDADFAMILIAREJG unidad, SCS_EJG EJG2, scs_personajg PJG " + 
				"	where  unidad.idinstitucion = pjg.idinstitucion " +
				"	and unidad.idpersona = pjg.idpersona " +
				"	and ejg2.IDINSTITUCION = unidad.IDINSTITUCION(+) " +
				"	and ejg2.ANIO = unidad.ANIO(+) " +
				"	and ejg2.NUMERO = unidad.NUMERO(+) " +
				"	and ejg2.IDTIPOEJG = unidad.IDTIPOEJG(+) " +
				"	and unidad.SOLICITANTE(+) = '1' " +
				"	and ejg2.IDINSTITUCION = ejg.IDINSTITUCION " +
				"	and ejg2.ANIO = ejg.ANIO " + 
				"	and ejg2.NUMERO = ejg.NUMERO " +
				"	and ejg2.IDTIPOEJG = ejg.IDTIPOEJG";
				consulta += " and "+ ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("NOMBRE")).trim(),"upper(pjg.NOMBRE)",contador,codigos ) + ")>0";
			}

			if ((miHash.containsKey("APELLIDO1")) && (!miHash.get("APELLIDO1").toString().equals(""))){
				contador++; 
				consulta += " and ( select COUNT(1) " + 
				"	from SCS_UNIDADFAMILIAREJG unidad, SCS_EJG EJG2, scs_personajg PJG " + 
				"	where  unidad.idinstitucion = pjg.idinstitucion " +
				"	and unidad.idpersona = pjg.idpersona " +
				"	and ejg2.IDINSTITUCION = unidad.IDINSTITUCION(+) " +
				"	and ejg2.ANIO = unidad.ANIO(+) " +
				"	and ejg2.NUMERO = unidad.NUMERO(+) " +
				"	and ejg2.IDTIPOEJG = unidad.IDTIPOEJG(+) " +
				"	and unidad.SOLICITANTE(+) = '1' " +
				"	and ejg2.IDINSTITUCION = ejg.IDINSTITUCION " +
				"	and ejg2.ANIO = ejg.ANIO " + 
				"	and ejg2.NUMERO = ejg.NUMERO " +
				"	and ejg2.IDTIPOEJG = ejg.IDTIPOEJG ";
				consulta += " and "+ ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO1")).trim(),"upper(pjg.apellido1)",contador,codigos ) + ")>0";
			}
			
			if ((miHash.containsKey("APELLIDO2")) && (!miHash.get("APELLIDO2").toString().equals(""))){
				contador++;
				consulta += " and ( SELECT COUNT(1) " + 
				"	from SCS_UNIDADFAMILIAREJG unidad, SCS_EJG EJG2, scs_personajg PJG " + 
				"	where  unidad.idinstitucion = pjg.idinstitucion " +
				"	and unidad.idpersona = pjg.idpersona " +
				"	and ejg2.IDINSTITUCION = unidad.IDINSTITUCION(+) " +
				"	and ejg2.ANIO = unidad.ANIO(+) " +
				"	and ejg2.NUMERO = unidad.NUMERO(+) " +
				"	and ejg2.IDTIPOEJG = unidad.IDTIPOEJG(+) " +
				"	and unidad.SOLICITANTE(+) = '1' " +
				"	and ejg2.IDINSTITUCION = ejg.IDINSTITUCION " +
				"	and ejg2.ANIO = ejg.ANIO " + 
				"	and ejg2.NUMERO = ejg.NUMERO " +
				"	and ejg2.IDTIPOEJG = ejg.IDTIPOEJG ";
				consulta += " and "+ ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO2")).trim(),"upper(pjg.apellido2)",contador,codigos ) + ")>0";
			}
		}

		if ((miHash.containsKey("JUZGADO")) && (!miHash.get("JUZGADO").toString().equals(""))) {
			String a[]=((String)miHash.get("JUZGADO")).split(",");
			contador++;
			consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(a[0].trim(),"ejg.JUZGADO", contador, codigos);
		}

		if ((miHash.containsKey("ASUNTO")) && (!miHash.get("ASUNTO").toString().equals(""))) {
			contador++;
			consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("ASUNTO")).trim(),"ejg.numerodiligencia", contador, codigos );
		}

		if ((miHash.containsKey("PROCEDIMIENTO")) && (!miHash.get("PROCEDIMIENTO").toString().equals(""))) {
			contador++;
			consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("PROCEDIMIENTO")).trim(),"ejg.numeroprocedimiento", contador, codigos);
		}

		if ((miHash.containsKey("CALIDAD")) && (!miHash.get("CALIDAD").toString().equals(""))) {
			contador++;
			consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("CALIDAD")).trim(),"ejg.calidad", contador, codigos);

		}
		
		if ((miHash.containsKey("ESTADOEJG")) && (!miHash.get("ESTADOEJG").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador), UtilidadesHash.getString(miHash, "ESTADOEJG"));
			consulta += " and f_siga_get_idultimoestadoejg(ejg.idinstitucion,ejg.idtipoejg, ejg.anio, ejg.numero) = :" + contador;
			
			if ((miForm.getfechaEstadoDesde() != null && !miForm.getfechaEstadoDesde().equals("")) ||
					(miForm.getfechaEstadoHasta() != null && !miForm.getfechaEstadoHasta().equals(""))) {
			  
				Vector v = GstDate.dateBetweenDesdeAndHastaBind("(select trunc(estadoejg.fechainicio)" +
						" from scs_estadoejg estadoEjg" +
						" where estadoEjg.Idtipoejg=ejg.IDTIPOEJG" +
						"  and  estadoEjg.Idinstitucion=ejg.idinstitucion" +
						"  and  estadoEjg.Anio=ejg.anio" +
						"  and  estadoEjg.numero=ejg.numero" +
						"  and estadoEjg.Idestadoejg= "+UtilidadesHash.getString(miHash, "ESTADOEJG")+
						"  and rownum=1)", GstDate.getApplicationFormatDate("",miForm.getfechaEstadoDesde()),GstDate.getApplicationFormatDate("",miForm.getfechaEstadoHasta()),contador,codigos);
				Integer in = (Integer)v.get(0);
				String st = (String)v.get(1);
				contador = in.intValue();
				consulta += " and " + st;                    
			} 
		} else if ((miHash.containsKey("DESCRIPCIONESTADO")) && (!miHash.get("DESCRIPCIONESTADO").toString().equals(""))) {
			contador++;
			codigos.put(new Integer(contador),this.usrbean.getLanguage());
			consulta += " and (select F_SIGA_GETRECURSO(maestroes.DESCRIPCION, :"+contador+") DESCRIPCION " + 
			" from SCS_ESTADOEJG         estadoejg, " + 
			" SCS_MAESTROESTADOSEJG maestroes " + 
			" WHERE estadoejg.IDINSTITUCION = ejg.IDINSTITUCION " + 
			" and estadoejg.IDTIPOEJG = ejg.IDTIPOEJG " + 
			" and estadoejg.ANIO = ejg.ANIO " + 
			" and estadoejg.NUMERO = ejg.NUMERO " + 
			" and maestroes.IDESTADOEJG = estadoejg.IDESTADOEJG " + 
			" and estadoejg.IDESTADOPOREJG = " + 
			" (SELECT MAX(ultimoestado.IDESTADOPOREJG) " + 
			"      from SCS_ESTADOEJG ultimoestado " + 
			"     where ultimoestado.IDINSTITUCION = " + 
			"           estadoejg.IDINSTITUCION " + 
			"       and ultimoestado.IDTIPOEJG = " + 
			"           estadoejg.IDTIPOEJG " + 
			"       and ultimoestado.ANIO = estadoejg.ANIO " + 
			"       and ultimoestado.NUMERO = estadoejg.NUMERO) "; 
			contador++;
			codigos.put(new Integer(contador),UtilidadesHash.getString(miHash,"DESCRIPCIONESTADO"));
			consulta += " and rownum = 1) = :"+contador;
		}


		consulta += " ORDER BY to_number(" + ScsEJGBean.C_ANIO + ") desc, to_number("+ ScsEJGBean.C_NUMEJG+") desc";

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
	
	private Vector getCalificacionEjgSalida (String idInstitucion, String tipoEjg,
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
			sql.append(" ,TO_CHAR(EJG.FECHAPRESENTACION, 'dd-mm-yyyy') as FECHAPRESENTACION");
			sql.append(" ,TO_CHAR(EJG.FECHALIMITEPRESENTACION, 'dd-mm-yyyy') as FECHALIMITEPRESENTACION, ");
			
			sql.append(" ejg.idpretension AS PRETENSION, ");
			sql.append(" ejg.idpretensioninstitucion AS PRETENSIONINSTITUCION ");
			
		       
			
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
	private Vector getSolicitanteCalificacionEjgSalida (String idInstitucion, String anioEjg, String tipoEjg, String numeroEjg,
			String idPersonaJG,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select f_siga_getrecurso(tgl.descripcion,:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append(" ) SITUACION_LABORAL,");
			sql.append(" nvl(pjg.nombre || ' ' || pjg.apellido1 || ' ' || pjg.apellido2, '-') as NOMBRE_APE_SOLIC, ");
			sql.append(" pjg.nif as NIFCIF_SOLIC, ");
			sql.append(" nvl(pjg.direccion, '-') as DIR_SOLIC, ");
			sql.append(" nvl(pjg.codigopostal, '-') as CP_SOLIC, ");
			sql.append(" nvl((SELECT F_SIGA_GETRECURSO(NOMBRE, 1) ");
			sql.append(" FROM CEN_POBLACIONES ");
			sql.append(" WHERE IDPOBLACION = pjg.idpoblacion), ");
			sql.append(" '-') as POB_SOLIC ");
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
	
	private Vector getFundamentoEjgSalida (String idInstitucion, String idFundamento,String idioma) throws ClsExceptions  
	{
				
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select       f_siga_getrecurso(nvl(tf.descripcion, '-'), :");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append(") as FUNDAMENTO ");
			sql.append(" from scs_tipofundamentocalif tf ");

			sql.append(" WHERE ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" tf.idinstitucion = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idFundamento);
			sql.append(" AND tf.idfundamentocalif  = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getFundamentoCalificacionEjgSalida");
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
	
	private Vector getComisariaEjgSalida (String idInstitucion, String idComisaria) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select COMDF.NOMBRE AS COMISARIA_DEFENSA_JURIDICA ");
			sql.append(" from  SCS_COMISARIA           COMDF ");
			
			sql.append(" WHERE ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" COMDF.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idComisaria);
			sql.append("  and COMDF.IDCOMISARIA = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
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
			String anioEjg, String numeroEjg,String idioma) throws ClsExceptions  
	{	 
		Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		try {
				
			vSalida = getCalificacionEjgSalida(idInstitucion, tipoEjg,anioEjg, numeroEjg,idioma); 
			//Como accedemos al ejg por clave primaria solo nos saldra un registro
			Hashtable registro = (Hashtable) vSalida.get(0);
			
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
			String idPersonaJG = (String)registro.get("IDPERSONAJG");
			helperInformes.completarHashSalida(registro,getSolicitanteCalificacionEjgSalida(idInstitucion,anioEjg,tipoEjg,numeroEjg,idPersonaJG,idioma));
			
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
				if(numProcedDJ!=null && !numProcedDJ.trim().equals("")){
					registro.put("AUTO", numProcedDJ);
				}
				String juzgadoDJ = (String)registro.get("JUZGADO_DEFENSA_JURIDICA");
				registro.put("JUZGADO", juzgadoDJ);
				
				String asuntoDJ = (String)registro.get("ASUNTO_DEFENSA_JURIDICA");
				registro.put("ASUNTO", asuntoDJ);
				
				
				
				
			
				
				
				
				
				
			}
			//Aniadimos los datos del colegiado del ejg
			String idPersona = (String)registro.get("IDPERSONA");
			helperInformes.completarHashSalida(registro,getColegiadoEjgCalificacionEjgSalida(idInstitucion, 
					idPersona));
			
			//Aniadimos los datos del procurador
			String idProcurador = (String)registro.get("IDPROCURADOR");
			String idInstitucionProc = (String)registro.get("IDINSTITUCIONPROC");
			helperInformes.completarHashSalida(registro,getProcuradorCalificacionEjgSalida(idInstitucionProc, 
					idProcurador,"PROCURADOR"));
			
			// Aniadimos el fundamento
			String idFundamento = (String)registro.get("IDFUNDAMENTOCALIF");
			helperInformes.completarHashSalida(registro,getFundamentoEjgSalida(idInstitucion, 
					idFundamento,idioma));
			
			//Aniadimos el Dictamen
			String idDictamen = (String)registro.get("IDTIPODICTAMENEJG");
			helperInformes.completarHashSalida(registro,getDictamenCalificacionEjgSalida(idInstitucion,idDictamen,idioma));
			
			
			//Aniadimos la comisaria
			String idComisaria = (String)registro.get("IDCOMISARIA");
			helperInformes.completarHashSalida(registro,getComisariaEjgSalida(idInstitucion, 
					idComisaria));
			
			//Aniadimos la pretension
			String idPretension = (String)registro.get("PRETENSION");
			String idPretensionInstitucion = (String)registro.get("PRETENSIONINSTITUCION");
			helperInformes.completarHashSalida(registro,getPretension(idPretension, idPretensionInstitucion));
							
			
			
			

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosInformeCalificacion");
		}
		return vSalida;
		
		
		
	}
	
	public Vector getEjgSalida (String idInstitucion, String tipoEjg,
			String anio, String numero,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT  ");
			sql.append(" EJGD.IDINSTITUCION DES_INSTITUCION,EJGD.IDTURNO DES_IDTURNO,EJGD.ANIODESIGNA DES_ANIO,EJGD.NUMERODESIGNA DES_NUMERO,  ");
			sql.append(" EJG.IDPERSONA,EJG.idfundamentocalif,EJG.IDPROCURADOR,EJG.IDINSTITUCION_PROC, ");
			sql.append(" EJG.JUZGADO AS IDJUZGADO_DJ, EJG.JUZGADOIDINSTITUCION AS JUZGADOIDINSTITUCION_DJ,EJG.COMISARIA, EJG.COMISARIAIDINSTITUCION, ");

			
			sql.append(" EJG.ANIO AS ANIO_EJG, ");
			sql.append(" to_char(EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA_EJG, ");
			sql.append(" EJG.OBSERVACIONES AS OBSERVACIONES, ");
		    sql.append(" (select observaciones from scs_designa des where des.IDINSTITUCION = EJGD.Idinstitucion and des.IDTURNO = EJGD.Idturno and des.ANIO = ejgd.aniodesigna and des.NUMERO = EJGD.Numerodesigna) as OBSERVACIONES_DESIGNA, ");			
			sql.append(" TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL, ");
			sql.append(" TO_CHAR(SYSDATE, 'dd/mm/yyyy') AS MES_ACTUAL, ");
			sql.append(" TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL, ");

			sql.append(" EJG.IDTIPOEJG, EJG.ANIO,EJG.NUMEJG as NUMERO, (EJG.ANIO || '/' || EJG.NUMEJG) as NUMERO_EJG , EJG.IDPERSONA, ");
			
			sql.append(" ejg.CALIDAD, ");
			
			sql.append(" DECODE(ejg.CALIDAD, null, '', 'D', ");
			sql.append(" 'gratuita.personaJG.calidad.literal.demandante', ");
			sql.append(" 'gratuita.personaJG.calidad.literal.demandado') ");
			sql.append(" AS CALIDAD_DEFENSA_JURIDICA, ");
			
			sql.append(" EJG.OBSERVACIONES AS ASUNTO_DEFENSA_JURIDICA, ");
			sql.append(" EJG.DELITOS AS COM_DEL_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMERO_CAJG AS NUMERO_CAJG_DEFENSA_JURIDICA, ");
			sql.append(" EJG.ANIOCAJG AS ANIO_CAJG_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMERODILIGENCIA AS NUMDILIGENCIA_DEFENSA_JURIDICA, ");
			sql.append(" EJG.NUMEROPROCEDIMIENTO AS NUMPROCED_DEFENSA_JURIDICA ");
			sql.append(" ,TO_CHAR(EJG.FECHA_DES_PROC,'dd-mm-yyyy') AS  FECHAEJG_PROCURADOR ");
			sql.append(" ,EJG.NUMERODESIGNAPROC AS  NUMDESIGNA_PROCURADOR ");
			sql.append(" ,EJG.idtipodictamenejg, ");
			sql.append(" TO_CHAR(EJG.fechadictamen,'dd-mm-yyyy') AS fechadictamen, ");
			sql.append(" EJG.dictamen, ");
			sql.append("  TO_CHAR(EJG.fecharesolucioncajg,'dd-mm-yyyy') AS fecharesolucioncajg, ");
			sql.append(" EJG.idtiporatificacionejg, ");
			sql.append(" TO_CHAR(EJG.fechanotificacion,'dd-mm-yyyy') AS fechanotificacion, ");
			sql.append(" EJG.refauto, ");
			sql.append(" EJG.ratificaciondictamen, ");
			sql.append(" TO_CHAR(EJG.fechaauto,'dd-mm-yyyy') AS fechaauto,");
			sql.append(" EJG.idtiporesolauto, ");
			sql.append(" EJG.idtiposentidoauto, ");
			
			sql.append(" (Select F_SIGA_GETRECURSO(DESCRIPCION, "+idioma+") as DESCRIPCION");
			sql.append("  From SCS_TIPOEJGCOLEGIO TEC");
			sql.append("  where tec.IDINSTITUCION = EJG.IDINSTITUCION and TEC.IDTIPOEJGCOLEGIO=EJG.IDTIPOEJGCOLEGIO) AS TIPO_EJG_COLEGIO ");
			
			sql.append(" ,TO_CHAR(EJG.Fecharatificacion, 'dd-mm-yyyy') AS Fecharatificacion ");
			
			sql.append(" ,TO_CHAR(EJG.FECHAPRESENTACION, 'dd-mm-yyyy') as FECHAPRESENTACION");
			sql.append(" ,TO_CHAR(EJG.FECHALIMITEPRESENTACION, 'dd-mm-yyyy') as FECHALIMITEPRESENTACION");

			sql.append(" ,to_char(EASI.FECHAHORA, 'dd/mm/yyyy') AS FECHA_ASISTENCIA ");
			sql.append(" ,  (select nombre ||' '||apellidos1||' '|| apellidos2 from cen_persona where idpersona = EASI.IDPERSONACOLEGIADO) AS NOMBRE_LETRADO_ASISTENCIA");
			
			// Campos necesarios para las comucioncaciones de la comision
			// Nos quedamos con los digitos para saber la cantidad que se reduce
			sql.append(" ,regexp_replace( (select F_SIGA_GETRECURSO(r.descripcion, 1) From Scs_Tiporesolucion r");
            sql.append(" where r.Idtiporesolucion=ejg.idtiporatificacionejg),'[^[:digit:]]','') as REDUCCION");
            // Las fechas en letra
		    sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.Fecharatificacion,'M',"+idioma+") AS Fecharatificacion_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.FECHAPRESENTACION,'M',"+idioma+") AS FECHAPRESENTACION_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.FECHALIMITEPRESENTACION,'M',"+idioma+") AS FECHALIMITEPRESENTACION_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EASI.FECHAHORA,'M',"+idioma+") AS FECHA_ASISTENCIA_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.fechaauto,'M',"+idioma+") AS fechaauto_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.fechanotificacion,'M',"+idioma+") AS fechanotificacion_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.fecharesolucioncajg,'M',"+idioma+") AS fecharesolucioncajg_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(EJG.FECHAAPERTURA,'M',"+idioma+") AS FECHAAPERTURA_EJG_LETRA ");
			sql.append(" , pkg_siga_fecha_en_letra.F_SIGA_FECHACOMPLETAENLETRA(SYSDATE,'M',"+idioma+") AS FECHAACTUAL_LETRA");

			sql.append(" FROM SCS_EJG EJG, SCS_EJGDESIGNA EJGD, SCS_ASISTENCIA EASI ");
			sql.append(" WHERE  ");

   
			sql.append(" EJG.IDINSTITUCION = EJGD.IDINSTITUCION(+) ");
			sql.append(" AND EJG.IDTIPOEJG = EJGD.IDTIPOEJG(+) ");
			sql.append(" AND EJG.ANIO = EJGD.ANIOEJG(+) ");
			sql.append(" AND EJG.NUMERO = EJGD.NUMEROEJG (+) ");
			
			sql.append(" and EJG.IDTIPOEJG = EASI.EJGIDTIPOEJG(+) ");
			sql.append(" AND EJG.NUMERO = EASI.EJGNUMERO(+) ");
			sql.append(" AND EJG.IDINSTITUCION = EASI.IDINSTITUCION(+) ");
			sql.append(" AND EJG.ANIO = EASI.EJGANIO(+) ");

     		sql.append(" AND  ");
			
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
			throw new ClsExceptions (e, "Error ScsEJGAdm.getEjgSalida.");
		}
	}
	
	
	
	
	private Vector getAsistenciaEjgSalida (String idInstitucion, String tipoEjg,
			String anio, String numero) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT  ");
			
			sql.append(" DECODE (ASI.NUMERODILIGENCIA,NULL, ");
			sql.append(" ASI.NUMEROPROCEDIMIENTO,ASI.NUMERODILIGENCIA) AS ASUNTODILIGENCIA, ");
			sql.append(" DECODE (ASI.COMISARIA,NULL, ");
			sql.append(" (SELECT J.NOMBRE FROM SCS_JUZGADO J WHERE J.IDINSTITUCION=ASI.JUZGADOIDINSTITUCION AND J.IDJUZGADO=ASI.JUZGADO), ");
			sql.append(" (SELECT C.NOMBRE FROM SCS_COMISARIA C WHERE C.IDINSTITUCION=ASI.COMISARIAIDINSTITUCION AND C.IDCOMISARIA=ASI.COMISARIA) ");
			sql.append(" ) AS COMISARIAJUZGADO ");
			sql.append(" FROM SCS_ASISTENCIA ASI ");
		
			
			
			sql.append(" WHERE "); 	
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" ASI.IDINSTITUCION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), tipoEjg);
			sql.append(" and ASI.EJGIDTIPOEJG = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), anio);
			sql.append(" and ASI.EJGANIO = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), numero);
			sql.append(" and ASI.EJGNUMERO = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getAsistenciaEjgSalida.");
		}
	}
	
	
	private Vector getProcuradorEjgSalida (String idInstitucion, String tipoEjg,
			String anio, String numero) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT  ");
			sql.append(" PROC.NOMBRE || ' ' || PROC.APELLIDOS1 || ' ' || ");
			sql.append(" PROC.APELLIDOS2 AS PROCURADOR, PROC.Domicilio AS PROCURADOR_DOMICILIO,");
			sql.append(" PROC.Codigopostal AS PROCURADOR_CP, PROC.Provincia AS PROCURADOR_PROVINCIA,");
			sql.append(" PROC.Poblacion AS PROCURADOR_POBLACION, PROC.Idpretencion AS IDPRETENCION");
			sql.append(" FROM V_SIGA_PROCURADOR_EJG         PROC ");					
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
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getProcuradorEjgSalida.");
		}
	}
	
	
	
	private Vector getInteresadosEjgSalida (String idInstitucion, String tipoEjg,
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
			sql.append(" INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' || ");
			sql.append("  INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO, ");
			//sql.append("  f_siga_getunidadejg(:1, :2, :3, :4) AS TOTAL_SOLICITANTE, ");
			sql.append(" INTERESADO.DIRECCION AS DOMICILIO_DEFENDIDO, ");
			sql.append(" INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO, ");
			sql.append(" INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO, ");
			sql.append(" INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO, ");
			sql.append(" INTERESADO.NIF AS NIF_DEFENDIDO, ");
			sql.append(" INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO, ");
			sql.append(" DECODE(INTERESADO.SEXO, null, null, 'M', ");
			sql.append(" F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append("), ");
			sql.append(" F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append(")) AS SEXO_INTERESADO, ");
			sql.append(" INTERESADO.IDLENGUAJE AS LENGUAJE_INTERESADO, ");
			sql.append(" DECODE(INTERESADO.CALIDAD, null, '', 'D', ");
			sql.append(" F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandante',:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append("), ");
			sql.append(" F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandado',:");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(keyContador);
			sql.append(")) AS CALIDAD_INTERESADO, ");
			sql.append(" DECODE(INTERESADO.ANIOEJG, NULL, NULL, INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG) AS NUMERO_EJG, ");
			sql.append(" F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE, ");
			sql.append(" INTERESADO.IDLENGUAJE AS IDLENGUAJE, ");
			sql.append(" f_siga_getrecurso (ESTADOCIVIL.DESCRIPCION,"+idioma+")  as ESTADOCIVIL_DEFENDIDO, ");
			sql.append(" to_char(PERSONAJG.FECHANACIMIENTO, 'DD/MM/YYYY')  FECHANAC_DEFENDIDO, ");
			sql.append("  DECODE(regimen_conyugal,'G', f_siga_getrecurso_etiqueta('gratuita.personaJG.regimen.literal.gananciales',"+idioma+"),");
            sql.append(" 'I', f_siga_getrecurso_etiqueta('gratuita.personaJG.regimen.literal.indeterminado',"+idioma+"),");
            sql.append(" 'S', f_siga_getrecurso_etiqueta('gratuita.personaJG.regimen.literal.separacion',"+idioma+")) as REGIMENCONYUGAL_DEFENDIDO,");
            sql.append(" f_siga_getrecurso (PROFESION.DESCRIPCION,"+idioma+") PROFESION_DEFENDIDO,");
            sql.append(" PERSONAJG.IDPERSONA IDPERSONA");
			sql.append(" FROM V_SIGA_INTERESADOS_EJG    INTERESADO, SCS_PERSONAJG PERSONAJG, CEN_ESTADOCIVIL ESTADOCIVIL, SCS_PROFESION PROFESION ");
  
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
			if(idPersonaJG!=null && !idPersonaJG.trim().equalsIgnoreCase("")){
				keyContador++;
				htCodigos.put(new Integer(keyContador), idPersonaJG);
				sql.append(" and INTERESADO.IDPERSONAJG = :");
				sql.append(keyContador);
				
			}
			sql.append(" AND PERSONAJG.IDINSTITUCION = INTERESADO.IDINSTITUCION ");
			sql.append(" AND PERSONAJG.IDPERSONA=INTERESADO.IDPERSONAJG ");	
			sql.append(" AND ESTADOCIVIL.IDESTADOCIVIL(+)=PERSONAJG.IDESTADOCIVIL ");
			sql.append(" AND PERSONAJG.IDPROFESION= PROFESION.IDPROFESION(+) ");
			
           
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		}
		catch (Exception e) {
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
			
			sql.append(" let.Idpersona IDPERSONA_DESIGNA,let.IDINSTITUCION IDINSTITUCION_LETDESIGNA , ");
			sql.append(" DES.IDINSTITUCION_JUZG IDINSTITUCION_JUZGDESIGNA ,DES.IDJUZGADO IDJUZGADODESIGNA, ");
			sql.append(" DES.NUMPROCEDIMIENTO AS AUTOS, ");
			sql.append(" TO_CHAR(DES.FECHAJUICIO, 'dd/MM/yyyy') AS FECHA_JUICIO, ");
			sql.append(" TO_CHAR(DES.FECHAJUICIO, 'HH24:MI') AS HORA_JUICIO, ");
			sql.append(" DES.IDPROCEDIMIENTO  AS IDPROCEDIMIENTO,");
		   
			
			sql.append(" DES.ANIO AS ANIO_DESIGNA, DES.CODIGO AS CODIGO, ");
			sql.append(" DES.RESUMENASUNTO AS ASUNTO, ");
			sql.append(" DES.ANIO || '/' || DES.CODIGO AS NOFICIO, ");
		   
		   // --DES.NOMBRE_PROCEDIMIENTO AS PROCEDIMIENTO,
			
			
			sql.append(" TO_CHAR(DES.FECHAENTRADA, 'dd-mm-yyyy') AS FECHA_DESIGNA ");
		 
			
		   
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
	
	
	
	
	
	public Vector getColegiadoSalida (String idInstitucionDesigna, String idPersonaDesigna,
			String idioma,String aliasSalida) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			
		
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" SELECT COLDES.NCOLEGIADO AS NCOLEGIADO");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,COLDES.NOMBRE || ' ' || COLDES.APELLIDOS1 || ' ' ||COLDES.APELLIDOS2 AS NOMBRE");
			sql.append("_");
			sql.append(aliasSalida);
			sql.append(" ,DECODE(COLDES.SEXO, null, null,'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_ST");
			sql.append("_");
			sql.append(aliasSalida);
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
	public Vector getDatosInformeEjg (String idInstitucion, String tipoEjg,
			String anioEjg, String numeroEjg,String idioma,boolean isSolicitantes,String idPersonaJG) throws ClsExceptions  
			{	 
		Vector vSalida = null;		
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		
		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		try {
			vSalida = new Vector();
			Vector vEjg = getEjgSalida(idInstitucion, tipoEjg,anioEjg, numeroEjg,idioma); 

			for (int j = 0; j < vEjg.size(); j++) {
				Hashtable registro = (Hashtable) vEjg.get(j);


				//Aniadimos los datos del colegiado del ejg
				
				String idLetradoEjg  = (String)registro.get("IDPERSONA");
				if(idLetradoEjg!=null && !idLetradoEjg.trim().equalsIgnoreCase("")){
					helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucion, 
							idLetradoEjg,idioma,"LETRADO"));
					String sexoLetradoEjg  = (String)registro.get("SEXO_ST_LETRADO");
					sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);
					registro.put("SEXO_LETRADO", sexoLetradoEjg);
					helperInformes.completarHashSalida(registro,getDireccionLetradoSalida(idLetradoEjg,idInstitucion,"LETRADO"));
					
					helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalida(idLetradoEjg,idInstitucion,"LETRADO"));
					
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
				
				//Aniadimos los contrarios de la defensa juridica

				Hashtable htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucion);
				htFuncion.put(new Integer(2), tipoEjg);
				htFuncion.put(new Integer(3), anioEjg);
				htFuncion.put(new Integer(4), numeroEjg);
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htFuncion, "F_SIGA_GETCONTRARIOS_EJG", "CONTRARIOS_DEFENSA_JURIDICA"));
				
				
				
				//Aniadimos los delitos de la defensa juridica
				htFuncion.put(new Integer(5), idioma);
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htFuncion, "F_SIGA_GETDELITOS_EJG", "DELITOS_DEFENSA_JURIDICA"));
				
				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucion);
				htFuncion.put(new Integer(2), anioEjg);
				htFuncion.put(new Integer(3), numeroEjg);
				htFuncion.put(new Integer(4), tipoEjg);
				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htFuncion, "f_siga_getunidadejg", "TOTAL_SOLICITANTE"));
				
				
				

				//Aniadimos los datos del procurador de la designa asociada a un EJG
				helperInformes.completarHashSalida(registro,getAsistenciaEjgSalida(idInstitucion, 
						tipoEjg,anioEjg,numeroEjg));

				Vector vprocuradorEjg = getProcuradorEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg);
					
					for (int l = 0; l < vprocuradorEjg.size(); l++) {
						Hashtable registroprocurador = (Hashtable) vprocuradorEjg.get(l);							
							String procurador = (String)registroprocurador.get("PROCURADOR");
							String ProProcurador = (String)registroprocurador.get("PROCURADOR_PROVINCIA");
							String PobProcurador = (String)registroprocurador.get("PROCURADOR_POBLACION");							
							String codigopostal= (String)registroprocurador.get("PROCURADOR_CP");
							String domicilioProcurador= (String)registroprocurador.get("PROCURADOR_DOMICILIO");
							String idpretencion= (String)registroprocurador.get("IDPRETENCION");
							
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
							
							if(idpretencion!=null && !idpretencion.trim().equalsIgnoreCase("")){			
								Vector vpretenciones=getPretension(idpretencion, idInstitucion);
									for (int s = 0; s < vpretenciones.size(); s++) {
										Hashtable registropretenciones = (Hashtable) vpretenciones.get(s);
										String procedimientodj = (String)registropretenciones.get("PRETENSION");
										
										if(procedimientodj!=null && !procedimientodj.trim().equalsIgnoreCase(""))
											registro.put("PROCEDIMIENTO_D_J", procedimientodj);
										else
											UtilidadesHash.set(registro, "PROCEDIMIENTO_D_J", "");
									}
								
							//	helperInformes.completarHashSalida(registro,getPretension(idpretencion, idInstitucion));
							}else {
								UtilidadesHash.set(registro, "PROCEDIMIENTO_D_J", "");
								
							}	
							
							
					}
				
				//Aniadimos los datos del procurador del ejg
				String idProcurador = (String)registro.get("IDPROCURADOR");
				String idInstitucionProc = (String)registro.get("IDINSTITUCION_PROC");
				Vector vprocuradorDjEjg = getDatosProcuradorEjgSalida(idInstitucionProc, idProcurador);				
	
					for (int l = 0; l < vprocuradorDjEjg.size(); l++) {
						Hashtable registroprocuradorDJ = (Hashtable) vprocuradorDjEjg.get(l);	
							String procuradordj = (String)registroprocuradorDJ.get("PROCURADOR_DEFENSA_JURIDICA");
							String ncolegiado = (String)registroprocuradorDJ.get("PROCURADOR_DJ_NCOLEGIADO");							
							String Procuradordjtel1 = (String)registroprocuradorDJ.get("PROCURADOR_DJ_TELEFONO1");
							String Procuradordjtel2 = (String)registroprocuradorDJ.get("PROCURADOR_DJ_TELEFONO2");							
							String domiciliodj= (String)registroprocuradorDJ.get("PROCURADOR_DOMICILIO_D_J");
							String provincia= (String)registroprocuradorDJ.get("PROCURADOR_PROVINCIA_D_J");
							String poblacion= (String)registroprocuradorDJ.get("PROCURADOR_POBLACION_D_J");
							String codigopostal= (String)registroprocuradorDJ.get("PROCURADOR_CODIGOPOSTAL_D_J");	
							
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
							
							if(codigopostal!=null && !codigopostal.trim().equalsIgnoreCase(""))
								registro.put("PROCURADOR_CODIGOPOSTAL_D_J", codigopostal);
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
							
					}
					
				// Aniadimos el fundamento del ejg
				String idFundamento = (String)registro.get("IDFUNDAMENTOCALIF");
				helperInformes.completarHashSalida(registro,getFundamentoEjgSalida(idInstitucion, 
						idFundamento,idioma));

				htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucion);
				htFuncion.put(new Integer(2), tipoEjg);
				htFuncion.put(new Integer(3), anioEjg);
				htFuncion.put(new Integer(4), numeroEjg);
				
				/*helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htFuncion, "F_SIGA_GETPROCURADORCONTR_EJG", "PROCURADOR_DJ_CONTRARIO"));*/
				
			/*inicio de la recuperacion de los datos del Procurador Contrario.*/
				
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
					for (int l = 0; l < datosprocuradorContrariodj.size(); l++) {
							Hashtable datosprocuradorContrario = (Hashtable) datosprocuradorContrariodj.get(l);	
								String procuradorcontrariodj = (String)datosprocuradorContrario.get("PROCURADOR_DJ_CONTRARIO");														
								String domiciliocontrio= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_DOMICI_D_J");
								String provinciacontrario= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_PROVIN_D_J");
								String poblacioncontrario= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_POBLA_D_J");
								String codigopostalcontrario= (String)datosprocuradorContrario.get("PROCURADOR_CONTRA_CP_D_J");	
								
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

				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htFuncion, "F_SIGA_GETABOGADOCONTRARIO_EJG", "ABOGADOSDEFENSA_JURIDICA"));
				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
						htFuncion, "F_SIGA_GETREPRESENTANTE_EJG", "REPRESENTANTESDEFENSA_JURIDICA"));

				//Aniadimos el Juzgado del ejg
				String idJuzgadoEjg = (String)registro.get("IDJUZGADO_DJ");
				String idInstitucionJuzgadoEjg = (String)registro.get("JUZGADOIDINSTITUCION_DJ");
				if(idJuzgadoEjg!=null && !idJuzgadoEjg.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getJuzgadoSalida(idInstitucionJuzgadoEjg, 
						idJuzgadoEjg,"D_J"));
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
					registro.put("ID_PROVINCIA_JUZGADO_D_J", " ");
					registro.put("ID_POBLACION_JUZGADO_D_J", " ");
					registro.put("JUZGADO_D_J", " ");
					
					
					
					

					
					
				}
				
				
				// Datos de la asistencia asociada
				String fechaAsistencia = (String)registro.get("FECHA_ASISTENCIA");				
				if(fechaAsistencia!=null && !fechaAsistencia.trim().equalsIgnoreCase("")){					
					registro.put("FECHA_ASISTENCIA", registro.get("FECHA_ASISTENCIA"));									 
				}else{
					registro.put("FECHA_ASISTENCIA", "");
				} 
				String letradoAsistencia = (String)registro.get("NOMBRE_LETRADO_ASISTENCIA");
				if(letradoAsistencia!=null && !fechaAsistencia.trim().equalsIgnoreCase("")){
					registro.put("NOMBRE_LETRADO_ASISTENCIA", letradoAsistencia);
					
				}else{
					registro.put("NOMBRE_LETRADO_ASISTENCIA", "");
				} 
				
				
				String idTipoResolAuto = (String)registro.get("IDTIPORESOLAUTO");
				if(idTipoResolAuto!=null && !idTipoResolAuto.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getTipoResolucionAutomatico(idTipoResolAuto));	
					
				}else{
					registro.put("DESC_TIPORESOLAUTO", " ");
					
					
				}
				
				
				String idTipoSentidoAuto = (String)registro.get("IDTIPOSENTIDOAUTO");
				if(idTipoSentidoAuto!=null && !idTipoSentidoAuto.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getTipoSentidoAutomatico(idTipoSentidoAuto));	
					
				}else{
					registro.put("DESC_TIPOSENTIDOAUTO", " ");
					
					
				}
				String idTipoDictamenEjg = (String)registro.get("IDTIPODICTAMENEJG");
				if(idTipoDictamenEjg!=null && !idTipoDictamenEjg.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getTipoDictamenEjg(idInstitucion,idTipoDictamenEjg));	
					
				}else{
					registro.put("DESC_TIPODICTAMENEJG", " ");
					
					
				}
				String idTipoRatificacionEjg = (String)registro.get("IDTIPORATIFICACIONEJG");
				if(idTipoRatificacionEjg!=null && !idTipoRatificacionEjg.trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getTipoRatificacionEjg(idTipoRatificacionEjg));	
					
				}else{
					registro.put("DESC_TIPORATIFICACIONEJG", " ");
					
					
				}
				
				
				//Aniadimos la comisaria del ejg
				String idComisariaEjg = (String)registro.get("COMISARIA");
				String idInstitucionComisariaEjg = (String)registro.get("COMISARIAIDINSTITUCION");
				helperInformes.completarHashSalida(registro,getComisariaEjgSalida(idInstitucionComisariaEjg, 
						idComisariaEjg));



				if(idComisariaEjg!=null && !idComisariaEjg.trim().equalsIgnoreCase("")){
					registro.put("LUGAR", registro.get("COMISARIA_DEFENSA_JURIDICA"));
				}else if(idJuzgadoEjg!=null && !idJuzgadoEjg.trim().equalsIgnoreCase("")){
					registro.put("LUGAR", registro.get("JUZGADO_DEFENSA_JURIDICA"));
				}else{

					registro.put("LUGAR", "");
				} 

				String numeroDesigna = (String)registro.get("DES_NUMERO");
				String anioDesigna = (String)registro.get("DES_ANIO");
				String idTurnoDesigna  = (String)registro.get("DES_IDTURNO");
				String idInstitucionDesigna  = (String)registro.get("DES_INSTITUCION");
				
				if(numeroDesigna!=null && !numeroDesigna.trim().equalsIgnoreCase("")){
					helperInformes.completarHashSalida(registro,getDesignaEjgSalida(idInstitucionDesigna, 
							idTurnoDesigna,anioDesigna,numeroDesigna,idioma));
					
					
					helperInformes.completarHashSalida(registro,helperInformes.getTurnoSalida(idInstitucion,idTurnoDesigna));
					
					String idProcedimiento = (String)registro.get("IDPROCEDIMIENTO");
					if(idProcedimiento==null || idProcedimiento.trim().equalsIgnoreCase("")){
					    idProcedimiento="-33"; // forzamos que no encuentre datos, en lugar de dar error
					}
					helperInformes.completarHashSalida(registro,helperInformes.getProcedimientoSalida(idInstitucion,idProcedimiento,""));
					
	
					htFuncion = new Hashtable();
					htFuncion.put(new Integer(1), idTurnoDesigna);
					htFuncion.put(new Integer(2), idInstitucionDesigna);
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htFuncion, "F_SIGA_NOMBRE_PARTIDO", "NOMBRE_PARTIDO"));
	
	
					htFuncion = new Hashtable();
					htFuncion.put(new Integer(1), idInstitucionDesigna);
					htFuncion.put(new Integer(2), idTurnoDesigna);
					htFuncion.put(new Integer(3), anioDesigna);
					htFuncion.put(new Integer(4), numeroDesigna);
	
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htFuncion, "F_SIGA_GETCONTRARIOS_DESIGNA", "CONTRARIOS"));
	
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htFuncion, "F_SIGA_GETPROCURADORCONT_DESIG", "PROCURADOR_CONTRARIOS"));
	
					htFuncion = new Hashtable();
					htFuncion.put(new Integer(1), idInstitucionDesigna);
					htFuncion.put(new Integer(2), anioDesigna);
					htFuncion.put(new Integer(3), idTurnoDesigna);
					htFuncion.put(new Integer(4), numeroDesigna);
					
					
					
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htFuncion, "F_SIGA_GETACTUACIONESDESIGNA", "LISTA_ACTUACIONES_DESIGNA"));
	
	
					htFuncion.put(new Integer(5), "dd-mm-yyyy");					
					
					helperInformes.completarHashSalida(registro,getFisrtAsistencia(htFuncion,"FECHA_ACTUACION"));				
					UtilidadesHash.set(registro, "FECHA_ACTUACION", (String)registro.get("FECHA_ACTUACION")); 
					
					htFuncion.put(new Integer(5), "hh:MI:ss");
	
					helperInformes.completarHashSalida(registro,getFisrtAsistencia(htFuncion,"HORA_ACTUACION"));
	
	
					htFuncion.put(new Integer(5), "0");
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htFuncion, "F_SIGA_GETINTERESADOSDESIGNA", "LISTA_INTERESADOS_DESIGNA"));
	
					htFuncion = new Hashtable();
					htFuncion.put(new Integer(1), idInstitucionDesigna);
					htFuncion.put(new Integer(2), numeroDesigna);
					htFuncion.put(new Integer(3), idTurnoDesigna);
					htFuncion.put(new Integer(4), anioDesigna);
					htFuncion.put(new Integer(5), idioma);
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htFuncion, "F_SIGA_GETDELITOS_DESIGNA", "DELITOS"));
	
	
					String idInstitucionLetradoDesigna  = (String)registro.get("IDINSTITUCION_LETDESIGNA");
					String idLetradoDesigna  = (String)registro.get("IDPERSONA_DESIGNA");
					 
					if(idLetradoDesigna!=null && !idLetradoDesigna.trim().equals("")){
						helperInformes.completarHashSalida(registro,getColegiadoSalida(idInstitucionLetradoDesigna, 
								idLetradoDesigna,idioma,"LETRADO_DESIGNADO"));
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
				if(isSolicitantes){
					Vector vDefendidos = getInteresadosEjgSalida(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,idPersonaJG);
					if(vDefendidos!=null && vDefendidos.size()>0){
						for (int k = 0; k < vDefendidos.size(); k++) {
							Hashtable clone = (Hashtable) registro.clone();
							Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);
							// jbd // Esto queda un poco feo, es porque getInteresadosEjgSalida siempre nos devuelve un registro,
							try{   // aunque no tenga datos y puede dar error al comunicar a un NO defendido
								Vector vDestinatario = admUniFam.getDatosInteresadoEjg(idInstitucion,tipoEjg,anioEjg,numeroEjg,idioma,(String)registroDefendido.get("IDPERSONA"));
								if(vDestinatario!=null && vDestinatario.size()>0){
									Hashtable destinatario = (Hashtable) vDestinatario.get(0);
									clone.putAll(destinatario);
								}
							}catch (Exception e) {
								
							}
							clone.putAll(registroDefendido);
							vSalida.add(clone);
						}
					}else{
						vSalida.add(registro);
					}	
				
				}else{
					vSalida.add(registro);
				}



			}




		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosInformeEjg");
		}
		return vSalida;



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

	public static Vector getPretension (String idPretension, String idPretensionInstitucion) throws ClsExceptions  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
	
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			sql.append(" select f_siga_getrecurso (DESCRIPCION,1) as PRETENSION ");
			sql.append(" from  SCS_PRETENSION PRET");
	
			sql.append(" WHERE ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPretension);
			sql.append(" PRET.IDPRETENSION = :");
			sql.append(keyContador);
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPretensionInstitucion);
			sql.append("  and PRET.IDINSTITUCION = :");
			sql.append(keyContador);
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getPretension");
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

	 
}

