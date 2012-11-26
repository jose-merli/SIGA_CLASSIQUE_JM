package com.siga.beans;



import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.siga.envios.form.ImagenPlantillaForm;

public class EnvImagenPlantillaBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private Integer idImagen;
	private String nombre;
	private String tipoArchivo;
	private FormFile theFile;
	EnvPlantillasEnviosBean plantillaEnvios = null;
	private String embebed;
	

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_IDIMAGEN = "IDIMAGEN";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_TIPOARCHIVO = "TIPOARCHIVO";
	static public final String C_EMBEBED = "EMBEBED";
	static public final String T_NOMBRETABLA = "ENV_IMAGENPLANTILLA";

	
	public ImagenPlantillaForm getImagenPlantillaForm(){
		ImagenPlantillaForm imagenForm = new ImagenPlantillaForm();
		imagenForm.setIdInstitucion(idInstitucion.toString());
		imagenForm.setIdTipoEnvios(idTipoEnvios.toString());
		imagenForm.setIdPlantillaEnvios(idPlantillaEnvios.toString());
		imagenForm.setIdImagen(idImagen.toString());
		imagenForm.setNombre(nombre);
		imagenForm.setTipoArchivo(tipoArchivo);
		imagenForm.setTheFile(theFile);
		imagenForm.setEmbebed(embebed!=null && embebed.equals(ClsConstants.DB_TRUE));
		return imagenForm;
		
	}
	
	// Métodos SET
    public Integer getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
    
    public Integer getIdTipoEnvios()
    {
        return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(Integer idTipoEnvios)
    {
        this.idTipoEnvios = idTipoEnvios;
    }

    public Integer getIdPlantillaEnvios()
    {
        return idPlantillaEnvios;
    }
    
    public void setIdPlantillaEnvios(Integer idPlantillaEnvios)
    {
        this.idPlantillaEnvios = idPlantillaEnvios;
    }

	public String getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	public Integer getIdImagen() {
		return idImagen;
	}

	public void setIdImagen(Integer idImagen) {
		this.idImagen = idImagen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public FormFile getTheFile() {
		return theFile;
	}

	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	public EnvPlantillasEnviosBean getPlantillaEnvios() {
		return plantillaEnvios;
	}

	public void setPlantillaEnvios(EnvPlantillasEnviosBean plantillaEnvios) {
		this.plantillaEnvios = plantillaEnvios;
	}
	public String getNombreParseo(){
		StringBuffer nombreParseo = new StringBuffer();
		nombreParseo.append("IMG.");
		if(nombre!=null)
			nombreParseo.append(nombre);
		return nombreParseo.toString();
		
	}
	public String getImagenSrcExterna(String separador){
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String urlSIGAExterno = rp.returnProperty("general.urlSIGA.externo");
		StringBuffer imagenSrc = new StringBuffer();
		imagenSrc.append(urlSIGAExterno);
		imagenSrc.append(ClsConstants.PATH_ACCESO_DIRECTO_IMAGENES);
		imagenSrc.append(getCarpetaImagen(separador));
		imagenSrc.append(separador);
		if(nombre!=null)
			imagenSrc.append(nombre);
		imagenSrc.append(".");
		if(tipoArchivo!=null)
			imagenSrc.append(tipoArchivo);
		return imagenSrc.toString();
		
	}
	public String getImagenSrcEmbebida(String separador){
		StringBuffer imagenSrc = new StringBuffer();
		//imagenSrc.append("<img src=\"cid:");
		imagenSrc.append("cid:");
		if(nombre!=null)
			imagenSrc.append(nombre);

		//imagenSrc.append("\">");

		return imagenSrc.toString();
		
	}
	
	public String getPathImagenes()
	{
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathImagenes = rp.returnProperty("general.path.imagenes");
		
		return pathImagenes;
	}
	public String getDirectorioImagen(String separador){
   		StringBuffer sDirectorioImagen = new StringBuffer( getPathImagenes());
   		//sDirectorioImagen.append(separador);
   		sDirectorioImagen.append(getCarpetaImagen(separador));
   		return  sDirectorioImagen.toString();  		
   		
   	}
	public String getCarpetaImagen(String separador){
//		StringBuffer sDirectorioImagen = new StringBuffer( );
//   		sDirectorioImagen.append(File.separator);
//   		sDirectorioImagen.append(imagenPlantilla.getIdInstitucion());
//   		sDirectorioImagen.append(File.separator);
//   		sDirectorioImagen.append(imagenPlantilla.getIdTipoEnvios());
//   		sDirectorioImagen.append(File.separator);
//   		sDirectorioImagen.append(imagenPlantilla.getIdPlantillaEnvios());
		StringBuffer sDirectorioImagen = new StringBuffer( );
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String carpetaEnvios = rp.returnProperty("general.path.imagenes.envios");
		sDirectorioImagen.append(carpetaEnvios);
		sDirectorioImagen.append(separador);
		sDirectorioImagen.append(this.getIdInstitucion());
		sDirectorioImagen.append(separador);
		sDirectorioImagen.append(this.getIdTipoEnvios());
		sDirectorioImagen.append(separador);
		sDirectorioImagen.append(this.getIdPlantillaEnvios());
   		return  sDirectorioImagen.toString();
		
		
	}
   	public String getPathImagen(String directorioImagen,String separador){
   		StringBuffer sPathImagen = null;
   		if(directorioImagen==null)
   			sPathImagen = new StringBuffer(getDirectorioImagen(separador));
   		else
   			sPathImagen = new StringBuffer(directorioImagen);
//   		sPathImagen.append(File.separator);
//   		sPathImagen.append(imagenPlantilla.getNombre());
//   		sPathImagen.append(".");
//   		sPathImagen.append(imagenPlantilla.getTipoArchivo());
   		sPathImagen.append(separador);
   		sPathImagen.append(this.getNombre());
   		sPathImagen.append(".");
   		sPathImagen.append(this.getTipoArchivo());
   		
   		return  sPathImagen.toString();  		
   		
   	}

	public String getEmbebed() {
		return embebed;
	}

	public void setEmbebed(String embebed) {
		this.embebed = embebed;
	}
	public boolean isEmbebed(){
		return embebed!=null && embebed.equals(ClsConstants.DB_TRUE);
		
	}

	
   	
	
	
}