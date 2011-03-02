//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com - 27/01/2005 
//Ultima modificación: 27/01/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DatosGeneralesSOJForm;

public class DefinirDatosGeneralesSOJAction extends MasterAction {	
		

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
					if (accion.equalsIgnoreCase("borrarRelacionConEJG")) {
						mapDestino=borrarRelacionarConEJG (true, miForm, request);
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
	
	protected String borrarRelacionarConEJG (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions, SIGAException{
		return this.relacionarConEJG(false, formulario, request);
	}

	protected String relacionarConEJG (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		try {
			DatosGeneralesSOJForm miForm 	= (DatosGeneralesSOJForm)formulario;
			
			Hashtable miHash = new Hashtable ();
			UtilidadesHash.set(miHash, ScsSOJBean.C_IDINSTITUCION, 	miForm.getIdInstitucion());
			UtilidadesHash.set(miHash, ScsSOJBean.C_ANIO,			miForm.getAnio());
			UtilidadesHash.set(miHash, ScsSOJBean.C_NUMERO,			miForm.getNumero());
			UtilidadesHash.set(miHash, ScsSOJBean.C_IDTIPOSOJ,		miForm.getIdTipoSOJ());

			if (bCrear) {
				// Creamos la relacion
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGANIO,  		miForm.getAnioEJG());
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGNUMERO,		miForm.getNumeroEJG());
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGIDTIPOEJG,	miForm.getTipoEJG());
			}
			else {
				// Borramos la relacion
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGANIO,   		"");
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGNUMERO,		"");
				UtilidadesHash.set(miHash, ScsSOJBean.C_EJGIDTIPOEJG, 	"");				
			}

			String [] campos = {ScsSOJBean.C_EJGANIO, ScsSOJBean.C_EJGNUMERO, ScsSOJBean.C_EJGIDTIPOEJG};
			ScsDefinirSOJAdm sojAdm = new ScsDefinirSOJAdm(this.getUserBean(request));  
			if (!sojAdm.updateDirect(miHash, null, campos)) {
				throw new ClsExceptions ("Error de la relacion SOJ - EJG");
			}
		}
		catch (Exception e) 
		{
			throw new SIGAException("Error de la relacion Asistencia - EJG",e,new String[] {"modulo.gratuita"});
		} 

		return "exito";
	}	
	
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DatosGeneralesSOJForm miForm = (DatosGeneralesSOJForm) formulario;	
		request.setAttribute("DATABACKUP",miForm.getDatos());
		
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		request.setAttribute("nombreColegiado",request.getParameter("nomColegiado"));
		request.setAttribute("nColegiado",request.getParameter("nColegiadoTramitador"));
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			//CenColegiadoBean elegido = new CenColegiadoBean();
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
				
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		return "insertar";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		DatosGeneralesSOJForm miForm = (DatosGeneralesSOJForm) formulario;		
		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();			
		
		try {	
			
			miHash = miForm.getDatos();			

			Hashtable hashBkp = new Hashtable();
			hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATABACKUP");
			
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.update(miHash,hashBkp)) {
			    forward = "exitoRefresco";

				///////////////////////////////////////////////////////////////////////////////////////////
				// RGG 21-03-2006 : Cambios debidos a la nueva asignacion de colegiados desde Busqueda SJCS
				// ----------------------------------------------------------------------------------------
				
				//-----------------------------------------------------
				// obtencion de valores a utilizar (MODIFICAR SEGUN ACTION)
				String idInstitucionSJCS=usr.getLocation();
				String idTurnoSJCS=miForm.getIdTurno();
				String idGuardiaSJCS=miForm.getidGuardia();
				String anioSJCS=miForm.getAnio();
				String numeroSJCS=miForm.getNumero();
				String idPersonaSJCS=miForm.getIdPersona();
				String origenSJCS = "gratuita.operarSOJ.literal.modificacionSOJ"; 
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
				// Primero: Actualiza si ha sido automático o manual (Designaciones)0
				//admFiltros.actualizaManualDesigna(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,anioSJCS, numeroSJCS, flagSalto,flagCompensacion);
				// Segundo: Tratamiento de último (Designaciones)
				//admFiltros.tratamientoUltimo(idInstitucionSJCS,idTurnoSJCS,idPersonaSJCS,flagSalto,flagCompensacion);
				// Tercero: Generación de salto (Designaciones y asistencias)
				ScsSaltosCompensacionesAdm saltosCompAdm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
				if (checkSalto != null&&(checkSalto.equals("on") || checkSalto.equals("1")))
					saltosCompAdm.crearSaltoCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS, motivoSaltoSJCS,ClsConstants.SALTOS);
				
				// Cuarto: Generación de compensación (Designaciones NO ALTAS)
				//admFiltros.crearCompensacion(idInstitucionSJCS,idTurnoSJCS,idGuardiaSJCS,idPersonaSJCS,checkCompensacion,motivoCompensacionSJCS);
				///////////////////////////////////////////////////////////////////////////////////////////
			}
	        else forward="exito";

			
			tx.commit();
			/* Se almacena en DATABACKUP el registro recién modificado puesto que permanecemos en la pantalla
			   de mantenimiento y sino se volviese a realizar alguna modificación daría un error.
			*/
			Vector resultado = admBean.selectPorClave(miHash);
			ScsSOJBean soj = (ScsSOJBean)resultado.get(0);
			request.getSession().setAttribute("accion","editar");
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(soj));
			request.getSession().setAttribute("elegido",admBean.beanToHashTable(soj));
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		request.setAttribute("hiddenframe","1");
        
		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.updated.success",request);
		return exito("messages.updated.error",request);
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosGeneralesSOJForm miForm = (DatosGeneralesSOJForm) formulario;
		ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		String consulta = "";
		String institucion=null;
		String tipoSoj=null;
		String anio=null;
		String numero=null;
		
		try {			
			try{
				/* Recuperamos del request los parámetros pasados a las pestasnhas */			
				institucion=request.getParameter(ScsSOJBean.C_IDINSTITUCION);
				tipoSoj=request.getParameter(ScsSOJBean.C_IDTIPOSOJ).toString();
				anio=request.getParameter(ScsSOJBean.C_ANIO).toString();
				numero=request.getParameter(ScsSOJBean.C_NUMERO).toString();
			} catch (Exception e){
				UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
				institucion=usr.getLocation();
				tipoSoj=miForm.getIdTipoSOJ();
				anio=miForm.getAnio();
				numero=miForm.getNumero();
			}
			
			miHash.put(ScsSOJBean.C_IDINSTITUCION,institucion);
			miHash.put(ScsSOJBean.C_IDTIPOSOJ,tipoSoj);
			miHash.put(ScsSOJBean.C_ANIO,anio);
			miHash.put(ScsSOJBean.C_NUMERO,numero);

			// Hacemos la join de las tablas que necesitamos
			consulta = "SELECT soj.*, turno.abreviatura as descripcionturno, guardia.nombre descripcionguardia, persona.nombre, persona.apellidos1, persona.apellidos2, colegiado.ncolegiado FROM scs_soj soj, scs_turno turno, scs_guardiasturno guardia, cen_colegiado colegiado, cen_persona persona " +
					   "WHERE soj.idinstitucion = turno.idinstitucion(+) and soj.idturno = turno.idturno(+) and soj.idinstitucion = guardia.idinstitucion(+) and soj.idturno = guardia.idturno(+) and soj.idguardia = guardia.idguardia(+) and soj.idguardia = guardia.idguardia(+) and soj.idpersona = persona.idpersona(+) and soj.idinstitucion = " +
					   " colegiado.idinstitucion(+) and soj.idpersona = colegiado.idpersona(+) ";
			// Anhadimos los campos de la clave
			consulta += "and soj.idinstitucion = " + miHash.get(ScsSOJBean.C_IDINSTITUCION) + " and soj.anio = " + miHash.get(ScsSOJBean.C_ANIO) + " and soj.numero = " + miHash.get(ScsSOJBean.C_NUMERO) + " and soj.idtiposoj = " + miHash.get(ScsSOJBean.C_IDTIPOSOJ);  
			
			/* Recuperamos de la base de datos los valores originales */
			Vector resultado = admBean.selectGenerico(consulta);
			Hashtable soj = (Hashtable)resultado.get(0);
			
			/* En DATABACKUP almacenmos la hash con los valores */
			request.getSession().setAttribute("DATABACKUP",soj);
			request.getSession().setAttribute("accion",request.getSession().getAttribute("accion"));
			
			
			ScsEJGAdm ejgAdm =  new ScsEJGAdm(this.getUserBean(request));
			Row ejg=ejgAdm.getEJGdeSOJ(institucion, anio, tipoSoj, numero);
			miForm.setEJG((ejg==null?null:ejg.getRow()));
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		
		return "inicio";		
	}	

}