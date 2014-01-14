/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.atos.utils.Validaciones;
import com.siga.beans.ExpClasificacionesBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpPlazoEstadoClasificacionAdm;
import com.siga.beans.ExpPlazoEstadoClasificacionBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.expedientes.form.EstadosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de fases de expediente.<br/>
 * Gestiona la edicion, borrado y creación de las fases. 
 *
 */
public class EstadosAction extends MasterAction {
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{	
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        String idInstitucion = userBean.getLocation();
        
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            userBean.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        EstadosForm form = (EstadosForm)formulario;
        form.setIdInstitucion(idInstitucion);
//        ExpEstadosAdm estadosAdm = new ExpEstadosAdm (this.getUserBean(request));
//        Vector datos = estadosAdm.selectBusqEstado(form);        
//        request.setAttribute("datos", datos);
                
//      ******************************        
        
        
//      Recupero el bean de Tipo de expediente para mostrar el nombre
        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        Vector tipoExp = tipoExpedienteAdm.select(idInstitucion,form.getIdTipoExpediente());
        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
        request.setAttribute("nombreExp", beantipoexp.getNombre( ));
        request.setAttribute("idInstitucion_TipoExpediente", idInstitucion);
        
        
        
//      ******************************
        
		return "abrir";
	}
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{	
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        String idInstitucion = userBean.getLocation();
        EstadosForm form = (EstadosForm)formulario;
        form.setIdInstitucion(idInstitucion);
        ExpEstadosAdm estadosAdm = new ExpEstadosAdm (this.getUserBean(request));
        Vector datos = estadosAdm.selectBusqEstado(form);        
        request.setAttribute("datos", datos);
                
//      ******************************        
        
        
//      Recupero el bean de Tipo de expediente para mostrar el nombre
        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        Vector tipoExp = tipoExpedienteAdm.select(idInstitucion,form.getIdTipoExpediente());
        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
        request.setAttribute("nombreExp", beantipoexp.getNombre( ));
        request.setAttribute("idInstitucion_TipoExpediente", idInstitucion);
        
		return "resultado";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    
	     return this.mostrarRegistro(mapping, formulario, request, response, true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    
	    return this.mostrarRegistro(mapping, formulario, request, response, false);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevoOld(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions { 	    
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();
	    EstadosForm form = (EstadosForm)formulario;        
//	    ExpEstadosAdm estadoAdm = new ExpEstadosAdm(this.getUserBean(request));
        ExpEstadosBean nuevoEstado = new ExpEstadosBean();
        nuevoEstado.setNombre("");
        nuevoEstado.setMensaje("");
        nuevoEstado.setIdInstitucion(Integer.valueOf(institucion));
        nuevoEstado.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
                
        Vector datos = new Vector();
        datos.add(nuevoEstado);
        request.setAttribute("datos", datos);
        
        ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm (this.getUserBean(request));
        String where2 = "C." + ExpEstadosBean.C_IDINSTITUCION + " = '" + institucion + "' AND ";
        where2 += "C." + ExpEstadosBean.C_IDTIPOEXPEDIENTE + " = '" + form.getIdTipoExpediente() + "'";                
             
        Vector vPlazos = plazoAdm.selectClasifPlazo("", where2);
        
        request.setAttribute("vPlazos", vPlazos);        
        
        request.setAttribute("editable", "1");

		return "mostrar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions { 	
		UsrBean userBean = this.getUserBean(request);	    
	    String institucion = userBean.getLocation();
	    
	    EstadosForm form = (EstadosForm)formulario;
	    
        ExpEstadosBean nuevoEstado = new ExpEstadosBean();
        nuevoEstado.setNombre("");
        nuevoEstado.setMensaje("");
        nuevoEstado.setIdInstitucion(Integer.valueOf(institucion));
        nuevoEstado.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
                
        Vector vEstados = new Vector();
        vEstados.add(nuevoEstado);
        request.setAttribute("vEstados", vEstados);
        
        ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm (userBean);
        String where2 = " C." + ExpEstadosBean.C_IDINSTITUCION + " = '" + institucion + "' " +
        		" AND C." + ExpEstadosBean.C_IDTIPOEXPEDIENTE + " = '" + form.getIdTipoExpediente() + "' ";                
             
        Vector vPlazos = plazoAdm.selectClasifPlazo("", where2);        
        request.setAttribute("vPlazos", vPlazos);        
        
        request.setAttribute("editable", "1");

		return "mostrar";
	}	

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();
        
	    ExpEstadosAdm estadosAdm = new ExpEstadosAdm(this.getUserBean(request));
	    EstadosForm form = (EstadosForm) formulario;

        // Recuperamos el nuevo id
	    Integer idEstado = estadosAdm.getNewIdEstado(form.getIdTipoExpediente(),form.getIdFase(),userBean);

	    //Obtenemos y validamos los plazos nuevos
	    String datosPlazos = request.getParameter("datosPlazos");		
		GstStringTokenizer st = new GstStringTokenizer(datosPlazos, "##");
		Hashtable htPlazosNuevos = new Hashtable();
		while (st.hasMoreTokens()) {
			String dupla = st.nextToken();
			if(dupla.equals(""))
				break;
			
			String parametros[] = dupla.split(",");			
			String idPlazo="", idClasificacion="";
			
			for (int i=0; i < parametros.length; i++) {
				String parametro = parametros[i];
				String d[] = parametro.split("==");
				
				String key = d[0];
				String value = d[1];
				
				if(key.equals("idPlazo"))
					 idPlazo = value;
					 
				else if(key.equals("idClasificacion"))
					 idClasificacion = value;
			}
				
			htPlazosNuevos.put(idClasificacion, this.validarPlazo(idPlazo));
		}    
	    
	    //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();    
	    
	    //Rellenamos el nuevo Bean
	    
	    ExpEstadosBean estado = new ExpEstadosBean();
	    
	    String siguienteestado=form.getIdEstadoSiguiente();
        String siguientefase=form.getIdFaseSiguiente();
            
	    estado.setIdInstitucion(Integer.valueOf(institucion));
	    estado.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
	    estado.setIdEstado(idEstado);
	    estado.setNombre(form.getEstado());
        if (form.getMensaje()!=null && !form.getMensaje().trim().equals("")) {
	        estado.setMensaje(form.getMensaje());
	    }
	    estado.setIdFase(Integer.valueOf(form.getIdFase()));
	    estado.setIdEstadoSiguiente(siguienteestado.equals("")?null:Integer.valueOf(siguienteestado));
        estado.setIdFaseSiguiente(siguientefase.equals("")?null:Integer.valueOf(siguientefase));
        estado.setAutomatico(form.getAutomatico()?"S":"N");
        estado.setEjecucionSancion(form.getEjecucionSancion()?"S":"N");
        estado.setEstadoFinal(form.getEstadoFinal()?"S":"N");
        
        estado.setActivarAlertas(form.getActivarAlertas()?"S":"N");
        if (Validaciones.validaNoInformado(form.getDiasAntelacion())) {
            estado.setDiasAntelacion(0);
        }
        else{
        	estado.setDiasAntelacion(new Integer(form.getDiasAntelacion().trim()));
        }
         
        estado.setPreSancionado(form.getSancionado());
        estado.setPreVisible(form.getPreVisible());
        estado.setPreVisibleFicha(form.getPreVisibleFicha());
        
        estado.setPostActPrescritas(form.getActPrescritas());
        estado.setPostSancionPrescrita(form.getSancionPrescrita());
        estado.setPostSancionFinalizada(form.getSancionFinalizada());
        estado.setPostAnotCanceladas(form.getAnotCanceladas());
        estado.setPostVisible(form.getPostVisible());
        estado.setPostVisibleFicha(form.getPostVisibleFicha());
        
        
       
       
        
        
        
	    
	    //Ahora procedemos a insertarlo
	    estadosAdm.insert(estado);
        
	    //insertamos los plazos nuevos
	    ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm(this.getUserBean(request));
	    
	    String sPlazoNuevo=null, sClasificacion=null;	    
	    Enumeration eClasificaciones = htPlazosNuevos.keys();
	    while (eClasificaciones.hasMoreElements()){
	        sClasificacion = (String)eClasificaciones.nextElement();
	        sPlazoNuevo=(String)htPlazosNuevos.get(sClasificacion);
	        // RGG 14-09-2005 para que inserte siempre aunque sea 0 if (!sPlazoNuevo.equals("0")){
                ExpPlazoEstadoClasificacionBean plazoBean = new ExpPlazoEstadoClasificacionBean();
                plazoBean.setIdInstitucion(Integer.valueOf(institucion));
                plazoBean.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
                plazoBean.setIdEstado(idEstado);
                plazoBean.setIdFase(Integer.valueOf(form.getIdFase()));
                plazoBean.setIdClasificacion(Integer.valueOf(sClasificacion));
                plazoBean.setPlazo(sPlazoNuevo);
                plazoAdm.insert(plazoBean);
            //}
	    }
	    
        tx.commit();
        ClsLogging.writeFileLog("Transacción de nuevo Estado realizada.",10);
        request.setAttribute("mensaje","messages.inserted.success");
	               
	    
	    
	    }catch (Exception exc) {
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},exc,tx);
	    } 
	    
	    request.setAttribute("modal","1");
	    return "exito";
	}	

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	    
	    EstadosForm form = (EstadosForm)formulario;	    
	    ExpEstadosAdm estadosAdm = new ExpEstadosAdm (this.getUserBean(request));
	    
//        Vector vOcultos = form.getDatosTablaOcultos(0);
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
	    //Obtenemos y validamos los plazos nuevos
	    String datosPlazos = request.getParameter("datosPlazos");		
		GstStringTokenizer st = new GstStringTokenizer(datosPlazos, "##");
		Hashtable htPlazosNuevos = new Hashtable();
		while (st.hasMoreTokens()) {
			String dupla = st.nextToken();
			if(dupla.equals(""))
				break;
			
			String parametros[] = dupla.split(",");			
			String idPlazo="", idClasificacion="";
			
			for (int i=0; i < parametros.length; i++) {
				String parametro = parametros[i];
				String d[] = parametro.split("==");
				
				String key = d[0];
				String value = d[1];
				
				if(key.equals("idPlazo"))
					 idPlazo = value;
					 
				else if(key.equals("idClasificacion"))
					 idClasificacion = value;
			}
				
			htPlazosNuevos.put(idClasificacion, this.validarPlazo(idPlazo));
		}  
               
        //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();	    
        
        // Recupero el bean del estado antiguo
        ExpEstadosBean estadobean=(ExpEstadosBean)hashOld.get("estado"); 
        Hashtable hashEstadoOld = estadosAdm.beanToHashTable(estadobean);
        
        // Modificamos los valores que vienen del formulario
        // Recordar que el bean guarda en su interior los datos antiguos
        String siguienteestado=form.getIdEstadoSiguiente();
        String siguientefase=form.getIdFaseSiguiente();
        
        estadobean.setNombre(form.getEstado());
        estadobean.setIdFase(Integer.valueOf(form.getIdFase()));
        estadobean.setIdEstadoSiguiente(siguienteestado.equals("")?null:Integer.valueOf(siguienteestado));
        estadobean.setIdFaseSiguiente(siguientefase.equals("")?null:Integer.valueOf(siguientefase));
        if (form.getMensaje()!=null && !form.getMensaje().trim().equals("")) {
            estadobean.setMensaje(form.getMensaje());
        }else{
        	estadobean.setMensaje("");
        }
        estadobean.setAutomatico(form.getAutomatico()?"S":"N");
        estadobean.setEjecucionSancion(form.getEjecucionSancion()?"S":"N");
        estadobean.setEstadoFinal(form.getEstadoFinal()?"S":"N");
        
        estadobean.setActivarAlertas(form.getActivarAlertas()?"S":"N");
        if (Validaciones.validaNoInformado(form.getDiasAntelacion())) {
            estadobean.setDiasAntelacion(0);
        }
        else{
        	estadobean.setDiasAntelacion(new Integer(form.getDiasAntelacion().trim()));
        }
        
        estadobean.setPreSancionado(form.getSancionado());
        estadobean.setPreVisible(form.getPreVisible());
        estadobean.setPreVisibleFicha(form.getPreVisibleFicha());
        
        estadobean.setPostActPrescritas(form.getActPrescritas());
        estadobean.setPostSancionPrescrita(form.getSancionPrescrita());
        estadobean.setPostSancionFinalizada(form.getSancionFinalizada());
        estadobean.setPostAnotCanceladas(form.getAnotCanceladas());
        estadobean.setPostVisible(form.getPostVisible());
        estadobean.setPostVisibleFicha(form.getPostVisibleFicha());
        
	    request.removeAttribute("DATABACKUP");
	    Hashtable hashEstado = estadosAdm.beanToHashTable(estadobean);
	    if (siguienteestado.equals(""))
	    	hashEstado.put(ExpEstadosBean.C_IDESTADOSIGUIENTE, "");
        estadosAdm.update(hashEstado, hashEstadoOld);
        
        
        //Recuperamos las clasificaciones
        Vector vPlazosOld = (Vector)hashOld.get("vPlazos");
        Hashtable hashPlazosOld = new Hashtable();
        
        //Obtenemos los datos comunes de la hash antigua
        // Como estos datos no deben haber cambiado, los tomo del bean
        hashPlazosOld.put(ExpPlazoEstadoClasificacionBean.C_IDESTADO,estadobean.getIdEstado());
        hashPlazosOld.put(ExpPlazoEstadoClasificacionBean.C_IDFASE, estadobean.getIdFase());        
        hashPlazosOld.put(ExpPlazoEstadoClasificacionBean.C_IDINSTITUCION, estadobean.getIdInstitucion());        
        hashPlazosOld.put(ExpPlazoEstadoClasificacionBean.C_IDTIPOEXPEDIENTE, estadobean.getIdTipoExpediente());        
         
        
	    String sPlazoViejo = null, sPlazoNuevo = null;
	    
	    for(int i=1;i<=htPlazosNuevos.size();i++){
	        sPlazoViejo="";
	        sPlazoNuevo=(String)htPlazosNuevos.get(String.valueOf(i));
             //Buscamos el plazo antiguo
            Enumeration e2=vPlazosOld.elements();
            while (e2.hasMoreElements()){
                Row fila=(Row)e2.nextElement();
                // Recuperamos el plazo para la clasificación
                if(fila.getString(ExpClasificacionesBean.C_IDCLASIFICACION).equalsIgnoreCase(String.valueOf(i))){
                    sPlazoViejo=fila.getString("PLAZO");
                    break;	                    
                }
            }
            // Si el plazo ha cambiado, lo actualizamos 
	        if (!sPlazoViejo.equals(sPlazoNuevo)){
	            hashPlazosOld.put(ExpPlazoEstadoClasificacionBean.C_IDCLASIFICACION,String.valueOf(i));	            
	            hashPlazosOld.put(ExpPlazoEstadoClasificacionBean.C_PLAZO,sPlazoViejo);
	            actualizarPlazo(hashPlazosOld,sPlazoNuevo,request);
	        }
	        	       
	    }      
	    
        tx.commit();
        ClsLogging.writeFileLog("Transacción de modificación de Estado realizada.",10);        
        request.setAttribute("mensaje","messages.updated.success");
	              
    } catch (Exception exc) {
        throwExcp("messages.general.error",new String[] {"modulo.expediente"},exc,tx);
    }
	    request.setAttribute("modal","1");
	    return "exito";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		EstadosForm form = (EstadosForm)formulario;


		form.setModal("false");

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
		ExpEstadosAdm estadosAdm = new ExpEstadosAdm (this.getUserBean(request));

		Vector vOcultos = form.getDatosTablaOcultos(0);
		Hashtable hash = new Hashtable();

		hash.put(ExpEstadosBean.C_IDINSTITUCION, userBean.getLocation());
		hash.put(ExpEstadosBean.C_IDESTADO, (String)vOcultos.elementAt(0));	
		hash.put(ExpEstadosBean.C_IDFASE, (String)vOcultos.elementAt(1));	
		hash.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,form.getIdTipoExpediente());
		
		
		
		UserTransaction tx = userBean.getTransaction();
		try {
			
			tx.begin();
			estadosAdm.updateEstadosSiguientesAsociados(hash);

			if (!estadosAdm.delete(hash)){
				throw new SIGAException("messages.elementoenuso.error");
				//throw new ClsExceptions("mensaje: "+estadosAdm.getError());
			}
			tx.commit();

		} catch (Exception exc){
			throwExcp("messages.general.error",new String[] {"modulo.expedientes"},exc,null);
			//throwExcp("messages.elementoenuso.error",exc,null);

		}

		return exitoRefresco("messages.deleted.success",request);
	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @param bEditable
	 * @return
	 * @throws ClsExceptions
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions {		  	
	    EstadosForm form = (EstadosForm)formulario;
	    UsrBean userBean = this.getUserBean(request);
        
	    String idInstitucion = userBean.getLocation();
	    String idTipoExpediente = form.getIdTipoExpediente();

		Vector vOcultos = form.getDatosTablaOcultos(0);		
		
        String idEstado = (String)vOcultos.elementAt(0);
        String idFase = (String)vOcultos.elementAt(1);
       
        return this.mostrarPlazo (idInstitucion, idTipoExpediente, idEstado, idFase, bEditable, request);
	}
	
	protected String mostrarPlazo (String idInstitucion, String idTipoExpediente, String idEstado, String idFase, boolean bEditable, HttpServletRequest request) throws ClsExceptions {
		UsrBean userBean = this.getUserBean(request);  
		ExpEstadosAdm estadosAdm = new ExpEstadosAdm (userBean);
		
		Hashtable hPlazo = estadosAdm.mostrarPlazo(idInstitucion, idTipoExpediente, idEstado, idFase);       
       
        // DEVUELVO RESULTADOS
        request.setAttribute("vFases", hPlazo.get("vFases"));
        request.setAttribute("vEstados", hPlazo.get("vEstados"));
        request.setAttribute("vEstadosSiguientes", hPlazo.get("vEstadosSiguientes"));
        request.setAttribute("vPlazos", hPlazo.get("vPlazos"));
        request.setAttribute("editable", bEditable ? "1" : "0");
        
        if (bEditable) {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put("estado", hPlazo.get("estado"));
            hashBackUp.put("vPlazos", hPlazo.get("vPlazos"));
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }    

		return "mostrar";
	}
	
	protected boolean actualizarPlazo(Hashtable hashPlazoViejo, String plazoNuevo,HttpServletRequest request) throws SIGAException{
	    
		// RGG cambio para controlar el nulo
		if (plazoNuevo == null) {
			plazoNuevo = "0";
		}
		
		Hashtable hashPlazosNew = (Hashtable)hashPlazoViejo.clone();
	    // Establecenos el nuevo valor del plazo
	    hashPlazosNew.put(ExpPlazoEstadoClasificacionBean.C_PLAZO,plazoNuevo);
	    
	    ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm(this.getUserBean(request));
	    
	    String plazoviejo=((String)hashPlazoViejo.get(ExpPlazoEstadoClasificacionBean.C_PLAZO)).trim();
	    boolean retorno=false;
	    try{
	      	if (!plazoviejo.equalsIgnoreCase("NULO")){
	      	   retorno=plazoAdm.update(hashPlazosNew,hashPlazoViejo);
         	} else 
         	{ retorno=plazoAdm.insert(hashPlazosNew);
         	};
	     
	    }catch(ClsExceptions ex){
	        throw new SIGAException(ex);
	    
	    }
	    return retorno;
	}
	
	public String validarPlazo(String sPlazo) throws SIGAException{
	    
	    String sTipoPlazo;
	  try{  
	    int iNumCaracteres;
	    
	    if (sPlazo.endsWith("dh")){
	        iNumCaracteres=2;
	        sTipoPlazo="dh";
    	}else if (sPlazo.endsWith("a")){
    	    iNumCaracteres=1;
    	    sTipoPlazo="a";
    	}else if (sPlazo.endsWith("m")){
    	    iNumCaracteres=1;
    	    sTipoPlazo="m";
    	}else{
    	    iNumCaracteres=0;
    	    sTipoPlazo="";
    	}
	    
	    String sValorPlazo = (sPlazo.substring(0,sPlazo.length()-iNumCaracteres)).trim();
	    try{
	        int iValorPlazo = Integer.valueOf(sValorPlazo).intValue();
	        
	        //LMS 18/08/2006
	        //Para no permitir plazos negativos.
	        if (iValorPlazo<0) throw new NumberFormatException();

	    } catch (NumberFormatException e){
	        SIGAException sigaExc = new SIGAException("messages.expedientes.formatoplazo.error");
	        //sigaExc.setSubLiteral("10, 5a, 8m, 7dh, 3 a, ...");
	        throw sigaExc;
	    }
	    
	    sPlazo = sValorPlazo + sTipoPlazo;
	    return sPlazo;
	  } catch (SIGAException e) {
			throw e;
	  }		
	}	
}
