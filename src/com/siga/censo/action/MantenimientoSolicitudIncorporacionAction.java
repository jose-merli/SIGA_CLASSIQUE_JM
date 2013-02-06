
package com.siga.censo.action;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDocumentacionModalidadAdm;
import com.siga.beans.CenDocumentacionModalidadBean;
import com.siga.beans.CenDocumentacionPresentadaAdm;
import com.siga.beans.CenDocumentacionPresentadaBean;
import com.siga.beans.CenDocumentacionSolicitudInstituBean;
import com.siga.beans.CenEstadoCivilAdm;
import com.siga.beans.CenEstadoCivilBean;
import com.siga.beans.CenEstadoColegialBean;
import com.siga.beans.CenEstadoSolicitudAdm;
import com.siga.beans.CenEstadoSolicitudBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPaisAdm;
import com.siga.beans.CenPaisBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.CenSolicitudIncorporacionAdm;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CenTipoColegiacionAdm;
import com.siga.beans.CenTipoColegiacionBean;
import com.siga.beans.CenTipoSolicitudAdm;
import com.siga.beans.CenTipoSolicitudBean;
import com.siga.beans.CenTratamientoAdm;
import com.siga.beans.CenTratamientoBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.SolicitudIncorporacionForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.RetencionesIRPFAction;


/**
 * Clase action del caso de uso SOLICITUD INCORPORACION
 * @author AtosOrigin
 * @since 10-09-2004
 * @version daniel.campos - 10-09-2004 - Creacion
 * @version raul.ggonzalez - 02-02-2005 - Incluir modo altaColegiado
 * @version david.sanchezp - 21-05-2005 - modificar la creacion del 
 *   idsolicitud mediante numeros aleatorios
 * @version david.sanchezp - 27-12-2005 - para incluir el campo sexo
 * @version adrian.ayala - 2008-06-03 - revision de modificar()
 *   para asegurar el correcto funcionamiento de la insercion en cola 
 *   para la copia de direcciones de colegiados a Consejos
 */
public class MantenimientoSolicitudIncorporacionAction extends MasterAction
{
	final int solicitudDesestima = -2;
	
		/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */

	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
				
				String accion = miForm.getModo();

		  		// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("editar")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("getAjaxExisteColegiado")){
					getAjaxExisteColegiado(mapping, miForm, request, response);
					return null;
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}	
	
	protected String abrir(ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		String forward = "";
		
		try {
			//obteniendo controles globales
			SolicitudIncorporacionForm miFormulario = (SolicitudIncorporacionForm)formulario;
			Hashtable hash = new Hashtable();
			
			//obteniendo beans y adms
			CenSolicitudIncorporacionBean bean = null;
			CenSolicitudIncorporacionAdm solicitudAdm = new CenSolicitudIncorporacionAdm (this.getUserBean(request));
			
			// Si es una consulta por Solicitud de incorporacion
			Long idSolicitud=miFormulario.getEditarIdSolicitud();
			/*if(miFormulario.getDatosTablaOcultos(0)!=null){
				idSolicitud = new Long((String) miFormulario.getDatosTablaOcultos(0).get(0));
			}else{
				idSolicitud = miFormulario.getEditarIdSolicitud();
			}*/
			if(idSolicitud==null){
				idSolicitud = new Long(request.getParameter("IDSOLICITUD"));
			}
			UtilidadesHash.set(hash, CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
			Vector datosSelect = solicitudAdm.selectByPK(hash);
			if ((datosSelect != null) && (datosSelect.size() > 0)) {
				bean = (CenSolicitudIncorporacionBean) datosSelect.get(0);
			}
			
			forward = this.mostrarDatosSolicitud(request, bean, miFormulario); 
			request.setAttribute("Editar", "true");
		} 	
		catch (Exception e) {
			 throwExcp("messages.general.error", new String[] {"modulo.censo"},e,null);
	   	}
		return forward;
	} //editar()
	
	protected String ver(ActionMapping mapping, 
						 MasterForm formulario, 
						 HttpServletRequest request, 
						 HttpServletResponse response)
			throws SIGAException
	{
		String forward = "";
		
		try {
			SolicitudIncorporacionForm miFormulario = (SolicitudIncorporacionForm)formulario;
			
			Hashtable hash = new Hashtable();
			CenSolicitudIncorporacionBean bean = null;
			CenSolicitudIncorporacionAdm solicitudAdm = new CenSolicitudIncorporacionAdm (this.getUserBean(request));
			
			// Si es una consulta por Solicitud de incorporacion
			Boolean aux = miFormulario.getBuscarModoAnteriorBusqueda();
			Long idSolicitud;
			/*if ((aux != null) && (aux.booleanValue() == true)) {
				idSolicitud = new Long((String) miFormulario.getDatosTablaOcultos(0).get(0));
			}else { // Si es una consulta por busqueda de solicitudes
				idSolicitud= new Long( miFormulario.getClave());
			}*/
			idSolicitud = new Long(request.getParameter("IDSOLICITUD"));
			
			UtilidadesHash.set(hash, CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
			Vector datosSelect = solicitudAdm.selectByPK(hash);
			if ((datosSelect != null) && (datosSelect.size() > 0)) {
				bean = (CenSolicitudIncorporacionBean) datosSelect.get(0);
			}
			forward = this.mostrarDatosSolicitud(request, bean, miFormulario);
			
			//Vemos si vamos a una modal o no:
			String esmodal = miFormulario.getEsModal();
			if (esmodal != null && esmodal.equals("N"))
				request.setAttribute("ModoAnterior","VERSINMODAL");
			else	
				request.setAttribute("ModoAnterior","VER");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return forward; 
	} //ver()
	
	protected String nuevo(ActionMapping mapping, 
						   MasterForm formulario, 
						   HttpServletRequest request, 
						   HttpServletResponse response)
			throws SIGAException
	{
		try {
			// Validamos el usuario para la fecha de hoy
			request.setAttribute("fechaSol", UtilidadesBDAdm.getFechaBD("")); 
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "nuevo";
	} //nuevo()
	
	protected String insertar(ActionMapping mapping, 
							  MasterForm formulario, 
							  HttpServletRequest request, 
							  HttpServletResponse response)
			throws SIGAException 
	{
		String mensaje="";		
		Integer tipoSolicitud, estadoSolicitud;
		Long numeroSolicitud;
		UserTransaction t = this.getUserBean(request).getTransaction();
		String mensajeRefresco = "messages.inserted.success";
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		try {
			String nColegiado[] = new String[1];
			nColegiado[0] = null;
			SolicitudIncorporacionForm miFormulario = (SolicitudIncorporacionForm)formulario;
			Integer usuario = this.getUserName(request);
						
			try {
				tipoSolicitud = new Integer(this.validaTipoSolicitud
						(miFormulario, usuario, request, nColegiado, mensaje));
			} catch (SIGAException siga){
				return exito("messages.censo.nifcifExiste2",request);
			}
			
			CenPersonaBean perBean = null;
			perBean= obtenerDatosPersona(miFormulario.getNIFCIF(),request);
				
				
			if (tipoSolicitud.intValue() == this.solicitudDesestima) {
				
				String msj=UtilidadesString.getMensajeIdioma(usr,
						"messages.censo.solicitudIncorporacion.errorSolicitudDesestimada");	
			  	msj+=" Sus datos son: "+miFormulario.getNIFCIF()+"-"+
			  			perBean.getNombre()+" "+perBean.getApellido1()+" "+perBean.getApellido2();
				return exito(msj,request);
			}
			
			CenInstitucionAdm adm = new CenInstitucionAdm (this.getUserBean(request));
			Vector documentacion = adm.getDocumentacionAPresentar(this.getIDInstitucion(request), 
					tipoSolicitud, miFormulario.getTipoColegiacion(), miFormulario.getTipoModalidadDocumentacion());
			if (documentacion != null) {
				estadoSolicitud = new Integer(ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC);
			}
			else {
				estadoSolicitud = new Integer(ClsConstants.ESTADO_SOLICITUD_PENDIENTE_APROBAR);
			}
			
			CenSolicitudIncorporacionAdm solicitudAdm = new CenSolicitudIncorporacionAdm (this.getUserBean(request));
			CenSolicitudIncorporacionBean bean = new CenSolicitudIncorporacionBean();
			
			//Rellenamos el bean con los datos del formulario:
			if(miFormulario.getApellido1()!=null)
				bean.setApellido1(miFormulario.getApellido1().trim());
			if(miFormulario.getApellido2()!=null)
				bean.setApellido2(miFormulario.getApellido2().trim());
			bean.setCodigoPostal(miFormulario.getCP());
			bean.setCorreoElectronico(miFormulario.getMail());
			bean.setDomicilio(miFormulario.getDomicilio());
			bean.setFax1(miFormulario.getFax1());
			bean.setFax2(miFormulario.getFax2());
			bean.setFechaEstado("sysdate");
			bean.setFechaEstadoColegial(miFormulario.getFechaEstadoColegial());
			bean.setFechaNacimiento(miFormulario.getFechaNacimiento());
			bean.setFechaSolicitud("sysdate");
			bean.setIdEstado(estadoSolicitud);			
			if (miFormulario.getEstadoCivil().toString().equals("0")) {
				bean.setIdEstadoCivil(null);
			} else {
				bean.setIdEstadoCivil(miFormulario.getEstadoCivil());
			}
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setIdPais((miFormulario.getPais().equals(""))?ClsConstants.ID_PAIS_ESPANA:miFormulario.getPais());
			if (bean.getIdPais().equals(ClsConstants.ID_PAIS_ESPANA)) {
				bean.setIdPoblacion(miFormulario.getPoblacion());
				bean.setIdProvincia(miFormulario.getProvincia());
			} else {
				bean.setIdPoblacion("");
				bean.setIdProvincia("");
			}
			bean.setPoblacionExtranjera(miFormulario.getPoblacionExt());
			
			bean.setIdTipoColegiacion(miFormulario.getTipoColegiacion());
			bean.setIdTipoIdentificacion(miFormulario.getTipoIdentificacion());
			bean.setIdTipoSolicitud(tipoSolicitud);
			bean.setIdTratamiento(miFormulario.getTipoDon());
			bean.setMovil(miFormulario.getTelefono3());
			bean.setNaturalDe(miFormulario.getNatural());
			bean.setNColegiado(nColegiado[0]);
			if(miFormulario.getNombre()!=null)
				bean.setNombre(miFormulario.getNombre());
			// RGG 18-03-2005 se mete el nifcif en mayusculas
			bean.setNumeroIdentificador(miFormulario.getNIFCIF().toUpperCase());
			bean.setObservaciones(miFormulario.getObservaciones());
			bean.setTelefono1(miFormulario.getTelefono1());
			bean.setTelefono2(miFormulario.getTelefono2());
			//Generamos un número aleatorio para el idSolicitud ay que será clave:
			numeroSolicitud = this.generarIdSolicitud(usr);
			//numeroSolicitud = new Long (ClsMngBBDD.nextSequenceValue("SEQ_SOLICITUDINCORPORACION"));
			bean.setIdSolicitud(numeroSolicitud);
			// Modificado el 27/12/2005 para incluir este nuevo campo:
			bean.setSexo(miFormulario.getSexo());
			bean.setIdModalidadDocumentacion(miFormulario.getTipoModalidadDocumentacion());
			// Ponemos por defecto el tipo de cuenta a Cargo
			bean.setAbonoCargo("C");
			bean.setResidente(miFormulario.getResidente());
			
			request.getSession().setAttribute("idSolicitud",numeroSolicitud.toString());
			
		    GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));		    
		    mensajeRefresco = paramAdm.getValor((this.getIDInstitucion(request)).toString(), 
		    		ClsConstants.MODULO_CENSO, ClsConstants.MENS_SOLICITUD, null);
			
			t.begin();
			if (!solicitudAdm.insert(bean)) {
				return exito("messages.censo.solicitudIncorporacion.errorSolicitudDesestimada",request);
			}
			t.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,t);
		}
		return exito(mensajeRefresco, request); // exitoRefresco(mensajeRefresco,request);
	} //insertar()
	
	protected String modificar (ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request,
								HttpServletResponse response)
			throws SIGAException, SIGAException
	{
		//Variables generales
		UsrBean user;
		SolicitudIncorporacionForm miFormulario;
		Hashtable hash;
		UserTransaction t = null;
		String forward = "";
		
		//Otras variables
		boolean modificarFechaEstado=false;
		Integer idTipoSolicitudNuevo, idTipoSolicitudActual;
		
		
		try
		{
			////////// OBTENIENDO DATOS //////////
			
			//obteniendo usuario e institucion
			user = this.getUserBean (request);
			miFormulario = (SolicitudIncorporacionForm) formulario;
			
			//onteniendo el id unico de solicitud
			Long idSolicitud = miFormulario.getEditarIdSolicitud ();
			
			//validando el tipo de solicitud
			idTipoSolicitudNuevo = miFormulario.getEditarEstadoSolicitud ();
			idTipoSolicitudActual = this.getTipoSolicitud (idSolicitud, user);
			if (!idTipoSolicitudActual.equals(idTipoSolicitudNuevo)){
				modificarFechaEstado=true;
				/*if (!validaModificacionTipoSolicitud(idTipoSolicitudActual,idTipoSolicitudNuevo, mensaje) ) {
					return error (mensaje[0], new ClsExceptions (mensaje[0]), request);
				}else{
					//cambiamos la fecha del estado
					modificarFechaEstado=true;
				}*/
			}
			
			
			////////// REALIZANDO INCORPORACION //////////
			CenDocumentacionPresentadaAdm admDoc = new CenDocumentacionPresentadaAdm (user);
			
			//comprobando los documentos presentados
			Vector vDocPresentado = new Vector ();
			int numDocumentosPresentados = 0;
			for (int i=0; i < miFormulario.getDatosTabla ().size (); i++)
			{
				//obteniendo datos del formulario
				Vector vOcultos = miFormulario.getDatosTablaOcultos (i);
				Vector vVisible = miFormulario.getDatosTablaVisibles (i);
				Integer idDocumento = new Integer ((String) vOcultos.get (0));
				
				//buscando en el adm con los datos del formulario
				hash = new Hashtable ();
				UtilidadesHash.set(hash, CenDocumentacionPresentadaBean.C_IDSOLICITUD, idSolicitud);
				UtilidadesHash.set(hash, CenDocumentacionPresentadaBean.C_IDDOCUMENTACION, idDocumento);
				Vector docEsta = admDoc.selectByPK(hash);
				
				//registrando cada documento que esta o no presentado
				if (docEsta.size() >= 1)
					vDocPresentado.add (new Boolean (true));
				else
					vDocPresentado.add (new Boolean (false));
				
				//guardando numero total de documentos presentados
				if (((String) vVisible.get (0)).equalsIgnoreCase ("true"))
					numDocumentosPresentados ++;
			} //for
			
			//obteniendo los datos de la nueva solicitud
			hash = new Hashtable ();
			CenSolicitudIncorporacionAdm admSol = new CenSolicitudIncorporacionAdm (user);
			UtilidadesHash.set (hash, CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
			Vector vDatosSolicitud = admSol.selectByPK(hash);
			Hashtable hashOriginal = (Hashtable) ((CenSolicitudIncorporacionBean) vDatosSolicitud.get (0)).getOriginalHash (); 
			Hashtable hashModificado = (Hashtable) hashOriginal.clone ();
			
			if (modificarFechaEstado)
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_FECHAESTADO, 
						"SYSDATE");
			UtilidadesHash.set (hashModificado, CenSolicitudIncorporacionBean.C_IDESTADO, 
					idTipoSolicitudNuevo);
			
			// jbd inc-6526 Escribimos los datos nuevos que haya metido el usuario
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDTIPOIDENTIFICACION, miFormulario.getTipoIdentificacion());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR, miFormulario.getNIFCIF());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_NCOLEGIADO, miFormulario.getNumeroColegiado());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDTRATAMIENTO, miFormulario.getTipoDon());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_APELLIDO1, miFormulario.getApellido1());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_APELLIDO2, miFormulario.getApellido2());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_NOMBRE, miFormulario.getNombre());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_SEXO, miFormulario.getSexo());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_NATURALDE, miFormulario.getNatural());
			if(miFormulario.getEstadoCivil()>0)
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDESTADOCIVIL, miFormulario.getEstadoCivil());
			else
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDESTADOCIVIL, "");
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_FECHANACIMIENTO, miFormulario.getFechaNacimiento());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_DOMICILIO, miFormulario.getDomicilio());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_CODIGOPOSTAL, miFormulario.getCP());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDPAIS, miFormulario.getPais());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDPROVINCIA, miFormulario.getProvincia());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDPOBLACION, miFormulario.getPoblacion());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_POBLACIONEXTRANJERA, miFormulario.getPoblacionExt());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_TELEFONO1, miFormulario.getTelefono1());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_TELEFONO2, miFormulario.getTelefono2());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_MOVIL, miFormulario.getTelefono3());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_FAX1, miFormulario.getFax1());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_FAX2, miFormulario.getFax2());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_CORREOELECTRONICO, miFormulario.getMail());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_OBSERVACIONES, miFormulario.getObservaciones());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_FECHASOLICITUD, miFormulario.getFechaSolicitud());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDTIPOCOLEGIACION, miFormulario.getTipoColegiacion());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_FECHAESTADOCOLEGIAL, miFormulario.getFechaEstadoColegial());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDMODALIDADDOCUMENTACION, miFormulario.getTipoModalidadDocumentacion());
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD, miFormulario.getTipoSolicitud());
			if(miFormulario.getResidente()){
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_RESIDENTE, ClsConstants.DB_TRUE);
			}else{								
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_RESIDENTE, ClsConstants.DB_FALSE);
			}
			// La cuenta bancaria
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_ABONOCARGO, this.validarTipoCuenta(miFormulario.getCuentaAbono(), miFormulario.getCuentaCargo()));   
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_ABONOSJCS, miFormulario.getAbonoSJCS());   
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_CBO_CODIGO, miFormulario.getCbo_Codigo());   
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_CODIGOSUCURSAL, miFormulario.getCodigoSucursal());   
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_DIGITOCONTROL, miFormulario.getDigitoControl());   
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_NUMEROCUENTA, miFormulario.getNumeroCuenta());   
			UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_TITULAR, miFormulario.getTitular());
			if(miFormulario.getAbonoSJCS().booleanValue()){
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_ABONOSJCS, ClsConstants.DB_TRUE);
			}else{								
				UtilidadesHash.set(hashModificado, CenSolicitudIncorporacionBean.C_ABONOSJCS, ClsConstants.DB_FALSE);
			}
			
			//iniciando la modificacion de datos en BD
			t = user.getTransactionPesada ();
			t.begin ();
			
			//modificando el estado de la solicitud de incorporacion 			
			admSol.update (hashModificado, hashOriginal);
			
			//realizando los insert y delete correspondientes de la documentacion pendiente
			for (int i=0; i < miFormulario.getDatosTabla ().size (); i++)
			{
				//obteniendo datos del formulario
				Vector vOcultos = miFormulario.getDatosTablaOcultos (i);
				Vector vVisible = miFormulario.getDatosTablaVisibles (i);
				Integer idDocumento = new Integer ((String) vOcultos.get (0));
				
				//buscando en el adm con los datos del formulario
				hash = new Hashtable ();
				UtilidadesHash.set (hash, CenDocumentacionPresentadaBean.C_IDSOLICITUD, idSolicitud);
				UtilidadesHash.set (hash, CenDocumentacionPresentadaBean.C_IDDOCUMENTACION, idDocumento);
				
				//si esta marcado y no esta en BD -> lo insertamos
				if ((((String) vVisible.get (0)).equalsIgnoreCase("true")) && 
						!((Boolean)vDocPresentado.get(i)).booleanValue())
					admDoc.insert(hash);
				//si no esta marcado y esta en BD -> lo borramos
				if ((((String) vVisible.get (0)).equalsIgnoreCase("false")) && 
						((Boolean)vDocPresentado.get(i)).booleanValue())
					admDoc.delete(hash);
			} //for
			
			//insercion de los demas datos si se aprueba la solicitud
			boolean fichaColegial = false;
			if (UtilidadesHash.getInteger (hashModificado, CenSolicitudIncorporacionBean.C_IDESTADO).intValue () == 
					ClsConstants.ESTADO_SOLICITUD_APROBADA)
			{
				//obteniendo los controles de la solicitud
				CenSolicitudIncorporacionAdm admSolic = new CenSolicitudIncorporacionAdm (user); 
				CenSolicitudIncorporacionBean beanSolic = null; 
				
				//obteniendo los datos de la solicitud
				Hashtable hashSolicitud = new Hashtable(); 
				String nombrePersona="", apellido1Persona="", apellido2Persona=""; 
				UtilidadesHash.set (hashSolicitud, CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
				Vector datosSelect = admSolic.selectByPK (hashSolicitud);
				if ((datosSelect != null) && (datosSelect.size() > 0)) {
					beanSolic = (CenSolicitudIncorporacionBean) datosSelect.get (0);
				}
				
				//obteniendo el administrador de cliente para la insercion del alta colegial
				CenClienteAdm admCli = new CenClienteAdm (user);
				//insertando persona, cliente, colegiado y datos colegiales
				// (cada una de las cosas solo si hace falta)
				
				// Aqui vamos a comprobar que no exista, pero antes miramos el continuarInsercion 
				// para ver si la pregunta ya se ha hecho 
				String continuar = request.getParameter ("continuarInsercionColegiado");
				if(!(continuar!=null && continuar.equalsIgnoreCase("1"))){
					//En caso de que ya exista el nº de colegiado se le mostrará un mensaje de advertencia
					// y no se seguirá con el alta del colegiado.
					CenColegiadoAdm admCol = new CenColegiadoAdm (user);
					if( admCol.existeNColegiado(beanSolic.getNColegiado(), 
												beanSolic.getIdInstitucion(),
												beanSolic.getIdTipoColegiacion())){
						String msj = UtilidadesString.getMensajeIdioma (user, "error.message.NumColegiadoRepetido");
						request.setAttribute ("msj", msj);
						forward = "continuarInsercionColegiado";
						t.rollback ();
						return forward;
					}
				}
				CenClienteBean beanCli = admCli.altaColegial 
						(beanSolic, request.getParameter ("continuarAprobacion"));
				
				//si no existe el cliente, se da marcha atras a los cambios realizados
				if (beanCli == null) {
					CenPersonaBean perBean = null;
					perBean = obtenerDatosPersona (beanSolic.getNumeroIdentificador (), request);
					nombrePersona = perBean.getNombre ().toUpperCase ();
					apellido1Persona = perBean.getApellido1 ().toUpperCase (); 
					apellido2Persona = perBean.getApellido2 ().toUpperCase ();
					String msj = UtilidadesString.getMensajeIdioma (user, "messages.censo.nifcifExiste3");	
					msj += "-"+nombrePersona+" "+apellido1Persona+" "+apellido2Persona+". "+ "\u00BFDesea"+"Continuar\u003F";
					request.setAttribute ("msj", msj);
					forward = "continuarAprobacion";
					t.rollback ();
					return forward;
				}
				
				//obteniendo el mensaje devuelto por el adm al obtener el cliente
				String mensInformacion = "";
				if (! admCli.getError().equals (""))
					mensInformacion = admCli.getError();
				mensInformacion = mensInformacion.equals ("") ? "messages.updated.success" : mensInformacion;
				
				
				// Comprobamos que se den las condiciones idoneas para crear la cuenta

				//Creamos la cuenta bancaria
				CenCuentasBancariasAdm cuentaAdm = new CenCuentasBancariasAdm(user);
				CenCuentasBancariasBean cuentaBean = new CenCuentasBancariasBean();
				cuentaBean.setIdPersona(beanCli.getIdPersona());
				cuentaBean.setIdInstitucion(beanCli.getIdInstitucion());
				cuentaBean.setIdCuenta(cuentaAdm.getNuevoID(cuentaBean));
				cuentaBean.setTitular(miFormulario.getTitular());
				cuentaBean.setCbo_Codigo(miFormulario.getCbo_Codigo());
				cuentaBean.setCodigoSucursal(miFormulario.getCodigoSucursal());
				cuentaBean.setDigitoControl(miFormulario.getDigitoControl());
				cuentaBean.setNumeroCuenta(miFormulario.getNumeroCuenta());
				if(miFormulario.getAbonoSJCS().booleanValue())cuentaBean.setAbonoSJCS(ClsConstants.DB_TRUE);			
				else cuentaBean.setAbonoSJCS(ClsConstants.DB_FALSE);
				cuentaBean.setAbonoCargo(this.validarTipoCuenta(miFormulario.getCuentaAbono(), miFormulario.getCuentaCargo()));
				// Solo hacemos el insert si tenemos los datos obligatorios
				if(	!cuentaBean.getCbo_Codigo().equalsIgnoreCase("") &&
					!cuentaBean.getCodigoSucursal().equalsIgnoreCase("") &&
					!cuentaBean.getDigitoControl().equalsIgnoreCase("") &&
					!cuentaBean.getNumeroCuenta().equalsIgnoreCase("") &&
					!cuentaBean.getTitular().equalsIgnoreCase(""))
						cuentaAdm.insert(cuentaBean);
				
				
				//lanzando el proceso de revision de suscripciones del letrado 
				String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado
						(beanCli.getIdInstitucion ().toString (), 
						beanCli.getIdPersona ().toString(), 
						"", 
						""+this.getUserName (request));
				
				if ((resultado == null) || (!resultado[0].equals("0")))
					throw new ClsExceptions ("Error al ejecutar el PL " +
							"PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO");

				

				
				//cargando la peticion para el reenvio
				request.setAttribute ("mensaje", mensInformacion);
				request.setAttribute ("idPersona", beanCli.getIdPersona ().toString ());
				request.setAttribute ("idInstitucion", beanCli.getIdInstitucion ().toString ());
				//quitando el boton volver porque va a ir a la ficha colegial
				request.getSession ().setAttribute ("CenBusquedaClientesTipo", "SI");
				fichaColegial = true;
					try{
					RetencionesIRPFAction irpf = new  RetencionesIRPFAction();
					irpf.insertarNuevo(beanCli.getIdPersona().toString(),"SYSDATE",request);
					}
					catch (Exception e) {
						t.rollback();
						throw e;
					}
					String [] claves = {CenSolicitudIncorporacionBean.C_IDSOLICITUD};
					String [] campos = {CenSolicitudIncorporacionBean.C_IDPERSONA,CenSolicitudIncorporacionBean.C_FECHAALTA};
					hashModificado.put(CenSolicitudIncorporacionBean.C_IDPERSONA, beanCli.getIdPersona());
					hashModificado.put(CenSolicitudIncorporacionBean.C_FECHAALTA,"SYSDATE");
					admSol.updateDirect(hashModificado, claves, campos) ;
			} //si se aprueba la solicitud
			
			//confirmando los cambios en BD
			t.commit ();
			
			
			//terminando para saltar a la ficha colegial
			if (fichaColegial)
				forward = "exitoInsercion";			
			else
				forward = exitoRefresco("messages.updated.success", request);
		}
		catch (Exception e) {
			throwExcp ("messages.general.error", new String[] {"modulo.censo"}, e, t);
		}
		return forward;
	} //modificar ()
	
	protected String buscarPor(ActionMapping mapping, 
							   MasterForm formulario, 
							   HttpServletRequest request, 
							   HttpServletResponse response)
			throws SIGAException
	{
		try {
			SolicitudIncorporacionForm miForm = (SolicitudIncorporacionForm)formulario;
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request)); 
			String plazoEntregaDocSolicitud = null;
			plazoEntregaDocSolicitud = paramAdm.getValor(this.getIDInstitucion(request).toString(),
					ClsConstants.MODULO_CENSO,ClsConstants.PLAZO_EN_DIAS_ENTREGA_DOCSOLICITUD, null);
			if (plazoEntregaDocSolicitud==null) plazoEntregaDocSolicitud = 
					new Integer(ClsConstants.MAX_DIAS_DOCUMENTACION_ENTREGA).toString();
			Hashtable hash = new Hashtable();
			
			String plazoSaltaAlarmaEntregaDocSolicitud = null;
			plazoSaltaAlarmaEntregaDocSolicitud = paramAdm.getValor(this.getIDInstitucion(request).toString(),
					ClsConstants.MODULO_CENSO,ClsConstants.PLAZO_EN_DIAS_SALTAALARMA_ENTREGA_DOCSOLICITUD, null);
			if (plazoSaltaAlarmaEntregaDocSolicitud==null) plazoSaltaAlarmaEntregaDocSolicitud = 
					new Integer(ClsConstants.MAX_DIAS_DOCUMENTACION_ENTREGA).toString();
			
			Integer estadoSol = miForm.getBuscarEstadoSolicitud(); 
			String select = "SELECT " + CenSolicitudIncorporacionBean.C_IDSOLICITUD 		+ ", " +
										CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD 	+ ", " ;
			select+=CenSolicitudIncorporacionBean.C_IDESTADO+ ", " ;
			select+=     				CenSolicitudIncorporacionBean.C_NOMBRE 				+ ", " +
										CenSolicitudIncorporacionBean.C_APELLIDO1			+ ", " +
										CenSolicitudIncorporacionBean.C_APELLIDO2			+ ", " +
										CenSolicitudIncorporacionBean.C_FECHAESTADO 		+ ", " +
										CenSolicitudIncorporacionBean.C_FECHASOLICITUD 		+ ", " +
										CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR + ", " +
										CenSolicitudIncorporacionBean.C_NCOLEGIADO			+ ", " +
							 " CASE " + 
			                 " WHEN (" + CenSolicitudIncorporacionBean.C_IDESTADO + " = " + ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC +
							 " AND (sysdate - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " >= " + plazoEntregaDocSolicitud + ")) THEN 'ALARMACADUCADA'" +
							 " WHEN (" + CenSolicitudIncorporacionBean.C_IDESTADO + " = " + ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC +
							 " AND (SYSDATE - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " >= " + plazoSaltaAlarmaEntregaDocSolicitud + ")) THEN 'ALARMA' " +
							 " ELSE 'SINALARMA' END as CAMPOALARMA ";
			
			String from = " FROM " + CenSolicitudIncorporacionBean.T_NOMBRETABLA + " "; 
			
			// Construimos la sentencia where
			String where = "WHERE " + CenSolicitudIncorporacionBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request);
			Integer tipoSol = miForm.getBuscarTipoSolicitud(); 
			if ((tipoSol != null) && (tipoSol.intValue() != 0)) {
				where += " AND " + CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD + " = " + tipoSol; 
			}
			estadoSol = miForm.getBuscarEstadoSolicitud(); 
			if ((estadoSol != null) && (estadoSol.intValue() != 0)) {
				if (estadoSol.intValue()==ClsConstants.ESTADO_SOLICITUD_APUNTODECADUCAR){
					where += " AND (" + CenSolicitudIncorporacionBean.C_IDESTADO + " = " + ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC+" AND (SYSDATE - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " >= " + plazoSaltaAlarmaEntregaDocSolicitud+" AND sysdate - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " < " + plazoEntregaDocSolicitud+" ))" ;
				}else if (estadoSol.intValue()==ClsConstants.ESTADO_SOLICITUD_CADUCADA){
					where += " AND (" + CenSolicitudIncorporacionBean.C_IDESTADO + " = " + ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC+" AND (sysdate - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " >= " + plazoEntregaDocSolicitud + "))" ;
				}
				else{
			 	   where += " AND " + CenSolicitudIncorporacionBean.C_IDESTADO + " = " + estadoSol;
				}
			}
			
			String fDesde = miForm.getBuscarFechaDesde(); 
			String fHasta = miForm.getBuscarFechaHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				where += " AND " + GstDate.dateBetweenDesdeAndHasta(CenSolicitudIncorporacionBean.C_FECHASOLICITUD,fDesde,fHasta);
			}
			
			Boolean bAlarma = miForm.getBuscarVerAlarmas();
			if ((bAlarma != null) && (bAlarma.booleanValue())) {
				where += " AND (" + CenSolicitudIncorporacionBean.C_IDESTADO + " = " + ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC;
				where += " AND (sysdate - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " >= " + plazoEntregaDocSolicitud;
				where += " OR sysdate - " + CenSolicitudIncorporacionBean.C_FECHAESTADO + " >= " + plazoSaltaAlarmaEntregaDocSolicitud + ")";
				where += ")";
			}
			
			//String orderBy = " ORDER BY " + CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR;
			
			// RGG 18-02-2005 Correccion de errores
			String orderBy = " ORDER BY " + CenSolicitudIncorporacionBean.C_FECHASOLICITUD + " DESC" ;
			
			String consulta = select + from + where + orderBy;
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				
				Vector resultados = new Vector (); 
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					CenSolicitudIncorporacionBean bean = new CenSolicitudIncorporacionBean();
					bean.setIdSolicitud(UtilidadesHash.getLong(fila.getRow(), CenSolicitudIncorporacionBean.C_IDSOLICITUD));
					bean.setFechaEstado(UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_FECHAESTADO));
					bean.setFechaSolicitud(UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_FECHASOLICITUD));
					String nombreCompleto =	UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_NOMBRE) + " " +
											UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_APELLIDO1) + " " +
											UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_APELLIDO2);
					bean.setNombre(nombreCompleto);
					bean.setNColegiado(UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_NCOLEGIADO));
					bean.setNumeroIdentificador(UtilidadesHash.getString(fila.getRow(), CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR));
					bean.setIdEstado(UtilidadesHash.getInteger(fila.getRow(), CenSolicitudIncorporacionBean.C_IDESTADO));
					
					Hashtable hashAux = new Hashtable ();
					bean.setFechaEstado(GstDate.getFormatedDateShort("", bean.getFechaEstado()));
					bean.setFechaSolicitud(GstDate.getFormatedDateShort("", bean.getFechaSolicitud()));
					hashAux.put("bean", bean);
					
					hash.clear();
					CenTipoSolicitudAdm admTS = new CenTipoSolicitudAdm (this.getUserBean(request));
					UtilidadesHash.set(hash, CenTipoSolicitudBean.C_IDTIPOSOLICITUD, 
							UtilidadesHash.getInteger(fila.getRow(), CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD));
					hashAux.put("tipoSol", ((CenTipoSolicitudBean)((admTS.select(hash)).get(0))).getDescripcion());
					
					hash.clear();
					CenEstadoSolicitudAdm admEstadoSol = new CenEstadoSolicitudAdm (this.getUserBean(request));
					UtilidadesHash.set(hash, CenEstadoSolicitudBean.C_IDESTADO, 
							UtilidadesHash.getInteger(fila.getRow(), CenSolicitudIncorporacionBean.C_IDESTADO));
					hashAux.put("estadoSol", ((CenEstadoSolicitudBean)((admEstadoSol.select(hash)).get(0))).getDescripcion());
					hashAux.put ("alarma", UtilidadesHash.getString(fila.getRow(), "CAMPOALARMA"));
					resultados.add (hashAux);
				}
				if (rc.size() < 1) resultados = null;
				request.setAttribute("resultados", resultados);
				
		        //Para volver correctamente desde envios:
		        request.getSession().setAttribute("EnvEdicionEnvio","GSI");
			}
	   } 
	   catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
	   return "resultados";
	} //buscarPor()
	
	/**  
	 * Devuelve el tipo de solicitud dependiendo del estado de cliente
	 * 
	 * @param Formulario 
	 * @param Usuario 
	 * @param Request
	 * @param numero de colegiado 
	 * @param mensaje de retorno si hay lagun error 
	 * @return el tipo de solicitud 
	 */
	private int validaTipoSolicitud(SolicitudIncorporacionForm f, 
									Integer usuario, 
									HttpServletRequest request,
									String nColegiado[], 
									String mensaje)
			throws SIGAException
	{
		CenPersonaBean persona = null;
		
		try {
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			try {
				persona = personaAdm.getPersonaNew(f.getNIFCIF().toUpperCase(), 
						f.getNombre(), f.getApellido1(), f.getApellido2());
			} catch (SIGAException siga){
				throw siga;
			}
			
			int tipoSolicitud = f.getTipoSolicitud().intValue();
			
			
			// Validamos el tipo de colegiacion
			if (f.getTipoColegiacion().intValue() == ClsConstants.TIPO_COLEGIACION_COMUNITARIO) {
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION) 
					//tipoSolicitud = ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_INCORPORACION; Se cambia por la incidencia inc-5096
					tipoSolicitud = ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION;
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION)
					//tipoSolicitud = ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_REINCORPORACION; Se cambia por la incidencia inc-5096
					tipoSolicitud = ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION;
			}
			
			// No existe la persona
			if (persona == null) {
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION) {
					return ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION;
				}
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_REINCORPORACION) {
					return ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_INCORPORACION;
				}
				return tipoSolicitud;
			}
			
			// Existe persona ...
			
			Long idPersona = persona.getIdPersona();
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			CenColegiadoBean colegiado = colegiadoAdm.getDatosColegiales(idPersona, this.getIDInstitucion(request));
			
			Hashtable estadoColegial = colegiadoAdm.getEstadoColegial(idPersona, this.getIDInstitucion(request));
			
			// No existe colegiado
			if (colegiado == null) {
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION) {
					return ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION;
				}
				if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_REINCORPORACION) {
					return ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_INCORPORACION;
				}
				return tipoSolicitud;
			}
			
			// Existe colegiado ...
			
			// Si el estado es distinto de Baja colegial
			Integer estado = UtilidadesHash.getInteger(estadoColegial, CenEstadoColegialBean.C_IDESTADO); 
			if (estado.intValue() != ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL && estado.intValue() != 
					ClsConstants.ESTADO_COLEGIAL_SUSPENSION)
			{
				// Desestimar solicitud y avisar
				mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request), 
						"censo.solicitudIncorporacion.literal.DesestimarSolicitud");
				return this.solicitudDesestima;
			}
			
			// El estado es Baja Colegial
			nColegiado[0] = colegiado.getNColegiado();
			if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_EJERCIENTE_INCORPORACION) {
				return ClsConstants.TIPO_SOLICITUD_EJERCIENTE_REINCORPORACION;
			}
			if (tipoSolicitud == ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_INCORPORACION) {
				return ClsConstants.TIPO_SOLICITUD_NOEJERCIENTE_REINCORPORACION;
			}
			return tipoSolicitud;
			
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);			
		}
		//Si llega hasta aqui es un error de solicitud de incorporacion
		return -2;
	} //validaTipoSolicitud()
	
	/** 
	 * Muestra los datos de la solicitud
	 * 
	 * @param Request request donde se guardan los datos para la jsp
	 * @param Bean Datos a visualizar 
	 * @return Forward a la pagina correspondiente.
	 *   Si Error: 'SolicitudIncorporacionError'.
	 *   Si OK: 'SolicitudIncorporacionDatos'.
	 *   Si exception: 'exception'
	 */
	private String mostrarDatosSolicitud(HttpServletRequest request, 
										 CenSolicitudIncorporacionBean bean, 
										 SolicitudIncorporacionForm miFormulario)
			throws SIGAException
	{
		try {
			String modoAnterior = miFormulario.getModo();
			if (bean == null) {
				return "SolicitudIncorporacionError";
			}
			
			Hashtable hash = new Hashtable();
			
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm (this.getUserBean(request));
			Vector documentos = admInstitucion.getDocumentacionAPresentar (bean.getIdInstitucion(), 
					bean.getIdTipoSolicitud(), bean.getIdTipoColegiacion(), bean.getIdModalidadDocumentacion()); 
			if (documentos != null) {
				
				CenDocumentacionPresentadaAdm admDocPresentada = new CenDocumentacionPresentadaAdm (this.getUserBean(request));
				UtilidadesHash.set(hash, CenDocumentacionPresentadaBean.C_IDSOLICITUD, bean.getIdSolicitud());
				Vector docPresentados = admDocPresentada.select(hash);
				hash.clear();
				
				Vector vDocumentosAPresentar = new Vector ();
				for (int i = 0; i < documentos.size(); i++) {
					Vector vDocu = new Vector();
					
					// Comprobamos si esta la documentacion
					String presentada = "false";
					CenDocumentacionSolicitudInstituBean documento = (CenDocumentacionSolicitudInstituBean) documentos.get(i);
					for (int k = 0; k < docPresentados.size(); k++) {
						if ((((CenDocumentacionPresentadaBean) docPresentados.get(k)).getIdDocumentacion()).equals
								(documento.getIdDocumentacionSolicitudInstitucion()))
						{
							presentada = "true";
							break;
						}	
					}
					vDocu.add(documento);
					vDocu.add(presentada);
					vDocumentosAPresentar.add (vDocu);
				}
				request.setAttribute("datosDocumentacion", vDocumentosAPresentar);
			}
			
			bean.setFechaEstado(GstDate.getFormatedDateShort("", bean.getFechaEstado()));
			bean.setFechaNacimiento(GstDate.getFormatedDateShort("", bean.getFechaNacimiento()));
			bean.setFechaSolicitud(GstDate.getFormatedDateShort("", bean.getFechaSolicitud()));
			if(bean.getFechaEstadoColegial()!=null){
				bean.setFechaEstadoColegial(GstDate.getFormatedDateShort("", bean.getFechaEstadoColegial()));
			}else{
				bean.setFechaEstadoColegial("");
			}
			request.setAttribute("datosPersonales", bean);
			
			hash.clear();
			CenTipoSolicitudAdm admTS = new CenTipoSolicitudAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenTipoSolicitudBean.C_IDTIPOSOLICITUD, bean.getIdTipoSolicitud());
			request.setAttribute("TipoSolicitud", ((CenTipoSolicitudBean)((admTS.select(hash)).get(0))).getDescripcion());
			
			hash.clear();
			CenTipoColegiacionAdm admTC = new CenTipoColegiacionAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenTipoColegiacionBean.C_IDCOLEGIACION, bean.getIdTipoColegiacion());
			request.setAttribute("TipoColegiacion", ((CenTipoColegiacionBean)((admTC.select(hash)).get(0))).getDescripcion());
			
			hash.clear();
			CenTratamientoAdm admT 	= new CenTratamientoAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenTratamientoBean.C_IDTRATAMIENTO, bean.getIdTratamiento());
			request.setAttribute("TipoTratamiento", ((CenTratamientoBean)((admT.select(hash)).get(0))).getDescripcion());
			
			hash.clear();
			CenEstadoCivilAdm admEC = new CenEstadoCivilAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenEstadoCivilBean.C_IDESTADO, bean.getIdEstadoCivil());
			
			if (bean.getIdEstadoCivil()!=null) {
				request.setAttribute("TipoEstadoCivil", ((CenEstadoCivilBean)((admEC.select(hash)).get(0))).getDescripcion());
			}
			
			hash.clear();
			CenProvinciaAdm admPro	= new CenProvinciaAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenProvinciaBean.C_IDPROVINCIA, bean.getIdProvincia());
			Vector v = admPro.select(hash);
			if (v!=null && v.size()>0) {
				request.setAttribute("Provincia", ((CenProvinciaBean)(v.get(0))).getNombre());
			} else {
				request.setAttribute("Provincia", "");
			}
			
			hash.clear();
			CenPoblacionesAdm admPob = new CenPoblacionesAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenPoblacionesBean.C_IDPOBLACION, bean.getIdPoblacion());
			Vector v2 = admPob.select(hash);
			if (v2!=null && v2.size()>0) {
				request.setAttribute("Poblacion", ((CenPoblacionesBean)(v2.get(0))).getNombre());
			} else {
				request.setAttribute("Poblacion", bean.getPoblacionExtranjera());
			}
			
			hash.clear();
			CenPaisAdm admPais	= new CenPaisAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenPaisBean.C_IDPAIS, bean.getIdPais());
			Vector v3 = admPais.select(hash);
			if (v3!=null && v3.size()>0) {
				request.setAttribute("Pais", ((CenPaisBean)(v3.get(0))).getNombre());
			} else {
				request.setAttribute("Pais", "");
			}
			
			hash.clear();
			CenEstadoSolicitudAdm admEstadoSol = new CenEstadoSolicitudAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenEstadoSolicitudBean.C_IDESTADO, bean.getIdEstado());
			request.setAttribute("EstadoSolicitud", ((CenEstadoSolicitudBean)((admEstadoSol.select(hash)).get(0))).getDescripcion());

			
			hash.clear();
			CenDocumentacionModalidadAdm admDocModalidad = new CenDocumentacionModalidadAdm (this.getUserBean(request));
			UtilidadesHash.set(hash, CenDocumentacionModalidadBean.C_IDINSTITUCION, bean.getIdInstitucion());
			UtilidadesHash.set(hash, CenDocumentacionModalidadBean.C_IDMODALIDAD, bean.getIdModalidadDocumentacion());
			request.setAttribute("ModalidadDocumentacion", ((CenDocumentacionModalidadBean)((admDocModalidad.select(hash)).get(0))).getDescripcion());
			
			
			request.setAttribute("ModoAnterior", modoAnterior);
			/*
			if(!miFormulario.getModo().equalsIgnoreCase("Editar")&&!miFormulario.getModo().equalsIgnoreCase("Ver")){
				request.setAttribute("isPosibilidadSolicitudAlta", Boolean.FALSE);
				request.setAttribute("motivoSolicitudAlta", "");
				request.setAttribute("mostrarSolicitudAlta", false);
			}else{
				try {
					
					// Leemos el parametro que nos dirá si se activa el WS de la mutualidad o no
					GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
					String wsActivo = paramAdm.getValor(bean.getIdInstitucion().toString(), "CEN", "WS_MUTUALIDAD_ACTIVO", "0");
					
					if(wsActivo.equalsIgnoreCase("1")){
						if(bean.getIdTipoIdentificacion()==ClsConstants.TIPO_IDENTIFICACION_NIF || 
						   bean.getIdTipoIdentificacion()==ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE){
							BusinessManager bm = getBusinessManager();
							MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
							
								List<CenSolicitudMutualidadBean> solicitudMutualidadBeans=mutualidadService.getSolicitudesMutualidad(bean, this.getUserBean(request));
							
								if(solicitudMutualidadBeans!=null && solicitudMutualidadBeans.size()>0){
									for(CenSolicitudMutualidadBean solicitudMutualidadBean:solicitudMutualidadBeans){
										if(solicitudMutualidadBean.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL)){
											miFormulario.setIdSolicitudPlanProfesional(""+solicitudMutualidadBean.getIdSolicitud().toString());
											if(solicitudMutualidadBean.getIdSolicitudAceptada()!=null)
												miFormulario.setIdSolicitudAceptadaPlanProfesional(""+solicitudMutualidadBean.getIdSolicitudAceptada().toString());
											miFormulario.setEstadoSolicitudPlanProfesional(solicitudMutualidadBean.getEstado());
											miFormulario.setEstadoMutualistaPlanProfesional(solicitudMutualidadBean.getEstadoMutualista());
											
										}else if(solicitudMutualidadBean.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_SEGUROUNIVERSAL)){
											miFormulario.setIdSolicitudSeguroUniversal(""+solicitudMutualidadBean.getIdSolicitud().toString());
											if(solicitudMutualidadBean.getIdSolicitudAceptada()!=null)
												miFormulario.setIdSolicitudAceptadaSeguroUniversal(""+solicitudMutualidadBean.getIdSolicitudAceptada().toString());
											miFormulario.setEstadoSolicitudSeguroUniversal(solicitudMutualidadBean.getEstado());
											
										}
									}
									request.setAttribute("isPosibilidadSolicitudAlta", true);
									request.setAttribute("motivoSolicitudAlta", "");
								}else{
									RespuestaMutualidad respuestaSolicitudAlta = mutualidadService.isPosibilidadSolicitudAlta(bean.getNumeroIdentificador(),bean.getFechaNacimiento(),this.getUserBean(request));
									request.setAttribute("isPosibilidadSolicitudAlta", respuestaSolicitudAlta.isPosibleAlta());
									request.setAttribute("motivoSolicitudAlta", respuestaSolicitudAlta.getValorRespuesta());
									
									
								}
							
						}else{
							request.setAttribute("isPosibilidadSolicitudAlta", false);
							request.setAttribute("motivoSolicitudAlta", "Solo se puede solicitar el alta con NIF o NIE");
						}
						request.setAttribute("mostrarSolicitudAlta", true);
					}else{
						request.setAttribute("isPosibilidadSolicitudAlta", false);
						request.setAttribute("mostrarSolicitudAlta", false);
						request.setAttribute("motivoSolicitudAlta", "WebService de la Mutualidad de la Abogacia no activo");
					}
				} catch (SIGAException e) {
				*/
					//Que hacemos si falla!! aHORA MISMO NO SE MOSTRARIA LA PARTE DEL FORMULARIO DONDE ESTAN LOS BOTONES
					//Y EL ESTADO DEL ALTA EN LA MUTUALIDAD
					request.setAttribute("isPosibilidadSolicitudAlta", false);
					request.setAttribute("mostrarSolicitudAlta", false);
					request.setAttribute("motivoSolicitudAlta", "WebService de la Mutualidad de la Abogacia no activo");
				//}
			//}
			
	   } 
	   catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
	   //return "SolicitudIncorporacionDatos";
	   return "inicio";
	} //mostrarDatosSolicitud()
	
	/** 
	 * Valida el cambio del estado del tipo de la solicitud
	 *  
	 * @param idSolicitud a modificar
	 * @param Nuevo idTipoSolicitud  
	 * @param Mensaje son el posible mensaje de error a mostrar en la jsp
	 * @return boolean true si es valido el cambio, false en caso contrario
	 * @exception SigaException 
	 */
	private Integer getTipoSolicitud (Long idSolicitud, UsrBean usr)
			throws ClsExceptions
	{
		// Cogemos los datos almacenados de la solicitud
		Hashtable claves = new Hashtable ();
		UtilidadesHash.set(claves, CenSolicitudIncorporacionBean.C_IDSOLICITUD,
				idSolicitud);
		CenSolicitudIncorporacionAdm solAdm = 
				new CenSolicitudIncorporacionAdm (usr);
		Vector v = null;
		v = solAdm.selectByPK(claves);
		if (v.size() != 1) {
			//return false;
			throw new ClsExceptions
					("no se ha encontrado el registro de la solicitud");
		}
		CenSolicitudIncorporacionBean b = (CenSolicitudIncorporacionBean) v.get(0);
		Integer idTipoSol = b.getIdEstado();
		
		return idTipoSol;
	} //getTipoSolicitud()
	
	/** 
	 * Valida el cambio del estado del tipo de la solicitud
	 *  
	 * @param idSolicitud a modificar
	 * @param Nuevo idTipoSolicitud  
	 * @param Mensaje son el posible mensaje de error a mostrar en la jsp
	 * @return boolean true si es valido el cambio, false en caso contrario
	 * @exception SigaException 
	 */
	private boolean validaModificacionTipoSolicitud(Integer idTipoSolicitudActual, 
													Integer idTipoSolicitudNuevo, 
													String []mensaje)
			throws SIGAException
	{
		try {
			mensaje[0] = "";
			Integer idTipoSol = idTipoSolicitudActual;
			// Validamos flujo para el tipo de la solicitud
			if (idTipoSol.intValue() == idTipoSolicitudNuevo.intValue())
				return true;
			
			// Si APROBADA -> no se puede modificar el estado
			if ((idTipoSol.intValue() == ClsConstants.ESTADO_SOLICITUD_APROBADA)) {
				if (idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_APROBADA) { 
					return true;
				}
				else {
					mensaje[0] = "messages.censo.solicitudIncorporacion.errorCambioEstado";
					return false;
				}
			}
			
			// Si PENDIENTE_DOC -> DENEGADA o PENDIENTE_APROBAR
			if ((idTipoSol.intValue() == ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC)) { 
				if ((idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_DENEGADA) ||
					(idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_PENDIENTE_APROBAR)) { 
					return true;
				}
				else {
					mensaje[0] = "messages.censo.solicitudIncorporacion.errorCambioEstado";
					return false;
				}
			}
			
			// Si PENDIENTE_APROBAR -> DENEGADA, PENDIENTE_DOC o APROBADA
			if ((idTipoSol.intValue() == ClsConstants.ESTADO_SOLICITUD_PENDIENTE_APROBAR)) {
				if ((idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_DENEGADA) ||
				    (idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_PENDIENTE_DOC) || 
					(idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_APROBADA)) { 
					return true;
				}
				else {
					mensaje[0] = "messages.censo.solicitudIncorporacion.errorCambioEstado";
					return false;
				} 
			}
			
			// Si SUSPENDIDA -> PENDIENTE_APROBAR
			if ((idTipoSol.intValue() == ClsConstants.ESTADO_SOLICITUD_SUSPENDIDA)) {
				if (idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_PENDIENTE_APROBAR) { 
					return true;
				}
				else {
					mensaje[0] = "messages.censo.solicitudIncorporacion.errorCambioEstado";
					return false;
				}
			}
			
			// Si DENEGADA -> SUSPENDIDA
			if ((idTipoSol.intValue() == ClsConstants.ESTADO_SOLICITUD_DENEGADA)) {
				if (idTipoSolicitudNuevo.intValue() == ClsConstants.ESTADO_SOLICITUD_SUSPENDIDA) { 
					return true;
				}
				else {
					mensaje[0] = "messages.censo.solicitudIncorporacion.errorCambioEstado";
					return false;
				}
			}
			mensaje[0] = "messages.censo.solicitudIncorporacion.errorCambioEstado";
			return false;
	   } 
	   catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
  	   }
	   return false;
	} //validaModificacionTipoSolicitud()
	
	private boolean noExisteNumeroSolicitud (String num, UsrBean usr)
			throws ClsExceptions
	{
		boolean noExiste = true;
		CenSolicitudIncorporacionAdm solicitudAdm = new CenSolicitudIncorporacionAdm (usr);
		
		String where = " WHERE "+CenSolicitudIncorporacionBean.C_IDSOLICITUD+"="+num;
		Vector v = solicitudAdm.select(where);
		if (v != null && v.size() > 0)
			noExiste  = false;
		else 
			noExiste  = true;
		return noExiste;
	} //noExisteNumeroSolicitud()
	
	private Long generarIdSolicitud (UsrBean usr)
			throws ClsExceptions
	{
		Random ran = new Random();
		Long salida = null;
		String salidaString = null;
		boolean noEncontrado = true;
		long numRandom = 0;
		
		while (noEncontrado) {
			numRandom = Math.abs(ran.nextLong());
			
			//Trunco a 10 caracteres:
			salidaString = String.valueOf(numRandom); 
			if (salidaString.length() > 10){
				salidaString = salidaString.substring(0,10);			
			}
			
			//Chequeo si existe en Base de Datos:
			if (this.noExisteNumeroSolicitud(salidaString,usr))
				noEncontrado = false;
		}
				
		salida = new Long(salidaString);
		return salida;
	} //generarIdSolicitud()
	
	protected String abrirAvanzada (ActionMapping mapping,
									MasterForm formulario,
									HttpServletRequest request, 
									HttpServletResponse response)
			throws SIGAException
	{
		CenSolicitudIncorporacionAdm solicitudAdm = new CenSolicitudIncorporacionAdm (this.getUserBean(request));
		SolicitudIncorporacionForm miFormulario = (SolicitudIncorporacionForm)formulario;
		String forward="", idSolicitud="";
		Hashtable hash = new Hashtable();
		try {
			//Leo de sesion el idsolicitud y luego lo borro de la misma:
			idSolicitud = (String)request.getSession().getAttribute("idSolicitud");
			request.getSession().removeAttribute("idSolicitud");
			
			if (idSolicitud != null){
				UtilidadesHash.set(hash, CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
				Vector vDatos = solicitudAdm.selectByPK(hash);
				forward =  this.mostrarDatosSolicitud(request, 
						(CenSolicitudIncorporacionBean)vDatos.get(0), miFormulario);
			} else
				return exito("messages.general.error",request);
		} catch (Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		request.setAttribute("ModoAnterior","INSERTAR");
		return forward;
	} //abrirAvanzada()
	
	protected CenPersonaBean obtenerDatosPersona (String nif,
												  HttpServletRequest request)
			throws ClsExceptions
	{
	  	try {
			CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
			Vector personas = perAdm.select("WHERE UPPER(" + CenPersonaBean.C_NIFCIF + ") = '" + nif.toUpperCase() + "'");
			
			CenPersonaBean perBean = null;
			if ((personas != null) && personas.size() == 1)
				perBean = (CenPersonaBean)personas.get(0);
			
			return perBean;
	  	}
	  	catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	} //obtenerDatosPersona()
	
	private String validarTipoCuenta (Boolean abono, Boolean cargo) throws SIGAException{
		try {
			String tipoCuenta = "";
			if (abono.booleanValue()) tipoCuenta = ClsConstants.TIPO_CUENTA_ABONO;
			if (cargo.booleanValue()) tipoCuenta = ClsConstants.TIPO_CUENTA_CARGO;
			if (abono.booleanValue() && cargo.booleanValue()) tipoCuenta = ClsConstants.TIPO_CUENTA_ABONO_CARGO;
			return tipoCuenta;
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return null;
	}
	
	protected String exitoGuardar(String mensaje, HttpServletRequest request) 
	{
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("modal","editar");
		return "exitoGuardar"; 
	}
	
	/**
	 * aalg. inc. 47. Comprobar si existe el colegiado
	 * 
	 */
	protected void getAjaxExisteColegiado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {
		
		// obtener institucion
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idInstitucion=user.getLocation();
		CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		
		String nColegiado = (String)request.getParameter("nColegiado");
		if (nColegiado==null||nColegiado.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el número del colegiado");	
				
		String colegiado = colegiadoAdm.getIdPersona(nColegiado, idInstitucion);
		
		JSONObject json = new JSONObject();	
		String mensaje ="";
		if (colegiado != null)
			mensaje = UtilidadesString.getMensajeIdioma (user, "error.message.NumColegiadoRepetido");
		json.put("mensaje", mensaje);
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 
	}	
}
