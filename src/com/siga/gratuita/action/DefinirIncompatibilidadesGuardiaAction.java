package com.siga.gratuita.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.*;
import com.atos.utils.*;

import org.apache.struts.action.*;

import java.util.*;

import com.siga.beans.*;

/**
 * Maneja las acciones que se pueden realizar sobre las tablas SCS_INCOMPATIBILIDADGUARDIAS
 * Implementa la parte de control del caso de uso de Definir Incompatibilidades de Guardias.
 * Se centra en el mantenimiento de las guardias incompatibles.
 * Terminado el 18-1-2005.
 * 
 * @author david.sanchezp
 * @since 13/1/2005 
 * @version 1.0
 */
public class DefinirIncompatibilidadesGuardiaAction extends MasterAction {

	/**
	 * Hace una busqueda de las guardias que son incompatibles. 
	 * Se corresponde con la busqueda de la primera pantalla del caso de uso.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirIncompatibilidadesGuardiaForm miForm = (DefinirIncompatibilidadesGuardiaForm) formulario;
		ScsIncompatibilidadGuardiasAdm admIncopatibilidadGuardia = new ScsIncompatibilidadGuardiasAdm(this.getUserBean(request));
		
		String forward = "buscarInicio";
		UsrBean usr;
		Vector salida = new Vector();
		String idinstitucion="", idturno="", idguardia="";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				
			idinstitucion = miForm.getIdInstitucionPestanha();
			idturno = miForm.getIdTurnoPestanha();
			idguardia = miForm.getIdGuardiaPestanha();
			
			//SELECT			
			salida = admIncopatibilidadGuardia.selectGenericoNLS(admIncopatibilidadGuardia.buscarGuardiasIncompatibles(idinstitucion,idturno,idguardia));						
			request.setAttribute("resultado",salida);
				
			//obteniendo y pasando por sesion los nombres de turno y guardia
			Hashtable hashTurno = new Hashtable ();
			hashTurno.put (ScsTurnoBean.C_IDINSTITUCION, miForm.getIdInstitucionPestanha ());
			hashTurno.put (ScsTurnoBean.C_IDTURNO, miForm.getIdTurnoPestanha ());
			ScsTurnoBean beanTurno = (ScsTurnoBean) (new ScsTurnoAdm (usr).select (hashTurno)).get (0);
			request.setAttribute ("NOMBRETURNO", beanTurno.getNombre ());
			Hashtable hashGuardia = new Hashtable ();
			hashGuardia.put (ScsGuardiasTurnoBean.C_IDINSTITUCION, miForm.getIdInstitucionPestanha ());
			hashGuardia.put (ScsGuardiasTurnoBean.C_IDTURNO, miForm.getIdTurnoPestanha ());
			hashGuardia.put (ScsGuardiasTurnoBean.C_IDGUARDIA, (String) miForm.getIdGuardiaPestanha ());
			ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) (new ScsGuardiasTurnoAdm (usr).select (hashGuardia)).get (0);
			request.setAttribute ("NOMBREGUARDIA", beanGuardia.getNombre ());
	        
			request.setAttribute("modo",miForm.getAccion());				
			forward = "buscarInicio";
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}												
		return forward;		
	}

	/**
	 * Navegaciona a la ventana modal para incluir una guardia como incompatible.
	 * Voy arrastrando los parametros de la pestanha (guardia editada): IDINSTITUCION, IDTURNO, IDGUARDIA
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirIncompatibilidadesGuardiaForm miForm = (DefinirIncompatibilidadesGuardiaForm) formulario;

		Hashtable miHash = new Hashtable();
		String forward = "error";
		UsrBean usr;
		
		
		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			request.setAttribute("IDINSTITUCIONPESTAÑA",miForm.getIdInstitucionPestanha());
			request.setAttribute("IDTURNOPESTAÑA",miForm.getIdTurnoPestanha());
			request.setAttribute("IDGUARDIAPESTAÑA",miForm.getIdGuardiaPestanha());
			request.setAttribute("modo",miForm.getModo());
			forward = "modal";
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}							
		return forward;
	}

	/**
	 * Anhade a una tabla hash los datos necesarios para hacer una insercion en la tabla SCS_INCOMPATIBILIDADGUARDIAS de una nueva <br> 
	 * guardia incompatible con la que hemos editado anteriormente.
	 * Se cerrara la modal despues de la insercion refrescando la tabla de guardias incompatibles.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirIncompatibilidadesGuardiaForm miForm = (DefinirIncompatibilidadesGuardiaForm) formulario;
		ScsIncompatibilidadGuardiasAdm admIncopatibilidadGuardia = new ScsIncompatibilidadGuardiasAdm(this.getUserBean(request));
		
		String forward = "error";
		Hashtable miHash = new Hashtable();
		UserTransaction tx = null;
		UsrBean usr;
		String idincompatibilidad = "", idinstitucion_pestanha="", idguardia_pestanha="", idturno_pestanha="";
		Vector registros = new Vector();
		
		try {			
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();
			Vector v=new Vector();
			
			//Valores obtenidos de la pestanha
			idinstitucion_pestanha = miForm.getIdInstitucionPestanha();
			idturno_pestanha = miForm.getIdTurnoPestanha();
			idguardia_pestanha = miForm.getIdGuardiaPestanha();
			
			//Calculo el id de idincompatibilidad
			
			
			String datos=request.getParameter("datosModificados");
			String datosAux[]=datos.split("#;#");
			//Si no hay elementos le asigno el primero: 1
			if (idincompatibilidad.equals("")) idincompatibilidad = "1";
			tx=usr.getTransaction();
            tx.begin(); 
			for (int i=0;i<datosAux.length;i++){
				String datosAux1 []=datosAux[i].split("#_#");
				//registros = admIncopatibilidadGuardia.selectGenerico(admIncopatibilidadGuardia.getIdIncompatibilidad(idinstitucion_pestanha,idturno_pestanha,idguardia_pestanha));
				idincompatibilidad = (String)(((Hashtable)registros.get(0)).get("IDINCOMPATIBILIDAD"));
				if (idincompatibilidad.equals("")) idincompatibilidad = "1";
				miHash.put("IDINCOMPATIBILIDAD",idincompatibilidad);
				miHash.put(ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION,idinstitucion_pestanha);
				miHash.put(ScsIncompatibilidadGuardiasBean.C_IDTURNO,idturno_pestanha);
				miHash.put(ScsIncompatibilidadGuardiasBean.C_IDGUARDIA,idguardia_pestanha);
				//miHash.put(ScsIncompatibilidadGuardiasBean.C_IDINCOMPATIBILIDAD,idincompatibilidad);
				miHash.put(ScsIncompatibilidadGuardiasBean.C_MOTIVOS,miForm.getMotivos());
				/*miHash.put(ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE,miForm.getIdGuardiaIncompatible());
				miHash.put(ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE,miForm.getIdTurnoIncompatible());*/
				miHash.put(ScsIncompatibilidadGuardiasBean.C_IDGUARDIA_INCOMPATIBLE,datosAux1[1]);
				miHash.put(ScsIncompatibilidadGuardiasBean.C_IDTURNO_INCOMPATIBLE,datosAux1[0]);
				miHash.put(ScsIncompatibilidadGuardiasBean.C_USUMODIFICACION,usr.getUserName());
				miHash.put(ScsIncompatibilidadGuardiasBean.C_FECHAMODIFICACION,"sysdate");
				
				//INSERT (INICIO TRANSACCION)
				                
				if (admIncopatibilidadGuardia.insert(miHash)) { 
					request.setAttribute("mensaje","messages.inserted.success");
				} else {
					request.setAttribute("mensaje","messages.inserted.error");
				}
			}
            tx.commit();
	            
		    request.setAttribute("modal", "1");
		    request.setAttribute("modo",miForm.getModo());						
		       
			forward = "exito";
		} 
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		
		return forward;
	}

	/**
	 * Borra una guardia incompatible de la tabla SCS_INCOMPATIBILIDADGUARDIAS.
	 * Se hace un refresco en el iframe de la tabla de guardias incompatibles.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirIncompatibilidadesGuardiaForm miForm = (DefinirIncompatibilidadesGuardiaForm) formulario;
		ScsIncompatibilidadGuardiasAdm admIncopatibilidadGuardia = new ScsIncompatibilidadGuardiasAdm(this.getUserBean(request));
		
		String forward = "exito";
		Hashtable miHash = new Hashtable();
		Vector ocultos = new Vector();
		UsrBean usr;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			ocultos = miForm.getDatosTablaOcultos(0);					
			miHash.clear();							
			//miHash.put(ScsIncompatibilidadGuardiasBean.C_IDINCOMPATIBILIDAD,ocultos.get(0));
			miHash.put(ScsIncompatibilidadGuardiasBean.C_IDTURNO,ocultos.get(1));
			miHash.put(ScsIncompatibilidadGuardiasBean.C_IDGUARDIA,ocultos.get(2));
			miHash.put(ScsIncompatibilidadGuardiasBean.C_IDINSTITUCION,ocultos.get(3));
			//DELETE
			if (admIncopatibilidadGuardia.delete(miHash))
				request.setAttribute("mensaje","messages.deleted.success");
			else
			    request.setAttribute("mensaje","error.messages.deleted");
				
	        request.setAttribute("hiddenFrame", "1");
	        request.setAttribute("modo",miForm.getModo());
		}
		catch (Exception e){
			throwExcp("messages.deleted.error",e,null);
		}
					
		return forward;
	}


	/**
	 * Realiza la busqueda en la modal de las guardias que pueden ser asignadas como incompatibles.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirIncompatibilidadesGuardiaForm miForm = (DefinirIncompatibilidadesGuardiaForm) formulario;
		ScsIncompatibilidadGuardiasAdm admIncopatibilidadGuardia = new ScsIncompatibilidadGuardiasAdm(this.getUserBean(request));
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();
		Hashtable hashFormulario = new Hashtable();
		UsrBean usr;
		String forward = "error";
		
		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			

			//Datos necesarios para la consulta
			miHash.put("IDINSTITUCION",usr.getLocation());
			miHash.put("IDINSTITUCIONPESTAÑA",miForm.getIdInstitucionPestanha());
			miHash.put("IDTURNOPESTAÑA",miForm.getIdTurnoPestanha());
			miHash.put("IDGUARDIAPESTAÑA",miForm.getIdGuardiaPestanha());
			miHash.put("ABREVIATURA",miForm.getAbreviatura());
			miHash.put("TURNO",miForm.getTurno());
			miHash.put("ZONA",miForm.getZona());
			miHash.put("SUBZONA",miForm.getSubzona());
			miHash.put("AREA",miForm.getArea());
			miHash.put("MATERIA",miForm.getMateria());
							
			v = admIncopatibilidadGuardia.selectGenericoNLS(admIncopatibilidadGuardia.buscarGuardiasModal(miHash));
			request.setAttribute("resultado",v);
			
			request.setAttribute("modo",miForm.getModo());				
			forward = "buscarModal";
		} 
		catch (Exception e){
			throwExcp("messages.select.error",e,null);
		}
		
		return forward;
	}

	/**
	 * No implementado
	 *
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirIncompatibilidadesGuardiaForm miForm = (DefinirIncompatibilidadesGuardiaForm) formulario;
		
		try {
			miForm.setIdInstitucionPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDINSTITUCION));
			miForm.setIdTurnoPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDTURNO));
			miForm.setIdGuardiaPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDGUARDIA));
			//Paso el modo de la pestanha:
			request.setAttribute("MODOPESTANA", request.getParameter("MODOPESTANA"));
		} catch (Exception e) {
			throwExcp("error.messages.editar",e,null);
		}
		return "inicio";	
	}
}