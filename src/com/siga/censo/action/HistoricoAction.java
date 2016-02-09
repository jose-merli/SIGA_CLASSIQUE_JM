/*
 * VERSIONES:
 * 
 * miguel.villegas - 21-12-2004 - Creacion
 *	
 */

/**
 * Clase action para el mantenimiento del historico.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento del historico  
 */

package com.siga.censo.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenTipoCambioAdm;
import com.siga.censo.form.HistoricoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class HistoricoAction extends MasterAction {

	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		String numero = "";
		String nombre = "";
		String estadoColegial="";
		Long idPersona=null;
		String idInstitucionPersona="";

		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String accion = (String)request.getParameter("accion");
			
			//Estamos volviendo del botón volver de editar/consulta/nuevo registro de auditoría
			if(accion==null){
				accion=(String)request.getSession().getAttribute("ACCION");
				idPersona =new Long(request.getSession().getAttribute("IDPERSONA").toString());
				idInstitucionPersona=request.getSession().getAttribute("IDINSTITUCIONPERSONA").toString();
				
			}else{
				try{
					// Obtengo el identificador de persona, la accion y el identificador de institucion del cliente
					idPersona = new Long(request.getParameter("idPersona").toString());
					idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();
				}catch (NumberFormatException e){
					//La persona no está creada aún
				}
				
			}
			
			// Vemos si venimos de nueva sociedad o nuevo no colegiado de tipo personal:
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
				 (idPersona.equals("") && idInstitucionPersona.equals("") )) {
				request.setAttribute("modoVolver",accion);
				return "clienteNoExiste";
			}
			
			String idInstitucion=user.getLocation();
			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			

			// Obtengo el numero de colegiado en caso de que este colegiado
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
//			if(!clienteAdm.esColegiado(idPersona,idInst).isEmpty()){
//				numero = clienteAdm.obtenerNumero(idPersona,idInst);				
//			}	
			// DCG Nuevo ////////////////////
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona, new Integer(idInstitucionPersona));
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucionPersona));
			//////////////////////
			
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("ACCION", accion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
			request.setAttribute("ESTADOCOLEGIAL", estadoColegial);
				
			// idPersona, accion e idInstitucionPersona los guardo en session porque me interesa 
			// acceder a ellos en varios lugares
			request.getSession().setAttribute("IDPERSONA", idPersona);
			request.getSession().setAttribute("ACCION", accion);		
			request.getSession().setAttribute("IDINSTITUCIONPERSONA", idInstitucionPersona);
			// Para dar "abastecimiento" a nuevas necesidades
			request.getSession().setAttribute("NOMBRE", nombre);
			request.getSession().setAttribute("NUMERO", numero);			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}				
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		String numero = "";
		String nombre = "";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
	
			// Obtengo el identificador de persona y la accion (para pruebas tambien el identificador institucion)
			Long idPersona = (Long)request.getSession().getAttribute("idPersona");
			String accion = (String)request.getSession().getAttribute("accion");
			String idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion")).toString();			
		
			// Obtengo manejadores para accesos a las BBDDs
//			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			

			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
//			if(!clienteAdm.esColegiado(idPersona,idInst).isEmpty()){
//				numero = clienteAdm.obtenerNumero(idPersona,idInst);				
//			}	
			
			// DCG Nuevo ////////////////////
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona, new Integer(idInstitucionPersona));
			numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			//////////////////////
	
			// Paso de parametros empleando request
			request.setAttribute("IDPERSONA", idPersona);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("ACCION", accion);
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return result;
		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="editar";

		try {
			Vector ocultos = new Vector();
			Vector infoEntrada = new Vector();	
			Vector infoTipoCambio = new Vector();

			HistoricoForm form = (HistoricoForm) formulario;
			CenHistoricoAdm admin=new CenHistoricoAdm(this.getUserBean(request));
			CenTipoCambioAdm adminTC=new CenTipoCambioAdm(this.getUserBean(request));
			Object remitente=(Object)"modificar";
			request.setAttribute("modelo",remitente);
		
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = form.getDatosTablaOcultos(0);	
			if(ocultos!=null)
				infoEntrada=admin.obtenerEntradaHistorico((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2));
			//Si estamos refrescando tras insertar
			else{
				Hashtable hash = (Hashtable) request.getSession().getAttribute("hashInsert");
				infoEntrada=admin.obtenerEntradaHistorico(hash.get("IDPERSONA").toString(),hash.get("IDINSTITUCION").toString(),hash.get("IDHISTORICO").toString());
				request.getSession().removeAttribute("hashInsert");
			}
			
			// Obtengo el id de TipoCambio
			Row fila = (Row)infoEntrada.firstElement();
			String tCambio=fila.getString(CenHistoricoBean.C_IDTIPOCAMBIO);
			
			// Obtengo la informacion sobre el tipo de cambio a mostrar
			infoTipoCambio = adminTC.obtenerDescripcion(tCambio);	
			
			// Paso valores originales del registro al session para tratar siempre copn los mismos valores
			// y no los de posibles modificaciones
			request.getSession().setAttribute("DATABACKUP", infoEntrada);
			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", infoEntrada);
			request.setAttribute("container_desc", infoTipoCambio);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}			
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="ver";

		try{
			HistoricoForm form = (HistoricoForm) formulario;
			CenHistoricoAdm admin=new CenHistoricoAdm(this.getUserBean(request));
			CenTipoCambioAdm adminTC=new CenTipoCambioAdm(this.getUserBean(request));		
			Object remitente=(Object)"consulta";
			request.setAttribute("modelo",remitente);
		
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			Vector ocultos = form.getDatosTablaOcultos(0);		
					
			// Obtengo la informacion sobre la entrada del historico
			Vector infoEntrada=admin.obtenerEntradaHistorico((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2));
			// Obtengo el id de TipoCambio
			Row fila = (Row)infoEntrada.firstElement();
			String tCambio=fila.getString(CenHistoricoBean.C_IDTIPOCAMBIO);
			
			// Obtengo la informacion sobre el tipo de cambio a mostrar
			Vector infoTipoCambio = adminTC.obtenerDescripcion(tCambio);			
			
			// Paso valores originales del registro al session para tratar siempre copn los mismos valores
			// y no los de posibles modificaciones
			request.getSession().setAttribute("DATABACKUP", infoEntrada);
			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", infoEntrada);			
			request.setAttribute("container_desc", infoTipoCambio);			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="nuevo";
		try{						
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);									 
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}			
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		boolean acceso = true;
		UserTransaction tx = null;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenHistoricoAdm admin=new CenHistoricoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			// Cargo la tabla hash con los valores del formulario para insertar en CEN_HISTORICO
			Hashtable hash = formulario.getDatos();
			// Obtengo el identificador del cliente y de la institucion
			Long idPersona = (Long)request.getSession().getAttribute("IDPERSONA");			
			String idInstitucion = (String) request.getSession().getAttribute("IDINSTITUCIONPERSONA");
			// Anhado valores que faltan
			hash.put("IDPERSONA",idPersona.toString());
			hash.put("IDINSTITUCION",idInstitucion);			
			
			// Creo el bean a pasar a la funcion publica
			CenHistoricoBean bean = new CenHistoricoBean();			
			bean.setIdPersona(idPersona);
			Integer inst=new Integer (idInstitucion);
			bean.setIdInstitucion(inst);
			bean.setFechaEntrada((String)hash.get("FECHAENTRADA"));
			bean.setFechaEfectiva((String)hash.get("FECHAEFECTIVA"));
			bean.setMotivo((String)hash.get("MOTIVO"));
			bean.setObservaciones((String)hash.get("OBSERVACIONES"));
			Integer tipoCambio=new Integer ((String)hash.get("IDTIPOCAMBIO"));			
			bean.setIdTipoCambio(tipoCambio);
			bean.setIdHistorico(admin.getNuevoID(hash));
			hash.put("IDHISTORICO",bean.getIdHistorico());
			// Comienzo la transaccion
			tx.begin();
			// inserto el bean (registro) en la BBDDs
			acceso=admin.insertarRegistroHistorico(bean,usr);			
			if (acceso){
				tx.commit();
				request.getSession().setAttribute("hashInsert",hash);
				request.setAttribute("mensaje","messages.inserted.success");
				result= "exito";		
			}			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		UserTransaction tx=null;
		
		try {		
			Hashtable hashOriginal = new Hashtable();
			Enumeration filaOriginal; 		
			Row registroOriginal=null;
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			CenHistoricoAdm admin=new CenHistoricoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			// Obtengo las diferentes formas de pago "modificadas"
			// Cargo la tabla hash con los valores del formulario para insertar en CEN_HISTORICO
			Hashtable hash = formulario.getDatos();			

			// Obtengo el identificador del cliente y de la institucion
			Long idPersona = (Long)request.getSession().getAttribute("IDPERSONA");			
			String idInstitucion = (String) request.getSession().getAttribute("IDINSTITUCIONPERSONA");
			// Anhado valores que faltan
			hash.put("IDPERSONA",idPersona.toString());
			hash.put("IDINSTITUCION",idInstitucion);			
			// Obtengo el IDHISTORICO
			UtilidadesHash.set(hash, CenHistoricoBean.C_IDHISTORICO, admin.getNuevoID(hash));
			
			// Adecuo formatos de las fechas
			admin.prepararFormatosFechas(hash);									
			
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			filaOriginal=((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			while(filaOriginal.hasMoreElements()){
              	registroOriginal = (Row) filaOriginal.nextElement(); 
				hashOriginal.put("IDPERSONA",registroOriginal.getString(CenHistoricoBean.C_IDPERSONA));				              									
				hashOriginal.put("IDINSTITUCION",registroOriginal.getString(CenHistoricoBean.C_IDINSTITUCION));
				hashOriginal.put("IDHISTORICO",registroOriginal.getString(CenHistoricoBean.C_IDHISTORICO));				              									
				hashOriginal.put("FECHAENTRADA",registroOriginal.getString(CenHistoricoBean.C_FECHAENTRADA));
				hashOriginal.put("FECHAEFECTIVA",registroOriginal.getString(CenHistoricoBean.C_FECHAEFECTIVA));
				hashOriginal.put("MOTIVO",registroOriginal.getString(CenHistoricoBean.C_MOTIVO));								              									
				hashOriginal.put("OBSERVACIONES",registroOriginal.getString(CenHistoricoBean.C_OBSERVACIONES));
			}

			// Doy el valor correcto a IDHISTORICO
			hash.put("IDHISTORICO",registroOriginal.getString(CenHistoricoBean.C_IDHISTORICO));
						
			// Comienzo la transaccion
			tx.begin();	
							
			// Actualizo CEN_HISTORICO
			if (admin.update(hash,hashOriginal)){
				tx.commit();
				request.setAttribute("mensaje","messages.updated.success");
				request.getSession().setAttribute("hashInsert",hash);
				result= "exito";		
			}
			else{
				throw new SIGAException (admin.getError());
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx); 
		}			
		return (result);		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="borrar";
		try{
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";

		try{
			Vector vect=new Vector();

			// Obtengo idPersona e idInstitucion
			String idPersona = ((Long)request.getSession().getAttribute("IDPERSONA")).toString();		
			String idInstitucion = (String) request.getSession().getAttribute("IDINSTITUCIONPERSONA");
		
			// Manejadores para el formulario y el acceso a las BBDDs
			HistoricoForm form = (HistoricoForm) formulario;
			CenHistoricoAdm admin=new CenHistoricoAdm(this.getUserBean(request));
			Map<String, String> clavesJsonMap = new HashMap<String, String>();
			clavesJsonMap.put("nombreFormulario", "HistoricoForm");
			clavesJsonMap.put("fechaInicio", form.getFechaInicio());
			clavesJsonMap.put("fechaFin", form.getFechaFin());
			clavesJsonMap.put("motivo", form.getMotivo());
			clavesJsonMap.put("idTipoCambio", form.getCmbCambioHistorico());
			String jsonVolver = UtilidadesString.createJsonString(clavesJsonMap);
			form.setJsonVolver(jsonVolver);
			// Obtengo las entradas del historico para la busqueda indicada en el formulario
			vect=admin.getHistorico(idPersona,idInstitucion,form.getCmbCambioHistorico(),form.getFechaInicio(),form.getFechaFin(),form.getMotivo());

			// Paso la busqueda como parametro en el request 
			request.setAttribute("container", vect);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		}		
		return (result);
				
	}

	/** 
	 *  Funcion que prepara el formato fecha
	 * @param  fecha - Fecha en formato original
	 * @return  String - Formato actualizado fecha   
	 */	
	protected String prepararFecha(String fecha){
		
		String sInicial = fecha;
		String sFinal="";		
		
		if (sInicial == null) 
			return sFinal;
		
		for (int i=0;i<sInicial.length();i++) {
			if (sInicial.charAt(i) == '-') sFinal += '/';
			else sFinal += sInicial.charAt(i);
		}
		sFinal += " 00:00:00";		
		return sFinal;
	}
}
