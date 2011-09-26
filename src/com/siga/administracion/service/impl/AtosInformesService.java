package com.siga.administracion.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.InformeForm;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmConsultaInformeAdm;
import com.siga.beans.AdmConsultaInformeBean;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.FileInforme;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AsistenciaForm;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosInformesService extends JtaBusinessServiceTemplate 
	implements InformesService {

	public AtosInformesService(BusinessManager businessManager) {
		super(businessManager);
	}
	public Object executeService(Object... parameters) throws BusinessException {
		return null;
	}
	/**
	 * Me inserta
	 */
	public Object executeService(AdmInformeBean informeVo)
			throws SIGAException, ClsExceptions {
		return null;
	}
	public List<InformeForm> getInformes(InformeForm informeForm,String idInstitucion,
			UsrBean usrBean) throws ClsExceptions {
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		List<InformeForm> lista = informeAdm.getInformes(informeForm,idInstitucion);
		return lista;
	}
	
	public AdmInformeBean getInforme(InformeForm informeForm, UsrBean usrBean)throws ClsExceptions {
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		String idInstitucion = informeForm.getIdInstitucion();
		AdmInformeBean informeVo = informeAdm.obtenerInforme(idInstitucion,informeForm.getIdPlantilla());
		return informeVo;
	}
	public void borrarInforme(InformeForm informeForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		AdmInformeBean informeVo = informeForm.getInformeVO();
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		boolean isNombreFisicoComun = isNombreFisicoComun(informeForm,usrBean);
		informeAdm.delete(informeVo);
		if(!isNombreFisicoComun)
			eliminaFicherosAsociados(getDirectorio(informeForm),informeForm.getNombreFisico());
		
	}
	
	public String getDirectorio(InformeForm informeForm) throws SIGAException{
		StringBuffer directorio = null;
		
		if(informeForm.getClaseTipoInforme().equals(AdmTipoInformeBean.CLASETIPOINFORME_GENERICO)){
			ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			final String directorioFisicoPlantillaInformesJava = rp3.returnProperty("informes.directorioFisicoPlantillaInformesJava");
			final String directorioPlantillaInformesJava = rp3.returnProperty("informes.directorioPlantillaInformesJava");
			
			directorio = new StringBuffer(directorioFisicoPlantillaInformesJava);
			directorio.append(directorioPlantillaInformesJava);
			directorio.append(ClsConstants.FILE_SEP);
//			directorio.append(informeForm.getIdInstitucion());
			if(informeForm.getIdInstitucion().equals("0")){
				directorio.append("2000");
			}else{
				directorio.append(informeForm.getIdInstitucion());
			}
			
			directorio.append(ClsConstants.FILE_SEP);
			
			directorio.append(informeForm.getDirectorio());
			
		}else if(informeForm.getClaseTipoInforme().equals(AdmTipoInformeBean.CLASETIPOINFORME_PERSONALIZABLE)||informeForm.getClaseTipoInforme().equals(AdmTipoInformeBean.CLASETIPOINFORME_CONSULTAS)){
			ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			final String directorioFisicoPlantillaInformesJava = rp3.returnProperty("informes.directorioFisicoPlantillaInformesJava");
			directorio = new StringBuffer(directorioFisicoPlantillaInformesJava);
			directorio.append(ClsConstants.FILE_SEP);
			directorio.append(informeForm.getDirectorio());
			directorio.append(ClsConstants.FILE_SEP);
//			directorio.append(informeForm.getIdInstitucion());
			if(informeForm.getIdInstitucion().equals("0")){
				directorio.append("2000");
			}else{
				directorio.append(informeForm.getIdInstitucion());
			}
			
			
			
		}else{
			throw new SIGAException("messages.enProceso");
			
		}
			
		
		return directorio.toString();
		
	}
	private void formatearInforme(InformeForm informe)throws SIGAException{
		sustituyeEspacios(informe);
		
		boolean validar = UtilidadesString.validarAlfaNumericoYGuiones(informe.getNombreSalida());				
		if (!validar)
    		throw new SIGAException ("administracion.informes.mensaje.aviso.caracteresAlfaNumericos");    			
	}
	
	private void sustituyeEspacios(InformeForm informe){
		
		informe.setNombreFisico(UtilidadesString.replaceAllIgnoreCase(informe.getNombreFisico(), " ", "_")); 
		informe.setNombreSalida(UtilidadesString.replaceAllIgnoreCase(informe.getNombreSalida(), " ", "_"));
		//informe.setDirectorio(UtilidadesString.replaceAllIgnoreCase(informe.getDirectorio(), " ", "_"));
		//informe.setIdPlantilla(UtilidadesString.replaceAllIgnoreCase(informe.getIdPlantilla(), " ", "_"));
	}
	
	public void insertaInforme(InformeForm informeForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		formatearInforme(informeForm);
		AdmInformeBean informeVo = informeForm.getInformeVO();
		 
		
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		Long idPlantilla = informeAdm.getNewIdPlantilla();
		informeVo.setIdPlantilla(idPlantilla.toString());
		informeAdm.insert(informeVo);
		informeForm.setIdPlantilla(idPlantilla.toString());
		creaDirectorio(getDirectorio(informeForm));
 		if(informeForm.getIdTipoInforme().equals(AdmTipoInformeBean.TIPOINFORME_CONSULTAS)){
			AdmConsultaInformeAdm consultaInformeAdm = new AdmConsultaInformeAdm(usrBean);
			Hashtable consultaHashtable = new Hashtable();
			consultaHashtable.put(AdmConsultaInformeBean.C_IDINSTITUCION, informeForm.getIdInstitucion());
			consultaHashtable.put(AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA, informeForm.getIdInstitucionConsulta());
			consultaHashtable.put(AdmConsultaInformeBean.C_IDCONSULTA, informeForm.getIdConsulta());
			consultaHashtable.put(AdmConsultaInformeBean.C_IDPLANTILLA, informeForm.getIdPlantilla());
			consultaHashtable.put(AdmConsultaInformeBean.C_VARIASLINEAS, ClsConstants.DB_TRUE);
			consultaHashtable.put(AdmConsultaInformeBean.C_NOMBRE, informeForm.getASolicitantes());
			consultaInformeAdm.insert(consultaHashtable);
			//Hay que insertar en admconsultainforme
			
		}
		
		
	}
	public void modificarInforme(InformeForm informeForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		formatearInforme(informeForm);
		AdmInformeBean informeVo = informeForm.getInformeVO();
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		Hashtable informePKHashtable =  new Hashtable();
		informePKHashtable.put(AdmInformeBean.C_IDINSTITUCION, informeVo.getIdInstitucion());
		informePKHashtable.put(AdmInformeBean.C_IDPLANTILLA, informeVo.getIdPlantilla());
		Vector informeOldVector = informeAdm.selectByPK(informePKHashtable);
		AdmInformeBean informeOld = (AdmInformeBean) informeOldVector.get(0);
		//Si hay cambio de directorio y nombre fisico, habra que camb iar todos los archivos
		//que hubiera en el directorio a su nombre actual
		
		InformeForm informeFormOld =informeOld.getInforme(); 
		informeFormOld.setClaseTipoInforme(informeForm.getClaseTipoInforme());
		if(!informeOld.getNombreFisico().equals(informeVo.getNombreFisico())){
			cambiaNombreFiles(getDirectorio(informeForm),informeOld.getNombreFisico(),informeVo.getNombreFisico());
		}
		
//		if(!informeOld.getDirectorio().equals(informeVo.getDirectorio())){
//			cambiaDirectorio(getDirectorio(informeFormOld),getDirectorio(informeForm));
//		}
//		
		informeAdm.updateDirect(informeVo);
	}
	private void cambiaNombreFiles(String pathDirectorio, String nombreFisicoOld, String nombreFisico){	
		File directorioFile = new File (pathDirectorio);
		if (directorioFile.isDirectory()) {
			String[] children = directorioFile.list();
			for (int i = 0; i<children.length; i++) {
				String childName = children[i];
			    File informeFile = new File(directorioFile, childName);			    		    
			    if(childName.indexOf(".")!=-1 && nombreFisicoOld.equalsIgnoreCase(childName.substring(0, childName.indexOf(".")-3))){
			    	String childNewName =   UtilidadesString.replaceAllIgnoreCase(childName,nombreFisicoOld,nombreFisico);	
			    	informeFile.renameTo(new File(directorioFile,childNewName));		
			    	int p=9;
			    }
		    }
			
		}
	}
	private void eliminaDirectorio(String pathDirectorio){	
		File directorioFile = new File (pathDirectorio);
		directorioFile.delete();
	}
	
	private void eliminaFicherosAsociados(String pathDirectorio, String nombreFisico){	
		File directorioFile = new File (pathDirectorio);
		if (directorioFile.isDirectory()) {				
			String[] children = directorioFile.list();
			FileInforme fileInforme = null;
			for (int i = 0; i<children.length; i++) {
			    File informeFile = new File(directorioFile, children[i]);	
			    if(informeFile.getName().indexOf(".")!=-1 && nombreFisico.equalsIgnoreCase(informeFile.getName().substring(0, informeFile.getName().indexOf(".")-3))){
			     	informeFile.delete();
			    }
			}
		}			    	
	}
	
	private void creaDirectorio(String pathDirectorio){	
		
		File directorioFile = new File (pathDirectorio);
		if(!directorioFile.exists())
			directorioFile.mkdir();
	}
	private void cambiaDirectorio(String pathDirectorioOld,String pathDirectorio){	
		File directorioOldFile = new File (pathDirectorioOld);
		File directorioFile = new File (pathDirectorio);
		if (!directorioFile.exists()) {
			directorioFile.mkdir();
		}
		if (directorioOldFile.isDirectory()) {
			String[] children = directorioOldFile.list();
			for (int i = 0; i<children.length; i++) {
			    File informeFile = new File(directorioOldFile, children[i]);
			    informeFile.renameTo(new File(directorioFile,children[i]));
		    }
			directorioOldFile.delete();
		}
	}
		

	
	public List<AdmTipoInformeBean> getTiposInforme(UsrBean usrBean) throws ClsExceptions {
		AdmTipoInformeAdm admTipoInforme = new AdmTipoInformeAdm(usrBean);
		List<AdmTipoInformeBean> tiposInformeList = admTipoInforme.getTiposInforme(true);
		if(tiposInformeList==null){
			tiposInformeList = new Vector<AdmTipoInformeBean>();
		
		}
		return tiposInformeList;
	}
	public List<CenInstitucionBean> getInstitucionesInformes(Integer idInstitucion, UsrBean usrBean)
			throws ClsExceptions {
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usrBean);
		List<CenInstitucionBean> institucionesList = admInstitucion.getInstitucionesInformes(idInstitucion,true);
		if(institucionesList==null){
			institucionesList = new ArrayList<CenInstitucionBean>();
		
		}
		return institucionesList;
	}
	public List<AdmLenguajesBean> getLenguajes(UsrBean usrBean) throws ClsExceptions{
		AdmLenguajesAdm admLenguajesAdm = new AdmLenguajesAdm(usrBean);
		List<AdmLenguajesBean> lenguajesList = admLenguajesAdm.select("");
		return lenguajesList;
		
	}
	public FileInforme getFileDirectorio(InformeForm informeForm,boolean isPermitidoBorrar)
			throws ClsExceptions, SIGAException {
		
		return getFileDirectorio(getDirectorio(informeForm),informeForm.getNombreFisico(),isPermitidoBorrar);
	}
	private FileInforme getFileDirectorio(String path,String nombreFisico,boolean isPermitidoBorrar) throws ClsExceptions 
	{
     	if (path == null || path.equals("")){
			return null;
		} 

    	File directorioFile = new File (path);
		if (!directorioFile.exists()) { 
			
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<FileInforme> filesDirectorio = new ArrayList<FileInforme>();
		
		Date directorioDate = new Date(directorioFile.lastModified());
		String directorioFecha = sdf.format(directorioDate);

		StringBuffer premisoDirectorio = new StringBuffer("");
		premisoDirectorio.append(directorioFile.canRead()?"+r":"-r");
		premisoDirectorio.append(directorioFile.canWrite()?"+w":"+w");
		FileInforme directorioInforme = new FileInforme(directorioFile.getName(),premisoDirectorio.toString(),directorioFecha,directorioFile,isPermitidoBorrar);
		
		if (directorioFile.isDirectory()) {
				
			String[] children = directorioFile.list();
			FileInforme fileInforme = null;
			for (int i = 0; i<children.length; i++) {
			    File informeFile = new File(directorioFile, children[i]);
			    
			    if(informeFile.getName().indexOf(".")!=-1 && nombreFisico.equalsIgnoreCase(informeFile.getName().substring(0, informeFile.getName().indexOf(".")-3))){
			        StringBuffer permisoFile = new StringBuffer("");
					
				    permisoFile.append(informeFile.canRead()?"+r":"-r");
				    permisoFile.append(informeFile.canWrite()?"+w":"+w");
				    
				    Date fileInformeDate = new Date(informeFile.lastModified());
					String fechaFileInforme = sdf.format(fileInformeDate);
				    fileInforme = new FileInforme(informeFile.getName(),permisoFile.toString(),fechaFileInforme,informeFile,isPermitidoBorrar);
				    filesDirectorio.add(fileInforme);
				    
//			    }else if (informeFile.isDirectory() && informeFile.getName().equals("recursos")){
//			    	String[] childrenRecursos = informeFile.list();
//			    	FileInforme fileInformeRecursos = null;
//			    	for (int j = 0; j<childrenRecursos.length; j++) {
//			    		File informeRecursos = new File(informeFile, childrenRecursos[j]);
//				    	StringBuffer permisoFile = new StringBuffer("");
//					
//					    permisoFile.append(informeRecursos.canRead()?"+r":"-r");
//					    permisoFile.append(informeRecursos.canWrite()?"+w":"+w");
//					    
//					    Date fileInformeDate = new Date(informeRecursos.lastModified());
//						String fechaFileInforme = sdf.format(fileInformeDate);
//					    fileInformeRecursos = new FileInforme("/"+informeFile.getName()+"/"+informeRecursos.getName(),permisoFile.toString(),fechaFileInforme,informeRecursos,isPermitidoBorrar);
//					    filesDirectorio.add(fileInformeRecursos);
//		    		}
			    }
			}
		}
		directorioInforme.setFiles(filesDirectorio);
		return directorioInforme;
	}
	
	public void borrarInformeFile(InformeForm informeForm) throws SIGAException 
	{
		FileInforme fileInforme = informeForm.getDirectorioFile().getFiles().get(informeForm.getFilaInformeSeleccionada());
		File informeFile = fileInforme.getFile();
		if(informeFile==null || !informeFile.exists()){
			throw new SIGAException("error.messages.fileNotFound"); 
		}
		informeFile.delete();
	}
	public boolean isNombreFisicoUnico(InformeForm informeForm,boolean isInsertar, UsrBean usrBean) throws SIGAException, ClsExceptions 
	{
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		int numero = isInsertar ?0:1;
		boolean isNombreFisicoUnico = informeAdm.getInformesNombreFisicoComun(informeForm.getInformeVO())==numero;
		return isNombreFisicoUnico;
		
	}
	/**
	 * Comprueba si el nombre fisico se esta usando para mas de un informe. en ese caso lanza la excepcion 
	 */
	private boolean isNombreFisicoComun(InformeForm informeForm, UsrBean usrBean) throws ClsExceptions 
	{
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		boolean isNombreFisicoComun = informeAdm.getInformesNombreFisicoComun(informeForm.getInformeVO())>1;
		return isNombreFisicoComun;
		
	}
	public void uploadFile(InformeForm informeForm) throws  SIGAException{
		
   		FormFile theFile = informeForm.getTheFile();
   		
   			InputStream stream =null;
   			OutputStream bos = null;
   			try {
   			// InformeForm informeEdicion =  informeForm.getInformes().get(informeForm.getFilaSeleccionada());
   	   			String directorio = getDirectorio(informeForm);
   	   			File fDirectorio = new File(directorio);
   	   			if(!fDirectorio.exists())
   	   				fDirectorio.mkdirs();
   				stream = theFile.getInputStream();
   				StringBuffer pathInforme = new StringBuffer(directorio);
   				int indiceExtension = theFile.getFileName().lastIndexOf(".");
   				
				if (theFile.getFileName().substring(indiceExtension).equals(".jpg")
						|| theFile.getFileName().substring(indiceExtension).equals(".bmp")
						|| theFile.getFileName().substring(indiceExtension).equals(".gif")
						|| theFile.getFileName().substring(indiceExtension).equals(".png")) {
							
					pathInforme.append(ClsConstants.FILE_SEP);
					pathInforme.append("recursos");
					pathInforme.append(ClsConstants.FILE_SEP);
					File directorioFile = new File(pathInforme.toString());
					if (!directorioFile.exists()) {
						directorioFile.mkdir();
					}
					
					pathInforme.append(theFile.getFileName());
	
				} else {
					pathInforme.append(ClsConstants.FILE_SEP);
					pathInforme.append(informeForm.getNombreFisico());
					pathInforme.append("_");
					pathInforme.append(informeForm.getLenguaje());
					// pathInforme.append("ES");
					if (indiceExtension != -1)
						pathInforme.append(theFile.getFileName().substring(indiceExtension));
				}
   				
   				String idIntitucionPropietario = informeForm.getIdInstitucion();
   				String  idInstitucionUsuario = informeForm.getUsrBean().getLocation();
   				//solo se tendra pemiso de borrado cuando sea de su propia intitucion o
   				boolean isPermitidoBorrar =(idIntitucionPropietario.equals("0")&&idInstitucionUsuario.equals("2000"))
   						|| !idIntitucionPropietario.equals("0")&&idIntitucionPropietario.equals(idInstitucionUsuario);
   				informeForm.getDirectorioFile().getFiles().add(new FileInforme(theFile.getFileName(),"","",new File(pathInforme.toString()),isPermitidoBorrar));
   				bos = new FileOutputStream(pathInforme.toString());
   	   			
   	   			
   	   			int bytesRead = 0;
   	   			byte[] buffer = new byte[8192];

   	   			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) 
   	   			{
   	   				bos.write(buffer, 0, bytesRead);
   	   			}
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
   			
   			
	}
	public List<InformeForm> getInformesConsulta(ConConsultaBean consulta,
			InformeForm informeForm, UsrBean usrBean) throws ClsExceptions {
		AdmInformeAdm informeAdm = new AdmInformeAdm(usrBean);
		List<InformeForm> lista = informeAdm.getInformesConsulta(consulta, informeForm);
		return lista;
	}
	public void borrarConsultaInforme(AdmConsultaInformeBean consultaInforme,
			InformeForm informeForm, UsrBean usrBean) throws ClsExceptions,
			SIGAException {
		AdmConsultaInformeAdm admConsulta = new AdmConsultaInformeAdm(usrBean);
//		Hashtable<String, String> deleteHash = new Hashtable<String, String>();
//		deleteHash.put(AdmConsultaInformeBean.C_IDINSTITUCION, consultaInforme.getIdInstitucion().toString());
//		deleteHash.put(AdmConsultaInformeBean.C_IDPLANTILLA, consultaInforme.getIdPlantilla());
//		if(consultaInforme.getIdConsulta()!=null){
//			deleteHash.put(AdmConsultaInformeBean.C_IDCONSULTA, consultaInforme.getIdConsulta().toString());
//			deleteHash.put(AdmConsultaInformeBean.C_IDINSTITUCION_CONSULTA, consultaInforme.getIdInstitucion_consulta().toString());
//		}
		admConsulta.deleteConsultaInforme(consultaInforme);
		borrarInforme(informeForm, usrBean);

		
		
		
	}
	

}
