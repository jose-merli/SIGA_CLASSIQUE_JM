package com.siga.censo.ws.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatos;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDireccion;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.censo.ws.form.EdicionColegiadoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class EdicionColegiadoAction extends MasterAction {
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_LISTADO_COLEGIADOS_HISTORICO";
	
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
				String accion = miForm.getModo();
				return super.executeInternal(mapping,formulario,request,response);				
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
	
	protected String ver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{
		return verEditar("ver", mapping, formulario, request, response);
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return verEditar("editar", mapping, formulario, request, response);
	}
	
	protected String verEditar(String accion, ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			HttpSession session = request.getSession();
			session.removeAttribute(DATAPAGINADOR);
			
			EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm) formulario;
						
			Long idcensodatos = null;
			
			if (edicionColegiadoForm.getIdcensodatosPadre() != null) {
				idcensodatos = edicionColegiadoForm.getIdcensodatosPadre();
			} else {
				// Recuperamos los datos del registro que hemos seleccionado
				Vector ocultos = formulario.getDatosTablaOcultos(0);
				if (ocultos != null && ocultos.size() > 0) {
					idcensodatos = Long.valueOf(ocultos.get(0).toString());
				} else {
					throw new IllegalArgumentException("No se ha recibido el identificador para editar el colegiado");
				}
			}
			
			if (edicionColegiadoForm.isHistorico()) {
				edicionColegiadoForm.setIdcensodatosPadre(edicionColegiadoForm.getIdcensodatos());
			}
			
					
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			EcomCenDatos ecomCenDatos = cenWSService.getEcomCenDatosByPk(idcensodatos);
						
			edicionColegiadoForm.setIdcensodatos(ecomCenDatos.getIdcensodatos());
			
			edicionColegiadoForm.setPublicarcolegiado(getValue(ecomCenDatos.getPublicarcolegiado()));
			edicionColegiadoForm.setNumsolicitudcolegiacion(getValue(ecomCenDatos.getNumsolicitudcolegiacion()));
			edicionColegiadoForm.setNcolegiado(getValue(ecomCenDatos.getNcolegiado()));
			edicionColegiadoForm.setNombre(getValue(ecomCenDatos.getNombre()));
			edicionColegiadoForm.setApellido1(getValue(ecomCenDatos.getApellido1()));
			edicionColegiadoForm.setApellido2(getValue(ecomCenDatos.getApellido2()));
			edicionColegiadoForm.setSexo(getValue(ecomCenDatos.getSexo()));
			edicionColegiadoForm.setFechanacimiento(getValue(ecomCenDatos.getFechanacimiento()));
			edicionColegiadoForm.setIdcensotipoidentificacion(getValue(ecomCenDatos.getIdcensotipoidentificacion()));
			edicionColegiadoForm.setPublicartelefono(getValue(ecomCenDatos.getPublicartelefono()));
			edicionColegiadoForm.setTelefono(getValue(ecomCenDatos.getTelefono()));
			edicionColegiadoForm.setPublicartelefonomovil(getValue(ecomCenDatos.getPublicartelefonomovil()));
			edicionColegiadoForm.setTelefonomovil(getValue(ecomCenDatos.getTelefonomovil()));
			edicionColegiadoForm.setPublicarfax(getValue(ecomCenDatos.getPublicarfax()));
			edicionColegiadoForm.setFax(getValue(ecomCenDatos.getFax()));
			edicionColegiadoForm.setPublicaremail(getValue(ecomCenDatos.getPublicaremail()));
			edicionColegiadoForm.setEmail(getValue(ecomCenDatos.getEmail()));
			edicionColegiadoForm.setIdecomcensosituacionejer(getValue(ecomCenDatos.getIdecomcensosituacionejer()));
			edicionColegiadoForm.setFechasituacion(getValue(ecomCenDatos.getFechasituacion()));
			edicionColegiadoForm.setResidente(getValue(ecomCenDatos.getResidente()));
			
			EcomCenDireccion ecomCenDireccion = cenWSService.getEcomCenDireccionesByPk(ecomCenDatos.getIdcensodireccion());
			edicionColegiadoForm.setIdcensodireccion(getValue(ecomCenDatos.getIdcensodireccion()));
			edicionColegiadoForm.setPublicardireccion(getValue(ecomCenDireccion.getPublicar()));
			edicionColegiadoForm.setDesctipovia(getValue(ecomCenDireccion.getDesctipovia()));
			edicionColegiadoForm.setDomicilio(getValue(ecomCenDireccion.getDomicilio()));
			edicionColegiadoForm.setCodigopostal(getValue(ecomCenDireccion.getCodigopostal()));
			edicionColegiadoForm.setCodigopaisextranj(getValue(ecomCenDireccion.getCodigopaisextranj()));
			edicionColegiadoForm.setCodigoprovincia(getValue(ecomCenDireccion.getCodigoprovincia()));
			edicionColegiadoForm.setCodigopoblacion(getValue(ecomCenDireccion.getCodigopoblacion()));
			edicionColegiadoForm.setDescripcionpoblacion(getValue(ecomCenDireccion.getDescripcionpoblacion()));
			
			edicionColegiadoForm.setIncidencias(cenWSService.getIncidencias(ecomCenDatos.getIdcensodatos()));
						
			// Entramos al formulario en modo 'modificación'
			session.setAttribute("accion", "editar");
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return accion;
	}

	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm) formulario;			
			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<EdicionColegiadoForm> paginador = (PaginadorVector<EdicionColegiadoForm>) databackup.get("paginador");
				List<EdicionColegiadoForm> datos = new ArrayList<EdicionColegiadoForm>();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("paginaListaCol");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
						// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
				List<EdicionColegiadoForm> edicionColegiadoForms = new ArrayList<EdicionColegiadoForm>();
				List<EcomCenDatos> ecomCenDatos = getDatos(edicionColegiadoForm);	
				if (ecomCenDatos != null) {
					for (EcomCenDatos ecomCenDato : ecomCenDatos) {
						EdicionColegiadoForm ecf = new EdicionColegiadoForm();
						ecf.setIdcensodatos(ecomCenDato.getIdcensodatos());
						ecf.setFechaCambio(GstDate.getFormatedDateLong("ES", ecomCenDato.getFechamodificacion()));
						ecf.setIncidencias(cenWSService.getIncidencias(ecomCenDato.getIdcensodatos()));
						edicionColegiadoForms.add(ecf);
					}
				}
				
				PaginadorVector<EdicionColegiadoForm> paginador = new PaginadorVector(edicionColegiadoForms);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					edicionColegiadoForms = paginador.obtenerPagina(1);
					databackup.put("datos", edicionColegiadoForms);
					request.getSession().setAttribute(DATAPAGINADOR, databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}
			}				
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "resultado";
	}


	private List<EcomCenDatos> getDatos(EdicionColegiadoForm edicionColegiadoForm) {
		CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
		return cenWSService.getHistorico(edicionColegiadoForm.getIdcensodatos());		
	}


	private String getValue(Object value) throws ClsExceptions {	
		String ret = "";
		if (value != null) {
			if (value instanceof Date) {
				ret = GstDate.getFormatedDateShort((Date)value);
			} else {
				ret = value.toString();
			}
		}
		
		return ret;
	}


	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
