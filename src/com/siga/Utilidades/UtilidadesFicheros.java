package com.siga.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;

import com.atos.utils.ListOfFiles;

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

	
}
