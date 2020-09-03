package com.siga.gratuita.pcajg.resoluciones;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.atos.utils.FileHelper;
import com.siga.general.SIGAException;

public class ResolucionesFicheroAlcala extends ResolucionesFicheroAbstract{

	@Override
	public File execute(String idInstitucion, String idRemesaResolucion, File ficheroCliente) throws SIGAException  {
		File ficheroTXT = null;
		
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(ficheroCliente);		
			
			FileHelper.mkdirs(getDirectorioArchivos(idInstitucion));
			File dirArchivos = new File(getDirectorioArchivos(idInstitucion));
			
			File destino = null;
			Enumeration<? extends ZipEntry> eZip = zipFile.entries();
			while(eZip.hasMoreElements()) {			
				ZipEntry zipEntry = (ZipEntry)eZip.nextElement();
				if (!zipEntry.isDirectory()) {
					
					if (zipEntry.getName().toUpperCase().endsWith("TXT")) {
						ficheroTXT = new File(ficheroCliente.getParentFile(), zipEntry.getName());
						destino = new File(ficheroCliente.getParentFile(), zipEntry.getName());
					}else if(zipEntry.getName().toUpperCase().endsWith("CSV")){
						ficheroTXT = new File(ficheroCliente.getParentFile(), zipEntry.getName());
						destino = new File(ficheroCliente.getParentFile(), zipEntry.getName());
					} else {
						if(zipEntry.getName().indexOf("/")!=-1) {
						//esta dentro de un directorios
							destino = new File(dirArchivos, zipEntry.getName().substring(zipEntry.getName().indexOf("/")+1));
						}else {
							destino = new File(dirArchivos, zipEntry.getName());
						}
					}
					
					copyInputStream(zipFile.getInputStream(zipEntry), new FileOutputStream(destino));					
				}
			}	
			zipFile.close();
			ficheroCliente.delete();
		} catch (Exception e) {
			//Se ha producido un error al tratar el fichero. Compruebe que ha subido un fichero comprimido zip válido.
			throw new SIGAException("messages.pcajg.zip_novalido", e);
		}
		
		return ficheroTXT;
	}
}