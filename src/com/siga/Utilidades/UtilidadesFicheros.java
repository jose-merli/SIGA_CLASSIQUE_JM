package com.siga.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.redabogacia.sigaservices.app.exceptions.BusinessException;

import com.atos.utils.ListOfFiles;
import com.siga.envios.Documento;

/**
 * Mantiene utilidades de ficheros
 *
 */
public class UtilidadesFicheros {





	/**
	 * Concatena la lista de ficheros <code>lFicheros</code> en el fichero <code>sFichero</code>
	 * @param lFicheros
	 * @param sFichero
	 * @throws IOException 
	 */
	public static void concatenarFicheros(ListOfFiles lFicheros, String sFichero) throws IOException {
		SequenceInputStream s = new SequenceInputStream(lFicheros);
		File ficheroTurnos= new File(sFichero);
		if (ficheroTurnos.exists()){
			ficheroTurnos.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(ficheroTurnos);
			int c;

			while ((c = s.read()) != -1){
				fos.write(c);
			}
			s.close();
			fos.close();
		} catch (IOException e) {
			throw e;
		}
	}
	
	
	/**
	 * Elimina el directorio <code>dir</code> 
	 * @param dir Directorio a eliminar
	 * @return <code>true</code> si el directorio se elimina correctamente, <code>false</code> en caso contrario.
	 */
	public static boolean deleteDir(File dir){
		File[] lista = dir.listFiles();
		for (int i = 0; i < lista.length; i++){
			if (!lista[i].delete()){
				return false;
			}
		}
		return dir.delete();
	}
	
	public static String getString(File file) throws IOException{
		FileInputStream inputStream =  new FileInputStream(file);
		byte [] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		inputStream.close();
		
		return new String(bytes);
	}
	
	public static void copyFile(File s, File t) {
        try{
          FileChannel in = (new FileInputStream(s)).getChannel();
          FileChannel out = (new FileOutputStream(t)).getChannel();
          in.transferTo(0, s.length(), out);
          in.close();
          out.close();
              
        } catch(Exception e) {
            
        }
    }	
	public static File doZip(String nombreZIP, List<Documento> list) throws BusinessException {	
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;
		
		try {
			if (list!=null && list.size()>0) {
				
				ficZip = new File(nombreZIP);
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (Documento doc : list) {
					File baos = doc.getDocumento();
					
					if (baos.exists() && ! baos.getAbsolutePath().equalsIgnoreCase(ficZip.getAbsolutePath())) {
						ZipEntry ze = new ZipEntry(doc.getDescripcion());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(baos);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0) {
							outTemp.write(buffer, 0, leidos);
						}
						outTemp.flush();
						fis.close();
						outTemp.closeEntry();
					}
				}
				
				outTemp.close();
			}
		 
		} catch (Exception e) {
			throw new BusinessException("Error al crear fichero zip",e);
		} finally {
		    try {
		    	if (outTemp != null) {
		    		outTemp.close();
		    	}
		    } catch (Exception eee) {}
		}
		
		return ficZip;
	}

	
}
