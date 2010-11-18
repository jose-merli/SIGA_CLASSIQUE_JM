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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
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

			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;

			ArrayList clavesRegSeleccinados = new ArrayList();
			String seleccionados = request.getParameter("Seleccion");
			if (seleccionados != null && !seleccionados.equalsIgnoreCase("")) {
				ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, new ArrayList());
				if (alRegistros != null) {
					clavesRegSeleccinados = alRegistros;
				}
			}
			miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);

			DuplicadosHelper helper = new DuplicadosHelper();
			HashMap databackup = new HashMap();
			if(request.getSession().getAttribute("DATOSPAGINADOR")!=null)
				databackup=(HashMap)request.getSession().getAttribute("DATOSPAGINADOR");
			if (databackup!=null && databackup.get("paginador")!=null){ 
				
				
				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();
				if (paginador!=null){
					String pagina = (String)request.getParameter("pagina");
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
				miFormulario.setDatosPaginador(databackup);	

			}else{	

				databackup=new HashMap();
				 			
				Paginador resultado = null;
				Vector datos = null;
				
				resultado = helper.getDuplicados(miFormulario);
				databackup.put("paginador",resultado);
				
				if (resultado!=null){ 
					miFormulario.setRegistrosSeleccionados(new ArrayList());
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
				}else{
					miFormulario.setRegistrosSeleccionados(new ArrayList());
				} 
				miFormulario.setDatosPaginador(databackup);
				miFormulario.setRegistrosSeleccionados(new ArrayList());
				request.setAttribute("CenResultadoBusquedaDuplicados",resultado);
				request.getSession().setAttribute("DATOSPAGINADOR", databackup);
			}

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
		CenPersonaAdm admPersona = new CenPersonaAdm(this.getUserBean(request));
		CenClienteAdm admCliente = new CenClienteAdm(this.getUserBean(request));
		CenColegiadoAdm admColeg = new CenColegiadoAdm(this.getUserBean(request));
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(this.getUserBean(request)); 
		
		try {
			Hashtable datos = new Hashtable();
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("registrosSeleccionados");
			if (seleccionados != null && !seleccionados.equalsIgnoreCase("")) {
				String[] registros = UtilidadesString.split(seleccionados, ",");
				for (int i=0; i<registros.length;i++){
					String[] claves = UtilidadesString.split(registros[i], "||");
					String idInstitucion="";
					String idPersona="";
					if(claves.length>1){
						idInstitucion = claves[0];
						idPersona = claves[1]; 
						
						CenColegiadoBean colegiacion = admColeg.getDatosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucion));
						datos.put("colegiacion"+i, colegiacion);
						//admInstitucion.getNombreInstitucion(idInstitucion);
						datos.put("nombreInstitucion"+i, admInstitucion.getAbreviaturaInstitucion(idInstitucion));
						
						Vector estados = admColeg.getEstadosColegiales(Long.valueOf(idPersona), Integer.valueOf(idInstitucion), "1");
						if(estados!=null && estados.size()>0){
							Row estado = (Row)estados.get(0);
							datos.put("estado"+i, estado);
						}
						
					}else{
						idPersona = claves[0];
					}
					CenPersonaBean beanP = admPersona.getPersonaPorId(idPersona); 
					datos.put("persona"+i, beanP);
					
					Vector vDirecciones = admCliente.getDirecciones(Long.valueOf(idPersona), Integer.valueOf(2000), false);
					datos.put("direcciones"+i, vDirecciones);
					
					Vector vColegiaciones = admColeg.getColegiaciones(idPersona);
					datos.put("colegiaciones"+i, vColegiaciones);

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
	 */
	protected String combinar(ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException{
		
		MantenimientoDuplicadosForm miForm = (MantenimientoDuplicadosForm)formulario;
		CenDireccionesAdm admDireccion = new CenDireccionesAdm(this.getUserBean(request));
		CenDireccionesBean beanDireccion = new CenDireccionesBean();	
		
		String personaDestino = miForm.getIdPersonaDestino();
		String personaOrigen = miForm.getIdPersonaOrigen();
		String institucion = miForm.getIdInstOrigen();
		Hashtable hashDireccionOriginal, hashDireccionDestino ;
		
		try {
			
			// jbd // Insertamos una copia de las direcciones que se quieran añadir, la original sera borrada en el proceso PL
			// Recuperamos las direcciones
			String[] direcciones = miForm.getListaDirecciones().split(","); 
			String[] direccion;
			String idInstitucion="2000", idPersona="", idDireccion="";
			beanDireccion.setIdInstitucion(Integer.valueOf(institucion));
			// Cogemos la direccion que vamos a copiar 
			beanDireccion.setIdPersona(Long.valueOf(personaDestino)); 
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
		
			// Realizamos el proceso de copia
			EjecucionPLs. ejecutarPL_copiaColegiado(miForm.getIdPersonaDestino(), miForm.getIdPersonaOrigen(), miForm.getIdInstOrigen());
		 
		} catch (ClsExceptions e) {
			e.printStackTrace();
			return exitoModal("Exito en la fusion", request);
		}
		return exitoModal("Exito en la fusion", request);
	}

}
