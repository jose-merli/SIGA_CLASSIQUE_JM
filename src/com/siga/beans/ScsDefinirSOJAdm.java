/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_SOJ
 * 
 * Creado: julio.vicente 24/01/2005
 *  
 */

package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;


public class ScsDefinirSOJAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDefinirSOJAdm (UsrBean usuario) {
		super( ScsSOJBean.T_NOMBRETABLA, usuario);
	}
	
	/**
	 * Consulta a la base de datos para obtener el número de SOJ más alto más uno (para insertar en la base de datos posteriormente)
	 * 
	 * @param  
	 * @return Integer
	 */
	public Hashtable calcularNumeroMaximoSOJ (Hashtable miHash)throws ClsExceptions 
	{			
		RowsContainer rc = null;		
		RowsContainer rc1 = null;
		String numeroMaximo = null;
		String codigo="";
		
		try { 
			rc = new RowsContainer();	
			rc1 = new RowsContainer();
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsSOJBean.C_NUMERO + ") + 1) AS NUMERO FROM " + nombreTabla +  " WHERE IDINSTITUCION = " + miHash.get("IDINSTITUCION") + " AND IDTIPOSOJ = " + miHash.get("IDTIPOSOJ") + " AND ANIO = " + miHash.get("ANIO");
			
//			PDM INC-4774
			
			String sqlNumeroSoj ="SELECT (MAX(to_number(NUMSOJ)) + 1) AS NUMEROSOJ FROM " + nombreTabla + 
			" WHERE IDINSTITUCION =" + miHash.get("IDINSTITUCION") +
			" AND ANIO =" + miHash.get("ANIO");//Obtenemos el max(codigo)+1 por institucion y anio y asi poder crear una UK del campo Codigo por institucion y anio.
			
			GenParametrosAdm paramAdm = new GenParametrosAdm (this.usrbean);
			String longitudSOJ = paramAdm.getValor (this.usrbean.getLocation (), "SCS", "LONGITUD_CODSOJ", "");
				
			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("NUMERO").equals("")) {
					numeroMaximo = "1";
				}
				else numeroMaximo = prueba.get("NUMERO").toString();				
			}	
			
			if (rc1.query(sqlNumeroSoj)) {
				Row fila1 = (Row) rc1.get(0);
				Hashtable prueba1 = fila1.getRow();			
				if (prueba1.get("NUMEROSOJ").equals("")) {
					codigo="1";
					codigo=UtilidadesString.formatea(codigo,Integer.parseInt(longitudSOJ),true);
					miHash.put(ScsSOJBean.C_NUMSOJ,codigo);
				}
				else{
					codigo=(String)prueba1.get("NUMEROSOJ");
					codigo=UtilidadesString.formatea(codigo,Integer.parseInt(longitudSOJ),true);
					miHash.put(ScsSOJBean.C_NUMSOJ,codigo);								
				}
			}
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE NUMERO");
		};
		miHash.put("NUMERO",numeroMaximo);
		return miHash;
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
			String sql = "SELECT * FROM " + ScsSOJBean.T_NOMBRETABLA + " " + where ;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	 * Efectúa un SELECT en la tabla SCS_SOJ con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsSOJBean.C_IDINSTITUCION + " = " + hash.get(ScsSOJBean.C_IDINSTITUCION) + " AND " + ScsSOJBean.C_IDTIPOSOJ + " = " + hash.get(ScsSOJBean.C_IDTIPOSOJ) + 
						   " AND " + ScsSOJBean.C_ANIO + " = " + hash.get(ScsSOJBean.C_ANIO) + " AND " + ScsSOJBean.C_NUMERO + " = " + hash.get(ScsSOJBean.C_NUMERO);		
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
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	public String[] getCamposBean() {
		String[] campos = {	ScsSOJBean.C_IDINSTITUCION,		ScsSOJBean.C_IDTIPOSOJ,
							ScsSOJBean.C_ANIO,				ScsSOJBean.C_NUMERO,
							ScsSOJBean.C_IDTIPOSOJCOLEGIO,	ScsSOJBean.C_IDPERSONA,
							ScsSOJBean.C_IDTURNO,			ScsSOJBean.C_IDGUARDIA,
							ScsSOJBean.C_IDPERSONAJG,		ScsSOJBean.C_FECHAAPERTURA,							
							ScsSOJBean.C_ESTADO,			ScsSOJBean.C_DESCRIPCIONCONSULTA,
							ScsSOJBean.C_RESPUESTALETRADO,  ScsSOJBean.C_FECHAMODIFICACION,	
							ScsSOJBean.C_USUMODIFICACION,   ScsSOJBean.C_FACTURADO,							
							ScsSOJBean.C_PAGADO,			ScsSOJBean.C_NUMSOJ,
							ScsSOJBean.C_IDFACTURACION,		ScsSOJBean.C_IDTIPOCONSULTA,
							ScsSOJBean.C_IDTIPORESPUESTA, 	ScsSOJBean.C_EJGANIO,
							ScsSOJBean.C_EJGIDTIPOEJG,		ScsSOJBean.C_EJGNUMERO
							};

		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsSOJBean.C_IDINSTITUCION,	ScsSOJBean.C_IDTIPOSOJ, ScsSOJBean.C_ANIO, ScsSOJBean.C_NUMERO};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsSOJBean bean = null;
		try{
			bean = new ScsSOJBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsSOJBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsSOJBean.C_NUMERO));			
			bean.setIdTipoSOJ(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDTIPOSOJ));			
			bean.setEstado(UtilidadesHash.getString(hash,ScsSOJBean.C_ESTADO));
			bean.setFechaApertura(UtilidadesHash.getString(hash,ScsSOJBean.C_FECHAAPERTURA));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDGUARDIA));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDTURNO));
			bean.setIdPersona(UtilidadesHash.getLong(hash,ScsSOJBean.C_IDPERSONA));
			bean.setIdPersonaJG(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDPERSONAJG));
			bean.setIdTipoSOJColegio(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDTIPOSOJCOLEGIO));
			bean.setDescripcionConsulta(UtilidadesHash.getString(hash,ScsSOJBean.C_DESCRIPCIONCONSULTA));
			bean.setRespuestaLetrado(UtilidadesHash.getString(hash,ScsSOJBean.C_RESPUESTALETRADO));
			bean.setIdFacturacion(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDFACTURACION));
			bean.setIdFActuracion(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDFACTURACION));
			bean.setFacturado(UtilidadesHash.getString(hash,ScsSOJBean.C_FACTURADO));
			bean.setPagado(UtilidadesHash.getString(hash,ScsSOJBean.C_PAGADO));
			bean.setNumSOJ(UtilidadesHash.getString(hash,ScsSOJBean.C_NUMSOJ));
			bean.setIdTipoConsulta(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDTIPOCONSULTA));
			bean.setIdTipoRespuesta(UtilidadesHash.getInteger(hash,ScsSOJBean.C_IDTIPORESPUESTA));
			bean.setEjgAnio(UtilidadesHash.getInteger(hash, ScsSOJBean.C_EJGANIO));
			bean.setEjgIdTipoEJG(UtilidadesHash.getInteger(hash, ScsSOJBean.C_EJGIDTIPOEJG));
			bean.setEjgNumero(UtilidadesHash.getInteger(hash, ScsSOJBean.C_EJGNUMERO));
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
	 * 
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		
		try{
			ScsSOJBean miBean = (ScsSOJBean) bean;			
			UtilidadesHash.set(hash, ScsSOJBean.C_IDINSTITUCION, miBean.getIdInstitucion());
			UtilidadesHash.set(hash, ScsSOJBean.C_ANIO, miBean.getAnio());
			UtilidadesHash.set(hash, ScsSOJBean.C_NUMERO, miBean.getNumero());
			UtilidadesHash.set(hash, ScsSOJBean.C_DESCRIPCIONCONSULTA, miBean.getDescripcionConsulta());
			UtilidadesHash.set(hash, ScsSOJBean.C_ESTADO, miBean.getEstado());
			UtilidadesHash.set(hash, ScsSOJBean.C_FECHAAPERTURA, miBean.getFechaApertura());
			UtilidadesHash.set(hash, ScsSOJBean.C_FACTURADO, miBean.getFacturado());
			UtilidadesHash.set(hash, ScsSOJBean.C_FECHAMODIFICACION, miBean.getFechaMod());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDFACTURACION, miBean.getIdFacturacion());			
			UtilidadesHash.set(hash, ScsSOJBean.C_IDGUARDIA, miBean.getIdGuardia());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDPERSONA, miBean.getIdPersona());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDPERSONAJG, miBean.getIdPersonaJG());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDTIPOSOJ, miBean.getIdTipoSOJ());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDTIPOSOJCOLEGIO, miBean.getIdTipoSOJColegio());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDTURNO, miBean.getIdTurno());
			UtilidadesHash.set(hash, ScsSOJBean.C_PAGADO, miBean.getPagado());
			UtilidadesHash.set(hash, ScsSOJBean.C_RESPUESTALETRADO, miBean.getRespuestaLetrado());
			UtilidadesHash.set(hash, ScsSOJBean.C_USUMODIFICACION, miBean.getUsuMod());
			UtilidadesHash.set(hash, ScsSOJBean.C_NUMSOJ, miBean.getNumSOJ());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDTIPOCONSULTA, miBean.getIdTipoConsulta());
			UtilidadesHash.set(hash, ScsSOJBean.C_IDTIPORESPUESTA, miBean.getIdTipoRespuesta());
			UtilidadesHash.set(hash, ScsSOJBean.C_EJGANIO, miBean.getEjgAnio());
			UtilidadesHash.set(hash, ScsSOJBean.C_EJGIDTIPOEJG, miBean.getEjgIdTipoEJG());
			UtilidadesHash.set(hash, ScsSOJBean.C_EJGNUMERO, miBean.getEjgNumero());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		String[] orden = {ScsSOJBean.C_IDINSTITUCION, ScsSOJBean.C_IDTIPOSOJ, ScsSOJBean.C_ANIO, ScsSOJBean.C_NUMERO};
		return orden;
	}
	
	/** Funcion getCamposActualizablesBean ()
	 *  @return conjunto de datos con los nombres de los campos actualizables
	 * */
	protected String[] getCamposActualizablesBean ()
	{
		/*String[] campos = {	ScsSOJBean.C_IDPERSONA, 			ScsSOJBean.C_ESTADO,
							ScsSOJBean.C_DESCRIPCIONCONSULTA, 	ScsSOJBean.C_RESPUESTALETRADO
						  };*/
	    return getCamposBean();
	}
	
	public Hashtable getTituloPantallaSOJ (String idInstitucion, String anio, String numero, String idTipoSOJ) 
	{
		try {
			String sql = 	"select " + ScsPersonaJGBean.C_NOMBRE + "," + 
			                            ScsPersonaJGBean.C_APELLIDO1 + ", " + 
										ScsPersonaJGBean.C_APELLIDO2 + ", " + 
										ScsSOJBean.C_ANIO + ", " +
										ScsSOJBean.C_NUMSOJ +", "+
										ScsSOJBean.C_SUFIJO +", "+
										"(select "+UtilidadesMultidioma.getCampoMultidiomaSimple(ScsTipoSOJ.C_DESCRIPCION,this.usrbean.getLanguage())+
										" from "+ ScsTipoSOJ.T_NOMBRETABLA+
										" where "+ScsTipoSOJ.T_NOMBRETABLA+"."+ScsTipoSOJ.C_IDTIPOSOJ+"=a."+ScsSOJBean.C_IDTIPOSOJ+") TIPOSOJ"+
										
							" from " + ScsPersonaJGBean.T_NOMBRETABLA + " p, " + ScsSOJBean.T_NOMBRETABLA + " a " + 
							" where a." + ScsSOJBean.C_IDINSTITUCION + " = " + idInstitucion +  
							  " and a." + ScsSOJBean.C_ANIO + " = " + anio +
							  " and a." + ScsSOJBean.C_NUMERO  + " = " + numero +
							  " and a." + ScsSOJBean.C_IDTIPOSOJ  + " = " + idTipoSOJ +
							  " and a." + ScsSOJBean.C_IDPERSONAJG + " = p." + ScsPersonaJGBean.C_IDPERSONA+"(+)" +
							  " and a." + ScsSOJBean.C_IDINSTITUCION+ " = p." + ScsPersonaJGBean.C_IDINSTITUCION+"(+)";
	
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
	
	public Hashtable existeTramitadorSOJ(String institucion,String numero, String anio, String tipoSOJ){
		
		//ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		
		Vector v=new Vector();
		Hashtable h=new Hashtable();
		
//		String sql="SELECT persona.idpersona AS IDPERSONA" +
//		"  FROM scs_soj soj, cen_colegiado colegiado, cen_persona persona" +
//		" WHERE soj.idpersona = persona.idpersona(+)" +//
//		"   and soj.idinstitucion = colegiado.idinstitucion(+)" +//
//		"   and soj.idpersona = colegiado.idpersona(+)" +//
//		"   and soj.idinstitucion = "+institucion+
//		"   and soj.anio = "+anio+
//		"   and soj.numero = "+ numero +
//		"   and soj.idtiposoj = "+ tipoSOJ;
		
		String sql="SELECT soj.idpersona AS IDPERSONA" +
		"  FROM scs_soj soj" +
		" WHERE soj.idinstitucion = "+institucion+
		"   and soj.anio = "+anio+
		"   and soj.numero = "+ numero +
		"   and soj.idtiposoj = "+ tipoSOJ;
		
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
	
	public PaginadorBind getBusquedaSOJ(String idInstitucion, Hashtable miHash) throws ClsExceptions , SIGAException{
		
		String consulta = "";
		Hashtable codigosBind = new Hashtable();
		int contador=0;
	  	
	  	// Acceso a BBDD
		int totalRegistros=0;
		
		
		
		try {
			//String bBusqueda = formulario.getChkBusqueda();
			consulta = "select soj.IDINSTITUCION,soj.ANIO, " +
			   " soj.NUMSOJ," +
	       " soj.NUMERO," +
	       " soj.IDTIPOSOJ," +
	       " soj.ESTADO," +
	       " soj.FECHAAPERTURA," +
	       " soj. IDPERSONAJG," +
	       " soj. IDFACTURACION," +
	       //" turno.IDTURNO," +
	       //" turno.NOMBRE AS+ NOMBRE," +
		   " soj.IDTURNO,"+
		   " soj.IDGUARDIA,"+
		   
		   " soj.IDTIPOSOJCOLEGIO, "+
		   " soj.SUFIJO,"+
		   " (persona.nombre || ' ' || persona.apellido1||' '|| persona.apellido2) as nombre"+
		   //" guardia.IDGUARDIA," +
	       //" guardia.NOMBRE AS NOMBREGUARDIA," +
	       //" tiposoj.DESCRIPCION AS DESCRIPCION," +
	       //" tiposojcolegio.DESCRIPCION AS DESCRIPCIONSOJCOLEGIO" +
	       " from SCS_SOJ            soj," +
	 // " SCS_TURNO          turno," +
	  //     " SCS_GUARDIASTURNO  guardia," +
	       " CEN_COLEGIADO      letrado," +
	       " SCS_PERSONAJG      persona " +
	       
	  //     " SCS_TIPOSOJ        tiposoj," +
	  //     " SCS_TIPOSOJCOLEGIO tiposojcolegio" +
	       " where " +
	      // "soj.IDINSTITUCION = turno.IDINSTITUCION (+)" +
	   //" and soj.IDTURNO = turno.IDTURNO (+) " +
	   //" and soj.IDINSTITUCION = guardia.IDINSTITUCION (+)" +
	   //" and soj.IDGUARDIA = guardia.IDGUARDIA (+)" +
	   //" and soj.IDTURNO = guardia.IDTURNO (+)" +
	   //" and " +
	   "soj.IDINSTITUCION = persona.IDINSTITUCION(+)" + 
	   " and soj.IDPERSONAJG = persona.IDPERSONA(+) " +
	   //"    and soj.IDTIPOSOJ = tiposoj.IDTIPOSOJ" +
	  // "    and soj.IDINSTITUCION = tiposojcolegio.IDINSTITUCION(+)" +
	  // "    and soj.IDTIPOSOJCOLEGIO = tiposojcolegio.IDTIPOSOJCOLEGIO(+)" +
	   " and soj.IDINSTITUCION = letrado.IDINSTITUCION (+)" +
	   " and soj.IDPERSONA = letrado.IDPERSONA (+)";   
			
	   contador++;
	   codigosBind.put(new Integer(contador),idInstitucion);		
	   consulta+=" and soj.IDINSTITUCION=:"+contador;
			
			/* Se van anhadiendo restricciones en los resultados en función de los criterios de búsqueda */							
			if (miHash.get(ScsSOJBean.C_ANIO).toString().length() > 0){ 
				
				contador++;
			    codigosBind.put(new Integer(contador),miHash.get(ScsSOJBean.C_ANIO).toString());
				consulta += " and soj." + ScsSOJBean.C_ANIO + " = :" + contador; 
			}
			
	        //		El código para la busqueda se trata como un numerico para que si no meten comodines(*) 
	        // haga la busqueda exacta y si intentan buscar el numero 3 devuelve el 3,03,003, etc...
			if (UtilidadesHash.getString(miHash,ScsSOJBean.C_NUMSOJ) != null && !UtilidadesHash.getString(miHash,ScsSOJBean.C_NUMSOJ).equalsIgnoreCase("")) {
			    if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash,ScsSOJBean.C_NUMSOJ))){
			    	contador++;
			    	consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(((String)UtilidadesHash.getString(miHash,ScsSOJBean.C_NUMSOJ)).trim(),"soj." + ScsSOJBean.C_NUMSOJ,contador,codigosBind ); 
			    }else{ 
			    	contador++;
				    codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,ScsSOJBean.C_NUMSOJ).trim());
			    	consulta += " AND ltrim(soj." + ScsSOJBean.C_NUMSOJ+",'0') = ltrim(:" +contador+",'0')";	
			    	
			    }	
			}
			
			
			 String fAperturaDesde = miHash.get("FECHAAPERTURADESDE").toString(); 
			   String fAperturaHasta = miHash.get("FECHAAPERTURAHASTA").toString();
				if ((fAperturaDesde != null && !fAperturaDesde.trim().equals("")) || (fAperturaHasta != null && !fAperturaHasta.trim().equals(""))) {
					if (!fAperturaDesde.equals(""))
						fAperturaDesde = GstDate.getApplicationFormatDate("", fAperturaDesde); 
					if (!fAperturaHasta.equals(""))
						fAperturaHasta = GstDate.getApplicationFormatDate("", fAperturaHasta);
					
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("soj."+ScsSOJBean.C_FECHAAPERTURA,
							fAperturaDesde,
							fAperturaHasta,
							  contador,
							  codigosBind);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					consulta +=" and " + vCondicion.get(1);
					
				}
			
			if (miHash.get(ScsSOJBean.C_IDPERSONA).toString().length() > 0){
				contador++;
			    codigosBind.put(new Integer(contador),miHash.get(ScsSOJBean.C_IDPERSONA).toString().trim());
				consulta += " and letrado." + CenColegiadoBean.C_IDPERSONA + " = :" + contador;
			}	
			if (miHash.get(ScsSOJBean.C_ESTADO).toString().length() > 0){
				contador++;
			    codigosBind.put(new Integer(contador),miHash.get(ScsSOJBean.C_ESTADO).toString().trim());
				consulta += " and soj." + ScsSOJBean.C_ESTADO + " = :" + contador;
			}	
			if (miHash.get(ScsSOJBean.C_IDTURNO).toString().length() > 0) {
				/*El identificador de turno está compuesto por (idinstitución,idturno), y sólo nos interesa el idturno, por
				 * ello debemos parsear la clave */			 
				contador++;
				String turno = miHash.get(ScsSOJBean.C_IDTURNO).toString();		
			    codigosBind.put(new Integer(contador),turno.substring(turno.indexOf(",")+1));
					
				consulta += " and soj." + ScsSOJBean.C_IDTURNO + " = :" + contador;
			}
			if (miHash.get(ScsSOJBean.C_IDGUARDIA).toString().length() > 0){
				contador++;
		        codigosBind.put(new Integer(contador),miHash.get(ScsSOJBean.C_IDGUARDIA).toString().trim());
				consulta += " and soj." + ScsSOJBean.C_IDGUARDIA + " = :" + contador;
			}	
			if (miHash.get(ScsSOJBean.C_IDTIPOSOJ).toString().length() > 0){
				contador++;
		        codigosBind.put(new Integer(contador),miHash.get(ScsSOJBean.C_IDTIPOSOJ).toString().trim());
				consulta += " and soj." + ScsSOJBean.C_IDTIPOSOJ + " = :" + contador;
			}	
			if (miHash.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString().length() > 0){
				contador++;
		        codigosBind.put(new Integer(contador),miHash.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString().trim());
				consulta += " and soj." + ScsSOJBean.C_IDTIPOSOJCOLEGIO + " = :" + contador;
			}	
//			Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso
			   // de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte 
			   // la consulta adecuada.		
			if (miHash.get("NIF").toString().length() > 0){
			  if (ComodinBusquedas.hasComodin(miHash.get("NIF").toString())){	
			  	contador++;
				consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(miHash.get("NIF").toString().trim(),"persona." + ScsPersonaJGBean.C_NIF,contador,codigosBind );
			  }else{
			  	contador++;
			  	//consulta +=" AND "+ComodinBusquedas.prepararSentenciaNIFBind(miHash.get("NIF").toString(),"UPPER(persona." + ScsPersonaJGBean.C_NIF+")",contador,codigosBind);
			  	consulta +=" AND "+ComodinBusquedas.prepararSentenciaNIFBind(miHash.get("NIF").toString(),"persona." + ScsPersonaJGBean.C_NIF,contador, codigosBind);
			  }
			}	
			if (miHash.get("NOMBRE").toString().length() > 0){
				contador++;
		        
				consulta += " and "+ ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("NOMBRE")).trim(),"persona." + ScsPersonaJGBean.C_NOMBRE,contador,codigosBind );
			}	
			if (miHash.get("APELLIDO1").toString().length() > 0){
				contador++;
				consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO1")).trim(),"persona." + ScsPersonaJGBean.C_APELLIDO1,contador,codigosBind);
			}	
			if (miHash.get("APELLIDO2").toString().length() > 0){
				contador++;
				consulta += " and "+ComodinBusquedas.prepararSentenciaCompletaBind(((String)miHash.get("APELLIDO2")).trim(),"persona." + ScsPersonaJGBean.C_APELLIDO2,contador,codigosBind);
			}	
			
			consulta += " ORDER BY soj." + ScsSOJBean.C_ANIO + " DESC , to_number(soj." + ScsSOJBean.C_NUMSOJ+") desc";
		
      // No utilizamos la clase Paginador para la busqueda de letrados porque al filtrar por residencia la consulta no devolvia bien los 
      //  datos que eran de tipo varchar (devolvía n veces el mismo resultado), utilizamos el paginador PaginadorCaseSensitive
       // y hacemos a parte el tratamiento de mayusculas y signos de acentuación
       PaginadorBind paginador = new PaginadorBind(consulta,codigosBind);
        totalRegistros = paginador.getNumeroTotalRegistros();
 		
 		if (totalRegistros==0){					
 			paginador =null;
 		}
      
		
       
		
		return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes colegiados"); 
		}
	}
	
	static public String getNombreTipoSOJ (String idTipoSOJ)  throws ClsExceptions,SIGAException 
	{
		
		Vector v;
		Hashtable codigos = new Hashtable();
		String consulta="";
		try {
		    codigos.put(new Integer(1),idTipoSOJ);
		    
		    consulta="select descripcion NOMBRE_SOJ from scs_tiposoj where idTipoSOJ=:1 ";
		    RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(consulta, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "NOMBRE_SOJ");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el nombre del tipo SOJ.");
	   			}
	   		}	
	    }
		return null;
	}
	
	static public String getNombreTipoSOJColegio (String idinstitucion,String idTipoSOJColegio)  throws ClsExceptions,SIGAException 
	{
		
		Vector v;
		Hashtable codigos = new Hashtable();
		String consulta="";
		try {
		    codigos.put(new Integer(1),idinstitucion);
		    codigos.put(new Integer(2),idTipoSOJColegio);
		    consulta="select descripcion NOMBRE_SOJCOLEGIO from scs_tiposojcolegio where idinstitucion=:1 and idTipoSOJColegio=:2 ";
		    RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(consulta, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "NOMBRE_SOJCOLEGIO");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el nombre del tipo SOJ Colegio.");
	   			}
	   		}	
	    }
		return null;
	}
	public Hashtable getDatosInformeSOJ(Integer idInstitucion,
			Integer idTipoSOJ,
			Integer anio,
			Integer numero) throws ClsExceptions
	{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SOJ.*, ");
		sql.append(" F_SIGA_GETRECURSO(TIPOSOJ.DESCRIPCION, NVL(PERS.IDLENGUAJE,"+usrbean.getLanguage()+")) DESCRIPCIONTIPOSOJ, ");
		sql.append(" F_SIGA_GETRECURSO(TIPOSOJC.DESCRIPCION, NVL(PERS.IDLENGUAJE,"+usrbean.getLanguage()+")) DESCRIPCIONTIPOSOJCOLEGIO, ");
		sql.append(" F_SIGA_GETRECURSO(TC.DESCRIPCION, NVL(PERS.IDLENGUAJE,"+usrbean.getLanguage()+")) DESCRIPCIONTIPOCONSULTA, ");
		sql.append(" F_SIGA_GETRECURSO(TR.DESCRIPCION, NVL(PERS.IDLENGUAJE,"+usrbean.getLanguage()+")) DESCRIPCIONTIPORESPUESTA, ");
		sql.append(" EJG.NUMEJG ");
		sql.append(" FROM SCS_SOJ       SOJ, ");
		sql.append(" SCS_TIPOSOJ        TIPOSOJ, ");
		sql.append(" SCS_TIPOSOJCOLEGIO TIPOSOJC, ");
		sql.append(" SCS_TIPOCONSULTA   TC, ");
		sql.append(" SCS_TIPORESPUESTA  TR, ");
		sql.append(" SCS_EJG            EJG, ");
		sql.append(" SCS_PERSONAJG 		PERS ");
		sql.append(" WHERE SOJ.IDINSTITUCION = EJG.IDINSTITUCION(+) ");
		sql.append(" AND SOJ.EJGIDTIPOEJG = EJG.IDTIPOEJG(+) ");
		sql.append(" AND SOJ.EJGANIO = EJG.ANIO(+) ");
		sql.append(" AND SOJ.EJGNUMERO = EJG.NUMERO(+) ");	      
		sql.append(" AND SOJ.IDINSTITUCION = TC.IDINSTITUCION(+) ");
		sql.append(" AND SOJ.IDTIPOCONSULTA = TC.IDTIPOCONSULTA(+) ");	      
		sql.append(" AND SOJ.IDINSTITUCION = TR.IDINSTITUCION(+) ");
		sql.append(" AND SOJ.IDTIPORESPUESTA = TR.IDTIPORESPUESTA(+) ");	      
		sql.append(" AND SOJ.IDINSTITUCION = TIPOSOJC.IDINSTITUCION(+) ");
		sql.append(" AND SOJ.IDTIPOSOJCOLEGIO = TIPOSOJC.IDTIPOSOJCOLEGIO(+) ");	      
		sql.append(" AND SOJ.IDTIPOSOJ = TIPOSOJ.IDTIPOSOJ ");
		sql.append(" AND SOJ.IDINSTITUCION = PERS.IDINSTITUCION(+) ");
		sql.append(" AND SOJ.IDPERSONAJG = PERS.IDPERSONA(+) ");
	    
		sql.append(" AND SOJ.IDINSTITUCION = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idInstitucion);
		
		sql.append(" AND SOJ.IDTIPOSOJ = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idTipoSOJ);
		
		sql.append(" AND SOJ.ANIO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), anio);
		
		sql.append(" AND SOJ.NUMERO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), numero);
		
		Hashtable sojHashtable =  new Hashtable();
		try {
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					sojHashtable = fila.getRow();
					
					actualizarDatosSOJ(sojHashtable);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return sojHashtable;

	}
	private void actualizarDatosGeneralesSOJ(Hashtable sojHashtable) throws ClsExceptions{
		String fechaAPertura = UtilidadesHash.getString(sojHashtable, ScsSOJBean.C_FECHAAPERTURA);
		UtilidadesHash.set(sojHashtable, ScsSOJBean.C_FECHAAPERTURA, GstDate.getFormatedDateShort("", fechaAPertura));
		if(UtilidadesHash.getString(sojHashtable, ScsSOJBean.C_ESTADO).equals("A")){
			UtilidadesHash.set(sojHashtable, ScsSOJBean.C_ESTADO, UtilidadesString.getMensajeIdioma(this.usrbean,"gratuita.SOJ.estado.abierto"));
		}else if(UtilidadesHash.getString(sojHashtable, ScsSOJBean.C_ESTADO).equals("P")){
			UtilidadesHash.set(sojHashtable, ScsSOJBean.C_ESTADO, UtilidadesString.getMensajeIdioma(this.usrbean,"gratuita.SOJ.estado.pendiente"));
		}else if(UtilidadesHash.getString(sojHashtable, ScsSOJBean.C_ESTADO).equals("C")){
			UtilidadesHash.set(sojHashtable, ScsSOJBean.C_ESTADO, UtilidadesString.getMensajeIdioma(this.usrbean,"gratuita.SOJ.estado.cerrado"));
		}
		
		String numSOJ  = (String)sojHashtable.get(ScsSOJBean.C_NUMSOJ);
		if(numSOJ!=null && !numSOJ.trim().equals("")){
			sojHashtable.put("CODIGO_SOJ", (String)sojHashtable.get(ScsSOJBean.C_ANIO)+"/"+numSOJ);
		}else{
			sojHashtable.put("CODIGO_SOJ", "");
		}
		
		//ponemos el codigo de ejg
		String ejgAnio  = (String)sojHashtable.get(ScsSOJBean.C_EJGANIO);
		if(ejgAnio!=null && !ejgAnio.trim().equals("")){
			sojHashtable.put("CODIGO_EJG", (String)sojHashtable.get(ScsSOJBean.C_EJGANIO)+"/"+(String)sojHashtable.get(ScsEJGBean.C_NUMEJG));
		}else{
			sojHashtable.put("CODIGO_EJG", "");
		}
		
	}
	private void actualizarDatosGuardiaSOJ(Hashtable sojHashtable) throws ClsExceptions{
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		String idTurno  = (String)sojHashtable.get("IDTURNO");
		String idInstitucion  = (String)sojHashtable.get("IDINSTITUCION");
		String idGuardia = (String)sojHashtable.get("IDGUARDIA");
		if(idTurno!=null && !idTurno.trim().equals("") && idGuardia!=null && !idGuardia.trim().equals("")){
			Hashtable htFuncion = new Hashtable();
			htFuncion.put(new Integer(1), idInstitucion);
			htFuncion.put(new Integer(2), idTurno);
			htFuncion.put(new Integer(3), idGuardia);
			helperInformes.completarHashSalida(sojHashtable,helperInformes.ejecutaFuncionSalida(htFuncion, "f_siga_getnombreguardia", "NOMBRE_GUARDIA"));
			  
			
			helperInformes.completarHashSalida(sojHashtable,helperInformes.getTurnoSalida(idInstitucion,idTurno));
		}else{
			sojHashtable.put("NOMBRE_GUARDIA", ""); 
			
		}
		
		
	}
	
	
	private void actualizarDatosTurnoSOJ(Hashtable sojHashtable) throws ClsExceptions{
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		  
		  
		String idTurno  = (String)sojHashtable.get("IDTURNO");
		if(idTurno!=null && !idTurno.trim().equals("") ){
			String idInstitucion  = (String)sojHashtable.get("IDINSTITUCION");
			helperInformes.completarHashSalida(sojHashtable,helperInformes.getTurnoSalida(idInstitucion,idTurno));
		}else{
			sojHashtable.put("DESCRIPCION_TURNO", "");
			sojHashtable.put("ABREV_TURNO", "");
			
		}
		
		
	}
	private void actualizarDatosSOJ(Hashtable sojHashtable) throws ClsExceptions{
		actualizarDatosGeneralesSOJ(sojHashtable);
		actualizarDatosGuardiaSOJ(sojHashtable);
		actualizarDatosTurnoSOJ(sojHashtable);
		actualizarDatosLetrado(sojHashtable);
		actualizarDatosPersonaJG(sojHashtable);
		actualizarDatosDocumentacion(sojHashtable);
		
	}
	private void actualizarDatosLetrado(Hashtable sojHashtable) throws ClsExceptions{
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		String idLetradoEjg  = (String)sojHashtable.get("IDPERSONA");
		String idInstitucion  = UtilidadesHash.getString(sojHashtable,"IDINSTITUCION");
		ScsEJGAdm ejgAdm = new ScsEJGAdm(this.usrbean);
		if(idLetradoEjg!=null && !idLetradoEjg.trim().equalsIgnoreCase("")){
			helperInformes.completarHashSalida(sojHashtable,ejgAdm.getColegiadoSalida(idInstitucion, 
					idLetradoEjg,"LETRADO"));
			String sexoLetradoEjg  = (String)sojHashtable.get("SEXO_ST_LETRADO");
			sexoLetradoEjg = UtilidadesString.getMensajeIdioma(usrbean, sexoLetradoEjg);
			sojHashtable.put("SEXO_LETRADO", sexoLetradoEjg);
			helperInformes.completarHashSalida(sojHashtable,ejgAdm.getDireccionLetradoSalida(idLetradoEjg,idInstitucion,"LETRADO"));
			
			helperInformes.completarHashSalida(sojHashtable,ejgAdm.getDireccionPersonalLetradoSalida(idLetradoEjg,idInstitucion,"LETRADO"));			
			String telefonoDespacho = (String)sojHashtable.get("TELDESPACHO_LETRADO");
			if(telefonoDespacho!=null)
				UtilidadesHash.set(sojHashtable, "TELEFONODESPACHO_LETRADO", telefonoDespacho);
			else
				UtilidadesHash.set(sojHashtable, "TELEFONODESPACHO_LETRADO", "");
			
			String pobLetradoEjg = (String)sojHashtable.get("POBLACION_LETRADO");
			if(pobLetradoEjg==null ||pobLetradoEjg.trim().equalsIgnoreCase("")){
				String idPobLetradoEjg = (String)sojHashtable.get("ID_POBLACION_LETRADO");
				helperInformes.completarHashSalida(sojHashtable,helperInformes.getNombrePoblacionSalida(idPobLetradoEjg,"POBLACION_LETRADO"));
				String idProvLetradoEjg = (String)sojHashtable.get("ID_PROVINCIA_LETRADO");
				if(idProvLetradoEjg!=null && !idProvLetradoEjg.trim().equalsIgnoreCase(""))
					helperInformes.completarHashSalida(sojHashtable,helperInformes.getNombreProvinciaSalida(idProvLetradoEjg,"PROVINCIA_LETRADO"));
				else
					UtilidadesHash.set(sojHashtable, "PROVINCIA_LETRADO", "");
				

			}else{
				UtilidadesHash.set(sojHashtable, "PROVINCIA_LETRADO", "");
				
			}
		}else{
			UtilidadesHash.set(sojHashtable, "NCOLEGIADO_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "NOMBRE_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "NIFCIF_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "SEXO_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "DOMICILIO_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "CP_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "POBLACION_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "PROVINCIA_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "TELEFONODESPACHO_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "EMAIL_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "FAX_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "TELEFONO1_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "TELEFONO2_LETRADO", "");
			UtilidadesHash.set(sojHashtable, "MOVIL_LETRADO", "");
		}
		
	}
	
	private void actualizarDatosPersonaJG(Hashtable sojHashtable) throws NumberFormatException, ClsExceptions{
		
		String idPersonaJG  = (String)sojHashtable.get("IDPERSONAJG");
		String idInstitucion  = UtilidadesHash.getString(sojHashtable,"IDINSTITUCION");
		if(idPersonaJG!=null && !idPersonaJG.trim().equalsIgnoreCase("")){
			ScsPersonaJGAdm personaJGAdm = new ScsPersonaJGAdm(this.usrbean);
			ScsPersonaJGBean personaJGBean = personaJGAdm.getPersonaJG(new Long(idPersonaJG), new Integer(idInstitucion));

			
			sojHashtable.put("NOMBRE_SOLICITANTE",personaJGBean.getNombre());
			sojHashtable.put("APELLIDO1_SOLICITANTE",personaJGBean.getApellido1());
			sojHashtable.put("APELLIDO2_SOLICITANTE",personaJGBean.getApellido2());
			sojHashtable.put("NIF_SOLICITANTE",personaJGBean.getNif());
			sojHashtable.put("DIRECCION_SOLICITANTE",personaJGBean.getDireccion());
			if(personaJGBean.getPoblacion()!=null && personaJGBean.getPoblacion().getNombre()!=null)
				sojHashtable.put("POBLACION_SOLICITANTE",personaJGBean.getPoblacion().getNombre());
			else
				sojHashtable.put("POBLACION_SOLICITANTE","");
				
			sojHashtable.put("CODIGOPOSTAL_SOLICITANTE",personaJGBean.getCodigoPostal());
			if(personaJGBean.getProvincia()!=null && personaJGBean.getProvincia().getNombre()!=null)
				sojHashtable.put("PROVINCIA_SOLICITANTE",personaJGBean.getProvincia().getNombre());
			else
				sojHashtable.put("PROVINCIA_SOLICITANTE","");
			sojHashtable.put("CORREOELECTRONICO_SOLICITANTE",personaJGBean.getCorreoElectronico());
			sojHashtable.put("FAX_SOLICITANTE",personaJGBean.getFax());
    		Vector<ScsTelefonosPersonaJGBean> vTelefonos = personaJGBean.getTelefonos();
    		if(vTelefonos!=null && vTelefonos.size()>0){
	    		for (int i = 0; i < vTelefonos.size(); i++) {
	    			ScsTelefonosPersonaJGBean telefono = (ScsTelefonosPersonaJGBean)vTelefonos.get(i);
	    			sojHashtable.put("TELEFONO_"+(i+1)+"_SOLICITANTE",telefono.getNumeroTelefono());
	    			sojHashtable.put("USO_"+(i+1)+"_SOLICITANTE",telefono.getNombreTelefono());
				}
    		}else{
    			sojHashtable.put("TELEFONO_1_SOLICITANTE","");
    			sojHashtable.put("USO_1_SOLICITANTE","");
    			
    		}

			
			
		}else{
			sojHashtable.put("NOMBRE_SOLICITANTE","");
			sojHashtable.put("APELLIDO1_SOLICITANTE","");
			sojHashtable.put("APELLIDO2_SOLICITANTE","");
			sojHashtable.put("NIF_SOLICITANTE","");
			sojHashtable.put("DIRECCION_SOLICITANTE","");
			sojHashtable.put("POBLACION_SOLICITANTE","");
			sojHashtable.put("CODIGOPOSTAL_SOLICITANTE","");
			sojHashtable.put("PROVINCIA_SOLICITANTE","");
			sojHashtable.put("CORREOELECTRONICO_SOLICITANTE","");
			sojHashtable.put("FAX_SOLICITANTE","");
			sojHashtable.put("TELEFONO_1_SOLICITANTE","");
			sojHashtable.put("USO_1_SOLICITANTE","");
			
			
		}
		
	}
	private void actualizarDatosDocumentacion(Hashtable sojHashtable) throws NumberFormatException, ClsExceptions{
		
		ScsDocumentacionSOJAdm documentacionSOJAdm = new ScsDocumentacionSOJAdm(this.usrbean);
		Vector documentacionSOJVector = documentacionSOJAdm.getDocumentosSOJ(sojHashtable);
		Vector newVector = new Vector();
		for (int i = 0; i < documentacionSOJVector.size(); i++) {
			ScsDocumentacionSOJBean docBean = (ScsDocumentacionSOJBean)documentacionSOJVector.get(i);
			Hashtable documentacionHashtable = documentacionSOJAdm.beanToHashTable(docBean);
			String fechaLimite = UtilidadesHash.getString(documentacionHashtable, ScsDocumentacionSOJBean.C_FECHALIMITE);
			UtilidadesHash.set(documentacionHashtable, ScsDocumentacionSOJBean.C_FECHALIMITE, GstDate.getFormatedDateShort("", fechaLimite));
			String fechaEntrega = UtilidadesHash.getString(documentacionHashtable, ScsDocumentacionSOJBean.C_FECHAENTREGA);
			UtilidadesHash.set(documentacionHashtable, ScsDocumentacionSOJBean.C_FECHAENTREGA, GstDate.getFormatedDateShort("", fechaEntrega));
			
			newVector.add(documentacionHashtable);			
		}
		
		sojHashtable.put("DOCUMENTACION_SOJ", newVector);
		
		
		
		
		
	}
	
}