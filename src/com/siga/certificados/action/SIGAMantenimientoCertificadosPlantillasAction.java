package com.siga.certificados.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.FileHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CerPlantillasAdm;
import com.siga.beans.CerPlantillasBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenParametrosBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.certificados.form.SIGAMantenimientoCertificadosPlantillasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGAMantenimientoCertificadosPlantillasAction extends MasterAction
{
    private String sContentTypeZIP="application/x-zip-compressed";
    
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
	            } else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("modosRelacionPlantillas")){
	            	SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)miForm;
	            	if(form.getNuevo().equalsIgnoreCase("1"))
	            		mapDestino = relacionPlantillas(mapping, miForm, request, response,true, true);
	            	else
	            		mapDestino = relacionPlantillas(mapping, miForm, request, response,true, false);
	            	
	            } else if (accion.equalsIgnoreCase("download"))
	            {
	                mapDestino = download(mapping, miForm, request, response);
	            }else if(accion.equalsIgnoreCase("borrarRelacion"))
	            {
	            	mapDestino = borrarRelacion(mapping, miForm, request, response);
	            } else if(accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxSeleccionPlantillas"))
	            {
	            	getAjaxSeleccionPlantillas(request, response);	     
					return null;
	            }

	            else 
	            {
	                return super.executeInternal(mapping,formulario,request,response);
	            }
	        }

    	    // Redireccionamos el flujo a la JSP correspondiente
    	    if (mapDestino == null)	
    	    { 
    	        throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
    	    }

   	        return mapping.findForward(mapDestino);
    	} 
    	catch (Exception e) 
    	{ 
    	    throw new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
    	} 
	}

	/**
	 * Obtiene el listado de plantillas que se pueden asociar
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxSeleccionPlantillas (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		
		JSONObject json = new JSONObject();
		UsrBean usr = this.getUserBean(request);
		
		String idPlantilla = request.getParameter("idPlantilla");
		String idInstitucion = request.getParameter("idInstitucion");
		String idTipoProducto = request.getParameter("idTipoProducto");
		String idProducto = request.getParameter("idProducto");
		String idProductoInstitucion = request.getParameter("idProductoInstitucion");
		
		 CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.getUserBean(request));
	        Vector vDatos = admPlantilla.obtenerComboRelacionesPlantillas(idInstitucion, idTipoProducto, 
	        		idProducto, idProductoInstitucion,idPlantilla);
	      
	     // Indico la primera opcion del seleccionable
	    	String sOptionsComboPlantilla = "<option value=''>" + UtilidadesString.getMensajeIdioma(usr, "general.combo.seleccionar") + "</option>";
	    	
	    	for (int i = 0; i < vDatos.size(); i++)	{
	    		CerPlantillasBean htDatos = (CerPlantillasBean)vDatos.elementAt(i);
	    		
	    		
	    		sOptionsComboPlantilla +="<option value='" + htDatos.getIdPlantilla() + "'>" +htDatos.getDescripcion()+ "</option>";
	    	}
	    	
	    	// Devuelvo la lista de series de facturacion
	    	ArrayList<String> aOptionsComboFacturacion = new ArrayList<String>();
	    	aOptionsComboFacturacion.add(sOptionsComboPlantilla);
	    	json.put("aOptionsSeriesFacturacion", aOptionsComboFacturacion);
	    	
	    	Integer valor = Integer.parseInt(idPlantilla);
			request.getSession().setAttribute("idPlantilla", valor);
	    	
	    	// json.
			response.setContentType("text/x-json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
		    response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 

	}
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		File fPlantilla = null;
		

		try {
			SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm) formulario;
			
			String idInstitucion = null;
			String idTipoProducto = null;
			String idProducto = null;
			String idProductoInstitucion = null;
			String idPlantilla = null;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			
			if(vOcultos != null ){
				idInstitucion = (String) vOcultos.elementAt(0);
				idTipoProducto = (String) vOcultos.elementAt(1);
				idProducto = (String) vOcultos.elementAt(2);
				idProductoInstitucion = (String) vOcultos.elementAt(3);
				idPlantilla = (String) vOcultos.elementAt(4);
			}else{
				//Descarga de la pantalla plantillas relacionadas
				idPlantilla = request.getParameter("idPlantilla");
				idInstitucion = request.getParameter("idInstitucion");
				idTipoProducto = request.getParameter("idTipoProducto");
				idProducto = request.getParameter("idProducto");
				idProductoInstitucion = request.getParameter("idProductoInstitucion");
			}

			CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
			fPlantilla = admPlantilla.descargarPlantilla(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla);

			String nombreFichero = fPlantilla.getName();

			StringBuffer rutaCompletaFicheroDoc = new StringBuffer(fPlantilla.getPath());
			rutaCompletaFicheroDoc.append(".doc");
			File fileDoc = new File(rutaCompletaFicheroDoc.toString());
			if (fileDoc.exists()) {
				nombreFichero = nombreFichero + ".doc";
				request.setAttribute("nombreFichero", nombreFichero);
				request.setAttribute("rutaFichero", fileDoc.getPath());
			} else {
				if (fPlantilla.getName().indexOf(".zip") == -1) {
					nombreFichero = nombreFichero + ".fo";
				} else {
					nombreFichero = fPlantilla.getName();
				}
				request.setAttribute("nombreFichero", nombreFichero);
				request.setAttribute("rutaFichero", fPlantilla.getPath());
			}

		} catch (Exception e) {
			  request.setAttribute("mensaje","messages.certificados.error.descargarplantilla");
			  return "exito";
		}

		return "descargaFichero";
	}
	
	protected String borrarRelacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		
		SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
		String forward = null;
		String sMensaje = null;
        
        CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.getUserBean(request));
        
        String sIdInstitucion = form.getIdInstitucion();
        String sIdTipoProducto = form.getIdTipoProducto();
        String sIdProducto = form.getIdProducto();
        String sIdProductoInstitucion = form.getIdProductoInstitucion();
        String sIdPlantilla = form.getIdPlantilla();  //Id plantilla hijo
        String aIdPlantillaRelacion = form.getIdRelacion();  //IdPlantilla padre
        
        if (admPlantilla.borrarRelacionPlantilla(sIdInstitucion, sIdTipoProducto, sIdProducto, sIdProductoInstitucion, sIdPlantilla,aIdPlantillaRelacion))
        {
        	Integer valor = Integer.parseInt(aIdPlantillaRelacion);
			request.getSession().setAttribute("idPlantilla", valor);
        	sMensaje="messages.deleted.success";
            forward = exitoRefresco(sMensaje,request);
        }
        
        else
        {
            throw new SIGAException("error.messages.deleted");
        }

        return forward;
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
	    CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));

	    String idInstitucion = form.getIdInstitucion();
	    String idTipoProducto = form.getIdTipoProducto();
	    String idProducto = form.getIdProducto();
	    String idProductoInstitucion = form.getIdProductoInstitucion();
	    
	    String sCertificado = form.getCertificado();
	    String sEditable = form.getEditable();
	    
	    String descCertificado = form.getDescripcionCertificado();
	    
	    Vector vDatos = admPlantilla.obtenerListaPlantillas(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion);
	    
	    request.setAttribute("idInstitucion", idInstitucion);
	    request.setAttribute("idTipoProducto", idTipoProducto);
	    request.setAttribute("idProducto", idProducto);
	    request.setAttribute("idProductoInstitucion", idProductoInstitucion);
	    
	    request.setAttribute("certificado", sCertificado);
	    request.setAttribute("editable", sEditable);
	    
	    request.setAttribute("descripcionCertificado", descCertificado);
	    
	    request.setAttribute("datos", vDatos);
	    
		return "abrir";
	}
	protected String relacionPlantillas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
		
	    Vector datos = new Vector();
	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idInstitucion", request.getParameter("idInstitucion"));
	    htDatos.put("idTipoProducto",  request.getParameter("idTipoProducto"));
	    htDatos.put("idProducto",  request.getParameter("idProducto"));
	    htDatos.put("idProductoInstitucion",  request.getParameter("idProductoInstitucion"));
	    htDatos.put("idPlantilla",  request.getParameter("idPlantilla"));
	    htDatos.put("descripcion", request.getParameter("descripcion"));
	    htDatos.put("porDefecto",  request.getParameter("porDefecto"));
	    htDatos.put("editable", bEditable ? "1" : "0");
	    htDatos.put("nuevo", bNuevo ? "1" : "0");
	    
	    datos.add(htDatos);
	    
        request.setAttribute("datos", datos);
	        
        CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.getUserBean(request));
       //Si es la parte de editar
      if(!bNuevo){  
	        Vector comprobarSiTieneHijo = admPlantilla.comprobarSiEsHijo(request.getParameter("idInstitucion"), request.getParameter("idTipoProducto"), 
	        		request.getParameter("idProducto"),  request.getParameter("idProductoInstitucion"),request.getParameter("idPlantilla"));
				  
	        if(comprobarSiTieneHijo.size() != 0){
					  //No se puede asociar ya que es hijo y seria hacer un bucle de plantillas
					  request.setAttribute("asociacion", "NO");
			  }else{
				  //Plantillas asociadas
				  Vector vDatosRelacionesPlantillas = admPlantilla.obtenerListaRelacionesPlantillas(request.getParameter("idInstitucion"), request.getParameter("idTipoProducto"), 
			        		request.getParameter("idProducto"),  request.getParameter("idProductoInstitucion"),request.getParameter("idPlantilla"));
				  
			        request.setAttribute("datosPlantillasRelaciondas", vDatosRelacionesPlantillas);
				
				  request.setAttribute("asociacion", "SI");
			  }
      }
        
      
        
		return "mostrar";
	}
	
	
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(this.getUserBean(request));

        String certificado = form.getCertificado();
        String institucion = userBean.getLocation();

        String where = " WHERE ";
        
        where += PysProductosInstitucionBean.C_TIPOCERTIFICADO + " IN ('" + PysProductosInstitucionBean.PI_COMUNICACION_CODIGO + "','" +
        																   PysProductosInstitucionBean.PI_DILIGENCIA_CODIGO + "','" +
        																   PysProductosInstitucionBean.PI_CERTIFICADO_CODIGO + "')";
        where += " AND " + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + institucion;
        
        where += (certificado!=null && !certificado.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(certificado.trim(),PysProductosInstitucionBean.C_DESCRIPCION):"";
  

        Vector datos = productoAdm.select(where);
        
        request.setAttribute("datos", datos);
        
        return "buscar";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, false, true);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false, false);
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, true, true);
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    return grabar(mapping, formulario, request, response);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
	    return grabar(mapping, formulario, request, response);
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UsrBean user = null;
		UserTransaction tx = null;
	    SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
		user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		tx = user.getTransaction();		
        
        CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.getUserBean(request));
        
        Vector vOcultos = form.getDatosTablaOcultos(0);
        
        String sIdInstitucion = form.getIdInstitucion();
        String sIdTipoProducto = form.getIdTipoProducto();
        String sIdProducto = form.getIdProducto();
        String sIdProductoInstitucion = form.getIdProductoInstitucion();
        String sIdPlantilla = ((String)vOcultos.elementAt(4)).trim();
        
        //Compruebo antes de borrar si la plantilla es hija de alguna del sistema, si es hija habrá que borrar esa relación
        //Si no es hija habrá que mirar si es padre para borrar las relaciones que tiene como padre y sino tiene ninguna relación se elimina sin más.
        try{
	        tx.begin();	
	        if (admPlantilla.borrarHijosOPadres(sIdInstitucion, sIdTipoProducto, sIdProducto, sIdProductoInstitucion, sIdPlantilla) && admPlantilla.borrarPlantilla(sIdInstitucion, sIdTipoProducto, sIdProducto, sIdProductoInstitucion, sIdPlantilla))
	        {
	        	tx.commit();
	            request.setAttribute("mensaje","messages.deleted.success");
	        }
	        
	        else
	        {
	        	tx.rollback();
	            request.setAttribute("mensaje","error.messages.deleted");
	        }
        } catch(Exception e)
	    {
			ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() ERROR " + e.toString(),10);

	    	throwExcp("messages.certificados.error.importarplantilla", e, tx);
	    }
        return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
	    SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
	    
		String sIdInstitucion=form.getIdInstitucion();
		String sIdTipoProducto=form.getIdTipoProducto();
		String sIdProducto=form.getIdProducto();
		String sIdProductoInstitucion=form.getIdProductoInstitucion();
		
		String sIdPlantilla="";
		String sDescripcion="";
		String sPorDefecto="";
		
		if (!bNuevo)
		{
			Vector vOcultos = form.getDatosTablaOcultos(0);
			
			sIdPlantilla=(String)vOcultos.elementAt(4);
			sDescripcion=(String)vOcultos.elementAt(5);
			sPorDefecto=(String)vOcultos.elementAt(6);
		}

	    Vector datos = new Vector();
	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idInstitucion", sIdInstitucion);
	    htDatos.put("idTipoProducto", sIdTipoProducto);
	    htDatos.put("idProducto", sIdProducto);
	    htDatos.put("idProductoInstitucion", sIdProductoInstitucion);
	    htDatos.put("idPlantilla", sIdPlantilla);
	    htDatos.put("descripcion", sDescripcion);
	    htDatos.put("porDefecto", sPorDefecto);
	    
	    datos.add(htDatos);
	    
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(CerPlantillasBean.C_IDINSTITUCION, sIdInstitucion);
            hashBackUp.put(CerPlantillasBean.C_IDTIPOPRODUCTO, sIdTipoProducto);
            hashBackUp.put(CerPlantillasBean.C_IDPRODUCTO, sIdProducto);
            hashBackUp.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, sIdProductoInstitucion);
            hashBackUp.put(CerPlantillasBean.C_IDPLANTILLA, sIdPlantilla);
            hashBackUp.put(CerPlantillasBean.C_DESCRIPCION, sDescripcion);
            hashBackUp.put(CerPlantillasBean.C_PORDEFECTO, sPorDefecto);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }
       
	    
		return "mostrar";
	}
	
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo,boolean param) throws ClsExceptions
	{
		
	    SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
	    
		String sIdInstitucion=form.getIdInstitucion();
		String sIdTipoProducto=form.getIdTipoProducto();
		String sIdProducto=form.getIdProducto();
		String sIdProductoInstitucion=form.getIdProductoInstitucion();
		String sCertificado=form.getCertificado();
		
		String sIdPlantilla="";
		String sDescripcion="";
		String sPorDefecto="";
		
		if (!bNuevo)
		{
			Vector vOcultos = form.getDatosTablaOcultos(0);
			if(vOcultos != null){
				sIdPlantilla=(String)vOcultos.elementAt(4);
				sDescripcion=(String)vOcultos.elementAt(5);
				sPorDefecto=(String)vOcultos.elementAt(6);
			}else{
				//Es recarga de dar de alta un nuevo elemento y pasa al modo editar, la plantilla viene en session
				if(request.getSession().getAttribute("idPlantilla") != null){
					Integer sIdPlantillaAux=(Integer) request.getSession().getAttribute("idPlantilla");
					request.getSession().removeAttribute("idPlantilla");
					sIdPlantilla=String.valueOf(sIdPlantillaAux);
				}else{
					sIdPlantilla = form.getIdRelacion();
				}
				sDescripcion=form.getDescripcion();
				if(form.getPorDefecto().equalsIgnoreCase("1")){
					sPorDefecto="S";
				}else{
					sPorDefecto="N";
				}
			}
		}
		

	    Vector datos = new Vector();
	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idInstitucion", sIdInstitucion);
	    htDatos.put("idTipoProducto", sIdTipoProducto);
	    htDatos.put("idProducto", sIdProducto);
	    htDatos.put("idProductoInstitucion", sIdProductoInstitucion);
	    htDatos.put("idPlantilla", sIdPlantilla);
	    htDatos.put("descripcion", sDescripcion);
	    htDatos.put("porDefecto", sPorDefecto);
	    htDatos.put("certificado", sCertificado);
	    htDatos.put("editable", bEditable ? "1" : "0");
	    htDatos.put("nuevo", bNuevo ? "1" : "0");
	    
	    
	    datos.add(htDatos);
	    
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(CerPlantillasBean.C_IDINSTITUCION, sIdInstitucion);
            hashBackUp.put(CerPlantillasBean.C_IDTIPOPRODUCTO, sIdTipoProducto);
            hashBackUp.put(CerPlantillasBean.C_IDPRODUCTO, sIdProducto);
            hashBackUp.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, sIdProductoInstitucion);
            hashBackUp.put(CerPlantillasBean.C_IDPLANTILLA, sIdPlantilla);
            hashBackUp.put(CerPlantillasBean.C_DESCRIPCION, sDescripcion);
            hashBackUp.put(CerPlantillasBean.C_PORDEFECTO, sPorDefecto);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }
             
        request.setAttribute("hashRelacionPlantillas",htDatos);
		request.setAttribute("modosRelacionPlantillas", form.getModos());	
	   
		if(form.getRecarga() != null && form.getRecarga().equalsIgnoreCase("SI")){
			  CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.getUserBean(request));
		        if(form.getRelacion() != null && form.getRelacion().equalsIgnoreCase("SI"))
		        {
		        	admPlantilla.insertarRelacionPlantillas(form.getIdRelacion(), sIdPlantilla, 
		        			sIdInstitucion, sIdTipoProducto, sIdProducto, sIdProductoInstitucion);
		        	
		        }
		        Vector vDatosRelacionesPlantillas = admPlantilla.obtenerListaRelacionesPlantillas(sIdInstitucion, sIdTipoProducto, 
		        		sIdProducto,  sIdProductoInstitucion,sIdPlantilla);
		        
		        request.setAttribute("datosPlantillasRelaciondas", vDatosRelacionesPlantillas);
			return "mostrar";
		}else{
			return "mostrarPestanas";
		}
		
	}
	
	protected String grabar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    String sMensaje = "messages.updated.error";
	    String forward = null;
	    UsrBean user = null;
	    UserTransaction tx = null;
	    
	    try
	    {
	    	user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = user.getTransaction();			
	    		
			ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() ",10);
			
		    SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;

		    FormFile theFile = form.getTheFile();
		    File fPlantilla=null;
		    String extension =  "";
		    boolean bZIP = false;
		    if(theFile!=null &&  theFile.getFileName()!=null && !theFile.getFileName().equals("")){
		   
			    String[] nombreFicheroStrings = theFile.getFileName().split("\\.");
			    
			    
			    if(nombreFicheroStrings!=null && nombreFicheroStrings.length>0){
			    	extension = nombreFicheroStrings[1];
			    }
				String sNombreFicheroTemporal="";
				
				bZIP=theFile.getContentType().equals(sContentTypeZIP);
		
			    if(theFile==null || theFile.getFileSize()<1){ 
	//		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
					ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() FICHERO VACIO ",10);
	
			    }else{
			    	InputStream stream =null;
			    	String sNombrePlantilla = form.getIdTipoProducto() + "_" + form.getIdProducto() + "_" + form.getIdProductoInstitucion();
			    	String sDirectorio = getPathPlantillasFromDB(this.getUserBean(request)) + File.separator + form.getIdInstitucion();
			    	
			    	FileHelper.mkdirs(sDirectorio);
			    	
			    	OutputStream bos = null;
			    	try 
			    	{			
			    		stream = theFile.getInputStream();
			    		sNombreFicheroTemporal=sDirectorio + File.separator + "#" + sNombrePlantilla + "_" + (new Date()).getTime() + ".tmp";
						ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() sNombreFicheroTemporal="+sNombreFicheroTemporal,10);
	
			    		bos = new FileOutputStream(sNombreFicheroTemporal);
			    		int bytesRead = 0;
			    		byte[] buffer = new byte[8192];
		
			    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) 
			    		{
			    			bos.write(buffer, 0, bytesRead);
			    		}
			    		
			    		fPlantilla = new File(sNombreFicheroTemporal);
			    		fPlantilla.deleteOnExit();
			    	} 
			    	
			    	catch (Exception e) 
			    	{
			    		throw new ClsExceptions(e,"Error al copiar la plantilla del certificado.");
			    	    //request.setAttribute("mensaje","messages.updated.error");
			    	}
			    	finally {
			    		bos.close();
			    		stream.close();
			    	}
			    }
		    }
	        CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
	
	        String descripcionPlantilla=form.getDescripcion();
	        String idInstitucion=form.getIdInstitucion();
	        String idTipoProducto=form.getIdTipoProducto();
	        String idProducto=form.getIdProducto();
	        String idProductoInstitucion=form.getIdProductoInstitucion();
	        String idPlantilla=form.getIdPlantilla();
	        
	        Hashtable htDatos = new Hashtable();
	    	
		    htDatos.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucion);
		    htDatos.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htDatos.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htDatos.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
		    
	        boolean bPorDefecto = (form.getPorDefecto()!=null && form.getPorDefecto().equals("1")) ? true : false;
	
	        ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() COMIENZO DE IMPORTAR PLANTILLA ",10);

	        tx.begin();	        
	        if (admPlantilla.importarPlantilla(descripcionPlantilla, idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, fPlantilla, bPorDefecto, bZIP,extension))
	        {
	        	ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() OK IMPORTAR PLANTILLA",10);

	            sMensaje="messages.updated.success";
	            tx.commit();
	            //Si es editar
	            if(idPlantilla != null && !"".equalsIgnoreCase(idPlantilla)){
	            	forward = exito(sMensaje,request);
	            }else{ //Si es nuevo 
	            	// CGP: Guardo el nuevo identificador de la plantilla
	            	Integer valor = Integer.parseInt(admPlantilla.getNuevoID(htDatos));
	    			request.getSession().setAttribute("idPlantilla", (valor-1));
	            	forward = exitoRefresco(sMensaje,request);
	            }
	        } else{
	        	ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() ERROR IMPORTAR PLANTILLA",10);

	        	throw new SIGAException("messages.certificados.error.importarplantilla");
	        	//tx.rollback();
	        	//sMensaje="messages.certificados.error.importarplantilla";
	        	//forward = exitoModalSinRefresco(sMensaje,request);
	        }
	    }
	    
	    catch(Exception e)
	    {
			ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() ERROR " + e.toString(),10);

	    	throwExcp("messages.certificados.error.importarplantilla", e, tx);
	    }
	    
	    return forward;
	}
	
	private String getPathPlantillasFromDB(UsrBean usr) throws SIGAException
	{
	    String sPath="";
	    
	    try
	    {
	        String sWhere = " WHERE " + GenParametrosBean.C_PARAMETRO + "='PATH_PLANTILLAS' AND "+
	                                    GenParametrosBean.C_MODULO + "='CER' AND " + 
	                                    GenParametrosBean.C_IDINSTITUCION + "=0";
	        
	        GenParametrosAdm admParametros = new GenParametrosAdm(usr);
	        Vector vParametros = admParametros.select(sWhere);
	        
	        if (vParametros==null || vParametros.size()==0)
	        {
	            throwExcp("messages.certificados.error.importarplantilla", null, null);
	        }

	        GenParametrosBean beanParametros = (GenParametrosBean)vParametros.elementAt(0);
	        sPath = beanParametros.getValor();
	    }
	    
	    catch(Exception e)
	    {
	        throwExcp("messages.certificados.error.importarplantilla", e, null);
	    }
	    
	    return sPath;
	}
}