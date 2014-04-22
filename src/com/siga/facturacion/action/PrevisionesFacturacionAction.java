/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * <p>
 * Clase que gestiona las previsiones de facturación.
 * </p>
 */

package com.siga.facturacion.action;

import java.io.File;
import com.atos.utils.GstDate;

import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.struts.action.*;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.PrevisionesFacturacionForm;


public class PrevisionesFacturacionAction extends MasterAction {

	/**
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo
	 * del formulario ejecuta distintas acciones
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
or	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * 
	 * @return String Destino del action
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	public ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			do {
				miForm = (MasterForm) formulario;

				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("")
							|| accion.equalsIgnoreCase("abrir")) {
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					}

					else if (accion.equalsIgnoreCase("download")) {
						mapDestino = download(mapping, miForm, request,
								response);
					}

					else if (accion.equalsIgnoreCase("programar")) {
						mapDestino = programar(mapping, miForm, request,
								response);
					}

					else {
						return super.executeInternal(mapping, formulario,
								request, response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo",
						"", "0", "GEN00", "15");
			}

			return mapping.findForward(mapDestino);

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.censo" });
		}

	}

	/**
	 * Funcion que atiende la accion abrirConParametros
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * 
	 * @return String Destino del action
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String abrirConParametros(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions {
		return mapSinDesarrollar;

	}

	/**
	 * Es el metodo inicial que se ejecuta al entrar a la pantalla. Ejecuta el
	 * método de la búsqueda.
	 * 
	 * @param ActionMapping
	 *            mapping Mapeador de las acciones.
	 * @param MasterForm
	 *            formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest
	 *            request: información de entrada de la pagina original.
	 * @param HttpServletResponse
	 *            response: información de salida para la pagina destino.
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		return "abrir";
	}

	/**
	 * Hace una busqueda en base de datos de las previsiones de facturación para
	 * la institución del usuario conectado.
	 * 
	 * @param ActionMapping
	 *            mapping Mapeador de las acciones.
	 * @param MasterForm
	 *            formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest
	 *            request: información de entrada de la pagina original.
	 * @param HttpServletResponse
	 *            response: información de salida para la pagina destino.
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute(
					"USRBEAN");
			String idInstitucion = user.getLocation();

			FacPrevisionFacturacionAdm admPrev = new FacPrevisionFacturacionAdm(
					this.getUserBean(request));
			String where = " where ";
			where += FacPrevisionFacturacionBean.T_NOMBRETABLA + "."
					+ FacPrevisionFacturacionBean.C_IDINSTITUCION + "="
					+ idInstitucion;

			Vector datosPrev = admPrev.selectTabla(where);
			request.setAttribute("datosPrev", datosPrev);
		} catch (Exception e) {
			throwExcp(
					"messages.general.error",
					new String[] { "modulo.facturacion.previsionesFacturacion" },
					e, null);
		}

		return "buscar";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping,
	 *      com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping,
	 *      com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Se va a mostrar una ventana modal para poder insertar una nueva prevision
	 * de facturación.
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {

		return "nuevaPrevisionFacturacion";
	}

	/**
	 * Ejecuta un sentencia INSERT en la Base de Datos para previsiones de
	 * facturación y genera el fichero de previsiones
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		String forward = "";
		UserTransaction tx = null;
		Hashtable miHash = new Hashtable();
		boolean result = false;
		FacPrevisionFacturacionAdm admPrev = null;
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PrevisionesFacturacionForm formPrev = (PrevisionesFacturacionForm) formulario;

			admPrev = new FacPrevisionFacturacionAdm(this.getUserBean(request));
			
			
			{	// Comprobamos si existe ese nombre para la institucion. Debe ser unico
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, FacPrevisionFacturacionBean.C_IDINSTITUCION, user.getLocation());
				UtilidadesHash.set(h, FacPrevisionFacturacionBean.C_DESCRIPCION,   formPrev.getDescripcionPrevision());
				Vector v = admPrev.select(h);
				if ((v != null) && (v.size() > 0)) {
					throw new SIGAException(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "facturacion.nuevaPrevisionFacturacion.error.descripcionDuplicada"));
				}
			}
			

			String fechaInicioProductosString = formPrev
					.getFechaInicioProductos();
			String fechaFinProductosString = formPrev.getFechaFinProductos();
			String fechaInicioServiciosString = formPrev
					.getFechaInicioServicios();
			String fechaFinServiciosString = formPrev.getFechaFinServicios();

			GstDate gstDate = new GstDate();
			Date fechaInicioProductos = gstDate.parseStringToDate(
					fechaInicioProductosString, "dd/MM/yyyy", new Locale(user
							.getLanguage()));
			Date fechaFinProductos = gstDate.parseStringToDate(
					fechaFinProductosString, "dd/MM/yyyy", new Locale(user
							.getLanguage()));
			Date fechaInicioServicios = gstDate.parseStringToDate(
					fechaInicioServiciosString, "dd/MM/yyyy", new Locale(user
							.getLanguage()));
			Date fechaFinServicios = gstDate.parseStringToDate(
					fechaFinServiciosString, "dd/MM/yyyy", new Locale(user
							.getLanguage()));

			ClsLogging.writeFileLog("RGG: PROCESO PREVISION FACTURACION........... Llamada a inserción y ejecución de facturación. "+formPrev.getIdSerieFacturacion().toString()+":"+fechaInicioProductosString+"-"+fechaFinProductosString+"-"+fechaInicioServiciosString+"-"+fechaFinProductosString,10);

			if ((fechaInicioProductos.before(fechaFinProductos))
					&& (fechaInicioServicios.before(fechaFinServicios))) {

				tx = user.getTransactionPesada();
				tx.begin();

				String idLenguaje = user.getLanguage().toUpperCase();
				String idInstitucion = user.getLocation();
				String idSerieFacturacion = formPrev.getIdSerieFacturacion()
						.toString();

				// Calculamos la previsión
				String where = " Where ";
				where += FacPrevisionFacturacionBean.T_NOMBRETABLA + "."
						+ FacPrevisionFacturacionBean.C_IDINSTITUCION + "="
						+ idInstitucion + " And "
						+ FacPrevisionFacturacionBean.T_NOMBRETABLA + "."
						+ FacPrevisionFacturacionBean.C_IDSERIEFACTURACION
						+ "=" + idSerieFacturacion;
				;
				Vector vec = admPrev.selectTabla_2(where);
				Hashtable hashMaximo = (Hashtable) vec.get(0);
				String idPrevision = UtilidadesHash.getString(hashMaximo,
						FacPrevisionFacturacionBean.C_IDPREVISION);

			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//				ReadProperties rp = new ReadProperties("SIGA.properties");
				String nombreFichero = rp
						.returnProperty("facturacion.prefijo.ficherosPrevisiones")
						+ idSerieFacturacion + "_" + idPrevision;
				;

				
				miHash.put(FacPrevisionFacturacionBean.C_IDINSTITUCION,
						idInstitucion);
				miHash.put(FacPrevisionFacturacionBean.C_IDSERIEFACTURACION,
						idSerieFacturacion);
				miHash.put(FacPrevisionFacturacionBean.C_IDPREVISION,
						idPrevision);
				miHash.put(FacPrevisionFacturacionBean.C_FECHAINICIOPRODUCTOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaInicioProductosString));
				miHash.put(FacPrevisionFacturacionBean.C_FECHAFINPRODUCTOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaFinProductosString));
				miHash.put(FacPrevisionFacturacionBean.C_FECHAINICIOSERVICIOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaInicioServiciosString));
				miHash.put(FacPrevisionFacturacionBean.C_FECHAFINSERVICIOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaFinServiciosString));
				miHash.put(FacPrevisionFacturacionBean.C_NOMBREFICHERO,
						nombreFichero);
				miHash.put(FacPrevisionFacturacionBean.C_DESCRIPCION, formPrev.getDescripcionPrevision());

				// Insertamos el registro.
				result = admPrev.insert(miHash);

				// RGG 13/02/2007 CIERRO LA TRANSACCION ANTES PARA QUE NO ME DE PROBLEMAS DE 
				// TIEMPO EL PROCESO DE CREAR EL FICHERO DE PREVISIONES.
				tx.commit();

				
				// Se crea el directorio en el servidor web.
				String sRutaJava = rp
						.returnProperty("facturacion.directorioPrevisionesJava");
				String sRutaFisicaJava = rp
						.returnProperty("facturacion.directorioFisicoPrevisionesJava");
				sRutaJava = sRutaFisicaJava + File.separator + sRutaJava;
				sRutaJava += File.separator + idInstitucion;
				File fDirectorio = new File(sRutaJava);
				fDirectorio.mkdirs();

				// Se genera el fichero de previsiones en el servidor Oracle.
				String sRutaOracle = rp
						.returnProperty("facturacion.directorioPrevisionesOracle");
				// Tratamiento de la barra de la ruta para posibles cambios de
				// servidor de BBDD
				String sBarra = "";
				if (sRutaOracle.indexOf("/") > -1)
					sBarra = "/";
				if (sRutaOracle.indexOf("\\") > -1)
					sBarra = "\\";
				sRutaOracle += sBarra + idInstitucion;
				String sExtension = "."
						+ rp
								.returnProperty("facturacion.extension.ficherosPrevisiones");
				nombreFichero += sExtension;

				//RGG
				tx.begin();
				
				Object[] param_in = new Object[6];
				param_in[0] = idInstitucion;
				param_in[1] = idSerieFacturacion;
				param_in[2] = idPrevision;
				param_in[3] = sRutaOracle;
				param_in[4] = nombreFichero;
				param_in[5] = idLenguaje;
				String resultado[] = new String[2];
				resultado = ClsMngBBDD
						.callPLProcedure(
								"{call PKG_SIGA_FACTURACION.PREVISIONFACTURACION(?,?,?,?,?,?,?,?)}",
								2, param_in);
				//            	 resultado = ClsMngBBDD.callPLProcedure("{call
				// PRUEBA_GENERARFICHERO.PREVISIONFACTURACION(?,?,?,?,?,?,?,?)}",
				// 2, param_in);
				String codretorno = resultado[0];
				if (!codretorno.equals("0")) {
					//RGG
					tx.rollback();
					
					/*
					request.setAttribute("mensaje",
						"facturacion.nuevaPrevisionFacturacion.mensaje.generacionFicheroERROR");
					
					tx.begin();
					String claves[] = new String[3];
					claves[0] = FacPrevisionFacturacionBean.C_IDINSTITUCION;
					claves[1] = FacPrevisionFacturacionBean.C_IDSERIEFACTURACION;
					claves[2] = FacPrevisionFacturacionBean.C_IDPREVISION;
					admPrev.deleteDirect(miHash, claves);
					tx.commit();
					*/
					
					throw new ClsExceptions(UtilidadesString.getMensajeIdioma(this.getLenguaje(request),"facturacion.nuevaPrevisionFacturacion.mensaje.generacionFicheroERROR")+ " - Codigo error:"+codretorno);

				} else {
					//RGG
					tx.commit();

					if (result)
						request
								.setAttribute("mensaje",
										"facturacion.nuevaPrevisionFacturacion.mensaje.insercionOK");
					else
						request.setAttribute("mensaje",
								"messages.inserted.error");
				}

				// Refrescamos la tabla con los valores insertados
				request.setAttribute("modal", "1");
				forward = "exito";
			} else {
				// Las fechas de inicio deben ser menores o iguales que las
				// fechas de fin.
				request.setAttribute("alertaFechasErroneas", "true");
				forward = "nuevaPrevisionFacturacion";
			}
		} catch (Exception e) {
			if (result) {
				// RGG hay que borrar la previsión porque ha fallado algo
				// pero la insercion se hizo bien.
				try {
				    tx.begin();
					admPrev.delete(miHash);
					tx.commit();
				} catch (Exception ex) {
					
				}
			}

			throwExcp(
					"messages.general.error",
					new String[] { "modulo.facturacion.previsionesFacturacion" },
					e, tx);
		}

		return forward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping,
	 *      com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de
	 * la base de datos. También borra el fichero de previsiones asociado.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute(
					"USRBEAN");
			String idInstitucion = user.getLocation();

			PrevisionesFacturacionForm form = (PrevisionesFacturacionForm) formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idSerieFacturacion = (String) vOcultos.elementAt(0);
			String idPrevision = (String) vOcultos.elementAt(1);

			FacFacturacionProgramadaAdm admFacProg = new FacFacturacionProgramadaAdm(
					this.getUserBean(request));
			String where = " Where ";
			where += FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
					+ FacFacturacionProgramadaBean.C_IDINSTITUCION + "="
					+ idInstitucion + " and "
					+ FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
					+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + "="
					+ idSerieFacturacion + " and "
					+ FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
					+ FacFacturacionProgramadaBean.C_IDPREVISION + "="
					+ idPrevision;
			Vector datosFacProg = admFacProg.select(where);

			if (datosFacProg == null || datosFacProg.size() == 0) {
				UserTransaction tx = null;

				Hashtable miHash = new Hashtable();
				miHash.put(FacPrevisionFacturacionBean.C_IDINSTITUCION, user
						.getLocation());
				miHash.put(FacPrevisionFacturacionBean.C_IDSERIEFACTURACION,
						(vOcultos.get(0)));
				miHash.put(FacPrevisionFacturacionBean.C_IDPREVISION, (vOcultos
						.get(1)));

				FacPrevisionFacturacionAdm admPrev = new FacPrevisionFacturacionAdm(
						this.getUserBean(request));
				tx = user.getTransaction();
				tx.begin();
				boolean result = admPrev.delete(miHash);

				// Se borra el fichero asociado a la previsión en el servidor
				// web.
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//				ReadProperties rp = new ReadProperties("SIGA.properties");
				String sRutaJava = rp
						.returnProperty("facturacion.directorioPrevisionesJava");
				String sRutaFisicaJava = rp
						.returnProperty("facturacion.directorioFisicoPrevisionesJava");
				sRutaJava = sRutaFisicaJava + File.separator + sRutaJava;
				String nombreFichero = rp
						.returnProperty("facturacion.prefijo.ficherosPrevisiones")
						+ idSerieFacturacion + "_" + idPrevision;
				;
				String sExtension = "."
						+ rp
								.returnProperty("facturacion.extension.ficherosPrevisiones");
				nombreFichero += sExtension;
				sRutaJava += File.separator + idInstitucion
						+ File.separator + nombreFichero;
				File fDirectorio = new File(sRutaJava);
				fDirectorio.delete();

				if (result) {
					request
							.setAttribute("mensaje",
									"facturacion.previsionesFacturacion.mensaje.borradoOK");
					tx.commit();
				} else {
					request.setAttribute("mensaje", "error.messages.deleted");
					tx.rollback();
				}
			} else {
				// No se puede borrar: La Prevision de Facturación seleccionada
				// está programada
				request
						.setAttribute("mensaje",
								"facturacion.previsionesFacturacion.literal.mensajePrevisionProgramada");
			}
		} catch (Exception e) {
			throwExcp(
					"messages.general.error",
					new String[] { "modulo.facturacion.previsionesFacturacion" },
					e, null);
		}

		// Refrescamos la tabla
		return "exito";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping,
	 *      com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping,
	 *      com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Se va a realizar la descarga del fichero (DOWNLOAD).
	 * 
	 * @param ActionMapping
	 *            mapping Mapeador de las acciones.
	 * @param MasterForm
	 *            formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest
	 *            request: información de entrada de la pagina original.
	 * @param HttpServletResponse
	 *            response: información de salida para la pagina destino.
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String download(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		String sNombreFichero = "";
		String sRutaJava = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute(
					"USRBEAN");
			PrevisionesFacturacionForm form = (PrevisionesFacturacionForm) formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = user.getLocation();
			String idSerieFacturacion = (String) vOcultos.elementAt(0);
			String idPrevision = (String) vOcultos.elementAt(1);

		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			sRutaJava = rp
					.returnProperty("facturacion.directorioPrevisionesJava");
			String sRutaFisicaJava = rp
					.returnProperty("facturacion.directorioFisicoPrevisionesJava");
			sRutaJava = sRutaFisicaJava + File.separator + sRutaJava;
			sNombreFichero = rp
					.returnProperty("facturacion.prefijo.ficherosPrevisiones")
					+ idSerieFacturacion + "_" + idPrevision;
			;
			String sExtension = "."
					+ rp
							.returnProperty("facturacion.extension.ficherosPrevisiones");
			sNombreFichero += sExtension;
			sRutaJava += File.separator + idInstitucion
					+ File.separator + sNombreFichero;

			//Control de que no exista el fichero a descargar:
			File tmp = new File(sRutaJava);
			if(tmp==null || !tmp.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			request.setAttribute("nombreFichero", sNombreFichero);
			request.setAttribute("rutaFichero", sRutaJava);
			
		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.facturacion.previsionesFacturacion" },
					e, null);
		}


		return "descargaFichero";
	}

	/**
	 * Se va a realizar la programación de la previsión.
	 * 
	 * @param ActionMapping
	 *            mapping Mapeador de las acciones.
	 * @param MasterForm
	 *            formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest
	 *            request: información de entrada de la pagina original.
	 * @param HttpServletResponse
	 *            response: información de salida para la pagina destino.
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */
	protected String programar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		UserTransaction tx = null;
		String salida = "";
		
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute(
					"USRBEAN");
			String idInstitucion = user.getLocation();

			PrevisionesFacturacionForm form = (PrevisionesFacturacionForm) formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idSerieFacturacion = (String) vOcultos.elementAt(0);
			String idPrevision = (String) vOcultos.elementAt(1);

			FacFacturacionProgramadaAdm admFacProg = new FacFacturacionProgramadaAdm(
					this.getUserBean(request));
			String where1 = " Where ";
			where1 += FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
					+ FacFacturacionProgramadaBean.C_IDINSTITUCION + "="
					+ idInstitucion + " and "
					+ FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
					+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + "="
					+ idSerieFacturacion + " and "
					+ FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
					+ FacFacturacionProgramadaBean.C_IDPREVISION + "="
					+ idPrevision;
			Vector datosPrev = admFacProg.select(where1);

			if (datosPrev == null || datosPrev.size() == 0) {
				// Insertamos en la tabla FAC_FACTURACIONPROGRAMADA
				String idLenguaje = user.getLanguage().toUpperCase();

				tx = (user).getTransaction();

				String where2 = " Where ";
				where2 += FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
						+ FacFacturacionProgramadaBean.C_IDINSTITUCION + "="
						+ idInstitucion + " And "
						+ FacFacturacionProgramadaBean.T_NOMBRETABLA + "."
						+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION
						+ "=" + idSerieFacturacion;
				Vector vec1 = admFacProg.selectTabla(where2);
				Hashtable hashMaximo1 = (Hashtable) vec1.get(0);
				Long idProgramacion = UtilidadesHash.getLong(hashMaximo1,
						FacFacturacionProgramadaBean.C_IDPROGRAMACION);

				String fechaInicioProductos = (String) vOcultos.elementAt(2);
				String fechaFinProductos = (String) vOcultos.elementAt(3);
				String fechaInicioServicios = (String) vOcultos.elementAt(4);
				String fechaFinServicios = (String) vOcultos.elementAt(5);
				String fechaRealGeneracion = null;
				String fechaConfirmacion = null;

				FacPrevisionFacturacionAdm admPrev = new FacPrevisionFacturacionAdm(
						this.getUserBean(request));
				String where3 = " Where ";
				where3 += FacPrevisionFacturacionBean.T_NOMBRETABLA + "." + FacPrevisionFacturacionBean.C_IDINSTITUCION + "=" + idInstitucion + 
				        " And " + FacPrevisionFacturacionBean.T_NOMBRETABLA + "." + FacPrevisionFacturacionBean.C_IDSERIEFACTURACION + "=" + idSerieFacturacion + 
						" and " + FacPrevisionFacturacionBean.T_NOMBRETABLA + "." + FacPrevisionFacturacionBean.C_IDPREVISION + "=" + idPrevision;
				Vector vec2 = admPrev.selectTabla_3(where3);
				Hashtable hashMaximo2 = (Hashtable) vec2.get(0);
				String descripcionPrevision = UtilidadesHash.getString(hashMaximo2,FacFacturacionProgramadaBean.C_DESCRIPCION);
				String fechaPrevistaGeneracion = UtilidadesHash.getString(hashMaximo2,FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION);
				
/*
				Hashtable miHash = new Hashtable();
				miHash.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,
						idInstitucion);
				miHash.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,
						idSerieFacturacion);
				miHash.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,
						idProgramacion);
				miHash.put(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaInicioProductos));
				miHash.put(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaFinProductos));
				miHash.put(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaInicioServicios));
				miHash.put(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS,
						GstDate.getApplicationFormatDate(idLenguaje,
								fechaFinServicios));
				miHash.put(FacFacturacionProgramadaBean.C_FECHAREALGENERACION,
						fechaRealGeneracion);
				miHash.put(FacFacturacionProgramadaBean.C_FECHACONFIRMACION,
						fechaConfirmacion);
				miHash.put(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION,
						"sysdate");
				miHash.put(
						FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION,
						fechaPrevistaGeneracion);
				miHash.put(FacFacturacionProgramadaBean.C_IDPREVISION,
						idPrevision);

*/
				
				// Obtengo la serie para obtener valores
				FacSerieFacturacionAdm adms = new FacSerieFacturacionAdm(this.getUserBean(request));
				Hashtable ht3 = new Hashtable();
				ht3.put(FacSerieFacturacionBean.C_IDINSTITUCION,idInstitucion);
				ht3.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION,idSerieFacturacion);
				Vector vsf = adms.selectByPK(ht3);
				FacSerieFacturacionBean b = null;
				if (vsf!=null && vsf.size()>0) {
				    b = (FacSerieFacturacionBean) vsf.get(0);
				}
				
				FacFacturacionProgramadaBean bean = new FacFacturacionProgramadaBean(); 	
				bean.setIdInstitucion(new Integer(idInstitucion));
				bean.setIdSerieFacturacion(new Long(idSerieFacturacion));
				bean.setIdProgramacion(idProgramacion);
				bean.setFechaInicioProductos(GstDate.getApplicationFormatDate(idLenguaje,
						fechaInicioProductos));
				bean.setFechaFinProductos(GstDate.getApplicationFormatDate(idLenguaje,
						fechaFinProductos));
				bean.setFechaInicioServicios(GstDate.getApplicationFormatDate(idLenguaje,
						fechaInicioServicios));
				bean.setFechaFinServicios(GstDate.getApplicationFormatDate(idLenguaje,
						fechaFinServicios));
				bean.setFechaRealGeneracion(fechaRealGeneracion);
				bean.setFechaConfirmacion(fechaConfirmacion);
				bean.setFechaPrevistaGeneracion(fechaPrevistaGeneracion);
				bean.setFechaProgramacion("sysdate");
				
				bean.setConfDeudor(b.getConfigDeudor());
				bean.setConfIngresos(b.getConfigIngresos());
				bean.setCtaClientes(b.getCuentaClientes());
				bean.setCtaIngresos(b.getCuentaIngresos());
				bean.setDescripcion(descripcionPrevision);
				
				bean.setVisible("S");
				
				bean.setIdPrevision(new Long(idPrevision));
				
				/** CR7 - Insercion de Fechas SEPA segun el algoritmo empleado en la ventana de Programacion **/
				String idInstitucionCGAE = String.valueOf(ClsConstants.INSTITUCION_CGAE);
				String fechaActual = GstDate.getHoyJsp(); // Obtengo la fecha actual
				String fechaPresentacion = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE, fechaActual, "1"); // Fecha actual + 1
				
				GenParametrosAdm admParametros = new GenParametrosAdm(user);
				String habilesUnicaCargos = admParametros.getValor(idInstitucion.toString(), "FAC", "DIAS_HABILES_UNICA_CARGOS", "7");
				String fechaUnicaCargos = EjecucionPLs.ejecutarSumarDiasHabiles(idInstitucionCGAE, fechaPresentacion, habilesUnicaCargos);
				
				bean.setFechaPresentacion		(GstDate.getApplicationFormatDate("en",fechaPresentacion));
				bean.setFechaCargoUnica			(GstDate.getApplicationFormatDate("en",fechaUnicaCargos));

				// se obtienen de la serie de facturacion
				FacSerieFacturacionAdm sfAdm = new FacSerieFacturacionAdm(this.getUserBean(request)); 
				Hashtable ht = new Hashtable();
				ht.put(FacSerieFacturacionBean.C_IDINSTITUCION,idInstitucion);
				ht.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION,idSerieFacturacion);
				Vector v = sfAdm.selectByPK(ht);
				if (v!=null && v.size()>0) {
					FacSerieFacturacionBean be = (FacSerieFacturacionBean) v.get(0);
					bean.setGenerarPDF(be.getGenerarPDF());			
					bean.setEnvio(be.getEnvioFactura());
					bean.setIdTipoPlantillaMail(be.getIdTipoPlantillaMail());
					bean.setIdTipoEnvios(be.getIdTipoEnvios());
				}
				
				bean.setFechaPrevistaConfirmacion(null);			
				bean.setArchivarFact("0");			
				bean.setLocked("0");
				
				// tratamiento de estados de la programacion 
				bean = admFacProg.tratamientoEstadosProgramacion(bean);
				
				// Comprobaciones antes de confirmacion 
				Vector ret = admFacProg.comprobarRecursosProgramacion(bean);
				
				// tratamiento del mensaje
				String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"facturacion.previsionesFacturacion.literal.mensajeProgramacionCorrecta"); 			
				if (ret.size()>0) {
					mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.facturacion.comprueba.avisos");
					for (int i=0;i<ret.size();i++) {
						mensaje += "\n" + UtilidadesString.getMensajeIdioma(this.getUserBean(request),(String)ret.get(i));
					}
				} 
				
				salida = exitoRefresco(mensaje,request);
				
				tx.begin();
				
				if (!admFacProg.insert(bean)) {
					throw new ClsExceptions("La programación ha dado errores: "+admFacProg.getError());
				}
				
				tx.commit();

			} else {
				// Ya estaba programada
				salida = exitoRefresco("facturacion.previsionesFacturacion.literal.mensajefacturacionProgramada",request);
			}
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] { "modulo.facturacion.previsionesFacturacion" },e, tx);
		}

		return salida;
	}
}