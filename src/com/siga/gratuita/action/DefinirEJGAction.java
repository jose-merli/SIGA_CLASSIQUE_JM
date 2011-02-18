//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
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
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.BusquedaClientesFiltrosAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsBeneficiarioSOJAdm;
import com.siga.beans.ScsBeneficiarioSOJBean;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsContrariosAsistenciaBean;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsContrariosEJGAdm;
import com.siga.beans.ScsContrariosEJGBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsDelitosDesignaAdm;
import com.siga.beans.ScsDelitosDesignaBean;
import com.siga.beans.ScsDelitosEJGAdm;
import com.siga.beans.ScsDelitosEJGBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasProcuradorAdm;
import com.siga.beans.ScsDesignasProcuradorBean;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsDocumentacionEJGBean;
import com.siga.beans.ScsDocumentacionSOJAdm;
import com.siga.beans.ScsDocumentacionSOJBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.certificados.Plantilla;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.form.BuscarDesignasForm;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;
import com.siga.informes.InformeDefinirEJG;



/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_EJG
*/
public class DefinirEJGAction extends MasterAction 
{
	final String[] clavesBusqueda={ScsEJGBean.C_IDINSTITUCION,ScsEJGBean.C_IDTIPOEJG,ScsEJGBean.C_ANIO
			,ScsEJGBean.C_NUMERO};
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					DefinirEJGForm form = (DefinirEJGForm)miForm;
					form.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					form.reset(mapping,request);
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("buscarInit")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPor(mapping, miForm, request, response); 
				}else if (accion.equalsIgnoreCase("generarCarta")){
					mapDestino = generarCarta(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("finalizarCarta")){
					mapDestino = finalizarCarta(mapping, miForm, request, response);
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
		return mapping.findForward(mapDestino);
	}
	
	/**
	 * Actualiza el paginador con los datos del letrado relacionado con la designacion o asistencia 
	 * @throws SIGAException 
	 */
	private Vector actualizarPagina(HttpServletRequest request,ScsEJGAdm admBean,Vector datos) throws ClsExceptions, SIGAException{
		String letradoDes = "";
		String turnoDes = "";
		// Obtenemos las relaciones de la EJG con designaciones y asistencias para mostrarlas en la busqueda
		try{
			for (int i=0; i<datos.size(); i++){
				Row fila = (Row)datos.get(i);
				Hashtable registro = (Hashtable) fila.getRow();
				String anio = registro.get("ANIO").toString();
				String idInstitucion = registro.get("IDINSTITUCION").toString();
				String idTipo = registro.get("IDTIPOEJG").toString();
				String numero = registro.get("NUMERO").toString();
				registro.put(ScsEJGBean.C_FECHAAPERTURA, admBean.getFechaAperturaEJG(idInstitucion, idTipo, anio, numero));
				// Obtenemos un vector con las relaciones del EJG
				Vector relacionados = new Vector();
				relacionados = admBean.getRelacionadoCon(idInstitucion, anio, numero, idTipo);
				letradoDes = "";
				turnoDes = "";
				if (relacionados.size()>0){
					// Nos recorremos las relaciones buscando informacion
					for (int r=0; r<relacionados.size(); r++){
						Hashtable elementoRel = new Hashtable();
						elementoRel = (Hashtable)relacionados.get(r);
						String tipoSJCS = UtilidadesHash.getString(elementoRel, "SJCS");
						if (tipoSJCS.equalsIgnoreCase("DESIGNA")){
							// Si se trata de una designa sacamos el idTurno y el año de la designa para poder 
							// recuperar el nombre del letrado
							String anioDes = UtilidadesHash.getString(elementoRel, "ANIO");
							ScsDesignaAdm desAdm = new ScsDesignaAdm(this.getUserBean(request));
							String idTurno = UtilidadesHash.getString(elementoRel, "IDTURNO");
							String numDes = UtilidadesHash.getString(elementoRel, "NUMERO");
							letradoDes = desAdm.getApeNomDesig(idInstitucion, idTurno, anioDes, numDes);
							turnoDes = UtilidadesHash.getString(elementoRel, "DES_TURNO");
						}else if (tipoSJCS.equalsIgnoreCase("ASISTENCIA")){
							// Damos preferencia a las designas. Solo usaremos las asistencias o SOJ cuando
							// No tengamos datos de la designa. Si luego aparecen sobreescribiran estos.
							String idLetrado = UtilidadesHash.getString(elementoRel, "IDLETRADO");
							if (idLetrado != null){
								Vector vPersona = new Vector();
								CenPersonaAdm perAdm = new CenPersonaAdm(this.getUserBean(request));
								// Si se trata de una asistencia o SOJ buscamos al letrado en el censo
								vPersona = perAdm.getDatosPersonaTag(idInstitucion, idLetrado);
								if (vPersona.size()>0){
									turnoDes = UtilidadesHash.getString(elementoRel, "DES_TURNO");
									letradoDes = UtilidadesHash.getString((Hashtable)vPersona.get(0),"NOMBRE");
								}
							}
						}

					}
				}

				// Finalmente escribimos los datos en el paginador (si no ha encontrado nada que quede vacio)
				if((letradoDes.equals(", "))||(letradoDes.equals(""))){
					letradoDes="-";
				}
				if((turnoDes.equals(", "))||(turnoDes.equals(""))){
					turnoDes="-";
				}
				registro.put("LETRADODESIGNA", letradoDes);
				registro.put("TURNODESIGNA", turnoDes);

			} // JBD INC-CAT-5
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return datos;

	}
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la consulta a partir de esos datos. Almacena un vector con los resultados
	 * en la sesión con el nombre "resultado"
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {

		ScsEJGAdm admBean =new ScsEJGAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirEJGForm miFormulario =(DefinirEJGForm)formulario;
		Hashtable miHash= new Hashtable();
		miHash = miFormulario.getDatos();
		String consulta= "";
		String idInstitucion= usr.getLocation();	
		try {

			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miFormulario.getSeleccionarTodos()!=null 
				&& !miFormulario.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a fectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
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
				// jbd //
				actualizarPagina(request, admBean, datos);
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);




			}else{	

				databackup=new HashMap();

				//obtengo datos de la consulta 			
				PaginadorBind resultado = null;
				resultado = admBean.getPaginadorBusquedaMantenimientoEJG(miHash, miFormulario,idInstitucion);
//				resultado=desigAdm.getBusquedaDesigna((String)usr.getLocation(),miHash);
				Vector datos = null;



				databackup.put("paginador",resultado);
				
				if (resultado!=null && resultado.getNumeroTotalRegistros()>0){ 
					
					
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)admBean.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
						datos = resultado.obtenerPagina(Integer.parseInt(miFormulario.getSeleccionarTodos()));
						miFormulario.setSeleccionarTodos("");
						
					}else{
//					
						miFormulario.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					// jbd //
					actualizarPagina(request, admBean, datos);
					databackup.put("datos",datos);
						
					
					
				}else{
					resultado = null;
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
				

				//resultado = admBean.selectGenerico(consulta);
				//request.getSession().setAttribute("resultado",v);
			}
		
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado			
			miHash.put("BUSQUEDAREALIZADA","1");
			request.getSession().setAttribute("DATOSFORMULARIO",miHash);	
			request.getSession().setAttribute("BUSQUEDAREALIZADA", consulta.toString());


		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}

		return "listarEJG";
	}
	
	
	protected String buscarPorOld(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {		
				
		
		ScsEJGAdm admBean;		
		Vector v = new Vector ();
		Hashtable miHash= new Hashtable();
		String consulta = "";
		
		try {
			
			DefinirEJGForm miForm = (DefinirEJGForm) formulario;		
			admBean =  new ScsEJGAdm(this.getUserBean(request));				
			miHash = miForm.getDatos();
			
			HashMap databackup=new HashMap();
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion= user.getLocation();		
			
			
		    if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
			 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
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
			
			// JBD INC-CAT-5
			 actualizarPagina(request, admBean, datos);
			 	
			databackup.put("paginador",paginador);
			databackup.put("datos",datos);
		  }else{	
				
			  	databackup=new HashMap();
					
				//obtengo datos de la consulta 			
				Vector datos = null;
				
				//v = admBean.selectGenerico(consulta);
	 			//	PaginadorBind paginador = new PaginadorBind(consulta);				
				PaginadorBind paginador = admBean.getPaginadorBusquedaMantenimientoEJG(miHash, miForm,idInstitucion);
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
		       	
				databackup.put("paginador",paginador);
				if (paginador!=null){ 
				   datos = paginador.obtenerPagina(1);
					// JBD INC-CAT-5
					 actualizarPagina(request, admBean, datos);

				   databackup.put("datos",datos);
				   request.getSession().setAttribute("DATAPAGINADOR",databackup);
				} 	
			
				//resultado = admBean.selectGenerico(consulta);
				//request.getSession().setAttribute("resultado",v);
		  }
  		  
		  // En "DATOSFORMULARIO" almacenamos el identificador del letrado			 			 
		  miHash.put("BUSQUEDAREALIZADA","1");
		  // Pasamos los datos del cajg para devolverlos en la busqueda
		  if(miForm.getAnioCAJG()!=null){
			  miHash.put(ScsEJGBean.C_ANIO_CAJG, miForm.getAnioCAJG());
		  }
		  if(miForm.getNumeroCAJG()!=null){
			  miHash.put(ScsEJGBean.C_NUMERO_CAJG, miForm.getNumeroCAJG());
		  }
		  if(miForm.getIdRenuncia()!=null){
			  miHash.put(ScsEJGBean.C_IDRENUNCIA, miForm.getIdRenuncia());
		  }
		
		  request.getSession().setAttribute("DATOSFORMULARIO",miHash);			
			
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		} catch (Exception e) {
		   throwExcp("messages.general.error",e,null);
		}		
		return "listarEJG";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		try {		
			HttpSession session =request.getSession();
			DefinirEJGForm miForm = (DefinirEJGForm) formulario;		
			
			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();
			if ((request.getParameter("desdeEjg")!=null && request.getParameter("desdeEjg").equalsIgnoreCase("si"))||(request.getParameter("desdeDesigna")!=null && request.getParameter("desdeDesigna").equalsIgnoreCase("si"))){
				session.removeAttribute("DATAPAGINADOR");
			}
			
			if ((ocultos != null && visibles != null) || ((miForm.getDesdeDesigna() != null) && (miForm.getDesdeDesigna().equalsIgnoreCase("si")))) {
				miHash.put(ScsEJGBean.C_IDTIPOEJG,ocultos.get(0));
				miHash.put(ScsEJGBean.C_IDINSTITUCION,ocultos.get(1));
				miHash.put(ScsEJGBean.C_ANIO,ocultos.get(2));
				miHash.put(ScsEJGBean.C_NUMERO,ocultos.get(3));
				session.removeAttribute("origenEJG");
			}	
			else {
				session.removeAttribute("DATAPAGINADOR");
				miHash.put(ScsEJGBean.C_IDTIPOEJG,miForm.getIdTipoEJG());
				miHash.put(ScsEJGBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				miHash.put(ScsEJGBean.C_ANIO,miForm.getAnio());
				miHash.put(ScsEJGBean.C_NUMERO,miForm.getNumero());
			}
			
			//Entramos al formulario en modo 'modificación'
			session.setAttribute("accion","editar");
	
			// 03-04-2006 RGG cambio en ventanas de Personas JG
			// Persona JG
			ScsEJGAdm admi = new ScsEJGAdm(this.getUserBean(request)); 
			Vector resultadoObj = admi.selectPorClave(miHash);
			ScsEJGBean obj = (ScsEJGBean)resultadoObj.get(0);
			if (obj.getIdPersonaJG()==null) {
				miHash.put("idPersonaJG","");
			} else {
				miHash.put("idPersonaJG",obj.getIdPersonaJG().toString());
			}
			miHash.put("idInstitucionJG", miHash.get(ScsEJGBean.C_IDINSTITUCION));
			
			// CONCEPTO
			miHash.put("conceptoE",PersonaJGAction.EJG);
			// clave EJG
			miHash.put("idTipoEJG", miHash.get(ScsEJGBean.C_IDTIPOEJG));
			miHash.put("idInstitucionEJG", miHash.get(ScsEJGBean.C_IDINSTITUCION));
			miHash.put("anioEJG", miHash.get(ScsEJGBean.C_ANIO));
			miHash.put("numeroEJG", miHash.get(ScsEJGBean.C_NUMERO));
			
			// titulo
			//miHash.put("tituloE","gratuita.busquedaEJG.interesado");
			miHash.put("tituloE","pestana.justiciagratuitaejg.solicitante");
			miHash.put("localizacionE","gratuita.busquedaEJG.localizacion");
			// accion
			miHash.put("accionE","editar");
			// action
			miHash.put("actionE","/JGR_InteresadoEJG.do");
			
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
	 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
	 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
			  String[] pestanasOcultas=new String [1];
			  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_EJG;
			  request.setAttribute("pestanasOcultas",pestanasOcultas);
	 		}
			
			// En EJG pasamos la clave principal a todas las pestanhas que constituyen el mantenimiento de un EJG
			session.removeAttribute("origenEJG");
			request.setAttribute("EJG",miHash);
		} 
		catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return "editar";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		DefinirEJGForm miForm = (DefinirEJGForm) formulario;		
		try {	
			HttpSession session =request.getSession();
			//Entramos al formulario en modo 'modificación'
			session.setAttribute("accion","ver");
			if ((request.getParameter("desdeEjg")!=null && request.getParameter("desdeEjg").equalsIgnoreCase("si"))||(request.getParameter("desdeDesigna")!=null && request.getParameter("desdeDesigna").equalsIgnoreCase("si"))){
				session.removeAttribute("DATAPAGINADOR");
			}
			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			Vector visibles = formulario.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();
			if ((ocultos != null && visibles != null) || 
				((miForm.getDesdeDesigna() != null) && (miForm.getDesdeDesigna().equalsIgnoreCase("si")))) {
				miHash.put(ScsEJGBean.C_IDTIPOEJG,ocultos.get(0));
				miHash.put(ScsEJGBean.C_IDINSTITUCION,ocultos.get(1));
				miHash.put(ScsEJGBean.C_ANIO,ocultos.get(2));
				miHash.put(ScsEJGBean.C_NUMERO,ocultos.get(3));
				session.removeAttribute("origenEJG");
			}	
			else {
				session.removeAttribute("DATAPAGINADOR"); 
				miHash.put(ScsEJGBean.C_IDTIPOEJG,miForm.getIdTipoEJG());
				miHash.put(ScsEJGBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				miHash.put(ScsEJGBean.C_ANIO,miForm.getAnio());
				miHash.put(ScsEJGBean.C_NUMERO,miForm.getNumero());
				String origen=request.getParameter("origen");
				if(origen!=null){
					session.setAttribute("origenEJG",origen);
					if (origen.equals("SOJ")) {
						String modoVolver = request.getParameter("modoActualSOJParaVolver");
						if (modoVolver != null)
						    session.setAttribute("modoVolverSOJ", modoVolver);
					}
				}
			}

			// 03-04-2006 RGG cambio en ventanas de Personas JG
			// Persona JG
			ScsEJGAdm admi = new ScsEJGAdm(this.getUserBean(request)); 
			Vector resultadoObj = admi.selectPorClave(miHash);
			ScsEJGBean obj = (ScsEJGBean)resultadoObj.get(0);
			if (obj.getIdPersonaJG()==null) {
				miHash.put("idPersonaJG","");
			} else {
				miHash.put("idPersonaJG",obj.getIdPersonaJG().toString());
			}
			// CONCEPTO
			miHash.put("conceptoE",PersonaJGAction.EJG);

			
			
			if ((ocultos != null && visibles != null) || 
					((miForm.getDesdeDesigna() != null) && (miForm.getDesdeDesigna().equalsIgnoreCase("si")))) {			
			
				miHash.put("idInstitucionJG",ocultos.get(1));
				// clave EJG
				miHash.put("idTipoEJG",ocultos.get(0));
				miHash.put("idInstitucionEJG",ocultos.get(1));
				miHash.put("anioEJG",ocultos.get(2));
				miHash.put("numeroEJG",ocultos.get(3));
			} else  {
				miHash.put("idInstitucionJG",miForm.getIdInstitucion());
				// clave EJG
				miHash.put("idTipoEJG",miForm.getIdTipoEJG());
				miHash.put("idInstitucionEJG",miForm.getIdInstitucion());
				miHash.put("anioEJG",miForm.getAnio());
				miHash.put("numeroEJG",miForm.getNumero());
			}

			
			
			// titulo
			miHash.put("tituloE","gratuita.busquedaEJG.literal.expedientesEJG");
			miHash.put("localizacionE","gratuita.busquedaEJG.literal.expedientesEJG");
			// accion
			miHash.put("accionE","ver");
			// action
			miHash.put("actionE","/JGR_InteresadoEJG.do");
			
			
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
	 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
	 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
			  String[] pestanasOcultas=new String [1];
			  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_EJG;
			  request.setAttribute("pestanasOcultas",pestanasOcultas);
	 		}
			
			
			
			// En EJG pasamos la clave principal a todas las pestanhas que constituyen el mantenimiento de un EJG
			request.setAttribute("EJG",miHash);
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "editar";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "

.
.Fiesta" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		DefinirEJGForm miform = (DefinirEJGForm)formulario;
		try{
			
						
			request.setAttribute("asistenciaNumero", (String)miform.getAsistenciaNumero());
			request.setAttribute("asistenciaAnio", (String)miform.getAsistenciaAnio());
		}
		catch(Exception e){
			request.setAttribute("asistenciaNumero", "");
			request.setAttribute("asistenciaAnio", "");
		}
		try{
			
			request.setAttribute("designaNumero", (String)miform.getDesignaNumero());
			request.setAttribute("designaAnio", (String)miform.getDesignaAnio());
			request.setAttribute("designaIdTurno", (String)miform.getDesignaIdTurno());			
			
		}
		catch(Exception e){
			request.setAttribute("designaNumero", "");
			request.setAttribute("designaAnio", "");
			request.setAttribute("designaIdTurno", "");
			
		}
		return "insertarEJG";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected  synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx=null;
		
		try {
		
			Hashtable miHash = new Hashtable(); 
			Hashtable hashTemporal = new Hashtable();	
			HttpSession session =request.getSession();

			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirEJGForm miForm = (DefinirEJGForm) formulario;	
			
			miHash = miForm.getDatos();
			Hashtable miHashSOJ=new Hashtable(); 
			Hashtable miHashSOJDocu=new Hashtable(); 
			Hashtable miHashDocuEJG=new Hashtable();
			Hashtable hashAux=new Hashtable(); 
			
			// 1. Obtenemos el idPersona
			// Si hemos introducido manualmente el numero de colegiado, no sabremos su idPersona lo consultamos de BD
//			if ((miForm.getIdPersona()==null)||(miForm.getIdPersona().equals("")) ||(miForm.getIdPersona().equals("null")) ){
//				try{
//					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
//					String idPersonaSel = colegiadoAdm.getIdPersona(miForm.getNColegiado(), this.getIDInstitucion(request).toString());
//					miHash.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersonaSel);
//				}
//				catch(Exception e){
//					throwExcp("No existe colegiado con ese NColegiado.",e,null);
//				}
//			}
			ScsEJGAdm ejgAdm =  new ScsEJGAdm(this.getUserBean(request));
			

			tx = usr.getTransaction();		
			tx.begin();
			// Preparamos los datos para insertarlos (formato de fecha adecuado, 
			// y campos que se rellenan automáticamente y que vienen vacíos de la jsp
			miHash = ejgAdm.prepararInsert(miHash);
			
			//OBTENCION DEL NUMEJG DEL EJG
			
			/*String numEJG;
    		GestorContadores cont=new GestorContadores(this.getUserBean(request));
    		Hashtable contadorHash=cont.getContador(this.getIDInstitucion(request),ClsConstants.EJG);
    		numEJG=cont.getNuevoContador(contadorHash);
    		
    		cont.setContador(contadorHash,numEJG);
    		
    		miHash.put(ScsEJGBean.C_NUMEJG, numEJG);*/

						
			ScsSOJBean sojBean=null;
			ScsDefinirSOJAdm sojAdm = new ScsDefinirSOJAdm (this.getUserBean(request));

			// Desde SOJ o nuevo EJG
			if (((miForm.getDesignaAnio()    == null || miForm.getDesignaAnio().equals("")) &&
				 (miForm.getAsistenciaAnio() == null || miForm.getAsistenciaAnio().equals("")))) { 

								
/**/      if (miForm.getSOJIdTipoSOJ()!=null){//Sólo en el caso de un EJG dado de alta desde un SOJ
			  // Recuperamos los datos introducidos en la pestana Datos Generales del SOJ para insertar aquellos que nos convengan en EJG
	            request.getSession().removeAttribute("DATAPAGINADOR");
				miHashSOJ.put(ScsSOJBean.C_ANIO, miForm.getSOJAnio());
				miHashSOJ.put(ScsSOJBean.C_NUMERO, miForm.getSOJNumero());
				miHashSOJ.put(ScsSOJBean.C_IDTIPOSOJ,miForm.getSOJIdTipoSOJ());
				miHashSOJ.put(ScsSOJBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				Vector vSOJ = sojAdm.selectByPK(miHashSOJ);
				
				if ((vSOJ != null) && (vSOJ.size() == 1)) {
					sojBean = (ScsSOJBean) vSOJ.get(0);
					miHash.put(ScsEJGBean.C_OBSERVACIONES,sojBean.getDescripcionConsulta());
					miHash.put(ScsEJGBean.C_DELITOS,sojBean.getRespuestaLetrado());
					
					
				}
				UtilidadesHash.set(miHashSOJ,ScsSOJBean.C_IDPERSONAJG,sojBean.getIdPersonaJG());
				if (miHash.get("IDPERSONA")==null ||((String)miHash.get("IDPERSONA")).equals("")){
				 hashAux=sojAdm.existeTramitadorSOJ((String)miForm.getIdInstitucion(),(String)miForm.getSOJNumero(),(String)miForm.getSOJAnio(),(String)miForm.getSOJIdTipoSOJ());
				 miHash.put(ScsEJGBean.C_IDPERSONA,hashAux.get("IDPERSONA"));
				}	
			  }
               /**INC_05960_SIGA, se introduce el campo idtipoencalidad a cero ya que cuando se crea por primera
               vez el campo calidad por defecto sera "Demandado"**/
				miHash.put(ScsEJGBean.C_CALIDAD,"0");
				miHash.put(ScsEJGBean.C_IDTIPOENCALIDAD,0);
				miHash.put(ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
				
				// 1. Insertamos el EJG
				if (!ejgAdm.insert(miHash)) {
					throw new ClsExceptions ("Error al crear el EJG desde la designa");
				}
				
/**/	  if (miForm.getSOJIdTipoSOJ()!=null){//Sólo en el caso de un EJG dado de alta desde un SOJ
			
				// creamos la relacion entre el SOJ y el EJG EN EL SOJ.
				miHashSOJ.put(ScsSOJBean.C_EJGIDTIPOEJG, (String)miHash.get(ScsEJGBean.C_IDTIPOEJG));
				miHashSOJ.put(ScsSOJBean.C_EJGANIO, (String)miHash.get(ScsEJGBean.C_ANIO));
				miHashSOJ.put(ScsSOJBean.C_EJGNUMERO, (String)miHash.get(ScsEJGBean.C_NUMERO));
				
				if (!sojAdm.updateDirect(miHashSOJ,new String[] {ScsSOJBean.C_IDINSTITUCION,ScsSOJBean.C_IDTIPOSOJ,ScsSOJBean.C_ANIO,ScsSOJBean.C_NUMERO},new String[] {ScsSOJBean.C_EJGIDTIPOEJG,ScsSOJBean.C_EJGANIO,ScsSOJBean.C_EJGNUMERO})) {
					throw new ClsExceptions("Error al actualizar la relacion en tre EJG y SOJ. "+sojAdm.getError());
				}
	
				if(sojBean.getIdPersonaJG()!=null){// Siempre que en el SOJ se haya dado de alta un beneficiario
			  	  copiarBeneficiarioSOJEnEJG(miHashSOJ,miHash,request);
			  	}
//				 Si el check copiar Documentacion está Activo copiamos la documentacion de SOJ a EJG
				boolean checkDocuSOJ=UtilidadesString.stringToBoolean(miForm.getChkDocumentacion());
				if (checkDocuSOJ){
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_ANIO, miForm.getSOJAnio());
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_NUMERO, miForm.getSOJNumero());
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ,miForm.getSOJIdTipoSOJ());
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					ScsDocumentacionSOJAdm sojDocuAdm = new ScsDocumentacionSOJAdm (this.getUserBean(request));
					ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm (this.getUserBean(request));
					Vector vSOJDocu = sojDocuAdm.select(miHashSOJDocu);
					ScsDocumentacionSOJBean sojDocuBean=null;
						if (vSOJDocu != null) {
						 for (int i=0; i<vSOJDocu.size();i++){
						 	sojDocuBean = (ScsDocumentacionSOJBean) vSOJDocu.get(i);
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_ANIO,miHash.get(ScsEJGBean.C_ANIO));
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_NUMERO,miHash.get(ScsEJGBean.C_NUMERO));
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_IDTIPOEJG,miHash.get(ScsEJGBean.C_IDTIPOEJG));
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_IDINSTITUCION,miForm.getIdInstitucion());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,sojDocuBean.getFechaEntrega());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHALIMITE,sojDocuBean.getFechaLimite());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_DOCUMENTACION,sojDocuBean.getDocumentacion());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_REGENTRADA,sojDocuBean.getRegEntrada());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_REGSALIDA,sojDocuBean.getRegSalida());
							miHashDocuEJG = ejgDocuAdm.prepararInsert(miHashDocuEJG);
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,sojDocuBean.getFechaEntrega());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHALIMITE,sojDocuBean.getFechaLimite());
							if (!ejgDocuAdm.insert(miHashDocuEJG)) {
								throw new ClsExceptions ("Error al crear registro de documentacion en EJG desde un SOJ");
							}
						 }
						}
			  }
			 } 	
			}
			else {
				
				Integer EJG_anio		  = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_ANIO);
				Integer EJG_idInstitucion = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_IDINSTITUCION);
				Integer EJG_idTipoEJG 	  = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_IDTIPOEJG);
				Integer EJG_numero		  = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_NUMERO);
				

				
				//  Desde Designacion
				if (miForm.getDesignaAnio()!=null && !miForm.getDesignaAnio().equals("")){ 

					// 1. Obtenemos todos los datos de la designa
					ScsDesignaBean designaBean = null;
					Hashtable p = new Hashtable(); 
					UtilidadesHash.set(p, ScsDesignaBean.C_ANIO,    		miForm.getDesignaAnio());
					UtilidadesHash.set(p, ScsDesignaBean.C_IDINSTITUCION, 	this.getIDInstitucion(request));
					UtilidadesHash.set(p, ScsDesignaBean.C_IDTURNO,  		miForm.getDesignaIdTurno());
					UtilidadesHash.set(p, ScsDesignaBean.C_NUMERO,     		miForm.getDesignaNumero());
					ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
					Vector vD = designaAdm.select(p);
					if ((vD != null) && (vD.size() == 1)) {
						designaBean = (ScsDesignaBean) vD.get(0);
					}

					// 2. Insertamos el EJG a partir de la Designa 
					
					// jbd // esto ya no se saca de la designa, hay que ir a la tabla scs_designaprocurador 
					// UtilidadesHash.set(miHash, ScsEJGBean.C_IDPROCURADOR,    			designaBean.getIdProcurador());
					// UtilidadesHash.set(miHash, ScsEJGBean.C_IDINSTITUCIONPROCURADOR,    designaBean.getIdInstitucionProcurador());
					ScsDesignasProcuradorAdm admProcurador = new ScsDesignasProcuradorAdm(this.getUserBean(request));
					Vector vP = admProcurador.select(p); // Aprovechamos la hash anterior que ya tiene los datos de la designa
					ScsDesignasProcuradorBean beanProcurador;
					if ((vP != null) && (vP.size() > 0)) {
						for (int i = 0; i < vP.size(); i++) {
							beanProcurador = (ScsDesignasProcuradorBean) vP.get(i);
							if((beanProcurador.getFechaRenuncia()==null)||(beanProcurador.getFechaRenuncia().equalsIgnoreCase(""))){
								UtilidadesHash.set(miHash, ScsEJGBean.C_IDINSTITUCIONPROCURADOR,    beanProcurador.getIdInstitucionProc());
								UtilidadesHash.set(miHash, ScsEJGBean.C_IDPROCURADOR,    			beanProcurador.getIdProcurador());
								UtilidadesHash.set(miHash, ScsEJGBean.C_NUMERODESIGNAPROC,    		beanProcurador.getNumeroDesignacion());
								UtilidadesHash.set(miHash, ScsEJGBean.C_FECHADESIGPROC,    			beanProcurador.getFechaDesigna());
							}
						}
					}
					UtilidadesHash.set(miHash, ScsEJGBean.C_IDPERSONAJG,     			miForm.getIdPersonaJG());
					
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADO,     				designaBean.getIdJuzgado());
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADOIDINSTITUCION,     	designaBean.getIdInstitucionJuzgado());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NUMEROPROCEDIMIENTO,     	designaBean.getNumProcedimiento());
															
					//obtengo la calidad del interesado (Del EJG) mediante la clave de designa y idPersonaJG en defendidos designa
					Hashtable bus = new Hashtable();
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_ANIO,    			designaBean.getAnio());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_IDTURNO, 			designaBean.getIdTurno());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_NUMERO,  			designaBean.getNumero());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_IDINSTITUCION,  			designaBean.getIdInstitucion());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_IDPERSONA,     			miForm.getIdPersonaJG());
					ScsDefendidosDesignaBean defBean = new ScsDefendidosDesignaBean ();
					ScsDefendidosDesignaAdm defAdm = new ScsDefendidosDesignaAdm (this.getUserBean(request));
					Vector vDe = defAdm.select(bus);
					if ((vDe != null) && (vDe.size() == 1)) {
						defBean = (ScsDefendidosDesignaBean) vDe.get(0);
					}
					  /**INC_05960_SIGA, se introduce el campo idtipoencalidad a cero ya que cuando se crea por primera
                      vez el campo calidad por defecto sera "Demandado"**/
					miHash.put(ScsEJGBean.C_CALIDAD,"0");
				    miHash.put(ScsEJGBean.C_IDTIPOENCALIDAD,0);					
				    miHash.put(ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
					
					if (!ejgAdm.insert(miHash)) {
						throw new ClsExceptions ("Error al crear el EJG desde la designa");
					}
					 //Insertamos tabla relacion EJG con designas
					ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
					
					ejgDesignabean.setIdInstitucion(this.getIDInstitucion(request));
					ejgDesignabean.setAnioDesigna(designaBean.getAnio());
					ejgDesignabean.setIdTurno(designaBean.getIdTurno());
					ejgDesignabean.setNumeroDesigna(new Integer(designaBean.getNumero().intValue()));
					ejgDesignabean.setAnioEJG(EJG_anio);
					ejgDesignabean.setIdTipoEJG(EJG_idTipoEJG);
					ejgDesignabean.setNumeroEJG(EJG_numero);
					ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
					if(!ejgDesignaAdm.insert(ejgDesignabean))
						throw new ClsExceptions ("Error al crear el EJG desde la designa");

					
					//como hemos creado un EJG desde una Designación, si esa Designación ha sido creada desde una asistencia
					// creamos automaticamente la relación entre la asistencia y el ejg
					ScsDesignaAdm desigAdm =new ScsDesignaAdm(this.getUserBean(request));
					Hashtable hashDesAsig = desigAdm.procedeDeAsistencia(designaBean.getIdTurno().toString(),designaBean.getNumero().toString(),designaBean.getAnio().toString());
					if (hashDesAsig.get("ASIANIO")!=null && !((String)hashDesAsig.get("ASIANIO")).equals("")){
						//esta relacionado con una asistencia
						// 4. Relacionar designa con la Asistencia siempre que la asistencia no tenga ninguna otra Designacion relacionada
						
						// Obtenemos la asistencia
						Hashtable datos = new Hashtable();
						ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
						UtilidadesHash.set(datos, ScsAsistenciasBean.C_ANIO,  hashDesAsig.get("ASIANIO").toString());
						UtilidadesHash.set(datos, ScsAsistenciasBean.C_IDINSTITUCION, this.getUserBean(request).getLocation());
						UtilidadesHash.set(datos, ScsAsistenciasBean.C_NUMERO, hashDesAsig.get("ASINUMERO").toString());

						
						ScsAsistenciasBean asistenciaBean = (ScsAsistenciasBean)((Vector)asistenciaAdm.selectByPK(datos)).get(0);
						if (asistenciaBean.getEjgAnio()==null || asistenciaBean.getEjgAnio().equals("")){
							asistenciaBean.setEjgAnio(EJG_anio);
							asistenciaBean.setEjgNumero(new Long (EJG_numero.intValue()));
							asistenciaBean.setEjgIdTipoEjg(EJG_idTipoEJG);
							
							if (!asistenciaAdm.update(asistenciaBean)) {
								throw new ClsExceptions ("Error al crear la relación automática entre Asistencia y el EJG");
							}
						}	
					}
					// 3. Insertamos en ContrariosDesigna los contrario del EJG  
					ScsContrariosDesignaAdm contrariosDesignaAdm = new ScsContrariosDesignaAdm (this.getUserBean(request));
					p.clear(); 
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_ANIO, 			designaBean.getAnio());
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_IDINSTITUCION, designaBean.getIdInstitucion());
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_IDTURNO, 		designaBean.getIdTurno());
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_NUMERO, 		designaBean.getNumero());
					Vector contrarios = contrariosDesignaAdm.select(p);

					ScsContrariosEJGAdm contrariosEJGAdm = new ScsContrariosEJGAdm (this.getUserBean(request)); 
					for (int i = 0; (contrarios != null) &&  (i < contrarios.size()); i++) {
						ScsContrariosDesignaBean contrariosDesignaBean = (ScsContrariosDesignaBean) contrarios.get(i); 
						ScsContrariosEJGBean contrariosEJGBean = new ScsContrariosEJGBean();
						contrariosEJGBean.setAnio(EJG_anio);
						contrariosEJGBean.setIdInstitucion(EJG_idInstitucion);
						contrariosEJGBean.setIdTipoEJG(EJG_idTipoEJG);
						contrariosEJGBean.setNumero(EJG_numero);
						contrariosEJGBean.setIdPersona(contrariosDesignaBean.getIdPersona());
						contrariosEJGBean.setObservaciones(contrariosDesignaBean.getObservaciones());
						if(contrariosDesignaBean.getIdProcurador()!=null)
							contrariosEJGBean.setIdProcurador(Long.valueOf(contrariosDesignaBean.getIdProcurador()));
						contrariosEJGBean.setIdInstitucionProcurador(contrariosDesignaBean.getIdInstitucionProcurador());
						// TODO // jbd // Con el representante tenemos un problema gordo, en EJG y asistencias es una personaJG y en designas es un colegiado
						// contrariosEJGBean.setIdRepresentanteEjg(contrariosDesignaBean.getIdRepresentanteLegal());
						// contrariosEJGBean.setNombreRepresentanteEjg(contrariosDesignaBean.getNombreRepresentante());
						contrariosEJGBean.setIdAbogadoContrarioEjg(contrariosDesignaBean.getIdAbogadoContrario());
						contrariosEJGBean.setNombreAbogadoContrarioEjg(contrariosDesignaBean.getnombreAbogadoContrario());
						
						if (!contrariosEJGAdm.insert(contrariosEJGBean))
							throw new ClsExceptions ("Error al crear el EJG desde la designa");
					}
					
					// 4. Insertamos en delitosEJG los delitos de la designa 
					ScsDelitosDesignaAdm delitosDesignaAdm = new ScsDelitosDesignaAdm (this.getUserBean(request));
					Hashtable aux = new Hashtable();
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_ANIO, 			designaBean.getAnio());
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_IDINSTITUCION, 	designaBean.getIdInstitucion());
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_IDTURNO, 		designaBean.getIdTurno());
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_NUMERO, 		designaBean.getNumero());
					Vector delitos = delitosDesignaAdm.select(aux);
					
					ScsDelitosEJGAdm delitosEJGAdm = new ScsDelitosEJGAdm (this.getUserBean(request));
					for (int i = 0; (delitos != null) &&  (i < delitos.size()); i++) {
						ScsDelitosDesignaBean delitosDesginaBean = (ScsDelitosDesignaBean) delitos.get(i); 
						ScsDelitosEJGBean delitosEJBBean = new ScsDelitosEJGBean();
						delitosEJBBean.setAnio(EJG_anio);
						delitosEJBBean.setIdInstitucion(EJG_idInstitucion);
						delitosEJBBean.setIdTipoEJG(EJG_idTipoEJG);
						delitosEJBBean.setNumero(EJG_numero);
						delitosEJBBean.setIdDelito(delitosDesginaBean.getIdDelito());
						if (!delitosEJGAdm.insert(delitosEJBBean))
							throw new ClsExceptions ("Error al crear el EJG desde la designa");
					}
					//Metemos el check de solicitante='1' para el beneficiario del EJG seleccionado
					if ((miHash.containsKey("IDPERSONAJG")) && (!((String)miHash.get("IDPERSONAJG")).equals(""))){
						ScsUnidadFamiliarEJGAdm admUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
						Hashtable miHashUF=new Hashtable();
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,EJG_idInstitucion);
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,EJG_idTipoEJG);
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_ANIO,EJG_anio);
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_NUMERO,EJG_numero);
						miHashUF.put("SOLICITANTE","1");
						miHashUF.put("ENCALIDADDE","SOLICITANTE");
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,miHash.get("IDPERSONAJG"));
						
						admUnidad.insert(miHashUF);
					}
				}
				
				// Desde Asistencia
				else {
					// 1. Obtenemos todos los datos de la asistencia
					ScsAsistenciasBean asistenciaBean = null;
/**/				Hashtable hasClavesAsistencia = new Hashtable(); 
					Hashtable p = new Hashtable(); 
					UtilidadesHash.set(p, ScsAsistenciasBean.C_ANIO,    		miForm.getAsistenciaAnio());
					UtilidadesHash.set(p, ScsAsistenciasBean.C_IDINSTITUCION, 	this.getIDInstitucion(request));
					UtilidadesHash.set(p, ScsAsistenciasBean.C_NUMERO,     		miForm.getAsistenciaNumero());
					ScsAsistenciasAdm asisnteciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
					Vector vA = asisnteciaAdm.select(p);
					if ((vA != null) && (vA.size() == 1)) {
						asistenciaBean = (ScsAsistenciasBean) vA.get(0);
					}
					
					// 2. Insertamos el EJG con los datos de la asistencia 

					UtilidadesHash.set(miHash, ScsEJGBean.C_IDPERSONAJG,     	  asistenciaBean.getIdPersonaJG());
					UtilidadesHash.set(miHash, ScsEJGBean.C_DELITOS,     		  asistenciaBean.getDelitosImputados());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NUMEROPROCEDIMIENTO,  asistenciaBean.getNumeroProcedimiento());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NUMERODILIGENCIA,     asistenciaBean.getNumeroDiligencia());	
					UtilidadesHash.set(miHash, ScsEJGBean.C_COMISARIA,     		  asistenciaBean.getComisaria());
					UtilidadesHash.set(miHash, ScsEJGBean.C_COMISARIAIDINSTITUCION,asistenciaBean.getComisariaIdInstitucion());
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADOIDINSTITUCION, asistenciaBean.getJuzgadoIdInstitucion());
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADO,     		  asistenciaBean.getJuzgado());	
					
					/**INC_05960_SIGA, se introduce el campo idtipoencalidad a cero ya que cuando se crea por primera
                      vez el campo calidad por defecto sera "Demandado"**/
					miHash.put(ScsEJGBean.C_CALIDAD,"0");
				    miHash.put(ScsEJGBean.C_IDTIPOENCALIDAD,0);
					miHash.put(ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
					if (!ejgAdm.insert(miHash)) {
						throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
					}
					//MKetemos tambien al solicitante en la unidad familiar
					if ((miHash.containsKey("IDPERSONAJG")) && (!((String)miHash.get("IDPERSONAJG")).equals(""))){
						ScsUnidadFamiliarEJGAdm admUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
						miHash.put("SOLICITANTE","1");
						miHash.put("ENCALIDADDE","SOLICITANTE");
						miHash.put("IDPERSONA",miHash.get("IDPERSONAJG"));
						
						admUnidad.insert(miHash);
					}
					
					

					String[] Claves={ScsAsistenciasBean.C_IDINSTITUCION,ScsAsistenciasBean.C_ANIO,ScsAsistenciasBean.C_NUMERO};
					String[] Campos={ScsAsistenciasBean.C_EJGANIO,ScsAsistenciasBean.C_EJGNUMERO,ScsAsistenciasBean.C_EJGIDTIPOEJG};
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_ANIO,  	  asistenciaBean.getAnio());
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_NUMERO,	  asistenciaBean.getNumero());
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_IDINSTITUCION, 	this.getIDInstitucion(request));
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_EJGANIO,  	  		(String)miHash.get(ScsEJGBean.C_ANIO));
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_EJGNUMERO,	  		(String)miHash.get(ScsEJGBean.C_NUMERO));
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_EJGIDTIPOEJG, 		(String)miHash.get(ScsEJGBean.C_IDTIPOEJG));

/**/				ScsAsistenciasAdm asisAdm =  new ScsAsistenciasAdm(this.getUserBean(request));					
/**/				if (!asisAdm.updateDirect(hasClavesAsistencia,Claves,Campos)) {
/**/					throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
/**/				}

					// Si la asistencia desde la que hemos creado el EJG tiene relación con una Designación, establecemos
                    // automáticamente la relación entre el EJG creado y la Designación

					if (asistenciaBean.getDesignaAnio()!=null && !(asistenciaBean.getDesignaAnio().equals(""))){
						ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
						
						ejgDesignabean.setIdInstitucion(this.getIDInstitucion(request));
						ejgDesignabean.setAnioDesigna(asistenciaBean.getDesignaAnio());
						ejgDesignabean.setIdTurno(asistenciaBean.getDesignaTurno());
						ejgDesignabean.setNumeroDesigna(new Integer(asistenciaBean.getDesignaNumero().intValue()));
						ejgDesignabean.setAnioEJG(new Integer((String)miHash.get(ScsEJGBean.C_ANIO)));
						ejgDesignabean.setIdTipoEJG(new Integer((String)miHash.get(ScsEJGBean.C_IDTIPOEJG)));
						ejgDesignabean.setNumeroEJG(new Integer((String)miHash.get(ScsEJGBean.C_NUMERO)));
						
						Hashtable hashEjgDesigna=new Hashtable();
						ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
						
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIODESIGNA, 			asistenciaBean.getDesignaAnio());
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMERODESIGNA,  new Integer(asistenciaBean.getDesignaNumero().intValue()));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTURNO, 		asistenciaBean.getDesignaTurno());
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTIPOEJG, 		new Integer((String)miHash.get(ScsEJGBean.C_IDTIPOEJG)));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMEROEJG, 		new Integer((String)miHash.get(ScsEJGBean.C_NUMERO)));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIOEJG, 		new Integer((String)miHash.get(ScsEJGBean.C_ANIO)));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDINSTITUCION, 		this.getIDInstitucion(request));
						Vector existeRelacion = ejgDesignaAdm.select(hashEjgDesigna);
						
						if (existeRelacion.size()==0){//Si no existe la relación, la creamos
							if(!ejgDesignaAdm.insert(ejgDesignabean))
								throw new ClsExceptions ("Error al crear la relacion entre EJG y la designa");
						}
					}
					// 3. Insertamos en ContrariosAsistencia los contrario del EJG  
					ScsContrariosAsistenciaAdm contrariosAsistenciaAdm = new ScsContrariosAsistenciaAdm (this.getUserBean(request));
					p.clear(); 
					UtilidadesHash.set(p, ScsContrariosAsistenciaBean.C_ANIO, 			asistenciaBean.getAnio());
					UtilidadesHash.set(p, ScsContrariosAsistenciaBean.C_IDINSTITUCION,  asistenciaBean.getIdInstitucion());
					UtilidadesHash.set(p, ScsContrariosAsistenciaBean.C_NUMERO, 		asistenciaBean.getNumero());
					Vector contrarios = contrariosAsistenciaAdm.select(p);

					ScsContrariosEJGAdm contrariosEJGAdm = new ScsContrariosEJGAdm (this.getUserBean(request)); 
					for (int i = 0; (contrarios != null) &&  (i < contrarios.size()); i++) {
						ScsContrariosAsistenciaBean contrariosAsistenciaBean = (ScsContrariosAsistenciaBean) contrarios.get(i); 
						ScsContrariosEJGBean contrariosEJGBean = new ScsContrariosEJGBean();
						contrariosEJGBean.setAnio(EJG_anio);
						contrariosEJGBean.setIdInstitucion(EJG_idInstitucion);
						contrariosEJGBean.setIdTipoEJG(EJG_idTipoEJG);
						contrariosEJGBean.setNumero(EJG_numero);
						contrariosEJGBean.setIdPersona(new Integer(contrariosAsistenciaBean.getIdPersona().intValue()));
						contrariosEJGBean.setObservaciones(contrariosAsistenciaBean.getObservaciones());
						if (!contrariosEJGAdm.insert(contrariosEJGBean))
							throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
					}

					// 4. Insertamos en delitosEJG todos los delitos de la asistencia
					ScsDelitosAsistenciaAdm delitosAsistenciaAdm = new ScsDelitosAsistenciaAdm (this.getUserBean(request));
					Hashtable aux = new Hashtable();
					UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_ANIO, 			asistenciaBean.getAnio());
					UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_IDINSTITUCION, 	asistenciaBean.getIdInstitucion());
					UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_NUMERO, 			asistenciaBean.getNumero());
					Vector delitos = delitosAsistenciaAdm.select(aux);
					
					ScsDelitosEJGAdm delitosEJGAdm = new ScsDelitosEJGAdm (this.getUserBean(request));
					for (int i = 0; (delitos != null) &&  (i < delitos.size()); i++) {
						ScsDelitosAsistenciaBean delitosAsistenciaBean = (ScsDelitosAsistenciaBean) delitos.get(i); 
						ScsDelitosEJGBean delitosEJBBean = new ScsDelitosEJGBean();
						delitosEJBBean.setAnio(EJG_anio);
						delitosEJBBean.setIdInstitucion(EJG_idInstitucion);
						delitosEJBBean.setIdTipoEJG(EJG_idTipoEJG);
						delitosEJBBean.setNumero(EJG_numero);
						delitosEJBBean.setIdDelito(delitosAsistenciaBean.getIdDelito());
						if (!delitosEJGAdm.insert(delitosEJBBean))
							throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
					}
				}
			}
/**/	 if (miHash.get(ScsSOJBean.C_ANIO)==null){//Sólo en el caso de un EJG dado de alta desde un SOJ
			// 5. Si se inserta una idPersonaJG (en el caso de crearse desde una Asistencia o Designa) se inserta también en UnidadFamiliar
			if ((miHash.containsKey("IDPERSONAJG")) && (!((String)miHash.get("IDPERSONAJG")).equals(""))){
				ScsUnidadFamiliarEJGAdm admUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
				miHash.put("SOLICITANTE","1");
				miHash.put("ENCALIDADDE","SOLICITANTE");
				miHash.put("IDPERSONA",miHash.get("IDPERSONAJG"));
				
				admUnidad.insert(miHash);
			}
		 }	
			// 7. Saltos y compensaciones
			if (miHash.get(ScsEJGBean.C_TIPOLETRADO).toString().equalsIgnoreCase("P")) {
			
				String idsaltosturno = "";
				Vector registros = new Vector();	
	            //Consulta para ver si tiene compensaciones           
	            String where = " WHERE " + ScsSaltosCompensacionesBean.C_IDINSTITUCION + " = " + miHash.get(ScsEJGBean.C_IDINSTITUCION) +
							   " AND " + ScsSaltosCompensacionesBean.C_IDTURNO + " = " + miHash.get(ScsEJGBean.C_GUARDIATURNO_IDTURNO) +
							   " AND " + ScsSaltosCompensacionesBean.C_IDGUARDIA + " = " + miHash.get(ScsEJGBean.C_GUARDIATURNO_IDGUARDIA) +
							   " AND " + ScsSaltosCompensacionesBean.C_IDPERSONA + " = " + miHash.get(ScsEJGBean.C_IDPERSONA) +
							   " AND " + ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION + " = 'C'" +
							   " AND " + ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO + " IS NULL ";
	            
	            registros.clear();
				ScsSaltosCompensacionesAdm admSaltosCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
	            registros = admSaltosCompensaciones.selectForUpdate(where);                                                                                                                  //Si hay compensacion
	            if (registros.size() > 0) {
	                idsaltosturno = (((ScsSaltosCompensacionesBean)registros.get(0)).getIdSaltosTurno()).toString();	                
	                //Anoto la fecha de cumplimiento
	                hashTemporal.clear();
	                hashTemporal.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,miHash.get(ScsEJGBean.C_IDINSTITUCION));
	                hashTemporal.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,idsaltosturno);
	                hashTemporal.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,miHash.get(ScsEJGBean.C_FECHAAPERTURA));
	                String claves[] = {ScsSaltosCompensacionesBean.C_IDINSTITUCION, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO};
	                String campos[] = {ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO};
	                //Actualizo la fecha de cumplimiento                
	                admSaltosCompensaciones.updateDirect(hashTemporal,claves,campos);
	            }
			}
			
			
			///////////////////////////////////////////////////////////////////////////////////////////
			// RGG 21-03-2006 : Cambios debidos a la nueva asignacion de colegiados desde Busqueda SJCS
			// ----------------------------------------------------------------------------------------
			
			//-----------------------------------------------------
			// obtencion de valores a utilizar (MODIFICAR SEGUN ACTION)
			String idInstitucionSJCS=usr.getLocation();
			String idTurnoSJCS=miForm.getGuardiaTurnoIdTurno();
			String idGuardiaSJCS=miForm.getGuardiaTurnoIdGuardia();
			String anioSJCS=miForm.getAnio();
			String numeroSJCS=miForm.getNumero();
			String idPersonaSJCS=miForm.getIdPersona();
			String origenSJCS = "general.boton.crearEJG"; 
			//-----------------------------------------------------
			
			
			// Obtención parametros de la busqueda SJCS (FIJOS, NO TOCAR)
			String flagSalto = request.getParameter("flagSalto");
			String flagCompensacion = request.getParameter("flagCompensacion");
			String checkSalto = request.getParameter("checkSalto");
			//String checkCompensacion = request.getParameter("checkCompensacion");
			String motivoSaltoSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarSaltoPor") + " " +
			UtilidadesString.getMensajeIdioma(usr,origenSJCS);
			//String motivoCompensacionSJCS = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.insertarCompensacionPor") + " " +
			//UtilidadesString.getMensajeIdioma(usr,origenSJCS);
			
			// Aplicar cambios (COMENTAR LO QUE NO PROCEDA) Revisar que no se hace algo ya en el action. 
			BusquedaClientesFiltrosAdm admFiltros = new BusquedaClientesFiltrosAdm(this.getUserBean(request)); 
			// Primero: Actualiza si ha sido automático o manual (Designaciones)0
			//admFiltros.actualizaManualDesigna(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,anioSJCS, numeroSJCS, flagSalto,flagCompensacion);
			// Segundo: Tratamiento de último (Designaciones)
			//admFiltros.tratamientoUltimo(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,flagSalto,flagCompensacion);
			// Tercero: Generación de salto (Designaciones y asistencias)
			admFiltros.crearSalto(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkSalto, motivoSaltoSJCS);
			// Cuarto: Generación de compensación (Designaciones NO ALTAS)
			//admFiltros.crearCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkCompensacion,motivoCompensacionSJCS);
			///////////////////////////////////////////////////////////////////////////////////////////
			
			
			tx.commit();
			

		// Añadimos parametros para las pestanhas
//			request.getSession().setAttribute("idEJG", miHash);
			session.setAttribute("accion","editar");
			//request.getSession().setAttribute("modo", "editar");
			request.setAttribute("NUMERO",miHash.get(ScsEJGBean.C_NUMERO));
			request.setAttribute("TIPO",miHash.get(ScsEJGBean.C_IDTIPOEJG));
			request.setAttribute("INSTITUCION",miHash.get(ScsEJGBean.C_IDINSTITUCION));
			request.setAttribute("ANIO",miHash.get(ScsEJGBean.C_ANIO));
			
			
//			{
//				miForm.setIdTipoEJG((String)miHash.get(ScsEJGBean.C_IDTIPOEJG));
//				miForm.setIdInstitucion((String)miHash.get(ScsEJGBean.C_IDINSTITUCION));
//				miForm.setAnio((String)miHash.get(ScsEJGBean.C_ANIO));
//				miForm.setNumero((String)miHash.get(ScsEJGBean.C_NUMERO));
//				return this.editar(mapping, miForm, request, response);
//			}
			String origen = miForm.getOrigen();
			if(origen!=null && origen.equals("A")&& miForm.getAsistenciaAnio()!=null && !miForm.getAsistenciaAnio().equals("")){
				return super.exitoModal("messages.inserted.success", request);
			}

//			if ((miForm.getde()!=null)&&(miForm.getDesdeAsistencia().equalsIgnoreCase("si")))
//				request.getSession().setAttribute("asistencia",(String)nuevaDesigna.get("NUMERO"));
//			else{
//				if((miForm.getDesdeEjg()!=null)&&(miForm.getDesdeEjg().equalsIgnoreCase("si")))
//					request.getSession().setAttribute("ejg",(String)nuevaDesigna.get("NUMERO"));
//			}
		} 
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.inserted.success",request);
	}

	protected String exitoModal(String mensaje, HttpServletRequest request) 
	{
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("modal","");
		return "exitoEJG"; 
	}
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
		
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsEJGBean.C_IDTIPOEJG,(ocultos.get(0)));
			miHash.put(ScsEJGBean.C_IDINSTITUCION,(ocultos.get(1)));						
			miHash.put(ScsEJGBean.C_ANIO,(ocultos.get(2)));
			miHash.put(ScsEJGBean.C_NUMERO,(ocultos.get(3)));										
			
			tx=usr.getTransaction();
			tx.begin();
			admBean.delete(miHash);		    
			tx.commit();
			
		} catch (Exception e) {
			throwExcp("messages.general.error",e,tx);
		}		

		request.setAttribute("hiddenFrame", "1");
      
		return exitoRefresco("messages.deleted.success",request);
	}

	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		/* Borramos de la sesion las variables que pueden haberse utilizado, para así evitar posibles problemas con datos erróneos contenidos ahí */		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		request.getSession().removeAttribute("resultadoTelefonos");
		try {
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			String eejg = paramAdm.getValor (usr.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_EEJG, "");
			Boolean isPermisoEejg = new Boolean((eejg!=null && eejg.equalsIgnoreCase(ClsConstants.DB_TRUE)));
			request.setAttribute("permisoEejg", isPermisoEejg);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		
		return "inicio";		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			CenPersonaAdm persona = new CenPersonaAdm(this.getUserBean(request));
			
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				String nombreColegiado = persona.obtenerNombreApellidos(new Long(usr.getIdPersona()).toString());
				request.setAttribute("nColegiado",numeroColegiado);
				request.setAttribute("nombreColegiado",nombreColegiado);
				
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		/* "DATABACKUP" y "DATOSFORMULARIO" se usan habitualmente así que en primero lugar las borramos esta*/
		request.getSession().removeAttribute("DATABACKUP");
        request.getSession().removeAttribute("DATOSFORMULARIO");
        request.getSession().removeAttribute("DATABACKUP");
        request.getSession().removeAttribute("accion");
        try {
        	 GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
     		String eejg = paramAdm.getValor (usr.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_EEJG, "");
     		Boolean isPermisoEejg = new Boolean((eejg!=null && eejg.equalsIgnoreCase(ClsConstants.DB_TRUE)));
     		request.setAttribute("permisoEejg", isPermisoEejg);
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
       
        
		
        
        return "inicio";           
    } 
	
	
	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String generarCarta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
	    
		try{
			DefinirEJGForm miForm =(DefinirEJGForm)formulario;
			
			// Guardo el formulario en sesión para poder fijar a quienes hay que enviar la carta
			request.getSession().setAttribute("DATABACKUP", miForm.getDatos());
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
        return "recogidaDatos";
	}
	
	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String finalizarCarta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
	    
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma = usr.getLanguage().toUpperCase();

		String resultado="recogidaDatos";
		
		Vector vResultado= new Vector();
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
			
		try {
			//obtener plantilla
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");			
		    String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoCartaEJGJava")+rp.returnProperty("sjcs.directorioCartaEJGJava"))+ClsConstants.FILE_SEP+institucion;
		    
		    // RGG cambio de codigos 
		    String lenguajeExt ="es";
		    AdmLenguajesAdm al = new AdmLenguajesAdm(this.getUserBean(request));
		    lenguajeExt=al.getLenguajeExt(idioma);
		    
		    String nombrePlantilla="plantillaCartaEJG_"+lenguajeExt+".fo";
		    
		    
		    InformeDefinirEJG informe = new InformeDefinirEJG();
			String contenidoPlantilla = informe.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
			
		    //obtener la ruta de descarga
		    String rutaServidor =
		    	Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava"))+
		    	ClsConstants.FILE_SEP+institucion;
			rutaFin=new File(rutaServidor);
			if (!rutaFin.exists()){
				if(!rutaFin.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}    
			String rutaServidorTmp=rutaServidor+ClsConstants.FILE_SEP+"tmp_ejg_"+System.currentTimeMillis();
			rutaTmp=new File(rutaServidorTmp);
			if(!rutaTmp.mkdirs()){
				throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
			}
			
		    //obtener los datos comunes
		    Hashtable datosComunes= this.obtenerDatosComunes(request);
			
			//buscar los registros seleccionados
			Hashtable miHash= (Hashtable) request.getSession().getAttribute("DATABACKUP");
		
			
			ScsEJGAdm admEJG =  new ScsEJGAdm(this.getUserBean(request));
			vResultado = admEJG.getDatosCartaEJG(miHash);
			
			if (vResultado!=null && !vResultado.isEmpty()){
				boolean correcto=true;
				Enumeration listaEJGs=vResultado.elements();
	    		
				while(correcto && listaEJGs.hasMoreElements()){
					Hashtable datosBaseEJG=(Hashtable)listaEJGs.nextElement();
					datosBaseEJG.putAll(datosComunes);
					File fPdf = informe.generarInforme(request,datosBaseEJG,rutaServidorTmp,contenidoPlantilla,rutaServidorTmp,"cartasEJG_" +numeroCarta);
					correcto=(fPdf!=null);
					if(correcto){
						ficherosPDF.add(fPdf);
						numeroCarta++;
					}
				}
				
				if(correcto){
					// Ubicacion de la carpeta donde se crean los ficheros PDF:
					String nombreFicheroPDF="cartasEJG_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip=rutaServidor + File.separator;
					
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroPDF,ficherosPDF);
					request.setAttribute("nombreFichero", nombreFicheroPDF + ".zip");
					request.setAttribute("rutaFichero", rutaServidorDescargasZip+nombreFicheroPDF + ".zip");			
					request.setAttribute("borrarFichero", "true");			
					
					//resultado = "descargaFichero";				
					request.setAttribute("generacionOK","OK");			
					resultado = "recogidaDatos";
				}else{
					request.setAttribute("generacionOK","ERROR");			
					resultado = "recogidaDatos";
				}
				
			}else{
				resultado=exitoModalSinRefresco("gratuita.retenciones.noResultados",request);
			}
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		} finally{ 
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return resultado;
	}
	
	/**
	 * 
	 */
    protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
    {
        DefinirEJGForm miForm =(DefinirEJGForm)formulario;
		request.setAttribute("nombreFichero", miForm.getFicheroDownload());
		request.setAttribute("rutaFichero", miForm.getRutaFicheroDownload());			
		request.setAttribute("borrarFichero", miForm.getBorrarFicheroDownload());			
        return "descargaFichero";
    }
    
	/**
	 * Este método reemplaza los valores comunes en las plantillas FO
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected Hashtable obtenerDatosComunes(HttpServletRequest request) throws ClsExceptions{
		DefinirEJGForm miForm = (DefinirEJGForm) request.getAttribute("DefinirEJGForm");
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma = usr.getLanguage();
		
		Hashtable datos= new Hashtable();
		UtilidadesHash.set(datos,"CABECERA_CARTA_EJG",miForm.getCabeceraCarta());
		UtilidadesHash.set(datos,"MOTIVO_CARTA_EJG",miForm.getMotivoCarta());
		UtilidadesHash.set(datos,"PIE_CARTA_EJG",miForm.getPieCarta());
		UtilidadesHash.set(datos,"FECHA",UtilidadesBDAdm.getFechaBD(""));
		UtilidadesHash.set(datos,"TEXTO_TRATAMIENTO_DESTINATARIO",UtilidadesString.getMensajeIdioma(idioma,"informes.cartaAsistencia.estimado"));
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");			
	    String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoCartaEJGJava")+rp.returnProperty("sjcs.directorioCartaEJGJava"))+ClsConstants.FILE_SEP+institucion;
		UtilidadesHash.set(datos,"RUTA_LOGO",rutaPlantilla+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"logo.gif");

		return datos;
	 }
	protected void copiarBeneficiarioSOJEnEJG (Hashtable hdatosSOJ,Hashtable hdatosEJG,HttpServletRequest request) throws SIGAException 
	{
		String result = "";
		
		Hashtable hIdPersonaJG=new Hashtable();
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     

	     	// clave del EJG
			
			boolean nuevaRel = false; 
	     	// Datos Persona
			Hashtable persona= new Hashtable();
			Hashtable beneficiari= new Hashtable();
			ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request));
			ScsBeneficiarioSOJAdm beneficiarioAdm=new ScsBeneficiarioSOJAdm(this.getUserBean(request));
			
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDINSTITUCION,user.getLocation());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPERSONA,hdatosSOJ.get(ScsSOJBean.C_IDPERSONAJG).toString());
			// Obtenemos toda la informacion del Beneficiario que se encuentra en la tabla scs_personajg
			Vector vPersonaJG = perAdm.selectByPK(persona);
			ScsPersonaJGBean personaJGBean=null;
			if ((vPersonaJG != null) && (vPersonaJG.size() == 1)) {
				personaJGBean = (ScsPersonaJGBean) vPersonaJG.get(0);
			}
			
			Vector vBeneficiarioSOJ = beneficiarioAdm.selectByPK(persona);
			ScsBeneficiarioSOJBean beneficiarioSOJBean=null;
			if ((vBeneficiarioSOJ != null) && (vBeneficiarioSOJ.size() == 1)) {
				beneficiarioSOJBean = (ScsBeneficiarioSOJBean) vBeneficiarioSOJ.get(0);
			}
			
			// OJO, utilizo los set, porque siempre tengo datos, aunque sean blancos, que es lo que me interesa
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_NIF,personaJGBean.getNif().toString());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_NOMBRE,personaJGBean.getNombre());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_APELLIDO1,personaJGBean.getApellido1());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_APELLIDO2,personaJGBean.getApellido2());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_DIRECCION,personaJGBean.getDireccion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_CODIGOPOSTAL,personaJGBean.getCodigoPostal());						
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate("",personaJGBean.getFechaNacimiento()));			
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROFESION,personaJGBean.getIdProfesion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPAIS,personaJGBean.getIdPais());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROVINCIA,personaJGBean.getIdProvincia());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPOBLACION,personaJGBean.getIdPoblacion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_ESTADOCIVIL,personaJGBean.getIdEstadoCivil());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_REGIMENCONYUGAL,personaJGBean.getRegimenConyugal());			 
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_TIPOPERSONAJG,personaJGBean.getTipo());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,personaJGBean.getTipoIdentificacion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_ENCALIDADDE,personaJGBean.getEnCalidadDe());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_OBSERVACIONES,personaJGBean.getObservaciones());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDREPRESENTANTEJG,personaJGBean.getIdRepresentanteJG());
			

			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDINSTITUCION,user.getLocation());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPERSONA,hdatosSOJ.get(ScsSOJBean.C_IDPERSONAJG).toString());
			
			// Obtenemos toda la informacion del Beneficiario que se encuentra en la tabla scs_personajg
			

			ScsUnidadFamiliarEJGAdm unidadFamiliarAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			
			// ATENCION!! SE IGNORA LA TABLA SCS_CONTRARIOS_EJG

			// CREAR SCS_UNIDADFAMILAREJG
			Hashtable unidadFamiliarBean = new Hashtable();
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,hdatosEJG.get(ScsEJGBean.C_IDINSTITUCION).toString());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_ANIO,hdatosEJG.get(ScsEJGBean.C_ANIO).toString());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_NUMERO,hdatosEJG.get(ScsEJGBean.C_NUMERO).toString());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,hdatosEJG.get(ScsEJGBean.C_IDTIPOEJG).toString());				
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDPERSONA,personaJGBean.getIdPersona());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,UtilidadesHash.getString(hdatosEJG,ScsEJGBean.C_CALIDAD));
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,"1");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_OTROSBIENES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,"");						 
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,"");	
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB,beneficiarioSOJBean.getIdTipoGrupoLab());
			
			
	     	
			
			
			
			// RELACIONARLO CON EL EJG (UPDATE NORMAL)
			// Si el concepto es EJG solamente
//			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
//			String idPersonaAnterior = "";
//			Hashtable ht = new Hashtable();
//			ht.put(ScsEJGBean.C_IDINSTITUCION,hdatosEJG.get(ScsEJGBean.C_IDINSTITUCION).toString());
//			ht.put(ScsEJGBean.C_IDTIPOEJG,hdatosEJG.get(ScsEJGBean.C_IDTIPOEJG).toString());
//			ht.put(ScsEJGBean.C_ANIO,hdatosEJG.get(ScsEJGBean.C_ANIO).toString());
//			ht.put(ScsEJGBean.C_NUMERO,hdatosEJG.get(ScsEJGBean.C_NUMERO).toString());
//			Vector v = admEJG.selectByPK(ht);
//			if (v!=null && v.size()>0) {
//				nuevaRel = true; 
//				ScsEJGBean beanEJG = (ScsEJGBean) v.get(0);
//				if (miform.getConceptoE().equals(PersonaJGAction.EJG)) {
//					// compruebo si ha cambiado el id persona para la relacion
//					if (beanEJG.getIdPersonaJG()!=null && !beanEJG.getIdPersonaJG().equals(new Integer(miform.getIdPersonaJG()))) {
//						// guardo el idpersona anterior para buscar las relaciones y actualizarlas
//						idPersonaAnterior = beanEJG.getIdPersonaJG().toString();
//					} else {
//						// si no el que borrare sera el mismo, el actual
//						idPersonaAnterior = miform.getIdPersonaJG();
//					}
//				} else 
//				if (miform.getConceptoE().equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
//					// en este caso lo cojo del databackup de unidad familiar, si esxiste
//					Hashtable oldUF = (Hashtable) dataBackup.get(ScsUnidadFamiliarEJGBean.T_NOMBRETABLA);
//					if (oldUF!=null) {
//						idPersonaAnterior = (String) oldUF.get(ScsUnidadFamiliarEJGBean.C_IDPERSONA);
//					} else {
//						idPersonaAnterior = null;
//					}
//				}
//				// RGG solamente para el interesado
//				if (miform.getConceptoE().equals(PersonaJGAction.EJG)) {
//					// actualizo la personaJG, que sera el interesado en este caso. 
//					beanEJG.setIdPersonaJG(new Integer(miform.getIdPersonaJG()));
//					if (!admEJG.updateDirect(beanEJG)) {
//						throw new ClsExceptions("Error en updateEJG. " + admEJG.getError());
//					}
//				}
//			}
//			
			// INSERTAR O ACTUALIZAR UNIDAD FAMILIAR EJG (RELACIONADO)
			ScsUnidadFamiliarEJGAdm ufAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			// Hay que actualizarla. La borro con la clave adecuada
			// anterior para el caso de que haya cambiado
			
				Hashtable borrar = new Hashtable();
				borrar.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,hdatosEJG.get(ScsEJGBean.C_IDINSTITUCION).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,hdatosEJG.get(ScsEJGBean.C_IDTIPOEJG).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_ANIO,hdatosEJG.get(ScsEJGBean.C_ANIO).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_NUMERO,hdatosEJG.get(ScsEJGBean.C_NUMERO).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,personaJGBean.getIdPersona());
				ufAdm.delete(borrar);
					
			
			// Insert unidad familiar con los nuevos valores
			if (!ufAdm.insert(unidadFamiliarBean)) {
				throw new ClsExceptions("Error en insert unidad familiar. " + ufAdm.getError());
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
	}
}