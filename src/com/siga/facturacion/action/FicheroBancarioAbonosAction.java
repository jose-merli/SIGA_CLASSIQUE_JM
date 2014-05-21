package com.siga.facturacion.action;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CenBancos;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBancosAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPaisAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacAbonoIncluidoEnDisqueteAdm;
import com.siga.beans.FacAbonoIncluidoEnDisqueteBean;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FacBancoInstitucionBean;
import com.siga.beans.FacDisqueteAbonosAdm;
import com.siga.beans.FacDisqueteAbonosBean;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.FicheroEmisorAbonoBean;
import com.siga.beans.FicheroReceptorAbonoBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.form.FicheroBancarioAbonosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * Clase action para Descargar los ficheros bancarios.<br/>
 * Gestiona abrir y descargar Ficheros
 */
public class FicheroBancarioAbonosAction extends MasterAction{
	/** 	
	 *  *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
			
			String accion = miForm.getModo();
			
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("")){
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);			
			} else if (accion.equalsIgnoreCase("abrir")){
				miForm.reset(mapping,request);
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = abrir(mapping, miForm, request, response);		
			}else if (accion.equalsIgnoreCase("download")){
				mapDestino = download(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("generarFichero")){
				mapDestino = generarFichero(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("informeRemesa")){
				mapDestino = informeRemesa(miForm, request);
			}else if (accion.equalsIgnoreCase("buscarInit")){
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscar(mapping, miForm, request, response);
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
//			Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	
			{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
			
		}catch (SIGAException es) { 
			throw es; 
		}catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		} 
		
		
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="abrir";

		try{
			FicheroBancarioAbonosForm form = (FicheroBancarioAbonosForm) formulario;
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();			
			form.setIdInstitucion(idInstitucion);
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancosConCuentasBancarias(new Integer(idInstitucion));
			request.setAttribute("listaBancos", bancosList);
			
			if(request.getParameter("buscar") != null)
				request.setAttribute("buscar", request.getParameter("buscar"));
	
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return result;
	}	
	
	/** 
	 *  Funcion que atiende la accion abrir. Muestra todas las facturas programadas cuyas Fecha Real de Generación es null 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();	
			FicheroBancarioAbonosForm form 		= (FicheroBancarioAbonosForm)formulario;
			form.setIdInstitucion(idInstitucion);
			
			HashMap databackup=new HashMap();
			if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
			     PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)databackup.get("paginador");
			     Vector datos=new Vector();
					
			     //Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				if (paginador!=null) {					
					String pagina = (String)request.getParameter("pagina");				
					if (pagina!=null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
					
			} else {	
		  	    databackup=new HashMap();
		  	    
		  	    FacDisqueteAbonosAdm adm = new FacDisqueteAbonosAdm(this.getUserBean(request));
		  	    boolean abonosSJCS      = false;
		  	    
		  	    // Recuperamos la procedencia de la llamada (1-Facturacion SJCS, 0-Facturacion)
		  	    String sjcs = request.getParameter("sjcs");
		  	    if ((sjcs!=null) && (sjcs.equals("1"))){
		  	    	abonosSJCS = true;
		  	    }
			
		  	    PaginadorCaseSensitive ficheros = adm.getDatosFichero(form, abonosSJCS);
				
				databackup.put("paginador", ficheros);
				if (ficheros!=null){ 
					Vector datos = ficheros.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				} 
			}		
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return "listado";
	}
	
	/** 
	 *  Funcion que atiende la accion download. Descarga los ficheros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//String keyFichero 		= "facturacion.directorioBancosJava";	
		String directorioFisico = "facturacion.directorioFisicoAbonosBancosJava";
		String directorio 		= "facturacion.directorioAbonosBancosJava";
		String nombreFichero 	= "";
		String pathFichero		= "";
		String idInstitucion	= "";
		String barra = "";
		
		try{		
			//Integer usuario = this.getUserName(request);
			
			FicheroBancarioAbonosForm form 		= (FicheroBancarioAbonosForm)formulario;
			//FacDisqueteAbonosAdm adm 			= new FacDisqueteAbonosAdm(usuario);
			//FacDisqueteAbonosBean beanDisquete	= new FacDisqueteAbonosBean();
			//FacFacturaAdm admFactura = new FacFacturaAdm(usuario);
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 	= new ReadProperties ("SIGA.properties");
			pathFichero 		= p.returnProperty(directorioFisico) + p.returnProperty(directorio);		
			//String nombreFichero 	= p.returnProperty(keyFichero);						
			
			Vector ocultos 			= new Vector();		
			ocultos 				= (Vector)form.getDatosTablaOcultos(0);			
			nombreFichero 			= (String)ocultos.elementAt(1);	
			idInstitucion			= this.getIDInstitucion(request).toString();			
			
			
			// CAMBIO MAV 24/06/2005 SUSTITUYO
			// pathFichero += File.separator + idInstitucion + File.separator + nombreFichero;
			// POR
			// Generamos el nombre del fichero.
			
			if (pathFichero.indexOf("/") > -1){ 
				barra = "/";
			}
			if (pathFichero.indexOf("\\") > -1){ 
				barra = "\\";
			}
			
			// FIN CAMBIO
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		//creamos la lista de ficheros adjuntos
    	List<File> lista = new ArrayList<File>();    	
    	File directorioFicheros= new File(pathFichero + barra + idInstitucion);
    	
    	//Se buscan todos los ficheros que coincidan con el nombre del fichero
    	if(directorioFicheros.exists()){
	    	File[] ficheros = directorioFicheros.listFiles();
	    	String nombreFicheroListadoSinExtension, nombreFicheroGeneradoSinExtension;
	    	for (int x=0; x<ficheros.length; x++){
				nombreFicheroListadoSinExtension = (ficheros[x].getName().indexOf(".") > 0) ? ficheros[x].getName().substring(0, ficheros[x].getName().indexOf(".")) 
																							: ficheros[x].getName();
				nombreFicheroGeneradoSinExtension = (nombreFichero.indexOf(".") > 0) ? nombreFichero.substring(0, nombreFichero.indexOf(".")) 
																					 : nombreFichero;
	    		if(nombreFicheroGeneradoSinExtension.equalsIgnoreCase(nombreFicheroListadoSinExtension)){
	    			lista.add(ficheros[x]);
	    		}	    		
	    	}
    	}
    	
    	if(lista.size() <= 0){
    		throw new SIGAException("No se ha encontrado el fichero generado");
    	}
    	 
    	pathFichero += barra + idInstitucion + barra + nombreFichero+".zip";
    	File filezip = SIGAServicesHelper.doZip(pathFichero, lista);		
		
		request.setAttribute("nombreFichero", nombreFichero+".zip");
		request.setAttribute("rutaFichero", pathFichero);
		request.setAttribute("borrarFichero", "true");				
		
		return "descargaFichero";		
	}	
	
	/** 
	 *  Funcion que atiende la accion generarFichero. Genera los ficheros de Abonos
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String generarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		UsrBean user = this.getUserBean(request);
		String lenguaje = user.getLanguage();
		String idInstitucion=user.getLocation();
		
		String resultado="";
		boolean correcto=true;
		UserTransaction tx = null;

		Vector bancos;
		Enumeration listaBancos, listaConceptos;
		Hashtable banco, bancoMenorComision;
		Vector abonosBanco, conceptos;
		String concepto;
		int cont = 0, nFicherosGenerados = 0;
		
		try {		 				
			
			// Recuperamos la procedencia de la llamada (1-Facturacion SJCS, 0-Facturacion)
			FicheroBancarioAbonosForm miForm = (FicheroBancarioAbonosForm)formulario;
			String fcs = miForm.getSjcs();
			if (fcs == null || fcs.equals(""))
				fcs = "0";
			
			// Manejadores para los accesos a BBDD
			FacAbonoAdm adminAbono=new FacAbonoAdm(user);
			FacBancoInstitucionAdm adminBancoInst=new FacBancoInstitucionAdm(this.getUserBean(request));
			GenParametrosAdm adminParam = new GenParametrosAdm(user);
			
			tx = user.getTransactionPesada();
			tx.begin();
			if (fcs.equals("1")){
				bancos=adminBancoInst.obtenerBancos(idInstitucion);
				if (!bancos.isEmpty()) {
					listaBancos=bancos.elements();
					while (listaBancos.hasMoreElements()){
						banco=((Row)listaBancos.nextElement()).getRow();
						
						conceptos=adminAbono.getConceptosAbonosBancoSjcs(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
						if (conceptos.isEmpty()) continue;
						listaConceptos=conceptos.elements();
						while (listaConceptos.hasMoreElements()) {
							concepto = (String) ((Row)listaConceptos.nextElement()).getRow().get("CONCEPTO");
							if (concepto == null)
								concepto = "";
						
							abonosBanco=adminAbono.getAbonosBancoSjcs(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO), concepto);
							nFicherosGenerados = prepararFichero(user, banco, abonosBanco, "000", fcs);
							if (nFicherosGenerados < 0) {
								correcto = false;
							} else {
								correcto = true;
								cont += nFicherosGenerados;
							}
						}
					}
				}
			} else {	
				bancos=adminBancoInst.getBancoMenorComision(idInstitucion);
				if (!bancos.isEmpty()){
					bancoMenorComision=((Row)bancos.firstElement()).getRow();
					abonosBanco=adminAbono.getAbonosBancosMenorComision(idInstitucion,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
					nFicherosGenerados = prepararFichero(user, bancoMenorComision, abonosBanco, "000"/*adminParam.getValor(idInstitucion, "FCS", "CONCEPTO_ABONO", "000")*/, fcs);
					if (nFicherosGenerados < 0) {
						correcto = false;
					} else {
						correcto = true;
						cont += nFicherosGenerados;
					}
				} else {
					bancoMenorComision = new Hashtable();
				}
				
				bancos=adminBancoInst.getRestoBancosConComision(idInstitucion,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
				if (!bancos.isEmpty()) {
					listaBancos=bancos.elements();
					while (listaBancos.hasMoreElements()){
						banco=((Row)listaBancos.nextElement()).getRow();
						abonosBanco=adminAbono.getAbonosBanco(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
						nFicherosGenerados = prepararFichero(user, banco, abonosBanco, "000"/*adminParam.getValor(idInstitucion, "FCS", "CONCEPTO_ABONO", "000")*/, fcs);
						if (nFicherosGenerados < 0) {
							correcto = false;
						} else {
							correcto = true;
							cont += nFicherosGenerados;
						}
					}
				}
			}
			
			if (correcto){
				tx.commit();
				
				String mensaje = "facturacion.ficheroBancarioAbonos.mensaje.generacionDisquetesOK";
				String[] datos = {String.valueOf(cont)};
				
				mensaje = UtilidadesString.getMensaje(mensaje, datos, lenguaje);				
				request.setAttribute("mensaje",mensaje);	
				
				resultado = "exitoConString";				
			}	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (resultado);		
	} //generarFichero()
	
	private int prepararFichero (UsrBean user, Hashtable banco, Vector abonosBanco, String sufijo, String fcs) throws Exception
	{
		// Controles
		CenInstitucionAdm admInstitucion=new CenInstitucionAdm(user);
		CenPersonaAdm admPersona=new CenPersonaAdm(user);
		CenColegiadoAdm admColegiado=new CenColegiadoAdm(user);
		FacDisqueteAbonosAdm admDisqueteAbonos=new FacDisqueteAbonosAdm(user);
		FacAbonoIncluidoEnDisqueteAdm admAbonoDisquete=new FacAbonoIncluidoEnDisqueteAdm(user);
		CenBancosAdm cenBancosAdm = new CenBancosAdm(user);
		CenPaisAdm cenPaisAdm = new CenPaisAdm(user);
		CenDireccionesAdm admDirecciones = new CenDireccionesAdm(user);
		FacAbonoAdm adminAbono=new FacAbonoAdm(user);
		GenParametrosAdm paramAdm = new GenParametrosAdm(user);
		
		String idInstitucion=user.getLocation();
		String lenguaje = user.getLanguage();
		
		boolean escribirAvisoCuentasIncompletas = false;
		int cont = 0;
		boolean correcto;
		Long idDisqueteAbono;

	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String prefijoFichero = rp.returnProperty("facturacion.prefijo.ficherosAbonos");
		
		if (abonosBanco.isEmpty())
			return cont;
			
		FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
		// Obtenemos el identificador
		idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
		// Los datos de la CCC
		if (banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO) == null || ((String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO)).equals("") ||
			banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL) == null || ((String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL)).equals("") ||
			banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA) == null || ((String)banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA)).equals("")) {
			escribirAvisoCuentasIncompletas = true;
		}
		emisor.setCodigoBanco((String)banco.get(FacBancoInstitucionBean.C_COD_BANCO));
		emisor.setCodigoSucursal((String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
		emisor.setNumeroCuenta((String)banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
		
		// Datos emisor
		emisor.setIdentificador(new Integer((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
		emisor.setIban((String)banco.get(FacBancoInstitucionBean.C_IBAN));
		String nombre=admInstitucion.getNombreInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION));
		emisor.setNif(admPersona.obtenerNIF(admInstitucion.getIdPersona(idInstitucion)));
		emisor.setNombre(nombre);
		emisor.setIdentificadorDisquete(idDisqueteAbono);
		String[] domicilioInstitucion;
		domicilioInstitucion = admInstitucion.getDomicilioInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION));
		emisor.setDomicilio(domicilioInstitucion[CenInstitucionAdm.C_SDOMICILIO]);
		emisor.setCodigopostal(domicilioInstitucion[CenInstitucionAdm.C_SCODIGOPOSTAL]);
		emisor.setPoblacion(domicilioInstitucion[CenInstitucionAdm.C_SPOBLACION]);
		emisor.setProvincia(domicilioInstitucion[CenInstitucionAdm.C_SPROVINCIA]);
		
		// Datos receptores
		Enumeration listaReceptores=abonosBanco.elements();
		Vector receptores=new Vector();
		Hashtable datosReceptor;
		FicheroReceptorAbonoBean receptor;
		while (listaReceptores.hasMoreElements()){
			datosReceptor = ((Row)listaReceptores.nextElement()).getRow();
			receptor=new FicheroReceptorAbonoBean();
			
			receptor.setIdentificador(new Long((String)datosReceptor.get(FacAbonoBean.C_IDPERSONA)));
			receptor.setNumeroAbono((String)datosReceptor.get(FacAbonoBean.C_NUMEROABONO));
			receptor.setReferenciaInterna((String)datosReceptor.get(FacAbonoBean.C_IDINSTITUCION) + " " + (String)datosReceptor.get(FacAbonoBean.C_IDPERSONA) + " " + (String)datosReceptor.get(FacAbonoBean.C_IDABONO));
			receptor.setIban((String)datosReceptor.get(CenCuentasBancariasBean.C_IBAN));
			receptor.setBic((String)(cenBancosAdm.getBancoIBAN((String)datosReceptor.get(CenCuentasBancariasBean.C_CBO_CODIGO))).getBic());
			receptor.setSepa((String)cenPaisAdm.getSepa((String)(cenBancosAdm.getBancoIBAN((String)datosReceptor.get(CenCuentasBancariasBean.C_CBO_CODIGO))).getIdPais()));
			if (datosReceptor.get(CenCuentasBancariasBean.C_CBO_CODIGO) == null || ((String)datosReceptor.get(CenCuentasBancariasBean.C_CBO_CODIGO)).equals("") ||
				datosReceptor.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL) == null || ((String)datosReceptor.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL)).equals("") ||
				datosReceptor.get(CenCuentasBancariasBean.C_NUMEROCUENTA) == null || ((String)datosReceptor.get(CenCuentasBancariasBean.C_NUMEROCUENTA)).equals("") ||
				datosReceptor.get(CenCuentasBancariasBean.C_DIGITOCONTROL) == null || ((String)datosReceptor.get(CenCuentasBancariasBean.C_DIGITOCONTROL)).equals("")) {
				escribirAvisoCuentasIncompletas = true;
			}
			receptor.setCodigoBanco((String)datosReceptor.get(CenCuentasBancariasBean.C_CBO_CODIGO));
			receptor.setCodigoSucursal((String)datosReceptor.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
			receptor.setNumeroCuenta((String)datosReceptor.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
			receptor.setDigitosControl((String)datosReceptor.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
			String nif=admPersona.obtenerNIF((String)datosReceptor.get(FacAbonoBean.C_IDPERSONA));
			receptor.setDni(nif);
			receptor.setImporte(new Double((String)datosReceptor.get("IMPORTE")));							
			nombre=admPersona.obtenerNombreApellidos((String)datosReceptor.get(FacAbonoBean.C_IDPERSONA));
			receptor.setNombre(nombre);
			String idPersona = (String)datosReceptor.get(FacAbonoBean.C_IDPERSONA);
			Vector direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
			if (direccionDespacho!=null && direccionDespacho.size()>0) {
				receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
				receptor.setCodigopostal((String)((Hashtable)direccionDespacho.get(0)).get("CODIGOPOSTAL_DESPACHO"));
				receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
				receptor.setProvincia((String)((Hashtable)direccionDespacho.get(0)).get("PROVINCIA_DESPACHO"));
				receptor.setPais((String)((Hashtable)direccionDespacho.get(0)).get("PAIS_DESPACHO"));
				receptor.setCodIsoPais((String)((Hashtable)direccionDespacho.get(0)).get("CODISO_PAIS_DESPACHO"));
			}
			
			receptor.setConcepto((fcs.equals("1")) ? (String)datosReceptor.get(FcsPagosJGBean.C_CONCEPTO) : "9");
			if (fcs.equals("1")) {
				String sacarLetrado="0";
				try {
					sacarLetrado = paramAdm.getValor(idInstitucion, "FCS", "INCLUIR_LETRADO_CONCEPTO_BANCO", "0");
				} catch (Exception e) {
				}
				if (sacarLetrado.equals("0"))
					receptor.setNombrePago((String)datosReceptor.get("NOMBREPAGO"));
				else{
					CenColegiadoBean beanColegiado =  admColegiado.getDatosColegiales(Long.valueOf((String)datosReceptor.get(FacAbonoBean.C_IDPERORIGEN)), Integer.valueOf(idInstitucion));
					receptor.setNombrePago((String)datosReceptor.get("NOMBREPAGO") + 
						'-' + ((beanColegiado.getComunitario().equals("0")) ? beanColegiado.getNColegiado() : beanColegiado.getNComunitario()) +
						'-' + admPersona.obtenerNombreApellidos((String)datosReceptor.get(FacAbonoBean.C_IDPERORIGEN)));
				}
			} else {
				receptor.setNombrePago((String)datosReceptor.get(FacAbonoBean.C_MOTIVOS));
			}
			
			receptores.addElement(receptor);						
		}
		
		if (sufijo == null)
			sufijo = "";
		
		// Creacion de un fichero de abonos por cada banco restante
		int nlineas = this.crearFichero(emisor, receptores, escribirAvisoCuentasIncompletas);
		this.crearFicheroSEPA(emisor, receptores, sufijo);
		cont ++;
		
		if (nlineas == 0)
			return cont;
			
		// Creacion entrada FAC_DISQUETEABONO
		Hashtable disqueteAbono=new Hashtable();
		disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
		disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
		disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
		disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
		disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString());
		// Sin extension ya que se emite el fichero antiguo y el nuevo. Se descargaran en un ZIP
		disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,fcs);
		disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
		correcto=admDisqueteAbonos.insert(disqueteAbono);
		
		if (!correcto)
			return -1;
		
		// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
		listaReceptores=abonosBanco.elements();
		while ((correcto)&&(listaReceptores.hasMoreElements())){
			Hashtable temporal=new Hashtable();
			temporal=((Row)listaReceptores.nextElement()).getRow();
			Hashtable abonoDisquete=new Hashtable();
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
			double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
			correcto=admAbonoDisquete.insert(abonoDisquete);
			
			if (!correcto) {
				new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
				return -1;
			}
				
			// RGG 29/05/2009 Cambio de funciones de abono
			// Obtengo el abono insertado
			Hashtable htA = new Hashtable();
			htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
			htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
			Vector vAbono = adminAbono.selectByPK(htA);
			FacAbonoBean bAbono = null;
			if (vAbono!=null && vAbono.size()>0) {
				bAbono = (FacAbonoBean) vAbono.get(0);
			}
			bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
			bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
			bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
			if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
				// pagado
				bAbono.setEstado(new Integer(1));
			} else {
				if (bAbono.getIdCuenta()!=null) {
					// pendiente pago banco
					bAbono.setEstado(new Integer(5));
				} else {
					// pendiente pago caja
					bAbono.setEstado(new Integer(6));
				}
			}
			if (!adminAbono.update(bAbono)){
				throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
			}
		}
		
		return cont;
	} //prepararFichero()
	
	/** 
	 *  Funcion que genera las lineas del fichero e inserta en el fichero.
	 * @param  bEmisor 		- bean que contiene los datos del emisor.
	 * @param  vReceptores	- Vector que contiene los bean de cada uno de los receptores.	 
	 * @return  boolean		- devuelve true en el caso de que se haya generado correctamente el fichero  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private int crearFichero(FicheroEmisorAbonoBean bEmisor, Vector vReceptores, boolean escribirAvisoCuentasIncompletas) throws SIGAException {
		//int n = (vReceptores.size() * 2) + 4 + 1;
		// (numeroReceptores * numeroCampos) + datosCabecera + datosTotales + lineaError (si es necesario)
		int n = (vReceptores.size() * 5) + 4 + 1 + 1;
		
		String[] cabecera = new String[n];
		boolean resul = false;		
		int nlinea = 0;
		try{	
			
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp 		= new ReadProperties("SIGA.properties");			
			String rutaServidor 	= rp.returnProperty("facturacion.directorioFisicoAbonosBancosJava") + rp.returnProperty("facturacion.directorioAbonosBancosJava");
			String sPrefijo 		= rp.returnProperty("facturacion.prefijo.ficherosAbonos");
			String sExtension 		= rp.returnProperty("facturacion.extension.ficherosAbonos");
			String nombreFichero	= "";
			String sIdInstitucion 	= bEmisor.getIdentificador().toString();	
			String numDisco			= bEmisor.getIdentificadorDisquete().toString();
			
//			Generamos el nombre del fichero.
			String barra = "";
			if (rutaServidor.indexOf("/") > -1){ 
				barra = "/";
			}
			if (rutaServidor.indexOf("\\") > -1){ 
				barra = "\\";
			}
			rutaServidor += barra + sIdInstitucion;
			nombreFichero = barra + sPrefijo + numDisco + "." + sExtension;
			
			// *********************	Generamos las lineas del fichero	*********************
			
			// ********************************* Datos Emisor **********************************
			String sCodRegistro			= "03";
			String sCodOperacion		= "56"; // 56:Si es una orden de transferencia.
			// 57:Si es ha de confecionarse un Cheque Bancario.
			String sCodOrdenante 		= completarEspacios("Nif", bEmisor.getNif(), "I", " ", 10, false);
			
			String fActual 				= UtilidadesBDAdm.getFechaBD("");
			String[] aux 				= fActual.split("/");
			String sFechaEnvio			= aux[0].concat(aux[1]).concat(aux[2].substring(2,4));
			String sFechaEmision		= sFechaEnvio;
			
			String sEntidadOrdenante	= completarEspacios("Entidad", bEmisor.getCodigoBanco(), "D", "0", 4, false);
			String sOficinaOrdenante	= completarEspacios("Oficina", bEmisor.getCodigoSucursal(), "D", "0", 4, false);
			String sCuentaOrdenante		= completarEspacios("Cuenta Bancaria", bEmisor.getNumeroCuenta(), "D", "0", 10, false);			
			String sDControlOrdenante	= obtenerDigitoControl("00" + sEntidadOrdenante + sOficinaOrdenante);
			sDControlOrdenante 			+=obtenerDigitoControl(sCuentaOrdenante);
			
			String sDetalleCargo		= "0"; // 0:Sin relacion
			// 1:Con relación 
			String sNombreOrdenante		= completarEspacios("Nombre", bEmisor.getNombre(), "I", " ", 36, true);
			String sDomicilioOrdenante	= completarEspacios("Domicilio", bEmisor.getDomicilio() + "  " + bEmisor.getCodigopostal(), "I", " ", 36, true);	
			String sPlazaOrdenante		= completarEspacios("Plaza", bEmisor.getPoblacion(), "I", " ", 36, true);
			
			String sNumDato	= "001";
			cabecera[0] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sFechaEnvio + sFechaEmision + sEntidadOrdenante + 
			sOficinaOrdenante +	sCuentaOrdenante + sDetalleCargo + rellenarEspacios(3) + sDControlOrdenante + rellenarEspacios(7);
			sNumDato		= "002";
			cabecera[1] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sNombreOrdenante + rellenarEspacios(7);
			sNumDato		= "003";
			cabecera[2] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sDomicilioOrdenante + rellenarEspacios(7);
			sNumDato		= "004";
			cabecera[3] = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(12)+ sNumDato + sPlazaOrdenante + rellenarEspacios(7);
			
			
//			****************************  Datos de los receptores   *****************************
			
			String sGastos 		= "1";
			String sConcepto 	= "9";			
			int cantidad 		= 0; 
			int importe;
			String sRefBeneficiario;			
			String sImporte;
			String sEntidadBeneficiario;
			String sOficinaBeneficiario;
			String sCuentaBeneficiario;
			String sDControlBeneficiario;
			String sNombreBeneficiario;
			String sNumRegistros;
			String sTotalRegistros;
			
			String sDomicilioBeneficiario;
			String sPoblacionBeneficiario;
			String sMotivos;
			
			nlinea = 3;
			Enumeration en = vReceptores.elements();
			while(en.hasMoreElements()){			
				FicheroReceptorAbonoBean bReceptor 	= (FicheroReceptorAbonoBean)en.nextElement();
				
				sCodRegistro			= "06";
				sRefBeneficiario 		= completarEspacios("Identificador", bReceptor.getIdentificador().toString(), "D", " ", 12, false);
				importe					= (int)Math.rint(bReceptor.getImporte().doubleValue()*100);
				cantidad 				+= importe;
				sImporte				= completarEspacios("Importe", Integer.toString(importe), "D", "0", 12, false);
				sEntidadBeneficiario	= completarEspacios("Entidad", bReceptor.getCodigoBanco(), "D", "0", 4, true);
				sOficinaBeneficiario	= completarEspacios("Oficina", bReceptor.getCodigoSucursal(), "D", "0", 4, false);	
				sCuentaBeneficiario		= completarEspacios("Cuenta Bancaria", bReceptor.getNumeroCuenta(), "D", "0", 10, false);
				sDControlBeneficiario	= obtenerDigitoControl("00" + sEntidadBeneficiario + sOficinaBeneficiario);
				sDControlBeneficiario	+=obtenerDigitoControl(sCuentaBeneficiario);
				sNombreBeneficiario		= completarEspacios("Nombre", bReceptor.getNombre(), "I", " ", 36, true);
				String direccion 		= UtilidadesString.replaceAllIgnoreCase(bReceptor.getDomicilio(), "\n", " ");
				direccion 		= UtilidadesString.replaceAllIgnoreCase(direccion, "\r", " ");
				sDomicilioBeneficiario = completarEspacios ("Direccion", direccion, "I", " ", 36, true);
				sPoblacionBeneficiario = completarEspacios ("Poblacion", bReceptor.getPoblacion(), "I", " ", 36, true);
				sMotivos = completarEspacios ("NombrePago", bReceptor.getNombrePago(), "I", " ", 36, true);
				
				if ((bReceptor.getConcepto()!=null)&& (bReceptor.getConcepto()!="0")){
					sConcepto = completarEspacios ("Concepto", bReceptor.getConcepto(), "I", " ", 1, true);
				}
				
				nlinea ++;
				sNumDato		 = "010";				
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sImporte + sEntidadBeneficiario + 
				sOficinaBeneficiario +	sCuentaBeneficiario + sGastos + sConcepto + rellenarEspacios(2) + sDControlBeneficiario + rellenarEspacios(7);
				
				nlinea ++;
				sNumDato		 = "011";
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sNombreBeneficiario + rellenarEspacios(7);
				
				// jbd 9/12/2008 - INC_05507_SIGA >>>
				nlinea ++;
				sNumDato		 = "012"; // Direccion
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sDomicilioBeneficiario + rellenarEspacios(7); 
				
				nlinea ++;
				sNumDato		 = "014"; // Poblacion
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato +  sPoblacionBeneficiario + rellenarEspacios(7);
				
				nlinea ++;
				sNumDato		 = "016"; // Concepto
				cabecera[nlinea] = sCodRegistro + sCodOperacion + sCodOrdenante + sRefBeneficiario + sNumDato + sMotivos + rellenarEspacios(7);
				// <<< INC_05507_SIGA
			}
			
//			****************************   Datos de los Totales   *****************************
			
			sCodRegistro		 = "08";
			sImporte		 	 = completarEspacios("Importe", Integer.toString(cantidad), "D", "0", 12, false);	
			sNumRegistros		 = completarEspacios("Numero Registros", Integer.toString(vReceptores.size()), "D", "0", 8, false);
			sTotalRegistros		 = completarEspacios("Numero Registros",Integer.toString(nlinea+2), "D", "0", 10, false);			
			cabecera[nlinea+1] 	 = sCodRegistro + sCodOperacion + sCodOrdenante + rellenarEspacios(15) + sImporte + sNumRegistros + sTotalRegistros + rellenarEspacios(13);
			
			// escribiendo linea de aviso por cuentas incompletas
			if (escribirAvisoCuentasIncompletas)
				cabecera[nlinea+2] = rp.returnProperty("facturacion.ficheroBancario.mensaje.avisoCuentasBancariasIncompletas");
			else
				cabecera[nlinea+2] = "";
			
			resul 				 = escribirFichero(cabecera,rutaServidor,nombreFichero);	
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		if (resul){
			return nlinea+2; // Se suma 2 porque empieza en cero y no se incrementa en la ultima linea
		}else{
			return 0;
		}
	} //crearFichero()
	
	/** 
	 *  Funcion que genera las lineas del fichero e inserta en el fichero SEPA.
	 * @param  bEmisor 		- bean que contiene los datos del emisor.
	 * @param  vReceptores	- Vector que contiene los bean de cada uno de los receptores.	 
	 * @return  boolean		- devuelve true en el caso de que se haya generado correctamente el fichero  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private int crearFicheroSEPA(FicheroEmisorAbonoBean bEmisor, Vector vReceptores, String sufijo) throws SIGAException
	{
		final int c_ORDENANTE = 0; 
		final int c_BENEFICIARIOSEPA = 1; 
		final int c_BENEFICIARIOOTROS = 2; 
		final int c_NBLOQUES = 3;
		StringBuffer[] cabeceras = new StringBuffer[c_NBLOQUES];
		StringBuffer[] totales = new StringBuffer[c_NBLOQUES];
		StringBuffer[] registrosBenefSEPA = new StringBuffer[vReceptores.size()]; //no se llenaran todos, pero a priori no sabemos cuantos seran
		StringBuffer[] registrosBenefOtros = new StringBuffer[vReceptores.size()]; //no se llenaran todos, pero a priori no sabemos cuantos seran
		int nRegistrosBenefSEPA = 0, nRegistrosBenefOtros = 0;
		int importe = 0, subtotalSEPA = 0, subtotalOtros = 0;
		
		boolean resul = false;
		
		try{	
			// calculando nombre y ruta del fichero
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String rutaServidor 	= rp.returnProperty("facturacion.directorioFisicoAbonosBancosJava") + rp.returnProperty("facturacion.directorioAbonosBancosJava");
			String sIdInstitucion 	= bEmisor.getIdentificador().toString();	
			String sPrefijo 		= rp.returnProperty("facturacion.prefijo.ficherosAbonos");
			String numDisco			= bEmisor.getIdentificadorDisquete().toString();
			numDisco 				+= ".SEPA";
			String sExtension 		= rp.returnProperty("facturacion.extension.ficherosTransferenciasSEPA");
			String barra 			= (rutaServidor.indexOf("/") > -1) ? "/" : "\\";
			rutaServidor 			+= barra + sIdInstitucion;
			String nombreFichero 	= barra + sPrefijo + numDisco + "." + sExtension;
			
			// calculando fechas y version del cuaderno
			String fActual 			= GstDate.getHoyJsp();
			
			// JPT: Si la fecha indicada no es habil, se indica el siguiente dia habil
			fActual = EjecucionPLs.ejecutarSumarDiasHabiles(fActual, "0");		
			
			String[] aux 			= fActual.split("/");
			String sFechaEnvio		= aux[2] + aux[1] + aux[0];
			String sFechaEmision	= sFechaEnvio;
			String versionCuaderno 	= rp.returnProperty("facturacion.cuaderno.transferencias.identificador");  
			versionCuaderno 		+= Integer.parseInt(versionCuaderno) % 7;
			
			// 1de7. generando cabecera de ordenante
			cabeceras[c_ORDENANTE] = new StringBuffer();
			cabeceras[c_ORDENANTE].append("01ORD"); //codigo de registro y operacion
			cabeceras[c_ORDENANTE].append(versionCuaderno); //version del cuaderno
			cabeceras[c_ORDENANTE].append("001"); //numero de dato
			cabeceras[c_ORDENANTE].append(completarEspacios("Nif", bEmisor.getNif(), "I", " ", 9, false)); //identificacion del ordenante
			cabeceras[c_ORDENANTE].append(completarEspacios("Sufijo", sufijo, "D", "0", 3, true)); //sufijo
			cabeceras[c_ORDENANTE].append(sFechaEnvio); //fecha de creacion del fichero
			cabeceras[c_ORDENANTE].append(sFechaEmision); //fecha de ejecucion de ordenes (configurable en el futuro??)
			cabeceras[c_ORDENANTE].append("A"); //identificador de la cuenta del ordenante
			cabeceras[c_ORDENANTE].append(completarEspacios("IBAN", bEmisor.getIban(), "I", " ", 34, false)); //cuenta del ordenante
			cabeceras[c_ORDENANTE].append("0"); //detalle del cargo (un solo cargo por el total de operaciones)
			cabeceras[c_ORDENANTE].append(completarEspacios("Nombre", bEmisor.getNombre(), "I", " ", 70, true)); //nombre
			cabeceras[c_ORDENANTE].append(completarEspacios("Domicilio", UtilidadesString.replaceAllIgnoreCase(UtilidadesString.replaceAllIgnoreCase(bEmisor.getDomicilio(), "\n", " "), "\r", " "), "I", " ", 50, true)); //direccion
			cabeceras[c_ORDENANTE].append(completarEspacios("Poblacion", bEmisor.getCodigopostal() + "  " + bEmisor.getPoblacion(), "I", " ", 50, true)); //codigo postal y poblacion
			cabeceras[c_ORDENANTE].append(completarEspacios("Provincia", bEmisor.getProvincia(), "I", " ", 40, true)); //provincia
			cabeceras[c_ORDENANTE].append("ES"); //pais
			cabeceras[c_ORDENANTE].append(rellenarEspacios(311)); //libre
			
			// 2de7. generando cabecera de beneficiarios iban
			cabeceras[c_BENEFICIARIOSEPA] = new StringBuffer();
			cabeceras[c_BENEFICIARIOSEPA].append("02SCT"); //codigo de registro y operacion
			cabeceras[c_BENEFICIARIOSEPA].append(versionCuaderno); //version del cuaderno
			cabeceras[c_BENEFICIARIOSEPA].append(completarEspacios("Nif", bEmisor.getNif(), "I", " ", 9, false)); //identificacion del ordenante
			cabeceras[c_BENEFICIARIOSEPA].append(completarEspacios("Sufijo", sufijo, "I", " ", 3, true)); //sufijo
			cabeceras[c_BENEFICIARIOSEPA].append(rellenarEspacios(578)); //libre
			
			// 3de7. generando cabecera de otros beneficiarios
			cabeceras[c_BENEFICIARIOOTROS] = new StringBuffer();
			cabeceras[c_BENEFICIARIOOTROS].append("02OTR"); //codigo de registro y operacion
			cabeceras[c_BENEFICIARIOOTROS].append(versionCuaderno); //version del cuaderno
			cabeceras[c_BENEFICIARIOOTROS].append(completarEspacios("Nif", bEmisor.getNif(), "I", " ", 9, false)); //identificacion del ordenante
			cabeceras[c_BENEFICIARIOOTROS].append(completarEspacios("Sufijo", sufijo, "I", " ", 3, true)); //sufijo
			cabeceras[c_BENEFICIARIOOTROS].append(rellenarEspacios(578)); //libre
			
			// 4de7. generando lineas de beneficiarios
			Enumeration en = vReceptores.elements();
			while(en.hasMoreElements()){
				FicheroReceptorAbonoBean bReceptor 	= (FicheroReceptorAbonoBean)en.nextElement();
				importe = (int)Math.rint(bReceptor.getImporte().doubleValue()*100);
				
				// en funcion de si el pais del banco del destinatario esta dentro de SEPA, elegimos el bloque donde se registrara el registro
				boolean esSEPA = bReceptor.getSepa().equalsIgnoreCase(ClsConstants.DB_TRUE);
				if (esSEPA) {
					subtotalSEPA += importe;
					registrosBenefSEPA[nRegistrosBenefSEPA] = new StringBuffer();
					
					registrosBenefSEPA[nRegistrosBenefSEPA].append("03SCT"); //codigo de registro y operacion
					registrosBenefSEPA[nRegistrosBenefSEPA].append(versionCuaderno); //version del cuaderno
					registrosBenefSEPA[nRegistrosBenefSEPA].append("002"); //numero de dato
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Referencia interna", bReceptor.getReferenciaInterna(), "I", " ", 35, false)); //referencia del ordenante (identificador de la transferencia interno)
					registrosBenefSEPA[nRegistrosBenefSEPA].append("A"); //identificador de la cuenta del beneficiario: A - iban
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Iban", bReceptor.getIban(), "I", " ", 34, false)); //cuenta del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Importe", Integer.toString(importe), "D", "0", 11, false));
					registrosBenefSEPA[nRegistrosBenefSEPA].append("3"); //clave de gastos: 3 - compartidos (da igual porque luego solo importa lo q aplique el destinatario)
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Bic", bReceptor.getBic(), "I", " ", 11, false)); //bic entidad del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Nombre", bReceptor.getNombre(), "I", " ", 70, true)); //nombre del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Domicilio", UtilidadesString.replaceAllIgnoreCase(UtilidadesString.replaceAllIgnoreCase(bReceptor.getDomicilio(), "\n", " "), "\r", " "), "I", " ", 50, true)); //direccion del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Poblacion", bReceptor.getCodigopostal() + "  " + bReceptor.getPoblacion(), "I", " ", 50, true)); //codigo postal y poblacion del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Provincia", bReceptor.getProvincia(), "I", " ", 40, true)); //provincia del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Pais", bReceptor.getCodIsoPais(), "I", " ", 2, true)); //pais del beneficiario
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("NombrePago", bReceptor.getNombrePago() + "- " + bReceptor.getConcepto(), "I", " ", 140, true)); //concepto
					registrosBenefSEPA[nRegistrosBenefSEPA].append(completarEspacios("Referencia interna", bReceptor.getReferenciaInterna(), "I", " ", 35, false)); //identificacion de la instruccion (usamos lo mismo que la referencia del ordenante)
					registrosBenefSEPA[nRegistrosBenefSEPA].append("    "); //tipo de transferencia
					registrosBenefSEPA[nRegistrosBenefSEPA].append("OTHR"); //proposito de transferencia
					registrosBenefSEPA[nRegistrosBenefSEPA].append(rellenarEspacios(99)); //libre
					
					nRegistrosBenefSEPA ++;
				} else {
					subtotalOtros += importe;
					registrosBenefOtros[nRegistrosBenefOtros] = new StringBuffer();
					
					registrosBenefOtros[nRegistrosBenefOtros].append("03OTR"); //codigo de registro y operacion
					registrosBenefOtros[nRegistrosBenefOtros].append(versionCuaderno); //version del cuaderno
					registrosBenefOtros[nRegistrosBenefOtros].append("006"); //numero de dato
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Nombre", bEmisor.getNombre(), "I", " ", 35, true)); //nombre del ultimo ordenante
					registrosBenefOtros[nRegistrosBenefOtros].append("A"); //identificador de la cuenta del beneficiario: A - iban
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Iban", bReceptor.getIban(), "I", " ", 34, false)); //cuenta del beneficiario
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Importe", Integer.toString(importe), "D", "0", 11, false));
					registrosBenefOtros[nRegistrosBenefOtros].append("3"); //clave de gastos: 3 - compartidos (da igual porque luego solo importa lo q aplique el destinatario)
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Bic", bReceptor.getBic(), "I", " ", 11, false)); //bic entidad del beneficiario
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Nombre", bReceptor.getNombre(), "I", " ", 35, true)); //nombre del beneficiario
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Domicilio", UtilidadesString.replaceAllIgnoreCase(UtilidadesString.replaceAllIgnoreCase(
							bReceptor.getDomicilio(), "\n", " "), "\r", " ") + "  " + 
							bReceptor.getCodigopostal() + "  " + 
							bReceptor.getPoblacion() + "  " + 
							bReceptor.getProvincia() + "  " + 
							bReceptor.getPais(), "I", " ", 105, true)); //direccion completa y pais del beneficiario
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("NombrePago", bReceptor.getNombrePago() + "- " + bReceptor.getConcepto(), "I", " ", 72, true)); //concepto
					registrosBenefOtros[nRegistrosBenefOtros].append(completarEspacios("Numero abono", bReceptor.getNumeroAbono(), "I", " ", 13, false)); //referencia de la transferencia para el beneficiario
					registrosBenefOtros[nRegistrosBenefOtros].append("3"); //proposito de transferencia
					registrosBenefOtros[nRegistrosBenefOtros].append(rellenarEspacios(268)); //libre
					
					nRegistrosBenefOtros ++;
				}
				
			} //while receptores
		
			// 5de7. generando subtotal de beneficiarios iban
			totales[c_BENEFICIARIOSEPA] = new StringBuffer();
			totales[c_BENEFICIARIOSEPA].append("04SCT"); //codigo de registro y operacion
			totales[c_BENEFICIARIOSEPA].append(completarEspacios("Importe", Integer.toString(subtotalSEPA), "D", "0", 17, false));
			totales[c_BENEFICIARIOSEPA].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefSEPA), "D", "0", 8, false)); //registros individuales
			totales[c_BENEFICIARIOSEPA].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefSEPA+2), "D", "0", 10, false)); //registros individuales + cabecera + total
			totales[c_BENEFICIARIOSEPA].append(rellenarEspacios(560)); //libre
			
			// 6de7. generando subtotal de otros beneficiarios
			totales[c_BENEFICIARIOOTROS] = new StringBuffer();
			totales[c_BENEFICIARIOOTROS].append("04OTR"); //codigo de registro y operacion
			totales[c_BENEFICIARIOOTROS].append(completarEspacios("Importe", Integer.toString(subtotalOtros), "D", "0", 17, false));
			totales[c_BENEFICIARIOOTROS].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefOtros), "D", "0", 8, false)); //registros individuales
			totales[c_BENEFICIARIOOTROS].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefOtros+2), "D", "0", 10, false)); //registros individuales + cabecera + total
			totales[c_BENEFICIARIOOTROS].append(rellenarEspacios(560)); //libre
			
			// 7de7. generando total
			totales[c_ORDENANTE] = new StringBuffer();
			totales[c_ORDENANTE].append("99ORD"); //codigo de registro y operacion
			totales[c_ORDENANTE].append(completarEspacios("Importe", Integer.toString(subtotalSEPA + subtotalOtros), "D", "0", 17, false));
			totales[c_ORDENANTE].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefSEPA + nRegistrosBenefOtros), "D", "0", 8, false)); //registros individuales
			if (nRegistrosBenefSEPA > 0 && nRegistrosBenefOtros > 0)
				totales[c_ORDENANTE].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefSEPA+2 + nRegistrosBenefOtros+2 +2), "D", "0", 10, false)); //registros individuales + 2*subcabecera + 2*subtotal (si existen ambos bloques) + cabecera + total
			else
				totales[c_ORDENANTE].append(completarEspacios("Numero", Integer.toString(nRegistrosBenefSEPA + nRegistrosBenefOtros +2 +2), "D", "0", 10, false)); //registros individuales + subcabecera + subtotal (si solo existe un bloque) + cabecera + total
			totales[c_ORDENANTE].append(rellenarEspacios(560)); //libre
		
			
			// escribiendo todas las lineas en el fichero
			String[] lineas;
			if (nRegistrosBenefSEPA > 0 && nRegistrosBenefOtros > 0)
				lineas = new String[c_NBLOQUES*2 + nRegistrosBenefSEPA + nRegistrosBenefOtros];
			else
				lineas = new String[c_NBLOQUES*2 -2 + nRegistrosBenefSEPA + nRegistrosBenefOtros];
			
			int j=0; lineas[j] = cabeceras[c_ORDENANTE].toString();
			
			if (nRegistrosBenefSEPA > 0) {
				j++; lineas[j] = cabeceras[c_BENEFICIARIOSEPA].toString();
				for (int i=0; i<nRegistrosBenefSEPA; i++) {
					j++; lineas[j] = registrosBenefSEPA[i].toString();
				}
				j++; lineas[j] = totales[c_BENEFICIARIOSEPA].toString();
			}
			
			if (nRegistrosBenefOtros > 0) {
				j++; lineas[j] = cabeceras[c_BENEFICIARIOOTROS].toString();
				for (int i=0; i<nRegistrosBenefOtros; i++) {
					j++; lineas[j] = registrosBenefOtros[i].toString();
				}
				j++; lineas[j] = totales[c_BENEFICIARIOOTROS].toString();
			}
			
			j++; lineas[j] = totales[c_ORDENANTE].toString();
			
			resul = escribirFichero(lineas,rutaServidor,nombreFichero);	
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		if (resul){
			return nRegistrosBenefSEPA+2 + nRegistrosBenefOtros+2 +2;
		}else{
			return 0;
		}
	} //crearFicheroSEPA()
	
	/* ANTIGUO PRE-SEPA
	protected String generarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String lenguaje = usr.getLanguage();
		
		String resultado="";
		Long idDisqueteAbono=new Long(0);
		boolean correcto=true;
		UserTransaction tx = null;
		Hashtable bancoMenorComision = new Hashtable();
		Hashtable banco = new Hashtable();
		Vector resultados=new Vector();
		Vector abonosAjenos=new Vector();
		Vector bancosRestantes=new Vector();
		Vector bancos=new Vector();
		Vector abonosBanco=new Vector();
		boolean ficheroSJCS = false;
		int cont = 0;
		
		try {		 				
			
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 	= new ReadProperties ("SIGA.properties");
			String extensionFichero = p.returnProperty("facturacion.extension.ficherosAbonos");
			String prefijoFichero = p.returnProperty("facturacion.prefijo.ficherosAbonos");
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
			FicheroBancarioAbonosForm miForm = (FicheroBancarioAbonosForm)formulario;
			
			// Recuperamos la procedencia de la llamada (1-Facturacion SJCS, 0-Facturacion)
			String fcs = miForm.getSjcs();
			if (fcs.equals("1")){
				ficheroSJCS = true;
			}
			
			// Manejadores para los accesos a BBDD
			FacAbonoAdm adminAbono=new FacAbonoAdm(this.getUserBean(request));
			FacBancoInstitucionAdm adminBancoInst=new FacBancoInstitucionAdm(this.getUserBean(request));
			CenInstitucionAdm admInstitucion=new CenInstitucionAdm(this.getUserBean(request));
			CenPersonaAdm admPersona=new CenPersonaAdm(this.getUserBean(request));
			//aalg INC_06366_SIGA
			CenPersonaAdm admPersonaEmisor=new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoAdm admColegiado=new CenColegiadoAdm(this.getUserBean(request));
			FacDisqueteAbonosAdm admDisqueteAbonos=new FacDisqueteAbonosAdm(this.getUserBean(request));
			FacAbonoIncluidoEnDisqueteAdm admAbonoDisquete=new FacAbonoIncluidoEnDisqueteAdm(this.getUserBean(request));
			CenSucursalesAdm admSucursal=new CenSucursalesAdm(this.getUserBean(request));
			
			CenDireccionesAdm admDirecciones = new CenDireccionesAdm(this.getUserBean(request));
			
			// Comienzo control de transacciones
			tx = user.getTransactionPesada();
			tx.begin();
			if (ficheroSJCS){
				// Obtenemos todos los bancos de la institucion
				bancos=adminBancoInst.obtenerBancos(idInstitucion);
				Enumeration listaBancos=bancos.elements();
				while (listaBancos.hasMoreElements()){
					banco=((Row)listaBancos.nextElement()).getRow();
					abonosBanco=adminAbono.getAbonosBancoSjcs(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
					if (!abonosBanco.isEmpty()){
						// Obtenemos el identificador
						idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
						// Los datos de la sucursal
						Hashtable sucursal = admSucursal.getSucursal((String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO),(String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
						// Datos emisor
						FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
						emisor.setIdentificador(new Integer((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						emisor.setCodigoBanco((String)banco.get(FacBancoInstitucionBean.C_COD_BANCO)); // JBD cambiado bancos_codigo por cod_banco
						emisor.setCodigoSucursal((String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
						emisor.setNumeroCuenta((String)banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
						//emisor.setNif((String)banco.get(FacBancoInstitucionBean.C_NIF));
						String nombre=admInstitucion.getNombreInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION));
						emisor.setNombre(nombre);
						emisor.setIdentificadorDisquete(idDisqueteAbono);
						// JBD cambiada direccion de sucursal por colegio
						// emisor.setDomicilio((String)sucursal.get(CenSucursalesBean.C_DOMICILIO));
						// emisor.setPlaza((String)sucursal.get("PLAZA"));
						emisor.setDomicilio((String)admInstitucion.getDomicilioInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						emisor.setPlaza((String)admInstitucion.getPoblacionInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						// Datos receptores
						Enumeration listaReceptores=abonosBanco.elements();
						Vector receptores=new Vector();
						while (listaReceptores.hasMoreElements()){
							Hashtable temporal=new Hashtable();
							temporal=((Row)listaReceptores.nextElement()).getRow();
							FicheroReceptorAbonoBean receptor=new FicheroReceptorAbonoBean();
							receptor.setIdentificador(new Long((String)temporal.get(FacAbonoBean.C_IDPERSONA)));
							receptor.setCodigoBanco((String)temporal.get(CenCuentasBancariasBean.C_CBO_CODIGO));
							receptor.setCodigoSucursal((String)temporal.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
							receptor.setNumeroCuenta((String)temporal.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
							receptor.setDigitosControl((String)temporal.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
							String nif=admPersona.obtenerNIF((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setConcepto((String)temporal.get(FcsPagosJGBean.C_CONCEPTO));
							receptor.setDni(nif);
							receptor.setImporte(new Double((String)temporal.get("IMPORTE")));							
							nombre=admPersona.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setNombre(nombre);
							// jbd 09/12/2008 INC_05507_SIGA >>>
							// receptor.setNombrePago((String)temporal.get(FacAbonoBean.C_MOTIVOS));
							String idPersona = (String)temporal.get(FacAbonoBean.C_IDPERSONA);
							Vector direccionDespacho = null;
							direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
							if (direccionDespacho!=null && direccionDespacho.size()>0) {
								receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
								receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
							}
							//aalg INC_06366_SIGA
							String sacarLetrado="0";
							String nombrePago;
							GenParametrosAdm paramAdm = new GenParametrosAdm(this
									.getUserBean(request));
							try {
								sacarLetrado = paramAdm.getValor(idInstitucion, "FCS",
										"INCLUIR_LETRADO_CONCEPTO_BANCO", "0");
							} catch (Exception e) {
							}
							if (sacarLetrado.equals("0"))
								nombrePago = (String)temporal.get("NOMBREPAGO");
							else{
								CenColegiadoBean beanColegiado =  admColegiado.getDatosColegiales(Long.valueOf((String)temporal.get(FacAbonoBean.C_IDPERORIGEN)), Integer.valueOf(idInstitucion));
								nombrePago = (String)temporal.get("NOMBREPAGO") + 
									'-' + ((beanColegiado.getComunitario().equals("0")) ? beanColegiado.getNColegiado() : beanColegiado.getNComunitario()) +
									'-' + admPersonaEmisor.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERORIGEN));
							}
							receptor.setNombrePago(nombrePago);
							// <<<
							receptores.addElement(receptor);						
						}
						// Creacion de un fichero de abonos por cada banco restante
						int nlineas = this.crearFichero(emisor, receptores);
						cont ++;
						if (nlineas>0){
							// Creacion entrada FAC_DISQUETEABONO
							Hashtable disqueteAbono=new Hashtable();
							
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
							disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
							disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
							disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString()+"."+extensionFichero);
							disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,"1");
							disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
							correcto=admDisqueteAbonos.insert(disqueteAbono);						
							if (correcto){
								// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
								listaReceptores=abonosBanco.elements();
								while ((correcto)&&(listaReceptores.hasMoreElements())){
									Hashtable temporal=new Hashtable();
									temporal=((Row)listaReceptores.nextElement()).getRow();
									Hashtable abonoDisquete=new Hashtable();
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
									//double importeAbonado = new Double((String)abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO)).doubleValue();
									double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
									correcto=admAbonoDisquete.insert(abonoDisquete);
									if (correcto) {
										// RGG 29/05/2009 Cambio de funciones de abono
									    // Obtengo el abono insertado
									    Hashtable htA = new Hashtable();
										htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
										htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
										Vector vAbono = adminAbono.selectByPK(htA);
										FacAbonoBean bAbono = null;
										if (vAbono!=null && vAbono.size()>0) {
										    bAbono = (FacAbonoBean) vAbono.get(0);
										}
										bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
									    bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
									    bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
									    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
									        // pagado
									        bAbono.setEstado(new Integer(1));
									    } else {
									        if (bAbono.getIdCuenta()!=null) {
									            // pendiente pago banco
										        bAbono.setEstado(new Integer(5));
									        } else {
									            // pendiente pago caja
										        bAbono.setEstado(new Integer(6));
									        }
									    }
									    
									   if (!adminAbono.update(bAbono)){
										   throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
										}

									} else {
									    new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
									}									
									
								}							
							}		
						}
					}
				}
			}else{ // Fichero no sjcs
				resultados=adminBancoInst.getBancoMenorComision(idInstitucion);
				if (!resultados.isEmpty()){
					bancoMenorComision=((Row)resultados.firstElement()).getRow();
					abonosAjenos=adminAbono.getAbonosBancosMenorComision(idInstitucion,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
					if (!abonosAjenos.isEmpty()){
						// Obtenemos el identificador
						idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
						// Los datos de la sucursal
						Hashtable sucursal = admSucursal.getSucursal((String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO),(String)bancoMenorComision.get(FacBancoInstitucionBean.C_COD_SUCURSAL)); 
						// Datos emisor
						FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
						emisor.setIdentificador(new Integer((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						emisor.setCodigoBanco((String)bancoMenorComision.get(FacBancoInstitucionBean.C_COD_BANCO)); // JBD cambiado bancos_codigo por cod_banco
						emisor.setCodigoSucursal((String)bancoMenorComision.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
						emisor.setNumeroCuenta((String)bancoMenorComision.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
						//emisor.setNif((String)bancoMenorComision.get(FacBancoInstitucionBean.C_NIF));
						String nombre=admInstitucion.getNombreInstitucion((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION));
						emisor.setNombre(nombre);
						emisor.setIdentificadorDisquete(idDisqueteAbono);
						//emisor.setDomicilio((String)sucursal.get(CenSucursalesBean.C_DOMICILIO));
						// RGG 09/07/2008 Se pone la direccion del colegio
						emisor.setDomicilio(admInstitucion.getDomicilioInstitucion((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						// RGG 09/07/2008 Se deja la plaza de la direccion de la sucursal.
						// emisor.setPlaza((String)sucursal.get("PLAZA"));
						emisor.setPlaza(admInstitucion.getPoblacionInstitucion((String)bancoMenorComision.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
						// Datos receptores
						Enumeration listaReceptores=abonosAjenos.elements();
						Vector receptores=new Vector();
						while (listaReceptores.hasMoreElements()){
							Hashtable temporal=new Hashtable();
							temporal=((Row)listaReceptores.nextElement()).getRow();
							FicheroReceptorAbonoBean receptor=new FicheroReceptorAbonoBean();
							receptor.setIdentificador(new Long((String)temporal.get(FacAbonoBean.C_IDPERSONA)));
							receptor.setCodigoBanco((String)temporal.get(CenCuentasBancariasBean.C_CBO_CODIGO));
							receptor.setCodigoSucursal((String)temporal.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
							receptor.setNumeroCuenta((String)temporal.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
							receptor.setDigitosControl((String)temporal.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
							String nif=admPersona.obtenerNIF((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setConcepto("9");
							receptor.setDni(nif);
							receptor.setImporte(new Double((String)temporal.get("IMPORTE")));
							nombre=admPersona.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERSONA));
							receptor.setNombre(nombre);
							// jbd 09/12/2008 INC_05507_SIGA >>>
							receptor.setNombrePago((String)temporal.get(FacAbonoBean.C_MOTIVOS));
							String idPersona = (String)temporal.get(FacAbonoBean.C_IDPERSONA);
							Vector direccionDespacho = null;
							direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
							if (direccionDespacho!=null && direccionDespacho.size()>0) {
								receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
								receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
							}
							// <<<
							receptores.addElement(receptor);	
							
						}
						
						// Creacion del fichero de abonos para el banco de menores comisiones ajenas
						int nlineas = this.crearFichero(emisor, receptores);
						cont ++;
						if (nlineas>0){
							// Creacion entrada FAC_DISQUETEABONO
							Hashtable disqueteAbono=new Hashtable();
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
							disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
							disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
							disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
							disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString()+"."+extensionFichero);
							disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,"0");
							disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
							correcto=admDisqueteAbonos.insert(disqueteAbono);						
							if (correcto){
								// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
								listaReceptores=abonosAjenos.elements();
								while ((correcto)&&(listaReceptores.hasMoreElements())){
									Hashtable temporal=new Hashtable();
									temporal=((Row)listaReceptores.nextElement()).getRow();
									Hashtable abonoDisquete=new Hashtable();
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
									//double importeAbonado = ((Double)abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO)).doubleValue();
									double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
									abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
									correcto=admAbonoDisquete.insert(abonoDisquete);
									if (correcto) {
										// RGG 29/05/2009 Cambio de funciones de abono
									    // Obtengo el abono insertado
									    Hashtable htA = new Hashtable();
										htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
										htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
										Vector vAbono = adminAbono.selectByPK(htA);
										FacAbonoBean bAbono = null;
										if (vAbono!=null && vAbono.size()>0) {
										    bAbono = (FacAbonoBean) vAbono.get(0);
										}
										bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
									    bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
									    bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
									    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
									        // pagado
									        bAbono.setEstado(new Integer(1));
									    } else {
									        if (bAbono.getIdCuenta()!=null) {
									            // pendiente pago banco
										        bAbono.setEstado(new Integer(5));
									        } else {
									            // pendiente pago caja
										        bAbono.setEstado(new Integer(6));
									        }
									    }
									    if (!adminAbono.update(bAbono)){
										    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
										}

									} else {
									    new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
									}									
								}							
							}
							
						}
					}
				}
				
				//Ahora los abonos para los bancos restantes 
				bancosRestantes=adminBancoInst.getRestoBancosConComision(idInstitucion,(String)bancoMenorComision.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
				if (!bancosRestantes.isEmpty()){
					Enumeration listaBancos=bancosRestantes.elements();
					while (listaBancos.hasMoreElements()){
						banco=((Row)listaBancos.nextElement()).getRow();
						abonosBanco=adminAbono.getAbonosBanco(idInstitucion,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
						if (!abonosBanco.isEmpty()){
							// Obtenemos el identificador
							idDisqueteAbono=admDisqueteAbonos.getNuevoID(idInstitucion);
							// Los datos de la sucursal
							Hashtable sucursal = admSucursal.getSucursal((String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO),(String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
							// Datos emisor
							FicheroEmisorAbonoBean emisor=new FicheroEmisorAbonoBean();
							emisor.setIdentificador(new Integer((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
							emisor.setCodigoBanco((String)banco.get(FacBancoInstitucionBean.C_COD_BANCO)); // JBD cambiado bancos_codigo por cod_banco
							emisor.setCodigoSucursal((String)banco.get(FacBancoInstitucionBean.C_COD_SUCURSAL));
							emisor.setNumeroCuenta((String)banco.get(FacBancoInstitucionBean.C_NUMEROCUENTA));
							//emisor.setNif((String)banco.get(FacBancoInstitucionBean.C_NIF));
							String nombre=admInstitucion.getNombreInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION));
							emisor.setNombre(nombre);
							emisor.setIdentificadorDisquete(idDisqueteAbono);
							// JBD cambiada direccion de sucursal por colegio
							//emisor.setDomicilio((String)sucursal.get(CenSucursalesBean.C_DOMICILIO));
							//emisor.setPlaza((String)sucursal.get("PLAZA"));
							emisor.setDomicilio((String)admInstitucion.getDomicilioInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
							emisor.setPlaza((String)admInstitucion.getPoblacionInstitucion((String)banco.get(FacBancoInstitucionBean.C_IDINSTITUCION)));
							
							// Datos receptores
							Enumeration listaReceptores=abonosBanco.elements();
							Vector receptores=new Vector();
							while (listaReceptores.hasMoreElements()){
								Hashtable temporal=new Hashtable();
								temporal=((Row)listaReceptores.nextElement()).getRow();
								FicheroReceptorAbonoBean receptor=new FicheroReceptorAbonoBean();
								receptor.setIdentificador(new Long((String)temporal.get(FacAbonoBean.C_IDPERSONA)));
								receptor.setCodigoBanco((String)temporal.get(CenCuentasBancariasBean.C_CBO_CODIGO));
								receptor.setCodigoSucursal((String)temporal.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
								receptor.setNumeroCuenta((String)temporal.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
								receptor.setDigitosControl((String)temporal.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
								String nif=admPersona.obtenerNIF((String)temporal.get(FacAbonoBean.C_IDPERSONA));
								receptor.setConcepto("9");
								receptor.setDni(nif);
								receptor.setImporte(new Double((String)temporal.get("IMPORTE")));
								nombre=admPersona.obtenerNombreApellidos((String)temporal.get(FacAbonoBean.C_IDPERSONA));
								receptor.setNombre(nombre);
								// jbd 09/12/2008 INC_05507_SIGA >>>
								// receptor.setNombrePago((String)temporal.get(FacAbonoBean.C_MOTIVOS));
								String idPersona = (String)temporal.get(FacAbonoBean.C_IDPERSONA);
								Vector direccionDespacho = null;
								direccionDespacho = admDirecciones.getDireccionDespacho(idPersona, idInstitucion, lenguaje);
								if (direccionDespacho!=null && direccionDespacho.size()>0) {
									receptor.setDomicilio((String)((Hashtable)direccionDespacho.get(0)).get("DOMICILIO_DESPACHO"));
									receptor.setPoblacion((String)((Hashtable)direccionDespacho.get(0)).get("POBLACION_DESPACHO"));
								}
								receptor.setNombrePago((String)temporal.get("NOMBREPAGO"));
								// <<<
								receptores.addElement(receptor);						
							}
							// Creacion de un fichero de abonos por cada banco restante
							int nlineas = this.crearFichero(emisor, receptores);
							cont ++;
							if (nlineas>0){
								// Creacion entrada FAC_DISQUETEABONO
								Hashtable disqueteAbono=new Hashtable();
								
								disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION,idInstitucion);
								disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
								disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA,"SYSDATE");
								disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO,(String)banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
								disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO,prefijoFichero+idDisqueteAbono.toString()+"."+extensionFichero);
								disqueteAbono.put(FacDisqueteAbonosBean.C_FCS,fcs);
								disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(nlineas));
								correcto=admDisqueteAbonos.insert(disqueteAbono);						
								if (correcto){
									// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
									listaReceptores=abonosBanco.elements();
									while ((correcto)&&(listaReceptores.hasMoreElements())){
										Hashtable temporal=new Hashtable();
										temporal=((Row)listaReceptores.nextElement()).getRow();
										Hashtable abonoDisquete=new Hashtable();
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION,idInstitucion);
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO,idDisqueteAbono.toString());
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO,admAbonoDisquete.getImporteAbonado(idInstitucion,(String)temporal.get(FacAbonoBean.C_IDABONO)));
										// double importeAbonado = new Double((String)abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO)).doubleValue();
										double importeAbonado = Double.parseDouble(abonoDisquete.get(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO).toString());
										abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO,"N");
										correcto=admAbonoDisquete.insert(abonoDisquete);
										if (correcto) {
											// RGG 29/05/2009 Cambio de funciones de abono
										    // Obtengo el abono insertado
										    Hashtable htA = new Hashtable();
											htA.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
											htA.put(FacAbonoBean.C_IDABONO,(String)temporal.get(FacAbonoBean.C_IDABONO));
											Vector vAbono = adminAbono.selectByPK(htA);
											FacAbonoBean bAbono = null;
											if (vAbono!=null && vAbono.size()>0) {
											    bAbono = (FacAbonoBean) vAbono.get(0);
											}
											bAbono.setImpPendientePorAbonar(new Double(bAbono.getImpPendientePorAbonar().doubleValue()-importeAbonado));
										    bAbono.setImpTotalAbonado(new Double(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado));
										    bAbono.setImpTotalAbonadoPorBanco(new Double(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado));
										    if (bAbono.getImpPendientePorAbonar().doubleValue()<=0) {
										        // pagado
										        bAbono.setEstado(new Integer(1));
										    } else {
										        if (bAbono.getIdCuenta()!=null) {
										            // pendiente pago banco
											        bAbono.setEstado(new Integer(5));
										        } else {
										            // pendiente pago caja
											        bAbono.setEstado(new Integer(6));
										        }
										    }
										    
										   
										     if (!adminAbono.update(bAbono)){
											    throw new ClsExceptions("Error al actualizar estado e importes del abono: "+adminAbono.getError());
											  }
										   

										} else {
										    new ClsExceptions("Error al insertar el abono en el disquete: "+admAbonoDisquete.getError());
										}
									}							
								}		
							}
						}
					}
				}
			} // FI sjcs
//			
			if (correcto){
			    
			    
				tx.commit();
				
				String mensaje = "facturacion.ficheroBancarioAbonos.mensaje.generacionDisquetesOK";
				String[] datos = {String.valueOf(cont)};
				
				mensaje = UtilidadesString.getMensaje(mensaje, datos, lenguaje);				
				request.setAttribute("mensaje",mensaje);	
				
				resultado = "exitoConString";				
			}	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (resultado);		
	}	
	*/
	
	/** 
	 *  Funcion para alinear y ajustar el texto segun los paramentros del fichero.
	 * @param  cadena 			- cadena que queremos ajustar.
	 * @param  alineacion		- alineación de la cadena 'D': derecha, 'I': izquierda.
	 * @param  comodin 			- comodin con el que queremos rellenar, (espacio en blanco o ceros).
	 * @param  longitudMaxima 	- longitud que debe contener la cadena, para incluirlo en el fichero
	 * @param  suprimir 		- true: permite cortar la cadena en el caso de superar el tamanho máximo.
	 * @return  String 			- devuelve la cadena con el formato adecuado.  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private String completarEspacios(String campo, String cadena, String alineacion, String comodin, int longitudMaxima, boolean suprimir) throws SIGAException{
		String auxCadena="";
		try{
		    if (cadena!=null) {
		    	// Pasamos el texto a mayusculas, quitamos acentos y caracteres raros
		    	// Usamos el mismo metodo que en el modelo 190
		    	cadena = cadena.toUpperCase();
		    	cadena = UtilidadesString.quitarAcentos(cadena);
		    	cadena = UtilidadesString.validarModelo34(cadena);
				int longitudCadena = cadena.length();
				// La cadena excede el tamanho máximo del que le corresponde en el fichero.
				if (longitudCadena > longitudMaxima){
					if (suprimir){			// Cortamos la cadena al tamanho máximo (ej, nombre) 	
						auxCadena = cadena.substring(0,longitudMaxima);
					}
					else{					// No podemos cortar la cadena (ej. DNI) pero excede del tamanho. Error
						throw new Exception("Error el dato " + campo + ": " + cadena + " excede el tamanho  del permitido por el registro.");
						
					}
				}	
				else{						// Tenemos que completar la cadena
					String sRelleno = "";
					for(int i=0; i<(longitudMaxima - longitudCadena); i++ ){
						sRelleno += comodin;
					}	
					if (alineacion.equalsIgnoreCase("D")){	// Alineación a la derecha				
						auxCadena = sRelleno + cadena;
					}
					else{					// Alineación a la izquierda
						auxCadena = cadena + sRelleno;
					}
					
				}
		    }
		}catch (Exception e) {		
			throwExcp("menssages.general.error",e,null); 	  	
		}
		
		return auxCadena;
	}
	
	/** 
	 *  Funcion que devuelve tantos espacios en blanco como el numero pasado como paramentro.
	 * @param  longitudMaxima - numero de espacios que queremos generar.
	 * @return  String - cadena con en numero de espacios. 	
	 */
	private String rellenarEspacios(int longitudMaxima){
		String auxCadena="";
		for(int i=0; i<longitudMaxima; i++ ){
			auxCadena += " ";
		}		
		return auxCadena;
	}
	
	/** 
	 *  Funcion que escribe en el fichero.
	 * @param  lineas[] - lineas que queremos escribir en el fichero.
	 * @param  fileName	- ruta del fichero donde vamos a escribir.
	 * @param  nomFich 	- nombre del fichero que vamos a crear.
	 * @return  boolean - devuelve true en el caso de que se haya generado correctamente el fichero  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	private boolean escribirFichero(String[] lineas, String fileName, String nomFich) throws SIGAException{
		boolean result = false;
		File ficLog;		
		try{
			
			ficLog = new File(fileName);
			if (!ficLog.exists()){
				ficLog.mkdirs();			     	       
			}
			ficLog = new File(fileName + nomFich);
			if (ficLog.exists()){
				ficLog.delete();
				ficLog.createNewFile();
			}
			
			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(ficLog, true)));
			for (int i=0; i<lineas.length; i++){
				printer.print(lineas[i]+"\r\n");
			}			
			printer.flush();
			printer.close();	
			result = true;
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}		
		return result;
	}
	
	
	/** 
	 *  Funcion para obtener el digito de control de la cuenta bancaria.
	 * @param  String - cadena con la que calcularemos el dígito de control.
	 * @return  String - digito de control  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	public String obtenerDigitoControl(String cadena) throws SIGAException{
		int[] valores = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
		int  control = 0;
		try{			
			for (int i=0; i<=9; i++){
				control += Integer.parseInt(String.valueOf(cadena.charAt(i))) * valores[i];	
			}			   	  
			control = 11 - (control % 11);
			if (control == 11) control = 0;
			else if (control == 10) control = 1;			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		return String.valueOf(control);
	}
	
	protected String informeRemesa (MasterForm formulario, HttpServletRequest request) throws SIGAException 
	{
		FicheroBancarioAbonosForm form = (FicheroBancarioAbonosForm)formulario;

		Vector ocultos 			= (Vector)form.getDatosTablaOcultos(0);			
		String idDisqueteCargo 	= (String)ocultos.elementAt(0);	
		String idInstitucion	= this.getIDInstitucion(request).toString();			

		FacDisqueteAbonosAdm adm = new FacDisqueteAbonosAdm(this.getUserBean(request));
		Hashtable h = adm.getInformeRemesa(idInstitucion, idDisqueteCargo);
		if (h != null) {
			
			request.setAttribute("datosImpreso", h);
			request.setAttribute("abonos", "1");
			
//			request.setAttribute("importeTotal",         UtilidadesHash.getString(h,"importeTotal"));
//			request.setAttribute("numOrdenes",           UtilidadesHash.getString(h,"numOrdenes"));
//			request.setAttribute("numRegistros",         UtilidadesHash.getString(h,"numRegistros"));
//			request.setAttribute("fechaCreacionFichero", UtilidadesHash.getString(h,"fechaCreacionFichero"));
//			request.setAttribute("fechaEmisionOrdenes",  UtilidadesHash.getString(h,"fechaEmisionOrdenes"));
//			request.setAttribute("nombreInstitucion",    UtilidadesHash.getString(h,"nombreInstitucion"));
//			request.setAttribute("codigoOrdenante",      UtilidadesHash.getString(h,"codigoOrdenante"));
//			request.setAttribute("cuentaAbono",          UtilidadesHash.getString(h,"cuentaAbono"));
		}
		
		return "informeRemesa"; 
	}
	
}





