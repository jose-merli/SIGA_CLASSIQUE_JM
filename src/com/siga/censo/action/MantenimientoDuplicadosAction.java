// VERSIONES:
// raul.ggonzalez 14-12-2004 Creacion
// miguel.villegas 11-01-2005 Incorpora "borrar"
// juan.grau 18-04-2005 Incorpora 'buscarPersona' y 'enviarPersona'

/**
 * @version 30/01/2006 (david.sanchezp): nuevo valor de pestanha.
 */
package com.siga.censo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesString;
//import com.siga.Utilidades.paginadores.Paginador;
//import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.DuplicadosHelper;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class MantenimientoDuplicadosAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={CenClienteBean.C_IDINSTITUCION,CenClienteBean.C_IDPERSONA};
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 

			miForm = (MasterForm) formulario;
			if (miForm != null) {
				//MantenimientoDuplicadosForm form = (MantenimientoDuplicadosForm)miForm;
				String accion = miForm.getModo();
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("inicio")){
					mapDestino = abrir(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("buscar")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					request.getSession().removeAttribute("DATOSPAGINADOR");
					mapDestino = buscar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("buscarPor")){
					mapDestino = buscar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("gestionar")){
					mapDestino = gestionar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("aceptar")){
					mapDestino = combinar(mapping, miForm, request, response);
				}else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}

	
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			// TODO: Cuando se vaya a algun sitio hay que recuperar todos los datos de la busqueda
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}
	
	
	
	/**
	 * Metodo que implementa el modo buscar para realizar la busqueda de duplicados
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();
			Vector resultado = null;

			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;

			DuplicadosHelper helper = new DuplicadosHelper();
			HashMap databackup = new HashMap();
			
			resultado = helper.getDuplicados(miFormulario);
			request.setAttribute("resultado", resultado);

			destino="resultado";

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}
	
	/**
	 * Metodo que implementa el modo buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String gestionar (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		Vector similares = new Vector();
		Vector colegiaciones = new Vector();
		CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
		CenClienteAdm admCliente = new CenClienteAdm(this.getUserBean(request));
		CenColegiadoAdm admColeg = new CenColegiadoAdm(this.getUserBean(request));
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request)); 
		// Vamos a leer los datos de la bbdd para cargarlos en la ventana de consulta de colegiaciones 
		try {
			Hashtable datos = new Hashtable();
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("registrosSeleccionados");
			// Los seleccionados deberian ser 2, separados por comas
			if (seleccionados != null && !seleccionados.equalsIgnoreCase("")) {
				String[] registros = UtilidadesString.split(seleccionados, ",");
				for (int i=0; i<registros.length;i++){
					
					Hashtable hashPersona = new Hashtable(); // Aqui meteremos todos los datos de la persona
					
					// Las claves de cada registro estan separadas por ||
					String[] claves = UtilidadesString.split(registros[i], "||");
					String idInstitucion="";
					String idPersona="";
					Vector estadosPersona = new Vector();
					Vector colegiacionesPersona = new Vector();
					Row estado = null;
					if(claves.length>1){
						colegiaciones = new Vector();
						idInstitucion = claves[0];
						idPersona = claves[1]; 
						// Si la institucion es nula significa que estamos mirando una persona y necesitamos todas las colegiaciones
						if(idInstitucion==null || idInstitucion.equalsIgnoreCase("")|| idInstitucion.equalsIgnoreCase("null")){
							colegiaciones=admColeg.getColegiaciones(idPersona);
						}else{
							colegiaciones.add(idInstitucion);
						}
						String stColegiaciones = "";
						// Buscamos los datos de cada colegiacion
						for (int c=0; c<colegiaciones.size();c++){
							Hashtable hashColegiacion = new Hashtable(); // Aqui meteremos todos los datos de la colegiacion de la persona
							String idInstitucionCol=colegiaciones.get(c).toString();
							stColegiaciones+=idInstitucionCol+"_";
							CenColegiadoBean colegiacion = admColeg.getDatosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucionCol));
							if(colegiacion!=null && colegiacion.getFechaIncorporacion()!=null && !colegiacion.getFechaIncorporacion().equalsIgnoreCase("")){
								colegiacion.setFechaIncorporacion(UtilidadesString.formatoFecha(colegiacion.getFechaIncorporacion(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
								hashColegiacion.put("datosColegiacion", colegiacion);
							}
							hashColegiacion.put("institucionColegiacion", admInstitucion.getAbreviaturaInstitucion(idInstitucionCol));
							hashColegiacion.put("fechaProduccion", admInstitucion.getFechaEnProduccion (idInstitucionCol));
							
							Vector estadosColegio = admColeg.getEstadosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucionCol), "1");
							if(estadosColegio!=null && estadosColegio.size()>0){
								estado = (Row)estadosColegio.get(0);
								estado.getRow().put("FECHAESTADO", UtilidadesString.formatoFecha(estado.getRow().get("FECHAESTADO").toString(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
								hashColegiacion.put("estadoColegiacion", estado);
							}
							
							colegiacionesPersona.add(hashColegiacion);
						}
						hashPersona.put("datosColegiales", colegiacionesPersona);
						hashPersona.put("colegiaciones", stColegiaciones);
						
						CenPersonaBean beanP = admPersona.getPersonaPorId(idPersona);
						if(beanP.getFechaNacimiento()!=null && !beanP.getFechaNacimiento().equalsIgnoreCase("")){
							beanP.setFechaNacimiento(UtilidadesString.formatoFecha(beanP.getFechaNacimiento(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						}
						beanP.setFechaMod(UtilidadesString.formatoFecha(beanP.getFechaMod(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						hashPersona.put("datosPersonales", beanP);
						
						Vector vDirecciones = admCliente.getDirecciones(Long.valueOf(idPersona), Integer.valueOf(2000), false);
						for (int j=0; j<vDirecciones.size();j++){
							Hashtable dir = (Hashtable)vDirecciones.get(j);
							dir.put("FECHAMODIFICACION", UtilidadesString.formatoFecha(dir.get("FECHAMODIFICACION").toString(),ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						}
						hashPersona.put("datosDirecciones", vDirecciones);
						
						
					}else{
						idPersona = claves[0];
					}
					
					datos.put("persona"+i, hashPersona);
					datos.put("numeroPersonas", i+1);
				}
				
			}
			request.setAttribute("datos", datos);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "gestionar";
	}
	
	/**
	 * Combina a 2 personas en una unica teniendo en cuenta las preferencias del usuario
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 */
	protected String combinar(ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException{
		
		UsrBean user = this.getUserBean(request);
		MantenimientoDuplicadosForm miForm = (MantenimientoDuplicadosForm)formulario;
		CenDireccionesAdm admDireccion     = new CenDireccionesAdm(user);
		CenDireccionesBean beanDireccion   = new CenDireccionesBean();
		CenHistoricoBean beanHistorico     = new CenHistoricoBean();
		CenColegiadoAdm admColeg  = new CenColegiadoAdm(user);
		CenPersonaAdm admPersona  = new CenPersonaAdm(user);
		CenClienteAdm admCliente  = new CenClienteAdm(user);
		CenInstitucionAdm admInst = new CenInstitucionAdm(user);
		CenHistoricoAdm	  admHistorico = new CenHistoricoAdm(user);
		
		String personaDestino = miForm.getIdPersonaDestino();
		String personaOrigen = miForm.getIdPersonaOrigen();
		String institucion = miForm.getIdInstOrigen();
		String msgError = "";
		String msgSalida = "Se han copiado correctamente los datos de la persona";
		Hashtable hashDireccionOriginal, hashDireccionDestino ;
		int colegiacionesCopiadas = 0;
		
		UserTransaction tx = null;
		
		try {
			
			// jbd // Insertamos una copia de las direcciones que se quieran añadir, la original sera borrada en el proceso PL
			// Recuperamos las direcciones
			String[] direcciones = miForm.getListaDirecciones().split(","); 
			String[] direccion;
			String idInstitucion="2000", idPersona="", idDireccion="";
			beanDireccion.setIdInstitucion(Integer.valueOf(idInstitucion));
			// Cogemos la direccion que vamos a copiar 
			beanDireccion.setIdPersona(Long.valueOf(personaDestino));
			
			tx = this.getUserBean(request).getTransactionPesada();
			tx.begin();	
			
			if(direcciones.length>0){
				for (int i = 0; i < direcciones.length; i++) {
					direccion=direcciones[i].split("&&");
					if(direccion.length>1){
						idPersona = direccion[1];
						idDireccion = direccion[2];
						// Recuperamos la direccion Original que vamos a copiar
						hashDireccionOriginal = admDireccion.getEntradaDireccion(idPersona, idInstitucion, idDireccion);
						// La clonamos
						hashDireccionDestino = (Hashtable) hashDireccionOriginal.clone();
						// Le cambiamos las claves para insertar la nueva direccion
						hashDireccionDestino.put(CenDireccionesBean.C_IDINSTITUCION, idInstitucion);
						hashDireccionDestino.put(CenDireccionesBean.C_IDPERSONA, personaDestino);
						hashDireccionDestino.put(CenDireccionesBean.C_IDDIRECCION, admDireccion.getNuevoID(beanDireccion));
						// Insertamos la direccion destino
						admDireccion.insert(hashDireccionDestino);
					}
				}
			}
			
			Vector instCliente = admCliente.getClientes(personaOrigen);
			String stInstitucion = "";
			String nombreInstitucion = "";
			int intInstitucion;
			// muevePersona
			
			for(int i=0;i<instCliente.size();i++){	
			// Recorremos la lista de clientes del colegiado origen
				stInstitucion = instCliente.get(i).toString();
				intInstitucion = Integer.parseInt(stInstitucion);
				nombreInstitucion = admInst.getAbreviaturaInstitucion(stInstitucion);
				if(admCliente.existeCliente(Long.parseLong(personaDestino),intInstitucion )!=null){
				// Si la persona destino ya es cliente de la institucion ...
					if(intInstitucion<=3000&&intInstitucion>2000){
					// Si la institucion es un colegio damos error porque no se puede hacer
						tx.rollback();
						throw new SIGAException("No se puede realizar la fusión porque ya existe como colegiado o no colegiado en "+nombreInstitucion);
					}
				}else{
					msgError= "Error al copiar los datos del cliente de " + nombreInstitucion;
					EjecucionPLs.ejecutarPL_copiaCliente(personaDestino, personaOrigen, stInstitucion);
				}
			}
			
			msgError= "Error al mover los datos de la persona";
			EjecucionPLs.ejecutarPL_mueveDatosPersona(personaDestino, personaOrigen);
			
			for(int i=0;i<instCliente.size();i++){	
			// Recorremos la lista de clientes del colegiado origen
				stInstitucion = instCliente.get(i).toString();
				intInstitucion = Integer.parseInt(stInstitucion);
				nombreInstitucion = admInst.getAbreviaturaInstitucion(stInstitucion);
				if(admCliente.existeCliente(Long.parseLong(personaDestino),intInstitucion )==null){
					msgError= "Error al borrar los datos del cliente de " + nombreInstitucion;
					EjecucionPLs.ejecutarPL_borraCliente(personaOrigen, stInstitucion);
				}
			}
			msgError= "Error al borrar los datos de la persona";
			EjecucionPLs.ejecutarPL_borraPersona(personaOrigen); // borraPersona
			
			// Modificamos la persona para que modifique la fechamodificacion
			admPersona.update(admPersona.getPersonaPorId(personaDestino));
			beanHistorico.setIdPersona(Long.parseLong(personaDestino));
			beanHistorico.setIdInstitucion(Integer.parseInt(user.getLocation()));
			beanHistorico.setIdHistorico(admHistorico.getNuevoID(beanHistorico));
			beanHistorico.setFechaEfectiva(GstDate.getHoyJsp());
			beanHistorico.setFechaEntrada(GstDate.getHoyJsp());
			beanHistorico.setMotivo("Se ha realizado un mantenimiento de duplicados sobre esta persona.");
			beanHistorico.setIdTipoCambio(10); // Datos Generales
			
			admHistorico.insertarRegistroHistorico(beanHistorico, user);
					
			tx.commit();
		} catch (Exception e) {
			throwExcp(msgError,new String[] {"modulo.censo"},e,tx);
		}
		return exitoModal(msgSalida, request);
	}

}
