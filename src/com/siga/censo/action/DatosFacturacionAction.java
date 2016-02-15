//VERSIONES
//raul.ggonzalez 07-02-2005 Creacion
//

package com.siga.censo.action;

import java.util.HashMap;
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
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesProductosServicios;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenSolModiFacturacionServicioAdm;
import com.siga.beans.CenSolModiFacturacionServicioBean;
import com.siga.beans.PysCompraAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosSolicitadosAdm;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.beans.PysServicioAnticipoAdm;
import com.siga.beans.PysServiciosSolicitadosAdm;
import com.siga.beans.PysServiciosSolicitadosBean;
import com.siga.beans.PysSuscripcionAdm;
import com.siga.beans.PysSuscripcionBean;
import com.siga.censo.form.DatosFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


/**
* Clase action del caso de uso DATOS FACTURACION
* @author AtosOrigin 07-02-2005
*/
public class DatosFacturacionAction extends MasterAction {

	// Atributos
	/**   */
	
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
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
				
				String accion = miForm.getModo();

		  		// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}
				else if (accion.equalsIgnoreCase("abrirSolicitud")){
					mapDestino = abrirSolicitud(mapping, miForm, request, response);
					break;
				} else if (accion.equalsIgnoreCase("abrirProductos")){
					mapDestino = abrirProductos(mapping, miForm, request, response);
					break;
				} else if (accion.equalsIgnoreCase("abrirServicios")){
					mapDestino = abrirServicios(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("abrirPaginaProducto")){
					mapDestino = abrirProductosPaginados(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("abrirProductosPaginados")){
					borrarPaginador(request, paginadorPenstania);
					mapDestino = abrirProductosPaginados(mapping, miForm, request, response);
					break;
				} 
				else if (accion.equalsIgnoreCase("abrirPaginaServicio")){
					mapDestino = abrirServiciosPaginados(mapping, miForm, request, response);
					break;
				}
				else if (accion.equalsIgnoreCase("abrirServiciosPaginados")){
					borrarPaginador(request, paginadorPenstania);
					mapDestino = abrirServiciosPaginados(mapping, miForm, request, response);
					break;
				} 
				else if (accion.equalsIgnoreCase("modificarServicio")){
					mapDestino = modificarServicio(mapping, miForm, request, response);
					break;
				} else if (accion.equalsIgnoreCase("modificarProducto")){
					mapDestino = modificarProducto(mapping, miForm, request, response);
					break;
				} else if (accion.equalsIgnoreCase("insertarSolicitud")){
					mapDestino = insertarSolicitud(mapping, miForm, request, response);
					break;
				} else if (accion.equalsIgnoreCase("editarCambiarFecha")){
					mapDestino = editarCambiarFecha(mapping, miForm, request, response);
					break;
				} else if (accion.equalsIgnoreCase("grabarCambiarFecha")){
					borrarPaginador(request, paginadorPenstania);
					mapDestino = grabarCambiarFecha(mapping, miForm, request, response);
					break;
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
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
			String accion = (String)request.getParameter("accion");
			
			// Vemos si venimos de nueva sociedad o nuevo no colegiado de tipo personal:
			if ( accion!=null && accion.equals("nuevo") || accion.equalsIgnoreCase("nuevaSociedad") || 
				 (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals("") )) {
				request.setAttribute("modoVolver",accion);
				return "clienteNoExiste";
			}
			String esLetrado=request.getParameter("tipo");
			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			miform.reset(mapping,request);

			// cargo los valores recibidos por paramtros en el FORM
			miform.setIdInstitucion(request.getParameter("idInstitucion"));			
			miform.setIdPersona(request.getParameter("idPersona"));			
			miform.setAccion(request.getParameter("accion"));			
			
			Integer tipoAcceso;
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String tipoCliente = clienteAdm.getTipoCliente(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			} else {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_FACTURACION_NOCOLEGIADO);
			}
			
			//String modo = "editar";
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",miform.getAccion());
			datosCliente.put("idPersona",miform.getIdPersona());
			datosCliente.put("idInstitucion",miform.getIdInstitucion());
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			UtilidadesHash.set(datosCliente,"tipoCliente",esLetrado);

			request.setAttribute("datosClienteFacturacion", datosCliente);		

	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }

		return "administracionFacturacion";

	}

	/**
	 * Metodo que implementa el modo editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";
		try {

			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// obtengo el identificador de cuenta
			Vector fila = miform.getDatosTablaOcultos(0);
			
			String tipo = miform.getPos();
			//String tipo = request.getParameter("tipoSolicitud");
			if (tipo!=null && tipo.equals("P")) {
				miform.setIdInstitucion((String)fila.get(0));			
				miform.setIdPersona((String)fila.get(4));
			} else {
				miform.setIdInstitucion((String)fila.get(1));			
				miform.setIdPersona((String)fila.get(6));
			}
			// para saber si es colegiado			
			CenColegiadoAdm admCol = new CenColegiadoAdm(user);
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			CenClienteAdm clienteAdm = new CenClienteAdm(user);
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.colegiado";
			} else if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.noColegiado";
			} else if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			}
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);

			// nombre y apellidos
			String nombreApellidos = "";
			Vector v = clienteAdm.getDatosPersonales(new Long(miform.getIdPersona()),new Integer(miform.getIdInstitucion()));
			if (v!=null && v.size()>0) {
				Hashtable registro =  (Hashtable)v.get(0);
				if (registro.get("NOMBRE")!=null) {
					nombreApellidos += (String) registro.get("NOMBRE") + "&nbsp;";
				}
				if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) { 
					nombreApellidos += (String) registro.get("APELLIDOS1") + "&nbsp;"; 
				}
				if (registro.get("APELLIDOS2")!=null) {
					nombreApellidos += (String) registro.get("APELLIDOS2") + "&nbsp;"; 
				}
			}
			request.setAttribute("CenDatosGeneralesNombreApellidos", nombreApellidos);
			
			if (tipo!=null && tipo.equals("P")) { // productos
				Hashtable hash= new Hashtable();
				hash.put(PysProductosSolicitadosBean.C_IDINSTITUCION,miform.getIdInstitucion());
				hash.put(PysProductosSolicitadosBean.C_IDPERSONA,miform.getIdPersona());
				hash.put(PysProductosSolicitadosBean.C_IDPETICION,(String)fila.get(7));
				hash.put(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO,(String)fila.get(1));
				hash.put(PysProductosSolicitadosBean.C_IDPRODUCTO,(String)fila.get(2));
				hash.put(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION,(String)fila.get(3));
				String idFormaPagoCuenta = new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA).toString();
				String idFormaPago = (String)fila.get(5);
				hash.put(PysProductosSolicitadosBean.C_IDFORMAPAGO,idFormaPago);

				//Tratamiento por si no hay cuenta seleccionada. En sesion meto "" y al JSP la primera opcion: 0.
				if (idFormaPago.equals(idFormaPagoCuenta)) {
					String idcuenta = "";
					if (fila.get(6)!=null && !((String)fila.get(6)).equals("0"))
						idcuenta = (String)fila.get(6);
					hash.put(PysProductosSolicitadosBean.C_IDCUENTA,idcuenta);
				}
				
				// Cargo una hastable con los valores originales del registro sobre el que se realizar� la modificacion						
				request.getSession().setAttribute("DATABACKUP",hash);
				
				request.setAttribute("CenDatosIdFormaPagoSel",(String)fila.get(5));
				request.setAttribute("CenDatosIdCuentaSel",(String)fila.get(6));
				request.setAttribute("CenDatosIdPeticion",(String)fila.get(7));
				
				Vector aux = new Vector();
				for (int i=0;i<4;i++) {
					aux.add((String)fila.get(i));
				}
				request.setAttribute("CenDatosFormaPagoProductos",aux);
				salida = "modificarProducto";
				
			} else { // servicios
				Hashtable hash= new Hashtable();
				hash.put(PysSuscripcionBean.C_IDINSTITUCION, miform.getIdInstitucion());
				hash.put(PysSuscripcionBean.C_IDPERSONA, miform.getIdPersona());
				hash.put(PysSuscripcionBean.C_IDPETICION, (String)fila.get(5));
				hash.put(PysSuscripcionBean.C_IDTIPOSERVICIOS, (String)fila.get(2));
				hash.put(PysSuscripcionBean.C_IDSERVICIO, (String)fila.get(3));
				hash.put(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, (String)fila.get(4));
				String idFormaPagoCuenta = String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA);
				String idFormaPago = (String)fila.get(7);
				hash.put(PysSuscripcionBean.C_IDFORMAPAGO, idFormaPago);

				//Tratamiento por si no hay cuenta seleccionada. En sesion meto "" y al JSP la primera opcion: 0.
				if (idFormaPago.equals(idFormaPagoCuenta)) {
					String idcuenta = "";
					if (fila.get(0)!=null && !((String)fila.get(0)).equals("0"))
						idcuenta = (String)fila.get(0);
					hash.put(PysSuscripcionBean.C_IDCUENTA,idcuenta);
				}

				// Cargo una hastable con los valores originales del registro sobre el que se realizar� la modificacion						
				request.getSession().setAttribute("DATABACKUP",hash);
				
				request.setAttribute("CenDatosIdFormaPagoSel",(String)fila.get(7));
				request.setAttribute("CenDatosIdCuentaSel",(String)fila.get(0));
				request.setAttribute("CenDatosIdPeticion",(String)fila.get(5));
				request.setAttribute("CenFechaEfectiva",(String)fila.get(8));

				Vector aux = new Vector();
				aux.add((String)fila.get(1));
				aux.add((String)fila.get(2));
				aux.add((String)fila.get(3));
				aux.add((String)fila.get(4));
				request.setAttribute("CenDatosFormaPagoServicios",aux);
				request.setAttribute("modo","editar");
				salida = "modificarServicio";
			}
			
	   } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }

		return salida;
	}

	/**
	 * Metodo que implementa el modo editarCambiarFecha para servicios
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editarCambiarFecha (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "editarCambiarFecha";
		try {

			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String pos=miform.getPos();

			// obtengo el identificador de cuenta
			Vector fila = miform.getDatosTablaOcultos(0);

			Long idPersona = new Long(miform.getIdPersona());
			String idInstitucion = miform.getIdInstitucion();
			
			// para saber si es colegiado
			CenColegiadoAdm admCol = new CenColegiadoAdm(user);
			CenColegiadoBean beanCol = admCol.getDatosColegiales(idPersona,new Integer(idInstitucion.trim()));
			CenClienteAdm clienteAdm = new CenClienteAdm(user);
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.colegiado";
			} else if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.noColegiado";
			} else if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			} 
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);

			// nombre y apellidos
			String nombreApellidos = "";
			Vector v = clienteAdm.getDatosPersonales(idPersona,new Integer(idInstitucion.trim()));
			if (v!=null && v.size()>0) {
				Hashtable registro =  (Hashtable)v.get(0);
				if (registro.get("NOMBRE")!=null) nombreApellidos += (String) registro.get("NOMBRE") + "&nbsp;"; 
				if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) nombreApellidos += (String) registro.get("APELLIDOS1") + "&nbsp;"; 
				if (registro.get("APELLIDOS2")!=null) nombreApellidos += (String) registro.get("APELLIDOS2") + "&nbsp;"; 
			}
			
			if(pos.equals("S")){ //Es un servicio
				request.setAttribute("CenDatosGeneralesNombreApellidos",nombreApellidos);
				request.setAttribute("CenDatosIdTipo",(String)fila.get(2));
				request.setAttribute("CenDatosId",(String)fila.get(3));
				request.setAttribute("CenDatosIdPSInstitucion",(String)fila.get(4));
				request.setAttribute("CenDatosIdPeticion",(String)fila.get(5));
				request.setAttribute("CenFechaEfectiva",(String)fila.get(8));
				request.setAttribute("POS",pos);
			}else{ //Es un producto
				request.setAttribute("CenDatosGeneralesNombreApellidos",nombreApellidos);
				request.setAttribute("CenDatosIdTipo",(String)fila.get(1));
				request.setAttribute("CenDatosId",(String)fila.get(2));
				request.setAttribute("CenDatosIdPSInstitucion",(String)fila.get(3));
				request.setAttribute("CenDatosIdPeticion",(String)fila.get(7));
				request.setAttribute("CenFechaEfectiva",(String)fila.get(8));
				request.setAttribute("POS",pos);
			}

	   } catch (Exception e) {
	       throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }

		return salida;
	}

	/**
	 * Metodo que implementa el modo grabarCambiarFecha 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String grabarCambiarFecha (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";
		UserTransaction tx = null;
		
		try {
			DatosFacturacionForm miform = (DatosFacturacionForm) formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=user.getTransaction();
			
			String pos=miform.getPos();
			
			String fecha = request.getParameter("fechaEfectiva");
			fecha = GstDate.getApplicationFormatDate("EN",fecha);
			
			tx.begin();
			
			if (pos.equals("S")) { //Es un servicio
				PysSuscripcionAdm admPysSuscripcion = new PysSuscripcionAdm(user);
				PysServiciosSolicitadosAdm admPysServiciosSolicitados = new PysServiciosSolicitadosAdm(user);

				String idTipoServicioSel = request.getParameter("idTipoServicioSel");
				String idServicioSel = request.getParameter("idServicioSel");
				String idServicioInstitucionSel = request.getParameter("idServicioInstitucionSel");
				String idPeticionSel = request.getParameter("idPeticionSel");
				
				Hashtable<String,String> hServiciosSolicitados = new Hashtable<String,String>();
				hServiciosSolicitados.put(PysSuscripcionBean.C_IDINSTITUCION, user.getLocation());
				hServiciosSolicitados.put(PysSuscripcionBean.C_IDTIPOSERVICIOS, idTipoServicioSel);
				hServiciosSolicitados.put(PysSuscripcionBean.C_IDSERVICIO, idServicioSel);
				hServiciosSolicitados.put(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, idServicioInstitucionSel);				
				hServiciosSolicitados.put(PysSuscripcionBean.C_IDPETICION, idPeticionSel); // Se incluye tambien el idPeticion porque si no le estamos cambiando la fecha efectiva a todas las personas que tengan el servicio seleccionado
				
				Vector<PysServiciosSolicitadosBean> vPysServiciosSolicitados = admPysServiciosSolicitados.select(hServiciosSolicitados);
				if (vPysServiciosSolicitados!=null && vPysServiciosSolicitados.size()>0) {
				    PysServiciosSolicitadosBean PysServiciosSolicitados = (PysServiciosSolicitadosBean) vPysServiciosSolicitados.get(0);
				    
					Vector<PysSuscripcionBean> vPysSuscripcion = admPysSuscripcion.select(hServiciosSolicitados);
					
					for (int i=0; i<vPysSuscripcion.size(); i++) {
					    PysSuscripcionBean beanPysSuscripcion = (PysSuscripcionBean) vPysSuscripcion.get(i);
					    
					    if (PysServiciosSolicitados.getAceptado().equals("A")) {
					        // Aceptado. Se mete en la fecha se suscripcion
					    	beanPysSuscripcion.setFechaSuscripcion(fecha);
					        
					    } else if (PysServiciosSolicitados.getAceptado().equals("B")) {
					    	beanPysSuscripcion.setFechaBajaFacturacion(fecha);
					    }
					    
					   if (!admPysSuscripcion.updateDirect(beanPysSuscripcion)) {
					       throw new ClsExceptions("Error al actualizar la fecha efectiva. " + admPysSuscripcion.getError());
					   }
	
					}
					
				} else {
				    throw new ClsExceptions("No existe la peticion.");
				}
				
				salida = this.exitoRefresco("messages.updated.success",request);
				
			}else{ /// Es un producto
//				 busco la suscripcion
				PysCompraAdm admCom = new PysCompraAdm(user);
				PysPeticionCompraSuscripcionAdm ppcsa=new PysPeticionCompraSuscripcionAdm(user);
				//PysProductosSolicitadosAdm admPS = new PysProductosSolicitadosAdm(user);
				Hashtable ht = new Hashtable();
				ht.put(PysCompraBean.C_IDINSTITUCION,user.getLocation());
				ht.put(PysCompraBean.C_IDPETICION,request.getParameter("idPeticion"));
				
				Vector v2 = ppcsa.select(ht);
				ht.put(PysCompraBean.C_IDTIPOPRODUCTO,request.getParameter("idTipoServicioSel"));
				ht.put(PysCompraBean.C_IDPRODUCTO,request.getParameter("idServicioSel"));
				ht.put(PysCompraBean.C_IDPRODUCTOINSTITUCION,request.getParameter("idServicioInstitucionSel"));
				/* Se incluye tambien la persona porque si no le estamos cambiando la fecha efectiva a todas las personas que tengan el producto
				 asignado*/
				ht.put(PysSuscripcionBean.C_IDPERSONA,miform.getIdPersona());
				Vector v = admCom.select(ht);
				if (v!=null && v.size()>0) {
				    PysCompraBean beanPS = (PysCompraBean) v.get(0);
				    PysPeticionCompraSuscripcionBean beanPCS = (PysPeticionCompraSuscripcionBean)v2.get(0);
//					Vector v2 = admSus.select(ht);
//					for (int i=0;i<v2.size();i++) {
//					    PysSuscripcionBean beanSus = (PysSuscripcionBean) v2.get(i);
//					    
//					    if (beanSS.getAceptado().equals("A")) {
//					        // Aceptado. Se mete en la fecha se suscripcion
//					        beanSus.setFechaSuscripcion(fecha);
//					    } else
//					    if (beanSS.getAceptado().equals("B")) {
//					        // Baja. Se mete en fecha baja solo si es mayor que la de suscripcion
//						    Date dFechaSus = new Date(beanSus.getFechaSuscripcion());
//						    Date dFechaBaj = new Date(fecha);
//						    if (dFechaSus.compareTo(dFechaBaj)>0) {
//						        // la fecha de baja es menor que la fecha de suscripcion
//						        throw new SIGAException("messages.Servicios.GestionSolicitudes.FechaEfectivaMal");
//						    }
				    	beanPS.setFecha(fecha);
				    	//beanPCS.setFecha(fecha);
//					    }
//					    
					   if (!ppcsa.updateDirect(beanPCS)) {
					       throw new ClsExceptions("Error al actualizar la fecha efectiva. "+ppcsa.getError());
					   }
					   if (!admCom.updateDirect(beanPS)) {
					       throw new ClsExceptions("Error al actualizar la fecha efectiva. "+admCom.getError());
					   }
	
				//	}
				} else {
				    throw new ClsExceptions("No existe la peticion.");
				}	
				
				salida = this.exitoModal("messages.updated.success",request);
			}
			
			tx.commit();
			
	   } catch (Exception e) {
	       throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }

    	return salida;

	}

	/**
	 * Metodo que implementa el modo insertarSolicitud (graba la insercion)
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarSolicitud (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		UserTransaction tx = null;
		String salida = null;
		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			String idTipoServicios = "";
			String idServicio = "";
			String idServiciosInstitucion = "";
			String idPeticion = "";
			idTipoServicios = request.getParameter("idTipoServicios");
			idServicio = request.getParameter("idServicio");
			idServiciosInstitucion = request.getParameter("idServiciosInstitucion");
			idPeticion = request.getParameter("idPeticion");
/* RGG 11-02-2005 Esto ya no vale porque ya no existe este campo suscripcion, ahora existe idPeticion 			
			// obtengo la suscripcion
			String idSuscripcion = null;
			PysSuscripcionAdm susAdm = new PysSuscripcionAdm(this.getUserName(request));
			Hashtable hashSus = new Hashtable();
			hashSus.put(PysSuscripcionBean.C_IDINSTITUCION,idInstitucion);
			hashSus.put(PysSuscripcionBean.C_IDTIPOSERVICIOS,idTipoServicios);
			hashSus.put(PysSuscripcionBean.C_IDSERVICIO,idServicio);
			hashSus.put(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION,idServiciosInstitucion);
			hashSus.put(PysSuscripcionBean.C_IDPETICION,idPeticion);
			Vector resSus = susAdm.select(hashSus);
			if (resSus!=null && resSus.size()>0) {
				PysSuscripcionBean susBean = (PysSuscripcionBean) resSus.get(0);
				if (susBean!=null) {
					idSuscripcion = susBean.getIdSuscripcion().toString();
				}
			}

*/						
			// Obtengo los datos del formulario
			DatosFacturacionForm miForm = (DatosFacturacionForm)formulario;
			Hashtable hash = miForm.getDatos();

			CenSolModiFacturacionServicioAdm adminFac=new CenSolModiFacturacionServicioAdm(this.getUserBean(request));
				
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	

			Long idSolicitud = adminFac.getNuevoId();
			hash.put(CenSolModiFacturacionServicioBean.C_IDCUENTA, miForm.getCuentaBancaria());
			hash.put(CenSolModiFacturacionServicioBean.C_IDSERVICIO, idServicio);
			hash.put(CenSolModiFacturacionServicioBean.C_IDTIPOSERVICIOS, idTipoServicios);
			hash.put(CenSolModiFacturacionServicioBean.C_IDSERVICIOSINSTITUCION, idServiciosInstitucion);
// RGG 11-02-2005  No vale ya			hash.put(CenSolModiFacturacionServicioBean.C_IDSUSCRIPCION, idSuscripcion);
			hash.put(CenSolModiFacturacionServicioBean.C_IDPETICION, idPeticion);
			hash.put(CenSolModiFacturacionServicioBean.C_MOTIVO, miForm.getMotivo());
			hash.put(CenSolModiFacturacionServicioBean.C_IDSOLICITUD, idSolicitud);
	 		hash.put(CenSolModiFacturacionServicioBean.C_IDESTADOSOLIC, new Integer(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE));
	 		hash.put(CenSolModiFacturacionServicioBean.C_FECHAALTA, "SYSDATE");
	
			if (!adminFac.insert(hash)) {
				throw new SIGAException(adminFac.getError());
			}
			
			tx.commit();

			salida = this.exitoModal("messages.censo.solicitudes.exito",request);			
			
			
		   } 	catch (Exception e) {
			 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
	   	   }

		   return salida;
	}

	
	/**
	 * Metodo que implementa el modo abrirSolicitud
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirSolicitud (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {

			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;

			// obtengo el identificador de cuenta
			Vector fila = miform.getDatosTablaOcultos(0);
			
			// enviamos el vector entero a la pagina de insercion insercion
			request.setAttribute("PysDatosFacturacionInsertarSolicitud",fila);
			
	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		
		return "abrirSolicitud";

	}

	/**
	 * Metodo que implementa el modo abrirProductos
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirProductos (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {

			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			
			miform.setIdPersona(request.getParameter("idPersona"));
			miform.setIdInstitucion(request.getParameter("idInstitucion"));
			miform.setAccion(request.getParameter("accion"));

			PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm(this.getUserBean(request));
			Hashtable criterios = new Hashtable();
			criterios.put(PysProductosSolicitadosBean.C_IDPERSONA,miform.getIdPersona());
			criterios.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION,ClsConstants.TIPO_PETICION_COMPRA_ALTA);
			criterios.put("FECHA_DESDE","SYSDATE-365"); // HACE UN A�O
			criterios.put("FECHA_HASTA","SYSDATE+1");
			
			Vector resultados = productosAdm.getProductosSolicitados(criterios,new Integer(miform.getIdInstitucion()));
			//Vector resultados = new Vector();
			request.setAttribute("CenProductosSolicitadosResultados",resultados);

			// para saber si es colegiado
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.colegiado";
			} else 
			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.noColegiado";
			} else 
			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			} 
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);

			// nombre y apellidos
			String nombreApellidos = "";
			Vector v = clienteAdm.getDatosPersonales(new Long(miform.getIdPersona()),new Integer(miform.getIdInstitucion()));
			if (v!=null && v.size()>0) {
				Hashtable registro =  (Hashtable)v.get(0);
				if (registro.get("NOMBRE")!=null) nombreApellidos += (String) registro.get("NOMBRE") + "&nbsp;"; 
				if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) nombreApellidos += (String) registro.get("APELLIDOS1") + "&nbsp;"; 
				if (registro.get("APELLIDOS2")!=null) nombreApellidos += (String) registro.get("APELLIDOS2") + "&nbsp;"; 
			}
			request.setAttribute("CenDatosGeneralesNombreApellidos",nombreApellidos);
			
			
	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }
		
		return "abrirProductos";

	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String abrirProductosPaginados(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		request.setAttribute(ClsConstants.PARAM_PAGINACION, paginadorPenstania);
		DatosFacturacionForm miform = (DatosFacturacionForm) formulario;
		
		PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm(this.getUserBean(request));
		
		try {
			HashMap databackup = getPaginador(request, paginadorPenstania);

			if (databackup != null) {

				PaginadorBind paginador = (PaginadorBind) databackup.get("paginador");
				Vector datos = new Vector();
				// Si no es la primera llamada, obtengo la p�gina del request yla busco con el paginador
				String pagina = (String) request.getParameter("pagina");
				String bIncluirRegistrosConBajaLogica = (String) request.getParameter("bIncluirRegistrosConBajaLogica");
				if (bIncluirRegistrosConBajaLogica == null){
					bIncluirRegistrosConBajaLogica = miform.getIncluirRegistrosConBajaLogica();
				}
				request.setAttribute("bIncluirRegistrosConBajaLogica",bIncluirRegistrosConBajaLogica);

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					String idPersona = (String) request.getSession().getAttribute("IDPERSONA");
					//Miramos si es la primera vez que accede a esta pagina, ya que si es asi hay
					//que actualizar los datos pesados ,FECHAEFEC y ESTADOPAGO. 
					//Para ello miramos si existe el dato de estadoPago en el primer registro
					//(no miramos) la fecha efectiva ya que puede ser nula
					Row fila = (Row)datos.get(0);
					Hashtable registro = (Hashtable) fila.getRow();
					String estadoPago = (String) registro.get("ESTADOPAGO");
					if(estadoPago==null)
						datos = actualizarProductosPaginados(idPersona, datos, this.getUserBean(request));
				}
				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(miform.getIncluirRegistrosConBajaLogica());
				String idPersona = request.getParameter("idPersona");
				String idInstitucion = request.getParameter("idInstitucion");
				String accion = request.getParameter("accion");
				request.getSession().setAttribute("IDPERSONA",idPersona);
				request.getSession().setAttribute("IDINSTITUCION",idInstitucion);
				request.getSession().setAttribute("MODO",accion);
				miform.setIdPersona(idPersona);
				miform.setIdInstitucion(idInstitucion);
				miform.setAccion(accion);
				Hashtable criterios = new Hashtable();
				criterios.put(PysProductosSolicitadosBean.C_IDPERSONA, miform.getIdPersona());
				criterios.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION, ClsConstants.TIPO_PETICION_COMPRA_ALTA);
				if(!bIncluirRegistrosConBajaLogica){
					criterios.put("FECHA_DESDE", "SYSDATE-730"); // HACE DOS A�O
					criterios.put("FECHA_HASTA", "SYSDATE+1");
				}
				request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
				
				if (!bIncluirRegistrosConBajaLogica){
					criterios.put(PysServiciosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_BAJA);
				}
				
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
				String nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
				CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(new Long(idPersona),new Integer(idInstitucion));
				
				String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			
				// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
				Hashtable datosColegiado = new Hashtable();
				datosColegiado.put("NOMBRECOLEGIADO",nombre);
				datosColegiado.put("NUMEROCOLEGIADO",numero);
				request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
				
				databackup = new HashMap();
				
				PaginadorBind paginador = productosAdm.getProductosSolicitadosPaginador(criterios,new Integer(miform.getIdInstitucion()));
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros == 0) {
					paginador = null;
				}
				//String idInstitucion = (String) request.getAttribute("idInstitucionPestanha");
				databackup.put("paginador", paginador);
				if (paginador != null) {
					Vector datos = paginador.obtenerPagina(1);
					datos = actualizarProductosPaginados(idPersona, datos, this.getUserBean(request));
					databackup.put("datos", datos);
					setPaginador(request, paginadorPenstania, databackup);
				}
			}
			
		} catch (SIGAException e1) {
			 return exitoRefresco("error.messages.obtenerPagina",request);
			 
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}

		return "abrirProductos";
	}
	
	/**
	 * Metodo que nos actualiza el vector de productos paginados
	 * @param idPersona
	 * @param datos
	 * @param usrBean
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector actualizarProductosPaginados(String idPersona, Vector datos, UsrBean usrBean) throws ClsExceptions {
		PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm(usrBean);
		
		for (int i=0; i<datos.size(); i++) {
			Row fila = (Row)datos.get(i);
			Hashtable registro = (Hashtable) fila.getRow();
			
			String idInstitucion = (String) registro.get(PysProductosSolicitadosBean.C_IDINSTITUCION);
			String idPeticion = (String) registro.get(PysProductosSolicitadosBean.C_IDPETICION);
			String idTipoProducto = (String) registro.get(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
			String idProducto = (String) registro.get(PysProductosSolicitadosBean.C_IDPRODUCTO);
			String idProductoInstitucion = (String) registro.get(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
			String sAceptado = UtilidadesHash.getString(registro, PysProductosSolicitadosBean.C_ACEPTADO);
			String existePysCompra = (String) registro.get("EXISTE_PYS_COMPRA");
									
			if (existePysCompra!=null && !existePysCompra.equals("")) {
			
				// Obtiene FECHAEFEC				
				Vector vFechaEfectiva = productosAdm.getFechaEfectivaCompraProducto(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPeticion, idPersona);
				registro = completarHashSalida(registro, vFechaEfectiva);
				
				// Obtiene ESTADOPAGO
				String estadoCompra = productosAdm.getEstadoCompra(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPeticion);
				String decripcionEstadoCompra = UtilidadesString.getMensajeIdioma(usrBean, estadoCompra);
				UtilidadesHash.set(registro, "ESTADOCOMPRA", estadoCompra);
				UtilidadesHash.set(registro, "DESCRIPCIONESTADOCOMPRA", decripcionEstadoCompra);
				
			} else {
				UtilidadesHash.set(registro, "DESCRIPCIONESTADOCOMPRA", UtilidadesString.getMensajeIdioma(usrBean, "estados.compra.pendiente"));
			}
			if(sAceptado != null && !"".equalsIgnoreCase(sAceptado) && sAceptado.equalsIgnoreCase("D")){
				UtilidadesHash.set(registro, "DESCRIPCIONESTADOCOMPRA", UtilidadesString.getMensajeIdioma(usrBean, "estados.compra.denegado"));
			}
			String estadoProducto = UtilidadesProductosServicios.getEstadoProductoServicio(sAceptado);
			String descripcionEstadoProducto = UtilidadesString.getMensajeIdioma(usrBean, estadoProducto);
			UtilidadesHash.set(registro, "DESCRIPCIONESTADOPRODUCTO", descripcionEstadoProducto);
		}
		
		return datos;
	}
	
	/**
	 * Metodo que nos actualiza el vector de productos paginados con 
	 * 1.FechaEfectiva
	 * 2.Estado Compra
	 * @param idPersona
	 * @param usrBean
	 * @param datos
	 * @return
	 * @throws ClsExceptions
	 */
	private Vector actualizarServiciosPaginados(String idPersona, UsrBean usrBean, Vector datos) throws ClsExceptions {
		
		for (int i=0; i<datos.size(); i++) {
			Row fila = (Row)datos.get(i);
			Hashtable registro = (Hashtable) fila.getRow();
			
			String idInstitucion = (String) registro.get(PysServiciosSolicitadosBean.C_IDINSTITUCION);
			String idPeticion = (String) registro.get(PysServiciosSolicitadosBean.C_IDPETICION);
			String idTipoServicio = (String) registro.get(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
			String idServicio = (String) registro.get(PysServiciosSolicitadosBean.C_IDSERVICIO);
			String idServicioInstitucion = (String) registro.get(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
			
			boolean bAnticipado=PysServicioAnticipoAdm.getAnticipoLetradoActivo(idInstitucion, idPersona, idTipoServicio, idServicio, idServicioInstitucion);
			registro.put("SERVICIO_ANTICIPADO", new Boolean(bAnticipado));
			
			PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm(usrBean);
			Vector vPrecioServicios = serviciosAdm.getPrecioServicio(idInstitucion, idTipoServicio, idServicio, idServicioInstitucion, idPersona, usrBean.getLanguageInstitucion());
			
			//registro = completarHashSalida(registro, vPrecioServicios); => JPT: No lo utiliza la jsp
			
			String valor = "-1";
			if (vPrecioServicios!=null && vPrecioServicios.size()>0) {
				valor = (String) ((Hashtable) vPrecioServicios.get(0)).get("PRECIO_SERVICIO");
			}
			
			// "-1" --> Error no existen datos en la tabla Pys_ServicioInstitucion
			if (!valor.equalsIgnoreCase("-1")){

				String datosPrecio[] =  UtilidadesString.split(valor, "#");

				// RGG cambio para 10g
				String diezg = datosPrecio[0];
				diezg = diezg.replaceAll(",",".");

				UtilidadesHash.set(registro, "VALOR", new Double(diezg));
				// RGG cambio para 10g
				String diezgg = datosPrecio[1];
				diezgg = diezgg.replaceAll(",",".");

				//UtilidadesHash.set(registro, "VALORIVA", new Float(diezgg)); => JPT: No lo utiliza la jsp
				UtilidadesHash.set(registro, "VALORIVA", new Float(diezgg));
				
				//UtilidadesHash.set(registro, "SERVICIO_IDPRECIOSSERVICIOS", new Integer(datosPrecio[2])); => JPT: No lo utiliza la jsp
				//UtilidadesHash.set(registro, "SERVICIO_IDPERIODICIDAD", new Integer(datosPrecio[3])); => JPT: No lo utiliza la jsp
				UtilidadesHash.set(registro, "SERVICIO_DESCRIPCION_PERIODICIDAD", datosPrecio[4]);
								
				if (datosPrecio.length == 6) {
					//Concatenaci�n de la descripcion del servicio + descripcion del precio
					UtilidadesHash.set(registro, "CONCEPTO", UtilidadesHash.getString(registro, "CONCEPTO") + ' ' + datosPrecio[5]); 
				}
			}
			
			// ESTADOPAGO
			String sEstadoSuscripcion = serviciosAdm.getEstadoSuscripcion(idInstitucion, idTipoServicio, idServicio, idServicioInstitucion, idPeticion);
			UtilidadesHash.set(registro, "ESTADOPAGO", sEstadoSuscripcion);
			
			String sAceptado = UtilidadesHash.getString(registro, "ACEPTADO");
			String sEstadoProductoServicio = UtilidadesProductosServicios.getEstadoProductoServicio(sAceptado);
			sEstadoProductoServicio = UtilidadesString.getMensajeIdioma(usrBean, sEstadoProductoServicio);
			UtilidadesHash.set(registro, "ESTADOSERVICIO", sEstadoProductoServicio);
		}
		
		return datos;
	}
	
	public Hashtable completarHashSalida(Hashtable htSalida, Vector vParcial){
		
		if (vParcial!=null && vParcial.size()>0) {
			Hashtable registro = (Hashtable) vParcial.get(0);
			htSalida.putAll(registro);
		}
	
		return htSalida;
		
		
	}
	/**
	 * Metodo que implementa el modo abrirServicios
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirServicios (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {

			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;

			miform.setIdPersona(request.getParameter("idPersona"));
			miform.setIdInstitucion(request.getParameter("idInstitucion"));
			miform.setAccion(request.getParameter("accion"));

			PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm(this.getUserBean(request));
			Hashtable criterios = new Hashtable();
			criterios.put(PysServiciosSolicitadosBean.C_IDPERSONA,miform.getIdPersona());
			criterios.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION,ClsConstants.TIPO_PETICION_COMPRA_ALTA);
//			criterios.put("FECHA_DESDE","SYSDATE-365"); // HACE UN A�O
//			criterios.put("FECHA_HASTA","SYSDATE+1");
			boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(miform.getIncluirRegistrosConBajaLogica());
			request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
			
			if (!bIncluirRegistrosConBajaLogica){
				criterios.put(PysServiciosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_BAJA);
			}
			
			Vector resultados = serviciosAdm.getServiciosSolicitados(criterios,new Integer(miform.getIdInstitucion()));
			//Vector resultados = new Vector();
			request.setAttribute("CenServiciosSolicitadosResultados",resultados);

			// para saber si es colegiado
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.colegiado";
			} else 
			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.noColegiado";
			} else 
			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			} 
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);
			
			// nombre y apellidos
			String nombreApellidos = "";
			Vector v = clienteAdm.getDatosPersonales(new Long(miform.getIdPersona()),new Integer(miform.getIdInstitucion()));
			if (v!=null && v.size()>0) {
				Hashtable registro =  (Hashtable)v.get(0);
				if (registro.get("NOMBRE")!=null) nombreApellidos += (String) registro.get("NOMBRE") + "&nbsp;"; 
				if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) nombreApellidos += (String) registro.get("APELLIDOS1") + "&nbsp;"; 
				if (registro.get("APELLIDOS2")!=null) nombreApellidos += (String) registro.get("APELLIDOS2") + "&nbsp;"; 
			}
			request.setAttribute("CenDatosGeneralesNombreApellidos",nombreApellidos);
			
			request.setAttribute("datosFacturacionForm", formulario);
			
	   } 	catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }

	   return "abrirServicios";
	}
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String abrirServiciosPaginados (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION, paginadorPenstania);
		
		DatosFacturacionForm miform = (DatosFacturacionForm) formulario;
		
		PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm(this.getUserBean(request));
		
		
		try {
			HashMap databackup = getPaginador(request, paginadorPenstania);
			if (databackup != null) {
				String incluirBajaLogica = request.getParameter("accion");
				
				PaginadorBind paginador = (PaginadorBind) databackup.get("paginador");
				Vector datos = new Vector();
				// Si no es la primera llamada, obtengo la p�gina del request y
				// la busco con el paginador
				String pagina = (String) request.getParameter("pagina");
				String bIncluirRegistrosConBajaLogica = (String) request.getParameter("bIncluirRegistrosConBajaLogica");
				if (bIncluirRegistrosConBajaLogica == null){
					bIncluirRegistrosConBajaLogica = miform.getIncluirRegistrosConBajaLogica();
				}
				request.setAttribute("bIncluirRegistrosConBajaLogica",bIncluirRegistrosConBajaLogica);
				
				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					
					String idPersona = (String) request.getSession().getAttribute("IDPERSONA");
					//Miramos si es la primera vez que accede a esta pagina, ya que si es asi hay
					//que actualizar los datos pesados ,PRECIO_SERVICIO,SERVICIO_ANTICIPADO y ESTADOPAGO. 
					//Para ello miramos si existe el dato de estadoPago en el primer registro
					//(no miramos) la fecha efectiva ya que puede ser nula
					Row fila = (Row)datos.get(0);
					Hashtable registro = (Hashtable) fila.getRow();
					String estadoPago = (String) registro.get("ESTADOPAGO");
					if(estadoPago==null)
						datos = actualizarServiciosPaginados(idPersona, this.getUserBean(request), datos);
				}
				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				boolean bIncluirRegistrosConBajaLogica = UtilidadesString.stringToBoolean(miform.getIncluirRegistrosConBajaLogica());
				request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
				
				String idPersona = request.getParameter("idPersona");
				String idInstitucion = request.getParameter("idInstitucion");
				String accion = request.getParameter("accion");
				
				request.getSession().setAttribute("IDPERSONA",idPersona);
				request.getSession().setAttribute("IDINSTITUCION",idInstitucion);
				request.getSession().setAttribute("MODO",accion);
				miform.setIdPersona(idPersona);
				miform.setIdInstitucion(idInstitucion);
				miform.setAccion(accion);
				Hashtable criterios = new Hashtable();
				criterios.put(PysServiciosSolicitadosBean.C_IDPERSONA,miform.getIdPersona());
				criterios.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION,ClsConstants.TIPO_PETICION_COMPRA_ALTA);
				request.setAttribute("bIncluirRegistrosConBajaLogica", "" + bIncluirRegistrosConBajaLogica);
				
				if (!bIncluirRegistrosConBajaLogica){
					criterios.put(PysServiciosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_BAJA);
				}
				
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
				String nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));
				CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(new Long(idPersona),new Integer(idInstitucion));
				
				String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			
				// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
				Hashtable datosColegiado = new Hashtable();
				datosColegiado.put("NOMBRECOLEGIADO",nombre);
				datosColegiado.put("NUMEROCOLEGIADO",numero);
				request.getSession().setAttribute("DATOSCOLEGIADO", datosColegiado);
				
				databackup = new HashMap();
				
				PaginadorBind paginador = serviciosAdm.getServiciosSolicitadosPaginador(criterios, new Integer(miform.getIdInstitucion()));
								
				// Paginador paginador = new Paginador(sql);
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros == 0) {
					paginador = null;
				}
				//String idInstitucion = (String) request.getAttribute("idInstitucionPestanha");
				databackup.put("paginador", paginador);
				if (paginador != null) {
					Vector datos = paginador.obtenerPagina(1);
					datos = actualizarServiciosPaginados(idPersona, this.getUserBean(request), datos);
					databackup.put("datos", datos);
					setPaginador(request, paginadorPenstania, databackup);
				}

			}
			
		} catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
			 
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}

		return "abrirServicios";
	}
	
	/**
	 * Metodo que implementa el modo modificarProducto
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificarProducto (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		String salida = "";
		try {
			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			// Tambien se le cambia a la compra si exite
			Hashtable hashPysCompra = new Hashtable();			
			hashPysCompra.put(PysCompraBean.C_IDINSTITUCION, miform.getIdInstitucion());
			hashPysCompra.put(PysSuscripcionBean.C_IDPERSONA, miform.getIdPersona());
			hashPysCompra.put(PysCompraBean.C_IDPETICION, request.getParameter("idPeticionSel"));
			hashPysCompra.put(PysCompraBean.C_IDTIPOPRODUCTO, request.getParameter("idTipoProductoSel"));
			hashPysCompra.put(PysCompraBean.C_IDPRODUCTO, request.getParameter("idProductoSel"));
			hashPysCompra.put(PysCompraBean.C_IDPRODUCTOINSTITUCION, request.getParameter("idProductoInstitucionSel"));
			
			String idFormaPagoCuenta = String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA);
			
			tx = user.getTransaction();
			tx.begin();			
			
			// Obtengo la compra del producto para modificar la forma de pago y/o la cuenta bancaria
			PysCompraAdm admPysCompra = new PysCompraAdm(user);
			Vector vPysCompra = admPysCompra.select(hashPysCompra);
			if (vPysCompra!=null && vPysCompra.size()>0) {
				PysCompraBean beanPysCompra = (PysCompraBean) vPysCompra.get(0);
			    if (request.getParameter("formaPago").equals(idFormaPagoCuenta)) {
			    	beanPysCompra.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA));
			    	beanPysCompra.setIdCuenta(new Integer(request.getParameter("nCuenta")));
				} else {
					beanPysCompra.setIdFormaPago(new Integer(request.getParameter("formaPago")));
					beanPysCompra.setIdCuenta(null);
				}
			    
			    if (!admPysCompra.updateDirect(beanPysCompra)) {
			        throw new ClsExceptions("Error al intentar actualizar PYS_COMPRA");
			    }
			}			

			if (request.getParameter("formaPago").equals(idFormaPagoCuenta)) {
				hashPysCompra.put(PysCompraBean.C_IDFORMAPAGO, ClsConstants.TIPO_FORMAPAGO_FACTURA);
				hashPysCompra.put(PysCompraBean.C_IDCUENTA, request.getParameter("nCuenta"));
			} else {
				hashPysCompra.put(PysCompraBean.C_IDFORMAPAGO, request.getParameter("formaPago"));
				hashPysCompra.put(PysCompraBean.C_IDCUENTA,"");
			}

			// Cargo una hastable con los valores originales del registro sobre el que se realizar� la modificacion
			Hashtable hashOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");

			// insert unico para el historico
			Hashtable hashHist = new Hashtable();			
			hashHist.put(CenHistoricoBean.C_MOTIVO, miform.getMotivo());
			CenHistoricoAdm admHis = new CenHistoricoAdm (this.getUserBean(request));
			if (!admHis.insertCompleto(hashHist, hashPysCompra, hashOriginal, "PysCompraBean", CenHistoricoAdm.ACCION_UPDATE, this.getLenguaje(request))) {
				throw new SIGAException(admHis.getError());
			}	
			
			/* 	IMPORTANTE JPT: 
			 *  - No se modifica la factura al cambiar la forma de pago o cuenta de una compra, ya que la factura puede contener varios productos y todos deben tener la misma forma de pago y cuenta
			 *  - No se actualizan los productos solicitados, solo PYS_COMPRA
			 */			
			
			tx.commit();			
			
			salida = this.exitoModal("messages.updated.success", request);
			
	   } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }

		return salida;
	}

	
	/**
	 * Metodo que implementa el modo modificarServicio
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificarServicio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		String salida = "";
		try {
			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			Hashtable hash= new Hashtable();
			hash.put(PysSuscripcionBean.C_IDINSTITUCION, miform.getIdInstitucion());
			hash.put(PysSuscripcionBean.C_IDPERSONA, miform.getIdPersona());
			hash.put(PysSuscripcionBean.C_IDPETICION, request.getParameter("idPeticionSel"));
			hash.put(PysSuscripcionBean.C_IDTIPOSERVICIOS, request.getParameter("idTipoServicioSel"));
			hash.put(PysSuscripcionBean.C_IDSERVICIO, request.getParameter("idServicioSel"));
			hash.put(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, request.getParameter("idServicioInstitucionSel"));
			
			String idFormaPagoCuenta = String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA);
			
			tx = user.getTransaction();
			tx.begin();				
			
			// Obtengo la suscripcion al servicio para modificar la forma de pago y/o la cuenta bancaria
			PysSuscripcionAdm admSus = new PysSuscripcionAdm(this.getUserBean(request));
			Vector v = admSus.select(hash);
			if (v!=null && v.size()>0) {
			    PysSuscripcionBean beanSus = (PysSuscripcionBean) v.get(0);
			    if (request.getParameter("formaPago").equals(idFormaPagoCuenta)) {
			    	beanSus.setIdFormaPago(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA));
			    	beanSus.setIdCuenta(new Integer(request.getParameter("nCuenta")));
				} else {
					beanSus.setIdFormaPago(new Integer(request.getParameter("formaPago")));
					beanSus.setIdCuenta(null);
				}
			    
			    if (!admSus.updateDirect(beanSus)) {
			        throw new ClsExceptions("Error al intentar actualizar PYS_SUSCRIPCION");
			    }
			}
						
			if (request.getParameter("formaPago").equals(idFormaPagoCuenta)) {
				hash.put(PysSuscripcionBean.C_IDFORMAPAGO, ClsConstants.TIPO_FORMAPAGO_FACTURA);
				hash.put(PysSuscripcionBean.C_IDCUENTA, request.getParameter("nCuenta"));
			} else {
				hash.put(PysSuscripcionBean.C_IDFORMAPAGO, request.getParameter("formaPago"));
				hash.put(PysSuscripcionBean.C_IDCUENTA,"");
			}

			// Cargo una hastable con los valores originales del registro sobre el que se realizar� la modificacion
			Hashtable hashOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");

			// insert unico para el historico
			Hashtable hashHist = new Hashtable();			
			hashHist.put(CenHistoricoBean.C_MOTIVO, miform.getMotivo());
			CenHistoricoAdm admHis = new CenHistoricoAdm (this.getUserBean(request));
			if (!admHis.insertCompleto(hashHist, hash, hashOriginal, "PysSuscripcionBean", CenHistoricoAdm.ACCION_UPDATE, this.getLenguaje(request))) {
				throw new SIGAException(admHis.getError());
			}	
			
			/* 	IMPORTANTE JPT: 
			 *  - No se modifica la factura al cambiar la forma de pago o cuenta de una suscripcion, ya que la factura puede contener varios servicios y todos deben tener la misma forma de pago y cuenta
			 *  - No se actualizan los servicios solicitados, solo PYS_SUSCRIPCION
			 */

			tx.commit();
			
			salida = this.exitoModal("messages.updated.success",request);
			
	   } catch (Exception e) {
		 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
   	   }

		return salida;
	}
	
	/**
	 * Metodo que implementa el modo ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salida = "";
		try {

			DatosFacturacionForm miform = (DatosFacturacionForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// obtengo el identificador de cuenta
			Vector fila = miform.getDatosTablaOcultos(0);
			String tipo = miform.getPos();

			if (tipo!=null && tipo.equals("P")) {
				miform.setIdInstitucion((String)fila.get(0));			
				miform.setIdPersona((String)fila.get(4));
			} else {
				miform.setIdInstitucion((String)fila.get(1));			
				miform.setIdPersona((String)fila.get(6));
			}
			// para saber si es colegiado			
			CenColegiadoAdm admCol = new CenColegiadoAdm(user);
			CenColegiadoBean beanCol = admCol.getDatosColegiales(new Long(miform.getIdPersona()), new Integer(miform.getIdInstitucion()));
			CenClienteAdm clienteAdm = new CenClienteAdm(user);
			String tipoColegiado = clienteAdm.getTipoCliente(beanCol);
			if (!tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				// es colegiado
				// obtengo sus datos colegiales para coger el numero de colegiado
				String nocol = admCol.getIdentificadorColegiado(beanCol);
				if (!nocol.equals("")) {
					request.setAttribute("CenDatosGeneralesNoColegiado",nocol);
				}
			}

			if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.colegiado";
			} else if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO)) {
				tipoColegiado = "censo.tipoCliente.noColegiado";
			} else if (tipoColegiado.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoColegiado = "censo.tipoCliente.colegiadoBaja";
			} 
			request.setAttribute("CenDatosGeneralesColegiado",tipoColegiado);

			// nombre y apellidos
			String nombreApellidos = "";
			Vector v = clienteAdm.getDatosPersonales(new Long(miform.getIdPersona()),new Integer(miform.getIdInstitucion()));
			if (v!=null && v.size()>0) {
				Hashtable registro =  (Hashtable)v.get(0);
				if (registro.get("NOMBRE")!=null) nombreApellidos += (String) registro.get("NOMBRE") + "&nbsp;"; 
				if (registro.get("APELLIDOS1")!=null && !registro.get("APELLIDOS1").equals("#NA")) nombreApellidos += (String) registro.get("APELLIDOS1") + "&nbsp;"; 
				if (registro.get("APELLIDOS2")!=null) nombreApellidos += (String) registro.get("APELLIDOS2") + "&nbsp;"; 
			}
			request.setAttribute("CenDatosGeneralesNombreApellidos",nombreApellidos);

			// servicios
			Hashtable hash= new Hashtable();
			hash.put(PysSuscripcionBean.C_IDINSTITUCION,miform.getIdInstitucion());
			hash.put(PysSuscripcionBean.C_IDPERSONA,miform.getIdPersona());
			hash.put(PysSuscripcionBean.C_IDPETICION,(String)fila.get(5));
			hash.put(PysSuscripcionBean.C_IDTIPOSERVICIOS,(String)fila.get(2));
			hash.put(PysSuscripcionBean.C_IDSERVICIO,(String)fila.get(3));
			hash.put(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION,(String)fila.get(4));
			String idFormaPagoCuenta = new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA).toString();
			String idFormaPago = (String)fila.get(7);
			hash.put(PysSuscripcionBean.C_IDFORMAPAGO,idFormaPago);

			//Tratamiento por si no hay cuenta seleccionada. En sesion meto "" y al JSP la primera opcion: 0.
			if (idFormaPago.equals(idFormaPagoCuenta)) {
				String idcuenta = "";
				if (fila.get(0)!=null && !((String)fila.get(0)).equals("0"))
					idcuenta = (String)fila.get(0);
				hash.put(PysSuscripcionBean.C_IDCUENTA,idcuenta);
			}

			// Cargo una hastable con los valores originales del registro sobre el que se realizar� la modificacion						
			request.getSession().setAttribute("DATABACKUP",hash);
			
			request.setAttribute("CenDatosIdFormaPagoSel",(String)fila.get(7));
			request.setAttribute("CenDatosIdCuentaSel",(String)fila.get(0));
			request.setAttribute("CenDatosIdPeticion",(String)fila.get(5));
			request.setAttribute("CenFechaEfectiva",(String)fila.get(8));

			Vector aux = new Vector();
			aux.add((String)fila.get(1));
			aux.add((String)fila.get(2));
			aux.add((String)fila.get(3));
			aux.add((String)fila.get(4));
			request.setAttribute("CenDatosFormaPagoServicios",aux);
			request.setAttribute("modo","ver");
			salida = "verServicio";
	
	   } catch (Exception e) {
		   throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
   	   }

		return salida;
	}
}