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
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.form.BusquedaClientesFiltrosForm;

public class BusquedaClientesFiltrosAction extends MasterAction {

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
		
	protected Paginador buscarPaginador(MasterForm formulario, HttpServletRequest request) throws SIGAException		
	{		
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
			
			// casting del formulario
			BusquedaClientesFiltrosForm miFormulario = (BusquedaClientesFiltrosForm)formulario;
			String filtro=miFormulario.getIdFiltro();
			String nif=miFormulario.getNif();
			String ncoleg=miFormulario.getNumeroColegiado();
			String nombre=miFormulario.getNombrePersona();
			String apellido1=miFormulario.getApellido1();
			String apellido2=miFormulario.getApellido2();
			String idGuardia=miFormulario.getIdGuardia();
			String idTurno=miFormulario.getIdTurno();
			if (idTurno != null)
				idTurno = idTurno.substring(idTurno.indexOf(",")+1);
			
			String idTurnoCombo = (String)request.getParameter("identificador");
			if(idTurnoCombo != null && !idTurnoCombo.equals("")) {
				idTurno = idTurnoCombo.substring(idTurnoCombo.indexOf(",")+1);
			}

			String idGuardiaCombo = (String)request.getParameter("identificador2");
			if(idGuardiaCombo != null){
				idGuardia = idGuardiaCombo.substring(idGuardiaCombo.indexOf(",")+1);
			}
			
			String fecha=miFormulario.getFecha();
			if(fecha!=null && fecha.length()>0) fecha=GstDate.getApplicationFormatDate(user.getLanguage(),fecha);

			BusquedaClientesFiltrosAdm adm= new BusquedaClientesFiltrosAdm();
			adm.setFiltosPersona(nif,nombre,apellido1,apellido2);
			adm.setFiltroNColegiado(ncoleg);
			
			// busqueda de clientes
			Vector resultado = null;
			int idFiltro=0;
			try {
				idFiltro=Integer.parseInt(filtro);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String fechaFomateada = null;
			switch(idFiltro){
				case 1://Letrados de la Misma Guardia
				
					if(fecha==null ||fecha.equals(""))
						fechaFomateada = "sysdate";
					else
						fechaFomateada = GstDate.getFormatedDateShort("", fecha);
					resultado=adm.buscaLetradosMismaGuardia(idInstitucion, idTurno, idGuardia, fechaFomateada);
					break;
					
				case 2://Letrados de Todas las Guardias del Mismo Turno
					//resultado=adm.buscaLetradosTodasLasGuardias(idInstitucion, idTurno, idGuardia, fecha);
					resultado=adm.buscaLetradosTodasLasGuardiasDistintasAGuadria(idInstitucion, idTurno, idGuardia, fecha);
					break;
					
				case 3:case 6://Letrados del Turno
					
					if(fecha==null ||fecha.equals(""))
						fechaFomateada = "sysdate";
					else
						fechaFomateada = GstDate.getFormatedDateShort("", fecha);
					resultado=adm.buscaLetradosDelTurno(idInstitucion, idTurno,  fechaFomateada,idFiltro);
					break;
					
				case 4:case 7://Letrados de Todos los Turnos
					resultado=adm.buscaLetradosTodosLosTurnos(idInstitucion, idTurno, fecha);
					break;
					
				case 5:case 8://Censo Completo
					resultado=adm.buscaLetradosCensoCompleto(idInstitucion,  fecha);
					break;
					
				
					
				default://no hay filtro
					throw new ClsExceptions("La opción seleccionada no está implementada");
			}
			
			Paginador paginador = new Paginador (resultado);
			return paginador;
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return null;
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
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean(request));
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
			beanDir.setIdDireccion(direccionesAdm.getNuevoID(beanDir));
			resultOk=direccionesAdm.insert(beanDir);
			if (!resultOk){
				throw new SIGAException (direccionesAdm.getError());
			}
			// insertamos el tipo de dirección
			CenDireccionTipoDireccionAdm admTipoDir = new CenDireccionTipoDireccionAdm (this.getUserBean(request));
			CenDireccionTipoDireccionBean beanTipoDir=new CenDireccionTipoDireccionBean();
			beanTipoDir.setIdDireccion(beanDir.getIdDireccion());
			beanTipoDir.setIdInstitucion(beanDir.getIdInstitucion());
			beanTipoDir.setIdPersona(beanDir.getIdPersona());
			beanTipoDir.setIdTipoDireccion(new Integer(ClsConstants.TIPO_DIRECCION_GUARDIA));
			resultOk=admTipoDir.insert(beanTipoDir);
			if (!resultOk){
				throw new SIGAException (admTipoDir.getError());
			}
			
			request.setAttribute("mensaje","messages.inserted.success");
			tx.commit();
			
			request.setAttribute("hiddenFrame", "1");
			request.setAttribute("modal","1");
			forward = "exito";
			
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
			CenDireccionesBean beanDir  = new CenDireccionesBean ();
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm (this.getUserBean(request));
			beanDir.setIdInstitucion(formulario.getIdInstitucion());
			beanDir.setIdPersona(formulario.getIdPersona());
			beanDir.setIdDireccion(formulario.getIdDireccion());
			beanDir.setFax1(formulario.getFax1());
			beanDir.setFax2(formulario.getFax2());
			beanDir.setMovil(formulario.getMovil());
			beanDir.setTelefono1(formulario.getTelefono1());
			beanDir.setTelefono2(formulario.getTelefono2());
			
			beanDir.setOriginalHash((Hashtable)request.getSession().getAttribute("DATABACKUP_CLIENTESFILTRO"));
			// Actualizamos el registro de la dirección de guardia
			beanDir.setIdDireccion(formulario.getIdDireccion());
			resultOk=direccionesAdm.update(direccionesAdm.beanToHashTable(beanDir),beanDir.getOriginalHash());
			if (!resultOk) {
				throw new SIGAException (direccionesAdm.getError());
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
