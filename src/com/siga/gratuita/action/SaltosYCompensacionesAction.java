package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Date;
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

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsSaltoCompensacionGrupoAdm;
import com.siga.beans.ScsSaltoCompensacionGrupoBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.SaltosYCompensacionesForm;



;/**
 * Maneja las acciones que se pueden realizar sobre las tablas SCS_SALTOSOCOMPENSACIONES. <br>
 * Implementa la parte de control del caso de uso de Saltos y Compensaciones.
 * 
 * @author david.sanchezp
 * @since 23/1/2005 
 * @version 1.0
 */
public class SaltosYCompensacionesAction extends MasterAction {

	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if(miForm.getModo()!=null && miForm.getModo().equals("nuevoGrupos")){
				return mapping.findForward(this.nuevoGrupos(mapping, miForm, request, response));
			}else if(miForm.getModo()!=null && miForm.getModo().equals("insertarGrupos")){
				return mapping.findForward(this.insertarGrupo(mapping, miForm, request, response));
			}else if(miForm.getModo()!=null && miForm.getModo().equals("modificarGrupos")){
				return mapping.findForward(this.modificarGrupo(mapping, miForm, request, response));				
			}else if (miForm.getModo()!=null && miForm.getModo().equalsIgnoreCase("getAjaxLetradosInscritos")){
				ClsLogging.writeFileLog("SALTOS Y COMPENSACION:getAjaxLetradosInscritos", 10);
				this.getAjaxLetradosInscritos(mapping, miForm, request, response);
				return null;
			} else 
				return super.executeInternal(mapping, formulario, request, response);
		} catch (SIGAException e) {
			throw e;
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		}
	}
	
	
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
		Vector ocultos = new Vector();
		ocultos = miForm.getDatosTablaOcultos(0);
		String idGrupoGuardia = (String)ocultos.get(3);
		//Si viene idpersona la fila seleccionada no es de grupos
		if(idGrupoGuardia.equals("null")){
			ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));

			ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));
			ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.getUserBean(request));

			Hashtable miHash = new Hashtable();
			Hashtable backupHash = new Hashtable();

			UsrBean usr;
			Vector registros = new Vector();

			Vector visibles = new Vector();
			String motivos = "", fechaCumplimiento = "";

			try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

				// Obtengo los datos del formulario:
				visibles = miForm.getDatosTablaVisibles(0);


				//mhg - INC_05540_SIGA Se ha modificado la forma de obtener los datos. Ahora no se cogen del vector oculto del jsp.
				//Obtenemos un objeto de tipo beanSaltoCompensacion y apartir de ahí rellenamos el miHash.
				Hashtable hash = new Hashtable();
				hash.put(ScsSaltosCompensacionesBean.C_IDINSTITUCION, (String) ocultos.get(0));
				hash.put(ScsSaltosCompensacionesBean.C_IDTURNO, (String) ocultos.get(1));
				hash.put(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, (String) ocultos.get(2));	
				ScsSaltosCompensacionesBean beanSaltoCompensacion = (ScsSaltosCompensacionesBean) admSaltosYCompensaciones.select(hash).get(0);
				
				
				// Datos de la Tabla que puedes venir vacios:
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_IDGUARDIA, beanSaltoCompensacion.getIdGuardia());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_MOTIVOS,  beanSaltoCompensacion.getMotivos().trim());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, beanSaltoCompensacion.getFechaCumplimiento().trim());

				// Datos de la Tabla que vienen rellenos:
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_IDINSTITUCION, beanSaltoCompensacion.getIdInstitucion());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_IDTURNO, beanSaltoCompensacion.getIdTurno());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, beanSaltoCompensacion.getIdSaltosTurno());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_IDPERSONA, beanSaltoCompensacion.getIdPersona());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION, beanSaltoCompensacion.getSaltoCompensacion());
				UtilidadesHash.set(miHash, ScsSaltosCompensacionesBean.C_FECHA, beanSaltoCompensacion.getFecha());
				

				// Almaceno en sesion el registro para futuras modificaciones
				request.getSession().setAttribute("DATABACKUP", miHash);
				// Datos de la Pantalla:
				miHash.put("NOMBRETURNO", (String) visibles.get(0));
				miHash.put("NOMBREGUARDIA", ((String) visibles.get(1)).trim());
				miHash.put("LETRADO", (String) visibles.get(2));
				request.setAttribute("DATOSINICIALES", miHash);
				request.setAttribute("modo", "editar");
			} catch (Exception e) {
				throwExcp("messages.select.error", e, null);
			}

			return "mantenimiento";
			
		}else{
			return this.editarGrupos(mapping, formulario, request, response);
		
		}
	}

	/**
	 * Abre la modal de Mantenimiento recuperando los datos de la fila de la tabla a editar.<br>
	 * Almacena en sesion una hash con los datos del registro que estamos editando.
	 * 
	 */
	protected String editarGrupos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltoCompensacionGrupoAdm admSaltosYCompensaciones = new ScsSaltoCompensacionGrupoAdm(this.getUserBean(request));
		
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

			
			//mhg - INC_05540_SIGA Se ha modificado la forma de obtener los datos. Ahora no se cogen del vector oculto del jsp.
			//Obtenemos un objeto de tipo beanSaltoCompensacion y apartir de ahí rellenamos el miHash.
			Hashtable hash = new Hashtable();
			hash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO,(String)ocultos.get(4));			
			ScsSaltoCompensacionGrupoBean beanSaltoCompensacionGrupo = (ScsSaltoCompensacionGrupoBean) admSaltosYCompensaciones.select(hash).get(0);
			
			// Datos de la Tabla que puedes venir vacios:
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_IDGUARDIA, beanSaltoCompensacionGrupo.getIdGuardia());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_MOTIVO,  beanSaltoCompensacionGrupo.getMotivo().trim());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO, beanSaltoCompensacionGrupo.getFechaCumplimiento().trim());
			
			//Datos de la Tabla que vienen rellenos:
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION, beanSaltoCompensacionGrupo.getIdInstitucion());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_IDTURNO, beanSaltoCompensacionGrupo.getIdTurno());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA, beanSaltoCompensacionGrupo.getIdGrupoGuardia());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, beanSaltoCompensacionGrupo.getSaltoCompensacion());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_FECHA,beanSaltoCompensacionGrupo.getFecha());
			UtilidadesHash.set(miHash, ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO, beanSaltoCompensacionGrupo.getIdSaltoCompensacionGrupo());
			
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
		return "mantenimientoGrupos";
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
			request.setAttribute("modo","nuevo");
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}
		GstDate gstDate = new GstDate();
		
		if(miForm.getFecha()==null || miForm.getFecha().equalsIgnoreCase("")){
			String fecha = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request)); 
			miForm.setFecha(fecha);
		}
			return "mantenimiento";
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
	protected String nuevoGrupos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
				
		try {
			miForm.setSalto("S");
			request.setAttribute("modo","nuevo");
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}
		GstDate gstDate = new GstDate();
		
		if(miForm.getFecha()==null || miForm.getFecha().equalsIgnoreCase("")){
			String fecha = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request)); 
			miForm.setFecha(fecha);
		}
			return "mantenimientoGrupos";
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
	protected String insertarGrupo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltoCompensacionGrupoAdm admSaltosYCompensacionesGrupo = new ScsSaltoCompensacionGrupoAdm(this.getUserBean(request));

		String forward = "exito";
		UsrBean usr = null;
		UserTransaction tx = null;
		Hashtable miHash = new Hashtable();
		String idGuardia = "", idSaltosTurno="", idInstitucion="", idTurno="";
		
		try {			
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			idInstitucion = usr.getLocation();
			String ids = miForm.getIdGrupoGuardia();
			String[] reg = ids.split(",");
			String salto = miForm.getSalto();
			
			//Nuevo identificador de la tabla:
			idSaltosTurno = admSaltosYCompensacionesGrupo.getNuevoIdSaltoCompensacionGrupo();
			miHash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO,idSaltosTurno);
			
			//Datos a insertar:
			miHash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVO,miForm.getMotivos());
			miHash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,reg[0]);
			miHash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO,reg[1]);
			miHash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA,reg[2]);			
			miHash.put(ScsSaltoCompensacionGrupoBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFecha()));
			miHash.put(ScsSaltoCompensacionGrupoBean.C_FECHACREACION,"sysdate");
			miHash.put(ScsSaltoCompensacionGrupoBean.C_USUCREACION,usr.getUserName());
			if(miForm.getSalto()!=null && miForm.getSalto().equalsIgnoreCase("SG")){
				salto = "S";
			}else if (miForm.getSalto()!=null && miForm.getSalto().equalsIgnoreCase("CG")){
				salto = "C";
			}
			miHash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION,salto);		
			miHash.put(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA,reg[3]);
			
			tx.begin();
			if (admSaltosYCompensacionesGrupo.insert(miHash))
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
				miHash.put(ScsSaltosCompensacionesBean.C_TIPOMANUAL, (fechaCumplimiento == null || fechaCumplimiento.equals("")) ? "0" : "1");				
				
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
	 * Guarda los datos de un Calendario de Guardias en Base de Datos.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificarGrupo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltoCompensacionGrupoAdm admSaltosYCompensaciones = new ScsSaltoCompensacionGrupoAdm(this.getUserBean(request));

		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		String forward = "exito";
		UsrBean usr = null;		
		UserTransaction tx = null;
		String fechaCumplimiento = "";
		
		try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				tx=usr.getTransaction();				
				String salto = miForm.getSalto();
				backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");
				String saltoBackup = (String)backupHash.get(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION);
				if (saltoBackup != null && saltoBackup.equalsIgnoreCase("SG")) {
					backupHash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, "S");
				} else if (saltoBackup != null && saltoBackup.equalsIgnoreCase("CG")) {
					backupHash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION, "C");
				}
				//Si la fecha de cumplimiento es vacia:
				if (!miForm.getFechaCumplimiento().equals(""))
					fechaCumplimiento = GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaCumplimiento());
				
				//Datos de la Tabla:
				miHash.put(ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,miForm.getIdInstitucion());			
				miHash.put(ScsSaltoCompensacionGrupoBean.C_IDTURNO,miForm.getIdTurno());
				miHash.put(ScsSaltoCompensacionGrupoBean.C_IDGUARDIA,miForm.getIdGuardia());			
				miHash.put(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO,miForm.getIdSaltoCompensacionGrupo());			
				miHash.put(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA,miForm.getIdGrupoGuardia());
				if (miForm.getSalto() != null && miForm.getSalto().equalsIgnoreCase("SG")) {
					salto = "S";
				} else if (miForm.getSalto() != null && miForm.getSalto().equalsIgnoreCase("CG")) {
					salto = "C";
				}
				miHash.put(ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION,salto);
				miHash.put(ScsSaltoCompensacionGrupoBean.C_FECHA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFecha()));
				miHash.put(ScsSaltoCompensacionGrupoBean.C_MOTIVO,miForm.getMotivos());
				miHash.put(ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO,fechaCumplimiento);
				miHash.put(ScsSaltoCompensacionGrupoBean.C_TIPOMANUAL, (fechaCumplimiento == null || fechaCumplimiento.equals("")) ? "0" : "1");	
				
				tx=usr.getTransaction();
	            tx.begin();                 
				if (admSaltosYCompensaciones.update(miHash,backupHash)){ 
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

		Vector ocultos = new Vector();
		ocultos = miForm.getDatosTablaOcultos(0);
		String idGrupoGuardia = (String) ocultos.get(3);
		// Si viene idpersona la fila seleccionada no es de grupos
		if (idGrupoGuardia.equals("null")) {

			ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));

			String forward = "exito";
			Hashtable registro = new Hashtable();
			UsrBean usr = null;

			try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

				// Relleno el hash con los datos clave a borrar
				UtilidadesHash.set(registro, ScsSaltosCompensacionesBean.C_IDINSTITUCION, (String) ocultos.get(0));
				UtilidadesHash.set(registro, ScsSaltosCompensacionesBean.C_IDTURNO, (String) ocultos.get(1));
				UtilidadesHash.set(registro, ScsSaltosCompensacionesBean.C_IDSALTOSTURNO, (String) ocultos.get(2));

				if (admSaltosYCompensaciones.delete(registro))
					forward = exitoRefresco("messages.deleted.success", request);
				else
					forward = exito("error.messages.deleted", request);
			} catch (Exception e) {
				throwExcp("messages.deteted.error", e, null);
			}
			return forward;

		} else {
			return this.borrarGrupo(mapping, formulario, request, response);
		}

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
	protected String borrarGrupo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		ScsSaltoCompensacionGrupoAdm admSaltosYCompensaciones = new ScsSaltoCompensacionGrupoAdm(this.getUserBean(request));

		String forward = "exito";
		Hashtable registro = new Hashtable();		
		Vector ocultos = new Vector();		
		UsrBean usr = null;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			//Relleno el hash con los datos clave a borrar
			ocultos = miForm.getDatosTablaOcultos(0);
			UtilidadesHash.set(registro,ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION,(String)ocultos.get(0));
			UtilidadesHash.set(registro,ScsSaltoCompensacionGrupoBean.C_IDTURNO,(String)ocultos.get(1));
			UtilidadesHash.set(registro,ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO,(String)ocultos.get(4));
			
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
		ScsSaltoCompensacionGrupoAdm admSaltosYCompensacionesGrupo = new ScsSaltoCompensacionGrupoAdm(this.getUserBean(request));
				
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
				UtilidadesHash.set(registros,"FECHADESDE",miForm.getFechaDesde());
				UtilidadesHash.set(registros,"FECHAHASTA",miForm.getFechaHasta());
				if(miForm.getIdGrupoGuardia()!=null)
					UtilidadesHash.set(registros,"IDGRUPOGUARDIA",miForm.getIdGrupoGuardia());	
				
				//Campo Compensado/No compensado/Todo:
				if (miForm.getCompensado().equalsIgnoreCase("si")){
					compensado="S";
					UtilidadesHash.set(registros,"COMPENSADO",compensado);
				}else if (miForm.getCompensado().equalsIgnoreCase("no")){ 
					compensado="N";
					UtilidadesHash.set(registros,"COMPENSADO",compensado);
				}
				
				//Campo salto/compensacion/Todo:
				if (miForm.getSalto().equalsIgnoreCase("S")||miForm.getSalto().equalsIgnoreCase("C")){
					//SELECT
					UtilidadesHash.set(registros,"SALTO",miForm.getSalto());
					salida = admSaltosYCompensaciones.selectGenerico(admSaltosYCompensaciones.buscar(registros));
				
				}else if (miForm.getSalto().equalsIgnoreCase("SG")||miForm.getSalto().equalsIgnoreCase("CG")){
					if(miForm.getSalto().equalsIgnoreCase("SG")){
						UtilidadesHash.set(registros,"SALTO","S");
					}else if (miForm.getSalto().equalsIgnoreCase("CG")){
						UtilidadesHash.set(registros,"SALTO","C");
					}
									
					salida = admSaltosYCompensacionesGrupo.selectMantenimientoSYC(admSaltosYCompensacionesGrupo.buscar(registros));
				
				}else{
					salida = admSaltosYCompensaciones.selectGenerico(admSaltosYCompensaciones.buscar(registros));
					Vector salida2 = admSaltosYCompensacionesGrupo.selectMantenimientoSYC(admSaltosYCompensacionesGrupo.buscar(registros));
					for(int i = 0; i<salida2.size(); i++){
						this.insertarPosicionVector(salida,(Hashtable)salida2.get(i));
					}
				}
			
				request.setAttribute("resultado",salida);			
				request.setAttribute("modo",miForm.getModo());
			}
			catch  (Exception e){
				throwExcp("messages.select.error",e,null);	
			}			
			return "listado";
	}
	
	public void insertarPosicionVector(Vector v, Hashtable registro) {
		try {
			Date fecha = new Date((String) registro.get("FECHA"));
			int i = 0;

			for (i = 0; i < v.size(); i++) {
				Hashtable ele = (Hashtable) v.get(i);
				Date fechaVector = new Date((String) ele.get("FECHA"));
				if (fecha.after(fechaVector)) {
					break;
				}
			}

			v.add(i, registro);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		
		try {
			miForm.setSalto("S");
			miForm.setCompensado("N");
		}
		catch  (Exception e){
			throwExcp("messages.select.error",e,null);	
		}	
		return "inicio";
	}
	
	@SuppressWarnings("unchecked")
	protected void getAjaxLetradosInscritos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, Exception {
		
		SaltosYCompensacionesForm miForm = (SaltosYCompensacionesForm) formulario;
		List listaParametros = new ArrayList();
		
		if(!request.getParameter("idGrupoGuardia").equals("")){
			//Recogemos el parametro enviado por ajax
			String ids = request.getParameter("idGrupoGuardia");
			String[] reg = ids.split(",");
			String idInstitucion = reg[0];
			String idTurno = reg[1];
			String idGuardia = reg[2];
			String idGrupoGuardia = reg[3];		
			
			ClsLogging.writeFileLog("SALTOS Y COMPENSACION idTurno:"+idTurno+"/", 10);
			ClsLogging.writeFileLog("SALTOS Y COMPENSACION idGuardia:"+idGuardia+"/", 10);
			ClsLogging.writeFileLog("SALTOS Y COMPENSACION idGrupoGuardia:"+idGrupoGuardia+"/", 10);		
	
			
			//Sacamos las guardias si hay algo selccionado en el turno
			ScsSaltoCompensacionGrupoAdm admSaltosYCompensacionesGrupo = new ScsSaltoCompensacionGrupoAdm(this.getUserBean(request));
			String letradosInscritos = admSaltosYCompensacionesGrupo.getInfoLetradosGrupoGuardia(idInstitucion, idTurno, idGuardia, idGrupoGuardia);
			miForm.setLetrado(letradosInscritos);
			
			listaParametros.add(letradosInscritos);
		}
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response);
	}
	
	
}