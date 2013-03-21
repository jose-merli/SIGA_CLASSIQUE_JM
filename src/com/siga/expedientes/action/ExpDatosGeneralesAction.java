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
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.SIGAServiceException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoConfAdm;
import com.siga.beans.ExpCampoConfBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpCamposValorAdm;
import com.siga.beans.ExpCamposValorBean;
import com.siga.beans.ExpClasificacionesAdm;
import com.siga.beans.ExpClasificacionesBean;
import com.siga.beans.ExpDenunciadoAdm;
import com.siga.beans.ExpDenunciadoBean;
import com.siga.beans.ExpDenuncianteAdm;
import com.siga.beans.ExpDenuncianteBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpPestanaConfAdm;
import com.siga.beans.ExpPlazoEstadoClasificacionAdm;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.expedientes.form.ExpDatosGeneralesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

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
	static public final String C_IDPERSONADENUNCIANTE = "IDPERSONADENUNCIANTE";
	static public final String C_NCOLDENUNCIANTE = "NCOLDENUNCIANTE";
					
	public static Hashtable<String,Integer> ultimoAniosNumExpediente = new Hashtable<String, Integer>();
	
	protected String abrir(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles
		UsrBean usr = this.getUserBean(request);
		ExpCampoTipoExpedienteAdm adm = new ExpCampoTipoExpedienteAdm(usr);
		
		ExpTipoExpedienteAdm tipoAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
		
		Vector v;
		Hashtable h;
		
		String tiempoCaducidad;
		
		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/yyyy");

		try {
			// Buscando idTipoExpediente
			String idTipoExpediente = "";
			String accion = (String) request.getParameter("accion");
			if (request.getParameter("idTipoExpediente") == null || request.getParameter("idTipoExpediente").toString().trim().equals("")) {
				idTipoExpediente = nuevo(mapping, formulario, request, response);
				accion = "nuevo";
			} else {
				idTipoExpediente = (String) request.getParameter("idTipoExpediente");
			}

			// Aplicar visibilidad a campos generales
			h = new Hashtable();
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);

			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
			
			if(!accion.equals("nuevo"))
			{
				//Se buscan los campos configurados en exp_campoconf para extraer sus nombres de campo
				ExpCampoConfAdm expCamConfAdm = new ExpCampoConfAdm(usr);			
				Vector vecExpCamConfAdm= expCamConfAdm.obtenerCamposPestanaGeneral(this.getIDInstitucion(request).toString(),idTipoExpediente);
			
				if(vecExpCamConfAdm!= null && vecExpCamConfAdm.size()>0)
				{
					//Declaración objetos
					Vector nombres = new Vector ();
					Vector datosCamposPestanas= new Vector ();					
					Vector nombresLongitud = new Vector ();
					Vector datosCamposPestanasLongitud= new Vector ();
					
					// Se busca el nombre de la etiqueta que tendra el recuadro en la jsp de Datos Generales
					ExpPestanaConfAdm expPestanaConfAdm = new ExpPestanaConfAdm(usr);
					String nombreCampo = expPestanaConfAdm.obtenerNombrePetanaGeneral(this.getIDInstitucion(request).toString(), idTipoExpediente);
			
					//Se buscan los valores configurados en exp_camposvalor para extraer sus valores de campo					
					if(numExpediente==null || numExpediente.equals(""))
				           numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
				   
				    if(anioExpediente==null || anioExpediente.equals(""))
				           anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");
					ExpCamposValorAdm expCamposValorAdm = new ExpCamposValorAdm(usr);
					Vector vecExpCamposValorAdm = expCamposValorAdm.obtenerValorCamposPestanaGeneral(this.getIDInstitucion(request).toString(), idTipoExpediente, numExpediente,anioExpediente);
											
				
					//inicilizacion de los vectores
					for (int m =0; m<5;m++)
					{
						//Inicializamos el vector de datos a "" para que aquellos campos que no tengan valor
						//por lo menos tengan cadena vacia
						nombres.add(new ExpCampoConfBean());
						datosCamposPestanas.add("");
						nombresLongitud.add(new Integer(0));
						//El tamaño de la caja de texto en la jsp por defecto será de 15
						//si el campo valor esta vacio en BBDD
						datosCamposPestanasLongitud.add(new Integer(15));
					}
				
					//Se carga el el vector nombres los nombre de los campos que tiene la pestaña
					//Se carga en el vector nombresLongitud la longitud del nombre de cada campo para luego mostrar
					//de manera dinamica los campos y sus valores en la jsp
					for (int i =0; i<vecExpCamConfAdm.size();i++)
					{
						Hashtable auxHash = (Hashtable)vecExpCamConfAdm.get(i);
						ExpCampoConfBean bean = (ExpCampoConfBean)expCamConfAdm.hashTableToBean((Hashtable)vecExpCamConfAdm.get(i));
						Integer orden= new Integer(i);
						String longitud = (String)auxHash.get("NOMBRE");
						nombresLongitud.set((orden.intValue()),longitud.length());
						nombres.set((orden.intValue()),bean);
					}
					
					//Se carga en el vector datosCamposPestanas los valores q llevan asociados los campos de las pestañas
					//Se carga en el vector datosCamposPestanasLongitud la longitud del valor de cada campo para luego mostrar
					//de manera dinamica los campos y sus valores en la jsp
					for (int j =0; (j<vecExpCamposValorAdm.size());j++)
					{
						Hashtable auxHashCamposValor = (Hashtable)vecExpCamposValorAdm.get(j);
					
						boolean encontrado = false;
						for (int k =0; k<vecExpCamConfAdm.size() && encontrado == false;k++)
						{
							Hashtable auxHashCamposConfi = (Hashtable)vecExpCamConfAdm.get(k);
						
							String ordenCampo=(String)auxHashCamposConfi.get("IDCAMPOCONF");
							String ordenValor=(String)auxHashCamposValor.get("IDCAMPOCONF");
							
							if(ordenCampo.equals(ordenValor))
							{		
								//El orde en que se tienen que guardar los valores en los vectores datosCamposPestanasLongitud y datosCamposPestanas
								//viene dado por el indice del vector de los nombre de los campos a los que van asignados
								//en este caso la variable k
								Integer orden= new Integer(k);								
								datosCamposPestanasLongitud.set((orden.intValue()),((ExpCampoConfBean)nombres.get(orden)).getMaxLong());
								/*
								String longitud = (String)auxHashCamposValor.get("VALOR");
								if(longitud.length()!=0)
									datosCamposPestanasLongitud.set((orden.intValue()),longitud.length());
								else
									datosCamposPestanasLongitud.set((orden.intValue()),15);
									*/
								datosCamposPestanas.set((orden.intValue()),(String)auxHashCamposValor.get("VALOR"));
								encontrado = true; 
							}										
						}																						
					}
						
					//envío de los objetos al jsp datosGenerales.jsp
					request.setAttribute("nombres", nombres);
					request.setAttribute("datosCamposPestanas", datosCamposPestanas);
					request.setAttribute("nombreCampo", nombreCampo);								
					request.setAttribute("nombresLongitud", nombresLongitud);
					
					//Se llama al método muestraCamposDinamicos para implementar el código neceasrio para
					//mostrar de manera dinámica los campos de la etiqueta configurable.
					Hashtable mostrarCampos = muestraCamposDinamicos(nombresLongitud,datosCamposPestanasLongitud);
					datosCamposPestanasLongitud = (Vector)mostrarCampos.get("datosCamposPestanasLongitud");
					request.setAttribute("datosCamposPestanasLongitud", datosCamposPestanasLongitud);					
					request.setAttribute("saltoLinea", (Vector)mostrarCampos.get("saltoLinea"));
				}
				else
				{
					//envio de los objetos al jsp datosGenerales.jsp
					request.setAttribute("nombres", null);
					request.setAttribute("datosCamposPestanas", null);
					request.setAttribute("nombreCampo", null);
					request.setAttribute("datosCamposPestanasLongitud", null);
					request.setAttribute("nombresLongitud", null);
				}
			}//fin if de accion!=nuevo	
			
			tiempoCaducidad = tipoAdm.getImporteCaducidad(this.getIDInstitucion(request).toString() , idTipoExpediente).toString();
			request.setAttribute("tiempoCaducidad", tiempoCaducidad );
			ExpDatosGeneralesForm formExpediente = (ExpDatosGeneralesForm)formulario;			
			
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_MINUTA_INICIAL));
			v = adm.select(h);
			if (v != null && v.size() > 0) {
				request.setAttribute("mostarMinuta", ((ExpCampoTipoExpedienteBean) v.get(0)).getVisible());
			}

			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_MINUTA_FINAL));
			v = adm.select(h);
			if (v != null && v.size() > 0) {
				request.setAttribute("mostarMinutaFinal", ((ExpCampoTipoExpedienteBean) v.get(0)).getVisible());
			}
			
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DERECHOS_COLEGIALES));
			v = adm.select(h);
			if (v != null && v.size() > 0) {
				request.setAttribute("derechosColegiales", ((ExpCampoTipoExpedienteBean) v.get(0)).getVisible());
			}
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_SOLICITANTEEJG));
			v = adm.select(h);
			boolean mostrarSolicitanteEJG = false;
			if (v != null && v.size() > 0) {
				mostrarSolicitanteEJG = UtilidadesString.stringToBoolean(((ExpCampoTipoExpedienteBean) v.get(0)).getVisible());
				request.setAttribute("mostrarSolicitanteEJG", ((ExpCampoTipoExpedienteBean) v.get(0)).getVisible());
			}
			
			

			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE));
			v = adm.select(h);

			// Obtenemos el Titulo Impugnante o Denunciante
			String nombreAux = "pestana.auditoriaexp.interesado";
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean) v.get(0);
				request.setAttribute("mostrarDenunciante", b.getVisible());
				nombreAux = b.getNombre();
				if (nombreAux == null || nombreAux.equals(""))
					nombreAux = ExpCampoTipoExpedienteBean.DENUNCIANTE;
			}

			// Comprobamos si tiene visible la pestaña denunciante. Si no la tiene ponemos interesado
			// si esta visible la descripcion del campo dependera de lo que haya seleccionado
			// en la configuiracion del tipo de expediente
			String nombre = "pestana.auditoriaexp.interesado";
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean) v.get(0);
				if (b.getVisible() != null && b.getVisible().equals(ExpCampoTipoExpedienteBean.si)) {
					UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIADO));
					v = adm.select(h);
					if (v != null && v.size() > 0) {
						b = (ExpCampoTipoExpedienteBean) v.get(0);
						request.setAttribute("mostrarDenunciado", b.getVisible());
						nombre = b.getNombre();
						if (nombre == null || nombre.equals(""))
							nombre = ExpCampoTipoExpedienteBean.DENUNCIADO;
					}
				}
				
				request.setAttribute("tituloDenunciado", nombre);
				request.setAttribute("tituloDenunciante", nombreAux);
			}
			
			// Obtenemos el num expediente o num ejg
			UtilidadesHash.set(h, ExpCampoTipoExpedienteBean.C_IDCAMPO, new Integer(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_N_DISCIPLINARIO));
			v = adm.select(h);

			String nombreAux2 = "expedientes.auditoria.literal.nexpdisciplinario";
			if (v != null && v.size() > 0) {
				ExpCampoTipoExpedienteBean b = (ExpCampoTipoExpedienteBean) v.get(0);
				request.setAttribute("mostrarNumExpNumEjg", b.getVisible());
				nombreAux2 = b.getNombre();
				if (nombreAux2 == null || nombreAux2.equals(""))
					nombreAux2 = ExpCampoTipoExpedienteBean.NUMEXPEDIENTE;
			}
			request.setAttribute("tituloExpDisciplinario", nombreAux2);			
			
			if (accion != null && accion.equals("nuevo")) {
				return abrirNuevo(mapping, formulario, request, response);
			} else {
				return abrirExistente(mapping, formulario, request, response);
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.expediente" }, e, null);
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
			form.setProcedimiento(request.getParameter("procedimiento"));
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
			HttpSession session = request.getSession();
			UsrBean user = (UsrBean)session.getAttribute("USRBEAN");
			session.removeAttribute("DATABACKUP");
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
			if (session.getAttribute("volverAuditoriaExpedientes")==null){
				session.setAttribute("volverAuditoriaExpedientes","N"); // busqueda normal	
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
				form.setIdPersonaDenunciado(idpersona);
				form.setNumColegiado((String)request.getParameter("numColDesignado"));
				//form.setIdPersona(numCol);
				CenClienteBean cliente = null;
				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
				Vector v = clienteAdm.getDatosPersonales(new Long(idpersona),new Integer(idInstitucion));
				if (v!=null && v.size()>0) {
					Hashtable registro =  (Hashtable)v.get(0);
					if (registro.get("NOMBRE")!=null) form.setNombreDenunciado((String) registro.get("NOMBRE") ); 
					if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) form.setPrimerApellidoDenunciado((String) registro.get("APELLIDOS1")); 
					if (registro.get("APELLIDOS2")!=null) form.setSegundoApellidoDenunciado((String) registro.get("APELLIDOS2")); 
					if (registro.get("NIFCIF")!=null) form.setNifDenunciado((String) registro.get("NIFCIF")); 
					
				}
				
			}
			
			request.setAttribute("accion","nuevo");
			session.setAttribute("DATABACKUP",hash);
			
			//Se pasa la fecha del sistema al campo Fecha de Apertura
			request.setAttribute("fechaApertura",(String)GstDate.getHoyJsp());
			
			//Se inicializa los campos numeroExpedienteSession y anioExpedienteSession a ""
			session.setAttribute("numeroExpedienteSession", "");
			session.setAttribute("anioExpedienteSession", "");
			session.setAttribute("institucionSession","");
			session.setAttribute("institucionExpeSession","");
			session.setAttribute("nombreExpedienteSession", "");
			session.setAttribute("tipoExpedienteSession", "");

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
	
	protected void busquedaRelacionExpediente(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		String relacionExpediente=null;
		try{
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
	        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
	        
	        String institucion = userBean.getLocation();        
	        
	        String where = " WHERE ";
			String idTipoExpediente = request.getParameter("idTipoExpediente");
	        where += ExpCampoTipoExpedienteBean.C_IDINSTITUCION + " = " + institucion+" AND "+ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE + " = " + idTipoExpediente;
	        
	        //Recupero los campos de un tipo de expediente para una institución
	        Vector camposExp = campoTipoExpedienteAdm.select(where);
	        //Recupero el tipo de expediente para editar el nombre
	        Vector tipoExp = tipoExpedienteAdm.select(where);
	        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
	        
	        where = " WHERE ";
			where += ExpCampoTipoExpedienteBean.C_IDINSTITUCION + " = " + institucion+" AND "+ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE + " = " + beantipoexp.getRelacionExpediente();
	        
	        //Recupero los campos de un tipo de expediente para una institución
	        camposExp = campoTipoExpedienteAdm.select(where);
	        //Recupero el tipo de expediente para editar el nombre
	        tipoExp = tipoExpedienteAdm.select(where);
	        beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
	        if(beantipoexp!=null && beantipoexp.getIdTipoExpediente()!=null){
		        form.setNombreRelacionExpediente(beantipoexp.getNombre());
		        form.setRelacionExpediente(beantipoexp.getIdTipoExpediente().toString());
	        }
			
		}catch(Exception e){
			
		}

		
	}
	protected String abrirExistente(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		try{
			ExpDatosGeneralesForm form = (ExpDatosGeneralesForm)formulario;
			ExpTipoExpedienteAdm tipoAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
			HttpSession ses = request.getSession();		
			UsrBean userBean = this.getUserBean(request);
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			if(idInstitucion==null)
				idInstitucion=userBean.getLocation();
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
			String idTipoExpedienteNew = request.getParameter("idTipoExpedienteNew");
			String nombreTipoExpedienteNew = request.getParameter("nombreTipoExpedienteNew");
			String copia= request.getParameter("copia");

			
			//Se pregunta si numExpediente y anioExpediente vienes como parámetros en la request, si no vienen
			//se recuperan de la sesión
			if((numExpediente==null ||numExpediente.equals("")) && (anioExpediente==null ||anioExpediente.equals("")))
			{
				numExpediente = (String)request.getSession().getAttribute("numeroExpedienteSession");
				anioExpediente = (String)request.getSession().getAttribute("anioExpedienteSession");
			}
			 busquedaRelacionExpediente( mapping, formulario,  request,	 response);
			String soloSeguimiento = request.getParameter("soloSeguimiento");
			String editable = request.getParameter("editable");
			boolean edit = (editable.equals("1")&&soloSeguimiento.equals("false"))?true:false;
			
			//obtenemos la hash con los campos a mostrar
			Hashtable camposVisibles =  tipoAdm.getCamposVisibles(idInstitucion_TipoExpediente,idTipoExpediente);
			if(copia!=null && copia.trim().equalsIgnoreCase("s")){
				 camposVisibles =  tipoAdm.getCamposVisibles(idInstitucion_TipoExpediente,idTipoExpedienteNew);
			}else{
				 camposVisibles =  tipoAdm.getCamposVisibles(idInstitucion_TipoExpediente,idTipoExpediente);
			}
			
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
				hashExp.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				hashExp.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
				hashExp.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				hashExp.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				
				
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
			    if(copia!=null && copia.trim().equalsIgnoreCase("s")){
				    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				    datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpedienteNew);
				    datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,"");
				    datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,"");
			    }else{
					datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
				    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
				    datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
				    datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
				    datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);   
				}
				datosExpediente.put("datosGenerales",datosGenerales);
				request.getSession().setAttribute("DATABACKUP",datosExpediente);
				
			}
	
	        
	        //PRIMERA SELECT: EXP_EXPEDIENTE--------------------------	        	        	        
			
			String where = " WHERE ";
			
			//join de las tablas EXPEDIENTE E, TIPOEXPEDIENTE T, PERSONA P, COLEGIADO C, INSTITUCION I
			where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDINSTITUCION+"(+)";
			where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = T."+ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE+"(+)";
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = I."+CenInstitucionBean.C_IDINSTITUCION+"(+)";
			//where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = P."+CenPersonaBean.C_IDPERSONA+"(+)";
			//where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = C."+CenColegiadoBean.C_IDINSTITUCION+"(+)";
			//where += " AND E."+ExpExpedienteBean.C_IDPERSONA+" = C."+CenColegiadoBean.C_IDPERSONA+"(+)";
			
			where += "AND E."+ExpExpedienteBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
			where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
			where += "E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";
			where += "E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
			where += "E."+ExpExpedienteBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "' ";
				
			datosExp = expAdm.selectDatosGenerales(where);
			Row fila = (Row)datosExp.elementAt(0);				
		
			//Hacemos los sets del formulario

			form.setTipoExpediente(fila.getString(C_NOMBRETIPOEXPEDIENTE));
			form.setNumExpediente(fila.getString(ExpExpedienteBean.C_NUMEROEXPEDIENTE));
			form.setAnioExpediente(fila.getString(ExpExpedienteBean.C_ANIOEXPEDIENTE));
			if(fila.getString(ExpTipoExpedienteBean.C_RELACIONEJG)!=null&&!fila.getString(ExpTipoExpedienteBean.C_RELACIONEJG).equals("")&&
					!fila.getString(ExpTipoExpedienteBean.C_RELACIONEJG).equals("0")){
				form.setNumExpDisciplinario(fila.getString(ExpExpedienteBean.C_NUMEROEJG));
				form.setAnioExpDisciplinario(fila.getString(ExpExpedienteBean.C_ANIOEJG));
				form.setTipoExpDisciplinario(fila.getString(ExpExpedienteBean.C_IDTIPOEJG));

				ScsEJGAdm admEjg = new ScsEJGAdm(userBean);
				Hashtable haste = admEjg.getDatosEjg(userBean.getLocation(), form.getAnioExpDisciplinario(), form.getNumExpDisciplinario(), form.getTipoExpDisciplinario());
					String SUFIJO = (String) haste.get("SUFIJO");
					String CODIGO = (String) haste.get("CODIGO");
					String codigoEjg = null;
					if (SUFIJO != null && !SUFIJO.equals("")) {
						codigoEjg = CODIGO + "-" + SUFIJO;
						
					} else {
						codigoEjg = CODIGO;
					}
					form.setNumExpDisciplinarioCalc(codigoEjg);
					form.setSolicitanteEjgNif(UtilidadesHash.getString(haste, "NIF"));
					form.setSolicitanteEjgNombre(UtilidadesHash.getString(haste, "NOMBRE"));
					form.setSolicitanteEjgApellido1(UtilidadesHash.getString(haste, "APELLIDO1"));
					form.setSolicitanteEjgApellido2(UtilidadesHash.getString(haste, "APELLIDO2"));
					
//					request.setAttribute("codigoEjg", codigoEjg);
				
				
				
				
			}else{
				form.setNumExpDisciplinario(fila.getString(ExpExpedienteBean.C_NUMEXPDISCIPLINARIO));
				form.setAnioExpDisciplinario(fila.getString(ExpExpedienteBean.C_ANIOEXPDISCIPLINARIO));
			}
			form.setFecha(GstDate.getFormatedDateShort("",fila.getString(ExpExpedienteBean.C_FECHA)));
			form.setInstitucion(fila.getString(C_NOMBREINSTITUCION));
			form.setAsunto(fila.getString(ExpExpedienteBean.C_ASUNTO));
			//BEGIN BNS
			// Cargamos el denunciado de la tabla de denunciante con ID 1
			if (this.getIDInstitucion(request) != null && idTipoExpediente != null && request.getParameter("idInstitucion_TipoExpediente") != null &&
					numExpediente != null && anioExpediente != null){
				//if (request.getAttribute("mostrarDenunciado") != null && "S".equalsIgnoreCase((String)request.getAttribute("mostrarDenunciado"))){
					// Obtenemos los datos del denunciado
					ExpDenunciadoAdm DenunciadoAdm = new ExpDenunciadoAdm (this.getUserBean(request));				
					Hashtable hash = new Hashtable();
					hash.put(ExpDenunciadoBean.C_IDINSTITUCION, this.getIDInstitucion(request));
					hash.put(ExpDenunciadoBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
					hash.put(ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE, request.getParameter("idInstitucion_TipoExpediente"));
					hash.put(ExpDenunciadoBean.C_NUMEROEXPEDIENTE, numExpediente);
					hash.put(ExpDenunciadoBean.C_ANIOEXPEDIENTE, anioExpediente);
					hash.put(ExpDenunciadoBean.C_IDDENUNCIADO, ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL);
					
			        Vector datos = DenunciadoAdm.select(hash);
			        if (datos != null && datos.size() > 0){
			        	ExpDenunciadoBean denunciado = (ExpDenunciadoBean)datos.get(0);
			        	form.setIdPersonaDenunciado(denunciado.getIdPersona().toString());
			        	if (denunciado.getIdDireccion() != null)
			        		form.setIdDireccionDenunciado(denunciado.getIdDireccion().toString());
			        	String idInstitucionOrigen = this.getIDInstitucion(request).toString();		        	
			        	if (denunciado.getIdInstitucionOrigen() != null)
			        		idInstitucionOrigen = denunciado.getIdInstitucionOrigen().toString();
			        	
			        	form.setIdInstitucionOrigenDenunciado(idInstitucionOrigen);
			        	
			        	// Obtenemos los datos personales
			        	CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
			        	Hashtable hashIdPers = new Hashtable();		
						hashIdPers.put(CenPersonaBean.C_IDPERSONA,denunciado.getIdPersona());
						Vector vPersona = personaAdm.selectByPK(hashIdPers);
						CenPersonaBean personaBean = (CenPersonaBean) vPersona.elementAt(0);					
						form.setNombreDenunciado(personaBean.getNombre());
						form.setPrimerApellidoDenunciado(personaBean.getApellido1());
						form.setSegundoApellidoDenunciado(personaBean.getApellido2());
						form.setNifDenunciado(personaBean.getNIFCIF());
						
						// Obtenemos datos del colegiado de la institución del expediente
						form.setnColDenunciado("");
				        Hashtable hashCol = new Hashtable();
				        CenColegiadoAdm colAdm = new CenColegiadoAdm (this.getUserBean(request));
				        hashCol.put(CenColegiadoBean.C_IDINSTITUCION,this.getIDInstitucion(request).toString());
				        hashCol.put(CenColegiadoBean.C_IDPERSONA,denunciado.getIdPersona());
				        Vector datosCol = colAdm.select(hashCol);			        
				        if(datosCol!=null && datosCol.size()>0){
				        	CenColegiadoBean cBean = (CenColegiadoBean)datosCol.elementAt(0);
				        	form.setnColDenunciado(cBean.getNColegiado());
				        }
				        else if (!idInstitucionOrigen.equals(this.getIDInstitucion(request).toString())){
				        	// Si no existe, buscamos en la institución origen
				        	hashCol.put(CenColegiadoBean.C_IDINSTITUCION,idInstitucionOrigen);
					        hashCol.put(CenColegiadoBean.C_IDPERSONA,denunciado.getIdPersona());
					        datosCol = colAdm.select(hashCol);			        
					        if(datosCol!=null && datosCol.size()>0){
					        	CenColegiadoBean cBean = (CenColegiadoBean)datosCol.elementAt(0);
					        	form.setnColDenunciado(cBean.getNColegiado());
					        }
				        }			        	
						
			        }
				//}
				//if (request.getAttribute("mostrarDenunciante") != null &&  ((String)request.getAttribute("mostrarDenunciante")).equalsIgnoreCase("S")){
					// Obtenemos los datos del denunciante
					ExpDenuncianteAdm DenuncianteAdm = new ExpDenuncianteAdm (this.getUserBean(request));				
					hash = new Hashtable();
					hash.put(ExpDenuncianteBean.C_IDINSTITUCION, this.getIDInstitucion(request));
					hash.put(ExpDenuncianteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
					hash.put(ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, request.getParameter("idInstitucion_TipoExpediente"));
					hash.put(ExpDenuncianteBean.C_NUMEROEXPEDIENTE, numExpediente);
					hash.put(ExpDenuncianteBean.C_ANIOEXPEDIENTE, anioExpediente);
					hash.put(ExpDenuncianteBean.C_IDDENUNCIANTE, ExpDenuncianteBean.ID_DENUNCIANTE_PRINCIPAL);
					
			        datos = DenuncianteAdm.select(hash);
			        if (datos != null && datos.size() > 0){
			        	ExpDenuncianteBean denunciante = (ExpDenuncianteBean)datos.get(0);
			        	form.setIdPersonaDenunciante(denunciante.getIdPersona().toString());
			        	if (denunciante.getIdDireccion() != null)
			        		form.setIdDireccionDenunciante(denunciante.getIdDireccion().toString());
			        	String idInstitucionOrigen = this.getIDInstitucion(request).toString();
			        	if (denunciante.getIdInstitucionOrigen() != null)
			        		idInstitucionOrigen = denunciante.getIdInstitucionOrigen().toString();
			        	form.setIdInstitucionOrigenDenunciado(idInstitucionOrigen);
			        	
			        	// Obtenemos los datos personales
			        	CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
			        	Hashtable hashIdPers = new Hashtable();		
						hashIdPers.put(CenPersonaBean.C_IDPERSONA,denunciante.getIdPersona());
						Vector vPersona = personaAdm.selectByPK(hashIdPers);
						CenPersonaBean personaBean = (CenPersonaBean) vPersona.elementAt(0);					
						form.setNombreDenunciante(personaBean.getNombre());
						form.setPrimerApellidoDenunciante(personaBean.getApellido1());
						form.setSegundoApellidoDenunciante(personaBean.getApellido2());
						form.setNifDenunciante(personaBean.getNIFCIF());
						
						// Obtenemos datos del colegiado en la institución del expediente
						form.setnColDenunciante("");
				        Hashtable hashCol = new Hashtable();
				        CenColegiadoAdm colAdm = new CenColegiadoAdm (this.getUserBean(request));
				        hashCol.put(CenColegiadoBean.C_IDINSTITUCION,this.getIDInstitucion(request).toString());
				        hashCol.put(CenColegiadoBean.C_IDPERSONA,denunciante.getIdPersona());
				        Vector datosCol = colAdm.select(hashCol);			        
				        if(datosCol!=null && datosCol.size()>0){
				        	CenColegiadoBean cBean = (CenColegiadoBean)datosCol.elementAt(0);
				        	form.setnColDenunciante(cBean.getNColegiado());
				        }
				        else if (!idInstitucionOrigen.equals(this.getIDInstitucion(request).toString())){
				        	// Si no existe, buscamos en la institución origen
				        	hashCol.put(CenColegiadoBean.C_IDINSTITUCION,idInstitucionOrigen);
					        hashCol.put(CenColegiadoBean.C_IDPERSONA,denunciante.getIdPersona());
					        datosCol = colAdm.select(hashCol);			        
					        if(datosCol!=null && datosCol.size()>0){
					        	CenColegiadoBean cBean = (CenColegiadoBean)datosCol.elementAt(0);
					        	form.setnColDenunciante(cBean.getNColegiado());
					        }
				        }			        	
						
			        }
				}
			//}
			
			/*
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
			if (fila.getString(C_IDPERSONADENUNCIANTE).equalsIgnoreCase("null")) {
				form.setIdPersonaDenunciante("");
			} else {
				form.setIdPersonaDenunciante(fila.getString(C_IDPERSONADENUNCIANTE));	
			}	
			if (fila.getString(C_NCOLDENUNCIANTE).equalsIgnoreCase("null")) {
				form.setnColDenunciante("");
			} else {
				form.setnColDenunciante(fila.getString(C_NCOLDENUNCIANTE));	
			}	
			form.setNumColegiado(fila.getString(CenColegiadoBean.C_NCOLEGIADO));
			*/
			//END BNS
			if(copia!=null && copia.trim().equalsIgnoreCase("s")){
				idTipoExpediente=idTipoExpedienteNew;
			}
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
			if(copia!=null && copia.trim().equalsIgnoreCase("s")){
				request.setAttribute("idinst_idtipo_idfase",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+idTipoExpediente+","+fila.getString(ExpExpedienteBean.C_IDFASE));
				request.setAttribute("idinst_idtipo_idfase_idestado",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+idTipoExpediente+","+fila.getString(ExpExpedienteBean.C_IDFASE)+","+fila.getString(ExpExpedienteBean.C_IDESTADO));			request.setAttribute("idinst_idtipo_idfase_idestado",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDFASE)+","+fila.getString(ExpExpedienteBean.C_IDESTADO));
			}else{
				request.setAttribute("idinst_idtipo_idfase",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDFASE));
				request.setAttribute("idinst_idtipo_idfase_idestado",fila.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)+","+fila.getString(ExpExpedienteBean.C_IDFASE)+","+fila.getString(ExpExpedienteBean.C_IDESTADO));
			}
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
					if(form.getFase()!=null && !form.getFase().equals("")){
						ExpFasesAdm faseAdm = new ExpFasesAdm (this.getUserBean(request));		
						Hashtable hashFase = new Hashtable();
						
						hashFase.put(ExpFasesBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
						hashFase.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
						hashFase.put(ExpFasesBean.C_IDFASE,form.getFase());
						datosFase = faseAdm.select(hashFase);
						if(datosFase!=null && datosFase.size()>0){
							ExpFasesBean faseBean = (ExpFasesBean)datosFase.elementAt(0);
						
							form.setFaseSel(faseBean.getNombre());
						}else{
							form.setFaseSel("");
						}
					}else{
						form.setFaseSel("");
						
					}
					
					//TERCERA SELECT: EXP_ESTADOS
				if (bEstado){
					if(form.getEstado()!=null && !form.getEstado().equals("")){
						ExpEstadosAdm estadoAdm = new ExpEstadosAdm (this.getUserBean(request));		
						Hashtable hashEstado = new Hashtable();
						hashEstado.put(ExpEstadosBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
						hashEstado.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
						hashEstado.put(ExpEstadosBean.C_IDFASE,form.getFase());
						hashEstado.put(ExpEstadosBean.C_IDESTADO,form.getEstado());
						datosEstado = estadoAdm.select(hashEstado);
						if(datosEstado!=null && datosEstado.size()>0){
							ExpEstadosBean estadoBean = (ExpEstadosBean)datosEstado.elementAt(0);
							
							
							form.setEstadoSel(estadoBean.getNombre());
						}else{
							form.setEstadoSel("");
							
						}
					}else{
						form.setEstadoSel("");
						
					}
				}
				
				
				//CUARTA SELECT: EXP_CLASIFICACION
				if(form.getClasificacion()!=null && !form.getClasificacion().equals("")){
					ExpClasificacionesAdm clasifAdm = new ExpClasificacionesAdm (this.getUserBean(request));		
					Hashtable hashClasif = new Hashtable();
					
					hashClasif.put(ExpClasificacionesBean.C_IDINSTITUCION,idInstitucion_TipoExpediente);
					hashClasif.put(ExpClasificacionesBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
					hashClasif.put(ExpClasificacionesBean.C_IDCLASIFICACION,form.getClasificacion());
					
					datosClasif = clasifAdm.select(hashClasif);
					if(datosClasif!=null && datosClasif.size()>0){
						ExpClasificacionesBean clasifBean = (ExpClasificacionesBean)datosClasif.elementAt(0);
						
						form.setClasificacionSel(clasifBean.getNombre());
					}else{
						form.setClasificacionSel("");
						
					}
				}else{
					form.setClasificacionSel("");
				}
				
				request.setAttribute("accion","consulta");
				
			}else{
				request.setAttribute("accion","edicion");
			}
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
			if(copia!=null && copia.trim().equalsIgnoreCase("s")){
				idTipoExpediente=idTipoExpedienteNew;
				form.setIdTipoExpediente(idTipoExpedienteNew);
				form.setTipoExpediente(nombreTipoExpedienteNew);
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
	        expBean.setObservaciones(form.getObservaciones());
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
	        //expBean.setIdPersona(Long.valueOf(form.getIdPersona()));
	        //if(form.getIdDireccion()!=null && !form.getIdDireccion().trim().equals(""))
	        	//expBean.setIdDireccion(form.getIdDireccion());
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
	    	
	    		    		    	
	    	//Inicio Guarda Datos Para Pestaña
	    	
	    	ExpCampoConfAdm expCamConfAdm = new ExpCampoConfAdm(userBean);			
			Vector vecExpCamConfAdm= expCamConfAdm.obtenerCamposPestanaGeneral(this.getIDInstitucion(request).toString(),expBean.getIdTipoExpediente().toString());
			
			if(vecExpCamConfAdm!=null && vecExpCamConfAdm.size()>0)
			{
	    	String numExpediente = expBean.getNumeroExpediente().toString();
			ExpCamposValorAdm expCamposValorAdm = new ExpCamposValorAdm(userBean);
			Vector vecExpCamposValorAdm = expCamposValorAdm.obtenerValorCamposPestanaGeneral(this.getIDInstitucion(request).toString(), expBean.getIdTipoExpediente().toString(), numExpediente,expBean.getAnioExpediente().toString());
	    	
			//Iniciamos la transacción
	        UserTransaction tra = userBean.getTransaction();
		    try {
		    	tra.begin();     
		        		        
		        for (int contador=0;vecExpCamConfAdm!=null && contador<vecExpCamConfAdm.size(); contador++) 
		        {
		        	Hashtable auxHash = (Hashtable)vecExpCamConfAdm.get(contador);
		        	Hashtable auxHash2 = new Hashtable();
										
					boolean encontrado = false;
					if(vecExpCamposValorAdm ==null || vecExpCamposValorAdm.size()==0)
					{						
						auxHash2.put("IDINSTITUCION", expBean.getIdInstitucion());
						auxHash2.put("IDINSTITUCION_TIPOEXPEDIENTE", expBean.getIdInstitucion_tipoExpediente());
						auxHash2.put("IDTIPOEXPEDIENTE", expBean.getIdTipoExpediente());
						auxHash2.put("NUMEROEXPEDIENTE", expBean.getNumeroExpediente());
						auxHash2.put("ANIOEXPEDIENTE", expBean.getAnioExpediente());
						auxHash2.put("IDCAMPO", "14");
						auxHash2.put("IDPESTANACONF", "1");
						Integer ordenConf= new Integer((String)auxHash.get("IDCAMPOCONF"));
						Integer orden= new Integer(contador);
						String idCampoConf = ""+(ordenConf.intValue());		            
			            //Procesamos el campo		            		            			            		
			            String	valor = request.getParameter("campo"+(orden.intValue()+1)+"");
			            if(valor !=null)
			            {
			            	// lo guardamos
			            	this.guardarValor(expCamposValorAdm,auxHash2, idCampoConf, valor);
			            }																			
					}
					else
					{																																																																													
						for (int k =0; vecExpCamposValorAdm !=null && k<vecExpCamposValorAdm.size() && encontrado == false;k++)
						{
							Hashtable auxHash1 = (Hashtable)vecExpCamposValorAdm.get(k);
						
							String ordenCampo=(String)auxHash.get("IDCAMPOCONF");
							String ordenValor=(String)auxHash1.get("IDCAMPOCONF");
							
							if(ordenCampo.equals(ordenValor))
							{
								Integer orden= new Integer(contador);
								Integer ordenConf= new Integer((String)auxHash.get("IDCAMPOCONF"));
								String idCampoConf = ""+(ordenConf.intValue());		            
								//Procesamos el campo		            		            			            		
								String	valor = request.getParameter("campo"+(orden.intValue()+1)+"");
								if(valor !=null)
								{
									// lo guardamos
									this.guardarValor(expCamposValorAdm,(Hashtable)vecExpCamposValorAdm.get(k), idCampoConf, valor);
								}																					
								encontrado = true; 
							}																		
						}//fin del for
						
						if(encontrado==false)
						{
							encontrado = true;
							auxHash2.put("IDINSTITUCION", expBean.getIdInstitucion());
							auxHash2.put("IDINSTITUCION_TIPOEXPEDIENTE", expBean.getIdInstitucion_tipoExpediente());
							auxHash2.put("IDTIPOEXPEDIENTE", expBean.getIdTipoExpediente());
							auxHash2.put("NUMEROEXPEDIENTE", expBean.getNumeroExpediente());
							auxHash2.put("ANIOEXPEDIENTE", expBean.getAnioExpediente());
							auxHash2.put("IDCAMPO", "14");
							auxHash2.put("IDPESTANACONF", "1");
							Integer ordenConf= new Integer((String)auxHash.get("IDCAMPOCONF"));
							Integer orden= new Integer(contador);
							String idCampoConf = ""+(ordenConf.intValue());		            
				            //Procesamos el campo		            		            			            		
				            String	valor = request.getParameter("campo"+(orden.intValue()+1)+"");
				            if(valor !=null)
				            {
				            	// lo guardamos
				            	this.guardarValor(expCamposValorAdm,auxHash2, idCampoConf, valor);
				            }
						}// fin del if encontrado==false
					}// fin del else															        																						        			        	
		        }// fin del for		        
		        tra.commit();		               
		    } 
		    catch (Exception e) 
		    {        
		        throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tra);
		    }
			}// fin del if princpal
			
			
	    	    	
	    	//Fin Guarda Datos Para Pestaña
	    		    	
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
		            		//hEjSancion.put("idPersona",form.getIdPersonaDenunciado());
		            		//aalg: para poder modificar el estado a todos los denunciados
		            		hEjSancion.put("IdInstitucion_tipoExpediente", expBean.getIdInstitucion_tipoExpediente());
		            		hEjSancion.put("IdTipoExpediente", expBean.getIdTipoExpediente());
		            		hEjSancion.put("numeroExpediente", expBean.getNumeroExpediente());
		            		hEjSancion.put("anioExpediente", expBean.getAnioExpediente());
		            		
		            		request.getSession().setAttribute("ejecucionSancion",hEjSancion);
		            		forward = "ejecucionSancion"; 
		            		
		            	}
		            }
		        	
		        }
		        
		        //BEGIN BNS
		        // Guardamos el denunciante y denunciado principal (id=1) en sus tablas correspondientes
		        if (form.getIdPersonaDenunciado() != null && !"".equals(form.getIdPersonaDenunciado()) && 
		        	form.getIdDireccionDenunciado() != null && !"".equals(form.getIdDireccionDenunciado())){
		        	guardarDenunciadoPrincipal(form,request,expBean.getIdInstitucion_tipoExpediente(), expBean.getIdTipoExpediente());
		        }
		        if (form.getIdPersonaDenunciante() != null && !"".equals(form.getIdPersonaDenunciante()) && 
			        	form.getIdDireccionDenunciante() != null && !"".equals(form.getIdDireccionDenunciante())){
		        	guardarDenunciantePrincipal(form,request,expBean.getIdInstitucion_tipoExpediente(), expBean.getIdTipoExpediente());
		        }
		        //END BNS
		        
		        
		        tx.commit();
		        if(forward.equals(""))
					forward = exitoRefresco("messages.updated.success",request);
		    }catch (Exception e) {
		    	throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx); 
		    	forward = exito("messages.updated.error",request); 
		    }
		
		
		
		return forward;
		
		
	}
	
	private boolean guardarDenunciadoPrincipal(ExpDatosGeneralesForm form,
			HttpServletRequest request, Integer idInstitucion_tipoExpediente, Integer idTipoExpediente) throws ClsExceptions {
		boolean bOk = false;
		
		ExpDenunciadoBean denBean = new ExpDenunciadoBean();	    
	    ExpDenunciadoAdm  denAdm  = new ExpDenunciadoAdm(this.getUserBean(request));
	    Integer idInstitucion = this.getIDInstitucion(request);
	    String numExpediente = form.getNumExpediente();
	    String anioExpediente = form.getAnioExpediente();
	    
	    if(numExpediente==null || numExpediente.equals(""))
	           numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
	   
		if(anioExpediente==null || anioExpediente.equals(""))
	       anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");
		
		String[] claves = {ExpDenunciadoBean.C_IDINSTITUCION,ExpDenunciadoBean.C_IDTIPOEXPEDIENTE,
				ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE,ExpDenunciadoBean.C_NUMEROEXPEDIENTE,
				ExpDenunciadoBean.C_ANIOEXPEDIENTE,ExpDenunciadoBean.C_IDDENUNCIADO};
		
		Hashtable hash = new Hashtable();
		hash.put(ExpDenunciadoBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ExpDenunciadoBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
		hash.put(ExpDenunciadoBean.C_IDINSTITUCION_TIPOEXPEDIENTE, idInstitucion_tipoExpediente);
		hash.put(ExpDenunciadoBean.C_NUMEROEXPEDIENTE, numExpediente);
		hash.put(ExpDenunciadoBean.C_ANIOEXPEDIENTE, anioExpediente);
		hash.put(ExpDenunciadoBean.C_IDDENUNCIADO, ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL);
		
		if (denAdm.deleteDirect(hash, claves)){			
		    denBean.setIdInstitucion(idInstitucion);
		    denBean.setIdTipoExpediente(idTipoExpediente);
		    denBean.setIdInstitucion_TipoExpediente(idInstitucion_tipoExpediente);						
		    denBean.setNumeroExpediente(Integer.valueOf(numExpediente));
		    denBean.setAnioExpediente(Integer.valueOf(anioExpediente));
		    denBean.setIdDenunciado(ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL);
		    denBean.setIdPersona(Long.valueOf(form.getIdPersonaDenunciado()));		    
		    denBean.setIdDireccion(Long.valueOf(form.getIdDireccionDenunciado()));
		    if (form.getIdInstitucionOrigenDenunciado() != null && !"".equals(form.getIdInstitucionOrigenDenunciado()))
		    	denBean.setIdInstitucionOrigen(Integer.valueOf(form.getIdInstitucionOrigenDenunciado()));
		    	    
		    if(denAdm.insert(denBean)) {
		    	bOk = true;
		    }
		}
	    return bOk;
	}
	
	private boolean guardarDenunciantePrincipal(ExpDatosGeneralesForm form,
			HttpServletRequest request, Integer idInstitucion_tipoExpediente, Integer idTipoExpediente) throws ClsExceptions {
		boolean bOk = false;
		
		ExpDenuncianteBean denBean = new ExpDenuncianteBean();	    
	    ExpDenuncianteAdm  denAdm  = new ExpDenuncianteAdm(this.getUserBean(request));
	    Integer idInstitucion = this.getIDInstitucion(request);
	    String numExpediente = form.getNumExpediente();
	    String anioExpediente = form.getAnioExpediente();
	    
	    if(numExpediente==null || numExpediente.equals(""))
	           numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
	   
		if(anioExpediente==null || anioExpediente.equals(""))
	       anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");
	    
		String[] claves = {ExpDenuncianteBean.C_IDINSTITUCION,ExpDenuncianteBean.C_IDTIPOEXPEDIENTE,
				ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,ExpDenuncianteBean.C_NUMEROEXPEDIENTE,
				ExpDenuncianteBean.C_ANIOEXPEDIENTE,ExpDenuncianteBean.C_IDDENUNCIANTE};
		
		Hashtable hash = new Hashtable();
		hash.put(ExpDenuncianteBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ExpDenuncianteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);
		hash.put(ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, idInstitucion_tipoExpediente);
		hash.put(ExpDenuncianteBean.C_NUMEROEXPEDIENTE, numExpediente);
		hash.put(ExpDenuncianteBean.C_ANIOEXPEDIENTE, anioExpediente);
		hash.put(ExpDenuncianteBean.C_IDDENUNCIANTE, ExpDenuncianteBean.ID_DENUNCIANTE_PRINCIPAL);
		
		if (denAdm.deleteDirect(hash, claves)){			
		    denBean.setIdInstitucion(idInstitucion);
		    denBean.setIdTipoExpediente(idTipoExpediente);
		    denBean.setIdInstitucion_TipoExpediente(idInstitucion_tipoExpediente);
		    denBean.setNumeroExpediente(Integer.valueOf(numExpediente));
		    denBean.setAnioExpediente(Integer.valueOf(anioExpediente));
		    denBean.setIdDenunciante(ExpDenuncianteBean.ID_DENUNCIANTE_PRINCIPAL);
		    denBean.setIdPersona(Long.valueOf(form.getIdPersonaDenunciante()));
		    denBean.setIdDireccion(Long.valueOf(form.getIdDireccionDenunciante()));
		    if (form.getIdInstitucionOrigenDenunciante() != null && !"".equals(form.getIdInstitucionOrigenDenunciante()))
		    	denBean.setIdInstitucionOrigen(Integer.valueOf(form.getIdInstitucionOrigenDenunciante()));
		    
		    if(denAdm.insert(denBean)) {
		    	bOk = true;
		    }
		}
	    return bOk;
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


			HttpSession session = request.getSession();
			UsrBean user = (UsrBean)session.getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();
			Hashtable hash = new Hashtable();
			ExpExpedienteBean expDatosParticulares = null;
			request.setAttribute("accion","nuevo");
			
			if(session.getAttribute("DATABACKUP") != null){
				if(session.getAttribute("DATABACKUP") instanceof Hashtable){
					hash = (Hashtable)session.getAttribute("DATABACKUP");	  
	
				}else if(session.getAttribute("DATABACKUP") instanceof HashMap<?, ?>){
					HashMap hashMap = (HashMap)session.getAttribute("DATABACKUP");	  
					hash = (Hashtable)hashMap.get("datosGenerales");
					expDatosParticulares = (ExpExpedienteBean)hashMap.get("datosParticulares");
				} else {
					throw new ClsExceptions("Se ha perdido la información de sesión (DATABACKUP no encontrado)");
				}
			} else {
				throw new ClsExceptions("Se ha perdido la información de sesión (DATABACKUP no encontrado)");
			}
			
			String idInstitucion_TipoExpediente = "";
			if (hash.containsKey(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE) && 
					!"".equals((String)hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)))
				idInstitucion_TipoExpediente = (String)hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE);
			else
				throw new ClsExceptions("Se ha perdido la información de sesión ("+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" no encontrado)");
			
			String idTipoExpediente = "";
			if (hash.containsKey(ExpExpedienteBean.C_IDTIPOEXPEDIENTE) && 
					!"".equals((String)hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)))
				idTipoExpediente = (String)hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE);
			else
				throw new ClsExceptions("Se ha perdido la información de sesión ("+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" no encontrado)");
			

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
			expBean.setIdPersonaDenunciado(form.getIdPersonaDenunciado().equals("")?null:Long.valueOf(form.getIdPersonaDenunciado()));
			expBean.setIdPersonaDenunciante(form.getIdPersonaDenunciante().equals("")?null:Long.valueOf(form.getIdPersonaDenunciante()));
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
			expBean.setIdDireccion(form.getIdDireccionDenunciado());
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
			//BEGIN BNS Si no es obligatorio y no se rellena mete la fecha actual
			String sFecha = form.getFecha();
			Date fechaActual = null;
			if (sFecha == null || sFecha.equals(""))
				fechaActual = new Date();
			else
				fechaActual = (Date)GstDate.convertirFechaDiaMesAnio(sFecha);
			//END BNS
			
			if (!hash.containsKey(ExpExpedienteBean.C_IDINSTITUCION) ||
					!hash.containsKey(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE) ||
					!hash.containsKey(ExpExpedienteBean.C_IDTIPOEXPEDIENTE))
				throw new ClsExceptions("Se ha perdido la información de sesión ("+ExpExpedienteBean.C_IDINSTITUCION+" o "+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" o "+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" no encontrado)");
			
			Hashtable numAnioExp= expAdm.getNewNumAnioExpediente(hash,GstDate.getYear(fechaActual));
			Integer numExp = (Integer)numAnioExp.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE);
			
			numExpAGuardar = numExp;
			anioExpAGuardar = Integer.valueOf(GstDate.getYear(fechaActual));
			
			expBean.setNumeroExpediente(numExpAGuardar);
			expBean.setAnioExpediente(anioExpAGuardar);	

			if(form.getNumExpediente()!=null && !form.getNumExpediente().trim().equalsIgnoreCase("")){
				hash.put("copia","s");
				session.setAttribute("copiaSession", "s");	
		    }else{
		    	session.setAttribute("copiaSession", "");
		    	hash.put("copia","");
		    }
			form.setAnioExpediente(anioExpAGuardar.toString());
			form.setNumExpediente(numExpAGuardar.toString());
			session.setAttribute("anioExpedienteSession", anioExpAGuardar.toString());			
			session.setAttribute("numeroExpedienteSession", numExp.toString());
			session.setAttribute("institucionSession",idInstitucion);
			session.setAttribute("institucionExpeSession",idInstitucion_TipoExpediente);
			if(form.getRelacionExpediente()!=null && !form.getRelacionExpediente().trim().equals("")){
				session.setAttribute("nombreExpedienteSession", form.getNombreRelacionExpediente());
				session.setAttribute("tipoExpedienteSession", form.getRelacionExpediente());
			}else{
				session.setAttribute("nombreExpedienteSession", form.getTipoExpediente());
				session.setAttribute("tipoExpedienteSession", idTipoExpediente);
			}
			//Pasar el nume y el año
			request.setAttribute("numeroExpediente", numExp.toString());
			request.setAttribute("anioExpediente", anioExpAGuardar.toString());
			session.setAttribute("numeroExpediente", numExp.toString());
			session.setAttribute("anioExpediente", anioExpAGuardar.toString());

			
			hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpAGuardar.toString());
			hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExp.toString());	
						
			String collectionTitle = null;
			if (anioExpAGuardar != null && numExpAGuardar != null) {
				collectionTitle = DocuShareHelper.getTitleExpedientes(form.getTipoExpediente(), anioExpAGuardar.toString(), numExpAGuardar.toString()) ;
			}
			
			/* Sólo se intentará la Conexion al DocuShare si el parámetro general para la institucion=1*/	
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(user);
			String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
			if (valor!=null && valor.equals("1")){				
				expBean.setIdentificadorDS(obtenerIdentificadorDS(getIDInstitucion(request).shortValue(), collectionTitle));
			}
			
			//Comprobamos que coincide con los propuestos al presionar en 'nuevo', si no, informamos de los nuevos valores
			if (!form.getNumExpediente().equals(String.valueOf(numExpAGuardar)) && !form.getAnioExpediente().equals(String.valueOf(anioExpAGuardar))){
				request.setAttribute("sufijo",anioExpAGuardar+" / "+numExpAGuardar);
			}
			
			//Iniciamos la transacción
			tx = user.getTransaction();
			tx.begin();   
								
			//Ahora procedemos a insertarlo
			if (!expAdm.insert(expBean))
				throw new ClsExceptions(expAdm.getError());
							
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
			
			//Se inserta el denunciado si hay
			if (form.getIdPersonaDenunciado() != null && !form.getIdPersonaDenunciado().equals("")){
			    ExpDenunciadoBean denBean = new ExpDenunciadoBean();	    
			    ExpDenunciadoAdm  denAdm  = new ExpDenunciadoAdm(this.getUserBean(request));
			    denBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			    denBean.setIdInstitucion_TipoExpediente(Integer.valueOf(idInstitucion_TipoExpediente));
			    denBean.setIdTipoExpediente(Integer.valueOf(idTipoExpediente));  
			    denBean.setNumeroExpediente(Integer.valueOf(form.getNumExpediente()));
			    denBean.setAnioExpediente(Integer.valueOf(form.getAnioExpediente()));
			    denBean.setIdDenunciado(ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL);
			    denBean.setIdPersona(Long.valueOf(form.getIdPersonaDenunciado()));
			    		    
			    if(!denAdm.insert(denBean)) {
				    return this.exitoModalSinRefresco("messages.inserted.error",request);
			    }		
			}
			
			//Se inserta el denunciante si hay
			if (form.getIdPersonaDenunciante() != null && !form.getIdPersonaDenunciante().equals("")){
			    ExpDenuncianteBean denBean = new ExpDenuncianteBean();	    
			    ExpDenuncianteAdm  denAdm  = new ExpDenuncianteAdm(this.getUserBean(request));
			    denBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			    denBean.setIdInstitucion_TipoExpediente(Integer.valueOf(idInstitucion_TipoExpediente));
			    denBean.setIdTipoExpediente(Integer.valueOf(idTipoExpediente));  
			    denBean.setNumeroExpediente(Integer.valueOf(form.getNumExpediente()));
			    denBean.setAnioExpediente(Integer.valueOf(form.getAnioExpediente()));
			    denBean.setIdDenunciante(ExpDenuncianteBean.ID_DENUNCIANTE_PRINCIPAL);
			    denBean.setIdPersona(Long.valueOf(form.getIdPersonaDenunciante()));
			    		    
			    if(!denAdm.insert(denBean)) {
				    return this.exitoModalSinRefresco("messages.inserted.error",request);
			    }		
			}
			
			String sCopia = "N";
			if (hash.containsKey("copia"))
				sCopia = (String) hash.get("copia");
			if ("S".equalsIgnoreCase(sCopia)){
				if (expDatosParticulares != null){
					// Obtenemos los datos del expediente orignal
					ExpExpedienteBean oldExpBean = new ExpExpedienteBean();
					oldExpBean.setIdInstitucion(expDatosParticulares.getIdInstitucion());
				    oldExpBean.setIdInstitucion_tipoExpediente(expDatosParticulares.getIdInstitucion_tipoExpediente());
				    oldExpBean.setIdTipoExpediente(expDatosParticulares.getIdTipoExpediente());  
				    oldExpBean.setNumeroExpediente(expDatosParticulares.getNumeroExpediente());
				    oldExpBean.setAnioExpediente(expDatosParticulares.getAnioExpediente());
				    
					// Insertamos todos los denunciados en el nuevo	expediente							   
				    ExpDenunciadoAdm  denAdm  = new ExpDenunciadoAdm(this.getUserBean(request));
				    
				    List<ExpDenunciadoBean> denunciadosExpOld = denAdm.getDenunciados(oldExpBean);
				    if (denunciadosExpOld != null && denunciadosExpOld.size() > 0){
				    	java.util.Iterator<ExpDenunciadoBean> iteraDenunciadosExpOld = denunciadosExpOld.iterator();
				    	while (iteraDenunciadosExpOld.hasNext()){
				    		ExpDenunciadoBean denunciado = iteraDenunciadosExpOld.next();
				    		if (!ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL.equals(denunciado.getIdDenunciado())){
					    		denunciado.setIdInstitucion(Integer.valueOf(idInstitucion));
					    		denunciado.setIdInstitucion_TipoExpediente(Integer.valueOf(idInstitucion_TipoExpediente));
					    		denunciado.setIdTipoExpediente(Integer.valueOf(idTipoExpediente));  
					    		denunciado.setNumeroExpediente(Integer.valueOf(form.getNumExpediente()));
					    		denunciado.setAnioExpediente(Integer.valueOf(form.getAnioExpediente()));
					    		
					    		if(!denAdm.insert(denunciado)) {
								    return this.exitoModalSinRefresco("messages.inserted.error",request);
					    		}
				    		}
				    	}
				    }
				    
				    // Insertamos todos los denunciantes en el nuevo expediene						   
				    ExpDenuncianteAdm  denteAdm  = new ExpDenuncianteAdm(this.getUserBean(request));						    
				    
				    List<ExpDenuncianteBean> denunciantesExpOld = denteAdm.getDenunciantes(oldExpBean);
				    if (denunciantesExpOld != null && denunciantesExpOld.size() > 0){
				    	java.util.Iterator<ExpDenuncianteBean> iteraDenunciantesExpOld = denunciantesExpOld.iterator();
				    	while (iteraDenunciantesExpOld.hasNext()){
				    		ExpDenuncianteBean denunciante = iteraDenunciantesExpOld.next();
				    		if (!ExpDenuncianteBean.ID_DENUNCIANTE_PRINCIPAL.equals(denunciante.getIdDenunciante())){
					    		denunciante.setIdInstitucion(Integer.valueOf(idInstitucion));
					    		denunciante.setIdInstitucion_TipoExpediente(Integer.valueOf(idInstitucion_TipoExpediente));
					    		denunciante.setIdTipoExpediente(Integer.valueOf(idTipoExpediente));  
					    		denunciante.setNumeroExpediente(Integer.valueOf(form.getNumExpediente()));
					    		denunciante.setAnioExpediente(Integer.valueOf(form.getAnioExpediente()));
					    		
					    		if(!denteAdm.insert(denunciante)) {
								    return this.exitoModalSinRefresco("messages.inserted.error",request);
					    		}
				    		}
				    	}
				    }
				} else {
					return this.exitoModalSinRefresco("messages.inserted.error",request);
				}
			}
			
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
						//hEjSancion.put("idPersona",form.getIdPersonaDenunciado());
						//aalg: para poder modificar el estado al denunciado en la tabla exp_denunciado
	            		hEjSancion.put("IdInstitucion_tipoExpediente", expBean.getIdInstitucion_tipoExpediente());
	            		hEjSancion.put("IdTipoExpediente", expBean.getIdTipoExpediente());
	            		hEjSancion.put("numeroExpediente", expBean.getNumeroExpediente());
	            		hEjSancion.put("anioExpediente", expBean.getAnioExpediente());
						session.setAttribute("ejecucionSancion",hEjSancion);
						tx.commit();
						return "ejecucionSancion";
					}
				}
			}
			
			tx.commit();			
			
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
			
			 if(datosExp.get("copia")!=null && datosExp.get("copia").toString().equalsIgnoreCase("s")){
				 form.setNumExpediente((String)datosExp.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE));
				 form.setAnioExpediente((String)datosExp.get(ExpExpedienteBean.C_ANIOEXPEDIENTE));
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
	 * @throws SIGAServiceException 
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private String obtenerIdentificadorDS(short idInstitucion, String collectionTitle) throws SIGAServiceException {
			
		DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);
		
		String idDS = docuShareHelper.buscaCollectionExpedientes(collectionTitle);
		
		if (idDS == null || idDS.trim().equals(idDS)) {
			idDS = docuShareHelper.createCollectionExpedientes(collectionTitle);	
		} 
			
		return idDS;
				
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
	
	
	public void guardarValor(ExpCamposValorAdm expCamposValorAdm,Hashtable auxHash, String idCampoConf, String valor) throws ClsExceptions {
        // comprobamos si existe. SI es asi modificamos, si no lo creamos.
        Vector salida = new Vector();
        Hashtable codigos = new  Hashtable();        
        								
		codigos.put(new Integer(1),auxHash.get("IDINSTITUCION").toString());
		codigos.put(new Integer(2),auxHash.get("IDINSTITUCION_TIPOEXPEDIENTE").toString());		
		codigos.put(new Integer(3),auxHash.get("IDTIPOEXPEDIENTE").toString());		
		codigos.put(new Integer(4),auxHash.get("NUMEROEXPEDIENTE").toString());		
		codigos.put(new Integer(5),auxHash.get("ANIOEXPEDIENTE").toString());		
		codigos.put(new Integer(6),auxHash.get("IDCAMPO").toString());		
		codigos.put(new Integer(7),auxHash.get("IDPESTANACONF").toString());
		codigos.put(new Integer(8),idCampoConf);
				
		try {		
			String sql = " where idinstitucion=:1 " + 
			    "       and   idinstitucion_tipoexpediente=:2 " + 
			    "       and   idtipoexpediente=:3 " + 
			    "       and   numeroexpediente=:4 " + 
			    "       and   anioexpediente=:5 " + 
			    "       and   idcampo=:6 " + 
			    "       and   idpestanaconf=:7 " + 
			    "       and   idcampoconf=:8";

			// RGG cambio visibilidad
			Vector v = expCamposValorAdm.selectBind(sql,codigos);
			if (v!=null && v.size()>0) {
				ExpCamposValorBean bean = (ExpCamposValorBean) v.get(0);
				bean.setValor(valor.trim());
				if (!expCamposValorAdm.updateDirect(bean)) {
				    throw new ClsExceptions("Error al actualizar campo valor. "+expCamposValorAdm.getError());
				}
				
			} else {
			    // hay que insertarlo.
			    ExpCamposValorBean bean = new ExpCamposValorBean();
			    bean.setIdInstitucion(new Integer(auxHash.get("IDINSTITUCION").toString()));
			    bean.setIdInstitucion_TipoExpediente(new Integer(auxHash.get("IDINSTITUCION_TIPOEXPEDIENTE").toString()));
			    bean.setIdTipoExpediente(new Integer(auxHash.get("IDTIPOEXPEDIENTE").toString()));
			    bean.setNumeroExpediente(new Integer(auxHash.get("NUMEROEXPEDIENTE").toString()));
			    bean.setAnioExpediente(new Integer(auxHash.get("ANIOEXPEDIENTE").toString()));
			    bean.setIdCampo(new Integer(auxHash.get("IDCAMPO").toString()));
			    bean.setIdPestanaConf(new Integer(auxHash.get("IDPESTANACONF").toString()));
			    bean.setIdCampoConf(new Integer(idCampoConf));
			    bean.setValor(valor);
				if (!expCamposValorAdm.insert(bean)) {
				    throw new ClsExceptions("Error al insertar campo valor. "+expCamposValorAdm.getError());
				}
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'guardarValor' en B.D.");		
		}		
    }
	
	/**
	 * Método que crea las modificaciones necesarias para mostrar de manera dinámica las cajas de texto en la etiqueta de pestaña configurable
	 * @param nombresLongitud
	 * @param datosCamposPestanasLongitud
	 * @return Hashtable	 
	 */
	public Hashtable muestraCamposDinamicos(Vector nombresLongitud,Vector datosCamposPestanasLongitud)	
	{
		Hashtable mostrarTexto = new  Hashtable();        
		
		Vector totales = new Vector();
		Vector saltoLinea = new Vector();
		
		for (int m =0;m<5;m++)
		{
			totales.add("");
			saltoLinea.add("");			
		}
		
		int valor1=124,valor2=144;		
		for (int i=0;i<5;i++)
		{				
			Integer iLongitudCampo = (Integer)nombresLongitud.get(i);
			Integer iLongitudValor = (Integer)datosCamposPestanasLongitud.get(i);
			if(iLongitudCampo!= null && iLongitudCampo.intValue()!=0)
			{				
				int total=iLongitudCampo.intValue();				
				// totalRestanteSizeLinea guarda los espacios que le quedan a la caja de texto para
				//llegar al final de la linea
				int totalRestanteSizeLinea= valor1-iLongitudCampo.intValue();								
				int totalRestanteSizeLineaTotal= valor2-iLongitudCampo.intValue();
																								
				// Si el contenido de la caja de texto es superior a totalRestanteSize entonces
				// se sustituye el nº de caracteres q tiene el contenido por el maximo permitido
				//para que no sobrepase la linea, que máximo sería de 149 espacios.
				
				if(iLongitudValor.intValue()>totalRestanteSizeLineaTotal)
				{
					datosCamposPestanasLongitud.set(i,iLongitudValor.valueOf(totalRestanteSizeLineaTotal));
					total += totalRestanteSizeLinea;
				}
				else
				{
					if(iLongitudValor.intValue()>totalRestanteSizeLinea)
					{
						datosCamposPestanasLongitud.set(i,iLongitudValor.valueOf(totalRestanteSizeLinea));
						total += totalRestanteSizeLinea;
					}
					else
						total += iLongitudValor.intValue();
				}																					
				totales.set(i,""+total);
			}
		}
		mostrarTexto.put("datosCamposPestanasLongitud",datosCamposPestanasLongitud);
		
		int totalCampos=0;
		for (int i=0;i<5;i++)
		{
			String sTotales= (String)totales.get(i);
			
			if(sTotales!=null && !sTotales.equals(""))
			{
				Integer iTotales= new Integer(sTotales);
				totalCampos += iTotales.intValue();
												
				if(totalCampos>valor1)
				{
					saltoLinea.set(i, ""+i);
					totalCampos = iTotales.intValue();					
				}								
			}						
		}
		mostrarTexto.put("saltoLinea",saltoLinea);
				
		return mostrarTexto;
	}
	
}
