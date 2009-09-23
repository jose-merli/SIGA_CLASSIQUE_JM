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

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.atos.utils.GstDate;

;/**
 * Maneja las acciones que se pueden realizar sobre las tablas SCS_SALTOSOCOMPENSACIONES. <br>
 * Implementa la parte de control del caso de uso de Saltos y Compensaciones.
 * 
 * @author david.sanchezp
 * @since 23/1/2005 
 * @version 1.0
 */
public class SaltosYCompensacionesAction extends MasterAction {

	
	/**
	 * No implementado
	 *
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}

	/**
	 * Abre la modal de Mantenimiento recuperando los datos de la fila de la tabla a editar.<br>
	 * Almacena en sesion una hash con los datos del registro que estamos editando.
	 * 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
		ScsSaltosCompensacionesBean beanSaltos = new ScsSaltosCompensacionesBean(); 
		
		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));
		ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		 
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		
		UsrBean usr;
		Vector registros = new Vector();
		Vector ocultos = new Vector();
		Vector visibles = new Vector();
		String motivos="", fechaCumplimiento="";
		
		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			//Obtengo los datos del formulario:
			ocultos = miForm.getDatosTablaOcultos(0);
			visibles = miForm.getDatosTablaVisibles(0);

			//Datos de la Tabla que puedes venir vacios:
			miHash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,((String)ocultos.get(3)).trim());
			miHash.put(ScsSaltosCompensacionesBean.C_MOTIVOS,((String)ocultos.get(7)).trim());
			miHash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,((String)ocultos.get(8)).trim());
			
			//Datos de la Tabla que vienen rellenos:
			miHash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,(String)ocultos.get(0));			
			miHash.put(ScsSaltosCompensacionesBean.C_IDTURNO,(String)ocultos.get(1));
			miHash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,(String)ocultos.get(2));					
			miHash.put(ScsSaltosCompensacionesBean.C_IDPERSONA,(String)ocultos.get(4));
			miHash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,(String)ocultos.get(5));
			miHash.put(ScsSaltosCompensacionesBean.C_FECHA,(String)ocultos.get(6));			
			
			//Almaceno en sesion el registro para futuras modificaciones
			request.getSession().setAttribute("DATABACKUP",miHash);						
			//Datos de la Pantalla:
			miHash.put("NOMBRETURNO",(String)visibles.get(0));
			miHash.put("NOMBREGUARDIA",((String)visibles.get(1)).trim());
			miHash.put("LETRADO",(String)visibles.get(2));
			request.setAttribute("DATOSINICIALES",miHash);
			request.setAttribute("modo","editar");
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}							
		return "mantenimiento";
	}

	/**
	 * Ejecuta el método editar() para abrir en modo Consulta la pantalla de Mantenimiento. Manda el modo a "ver".
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";

		try {
			forward = this.editar(mapping,formulario,request,response);
			request.setAttribute("modo","ver");
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}							
		return forward;
	}

	/**
	 * Inserta "S" en el campos salto para seleccionar el radiobutton de Salto.<br>
	 * Redirije a la pantalla para insertar nuevos Saltos y Compensacioens.
	 * 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws ClsExceptions 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
				
		try {
			miForm.setSalto("S");
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}
		GstDate gstDate = new GstDate();
		
		if(miForm.getFecha()==null || miForm.getFecha().equalsIgnoreCase("")){
			String fecha = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request)); 
			miForm.setFecha(fecha);
		}
			return "nuevo";
	}
		

	/**
	 *
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws SIGAExceptions
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));

		String forward = "exito";
		UsrBean usr = null;
		UserTransaction tx = null;
		Hashtable miHash = new Hashtable();
		String idGuardia = "", idSaltosTurno="", idInstitucion="", idTurno="";
		
		try {			
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			idInstitucion = usr.getLocation();
			idTurno = miForm.getIdTurno().split(",")[1];
			
			//Nuevo identificador de la tabla:
			idSaltosTurno = admSaltosYCompensaciones.getNuevoIdSaltosTurno(idInstitucion,idTurno);
			miHash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,idSaltosTurno);
			
			//Datos a insertar:
			miHash.put(ScsSaltosCompensacionesBean.C_MOTIVOS,miForm.getMotivos());
			miHash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,idInstitucion);
			miHash.put(ScsSaltosCompensacionesBean.C_IDTURNO,idTurno);
			if (miForm.getIdGuardia() != null && !miForm.getIdGuardia().equals(""))
				idGuardia = miForm.getIdGuardia();
			miHash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,idGuardia);			
			miHash.put(ScsSaltosCompensacionesBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFecha()));			
			miHash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,miForm.getSalto());		
			miHash.put(ScsSaltosCompensacionesBean.C_IDPERSONA,miForm.getIdPersona());
			
			tx.begin();
			if (admSaltosYCompensaciones.insert(miHash))
				exitoModal("messages.inserted.success",request);
			else
				exitoModalSinRefresco("messages.inserted.error",request);
			tx.commit();
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return forward;
	}

	/**
	 * Guarda los datos de un Calendario de Guardias en Base de Datos.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));

		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		String forward = "exito";
		UsrBean usr = null;		
		UserTransaction tx = null;
		String fechaCumplimiento = "";
		
		try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				tx=usr.getTransaction();				

				backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");

				//Si la fecha de cumplimiento es vacia:
				if (!miForm.getFechaCumplimiento().equals(""))
					fechaCumplimiento = GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaCumplimiento());
				
				//Datos de la Tabla:
				miHash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION,miForm.getIdInstitucion());			
				miHash.put(ScsSaltosCompensacionesBean.C_IDTURNO,miForm.getIdTurno());
				miHash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,miForm.getIdSaltosTurno());
				miHash.put(ScsSaltosCompensacionesBean.C_IDGUARDIA,miForm.getIdGuardia());			
				miHash.put(ScsSaltosCompensacionesBean.C_IDPERSONA,miForm.getIdPersona());
				miHash.put(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION,miForm.getSalto());
				miHash.put(ScsSaltosCompensacionesBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFecha()));
				miHash.put(ScsSaltosCompensacionesBean.C_MOTIVOS,miForm.getMotivos());
				miHash.put(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO,fechaCumplimiento);
				
				tx=usr.getTransaction();
	            tx.begin();                 
				if (admSaltosYCompensaciones.update(miHash,backupHash)) { 
					forward = exitoModal("messages.updated.success",request);
				} else {
					forward = exitoModalSinRefresco("messages.updated.error",request);
				}
	            tx.commit();

				//Almaceno en sesion el registro para futuras modificaciones
				request.getSession().setAttribute("DATABACKUP",miHash);	            
				request.setAttribute("modo",miForm.getModo());
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return forward;
	}		

	/**
	 * Borra un registro de la tabla.
	 * 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));

		String forward = "exito";
		Hashtable registro = new Hashtable();		
		Vector ocultos = new Vector();		
		UsrBean usr = null;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			//Relleno el hash con los datos clave a borrar
			ocultos = miForm.getDatosTablaOcultos(0);
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_IDINSTITUCION,(String)ocultos.get(0));
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_IDTURNO,(String)ocultos.get(1));
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_IDSALTOSTURNO,(String)ocultos.get(2));
			
			if (admSaltosYCompensaciones.delete(registro))
				forward = exitoRefresco("messages.deleted.success",request);				
			else
				forward = exito("error.messages.deleted",request);
		}
		catch (Exception e){
			throwExcp("messages.deteted.error",e,null);
		}						
		return forward;
	}

	/**
	 * En la pantalla inicial realiza la busqueda de Turnos y Guardias de Letrados con Saltos o Compensaciones.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
				
		UsrBean usr = null;
		Vector salida = new Vector();		
		Hashtable registros = new Hashtable();
		String idTurno="", idGuardia="", idInstitucion="", compensado ="";
		
			try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");					
			
				//Datos para la consulta:
				idInstitucion = usr.getLocation(); 
				if (miForm.getIdTurno() != null && !miForm.getIdTurno().equals(""))
					idTurno = miForm.getIdTurno().split(",")[1];
				if (miForm.getIdGuardia() != null && !miForm.getIdGuardia().equals(""))
					idGuardia = miForm.getIdGuardia();

				UtilidadesHash.set(registros,"IDINSTITUCION",idInstitucion);
				UtilidadesHash.set(registros,"IDTURNO",idTurno);
				UtilidadesHash.set(registros,"IDGUARDIA",idGuardia);
				UtilidadesHash.set(registros,"IDPERSONA",miForm.getIdPersona());
				UtilidadesHash.set(registros,"SALTO",miForm.getSalto());
				UtilidadesHash.set(registros,"FECHADESDE",miForm.getFechaDesde());
				UtilidadesHash.set(registros,"FECHAHASTA",miForm.getFechaHasta());
				//Campo Compensado:
				if (miForm.getCompensado()!=null) compensado="S";
				else compensado="N";
				UtilidadesHash.set(registros,"COMPENSADO",compensado);
				
			    //SELECT
				salida = admSaltosYCompensaciones.selectGenerico(admSaltosYCompensaciones.buscar(registros));
			
				request.setAttribute("resultado",salida);			
				request.setAttribute("modo",miForm.getModo());
			}
			catch  (Exception e){
				throwExcp("messages.select.error",e,null);	
			}			
			return "listado";
	}

	/**
	 * No implementado
	 *
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/**
	 * Inicializa el radiobutton y el checkbox del formulario inicial.
	 *
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		Boolean bool = new Boolean(false);
		
		try {
			miForm.setSalto("S");
			miForm.setCompensado(bool);
		}
		catch  (Exception e){
			throwExcp("messages.select.error",e,null);	
		}	
		return "inicio";
	}
}