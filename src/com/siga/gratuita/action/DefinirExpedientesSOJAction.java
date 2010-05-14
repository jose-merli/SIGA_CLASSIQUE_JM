//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 21/01/2005

package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.BusquedaClientesFiltrosAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsSOJBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DatosGeneralesSOJForm;
import com.siga.gratuita.form.DefinirSOJForm;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirExpedientesSOJAction extends MasterAction {		
	
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		MasterForm miForm = (MasterForm) formulario;
		try {
			if (miForm == null){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}
			else {
				String accion = miForm.getModo();
				String mapDestino = null;
				if (accion != null && !accion.equalsIgnoreCase("")) {
					if (accion.equalsIgnoreCase("relacionarConEJG")) {
						mapDestino=relacionarConEJG (true, miForm, request);
					}
				}
				else if((miForm.getModo()==null)||(miForm.getModo().equals(""))){
					return mapping.findForward(this.abrir(mapping, miForm, request, response));
				}
				if (mapDestino != null) {
					return mapping.findForward(mapDestino);
				}
			}
		}
		catch(Exception e){
			return mapping.findForward("exception");
		}
		return super.executeInternal(mapping, formulario, request, response); 
		
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
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException {		
		try{
			
		DefinirSOJForm miForm = (DefinirSOJForm) formulario;		
 		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
 		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();
		miHash = miForm.getDatos();	
		HashMap databackup=new HashMap();

		//Realiza la recuperación de los datos.
	   if ((request.getSession().getAttribute("DATAPAGINADOR")!=null)||(request.getSession().getAttribute("DATAPAGINADOR")==null)){		   
	 	/*databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
			     PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
			     Vector datos=new Vector();
			     //Si no es la primera llamada, obtengo la página del request y la busco con el paginador
			     String pagina = (String)request.getParameter("pagina");
			
			     if (paginador!=null){	
			    	 if (pagina!=null)
			    		 datos = paginador.obtenerPagina(Integer.parseInt(pagina));
			    	 else// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
			    		 datos = paginador.obtenerPagina((paginador.getPaginaActual()));			    	 
			     }	
			     databackup.put("paginador",paginador);
			     databackup.put("datos",datos);	   
			     
	 
	   } else {*/				
	  	    databackup=new HashMap();
			//obtengo datos de la consulta 			
	  	    PaginadorBind resultado = null;
	  	    Vector datos = null;		
	  	    ScsDefinirSOJAdm sojAdm=new ScsDefinirSOJAdm(this.getUserBean(request));
	  	    resultado=sojAdm.getBusquedaSOJ((String)usr.getLocation(),miHash);
		    databackup.put("paginador",resultado);
	  	    
		    if (resultado!=null){ 
	  	    	datos = resultado.obtenerPagina(1);
	  	    	databackup.put("datos",datos);
	  	    	request.getSession().setAttribute("DATAPAGINADOR",databackup);
	  	    } 
	  }
	   
//	  En "DATOSFORMULARIO" almacenamos el identificador del letrado			 			 
		miHash.put("BUSQUEDAREALIZADA","1");
		request.getSession().setAttribute("DATOSFORMULARIO",miHash);		
	} catch (SIGAException e1) {
		// Excepcion procedente de obtenerPagina cuando se han borrado datos
		 borrarPaginador(request, paginadorPenstania);
		 return exitoRefresco("error.messages.obtenerPagina",request);
	}catch (Exception e) { 	
		throw new ClsExceptions(e,"Error obteniendo Expedientes SOJ"); 
	}
	return "listarSOJ";
	}
	
	protected String relacionarConEJG (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		try {
			DatosGeneralesSOJForm miForm 	= (DatosGeneralesSOJForm)formulario;
			
			Hashtable miHash = new Hashtable ();

			UtilidadesHash.set(miHash, ScsSOJBean.C_ANIO,			miForm.getAnio());
			UtilidadesHash.set(miHash, ScsSOJBean.C_NUMERO,			miForm.getNumSOJ());
			UtilidadesHash.set(miHash, ScsSOJBean.C_IDTIPOSOJ,		miForm.getIdTipoSOJ());
			UtilidadesHash.set(miHash, ScsSOJBean.C_IDINSTITUCION, 	miForm.getIdInstitucion());

			if (bCrear) {
				// Creamos la relacion
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGANIO,		miForm.getAnioEJG());
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGNUMERO,		miForm.getNumeroEJG());
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGIDTIPOEJG,	miForm.getDescTipoEJG());
			}
			else {
				// Borramos la relacion
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGANIO,		"");
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGNUMERO,		"");
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGIDTIPOEJG,	"");
			}

			String [] campos = {ScsSOJBean.C_EJGANIO, ScsSOJBean.C_EJGNUMERO, ScsSOJBean.C_EJGIDTIPOEJG};

			ScsDefinirSOJAdm sojAdm = new ScsDefinirSOJAdm(this.getUserBean(request));  
			if (!sojAdm.updateDirect(miHash, null, campos)) {
				throw new ClsExceptions ("Error de la relacion Asistencia - EJG");
			}
		}
		catch (Exception e) 
		{
			throw new SIGAException("Error de la relacion Asistencia - EJG",e,new String[] {"modulo.gratuita"});
		} 

		return "exito";
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
				
		HttpSession session=request.getSession();
		DefinirSOJForm miForm = (DefinirSOJForm) formulario;		
		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		//Entramos al formulario en modo 'modificación'
		session.setAttribute("accion","editar");
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		Vector visibles = formulario.getDatosTablaVisibles(0);		
	    String desdeEjg=request.getParameter("desdeEJG");
	    if (desdeEjg!=null && desdeEjg.equalsIgnoreCase("si")){
	    	session.removeAttribute("DATAPAGINADOR");
	    }
		
		Hashtable miHash = new Hashtable();
		String tipo,institucion,anio,numero;
		
		if(ocultos!=null && visibles!=null){
			if (((String)ocultos.get(0)).equalsIgnoreCase("desdeEJG")) {
				// Venimos desde la ficha de ejg
				institucion = (String) ocultos.get(1);
				anio        = (String) ocultos.get(2);
				numero      = (String) ocultos.get(3);
				tipo        = (String) ocultos.get(4);
			}
			else {
				tipo        =(String) ocultos.get(0);
				institucion =(String) ocultos.get(1);
				numero      =(String) ocultos.get(3);
				anio        =(String) ocultos.get(4);
			}
			//los metemos en el formulario
			miForm.setIdTipoSOJ(tipo);
			miForm.setIdInstitucion(institucion);
			miForm.setAnio(anio);
			miForm.setNumero(numero);
			
		}else{
			tipo=miForm.getIdTipoSOJ();
			institucion=miForm.getIdInstitucion();
			anio=miForm.getAnio();
			numero=miForm.getNumero();
			
		}
		Hashtable miHash2 = new Hashtable();
		miHash2.put(ScsSOJBean.C_IDTIPOSOJ,tipo);
		miHash2.put(ScsSOJBean.C_IDINSTITUCION,institucion);
		miHash2.put(ScsSOJBean.C_ANIO,anio);
		miHash2.put(ScsSOJBean.C_NUMERO,numero);
		
		miHash.put(ScsSOJBean.C_IDTIPOSOJ,tipo);
		miHash.put(ScsSOJBean.C_IDINSTITUCION,institucion);
		miHash.put(ScsSOJBean.C_ANIO,anio);
		miHash.put(ScsSOJBean.C_NUMERO,numero);

		// 03-04-2006 RGG cambio en ventanas de Personas JG
		// Persona JG
		Hashtable hash = new Hashtable();
		hash.put("IDINSTITUCION",institucion);
		hash.put("IDTIPOSOJ",tipo);
		hash.put("ANIO",anio);
		hash.put("NUMERO",numero);
		ScsDefinirSOJAdm admi = new ScsDefinirSOJAdm(this.getUserBean(request)); 
		Vector resultadoObj = admi.selectByPK(hash);
		ScsSOJBean obj = (ScsSOJBean)resultadoObj.get(0);
		if (obj.getIdPersonaJG()==null) {
			miHash.put("idPersonaJG","");
		} else {
			miHash.put("idPersonaJG",obj.getIdPersonaJG().toString());
		}
		miHash.put("idInstitucionJG",usr.getLocation());
		
		// CONCEPTO
		miHash.put("conceptoE",PersonaJGAction.SOJ);
		// clave ASI
		miHash.put("idInstitucionSOJ",institucion);
		miHash.put("idTipoSOJ",tipo);
		miHash.put("anioSOJ",anio);
		miHash.put("numeroSOJ",numero);
		// titulo
		miHash.put("tituloE","gratuita.busquedaSOJ.beneficiarios");
		miHash.put("localizacionE","gratuita.busquedaSOJ.localizacion");
		// accion
		miHash.put("accionE","editar");
		// action
		miHash.put("actionE","/JGR_PestanaSOJBeneficiarios.do");
		
		
		// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
		try {
			Vector resultado = admBean.selectPorClave(miHash2);
			ScsSOJBean soj = (ScsSOJBean)resultado.get(0);
			request.setAttribute("SOJ",miHash);			
			session.setAttribute("elegido",admBean.beanToHashTable(soj));
			session.setAttribute("DATABACKUP",admBean.beanToHashTable(soj));
		} catch (Exception e) {
		   throwExcp("messages.general.error",e,null);
		}
		
		return "editar";
	
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {		
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		Vector visibles = formulario.getDatosTablaVisibles(0);
		
		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		//Entramos al formulario en modo 'consulta'
		request.getSession().setAttribute("accion","ver");
		DefinirSOJForm miForm = (DefinirSOJForm) formulario;		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		 String desdeEjg=request.getParameter("desdeEJG");
		    if (desdeEjg!=null && desdeEjg.equalsIgnoreCase("si")){
		    	request.getSession().removeAttribute("DATAPAGINADOR");
		    }
		Hashtable miHash = new Hashtable();
		String tipo,institucion,anio,numero;
		
		if(ocultos!=null && visibles!=null){
			
			if (((String)ocultos.get(0)).equalsIgnoreCase("desdeEJG")) {
				// Venimos desde la ficha de ejg
				institucion = (String) ocultos.get(1);
				anio        = (String) ocultos.get(2);
				numero      = (String) ocultos.get(3);
				tipo        = (String) ocultos.get(4);
			}
			else {
				tipo        =(String) ocultos.get(0);
				institucion =(String) ocultos.get(1);
				numero      =(String) ocultos.get(3);
				anio        =(String) ocultos.get(4);
			}

			//los metemos en el formulario
			miForm.setIdTipoSOJ(tipo);
			miForm.setIdInstitucion(institucion);
			miForm.setAnio(anio);
			miForm.setNumero(numero);
		}
		else{
			tipo=miForm.getIdTipoSOJ();
			institucion=miForm.getIdInstitucion();
			anio=miForm.getAnio();
			numero=miForm.getNumero();
		}
		Hashtable miHash2 = new Hashtable();
		miHash2.put(ScsSOJBean.C_IDTIPOSOJ,tipo);
		miHash2.put(ScsSOJBean.C_IDINSTITUCION,institucion);
		miHash2.put(ScsSOJBean.C_ANIO,anio);
		miHash2.put(ScsSOJBean.C_NUMERO,numero);

		miHash.put(ScsSOJBean.C_IDTIPOSOJ,tipo);
		miHash.put(ScsSOJBean.C_IDINSTITUCION,institucion);
		miHash.put(ScsSOJBean.C_ANIO,anio);
		miHash.put(ScsSOJBean.C_NUMERO,numero);

		// 03-04-2006 RGG cambio en ventanas de Personas JG
		// Persona JG
		Hashtable hash = new Hashtable();
		hash.put("IDINSTITUCION",institucion);
		hash.put("IDTIPOSOJ",tipo);
		hash.put("ANIO",anio);
		hash.put("NUMERO",numero);
		ScsDefinirSOJAdm admi = new ScsDefinirSOJAdm(this.getUserBean(request)); 
		Vector resultadoObj = admi.selectByPK(hash);
		ScsSOJBean obj = (ScsSOJBean)resultadoObj.get(0);
		if (obj.getIdPersonaJG()==null) {
			miHash.put("idPersonaJG","");
		} else {
			miHash.put("idPersonaJG",obj.getIdPersonaJG().toString());
		}
		miHash.put("idInstitucionJG",usr.getLocation());
		
		// CONCEPTO
		miHash.put("conceptoE",PersonaJGAction.SOJ);
		// clave ASI
		miHash.put("idInstitucionSOJ",institucion);
		miHash.put("idTipoSOJ",tipo);
		miHash.put("anioSOJ",anio);
		miHash.put("numeroSOJ",numero);
		// titulo
		miHash.put("tituloE","gratuita.busquedaSOJ.beneficiarios");
		miHash.put("localizacionE","gratuita.busquedaSOJ.localizacion");
		// accion
		miHash.put("accionE","ver");
		// action
		miHash.put("actionE","/JGR_PestanaSOJBeneficiarios.do");

		
		// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
		try {
			Vector resultado = admBean.selectPorClave(miHash2);
			ScsSOJBean soj = (ScsSOJBean)resultado.get(0);
			request.setAttribute("SOJ",miHash);			
			request.getSession().setAttribute("elegido",admBean.beanToHashTable(soj));
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(soj));
		} catch (Exception e) {
		   throwExcp("messages.general.error",e,null);
		}	
		
		return "ver";
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
			CenColegiadoBean elegido = new CenColegiadoBean();
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		return "insertarSOJ";
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
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirSOJForm miForm = (DefinirSOJForm) formulario;		
		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		String forward="";
		String idPersona = null;
		Hashtable miHash = new Hashtable();		
		
		try {
			miHash = miForm.getDatos();
			
			// Si hemos introducido manualmente el numero de colegiado, no sabremos su idPersona
			//lo consultamos de BD
			/*if ((miForm.getIdPersona()==null)||(miForm.getIdPersona().equals(""))){
				try{
					CenColegiadoBean colegiadoAuxBean = new CenColegiadoBean();
					CenColegiadoAdm colegiadoAuxAdm = new CenColegiadoAdm (this.getUserBean(request));
					idPersona = colegiadoAuxAdm.getIdPersona(miForm.getNColegiado(),usr.getLocation());
					miHash.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersona);
				}catch(Exception e){
					throwExcp("No existe colegiado con ese NColegiado.",e,tx);
				}
			}*/			
			
			// Formateamos la fecha de apertura
			miHash.put(ScsSOJBean.C_FECHAAPERTURA,GstDate.getApplicationFormatDate("",miHash.get(ScsSOJBean.C_FECHAAPERTURA).toString()));
			
			//Obtenemos el numSOJ del SOJ
			/*GestorContadores cont=new GestorContadores(this.getUserBean(request));
    		Hashtable contadorHash=cont.getContador(this.getIDInstitucion(request),ClsConstants.SOJ);
    		String numSOJ;
    		//numSOJ=cont.getSiguienteNumContador(contadorHash);
    		//numSOJ++;
    		numSOJ=cont.getNuevoContador(contadorHash);
    		tx=usr.getTransaction();
			tx.begin();
    		cont.setContador(contadorHash,numSOJ);
    		//String SOJ_codigo= String.valueOf(numSOJ);
    		
    		miHash.put(ScsSOJBean.C_NUMSOJ, numSOJ);*/
			tx=usr.getTransaction();
			tx.begin();
			miHash=admBean.calcularNumeroMaximoSOJ(miHash);
					
			
			request.setAttribute("NUMERO",miHash.get("NUMERO"));
			request.setAttribute("TIPO",miHash.get(ScsSOJBean.C_IDTIPOSOJ));
			request.setAttribute("INSTITUCION",miHash.get(ScsSOJBean.C_IDINSTITUCION));
			request.setAttribute("ANIO",miHash.get(ScsSOJBean.C_ANIO));
			if (!admBean.insert(miHash))
	        {
				throw new ClsExceptions("Error al insertar SOJ: "+admBean.getError());
	        }
			
			///////////////////////////////////////////////////////////////////////////////////////////
			// RGG 21-03-2006 : Cambios debidos a la nueva asignacion de colegiados desde Busqueda SJCS
			// ----------------------------------------------------------------------------------------
			
			//-----------------------------------------------------
			// obtencion de valores a utilizar (MODIFICAR SEGUN ACTION)
			String idInstitucionSJCS=usr.getLocation();
			String idTurnoSJCS=miForm.getIdTurno();
			String idGuardiaSJCS=miForm.getIdGuardia();
			String anioSJCS=miForm.getAnio();
			String numeroSJCS=miForm.getNumero();
			String idPersonaSJCS=miForm.getIdPersona();
			String origenSJCS = "gratuita.insertarSOJ.literal.insertarSOJ"; 
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
						
		} catch (Exception e) {
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
		return "exitoSOJ"; 
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
		String forward="";
		Vector visibles = formulario.getDatosTablaVisibles(0);
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsSOJBean.C_IDTIPOSOJ,(ocultos.get(0)));
			miHash.put(ScsSOJBean.C_IDINSTITUCION,(ocultos.get(1)));						
			miHash.put(ScsSOJBean.C_ANIO,(visibles.get(3)));
			miHash.put(ScsSOJBean.C_NUMERO,(ocultos.get(3)));										
			
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.delete(miHash))
		    {
				tx.commit();
				forward="exitoRefresco";		        
		    }		    
		    else forward="exito";	    
			
		} catch (Exception e) {
		   throwExcp("messages.general.error",e,null);
		}		

		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.deleted.success",request);
		else return exito("messages.deleted.error",request);
        
		
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			CenPersonaAdm persona = new CenPersonaAdm(this.getUserBean(request));
			CenColegiadoBean elegido = new CenColegiadoBean();
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
		/* "DATOSFORMULARIO" Y "DATABACKUP" son variables de sesión que utilizamos y también se utilizan en otros casos de uso
		 * así que por seguridad, borramos estas variables por si conteniesen algún valor, para evitar problemas. */
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("DATABACKUP");
		return "inicio";		
	}
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		/* Borramos de la sesion las variables que pueden haberse utilizado, para así evitar posibles problemas con datos erróneos contenidos ahí */		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		request.getSession().removeAttribute("resultadoTelefonos");
		return "inicio";
	}	

}