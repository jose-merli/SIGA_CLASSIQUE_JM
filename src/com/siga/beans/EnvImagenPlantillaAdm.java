package com.siga.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.envios.form.ImagenPlantillaForm;
import com.siga.general.SIGAException;

public class EnvImagenPlantillaAdm extends MasterBeanAdministrador
{
    
	public EnvImagenPlantillaAdm(UsrBean usuario)
	{
	    super(EnvImagenPlantillaBean.T_NOMBRETABLA, usuario);
	    
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvImagenPlantillaBean.C_IDINSTITUCION,
		        		   EnvImagenPlantillaBean.C_IDTIPOENVIOS,
		        		   EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS,
		        		   EnvImagenPlantillaBean.C_IDIMAGEN,
		        		   EnvImagenPlantillaBean.C_NOMBRE,
		        		   EnvImagenPlantillaBean.C_TIPOARCHIVO,
		        		   EnvImagenPlantillaBean.C_EMBEBED,
		        		   EnvImagenPlantillaBean.C_FECHAMODIFICACION, 
						   EnvImagenPlantillaBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvImagenPlantillaBean.C_IDINSTITUCION, EnvImagenPlantillaBean.C_IDTIPOENVIOS, EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS, EnvImagenPlantillaBean.C_IDIMAGEN};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvImagenPlantillaBean bean = null;

		try
		{
			bean = new EnvImagenPlantillaBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvImagenPlantillaBean.C_IDINSTITUCION));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvImagenPlantillaBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS));
			bean.setIdImagen(UtilidadesHash.getInteger(hash, EnvImagenPlantillaBean.C_IDIMAGEN));
			bean.setNombre(UtilidadesHash.getString(hash, EnvImagenPlantillaBean.C_NOMBRE));
			bean.setTipoArchivo(UtilidadesHash.getString(hash, EnvImagenPlantillaBean.C_TIPOARCHIVO));
			bean.setEmbebed(UtilidadesHash.getString(hash, EnvImagenPlantillaBean.C_EMBEBED));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			EnvImagenPlantillaBean b = (EnvImagenPlantillaBean) bean;

			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_IDIMAGEN, b.getIdImagen());
			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_TIPOARCHIVO, b.getTipoArchivo());
			UtilidadesHash.set(htData, EnvImagenPlantillaBean.C_EMBEBED, b.getEmbebed());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return new String[]{EnvImagenPlantillaBean.C_NOMBRE};
    }
    
    public List<ImagenPlantillaForm> getImagenes(EnvImagenPlantillaBean imagenBean) throws ClsExceptions
    {
    	Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
    	int contador = 0;
    		StringBuffer sWhere = new StringBuffer();
    		sWhere.append(" WHERE ");
    		sWhere.append(EnvImagenPlantillaBean.C_IDINSTITUCION);
    		sWhere.append("=:");
    		contador++;
    		htCodigos.put(new Integer(contador), imagenBean.getIdInstitucion());
    		sWhere.append(contador);
    		sWhere.append(" AND ");
    		sWhere.append(EnvImagenPlantillaBean.C_IDTIPOENVIOS);
    		sWhere.append("=:");
    		contador++;
    		htCodigos.put(new Integer(contador), imagenBean.getIdTipoEnvios());
    		sWhere.append(contador);
    		
    		sWhere.append(" AND ");
    		sWhere.append(EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS);
    		sWhere.append("=:");
    		contador++;
    		htCodigos.put(new Integer(contador), imagenBean.getIdPlantillaEnvios());
    		sWhere.append(contador);
    		
    		
    		Vector<EnvImagenPlantillaBean> vDatos;
			try {
				vDatos = selectBind(sWhere.toString(),htCodigos);
			} catch (ClsExceptions e) {
				throw e;
			}
    		List<ImagenPlantillaForm> lImagenes = new ArrayList<ImagenPlantillaForm>();
    		for(EnvImagenPlantillaBean auxImagenBean:vDatos){
    			lImagenes.add(auxImagenBean.getImagenPlantillaForm());
    		}
			return lImagenes;
    	
    }
    
   	public Integer getNuevoID(Hashtable entrada) throws ClsExceptions 
	{
		Integer sID=Integer.valueOf(0);
		
		try
		{
			RowsContainer rc = new RowsContainer();

			String sSQL="";
			
			sSQL = " SELECT MAX(" + EnvImagenPlantillaBean.C_IDIMAGEN + ") AS " + EnvImagenPlantillaBean.C_IDIMAGEN + 
				   " FROM " + nombreTabla +
				   " WHERE " + EnvImagenPlantillaBean.C_IDINSTITUCION + "=" + entrada.get(EnvImagenPlantillaBean.C_IDINSTITUCION) + " AND " +
				   			   EnvImagenPlantillaBean.C_IDTIPOENVIOS + "=" + entrada.get(EnvImagenPlantillaBean.C_IDTIPOENVIOS) + " AND " +
				   			   EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS + "=" + entrada.get(EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS);
		
			if (rc.findForUpdate(sSQL))
			{
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				
				String sIDAux = (String)ht.get(EnvImagenPlantillaBean.C_IDIMAGEN);
				
				if (sIDAux!=null && !sIDAux.equals(""))
				{
					sID = (Integer.parseInt(sIDAux)+1);
				}
			}
			
			return sID;
		}	

		catch (ClsExceptions e)
		{
			throw new ClsExceptions (e, "Error al obtener el MAX(IDPLANTILLA).");
		}
	}
   	
   	
  	  	public void insertar(EnvImagenPlantillaBean imagenPlantilla) throws SIGAException
   	{
   		FormFile theFile = imagenPlantilla.getTheFile();
   		ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
   		String MAX_SIZE_IMAGE_EMBEBED = properties.returnProperty("env.maxsizefileembebed");
   		if(theFile.getFileSize()>Integer.parseInt(MAX_SIZE_IMAGE_EMBEBED) && imagenPlantilla.isEmbebed()){
   			throw new SIGAException(UtilidadesString.getMensajeIdioma(this.usrbean, "envios.imagenes.aviso.bytesExcedidos")+ " "+MAX_SIZE_IMAGE_EMBEBED);
   		}
   			InputStream stream =null;
   			OutputStream bos = null;

   			//String sNombrePlantilla = form.getIdTipoEnvios() + "_" + form.getIdPlantillaEnvios();
   			String directorioImagen = imagenPlantilla.getDirectorioImagen(File.separator);

   			File fDirectorio = new File(directorioImagen);
   			if(!fDirectorio.exists())
   				fDirectorio.mkdirs();


   			try {
				stream = theFile.getInputStream();
				String pathImagen = imagenPlantilla.getPathImagen(directorioImagen,File.separator);
	   			bos = new FileOutputStream(pathImagen);
	   			int bytesRead = 0;
	   			byte[] buffer = new byte[8192];

	   			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) 
	   			{
	   				bos.write(buffer, 0, bytesRead);
	   			}

	   			//fImagen = new File(pathImagen);

			} catch (FileNotFoundException e) {
				throw new SIGAException("error.messages.fileNotFound");
			} catch (IOException e) {
				throw new SIGAException("error.messages.io");
			}finally {
	    		try {
	    			if(bos!=null)
	    				bos.close();
	    			if(stream!=null)
	    				stream.close();
	    		}
		    	catch (Exception e) {
		    		
		    	}
	    	}
   		try {
   			Integer idImagen = getNuevoID(this.beanToHashTable(imagenPlantilla));
   			imagenPlantilla.setIdImagen(idImagen);
			this.insert(imagenPlantilla);
		} catch (ClsExceptions e) {
			throw new SIGAException("messages.insert.error");
			
		}

   	}
  	  public File getImagen(EnvImagenPlantillaBean imagenPlantilla) throws SIGAException 
  	{
  		Hashtable<String,Object> htPk = new Hashtable<String, Object>();
   		htPk.put(EnvImagenPlantillaBean.C_IDINSTITUCION,imagenPlantilla.getIdInstitucion());
   		htPk.put(EnvImagenPlantillaBean.C_IDTIPOENVIOS,imagenPlantilla.getIdTipoEnvios());
   		htPk.put(EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS,imagenPlantilla.getIdPlantillaEnvios());
   		htPk.put(EnvImagenPlantillaBean.C_IDIMAGEN,imagenPlantilla.getIdImagen());

   		Vector vImagen;
   		try {
   			vImagen = this.selectByPK(htPk);
   		} catch (ClsExceptions e1) {
   			throw new SIGAException("messages.update.error");
   		}
   		EnvImagenPlantillaBean imagenBean =(EnvImagenPlantillaBean)vImagen.firstElement();
   		imagenPlantilla.setTipoArchivo(imagenBean.getTipoArchivo());
   		String directorioImagen = imagenBean.getDirectorioImagen(File.separator);
   		File fileImagen = new File(imagenBean.getPathImagen( directorioImagen,File.separator));
	        return fileImagen;
     	    
  	}
   	public void modificarImagen(EnvImagenPlantillaBean imagenPlantilla) throws SIGAException
   	{
   		Hashtable<String,Object> htPk = new Hashtable<String, Object>();
   		htPk.put(EnvImagenPlantillaBean.C_IDINSTITUCION,imagenPlantilla.getIdInstitucion());
   		htPk.put(EnvImagenPlantillaBean.C_IDTIPOENVIOS,imagenPlantilla.getIdTipoEnvios());
   		htPk.put(EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS,imagenPlantilla.getIdPlantillaEnvios());
   		htPk.put(EnvImagenPlantillaBean.C_IDIMAGEN,imagenPlantilla.getIdImagen());

   		Vector vImagen;
   		try {
   			vImagen = this.selectByPK(htPk);
   		} catch (ClsExceptions e1) {
   			throw new SIGAException("messages.update.error");
   		}
   		EnvImagenPlantillaBean imagenBean =(EnvImagenPlantillaBean)vImagen.firstElement();
   		imagenPlantilla.setTipoArchivo(imagenBean.getTipoArchivo());
   		String directorioImagen = imagenBean.getDirectorioImagen(File.separator);
   		File fileImagenOld = new File(imagenBean.getPathImagen( directorioImagen,File.separator));
   		fileImagenOld.renameTo(new File(imagenPlantilla.getPathImagen( directorioImagen,File.separator)));
   		try {
   			this.update(imagenPlantilla,imagenBean);
   		} catch (ClsExceptions e) {
   			throw new SIGAException("messages.update.error");

   		}

   	}
   	
	public void borrarImagen(EnvImagenPlantillaBean imagenPlantilla) throws SIGAException
   	{
		Hashtable<String,Object> htPk = new Hashtable<String, Object>();
   		htPk.put(EnvImagenPlantillaBean.C_IDINSTITUCION,imagenPlantilla.getIdInstitucion());
   		htPk.put(EnvImagenPlantillaBean.C_IDTIPOENVIOS,imagenPlantilla.getIdTipoEnvios());
   		htPk.put(EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS,imagenPlantilla.getIdPlantillaEnvios());
   		htPk.put(EnvImagenPlantillaBean.C_IDIMAGEN,imagenPlantilla.getIdImagen());

   		Vector vImagen;
   		try {
   			vImagen = this.selectByPK(htPk);
   		} catch (ClsExceptions e1) {

   			throw new SIGAException("messages.insert.error");
   		}

   		EnvImagenPlantillaBean imagenBean =(EnvImagenPlantillaBean)vImagen.firstElement();

  		String directorioImagen = imagenBean.getDirectorioImagen(File.separator);
   		File fileImagen = new File(imagenBean.getPathImagen(directorioImagen,File.separator));
   		fileImagen.delete();
   		try {
   			this.delete(imagenBean);
   		} catch (ClsExceptions e) {
   			throw new SIGAException("messages.delete.error");

   		}

   	}
	
   	
   	
 }