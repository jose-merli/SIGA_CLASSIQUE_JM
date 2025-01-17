package com.siga.gratuita.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenComponentesAdm;
import com.siga.beans.CenComponentesBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsRetencionesAdm;
import com.siga.beans.ScsRetencionesBean;
import com.siga.beans.ScsRetencionesIRPFAdm;
import com.siga.beans.ScsRetencionesIRPFBean;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.RetencionesIRPFForm;


/**
 * @author carlos.vidal
 */

public class RetencionesIRPFAction extends MasterAction {
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		
		String modo = (String) request.getParameter("MODO");
		
		if (modo != null && !modo.equals("") && miForm != null && (miForm.getModo()==null||miForm.getModo().equals(""))) {
			miForm.setModo(modo);
		}
		
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))) {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
				
			} else if(miForm.getModo().equalsIgnoreCase("buscarIRPFNoColegiado")) {
				return mapping.findForward(this.buscarIRPFNoColegiado(mapping, miForm, request, response));
			
			} else if(miForm.getModo().equalsIgnoreCase("guardarPagoPor")) {
				return mapping.findForward(this.guardarPagoPor(mapping, miForm, request, response));
				
			} else if (miForm.getModo().equalsIgnoreCase("dialogoInformeIRPF")) {
				return mapping.findForward(this.dialogoInformeIRPF(mapping, miForm, request, response));				
				
			} else if (miForm.getModo().equalsIgnoreCase("generarInformeIRPF")) {
				return mapping.findForward(this.generarInformeIRPF(mapping, miForm, request, response));
			
			} else if (miForm.getModo().equalsIgnoreCase("traeDatos")) {
				return mapping.findForward(this.traeDatos(mapping, miForm, request, response));
				
			} else return super.executeInternal(mapping, formulario, request, response);
			
		} catch (SIGAException e) {
			throw e;
			
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
			
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		}
	}
	protected String generarInformeIRPF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		RetencionesIRPFForm miform = (RetencionesIRPFForm)formulario;
		
		String salida = "generaInformeIRPF";
		return salida;
	}
	
	protected String dialogoInformeIRPF(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {

			RetencionesIRPFForm miform = (RetencionesIRPFForm) formulario;
			UsrBean user = this.getUserBean(request);

			String desdeFicha = request.getParameter("desdeFicha");
			Long idPersona = new Long(miform.getIdPersona());
			String idInstitucion = miform.getIdInstitucion();
			CenColegiadoAdm admCol = new CenColegiadoAdm(user);
			CenColegiadoBean beanCol = admCol.getDatosColegiales(idPersona,	new Integer(idInstitucion.trim()));
			CenPersonaAdm personaAdm = new CenPersonaAdm(user);
			String nombre = personaAdm.obtenerNombreApellidos(miform.getIdPersona());
			request.setAttribute("nombre", nombre);
			request.setAttribute("idPersona", idPersona.toString());
			request.setAttribute("colegiado", beanCol);
			request.setAttribute("desdeFicha", desdeFicha);
			request.setAttribute("anyoIRPF", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			
			String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			// mostramos la ventana con la pregunta
			
			Vector informeBeans=adm.obtenerInformesTipo(idInstitucion,"IRPF",null, null);
			if(informeBeans!=null && informeBeans.size()>1){
				informeUnico = ClsConstants.DB_FALSE;				
			}

			request.setAttribute("informeUnico", informeUnico);			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}

		return "dialogoInformeIRPF";
	}

	private String guardarPagoPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException{
		
		
		RetencionesIRPFForm miForm = (RetencionesIRPFForm) formulario;			
		CenComponentesAdm componentesAdm = new CenComponentesAdm(this.getUserBean(request));
				
		String forward = "error";
		String update = "";
		UsrBean usr;
		UserTransaction tx = null;
		
		try {
			Integer sociedadesCliente = miForm.getSociedadesCliente();
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction(); 
			String idsociedad = (String)request.getParameter("idsociedad");
			tx.begin();
			String idcuenta=null;
			String idsoci=null;
			if(idsociedad!=null && !idsociedad.trim().equalsIgnoreCase("")){ 
				String temp[] =  UtilidadesString.split(idsociedad, "*");

				if(temp.length==2){
					 idcuenta=temp[0];
					 idsoci=temp[1];
				}
			}
			// HACEMOS EL UPDATE DE CEN_COMPONENTES PONIENDO EL CAMPO SOCIEDAD = 0 PARA LA SOCIEDAD SELECCIONADA
			if(sociedadesCliente==null){
				
				
					update = "UPDATE " + CenComponentesBean.T_NOMBRETABLA        +
							"   SET " + CenComponentesBean.C_SOCIEDAD    + "= 0, " + CenComponentesBean.C_IDCUENTA + "= '' " + 
							" WHERE " + CenComponentesBean.C_IDINSTITUCION + "= " + usr.getLocation() + 
							"   AND " + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + "= " + request.getSession().getAttribute("idPersonaTurno");
					ClsMngBBDD.executeUpdate(update);
					insertarNuevo((String) request.getSession().getAttribute("idPersonaTurno"),  "SYSDATE", request);
			}else {
				
				
				update = "UPDATE " + CenComponentesBean.T_NOMBRETABLA        +
				"   SET " + CenComponentesBean.C_SOCIEDAD    + "= 0, " + CenComponentesBean.C_IDCUENTA + "= '' " + 
				" WHERE " + CenComponentesBean.C_IDINSTITUCION + "= " + usr.getLocation() + 
				"   AND " + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + "= " + request.getSession().getAttribute("idPersonaTurno");
				ClsMngBBDD.executeUpdate(update);
				
				update = "UPDATE " + CenComponentesBean.T_NOMBRETABLA        +
							"   SET " + CenComponentesBean.C_SOCIEDAD    + "= 1, " + CenComponentesBean.C_IDCUENTA + "= " +idcuenta +
							" WHERE " + CenComponentesBean.C_IDINSTITUCION + "= " + usr.getLocation() + 
							"   AND " + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + "= " + request.getSession().getAttribute("idPersonaTurno") +
							"   AND " + CenComponentesBean.C_IDPERSONA + "= " + idsoci;
				ClsMngBBDD.executeUpdate(update);
				request.setAttribute("idPersonaSociedadInicial",idsoci);
			}
			//ClsMngBBDD.executeUpdate(update);
			
			tx.commit();
		} 
		catch (SIGAException se) 
		{
			se.prepare(request);
			request.setAttribute("mensaje", se.getLiteral());
			return "exito"; 
		} 		
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoRefresco("messages.updated.success",request);
	}
	

	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String numero = "";
		String nombre = "";
		CenColegiadoBean datosColegiales;		
		Long idPers = null;
		Integer idInstPers = null;
		try {
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
				UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
				String idInstitucion = usr.getLocation();
				try {
					// Preparo para obtener la informacion del colegiado:
					idPers = new Long(idPersona);
					idInstPers = new Integer(idInstitucion);
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
					
				} catch (Exception e1){
					nombre = (String)request.getAttribute("NOMBRECOLEGPESTA�A");
					numero = (String)request.getAttribute("NUMEROCOLEGPESTA�A");
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			
			request.setAttribute("NOMBRECOLEGPESTA�A", nombre);
			request.setAttribute("NUMEROCOLEGPESTA�A", numero);
		} catch (Exception e){
			throwExcp("messages.select.error",e,null);
		}

		return "abrir";
	}
		
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'buscarPor'. Busca los turnos para los letrado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, 
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws SIGAException {
		String forward = "listado";
		try
		{
			String sql = "";
			RetencionesIRPFForm miForm = (RetencionesIRPFForm)formulario;
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			
			String idPersona2 = miForm.getIdPersona();
			if(idPersona2==null || idPersona2.equals("0")){
				idPersona2=idPersona;
			}
			Vector vRete = new Vector();
			Vector vReteSoc = new Vector();
						
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsRetencionesIRPFAdm reten = new ScsRetencionesIRPFAdm(usr);
			//Preparamos la select a ejecutar.
			// Comprobamos si el letrado actua como sociedad.
			CenComponentesAdm cenComponentesAdm = new CenComponentesAdm(usr);
			CenNoColegiadoAdm cenNoCol  = new CenNoColegiadoAdm(usr);
			
			String idInstitucion = usr.getLocation();
			
			String where = " where CEN_CLIENTE_IDINSTITUCION ="+usr.getLocation()+
						   "   and CEN_CLIENTE_IDPERSONA = "+request.getSession().getAttribute("idPersonaTurno")
						   + " and ("+CenComponentesBean.C_FECHABAJA + " is null OR FECHABAJA > SYSDATE) "
						   + " and "+CenComponentesBean.C_SOCIEDAD +"=1";
			Vector vCenComponentes = cenComponentesAdm.select(where);
			// Si es 0, el letrado actua en modo propio
			if(vCenComponentes.size() == 0) {
				sql = 
					"select "+
					"a.idretencion IDRETENCION,a.fechainicio FECHAINICIO,a.fechafin FECHAFIN, "+
					"a.idinstitucion IDINSTITUCION,a.idpersona IDPERSONA,a.fechamodificacion FECHAMODIFICACION, "+
					"a.usumodificacion USUMODIFICACION, "+
					"b.letranifsociedad LETRA, f_siga_getrecurso(b.descripcion, "+ usr.getLanguage()+ ") DESCRIPCION, b.RETENCION "+
					"from Scs_Retencionesirpf a,scs_maestroretenciones b where "+
					"a.idretencion = b.idretencion " +
					"and a.idpersona = "+request.getSession().getAttribute("idPersonaTurno")+" "+
					"and a.idinstitucion ="+usr.getLocation()+" order by FECHAINICIO desc";
						
				vRete = reten.select(sql);
				request.setAttribute("idSociedadLetradoSel","0");				
				request.setAttribute("SOCIEDAD", "0");
				
				/*String sq2 = "select idpersona from cen_componentes  WHERE cen_cliente_idpersona = "+idPersona;
				Vector cenPersona = reten.select(sq2);
				if(cenPersona.size() == 0) {
					request.setAttribute("idPersona",idPersona);
				}else{
					Hashtable<String, String> hash = (Hashtable<String, String>)cenPersona.get(0);				
					request.setAttribute("idPersona", hash.get("IDPERSONA"));
				}*/
				
				request.setAttribute("idPersona",idPersona);
				String idPersonaSociedad=cenComponentesAdm.getPersonaSociedad ( idPersona, usr.getLocation());
				if(idPersonaSociedad!= null){ 
					miForm.setCuentasSJCSSociedad(getCuentasList(miForm,idPersonaSociedad, usr));
					request.setAttribute("bloqueacheck","S");
				}else{ 
					miForm.setCuentasSJCSSociedad(new ArrayList<ValueKeyVO>());
					request.setAttribute("bloqueacheck","N");
				}	
			}
			// Si es > 0, el letrado actua como sociedad
			else
			{
				CenComponentesBean sociedad = (CenComponentesBean) vCenComponentes.get(0);				
				Vector vNoCol = cenNoCol.select("where idpersona = "+sociedad.getIdPersona()); 
				request.setAttribute("idCuenta", String.valueOf(sociedad.getIdCuenta()));
				request.setAttribute("idPersona", String.valueOf(sociedad.getIdPersona()));
				CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(usr);
				where = " where IDPERSONA = "+sociedad.getIdPersona();
				Vector vCenPersona = cenPersonaAdm.select(where);
				String resultado[] = EjecucionPLs.ejecutarPLCalcularIRPF_Pagos(idInstitucion, ""+sociedad.getIdPersona(), true);
				where = " where " + ScsRetencionesBean.T_NOMBRETABLA + "." + ScsRetencionesBean.C_IDRETENCION + " = '" + resultado[1] + "'";
				ScsRetencionesAdm irpf = new ScsRetencionesAdm(usr);
				Vector vIrpf = irpf.select(where);
				miForm.setSociedadesCliente( Integer.valueOf( String.valueOf(sociedad.getIdPersona())));
				if (vIrpf.size() == 0) {
	
					request.setAttribute("mensaje", "gratuita.retencionesIRPF.mensaje.error1");
					request.setAttribute("modal", "1");
					request.setAttribute("SOCIEDAD", String.valueOf(sociedad.getIdComponente()));
					request.setAttribute("idSociedadLetradoSel", String.valueOf(sociedad.getIdPersona()));
					//forward = "exito";
					
				// Tiene IRPF	
				} else {
						
					// OBTIENE EL PRIMER IRPF (LAS SOCIEDADES SOLO PUEDEN TENER UN IRPF)
					ScsRetencionesBean scsRetencionesBean = (ScsRetencionesBean) vIrpf.get(0);				
				
					// COMPRUEBO LA RELACION CON MAESTRO DE RETENCIONES
					Vector vMaestroReten = reten.select("select f_siga_getrecurso(descripcion, "+ usr.getLanguage()+ ") DESCRIPCION from scs_maestroretenciones where descripcion = "+scsRetencionesBean.getDescripcion());
					if (vMaestroReten.size()>0) {
						Hashtable hashRetencion = new Hashtable();
						Hashtable<String, String> descripcion = (Hashtable<String, String>)vMaestroReten.get(0);
				
						// GENERO EL HASH
						hashRetencion.put("LETRA", scsRetencionesBean.getLetraNifSociedad());
						hashRetencion.put("DESCRIPCION",descripcion.get("DESCRIPCION") );
						hashRetencion.put("RETENCION", String.valueOf(scsRetencionesBean.getRetencion()));
					
						vRete.add(hashRetencion);
					}				
				
					request.setAttribute("SOCIEDAD", String.valueOf(sociedad.getIdComponente()));
					request.setAttribute("idSociedadLetradoSel", String.valueOf(sociedad.getIdPersona()));					
				}
				
				miForm.setCuentasSJCSSociedad(getCuentasList(miForm,sociedad.getIdPersona().toString(), usr));		
			}
			
			String accion = (String)request.getSession().getAttribute("accion");
			if (accion == null)
				accion = "";
			request.setAttribute("accion", accion);
			
			request.setAttribute("idInstitucion", idInstitucion);
			request.setAttribute("resultado",vRete);
			request.getSession().removeAttribute("fechas");
			request.getSession().setAttribute("fechas",vRete);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	
	public List<ValueKeyVO> getCuentasList(
			RetencionesIRPFForm retencionesIRPFForm,String idpersona, UsrBean usrBean)
			throws ClsExceptions {
		
		CenComponentesAdm cenComponentesAdm = new CenComponentesAdm(usrBean);
    	List<ValueKeyVO> cuentasList = null;
		try {
			cuentasList = cenComponentesAdm.getCuentas(idpersona,  usrBean.getLocation());
		} catch (SIGAException e) {
    		cuentasList = new ArrayList<ValueKeyVO>();
		}
    	if(cuentasList==null)
    		cuentasList = new ArrayList<ValueKeyVO>();
    	return cuentasList;

	}
	

	
	protected String traeDatos(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
	String forward = "listado";
	try
	{
		Vector vCenComponentes = null;
		String sql = "";
		RetencionesIRPFForm miForm = (RetencionesIRPFForm)formulario;
		String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
		
		String idPersona2 = miForm.getIdPersona();
		if(idPersona2==null || idPersona2.equals("0")){
			idPersona2=idPersona;
		}
		Vector vRete = new Vector();
		Vector vReteSoc = new Vector();
					
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsRetencionesIRPFAdm reten = new ScsRetencionesIRPFAdm(usr);
		//Preparamos la select a ejecutar.
		// Comprobamos si el letrado actua como sociedad.
		CenComponentesAdm cenComponentesAdm = new CenComponentesAdm(usr);
		CenNoColegiadoAdm cenNoCol  = new CenNoColegiadoAdm(usr);
		
		String idInstitucion = usr.getLocation();
		String where = " where IDINSTITUCION ="+usr.getLocation()+
		   "   and CEN_CLIENTE_IDPERSONA = "+request.getSession().getAttribute("idPersonaTurno")+
			"   and IDPERSONA = "+miForm.getSociedadRefresca();
		if(!miForm.getSociedadRefresca().trim().equals("")){
		
			vCenComponentes = cenComponentesAdm.select(where);
		}
		// Si es 0, el letrado actua en modo propio
		if(miForm.getSociedadRefresca().trim().equals("") || vCenComponentes.size() == 0) {
			sql = 
				"select "+
				"a.idretencion IDRETENCION,a.fechainicio FECHAINICIO,a.fechafin FECHAFIN, "+
				"a.idinstitucion IDINSTITUCION,a.idpersona IDPERSONA,a.fechamodificacion FECHAMODIFICACION, "+
				"a.usumodificacion USUMODIFICACION, "+
				"b.letranifsociedad LETRA, f_siga_getrecurso(b.descripcion, "+ usr.getLanguage()+ ") DESCRIPCION, b.RETENCION "+
				"from Scs_Retencionesirpf a,scs_maestroretenciones b where "+
				"a.idretencion = b.idretencion " +
				"and a.idpersona = "+request.getSession().getAttribute("idPersonaTurno")+" "+
				"and a.idinstitucion ="+usr.getLocation()+" order by FECHAINICIO";
					
			vRete = reten.select(sql);
			request.setAttribute("idSociedadLetradoSel","0");				
			request.setAttribute("SOCIEDAD", "0");
			String idPersonaSociedad=cenComponentesAdm.getPersonaSociedad ( idPersona, usr.getLocation());
			if(idPersonaSociedad!= null)
				miForm.setCuentasSJCSSociedad(getCuentasList(miForm,idPersonaSociedad, usr));
			else 
				miForm.setCuentasSJCSSociedad(new ArrayList<ValueKeyVO>());
			
			request.setAttribute("bloqueacheck","true");
			request.setAttribute("idPersona",idPersona);
			 miForm.setSociedadesCliente(new Integer("0"));
		}
		// Si es > 0, el letrado actua como sociedad
		else
		{
			CenComponentesBean sociedad = (CenComponentesBean) vCenComponentes.get(0);				
			Vector vNoCol = cenNoCol.select("where idpersona = "+sociedad.getIdPersona()); 
			request.setAttribute("idCuenta", String.valueOf(sociedad.getIdCuenta()));
			request.setAttribute("idPersona", String.valueOf(sociedad.getIdPersona()));
			CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(usr);
			where = " where IDPERSONA = "+sociedad.getIdPersona();
			Vector vCenPersona = cenPersonaAdm.select(where);
			
			String resultado[] = EjecucionPLs.ejecutarPLCalcularIRPF_Pagos(idInstitucion, ""+sociedad.getIdPersona(), true);
			where = " where " + ScsRetencionesBean.T_NOMBRETABLA + "." + ScsRetencionesBean.C_IDRETENCION + " = '" + resultado[1] + "'";
			ScsRetencionesAdm irpf = new ScsRetencionesAdm(usr);
			Vector vIrpf = irpf.select(where);
			miForm.setSociedadesCliente( Integer.valueOf( String.valueOf(sociedad.getIdPersona())));
			if (vIrpf.size() == 0) {
				request.setAttribute("mensaje", "gratuita.retencionesIRPF.mensaje.error1");
				request.setAttribute("modal", "1");
				request.setAttribute("SOCIEDAD", String.valueOf(sociedad.getIdComponente()));
				request.setAttribute("idSociedadLetradoSel", String.valueOf(sociedad.getIdPersona()));
				//forward = "exito";
				
			// Tiene IRPF	
			} else {
						
				// OBTIENE EL PRIMER IRPF (LAS SOCIEDADES SOLO PUEDEN TENER UN IRPF)
				ScsRetencionesBean scsRetencionesBean = (ScsRetencionesBean) vIrpf.get(0);				
				
				// COMPRUEBO LA RELACION CON MAESTRO DE RETENCIONES
				Vector vMaestroReten = reten.select("select f_siga_getrecurso(descripcion, "+ usr.getLanguage()+ ") DESCRIPCION from scs_maestroretenciones where descripcion = "+scsRetencionesBean.getDescripcion());
				if (vMaestroReten.size()>0) {
					Hashtable hashRetencion = new Hashtable();
					Hashtable<String, String> descripcion = (Hashtable<String, String>)vMaestroReten.get(0);
				
					// GENERO EL HASH
					hashRetencion.put("LETRA", scsRetencionesBean.getLetraNifSociedad());
					hashRetencion.put("DESCRIPCION",descripcion.get("DESCRIPCION") );
					hashRetencion.put("RETENCION", String.valueOf(scsRetencionesBean.getRetencion()));
					
					vRete.add(hashRetencion);
				}				
				
				request.setAttribute("SOCIEDAD", String.valueOf(sociedad.getIdComponente()));
				request.setAttribute("idSociedadLetradoSel", String.valueOf(sociedad.getIdPersona()));
			}
			miForm.setCuentasSJCSSociedad(getCuentasList(miForm,miForm.getSociedadRefresca(), usr));
		}
		
		request.setAttribute("idInstitucion", idInstitucion);
		request.setAttribute("resultado",vRete);
		request.getSession().removeAttribute("fechas");
		request.getSession().setAttribute("fechas",vRete);
		String accion = (String)request.getSession().getAttribute("accion");
		if (accion == null)
			accion = "";
		request.setAttribute("accion", accion);
	}
	catch (Exception e) 
	{
		throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
	} 
	return forward;
}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		String forward = "editar";
		try {
		    UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");

		    RetencionesIRPFForm miForm = (RetencionesIRPFForm) formulario;			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);
			ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
			String idInstitucion 		= (String)vOcultos.elementAt(0);
			String idPersona 			= (String)vOcultos.elementAt(1);
			String idRetencion 			= (String)vOcultos.elementAt(2);
			String fechaInicio 			= (String)vOcultos.elementAt(3);
			String fechaFin 			= (String)vOcultos.elementAt(4);
			String fechaModificacion 	= (String)vOcultos.elementAt(5);
			String usuModificacion 		= (String)vOcultos.elementAt(6);
			//Creamos la hash de backup
			Hashtable backup = new Hashtable();
			backup.put("IDINSTITUCION",idInstitucion);
			backup.put("IDPERSONA",idPersona);
			backup.put("IDRETENCION",idRetencion);
			backup.put("FECHAINICIO",fechaInicio);
			backup.put("FECHAFIN",fechaFin);
			backup.put("FECHAMODIFICACION",fechaModificacion);
			backup.put("USUMODIFICACION",usuModificacion);
			request.getSession().setAttribute("BACKUP",backup);
			//Preparamos la select a ejecutar.
			String sql = 
				"select "+
				"a.idretencion IDRETENCION,a.fechainicio FECHAINICIO,a.fechafin FECHAFIN, "+
				"b.letranifsociedad LETRA, f_siga_getrecurso(b.descripcion, "+ usr.getLanguage()+ ") DESCRIPCION, b.RETENCION "+
				// "b.letranifsociedad LETRA,b.descripcion DESCRIPCION, b.RETENCION "+
				"from Scs_Retencionesirpf a,scs_maestroretenciones b where "+
				"a.idretencion = b.idretencion " +
				"and a.idpersona = "+idPersona+" "+
				"and a.idinstitucion ="+usr.getLocation()+" "+
				"and a.fechainicio =TO_DATE('" + fechaInicio + "', '" + ClsConstants.DATE_FORMAT_SQL + "') "+
				"and a.idretencion ="+idRetencion;
					
			Vector vRete = scsRetencionesIRPFAdm.select(sql);
			request.setAttribute("resultado",vRete);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'ver'. Muestra el detalle del turno seleccionado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'nuevo'. Existen dos posibilidades dependiendo del valor del paso.
	 *  Si el paso es = 1, consulta las guardias para el turno seleccionado, por el contrario, si el paso != 1
	 *  nos muestra una pantalla donde se solicita fecha de solicitud y observaciones a la solicitud. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "nuevo";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'insertar'. Esta funcion dara de alta para el letrado el turno seleccionado. 
	 *  Si el letrado ha seleccionado apuntarse a las guardias (valor 0), se le dara de alta en todas las guardias
	 *  del turno.  
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		RetencionesIRPFForm miForm = (RetencionesIRPFForm) formulario;			
		ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
		ScsRetencionesIRPFBean scsRetencionesIRPFBean = new ScsRetencionesIRPFBean();
				
		String forward = "error";
		UsrBean usr;
		UserTransaction tx=null;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();
			
			tx.begin();
			// Cargamos los datos del alta
			Hashtable hash = new Hashtable();
			hash.put("IDINSTITUCION",usr.getLocation());
			// Se coge de la sesion la persona.
			hash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
			hash.put("IDRETENCION",miForm.getIdRetencion());
			hash.put("FECHAINICIO",GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInicio()));
			
			String sql = "select * from Scs_Retencionesirpf where "+
						 " idpersona = "+request.getSession().getAttribute("idPersonaTurno")+
						 " and idinstitucion ="+usr.getLocation()+
						 " order by fechafin desc, fechainicio desc";
			
			Vector vRete = scsRetencionesIRPFAdm.select(sql);
			if(vRete != null && vRete.size() > 0){
				
				Hashtable miHash0  = (Hashtable) vRete.get(0);
				Hashtable backup0  = (Hashtable)((Hashtable) vRete.get(0)).clone();
				String fechaInicio = (String) miHash0.get("FECHAINICIO");
				String fechaInicioNueva = GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInicio());
				
				if(fechaInicioNueva.compareTo(fechaInicio) < 0)
					return error("gratuita.altaRetencionesIRPF.literal.alert2",new ClsExceptions("gratuita.altaRetencionesIRPF.literal.alert2"),request);
				else{
					
					miHash0.put("FECHAFIN",GstDate.dateSumaDiasJava(fechaInicioNueva,-1));
					if(!scsRetencionesIRPFAdm.update(miHash0,backup0))
						throw new SIGAException(scsRetencionesIRPFAdm.getError());
					if(!scsRetencionesIRPFAdm.insert(hash))
						throw new SIGAException(scsRetencionesIRPFAdm.getError());
				}
					
				
			}
			else{
				if(!scsRetencionesIRPFAdm.insert(hash))
					throw new SIGAException(scsRetencionesIRPFAdm.getError());
			}
			
			tx.commit();
		} 
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
//			return exitoModalSinRefresco("messages.rangoFechas.error",request);			
		} 
		return exitoModal("messages.inserted.success",request);
		
	}

	public void insertarNuevo(String idPersona, String date, HttpServletRequest request) throws Exception {

	ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
	ScsRetencionesIRPFBean scsRetencionesIRPFBean = new ScsRetencionesIRPFBean();
			
	String forward = "error";
	UsrBean usr;
	//UserTransaction tx=null;

	try {
		usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		//tx=usr.getTransaction();
		
		//tx.begin();
		// Cargamos los datos del alta
		
		ScsRetencionesAdm admBean = new ScsRetencionesAdm(usr);
		String idRetencion = admBean.getRetencionPorDefecto();
		
		Hashtable hash = new Hashtable();
		hash.put("IDINSTITUCION",usr.getLocation());
		// Se coge de la sesion la persona.
		hash.put("IDPERSONA",idPersona);
		hash.put("IDRETENCION",idRetencion);
		hash.put("FECHAINICIO",date);

		String sql = "select * from Scs_Retencionesirpf where "+
		 " idpersona = "+idPersona+
		 " and idinstitucion = "+usr.getLocation()+
		 " order by fechafin desc";
		
		Vector vRete = scsRetencionesIRPFAdm.select(sql);
		if(vRete != null && vRete.size() > 0){
		
			Hashtable miHash0  = (Hashtable) vRete.get(0);
			Hashtable backup0  = (Hashtable)((Hashtable) vRete.get(0)).clone();
			String fechaFin = (String) miHash0.get("FECHAFIN");
			String fechaInicioNueva = date;
			if(date.equalsIgnoreCase("sysdate")){
				fechaInicioNueva= getDateTime();
			}
			
			if(fechaFin!=null && !fechaFin.equals("") && fechaInicioNueva.compareTo(fechaFin) > 0)
				if(!scsRetencionesIRPFAdm.insert(hash))
					throw new SIGAException(scsRetencionesIRPFAdm.getError());
		}
		else{
			if(!scsRetencionesIRPFAdm.insert(hash))
				throw new SIGAException(scsRetencionesIRPFAdm.getError());
		}
		
	} 
	catch (Exception e) 
	{
		throw e; 
//		return exitoModalSinRefresco("messages.rangoFechas.error",request);			
	} 
	
	
}
	
	 private String getDateTime() {   
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");   
	        Date date = new Date();   
	        return dateFormat.format(date);   
	    }  


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		RetencionesIRPFForm miForm = (RetencionesIRPFForm) formulario;			
		ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
				
		String forward = "error";
		UsrBean usr;
		UserTransaction tx = null;
		

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction(); 
			tx.begin();	

			// Cargamos los datos del alta
			Hashtable hash = new Hashtable();
			hash.put("IDINSTITUCION",usr.getLocation());
			hash.put("IDPERSONA"	,request.getSession().getAttribute("idPersonaTurno"));
			hash.put("IDRETENCION"	,miForm.getIdRetencion());
			hash.put("FECHAINICIO"	,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInicio()));
			if(!miForm.getFechaFin().equals(""))
				hash.put("FECHAFIN"		,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaFin()));
			// obtenemos el backup y limpiamos la sesion
			Hashtable backup = new Hashtable();
			backup = (Hashtable) request.getSession().getAttribute("BACKUP");
			request.getSession().removeAttribute("BACKUP");
			// Como uno de los campos pertenece a la clave primaria, borramos y luego insertamos.

			boolean result = scsRetencionesIRPFAdm.delete(backup);
			if(result)
			{
				result = scsRetencionesIRPFAdm.insert(hash);
				if (result)
				{				
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}
		} 
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
//			return exitoModal("messages.rangoFechas.error",request);
		} 
		return exitoModal("messages.updated.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		String forward = "";
		UsrBean usr;
		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
		    RetencionesIRPFForm miForm = (RetencionesIRPFForm) formulario;
		    Vector vOcultos = miForm .getDatosTablaOcultos(0);
			ScsRetencionesIRPFAdm scsRetencionesIRPFAdm = new ScsRetencionesIRPFAdm(this.getUserBean(request));
			Hashtable hash = new Hashtable();
			hash.put("IDRETENCION",(String)vOcultos.elementAt(2));
			hash.put("FECHAINICIO",(String)vOcultos.elementAt(3));
			hash.put("IDPERSONA",request.getSession().getAttribute("idPersonaTurno"));
			hash.put("IDINSTITUCION",usr.getLocation());
			//Si es la unica retencion que hay, comprobamos si existen turnos asociados.
			String where = " where idinstitucion = "+usr.getLocation()+ 
							" and idpersona = "+request.getSession().getAttribute("idPersonaTurno");
			if(scsRetencionesIRPFAdm.selectTabla(where).size()> 1 )
			{
				if(scsRetencionesIRPFAdm.delete(hash))
				{
			        request.setAttribute("mensaje","messages.deleted.success");
			        // Actualizamos el fecha desde.
					String sql = "select * from Scs_Retencionesirpf where "+
					" idpersona = "+request.getSession().getAttribute("idPersonaTurno")+
					" and idinstitucion ="+usr.getLocation()+
					" order by fechafin desc";
				
					Vector vRete = scsRetencionesIRPFAdm.select(sql);
					if(vRete != null && vRete.size()>0)
					{
						Hashtable miHash  = (Hashtable) vRete.get(0);
						Hashtable backup  = (Hashtable)((Hashtable) vRete.get(0)).clone();
						if(!miHash.get("FECHAFIN").equals(""))
						{
							miHash.put("FECHAFIN","");
							scsRetencionesIRPFAdm.update(miHash,backup);
						}
					}
				}
				else
				{
					request.getSession().removeAttribute("fechas");
			        request.setAttribute("mensaje","error.messages.deleted");
				}
			}
			else
			{
				request.getSession().removeAttribute("fechas");
				request.setAttribute("mensaje","gratuita.retencionesIRPF.mensaje.error2");
			}
			request.setAttribute("hiddenFrame", "1");
		    forward = "exito";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String validarInscripcion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private String buscarIRPFNoColegiado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");
			String sIdPersona = (String)request.getParameter("idPersonaPestana");
			String sIdInstitucion = (String)request.getParameter("idInstitucionPestana");
			
			ScsRetencionesAdm admRetenciones = new ScsRetencionesAdm(usuario);
			ScsRetencionesIRPFAdm admIRPF = new ScsRetencionesIRPFAdm(usuario);
			
			// Calcula IRPF
			String resultado[] = EjecucionPLs.ejecutarPLCalcularIRPF_Pagos(sIdInstitucion, sIdPersona, true);
			
			// Obtiene Retenciones
			String where = " WHERE " + ScsRetencionesBean.T_NOMBRETABLA + "." + ScsRetencionesBean.C_IDRETENCION + " = '" + resultado[1] + "'";			
			Vector vRetenciones = admRetenciones.select(where);				
				
			//  Compruebo que tiene Retenciones
			Vector vResultado = new Vector();
			if (vRetenciones.size() > 0) {				

				// Actualmente una sociedad solo puede tener un IRPF 
				ScsRetencionesBean scsRetencionesBean = (ScsRetencionesBean) vRetenciones.get(0);
								
				// Busco el nombre de la retencion
				String sql = "SELECT F_SIGA_GETRECURSO(descripcion, " + usuario.getLanguage() + ") DESCRIPCION FROM scs_maestroretenciones WHERE descripcion = '" + scsRetencionesBean.getDescripcion() + "'";								
				Vector vIRPF = admIRPF.select(sql);
				
				if (vIRPF.size() > 0) {
					Hashtable hashRetencion = new Hashtable();
					
					Hashtable<String, String> descripcion = (Hashtable<String, String>) vIRPF.get(0);
					hashRetencion.put("DESCRIPCION", descripcion.get("DESCRIPCION") );				
				
					hashRetencion.put("LETRA", scsRetencionesBean.getLetraNifSociedad());
					hashRetencion.put("RETENCION", String.valueOf(scsRetencionesBean.getRetencion()));
													
					vResultado.add(hashRetencion);
				}
			}
							
			request.setAttribute("resultado", vResultado);
			request.setAttribute("idPersona", sIdPersona);
			request.setAttribute("idInstitucion", sIdInstitucion);
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] {"modulo.gratuita"});
		} 	
		
		return "inicio"; 
	}		
}