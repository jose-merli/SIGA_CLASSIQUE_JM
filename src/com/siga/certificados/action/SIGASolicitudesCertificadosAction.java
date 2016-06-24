package com.siga.certificados.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.GEN_PROPERTIES;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificados;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificadostexto;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificadostextoExample;
import org.redabogacia.sigaservices.app.autogen.model.CerSolicitudesAccion;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosAccionService;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosService;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosTextoService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.services.helper.SigaServiceHelperService;
import org.redabogacia.sigaservices.app.services.mutualidad.MutualidadService;
import org.redabogacia.sigaservices.app.util.PropertyReader;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.LogFileWriter;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.PaginadorBind;
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
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacSerieFacturacionAdm;
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
import com.siga.certificados.form.SIGASolicitudesCertificadosForm;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.action.AltaAbonosAction;
import com.siga.general.CenVisibilidad;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;



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

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
					mapDestino = abrir(mapping, miForm, request, response);
					
	            } else if (accion.equalsIgnoreCase("volver")) {
	            	request.setAttribute("volver", "volver");
	                mapDestino = abrir(mapping, miForm, request, response);	
	            } else if (accion.equalsIgnoreCase("editarDescarga")) {
	            	request.setAttribute("descarga", "1");
	                mapDestino = editar(mapping, miForm, request, response);		                
	            } else if (accion.equalsIgnoreCase("buscarInicio") || accion.equalsIgnoreCase("buscarInicioSeleccionarTodos")) {
					SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) miForm;
					form.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					if(accion.equalsIgnoreCase("buscarInicio")){
						form.setSeleccionarTodos("");
					}
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscar(mapping, form, request, response);
					
	            } else if (accion.equalsIgnoreCase("aprobarYGenerarVariosCertificados")) {
	                mapDestino = aprobarYGenerarVariosCertificados(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("generarPDF")) {
	                mapDestino = generarPDF(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("enviar") || accion.equalsIgnoreCase("ver")) {
	                mapDestino = editar(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("denegar")) {
	                mapDestino = denegar(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("anular")) {
	                mapDestino = anular(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("finalizar")) {
	                mapDestino = finalizar(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("facturarCertificadosSeleccionados")) {
	                mapDestino = facturarCertificadosSeleccionados(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("facturacionRapida")) {
	                mapDestino = facturacionRapida(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("finalizarCertificados")) {
	                mapDestino = finalizarCertificados(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("descargarLogError")) {
	                mapDestino = descargarLog(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("descargar")) {
	                mapDestino = descargar(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("asignarPlantillaCertificado")) {
	                mapDestino = asignarPlantillaCertificado(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("copiarSanciones")) {
	                mapDestino = copiarSanciones(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("copiarHistorico")) {
	                mapDestino = copiarHistorico(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("historicoObservaciones")) {
	                mapDestino = historicoObservaciones(mapping, miForm, request, response);
	                
	            } else if (accion.equalsIgnoreCase("comprobarNumPlantillas")) {
	                mapDestino = comprobarNumPlantillas(mapping, miForm, request, response);
	                
				} else if (accion.equalsIgnoreCase("getAjaxSeleccionSerieFacturacion")) {
					getAjaxSeleccionSerieFacturacion(request, response);	     
					return null;
				} else if (accion.equalsIgnoreCase("getAjaxFacturaAsociada")) {
					getAjaxFacturaAsociada(request, response);	     
					return null;					
	                
	            } else if (accion.equalsIgnoreCase("getAjaxSeleccionSerieFacturacionFacturacionMasiva")) {
	            	getAjaxSeleccionSerieFacturacionFacturacionMasiva(request, response);	     
					return null;
	                
	            } else {
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


	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
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
			request.setAttribute("idPeticion", idSolicitudCertificado);
			request.setAttribute("concepto", concepto);

			if ((request.getParameter("buscar") != null && request.getParameter("buscar").equalsIgnoreCase("true")) || (request.getAttribute("volver") != null && ((String)request.getAttribute("volver")).equalsIgnoreCase("volver"))) {
				if (request.getSession().getAttribute("DATABACKUP") != null && (request.getSession().getAttribute("DATABACKUP") instanceof SIGASolicitudesCertificadosForm)) {
					form = (SIGASolicitudesCertificadosForm) request.getSession().getAttribute("DATABACKUP");
				}
			} else {
				form.resetCamposBusqueda();
			}
			
			request.setAttribute("SolicitudesCertificadosForm", form);
			request.getSession().removeAttribute("DATABACKUP");
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
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
	
   protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
    	try {			
    		final String[] clavesBusqueda={CerSolicitudCertificadosBean.C_FECHASOLICITUD,CerSolicitudCertificadosBean.C_IDSOLICITUD,"TIPOCERTIFICADO",CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO,
    				CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO,CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION,CerSolicitudCertificadosBean.C_IDINSTITUCION,
    				CerSolicitudCertificadosBean.C_IDPERSONA_DES,CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN,CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
   
	        SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
	        UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
	        
	        String idInstitucion = userBean.getLocation();
	        
	        GenParametrosAdm paramAdm = new GenParametrosAdm (userBean);
	        CerSolicitudCertificadosAdm admSolicitudCertificados = new CerSolicitudCertificadosAdm(userBean);
	        
			//Haria falta meter los parametros en con ClsConstants
	        String permitir_factura_certificado = paramAdm.getValor (idInstitucion, "CER", ClsConstants.PERMITIR_FACTURA_CERTIFICADO, "");
	        
	        request.setAttribute("isPermitirFacturaCertificado", new Boolean(permitir_factura_certificado.equals(ClsConstants.DB_TRUE)));	        
	        
	        
	      //Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = form.getSeleccionarTodos()!=null 
				&& !form.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = form.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						form.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			
			
			HashMap databackup = form.getDatosPaginador();
			if (databackup != null && databackup.get("paginador") != null && !isSeleccionarTodos) {
				PaginadorBind paginador = (PaginadorBind) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String) request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				datos = admSolicitudCertificados.obtenerDatosGestionCertificados(datos);
				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {

				databackup = new HashMap();

				// obtengo datos de la consulta
				PaginadorBind resultado = null;
				resultado = admSolicitudCertificados.buscarSolicitudes(form, idInstitucion);
				Vector datos = null;

				databackup.put("paginador", resultado);

				if (resultado != null && resultado.getNumeroTotalRegistros() > 0) {

					if (isSeleccionarTodos) {
						// Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection) admSolicitudCertificados.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(clavesBusqueda, clavesRegSeleccinados);
						form.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(form.getSeleccionarTodos()));
						form.setSeleccionarTodos("");

					} else {
						form.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}

					datos = admSolicitudCertificados.obtenerDatosGestionCertificados(datos);
					databackup.put("datos", datos);

				} else {
					resultado = null;
					form.setRegistrosSeleccionados(new ArrayList());
				}
				form.setDatosPaginador(databackup);

			}

			// Comprobamos si estamos en CGAE y lo pasamos a la jsp
			String esCGAE = CenVisibilidad.getNivelInstitucion(idInstitucion).equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_INTITUCION_CGAE)) ? "true" : "false";
			request.setAttribute("esCGAE", esCGAE);

			request.getSession().setAttribute("DATABACKUP", form);

			// Para volver correctamente desde envios:
			request.getSession().setAttribute("EnvEdicionEnvio", "GS");

		} catch (SIGAException e1) { // Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina", request);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
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
		    Hashtable<String,Object> hash = new Hashtable<String,Object>();
		    
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


	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MasterForm miForm = (MasterForm) formulario;
			String accion = miForm.getModo();
			request.setAttribute("modo", accion);

			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
			String idInstitucion = "", idSolicitud = "", idEstadoSolicitud = "", tipoCertificado = "";
			Vector vOcultos = form.getDatosTablaOcultos(0);

			if (vOcultos != null) {
				/** Icono listado de solicitudes **/
				idInstitucion = ((String) vOcultos.elementAt(0)).trim();
				idSolicitud = ((String) vOcultos.elementAt(1)).trim();
				idEstadoSolicitud = ((String) vOcultos.elementAt(9)).trim();
				tipoCertificado = ((String) vOcultos.elementAt(11)).trim();

			} else {
				/** Nueva ventana detalle certificado **/
				idInstitucion = form.getIdInstitucion();
				idSolicitud = form.getIdSolicitud();
				tipoCertificado = form.getTipoCertificado();
			}

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request));
			String idInstitucionOrigen = "" + beanSolicitud.getIdInstitucionOrigen();
			String idInstitucionDestino = "" + beanSolicitud.getIdInstitucionDestino();
			String idInstitucionColegiacion = "" + beanSolicitud.getIdInstitucionColegiacion();
			CenInstitucionBean beanInstitucionOrigen = null;
			Hashtable<String, Object> htInstitucion = new Hashtable<String, Object>();
			if (!idInstitucionOrigen.equalsIgnoreCase("null")) {
				htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionOrigen);
				vDatos = admInstitucion.selectByPK(htInstitucion);
				if (vDatos != null && vDatos.size() == 1) {
					beanInstitucionOrigen = (CenInstitucionBean) vDatos.elementAt(0);
				}
			}

			CenInstitucionBean beanInstitucionColegiacion = null;
			if (!idInstitucionColegiacion.equalsIgnoreCase("null")) {
				htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionColegiacion);
				vDatos = admInstitucion.selectByPK(htInstitucion);
				if (vDatos != null && vDatos.size() == 1) {
					beanInstitucionColegiacion = (CenInstitucionBean) vDatos.elementAt(0);
				}
			}

			CenInstitucionBean beanInstitucionDestino = null;
			if (!idInstitucionDestino.equalsIgnoreCase("null")) {
				htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionDestino);
				vDatos = admInstitucion.selectByPK(htInstitucion);

				if (vDatos != null && vDatos.size() == 1) {
					beanInstitucionDestino = (CenInstitucionBean) vDatos.elementAt(0);
				}
			}

			// obtengo el texto de sanciones. se modifica. Lo que se obtiene es el texto del certificado
			// ESTA TABLA ESTA MAL YA QUE NO TIENE SENTIDO EL CAMPO IDTEXTO COMO PARTE DE LA PK CUANDO NO EXSITE TAL MULTIPLICAIDAD.
			CerSolicitudCertificadosTextoService certificadosTextoService = (CerSolicitudCertificadosTextoService) getBusinessManager().getService(CerSolicitudCertificadosTextoService.class);
			CerSolicitudcertificadostextoExample cerSolicitudcertificadostextoExample = new CerSolicitudcertificadostextoExample();
			org.redabogacia.sigaservices.app.autogen.model.CerSolicitudcertificadostextoExample.Criteria criteria = cerSolicitudcertificadostextoExample.createCriteria();

			criteria.andIdinstitucionEqualTo(beanSolicitud.getIdInstitucion().shortValue());
			criteria.andIdsolicitudEqualTo(beanSolicitud.getIdSolicitud());

			List<CerSolicitudcertificadostexto> cerSolicitudcertificadostextoList = certificadosTextoService.getList(cerSolicitudcertificadostextoExample);
			String textoCertificado = "";
			String incluirDeudas = "off";
			String incluirSanciones = "off";
			if (cerSolicitudcertificadostextoList.size() > 0) {
				CerSolicitudcertificadostexto cerSolicitudcertificadostexto = cerSolicitudcertificadostextoList.get(0);
				if (cerSolicitudcertificadostexto != null) {
					textoCertificado = cerSolicitudcertificadostexto.getTexto();
					if (cerSolicitudcertificadostexto.getIncluirdeudas() != null && cerSolicitudcertificadostexto.getIncluirdeudas().equalsIgnoreCase("S"))
						incluirDeudas = "on";
					if (cerSolicitudcertificadostexto.getIncluirsanciones() != null && cerSolicitudcertificadostexto.getIncluirsanciones().equalsIgnoreCase("S"))
						incluirSanciones = "on";

				}

				form.setIncluirDeudas(incluirDeudas);
				form.setIncluirSanciones(incluirSanciones);
			}
			
			request.setAttribute("sanciones", textoCertificado);
			request.setAttribute("solicitud", beanSolicitud);
			
			/** ESTADOS SOLICITUD CERTIFICADO **/
			if(idEstadoSolicitud== null || idEstadoSolicitud.equals("")){
				//No se rellena si venimos de vuelta
				idEstadoSolicitud = beanSolicitud.getIdEstadoSolicitudCertificado().toString();
			}
			
			CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(this.getUserBean(request));
			String strEstadoActual = estAdm.getNombreEstadoSolicitudCert(idEstadoSolicitud);
			String strSiguienteEstado = "-";
			if(idEstadoSolicitud!=null){
				switch (Integer.parseInt(idEstadoSolicitud)) {
					case 1://Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)
						strSiguienteEstado = estAdm.getNombreEstadoSolicitudCert(CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBADO);
					break;
					
					case 2://Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBADO)
						Thread.sleep(2000);
						if(beanSolicitud.getFechaCobro()!=null && !beanSolicitud.getFechaCobro().trim().equals("")){
							strSiguienteEstado = estAdm.getNombreEstadoSolicitudCert(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);
						}else{
							strSiguienteEstado = estAdm.getNombreEstadoSolicitudCert(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
						}
					break;
					
					case 4://Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)
						Thread.sleep(2000);
						strSiguienteEstado = "-";
					break;					
					
					case 10://Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR)
							strSiguienteEstado = estAdm.getNombreEstadoSolicitudCert(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
						break;					
						
					default:
						strSiguienteEstado = "-";
					break;
				}
			} 
			
			request.setAttribute("idEstadoSolicitud", idEstadoSolicitud);
			request.setAttribute("strEstadoSolicitud", strEstadoActual);
			request.setAttribute("strSiguienteEstado", strSiguienteEstado);
			

			if (beanSolicitud.getIdEstadoSolicitudCertificado().toString().equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) || beanSolicitud.getIdEstadoSolicitudCertificado().toString().equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO)) {
				request.setAttribute("modificarSolicitud", "0");
			} else {
				request.setAttribute("modificarSolicitud", "1");
			}
			request.setAttribute("institucionOrigen", beanInstitucionOrigen);
			request.setAttribute("institucionDestino", beanInstitucionDestino);
			request.setAttribute("institucionColegiacion", beanInstitucionColegiacion);
			request.setAttribute("tipoCertificado", tipoCertificado);

			// TRATAMIENTO DEL CONTADOR
			String pre = (beanSolicitud.getPrefijoCer() != null) ? beanSolicitud.getPrefijoCer() : "";
			String suf = (beanSolicitud.getSufijoCer() != null) ? beanSolicitud.getSufijoCer() : "";
			String numContador = beanSolicitud.getContadorCer();
			String idtipop = beanSolicitud.getPpn_IdTipoProducto().toString();
			String idp = beanSolicitud.getPpn_IdProducto().toString();
			String idpi = beanSolicitud.getPpn_IdProductoInstitucion().toString();
			boolean pintarCheckMutualidad = false;

			// jbd // Tratamiento para los certificados que pasan informacion a la mutualidad
			if (admSolicitud.isCertNuevaIncorporacion(beanSolicitud.getIdInstitucion().toString(), beanSolicitud.getPpn_IdProducto().toString(), beanSolicitud.getPpn_IdTipoProducto().toString(), beanSolicitud.getPpn_IdProductoInstitucion().toString())) {
				pintarCheckMutualidad = true;
			}

			request.setAttribute("pintarCheckMutualidad", pintarCheckMutualidad);

			PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.getUserBean(request));
			Vector vector = admProd.select("WHERE " + PysProductosInstitucionBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " + PysProductosInstitucionBean.C_IDTIPOPRODUCTO + "=" + idtipop + " AND " + PysProductosInstitucionBean.C_IDPRODUCTO + "=" + idp + " AND " + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION + "=" + idpi);
			PysProductosInstitucionBean beanProd = null;
			if (vector != null && vector.size() > 0) {
				beanProd = (PysProductosInstitucionBean) vector.get(0);
			}

			// Si el pcerificado es facturable o no
			boolean facturable = false;
			if(beanProd != null && beanProd.getnoFacturable().equals("0"))
				facturable = true;
			request.setAttribute("facturable", facturable);
			
			//Contador
			if (numContador != null && !numContador.equals("")) {
				// obtengo el objeto contador
				GestorContadores gc = new GestorContadores(this.getUserBean(request));

				// obtenemos el contador de la FK del producto
				String idContador = "";
				if (beanProd != null)
					idContador = beanProd.getIdContador();

				Hashtable<String, Object> contadorTablaHash = gc.getContador(new Integer(idInstitucion), idContador);

				// formateo el contador
				Integer longitud = new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));
				int longitudContador = longitud.intValue();
				Integer contadorSugerido = new Integer(numContador);
				String contadorFinalSugerido = UtilidadesString.formatea(contadorSugerido, longitudContador, true);
				String contador = pre.trim() + contadorFinalSugerido + suf.trim();

				// lo guardamos formateado
				request.setAttribute("codigo", contador);
			} else {
				request.setAttribute("codigo", "");
			}
    		
			String nombreSolicitante = "", nidSolicitante = "", ncolSolicitante = "";
			/** DATOS SOLICITANTE **/
			if (beanSolicitud.getIdPersona_Des() != null) {
				String idPersona = beanSolicitud.getIdPersona_Des().toString();
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				CenColegiadoAdm colAdm=new CenColegiadoAdm(this.getUserBean(request));
				nidSolicitante = personaAdm.obtenerNIF(idPersona);
				nombreSolicitante = personaAdm.obtenerNombreApellidos(idPersona);
				ncolSolicitante=colAdm.getNumColegiado(beanSolicitud.getIdInstitucion(),idPersona);
			}
			request.setAttribute("nombreSolicitante", nombreSolicitante);
			request.setAttribute("nidSolicitante", nidSolicitante);
			request.setAttribute("ncolSolicitante", ncolSolicitante);

			AdmUsuariosAdm adm = new AdmUsuariosAdm(this.getUserBean(request));
			Hashtable<String, Object> h = new Hashtable<String, Object>();
			UtilidadesHash.set(h, AdmUsuariosBean.C_IDUSUARIO, beanSolicitud.getUsuMod());
			UtilidadesHash.set(h, AdmUsuariosBean.C_IDINSTITUCION, beanSolicitud.getIdInstitucion());
			Vector v = adm.select(h);
			if (v != null && v.size() == 1) {
				request.setAttribute("nombreUltimoUsuMod", ((AdmUsuariosBean) v.get(0)).getDescripcion());
			}

			UtilidadesHash.set(h, AdmUsuariosBean.C_IDUSUARIO, beanSolicitud.getUsuCreacion());
			v = adm.select(h);
			if (v != null && v.size() == 1) {
				request.setAttribute("nombreUsuCreacion", ((AdmUsuariosBean) v.get(0)).getDescripcion());
			}
			
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}
		return "mostrar";
	}

    /**
     * Obtiene el contador y aprueba. Y ademas genera el fichero PDF
     * @param idInstitucion
     * @param idSolicitud
     * @param usr
     * @param beanProd
     * @param idPlantilla
     * @param usarIdInstitucion
     * @param listaDeObjetos
     * @throws ClsExceptions
     * @throws SIGAException
     */
	public static void almacenarCertificado(String idInstitucion, String idSolicitud, UsrBean usr ,PysProductosInstitucionBean beanProd,
			String idPlantilla,boolean usarIdInstitucion,HashMap<String, Object> listaDeObjetos)throws ClsExceptions, SIGAException {
    	// OBTENEMOS LA SOLICITUD
    	Hashtable<String,Object> htSolicitud = new Hashtable<String,Object>();
	    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
	    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
		    CerSolicitudCertificadosAdm admCert = new CerSolicitudCertificadosAdm(usr);
		   
	    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
	    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		String[] claves = {CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD};
        
        //pdm es necesario actualizar estas fechas cuando se aprueba la solicitud del certificado
	        String[] campos = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO,CerSolicitudCertificadosBean.C_PREFIJO_CER,CerSolicitudCertificadosBean.C_SUFIJO_CER, 
	        		CerSolicitudCertificadosBean.C_CONTADOR_CER,CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,CerSolicitudCertificadosBean.C_FECHAMODIFICACION,CerSolicitudCertificadosBean.C_FECHAESTADO};
		

		    // Si viene con el estado aprobando = 7 se debe de aprobar sino no.
		    Hashtable<String,Object> htNew = beanSolicitud.getOriginalHash();	
		    if (String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBANDO))
		    	obtenerContadorYAprobar (admSolicitud,beanSolicitud,beanProd,htNew,claves,campos,usr);
        
		    //PROCESO DE GENERACIÓN, SIEMPRE GENERAMOS
		    ProcesoCompletoDeGeneracion(beanSolicitud,listaDeObjetos,idPlantilla,usarIdInstitucion,usr,admCert,htNew,admSolicitud,claves);
	    
	    }
	       
	/**
	 * aprobarYGenerarVariosCertificados. Este modo lo que hace ahora es coger todos los ids y generarlos. Para ellos coge su plantilla si es que está configurada y
	 * coge primero la plantilla fisica por defecto. SI no la tiene coge la primera que encuentra. Si no continua con otro certificado.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String aprobarYGenerarVariosCertificados(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		LogFileWriter log = null;
		try{
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    
		    CerPlantillasAdm admPlantillas = new CerPlantillasAdm(this.getUserBean(request));
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
		    List <CerSolicitudcertificados> listaCerSolicitudcertificados = null;
		    
		    int contador=0;
		    int contadorErrores=0;
		    int contadorColegiadosNoOrigen=0;
		    
		    StringTokenizer st = null;
		    int contadorReg=1;
		    String tok=form.getIdsParaGenerarFicherosPDF();
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }
		    //Si es menos de 20 elementos de hace de manera online, sino se almacenarán los registros y se ejecutarán a través de un demonio
		    if(contadorReg <= 20){
		    
				    String nombreSolicitud="";
				    CerSolicitudcertificados cerSolicitudcertificados = null;
				    
				    while (st.hasMoreElements())
				    {
				    	String to = (String)st.nextToken();
				    	if (to.equals("undefined")) {
				    		continue;
				    	}
				        StringTokenizer st2 = new StringTokenizer(to, "||");
			
				        String fechaSolicitud = st2.nextToken();
				        String idSolicitud = st2.nextToken();
				        
				        st2.nextToken();
				        
				        String idTipoProducto = st2.nextToken();
				        String idProducto = st2.nextToken();
				        String idProductoInstitucion = st2.nextToken();
				        String idInstitucion = st2.nextToken();
				        String idPersona = st2.nextToken();
			
				        nombreSolicitud="[Institucion:"+idInstitucion+"][Solicitud:"+idSolicitud + "][fecha:"+fechaSolicitud+"]";
				        
				        // Obtengo la plantilla por defecto para cada certificado
				        String idPlantilla = admPlantillas.obtenerPlantillaDefecto(idInstitucion,idTipoProducto,idProducto, idProductoInstitucion);	
		
				    	HashMap<String, Object> listadoColegiado = obtenerPersonaCertificado(userBean,idInstitucion,idTipoProducto,idProducto,idProductoInstitucion,idSolicitud);
						
				    	/////////////////////////////////////////////////
					    Hashtable<String,Object> htSolicitud = new Hashtable<String,Object>();
					    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
					    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
					    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
					    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
					    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
				    	//Creamos el log de error
				        log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(beanSolicitud),idSolicitud+"-LogError");
						log.clear();
						// solo si no esta anulado y no esta denegado
						if ( beanSolicitud.getIdEstadoSolicitudCertificado()!= Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) 
								&& beanSolicitud.getIdEstadoSolicitudCertificado()!=Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO)) {  
						        if ((Boolean)listadoColegiado.get("colegiadoEnOrigen")) {
						        	// Para los certificados normales, este valor siempre es verdadero
						        	// Para las comunicaciones/diligencias, entra por aqui cuando ES colegiadoEnOrigen
						        
						        	// Obtenemos cada vez los paraemtros por si cambia la institucion (Que no creo que lo haga, pero bueno...) Se puede sacar del bucle si eliminamos la institucion del listado de solicitudes recibidos en el formulario
						        	GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
						        	HashMap<String, Object> listaDeObjetos = obtenerPathBD(admParametros,idInstitucion,idSolicitud);
							        try {
							        	  	  listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
						
								        	  cerSolicitudcertificados = new CerSolicitudcertificados();
								        	  cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
										      cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
										      cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
							        
										      listaCerSolicitudcertificados.add(cerSolicitudcertificados);
							        
									        //Actualizamos el estado
									        cerSolicitudCertificadosService.updateMaxivoAyG(listaCerSolicitudcertificados);
								        	
									     
									        
							        	//////  UNIFICACION PARA LOS 3 METODOS DE GENERAR PDF //////////
								        	almacenarCertificado (idInstitucion, idSolicitud, this.getUserBean(request), (PysProductosInstitucionBean)listadoColegiado.get("beanProd"),
								        			 idPlantilla,(Boolean)listadoColegiado.get("usarIdInstitucion"),listaDeObjetos);
									    
								        contador++;
								        
				
								        } catch (ClsExceptions e) {
								        	
								        	ClsLogging.writeFileLog("----- ERROR APROBAR Y GENERAR PDF -----"+nombreSolicitud,4);
								    		log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), e.getMsg()});
									    	  /** Escribiendo fichero de log **/
											if (log != null)
												log.flush();
											contadorErrores++;
											cerSolicitudCertificadosService.updateMaxivoCasoDeError(listaCerSolicitudcertificados);
									       
							            }catch (SIGAException e) {
							            
							            	ClsLogging.writeFileLogError("ERROR EN APROBAR Y GENERAR PDF MASIVO MENOS DE 20. SOLICITUD:" +nombreSolicitud+ " Error:" + ((SIGAException) e).getLiteral(userBean.getLanguage()),e, 3);
								    		log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), ((SIGAException) e).getLiteral(userBean.getLanguage())});
									    	  /** Escribiendo fichero de log **/
											if (log != null)
												log.flush();
											contadorErrores++;
											cerSolicitudCertificadosService.updateMaxivoCasoDeError(listaCerSolicitudcertificados);
									       
							            }
						
								        ((File) listaDeObjetos.get("fIn")).delete(); //Se hace otro delete porque el deleteOnExit no borra el temporal.
						        } else { 
						        	// Para las comunicaciones/diligencias, entra por aqui cuando NO ES colegiadoEnOrigen
							        ClsLogging.writeFileLog("ERROR EN APROBAR Y GENERAR PDF MASIVO MENOS DE 20.:"+nombreSolicitud+" Error: El cliente no es colegiadoEnOrigen, idpersona=" + idPersona,3);
						    		log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), " Error: El cliente no es colegiadoEnOrigen. Si el problema persiste, contacte con el Administrador"});
							    	  /** Escribiendo fichero de log **/
									if (log != null)
										log.flush();
						        	
						        	contadorErrores++;
						        	contadorColegiadosNoOrigen++;
						        	if (contadorReg==1) {
					
						        		String mensaje = "messages.error.solicitud.clienteEsColegiado";
						        		String[] datos = {""+contador};
						        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
						        		
						        		request.setAttribute("mensaje",mensaje);	
				
						        	    return "exitoConString";
						        	}
						           }
						}else{
							CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(this.getUserBean(request));
				        	ClsLogging.writeFileLog("----- ERROR APROBAR Y GENERAR PDF -----",4);
				    		log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": no es " +
				    				"posible procesarlo. Tendrá que crear otro nuevo certificado si es necesario."});
					    	  /** Escribiendo fichero de log **/
							if (log != null)
								log.flush();
				        	
				        	contadorErrores++;
						}
				    } // WHILE 
				    /*Muestra el resultado por pantalla de certificados con existo y fallidos*/
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
				    	tipoAlert="warning";
				    	// Hay errores en la generacion de los certificados
				    	String[] datos = {""+contador,""+contadorErrores};
				    	mensaje = UtilidadesString.getMensaje("certificados.solicitudes.mensaje.generacionCertificadosPDF.KO", datos, userBean.getLanguage());
				    }
				    request.setAttribute("estiloMensaje",tipoAlert);	
					request.setAttribute("mensaje",mensaje);
			
		}else{
			//Cuando es más de 20 se ejecuta el proceso automático cada min
			CerSolicitudCertificadosAccionService cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance().getService(CerSolicitudCertificadosAccionService.class);
			CerSolicitudesAccion cerSolicitudesAccion = new CerSolicitudesAccion();
			CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados();
			List <CerSolicitudesAccion> listaCerSolicitudesAccion = new ArrayList<CerSolicitudesAccion>();
			listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
			   while (st.hasMoreElements())
			    {
				   
			    	String to = (String)st.nextToken();
			    	if (to.equals("undefined")) {
			    		continue;
			    	}
		    	  StringTokenizer st2 = new StringTokenizer(to, "||");
		    		
			        st2.nextToken();
			        String idSolicitud = st2.nextToken();

					 st2.nextToken();
					 
					st2.nextToken();
			        st2.nextToken();
			        st2.nextToken();
			        String idInstitucion = st2.nextToken();
			 
			       
			        //Rellenamos los objetos
			        cerSolicitudesAccion.setIdinstitucion(Short.valueOf(idInstitucion));
			        cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
			        cerSolicitudesAccion.setIdsolicitud(Long.valueOf(idSolicitud));
			        cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
			        cerSolicitudesAccion.setAccion(Long.valueOf(CerSolicitudCertificadosAdm.A_ABROBAR_GENERAR));
			        cerSolicitudesAccion.setUsucreacion(new Integer(userBean.getUserName()));
			        cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
			        cerSolicitudesAccion.setUsumodificacion(new Integer(userBean.getUserName()));
			        cerSolicitudesAccion.setIdseriefacturacionseleccionada("-1");
			        
			        //Insertamos en la tabla de acciones
			        listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			        listaCerSolicitudesAccion.add(cerSolicitudesAccion);
			        
					cerSolicitudesAccion = new CerSolicitudesAccion();
					cerSolicitudcertificados = new CerSolicitudcertificados();
					
			    }	
			 //Insertamos en la tabla acciones
			    cerSolicitudCertificadosAccionService.insertMaxivo(listaCerSolicitudesAccion);
			 //Cambiamos el estado en la tabla solicitudCertificado
		        cerSolicitudCertificadosService.updateMaxivoAyG(listaCerSolicitudcertificados);
		        
		        String tipoAlert ="success";   
			    request.setAttribute("estiloMensaje",tipoAlert);	
				request.setAttribute("mensaje","Las solicitudes han sido marcadas para ser procesadas. Nº total de solicitudes: "+contadorReg);
		}	
		    		
				
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		 	String tipoAlert ="";   
		    tipoAlert="error";
			request.setAttribute("estiloMensaje",tipoAlert);	
			request.setAttribute("mensaje","Se ha producido un error en la aprobación y generación masiva.");
		}
		
	    return "exitoConString";
	}   
	protected String generarPDF(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		//UserTransaction tx=null;
		try	{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			 CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
			 List <CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
			// Comienzo control de transacciones
			//tx = usr.getTransactionPesada();
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
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
		    CerSolicitudcertificados cerSolicitudcertificados = null;
		    while (st.hasMoreElements())
		    {
		        StringTokenizer st2 = new StringTokenizer(st.nextToken(), "||");
	
				st2.nextToken();
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
	
		        if (colegiadoEnOrigen) {
		        
		        	// Obtenemos cada vez los paraemtros por si cambia la institucion (Que no creo que lo haga, pero bueno...)
			        GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
		        	HashMap<String, Object> listaDeObjetos = obtenerPathBD(admParametros,idInstitucion,idSolicitud);
		
		
			        try {
			        
			        	  cerSolicitudcertificados = new CerSolicitudcertificados();
			        	  cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
					      cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
					      cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
			        
					      listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			        
				        //Cambiamos el estado
				        cerSolicitudCertificadosService.updateMaxivoAyG(listaCerSolicitudcertificados);
		
			        	//////  UNIFICACION PARA LOS 3 METODOS DE GENERAR PDF //////////
			        	almacenarCertificado (idInstitucion, idSolicitud, this.getUserBean(request), beanProd,
			        			 idPlantilla, usarIdInstitucion,listaDeObjetos);
				        
				        contador++;
			        


			        } catch (SIGAException e) {
				        ClsLogging.writeFileLogError("Error GENERAL al aprobar y generar el certificado PDF: " + e.getLiteral(userBean.getLanguage()),e, 3);
				        contadorErrores++;
				        cerSolicitudCertificadosService.updateMaxivoCasoDeError(listaCerSolicitudcertificados);
		            }
		
			        ((File) listaDeObjetos.get("fIn")).delete();
			        
			        
	
		        } else {
		        	
			        contadorErrores++;
			        contadorColegiadosNoOrigen++;
		        	// NO ES colegiadoEnOrigen
			        ClsLogging.writeFileLog("Error al genera el certificado PDF: El cliente no es colegiadoEnOrigen, idpersona=" + idPersona,3);
		        	if (contadorReg==1) {
	
		        		String mensaje = "messages.error.solicitud.clienteEsColegiado";
		        		String[] datos = {""+contador};
		        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
		        		
		        		request.setAttribute("mensaje",mensaje);	
		        	    
		        		return "exitoConString";
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
		    	request.setAttribute("estilo",tipoAlert);	
		        request.setAttribute("mensaje",mensaje);
		    	return "exitoConStringDescarga";
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

	
	protected String generarPDFUnaPlantilla(ActionMapping mapping, String IdsParaGenerarFicheros, SIGASolicitudesCertificadosForm form, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		 CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
		 List <CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();

		try {

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));

			int contador = 0;

			StringTokenizer st = null;
			int contadorReg = 1;
			String tok = IdsParaGenerarFicheros;
			try {
				st = new StringTokenizer(tok, ";");
				contadorReg = st.countTokens();
			} catch (java.util.NoSuchElementException nee) {
				// solamente existe un token
			}

		    int contadorErrores=0;
		    int contadorColegiadosNoOrigen=0;
		    CerSolicitudcertificados cerSolicitudcertificados = null;
		    while (st.hasMoreElements())
		    {
		        StringTokenizer st2 = new StringTokenizer(st.nextToken(), "||");
	
				st2.nextToken();
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
		        
		        if (colegiadoEnOrigen) {
		        
		        	// Obtenemos cada vez los paraemtros por si cambia la institucion (Que no creo que lo haga, pero bueno...)
			        GenParametrosAdm admParametros = new GenParametrosAdm(this.getUserBean(request));
		        	HashMap<String, Object> listaDeObjetos = obtenerPathBD(admParametros,idInstitucion,idSolicitud);
		
		
			        try {
			        
			        	  cerSolicitudcertificados = new CerSolicitudcertificados();
			        	  cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
					      cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
					      cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
			        
					      listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			        
				        //Cambiamos el estado
				        cerSolicitudCertificadosService.updateMaxivoAyG(listaCerSolicitudcertificados);
		
			        	//////  UNIFICACION PARA LOS 3 METODOS DE GENERAR PDF //////////
			        	almacenarCertificado (idInstitucion, idSolicitud, this.getUserBean(request), beanProd,
			        			 idPlantilla, usarIdInstitucion,listaDeObjetos);
			        
				        contador++;
			        

			        } catch (SIGAException e) {
				        ClsLogging.writeFileLogError("Error GENERAL al aprobar y generar el certificado PDF: " + e.getLiteral(userBean.getLanguage()),e, 3);
				        contadorErrores++;
				        cerSolicitudCertificadosService.updateMaxivoCasoDeError(listaCerSolicitudcertificados);
		            }
		
			        ((File) listaDeObjetos.get("fIn")).delete();
			        
	
		        } else {
		        	
			        contadorErrores++;
		        	// NO ES colegiadoEnOrigen
			        contadorColegiadosNoOrigen++;
			        ClsLogging.writeFileLog("Error al genera el certificado PDF: El cliente no es colegiadoEnOrigen, idpersona=" + idPersona,3);
		        	if (contadorReg==1) {
	
		        		String mensaje = "messages.error.solicitud.clienteEsColegiado";
		        		String[] datos = {""+contador};
		        		mensaje = UtilidadesString.getMensaje(mensaje, datos, userBean.getLanguage());
		        		
		        		request.setAttribute("mensaje",mensaje);	
		        	    
		        		return descargar(mapping,form,request,response);
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
		    	request.setAttribute("estilo",tipoAlert);	
		        request.setAttribute("mensaje",mensaje);
		    	return "exitoConStringDescarga";		    	
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
			
	    return descargar(mapping,form,request,response);
	}

	protected String enviar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{

		try{
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
		    String idSolicitud = ((String)vOcultos.elementAt(1)).trim();
		    
		    Hashtable<String,Object> htSolicitud = new Hashtable<String,Object>();
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
		    
		    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    
		    CerSolicitudCertificadosBean solBean = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		    String sRutaBd = admSolicitud.getRutaCertificadoFichero(solBean,admSolicitud.getRutaCertificadoDirectorioBD(solBean.getIdInstitucion()));
		    
		    request.setAttribute("ruta", sRutaBd);	    
	    
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
		}
	    
	    return "envioModal";
	}

	
	protected String anular(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
			PysCompraAdm pysCompra = new PysCompraAdm(this.getUserBean((request)));
			String idInstitucion = "", idSolicitud = "", idPeticion = "", idTipoProducto = "", idProducto = "", idProductoInstitucion = "";

			/** Nueva ventana detalle certificado **/
			idInstitucion = form.getIdInstitucion();
			idSolicitud = form.getIdSolicitud();
			idPeticion = (String) request.getParameter("idPeticion");
			idProducto = form.getIdProducto();
			idTipoProducto = form.getIdTipoProducto();
			idProductoInstitucion = form.getIdProductoInstitucion();
		    
			tx.begin();
			if (idPeticion != null && !idPeticion.equals("")) {
				/** Si existe idPeticion miramos si tambien existe un idFacturacion, si no existiera, borramos el registro de la tabla PYS_COMPRA **/
				Hashtable<String, Object> htCompra = new Hashtable<String, Object>();
				htCompra.put(PysCompraBean.C_IDINSTITUCION, idInstitucion);
				htCompra.put(PysCompraBean.C_IDPETICION, idPeticion);
				htCompra.put(PysCompraBean.C_IDPRODUCTO, idProducto);
				htCompra.put(PysCompraBean.C_IDTIPOPRODUCTO, idTipoProducto);
				htCompra.put(PysCompraBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);

				Vector vDatosCompra = pysCompra.selectByPK(htCompra);
				if (vDatosCompra != null && vDatosCompra.size() > 0) {
					PysCompraBean beanCompra = (PysCompraBean) vDatosCompra.elementAt(0);
					if (beanCompra.getIdFactura() == null || beanCompra.getIdFactura().equals("")) {
						if (!pysCompra.delete(htCompra)) {
							tx.rollback();
							request.setAttribute("mensaje", "messages.updated.error");
						}
					} else{
						AltaAbonosAction abonosAction = new AltaAbonosAction();
						abonosAction.insertarNuevoAbono(idInstitucion, beanCompra.getIdFactura(), "Nuevo abono por anulacion de certificado", usr, tx);
					}
				}
			}

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			
			Hashtable<String, Object> htOld = beanSolicitud.getOriginalHash();
			Hashtable<String, Object> htNew = (Hashtable<String, Object>) htOld.clone();
			htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO);
			htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO, "sysdate");
			htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION, "sysdate");

			if (admSolicitud.update(htNew, htOld)) {
				tx.commit();
				request.setAttribute("mensaje", "messages.updated.success");
			} else {
				tx.rollback();
				request.setAttribute("mensaje", "messages.updated.error");
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}

		return "exito";

	}

	protected String denegar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
			String idInstitucion = "";
			String idSolicitud = "";

			/** Nueva ventana detalle certificado **/
			idInstitucion = form.getIdInstitucion();
			idSolicitud = form.getIdSolicitud();

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

			if (!("" + beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)) {
				throw new SIGAException("messages.certificados.error.solicitudyaaceptada");
			}

			Hashtable<String, Object> htOld = beanSolicitud.getOriginalHash();
			Hashtable<String, Object> htNew = (Hashtable<String, Object>) htOld.clone();
			htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO);
			htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO, "sysdate");
			htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION, "sysdate");

			if (admSolicitud.update(htNew, htOld)) {
				request.setAttribute("mensaje", "messages.updated.success");
			} else {
				request.setAttribute("mensaje", "messages.updated.error");
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}
		return "exito";
	}

	protected String finalizar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		int contError = 0;
		String idInstitucion = "";
		String idSolicitud = "";

		try {
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			/** Nueva ventana detalle certificado **/
			idInstitucion = form.getIdInstitucion();
			idSolicitud = form.getIdSolicitud();
			this.guardarInfoSolicitudCertificado(form, userBean);

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			finalizarCertificado(htSolicitud, request, response, contError, userBean);

		} catch (Exception e) {
			// Se cambia el estado en caso de error
			CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados();
			cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
			cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
			cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
			CerSolicitudCertificadosService cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
			List<CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
			listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			cerSolicitudCertificadosService.updateMaxivoCasoDeError(listaCerSolicitudcertificados);
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}

		return "exito";
	}

	protected String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = "";
			String idSolicitud = "";
			
			if (vOcultos != null) {
				/** Icono listado de solicitudes **/
				idInstitucion = ((String) vOcultos.elementAt(0)).trim();
				idSolicitud = ((String) vOcultos.elementAt(1)).trim();

			} else {
				/** Nueva ventana detalle certificado **/
				idInstitucion = form.getIdInstitucion();
				idSolicitud = form.getIdSolicitud();
			}			

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

			Vector vDatos = admSolicitud.selectByPK(htSolicitud);

			CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

			ArrayList<File> fCertificado = admSolicitud.recuperarVariosCertificado(solicitudCertificadoBean);

			if (fCertificado == null || fCertificado.size()<=0) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			}
			
			solicitudCertificadoBean.setFechaDescarga("SYSDATE");
		    if (!admSolicitud.updateDirect(solicitudCertificadoBean)) {
		    	throw new ClsExceptions("Error al actualizar la fecha de descarga: "+admSolicitud.getError());
		    }

			String nombrePrefijo = admSolicitud.getNombreFicheroSalida(solicitudCertificadoBean);
			String [] arrayNombrePrefijo = nombrePrefijo.split(String.valueOf(solicitudCertificadoBean.getIdSolicitud()));
			
			//Si obtenemos más de un elemento en el array habrá que generar un zip.
			if(fCertificado.size()>1){
				//Genero el zip
				generarZip(usr,idInstitucion,idSolicitud);
				
				//Recupero de nuevo los certificados ya que ahora hay uno más que será el .zip
				fCertificado = admSolicitud.recuperarVariosCertificado(solicitudCertificadoBean);
				//Recorro los certificados hasta obtener el .zip
				Iterator<File> iteratorFicheros = fCertificado.iterator();
				Boolean encontrado = Boolean.FALSE;
				File fichero = null;
				while (iteratorFicheros.hasNext() && !encontrado) {
					fichero = (File) iteratorFicheros.next();
					String nombre = fichero.getName();
					int resultado = nombre.indexOf("zip");
					if(resultado != -1) {
						encontrado = Boolean.TRUE;
					}
				}
				//Descargo el fichero .zip
				request.setAttribute("nombreFichero",fichero.getName());
				String path =  UtilidadesString.replaceAllIgnoreCase( fichero.getPath(), "\\", "/");
				request.setAttribute("rutaFichero", path);
				request.setAttribute("borrarFichero", "true");

			} else {
				request.setAttribute("nombreFichero", admSolicitud.getNombreFicheroSalida(solicitudCertificadoBean));
				String path =  UtilidadesString.replaceAllIgnoreCase( fCertificado.get(0).getPath(), "\\", "/");
				request.setAttribute("rutaFichero", path);
				request.setAttribute("borrarFichero", "false");
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, tx);
		}

		if(request.getParameter("descargarCertificado") != null && !"".equals(request.getParameter("descargarCertificado")) && request.getParameter("descargarCertificado").equalsIgnoreCase("1")){
			return "exitoDescarga";
		}else{
			return "descargaFichero";
		}	

	}

	public static void generarZip(UsrBean usr, String idInstitucion, String idSolicitud) throws ClsExceptions, SIGAException {

		CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);

		Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
		htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

		Vector vDatos = admSolicitud.selectByPK(htSolicitud);

		CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

		ArrayList<File> fCertificado = admSolicitud.recuperarVariosCertificado(solicitudCertificadoBean);

		// Genera un array con los ficheros y ruta de su carpeta
		ArrayList<Hashtable<String, Object>> arrayDatosCertificados = new ArrayList<Hashtable<String, Object>>();
		Hashtable<String, Object> hDatosCertificados = new Hashtable<String, Object>();
		for (int i = 0; i < fCertificado.size(); i++) {

			hDatosCertificados.put("fichero", fCertificado.get(i));
			hDatosCertificados.put("rutaCarpeta", fCertificado.get(i).getPath());
			arrayDatosCertificados.add(hDatosCertificados);
			hDatosCertificados = new Hashtable<String, Object>();
		}

		// Generamos el ZIP
		File ficheroZip = null;
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		// Obtenemos las rutas del zip
		String rutaAlmacen = admSolicitud.getRutaCertificadoDirectorioBD(solicitudCertificadoBean.getIdInstitucion());
		rutaAlmacen = admSolicitud.getRutaCertificadoDirectorio(solicitudCertificadoBean, rutaAlmacen);

		String rutaFicheroZip = rutaAlmacen + File.separator + solicitudCertificadoBean.getIdSolicitud() + ".zip";
		ficheroZip = new File(rutaFicheroZip);

		// Generamos el fichero zip con todas los certificados asociadas a la solicitud
		Certificado certificado = new Certificado();
		certificado.doZip(rutaAlmacen + File.separator, String.valueOf(solicitudCertificadoBean.getIdSolicitud()), fCertificado, solicitudCertificadoBean, admSolicitud);

	}

	protected String asignarPlantillaCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		try{
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    
		    String sTemp = form.getIdsTemp();
		    StringTokenizer st = new StringTokenizer(sTemp, "||");
		    
		    st.nextToken();
		    st.nextToken();
		    
		    String sCertificado = st.nextToken();
		    String idTipoProducto = st.nextToken();
		    String idProducto = st.nextToken();
		    String idProductoInstitucion = st.nextToken();
		    String idInstitucion = st.nextToken();
	
		    String porDefecto="";
		    
		    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
		    
		    Hashtable<String,Object> htPlantilla = new Hashtable<String,Object>();
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

	protected String comprobarNumPlantillas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			/** Lo primero guardamos el formulario **/
			this.guardarInfoSolicitudCertificado(form, usr);

			String sTemp = form.getIdsTemp();
		    StringTokenizer st = new StringTokenizer(sTemp, "||");

			st.nextToken();
			st.nextToken();

			String sCertificado = st.nextToken();
			String idTipoProducto = st.nextToken();
			String idProducto = st.nextToken();
			String idProductoInstitucion = st.nextToken();
			String idInstitucion = st.nextToken();

			String porDefecto = "";

			CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));

			Hashtable<String, Object> htPlantilla = new Hashtable<String, Object>();
			htPlantilla.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucion);
			htPlantilla.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
			htPlantilla.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
			htPlantilla.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);

			Vector vDatos = admPlantilla.select(htPlantilla);

			if (vDatos != null) {
				if (vDatos.size() == 0) {
					throw new SIGAException("messages.error.solicitud.noExistePlantilla");
				} else {
					if (vDatos.size() == 1) {// si solo hay una plantilla se genera directamente el PDF
						CerPlantillasBean beanPlantilla = (CerPlantillasBean) vDatos.elementAt(0);
						sTemp += "||" + beanPlantilla.getIdPlantilla();
						return generarPDFUnaPlantilla(mapping, sTemp, form, request, response);
					} else {// Si hay mas de una plantilla se muestra el combo para seleccionarla
						request.setAttribute("idsTemp", sTemp);
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

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}
		
		return "seleccionarPlantilla";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx=null;
		String salida ="";
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;

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
			
			// Comienzo control de transacciones
		    tx.begin();
		    
		    /** Se unifica el metodo guardar CerSolicitudCertificadosBean para usar en otros metodos (finalizar y aprobar) **/ 
		    CerSolicitudCertificadosBean bean = this.guardarInfoSolicitudCertificado(form, usr);

		    // No guarda cuando el texto es vacion
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
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
		    admSolicitud.actualizaTextoCertificados(bean.getIdInstitucion().toString(), bean.getIdSolicitud().toString(), beanSolicitudCertificadosTexto);

			tx.commit();

			// fin correcto
			salida = this.exitoRefresco("messages.updated.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,tx); 
		}
		    
	    return salida;
	}

	
	/**
	 * @param form
	 * @return
	 * @throws ClsExceptions 
	 */
	private CerSolicitudCertificadosBean guardarInfoSolicitudCertificado(SIGASolicitudesCertificadosForm form, UsrBean usr) throws ClsExceptions {
		CerSolicitudCertificadosBean bean = null;
		try {
			// obtengo la solicitud
			Hashtable<String, Object> ht = new Hashtable<String, Object>();
			ht.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, form.getIdInstitucion());
			ht.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, form.getIdSolicitud());
			bean = null;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			Vector v = admSolicitud.selectByPK(ht);
			if (v != null && v.size() > 0) {
				bean = (CerSolicitudCertificadosBean) v.get(0);
			}

			if (!form.getFechaEmision().trim().equals("")) {
				bean.setFechaEmisionCertificado(GstDate.getApplicationFormatDate(usr.getLanguage(), form.getFechaEmision()));
			} else {
				bean.setFechaEmisionCertificado(null);
			}
			if (!form.getFechaDescarga().trim().equals("")) {
				bean.setFechaDescarga(GstDate.getApplicationFormatDate(usr.getLanguage(), form.getFechaDescarga()));
			} else {
				bean.setFechaDescarga(null);
			}

			if (!form.getFechaSolicitud().trim().equals("")) {
				bean.setFechaSolicitud(GstDate.getApplicationFormatDate(usr.getLanguage(), form.getFechaSolicitud()));
			} else {
				bean.setFechaSolicitud(null);
			}

			if (!form.getFechaCobro().trim().equals("")) {
				bean.setFechaCobro(GstDate.getApplicationFormatDate(usr.getLanguage(), form.getFechaCobro()));

				if (form.getCodigoBanco() != null && !form.getCodigoBanco().trim().equals("")) {
					bean.setCbo_codigo(form.getCodigoBanco());
				} else {
					bean.setCbo_codigo(null);
				}

				if (form.getSucursalBanco() != null && !form.getSucursalBanco().trim().equals("")) {
					bean.setCodigo_sucursal(form.getSucursalBanco());
				} else {
					bean.setCodigo_sucursal(null);
				}

			} else {
				bean.setFechaCobro(null);
				bean.setCbo_codigo(null);
				bean.setCodigo_sucursal(null);
			}

			if (!form.getFechaEntregaInfo().trim().equals("")) {
				bean.setFechaEntregaInfo(GstDate.getApplicationFormatDate(usr.getLanguage(), form.getFechaEntregaInfo()));
			} else {
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

			if (form.getIdPersonaSolicitante() != null && !form.getIdPersonaSolicitante().equals("")) {
				bean.setIdPersona_Des(Long.valueOf(form.getIdPersonaSolicitante()));
			}

			if (form.getAceptaCesionMutualidad().equalsIgnoreCase("1")) {
				bean.setAceptaCesionMutualidad("1");
			} else {
				bean.setAceptaCesionMutualidad("");
			}

			bean.setFechaMod("sysdate");
			bean.setFechaCreacion(bean.getFechaCreacion());
			bean.setUsuCreacion(bean.getUsuCreacion());
			if (!admSolicitud.updateDirect(bean)) {
				throw new ClsExceptions("Error al actualizar solicitud: " + admSolicitud.getError());
			}
		} catch (NumberFormatException e) {
			throw e;
		} catch (ClsExceptions e) {
			throw e;
		}

		return bean;

	}
	
	protected String copiarSanciones(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		String salida ="";
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
		    SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm)formulario;
		    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    
		    // obtengo la solicitud
		    Hashtable<String,Object> ht = new Hashtable<String,Object>();
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
		    Hashtable<String,Object> ht = new Hashtable<String,Object>();
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
		    Hashtable<String,Object> ht = new Hashtable<String,Object>();
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
		List <CerSolicitudcertificados>  listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
		CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
	    String mensaje="";
	    int erroresTotales=0;
	    LogFileWriter log = null;

	    try {

		    int contadorReg=1;
		    StringTokenizer st = null;
		    String tok=form.getIdsParaFinalizar();
		    try {
		    	st = new StringTokenizer(tok, ";");
			    contadorReg=st.countTokens();
		    } catch (java.util.NoSuchElementException nee) {
		    	// solamente existe un token
		    }
		    //Si es menos de 20 elementos de hace de manera online, sino se almacenarán los registros y se ejecutarán a través de un demonio
		    if(contadorReg <= 20){

		    int contErrores=0;
		    while (st.hasMoreElements())
		    {
		    	String idSolicitud = "";
			    	String to = (String)st.nextToken();
					        StringTokenizer st2 = new StringTokenizer(to, "||");
		
			        st2.nextToken();
			        idSolicitud = st2.nextToken();
			        st2.nextToken();
			        st2.nextToken();
			        st2.nextToken();
			        st2.nextToken();
			        String idInstitucion = st2.nextToken();
			        // nos saltamos la persona
			        st2.nextToken();
			        st2.nextToken();

			        /////////////////////////////////////////////////
				    Hashtable<String,Object> htSolicitud = new Hashtable<String,Object>();
				    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
				    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
						    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
				    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
				    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
				    //Creamos el log de error
			        log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(beanSolicitud),idSolicitud+"-LogError");
					log.clear();
				 // solo si no esta anulado y no esta denegado
					if ( beanSolicitud.getIdEstadoSolicitudCertificado()!= Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) 
							&& beanSolicitud.getIdEstadoSolicitudCertificado()!=Integer.parseInt(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO)) {
						 //aalg: INC_10287_SIGA. Se unifica la manera de finalizar las solicitudes de certificados
						    try{
						    	
						    	finalizarCertificado(htSolicitud, request, response, contErrores,userBean);
						    	
						    }catch(Exception e){
						    	log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), ((SIGAException) e).getLiteral(userBean.getLanguage())});
						    	  /** Escribiendo fichero de log **/
								if (log != null)
									log.flush();
						    
								 //Volvemos a ponerle en el estado anterior al producirse un fallo
								CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados();
				        	    cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
						        cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
						        cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
								listaCerSolicitudcertificados.add(cerSolicitudcertificados);	
						        cerSolicitudCertificadosService.updateMaxivoCasoDeError(listaCerSolicitudcertificados);
						        
								erroresTotales++;
						    	
						    }
					}else{
			        	ClsLogging.writeFileLog("----- ERROR FINALIZAR PDF -----",4);
			        	CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(userBean);
						log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": no es " +
	    				"posible procesarlo. Tendrá que crear otro nuevo certificado si es necesario."});
				    	  /** Escribiendo fichero de log **/
						if (log != null)
							log.flush();
						
						erroresTotales++;
					}
						    
						    
		    }
				    String tipoAlert ="";   
			    	tipoAlert=erroresTotales==0?"success":"warning";
				    request.setAttribute("estiloMensaje",tipoAlert);	
					request.setAttribute("mensaje","Se han finalizado con éxito "+(contadorReg-erroresTotales)+" y se han producido fallo en "+erroresTotales+" certificados.");
		    
				     
		    } else {
		    	
		    	//Cuando es más de 20 se ejecuta el proceso automático cada min
		    	CerSolicitudCertificadosAccionService cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance().getService(CerSolicitudCertificadosAccionService.class);
				CerSolicitudesAccion cerSolicitudesAccion = new CerSolicitudesAccion();
				CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados();
				List <CerSolicitudesAccion> listaCerSolicitudesAccion = new ArrayList<CerSolicitudesAccion>();
				   while (st.hasMoreElements())
				    {
				    	String to = (String)st.nextToken();
				    	if (to.equals("undefined")) {
				    		continue;
				    	}
			    	  StringTokenizer st2 = new StringTokenizer(to, "||");
			    		
				        st2.nextToken();
				        String idSolicitud = st2.nextToken();

						 st2.nextToken();
						 
						st2.nextToken();
				        st2.nextToken();
				        st2.nextToken();
				        String idInstitucion = st2.nextToken();
				 
				       
				        //Rellenamos los objetos
				        cerSolicitudesAccion.setIdinstitucion(Short.valueOf(idInstitucion));
				        cerSolicitudcertificados.setIdinstitucion(Short.valueOf(idInstitucion));
				        cerSolicitudesAccion.setIdsolicitud(Long.valueOf(idSolicitud));
				        cerSolicitudcertificados.setIdsolicitud(Long.valueOf(idSolicitud));
				        cerSolicitudesAccion.setAccion(Long.valueOf(CerSolicitudCertificadosAdm.A_FINALIZAR));
				        cerSolicitudesAccion.setUsucreacion(new Integer(userBean.getUserName()));
				        cerSolicitudcertificados.setUsucreacion(new Integer(userBean.getUserName()));
				        cerSolicitudesAccion.setUsumodificacion(new Integer(userBean.getUserName()));
				        cerSolicitudesAccion.setIdseriefacturacionseleccionada("-1");
				        
				        //Insertamos en la tabla de acciones
				        listaCerSolicitudcertificados.add(cerSolicitudcertificados);
				        listaCerSolicitudesAccion.add(cerSolicitudesAccion);
				        
						cerSolicitudesAccion = new CerSolicitudesAccion();
						cerSolicitudcertificados = new CerSolicitudcertificados();
						
		    }
				 //Insertamos en la tabla acciones
				    cerSolicitudCertificadosAccionService.insertMaxivo(listaCerSolicitudesAccion);
				 //Cambiamos el estado en la tabla solicitudCertificado
			        cerSolicitudCertificadosService.updateMasivoFinalizar(listaCerSolicitudcertificados);
		     
			        String tipoAlert ="success";   
				    request.setAttribute("estiloMensaje",tipoAlert);	
					request.setAttribute("mensaje","Las solicitudes han sido marcadas para ser procesadas. Nº total de solicitudes: "+contadorReg);
		    }

	    }catch (Exception e){
	    	String tipoAlert ="";   
		    tipoAlert="error";
			request.setAttribute("estiloMensaje",tipoAlert);	
			request.setAttribute("mensaje","Se ha producido un error en la finalización masiva.");
	        this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
	    }
        return "exitoConString";

	}
    
	 //aalg: INC_10287_SIGA. Se unifica la manera de finalizar las solicitudes de certificados
	public static void  finalizarCertificado(Hashtable<String,Object> htSolicitud, HttpServletRequest request, HttpServletResponse response, int contErrores, UsrBean userBean) 
			throws SIGAException{
		
	    UserTransaction tx = userBean.getTransactionPesada();
	    String idInstitucion=(String)htSolicitud.get(CerSolicitudCertificadosBean.C_IDINSTITUCION);
	    String idSolicitud=(String)htSolicitud.get(CerSolicitudCertificadosBean.C_IDSOLICITUD);
        CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
		
        try{
        	
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
		    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
		    
		    if (!((""+beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)) &&
		        !((""+beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)) &&
		        !((""+beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR)))
			{

			    //RGG 05-09-2005 anhado al estado generado el de firmado ya que no parece que sea nunca generado. Pasa directamente a firmado. 
			    if (!(""+beanSolicitud.getIdEstadoCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO) &&
				    !(""+beanSolicitud.getIdEstadoCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO))
			    {
			        throw new SIGAException("messages.certificados.error.nogenerado");
			    }

			    Hashtable<String,Object> htOld = beanSolicitud.getOriginalHash();
			    Hashtable<String,Object> htNew = (Hashtable<String,Object>)htOld.clone();
			    htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION,"sysdate");
			    htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
			    if(beanSolicitud.getFechaCobro() != null && !"".equalsIgnoreCase(beanSolicitud.getFechaCobro())){
			    	  htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);	
			    }else{
			    htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
			    }
			    
			    tx.begin();
			    if (admSolicitud.update(htNew, htOld)) {
			    	//Si viene vacio quiere decir que estamos llamando a este método desde el demonio
			    	if(request != null){
			        request.setAttribute("mensaje", "messages.updated.success");
			    	}else{
			    		ClsLogging.writeFileLog("-- SE HA FINALIZADO CON EXITO --",4);
			    	}
			        
			        // YGE 07/09/2005 Si todo ha ido bien insertamos el registro en la tabla PysCompra
			        // MAV 13/09/2005 Resolución incidencia
			        
			        Hashtable<String,Object> claves = new Hashtable<String,Object>();
					UtilidadesHash.set(claves, CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
					UtilidadesHash.set(claves, CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
					CerSolicitudCertificadosAdm solicitudAdm = new CerSolicitudCertificadosAdm(userBean);
					CerSolicitudCertificadosBean solicitudBean = (CerSolicitudCertificadosBean) solicitudAdm.selectByPK(claves).get(0);
		
					claves.clear();
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDINSTITUCION, idInstitucion);
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solicitudBean.getPpn_IdTipoProducto());
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDPRODUCTO, solicitudBean.getPpn_IdProducto());
					UtilidadesHash.set(claves, PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solicitudBean.getPpn_IdProductoInstitucion());
					PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(userBean);
					
					// MAV 12/9/2005 Resolucion incidencia
					//PysProductosSolicitadosBean productoBean = (PysProductosSolicitadosBean) productoAdm.selectByPK(claves).get(0);
					Vector productosInstitucion = productoAdm.selectByPK(claves);
					if (productosInstitucion.isEmpty()){
				    	tx.rollback();
				        throw new SIGAException("messages.certificado.error.noFinalizacion");
					
					}else{
						PysProductosInstitucionBean productoBean = (PysProductosInstitucionBean) productosInstitucion.get(0);
						if (productoBean.getTipoCertificado().equalsIgnoreCase("C")){
							claves.clear();
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDINSTITUCION, idInstitucion);
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, solicitudBean.getPpn_IdTipoProducto());
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPETICION, solicitudBean.getIdPeticionProducto());
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTO, solicitudBean.getPpn_IdProducto());
							UtilidadesHash.set(claves, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, solicitudBean.getPpn_IdProductoInstitucion());
							PysProductosSolicitadosAdm productoSolicitadoAdm = new PysProductosSolicitadosAdm(userBean);
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
								GenParametrosAdm admParametros = new GenParametrosAdm(userBean);
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
										CenInstitucionAdm cenInstitucion = new CenInstitucionAdm(userBean);
										Hashtable<String,Object> datosInstitucion = new Hashtable<String,Object>();
										datosInstitucion.put(CenInstitucionBean.C_IDINSTITUCION,beanSolicitud.getIdInstitucionDestino());
										Vector v_datosInstitucion = cenInstitucion.selectByPK(datosInstitucion);
										CenInstitucionBean b = (CenInstitucionBean) v_datosInstitucion.get(0);
										//compraBean.setIdPersona(new Long(b.getIdPersona().intValue()));
								    if (productoSolicitadoBean.getIdPersona().intValue()!=b.getIdPersona().intValue()){
											 compraBean.setIdPersonaDeudor(new Long(b.getIdPersona().intValue()));
										
	//									 comprobamos que es cliente del colegio que finaliza el certificado
										CenClienteAdm cenCliente=new CenClienteAdm(userBean);
										Hashtable<String,Object> datosCliente = new Hashtable<String,Object>();
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
										  	Hashtable<Integer,Object> codigos=new Hashtable<Integer, Object>();
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
									
								} else {
									compraBean.setIdPersonaDeudor(null);
									compraBean.setIdCuentaDeudor(null);
									compraBean.setIdCuenta(null);// cuando el check de cobrado esta activado el cobro se le hace al cliente en metalico luego no rellenamos el idcuenta. 
								 }

								//compraBean.setIdPersona(productoSolicitadoBean.getIdPersona());
								
								// RGG 29-04-2005 cambio para insertar la descripcion
								// buscamos la descripcion
								PysProductosInstitucionAdm pyspiAdm = new PysProductosInstitucionAdm(userBean);
								Hashtable<String,Object> claves2 = new Hashtable<String,Object>();
								claves2.put(PysProductosInstitucionBean.C_IDINSTITUCION,productoBean.getIdInstitucion());
								claves2.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,productoBean.getIdTipoProducto());
								claves2.put(PysProductosInstitucionBean.C_IDPRODUCTO,productoBean.getIdProducto());
								claves2.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,productoBean.getIdProductoInstitucion());
								Vector vpi = pyspiAdm.selectByPK(claves2);
								String descripcion = "";
	//							 Obtenemos el nombre y apellidos del solicitante
								CenPersonaAdm datosPersona=new CenPersonaAdm(userBean);
								Hashtable<String,Object> hashPersona=new Hashtable<String,Object>();
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
								
								compraBean.setIdTipoIva(productoSolicitadoBean.getIdTipoIva());
								PysCompraAdm compraAdm = new PysCompraAdm(userBean);
								try {
									if (!compraAdm.insert(compraBean)) {
										tx.rollback();
										if(request != null){
										request.setAttribute("mensaje", "messages.inserted.error");
									} else {
											ClsLogging.writeFileLog("-- SE HA PRODUCIDO UN ERROR EN LA FINALIZACIÓN --",4);
										}
										
									} else {
										tx.commit();
										
										/**********************************  ENVIO A LA MUTUALIDAD     ******************************/
										try {
											if(beanSolicitud.getAceptaCesionMutualidad() != null && beanSolicitud.getAceptaCesionMutualidad().equals("1")){
												MutualidadService service = (MutualidadService) BusinessManager.getInstance().getService(MutualidadService.class);
												service.insertarFinalizacionCertificado(productoSolicitadoBean.getIdPersona(),beanSolicitud.getIdInstitucionOrigen());
											}
										} catch (Exception e) {											
											e.printStackTrace();
											enviarEmailResumenEnvios(nombre+" "+apellido1+" "+apellido2,productoSolicitadoBean.getIdPersona().toString(),e);
										}
										/**********************************  FIN ENVIO A LA MUTUALIDAD     ******************************/
									}
								} catch (Exception e) {
									tx.rollback();
									throw e;
								}
							}
						} else {
							tx.commit();
						}
					}
				} else {
					tx.rollback();
					if(request != null){
					request.setAttribute("mensaje", "messages.updated.error");
					}else{
						ClsLogging.writeFileLog("-- SE HA PRODUCIDO UN ERROR EN LA FINALIZACIÓN--",4);
				}
			}
			}else{
				CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(userBean);
				switch(beanSolicitud.getIdEstadoSolicitudCertificado()){
				case 1:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": es " +
		    				"necesario aprobarlo y generarlo antes de finalizar.");
				case 10:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": ya " +
		    				"no se puede finalizar más.");
				case 4:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": ya " +
		    				"no se puede finalizar más.");
				case 7:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
		    				"Si lleva más de una hora sin cambiar, contacte con el Administrador.");
				case 11:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
		    				"Si lleva más de una hora sin cambiar, contacte con el Administrador.");	
				case 8:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
							"Si lleva más de una hora sin cambiar, contacte con el Administrador.");
				case 12:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
		    				"Si lleva más de una hora sin cambiar, contacte con el Administrador.");
				case 9:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
		    				"Si lleva más de una hora sin cambiar, contacte con el Administrador.");
				case 13:
					throw new SIGAException("El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
		    				"Si lleva más de una hora sin cambiar, contacte con el Administrador.");
				
					
				}
	
			}
        ////////////////////////////////////////////////
        }catch(Exception e){
        	contErrores++;
    		ClsLogging.writeFileLog("----- ERROR FINALIZACION -----",4);
    		ClsLogging.writeFileLog("ERROR EN FINALIZACION MASIVA. solicitud certificado: "+idSolicitud+" Error: "+e.toString(),4);

    		try {
    			if (tx!=null) {
    				tx.rollback();
    			}
    		} catch (Exception el) {
    			//el.printStackTrace();
    		}
    		if (e!=null && e instanceof SIGAException) {
    			((SIGAException)e).setParams(new String[] {"modulo.certificados"});
    			throw (SIGAException)e; 
    		}
    		if (e!=null) {
    			SIGAException se = new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
    			// RGG Indico que es clsExceptions para  mostrar el codigo de error  
    			se.setClsException(true);
    			throw se;
    		}
        }
        
	}
	
	public static void enviarEmailResumenEnvios(String nombre, String idPersona, Exception e)	throws BusinessException {
		try {			
			SigaServiceHelperService serviceHelperService = (SigaServiceHelperService) BusinessManager.getInstance().getService(SigaServiceHelperService.class);
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
    	   
			String from = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.MUTUALIDAD_MAILFALLOINSERCION_FROM,MODULO.ECOM);
    	    String bcc = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.MUTUALIDAD_MAILFALLOINSERCION_BCC,MODULO.ECOM);
    	    String[] bccArray = bcc.split(";"); 
    	    
    	    /** ASUNTO */
    	    Properties props = PropertyReader.getProperties(SIGAReferences.RESOURCE_FILES.SIGA);
    	    String entorno = props.getProperty("administracion.login.entorno"); //Sacar a constante
    	    String asunto = "[SIGA - MUTUALIDAD] ["+entorno+"] Fallo en la inserción en EcomMutualidadCertificados";
    	    
    	    /** Tras recuperar los parámetros de configuración del mail rellenamos el cuerpo del mensaje */
    	    StringBuffer sb = new StringBuffer();
    	    sb.append("["+ new Date() + "] Error en la inserción de EcomMutualidadCertificados del colegiado " + nombre + " con IdPersona "+idPersona);
    	    SIGAServicesHelper.saltoLinea(sb);		
    	    sb.append("Exception: " +e);
    	    SIGAServicesHelper.saltoLinea(sb);
    	    sb.append("Mensaje Exception: " +e.getMessage());
    	    SIGAServicesHelper.saltoLinea(sb);
    	    sb.append("Causa: " +e.getCause());
    	    
    	    serviceHelperService.enviarCorreo(from, bccArray, asunto, sb.toString(), null, GEN_PROPERTIES.mail_smtp_host, GEN_PROPERTIES.mail_smtp_user, GEN_PROPERTIES.mail_smtp_pwd);
			
		} catch (Exception ex) {
			ClsLogging.writeFileLog("Error al enviar el correo con informe resumen de carga recibida.", 4);			
		}
	}	

	protected String facturarCertificadosSeleccionados(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	   SIGASolicitudesCertificadosForm formSolicitudesCertificados = (SIGASolicitudesCertificadosForm)formulario;
	   String idSerieSeleccionada = (String) formSolicitudesCertificados.getIdSerieSeleccionada();
	   UsrBean usr = this.getUserBean(request);
	   String[] claves = {CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD};
	   String[] campos = {CerSolicitudCertificadosBean.C_FECHAESTADO,CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
  	   Facturacion facturacion = new Facturacion(usr);
  	   FacFacturaAdm admFactura = new FacFacturaAdm(usr);
  	   CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
	   List <CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
	   LogFileWriter log = null;
	   int contadorError=0;
	   
	   
    	if (idSerieSeleccionada!=null) {
    		idSerieSeleccionada = idSerieSeleccionada.trim();
    	}	
		   String listadoDeIdsParaFacturar=formSolicitudesCertificados.getIdsParaFacturar();
		    
		   String [] arrayInformacionDeCadaCertificado  = listadoDeIdsParaFacturar.split(";");
		   
		    //Si es menos de 20 elementos de hace de manera online, sino se almacenarán los registros y se ejecutarán a través de un demonio
		    if(arrayInformacionDeCadaCertificado.length <= 20){
		    	//Recorro el array para obtener los datos de cada certificada/solicitud. y obtener las compras, en este caso cada compra coincide con cada certificado
				for (String s: arrayInformacionDeCadaCertificado) { 
					if(s != null && !"".equalsIgnoreCase(s)){
						  Hashtable<String,Object> htNew = null;
						try {	
							 
							String [] informacionDeUnCertificado = s.split(",");
							//Sólo se llamará a esta función cuando el estado del certificado sea Pendiente de facturar que pasará a estar facturando
							//Obtenemos los datos del certificado
							Hashtable<String,Object> htSolicitud = new Hashtable<String,Object>();
						    htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, (informacionDeUnCertificado[1].split("="))[1]);
						    htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,  (informacionDeUnCertificado[0].split("="))[1]);
						    CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
						
						    Vector vDatos = admSolicitud.selectByPK(htSolicitud);
						    CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vDatos.elementAt(0);
						    htNew = beanSolicitud.getOriginalHash();	
						   
						    log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(beanSolicitud),beanSolicitud.getIdSolicitud()+"-LogError");
							log.clear();
							 
							    if (String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR) || 
							    		String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()).equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FACTURANDO)){
								    	//Si es pendiente de facturar pasamos el estado a facturando.
								    	try{
								    		//Si es pendiente de facturar pasamos el estado a facturando.
									    	htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FACTURANDO); 
									    	admSolicitud.updateDirect(htNew, claves, campos );
									    	// Obtiene las facturas de una solicitud de certificado
								    		 Vector<Hashtable<String,Object>> vFacturas = admFactura.obtenerFacturasFacturacionRapida((informacionDeUnCertificado[1].split("="))[1], null, (informacionDeUnCertificado[0].split("="))[1]);	
								    		 if (vFacturas==null ||  vFacturas.size()==0) { // No esta facturado 
									    	facturacion.facturacionRapidaProductosCertificados((informacionDeUnCertificado[1].split("="))[1], null, idSerieSeleccionada, (informacionDeUnCertificado[0].split("="))[1], request);
									    	//Pasamos a facturado
										    	htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
									    	htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
									    	admSolicitud.updateDirect(htNew, claves, campos );
								    		 }else{
								    			 //Está facturado, se comprueba si la factura está en revisión o no
									    			// Obtengo los datos de la factura
										        	Hashtable<String,Object> hFactura = vFacturas.get(0);
										        	String estadoFacturacion=UtilidadesHash.getString(hFactura, FacFacturaBean.C_ESTADO);
										        	if(estadoFacturacion != null && !"".equalsIgnoreCase(estadoFacturacion)){
										        		if(Integer.parseInt(estadoFacturacion) != 7){
										        			//Pasamos a finalizado, la factura está confirmada
													    	htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
													    	htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
													    	admSolicitud.updateDirect(htNew, claves, campos );
										        		}else{
										        			//La factura está en revisión introducimos mensaje 
										        			log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado ya está facturado, pero la factura está En revisión. "+
										        						"Si quiere continuar con la facturación rápida de este certificado, hay que eliminar previamente la facturación de Certificados " +
										        						"NO confirmada en Facturación > Mantenimiento Facturación."});
										        			  /** Escribiendo fichero de log **/
															if (log != null)
																log.flush();
								    	
															contadorError++;
															String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
												    		htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);
												    		admSolicitud.updateDirect(htNew, claves, camposAux );
										        		}
										        		
										        	}
									    			
									    		 }
								    	
								    	}catch(SIGAException e){
								    		log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),((SIGAException) e).getLiteral(usr.getLanguage())});
									    	  /** Escribiendo fichero de log **/
											if (log != null)
												log.flush();
								    		
											contadorError++;
											String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
								    		htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);
								    		admSolicitud.updateDirect(htNew, claves, camposAux );
								    	}catch(ClsExceptions e){
								    		log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), e.getMsg()});
									    	  /** Escribiendo fichero de log **/
											if (log != null)
												log.flush();
								    		
											contadorError++;
											String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
								    		htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);
								    		admSolicitud.updateDirect(htNew, claves, camposAux );
								    	}
								   }else{
									   CerEstadoSoliCertifiAdm estAdm = new CerEstadoSoliCertifiAdm(usr);
										switch(beanSolicitud.getIdEstadoSolicitudCertificado()){
										case 1:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": es " +
								    				"necesario aprobarlo y generarlo, y finalizarlo antes de facturar."});
											break;
										case 4:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": ya " +
								    				"no se puede facturar."});
											break;
										case 7:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
								    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
											break;
										case 11:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
								    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
											break;
										case 8:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
													"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
											break;
										case 12:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
								    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
											break;
										case 9:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
								    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
											break;
										case 13:
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "El certificado está "+estAdm.getNombreEstadoSolicitudCert(String.valueOf(beanSolicitud.getIdEstadoSolicitudCertificado()))+": espere a que termine.  " +
								    				"Si lleva más de una hora sin cambiar, contacte con el Administrador."});
											break;
										}
								    	  /** Escribiendo fichero de log **/
										if (log != null)
											log.flush();
										
										contadorError++;
									   
								   
								 }
										
						    } catch (Exception e) {
								   log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "Error en la facturación del certificado"});
							    	  /** Escribiendo fichero de log **/
									if (log != null)
										log.flush();
								
									if (e instanceof SIGAException || e instanceof ClsExceptions)
									throwExcp (e.getMessage(), new String[] {"modulo.certificados"}, e, null); 
								else
									throwExcp("messages.general.error", new String[] {"modulo.certificados"}, e, null); 
							}
					}
				}
				
				 String tipoAlert ="";   
			     tipoAlert=contadorError==0?"success":"warning";
				 request.setAttribute("estiloMensaje",tipoAlert);	
				 //arrayInformacionDeCadaCertificado.length-1, lleva el -1 debido a que la primera posición es vacia siempre
			     request.setAttribute("mensaje","Se han facturado con éxito "+((arrayInformacionDeCadaCertificado.length-1)-contadorError)+" y se han producido fallo en "+contadorError+" certificados.");
				    
		    }else{
		    	try{
				    	//Cuando es más de 20 se ejecuta el proceso automático cada min
						CerSolicitudCertificadosAccionService cerSolicitudCertificadosAccionService = (CerSolicitudCertificadosAccionService) BusinessManager.getInstance().getService(CerSolicitudCertificadosAccionService.class);
						CerSolicitudesAccion cerSolicitudesAccion = new CerSolicitudesAccion();
						CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados();
						List <CerSolicitudesAccion> listaCerSolicitudesAccion = new ArrayList<CerSolicitudesAccion>();
						for (String s: arrayInformacionDeCadaCertificado) { 
							if(s != null && !"".equalsIgnoreCase(s)){
								String [] informacionDeUnCertificado = s.split(",");
								
								 //Rellenamos los objetos
						        cerSolicitudesAccion.setIdinstitucion(Short.valueOf((informacionDeUnCertificado[1].split("="))[1]));
						        cerSolicitudcertificados.setIdinstitucion(Short.valueOf((informacionDeUnCertificado[1].split("="))[1]));
						        cerSolicitudesAccion.setIdsolicitud(Long.valueOf((informacionDeUnCertificado[0].split("="))[1]));
						        cerSolicitudcertificados.setIdsolicitud(Long.valueOf((informacionDeUnCertificado[0].split("="))[1]));
						        cerSolicitudesAccion.setAccion(Long.valueOf(CerSolicitudCertificadosAdm.A_FACTURAR));
						        cerSolicitudesAccion.setIdseriefacturacionseleccionada(idSerieSeleccionada);
						        cerSolicitudesAccion.setUsucreacion(new Integer(usr.getUserName()));
						        cerSolicitudcertificados.setUsucreacion(new Integer(usr.getUserName()));
						        cerSolicitudesAccion.setUsumodificacion(new Integer(usr.getUserName()));
						        
						      //Insertamos en la tabla de acciones
						        listaCerSolicitudcertificados.add(cerSolicitudcertificados);
						        listaCerSolicitudesAccion.add(cerSolicitudesAccion);
						        
						    	cerSolicitudesAccion = new CerSolicitudesAccion();
								cerSolicitudcertificados = new CerSolicitudcertificados();
							}
						}
		 
						 //Insertamos en la tabla acciones
						    cerSolicitudCertificadosAccionService.insertMaxivo(listaCerSolicitudesAccion);
						 //Cambiamos el estado en la tabla solicitudCertificado
					        cerSolicitudCertificadosService.updateMasivoFacturar(listaCerSolicitudcertificados);	
					     
				        String tipoAlert ="success";   
					    request.setAttribute("estiloMensaje",tipoAlert);	
						request.setAttribute("mensaje","Las solicitudes han sido marcadas para ser procesadas. Nº total de solicitudes: "+(arrayInformacionDeCadaCertificado.length-1));
						
		    	}catch(Exception e){
		    		 String tipoAlert ="error";   
					 request.setAttribute("estiloMensaje",tipoAlert);	
				     request.setAttribute("mensaje","Se ha producido un error en la facturación masiva de más de 20 elementos.");
		    	}
		    }

	    return "exitoConString";
	}
	/**
	 * Notas Jorge PT 118:
     * - Certificados > Gestión de solicitudes (facturacion rapida)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String facturacionRapida(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";

		UsrBean usr = this.getUserBean(request);
		CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
		String[] claves = { CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD };
		String[] campos = {CerSolicitudCertificadosBean.C_FECHAESTADO,CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};

		try {

			// datos llamada
			SIGASolicitudesCertificadosForm formSolicitudesCertificados = (SIGASolicitudesCertificadosForm) formulario;
			Hashtable<String, Object> htNew = null;
			String idInstitucion = "", idSolicitudCertificado = "", idSerieSeleccionada = "";
			Vector vOcultos = formSolicitudesCertificados.getDatosTablaOcultos(0);
			if (vOcultos != null) {
				salida = "descarga";
				idInstitucion = ((String) vOcultos.elementAt(0)).trim();
				idSolicitudCertificado = ((String) vOcultos.elementAt(1)).trim();
				idSerieSeleccionada = (String) vOcultos.elementAt(2);
				if (idSerieSeleccionada != null) {
					idSerieSeleccionada = idSerieSeleccionada.trim();
				}
			} else {
				salida = "exitoDescarga";
				idInstitucion = formSolicitudesCertificados.getIdInstitucion();
				idSolicitudCertificado = formSolicitudesCertificados.getIdSolicitud();
				idSerieSeleccionada = formSolicitudesCertificados.getIdSerieSeleccionada();
				this.guardarInfoSolicitudCertificado(formSolicitudesCertificados,usr);
			}
			// Sólo se llamará a esta función cuando el estado del certificado sea Pendiente de facturar que pasará a estar facturando
			// Obtenemos los datos del certificado
			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitudCertificado);

			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			htNew = beanSolicitud.getOriginalHash();
			try {
				Facturacion facturacion = new Facturacion(usr);
				FacFacturaAdm admFactura = new FacFacturaAdm(usr);
				Vector<Hashtable<String,Object>> vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, null, idSolicitudCertificado);	
				 if (vFacturas==null ||  vFacturas.size()==0) { // No esta facturado 
				facturacion.facturacionRapidaProductosCertificados(idInstitucion, null, idSerieSeleccionada, idSolicitudCertificado, request);
				request.setAttribute("mensaje", "messages.updated.success");
				request.setAttribute("borrarFichero", "false");		
				// Pasamos a facturado
					htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
			    	htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
			    	admSolicitud.updateDirect(htNew, claves, campos );
				 }else{
						//Está facturado, se comprueba si la factura está en revisión o no
			    			// Obtengo los datos de la factura
				        	Hashtable<String,Object> hFactura = vFacturas.get(0);
				        	String estadoFacturacion=UtilidadesHash.getString(hFactura, FacFacturaBean.C_ESTADO);
				        	if(estadoFacturacion != null && !"".equalsIgnoreCase(estadoFacturacion)){
				        		if(Integer.parseInt(estadoFacturacion) != 7){
				        			//Pasamos a finalizado, la factura está confirmada
				        			//Import! volvemos a llamar al método facturacionRapidaProductosCertificados, al existir factura este método sólo descargará la factura.
				        			facturacion.facturacionRapidaProductosCertificados(idInstitucion, null, idSerieSeleccionada, idSolicitudCertificado, request); 
				        			request.setAttribute("mensaje", "messages.updated.success");
									request.setAttribute("borrarFichero", "false");		
							    	htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
				htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO);
				admSolicitud.updateDirect(htNew, claves, campos);
				        		}else{
				        			//La factura está en revisión introducimos mensaje 
									String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
						    		htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);
						    		admSolicitud.updateDirect(htNew, claves, camposAux );
						    		String tipoAlert ="error";   
									request.setAttribute("estiloMensaje",tipoAlert);	
								     request.setAttribute("mensaje","El certificado ya está facturado, pero la factura está En revisión. "+
				        						"Si quiere continuar con la facturación rápida de este certificado, hay que eliminar previamente la facturación de Certificados " +
				        						"NO confirmada en Facturación > Mantenimiento Facturación.");
						    		
				        		}
				        		
				        	}
					 }
			} catch (Exception e) {
				String[] camposAux = {CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO};
				htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND_FACTURAR);
				admSolicitud.updateDirect(htNew, claves, camposAux);
				throw new SIGAException("Error en la facturación");
			}


		} catch (Exception e) {
			if (e instanceof SIGAException || e instanceof ClsExceptions)
				throwExcp(e.getMessage(), new String[] { "modulo.certificados" }, e, null);
			else
				throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}

		return salida;
	}
	


	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxSeleccionSerieFacturacionFacturacionMasiva (HttpServletRequest request, HttpServletResponse response) throws Exception {			
		
		String listadoDeSolicitudes = request.getParameter("listadoDeSolicitudes");
		if (listadoDeSolicitudes==null || listadoDeSolicitudes.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el listado de solicitudes");
		
		JSONObject json = new JSONObject();
		UsrBean usr = this.getUserBean(request);		
		String idSerieFacturacion = "";	
		
		PysCompraAdm admCompra = new PysCompraAdm(usr);

		//Obtenemos los elementos que se encuentran separados por ;
		String [] arrayInformacionDeCadaCertificado = listadoDeSolicitudes.split(";");
		PysCompraBean pysCompraBean =new PysCompraBean();
		Vector<PysCompraBean> vectorPysCompraBean = new Vector<PysCompraBean> ();

		//Recorro el array para obtener los datos de cada certificada/solicitud. y obtener las compras, en este caso cada compra coincide con cada certificado
		for (String s: arrayInformacionDeCadaCertificado) { 
			if(s != null && !"".equalsIgnoreCase(s)){

				String [] informacionDeUnCertificado = s.split(",");
				try{
					pysCompraBean = admCompra.obtenerCompraCertificado((informacionDeUnCertificado[1].split("="))[1], (informacionDeUnCertificado[0].split("="))[1]);
				}catch  (Exception e){
					//Pueden venirte certificados que no tengan compra en ese caso pysCompraBean estará inicializado pero sin valor luego  no lo añadimos ya que no se podrán facturar.	
					//Si no ponemos el catch se mostrará un error por el log y no se ejecutarán los demás certificados
				}
				if(pysCompraBean.getIdInstitucion() != null)
					vectorPysCompraBean.add(pysCompraBean);
				pysCompraBean=new PysCompraBean();
			}
				}


		if (vectorPysCompraBean==null || vectorPysCompraBean.size()==0) {
			//throw new SIGAException("messages.facturacionRapidaCompra.noElementosFacturables");
			idSerieFacturacion = "-1";
		}else{

			// Busca las series de facturacion del producto
	    	FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usr);
			Vector<FacSerieFacturacionBean> vSeriesFacturacion =  admSerieFacturacion.obtenerSeriesAdecuadas(vectorPysCompraBean);
				
		    // Compruebo que tiene una serie de facturacion
		    if (vSeriesFacturacion.size()==1) {
		    	FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) vSeriesFacturacion.get(0);
		    	idSerieFacturacion = beanSerieFacturacion.getIdSerieFacturacion().toString();
				
		    } else { // Tiene varias series de facturacion asociadas al producto
				
		    	// Indico la primera opcion del seleccionable
		    	String sOptionsSerieFacturacion = "<option value=''>" + UtilidadesString.getMensajeIdioma(usr, "general.combo.seleccionar") + "</option>";
				
		    	// Cargo el resto de opciones del seleccionable
		    	for (int i = 0; i < vSeriesFacturacion.size(); i++)	{
		    		FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) vSeriesFacturacion.get(i);
		    		sOptionsSerieFacturacion +="<option value='" +  beanSerieFacturacion.getIdSerieFacturacion() + "'>" + beanSerieFacturacion.getNombreAbreviado() + "</option>";
			}

		    	// Devuelvo la lista de series de facturacion
		    	ArrayList<String> aOptionsSeriesFacturacion = new ArrayList<String>();
		    	aOptionsSeriesFacturacion.add(sOptionsSerieFacturacion);
		    	json.put("aOptionsSeriesFacturacion", aOptionsSeriesFacturacion);
	        }
	        
		    

		}
		// Deuelve el identificador de la serie de facturacion
		json.put("idSerieFacturacion", idSerieFacturacion);
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 		
	}
	
	/**
	 * Obtiene las series de facturacion que estan asociadas al tipo de producto del certificado
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxSeleccionSerieFacturacion (HttpServletRequest request, HttpServletResponse response) throws Exception {			
		
		String idInstitucion = request.getParameter("idInstitucion");
		if (idInstitucion==null || idInstitucion.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador de la institución");
		
		String idTipoProducto = request.getParameter("idTipoProducto");
		if (idTipoProducto==null || idTipoProducto.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador del tipo de producto");
		
		String idProducto = request.getParameter("idProducto");
		if (idProducto==null || idProducto.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador del producto");
		
		String idSolicitud = request.getParameter("idSolicitud");
		if (idSolicitud==null || idSolicitud.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador de la solicitud");
		
		String idPersona = request.getParameter("idPersona");
		if (idPersona==null || idPersona.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador de la persona");				
		
		JSONObject json = new JSONObject();
		UsrBean usr = this.getUserBean(request);
		FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usr);
		String idSerieFacturacion = "";	
		
		// Obtengo la peticion de compra
    	PysCompraAdm admCompra = new PysCompraAdm(usr);
	    PysCompraBean beanCompra = admCompra.obtenerCompraCertificado(idInstitucion, idSolicitud);
	    
	    // Compruebo si esta facturada la compra
	    if (beanCompra.getIdFactura()!=null && !beanCompra.getIdFactura().trim().equals("")) {	
	    	idSerieFacturacion = "Facturado"; // JPT: Esto sirve para indicar que ya esta facturado
	    	
	    } else {
		
			// Busca las series de facturacion del producto
			Vector<FacSerieFacturacionBean> vSeriesFacturacion =  admSerieFacturacion.obtenerSeriesFacturacionProducto(idInstitucion, idTipoProducto, idProducto, idPersona);
			    		
		    // Compruebo que tiene una serie de facturacion
		    if (vSeriesFacturacion.size()==1) {
		    	FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) vSeriesFacturacion.get(0);
		    	idSerieFacturacion = beanSerieFacturacion.getIdSerieFacturacion().toString();
		    		    
		    } else { // Tiene varias series de facturacion asociadas al producto
		    	
		    	// Indico la primera opcion del seleccionable
		    	String sOptionsSerieFacturacion = "<option value=''>" + UtilidadesString.getMensajeIdioma(usr, "general.combo.seleccionar") + "</option>";
		    	
		    	// Cargo el resto de opciones del seleccionable
		    	for (int i = 0; i < vSeriesFacturacion.size(); i++)	{
		    		FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) vSeriesFacturacion.get(i);
		    		sOptionsSerieFacturacion +="<option value='" +  beanSerieFacturacion.getIdSerieFacturacion() + "'>" + beanSerieFacturacion.getNombreAbreviado() + "</option>";
		    	}
		    	
		    	// Devuelvo la lista de series de facturacion
		    	ArrayList<String> aOptionsSeriesFacturacion = new ArrayList<String>();
		    	aOptionsSeriesFacturacion.add(sOptionsSerieFacturacion);
		    	json.put("aOptionsSeriesFacturacion", aOptionsSeriesFacturacion);
		    }
	    }
		
    	// Deuelve el identificador de la serie de facturacion
		json.put("idSerieFacturacion", idSerieFacturacion);
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 		
	}
	
	public static HashMap<String, Object> obtenerPathBD(GenParametrosAdm admParametros, String idInstitucion, String idSolicitud) throws ClsExceptions {

		HashMap<String, Object> valoresRetornados = new HashMap<String, Object>();

		String sRutaDB = admParametros.getValor(idInstitucion, "CER", "PATH_CERTIFICADOS", "");
		if (sRutaDB == null || sRutaDB.equals("")) {
			throw new ClsExceptions("No se ha encontrado el parámetro PATH_CERTIFICADOS en la BD");
		}
		valoresRetornados.put("sRutaDB", sRutaDB);

		String sRutaPlantillas = admParametros.getValor(idInstitucion, "CER", "PATH_PLANTILLAS", "");
		if (sRutaPlantillas == null || sRutaPlantillas.equals("")) {
			throw new ClsExceptions("No se ha encontrado el parámetro PATH_PLANTILLAS en la BD");
		}

		sRutaPlantillas += File.separator + idInstitucion;
		valoresRetornados.put("sRutaPlantillas", sRutaPlantillas);

		String sAux = idInstitucion + "_" + idSolicitud;

		sRutaDB += File.separator + "tmp";

		File fDirTemp = new File(sRutaDB);
		fDirTemp.mkdirs();

		File fOut = new File(fDirTemp.getPath() + File.separator + sAux + "_" + System.currentTimeMillis() + ".pdf");
		valoresRetornados.put("fOut", fOut);
		File fIn = new File(fOut.getPath() + ".tmp");
		valoresRetornados.put("fIn", fIn);

		fOut.deleteOnExit();
		fIn.deleteOnExit();

		return valoresRetornados;
	}
	
	public static synchronized void obtenerContadorYAprobar ( CerSolicitudCertificadosAdm admSolicitud, CerSolicitudCertificadosBean beanSolicitud, 
			PysProductosInstitucionBean beanProd, Hashtable<String,Object> htNew, String[] claves,String[] campos,UsrBean usr) throws ClsExceptions,SIGAException{

		
		// INICIO BLOQUE SINCRONIZADO 
		UserTransaction tx=null;

		try {
			tx = usr.getTransaction();
			tx.begin();
			GestorContadores gc = new GestorContadores(usr);
	        boolean tieneContador=admSolicitud.tieneContador(String.valueOf(beanSolicitud.getIdInstitucion()),String.valueOf(beanSolicitud.getIdSolicitud()));
			//Obtenemos los datos de la tabla de contadores sin pasarle el prefijo y el sufijo del formulario (datos originales)	
			//String idContador="PYS_"+idTipoProducto+"_"+idProducto+"_"+idProductoInstitucion;
			// obtenemos el contador de la FK del producto
			String idContador=beanProd.getIdContador();
			
			// obteniendo registro de contador de BD
			Hashtable<String,Object> contadorTablaHash = gc.getContador(new Integer(beanSolicitud.getIdInstitucion()), idContador);

			if (!tieneContador && contadorTablaHash.get("MODO").toString().equals("0")) {
				// MODO REGISTRO. Se suponen siempre asi estos

				int numContador;
				numContador = new Integer((contadorTablaHash.get("CONTADOR").toString())).intValue();
				// En la tabla contador se guarda el ultimo nº utilizado por lo que el siguiente a utilizar el contador + 1

				numContador = numContador + 1;
				// comprobando la unicidad de este contador junto con el prefijo y sufijo guardado en la hash contador
				while (gc.comprobarUnicidadContadorProdCertif(numContador, contadorTablaHash,String.valueOf(beanSolicitud.getPpn_IdTipoProducto()),
						String.valueOf(beanSolicitud.getPpn_IdProducto()),String.valueOf(beanSolicitud.getPpn_IdProductoInstitucion() ))) {
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
				
				beanSolicitud.setPrefijoCer((String) contadorTablaHash.get("PREFIJO"));
				beanSolicitud.setSufijoCer((String) contadorTablaHash.get("SUFIJO"));
				beanSolicitud.setContadorCer(contadorFinalSugerido);
			}else{
				//si tiene contador hay que setear el contador final sugerido
				Integer longitud = new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));
				int longitudContador = longitud.intValue();
				
				String contadorFinalSugerido = UtilidadesString.formatea(contadorTablaHash.get("CONTADOR").toString(), longitudContador, true);
				beanSolicitud.setContadorCer(contadorFinalSugerido);
				
				
			}
			//aalg: se añade la carga en base de datos dentro de la sincronización para evitar la pérdida de número de certificado
			//inc_10359_siga
			
		      htNew.put(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBADO);    
		      htNew.put(CerSolicitudCertificadosBean.C_FECHAESTADO,"sysdate");
		        

	        // RGG 15/10/2007 CAMBIO PARA ACTUALIZAR LA FECHA DE EMISION SOLAMENTE SI ESTA A NULA  
	        if (beanSolicitud.getFechaEmisionCertificado() == null || beanSolicitud.getFechaEmisionCertificado().equals("")) {
	            htNew.put(CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,"sysdate");
	        }
	        
		  	htNew.put(CerSolicitudCertificadosBean.C_FECHAMODIFICACION,"sysdate");
		    
	        if (!admSolicitud.updateDirect(htNew, claves, campos ))
		    {
	        	tx.rollback();
		        throw new ClsExceptions("Error al aprobar la solicitud. Inténtelo de nuevo. Si el problema persiste, contacte con el Administrador");
		    }
	        tx.commit(); 

		} catch (Exception e) {
			try {
				tx.rollback();
				throw new SIGAException("Error al obtener el contador para el certificado. Si ha cambiado recientemente el contador de los certificados (por ejemplo, es de los primeros del año" +
						" o es un nuevo tipo de certificado), compruebe el contador en Administración> Gestión de parámetros> Contadores e inténtelo de nuevo. Si el problema persiste, contacte con el Administrador.");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		
			}
		}
		//FIN BLOQUE SINCRONIZADO 
		
	}
	/**
	 * Método encargado de realizar el proceso de Generación del certificado.
	 * @param beanSolicitud
	 * @param listaDeObjetos
	 * @param idPlantilla
	 * @param usarIdInstitucion
	 * @param usr
	 * @param admCert
	 * @param htNew
	 * @param admSolicitud
	 * @param claves
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public static void ProcesoCompletoDeGeneracion(CerSolicitudCertificadosBean beanSolicitud,HashMap<String, Object> listaDeObjetos, 
			String idPlantilla,boolean usarIdInstitucion,UsrBean usr,CerSolicitudCertificadosAdm admCert, Hashtable<String,Object> htNew,  
			CerSolicitudCertificadosAdm admSolicitud,String[] claves) throws SIGAException, ClsExceptions{
		 
		CerSolicitudCertificadosService  cerSolicitudCertificadosService = (CerSolicitudCertificadosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosService.class);
		List <CerSolicitudcertificados> listaCerSolicitudcertificados = new ArrayList<CerSolicitudcertificados>();
		CerSolicitudcertificados cerSolicitudcertificados = new CerSolicitudcertificados();
		
		////////////////////////////////////////////////////////
	    // GENERAR CERTIFICADO
        ////////////////////////////////////////////////////////
		
    	Certificado.generarCertificadoPDF(String.valueOf(beanSolicitud.getPpn_IdTipoProducto()),String.valueOf(beanSolicitud.getPpn_IdProducto()),String.valueOf(beanSolicitud.getPpn_IdProductoInstitucion()) , 
    			String.valueOf(beanSolicitud.getIdInstitucion()), idPlantilla,String.valueOf(beanSolicitud.getIdPersona_Des()),(File)listaDeObjetos.get("fIn"),(File)listaDeObjetos.get("fOut") , 
                						 (String)listaDeObjetos.get("sRutaPlantillas"),String.valueOf(beanSolicitud.getIdSolicitud()),String.valueOf(beanSolicitud.getIdInstitucionOrigen()),usarIdInstitucion,usr);
    	
    	
    	
    	admCert.guardarCertificado(beanSolicitud, (File)listaDeObjetos.get("fOut"));

    	((File)listaDeObjetos.get("fOut")).delete();

        // FIRMAR CERTIFICADO
	    Boolean firmaUnCertificado = admCert.firmarPDF(String.valueOf(beanSolicitud.getIdSolicitud()), String.valueOf(beanSolicitud.getIdInstitucion()),-1);
        
	    //OBTENEMOS LAS DEMÁS PLANTILLAS EN EL CASO DE QUE EXISTAN
         CerPlantillasAdm admPlantilla = new CerPlantillasAdm(usr);
		 
		 Vector plantillas = admPlantilla.obtenerListaRelacionesPlantillas(String.valueOf(beanSolicitud.getIdInstitucion()), String.valueOf(beanSolicitud.getPpn_IdTipoProducto()), String.valueOf(beanSolicitud.getPpn_IdProducto()),
				 String.valueOf(beanSolicitud.getPpn_IdProductoInstitucion()), idPlantilla);
		 boolean firmaCorrecta = Boolean.TRUE; 
		 if(plantillas !=null && plantillas.size()>0 ){
			 for(int i = 0; i<plantillas.size();i++){
			   	if(firmaCorrecta){
					CerPlantillasBean htDatos = (CerPlantillasBean)plantillas.elementAt(i);
				   	Certificado.generarCertificadoPDF(String.valueOf(beanSolicitud.getPpn_IdTipoProducto()),String.valueOf(beanSolicitud.getPpn_IdProducto()),String.valueOf(beanSolicitud.getPpn_IdProductoInstitucion()) , 
			    			String.valueOf(beanSolicitud.getIdInstitucion()),String.valueOf(htDatos.getIdPlantilla()),String.valueOf(beanSolicitud.getIdPersona_Des()),(File)listaDeObjetos.get("fIn"),(File)listaDeObjetos.get("fOut") , 
   						 (String)listaDeObjetos.get("sRutaPlantillas"),String.valueOf(beanSolicitud.getIdSolicitud()),String.valueOf(beanSolicitud.getIdInstitucionOrigen()),usarIdInstitucion,usr);
				   	
				   	
				   	admCert.guardarVariosCertificado(beanSolicitud, (File)listaDeObjetos.get("fOut"),htDatos.getIdPlantilla());
				   	firmaCorrecta = admCert.firmarPDF(String.valueOf(beanSolicitud.getIdSolicitud()), String.valueOf(beanSolicitud.getIdInstitucion()),htDatos.getIdPlantilla());
				   	((File)listaDeObjetos.get("fOut")).delete();
			   	}
			   	
			   
			 }
	      }
	      
		  htNew.put(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_CER_GENERADO);
			 
	      if (!(firmaUnCertificado && firmaCorrecta)) {
		        ClsLogging.writeFileLog("Error al FIRMAR el PDF de la Solicitud: " + beanSolicitud.getIdSolicitud(), 3);
		    } else {
		    	
		        htNew.put(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, CerSolicitudCertificadosAdm.K_ESTADO_CER_FIRMADO);
		    }
		    
	        String[] campos2 = {CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO};
		    
	        if (!admSolicitud.updateDirect(htNew, claves, campos2 ))
		    {
		        throw new ClsExceptions("Error al GENERAR el/los PDF/s. Compruebe la plantilla seleccionada para generar los PDFs.");
		    }else{
		    	
		    	// Si no se produce error cambiamos el estado de las solicitud
		    	  cerSolicitudcertificados = new CerSolicitudcertificados();
	        	  cerSolicitudcertificados.setIdinstitucion(beanSolicitud.getIdInstitucion().shortValue());
			      cerSolicitudcertificados.setIdsolicitud(Long.valueOf(beanSolicitud.getIdSolicitud()));
			      cerSolicitudcertificados.setUsucreacion(new Integer(usr.getUserName()));
			        
			      listaCerSolicitudcertificados.add(cerSolicitudcertificados);
			        
			     //Cambiamos el estado
			      cerSolicitudCertificadosService.updateEstadosGeneracion(listaCerSolicitudcertificados);
		    	
		    }
	}
	
	public static HashMap<String, Object> obtenerPersonaCertificado(UsrBean usr,String idInstitucion,String idTipoProducto, String idProducto, String idProductoInstitucion, String idSolicitud) throws ClsExceptions{
		
		HashMap<String, Object> resultado = new HashMap<String, Object> ();
		// RGG 28/03/2007 CAMBIO FINAL PARA OBTENER LA PERSONA DEL CERTIFICADO
        boolean usarIdInstitucion = false;
		boolean colegiadoEnOrigen = true;
		CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(usr);
		PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(usr);
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
		resultado.put("usarIdInstitucion", usarIdInstitucion);
		resultado.put("colegiadoEnOrigen", colegiadoEnOrigen);
		resultado.put("beanProd", beanProd);
		return resultado;
		
	}
	
	
	/**
	 * Descarga el fichero de Log.
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String descargarLog(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		UserTransaction tx = null;
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CerSolicitudCertificadosAdm admCert = new CerSolicitudCertificadosAdm(usr);
			 
			SIGASolicitudesCertificadosForm form = (SIGASolicitudesCertificadosForm) formulario;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			Vector vOcultos = form.getDatosTablaOcultos(0);

			String idInstitucion = ((String) vOcultos.elementAt(0)).trim();
			String idSolicitud = ((String) vOcultos.elementAt(1)).trim();

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

			Vector vDatos = admSolicitud.selectByPK(htSolicitud);

			CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

			String sFicheroLog = admCert.obtenerRutaLogError(solicitudCertificadoBean);
					sFicheroLog+=	File.separator+idSolicitud+"-LogError.log.xls";
			File fichero = new File(sFicheroLog);
			if (fichero == null || !fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());

			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, tx);
		}

		return "descargaFichero";
	}
	
	   protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
	    	try{
	    		MasterForm miForm = (MasterForm) formulario;
	    		String accion = miForm.getModo();
	    		request.setAttribute("modo", accion);
	    		request.setAttribute("modificarSolicitud", "1");
	    		request.setAttribute("pintarCheckMutualidad", true);
	    		request.setAttribute("facturable", false);
	    		request.setAttribute("idEstadoSolicitud", CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND);
	    
	    	} catch (Exception e) { 
	    		throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,null); 
	    	}
	    	
	    	return "mostrar";
	    }
	    
	    /**
		 * @param request
		 * @param response
		 */
		private void getAjaxFacturaAsociada(HttpServletRequest request, HttpServletResponse response) throws Exception {	
			String idInstitucion = request.getParameter("idInstitucion");
			if (idInstitucion==null || idInstitucion.trim().equalsIgnoreCase(""))
				throw new SIGAException("Falta el identificador de la institución");
			
			String idSolicitud = request.getParameter("idSolicitud");
			if (idSolicitud==null || idSolicitud.trim().equalsIgnoreCase(""))
				throw new SIGAException("Falta el identificador de la solicitud");
			
			JSONObject json = new JSONObject();
			UsrBean usr = this.getUserBean(request);
			String idFactura = "";	
			
			// Obtengo la peticion de compra
	    	PysCompraAdm admCompra = new PysCompraAdm(usr);
		    PysCompraBean beanCompra = admCompra.obtenerCompraCertificado(idInstitucion, idSolicitud);
		    
		    // Compruebo si esta facturada la compra
		    if (beanCompra.getIdFactura()!=null && !beanCompra.getIdFactura().trim().equals("")) {	
		    	idFactura = beanCompra.getIdFactura(); 
		    } 
			
	    	// Deuelve el idFactura asociada
			json.put("idFactura", idFactura);
			
			// json.
			response.setContentType("text/x-json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
		    response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 		
		}    	
	
	
}