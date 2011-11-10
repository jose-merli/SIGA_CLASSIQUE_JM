package com.siga.administracion.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.FileInforme;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;

public class InformeForm extends MasterForm {

	private String idPlantilla;
	private String descripcion;
	private String alias;
	private String nombreFisico;
	private String directorio;
	private String idTipoInforme;
	private String descripcionTipoInforme;
	private String visible;
	private String nombreSalida;
	private String preseleccionado;
	private String destinatarios;
	private String tipoFormato;
	private String idInstitucion;
	private String descripcionInstitucion;
	private String aSolicitantes;
	private String modoInterno;
	private String claseTipoInforme;
	private String orden;
	FilaExtElement[] elementosFila;
	private FormFile theFile;
	private FileInforme directorioFile;
	String msgError;
	String msgAviso;
	UsrBean usrBean;
	List<AdmTipoInformeBean> tiposInforme;
	List<AdmLenguajesBean> lenguajes;
	List<CenInstitucionBean> instituciones;
	int filaSeleccionada;
	int filaInformeSeleccionada;
	String botones;
	private String lenguaje;
	String idConsulta;
	String idInstitucionConsulta;
	String idTipoEnvio;
	private String idPlantillaEnvio;
	private String idPlantillaGeneracion;
	
	public String getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(String idConsulta) {
		this.idConsulta = idConsulta;
	}

	public String getIdInstitucionConsulta() {
		return idInstitucionConsulta;
	}

	public void setIdInstitucionConsulta(String idInstitucionConsulta) {
		this.idInstitucionConsulta = idInstitucionConsulta;
	}

	public String getBotones() {
		if (usrBean.getLocation() != null
				&& usrBean.getLocation().equals("2000")) {
			this.botones = "C,E,B";

		} else {
			this.botones = "C";
			if (!idInstitucion.equals("0")) {
				this.botones = "C,E,B";
			}
		}
		setBotones(this.botones);

		return this.botones;
	}

	public void setBotones(String botones) {
		this.botones = botones;
	}
	
	public FilaExtElement[] getElementosFila() {
		FilaExtElement[] elementosFila = null;
		if (!idInstitucion.equals("0")) {
			elementosFila = new FilaExtElement[3];
		} else {
			elementosFila = new FilaExtElement[2];
			elementosFila[1] = new FilaExtElement("duplicar", "duplicar","general.boton.duplicar", SIGAConstants.ACCESS_READ);

		}
		this.setElementosFila(elementosFila);
		return elementosFila;
	}

	public void setElementosFila(FilaExtElement[] elementosFila) {
		this.elementosFila = elementosFila;
	}

	public int getFilaSeleccionada() {
		return filaSeleccionada;
	}

	public void setFilaSeleccionada(int filaSeleccionada) {
		this.filaSeleccionada = filaSeleccionada;
	}

	public List<AdmTipoInformeBean> getTiposInforme() {
		return tiposInforme;
	}

	public void setTiposInforme(List<AdmTipoInformeBean> tiposInforme) {
		this.tiposInforme = tiposInforme;
	}

	public UsrBean getUsrBean() {
		return usrBean;
	}

	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgAviso() {
		return msgAviso;
	}

	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}

	public String getIdPlantilla() {
		return idPlantilla;
	}

	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getNombreFisico() {
		return nombreFisico;
	}

	public void setNombreFisico(String nombreFisico) {
		this.nombreFisico = nombreFisico;
	}

	public String getDirectorio() {
		return directorio;
	}

	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}

	public String getIdTipoInforme() {
		return idTipoInforme;
	}

	public void setIdTipoInforme(String idTipoInforme) {
		this.idTipoInforme = idTipoInforme;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getNombreSalida() {
		return nombreSalida;
	}

	public void setNombreSalida(String nombreSalida) {
		this.nombreSalida = nombreSalida;
	}

	public String getPreseleccionado() {
		return preseleccionado;
	}

	public void setPreseleccionado(String preseleccionado) {
		this.preseleccionado = preseleccionado;
	}

	public String getASolicitantes() {
		return aSolicitantes;
	}

	public void setASolicitantes(String aSolicitantes) {
		this.aSolicitantes = aSolicitantes;
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getDescripcionTipoInforme() {
		return descripcionTipoInforme;
	}

	public void setDescripcionTipoInforme(String descripcionTipoInforme) {
		this.descripcionTipoInforme = descripcionTipoInforme;
	}

	public AdmInformeBean getInformeVO() {
		return getInformeVO(new AdmInformeBean());

	}

	public AdmInformeBean getInformeVO(AdmInformeBean informeVO) {
		if (informeVO == null)
			informeVO = new AdmInformeBean();
		informeVO.setIdPlantilla(idPlantilla);
		informeVO.setDescripcion(descripcion);
		informeVO.setAlias(alias);
		informeVO.setNombreFisico(nombreFisico);
		informeVO.setDirectorio(directorio);
		informeVO.setIdTipoInforme(idTipoInforme);
		informeVO.setVisible(visible);
		informeVO.setNombreSalida(nombreSalida);
		if(idInstitucion!=null)
			informeVO.setIdInstitucion(new Integer(idInstitucion));
		informeVO.setPreseleccionado(preseleccionado);
		informeVO.setDestinatarios(destinatarios);
		informeVO.setASolicitantes(aSolicitantes);
		informeVO.setTipoformato(tipoFormato);
		informeVO.setUsrBean(usrBean);
		informeVO.setOrden(orden);
		informeVO.setIdTipoEnvio(idTipoEnvio);
		informeVO.setIdPlantillaEnvio(idPlantillaEnvio);
		informeVO.setIdPlantillaGeneracion(idPlantillaGeneracion);
		
		return informeVO;

	}

	public void clear() {
		idPlantilla = null;
		descripcion = null;
		alias = null;
		nombreFisico = null;
		directorio = null;
		idTipoInforme = null;
		descripcionTipoInforme = null;
		visible = null;
		nombreSalida = null;
		preseleccionado = null;
		aSolicitantes = null;
		destinatarios = null;
		tipoFormato = null;
		theFile = null;
		directorioFile = null;
		lenguaje = null;
		orden = null;

	}

	public List<CenInstitucionBean> getInstituciones() {
		return instituciones;
	}

	public void setInstituciones(List<CenInstitucionBean> instituciones) {
		this.instituciones = instituciones;
	}

	public String getDescripcionInstitucion() {
		return descripcionInstitucion;
	}

	public void setDescripcionInstitucion(String descripcionInstitucion) {
		this.descripcionInstitucion = descripcionInstitucion;
	}

	public FormFile getTheFile() {
		return theFile;
	}

	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	public FileInforme getDirectorioFile() {
		return directorioFile;
	}

	public void setDirectorioFile(FileInforme directorioFile) {
		this.directorioFile = directorioFile;
	}

	public int getFilaInformeSeleccionada() {
		return filaInformeSeleccionada;
	}

	public void setFilaInformeSeleccionada(int filaInformeSeleccionada) {
		this.filaInformeSeleccionada = filaInformeSeleccionada;
	}

	public String getLenguaje() {
		return lenguaje;
	}

	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}

	public List<AdmLenguajesBean> getLenguajes() {
		return lenguajes;
	}

	public void setLenguajes(List<AdmLenguajesBean> lenguajes) {
		this.lenguajes = lenguajes;
	}

	public String getModoInterno() {
		return modoInterno;
	}

	public void setModoInterno(String modoInterno) {
		this.modoInterno = modoInterno;
	}

	public String getClaseTipoInforme() {
		return claseTipoInforme;
	}

	public void setClaseTipoInforme(String claseTipoInforme) {
		this.claseTipoInforme = claseTipoInforme;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getIdTipoEnvio() {
		return idTipoEnvio;
	}

	public void setIdTipoEnvio(String idTipoEnvio) {
		this.idTipoEnvio = idTipoEnvio;
	}

	public String getIdPlantillaEnvio() {
		return idPlantillaEnvio;
	}

	public void setIdPlantillaEnvio(String idPlantillaEnvio) {
		this.idPlantillaEnvio = idPlantillaEnvio;
	}

	public String getIdPlantillaGeneracion() {
		return idPlantillaGeneracion;
	}

	public void setIdPlantillaGeneracion(String idPlantillaGeneracion) {
		this.idPlantillaGeneracion = idPlantillaGeneracion;
	}

	
}
