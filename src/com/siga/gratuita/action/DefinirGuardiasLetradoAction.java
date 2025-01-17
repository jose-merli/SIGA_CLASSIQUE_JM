package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirGuardiasLetradoForm;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;


/**
 * En el caso de que no haya ningun turno almacenado como variable de sesion, es porque 
 * estan pidiendo todos las guardias en las que est� inscrita una persona
 * 
 * @author ruben.fernandez
 * 
 */

public class DefinirGuardiasLetradoAction extends MasterAction {
	
	/**
	 * Desde las pestanhas no es necesario pasar un formulario.
	 * Este execute lanza el abrir de este action en caso de que no se reciba ningun formulario.
	 * En caso contrario, lanza el execute normal del MasterAction.
	 */
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		String numero = "";
		String nombre = "";
		String estado = "";
		CenColegiadoBean datosColegiales;		
		
		try {
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				try {
					// Preparo para obtener la informacion del colegiado:
					Long idPers = new Long(request.getParameter("idPersonaPestanha"));
					Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
					CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
					estado = clienteAdm.getEstadoColegial(String.valueOf(idPers), String.valueOf(idInstPers));
				} catch (Exception e1){
					try {
						Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
						nombre = (String)datosColegiado.get("NOMBRECOLEGIADO");
						numero = (String)datosColegiado.get("NUMEROCOLEGIADO");
						estado = (String)datosColegiado.get("ESTADOCOLEGIAL");
					}catch(Exception e2){
						nombre = "";
						numero = "";
						estado ="";
					}
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			Hashtable datosColegiado = new Hashtable();
			datosColegiado.put("NOMBRECOLEGIADO",nombre);
			datosColegiado.put("NUMEROCOLEGIADO",numero);
			datosColegiado.put("ESTADOCOLEGIAL",estado!=null?estado:"");
			request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
			
			DefinirGuardiasLetradoForm miForm = null;
			miForm = (DefinirGuardiasLetradoForm) formulario;
			if (miForm != null) {
				if(miForm.getModo()!=null && miForm.getModo().equals("abrirGuardias")){
					return mapping.findForward(this.abrirGuardias(mapping,miForm,request,response));
				}else{
					//aalg: INC_08112_SIGA. Al entrar en la p�gina debe aparecer siempre la fecha del sistema
//					String fechaConsultaTurno =  (String)request.getSession().getAttribute("fechaConsultaInscripcionTurno");
//					if(fechaConsultaTurno!=null)
//						miForm.setFechaConsulta(fechaConsultaTurno);
//					else
					miForm.setFechaConsulta("sysdate");
					return super.executeInternal(mapping, formulario,request,response);
				}
				
			}
			else{
				return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
			}
			
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicaci�n",e);
		}
	}
	
	protected String abrirGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return this.abrir(mapping,formulario,request,response);
	}
	
	/**
	 * Esta clase se encarga de hacer una consulta para sacar todas las guardias
	 * para un turno seleccionado, o todas las guardias en las que est� o ha estado
	 * inscrito el letrado logado. 
	 * El resutado lo manda como variable del request a la jsp que se encargar� de mostrar una tabla.
	 * No se espera un formulario porque los dato de consulta se obtienen del UsrBean y de una variable de sesion
	 * que corresponder�a con el turno seleccionado.
	 */ 
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward="exception";
		String numero = "";
		String nombre = "";
		CenColegiadoBean datosColegiales;		
		DefinirGuardiasLetradoForm miForm = (DefinirGuardiasLetradoForm)formulario;

		try {
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				try {
					// Preparo para obtener la informacion del colegiado:
					Long idPers = new Long(request.getParameter("idPersonaPestanha"));
					Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
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
			
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector visibles = (Vector)request.getSession().getAttribute("campos");
			request.removeAttribute("DATABACKUPPESTANA");
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			String consulta="";
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			
			
			String fecha = miForm.getFechaConsulta();
			//aalg: INC_8988_SIGA.
			consulta =  " SELECT " + guardias.getCamposTabla(ScsGuardiasTurnoAdm.CAMPOS_LISTAINSCRIPCIONES) + ", " +
					" (SELECT IT.FECHAVALIDACION  " +
					" FROM SCS_INSCRIPCIONTURNO IT " +
					" WHERE IT.IDPERSONA = SCS_INSCRIPCIONGUARDIA.IDPERSONA " +
						" AND IT.IDINSTITUCION = SCS_INSCRIPCIONGUARDIA.IDINSTITUCION " +
						" AND IT.IDTURNO = SCS_INSCRIPCIONGUARDIA.IDTURNO " +
						" AND IT.FECHABAJA IS NULL " +
						" AND IT.FECHADENEGACION IS NULL " +
						" AND ROWNUM = 1) validacionTurno " +					
                        
				" FROM SCS_GUARDIASTURNO, " +
					" SCS_INSCRIPCIONGUARDIA, " +
					" SCS_TURNO " +
					
				" WHERE SCS_TURNO.IDINSTITUCION = SCS_GUARDIASTURNO.IDINSTITUCION"+
					" AND SCS_TURNO.IDTURNO = SCS_GUARDIASTURNO.IDTURNO"+
					" AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_INSCRIPCIONGUARDIA.IDINSTITUCION"+
					" AND SCS_GUARDIASTURNO.IDTURNO = SCS_INSCRIPCIONGUARDIA.IDTURNO"+
					" AND SCS_GUARDIASTURNO.IDGUARDIA  = SCS_INSCRIPCIONGUARDIA.IDGUARDIA"+
					" AND SCS_INSCRIPCIONGUARDIA.IDINSTITUCION = " + usr.getLocation() + //la de la sesi�n
					" AND SCS_INSCRIPCIONGUARDIA.IDPERSONA = " + idPersona; // la del colegiado
					
			if(miForm==null || miForm.getBajaLogica()==null || miForm.getBajaLogica().equals("N")){
				//String fechaFmt =GstDate.getApplicationFormatDate("", fecha);
				if(fecha==null || fecha.equalsIgnoreCase("sysdate"))
					fecha = "trunc(sysdate)";
				else
					fecha = "'"+fecha+"'";
					
				// por defecto no se muestran las guardias de baja
				consulta += " AND nvl(SCS_GUARDIASTURNO.FECHABAJA, '31/12/2999') > " + fecha.trim();
				
				consulta += " AND ( ";
					
				// ALTA PENDIENTE 
				consulta += " (SCS_INSCRIPCIONGUARDIA.FECHADENEGACION IS NULL " +
					" AND SCS_INSCRIPCIONGUARDIA.FECHAVALIDACION IS NULL " +
					" AND SCS_INSCRIPCIONGUARDIA.FECHABAJA IS NULL) ";
				// O FECHAS DE ALTA DENTRO DEL PERIODO DE VIDA DE LA GUARDIA
				consulta += " OR (SCS_INSCRIPCIONGUARDIA.FECHAVALIDACION IS NOT NULL " +
					" AND TRUNC(SCS_INSCRIPCIONGUARDIA.FECHAVALIDACION) <= " + fecha.trim() + 
					" AND (SCS_INSCRIPCIONGUARDIA.FECHABAJA IS NULL " + 
					" OR TRUNC(SCS_INSCRIPCIONGUARDIA.FECHABAJA) >= " + fecha.trim() + "))) ";
			}
									
//								" ORDER BY SCS_INSCRIPCIONGUARDIA.FECHABAJA DESC, SCS_TURNO.NOMBRE";
			consulta+= " ORDER BY GUARDIA ,SCS_INSCRIPCIONGUARDIA.FECHASUSCRIPCION ,SCS_TURNO.NOMBRE, "+ ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE;
			
			Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
			request.getSession().setAttribute("resultado",resultado);
			request.getSession().setAttribute("fechaConsultaInscripcionTurno",miForm.getFechaConsulta());
			forward="listado";
			
		} catch(Exception e) {
			throwExcp("messages.select.error", e, null);
		}
		
		return forward;
	}
	
	/**
	 * Antes de ver los datos de la guardia seleccionada se hace una consulta a la bbdd para recuperar los campos
	 * que faltan.
	 */
	protected String ver(ActionMapping mapping, 
						 MasterForm formulario, 
						 HttpServletRequest request, 
						 HttpServletResponse response) throws ClsExceptions {
		
		String forward="exception";
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		//DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		//ScsTurnoAdm admTurno = new ScsTurnoAdm(this.getUserBean(request));
		Vector ocultos = (Vector) formulario.getDatosTablaOcultos(0);
		Hashtable hash = new Hashtable();
		
		hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
		hash.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)ocultos.get(0));
		hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA,(String)ocultos.get(1));
      
		try{
			ScsGuardiasTurnoBean resultado = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);			//aqui est� seleccionada la guardia que queremos editar
			//Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
			//String condicion =" where idinstitucion = "+usr.getLocation()+" and idturno="+turno.get("IDTURNO")+" ";
			//ScsTurnoBean miTurno = (ScsTurnoBean)((Vector)admTurno.select(condicion)).get(0);
			//request.getSession().setAttribute("turnoElegido",miTurno);
			request.getSession().setAttribute("DATABACKUPPESTANA",resultado);
			request.getSession().setAttribute("modo","ver");
			forward="ver";
		}catch(Exception e){
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return forward;
	}
		
}