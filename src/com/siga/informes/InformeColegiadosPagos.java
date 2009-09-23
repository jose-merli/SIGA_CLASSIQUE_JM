
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes;

import java.io.File;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvProgramPagosBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.general.SIGAException;
import com.siga.informes.form.MantenimientoInformesForm;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Pagos a Colegiados.
 */
public class InformeColegiadosPagos extends MasterReport {
	
	protected String formatoImportes="999,999,999,999,990.00";
	
	/**
	 * Metodo que implementa el modo generarPago
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String generarColegiadoPago (ActionMapping mapping,ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {

		String resultado="exito";
		 
		MantenimientoInformesForm miform = (MantenimientoInformesForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		
		String idioma=miform.getIdioma();

		// RGG 26/02/2007 cambio en los codigos de lenguajes
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		String idiomaExt = "es";
		try {
			idiomaExt = a.getLenguajeExt(idioma).toUpperCase();
		} catch (Exception e) {
			
		}
		
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
			
		try {
			//obtener plantilla
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);
			CenColegiadoAdm admColegiado=new CenColegiadoAdm(usr);
			String rutaPlantilla = 
				admParametros.getValor(institucion, "INF", "PATH_INFORMES_PLANTILLA", "")+
				ClsConstants.FILE_SEP+institucion;

		    
		    String nombrePlantilla=ClsConstants.PLANTILLA_FO_COLEGIADOPAGO+"_"+idiomaExt+".fo";
		    
		    String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
			
		    //obtener la ruta de descarga
			String rutaServidor = 
				admParametros.getValor(institucion, "INF", "PATH_INFORMES_DESCARGA", "")+
				ClsConstants.FILE_SEP+institucion;
			rutaFin=new File(rutaServidor);
			if (!rutaFin.exists()){
				if(!rutaFin.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}    
			String rutaServidorTmp=rutaServidor+ClsConstants.FILE_SEP+"tmp_ColegiadoPago_"+System.currentTimeMillis();
			rutaTmp=new File(rutaServidorTmp);
			if(!rutaTmp.mkdirs()){
				throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
			}
			
		    //obtener los datos comunes
		    Hashtable datosComunes= this.obtenerDatosComunes(usr,idiomaExt);
			
			// TRATAMIENTO POR CADA COLEGIADO
			/////////////////////////////////
			String idPago = miform.getIdPago();
			datosComunes.put("IDPAGO",idPago);
			Vector vColegiados = null;
			if(miform.getIdPersona()!=null && !miform.getIdPersona().trim().equals("")){
				vColegiados = new Vector();
				Hashtable registro = new Hashtable(); 
				registro.put("IDPERSONA_SJCS", miform.getIdPersona());
					
				vColegiados.add(registro);
			}else{
				FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(usr);
				vColegiados = pagosAdm.getColegiadosAPagar(institucion,idPago);
				
			}
			
			File fPdf = null;
			if (vColegiados!=null && !vColegiados.isEmpty()){
				boolean correcto=true;
				Enumeration lista=vColegiados.elements();
	    		
				while(correcto && lista.hasMoreElements()){
					
					Hashtable datosBase=(Hashtable)lista.nextElement();
					String ncolegiado=admColegiado.getIdentificadorColegiado(admColegiado.getDatosColegiales((UtilidadesHash.getLong(datosBase,"IDPERSONA_SJCS")),new Integer(usr.getLocation())));
					if (ncolegiado==null){
						ncolegiado="";
					}
					datosBase.putAll(datosComunes);
					fPdf = this.generarInforme(usr,datosBase,rutaPlantilla,contenidoPlantilla,rutaPlantilla,ncolegiado+"-"+"colegiadoPago_" +numeroCarta);
					correcto=(fPdf!=null);
					if(correcto){
						ficherosPDF.add(fPdf);
						numeroCarta++;
					}
				}
				
				if(correcto){
					// Ubicacion de la carpeta donde se crean los ficheros PDF:
					String nombreFicheroPDF="colegiadoPago_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip=rutaServidor + ClsConstants.FILE_SEP;
					if(ficherosPDF.size()>1){
						Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroPDF,ficherosPDF);
						request.setAttribute("nombreFichero", nombreFicheroPDF + ".zip");
						request.setAttribute("rutaFichero", rutaServidorDescargasZip+nombreFicheroPDF + ".zip");			
						request.setAttribute("borrarFichero", "true");
					}else{
						
						request.setAttribute("nombreFichero", fPdf.getName());
						request.setAttribute("rutaFichero", fPdf.getPath());			
						
						
					}
					
					//resultado = "descargaFichero";				
					request.setAttribute("generacionOK","OK");			
					resultado = "descarga";
				}else{
					request.setAttribute("generacionOK","ERROR");			
					resultado = "descarga";
				}
				
			}else{
				resultado = "error";//exitoModalSinRefresco("gratuita.retenciones.noResultados",request);
			}
			
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		} finally{
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return resultado;
	}
	public String getPathInformePagoColegiadoAEnviar (UsrBean usr,	String idPago,String idPersona,
			String idioma,String idInstitucion) throws ClsExceptions {

		String resultado="exito";
		 
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		String idiomaExt = "es";
		try {
			idiomaExt = a.getLenguajeExt(idioma).toUpperCase();
		} catch (Exception e) {
			
		}
		
		
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
		String pathFicheroGenerado = null;
			
		try {
			//obtener plantilla
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);
			CenColegiadoAdm admColegiado=new CenColegiadoAdm(usr);
			String rutaPlantilla = 
				admParametros.getValor(idInstitucion, "INF", "PATH_INFORMES_PLANTILLA", "")+
				ClsConstants.FILE_SEP+idInstitucion;

		    
		    String nombrePlantilla=ClsConstants.PLANTILLA_FO_COLEGIADOPAGO+"_"+idiomaExt+".fo";
		    
		    String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
			
		    //obtener la ruta de descarga
			String rutaServidor = 
				admParametros.getValor(idInstitucion, "INF", "PATH_INFORMES_DESCARGA", "")+
				ClsConstants.FILE_SEP+idInstitucion;
			rutaFin=new File(rutaServidor);
			if (!rutaFin.exists()){
				if(!rutaFin.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}    
			String rutaServidorTmp=rutaServidor+ClsConstants.FILE_SEP+"tmp_ColegiadoPago_"+System.currentTimeMillis();
			rutaTmp=new File(rutaServidorTmp);
			if(!rutaTmp.mkdirs()){
				throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
			}
			
		    //obtener los datos comunes
		    Hashtable datosComunes= this.obtenerDatosComunes(usr,idiomaExt);
			
			// TRATAMIENTO POR CADA COLEGIADO
			/////////////////////////////////
			
			datosComunes.put("IDPAGO",idPago);
			Vector vColegiados = null;
			if(idPersona!=null && !idPersona.trim().equals("")){
				vColegiados = new Vector();
				Hashtable registro = new Hashtable(); 
				registro.put("IDPERSONA_SJCS", idPersona);
					
				vColegiados.add(registro);
			}else{
				FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(usr);
				vColegiados = pagosAdm.getColegiadosAPagar(idInstitucion,idPago);
				
			}
			
			File fPdf = null;
			if (vColegiados!=null && !vColegiados.isEmpty()){
				boolean correcto=true;
				Enumeration lista=vColegiados.elements();
	    		
				while(correcto && lista.hasMoreElements()){
					
					Hashtable datosBase=(Hashtable)lista.nextElement();
					String ncolegiado=admColegiado.getIdentificadorColegiado(admColegiado.getDatosColegiales((UtilidadesHash.getLong(datosBase,"IDPERSONA_SJCS")),new Integer(usr.getLocation())));
					if (ncolegiado==null){
						ncolegiado="";
					}
					datosBase.putAll(datosComunes);
					fPdf = this.generarInforme(usr,datosBase,rutaPlantilla,contenidoPlantilla,rutaPlantilla,ncolegiado+"-"+"colegiadoPago_" +numeroCarta);
					correcto=(fPdf!=null);
					if(correcto){
						ficherosPDF.add(fPdf);
						numeroCarta++;
					}
				}
				
				if(correcto){
					// Ubicacion de la carpeta donde se crean los ficheros PDF:
					String nombreFicheroPDF="colegiadoPago_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip=rutaServidor + ClsConstants.FILE_SEP;
					if(ficherosPDF.size()>1){
						Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroPDF,ficherosPDF);
						pathFicheroGenerado = rutaServidorDescargasZip+nombreFicheroPDF + ".zip";

					}else{

						pathFicheroGenerado = fPdf.getPath();
									
						
						
					}
					
					
				}else{
					throw new ClsExceptions("Error al generar el informe");
				}
				
			}
			
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		} finally{
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return pathFicheroGenerado;
	}
	
	
	/**
	 * Este método busca los valores comunes de los informes
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected Hashtable obtenerDatosComunes(UsrBean usr, String lenguaje) throws ClsExceptions{
		
		String institucion =usr.getLocation();
		//String idioma = usr.getLanguageExt().toUpperCase();
				
		Hashtable datos= new Hashtable();
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);
	    String rutaPlantilla =
	    	//"\\\\SG00582\\datos\\plantillas\\informes_fact_sjcs"+
	    	admParametros.getValor(institucion, "INF", "PATH_INFORMES_PLANTILLA", "")+
	    	ClsConstants.FILE_SEP+institucion;
		UtilidadesHash.set(datos,"RUTA_LOGO",rutaPlantilla+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"Logo.jpg");
		UtilidadesHash.set(datos,"FECHA",UtilidadesBDAdm.getFechaEscritaBD(lenguaje));
		return datos;
	 }
	 
	 /**
	  * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	  * @param request Objeto HTTPRequest
	  * @param plantillaFO Plantilla FO con parametros 
	  * @return Plantilla FO en donde se han reemplazado los parámetros
	  * @throws ClsExceptions
	  */
	protected String reemplazarDatos(UsrBean usr, String plantillaFO, Hashtable datosBase) throws ClsExceptions, SIGAException{
		Hashtable htDatos=(Hashtable)datosBase.clone();
		
		String idioma = usr.getLanguage().toUpperCase();
		String institucion =usr.getLocation();
		String idPagos =(String)datosBase.get("IDPAGO");
		Hashtable htAux=null;
		
		//firmas
		htDatos.put("SECRETARIO","XXXXXXX XXXXXXXXXXXXXXX XXXXXXXXXX");
		htDatos.put("FIRMA","xxxxxxxxx xxxxxxxxx xxxxxxxx");
		htDatos.put("NDUPLICADO","XXXXXXXX");
		
		//datos colegio
		CenInstitucionAdm institAdm= new CenInstitucionAdm(usr);
		String nombreInstit=institAdm.getNombreInstitucion(institucion);
		if(nombreInstit!=null)htDatos.put("NOMBRE_INSTITUCION",nombreInstit);
		
		//datos cabecera
		String idPersona=(String) htDatos.get("IDPERSONA_SJCS");
		
		//Datos Cabecera
		htAux=this.obtenerDatosPersonaSociedad(institucion,idPersona,usr);
		
		String cuenta=(String)htAux.get("CUENTA_CORRIENTE");
		if (cuenta==null || cuenta.equals("")) {
			String delimIni=CTR+"INI_TODO_CUENTA"+CTR;
			String delimFin=CTR+"FIN_TODO_CUENTA"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}else{
			// JBD 03/02/2009 Si hemos obtenido una cuenta debemos ocultar el numero con asteriscos (INC-5635)
			// Nos aseguramos de que el numero de cuenta este completo
			if(cuenta.length()>=23){
				// Como viene concatenada la cuenta recuperamos solo el numero de cuenta.
				String numero = cuenta.substring(13, 23);
				// Le ocultamos parte con asteriscos
				numero = UtilidadesString.mostrarNumeroCuentaConAsteriscos(numero);
				// Volvemos a unir la cuenta
				cuenta = cuenta.substring(0, 13) + numero + cuenta.substring(23);
				htAux.put("CUENTA_CORRIENTE", cuenta);
			}
		}
		htDatos.putAll(htAux);
		
		//Datos de las Asistencias
		Vector datosAsistencias=this.obtenerDatosAsistencia(institucion, idPersona, idPagos, idioma);
		plantillaFO = this.reemplazaRegistros(plantillaFO,datosAsistencias,htDatos,"ASISTENCIA");
		
		//Datos de los Oficios
		Vector datosOficios=this.obtenerDatosOficio(institucion,idPersona, idPagos);
		plantillaFO = this.reemplazaRegistros(plantillaFO,datosOficios,htDatos,"OFICIOS");
		
		//Datos del Pago y Totales
		htAux=this.obtenerDatosPago(institucion, idPersona, idPagos);
		String total2=(htAux.get("TOTAL_MOVIMIENTOS")!=null)?(String)htAux.get("TOTAL_MOVIMIENTOS"):"0";
		if (total2.length()>2) total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_MOVIMIENTOS"+CTR;
			String delimFin=CTR+"FIN_TODO_MOVIMIENTOS"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
		total2=(htAux.get("TOTAL_RETENCIONES")!=null)?(String)htAux.get("TOTAL_RETENCIONES"):"0";
		if (total2.length()>2) total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_RETENCIONES"+CTR;
			String delimFin=CTR+"FIN_TODO_RETENCIONES"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
		htDatos.putAll(htAux);
		
		return this.reemplazaVariables(htDatos,plantillaFO);
	} //reemplazarDatos()
	
	/**
	 * Obtienes nombre y direccion del letrado o Sociedad
	 * @param idInstitucion Identificador de la institucion
	 * @param idPersona Identificador del letrado o Sociedad
	 * @return
	 * @throws SIGAException
	 */	
	protected Hashtable obtenerDatosPersonaSociedad(String idInstitucion, String idPersona, UsrBean user) throws ClsExceptions {
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		String idSociedad=null;
		//UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try {
			 //buscamos el nombre de la persona
			String sql=
				 "select "+UtilidadesMultidioma.getCampoMultidiomaSimple("T.DESCRIPCION",user.getLanguage())+"||' '||P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_PERSONA" +
				 "  from CEN_PERSONA P, CEN_CLIENTE C, CEN_TRATAMIENTO T " +
				 " where C.IDPERSONA = P.IDPERSONA  " +
				 "   and C.IDTRATAMIENTO = T.IDTRATAMIENTO " +
				 "   and C.IDPERSONA = " +idPersona+
				 "   and C.IDINSTITUCION ="+idInstitucion;
			 rc = new RowsContainer();
			 rc.find(sql);
			 if(rc!=null && rc.size()>0){
				 Row r=(Row)rc.get(0);
				 result.putAll(r.getRow());
			 }
			 
			 //compruebo si actua en nombre de una sociedad
			 sql=
				 "select C.IDPERSONA, C.SOCIEDAD " +
				 "  from CEN_PERSONA P, CEN_COMPONENTES C " +
				 " where C.IDPERSONA = P.IDPERSONA " +
				 "   and C.CEN_CLIENTE_IDPERSONA = " +idPersona+
				 "   and C.CEN_CLIENTE_IDINSTITUCION = "+idInstitucion;
			 rc = new RowsContainer(); 
			 rc.find(sql);
			 if(rc!=null && rc.size()>0){
				 Row r=(Row)rc.get(0);
				 String sSociedad= r.getString("SOCIEDAD");
				 if(sSociedad!=null && sSociedad.equals(ClsConstants.DB_TRUE)){
					 idSociedad=r.getString("IDPERSONA");
				 }
			 }

			 
			 if(idSociedad==null){
				 //Si no existe sociedad sacamos los datos relativos a la persona
				 idSociedad=idPersona;
				 
			 }else{
				 //nombre de la sociedad
				 sql=
					 "select P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_SOCIEDAD" +
					 "  from CEN_PERSONA P, CEN_CLIENTE C" +
					 " where C.IDPERSONA = P.IDPERSONA  " +
					 "   and C.IDPERSONA = " +idSociedad+
					 "   and C.IDINSTITUCION ="+idInstitucion;
				 rc = new RowsContainer();
				 rc.find(sql);
				 if(rc!=null && rc.size()>0){
					 Row r=(Row)rc.get(0);
					 result.putAll(r.getRow());
				 }
			 }
			
			
			//Direccion de la sociedad o persona
		    /*sql=
		    	"select D.DOMICILIO, D.CODIGOPOSTAL," +
		    	"(select p.nombre from cen_poblaciones p where p.idpoblacion=d.idpoblacion) POBLACION," +
		    	"(select p.nombre from cen_provincias p where p.idprovincia=d.idprovincia) PROVINCIA" +
		    	"  from CEN_DIRECCIONES D, CEN_DIRECCION_TIPODIRECCION DTD" +
		    	" where D.IDDIRECCION = DTD.IDDIRECCION  " +
		    	"   and D.IDINSTITUCION = DTD.IDINSTITUCION   " +
		    	"   and D.IDPERSONA = DTD.IDPERSONA    " +
		    	//"   and D.PREFERENTE LIKE '%C%'   " +
		    	"   and DTD.IDTIPODIRECCION = 2   " +
				"   and d.fechabaja is null"+
		    	"   and D.IDPERSONA = " +idSociedad;
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}*/
			
			 String direccion="";
				 direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"C");// buscamos una direccion preferente correo
				 rc = new RowsContainer();
					rc.find(direccion);
					if(rc!=null && rc.size()>0){
						Row r=(Row)rc.get(0);
						result.putAll(r.getRow());
					}
				 
				 if (rc==null || rc.size()==0 ) {
				 	direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"2");// si no hay direccion preferente Correo, buscamos cualquier direccion de despacho.
				 	 rc = new RowsContainer();
						rc.find(direccion);
						if(rc!=null && rc.size()>0){
							Row r=(Row)rc.get(0);
							result.putAll(r.getRow());
						}
				 	if (rc==null || rc.size()==0 ) {
				 		direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"3");// si no hay direccion de despacho, buscamos cualquier direccion de correo.
				 		 rc = new RowsContainer();
							rc.find(direccion);
							if(rc!=null && rc.size()>0){
								Row r=(Row)rc.get(0);
								result.putAll(r.getRow());
							}
				 		if (rc ==null || rc.size()==0 ){
				 			direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"");// si no hay direccion de correo, buscamos cualquier direccion.
				 			 rc = new RowsContainer();
								rc.find(direccion);
								if(rc!=null && rc.size()>0){
									Row r=(Row)rc.get(0);
									result.putAll(r.getRow());
								}
				 		   
				 		}  
				 	}
				 }
				
				 

			// Datos Bancarios de la sociedad o persona
		    sql=
		    	"SELECT DECODE(CUEN.NUMEROCUENTA,NULL,'',CUEN.CBO_CODIGO||' '||CUEN.CODIGOSUCURSAL||' '||CUEN.DIGITOCONTROL||' '||CUEN.NUMEROCUENTA||' '||BAN.NOMBRE) CUENTA_CORRIENTE" +
		    	"  FROM CEN_CUENTASBANCARIAS CUEN, CEN_BANCOS BAN" +
		    	" WHERE BAN.CODIGO = CUEN.CBO_CODIGO " +
		    	"   AND CUEN.FECHABAJA IS NULL " +
		    	"   AND CUEN.ABONOSJCS = 1" +
		    	"   AND CUEN.IDINSTITUCION = "+idInstitucion+
		    	"   AND CUEN.IDPERSONA = " +idSociedad;
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}

		return result;
	}
	
	
	/**
	 * Obtiene el listado de datos del pago de las asistencias 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idioma
	 * @return
	 * @throws SIGAException
	 */	
	protected Vector obtenerDatosAsistencia(String idInstitucion, String idPersona, String idPagos, String idioma) throws ClsExceptions {
		Vector result= new Vector();
		String sinActuaciones=//"Guardia sense actuacions";
			UtilidadesString.getMensajeIdioma(idioma,"informes.colegiadosPagos.guardiaSinActuaciones");
		int contador=0;
		Hashtable codigo =new Hashtable();
		double importeAsistenciaAux=0;
		
		try {
			String sql1=
				"select to_char(AP.FECHAINICIO,'DD/MM/YYYY') FECHA, TU.NOMBRE TURNO," +
				"       f_siga_formatonumero(AP.IMPORTEPAGADO,2) IMPORTEPAGADO, " +
				"		AP.IDFACTURACION, AP.IDTURNO, AP.IDGUARDIA, AP.IDCALENDARIOGUARDIAS, " +
				"       f_siga_formatonumero(fa.precioaplicado,2) IMPORTE_ACTUACION, AP.IDAPUNTE "+
				"  from FCS_PAGO_APUNTE AP, SCS_TURNO TU, fcs_fact_apunte fa " +
				" where AP.IDINSTITUCION = TU.IDINSTITUCION" +
				"   and AP.IDTURNO = TU.IDTURNO" ;
			contador++;
			codigo.put(new Integer(contador),idInstitucion);
			sql1+= "   and AP.IDINSTITUCION = :"+contador;
			contador++;
			codigo.put(new Integer(contador),idPersona);
			sql1+= 	"   and AP.IDPERSONA = :" +contador;
			contador++;
			codigo.put(new Integer(contador),idPagos);	
			sql1+= 	"   and AP.IDPAGOSJG = :" +contador+
				"   and AP.IDINSTITUCION = fa.IDINSTITUCION"+
				"   and AP.Idfacturacion = fa.idfacturacion"+
				"   and AP.Idapunte = fa.idapunte"+
				" order by AP.FECHAINICIO, TU.NOMBRE, AP.IDAPUNTE";
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql1,codigo);
			if(rc!=null && rc.size()>0){
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc.get(i);
					Hashtable htAux=r1.getRow();
					String sImportePagado=r1.getString("IMPORTEPAGADO");
					htAux.put("IMPORTEPAGADO",sImportePagado+ClsConstants.CODIGO_EURO);
					String sImporteFacturado=r1.getString("IMPORTE_ACTUACION");
					htAux.put("IMPORTE_ACTUACION",sImporteFacturado+ClsConstants.CODIGO_EURO);
					
					String idTurno=r1.getString("IDTURNO");
					String idGuardia=r1.getString("IDGUARDIA");
					String idCalendarioGuardias=r1.getString("IDCALENDARIOGUARDIAS");
					String idFacturacion=r1.getString("IDFACTURACION");
					String fecha=r1.getString("FECHA");
					String idApunte=r1.getString("IDAPUNTE");
					
					String sql2=
						"select AAS.ANIO||'/'||AAS.NUMERO ACTUACION," +//+ (aas.descripcionbreve o aas.idactuacion
						"       PJG.NOMBRE||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2 NOMBRE_ASISTIDO, to_char(AAS.FECHA,'DD/MM/YYYY') FECHA_ACTUACION" +
						"  from FCS_FACT_APUNTE FAP, FCS_FACT_ACTUACIONASISTENCIA FAAS," +
						"		SCS_ACTUACIONASISTENCIA AAS," +
						"		SCS_ASISTENCIA ASI, SCS_PERSONAJG PJG" +
						" where FAP.IDINSTITUCION = FAAS.IDINSTITUCION " +
						"   and FAP.IDFACTURACION = FAAS.IDFACTURACION " +
						"   and FAP.IDAPUNTE = FAAS.IDAPUNTE " +
						"   and FAP.IDPERSONA = FAAS.IDPERSONA " +
						"   and FAP.IDINSTITUCION = "+idInstitucion +
						"   and FAP.IDFACTURACION = "+idFacturacion +
						"   and FAP.IDTURNO ="+idTurno +
						"   and FAP.IDGUARDIA = "+idGuardia +
						"   and FAP.IDCALENDARIOGUARDIAS = "+idCalendarioGuardias +
						"   and to_char(FAP.FECHAINICIO,'DD/MM/YYYY') = '"+fecha+"'"+
						"   and FAP.Idpersona = "+idPersona+" "+
						"   and FAP.IdApunte = "+idApunte+" "+
						"   and FAAS.IDINSTITUCION = AAS.IDINSTITUCION (+)" +
						"   and FAAS.NUMERO = AAS.NUMERO (+)" +
						"   and FAAS.ANIO = AAS.ANIO (+)" +
						"   and FAAS.IDACTUACION=AAS.IDACTUACION (+)" +
						"   and FAAS.IDINSTITUCION = ASI.IDINSTITUCION (+)" +
						"   and FAAS.NUMERO = ASI.NUMERO (+)" +
						"   and FAAS.ANIO = ASI.ANIO (+)" +
						"   and ASI.IDPERSONAJG = PJG.IDPERSONA(+)" +
						"   and ASI.IDINSTITUCION = PJG.IDINSTITUCION(+)" +
						" order by ACTUACION, NOMBRE_ASISTIDO";

					RowsContainer rc2 = new RowsContainer();
					rc2.find(sql2);
					if(rc2!=null && rc2.size()>0){
						//tratar el primero
						Row r2=(Row)rc2.get(0);
						htAux.putAll(r2.getRow());
						result.addElement(htAux);
						//tratar el resto
						int size2=rc2.size();
						for(int j=1;j<size2;j++){
							r2=(Row)rc2.get(j);
							htAux=r2.getRow();
							result.addElement(htAux);
						}
					}else{
						htAux.put("NOMBRE_ASISTIDO",sinActuaciones);
						result.addElement(htAux);
					}
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		return result;
	}
	

	
	/**
	 * Obtiene el listado de datos del pago de los oficios 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idPagos
	 * @return
	 * @throws SIGAException
	 */
	protected Vector obtenerDatosOficio(String idInstitucion, String idPersona, String idPagos) throws ClsExceptions {
		Vector result= null;
		int contador=0;
		Hashtable codigo=new Hashtable();
		
		try {
			String sql1=
				"select distinct AD.FECHA, to_char(AD.FECHA,'DD/MM/YYYY') FECHA_OFICIO,  PRO.NOMBRE PROCEDIMIENTO," +
				"       f_siga_formatonumero(PAD.IMPORTEPAGADO,2)  IMPORTEPAGADO, " +
//				"       PAD.ANIO || '/' || DES.CODIGO ||'/'||PAD.NUMERO ASIOFI, " +
				"       PAD.ANIO || '/' || DES.CODIGO  ASIOFI, " +
//				"       PJG.NOMBRE||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2 NOMBRE_SOLICITANTE " +
				"       f_siga_getdefendidosdesigna(DES.IDINSTITUCION, des.anio, des.idturno, des.numero,0) NOMBRE_SOLICITANTE, " +
				"       f_siga_formatonumero(fact.precioaplicado,2) IMPORTE_PROCEDIMIENTO, " +
				"       f_siga_formatonumero(fact.precioaplicado*fact.porcentajefacturado/100,2) IMPORTE_OFICIO " +
				"  from FCS_PAGO_ACTUACIONDESIGNA PAD, SCS_ACTUACIONDESIGNA AD, " +
				"       SCS_PROCEDIMIENTOS PRO, SCS_DESIGNA DES,  fcs_fact_actuaciondesigna fact " +
				" where DES.IDINSTITUCION=AD.IDINSTITUCION " +
				" AND DES.IDTURNO=AD.IDTURNO " +
				" AND DES.ANIO = AD.ANIO " +
				" AND DES.NUMERO=AD.NUMERO " +
				" AND PAD.IDINSTITUCION = AD.IDINSTITUCION" +
				"   and PAD.IDTURNO = AD.IDTURNO" +
				"   and PAD.ANIO = AD.ANIO" +
				"   and PAD.NUMERO = AD.NUMERO" +
				"   and PAD.NUMEROASUNTO=AD.NUMEROASUNTO" +
				"   and AD.IDINSTITUCION = PRO.IDINSTITUCION" +
				"   and AD.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO" ;
			
//				"   and AD.IDINSTITUCION = DD.IDINSTITUCION(+)" +
//				"   and AD.IDTURNO = DD.IDTURNO(+)" +
//				"   and AD.ANIO= DD.ANIO(+)" +
//				"   and AD.NUMERO = DD.NUMERO(+)" +
//				"   and DD.IDINSTITUCION = PJG.IDINSTITUCION(+)" +
//				"   and DD.IDPERSONA = PJG.IDPERSONA(+)" +
			contador++;
			codigo.put(new Integer(contador),idInstitucion);
			
			
			sql1+="   and PAD.IDINSTITUCION= :"+contador;
			contador++;
			codigo.put(new Integer(contador),idPersona);
			sql1+="   and PAD.IDPERSONA=:" +contador;
			contador++;
			codigo.put(new Integer(contador),idPagos);
			sql1+=	"   and PAD.IDPAGOSJG= :"+contador+
				"   and pad.idfacturacion=fact.idfacturacion"+
				"   and pad.idturno=fact.idturno"+
				"   and pad.anio=fact.anio"+
				"   and pad.numero=fact.numero"+
				"   and pad.numeroasunto=fact.numeroasunto"+
				"   and pad.idinstitucion=fact.idinstitucion"+
				" order by AD.FECHA";
			
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql1,codigo);
			result=new Vector();
			double importeOficioAux=0;
			if(rc!=null && rc.size()>0){
				Vector result3=rc.getAll();
				Vector aux=new Vector();
				for (int g=0;result3!=null && g<result3.size();g++){
				    aux.add(g,(Row) result3.get(g));
				}
				for (int g=0;aux!=null && g<aux.size();g++){
				    Hashtable ht = ((Row) aux.get(g)).getRow();
				    ht.put("IMPORTEPAGADO", ((String)ht.get("IMPORTEPAGADO"))+ClsConstants.CODIGO_EURO);
				    ht.put("IMPORTE_OFICIO", ((String)ht.get("IMPORTE_OFICIO"))+ClsConstants.CODIGO_EURO);
				    result.add(g,ht);
				     
				}
			}
			

		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		
		return result;
	}
	
	
	/**
	 * Calcula la cantidad equivalente al cien por cien, dada una cantidad y el porcentaje al que corresponde
	 * @param porcentaje
	 * @param cantidad
	 * @return
	 */
	protected double obtenerCienPorCien(String porcentaje, double cantidad) {
		double result= 0;
		try {
			int iPorcentaje=Integer.parseInt(porcentaje);
			if(iPorcentaje!=0 && cantidad!=0){
				result=(cantidad*100)/iPorcentaje;
			}
		} catch (Exception e) {
			//me lo trago, devuelvo 0
		}

		return result;
	}
	
	/**
	 * Obtiene el importe total bruto, el IRPF y el importe total neto 
	 * @param totalGeneral
	 * @return
	 * @throws SIGAException
	 */
	protected Hashtable obtenerDatosPago(String idInstitucion, String idPersona, String idPago) throws ClsExceptions {
		RowsContainer rc=null;
		Hashtable result= new Hashtable();
		double dTotalAsistencia=0;
		double dTotalFactAsistencia=0;
		double dTotalOficio=0;
		double dTotalFactOficio=0;
		String pcAsistencia=null;
		String pcOficio=null;
		
		int IRPF = 0;
		
		try {
			// Porcentajes DEL PAGO
			StringBuffer buf0 = new StringBuffer();
			buf0.append("select PA.IMPORTEPAGADO, PA.NOMBRE NOMBRE_PAGO, ");
			buf0.append("       PA.PORCENTAJEOFICIO PORCENTAJE_TURNOS, ");
			buf0.append("       PA.PORCENTAJEGUARDIAS PORCENTAJE_ASISTENCIA");
			buf0.append("  from FCS_PAGOSJG PA");
			buf0.append(" where pa.idinstitucion = "+idInstitucion);
			buf0.append("   and pa.idpagosjg = " +idPago);

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result=r.getRow();
				pcAsistencia=(String)r.getString("PORCENTAJE_ASISTENCIA");
				pcOficio=(String)r.getString("PORCENTAJE_TURNOS");
			}

			
			
			
			//1. Calculo los importes:
			//TOTAL_ASISTENCIA,TOTAL_OFICIO,TOTAL_GENERAL,TOTAL_IRPF y TOTAL_LIQUIDACION
			/*StringBuffer buf1 = new StringBuffer();
			buf1.append("select A.importe_pagado TOTAL_ASISTENCIA, ");
			buf1.append(" A.importe_facturado TOTAL_FACTURADO, ");
			buf1.append("   O.importe_pagado TOTAL_OFICIO, ");
			buf1.append("   O.importe_FACTURADO TOTAL_FACTURADO_OFICIO, ");
			buf1.append("M.importe_pagado TOTAL_MOVIMIENTOS, ");
			buf1.append("		       R.importe_pagado TOTAL_RETENCIONES, ");
			buf1.append("f_siga_formatonumero(A.importe_pagado + O.importe_pagado,2) ");
			buf1.append(" TOTAL_GENERAL, ");
			buf1.append("f_siga_formatonumero(A.importe_irpf + O.importe_irpf + M.importe_irpf, 2) TOTAL_IRPF, ");
			buf1.append("f_siga_formatonumero(A.importe_TOTAL + O.importe_TOTAL + M.importe_TOTAL + R.importe_pagado,2) TOTAL_LIQUIDACION ");
			buf1.append("		  from (select nvl(sum(importepagado), 0) importe_pagado, ");
			buf1.append("nvl(sum(importeIrpf), 0) importe_irpf, ");
			buf1.append("nvl(sum(importepagado - importeIrpf), 0) importe_total, ");
			buf1.append("nvl(sum(fcs_fact_apunte.Precioaplicado), 0) importe_facturado ");
			buf1.append("		          from fcs_pago_apunte ,fcs_fact_apunte");
			buf1.append("		         where fcs_pago_apunte.idpagosjg = "+idPago+" ");
			buf1.append("and fcs_pago_apunte.idpersona = "+idPersona+" ");
			buf1.append("AND fcs_pago_apunte.idinstitucion=fcs_fact_apunte.IDINSTITUCION ");
			buf1.append("AND fcs_pago_apunte.idfacturacion=fcs_fact_apunte.idfacturacion ");
			buf1.append("and fcs_pago_apunte.idapunte=fcs_fact_apunte.idapunte ");
			buf1.append("and fcs_pago_apunte.idInstitucion = "+idInstitucion+") A, ");
			buf1.append("(select nvl(sum(importepagado), 0) importe_pagado, ");
			buf1.append("nvl(sum(importeIrpf), 0) importe_irpf, ");
			buf1.append("nvl(sum(importepagado - importeIrpf), 0) importe_total, ");
			buf1.append("nvl(sum(fcs_fact_actuaciondesigna.precioaplicado), 0) importe_facturado ");
			buf1.append("from fcs_pago_actuaciondesigna , fcs_fact_actuaciondesigna ");
			buf1.append("where fcs_pago_actuaciondesigna.idpagosjg = "+idPago+" ");
			buf1.append("and fcs_pago_actuaciondesigna.idpersona = "+idPersona+" ");
			buf1.append("AND fcs_pago_actuaciondesigna.idinstitucion=fcs_fact_actuaciondesigna.IDINSTITUCION ");
			buf1.append("AND fcs_pago_actuaciondesigna.idfacturacion=fcs_fact_actuaciondesigna.idfacturacion ");
			buf1.append("and fcs_pago_actuaciondesigna.idturno=fcs_fact_actuaciondesigna.idturno ");
			buf1.append("and fcs_pago_actuaciondesigna.anio=fcs_fact_actuaciondesigna.anio ");
			buf1.append("and fcs_pago_actuaciondesigna.numero=fcs_fact_actuaciondesigna.numero ");
			buf1.append(" and fcs_pago_actuaciondesigna.numeroasunto=fcs_fact_actuaciondesigna.numeroasunto ");
			buf1.append("	           and fcs_pago_actuaciondesigna.idInstitucion = "+idInstitucion+") O, ");
			buf1.append("		       (select nvl(sum(cantidad), 0) importe_pagado, ");
			buf1.append("		               nvl(sum(importeIrpf), 0) importe_irpf, ");
			buf1.append("		               nvl(sum(cantidad - importeIrpf), 0) importe_total ");
			buf1.append("		          from fcs_movimientosvarios ");
			buf1.append("		         where idpagosjg = "+idPago+" ");
			buf1.append("		           and idpersona = "+idPersona+" ");
			buf1.append("		           and idInstitucion = "+idInstitucion+") M, ");
			buf1.append("		       (select nvl(sum(importeretenido), 0) importe_pagado, ");
			buf1.append("		               0 importe_irpf, ");
			buf1.append("		               nvl(sum(importeretenido), 0)  importe_total ");
			buf1.append("		          from fcs_cobros_retencionjudicial ");
			buf1.append("		         where idpagosjg = "+idPago+" ");
			buf1.append("		           and idpersona = "+idPersona+" ");
			buf1.append("		           and idInstitucion = "+idInstitucion+") R ");*/
			StringBuffer buf1 = new StringBuffer();
			buf1.append("SELECT IMPORTETOTALMOVIMIENTOS AS TOTAL_MOVIMIENTOS,                                   ");
			buf1.append("       IMPORTETOTALRETENCIONES AS TOTAL_RETENCIONES,                                   ");
			buf1.append("       TOTALIMPORTESJCS AS TOTAL_GENERAL,                                              ");
			buf1.append("       (-1 * TOTALIMPORTEIRPF) AS TOTAL_IRPF,                       ");
			//buf1.append("       TOTALIMPORTESJCS + IMPORTETOTALMOVIMIENTOS - IMPORTETOTALRETENCIONES -          ");
			//buf1.append("       TOTALIMPORTEIRPF AS TOTAL_LIQUIDACION,                                          ");
			buf1.append("       TOTALIMPORTESJCS + IMPORTETOTALMOVIMIENTOS + IMPORTETOTALRETENCIONES +          ");
			buf1.append("       (-1 * TOTALIMPORTEIRPF) AS TOTAL_LIQUIDACION,                                   ");
			buf1.append("       A.IMPORTE_PAGADO AS TOTAL_ASISTENCIA,                                           ");
			buf1.append("       A.IMPORTE_FACTURADO AS TOTAL_FACTURADO,                                         ");
			buf1.append("       O.IMPORTE_PAGADO AS TOTAL_OFICIO,                                               ");
			buf1.append("       O.IMPORTE_FACTURADO AS TOTAL_FACTURADO_OFICIO                                   ");
			buf1.append("  FROM (SELECT IDPERSONASJCS,                                                          ");
			buf1.append("               SUM(TOTALIMPORTESJCS) AS TOTALIMPORTESJCS,                              ");
			buf1.append("               SUM(IMPORTETOTALRETENCIONES) AS IMPORTETOTALRETENCIONES,                ");
			buf1.append("               CASE                                                                    ");
			buf1.append("                 WHEN (SUM(IMPORTETOTALMOVIMIENTOS) < 0 AND                            ");
			buf1.append("                      ABS(SUM(IMPORTETOTALMOVIMIENTOS)) >                              ");
			buf1.append("                      SUM(TOTALIMPORTESJCS)) THEN                                      ");
			buf1.append("                  -1 * SUM(TOTALIMPORTESJCS)                                           ");
			buf1.append("                 ELSE                                                                  ");
			buf1.append("                  SUM(IMPORTETOTALMOVIMIENTOS)                                         ");
			buf1.append("               END AS IMPORTETOTALMOVIMIENTOS,                                         ");
			
			/*buf1.append("               (((SUM(TOTALIMPORTESJCS) + CASE                                         ");
			buf1.append("                 WHEN (SUM(IMPORTETOTALMOVIMIENTOS) < 0 AND                            ");
			buf1.append("                      ABS(SUM(IMPORTETOTALMOVIMIENTOS)) >                              ");
			buf1.append("                      SUM(TOTALIMPORTESJCS)) THEN                                      ");
			buf1.append("                  -1 * SUM(TOTALIMPORTESJCS)                                           ");
			buf1.append("                 ELSE                                                                  ");
			buf1.append("                  SUM(IMPORTETOTALMOVIMIENTOS)                                         ");
			buf1.append("               END) * MAX(PORCERTAJEIRPF)) / 100) AS TOTALIMPORTEIRPF                  ");*/
			
			buf1.append("               CASE ");
			buf1.append("                 WHEN (SUM(IMPORTETOTALMOVIMIENTOS) < 0 AND ABS(SUM(IMPORTETOTALMOVIMIENTOS)) >                              ");
			buf1.append("                      SUM(TOTALIMPORTESJCS)) THEN                                      ");
			buf1.append("                    0 ");
			buf1.append("                 ELSE ");
			buf1.append("                    SUM(IMPORTEIRPF) + (SUM(IMPORTETOTALMOVIMIENTOS)*MAX(PORCENTAJEIRPF)/100) ");
			buf1.append("                 END AS TOTALIMPORTEIRPF " );
			
			buf1.append("          FROM ((SELECT IDPERSONA AS IDPERSONASJCS,                                    ");
			buf1.append("                        SUM(IMPORTEPAGADO) AS TOTALIMPORTESJCS,                        ");
			buf1.append("                        0 AS IMPORTETOTALRETENCIONES,                                  ");
			buf1.append("                        0 AS IMPORTETOTALMOVIMIENTOS,                                  ");
			buf1.append("                        MAX(PORCENTAJEIRPF) AS PORCENTAJEIRPF,                          ");
			buf1.append("                        SUM(IMPORTEIRPF) AS IMPORTEIRPF                                ");
			buf1.append("                   FROM FCS_PAGO_ACTUACIONDESIGNA                                      ");
			buf1.append("                  WHERE IDINSTITUCION = "+idInstitucion+"                              ");
			buf1.append("                    AND IDPAGOSJG = "+idPago+"                                         ");
			buf1.append("                  GROUP BY IDPERSONA) UNION ALL                                        ");
			buf1.append("                (SELECT IDPERSONA AS IDPERSONASJCS,                                    ");
			buf1.append("                        SUM(IMPORTEPAGADO) AS TOTALIMPORTESJCS,                        ");
			buf1.append("                        0 AS IMPORTETOTALRETENCIONES,                                  ");
			buf1.append("                        0 AS IMPORTETOTALMOVIMIENTOS,                                  ");
			buf1.append("                        MAX(PORCENTAJEIRPF) AS PORCENTAJEIRPF,                         ");
			buf1.append("                        SUM(IMPORTEIRPF) AS IMPORTEIRPF                                ");
			buf1.append("                   FROM FCS_PAGO_APUNTE                                                ");
			buf1.append("                  WHERE IDINSTITUCION = "+idInstitucion+"                              ");
			buf1.append("                    AND IDPAGOSJG = "+idPago+"                                         ");
			buf1.append("                  GROUP BY IDPERSONA) UNION ALL                                        ");
			buf1.append("                (SELECT IDPERSONA AS IDPERSONASJCS,                                    ");
			buf1.append("                        SUM(IMPORTEPAGADO) AS TOTALIMPORTESJCS,                        ");
			buf1.append("                        0 AS IMPORTETOTALRETENCIONES,                                  ");
			buf1.append("                        0 AS IMPORTETOTALMOVIMIENTOS,                                  ");
			buf1.append("                        MAX(PORCENTAJEIRPF) AS PORCENTAJEIRPF,                         ");
			buf1.append("                        SUM(IMPORTEIRPF) AS IMPORTEIRPF                                ");
			buf1.append("                   FROM FCS_PAGO_SOJ                                                   ");
			buf1.append("                  WHERE IDINSTITUCION = "+idInstitucion+"                              ");
			buf1.append("                    AND IDPAGOSJG = "+idPago+"                                         ");
			buf1.append("                  GROUP BY IDPERSONA) UNION ALL                                        ");
			buf1.append("                (SELECT IDPERSONA AS IDPERSONASJCS,                                    ");
			buf1.append("                        SUM(IMPORTEPAGADO) AS TOTALIMPORTESJCS,                        ");
			buf1.append("                        0 AS IMPORTETOTALRETENCIONES,                                  ");
			buf1.append("                        0 AS IMPORTETOTALMOVIMIENTOS,                                  ");
			buf1.append("                        MAX(PORCENTAJEIRPF) AS PORCENTAJEIRPF,                         ");
			buf1.append("                        SUM(IMPORTEIRPF) AS IMPORTEIRPF                                ");
			buf1.append("                   FROM FCS_PAGO_EJG                                                   ");
			buf1.append("                  WHERE IDINSTITUCION = "+idInstitucion+"                              ");
			buf1.append("                    AND IDPAGOSJG = "+idPago+"                                         ");
			buf1.append("                  GROUP BY IDPERSONA) UNION ALL                                        ");
			buf1.append("                (SELECT IDPERSONA AS IDPERSONASJCS,                                    ");
			buf1.append("                        0 AS TOTALIMPORTESJCS,                                         ");
			buf1.append("                        0 AS IMPORTETOTALRETENCIONES,                                  ");
			buf1.append("                        SUM(NVL(CANTIDAD, 0)) AS IMPORTETOTALMOVIMIENTOS,              ");
			buf1.append("                        0 AS PORCENTAJEIRPF,                                           ");
			buf1.append("                        0 AS IMPORTEIRPF                                               ");
			buf1.append("                   FROM FCS_MOVIMIENTOSVARIOS                                          ");
			buf1.append("                  WHERE IDINSTITUCION = "+idInstitucion+"                              ");
			buf1.append("                    AND IDPAGOSJG = "+idPago+"                                         ");
			buf1.append("                  GROUP BY IDPERSONA) UNION ALL                                        ");
			buf1.append("                (SELECT IDPERSONA AS IDPERSONASJCS,                                    ");
			buf1.append("                        0 AS TOTALIMPORTESJCS,                                         ");
			buf1.append("                        SUM(NVL(IMPORTERETENIDO, 0)) AS IMPORTETOTALRETENCIONES,       ");
			buf1.append("                        0 AS IMPORTETOTALMOVIMIENTOS,                                  ");
			buf1.append("                        0 AS PORCENTAJEIRPF,                                           ");
			buf1.append("                        0 AS IMPORTEIRPF                                               ");
			buf1.append("                   FROM FCS_COBROS_RETENCIONJUDICIAL                                   ");
			buf1.append("                  WHERE IDINSTITUCION = "+idInstitucion+"                              ");
			buf1.append("                    AND IDPAGOSJG = "+idPago+"                                         ");
			buf1.append("                  GROUP BY IDPERSONA))                                                 ");
			buf1.append("         GROUP BY IDPERSONASJCS),                                                      ");
			buf1.append("       (SELECT NVL(SUM(IMPORTEPAGADO), 0) IMPORTE_PAGADO,                              ");
			buf1.append("               NVL(SUM(FCS_FACT_APUNTE.PRECIOAPLICADO), 0) IMPORTE_FACTURADO           ");
			buf1.append("          FROM FCS_PAGO_APUNTE, FCS_FACT_APUNTE                                        ");
			buf1.append("         WHERE FCS_PAGO_APUNTE.IDPAGOSJG = "+idPago+"                                  ");
			buf1.append("           AND FCS_PAGO_APUNTE.IDPERSONA = "+idPersona+"                               ");
			buf1.append("           AND FCS_PAGO_APUNTE.IDINSTITUCION = FCS_FACT_APUNTE.IDINSTITUCION           ");
			buf1.append("           AND FCS_PAGO_APUNTE.IDFACTURACION = FCS_FACT_APUNTE.IDFACTURACION           ");
			buf1.append("           AND FCS_PAGO_APUNTE.IDAPUNTE = FCS_FACT_APUNTE.IDAPUNTE                     ");
			buf1.append("           AND FCS_PAGO_APUNTE.IDINSTITUCION = "+idInstitucion+") A,                   ");
			buf1.append("       (SELECT NVL(SUM(IMPORTEPAGADO), 0) IMPORTE_PAGADO,                              ");
			buf1.append("               NVL(SUM(FCS_FACT_ACTUACIONDESIGNA.PRECIOAPLICADO), 0) IMPORTE_FACTURADO ");
			buf1.append("          FROM FCS_PAGO_ACTUACIONDESIGNA, FCS_FACT_ACTUACIONDESIGNA                    ");
			buf1.append("         WHERE FCS_PAGO_ACTUACIONDESIGNA.IDPAGOSJG = "+idPago+"                        ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.IDPERSONA = "+idPersona+"                     ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.IDINSTITUCION =                               ");
			buf1.append("               FCS_FACT_ACTUACIONDESIGNA.IDINSTITUCION                                 ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.IDFACTURACION =                               ");
			buf1.append("               FCS_FACT_ACTUACIONDESIGNA.IDFACTURACION                                 ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.IDTURNO =                                     ");
			buf1.append("               FCS_FACT_ACTUACIONDESIGNA.IDTURNO                                       ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.ANIO =                                        ");
			buf1.append("               FCS_FACT_ACTUACIONDESIGNA.ANIO                                          ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.NUMERO =                                      ");
			buf1.append("               FCS_FACT_ACTUACIONDESIGNA.NUMERO                                        ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.NUMEROASUNTO =                                ");
			buf1.append("               FCS_FACT_ACTUACIONDESIGNA.NUMEROASUNTO                                  ");
			buf1.append("           AND FCS_PAGO_ACTUACIONDESIGNA.IDINSTITUCION = "+idInstitucion+") O          ");
			buf1.append("           WHERE IDPERSONASJCS = "+idPersona+"                                         ");
			
			
			/*buf1.append("select ");
			buf1.append("A.importe_pagado TOTAL_ASISTENCIA,");
			buf1.append("O.importe_pagado TOTAL_OFICIO,");
			buf1.append("to_char(A.importe_pagado + O.importe_pagado,'"+formatoImportes+"') TOTAL_GENERAL,");
			buf1.append("to_char(A.importe_irpf + O.importe_irpf,'"+formatoImportes+"') TOTAL_IRPF,");
			buf1.append("to_char(A.importe_TOTAL + O.importe_TOTAL,'"+formatoImportes+"') TOTAL_LIQUIDACION");
			buf1.append("  from ");
			buf1.append("(select nvl(sum(importepagado), 0) importe_pagado, nvl(sum(importeIrpf), 0) importe_irpf, nvl(sum(importepagado - importeIrpf), 0) importe_total");
			buf1.append("   from fcs_pago_apunte ");
			buf1.append("  where idpagosjg="+idPago+" and idpersona="+idPersona+" and idInstitucion="+idInstitucion);
			buf1.append(") A,");
			buf1.append("(select nvl(sum(importepagado), 0) importe_pagado, nvl(sum(importeIrpf), 0) importe_irpf, nvl(sum(importepagado - importeIrpf), 0) importe_total");
			buf1.append("   from fcs_pago_actuaciondesigna ");
			buf1.append("  where idpagosjg="+idPago+" and idpersona="+idPersona+" and idInstitucion="+idInstitucion);
			buf1.append(") O");*/
			/*
			 buf.append("from fcs_pago_ejg ");
			 buf.append("from fcs_pago_soj ");
			 */
			rc = new RowsContainer();
			rc.find(buf1.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				
				String sTotalGeneral=r.getString("TOTAL_GENERAL");
				result.put("IMPORTETOTAL",UtilidadesNumero.formatoCartaPago(sTotalGeneral));
				result.put("TOTAL_GENERAL",UtilidadesNumero.formatoCartaPago(sTotalGeneral)+ClsConstants.CODIGO_EURO);
				
				String sTotalLiquidacion=r.getString("TOTAL_LIQUIDACION");
			
				result.put("TOTAL_LIQUIDACION",UtilidadesNumero.formatoCartaPago(sTotalLiquidacion)+ClsConstants.CODIGO_EURO);
				String sTotalIRPF=r.getString("TOTAL_IRPF");
				
				result.put("TOTAL_IRPF",UtilidadesNumero.formatoCartaPago(sTotalIRPF)+ClsConstants.CODIGO_EURO);
				
				String sTotalMovimientos=r.getString("TOTAL_MOVIMIENTOS");
				result.put("TOTAL_MOVIMIENTOS",UtilidadesNumero.formatoCartaPago(sTotalMovimientos)+ClsConstants.CODIGO_EURO);
				
				String sTotalRetenciones=r.getString("TOTAL_RETENCIONES");
				result.put("TOTAL_RETENCIONES",UtilidadesNumero.formatoCartaPago(sTotalRetenciones)+ClsConstants.CODIGO_EURO);
				
				dTotalAsistencia = Double.parseDouble(r.getString("TOTAL_ASISTENCIA"));
				result.put("TOTAL_ASISTENCIA",UtilidadesString.formatoImporte(dTotalAsistencia)+ClsConstants.CODIGO_EURO);
				dTotalFactAsistencia = Double.parseDouble(r.getString("TOTAL_FACTURADO"));
			    result.put("CPC_ASISTENCIA",
						 UtilidadesString.formatoImporte(dTotalFactAsistencia)+ClsConstants.CODIGO_EURO);
				
				dTotalOficio = Double.parseDouble(r.getString("TOTAL_OFICIO"));
				dTotalFactOficio = Double.parseDouble(r.getString("TOTAL_FACTURADO_OFICIO"));
				result.put("TOTAL_OFICIOS",UtilidadesString.formatoImporte(dTotalOficio)+ClsConstants.CODIGO_EURO);
				result.put("CPC_OFICIOS",
						 UtilidadesString.formatoImporte(dTotalFactOficio)+ClsConstants.CODIGO_EURO);
			}
			
			//2. Calculo el IRPF:
			StringBuffer buf2 = new StringBuffer();
			buf2.append("select  distinct porcentajeirpf from ");
			buf2.append(" ( ");
			
			buf2.append("select  porcentajeirpf ");
			buf2.append("from fcs_pago_actuaciondesigna pp ");
			buf2.append("where idpagosjg="+idPago+" and idpersona="+idPersona+" and idInstitucion="+idInstitucion+" ");
			
			buf2.append("union ");
			
			buf2.append("select  porcentajeirpf ");
			buf2.append("from fcs_pago_apunte ");
			buf2.append("where idpagosjg="+idPago+" and idpersona="+idPersona+" and idInstitucion="+idInstitucion+" ");
			
			buf2.append("union ");
			 
			buf2.append("select  porcentajeirpf ");
			buf2.append("from fcs_pago_ejg ");
			buf2.append("where idpagosjg="+idPago+" and idpersona="+idPersona+" and idInstitucion="+idInstitucion+" ");
			 
			buf2.append("union ");
			 
			buf2.append("select  porcentajeirpf ");
			buf2.append("from fcs_pago_soj ");
			buf2.append("where idpagosjg="+idPago+" and idpersona="+idPersona+" and idInstitucion="+idInstitucion+" ");
			 
			buf2.append(") ");
			
			rc = new RowsContainer();
			rc.find(buf2.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				if (r.getString("PORCENTAJEIRPF")!=null && !r.getString("PORCENTAJEIRPF").equals(""))
					IRPF = Integer.parseInt(r.getString("PORCENTAJEIRPF"));
			}
			result.put("IRPF",Integer.toString(IRPF));
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}		
		return result;
	}
	public String getDireccionCartaPago (String idSociedad, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
		   String sql="";
		   Row result=new Row();
	      
	            RowsContainer rc = new RowsContainer(); 
	            sql=
			    	"select D.DOMICILIO, D.CODIGOPOSTAL," +
			    	"(select p.nombre from cen_poblaciones p where p.idpoblacion=d.idpoblacion) POBLACION," +
			    	"(select p.nombre from cen_provincias p where p.idprovincia=d.idprovincia) PROVINCIA" +
			    	"  from CEN_DIRECCIONES D, CEN_DIRECCION_TIPODIRECCION DTD" +
			    	" where D.IDDIRECCION = DTD.IDDIRECCION  " +
			    	"   and D.IDINSTITUCION = DTD.IDINSTITUCION   " +
			    	"   and D.IDPERSONA = DTD.IDPERSONA    " +
			    	"   and d.fechabaja is null"+
			    	"   and D.IDPERSONA = " +idSociedad+
	                "   and D.IDINSTITUCION="+idInstitucion;
	                
							try{
							 if (idDireccion!=null){
								if ((new Integer(idDireccion)) != null){ 
									sql+=  " AND DTD." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "=" +idDireccion;
								}	
								else {
									throw new Exception ("");
								}
							 }	
							 	
							}	catch(Exception e){
								sql+=  " AND upper(D.PREFERENTE) like upper('%" +idDireccion+"%') ";
							}
							
							
	      
	       
	      
	       return sql;                        
	    }
	public void enviarPagoColegiado(UsrBean usrBean, EnvProgramPagosBean programPagoBean, 
			EnvEnvioProgramadoBean envioProgramadoBean)throws ClsExceptions,SIGAException{
		
		Envio envio = new Envio(usrBean,envioProgramadoBean.getNombre());

		// Bean envio
		EnvEnviosBean enviosBean = envio.getEnviosBean();
		enviosBean.setDescripcion(enviosBean.getIdEnvio()+" "+enviosBean.getDescripcion());
		// Preferencia del tipo de envio si el usuario tiene uno:
		enviosBean.setIdTipoEnvios(envioProgramadoBean.getIdTipoEnvios());

		enviosBean.setIdPlantillaEnvios(envioProgramadoBean.getIdPlantillaEnvios());
		if (envioProgramadoBean.getIdPlantilla()!=null && !envioProgramadoBean.getIdPlantilla().equals("")) {
			enviosBean.setIdPlantilla(envioProgramadoBean.getIdPlantilla());
		} else {
			enviosBean.setIdPlantilla(null);
		}
		
		String pathDocumento=getPathInformePagoColegiadoAEnviar(usrBean,
				programPagoBean.getIdPago().toString(),programPagoBean.getIdPersona().toString(),
				programPagoBean.getIdiomaCodigoExt(),programPagoBean.getIdInstitucion().toString());
		
		// Creacion documentos
		int indice = pathDocumento.lastIndexOf(ClsConstants.FILE_SEP);
		String descDocumento = "";
		if(indice >0)
			descDocumento = pathDocumento.substring(indice+1);
		
		
		Documento documento = new Documento(pathDocumento,descDocumento);
		Vector vDocumentos = new Vector();
		vDocumentos.add(documento);
	
		// Genera el envio:
		envio.generarEnvio(programPagoBean.getIdPersona().toString(),vDocumentos);
		
	} 
	
}