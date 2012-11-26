/*
 * Created on Mar 21, 2005
 * @author emilio.grau
 * 
 */
package com.siga.facturacion.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvDocumentosAdm;
import com.siga.beans.EnvDocumentosBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.FacLineaFacturaBean;
import com.siga.certificados.Plantilla;
import com.siga.facturacion.form.ConsultaMorososForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
 * Action para la consulta de morosos.
 */
public class ConsultaMorososAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={FacFacturaBean.C_IDFACTURA,FacFacturaBean.C_IDPERSONA,FacFacturaBean.C_IDINSTITUCION};
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

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
					ConsultaMorososForm formMororos = (ConsultaMorososForm)miForm;
					formMororos.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					formMororos.reset(mapping,request);
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscarInit")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPor(mapping, miForm, request, response); 
				}
				else if (accion.equalsIgnoreCase("consultaMoroso")){
					mapDestino = consultaMoroso(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("generaExcel")){
					mapDestino = generaExcel(mapping, miForm, request, response);
				}/*else if (accion.equalsIgnoreCase("comunicarMorosos")){
					mapDestino = comunicarMorosos(mapping, miForm, request, response);
				}*/else if (accion.equalsIgnoreCase("download")){
					mapDestino = download(mapping, miForm, request, response);
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
			    
			    //throw new SIGAException("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		} catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}
		
	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
	
        Vector datos = new Vector();
		String idInstitucion="";
		
		
		try {
			// Obtener los datos necesarios para realizar la busqueda de morosos
	       // ConsultaMorososForm form = (ConsultaMorososForm) request.getSession().getAttribute("DATABACKUP");
			// Obtener los datos necesarios para realizar la busqueda de morosos
	        ConsultaMorososForm form = (ConsultaMorososForm)formulario;
	        
	        // Obtenemos la información pertinente relacionada con los morosos
	        FacFacturaAdm facAdm = new FacFacturaAdm (this.getUserBean(request));
	        UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
	        idInstitucion = user.getLocation();
	        
	        Hashtable htFacturasPorPersona  = getFacturasPersonaAComunicar(form);
			//Recorremos la lista de morosos que nos han seleccoinado.
			//Terminando si encontramos un error al generar la carta o al realizar alguno de estos envios.  
			Iterator itePersona = htFacturasPorPersona.keySet().iterator();
			while (itePersona.hasNext()) {
				String idPersona = (String) itePersona.next();
				ArrayList alFacturas = (ArrayList) htFacturasPorPersona.get(idPersona);
							
				//Si va a depender el envio del numero de veces que ya se ha enviado la
				//comunicacion deberemos sacar tambien el numero exacto de comunicaciones.
				//Entonces en el ArrayList en vez de meter String meteremos algun objeto que contenga
				//factura y numero de comunicaciones
				//String comunica = (String) vCampos.get(3);

				//Vamos a hacer la comunicacion por persona, asi que filtraremos solo por esa persona
				//Aqui habria que ver si a la query le metemos los numero de facturas o sin meterlo recorremos el
				//Vector y eliminamos las facturas que no ha seleccionado. Vamos a hacerlo de este 
				//segundo modo...
				Vector datosTabla = facAdm.selectFacturasMoroso(idInstitucion,idPersona, null, null,alFacturas,null,false,false,user.getLanguage());
				datos.addAll(datosTabla);
			}
			/*FacFacturaBean.C_IDFACTURA	+ ", " +
			"NETO,TOTALIVA,TOTAL,PAGADO,DEUDA,COMUNICACIONES,"+
			CenColegiadoBean.C_NCOLEGIADO+",NOMBRE
			*/
			String sDeuda="";
			for (int i=0;i<datos.size();i++){
				Row row = (Row)datos.elementAt(i);
				Hashtable hashDeuda=row.getRow();
				 sDeuda=hashDeuda.get("DEUDA").toString();
				 hashDeuda.put("DEUDA",UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(sDeuda,2)));
				 row.setRow(hashDeuda);
				
				
			}
			
			String[] cabeceras = new String[]{UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.literal.ncolegiado")
					,UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.literal.nombreyapellidos"),
					UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.literal.fecha")
					, UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.literal.factura"),
					UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.literal.pendientepago"),
			UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.nifcif")};
					//,UtilidadesString.getMensajeIdioma(user, "facturacion.consultamorosos.literal.comunicaciones")};
			String[] campos = new String[]{CenColegiadoBean.C_NCOLEGIADO,"NOMBRE",FacFacturaBean.C_FECHAEMISION,
					FacFacturaBean.C_NUMEROFACTURA,FacFacturaBean.C_DEUDA ,CenPersonaBean.C_NIFCIF};
				//,"COMUNICACIONES"};
			request.setAttribute("campos",campos);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", idInstitucion+"_"+this.getUserName(request).toString());
						
			
		} 
		catch (Exception e) { 
			
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "generaExcel";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		try{
			
			//Si se viene de un proceso de envio de cartas de morosos, se eliminan los documentos generados
    		eliminarInformesCobros(request);
	        
	        ConsultaMorososForm miFormulario = (ConsultaMorososForm)formulario;        
	        FacFacturaAdm facAdm = new FacFacturaAdm (this.getUserBean(request));

			
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			
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
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null&&!isSeleccionarTodos){ 
				PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind)databackup.get("paginador");
				Vector datos=new Vector();
	
	
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
	
	
	
				if (paginador!=null){	 
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
	
				//obtengo datos de la consulta 			
				PaginadorCaseSensitiveBind resultado = null;
				Vector datos = null;
	
				resultado = facAdm.selectMorosos(user,miFormulario);
				// Paso de parametros empleando la sesion
				databackup.put("paginador",resultado);
				
				
				if (resultado!=null){ 
					
					
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)facAdm.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
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
				
				
	
				//request.setAttribute("datos", datos);
				request.setAttribute("fechaDesde",miFormulario.getFechaDesde());
				request.setAttribute("fechaHasta",miFormulario.getFechaHasta());
			}
	
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}

		return "resultado";
	}
	
	
	
	
    
        /**
     * Elimina los ficheros generados en el proceso de envios
     * @param request
     */
    private void eliminarInformesCobros(HttpServletRequest request) {
    	Vector informesCobros = ((Vector)request.getSession().getAttribute(("INFORMESCOBROS")));
    	if (informesCobros != null){
    		for (Iterator iter = informesCobros.iterator(); iter.hasNext(); ){
    			File file = (File)iter.next();
    			if (file.exists()){
    				file.delete();
    			}
    		}
    		request.getSession().removeAttribute("INFORMESCOBROS");
    	}
    }

    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String consultaMoroso(ActionMapping mapping, MasterForm formulario, 
            				   HttpServletRequest request, HttpServletResponse response) 
    	throws SIGAException
	{
    	try {
    	ConsultaMorososForm form = (ConsultaMorososForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = userBean.getLocation();
        //String idPersona = form.getIdPersona();
        String fechaDesde = form.getFechaDesde();
        String fechaHasta = form.getFechaHasta();
        //String idEstadoColegial = form.getCmbEstadoColegial();
        
                
        FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));       
        Vector datos;
        //Recogemos los datos de la tabla que hemos metido en el formulario.
        //Como solo tenemos un registro sacamos el elemento 0. Si viene vacio voy a lanzar una excepcion 
        String idFactura = null;
        String idPersona = null;
        if(form.getDatosTabla().size()>0){
        	Vector vCampos = form.getDatosTablaOcultos(0);
        	idPersona = (String) vCampos.get(0);
			idFactura = (String) vCampos.get(1);
        }else{
        	throw new Exception();
        }
        
        datos = facturaAdm.selectFacturasMoroso(idInstitucion,idPersona,fechaDesde,fechaHasta,null,idFactura,true,false,userBean.getLanguage());
        
        Hashtable clavesLineas = new Hashtable ();
		UtilidadesHash.set(clavesLineas, FacLineaFacturaBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(clavesLineas, FacLineaFacturaBean.C_IDFACTURA, idFactura);

		FacLineaFacturaAdm lineasAdm = new FacLineaFacturaAdm(this.getUserBean(request));
		Vector vLineasFactura = lineasAdm.select(clavesLineas);
		request.setAttribute("lineasFactura", vLineasFactura);
        request.setAttribute("filaFactura", (Row)datos.get(0));
		
        /*Hashtable clavesComunicaciones = new Hashtable ();
        UtilidadesHash.set(clavesComunicaciones, EnvComunicacionMorososBean.C_IDFACTURA, idFactura);
        UtilidadesHash.set(clavesComunicaciones, EnvComunicacionMorososBean.C_IDINSTITUCION, idInstitucion);
        UtilidadesHash.set(clavesComunicaciones, EnvComunicacionMorososBean.C_IDPERSONA, idPersona);
		EnvComunicacionMorososAdm admEnvComunicacion = new EnvComunicacionMorososAdm(this.getUserBean(request));
		Vector lineasComunicaciones = admEnvComunicacion.select(clavesComunicaciones);
		form.setLineasComunicaciones(lineasComunicaciones);*/
        
        Hashtable clavesDocumentos = new Hashtable ();
        UtilidadesHash.set(clavesDocumentos, EnvDocumentosBean.C_IDINSTITUCION, idInstitucion);
        UtilidadesHash.set(clavesDocumentos, EnvDocumentosBean.C_IDENVIO, idPersona);
		EnvDocumentosAdm admDocumentos = new EnvDocumentosAdm(this.getUserBean(request));
		Vector lineasComunicaciones = admDocumentos.getDocumentosFactura(idInstitucion,idFactura);
		form.setLineasComunicaciones(lineasComunicaciones);
        
        
		//request.setAttribute("lineasComunicaciones", vComunicaciones);
            
        } catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"});
		}
             
        return "facturasPendientes";
	}
    
	
	/**
	 * Este metodo no convierte el vector de datos que viene de la jsp y los trasforma en
	 * un hashtable donde las clavs son las persona. Luego los envios a morosos se haran por persona.
	 * @return
	 */
	private Hashtable getFacturasPersonaAComunicar(ConsultaMorososForm form){
		Hashtable htFacturasPersona = new Hashtable();
		ArrayList alFacturas = null;
		for (int i = 0; i < form.getDatosTabla().size(); i++) {
			Vector vCampos = form.getDatosTablaOcultos(i);
			String idPersona = (String) vCampos.get(0);
			String idFactura = (String) vCampos.get(1);
			if(htFacturasPersona.containsKey(idPersona)){
				alFacturas = (ArrayList) htFacturasPersona.get(idPersona);
				
			}else{
				alFacturas = new ArrayList();
				
			}
			alFacturas.add(idFactura);
			htFacturasPersona.put(idPersona, alFacturas);
			
			
		}
		
		
		return htFacturasPersona;
		
	}
	
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
	    File fDocumento = null;
	    try 
		{
			ConsultaMorososForm form = (ConsultaMorososForm)formulario;
			EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));
			
			

			String idInstitucion = String.valueOf(form.getIdInstitucion().intValue());
			String idEnvio = String.valueOf(form.getIdEnvioDoc().intValue());
			String idDocumento = String.valueOf(form.getIdDocumento().intValue());			
			String pathDocumento = form.getPathDocumento();			
			
			fDocumento = docAdm.getFile(idInstitucion,idEnvio,idDocumento);
			if(fDocumento==null || !fDocumento.exists()){
					throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			request.setAttribute("rutaFichero", fDocumento.getPath());
			request.setAttribute("nombreFichero", pathDocumento);
		} 		
		catch (Exception e) { 
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
				
		return "descargaFichero";
	}
    
}
