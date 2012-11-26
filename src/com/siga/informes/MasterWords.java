package com.siga.informes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.SaveFormat;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.general.SIGAException;


/**
 * Descripcion: Clase que recibiendo un vector de datos, los añade a documentos doc en funcion del nombre 
 * de los campos que contenga dicho documento. 
 * @author Leandro
 */
public class MasterWords
{
	String pathPlantilla=null;
	Vector<Hashtable<String,String>> datos=null;

	/**
	 * Descripcion: Primer constructor que solo recibe un parametro
	 * @param _pathPlantilla: ruta donde se encuentra la plantilla
	 */
	public MasterWords(String _pathPlantilla){
		this.pathPlantilla=_pathPlantilla;
	}
	
	/**
	 * Descripcion: Segundo constructor que recibe dos parametros
	 * @param _pathPlantilla: ruta donde se encuentra la plantilla
	 * @param _datos: vector de Hashtable con la informacion a incluir en los doc
	 */
	public MasterWords(String _pathPlantilla, Vector<Hashtable<String,String>> _datos){
		this.pathPlantilla=_pathPlantilla;
		this.datos=_datos;
	}


	/**
	 * 
	 * @param nombreFichero
	 * @param pathFinal
	 * @param salida
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector<File> generarInforme(String nombreFichero,String pathFinal, Vector<File> salida) throws ClsExceptions
	{
		Document doc;
		File nuevo=new File(pathFinal);
		nuevo.mkdirs();
		String rutaFinal=pathFinal+ClsConstants.FILE_SEP+nombreFichero;//+sysdate;
		File aux=null;//
		if(datos!=null && datos.size()>0) //Más de un documento
		{
			for(int i=0;i<datos.size();i++){
				//relleno plantilla y la almaceno en el ArrayList
				doc=generaUnInforme(pathPlantilla,(Hashtable<String,String>)datos.get(i));
				try {
					doc.save(rutaFinal+i+".doc");
					aux=new File(rutaFinal+i+".doc");//
					salida.add(aux);//
				} catch (Exception e) {
					throw new ClsExceptions(e,"Error al generar informe");
				}
			}
		}		
		return salida;
	}
	
	 
		public Vector<File> generarInformePorIdioma(String nombreFichero,String pathFinal, Vector<File> salida,String idiomaUsuario) throws ClsExceptions
		{
			Document doc;
			//File nuevo=new File(pathFinal);
			//nuevo.mkdirs();
			String rutaFinal=pathFinal+ClsConstants.FILE_SEP+nombreFichero;//+sysdate;
			File aux=null;//
			
			
			if(datos!=null && datos.size()>0) //Más de un documento
			{
				for(int i=0;i<datos.size();i++){
					//relleno plantilla y la almaceno en el ArrayList
					Hashtable<String,String> auxidioma=(Hashtable<String,String>) datos.get(i);
    				String idiomainteresado=(String)auxidioma.get("CODIGOLENGUAJE");
    				if (idiomainteresado==null || idiomainteresado.equals("")){
    					idiomainteresado=idiomaUsuario;
    				}
    				
					doc=generaUnInforme(pathPlantilla+"_"+idiomainteresado+".doc",(Hashtable<String,String>)datos.get(i));
					try {
						doc.save(rutaFinal+i+".doc");
						aux=new File(rutaFinal+i+".doc");//
						salida.add(aux);//
					} catch (Exception e) {
						throw new ClsExceptions(e,"Error al generar informe");
					}
				}
			}	
			
			return salida;
		}
		public Vector<File> generarInformePorIdioma(String nombreFichero,String pathFinal,String pathTemporal, Vector<File> salida,String idiomaUsuario) throws ClsExceptions
		{
			Document doc;
			//File nuevo=new File(pathFinal);
			//nuevo.mkdirs();
			String rutaFinal=pathFinal+ClsConstants.FILE_SEP+nombreFichero;//+sysdate;
			File aux=null;//
			
			
			if(datos!=null && datos.size()>0) //Más de un documento
			{
				for(int i=0;i<datos.size();i++){
					//relleno plantilla y la almaceno en el ArrayList
					Hashtable<String, String> auxidioma=(Hashtable<String, String>) datos.get(i);
    				String idiomainteresado=(String)auxidioma.get("CODIGOLENGUAJE");
    				if (idiomainteresado==null || idiomainteresado.trim().equals("")){
    					idiomainteresado=idiomaUsuario;
    				}
    				
					doc=generaUnInforme(pathPlantilla+"_"+idiomainteresado+".doc",pathTemporal,nombreFichero,(Hashtable<String, String>)datos.get(i));
					try {
						doc.save(rutaFinal+i+".doc");
						//Borramos el fichero que nos sirvio para generar el nuevo docuemento
						File f= new File(pathTemporal+ClsConstants.FILE_SEP+nombreFichero+".doc");
						f.delete();
     					aux=new File(rutaFinal+i+".doc");//
						salida.add(aux);//
					} catch (Exception e) {
						throw new ClsExceptions(e,"Error al generar informe");
					}
				}
			}	
			return salida;
		}
		
		
	//Agrupa en un fichero tipo zip los doc generados
	public static File doZip(ArrayList<File> array, String rutaFinal) throws ClsExceptions
	{
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;
		try {
			if ((array!=null) && (array.size()>0)) {
				
				ficZip = new File(rutaFinal+".zip");
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<array.size(); i++)
				{
					File auxFile = (File)array.get(i);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(auxFile.getName());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						outTemp.flush();
						fis.close();
						outTemp.closeEntry();
						auxFile.delete();
					}
				}
				
				outTemp.close();
			}
		 
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} finally {
		    try {
		        outTemp.close();
		    } catch (Exception eee) {}
		}
		
		return ficZip;
	}

	//Rellena las plantillas sustituyendo los campos por la información
	private Document generaUnInforme(String pathFichero,Hashtable<String,String> dato) throws ClsExceptions
	{
		 
		
		Enumeration<String> claves=dato.keys();
		Document doc=null;
		
		try {
			doc=new Document(pathFichero);
			do{
				DocumentBuilder builder=new DocumentBuilder(doc);
				String clave = (String) claves.nextElement();
				do{
					if(builder.moveToMergeField(clave)) //si lo encuentra
					{
						builder.write((String)dato.get(clave));
					}
					else {
						break;
					}
				}while(true);
					
			}while(claves.hasMoreElements());
			

		} 
		catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar informe");
		}
		
		return doc;
	}
	private Document generaUnInforme(String pathFichero,String pathFicheroTemporal,String nombreFichero,Hashtable<String, String> dato) throws ClsExceptions
	{
		 
		
		Enumeration<String> claves=dato.keys();
		Document doc=null;
		String ficheroTemporal = pathFicheroTemporal+ClsConstants.FILE_SEP+nombreFichero+".doc";
		//File fileDocMaestro = new File(ficheroTemporal);
		
		
		
		try {
			//Copiamos la plantilla en un fichero temporal
			//copiaFichero(new File(pathFichero), fileDocMaestro);
			//ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO MasterWords.generaUnInforme.new Document",10);
			doc=new Document(pathFichero);
			doc.save(ficheroTemporal);
			doc=new Document(ficheroTemporal);
			//ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN MasterWords.generaUnInforme.new Document",10);
			
			do{
				DocumentBuilder builder=new DocumentBuilder(doc);
				String clave = (String) claves.nextElement();
				do{
					if(builder.moveToMergeField(clave)) //si lo encuentra
					{
						builder.write((String)dato.get(clave));
					}
					else {
						break;
					}
				}while(true);
					
			}while(claves.hasMoreElements());
			

		} 
		catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar informe");
		}
		
		return doc;
	}
	
	
	

	/**
	 * Descripcion: Carga los datos en el documento en funcion de las regiones preestablecidas en el mismo
	 * @param nombreRegion: Nombre dentro del documento donde cargara los datos
	 * @param nombreFichero: Nombre que recibira el fichero resultante
	 * @param pathFinal: Ruta donde se almacenara el fichero resultante
	 * @return
	 * @throws ClsExceptions
	 */
	public File generarInformeRegiones(String nombreRegion,String nombreFichero,String pathFinal) throws ClsExceptions
	{
		File archivo = null;
		String rutaFinal=pathFinal+nombreFichero;//+sysdate;
		
		Document doc;
		
		DataMailMergeDataSource dataMerge = new DataMailMergeDataSource(nombreRegion,datos);
		
		try {
			doc=new Document(pathPlantilla);
		    doc.getMailMerge().executeWithRegions(dataMerge);
			doc.save(rutaFinal+".doc");
			archivo=new File(rutaFinal+".doc");
			if(!archivo.exists())
				throw new SIGAException("messages.general.error.ficheroNoExiste");
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar informe");
		}
		return archivo;
	}

	public Document nuevoDocumento()throws ClsExceptions
	{
		Document doc;
		try {
			doc = new Document(pathPlantilla);
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar informe");
		}
		return doc;
	}
	
	public Document sustituyeDocumento(Document doc,Hashtable<String,Object> dato)throws ClsExceptions
	{

		try {
			Enumeration<String> claves=dato.keys();
			DocumentBuilder builder=new DocumentBuilder(doc);
			while(claves.hasMoreElements()){
				
				String clave = (String) claves.nextElement();
				while(builder.moveToMergeField(clave))
					{
						Object o = dato.get(clave);
						if (!(o instanceof String)) {
							o = (String)o;
						}
						builder.write(((String)o).trim());	
					}
					
				
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al rellenar informe");
		}
		return doc;
	}
	
	public Document sustituyeRegionDocumento(Document doc,String region,Vector dato)throws ClsExceptions
	{
		DataMailMergeDataSource dataMerge = new DataMailMergeDataSource(region,dato);
		
		try {
			//doc=new Document(pathPlantilla);
		    doc.getMailMerge().executeWithRegions(dataMerge);
			//doc.save(rutaFinal+".doc");
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar informe");
		}
		return doc;
	}
	

	
	public File grabaDocumento(Document doc, String pathfinal,String nombrefichero) throws SIGAException, ClsExceptions
	{
		//nombrefichero incluye la extension .doc
		File archivo = null;
		try {
			doc.save(pathfinal+ClsConstants.FILE_SEP+nombrefichero);
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al crear el archivo");
		}
		archivo=new File(pathfinal+ClsConstants.FILE_SEP+nombrefichero);
		if(!archivo.exists())
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		
		return archivo;
	}

	public static void precargaInformes() {
		File salida=null;
		
		try {
		    // --- acceso a paths y nombres 
//		    String pathAplicacion = "";
//		    try {
//		    	UsrBean us = new UsrBean();
//			    us.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
//			    GenParametrosAdm adm = new GenParametrosAdm (us);
//			    pathAplicacion = adm.getValor("0", "CEN", "PATH_APP", ClsConstants.RES_DIR);
//		    }
//		    catch (Exception e) {
//		    	pathAplicacion = ClsConstants.RES_DIR;
//			}
//			String rutaPlantilla = pathAplicacion + ClsConstants.FILE_SEP + "html" + ClsConstants.FILE_SEP +"jsp"+ ClsConstants.FILE_SEP + "general" + ClsConstants.FILE_SEP + "inicializacionWords.doc";
//			String rutaAlmacen = pathAplicacion + ClsConstants.FILE_SEP + "html" + ClsConstants.FILE_SEP +"jsp"+ ClsConstants.FILE_SEP + "general" + ClsConstants.FILE_SEP + "inicializacionCrystal"+UtilidadesString.formatoFecha(new Date(),"yyyyMMddhhmmss")+".doc";
		    Hashtable<String, String> htLocal = new Hashtable<String, String>();
		    htLocal.put("NUMERO_EJG","123456");
			//relleno plantilla y la almaceno en el ArrayList
//			Enumeration claves=htLocal.keys();
//			Document docu=new Document(rutaPlantilla);
//			do{
//				DocumentBuilder builder=new DocumentBuilder(docu);
//				String clave = (String) claves.nextElement();
//				if(builder.moveToMergeField(clave)) //si lo encuentra
//					builder.write((String)htLocal.get(clave));
//			} while(claves.hasMoreElements());

			InputStream is=SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.WORDS_INIT);
			Document docu=new Document(is);
			for (Map.Entry<String,String> entrada:htLocal.entrySet()){
				DocumentBuilder builder=new DocumentBuilder(docu);
				if(builder.moveToMergeField(entrada.getKey())) //si lo encuentra
					builder.write(entrada.getValue());
			}
			salida=SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.WORDS_INIT_RESULT);
			salida.createNewFile();
			FileOutputStream fos=new FileOutputStream(salida);
			docu.save(fos, SaveFormat.DOC);
			fos.flush();
			fos.close();
		} catch (Exception e) {
		    ClsLogging.writeFileLog("ERROR al precargar informes aspose.words: "+e.toString(),3);
		} finally {
//Comentado para que no se borre el fichero y en la siguiente ejecucion no falle.
//			if (salida!=null){
//				try {
//					salida.delete();
//				} catch (Exception e){}
//			}
		}
	}
	


}