package com.siga.certificados.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


import com.siga.general.MasterForm;

public class SIGASolicitudesCertificadosForm extends MasterForm
{
//    private String modo="";
    private String fechaDesde="";
    private String fechaHasta="";
    private String fechaEmisionDesde="";
    private String fechaEmisionHasta="";
    private String estado="";
    private String tipoCertificado="";
    private String numeroCertificado="";
    private String CIFNIF="";
    private String nombre="";
    private String apellido1="";
    private String apellido2="";
    private String idsParaGenerarFicherosPDF="";
    private String idsParaFinalizar="";
    private String idsTemp="";
    private String buscarIdPeticionCompra;
    private String idInstitucionOrigen="";
    private String idInstitucionDestino="";
    private String idInstitucionSolicitud="";
    private String idInstitucion="";
    
    private String observaciones="";

    private String fechaEntregaInfo="";
    private String fechaDescarga="";
    private String fechaCobro="";
    private String fechaEmision="";
    private String fechaSolicitud="";
    private String textoSanciones="";
    private String comentario="";
    private String buscarNumCertificadoCompra="",buscarIdSolicitudCertif="";
    
    private String incluirSanciones;
    private String incluirDeudas;
    private String incluirLiteratura;
    
    private String idSolicitud="";
    private String idTipoProducto="";
    private String idProducto="";
    private String idProductoInstitucion="";
    private String metodoSolicitud="";
    
        
	public String getIdTipoProducto() {
		return idTipoProducto;
	}
	public void setIdTipoProducto(String idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}
	public String getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	public String getIdProductoInstitucion() {
		return idProductoInstitucion;
	}
	public void setIdProductoInstitucion(String idProductoInstitucion) {
		this.idProductoInstitucion = idProductoInstitucion;
	}
	public String getIncluirSanciones() {
		return incluirSanciones;
	}
	public void setIncluirSanciones(String incluirSanciones) {
		this.incluirSanciones = incluirSanciones;
	}
	public String getIncluirDeudas() {
		return incluirDeudas;
	}
	public void setIncluirDeudas(String incluirDeudas) {
		this.incluirDeudas = incluirDeudas;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{
		
		super.reset(mapping, request);
		String noreset=(String)request.getParameter("noReset");
		if (noreset==null||(noreset!=null&&!noreset.equalsIgnoreCase("true"))){
		
			resetCamposSolicitud();
		}
	}
	public void resetCamposSolicitud() 
	{
		this.fechaDesde="";
	    this.fechaHasta="";
	    this.fechaEmisionDesde="";
	    this.fechaEmisionHasta="";
	    this.estado="";
	    this.tipoCertificado="";
	    this.numeroCertificado="";
	    this.CIFNIF="";
	    this.nombre="";
	    this.apellido1="";
	    this.apellido2="";
	    this.idsParaGenerarFicherosPDF="";
	    this.idsParaFinalizar="";
	    this.idsTemp="";
	    this.buscarIdPeticionCompra="";
	    this.idInstitucionOrigen="";
	    this.idInstitucionDestino="";
	    this.idInstitucion="";
	    this.idSolicitud="";
	    this.fechaDescarga="";
	    this.fechaCobro="";
	    this.fechaEmision="";
	    this.fechaSolicitud="";
	    this.textoSanciones="";
	    this.buscarNumCertificadoCompra="";
	    this.buscarIdSolicitudCertif="";
	    this.comentario="";
	    this.observaciones="";
	
	}
//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public String getFechaDesde()
    {
    	return fechaDesde;
    }
    
    public void setFechaDesde(String fechaDesde)
    {
    	this.fechaDesde=fechaDesde;
    }
    
    public String getFechaHasta()
    {
    	return fechaHasta;
    }
    
    public void setFechaHasta(String fechaHasta)
    {
    	this.fechaHasta=fechaHasta;
    }
    
    public String getFechaEmisionDesde()
    {
    	return fechaEmisionDesde;
    }
    
    public void setFechaEmisionDesde(String fechaDesde)
    {
    	this.fechaEmisionDesde=fechaDesde;
    }
    
    public String getFechaEmisionHasta()
    {
    	return fechaEmisionHasta;
    }
    
    public void setFechaEmisionHasta(String fechaHasta)
    {
    	this.fechaEmisionHasta=fechaHasta;
    }

    public String getEstado()
    {
    	return estado;
    }
    
    public void setEstado(String estado)
    {
    	this.estado=estado;
    }

    public String getTipoCertificado()
    {
    	return tipoCertificado;
    }
    
    public void setTipoCertificado(String tipoCertificado)
    {
    	this.tipoCertificado=tipoCertificado;
    }

    public String getNumeroCertificado()
    {
    	return numeroCertificado;
    }
    
    public void setNumeroCertificado(String numeroCertificado)
    {
    	this.numeroCertificado=numeroCertificado;
    }

    public String getCIFNIF()
    {
    	return CIFNIF;
    }
    
    public void setCIFNIF(String CIFNIF)
    {
    	this.CIFNIF=CIFNIF;
    }

    public String getNombre()
    {
    	return nombre;
    	//return UtilidadesHash.getString(this.datos, "NombrePersona");		
    }
    
    public void setNombre(String nombre)
    {
    	this.nombre=nombre;
    	/*try {
 			UtilidadesHash.set(this.datos,"NombrePersona", nombre);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}*/
    }

    public String getApellido1()
    {
    	return apellido1;
    }
    
    public void setApellido1(String apellido1)
    {
    	this.apellido1=apellido1;
    }

    public String getApellido2()
    {
    	return apellido2;
    }
    
    public void setApellido2(String apellido2)
    {
    	this.apellido2=apellido2;
    }
    
    public String getIdsParaGenerarFicherosPDF()
    {
    	return idsParaGenerarFicherosPDF;
    }
    
    public void setIdsParaGenerarFicherosPDF(String idsParaGenerarFicherosPDF)
    {
    	this.idsParaGenerarFicherosPDF=idsParaGenerarFicherosPDF;
    }

    public String getIdsParaFinalizar()
    {
    	return idsParaFinalizar;
    }
    
    public void setIdsParaFinalizar(String idsParaFinalizar)
    {
    	this.idsParaFinalizar=idsParaFinalizar;
    }

    public String getIdsTemp()
    {
    	return idsTemp;
    }
    
    public void setIdsTemp(String idsTemp)
    {
    	this.idsTemp=idsTemp;
    }
    
    public String getBuscarIdPeticionCompra()
    {
    	return buscarIdPeticionCompra;
    }
    
    public void setBuscarIdPeticionCompra(String valor)
    {
    	this.buscarIdPeticionCompra=valor;
    }

    public String getBuscarIdSolicitudCertif()
    {
    	return buscarIdSolicitudCertif;
    }
    
    public void setBuscarIdSolicitudCertif(String valor)
    {
    	this.buscarIdSolicitudCertif=valor;
    }

    public String getIdInstitucionOrigen()
    {
    	return idInstitucionOrigen;
    }
    
    public void setIdInstitucionOrigen(String id)
    {
    	this.idInstitucionOrigen=id;
    }
    public String getIdInstitucionDestino()
    {
    	return idInstitucionDestino;
    }
    
    public void setIdInstitucionDestino(String id)
    {
    	this.idInstitucionDestino=id;
    }

    public String getFechaDescarga()
    {
    	return fechaDescarga;
    }
    
    public void setFechaDescarga(String fechaDescarga)
    {
    	this.fechaDescarga=fechaDescarga;
    }

    public String getFechaCobro()
    {
    	return fechaCobro;
    }
    
    public void setFechaCobro(String fechaCobro)
    {
    	this.fechaCobro=fechaCobro;
    }

    public String getFechaEmision()
    {
    	return fechaEmision;
    }
    
    public void setFechaEmision(String fechaEmision)
    {
    	this.fechaEmision=fechaEmision;
    }

    public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public String getIdInstitucion()
    {
    	return idInstitucion;
    }
    
    public void setIdInstitucion(String id)
    {
    	this.idInstitucion=id;
    }
    public String getIdSolicitud()
    {
    	return idSolicitud;
    }
    
    public void setIdSolicitud(String id)
    {
    	this.idSolicitud=id;
    }
    public String getObservaciones()
    {
    	return observaciones;
    	
    }
    public void setObservaciones(String observaciones)
    {
    	this.observaciones=observaciones;
    	
    }
    
    
    public String getTextoSanciones()
    {
    	return textoSanciones;
    }
    
    public void setTextoSanciones(String tx)
    {
    	this.textoSanciones=tx;
    }
    
    public String getComentario()
    {
    	return comentario;
    }
    
    public void setComentario(String comentario)
    {
    	this.comentario=comentario;
    }
    
    public String getBuscarNumCertificadoCompra()
    {
    	return buscarNumCertificadoCompra;
    }
    
    public void setBuscarNumCertificadoCompra(String tx)
    {
    	this.buscarNumCertificadoCompra=tx;
    }
	public String getFechaEntregaInfo() {
		return fechaEntregaInfo;
	}
	public void setFechaEntregaInfo(String fechaEntregaInfo) {
		this.fechaEntregaInfo = fechaEntregaInfo;
	}
	
	public String getIncluirLiteratura() {
		return incluirLiteratura;
	}
	public void setIncluirLiteratura(String incluirLiteratura) {
		this.incluirLiteratura = incluirLiteratura;
	}
	public String getIdInstitucionSolicitud() {
		return idInstitucionSolicitud;
	}
	public void setIdInstitucionSolicitud(String idInstitucionSolicitud) {
		this.idInstitucionSolicitud = idInstitucionSolicitud;
	}
    public String getMetodoSolicitud() {
    	return metodoSolicitud;
    }
	public void setMetodoSolicitud(String metodo) {
        this.metodoSolicitud = metodo;
    }
	
}