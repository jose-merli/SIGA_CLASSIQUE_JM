/*
 * Created on Feb 3, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSolicitudIncorporacionAdm;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.EnvComunicacionMorososAdm;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnvioProgramadoAdm;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvEstadoEnvioAdm;
import com.siga.beans.EnvEstatEnvioAdm;
import com.siga.beans.EnvProgramIRPFAdm;
import com.siga.beans.EnvProgramIRPFBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasBean;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeCertificadoIRPF;
import com.siga.informes.InformeFactura;
import com.siga.informes.MasterReport;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;


/**
 * Action para la búsqueda de expedientes (simple y avanzada)
 */
public class DefinirEnviosAction extends MasterAction {


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

				else if (accion.equalsIgnoreCase("envioModal"))
				{
					mapDestino = envioModal(mapping, miForm, request, response);
				} 

				else if (accion.equalsIgnoreCase("envioModalCertificado"))
				{
					mapDestino = envioModalCertificado(mapping, miForm, request, response);
				} 

				else if (accion.equalsIgnoreCase("insertarEnvioModal"))
				{
					mapDestino = insertarEnvioModal(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("insertarEnvioModalCertificado"))
				{
					mapDestino = insertarEnvioModalCertificado(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("procesarEnvio"))
				{
					mapDestino = procesarEnvio(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("descargarLogErrores"))
				{
					mapDestino = descargarLogErrores(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("descargarEstadistica"))
				{
					mapDestino = descargarEstadistica(mapping, miForm, request, response);
				}

				/*else if (accion.equalsIgnoreCase("procesarCorreoOrdinario"))
	            {
	                mapDestino = procesarCorreoOrdinario(mapping, miForm, request, response);
	            }*/
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
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		} 
	}

	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		// Si el acceso es de lectura, quitamos el botón de 'Nuevo'

		try{

			//DefinirEnviosForm form = (DefinirEnviosForm)formulario;

			//if (request.getParameter("buscar")!=null){
			/*if (ses.getAttribute("buscarEnvios")!=null){
	    	    request.setAttribute("buscarEnvios","true");
	    	    ses.removeAttribute("buscarEnvios");
		    	if (ses.getAttribute("DATABACKUP")!=null){
			    	if (ses.getAttribute("DATABACKUP").getClass().getName().equals("com.siga.envios.form.DefinirEnviosForm")){
			    	    DefinirEnviosForm formBackup = (DefinirEnviosForm)request.getSession().getAttribute("DATABACKUP");
			    		form.setFechaDesde(formBackup.getFechaDesde());
		    			form.setFechaHasta(formBackup.getFechaHasta());
		    			form.setNombre(formBackup.getNombre());
		    			form.setIdEstado(formBackup.getIdEstado());
		    			form.setIdTipoEnvio(formBackup.getIdTipoEnvio());
		    		}
		    	}
	    	}*/

			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar2 = request.getParameter("buscar");
			request.setAttribute("buscar",buscar2);

			/*
	    	if (ses.getAttribute("DATABACKUP")!=null){
	    	    try{
	    	        Hashtable htBackup = (Hashtable)ses.getAttribute("DATABACKUP");

	    	        //parámetro que fuerza la búsqueda
	    	        String buscar = (String)htBackup.get("buscarEnvios");
	    	        if (buscar!=null) request.setAttribute("buscarEnvios","true");

	    	        //rellenamos el formulario
	    	        DefinirEnviosForm formBackup = (DefinirEnviosForm)htBackup.get("enviosForm");
		    		if (formBackup!=null) {
		    	        form.setFechaDesde(formBackup.getFechaDesde());
		    			form.setFechaHasta(formBackup.getFechaHasta());
		    			form.setNombre(formBackup.getNombre());
		    			form.setIdEstado(formBackup.getIdEstado());
		    			form.setComboTipoEnvio(formBackup.getComboTipoEnvio());
		    		}		    	    
	    			// miro a ver si tengo que ejecutar 
	    			//la busqueda una vez presentada la pagina
	    			String buscar2 = request.getParameter("buscar");
	    			request.setAttribute("buscar",buscar2);

	    	    } catch (Exception e){
	    	        e.printStackTrace();
	    	    }
	    	}
			 */

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return("inicio");
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {	    
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;
		form.setNombre("");	   

		//Para volver correctamente desde envios:
		request.getSession().removeAttribute("EnvEdicionEnvio");		
		GenParametrosAdm param = new GenParametrosAdm(userBean);
		try {
			request.setAttribute("smsHabilitado",param.getValor(userBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
		} catch (Exception e) {
			// Si no se mete este parametro se controla en la jsp
		}

		return "nuevo";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = userBean.getTransaction();

		EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));	    
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;

		//Obtenemos los parámetros del formulario

		EnvEnviosBean envioBean = new EnvEnviosBean();	    
		Integer id = null;
		try {
			tx.begin();
			id=envioAdm.getNewIdEnvio(userBean);
			envioBean.setIdEnvio(id);
			envioBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
			envioBean.setDescripcion(form.getNombre());
			envioBean.setIdTipoEnvios(Integer.valueOf(form.getIdTipoEnvio()));
			envioBean.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (!form.getIdPlantillaGeneracion().trim().equals("")){
				envioBean.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			}
			envioBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
			envioBean.setFechaCreacion("SYSDATE");
			envioBean.setGenerarDocumento("N");//Valor por defecto
			envioBean.setImprimirEtiquetas("N");//Valor por defecto
			envioAdm.insert(envioBean);

			Integer idTipoEnvio = Integer.valueOf(form.getIdTipoEnvio());

			envioAdm.copiarCamposPlantilla(Integer.valueOf(userBean.getLocation()), 
					id, 
					idTipoEnvio,
					Integer.valueOf(form.getIdPlantillaEnvios()));


			request.setAttribute("descOperation","messages.inserted.success");
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}
		//"retorno" contiene el valor que devolverá la ventana modal, utilizando "refresh" (genericLoadForm.jsp)
		//en retorno, simulamos la pulsación en editar de una fila
		//debemos montar un string con los campos ocultos separados por comas, 
		//un %, y los campos visibles separados por comas, todo entre comillas simples

		request.setAttribute("retorno",String.valueOf(id.intValue())+","+form.getIdTipoEnvio()+"%"+form.getNombre());
		request.setAttribute("modal","1");        
		return "refresh";

	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, 
			HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException
	{
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

		String fechaDesde = form.getFechaDesde();
		String fechaHasta = form.getFechaHasta();
		//String sIdEstado = form.getIdEstado();
		String idEstado = form.getIdEstado();
		String nombre = form.getNombre();
		String idTipoEnvio = form.getIdTipoEnvio();        
		String idEnvio = form.getIdEnvioBuscar();        
		String idInstitucion = userBean.getLocation();
		String tipoFecha = form.getTipoFecha();

		EnvEnviosAdm enviosAdm = new EnvEnviosAdm (this.getUserBean(request));
		Vector datos = null;
		try {

			/***** PAGINACION*****/
			HashMap databackup=new HashMap();
			if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador)databackup.get("paginador");
				datos=new Vector();


				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");




				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}




				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();
				Paginador resultado = null;
				datos = null;

				resultado = enviosAdm.buscarEnvio(idEnvio,tipoFecha,fechaDesde,fechaHasta,idEstado,
						nombre,idTipoEnvio,idInstitucion);
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					request.getSession().setAttribute("DATAPAGINADOR",databackup);
				}   



			}

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		// Obtengo el pathFTP
		String pathFTP = "";
		GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
		try {
			pathFTP = paramAdm.getValor(userBean.getLocation(),"ENV","URL_FTP_DESCARGA_ENVIOS_ORDINARIOS","");
		} catch (Exception e) {
			//
		}
		request.setAttribute("pathFTP",pathFTP);

		Hashtable htBackup = new Hashtable();
		htBackup.put("enviosForm",form);
		htBackup.put("buscarEnvios","true");        
		ses.setAttribute("DATABACKUP",htBackup);

		request.setAttribute("datos", datos);        

		return "resultado";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions {

		return mostrarRegistro(mapping,formulario,request,response,true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions {

		return mostrarRegistro(mapping,formulario,request,response,false);		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;

		EnvEnviosAdm enviosAdm = new EnvEnviosAdm (this.getUserBean(request));
		EnvEstatEnvioAdm estatEnvioAdm = new EnvEstatEnvioAdm (this.getUserBean(request));

		Vector vOcultos = form.getDatosTablaOcultos(0);		

		String idInstitucion = userBean.getLocation();
		String idEnvio = (String)vOcultos.elementAt(0);
		String idTipoEnvio = (String)vOcultos.elementAt(1);
		Vector resultado=new Vector();
		EnvComunicacionMorososAdm admComunicaMorosos = new EnvComunicacionMorososAdm(userBean);
		UserTransaction tx = userBean.getTransaction();
		Hashtable htEnvioMorosos=null;
		TreeMap tmIdEnviosAActualizar=null;
		try {
			//Hacemos todo esto antes para evitar que vayan los selects dentro de la transaccion
			
			String sql="select * from env_envios e " +
					"  where e.idenvio in (select t.idenviodoc from env_comunicacionmorosos t" +
					"                      where idinstitucion=" +userBean.getLocation()+
					"                      and idenviodoc="+idEnvio+")"+
					"  and idinstitucion="+userBean.getLocation();
			
			resultado = (Vector)enviosAdm.selectGenerico(sql);
			if (resultado!=null && resultado.size()>=1){ 
			htEnvioMorosos = admComunicaMorosos.getEnvioMorosos(idInstitucion, idEnvio);
			tmIdEnviosAActualizar = enviosAdm.getActualizacionIdEnviosMorosos(htEnvioMorosos);
			}
			
			tx.begin();
			estatEnvioAdm.borrarEnvio(idInstitucion,idEnvio, idTipoEnvio);
            enviosAdm.borrarEnvio(idInstitucion,idEnvio,htEnvioMorosos,tmIdEnviosAActualizar);
            tx.commit();
		} catch (Exception e) {
			
			this.throwExcp("messages.elementoenuso.error",e,tx);
		}

		return exitoRefresco("messages.deleted.success",request);
	}


	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;

		Vector vOcultos = form.getDatosTablaOcultos(0);		
		String idEnvio = (String)vOcultos.elementAt(0);
		String idTipoEnvio = (String)vOcultos.elementAt(1);

		//Anhadimos parametros para las pestanhas
		Hashtable htParametros=new Hashtable();
		htParametros.put("idEnvio",idEnvio);
		htParametros.put("idTipoEnvio",idTipoEnvio);
		htParametros.put("acceso",bEditable?"editar":"ver");
		request.setAttribute("envio", htParametros);    


		request.setAttribute("nuevo", "false");	    
		request.setAttribute("buscarEnvios","true");

		return "editar";
	}
	/**
	 * Este metodo no convierte el vector de datos que viene de la jsp y los trasforma en
	 * un hashtable donde las clavs son las persona. Luego los envios a morosos se haran por persona.
	 * @return
	 */
	/*private Hashtable getFacturasPersonaAComunicar(DefinirEnviosForm form){
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


	}*/
	protected String envioModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   

		String idInstitucion = userBean.getLocation();	
		String idSolicitud = form.getIdSolicitud();
		String idFactura = form.getIdFactura();
		String idPersona = form.getIdPersona();
		String desc = form.getDescEnvio();
		String subModo = form.getSubModo();
		String datosEnvios = form.getDatosEnvios();
		String descargar = form.getDescargar();

		String nombreyapellidos = "";
		String idEnvio=null;
		String idTipoEnvio=form.getIdTipoEnvio();
		boolean isEnvioSMS = idTipoEnvio!=null && !idTipoEnvio.equals("") && (Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_SMS || Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_BUROSMS);

		try {
			//SOLICITUD CERTIFICADO:
			// Si el envío procede de una solicitud de certificado, seteamos el tipo
			// de envío según el tipo que se eligió al solicitar el certificado.
			if (subModo!=null && subModo.equalsIgnoreCase("SolicitudCertificado")){
				CerSolicitudCertificadosAdm solCerAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
				Hashtable htPk = new Hashtable();
				htPk.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
				htPk.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
				CerSolicitudCertificadosBean solCerBean = (CerSolicitudCertificadosBean) solCerAdm.selectByPK(htPk).firstElement();
				idTipoEnvio = String.valueOf(solCerBean.getIdTipoEnvios());
				//form.setComboTipoEnvio(idInstitucion+","+idTipoEnvio);
				ArrayList comboTipoEnvio = new ArrayList();
				comboTipoEnvio.add(idInstitucion+","+idTipoEnvio);
				request.setAttribute("comboTipoEnvio",comboTipoEnvio);

				PysProductosInstitucionAdm prodAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
				Integer idTipoProducto = solCerBean.getPpn_IdTipoProducto();
				Long idProducto = solCerBean.getPpn_IdProducto();
				Long idProductoInstitucion = solCerBean.getPpn_IdProductoInstitucion();
				Hashtable htPk2 = new Hashtable();
				htPk2.put(PysProductosInstitucionBean.C_IDINSTITUCION,idInstitucion);
				htPk2.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,idTipoProducto);
				htPk2.put(PysProductosInstitucionBean.C_IDPRODUCTO,idProducto);
				htPk2.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,idProductoInstitucion);
				PysProductosInstitucionBean prodBean = (PysProductosInstitucionBean)prodAdm.selectByPK(htPk2).firstElement();
				String tipoCert = prodBean.getTipoCertificado();
				if (tipoCert.equalsIgnoreCase(PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION)||
						tipoCert.equalsIgnoreCase(PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA)){
					request.setAttribute("ComDil","true");
				}
				//SOLICITUD INCORPORACION:
			} else if (subModo!=null && subModo.equalsIgnoreCase("solicitudIncorporacion")){
				CenSolicitudIncorporacionAdm admSolic = new CenSolicitudIncorporacionAdm(this.getUserBean(request));
				Hashtable hashSolic = new Hashtable();
				hashSolic.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD,idSolicitud);
				Vector aux = admSolic.selectByPK(hashSolic);
				CenSolicitudIncorporacionBean beanSolic = null;
				if (aux!=null && aux.size()>0) {
					beanSolic = (CenSolicitudIncorporacionBean) aux.get(0);
					nombreyapellidos = beanSolic.getNombre()+" "+beanSolic.getApellido1()+" "+beanSolic.getApellido2(); 
				}
			} else if (subModo!=null && subModo.equalsIgnoreCase("envioFactura")){
				FacFacturaAdm admFac = new FacFacturaAdm(this.getUserBean(request));
				Hashtable hashFac = new Hashtable();
				hashFac.put(FacFacturaBean.C_IDINSTITUCION,userBean.getLocation());
				hashFac.put(FacFacturaBean.C_IDFACTURA,idFactura);
				Vector aux = admFac.selectByPK(hashFac);
				FacFacturaBean beanFac = null;
				if (aux!=null && aux.size()>0) {
					beanFac = (FacFacturaBean) aux.get(0);
					desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(),"envios.literal.facturaNumero") + " " + beanFac.getNumeroFactura();
					idPersona=beanFac.getIdPersona().toString();
				}
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesMorosos)){
				//Hashtable htFacturasPersona  = getFacturasPersonaAComunicar(form);
				idPersona = getIdColegiadoUnico(form);

				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS ));//
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null)); 


				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "facturacion.consultamorosos.mail.asunto");

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesDesigna)){
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				idPersona = getIdColegiadoUnicoDesignas(vCampos,userBean);
				//si la persona es null es que hay mas de un colegiado de las distintas designas
				//si solo hay uno comprobaremos que si hay mas de un solicitante(siempre y cuando algun informe sea
				// de tipo solicitante)
				boolean isPersonaUnica = idPersona!=null;
				//Vamos a permitir editar cuando sea solo a colegiados
				
				
				/*
				 //ESTO FUNCIONARIA EL PROBLEMA ES AL ENVIARLOS
				if(isPersonaUnica){
					
					
						Hashtable ht = (Hashtable) vCampos.get(0); 
						String plantillas = (String)ht.get("plantillas");
						EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
						Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas, idInstitucion, userBean);
						
						int numSolicitantes = 0;
						for (int j = 0; j < vPlantillas.size(); j++) {
							AdmInformeBean informeBean = (AdmInformeBean)vPlantillas.get(j);
							boolean isASolicitantes = false;
							boolean isAColegiados = false;
							String tiposDestinatario = informeBean.getDestinatarios();
							if(tiposDestinatario!=null){
								char[] tipoDestinatario = tiposDestinatario.toCharArray();
								for (int k = 0; k < tipoDestinatario.length; k++) {
									
									if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_CENPERSONA)){
										isAColegiados = true;
									}else if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
										isASolicitantes = true;
									}else if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSJUZGADO)){
										//TODO SCS_JUZGADOSJG
									}
								}

							}
							//Comprbamos que al ser a solicitantes haya una persona unica
							
							if(isASolicitantes)
								numSolicitantes += getNumSolicitantesDesignas(vCampos, userBean);
							//si es solicitante unica comprbamos que no vaya a colegiados tambien
							if(numSolicitantes>1){
								isPersonaUnica = false;
								break;
							}
							isPersonaUnica = numSolicitantes+1==1; 
							if(!isPersonaUnica){
								break;
							}

						}
						
						
					}
					*/
				boolean isASolicitantes = false;
				boolean isAprocurador = false;
				if(isPersonaUnica){
					
					
					Hashtable ht = (Hashtable) vCampos.get(0); 
					String plantillas = (String)ht.get("plantillas");
					EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
					Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas, idInstitucion, userBean);
					
					for (int j = 0; j < vPlantillas.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean)vPlantillas.get(j);
						
						String tiposDestinatario = informeBean.getDestinatarios();
						if(tiposDestinatario!=null){
							char[] tipoDestinatario = tiposDestinatario.toCharArray();
							for (int k = 0; k < tipoDestinatario.length; k++) {
								
								if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
									isASolicitantes = true;
									break;
								}else if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPROCURADOR)){
									isAprocurador = true;
									break;
								}
							}

						}
						//Comprbamos que al ser a solicitantes haya una persona unica
						
						
					}
					
					
					
				}
				
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",Boolean.valueOf(isPersonaUnica&&!isASolicitantes&&!isAprocurador));
				//request.setAttribute("isEditarEnvio",Boolean.valueOf(false));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.designas.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesEjg)){
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
				informesAdm.setPersonasEjg(vCampos,userBean);
				Hashtable destinatariosHashtable = informesAdm.getDestinatariosEjg(vCampos);
				
				

				//si la persona es null es que hay mas de un colegiado de las distintas designas
				//si solo hay uno comprobaremos que si hay mas de un solicitante(siempre y cuando algun informe sea
				// de tipo solicitante)
				boolean isDestinatarioUnico = destinatariosHashtable!=null && destinatariosHashtable.size()==1;
				//Vamos a permitir editar cuando sea solo a colegiados
				
				boolean isASolicitantes = false;
				if(isDestinatarioUnico &&!isEnvioSMS){
					
					
					Hashtable ht = (Hashtable) vCampos.get(0); 
					String plantillas = (String)ht.get("plantillas");
					
					Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas, idInstitucion, userBean);
					
					for (int j = 0; j < vPlantillas.size(); j++) {
						AdmInformeBean informeBean = (AdmInformeBean)vPlantillas.get(j);
						
						String tiposDestinatario = informeBean.getDestinatarios();
						if(tiposDestinatario!=null){
							char[] tipoDestinatario = tiposDestinatario.toCharArray();
							for (int k = 0; k < tipoDestinatario.length; k++) {
								
								if(String.valueOf(tipoDestinatario[k]).equalsIgnoreCase(AdmInformeBean.TIPODESTINATARIO_SCSPERSONAJG)){
									isASolicitantes = true;
									break;
								}
							}

						}
						//Comprbamos que al ser a solicitantes haya una persona unica
						
						
					}
					
					
					
				}
				
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",Boolean.valueOf(isDestinatarioUnico&&!isEnvioSMS));
				
				if(form.getIdTipoEnvio()!=null && !form.getIdTipoEnvio().equals("")){
					ArrayList comboTipoEnvio = new ArrayList();
					comboTipoEnvio.add(idInstitucion+","+form.getIdTipoEnvio());
					request.setAttribute("comboTipoEnvio",comboTipoEnvio);
				}
				
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.ejg.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesJustificacion)){
				
				Hashtable htPersonas = new Hashtable();
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				Hashtable ht = (Hashtable) vCampos.get(0); 
				idPersona = (String) ht.get("idPersona");
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));//
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null)); 


				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.justificacion.asunto");

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesCenso)){
				idPersona = getIdColegiadoUnico(form);

				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.censo.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesListadoGuardias)){
				request.setAttribute("exitenDatos","");
				try{
					idPersona = getIdColegiados(form, request);

				} catch (SIGAException e) {
					request.setAttribute("exitenDatos","messages.general.error.noExistenDatos");
				}
				datosEnvios = form.getDatosEnvios();
	    		Hashtable backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	    		request.setAttribute("DATABACKUP",backupHash);
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.lista.envio.guardias");

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesPagoColegiados)){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.pagos.envio.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isDescargar",new Boolean(descargar!=null &&descargar.equals("1")&&!isEnvioSMS));
				
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.facturacion.envio.asunto");


			}
			else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesExpedientes)){
				idPersona = getIdColegiadoUnico(form);

				request.setAttribute("isDescargar",new Boolean(idPersona!=null&&!isEnvioSMS));
				form.setDescargar((idPersona!=null)?"1":"");
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				// RGG también se añade que, incluso siendo un aúnica persona, no sea asolicitantes
				MasterReport masterReport = new  MasterReport(); 
				EnvioInformesGenericos informesAdm = new EnvioInformesGenericos();
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				Hashtable datosInforme = null;
				if (vCampos.size()>0) {
					datosInforme = (Hashtable) vCampos.get(0);
				}
				String plantillas = (String) datosInforme.get("plantillas");
				Vector vPlantillas = informesAdm.getPlantillasInforme(plantillas,userBean.getLocation(),userBean);
				boolean aSolicitantes = informesAdm.esAlgunaASolicitantes(vPlantillas);

				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null && !aSolicitantes));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.genericos.expedientes.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase("pagoColegiados")){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "informes.sjcs.pagos.envio.asunto");


			}else if (subModo!=null && subModo.equalsIgnoreCase("certificadoIRPF")){
				idPersona = getIdPersonaUnica(form);
				//ATENCION. Se habilitara siempre y cuando solo haya el envio a una unicaPersona.
				request.setAttribute("isEditarEnvio",new Boolean(idPersona!=null));
				desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(), "gratuita.retencionesIRPF.informe.envio.asunto");


			}

			//obtener idEnvio
			EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
			//idEnvio = String.valueOf(envAdm.getNewIdEnvio(idInstitucion));


			//obtener nombreyapellidos
			CenPersonaAdm persAdm = new CenPersonaAdm(this.getUserBean(request));		
			if ( (idPersona!=null && !idPersona.equals("")) && 
					(subModo!=null && !subModo.equalsIgnoreCase("solicitudIncorporacion")
					) )
				nombreyapellidos = persAdm.obtenerNombreApellidos(idPersona);

			/*// RGG como no entiendo la condicion de arriba, anhado la que necesita
			//JTA Yo tampoco! no obstante añado el or con envio morosos
			if ( idPersona!=null && !idPersona.equals(""))
					nombreyapellidos = persAdm.obtenerNombreApellidos(idPersona);*/


			//Nombre:
			// Antes: String nombre = idEnvio + "-" + desc + "-" + nombreyapellidos;
			// Ahora INC 4403:
			String nombre = ""  + desc + " " + nombreyapellidos;
			//////////////////////////////

			if (nombre.length()>100){
				nombre = nombre.substring(0,99);
			}
			form.setNombre(nombre);

			//Fecha Programada:
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);

			if (form.getModo().equalsIgnoreCase("envioModal"))
			{
				sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			}

			else
			{
				sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_SPANISH);
			}

			Calendar cal = Calendar.getInstance();   
			String fecha;
			fecha = sdf.format(cal.getTime()).toString();
			form.setFechaProgramada(fecha);


			//request.setAttribute(EnvEnviosBean.C_IDENVIO,idEnvio);
			request.setAttribute(CerSolicitudCertificadosBean.C_IDPERSONA_DES,idPersona);
			request.setAttribute(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
			request.setAttribute(FacFacturaBean.C_IDFACTURA,idFactura);
			request.setAttribute("subModo",subModo);
			request.setAttribute("datosEnvios",datosEnvios);
			GenParametrosAdm param = new GenParametrosAdm(userBean);
			request.setAttribute("smsHabilitado",param.getValor(idInstitucion, "ENV", "HABILITAR_SMS_BUROSMS", "N"));
		
		
		} catch (SIGAException e) {
			throwExcp(e.getLiteral(),new String[] {},e,null);
		} catch (Exception ex) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},ex,null);
		}

		return "envioModal";
	}

	protected String envioModalCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN"))); 

		try {
			//SOLICITUD CERTIFICADO:

			//Fecha Programada:
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);

			Calendar cal = Calendar.getInstance();   
			String fecha;
			fecha = sdf.format(cal.getTime()).toString();
			form.setFechaProgramada(fecha);
			GenParametrosAdm param = new GenParametrosAdm(userBean);
			request.setAttribute("smsHabilitado",param.getValor(userBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return "envioModalCertificado";
	}

	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarEnvioModal (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		UserTransaction tx = userBean.getTransactionPesada();
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		boolean tieneDireccion = true;
		boolean isEnvioBatch = false;
		String subModo = form.getSubModo();
		try {

			// obtener idInstitucion
			String idInstitucion = userBean.getLocation();
			// obtener nombre
			String nombreEnvio = form.getNombre();
			// obtener tipoEnvio
			String idTipoEnvio = form.getIdTipoEnvio();
			boolean isEnvioSMS = Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_BUROSMS || Integer.parseInt(idTipoEnvio)==EnvEnviosAdm.TIPO_SMS;
			
			// obtener plantilla
			
			if(form.getIdPlantillaEnvios().equals("predefinida"))
				form.setIdPlantillaEnvios("");
			String idPlantilla = form.getIdPlantillaEnvios();
			//obtener plantilla de generacion
			String idPlantillaGeneracion = form.getIdPlantillaGeneracion();

			// obtener fechaProgramada
			String fechaProgramada = null;
			String fechaProg = form.getFechaProgramada() + " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();

			String language = userBean.getLanguage();
			String format = language.equalsIgnoreCase("EN")?ClsConstants.DATE_FORMAT_LONG_ENGLISH:ClsConstants.DATE_FORMAT_LONG_SPANISH;		    
			GstDate gstDate = new GstDate();
			if (fechaProg != null && !fechaProg.equals("")) {
				Date date = gstDate.parseStringToDate(fechaProg,format,request.getLocale());
				//A peticion de Luis pedro retraso 15 minutos el envio
				date.setTime(date.getTime()+900000);
				//date.
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				fechaProgramada = sdf.format(date);
			} else
				fechaProgramada = null;

			// obtener idSolicitud
			String idSolicitud = form.getIdSolicitud();
			// obtener idFactura
			String idFactura = form.getIdFactura();
			//obtener idPersona
			String idPersona = form.getIdPersona();
			if (idPersona!=null && idPersona.trim().equals("")) {
				idPersona = null;
			}

			
			
			
			String idEnvio = null;
			// obtener idEnvio
			if(!subModo.equals(EnvioInformesGenericos.comunicacionesCenso)&&
					!subModo.equals(EnvioInformesGenericos.comunicacionesDesigna)&&
					!subModo.equals(EnvioInformesGenericos.comunicacionesEjg)&&
					!subModo.equals(EnvioInformesGenericos.comunicacionesExpedientes)&&
					!subModo.equals(EnvioInformesGenericos.comunicacionesMorosos)&&
					!subModo.equals(EnvioInformesGenericos.comunicacionesPagoColegiados)&&
					!subModo.equals(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)&&
					!subModo.equals("certificadoIRPF") && !subModo.equals(EnvioInformesGenericos.comunicacionesListadoGuardias)){
					idEnvio = form.getIdEnvio();
				if (idEnvio.equalsIgnoreCase("")){
					EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
					idEnvio = String.valueOf(envAdm.getNewIdEnvio(idInstitucion));
				}
			}
			
			


			//***** Creamos bean de envio *****
			EnvEnviosBean enviosBean = new EnvEnviosBean();
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));

			enviosBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			if(idEnvio!=null)
				enviosBean.setIdEnvio(Integer.valueOf(idEnvio));
			enviosBean.setDescripcion(nombreEnvio);
			enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
			if(!isEnvioSMS){
				enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
				if (idPlantillaGeneracion!=null && !idPlantillaGeneracion.equals("")) {
					enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
				} else {
					enviosBean.setIdPlantilla(null);
				}
			}
			enviosBean.setFechaProgramada(fechaProgramada);
			enviosBean.setFechaCreacion("SYSDATE");
			enviosBean.setGenerarDocumento(paramAdm.getValor(idInstitucion,"ENV","GENERAR_DOCUMENTO_ENVIO","C"));
			enviosBean.setImprimirEtiquetas(paramAdm.getValor(idInstitucion,"ENV","IMPRIMIR_ETIQUETAS_ENVIO","1"));
			if (fechaProgramada==null || fechaProgramada.equals(""))
				enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
			else{
				if(idTipoEnvio!=null &&!idTipoEnvio.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))			
					enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
				else
					enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
			}
			


			//El vector vDocs se rellenará en función del subModo.
			Vector vDocs = null;


			CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);

			tx.begin();

			if (subModo!=null && subModo.equalsIgnoreCase("SolicitudCertificado")){

				boolean existe = true;
				//System.out.println("paraconsejo="+request.getParameter("paraConsejo"));
				// control de direcciones

				// Obtenemos el certificado
				Vector v = admCer.select("WHERE "+CerSolicitudCertificadosBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+CerSolicitudCertificadosBean.C_IDSOLICITUD+"="+idSolicitud);
				CerSolicitudCertificadosBean beanCer = null;
				if (v!=null && v.size()>0) {
					beanCer = (CerSolicitudCertificadosBean) v.get(0);
				}
				PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.getUserBean(request));
				Vector v2=admProd.select("WHERE "+PysProductosInstitucionBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+"="+beanCer.getPpn_IdTipoProducto()+" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+"="+beanCer.getPpn_IdProducto()+" AND "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+"="+beanCer.getPpn_IdProductoInstitucion());
				PysProductosInstitucionBean beanProd=null;
				if (v2!=null && v2.size()>0) {
					beanProd = (PysProductosInstitucionBean) v2.get(0);
				}

				if (beanProd.getTipoCertificado().equalsIgnoreCase("D"))  {
					// DILIGENCIA (CGAE)
					// en caso de uno se pregunta, si no se pone directamente la institucion de cgae
					if (request.getParameter("paraConsejo")==null) {
						// Proceso normal
						// Cuando es un consejo o cgae comprobamos si existe la direccion en el colegio origen. 
						// Si no existe enviamos un mensaje de confirmacion
						boolean esDeConsejo=admCer.esConsejo(idInstitucion);
						if (esDeConsejo) {
							existe = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
						}
						if (!existe) {

							// Si no existe lo tenemos que buscar en CGAE en lugar de en Origen.
							// Y si no existe tammbien en CGAE (Esta pregunta solamente avisa)
							tx.rollback();

							request.setAttribute("PREG_idTipoEnvio",form.getIdTipoEnvio());
							request.setAttribute("PREG_idSolicitud",form.getIdSolicitud());
							request.setAttribute("PREG_idFactura",form.getIdFactura());
							request.setAttribute("PREG_idPersona",form.getIdPersona());
							request.setAttribute("PREG_idEnvio",form.getIdEnvio());
							request.setAttribute("PREG_idTipoEnvio",form.getIdTipoEnvio());
							request.setAttribute("PREG_idTipoEnvio",form.getIdTipoEnvio());
							request.setAttribute("PREG_nombreEnvio",nombreEnvio);
							request.setAttribute("PREG_idPlantilla",idPlantilla);
							request.setAttribute("PREG_idPlantillaGeneracion",idPlantillaGeneracion);
							request.setAttribute("PREG_fechaProgramada",form.getFechaProgramada());
							request.setAttribute("PREG_existe",new Boolean(existe).toString());
							request.setAttribute("PREG_subModo",subModo);

							return "preguntaDireccionEnvio";
						}
					} else {
						//existe = new Boolean(request.getParameter("PREG_existe")).booleanValue();
						// RGG Nos da igual la respuesta, se coge siempre la de CGAE
						existe = false;
					}
				} else {
					// si es certificado o comunicacion no se pregunta.
					// existe = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
					// RGG me da igual que exista o no, se coge siempre la de CGAE
					existe = false;
				}

				/*
				// Proceso normal
				// Cuando es un consejo o cgae comprobamos si existe la direccion en el colegio origen. 
				boolean esDeConsejo=admCer.esConsejo(idInstitucion);
				if (esDeConsejo) {
					existe = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
				}
				 */				
				vDocs = tratarSolicitudCertificado(idInstitucion,idSolicitud,this.getUserBean(request),subModo);
				Envio envio = new Envio(enviosBean,userBean);
				tieneDireccion = envio.generarEnvioCertificado(idPersona,vDocs,idSolicitud,enviosBean,existe);	

				// Enviado. Ahora ponemos la fecha de envio
				beanCer.setFechaEnvio("SYSDATE");
				if (!admCer.updateDirect(beanCer)) {
					throw new ClsExceptions("No se ha podido actualizar la fecha de envío. Error: "+admCer.getError());
				}


			} else if (subModo!=null && subModo.equalsIgnoreCase("envioFactura")){
				vDocs = tratarFactura(request, idInstitucion,idFactura,this.getUserBean(request),subModo);
				//Generamos el envío del certificado:
				//Para idinstitucion=CGAE(2000) se realiza el commit aunque no tenga direccion:
				Envio envio = new Envio(enviosBean,userBean);
				tieneDireccion = envio.generarEnvioFactura(idPersona,vDocs,idFactura);	
			} else if (subModo!=null && subModo.equalsIgnoreCase("solicitudIncorporacion")){
				EnvDestinatariosBean dest = tratarSolicitudIncorporacion(idSolicitud,this.getUserBean(request), new Integer(idInstitucion),idTipoEnvio);

				// RGG 30-09-2005 Compruebo si es cliente
				//CenClienteAdm cliAdm = new CenClienteAdm(this.getUserName(request));
				//if (cliAdm.existeCliente(dest.getIdPersona(),dest.getIdInstitucion())==null) {
				//	throw new SIGAException ("messages.envios.error.noCliente"); 
				//}

				dest.setIdEnvio(Integer.valueOf(idEnvio));
				//Generamos el envío de la solicitud de incorporacion:
				Envio envio = new Envio(enviosBean,userBean);
				envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA, vDocs);
				envio.addDestinatarioIndividualDocAdjuntos(dest,null,true);
				// RGG envio.addDestinatarioIndividual(dest,null,true);
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesMorosos)){


				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionMorosos(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesExpedientes)){


				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionExpedientes(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				


			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesDesigna)){


			
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionDesignas(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
			
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesEjg)){


				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					
					envioInformesGenericos.gestionarComunicacionEjg(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesPagoColegiados)){
				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionPagoColegiados(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)){
				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionFacturacionColegiados(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
								
				
			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesCenso)){
				
				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionCenso(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesListadoGuardias)){
				
				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionListadoGuardias(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				

			}else if (subModo!=null && subModo.equalsIgnoreCase(EnvioInformesGenericos.comunicacionesJustificacion)){
				
				
					EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
					envioInformesGenericos.gestionarComunicacionJustificaciones(form,  request.getLocale(), userBean);
					idEnvio = form.getIdEnvio();
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				

			}else if (subModo!=null && subModo.equalsIgnoreCase("certificadoIRPF")){


				
					// Obtenemos la información pertinente relacionada con los certificados
					idPersona = getIdPersonaUnica(form);
					InformeCertificadoIRPF informeCertificado = new InformeCertificadoIRPF();
					if(idPersona!=null){
						Vector vCampos = informeCertificado.obtenerDatosFormulario(form);

						String periodo = null;
						String anyoInformeIRPF = null;
						String idioma = null;
						String plantillas = null;
						for (int i = 0; i < vCampos.size(); i++) {
							Hashtable ht = (Hashtable) vCampos.get(i); 
							idPersona= (String)ht.get("idPersona");
							periodo = (String)ht.get("periodo");
							anyoInformeIRPF= (String)ht.get("anyoInformeIRPF");
							idInstitucion= (String)ht.get("idInstitucion");
							idioma = (String)ht.get("idioma");
							plantillas = (String) ht.get("plantillas");


						} 

						Vector vDocumentos = informeCertificado.getDocumentosAEnviar(plantillas, periodo, anyoInformeIRPF, idPersona, idioma, idInstitucion, userBean);

						//envio = new Envio(userBean,nombreEnvio);


						if (idEnvio ==null ||idEnvio.equalsIgnoreCase("")){
							EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
							idEnvio = String.valueOf(envAdm.getNewIdEnvio(idInstitucion));
						}
						enviosBean.setIdEnvio(Integer.valueOf(idEnvio));
						
						// Bean envio
						Envio envio = new Envio(enviosBean,userBean);
						//enviosBean = envio.getEnviosBean();
						enviosBean.setDescripcion(enviosBean.getIdEnvio()+" "+enviosBean.getDescripcion());
						// trunco la descripción
						if (enviosBean.getDescripcion().length()>200)  enviosBean.setDescripcion(enviosBean.getDescripcion().substring(0,99));
						// Preferencia del tipo de envio si el usuario tiene uno:
						enviosBean.setIdTipoEnvios(new Integer(idTipoEnvio));

						enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
						if (idPlantillaGeneracion!=null && !idPlantillaGeneracion.equals("")) {
							enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
						} else {
							enviosBean.setIdPlantilla(null);
						}



						//Vector vDocumentos = (Vector) htDocumentosPersona.get(idPersona);

						// Genera el envio:
						envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,vDocumentos);



					}else{
						//idInstitucion = userBean.getLocation();
						Vector vCampos = informeCertificado.obtenerDatosFormulario(form);

						String periodo = null;
						String anyoInformeIRPF = null;
						String idioma = null;
						String plantillas = null;
						EnvEnvioProgramadoAdm envioProgramadoAdm  = new EnvEnvioProgramadoAdm(userBean);
						EnvProgramIRPFAdm programIRPFAdm = new EnvProgramIRPFAdm(userBean);
						EnvEnvioProgramadoBean envioProgramado = null;
						EnvProgramIRPFBean programIRPF = null;
						for (int i = 0; i < vCampos.size(); i++) {
							Hashtable ht = (Hashtable) vCampos.get(i); 
							idPersona = (String) ht.get("idPersona");
							idInstitucion = (String) ht.get("idInstitucion");
							periodo = (String) ht.get("periodo");
							anyoInformeIRPF = (String) ht.get("anyoInformeIRPF");
							idioma = (String) ht.get("idioma");
							plantillas = (String) ht.get("plantillas");

							envioProgramado = new EnvEnvioProgramadoBean();

							envioProgramado.setIdEnvio(envioProgramadoAdm.getNewIdEnvio(idInstitucion));
							envioProgramado.setIdInstitucion(new Integer(idInstitucion));
							envioProgramado.setIdTipoEnvios(new Integer(idTipoEnvio));
							envioProgramado.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
							if (idPlantillaGeneracion!=null && !idPlantillaGeneracion.equals("")) {
								envioProgramado.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
							} else {
								envioProgramado.setIdPlantilla(null);
							}

							envioProgramado.setNombre(nombreEnvio);
							envioProgramado.setEstado(ClsConstants.DB_FALSE);
							envioProgramado.setFechaProgramada(fechaProgramada);

							programIRPF = new EnvProgramIRPFBean();
							programIRPF.setIdProgram(programIRPFAdm.getNewIdProgramIrpf(idInstitucion));
							programIRPF.setIdEnvio(envioProgramado.getIdEnvio());
							programIRPF.setIdInstitucion(envioProgramado.getIdInstitucion());
							programIRPF.setIdPersona(new Long(idPersona));
							programIRPF.setIdioma(new Integer(idioma));
							programIRPF.setEstado(ClsConstants.DB_FALSE);
							programIRPF.setPeriodo(new Integer(periodo));
							programIRPF.setAnyoIRPF(new Integer(anyoInformeIRPF));
							programIRPF.setPlantillas(plantillas);

							envioProgramadoAdm.insert(envioProgramado);
							programIRPFAdm.insert(programIRPF);

							//Obtenemos el bean del envio:






						}
						isEnvioBatch = true;
					}

				
			}  
			else {
				//Generamos el envío
				Envio envio = new Envio(enviosBean,userBean);
				envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,null);
			}

			if (!tieneDireccion) {
				throw new SIGAException("messages.envio.errorNoDireccion");
			}

			if(idEnvio!=null&&!idEnvio.equals("")){
				Hashtable htEnvio = new Hashtable();
				htEnvio.put(EnvEnviosBean.C_IDTIPOENVIOS,idTipoEnvio);
				htEnvio.put(EnvEnviosBean.C_IDENVIO,idEnvio);
				htEnvio.put(EnvEnviosBean.C_DESCRIPCION,nombreEnvio);			
				request.setAttribute("envio",htEnvio);
			}

			tx.commit();
			if(isEnvioBatch)
				SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoGeneracionEnvio);

		}
		catch (ClsExceptions e){
			this.throwExcp(e.getMessage(),new String[] {"modulo.envios"}, e, tx);
		}
		catch (Exception e){
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}

		if (form.getEditarEnvio()!=null && form.getEditarEnvio().equalsIgnoreCase("true")){
			// JTA Borramos el paginador Paginador ya que es el mismo que el de envios 
			// Asi evitamos ClassCastException
			if(subModo!=null && (subModo.equalsIgnoreCase("certificadoIRPF")||subModo.equalsIgnoreCase("pagoColegiados")))
				borrarPaginador(request,this.paginador);
			return "seleccionEnvio";
		} else {
			return exitoModal("messages.inserted.success",request);
		}
			} 

	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarEnvioModalCertificado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		UserTransaction tx = userBean.getTransactionLigera();
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		boolean tieneDireccion = true;
		String mensaje="";
		int contador=0;
		Hashtable codigos=new Hashtable();
		int contador1=0;
		Hashtable codigos1=new Hashtable();
		int contador2=0;
		Hashtable codigos2=new Hashtable();

		try {

			// obtener tipoEnvio
			String idTipoEnvio = form.getIdTipoEnvio();
			// obtener plantilla
			String idPlantilla = form.getIdPlantillaEnvios();
			//obtener plantilla de generacion
			String idPlantillaGeneracion = form.getIdPlantillaGeneracion();

			// Obtenemos el coegio destino
			String colegio = form.getColegio();


			// obtener fechaProgramada
			String fechaProgramada = null;
			String fechaProg = form.getFechaProgramada() + " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();

			String language = userBean.getLanguage();
			String format = language.equalsIgnoreCase("1")?ClsConstants.DATE_FORMAT_LONG_ENGLISH:ClsConstants.DATE_FORMAT_LONG_SPANISH;		    
			GstDate gstDate = new GstDate();
			if (fechaProg != null && !fechaProg.equals("")) {
				Date date = gstDate.parseStringToDate(fechaProg,format,request.getLocale());
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				fechaProgramada = sdf.format(date);
			} else {
				fechaProgramada = null;
			}


			StringTokenizer st = null;
			String tok=form.getIdsParaEnviar();
			try {
				st = new StringTokenizer(tok, ";");
			} catch (java.util.NoSuchElementException nee) {
				// solamente existe un token
			}
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this
					.getUserBean(request));
			EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this
					.getUserBean(request));

			// Obtenemos el certificado
			CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(
					userBean);

			
			int contErrores=0;
			while (st.hasMoreElements()) {
				contador = 0;
				contador1 = 0;
				contador2 = 0;

				String nombreEnvioError = "";
				String nombreSolicitud = "";
				try {
					Integer idEnvio = enviosAdm.getNewIdEnvio(
							userBean.getLocation());
					
					tx.begin();

					String to = (String) st.nextToken();
					StringTokenizer st2 = new StringTokenizer(to, "%%");

					String fechaSolicitud = st2.nextToken();
					String idSolicitud = st2.nextToken();
					st2.nextToken();
					st2.nextToken();
					st2.nextToken();
					st2.nextToken();
					String idInstitucion = st2.nextToken();
					// nos saltamos la persona
					st2.nextToken();
					st2.nextToken();

					nombreSolicitud = "[Institucion:" + idInstitucion
							+ "][Solicitud:" + idSolicitud + "][fecha:"
							+ fechaSolicitud + "]";

					// ***** Creamos bean de envio *****
					EnvEnviosBean enviosBean = new EnvEnviosBean();
										contador++;
					codigos.put(new Integer(contador), idInstitucion);
					String where = " WHERE "
							+ CerSolicitudCertificadosBean.C_IDINSTITUCION
							+ "=:" + contador;
					contador++;
					codigos.put(new Integer(contador), idSolicitud);
					where += " AND "
							+ CerSolicitudCertificadosBean.C_IDSOLICITUD + "=:"
							+ contador;

					Vector v = admCer.selectBind(where, codigos);
					CerSolicitudCertificadosBean beanCer = null;
					if (v != null && v.size() > 0) {
						beanCer = (CerSolicitudCertificadosBean) v.get(0);
					}
					// Obtenemos el producto certificado
					PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(
							this.getUserBean(request));
					contador1++;
					codigos1.put(new Integer(contador1), idInstitucion);
					String where1 = " WHERE "
							+ PysProductosInstitucionBean.C_IDINSTITUCION
							+ "=:" + contador1;
					contador1++;
					codigos1.put(new Integer(contador1), String.valueOf(beanCer
							.getPpn_IdTipoProducto().intValue()));
					where1 += " AND "
							+ PysProductosInstitucionBean.C_IDTIPOPRODUCTO
							+ "=:" + contador1;
					contador1++;
					codigos1.put(new Integer(contador1), String.valueOf(beanCer
							.getPpn_IdProducto().longValue()));
					where1 += " AND "
							+ PysProductosInstitucionBean.C_IDPRODUCTO + "=:"
							+ contador1;
					contador1++;
					codigos1.put(new Integer(contador1), String.valueOf(beanCer
							.getPpn_IdProductoInstitucion().longValue()));
					where1 += " AND "
							+ PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION
							+ "=:" + contador1;
					Vector v2 = admProd.selectBind(where1, codigos1);
					PysProductosInstitucionBean beanProd = null;
					if (v2 != null && v2.size() > 0) {
						beanProd = (PysProductosInstitucionBean) v2.get(0);
					}

					// dexcidimos sobre cual es la institucion para enviar
					String idInstitucionAEnviar = "";
					if (!beanProd.getTipoCertificado().equals("C")) {
						// si es certificado siempre al origen, si no, segun
						// seleccion en pantalla
						if (colegio.equals("d")) {
							// destinatario el colegio destino
							idInstitucionAEnviar = beanCer
									.getIdInstitucionDestino().toString();
						} else {
							// destinatario el colegio origen
							idInstitucionAEnviar = beanCer
									.getIdInstitucionOrigen().toString();
						}
					} else {
						if (beanCer.getIdInstitucionOrigen() == null) {
							// Es certificado y no han definido colegio
							// presentador
							throw new ClsExceptions(
									"Es un certificado y no se ha definido colegio presentador (No se envía).");
						} else {
							// destinatario el colegio origen / PRESENTADOR
							idInstitucionAEnviar = beanCer
									.getIdInstitucionOrigen().toString();
						}
					}

					// obtenemos el nombre del colegio
					CenInstitucionAdm admInst = new CenInstitucionAdm(this
							.getUserBean(request));
					contador2++;
					codigos2.put(new Integer(contador2), idInstitucionAEnviar);
					String where2 = "WHERE "
							+ CenInstitucionBean.C_IDINSTITUCION + "=:"
							+ contador2;

					Vector v3 = admInst.selectBind(where2, codigos2);
					CenInstitucionBean beanInst = null;
					if (v3 != null && v3.size() > 0) {
						beanInst = (CenInstitucionBean) v3.get(0);
					}
					// Obtenemos la persona de la isntitucion
					String idPersona = beanInst.getIdPersona().toString();

					enviosBean.setIdInstitucion(Integer.valueOf(idInstitucion));

					enviosBean.setIdEnvio(idEnvio);

					// formo el nombre del envio
					// Antes: // String nombreEnvio=idEnvio+" -
					// "+UtilidadesString.getMensajeIdioma(userBean,"messages.envios.envioCertificadosMasivo")+"
					// - "+beanInst.getAbreviatura();
					// String
					// nombreEnvio=idEnvio+"-"+beanCer.getDescripcion()+"-"+beanInst.getAbreviatura();
					// Ahora INC 4403:
					String nombreEnvio = "SOL" + idSolicitud + "-"
							+ beanProd.getDescripcion() + "-"
							+ beanInst.getAbreviatura();
					// ////////////////////////////

					nombreEnvioError = nombreEnvio;
					enviosBean.setDescripcion(nombreEnvio);
					// trunco la descripción
					if (enviosBean.getDescripcion().length() > 200)
						enviosBean.setDescripcion(enviosBean.getDescripcion()
								.substring(0, 99));

					enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
					enviosBean.setIdPlantillaEnvios(Integer
							.valueOf(idPlantilla));
					if (idPlantillaGeneracion != null
							&& !idPlantillaGeneracion.equals("")) {
						enviosBean.setIdPlantilla(Integer
								.valueOf(idPlantillaGeneracion));
					} else {
						enviosBean.setIdPlantilla(null);
					}
					enviosBean.setFechaProgramada(fechaProgramada);
					enviosBean.setFechaCreacion("SYSDATE");
					enviosBean.setGenerarDocumento(paramAdm.getValor(
							idInstitucion, "ENV", "GENERAR_DOCUMENTO_ENVIO",
							"C"));
					enviosBean.setImprimirEtiquetas(paramAdm.getValor(
							idInstitucion, "ENV", "IMPRIMIR_ETIQUETAS_ENVIO",
							"1"));
					if (fechaProgramada == null || fechaProgramada.equals(""))
						enviosBean.setIdEstado(new Integer(
								EnvEnviosAdm.ESTADO_INICIAL));
					else{
						if(idTipoEnvio!=null &&!idTipoEnvio.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO))			
							enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
						else
							enviosBean.setIdEstado(new Integer(
								EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
					}
					Envio envio = new Envio(enviosBean, userBean);

					// El vector vDocs se rellenará en función del subModo.
					Vector vDocs = null;

					vDocs = tratarSolicitudCertificado(idInstitucion,
							idSolicitud, this.getUserBean(request),
							"SolicitudCertificado");
					tieneDireccion = envio.generarEnvioCertificado(idPersona,
							vDocs, idSolicitud, enviosBean, true);

					if (!tieneDireccion) {
						throw new ClsExceptions(
								"El destinatario no tiene dirección. idPersona:"
										+ idPersona);
					}

					// Enviado. Ahora ponemos la fecha de envio
					beanCer.setFechaEnvio("SYSDATE");
					if (!admCer.updateDirect(beanCer)) {
						throw new ClsExceptions(
								"No se ha podido actualizar la fecha de envío. envio: "
										+ nombreEnvioError + " Error: "
										+ admCer.getError());
					}

					tx.commit();

				} catch (Exception e) {

					tx.rollback();

					contErrores++;
					ClsLogging.writeFileLog("----- ERROR ENVIO -----", 4);
					ClsLogging.writeFileLogError(
							"ERROR EN ENVIO MASIVO. SOLICITUD: "
									+ nombreSolicitud + " Error: "
									+ e.toString(), e, 3);
				}
			}

			if (contErrores == 0) {
				mensaje = "messages.inserted.success";
			} else {
				mensaje = UtilidadesString.getMensaje(
						"messages.envios.envioCertificadosMasivo.success",
						new String[] { new Integer(contErrores).toString() },
						userBean.getLanguage());
			}

		} catch (Exception e) {
			this.throwExcp("messages.general.error",
					new String[] { "modulo.envios" }, e, tx);
		}
		return exitoModal(mensaje, request);
	} 


	private EnvDestinatariosBean tratarSolicitudIncorporacion(String idSolicitud, UsrBean userBean, Integer idInstitucion, String idTipoEnvio)
	throws ClsExceptions, SIGAException {

		CenPersonaAdm admPer = new CenPersonaAdm(userBean);
		CenClienteAdm admCli = new CenClienteAdm(userBean);

		// obtenemos la solicitud
		CenSolicitudIncorporacionAdm admSolic = new CenSolicitudIncorporacionAdm(userBean);
		Hashtable hashSolic = new Hashtable();
		hashSolic.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD,idSolicitud);
		Vector aux = admSolic.selectByPK(hashSolic);
		CenSolicitudIncorporacionBean beanSolic = null;
		if (aux!=null && aux.size()>0) {
			beanSolic = (CenSolicitudIncorporacionBean) aux.get(0);
		}

		CenPersonaBean beanPer = null;
		Long idPersona = null;

		try {
			beanPer = admPer.getPersonaSolicitud(beanSolic.getNumeroIdentificador(), beanSolic.getNombre(), beanSolic.getApellido1(), beanSolic.getApellido2());
			if (beanPer==null) {
				throw new SIGAException("NO PERSONA");
			}
			idPersona = beanPer.getIdPersona();
			Hashtable hashCli = new Hashtable();
			hashCli.put(CenClienteBean.C_IDPERSONA,beanPer.getIdPersona());
			hashCli.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
			Vector aux2 = admCli.selectByPK(hashCli);
			if (aux2==null || aux2.size()==0) {
				// no existe el cliente
				idPersona = null;
			}
		} catch (SIGAException e) {
			// no existe la persona
		}

		EnvDestinatariosBean salida = new EnvDestinatariosBean();

		if (beanPer!=null) {
			salida.setIdPersona(beanPer.getIdPersona());
			salida.setApellidos1(beanSolic.getApellido1());
			salida.setApellidos2(beanSolic.getApellido2());
			salida.setNombre(beanSolic.getNombre());
			salida.setNifcif(beanSolic.getNumeroIdentificador());
			salida.setIdInstitucion(beanSolic.getIdInstitucion());

			// la persona existe , entonces cogemos su direccion preferente
			EnvEnviosAdm enviosAdm = new EnvEnviosAdm(userBean);
			Vector direcciones = null;
			try {
				direcciones = enviosAdm.getDirecciones(idInstitucion.toString(),idPersona.toString(),idTipoEnvio);
				if (direcciones!=null && direcciones.size()>0) {
					Hashtable hashDir = (Hashtable) direcciones.get(0); 
					if (hashDir!=null) {
						salida.setCodigoPostal((String)hashDir.get(CenDireccionesBean.C_CODIGOPOSTAL));
						salida.setCorreoElectronico((String)hashDir.get(CenDireccionesBean.C_CORREOELECTRONICO));
						salida.setDomicilio((String)hashDir.get(CenDireccionesBean.C_DOMICILIO));
						salida.setFax1((String)hashDir.get(CenDireccionesBean.C_FAX1));
						salida.setFax2((String)hashDir.get(CenDireccionesBean.C_FAX2));
						salida.setIdPais((String)hashDir.get(CenDireccionesBean.C_IDPAIS));
						if (salida.getIdPais().equals("")) salida.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
						salida.setIdPoblacion((String)hashDir.get(CenDireccionesBean.C_IDPOBLACION));
						salida.setPoblacionExtranjera((String)hashDir.get(CenDireccionesBean.C_POBLACIONEXTRANJERA));
						salida.setIdProvincia((String)hashDir.get(CenDireccionesBean.C_IDPROVINCIA));
						salida.setMovil((String)hashDir.get(CenDireccionesBean.C_MOVIL));
					} else {

					}
				} else {

				}
			} 
			catch (Exception e) { }
		} 
		else {
			salida.setIdPersona(CenPersonaAdm.K_PERSONA_GENERICA);
			salida.setApellidos1(beanSolic.getApellido1());
			salida.setApellidos2(beanSolic.getApellido2());
			salida.setNombre(beanSolic.getNombre());
			salida.setNifcif(beanSolic.getNumeroIdentificador());
			salida.setCodigoPostal(beanSolic.getCodigoPostal());
			salida.setCorreoElectronico(beanSolic.getCorreoElectronico());
			salida.setDomicilio(beanSolic.getDomicilio());
			salida.setFax1(beanSolic.getFax1());
			salida.setFax2(beanSolic.getFax2());
			salida.setIdPais(beanSolic.getIdPais());
			if (salida.getIdPais().equals("")) 
				salida.setIdPais(ClsConstants.ID_PAIS_ESPANA);

			salida.setIdPoblacion(beanSolic.getIdPoblacion());
			salida.setMovil(beanSolic.getMovil());
			salida.setPoblacionExtranjera(beanSolic.getPoblacionExtranjera());
			salida.setIdProvincia(beanSolic.getIdProvincia());
			salida.setIdInstitucion(beanSolic.getIdInstitucion());
		}
		return salida;
	}

	private Vector tratarSolicitudCertificado(String idInstitucion,String idSolicitud, UsrBean userBean, String desc) throws ClsExceptions 
	{
		//obtenemos la ruta al documento a partir del bean de solicitud
		Hashtable htPk = new Hashtable();
		htPk.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
		CerSolicitudCertificadosAdm solAdm = new CerSolicitudCertificadosAdm(userBean);
		CerSolicitudCertificadosBean solBean = null;
		solBean = (CerSolicitudCertificadosBean)solAdm.selectByPK(htPk).firstElement();        
		String rutaDir = solAdm.getRutaCertificadoDirectorioBD(solBean);
		String rutaFichero = solAdm.getRutaCertificadoFichero(solBean,rutaDir);

		//Generamos el vector de documentos
		Documento doc = new Documento(rutaFichero,desc);
		Vector vDocs = new Vector();
		vDocs.add(doc);
		return vDocs;
	}

	private Vector tratarFactura(HttpServletRequest request, String idInstitucion,String idFactura, UsrBean userBean, String desc) throws ClsExceptions, SIGAException {

		//obtenemos la ruta al documento a partir del bean de solicitud
		Hashtable htPk = new Hashtable();
		htPk.put(FacFacturaBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(FacFacturaBean.C_IDFACTURA,idFactura);
		FacFacturaAdm facAdm = new FacFacturaAdm(userBean);
		FacFacturaBean facBean = null;
		facBean = (FacFacturaBean)facAdm.selectByPK(htPk).firstElement();        
		CenColegiadoAdm admCol = new CenColegiadoAdm(userBean);
		Hashtable htCol = admCol.obtenerDatosColegiado(idInstitucion,facBean.getIdPersona().toString(),userBean.getLanguage());
		String nColegiado = "";
		if (htCol!=null && htCol.size()>0) {
			nColegiado = (String)htCol.get("NCOLEGIADO_LETRADO");
		}			 


		InformeFactura inf = new InformeFactura (userBean);
		File f = inf.generarFactura(request, this.getLenguaje(request), "" + this.getIDInstitucion(request), idFactura,nColegiado);
		String rutaFichero = f.getPath();


		//	String rutaFichero = facAdm.getRutaFactura(facBean);

		//Generamos el vector de documentos 
		Documento doc = new Documento(rutaFichero,desc);
		Vector vDocs = new Vector();
		vDocs.add(doc);

		return vDocs;
	}

	protected String procesarEnvio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = null;
		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
		Integer idEstadoEnvioFinal = null;
		try {

			
			tx = userBean.getTransaction();

			// Obtengo las claves
			DefinirEnviosForm form = (DefinirEnviosForm)formulario;
			String idInstitucion = userBean.getLocation();	
			String idEnvio = form.getIdEnvio();

			Hashtable htPk = new Hashtable();
			htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
			htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
			
			//obtengo el envio
			EnvEnviosBean envBean = (EnvEnviosBean)envAdm.selectByPKForUpdate(htPk).firstElement();
			
			if(envBean.getIdEstado().compareTo(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO)==0){
				throw new SIGAException("messages.envios.procesandoEnvio");
				
			}else{
				envBean.setIdEstado(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO);
                envAdm.updateDirect(envBean);
                
				
			}
			
			Envio envio = new Envio(envBean, userBean);
			// lo proceso
			envio.procesarEnvio(tx);
			idEstadoEnvioFinal = envBean.getIdEstado();

		}catch (Exception e){
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}
		if(idEstadoEnvioFinal!=null && idEstadoEnvioFinal.compareTo(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES))==0)
			return exitoRefresco("messages.envio.successErrores",request);
		else
			return exitoRefresco("messages.envio.success",request);
	}	


	/**
	 * Descarga el fichero de Log.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String descargarLogErrores(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		String forward = "descargaFichero";
		String sFicheroLog=null, sIdInstitucion=null, sIdEnvio=null;

		try {
			DefinirEnviosForm form = (DefinirEnviosForm)formulario;
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   

			Vector vOcultos = form.getDatosTablaOcultos(0);		
			sIdInstitucion = user.getLocation();  
			sIdEnvio = (String)vOcultos.elementAt(0);
			EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));

			sFicheroLog = envioAdm.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls";
			File fichero = new File(sFicheroLog);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());

		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return forward;
	}


	/**
	 * 
	 * @param form
	 * @return idPersona si es una unica, si no devolvera nul
	 * @throws ClsExceptions
	 */
	private String getIdPersonaUnica(DefinirEnviosForm form) throws ClsExceptions{
		Hashtable htPersonas = new Hashtable();
		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);

		String idPersona  = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idPersona = (String) ht.get("idPersona");

			if(!htPersonas.containsKey(idPersona)&&i!=0){
				idPersona = null;
				break;

			}else{
				htPersonas.put(idPersona, idPersona);

			}



		}


		return idPersona;


	}
	private String getIdColegiadoUnico(DefinirEnviosForm form) throws ClsExceptions{
		Hashtable htPersonas = new Hashtable();
		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);

		String idPersona  = null;
		String idInstitucion  = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idPersona = (String) ht.get("idPersona");
			idInstitucion = (String) ht.get("idInstitucion");
			String key = idPersona+"||"+idInstitucion;
			if(!htPersonas.containsKey(key)&&i!=0){
				idPersona = null;
				break;

			}else{
				htPersonas.put(key, idPersona);

			}

		}
		return idPersona;
	}
	private String getIdColegiados(DefinirEnviosForm form, HttpServletRequest request) throws ClsExceptions, SIGAException{
		Hashtable htPersonas = new Hashtable();
		Vector datos = new Vector();
		Vector guardias = new Vector();
		String fechaInicio = null;
		String fechaFin = null;
		String idlista = null;
		String idPersona  = null;
		String idInstitucion  = null;
	
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				if ( vCampos.size()>0) {
					Hashtable ht = (Hashtable) vCampos.get(0); 
					idInstitucion = (String) ht.get("idInstitucion");
					fechaInicio = (String) ht.get("fechaIni");
					fechaFin = (String) ht.get("fechaFin");
					idlista = (String) ht.get("idLista");
				}
				ScsInclusionGuardiasEnListasAdm admIGL =new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request));
				Hashtable paramBusqueda=new Hashtable();
				paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,idInstitucion);
				paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,idlista);
				Vector listasIncluidas=admIGL.select(paramBusqueda);
				Enumeration listaResultados=listasIncluidas.elements();
				
				while(listaResultados.hasMoreElements()){
					ScsInclusionGuardiasEnListasBean fila=(ScsInclusionGuardiasEnListasBean)listaResultados.nextElement();
					// Recopilacion datos
					/* RGG 08/10/2007 cambio para que obtenga todas las guardias del tiron
					 * Vector datosParciales= admGT.getDatosListaGuardias(fila.getIdInstitucion().toString(),fila.getIdTurno().toString(),fila.getIdGuardia().toString(),fechaInicio,fechaFin);
					int i=0;
					while (i<datosParciales.size()){
						datos.add(datosParciales.elementAt(i));
						i++;
					}
					*/
					Vector guardia = new Vector();
					guardia.add(fila.getIdTurno().toString());
					guardia.add(fila.getIdGuardia().toString());
					
					guardias.add(guardia);
					
				}
				
				ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(this.getUserBean(request));
				
				datos = admGT.getDatosPersonasGuardias(idInstitucion,idlista,guardias,fechaInicio,fechaFin);
				idPersona = calcularPersona(datos,form);
				
 

		return idPersona;
	}
	
	protected String calcularPersona(Vector datos, DefinirEnviosForm form) throws SIGAException  
	{
		
		String todos="";
		String idPersona=null;

		 //Map map = new HashMap();
		
		    String datosInforme = "";
		    int j;
		    String resto = form.getDatosEnvios();
		    if(datos.size()==0)
		    	throw new SIGAException("messages.general.error.noExistenDatos"); 
			for (j=0; j<datos.size(); j++){		    						
				Hashtable letradoOut=(Hashtable)datos.get(j);	
				Long letrado =new Long((String)letradoOut.get("IDPERSONA")).longValue();	
		    	datosInforme="idPersona==" +letrado+"##"+resto;
		    	todos+= datosInforme;
		    	idPersona="" +letrado;

			}				   
			
			/*List guardiasList = null;
			for (int j=0; j<datos.size(); j++){		    						
				   Hashtable lineaGuardia=(Hashtable)datos.get(j);			
				   Long letrado =new Long((String)lineaGuardia.get("IDPERSONA")).longValue();	
				   if(map.containsKey(letrado)){
					   guardiasList = (List) map.get(letrado);
				   }else{
					   guardiasList = new ArrayList();
				   }
				   guardiasList.add(lineaGuardia);
				   map.put(letrado, guardiasList);
			}
			Iterator it = map.keySet().iterator();
		    List guardiasOutList = null;
		    String datosInforme = "";
		    String resto = form.getDatosEnvios();	
		    while (it.hasNext()) {
	    		i++;
		    	long letradoOut = (Long) it.next();
		    	datosInforme="idPersona==" +letradoOut+"##"+resto;
		    	todos+= datosInforme;
		    	idPersona="" +letradoOut;
		    }
			*/
			form.setDatosEnvios(todos);
			if(datos.size()!=1)
				idPersona=null;
			
		
		
        return  idPersona;
	}

	private String getIdColegiadoUnicoDesignas(Vector vCampos,UsrBean userBean)throws ClsExceptions,SIGAException{

		
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		String idPersona  = null;
		String idTurno  = null;
		String idInstitucion  = null;
		String anio  = null;
		String numero  = null;
		List alPersonas = new ArrayList();
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String)ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String)ht.get("numero");
			idPersona =  desigAdm.getIDLetradoDesig(idInstitucion,idTurno,anio,numero);
			ht.put("idPersona", idPersona);

			String key = idPersona+"||"+idInstitucion;
			if(!alPersonas.contains(key)&&i!=0){
				idPersona = null;
				break;

			}else{
				alPersonas.add(key);

			}
		}
		return idPersona;

	}
	
	
	private int getNumSolicitantesDesignas(Vector vCampos,UsrBean userBean)throws ClsExceptions,SIGAException{
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(userBean);
		
		String idPersonaJG = null;
		String idTurno  = null;
		String idInstitucion  = null;
		String anio  = null;
		String numero  = null;
		boolean isSolicitanteUnicoDesignas = true;
		int numSolicitantes = 0;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String)ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String)ht.get("numero");
			Vector vDefendidos = defendidosAdm.getDefendidosDesigna(new Integer(idInstitucion), new Integer(anio),new Integer(numero), new Integer(idTurno));
			if(vDefendidos!=null)
				numSolicitantes += vDefendidos.size();
			if(numSolicitantes>1){
				break;
			}


		}	
		
		
		
		return numSolicitantes;

	}


	/** 
	 * Descarga el resultado de la consulta en un fichero
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String descargarEstadistica(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {


		try {
			EnvEstatEnvioAdm adm = new EnvEstatEnvioAdm(this.getUserBean(request));
			Vector v = adm.getDatosInforme((DefinirEnviosForm)formulario);
			String sql = "";
			Hashtable codigos = new Hashtable();
			if (v!=null && v.size()==2) {
				sql=(String)v.get(0);
				codigos=(Hashtable)v.get(1);
			}
			String nombreFichero = "stat_envios"; 
			String[] cabeceras = {"IDPERSONA","APELLIDOS1","APELLIDOS2","NOMBRE","NUM_ELECTRONICO","NUM_ORDINARIO","NUM_FAX","NUM_SMS","NUM_BUROSMS"};


			RowsContainer rc = new RowsContainer();
			rc.queryBind(sql, codigos);
			request.setAttribute("datos",rc.getAll());
			request.setAttribute("descripcion",nombreFichero);
			request.setAttribute("cabeceras",cabeceras);


		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}
		return "export";
	}



}