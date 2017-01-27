package com.siga.certificados.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.general.MasterForm;

public class SIGASolicitudesCertificadosForm extends MasterForm
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1524975115704111648L;
	//    private String modo="";
    private String fechaDesde="";
    private String fechaHasta="";
    private String fechaEmisionDesde="";
    private String fechaEmisionHasta="";
    private String fechaSolicitudDesde="";
    private String fechaSolicitudHasta="";
    private String estado="";
    private String tipoCertificado="";
    private String numeroCertificado="";
    private String CIFNIF="";
    private String nombre="";
    private String apellido1="";
    private String apellido2="";
    private String idsParaGenerarFicherosPDF="";
    private String idsParaFinalizar="";
    private String idsParaFacturar="";
    private String idsTemp="";
    private String buscarIdPeticionCompra;
    private String idInstitucionOrigen="";
    private String idInstitucionDestino="";
    private String idInstitucionSolicitud="";
    private String idInstitucionColegiacion="";
    private String idInstitucion="";
    private String idSerieSeleccionada = "";
    
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
    
    private String codigoBanco;
    private String sucursalBanco;
    private String bancoNombre;
    private String nidSolicitante;
    private String idPersonaSolicitante;
    private String idInstitucionOrigenSolicitante;    
    private String nColSolicitante;    
    private String idProductoCertificado;
    private String aceptaCesionMutualidad;
    private String cobrado;
    private String descargado;
    private String enviado;                                          //15-12-2015 valores "SI/NO/''" para indicar si queremos visualizar los certificados que tengan fecha de envio o no. 
    
    private String busquedaNombre;
    private String busquedaApellidos;
    private String busquedaNIF;
    private String busquedaNumCol;
    private String busquedaTipoCertificado;
    private String busquedaEstado;
    private String busquedaIdSolicitud;
    private String busquedaIdInstitucionDestino;
    private String busquedaIdInstitucionOrigen;
    
    private String regenerar;
    
    
    
	public String getRegenerar() {
		return regenerar;
	}
	public void setRegenerar(String regenerar) {
		this.regenerar = regenerar;
	}
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

	public void resetCamposSolicitud() {
		this.estado = "";
		this.tipoCertificado = "";
		this.numeroCertificado = "";
		this.CIFNIF = "";
		this.nombre = "";
		this.apellido1 = "";
		this.apellido2 = "";
		this.idsParaGenerarFicherosPDF = "";
		this.idsParaFinalizar = "";
		this.idsParaFacturar = "";
		this.idsTemp = "";
		this.idSerieSeleccionada = "";
		this.idInstitucionOrigen = "";
		this.idInstitucionDestino = "";
		this.idInstitucion = "";
		this.idSolicitud = "";
		this.fechaDescarga = "";
		this.fechaCobro = "";
		this.fechaEmision = "";
		this.fechaSolicitud = "";
		this.textoSanciones = "";
		this.comentario = "";
		this.observaciones = "";
		this.aceptaCesionMutualidad = "";
	}

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
    public String getIdsParaFacturar() {
		return idsParaFacturar;
	}
	public void setIdsParaFacturar(String idsParaFacturar) {
		this.idsParaFacturar = idsParaFacturar;
	}
	
	public String getIdSerieSeleccionada() {
		return idSerieSeleccionada;
	}
	public void setIdSerieSeleccionada(String idSerieSeleccionada) {
		this.idSerieSeleccionada = idSerieSeleccionada;
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
	public String getIdInstitucionColegiacion() {
		return idInstitucionColegiacion;
	}
	public void setIdInstitucionColegiacion(String idInstitucionColegiacion) {
		this.idInstitucionColegiacion = idInstitucionColegiacion;
	}
	public String getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getSucursalBanco() {
		return sucursalBanco;
	}
	public void setSucursalBanco(String sucursalBanco) {
		this.sucursalBanco = sucursalBanco;
	}
	public String getBancoNombre() {
		return bancoNombre;
	}
	public void setBancoNombre(String bancoNombre) {
		this.bancoNombre = bancoNombre;
	}
	public String getAceptaCesionMutualidad() {
		return aceptaCesionMutualidad;
	}
	public void setAceptaCesionMutualidad(String aceptaCesionMutualidad) {
		this.aceptaCesionMutualidad = aceptaCesionMutualidad;
	}
	public String getIdPersonaSolicitante() {
		return idPersonaSolicitante;
	}
	public void setIdPersonaSolicitante(String idPersonaSolicitante) {
		this.idPersonaSolicitante = idPersonaSolicitante;
	}
	public String getIdInstitucionOrigenSolicitante() {
		return idInstitucionOrigenSolicitante;
	}
	public void setIdInstitucionOrigenSolicitante(String idInstitucionOrigenSolicitante) {
		this.idInstitucionOrigenSolicitante = idInstitucionOrigenSolicitante;
	}
	public String getnColSolicitante() {
		return nColSolicitante;
	}
	public void setnColSolicitante(String nColSolicitante) {
		this.nColSolicitante = nColSolicitante;
	}
	public String getNidSolicitante() {
		return nidSolicitante;
	}
	public void setNidSolicitante(String nidSolicitante) {
		this.nidSolicitante = nidSolicitante;
	}
	public String getIdProductoCertificado() {
		return idProductoCertificado;
	}
	public void setIdProductoCertificado(String idProductoCertificado) {
		this.idProductoCertificado = idProductoCertificado;
	}
	public String getCobrado() {
		return cobrado;
	}
	public void setCobrado(String cobrado) {
		this.cobrado = cobrado;
	}
	public String getDescargado() {
		return descargado;
	}
	public void setDescargado(String descargado) {
		this.descargado = descargado;
	}
	public String getEnviado() {
		return enviado;
	}
	public void setEnviado(String enviado) {
		this.enviado = enviado;
	}
	public String getFechaSolicitudDesde() {
		return fechaSolicitudDesde;
	}
	public void setFechaSolicitudDesde(String fechaSolicitudDesde) {
		this.fechaSolicitudDesde = fechaSolicitudDesde;
	}
	public String getFechaSolicitudHasta() {
		return fechaSolicitudHasta;
	}
	public void setFechaSolicitudHasta(String fechaSolicitudHasta) {
		this.fechaSolicitudHasta = fechaSolicitudHasta;
	}
	public String getBusquedaNombre() {
		return busquedaNombre;
	}
	public void setBusquedaNombre(String busquedaNombre) {
		this.busquedaNombre = busquedaNombre;
	}
	public String getBusquedaApellidos() {
		return busquedaApellidos;
	}
	public void setBusquedaApellidos(String busquedaApellidos) {
		this.busquedaApellidos = busquedaApellidos;
	}
	public String getBusquedaNIF() {
		return busquedaNIF;
	}
	public void setBusquedaNIF(String busquedaNIF) {
		this.busquedaNIF = busquedaNIF;
	}
	public String getBusquedaNumCol() {
		return busquedaNumCol;
	}
	public void setBusquedaNumCol(String busquedaNumCol) {
		this.busquedaNumCol = busquedaNumCol;
	}
	public String getBusquedaTipoCertificado() {
		return busquedaTipoCertificado;
	}
	public void setBusquedaTipoCertificado(String busquedaTipoCertificado) {
		this.busquedaTipoCertificado = busquedaTipoCertificado;
	}
	public String getBusquedaEstado() {
		return busquedaEstado;
	}
	public void setBusquedaEstado(String busquedaEstado) {
		this.busquedaEstado = busquedaEstado;
	}
	public String getBusquedaIdSolicitud() {
		return busquedaIdSolicitud;
	}
	public void setBusquedaIdSolicitud(String busquedaIdSolicitud) {
		this.busquedaIdSolicitud = busquedaIdSolicitud;
	}
	public String getBusquedaIdInstitucionDestino() {
		return busquedaIdInstitucionDestino;
	}
	public void setBusquedaIdInstitucionDestino(String busquedaIdInstitucionDestino) {
		this.busquedaIdInstitucionDestino = busquedaIdInstitucionDestino;
	}

	public String getBusquedaIdInstitucionOrigen() {
		return busquedaIdInstitucionOrigen;
	}
	public void setBusquedaIdInstitucionOrigen(String busquedaIdInstitucionOrigen) {
		this.busquedaIdInstitucionOrigen = busquedaIdInstitucionOrigen;
	}
	
	public void resetCamposBusqueda() {
		this.cobrado = "";
		this.descargado = "";
		this.enviado = "";
		this.busquedaNombre = "";
		this.busquedaApellidos = "";
		this.busquedaNIF = "";
		this.busquedaNumCol = "";
		this.busquedaTipoCertificado = "";
		this.busquedaEstado = "";
		this.busquedaIdSolicitud = "";
		this.busquedaIdInstitucionDestino = "";		
		this.buscarNumCertificadoCompra = "";
		this.busquedaIdInstitucionOrigen = "";
	}
}