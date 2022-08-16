package com.siga.facturacion.action;


import java.io.File;
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

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacAbonoIncluidoEnDisqueteAdm;
import com.siga.beans.FacAbonoIncluidoEnDisqueteBean;
import com.siga.beans.FacBancoInstitucionBean;
import com.siga.beans.FacDisqueteAbonosAdm;
import com.siga.beans.FacDisqueteAbonosBean;
import com.siga.beans.FacPropositosAdm;
import com.siga.beans.FacPropositosBean;
import com.siga.beans.FacSerieFacturacionBancoAdm;
import com.siga.beans.FacSufijoBean;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.form.FicheroBancarioAbonosForm;
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
			}else if (accion.equalsIgnoreCase("configurarFichero")){
				mapDestino = configurarFichero(mapping, miForm, request, response);
			}else {
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
		
		String directorioFisico = "facturacion.directorioFisicoAbonosBancosJava";
		String directorio 		= "facturacion.directorioAbonosBancosJava";
		String nombreFichero 	= "";
		String pathFichero		= "";
		String idInstitucion	= "";
		String barra = "";
		
		try{		

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
	protected String generarFichero(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Variables Generales
		UsrBean user = this.getUserBean(request);
		String lenguaje = user.getLanguage();
		String idInstitucion = user.getLocation();
		UserTransaction tx = null;

		// Variables de trabajo
		String fcs;
		Integer idPropositoSEPA, idPropositoOtros;
		
		// Listas de trabajo
		Vector<Row> bancos;
		Enumeration<Row> listaBancos;
		Hashtable banco;
		Vector<Row> abonosBanco;

		// Variables de resultados
		String resultado = "";
		boolean correcto;
		int nFicherosGenerados;

		try {

			// Manejadores para los accesos a BBDD
			FacAbonoAdm adminAbono = new FacAbonoAdm(user);
			FacSerieFacturacionBancoAdm bancoSufInst = new FacSerieFacturacionBancoAdm(user);

			// recuperando la procedencia de la llamada (1-Facturacion SJCS, 0-Facturacion)
			FicheroBancarioAbonosForm miForm = (FicheroBancarioAbonosForm) formulario;
			fcs = miForm.getSjcs();
			fcs = ("".equals(fcs)) ? "0" : fcs;

			if (fcs.equals("1")) {
				// obteniendo los bancos-sufijo de los abonos SJCS pendientes
				bancos = adminAbono.getBancosSufijosSJCS(idInstitucion);
			} else {
				// obteniendo los bancos-sufijo de las diferentes series
				bancos = bancoSufInst.getBancosSufijosSeries(Integer.parseInt(idInstitucion));
			}

			//TODO Esta transaccion comienza demasiado pronto: deberia comenzar cuando se graba el disquete
			tx = user.getTransactionPesada();
			tx.begin();

			correcto = true; //el proceso sera correcto a menos que una de las generaciones de error
			nFicherosGenerados = 0;
			
			// Se trata cada uno de los bancos
			listaBancos = bancos.elements();
			while (listaBancos.hasMoreElements()) {
				banco = listaBancos.nextElement().getRow();

				String banco_codigo = (String) banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO);
				String banco_sufijo = (String) banco.get(FacSufijoBean.C_IDSUFIJO);

				// Si es de sjcs el sufijo está relacionado con el abono al configurar el abono
				if (fcs.equals("1")) {

					// Si el banco tiene algún abono sin sufijo no se genera el fichero hasta que todas tengan un sufijo asignado
					if ("".equals(banco_sufijo)) {
						tx.rollback();
						throw new SIGAException(UtilidadesString.getMensajeIdioma(user,
												"facturacion.ficheroBancarioTransferencias.errorSufijosPagosJG.mensajeCondicionesIncumplidas")
										+ " " + (String) banco.get(FacBancoInstitucionBean.C_IBAN));
					}
					abonosBanco = adminAbono.getAbonosBancoSjcs(idInstitucion, banco_codigo, Integer.parseInt(banco_sufijo));
					
					// obteniendo los propositos directamente del pago
					idPropositoSEPA = Integer.parseInt((String) banco.get(FcsPagosJGBean.C_IDPROPSEPA));
					idPropositoOtros = Integer.parseInt((String) banco.get(FcsPagosJGBean.C_IDPROPOTROS));

					// Sino es de sjcs hay que buscar los abonos pendientes a través de las facturas de la serie
					// relacionada con el banco y el sufijo que estamos tratando
				} else {

					// Si la serie no tiene sufijo no se genera el fichero hasta que todas tengan un sufijo asignado
					if ("".equals(banco_sufijo)) {
						tx.rollback();
						throw new SIGAException(UtilidadesString.getMensajeIdioma(user,
												"facturacion.ficheroBancarioTransferencias.errorSufijosSerie.mensajeCondicionesIncumplidas")
										+ " " + (String) banco.get(FacBancoInstitucionBean.C_IBAN));
					}
					abonosBanco = adminAbono.getAbonosBanco(Integer.parseInt(idInstitucion), banco_codigo, Integer.parseInt(banco_sufijo));

					// Se informan los propósitos introducidos en la ventana para generar el fichero (en el caso de
					// SJCS los propósitos se informan al configurar el pago)
					idPropositoSEPA = miForm.getIdpropSEPA();
					idPropositoOtros = miForm.getIdpropOtros();
				}

				// SE GENERA EL FICHERO CON LOS ABONOS OBTENIDOS
				if (!abonosBanco.isEmpty()) {
					String sufijo = (String) banco.get(FacSufijoBean.C_SUFIJO);

					// Sufijo con tres espacios como código
					if ("".equals(sufijo))
						sufijo = "   ";

					//int resultadoGeneracionFicheros = prepararFichero(user, banco, abonosBanco, sufijo, fcs);
					int resultadoGeneracionFicheros = prepararFichero(fcs, banco, sufijo, abonosBanco, idPropositoSEPA, idPropositoOtros, user);
					if (resultadoGeneracionFicheros < 0) {
						// En cuanto falle un fichero, hay que dar error
						correcto = false;
					} else {
						nFicherosGenerados += resultadoGeneracionFicheros;
					}
				}

			} //while

			if (correcto) {
				if (nFicherosGenerados > 0)
					tx.commit();

				String[] datos = { String.valueOf(nFicherosGenerados) };
				String mensaje = UtilidadesString.getMensaje("facturacion.ficheroBancarioAbonos.mensaje.generacionDisquetesOK", datos, lenguaje);

				if (fcs.equals("1")) {
					request.setAttribute("mensaje", mensaje);
					resultado = "exitoConString";

				} else {
					String resultadoFinal[] = new String[1];
					resultadoFinal[0] = mensaje;
					request.setAttribute("parametrosArray", resultadoFinal);
					request.setAttribute("modal", "");
					resultado = "exitoParametros";
				}

			} else {
				tx.rollback();
			}

		} catch (Exception e) {

			throwExcp("messages.general.error", new String[] { "modulo.facturacion" }, e, tx);
		}
		return (resultado);
	} // generarFichero()
	
	/**
	 * Inserta en BD y genera el fichero en disco
	 * @return -1: Si la insercion en BD da error
	 *          0: Si no hay abonos para este disquete
	 *          1: Si todo fue bien
	 */
	private int prepararFichero(String fcs,
			Hashtable banco,
			String sufijo,
			Vector abonosBanco,
			Integer idPropositoSEPA,
			Integer idPropositoOtros,
			UsrBean user) throws Exception
	{

		// Controles
		FacDisqueteAbonosAdm admDisqueteAbonos = new FacDisqueteAbonosAdm(user);
		FacAbonoIncluidoEnDisqueteAdm admAbonoDisquete = new FacAbonoIncluidoEnDisqueteAdm(user);
		FacAbonoAdm adminAbono = new FacAbonoAdm(user);
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);

		// Variables generales
		String idInstitucion = user.getLocation();
		String lenguaje = user.getLanguage();
		Long idDisqueteAbono; // id del nuevo disquete a generar

		// Variables de control
		int numeroAbonosIncluidosEnDisquete = 0;

		// Control basico de entrada
		if (abonosBanco.isEmpty()) {
			return 0;
		}

		// Creando registro de nuevo fichero
		idDisqueteAbono = admDisqueteAbonos.getNuevoID(idInstitucion);
		String nombreFichero = rp.returnProperty("facturacion.prefijo.ficherosAbonos") + idDisqueteAbono;

		// Creacion entrada FAC_DISQUETEABONO
		Hashtable disqueteAbono = new Hashtable();
		disqueteAbono.put(FacDisqueteAbonosBean.C_IDINSTITUCION, idInstitucion);
		disqueteAbono.put(FacDisqueteAbonosBean.C_IDDISQUETEABONO, idDisqueteAbono.toString());
		disqueteAbono.put(FacDisqueteAbonosBean.C_FECHA, "SYSDATE");
		disqueteAbono.put(FacDisqueteAbonosBean.C_FECHAEJECUCION, "SYSDATE");
		disqueteAbono.put(FacDisqueteAbonosBean.C_BANCOS_CODIGO, (String) banco.get(FacBancoInstitucionBean.C_BANCOS_CODIGO));
		disqueteAbono.put(FacDisqueteAbonosBean.C_NOMBREFICHERO, nombreFichero);
			// sin extension ya que se pueden emitir fichero antiguo y nuevo. Se descargaran en un ZIP
		disqueteAbono.put(FacDisqueteAbonosBean.C_FCS, fcs);
		disqueteAbono.put(FacDisqueteAbonosBean.C_NUMEROLINEAS, new Integer(abonosBanco.size()));
		disqueteAbono.put(FacDisqueteAbonosBean.C_IDSUFIJO, (String) banco.get(FacSufijoBean.C_IDSUFIJO));

		if (!admDisqueteAbonos.insert(disqueteAbono)) {
			return -1;
		}

		// Por cada abono incluido en el disquete, inserto entrada en FAC_ABONOINCLUIDODISQUETE
		boolean correcto = true;
		Hashtable datosAbono, abonoDisquete, abonoParaActualizar;
		Vector vAbono;
		FacAbonoBean bAbono;
		Double importeAbonado, impPendientePorAbonar, impTotalAbonado, impTotalAbonadoPorBanco;
		Enumeration listaReceptores = abonosBanco.elements();
		while ((correcto) && (listaReceptores.hasMoreElements())) {
			// obteniendo datos del abono a introducir en fichero
			datosAbono = ((Row) listaReceptores.nextElement()).getRow();
			
			// control del importe a abonar
			importeAbonado = admAbonoDisquete.getImporteAbonado(idInstitucion, (String) datosAbono.get(FacAbonoBean.C_IDABONO));
			if (importeAbonado == 0) {
				continue;
			}
			// si hay importe a abonar, se puede continuar con la generacion del fichero 
			numeroAbonosIncluidosEnDisquete++;
			
			// introduciendo el abono en el fichero
			abonoDisquete = new Hashtable();
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION, idInstitucion);
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDABONO, (String) datosAbono.get(FacAbonoBean.C_IDABONO));
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO, idDisqueteAbono.toString());
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO, importeAbonado);
			abonoDisquete.put(FacAbonoIncluidoEnDisqueteBean.C_CONTABILIZADO, "N");
			correcto = admAbonoDisquete.insert(abonoDisquete);

			if (!correcto) {
				new ClsExceptions("Error al insertar el abono en el disquete: " + admAbonoDisquete.getError());
				return -1;
			}

			// actualizando importes y estado del abono pagado
			abonoParaActualizar = new Hashtable();
			abonoParaActualizar.put(FacAbonoBean.C_IDINSTITUCION, idInstitucion);
			abonoParaActualizar.put(FacAbonoBean.C_IDABONO, (String) datosAbono.get(FacAbonoBean.C_IDABONO));
			vAbono = adminAbono.selectByPK(abonoParaActualizar);
			if (vAbono != null && vAbono.size() > 0) {
				bAbono = (FacAbonoBean) vAbono.get(0);

				// importes
				impPendientePorAbonar = UtilidadesNumero.redondea(bAbono.getImpPendientePorAbonar().doubleValue() - importeAbonado, 2);
				impTotalAbonado = UtilidadesNumero.redondea(bAbono.getImpTotalAbonado().doubleValue() + importeAbonado,	2);
				impTotalAbonadoPorBanco = UtilidadesNumero.redondea(bAbono.getImpTotalAbonadoPorBanco().doubleValue() + importeAbonado, 2);

				bAbono.setImpPendientePorAbonar(new Double(impPendientePorAbonar));
				bAbono.setImpTotalAbonado(new Double(impTotalAbonado));
				bAbono.setImpTotalAbonadoPorBanco(new Double(impTotalAbonadoPorBanco));

				// estado
				if (impPendientePorAbonar <= 0) {
					bAbono.setEstado(FacAbonoAdm.ESTADO_PAGADO);
				} else if (bAbono.getIdCuenta() != null) {
					bAbono.setEstado(FacAbonoAdm.ESTADO_PENDIENTE_BANCO);
				} else {
					bAbono.setEstado(FacAbonoAdm.ESTADO_PENDIENTE_CAJA);
				}

				// actualizando
				if (!adminAbono.update(bAbono)) {
					throw new ClsExceptions("Error al actualizar estado e importes del abono: " + adminAbono.getError());
				}
			}
		}
		
		// El siguiente control se realiza para que no se generen ficheros vacios (mas info en R1608_0005)
		// TODO Este control es solo un parche. La solucion completa seria impedir iniciar el proceso si hay una generacion de fichero bancario en marcha: esto requiere introducir la generacion de ficheros de abonos en un proceso controlado en segundo plano.
		if (numeroAbonosIncluidosEnDisquete == 0) {
			return 0;
		}

		// Creacion de un fichero de abonos por cada banco
		String resultado[];
		int resultadoInt;
		try {
			// calculando nombre y ruta del fichero
			String rutaServidor = rp.returnProperty("facturacion.directorioAbonosBancosOracle");
			String sBarra = "";
			if (rutaServidor.indexOf("/") > -1) sBarra = "/"; 
			if (rutaServidor.indexOf("\\") > -1) sBarra = "\\";
			rutaServidor += sBarra + idInstitucion;
			String sExtension = rp.returnProperty("facturacion.extension.ficherosTransferenciasSEPA");
			nombreFichero += "." + sExtension;

			int nParametrosIn = 7;
			Object[] paramIn = new Object[nParametrosIn];
			paramIn[0] = idInstitucion.toString();
			paramIn[1] = idDisqueteAbono.toString();
			paramIn[2] = idPropositoSEPA.toString();
			paramIn[3] = idPropositoOtros.toString();
			paramIn[4] = rutaServidor;
			paramIn[5] = nombreFichero;
			paramIn[6] = lenguaje;

			int nParametrosOut = 2;
			resultado = new String[nParametrosOut];
			String paramInCadena = "?";
			for (int i = 1; i < nParametrosIn + nParametrosOut; i++) {
				paramInCadena = paramInCadena + ",?";
			}
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_ABONOS.Generarficherotransferencias(" + paramInCadena + ")}", nParametrosOut, paramIn);
			resultadoInt = Integer.parseInt(resultado[0]);

		} catch (Exception e) {
			throw new SIGAException("error.messages.application", e);
		}
		
		// propagando errores a la interfaz
		if (resultadoInt > 0) {
			throw new SIGAException(resultado[1]);
		} else if (resultadoInt < 0) {
			throw new ClsExceptions("Ha ocurrido un error al generar el fichero de transferencias. \nError en PL = " + resultado[0] + " - " + resultado[1]);
		}

		return 1; // 1 fichero generado
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
	
	
	protected String configurarFichero (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
	
	try{
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			
			String paramPropSEPA="";
			String paramPropOtros="";
			
			paramPropSEPA = paramAdm.getValor(this.getUserBean(request).getLocation(), "FAC","PROPOSITO_TRANSFERENCIA_SEPA", "");
			paramPropOtros = paramAdm.getValor(this.getUserBean(request).getLocation(), "FAC","PROPOSITO_OTRA_TRANSFERENCIA", "");
			
			FacPropositosAdm propositosAdm = new FacPropositosAdm(this.getUserBean(request));
			
			Integer idpropDefSEPA=propositosAdm.selectIdPropositoPorCodigo(paramPropSEPA);
			Integer idpropDefOtros=propositosAdm.selectIdPropositoPorCodigo(paramPropOtros);
			
			Vector vpropositos = propositosAdm.selectPropositos();
			
			Vector vpropositosList = new Vector();
			List <FacPropositosBean> propositosListSEPAFinal= new ArrayList<FacPropositosBean>();
			List <FacPropositosBean> propositosListOtrosFinal= new ArrayList<FacPropositosBean>();
			
			for (int vs = 0; vs < vpropositos.size(); vs++){
				
				FacPropositosBean propositosBean = (FacPropositosBean) vpropositos.get(vs);
				
				if (propositosBean.getTipoSEPA()!=0)
					propositosListSEPAFinal.add(propositosBean);
				else
					propositosListOtrosFinal.add(propositosBean);
			}

			request.setAttribute("listaPropositosSEPA", propositosListSEPAFinal);
			request.setAttribute("listaPropositosOtros", propositosListOtrosFinal);

			request.setAttribute("idpropDefSEPA", idpropDefSEPA);
			request.setAttribute("idpropDefOtros", idpropDefOtros);
			
			
		}  catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}	
		
		return "configurarFicheroBancario";
	
	}
		
}





