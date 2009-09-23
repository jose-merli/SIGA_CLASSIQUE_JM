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

import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
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
	final String separador = "||";
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
					formMororos.reset(new String[]{"registrosSeleccionados","datosPaginador"});
					formMororos.reset(mapping,request);
					mapDestino = abrir(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscarInit")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador"});
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
		
	/**
	 * @param mapping
	 * @param miForm
	 * @param request
	 * @param response
	 * @return
	 */
	/*protected String seleccionCarta(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) {
		
		UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		
		Vector vector=new Vector();
		
		String path = "/Datos/plantillas/comunicacion_morosos/"+user.getLocation();
		
		File f=new File(path);
		File hijo;
		
		String[] children = f.list();
		for (int i = 0; i<children.length; i++) {
			hijo=new File(path+ClsConstants.FILE_SEP+children[i]);
			if(hijo.isFile())
				vector.add(children[i]);
		}

		
		request.setAttribute("plantillas", vector);
		
		
		return "seleccionCarta";
	}
*/
	
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
				Vector datosTabla = facAdm.selectFacturasMoroso(idInstitucion,idPersona, null, null,alFacturas,null,false,false);
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
protected String imprimirMorososPDF(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		File ficFOP=null;
		File ficPDF=null;
        Vector datos = new Vector();
		String idInstitucion="";
		int tamanho=40;
		
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
				Vector datosTabla = facAdm.selectFacturasMoroso(idInstitucion,idPersona, null, null,alFacturas,null,true,false);
				datos.addAll(datosTabla);
			}
	        
	        //datos = facAdm.selectMorosos(user,form);
	        
	       
	        
			// Ubicacion de la carpeta donde se crean los ficheros FOP y PDF temporales
			ReadProperties rp = new ReadProperties("SIGA.properties");			
		    String rutaServidor = rp.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+rp.returnProperty("facturacion.directorioTemporalFacturasJava");
    		String barra = "";
    		String nombreFichero = "";
    		if (rutaServidor.indexOf("/") > -1){ 
    			barra = "/";
    		}
    		if (rutaServidor.indexOf("\\") > -1){ 
    			barra = "\\";
    		}    		
     		rutaServidor += barra+idInstitucion;
     		nombreFichero=rutaServidor+barra+idInstitucion+"_"+this.getUserName(request).toString()+".fo";     		
			ficFOP = new File(nombreFichero);
			File rutaFOP=new File(rutaServidor);
			if (!rutaFOP.exists()){
				if(!rutaFOP.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}

			Plantilla plantilla = new Plantilla(this.getUserBean(request));
			
		    String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaMorososJava")+rp.returnProperty("facturacion.directorioPlantillaListaMorososJava");
    		if (rutaPlantilla.indexOf("/") > -1){
    			barra = "/";
    		}
    		if (rutaPlantilla.indexOf("\\") > -1){
    			barra = "\\";
    		}

    		// Divido en las paginas pertinentes
    		double importeTotal=0;
    		
    		if (datos.size()<=tamanho){
        		importeTotal=plantilla.obtencionPaginaListaMorosos(ficFOP,idInstitucion,datos,rutaPlantilla+barra+idInstitucion+barra,"plantillaMorosos1-1.fo",datos.size(),user.getLanguage(),importeTotal);    			
    		}
    		else{
        		importeTotal=plantilla.obtencionPaginaListaMorosos(ficFOP,idInstitucion,datos,rutaPlantilla+barra+idInstitucion+barra,"plantillaMorosos1-N.fo",tamanho,user.getLanguage(),importeTotal);
        		for (int cont=0; cont<tamanho; cont++){
        			datos.removeElementAt(0);
        		}
        		while ((importeTotal!=-1)&&(datos.size()>tamanho)){
            		importeTotal=plantilla.obtencionPaginaListaMorosos(ficFOP,idInstitucion,datos,rutaPlantilla+barra+idInstitucion+barra,"plantillaMorososI-N.fo",tamanho,user.getLanguage(),importeTotal);
            		for (int cont=0; cont<tamanho; cont++){
            			datos.removeElementAt(0);
            		}
        	    }
        		importeTotal=plantilla.obtencionPaginaListaMorosos(ficFOP,idInstitucion,datos,rutaPlantilla+barra+idInstitucion+barra,"plantillaMorososN-N.fo",datos.size(),user.getLanguage(),importeTotal);
        		for (int cont=0; cont<datos.size(); cont++){
        			datos.removeElementAt(0);
        		}    			
    		}
    		
			// Obtencion fichero PDF
			if (importeTotal!=-1){
				ficPDF=new File(rutaServidor+barra+idInstitucion+"_"+this.getUserName(request).toString()+".pdf"); 
				plantilla.convertFO2PDF(ficFOP, ficPDF,rutaPlantilla+File.separator+idInstitucion);
				ficFOP.delete();
			}

			request.setAttribute("nombreFichero", idInstitucion+"_"+this.getUserName(request).toString()+".pdf");
			request.setAttribute("rutaFichero", rutaServidor+barra+idInstitucion+"_"+this.getUserName(request).toString()+".pdf");			
			
		} 
		catch (Exception e) { 
			if (ficFOP.exists()){
				ficFOP.delete();
			}
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return "descargaFichero";
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
			
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("Seleccion");
			
			if (seleccionados != null) {
				ArrayList alRegistros = actualizarSelecionados(seleccionados, clavesRegSeleccinados);
				if (alRegistros != null) {
					clavesRegSeleccinados = alRegistros;
					miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
				}
			}
			
			
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){ 
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
					//Sacanmos las claves para el check multiregistro
					clavesRegSeleccinados = new ArrayList((Collection)facAdm.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
					//Inciializamos los registros a sin seleccionar
					clavesRegSeleccinados = getClavesBusqueda(clavesRegSeleccinados,false);
					
					
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					
				} 
				miFormulario.setDatosPaginador(databackup);
				
				//Inicializamos los registros seleccionados
				if (clavesRegSeleccinados != null) {
					miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
				}else{
					if(miFormulario.getRegistrosSeleccionados()==null)
						miFormulario.setRegistrosSeleccionados(new ArrayList());
					
				}
	
				//request.setAttribute("datos", datos);
				request.setAttribute("fechaDesde",miFormulario.getFechaDesde());
				request.setAttribute("fechaHasta",miFormulario.getFechaHasta());
			}
	
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}

		return "resultado";
	}
	protected ArrayList getClavesBusqueda(ArrayList v,boolean isSeleccionado){
		 
		Hashtable aux=new Hashtable();
		ArrayList claves= new ArrayList();

		for (int k=0;k<v.size();k++){
			aux = (Hashtable) v.get(k);
			String idFactura = (String)aux.get(FacFacturaBean.C_IDFACTURA);
			String idPersona = (String)aux.get(FacFacturaBean.C_IDPERSONA);
			String idInstitucion = (String)aux.get(FacFacturaBean.C_IDINSTITUCION);
			Hashtable aux2= getIds(idFactura, idPersona, idInstitucion);
			if(isSeleccionado)
				aux2.put("SELECCIONADO", "1");
			else
				aux2.put("SELECCIONADO", "0");
			claves.add(aux2);	
		}
		
		return claves;
	}
	private Hashtable getIds(String idFactura, String idPersona, String idInstitucion){
		Hashtable aux2= new Hashtable();
		StringBuffer clave = new StringBuffer();
		clave.append(idFactura);
		clave.append(separador);
		clave.append(idPersona);
		clave.append(separador);
		clave.append(idInstitucion);
		
		
		aux2.put("CLAVE",clave.toString());
		aux2.put(FacFacturaBean.C_IDPERSONA,idPersona );
		aux2.put(FacFacturaBean.C_IDFACTURA,idFactura);
		aux2.put(FacFacturaBean.C_IDINSTITUCION,idInstitucion);
		aux2.put("SELECCIONADO", "1");
		return aux2;
	}

	protected ArrayList actualizarSelecionados(String seleccionados, ArrayList alClaves){
		
    	String[] aSeleccionados = seleccionados.split(",");
    	
    	for (int z=0;z<alClaves.size();z++){
	    	Hashtable htclavesBusqueda = (Hashtable)alClaves.get(z);
	    	String clave = (String)htclavesBusqueda.get("CLAVE");
	    	boolean isEncontrado = false;
	    	if (!seleccionados.equals("")){
		    	for (int i = 0; i < aSeleccionados.length; i++) {
		    		String registro = aSeleccionados[i];
		    		String[] ids = UtilidadesString.split(registro, separador);
		    		String idFactura = ids[0];
		    		String idPersona = ids[1];
		    		String idInstitucion = ids[2];
		    		
		    		Hashtable aux2= getIds(idFactura, idPersona, idInstitucion);
		    		if(clave.equals(aux2.get("CLAVE"))){
		    			htclavesBusqueda.put("SELECCIONADO", "1");
		    			isEncontrado = true;
		    			break;
		    		}
		    		
		    		
				}
	    	}
	    	if (!isEncontrado){
	    		htclavesBusqueda.put("SELECCIONADO", "0");
	    	}
	    
    	}
    	
    	
    	return alClaves;
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
        
        datos = facturaAdm.selectFacturasMoroso(idInstitucion,idPersona,fechaDesde,fechaHasta,null,idFactura,true,false);
        
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
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/*protected String comunicarMorosos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		File ficFOP=null;
		File ficPDF=null;
		boolean correcto=true;
        //Vector datos = new Vector();
		String idInstitucion="";
		String result="resultado";
		//String modeloCarta="";
		
		try {
			// Obtener los datos necesarios para realizar la busqueda de morosos
	        ConsultaMorososForm form = (ConsultaMorososForm)formulario;
	        // Obtenemos la información pertinente relacionada con los morosos
	        FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));
			Plantilla plantilla=new Plantilla ();
	        UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   	        
	        //datos = facturaAdm.selectMorosos(user,form);
	        //modeloCarta=form.getModelo();
	        
	        idInstitucion = user.getLocation();
			ReadProperties rp = new ReadProperties("SIGA.properties");
	        
			// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
		    String rutaTemporal =  rp.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+rp.returnProperty("facturacion.directorioTemporalFacturasJava");
    		String barraTemporal = "";
    		if (rutaTemporal.indexOf("/") > -1){ 
    			barraTemporal = "/";
    		}
    		if (rutaTemporal.indexOf("\\") > -1){ 
    			barraTemporal = "\\";
    		}    		
    		rutaTemporal += barraTemporal+idInstitucion;
			File rutaFOP=new File(rutaTemporal);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaFOP.exists()){
				if(!rutaFOP.mkdirs()){
					throw new ClsExceptions("La ruta de acceso a los ficheros temporales no puede ser creada");					
				}
			}
    		
			// Obtencion de la ruta donde se almacenan las facturas en formato PDF			
		    String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoComunicacionesPDFJava")+rp.returnProperty("facturacion.directorioComunicacionesPDFJava");
    		String barraAlmacen = "";
    		if (rutaAlmacen.indexOf("/") > -1){ 
    			barraAlmacen = "/";
    		}
    		if (rutaAlmacen.indexOf("\\") > -1){ 
    			barraAlmacen = "\\";
    		}    		
    		rutaAlmacen += barraAlmacen+idInstitucion;
			File rutaPDF=new File(rutaAlmacen);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaPDF.exists()){
				if(!rutaPDF.mkdirs()){
					throw new ClsExceptions("La ruta de acceso a los ficheros PDF no puede ser creada");					
				}
			}
			
			// Obtencion de la ruta de donde se obtiene la plantilla adecuada			
		    String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaMorososJava")+rp.returnProperty("facturacion.directorioPlantillaComunicacionMorososJava");
		    String barraPlantilla="";
    		if (rutaPlantilla.indexOf("/") > -1){
    			barraPlantilla = "/";
    		}
    		if (rutaPlantilla.indexOf("\\") > -1){
    			barraPlantilla = "\\";
    		}
    		rutaPlantilla += barraPlantilla+idInstitucion;
			File rutaModelo=new File(rutaPlantilla);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaModelo.exists()){
				throw new SIGAException ("messages.facturacion.errorRutaNoExiste");
				// ClsExceptions("La ruta de acceso a la plantilla de la factura no existe");					
			}
			
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
				Vector datosTabla = facturaAdm.selectFacturasMoroso(idInstitucion,idPersona, null, null,alFacturas,null,false);
		        
		        // Generacion de la carta de comunicacion a morosos en formato FOP     		
				ficFOP = new File(rutaTemporal+barraTemporal+idPersona+".fo");
				
				//VENTANA SELECCION DE CARTA A MOROSOS
				//correcto=plantilla.obtencionComunicacionMoroso(ficFOP,idInstitucion,datosTabla,rutaPlantilla+barraPlantilla,modeloCarta,user.getLanguage(),(String)impago.get(FacFacturaBean.C_IDPERSONA), user);
				//correcto=plantilla.obtencionComunicacionMoroso(ficFOP,idInstitucion,datosTabla,rutaPlantilla+barraPlantilla,"plantillaCartaMorosos.fo",user.getLanguage(),idPersona, user);
				correcto=plantilla.obtencionComunicacionMoroso(ficFOP,idInstitucion,datosTabla,rutaPlantilla+barraPlantilla,user.getLanguage(),idPersona, user);
				// Obtencion fichero PDF
				if (correcto){
					String fechaActual=UtilidadesBDAdm.getFechaBD("").replace('/','-')+"_"+UtilidadesBDAdm.getHoraBD().replace(':','-');
					ficPDF=new File(rutaAlmacen+barraAlmacen+idPersona+"_"+fechaActual+".pdf"); 
					plantilla.convertFO2PDF(ficFOP, ficPDF, rutaPlantilla);
					ficFOP.delete();
					
    				//Obtenemos el bean del envio:
    				CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
    				String descripcion = "Envio carta morosos - "+admPersona.obtenerNombreApellidos(idPersona);
    				Envio envio = new Envio(user,descripcion);

    				// Bean envio
    				EnvEnviosBean enviosBean = envio.enviosBean;
    				 
    				// Preferencia del tipo de envio si el usuario tiene uno:
    				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.getUserBean(request));
    				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"2");
    				if (direccion.get(CenDireccionesBean.C_PREFERENTE)!=null){
    					String preferencia=(String)direccion.get(CenDireccionesBean.C_PREFERENTE);
        				Integer valorPreferencia = Envio.calculaTipoEnvio(preferencia);
        	            enviosBean.setIdTipoEnvios(valorPreferencia);
    				}
    				
    				// Recojo una plantilla valida cualquiera:
    				EnvPlantillasEnviosAdm plantillasEnviosAdm = new EnvPlantillasEnviosAdm(this.getUserBean(request));
    				Vector plantillasValidas=plantillasEnviosAdm.getIdPlantillasValidos(idInstitucion,enviosBean.getIdTipoEnvios().toString());
    				//Si no hay plantillas:
    				if (plantillasValidas.isEmpty()){
    					correcto=false; //ERROR
    				}
    				//Tenemos plantillas:
    				else {
        				enviosBean.setIdPlantillaEnvios(new Integer((String)plantillasValidas.firstElement()));

        				// Creacion documentos
        				Documento documento = new Documento(rutaAlmacen+barraAlmacen+idPersona+"_"+fechaActual+".pdf","Carta impago"+idPersona+"_"+fechaActual);
        				Vector documentos = new Vector(1);
        				documentos.add(documento);
        				
        				// Genera el envio:
        				envio.generarEnvio(idPersona,documentos);
    				}
				}				
			}
			
			//Chequeo de toda la operacion:
			if (correcto){
				result=exito("facturacion.listadoMorosos.literal.envioOk",request);
			}
			else{
				result=exito("facturacion.consultaMorosos.errorInformes",request);
			}
		} 
		catch (Exception e) {
			if ((ficFOP != null) && ficFOP.exists()){
				ficFOP.delete();
			}
			throwExcp("facturacion.consultaMorosos.errorInformes", new String[] {"modulo.facturacion"}, e, null); 
		}

		return result;
	}*/
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
