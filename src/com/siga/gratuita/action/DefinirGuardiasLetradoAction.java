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


/**
 * En el caso de que no haya ningun turno almacenado como variable de sesion, es porque 
 * estan pidiendo todos las guardias en las que está inscrita una persona
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
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				} catch (Exception e1){
					try {
						Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
						nombre = (String)datosColegiado.get("NOMBRECOLEGIADO");
						numero = (String)datosColegiado.get("NUMEROCOLEGIADO");
					}catch(Exception e2){
						nombre = "";
						numero = "";
					}
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			Hashtable datosColegiado = new Hashtable();
			datosColegiado.put("NOMBRECOLEGIADO",nombre);
			datosColegiado.put("NUMEROCOLEGIADO",numero);
			request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
			
			MasterForm miForm = null;
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				return super.executeInternal(mapping, formulario,request,response);
			}
			else{
				return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
			}
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
	}
	
	
	/**
	 * Esta clase se encarga de hacer una consulta para sacar todas las guardias
	 * para un turno seleccionado, o todas las guardias en las que está o ha estado
	 * inscrito el letrado logado. 
	 * El resutado lo manda como variable del request a la jsp que se encargará de mostrar una tabla.
	 * No se espera un formulario porque los dato de consulta se obtienen del UsrBean y de una variable de sesion
	 * que correspondería con el turno seleccionado.
	 */ 
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String forward="exception";
		String numero = "";
		String nombre = "";
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
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				} catch (Exception e1){
					nombre = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
					numero = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			request.setAttribute("NOMBRECOLEGPESTAÑA", nombre);
			request.setAttribute("NUMEROCOLEGPESTAÑA", numero);		
			
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector visibles = (Vector)request.getSession().getAttribute("campos");
			request.removeAttribute("DATABACKUPPESTANA");
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			String consulta="";
			ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			consulta =  " SELECT "+guardias.getCamposTabla(2)+", "+
	                    " (select s."+ScsInscripcionTurnoBean.C_FECHAVALIDACION+
                        "  from "+ScsInscripcionTurnoBean.T_NOMBRETABLA+" s "+
                        "  where s."+ScsInscripcionTurnoBean.C_IDPERSONA+" = "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDPERSONA+
                        "   and  s."+ScsInscripcionTurnoBean.C_IDINSTITUCION+" = "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDINSTITUCION+
                        "   and s."+ScsInscripcionTurnoBean.C_IDTURNO+" = "+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDTURNO+
						"   and s."+ScsInscripcionTurnoBean.C_FECHABAJA+ " is null) validacionTurno"+  		               
								" FROM SCS_GUARDIASTURNO ,SCS_INSCRIPCIONGUARDIA, SCS_TURNO"+
								" WHERE SCS_TURNO.IDINSTITUCION = SCS_GUARDIASTURNO.IDINSTITUCION"+
								" AND SCS_TURNO.IDTURNO = SCS_GUARDIASTURNO.IDTURNO"+
								" AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_INSCRIPCIONGUARDIA.IDINSTITUCION"+
								" AND SCS_GUARDIASTURNO.IDTURNO = SCS_INSCRIPCIONGUARDIA.IDTURNO"+
								" AND SCS_GUARDIASTURNO.IDGUARDIA  = SCS_INSCRIPCIONGUARDIA.IDGUARDIA"+
								" AND SCS_INSCRIPCIONGUARDIA.IDINSTITUCION = "+usr.getLocation()+//la de la sesión
								" AND SCS_INSCRIPCIONGUARDIA.IDPERSONA = "+idPersona+ // la del colegiado
								" AND SCS_INSCRIPCIONGUARDIA.FECHABAJA IS NULL "+ // que este inscrito el colegiado
//								" ORDER BY SCS_INSCRIPCIONGUARDIA.FECHABAJA DESC, SCS_TURNO.NOMBRE";
								" ORDER BY SCS_TURNO.NOMBRE, "+ ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE;
			
			Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
			request.getSession().setAttribute("resultado",resultado);
			forward="listado";
		}catch(Exception e){
			throwExcp("messages.select.error",e,null);
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
			ScsGuardiasTurnoBean resultado = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);			//aqui está seleccionada la guardia que queremos editar
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