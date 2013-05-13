package com.siga.administracion.form;

import com.siga.beans.GenTablasMaestrasBean;
import com.siga.general.MasterForm;

public class SIGAListadoTablasMaestrasForm extends MasterForm
{
//    private String modo="";
    private String modal="";
    private String codigoRegistro="";
    private String codigoRegistroExt="";
    private String descripcionRegistro="";
    private String nombreTablaMaestra="";
    private String nombreCampoCodigo="";
    private String nombreCampoCodigoExt="";
    private String nombreCampoDescripcion="";
    private String local="";
    private String aliasTabla="";
    private String longitudCodigo="";
    private String longitudCodigoExt="";
    private String longitudDescripcion="";
    private String tipoCodigo="";
    private String tipoCodigoExt="";
    private String codigoBusqueda="";
    private String descripcionBusqueda="";
    private String ponerBajaLogica="";
    private String idRelacionado="";
    
    private String idTablaRel;
    private String idCampoCodigoRel;
    private String descripcionRel;
    private String queryTablaRel;
    private String numeroTextoPlantillas;
    private String textoPlantillas;
    
    
//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }

    public String getModal() 
    {
        return modal;
    }
    
    public void setModal(String modal) 
    {
        this.modal = modal;
    }

    public String getCodigoRegistro() 
    {
        return codigoRegistro;
    }
    
    public void setCodigoRegistro(String codigoRegistro) 
    {
        this.codigoRegistro = codigoRegistro;
    }
    
    public String getCodigoRegistroExt() 
    {
        return codigoRegistroExt;
    }
    
    public void setCodigoRegistroExt(String codigoRegistroExt) 
    {
        this.codigoRegistroExt = codigoRegistroExt;
    }
    
    public String getDescripcionRegistro() 
    {
        return descripcionRegistro;
    }
    
    public void setDescripcionRegistro(String descripcionRegistro) 
    {
        this.descripcionRegistro = descripcionRegistro;
    }
    
    public String getNombreTablaMaestra() 
    {
        return nombreTablaMaestra;
    }
    
    public void setNombreTablaMaestra(String nombreTablaMaestra) 
    {
        this.nombreTablaMaestra = nombreTablaMaestra;
    }

    public String getNombreCampoCodigo() 
    {
        return nombreCampoCodigo;
    }
    public String getNombreCampoCodigoExt() 
    {
        return nombreCampoCodigoExt;
    }
    
    public void setNombreCampoCodigo(String nombreCampoCodigo) 
    {
        this.nombreCampoCodigo = nombreCampoCodigo;
    }

    public void setNombreCampoCodigoExt(String nombreCampoCodigoExt) 
    {
        this.nombreCampoCodigoExt = nombreCampoCodigoExt;
    }

    public String getNombreCampoDescripcion() 
    {
        return nombreCampoDescripcion;
    }
    
    public void setNombreCampoDescripcion(String nombreCampoDescripcion) 
    {
        this.nombreCampoDescripcion = nombreCampoDescripcion;
    }

    public String getLocal()
    {
        return local;
    }
    
    public void setLocal(String local) 
    {
        this.local = local;
    }

    public String getAliasTabla()
    {
        return aliasTabla;
    }
    
    public void setAliasTabla(String aliasTabla) 
    {
        this.aliasTabla = aliasTabla;
    }

    public String getLongitudCodigo()
    {
        return longitudCodigo;
    }
    
    public void setLongitudCodigo(String longitudCodigo) 
    {
        this.longitudCodigo = longitudCodigo;
    }
    public String getLongitudCodigoExt()
    {
        return longitudCodigoExt;
    }
    
    public void setLongitudCodigoExt(String longitudCodigoExt) 
    {
        this.longitudCodigoExt = longitudCodigoExt;
    }

    public String getLongitudDescripcion()
    {
        return longitudDescripcion;
    }
    
    public void setLongitudDescripcion(String longitudDescripcion) 
    {
        this.longitudDescripcion = longitudDescripcion;
    }

    public String getTipoCodigo()
    {
        return tipoCodigo;
    }
    
    public void setTipoCodigo(String tipoCodigo) 
    {
        this.tipoCodigo = tipoCodigo;
    }

    public String getTipoCodigoExt()
    {
        return tipoCodigoExt;
    }
    
    public void setTipoCodigoExt(String tipoCodigoExt) 
    {
        this.tipoCodigoExt = tipoCodigoExt;
    }

    public String getCodigoBusqueda()
    {
        return codigoBusqueda;
    }
    
    public void setCodigoBusqueda(String codigoBusqueda) 
    {
        this.codigoBusqueda = codigoBusqueda;
    }

    public String getDescripcionBusqueda()
    {
        return descripcionBusqueda;
    }
    
    public void setDescripcionBusqueda(String descripcionBusqueda) 
    {
        this.descripcionBusqueda = descripcionBusqueda;
    }

	public String getPonerBajaLogica() {
		return ponerBajaLogica;
	}

	public void setPonerBajaLogica(String ponerBajaLogica) {
		this.ponerBajaLogica = ponerBajaLogica;
	}

	public String getIdRelacionado() {
		return idRelacionado;
	}

	public void setIdRelacionado(String idRelacionado) {
		this.idRelacionado = idRelacionado;
	}

	public String getIdTablaRel() {
		return idTablaRel;
	}

	public void setIdTablaRel(String idTablaRel) {
		this.idTablaRel = idTablaRel;
	}

	public String getIdCampoCodigoRel() {
		return idCampoCodigoRel;
	}

	public void setIdCampoCodigoRel(String idCampoCodigoRel) {
		this.idCampoCodigoRel = idCampoCodigoRel;
	}

	

	public String getNumeroTextoPlantillas() {
		return numeroTextoPlantillas;
	}

	public void setNumeroTextoPlantillas(String numeroTextoPlantillas) {
		this.numeroTextoPlantillas = numeroTextoPlantillas;
	}

	public String getQueryTablaRel() {
		return queryTablaRel;
	}

	public void setQueryTablaRel(String queryTablaRel) {
		this.queryTablaRel = queryTablaRel;
	}

	public String getDescripcionRel() {
		return descripcionRel;
	}

	public void setDescripcionRel(String descripcionRel) {
		this.descripcionRel = descripcionRel;
	}

	public String getTextoPlantillas() {
		return textoPlantillas;
	}

	public void setTextoPlantillas(String textoPlantillas) {
		this.textoPlantillas = textoPlantillas;
	}

	
	
}