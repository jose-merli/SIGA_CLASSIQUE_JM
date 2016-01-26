package com.siga.certificados.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;

public class SIGAMantenimientoCertificadosPlantillasForm extends MasterForm
{
/**
	 * 
	 */
	private static final long serialVersionUID = 2862542709226458772L;
	//    private String modo="";
    private String certificado="";
    private String editable="";
    private String idInstitucion="";
    private String idTipoProducto="";
    private String idProducto="";
    private String idProductoInstitucion="";
    private String idPlantilla="";
    private String descripcion="";
    private String porDefecto="";
    private FormFile theFile;
    private String[] modos = {"modosRelacionPlantillas"};
    public String[] getModos () {return this.modos;}
    private String recarga;
    private String nuevo;
    private String relacion;     //indica si es vamos por el camino de relación o simplemente es editar.
    private String idRelacion;
    
    private String descripcionCertificado="";

//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public String getCertificado()
    {
    	return certificado;
    }
    
    public void setCertificado(String certificado)
    {
    	this.certificado=certificado;
    }

    public String getEditable()
    {
    	return editable;
    }
    
    public void setEditable(String editable)
    {
    	this.editable=editable;
    }

    public String getIdInstitucion()
    {
    	return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion)
    {
    	this.idInstitucion=idInstitucion;
    }

    public String getIdTipoProducto()
    {
    	return idTipoProducto;
    }
    
    public void setIdTipoProducto(String idTipoProducto)
    {
    	this.idTipoProducto=idTipoProducto;
    }

    public String getIdProducto()
    {
    	return idProducto;
    }
    
    public void setIdProducto(String idProducto)
    {
    	this.idProducto=idProducto;
    }

    public String getIdProductoInstitucion()
    {
    	return idProductoInstitucion;
    }
    
    public void setIdProductoInstitucion(String idProductoInstitucion)
    {
    	this.idProductoInstitucion=idProductoInstitucion;
    }

    public String getIdPlantilla()
    {
    	return idPlantilla;
    }
    
    public void setIdPlantilla(String idPlantilla)
    {
    	this.idPlantilla=idPlantilla;
    }

    public String getDescripcion()
    {
    	return descripcion;
    }
    
    public void setDescripcion(String descripcion)
    {
    	this.descripcion=descripcion;
    }

    public String getPorDefecto()
    {
    	return porDefecto;
    }
    
    public void setPorDefecto(String porDefecto)
    {
    	this.porDefecto=porDefecto;
    }

    public FormFile getTheFile() 
    {
        return theFile;
    }

    public void setTheFile(FormFile theFile) 
    {
        this.theFile = theFile;
    }

    public String getDescripcionCertificado()
    {
    	return descripcionCertificado;
    }
    
    public void setDescripcionCertificado(String descripcionCertificado)
    {
    	this.descripcionCertificado=descripcionCertificado;
    }

	public String getRecarga() {
		return recarga;
	}

	public void setRecarga(String recarga) {
		this.recarga = recarga;
	}

	public String getNuevo() {
		return nuevo;
	}

	public void setNuevo(String nuevo) {
		this.nuevo = nuevo;
	}

	public String getRelacion() {
		return relacion;
	}

	public void setRelacion(String relacion) {
		this.relacion = relacion;
	}

	public String getIdRelacion() {
		return idRelacion;
	}

	public void setIdRelacion(String idRelacion) {
		this.idRelacion = idRelacion;
	}
    
}