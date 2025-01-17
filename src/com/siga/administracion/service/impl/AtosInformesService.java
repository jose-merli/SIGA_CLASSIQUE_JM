package com.siga.administracion.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.InformeForm;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmConsultaInformeAdm;
import com.siga.beans.AdmConsultaInformeBean;
import com.siga.beans.AdmEnvioInformeAdm;
import com.siga.beans.AdmEnvioInformeBean;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.EnvPlantillasEnviosAdm;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.FileInforme;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosInformesService extends JtaBusinessServiceTemplate 
	implements InformesService {
//	private static Logger log = Logger.getLogger(AtosInformesService.class);
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
		AdmEnvioInformeAdm envioInformeAdm = new AdmEnvioInformeAdm(usrBean);
		Hashtable tiposEnvioHashtable = new Hashtable();
		tiposEnvioHashtable.put(AdmEnvioInformeBean.C_IDINSTITUCION, informeVo.getIdInstitucion());
		tiposEnvioHashtable.put(AdmEnvioInformeBean.C_IDPLANTILLA, informeVo.getIdPlantilla());
		String[] claves = {AdmEnvioInformeBean.C_IDINSTITUCION,AdmEnvioInformeBean.C_IDPLANTILLA};
		envioInformeAdm.deleteDirect(tiposEnvioHashtable, claves);
		
		informeAdm.delete(informeVo);
		
		//Si se trata de la institucion 2000, se dejan los archivos asociados
		if(!isNombreFisicoComun && informeVo.getIdInstitucion()!=2000)
			eliminaFicherosAsociados(getDirectorio(informeForm),informeForm.getNombreFisico());
		
	}
	
	public String getDirectorio(InformeForm informeForm) throws SIGAException{
		StringBuffer directorio = null;
		
		if(informeForm.getClaseTipoInforme().equals(AdmTipoInformeBean.CLASETIPOINFORME_GENERICO)){
			if(informeForm.getIdTipoInforme().equalsIgnoreCase("CERPA")||informeForm.getIdTipoInforme().equalsIgnoreCase("FACJ2")){
				
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
				
				directorio.append(ClsConstants.FILE_SEP);
				
				
				
				
				
				
				
				
				
			} else{
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
			}
			
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
		
		AdmEnvioInformeAdm envioInformeAdm = new AdmEnvioInformeAdm(usrBean);
		String idTipoEnvios = informeForm.getIdTiposEnvio();
		if(idTipoEnvios!=null){
			String[] idTiposEnvio = idTipoEnvios.split("##");
			if(idTiposEnvio.length>0){
				for (int i = 0; i < idTiposEnvio.length; i++) {
					String idTipoEnvio = idTiposEnvio[i];
					
					if(!idTipoEnvio.equals("")){
						AdmEnvioInformeBean envioInformeBean = new AdmEnvioInformeBean();
						envioInformeBean.setIdInstitucion(informeVo.getIdInstitucion());
						envioInformeBean.setIdPlantilla(informeVo.getIdPlantilla());
						envioInformeBean.setIdTipoEnvios(idTipoEnvio);
						if(informeForm.getIdTipoEnvio()!=null &&informeForm.getIdTipoEnvio().equals(idTipoEnvio)){
							envioInformeBean.setDefecto(ClsConstants.DB_TRUE);
							if(informeForm.getIdPlantillaEnvio()!=null &&!informeForm.getIdPlantillaEnvio().equals(""))
								envioInformeBean.setIdPlantillaEnvioDef(informeForm.getIdPlantillaEnvio());
						}else{
							envioInformeBean.setDefecto(ClsConstants.DB_FALSE);
							
						}
						envioInformeAdm.insert(envioInformeBean);
					}else{
						break;
					}
					
					
				}
			}
		}
		
		
		
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
		
		//Por si estuviera relleno el CLOB plantilla
		if(informeOld.getPlantilla() != null && !informeOld.getPlantilla().equals("")){
			informeVo.setPlantilla(informeOld.getPlantilla());
		}
		
		informeAdm.updateDirect(informeVo);
		AdmEnvioInformeAdm envioInformeAdm = new AdmEnvioInformeAdm(usrBean);
		String idTipoEnvios = informeForm.getIdTiposEnvio();
		String[] idTiposEnvio = idTipoEnvios.split("##");
		
		Hashtable tiposEnvioHashtable = new Hashtable();
		tiposEnvioHashtable.put(AdmEnvioInformeBean.C_IDINSTITUCION, informeVo.getIdInstitucion());
		tiposEnvioHashtable.put(AdmEnvioInformeBean.C_IDPLANTILLA, informeVo.getIdPlantilla());
		String[] claves = {AdmEnvioInformeBean.C_IDINSTITUCION,AdmEnvioInformeBean.C_IDPLANTILLA};
		envioInformeAdm.deleteDirect(tiposEnvioHashtable, claves);
		if(idTiposEnvio.length>0){
			for (int i = 0; i < idTiposEnvio.length; i++) {
				String idTipoEnvio = idTiposEnvio[i];
				
				if(!idTipoEnvio.equals("")){
					AdmEnvioInformeBean envioInformeBean = new AdmEnvioInformeBean();
					envioInformeBean.setIdInstitucion(informeVo.getIdInstitucion());
					envioInformeBean.setIdPlantilla(informeVo.getIdPlantilla());
					envioInformeBean.setIdTipoEnvios(idTipoEnvio);
					if(informeForm.getIdTipoEnvio()!=null &&informeForm.getIdTipoEnvio().equals(idTipoEnvio)){
						envioInformeBean.setDefecto(ClsConstants.DB_TRUE);
						if(informeForm.getIdPlantillaEnvio()!=null &&!informeForm.getIdPlantillaEnvio().equals(""))
							envioInformeBean.setIdPlantillaEnvioDef(informeForm.getIdPlantillaEnvio());
					}else{
						envioInformeBean.setDefecto(ClsConstants.DB_FALSE);
						
					}
					envioInformeAdm.insert(envioInformeBean);
				}else{
					break;
				}
				
				
			}
		}
		
		
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
		
		ClsLogging.writeFileLog("Entramos uploadFile...",3);
   		FormFile theFile = informeForm.getTheFile();
   		
   			InputStream stream =null;
   			OutputStream bos = null;
   			try {
   			// InformeForm informeEdicion =  informeForm.getInformes().get(informeForm.getFilaSeleccionada());
   				ClsLogging.writeFileLog("Entramos uploadFile.getDirectorio...",3);
   	   			String directorio = getDirectorio(informeForm);
   	   			ClsLogging.writeFileLog("uploadFile.directorio..."+directorio,3);
   	   			
   	   			FileHelper.mkdirs(directorio);
   	   			ClsLogging.writeFileLog("uploadFile.inputStream...",3);
   				stream = theFile.getInputStream();
   				StringBuffer pathInforme = new StringBuffer(directorio);
   				int indiceExtension = theFile.getFileName().lastIndexOf(".");
   				ClsLogging.writeFileLog("uploadFile.indiceExtension..."+indiceExtension,3);
   				
   				ClsLogging.writeFileLog("uploadFile.Extension..."+theFile.getFileName().substring(indiceExtension),3);
   				
				if (theFile.getFileName().substring(indiceExtension).equals(".jpg")
						|| theFile.getFileName().substring(indiceExtension).equals(".bmp")
						|| theFile.getFileName().substring(indiceExtension).equals(".gif")
						|| theFile.getFileName().substring(indiceExtension).equals(".png")) {
					
					
					pathInforme.append(ClsConstants.FILE_SEP);
					pathInforme.append("recursos");
					pathInforme.append(ClsConstants.FILE_SEP);
					File directorioFile = new File(pathInforme.toString());
					if (directorio!=null && directorioFile.exists()) {
						directorioFile.mkdir();
					}
					
					pathInforme.append(theFile.getFileName());
					ClsLogging.writeFileLog("uploadFile.pathInforme..."+pathInforme,3);
	
				} else {
					pathInforme.append(ClsConstants.FILE_SEP);
					pathInforme.append(informeForm.getNombreFisico());
					pathInforme.append("_");
					pathInforme.append(informeForm.getLenguaje());
					// pathInforme.append("ES");
					if (indiceExtension != -1)
						pathInforme.append(theFile.getFileName().substring(indiceExtension));
					ClsLogging.writeFileLog("uploadFile.pathInforme..."+pathInforme,3);
				}
   				
   				String idIntitucionPropietario = informeForm.getIdInstitucion();
   				String  idInstitucionUsuario = informeForm.getUsrBean().getLocation();
   				//solo se tendra pemiso de borrado cuando sea de su propia intitucion o
   				
   				boolean isPermitidoBorrar =(idIntitucionPropietario.equals("0")&&idInstitucionUsuario.equals("2000"))
   						|| !idIntitucionPropietario.equals("0")&&idIntitucionPropietario.equals(idInstitucionUsuario);
   				ClsLogging.writeFileLog("uploadFile.isPermitidoBorrar..."+isPermitidoBorrar,3);
   				informeForm.getDirectorioFile().getFiles().add(new FileInforme(theFile.getFileName(),"","",new File(pathInforme.toString()),isPermitidoBorrar));
   				
   				ClsLogging.writeFileLog("uploadFile.getFiles().add..."+theFile.getFileName()+"   "+pathInforme.toString(),3);
   				
   				bos = new FileOutputStream(pathInforme.toString());
   	   			
   				ClsLogging.writeFileLog("uploadFile.bos"+pathInforme.toString(),3);
   	   			
   	   			int bytesRead = 0;
   	   			byte[] buffer = new byte[8192];

   	   			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) 
   	   			{
   	   				bos.write(buffer, 0, bytesRead);
   	   			}
   	   			
   	   			ClsLogging.writeFileLog("uploadFile.write.....",3);
   	   			
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
	    			
	    			ClsLogging.writeFileLog("uploadFile.FIN DEL METODO",3);
	    		}
		    	catch (Exception e) {
		    		ClsLogging.writeFileLog("uploadFile.Error..."+e,3);
		    	}
	    	}
   			
   			
	}
	
	public void duplicarFicherosAsociados(InformeForm form)throws  SIGAException{	
		try {
			String path = getDirectorio(form);
			String pathNuevo = path.replace("2000", form.getIdInstitucion());
			
			File directorioDestino = new File (pathNuevo);
//			if (!directorioFileNew.exists()) {
//				directorioFileNew.mkdir();
//			}
			
			FileInforme directorio = form.getDirectorioFile();
			File directorioOrigen= directorio.getFile();
			
			copiarDirectorio(directorioOrigen,directorioDestino,form);
			
			
			
			
			
			
//			if (directorioFileOld.isDirectory()) {				
//				String[] children = directorioFileOld.list();
//				FileInforme fileInforme = null;
//				for (int i = 0; i<children.length; i++) {
//					 if(children[i].indexOf(".")!=-1 && form.getNombreFisico().equalsIgnoreCase(children[i].substring(0, children[i].indexOf(".")-3))){
//						 File informeFile = new File(directorioFileOld, children[i]);
//						 informeFile.renameTo(new File(directorioFileNew,children[i]));
//						 informeFile.createNewFile();
//					 }
//				}
//			}

		} catch (Exception e) {
			throw new SIGAException("error.messages.io");
		}
	}	
	
	public void copiarDirectorio(File dirOrigen, File dirDestino,InformeForm form) throws Exception { 
		try {
			if (dirOrigen.isDirectory()) { 
				if (!dirDestino.exists())
					dirDestino.mkdir(); 
	 
				String[] hijos = dirOrigen.list(); 
				for (int i=0; i < hijos.length; i++) { 
					if(hijos[i].indexOf(".")!=-1 && form.getNombreFisico().equalsIgnoreCase(hijos[i].substring(0, hijos[i].indexOf(".")-3))){
						copiarDirectorio(new File(dirOrigen, hijos[i]),	new File(dirDestino, hijos[i]),form); 
					}
				} // end for
			} else { 
				copiar(dirOrigen, dirDestino); 
			} // end if
		} catch (Exception e) {
			throw e;
		} // end try
	} // end CopiarDirectorio
	 
	public void copiar(File dirOrigen, File dirDestino) throws Exception { 
	 
		InputStream in = new FileInputStream(dirOrigen); 
		OutputStream out = new FileOutputStream(dirDestino); 
	 
		byte[] buffer = new byte[1024];
		int len;
	 
		try {
			// recorrer el array de bytes y recomponerlo
			while ((len = in.read(buffer)) > 0) { 
				out.write(buffer, 0, len); 
			} // end while
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			in.close(); 
			out.close(); 
		} // end ty
	} // end Copiar
	 
	public void copiar(String dirOrigen, String dirDestino) throws Exception { 
		InputStream in = new FileInputStream(dirOrigen); 
		OutputStream out = new FileOutputStream(dirDestino); 
	 
		byte[] buffer = new byte[1024];
		int len;
	 
		try {
			// recorrer el array de bytes y recomponerlo
			while ((len = in.read(buffer)) > 0) { 
				out.write(buffer, 0, len); 
			} // end while
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			in.close(); 
			out.close(); 
		} // end ty
	} // end Copiar
	
	
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
	public List<EnvTipoEnviosBean> getTiposEnvio(List<String> excluidosList, UsrBean usrBean) throws ClsExceptions {
		EnvTipoEnviosAdm tipoEnviosAdm = new EnvTipoEnviosAdm(usrBean);
		List<EnvTipoEnviosBean> tipoEnviosBeans = tipoEnviosAdm.select(excluidosList,usrBean.getLanguage());
		return tipoEnviosBeans;
	}
	public List<EnvTipoEnviosBean> getTiposEnvioPermitidos(AdmInformeBean informeBean, UsrBean usrBean) throws ClsExceptions {
		EnvTipoEnviosAdm tipoEnviosAdm = new EnvTipoEnviosAdm(usrBean);
		List<EnvTipoEnviosBean> tipoEnviosBeans = tipoEnviosAdm.getEnviosPermitidos(informeBean,usrBean);
		return tipoEnviosBeans;
	}
	public List<EnvPlantillasEnviosBean> getPlantillasEnvio(String idTipoEnvio, String idInstitucion, UsrBean usrBean, String idPlantillaEnvioDefecto) throws ClsExceptions {
		EnvPlantillasEnviosAdm plantillasEnviosAdm = new EnvPlantillasEnviosAdm(usrBean);
		if(idPlantillaEnvioDefecto == null || idPlantillaEnvioDefecto.equals("")){
			idPlantillaEnvioDefecto = "-1";
		}
		List<EnvPlantillasEnviosBean> plantillasEnviosBeans = plantillasEnviosAdm.getPlantillasEnvio(idTipoEnvio,idInstitucion, idPlantillaEnvioDefecto);
		return plantillasEnviosBeans;
	}
	/* (non-Javadoc)
	 * @see com.siga.administracion.service.InformesService#getTiposInformeComision(com.atos.utils.UsrBean)
	 */
	@Override
	public List<AdmTipoInformeBean> getTiposInformeComisionMultiple(UsrBean usrBean)
			throws ClsExceptions {
		AdmTipoInformeAdm admTipoInforme = new AdmTipoInformeAdm(usrBean);
		List<AdmTipoInformeBean> tiposInformeList = admTipoInforme.getTiposInformeComisionMultiple(false);
		if(tiposInformeList==null){
			tiposInformeList = new Vector<AdmTipoInformeBean>();
		
		}
		return tiposInformeList;
	}
	public List<AdmTipoInformeBean> getTiposInformeIntitucionComisionMultiple(UsrBean usrBean)
			throws ClsExceptions {
		AdmTipoInformeAdm admTipoInforme = new AdmTipoInformeAdm(usrBean);
		List<AdmTipoInformeBean> tiposInformeList = admTipoInforme.getTiposInformeIntitucionComisionMultiple(true);
		if(tiposInformeList==null){
			tiposInformeList = new Vector<AdmTipoInformeBean>();
		
		}
		return tiposInformeList;
	}
	

}
