package com.siga.certificados.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Certificado;
import com.siga.certificados.form.SIGACerDetalleSolicitudForm;
import com.siga.certificados.form.SIGACerSolicitudOriginalForm;
import com.siga.facturacion.form.ConfirmarFacturacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGACerSolicitudOriginalAction extends MasterAction {

	public static Hashtable<String, Integer> contadores = new Hashtable<String, Integer>();

	public ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;

			if (miForm != null) {
				String accion = miForm.getModo();

				if (accion.equalsIgnoreCase("ver")) {
					mapDestino = editar(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("descargar")) {
					mapDestino = descargar(mapping, miForm, request, response);

				} else {
					return super.executeInternal(mapping, formulario, request, response);
				}
			}

			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.certificados" });
		}

	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UsrBean userBean = this.getUserBean(request);
		
		try {
			MasterForm miForm = (MasterForm) formulario;
			String accion = miForm.getModo();
			request.setAttribute("modo", accion);

			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
			FacFacturaAdm admFactura = new FacFacturaAdm(userBean);
			String idInstitucion = "", idSolicitud = "", idEstadoSolicitud = "", tipoCertificado = "";
			
			idInstitucion = request.getParameter("idInstitucion");
			request.setAttribute("idInstitucion", idInstitucion);
			idSolicitud = request.getParameter("idSolicitud");
			request.setAttribute("idSolicitud", idSolicitud);
			idEstadoSolicitud = request.getParameter("idEstadoSolicitud");
			request.setAttribute("idEstadoSolicitud", idEstadoSolicitud);
			tipoCertificado = request.getParameter("tipoCertificado");
			request.setAttribute("tipoCertificado", tipoCertificado);

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(userBean);
			String idInstitucionOrigen = "" + beanSolicitud.getIdInstitucionOrigen();
			String idInstitucionDestino = "" + beanSolicitud.getIdInstitucionDestino();
			String idInstitucionColegiacion = "" + beanSolicitud.getIdInstitucionColegiacion();
			CenInstitucionBean beanInstitucionOrigen = null;
			Hashtable<String, Object> htInstitucion = new Hashtable<String, Object>();
			if (!idInstitucionOrigen.equalsIgnoreCase("null")) {
				htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionOrigen);
				vDatos = admInstitucion.selectByPK(htInstitucion);
				if (vDatos != null && vDatos.size() == 1) {
					beanInstitucionOrigen = (CenInstitucionBean) vDatos.elementAt(0);
				}
			}

			CenInstitucionBean beanInstitucionColegiacion = null;
			if (!idInstitucionColegiacion.equalsIgnoreCase("null")) {
				htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionColegiacion);
				vDatos = admInstitucion.selectByPK(htInstitucion);
				if (vDatos != null && vDatos.size() == 1) {
					beanInstitucionColegiacion = (CenInstitucionBean) vDatos.elementAt(0);
				}
			}

			CenInstitucionBean beanInstitucionDestino = null;
			if (!idInstitucionDestino.equalsIgnoreCase("null")) {
				htInstitucion.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionDestino);
				vDatos = admInstitucion.selectByPK(htInstitucion);

				if (vDatos != null && vDatos.size() == 1) {
					beanInstitucionDestino = (CenInstitucionBean) vDatos.elementAt(0);
				}
			}
			request.setAttribute("solicitud", beanSolicitud);
			request.setAttribute("institucionOrigen", beanInstitucionOrigen);
			request.setAttribute("institucionDestino", beanInstitucionDestino);
			request.setAttribute("institucionColegiacion", beanInstitucionColegiacion);
			request.setAttribute("documentoDescarga", beanSolicitud.getFicheroDocumento());
			
			// obteniendo el valor del parametro de control de facturas a la JSP para que actue en consecuencia
			GenParametrosAdm paramAdm = new GenParametrosAdm(userBean);
			String controlFacturasSII = paramAdm.getValor(idInstitucion, "FAC", "CONTROL_EMISION_FACTURAS_SII", "0");
			request.setAttribute("controlFacturasSII", controlFacturasSII);
			
			// ahora hay que buscar si se han hecho facturaciones que incluyan el dia de hoy y 
			// que petenezcan a una serie que incluya el tipo de producto de este certificado que se esta editando
			{
				ConfirmarFacturacionForm formFacturacion = new ConfirmarFacturacionForm();
				formFacturacion.setFechaDesdeProductos(UtilidadesFecha.getToday(UtilidadesFecha.FORMATO_FECHA_ES));
				formFacturacion.setFechaHastaProductos(UtilidadesFecha.getToday(UtilidadesFecha.FORMATO_FECHA_ES));
				formFacturacion.setIdTipoProducto(beanSolicitud.getPpn_IdTipoProducto().toString());
				formFacturacion.setIdProducto(beanSolicitud.getPpn_IdProducto().toString());
				
				FacFacturacionProgramadaAdm	facturacionProgramadaAdm = new FacFacturacionProgramadaAdm(userBean);
				Paginador facturacionesEncontradas = facturacionProgramadaAdm.getProgramacioneFacturacionPaginador(formFacturacion);
				boolean hayFacturacionHoy = (facturacionesEncontradas != null && facturacionesEncontradas.getNumeroTotalRegistros() > 0);
				request.setAttribute("hayFacturacionHoy", hayFacturacionHoy ? "1" : "0");
			}
			
			//Comprobamos si el certificado tiene factura o no, esto lo realizamos para mostrar en la edicción el botón anular.
			Vector<Hashtable<String, Object>> vFacturas = admFactura.obtenerFacturasFacturacionRapida(String.valueOf(beanSolicitud.getIdInstitucion()), null,
					String.valueOf(beanSolicitud.getIdSolicitud()));
			if (vFacturas == null || vFacturas.size() == 0) { // No esta facturado
				request.setAttribute("facturado", "0");
			}else{
				request.setAttribute("facturado", "1");
			}
			
			// Tratamiento para las solicitudes de certificados de forma telemática
			boolean pintarCamposTelematica = false;
			if (beanSolicitud.getMetodoSolicitud().equals("5")) {
				pintarCamposTelematica = true;
			}

			request.setAttribute("pintarCamposTelematica", pintarCamposTelematica);
			
			// Datos Interesado para comparar
			String numSolicitudCol = "", nombreInteresado = "", apellido1Interesado = "", apellido2Interesado = "", nidInteresado = "";
			String fechaNacInteresado = "", telInteresado = "", movilInteresado = "", faxInteresado = "", emailInteresado = "";
			String paisInteresado = "", domicilioInteresado = "", cpostalInteresado = "", provInteresado = "", poblInteresado = "";
			String estadoInc = "", residenteInc = "", anioLicenciatura = "";
			
			String sql = "SELECT cens.nombre, cens.apellido1, cens.apellido2, cens.numdocumento as NIFCIF, ";
			sql = sql + " TO_CHAR(cens.fechanacimiento,'DD/MM/YYYY') as FECHANACIMIENTO, dir.telefono1 as TELEFONO1, dir.movil as MOVIL, dir.fax1 as FAX1, ";
			sql = sql + " dir.correoelectronico as CORREOELECTRONICO, dir.domicilio as DOMICILIO, dir.codigopostal as CODIGOPOSTAL, dir.idpais as IDPAIS, ";
			sql = sql + " (select nombre from cen_provincias where idprovincia = dir.idprovincia) as provincia, ";
			sql = sql + " (select nombre from cen_poblaciones where idpoblacion = dir.idpoblacion) as poblacion, ";
			sql = sql + " cens.numsolicitudcolegiacion AS NUMSOLICITUDCOLEGIACION, ";
			sql = sql + " NVL(cens.idecomcensosituacionejer,10) AS SITUACIONEJERCICIO, ";
			sql = sql + " NVL(cens.residente,0) AS SITUACIONRESIDENTE, ";
			sql = sql + " cens.aniolicenciatura as ANIOLICENCIATURA ";
			sql = sql + " FROM cer_solicitudcertificados cer join cen_persona CD ON cer.idpersona_des = cd.idpersona ";
			sql = sql + " join CEN_DIRECCIONES DIR ON cer.iddireccion_dir = dir.iddireccion and cer.idpersona_dir = dir.idpersona ";
			sql = sql + " and cer.idinstitucion = dir.idinstitucion left outer join CEN_COLEGIADO COL ON CD.IDPERSONA = COL.IDPERSONA ";
			sql = sql + " left outer join ECOM_CEN_DATOS cens on cer.idcensodatos = cens.idcensodatos WHERE cer.idsolicitud = " + idSolicitud;
			
			CenColegiadoAdm cenColegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			RowsContainer rowsContainer = cenColegiadoAdm.find(sql);
			if (rowsContainer != null && rowsContainer.size()>0) {
				Vector vector = rowsContainer.getAll();
				if (vector != null && vector.size() > 0) {
					Row row = (Row) vector.get(0);
					numSolicitudCol = row.getString("NUMSOLICITUDCOLEGIACION");
					nombreInteresado = row.getString("NOMBRE"); 
					apellido1Interesado = row.getString("APELLIDO1");
					apellido2Interesado = row.getString("APELLIDO2");
					nidInteresado = row.getString("NIFCIF");
					fechaNacInteresado = row.getString("FECHANACIMIENTO");
					telInteresado = row.getString("TELEFONO1");
					movilInteresado = row.getString("MOVIL");
					faxInteresado = row.getString("FAX1");
					emailInteresado = row.getString("CORREOELECTRONICO");
					paisInteresado = row.getString("IDPAIS");
					domicilioInteresado = row.getString("DOMICILIO");
					cpostalInteresado = row.getString("CODIGOPOSTAL");
					provInteresado = row.getString("PROVINCIA");
					poblInteresado = row.getString("POBLACION");
					estadoInc = row.getString("SITUACIONEJERCICIO");
					residenteInc = row.getString("SITUACIONRESIDENTE");
					anioLicenciatura = row.getString("ANIOLICENCIATURA");
				}
			}
			
			String numSolicitudColOld = "", nombreInteresadoOld = "", apellido1InteresadoOld = "", apellido2InteresadoOld = "", nidInteresadoOld = "";
			String fechaNacInteresadoOld = "", telInteresadoOld = "", movilInteresadoOld = "", faxInteresadoOld = "", emailInteresadoOld = "";
			String paisInteresadoOld = "", domicilioInteresadoOld = "", cpostalInteresadoOld = "", provInteresadoOld = "", poblInteresadoOld = "";
			String estadoIncOld = "", residenteIncOld = "";
			
			sql = "SELECT CD.NOMBRE as NOMBRE, CD.APELLIDOS1 as APELLIDO1, CD.APELLIDOS2 as APELLIDO2, CD.Nifcif as NUMDOCUMENTO, ";
			sql = sql + "TO_CHAR(CD.FECHANACIMIENTO,'DD/MM/YYYY') as FECHANACIMIENTO, dir.telefono1 as TELEFONO, dir.movil as TELEFONOMOVIL, dir.fax1 as FAX, ";
			sql = sql + "dir.correoelectronico as EMAIL, dir.domicilio as DOMICILIO, dir.codigopostal as CODIGOPOSTAL, dir.idpais as CODIGOPAISEXTRANJ, ";
			sql = sql + "(select nombre from cen_provincias where idprovincia = dir.idprovincia) as DESCRIPCIONPROVINCIA, ";
			sql = sql + "(select nombre from cen_poblaciones where idpoblacion = dir.idpoblacion) as DESCRIPCIONPOBLACION, ";
			sql = sql + "col.numsolicitudcolegiacion AS NUMSOLICITUDCOLEGIACION, ";
			sql = sql + "NVL(col.situacionejercicio,10) AS SITUACION, ";
			sql = sql + "NVL(col.situacionresidente,0) AS RESIDENTE ";
			sql = sql + "FROM cer_solicitudcertificados cer join cen_persona CD ON cer.idpersona_des = cd.idpersona ";
			sql = sql + "join cen_direcciones DIR ON cer.idpersona_dir = dir.idpersona  and dir.idinstitucion = 2000 ";
			sql = sql + "left outer join CEN_COLEGIADO COL ON CD.IDPERSONA = COL.IDPERSONA ";
			sql = sql + "WHERE dir.iddireccion = (select max(indir.iddireccion) from cen_direcciones indir ";
			sql = sql + "where indir.idinstitucion = 2000 and indir.idpersona = cer.idpersona_dir and indir.iddireccion < cer.iddireccion_dir) ";
			sql = sql + "AND cer.idsolicitud = " +idSolicitud;

			rowsContainer = cenColegiadoAdm.find(sql);
			if (rowsContainer != null && rowsContainer.size()>0) {
				Vector vector = rowsContainer.getAll();
				if (vector != null && vector.size() > 0) {
					Row row = (Row) vector.get(0);
					numSolicitudColOld = row.getString("NUMSOLICITUDCOLEGIACION");
					nombreInteresadoOld = row.getString("NOMBRE"); 
					apellido1InteresadoOld = row.getString("APELLIDO1");
					apellido2InteresadoOld = row.getString("APELLIDO2");
					nidInteresadoOld = row.getString("NUMDOCUMENTO");
					fechaNacInteresadoOld = row.getString("FECHANACIMIENTO");
					telInteresadoOld = row.getString("TELEFONO");
					movilInteresadoOld = row.getString("TELEFONOMOVIL");
					faxInteresadoOld = row.getString("FAX");
					emailInteresadoOld = row.getString("EMAIL");
					paisInteresadoOld = row.getString("CODIGOPAISEXTRANJ");
					domicilioInteresadoOld = row.getString("DOMICILIO");
					cpostalInteresadoOld = row.getString("CODIGOPOSTAL");
					provInteresadoOld = row.getString("DESCRIPCIONPROVINCIA");
					poblInteresadoOld = row.getString("DESCRIPCIONPOBLACION");
					estadoIncOld = row.getString("SITUACION");
					residenteIncOld = row.getString("RESIDENTE");
				}
			}
			
			request.setAttribute("numSolicitudCol",numSolicitudCol);
			request.setAttribute("nombreInteresado",nombreInteresado);
			request.setAttribute("apellido1Interesado",apellido1Interesado);
			request.setAttribute("apellido2Interesado",apellido2Interesado);
			request.setAttribute("nidInteresado",nidInteresado);
			request.setAttribute("fechaNacInteresado",fechaNacInteresado);
			request.setAttribute("telInteresado",telInteresado);
			request.setAttribute("movilInteresado",movilInteresado);
			request.setAttribute("faxInteresado",faxInteresado);
			request.setAttribute("emailInteresado",emailInteresado);
			request.setAttribute("paisInteresado",paisInteresado);
			request.setAttribute("domicilioInteresado",domicilioInteresado);
			request.setAttribute("cpostalInteresado",cpostalInteresado);
			request.setAttribute("provInteresado",provInteresado);
			request.setAttribute("poblInteresado",poblInteresado);
			request.setAttribute("residenteInc", residenteInc);
			request.setAttribute("estadoInc", estadoInc);
			request.setAttribute("anioLicenciatura", anioLicenciatura);
			request.setAttribute("numSolicitudColOld",numSolicitudColOld);
			request.setAttribute("nombreInteresadoOld",nombreInteresadoOld);
			request.setAttribute("apellido1InteresadoOld",apellido1InteresadoOld);
			request.setAttribute("apellido2InteresadoOld",apellido2InteresadoOld);
			request.setAttribute("nidInteresadoOld",nidInteresadoOld);
			request.setAttribute("fechaNacInteresadoOld",fechaNacInteresadoOld);
			request.setAttribute("telInteresadoOld",telInteresadoOld);
			request.setAttribute("movilInteresadoOld",movilInteresadoOld);
			request.setAttribute("faxInteresadoOld",faxInteresadoOld);
			request.setAttribute("emailInteresadoOld",emailInteresadoOld);
			request.setAttribute("paisInteresadoOld",paisInteresadoOld);
			request.setAttribute("domicilioInteresadoOld",domicilioInteresadoOld);
			request.setAttribute("cpostalInteresadoOld",cpostalInteresadoOld);
			request.setAttribute("provInteresadoOld",provInteresadoOld);
			request.setAttribute("poblInteresadoOld",poblInteresadoOld);
			request.setAttribute("residenteIncOld", residenteIncOld);
			request.setAttribute("estadoIncOld", estadoIncOld);

			//Fin Datos Interesado para comparar
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, null);
		}
		return "mostrar";
	}
	
	protected String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction tx = null;
		try {
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			SIGACerSolicitudOriginalForm form = (SIGACerSolicitudOriginalForm) formulario;
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = "";
			String idSolicitud = "";

			if (vOcultos != null) {
				/** Icono listado de solicitudes **/
				idInstitucion = ((String) vOcultos.elementAt(0)).trim();
				idSolicitud = ((String) vOcultos.elementAt(1)).trim();

			} else {
				/** Nueva ventana detalle certificado **/
				idInstitucion = form.getIdInstitucion();
				idSolicitud = form.getIdSolicitud();
			}

			Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
			htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

			Vector vDatos = admSolicitud.selectByPK(htSolicitud);

			CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			String pathFichero = solicitudCertificadoBean.getFicheroDocumento();
			File fDocumento = new File(pathFichero);

			String nombrefichero = pathFichero.substring(pathFichero.lastIndexOf("/")+1);
			request.setAttribute("nombreFichero", nombrefichero);
			String path = UtilidadesString.replaceAllIgnoreCase(fDocumento.getPath(), "\\", "/");
			request.setAttribute("rutaFichero", path);
			request.setAttribute("borrarFichero", "false");

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.certificados" }, e, tx);
		}

		if (request.getParameter("descargarCertificado") != null && !"".equals(request.getParameter("descargarCertificado"))
				&& request.getParameter("descargarCertificado").equalsIgnoreCase("1")) {
			return "exitoDescarga";
		} else {
			return "descargaFichero";
		}

	}
	
	public static void generarZip(UsrBean usr, String idInstitucion, String idSolicitud) throws ClsExceptions, SIGAException {

		CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(usr);

		Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
		htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
		htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

		Vector vDatos = admSolicitud.selectByPK(htSolicitud);

		CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) vDatos.elementAt(0);

		ArrayList<File> fCertificado = admSolicitud.recuperarVariosCertificado(solicitudCertificadoBean);

		// Genera un array con los ficheros y ruta de su carpeta
		ArrayList<Hashtable<String, Object>> arrayDatosCertificados = new ArrayList<Hashtable<String, Object>>();
		Hashtable<String, Object> hDatosCertificados = new Hashtable<String, Object>();
		for (int i = 0; i < fCertificado.size(); i++) {

			hDatosCertificados.put("fichero", fCertificado.get(i));
			hDatosCertificados.put("rutaCarpeta", fCertificado.get(i).getPath());
			arrayDatosCertificados.add(hDatosCertificados);
			hDatosCertificados = new Hashtable<String, Object>();
		}

		// Generamos el ZIP
		File ficheroZip = null;
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		// Obtenemos las rutas del zip
		String rutaAlmacen = admSolicitud.getRutaCertificadoDirectorioBD(solicitudCertificadoBean.getIdInstitucion());
		rutaAlmacen = admSolicitud.getRutaCertificadoDirectorio(solicitudCertificadoBean, rutaAlmacen);

		String rutaFicheroZip = rutaAlmacen + File.separator + solicitudCertificadoBean.getIdSolicitud() + ".zip";
		ficheroZip = new File(rutaFicheroZip);

		// Generamos el fichero zip con todas los certificados asociadas a la
		// solicitud
		Certificado certificado = new Certificado();
		certificado.doZip(rutaAlmacen + File.separator, String.valueOf(solicitudCertificadoBean.getIdSolicitud()), fCertificado, solicitudCertificadoBean, admSolicitud);

	}
	
}