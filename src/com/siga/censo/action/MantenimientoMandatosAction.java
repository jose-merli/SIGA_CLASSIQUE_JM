// VERSIONES:
// raul.ggonzalez 14-12-2004 Creacion
// miguel.villegas 11-01-2005 Incorpora "borrar"
// juan.grau 18-04-2005 Incorpora 'buscarPersona' y 'enviarPersona'

/**
 * @version 30/01/2006 (david.sanchezp): nuevo valor de pestanha.
 */
package com.siga.censo.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.LogFileWriter;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenMandatosAdm;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.EnvEnviosAdm;
import com.siga.censo.form.MantenimientoMandatosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class MantenimientoMandatosAction extends MasterAction {	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						MantenimientoMandatosForm formClientes = (MantenimientoMandatosForm)miForm;
						formClientes.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						formClientes.reset(mapping,request);
						request.getSession().removeAttribute("DATAPAGINADOR");
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("enviarCliente")){
						// enviarCliente
						mapDestino = enviarCliente(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("enviarPersona")){
						// enviarCliente
						mapDestino = enviarPersona(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarInit")){
						miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
						request.getSession().removeAttribute("DATAPAGINADOR");
						mapDestino = buscarPor(mapping, miForm, request, response); 
					}else if (accion.equalsIgnoreCase("generaExcel")){
						mapDestino = generaExcel(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("firmarSeleccionados")){
						mapDestino = firmarMandatos(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("procesarFichero")){
						mapDestino = procesarFichero(mapping, miForm, request, response);
					}else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}

	



	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			
			MantenimientoMandatosForm miform = (MantenimientoMandatosForm)formulario;
			miform.reset(mapping,request);
			
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);

	        
	      /***************************************************/  
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}


	
	/**
	 * Metodo que implementa el modo buscarPor para realizar la busqueda de un colegiado o no colegiado.
	 * <br> Implementa tanto la busqueda simple como avanzada.
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
		//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
		//de la super clase(MasterAction)
		String[] clavesBusqueda={"REFERENCIA"};
	 
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			// casting del formulario
			MantenimientoMandatosForm miFormulario = (MantenimientoMandatosForm)formulario;

			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
				&& !miFormulario.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			CenMandatosAdm mandatoAdm = new CenMandatosAdm(user);
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null &&!isSeleccionarTodos){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				
				Vector datos=new Vector();
				if (paginador!=null){
					String pagina = (String)request.getParameter("pagina");
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();
				 			
				Paginador resultado = null;
				Vector datos = null;

				resultado = mandatoAdm.getClientesMandatos(idInstitucion,miFormulario, user.getLanguage());
				
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)mandatoAdm.selectGenerico(resultado.getQueryInicio()));
						aniadeClavesBusqueda(clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
//					
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					databackup.put("datos",datos);
					
					
					
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);				

				request.setAttribute("CenResultadoBusquedaClientes",resultado);				
			}
			destino="resultado";
			request.getSession().setAttribute("CenBusquedaClientesTipo","N"); // busqueda normal
			
	        CenInstitucionAdm instAdm = new CenInstitucionAdm(user); 
	        request.setAttribute("nombreColegio",instAdm.getNombreInstitucion(idInstitucion));

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}
	

	/**
	 * Metodo que implementa el modo enviarCliente
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviarCliente (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			MantenimientoMandatosForm miform = (MantenimientoMandatosForm)formulario;

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
//			Vector vOcultos = miform.getDatosTablaOcultos(0);
			
			
				// obtener idpersona
			String idPersona = miform.getIdPersona();
			// obtener idinstitucion
			String idInstitucion = miform.getIdInstitucion();
			
			// obtener nColegiado
			//String nColegiado = (String)vOcultos.get(2);
			String nColegiado = miform.getNumeroColegiado();
			// obtener nifCif
			//String nifCif = (String)vOcultos.get(3);
			String nifCif = miform.getNif();
			// obtener nombre
			//String nombre = (String)vOcultos.get(4);
			String nombre = miform.getNombrePersona();
			// obtener apellido1
			//String apellido1 = (String)vOcultos.get(5);
			String apellido1 = miform.getApellido1();
			// obtener apellido2
			//String apellido2 = (String)vOcultos.get(6);
			String apellido2 = miform.getApellido2();
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Hashtable datosCliente = new Hashtable();
			
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("nColegiado",nColegiado);
			datosCliente.put("nifCif",nifCif);
			datosCliente.put("nombre",nombre);
			datosCliente.put("apellido1",apellido1);
			datosCliente.put("apellido2",apellido2);
			
			request.setAttribute("datosClienteModal", datosCliente);		

			try {
				EnvEnviosAdm adm = new EnvEnviosAdm (this.getUserBean(request));
				Vector v = adm.getDirecciones(idInstitucion, idPersona, "-1");
				if (v != null && v.size() == 1)
					request.setAttribute("unicaDireccion", (Hashtable)v.get(0));
			}
			catch (Exception e) {}
			
			destino="seleccion";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
		 return destino;
	}

	
	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviarPersona (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String destino = "";

		try {
			
			MantenimientoMandatosForm miform = (MantenimientoMandatosForm)formulario;

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);

			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion = (String)vOcultos.get(1);
			/*// obtener nifCif
			String nifCif = (String)vOcultos.get(2);
			// obtener nombre
			String nombre = (String)vOcultos.get(3);
			// obtener apellido1
			String apellido1 = (String)vOcultos.get(4);
			// obtener apellido2
			String apellido2 = (String)vOcultos.get(5);*/
			

			// obtener nifCif
			//String nifCif = (String)vOcultos.get(3);
			String nifCif = miform.getNif();
			// obtener nombre
			//String nombre = (String)vOcultos.get(4);
			String nombre = miform.getNombrePersona();
			// obtener apellido1
			//String apellido1 = (String)vOcultos.get(5);
			String apellido1 = miform.getApellido1();
			// obtener apellido2
			//String apellido2 = (String)vOcultos.get(6);
			String apellido2 = miform.getApellido2();
			
			
			
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Hashtable datosPersona = new Hashtable();
			
			datosPersona.put("idPersona",idPersona);
			datosPersona.put("idInstitucion",idInstitucion);
			datosPersona.put("nifCif",nifCif);
			datosPersona.put("nombre",nombre);
			datosPersona.put("apellido1",apellido1);
			datosPersona.put("apellido2",apellido2);
			
			request.setAttribute("datosPersona", datosPersona);		

			destino="seleccionPersona";
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	   	 }
		 return destino;
	}
	
	

	
	
	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		
        Vector datos = new Vector();
		
		
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idInstitucion=user.getLocation();
			MantenimientoMandatosForm form = (MantenimientoMandatosForm)formulario;
			CenColegiadoAdm colegiadoAdm = null;
			CenNoColegiadoAdm noColegiadoAdm = null;
			Vector datosTabla = null;
			String idTipoPersona = null;
			CenMandatosAdm mandatoAdm = new CenMandatosAdm(user);
			HashMap databackup = (HashMap) form.getDatosPaginador();
			Paginador resultado = mandatoAdm.getClientesMandatos(idInstitucion, form, user.getLanguage());
			ArrayList registrosBusqueda = new ArrayList((Collection)mandatoAdm.selectGenerico(resultado.getQueryInicio()));
			Hashtable registro = null;
			boolean isSeleccionado=false;
			int fila=1;
			
			String ruta=getDirectorioFicherosCarga(user.getLocation());
			File path = new File(ruta);
			path.mkdirs();
			
			StringBuffer nombreFichero = new StringBuffer("Mandatos_" + user.getLocation());
			nombreFichero.append("_");
			nombreFichero.append(UtilidadesString.getTimeStamp());
			nombreFichero.append(".xls");
			
			String fichero=ruta+File.separator+nombreFichero.toString();
			
			File output = new File(fichero);
			
			FileOutputStream fileOut = new FileOutputStream(output);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet("Mandatos");
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			HSSFFont font = workbook.createFont();
			cellStyle.setBorderBottom((short) 2);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);
			
			worksheet.setColumnWidth(0, 10*256);
			worksheet.setColumnWidth(1, 14*256);
			worksheet.setColumnWidth(2, 20*256);
			worksheet.setColumnWidth(3, 20*256);
			worksheet.setColumnWidth(4, 13*256);
			worksheet.setColumnWidth(5, 30*256);
			worksheet.setColumnWidth(6, 30*256);
			worksheet.setColumnWidth(7, 13*256);
			worksheet.setColumnWidth(8, 30*256);
			
			HSSFRow cabecera = worksheet.createRow(0);
			HSSFCell cab0 = cabecera.createCell(0);
			cab0.setCellValue("NCOLEGIADO");
			cab0.setCellStyle(cellStyle);
			HSSFCell cab1 = cabecera.createCell(1);
			cab1.setCellValue("NIFCIF");
			cab1.setCellStyle(cellStyle);
			HSSFCell cab2 = cabecera.createCell(2);
			cab2.setCellValue("APELLIDOS");
			cab2.setCellStyle(cellStyle);
			HSSFCell cab3 = cabecera.createCell(3);
			cab3.setCellValue("NOMBRE");
			cab3.setCellStyle(cellStyle);
			HSSFCell cab4 = cabecera.createCell(4);
			cab4.setCellValue("TIPOMANDATO");
			cab4.setCellStyle(cellStyle);
			HSSFCell cab5 = cabecera.createCell(5);
			cab5.setCellValue("IBAN");
			cab5.setCellStyle(cellStyle);
			HSSFCell cab6 = cabecera.createCell(6);
			cab6.setCellValue("REFERENCIA");
			cab6.setCellStyle(cellStyle);
			HSSFCell cab7 = cabecera.createCell(7);
			cab7.setCellValue("FECHA FIRMA");
			cab7.setCellStyle(cellStyle);
			HSSFCell cab8 = cabecera.createCell(8);
			cab8.setCellValue("LUGAR FIRMA");
			cab8.setCellStyle(cellStyle);

			for (int i = 0; i < registrosBusqueda.size(); i++) {
				registro=(Hashtable)registrosBusqueda.get(i);
				
	        	isSeleccionado = form.getDatosTabla().toString().contains("[["+registro.get("REFERENCIA")+"]]");
	        	
	        	if(isSeleccionado){
					HSSFRow row = worksheet.createRow(fila);
					HSSFCell c0 = row.createCell(0);
					c0.setCellValue(UtilidadesHash.getString(registro,"NCOLEGIADO"));
					HSSFCell c1 = row.createCell(1);
					c1.setCellValue(UtilidadesHash.getString(registro,"NIFCIF"));
					HSSFCell c2 = row.createCell(2);
					c2.setCellValue(UtilidadesHash.getString(registro,"APELLIDOS"));
					HSSFCell c3 = row.createCell(3);
					c3.setCellValue(UtilidadesHash.getString(registro,"NOMBRE"));
					HSSFCell c4 = row.createCell(4);
					c4.setCellValue(UtilidadesHash.getString(registro,"TIPOMANDATO"));
					HSSFCell c5 = row.createCell(5);
					c5.setCellValue(UtilidadesHash.getString(registro,"IBAN"));
					HSSFCell c6 = row.createCell(6);
					c6.setCellValue(UtilidadesHash.getString(registro,"REFERENCIA"));
					HSSFCell c7 = row.createCell(7);
					c7.setCellValue(UtilidadesString.formatoFecha(UtilidadesHash.getString(registro,"FECHAFIRMA"), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
					HSSFCell c8 = row.createCell(8);
					c8.setCellValue(UtilidadesHash.getString(registro,"LUGARFIRMA"));
					fila++;
	        	}
			}
			// Bloqueamos la primera
			workbook.getSheetAt(workbook.getActiveSheetIndex()).createFreezePane(0, 1);

			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
			if(output==null || !output.exists()){
				throw new SIGAException("error.messages.fileNotFound"); 
			}
			request.setAttribute("nombreFichero", output.getName());
			request.setAttribute("rutaFichero", output.getPath());
			request.setAttribute("borrarFichero", "true");			
			request.setAttribute("generacionOK","OK");
		} 
		catch (SIGAException e) 
		{ 
		  	throw e; 
		}
		catch (Exception e) 
		{ 
			throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null); 
		}
		
		
		return "descarga";
	}
	
	protected String firmarMandatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
        Vector datos = new Vector();
		
		try {
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idInstitucion=user.getLocation();
	        MantenimientoMandatosForm form = (MantenimientoMandatosForm)formulario;
			
			CenMandatosAdm mandatoAdm = new CenMandatosAdm(user);
			
	        for (int i = 0; i < form.getDatosTabla().size(); i++) {
	        	String linea = form.getDatosTablaOcultos(i).toString();
	        	linea=linea.replaceAll("#", " ");
	        	linea=linea.substring(1, linea.length()-1);
	        	
	        	Hashtable ht = new Hashtable();				
				mandatoAdm.firmarReferencia(linea, form.getFechaFirma(), form.getLugarFirma());

			}

		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "exito";
	}

	private String procesarFichero(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		

		try {
			MantenimientoMandatosForm form = (MantenimientoMandatosForm) formulario;
			FormFile formFile = form.getFichero();
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenMandatosAdm mandatoAdm = new CenMandatosAdm(user);
			
			StringBuffer pathFichero = new StringBuffer(getDirectorioFicherosCarga(user.getLocation()));
			File path = new File(pathFichero.toString());
			path.mkdirs();
			
			StringBuffer nombreFichero = new StringBuffer(formFile.getFileName().substring(0, formFile.getFileName().lastIndexOf('.')));
			nombreFichero.append("_");
			nombreFichero.append( user.getUserName()+"_"+UtilidadesString.getTimeStamp());
			nombreFichero.append(".xls");
			FileOutputStream fileOut = new FileOutputStream(pathFichero.toString()+File.separator+nombreFichero.toString());
			
			String ruta = getDirectorioFicherosCarga(user.getLocation());
			
			// Recuperamos el archivo del formulario 
			File file = new File(formFile.getFileName());
			// Creamos el streamd el archivo
			//InputStream is = new BufferedInputStream(formFile.getInputStream());  
			InputStream is = formFile.getInputStream();
			// A partir del stream creamos el workbook (generico para xls y xlsx)
			Workbook wb = WorkbookFactory.create(is);
			// Usaremos la primera hoja
	        Sheet ws = wb.getSheetAt(0);
	        // Cogemos los datos de columnas y filas
	        int rowNum = ws.getLastRowNum() + 1;
	        int colNum = ws.getRow(0).getLastCellNum();

	        String ref = "";
	        String fecha = "";
	        Date fechaDate;
	        String lugar = "";
	        
	        String accion = "";
	        
	        // Recorremos las filas
	        for(int i = 1; i <rowNum; i++){
	            Row row = ws.getRow(i);
	            // Para cada fila nos quedamos con la referencia, fecha y lugar
	            ref   = row.getCell(6, Row.CREATE_NULL_AS_BLANK).toString();
	            
	            try{
	            	fechaDate= row.getCell(7, Row.CREATE_NULL_AS_BLANK).getDateCellValue();
	            	fecha=UtilidadesString.formatoFecha(fechaDate,ClsConstants.DATE_FORMAT_SHORT_SPANISH);
	            }catch (Exception e) {
	        		fecha = row.getCell(7, Row.CREATE_NULL_AS_BLANK).toString();
	        	}
	            lugar = row.getCell(8, Row.CREATE_NULL_AS_BLANK).toString();
	            if(notNull(ref)&&notNull(fecha)&&notNull(lugar)){
	            	fecha = fecha.replaceAll("[^\\d]", "/");
	            	if(isValid(fecha)){
		        	// Si los campos tienen valor atualizamos el mandato
			        	if(mandatoAdm.firmarReferencia(ref, fecha, lugar)){
			        		accion="FIRMADO";
			        	}else{
			        		accion="No se ha podido firmar";
			        	}
	            	}else{
	            		accion="Fecha no válida - dd/mm/yyyy";
	            	}
	            }else{
		        	// Si no no podemos hacer nada
		        	accion="Datos insuficientes para poder firmar";
	            }
	            // Finalmente escribimos en el archivo la fila con
	            Cell resultado = row.createCell(9);
	            resultado.setCellValue(accion);
	        }
	        
	        // Finalmente guardamos el excel extendido como log
	        wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			request.setAttribute("nombreFichero",nombreFichero.toString());
			request.setAttribute("rutaFichero", pathFichero.toString()+"/"+nombreFichero.toString());
			request.setAttribute("borrarFichero", "false");			
			request.setAttribute("generacionOK","OK");
			
			return "descarga";
			
		} catch (Exception e) {
		    throwExcp("Error al leer el documento", new String[] {"modulo.censo"}, e, null);
		}
		
		return "inicio";
	}
	
	private boolean notNull(String val){
	    return (val!=null&!val.equalsIgnoreCase(""));
	}
	
	private String getDirectorioFicherosCarga(String idInstitucion){
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	    String pathFicheros = rp.returnProperty("gen.ficheros.path");
	    StringBuffer directorioFichero = new StringBuffer(pathFicheros);
		directorioFichero.append(idInstitucion);
		directorioFichero.append(File.separator);
		directorioFichero.append(rp.returnProperty("fac.ficheros.mandatos"));
		directorioFichero.append(File.separator);
		directorioFichero.append("ficherosCarga");
					
		return directorioFichero.toString();
	}
	
	private boolean isValid(String text) {
	    if (text == null || !text.matches("\\d{2}.\\d{2}.\\d{4}"))
	        return false;
	    
	    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	    df.setLenient(false);
	    try {
	        df.parse(text);
	        return true;
	    } catch (Exception ex) {
	        return false;
	    }
	}

}
