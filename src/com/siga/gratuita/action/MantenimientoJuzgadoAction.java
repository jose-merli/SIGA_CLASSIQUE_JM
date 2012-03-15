package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsJuzgadoProcedimientoAdm;
import com.siga.beans.ScsJuzgadoProcedimientoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoJuzgadoForm;
import com.siga.gratuita.form.VolantesExpressForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoJuzgadoAction extends MasterAction {

	
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
			miForm = (MasterForm) formulario;
			
			String accion = miForm.getModo();

	  		// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("nuevoProcedimientoModal")){
				mapDestino = nuevoProcedimientoModal(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("insertarProcedimientoModal")){
				mapDestino = insertarProcedimientoModal(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("borrarProcedimiento")){
				mapDestino = borrarProcedimiento(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("recargarJuzgadoModal")){
				mapDestino = recargarJuzgadoModal(mapping, miForm, request, response);
			}else if (accion.equalsIgnoreCase("buscarJuzgado")){
			    mapDestino = buscarJuzgado(mapping, miForm, request, response);	
		    	
			}else if (accion.equalsIgnoreCase("getAjaxJuzgado")){
			    getAjaxJuzgado(mapping, miForm, request, response);
			    return null;
		    	
			} else {
				return super.executeInternal(mapping,
						      formulario,
						      request, 
						      response);
			}			
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		return mapping.findForward(mapDestino);
	}	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm (this.getUserBean(request));
			
			Vector vJuzgados = juzgadoAdm.busquedaJuzgados(miForm, user.getLocation());

			request.setAttribute("vJuzgados", vJuzgados);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		} 
		return "buscarPor";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String obtenerDatos(String modo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		
		try {
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			String	idInstitucionJuzgado = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idJuzgado = (String)miForm.getDatosTablaOcultos(0).get(1);

		ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm (this.getUserBean(request));
		Vector vJuzgados = juzgadoAdm.busquedaJuzgado(idInstitucionJuzgado, idJuzgado);

		if (vJuzgados.size() == 1) {
				Hashtable hashJuzgados = (Hashtable) vJuzgados.get(0);
				miForm.setDatos(hashJuzgados);
				hashJuzgados.remove("POBLACION");
				hashJuzgados.remove("PROVINCIA");
				request.getSession().setAttribute("DATABACKUP", hashJuzgados);
				
			}
			request.setAttribute("modo", modo);			
			
			//Realizo la busqueda de Procedimientos de Juzgados y los almaceno en el request:
			this.buscarProcedimientos(idInstitucionJuzgado, idJuzgado, request);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String obtenerDatos(String modo, MasterForm formulario, String	idInstitucionJuzgado, String	idJuzgado, HttpServletRequest request) throws SIGAException {
		try {

		ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm (this.getUserBean(request));
		Vector vJuzgados = juzgadoAdm.busquedaJuzgado(idInstitucionJuzgado, idJuzgado);

		if (vJuzgados.size() == 1) {
				Hashtable hashJuzgados = (Hashtable) vJuzgados.get(0);
				formulario.setDatos(hashJuzgados);
				hashJuzgados.remove("POBLACION");
				hashJuzgados.remove("PROVINCIA");
				request.getSession().setAttribute("DATABACKUP", hashJuzgados);
				
			}
			request.setAttribute("modo", modo);			
			
			//Realizo la busqueda de Procedimientos de Juzgados y los almaceno en el request:
			this.buscarProcedimientos(idInstitucionJuzgado, idJuzgado, request);
		
		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		return obtenerDatos(modo,formulario,request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		return obtenerDatos(modo,formulario,request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm (this.getUserBean(request));

			Hashtable hashJuzgadoOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashJuzgadoModificado = miForm.getDatos();
			boolean bVisibleModif =UtilidadesString.stringToBoolean(miForm.getVisible());
			if (bVisibleModif){
				miForm.setVisible(ClsConstants.DB_TRUE);
				
			}else{
				miForm.setVisible(ClsConstants.DB_FALSE);
			}
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			hashJuzgadoModificado.put(ScsJuzgadoBean.C_IDINSTITUCION,user.getLocation());

			String nombreOrig=UtilidadesHash.getString(hashJuzgadoOriginal,ScsJuzgadoBean.C_NOMBRE);
			String nombreModif=UtilidadesHash.getString(hashJuzgadoModificado,ScsJuzgadoBean.C_NOMBRE);
			String poblacionOrig=UtilidadesHash.getString(hashJuzgadoOriginal,ScsJuzgadoBean.C_IDPOBLACION);
			String poblacionModif=UtilidadesHash.getString(hashJuzgadoModificado,ScsJuzgadoBean.C_IDPOBLACION);
			String codProcuradorOrig=UtilidadesHash.getString(hashJuzgadoOriginal,ScsJuzgadoBean.C_CODPROCURADOR);
			String codProcuradorModif=UtilidadesHash.getString(hashJuzgadoModificado,ScsJuzgadoBean.C_CODPROCURADOR);
			//boolean bVisible  = UtilidadesString.stringToBoolean(miForm.getVisible());
			//String visibleOrig=UtilidadesHash.getString(hashJuzgadoOriginal,ScsJuzgadoBean.C_VISIBLE);
			String bVisibleOrig  = UtilidadesHash.getString(hashJuzgadoOriginal,ScsJuzgadoBean.C_VISIBLE);
			hashJuzgadoModificado.put(ScsJuzgadoBean.C_VISIBLE,miForm.getVisible());
			if(miForm.getPonerBaja() != null && miForm.getPonerBaja().equalsIgnoreCase("S"))
				hashJuzgadoModificado.put(ScsJuzgadoBean.C_FECHABAJA,"SYSDATE");
			else
				hashJuzgadoModificado.put(ScsJuzgadoBean.C_FECHABAJA,"");
		
			boolean modificar=true;

			ScsJuzgadoAdm admJuz = new ScsJuzgadoAdm(this.getUserBean(request));
//			if (!miForm.getCodigoExt().trim().equals("") && admJuz.comprobarCodigoExt(user.getLocation(),miForm.getIdJuzgado(),miForm.getCodigoExt())) {
//				// true=existe el codigo externo
//				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.codigoExtDuplicado"); 
//			}
			
			//si el nombre o la poblacion han cambiado, comprobamos que no este repetido
			if(!nombreOrig.equals(nombreModif) || !poblacionOrig.equals(poblacionModif)){
				modificar=ScsJuzgadoAdm.comprobarDuplicidad(user.getLocation(),poblacionModif,nombreModif);
			}
			if(modificar){
				tx.begin();
				juzgadoAdm.update(hashJuzgadoModificado, hashJuzgadoOriginal);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.juzgadoDuplicado"); 
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.updated.success", request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		try {
			request.setAttribute("modo", "nuevo");			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			boolean bVisibleModif =UtilidadesString.stringToBoolean(miForm.getVisible());
			if (bVisibleModif){
				miForm.setVisible(ClsConstants.DB_TRUE);
				
			}else{
				miForm.setVisible(ClsConstants.DB_FALSE);
			}
			
			ScsJuzgadoBean beanJuzgado = new ScsJuzgadoBean();
			ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm (this.getUserBean(request));
			
			String idInstitucion=user.getLocation();
			String idPoblacion=miForm.getIdPoblacion();
			String nombre=miForm.getNombre();
			String codigoExt=miForm.getCodigoExt();
			String codigoExt2=miForm.getCodigoExt2();
			
			beanJuzgado.setIdInstitucion(new Integer(idInstitucion));
			beanJuzgado.setIdPoblacion(idPoblacion);
			beanJuzgado.setNombre(nombre);
			beanJuzgado.setCodigoExt(codigoExt);
			beanJuzgado.setCodigoExt2(codigoExt2);
			
			beanJuzgado.setDireccion(miForm.getDireccion());
			beanJuzgado.setCodigoPostal(miForm.getCodigoPostal());
			beanJuzgado.setIdProvincia(miForm.getIdProvincia());
			beanJuzgado.setTelefono1(miForm.getTelefono1());
			beanJuzgado.setTelefono2(miForm.getTelefono2());
			beanJuzgado.setFax1(miForm.getFax1());			
			beanJuzgado.setIdJuzgado(juzgadoAdm.getNuevoIdJuzgado(idInstitucion));
			beanJuzgado.setVisible(miForm.getVisible());
			beanJuzgado.setMovil(miForm.getMovil());
			if(miForm.getPonerBaja() != null && miForm.getPonerBaja().equalsIgnoreCase("S"))
				beanJuzgado.setFechabaja("SYSDATE");
			else
				beanJuzgado.setFechabaja("");
			
			ScsJuzgadoAdm admJuz = new ScsJuzgadoAdm(this.getUserBean(request));
//			if (!codigoExt.trim().equals("") && admJuz.comprobarCodigoExt(idInstitucion,null,codigoExt)) {
//				// true=existe el codigo externo
//				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.codigoExtDuplicado"); 
//			}
			
			if(ScsJuzgadoAdm.comprobarDuplicidad(idInstitucion,idPoblacion,nombre)){
				tx.begin();
				juzgadoAdm.insert(beanJuzgado);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.juzgadoDuplicado"); 
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			tx = this.getUserBean(request).getTransaction();
						
			String	idInstitucionJuzgado = (String)miForm.getDatosTablaOcultos(0).get(0);
			Integer idJuzgado = new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, ScsJuzgadoBean.C_IDJUZGADO, idJuzgado);
			UtilidadesHash.set (claves, ScsJuzgadoBean.C_IDINSTITUCION, idInstitucionJuzgado);
			
			tx.begin();
			juzgadoAdm.delete(claves);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.gratuita.error.eliminarProcedimiento", e, tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}


	protected String nuevoProcedimientoModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			MantenimientoJuzgadoForm form = (MantenimientoJuzgadoForm) formulario; 
			
			ScsJuzgadoProcedimientoAdm adm = new ScsJuzgadoProcedimientoAdm (this.getUserBean(request));
			Vector v = adm.busquedaProcedimientosJuzgadoQueNoEstenEnJuzgado("" + this.getIDInstitucion(request), form.getIdJuzgado());
			request.setAttribute("PROCEDIMIENTOS", v);			
		}
		catch (Exception e) {
			
		}

		return "nuevoProcedimiento";
	}	

	protected String insertarProcedimientoModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		UsrBean user = null;
		String idInstitucionProcedimiento=null, idProcedimiento=null;
		
		try {
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = user.getTransaction();
			
			ScsJuzgadoProcedimientoAdm juzgadoProcedimientoAdm = new ScsJuzgadoProcedimientoAdm (this.getUserBean(request));
			ScsJuzgadoProcedimientoBean beanJuzgadoProcedimiento = new ScsJuzgadoProcedimientoBean();
			
			String idJuzgado = miForm.getIdJuzgado();
			String idInstitucion = user.getLocation();

			tx.begin();

			beanJuzgadoProcedimiento.setIdInstitucionJuzgado(new Integer(idInstitucion));
			beanJuzgadoProcedimiento.setIdJuzgado(new Integer(idJuzgado));

			String procedimiento[] = miForm.getProcedimiento().split("%");
			for (int i = 0; i < procedimiento.length; i++) {
				idInstitucionProcedimiento = procedimiento[i].substring(0,procedimiento[i].indexOf(","));
				idProcedimiento = procedimiento[i].substring(procedimiento[i].indexOf(",")+1);			

				beanJuzgadoProcedimiento.setIdProcedimiento(idProcedimiento);
				beanJuzgadoProcedimiento.setIdInstitucion(new Integer(idInstitucionProcedimiento));
				juzgadoProcedimientoAdm.insert(beanJuzgadoProcedimiento);
			}
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
	}	

	protected String borrarProcedimiento(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		UsrBean user = null;
		String idInstitucionProcedimiento=null, idProcedimiento=null, idInstitucion=null, idJuzgado=null;
		
		try {
			MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
			user = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = user.getTransaction();
			
			ScsJuzgadoProcedimientoAdm juzgadoProcedimientoAdm = new ScsJuzgadoProcedimientoAdm (this.getUserBean(request));
			ScsJuzgadoProcedimientoBean beanJuzgadoProcedimiento = new ScsJuzgadoProcedimientoBean();
			
			idInstitucionProcedimiento = miForm.getIdInstitucionProcedimiento();
			idProcedimiento = miForm.getIdProcedimiento();
			idInstitucion = miForm.getIdInstitucionJuzgado();	
			idJuzgado = miForm.getIdJuzgado();
			
			beanJuzgadoProcedimiento.setIdInstitucionJuzgado(new Integer(idInstitucion));
			beanJuzgadoProcedimiento.setIdJuzgado(new Integer(idJuzgado));
			beanJuzgadoProcedimiento.setIdProcedimiento(idProcedimiento);
			beanJuzgadoProcedimiento.setIdInstitucion(new Integer(idInstitucionProcedimiento));
						
			tx.begin();
			juzgadoProcedimientoAdm.delete(beanJuzgadoProcedimiento);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.deleted.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoRefresco("messages.deleted.success", request);
	}	
	
	private void buscarProcedimientos(String idInstitucionJuzgado, String idJuzgado, HttpServletRequest request) throws ClsExceptions, SIGAException {
		try {
			
			ScsJuzgadoProcedimientoAdm juzgadoProcedimientoAdm = new ScsJuzgadoProcedimientoAdm (this.getUserBean(request));
			Vector vProcedimientos = juzgadoProcedimientoAdm.busquedaProcedimientosJuzgado(idInstitucionJuzgado, idJuzgado);

			request.setAttribute("vProcedimientos", vProcedimientos);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		} 		
	}
		
		/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String recargarJuzgadoModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		MantenimientoJuzgadoForm miForm = (MantenimientoJuzgadoForm) formulario;
		String	idJuzgado = miForm.getIdJuzgado();
		String	idInstitucionJuzgado = this.getUserBean(request).getLocation();
		return obtenerDatos(modo,formulario,idInstitucionJuzgado,idJuzgado,request);
	}
	protected String buscarJuzgado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions 
	{
		try {
			MantenimientoJuzgadoForm miform = (MantenimientoJuzgadoForm)formulario;
			ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
			String codigoExt2 = miform.getCodigoExt2().toUpperCase();
			String where = " where upper(codigoext2) = upper ('"+codigoExt2+"')" +
					       " and idinstitucion="+this.getUserBean(request).getLocation();
			Vector resultadoJuzgado = juzgadoAdm.select(where);
			if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
				ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			}
			request.setAttribute("resultadoJuzgado",resultadoJuzgado);
			request.setAttribute("nombreObjetoDestino",miform.getNombreObjetoDestino());
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return "buscarJuzgado";
	}
	@SuppressWarnings("unchecked")
	protected void getAjaxJuzgado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		//Sacamos las guardias si hay algo selccionado en el turno
		String codigoExt2 = request.getParameter("codigoExtJuzgado");
		
		
		MantenimientoJuzgadoForm miform = (MantenimientoJuzgadoForm)formulario;
		ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
		String where = " where upper(codigoext2) = upper ('"+codigoExt2+"')" +
				       " and idinstitucion="+this.getUserBean(request).getLocation();
		Vector resultadoJuzgado = juzgadoAdm.select(where);
		String idJuzgado ="";
		if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
			ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			idJuzgado = juzgadoBean.getIdJuzgado().toString();
			miform.setIdJuzgado(idJuzgado);
		}
		
		List listaParametros = new ArrayList();
		listaParametros.add(idJuzgado);
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		
	}
}