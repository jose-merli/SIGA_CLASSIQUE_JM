/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.censo.form.ComponentesJuridicosForm;
import com.siga.censo.form.DatosGeneralesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComponentesJuridicosAction extends MasterAction{


	
	public ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

	String mapDestino = "exception";
	MasterForm miForm = null;
	
	try { 
		do {
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				
				String accion=miForm.getModo();
				if (accion==null){
					accion = "";
				}
				if (accion.equalsIgnoreCase("buscarNIF")){
					// abrirAvanzadaConParametros
					mapDestino = buscarNIF(mapping, miForm, request, response);
				}else if ( accion.equalsIgnoreCase("existeOtraSociedad")){
					ClsLogging.writeFileLog("DATOS NO COLEGIALES:getIdenHistorico", 10);
					mapDestino = existeOtraSociedad(mapping, miForm, request, response,0,"");
				}else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			}
		} while (false);
	
	// Redireccionamos el flujo a la JSP correspondiente
		/*if (mapDestino == null)	{ 
			throw new ClsExceptions("El ActionMapping no puede ser nulo");
		}*/
		
		return mapping.findForward(mapDestino);
		
	} catch (SIGAException es) {
		throw es;
	} catch (Exception e) {
		throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
	}
}
	
	@SuppressWarnings("unchecked")
	protected String existeOtraSociedad (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response, int bandera,String institucion) throws ClsExceptions, SIGAException ,Exception
			{
		String numero=null;
		boolean actualizable= true;
		Long idPersona = null;
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		ComponentesJuridicosForm miForm = (ComponentesJuridicosForm) formulario;
		Hashtable hashOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
		CenComponentesBean beanComponentes = new CenComponentesBean();
		String idClientePersona =(String) request.getParameter("idClientePersona");
		String insti=(String) request.getParameter("idInstitucion");
		if(request.getParameter("idPersona")!=null)
		 idPersona =new Long((String)request.getParameter("idPersona"));
		
		//Long idPersona =miForm.getIdPersona();		
		if(idClientePersona== null || idClientePersona.trim().equals("")){
			idClientePersona = UtilidadesHash.getString(hashOriginal, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA).toString();
			idPersona = UtilidadesHash.getLong(hashOriginal, CenComponentesBean.C_IDPERSONA);
		}	
		//Comprobamos que el cliente no sea componente de otra sociedad en la qu el campo SOCIEDAD sea = 1(que actue como sociedad)
		String where = " where CEN_CLIENTE_IDINSTITUCION ="+insti+
		   			   "   and CEN_CLIENTE_IDPERSONA = "+idClientePersona
		             + "   and "+CenComponentesBean.C_SOCIEDAD +"="+ClsConstants.DB_TRUE
					+ "   and ("+CenComponentesBean.C_FECHABAJA +" is null OR FECHABAJA > SYSDATE)";
		CenComponentesAdm componentesAdm = new CenComponentesAdm(user);
		Vector v2 = componentesAdm.select(where);
		
		if ((v2 != null) && (v2.size() > 0)) {
			
			CenComponentesBean beancomponentes=(CenComponentesBean) v2.get(0);
			CenPersonaAdm admpersona=new CenPersonaAdm(this.getUserBean(request));
			String[] nifaux=new String[1];
			numero=admpersona.obtenerNIF(beancomponentes.getIdPersona().toString());
			actualizable= false;
			if(idPersona.equals(beancomponentes.getIdPersona()))
				actualizable=true;
				
		}

		JSONObject json = new JSONObject();
		if(actualizable){
			json.put("exite", "N");
			json.put("nifSociedad", "");
		}else{
			json.put("exite", "S");
			json.put("nifSociedad", numero);
		}
		
		
		 //response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
		return null;//"completado";
		
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String rc = "";
		
		try{
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();

			String accion = (String)request.getParameter("accion");
			
			// Vemos si venimos de nueva sociedad o nuevo no colegiado de tipo personal:
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
				 (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals("") )) {
				request.setAttribute("modoVolver",accion);
				return "clienteNoExiste";
			}
			
			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion"));
			
			//CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request));
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
			CenComponentesAdm componentesAdm = new CenComponentesAdm(user);
			
			Vector v = null;
			Vector vCliente = null;
			String nombre = null;
			String numero = "";
			boolean incluirHistorico = true; // Recuperamos siempre todos los componentes y los gestionamos en la jsp
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (user);
			CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			numero = colegiadoAdm.getIdentificadorColegiado(bean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
			
			v = componentesAdm.selectComponentes(idPersona,idInstitucionPersona, incluirHistorico);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idInstitucion", idInstitucionPersona);
			request.setAttribute("accion", accion);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("vDatos", v);

			// Para verificar si es cliente jurido (esdecir si tiene CIF)
			CenPersonaBean beanPersona = personaAdm.getIdentificador(idPersona);
			if (beanPersona != null) { 
				request.setAttribute(CenPersonaBean.C_IDTIPOIDENTIFICACION, beanPersona.getIdTipoIdentificacion());
				
				// obtengo los datos de nocolegiado
				Hashtable hashNoCol = new Hashtable();
				hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucionPersona);
				hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
				CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
				v = nocolAdm.selectByPK(hashNoCol);
				if (v!=null && v.size()>0) {
					CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
					if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
						// pongo el atributo cuando no es tipo personal (Osea es sociedad)
						request.setAttribute(CenNoColegiadoBean.C_TIPO, nocolBean.getTipo());
					}
				}
			}
			
			rc = "inicio";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return rc;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String modo = "editar";
		try {
		    UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			Vector ocultos = new Vector();
			ComponentesJuridicosForm form= (ComponentesJuridicosForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			String accion = (String)request.getParameter("accion");		
			Integer idComponente = Integer.valueOf((String)ocultos.elementAt(0));
			CenComponentesAdm componentesAdm = new CenComponentesAdm(this.getUserBean(request));
			Hashtable hash = componentesAdm.selectComponentes(form.getIdPersona(), form.getIdInstitucion(), idComponente);
			
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (user);
			CenColegiadoBean beanColegiado = colegiadoAdm.getDatosColegiales(UtilidadesHash.getLong(hash, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA), UtilidadesHash.getInteger(hash, CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION));
			if (hash.get("IDTIPOCOLEGIO").equals("1")){
				UtilidadesHash.set(hash, "_NUMERO_COLEGIADO_", colegiadoAdm.getIdentificadorColegiado(beanColegiado));
			}else{
				UtilidadesHash.set(hash, "_NUMERO_COLEGIADO_", hash.get("NUMCOLEGIADO").toString());
			}
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("modoConsulta", modo);		
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String modo = "ver";;
		try{
		    UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			Vector ocultos = new Vector();
			ComponentesJuridicosForm form= (ComponentesJuridicosForm) formulario;
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPersona = form.getIdPersona();
			Integer idInstitucionPersona = form.getIdInstitucion();
			String accion = (String)request.getParameter("accion");		
			Integer idComponente = Integer.valueOf((String)ocultos.elementAt(0));
			CenComponentesAdm componentesAdm = new CenComponentesAdm(this.getUserBean(request));
			Hashtable hash = componentesAdm.selectComponentes(idPersona,idInstitucionPersona,idComponente);

			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (user);
			CenColegiadoBean beanColegiado = colegiadoAdm.getDatosColegiales(UtilidadesHash.getLong(hash, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA), UtilidadesHash.getInteger(hash, CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION));
			//UtilidadesHash.set(hash, "_NUMERO_COLEGIADO_", colegiadoAdm.getIdentificadorColegiado(beanColegiado));
			if (CenComponentesBean.C_IDTIPOCOLEGIO.equals("5")){
				UtilidadesHash.set(hash, "_NUMERO_COLEGIADO_", colegiadoAdm.getIdentificadorColegiado(beanColegiado));
			}else{
				UtilidadesHash.set(hash, "_NUMERO_COLEGIADO_", hash.get("NUMCOLEGIADO").toString());
			}

			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("accion", accion);	
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("modoConsulta", modo);			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String modo = "nuevo";
 		try
		{
 			ComponentesJuridicosForm miForm = (ComponentesJuridicosForm) formulario;
			request.setAttribute("modoConsulta", modo);			
			request.setAttribute("idPersona", miForm.getIdPersona());			
			request.setAttribute("idInstitucion", miForm.getIdInstitucion());			
			request.setAttribute("numero", request.getParameter("numeroUsuario"));
			request.setAttribute("nombrePersona", request.getParameter("nombreUsuario"));
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return modo;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		UsrBean user = null;

		try {
			user = this.getUserBean(request);
			tx= user.getTransaction();
			
			ComponentesJuridicosForm miForm = (ComponentesJuridicosForm) formulario;
			CenComponentesAdm componentesAdm = new CenComponentesAdm (this.getUserBean(request));
			Hashtable claves1 = new Hashtable();
			Hashtable claves = new Hashtable();
			
			boolean isertarNoColegiado = true;
			
			// Verificamos si hay algun regstro pra ese usuario
			tx.begin();
			if ((miForm.getClienteIdPersona()!=null) &&  (!miForm.getClienteIdPersona().equals(""))){
				
				String whereExiste = " where " + CenComponentesBean.C_FECHABAJA + " is null and ";
				whereExiste += CenComponentesBean.C_IDPERSONA + " = " + miForm.getIdPersona() + " and ";
				whereExiste += CenComponentesBean.C_IDINSTITUCION + " = " + miForm.getIdInstitucion() + " and ";
				whereExiste += CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + " = " + miForm.getClienteIdPersona() + " and ";
				whereExiste += CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION + " = " + miForm.getClienteIdInstitucion();
				Vector v1 = componentesAdm.select(whereExiste);
				if ((v1 != null) && (v1.size() > 0)) {
					throw new SIGAException ("messages.censo.componentes.errorExisteComponente");
				}
				
				//Comprobamos que el cliente no sea componente de otra sociedad en la qu el campo SOCIEDAD sea = 1(que actue como sociedad)
				String where = " where CEN_CLIENTE_IDINSTITUCION ="+miForm.getIdInstitucion()+
				   			   "   and CEN_CLIENTE_IDPERSONA = "+miForm.getClienteIdPersona()
				             + "   and "+CenComponentesBean.C_SOCIEDAD +"="+ClsConstants.DB_TRUE;
				Vector v2 = componentesAdm.select(where);
				if (miForm.getSociedad()!= null && miForm.getSociedad() && (v2 != null) && (v2.size() > 0)) {
					
					String update = "UPDATE " + CenComponentesBean.T_NOMBRETABLA        +
					"   SET " + CenComponentesBean.C_SOCIEDAD    + "= 0, " + CenComponentesBean.C_IDCUENTA + "= '' " + 
					" WHERE " + CenComponentesBean.C_IDINSTITUCION + "= " + miForm.getIdInstitucion() + 
					"   AND " + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + "= " +miForm.getClienteIdPersona();
					ClsMngBBDD.executeUpdate(update);
					//CenComponentesBean beancomponentes=(CenComponentesBean) v2.get(0);
					//CenPersonaAdm admpersona=new CenPersonaAdm(this.getUserBean(request));
					//String[] nifaux=new String[1];
					//nifaux[0]=admpersona.obtenerNIF(beancomponentes.getIdPersona().toString());
					//throw new SIGAException ("messages.censo.componentes.errorExisteCliente",nifaux);
				}
				
				//Comprueba si el idpersona está dado de alta en clientes de la institucion
				 where = " where IDINSTITUCION ="+miForm.getIdInstitucion()+
				   			   "   and IDPERSONA = "+miForm.getClienteIdPersona();
				CenClienteAdm clienteAdm = new CenClienteAdm (this.getUserBean(request));
				v2 = clienteAdm.select(where);
				if ((v2 == null) || (v2.size() == 0)) {
					insertNoColegiado(request, miForm);
				}
				else{
					//como sí existe un cliente para la institucion con el idpersona proporcionado
					//no se insertará un noColegiado
					isertarNoColegiado = false;
				}

				UtilidadesHash.set(claves, CenNoColegiadoBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				UtilidadesHash.set(claves, CenNoColegiadoBean.C_IDPERSONA, miForm.getClienteIdPersona());
			}else{
				CenClienteBean beanCli = insertNoColegiado(request, miForm);
				UtilidadesHash.set(claves, CenNoColegiadoBean.C_IDINSTITUCION, beanCli.getIdInstitucion());
				UtilidadesHash.set(claves, CenNoColegiadoBean.C_IDPERSONA, beanCli.getIdPersona());
			}
			
			// Comprabamos si esta insertado el NO_COLEGIADO
			CenNoColegiadoAdm noColAdm = new CenNoColegiadoAdm (this.getUserBean(request));
			
			
			Vector v = noColAdm.select(claves);
			CenNoColegiadoBean beanNoColegiado = null;
			//Si no existe un registro del mismo lo inserto con estos valores:
			//Cuando se inserta el componente con componentesAdm.insertarConHistorico()
			//si beanNoColegiado==null, no se inserta el no colegiado
			if ((v==null) || (v.isEmpty()) && isertarNoColegiado) {
				// Insertamos el no Colegiado
				beanNoColegiado = new CenNoColegiadoBean();
				beanNoColegiado.setIdInstitucion(Integer.valueOf(claves.get(CenNoColegiadoBean.C_IDINSTITUCION).toString()));
				beanNoColegiado.setIdPersona(Long.valueOf(claves.get(CenNoColegiadoBean.C_IDPERSONA).toString()));
				beanNoColegiado.setUsuMod(new Integer(user.getUserName()));
				beanNoColegiado.setFechaMod("sysdate");
				beanNoColegiado.setSociedadSJ(ClsConstants.DB_FALSE);
				beanNoColegiado.setTipo(seleccionarTipoNoColegiado(miForm, this.getUserBean(request)));
				beanNoColegiado.setAnotaciones("Sociedad dada de alta antes de los cambios No colegiados.");
				
				//noColAdm.insert(beanNoColegiado);	
			}

			// Fijamos los datos del componente
			CenComponentesBean beanComponentes  = new CenComponentesBean ();
			beanComponentes.setCargo(miForm.getCargo());
			beanComponentes.setIdInstitucion(miForm.getIdInstitucion());
			if (claves1.get(CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION)!=null && !claves1.get(CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION).equals("")){
			 beanComponentes.setCen_Cliente_IdInstitucion(Integer.valueOf(claves1.get(CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION).toString()));
			}else{
			 beanComponentes.setCen_Cliente_IdInstitucion(Integer.valueOf(claves.get(CenNoColegiadoBean.C_IDINSTITUCION).toString()));
			}
			beanComponentes.setCen_Cliente_IdPersona(claves.get(CenNoColegiadoBean.C_IDPERSONA).toString());
			
			if ((miForm.getFechaCargo()!=null)){
				beanComponentes.setFechaCargo(miForm.getFechaCargo());
			}
			beanComponentes.setIdPersona(miForm.getIdPersona());
			if (miForm.getSociedad().booleanValue())  {
				beanComponentes.setSociedad(ClsConstants.DB_TRUE);
				beanComponentes.setIdCuenta(miForm.getIdCuenta());
			}
			else { 
				beanComponentes.setSociedad(ClsConstants.DB_FALSE);
				beanComponentes.setIdCuenta(null);
			}
			if (miForm.getIdTipoColegio()!=null && !miForm.getIdTipoColegio().equals("")){
				beanComponentes.setIdTipoColegio(miForm.getIdTipoColegio());
			}
			beanComponentes.setNumColegiado(miForm.getNumColegiado());
			
			if ((miForm.getIdCargo()!=null && !miForm.getIdCargo().equals(""))){
				beanComponentes.setIdCargo(miForm.getIdCargo());
			}
		
			beanComponentes.setIdProvincia(miForm.getIdProvincia());
			
			if (miForm.getCapitalSocial().doubleValue()!=0.0){
				beanComponentes.setCapitalSocial(miForm.getCapitalSocial());
			}else{
				beanComponentes.setCapitalSocial(null);
			}
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
			if (miForm.getIdTipoColegio().equals("5")){
				beanNoColegiado=null;
			}
			
			if (!componentesAdm.insertarConHistorico(beanComponentes, beanNoColegiado, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (componentesAdm.getError());
			}
			tx.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, tx);
		}

		return exitoModal("messages.inserted.success", request);
	}
	private CenClienteBean insertNoColegiado(HttpServletRequest request,
			ComponentesJuridicosForm miForm) throws SIGAException {
		Hashtable hash_cliente = new Hashtable();
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_IDINSTITUCION, miForm.getIdInstitucion());
		UtilidadesHash.set(hash_cliente, CenPersonaBean.C_APELLIDOS1, miForm.getApellidos1());
		UtilidadesHash.set(hash_cliente, CenPersonaBean.C_APELLIDOS2, miForm.getApellidos2());
		UtilidadesHash.set(hash_cliente, CenPersonaBean.C_NOMBRE, miForm.getNombre());
		UtilidadesHash.set(hash_cliente, CenPersonaBean.C_NIFCIF, miForm.getNifcif());
		UtilidadesHash.set(hash_cliente, CenPersonaBean.C_IDTIPOIDENTIFICACION, ""+(ClsConstants.TIPO_IDENTIFICACION_NIF));
		UtilidadesHash.set(hash_cliente, CenPersonaBean.C_FALLECIDO, ClsConstants.DB_FALSE);
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_GUIAJUDICIAL, ClsConstants.DB_FALSE);
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_FECHAALTA, "sysdate");
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_COMISIONES, ClsConstants.DB_FALSE);
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_IDTRATAMIENTO, ClsConstants.DB_TRUE);
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_IDLENGUAJE, this.getUserBean(request).getLanguage());
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_PUBLICIDAD, ClsConstants.DB_FALSE);
		UtilidadesHash.set(hash_cliente, CenClienteBean.C_EXPORTARFOTO, ClsConstants.DB_FALSE);
		CenClienteAdm admCli=new CenClienteAdm (this.getUserBean(request));
		CenClienteBean beanCli=admCli.insertNoColegiado(hash_cliente, request);
		return beanCli;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction t = null;

		try {
			t = this.getUserBean(request).getTransaction();
			ComponentesJuridicosForm miForm = (ComponentesJuridicosForm) formulario;

			// Fijamos los datos del componente
			Hashtable hashOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			CenComponentesBean beanComponentes = new CenComponentesBean();
			beanComponentes.setCargo(miForm.getCargo());
			beanComponentes.setCen_Cliente_IdInstitucion(miForm.getClienteIdInstitucion());
			beanComponentes.setCen_Cliente_IdPersona(UtilidadesHash.getLong(hashOriginal, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA).toString());
			beanComponentes.setFechaCargo(miForm.getFechaCargo());
			beanComponentes.setIdComponente(UtilidadesHash.getInteger(hashOriginal, CenComponentesBean.C_IDCOMPONENTE));
			beanComponentes.setIdInstitucion(miForm.getIdInstitucion());
			beanComponentes.setIdPersona(miForm.getIdPersona());
			beanComponentes.setCapitalSocial(miForm.getCapitalSocial());
			
			if (miForm.getSociedad().booleanValue())  {
				beanComponentes.setSociedad(ClsConstants.DB_TRUE);
				beanComponentes.setIdCuenta(miForm.getIdCuenta());
			}
			else { 
				beanComponentes.setSociedad(ClsConstants.DB_FALSE);
				beanComponentes.setIdCuenta(null);
			}
			if (miForm.getIdTipoColegio()!=null && !miForm.getIdTipoColegio().equals("")){
				beanComponentes.setIdTipoColegio(miForm.getIdTipoColegio());
			}else{
				beanComponentes.setIdTipoColegio("");
			}
			beanComponentes.setNumColegiado(miForm.getNumColegiado());
			beanComponentes.setIdCargo(miForm.getIdCargo());
			beanComponentes.setIdProvincia(miForm.getIdProvincia());
			beanComponentes.setOriginalHash(hashOriginal);
			
			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(miForm.getMotivo());
	
			CenComponentesAdm componentesAdm = new CenComponentesAdm (this.getUserBean(request));
			
			//Comprobamos que el cliente no sea componente de otra sociedad en la qu el campo SOCIEDAD sea = 1(que actue como sociedad)
			// Para eso primero comprobamos que 
			String sociedadAntes=UtilidadesHash.getString(hashOriginal, CenComponentesBean.C_SOCIEDAD);
			
			
			//Comprobamos que el cliente no sea componente de otra sociedad en la qu el campo SOCIEDAD sea = 1(que actue como sociedad)
			String where = " where CEN_CLIENTE_IDINSTITUCION ="+miForm.getIdInstitucion()+
			   			   "   and CEN_CLIENTE_IDPERSONA = "+UtilidadesHash.getLong(hashOriginal, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA).toString()
			             + "   and "+CenComponentesBean.C_SOCIEDAD +"="+ClsConstants.DB_TRUE;
			Vector v2 = componentesAdm.select(where);
			if (sociedadAntes.equals(ClsConstants.DB_FALSE) && miForm.getSociedad()!= null && miForm.getSociedad() && (v2 != null) && (v2.size() > 0)) {
				
				String update = "UPDATE " + CenComponentesBean.T_NOMBRETABLA        +
				"   SET " + CenComponentesBean.C_SOCIEDAD    + "= 0, " + CenComponentesBean.C_IDCUENTA + "= '' " + 
				" WHERE " + CenComponentesBean.C_IDINSTITUCION + "= " + miForm.getIdInstitucion() + 
				"   AND " + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + "= " +UtilidadesHash.getLong(hashOriginal, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA).toString();
				ClsMngBBDD.executeUpdate(update);
				
			}

			/*if(sociedadAntes.equals(ClsConstants.DB_FALSE))
			{
				if (miForm.getSociedad().booleanValue())  {
					String where = " where CEN_CLIENTE_IDINSTITUCION ="+beanComponentes.getIdInstitucion()+
			   			   		   "   and CEN_CLIENTE_IDPERSONA = "+beanComponentes.getCen_Cliente_IdPersona()
			   			   		 + "   and "+CenComponentesBean.C_SOCIEDAD +"="+ClsConstants.DB_TRUE;
					Vector v2 = componentesAdm.select(where);
					if ((v2 != null) && (v2.size() > 0)) {
						CenComponentesBean beancomponentes=(CenComponentesBean) v2.get(0);
						CenPersonaAdm admpersona=new CenPersonaAdm(this.getUserBean(request));
						String[] nifaux=new String[1];
						nifaux[0]=admpersona.obtenerNIF(beancomponentes.getIdPersona().toString());
						throw new SIGAException ("messages.censo.componentes.errorExisteCliente",nifaux);
					}
				}
			}*/
			
			
			t.begin();
			if (!componentesAdm.updateConHistorico(beanComponentes, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (componentesAdm.getError());
			}
			t.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,t);
		}
		return exitoModal("messages.updated.success", request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction t = null;

		try {
			t = this.getUserBean(request).getTransaction();
			ComponentesJuridicosForm miForm = (ComponentesJuridicosForm) formulario;

			Long 	idPersona 		= miForm.getIdPersona();
			Integer idInstitucion 	= miForm.getIdInstitucion();
			Integer idComponente	= new Integer((String) miForm.getDatosTablaOcultos(0).get(0));

			CenComponentesAdm componenteAdm = new CenComponentesAdm (this.getUserBean(request));
			Hashtable clavesComponente = new Hashtable();
			UtilidadesHash.set (clavesComponente, CenComponentesBean.C_IDCOMPONENTE, idComponente);
			UtilidadesHash.set (clavesComponente, CenComponentesBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set (clavesComponente, CenComponentesBean.C_IDPERSONA, idPersona);

			// Fijamos los datos del Historico
			CenHistoricoBean beanHis = new CenHistoricoBean();
			beanHis.setMotivo(ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
	
			t.begin();
			if (!componenteAdm.deleteConHistorico(clavesComponente, beanHis, this.getLenguaje(request))) {
				throw new SIGAException (componenteAdm.getError());
			}
			t.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,t);
		}

		return exitoRefresco("messages.deleted.success", request);
	}
	protected String buscarNIF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
	{
		try {
			
	     	ComponentesJuridicosForm miform = (ComponentesJuridicosForm)formulario;

			// Consultamos las personas con el nif indicado
			CenClienteAdm admCli =  new CenClienteAdm(this.getUserBean(request));
			String elNif = miform.getNifcif().toUpperCase();
			
			//Le anhadimos 0 por delante hasta completar los 9 carácteres
			//while  (elNif.length() < 9) elNif = "0" + elNif;
			
			Vector resultadoNIF = admCli.selectComponentes(elNif.trim());
			
			// RGG 18-04-2006 actualizo el databackup para que no me de error el update
			if (resultadoNIF!=null && resultadoNIF.size()>0) {
				Hashtable resultado = (Hashtable) resultadoNIF.get(0);
				request.setAttribute("RESULTADO",resultado);
			}
	     	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "buscarNIF";
	}

	
	/**
	 * 
	 * @param miForm
	 * @param user
	 * @return
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 */
	private String seleccionarTipoNoColegiado(ComponentesJuridicosForm miForm, UsrBean user) throws ClsExceptions, SIGAException{
		//Inserta el colegiado con tipo=personal si
		//- El DNI no se encuentra en el colegio institucion, 
		//- colegiado en otro colegio
		//- y no colegiado. 
		
		// Consultamos las personas con el nif indicado
		CenClienteAdm admCli =  new CenClienteAdm(user);
		String elNif = miForm.getNifcif().toUpperCase();
		Vector resultadoNIF = admCli.selectComponentes(elNif.trim());
		if (resultadoNIF!=null && resultadoNIF.size()>0) {
			Hashtable resultado = (Hashtable) resultadoNIF.get(0);
			String tipo = (String) resultado.get("TIPO");
			if ( tipo == null || "4".equals(tipo) ){
				return ClsConstants.COMBO_TIPO_PERSONAL;
			}
			else{
				return ClsConstants.COMBO_TIPO_OTROS;
			}
		}
		else{
			return ClsConstants.COMBO_TIPO_PERSONAL;			
		}
	}
		
}


