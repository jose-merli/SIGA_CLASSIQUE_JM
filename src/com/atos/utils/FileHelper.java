package com.atos.utils;

import java.io.File;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

public class FileHelper
{
	private static final String SIGArootPath = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA).returnProperty("directorios.path.OrigenPlantillas");
	private static void addPerm777(File file, boolean changeParentsToo) {
		if (file != null && file.exists()) {
			try {
				// Permissions to this file
				ClsLogging.writeFileLogWithoutSession("Cambiando los permisos de: " + file.getAbsolutePath());
				file.setReadable(true, false);
				file.setWritable(true, false);
				file.setExecutable(true, false);
				ClsLogging.writeFileLogWithoutSession("OK - Permisos cambiados a: " + file.getAbsolutePath());
				
				// Permissions to the parent file
				File parentFile = file.getParentFile();
				if (changeParentsToo && parentFile != null && parentFile.exists() && !parentFile.getAbsolutePath().equalsIgnoreCase(SIGArootPath)) {
					addPerm777(parentFile, changeParentsToo);
				}
			} catch (Exception e) {
				ClsLogging.writeFileLogWithoutSession("Error al cambiar los permisos del fichero " + file.getAbsolutePath() + " - " + e.toString());
			}
		}
	}
	
	/**
	 * Checks if the given file path exists. If not, it calls File.mkdirs(). 
	 * Then, adds all ther permisions (777 on Linux) to the directory named by this abstract pathname, including any necessary parent directories. 
	 * Note that this operation fails when the system user does not have permissions to do that on some parent, but it will have been succeeded in giving permissions backwards up to that point.
	 * @param filePath
	 */
	public static void mkdirs (String filePath) {
		boolean changeParentsToo;
		
		if (filePath == null || filePath.trim().equalsIgnoreCase(""))
			return;
		
		File fileDir = new File(filePath.toString());
		if(fileDir!=null && !fileDir.exists()) {
			changeParentsToo = !fileDir.getParentFile().exists(); 
			fileDir.mkdirs();
			addPerm777(fileDir, changeParentsToo);
		}
	}
}
