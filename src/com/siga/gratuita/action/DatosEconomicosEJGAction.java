package com.siga.gratuita.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.general.MasterAction;
import com.siga.general.SIGAException;
import com.siga.gratuita.adm.DatosEconomicosEJGAdm;
import com.siga.gratuita.form.DatosEconomicosEJGForm;

public class DatosEconomicosEJGAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";

		try { 
			do {
				DatosEconomicosEJGForm formDatosEconomicosEJG = (DatosEconomicosEJGForm) formulario;
				
				if (formDatosEconomicosEJG != null) {
					String accion = formDatosEconomicosEJG.getModo();
					DatosEconomicosEJGAdm adm = new DatosEconomicosEJGAdm();

					if (accion == null || accion.equalsIgnoreCase("")){
						mapDestino = adm.verDatosEconomicos(mapping, formDatosEconomicosEJG, request, response);
						break;

					} else if (accion.equalsIgnoreCase("ingresos")){
						mapDestino = adm.listarDatosEconomicosIngresos(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("borrarIngreso")){
						mapDestino = adm.borrarDatosEconomicosIngresos(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("guardarIngresos")){
						mapDestino = adm.guardarDatosEconomicosIngresos(mapping, formDatosEconomicosEJG, request, response);
						break;						

					} else if (accion.equalsIgnoreCase("bieninmueble")){
						mapDestino = adm.listarDatosEconomicosBienInmueble(mapping, formDatosEconomicosEJG, request, response);
						break;

					} else if (accion.equalsIgnoreCase("bienmueble")){
						mapDestino = adm.listarDatosEconomicosBienMueble(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("borrarMueble")){
						mapDestino = adm.borrarDatosEconomicosBienMueble(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("guardarMueble")){
						mapDestino = adm.guardarDatosEconomicosBienMueble(mapping, formDatosEconomicosEJG, request, response);
						break;							

					} else if (accion.equalsIgnoreCase("cargaeconomica")){
						mapDestino = adm.listarDatosEconomicosCargaEconomica(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("borrarCargaEconomica")){
						mapDestino = adm.borrarDatosEconomicosCargaEconomica(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("guardarCargaEconomica")){
						mapDestino = adm.guardarDatosEconomicosCargaEconomica(mapping, formDatosEconomicosEJG, request, response);
						break;						

					} else if (accion.equalsIgnoreCase("irpf20")){
						mapDestino = adm.listarDatosEconomicosIrpf20(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("borrarIrpf20")){
						mapDestino = adm.borrarDatosEconomicosIrpf20(mapping, formDatosEconomicosEJG, request, response);
						break;
						
					} else if (accion.equalsIgnoreCase("guardarIrpf20")){
						mapDestino = adm.guardarDatosEconomicosIrpf20(mapping, formDatosEconomicosEJG, request, response);
						break;						

					} else {
						return super.executeInternal(mapping,formDatosEconomicosEJG,request,response);
					}
				}
			} while (false);

			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			
			return mapping.findForward(mapDestino);
			
		} catch (SIGAException exc) {
			throw exc;

		} catch (Exception exc) {
			throw new SIGAException("messages.general.error", exc, new String[] {"modulo.gratuita"});
		}
	}		
}