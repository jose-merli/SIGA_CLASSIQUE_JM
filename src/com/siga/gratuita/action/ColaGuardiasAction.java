package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ColaGuardiasForm;
import com.siga.informes.InformeColaGuardias;

/**
 * Action de Colas de Oficios.
 * @author cristina.santos
 * @since 13/02/2006
 */
public class ColaGuardiasAction extends MasterAction {
	
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm != null)&&(miForm.getModo()!=null)&&(miForm.getModo().equals("imprimir"))){
				return mapping.findForward(this.imprimir(mapping, miForm, request, response));
			}
			else if((miForm != null)&&(miForm.getModo()!=null)&&(miForm.getModo().equals("fijarUltimoLetrado"))){
				return mapping.findForward(this.fijarUltimoLetrado(mapping, miForm, request, response));
			}
			else{
				return super.executeInternal(mapping, formulario, request, response);
			}
		
		} catch (SIGAException e) {
			throw e;
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		}
	}

	
	/** 
	 * Método que atiende la accion abrir. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(
			ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "inicio";
	}
	
	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String ver (
			ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		ColaGuardiasForm coForm=(ColaGuardiasForm)formulario;
		HttpSession ses = request.getSession();
		Hashtable turnoElegido = (Hashtable)ses.getAttribute("turnoElegido");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN"); 
		
		Integer usuario=this.getUserName(request);
		String institucion =usr.getLocation();
		String turno =(String)turnoElegido.get("IDTURNO");
		String guardia=coForm.getIdGuardia();
		ScsGuardiasTurnoAdm guardiasTurnoAdm = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		ScsSaltosCompensacionesAdm saltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
		
		//Cargar último letrado
		cargarUltimoLetrado(this.getUserBean(request), institucion, turno, guardia, coForm);
		
		//Cargar listado de letrados en cola
		Vector vLetradosEnCola=guardiasTurnoAdm.selectLetradosEnCola(institucion,turno,guardia);
		if(vLetradosEnCola!=null && !vLetradosEnCola.isEmpty()){
			request.setAttribute("vLetradosEnCola",vLetradosEnCola);
		}
		
		//Cargar listado de compensaciones
		Vector vCompensaciones=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, guardia, ClsConstants.COMPENSACIONES);
		if(vCompensaciones!=null && !vCompensaciones.isEmpty()){
			request.setAttribute("vCompensaciones",vCompensaciones);
		}
		//Cargar listado de saltos
		Vector vSaltos=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, guardia, ClsConstants.SALTOS);
		if(vSaltos!=null && !vSaltos.isEmpty()){
			request.setAttribute("vSaltos",vSaltos);
		}

		request.setAttribute("idGuardia", guardia);

		return "ver";
	}
	
	/** 
	 *  Funcion que atiende la accion imprimir. Imprime listas de cola de guardias
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String imprimir (
			ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		String generacionPdfOK = "ERROR";
		String salida = null;
		try {		
			//Obtengo el bean de la facturacion:
			// Nombre de la plantilla FO:
			String nombreFicheroFO = ClsConstants.PLANTILLA_FO_COLAGUARDIAS;

			InformeColaGuardias reportColaGuardias = new InformeColaGuardias(this.getUserBean(request));
			
			//Generamos el Informe si la hash no es null:			
			if (reportColaGuardias.generarInforme(request, nombreFicheroFO))
				generacionPdfOK = "OK";			
			else
				generacionPdfOK = "ERROR";
			
			request.setAttribute("generacionOK",generacionPdfOK);			
			salida = "descarga";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return salida;	
	}
	
	/**
	 * Invoca varias clases para obtener el ultimo letrado con numero de colegiado, nombre y apellidos
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param turno Codigo turno seleccionado
	 * @param turno Codigo guardia seleccionada
	 * @param coForm Formulario en que se cargan los valores
	 * @throws ClsExceptions Error interno
	 */	
	protected void cargarUltimoLetrado(
			UsrBean usuario, 
			String institucion, 
			String turno,
			String guardia,
			ColaGuardiasForm coForm) throws ClsExceptions{
		
		ScsGuardiasTurnoAdm guardiasTurnoAdm = new ScsGuardiasTurnoAdm(usuario);
		CenPersonaAdm personaAdm= new CenPersonaAdm(usuario);
		CenColegiadoAdm colegiadoAdm= new CenColegiadoAdm(usuario);
		
		//buscar persona ultimo turno 
		Hashtable hashTurno = new Hashtable();
		hashTurno.put(ScsGuardiasTurnoBean.C_IDTURNO,turno);
		hashTurno.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,institucion);
		hashTurno.put(ScsGuardiasTurnoBean.C_IDGUARDIA,guardia);
		ScsGuardiasTurnoBean guardiasTurnoBean = (ScsGuardiasTurnoBean)((Vector)guardiasTurnoAdm.select(hashTurno)).get(0);
		Long ultimo=guardiasTurnoBean.getIdPersona_Ultimo();
		
		if(ultimo!=null){
			//buscar numero colegiado
			Hashtable hashColegiado = new Hashtable();
			hashColegiado.put(CenColegiadoBean.C_IDINSTITUCION,institucion);
			hashColegiado.put(CenColegiadoBean.C_IDPERSONA,ultimo);
			CenColegiadoBean colegiadoBean = (CenColegiadoBean)((Vector)colegiadoAdm.select(hashColegiado)).get(0);
			coForm.setNColegiado(colegiadoAdm.getIdentificadorColegiado (colegiadoBean));
			
			//buscar datos persona
			Hashtable hashPersona = new Hashtable();
			hashPersona.put(CenPersonaBean.C_IDPERSONA,ultimo);
			CenPersonaBean personaBean= (CenPersonaBean)((Vector)personaAdm.select(hashPersona)).get(0);

			coForm.setNombre(personaBean.getNombre());
			coForm.setApellido1(personaBean.getApellido1() + " " + personaBean.getApellido2());
//			coForm.setApellido2(personaBean.getApellido2());
		}
	}
	

	protected String fijarUltimoLetrado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			ColaGuardiasForm miForm =(ColaGuardiasForm)formulario;
			String guardia = miForm.getIdGuardia();
			Hashtable turnoElegido = (Hashtable)request.getSession().getAttribute("turnoElegido");
			String turno   = (String)turnoElegido.get("IDTURNO");
	
			Hashtable h = new Hashtable ();
			UtilidadesHash.set (h, ScsGuardiasTurnoBean.C_IDGUARDIA, guardia);
			UtilidadesHash.set (h, ScsGuardiasTurnoBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			UtilidadesHash.set (h, ScsGuardiasTurnoBean.C_IDTURNO, turno);
			ScsGuardiasTurnoAdm adm = new ScsGuardiasTurnoAdm (this.getUserBean(request));
			Vector v = adm.selectByPKForUpdate(h);
			if (v != null && v.size() == 1) {
				ScsGuardiasTurnoBean b = (ScsGuardiasTurnoBean) v.get(0);
				b.setIdPersona_Ultimo(new Long(miForm.getIdPersona()));
				if (!adm.update(b)) {
					return exito("messages.updated.error",request);
				}
			}
		}
		catch (Exception e) {
			return exito("messages.updated.error",request);
		}
		return exitoRefresco("messages.updated.success",request);	}
}