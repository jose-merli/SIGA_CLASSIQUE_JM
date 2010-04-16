package com.siga.envios.form;

import java.util.List;

import org.apache.struts.upload.FormFile;

import com.atos.utils.ClsConstants;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.EnvImagenPlantillaBean;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;

public class ImagenPlantillaForm extends MasterForm
{
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 6983292144179507196L;
	private final String txtEmbebedSi = "Sí";
	private final String txtEmbebedNo = "No";
    private String idInstitucion;
    private String idTipoEnvios;
    private String idPlantillaEnvios;
    private String idImagen;
    private String nombre;
    private FormFile theFile;
    EnvPlantillasEnviosBean plantillaEnvios = null;
    
    List<ImagenPlantillaForm> imagenes;
    
    
    private String tipoArchivo;
    private boolean editable;
    
    private String botones;
    private String botonesEdicion;
    private String botonesFila;
	FilaExtElement[] elementosFila;
    
	private boolean embebed;
	String txtEmbebed;
	private String volverAccion;
	private String volverModo;
	
	public String getBotonesEdicion() {
		if (isEditable()){
			this.botonesEdicion = "Y,C";
		}else{
			this.botonesEdicion = "C";	
			
		}
		
		return this.botonesEdicion;
	}
	
	public String getBotones() {
		
		if (isEditable()){
			this.botones = "N,V";
		}else{
			this.botones = "V";	
			
		}
		
		return this.botones;
	}
	
	public String getBotonesFila() {
		
		if (isEditable()){
			this.botonesFila = "E,B";
		}else{
			this.botonesFila = "";	
		}
		return this.botonesFila;
		
		
	}

	public FilaExtElement[] getElementosFila() {
		
//		if (isEditable()){
			elementosFila = new FilaExtElement[4];
			elementosFila[3] = new FilaExtElement("download", "download",	SIGAConstants.ACCESS_READ);
			
//		}else{
//			elementosFila = new FilaExtElement[3];
//		}
		return this.elementosFila;
	}

	

	public String getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	public String getIdImagen() {
		return idImagen;
	}

	public void setIdImagen(String idImagen) {
		this.idImagen = idImagen;
	}

	public String getIdInstitucion()
    {
    	return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion)
    {
    	this.idInstitucion=idInstitucion;
    }

   
    public String getIdTipoEnvios() {
		return idTipoEnvios;
	}

	public void setIdTipoEnvios(String idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}

	public String getIdPlantillaEnvios()
    {
    	return idPlantillaEnvios;
    }
    
    public void setIdPlantillaEnvios(String idPlantillaEnvios)
    {
    	this.idPlantillaEnvios=idPlantillaEnvios;
    }

    public FormFile getTheFile() 
    {
        return theFile;
    }

    public void setTheFile(FormFile theFile) 
    {
        this.theFile = theFile;
    }

   
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<ImagenPlantillaForm> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<ImagenPlantillaForm> imagenes) {
		this.imagenes = imagenes;
	}
	
	public EnvImagenPlantillaBean getImagenPlantillaBean(){
		EnvImagenPlantillaBean imagenBean = new EnvImagenPlantillaBean();
		imagenBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		imagenBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvios));
		imagenBean.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvios));
		if(idImagen!=null && !idImagen.equals(""))
			imagenBean.setIdImagen(Integer.valueOf(idImagen));
		if(nombre!=null)
			imagenBean.setNombre(nombre.toUpperCase());
		imagenBean.setTheFile(theFile);
		if(tipoArchivo==null && theFile!=null){
			String nombreArchivo = theFile.getFileName();
			String 	extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".")+1).toLowerCase();
			tipoArchivo = extension;
			imagenBean.setTipoArchivo(tipoArchivo);
			
		}else{
			imagenBean.setTipoArchivo(tipoArchivo);
			
		}
		imagenBean.setEmbebed(embebed?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE);
		return imagenBean;
		
	}

	public EnvPlantillasEnviosBean getPlantillaEnvios() {
		return plantillaEnvios;
	}

	public void setPlantillaEnvios(EnvPlantillasEnviosBean plantillaEnvios) {
		this.plantillaEnvios = plantillaEnvios;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isEmbebed() {
		return embebed;
	}

	public void setEmbebed(boolean embebed) {
		this.embebed = embebed;
	}
	public String getEmbebedTxt() {
		
		return (this.embebed)?txtEmbebedSi:txtEmbebedNo;
		
	}
	

	public String getVolverAccion() {
		return volverAccion;
	}

	public void setVolverAccion(String volverAccion) {
		this.volverAccion = volverAccion;
	}

	public String getVolverModo() {
		return volverModo;
	}

	public void setVolverModo(String volverModo) {
		this.volverModo = volverModo;
	}

	

	
	
}