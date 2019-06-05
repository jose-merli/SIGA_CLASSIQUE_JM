package com.siga.gratuita.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.helper.StringHelper;
import org.redabogacia.sigaservices.app.services.scs.GestionEnvioInformacionEconomicaCatalunyaService;
import org.redabogacia.sigaservices.app.vo.scs.GestionEconomicaCatalunyaVo;
import org.redabogacia.sigaservices.app.vo.scs.JustificacionCatalunyaVo;

import com.atos.utils.UsrBean;
import com.ibm.icu.math.BigDecimal;
import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;

/***
 * 
 * @author jorgeta
 * @date 12/07/2018
 *
 *       La imaginación es más importante que el conocimiento
 *
 */
public class JustificacionCatalunyaForm extends MasterForm {
	
	
	
	String codActuacion;
	String fechaActuacion;
	String abogado;
	String turno;
	String ejg;
	String guardia;
	String oficio;
	String fechaJustificacion;
	String modulo;
	String importe;
	String justiciable;
	String error;

	

	
	
	public final String getError() {
		return error;
	}
	public final void setError(String error) {
		this.error = error;
	}
	public final String getCodActuacion() {
		return codActuacion;
	}
	public final void setCodActuacion(String codActuacion) {
		this.codActuacion = codActuacion;
	}
	public final String getFechaActuacion() {
		return fechaActuacion;
	}
	public final void setFechaActuacion(String fechaActuacion) {
		this.fechaActuacion = fechaActuacion;
	}
	public final String getAbogado() {
		return abogado;
	}
	public final void setAbogado(String abogado) {
		this.abogado = abogado;
	}
	public final String getEjg() {
		return ejg;
	}
	public final void setEjg(String ejg) {
		this.ejg = ejg;
	}
	public final String getGuardia() {
		return guardia;
	}
	public final void setGuardia(String guardia) {
		this.guardia = guardia;
	}
	
	public final String getTurno() {
		return turno;
	}
	public final void setTurno(String turno) {
		this.turno = turno;
	}
	
	public final String getOficio() {
		return oficio;
	}
	public final void setOficio(String oficio) {
		this.oficio = oficio;
	}
	public final String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public final void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public final String getModulo() {
		return modulo;
	}
	public final void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public final String getImporte() {
		return importe;
	}
	public final void setImporte(String importe) {
		this.importe = importe;
	}
	
	public final String getJusticiable() {
		return justiciable;
	}
	public final void setJusticiable(String justiciable) {
		this.justiciable = justiciable;
	}
	
	public List<JustificacionCatalunyaForm> getVo2FormList(
			List<JustificacionCatalunyaVo> voList) {
		List<JustificacionCatalunyaForm> list = new ArrayList<JustificacionCatalunyaForm>();
		JustificacionCatalunyaForm gestionEconomicaForm   = null;
		for (JustificacionCatalunyaVo objectVo : voList) {
			gestionEconomicaForm = getVo2Form(objectVo);
			list.add(gestionEconomicaForm);
			
		}
		return list;
	}
	public JustificacionCatalunyaForm getVo2Form(JustificacionCatalunyaVo objectVo) {
		return getVo2Form(objectVo,new JustificacionCatalunyaForm());
	}
	public JustificacionCatalunyaForm getVo2Form(JustificacionCatalunyaVo objectVo, JustificacionCatalunyaForm objectForm) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(objectVo.getCodActuacion()!=null)
			objectForm.setCodActuacion(objectVo.getCodActuacion());
		if(objectVo.getFechaActuacion()!=null)
			objectForm.setFechaActuacion(sdf.format(objectVo.getFechaActuacion()));
		if(objectVo.getFechaJustificacionActuacion()!=null)
			objectForm.setFechaJustificacion(sdf.format(objectVo.getFechaJustificacionActuacion()));
		
		
		
		if(objectVo.getDesigna()!=null) {
			if(objectVo.getDesigna().getNombreAbogado()!=null) {
	//		datos del abogado designado
				StringBuilder abogado = new StringBuilder();
				if(objectVo.getDesigna().getNumColegiadoAbogado()!=null) {
					abogado.append(objectVo.getDesigna().getNumColegiadoAbogado());
					abogado.append(" ");
				}
				abogado.append(objectVo.getDesigna().getNombreAbogado());
				abogado.append(" ");
				abogado.append(objectVo.getDesigna().getPrimerApellidoAbogado());
				abogado.append(" ");
				
				if(objectVo.getDesigna().getSegundoApellidoAbogado()!=null) 
					abogado.append(objectVo.getDesigna().getSegundoApellidoAbogado());
				objectForm.setAbogado(abogado.toString());
				
				if(objectVo.getDesigna().getTurno()!=null) {
					StringBuilder turno = new StringBuilder();
					turno.append(objectVo.getDesigna().getTurno());
					if(objectVo.getDesigna().getAreaPartidoJudicial()!=null) {
						turno.append(" ");
						turno.append(objectVo.getDesigna().getAreaPartidoJudicial());
						
					} 
					objectForm.setTurno(turno.toString());
					
				}
				
				
	//			O --> Oficio A-->datos de la guardia
				if(objectVo.getDesigna().getTipoIniciacion()!=null && objectVo.getDesigna().getTipoIniciacion().equals("O")) {
					StringBuilder oficio = new StringBuilder();
					oficio.append("Oficio ");
					oficio.append(objectVo.getDesigna().getNumDesignacionAbogado());
					if(objectVo.getDesigna().getFechaDesignacionAbogado() != null)
					oficio.append(" ");
					oficio.append(sdf.format(objectVo.getDesigna().getFechaDesignacionAbogado()));
					objectForm.setOficio(oficio.toString());
				}else {
					objectForm.setGuardia(abogado.toString());
					StringBuilder guardia = new StringBuilder();
					guardia.append("Guardia");
					if(objectVo.getFestivo()!=null && objectVo.getFestivo().equals("1"))
						guardia.append("Fest ");
					if(objectVo.getFueraPlazo()!=null && objectVo.getFueraPlazo().equals("1"))
						guardia.append("Fora");
					objectForm.setGuardia(guardia.toString());
				}
				
			}
		}
		if(objectVo.getCodigoExpediente()!=null) {
			StringBuilder expediente = new StringBuilder();
			expediente.append(objectVo.getCodigoExpediente().getAnioExpediente());
			expediente.append("/");
			expediente.append(objectVo.getCodigoExpediente().getNumExpediente());
			objectForm.setEjg(expediente.toString());
		}
		
		
			
		if(objectVo.getModulo()!=null)
			objectForm.setModulo(objectVo.getModulo());
		if(objectVo.getImporte()!=null)
			objectForm.setImporte(objectVo.getImporte().toString());
		
		if(objectVo.getJusticiable()!=null) {
			//		datos del abogado designado
			StringBuilder justiciable = new StringBuilder();
			if(objectVo.getJusticiable().getIdentificacion()!=null) {
				justiciable.append(objectVo.getJusticiable().getIdentificacion());
				justiciable.append(" ");
			}
			justiciable.append(objectVo.getJusticiable().getNombreJusticiable());
			justiciable.append(" ");
			justiciable.append(objectVo.getJusticiable().getPrimerApellidoJusticiable());
			justiciable.append(" ");
			
			if(objectVo.getJusticiable().getSegundoApellidoJusticiable()!=null) 
				justiciable.append(objectVo.getJusticiable().getSegundoApellidoJusticiable());
			objectForm.setJusticiable(justiciable.toString());
		}
		
		if(objectVo.getError()!=null)
			objectForm.setError(objectVo.getError());
		return objectForm;
	}

	
	


}