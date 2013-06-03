package com.siga.administracion.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.CLSAdminLog;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.administracion.form.SIGAListadoTablasMaestrasForm;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.beans.GenTablasMaestrasAdm;
import com.siga.beans.GenTablasMaestrasBean;
import com.siga.beans.MasterBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGAListadoTablasMaestrasAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
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
	        
	        String sSQL = "SELECT " + sCampoCodigoExt + " AS CODIGOEXTERNO," + sCampoCodigo + " AS CODIGO, " +
	        		              " F_SIGA_GETRECURSO(" + sCampoDescripcion + ", " + this.getUserBean(request).getLanguage() +") AS DESCRIPCION" +
	                       ",BLOQUEADO AS BLOQUEADO ";
	        
	        if(beanTablaMaestra.getAceptabaja()==1)
	        	sSQL += " ,FECHABAJA ";
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
	        }
	        
	        sSQL += " FROM " + idTabla +  " WHERE 1 = 1 "; 
	                      
	        //sSQL += (sCodigoBusqueda!=null && !sCodigoBusqueda.equals("")) ? " AND " + sCampoCodigoExt + " = '" + sCodigoBusqueda + "'" : "";
	        sSQL += (sCodigoBusqueda!=null && !sCodigoBusqueda.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(sCodigoBusqueda.trim(),sCampoCodigoExt): "";
	        sSQL += (sDescripcionBusqueda!=null && !sDescripcionBusqueda.equals("")) ? " AND " + ComodinBusquedas.prepararSentenciaCompleta(sDescripcionBusqueda.trim(), "F_SIGA_GETRECURSO(" + sCampoDescripcion + ", " + this.getUserBean(request).getLanguage() +")"): "";
	        sSQL += (sLocal!=null && sLocal.equals("S")) ? " AND IDINSTITUCION = " + userBean.getLocation() : "";
	        
	        sSQL += " ORDER BY " + sCampoDescripcion;
	        
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

	        String codigoNuevo = this.getNuevoID(userBean.getLocation(), sNombreCampoCodigo, sNombreTabla, sLocal, sLongitudCodigo, sTipoCodigo);
	        
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), codigoNuevo);

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
	            row.setValue("IDINSTITUCION", userBean.getLocation());
	            vFields.add("IDINSTITUCION");
	        }
	
	        fields = vFields.toArray();
	        
	        //Chequeo si existe una descripcion igual:
	        if (this.existeDescripcion(userBean.getLocation(),sDescripcion,sNombreCampoDescripcion,sNombreCampoCodigo,sNombreTabla,sLocal,codigoNuevo,sTipoCodigo,userBean.getLanguage())){
	        	return error("messages.inserted.descDuplicated", new ClsExceptions("messages.inserted.descDuplicated"), request);
	        } else if (!sCodigoExt.trim().equals("") && this.existeCodigoExterno(userBean.getLocation(),sCodigoExt,sNombreCampoCodigoExt,sNombreCampoCodigo,sNombreTabla,sLocal,codigoNuevo,sTipoCodigo,sTipoCodigoExt)) {  
//	        	request.setAttribute("mensaje","messages.inserted.codDuplicated");
	        	return error("messages.inserted.codDuplicated", new ClsExceptions("messages.inserted.codDuplicated"), request);
	        } else {
	        	//if (true) {
	        	tx.begin();
		        if (row.add(sNombreTabla, fields)>0)
		        {
		        	///////////////////////////////////////////
		        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
	        		if (idRecurso != null) {
		    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), codigoNuevo);
			        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
			        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
			        	recCatalogoBean.setCampoTabla(sNombreCampoDescripcion);
			        	recCatalogoBean.setDescripcion(sDescripcion);
				        if (sLocal.equalsIgnoreCase("S")) {
				        	recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
				        }
			        	recCatalogoBean.setIdRecurso(idRecurso);
			        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
			        	recCatalogoBean.setNombreTabla(sNombreTabla);
			        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) 
			        		throw new SIGAException ("messages.inserted.error");
		        	}
		        	///////////////////////////////////////////
		        	
		        	tx.commit();
		            request.setAttribute("mensaje","messages.inserted.success");
		            
		            String mensaje = "El registro de la tabla [" + form.getAliasTabla() + "] con CÓDIGO EXTERNO=\"" + sCodigoExt + 
		            				 "\" y DESCRIPCIÓN=\"" + sDescripcion + "\" ha sido insertado";	
		            
		            CLSAdminLog.escribirLogAdmin(request, mensaje);
		        }
		        else {
		        	tx.rollback();
		            request.setAttribute("mensaje","messages.inserted.error");
		        }
	        }

		} 
		catch (Exception e){
			throwExcp("error.messages.application",e,tx);
		}
        request.setAttribute("modal","1");
	    return "exito";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		UsrBean userBean = null;
		UserTransaction tx = null;
		
		try {
	        SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	        userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        tx = userBean.getTransaction();
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
	        int aceptaBaja = (Integer)request.getSession().getAttribute("aceptaBaja");
	       
	        String sFechaModificacion =MasterBean.C_FECHAMODIFICACION;
	        String sUsuModif = MasterBean.C_USUMODIFICACION;
	        
		    String sSQL = "SELECT " + sNombreCampoCodigo + ", " + sNombreCampoCodigoExt + ", " + sNombreCampoDescripcion;
		    
		    if (sLocal.equals("S")) {
		        sSQL += ", IDINSTITUCION";
		    }
	
		    sSQL += " FROM " + sNombreTabla + " WHERE " + sNombreCampoCodigo + " = '" + sCodigo + "'";
	
		    if (sLocal.equals("S")) {
		        sSQL += " AND IDINSTITUCION = " + userBean.getLocation();
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
	        if (this.existeDescripcion(userBean.getLocation(),sDescripcion,sNombreCampoDescripcion, sNombreCampoCodigo,sNombreTabla,sLocal,sCodigo, sTipoCodigo, userBean.getLanguage())) {
	        	return error("messages.inserted.descDuplicated", new ClsExceptions("messages.inserted.descDuplicated"), request);
	        } 
	        else if (!sCodigoExt.trim().equals("") && this.existeCodigoExterno(userBean.getLocation(),sCodigoExt,sNombreCampoCodigoExt,sNombreCampoCodigo,sNombreTabla,sLocal,sCodigo,sTipoCodigo,sTipoCodigoExt)) {  
	        	return error("messages.inserted.codDuplicated", new ClsExceptions("messages.inserted.codDuplicated"), request);
	        } 
	        else {
	        	tx.begin();
	        	if (rc.findForUpdate(sSQL)) {

			        Row row = (Row)rc.get(0);
			        row.setCompareData(row.getRow());
			        Hashtable htNew = (Hashtable)row.getRow().clone();
			        htNew.put(sNombreCampoCodigoExt, sCodigoExt);

	    			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), sCodigo);
	    			if (idRecurso == null) {
	    				htNew.put(sNombreCampoDescripcion, sDescripcion);
	    			}
	    			htNew.put(sFechaModificacion, "SYSDATE");
	    			htNew.put(sUsuModif, userBean.getUserName());
	    			if(aceptaBaja == 1){
	    				if(sDarDeBaja.equals("S")){
	    					if(!this.existeFechaBaja(userBean.getLocation(), sNombreCampoCodigoExt, sNombreTabla, sLocal,sTipoCodigo, sCodigo)){
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
		    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), sCodigo);
		    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
			        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
			        	recCatalogoBean.setDescripcion(sDescripcion);
			        	recCatalogoBean.setIdRecurso(idRecurso);
			        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
			        	if(!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) { 
			        		throw new SIGAException ("messages.updated.error");
			        	}
	    			}
		        	///////////////////////////////////////////
			        
			    	tx.commit();
		            request.setAttribute("mensaje","messages.updated.success");
		            String mensaje = "El registro de la tabla [" + form.getAliasTabla() + "] con CÓDIGO EXTERNO=\"" + sCodigoExt + "\" ha sido modificado";	
		            CLSAdminLog.escribirLogAdmin(request, mensaje);
			    }
			    else {
			    	tx.rollback();
			    	request.setAttribute("mensaje", "error.messages.deleted");			    	
			    }
	        }
		} 
		catch (Exception e){
			throwExcp("messages.updated.error",e,tx);
		}
//      request.setAttribute("modal","1");
//    	request.setAttribute("sinrefresco", "1");			    	
//      return "exito";
		return this.exitoModal("messages.updated.success", request);
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, SIGAException
	{
	    try
	    {
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
		    form.setModal("false");
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    String sCodigo = (String)vOcultos.elementAt(0);
		    String sDescripcion = (String)vOcultos.elementAt(1);
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
		        sSQL += " AND IDINSTITUCION = " + userBean.getLocation();
		    }

		    RowsContainer rc = new RowsContainer();
		    UserTransaction tx = userBean.getTransaction();
		    tx.begin();
		    
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
    			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), sCodigo);
    			if (idRecurso != null) {
	    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
		        		throw new SIGAException ("error.messages.deleted");
		        	}
    			}
	        	///////////////////////////////////////////
			    
			    tx.commit();
		        request.setAttribute("mensaje","messages.deleted.success");
	            String mensaje = "El registro de la tabla [" + form.getAliasTabla() + "] con CÓDIGO EXTERNO =\"" + codigoExterno +  "\" y DESCRIPCIÓN=\"" + sDescripcion + "\" ha sido borrado";	
	            CLSAdminLog.escribirLogAdmin(request, mensaje);
		    }
		    else  {
		    	tx.rollback();
		        request.setAttribute("mensaje", "error.messages.deleted");
		    }
        } 
	    catch (Exception e) {
        	this.throwExcp("error.messages.application",e,null);
        }
	    return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
			SIGAListadoTablasMaestrasForm form = (SIGAListadoTablasMaestrasForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
	        String sCodigoExt="";
	        //String sDescripcion="";
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
	       
	        if (!bNuevo)
	        {
			    Vector vOcultos = form.getDatosTablaOcultos(0);
			    
			    sCodigoExt = (String)vOcultos.elementAt(0);
			   // sDescripcion = (String)vOcultos.elementAt(1);
			    sBloqueo = (String)vOcultos.elementAt(1);
		        
			    String sSQL = 	"SELECT " + sNombreCampoCodigoExt + " AS CODIGOEXTERNO, " + sNombreCampoCodigo + " AS CODIGO, F_SIGA_GETRECURSO(" + sNombreCampoDescripcion + ", " + this.getUserBean(request).getLanguage() + ") AS DESCRIPCION "; 
			    
			    if(aceptaBaja == 1){
			    	sSQL += " ,FECHABAJA ";           
			    }
			    
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
			    
			    
			    if (sLocal.equals("S"))
			    {
			        sSQL += " AND IDINSTITUCION = " + userBean.getLocation();
			    }
			    
			    RowsContainer rc = new RowsContainer();
			    
			    rc.findForUpdate(sSQL);
		
			    Row row = (Row)rc.get(0);
		        request.setAttribute("datos", row);
		        request.setAttribute("bloqueo",sBloqueo);
		
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
}