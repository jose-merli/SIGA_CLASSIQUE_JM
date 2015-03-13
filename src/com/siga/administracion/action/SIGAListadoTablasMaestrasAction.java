package com.siga.administracion.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.CenInstitucion;
import org.redabogacia.sigaservices.app.autogen.model.ScsTipoactuacioncostefijo;
import org.redabogacia.sigaservices.app.services.cen.CenInstitucionService;
import org.redabogacia.sigaservices.app.services.scs.ScsTipoactuacioncostefijoService;

import com.atos.utils.CLSAdminLog;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.administracion.form.SIGAListadoTablasMaestrasForm;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.beans.GenTablasMaestrasAdm;
import com.siga.beans.GenTablasMaestrasBean;
import com.siga.beans.MasterBean;
import com.siga.beans.ScsActuacionAsistCosteFijoAdm;
import com.siga.beans.ScsActuacionAsistCosteFijoBean;
import com.siga.beans.ScsTipoActuacionAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


public class SIGAListadoTablasMaestrasAction extends MasterAction
{	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;
					if ((accion!=null)&&((accion.equalsIgnoreCase("abrirConfiguracionCosteFijo"))||(accion.equalsIgnoreCase("editarAsistencia")) || (accion.equalsIgnoreCase("verAsistencia"))||(accion.equalsIgnoreCase("configuracionCosteFijoBuscarPor")) )){
						if(!accion.equalsIgnoreCase("configuracionCosteFijoBuscarPor")){
							miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
							miForm.reset(mapping,request);
							request.getSession().removeAttribute("DATAPAGINADOR");
						}
						mapDestino = abrirConfiguracionCosteFijo(mapping, miForm, request, response);
					}else if ((accion!=null)&&(accion.equalsIgnoreCase("insetarAsistencia"))) {
						mapDestino = insertarRelTipoAsistCosteFijo(mapping, miForm, request, response);
					}else if ((accion!=null)&&(accion.equalsIgnoreCase("borrarAsistencia"))) {
						mapDestino = borrarRelTipoAsistCosteFijo(mapping, miForm, request, response);
					}else{
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	
	
	
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	
	{
		
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		boolean esComision = userBean.isComision();
		request.setAttribute("intitucionComisionMultiple", ClsConstants.DB_FALSE);
		if(!esComision){
			//sin o es comision vamos aver si su comision es multiple, con lo que tendremos que ocultar ciertos tipos de informe
			CenInstitucionService cenInstitucionService = (CenInstitucionService)getBusinessManager().getService(CenInstitucionService.class);
			CenInstitucion cenInstitucion = new CenInstitucion();
			cenInstitucion.setIdinstitucion(userBean.getIdInstitucionComision());
			List<CenInstitucion> instituciones =  cenInstitucionService.getInstitucionesComision(cenInstitucion);
			if(instituciones!=null && instituciones.size()>1 ){
				request.setAttribute("intitucionComisionMultiple", ClsConstants.DB_TRUE);
			}
		}
		
		
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
        try {
	    	SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	    	
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        /***** PAGINACION*****/
	        HashMap databackup=new HashMap();
	        if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
			 	databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();
		
			
			//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
			String pagina = (String)request.getParameter("pagina");
			
			if (pagina!=null){
				datos = paginador.obtenerPagina(Integer.parseInt(pagina));
			}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
				datos = paginador.obtenerPagina((paginador.getPaginaActual()));
			}
			
			databackup.put("paginador",paginador);
			databackup.put("datos",datos);
			request.setAttribute("beanTablaMaestra", request.getSession().getAttribute("beanTablaMaestraOld"));
	}
      else{	
	  	    databackup=new HashMap();
	        Paginador resultado = null;
			Vector datos = null;
			String idTabla = form.getNombreTablaMaestra();
	        
	        GenTablasMaestrasAdm tablasMaestrasAdm = new GenTablasMaestrasAdm(this.getUserBean(request));
	        /**/
	        Vector datosTablaMaestra=tablasMaestrasAdm.select(" WHERE " + GenTablasMaestrasBean.C_IDTABLAMAESTRA + " = '" + idTabla + "'");
	
	        GenTablasMaestrasBean beanTablaMaestra = (GenTablasMaestrasBean)datosTablaMaestra.elementAt(0);
	
			resultado=getTablaMaestras( mapping, form,  request,  response);
			databackup.put("paginador",resultado);
			if (resultado!=null){ 
			   datos = resultado.obtenerPagina(1);
			   databackup.put("datos",datos);
			   request.getSession().setAttribute("DATAPAGINADOR",databackup);
			}   
			
			request.setAttribute("beanTablaMaestra", beanTablaMaestra);
			request.getSession().setAttribute("aceptaBaja", beanTablaMaestra.getAceptabaja());
			request.getSession().setAttribute("beanTablaMaestraOld", request.getAttribute("beanTablaMaestra"));
	  }	
	        
	  /******************************/
        } catch (Exception e) {
        	this.throwExcp("error.messages.application",e,null);
        }
        return "buscar";
	}

    /**
	 * Obtiene los valores dados de alta en las tablas maestras
	 * @param formulario Formulario de SIGAListadoTablasMaestrasForm con los datos de busqueda 
	 * @return Paginador 
	 */    
    protected Paginador getTablaMaestras(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
    	Paginador paginador=null;
        try {
        	
	    	SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
	        String idTabla = form.getNombreTablaMaestra();
	        
	        GenTablasMaestrasAdm tablasMaestrasAdm = new GenTablasMaestrasAdm(this.getUserBean(request));
	        /*SELECT IDTABLAMAESTRA, IDCAMPOCODIGO, IDCAMPOCODIGOEXT, LONGITUDCODIGOEXT, TIPOCODIGOEXT, IDTABLATRADUCCION, IDCAMPODESCRIPCION, PATHACCION, ALIASTABLA, FLAGBORRADOLOGICO, FLAGUSALENGUAJE, LONGITUDCODIGO, LONGITUDDESCRIPCION, IDRECURSO, IDLENGUAJE, TIPOCODIGO, LOCAL, FECHAMODIFICACION, USUMODIFICACION, ACEPTABAJA, IDTABLAREL, IDCAMPOCODIGOREL, DESCRIPCIONREL, QUERYTABLAREL, NUMEROTEXTOPLANTILLAS   FROM GEN_TABLAS_MAESTRAS  WHERE IDTABLAMAESTRA = 'SCS_TIPOFUNDAMENTOS' ORDER BY ALIASTABLA  */
	        Vector datosTablaMaestra=tablasMaestrasAdm.select(" WHERE " + GenTablasMaestrasBean.C_IDTABLAMAESTRA + " = '" + idTabla + "'");
	
	        GenTablasMaestrasBean beanTablaMaestra = (GenTablasMaestrasBean)datosTablaMaestra.elementAt(0);
	
	        String sCampoCodigo = beanTablaMaestra.getIdCampoCodigo();
	        String sCampoCodigoExt = beanTablaMaestra.getIdCampoCodigoExt();
	        String sCampoDescripcion = beanTablaMaestra.getIdCampoDescripcion();
	        String sAliasTabla = beanTablaMaestra.getAliasTabla();
	        String sLocal = beanTablaMaestra.getLocal();
	        if(beanTablaMaestra.getAceptabaja()==1)
	        	form.setPonerBajaLogica("S");
	        else
	        	form.setPonerBajaLogica("N");
	        
	        form.setIdTablaRel(beanTablaMaestra.getIdTablaRel());
	        form.setDescripcionRel(beanTablaMaestra.getDescripcionRel());
	        form.setIdCampoCodigoRel(beanTablaMaestra.getIdCampoCodigoRel());
	        form.setNumeroTextoPlantillas(beanTablaMaestra.getNumeroTextoPlantillas()!=null?beanTablaMaestra.getNumeroTextoPlantillas().toString():"");
	        form.setQueryTablaRel(beanTablaMaestra.getQueryTablaRel());
	        
	        String sCodigoBusqueda = form.getCodigoBusqueda().trim();
	        String sDescripcionBusqueda = form.getDescripcionBusqueda().trim();
	        
	        RowsContainer rc = new RowsContainer();
	        
	        boolean esComision = userBean.isComision();
			boolean esComisionMultiple = userBean.getInstitucionesComision()!=null && userBean.getInstitucionesComision().length>1;
	        
	        String sSQL = "SELECT " + sCampoCodigoExt + " AS CODIGOEXTERNO," + sCampoCodigo + " AS CODIGO " ;
	        if(esComision && esComisionMultiple && beanTablaMaestra.getIdTablaMaestra().equals("SCS_TIPOFUNDAMENTOS")){
	        	sSQL+= " ,DESCRIPCION " ;
	        }else{
	        	sSQL+= " ,F_SIGA_GETRECURSO(" + sCampoDescripcion + ", " + this.getUserBean(request).getLanguage() +") AS DESCRIPCION" ;
	        }
	        sSQL+=",BLOQUEADO AS BLOQUEADO ";
	        
	        if(beanTablaMaestra.getAceptabaja()==1)
	        	sSQL += " ,FECHABAJA ";/*
	        if(form.getNumeroTextoPlantillas()!=null && !form.getNumeroTextoPlantillas().equals("")&&!form.getNumeroTextoPlantillas().equals("null")){
	        	int numeroTextoPlantillas = Integer.parseInt(form.getNumeroTextoPlantillas());
	        	if(numeroTextoPlantillas>0)
	        		sSQL += ", ";
		        for (int i = 0; i < numeroTextoPlantillas; i++) {
		        	sSQL += "NVL(";
		        	sSQL += i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1);
		        	sSQL += ",' ')";
		        	sSQL += i==numeroTextoPlantillas-1?"||'%%'":"||'%%'||";
					
				}
		        if(numeroTextoPlantillas>0)
	        		sSQL += " TEXTOPLANTILLAS ";
	        }*/
	        
	        
	                      
	        //sSQL += (sCodigoBusqueda!=null && !sCodigoBusqueda.equals("")) ? " AND " + sCampoCodigoExt + " = '" + sCodigoBusqueda + "'" : "";
	        
			if(esComision && esComisionMultiple && beanTablaMaestra.getIdTablaMaestra().equals("SCS_TIPOFUNDAMENTOS")){
				sSQL += " FROM (SELECT CODIGO ,IDFUNDAMENTO ,  F_SIGA_GETRECURSO(DESCRIPCION, " + this.getUserBean(request).getLanguage() +") AS DESCRIPCION,BLOQUEADO ";
				 if(beanTablaMaestra.getAceptabaja()==1)
			        	sSQL += " ,FECHABAJA ";
				sSQL += " from SCS_TIPOFUNDAMENTOS ";
				sSQL += " where ";
				
				sSQL += " IDINSTITUCION IN ";
				sSQL += " (select idinstitucion ";
				sSQL += " from cen_institucion int ";
				sSQL += " where int.idcomision in ";
				sSQL += " (select intcom.idcomision ";
				sSQL += " from cen_institucion intcom ";
				sSQL += " where intcom.idinstitucion = idInstitucion ";
				sSQL += " and intcom.idinstitucion =  ";
				sSQL += userBean.getLocation();
				sSQL += " ) and idinstitucion <>  ";
				sSQL += userBean.getLocation();
				sSQL += " )) GROUP BY CODIGO,IDFUNDAMENTO,DESCRIPCION,BLOQUEADO ";
				 if(beanTablaMaestra.getAceptabaja()==1)
			        	sSQL += " ,FECHABAJA ";
				 sSQL += " HAVING COUNT(1) = "+userBean.getInstitucionesComision().length+" ";
				 
				/*sSQL += " HAVING COUNT(*) = (select COUNT(*) ";
				sSQL += " from cen_institucion int ";
				sSQL += " where int.idcomision in ";
				sSQL += " (select intcom.idcomision ";
				sSQL += " from cen_institucion intcom ";
				sSQL += " where intcom.idinstitucion = idInstitucion ";
				sSQL += " and intcom.idinstitucion =  ";
				sSQL += userBean.getLocation();
				sSQL += " ) and idinstitucion <>  ";
				sSQL += userBean.getLocation();
				sSQL += " ) ";*/
				sSQL += (sCodigoBusqueda!=null && !sCodigoBusqueda.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(sCodigoBusqueda.trim(),sCampoCodigoExt): "";
		        sSQL += (sDescripcionBusqueda!=null && !sDescripcionBusqueda.equals("")) ? " AND " + ComodinBusquedas.prepararSentenciaCompleta(sDescripcionBusqueda.trim(), "F_SIGA_GETRECURSO(" + sCampoDescripcion + ", " + this.getUserBean(request).getLanguage() +")"): "";
				sSQL += " ORDER BY DESCRIPCION ";
				
				
			}else{
				sSQL += " FROM " + idTabla +  " WHERE 1 = 1 ";
		        sSQL += (sCodigoBusqueda!=null && !sCodigoBusqueda.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(sCodigoBusqueda.trim(),sCampoCodigoExt): "";
		        sSQL += (sDescripcionBusqueda!=null && !sDescripcionBusqueda.equals("")) ? " AND " + ComodinBusquedas.prepararSentenciaCompleta(sDescripcionBusqueda.trim(), "F_SIGA_GETRECURSO(" + sCampoDescripcion + ", " + this.getUserBean(request).getLanguage() +")"): "";
		        sSQL += (sLocal!=null && sLocal.equals("S")) ? " AND IDINSTITUCION = " + userBean.getLocation() : "";
		        
		        sSQL += " ORDER BY " + sCampoDescripcion;
			}
	        
	       /*** PAGINACION ***/ 
	        paginador = new Paginador(sSQL);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}else{
				int registrosPorPagina = paginador.getNumeroRegistrosPorPagina();	    		
	    		Vector datos = paginador.obtenerPagina(1);
	    	
			}
	       /*********************/
			
		 } catch (Exception e) {
        	this.throwExcp("error.messages.application",e,null);
        }
        
        return paginador;
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String salida="";
		try {
			salida= mostrarRegistro(mapping, formulario, request, response, true, false);
	    } catch (Exception e) {
	    	this.throwExcp("error.messages.application",e,null);
	    }
	    return salida;
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String salida="";
		try {
			salida= mostrarRegistro(mapping, formulario, request, response, false, false);
	    } catch (Exception e) {
	    	this.throwExcp("error.messages.application",e,null);
	    }
	    return salida;
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String salida="";
		try {
			salida= mostrarRegistro(mapping, formulario, request, response, true, true);
	    } catch (Exception e) {
	    	this.throwExcp("error.messages.application",e,null);
	    }
	    return salida;
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		UsrBean userBean = null;
		UserTransaction tx = null;
		
		try {
	        SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	        userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        tx = userBean.getTransaction();
	        
	        
	        
	        boolean esComision = userBean.isComision();
			boolean esComisionMultiple = userBean.getInstitucionesComision()!=null && userBean.getInstitucionesComision().length>1;
			tx.begin();
			if(esComision && esComisionMultiple && form.getNombreTablaMaestra().equals("SCS_TIPOFUNDAMENTOS")){
				for (int i = 0; i < userBean.getInstitucionesComision().length; i++) {
					insertarRegistroMaestro(userBean.getInstitucionesComision()[i].toString(), form, userBean);
				}
				
			}else{
				insertarRegistroMaestro(userBean.getLocation(), form, userBean);
		        
			}
			tx.commit();
			request.setAttribute("mensaje","messages.inserted.success");
			String mensaje = "El registro de la tabla [" + form.getAliasTabla() + "] con CÓDIGO EXTERNO=\"" + form.getCodigoRegistroExt() + 
	 				 "\" y DESCRIPCIÓN=\"" + form.getDescripcionRegistro() + "\" ha sido insertado";	
			CLSAdminLog.escribirLogAdmin(request, mensaje);
			
		} 
		catch (SIGAException e){
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			return error(e.getLiteral(), new ClsExceptions(e.getLiteral()), request);
		}
		catch (Exception e){
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			request.setAttribute("mensaje","messages.inserted.error");
			if(e.getMessage().indexOf("ORA-01438") != -1) {
				return error("messages.inserted.maxLongitud", new ClsExceptions("messages.inserted.maxLongitud"), request);
			}else{
				throwExcp("error.messages.application",e,tx);
			}
		}
       
	    request.setAttribute("mensaje","messages.inserted.success");
	    request.setAttribute("sinrefresco","1");
		return "exito";
	}
	private void insertarRegistroMaestro(String idInstitucion,SIGAListadoTablasMaestrasForm form,UsrBean userBean) throws ClsExceptions, SIGAException{
		String strTextoPlantillas =  form.getNumeroTextoPlantillas();
        int tamañoTexto = 0;
        
        String [] textoPlantillasArray = null;
        if(strTextoPlantillas!=null &&!strTextoPlantillas.equals("")){
        	textoPlantillasArray = strTextoPlantillas.split("%%");
        	tamañoTexto = textoPlantillasArray.length;
        }
        String sNombreTabla = form.getNombreTablaMaestra();
        String sNombreCampoCodigo = form.getNombreCampoCodigo();
        String sNombreCampoCodigoExt = form.getNombreCampoCodigoExt();
        String sNombreCampoDescripcion = form.getNombreCampoDescripcion();
        
        String sCodigoExt = form.getCodigoRegistroExt();
        String sDescripcion = form.getDescripcionRegistro();
        String sLocal = form.getLocal();
        String sLongitudCodigo = form.getLongitudCodigo();
        String sLongitudCodigoExt = form.getLongitudCodigoExt();
        String sTipoCodigo = form.getTipoCodigo();
        String sTipoCodigoExt = form.getTipoCodigoExt();
    	
        Object[] fields  = new Object[]{};
        Vector   vFields = new Vector();
		
		Row row = new Row();
		
        String codigoNuevo = this.getNuevoID(idInstitucion, sNombreCampoCodigo, sNombreTabla, sLocal, sLongitudCodigo, sTipoCodigo);
        
		String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?Integer.valueOf(idInstitucion):null), codigoNuevo);

        row.setValue(sNombreCampoCodigo, codigoNuevo);
        row.setValue(sNombreCampoCodigoExt, sCodigoExt);
        row.setValue(sNombreCampoDescripcion, (idRecurso!=null)?""+idRecurso:sDescripcion);
        row.setValue(MasterBean.C_USUMODIFICACION, userBean.getUserName());
        row.setValue(MasterBean.C_FECHAMODIFICACION, "SYSDATE");	    

        vFields.add(sNombreCampoCodigo);
        vFields.add(sNombreCampoCodigoExt);
        vFields.add(sNombreCampoDescripcion);
        vFields.add(MasterBean.C_USUMODIFICACION);
        vFields.add(MasterBean.C_FECHAMODIFICACION);
        
        if(form.getIdTablaRel()!=null && !form.getIdTablaRel().equals("") && form.getIdCampoCodigoRel()!=null && !form.getIdCampoCodigoRel().equals("") && form.getIdRelacionado()!=null && !form.getIdRelacionado().equals("")){
        	row.setValue(form.getIdCampoCodigoRel(), new Integer(form.getIdRelacionado().split(",")[0]));
    		vFields.add(form.getIdCampoCodigoRel());
        }
        if(textoPlantillasArray!=null){
    		 for (int i = 0; i < textoPlantillasArray.length; i++) {
    			 row.setValue(i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1), textoPlantillasArray[i].trim());
    			 vFields.add(i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1));
 			}
    	}

        if (sLocal.equals("S"))
        {
            row.setValue("IDINSTITUCION", idInstitucion);
            vFields.add("IDINSTITUCION");
        }

        fields = vFields.toArray();
        
        //Chequeo si existe una descripcion igual:
        if (this.existeDescripcion(idInstitucion,sDescripcion,sNombreCampoDescripcion,sNombreCampoCodigo,sNombreTabla,sLocal,codigoNuevo,sTipoCodigo,userBean.getLanguage())){
        	throw new SIGAException("messages.inserted.descDuplicated");
        } else if (!sCodigoExt.trim().equals("") && !sNombreTabla.equals("SCS_PRETENSION") && this.existeCodigoExterno(idInstitucion,sCodigoExt,sNombreCampoCodigoExt,sNombreCampoCodigo,sNombreTabla,sLocal,codigoNuevo,sTipoCodigo,sTipoCodigoExt) ) {
        	throw new SIGAException("messages.inserted.codDuplicated");
        } else {
        	//if (true) {
        	
	        if (row.add(sNombreTabla, fields)>0)
	        {
	        	///////////////////////////////////////////
	        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
        		if (idRecurso != null) {
	    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?Integer.valueOf(idInstitucion):null), codigoNuevo);
		        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (userBean);
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setCampoTabla(sNombreCampoDescripcion);
		        	recCatalogoBean.setDescripcion(sDescripcion);
			        if (sLocal.equalsIgnoreCase("S")) {
			        	recCatalogoBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			        }
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	recCatalogoBean.setNombreTabla(sNombreTabla);
		        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) 
		        		throw new SIGAException ("messages.inserted.error");
	        	}
	        	///////////////////////////////////////////
	        }
	        
        }
        

	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UsrBean userBean = null;
		UserTransaction tx = null;
		
		try {
	        SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	        userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        int aceptaBaja = (Integer)request.getSession().getAttribute("aceptaBaja");
	        tx = userBean.getTransaction();
	        boolean esComision = userBean.isComision();
			boolean esComisionMultiple = userBean.getInstitucionesComision()!=null && userBean.getInstitucionesComision().length>1;
			tx.begin();
			if(esComision && esComisionMultiple && form.getNombreTablaMaestra().equals("SCS_TIPOFUNDAMENTOS")){
				for (int i = 0; i < userBean.getInstitucionesComision().length; i++) {
					modificarRegistroMaestro(userBean.getInstitucionesComision()[i].toString(), form,aceptaBaja, userBean);
				}
				
			}else{
				modificarRegistroMaestro(userBean.getLocation(), form,aceptaBaja, userBean);
		        
			}
			tx.commit();
	        
	        
	        request.setAttribute("mensaje","messages.updated.success");
            String mensaje = "El registro de la tabla [" + form.getAliasTabla() + "] con CÓDIGO EXTERNO=\"" + form.getCodigoRegistroExt() + "\" ha sido modificado";	
            CLSAdminLog.escribirLogAdmin(request, mensaje);
	        
		}catch (SIGAException e){
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			return error(e.getLiteral(), new ClsExceptions(e.getLiteral()), request);
		} 
		catch (Exception e){
			throwExcp("messages.updated.error",e,tx);
		}

		request.setAttribute("mensaje","messages.updated.success");
		request.setAttribute("sinrefresco","1");
		return "exito";
	}
	private void modificarRegistroMaestro(String idInstitucion,SIGAListadoTablasMaestrasForm form,int aceptaBaja,UsrBean userBean) throws ClsExceptions, SIGAException{
		 String strTextoPlantillas =  form.getNumeroTextoPlantillas();
	        int tamañoTexto = 0;
	        
	        String [] textoPlantillasArray = null;
	        if(strTextoPlantillas!=null &&!strTextoPlantillas.equals("")){
	        	textoPlantillasArray = strTextoPlantillas.split("%%");
	        	tamañoTexto = textoPlantillasArray.length;
	        }
	        
	       
	        
	        String sCodigo = form.getCodigoRegistro();
	        String sCodigoExt = form.getCodigoRegistroExt();
	        String sDescripcion = form.getDescripcionRegistro();
	        String sNombreTabla = form.getNombreTablaMaestra();
	        String sNombreCampoCodigo = form.getNombreCampoCodigo();
	        String sNombreCampoCodigoExt = form.getNombreCampoCodigoExt();
	        String sNombreCampoDescripcion = form.getNombreCampoDescripcion();
	        String sNombreCampoFechaBaja = "FECHABAJA";
	        String sLocal = form.getLocal();
	        String sAliasTabla = form.getAliasTabla();
	        String sLongitudCodigo = form.getLongitudCodigo();
	        String sLongitudCodigoExt = form.getLongitudCodigoExt();
	        String sLongitudDescripcion = form.getLongitudDescripcion();
	        String sTipoCodigo = form.getTipoCodigo();
	        String sTipoCodigoExt = form.getTipoCodigoExt();
	        String sDarDeBaja = form.getPonerBajaLogica();
	        
	       
	        String sFechaModificacion =MasterBean.C_FECHAMODIFICACION;
	        String sUsuModif = MasterBean.C_USUMODIFICACION;
	        
		    String sSQL = "SELECT " + sNombreCampoCodigo + ", " + sNombreCampoCodigoExt + ", " + sNombreCampoDescripcion;
		    
		    if (sLocal.equals("S")) {
		        sSQL += ", IDINSTITUCION";
		    }
	
		    sSQL += " FROM " + sNombreTabla + " WHERE " + sNombreCampoCodigo + " = '" + sCodigo + "'";
	
		    if (sLocal.equals("S")) {
		        sSQL += " AND IDINSTITUCION = " + idInstitucion;
		    }
		    		    
		    RowsContainer rc = new RowsContainer();
		    
		    Object[] claves = null;
		    
		    if (sLocal.equals("S")) {
		        claves = new Object[2];
		        claves[0] = sNombreCampoCodigo;
		        claves[1] = "IDINSTITUCION";
		    }
		    else {
		        claves = new Object[1];
		        claves[0] = sNombreCampoCodigo;
		    }
	
		    Object[] campos = null;
		    if(aceptaBaja == 1){
		    	campos = new Object[5+tamañoTexto];
		    	campos[0] = sNombreCampoDescripcion;
		        campos[1] = sNombreCampoCodigoExt;
		        campos[2] = sFechaModificacion;
		        campos[3] = sUsuModif;
		    	campos[4] = sNombreCampoFechaBaja;
		    	if(textoPlantillasArray!=null){
		    		 for (int i = 0; i < textoPlantillasArray.length; i++) {
		    			 campos[5+i] = i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1);
		 				
		 			}
		    		
		    	}
		    	
		    	if(form.getIdTablaRel()!=null && !form.getIdTablaRel().equals("") && form.getIdCampoCodigoRel()!=null && !form.getIdCampoCodigoRel().equals("")){
		    		campos = new Object[6+tamañoTexto];
			    	campos[0] = sNombreCampoDescripcion;
			        campos[1] = sNombreCampoCodigoExt;
			        campos[2] = sFechaModificacion;
			        campos[3] = sUsuModif;
			    	campos[4] = sNombreCampoFechaBaja;
		    		campos[5] = form.getIdCampoCodigoRel();
		    		if(textoPlantillasArray!=null){
			    		 for (int i = 0; i < textoPlantillasArray.length; i++) {
			    			 campos[6+i] = i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1);
			 				
			 			}
			    		
			    	}
		    		
		    	}
		    	
		    	
	        }else if(form.getIdTablaRel()!=null && !form.getIdTablaRel().equals("") && form.getIdCampoCodigoRel()!=null && !form.getIdCampoCodigoRel().equals("")){
	        	campos = new Object[5+tamañoTexto];
	        	campos[0] = sNombreCampoDescripcion;
		        campos[1] = sNombreCampoCodigoExt;
		        campos[2] = sFechaModificacion;
		        campos[3] = sUsuModif;
		        campos[4] = form.getIdCampoCodigoRel();
		        if(textoPlantillasArray!=null){
		    		 for (int i = 0; i < textoPlantillasArray.length; i++) {
		    			 campos[5+i] = i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1);
		 				
		 			}
		    		
		    	}
		    }else{
	        	campos = new Object[4+tamañoTexto];
	        	campos[0] = sNombreCampoDescripcion;
		        campos[1] = sNombreCampoCodigoExt;
		        campos[2] = sFechaModificacion;
		        campos[3] = sUsuModif;
		        if(textoPlantillasArray!=null){
		    		 for (int i = 0; i < textoPlantillasArray.length; i++) {
		    			 campos[4+i] = i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1);
		 				
		 			}
		    		
		    	}
	        }
		    
	        //Chequeo si existe una descripcion igual:
	        if (this.existeDescripcion(idInstitucion,sDescripcion,sNombreCampoDescripcion, sNombreCampoCodigo,sNombreTabla,sLocal,sCodigo, sTipoCodigo, userBean.getLanguage())) {
	        	throw new SIGAException("messages.inserted.descDuplicated");
	        } 
	        else if (!sCodigoExt.trim().equals("")  && !sNombreTabla.equals("SCS_TIPOFUNDAMENTOS") && !sNombreTabla.equals("SCS_PRETENSION") && this.existeCodigoExterno(idInstitucion,sCodigoExt,sNombreCampoCodigoExt,sNombreCampoCodigo,sNombreTabla,sLocal,sCodigo,sTipoCodigo,sTipoCodigoExt)) {  
	        	throw new SIGAException("messages.inserted.codDuplicated");
	        } 
	        else {
	        	if (rc.findForUpdate(sSQL)) {

			        Row row = (Row)rc.get(0);
			        row.setCompareData(row.getRow());
			        Hashtable htNew = (Hashtable)row.getRow().clone();
			        htNew.put(sNombreCampoCodigoExt, sCodigoExt);

	    			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?Integer.valueOf(idInstitucion):null), sCodigo);
	    			if (idRecurso == null) {
	    				htNew.put(sNombreCampoDescripcion, sDescripcion);
	    			}
	    			htNew.put(sFechaModificacion, "SYSDATE");
	    			htNew.put(sUsuModif, userBean.getUserName());
	    			if(aceptaBaja == 1){
	    				if(sDarDeBaja.equals("S")){
	    					if(!this.existeFechaBaja(idInstitucion, sNombreCampoCodigoExt, sNombreTabla, sLocal,sTipoCodigo, sCodigo)){
	    						htNew.put(sNombreCampoFechaBaja, "SYSDATE");
	    					}
	    				}else{
	    					htNew.put(sNombreCampoFechaBaja, "");
	    				}
	    			}
	    			
	    			//En el caso de la tabla SCS_TIPOFUNDAMENTOCALIF se añade el tipo de dictamen escogido
	    			
	    			if(form.getIdTablaRel()!=null && !form.getIdTablaRel().equals("") && form.getIdCampoCodigoRel()!=null && !form.getIdCampoCodigoRel().equals("")){
	    				if (form.getIdRelacionado() == null || form.getIdRelacionado().equals("")){
	    					htNew.put(form.getIdCampoCodigoRel(), "");
	    				} else {
	    					htNew.put(form.getIdCampoCodigoRel(), new Integer(form.getIdRelacionado().split(",")[0]));
	    				}
	    				
	    			}
	    			
	    			if(textoPlantillasArray!=null){
			    		 for (int i = 0; i < textoPlantillasArray.length; i++) {
			    			 htNew.put(i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1), textoPlantillasArray[i].trim());
			 				
			 			}
			    		
			    	}
	    			
	    			
			        row.load(htNew);
			        if (row.update(sNombreTabla, claves, campos) <= 0) {
			        	throw new SIGAException ("messages.updated.error");
			        }
			        
		        	///////////////////////////////////////////
		        	// Multiidioma: Actualizamos los recursos en gen_recursos_catalogos
	    			// Long idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), sCodigo);
	    			if (idRecurso != null) {
		    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?Integer.valueOf(idInstitucion):null), sCodigo);
		    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(userBean);
			        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
			        	recCatalogoBean.setDescripcion(sDescripcion);
			        	recCatalogoBean.setIdRecurso(idRecurso);
			        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
			        	if(!admRecCatalogos.update(recCatalogoBean, userBean)) { 
			        		throw new SIGAException ("messages.updated.error");
			        	}
	    			}
		        	///////////////////////////////////////////
			        
		            
			    }
			    else {
			    	throw new SIGAException("messages.updated.error");
			    	
			    				    	
			    }
	        }
		
	}
	
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, SIGAException
	{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = userBean.getTransaction();
	    
	    try
	    {
		    
		    SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
		   
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    String sCodigo = (String)vOcultos.elementAt(0);
		    String sDescripcion = (String)vOcultos.elementAt(1);
		    boolean esComision = userBean.isComision();
			boolean esComisionMultiple = userBean.getInstitucionesComision()!=null && userBean.getInstitucionesComision().length>1;
			tx.begin();
			if(esComision && esComisionMultiple && form.getNombreTablaMaestra().equals("SCS_TIPOFUNDAMENTOS")){
				for (int i = 0; i < userBean.getInstitucionesComision().length; i++) {
					borrarRegistroMaestro(userBean.getInstitucionesComision()[i].toString(),form,sCodigo,sDescripcion,  userBean);
				}
				
			}else{
				borrarRegistroMaestro(userBean.getLocation(),form,sCodigo,sDescripcion,  userBean);
		        
			}
	        request.setAttribute("mensaje","messages.deleted.success");
            String mensaje = "El registro de la tabla [" + form.getAliasTabla() + "] con DESCRIPCIÓN=\"" + sDescripcion + "\" ha sido borrado";	
            CLSAdminLog.escribirLogAdmin(request, mensaje);
		    tx.commit();
		    
        } 
	   
	    catch (Exception e) {
        	this.throwExcp("error.messages.application",e,tx);
        }
	    return "exito";
	}
	private void borrarRegistroMaestro(String idInstitucion,SIGAListadoTablasMaestrasForm form, String sCodigo,String sDescripcion,UsrBean userBean) throws ClsExceptions, SIGAException{
		String sNombreTabla = form.getNombreTablaMaestra();
	    String sNombreCampoCodigo = form.getNombreCampoCodigo();
	    String sNombreCampoCodigoExt = form.getNombreCampoCodigoExt();
	    String sNombreCampoDescripcion = form.getNombreCampoDescripcion();
	    String sLocal = form.getLocal();
	    
	    String sSQL = "SELECT " + sNombreCampoCodigo + ", " + sNombreCampoCodigoExt + ", " + sNombreCampoDescripcion;
	    
	    if (sLocal.equals("S")) {
	        sSQL += ", IDINSTITUCION";
	    }
	    
	    sSQL += " FROM " + sNombreTabla + " WHERE " + sNombreCampoCodigo + " = '" + sCodigo + "'";
	    
	    if (sLocal.equals("S"))  {
	        sSQL += " AND IDINSTITUCION = " + idInstitucion;
	    }

	    RowsContainer rc = new RowsContainer();
	    
	    
	    if (rc.findForUpdate(sSQL))
	    {
		    Row row = (Row)rc.get(0);
		    
		    String codigoExterno= row.getString(sNombreCampoCodigoExt);
		    
		    Object[] claves = null;
		    
		    if (sLocal.equals("S")) {
		        claves = new Object[2];
		        claves[0] = sNombreCampoCodigo;
		        claves[1] = "IDINSTITUCION";
		    }
		    else {
		        claves = new Object[1];
		        claves[0] = sNombreCampoCodigo;
		    }
		    
		    if (row.delete(sNombreTabla, claves) <= 0) {
		    	throw new SIGAException ("error.messages.deleted");
		    }

        	///////////////////////////////////////////
        	// Multiidioma: Borramos los recursos en gen_recursos_catalogos
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?Integer.valueOf(idInstitucion):null), sCodigo);
			if (idRecurso != null) {
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (userBean);
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
	        		throw new SIGAException ("error.messages.deleted");
	        	}
			}
        	///////////////////////////////////////////
		    

	    }
	    else  {
	    	throw new SIGAException ("error.messages.deleted");
	        
	    }
		
	}
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions {
			SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
	        String sCodigoExt="";
	        String sBloqueo="";
	        
	        String sNombreTabla = form.getNombreTablaMaestra();
	        String sNombreCampoCodigo = form.getNombreCampoCodigo();
	        String sNombreCampoCodigoExt = form.getNombreCampoCodigoExt();
	        String sNombreCampoDescripcion = form.getNombreCampoDescripcion();
	        String sLocal = form.getLocal();
	        String sAliasTabla = form.getAliasTabla();
	        String sLongitudCodigo = form.getLongitudCodigo();
	        String sLongitudCodigoExt = form.getLongitudCodigoExt();
	        String sLongitudDescripcion = form.getLongitudDescripcion();
	        String sTipoCodigo = form.getTipoCodigo();
	        String sTipoCodigoExt = form.getTipoCodigoExt();
	        String idTableRel = form.getIdTablaRel();
	        String idCampoCodigoTablaRel = form.getIdCampoCodigoRel();
	        
	        String sDarDeBaja = "N";
	        int aceptaBaja = (Integer)request.getSession().getAttribute("aceptaBaja");
	        if(aceptaBaja == 1){
	        	sDarDeBaja = "S";
	        }
	       
	        if (!bNuevo) {
			    Vector vOcultos = form.getDatosTablaOcultos(0);
			    
			    if(vOcultos!=null){
				    sCodigoExt = (String)vOcultos.elementAt(0);
				    sBloqueo = (String)vOcultos.elementAt(1);
			    //Si hemos configurado un coste fijo hay que recargar la ventana
			    }else{
			    	sBloqueo=(String) request.getSession().getAttribute("bloqueo");
			    	request.getSession().removeAttribute("bloqueo");
			    	sCodigoExt=form.getCodigoRegistro();
			    }
			    String sSQL = "SELECT " + sNombreCampoCodigoExt + " AS CODIGOEXTERNO, " + 
			    				sNombreCampoCodigo + " AS CODIGO, " +
			    				" F_SIGA_GETRECURSO(" + sNombreCampoDescripcion + ", " + 
			    				this.getUserBean(request).getLanguage() + ") AS DESCRIPCION"; 
			    
			    if(aceptaBaja == 1){
			    	sSQL += ", FECHABAJA ";           
			    }
			    
			    if(form.getNumeroTextoPlantillas()!=null && !form.getNumeroTextoPlantillas().equals("") && !form.getNumeroTextoPlantillas().equals("null")){
		        	int numeroTextoPlantillas = Integer.parseInt(form.getNumeroTextoPlantillas());
		        	if(numeroTextoPlantillas>0)
		        		sSQL += ", ";
		        	
			        for (int i = 0; i < numeroTextoPlantillas; i++) {
			        	sSQL += "TO_CLOB(NVL(";
			        	sSQL += i==0?GenTablasMaestrasBean.C_TEXTOPLANTILLA:GenTablasMaestrasBean.C_TEXTOPLANTILLA+(i+1);
			        	sSQL += ",' '))";
			        	sSQL += i==numeroTextoPlantillas-1?"||'%%'":"||'%%'||";
						
					}
			        if(numeroTextoPlantillas>0)
		        		sSQL += " TEXTOPLANTILLAS ";
		        }
			    
			    
			    if(idTableRel!=null && !idTableRel.equals("") && idCampoCodigoTablaRel!=null && !idCampoCodigoTablaRel.equals("")){
			    	sSQL += " ,"+idCampoCodigoTablaRel +" AS IDRELACIONADO";
			    	
			    	//request.setAttribute("IDRELACIONADO",idTableRel);
		 	        request.setAttribute("IDTABLAREL",idTableRel);
		 	       request.setAttribute("IDCAMPOCODIGOREL", form.getIdCampoCodigoRel());
		 	        request.setAttribute("DESCRIPCIONREL", form.getDescripcionRel());
		 	        
		 	        request.setAttribute("QUERYTABLAREL", form.getQueryTablaRel());
			    	
			    }
			    
//			    if (sNombreTabla !=null && sNombreTabla.equals(ScsTipoFundamentosCalifBean.T_NOMBRETABLA)){
//			    	sSQL += " ,IDTIPODICTAMENEJG"; 
//			    }else if (sNombreTabla !=null && sNombreTabla.equals(ScsTipoFundamentosBean.T_NOMBRETABLA)){
//			    	sSQL += " ,IDTIPORESOLUCION"; 
//			    } 
			    
			    sSQL += "  FROM " + sNombreTabla +
			    	   " WHERE " + sNombreCampoCodigo + " = '" + sCodigoExt + "'";
			    
			    boolean esComision = userBean.isComision();
				boolean esComisionMultiple = userBean.getInstitucionesComision()!=null && userBean.getInstitucionesComision().length>1;
				if(esComision && esComisionMultiple && sNombreTabla.equals("SCS_TIPOFUNDAMENTOS")){
					//como las instituciones de las comisiones tienen los mismos fundamento cogemos el primero...
					sSQL += " AND IDINSTITUCION = " + userBean.getInstitucionesComision()[0];
				}else{
				    if (sLocal.equals("S"))
				    {
				        sSQL += " AND IDINSTITUCION = " + userBean.getLocation();
				    }
				}
			    
			    RowsContainer rc = new RowsContainer();
			    
			    rc.findForUpdate(sSQL);
		
			    Row row = (Row)rc.get(0);
		        request.setAttribute("datos", row);
		        request.setAttribute("bloqueo",sBloqueo);
		        
		        //Si la tabla es costes fijos mostramos los tipos de asistencias relacionadas
		        if(sNombreTabla.equals("SCS_COSTEFIJO")){
		        	
		        	ScsActuacionAsistCosteFijoAdm actAsisCostAdm = new ScsActuacionAsistCosteFijoAdm (userBean);
		        	List tiposAsistenciasRelList= new ArrayList();
		        	boolean regBajaLog=false;
		        	
		        	if(form.getRegBajaLogica()!=null){
		        		regBajaLog=UtilidadesString.stringToBoolean(form.getRegBajaLogica());
		        		request.setAttribute("bIncluirRegistrosConBajaLogica", form.getRegBajaLogica());
		        	}
		        	
		        	Vector tiposAsistenciasRelV=actAsisCostAdm.getTiposAsistenciasCosteFijo(userBean.getLocation(), sCodigoExt,regBajaLog,userBean.getLanguage());
	        	
		        	if((tiposAsistenciasRelV!=null)&&(tiposAsistenciasRelV.size()>0)){
		        	
		        		for(int t=0;t<tiposAsistenciasRelV.size();t++){
		        			
		        			 tiposAsistenciasRelList.add((Hashtable) tiposAsistenciasRelV.get(t));
		        		}
		        		        	
		        	}	
		        	
		        
		        	request.setAttribute("tiposAsistenciasRel", tiposAsistenciasRelList);
		        }
		
	        } else {
	        	
	        	if(idTableRel!=null && !idTableRel.equals("") ){
			    	
		 	        request.setAttribute("IDTABLAREL",idTableRel);
		 	        request.setAttribute("DESCRIPCIONREL", form.getDescripcionRel());
		 	       request.setAttribute("IDCAMPOCODIGOREL", form.getIdCampoCodigoRel());
		 	        
		 	        request.setAttribute("QUERYTABLAREL", form.getQueryTablaRel());
			    	
			    }
	        	
	            //Obtengo el nuevo identificador secuencial:
	            // RGG ya no obtengo porque no hace falta sugerir 
	        	//String codigoNuevo = this.getNuevoID(userBean.getLocation(), sNombreCampoCodigo, sNombreTabla, sLocal, sLongitudCodigo, sTipoCodigo);
	        	//request.setAttribute("codigoNuevo", codigoNuevo);
	        }
	
	        request.setAttribute("editable", bEditable ? "1" : "0");
	        request.setAttribute("nuevo", bNuevo ? "1" : "0");
	        request.setAttribute("nombreTabla", sNombreTabla);
	        request.setAttribute("nombreCampoCodigo", sNombreCampoCodigo);
	        request.setAttribute("nombreCampoCodigoExt", sNombreCampoCodigoExt);
	        request.setAttribute("nombreCampoDescripcion", sNombreCampoDescripcion);
	        request.setAttribute("local", sLocal);
	        request.setAttribute("aliasTabla", sAliasTabla);
	        request.setAttribute("longitudCodigo", sLongitudCodigo);
	        request.setAttribute("longitudCodigoExt", sLongitudCodigoExt);
	        request.setAttribute("longitudDescripcion", sLongitudDescripcion);
	        request.setAttribute("tipoCodigo", sTipoCodigo);
	        request.setAttribute("tipoCodigoExt", sTipoCodigoExt);
	        request.setAttribute("darDeBaja", sDarDeBaja);
	        request.setAttribute("NUMEROTEXTOPLANTILLAS", form.getNumeroTextoPlantillas());
	        
	       
	        

	    return "mostrar";
	}
	
	//Obtengo un nuevo id de forma secuencial:
	private String getNuevoID(String idInstitucion, String sNombreCampoCodigo, String sNombreTabla, String sLocal, String sLongitudCodigo, String sTipoCodigo) throws ClsExceptions{
		String id=null, sSQL=null;
/*
		String maximo = "";
		if (sLongitudCodigo!=null && !sLongitudCodigo.equals("")) {
			int longi = new Integer(sLongitudCodigo).intValue();
			for (int i=0;i<longi;i++) {
				maximo += "9";
			}
		}
*/		

		sSQL = "SELECT NVL(MAX(TO_NUMBER(" + sNombreCampoCodigo + ")),0)+1 AS CODIGONUEVO " +  
        	   " FROM " + sNombreTabla;

		if (sLocal.equals("S"))
	    {
	        sSQL += " WHERE IDINSTITUCION = " + idInstitucion;
	    }

/*
		sSQL = "SELECT (ABS(MOD(dbms_random.random,"+maximo+"))) as CODIGONUEVO " +
		     "FROM dual where (ABS(MOD(dbms_random.random,"+maximo+")))";

		if (sTipoCodigo.equalsIgnoreCase("A")) {
			sSQL += "||'' ";
		}
		
		sSQL += " not in (select " + sNombreCampoCodigo + " from " + sNombreTabla ;

		if (sLocal.equals("S"))
		{
	     sSQL += " WHERE IDINSTITUCION = " + idInstitucion;
		}
		
		sSQL += ")";
*/		
/* version con sugerencia y tabla de codigos
		sSQL = "SELECT COD as CODIGONUEVO FROM ((select ";
		if (sTipoCodigo.equalsIgnoreCase("A")) {
			sSQL += " to_char(codigo) ";
		} else {
			sSQL += " codigo ";
		}

		sSQL +=" as COD from gen_codigosmaestros) minus (select " + sNombreCampoCodigo + " as COD from " + sNombreTabla ;
	
		if (sLocal.equals("S"))
		{
	    sSQL += " WHERE IDINSTITUCION = " + idInstitucion;
		}
	
		sSQL += ")";
		sSQL += ") where rownum=1";
*/
		RowsContainer rc = new RowsContainer();
		rc.findForUpdate(sSQL);
	    Row row = (Row)rc.get(0);
/*	    
	    if (row.getString("CODIGONUEVO")!=null && !row.getString("CODIGONUEVO").equals(""))
	    	id = (String)row.getString("CODIGONUEVO");
	    else
	    	id = "1";
*/	    	
    	id = (String)row.getString("CODIGONUEVO");
		
		return id;
	}
	
	//Compruebo si existe un registro con esa descripcion:
	private boolean existeDescripcion(String idInstitucion, String descripcion, String sNombreCampoDescripcion, String sNombreCampoCodigo, String sNombreTabla, String sLocal, String codigo, String sTipoCodigo, String lenguaje) throws ClsExceptions{
		String sSQL=null;
		boolean existe = false;

		sSQL = "SELECT count(1) AS DESCRIPCION" +  
        	   " FROM " + sNombreTabla+
			   " WHERE upper (f_siga_getrecurso("+ sNombreCampoDescripcion +","+lenguaje+")) = '"+descripcion.toUpperCase().trim()+"' ";
		if (sTipoCodigo.equalsIgnoreCase("A")) {
			sSQL += " AND   "+ sNombreCampoCodigo +" <> '"+codigo+"' ";
		} else  {
			sSQL += " AND   "+ sNombreCampoCodigo +" <> "+codigo+" ";
		}

		if (sLocal.equals("S"))
	    {
	        sSQL += " AND IDINSTITUCION = " + idInstitucion;
	    }

		RowsContainer rc = new RowsContainer();
		rc.findForUpdate(sSQL);
	    Row row = (Row)rc.get(0);
	    
	    if (row.getString("DESCRIPCION")!=null && !row.getString("DESCRIPCION").equals("0"))
	    	existe = true;
		
		return existe;
	}
	//Compruebo si existe un registro con ese codigo externo:
	private boolean existeCodigoExterno(String idInstitucion, String codigoExterno, String sNombreCampoCodigoExterno, String sNombreCampoCodigo, String sNombreTabla, String sLocal, String codigo, String sTipoCodigo, String sTipoCodigoExterno) throws ClsExceptions{
		String sSQL=null;
		boolean existe = false;

		sSQL = "SELECT count(1) AS CODEXT" +  
        	   " FROM " + sNombreTabla;
       	if (sTipoCodigoExterno.equalsIgnoreCase("A")) {
			   sSQL+=" WHERE "+ sNombreCampoCodigoExterno +" = '"+codigoExterno.trim()+"' ";
       	} else {
			   sSQL+=" WHERE "+ sNombreCampoCodigoExterno +" = "+codigoExterno.trim()+" ";
       	}
       	
		if (sTipoCodigo.equalsIgnoreCase("A")) {
			sSQL += " AND   "+ sNombreCampoCodigo +" <> '"+codigo+"' ";
		} else  {
			sSQL += " AND   "+ sNombreCampoCodigo +" <> "+codigo+" ";
		}

		if (sLocal.equals("S"))
	    {
	        sSQL += " AND IDINSTITUCION = " + idInstitucion;
	    }

		RowsContainer rc = new RowsContainer();
		rc.findForUpdate(sSQL);
	    Row row = (Row)rc.get(0);
	    
	    if (row.getString("CODEXT")!=null && !row.getString("CODEXT").equals("0"))
	    	existe = true;
		
		return existe;
	}
	
		//Compruebo si existe un registro con esa descripcion:
	private boolean existeFechaBaja(String idInstitucion, String sNombreCampoCodigo, String sNombreTabla, String sLocal,String sTipoCodigo, String codigo) throws ClsExceptions{
		String sSQL=null;
		boolean existe = false;

		sSQL = "SELECT FECHABAJA" +  
        	   " FROM  " + sNombreTabla;
		
		sSQL += " WHERE   "+ sNombreCampoCodigo +" = '"+codigo+"' ";
		
		/*if (sTipoCodigo.equalsIgnoreCase("A")) {
			sSQL += " WHERE   "+ sNombreCampoCodigo +" = '"+codigo+"' ";
		} else  {
			sSQL += " WHERE   "+ sNombreCampoCodigo +" = "+codigo+" ";
		}*/
		
		if (sLocal.equals("S"))
	    {
	        sSQL += " AND IDINSTITUCION = " + idInstitucion;
	    }

		RowsContainer rc = new RowsContainer();
		rc.findForUpdate(sSQL);
	    Row row = (Row)rc.get(0);
	    if(row !=null){
	    	if (row.getString("FECHABAJA")!=null && !row.getString("FECHABAJA").equals(""))
	    		existe = true;
	    }
		return existe;
	}
	
	protected String abrirConfiguracionCosteFijo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
	
	{
		final String[] clavesBusqueda={ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA,ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION};
		
		try{
		
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
			String idCosteFijo=(String)form.getCodigoRegistro();
			String idTipoAsistencia="";
			
			if(request.getParameter("modo").equals("verAsistencia"))
				request.setAttribute("modoConsulta","1");
			else
				request.setAttribute("modoConsulta","0");
			
			request.setAttribute("modo",request.getParameter("modo"));
			
			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = form.getSeleccionarTodos()!=null 
				&& !form.getSeleccionarTodos().equals("");
			
			
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) form.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if ((seleccionados != null )&&(!seleccionados.isEmpty())) {
					ArrayList alRegistros = actualizarSelecionados(clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						form.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}

			HashMap databackup = (HashMap) form.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null&&!isSeleccionarTodos){
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
				Vector datos = null;
				boolean regBajaLog=false;
				
				if(request.getParameter("modo")!=null)
					if(!request.getParameter("modo").equals("abrirConfiguracionCosteFijo")){
						idTipoAsistencia = form.getId();
						//Si estamos consultando/editando un registro en baja lógica
						if(form.getRegBajaLogica()!=null){
			        		regBajaLog=UtilidadesString.stringToBoolean(form.getRegBajaLogica());
			        		request.setAttribute("bIncluirRegistrosConBajaLogica", form.getRegBajaLogica());
						}
						
					}

				ScsTipoActuacionAdm actAsisCostAdm = new ScsTipoActuacionAdm (userBean);
	        	resultado=actAsisCostAdm. getTiposAsistTiposActDispCosteFijo (userBean.getLocation(), idCosteFijo, idTipoAsistencia,regBajaLog, userBean.getLanguage());

				databackup.put("paginador",resultado);
				
				if (resultado!=null && resultado.getNumeroTotalRegistros()>0){ 
							
					
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)actAsisCostAdm.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));

						aniadeClavesBusqueda(clavesBusqueda,clavesRegSeleccinados);
						form.setRegistrosSeleccionados(clavesRegSeleccinados);

						int pagina;
						try{
							pagina = Integer.parseInt(form.getSeleccionarTodos());
						}catch (Exception e) {
							// Con esto evitamos un error cuando se recupera una pagina y hemos "perdido" la pagina actual
							// cargamos la primera y no evitamos mostrar un error
							pagina = 1;
						}
						datos = resultado.obtenerPagina(pagina);
						form.setSeleccionarTodos("");
						
					}else{				
						form.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					databackup.put("datos",datos);

				}else{
					resultado = null;
					form.setRegistrosSeleccionados(new ArrayList());
				}  
				form.setDatosPaginador(databackup);

			}			
		
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return "abrirConfCostesFijos";
	}

	protected String borrarRelTipoAsistCosteFijo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
	
	{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		
		
		try{
		
			SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			ScsTipoactuacioncostefijoService scsTipoActCosteFijService = (ScsTipoactuacioncostefijoService) businessManager.getService(ScsTipoactuacioncostefijoService.class);
			ScsTipoactuacioncostefijo obj = new ScsTipoactuacioncostefijo();
			
			obj.setIdinstitucion(Short.parseShort(userBean.getLocation()));
			obj.setIdtipoasistencia(Short.parseShort(form.getId()));
			obj.setIdcostefijo(Short.parseShort(form.getCodigoRegistro()));
			obj.setUsumodificacion(Integer.parseInt(userBean.getUserName()));
			
			
			
			scsTipoActCosteFijService.delete(obj);	
			
		

		} catch (Exception e) {
			
			throwExcp("messages.deleted.error",new String[] {"modulo.administracion"}, e, null);
		}

		request.setAttribute("mensaje","messages.deleted.success");
		return "exito";
	}
	
	protected String insertarRelTipoAsistCosteFijo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
	
	{
		
		String resultado="";
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		
			
		try{
		
			
			SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
						
			BusinessManager businessManager =  BusinessManager.getInstance();
			ScsTipoactuacioncostefijoService scsTipoActCosteFijService = (ScsTipoactuacioncostefijoService) businessManager.getService(ScsTipoactuacioncostefijoService.class);
			
			ScsTipoactuacioncostefijo obj = new ScsTipoactuacioncostefijo();
			obj.setIdinstitucion(Short.parseShort(userBean.getLocation()));
			obj.setIdcostefijo(Short.parseShort(form.getCodigoRegistro()));
			obj.setUsumodificacion(Integer.parseInt(userBean.getUserName()));
			
			//Borramos las relaciones existentes e insertamos las nuevas
			scsTipoActCosteFijService.insertarRelacionAsistActCosteFijo(form.getDatosConf(),form.getId(),obj);		
			
			if((form.getId()!=null)&&(!form.getId().isEmpty()))
				resultado="successEditarRelAsistencia";
			else
				resultado="successNuevaRelAsistencia";
				
		} catch (Exception e) {
			
			throwExcp("messages.inserted.error",new String[] {"modulo.administracion"}, e, null);
		}
		request.setAttribute("mensaje","messages.inserted.success");
		return resultado;
	}
	
	
	
}