package com.siga.gratuita.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.ibatis.sqlmap.engine.mapping.sql.dynamic.elements.IsEmptyTagHandler;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.censo.action.Direccion;
import com.siga.general.*;
import com.siga.gratuita.form.BusquedaClientesFiltrosForm;

public class BusquedaClientesFiltrosAction extends MasterAction
{
	private static final int FILTRO_COLAGUARDIA = 1;
	private static final int FILTRO_COLATURNO = 2;
	private static final int FILTRO_INSCRITOSGUARDIA = 3;
	private static final int FILTRO_INSCRITOSTURNO = 4;
	private static final int FILTRO_EJERCIENTES = 5;

	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request,HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
				
				String accion = miForm.getModo();
				
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}
				
				//  Buscar Paginador
				if (accion.equalsIgnoreCase("buscarPaginador")){
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPaginador(mapping, miForm, request, response);
					break;
				}
				
				return super.executeInternal(mapping,formulario,request,response);
				
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException e) {
			throw e;
		} 
		catch (Exception e){
			throw new SIGAException("error.messages.application",e);
		}
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		request.getSession().removeAttribute("DATAPAGINADOR_VECTOR");
		return buscarPaginador(mapping, formulario, request, response);
	}

	/**
	 * Metodo que implementa el modo buscar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPaginador(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
        try {
	        /***** PAGINACION*****/
        	Vector datos = null;
	        HashMap databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR_VECTOR");

	        if (databackup != null) { 
		    	
				Paginador paginador = (Paginador)databackup.get("paginador");
				
				String pagina = (String)request.getParameter("pagina");
				if (pagina!=null){
					datos = paginador.obtenerPaginaNoCache(Integer.parseInt(pagina));
				}
				else {
					// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPaginaNoCache((paginador.getPaginaActual()));
				}
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
				request.getSession().setAttribute("DATAPAGINADOR_VECTOR", databackup);
		    }
		    else {	
		        Paginador paginador = this.buscarPaginador(formulario, request);
		        
		        databackup = new HashMap();
				databackup.put("paginador",paginador);
				
				BusquedaClientesFiltrosForm f = (BusquedaClientesFiltrosForm) formulario;
				databackup.put("concepto", f.getConcepto());
				
				if (paginador!=null){ 
				   datos = paginador.obtenerPaginaNoCache(1);
				   databackup.put("datos",datos);
				   request.getSession().setAttribute("DATAPAGINADOR_VECTOR",databackup);
				}   
		    }
        } 
        catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
	    return "resultado";
	}
	
	/**
	 * Obtiene los resultados de la busqueda segun los filtros de la ventana modal
	 * 
	 * @param formulario
	 * @param request
	 * @return
	 * @throws SIGAException
	 */
	protected Paginador buscarPaginador(MasterForm formulario, HttpServletRequest request) throws SIGAException
	{
		// Controles generales
		UsrBean usr = this.getUserBean(request);
		String idInstitucion = usr.getLocation();
		String idioma = usr.getLanguage();
		
		BusquedaClientesFiltrosAdm adm = new BusquedaClientesFiltrosAdm();
		Vector resultado;
		
		try {
			// obteniendo campos del formulario
			BusquedaClientesFiltrosForm miFormulario = (BusquedaClientesFiltrosForm) formulario;
			String filtro = miFormulario.getIdFiltro();
			String nif = miFormulario.getNif();
			String ncoleg = miFormulario.getNumeroColegiado();
			String nombre = miFormulario.getNombrePersona();
			String apellido1 = miFormulario.getApellido1();
			String apellido2 = miFormulario.getApellido2();
			String idTurno;// = miFormulario.getIdTurno();
			String idTurnoCombo = (String) request.getParameter("identificador");
			idTurno = (idTurnoCombo == null) ? "" : idTurnoCombo.substring(idTurnoCombo.indexOf(",") + 1);
			String idGuardia;// = miFormulario.getIdGuardia();
			String idGuardiaCombo = (String) request.getParameter("identificador2");
			idGuardia = (idGuardiaCombo == null) ? "" : idGuardiaCombo.substring(idGuardiaCombo.indexOf(",") + 1);
			
			String fecha = miFormulario.getFecha();
			if (fecha != null && fecha.length() > 0)
				fecha = GstDate.getApplicationFormatDate(idioma, fecha);
			String fechaFomateada = (fecha == null || fecha.equals("")) ? "sysdate" : GstDate.getFormatedDateShort("", fecha);
			int idFiltro;
			try {
				idFiltro = Integer.parseInt(filtro);
			} catch (Exception e) {
				e.printStackTrace();
				idFiltro = 0;
			}

			// pasando los filtros al ADM
			adm.setFiltosPersona(nif, nombre, apellido1, apellido2);
			adm.setFiltroNColegiado(ncoleg);

			// ejecutando la busqueda
			switch (idFiltro) {
				case FILTRO_INSCRITOSGUARDIA:
					if (isFormEmpty(ncoleg, nombre, apellido1, apellido2, nif)) {
						resultado = adm.buscaLetradosColaGuardia(idInstitucion,idTurno, idGuardia, fechaFomateada, 0);
					} else {
						resultado = adm.buscaLetradosInscritosGuardia(idInstitucion, idTurno, idGuardia, fechaFomateada);
					}
					break;
				case FILTRO_INSCRITOSTURNO:
					if (isFormEmpty(ncoleg, nombre, apellido1, apellido2, nif)) {
						resultado = adm.buscaLetradosColaTurno(idInstitucion,idTurno, fechaFomateada, idFiltro);
					} else {
						resultado = adm.buscaLetradosInscritosTurno(idInstitucion,idTurno, fechaFomateada);
					}
					break;
				case FILTRO_EJERCIENTES:
					resultado = adm.buscaLetradosCensoCompleto(idInstitucion,fechaFomateada);
					break;
				default:// no hay filtro
					throw new ClsExceptions("La opción seleccionada no está implementada");
			}

			Paginador paginador = new Paginador(resultado);
			return paginador;
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return null;
	} // buscarPaginador()

	
	/**
	 * Metodo que comprueba si el formulario esta vacío
	 * @param  ncoleg - Numero de colegiado
	 * @param  nom -  Nombre del colegiado
	 * @param  ape1 - Primer apellido del colegiado 
	 * @param  ape2 - Segundo del colegiado
	 * @param  nif - NIF del colegiado
	 * @return  Boolean  Comprobacion del formulario  
	 */
	private boolean isFormEmpty(String ncoleg, String nom, String ape1,
			String ape2, String nif) {
		if (ncoleg.equals("") && nom.equals("") && ape1.equals("")
				&& ape2.equals("") && nif.equals("")) {
			return true;
		} else {
			return false;
		}
	}
		
	
	/**
	 * Metodo que implementa la busqueda automatica
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String destino = "";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
	
			// casting del formulario
			BusquedaClientesFiltrosForm miFormulario = (BusquedaClientesFiltrosForm)formulario;
			String concepto=miFormulario.getConcepto().toUpperCase();
			String idTurno=miFormulario.getIdTurno();
			if(idTurno!=null)idTurno=idTurno.substring(idTurno.indexOf(",")+1);
			String idGuardia=miFormulario.getIdGuardia();
			String fecha=miFormulario.getFecha();
			if(fecha!=null && fecha.length()>0) fecha=GstDate.getApplicationFormatDate(user.getLanguage(),fecha);

			BusquedaClientesFiltrosAdm adm= new BusquedaClientesFiltrosAdm();
			
			// busqueda de clientes
			Row resultado = null;
			if		(concepto.equals("DESIGNACION")){
				//ya no se usa el botón de búsqueda automática
				//además el metodo no existe. Ahora habría que usar gestionaDesignacionesAutomaticas que además de devolver
				//el próximo en la cola, gestiona los saltos y compensaciones
//				resultado=adm.buscaAutomaticaDesignaciones(idInstitucion, idTurno);
				
			}else if(concepto.equals("ASISTENCIA")){
				resultado=adm.buscaAutomaticaAsistencias(idInstitucion, idTurno, idGuardia, fecha);
				
			}else if(concepto.equals("EJG")){
				resultado=adm.buscaAutomaticaEJG(idInstitucion, idTurno, idGuardia, fecha);
				
			}else if(concepto.equals("SOJ")){
				resultado=adm.buscaAutomaticaSOJ(idInstitucion, idTurno, idGuardia, fecha);
				
			}
			
			request.setAttribute("resultado",resultado);
			destino="seleccion";

	     }catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		 return destino;
	}
	
	/** 
	 *  Funcion que atiende a la peticion 'editar telefono'. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		
		try {
			// INICIALIZAMOS EL DATABUCKUP
			BusquedaClientesFiltrosForm formulario = (BusquedaClientesFiltrosForm)miForm;
			
			Integer idInstitucion=formulario.getIdInstitucion();
			Long idPersona=formulario.getIdPersona();
			 Long indicadorDirec=null;

			//BUSCAMOS SI EL CLIENTE TIENE ALGUNA DIRECCION DE TIPO GUARDIA
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm (this.getUserBean(request));
//			Hashtable claves=new Hashtable();
//			claves.put(CenDireccionTipoDireccionBean.C_IDINSTITUCION, idInstitucion);
//			claves.put(CenDireccionTipoDireccionBean.C_IDPERSONA, idPersona);
//			claves.put(CenDireccionTipoDireccionBean.C_IDTIPODIRECCION, new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
			Hashtable direccionGuardia=new Hashtable();
			direccionGuardia = tipoDirAdm.selectDireccionGuardia(idInstitucion,idPersona,new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
			
			if(direccionGuardia != null && direccionGuardia.size() > 0){//solo debe haber un registro
				CenDireccionesAdm dirAdm=new CenDireccionesAdm(this.getUserBean(request));
				//CenDireccionTipoDireccionBean beanTipoDirec=(CenDireccionTipoDireccionBean)v.elementAt(0);
				//Long indicadorDirec=(Long)beanTipoDirec.getIdDireccion();
			    indicadorDirec = UtilidadesHash.getLong(direccionGuardia, CenDireccionTipoDireccionBean.C_IDDIRECCION);
				Hashtable direccion=dirAdm.selectDirecciones(idPersona,idInstitucion,indicadorDirec);
				
				//ponemos los datos de la dirección de guardia en el form
				formulario.setModo("modificar");
				formulario.setIdDireccion(indicadorDirec);
				formulario.setTelefono1((String)direccion.get(CenDireccionesBean.C_TELEFONO1));
				formulario.setTelefono2((String)direccion.get(CenDireccionesBean.C_TELEFONO2));
				formulario.setMovil((String)direccion.get(CenDireccionesBean.C_MOVIL));
				formulario.setFax1((String)direccion.get(CenDireccionesBean.C_FAX1));
				formulario.setFax2((String)direccion.get(CenDireccionesBean.C_FAX2));
				//hacemos el backup
				request.getSession().setAttribute("DATABACKUP_CLIENTESFILTRO", direccion);
			}else{
				formulario.setModo("insertar");
			}

		}
		catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "editarTelefono";
	}
	
	/** 
	 *  Funcion que atiende a la peticion ver, que busca los letrados de guardia ese dia. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		
		try {
			// INICIALIZAMOS EL DATABUCKUP
			BusquedaClientesFiltrosForm formulario = (BusquedaClientesFiltrosForm)miForm;
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			String idInstitucion=usr.getLocation();
			String idTurno=formulario.getIdTurno().substring(formulario.getIdTurno().indexOf(",")+1,formulario.getIdTurno().length());
			String idGuardia=formulario.getIdGuardia();
			String fecha=formulario.getFecha();
			
			ScsGuardiasColegiadoAdm adm = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
			Vector v = adm.getColegiadosGuardiaDia(idInstitucion, idTurno, idGuardia, fecha);
//			if (v==null || v.size()==0) {
//			    throw new SIGAException("gratuita.messages.noLetradosDiaGuardia");
//			}
			request.setAttribute("resultado",v);
			
		}
		catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "cargarCombo";
	}
	
	/** 
	 *  Funcion que atiende a la peticion 'insertar' que proviene de 'editar telefono'. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		boolean resultOk=false;
		UserTransaction tx = null;
		try	{
			BusquedaClientesFiltrosForm formulario = (BusquedaClientesFiltrosForm)miForm;

			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction(); 
			tx.begin();
			
			// ACTUALIZAMOS LA DIRECCIÓN DE GUARDIA DEL CLIENTE. EN CASO DE QUE NO EXISTIERA UNA DIRECCIÓN DE GUARDIA
			// ENTONCES LA INSERTAMOS Y SI YA EXISTIA ACTUALIZAMOS LOS DATOS DE LA MISMA
			CenDireccionesBean beanDir  = new CenDireccionesBean ();
			Direccion direccion = new Direccion();	
			beanDir.setIdInstitucion(formulario.getIdInstitucion());
			beanDir.setIdPersona(formulario.getIdPersona());
			beanDir.setFax1(formulario.getFax1());
			beanDir.setFax2(formulario.getFax2());
			beanDir.setMovil(formulario.getMovil());
			beanDir.setTelefono1(formulario.getTelefono1());
			beanDir.setTelefono2(formulario.getTelefono2());
			
			//Insertamos la nueva direccion de guardia
			beanDir.setCodigoPostal("");
			beanDir.setCorreoElectronico("");
			beanDir.setDomicilio("");
			beanDir.setFechaBaja("");
			beanDir.setIdPais("");
			beanDir.setIdPoblacion("");
			beanDir.setIdProvincia("");
			beanDir.setPaginaweb("");
			beanDir.setPreferente("");
			
			//estableciendo los datos del tipo de direccion
			String tiposDir = ""+ClsConstants.TIPO_DIRECCION_GUARDIA;
	
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo ("");
			
			// Se llama a la interfaz Direccion para actualizar una nueva direccion
			Direccion dirAux = direccion.insertar(beanDir, tiposDir, beanHis, null, usr);
							
			//Si existe algún fallo en la inserción se llama al metodo exito con el error correspondiente
			if(dirAux.isFallo()){
				throw new SIGAException (dirAux.getMsgError());
			}
			
			request.setAttribute("mensaje","messages.inserted.success");
			tx.commit();
			
			request.setAttribute("hiddenFrame", "1");
			request.setAttribute("modal","1");
			forward = "exito";
			
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.censo"}, es, tx);			
		}catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return forward;
	}

	/** 
	 *  Funcion que atiende a la peticion 'modificar' que proviene de 'editar telefono'. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		boolean resultOk=false;
		UserTransaction tx = null;
		try	{
			BusquedaClientesFiltrosForm formulario = (BusquedaClientesFiltrosForm)miForm;

			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction(); 
			tx.begin();
			
			// ACTUALIZAMOS LA DIRECCIÓN DE GUARDIA DEL CLIENTE. EN CASO DE QUE NO EXISTIERA UNA DIRECCIÓN DE GUARDIA
			// ENTONCES LA INSERTAMOS Y SI YA EXISTIA ACTUALIZAMOS LOS DATOS DE LA MISMA
			Direccion direccion = new Direccion();	
			CenDireccionesBean beanDir  = new CenDireccionesBean ();
			beanDir.setIdInstitucion(formulario.getIdInstitucion());
			beanDir.setIdPersona(formulario.getIdPersona());
			beanDir.setIdDireccion(formulario.getIdDireccion());
			beanDir.setFax1(formulario.getFax1());
			beanDir.setFax2(formulario.getFax2());
			beanDir.setMovil(formulario.getMovil());
			beanDir.setTelefono1(formulario.getTelefono1());
			beanDir.setTelefono2(formulario.getTelefono2());
			
			// Actualizamos el registro de la dirección de guardia
			beanDir.setIdDireccion(formulario.getIdDireccion());
			beanDir.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP_CLIENTESFILTRO"));
			
			//estableciendo los datos del tipo de direccion guardia
			String tiposDir = ""+ClsConstants.TIPO_DIRECCION_GUARDIA;
	
			//estableciendo los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean ();
			beanHis.setMotivo ("");
			
			// Se llama a la interfaz Direccion para actualizar una nueva direccion
			Direccion dirAux = direccion.actualizarDireccion(beanDir, tiposDir, beanHis, null, usr);
							
			//Si existe algún fallo en la inserción se llama al metodo exito con el error correspondiente
			if(dirAux.isFallo()){
				throw new SIGAException (dirAux.getMsgError());
			}
			
			request.setAttribute("mensaje","messages.inserted.success");
			tx.commit();
			
			request.setAttribute("hiddenFrame", "1");
			request.setAttribute("modal","1");
			forward = "exito";
			
			//ELIMINAMOS EL DATABUCKUP
			if(request.getSession().getAttribute("DATABACKUP_CLIENTESFILTRO") != null)
				request.getSession().removeAttribute("DATABACKUP_CLIENTESFILTRO");
		
		}catch (Exception e) {
			throwExcp("messages.inserted.error",e,tx); 
		} 
		return forward;
	}
	
	
}
