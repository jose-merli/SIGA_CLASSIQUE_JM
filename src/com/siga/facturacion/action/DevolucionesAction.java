/*
 * VERSIONES:
 * 
 * miguel.villegas - 22-12-2005 - Creacion
 *	
 */

/**
 * Clase action para el tratamiento de devoluciones.<br/>
 * Gestiona las devoluciones de facturas  
 */

package com.siga.facturacion.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

import com.atos.utils.*;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.DevolucionesForm;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;


public class DevolucionesAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("editarFactura")){
					mapDestino = editarFactura(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("reintentar")){
					mapDestino = reintentar(mapping, miForm, request, response);					
				}else if (accion.equalsIgnoreCase("anular")){
					mapDestino = anular(mapping, miForm, request, response);
				}else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request -  objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		Vector devoluciones = new Vector();

		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");						
			String idInstitucion=user.getLocation();			
			
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","DEV"); // busqueda normal

			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
//			if (buscar==null){
				DevolucionesForm miformSession = (DevolucionesForm)request.getSession().getAttribute("DevolucionesForm");
//				if (miformSession!=null) {
					miformSession.reset(mapping,request);
					miformSession.setComisiones("");
					miformSession.setIdDisqueteDevoluciones("");
					miformSession.setIdInstitucion("");
					miformSession.setRuta(null);
//				}
				DevolucionesForm miform = (DevolucionesForm)formulario;
				miform.reset(mapping,request);
				miform.setComisiones("");
				miform.setIdDisqueteDevoluciones("");
				miform.setIdInstitucion("");
				miform.setRuta(null);
//			}
			request.setAttribute("buscar",buscar);
			
			// Obtengo los diferentes disquetes de devoluciones
			FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(this.getUserBean(request));
			devoluciones=devolucionesAdm.getDevoluciones(idInstitucion);
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("container", devoluciones);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}				
		return result;
	}
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="abrir";
		return result;
		
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="editar";
		Vector ocultos=new Vector();
		Vector desgloseDevolucion=new Vector();
		try{
		
			DevolucionesForm form = (DevolucionesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			
			// Obtengo la informacion relacionada con los abonos
			FacLineaDevoluDisqBancoAdm devolucionAdm = new FacLineaDevoluDisqBancoAdm(this.getUserBean(request));
			desgloseDevolucion=devolucionAdm.getDesgloseDevolucion((String)ocultos.get(0),(String)ocultos.get(1));
					
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", ocultos.get(0));
			request.setAttribute("container", desgloseDevolucion);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		return (result);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="ver";

		try{
			Vector ocultos=new Vector();
			String modo="consulta";
			Hashtable datosAbonos=new Hashtable();

			DevolucionesForm form = (DevolucionesForm) formulario;
		
			// Obtengo valores del formulario y los estructuro
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
			modo = "consulta";
			datosAbonos.put("accion",modo);
			datosAbonos.put("idAbono",ocultos.get(0));
			datosAbonos.put("idInstitucion",ocultos.get(1));
			
			// Paso de parametros a las pestanhas
			request.setAttribute("datosAbonos", datosAbonos);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="nuevo";
		String idInstitucion="";
		
		try{		
			DevolucionesForm form = (DevolucionesForm) formulario;
			idInstitucion=form.getIdInstitucion();
			
			// Paso de parametros
			request.setAttribute("IDINSTITUCION", idInstitucion);
			
			// AQUI METER SI LA INSTITUCION TIENE COMISION
			CenInstitucionAdm admIns = new CenInstitucionAdm(this.getUserBean(request));
			String tiene = new Boolean(admIns.tieneProductoComision(idInstitucion)).toString();
			request.setAttribute("TIENEPRODUCTOCOMISION",tiene);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		String result		= "abrir";
		UserTransaction tx 	= null;
		
		String idInstitucion= "";
		String identificador= "";
		String nombreFichero= "";
		
		boolean correcto=true;
		
		String codigoError = "5397";	// Código de error, el fichero no se ha encontrado.
		String codigoErrorFormato = "5402";	// Código de error, Formato incorrecto.
		String codretorno;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr 		 = (UsrBean) request.getSession().getAttribute("USRBEAN");//							
			idInstitucion = usr.getLocation();	
						
//			 Gestion de nombres de ficheros del servidor y de oracle
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp 	 = new ReadProperties("SIGA.properties");			
		    String rutaServidor  = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + rp.returnProperty("facturacion.directorioDevolucionesJava");
		    String rutaOracle  	 = rp.returnProperty("facturacion.directorioDevolucionesOracle");

//		    Comienzo control de transacciones
			tx = usr.getTransactionPesada(); 			

			// Comienzo la transaccion
			tx.begin();		

			// Obtengo los datos del formulario			
			FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(this.getUserBean(request));
			DevolucionesForm miForm = (DevolucionesForm)formulario;			

			ClsLogging.writeFileLog("Aplicar Comisiones de devolucion="+miForm.getComisiones(),8);
				
			identificador	= devolucionesAdm.getNuevoID(idInstitucion).toString();	

			////////////////////////////////////////////////
			
			// Obtenemos la ruta completa del servidor donde vamos a generar el fichero
		    //rutaPC 			= miForm.getRuta();		
     		rutaServidor 	+= File.separator + idInstitucion;
     		nombreFichero 	= rutaServidor + File.separator +identificador+".d19";
     		
     		// Obtenemos la ruta completa de Oracle.
     		String barra 	= "";
    		if (rutaOracle.indexOf("/") > -1) barra = "/"; 
    		if (rutaOracle.indexOf("\\") > -1) barra = "\\";        		
    		rutaOracle 	+= barra + idInstitucion + barra;
   		
     		// Control de errores y apertura de ficheros
			//ficOrigen = new File(rutaPC);
			//dirDestino = new File(rutaServidor);

    		// tratamiento del fichero de ficheroOriginalgrafia
		    FormFile ficheroOriginal = miForm.getRuta();
		    if(ficheroOriginal==null || ficheroOriginal.getFileSize()<1){
		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
		    	
		    }else{
		    	InputStream stream =null;
	    		BufferedReader rdr = null;
	    		BufferedWriter out = null;
		    	try {			
		    		//retrieve the file data
		    		stream = ficheroOriginal.getInputStream();
		    		//write the file to the file specified
		    		File camino = new File (rutaServidor);
		    		camino.mkdirs();
/*
		    		OutputStream bos = new FileOutputStream(nombreFichero);
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}
*/

		    		// RGG CODIFICACION DEL FICHERO DE DEVOLUCIONES ES UTF-16 ¿?
//		    		BufferedReader rdr = new BufferedReader(new InputStreamReader(stream,"UTF-16"));
//		    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\1.d19"),"ISO-8859-1"));

		    		rdr = new BufferedReader(new InputStreamReader(stream));
		    		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreFichero),"ISO-8859-1"));

		    		String line = rdr.readLine();
		    		
		    		while (line!=null) {
			    		out.write(line);
			    		out.write("\n");
			    		line = rdr.readLine();
		    		}
/*		    		
		    		BufferedReader rdr = new BufferedReader(new InputStreamReader(stream));
//		    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreFichero),"ISO-8859-1"));
		    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:\1.d19"),"ISO-8859-1"));
		    		int bytesRead = 0;
		    		char[] buffer = new char[8192];
		    		while ((bytesRead = rdr.read(buffer, 0, 8192)) != -1) {
		    			out.write(buffer, 0, bytesRead);
		    		}
		    		
		    		out.close();
*/		    		
		    		//bos.close();
		    		
		    	} catch (FileNotFoundException fnfe) {
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
		    	}catch (IOException ioe) {
					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
		    	}
		    	finally	{
		    		// close the stream
		    		stream.close();
		    		out.close();
		    		rdr.close();
		    	}
		    }

			
			///////////////////
/*		    
		    if (!ficOrigen.exists()){
				throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");
	        }
		    else{
			    if (!ficOrigen.canRead()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			    }
			    else{
			    	try {
			    		// Creacion de fichero de devoluciones en servidor
						bufferLectura = new BufferedReader(new FileReader(ficOrigen));												
					    if (!dirDestino.exists()){
					       	dirDestino.mkdirs();
				        }
						ficDestino = new File(nombreFichero);
						
//						///////////////////////////////////////////////////////////////////////
//						// PRUEBAS PDF														///
//						File ficPDF=new File(rutaServidor+barra+identificador+".pdf");		///
//						Plantilla plantilla = new Plantilla();								/// 
//						plantilla.convertFO2PDF(ficOrigen, ficPDF);							///
//						///////////////////////////////////////////////////////////////////////
												
						printer = new PrintWriter(new BufferedWriter(new FileWriter(ficDestino, true)));
						linea=bufferLectura.readLine();
				    	while (linea != null){
					        printer.println(linea);
				    		contador++;
							linea=bufferLectura.readLine();
				    	}
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
					}catch(IOException _exc) {
				        printer.flush();
				        printer.close();
				    	bufferLectura.close();
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion");
					}catch(Exception _ex) {
						throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion");
					}			    	
			    }				    	
		    }
*/
				
			// Llamada a PL     		
			codretorno = actualizacionTablasDevoluciones(miForm.getIdInstitucion(), rutaOracle, identificador + ".d19", this.getUserBean(request).getLanguageInstitucion(), this.getUserName(request).toString());
		    
			if (codretorno.equalsIgnoreCase("0")){
				// Aplicacion de comisiones
			    if (miForm.getComisiones()!=null && miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
			    	ClsLogging.writeFileLog("Aplicando Comisiones de devolucion="+miForm.getComisiones(),8);
			    	correcto=aplicarComisiones(miForm.getIdInstitucion(),identificador,miForm.getComisiones(),this.getUserBean(request));
			    }
			}else if(codretorno.equalsIgnoreCase(codigoError)){
				tx.rollback();
				request.setAttribute("mensaje","facturacion.nuevoFichero.literal.confirmarReintentar");
				return "mostrarVentana";
			}else if(codretorno.equalsIgnoreCase(codigoErrorFormato)){
				tx.rollback();				
				return exitoModal("facturacion.nuevoFichero.literal.errorFormato",request);
			}else{
				correcto=false;
			}
		    
			if (correcto){
				tx.commit();				
				result=exitoModal("facturacion.nuevoFichero.literal.procesoCorrecto",request);			
			}
			else{
				tx.rollback();				
				result=exitoModal("facturacion.nuevoFichero.literal.errorLectura",request);
			}
		}catch (Exception e) { 
			try{
				borrarFichero(identificador, idInstitucion);
			}catch(Exception ex){
				throwExcp("messages.general.error",new String[] {"modulo.facturacion"},ex,tx); 
			}
			
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}		

		return result;
	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="modificar";
		return (result);		
	}

	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="borrar";
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";
		return (result);
	}

	/** 
	 * Aplica comisiones correspondientes a un determinado 
	 * @param  institucion - identificador de la institucion
	 * @param  idDisqDevoluciones -  identificador del disquete de devoluciones
	 * @param  usuario - usuario de sesion 
	 * @return  boolean - resultado de la operacion  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected boolean aplicarComisiones(String institucion, String idDisqDevoluciones, String aplicaComisionesCliente, UsrBean userBean) throws SIGAException {
		//if (aplicaComisionesCliente!=null && aplicaComisionesCliente.equalsIgnoreCase(ClsConstants.DB_TRUE)){
		boolean resultado = true;
		Vector productos = new Vector();
		Hashtable productoComision = new Hashtable();
		FacLineaDevoluDisqBancoBean lineaDevolucion = new FacLineaDevoluDisqBancoBean();
		
		try{			
		
			
				if (aplicaComisionesCliente!=null && 
				aplicaComisionesCliente.equalsIgnoreCase(ClsConstants.DB_TRUE) &&
				true ){}
				//SE APLICAN COMISIONES AL CLIENTE

				
				// Identificamos los productos con comisiones asociadas
			PysProductosInstitucionAdm admPI= new PysProductosInstitucionAdm(userBean);			
			productos=admPI.getProductosComisiones(institucion);
			
			// LO QUITO PORQUE SE COMPRUEBA ANTES if (!productos.isEmpty()){
				
				// RGG por si no tiene ningun producto con conmision
				productoComision=((Row)productos.firstElement()).getRow();		

				// Identificamos los disquetes devueltos asociados al fichero de devoluciones
				Vector devoluciones = new Vector();
				Hashtable criteriosDevolucion = new Hashtable();
				FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(userBean);
				criteriosDevolucion.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,institucion);
				criteriosDevolucion.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,idDisqDevoluciones);
				devoluciones=admLDDB.selectForUpdate(criteriosDevolucion);				
				Enumeration listaDevoluciones = devoluciones.elements();
				// Para cada devolucion...
				while (listaDevoluciones.hasMoreElements()){
					lineaDevolucion=(FacLineaDevoluDisqBancoBean)listaDevoluciones.nextElement();
					// Obtenemos el cliente al que aplicar la comision
					FacFacturaIncluidaEnDisqueteAdm admFIED= new FacFacturaIncluidaEnDisqueteAdm(userBean);
					FacFacturaIncluidaEnDisqueteBean facturaDisquete = new FacFacturaIncluidaEnDisqueteBean();
					Vector clientes = new Vector();
					Hashtable criteriosFactura = new Hashtable();
					if(lineaDevolucion.getIdInstitucion()!= null)
						criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,lineaDevolucion.getIdInstitucion().toString());
					if(lineaDevolucion.getIdDisqueteCargos()!= null)
						criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,lineaDevolucion.getIdDisqueteCargos().toString());
					if(lineaDevolucion.getIdFacturaIncluidaEnDisquete()!= null)
						criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,lineaDevolucion.getIdFacturaIncluidaEnDisquete().toString());
					clientes=admFIED.selectByPKForUpdate(criteriosFactura);
					facturaDisquete=(FacFacturaIncluidaEnDisqueteBean)clientes.firstElement();
					
					// Averiguamos si se le aplica la comision a dicho cliente
					Vector comisionesClientes = new Vector();
					CenClienteAdm admCliente= new CenClienteAdm(userBean);
					CenClienteBean clienteBean = new CenClienteBean();
					Hashtable criteriosCliente = new Hashtable();
					if(facturaDisquete.getIdInstitucion()!= null)
						criteriosCliente.put(CenClienteBean.C_IDINSTITUCION,facturaDisquete.getIdInstitucion().toString());
					if(facturaDisquete.getIdPersona()!= null)
						criteriosCliente.put(CenClienteBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
					comisionesClientes=admCliente.selectForUpdate(criteriosCliente);
					clienteBean=(CenClienteBean)comisionesClientes.firstElement();
					
					if (aplicaComisionesCliente!=null && 
						aplicaComisionesCliente.equalsIgnoreCase(ClsConstants.DB_TRUE) &&
						clienteBean.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){

						// RGG 15/09/2006 COMISIONES A CARGO DEL CLIENTE
						
						// Deteccion banco CGAE
						Vector bancos = new Vector();
						FacDisqueteDevolucionesAdm admDD= new FacDisqueteDevolucionesAdm(userBean);
						FacDisqueteDevolucionesBean disqueteDevolucion = new FacDisqueteDevolucionesBean();
						bancos=admDD.selectByPKForUpdate(criteriosDevolucion);
						disqueteDevolucion=(FacDisqueteDevolucionesBean)bancos.firstElement();
						// Comision banco a aplicar (ajena o propia)
						Vector comisiones = new Vector();
						Hashtable criteriosBanco = new Hashtable();
						FacBancoInstitucionAdm admBI= new FacBancoInstitucionAdm(userBean);
						FacBancoInstitucionBean comision = new FacBancoInstitucionBean();
						if(disqueteDevolucion.getIdInstitucion()!= null)
							criteriosBanco.put(FacBancoInstitucionBean.C_IDINSTITUCION,disqueteDevolucion.getIdInstitucion().toString());
						criteriosBanco.put(FacBancoInstitucionBean.C_BANCOS_CODIGO,disqueteDevolucion.getBancosCodigo());
						comisiones=admBI.selectByPKForUpdate(criteriosBanco);
						comision=(FacBancoInstitucionBean)comisiones.firstElement();
						// Deteccion banco del cliente
						CenCuentasBancariasAdm admCB= new CenCuentasBancariasAdm(userBean);
						CenCuentasBancariasBean cuentaCliente = new CenCuentasBancariasBean();
						Vector bancosCliente = new Vector();
						Hashtable criteriosCC = new Hashtable();
						if(facturaDisquete.getIdInstitucion()!= null)
							criteriosCC.put(CenCuentasBancariasBean.C_IDINSTITUCION,facturaDisquete.getIdInstitucion().toString());
						if(facturaDisquete.getIdPersona()!= null)
							criteriosCC.put(CenCuentasBancariasBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
						if(facturaDisquete.getIdCuenta()!= null)
							criteriosCC.put(CenCuentasBancariasBean.C_IDCUENTA,facturaDisquete.getIdCuenta().toString());						
						bancosCliente=admCB.selectByPKForUpdate(criteriosCC);
						cuentaCliente=(CenCuentasBancariasBean)bancosCliente.firstElement();
						
						// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
						Hashtable original = new Hashtable();
						if(lineaDevolucion.getIdInstitucion()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,lineaDevolucion.getIdInstitucion().toString());
						if(lineaDevolucion.getIdDisqueteDevoluciones()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,lineaDevolucion.getIdDisqueteDevoluciones().toString());
						original.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,lineaDevolucion.getIdRecibo());
						if(lineaDevolucion.getIdDisqueteCargos()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS,lineaDevolucion.getIdDisqueteCargos().toString());						
						original.put(FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE,lineaDevolucion.getIdFacturaIncluidaEnDisquete()); 
						original.put(FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS,lineaDevolucion.getDescripcionMotivos());
						if(lineaDevolucion.getGastosDevolucion()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION,lineaDevolucion.getGastosDevolucion().toString());
						original.put(FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE,lineaDevolucion.getCargarCliente());
						if(lineaDevolucion.getContabilizada()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_CONTABILIZADA,lineaDevolucion.getContabilizada().toString());
						lineaDevolucion.setOriginalHash(original);
						lineaDevolucion.setCargarCliente("S");
						if (disqueteDevolucion.getBancosCodigo().equalsIgnoreCase(cuentaCliente.getCbo_Codigo())){
							lineaDevolucion.setGastosDevolucion(comision.getImpComisionPropiaCargo());
						}
						else{
							lineaDevolucion.setGastosDevolucion(comision.getImpComisionAjenaCargo());
						}
						resultado=admLDDB.update(lineaDevolucion);

						if (resultado){
							String formaPago="";
							String idCuenta="";
							// Inserciones relacionadas previa obtencion forma de pago
							PysFormaPagoProductoAdm admFPP= new PysFormaPagoProductoAdm(userBean);
							
							
							/* RGG 
							 * 12/11/2007
							 */ 
							
							formaPago=String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA);
							idCuenta=facturaDisquete.getIdCuenta().toString();
							
							/* RGG 
							 * 12/11/2007 
							 * Como finalmente el tratamiento es el mismo se quita el if y las operaciones.
							 * Lo que hacemos es ponerle siempre la misma forma de pago (Domiciliacion) y la cuenta la del disquete.
							 * 
							Vector formasPagoProductoComision = admFPP.getProductoPorFactura((String)productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION),(String)productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO), (String)productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO), (String)productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
							if (formasPagoProductoComision.isEmpty()){
								// No existe forma de pago por banco para el producto comision.
								formaPago=String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA);
								idCuenta=facturaDisquete.getIdCuenta().toString();
							} else {
								// SI existe forma de pago por banco para el producto comision.
								//Vector formasPago=new Vector();
								//formasPago=admFPP.getFormasPagoProducto((String)productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION),(String)productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO), (String)productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO), (String)productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
								if(formasPagoProductoComision.size()>0)
									formaPago=((Row)formasPagoProductoComision.firstElement()).getRow().get(PysFormaPagoProductoBean.C_IDFORMAPAGO).toString();
								//idCuenta=null;
								idCuenta=facturaDisquete.getIdCuenta().toString();
							}
							*/
							
							// Insercion PYS_COMPRASUSCRIPCION						
							PysPeticionCompraSuscripcionAdm admPCS= new PysPeticionCompraSuscripcionAdm(userBean);
							String idPeticion="";
							idPeticion=admPCS.getNuevoID(new Integer((String)productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION))).toString();
							Hashtable compraSuscripcion=new Hashtable();
							compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION));
							compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDPETICION,idPeticion);
							compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION,"A");
							compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
							compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_FECHA,"sysdate");
							compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION,"20");
					//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA,null);
					//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_NUM_AUT,null);
					//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_NUM_OPERACION,null);
					//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_REFERENCIA,null);
							resultado=admPCS.insert(compraSuscripcion);
							
							// Insercion PYS_PRODUCTOSSOLICITADOS
							if (resultado){
								PysProductosSolicitadosAdm admPS= new PysProductosSolicitadosAdm(userBean);
								Hashtable productosSolicitados=new Hashtable();
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION));
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDPETICION,idPeticion);
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO));
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
								productosSolicitados.put(PysProductosSolicitadosBean.C_IDFORMAPAGO,formaPago);
								productosSolicitados.put(PysProductosSolicitadosBean.C_CANTIDAD,"1");
								productosSolicitados.put(PysProductosSolicitadosBean.C_ACEPTADO,"A");
								productosSolicitados.put(PysProductosSolicitadosBean.C_VALOR,lineaDevolucion.getGastosDevolucion());
								productosSolicitados.put(PysProductosSolicitadosBean.C_PORCENTAJEIVA,productoComision.get(PysProductosInstitucionBean.C_PORCENTAJEIVA));
								
								productosSolicitados.put(PysProductosSolicitadosBean.C_NOFACTURABLE,productoComision.get(PysProductosInstitucionBean.C_NOFACTURABLE));
								
								if(idCuenta != null)
									productosSolicitados.put(PysProductosSolicitadosBean.C_IDCUENTA,idCuenta);
					//			productosSolicitados.put(PysProductosSolicitadosBean.C_IDTIPOENVIOS,null);
					//			productosSolicitados.put(PysProductosSolicitadosBean.C_IDDIRECCION,null);
								resultado=admPS.insert(productosSolicitados);
								
								// Insercion PYS_PRODUCTOSSOLICITADOS
								if (resultado){
									PysCompraAdm admCompra= new PysCompraAdm(userBean);
									Hashtable compra=new Hashtable();
									compra.put(PysCompraBean.C_IDINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION));
									compra.put(PysCompraBean.C_IDPETICION,idPeticion);
									compra.put(PysCompraBean.C_IDTIPOPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
									compra.put(PysCompraBean.C_IDPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO));
									compra.put(PysCompraBean.C_IDPRODUCTOINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
									compra.put(PysCompraBean.C_FECHA,"sysdate");
									compra.put(PysCompraBean.C_CANTIDAD,"1");
									compra.put(PysCompraBean.C_IMPORTEUNITARIO,lineaDevolucion.getGastosDevolucion());
									compra.put(PysCompraBean.C_PORCENTAJEIVA,productoComision.get(PysProductosInstitucionBean.C_PORCENTAJEIVA));
									compra.put(PysCompraBean.C_IDFORMAPAGO,formaPago);
									compra.put(PysCompraBean.C_DESCRIPCION,"Comisión bancaria por devolucion factura el día "+disqueteDevolucion.getFechaGeneracion());
									compra.put(PysCompraBean.C_IMPORTEANTICIPADO,"0");
									compra.put(PysCompraBean.C_ACEPTADO,"A");
									
									compra.put(PysCompraBean.C_NOFACTURABLE,productoComision.get(PysProductosInstitucionBean.C_NOFACTURABLE));
									
					//				compra.put(PysCompraBean.C_NUMEROLINEA,null);									
					//				compra.put(PysCompraBean.C_IDFACTURA,null);
					//				compra.put(PysCompraBean.C_FECHABAJA,null);
									compra.put(PysCompraBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
									if(idCuenta != null)
										compra.put(PysCompraBean.C_IDCUENTA,idCuenta);
									resultado=admCompra.insert(compra);
								}	
							}
						}	
					} else {
						
						// RGG 15/09/2006 COMISIONES A CARGO DE LA INSTITUCION
						
						// Deteccion banco CGAE
						Vector bancos = new Vector();
						FacDisqueteDevolucionesAdm admDD= new FacDisqueteDevolucionesAdm(userBean);
						FacDisqueteDevolucionesBean disqueteDevolucion = new FacDisqueteDevolucionesBean();
						bancos=admDD.selectByPKForUpdate(criteriosDevolucion);
						disqueteDevolucion=(FacDisqueteDevolucionesBean)bancos.firstElement();
						// Comision banco a aplicar (ajena o propia)
						Vector comisiones = new Vector();
						Hashtable criteriosBanco = new Hashtable();
						FacBancoInstitucionAdm admBI= new FacBancoInstitucionAdm(userBean);
						FacBancoInstitucionBean comision = new FacBancoInstitucionBean();
						if(disqueteDevolucion.getIdInstitucion()!= null)
							criteriosBanco.put(FacBancoInstitucionBean.C_IDINSTITUCION,disqueteDevolucion.getIdInstitucion().toString());
						criteriosBanco.put(FacBancoInstitucionBean.C_BANCOS_CODIGO,disqueteDevolucion.getBancosCodigo());
						comisiones=admBI.selectByPKForUpdate(criteriosBanco);
						comision=(FacBancoInstitucionBean)comisiones.firstElement();
						// Deteccion banco del cliente
						CenCuentasBancariasAdm admCB= new CenCuentasBancariasAdm(userBean);
						CenCuentasBancariasBean cuentaCliente = new CenCuentasBancariasBean();
						Vector bancosCliente = new Vector();
						Hashtable criteriosCC = new Hashtable();
						if(facturaDisquete.getIdInstitucion()!= null)
							criteriosCC.put(CenCuentasBancariasBean.C_IDINSTITUCION,facturaDisquete.getIdInstitucion().toString());
						if(facturaDisquete.getIdPersona()!= null)
							criteriosCC.put(CenCuentasBancariasBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
						if(facturaDisquete.getIdCuenta()!= null)
							criteriosCC.put(CenCuentasBancariasBean.C_IDCUENTA,facturaDisquete.getIdCuenta().toString());						
						bancosCliente=admCB.selectByPKForUpdate(criteriosCC);
						cuentaCliente=(CenCuentasBancariasBean)bancosCliente.firstElement();
						
						// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
						Hashtable original = new Hashtable();
						if(lineaDevolucion.getIdInstitucion()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,lineaDevolucion.getIdInstitucion().toString());
						if(lineaDevolucion.getIdDisqueteDevoluciones()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,lineaDevolucion.getIdDisqueteDevoluciones().toString());
						original.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,lineaDevolucion.getIdRecibo());
						if(lineaDevolucion.getIdDisqueteCargos()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS,lineaDevolucion.getIdDisqueteCargos().toString());						
						original.put(FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE,lineaDevolucion.getIdFacturaIncluidaEnDisquete()); 
						original.put(FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS,lineaDevolucion.getDescripcionMotivos());
						if(lineaDevolucion.getGastosDevolucion()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION,lineaDevolucion.getGastosDevolucion().toString());
						original.put(FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE,lineaDevolucion.getCargarCliente());
						if(lineaDevolucion.getContabilizada()!= null)
							original.put(FacLineaDevoluDisqBancoBean.C_CONTABILIZADA,lineaDevolucion.getContabilizada().toString());
						lineaDevolucion.setOriginalHash(original);
						// RGG 15/09/2006 DIFERENCIA CON LO DE ARRIBA
						lineaDevolucion.setCargarCliente("N");
						if (disqueteDevolucion.getBancosCodigo().equalsIgnoreCase(cuentaCliente.getCbo_Codigo())){
							lineaDevolucion.setGastosDevolucion(comision.getImpComisionPropiaCargo());
						}
						else{
							lineaDevolucion.setGastosDevolucion(comision.getImpComisionAjenaCargo());
						}
						resultado=admLDDB.update(lineaDevolucion);
						
						// RGG 15/09/2006 NO DUPLICO LO SIGUIENTE PORQUE NO ES NECESARIO
						
					} // ELSE
				} // WHILE
			//}
		} 
		catch (Exception e) { 
 			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		return resultado;
	}
	
	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editarFactura(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="editarFactura";
		Vector ocultos=new Vector();
		try{
		
			DevolucionesForm form = (DevolucionesForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);
					
			// Paso de parametros empleando request
			Hashtable datosFac = new Hashtable();
			UtilidadesHash.set(datosFac,"accion", "editar");
			UtilidadesHash.set(datosFac,"idFactura", (String)ocultos.get(1));
			UtilidadesHash.set(datosFac,"idInstitucion", (String)ocultos.get(0));

			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, (String)ocultos.get(0));
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, (String)ocultos.get(1));
			FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));
			Vector v = facturaAdm.selectByPK(claves);
			FacFacturaBean facturaBean = null;
			if (v != null && v.size()>0) {
				facturaBean = (FacFacturaBean) v.get(0);
				UtilidadesHash.set(datosFac,"idPersona", facturaBean.getIdPersona());
			}
			
			// Paso de parametros a las pestanhas
			request.setAttribute("datosFacturas", datosFac);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		return (result);
	}
	/** 
	 *  Funcion que reintenta insertar el fichero 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String reintentar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result			= "abrir";
		UserTransaction tx 		= null;		
		String identificador	= "";		
		boolean correcto		= true;
		
		//String codigoError 		= "5397";	// Código de error, el fichero no se ha encontrado.
		String codretorno;
		try{
//			 Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr 		 = (UsrBean) request.getSession().getAttribute("USRBEAN");//							
			String idInstitucion = usr.getLocation();	
						
//			 Gestion del nombre del fichero de oracle
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp 	 = new ReadProperties("SIGA.properties");
		    String rutaOracle  	 = rp.returnProperty("facturacion.directorioDevolucionesOracle");
		    
//		    Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			
			// Obtengo los datos del formulario			
			FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(this.getUserBean(request));
			DevolucionesForm miForm = (DevolucionesForm)formulario;			
				
			identificador	= devolucionesAdm.getNuevoID(idInstitucion).toString();				
			
     		// Obtenemos la ruta completa de Oracle.
     		String barra 	= "";
    		if (rutaOracle.indexOf("/") > -1) barra = "/"; 
    		if (rutaOracle.indexOf("\\") > -1) barra = "\\";        		
    		rutaOracle 	+= barra + idInstitucion + barra;
    		
//     	 Comienzo la transaccion
			tx.begin();		
				
			// Llamada a PL 
			codretorno = actualizacionTablasDevoluciones(miForm.getIdInstitucion(), rutaOracle, identificador + ".d19", this.getUserBean(request).getLanguageInstitucion(), this.getUserName(request).toString());
		    
			if (codretorno.equalsIgnoreCase("0")){
				// Aplicacion de comisiones
			    if (miForm.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){
			    	correcto=aplicarComisiones(miForm.getIdInstitucion(),identificador,miForm.getComisiones(),this.getUserBean(request));
			    }			
			}else{
				correcto=false;
			}
			
			if (correcto){
				tx.commit();				
				result=exitoRefresco("facturacion.nuevoFichero.literal.procesoCorrecto",request);			
			}
			else{
				tx.rollback();
				borrarFichero(identificador, idInstitucion);
				result=exitoModal("facturacion.nuevoFichero.literal.errorLectura",request);
			}
		}catch (Exception e) { 			
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}	
		return result;  		
     	
	}

	/** 
	 *  Funcion que borra el fichero 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String anular(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result		= "abrir";			
		String identificador= "";	
		
		try{
//			 Obtengo usuario y creo manejadores para acceder a las BBDD
				UsrBean usr 		 = (UsrBean) request.getSession().getAttribute("USRBEAN");//							
				String idInstitucion = usr.getLocation();								
//				
				// Obtengo los datos del formulario			
				FacDisqueteDevolucionesAdm devolucionesAdm = new FacDisqueteDevolucionesAdm(this.getUserBean(request));
					
				identificador			= devolucionesAdm.getNuevoID(idInstitucion).toString();				
	    		borrarFichero(identificador, idInstitucion);
	    		
     		}catch (Exception e) { 
    			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
    		}
     		return result;  
	}

	/** 
	 *  Funcion que realiza una llamada a la PL PKG_SIGA_CARGOS.DEVOLUCIONES
	 * @param  String - identificador institucion
	 * @param  path -  ruta hasta el fichero
	 * @param  fichero - nombre del fichero 
	 * @param  usuario - identificador del usuario  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String actualizacionTablasDevoluciones(String institucion, String path, String fichero, String idioma, String usuario) throws ClsExceptions {	
		String resultado[] = new String[2];
	//	boolean devolucion=true;
		String codigoError = "5397";	// Código de error, el fichero no se ha encontrado.
		String codigoErrorFormato = "5402";	// Código de error, Formato incorrecto
		String codretorno  = codigoError;
		try	{			
			int i=0;
			while (i<3 && codretorno.equalsIgnoreCase(codigoError)){
				i++;
				Thread.sleep(1000);
				Object[] param_in = new Object[5];
		    	param_in[0] = institucion;
		    	param_in[1] = path;
		    	param_in[2] = fichero;
		    	param_in[3] = idioma;
		    	param_in[4] = usuario;
		    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.DEVOLUCIONES(?,?,?,?,?,?,?)}", 2, param_in);
		    	codretorno = resultado[0]; 
			}
			
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en actualizacionTableroDevoluciones.  Proc:PKG_SIGA_CARGOS.DEVOLUCIONES "+resultado[1]);
		}
		return codretorno;
	}
	
	protected void borrarFichero(String identificador, String idInstitucion) throws ClsExceptions {	
		File ficJava = null;
		String rutaServidor;
		String nombreFichero;
		
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp 	 = new ReadProperties("SIGA.properties");			
	    rutaServidor  = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + rp.returnProperty("facturacion.directorioDevolucionesJava");
		boolean ok=false; 
		
		// Obtenemos la ruta completa del servidor donde vamos a generar el fichero	  
 		rutaServidor 	+= File.separator + idInstitucion;
 		nombreFichero 	= rutaServidor + File.separator +identificador+".d19";
 		
 		ficJava = new File(nombreFichero);
 		if (ficJava.exists()){		 
 			ok=ficJava.delete();
 		}
		
	}
	
	
    /* (non-Javadoc)
     * @see com.siga.general.MasterAction#download(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
    {
		try {
		    DevolucionesForm form = (DevolucionesForm)formulario;
	        
	        Vector vOcultos = form.getDatosTablaOcultos(0);
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();

		    // Nombre del fichero
	 		String ficheroDownload = ((String)vOcultos.elementAt(2)).trim();
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//	 		ReadProperties rp 	 = new ReadProperties("SIGA.properties");			
		    String rutaFicheroDownload = rp.returnProperty("facturacion.directorioFisicoDevolucionesJava") + 
		    							 rp.returnProperty("facturacion.directorioDevolucionesJava") +
		    							 File.separator + idInstitucion +
		    							 File.separator + ficheroDownload;

		    request.setAttribute("nombreFichero", ficheroDownload);
		    request.setAttribute("rutaFichero", rutaFicheroDownload);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		return "descargaFichero";
    }
}

