package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsComisariaAdm;
import com.siga.beans.ScsComisariaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoComisariaForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoComisariaAction extends MasterAction {
	

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
				}else if (accion.equalsIgnoreCase("buscarComisaria")){
				    mapDestino = buscarComisaria(mapping, miForm, request, response);
				    
				}else if (accion.equalsIgnoreCase("getAjaxComisaria")){
					getAjaxComisaria(mapping, miForm, request, response);
				    return null;	
				    
				}else if (accion.equalsIgnoreCase("getAjaxComisaria2")){
					getAjaxComisaria2(mapping, miForm, request, response);
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
			MantenimientoComisariaForm miForm = (MantenimientoComisariaForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			ScsComisariaAdm comisariaAdm = new ScsComisariaAdm (this.getUserBean(request));
			
			Vector vComisarias = comisariaAdm.busquedaComisarias(miForm, user.getLocation());

			request.setAttribute("vComisarias", vComisarias);
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
			MantenimientoComisariaForm miForm = (MantenimientoComisariaForm) formulario;
			
			String	idInstitucionComisaria = (String)miForm.getDatosTablaOcultos(0).get(0);
			String	idComisaria = (String)miForm.getDatosTablaOcultos(0).get(1);
			
			ScsComisariaAdm comisariaAdm = new ScsComisariaAdm (this.getUserBean(request));
			
			Vector vComisarias = comisariaAdm.busquedaComisaria(idInstitucionComisaria, idComisaria);
			if (vComisarias.size() == 1) {
				Hashtable hashComisarias = (Hashtable) vComisarias.get(0);
				miForm.setDatos(hashComisarias);
				hashComisarias.remove("POBLACION");
				hashComisarias.remove("PROVINCIA");
				request.getSession().setAttribute("DATABACKUP", hashComisarias);
			}
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		return this.obtenerDatos(modo,formulario,request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		return this.obtenerDatos(modo,formulario,request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		
		try {
			MantenimientoComisariaForm miForm = (MantenimientoComisariaForm) formulario;
			tx = this.getUserBean(request).getTransaction();
			ScsComisariaAdm comisariaAdm = new ScsComisariaAdm (this.getUserBean(request));

			Hashtable hashComisariaOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashComisariaModificado = miForm.getDatos(); 

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			hashComisariaModificado.put(ScsComisariaBean.C_IDINSTITUCION,user.getLocation());
			if(miForm.getPonerBaja() != null && miForm.getPonerBaja().equalsIgnoreCase("S"))
				hashComisariaModificado.put(ScsComisariaBean.C_FECHABAJA,"SYSDATE");
			else
				hashComisariaModificado.put(ScsComisariaBean.C_FECHABAJA,"");

			String nombreOrig=UtilidadesHash.getString(hashComisariaOriginal,ScsComisariaBean.C_NOMBRE);
			String nombreModif=UtilidadesHash.getString(hashComisariaModificado,ScsComisariaBean.C_NOMBRE);
			String poblacionOrig=UtilidadesHash.getString(hashComisariaOriginal,ScsComisariaBean.C_IDPOBLACION);
			String poblacionModif=UtilidadesHash.getString(hashComisariaModificado,ScsComisariaBean.C_IDPOBLACION);
			
			boolean modificar=true;
			//si el nombre o la poblacion han cambiado, comprobamos que no este repetido
			if(!nombreOrig.equals(nombreModif) || !poblacionOrig.equals(poblacionModif)){
				modificar=ScsComisariaAdm.comprobarDuplicidad(user.getLocation(),poblacionModif,nombreModif);
			}
			if(modificar){
				tx.begin();
				comisariaAdm.update(hashComisariaModificado, hashComisariaOriginal);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.comisariaDuplicada"); 
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
		String modo = "nuevo";
		try {
			request.setAttribute("modo", modo);			
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
			MantenimientoComisariaForm miForm = (MantenimientoComisariaForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsComisariaBean beanComisaria = new ScsComisariaBean();
			ScsComisariaAdm comisariaAdm = new ScsComisariaAdm (this.getUserBean(request));
			
			String idInstitucion=user.getLocation();
			String idPoblacion=miForm.getIdPoblacion();
			String nombre=miForm.getNombre();
			beanComisaria.setIdInstitucion(new Integer(idInstitucion));
			beanComisaria.setIdPoblacion(idPoblacion);
			beanComisaria.setNombre(nombre);
			beanComisaria.setDireccion(miForm.getDireccion());
			beanComisaria.setCodigoPostal(miForm.getCodigoPostal());
			beanComisaria.setIdProvincia(miForm.getIdProvincia());
			beanComisaria.setTelefono1(miForm.getTelefono1());
			beanComisaria.setTelefono2(miForm.getTelefono2());
			beanComisaria.setFax1(miForm.getFax1());			
			beanComisaria.setIdComisaria(comisariaAdm.getNuevoIdComisaria(idInstitucion));
			beanComisaria.setCodigoExt(miForm.getCodigoExt());
			if(miForm.getPonerBaja() != null && miForm.getPonerBaja().equalsIgnoreCase("S"))
				beanComisaria.setFechabaja("SYSDATE");
			else
				beanComisaria.setFechabaja("");
			
			if(ScsComisariaAdm.comprobarDuplicidad(idInstitucion,idPoblacion,nombre)){
				tx.begin();
				comisariaAdm.insert(beanComisaria);
				tx.commit();
			}else{
				throw new SIGAException("gratuita.mantenimientoTablasMaestra.mensaje.comisariaDuplicada"); 
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
			MantenimientoComisariaForm miForm = (MantenimientoComisariaForm) formulario;
			tx = this.getUserBean(request).getTransaction();
						
			String	idInstitucionComisaria = (String)miForm.getDatosTablaOcultos(0).get(0);
			Integer idComisaria = new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			ScsComisariaAdm comisariaAdm = new ScsComisariaAdm(this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, ScsComisariaBean.C_IDCOMISARIA, idComisaria);
			UtilidadesHash.set (claves, ScsComisariaBean.C_IDINSTITUCION, idInstitucionComisaria);
			
			tx.begin();
			comisariaAdm.delete(claves);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.gratuita.error.eliminarProcedimiento", e, tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
	protected String buscarComisaria (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions 
	{
		String comisariaID="";
		try {
			MantenimientoComisariaForm miform = (MantenimientoComisariaForm)formulario;
			ScsComisariaAdm comisariaAdm= new ScsComisariaAdm(this.getUserBean(request));
			String codigoExt = miform.getCodigoExtBusqueda().toUpperCase();
			
			String sql = "Select C.IDCOMISARIA || ',' || C.IDINSTITUCION as IDCOMISARIA from scs_comisaria c "+ 
						 " where upper(c.codigoext) = upper ('"+codigoExt+"')" +
					     " and c.idinstitucion="+this.getUserBean(request).getLocation();
			Vector resultadoComisaria = comisariaAdm.selectGenerico(sql);
			if (resultadoComisaria!=null && resultadoComisaria.size()>0) {
				 comisariaID =  (String)((Hashtable)resultadoComisaria.get(0)).get("IDCOMISARIA");
			}
			request.setAttribute("resultadoComisaria",comisariaID);
			request.setAttribute("nombreObjetoDestino",miform.getNombreObjetoDestino());
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		//System.out.println("Dentro del actio"+comisariaID);
		return "buscarComisaria";
	}
	
	@SuppressWarnings("unchecked")
	protected void getAjaxComisaria (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
		{
		String codigoExt = "";
		
		String idCombo = request.getParameter("idCombo");
		
		String sql = "SELECT C.CODIGOEXT FROM SCS_COMISARIA C " + 
			" WHERE UPPER(C.IDCOMISARIA||','||C.IDINSTITUCION) = UPPER('"+idCombo+"')";
			
		ScsComisariaAdm comisariaAdm= new ScsComisariaAdm(this.getUserBean(request));		
		Vector resultadoComisaria = comisariaAdm.selectGenerico(sql);
		
		if (resultadoComisaria!=null && resultadoComisaria.size()>0)
			codigoExt =  (String)((Hashtable)resultadoComisaria.get(0)).get("CODIGOEXT");
				
		JSONObject json = new JSONObject();		
		json.put("codigoExt", codigoExt);
		
		 response.setContentType("text/x-json;charset=ISO-8859-15");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 			
	}
	
	@SuppressWarnings("unchecked")
	protected void getAjaxComisaria2 (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
		{
		String codigoExt = "";
		
		String idCombo = request.getParameter("idCombo");
		
		String sql = "SELECT SC.CODIGOEXT FROM SCS_COMISARIA SC WHERE SC.IDCOMISARIA = '"+idCombo+"'"+
				"AND SC.IDINSTITUCION="+this.getUserBean(request).getLocation();
			
		ScsComisariaAdm comisariaAdm= new ScsComisariaAdm(this.getUserBean(request));		
		Vector resultadoComisaria = comisariaAdm.selectGenerico(sql);
		
		if (resultadoComisaria!=null && resultadoComisaria.size()>0)
			codigoExt =  (String)((Hashtable)resultadoComisaria.get(0)).get("CODIGOEXT");
				
		JSONObject json = new JSONObject();		
		json.put("codigoExt", codigoExt);
		
		 response.setContentType("text/x-json;charset=ISO-8859-15");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 			
	}		
}