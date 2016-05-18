package com.siga.censo.ws.action;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_TIPO_ENVIO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenExColumn;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenExPerfil;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvio;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsPagina;
import org.redabogacia.sigaservices.app.services.cen.CenInstitucionService;
import org.redabogacia.sigaservices.app.services.cen.EcomCenWsEnvioService;
import org.redabogacia.sigaservices.app.services.cen.ws.EcomCenColegiadoService;
import org.redabogacia.sigaservices.app.vo.ColegioPerfilColumnaVO;
import org.redabogacia.sigaservices.app.vo.ColumnaPerfilVO;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.ParametrosGeneralesForm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenParametrosBean;
import com.siga.censo.ws.form.ConfiguracionPerfilColegioForm;
import com.siga.censo.ws.form.NuevaRemesaForm;
import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class ConfiguracionPerfilColegioAction extends MasterAction {
	
	public ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {

			miForm = (MasterForm) formulario;
			if (miForm != null) {
				return super.executeInternal(mapping, formulario, request,
						response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.censo" });
		}
	}
	
	protected String abrir (ActionMapping mapping,MasterForm formulario,HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException{
			ConfiguracionPerfilColegioForm form = (ConfiguracionPerfilColegioForm) formulario;	
		try {
			EcomCenColegiadoService service =  (EcomCenColegiadoService) getBusinessManager().getService(EcomCenColegiadoService.class);
			List<ColumnaPerfilVO> columnas=service.getAllColumnas();
			List<InstitucionVO> listInstitucionVOs =service.getColegiosWithPerfil();
			
			form.setInstituciones(listInstitucionVOs);
			form.setTiposColumna(columnas);
				
			} catch (Exception e) {
				throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
			}
		return "inicio";
	}
	
	protected String buscar(ActionMapping mapping,MasterForm formulario,HttpServletRequest request,
			HttpServletResponse response){
		ConfiguracionPerfilColegioForm form = (ConfiguracionPerfilColegioForm) formulario;	
		String idInsti = 	form.getIdColegio();
		Short idinstitucion = Short.valueOf(idInsti);
		List<ColegioPerfilColumnaVO> listadoPerfil = new ArrayList<ColegioPerfilColumnaVO>();
		EcomCenColegiadoService service =  (EcomCenColegiadoService) getBusinessManager().getService(EcomCenColegiadoService.class);
		listadoPerfil=service.getPerfilColegioByIdInstitucion(idinstitucion);
		form.setListadoPerfil(listadoPerfil);
		request.getSession().setAttribute("DATA", listadoPerfil);
		return "resultado";

	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		EcomCenColegiadoService service =  (EcomCenColegiadoService) getBusinessManager().getService(EcomCenColegiadoService.class);
		ConfiguracionPerfilColegioForm form = (ConfiguracionPerfilColegioForm) formulario;
		String idperfil = null;
		Vector ocultos = form.getDatosTablaOcultos(0);
		if (ocultos != null) {
			if (ocultos.size()>= 1) {					
				idperfil = ocultos.get(0).toString();
			}
		} else {
			throw new IllegalArgumentException("No se ha recibido el identificador para editar el colegiado");
		}
		service.deleteColumnaPerfilColegio(idperfil);
		return this.exitoRefresco("messages.deleted.success", request);
	}

	protected String modificar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		ConfiguracionPerfilColegioForm form = (ConfiguracionPerfilColegioForm) formulario;
		String idperfil = form.getIdPerfil();
		String nombreCol=form.getNombreColumna();
		EcomCenColegiadoService service =  (EcomCenColegiadoService) getBusinessManager().getService(EcomCenColegiadoService.class);
		
		service.editaColumnaPerfilColegio(nombreCol, idperfil);
		return this.exitoRefresco("messages.updated.success", request);
	}
	

	
}
