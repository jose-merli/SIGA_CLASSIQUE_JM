package com.siga.certificados.form;

import java.util.Hashtable;
import java.util.StringTokenizer;
import com.siga.general.MasterForm;

public class SIGAMantenimientoCertificadosCamposForm extends MasterForm
{
//    private String modo="";
    private String certificado="";
    private String editable="";
    private String idInstitucion="";
    private String idTipoProducto="";
    private String idProducto="";
    private String idProductoInstitucion="";
    
    private String idFormato="";
    private String tipoCampo="";
    private String valor="";
    private String idCampoCertificado="";
    
    private String descripcionCertificado="";
    public String filasSelect=null;
    public String getFilasSelect() {
		return filasSelect;
	}

	public void setFilasSelect(String filasSelect) {
		this.filasSelect = filasSelect;
	}

	public Hashtable certificados= null;

//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public Hashtable getCertificados() {
		return certificados;
	}

	public void setCertificados(Hashtable certificados) {
		this.certificados = certificados;
	}

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

    public String getIdFormato()
    {
    	return idFormato;
    }
    
    public void setIdFormato(String idFormato)
    {
        try
        {
	        StringTokenizer st = new StringTokenizer(idFormato, ",");
	        
	    	this.idFormato=st.nextToken();
        }
	    	
	    catch(Exception e)
	    {
	        this.idFormato="";
	    }
    }

    public String getTipoCampo()
    {
    	return tipoCampo; 
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        try
        {
	        StringTokenizer st = new StringTokenizer(tipoCampo, ",");
	        
	    	this.tipoCampo=st.nextToken();
        }
        
        catch(Exception e)
        {
            this.tipoCampo="";
        }
    }

    public String getValor()
    {
    	return valor;
    }
    
    public void setValor(String valor)
    {
    	this.valor=valor;
    }

    public String getIdCampoCertificado()
    {
    	return idCampoCertificado;
    }
    
    public void setIdCampoCertificado(String idCampoCertificado)
    {
        if (idCampoCertificado.indexOf(",")>-1)
        {
            StringTokenizer st = new StringTokenizer(idCampoCertificado, ",");
        
            st.nextToken();
        
            this.idCampoCertificado=st.nextToken();
        }
        
        else
        {
            this.idCampoCertificado=idCampoCertificado;
        }
    }
    
    public String getDescripcionCertificado()
    {
    	return descripcionCertificado;
    }
    
    public void setDescripcionCertificado(String descripcionCertificado)
    {
    	this.descripcionCertificado=descripcionCertificado;
    }

}