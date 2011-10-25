/*
 * Created on Jan 17, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.DocuShareHelper;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoConfAdm;
import com.siga.beans.ExpCampoConfBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpCamposValorAdm;
import com.siga.beans.ExpCamposValorBean;
import com.siga.beans.ExpClasificacionesAdm;
import com.siga.beans.ExpClasificacionesBean;
import com.siga.beans.ExpDenuncianteBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpPlazoEstadoClasificacionAdm;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.expedientes.form.ExpDatosGeneralesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.xerox.docushare.DSException;

/**
 * Action Datos Generales de un Expediente
 */
public class ExpDatosGeneralesAction extends MasterAction 
{
	static public final String C_NOMBRETIPOEXPEDIENTE = "NOMBRETIPOEXPEDIENTE";
	static public final String C_NOMBREINSTITUCION = "NOMBREINSTITUCION";
	static public final String C_NOMBREPERSONA = "NOMBREPERSONA";
	static public final String C_NOMBREDENUNCIANTE = "NOMBREDENUNCIANTE";
	static public final String C_APELLIDO1DENUNCIANTE = "APELLIDO1DENUNCIANTE";
	static public final String C_APELLIDO2DENUNCIANTE = "APELLIDO2DENUNCIANTE";
	static public final String C_NIFDENUNCIANTE = "NIFDENUNCIANTE";	
					
	public static Hashtable<String,Integer> ultimoAniosNumExpediente = new Hashtable<String, Integer>();
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			// Aplicar visibilidad a campos
			ExpCampoTipoExpedienteAdm adm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
			Hashtable h = new Hashtable();
			String idTipoExpediente="";
			/////
			String accion = (String)request.getParameter("accion");
			if(request.getParameter("idTipoExpediente")==null || request.getParameter("idTipoExpediente").toString().trim().equals("")){
				 idTipoExpediente =nuevo( mapping,  formulario,  request,  response);
				 accion = "nuevo";
			}else
			     idTipoExpediente =(String)request.getParameter("idTipoExpediente");
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_MINUTA_INICIAL)); // Minuta
			Vector v = adm.select(h);
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean)v.get(0);
				request.setAttribute("mostarMinuta", b.getVisible());
			}
			
			//Minuta Final
			h = new Hashtable();
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_MINUTA_FINAL)); // Minuta final
			v = adm.select(h);
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean)v.get(0);
				request.setAttribute("mostarMinutaFinal", b.getVisible());
			}
			
			
			//Derechos colegiales
			h = new Hashtable();
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DERECHOS_COLEGIALES)); // Derechos
			v = adm.select(h);
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean)v.get(0);
				request.setAttribute("derechosColegiales", b.getVisible());
			}
			
			
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE)); // Denunciante/Impugnante
			v = adm.select(h);
			
			//Obtenemos el Titulo Impugnante o Denunciante
			
			String nombreAux = "pestana.auditoriaexp.interesado";
			
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean aux = (ExpCampoTipoExpedienteBean)v.get(0);
				aux = (ExpCampoTipoExpedienteBean)v.get(0);
				request.setAttribute("mostrarDenunciante",aux.getVisible());
				nombreAux = aux.getNombre();
				if(nombreAux ==null||nombreAux.equals(""))
					nombreAux = ExpCampoTipoExpedienteBean.DENUNCIANTE;
			}
			
			//Comprobamos si tiene visible la pestaña denunciante. Si no la tiene ponemos interesado
			//si esta visible la descripcion del campo dependera de lo que haya seleccionado
			//en la configuiracion del tipo de expediente
			String nombre = "pestana.auditoriaexp.interesado";
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean)v.get(0);
				
				if(b.getVisible()!=null && b.getVisible().equals(ExpCampoTipoExpedienteBean.si)){
				
					UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIADO)); // Denunciado/Impugnado
					v = adm.select(h);
					if (v != null && v.size() > 0) {
						b = (ExpCampoTipoExpedienteBean)v.get(0);
						request.setAttribute("mostrarDenunciado",b.getVisible());
						nombre = b.getNombre();
						if(nombre ==null||nombre.equals(""))
							nombre = ExpCampoTipoExpedienteBean.DENUNCIADO;
					}
					
					
			}
			request.setAttribute("tituloDenunciado", nombre);
			request.setAttribute("tituloDenunciante", nombreAux);
			
			}
			///// Obtenemos el num expediente o num ejg
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_N_DISCIPLINARIO)); // Denunciante/Impugnante
			v = adm.select(h);
			
			String nombreAux2 = "expedientes.auditoria.literal.nexpdisciplinario";
			
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean aux2 = (ExpCampoTipoExpedienteBean)v.get(0);
				aux2 = (ExpCampoTipoExpedienteBean)v.get(0);
				request.setAttribute("mostrarNumExpNumEjg",aux2.getVisible());
				nombreAux2 = aux2.getNombre();
				if(nombreAux2 ==null||nombreAux2.equals(""))
					nombreAux2 = ExpCampoTipoExpedienteBean.NUMEXPEDIENTE;
			}
			
			
			request.setAttribute("tituloExpDisciplinario", nombreAux2);
			
			if (accion!=null && accion.equals("nuevo")){				
				return abrirNuevo(mapping, formulario, request, response);			
			}else{
				return abrirExistente(mapping, formulario, request, response);
			}
		}catch(Exception e){		
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
			return "exception";
		}
		
	}
	
	/** 
	 * Funcion que inicializa el formulario en modo para inserción
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response 
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @return String para el forward
	 */
	
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
		Integer idTipoExpediente =null;
		try{
			 Hashtable datosTipoExpediente = new Hashtable();
			ExpExpedienteAdm expedientesAdm = new ExpExpedienteAdm(this.getUserBean(request));
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			form.setTipoExpDisciplinario(request.getParameter("idTipoEjg"));
			form.setAnioExpDisciplinario(request.getParameter("anioEjg"));
			form.setNumExpDisciplinario(request.getParameter("numeroEjg"));
			form.setObservaciones(UtilidadesString.getMensajeIdioma(this.getUserBean(request).getLanguage(),"gratuita.EJG.solicitante")+request.getParameter("nifSolicitante")+", "+request.getParameter("nombreSolicitante"));
			form.setProcedimiento("1");
			form.setNumAsunto(request.getParameter("numeroProcedimiento"));
			form.setAsunto(request.getParameter("asunto"));
			form.setJuzgado(request.getParameter("juzgado"));
			form.setIdInstitucionJuzgado(request.getParameter("juzgadoInstitucion"));
			form.setIdPretension(request.getParameter("pretension"));
			form.setNumColegiado(request.getParameter("numColDesignado"));
			//form.setIdPersona(request.getParameter("nombreDesignado"));
			String institucion = (String)request.getParameter("idInstitucion_TipoExpediente");
			 idTipoExpediente = expedientesAdm.selectTipoExpedienteEJG(institucion);
			 form.setTipoExpediente(idTipoExpediente.toString());
			 request.setAttribute("idTipoExpediente",idTipoExpediente.toString());
			datosTipoExpediente.put("idInstitucion_TipoExpediente",institucion);
			datosTipoExpediente.put("idTipoExpediente",idTipoExpediente.toString());
			
			datosTipoExpediente.put("anioEjg",request.getParameter("anioEjg"));
			datosTipoExpediente.put("numeroEjg",request.getParameter("numeroEjg"));
			datosTipoExpediente.put("idTipoEjg",request.getParameter("idTipoEjg"));
			datosTipoExpediente.put("asunto",request.getParameter("asunto"));			
			datosTipoExpediente.put("observaciones", UtilidadesString.getMensajeIdioma(this.getUserBean(request).getLanguage(),"gratuita.EJG.solicitante")+request.getParameter("nifSolicitante")+", "+request.getParameter("nombreSolicitante"));
			datosTipoExpediente.put("numeroProcedimiento",request.getParameter("numeroProcedimiento"));	
			datosTipoExpediente.put("anioEjg",request.getParameter("anioEjg"));
			datosTipoExpediente.put("juzgado",request.getParameter("juzgado"));
			datosTipoExpediente.put("juzgadoInstitucion",request.getParameter("juzgadoInstitucion"));
			datosTipoExpediente.put("pretension",request.getParameter("pretension"));
			datosTipoExpediente.put("pretensionInstitucion",request.getParameter("pretensionInstitucion"));
			datosTipoExpediente.put("idturnoDesignado",request.getParameter("idturnoDesignado"));
			datosTipoExpediente.put("nombreDesignado",request.getParameter("nombreDesignado"));
			datosTipoExpediente.put("numColDesignado",request.getParameter("numColDesignado"));


			request.setAttribute("datosTipoExpediente", datosTipoExpediente);
			//select  IDTIPOEXPEDIENTE from  exp_tipoexpediente WHERE IDINSTITUCION = 2040 AND RELACIONEJG = 1
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return idTipoExpediente.toString();
		
	}
	protected String abrirNuevo(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException{
		String metodo ="";
		try{
		
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
			ExpTipoExpedienteAdm tipoAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
			HttpSession ses = request.getSession();
			UsrBean user = (UsrBean)ses.getAttribute("USRBEAN");
			ses.removeAttribute("DATABACKUP");
			String idTipoExpediente = "";
			String idInstitucion_TipoExpediente = "";
			String idInstitucion = user.getLocation();
			if(request.getParameter("idInstitucion_TipoExpediente")!=null && !request.getParameter("idInstitucion_TipoExpediente").toString().trim().equals(""))
				idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			else 
				idInstitucion_TipoExpediente = this.getUserBean(request).getLocation();
			if(request.getParameter("idTipoExpediente")!=null && !request.getParameter("idTipoExpediente").toString().trim().equals(""))
				 idTipoExpediente = request.getParameter("idTipoExpediente");
			else
				 idTipoExpediente = form.getTipoExpediente();
			//String nombreTipoExpediente = request.getParameter("nombreTipoExpediente");
			request.setAttribute("idTipoExpediente", idTipoExpediente);
			request.setAttribute("idInstitucion_TipoExpediente", idInstitucion_TipoExpediente);
			//Recupero el nuevo numero/anio expediente
			Hashtable hash = new Hashtable();
			hash.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			
			//Hashtable numAnioExp = expAdm.getNewNumAnioExpediente(hash);
			
			//Recupero el nombre del tipo de expediente
			ExpTipoExpedienteAdm teAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
		    Hashtable hte= new Hashtable();
		    hte.put(ExpTipoExpedienteBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
		    hte.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
		    ExpTipoExpedienteBean teBean = (ExpTipoExpedienteBean)teAdm.select(hte).elementAt(0);
			
		    //Sets del formulario, se inicializan Num.Expediente y Anio Expediente a "" por que no se debe de pasar 
		    //ningún valor al crearse un registro nuevo
		    form.setNumExpediente("");
			form.setAnioExpediente("");
			form.setTipoExpediente(teBean.getNombre());
			
			//obtenemos la hash con los campos a mostrar
			Hashtable camposVisibles =  tipoAdm.getCamposVisibles(idInstitucion_TipoExpediente,idTipoExpediente);
			request.setAttribute("camposVisibles",camposVisibles);
	
			//obtenemos el nombre de la institución
			CenInstitucionAdm instAdm = new CenInstitucionAdm(this.getUserBean(request));
			form.setInstitucion(instAdm.getNombreInstitucion(user.getLocation()));

			
			//permitimos que desde las pestanhas, pueda volver a la búsqueda
			if (request.getSession().getAttribute("volverAuditoriaExpedientes")==null){
				request.getSession().setAttribute("volverAuditoriaExpedientes","N"); // busqueda normal	
			}
			 metodo = (String)request.getParameter("metodo");
					
			if(	request.getParameter("tipoExpDis")!=null && !request.getParameter("tipoExpDis").toString().trim().equals("")){
					form.setTipoExpDisciplinario((String)request.getParameter("tipoExpDis"));
					form.setAnioExpDisciplinario((String)request.getParameter("anioExpDis"));
					form.setNumExpDisciplinario(new Integer((String)request.getParameter("numExpDis")).toString());
			}
			if (metodo!=null && metodo.equals("abrirNuevoEjg")){	
				
				//form.setTipoExpDisciplinario(request.getParameter("idTipoEjg"));
				//form.setAnioExpDisciplinario(request.getParameter("anioEjg"));
				//form.setNumExpDisciplinario(new Integer((String)request.getParameter("numeroEjg")).toString());
				//form.setObservaciones(request.getParameter("observaciones"));
				String proc = form.getProcedimiento()+","+idInstitucion;
				form.setProcedimiento(proc);
				//form.setNumAsunto(request.getParameter("numeroProcedimiento"));
				//form.setAsunto(request.getParameter("asunto"));
				String juz = form.getJuzgado()+","+form.getIdInstitucionJuzgado();
				form.setJuzgado(juz);
				//form.setIdInstitucionJuzgado(request.getParameter("juzgadoInstitucion"));
				//form.setIdPretension(request.getParameter("pretension"));
				
				Date hoy = new Date();
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
				String sHoy = sdf2.format(hoy);
				form.setFecha(sHoy);				
				request.getParameter("pretensionInstitucion");
				request.getParameter("numDesignado");
				request.getParameter("nombreDesignado");
				request.setAttribute("numeroExpediente", "1");
				request.setAttribute("anioExpediente", "3");
				request.setAttribute("soloSeguimiento", false);
				request.setAttribute("editable", "1");
				request.setAttribute("idclasificacion","1");
				request.setAttribute("soloSeguimiento", false);
				request.setAttribute("idProcedimiento", form.getProcedimiento());
				request.setAttribute("idInstitucionProcedimiento", idInstitucion);
				request.setAttribute("idTipoExpediente", idTipoExpediente);
				request.setAttribute("idTExpediente", idTipoExpediente);	
				form.setNumExpDisciplinarioCalc(idTipoExpediente);
				form.setIdTipoExpediente(idTipoExpediente);
				ScsTurnoAdm turnoAdm = new ScsTurnoAdm(user);
				Hashtable hashTurno = new Hashtable();
				hashTurno.put(ScsTurnoBean.C_IDTURNO,(String)request.getParameter("idturnoDesignado"));
				hashTurno.put(ScsTurnoBean.C_IDINSTITUCION,idInstitucion);
				ScsTurnoBean turnoBean = (ScsTurnoBean)((Vector)turnoAdm.select(hashTurno)).get(0);
				String mat=turnoBean.getIdMateria().toString();
				Integer area=turnoBean.getIdArea();
				form.setIdMateria(idInstitucion+","+area.toString()+","+mat);
				form.setIdArea(area.toString());
				String idpersona= (String)request.getParameter("nombreDesignado");
				form.setIdPersona(idpersona);
				form.setNumColegiado((String)request.getParameter("numColDesignado"));
				//form.setIdPersona(numCol);
				CenClienteBean cliente = null;
				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
				Vector v = clienteAdm.getDatosPersonales(new Long(idpersona),new Integer(idInstitucion));
				if (v!=null && v.size()>0) {
					Hashtable registro =  (Hashtable)v.get(0);
					if (registro.get("NOMBRE")!=null) form.setNombre((String) registro.get("NOMBRE") ); 
					if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) form.setPrimerApellido((String) registro.get("APELLIDOS1")); 
					if (registro.get("APELLIDOS2")!=null) form.setSegundoApellido((String) registro.get("APELLIDOS2")); 
					if (registro.get("NIFCIF")!=null) form.setNif((String) registro.get("NIFCIF")); 
					
				}
				
			}
			
			request.setAttribute("accion","nuevo");
			ses.setAttribute("DATABACKUP",hash);
			
			//Se pasa la fecha del sistema al campo Fecha de Apertura
			request.setAttribute("fechaApertura",(String)GstDate.getHoyJsp());
			
			//Se inicializa los campos numeroExpedienteSession y anioExpedienteSession a ""
			request.getSession().setAttribute("numeroExpedienteSession", "");
			request.getSession().setAttribute("anioExpedienteSession", "");

		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		if (metodo!=null && metodo.equals("abrirNuevoEjg"))	
			return("inicioEjg");
		else
			return("inicio");
		
	}

			
	/** 
	 * Funcion que inicializa el formulario en modo para edición o consulta
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response 
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @return String para el forward
	 */	
	protected String abrirExistente(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		boolean ejg=false;
		try{
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			ExpTipoExpedienteAdm tipoAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
			HttpSession ses = request.getSession();		
		
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
			
			//Se pregunta si numExpediente y anioExpediente vienes como parámetros en la request, si no vienen
			//se recuperan de la sesión
			if((numExpediente==null ||numExpediente.equals("")) && (anioExpediente==null ||anioExpediente.equals("")))
			{
				numExpediente = (String)request.getSession().getAttribute("numeroExpedienteSession");
				anioExpediente = (String)request.getSession().getAttribute("anioExpedienteSession");
			}

			String soloSeguimiento = request.getParameter("soloSeguimiento");
			String editable = request.getParameter("editable");
			boolean edit = (editable.equals("1")&&soloSeguimiento.equals("false"))?true:false;
			
			//obtenemos la hash con los campos a mostrar
			Hashtable camposVisibles =  tipoAdm.getCamposVisibles(idInstitucion_TipoExpediente,idTipoExpediente);
			request.setAttribute("camposVisibles",camposVisibles);
			String sEstado = (String)camposVisibles.get("3");
			boolean bEstado = (sEstado!=null && sEstado.equals("S"));
			
			//vectores para almacenar el resultado de las select concretas
			Vector datosExp = null;
			Vector datosFase = null;
			Vector datosEstado = null;
			Vector datosClasif = null;			
			
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));	
			
			//Si estamos en edición, recuperamos el bean para poner en backup
			if (edit){
				Hashtable hashExp = new Hashtable();		
				hashExp.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				hashExp.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				hashExp.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				hashExp.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				hashExp.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
				datosExp =expAdm.select(hashExp);
				ExpExpedienteBean expBean = (ExpExpedienteBean)datosExp.elementAt(0);
				HashMap datosExpediente=new HashMap();
				try{
					if (ses.getAttribute("DATABACKUP")!=null){
					   datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
					
					}
				}catch(Exception e){
					
				}
				datosExpediente.put("datosParticulares",expBean);
				ses.setAttribute("DATABACKUP",datosExpediente);
			
				//Metemos los datos no editables del expediente en Backup.
			    //Los datos particulares se anhadirán a la HashMap en cada caso.
			    Hashtable datosGenerales = new Hashtable();
			    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
			    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			    datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			    datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
			    datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
				datosExpediente.put("datosGenerales",datosGenerales);
				request.getSession().setAttribute("DATABACKUP",datosExpediente);
				
			}					
	
	        
	        //PRIMERA SELECT: EXP_EXPEDIENTE--------------------------
	        
	        
	        String whereCount = " WHERE ";
	        int numeroCount = 0;
	        
			whereCount += " DEN."+ExpDenuncianteBean.C_IDPERSONA+" = PER."+CenPersonaBean.C_IDPERSONA+"(+)";
			whereCount += "AND DEN."+ExpDenuncianteBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
			whereCount += "DEN."+ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
			whereCount += "DEN."+ExpDenuncianteBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";	
			whereCount += "DEN."+ExpDenuncianteBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
			whereCount += "DEN."+ExpDenuncianteBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "' ";			
			
			numeroCount = expAdm.getNumeroDenunciantes(whereCount); 
			
			String where = " WHERE ";
			
			if ( numeroCount > 0) {
			
				//join de las tablas EXPEDIENTE E, TIPOEXPEDIENTE T, PERSONA P, COLEGIADO C, INSTITUCION I
				where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = I."+CenInstitucionBean.C_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = P."+CenPersonaBean.C_IDPERSONA+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = C."+CenColegiadoBean.C_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = C."+CenColegiadoBean.C_IDPERSONA+"(+)";
				where += " AND DEN."+ExpDenuncianteBean.C_IDPERSONA+" = PER."+CenPersonaBean.C_IDPERSONA+"(+)";
	        
				where += "AND E."+ExpExpedienteBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
				where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
				where += "E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";
				where += "E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
				where += "E."+ExpExpedienteBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "' AND ";
				where += "DEN."+ExpDenuncianteBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
				where += "DEN."+ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
				where += "DEN."+ExpDenuncianteBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";	
				where += "DEN."+ExpDenuncianteBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
				where += "DEN."+ExpDenuncianteBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "' ";
			} else {
				//join de las tablas EXPEDIENTE E, TIPOEXPEDIENTE T, PERSONA P, COLEGIADO C, INSTITUCION I
				where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = I."+CenInstitucionBean.C_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = P."+CenPersonaBean.C_IDPERSONA+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = C."+CenColegiadoBean.C_IDINSTITUCION+"(+)";
				where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = C."+CenColegiadoBean.C_IDPERSONA+"(+)";
				
				where += "AND E."+ExpExpedienteBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
				where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
				where += "E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";
				where += "E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
				where += "E."+ExpExpedienteBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "' ";				
			}
				
			datosExp = expAdm.selectDatosGenerales(where, numeroCount);
				
	
			//Hacemos los sets del formulario
			Row fila = (Row)datosExp.elementAt(0);
			form.setTipoExpediente(fila.getString(C_NOMBRETIPOEXPEDIENTE));
			form.setNumExpediente(fila.getString(ExpExpedienteBean.C_NUMEROEXPEDIENTE));
			form.setAnioExpediente(fila.getString(ExpExpedienteBean.C_ANIOEXPEDIENTE));
			if(fila.getString(ExpExpedienteBean.C_IDTIPOEJG)!=null){
				ejg=true;
				form.setNumExpDisciplinario(fila.getString(ExpExpedienteBean.C_NUMEROEJG));
				form.setAnioExpDisciplinario(fila.getString(ExpExpedienteBean.C_ANIOEJG));
				form.setTipoExpDisciplinario(fila.getString(ExpExpedienteBean.C_IDTIPOEJG));
			}else{
				form.setNumExpDisciplinario(fila.getString(ExpExpedienteBean.C_NUMEXPDISCIPLINARIO));
				form.setAnioExpDisciplinario(fila.getString(ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO));
			}
			form.setFecha(GstDate.getFormatedDateShort("",fila.getString(ExpExpedienteBean.C_FECHA)));
			form.setInstitucion(fila.getString(C_NOMBREINSTITUCION));
			form.setAsunto(fila.getString(ExpExpedienteBean.C_ASUNTO));
			form.setIdPersona(fila.getString(ExpExpedienteBean.C_IDPERSONA));
			form.setIdDireccion(fila.getString(ExpExpedienteBean.C_IDDIRECCION));
			form.setNombre(fila.getString(C_NOMBREPERSONA));
			form.setPrimerApellido(fila.getString(CenPersonaBean.C_APELLIDOS1));
			form.setSegundoApellido(fila.getString(CenPersonaBean.C_APELLIDOS2));
			form.setNif(fila.getString(CenPersonaBean.C_NIFCIF));
			if (fila.getString(C_NOMBREDENUNCIANTE).equalsIgnoreCase("null")) {
				form.setNombreDenunciante("");
			} else {
				form.setNombreDenunciante(fila.getString(C_NOMBREDENUNCIANTE));
			}
			if (fila.getString(C_APELLIDO1DENUNCIANTE).equalsIgnoreCase("null")) {
				form.setPrimerApellidoDenunciante("");
			} else {
				form.setPrimerApellidoDenunciante(fila.getString(C_APELLIDO1DENUNCIANTE));
			}
			if (fila.getString(C_APELLIDO2DENUNCIANTE).equalsIgnoreCase("null")) {
				form.setSegundoApellidoDenunciante("");
			} else {
				form.setSegundoApellidoDenunciante(fila.getString(C_APELLIDO2DENUNCIANTE));
			}
			if (fila.getString(C_NIFDENUNCIANTE).equalsIgnoreCase("null")) {
				form.setNifDenunciante("");
			} else {
				form.setNifDenunciante(fila.getString(C_NIFDENUNCIANTE));	
			}	
			form.setNumColegiado(fila.getString(CenColegiadoBean.C_NCOLEGIADO));
			form.setIdAreaSolo(fila.getString(ExpExpedienteBean.C_IDAREA));
			form.setIdMateriaSolo(fila.getString(ExpExpedienteBean.C_IDMATERIA));
			form.setJuzgado(fila.getString(ExpExpedienteBean.C_JUZGADO));
			form.setProcedimiento(fila.getString(ExpExpedienteBean.C_PROCEDIMIENTO));
			form.setIdPretension(fila.getString(ExpExpedienteBean.C_IDPRETENSION));
			form.setOtrasPretensiones(fila.getString(ExpExpedienteBean.C_OTRASPRETENSIONES));
			form.setIdInstitucionJuzgado(fila.getString(ExpExpedienteBean.C_IDINSTITUCION_JUZGADO));
			form.setIdInstitucionProcedimiento(fila.getString(ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO));
			form.setNumAsunto(fila.getString(ExpExpedienteBean.C_NUMASUNTO));
			form.setFase(fila.getString(ExpExpedienteBean.C_IDFASE));
			form.setEstado(fila.getString(ExpExpedienteBean.C_IDESTADO));
			form.setClasificacion(fila.getString(ExpExpedienteBean.C_IDCLASIFICACION));
			form.setFechaInicial(GstDate.getFormatedDateShort("",fila.getString(ExpExpedienteBean.C_FECHAINICIALESTADO)));
			request.setAttribute("idinst_idtipo_idfase",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDFASE));
			request.setAttribute("idinst_idtipo_idfase_idestado",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDFASE)+","+fila.getString(ExpExpedienteBean.C_IDESTADO));
			request.setAttribute("idclasificacion",fila.getString(ExpExpedienteBean.C_IDCLASIFICACION));
			
			request.setAttribute("idJuzgado", fila.getString(ExpExpedienteBean.C_JUZGADO));
			request.setAttribute("idInstitucionJuzgado", fila.getString(ExpExpedienteBean.C_IDINSTITUCION_JUZGADO));
			request.setAttribute("idProcedimiento", fila.getString(ExpExpedienteBean.C_PROCEDIMIENTO));
			request.setAttribute("idInstitucionProcedimiento", fila.getString(ExpExpedienteBean.C_IDINSTITUCION_PROCEDIMIENTO));
			
			if (fila.getString(ExpExpedienteBean.C_FECHAFINALESTADO)!=null && !fila.getString(ExpExpedienteBean.C_FECHAFINALESTADO).equals("")){
				form.setFechaFinal(GstDate.getFormatedDateShort("",fila.getString(ExpExpedienteBean.C_FECHAFINALESTADO)));
			}else{
				form.setFechaFinal("");				
			}
			
			if (fila.getString(ExpExpedienteBean.C_FECHAPRORROGAESTADO)!=null && !fila.getString(ExpExpedienteBean.C_FECHAPRORROGAESTADO).equals("")){
				form.setFechaProrroga(GstDate.getFormatedDateShort("",fila.getString(ExpExpedienteBean.C_FECHAPRORROGAESTADO)));
			}else{
				form.setFechaProrroga("");				
			}		

			String sMinuta = fila.getString(ExpExpedienteBean.C_MINUTA); 
			if (sMinuta != null && !sMinuta.equals("")) {
				double minuta = Double.parseDouble(sMinuta);
				form.setMinuta("" + UtilidadesNumero.formatoCampo(minuta));
				//if (fila.getString("VALOR_IVA") != null)
				//	request.setAttribute("idTipoIVA", fila.getString("VALOR_IVA"));
			}
			String sImporteTotal = fila.getString(ExpExpedienteBean.C_IMPORTETOTAL); 
			if (sImporteTotal != null && !sImporteTotal.equals("")) {
				double ImporteTotal = Double.parseDouble(sImporteTotal);
				form.setImporteTotal("" + UtilidadesNumero.formatoCampo(ImporteTotal));
			}			
		
			String sPorcentajeIVA = fila.getString(ExpExpedienteBean.C_PORCENTAJEIVA); 
			if (sPorcentajeIVA != null && !sPorcentajeIVA.equals("")) {
				double PorcentajeIVA = Double.parseDouble(sPorcentajeIVA);
				form.setPorcentajeIVA("" + UtilidadesNumero.formatoCampo(PorcentajeIVA));
				form.setPorcentajeIVAFinal("" + UtilidadesNumero.formatoCampo(PorcentajeIVA));
			}	
			
			String sMinutaFinal = fila.getString(ExpExpedienteBean.C_MINUTAFINAL); 
			if (sMinutaFinal != null && !sMinutaFinal.equals("")) {
				double minuta = Double.parseDouble(sMinutaFinal);
				form.setMinutaFinal("" + UtilidadesNumero.formatoCampo(minuta));
			}
			String sImporteTotalFinal = fila.getString(ExpExpedienteBean.C_IMPORTETOTALFINAL); 
			if (sImporteTotalFinal != null && !sImporteTotalFinal.equals("")) {
				double ImporteTotal = Double.parseDouble(sImporteTotalFinal);
				form.setImporteTotalFinal("" + UtilidadesNumero.formatoCampo(ImporteTotal));
			}			
			
			String sDerechosColegiales = fila.getString(ExpExpedienteBean.C_DERECHOSCOLEGIALES); 
			if (sDerechosColegiales != null && !sDerechosColegiales.equals("")) {
				double derechos = Double.parseDouble(sDerechosColegiales);
				form.setDerechosColegiales("" + UtilidadesNumero.formatoCampo(derechos));
			}
			
			form.setObservaciones(fila.getString(ExpExpedienteBean.C_OBSERVACIONES));
			if (fila.getString(ExpExpedienteBean.C_FECHACADUCIDAD)!=null && !fila.getString(ExpExpedienteBean.C_FECHACADUCIDAD).equals("")){
				form.setFechaCaducidad(GstDate.getFormatedDateShort("",fila.getString(ExpExpedienteBean.C_FECHACADUCIDAD)));
			}
			
			//Si estamos en consulta, necesitamos el nombre de la clasificacion,
			//y si es visible el conjunto de campos 'estado' 
			//necesitamos el nombre de la fase y del estado
			//Modificado para que en consulta obtenga la fase siempre.
			if (!edit){
					//SEGUNDA SELECT: EXP_FASES---------------------------------------------
			
					ExpFasesAdm faseAdm = new ExpFasesAdm (this.getUserBean(request));		
					Hashtable hashFase = new Hashtable();
					
					hashFase.put(ExpFasesBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
					hashFase.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
					hashFase.put(ExpFasesBean.C_IDFASE,form.getFase());
					datosFase = faseAdm.select(hashFase);
					ExpFasesBean faseBean = (ExpFasesBean)datosFase.elementAt(0);
					
					form.setFaseSel(faseBean.getNombre());
					
					//TERCERA SELECT: EXP_ESTADOS
				if (bEstado){
			
					ExpEstadosAdm estadoAdm = new ExpEstadosAdm (this.getUserBean(request));		
					Hashtable hashEstado = new Hashtable();
					
					hashEstado.put(ExpEstadosBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
					hashEstado.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
					hashEstado.put(ExpEstadosBean.C_IDFASE,form.getFase());
					hashEstado.put(ExpEstadosBean.C_IDESTADO,form.getEstado());
					datosEstado = estadoAdm.select(hashEstado);
					ExpEstadosBean estadoBean = (ExpEstadosBean)datosEstado.elementAt(0);
					
					form.setEstadoSel(estadoBean.getNombre());
				}
				
				
				//CUARTA SELECT: EXP_CLASIFICACION
		
				ExpClasificacionesAdm clasifAdm = new ExpClasificacionesAdm (this.getUserBean(request));		
				Hashtable hashClasif = new Hashtable();
				
				hashClasif.put(ExpClasificacionesBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
				hashClasif.put(ExpClasificacionesBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				hashClasif.put(ExpClasificacionesBean.C_IDCLASIFICACION,form.getClasificacion());
				datosClasif = clasifAdm.select(hashClasif);
				ExpClasificacionesBean clasifBean = (ExpClasificacionesBean)datosClasif.elementAt(0);
				
				form.setClasificacionSel(clasifBean.getNombre());
				
				request.setAttribute("accion","consulta");
				request.setAttribute("tipoExpedienteEjg", Boolean.FALSE);
			}else{
				ExpExpedienteAdm exp = new ExpExpedienteAdm (this.getUserBean(request));
				Integer idTipoExpe = exp.selectTipoExpedienteEJG(this.getUserBean(request).getLocation());
							
				if(idTipoExpe != null && idTipoExpe.toString().equals(idTipoExpediente)){
					//if(form.getNumExpDisciplinario()!=null && !form.getNumExpDisciplinario().trim().equals(""))
						request.setAttribute("tipoExpedienteEjg", Boolean.TRUE);
				//	else
					//	request.setAttribute("tipoExpedienteEjg", Boolean.FALSE);
				}else{
					request.setAttribute("tipoExpedienteEjg", Boolean.FALSE);
				}
				
				request.setAttribute("accion","edicion");
			}
		
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
			return("inicio");
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		// Recuperamos los datos anteriores de la sesión		
		String forward = "";
	    HttpSession ses=request.getSession();
	    UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));        
	    HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
			UserTransaction tx = userBean.getTransactionPesada();
			try {
	    
		    	
		    ExpExpedienteBean expBean=(ExpExpedienteBean)datosExpediente.get("datosParticulares");
		    Integer idEstadoOld=expBean.getIdEstado();
		    Integer idFaseOld=expBean.getIdFase();
		    
		    
		    
		    ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
	        ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));
	        
	        
	        Hashtable hashExpOld = expAdm.beanToHashTableForUpdate(expBean);
	        
	        
	        //Averiguamos si el estado ha cambiado de valor
	        boolean estadoCambiado = (!form.getEstado().equals(String.valueOf(expBean.getIdEstado())) 
	        						  && form.getEstado()!=null && !form.getEstado().equals("")) 
									  ||
									 (!form.getFase().equals(String.valueOf(expBean.getIdFase())) 
									  && form.getFase()!=null && !form.getFase().equals(""));
	        //Si no tenia estado y no se lo doy no ha cambiado:
	        if (form.getEstado().equals("") && expBean.getIdEstado()==null)
	        	estadoCambiado = false;
	        
	        // Actualizamos los datos del expediente
	        expBean.setNumeroExpediente(Integer.valueOf(form.getNumExpediente()));
	        expBean.setAnioExpediente(Integer.valueOf(form.getAnioExpediente()));
	        expBean.setTipoExpDisciplinario(form.getTipoExpDisciplinario().equals("")?null:Integer.valueOf(form.getTipoExpDisciplinario()));
		    expBean.setNumExpDisciplinario(form.getNumExpDisciplinario().equals("")?null:Integer.valueOf(form.getNumExpDisciplinario()));
		    expBean.setAnioExpDisciplinario(form.getAnioExpDisciplinario().equals("")?null:Integer.valueOf(form.getAnioExpDisciplinario()));
	        
	        expBean.setAsunto(form.getAsunto());
	        expBean.setIdClasificacion(form.getClasificacion().equals("")?null:Integer.valueOf(form.getClasificacion()));
	        expBean.setJuzgado(form.getJuzgado());
	        expBean.setIdPretension(form.getIdPretension().equals("")?null:Integer.valueOf(form.getIdPretension()));
	        expBean.setOtrasPretensiones(form.getOtrasPretensiones());
	        expBean.setProcedimiento(form.getProcedimiento().equals("")?null:Integer.valueOf(form.getProcedimiento()));
	        expBean.setIdInstitucionJuzgado(form.getIdInstitucionJuzgado());
	        expBean.setIdInstitucionProcedimiento(form.getIdInstitucionProcedimiento());
	        expBean.setNumAsunto(form.getNumAsunto().equals("")?null:form.getNumAsunto());
	        expBean.setIdFase(form.getFase().equals("")?null:Integer.valueOf(form.getFase()));
	        expBean.setIdArea(form.getIdArea().equals("")?null:Integer.valueOf(form.getIdArea()));
	        expBean.setIdMateria(form.getIdMateria().equals("")?null:Integer.valueOf(form.getIdMateria()));
	        //expBean.setTipoExpDisciplinario(form.getTipoExpDisciplinario());
	        //expBean.setIdEstado(form.getEstado().equals("")?null:Integer.valueOf(form.getEstado()));
	        expBean.setIdPersona(Long.valueOf(form.getIdPersona()));
	        //if(form.getIdDireccion()!=null && !form.getIdDireccion().trim().equals(""))
	        	expBean.setIdDireccion(form.getIdDireccion());
	       // else
	        //	expBean.setIdDireccion(null);
	        	
	        if (form.getFechaInicial().trim().equals("")) {
	        	expBean.setFechaInicialEstado(GstDate.getApplicationFormatDate("",form.getFecha()));
	        } else {
	        	expBean.setFechaInicialEstado(GstDate.getApplicationFormatDate("",form.getFechaInicial()));
	        }
			//expBean.setFechaInicialEstado(form.getFechaInicial().equals("")?"":GstDate.getApplicationFormatDate("",form.getFechaInicial()));
	        if (form.getFechaFinal().equals("")) {
				//calculo la fecha final con este método, y me la devuelve en el bean
				/*ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm (this.getUserBean(request));
				if(expBean.getIdEstado()!=null)
					plazoAdm.establecerFechaFinal(expBean);*/
	        	// En vez de calcular la fecha mantenemos el valor nulo
	        	expBean.setFechaFinalEstado("");
	        } else {
	        	expBean.setFechaFinalEstado(GstDate.getApplicationFormatDate("",form.getFechaFinal()));
	        }
	        //expBean.setFechaFinalEstado(form.getFechaFinal().equals("")?"":GstDate.getApplicationFormatDate("",form.getFechaFinal()));
	        expBean.setFechaProrrogaEstado(form.getFechaProrroga().equals("")?"":GstDate.getApplicationFormatDate("",form.getFechaProrroga()));
	        
	        expBean.setFechaCaducidad(form.getFechaCaducidad().equals("")?"":GstDate.getApplicationFormatDate("",form.getFechaCaducidad()));
	        if (form.getObservaciones()!= null && !form.getObservaciones().equals(""))
	        	expBean.setObservaciones(form.getObservaciones());
	        if (form.getMinuta()!= null){
	        	if(!form.getMinuta().equals(""))
	        		try{
	        		expBean.setMinuta(new Double(form.getMinuta().trim()));
	        		}catch (Exception e) {
	        			String minuta = form.getMinuta().replace(',', '.');
	        			expBean.setMinuta(Double.parseDouble(minuta));
	    		    }
	        	else
	        		expBean.setMinuta(null);
	        }
			if (form.getImporteIVA()!= null){
				 if(!form.getImporteIVA().trim().equals("")) {
					 
					 try{
						 expBean.setImporteIVA(new Double(form.getImporteIVA()));
			        	}catch (Exception e) {
			        		String iva = form.getImporteIVA().replace(',', '.');
			        		expBean.setImporteIVA(Double.parseDouble(iva));
			    	    }
				 }else{
					 expBean.setImporteIVA(null);
				 }
			}
			if (form.getImporteTotal()!= null){
				if(!form.getImporteTotal().trim().equals("")) {
					 try{
						 expBean.setImporteTotal(new Double(form.getImporteTotal()));
						}catch (Exception e) {
			        		String impor = form.getImporteTotal().replace(',', '.');
			        		expBean.setImporteTotal(Double.parseDouble(impor));
			    	    }
				}else{
					expBean.setImporteTotal(null);
				}
			}
			if (form.getMinutaFinal()!= null){
				if(!form.getMinutaFinal().equals("")){
					 try{
						 expBean.setMinutaFinal(new Double(form.getMinutaFinal()));
						}catch (Exception e) {
			        		String minutaf = form.getMinutaFinal().replace(',', '.');
			        		expBean.setMinutaFinal(Double.parseDouble(minutaf));
			    	    }
				}else{
					expBean.setMinutaFinal(null);
				}
			}

			if (form.getImporteIVAFinal()!= null){
				if(!form.getImporteIVAFinal().trim().equals("")) {
					
					try{
						expBean.setImporteIVAFinal(new Double(form.getImporteIVAFinal()));
						}catch (Exception e) {
			        		String imporiva = form.getImporteIVAFinal().replace(',', '.');
			        		expBean.setImporteIVAFinal(Double.parseDouble(imporiva));
			    	    }
				}else{
					expBean.setImporteIVAFinal(null);
				}
			}
			if (form.getImporteTotalFinal()!= null) {
				if(!form.getImporteTotalFinal().trim().equals("")) {
					
					try{
						expBean.setImporteTotalFinal(new Double(form.getImporteTotalFinal()));
						}catch (Exception e) {
			        		String imporiva = form.getImporteTotalFinal().replace(',', '.');
			        		expBean.setImporteTotalFinal(Double.parseDouble(imporiva));
			    	    }
				}else{
					expBean.setImporteTotalFinal(null);
				}
			}
			
			if (form.getDerechosColegiales()!= null && !form.getDerechosColegiales().trim().equals("")) {
			   
			    try{
			    	 expBean.setDerechosColegiales(new Double(form.getDerechosColegiales()));
					}catch (Exception e) {
		        		String derechos = form.getDerechosColegiales().replace(',', '.');
		        		expBean.setDerechosColegiales(Double.parseDouble(derechos));
		    	    }
			}
			if (form.getPorcentajeIVA()!= null){
				if(!form.getPorcentajeIVA().trim().equals("")) {
					

			    	try{
						expBean.setPorcentajeIVA(new Double(form.getPorcentajeIVA()));
				    	expBean.setPorcentajeIVAFinal(new Double(form.getPorcentajeIVA()));
						}catch (Exception e) {
			        		String porceniva = form.getPorcentajeIVA().replace(',', '.');
			        		expBean.setPorcentajeIVA(Double.parseDouble(porceniva));
			        		expBean.setPorcentajeIVAFinal(Double.parseDouble(porceniva));
			    	    }
				}else{
					expBean.setPorcentajeIVA(null);
			    	expBean.setPorcentajeIVAFinal(null);
				}
			}
	        
	        if (form.getIdTipoIVA() != null && !form.getIdTipoIVA().equals("")) {
	        	String a = form.getIdTipoIVA().split(",")[0];
	        	expBean.setIdTipoIVA(new Integer(a));
	        }
	        
	        if (estadoCambiado && form.getEstado() != null && !form.getEstado().equals(""))
	            expBean.setIdEstado(new Integer(form.getEstado()));
	        
	    	//Recuperamos el nuevo bean de Estado
	        Hashtable hEstadoNew = new Hashtable();
	        hEstadoNew.put(ExpEstadosBean.C_IDINSTITUCION,expBean.getIdInstitucion_tipoExpediente());
	        hEstadoNew.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,expBean.getIdTipoExpediente());
	        hEstadoNew.put(ExpEstadosBean.C_IDFASE,form.getFase());
	        hEstadoNew.put(ExpEstadosBean.C_IDESTADO,form.getEstado());
	        ExpEstadosAdm estAdm = new ExpEstadosAdm(this.getUserBean(request));
	    	Vector vEstadoNew = estAdm.select(hEstadoNew);

	    	//Eliminada la captura de excepcion "ArrayIndexOutOfBoundsException"
	    	// porque no es necesaria si se programa correctamente
	    	//ExpEstadosBean estBeanNew = null;
	    	//try {
	    	//	estBeanNew = vEstadoNew.isEmpty()?
	    	//			null:(ExpEstadosBean)vEstadoNew.elementAt(0);
	    	//} catch (ArrayIndexOutOfBoundsException a){
	    	//	throw new ClsExceptions(a,"Error al obtener el estado");
	    	//}
	    	ExpEstadosBean estBeanNew = vEstadoNew.isEmpty()?
	    			null:(ExpEstadosBean)vEstadoNew.elementAt(0);
	    	
	    	//Iniciamos la transacción
	        
	        
	        
		        tx.begin();   
	    	    	
		        //Si cambia el estado, vamos a necesitar el anterior bean de estado
		        if (estadoCambiado){
		        	try{
			        	//Si el expediente tenía un estado anterior, se crean los seguimientos de un cambio de estado. 
			        	//Si no, se crean los seguimientos propios de un expediente nuevo.
			        	if (expBean.getIdFase()!=null && expBean.getIdEstado()!=null){
			        	
				        	//Recuperamos el antiguo bean de Estado
				            Hashtable hEstado = new Hashtable();
							UtilidadesHash.set(hEstado, ExpEstadosBean.C_IDINSTITUCION,expBean.getIdInstitucion_tipoExpediente());
							UtilidadesHash.set(hEstado, ExpEstadosBean.C_IDTIPOEXPEDIENTE,expBean.getIdTipoExpediente());
//				        	hEstado.put(ExpEstadosBean.C_IDFASE,expBean.getIdFase());
//				        	hEstado.put(ExpEstadosBean.C_IDESTADO,expBean.getIdEstado());
							UtilidadesHash.set(hEstado, ExpEstadosBean.C_IDFASE,idFaseOld);
							UtilidadesHash.set(hEstado, ExpEstadosBean.C_IDESTADO,idEstadoOld);
				        	
				        	Vector vEstado = estAdm.select(hEstado);
				        	ExpEstadosBean estBeanOld = (ExpEstadosBean)vEstado.elementAt(0);
				        	
				        	expAdm.cambioEstado(expBean,estBeanOld,estBeanNew,false);
			        	} else {
			        		expAdm.cambioEstado(expBean,null,estBeanNew,false);
			        	}
		        	}
		        	catch (Exception e) {
				    	throw new ClsExceptions(e,"Error al cambiar el estado");
		        	}
		        }
		
		        
		        //Transformamos el bean en hash para poder hacer update sobre campos numericos que se ponen a vacio:
		        Hashtable hashExp = new Hashtable();
		        hashExp = expAdm.beanToHashTableForUpdate(expBean);

		        
		        if (expAdm.update(hashExp, hashExpOld)){
		        	//Si ha cambiado el estado, averiguamos si el nuevo estado tiene ESEJECUCIONSANCION=S
		        	if (estadoCambiado){
		            	if (estBeanNew.getEjecucionSancion().equals("S")){
		            		Hashtable hEjSancion = new Hashtable();
		            		hEjSancion.put("idInstitucion",userBean.getLocation());
		            		hEjSancion.put("idPersona",form.getIdPersona());
		            		request.getSession().setAttribute("ejecucionSancion",hEjSancion);
		            		forward = "ejecucionSancion"; 
		            		
		            	}
		            }
		        	
		        }
		        tx.commit();
		        if(forward.equals(""))
					forward = exitoRefresco("messages.updated.success",request);
		    }catch (Exception e) {
		    	throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx); 
		    	forward = exito("messages.updated.error",request); 
		    }
		
		
		
		return forward;
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException, ClsExceptions {
		
		UserTransaction tx = null;
		try{
			
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
			ExpTipoExpedienteAdm expTipoAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			HttpSession ses = request.getSession();
			UsrBean user = (UsrBean)ses.getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();
			Hashtable hash = new Hashtable();
			
			if(request.getSession().getAttribute("DATABACKUP") instanceof Hashtable){
				hash = (Hashtable)request.getSession().getAttribute("DATABACKUP");	  

			}else if(request.getSession().getAttribute("DATABACKUP") instanceof HashMap<?, ?>){
				HashMap hashMap = (HashMap)request.getSession().getAttribute("DATABACKUP");	  
				hash = (Hashtable)hashMap.get("datosGenerales");
			}
			
			String idInstitucion_TipoExpediente = (String)hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE);
			String idTipoExpediente = (String)hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE);
				
			// obtenemos el tipo de expediente para ver el tiempo de caducidad y calcularlo
			Hashtable hashTipo = new Hashtable();
			hashTipo.put(ExpTipoExpedienteBean.C_IDINSTITUCION, idInstitucion_TipoExpediente);
			hashTipo.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
			Vector vTipo = expTipoAdm.selectByPK(hashTipo);
			Integer diasCaducidad = null;
			if (vTipo.size()>0) {
				ExpTipoExpedienteBean beanTipo = (ExpTipoExpedienteBean) vTipo.get(0);
				diasCaducidad = beanTipo.getTiempoCaducidad();
				if (diasCaducidad==null) diasCaducidad = new Integer(0);
			}
			
			
			//Rellenamos el nuevo Bean	    
			ExpExpedienteBean expBean = new ExpExpedienteBean();	    

			expBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			expBean.setIdInstitucion_tipoExpediente(Integer.valueOf(idInstitucion_TipoExpediente));
			expBean.setIdTipoExpediente(Integer.valueOf(idTipoExpediente));
			expBean.setIdPersona(form.getIdPersona().equals("")?null:Long.valueOf(form.getIdPersona()));   
			expBean.setNumExpDisciplinario(form.getNumExpDisciplinario().equals("")?null:Integer.valueOf(form.getNumExpDisciplinario()));
			expBean.setAnioExpDisciplinario(form.getAnioExpDisciplinario().equals("")?null:Integer.valueOf(form.getAnioExpDisciplinario()));
			if(form.getTipoExpDisciplinario()!=null && !form.getTipoExpDisciplinario().trim().equals(""))
				expBean.setTipoExpDisciplinario(new Integer(form.getTipoExpDisciplinario()));
			if (form.getFecha().equals("")) {
				expBean.setFecha("sysdate");
			} else {
				expBean.setFecha(GstDate.getApplicationFormatDate("",form.getFecha()));
			}
			
			if (form.getFechaCaducidad().equals("")) {
				/*
				if (expBean.getFecha().equals("sysdate")) {
					// tengo que obtener la fecha de hoy para trabajar con ella
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		            Date date = new Date();
		            expBean.setFechaCaducidad(GstDate.dateSumaDiasJava(dateFormat.format(date), diasCaducidad));
				} else {
					// tengo una fecha normal
					expBean.setFechaCaducidad(GstDate.dateSumaDiasJava(expBean.getFecha(), diasCaducidad));
				}
				*/
				expBean.setFechaCaducidad("");
				
			} else {
				expBean.setFechaCaducidad(GstDate.getApplicationFormatDate("",form.getFechaCaducidad()));
			}
			expBean.setAsunto(form.getAsunto());
			if (form.getObservaciones()!= null && !form.getObservaciones().equals(""))
	        	expBean.setObservaciones(form.getObservaciones());
			
			if (form.getMinuta()!= null && !form.getMinuta().trim().equals("")) {
			    expBean.setMinuta(new Double(form.getMinuta()));
			}
			if (form.getImporteIVA()!= null && !form.getImporteIVA().trim().equals("")) {
			    expBean.setImporteIVA(new Double(form.getImporteIVA()));
			}
			if (form.getImporteTotal()!= null && !form.getImporteTotal().trim().equals("")) {
			    expBean.setImporteTotal(new Double(form.getImporteTotal()));
			}
			if (form.getPorcentajeIVA()!= null && !form.getPorcentajeIVA().trim().equals("")) {
			    expBean.setPorcentajeIVA(new Double(form.getPorcentajeIVA()));
			}
			
			if (form.getPorcentajeIVAFinal()!= null && !form.getPorcentajeIVAFinal().trim().equals("")) {
			    expBean.setPorcentajeIVAFinal(new Double(form.getPorcentajeIVAFinal()));
			}
			
			if (form.getMinutaFinal()!= null && !form.getMinutaFinal().trim().equals("")) {
			    expBean.setMinutaFinal(new Double(form.getMinutaFinal()));
			}
			if (form.getImporteIVAFinal()!= null && !form.getImporteIVAFinal().trim().equals("")) {
			    expBean.setImporteIVAFinal(new Double(form.getImporteIVAFinal()));
			}
			if (form.getImporteTotalFinal()!= null && !form.getImporteTotalFinal().trim().equals("")) {
			    expBean.setImporteTotalFinal(new Double(form.getImporteTotalFinal()));
			}
			if (form.getDerechosColegiales()!= null && !form.getDerechosColegiales().trim().equals("")) {
			    expBean.setDerechosColegiales(new Double(form.getDerechosColegiales()));
			}
			if (form.getIdTipoIVA() != null && !form.getIdTipoIVA().equals("")) {
	        	String a = form.getIdTipoIVA().split(",")[0];
	        	expBean.setIdTipoIVA(new Integer(a));
	        }			
			expBean.setJuzgado(form.getJuzgado().equals("")?null:form.getJuzgado());
	        expBean.setIdPretension(form.getIdPretension().equals("")?null:Integer.valueOf(form.getIdPretension()));
	        expBean.setOtrasPretensiones(form.getOtrasPretensiones());
			expBean.setProcedimiento(form.getProcedimiento().equals("")?null:Integer.valueOf(form.getProcedimiento()));
			expBean.setIdInstitucionJuzgado(form.getIdInstitucionJuzgado().equals("")?null:form.getIdInstitucionJuzgado());
			expBean.setIdInstitucionProcedimiento(form.getIdInstitucionProcedimiento().equals("")?null:form.getIdInstitucionProcedimiento());
			expBean.setNumAsunto(form.getNumAsunto().equals("")?null:form.getNumAsunto());
			expBean.setIdClasificacion(form.getClasificacion().equals("")?null:Integer.valueOf(form.getClasificacion()));
			expBean.setIdFase(form.getFase().equals("")?null:Integer.valueOf(form.getFase()));
			expBean.setIdEstado(form.getEstado().equals("")?null:Integer.valueOf(form.getEstado()));
			if (form.getFechaInicial().trim().equals("")) {
	        	expBean.setFechaInicialEstado(expBean.getFecha());
	        } else {
	        	expBean.setFechaInicialEstado(GstDate.getApplicationFormatDate("",form.getFechaInicial()));
	        }
			//expBean.setFechaInicialEstado(form.getFechaInicial().equals("")?null:GstDate.getApplicationFormatDate("",form.getFechaInicial()));
        	if (form.getFechaFinal().trim().equals("")) {
				//calculo la fecha final con este método, y me la devuelve en el bean
				ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm (this.getUserBean(request));
				if (expBean.getIdEstado()!=null)
					plazoAdm.establecerFechaFinal(expBean);
	        } else {
	        	expBean.setFechaFinalEstado(GstDate.getApplicationFormatDate("",form.getFechaFinal()));
	        }
			//expBean.setFechaFinalEstado(form.getFechaFinal().equals("")?null:GstDate.getApplicationFormatDate("",form.getFechaFinal()));
			expBean.setFechaProrrogaEstado(form.getFechaProrroga().equals("")?null:GstDate.getApplicationFormatDate("",form.getFechaProrroga()));
			expBean.setIdArea(form.getIdArea().equals("")?null:Integer.valueOf(form.getIdArea()));
			expBean.setIdMateria(form.getIdMateria().equals("")?null:Integer.valueOf(form.getIdMateria()));
			expBean.setIdPretension(form.getIdPretension().equals("")?null:Integer.valueOf(form.getIdPretension()));
			expBean.setOtrasPretensiones(form.getOtrasPretensiones());
			expBean.setIdDireccion(form.getIdDireccion());
			expBean.setDescripcionResolucion("");
			expBean.setActuacionesPrescritas(null);
			expBean.setAnotacionesCanceladas(null);
			expBean.setSancionPrescrita(null);
			expBean.setSancionFinalizada(null);
			expBean.setSancionado("N");
			expBean.setAlertaGenerada("N");
			expBean.setAlertaGeneradaCad("N");
			expBean.setAlertaCaducidadGenerada("N");
			expBean.setAlertaFaseGenerada("N");
			expBean.setAlertaFinalGenerada("N");
			expBean.setEsVisible("N");
			expBean.setEsVisibleEnFicha("N");
			
			Integer numExpAGuardar =null;
			Integer anioExpAGuardar = null;
			Date fechaActual = (Date)GstDate.convertirFechaDiaMesAnio(form.getFecha());
			
			Hashtable numAnioExp= expAdm.getNewNumAnioExpediente(hash,GstDate.getYear(fechaActual));
			Integer numExp = (Integer)numAnioExp.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE);
			
			numExpAGuardar = numExp;
			anioExpAGuardar = Integer.valueOf(GstDate.getYear(fechaActual));
			
			expBean.setNumeroExpediente(numExpAGuardar);
			expBean.setAnioExpediente(anioExpAGuardar);	
			
			form.setAnioExpediente(anioExpAGuardar.toString());
			form.setNumExpediente(numExpAGuardar.toString());
			
			//Pasar el nume y el año
			request.setAttribute("numeroExpediente", numExp.toString());
			request.setAttribute("anioExpediente", anioExpAGuardar.toString());			
			
			request.getSession().setAttribute("numeroExpedienteSession", numExp.toString());
			request.getSession().setAttribute("anioExpedienteSession", anioExpAGuardar.toString());
																			
			String collectionTitle = form.getTipoExpediente() + " " + numExpAGuardar + "/" + anioExpAGuardar;
			
			/* Sólo se intentará la Conexion al DocuShare si el parámetro general para la institucion=1*/	
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(user);
			String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
			if (valor!=null && valor.equals("1")){
			  expBean.setIdentificadorDS(obtenerIdentificadorDS(user, collectionTitle));
			}
			
			//Comprobamos que coincide con los propuestos al presionar en 'nuevo', si no, informamos de los nuevos valores
			if (!form.getNumExpediente().equals(String.valueOf(numExpAGuardar)) && !form.getAnioExpediente().equals(String.valueOf(anioExpAGuardar))){
				request.setAttribute("sufijo",numExpAGuardar+"/"+anioExpAGuardar);
			}
			
			//Iniciamos la transacción
			tx = user.getTransaction();
			tx.begin();   
			
			
			
			//Ahora procedemos a insertarlo
			if (expAdm.insert(expBean)){
				
				if (!form.getEstado().equals("")){
					Hashtable hEstado = new Hashtable();
					hEstado.put(ExpEstadosBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
					hEstado.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
					hEstado.put(ExpEstadosBean.C_IDFASE,form.getFase());
					hEstado.put(ExpEstadosBean.C_IDESTADO,form.getEstado());
					ExpEstadosAdm estAdm = new ExpEstadosAdm(this.getUserBean(request));
					Vector vEstado = estAdm.select(hEstado);
					ExpEstadosBean estBean = (ExpEstadosBean)vEstado.elementAt(0);
					
					
					//Si al nuevo expediente se le asigna un estado, se generan las alertas/logs correspondientes
					try{
						expAdm.cambioEstado(expBean,null,estBean,false);
					}catch (Exception e) {
						throw new ClsExceptions(e,"Error al cambiar el estado");
					}
					
					
					Hashtable nuevo = expAdm.beanToHashTable(expBean); 
					if (expAdm.updateDirect(nuevo,null,null)){
						//Averiguamos si el estado tiene ESEJECUCIONSANCION='S'
						if (estBean.getEjecucionSancion().equals("S")){
							request.setAttribute("nuevo","true");
							Hashtable hEjSancion = new Hashtable();
							hEjSancion.put("idInstitucion",idInstitucion);
							hEjSancion.put("idPersona",form.getIdPersona());
							request.getSession().setAttribute("ejecucionSancion",hEjSancion);
							tx.commit();
							return "ejecucionSancion";
						}
					}
					
					// RGG obtengo los campos configurados para las pestañas configuradas del tipo de expediente
					// Y Los inseerto en blanco.
					ExpCampoConfAdm admCampoConf = new ExpCampoConfAdm(this.getUserBean(request));
					ExpCamposValorAdm admCampoVal = new ExpCamposValorAdm(this.getUserBean(request));
					
					Vector campos1 = admCampoConf.obtenerCamposConfigurados (idInstitucion_TipoExpediente,idTipoExpediente,"1");
					for (int g=0;g<campos1.size();g++) {
					    ExpCampoConfBean beanCampoConf = (ExpCampoConfBean) campos1.get(g);
					    ExpCamposValorBean beanCampoVal = new ExpCamposValorBean();
					    beanCampoVal.setIdInstitucion(new Integer(user.getLocation()));
					    beanCampoVal.setIdInstitucion_TipoExpediente(beanCampoConf.getIdInstitucion());
					    beanCampoVal.setIdTipoExpediente(beanCampoConf.getIdTipoExpediente());
					    beanCampoVal.setIdPestanaConf(beanCampoConf.getIdPestanaConf());
					    beanCampoVal.setIdCampo(beanCampoConf.getIdCampo());
					    beanCampoVal.setIdCampoConf(beanCampoConf.getIdCampoConf());
					    beanCampoVal.setNumeroExpediente(numExpAGuardar);
					    beanCampoVal.setAnioExpediente(anioExpAGuardar);
					    beanCampoVal.setValor("");
					    if (!admCampoVal.insert(beanCampoVal)) {
					        throw new ClsExceptions("Error al crear campos valor 1. "+admCampoVal.getError());
					    }
					}
					Vector campos2 = admCampoConf.obtenerCamposConfigurados (idInstitucion_TipoExpediente,idTipoExpediente,"2");
					for (int g=0;g<campos2.size();g++) {
					    ExpCampoConfBean beanCampoConf = (ExpCampoConfBean) campos2.get(g);
					    ExpCamposValorBean beanCampoVal = new ExpCamposValorBean();
					    beanCampoVal.setIdInstitucion(new Integer(user.getLocation()));
					    beanCampoVal.setIdInstitucion_TipoExpediente(beanCampoConf.getIdInstitucion());
					    beanCampoVal.setIdTipoExpediente(beanCampoConf.getIdTipoExpediente());
					    beanCampoVal.setIdPestanaConf(beanCampoConf.getIdPestanaConf());
					    beanCampoVal.setIdCampo(beanCampoConf.getIdCampo());
					    beanCampoVal.setIdCampoConf(beanCampoConf.getIdCampoConf());
					    beanCampoVal.setNumeroExpediente(numExpAGuardar);
					    beanCampoVal.setAnioExpediente(anioExpAGuardar);
					    beanCampoVal.setValor("");
					    if (!admCampoVal.insert(beanCampoVal)) {
					        throw new ClsExceptions("Error al crear campos valor 2. "+admCampoVal.getError());
					    }
					}
					 
					
				}
				tx.commit();
			} else {
				throw new ClsExceptions(expAdm.getError());
			}
			
		}catch (ClsExceptions cl) {
			if(cl.getMsg().contains("PK_EXP_EXPEDIENTE")){
				throw new SIGAException("No se ha podido guardar el expediente porque otro usuario estaba guardando otro del mismo tipo al mismo tiempo. Por favor, vuelva a darle al botón Guardar");
			}else{
				throwExcp("messages.general.error",new String[] {"modulo.expediente"},cl,tx);
			}
					
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx); 
		}
		return exitoRefresco("messages.inserted.success",request);
	}
	


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		try{
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			
			Hashtable datosExp = new Hashtable();
			
			if(request.getSession().getAttribute("DATABACKUP") instanceof Hashtable){
				datosExp = (Hashtable)request.getSession().getAttribute("DATABACKUP");	  

			}else if(request.getSession().getAttribute("DATABACKUP") instanceof HashMap<?, ?>){
				HashMap hashMap = (HashMap)request.getSession().getAttribute("DATABACKUP");	  
				datosExp = (Hashtable)hashMap.get("datosGenerales");
			}
			
			Hashtable htParametros=new Hashtable();
		    htParametros.put("idInstitucion",datosExp.get(ExpExpedienteBean.C_IDINSTITUCION));
		    htParametros.put("idInstitucion_TipoExpediente",datosExp.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
		    htParametros.put("idTipoExpediente",datosExp.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
		    htParametros.put("numeroExpediente",form.getNumExpediente());
		    htParametros.put("anioExpediente",form.getAnioExpediente());
		    htParametros.put("nombreTipoExpediente",form.getTipoExpediente());
		    htParametros.put("tipoExpDisciplinario",form.getTipoExpDisciplinario());
		    htParametros.put("anioExpDisciplinario",form.getAnioExpDisciplinario());
		    htParametros.put("numExpDisciplinario",form.getNumExpDisciplinario());
		    
			
		    htParametros.put("editable", "1");
		    htParametros.put("accion","edicion");
		    htParametros.put("soloSeguimiento", "false");
		    
		    request.setAttribute("expediente", htParametros);
		    
		    request.setAttribute("nuevo", "false");	
		    
		    //Recuperamos las pestanhas ocultas para no mostrarlas
		    ExpCampoTipoExpedienteAdm campoAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
		    String[] pestanasOcultas = campoAdm.obtenerPestanasOcultas((String)datosExp.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE),(String)datosExp.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
		    request.setAttribute("pestanasOcultas",pestanasOcultas);
		    
		    // Metemos los datos no editables del expediente en Backup.
		    //Los datos particulares se anhadirán a la HashMap en cada caso.
		    HashMap datosExpediente = new HashMap();
		    Hashtable datosGenerales = new Hashtable();
		    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,datosExp.get(ExpExpedienteBean.C_IDINSTITUCION));
		    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,datosExp.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
		    datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,datosExp.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
		    datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,form.getNumExpediente());
		    datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,form.getAnioExpediente());
			datosExpediente.put("datosGenerales",datosGenerales);
			request.getSession().setAttribute("DATABACKUP",datosExpediente);
			
			// RGG 	
			request.setAttribute("idTipoExpediente",datosExp.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
			request.setAttribute("idInstitucionTipoExpediente",datosExp.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));

			
		}catch (Exception e) {
	    	throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
	    }
		
		return "editar";
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirConParametros(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirConParametros(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try{
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			HttpSession ses = request.getSession(); 
			//UsrBean user = (UsrBean)ses.getAttribute("USRBEAN");
			Hashtable hashExp = new Hashtable();
			ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));
			ExpExpedienteBean expBean = new ExpExpedienteBean(); //(ExpExpedienteBean)vExp.elementAt(0);
						
			if (ses.getAttribute("DATABACKUP").getClass().getName().equals("java.util.HashMap")){
				HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
			    hashExp = (Hashtable)datosExpediente.get("datosGenerales");
			    Vector vExp = expAdm.select(hashExp);
				expBean = (ExpExpedienteBean)vExp.elementAt(0);
			}else{
				hashExp = (Hashtable)ses.getAttribute("DATABACKUP");
				expBean.setIdInstitucion(Integer.valueOf((String)hashExp.get(ExpExpedienteBean.C_IDINSTITUCION)));
				expBean.setIdInstitucion_tipoExpediente(Integer.valueOf((String)hashExp.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)));
				expBean.setIdTipoExpediente(Integer.valueOf((String)hashExp.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)));
			}
			
			
			//Recojo el valor de la fecha inicial del formulario, de la fase y del estado
			expBean.setIdClasificacion(Integer.valueOf(form.getClasificacion()));
			expBean.setIdFase(Integer.valueOf(form.getFase()));
			expBean.setIdEstado(Integer.valueOf(form.getEstado()));
			expBean.setFechaInicialEstado(GstDate.getApplicationFormatDate("",form.getFechaInicial()));
			
			ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm (this.getUserBean(request));
						
			//calculo la fecha final con este método, y me la devuelve en el bean
			try{
				if(plazoAdm.establecerFechaFinal(expBean)){
					request.setAttribute("fechaFinal",GstDate.getFormatedDateShort("",expBean.getFechaFinalEstado()));
				}else{
					request.setAttribute("fechaFinal","");
				}
			}catch(Exception e){
				throw new ClsExceptions(e,"Error en el cálculo del plazo"); 
			}
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		
		return "plazo";
		
	}
		
	/**
	 * Crea una nueva collección en DocuShare y devuelve el identificador
	 * @param usrBean
	 * @param collectionTitle
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private String obtenerIdentificadorDS(UsrBean usrBean, String collectionTitle) throws SIGAException, ClsExceptions, DSException {
		DocuShareHelper docuShareHelper = new DocuShareHelper(usrBean);
		return docuShareHelper.createCollection(collectionTitle);		
	}
	
	/**
	 * Método que realiza la sincronia a la hora de insertar un nuevo registro y retorna el num.expediente
	 * @param anioExpediente
	 * @param hash
	 * @param expAdm
	 * @return Integer	 
	 */
	public Integer devuelveNumExpediente(String anioExpediente,Hashtable hash,ExpExpedienteAdm expAdm)
	{
		synchronized (ultimoAniosNumExpediente) 
		{										
			int numContador;
			Integer numExp =null;
			
			// comprobando si el contador ya ha sido actualizado en este metodo (por otro hilo)		  
			//Si existe el año se coge su Nº eXPE Y SE LE SUMA 1 y se guarda el nuevo Nº Expe
			if(ultimoAniosNumExpediente.get(anioExpediente)!=null)
			{						  
			  	Integer numExpe= ultimoAniosNumExpediente.get(anioExpediente);
			  				  					 				 			  					  							    								
				//valores que se van a guardar						  	
			  	numContador = numExpe.intValue() +1;
			  	
			  //Traemos el numºExp de la BBDD para compararlo con el que esta guarda en la hash, en caso de que en 
			  	//bbdd el nº Exp sea mayor que en el hash entonces se sustituye el valor de la hash
			  	try
				 {
			  		Hashtable numAnioExp = expAdm.getNewNumAnioExpediente(hash,anioExpediente);
			  		numExp = (Integer)numAnioExp.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE);
				 }catch(ClsExceptions ex) { }
				 
				 if(numContador<numExp.intValue())
					 numContador = numExp.intValue();
			  				  	
			  	 numExp = Integer.valueOf(numContador);				  																	
			 }
			 else //si no existe se crea el registro en el hash y se coge el nºExpe de la bbdd
			 {
				 try
				 {
					 Hashtable numAnioExp = expAdm.getNewNumAnioExpediente(hash,anioExpediente);
					 numExp = (Integer)numAnioExp.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE);
				 }catch(ClsExceptions ex) { }			    			    			    														
			}					  
		 //Se crea el registro en el hash
		 ultimoAniosNumExpediente.put(anioExpediente, numExp);
		 return numExp;
		}//fin del synchronized
	}//fin del método
	
	
}
