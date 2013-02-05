/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la administración de las fases de los expediente.
 */
public class CampoTipoExpedienteForm extends MasterForm {
    
    
//    private String modo="";
    private String modal="";
    //private boolean fecha;
    private boolean nexpDisciplinario;
    private boolean estado;
    private boolean institucion;
    private boolean asuntoJudicial;
    private boolean alertas;
    private boolean documentacion;
    private boolean seguimiento;
    private boolean denunciantes;
    private boolean denunciados;
    private boolean partes;
    private boolean resolucion;

    private boolean minutaInicial;
    private boolean minutaFinal;
    private boolean derechos;
    private boolean solicitanteEJG;
    private boolean resultadoInforme;
    private boolean relacionEJG;  
    private boolean relacionExpediente;  
    private String chkPestanaConf1;
    private String pestanaConf1;
    private String chkPestanaConf2;
    private String pestanaConf2;
    private String tiempoCaducidad;
    private String diasAntelacionCad;
 
	private String nombre;
	private String idTipoExpediente;
	private String nombreCampoDenunciante;
	private String nombreCampoDenunciado;
	private String nombreCampoNumExp;
	
	private String enviarAvisos;
	private String idTipoEnvios;
	private String idPlantillaEnvios;
	private String idPlantilla;
	private String comboTipoExpediente;
	
	//mhg Incidencia EJGs
	private String modificarEJG;

    public String getModificarEJG() {
		return modificarEJG;
	}
	public void setModificarEJG(String modificarEJG) {
		this.modificarEJG = modificarEJG;
	}
	public boolean isRelacionExpediente() {
		return relacionExpediente;
	}
	public void setRelacionExpediente(boolean relacionExpediente) {
		this.relacionExpediente = relacionExpediente;
	}
	public String getComboTipoExpediente() {
		return comboTipoExpediente;
	}
	public void setComboTipoExpediente(String comboTipoExpediente) {
		this.comboTipoExpediente = comboTipoExpediente;
	}
	public boolean isRelacionEJG() {
		return relacionEJG;
	}
	public void setRelacionEJG(boolean relacionEJG) {
		this.relacionEJG = relacionEJG;
	}
	public String getNombreCampoNumExp() {
		return nombreCampoNumExp;
	}
	public void setNombreCampoNumExp(String nombreCampoNumExp) {
		this.nombreCampoNumExp = nombreCampoNumExp;
	}
	public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public boolean getAlertas() {
        return alertas;
    }
    public void setAlertas(boolean alertas) {
        this.alertas = alertas;
    }
    public boolean getAsuntoJudicial() {
        return asuntoJudicial;
    }
    public void setAsuntoJudicial(boolean asuntoJudicial) {
        this.asuntoJudicial = asuntoJudicial;
    }
    public boolean getDenunciantes() {
        return denunciantes;
    }
    public void setDenunciantes(boolean denunciantes) {
        this.denunciantes = denunciantes;
    }
    
    public boolean getDenunciados() {
		return denunciados;
	}
	public void setDenunciados(boolean denunciados) {
		this.denunciados = denunciados;
	}
	public boolean getDocumentacion() {
        return documentacion;
    }
    public void setDocumentacion(boolean documentacion) {
        this.documentacion = documentacion;
    }
    public boolean getEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    /*public boolean getFecha() {
        return fecha;
    }
    public void setFecha(boolean fecha) {
        this.fecha = fecha;
    }*/
    public boolean getInstitucion() {
        return institucion;
    }
    public void setInstitucion(boolean institucion) {
        this.institucion = institucion;
    }
    public String getModal() {
        return modal;
    }
    public void setModal(String modal) {
        this.modal = modal;
    }
//    public String getModo() {
//        return modo;
//    }
//    public void setModo(String modo) {
//        this.modo = modo;
//    }
    
    public boolean getPartes() {
        return partes;
    }
    public void setPartes(boolean partes) {
        this.partes = partes;
    }
    public boolean getSeguimiento() {
        return seguimiento;
    }
    public void setSeguimiento(boolean seguimiento) {
        this.seguimiento = seguimiento;
    }
    public boolean getNexpDisciplinario() {
        return nexpDisciplinario;
    }
    public void setNexpDisciplinario(boolean nexpDisciplinario) {
        this.nexpDisciplinario = nexpDisciplinario;
    }
    public boolean getResolucion() {
        return resolucion;
    }
    public void setResolucion(boolean resolucion) {
        this.resolucion = resolucion;
    }
    public String getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(String idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    
	public boolean getMinutaInicial() {
		return minutaInicial;
	}
	public void setMinutaInicial(boolean minuta) {
		this.minutaInicial = minuta;
	}
	
	public boolean getMinutaFinal() {
		return minutaFinal;
	}
	public void setMinutaFinal(boolean minutaFinal) {
		this.minutaFinal = minutaFinal;
	}
	public boolean getDerechos() {
		return derechos;
	}
	public void setDerechos(boolean derechos) {
		this.derechos = derechos;
	}
	public boolean getResultadoInforme() {
		return resultadoInforme;
	}
	public void setResultadoInforme(boolean resultadoInforme) {
		this.resultadoInforme = resultadoInforme;
	}

    public void setChkPestanaConf1(String valor) {
        this.chkPestanaConf1 = valor;
    }
    public void setPestanaConf1(String valor) {
        this.pestanaConf1 = valor;
    }
    public void setChkPestanaConf2(String valor) {
        this.chkPestanaConf2 = valor;
    }
    public void setPestanaConf2(String valor) {
        this.pestanaConf2 = valor;
    }
	public String getChkPestanaConf1() {
		return chkPestanaConf1;
	}
	public String getPestanaConf1() {
		return pestanaConf1;
	}
	public String getChkPestanaConf2() {
		return chkPestanaConf2;
	}
	public String getPestanaConf2() {
		return pestanaConf2;
	}
	public String getNombreCampoDenunciante() {
		return nombreCampoDenunciante;
	}
	public void setNombreCampoDenunciante(String nombreCampoDenunciante) {
		this.nombreCampoDenunciante = nombreCampoDenunciante;
	}
	public String getNombreCampoDenunciado() {
		return nombreCampoDenunciado;
	}
	public void setNombreCampoDenunciado(String nombreCampoDenunciado) {
		this.nombreCampoDenunciado = nombreCampoDenunciado;
	}
    public String getTiempoCaducidad() {
        return tiempoCaducidad;
    }
    public void setTiempoCaducidad(String valor) {
        this.tiempoCaducidad = valor;
    }

    public String getDiasAntelacionCad() {
        return diasAntelacionCad;
    }
    public void setDiasAntelacionCad(String valor) {
        this.diasAntelacionCad = valor;
    }
	public String getEnviarAvisos() {
		return enviarAvisos;
	}
	public void setEnviarAvisos(String enviarAvisos) {
		this.enviarAvisos = enviarAvisos;
	}
	public String getIdTipoEnvios() {
		return idTipoEnvios;
	}
	public void setIdTipoEnvios(String idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}
	public String getIdPlantillaEnvios() {
		return idPlantillaEnvios;
	}
	public void setIdPlantillaEnvios(String idPlantillaEnvios) {
		this.idPlantillaEnvios = idPlantillaEnvios;
	}
	public String getIdPlantilla() {
		return idPlantilla;
	}
	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}
	public boolean getSolicitanteEJG() {
		return solicitanteEJG;
	}
	public void setSolicitanteEJG(boolean solicitanteEJG) {
		this.solicitanteEJG = solicitanteEJG;
	}

}
