package com.siga.censo.form.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.pys.CargaMasivaDatosProductosVo;

import com.siga.comun.VoUiService;
import com.siga.productos.form.CargaProductosForm;

/**
 * @author Carlos Ruano Martínez
 * @date 17/06/2015
 * 
 *       Ser Campeón no es una Meta, es una Actitud
 * 
 */
public class CargaMasivaDatosProductosVoService implements VoUiService<CargaProductosForm, CargaMasivaDatosProductosVo> {

	@Override
	public List<CargaProductosForm> getVo2FormList(List<CargaMasivaDatosProductosVo> voList) {
		List<CargaProductosForm> cargaMasivaCVForms = new ArrayList<CargaProductosForm>();
		CargaProductosForm cargaMasivaCVForm = null;
		for (CargaMasivaDatosProductosVo objectVo : voList) {
			cargaMasivaCVForm = getVo2Form(objectVo);
			cargaMasivaCVForms.add(cargaMasivaCVForm);
		}

		return cargaMasivaCVForms;
	}

	@Override
	public CargaMasivaDatosProductosVo getForm2Vo(CargaProductosForm objectForm) {
		CargaMasivaDatosProductosVo objectVo = new CargaMasivaDatosProductosVo();

		if (objectForm.getIdInstitucion() != null && !objectForm.getIdInstitucion().equals("")) {
			objectVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if (objectForm.getIdFichero() != null && !objectForm.getIdFichero().equals("")) {
			objectVo.setIdFichero(Long.valueOf(objectForm.getIdFichero()));
		}
		if (objectForm.getIdFicheroLog() != null && !objectForm.getIdFicheroLog().equals("")) {
			objectVo.setIdFicheroLog(Long.valueOf(objectForm.getIdFicheroLog()));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (objectForm.getFechaCarga() != null && !objectForm.getFechaCarga().equals(""))
			try {
				objectVo.setFechaCarga(sdf.parse(objectForm.getFechaCarga()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

		if (objectForm.getNombreFichero() != null && !objectForm.getNombreFichero().equals(""))
			objectVo.setNombreFichero(objectForm.getNombreFichero());

		if (objectForm.getNumRegistros() != null && !objectForm.getNumRegistros().equals(""))
			objectVo.setNumRegistros(Short.valueOf(objectForm.getNumRegistros()));

		if (objectForm.getUsuario() != null && !objectForm.getUsuario().equals(""))
			objectVo.setUsuario(objectForm.getUsuario());

		if (objectForm.getError() != null && !objectForm.getError().equals(""))
			objectVo.setError(objectForm.getError());

		objectVo.setCodIdioma(objectForm.getCodIdioma());

		return objectVo;
	}

	@Override
	public CargaProductosForm getVo2Form(CargaMasivaDatosProductosVo objectVo) {
		CargaProductosForm objectForm = new CargaProductosForm();

		if (objectVo.getIdInstitucion() != null && !objectVo.getIdInstitucion().equals("")) {
			objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		}
		if (objectVo.getIdFichero() != null && !objectVo.getIdFichero().equals("")) {
			objectForm.setIdFichero(objectVo.getIdFichero().toString());
		}
		if (objectVo.getIdFicheroLog() != null && !objectVo.getIdFicheroLog().equals("")) {
			objectForm.setIdFicheroLog(objectVo.getIdFicheroLog().toString());
		}

		objectForm.setCodIdioma(objectVo.getCodIdioma());

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (objectVo.getFechaCarga() != null && !objectVo.getFechaCarga().equals(""))
			objectForm.setFechaCarga(sdf.format(objectVo.getFechaCarga()));

		if (objectVo.getNombreFichero() != null && !objectVo.getNombreFichero().equals(""))
			objectForm.setNombreFichero(objectVo.getNombreFichero());

		if (objectVo.getNumRegistros() != null && !objectVo.getNumRegistros().equals(""))
			objectForm.setNumRegistros(objectVo.getNumRegistros().toString());

		if (objectVo.getUsuario() != null && !objectVo.getUsuario().equals(""))
			objectForm.setUsuario(objectVo.getUsuario());

		if (objectVo.getError() != null && !objectVo.getError().equals(""))
			objectForm.setError(objectVo.getError());
		else
			objectForm.setError("");

		if (objectVo.getNumColegiadoCliente() != null && !objectVo.getNumColegiadoCliente().equals(""))
			objectForm.setNumColegiadoCliente(objectVo.getNumColegiadoCliente());
		else
			objectForm.setNumColegiadoCliente("");

		if (objectVo.getNifCliente() != null && !objectVo.getNifCliente().equals(""))
			objectForm.setNifCliente(objectVo.getNifCliente());
		else
			objectForm.setNifCliente("");
		
		if (objectVo.getApellidosCliente() != null && !objectVo.getApellidosCliente().equals(""))
			objectForm.setApellidosCliente(objectVo.getApellidosCliente());
		else
			objectForm.setApellidosCliente("");
		
		if (objectVo.getNombreCliente() != null && !objectVo.getNombreCliente().equals(""))
			objectForm.setNombreCliente(objectVo.getNombreCliente());
		else
			objectForm.setNombreCliente("");		

		if (objectVo.getIdProducto() != null && !objectVo.getIdProducto().equals(""))
			objectForm.setIdProducto(objectVo.getIdProducto().toString());
		else
			objectForm.setIdProducto("");

		if (objectVo.getIdTipoProducto() != null && !objectVo.getIdTipoProducto().equals(""))
			objectForm.setIdTipoProducto(objectVo.getIdTipoProducto().toString());
		else
			objectForm.setIdTipoProducto("");

		if (objectVo.getIdCategoriaProducto() != null && !objectVo.getIdCategoriaProducto().equals(""))
			objectForm.setIdCategoriaProducto(objectVo.getIdCategoriaProducto().toString());
		else
			objectForm.setIdCategoriaProducto("");

		if (objectVo.getNombreProducto() != null && !objectVo.getNombreProducto().equals(""))
			objectForm.setNombreProducto(objectVo.getNombreProducto());
		else
			objectForm.setNombreProducto("");
		
		if (objectVo.getCantidadProducto() != null && !objectVo.getCantidadProducto().equals(""))
			objectForm.setCantidadProducto(objectVo.getCantidadProducto().toString());
		else
			objectForm.setCantidadProducto("");		
		
		if (objectVo.getFechaCompra() != null && !objectVo.getFechaCompra().equals(""))
			objectForm.setFechaCompra(objectVo.getFechaCompra());		
		else
			objectForm.setFechaCompra("");		

		return objectForm;
	}

	@Override
	public CargaProductosForm getVo2Form(CargaMasivaDatosProductosVo objectVo, CargaProductosForm objectForm) {
		return null;
	}

}
