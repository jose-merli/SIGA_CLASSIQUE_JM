//VERSIONES:
//ruben.fernandez Creacion: 05/04/2005 
//

package com.siga.gratuita.action;



import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ScsAcreditacionAdm;
import com.siga.beans.ScsAcreditacionProcedimientoAdm;
import com.siga.beans.ScsAcreditacionProcedimientoBean;
import com.siga.beans.ScsProcedimientosAdm;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoProcedimientosForm;


public class MantenimientoProcedimientosAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#executeInternal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

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
					mapDestino = abrir(mapping, miForm, request, response);						
				}
				else if (accion.equalsIgnoreCase("nuevoAcreditacion")){
					mapDestino = nuevoAcreditacion(mapping, miForm, request, response);
				}
				else if (accion.equalsIgnoreCase("insertarAcreditacion")){
					mapDestino = insertarAcreditacion(mapping, miForm, request, response);
				}
				else if (accion.equalsIgnoreCase("modificarAcreditacion")){
					mapDestino = modificarAcreditacion(mapping, miForm, request, response);
				}
				else {
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
			return mapping.findForward(mapDestino);
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 

	}
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		return "inicio";
	}


	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		//recogemos el UsrBean
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//recogemos el formulario
		MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
		
		//recogemos el campo oculto de fila seleccionada, que será el idProcedimiento
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		String idProcedimiento = "";

		String refresco = miform.getRefresco();  
		{	// Redireccionamos a la edicion de acreditacion si es necesario
			if ((refresco == null) || (refresco.equals(""))){ 
				if ((ocultos != null) && (ocultos.size() > 3) && ((String)ocultos.get(3)).equalsIgnoreCase("detalleAcreditacion")) {
					return this.editarAcreditacion(mapping, formulario, request, response);
				}
			}
		}

		if ((ocultos == null) || (refresco != null) && (!refresco.equals(""))){
			idProcedimiento = miform.getIdProcedimiento();
		}
		else idProcedimiento = (String)ocultos.get(0);
		

		//consultamos en bd el registro a editar
		Hashtable procedimientoOld = new Hashtable();
		ScsProcedimientosAdm procedimientoAdm = new ScsProcedimientosAdm(this.getUserBean(request));
		ScsProcedimientosBean procedimientoBean = new ScsProcedimientosBean();
		
		Vector vAcreditaciones = null;
		try{
			String condicion = " where " + ScsProcedimientosBean.C_IDINSTITUCION + "=" + (String)usr.getLocation() + 
								" AND "  + ScsProcedimientosBean.C_IDPROCEDIMIENTO + "=" + idProcedimiento + " ";
			procedimientoBean = (ScsProcedimientosBean)((Vector)procedimientoAdm.select(condicion)).get(0);

			Integer idInstitucion = this.getIDInstitucion(request);
			ScsAcreditacionAdm acreAdm = new ScsAcreditacionAdm (this.getUserBean(request));
			vAcreditaciones = acreAdm.getAcreditacionDeProcedimiento(idInstitucion, idProcedimiento);

		}catch(Exception e){
			throwExcp("error.messages.notedited",e,null);
		}
		request.setAttribute("resultado",procedimientoBean.getOriginalHash());
		//pasamos el databackup or sesion para posibles futuras actualizaciones
		request.getSession().setAttribute("DATABACKUP",procedimientoBean.getOriginalHash());
		request.setAttribute("acreditaciones", vAcreditaciones);
		return "mantenimiento";
	}

	/**
	 * Metodo que implementa el modo insertar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		//recogemos el usrBean
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//recogemos el formulario
		MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
		
		//construimos el hash a insertar
		Hashtable procedimientoNuevo = new Hashtable();
		
		//variable para saber si el insert ha ido bien o no
		boolean ok = false;
		

		try {
			
			//administrador de la tabla
			ScsProcedimientosAdm procedimientoAdm = new ScsProcedimientosAdm(this.getUserBean(request));
			if (!miform.getCodigo().trim().equals("") && procedimientoAdm.comprobarCodigoExt(usr.getLocation(),miform.getIdProcedimiento(),miform.getCodigo())) {
				// true=existe el codigo externo
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.codigoExtDuplicado"); 
			}

			//construimos el hash a insertar
			procedimientoNuevo = (Hashtable)miform.getDatos();
			
			//preparamos la hashtable
			Integer idInstitucionP = this.getIDInstitucion(request);
			procedimientoNuevo = (Hashtable)procedimientoAdm.prepararInsert(procedimientoNuevo, idInstitucionP);
			procedimientoNuevo.put(ScsProcedimientosBean.C_IDINSTITUCION,(String)usr.getLocation());
			procedimientoNuevo.put(ScsProcedimientosBean.C_IDJURISDICCION,miform.getJurisdiccion());

			if (miform.getComplemento()!=null && miform.getComplemento().equals("1"))
			    UtilidadesHash.set(procedimientoNuevo, ScsProcedimientosBean.C_COMPLEMENTO, ClsConstants.DB_TRUE);
			else
			    UtilidadesHash.set(procedimientoNuevo, ScsProcedimientosBean.C_COMPLEMENTO, ClsConstants.DB_FALSE);
			
			if (miform.getVigente()!=null && miform.getVigente().equals("1"))
			    UtilidadesHash.set(procedimientoNuevo, ScsProcedimientosBean.C_VIGENTE, ClsConstants.DB_TRUE);
			else
			    UtilidadesHash.set(procedimientoNuevo, ScsProcedimientosBean.C_VIGENTE, ClsConstants.DB_FALSE);
			

			request.setAttribute("idProcedimiento",(String) procedimientoNuevo.get(ScsProcedimientosBean.C_IDPROCEDIMIENTO));
			request.setAttribute("idInstitucionProcedimiento",idInstitucionP.toString());
			
			//insertamos
			if (!procedimientoAdm.insert(procedimientoNuevo)) {
				throw new SIGAException (procedimientoAdm.getError());
			}
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		//devolvemos el mapping dependiendo de si todo ha ido bien o no
		request.setAttribute("mensaje","messages.inserted.success");
		return "exitoInsercionProcedimiento"; 		
		
	}

		
	/**
	 * Metodo que implementa el modo modificar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		//recogemos el formulario
		MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		//recogemos el idProcedimiento
		String idProcedimiento = (String)miform.getIdProcedimiento();
		
		//variable para saber si el modificado ha ido bien o no
		boolean ok = false;
		
		//consultamos en bd el registro a modificar
		Hashtable procedimientoOld = new Hashtable();

		ScsProcedimientosAdm procedimientoAdm = new ScsProcedimientosAdm(this.getUserBean(request));

		/*ScsProcedimientosBean procedimientoBean = new ScsProcedimientosBean();*/
		try{
			if (!miform.getCodigo().trim().equals("") && procedimientoAdm.comprobarCodigoExt(usr.getLocation(),miform.getIdProcedimiento(),miform.getCodigo())) {
				// true=existe el codigo externo
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.codigoExtDuplicado"); 
			}

			String condicion = " where " + ScsProcedimientosBean.C_IDPROCEDIMIENTO + "=" + idProcedimiento + " ";
			//procedimientoBean = (ScsProcedimientosBean)((Vector)procedimientoAdm.select(condicion)).get(0);
			
			//procedimientoOld = (Hashtable)procedimientoBean.getOriginalHash().clone();
			procedimientoOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			//construimos el nuevo procedimiento
			boolean checkVigente  = UtilidadesString.stringToBoolean(miform.getVigente());
			Hashtable tramoNew = (Hashtable)procedimientoOld.clone();
			tramoNew.put(ScsProcedimientosBean.C_NOMBRE, (String)miform.getNombre());
			tramoNew.put(ScsProcedimientosBean.C_PRECIO, (String)miform.getPrecio());
			//tramoNew.put(ScsProcedimientosBean.C_PUNTOS, (String)miform.getPuntos());// Se ha ocultado el campo puntos
			tramoNew.put(ScsProcedimientosBean.C_CODIGO, (String)miform.getCodigo());
			tramoNew.put(ScsProcedimientosBean.C_IDJURISDICCION, miform.getJurisdiccion());

			if (miform.getComplemento()!=null && miform.getComplemento().equals("1"))
			    UtilidadesHash.set(tramoNew, ScsProcedimientosBean.C_COMPLEMENTO, ClsConstants.DB_TRUE);
			else
			    UtilidadesHash.set(tramoNew, ScsProcedimientosBean.C_COMPLEMENTO, ClsConstants.DB_FALSE);
			
			if (checkVigente){
				UtilidadesHash.set(tramoNew, ScsProcedimientosBean.C_VIGENTE, ClsConstants.DB_TRUE);
			}else{
				UtilidadesHash.set(tramoNew, ScsProcedimientosBean.C_VIGENTE, ClsConstants.DB_FALSE);
			}
			//modificamos en bd
			ok = procedimientoAdm.update(tramoNew, procedimientoOld);
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		if (ok) return exitoModal("messages.updated.success",request);
		else throw new SIGAException (procedimientoAdm.getError());
	}

	/**
	 * Metodo que implementa el modo nuevo 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			MantenimientoProcedimientosForm miForm = (MantenimientoProcedimientosForm)formulario;
			request.getSession().setAttribute("modo","nuevo");
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		
		return "mantenimiento";
	}

	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		//Recogemos de sesion el UsrBean
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		MantenimientoProcedimientosForm miForm = (MantenimientoProcedimientosForm)formulario;
		
		try {
			//hacemos la consulta de los procediminetos de la institucion 
			ScsProcedimientosAdm procedimientosAdm = new ScsProcedimientosAdm(this.getUserBean(request));
			String condicion = " select " + ScsProcedimientosBean.C_NOMBRE + "," + ScsProcedimientosBean.C_PRECIO + "," + ScsProcedimientosBean.C_CODIGO + "," + ScsProcedimientosBean.C_PUNTOS + "," + ScsProcedimientosBean.C_IDPROCEDIMIENTO + " " +
								" from " + ScsProcedimientosBean.T_NOMBRETABLA + " " +
								" where " + ScsProcedimientosBean.C_IDINSTITUCION + "=" + (String)usr.getLocation(); 

			// Ahora se anhade el criterio de búsqueda
			if ((miForm.getCodigoBusqueda()!= null) && (!miForm.getCodigoBusqueda().toString().equals("")))	condicion += " and "  + ComodinBusquedas.prepararSentenciaCompleta(miForm.getCodigoBusqueda().trim(),ScsProcedimientosBean.C_CODIGO);
			if ((miForm.getNombre()!= null) && (!miForm.getNombre().toString().equals("")))	condicion += " and "+ComodinBusquedas.prepararSentenciaCompleta(miForm.getNombre().trim(),ScsProcedimientosBean.C_NOMBRE);
			
			// Por último el critero de búsqueda
			condicion += " ORDER BY 1 ";
			
			Vector resultado = (Vector)procedimientosAdm.selectGenerico(condicion);
			
			//pasamos los resultados por la request
			request.setAttribute("resultado",resultado);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		//mapping normal
		return "listarProcedimientos";
	}

	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		//recogemos el usrBean
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//recogemos el formulario
		MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
		
		//recogemos la fila seleccionada para borrar
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		
		if ((ocultos != null) && (ocultos.size() > 3) && ((String)ocultos.get(3)).equalsIgnoreCase("detalleAcreditacion")) {
			return this.borrarAcreditacion(mapping, formulario, request, response);
		}
		
		//construimos la hash del registro a borrar
		Hashtable aBorrar = new Hashtable();
		aBorrar.put(ScsProcedimientosBean.C_IDPROCEDIMIENTO,(String)ocultos.get(0));
		aBorrar.put(ScsProcedimientosBean.C_IDINSTITUCION,(String)usr.getLocation());
		//variable para saber si el borrado ha ido bien
		boolean ok = false;
		
		try
		{
			//borramos
			ScsProcedimientosAdm procedimientoAdm = new ScsProcedimientosAdm (this.getUserBean(request));
			ok = procedimientoAdm.delete(aBorrar);
		}
		catch(Exception e){
			throwExcp("messages.gratuita.error.eliminarProcedimiento",e,null);
		}
		//si ha ido bien refrescamos y devolvemos un mensage de exito
		if (ok) return exitoRefresco("messages.deleted.success",request);
		//si ha fallado habría saltado el thowExcp, pero en todo caso , 
		//sino ha saltado el error porque no ha encontrado el idProcedimientos
		//devolvemos mensage de que no se ha borrado
		else return exitoRefresco("messages.gratuita.error.eliminarProcedimiento",request);
		
	}
	
	protected String nuevoAcreditacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
			Integer idInstitucion = this.getIDInstitucion(request);
	
			request.setAttribute("idInstitucion", idInstitucion);
			request.setAttribute("idProcedimiento", miform.getIdProcedimiento());
			request.setAttribute("modo", miform.getModo());
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "nuevoAcreditacion";
	}
	
	protected String insertarAcreditacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
			ScsAcreditacionProcedimientoBean bean = new ScsAcreditacionProcedimientoBean ();
			bean.setIdAcreditacion(miform.getIdAcreditacion());
			bean.setIdInstitucion(this.getIDInstitucion(request));
			bean.setIdProcedimiento(miform.getIdProcedimiento());
			bean.setPorcentaje(miform.getPorcentaje());
			ScsAcreditacionProcedimientoAdm adm = new ScsAcreditacionProcedimientoAdm (this.getUserBean(request));
			
			if (adm.insert(bean)) {
				return exitoRefresco("messages.inserted.success",request);
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return exitoModal("messages.insert.success",request);		
	}

	protected String borrarAcreditacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
			Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
			ScsAcreditacionProcedimientoBean bean = new ScsAcreditacionProcedimientoBean ();
			bean.setIdAcreditacion(new Integer((String)ocultos.get(0)));
			bean.setIdInstitucion(new Integer((String)ocultos.get(1)));
			bean.setIdProcedimiento((String)ocultos.get(2));
			ScsAcreditacionProcedimientoAdm adm = new ScsAcreditacionProcedimientoAdm (this.getUserBean(request));
			if (adm.delete(bean)) {
				return exitoRefresco("messages.deleted.success",request);
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
 
		return exitoRefresco("messages.gratuita.error.eliminarProcedimiento",request);
	}
	
	protected String editarAcreditacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;

			Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
			ScsAcreditacionProcedimientoBean bean = new ScsAcreditacionProcedimientoBean ();
			
			Hashtable datos = new Hashtable();
			UtilidadesHash.set (datos,ScsAcreditacionProcedimientoBean.C_IDACREDITACION, new Integer((String)ocultos.get(0)));
			UtilidadesHash.set (datos,ScsAcreditacionProcedimientoBean.C_IDINSTITUCION, new Integer((String)ocultos.get(1)));
			UtilidadesHash.set (datos,ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO, new Integer((String)ocultos.get(2)));
			ScsAcreditacionProcedimientoAdm adm = new ScsAcreditacionProcedimientoAdm (this.getUserBean(request));
			Vector v = adm.select(datos);

//			request.setAttribute("datosAcreditacion", v);
			request.getSession().setAttribute("datosAcreditacionOriginal", v);
			request.setAttribute("idInstitucion", new Integer((String)ocultos.get(1)));
			request.setAttribute("modo", miform.getModo());
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "nuevoAcreditacion";
	}

	protected String modificarAcreditacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			MantenimientoProcedimientosForm miform = (MantenimientoProcedimientosForm)formulario;
	
			Vector v = (Vector) request.getSession().getAttribute("datosAcreditacionOriginal");
			ScsAcreditacionProcedimientoBean bean = null;
			if ((v != null) && (v.size() > 0)) {
				bean = (ScsAcreditacionProcedimientoBean) v.get(0);
				bean.setPorcentaje(miform.getPorcentaje());
			
				ScsAcreditacionProcedimientoAdm adm = new ScsAcreditacionProcedimientoAdm(this.getUserBean(request));			
				if (!adm.update(bean)) {
					throw new SIGAException (adm.getError());
				}
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return exitoModal("messages.updated.success",request);
	}
}
