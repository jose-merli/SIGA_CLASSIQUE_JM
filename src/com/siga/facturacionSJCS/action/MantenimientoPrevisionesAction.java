//VERSIONES:
//raul.ggonzalez 07-03-2005 Creacion
//

package com.siga.facturacionSJCS.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.FcsEstadosFacturacionBean;
import com.siga.beans.FcsFactEstadosFacturacionAdm;
import com.siga.beans.FcsFactEstadosFacturacionBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.MantenimientoPrevisionesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
* Clase action del caso de uso BUSCAR FACTURACION
* @author AtosOrigin 07-03-2005
*/
public class MantenimientoPrevisionesAction extends MasterAction {
	// Atributos
	/**   */

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
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("ejecutar")){
						// ejecutar
						mapDestino = ejecutar(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("configurarSalida")){
						// configurarSalida
						mapDestino = configurarSalida(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("descargas")){
						// descargas
						mapDestino = descargas(mapping, miForm, request, response);
					} else {
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacionSJCS"});
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
							HttpServletResponse response) throws SIGAException	{
		try {
			
			// con esto aseguramos que volvera a busqueda de previsiones
			request.getSession().setAttribute("SJCSBusquedaPrevisionesTipo","BP"); 
			
			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		// COMUN
		return "inicio";
	}

	
	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			// casting del formulario
			MantenimientoPrevisionesForm miFormulario = (MantenimientoPrevisionesForm)formulario;
			
			// busqueda de clientes
			FcsFacturacionJGAdm adm = new FcsFacturacionJGAdm(this.getUserBean(request));
			
			Vector resultado = null;
			
			
			Hashtable criterios = new Hashtable();
			if (miFormulario.getNombreB()!=null) criterios.put("nombre",miFormulario.getNombreB());
			//if (miFormulario.getIdInstitucion()!=null) criterios.put("idInstitucion",miFormulario.getIdInstitucion());
			if (miFormulario.getFechaDesdeB()!=null) criterios.put("fechaDesde",miFormulario.getFechaDesdeB());
			if (miFormulario.getFechaHastaB()!=null) criterios.put("fechaHasta",miFormulario.getFechaHastaB());
			if (miFormulario.getHitosB()!=null) criterios.put("hito",miFormulario.getHitosB());
			
			resultado = adm.getPrevisiones(criterios,user.getLocation());

			request.setAttribute("SJCSResultadoBusquedaPrevisiones",resultado);
			
			destino="resultado";
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
	}


	/** 
	 *  Funcion que atiende la accion editar.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
	 String forward="";
	 
	 try {
	 	
		String accion = "edicion";

		// Obtengo los datos del formulario		
		MantenimientoPrevisionesForm miForm = (MantenimientoPrevisionesForm)formulario;		
		Vector fila = miForm.getDatosTablaOcultos(0);
		String idFacturacion = "";
		String idInstitucionRegistro = "";
		//String idInstitucionUsuario = "";
		if (fila==null || fila.size()==0) {
			idFacturacion = miForm.getIdFacturacion();
			idInstitucionRegistro = miForm.getIdInstitucion();
		} else {
			idFacturacion = (String)fila.get(0);
			idInstitucionRegistro = (String)fila.get(1);
			//idInstitucionUsuario = (String)fila.get(2);
		}	
		
		//Creamos una clausula where que nos servirá para consultar por idFacturacion e idInstitucion
		String consultaFact = " where idInstitucion = " + idInstitucionRegistro +" and idFacturacion =" + idFacturacion + " ";
		
		//Traemos de base de datos la factura seleccionada, con los datos recogidos de la pestanha
		FcsFacturacionJGAdm facturaAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
		FcsFacturacionJGBean facturaBean = (FcsFacturacionJGBean)((Vector)facturaAdm.select(consultaFact)).get(0);

		//Consultamos los criterios de facturacion
		Vector vHito = (Vector)facturaAdm.getCriteriosFacturacion(idInstitucionRegistro, idFacturacion);
		
		//Pasamos la factura seleccionada ,el modo, el importe de la partida, 
		//el vector con los hitos a facturar y el vector de los grupos de facturacion

		Hashtable datosEntrada = new Hashtable();
		datosEntrada.put(FcsFacturacionJGBean.C_FECHADESDE,facturaBean.getFechaDesde());
		datosEntrada.put(FcsFacturacionJGBean.C_FECHAHASTA,facturaBean.getFechaHasta());
		datosEntrada.put(FcsFacturacionJGBean.C_NOMBRE,facturaBean.getNombre());
		datosEntrada.put(FcsFacturacionJGBean.C_IDFACTURACION,facturaBean.getIdFacturacion().toString());
		datosEntrada.put(FcsFacturacionJGBean.C_IDINSTITUCION,facturaBean.getIdInstitucion().toString());

		request.getSession().setAttribute("DATABACKUP",datosEntrada);
		
		request.setAttribute("datosFactura",facturaBean);
		request.setAttribute("modo",accion);
		request.setAttribute("idInstitucion",idInstitucionRegistro);
		request.setAttribute("idFacturacion",idFacturacion);
		
		//para el iframe de abajo pasamos el modo por session, se borrará en la jsp
		request.getSession().setAttribute("vHito",vHito);
		forward="abrir";
	 } 	
	 catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	 }
	 
	 return forward;
	}

	/** 
	 *  Funcion que atiende la accion ver.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
	 String forward="";
	 
	 try {
		String accion = "consulta";
		
		// Obtengo los datos del formulario		
		MantenimientoPrevisionesForm miForm = (MantenimientoPrevisionesForm)formulario;		
		Vector fila = miForm.getDatosTablaOcultos(0);
		String idFacturacion = (String)fila.get(0);
		String idInstitucionRegistro = (String)fila.get(1);
		//String idInstitucionUsuario = (String)fila.get(2);
		
		//Creamos una clausula where que nos servirá para consultar por idFacturacion e idInstitucion
		String consultaFact = " where idInstitucion = " + idInstitucionRegistro +" and idFacturacion =" + idFacturacion + " ";
		
		//Traemos de base de datos la factura seleccionada, con los datos recogidos de la pestanha
		FcsFacturacionJGAdm facturaAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
		FcsFacturacionJGBean facturaBean = (FcsFacturacionJGBean)((Vector)facturaAdm.select(consultaFact)).get(0);
		
		
		//Consultamos los criterios de facturacion
		Vector vHito = (Vector)facturaAdm.getCriteriosFacturacion(idInstitucionRegistro, idFacturacion);
		
		//Pasamos la factura seleccionada ,el modo, el importe de la partida, 
		//el vector con los hitos a facturar y el vector de los grupos de facturacion

		request.setAttribute("datosFactura",facturaBean);
		request.setAttribute("modo",accion);
		request.setAttribute("idInstitucion",idInstitucionRegistro);
		request.setAttribute("idFacturacion",idFacturacion);
		
		//para el iframe de abajo pasamos el modo por session, se borrará en la jsp
		request.getSession().setAttribute("vHito",vHito);
		forward="abrir";
	 } 	
	 catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	 }
	 
	 return forward;
	}


	/** 
	 * Funcion que atiende la accion nuevo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		 String forward="";
		 try {
		 	
			//Recuperamos el USRBEAN
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			String accion = "nuevo";
			
			//pasamos los identificadores de la facturacion y la institucion
			request.setAttribute("idInstitucion",usr.getLocation());
			request.setAttribute("modo",accion);
			forward = "abrir";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		 }
		 return forward;
	}

	/** 
	 * Funcion que implementa la accion ejecutar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ejecutar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String salida = "";
		UserTransaction tx = null;

		try 
		{ 
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");

			tx = usr.getTransactionPesada();
			tx.begin();
			
			MantenimientoPrevisionesForm miform = (MantenimientoPrevisionesForm)formulario;
			String idFacturacion = miform.getIdFacturacion();
			String idInstitucion = usr.getLocation(); 
			FcsFacturacionJGAdm admFac = new FcsFacturacionJGAdm(this.getUserBean(request));
			FcsFactEstadosFacturacionAdm admEstado = new FcsFactEstadosFacturacionAdm(this.getUserBean(request));
			Hashtable criterios = new Hashtable();
			criterios.put(FcsFacturacionJGBean.C_IDINSTITUCION,idInstitucion);
			criterios.put(FcsFacturacionJGBean.C_IDFACTURACION,idFacturacion);
			Vector v = (Vector)admFac.select(criterios);
			if (v!=null && v.size()>0) {
				FcsFacturacionJGBean beanFac = (FcsFacturacionJGBean)v.get(0);
				Hashtable estado = admFac.getEstadoFacturacion(idInstitucion,idFacturacion);
				String idEstado = (String) estado.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION);
				if (!idEstado.equals(new Integer(ClsConstants.ESTADO_FACTURACION_ABIERTA).toString())) {
					throw new SIGAException("messages.factSJCS.error.estadoNoCorrecto");
				} 
				else {

					// proceso de facturacion
					double  importeTotal = 0;
					Double  importeOficio = null, 
							importeGuardia = null, 
							importeSOJ = null,  
							importeEJG = null;

					//////////////////////////////////
					// TURNOS DE OFICIO rgg 16-03-2005
					
					Object[] param_in_facturacion = new Object[3];
					param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
					param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION 
					param_in_facturacion[2] = beanFac.getUsuMod().toString();        // USUMODIFICACION
					
		        	String resultado[] = new String[3];
		        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_TURNOS_OFI(?,?,?,?,?,?)}", 3, param_in_facturacion);
		        	if (!resultado[1].equalsIgnoreCase("0")) {
		        		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
		        		throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturación de Turnos de Oficio");
		        	}
		        	importeOficio = new Double(resultado[0]);
	        		importeTotal += importeOficio.doubleValue();


	        		//////////////////////////////////
					// GUARDIAS rgg 22-03-2005
					
		        	param_in_facturacion = new Object[3];
		        	param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
		        	param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION
					param_in_facturacion[2] = beanFac.getUsuMod().toString(); // USUMODIFICACION
					
		        	resultado = new String[3];
		        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_GUARDIAS(?,?,?,?,?,?)}", 3, param_in_facturacion);
		        	if (!resultado[1].equalsIgnoreCase("0")) {
		        		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
		        		throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturación de Guardias");
		        	} 
		        	importeGuardia = new Double(resultado[0]);
	        		importeTotal += importeGuardia.doubleValue();

		        	
					//////////////////////////////////
					// EXPEDIENTES SOJ rgg 22-03-2005
					
		        	param_in_facturacion = new Object[3];
		        	param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
		        	param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION
					param_in_facturacion[2] = beanFac.getUsuMod().toString(); 		 // USUMODIFICACION
					
		        	resultado = new String[3];
		        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_SOJ(?,?,?,?,?,?)}", 3, param_in_facturacion);
		        	if (!resultado[1].equalsIgnoreCase("0")) {
		        		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
		        		throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturación de Expedientes de Orientación Jurídica");
		        	} 
		        	importeSOJ = new Double(resultado[0]);
	        		importeTotal += importeSOJ.doubleValue();
					
	        		
					//////////////////////////////////
					// EXPEDIENTES EJG rgg 22-03-2005
		        	
		        	param_in_facturacion = new Object[3];
		        	param_in_facturacion[0] = beanFac.getIdInstitucion().toString(); // IDINSTITUCION
		        	param_in_facturacion[1] = beanFac.getIdFacturacion().toString(); // IDFACTURACION
					param_in_facturacion[2] = beanFac.getUsuMod().toString(); 		 // USUMODIFICACION
					
		        	resultado = new String[3];
		        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_FACTURAR_EJG (?,?,?,?,?,?)}", 3, param_in_facturacion);
		        	if (!resultado[1].equalsIgnoreCase("0")) {
		        		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[2],3);
		        		throw new ClsExceptions ("Ha ocurrido un error al ejecutar la facturación de  Expedientes de Justicia Gratuita");
		        	} 

		        	importeEJG = new Double(resultado[0]);
	        		importeTotal += importeEJG.doubleValue();

		        	//////////////////////////////////
					// ACTUALIZO EL TOTAL
	        		beanFac.setImporteEJG(importeEJG);
	        		beanFac.setImporteGuardia(importeGuardia);
	        		beanFac.setImporteOficio(importeOficio);
	        		beanFac.setImporteSOJ(importeSOJ);
		        	beanFac.setImporteTotal(new Double(importeTotal));
		        	if (!admFac.update(beanFac)) {
		        		throw new SIGAException(admFac.getError());
		        	}
		        	
					//////////////////////////////////
					// cambio de estado
					FcsFactEstadosFacturacionBean beanEstado = new FcsFactEstadosFacturacionBean();
					beanEstado.setIdInstitucion(new Integer(idInstitucion));
					beanEstado.setIdFacturacion(new Integer(idFacturacion));
					beanEstado.setIdEstadoFacturacion(new Integer(ClsConstants.ESTADO_FACTURACION_EJECUTADA));
					beanEstado.setFechaEstado("SYSDATE");
					admEstado.insert(beanEstado);
					
					// Exportacion de datos a EXCEL
					UtilidadesFacturacionSJCS.exportarDatosFacturacion(new Integer(idInstitucion), new Integer(idFacturacion), usr);
				}
			}
			tx.rollback();
			salida = this.exito("messages.updated.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}					
		return salida;
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
		
  	  UserTransaction tx = null;
  	  String forward="error";
	  
  	  try {
	  	
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		MantenimientoPrevisionesForm miform = (MantenimientoPrevisionesForm)formulario;
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		FcsFactEstadosFacturacionAdm estadosAdm = new FcsFactEstadosFacturacionAdm(this.getUserBean(request));
		Hashtable datos = (Hashtable)miform.getDatos();
		
		//calculamos el nuevo idFacturacion
		datos.put("IDINSTITUCION", (String)usr.getLocation());
		facturacionAdm.prepararInsert(datos);
		
		//ponemos el campo regularizacion a false
		datos.put(FcsFacturacionJGBean.C_REGULARIZACION,ClsConstants.DB_FALSE);
		datos.put(FcsFacturacionJGBean.C_PREVISION,ClsConstants.DB_TRUE);
		
		//comprobamos que la facturacion no se solape con otra ya existente
		FcsFacturacionJGBean facturaPrueba  = new FcsFacturacionJGBean ();
		facturaPrueba.setFechaDesde((String)miform.getFechaDesde());
		facturaPrueba.setFechaHasta((String)miform.getFechaHasta());
		facturaPrueba.setIdInstitucion(Integer.valueOf((String)usr.getLocation()));
		facturaPrueba.setIdFacturacion(Integer.valueOf((String)datos.get("IDFACTURACION")));
		
		//preparamos la insercion en estados fact
		Hashtable estado = new Hashtable();
		estado.put(FcsFactEstadosFacturacionBean.C_IDFACTURACION , datos.get("IDFACTURACION"));
		estado.put(FcsFactEstadosFacturacionBean.C_FECHAESTADO , "sysdate");
		estado.put(FcsFactEstadosFacturacionBean.C_FECHAMODIFICACION, "sysdate");
		estado.put(FcsFactEstadosFacturacionBean.C_IDESTADOFACTURACION, String.valueOf(ClsConstants.ESTADO_FACTURACION_ABIERTA));
		estado.put(FcsFactEstadosFacturacionBean.C_IDINSTITUCION, usr.getLocation());
		estado.put(FcsFactEstadosFacturacionBean.C_USUMODIFICACION , usr.getUserName());
		tx = usr.getTransaction();
		tx.begin();
			
		if (!facturacionAdm.insert(datos)) {
			throw new SIGAException(facturacionAdm.getError());
		}
		if (!estadosAdm.insert(estado)) {
			throw new SIGAException(estadosAdm.getError());
		}
			
		tx.commit();

		request.setAttribute("idFacturacion",(String)datos.get("IDFACTURACION"));
		request.setAttribute("idInstitucion",(String)miform.getIdInstitucion());

		// request.setAttribute("mensaje","messages.updated.success");
		forward = "exitoInsercion";
	  } 
	  catch (Exception e)
 	  {
		throwExcp("messages.general.error", new String[] {"modulo.facturacionSJCS"},e,tx);
	  }
	  return forward;
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
		
		String forward="";
    	UserTransaction tx = null;
		
    	try 
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");

			MantenimientoPrevisionesForm miform = (MantenimientoPrevisionesForm)formulario;
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm(this.getUserBean(request));

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			Hashtable hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");

			
			tx = usr.getTransaction();
			tx.begin();
						
			//preparamos la nueva facturacion
			miform.setFechaDesde(GstDate.getApplicationFormatDate(usr.getLanguage(),miform.getFechaDesde()));
			miform.setFechaHasta(GstDate.getApplicationFormatDate(usr.getLanguage(),miform.getFechaHasta()));
			
			FcsFacturacionJGBean beanNuevo = new FcsFacturacionJGBean();
			beanNuevo.setFechaDesde(miform.getFechaDesde());
			beanNuevo.setFechaHasta(miform.getFechaHasta());
			beanNuevo.setNombre(miform.getNombre());
			beanNuevo.setIdFacturacion(new Integer(miform.getIdFacturacion()));
			beanNuevo.setIdInstitucion(new Integer(miform.getIdInstitucion()));
				
			beanNuevo.setOriginalHash(hashOriginal);
			
			//actualizamos
			if (!facturacionAdm.update(beanNuevo)) {
				throw new SIGAException(facturacionAdm.getError());
			}
			
			tx.commit();
			
			forward = exitoRefresco("messages.updated.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx); 
		}
		return forward;
	}
	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String result = "error";		
		boolean correcto = false;		
		UserTransaction tx = null;
		
		try{
			//Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Obtengo los datos del formulario		
			MantenimientoPrevisionesForm miForm = (MantenimientoPrevisionesForm)formulario;		
			Vector fila = miForm.getDatosTablaOcultos(0);
			String idFacturacion = (String)fila.get(0);
			String idInstitucionRegistro = (String)fila.get(1);
			//String idInstitucionUsuario = (String)fila.get(2);

			//FcsFacturacionJGAdm adm = new FcsFacturacionJGAdm(this.getUserBean(request));

			// Recupero el nombre de los ficheros asociados a la facturacion
			Hashtable nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(new Integer(idInstitucionRegistro), new Integer(idFacturacion), this.getUserBean(request));

			//	Comienzo control de transacciones 
			tx = usr.getTransactionPesada();			
			tx.begin();

			Object[] param_in = new Object[2];
	 		String resultadoPl[] = new String[2];

	 		//Parametros de entrada del PL
			param_in[0] = idInstitucionRegistro;			
			param_in[1] = idFacturacion;
	 		resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_BORRAR_FACTURACION (?,?,?,?)}", 2, param_in);
			correcto = ((String)resultadoPl[0]).equals("0");
			if (!correcto) 
				throw new SIGAException("messages.deleted.error");
			
			// Hago Commit 
			tx.commit();

			// borrado fisico de ficheros del servidor web
			UtilidadesFacturacionSJCS.borrarFicheros(new Integer(idInstitucionRegistro), nombreFicheros, this.getUserBean(request));

			result = exitoRefresco("messages.deleted.success",request);
		} 	
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		return result;
	}

	/** 
	 *  Funcion que implementa la accion configurarSalida
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String configurarSalida(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";		
		
		try{

			result = "configurarSalida";

		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 
		 return result;
		 
	}

	/** 
	 *  Funcion que implementa la accion descargas
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String descargas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "error";		
		try {
			MantenimientoPrevisionesForm miForm = (MantenimientoPrevisionesForm) formulario; 
			request.setAttribute("idFacturacion",miForm.getIdFacturacion());
			request.setAttribute("idInstitucion",miForm.getIdInstitucion());
			result = "descargas";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return result;
	}
}
