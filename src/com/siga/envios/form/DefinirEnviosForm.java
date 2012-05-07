package com.siga.envios.form;

import java.util.List;
import java.util.StringTokenizer;

import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.beans.ScsComisariaBean;
import com.siga.general.MasterForm;

/**
 * Formulario para la definición de envios. Created on Mar 15, 2005
 * 
 * @author juan.grau
 */
public class DefinirEnviosForm extends MasterForm
{
	// Atributos
	private String tipoFecha = EnvEnviosAdm.FECHA_CREACION;
	private String idTipoInforme;

	List<EnvPlantillasEnviosBean> plantillasEnvio;
	// GETTERS
	public String getTipoFecha() { return tipoFecha; }
	private String datosInforme;
	public String getComboTipoEnvio() { return (String) this.datos.get("comboTipoEnvio"); }
	public String getIdEstado() { return (String) this.datos.get("idEstado"); }
	public String getIdTipoEnvio() { return (String) this.datos.get("idTipoEnvio"); }
	public String getNombre() { return (String) this.datos.get("nombre"); }
	public String getFechaDesde() { return (String) this.datos.get("fechaDesde"); }
	public String getFechaHasta() { return (String) this.datos.get("fechaHasta"); }
	public String getIdEnvio() { return (String) this.datos.get("idEnvio"); }
	public String getIdEnvioBuscar() { return (String) this.datos.get("idEnvioBuscar"); }
	public String getIdPlantillaEnvios() { return (String) this.datos.get("idPlantillaEnvios"); }
	public String getFechaProgramada() { return (String) this.datos.get("fechaProgramada"); }
	public String getIdPersona() { return (String) this.datos.get("idPersona"); }
	public String getIdSolicitud() { return (String) this.datos.get("idSolicitud"); }
	public String getComboPlantillaEnvio() { return (String) this.datos.get("comboPlantillaEnvio"); }
	public String getIdPlantillaGeneracion() { return (String) this.datos.get("idPlantillaGeneracion"); }
	public String getDescEnvio() { return (String) this.datos.get("descEnvio"); }
	public String getSubModo() { return (String) this.datos.get("subModo"); }
	public String getEditarEnvio() { return (String) this.datos.get("editarEnvio"); }
	public String getIdImpresora() { return (String) this.datos.get("idImpresora"); }
	public String getIdFactura() { return (String) this.datos.get("idFactura"); }
	public String getIdsParaEnviar() { return (String) this.datos.get("idsParaEnviar"); }
	public String getColegio() { return (String) this.datos.get("colegio"); }
	public String getDatosEnvios() { return (String) this.datos.get("datosEnvios"); }
	public String getDescargar() { return (String) this.datos.get("descargar"); }
	public String getClavesIteracion() { return (String) this.datos.get("clavesIteracion"); }
	public String getAcuseRecibo() { return (String) this.datos.get("acuseRecibo"); }
	
	
	// SETTERS
	public void setTipoFecha(String tipoFecha) { try { this.tipoFecha = tipoFecha; } catch (Exception e) {} }
	
	public void setComboTipoEnvio(String comboTipoEnvio) {
		this.datos.put("comboTipoEnvio", comboTipoEnvio);
		if (comboTipoEnvio != null && !comboTipoEnvio.equals("")) {
			StringTokenizer st = new StringTokenizer(comboTipoEnvio, ",");
			st.nextToken();// idinstitucion
			this.setIdTipoEnvio(st.nextToken());
		} else {
			this.setIdTipoEnvio("");
		}
	}
	public void setIdEstado(String idEstado) { try { this.datos.put("idEstado", idEstado); } catch (Exception e) {} }
	public void setIdTipoEnvio(String idTipoEnvio) { try { this.datos.put("idTipoEnvio", idTipoEnvio); } catch (Exception e) {} }
	public void setNombre(String nombre) { try { this.datos.put("nombre", nombre); } catch (Exception e) {} }
	public void setFechaDesde(String fechaDesde) { try { this.datos.put("fechaDesde", fechaDesde); } catch (Exception e) {} }
	public void setFechaHasta(String fechaHasta) { try { this.datos.put("fechaHasta", fechaHasta); } catch (Exception e) {} }
	public void setIdEnvio(String idEnvio) { try { this.datos.put("idEnvio", idEnvio); } catch (Exception e) {} }
	public void setIdEnvioBuscar(String idEnvio) { try { this.datos.put("idEnvioBuscar", idEnvio); } catch (Exception e) {} }
	public void setIdPlantillaEnvios(String idPlantillaEnvios) { try { this.datos.put("idPlantillaEnvios", idPlantillaEnvios); } catch (Exception e) {} }
	public void setFechaProgramada(String fechaProgramada) { try { this.datos.put("fechaProgramada", fechaProgramada); } catch (Exception e) {} }
	public void setIdPersona(String idPersona) { try { this.datos.put("idPersona", idPersona); } catch (Exception e) {} }
	public void setAcuseRecibo(String acuseRecibo) { try { this.datos.put("acuseRecibo", acuseRecibo); } catch (Exception e) {} }
	
	public void setIdSolicitud(String idSolicitud) { try { this.datos.put("idSolicitud", idSolicitud); } catch (Exception e) {} }
	public void setComboPlantillaEnvio(String comboPlantillaEnvio) {
		this.datos.put("comboPlantillaEnvio", comboPlantillaEnvio);
		if (comboPlantillaEnvio != null && !comboPlantillaEnvio.equals("")) {
			StringTokenizer st = new StringTokenizer(comboPlantillaEnvio, ",");
			this.setIdPlantillaEnvios(st.nextToken());
		} else {
			this.setIdPlantillaEnvios("");
		}
	}
	public void setIdPlantillaGeneracion(String idPlantillaGeneracion) { try { this.datos.put("idPlantillaGeneracion", idPlantillaGeneracion); } catch (Exception e) {} }
	public void setDescEnvio(String descEnvio) { try { this.datos.put("descEnvio", descEnvio); } catch (Exception e) {} }
	public void setSubModo(String subModo) { try { this.datos.put("subModo", subModo); } catch (Exception e) {} }
	public void setEditarEnvio(String editarEnvio) { try { this.datos.put("editarEnvio", editarEnvio); } catch (Exception e) {} }
	public void setIdImpresora(String idImpresora) { try { this.datos.put("idImpresora", idImpresora); } catch (Exception e) {} }
	public void setIdFactura(String idFactura) { try { this.datos.put("idFactura", idFactura); } catch (Exception e) {} }
	public void setIdsParaEnviar(String idsParaEnviar) { try { this.datos.put("idsParaEnviar", idsParaEnviar); } catch (Exception e) {} }
	public void setColegio(String colegio) { try { this.datos.put("colegio", colegio); } catch (Exception e) {} }
	public void setDatosEnvios(String datosEnvios) { try { this.datos.put("datosEnvios", datosEnvios); } catch (Exception e) {} }
	public void setDescargar(String descargar) { try { this.datos.put("descargar", descargar); } catch (Exception e) {} }
	public void setClavesIteracion(String clavesIteracion) { try { this.datos.put("clavesIteracion", clavesIteracion); } catch (Exception e) {} }

	public List<EnvPlantillasEnviosBean> getPlantillasEnvio() {
		return plantillasEnvio;
	}

	public void setPlantillasEnvio(List<EnvPlantillasEnviosBean> plantillasEnvio) {
		this.plantillasEnvio = plantillasEnvio;
	}
	public String getDatosInforme() {
		return datosInforme;
	}
	public void setDatosInforme(String datosInforme) {
		this.datosInforme = datosInforme;
	}
	public String getIdTipoInforme() {
		return idTipoInforme;
	}
	public void setIdTipoInforme(String idTipoInforme) {
		this.idTipoInforme = idTipoInforme;
	}
	
	
	
	
}
