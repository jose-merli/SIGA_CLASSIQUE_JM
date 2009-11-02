
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
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvProgramPagosBean;
import com.siga.beans.FcsPagoColegiadoAdm;
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
			
			
			// TRATAMIENTO POR CADA COLEGIADO
			/////////////////////////////////
			String idPago = miform.getIdPago();

			Vector vColegiados = null;
			if(miform.getIdPersona()!=null && !miform.getIdPersona().trim().equals("")){
				vColegiados = new Vector();
				Hashtable registro = new Hashtable(); 
				registro.put("IDPERSONA_SJCS", miform.getIdPersona());
				vColegiados.add(registro);
			}else{
				FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(usr);
				vColegiados = pagosAdm.getColegiadosInformeCartaPago(institucion,idPago);
			}
			
			File fPdf = null;
			if (vColegiados!=null && !vColegiados.isEmpty()){
				boolean correcto=true;
				Enumeration lista=vColegiados.elements();
	    		
				while(correcto && lista.hasMoreElements()){
					Hashtable datosBase=(Hashtable)lista.nextElement();
					
					// Obtener el idioma del colegiado
					AdmLenguajesAdm lenguajesAdm = new AdmLenguajesAdm(usr);
					CenClienteAdm clienteAdm = new CenClienteAdm(usr);
					String idiomaExt;
					String idpersona = datosBase.get("IDPERSONA_SJCS").toString();
					idiomaExt = lenguajesAdm.getCodigoExt(
								clienteAdm.getLenguaje(miform.getIdInstitucion(), idpersona));
				
					//Obtiene la plantilla segun el idioma
				    String nombrePlantilla=ClsConstants.PLANTILLA_FO_COLEGIADOPAGO+"_"+idiomaExt+".fo";
				    String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
					
					
					String ncolegiado=admColegiado.getIdentificadorColegiado(
							admColegiado.getDatosColegiales(UtilidadesHash.getLong(datosBase,"IDPERSONA_SJCS"),new Integer(usr.getLocation())));
					if (ncolegiado==null){
						ncolegiado="";
					}
					
				    //Los datos comunes son los mismos datos para todos los 
					//colegiados pero puede variar el idioma.
				    Hashtable datosComunes= this.obtenerDatosComunes(usr,idiomaExt);
				    datosComunes.put("IDPAGO",idPago);				    
					datosBase.putAll(datosComunes);
					
					
					fPdf = this.generarInforme(usr,datosBase,rutaServidorTmp,contenidoPlantilla,rutaServidorTmp,ncolegiado+"-"+"colegiadoPago_" +numeroCarta);
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
						if(rutaTmp!=null){
							Plantilla.borrarDirectorio(rutaTmp);
						}
					}else{
						//una vez se descargue el fichero, hay que borrar el directorio
						request.setAttribute("borrarDirectorio","true");
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
		htAux=this.obtenerDatosPago(institucion, idPersona, idPagos, usr);
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
		    	"SELECT DECODE(CUEN.NUMEROCUENTA,NULL,'',CUEN.CBO_CODIGO||' '||CUEN.CODIGOSUCURSAL||' '||CUEN.DIGITOCONTROL||' '||CUEN.NUMEROCUENTA||' '|| substr(BAN.NOMBRE,6)) CUENTA_CORRIENTE" +
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
			StringBuffer sql1 = new StringBuffer();
			sql1.append("select to_char(pag.fechadesde, 'DD/MM/YYYY') FECHADESDE, ");
			sql1.append(" to_char(pag.fechahasta, 'DD/MM/YYYY') FECHAHASTA, ");
			sql1.append("TU.NOMBRE TURNO, ");
			sql1.append("f_siga_formatonumero(col.impasistencia, 2) IMPORTEPAGADO, ");
			sql1.append("pag.IDFACTURACION, ");
			sql1.append("fa.IDTURNO, ");
			sql1.append("fa.IDGUARDIA, ");
			sql1.append("fa.IDCALENDARIOGUARDIAS, ");
			sql1.append("f_siga_formatonumero(fa.precioaplicado, 2) IMPORTE_ACTUACION, ");
			sql1.append("fa.IDAPUNTE ");
			sql1.append("from FCS_PAGO_COLEGIADO col, fcs_pagosjg pag, fcs_fact_apunte fa, SCS_TURNO TU ");
			contador++;
			codigo.put(new Integer(contador),idInstitucion);
			sql1.append(" where COL.IDINSTITUCION = :"+contador);
			contador++;
			codigo.put(new Integer(contador),idPersona);
			sql1.append("   and COL.IDPERORIGEN = :" +contador);
			contador++;
			codigo.put(new Integer(contador),idPagos);	
			sql1.append("   and COL.IDPAGOSJG = :" +contador);
			sql1.append("   and COL.IDINSTITUCION = PAG.IDINSTITUCION ");
			sql1.append("   and COL.IDPAGOSJG = PAG.IDPAGOSJG ");
			sql1.append("   and PAG.IDFACTURACION = FA.IDFACTURACION ");
			sql1.append("   and PAG.IDINSTITUCION= FA.IDINSTITUCION ");
			sql1.append("   and COL.IDPERORIGEN = FA.IDPERSONA ");
			sql1.append("   and FA.IDTURNO = TU.IDTURNO ");
			sql1.append("   and FA.IDINSTITUCION = TU.IDINSTITUCION ");
			sql1.append(" order by PAG.FECHADESDE, TU.NOMBRE, FA.IDAPUNTE");
	 
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql1.toString(),codigo);
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
					String fechaDesde=r1.getString("FECHADESDE");
					String fechaHasta=r1.getString("FECHAHASTA");
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
						"   and FAP.FECHAINICIO between to_date('"+fechaDesde+"', 'DD/MM/YYYY') and to_date('"+fechaHasta+"', 'DD/MM/YYYY') "+
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
			StringBuffer sql = new StringBuffer();
			sql.append(" select distinct AD.FECHA, to_char(AD.FECHA,'DD/MM/YYYY') FECHA_OFICIO,  PRO.NOMBRE PROCEDIMIENTO, ");
			sql.append(" f_siga_formatonumero(COL.IMPOFICIO,2)  IMPORTEPAGADO, ");
			sql.append(" DES.ANIO || '/' || DES.CODIGO  ASIOFI, ");
			sql.append(" f_siga_getdefendidosdesigna(DES.IDINSTITUCION, des.anio, des.idturno, des.numero,0) NOMBRE_SOLICITANTE, ");
			sql.append(" f_siga_formatonumero(fact.precioaplicado,2) IMPORTE_PROCEDIMIENTO, ");
			sql.append(" f_siga_formatonumero(fact.precioaplicado*fact.porcentajefacturado/100,2) IMPORTE_OFICIO ");
			sql.append(" from FCS_PAGO_COLEGIADO   COL, ");
			sql.append(" SCS_ACTUACIONDESIGNA      AD, ");
			sql.append(" SCS_PROCEDIMIENTOS        PRO, ");
			sql.append(" SCS_DESIGNA               DES, ");
			sql.append(" FCS_FACT_ACTUACIONDESIGNA fact, ");
			sql.append(" FCS_PAGOSJG               pag, ");
			sql.append(" FCS_FACTURACIONJG         fac ");

			sql.append(" where DES.IDINSTITUCION = AD.IDINSTITUCION ");
			sql.append("    AND DES.IDTURNO = AD.IDTURNO ");
			sql.append("    AND DES.ANIO = AD.ANIO ");
			sql.append("    AND DES.NUMERO = AD.NUMERO ");
			sql.append("    AND COL.IDINSTITUCION = AD.IDINSTITUCION ");
			sql.append("    and AD.IDINSTITUCION = PRO.IDINSTITUCION ");
			sql.append("    and AD.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO ");
			   
			sql.append("    and COL.IDINSTITUCION = "+idInstitucion);
			sql.append("    and COL.IDPERORIGEN = "+idPersona);
			sql.append("    and COL.IDPAGOSJG = "+idPagos);
			  
			sql.append("    and col.idinstitucion = pag.idinstitucion ");
			sql.append("    and col.idpagosjg = pag.idpagosjg ");
			sql.append("    and COL.idinstitucion = fact.idinstitucion ");
			sql.append("    and COL.idperorigen = fact.idpersona ");
			  
			sql.append("    and ad.idinstitucion = fact.idinstitucion ");
			sql.append("    and ad.idfacturacion = fact.idfacturacion ");
			sql.append("    and ad.idpersonacolegiado = fact.idpersona ");
			sql.append("    and ad.NUMEROASUNTO = fact.NUMEROASUNTO ");
			sql.append("    and ad.NUMERO = fact.NUMERO ");
			sql.append("    and ad.ANIO = fact.ANIO ");
			sql.append("    and ad.IDTURNO = fact.IDTURNO ");
			
			sql.append("    and pag.idinstitucion = fact.idinstitucion ");
			sql.append("    and pag.idfacturacion = fact.idfacturacion ");
			  
			sql.append("    and fac.idinstitucion = fact.idinstitucion ");
			sql.append("    and fac.idfacturacion = fact.idfacturacion ");
			sql.append(" order by AD.FECHA");
			
			RowsContainer rc=new RowsContainer();
			rc.find(sql.toString());
			result=new Vector();
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
	protected Hashtable obtenerDatosPago(String idInstitucion, String idPersona, String idPago, UsrBean usr) throws ClsExceptions {
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
			buf0.append("       DECODE(FA.IMPORTEOFICIO,0,'0',F_SIGA_FORMATONUMERO(PA.IMPORTEOFICIO * 100 / FA.IMPORTEOFICIO, 2)) PORCENTAJE_TURNOS, ");
			buf0.append("       DECODE(FA.IMPORTEGUARDIA,0,'0',F_SIGA_FORMATONUMERO(PA.IMPORTEGUARDIA * 100 / FA.IMPORTEGUARDIA, 2)) PORCENTAJE_ASISTENCIA");
			buf0.append("  from FCS_PAGOSJG PA, FCS_FACTURACIONJG FA");
			buf0.append(" where pa.idinstitucion = "+idInstitucion);
			buf0.append("   and pa.idpagosjg = " +idPago);
			buf0.append("   and pa.idinstitucion = fa.idinstitucion");
			buf0.append("   and pa.idfacturacion = fa.idfacturacion");

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result=r.getRow();
				pcAsistencia=(String)r.getString("PORCENTAJE_ASISTENCIA");
				pcOficio=(String)r.getString("PORCENTAJE_TURNOS");
			}


			//Se reutiliza la query del detalle de pagos para recuperar importes
			FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(usr);
			String sql = pagosAdm.getQueryDetallePagoColegiado(idInstitucion, idPago, idPersona);
			
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				
				String sTotalGeneral=r.getString("TOTALIMPORTESJCS");
				result.put("IMPORTETOTAL",UtilidadesNumero.formatoCartaPago(sTotalGeneral));
				result.put("TOTAL_GENERAL",UtilidadesNumero.formatoCartaPago(sTotalGeneral)+ClsConstants.CODIGO_EURO);
					
				String sTotalIRPF=r.getString("TOTALIMPORTEIRPF");
				result.put("TOTAL_IRPF",UtilidadesNumero.round(sTotalIRPF,2)+ClsConstants.CODIGO_EURO);
				
				String sTotalMovimientos=r.getString("IMPORTETOTALMOVIMIENTOS");
				result.put("TOTAL_MOVIMIENTOS",UtilidadesNumero.formatoCartaPago(sTotalMovimientos)+ClsConstants.CODIGO_EURO);
				
				String sTotalRetenciones=r.getString("IMPORTETOTALRETENCIONES");
				result.put("TOTAL_RETENCIONES",UtilidadesNumero.formatoCartaPago(sTotalRetenciones)+ClsConstants.CODIGO_EURO);
				
				Double dTotalLiquidacion =  Double.parseDouble(sTotalGeneral) + 
											Double.parseDouble(sTotalRetenciones) + 
											Double.parseDouble(sTotalIRPF) + 
											Double.parseDouble(sTotalMovimientos); 
				result.put("TOTAL_LIQUIDACION",UtilidadesNumero.formatoCartaPago(dTotalLiquidacion.toString())+ClsConstants.CODIGO_EURO);
			}
			
			//Obtiene el IRPF,los totales y facturados de oficios
			buf0 = new StringBuffer();
			buf0.append("SELECT NVL(SUM(col.impoficio), 0) TOTAL_OFICIO, ");
			buf0.append("NVL(SUM(act.PRECIOAPLICADO), 0) TOTAL_FACTURADO_OFICIO ");
			buf0.append("FROM FCS_PAGO_COLEGIADO col, FCS_PAGOSJG pag, FCS_FACTURACIONJG fac, FCS_FACT_ACTUACIONDESIGNA act ");
			buf0.append("WHERE col.IDPAGOSJG = "+idPago);
			buf0.append(" and col.idinstitucion = "+idInstitucion);
			buf0.append(" and col.idperorigen = "+idPersona);
			buf0.append(" and col.idinstitucion = pag.idinstitucion ");
			buf0.append(" and col.idpagosjg = pag.idpagosjg ");
			buf0.append(" and pag.idinstitucion = act.idinstitucion ");
			buf0.append(" and pag.idfacturacion = act.idfacturacion ");
			buf0.append(" and fac.idinstitucion = act.idinstitucion ");
			buf0.append(" and fac.idfacturacion = act.idfacturacion ");
			buf0.append(" and col.idperorigen = act .idpersona ");

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
								
				dTotalOficio = Double.parseDouble(r.getString("TOTAL_OFICIO"));
				dTotalFactOficio = Double.parseDouble(r.getString("TOTAL_FACTURADO_OFICIO"));
				result.put("TOTAL_OFICIOS",UtilidadesString.formatoImporte(dTotalOficio)+ClsConstants.CODIGO_EURO);
				result.put("CPC_OFICIOS", UtilidadesString.formatoImporte(dTotalFactOficio)+ClsConstants.CODIGO_EURO);
			}
			
			//Obtiene el IRPF,los totales y facturados de asistencias
			buf0 = new StringBuffer();
			buf0.append("SELECT NVL(SUM(col.impasistencia), 0) TOTAL_ASISTENCIA, ");
			buf0.append("NVL(SUM(apu.PRECIOAPLICADO + apu.preciocostesfijos), 0) TOTAL_FACTURADO ");
			buf0.append("FROM FCS_PAGO_COLEGIADO col, FCS_PAGOSJG pag, FCS_FACTURACIONJG fac, FCS_FACT_APUNTE apu ");
			buf0.append("WHERE col.IDPAGOSJG = "+idPago);
			buf0.append(" and col.idinstitucion = "+idInstitucion);
			buf0.append(" and col.idperorigen = "+idPersona);
			buf0.append(" and col.idinstitucion = pag.idinstitucion ");
			buf0.append(" and col.idpagosjg = pag.idpagosjg ");
			buf0.append(" and pag.idinstitucion = apu.idinstitucion ");
			buf0.append(" and pag.idfacturacion = apu.idfacturacion ");
			buf0.append(" and fac.idinstitucion = apu.idinstitucion ");
			buf0.append(" and fac.idfacturacion = apu.idfacturacion ");
			buf0.append(" and col.idperorigen = apu.idpersona ");

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				
				dTotalAsistencia = Double.parseDouble(r.getString("TOTAL_ASISTENCIA"));
				result.put("TOTAL_ASISTENCIA",UtilidadesString.formatoImporte(dTotalAsistencia)+ClsConstants.CODIGO_EURO);
				dTotalFactAsistencia = Double.parseDouble(r.getString("TOTAL_FACTURADO"));
				result.put("CPC_ASISTENCIA", UtilidadesString.formatoImporte(dTotalFactAsistencia)+ClsConstants.CODIGO_EURO);
			}
			
			//obtiene el irpf del pago
			FcsPagoColegiadoAdm pagoAdm = new FcsPagoColegiadoAdm(usr);
			String irpf = pagoAdm.getIrpf(idInstitucion, idPago, idPersona);		
			result.put("IRPF",irpf);
				
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