/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.action;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.*;
import com.siga.censo.form.DatosRegistralesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author fernand.gomez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DatosRegistralesAction extends MasterAction{
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
				// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("modificarRegistrales")){
					mapDestino = modificarRegistrales(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("buscarNIF")){
					mapDestino = buscarNIF(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscarNotarioInit")){
					borrarPaginador(request, paginadorModal);
					mapDestino = buscarNotario(mapping, miForm, request, response);		
				}else if (accion.equalsIgnoreCase("buscarNotario")){
					mapDestino = buscarNotario(mapping, miForm, request, response);	
				} else if (accion.equalsIgnoreCase("validarNoColegiado")){
					mapDestino = validarNoColegiado(mapping, miForm, request, response);					
				} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxBusquedaNIF")){
					ClsLogging.writeFileLog("BUSQUEDA CENSO:getAjaxBusquedaNIF", 10);
					getAjaxBusquedaNIF(mapping, miForm, request, response);
					return null;
				} else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
	}
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String rc = "";
		String forward="inicio", accionPestanha=null;
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
				DatosRegistralesForm miform = (DatosRegistralesForm)formulario;
				miform.reset(mapping,request);

				CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),user,idInstitucionPersona.intValue(),idPersona.longValue());
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));				
		
				Vector v=null;
				Vector vCliente=null;
				String nombre = null;
				String numero = "";
				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
				CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
				numero = colegiadoAdm.getIdentificadorColegiado(bean);
				nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
				
				CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPersona = null;
				String where = " WHERE "+CenNoColegiadoBean.C_IDPERSONA+"="+idPersona;
				Vector vPersonas = admPersona.select(where);
				if (!vPersonas.isEmpty()) {
					beanPersona = (CenPersonaBean)admPersona.select(where).get(0);
					miform.setFechaConstitucion(GstDate.getFormatedDateShort(user.getLanguage(),beanPersona.getFechaNacimiento()));
				}
				CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
				CenNoColegiadoBean beanNoColegiado = null;
				//String where1 = " WHERE "+CenNoColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+
				//	" AND "+CenNoColegiadoBean.C_IDPERSONA+"="+idPersona;
				String where1 = " WHERE ("+CenNoColegiadoBean.C_IDINSTITUCION+"="+idInstitucionPersona+
					"OR IDINSTITUCION IN (SELECT IDINSTITUCION FROM CEN_INSTITUCION WHERE CEN_INSTITUCION.CEN_INST_IDINSTITUCION = " + idInstitucionPersona +"))"+
					" AND "+CenNoColegiadoBean.C_IDPERSONA+"="+idPersona;
				Vector vNoColegiados = admNoColegiado.select(where);
				
				if (!vNoColegiados.isEmpty()) {
					beanNoColegiado = (CenNoColegiadoBean)admNoColegiado.select(where1).get(0);
					if (beanNoColegiado.getFechaFin()!=null){
						miform.setfecha_fin(GstDate.getFormatedDateShort(user.getLanguage(),beanNoColegiado.getFechaFin()));
					}
					if (beanNoColegiado.getSociedadSP().trim().equals("1")){
						request.setAttribute("SSPP", "1");
					}else{
						request.setAttribute("SSPP", "0");
					}
					
					miform.setResena(beanNoColegiado.getResena());
					miform.setObjetoSocial(beanNoColegiado.getObjetoSocial());
					miform.setNoPoliza(beanNoColegiado.getNoPoliza());
					miform.setCompaniaSeg(beanNoColegiado.getCompaniaSeg());
					Long idnotario=beanNoColegiado.getIdPersonaNotario();
					
					miform.setIdPersona(beanNoColegiado.getIdPersona().toString());
					if (idnotario!=null){
						//Notario
						miform.setIdPersonaNotario(beanNoColegiado.getIdPersonaNotario().toString());
						CenPersonaAdm admNotario = new CenPersonaAdm(this.getUserBean(request));
						CenPersonaBean beanNotario = null;
						
						String whereNotario = " WHERE "+CenPersonaBean.C_IDPERSONA+"="+idnotario;
						Vector vNotario = admNotario.select(where);
						if (!vNotario.isEmpty()) {
							beanNotario = (CenPersonaBean)admNotario.select(whereNotario).get(0);
							miform.setNumIdentificacion(beanNotario.getNIFCIF());
							miform.setNombre(beanNotario.getNombre());
							miform.setApellido1(beanNotario.getApellido1());
							miform.setApellido2(beanNotario.getApellido2());
							miform.setTipoIdentificacion(beanNotario.getIdTipoIdentificacion().toString());
							request.setAttribute("tipoident", beanNotario.getIdTipoIdentificacion().toString());
							
						}
					}
				}
				
				Hashtable hashNoColegiadoOriginal=new Hashtable();
				hashNoColegiadoOriginal = this.prepararFormatosFechas(hashNoColegiadoOriginal);
				hashNoColegiadoOriginal.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
				hashNoColegiadoOriginal.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
				hashNoColegiadoOriginal.put(CenNoColegiadoBean.C_RESENA,beanNoColegiado.getResena());
				UtilidadesHash.set(hashNoColegiadoOriginal, CenNoColegiadoBean.C_FECHAFIN, beanNoColegiado.getFechaFin());
				UtilidadesHash.set(hashNoColegiadoOriginal, CenNoColegiadoBean.C_NOPOLIZA, beanNoColegiado.getNoPoliza());
				UtilidadesHash.set(hashNoColegiadoOriginal, CenNoColegiadoBean.C_COMPANIASEG, beanNoColegiado.getCompaniaSeg());
				hashNoColegiadoOriginal.put(CenNoColegiadoBean.C_OBJETOSOCIAL,beanNoColegiado.getObjetoSocial());
				hashNoColegiadoOriginal.put(CenPersonaBean.C_FECHANACIMIENTO,beanPersona.getFechaNacimiento());
				UtilidadesHash.set(hashNoColegiadoOriginal, CenNoColegiadoBean.C_IDPERSONANOTARIO, beanNoColegiado.getIdPersonaNotario());
				
				
				request.getSession().setAttribute("hashNoColegiadoOriginal", hashNoColegiadoOriginal);
				accionPestanha = request.getParameter("accion");
				request.setAttribute("idPersona", idPersona);
				request.setAttribute("idInstitucion", idInstitucionPersona);
				request.setAttribute("accion", accion);
				request.setAttribute("nombrePersona", nombre);
				request.setAttribute("numero", numero);
				request.setAttribute("vDatos", v);
				request.setAttribute("modoPestanha",accionPestanha);
			
			rc = "inicio";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return rc;
	}
	
	protected String validarNoColegiado (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String forward ="exception";
		try {
			// Obtengo los datos del formulario
			DatosRegistralesForm miForm = (DatosRegistralesForm)formulario;
			UsrBean usr = this.getUserBean(request);
			
			CenPersonaAdm adminPer=new CenPersonaAdm(usr);
    		CenPersonaBean cenPersona = adminPer.getPersona(miForm.getNumIdentificacion());
    		if(cenPersona!=null){
    			boolean isNombre = ComodinBusquedas.sustituirVocales(cenPersona.getNombre().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getNombre().toUpperCase().trim())); 
    			boolean isApellido1 =ComodinBusquedas.sustituirVocales(cenPersona.getApellido1().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getApellido1().toUpperCase().trim()));
    			boolean isApellido2 =ComodinBusquedas.sustituirVocales(cenPersona.getApellido2().toUpperCase().trim()).equalsIgnoreCase(ComodinBusquedas.sustituirVocales(miForm.getApellido2().toUpperCase().trim()));
    			if (!isNombre || ! isApellido1 ||!isApellido2){
    				miForm.setAccion("messages.fichaCliente.mostrarPersonaExistente");
    				forward = "validarNoColegiado";
   				}else{
   					CenClienteAdm clienteAdm = new CenClienteAdm(usr);
   					CenClienteBean cli = clienteAdm.existeCliente(cenPersona.getIdPersona(),new Integer(usr.getLocation()));
   					if (cli==null) {
   						forward =  modificarRegistrales(mapping, formulario, request, response);
   					}else{
  							  						
   						miForm.setAccion("messages.fichaCliente.clienteExiste");
   						miForm.setIdInstitucion(cli.getIdInstitucion().toString());

   						forward = "validarNoColegiado";
   					}	
   					
   				}
    			
    			miForm.setIdPersona(cenPersona.getIdPersona().toString());
    			miForm.setNombre(cenPersona.getNombre());
				miForm.setApellido1(cenPersona.getApellido1());
				miForm.setApellido2(cenPersona.getApellido2());
    			
    		}else{
    			forward =  modificarRegistrales(mapping, formulario, request, response);
    			
    		}
			
			

	   } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		return forward;
	}
	
	protected String modificarRegistrales (ActionMapping mapping,	MasterForm formulario,	HttpServletRequest request,	HttpServletResponse response) throws SIGAException 
	{
		Hashtable hashOriginal = new Hashtable();
		UsrBean usr = null;
		UserTransaction tx = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=usr.getLocation();
			tx = usr.getTransactionPesada();
			
		
			CenPersonaAdm adminPer=new CenPersonaAdm(this.getUserBean(request));

			
			// Obtengo los datos del formulario
			DatosRegistralesForm miForm = (DatosRegistralesForm)formulario;
			
			if(!adminPer.existeNif(miForm.getNumIdentificacion(), miForm.getNombre(), miForm.getApellido1(), miForm.getApellido2())){
				// insert de la parte de cliente paso un solo hash con los datos de cliente y de persona
				CenClienteAdm adminCli=new CenClienteAdm(usr);
				Hashtable hashPer = new Hashtable();
				hashPer = miForm.getDatos();
				hashPer.put(CenPersonaBean.C_NOMBRE, miForm.getNombre());
				hashPer.put(CenPersonaBean.C_APELLIDOS1, miForm.getApellido1());
				hashPer.put(CenPersonaBean.C_APELLIDOS2, miForm.getApellido2());
				hashPer.put(CenClienteBean.C_IDLENGUAJE,usr.getLanguage());
				
				// Adecuo formatos
				hashPer = this.prepararFormatosFechas(hashPer);
				hashPer = this.controlFormatosCheck(hashPer);
				CenClienteBean beanCli = adminCli.insertNoColegiado(hashPer, request);
				if(beanCli != null){
					miForm.setIdPersonaNotario(beanCli.getIdPersona().toString());
				}
			}
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			Hashtable hash = new Hashtable();
			hash = this.prepararFormatosFechas(hash);
			hash.put(CenPersonaBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set(hash,CenPersonaBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaConstitucion()));						

			

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=(Hashtable)request.getSession().getAttribute("hashNoColegiadoOriginal");
			
			
			
			// Cargo una nueva tabla hash para insertar en la tabla de historico
			

			// Comienzo la transaccion:			
			tx.begin();	

		
			// insert de la parte de cliente
			
			//**************************************************************
			// SI EL UPDATE DEVUELVE FALSE ES QUE NO EXISTE REGISTRO EN BBDD
			// update de la parte de persona
			if (!adminPer.update(hash,hashOriginal)) {
				//LMS 21/08/2006
				//Cambio por el nuevo uso de subLiteral en SIGAException.
				//SIGAException exc=new SIGAException("messages.err.noseque.datosgenerales.de.alguien");
				//exc.setSubLiteral("messages.censo.personanoexiste.bbdd");
				
				SIGAException exc=new SIGAException("messages.censo.personanoexiste.bbdd");
				throw exc;
				//throw new ClsExceptions(adminPer.getError());
			}
		
			// insert unico para el historico
			// no hace falta hashHist.put(CenHistoricoBean.C_IDHISTORICO, adminHist.getNuevoID(hash).toString());			
			
			
			// Modifico los datos del no colegiado en CenNoColegiado:
		
			CenNoColegiadoAdm admNoColegiado = new CenNoColegiadoAdm(this.getUserBean(request));
			Hashtable hashNoColegiado = new Hashtable();
			hashNoColegiado = this.prepararFormatosFechas(hashNoColegiado);
			UtilidadesHash.set(hashNoColegiado,CenNoColegiadoBean.C_RESENA,miForm.getResena());
			hashNoColegiado.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
			hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONA,miForm.getIdPersona());
			UtilidadesHash.set(hashNoColegiado,CenNoColegiadoBean.C_FECHAFIN,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getfecha_fin()));
			UtilidadesHash.set(hashNoColegiado,CenNoColegiadoBean.C_OBJETOSOCIAL,miForm.getObjetoSocial());
			UtilidadesHash.set(hashNoColegiado,CenNoColegiadoBean.C_NOPOLIZA,miForm.getNoPoliza());
			UtilidadesHash.set(hashNoColegiado,CenNoColegiadoBean.C_COMPANIASEG,miForm.getCompaniaSeg());
			String cargo=miForm.getActividadProfesional();
			String[] cargoaux=new String[1];
				
			if ((!miForm.getIdPersonaNotario().equals(""))){
				hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONANOTARIO,miForm.getIdPersonaNotario());
			}else{
				hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONANOTARIO,"");
				if ((!miForm.getNumIdentificacion().equals(""))){
					//inserto el notario
					Hashtable claves = new Hashtable();
					Hashtable hash_cliente = new Hashtable();
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_IDINSTITUCION, miForm.getIdInstitucion());
					UtilidadesHash.set(hash_cliente, CenPersonaBean.C_APELLIDOS1, miForm.getApellido1());
					UtilidadesHash.set(hash_cliente, CenPersonaBean.C_APELLIDOS2, miForm.getApellido2());
					UtilidadesHash.set(hash_cliente, CenPersonaBean.C_NOMBRE, miForm.getNombre());
					UtilidadesHash.set(hash_cliente, CenPersonaBean.C_NIFCIF, miForm.getNumIdentificacion());
					UtilidadesHash.set(hash_cliente, CenPersonaBean.C_IDTIPOIDENTIFICACION, miForm.getTipoIdentificacion());
					UtilidadesHash.set(hash_cliente, CenPersonaBean.C_FALLECIDO, ClsConstants.DB_FALSE);
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_GUIAJUDICIAL, ClsConstants.DB_FALSE);
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_FECHAALTA, "sysdate");
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_COMISIONES, ClsConstants.DB_FALSE);
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_IDTRATAMIENTO, ClsConstants.DB_TRUE);
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_IDLENGUAJE, this.getUserBean(request).getLanguage());
					UtilidadesHash.set(hash_cliente, CenClienteBean.C_PUBLICIDAD, ClsConstants.DB_FALSE);
					CenClienteAdm admCli=new CenClienteAdm (this.getUserBean(request));
					CenClienteBean beanCli=admCli.insertNoColegiado(hash_cliente, request);
					UtilidadesHash.set(claves, CenNoColegiadoBean.C_IDINSTITUCION, beanCli.getIdInstitucion());
					UtilidadesHash.set(claves, CenNoColegiadoBean.C_IDPERSONA, beanCli.getIdPersona());
					CenNoColegiadoAdm noColAdm = new CenNoColegiadoAdm (this.getUserBean(request));
					Vector v = noColAdm.select(claves);
					CenNoColegiadoBean beanNoColegiado = null;
					//Si no existe un registro del mismo lo inserto con estos valores:
					if ((v==null) || (v.isEmpty()) ) {
						// Insertamos el no Colegiado
						beanNoColegiado = new CenNoColegiadoBean();
						beanNoColegiado.setIdInstitucion(Integer.valueOf(claves.get(CenNoColegiadoBean.C_IDINSTITUCION).toString()));
						beanNoColegiado.setIdPersona(Long.valueOf(claves.get(CenNoColegiadoBean.C_IDPERSONA).toString()));
						beanNoColegiado.setUsuMod(new Integer(usr.getUserName()));
						beanNoColegiado.setFechaMod("sysdate");
						beanNoColegiado.setSociedadSJ(ClsConstants.DB_FALSE);
						beanNoColegiado.setTipo(ClsConstants.DB_TRUE);
						beanNoColegiado.setAnotaciones("Sociedad dada de alta antes de los cambios No colegiados.");
						
						noColAdm.insert(beanNoColegiado);
						hashNoColegiado.put(CenNoColegiadoBean.C_IDPERSONANOTARIO,claves.get(CenNoColegiadoBean.C_IDPERSONA).toString());
					}
				
					
					CenGruposClienteClienteBean bean = new CenGruposClienteClienteBean();
					bean.setIdGrupo(new Integer(5));
					bean.setIdInstitucion(new Integer(claves.get(CenNoColegiadoBean.C_IDINSTITUCION).toString()));
					bean.setIdInstitucionGrupo(new Integer(2000));
					bean.setIdPersona(new Long(claves.get(CenNoColegiadoBean.C_IDPERSONA).toString()));
	
					CenGruposClienteClienteAdm admGrupos = new CenGruposClienteClienteAdm(this.getUserBean(request));
					admGrupos.insert(bean);
				}
					
			}
	
			Hashtable hashNoColegiadoOriginal = (Hashtable)request.getSession().getAttribute("hashNoColegiadoOriginal");
			admNoColegiado.update(hashNoColegiado,hashNoColegiadoOriginal);

			
			
			// Fin de la transaccion:
			tx.commit();
			
			//Mandamos los datos para refrescar:
			request.setAttribute("mensaje","messages.updated.success");
			request.setAttribute("idPersona",miForm.getIdPersona());
			request.setAttribute("idInstitucion",miForm.getIdInstitucion());
			
			request.setAttribute("error","OK");
	  
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }
   	   return this.exitoRefresco("messages.updated.success", request);
	}
	
	private Hashtable prepararFormatosFechas (Hashtable entrada)throws ClsExceptions 
	{
		String fecha;
				
		try {		
			fecha=(String)entrada.get("FECHAFIN");
			if ((fecha!=null)&&(!fecha.equalsIgnoreCase(""))){
				entrada.put("FECHAFIN",GstDate.getApplicationFormatDate("",fecha));	
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al preparar los formatos delas fechas.");		
		}
		
		return entrada;
	}
	
	protected String buscarNIF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     	DatosRegistralesForm miform = (DatosRegistralesForm)formulario;
			Hashtable dataBackup = (Hashtable) request.getSession().getAttribute("DATABACKUP");

	     

			// Consultamos las personas con el nif indicado
			CenClienteAdm admCli =  new CenClienteAdm(this.getUserBean(request));
			String elNif = miform.getNumIdentificacion().toUpperCase();
			
			//Le anhadimos 0 por delante hasta completar los 9 carácteres
			//while  (elNif.length() < 9) elNif = "0" + elNif;
			
			Vector resultadoNIF = admCli.selectNotarios(elNif);
			
			// RGG 18-04-2006 actualizo el databackup para que no me de error el update
			Hashtable resultado = new Hashtable();
			
			if (resultadoNIF!=null && resultadoNIF.size()>0) {
				resultado = (Hashtable) resultadoNIF.get(0);
					
			}
			
			request.setAttribute("RESULTADO",resultado);
			
			
	     	
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "buscarNIF";
	}
	
	protected String buscarNotario(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {		
		
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		// casting del formulario
		DatosRegistralesForm miFormulario = (DatosRegistralesForm)formulario;
		String idInstitucion = miFormulario.getIdInstitucion();	
		
		// busqueda de clientes
		CenPersonaAdm cliente = new CenPersonaAdm(this.getUserBean(request));
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorModal);
		request.setAttribute("si",UtilidadesString.getMensajeIdioma(user, "general.yes"));
		request.setAttribute("no",UtilidadesString.getMensajeIdioma(user, "general.no"));
		
		try {
			HashMap databackup=getPaginador(request, paginadorModal);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					Vector datos=new Vector();
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					 
					request.setAttribute("letradoList", datos);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					databackup.put("paginador",paginador);
					databackup.put("datos",datos);
				}else{
					request.setAttribute("letradoList", new Vector());
					databackup.put("datos",new Vector());
					
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					setPaginador(request, paginadorModal, databackup);
					
				}	
				

			}else{	
				databackup=new HashMap();
				//Haria falta meter los parametros en con ClsConstants

				PaginadorBind paginador = cliente.getDatosPersonas(miFormulario, user.getLocation());
				
				if (paginador!=null&& paginador.getNumeroTotalRegistros()>0){
					int totalRegistros = paginador.getNumeroTotalRegistros();
					databackup.put("paginador",paginador);
					Vector datos = paginador.obtenerPagina(1);
					request.setAttribute("paginaSeleccionada", paginador.getPaginaActual());
					request.setAttribute("totalRegistros", paginador.getNumeroTotalRegistros());
					request.setAttribute("registrosPorPagina", paginador.getNumeroRegistrosPorPagina());
					request.setAttribute("letradoList", datos);
					databackup.put("datos",datos);
					
					setPaginador(request, paginadorModal, databackup);
				}else{
					databackup.put("datos",new Vector());
					request.setAttribute("paginaSeleccionada", 1);
					request.setAttribute("totalRegistros", 0);
					request.setAttribute("registrosPorPagina",1);
					request.setAttribute("letradoList", new Vector());
					setPaginador(request, paginadorModal, databackup);
				} 	
			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "resultadoNotarios";
	}

	@SuppressWarnings("unchecked")
	protected void getAjaxBusquedaNIF (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {
		DatosRegistralesForm miForm = (DatosRegistralesForm)formulario;
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Recogemos el parametro enviado por ajax
		String tipoIden = request.getParameter("tipoIdentificacion").trim();
		String nif = request.getParameter("numIdentificacion").trim();
		String nombre = request.getParameter("nombre").trim();
		String ape1 = request.getParameter("apellido1").trim();
		String ape2 = request.getParameter("apellido2");
		List listaParametros = new ArrayList();
	 
		if(nif!=null && !nif.equals("")){
			CenPersonaAdm perAdm = new CenPersonaAdm(user);
			CenPersonaBean perBean = new CenPersonaBean();			
			perBean = perAdm.getPersona(nif);
			if(perBean != null){
				miForm.setIdPersonaNotario(""+perBean.getIdPersona());
				miForm.setApellido2(perBean.getApellido2());
				miForm.setApellido1(perBean.getApellido1());
				miForm.setNombre(perBean.getNombre());
				miForm.setNumIdentificacion(nif);
			}else{
				miForm.setIdPersonaNotario(null);
				miForm.setApellido2(ape2);
				miForm.setApellido1(ape1);
				miForm.setNombre(nombre);
				miForm.setNumIdentificacion(nif);
			}
		}else{
			miForm.setIdPersonaNotario(null);
			miForm.setApellido2(ape2);
			miForm.setApellido1(ape1);
			miForm.setNombre(nombre);
			miForm.setNumIdentificacion(nif);
		}
			
		listaParametros.add(miForm.getIdPersonaNotario());
		listaParametros.add(miForm.getApellido1());
		listaParametros.add(miForm.getApellido2());
		listaParametros.add(miForm.getNombre());
		listaParametros.add(miForm.getNumIdentificacion());
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);
	}	

	/** 
	 * Prepara los campos de check para insertar en tabla. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	private Hashtable controlFormatosCheck (Hashtable entrada)throws SIGAException 
	{
		try {
			if (!entrada.containsKey(CenClienteBean.C_COMISIONES)) {
				entrada.put(CenClienteBean.C_COMISIONES,"0");
			}
				
			if (!entrada.containsKey(CenClienteBean.C_PUBLICIDAD)) {
				entrada.put(CenClienteBean.C_PUBLICIDAD,"0");
			}							
	
			if (!entrada.containsKey(CenClienteBean.C_GUIAJUDICIAL)) {
				entrada.put(CenClienteBean.C_GUIAJUDICIAL,"0");
			}
			if (!entrada.containsKey(CenClienteBean.C_NOENVIARREVISTA)) {
				entrada.put(CenClienteBean.C_NOENVIARREVISTA,"0");
			}
				
			if (!entrada.containsKey(CenClienteBean.C_NOAPARECERREDABOGACIA)) {
				entrada.put(CenClienteBean.C_NOAPARECERREDABOGACIA,"0");
			}
				
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
		return entrada;
	}		
	
}
