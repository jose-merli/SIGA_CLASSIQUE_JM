package com.siga.certificados.action;

import java.io.*;
import java.util.*;
import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import com.siga.certificados.form.*;

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
	            } 

	            else if (accion.equalsIgnoreCase("download"))
	            {
	                mapDestino = download(mapping, miForm, request, response);
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

	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
	    File fPlantilla = null;
	    
		try 
		{
			SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;

			Vector vOcultos = form.getDatosTablaOcultos(0);

			String idInstitucion = (String)vOcultos.elementAt(0);
			String idTipoProducto = (String)vOcultos.elementAt(1);
			String idProducto = (String)vOcultos.elementAt(2);
			String idProductoInstitucion = (String)vOcultos.elementAt(3);
			String idPlantilla = (String)vOcultos.elementAt(4);
			
	    	//String sNombrePlantilla = idTipoProducto + "_" + idProducto + "_" + idProductoInstitucion + "_" + idPlantilla + ".zip";
	    	//String sRutaPlantilla = getPathPlantillasFromDB() + File.separator + form.getIdInstitucion();
			//String sNombreFichero=sRutaPlantilla + File.separator + sNombrePlantilla;
			
			CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
			fPlantilla = admPlantilla.descargarPlantilla(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla);
			
			/*Ya se comprueba en descargarPlantilla
			 * if(fPlantilla==null || !fPlantilla.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}*/

			String nombreFichero = fPlantilla.getName();
			if (fPlantilla.getName().indexOf(".zip")==-1){
			    nombreFichero = nombreFichero + ".fo";
			}else{
				nombreFichero = fPlantilla.getName();
			}
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", fPlantilla.getPath());

		}
		catch (Exception e) 
		{ 
		  	throwExcp("messages.certificados.error.descargarplantilla", null, null); 
		}
		
		
		return "descargaFichero";
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
	    return mostrarRegistro(mapping, formulario, request, response, true, false);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false, false);
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, true);
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
	    SIGAMantenimientoCertificadosPlantillasForm form = (SIGAMantenimientoCertificadosPlantillasForm)formulario;
        
        CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.getUserBean(request));
        
        Vector vOcultos = form.getDatosTablaOcultos(0);
        
        String sIdInstitucion = form.getIdInstitucion();
        String sIdTipoProducto = form.getIdTipoProducto();
        String sIdProducto = form.getIdProducto();
        String sIdProductoInstitucion = form.getIdProductoInstitucion();
        String sIdPlantilla = ((String)vOcultos.elementAt(4)).trim();
        
        if (admPlantilla.borrarPlantilla(sIdInstitucion, sIdTipoProducto, sIdProducto, sIdProductoInstitucion, sIdPlantilla))
        {
            request.setAttribute("mensaje","messages.deleted.success");
        }
        
        else
        {
            request.setAttribute("mensaje","error.messages.deleted");
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

			String sNombreFicheroTemporal="";
			File fPlantilla=null;
			boolean bZIP=theFile.getContentType().equals(sContentTypeZIP);
	
		    if(theFile==null || theFile.getFileSize()<1){ 
//		    	throw new SIGAException("messages.general.error.ficheroNoExiste");
				ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() FICHERO VACIO ",10);

		    }else{
		    	InputStream stream =null;
		    	String sNombrePlantilla = form.getIdTipoProducto() + "_" + form.getIdProducto() + "_" + form.getIdProductoInstitucion();
		    	String sDirectorio = getPathPlantillasFromDB(this.getUserBean(request)) + File.separator + form.getIdInstitucion();
		    	
		    	File fDirectorio = new File(sDirectorio);

		    	fDirectorio.mkdirs();
		    	
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
	        
	        CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUserBean(request));
	
	        String descripcionPlantilla=form.getDescripcion();
	        String idInstitucion=form.getIdInstitucion();
	        String idTipoProducto=form.getIdTipoProducto();
	        String idProducto=form.getIdProducto();
	        String idProductoInstitucion=form.getIdProductoInstitucion();
	        String idPlantilla=form.getIdPlantilla();
	        boolean bPorDefecto = (form.getPorDefecto()!=null && form.getPorDefecto().equals("1")) ? true : false;
	
	        ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() COMIENZO DE IMPORTAR PLANTILLA ",10);

	        tx.begin();	        
	        if (admPlantilla.importarPlantilla(descripcionPlantilla, idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, fPlantilla, bPorDefecto, bZIP))
	        {
	        	ClsLogging.writeFileLog("SIGAMantenimientoCertificadosPlantillasAction.grabar() OK IMPORTAR PLANTILLA",10);

	            sMensaje="messages.updated.success";
	            tx.commit();
	            forward = exitoModal(sMensaje,request);
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