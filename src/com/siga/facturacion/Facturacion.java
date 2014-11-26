/*
 * Created on 13-abr-2005
 *
 */
package com.siga.facturacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.AdmContadorBean;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmTipoFiltroInformeBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FacBancoInstitucionBean;
import com.siga.beans.FacDisqueteDevolucionesAdm;
import com.siga.beans.FacDisqueteDevolucionesBean;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturaIncluidaEnDisqueteAdm;
import com.siga.beans.FacFacturaIncluidaEnDisqueteBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacLineaDevoluDisqBancoAdm;
import com.siga.beans.FacLineaDevoluDisqBancoBean;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.FacLineaFacturaBean;
import com.siga.beans.FacPlantillaFacturacionAdm;
import com.siga.beans.FacRenegociacionAdm;
import com.siga.beans.FacRenegociacionBean;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysTipoIvaAdm;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;
import com.siga.informes.InformePersonalizable;


/**
 * @author juan.grau
 *
 */

/**
 * @author juan.grau
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Facturacion {
    
    public FacFacturacionProgramadaBean programacionBean;
//    private Integer idUsuario;
    private UsrBean usrbean=null;
    
    private String consulta=null;

	public void setConsulta(String consulta)
	{
		this.consulta=consulta;
	}
	
	public String getConsulta()
	{
		return this.consulta;
	}
	
    /**
     * @param programacionBean Bean del envío
     * @param idUsuario id del usuario
     */
    public Facturacion(FacFacturacionProgramadaBean programacionBean /*, Integer idUsuario */)
    {        
        this.programacionBean = programacionBean;
//        this.idUsuario = idUsuario;	    
    }
 
    public Facturacion(UsrBean usr)
    {        
        this.programacionBean = null;
        this.usrbean = usr;
    }    	
	
	/**
	 * Método estático para el procesado automático de facturacion (SIGASvlProcesoFacturacion)
	 * 
	 * @param idInstitucion
	 * @param idUsuario
	 * @throws SIGAException
	 */
	public boolean procesarFacturas(String idInstitucion, UsrBean userBean) {	    		
		UserTransaction tx = (UserTransaction) userBean.getTransactionPesada();		
		Facturacion facturacion = new Facturacion(userBean);
		boolean alMenosUnafacturacionProgramadaEncontrada = false;
		
		try {				
			Hashtable<Integer,Object> codigos = new Hashtable<Integer,Object>();
			codigos.put(new Integer("1"), idInstitucion);
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
			// Obtencion de la propiedad que contiene el tiempo de espera que se les da a las facturaciones en ejcucion no generadas por alguna anomalía			
		    Long tiempoEsperaBloqueosProperty = Long.valueOf(rp.returnProperty("facturacion.programacionAutomatica.maxMinutosEnEjecucion"));
			String tiempoMaximoEjecucionBloqueada = String.valueOf(tiempoEsperaBloqueosProperty/(24.0*60.0));
			
			String sWhere=" WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NULL " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION + " <= SYSDATE " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDINSTITUCION+" = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND (" + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.GENERACION_PROGRAMADA +
								   " OR (" + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.EJECUTANDO_GENERACION +
								           " AND SYSDATE - " +tiempoMaximoEjecucionBloqueada+ " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" )) ";
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			FacFacturacionProgramadaAdm admFacturacionProgramada = new FacFacturacionProgramadaAdm(userBean);
			Vector<?> vDatos = admFacturacionProgramada.selectDatosFacturacionBean(sWhere,codigos, orden);
			
		    if(vDatos != null && vDatos.size()>0){
				alMenosUnafacturacionProgramadaEncontrada = true;
		    	FacFacturacionProgramadaBean beanFacturacionProgramada = (FacFacturacionProgramadaBean) vDatos.get(0);
		    	
		    	try {
					// Pasamos a estado ejecutando
		    		beanFacturacionProgramada.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.EJECUTANDO_GENERACION); //Ponemos la factura a estado EJECUTANDO GENERACION
		    		
		    		tx.begin();
					admFacturacionProgramada.updateDirect(beanFacturacionProgramada);
					tx.commit();
	
		    		// Generamos la Facturacion (LA TRANSACCION VA DENTRO DEL METODO)				
					facturacion.generandoFacturacion(idInstitucion, beanFacturacionProgramada.getIdSerieFacturacion().toString(), beanFacturacionProgramada.getIdProgramacion().toString());

					ClsLogging.writeFileLog("### PROCESADO facturación AUTOMATICA " ,7);
		    		
		    	} catch (Exception e) {
					try { // Tratamiento rollback
						if (Status.STATUS_ACTIVE  == tx.getStatus()){
							tx.rollback();
						}
					} catch (Exception e2) {}		
		    		ClsLogging.writeFileLogError("### Error procesando facturación AUTOMATICA " ,e,3);
		    	}
				
		    	vDatos = admFacturacionProgramada.selectDatosFacturacionBean(sWhere,codigos, orden);
		    }// del WHILE		
		    
		} catch (Exception e) { 
			ClsLogging.writeFileLogError("### Error general al procesar facturas (INSTITUCION:" + idInstitucion + ")", e, 3);
		}
		
		return alMenosUnafacturacionProgramadaEncontrada;
	}
	
	/**
	 * Confirmación automática de facturacion programada (SIGASvlProcesoFacturacion)
	 * Solo ejecuta una facturacion. El proceso que llama es el que se encargara de pedir varias veces
	 * @param request
	 * @param idInstitucion
	 * @param userBean
	 * @return boolean: true si se ha ejecutado una facturacion; false si no se ha encontrado ninguna facturacion para ejecutar
	 */
	public boolean confirmarProgramacionesFacturasInstitucion(HttpServletRequest request, String idInstitucion, UsrBean userBean) {
		boolean alMenosUnafacturacionProgramadaEncontrada = false;
		
		try {
			ClsLogging.writeFileLog("CONFIRMAR PROGRAMACIONES FACTURAS INSTITUCION: "+idInstitucion,10);
			
   			// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
	    	// ficheros de log
			String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
			String nombreFichero = "";
			SIGALogging log=null;
			
			// obtención de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Hashtable<Integer, Object> codigos = new Hashtable<Integer, Object>();
			codigos.put(new Integer("1"), idInstitucion);
			String sWhere=" WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_VISIBLE + " = 'S' " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " IN (" + FacEstadoConfirmFactBean.GENERADA + ", " + FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA + ") " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
   							" AND NOT EXISTS (SELECT 1 " +
						         " FROM " + FacFacturacionProgramadaBean.T_NOMBRETABLA + " PREVIA " +
						         " WHERE PREVIA." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + idInstitucion +
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA +
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_VISIBLE + " = 'S' " +
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " <> " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + ") ";
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM};
			Vector<?> vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
			if(vDatos != null && vDatos.size()>0){
				alMenosUnafacturacionProgramadaEncontrada = true;
			
				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean beanFacturacionProgramada = (FacFacturacionProgramadaBean)vDatos.get(0);
		    	
				ClsLogging.writeFileLog("CONFIRMAR FACTURACION PROGRAMADA: "+ idInstitucion + " " + beanFacturacionProgramada.getIdSerieFacturacion() + " " + beanFacturacionProgramada.getIdProgramacion(), 10);

		    	// fichero de log
				nombreFichero = "LOG_FAC_CONFIRMACION_" + beanFacturacionProgramada.getIdSerieFacturacion() + "_" + beanFacturacionProgramada.getIdProgramacion() + ".log.xls"; 
				log = new SIGALogging(pathFichero2 + sBarra2 + beanFacturacionProgramada.getIdInstitucion() + sBarra2 + nombreFichero);
				try {
					boolean esfacturacionRapida=false;
					this.confirmarProgramacionFactura(beanFacturacionProgramada, request, false, log, true, false, 1,esfacturacionRapida);
					
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso automático):" + e.getMsg(), e, 3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso automático) Programación:", e, 3);
				}
				
				vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
		    }// del WHILE

		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso automático) INSTITUCION:"+idInstitucion ,e,3);
		}
		
		return alMenosUnafacturacionProgramadaEncontrada;
	}

	/**
	 * Notas Jorge PT 118:
	 * Genera el zip con los pdf de las facturas de las series de facturación programadas para la institucion
	 * <code>idinstitucion</code>. Se buscan series de facturación con los siguientes criterios:
	 * <ul>
	 * <li>fechaPrevistaConfirm is not null</li>
	 * <li>fechaPrevistaConfirm <= SYSDATE</li>
	 * <li>fechaRealGeneracion is not null</li>
	 * <li>idEstadoConfirmacion = procesado sin errores</li>
	 * <li>idEstadoPDF = programado</li>
	 * </ul>
	 * @param request
	 * @param idInstitucion
	 * @param userBean
	 */
	public void generarZipFacturasSolo(HttpServletRequest request, String idInstitucion, UsrBean userBean) {
		try {
			ClsLogging.writeFileLog("GENERAR PDF DE FACTURAS POR INSTITUCION: "+idInstitucion,10);
			
   			// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
	    	// ficheros de log
			String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
			String nombreFichero = "";
			SIGALogging log=null;
			// obtención de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Hashtable<Integer,Object> codigos = new Hashtable<Integer,Object>();
			codigos.put(new Integer("1"), idInstitucion);
			String sWhere=" WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDINSTITUCION+" = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA +
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROGRAMADA;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM};
			
			Vector<?> vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0; i<vDatos.size(); i++){

				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("CONFIRMAR FACTURACION PROGRAMADA: "+idInstitucion+" " +factBean.getIdSerieFacturacion()+" " +factBean.getIdProgramacion(),10);

		    	// fichero de log
				nombreFichero = "LOG_FAC_CONFIRMACION_" + factBean.getIdSerieFacturacion() +"_"+ factBean.getIdProgramacion() +".log.xls"; 
				log = new SIGALogging(pathFichero2+sBarra2+factBean.getIdInstitucion()+sBarra2+nombreFichero);
				try {
					boolean esFacturacionRapida=false;
					confirmarProgramacionFactura(factBean, request, false, log, true, true, 1,esFacturacionRapida);
					generarZip(factBean.getIdInstitucion().toString(), factBean.getIdSerieFacturacion().toString(), factBean.getIdProgramacion().toString());
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso automático):"+e.getMsg(),e,3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso automático) Programación:" ,e,3);
				}
		    }// del for

		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso automático) INSTITUCION:"+idInstitucion ,e,3);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param idInstitucion
	 */
	public void generarPDFsYenviarFacturasProgramacion(HttpServletRequest request, String idInstitucion) {
		UsrBean userBean = this.usrbean;
		
		try {
			ClsLogging.writeFileLog("GENERAR PDF DE FACTURAS POR INSTITUCION: "+idInstitucion,10);			
			
   			// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
	    	// ficheros de log
			String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
			String nombreFichero = "";
			SIGALogging log=null;
			
			// Obtencion de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora			
			Hashtable<Integer,Object> codigos = new Hashtable<Integer,Object>();
			codigos.put(new Integer("1"), idInstitucion);
			
			String sWhere=" WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDINSTITUCION+" = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " IS NOT NULL " + // Para fechas previstas de confirmacion adecuadas 
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " + // Solo las que estan generadas 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROGRAMADA; // Para las que tienen pdf programados pero no generados
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Vector<?> vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0;i<vDatos.size();i++){

				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("GENERAR PDFs Y ENVIAR FACTURACION PROGRAMADA: "+idInstitucion+" " +factBean.getIdSerieFacturacion()+" " +factBean.getIdProgramacion(),10);

		    	// fichero de log
				nombreFichero = "LOG_FAC_CONFIRMACION_" + factBean.getIdSerieFacturacion() +"_"+ factBean.getIdProgramacion() +".log.xls"; 
				log = new SIGALogging(pathFichero2+sBarra2+factBean.getIdInstitucion()+sBarra2+nombreFichero);
				try {
		    		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION,FacFacturacionProgramadaBean.C_IDPROGRAMACION,FacFacturacionProgramadaBean.C_IDSERIEFACTURACION};
		    		Hashtable<String,Object> hashNew = new Hashtable<String,Object>();	
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, factBean.getIdInstitucion());
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, factBean.getIdProgramacion());
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,factBean.getIdSerieFacturacion() );
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA);

		    		UserTransaction tx = (UserTransaction) this.usrbean.getTransactionPesada();
					boolean esFacturacionRapida=false;
		    		this.generarPdfEnvioProgramacionFactura(factBean, request, log, factBean.getIdSerieFacturacion(), factBean.getIdProgramacion(), claves, hashNew, true, tx,esFacturacionRapida);
				
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso automático):"+e.getMsg(),e,3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso automático) Programación:" ,e,3);
				}
		    }// del for
		    
		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso automático) INSTITUCION:"+idInstitucion ,e,3);
		}
	}

	
	public void generarZip(String idInstitucion, String idSerieFacturacion, String idProgramacion) throws SIGAException, ClsExceptions{
		String sRutaJava = "";
		String sRutaTemporal = "";
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		sRutaJava = rp.returnProperty("facturacion.directorioFacturaPDFJava");
		sRutaTemporal = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava") +
		rp.returnProperty("facturacion.directorioFacturaPDFJava");

		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava");

		sRutaJava = sRutaFisicaJava +  sRutaJava;

		ArrayList<File> lista=new ArrayList<File>();

		//String sNombreFichero = idSerieFacturacion + "_" + idProgramacion + ".zip";
		sRutaJava += File.separator + idInstitucion	+ File.separator+ idSerieFacturacion+"_"+idProgramacion+ File.separator/*+ sNombreFichero*/;

		sRutaTemporal += File.separator + idInstitucion+ File.separator;

		//Control de que no exista el fichero a descargar:
		File directorio = new File(sRutaJava);

		String[] ficheros = directorio.list();
		if (ficheros == null||ficheros.length==0){
			directorio.delete();
			throw new SIGAException("messages.facturacion.descargaFacturas");
		}else{
			for (int x=0;x<ficheros.length;x++){
				File fichero = new File(sRutaJava+File.separator+ficheros[x]);
				lista.add(fichero);
			}
			
			doZip(sRutaTemporal,idSerieFacturacion+"_"+idProgramacion,lista);
		}
		
		//Se eliminen las facturas existentes
		if(directorio.exists()){
			for (int x=0;x<ficheros.length;x++){
				File fichero = new File(sRutaJava+File.separator+ficheros[x]);
				fichero.delete();
			}
			directorio.delete();
		}
	}
	
	
	private void doZip(String rutaServidorDescargasZip, String nombreFicheroPDF, ArrayList<File> ficherosPDF) throws ClsExceptions	{
		// Generar Zip
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;
		
		try {
		    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: numero de facturas = "+ficherosPDF.size(),10);

			if ((ficherosPDF!=null) && (ficherosPDF.size()>0)) {
				
				ficZip = new File(rutaServidorDescargasZip +  nombreFicheroPDF + ".zip");

				// RGG 
				if (ficZip.exists()) {
				    ficZip.delete();
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: el fichero zip ya existia. Se elimina",10);
				}
				
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<ficherosPDF.size(); i++)
				{

				    File auxFile = (File)ficherosPDF.get(i);
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: fichero numero "+i+" longitud="+auxFile.length(),10);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(auxFile.getName());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						
						fis.close();
						outTemp.closeEntry();
					}
				}
			    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: ok ",10);
				
				outTemp.close();

			}
		} catch (FileNotFoundException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} catch (IOException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		}
		finally {
			try {
				outTemp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
    
    /**
     * Notas Jorge PT 118:
     * - Productos y Servicios > Solicitudes > Compra/Subscripción (facturacion rapida)
     * - Productos y Servicios > Gestión Solicitudes (facturacion rapida)
     * - Certificados > Gestión de solicitudes (facturacion rapida)
     * - Confirmación automática de facturacion programada (SIGASvlProcesoFacturacion)
     * 
     * @param beanP
     * @param req
     * @param archivarFacturacion
     * @param log
     * @param generarPagosBanco
     * @param soloGenerarFactura
     * @param iTransaccionInterna = 0:TransaccionPadre; 1:TransaccionPesada; 2:TransaccionLigera
     * @throws ClsExceptions
     * @throws SIGAException
     */
    public void confirmarProgramacionFactura(
    		FacFacturacionProgramadaBean beanP, 
    		HttpServletRequest req, 
    		boolean archivarFacturacion, 
    		SIGALogging log, 
    		boolean generarPagosBanco, 
    		boolean soloGenerarFactura, 
    		int iTransaccionInterna, boolean esFacturacionRapida) throws ClsExceptions, SIGAException {
    	
		UserTransaction tx = null;
		if (iTransaccionInterna == 1) {
			tx = (UserTransaction) this.usrbean.getTransactionPesada();
		} else if (iTransaccionInterna == 2) { 
			tx = this.usrbean.getTransaction();
		}
    	
    	String msjAviso = null; 
    	try {
    		// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
    		String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
    		String nombreFichero = "";

    		ClsLogging.writeFileLog("CONFIRMAR FACTURACION RAPIDA: "+beanP.getIdInstitucion()+" " +beanP.getIdSerieFacturacion()+" " +beanP.getIdProgramacion(),10);

    		// fichero de log
    		nombreFichero = "LOG_FAC_CONFIRMACION_" + beanP.getIdSerieFacturacion() + "_" + beanP.getIdProgramacion() + ".log.xls"; 
    		if(log==null)
    			log = new SIGALogging(pathFichero2+sBarra2+beanP.getIdInstitucion()+sBarra2+nombreFichero);

    		//Facturacion factura = new Facturacion (beanP);
    		Long idSerieFacturacion = beanP.getIdSerieFacturacion();			
    		Long idProgramacion 	= beanP.getIdProgramacion();
    		String usuMod			= this.usrbean.getUserName();
    		String pathFichero 		= p.returnProperty("facturacion.directorioBancosOracle");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
    		pathFichero += sBarra+beanP.getIdInstitucion().toString();

    		// Se confirma la facturación
    		FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
    		Hashtable<String,Object> hashNew = new Hashtable<String,Object>();				
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, beanP.getIdInstitucion());
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion );
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
    		if (archivarFacturacion) {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_ARCHIVARFACT, "1");
    		} else {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_ARCHIVARFACT, "0");
    		}

    		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION,FacFacturacionProgramadaBean.C_IDPROGRAMACION,FacFacturacionProgramadaBean.C_IDSERIEFACTURACION};
    		String [] camposFactura = {FacFacturacionProgramadaBean.C_FECHACONFIRMACION, FacFacturacionProgramadaBean.C_ARCHIVARFACT,FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION,FacFacturacionProgramadaBean.C_LOGERROR};

    		if(!soloGenerarFactura){    		
	    		try {
	    			//////////// INICIO TRANSACCION ////////////////
	    			if (tx!=null && Status.STATUS_NO_TRANSACTION == tx.getStatus())
	    				tx.begin();	    		
	    			
	    			// Lo primero que se hace es poner la facturacion en estado EJECUTANDO CONFIRMACION
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.EJECUTANDO_CONFIRMACION);
	    			facadm.updateDirect(hashNew, claves, camposFactura);	    
	    			
	    			if (tx!=null)
	    				tx.commit();
	    			//////////// FIN TRANSACCION ////////////////
				
	    			ClsLogging.writeFileLog("### Procesando CONFIRMACION: "+idProgramacion ,7);
				
	    			//////////// INICIO TRANSACCION ////////////////
	    			if (tx!=null)
	    				tx.begin();
	
	    			//Se genera numero de factura definitivo
	    			Object[] param_in_confirmacion = new Object[4];
	    			param_in_confirmacion[0] = beanP.getIdInstitucion().toString();
	    			param_in_confirmacion[1] = idSerieFacturacion.toString();
	    			param_in_confirmacion[2] = idProgramacion.toString();
	    			param_in_confirmacion[3] = usuMod;
	    			String resultadoConfirmar[] = new String[2];
	    			
	    			resultadoConfirmar = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.CONFIRMACIONFACTURACION(?,?,?,?,?,?)}", 2, param_in_confirmacion);
	    			String codretorno = resultadoConfirmar[0];
	    			
	    			if (codretorno.equals("-33")){
	    				throw new SIGAException ("messages.facturacion.confirmar.contadorRepetido");
	    			}
	    			if (!codretorno.equals("0")){
	    				throw new ClsExceptions ("Error al generar números de facturación: "+resultadoConfirmar[1]);
	    			}
	
	    			// RGG 05/05/2009 Cambio (solo se generan los pagos por banco cuando se indica por parámetro)
	    			if (generarPagosBanco) {
		    		
	    				// Se envían a banco para su cobro
	        			Object[] param_in_banco = new Object[11];
	        			param_in_banco[0] = beanP.getIdInstitucion().toString();
	        			param_in_banco[1] = idSerieFacturacion.toString();
	        			param_in_banco[2] = idProgramacion.toString();
		    			param_in_banco[3] = "";
		    			param_in_banco[4] = "";
		    			param_in_banco[5] = "";
		    			param_in_banco[6] = "";
		    			param_in_banco[7] = "";
		    			param_in_banco[8] = pathFichero;
		    			param_in_banco[9] = usuMod;
		    			param_in_banco[10] = this.usrbean.getLanguage();
		
		    			String resultado[] = new String[3];
		    			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
		    			
		    			codretorno = resultado[1];
		    			if (!codretorno.equals("0")){
		    				throw new ClsExceptions ("Error al generar disquetes bancarios: " + resultado[2]);
		    			}
	    			}
	    			
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA);
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_LOGERROR,"");
	    			facadm.updateDirect(hashNew, claves, camposFactura);
	    			
	    			if (tx!=null)
	    				tx.commit();
	    			//////////// FIN TRANSACCION ////////////////
	    			
	    			ClsLogging.writeFileLog("CONFIRMAR Y PRESENTAR OK ",10);
	
	    		} catch (Exception e) {    		
	    			if (tx!=null) {
	    				try { // Tratamiento rollback
	    					if (Status.STATUS_ACTIVE  == tx.getStatus()){
	    						tx.rollback();
	    					}
	    				} catch (Exception e2) {}
	    			} 			
	
	    			if (e instanceof SIGAException) {
	    				SIGAException e2 = (SIGAException) e;
	    				ClsLogging.writeFileLog("ERROR AL CONFIRMAR Y PRESENTAR: "+UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),e2.getLiteral()),10);
	    				log.writeLogFactura("CONFIRMACION","N/A","N/A","Error en proceso de confirmación: "+UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),e2.getLiteral()));
	    				
	    			} else {
	    				ClsLogging.writeFileLog("ERROR AL CONFIRMAR Y PRESENTAR: " + e.toString(),10);
	    				log.writeLogFactura("CONFIRMACION","N/A","N/A","Error en proceso de confirmación: "+ e.toString());
	    			}
	
	    			//////////// INICIO TRANSACCION ////////////////
	    			if (tx!=null)
	    				tx.begin();
	    			
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.ERROR_CONFIRMACION);
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "");
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
	    			facadm.updateDirect(hashNew, claves, camposFactura);
	    			
	    			if (tx!=null)
	    				tx.commit();
	    			//////////// FIN TRANSACCION ////////////////
	
	    			ClsLogging.writeFileLog("CAMBIA ESTADO A FINALIZADA ERRORES.",10);
	    			throw new ClsExceptions("Error al confirmar facturacion rápida. " + e.getMessage());
	    		}

    		} else {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA); //Si entramos por aqui es que ya hemos confirmado previamente
    		}// FIN IF EJECUTAR CONFIRMACION

    		ClsLogging.writeFileLog("ENTRA A GENERAR Y ENVIAR",10);
    		
    		boolean isGenerarPdf = beanP.getGenerarPDF() != null && beanP.getGenerarPDF().trim().equals("1");
    		boolean isGenerarEnvio = beanP.getEnvio() != null && beanP.getEnvio().trim().equals("1") && (beanP.getRealizarEnvio()==null || beanP.getRealizarEnvio().toString().equalsIgnoreCase("1"));
    		if(isGenerarPdf){
    			msjAviso = generarPdfEnvioProgramacionFactura(beanP,req,log,idSerieFacturacion, idProgramacion, claves, hashNew, isGenerarEnvio, tx,esFacturacionRapida);
    		}
			
    	} catch (Exception e) {
			try { // Tratamiento rollback
				if (tx!=null && Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e2) {}
			
			if (e instanceof SIGAException) {
				throw (SIGAException) e;
				
			} else if (e instanceof ClsExceptions) {
    			throw (ClsExceptions) e;
			}
			
    		throw new SIGAException("Error al confirmar facturacion rápida.");
    	}

    	if(msjAviso!=null)
    		throw new ClsExceptions(msjAviso);
    }
    
    /**
     * 
     * @param beanP
     * @param req
     * @param log
     * @param idSerieFacturacion
     * @param idProgramacion
     * @param claves
     * @param hashFactura
     * @param isGenerarEnvio
     * @param tx
     * @return
     * @throws ClsExceptions
     * @throws SIGAException
     * @throws Exception
     */
    private String generarPdfEnvioProgramacionFactura(
	    		FacFacturacionProgramadaBean beanP, 
	    		HttpServletRequest req,
	    		SIGALogging log, 
	    		Long idSerieFacturacion, 
	    		Long idProgramacion,
	    		String [] claves,	
	    		Hashtable<String,Object> hashFactura,
	    		boolean isGenerarEnvio,
	    		UserTransaction tx,
    		boolean esFacturacionRapida)throws ClsExceptions, SIGAException, Exception {
    	
    	FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
    	
    	String msjAviso = null;
		String [] camposPDF = {FacFacturacionProgramadaBean.C_IDESTADOPDF};
		String [] camposEnvioPdf = {FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacFacturacionProgramadaBean.C_IDESTADOPDF};
    	
		try {

			// Se guardan las facturas impresas.
			ClsLogging.writeFileLog("TIENE QUE GENERAR PDF",10);
	
			//////////// INICIO TRANSACCION ////////////////
			if (tx!=null) 
				tx.begin();
			
			UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_PROCESANDO); // cambio de estado PDF a PROCESANDO
			facadm.updateDirect(hashFactura, claves, camposPDF);
			
			if (tx!=null) 
				tx.commit();
			//////////// FIN TRANSACCION ////////////////
	
			//////////// ALMACENAR RAPIDA ////////////////
			//En facturaciones rápidas, en compra de PYS no hay que generar el excel con el log
			int errorAlmacenar = this.generaryEnviarProgramacionFactura(req, beanP.getIdInstitucion(), idSerieFacturacion, idProgramacion, isGenerarEnvio, log, esFacturacionRapida);
			
			////////////INICIO TRANSACCION ////////////////
			if (tx!=null) 
				tx.begin();	
			switch (errorAlmacenar) {
				case 0: //NO HAY ERROR. SE HA GENERADO CORRECTAMENTE Y SE PROCESADO EL ENVIO										
					if (isGenerarEnvio) {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA); // cambio de estado PDF a FINALIZADA
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADA); // cambio de estado ENVIO a FINALIZADO
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
		
					} else{
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA); // cambio de estado PDF a FINALIZADA
						facadm.updateDirect(hashFactura, claves, camposPDF);
					}		
					
					ClsLogging.writeFileLog("OK TODO. CAMBIO DE ESTADOS",10);
					break;
					
				case 1: // ERROR EN GENERAR PDF
					ClsLogging.writeFileLog("ERROR AL ALMACENAR FACTURA. RETORNO=" + errorAlmacenar, 3);
					msjAviso = "messages.facturacion.confirmacion.errorPdf";
					if (isGenerarEnvio) {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRRORES
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
					} else {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
					}
					facadm.updateDirect(hashFactura, claves, camposPDF);
					break;
					
				case 2: // ERROR EN ENVIO FACTURA
					ClsLogging.writeFileLog("ERROR AL ALMACENAR FACTURA. RETORNO="+errorAlmacenar,3);					
					msjAviso = "messages.facturacion.confirmacion.errorEnvio";
					if (isGenerarEnvio) {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA); // cambio de estado PDF a FINALIZADA
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRRORES
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
					} else {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA); // cambio de estado PDF a FINALIZADA
						facadm.updateDirect(hashFactura, claves, camposPDF);
					}
		
					ClsLogging.writeFileLog("ERROR ENVIAR FACTURA. CAMBIO DE ESTADOS",10);
					break;
					
				default:
					msjAviso = "messages.facturacion.confirmacion.errorPdf";
					if (isGenerarEnvio) {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRRORES
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
					} else {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
						facadm.updateDirect(hashFactura, claves, camposPDF);
					}
		
					ClsLogging.writeFileLog("ERROR GENERAL GENERAR/ENVIAR FACTURA. CAMBIO DE ESTADOS",10);
					break;
			}
			if (tx!=null)
				tx.commit();
			//////////// FIN TRANSACCION ////////////////

		} catch (Exception e) {

			ClsLogging.writeFileLog("ERROR GENERAL EN TRY GENERAR/ENVIAR.",10);
			if (tx!=null && Status.STATUS_ACTIVE  == tx.getStatus())
				tx.rollback();

			// ESCRIBO EN EL LOG mensaje general ??
			log.writeLogFactura("PDF/ENVIO","N/A","N/A","Error general en el proceso de generación o envío de facturas PDF: "+e.toString());

			//////////// INICIO TRANSACCION ////////////////
			if (tx!=null)
				tx.begin();
			
			if (isGenerarEnvio) {
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRRORES
				facadm.updateDirect(hashFactura, claves, camposEnvioPdf);

			} else {
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
				facadm.updateDirect(hashFactura, claves, camposPDF);
			}
			
			if (tx!=null)
				tx.commit();
			//////////// FIN TRANSACCION ////////////////

			throw e;
		}
		
		return msjAviso;
    }
    
    /**
     * 
     * @param request
     * @param institucion
     * @param serieFacturacion
     * @param idProgramacion
     * @param bGenerarEnvios
     * @param log
     * @param esFacturacionRapida
	 * @return int - Devuelve: - 0 si todo está correcto.
	 * 						   - 1 si ha existido un error en el procesado de la factura.
	 * 						   - 2 si ha existido un error en el procesado del envío. 
     * @throws ClsExceptions
     * @throws SIGAException
     */
	public int generaryEnviarProgramacionFactura (
			HttpServletRequest request, 
			Integer institucion, 
			Long serieFacturacion, 
			Long idProgramacion, 
			boolean bGenerarEnvios, 
			SIGALogging log,
			boolean esFacturacionRapida
		)  throws ClsExceptions, SIGAException {
		
		UsrBean userbean = this.usrbean;
		
		File ficFOP=null;
		int salida = 0;
		boolean existeAlgunErrorPdf = false;
		boolean existeAlgunErrorEnvio = false;
		
		try {		
			// Obtengo las facturas a almacenar
		    FacFacturaAdm admF = new FacFacturaAdm(userbean);		    
		    Vector<?> vFacturas = admF.getSerieFacturacionConfirmada(institucion.toString(), serieFacturacion.toString(), idProgramacion.toString());
			
			ClsLogging.writeFileLog("ALMACENAR >> "+institucion.toString()+" "+serieFacturacion.toString()+" "+idProgramacion.toString(),10);
			
			// Obtengo la plantilla a utilizar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(userbean);			
			Vector<?> vPlantillas = plantillaAdm.getPlantillaSerieFacturacion(institucion.toString(),serieFacturacion.toString());
			String sPlantilla = vPlantillas.firstElement().toString();
						
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
			// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
		    String rutaTemporal = rp.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+rp.returnProperty("facturacion.directorioTemporalFacturasJava");
    		String barraTemporal = "";
    		if (rutaTemporal.indexOf("/") > -1){ 
    			barraTemporal = "/";
    		}
    		if (rutaTemporal.indexOf("\\") > -1){ 
    			barraTemporal = "\\";
    		}    		
    		rutaTemporal += barraTemporal+institucion.toString();
			File rutaFOP=new File(rutaTemporal);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaFOP.exists()){
				if(!rutaFOP.mkdirs()){
					// ESCRIBO EN EL LOG
					throw new SIGAException("messages.facturacion.almacenar.rutaTemporalMal");					
				}
			}
    		
			// Obtencion de la ruta donde se almacenan las facturas en formato PDF			
			String idserieidprogramacion = serieFacturacion.toString()+"_" + idProgramacion.toString();
			String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
    		String barraAlmacen = "";
     		if (rutaAlmacen.indexOf("/") > -1){ 
    			barraAlmacen = "/";
    		}
    		if (rutaAlmacen.indexOf("\\") > -1){ 
    			barraAlmacen = "\\";
    		}    		
    		rutaAlmacen += barraAlmacen+institucion.toString()+barraAlmacen+idserieidprogramacion;
			File rutaPDF=new File(rutaAlmacen);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaPDF.exists()){
				if(!rutaPDF.mkdirs()){

					// ESCRIBO EN EL LOG
					throw new SIGAException("messages.facturacion.almacenar.rutaAccesoPDFMal");					
				}
			}
			
			// Obtencion de la ruta de donde se obtiene la plantilla adecuada			
		    String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+rp.returnProperty("facturacion.directorioPlantillaFacturaJava");
		    String barraPlantilla="";
    		if (rutaPlantilla.indexOf("/") > -1){
    			barraPlantilla = "/";
    		}
    		if (rutaPlantilla.indexOf("\\") > -1){
    			barraPlantilla = "\\";
    		}
    		rutaPlantilla += barraPlantilla+institucion.toString()+barraPlantilla+sPlantilla;
			File rutaModelo=new File(rutaPlantilla);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaModelo.exists()){

				// ESCRIBO EN EL LOG
				throw new SIGAException("messages.facturacion.almacenar.rutaPlantillaFacturaMal");					
			}
    		
			
			
			ClsLogging.writeFileLog("ALMACENAR >> TERMINA DE OBTENER PLANTILLAS Y DATOS GENERALES",10);

			// recorro todas las facturas para ir creando lo sinformes pertinentes
    		Enumeration<?> listaFacturas = vFacturas.elements();
    		    		
			ClsLogging.writeFileLog("ALMACENAR >> NUMERO DE FACTURAS: "+vFacturas.size(),10);
    		
			String idFactura="";
			Integer plantillaMail = null;
			
			/** CR7 - Se saca fuera ya que siempre se usa la misma plantilla para tdas las facturas **/
			//SE SELECCIONA LA PLANTILLA MAIL
			FacFacturacionProgramadaAdm facProgAdm = new FacFacturacionProgramadaAdm(userbean);
			Hashtable<String,Object> hashProg = new Hashtable<String,Object>();
			hashProg.put(FacFacturacionProgramadaBean.C_IDINSTITUCION, institucion);
			hashProg.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, serieFacturacion);
			hashProg.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
			Vector<?> vFacProg = facProgAdm.select(hashProg);
			if(vFacProg != null && vFacProg.size()>0){
				FacFacturacionProgramadaBean facProgBean = (FacFacturacionProgramadaBean) vFacProg.get(0);
				if(facProgBean.getIdTipoPlantillaMail() != null){
					plantillaMail = facProgBean.getIdTipoPlantillaMail();
				}
			}
			
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no COMPROBAMOS QUE HAYA SIDO CORRECTO EL CAMBIO ANTERIOR
			//while (correcto && listaFacturas.hasMoreElements()){
    		while (listaFacturas.hasMoreElements()){
    			boolean correcto=true;
    			try {

	    			Hashtable<?,?> facturaHash=(Hashtable<?,?>)listaFacturas.nextElement();
	    			idFactura=(String)facturaHash.get(FacFacturaBean.C_IDFACTURA);
	    			String idPersona=(String)facturaHash.get(FacFacturaBean.C_IDPERSONA);
	    			String numFactura=(String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA);
	    			
	    			// Obtenemos el lenguaje del cliente 
	    			CenClienteAdm cliAdm = new CenClienteAdm(userbean);
	    			String lenguaje = cliAdm.getLenguaje(institucion.toString(),idPersona);
	    				    			
		   			CenColegiadoAdm admCol = new CenColegiadoAdm(userbean);
		  			Hashtable<?,?> htCol = admCol.obtenerDatosColegiado(this.usrbean.getLocation(),idPersona,this.usrbean.getLanguage());
		  			
		  			String nColegiado = "";
		  			if (htCol!=null && htCol.size()>0) {
		  			    nColegiado = UtilidadesHash.getString(htCol,"NCOLEGIADO_LETRADO");
		  			}			 

	    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA: "+idFactura,10);
	    			
	    			// TRY del proceso de generacion de la factura en PDF
	    			try {
	    				
	    				// PROCESO DE CREAR EL PDF
	    				
	    				// RGG 15/02/2007 CAMBIOS PARA INFORME MASTER REPOR
	    				InformeFactura inf = new InformeFactura(userbean);
	    				File filePDF = inf.generarFactura(request,lenguaje.toUpperCase(),userbean.getLocation(),idFactura,nColegiado);

	    				if (filePDF==null) {
	    					correcto = false;
	    				    throw new ClsExceptions("message.facturacion.error.fichero.nulo");				
	    				} else {
	    					correcto = true;
	    				}
						
		    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA GENERADA EN PDF",10);
	    				
		    			
    				} catch (SIGAException ee) {
    					correcto = false;
    					ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.getLiteral(userbean.getLanguage()),10);
	    				
	    				// ESCRIBO EN EL LOG
						
    					if(!esFacturacionRapida)
    						log.writeLogFactura("PDF",idPersona,numFactura,"Error en el proceso de generación de facturas PDF: "+ee.getLiteral(userbean.getLanguage()));
    					else{
    						String msj=UtilidadesString.getMensajeIdioma(userbean.getLanguage(), "message.facturacion.error.generacion.factura.pdf")+ee.getLiteral(userbean.getLanguage());
    						throw new SIGAException(msj);
    					}
    					salida=1;
	    	    		//Aunque nos ha fallado esta factura es posible que la siguiente, no.
	    	    		//POR LO TANTO no cazamos la excepcion
	    	    		//throw ee;
	    	    		existeAlgunErrorPdf = true;

	    			} catch (Exception ee) {
	    				correcto = false;
	    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.toString(),10);
	    				
	    				// ESCRIBO EN EL LOG
						if(!esFacturacionRapida)
							log.writeLogFactura("PDF",idPersona,numFactura,"message.facturacion.error.generacion.factura.pdf"+ee.toString());
						else{
							String msj=UtilidadesString.getMensajeIdioma(userbean.getLanguage(),"message.facturacion.error.generacion.factura.pdf")+ee.toString();
							throw new SIGAException(msj);
						}
						salida=1;
	    	    		//Aunque nos ha fallado esta factura es posible que la siguiente, no.
	    	    		//POR LO TANTO no cazamos la excepcion
	    	    		//throw ee;
	    	    		existeAlgunErrorPdf = true;
	    			}
	    			
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> VAMOS A VER SI ENVIARMOS: ENVIAR:"+bGenerarEnvios+" CORRECTO:"+correcto,10);

		    	    	
	    			/***************    ENVIO FACTURAS *****************/
	    			if (bGenerarEnvios && correcto){
	    				enviarProgramacionFactura(idPersona, institucion.toString(), idFactura, plantillaMail, nColegiado, numFactura,rutaAlmacen,log,esFacturacionRapida, salida, existeAlgunErrorEnvio);
	    			}


    			}catch (SIGAException se){
    				throw se;
    			} catch (Exception tot) {
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> CATCH GENERAL",10);
    				throw tot;
   				}
    			
				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE FACTURA OK ",10);

    		} // bucle
    		 
    		
    		if(!existeAlgunErrorPdf){
    		
	    		/**************  CREAMOS EL INFORME DE CONFIRMACION DE FACTURA QUE SE AÑADIRÁ AL ZIP DE FACTURAS EMITIDAS    ****************/
				AdmInformeAdm datosInforme = new AdmInformeAdm(this.usrbean);
				Hashtable<String,Object> hashWhere = new Hashtable<String,Object>();			
				UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDTIPOINFORME, "FACT");
				UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDINSTITUCION, "0");
				
				ClsLogging.writeFileLog("### Inicio datosInforme CONFIRMACION",7);
				
				Vector<?> v =datosInforme.select(hashWhere);
	
				if(v!=null && v.size()>0){				
					for (int dv = 0; dv < v.size(); dv++){				
						AdmInformeBean informe = (AdmInformeBean) v.get(dv);
						
						ArrayList<HashMap<String, String>> filtrosInforme = new ArrayList<HashMap<String, String>>();
						HashMap<String, String> filtro;
						filtro = new HashMap<String, String>();
						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDIOMA");
						filtro.put("VALOR", this.usrbean.getLanguageInstitucion().toString());
						filtrosInforme.add(filtro);	
						
						filtro = new HashMap<String, String>();
						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDSERIEFACTURACION");
						filtro.put("VALOR", serieFacturacion.toString());
						filtrosInforme.add(filtro);	
						
						filtro = new HashMap<String, String>();
						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDPROGRAMACION"); 
						filtro.put("VALOR", idProgramacion.toString());
						filtrosInforme.add(filtro);	
						
						filtro = new HashMap<String, String>();
						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDINSTITUCION");
						filtro.put("VALOR", institucion.toString());
						filtrosInforme.add(filtro);
						
						informe.setNombreSalida("CONFIRMACION_" + serieFacturacion + "_" + idProgramacion); 
						
						ClsLogging.writeFileLog("### Inicio generación fichero excel CONFIRMACION",7);
						
						ArrayList<File> fichPrev= InformePersonalizable.generarInformeXLS(informe, filtrosInforme,rutaAlmacen, this.usrbean);
						
						ClsLogging.writeFileLog("### Fin generación fichero excel CONFIRMACION",7);
		
						//Si la previsión está vacía
						if(fichPrev==null || fichPrev.size()==0) {
							ClsLogging.writeFileLog("### Error al generar el informe de la confirmacion. Inicio creación fichero log CONFIRMACION sin datos",7);
							throw new ClsExceptions("message.facturacion.error.fichero.nulo");
						
						} 
					}	
				}
	    		
	    		/********************************************************************************************************/
	    		
	    		// inc6666 - Si es correcto generamos el ZIP con todas las facturas
				generarZip(institucion.toString(), serieFacturacion.toString(), idProgramacion.toString());
    		}
    				
		}catch (SIGAException e) {
			
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.getLiteral(userbean.getLanguage()),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			if(!esFacturacionRapida)
				log.writeLogFactura("PDF/ENVIO","N/A","N/A","message.facturacion.error.generacion.envio.factura"+e.getLiteral(userbean.getLanguage()));
			else
				throw e;
			
			if (ficFOP!=null && ficFOP.exists()){
				ficFOP.delete();
			}
			existeAlgunErrorEnvio = true;
			existeAlgunErrorPdf = true;
			//throw e;
		}catch (Exception e) {
			
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.toString(),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			if(!esFacturacionRapida)
				log.writeLogFactura("PDF/ENVIO","N/A","N/A","message.facturacion.error.generacion.envio.factura"+e.toString());
			else{
				String msj=UtilidadesString.getMensajeIdioma(userbean.getLanguage(),"message.facturacion.error.generacion.envio.factura")+e.toString();
				throw new SIGAException(msj);
			
			}
			if (ficFOP!=null && ficFOP.exists())
				ficFOP.delete();
			
			
			existeAlgunErrorEnvio = true;
			existeAlgunErrorPdf = true;
			//throw new ClsExceptions(e,"ERROR general al almacenar factura");
		}
		ClsLogging.writeFileLog("ALMACENAR >> SALIDA="+salida,10);
		if(existeAlgunErrorPdf && existeAlgunErrorEnvio){
			salida = 3;
		}else if(!existeAlgunErrorPdf && !existeAlgunErrorEnvio){
			salida = 0;
		}else if(existeAlgunErrorPdf){
			salida = 1;
		}else if(existeAlgunErrorEnvio){
			salida = 2;
			
		}else{
			//IMPOSIBLE
			
		}
		
		return salida;
	}
    
    /**
     * @param nColegiado 
     * @param numeroFactura 
     * @param salida 
     * @param existeAlgunErrorEnvio 
     * @throws SIGAException 
	 * 
	 */
	public void enviarProgramacionFactura(String idPersona, String idInstitucion, String idFactura, Integer plantillaMail, String nColegiado, String numeroFactura, String rutaAlmacen,
			SIGALogging log,boolean generarLog, int salida, boolean existeAlgunErrorEnvio) throws SIGAException {	    				
		UserTransaction tx = this.usrbean.getTransaction();
		UsrBean userbean = this.usrbean;
		
		try {			
			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE ENVIO",10);
			
			//Obtenemos el bean del envio: 
			CenPersonaAdm admPersona = new CenPersonaAdm(userbean);
			String descripcion = "Envio factura " + numeroFactura + " - " + admPersona.obtenerNombreApellidos(idPersona);
			Envio envio = new Envio(userbean,descripcion);

			// Bean envio
			EnvEnviosBean enviosBean = envio.enviosBean;
			
			// RGG
			GenParametrosAdm paramAdm = new GenParametrosAdm(userbean);
			String preferencia = paramAdm.getValor(idInstitucion,"ENV","TIPO_ENVIO_PREFERENTE","1");
			Integer valorPreferencia = Envio.calculaTipoEnvio(preferencia);
            enviosBean.setIdTipoEnvios(valorPreferencia);

			// Preferencia del tipo de envio si el usuario tiene uno:
			CenDireccionesAdm direccionAdm = new CenDireccionesAdm(userbean);
			Hashtable<?,?> direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,preferencia);
			
			if (direccion==null || direccion.size()==0) {
				 direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"3");// si no hay direccion preferente mail, buscamos la de correo
				 if (direccion==null || direccion.size()==0) {
				 	direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"2");// si no hay direccion de despacho, buscamos la de despacho
				 	if (direccion==null || direccion.size()==0) {
				 		direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"");// si no hay direccion de despacho, buscamos cualquier dirección.
				 		if (direccion==null || direccion.size()==0) {
				 			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> NO TIENE DIRECCION PREFERENTE "+preferencia,10);
	    					throw new ClsExceptions("No se ha encontrado dirección de la persona para el tipo de envio preferente: "+preferencia); 			
				 		}
				 	}
				 }
				
			}
			
			if(plantillaMail != null){
				enviosBean.setIdPlantillaEnvios(plantillaMail);
				String barraAlmacen = "";
	     		if (rutaAlmacen.indexOf("/") > -1){ 
	    			barraAlmacen = "/";
	    		}
	    		if (rutaAlmacen.indexOf("\\") > -1){ 
	    			barraAlmacen = "\\";
	    		}
				// Creacion documentos
 				Documento documento = new Documento(rutaAlmacen+barraAlmacen+nColegiado+"-"+UtilidadesString.validarNombreFichero(numeroFactura)+".pdf","Factura "+nColegiado+"-"+UtilidadesString.validarNombreFichero(numeroFactura)+".pdf");
				if(numeroFactura==null ||numeroFactura.equals("")){
					documento = new Documento(rutaAlmacen+barraAlmacen+nColegiado+"-"+idFactura+".pdf","Factura "+nColegiado+"-"+idFactura+".pdf");	
				}
				Vector<Documento> documentos = new Vector<Documento>(1);
				documentos.add(documento);
				
				/*************** INICIO TRANSACCION ***************/
				tx.begin();
				
				// Genera el envio:
				envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,documentos);
				tx.commit();
				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ENVIO GENERADO OK",10);
				/*************** FIN TRANSACCION ***************/
					
			}else{
				throw new SIGAException("messages.facturacion.almacenar.plantillasEnvioMal");		
			}

		} catch (SIGAException eee) {
    		
			try {tx.rollback();} catch (Exception ee) {}
			
			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+eee.getLiteral(userbean.getLanguage()),10);
    		// ESCRIBO EN EL LOG
			if(generarLog)
				log.writeLogFactura("ENVIO",idPersona,numeroFactura,"message.facturacion.error.envio.factura"+eee.getLiteral(userbean.getLanguage()));
			else{
				String msj=UtilidadesString.getMensajeIdioma(userbean.getLanguage(),"message.facturacion.error.envio.factura")+eee.getLiteral(userbean.getLanguage());
				throw new SIGAException(msj);
			}
			salida=2;
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no cazamos la excepcion
			//throw eee;
			existeAlgunErrorEnvio = true;
    		
		} catch (Exception eee) {
			
			try {tx.rollback();} catch (Exception ee) {}
    		
			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+eee.toString(),10);
    		// ESCRIBO EN EL LOG
			if(generarLog)
				log.writeLogFactura("ENVIO",idPersona,numeroFactura,"message.facturacion.error.envio.factura"+eee.toString());
			else{
				String msj=UtilidadesString.getMensajeIdioma(userbean.getLanguage(),"message.facturacion.error.envio.factura")+eee.toString();
				throw new SIGAException(msj);
			}
			salida=2;
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no cazamos la excepcion
			//throw eee;
			existeAlgunErrorEnvio = true;
    		
		}
		
	}

	/**
     * Notas Jorge PT 118: Generacion de la facturacion rapida de compras y certificados (SolicitudCompraAction y SIGASolicitudesCertificadosAction):
     * - Productos y Servicios > Solicitudes > Compra/Subscripción
     * - Productos y Servicios > Gestión Solicitudes
     * - Certificados > Gestión de solicitudes
     * 
     * @param beanPeticionCompraSuscripcion
     * @param compras
     * @param beanSerieCandidata
     * @return
     * @throws ClsExceptions
     */
    public FacFacturacionProgramadaBean procesarFacturacionRapidaCompras(PysPeticionCompraSuscripcionBean beanPeticionCompraSuscripcion, Vector<PysCompraBean> compras, FacSerieFacturacionBean beanSerieCandidata) throws ClsExceptions {
    	FacFacturacionProgramadaBean beanFacturacionProgramada = new FacFacturacionProgramadaBean();
    	
    	try {
    	    FacFacturacionProgramadaAdm admFacturacionProgramada = new FacFacturacionProgramadaAdm(this.usrbean);
            Date fechaMin=null, fechaMax=null;
                
    	    // Recorro la lista de compras, para insertar las compras
    	    for (int i=0; i<compras.size(); i++) {
    	    	
    	    	// Obtengo la compra actual
    	        PysCompraBean compra = (PysCompraBean) compras.get(i);
    	        
    	        // Obtengo la fecha de la compra
    	        Date fechaAux = GstDate.convertirFecha(compra.getFecha());
    	        
    	        // Obtengo la fecha minima y maxima
    	        if (fechaMin==null || fechaAux.before(fechaMin)) {
    	        	fechaMin = fechaAux;
    	        }    	        
    	        if (fechaMax==null || fechaAux.after(fechaMax)) {
    	        	fechaMax=fechaAux;
    	        }         
    	    }
    	    
    	    // Obtiene la fecha minima y maxima en el formato con hora
    	    String fechaini = GstDate.convertirFechaHora(fechaMin);
    	    String fechafin = GstDate.convertirFechaHora(fechaMax);

    	    // Creo la programacion de la facturacion  	        	    
    	    beanFacturacionProgramada.setIdInstitucion(beanPeticionCompraSuscripcion.getIdInstitucion());
    	    beanFacturacionProgramada.setArchivarFact("0");
    	    beanFacturacionProgramada.setConfDeudor(beanSerieCandidata.getConfigDeudor());
    	    beanFacturacionProgramada.setConfIngresos(beanSerieCandidata.getConfigIngresos());
    	    beanFacturacionProgramada.setCtaClientes(beanSerieCandidata.getCuentaClientes());
    	    beanFacturacionProgramada.setCtaIngresos(beanSerieCandidata.getCuentaIngresos());
    	    beanFacturacionProgramada.setFechaCargo("sysdate");
    	    beanFacturacionProgramada.setFechaInicioProductos(fechaini);
    	    beanFacturacionProgramada.setFechaFinProductos(fechafin);
    	    beanFacturacionProgramada.setFechaInicioServicios(fechaini);
    	    beanFacturacionProgramada.setFechaFinServicios(fechafin);
    	    beanFacturacionProgramada.setFechaProgramacion("sysdate");
    	    beanFacturacionProgramada.setFechaPrevistaGeneracion("sysdate");
    	    beanFacturacionProgramada.setFechaRealGeneracion("sysdate");
    	    beanFacturacionProgramada.setFechaPrevistaConfirmacion(null);
    	    beanFacturacionProgramada.setFechaConfirmacion(null);
    	    beanFacturacionProgramada.setGenerarPDF("1"); 
    	    beanFacturacionProgramada.setEnvio(beanSerieCandidata.getEnvioFactura());    	    
    	    beanFacturacionProgramada.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
    	    beanFacturacionProgramada.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
    	    beanFacturacionProgramada.setIdPrevision(null);
    	    beanFacturacionProgramada.setIdSerieFacturacion(beanSerieCandidata.getIdSerieFacturacion());
    	    beanFacturacionProgramada.setIdTipoPlantillaMail(beanSerieCandidata.getIdTipoPlantillaMail());
    	    beanFacturacionProgramada.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.GENERADA);
    	    
    	    // Obtiene un nuevo identidicador de serie de facturacion
    	    Long idFacturacionProgramada = admFacturacionProgramada.getNuevoID(beanFacturacionProgramada);
    	    beanFacturacionProgramada.setIdProgramacion(idFacturacionProgramada);
    	    
    	    // Obtiene la descripcion
    	    String descripcion = beanSerieCandidata.getNombreAbreviado() + " [" + idFacturacionProgramada + "]";
    	    beanFacturacionProgramada.setDescripcion(descripcion);
    	    
    	    if (!admFacturacionProgramada.insert(beanFacturacionProgramada)) {
    	        throw new ClsExceptions("Error al insertar cliente incluido en serie: " + admFacturacionProgramada.getError());
    	    }
    	    
    	    // Carga los parametros
    	    String resultado[] = new String[2];
			Object[] param_in = new Object[7];
			param_in[0] = beanFacturacionProgramada.getIdInstitucion().toString();
        	param_in[1] = beanFacturacionProgramada.getIdSerieFacturacion().toString();
        	param_in[2] = beanFacturacionProgramada.getIdProgramacion().toString();
        	param_in[3] = this.usrbean.getLanguageInstitucion(); // Idioma
        	param_in[4] = beanPeticionCompraSuscripcion.getIdPeticion().toString();
        	param_in[5] = this.usrbean.getUserName();
        	param_in[6] = "0"; // IdPrevision
        	
        	// Genera la facturacion
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?,?)}", 2, param_in);
        	
        	// Compruebo que ha finalizado correctamente
        	String codretorno = resultado[0];
        	if (!codretorno.equals("0")){
        		throw new ClsExceptions ("Error al generar la Facturación rapida: "+resultado[1]);
        	}
        	
        	// Desbloquea la facturacion programada
			if (!admFacturacionProgramada.updateDirect(beanFacturacionProgramada)) {
    	        throw new ClsExceptions("Error al actualizar la programacion: " + admFacturacionProgramada.getError());
    	    }
    	    
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al realizar generacion de facturacion rápida.");
    	}
    	
    	return beanFacturacionProgramada;
    }
    
    /**
     * Comprueba que la cuenta bancaria existe, no tiene fecha baja y es de cargos
     * @param idInstitucion
     * @param idPersona
     * @param idCuenta
     * @return cuenta bancaria mientras no tenga fecha de baja
     * @throws SIGAException
     * @throws ClsExceptions
     */
    public CenCuentasBancariasBean getCuentaActiva(Integer idInstitucion, Long idPersona, Integer idCuenta) throws SIGAException, ClsExceptions {
    	CenCuentasBancariasBean cuentaBancaria = null;
    	
		// JPT - Renegociacion 118: Devuelve un Hastable con los datos de la cuenta bancaria del cliente
    	CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(this.usrbean);
		Hashtable<?, ?> cuentaBancariaHashtable = cuentasBancariasAdm.selectCuentas(idPersona, idInstitucion, idCuenta);
		
		// JPT - Renegociacion 118: Compruebo que existe la cuenta bancaria del cliente
		if (cuentaBancariaHashtable != null) {
		
			// JPT - Renegociacion 118: Consulta la fecha de baja y el tipo de cuenta (cargos)
			String fechaBajaCuenta = (String) cuentaBancariaHashtable.get(CenCuentasBancariasBean.C_FECHABAJA);
			String sCargo = (String) cuentaBancariaHashtable.get(CenCuentasBancariasBean.C_ABONOCARGO);						
			
			// JPT - Renegociacion 118: Comprueba que no tiene fecha de baja y es de cargos			
			if ((fechaBajaCuenta == null || fechaBajaCuenta.equals("")) && sCargo!=null && (sCargo.equals("T") || sCargo.equals("C"))) {
				cuentaBancaria = (CenCuentasBancariasBean) cuentasBancariasAdm.hashTableToBean(cuentaBancariaHashtable);
			}
		}
    	
		return cuentaBancaria;		
    }
    
    /**
     * Obtiene la cuenta bancaria activa de cargo de la persona, en caso de tener solo una
     * @param idInstitucion
     * @param idPersona
     * @return
     * @throws SIGAException
     * @throws ClsExceptions
     */
    public CenCuentasBancariasBean getCuentaActivaUnica(Integer idInstitucion, Long idPersona) throws SIGAException, ClsExceptions {
    	CenCuentasBancariasBean cuentaBancaria = null;
    		
		// JPT - Renegociacion 118: Consulta las cuentas bancarias activas de cargos de la persona
		CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(this.usrbean);
		List<?> listaCuentasCargo = cuentasBancariasAdm.getCuentasCargo(idPersona, idInstitucion);
		
		// JPT - Renegociacion 118: Compruebo si solo tiene una cuenta de cargo
		if (listaCuentasCargo != null && listaCuentasCargo.size() == 1) {
			cuentaBancaria = (CenCuentasBancariasBean) listaCuentasCargo.get(0);
		}    		    		
		
		return cuentaBancaria;
    }
    
    /**
     * Funcion que realiza la renegociacion:
     * - Facturacion > Gestion de Cobros y Recobros
     * - Facturacion > Facturas > Pagos
     * - Facturacion > Fichero de Devoluciones > Gestion de Devoluciones
     * @param facturaBean
     * @param formaPago
     * @param idCuenta
     * @param observaciones
     * @param fechaRenegociar
     * @param isAutomatica
     * @throws Exception
     */
    public int insertarRenegociar(
    		FacFacturaBean facturaBean,
			String formaPago,
			String idCuenta,
			String observaciones, 
			String fechaRenegociar, 
			boolean isAutomatica) throws Exception {

		int idFormaPago = 0;
		int nuevoEstado = 0;
		boolean actualizarFacturaEnDisco = false;
		int estadoFactura = facturaBean.getEstado().intValue();
		FacFacturaAdm facturaAdm = new FacFacturaAdm (this.usrbean);
		
		do {
			if (estadoFactura == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA) ||
				estadoFactura == Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO) ||
				estadoFactura == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA)) {			
				
				// JPT - Devoluciones 117 - Para devolucion de facturas con comision, se anula la factura anterior y se crea una nueva, con lo que no se actualiza FacFacturaIncluidaEnDisquete	
				if (estadoFactura == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA) && 
						(facturaBean.getComisionIdFactura() == null || facturaBean.getComisionIdFactura().equals(""))) {
					actualizarFacturaEnDisco = true;
				}				
				
				if (formaPago.equalsIgnoreCase("porCaja")) { // JPT - Renegociacion 118: Renegocia por caja
					
					if (estadoFactura == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) { // JPT - Renegociacion 118:  No se puede renegociar dos veces por caja
						return 1;
					}
					
					idFormaPago = ClsConstants.TIPO_FORMAPAGO_METALICO; // JPT - Renegociacion 117: Por caja
					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA);
					idCuenta = null;														
					break;
					
					
				} else if (formaPago.equalsIgnoreCase("cuentaFactura")) { 
					/* JPT - Renegociacion 118: 
					 * 1. Renegocia por la cuenta bancaria activa de cargos de la factura
					 * 2. No renegocia
					 */
					
					if (facturaBean.getIdCuenta() == null) { // JPT - Renegociacion 118:  No tenia cuenta asociada para poder renegociarla por la misma cuenta
						return 2;
					}					
					
					idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA; // JPT - Renegociacion 118: Renegocia por banco
					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
					
					// JPT - Renegociacion 118: Comprueba que la cuenta bancaria existe, no tiene fecha baja y es de cargos
					CenCuentasBancariasBean cuentaBancaria = this.getCuentaActiva(facturaBean.getIdInstitucion(), facturaBean.getIdPersona(), facturaBean.getIdCuenta());					
					
					if (cuentaBancaria != null && cuentaBancaria.getIdCuenta() != null) { // JPT - Renegociacion 118: Encuentra la cuenta para renegociar
						idCuenta = cuentaBancaria.getIdCuenta().toString();
						
					} else { // JPT - Renegociacion 118: No encuentra la cuenta para renegociar
						return 3;
					}
					break;		
					
					
				} else if (formaPago.equalsIgnoreCase("cuentaFactura_cuentaUnica_cuentaServicios")) { 
					/* JPT - Renegociacion 118: 
					 * 1. Renegocia por la cuenta bancaria activa de cargos de la factura
					 * 2. Renegocia por la cuenta bancaria activa de cargos, si solo tiene una cuenta de cargos
					 * 3. Renegocia por la cuenta banacria activa de cargos mas utilizada por los servicios de la persona
					 * 4. No renegocia
					 */
					
					idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA; // JPT - Renegociacion 118: Renegocia por banco
					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
					boolean bEncuentraCuenta = false;
					
					if (facturaBean.getIdCuenta() != null) { // JPT - Renegociacion 118:  No tenia cuenta asociada para poder renegociarla por la misma cuenta
						
						// JPT - Renegociacion 118: Comprueba que la cuenta bancaria existe, no tiene fecha baja y es de cargos
						CenCuentasBancariasBean cuentaBancaria = this.getCuentaActiva(facturaBean.getIdInstitucion(), facturaBean.getIdPersona(), facturaBean.getIdCuenta());
						
						if (cuentaBancaria != null && cuentaBancaria.getIdCuenta() != null) { // JPT - Renegociacion 118: Encuentra la cuenta para renegociar
							idCuenta = cuentaBancaria.getIdCuenta().toString();
							bEncuentraCuenta = true;
						}	
					}					
					
					if (!bEncuentraCuenta) {
						// JPT - Renegociacion 118: Obtiene la cuenta bancaria activa de cargo de la persona, en caso de tener solo una
						CenCuentasBancariasBean cuentaBancaria = this.getCuentaActivaUnica(facturaBean.getIdInstitucion(), facturaBean.getIdPersona());
						
						if (cuentaBancaria != null && cuentaBancaria.getIdCuenta() != null) { // JPT - Renegociacion 118: Encuentra la cuenta para renegociar
							idCuenta = cuentaBancaria.getIdCuenta().toString();
							bEncuentraCuenta = true;
						}	
					}
					
					if (!bEncuentraCuenta) {
						// JPT - Renegociacion 118: Obtiene la cuenta bancaria activa de cargo de la persona mas utilizada para los servicios
						CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(this.usrbean);
						idCuenta = cuentasBancariasAdm.getCuentaActivaServiciosActivos(facturaBean.getIdInstitucion(), facturaBean.getIdPersona());
						
						if (idCuenta!=null) { // JPT - Renegociacion 118: Encuentra la cuenta para renegociar
							bEncuentraCuenta = true;
						}	
					}					
					
					if (!bEncuentraCuenta) {
						return 4;	
					}

					break;						
					
					
				} else if (formaPago.equalsIgnoreCase("porOtroBanco")) { // JPT - Renegociacion 118: Por otro banco activo del cliente
					idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA; // JPT - Renegociacion 118: Por banco
					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
					
					if (idCuenta == null || idCuenta.equals("")) { // JPT - Renegociacion 118: No ha indicado el banco por el que renegociar
						return 5;						
					}
					break;
				}								
			}			
		} while (false);
		
		// Insertamos un nuevo registro en Fac_Renegociacion
		FacRenegociacionBean renegociacionBean = new FacRenegociacionBean();
		renegociacionBean.setComentario(observaciones);
		renegociacionBean.setIdFactura(facturaBean.getIdFactura());
		renegociacionBean.setIdInstitucion(facturaBean.getIdInstitucion());
		renegociacionBean.setIdPersona(facturaBean.getIdPersona());    		
		renegociacionBean.setImporte(facturaBean.getImpTotalPorPagar());
		
		if (fechaRenegociar == null || fechaRenegociar.equals("")) {
			renegociacionBean.setFechaRenegociacion("sysdate");
		} else {
			renegociacionBean.setFechaRenegociacion(fechaRenegociar);
		}
		
		if (isAutomatica) {
			renegociacionBean.setComentario(ClsConstants.TIPO_RENEGOCIA_MASIVA);
		} else {
			renegociacionBean.setComentario(ClsConstants.TIPO_RENEGOCIA_MANUAL);
		}

		// Actualizamos el idCuenta de la tabla FacFactura
		if (idCuenta != null) {
			renegociacionBean.setIdCuenta(new Integer(idCuenta));
			facturaBean.setIdCuenta(new Integer(idCuenta));
		} else {
			facturaBean.setIdCuenta(null);
		}

		FacRenegociacionAdm renegociacionAdm = new FacRenegociacionAdm (this.usrbean);
		Integer idNuevaRenegociacion = renegociacionAdm.getNuevoID(facturaBean.getIdInstitucion(), facturaBean.getIdFactura());
		renegociacionBean.setIdRenegociacion(idNuevaRenegociacion);
		if (!renegociacionAdm.insert(renegociacionBean)) {
			throw new ClsExceptions("Error al insertar la renegociación: " + renegociacionAdm.getError()); 
		}

		// Actualizamos la tabla FacFacturaIncluidaEnDisquete    		
		if (actualizarFacturaEnDisco) {
			FacFacturaIncluidaEnDisqueteAdm facturaDiscoAdm = new FacFacturaIncluidaEnDisqueteAdm (this.usrbean); 
			if (!facturaDiscoAdm.updateRenegociacion(renegociacionBean.getIdInstitucion(), renegociacionBean.getIdFactura(), renegociacionBean.getIdRenegociacion())) {
				throw new ClsExceptions("Error al actualizar la factura incluida en disquete: "+facturaDiscoAdm.getError()); 
			}
		}

		// Actualizamos el estado y forma de pago de la factura
		facturaBean.setEstado(new Integer(nuevoEstado));
		facturaBean.setIdFormaPago(new Integer(idFormaPago)); 
		
		if (!facturaAdm.actualizarFacturaRenegociacion(facturaBean)) {
			throw new ClsExceptions("Error al actualizar la factura renegociada: " + facturaAdm.getError());        
		}
		
		return 0;
    }
    
    /**
     * Aplica la comision de la factura
     * @param institucion
     * @param lineaDevolucion
     * @param aplicaComisionesCliente
     * @param userBean
     * @param fechaDevolucion
     * @return
     * @throws Exception
     */
	public FacFacturaBean aplicarComisionAFactura (String institucion, FacLineaDevoluDisqBancoBean lineaDevolucion, String aplicaComisionesCliente, UsrBean userBean, String fechaDevolucion) throws Exception {
		FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(userBean);
		
		// Obtenemos la factura incluida en disquete		
		Hashtable<String,Object> criteriosFactura = new Hashtable<String,Object>();
		if(lineaDevolucion.getIdInstitucion()!= null)
			criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,lineaDevolucion.getIdInstitucion().toString());
		if(lineaDevolucion.getIdDisqueteCargos()!= null)
			criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,lineaDevolucion.getIdDisqueteCargos().toString());
		if(lineaDevolucion.getIdFacturaIncluidaEnDisquete()!= null)
			criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,lineaDevolucion.getIdFacturaIncluidaEnDisquete().toString());
		
		FacFacturaIncluidaEnDisqueteAdm admFIED= new FacFacturaIncluidaEnDisqueteAdm(userBean);
		Vector<?> clientes = admFIED.selectByPK(criteriosFactura);
		FacFacturaIncluidaEnDisqueteBean beanFacturaIncluidaEnDisquete = (FacFacturaIncluidaEnDisqueteBean) clientes.firstElement();
		
		// Obtenemos datos del cliente deudor
		Hashtable<String,Object> criteriosCliente = new Hashtable<String,Object>();
		if(beanFacturaIncluidaEnDisquete.getIdInstitucion()!= null)
			criteriosCliente.put(CenClienteBean.C_IDINSTITUCION,beanFacturaIncluidaEnDisquete.getIdInstitucion().toString());
		if(beanFacturaIncluidaEnDisquete.getIdPersona()!= null)
			criteriosCliente.put(CenClienteBean.C_IDPERSONA,beanFacturaIncluidaEnDisquete.getIdPersona().toString());
		
		CenClienteAdm admCliente= new CenClienteAdm(userBean);
		Vector<?> comisionesClientes = admCliente.selectByPK(criteriosCliente);
		CenClienteBean beanCliente = (CenClienteBean) comisionesClientes.firstElement();
		
		// Obtenemos el disquete de devoluciones		
		Hashtable<String,Object> criteriosDevolucion = new Hashtable<String,Object>();
		criteriosDevolucion.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,institucion);
		criteriosDevolucion.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,lineaDevolucion.getIdDisqueteDevoluciones());
				
		FacDisqueteDevolucionesAdm admDD = new FacDisqueteDevolucionesAdm(userBean);
		Vector<?> bancos = admDD.selectByPKForUpdate(criteriosDevolucion);
		FacDisqueteDevolucionesBean beanDisqueteDevoluciones = (FacDisqueteDevolucionesBean) bancos.firstElement();		
		
		// Obtenemos el banco del acreedor	
		Hashtable<String,Object> criteriosBanco = new Hashtable<String,Object>();		
		if(beanDisqueteDevoluciones.getIdInstitucion()!= null)
			criteriosBanco.put(FacBancoInstitucionBean.C_IDINSTITUCION,beanDisqueteDevoluciones.getIdInstitucion().toString());
		criteriosBanco.put(FacBancoInstitucionBean.C_BANCOS_CODIGO,beanDisqueteDevoluciones.getBancosCodigo());
		
		FacBancoInstitucionAdm admBI= new FacBancoInstitucionAdm(userBean);
		Vector<?> comisiones = admBI.selectByPK(criteriosBanco);
		FacBancoInstitucionBean beanBancoInstitucion = (FacBancoInstitucionBean) comisiones.firstElement();		
		
		// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
		Hashtable<String,Object> original = new Hashtable<String,Object>();
		if(lineaDevolucion.getIdInstitucion()!= null)
			original.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,lineaDevolucion.getIdInstitucion().toString());
		if(lineaDevolucion.getIdDisqueteDevoluciones()!= null)
			original.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,lineaDevolucion.getIdDisqueteDevoluciones().toString());
		original.put(FacLineaDevoluDisqBancoBean.C_IDRECIBO,lineaDevolucion.getIdRecibo());
		if(lineaDevolucion.getIdDisqueteCargos()!= null)
			original.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS,lineaDevolucion.getIdDisqueteCargos().toString());						
		original.put(FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE,lineaDevolucion.getIdFacturaIncluidaEnDisquete()); 
		original.put(FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS,lineaDevolucion.getDescripcionMotivos());
		if(lineaDevolucion.getGastosDevolucion()!= null)
			original.put(FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION,lineaDevolucion.getGastosDevolucion().toString());
		original.put(FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE,lineaDevolucion.getCargarCliente());
		if(lineaDevolucion.getContabilizada()!= null)
			original.put(FacLineaDevoluDisqBancoBean.C_CONTABILIZADA,lineaDevolucion.getContabilizada().toString());
		lineaDevolucion.setOriginalHash(original);
		if (beanBancoInstitucion.getComisionImporte()==null || beanBancoInstitucion.getComisionImporte()<0.0) {
			lineaDevolucion.setGastosDevolucion(0.0);
		} else {
			lineaDevolucion.setGastosDevolucion(beanBancoInstitucion.getComisionImporte());
		}		
		
		// RGG 15/09/2006 COMISIONES A CARGO DEL CLIENTE
		if (aplicaComisionesCliente!=null && aplicaComisionesCliente.equalsIgnoreCase(ClsConstants.DB_TRUE) && // Tiene que tener marcado aplicar comisiones
				beanCliente.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE) && // Tiene que tener el ciente activado las comisiones
				beanBancoInstitucion.getComisionImporte() > 0.0) { // Tiene que tener una comision de la gestion de cuentas bancarias mayor que cero 
			
			// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
			lineaDevolucion.setCargarCliente("S");
						
			if (!admLDDB.update(lineaDevolucion)) {
				throw new ClsExceptions("Error porque no actualiza la línea de devoluciones");
			}
			
			// JPT - Devoluciones 117 - Obtenemos la factura original			
	    	Hashtable<String,Object> hFacFactura = new Hashtable<String,Object>();
	    	UtilidadesHash.set(hFacFactura, FacFacturaBean.C_IDINSTITUCION, beanFacturaIncluidaEnDisquete.getIdInstitucion());
	    	UtilidadesHash.set(hFacFactura, FacFacturaBean.C_IDFACTURA, beanFacturaIncluidaEnDisquete.getIdFactura());		    	
	    	
	    	FacFacturaAdm admFacFactura = new FacFacturaAdm(userBean);
	    	Vector<?> vFacFactura = admFacFactura.selectByPK(hFacFactura);
	    	
	    	if (vFacFactura== null || vFacFactura.size() != 1) {
	    		throw new ClsExceptions("Error porque no encuentra la factura a devolver: " + lineaDevolucion.getIdFacturaIncluidaEnDisquete());
	    	}
			FacFacturaBean beanFacFactura = (FacFacturaBean) vFacFactura.get(0);
			
			// JPT - Devoluciones 117 - Calcula la forma de pago y el estado de la factura	
			Integer idFormaPago = beanFacFactura.getIdFormaPago(); // Inicialmente tiene la forma de pago de la factura
			beanFacFactura.setIdFormaPago(idFormaPago);
			
			// JPT - Devoluciones 117 - Indico que el estado de la factura actual es anulada
			beanFacFactura.setEstado(new Integer(ClsConstants.ESTADO_FACTURA_ANULADA));
			
			// JPT - Devoluciones 117 - Actualizo la factura actual			
			if (!admFacFactura.update(beanFacFactura)) {
				throw new ClsExceptions("Error porque no anula la factura de devoluciones actual");
			}				
			
			// JPT - Devoluciones 117 - Indico la fecha de devolucion
			beanFacFactura.setFechaEmision(fechaDevolucion);

			// JPT - Devoluciones 117 - Indico el estado de la factura calculado anteriormente
			beanFacFactura.setEstado(new Integer(ClsConstants.ESTADO_FACTURA_DEVUELTA)); // Inicialmente tiene estado devuelta
			
			// JPT - Devoluciones 117 - Pone en FAC_FACTURA.COMISIONIDFACTURA el identificador de la factura original  
			beanFacFactura.setComisionIdFactura(beanFacFactura.getIdFactura());
			
			// JPT - Devoluciones 117 - Obtiene nuevo identificador de factura
			String sNuevoIdFactura = admFacFactura.getNuevoID(beanFacFactura.getIdInstitucion().toString()).toString();
			
			// JPT - Devoluciones 117 - Asigna el nuevo identificador de factura
			beanFacFactura.setIdFactura(sNuevoIdFactura);
			
			// JPT - Devoluciones 117 - Obtiene nuevo numero de factura
			Hashtable<String, Object> hNuevoNumeroFactura = admFacFactura.obtenerNuevoNumeroFactura(beanFacFactura.getIdInstitucion().toString(), beanFacFactura.getIdSerieFacturacion().toString());
			String sContadorPrefijo = (String) hNuevoNumeroFactura.get(AdmContadorBean.C_PREFIJO);
			String sContadorContador = (String) hNuevoNumeroFactura.get("NUEVOCONTADOR");
			String sContadorSufijo = (String) hNuevoNumeroFactura.get(AdmContadorBean.C_SUFIJO);
			
			// JPT - Devoluciones 117 - Asigna el nuevo numero de factura
			beanFacFactura.setNumeroFactura(sContadorPrefijo + sContadorContador + sContadorSufijo);				
			
			// JPT - Devoluciones 117 - Actualiza el contador
			AdmContadorAdm admContador = new AdmContadorAdm(userBean);
			AdmContadorBean beanContador = (AdmContadorBean) admContador.hashTableToBean(hNuevoNumeroFactura);
			beanContador.setOriginalHash(hNuevoNumeroFactura);
			beanContador.setContador(new Long(sContadorContador));												
			if (!admContador.update(beanContador)) {
				throw new ClsExceptions("Error porque no actualiza el contador");
			}							
			
			// JPT - Devoluciones 117 - Calcula el importe del iva
			double importeComision = beanBancoInstitucion.getComisionImporte();
			double importeIvaComision = UtilidadesNumero.redondea(beanBancoInstitucion.getComisionImporte() * beanBancoInstitucion.getComisionIVA() / 100, 2);						
			
			// JPT - Devoluciones 117 - Calcula los importes de la factura final con los importes de la comision
			beanFacFactura.setImpTotalPorPagar(beanFacFactura.getImpTotalPorPagar() + importeComision + importeIvaComision);														
			beanFacFactura.setImpTotalNeto(beanFacFactura.getImpTotalNeto() + importeComision);
			beanFacFactura.setImpTotalIva(beanFacFactura.getImpTotalIva() + importeIvaComision);
			beanFacFactura.setImpTotal(beanFacFactura.getImpTotal() + importeComision + importeIvaComision);						
			
			// JPT - Devoluciones 117 - Inserta la nueva factura
			if (!admFacFactura.insert(beanFacFactura)) {
				throw new ClsExceptions("Error porque no inserta la nueva factura con la comisión");
			}						
				
			// JPT - Devoluciones 117 - Obtenemos las lineas de la factura
			FacLineaFacturaAdm admLineaFactura = new FacLineaFacturaAdm(userBean);
			Vector<?> vFacLineaFactura = admLineaFactura.select(hFacFactura);
			
			// JPT - Devoluciones 117 - Recorro las lineas de la factura
			FacLineaFacturaBean beanFacLineaFactura = null;
			long maximoNumeroLinea = 0;
			long maximoNumeroOrden = 0;
			for (int contadorLineaFactura=0; contadorLineaFactura<vFacLineaFactura.size(); contadorLineaFactura++) {								
				
				// JPT - Devoluciones 117 - Obtengo una linea de la factura
				beanFacLineaFactura = (FacLineaFacturaBean) vFacLineaFactura.get(contadorLineaFactura);
				
				// JPT - Devoluciones 117 - Asigna el nuevo identificador de factura a la linea de la factura
				beanFacLineaFactura.setIdFactura(sNuevoIdFactura);
				
				// JPT - Devoluciones 117 - Inserto la nueva linea de la factura
				if (!admLineaFactura.insert(beanFacLineaFactura)) {
					throw new ClsExceptions("Error porque no inserta la nueva línea de factura de devoluciones con comisión");
				}		
				
				// JPT - Devoluciones 117 - Calculos para obtener el mayor numero de linea y orden
				if (beanFacLineaFactura.getNumeroLinea() > maximoNumeroLinea)
					maximoNumeroLinea = beanFacLineaFactura.getNumeroLinea();																		
				if (beanFacLineaFactura.getNumeroOrden() > maximoNumeroOrden)
					maximoNumeroOrden = beanFacLineaFactura.getNumeroOrden();
			}						
			
			// JPT - Devoluciones 117 - Calculo el campo CTAIVA
			PysTipoIvaAdm admTipoIva = new PysTipoIvaAdm(userBean);
			String sCTAIVA = admTipoIva.obtenerCTAIVA(beanFacFactura.getIdInstitucion().toString(), beanBancoInstitucion.getComisionIVA().toString());
			
			// JPT - Devoluciones 117 - Genero un objeto para la nueva linea con la comision
			beanFacLineaFactura = new FacLineaFacturaBean();
			beanFacLineaFactura.setIdInstitucion(beanFacFactura.getIdInstitucion());
			beanFacLineaFactura.setIdFactura(sNuevoIdFactura); // Asigna el nuevo identificador de factura a la linea de la factura							
			beanFacLineaFactura.setNumeroLinea(maximoNumeroLinea + 1); // Asigno el siguiente numero de linea
			beanFacLineaFactura.setNumeroOrden(maximoNumeroOrden + 1); // Asigno el siguient numero de orden
			beanFacLineaFactura.setCantidad(1); // Se indica que es una unidad de comisión
			beanFacLineaFactura.setImporteAnticipado(0.0); // Se indica que no tiene importe anticipado
			beanFacLineaFactura.setDescripcion(beanBancoInstitucion.getComisionDescripcion()); // Obtiene la descripcion de la comision del banco del acreedor
			beanFacLineaFactura.setPrecioUnitario(beanBancoInstitucion.getComisionImporte()); // Obtiene el importe de la comision del banco del acreedor
			beanFacLineaFactura.setIva(beanBancoInstitucion.getComisionIVA().floatValue()); // Obtiene el iva de la comision del banco del acreedor
			beanFacLineaFactura.setCtaProductoServicio(beanBancoInstitucion.getComisionCuentaContable()); // Obtiene la cuenta contable del banco del acreedor
			beanFacLineaFactura.setCtaIva(sCTAIVA);
			beanFacLineaFactura.setIdFormaPago(idFormaPago); // Indica la forma de pago de la factura	                  
			
			// JPT - Devoluciones 117 - Inserto la nueva linea de la factura
			if (!admLineaFactura.insert(beanFacLineaFactura)) {
				throw new ClsExceptions("Error porque no inserta la nueva línea de factura de devoluciones con comisión");
			}			
			
			// JPT: Devuelve los resultados de la nueva factura
			return beanFacFactura; 		
			
		} else {			
			// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
			lineaDevolucion.setCargarCliente("N"); // Diferencia con lo de arriba
			
			if (!admLDDB.update(lineaDevolucion)) {
				throw new ClsExceptions("Error porque no actualiza la línea de devoluciones");
			}
			
			// No duplico lo siguiente porque no es necesario		
			
			// JPT: Devuelve los resultados de la nueva factura
		}
		
		return null;
	}

	/**
	 * Genera facturas mediante el proceso automatico (SIGASvlProcesoFacturacion)
	 * @param idInstitucion
	 * @param idSerieFacturacion
	 * @param idProgramacion
	 * @throws Exception
	 */
	private void generandoFacturacion(String idInstitucion, String idSerieFacturacion, String idProgramacion) throws Exception {
		
		ClsLogging.writeFileLog("### Inicio generarFicheroPrevisiones institución: " + idInstitucion, 7);
		
		UserTransaction tx = this.usrbean.getTransactionPesada(); 
		FacFacturacionProgramadaAdm admFacturacionProgramada = new FacFacturacionProgramadaAdm(this.usrbean);		
		
		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, FacFacturacionProgramadaBean.C_IDPROGRAMACION};
		String nombreFichero = "GENERACION_" + idSerieFacturacion + "_" + idProgramacion;    
		Hashtable<String,Object> hashEstado = new Hashtable<String,Object>();
		UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion);
    	UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
    	UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion);
    	
		try {			
			ClsLogging.writeFileLog("### Procesando GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + ")", 7);
			
			// Carga los parametros
			String resultado[] = new String[2];
			Object[] param_in = new Object[7];
			param_in[0] = idInstitucion;
        	param_in[1] = idSerieFacturacion;
        	param_in[2] = idProgramacion;
        	param_in[3] = this.usrbean.getLanguageInstitucion(); // Idioma
        	param_in[4] = ""; // IdPeticion
        	param_in[5] = this.usrbean.getUserName();
        	param_in[6] = "0"; // IdPrevision	
        	
        	try {
        		ClsLogging.writeFileLog("### Inicio GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + ")",7);  
        		tx.begin();
        		resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?,?)}", 2, param_in);

        	} catch (Exception e) {
        		ClsLogging.writeFileLog("### Fin GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), excepcion " + e.getMessage(), 7);	     		
			    throw new ClsExceptions(UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"facturacion.nuevaPrevisionFacturacion.mensaje.procesoPlSQLERROR") + 
			    		"(Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "; CodigoError:" + e.getMessage() + ")");
			}

			String codretorno = resultado[0];
			
			if (!codretorno.equals("0")) {				
				ClsLogging.writeFileLog("### Fin GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), finalizada con errores", 7);				
				throw new ClsExceptions(UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"facturacion.nuevaPrevisionFacturacion.mensaje.generacionFicheroERROR") + 
			    		"(Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "; CodigoError:" + codretorno + ")");
				
			} else {
				ClsLogging.writeFileLog("### Fin GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), finalizada correctamente",7);
				
				/** ACTUALIZAMOS ESTADO A GENERADA **/
				ClsLogging.writeFileLog("### CAMBIANDO A ESTADO GENERADA ",7);
				String [] campos = {FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION,FacFacturacionProgramadaBean.C_LOGERROR};
				UtilidadesHash.set(hashEstado,FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.GENERADA); //Si todo finaliza correctamente, se pasa a estado GENERADO
				UtilidadesHash.setForCompare(hashEstado,FacFacturacionProgramadaBean.C_LOGERROR,"");				
				if (!admFacturacionProgramada.updateDirect(hashEstado,claves,campos)) {
			        throw new ClsExceptions("### Error al actualizar el estado de la GENERACION.");
			    }							
				tx.commit();				
				
				
				/****** INICIAMOS LA GENERACION DEL INFORME *******/
				try {
					ClsLogging.writeFileLog("### Inicio datosInforme GENERACION",7);
					String nameFile = generarInformeGeneracion(idInstitucion, idSerieFacturacion, idProgramacion);
					// Si la previsión está vacía
					if (nameFile == null || nameFile.length() == 0) {
						ClsLogging.writeFileLog("### Inicio creación fichero log GENERACION sin datos", 7);
						controlarEstadoErrorGeneracion(tx, admFacturacionProgramada, claves, hashEstado, nombreFichero, FacEstadoConfirmFactBean.GENERADA);
						ClsLogging.writeFileLog("### Fin creación fichero log GENERACION sin datos", 7);
	
					} else {
						ClsLogging.writeFileLog("### GENERACION finalizada correctamente con datos ", 7);
						String[] camposInforme = {FacFacturacionProgramadaBean.C_NOMBREFICHERO, FacFacturacionProgramadaBean.C_LOGERROR};
						UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_NOMBREFICHERO, nameFile);
						UtilidadesHash.setForCompare(hashEstado, FacFacturacionProgramadaBean.C_LOGERROR, "");
	
						tx.begin();
						if (!admFacturacionProgramada.updateDirect(hashEstado, claves, camposInforme)) {
							throw new ClsExceptions("### Error al actualizar el nombre del fichero de la GENERACION.");
						}
						tx.commit();
					}
	        	} catch (Exception e) {
	        		ClsLogging.writeFileLog("### Excepcion " + e.getMessage(), 7);	     		
	        		throw new ClsExceptions(UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"facturacion.nuevaPrevisionFacturacion.mensaje.generacionFicheroERROR") +
	    					"(Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "; CodigoError:" + e.getMessage() + ")");
				}					
			}
			
		} catch (Exception e) {
			try { // Tratamiento rollback
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e2) {}		
			
			// Le cambio el estado a error
			try { 
				controlarEstadoErrorGeneracion(tx,admFacturacionProgramada,claves,hashEstado,nombreFichero, FacEstadoConfirmFactBean.ERROR_GENERACION);	
				
			} catch(Exception e2){
				try { // Tratamiento rollback
					if (Status.STATUS_ACTIVE  == tx.getStatus()){
						tx.rollback();
					}
				} catch (Exception e3) {}						
			}
			
			throw e;			
		}
	}
	
	public String generarInformeGeneracion(String idInstitucion, String idSerieFacturacion, String idProgramacion) throws Exception {
		String nameFile = null;		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sRutaJava = rp.returnProperty("facturacion.directorioPrevisionesJava");
		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
		sRutaJava = sRutaFisicaJava + File.separator + sRutaJava + File.separator + idInstitucion;		
		String nombreFichero = "GENERACION_" + idSerieFacturacion + "_" + idProgramacion;    
		
		// Consulto los datos de la prevision y genero el fichero
		Hashtable<String, Object> hashWhere = new Hashtable<String, Object>();
		UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDTIPOINFORME, "PREV");
		UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDINSTITUCION, "0");

		try {
			AdmInformeAdm admInforme = new AdmInformeAdm(this.usrbean);
			Vector<?> vInforme = admInforme.select(hashWhere);

			if (vInforme != null) {
				for (int dv = 0; dv < vInforme.size(); dv++) {

					AdmInformeBean beanInforme = (AdmInformeBean) vInforme.get(dv);

					ArrayList<HashMap<String, String>> filtrosInforme = new ArrayList<HashMap<String, String>>();

					HashMap<String, String> filtro = new HashMap<String, String>();
					filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDIOMA");
					filtro.put("VALOR", this.usrbean.getLanguageInstitucion().toString());
					filtrosInforme.add(filtro);

					filtro = new HashMap<String, String>();
					filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDSERIEFACTURACION");
					filtro.put("VALOR", idSerieFacturacion);
					filtrosInforme.add(filtro);

					filtro = new HashMap<String, String>();
					filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDPROGRAMACION");
					filtro.put("VALOR", idProgramacion);
					filtrosInforme.add(filtro);

					filtro = new HashMap<String, String>();
					filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDINSTITUCION");
					filtro.put("VALOR", idInstitucion);
					filtrosInforme.add(filtro);

					beanInforme.setNombreSalida(nombreFichero);

					ClsLogging.writeFileLog("### Inicio generación fichero excel GENERACION", 7);

					ArrayList<File> fichPrev = InformePersonalizable.generarInformeXLS(beanInforme, filtrosInforme, sRutaJava, this.usrbean);

					ClsLogging.writeFileLog("### Fin generación fichero excel GENERACION", 7);

					if (fichPrev != null && fichPrev.size() > 0) {
						nameFile = fichPrev.get(0).getName();
					
					} else{
						//Generamos un fichero de Error
						File ficheroGenerado = null;
						BufferedWriter out;
						ficheroGenerado = new File (sRutaJava + File.separator + nombreFichero + ".xls");
						if (ficheroGenerado.exists())
							ficheroGenerado.delete();
						ficheroGenerado.createNewFile();
						out = new BufferedWriter(new FileWriter(ficheroGenerado));
						out.write("No se ha podido facturar nada. Compruebe la configuracion y el periodo indicado\t");
						out.close();	
						
						nameFile = ficheroGenerado.getName();
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
		
		return nameFile;
	}

	/**
	 * @param sRutaJava 
	 * @param nombreFichero 
	 * @throws Exception 
	 * 
	 */
	private void controlarEstadoErrorGeneracion(UserTransaction tx, FacFacturacionProgramadaAdm admProg,String [] claves,Hashtable<String,Object> hashEstado, String nombreFichero, Integer estadoFin) throws Exception {
		try {
			String [] campos = {FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION,FacFacturacionProgramadaBean.C_LOGERROR};
			UtilidadesHash.set(hashEstado,FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, estadoFin); //ESTADO ERROR GENERAION 
			UtilidadesHash.set(hashEstado,FacFacturacionProgramadaBean.C_LOGERROR,"LOG_FAC_" + nombreFichero + ".log.xls");
			
			if (Status.STATUS_NO_TRANSACTION == tx.getStatus())
				tx.begin();

			if (!admProg.updateDirect(hashEstado,claves,campos)) {
			      throw new ClsExceptions("Error al actualizar el estado de la generación. finalizada con errores.");
			}
			
			//COMPONEMOS LA RUTA DONDE SE GUARDARAN LOS LOG DE ERROR
		    ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String pathFichero = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\"; 
			
			//Generamos un fichero de log
			File ficheroGenerado = null;
			BufferedWriter out;
			ficheroGenerado = new File (pathFichero + sBarra + hashEstado.get(FacFacturacionProgramadaBean.C_IDINSTITUCION) + sBarra + "LOG_FAC_" + nombreFichero + ".log.xls");

			if (ficheroGenerado.exists())
				ficheroGenerado.delete();
			
			ficheroGenerado.createNewFile();
			out = new BufferedWriter(new FileWriter(ficheroGenerado));
			out.write("No se ha podido facturar nada. Compruebe la configuracion y el periodo indicado\t");
			out.close();			
			
			tx.commit();
			
		} catch (Exception e) {
			throw e;
		} 
	}
}