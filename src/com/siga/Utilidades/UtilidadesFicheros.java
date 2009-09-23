package com.siga.Utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;

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

	
}
