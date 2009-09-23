/* VERSIONES:
 * ruben.fernandez - 09/03/2005 - Creacion
 * Modificado por david.sanchez para incluir diversos cambios.
 */

/**
 * Clase action para la visualizacion y mantenimiento de los datos de facturación para una Institución.<br/>
 * Gestiona la edicion, consulta y mantenimiento de la ejecución de facturaciones.  
 */

package com.siga.facturacionSJCS.action;


import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.beans.*;
import com.siga.facturacionSJCS.form.DatosDetalleFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class DatosDetalleFacturacionAction extends MasterAction {

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
					} else if (accion.equalsIgnoreCase("verDetalle")){
						// verDetalle
						mapDestino = verDetalle(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("descargaFicheros")){
							// verDetalle
							mapDestino = descargaFicheros(mapping, miForm, request, response);
						}
					else {
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

	
	
	private String descargaFicheros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		String result = "error";		
		try {
			DatosDetalleFacturacionForm miForm = (DatosDetalleFacturacionForm) formulario; 
			request.setAttribute("idFacturacion",miForm.getIdFacturacion());
			request.setAttribute("idInstitucion",miForm.getIdInstitucion());
			result = "descargas";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return result;
	}



	/** 
	 *  Funcion que atiende la accion abrirAvanzada.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String forward="inicio";
		//Recuperamos el USRBEAN
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//Recogemos los parámetros de las pestanhas 
		String idInstitucion = (String)request.getParameter("idInstitucion");
		String idFacturacion = (String)request.getParameter("idFacturacion");
		String accion = (String)request.getParameter("accion");
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//vector que pasaremos como resultado a la jsp
		Vector resultado = new Vector();
		
		//vector con el nombre de los hitos generales
		Vector hitos = new Vector();
		Hashtable h=new Hashtable();
		
		//variable para recoger el nombre del colegio
		String nombreInstitucion = "";
		
		//Creamos una clausula where que nos servirá para consultar por idFacturacion e idInstitucion
		String consultaFact = " where idInstitucion = " + idInstitucion +" and idFacturacion =" + idFacturacion + " ";
		
		//variable para saber si la factura está en estado abierta, con lo que no habria detalle
		boolean abierta = false;
		
		//comprobamos que el estado no sea ABIERTO
		Hashtable hash = new Hashtable();
		try{
			FcsFacturacionJGAdm facturaAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			if (!idFacturacion.equals(""))
				hash = (Hashtable)facturaAdm.getEstadoFacturacion(idInstitucion,idFacturacion);
			else
				hash = new Hashtable();
			if (((String)hash.get(FcsEstadosFacturacionBean.C_IDESTADOFACTURACION)).equalsIgnoreCase("10"))
				abierta = true;
		}catch(Exception e){
			hash = new Hashtable();	
		}
		
		try{
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			//nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
			//INC-3098: se le estaba pasando la institucion del CGAE en lugar de la del colegio que se quiere visualizar los datos.
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(idInstitucion.toString());
		}catch(ClsExceptions e){
			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
		}
		
		if (!abierta){
			try 
			{
				//Recuperamos los importes de la facturacion
				//resultado = (Vector)this.getDetalleFacturacion(usr.getLocation(),idFacturacion , request);
//				  INC-3098: se le estaba pasando la institucion del CGAE en lugar de la del colegio que se quiere visualizar los datos.
				resultado = (Vector)this.getDetalleFacturacion(idInstitucion,idFacturacion , request);
				// obtenemos la descripcion de los hitos
				FcsHitoGeneralAdm hitosAdm=new FcsHitoGeneralAdm(this.getUserBean(request));
				hitos = (Vector)hitosAdm.select();
				for(int i=0;i<hitos.size();i++)
				{
					FcsHitoGeneralBean bean=(FcsHitoGeneralBean)hitos.elementAt(i);
					h.put(bean.getIdHitoGeneral().toString(),bean.getDescripcion());
				}
				
					
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: DatosDetalleFacturacionAction"+e.getMessage(),e,3);
				resultado = new Vector();
			}
			catch(Exception e)
			{	
				ClsLogging.writeFileLogError("Error: DatosDetalleFacturacionAction"+e.getMessage(),e,3);
				throwExcp("Error: DatosDetalleFacturacionAction",e,null);
			}
		
			//pasamos el resultado por la request, y tambien el nombre de la institución, y 
			request.setAttribute("resultado",resultado);
			request.setAttribute("hitos",h);
		}else{
			//si esta en estado abierta no se ha consultado el detalle
			request.setAttribute("estado","abierta");
		}
		//pasamos los identificadores de la facturacion y la institucion
		request.setAttribute("idFacturacion",idFacturacion);
		request.setAttribute("idInstitucion",idInstitucion);
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		return forward;
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
		return mapSinDesarrollar;
	}

	
	/** 
	 *  Funcion que implementa la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		//Recogemos el formulario
		DatosDetalleFacturacionForm miform = (DatosDetalleFacturacionForm)formulario;

		//recuperamos el UsrBean
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		
		//recuperamos el identificador de la facturacion y de la institucion
		String idInstitucion = (String)miform.getIdInstitucion();
		String idFacturacion = (String)miform.getIdFacturacion();
		
		//variable para recoger el nombre del colegio
		String nombreInstitucion = "";
		
		try{
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
		}catch(ClsExceptions e){
			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio",e,3);
		}

		
		//recuperamos la fila seleccionada y el campo oculto(idPersona)
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		String idPersona = (String)ocultos.get(0);
		String nColegiado = (String)visibles.get(0);
		String nombreColegiado = (String)visibles.get(1);
		//String importeTotal = (String)visibles.get(2);
		
		//consultamos los detalles de la facturacion para esa persona
		FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
		Vector resultado = (Vector)facturacionAdm.getDetallePorColegiado(idInstitucion,idFacturacion,idPersona);
		
		// para el scroll de la ventana modal
		//request.getSession().setAttribute("ScrollModal","S");
		
		//devolvemos el vector con los resultados y el nombre del cliente
		request.setAttribute("resultado",resultado);
		request.setAttribute("nombreColegiado",nombreColegiado);
		request.setAttribute("numeroColegiado",nColegiado);
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		return "consulta";
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
		return mapSinDesarrollar;
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
		
		return mapSinDesarrollar;
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
		
		return mapSinDesarrollar;
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
		
		return mapSinDesarrollar;					
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

		return mapSinDesarrollar;
	}
	
	/**
	 * Funcion que obtiene los idPersona de los colegiados que intervienen en una facturacion,
	 * y consulta los detalles de facturacion para cada persona,
	 * y por cada persona, obtiene:
	 * 
	 * -IDPERSONA
	 * -NCOLEGIADO
	 * -NOMBRECOLEGIADO
	 * -IMPORTETOTAL
	 * 
	 * @param idInstitucion
	 * @param isFacturacion
	 * @return
	 */
	/*protected Vector getDetalleFacturacion (String idInstitucion, String idFacturacion, HttpServletRequest request) throws ClsExceptions
	{
		//resultado final, vector de Hashtables
		Vector resultado = new Vector();
		
		//vector con todos los colegiados que intervendrán en la facturacion
		Vector colegiados = new Vector();
		
		//recogemos todos los idPersona
		try{
			FcsFacturacionJGAdm factAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			if (!idFacturacion.equals(""))				
				colegiados = (Vector)factAdm.getColegiadosFacturables(idInstitucion, idFacturacion);
			else
				colegiados = new Vector();
		}catch(Exception e){
			colegiados = new Vector();
			com.atos.utils.ClsLogging.writeFileLogError("Consulta en DatosDetalleFacturacionAction.getDetalleFacturacion SQL:"+e.getMessage(),e, 3);
		}
		
		//variables para recorrer el vector de los idPersonas
		Hashtable Persona = new Hashtable();
		String idPersona = "";

		//variables para los importes en detalle de la facturacion
		String  importeActuacionDesigna="", importeGuardiasPresenciales="", importeAsistencias="", importeActuaciones="", importeSoj="", importeEjg="";
		
		//variables resultado para cada colegiado
		String importeTotal="", nombreColegiado="", nColegiado="";
				
		//recorrer el vector con todos los colegiados
		for (int cont=0; cont<colegiados.size(); cont++)
		{
			//cogemos el idPersona de la Persona
			Persona = (Hashtable)colegiados.get(cont);
			idPersona = (String)Persona.get("IDPERSONA");
			
			//calculamos los importes parciales
				//para ActuacionesDesignas
				try{
					FcsFactActuacionDesignaAdm actDesAdm = new FcsFactActuacionDesignaAdm (this.getUserBean(request));
					importeActuacionDesigna = (String)actDesAdm.getImporteFacturado(idInstitucion,idFacturacion,idPersona);
				}catch(Exception e){
					importeActuacionDesigna = "0";
				}
				//para guardias presenciales
				try{
					FcsFactGuardiasColegiadoAdm guardAdm = new FcsFactGuardiasColegiadoAdm (this.getUserBean(request));
					importeGuardiasPresenciales = (String)guardAdm.getImporteFacturado(idInstitucion,idFacturacion,idPersona);
				}catch(Exception e){
					importeGuardiasPresenciales = "0";
				}
				//para asistencias
				try{
					FcsFactAsistenciaAdm asisAdm = new FcsFactAsistenciaAdm (this.getUserBean(request));
					importeAsistencias = (String)asisAdm.getImporteFacturado(idInstitucion,idFacturacion,idPersona);
				}catch(Exception e){
					importeAsistencias = "0";
				}
				//para actuaciones
				try{
					FcsFactActuacionAsistenciaAdm actAdm = new FcsFactActuacionAsistenciaAdm (this.getUserBean(request));
					importeActuaciones = (String)actAdm.getImporteFacturado(idInstitucion,idFacturacion,idPersona);
				}catch(Exception e){
					importeActuaciones= "0";
				}
				//para SOJ
				try{
					FcsFactSojAdm sojAdm = new FcsFactSojAdm (this.getUserBean(request));
					importeSoj = (String)sojAdm.getImporteFacturado(idInstitucion,idFacturacion,idPersona);
				}catch(Exception e){
					importeSoj = "0";
				}
				//para EJG
				try{
					FcsFactEjgAdm ejgAdm = new FcsFactEjgAdm (this.getUserBean(request));
					importeEjg = (String)ejgAdm.getImporteFacturado(idInstitucion,idFacturacion,idPersona);
				}catch(Exception e){
					importeEjg = "0";
				}
			
			//calculamos el importe Total
			float contTot = 0;
			Float contActDes = new Float(importeActuacionDesigna);
			Float contGuar = new Float(importeGuardiasPresenciales);
			Float contAsis = new Float(importeAsistencias);
			Float contAct = new Float(importeActuaciones);
			Float contSoj = new Float(importeSoj);
			Float contEjg = new Float(importeEjg);
			contTot += contActDes.floatValue();
			contTot += contGuar.floatValue();
			contTot += contAsis.floatValue();
			contTot += contAct.floatValue();
			contTot += contSoj.floatValue();
			contTot += contEjg.floatValue();
			importeTotal = String.valueOf(contTot);
			
			//recuperamos el nombre del colegiado
			CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
			try{
				nombreColegiado = (String)personaAdm.obtenerNombreApellidos(idPersona);
			}catch(Exception e){
				nombreColegiado = "";
				com.atos.utils.ClsLogging.writeFileLogError("DatosDetalleFacturacionAction: Error al intentar recuperar el nombre del usuario con idPersona"+idPersona+"."+e.getMessage(), e, 3);
			}
			
			//recuperamos el nColegiado
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			try{
				Long idPersonaLong = new Long(idPersona);
				Integer idInsititucionInteger = new Integer(idInstitucion);
				CenColegiadoBean colegiadoBean = (CenColegiadoBean)colegiadoAdm.getDatosColegiales(idPersonaLong, idInsititucionInteger);
				nColegiado = colegiadoAdm.getIdentificadorColegiado(colegiadoBean);
			}catch(Exception e){
				nColegiado = "";
				com.atos.utils.ClsLogging.writeFileLogError("DatosDetalleFacturacionAction: Error al intentar recuperar el nombre del usuario con idPersona"+idPersona+"."+e.getMessage(), e,3);
			}
			
			//nuevo hashtable para meterlo en el Vector resultado
			Hashtable personaActual = new Hashtable ();
			personaActual.put("IDPERSONA",idPersona);
			personaActual.put("IMPORTETOTAL",importeTotal);
			personaActual.put("NCOLEGIADO",nColegiado);
			personaActual.put("NOMBRECOLEGIADO",nombreColegiado);
			
			//anhadimos el registro
			resultado.add(cont, personaActual);
		}
		return resultado;
	}*/
	protected Vector getDetalleFacturacion (String idInstitucion, String idFacturacion, HttpServletRequest request) throws ClsExceptions
	{
		//resultado final, vector de Hashtables
		Vector resultado = new Vector();
		FcsFacturacionJGAdm facturacionJGAdm = new FcsFacturacionJGAdm(this.getUserBean(request));
		
		try{
			
			// obtenemos los valores de los importes de oficio, guardia, soj y ejg
			Hashtable claves = new Hashtable();
			claves.put(FcsFacturacionJGBean.C_IDFACTURACION,idFacturacion);
			claves.put(FcsFacturacionJGBean.C_IDINSTITUCION,idInstitucion);
			resultado = facturacionJGAdm.selectByPK(claves);
			
			
		}catch(Exception e){
			com.atos.utils.ClsLogging.writeFileLogError("Consulta en DatosDetalleFacturacionAction.getDetalleFacturacion SQL:"+e.getMessage(),e, 3);
		}
		
		
		return resultado;
	}
	
	/** 
	 * Funcion que implementa la accion verDetalle
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String verDetalle (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String salida = "";
		
		try 
		{ 
			
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");

			double totalTurnos = 0;
			double totalGuardias = 0;
			double totalEJG = 0;
			double totalSOJ = 0;
			double total = 0;
			

			DatosDetalleFacturacionForm miform = (DatosDetalleFacturacionForm)formulario;
			String idFacturacion = miform.getIdFacturacion();
			String idInstitucion = miform.getIdInstitucion(); 
			
	
			//////////////////////////////////
			// TURNOS DE OFICIO rgg 10-05-2005
			
			Object[] param_in_facturacion = new Object[2];
			// parametros de entrada
        	param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
        	param_in_facturacion[1] = idFacturacion; // IDFACTURACION
        	
        	String resultado[] = new String[3];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_IMPORTE_TURNOS(?,?,?,?,?)}", 3, param_in_facturacion);
        	String codretorno = (String)resultado[1];
        	if (!codretorno.equals("0"))
        	{
        		//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
        		throw new ClsExceptions ("Error al mostrar el detalle."+
        				"\nError en PL = "+(String)resultado[2]);
        	} else {		        		
        		Double aux = new Double((String)resultado[0]);
        		totalTurnos = aux.doubleValue();
        	}
        	
			//////////////////////////////////
			// TURNOS GUARDIAS rgg 10-05-2005
			
			param_in_facturacion = new Object[2];
			// parametros de entrada
        	param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
        	param_in_facturacion[1] = idFacturacion; // IDFACTURACION
        	
        	resultado = new String[3];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_IMPORTE_GUAR_COL(?,?,?,?,?)}", 3, param_in_facturacion);
        	codretorno = (String)resultado[1];
        	if (!codretorno.equals("0"))
        	{
        		//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
        		throw new ClsExceptions ("Error al mostrar el detalle."+
        				"\nError en PL = "+(String)resultado[2]);
        	} else {		        		
        		Double aux = new Double((String)resultado[0]);
        		totalGuardias += aux.doubleValue();
        	}
        	
			param_in_facturacion = new Object[2];
			// parametros de entrada
        	param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
        	param_in_facturacion[1] = idFacturacion; // IDFACTURACION
        	
        	resultado = new String[3];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_IMPORTE_ACT_ASIST(?,?,?,?,?)}", 3, param_in_facturacion);
        	codretorno = (String)resultado[1];
        	if (!codretorno.equals("0"))
        	{
        		//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
        		throw new ClsExceptions ("Error al mostrar el detalle."+
        				"\nError en PL = "+(String)resultado[2]);
        	} else {		        		
        		Double aux = new Double((String)resultado[0]);
        		totalGuardias += aux.doubleValue();
        	}
        	
			param_in_facturacion = new Object[2];
			// parametros de entrada
        	param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
        	param_in_facturacion[1] = idFacturacion; // IDFACTURACION
        	
        	resultado = new String[3];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_IMPORTE_ASISTENCIAS(?,?,?,?,?)}", 3, param_in_facturacion);
        	codretorno = (String)resultado[1];
        	if (!codretorno.equals("0"))
        	{
        		//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
        		throw new ClsExceptions ("Error al mostrar el detalle."+
        				"\nError en PL = "+(String)resultado[2]);
        	} else {		        		
        		Double aux = new Double((String)resultado[0]);
        		totalGuardias += aux.doubleValue();
        	}
        	
			//////////////////////////////////
			// EJG rgg 10-05-2005
			
			param_in_facturacion = new Object[2];
			// parametros de entrada
        	param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
        	param_in_facturacion[1] = idFacturacion; // IDFACTURACION
        	
        	resultado = new String[4];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_IMPORTE_EJG(?,?,?,?,?,?)}", 4, param_in_facturacion);
        	codretorno = (String)resultado[2];
        	if (!codretorno.equals("0")){
        		//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
        		throw new ClsExceptions ("Error al mostrar el detalle."+
        				"\nError en PL = "+(String)resultado[2]);
        	} else {		        		
        		Double aux = new Double((String)resultado[0]);
        		totalEJG = aux.doubleValue();
        		aux = new Double((String)resultado[1]);
        		totalEJG += aux.doubleValue();
        	}
        	
			//////////////////////////////////
			// SOJ rgg 10-05-2005
			
			param_in_facturacion = new Object[2];
			// parametros de entrada
        	param_in_facturacion[0] = idInstitucion; // IDINSTITUCION
        	param_in_facturacion[1] = idFacturacion; // IDFACTURACION
        	
        	resultado = new String[4];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS.PROC_FCS_IMPORTE_SOJ(?,?,?,?,?,?)}", 4, param_in_facturacion);
        	codretorno = (String)resultado[2];
        	if (!codretorno.equals("0")){
        		//ClsLogging.writeFileLogError("ERROR EN PL = "+(String)resultado[2],3);
        		throw new ClsExceptions ("Error al mostrar el detalle."+
        				"\nError en PL = "+(String)resultado[2]);
        	} else {		        		
        		Double aux = new Double((String)resultado[0]);
        		totalSOJ = aux.doubleValue();
        		aux = new Double((String)resultado[1]);
        		totalSOJ += aux.doubleValue();
        	}
        	
			total = totalTurnos + totalGuardias + totalEJG + totalSOJ; 
			
			Hashtable valores = new Hashtable();
			valores.put("turnos", String.valueOf(UtilidadesNumero.redondea(totalTurnos,2)));
			valores.put("guardias", String.valueOf(UtilidadesNumero.redondea(totalGuardias,2)));
			valores.put("ejg", String.valueOf(UtilidadesNumero.redondea(totalEJG,2)));
			valores.put("soj", String.valueOf(UtilidadesNumero.redondea(totalSOJ,2)));
			valores.put("total", String.valueOf(UtilidadesNumero.redondea(total,2)));

			request.setAttribute("valoresFacturacion",valores);
			
			String nombreInstitucion = "";
			try{
				//Consultamos el nombre de la institucion
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
				nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
			}catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
			}
			
			//pasamos el nombre de la institución, y los identificadores del pago y la institucion
			request.setAttribute("nombreInstitucion",nombreInstitucion);
			
			
			salida = "verDetalle";
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}					
		return salida;
	}

}
