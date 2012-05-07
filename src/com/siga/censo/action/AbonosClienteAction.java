/*
 * VERSIONES:
 * 
 * miguel.villegas - 21-12-2005 - Creacion
 *	
 */

/**
 * Clase action para el tratamiento de abonos en la ficha del cliente.<br/>
 * Gestiona la consulta de los abonos asociados a un cliente  
 */

package com.siga.censo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.InformeForm;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.censo.form.AbonosClienteForm;
import com.siga.censo.form.DatosFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformePersonalizable;
import com.siga.informes.form.MantenimientoInformesForm;


public class AbonosClienteAction extends MasterAction {

	
	
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
					borrarPaginador(request, paginadorPenstania);
					mapDestino = abrirAbonosPaginados(mapping, miForm, request, response);
					break;
				}
				else if (accion.equalsIgnoreCase("abrirAbonosPaginados")){
					mapDestino = abrirAbonosPaginados(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("imprimir")){
					mapDestino = imprimir(mapping, miForm, request, response);
					break;
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
	
	
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		String numero = "";
		String nombre = "";
		Vector abonos = new Vector();

		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String accion = (String)request.getParameter("accion");
			
			// RGG 02-02-2005 Para controlar si estamos en alta
			if (accion.equals("nuevo") || (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals(""))) {
				return "clienteNoExiste";
			}
			
			String idInstitucion=user.getLocation();

			// Obtengo el identificador de persona, la accion y el identificador de institucion del cliente
			Long idPersona = new Long(request.getParameter("idPersona"));
			String idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();
			
			
			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			

			// Obtengo el numero de colegiado en caso de que este colegiado
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
//			if(!clienteAdm.esColegiado(idPersona,idInst).isEmpty()){
//				numero = clienteAdm.obtenerNumero(idPersona,idInst);				
//			}	
			// DCG Nuevo ////////////////////
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona, new Integer(idInstitucionPersona));
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			//////////////////////
			
			// Obtengo la informacion relacionada con los abonos
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			abonos=abonoAdm.getAbonosCliente(idInstitucionPersona,idPersona.toString());
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);
			request.setAttribute("ACCION", accion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
			request.setAttribute("container", abonos);
			String informeUnico = ClsConstants.DB_TRUE;
			if(abonos!=null && abonos.size()>0){
				AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
				// mostramos la ventana con la pregunta
				
				Vector informeBeans=adm.obtenerInformesTipo(idInstitucion,"CPAGO",null, null);
				if(informeBeans!=null && informeBeans.size()>1){
					informeUnico = ClsConstants.DB_FALSE;
					
				}
				
				
			}
			request.setAttribute("informeUnico", informeUnico);
			
			
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}				
		return result;
	}
	
	
	/** 
	 * Funcion que atiende la accion abrirPaginaAbonos.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirAbonosPaginados (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{

		request.setAttribute(ClsConstants.PARAM_PAGINACION, paginadorPenstania);
		AbonosClienteForm miform = (AbonosClienteForm) formulario;

		
		String numero = "";
		String nombre = "";
		Vector abonos = new Vector();
		
					
		try{
			// Obtengo el UserBean y el identificador de la institucion
			Integer anyosAbono=null;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String accion = (String)request.getParameter("accion");
			
			if(accion == null){
				
				accion = (String)request.getAttribute("accion");
				
			}
			
			// RGG 02-02-2005 Para controlar si estamos en alta
			if ("nuevo".equals(accion) || (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals(""))) {
				return "clienteNoExiste";
			}
			
			String idInstitucion=user.getLocation();

			// Obtengo el identificador de persona, la accion y el identificador de institucion del cliente
			String b = miform.getIncluirRegistrosConBajaLogica();
			if (b == null){
				b = request.getParameter("bIncluirRegistrosConBajaLogica");
			}
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(b);
			Long idPersona = new Long(request.getParameter("idPersona"));
			String idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();
			request.getSession().setAttribute("IDPERSONA",idPersona);
			request.getSession().setAttribute("IDINSTITUCION",idInstitucion);
			
			
						
			idPersona = (Long) request.getAttribute("idPersona");
			if (idPersona == null){
				idPersona = (Long) request.getSession().getAttribute("idPersona");
				if (idPersona == null){
					idPersona = new Long (request.getParameter("idPersona"));
				}
			}
			
			idInstitucion = (String) request.getAttribute("idInstitucion");
			if(idInstitucion == null){
				idInstitucion = (String) request.getSession().getAttribute("idInstitucion");
				if(idInstitucion == null){
					idInstitucion = new String(request.getParameter("idInstitucion"));	
				
				}
			
			}
			
			idInstitucionPersona = (String) request.getAttribute("idInstitucion");
			if(idInstitucionPersona == null){
				idInstitucionPersona = (String) request.getSession().getAttribute("idInstitucion");
				if(idInstitucionPersona == null){
					idInstitucionPersona = new String(request.getParameter("idInstitucion"));	
				
				}
			
			}
			
			
			if(!bIncluirRegistrosConBajaLogica){
			anyosAbono= new Integer(730);
			}
			
			
			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			

			// Obtengo el numero de colegiado en caso de que este colegiado
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
//			if(!clienteAdm.esColegiado(idPersona,idInst).isEmpty()){
//				numero = clienteAdm.obtenerNumero(idPersona,idInst);				
//			}	
			// DCG Nuevo ////////////////////
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona, new Integer(idInstitucionPersona));
			if(datosColegiales!=null)
				numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			//////////////////////
			int destinatarioAbono = 0;
			if(datosColegiales!=null){
				destinatarioAbono = mapping.getPath().equals("/JGR_AbonosClienteSJCS")?1:2;
			}
			HashMap databackup = getPaginador(request, paginadorPenstania);
						
			if (databackup != null) {
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				int totalRegistros = paginador.getNumeroTotalRegistros();
				Vector datos=new Vector();
				
				if (totalRegistros == 0) {
					paginador = null;
				}				
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
				FacAbonoAdm abonoAdm = new FacAbonoAdm(user);
				
				 
				resultado = abonoAdm.getAbonosClientePaginador(user.getLocation(),idPersona.toString(),anyosAbono,destinatarioAbono);
				
				Vector datos = null;
				

				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					setPaginador(request, paginadorPenstania, databackup);				} 
			}
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);
			request.setAttribute("ACCION", accion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
			request.setAttribute("container", abonos);
			request.setAttribute("bIncluirRegistrosConBajaLogica",new Boolean(bIncluirRegistrosConBajaLogica).toString());
			request.setAttribute("destinatarioAbono", (Integer)destinatarioAbono);
			String informeUnico = ClsConstants.DB_TRUE;
			
				AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
				// mostramos la ventana con la pregunta
				String idTipoInforme = "ABONO";
				if(destinatarioAbono==FacAbonoAdm.DESTINATARIOABONO_SJCS){
					idTipoInforme = "CPAGO";
				}
				
				Vector informeBeans=adm.obtenerInformesTipo(idInstitucion,idTipoInforme,null, null);
				if(informeBeans!=null && informeBeans.size()>1){
					informeUnico = ClsConstants.DB_FALSE;
					
				}
				
				
			
			request.setAttribute("informeUnico", informeUnico);
			

			
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}

		return "abrir";
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		return result;
		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="editar";
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="ver";

		try{
			Vector ocultos=new Vector();
			String modo="consulta";
			Hashtable datosAbonos=new Hashtable();

			AbonosClienteForm form = (AbonosClienteForm) formulario;
		
			// Obtengo valores del formulario y los estructuro
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
			modo = "consulta";
			datosAbonos.put("accion",modo);
			datosAbonos.put("idAbono",ocultos.get(0));
			datosAbonos.put("idInstitucion",ocultos.get(1));
			
			if(mapping.getPath().equals("/CEN_AbonosCliente"))
				request.setAttribute("pestanaId", "ABNCEN");
			else
				request.setAttribute("pestanaId", "PGOCEN");
			// Paso de parametros a las pestanhas
			request.setAttribute("datosAbonos", datosAbonos);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="nuevo";
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="insertar";
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="modificar";
		return (result);		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="borrar";
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";
		return (result);
				
	}
	protected String imprimir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			// casting del formulario
//			AbonosClienteForm miFormulario = (AbonosClienteForm) formulario;
			
			HashMap databackup = getPaginador(request, paginadorPenstania);
			
			if (databackup != null) {
		
			
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				AdmInformeAdm adm = new AdmInformeAdm(user);
				Vector datos = adm.selectGenericoBind(paginador.getQueryInicio(), paginador.getCodigosInicio());
				Hashtable<String, Object> datosHashtable = (Hashtable<String, Object>)datos.get(0);
				//La linea de debajo serviria si no se necesita Orden
//				String[] cabeceras = UtilidadesHash.getClaves(datosHashtable);
				String[] cabeceras = new String[7];
				cabeceras[0] = "FECHA";
				cabeceras[1] = "NUMEROABONO";
				cabeceras[2] = "OBSERVACIONES";
				cabeceras[3] = "TOTALNETO";
				cabeceras[4] = "TOTALIVA";
				cabeceras[5] = "TOTAL";
				cabeceras[6] = "TOTALABONADO";
							
				for (int i = 0; i < datos.size(); i++) {
					Hashtable filaHashtable = (Hashtable) datos.elementAt(i);
					//Hashtable filaHashtable = fila.getRow();
					UtilidadesHash.set(filaHashtable, "TOTALNETO", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALNETO"), 2)));
					UtilidadesHash.set(filaHashtable, "TOTALIVA", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALIVA"), 2)));
					UtilidadesHash.set(filaHashtable, "TOTAL", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTAL"), 2)));
					UtilidadesHash.set(filaHashtable, "TOTALABONADO", UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea((String)filaHashtable.get("TOTALABONADO"), 2)));
					UtilidadesHash.set(filaHashtable, "FECHA", GstDate.getFormatedDateShort("",(String)filaHashtable.get("FECHA")));
					
					
				}
				
				
				InformePersonalizable informePersonalizable = new InformePersonalizable();
				List<InformeForm> informesForms = new ArrayList<InformeForm>();
				InformeForm informeForm = new InformeForm();
				informeForm.setDirectorio("");
				informeForm.setIdInstitucion(user.getLocation());
				informeForm.setNombreSalida("excelAbonosCliente");
				informeForm.setAlias("");
				informeForm.setTipoFormato(AdmInformeBean.TIPOFORMATO_EXCEL);
				informesForms.add(informeForm);

				File ficheroSalida = informePersonalizable.getFicheroGenerado(informesForms, datos,cabeceras, user);
				request.setAttribute("nombreFichero", ficheroSalida.getName());
				request.setAttribute("rutaFichero", ficheroSalida.getPath());
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK","OK");
				
				return "descarga";	
				
				
			}

		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
		return exitoRefresco("messages.noRecordFound",request);
	}

}
