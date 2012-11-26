package com.siga.gratuita.pcajg.resoluciones;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.siga.general.SIGAException;

public abstract class ResolucionesFicheroAbstract {
	
	private static final String EXTENSION_PDF = "pdf";
	
	public abstract File execute(String idInstitucion,
			String idRemesaResolucion, File ficheroCliente)
			throws SIGAException;

	protected void copyInputStream(InputStream in, OutputStream out)	throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}
	
	public static String getDirectorioArchivos(String idInstitucion) {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");				
		rutaAlmacen += File.separator + idInstitucion + File.separator + rp.returnProperty("cajg.directorioRemesaResoluciones");		
		
		return rutaAlmacen + File.separator + rp.returnProperty("cajg.directorioResolucionesArchivos");
	}
	
	public static String getExtension(String idInstitucion) {				
		return EXTENSION_PDF;
	}
}
