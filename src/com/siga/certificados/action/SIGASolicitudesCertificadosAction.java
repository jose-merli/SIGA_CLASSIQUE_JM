package com.siga.certificados.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificadostexto;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificadostextoExample;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosTextoService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.CenBancosBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CerEstadoSoliCertifiAdm;
import com.siga.beans.CerEstadoSoliCertifiBean;
import com.siga.beans.CerPlantillasAdm;
import com.siga.beans.CerPlantillasBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.CerSolicitudCertificadosTextoBean;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysCompraAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.PysProductosSolicitadosAdm;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.beans.PysServiciosSolicitadosBean;
import com.siga.certificados.Certificado;
import com.siga.certificados.Plantilla;
import com.siga.certificados.form.SIGASolicitudesCertificadosForm;
import com.siga.facturacion.Facturacion;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;



public class SIGASolicitudesCertificadosAction extends MasterAction
{
	
	public static Hashtable<String,Integer> contadores = new Hashtable<String, Integer>();
	
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
    	String mapDestino = "exception";
    	MasterForm miForm = null;

    	try 
    	{ 
	        miForm = (MasterForm) formulario;

	        if (miForm != null) 
	        {
	            String accion = miForm.getModo();

	            if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
	            {
	                mapDestino = abrir(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("generarVariosPDF"))
	            {
	                mapDestino = generarVariosPDF(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("generarPDF"))
	            {
	                mapDestino = generarPDF(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("enviar"))
	            {
	                mapDestino = enviar(mapping, miForm, request, response);
	            } 
	            
	            else if (accion.equalsIgnoreCase("ver")){
	            	// Para no repetir un metodo igual que editar usamos el mismo
	                mapDestino = editar(mapping, miForm, request, response);
	            }
	            
	            else if (accion.equalsIgnoreCase("denegar"))
	            {
	                mapDestino = denegar(mapping, miForm, request, response);
	            } 
	            else if (accion.equalsIgnoreCase("anular"))
	            {
	                mapDestino = anular(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("finalizar"))
	            {
	                mapDestino = finalizar(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("facturacionRapida"))
	            {
	                mapDestino = facturacionRapida(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("finalizarCertificados"))
	            {
	                mapDestino = finalizarCertificados(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("descargar"))
	            {
	                mapDestino = descargar(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("asignarPlantillaCertificado"))
	            {
	                mapDestino = asignarPlantillaCertificado(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("copiarSanciones"))
	            {
	                mapDestino = copiarSanciones(mapping, miForm, request, response);
	            } 
	            else if (accion.equalsIgnoreCase("copiarHistorico"))
	            {
	                mapDestino = copiarHistorico(mapping, miForm, request, response);
	            }
	            else if (accion.equalsIgnoreCase("historicoObservaciones"))
	            {
	                mapDestino = historicoObservaciones(mapping, miForm, request, response);
	            }
	            else if (accion.equalsIgnoreCase("comprobarNumPlantillas"))
	            {
	                mapDestino = comprobarNumPlantillas(mapping, miForm, request, response);
	            }   
	            else 
	            {
	                return super.executeInternal(mapping,formulario,request,response);
	            }
	        }

    	    if (mapDestino == null)	
    	    { 
    	        throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
    	    }

   	        return mapping.findForward(mapDestino);
    	} 
    	catch (SIGAException es) 
    	{ 
    	    throw es; 
    	} 
    	catch (Exception e) 
    	{ 
    	    throw new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
    	} 

	}

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
    	try{
    		
	        SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
	        String idSolicitudCompra="";
	        String idSolicitudCertificado="";
	        String concepto="";
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        if (request.getParameter("idSolicitud")!=null){
	        	idSolicitudCompra=(String)request.getParameter("idSolicitud");
	        }
	        if (request.getParameter("idSolicitudCertificado")!=null){
	        	idSolicitudCertificado=(String)request.getParameter("idSolicitudCertificado");
	        }
	        
	        if (request.getParameter("concepto")!=null){
	        	concepto=(String)request.getParameter("concepto");
	        }

	        String idProducto=request.getParameter("idProducto");
	        String idProductoInstitucion=request.getParameter("idProductoInstitucion");
	        String idTipoProducto=request.getParameter("idTipoProducto");
	        // RGG para obtener manualmente los estados de la solicitud
	        CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(this.getUserBean(request));
	        String where=" WHERE "+CerEstadoSoliCertifiBean.T_NOMBRETABLA+"."+CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO+"<>"+CerSolicitudCertificadosAdm.K_ESTADO_SOL_ENVIOP;
	        Vector v = estAdm.select(where);
	        CerEstadoSoliCertifiBean b = new CerEstadoSoliCertifiBean();
	        b.setIdEstadoSolicitudCertificado(new Integer(99));
	        //b.setDescripcion("No Enviados");
	        b.setDescripcion(UtilidadesString.getMensajeIdioma(userBean,"certificados.estadoSolicitud.noEnviados"));
	        
	        v.add(b);
        	request.setAttribute("EstadosSolicitud",v);
	        
	        
	        
	        // RGG cambio para enviar el idSolicitudCertificado y no el idPeticionCompra
	        if (idSolicitudCertificado.equals("")) {
	        	CerSolicitudCertificadosAdm solAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		        if (idSolicitudCompra!=null && !idSolicitudCompra.equals("")) {
		        	// PDM: Al metodo obtenerIdSolicitudDesdeIdCompra se le pasa tambien la clave del producto porque si una peticion de compra tiene
		        	// mas de un producto certificado, la consulta no devolvia bien el idsolicitud, nos devolvia el del primer producto certificado que encontraba
		        	idSolicitudCertificado = solAdm.obtenerIdSolicitudDesdeIdCompra(idSolicitudCompra,idProducto,idProductoInstitucion,idTipoProducto,userBean.getLocation());
		        }
	        }
		    request.setAttribute("idPeticion",idSolicitudCertificado);
	        request.setAttribute("concepto",concepto);
	        
	        
	        if (request.getParameter("buscar")!=null&&request.getParameter("buscar").equalsIgnoreCase("true")){
	            
	            try {
	            	if (request.getSession().getAttribute("DATABACKUP")!=null){
	                 form =(SIGASolicitudesCertificadosForm)request.getSession().getAttribute("DATABACKUP");
	                 request.setAttribute("SolicitudesCertificadosForm",form);
	            	}
	               
	            } 
	            catch (Exception e) {
	            }
	        }
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		return "abrir";
	}

	private boolean isNumber(String in){
		try {
            Integer.parseInt(in);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
	}
	
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		
    	
    	
    	try{
			HashMap databackup=new HashMap();
	        SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
	        GenParametrosAdm paramAdm = new GenParametrosAdm (userBean);
			//Haria falta meter los parametros en con ClsConstants
	        String permitir_factura_certificado = paramAdm.getValor (userBean.getLocation (), "CER", ClsConstants.PERMITIR_FACTURA_CERTIFICADO, "");
	        
	        request.setAttribute("isPermitirFacturaCertificado", new Boolean(permitir_factura_certificado.equals(ClsConstants.DB_TRUE)));
	        
	        
	        CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
	        
	        String idInstitucion = userBean.getLocation();
	        
	        if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
		 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
			     PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
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
		    PaginadorBind resultado = null;
		    
		    if (!isNumber(form.getBuscarIdSolicitudCertif())){
		    	form.setBuscarIdSolicitudCertif(null);
		    }
	
	         resultado = admSolicitud.buscarSolicitudes(idInstitucion,
	                									  form.getFechaDesde(),
	                									  form.getFechaHasta(),
	                									  form.getFechaEmisionDesde(),
	                									  form.getFechaEmisionHasta(),
	                									  form.getEstado(),
	                									  form.getTipoCertificado(),
	                									  form.getNumeroCertificado(),
	                									  form.getCIFNIF(),
	                									  form.getNombre(),
	                									  form.getApellido1(),
	                									  form.getIdInstitucionOrigen(),
	                									  form.getIdInstitucionDestino(),
														  form.getBuscarIdSolicitudCertif(),
														  form.getBuscarNumCertificadoCompra());  

	        
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//	        ReadProperties rp = new ReadProperties("SIGA.properties");
	        String numMaxReg = rp.returnProperty("certificados.numMaxRegistros");
	        
	       
		        
		        Vector datos = null;
				
					
				
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
				   datos = resultado.obtenerPagina(1);
				   databackup.put("datos",datos);
				   request.getSession().setAttribute("DATAPAGINADOR",databackup);
				   
				   // en el caso de que el numero de registros recuperados en la busqueda supere el numero de registros
			        // configurado en SIGA.properties se muestra un mensaje de error
			         
				        if ( resultado.getNumeroTotalRegistros()>new Integer(numMaxReg).intValue()){
				        	
				        	return exito("messages.certificados.numMaxReg",request);
				        }
			        //
				} 
	  }
	       // request.setAttribute("datos", datos);
	        
	        //Comprobamos si estamos en CGAE y lo pasamos a la jsp
	        String esCGAE = CenVisibilidad.getNivelInstitucion(idInstitucion).equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_INTITUCION_CGAE))?"true":"false";
	        request.setAttribute("esCGAE",esCGAE);
	        
	        request.getSession().setAttribute("DATABACKUP",form);
	        
	        //Para volver correctamente desde envios:
	        request.getSession().setAttribute("EnvEdicionEnvio","GS");
	        
		} catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
	        
        return "buscar";
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		
		
		UserTransaction tx=null;
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    String idInstitucion         = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud           = ((String)vOcultos.elementAt(1)).trim();
		    String idPeticion            = ((String)vOcultos.elementAt(5)).trim();
		    String idProducto            = ((String)vOcultos.elementAt(6)).trim();
		    String idTipoProducto        = ((String)vOcultos.elementAt(7)).trim();
		    String idProductoInstitucion = ((String)vOcultos.elementAt(8)).trim();

			// Comienzo control de transacciones
			tx = usr.getTransaction();		    
			tx.begin();
			
			// 1. Borramos la solicitud de certificado
		    Hashtable hash = new Hashtable();
		    
		    boolean tieneCompraFacturada = false;
		    	    
			// 2. Borramos la compra asociada siempre y cuando no se haya facturado
	    	hash.clear();
	    	UtilidadesHash.set(hash, PysCompraBean.C_IDINSTITUCION, idInstitucion);
	    	UtilidadesHash.set(hash, PysCompraBean.C_IDPETICION, idPeticion);
	    	UtilidadesHash.set(hash, PysCompraBean.C_IDPRODUCTO, idProducto);
	    	UtilidadesHash.set(hash, PysCompraBean.C_IDTIPOPRODUCTO, idTipoProducto);
	    	UtilidadesHash.set(hash, PysCompraBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
	    	PysCompraAdm admCompra = new PysCompraAdm(this.getUserBean(request));
	    	Vector v = admCompra.selectByPK(hash);
	    	if (v != null && v.size() == 1) {
	    		PysCompraBean b = (PysCompraBean) v.get(0);
	    		if (b.getIdFactura() == null || b.getIdFactura().equals("")) {
	    			admCompra.delete(hash);
	    		} else {
	    			tieneCompraFacturada = true;
	    		}
	    	}
	    	
	    	if (!tieneCompraFacturada) {
	    		
	    		hash.clear();
	    		hash.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			    hash.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			    if (!admSolicitud.delete(hash)) {
			    	throw new SIGAException ("error.messages.deleted");
			    }
			    
		    	// 3. Borramos el producto solicitado
		    	hash.clear();
		    	UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
		    	UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
		    	UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTO, idProducto);
		    	UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    	UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		    	PysProductosSolicitadosAdm pysProductosSolicitadosAdm = new PysProductosSolicitadosAdm(this.getUserBean(request));
		    	v = pysProductosSolicitadosAdm.selectByPK(hash);
		    	if (v != null && v.size() == 1) {
		    		pysProductosSolicitadosAdm.delete(hash);	    		
		    	}
					    	
		    	if (!contieneMasProductos(request, PysProductosSolicitadosBean.T_NOMBRETABLA, idInstitucion, idPeticion)
		    			&& !contieneMasProductos(request, PysServiciosSolicitadosBean.T_NOMBRETABLA, idInstitucion, idPeticion)) {
			    
					// 4. Borramos la peticion de compra asociada
			    	// OJO hay un trigger que cambia el estado de PeticionCompraSuscripcion.estadopeticion = 20 (procesada)
			    	// cuando se borra una solicitud de certificado, por eso no hacemos el delete
			    	hash.clear();
			    	UtilidadesHash.set(hash, PysPeticionCompraSuscripcionBean.C_IDINSTITUCION, idInstitucion);
			    	UtilidadesHash.set(hash, PysPeticionCompraSuscripcionBean.C_IDPETICION, idPeticion);
			    	PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.getUserBean(request));
			    	if (!ppcsa.delete(hash)) {
			    		;//throw new SIGAException ("error.messages.deleted");
			    	}
		    	}
		        request.setAttribute("mensaje","messages.deleted.success");
	    	} else {
	    	    //throw new SIGAException("messages.certificados.compraFacturadaRefresque");
	    	    exitoRefresco("messages.certificados.compraFacturadaRefresque",request);
	    	}
			
	    	tx.commit();
		} 
		catch (Exception e) { 
	        throwExcp("messages.deleted.error",new String[] {"modulo.certificados"},e,tx); 
		}
	    return "exito";
	}

    private boolean contieneMasProductos (HttpServletRequest request,
    									  String tabla,
    									  String idInstitucion,
    									  String idPeticion)
    		throws ClsExceptions
    {
		boolean contieneMasProductos;
		PysProductosSolicitadosAdm pysProductosSolicitadosAdm;
		
    	if (idPeticion == null)
    		contieneMasProductos = false;
    	else if (idPeticion.equals (""))
    		contieneMasProductos = false;
    	else {
    		//buscando cantidad de productos o servicios relacionados
			pysProductosSolicitadosAdm = 
				new PysProductosSolicitadosAdm (this.getUserBean (request));
			String sql =
				"SELECT COUNT(1) COUNT" +
				"  FROM "+tabla+" T" +
				" WHERE T.IDINSTITUCION = "+idInstitucion+" "+
				"   AND T.IDPETICION = "+idPeticion+" ";
			
			contieneMasProductos = true;
			RowsContainer rowsContainer = pysProductosSolicitadosAdm.find (sql);
			if (rowsContainer != null) {
				Vector vector = rowsContainer.getAll();
				if (vector != null && vector.size() > 0) {
					Row row = (Row) vector.get (0);
					if (Integer.parseInt (row.getString ("COUNT")) == 0)
						contieneMasProductos = false;
					else
						contieneMasProductos = true;
				}
			}
    	}
		
		return contieneMasProductos;
	} //contieneMasProductos ()


    protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
    {
    	try{
    		MasterForm miForm = (MasterForm) formulario;
    		String accion = miForm.getModo();
    		request.setAttribute("modo", accion);

    		SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
    		CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
    		Vector vOcultos = form.getDatosTablaOcultos(0);

    		String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
    		String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
    		String idEstadoSolicitud=((String)vOcultos.elementAt(9)).trim();
    		String tipoCertificado=((String)vOcultos.elementAt(11)).trim();


    		Hashtable htSolicitud = new Hashtable();
    		htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
    		htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

    		Vector vDatos = admSolicitud.selectByPK(htSolicitud);

    		CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);

    		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request));

    		String idInstitucionOrigen = ""+beanSolicitud.getIdInstitucionOrigen();
    		String idInstitucionDestino = ""+beanSolicitud.getIdInstitucionDestino();
    		String idInstitucionColegiacion = ""+beanSolicitud.getIdInstitucionColegiacion();
    		CenInstitucionBean beanInstitucionOrigen = null;
    		Hashtable htInstitucion = new Hashtable();
    		if(!idInstitucionOrigen.equalsIgnoreCase("null") ){
    			htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionOrigen);
    			vDatos = admInstitucion.selectByPK(htInstitucion);
    			if (vDatos!=null && vDatos.size()==1)
    			{
    				beanInstitucionOrigen = (CenInstitucionBean)vDatos.elementAt(0);
    			}

    		}

    		CenInstitucionBean beanInstitucionColegiacion = null;
    		if(!idInstitucionColegiacion.equalsIgnoreCase("null") ){
    			htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionColegiacion);
    			vDatos = admInstitucion.selectByPK(htInstitucion);
    			if (vDatos!=null && vDatos.size()==1)
    			{
    				beanInstitucionColegiacion = (CenInstitucionBean)vDatos.elementAt(0);
    			}

    		}

    		CenInstitucionBean beanInstitucionDestino = null;

    		if(!idInstitucionDestino.equalsIgnoreCase("null")){
    			htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionDestino);
    			vDatos = admInstitucion.selectByPK(htInstitucion);

    			if (vDatos!=null && vDatos.size()==1)
    			{
    				beanInstitucionDestino = (CenInstitucionBean)vDatos.elementAt(0);
    			}
    		}

    		// obtengo el texto de sanciones. se modifica. Lo que se obtiene es el texto del certificado
    		//String textoCertificado = admSolicitud.getTextoCertificado(beanSolicitud.getIdInstitucion().toString(), beanSolicitud.getIdSolicitud().toString());
    		//		    ESTA TABLA ESTA MAL YA QUE NO TIENE SENTIDO EL CAMPO IDTEXTO COMO PARTE DE LA PK CUANDO NO EXSITE TAL MULTIPLICAIDAD. 
    		CerSolicitudCertificadosTextoService certificadosTextoService   = (CerSolicitudCertificadosTextoService) getBusinessManager().getService(CerSolicitudCertificadosTextoService.class);
    		CerSolicitudcertificadostextoExample cerSolicitudcertificadostextoExample = new CerSolicitudcertificadostextoExample();
    		 org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificadostextoExample.Criteria criteria =  cerSolicitudcertificadostextoExample.createCriteria();
    		
    		 criteria.andIdinstitucionEqualTo(beanSolicitud.getIdInstitucion().shortValue());
    		 criteria.andIdsolicitudEqualTo(beanSolicitud.getIdSolicitud());
    		//		    cerSolicitudcertificadostexto.setIdtexto((short) 2);
    		
//		    
    		
    		List<CerSolicitudcertificadostexto> cerSolicitudcertificadostextoList =certificadosTextoService.getList(cerSolicitudcertificadostextoExample);
    		String textoCertificado = "";
    		String incluirDeudas = "off";
    		String incluirSanciones = "off";
    		if(cerSolicitudcertificadostextoList.size()>0){
    			CerSolicitudcertificadostexto cerSolicitudcertificadostexto = cerSolicitudcertificadostextoList.get(0);
//    			cerSolicitudcertificadostexto = certificadosTextoService.get(cerSolicitudcertificadostexto);

    			

    			if(cerSolicitudcertificadostexto!=null){
    				textoCertificado = cerSolicitudcertificadostexto.getTexto();
    				if(cerSolicitudcertificadostexto.getIncluirdeudas()!=null && cerSolicitudcertificadostexto.getIncluirdeudas().equalsIgnoreCase("S"))
    					incluirDeudas = "on";
    				if(cerSolicitudcertificadostexto.getIncluirsanciones()!=null && cerSolicitudcertificadostexto.getIncluirsanciones().equalsIgnoreCase("S"))
    					incluirSanciones = "on";


    			}

    			form.setIncluirDeudas(incluirDeudas);
    			form.setIncluirSanciones(incluirSanciones);
    		}
    		request.setAttribute("sanciones", textoCertificado);

    		request.setAttribute("solicitud", beanSolicitud);
    		request.setAttribute("idEstadoSolicitud", idEstadoSolicitud);

    		if (beanSolicitud.getIdEstadoSolicitudCertificado().toString().equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO)
    				|| beanSolicitud.getIdEstadoSolicitudCertificado().toString().equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO)) {

    			request.setAttribute("modificarSolicitud", "0");
    		} else {
    			request.setAttribute("modificarSolicitud", "1");
    		}
    		request.setAttribute("institucionOrigen", beanInstitucionOrigen);
    		request.setAttribute("institucionDestino", beanInstitucionDestino);
    		request.setAttribute("institucionColegiacion", beanInstitucionColegiacion);
    		request.setAttribute("tipoCertificado", tipoCertificado);

    		// TRATAMIENTO DEL CONTADOR
    		String pre = (beanSolicitud.getPrefijoCer()!=null)?beanSolicitud.getPrefijoCer():"";
    		String suf = (beanSolicitud.getSufijoCer()!=null)?beanSolicitud.getSufijoCer():"";
    		String numContador=beanSolicitud.getContadorCer();
    		String idtipop=beanSolicitud.getPpn_IdTipoProducto().toString();
    		String idp=beanSolicitud.getPpn_IdProducto().toString();
    		String idpi=beanSolicitud.getPpn_IdProductoInstitucion().toString();

    		if (numContador!=null && !numContador.equals("")) {
    			// obtengo el objeto contador
    			GestorContadores gc = new GestorContadores(this.getUserBean(request)); 

    			//String idContador="PYS_"+idtipop+"_"+idp+"_"+idpi;
    			// obtenemos el contador de la FK del producto
    			String idContador="";
    			PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.getUserBean(request));
    			Vector v=admProd.select("WHERE "+PysProductosInstitucionBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+"="+idtipop+" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+"="+idp+" AND "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+"="+idpi);
    			PysProductosInstitucionBean beanProd=null;
    			if (v!=null && v.size()>0) {
    				beanProd = (PysProductosInstitucionBean) v.get(0);
    				idContador=beanProd.getIdContador();
    			}
    			Hashtable contadorTablaHash=gc.getContador(new Integer(idInstitucion),idContador);

    			// formateo el contador
    			Integer longitud= new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));
    			int longitudContador=longitud.intValue();
    			Integer contadorSugerido=new Integer(numContador);
    			String contadorFinalSugerido=UtilidadesString.formatea(contadorSugerido,longitudContador,true);
    			String contador =pre.trim()+contadorFinalSugerido+suf.trim(); 			

    			// lo guardamos formateado
    			request.setAttribute("codigo",contador);
    		} else {
    			request.setAttribute("codigo","");
    		}

    		{
    			AdmUsuariosAdm adm = new AdmUsuariosAdm(this.getUserBean(request));
    			Hashtable h = new Hashtable();
    			UtilidadesHash.set (h, AdmUsuariosBean.C_IDUSUARIO,     beanSolicitud.getUsuMod());
    			UtilidadesHash.set (h, AdmUsuariosBean.C_IDINSTITUCION, beanSolicitud.getIdInstitucion());
    			Vector v = adm.select(h);
    			if ((v != null) && (v.size() == 1)) 
    				request.setAttribute("nombreUltimoUsuMod", ((AdmUsuariosBean)v.get(0)).getDescripcion());
    		}
    	}catch (SIGAException e) {
    		throw e;	
    	}    	
    	catch (Exception e) { 
    		throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
    	}		    
    	return "mostrar";
    }

	private void almacenarCertificado(String idInstitucion, String idSolicitud, HttpServletRequest request,PysProductosInstitucionBean beanProd,String idTipoProducto,String idProducto,String idProductoInstitucion,
		String idPlantilla,String idPersona,File fIn,File fOut,String sRutaPlantillas,String idInstitucionOrigen,boolean usarIdInstitucion, UsrBean usr)throws ClsExceptions, SIGAException {
	    
    	// OBTENEMOS LA SOLICITUD
    	Hashtable htSolicitud = new Hashtable();
	    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
	    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
	    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
	    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
	    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
	    CerSolicitudCertificadosAdm admCert = new CerSolicitudCertificadosAdm(this.getUserBean(request));
	    
    	// HACEMOS UNO NUEVO PARA ACTUALIZAR EL ESTADO
        //Hashtable htOld = beanSolicitud.getOriginalHash();
	    Hashtable htNew = beanSolicitud.getOriginalHash();
	    
	    /// RGG 05/03/2007 CAMBIO DE CONTADORES /////////////////////
        boolean tieneContador=admSolicitud.tieneContador(idInstitucion,idSolicitud);
		GestorContadores gc = new GestorContadores(this.getUserBean(request));
		//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
		//String idContador="PYS_"+idTipoProducto+"_"+idProducto+"_"+idProductoInstitucion;
		// obtenemos el contador de la FK del producto
		String idContador=beanProd.getIdContador();
		
		String[] claves = {CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD};
        
        //pdm es necesario actualizar estas fechas cuando se aprueba la solicitud del certificado
        String[] campos = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO,CerSolicitudCertificadosBean.C_PREFIJO_CER,CerSolicitudCertificadosBean.C_SUFIJO_CER, CerSolicitudCertificadosBean.C_CONTADOR_CER,CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,CerSolicitudCertificadosBean.C_FECHAMODIFICACION,CerSolicitudCertificadosBean.C_FECHAESTADO};
		
		// INICIO BLOQUE SINCRONIZADO 
		//obtenerContadorSinronizado(gc, idInstitucionOrigen, idContador, idTipoProducto, idProducto, idProductoInstitucion, tieneContador, htNew);
		obtenerContadorSinronizado(gc, idInstitucion, idContador, idTipoProducto, idProducto, idProductoInstitucion, tieneContador, htNew, admSolicitud, beanSolicitud, claves, campos, usr);
		//FIN BLOQUE SINCRONIZADO 				

		///////////////////////////////////////////////
        
	    
        ////////////////////////////////////////////////////////
	    // GENERAR CERTIFICADO
        ////////////////////////////////////////////////////////
    	Certificado.generarCertificadoPDF(idTipoProducto, idProducto, idProductoInstitucion, 
                						  idInstitucion, idPlantilla, idPersona, fIn, fOut, 
                						  sRutaPlantillas, idSolicitud, idInstitucionOrigen,usarIdInstitucion, this.getUserBean(request),request);
	
    	admCert.guardarCertificado(beanSolicitud, fOut);

    	fOut.delete();
        
	    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO);

        // FIRMAR CERTIFICADO
        if (!admCert.firmarPDF(idSolicitud, idInstitucion)) {
	        ClsLogging.writeFileLog("Error al FIRMAR el PDF de la Solicitud: " + idSolicitud, 3);
	    } else {
	        htNew.put(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO);
	    }
        ////////////////////////////////////////////////////////
	    
        String[] campos2 = {CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO};
	    
        if (!admSolicitud.updateDirect(htNew, claves, campos2 ))
	    {
	        throw new ClsExceptions("Error al GENERAR el/los PDF/s");
	    }
	}
	

	/**
	 * generarVariosPDF. Este modo lo que hace ahora es coger todos los ids y generarlos. Para ellos coge su plantilla si es que está configurada y
	 * coge primero la plantilla fisica por defecto. SI no la tiene coge la primera que encuentra. Si no continua con otro certificado.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String generarVariosPDF(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{

		//UserTransaction tx=null;
		try{
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Comienzo control de transacciones
			//tx = usr.getTransactionPesada();
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    
		    CerPlantillasAdm admPlantillas = new CerPlantillasAdm(this.getUserBean(request));
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    
		    int contador=0;
		    int contadorErrores=0;
		    int contadorColegiadosNoOrigen=0;
		    
		    // AVISO: El orden de las solicitudes esta definido por como salen 
		    //en la pantalla (ver metodo que devuelve las solicitudes).
		    // Hasta el momento (2010-05-04), el orden es idsolicitud desc.
		    // Pero desde la JSP 'abrirListadoSolicitudes.jsp' se manda el
		    //listado de solicitudes (para generar los PDFs) en orden inverso,
		    //para que la generacion de los certificados (numero de certificado) 
		    //se haga en orden asc.
		    /** TODO
		     *  Habria que cambiar la comunicacion entre la JSP y el Action: 
		     * la JSP solo deberia enviar los idsolicitud 
		     * (o los filtros de busqueda, por ejemplo)
		     * y el Action calcularia todo lo demas
		     */
		    StringTokenizer st = null;
		    int contadorReg=1;
		    String tok=form.getIdsParaGenerarFicherosPDF();
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }

		    
		    String nombreSolicitud="";
		    while (st.hasMoreElements())
		    {
		    	String to = (String)st.nextToken();
		    	if (to.equals("undefined")) {
		    		continue;
		    	}
		        StringTokenizer st2 = new StringTokenizer(to, "%%");
	
		        String fechaSolicitud = st2.nextToken();
		        String idSolicitud = st2.nextToken();
		        
		        st2.nextToken();
		        
		        String idTipoProducto = st2.nextToken();
		        String idProducto = st2.nextToken();
		        String idProductoInstitucion = st2.nextToken();
		        String idInstitucion = st2.nextToken();
		        String idPersona = st2.nextToken();
		        String idInstitucionOrigen = st2.nextToken();
	
		        nombreSolicitud="[Institucion:"+idInstitucion+"][Solicitud:"+idSolicitud + "][fecha:"+fechaSolicitud+"]";
		        
		        // RGG obtengo la plantilla por defecto para cada certificado
		        String idPlantilla = admPlantillas.obtenerPlantillaDefecto(idInstitucion,idTipoProducto,idProducto, idProductoInstitucion);	

		        // RGG 28/03/2007 CAMBIO FINAL PARA OBTENER LA PERSONA DEL CERTIFICADO
		        boolean usarIdInstitucion = false;
				boolean colegiadoEnOrigen = true;
				CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);
				PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.getUserBean(request));
				Vector v=admProd.select("WHERE "+PysProductosInstitucionBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+"="+idTipoProducto+" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+"="+idProducto+" AND "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+"="+idProductoInstitucion);
				PysProductosInstitucionBean beanProd=null;
				if (v!=null && v.size()>0) {
					beanProd = (PysProductosInstitucionBean) v.get(0);
				}
				
				if (beanProd.getTipoCertificado().equalsIgnoreCase("C")) {
					// Es un certificado normal 
					// se usa siempre la institucion
					colegiadoEnOrigen=true;
					usarIdInstitucion=true;
				} else if (beanProd.getTipoCertificado().equalsIgnoreCase("M")) {
					// Es un comunicado
					// Compruebo si está en origen. Si no lanzo mensaje e impido seguir.
					colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
					// RGG siempre idinstitucion
					usarIdInstitucion=true;
				} else {
					// Es una diligencia
					colegiadoEnOrigen = true;
					usarIdInstitucion=true;
				}
		        
		        String sEstadoCertificado = null;
	
		        if (colegiadoEnOrigen) {
		        
		        	// Obtenemos cada vez los paraemtros por si cambia la institucion (Que no creo que lo haga, pero bueno...)
		        	GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
			        String sRutaDB = admParametros.getValor(idInstitucion, "CER" ,"PATH_CERTIFICADOS", "");
			        if (sRutaDB==null || sRutaDB.equals(""))
			        {
			            throw new ClsExceptions("No se ha encontrado el parámetro PATH_CERTIFICADOS en la BD");
			        }
			        String sRutaPlantillas = admParametros.getValor(idInstitucion, "CER" ,"PATH_PLANTILLAS", "");
			        if (sRutaPlantillas==null || sRutaPlantillas.equals(""))
			        {
			            throw new ClsExceptions("No se ha encontrado el parámetro PATH_PLANTILLAS en la BD");
			        }
			        sRutaPlantillas += File.separator + idInstitucion;
		
			        String sAux = idInstitucion + "_" + idSolicitud;
			        
			        sRutaDB += File.separator + "tmp";
			        
			        File fDirTemp = new File(sRutaDB);
			        fDirTemp.mkdirs();
		
			        File fOut = new File(fDirTemp.getPath() + File.separator + sAux + "_" + System.currentTimeMillis() + ".pdf");
			        File fIn = new File(fOut.getPath() + ".tmp");
			        
			        fOut.deleteOnExit();
			        fIn.deleteOnExit();
			        
			        try {
			        	//tx.begin();
			        	
			        	//////  UNIFICACION PARA LOS 3 METODOS DE GENERAR PDF //////////
			        	almacenarCertificado(idInstitucion, idSolicitud, request, beanProd, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, idPersona, fIn, fOut, 
                						  sRutaPlantillas, idInstitucionOrigen,usarIdInstitucion, usr);
					    
				        contador++;
				        
						//tx.commit();

			        
			        } catch (SIGAException e) {
			        	ClsLogging.writeFileLog("----- ERROR APROBAR Y GENERAR PDF -----",4);
			    		ClsLogging.writeFileLogError("ERROR EN APROBAR Y GENERAR PDF MASIVO. SOLICITUD:" +nombreSolicitud+ " Error:" + e.getLiteral(userBean.getLanguage()),e, 3);
			        	contadorErrores++;
		        		
			        	//tx.rollback();
		        	    
				       /* if (contadorReg==1) {
			        	    throw e;
			        	} else {*/
			        		sEstadoCertificado = CerSolicitudCertificadosAdm.K_ESTADO_CER_ERRORGENERANDO;
			        	//}
		            }
		
			        fIn.delete();
		        } 
		        else {
		        	
		        	// NO ES colegiadoEnOrigen
		        	ClsLogging.writeFileLog("----- ERROR APROBAR Y GENERAR PDF -----",4);
			        ClsLogging.writeFileLog("ERROR EN APROBAR Y GENERAR PDF MASIVO.:"+nombreSolicitud+" Error: El cliente no es colegiadoEnOrigen, idpersona=" + idPersona,3);
		        	contadorErrores++;
		        	contadorColegiadosNoOrigen++;
		        	if (contadorReg==1) {
	
		        		String mensaje = "messages.error.solicitud.clienteEsColegiado";
		        		if (mensaje==null) mensaje = "";
		        		String[] datos = {""+contador};
		        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
		        		
		        		request.setAttribute("mensaje",mensaje);	

		        	    return "exitoConString";
		        	} 
		        	else {
		        		sEstadoCertificado = CerSolicitudCertificadosAdm.K_ESTADO_CER_ERRORGENERANDO;
		        	}
		        }
		    } // WHILE 
			
			String mensaje="";
			String tipoAlert="";
		    if (contadorErrores==0) {
		    	// No hay errores
		    	String[] datos = {""+contador};
		    	tipoAlert="success";
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.OK", datos, userBean.getLanguage());
		    } else if(contadorErrores>0&&contadorColegiadosNoOrigen>0){
		    	// Hay errores debidos a colegiados que no estan en el colegio de origen
		    	tipoAlert=contador==0?"error":"warning";
		    	String[] datos = {""+contador,""+contadorErrores};
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF", datos, userBean.getLanguage());
		    } else {
		    	tipoAlert=contador==0?"error":"warning";
		    	// Hay errores en la generacion de los certificados
		    	String[] datos = {""+contador,""+contadorErrores};
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.KO", datos, userBean.getLanguage());
		    }
		    request.setAttribute("estiloMensaje",tipoAlert);	
			request.setAttribute("mensaje",mensaje);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
	    return "exitoConString";
	}
   
	protected String generarPDF(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		//UserTransaction tx=null;
		try	{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Comienzo control de transacciones
			//tx = usr.getTransactionPesada();
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    
		    int contador=0;
		    
		    
		    StringTokenizer st = null;
		    int contadorReg=1;
		    String tok=form.getIdsParaGenerarFicherosPDF();
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } 
		    catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }

		    int contadorErrores=0;
		    int contadorColegiadosNoOrigen=0;
		    
		    while (st.hasMoreElements())
		    {
		        StringTokenizer st2 = new StringTokenizer(st.nextToken(), "%%");
	
		        String fechaSolicitud = st2.nextToken();
		        String idSolicitud = st2.nextToken();
		        
		        st2.nextToken();
		        
		        String idTipoProducto = st2.nextToken();
		        String idProducto = st2.nextToken();
		        String idProductoInstitucion = st2.nextToken();
		        String idInstitucion = st2.nextToken();
		        String idPersona = st2.nextToken();
		        String idInstitucionOrigen = st2.nextToken();
		        String idPlantilla = st2.nextToken();	
		        
		        // RGG 28/03/2007 CAMBIO FINAL PARA OBTENER LA PERSONA DEL CERTIFICADO
		        boolean usarIdInstitucion = false;
				boolean colegiadoEnOrigen = true;
				CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);
				PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.getUserBean(request));
				Vector v=admProd.select("WHERE "+PysProductosInstitucionBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+"="+idTipoProducto+" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+"="+idProducto+" AND "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+"="+idProductoInstitucion);
				PysProductosInstitucionBean beanProd=null;
				if (v!=null && v.size()>0) {
					beanProd = (PysProductosInstitucionBean) v.get(0);
				}
				
				if (beanProd.getTipoCertificado().equalsIgnoreCase("C")) {
					// Es un certificado normal 
					// se usa siempre la institucion
					colegiadoEnOrigen=true;
					usarIdInstitucion=true;
				} else if (beanProd.getTipoCertificado().equalsIgnoreCase("M")) {
					// Es un comunicado
					// Compruebo si está en origen. Si no lanzo mensaje e impido seguir.
					colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
					// RGG cojo los datos de la institucion en cualquier caso.
					usarIdInstitucion=true;
				} else {
					// Es una diligencia
					boolean esDeConsejo=admCer.esConsejo(idInstitucion);
					if (esDeConsejo) {
						// Se trata de CGAE
						// Compruebo si está en origen. Si no pregunto si desea continuar utilizando al cliente de CGAE.
						colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
						// RGG siempre la institucion aunque exista el cliente en origen
						usarIdInstitucion=true;
						if (!colegiadoEnOrigen) {
							if (request.getParameter("paraConsejo")==null) {
								
						        request.setAttribute("PREG_PDF_idsParaGenerarFicherosPDF",form.getIdsParaGenerarFicherosPDF());
						        request.setAttribute("PREG_PDF_existe",new Boolean(colegiadoEnOrigen).toString());
						        
								return "preguntaDireccionGeneraPDF";
							} else {
								colegiadoEnOrigen=true;
								usarIdInstitucion=true;
							}
						}
					}
				}
	
		        
		        String sEstadoCertificado = null;
	
		        if (colegiadoEnOrigen) {
		        
			        GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
		
		            String sRutaDB = admParametros.getValor(idInstitucion, "CER" ,"PATH_CERTIFICADOS", "");
		
			        if (sRutaDB==null || sRutaDB.equals(""))
			        {
			            throw new ClsExceptions("No se ha encontrado el parámetro PATH_CERTIFICADOS en la BD");
			        }
			        
			        String sRutaPlantillas = admParametros.getValor(idInstitucion, "CER" ,"PATH_PLANTILLAS", "");
			        
			        if (sRutaPlantillas==null || sRutaPlantillas.equals(""))
			        {
			            throw new ClsExceptions("No se ha encontrado el parámetro PATH_PLANTILLAS en la BD");
			        }
			        
			        sRutaPlantillas += File.separator + idInstitucion;
		
			        String sAux = idInstitucion + "_" + idSolicitud;
			        
			        sRutaDB += File.separator + "tmp";
			        
			        File fDirTemp = new File(sRutaDB);
			        fDirTemp.mkdirs();
		
			        File fOut = new File(fDirTemp.getPath() + File.separator + sAux + "_" + System.currentTimeMillis() + ".pdf");
			        File fIn = new File(fOut.getPath() + ".tmp");
			        
			        fOut.deleteOnExit();
			        fIn.deleteOnExit();
			        
			        try {
			        	
					   // tx.begin();
					    
			        	//////  UNIFICACION PARA LOS 3 METODOS DE GENERAR PDF //////////
			        	almacenarCertificado(idInstitucion, idSolicitud, request, beanProd, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, idPersona, fIn, fOut, 
                						  sRutaPlantillas, idInstitucionOrigen,usarIdInstitucion, usr);					    
				        
				        contador++;
			        
						//tx.commit();


			        } catch (SIGAException e) {
				        ClsLogging.writeFileLogError("Error GENERAL al aprobar y generar el certificado PDF: " + e.getLiteral(userBean.getLanguage()),e, 3);
				        contadorErrores++;
		        		//tx.rollback();
		        		
			        	/*if (contadorReg==1) {
	
			        		String mensaje = e.getLiteral(userBean.getLanguage());
			        		if (mensaje==null) mensaje = "";
			        		String[] datos = {""+contador};
			        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
			        		
			        		request.setAttribute("mensaje",mensaje);	
			        		
			        	    //return "exitoConString";
			        		throw e;
	
			        	} else {*/
			        		sEstadoCertificado = CerSolicitudCertificadosAdm.K_ESTADO_CER_ERRORGENERANDO;
			        	//}
		            }
		
			        fIn.delete();
			        
			        
	
		        } else {
		        	
			        contadorErrores++;
			        contadorColegiadosNoOrigen++;
		        	// NO ES colegiadoEnOrigen
			        ClsLogging.writeFileLog("Error al genera el certificado PDF: El cliente no es colegiadoEnOrigen, idpersona=" + idPersona,3);
		        	if (contadorReg==1) {
	
		        		String mensaje = "messages.error.solicitud.clienteEsColegiado";
		        		if (mensaje==null) mensaje = "";
		        		String[] datos = {""+contador};
		        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
		        		
		        		request.setAttribute("mensaje",mensaje);	
		        	    
		        		return "exitoConString";
	
		        	} else {
		        		sEstadoCertificado = CerSolicitudCertificadosAdm.K_ESTADO_CER_ERRORGENERANDO;
		        	}
		        	
		        }
			    
		    }
		    
			String mensaje="";
			String tipoAlert="";
		    if (contadorErrores==0) {
		    	// No hay errores
		    	String[] datos = {""+contador};
		    	tipoAlert="success";
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.OK", datos, userBean.getLanguage());
		    } else if(contadorErrores>0&&contadorColegiadosNoOrigen>0){
		    	// Hay errores debidos a colegiados que no estan en el colegio de origen
		    	tipoAlert=contador==0?"error":"warning";
		    	String[] datos = {""+contador,""+contadorErrores};
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF", datos, userBean.getLanguage());
		    } else {
		    	tipoAlert=contador==0?"error":"warning";
		    	// Hay errores en la generacion de los certificados
		    	String[] datos = {""+contador,""+contadorErrores};
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.KO", datos, userBean.getLanguage());
		    }
		    request.setAttribute("estiloMensaje",tipoAlert);	
			request.setAttribute("mensaje",mensaje);
		}/*catch (SIGAException e) {
			throw e;	
		}*/ 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
			
	    return "exitoConString";
	}

	
	protected String generarPDFUnaPlantilla(ActionMapping mapping, String IdsParaGenerarFicheros, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{

		//UserTransaction tx=null;
		try{
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// Comienzo control de transacciones
			//tx = usr.getTransactionPesada();
			
		  //  SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    
		    int contador=0;
		    
		    
		    StringTokenizer st = null;
		    int contadorReg=1;
		    String tok=IdsParaGenerarFicheros;
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }

		    int contadorErrores=0;
		    int contadorColegiadosNoOrigen=0;
			
		    while (st.hasMoreElements())
		    {
		        StringTokenizer st2 = new StringTokenizer(st.nextToken(), "%%");
	
		        String fechaSolicitud = st2.nextToken();
		        String idSolicitud = st2.nextToken();
		        
		        st2.nextToken();
		        
		        String idTipoProducto = st2.nextToken();
		        String idProducto = st2.nextToken();
		        String idProductoInstitucion = st2.nextToken();
		        String idInstitucion = st2.nextToken();
		        String idPersona = st2.nextToken();
		        String idInstitucionOrigen = st2.nextToken();
		        String idPlantilla = st2.nextToken();	

		        
		        // RGG 28/03/2007 CAMBIO FINAL PARA OBTENER LA PERSONA DEL CERTIFICADO
		        boolean usarIdInstitucion = false;
				boolean colegiadoEnOrigen = true;
				CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);
				PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.getUserBean(request));
				Vector v=admProd.select("WHERE "+PysProductosInstitucionBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+"="+idTipoProducto+" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+"="+idProducto+" AND "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+"="+idProductoInstitucion);
				PysProductosInstitucionBean beanProd=null;
				if (v!=null && v.size()>0) {
					beanProd = (PysProductosInstitucionBean) v.get(0);
				}
				
				if (beanProd.getTipoCertificado().equalsIgnoreCase("C")) {
					// Es un certificado normal 
					// se usa siempre la institucion
					colegiadoEnOrigen=true;
					usarIdInstitucion=true;
				} else if (beanProd.getTipoCertificado().equalsIgnoreCase("M")) {
					// Es un comunicado
					// Compruebo si está en origen. Si no lanzo mensaje e impido seguir.
					colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
					// RGG cojo los datos de la institucion en cualquier caso.
					usarIdInstitucion=true;
				} else {
					// Es una diligencia
					boolean esDeConsejo=admCer.esConsejo(idInstitucion);
					if (esDeConsejo) {
						// Se trata de CGAE
						// Compruebo si está en origen. Si no pregunto si desea continuar utilizando al cliente de CGAE.
						colegiadoEnOrigen = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
						// RGG siempre la institucion aunque exista el cliente en origen
						usarIdInstitucion=true;
						if (!colegiadoEnOrigen) {
							if (request.getParameter("paraConsejo")==null) {
								
						        request.setAttribute("PREG_PDF_idsParaGenerarFicherosPDF",IdsParaGenerarFicheros);
						        request.setAttribute("PREG_PDF_existe",new Boolean(colegiadoEnOrigen).toString());
						        
								return "preguntaDireccionGeneraPDF";
							} else {
								colegiadoEnOrigen=true;
								usarIdInstitucion=true;
							}
						}
					}
				}
		        
		        String sEstadoCertificado = null;
	
		        if (colegiadoEnOrigen) {
		        
			        GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
		
		            String sRutaDB = admParametros.getValor(idInstitucion, "CER" ,"PATH_CERTIFICADOS", "");
		
			        if (sRutaDB==null || sRutaDB.equals("")){
			            throw new ClsExceptions("No se ha encontrado el parámetro PATH_CERTIFICADOS en la BD");
			        }
			        
			        String sRutaPlantillas = admParametros.getValor(idInstitucion, "CER" ,"PATH_PLANTILLAS", "");
			        
			        if (sRutaPlantillas==null || sRutaPlantillas.equals("")){
			            throw new ClsExceptions("No se ha encontrado el parámetro PATH_PLANTILLAS en la BD");
			        }
			        
			        sRutaPlantillas += File.separator + idInstitucion;
		
			        String sAux = idInstitucion + "_" + idSolicitud;
			        
			        sRutaDB += File.separator + "tmp";
			        
			        File fDirTemp = new File(sRutaDB);
			        fDirTemp.mkdirs();
		
			        File fOut = new File(fDirTemp.getPath() + File.separator + sAux + "_" + System.currentTimeMillis() + ".pdf");
			        File fIn = new File(fOut.getPath() + ".tmp");
			        
			        fOut.deleteOnExit();
			        fIn.deleteOnExit();
			        
			        try {
			        	
			        	almacenarCertificado(idInstitucion, idSolicitud, request, beanProd, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, idPersona, fIn, fOut, 
                						  sRutaPlantillas, idInstitucionOrigen,usarIdInstitucion, usr);					    
				        contador++;

			        } catch (SIGAException e) {
				        ClsLogging.writeFileLogError("Error GENERAL al aprobar y generar el certificado PDF: " + e.getLiteral(userBean.getLanguage()),e, 3);
				        contadorErrores++;
		        		sEstadoCertificado = CerSolicitudCertificadosAdm.K_ESTADO_CER_ERRORGENERANDO;
		            }
		
			        fIn.delete();
			        
	
		        } else {
		        	
			        contadorErrores++;
		        	// NO ES colegiadoEnOrigen
			        contadorColegiadosNoOrigen++;
			        ClsLogging.writeFileLog("Error al genera el certificado PDF: El cliente no es colegiadoEnOrigen, idpersona=" + idPersona,3);
		        	if (contadorReg==1) {
	
		        		String mensaje = "messages.error.solicitud.clienteEsColegiado";
		        		if (mensaje==null) mensaje = "";
		        		String[] datos = {""+contador};
		        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
		        		
		        		request.setAttribute("mensaje",mensaje);	
		        	    
		        		return "exitoConString";
	
		        	} else {
		        		sEstadoCertificado = CerSolicitudCertificadosAdm.K_ESTADO_CER_ERRORGENERANDO;
		        	}
		        	
		        }
			    
		    }
		    
			String mensaje="";
			String tipoAlert="";
		    if (contadorErrores==0) {
		    	// No hay errores
		    	String[] datos = {""+contador};
		    	tipoAlert="success";
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.OK", datos, userBean.getLanguage());
		    } else if(contadorErrores>0&&contadorColegiadosNoOrigen>0){
		    	// Hay errores debidos a colegiados que no estan en el colegio de origen
		    	tipoAlert=contador==0?"error":"warning";
		    	String[] datos = {""+contador,""+contadorErrores};
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF", datos, userBean.getLanguage());
		    } else {
		    	tipoAlert=contador==0?"error":"warning";
		    	// Hay errores en la generacion de los certificados
		    	String[] datos = {""+contador,""+contadorErrores};
		    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.KO", datos, userBean.getLanguage());
		    }
			
		    request.setAttribute("estiloMensaje",tipoAlert);	
			request.setAttribute("mensaje",mensaje);	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
			
	    return "exitoConString";
	}

	protected String enviar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{

		try{
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
		    
		    Hashtable htSolicitud = new Hashtable();
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    
		    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    
		    CerSolicitudCertificadosBean solBean = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		    String sRutaBd = admSolicitud.getRutaCertificadoFichero(solBean,admSolicitud.getRutaCertificadoDirectorioBD(solBean));
		    
		    request.setAttribute("ruta", sRutaBd);	    
	    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
	    
	    return "envioModal";
	}

	
	protected String anular(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UserTransaction tx=null;
		
		try{
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN"); 
			tx = usr.getTransaction();
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    PysCompraAdm pysCompra= new PysCompraAdm(this.getUserBean((request)));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
		    String idPeticion = ((String)vOcultos.elementAt(5)).trim();
		    String idProducto = ((String)vOcultos.elementAt(6)).trim();
		    String idTipoProducto = ((String)vOcultos.elementAt(7)).trim();
		    String idProductoInstitucion = ((String)vOcultos.elementAt(8)).trim();
		    tx.begin();
	        if (idPeticion!=null && !idPeticion.equals("")){// Si existe idPeticion miramos si tambien existe un idFacturacion, si no existiera, borramos
	        	                                            // el registro de la tabla PYS_COMPRA
			    Hashtable htCompra = new Hashtable();
			    htCompra.put(PysCompraBean.C_IDINSTITUCION, idInstitucion);
			    htCompra.put(PysCompraBean.C_IDPETICION, idPeticion);
			    htCompra.put(PysCompraBean.C_IDPRODUCTO, idProducto);
			    htCompra.put(PysCompraBean.C_IDTIPOPRODUCTO, idTipoProducto);
			    htCompra.put(PysCompraBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
			    
			    
			    Vector vDatosCompra = pysCompra.selectByPK(htCompra);
			    
			    PysCompraBean beanCompra = (PysCompraBean)vDatosCompra.elementAt(0);
			    
			    if (beanCompra.getIdFactura()==null || beanCompra.getIdFactura().equals("")){
			    	
			    	if (!pysCompra.delete(htCompra)){
			    		tx.rollback();
			    		request.setAttribute("mensaje", "messages.updated.error");
			    	}
			    	
			    }
		    }	
	       
	        
	        Hashtable htSolicitud = new Hashtable();
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    
		    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    
		    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		    Hashtable htOld = beanSolicitud.getOriginalHash();
		    Hashtable htNew = (Hashtable)htOld.clone();
		    
		    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO);
		    htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO, "sysdate");
		    htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION, "sysdate");
		    
		    if (admSolicitud.update(htNew, htOld))
		    { 
		    	tx.commit();  
		        request.setAttribute("mensaje", "messages.updated.success");
		    }
	
		    else
		    {   tx.rollback();
		        request.setAttribute("mensaje", "messages.updated.error");
		    }
	    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		    
	    return "exito";

	}
	protected String denegar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		
		try{
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
	
		    Hashtable htSolicitud = new Hashtable();
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    
		    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    
		    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		    
		    if (!(""+beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND))
		    {
		        throw new SIGAException("messages.certificados.error.solicitudyaaceptada");
		    }
	
		    Hashtable htOld = beanSolicitud.getOriginalHash();
		    Hashtable htNew = (Hashtable)htOld.clone();
		    
		    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO);
		    htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO, "sysdate");
		    htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION, "sysdate");
	
		    if (admSolicitud.update(htNew, htOld))
		    {
		        request.setAttribute("mensaje", "messages.updated.success");
		    }
	
		    else
		    {
		        request.setAttribute("mensaje", "messages.updated.error");
		    }
	    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		    
	    return "exito";
	}

	protected String finalizar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		
		//UserTransaction tx=null;
		try{
			// Obtengo usuario 
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
			int contError = 0;
			
			Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
		    
		    Hashtable htSolicitud = new Hashtable();
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    //aalg: INC_10287_SIGA. Se unifica la manera de finalizar las solicitudes de certificados
		    finalizarCertificado(htSolicitud, request, response, contError);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		
	    return "exito";
	}

	protected String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UserTransaction tx=null;
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
	        CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
	        Vector vOcultos = form.getDatosTablaOcultos(0);
	        
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
	
		    Hashtable htSolicitud = new Hashtable();
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    
		    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    
		    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);

		    File fCertificado = admSolicitud.recuperarCertificado(beanSolicitud);
			
		    if(fCertificado==null || !fCertificado.exists()){
		    	throw new SIGAException("messages.general.error.ficheroNoExiste"); 
		    }

			// Comienzo control de transacciones
			tx = usr.getTransaction();

		    tx.begin();
		    
		    beanSolicitud.setFechaDescarga("SYSDATE");
		    if (!admSolicitud.updateDirect(beanSolicitud)) {
		    	throw new ClsExceptions("Error al actualizar la fecha de descarga: "+admSolicitud.getError());
		    }

		    request.setAttribute("nombreFichero", fCertificado.getName());
		    request.setAttribute("rutaFichero", fCertificado.getPath());
		    
		    tx.commit();
		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,tx); 
		}
		
		return "descargaFichero";
	}

	protected String asignarPlantillaCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		try{
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    
		    String sTemp = form.getIdsTemp();
		    StringTokenizer st = new StringTokenizer(sTemp, "%%");
		    
		    st.nextToken();
		    st.nextToken();
		    
		    String sCertificado = st.nextToken();
		    String idTipoProducto = st.nextToken();
		    String idProducto = st.nextToken();
		    String idProductoInstitucion = st.nextToken();
		    String idInstitucion = st.nextToken();
	
		    String porDefecto="";
		    
		    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
		    
		    Hashtable htPlantilla = new Hashtable();
		    htPlantilla.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucion);
		    htPlantilla.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		    
		    Vector vDatos = admPlantilla.select(htPlantilla);
		    
		    if (vDatos!=null)
		    {
		        for (int i=0; i<vDatos.size(); i++)
		        {
		            CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(i);
		            
		            if (beanPlantilla.getPorDefecto().equals("S"))
		            {
		                porDefecto=""+beanPlantilla.getIdPlantilla();
		                
		                i=vDatos.size();
		            }
		        }
		    }
	
		    request.setAttribute("certificado", sCertificado);
		    request.setAttribute("idInstitucion", idInstitucion);
		    request.setAttribute("idProducto", idProducto);
		    request.setAttribute("idTipoProducto", idTipoProducto);
		    request.setAttribute("idProductoInstitucion", idProductoInstitucion);
		    
		    request.setAttribute("porDefecto", porDefecto);
	    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		    
	    return "asignarPlantillaCertificado";
	}
	protected String comprobarNumPlantillas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		try{
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    
		    String sTemp = form.getIdsTemp();
		    StringTokenizer st = new StringTokenizer(sTemp, "%%");
		    
		    st.nextToken();
		    st.nextToken();
		    
		    String sCertificado = st.nextToken();
		    String idTipoProducto = st.nextToken();
		    String idProducto = st.nextToken();
		    String idProductoInstitucion = st.nextToken();
		    String idInstitucion = st.nextToken();
	
		    String porDefecto="";
		    
		    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
		    
		    Hashtable htPlantilla = new Hashtable();
		    htPlantilla.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucion);
		    htPlantilla.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htPlantilla.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		    
		    Vector vDatos = admPlantilla.select(htPlantilla);
		    
		    if (vDatos!=null){
		    	if (vDatos.size()==0){
		    		throw new SIGAException("messages.error.solicitud.noExistePlantilla");
		    	}else{
			    	if (vDatos.size()==1){// si solo hay una plantilla se genera directamente el PDF
				    	CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(0);
			            
			            
				    	sTemp+="%%"+beanPlantilla.getIdPlantilla();
				    	return generarPDFUnaPlantilla( mapping, sTemp, request,  response);
				    }else{// Si hay mas de una plantilla se muestra el combo para seleccionarla
				    	request.setAttribute("idsTemp",sTemp);
				    	 return "seleccionarPlantilla";
				    }
		    	}	
		    }
	
		    request.setAttribute("certificado", sCertificado);
		    request.setAttribute("idInstitucion", idInstitucion);
		    request.setAttribute("idProducto", idProducto);
		    request.setAttribute("idTipoProducto", idTipoProducto);
		    request.setAttribute("idProductoInstitucion", idProductoInstitucion);
		    
		    request.setAttribute("porDefecto", porDefecto);
	    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		 return "seleccionarPlantilla";
	  
	}
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		
		UserTransaction tx=null;
		String salida ="";
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    /*boolean isSolicitudColegio = false;
		    isSolicitudColegio = form.getIdInstitucionSolicitud()!=null && !form.getIdInstitucionSolicitud().equals(CerSolicitudCertificadosAdm.IDCGAE) && !form.getIdInstitucionSolicitud().substring(0,2).equals("30");
		    */
		    // validaciones
		    String idInstitucion = usr.getLocation();
	        CenInstitucionAdm instAdm = new CenInstitucionAdm(usr);
	        String idInstPadre = "";
	        if (form.getIdInstitucionOrigen() != null && !form.getIdInstitucionOrigen().equals("")) { 
	        	idInstPadre = String.valueOf(instAdm.getInstitucionPadre(Integer.valueOf(form.getIdInstitucionOrigen())));
	        }
	        
	        boolean instCorrecta = idInstitucion.equals(CerSolicitudCertificadosAdm.IDCGAE) ||
	        					   idInstitucion.equals(form.getIdInstitucionOrigen())      ||
	        					   idInstitucion.equals(form.getIdInstitucionDestino())     ||
	        					   idInstitucion.equals(idInstPadre);
	        if (!instCorrecta){
	        	throw new SIGAException("messages.certificados.error.institucionnoadecuada");
	        }
			
	        // RGG ¡ATENCION!
	        // SI EL FUTURO PARAMETRO QUE INDICARA QUE SE ENVIAN COMUNICADOS AL ORIGEN 
	        // ESTARIA ACTIVADO, ENTONCES HABRIA QUE COMPROBAR LO SIGUIENTE: 
	        // - Que existe un producto comunicado en el origen (tal y como se hace en sol. de comun. y dilig.
	        
			// Obtengo la transaccion
			tx = usr.getTransactionPesada();
			
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    // obtengo la solicitud
		    Hashtable ht = new Hashtable();
		    ht.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,form.getIdInstitucion());
		    ht.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,form.getIdSolicitud());
		    CerSolicitudCertificadosBean bean = null;
		    Vector v = admSolicitud.selectByPK(ht);
		    if (v!=null && v.size()>0) {
		    	bean = (CerSolicitudCertificadosBean) v.get(0);
		    }
			// Comienzo control de transacciones
		    tx.begin();

		    if (!form.getFechaEmision().trim().equals("")) {
		    	bean.setFechaEmisionCertificado(GstDate.getApplicationFormatDate(usr.getLanguage(),form.getFechaEmision()));
		    }else{
		    	bean.setFechaEmisionCertificado(null);
		    }
		    if (!form.getFechaDescarga().trim().equals("")) {
		    	bean.setFechaDescarga(GstDate.getApplicationFormatDate(usr.getLanguage(),form.getFechaDescarga()));
		    }else{
		    	bean.setFechaDescarga(null);
		    }
		    
		    if (!form.getFechaSolicitud().trim().equals("")) {
		    	bean.setFechaSolicitud(GstDate.getApplicationFormatDate(usr.getLanguage(),form.getFechaSolicitud()));
		    }else{
		    	bean.setFechaSolicitud(null);
		    }		    
		    
		    if (!form.getFechaCobro().trim().equals("")) {
		    	bean.setFechaCobro(GstDate.getApplicationFormatDate(usr.getLanguage(),form.getFechaCobro()));
		    	
			    if (form.getCodigoBanco()!= null && !form.getCodigoBanco().trim().equals("")) {
			    	bean.setCbo_codigo(form.getCodigoBanco());
			    } else {
			    	bean.setCbo_codigo(null);
			    }
			    
			    if (form.getSucursalBanco() != null && !form.getSucursalBanco().trim().equals("")) {
			    	bean.setCodigo_sucursal(form.getSucursalBanco());
			    } else {
			    	bean.setCodigo_sucursal(null);
			    }			    	
		    	
		    }else{
		    	bean.setFechaCobro(null);
		    	bean.setCbo_codigo(null);
		    	bean.setCodigo_sucursal(null);
		    }		    
		    
		    if (!form.getFechaEntregaInfo().trim().equals("")) {
		    	bean.setFechaEntregaInfo(GstDate.getApplicationFormatDate(usr.getLanguage(),form.getFechaEntregaInfo()));
		    }else{
		    	bean.setFechaEntregaInfo(null);
		    }
		    
		    if (!form.getIdInstitucionOrigen().trim().equals("")) {
		    	bean.setIdInstitucionOrigen(new Integer(form.getIdInstitucionOrigen()));
		    } else {
		    	bean.setIdInstitucionOrigen(null);
		    }		    
		    	
		    if (!form.getIdInstitucionColegiacion().trim().equals("")) {
		    	bean.setIdInstitucionColegiacion(new Integer(form.getIdInstitucionColegiacion()));
		    } else {
		    	bean.setIdInstitucionColegiacion(null);
		    }

		    if (!form.getIdInstitucionDestino().trim().equals("")) {
		    	bean.setIdInstitucionDestino(new Integer(form.getIdInstitucionDestino()));
		    } else {
		    	bean.setIdInstitucionDestino(null);
		    }
		    if (!form.getComentario().trim().equals("")) {
		    	bean.setComentario(form.getComentario());
		    } else {
		    	bean.setComentario(null);
		    }
		    
		    if (!form.getMetodoSolicitud().equals("")) {
		    	bean.setMetodoSolicitud(form.getMetodoSolicitud());
		    } else {
		    	bean.setMetodoSolicitud(null);
		    }

		    bean.setFechaMod("sysdate");
		    if (!admSolicitud.updateDirect(bean)) {
		    	throw new ClsExceptions("Error al actualizar solicitud: "+admSolicitud.getError());
		    }

		    // No guarda cuando el texto es vacion
		    //  if (!form.getTextoSanciones().trim().equals("")) {
		    //Creamos el bean a modificar o insertar
		    CerSolicitudCertificadosTextoBean beanSolicitudCertificadosTexto = new CerSolicitudCertificadosTextoBean();
		    boolean isIncluirDeudas = UtilidadesString.stringToBoolean(form.getIncluirDeudas());
		    boolean isIncluirSanciones = UtilidadesString.stringToBoolean(form.getIncluirSanciones());
		    String incluirDeudas = "N";
		    if(isIncluirDeudas)
		    	incluirDeudas = "S";
		    String incluirSanciones = "N";
		    if(isIncluirSanciones)
		    	incluirSanciones = "S";
		    beanSolicitudCertificadosTexto.setIncluirDeudas(incluirDeudas);
		    beanSolicitudCertificadosTexto.setIncluirSanciones(incluirSanciones);
		    beanSolicitudCertificadosTexto.setTexto(form.getTextoSanciones());

		    admSolicitud.actualizaTextoCertificados(bean.getIdInstitucion().toString(), bean.getIdSolicitud().toString(), beanSolicitudCertificadosTexto);
		    	//admSolicitud.actualizaTextoSanciones(bean.getIdInstitucion().toString(), bean.getIdSolicitud().toString(), form.getTextoSanciones());
		    //	}

			tx.commit();

			// fin correcto
			salida = this.exitoModal("messages.updated.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,tx); 
		}
		    
	    return salida;
	}


	protected String copiarSanciones(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		
		UserTransaction tx=null;
		String salida ="";
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			tx=usr.getTransaction();
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    // obtengo la solicitud
		    Hashtable ht = new Hashtable();
		    ht.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,form.getIdInstitucion());
		    ht.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,form.getIdSolicitud());
		    CerSolicitudCertificadosBean bean = null;
		    Vector v = admSolicitud.selectByPK(ht);
		    if (v!=null && v.size()>0) {
		    	bean = (CerSolicitudCertificadosBean) v.get(0);
		    }

		    String textoSanciones = admSolicitud.obtenerTextoSancionesParaCertificado(usr.getLocation(),bean.getIdPersona_Des().toString());
		    
		    request.setAttribute("sanciones",textoSanciones);
		    
			// fin correcto
			salida = "copiarSanciones";
		
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		    
	    return salida;
	}

	protected String copiarHistorico(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		try {
			UsrBean usr = this.getUserBean(request);
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    // obtengo el historico
		    Hashtable ht = new Hashtable();
		    ht.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,form.getIdInstitucion());
		    ht.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,form.getIdSolicitud());
		    CerSolicitudCertificadosBean bean = null;
		    Vector v = admSolicitud.selectByPK(ht);
		    bean = (CerSolicitudCertificadosBean) v.get(0);
		   //String textoHitorico = admSolicitud.obtenerTextoHistoricoParaCertificado(usr.getLocation(),"" + bean.getIdPersona_Des(), usr);
		    String incluirLiteratura  = form.getIncluirLiteratura();
		    boolean isIncluirLiteratura =  incluirLiteratura!=null && incluirLiteratura.equals("on");
		    String textoHitorico = admSolicitud.getTextoHistoricoEstados(usr.getLocation(),"" + bean.getIdPersona_Des(), usr,
		    		isIncluirLiteratura);
		    request.setAttribute("sanciones", textoHitorico);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
	    return "copiarSanciones";
	}
	protected String historicoObservaciones(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		try {
			UsrBean usr = this.getUserBean(request);
			
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    // obtengo el historico
		    Hashtable ht = new Hashtable();
		    ht.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,form.getIdInstitucion());
		    ht.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,form.getIdSolicitud());
		    CerSolicitudCertificadosBean bean = null;
		    Vector v = admSolicitud.selectByPK(ht);
		    bean = (CerSolicitudCertificadosBean) v.get(0);
		    String incluirLiteratura  = form.getIncluirLiteratura();
		    boolean isIncluirLiteratura =  incluirLiteratura!=null && incluirLiteratura.equals("on");
		    String textoHitoricoConObservaciones = admSolicitud.getTextoHistoricoEstadosConObservaciones(usr.getLocation(),"" + bean.getIdPersona_Des(), usr,isIncluirLiteratura);
		    request.setAttribute("sanciones", textoHitoricoConObservaciones);
		    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
	    return "copiarSanciones";
	}
	
	protected String finalizarCertificados(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
	    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
	    String mensaje="";

	    try {

		    StringTokenizer st = null;
		    int contadorReg=1;
		    String tok=form.getIdsParaFinalizar();
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }

		    int contErrores=0;
		    while (st.hasMoreElements())
		    {
		    	String idSolicitud = "";
			    	String to = (String)st.nextToken();
			        StringTokenizer st2 = new StringTokenizer(to, "%%");
		
			        String fechaSolicitud = st2.nextToken();
			        idSolicitud = st2.nextToken();
			        st2.nextToken();
			        String idTipoProducto = st2.nextToken();
			        String idProducto = st2.nextToken();
			        String idProductoInstitucion = st2.nextToken();
			        String idInstitucion = st2.nextToken();
			        // nos saltamos la persona
			        st2.nextToken();
			        String idInstitucionOrigen = st2.nextToken();

			        /////////////////////////////////////////////////
				    Hashtable htSolicitud = new Hashtable();
				    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
				    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
				    //aalg: INC_10287_SIGA. Se unifica la manera de finalizar las solicitudes de certificados
				    finalizarCertificado(htSolicitud, request, response, contErrores);
		    }
		    
		    if (contErrores==0) {
		    	mensaje = "messages.certificados.finalizarCertificadosMasivo.success2";
		    } else {
		    	mensaje=UtilidadesString.getMensaje("messages.certificados.finalizarCertificadosMasivo.success",new String[] {new Integer(contErrores).toString()},userBean.getLanguage());
		    }
		     

	    }catch (Exception e){
	        this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
	    }
        return exitoRefresco(mensaje,request);

	}
    
	 //aalg: INC_10287_SIGA. Se unifica la manera de finalizar las solicitudes de certificados
	protected void finalizarCertificado(Hashtable htSolicitud, HttpServletRequest request, HttpServletResponse response, int contErrores) 
			throws SIGAException{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
	    UserTransaction tx = userBean.getTransactionPesada();
	    String idInstitucion=(String)htSolicitud.get(CerSolicitudCertificadosBean.C_IDINSTITUCION);
	    String idSolicitud=(String)htSolicitud.get(CerSolicitudCertificadosBean.C_IDSOLICITUD);
	    String mensaje="";
        CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		
        try{
        	
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		    
		    if (!((""+beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)) &&
		        !((""+beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)))
			{

			    //RGG 05-09-2005 anhado al estado generado el de firmado ya que no parece que sea nunca generado. Pasa directamente a firmado. 
			    if (!(""+beanSolicitud.getIdEstadoCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) &&
				    !(""+beanSolicitud.getIdEstadoCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO))
			    {
			        throw new SIGAException("messages.certificados.error.nogenerado");
			    }

			    Hashtable htOld = beanSolicitud.getOriginalHash();
			    Hashtable htNew = (Hashtable)htOld.clone();
			    htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION,"sysdate");
			    htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
			    
			    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
			    
			    tx.begin();
			    if (admSolicitud.update(htNew, htOld))
			    {
			        request.setAttribute("mensaje", "messages.updated.success");
			        
			        // YGE 07/09/2005 Si todo ha ido bien insertamos el registro en la tabla PysCompra
			        // MAV 13/09/2005 Resolución incidencia
			        
			        Hashtable claves = new Hashtable();
					UtilidadesHash.set(claves, CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
					UtilidadesHash.set(claves, CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
					CerSolicitudCertificadosAdm solicitudAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
					CerSolicitudCertificadosBean solicitudBean = (CerSolicitudCertificadosBean) solicitudAdm.selectByPK(claves).get(0);
		
					claves.clear();
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucion);
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solicitudBean.getPpn_IdTipoProducto());
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDPRODUCTO, solicitudBean.getPpn_IdProducto());
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solicitudBean.getPpn_IdProductoInstitucion());
					PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
					
					// MAV 12/9/2005 Resolucion incidencia
					//PysProductosSolicitadosBean productoBean = (PysProductosSolicitadosBean) productoAdm.selectByPK(claves).get(0);
					Vector productosInstitucion = productoAdm.selectByPK(claves);
					if (productosInstitucion.isEmpty()){
				    	tx.rollback();
				        throw new SIGAException("messages.certificado.error.noFinalizacion");
					}
					else{
						PysProductosInstitucionBean productoBean = (PysProductosInstitucionBean) productosInstitucion.get(0);
						if (productoBean.getTipoCertificado().equalsIgnoreCase("C")){
							claves.clear();
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, solicitudBean.getPpn_IdTipoProducto());
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, solicitudBean.getIdPeticionProducto());
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, solicitudBean.getPpn_IdProducto());
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, solicitudBean.getPpn_IdProductoInstitucion());
							PysProductosSolicitadosAdm productoSolicitadoAdm = new PysProductosSolicitadosAdm(this.getUserBean(request));
							Vector productosSolicitados = productoSolicitadoAdm.selectByPK(claves); 
							if (productosSolicitados.isEmpty()){
						    	tx.rollback();
						        throw new SIGAException("messages.certificado.error.noFinalizacion");
							}
							else{
								PysProductosSolicitadosBean productoSolicitadoBean = (PysProductosSolicitadosBean) productosSolicitados.get(0);						
								PysCompraBean compraBean = new PysCompraBean ();
								compraBean.setCantidad(productoSolicitadoBean.getCantidad());
								compraBean.setFecha("sysdate");
	//							compraBean.setIdFormaPago(productoSolicitadoBean.getIdFormaPago());
								
								// Si el check Cobrado está activo  el cliente es el que paga y se le asigna automaticamente
						  		// la forma de pago en metálico
								compraBean.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_METALICO));
								compraBean.setIdInstitucion(productoSolicitadoBean.getIdInstitucion());
								compraBean.setIdPeticion(productoSolicitadoBean.getIdPeticion());
								compraBean.setIdProducto(productoSolicitadoBean.getIdProducto());
								compraBean.setIdProductoInstitucion(productoSolicitadoBean.getIdProductoInstitucion());
								compraBean.setIdTipoProducto(productoSolicitadoBean.getIdTipoProducto());
								compraBean.setImporteUnitario(productoSolicitadoBean.getValor());
								compraBean.setIdCuenta(productoSolicitadoBean.getIdCuenta());
								compraBean.setNoFacturable(productoSolicitadoBean.getNoFacturable());
								
								compraBean.setIdPersona(productoSolicitadoBean.getIdPersona());
	//							 PDM: si el certificado tiene Cobrado = 0 y parametrizado la facturacion al colegio, se le factura al Colegio Destino o Facturable
								GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
						        String sFacturaColegio = admParametros.getValor(idInstitucion, "CER" ,"FACTURACION_COLEGIO", "");
						        if (sFacturaColegio!=null && sFacturaColegio.equals("1") && ( beanSolicitud.getFechaCobro() == null || beanSolicitud.getFechaCobro().equals("")|| beanSolicitud.getFechaCobro().equals("0"))){
								//if ( beanSolicitud.getFechaCobro() == null || beanSolicitud.getFechaCobro().equals("")|| beanSolicitud.getFechaCobro().equals("0")) {
									if (beanSolicitud.getIdInstitucionDestino() == null || beanSolicitud.getIdInstitucionDestino().equals("") )
									{ 
										tx.rollback();
								        throw new SIGAException("messages.certificado.error.noExisteColegioFacturable");
										}
									else {
										// obtener IDPERSONA de la Institucion  Destino o Facturable
										CenInstitucionAdm cenInstitucion = new CenInstitucionAdm(this.getUserBean(request));
										Hashtable datosInstitucion = new Hashtable();
										datosInstitucion.put(CenInstitucionBean.C_IDINSTITUCION,beanSolicitud.getIdInstitucionDestino());
										Vector v_datosInstitucion = cenInstitucion.selectByPK(datosInstitucion);
										CenInstitucionBean b = (CenInstitucionBean) v_datosInstitucion.get(0);
										//compraBean.setIdPersona(new Long(b.getIdPersona().intValue()));
								    if (productoSolicitadoBean.getIdPersona().intValue()!=b.getIdPersona().intValue()){
											 compraBean.setIdPersonaDeudor(new Long(b.getIdPersona().intValue()));
										
	//									 comprobamos que es cliente del colegio que finaliza el certificado
										CenClienteAdm cenCliente=new CenClienteAdm(this.getUserBean(request));
										Hashtable datosCliente = new Hashtable();
										datosCliente.put(CenClienteBean.C_IDINSTITUCION,beanSolicitud.getIdInstitucion());
										datosCliente.put(CenClienteBean.C_IDPERSONA,b.getIdPersona());
										Vector v_datosCliente=cenCliente.selectByPK(datosCliente);
											if (v_datosCliente.size()==0){
												tx.rollback();
										        throw new SIGAException("messages.certificado.error.colegioNoEsCliente"); 
												
											}
											
	//									 	
									 // if (productoSolicitadoBean.getIdCuenta()!=null ) {// Esto significa que se ha solicitado un certificado por domiciliacion bancaria
	//								  	obtenemos IDCUENTA de la Institucion Presentación para cobrarle el certificado solicitado											  	
											RowsContainer rc = null;
										  	rc = new RowsContainer(); 
										  	int contador=0;
										  	Hashtable codigos=new Hashtable();
										  	String sql="select C."+CenCuentasBancariasBean.C_IDCUENTA+
											           " from "+CenCuentasBancariasBean.T_NOMBRETABLA+" C, "+CenBancosBean.T_NOMBRETABLA+" B"+
	                                                   " where C."+CenCuentasBancariasBean.C_CBO_CODIGO+" = B."+CenBancosBean.C_CODIGO;
										  	contador++;
										  	codigos.put(new Integer(contador),String.valueOf(b.getIdPersona().intValue()));
										  	sql+=      "   AND C."+CenCuentasBancariasBean.C_IDPERSONA+" = :"+contador;
										  	contador++;
										  	codigos.put(new Integer(contador),String.valueOf(beanSolicitud.getIdInstitucion().intValue()));
										  	sql+=      "   AND C."+CenCuentasBancariasBean.C_IDINSTITUCION+" = :"+contador+
	                                                   "   AND (C."+CenCuentasBancariasBean.C_ABONOCARGO+" = 'C' OR C."+CenCuentasBancariasBean.C_ABONOCARGO+" = 'T')" +
	                                                   "   AND ROWNUM=1 ";
										  	if (rc.queryBind(sql,codigos)) {
										  	  if (rc.size()>0){	
										  		Row fila = (Row) rc.get(0);
										  		String idCuentaPresentador = fila.getString(CenCuentasBancariasBean.C_IDCUENTA);
										  		//compraBean.setIdCuenta(new Integer(idCuentaPresentador));
										  		compraBean.setIdCuentaDeudor(new Integer(idCuentaPresentador));
										  	  }else{// Si no existe cuenta 	
										  	    tx.rollback();
										        throw new SIGAException("messages.certificado.error.NoExisteCuentaBancaria");
										  	  }
										  	}else{// Si no existe cuenta 	
										  		tx.rollback();
										        throw new SIGAException("messages.certificado.error.NoExisteCuentaBancaria");
										  	}
									 // }
	//									  Si el check Cobrado está desactivado la forma de pago es por domiciliacion bancaria
										  	compraBean.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA));
								    }else{
										compraBean.setIdPersonaDeudor(null);
										compraBean.setIdCuentaDeudor(null);	
										compraBean.setIdCuenta(null);
										compraBean.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA));
									}
										
										}	
								}
								 else {
										compraBean.setIdPersonaDeudor(null);
										compraBean.setIdCuentaDeudor(null);
										compraBean.setIdCuenta(null);// cuando el check de cobrado esta activado el cobro se le hace al cliente
										                             // en metalico luego no rellenamos el idcuenta. 
								 }

								//compraBean.setIdPersona(productoSolicitadoBean.getIdPersona());
								
								// RGG 29-04-2005 cambio para insertar la descripcion
								// buscamos la descripcion
								PysProductosInstitucionAdm pyspiAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
								Hashtable claves2 = new Hashtable();
								claves2.put(PysProductosInstitucionBean.C_IDINSTITUCION,productoBean.getIdInstitucion());
								claves2.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,productoBean.getIdTipoProducto());
								claves2.put(PysProductosInstitucionBean.C_IDPRODUCTO,productoBean.getIdProducto());
								claves2.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,productoBean.getIdProductoInstitucion());
								Vector vpi = pyspiAdm.selectByPK(claves2);
								String descripcion = "";
	//							 Obtenemos el nombre y apellidos del solicitante
								CenPersonaAdm datosPersona=new CenPersonaAdm(this.getUserBean(request));
								Hashtable hashPersona=new Hashtable();
								hashPersona.put(CenPersonaBean.C_IDPERSONA,productoSolicitadoBean.getIdPersona());
								Vector  vpersona=datosPersona.selectByPK(hashPersona);
								CenPersonaBean personaBean = (CenPersonaBean) vpersona.get(0);
								  String apellido1=personaBean.getApellido1();
								  String apellido2=personaBean.getApellido2();
								  String nombre=personaBean.getNombre();
								if (vpi!=null && vpi.size()>0) {
									PysProductosInstitucionBean b = (PysProductosInstitucionBean) vpi.get(0);
									descripcion = b.getDescripcion();
									//ACG en el caso de certifiados concatenamos el nºde certificado por si la facturacion se le hace al colegio
									descripcion = descripcion + " - NºCert: " + beanSolicitud.getPrefijoCer() + beanSolicitud.getContadorCer() + beanSolicitud.getSufijoCer()+" - "+apellido1+" "+apellido2+", "+nombre;
									if (descripcion.length()>150){
									  descripcion = descripcion.substring(0,149);
									}
								}
								compraBean.setDescripcion(descripcion);
								
								// Creo que el importe anticipado es cero
								compraBean.setImporteAnticipado(new Double(0));
								
								compraBean.setIva(productoSolicitadoBean.getPorcentajeIVA());
								PysCompraAdm compraAdm = new PysCompraAdm (this.getUserBean(request));
								try{
								if (!compraAdm.insert(compraBean)) {
									tx.rollback();
									request.setAttribute("mensaje", "messages.inserted.error");
								}
								else{
									tx.commit();
								}
								}catch (Exception e){
									tx.rollback();
									throw e;
								}
							}						
						}
						else{
							tx.commit();
						}
					}
			    }
			    else
			    {   tx.rollback();
			        request.setAttribute("mensaje", "messages.updated.error");
			    }
			}
        ////////////////////////////////////////////////
        }catch(Exception e){
        	contErrores++;
    		ClsLogging.writeFileLog("----- ERROR FINALIZACION -----",4);
    		ClsLogging.writeFileLog("ERROR EN FINALIZACION MASIVA. solicitud certificado: "+idSolicitud+" Error: "+e.toString(),4);
    		throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
        }
        
	}
	protected String facturacionRapida(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    UserTransaction tx = null;
	    String mensaje="";
	    String salida = "";
	    try {						
		    // datos llamada
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitudCertificado = ((String)vOcultos.elementAt(1)).trim();
		    
		    // Transacción
		    UsrBean usr = this.getUserBean(request);
			tx=usr.getTransaction();
			tx.begin();
		    
		    // administradores
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    PysCompraAdm admCompra = new PysCompraAdm(this.getUserBean(request));
		    Facturacion facturacion = new Facturacion(this.getUserBean(request));
		    FacFacturaAdm admFactura = new FacFacturaAdm(this.getUserBean(request));
		    
		    // Bloqueamos las tablas de factura y compra
		    admFactura.lockTable();
		    admCompra.lockTable();
		    
		    // Obtengo la peticion de compra
		    PysCompraBean beanCompra = admSolicitud.obtenerCompra(idInstitucion, idSolicitudCertificado);
		    
		    // Compruebo SI esta facturada la compra
		    if (beanCompra.getIdFactura()!=null && !beanCompra.getIdFactura().trim().equals("")) {
		    	// YA ESTÁ FACTURADA
		    	// LIBERAMOS EL BLOQUEO EN LAS TABLAS Y LA TRANSACCIÓN
		    	tx.rollback();
		    	// Comprobamos si no esta confirmada la facturacion, la confirmamos
	    		FacFacturacionProgramadaAdm adm = new FacFacturacionProgramadaAdm (this.getUserBean(request));
		    	FacFacturacionProgramadaBean b = adm.getFacturacionProgramadaDesdeCompra(beanCompra);
		    	if (b != null) {
			    	String fechaConfirmacion = b.getFechaConfirmacion(); 
		    		if (fechaConfirmacion == null || fechaConfirmacion.equals("") || b.getIdEstadoPDF().intValue() != FacEstadoConfirmFactBean.PDF_FINALIZADA.intValue()) {
					    try {
					    	b.setGenerarPDF("1");// Si se entramos por facturacion rapida se obliga siempre al generarPDF
					        facturacion.confirmarProgramacionFactura(b, request,true,null,false,true);
						} 
						catch (Exception e) {
							mensaje="messages.facturacionRapida.errorConfirmacion";
							return exitoRefresco(mensaje,request);
					    }
		    		}
		    	}
		   
		        //throw new SIGAException("messages.facturacionRapida.error.facturado");
//			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		        ReadProperties rp = new ReadProperties("SIGA.properties");
//		        String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
//		        String barraAlmacen = "";
//		        String nombreFicheroAlmacen = "";
//		        if (rutaAlmacen.indexOf("/") > -1){ 
//		        	barraAlmacen = "/";
//		        }
//		        if (rutaAlmacen.indexOf("\\") > -1){ 
//		        	barraAlmacen = "\\";
//		        }
		        
		        ////////////////////////////
		        // Antes:
				//	 nombreFicheroAlmacen = beanCompra.getIdFactura() +".pdf";
				//   rutaAlmacen += barraAlmacen + idInstitucion + barraAlmacen + nombreFicheroAlmacen;
		        // Ahora:
	        	FacFacturaAdm admFac = new FacFacturaAdm (this.getUserBean(request));
	        	/**********/
	        	InformeFactura inf = new InformeFactura(this.getUserBean(request));
		        CenClienteAdm cliAdm = new CenClienteAdm(this.getUserBean(request));
		        /**********/
	        	Hashtable h = new Hashtable ();
	        	UtilidadesHash.set(h,FacFacturaBean.C_IDINSTITUCION, beanCompra.getIdInstitucion());
	        	UtilidadesHash.set(h,FacFacturaBean.C_IDFACTURA, beanCompra.getIdFactura());
	        	boolean correcto=true;
	        	Vector v = admFac.selectByPK(h);
	        	
	        	if (v != null && v.size() == 1) {
	        		FacFacturaBean fac = (FacFacturaBean) v.get(0);
	        		/***********/
	        		String idPersona=fac.getIdPersona().toString();
	    			// Obtenemos el lenguaje del cliente 
	    			String lenguaje = cliAdm.getLenguaje(fac.getIdInstitucion().toString(),idPersona); 
	        		/**********/
	        		String nombreFactura="";
	        		if (fac.getNumeroFactura()!=null && !fac.getNumeroFactura().equals("")){
	        			nombreFactura=fac.getNumeroFactura();
	        		}else{
	        			nombreFactura=fac.getIdFactura();
	        		}
	        		CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
					Hashtable htCol = admCol.obtenerDatosColegiado(this.getUserBean(request).getLocation(),fac.getIdPersona().toString(),this.getUserBean(request).getLanguage());
		  			String nColegiado = "";
		  			if (htCol!=null && htCol.size()>0) {
		  			    nColegiado = (String)htCol.get("NCOLEGIADO_LETRADO");
		  			}	
//				        nombreFicheroAlmacen = UtilidadesString.validarNombreFichero(nColegiado+"-"+nombreFactura +".pdf");
//				        rutaAlmacen += barraAlmacen + idInstitucion + barraAlmacen + fac.getIdSerieFacturacion() + "_" + fac.getIdProgramacion() + barraAlmacen + nombreFicheroAlmacen;
			        
			        /**********************/
//				        File directorio1 = new File(rutaAlmacen);// Si el fichero no existe en el directorio sRutaJava se genera
//			  			if (directorio1==null || !directorio1.exists()){
					 File filePDF = inf.generarFactura(request,lenguaje.toUpperCase(),this.getUserBean(request).getLocation(),fac.getIdFactura(),nColegiado);
						 if (filePDF==null) {
						    throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");				
						} else {
							request.setAttribute("nombreFichero", filePDF.getName());
							request.setAttribute("rutaFichero", filePDF.getAbsolutePath());
							request.setAttribute("borrarFichero", "true");
							correcto = true;
						}
					if (!filePDF.exists()){
						throw new SIGAException("messages.general.error.ficheroNoExisteReintentar");
					}
				    return "descargaFichero";				    
		  			/*****************/
	        	} else {
	        		throw new SIGAException("messages.abonos.compensacionManual.noExisteFactura");
	        	}
		        ////////////////////////////
		        
//				File fileFAC = new File(rutaAlmacen);
//			    if (!fileFAC.exists())  {
//			        throw new SIGAException("messages.general.error.ficheroNoExiste");
//			    }		        							
		    }
		   
		    
			// CONTROL DE NUMERO FACTURAS (MENSAJE)
			if (request.getParameter("validado")==null || !request.getParameter("validado").equals("1")) {
			    // hay que comprobar
			    int numero = admCompra.compruebaNumeroFacturas(beanCompra);
			    if (numero>1) {
			        request.setAttribute("numeroFacturas",new Integer(numero));
			        request.setAttribute("mensaje","messages.facturacionRapida.aviso.variasFacturas");
			        salida =  "preguntaNumeroFacturas";
			        // LIBERAMOS EL BLOQUEO DE LAS TABLAS Y LA TRANSACCIÓN
			        tx.rollback();
			        return salida;
			    }
			}
			
			FacSerieFacturacionBean serieFacturacionTemporal = null;
			FacFacturacionProgramadaBean programacion = null;
			Vector facts = null;
		    try {
			    
			    // PASO 0: ANTES DE FACTURAR APUNTO EL IMPORTE TOTAL COMO IMPORTE ANTICIPADO
			    double importe = (beanCompra.getCantidad().intValue() * beanCompra.getImporteUnitario().doubleValue()) * (1+(beanCompra.getIva().doubleValue()/100));
			    beanCompra.setImporteAnticipado(new Double(importe));
			    if (!admCompra.updateDirect(beanCompra)) {
			        throw new ClsExceptions("Error al actualizar el importe anticipado: "+admCompra.getError());
			    }
			    
			    // PASO 1: FACTURACION RAPIDA (GENERACION)
			    serieFacturacionTemporal =  facturacion.procesarFacturacionRapidaCompraCertificado(beanCompra);
			    
			    // Aqui obtengo las facturas implicadas
			    FacFacturaAdm admF = new FacFacturaAdm(this.getUserBean(request));
			    //facts = admF.getFacturasSerieFacturacion(serieFacturacionTemporal);
			    
			    // PASO 2: DESHACER RELACIONES TEMPORALES
			    programacion = facturacion.restaurarSerieFacturacionGenerica(serieFacturacionTemporal);
		        
			    // PASO 3: FACTURACION RAPIDA (CONFIRMACION)
				// BNS INCLUYO LA CONFIRMACIÓN EN LA TRANSACCIÓN
			    facturacion.confirmarProgramacionFactura(programacion, request,true,null,false,true, tx);
			    
			    if (Status.STATUS_ACTIVE  == tx.getStatus())
			    	tx.commit();

		    } catch (Exception e) {
			    try {
			    	tx.rollback();
			    } catch (Exception ee) {}

			    throw e;
		    }		    
			
			/*********************pdm***********/
		 if (programacion.getGenerarPDF().trim().equals("1")) {
			// Volvemos a hacer la consulta sobre la tabla pyscompra para recuperar el idfactura
			  beanCompra = admSolicitud.obtenerCompra(idInstitucion, idSolicitudCertificado);
			  FacFacturaAdm admFac = new FacFacturaAdm (this.getUserBean(request));
			  FacFacturaBean fac=new FacFacturaBean();
			  Hashtable h = new Hashtable ();
	        	UtilidadesHash.set(h,FacFacturaBean.C_IDINSTITUCION, beanCompra.getIdInstitucion());
	        	UtilidadesHash.set(h,FacFacturaBean.C_IDFACTURA, beanCompra.getIdFactura());
	        	String nombreFicheroAlmacen="";
	        	 facts= admFac.selectByPK(h);
	        	
			 /*************************************/
			// devolver factura
     	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
    		rutaAlmacen += ClsConstants.FILE_SEP+idInstitucion;
			rutaAlmacen+=ClsConstants.FILE_SEP;
		    String rutaServidor =
		    	Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava"))+
		    	ClsConstants.FILE_SEP+idInstitucion;

			if (facts==null || facts.size()==0) {
			    throw new SIGAException("messages.facturacionRapida.noFactura");
			} else{
				CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
					
			if (facts.size()==1) {
			    /*FacFacturaBean bf = (FacFacturaBean) facts.get(0);
			    rutaAlmacen += bf.getIdFactura()+".pdf";*/
				
	  					 

				/*********************pdm***********/
				 fac = (FacFacturaBean) facts.get(0);
				 String nombreFactura="";
				 if (fac.getNumeroFactura()!=null && !fac.getNumeroFactura().equals("")){
        			nombreFactura=fac.getNumeroFactura();
        		}else{
        			nombreFactura=fac.getIdFactura();
        		}
				 Hashtable htCol = admCol.obtenerDatosColegiado(this.getUserBean(request).getLocation(),fac.getIdPersona().toString(),this.getUserBean(request).getLanguage());
		  			String nColegiado = "";
		  			if (htCol!=null && htCol.size()>0) {
		  			    nColegiado = (String)htCol.get("NCOLEGIADO_LETRADO");
		  			}	
		         nombreFicheroAlmacen = UtilidadesString.validarNombreFichero(nColegiado+"-"+nombreFactura +".pdf");
			       // String nombreFicheroAlmacen = UtilidadesString.validarNombreFichero(bf.getNumeroFactura() +".pdf");
			       // rutaAlmacen += ClsConstants.FILE_SEP + bf.getIdSerieFacturacion() + "_" + bf.getIdProgramacion() + ClsConstants.FILE_SEP + nombreFicheroAlmacen;
				    rutaAlmacen += ClsConstants.FILE_SEP + fac.getIdSerieFacturacion()+ "_" + fac.getIdProgramacion() + ClsConstants.FILE_SEP;
				/******************************************************************************************/
				    
			    File filePDF = new File(rutaAlmacen+ nombreFicheroAlmacen);
			    if (!filePDF.exists())  {
			        return exito("messages.facturacionRapida.aviso.esperaDescarga",request);
			    }
			    request.setAttribute("nombreFichero", filePDF.getName());
				request.setAttribute("rutaFichero", filePDF.getPath());			
				request.setAttribute("borrarFichero", "true");
				salida = "descargaFichero";
			} else {
			    ArrayList ficherosPDF= new ArrayList();
			    String nombreFactura="";
			    for (int i=0;i<facts.size();i++) {
				    FacFacturaBean bf = (FacFacturaBean) facts.get(i);
				   
					 if (bf.getNumeroFactura()!=null && !bf.getNumeroFactura().equals("")){
	        			nombreFactura=bf.getNumeroFactura();
	        		}else{
	        			nombreFactura=bf.getIdFactura();
	        		}
				    Hashtable htCol = admCol.obtenerDatosColegiado(this.getUserBean(request).getLocation(),bf.getIdPersona().toString(),this.getUserBean(request).getLanguage());
		  			String nColegiado = "";
		  			if (htCol!=null && htCol.size()>0) {
		  			    nColegiado = (String)htCol.get("NCOLEGIADO_LETRADO");
		  			}	
				    String ruta = rutaAlmacen + UtilidadesString.validarNombreFichero(nColegiado+"-"+nombreFactura +".pdf");
				    File filePDF = new File(ruta);
				    ficherosPDF.add(filePDF);
			    }
				String nombreFicheroZIP="facturasGeneradas_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
				String rutaServidorDescargasZip=rutaServidor + File.separator;
				
				Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
				File fileZIP = new File(rutaServidorDescargasZip+nombreFicheroZIP + ".zip");
			    if (!fileZIP.exists())  {
			        throw new SIGAException("messages.general.error.ficheroNoExiste");
			    }
			    request.setAttribute("nombreFichero", nombreFicheroZIP + ".zip");
				request.setAttribute("rutaFichero", rutaServidorDescargasZip+nombreFicheroZIP + ".zip");			
				request.setAttribute("borrarFichero", "true");			
				salida = "descargaFichero";
			}
		 }
		 }else{
		 	throw new SIGAException("No se ha generado el PDF");
		 }
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
		return salida;
	}
	
	//aalg: inc_10359_siga. Se ha modificado para que siempre busque el contador en base de datos
	//e inmediatamente asigne en base de datos el número de certificado.
	//todo dentro del método sincronizado para que no se puedan ejecutar varios a la vez.
	public synchronized void obtenerContadorSinronizado(GestorContadores gc, String idInstitucion, String idContador, String idTipoProducto, String idProducto,
			String idProductoInstitucion, boolean tieneContador, Hashtable htNew,
			CerSolicitudCertificadosAdm admSolicitud, CerSolicitudCertificadosBean beanSolicitud,
			String[] claves, String[] campos, UsrBean usr) {

		UserTransaction tx=null;

		try {
			tx = usr.getTransaction();
			tx.begin();
			// obteniendo registro de contador de BD
			Hashtable contadorTablaHash = gc.getContador(new Integer(idInstitucion), idContador);

			if (!tieneContador && contadorTablaHash.get("MODO").toString().equals("0")) {
				// MODO REGISTRO. Se suponen siempre asi estos

				int numContador;
				numContador = new Integer((contadorTablaHash.get("CONTADOR").toString())).intValue();
				// En la tabla contador se guarda el ultimo nº utilizado por lo que el siguiente a utilizar el contador + 1

				numContador = numContador + 1;
				// comprobando la unicidad de este contador junto con el prefijo y sufijo guardado en la hash contador
				while (gc.comprobarUnicidadContadorProdCertif(numContador, contadorTablaHash, idTipoProducto, idProducto, idProductoInstitucion)) {
					numContador++;
				}
				gc.validarLogitudContador(numContador, contadorTablaHash);

				// construyendo contador final
				Integer longitud = new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));
				int longitudContador = longitud.intValue();
				Integer contadorSugerido = new Integer(numContador);
				String contadorFinalSugerido = UtilidadesString.formatea(contadorSugerido, longitudContador, true);

				// guardando contador en BD
				gc.setContador(contadorTablaHash, contadorFinalSugerido);

				// devolviendo el contador para la continuacion del proceso
				htNew.put(CerSolicitudCertificadosBean.C_PREFIJO_CER, (String) contadorTablaHash.get("PREFIJO"));
				htNew.put(CerSolicitudCertificadosBean.C_SUFIJO_CER, (String) contadorTablaHash.get("SUFIJO"));
				htNew.put(CerSolicitudCertificadosBean.C_CONTADOR_CER, contadorFinalSugerido);
			}
			//aalg: se añade la carga en base de datos dentro de la sincronización para evitar la pérdida de número de certificado
			//inc_10359_siga
			if (!htNew.get(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)){
			      htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBADO);    
			      htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
		        }

	        // RGG 15/10/2007 CAMBIO PARA ACTUALIZAR LA FECHA DE EMISION SOLAMENTE SI ESTA A NULA 
	        if (beanSolicitud.getFechaEmisionCertificado() == null || beanSolicitud.getFechaEmisionCertificado().equals("")) {
	            htNew.put(CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,"sysdate");
	        }
	        
		  	htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION,"sysdate");
		    
	        if (!admSolicitud.updateDirect(htNew, claves, campos ))
		    {
	        	tx.rollback();
		        throw new ClsExceptions("Error al aprobar la solicitud");
		    }
	        tx.commit(); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}