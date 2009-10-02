/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 21-03-2005 - Inicio
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacDisqueteCargosAdm;
import com.siga.facturacion.form.FicheroBancarioPagosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Clase action para Descargar los ficheros bancarios.<br/>
 * Gestiona abrir y descargar Ficheros
 * @version david.sanchezp: cambios para pedir y tener en cuenta la fecha de cargo.
 */
public class FicheroBancarioPagosAction extends MasterAction{
	
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
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);	
				
			}else if (accion.equalsIgnoreCase("download")){
				mapDestino = download(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("generarFichero")){
				mapDestino = generarFichero(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("informeRemesa")){
				mapDestino = informeRemesa(miForm, request);
			}
			else {
				return super.executeInternal(mapping, formulario, request, response);
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
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
	
	/** 
	 *  Funcion que atiende la accion abrir. Muestra todas las facturas programadas cuyas Fecha Real de Generación es null 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {					
			Integer idInstitucion	= this.getIDInstitucion(request);			
			FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));
			
			Vector vDatos = adm.getDatosFichero(idInstitucion);
			request.getSession().setAttribute("DATABACKUP", vDatos);	
			
			//Calculo si necesitara la fecha de cargo antes de generar los ficheros:	
			// ACG: no hacemos esta consulta que ralentiza enormemente la ejecucion de esta funcion. De esta forma en la jsp se
			// solicitará siempre la fecha de cargo con lo que en el fichero generado el campo fecha aparecerá siempre relleno
			String tieneFechaCargo = "SI";
			/*	String select = " SELECT "+FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.C_IDFACTURA+
			" FROM "+FacFacturaBean.T_NOMBRETABLA+
			" WHERE "+FacFacturaBean.C_IDINSTITUCION+"="+usr.getLocation()+
			" AND f_siga_estadosfactura ("+FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.C_IDFACTURA+") = 3";
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm(usuario);
			
			Vector v = facturaAdm.selectGenerico(select);
			*/
			
			/*	
			 String tieneFechaCargo = "NO";
			if (v!=null && v.size()>0)
				tieneFechaCargo = "SI";
	    */		
			request.setAttribute("TIENEFECHACARGO",tieneFechaCargo);
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return "abrir";
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
		
		//	String keyFichero 		= "facturacion.directorioBancosJava";				
		String directorioFisico = "facturacion.directorioFisicoPagosBancosJava";
		String directorio 		= "facturacion.directorioPagosBancosJava";
		String nombreFichero 	= "";
		String pathFichero		= "";
		//String idDisqueteCargos	= "";
		String idInstitucion	= "";
		
		try{		
			//Integer usuario = this.getUserName(request);
			
			FicheroBancarioPagosForm form 		= (FicheroBancarioPagosForm)formulario;
			//FacDisqueteCargosAdm adm 			= new FacDisqueteCargosAdm(usuario);
			//FacDisqueteCargosBean beanDisquete	= new FacDisqueteCargosBean();
			//FacFacturaAdm admFactura = new FacFacturaAdm(usuario);
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 	= new ReadProperties ("SIGA.properties");
			pathFichero 		= p.returnProperty(directorioFisico) + p.returnProperty(directorio);			
			//String nombreFichero 	= p.returnProperty(keyFichero);						
			
			Vector ocultos 			= new Vector();		
			ocultos 				= (Vector)form.getDatosTablaOcultos(0);			
			//idDisqueteCargos 		= (String)ocultos.elementAt(0);			
			nombreFichero 			= (String)ocultos.elementAt(1);	
			idInstitucion			= this.getIDInstitucion(request).toString();			
			
			pathFichero += File.separator + idInstitucion + File.separator + nombreFichero;
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		//response.setHeader("Content-disposition","attachment; filename=" + nombreFichero);
		//return sPrefijoDownload + pathFichero;
		
		request.setAttribute("nombreFichero", nombreFichero);
		request.setAttribute("rutaFichero", pathFichero);
		
		return "descargaFichero";
	}	
	
	/** 
	 *  Funcion que atiende la accion generarFichero. Genera los ficheros de Renegociación
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String generarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		String lenguaje = usr.getLanguage();
		
		String cont 				= "";
		String keyPath 				= "facturacion.directorioBancosOracle";			
		String pathFichero			= "";		
		String idInstitucion		= "";
		UserTransaction tx			= null;
		
		try{	
			tx = usr.getTransactionPesada(); 
			tx.begin();
			Integer usuario = this.getUserName(request);
			
			FicheroBancarioPagosForm form 		= (FicheroBancarioPagosForm)formulario;
			//FacDisqueteCargosAdm adm 			= new FacDisqueteCargosAdm(usuario);
			//FacDisqueteCargosBean beanDisquete	= new FacDisqueteCargosBean();	
			
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p 		= new ReadProperties ("SIGA.properties");
			pathFichero 			= p.returnProperty(keyPath);
			idInstitucion			= this.getIDInstitucion(request).toString();
			
			String sBarra 			= "";
			if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
			if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
			
			pathFichero += sBarra + idInstitucion;
			
			String fechaCargo = form.getFechaCargo();
			
			// Se envían a banco para su renegociación
			Object[] param_in_banco = new Object[7];
			param_in_banco[0] = idInstitucion;
			param_in_banco[1] = "";
			param_in_banco[2] = "";
			//Fecha de Cargo (DDMMAA):
			String fechaTMP = null;
			try {
				fechaTMP = fechaCargo.substring(0,2)+fechaCargo.substring(3,5)+fechaCargo.substring(8,10); 
			} catch (Exception e){
				fechaTMP = "";
			}
			param_in_banco[3] = fechaTMP;
			param_in_banco[4] = pathFichero;
			param_in_banco[5] = usuario.toString();
			param_in_banco[6] =this.getUserBean(request).getLanguage();
			
			String resultado[] = new String[3];
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
			String codretorno = resultado[1];        	
			if (!codretorno.equals("0")){
				throw new ClsExceptions ("Error al generar los Ficheros de Renegociación");
			}
			tx.commit();
			cont = resultado[0];
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx);  	  	
		}
		
		String mensaje = "facturacion.ficheroBancarioPagos.mensaje.generacionDisquetesOK";
		String[] datos = {cont};
		
		mensaje = UtilidadesString.getMensaje(mensaje, datos, lenguaje);
		
		request.setAttribute("mensaje",mensaje);	
		return "exitoConString"; 
	}
	
	protected String informeRemesa (MasterForm formulario, HttpServletRequest request) throws SIGAException 
	{
		FicheroBancarioPagosForm form = (FicheroBancarioPagosForm)formulario;

		Vector ocultos 			= (Vector)form.getDatosTablaOcultos(0);			
		String idDisqueteCargo 	= (String)ocultos.elementAt(0);	
		String idInstitucion	= this.getIDInstitucion(request).toString();			

		FacDisqueteCargosAdm adm = new FacDisqueteCargosAdm(this.getUserBean(request));
		Hashtable h = adm.getInformeRemesa(idInstitucion, idDisqueteCargo);
		if (h != null) {
			
			request.setAttribute("datosImpreso", h);
			
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


