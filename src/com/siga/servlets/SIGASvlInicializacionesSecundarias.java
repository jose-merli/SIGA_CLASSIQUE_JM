/*
 * Created on 16-ene-2008
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.siga.servlets;

import java.util.Hashtable;

import javax.servlet.ServletContextEvent;

import org.redabogacia.sigaservices.app.services.SigaLog4jService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.informes.CrystalReportMaster;
import com.siga.informes.MasterWords;

import es.satec.businessManager.BusinessManager;

/**
 * @author danielc
 * 
 */

public final class SIGASvlInicializacionesSecundarias extends SIGAContextListenerAdapter {
	public void contextInitialized(ServletContextEvent arg0) {
		super.contextInitialized(arg0);
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		ClsLogging.writeFileLogWithoutSession("  Arrancando Inicializaciones Secundarias ", 3);
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		SIGAReferences.initialize(arg0.getServletContext());
		// this.inicializarCrystal();
		this.inicializarWords();
		this.inicializarLog4j();
		this.inicializarParametros();
	}

	private void inicializarParametros() {
		GenParametrosService parametersService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		parametersService.initParameters();
	}

	private void inicializarLog4j() {
		SigaLog4jService sigaLog4jService = (SigaLog4jService) BusinessManager.getInstance().getService(SigaLog4jService.class);
		sigaLog4jService.initLog4j();
	}

	private void inicializarCrystal() {
		ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
		ClsLogging.writeFileLogWithoutSession("  <> CrystalReport: Inicializando ...", 3);
		ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);

		Hashtable<String, String> parametros = new Hashtable<String, String>();
		UtilidadesHash.set(parametros, "idlenguaje", "1");
		try {
			CrystalReportMaster.generarPDF(SIGAReferences.getDirectoryReference(SIGAReferences.RESOURCE_FILES.CRYSTAL_INIT), SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.CRYSTAL_INIT_RESULT), parametros);
			ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
			ClsLogging.writeFileLogWithoutSession("  <> CrystalReport: Inicializacion Completada", 3);
			ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
		} catch (Exception e) {
			ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
			ClsLogging.writeFileLogWithoutSession("  <> CrystalReport: Error en la inicializacion " + e, 3);
			ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
			return;
		}
	}

	private void inicializarWords() {
		ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
		ClsLogging.writeFileLogWithoutSession("  <> Aspose.Words: Inicializando ...", 3);
		ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);

		License license = new License();
		try {
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String directorioFuentes = rp.returnProperty("general.directorio.fonts");
			if (directorioFuentes != null && !directorioFuentes.equalsIgnoreCase(""))
				FontSettings.setFontsFolder(directorioFuentes, true);
			license.setLicense(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.WORDS_LICENSE));
		} catch (Exception e) {
			ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
			ClsLogging.writeFileLogWithoutSession("Error en licencia de Aspose ", 3);
			e.printStackTrace();
			ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
		}

		MasterWords.precargaInformes();

		ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
		ClsLogging.writeFileLogWithoutSession("  <> Aspose.Words: Inicializacion Completada", 3);
		ClsLogging.writeFileLogWithoutSession("-------------------------------------------------", 3);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
