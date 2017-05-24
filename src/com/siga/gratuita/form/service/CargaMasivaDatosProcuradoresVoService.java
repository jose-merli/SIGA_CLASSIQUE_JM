package com.siga.gratuita.form.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.scs.CargaMasivaDatosProcuradoresVo;

import com.siga.comun.VoUiService;
import com.siga.gratuita.form.CargaMasivaProcuradoresForm;

/**
 * 
 * @author jorgeta
 *
 */
public class CargaMasivaDatosProcuradoresVoService implements VoUiService<CargaMasivaProcuradoresForm, CargaMasivaDatosProcuradoresVo> {

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2FormList(java.util.List)
	 */
	@Override
	public List<CargaMasivaProcuradoresForm> getVo2FormList(
			List<CargaMasivaDatosProcuradoresVo> voList) {
		List<CargaMasivaProcuradoresForm> cargaMasivaProcuradoresForms = new ArrayList<CargaMasivaProcuradoresForm>();
		CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm   = null;
		for (CargaMasivaDatosProcuradoresVo objectVo : voList) {
			cargaMasivaProcuradoresForm = getVo2Form(objectVo);
			cargaMasivaProcuradoresForms.add(cargaMasivaProcuradoresForm);

		}
		return cargaMasivaProcuradoresForms;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getForm2Vo(java.lang.Object)
	 */
	@Override
	public CargaMasivaDatosProcuradoresVo getForm2Vo(CargaMasivaProcuradoresForm objectForm) {
		CargaMasivaDatosProcuradoresVo objectVo = new CargaMasivaDatosProcuradoresVo(); 
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
			objectVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if(objectForm.getIdFichero()!=null && !objectForm.getIdFichero().equals("")){
			objectVo.setIdFichero(Long.valueOf(objectForm.getIdFichero()));
		}
		if(objectForm.getIdFicheroLog()!=null && !objectForm.getIdFicheroLog().equals("")){
			objectVo.setIdFicheroLog(Long.valueOf(objectForm.getIdFicheroLog()));
		}
		
		if(objectForm.getCodigoDesignaAbogado()!=null && !objectForm.getCodigoDesignaAbogado().equals(""))
			objectVo.setDesignaAbogadoCodigo(objectForm.getCodigoDesignaAbogado());
		
		if(objectForm.getNombreProcurador()!=null && !objectForm.getNombreProcurador().equals(""))
			objectVo.setNombreProcurador(objectForm.getNombreProcurador());
		if(objectForm.getNumColProcurador()!=null && !objectForm.getNumColProcurador().equals(""))
			objectVo.setNumColProcurador(objectForm.getNumColProcurador());
		if(objectForm.getNumDesignaProcurador()!=null && !objectForm.getNumDesignaProcurador().equals(""))
			objectVo.setNumDesignaProcurador(objectForm.getNumDesignaProcurador());
		if(objectForm.getNumEjg()!=null && !objectForm.getNumEjg().equals(""))
			objectVo.setEjgNumEjg(objectForm.getNumEjg());
		if(objectForm.getObservaciones()!=null && !objectForm.getObservaciones().equals(""))
			objectVo.setObservaciones(objectForm.getObservaciones());

		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getFechaDesignaProcurador()!=null && !objectForm.getFechaDesignaProcurador().equals(""))
			try {
				objectVo.setFechaDesignaProcurador(sdf.parse(objectForm.getFechaDesignaProcurador()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		
		if(objectForm.getIdCargaMasivaProcuradores()!=null && !objectForm.getIdCargaMasivaProcuradores().equals(""))
			objectVo.setIdCargaMasivaProcuradores(Long.valueOf(objectForm.getIdCargaMasivaProcuradores()));

		if(objectForm.getNombreFichero()!=null && !objectForm.getNombreFichero().equals(""))
			objectVo.setNombreFichero(objectForm.getNombreFichero());
		if(objectForm.getNumRegistros()!=null && !objectForm.getNumRegistros().equals(""))
			objectVo.setNumRegistros(Short.valueOf(objectForm.getNumRegistros()));

		if(objectForm.getUsuario()!=null && !objectForm.getUsuario().equals(""))
			objectVo.setUsuario(objectForm.getUsuario());
		if(objectForm.getError()!=null && !objectForm.getError().equals(""))
			objectVo.setError(objectForm.getError());
		
		objectVo.setCodIdioma(objectForm.getCodIdioma());
		return objectVo;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object)
	 */
	@Override
	public CargaMasivaProcuradoresForm getVo2Form(CargaMasivaDatosProcuradoresVo objectVo) {
		CargaMasivaProcuradoresForm objectForm = new CargaMasivaProcuradoresForm();

		if(objectVo.getIdInstitucion()!=null && !objectVo.getIdInstitucion().equals("")){
			objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		}
		if(objectVo.getIdFichero()!=null && !objectVo.getIdFichero().equals("")){
			objectForm.setIdFichero(objectVo.getIdFichero().toString());
		}
		if(objectVo.getIdFicheroLog()!=null && !objectVo.getIdFicheroLog().equals("")){
			objectForm.setIdFicheroLog(objectVo.getIdFicheroLog().toString());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if(objectVo.getFechaCarga()!=null && !objectVo.getFechaCarga().equals(""))
			objectForm.setFechaCarga(sdf.format(objectVo.getFechaCarga()));
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		
		if(objectVo.getDesignaAbogadoCodigo()!=null && !objectVo.getDesignaAbogadoCodigo().equals(""))
			objectForm.setCodigoDesignaAbogado(objectVo.getDesignaAbogadoCodigo());
		
		if(objectVo.getNombreProcurador()!=null && !objectVo.getNombreProcurador().equals(""))
			objectForm.setNombreProcurador(objectVo.getNombreProcurador());
		if(objectVo.getNumColProcurador()!=null && !objectVo.getNumColProcurador().equals(""))
			objectForm.setNumColProcurador(objectVo.getNumColProcurador());
		if(objectVo.getNumDesignaProcurador()!=null && !objectVo.getNumDesignaProcurador().equals(""))
			objectForm.setNumDesignaProcurador(objectVo.getNumDesignaProcurador());
		if(objectVo.getEjgNumEjg()!=null && !objectVo.getEjgNumEjg().equals(""))
			objectForm.setNumEjg(objectVo.getEjgNumEjg());
		if(objectVo.getObservaciones()!=null && !objectVo.getObservaciones().equals(""))
			objectForm.setObservaciones(objectVo.getObservaciones());

		
		
	
		if(objectVo.getFechaDesignaProcurador()!=null && !objectVo.getFechaDesignaProcurador().equals(""))
			
			objectForm.setFechaDesignaProcurador(sdf.format(objectVo.getFechaDesignaProcurador()));
			
		
		if(objectVo.getIdCargaMasivaProcuradores()!=null)
			objectForm.setIdCargaMasivaProcuradores(objectVo.getIdCargaMasivaProcuradores().toString());
		
		
		
		
		
		if(objectVo.getNombreFichero()!=null && !objectVo.getNombreFichero().equals(""))
			objectForm.setNombreFichero(objectVo.getNombreFichero());
		if(objectVo.getNumRegistros()!=null && !objectVo.getNumRegistros().equals(""))
			objectForm.setNumRegistros(objectVo.getNumRegistros().toString());
		
		if(objectVo.getUsuario()!=null && !objectVo.getUsuario().equals(""))
			objectForm.setUsuario(objectVo.getUsuario());
		if(objectVo.getError()!=null && !objectVo.getError().equals(""))
			objectForm.setError(objectVo.getError());
		else
			objectForm.setError("");
		
		objectForm.setCodIdioma(objectVo.getCodIdioma());
		return objectForm;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CargaMasivaProcuradoresForm getVo2Form(CargaMasivaDatosProcuradoresVo objectVo,
			CargaMasivaProcuradoresForm objectForm) {
		// TODO Auto-generated method stub
		return null;
	}









}
