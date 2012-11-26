package com.siga.app;

import java.io.File;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;



public class DirectoriosRemesasCAJG {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("/****** Este script mueve los ficheros de la remesas a subdirectorios. ******/");	
		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	 	String pathCAJG = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
	 	
		File dirCajg = new File(pathCAJG);	
//		File dirCajg = new File("C:\\Datos\\ficheros\\CAJG");
		int ficherosMovidos = 0;
		
		if (dirCajg.listFiles() != null) {
			for (int i = 0; i < dirCajg.listFiles().length; i++) {
				File dirInstitucion = dirCajg.listFiles()[i];
				if (dirInstitucion.getName().length() == 4 && dirInstitucion.getName().startsWith("20")) {
					ficherosMovidos = generaDirRemesa(dirInstitucion, ficherosMovidos);				
				}
			}
		} else {
			throw new Exception("No existen ficheros en el directorio CAJG");
		}
		
		System.out.println("Numero de ficheros movidos = " + ficherosMovidos);

	}

	/**
	 * 
	 * @param dirInstitucion
	 */
	private static int generaDirRemesa(File dirInstitucion, int ficherosMovidos) {
		if (dirInstitucion.isDirectory()){			
			if (dirInstitucion.listFiles() != null)  {
				File[] files = dirInstitucion.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];					
					if (file.isFile()) {
						String name = file.getName();
						String idRemesa = name.substring(0, name.indexOf("_"));
						File newDir = new File(dirInstitucion, idRemesa);						
						if (newDir.mkdirs()) {
							File newFile = new File(newDir, name);
							file.renameTo(newFile);
							System.out.println("Moviendo de \"" + file.getAbsolutePath() + "\" a \"" + newFile.getAbsolutePath() + "\"");
							ficherosMovidos++;
						} else {
							System.out.println("No se ha podido crear el directorio " + newDir.getAbsolutePath());
						}
					}
				}
			}
		}	
		return ficherosMovidos;
	}

}
