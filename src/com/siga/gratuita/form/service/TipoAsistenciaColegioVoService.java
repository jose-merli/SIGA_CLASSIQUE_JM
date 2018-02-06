package com.siga.gratuita.form.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.redabogacia.sigaservices.app.autogen.model.ScsTipoasistenciacolegio;
import org.redabogacia.sigaservices.app.vo.scs.TipoAsistenciaColegioVo;

import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.VoUiService;
import com.siga.gratuita.form.TipoAsistenciaColegioForm;
/**
 * 
 * @author jorgeta
 * La imaginación es mas importante que el conocimiento
 * @date 04/01/2018
 *
 */
public class TipoAsistenciaColegioVoService implements VoUiService<TipoAsistenciaColegioForm , TipoAsistenciaColegioVo> {

	

	public List<TipoAsistenciaColegioForm> getVo2FormList(
			List<TipoAsistenciaColegioVo> voList) {
		List<TipoAsistenciaColegioForm> objectForms = new ArrayList<TipoAsistenciaColegioForm>();
		TipoAsistenciaColegioForm  objectForm = null;
		for (TipoAsistenciaColegioVo asistenciaVo : voList) {
			objectForm = getVo2Form(asistenciaVo);
			objectForms.add(objectForm);
			
		}
		return objectForms;
	}

	@Override
	public TipoAsistenciaColegioVo getForm2Vo(
			TipoAsistenciaColegioForm objectForm) {
		TipoAsistenciaColegioVo objectVo = new TipoAsistenciaColegioVo();
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equalsIgnoreCase(""))
			objectVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		if(objectForm.getIdTipoAsistenciaColegio()!=null&& !objectForm.getIdTipoAsistenciaColegio().equalsIgnoreCase(""))
			objectVo.setIdTipoAsistenciaColegio(Short.valueOf(objectForm.getIdTipoAsistenciaColegio()));
		objectVo.setDescripcion(objectForm.getDescripcion());
		if(objectForm.getImporte()!=null&& !objectForm.getImporte().equalsIgnoreCase(""))
			objectVo.setImporte(UtilidadesNumero.getDouble(objectForm.getImporte()));
		if(objectForm.getImporteMaximo()!=null && !objectForm.getImporteMaximo().equalsIgnoreCase(""))
			objectVo.setImporteMaximo(UtilidadesNumero.getDouble(objectForm.getImporteMaximo()));
		if(objectForm.getVisibleMovil()!=null && !objectForm.getVisibleMovil().equalsIgnoreCase(""))
			objectVo.setVisibleMovil(Short.valueOf(objectForm.getVisibleMovil()));
		if(objectForm.getUsuModificacion()!=null && !objectForm.getUsuModificacion().equalsIgnoreCase(""))
			objectVo.setUsuModificacion(Integer.valueOf(objectForm.getUsuModificacion()));
		if(objectForm.getFechaModificacion()!=null&& !objectForm.getFechaModificacion().equalsIgnoreCase("")){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				objectVo.setFechaModificacion(sdf.parse(objectForm.getFechaModificacion()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(objectForm.getTipoGuardia()!=null && !objectForm.getTipoGuardia().equalsIgnoreCase("")){
			String tipoGuardia = objectForm.getTipoGuardia();
			List<String> tiposGuardia = Arrays.asList(tipoGuardia.split("##"));
			objectVo.setTiposGuardia(tiposGuardia);
		}
		if(objectForm.getTipoGuardia()!=null && !objectForm.getTipoGuardia().equalsIgnoreCase("")){
			String tipoGuardia = objectForm.getTipoGuardia();
			objectVo.setTipoGuardia(tipoGuardia);
		}
		
		return objectVo;
	}

	public TipoAsistenciaColegioForm getVo2Form(TipoAsistenciaColegioVo objectVo) {
		return getVo2Form(objectVo,new TipoAsistenciaColegioForm());
	}

	public TipoAsistenciaColegioForm getVo2Form(TipoAsistenciaColegioVo objectVo,TipoAsistenciaColegioForm objectForm) {
		objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		objectForm.setIdTipoAsistenciaColegio(objectVo.getIdTipoAsistenciaColegio().toString());
		objectForm.setDescripcion(objectVo.getDescripcion());
		if(objectVo.getImporte()!=null){
			objectForm.setImporte(UtilidadesNumero.formatoCampo(objectVo.getImporte()));
			objectForm.setImporteNoEditable(UtilidadesString.formatoImporte(objectVo.getImporte()));
		}
		if(objectVo.getImporteMaximo()!=null){
			objectForm.setImporteMaximo(UtilidadesNumero.formatoCampo(objectVo.getImporteMaximo()));
			objectForm.setImporteMaximoNoEditable(UtilidadesString.formatoImporte(objectVo.getImporteMaximo()));
		}
		
		if(objectVo.getVisibleMovil()!=null)
			objectForm.setVisibleMovil(objectVo.getVisibleMovil().toString());
		if(objectVo.getUsuModificacion()!=null)
			objectForm.setUsuModificacion(objectVo.getUsuModificacion().toString());
		if(objectVo.getFechaModificacion()!=null){
			SimpleDateFormat sdfddmmyyyyhhmm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			objectForm.setFechaModificacion(sdfddmmyyyyhhmm.format(objectVo.getFechaModificacion()));
		}
		if(objectVo.getTiposGuardia()!=null){
			objectForm.setTiposGuardia(objectVo.getTiposGuardia());
		}
		if(objectVo.getTipoGuardia()!=null){
			objectForm.setTipoGuardia(objectVo.getTipoGuardia());
		}
		if(objectVo.getDescripcionTiposGuardia()!=null){
			objectForm.setDescripcionTiposGuardia(objectVo.getDescripcionTiposGuardia());
		}
		return objectForm;
		
	}

	
	
	

	
	
	

}
