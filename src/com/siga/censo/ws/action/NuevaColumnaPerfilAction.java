package com.siga.censo.ws.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.services.cen.ws.EcomCenColegiadoService;

import com.siga.censo.ws.form.NuevaColumnaPerfilForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class NuevaColumnaPerfilAction extends MasterAction{
	
	protected String insertar(ActionMapping mapping, MasterForm masterForm, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		Short idinstitucion=null;
		String nombreCol=null;
		Short idcol =null;
		NuevaColumnaPerfilForm form = (NuevaColumnaPerfilForm) masterForm;	
		try {
			EcomCenColegiadoService servicio =  (EcomCenColegiadoService) getBusinessManager().getService(EcomCenColegiadoService.class);
			
			nombreCol=form.getNombreColumna();
			
			if(form.getIdColegioInsertar()!=null){
				idinstitucion=Short.valueOf(form.getIdColegioInsertar());
			}
			if(form.getTipoColumna()!=null){
				idcol=Short.valueOf(form.getTipoColumna());
			}
			
			servicio.addColumnaPerfilColegio(idinstitucion, nombreCol, idcol);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		
		return exitoRefresco("messages.inserted.success.nuevoFicheroExcel", request);
	}
	

}
