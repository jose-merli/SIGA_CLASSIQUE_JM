/*
 * Created on 13-abr-2005
 *
 */
package com.siga.facturacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FacBancoInstitucionBean;
import com.siga.beans.FacClienIncluidoEnSerieFacturAdm;
import com.siga.beans.FacClienIncluidoEnSerieFacturBean;
import com.siga.beans.FacDisqueteCargosAdm;
import com.siga.beans.FacDisqueteCargosBean;
import com.siga.beans.FacDisqueteDevolucionesAdm;
import com.siga.beans.FacDisqueteDevolucionesBean;
import com.siga.beans.FacEstadoConfirmFactBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturaIncluidaEnDisqueteAdm;
import com.siga.beans.FacFacturaIncluidaEnDisqueteBean;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacFacturacionSuscripcionAdm;
import com.siga.beans.FacLineaDevoluDisqBancoAdm;
import com.siga.beans.FacLineaDevoluDisqBancoBean;
import com.siga.beans.FacPlantillaFacturacionAdm;
import com.siga.beans.FacRenegociacionAdm;
import com.siga.beans.FacRenegociacionBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.FacTiposProduIncluEnFactuAdm;
import com.siga.beans.FacTiposProduIncluEnFactuBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysCompraAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysFormaPagoProductoAdm;
import com.siga.beans.PysFormaPagoProductoBean;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.PysProductosSolicitadosAdm;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;


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
  	
	public void procesarFactura(String idInstitucion, String idSerie, String idProgramacion, UsrBean userBean) throws SIGAException{
		
		//UsrBean usr			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		//String lenguaje 	= usr.getLanguage();		
		UserTransaction tx	= null;
		try{
			//tx = idUsua.getTransaction(); 
			//tx.begin();
			
			Object[] param_in = new Object[6];
        	param_in[0] = idInstitucion;
        	param_in[1] = idSerie;
        	param_in[2] = idProgramacion;
        	param_in[3] = userBean.getLanguageInstitucion();	//idioma;
        	param_in[4] = ""; 				// idUsuario;
        	param_in[5] = userBean.getUserName(); 				// idUsuario;
        	
        	String resultado[] = new String[2];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?)}", 2, param_in);
        	String codretorno = resultado[0];
        	if (!codretorno.equals("0")){
        		 throw new ClsExceptions ("Error al generar la Facturación");
        	}
 
        } catch (Exception e) { 
			throw new SIGAException("messages.updated.error",e);
		}

		

	}
	
	
	/**
	 * Método estático para el procesado automático de facturacion
	 * 
	 * @param idInstitucion
	 * @param idUsuario
	 * @throws SIGAException
	 */
	public static void procesarFacturas(String idInstitucion, UsrBean userBean)
		throws SIGAException,ClsExceptions{
	    
		UserTransaction tx = null;
		UsrBean usr = new UsrBean();
			
		tx = (UserTransaction) usr.getTransactionPesada();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer("1"), idInstitucion);
   	    Vector vDatos = new Vector();
		FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
		String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1"; 
		sWhere += " and ";
		sWhere += FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NULL";
		sWhere += " and "+FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION + " IS NOT NULL ";
		sWhere += " and "+FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION + " <= sysdate ";
		sWhere += " and "+FacFacturacionProgramadaBean.C_LOCKED + "='0' ";
		String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
		
		vDatos = factAdm.selectDatosFacturacionBean(sWhere,codigos, orden);
		
	   
	    
	    for (int i=0;i<vDatos.size();i++){
	    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
    		Facturacion factura = new Facturacion(factBean);
	    	try {

	    		// si no esta bloqueada
	    		if (factBean.getLocked().equals("1")) {
					throw new Exception("bloqueado");
				}

	    		////// TRANSACCION //////
	    		tx.begin();
	    		////// TRANSACCION //////

				// la bloqueamos
				factBean.setLocked("1");
				factAdm.updateDirect(factBean);

	    		////// TRANSACCION //////
	    		tx.commit();
	    		////// TRANSACCION //////

	    		////// TRANSACCION //////
	    		tx.begin();
	    		////// TRANSACCION //////

	    		// la procesamos
	    		factura.procesarFactura(idInstitucion, ""+factBean.getIdSerieFacturacion(), ""+factBean.getIdProgramacion(), userBean);

	    		////// TRANSACCION //////
	    		tx.commit();
	    		////// TRANSACCION //////

	    		
	    		////// TRANSACCION //////
	    		tx.begin();
	    		////// TRANSACCION //////

				// la desbloqueamos
				Hashtable ht = new Hashtable();
				ht.put(FacFacturacionProgramadaBean.C_IDINSTITUCION,idInstitucion);
				ht.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,factBean.getIdSerieFacturacion().toString());
				ht.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION,factBean.getIdProgramacion().toString());
				Vector v = factAdm.selectByPK(ht);
				FacFacturacionProgramadaBean b = null;
				if (v!=null && v.size()>0) {
					b = (FacFacturacionProgramadaBean) v.get(0);
					b.setLocked("0");
					factAdm.updateDirect(b);
				}

	    		////// TRANSACCION //////
	    		tx.commit();
	    		////// TRANSACCION //////

	    		ClsLogging.writeFileLog("### PROCESADO facturación AUTOMATICA " ,7);
	    		
	    	} catch (SIGAException e) {
				
	    		////// TRANSACCION //////
	    		try {
	    			tx.begin();
	    		} catch (Exception ee) {}
	    		////// TRANSACCION //////

	    		// la desbloqueamos
	    		factBean.setLocked("0");
				factAdm.updateDirect(factBean);

	    		////// TRANSACCION //////
	    		try {
	    			tx.commit();
	    		} catch (Exception ee) {}
	    		////// TRANSACCION //////
	    		
				// RGG 13-09-2005 para que siga procesando otros envios automaticos
	    		ClsLogging.writeFileLogError("@@@ Error procesando facturación AUTOMATICA " ,e,3);
	    		
	    	} catch (Exception e) {

	    		////// TRANSACCION //////
	    		try {
	    			tx.begin();
	    		} catch (Exception ee) {}
	    		////// TRANSACCION //////
	    		
	    		// la desbloqueamos siempre que no lo estuviera ya
	    		if (!e.getMessage().equals("bloqueado")) {
	    			factBean.setLocked("0");
	    			factAdm.updateDirect(factBean);
	    		}

	    		////// TRANSACCION //////
	    		try {
	    			tx.commit();
	    		} catch (Exception ee) {}
	    		////// TRANSACCION //////

	    		// RGG 13-09-2005 para que siga procesando otros envios automaticos
	    		ClsLogging.writeFileLogError("Error procesando facturación AUTOMATICA " ,e,3);
	    	}
	    }
	}
	

	
	
	public void confirmarProgramacionesFacturasInstitucion(HttpServletRequest request, String idInstitucion, UsrBean userBean) {
		
		
		try {
			ClsLogging.writeFileLog("CONFIRMAR PROGRAMACIONES FACTURAS INSTITUCION: "+idInstitucion,10);
			
   			// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p = new ReadProperties ("SIGA.properties");
			
	    	// ficheros de log
			String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
			String nombreFichero = "";
			SIGALogging log=null;
			// obtención de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer("1"), idInstitucion);
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 ";
			// para fechas previstas de confirmacion adecuadas 
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM+ " IS NOT NULL ";
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM+ " <= SYSDATE";
			// solo las que estan generadas 
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAREALGENERACION+ " IS NOT NULL ";
			// para los estados de confirmacion adecuados
			sWhere += " and (";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_PENDIENTE;
			sWhere += " or ";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA;
			sWhere += " ) ";
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM};
			
			Vector vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0;i<vDatos.size();i++){

				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("CONFIRMAR FACTURACION PROGRAMADA: "+idInstitucion+" " +factBean.getIdSerieFacturacion()+" " +factBean.getIdProgramacion(),10);

		    	// fichero de log
				nombreFichero = "LOG_CONFIRM_FAC_"+ factBean.getIdInstitucion()+"_"+factBean.getIdSerieFacturacion()+"_"+factBean.getIdProgramacion()+".log.xls"; 
				log = new SIGALogging(pathFichero2+sBarra2+factBean.getIdInstitucion()+sBarra2+nombreFichero);
				try {
					confirmarProgramacionFactura(factBean,request,false,log,true,true);	
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
//			ReadProperties p = new ReadProperties ("SIGA.properties");
			
	    	// ficheros de log
			String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
			String nombreFichero = "";
			SIGALogging log=null;
			// obtención de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer("1"), idInstitucion);
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 ";
			// para fechas previstas de confirmacion adecuadas 
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM+ " IS NOT NULL ";
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM+ " <= SYSDATE";
			// solo las que estan generadas 
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAREALGENERACION+ " IS NOT NULL ";
			// para los estados de confirmacion adecuados
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA;
			// para las que tienen pdf programados pero no generados
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROGRAMADA;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM};
			
			Vector vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0;i<vDatos.size();i++){

				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("CONFIRMAR FACTURACION PROGRAMADA: "+idInstitucion+" " +factBean.getIdSerieFacturacion()+" " +factBean.getIdProgramacion(),10);

		    	// fichero de log
				nombreFichero = "LOG_CONFIRM_FAC_"+ factBean.getIdInstitucion()+"_"+factBean.getIdSerieFacturacion()+"_"+factBean.getIdProgramacion()+".log.xls"; 
				log = new SIGALogging(pathFichero2+sBarra2+factBean.getIdInstitucion()+sBarra2+nombreFichero);
				try {
					confirmarProgramacionFactura(factBean,request,false,log,true,true);
					generarZip(
							factBean.getIdInstitucion().toString(),
							factBean.getIdSerieFacturacion().toString(),
							factBean.getIdProgramacion().toString());
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
	
	public void generarPDFsYenviarFacturasProgramacion(HttpServletRequest request, String idInstitucion, UsrBean userBean){
		
		
		try {
			ClsLogging.writeFileLog("GENERAR PDF DE FACTURAS POR INSTITUCION: "+idInstitucion,10);
			UserTransaction tx = null;
			boolean envio;
			tx = (UserTransaction) userBean.getTransactionPesada();
			
   			// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties p = new ReadProperties ("SIGA.properties");
			
	    	// ficheros de log
			String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
			String nombreFichero = "";
			SIGALogging log=null;
			// obtención de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer("1"), idInstitucion);
			String sWhere=" where " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 ";
			// para fechas previstas de confirmacion adecuadas 
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM+ " IS NOT NULL ";
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM+ " <= SYSDATE";
			// solo las que estan generadas 
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_FECHAREALGENERACION+ " IS NOT NULL ";
			// para los estados de confirmacion adecuados
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA;
			// para las que tienen pdf programados pero no generados
			sWhere += " and ";
			sWhere += FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROGRAMADA;
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			Vector vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0;i<vDatos.size();i++){

				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("GENERAR PDFs Y ENVIAR FACTURACION PROGRAMADA: "+idInstitucion+" " +factBean.getIdSerieFacturacion()+" " +factBean.getIdProgramacion(),10);

		    	// fichero de log
				nombreFichero = "LOG_CONFIRM_FAC_"+ factBean.getIdInstitucion()+"_"+factBean.getIdSerieFacturacion()+"_"+factBean.getIdProgramacion()+".log.xls"; 
				log = new SIGALogging(pathFichero2+sBarra2+factBean.getIdInstitucion()+sBarra2+nombreFichero);
				try {
					/*if (factBean.getEnvio().equals("0"))
						envio = false;
					 	else envio = true;*/
					
					FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
		    		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION,FacFacturacionProgramadaBean.C_IDPROGRAMACION,FacFacturacionProgramadaBean.C_IDSERIEFACTURACION};
		    		String [] camposFactura = {FacFacturacionProgramadaBean.C_FECHACONFIRMACION,FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM,
		    				FacFacturacionProgramadaBean.C_ARCHIVARFACT,FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION};
		    		Hashtable hashNew = new Hashtable();	
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, factBean.getIdInstitucion());
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, factBean.getIdProgramacion());
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,factBean.getIdSerieFacturacion() );
		    		//UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM, "sysdate");
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA);

					
					/*generaryEnviarProgramacionFactura(request, userBean, 
							factBean.getIdInstitucion(), 
							factBean.getIdSerieFacturacion(), 
							factBean.getIdProgramacion(), envio, log, tx);*/
					generarPdfEnvioProgramacionFactura(tx, facadm, factBean, request, log,  
							factBean.getIdSerieFacturacion().toString(), factBean.getIdProgramacion().toString(), claves,
							camposFactura, hashNew, true);
					/*
					confirmarProgramacionFactura(factBean,request,false,log,true,true);
					generarZip(
							factBean.getIdInstitucion().toString(),
							factBean.getIdSerieFacturacion().toString(),
							factBean.getIdProgramacion().toString());*/
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
		String sNombreFichero = "";
		String sRutaJava = "";
		String sRutaTemporal = "";
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");
		sRutaJava = rp.returnProperty("facturacion.directorioFacturaPDFJava");
		sRutaTemporal = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava") +
		rp.returnProperty("facturacion.directorioFacturaPDFJava");

		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava");

		sRutaJava = sRutaFisicaJava +  sRutaJava;
		sNombreFichero = idSerieFacturacion+"_"+idProgramacion;

		String sExtension = ".zip";
		ArrayList lista=new ArrayList();

		sNombreFichero += sExtension;
		sRutaJava += File.separator + idInstitucion
		+ File.separator+ idSerieFacturacion+"_"+idProgramacion+ File.separator/*+ sNombreFichero*/;

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
	
	
	private void doZip(String rutaServidorDescargasZip, String nombreFicheroPDF, ArrayList ficherosPDF) throws ClsExceptions	{
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

	
	
	
	
    public int compruebaNumeroFacturas(PysCompraBean compra) throws ClsExceptions 
	{
        int salida = 0;
    	try {
    	    CerSolicitudCertificadosAdm adm = new CerSolicitudCertificadosAdm(this.usrbean);
    	    
    		String sql = " select count(*) NUMERO " +
    		    " from pys_compra c " +
    		    " where c.fechabaja is null " +
    		    " and   to_char(c.fecha,'YYYY/MM/DD') = to_char(sysdate,'YYYY/MM/DD')" +
    		    " and   c.idpersona=" + compra.getIdPersona() +
    		    " and   c.idtipoproducto=" + compra.getIdTipoProducto() + 
    		    " and   c.idproducto=" + compra.getIdProducto() + 
    		    " and   c.idinstitucion=" + compra.getIdInstitucion() +
    			" and   c.idfactura is null";
		    	
    		Vector v = adm.selectGenerico(sql);
    		if (v.size()>0) {
    			Hashtable h = (Hashtable) v.get(0);
    			salida = new Integer(UtilidadesHash.getString(h, "NUMERO")).intValue();
    		}

    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Numero de facturas por compra.");
    	}

    	return salida;
    }


    /**
     * Genera una facturación rápida de compra de un certificado utilizando 
     * como modelo la serie de facturacion genérica.
     * @param compra
     * @return serie de facturación temporal para la operacion
     * @throws ClsExceptions
     */
    public FacSerieFacturacionBean procesarFacturacionRapidaCompraCertificado(PysCompraBean compra) throws ClsExceptions 
	{
        FacSerieFacturacionBean salida = null;
    	try {
    	    FacSerieFacturacionAdm admSerie = new FacSerieFacturacionAdm(this.usrbean);
    	    
    	    // alta de la serie de facturacion temporal
    	    salida = admSerie.obtenerSerieTemporalDesdeGenerica(compra.getIdInstitucion().toString(), compra);
    	    if (salida==null) {
    	        throw new ClsExceptions("No se ha podido crear la serie de facturacion temporal");
    	    }

    	    // inserto el tipo de producto
    	    FacTiposProduIncluEnFactuAdm admTipoProd = new FacTiposProduIncluEnFactuAdm(this.usrbean);
    	    FacTiposProduIncluEnFactuBean beanTipoProd = new FacTiposProduIncluEnFactuBean();
    	    beanTipoProd.setIdInstitucion(compra.getIdInstitucion());
    	    beanTipoProd.setIdProducto(new Integer(compra.getIdProducto().toString()));
    	    beanTipoProd.setIdTipoProducto(compra.getIdTipoProducto());
    	    beanTipoProd.setIdSerieFacturacion(salida.getIdSerieFacturacion());
    	    if (!admTipoProd.insert(beanTipoProd)) {
    	        throw new ClsExceptions("Error al insertar producto incluido en serie: "+admTipoProd.getError());
    	    }
    	    
    	    // inserto el destinatario individual
    	    FacClienIncluidoEnSerieFacturAdm admCli = new FacClienIncluidoEnSerieFacturAdm(this.usrbean);
    	    FacClienIncluidoEnSerieFacturBean beanCli = new FacClienIncluidoEnSerieFacturBean();
    	    beanCli.setIdInstitucion(compra.getIdInstitucion());
    	    beanCli.setIdPersona(compra.getIdPersona());
    	    beanCli.setIdSerieFacturacion(salida.getIdSerieFacturacion());
    	    if (!admCli.insert(beanCli)) {
    	        throw new ClsExceptions("Error al insertar cliente incluido en serie: "+admCli.getError());
    	    }
    	    String fecha = GstDate.quitaHora(compra.getFecha());
    	    // creo la programación de la facturacion
    	    FacFacturacionProgramadaAdm admPro = new FacFacturacionProgramadaAdm(this.usrbean);
    	    FacFacturacionProgramadaBean beanPro = new FacFacturacionProgramadaBean();
    	    beanPro.setIdInstitucion(compra.getIdInstitucion());
    	    beanPro.setArchivarFact("0");
    	    beanPro.setConfDeudor(salida.getConfigDeudor());
    	    beanPro.setConfIngresos(salida.getConfigIngresos());
    	    beanPro.setCtaClientes(salida.getCuentaClientes());
    	    beanPro.setCtaIngresos(salida.getCuentaIngresos());
    	    beanPro.setFechaCargo("sysdate");
    	    beanPro.setFechaFinProductos(fecha);
    	    beanPro.setFechaFinServicios(fecha);
    	    beanPro.setFechaInicioProductos(fecha);
    	    beanPro.setFechaInicioServicios(fecha);
    	    beanPro.setFechaProgramacion("sysdate");
    	    beanPro.setFechaPrevistaGeneracion("sysdate");
    	    beanPro.setFechaRealGeneracion("sysdate");
    	    beanPro.setFechaPrevistaConfirmacion(null);
    	    beanPro.setFechaConfirmacion(null);
    	    beanPro.setGenerarPDF("1"); 
    	    beanPro.setEnvio(salida.getEnvioFactura());
    	    beanPro.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.CONFIRM_PENDIENTE);
    	    beanPro.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
    	    beanPro.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
    	    beanPro.setIdPrevision(null);
    	    beanPro.setIdSerieFacturacion(salida.getIdSerieFacturacion());
    	    beanPro.setLocked("1");
    	    beanPro.setIdProgramacion(admPro.getNuevoID(beanPro));
    	    
    	    // Ultimo campo a setear necesita idprogramacion, idinstitucion, idseriefacturacion
    	    beanPro.setDescripcion(admPro.getDescripcionPorDefecto(beanPro));
    	    
    	    if (!admPro.insert(beanPro)) {
    	        throw new ClsExceptions("Error al insertar cliente incluido en serie: "+admPro.getError());
    	    }
    	    
    	    // generamos la facturacion programada
			Object[] param_in = new Object[6];
			param_in[0] = beanPro.getIdInstitucion().toString();
        	param_in[1] = beanPro.getIdSerieFacturacion().toString();
        	param_in[2] = beanPro.getIdProgramacion().toString();
        	param_in[3] = this.usrbean.getLanguageInstitucion();		// idioma
        	param_in[4] = "";
        	param_in[5] = this.usrbean.getUserName();
        	String resultado[] = new String[2];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?)}", 2, param_in);
        	String codretorno = resultado[0];
        	if (!codretorno.equals("0")){
        		 throw new ClsExceptions ("Error al generar la Facturación rapida: "+resultado[1]);
        	}
        	beanPro.setLocked("0");
			if (!admPro.updateDirect(beanPro)) {
    	        throw new ClsExceptions("Error al actualizar locked de programacion: "+admPro.getError());
    	    }
    	    
    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al realizar generacion de facturacion rápida.");
    	}

    	return salida;
    }
    
    

    /**
     * Restaura los objetos implicados en la facturación temporal, dejando estos relacionados a la serie Genérica
     * y eliminando los objetos no necesarios, incluida la serie temporal. Devuelve la programación debidamente relacionada
     * @param serieTemporal
     * @return
     * @throws ClsExceptions
     */
    public FacFacturacionProgramadaBean restaurarSerieFacturacionGenerica(FacSerieFacturacionBean serieTemporal) throws ClsExceptions 
	{
        return restaurarSerieFacturacion(null,serieTemporal);
	}
    
    /**
     * Restaura los objetos implicados en la facturación temporal, dejando estos relacionados a la serie pasada como 
     * parámetro (serieCandidata) y eliminando los objetos no necesarios, incluida la serie temporal. Devuelve la programación debidamente relacionada
     * @param serieCandidata
     * @param serieTemporal
     * @return
     * @throws ClsExceptions
     */
    public FacFacturacionProgramadaBean restaurarSerieFacturacion(FacSerieFacturacionBean serieCandidata, FacSerieFacturacionBean serieTemporal) throws ClsExceptions 
	{
        FacFacturacionProgramadaBean salida = null;
        FacFacturacionProgramadaBean aux = null;
        try {
            FacSerieFacturacionAdm admSerie=new FacSerieFacturacionAdm(this.usrbean);
            if (serieCandidata==null) {
                // utilizamos la generica
                serieCandidata = admSerie.obtenerSerieGenerica(serieTemporal.getIdInstitucion().toString());
            }
            Hashtable ht = new Hashtable();
            ht.put(FacFacturaBean.C_IDINSTITUCION, serieTemporal.getIdInstitucion());
            ht.put(FacFacturaBean.C_IDSERIEFACTURACION, serieTemporal.getIdSerieFacturacion());

            // Obtengo la programacion para que apunte a la temporal
            FacFacturacionProgramadaAdm admPr=new FacFacturacionProgramadaAdm(this.usrbean);
            Vector v4 = admPr.select(ht);
            Long idSerieAnt = null; 
            Long idProgAnt = null;
            
            for (int i=0;v4!=null && i<v4.size();i++) {
                aux = (FacFacturacionProgramadaBean) v4.get(i);
                
                idProgAnt = aux.getIdProgramacion();
                idSerieAnt = aux.getIdSerieFacturacion();
                Long nuevoIdP= admPr.getNuevoID(aux.getIdInstitucion().toString(),serieCandidata.getIdSerieFacturacion().toString());
                
                salida = new FacFacturacionProgramadaBean();
                // campos comunes
                salida.setArchivarFact(aux.getArchivarFact());
                salida.setConfDeudor(aux.getConfDeudor());
                salida.setConfIngresos(aux.getConfIngresos());
                salida.setCtaClientes(aux.getCtaClientes());
                salida.setCtaIngresos(aux.getCtaIngresos());
                salida.setEnvio(aux.getEnvio());
                salida.setFechaCargo(aux.getFechaCargo());
                salida.setFechaConfirmacion(aux.getFechaConfirmacion());
                salida.setFechaFinProductos(aux.getFechaFinProductos());
                salida.setFechaFinServicios(aux.getFechaFinServicios());
                salida.setFechaInicioProductos(aux.getFechaInicioProductos());
                salida.setFechaInicioServicios(aux.getFechaInicioServicios());
                salida.setFechaPrevistaConfirmacion(aux.getFechaPrevistaConfirmacion());
                salida.setFechaPrevistaGeneracion(aux.getFechaPrevistaGeneracion());
                salida.setFechaProgramacion(aux.getFechaProgramacion());
                salida.setFechaRealGeneracion(aux.getFechaRealGeneracion());
                salida.setGenerarPDF(aux.getGenerarPDF());
                salida.setIdEstadoConfirmacion(aux.getIdEstadoConfirmacion());
                salida.setIdEstadoEnvio(aux.getIdEstadoEnvio());
                salida.setIdEstadoPDF(aux.getIdEstadoPDF());
                salida.setIdInstitucion(aux.getIdInstitucion());
                salida.setIdPrevision(aux.getIdPrevision());
                salida.setLocked(aux.getLocked());
                salida.setVisible(aux.getVisible());
                if (salida.getEnvio().equals(ClsConstants.ENVIO)) {
                	salida.setIdTipoPlantillaMail(aux.getIdTipoPlantillaMail());
                }

                // campos nuevos
                salida.setIdProgramacion(nuevoIdP);
                salida.setIdSerieFacturacion(serieCandidata.getIdSerieFacturacion());
                
                // Ultimo campo a setear necesita idprogramacion, idinstitucion, idseriefacturacion
                salida.setDescripcion(admPr.getDescripcionPorDefecto(salida));			

                // insertamos el nuevo
                if (!admPr.insert(salida)) {
                    throw new ClsExceptions("Error al insertar la nueva programacion: "+admPr.getError());
                }
            }
    	    
            // restauro programacion nueva en las facturas 
            FacFacturaAdm admFac=new FacFacturaAdm(this.usrbean);
            Vector v = admFac.select(ht);
            for (int i=0;v!=null && i<v.size();i++) {
                FacFacturaBean b = (FacFacturaBean) v.get(i);
                b.setIdSerieFacturacion(serieCandidata.getIdSerieFacturacion());
                b.setIdProgramacion(salida.getIdProgramacion());
                if (!admFac.updateDirect(b)) {
                    throw new ClsExceptions("Error al actualizar la serie en la factura: "+admFac.getError());
                }
            }
    	    
            // restauro programacion nueva en disquete cargos
            FacDisqueteCargosAdm admDis=new FacDisqueteCargosAdm(this.usrbean);
            Vector v2 = admDis.select(ht);
            for (int i=0;v2!=null && i<v2.size();i++) {
                FacDisqueteCargosBean b2 = (FacDisqueteCargosBean) v2.get(i);
                b2.setIdSerieFacturacion(serieCandidata.getIdSerieFacturacion());
                b2.setIdProgramacion(salida.getIdProgramacion());
                if (!admDis.updateDirect(b2)) {
                    throw new ClsExceptions("Error al actualizar la serie en la factura: "+admDis.getError());
                }
            }
    	    
            // Elimino la serie temporal y relaciones
            // cliente
            FacClienIncluidoEnSerieFacturAdm admClis=new FacClienIncluidoEnSerieFacturAdm(this.usrbean);
            Vector v3 = admClis.select(ht);
            for (int i=0;v3!=null && i<v3.size();i++) {
                FacClienIncluidoEnSerieFacturBean b3 = (FacClienIncluidoEnSerieFacturBean) v3.get(i);
                if (!admClis.delete(b3)) {
                    throw new ClsExceptions("Error al eliminar clientes de la serie temporal: "+admClis.getError());
                }
            }
            // bancos
            if (!admClis.deleteSQL("delete from fac_seriefacturacion_banco where idinstitucion="+serieTemporal.getIdInstitucion()+ " and idseriefacturacion="+serieTemporal.getIdSerieFacturacion())) {
                throw new ClsExceptions("Error al eliminar bancos de la serie temporal: "+admClis.getError());
            }
            // tipo producto
            if (!admClis.deleteSQL("delete from fac_tiposproduincluenfactu where idinstitucion="+serieTemporal.getIdInstitucion()+ " and idseriefacturacion="+serieTemporal.getIdSerieFacturacion())) {
                throw new ClsExceptions("Error al eliminar tipos de producto de la serie temporal: "+admClis.getError());
            }
            // la programacion inicial
            if (!admPr.deleteSQL("DELETE FROM FAC_FACTURACIONPROGRAMADA WHERE  idinstitucion="+serieTemporal.getIdInstitucion()+ " and idseriefacturacion="+serieTemporal.getIdSerieFacturacion()+ " and idprogramacion="+idProgAnt.toString())) {
                throw new ClsExceptions("Error al eliminar la serie temporal: "+admSerie.getError());
            }
            // la serie de facturacion
            if (!admSerie.delete(serieTemporal)) {
                throw new ClsExceptions("Error al eliminar la serie temporal: "+admSerie.getError());
            }

            // OKIS
            return salida;
    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al restaurar relaciones de serie de facturacion temporales.");
    	}
    }
 
    public void confirmarProgramacionFactura(FacFacturacionProgramadaBean beanP, HttpServletRequest req, boolean archivarFacturacion, SIGALogging log, boolean isTransacionPesada, boolean generarPagosBanco) throws ClsExceptions, SIGAException {
    	confirmarProgramacionFactura(beanP, req, archivarFacturacion, log, isTransacionPesada, generarPagosBanco, null);
    }
    
    public void confirmarProgramacionFactura(FacFacturacionProgramadaBean beanP, HttpServletRequest req, boolean archivarFacturacion, SIGALogging log, boolean isTransacionPesada, boolean generarPagosBanco, UserTransaction tx) throws ClsExceptions, SIGAException 
    {
    	boolean bTransaccionInterna = tx == null;
    	boolean isFacturadoOk = true;
    	String msjAviso = null; 
    	try {
    		if (bTransaccionInterna){
	    		if(isTransacionPesada)
	    			tx = (UserTransaction) this.usrbean.getTransactionPesada();
	    		else
	    			tx = this.usrbean.getTransaction();
    		}

    		// actualizacion de estado
    		// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//    		ReadProperties p = new ReadProperties ("SIGA.properties");
    		String pathFichero2 = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
    		String sBarra2 = "";
    		if (pathFichero2.indexOf("/") > -1) sBarra2 = "/"; 
    		if (pathFichero2.indexOf("\\") > -1) sBarra2 = "\\";        		
    		String nombreFichero = "";

    		ClsLogging.writeFileLog("CONFIRMAR FACTURACION RAPIDA: "+beanP.getIdInstitucion()+" " +beanP.getIdSerieFacturacion()+" " +beanP.getIdProgramacion(),10);

    		// fichero de log
    		nombreFichero = "LOG_CONFIRM_FAC_"+ beanP.getIdInstitucion()+"_"+beanP.getIdSerieFacturacion()+"_"+beanP.getIdProgramacion()+".log.xls"; 
    		if(log==null)
    			log = new SIGALogging(pathFichero2+sBarra2+beanP.getIdInstitucion()+sBarra2+nombreFichero);

    		//Facturacion factura = new Facturacion (beanP);
    		String idSerieFacturacion = beanP.getIdSerieFacturacion().toString();			
    		String idProgramacion 	= beanP.getIdProgramacion().toString();
    		String usuMod			= this.usrbean.getUserName();
    		String pathFichero 		= p.returnProperty("facturacion.directorioBancosOracle");
    		String sBarra = "";
    		if (pathFichero.indexOf("/") > -1) sBarra = "/"; 
    		if (pathFichero.indexOf("\\") > -1) sBarra = "\\";        		
    		pathFichero += sBarra+beanP.getIdInstitucion().toString();
    		//boolean noEncontrado = true;

    		// Se confirma la facturación
    		FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
    		Hashtable hashNew = new Hashtable();				
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, beanP.getIdInstitucion());
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion );
    		//UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM, "sysdate");
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA);
    		if (archivarFacturacion) {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_ARCHIVARFACT, "1");
    		} else {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_ARCHIVARFACT, "0");
    		}

    		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION,FacFacturacionProgramadaBean.C_IDPROGRAMACION,FacFacturacionProgramadaBean.C_IDSERIEFACTURACION};
    		String [] camposFactura = {FacFacturacionProgramadaBean.C_FECHACONFIRMACION,FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM,
    				FacFacturacionProgramadaBean.C_ARCHIVARFACT,FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION};
    		//String [] camposFecha = {FacFacturacionProgramadaBean.C_FECHACONFIRMACION,FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM,FacFacturacionProgramadaBean.C_ARCHIVARFACT};
    		//String [] camposEnvio = {FacFacturacionProgramadaBean.C_IDESTADOENVIO};
    		
    		//String [] camposConfirmacion = {FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION};

    		try {

    			//////////// TRANSACCION ////////////////
    			if (Status.STATUS_NO_TRANSACTION == tx.getStatus())
    				tx.begin();
    			//////////// TRANSACCION ////////////////

    			//Se genera numero de factura definitivo
    			Object[] param_in_confirmacion = new Object[4];
    			param_in_confirmacion[0] = beanP.getIdInstitucion().toString();
    			param_in_confirmacion[1] = idSerieFacturacion;
    			param_in_confirmacion[2] = idProgramacion;
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
    			// Se envían a banco para su cobro
    			Object[] param_in_banco = new Object[7];
    			param_in_banco[0] = beanP.getIdInstitucion().toString();
    			param_in_banco[1] = idSerieFacturacion;
    			param_in_banco[2] = idProgramacion;

    			String fechaCargo = "";
    			try {
    				if (fechaCargo == null || fechaCargo.equals("")){
    					// Sysdate
    					fechaCargo = UtilidadesBDAdm.getFechaBD("formato_ingles");
    				}
    				// Fecha de Cargo (DDMMAA):
    				fechaCargo = fechaCargo.substring(8,10) + fechaCargo.substring(5,7) + fechaCargo.substring(2,4); 
    			}
    			catch (Exception e) {
    				// Sysdate
    				fechaCargo = UtilidadesBDAdm.getFechaBD("formato_ingles");
    				// Fecha de Cargo (DDMMAA):
    				fechaCargo = fechaCargo.substring(8,10) + fechaCargo.substring(5,7) + fechaCargo.substring(2,4); 
    			}
    			////////////////////////////////////////////////

    			// RGG 05/05/2009 Cambio (solo se generan los pagos por banco cuando se indica por parámetro)
    			if (generarPagosBanco) {
	    			    
	    			param_in_banco[3] = fechaCargo;
	    			param_in_banco[4] = pathFichero;
	    			param_in_banco[5] = usuMod;
	    			param_in_banco[6] = this.usrbean.getLanguage();
	
	    			String resultado[] = new String[3];
	    			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
	    			codretorno = resultado[1];
	    			if (!codretorno.equals("0")){
	    				throw new ClsExceptions ("Error al generar disquetes bancarios: " + resultado[2]);
	    			}

    			}
    			
    			//AÑADIMOS C_IDESTADOCONFIRMACION(CONFIRM_FINALIZADA),C_FECHACONFIRMACION(current),C_FECHAPREVISTACONFIRM(current),C_ARCHIVARFACT(0 ó 1)};
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA);
    			facadm.updateDirect(hashNew, claves, camposFactura);

    			//////////// TRANSACCION ////////////////
    			tx.commit();
    			//////////// TRANSACCION ////////////////
    			isFacturadoOk = true;
    			ClsLogging.writeFileLog("CONFIRMAR Y PRESENTAR OK ",10);

    		} catch (Exception ee) {

    			//////////// TRANSACCION ////////////////
    			tx.rollback();
    			//////////// TRANSACCION ////////////////

    			if(ee instanceof SIGAException) {
    				SIGAException e2=(SIGAException)ee;
    				ClsLogging.writeFileLog("ERROR AL CONFIRMAR Y PRESENTAR: "+UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),e2.getLiteral()),10);
    				log.writeLogFactura("CONFIRMACION","N/A","N/A","Error en proceso de confirmación: "+UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),e2.getLiteral()));
    			} else {
    				ClsLogging.writeFileLog("ERROR AL CONFIRMAR Y PRESENTAR: "+ee.toString(),10);
    				log.writeLogFactura("CONFIRMACION","N/A","N/A","Error en proceso de confirmación: "+ee.toString());
    			}

    			//----- cambio de estado confirmacion a FINALIZADA CON ERRORES -------//
    			UserTransaction tx2 = this.usrbean.getTransaction();
    			tx2.begin();

    			// C_IDESTADOCONFIRMACION(CONFIRM_FINALIZADAERRORES),C_FECHACONFIRMACION(""),C_FECHAPREVISTACONFIRM(sysdate),C_ARCHIVARFACT(0 ó 1)};
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADAERRORES);
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM, "sysdate");
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "");
    			facadm.updateDirect(hashNew, claves, camposFactura);

    			tx2.commit();

    			ClsLogging.writeFileLog("CAMBIA ESTADO A FINALIZADA ERRORES.",10);
    			isFacturadoOk =false;
    			throw new ClsExceptions("Error al confirmar facturacion rápida. " + ee.toString());


    		}


    		ClsLogging.writeFileLog("ENTRA A GENERAR Y ENVIAR",10);
    		
    		boolean isGenerarPdf = beanP.getGenerarPDF() != null && beanP.getGenerarPDF().trim().equals("1");
    		boolean isGenerarEnvio = beanP.getEnvio() != null && beanP.getEnvio().trim().equals("1") && (beanP.getRealizarEnvio()==null || beanP.getRealizarEnvio().toString().equalsIgnoreCase("1"));
    		//aqui
    		if(isGenerarPdf){
    			msjAviso = generarPdfEnvioProgramacionFactura(tx,facadm,beanP,req,log,idSerieFacturacion,
    				idProgramacion, claves,  camposFactura,	 hashNew,   isGenerarEnvio);
    		}
			
    		

    	} 
    	catch (ClsExceptions e) {
    		try { 
    			if(tx != null)
    				tx.rollback(); 
    		} catch (Exception e3) {

    		}
    		if(!isFacturadoOk){
    			throw new ClsExceptions(e,"messages.facturacion.confirmacion.error");
    		}else{
    			throw e;
    		}

    	} 
    	catch (SIGAException e) {
    		try { 
    			if(tx!=null)
    				tx.rollback(); 
    		} catch (Exception e3) {

    		}
    		throw e;
    	} 
    	catch (Exception e) {
    		try { 
    			if(tx!=null)
    				tx.rollback(); 
    		} 
    		catch (Exception e3) {

    		}
    		throw new SIGAException("Error al confirmar facturacion rápida.");
    	}

    	if(msjAviso!=null)
    		throw new ClsExceptions(msjAviso);
    }
    
    private String generarPdfEnvioProgramacionFactura(UserTransaction tx,FacFacturacionProgramadaAdm facadm,FacFacturacionProgramadaBean beanP,
    		
    		HttpServletRequest req,SIGALogging log,String idSerieFacturacion,
    		String idProgramacion,String [] claves, String [] camposFactura, 
    		Hashtable hashFactura,boolean isGenerarEnvio)throws ClsExceptions, SIGAException,Exception{
    	String msjAviso = null;
    	// Try de PDFs y envios
		String [] camposPDF = {FacFacturacionProgramadaBean.C_IDESTADOPDF};
		String [] camposEnvioPdf = {FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacFacturacionProgramadaBean.C_IDESTADOPDF};
    	
		try {

			// Se guardan las facturas impresas.
			//if (isGenerarPdf) {

				ClsLogging.writeFileLog("TIENE QUE GENERAR PDF",10);

				//////////// TRANSACCION ////////////////
				tx.begin();
				//////////// TRANSACCION ////////////////

				//----- cambio de estado PDF a PROCESANDO -------//
				//C_IDESTADOPDF(PDF_PROCESANDO)
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_PROCESANDO);
				facadm.updateDirect(hashFactura, claves, camposPDF);

				//////////// TRANSACCION ////////////////
				tx.commit();
				//////////// TRANSACCION ////////////////

				//////////// ALMACENAR RAPIDA ////////////////
				int errorAlmacenar = this.generaryEnviarProgramacionFactura(req,this.usrbean,beanP.getIdInstitucion(), 
						Long.valueOf(idSerieFacturacion), Long.valueOf(idProgramacion),isGenerarEnvio,log,tx);
				switch (errorAlmacenar) {
				case 0:
					//NO HAY ERROR. SE HA GENERADO CORRECTAMENTE Y SE PROCESADO EL ENVIO
					//////////// TRANSACCION ////////////////
					tx.begin();
					//////////// TRANSACCION ////////////////

					if (isGenerarEnvio) {
						//----- cambio de estado ENVIO a FINALIZADO -------//
						//C_IDESTADOPDF(PDF_FINALIZADA),C_IDESTADOENVIO(ENVIO_FINALIZADA)
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA);
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADA);
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);

					}else{
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA);
						//C_IDESTADOPDF(PDF_FINALIZADA)
						facadm.updateDirect(hashFactura, claves, camposPDF);
					}



					//////////// TRANSACCION ////////////////
					tx.commit();
					//////////// TRANSACCION ////////////////

					ClsLogging.writeFileLog("OK TODO. CAMBIO DE ESTADOS",10);
					break;
				case 1:
					ClsLogging.writeFileLog("ERROR AL ALMACENAR FACTURA. RETORNO="+errorAlmacenar,3);
					//////////// TRANSACCION ////////////////
					tx.begin();
					//////////// TRANSACCION ////////////////
					// ERROR EN GENERAR PDF

					msjAviso = "messages.facturacion.confirmacion.errorPdf";
					if (isGenerarEnvio) {
						//C_IDESTADOPDF(PDF_FINALIZADAERRORES),C_IDESTADOENVIO(ENVIO_FINALIZADAERRORES)
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES);
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES);
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
					}else{
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES);
						//C_IDESTADOPDF(PDF_FINALIZADAERRORES)
						facadm.updateDirect(hashFactura, claves, camposPDF);

					}
					//////////// TRANSACCION ////////////////
					tx.commit();
					//////////// TRANSACCION ////////////////
					break;
				case 2:
					ClsLogging.writeFileLog("ERROR AL ALMACENAR FACTURA. RETORNO="+errorAlmacenar,3);
					// ERROR EN ENVIO FACTURA
					//////////// TRANSACCION ////////////////
					tx.begin();
					//////////// TRANSACCION ////////////////

					msjAviso = "messages.facturacion.confirmacion.errorEnvio";
					if (isGenerarEnvio) {
						//C_IDESTADOPDF(PDF_FINALIZADA),C_IDESTADOENVIO(ENVIO_FINALIZADAERRORES)
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA);
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES);
						facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
					}else{
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA);
						//C_IDESTADOPDF(PDF_FINALIZADA)
						facadm.updateDirect(hashFactura, claves, camposPDF);

					}


					//////////// TRANSACCION ////////////////
					tx.commit();
					//////////// TRANSACCION ////////////////

					ClsLogging.writeFileLog("ERROR ENVIAR FACTURA. CAMBIO DE ESTADOS",10);
					break;
				default:
					//////////// TRANSACCION ////////////////
					tx.begin();
				//////////// TRANSACCION ////////////////
				msjAviso = "messages.facturacion.confirmacion.errorPdf";
				if (isGenerarEnvio) {
					//C_IDESTADOPDF(PDF_FINALIZADAERRORES),C_IDESTADOENVIO(ENVIO_FINALIZADAERRORES)
					UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES);
					UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES);
					facadm.updateDirect(hashFactura, claves, camposEnvioPdf);
				}else{
					UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES);
					//C_IDESTADOPDF(PDF_FINALIZADAERRORES)
					facadm.updateDirect(hashFactura, claves, camposPDF);

				}



				//////////// TRANSACCION ////////////////
				tx.commit();
				//////////// TRANSACCION ////////////////

				ClsLogging.writeFileLog("ERROR GENERAL GENERAR/ENVIAR FACTURA. CAMBIO DE ESTADOS",10);
				break;
				}


			//}

		} catch (Exception ee) {

			ClsLogging.writeFileLog("ERROR GENERAL EN TRY GENERAR/ENVIAR.",10);

			//////////// TRANSACCION ////////////////
			try {
				if(tx != null)
					tx.rollback();
			} catch (Exception ex) {

			}
			//////////// TRANSACCION ////////////////

			// ESCRIBO EN EL LOG mensaje general ??
			log.writeLogFactura("PDF/ENVIO","N/A","N/A","Error general en el proceso de generación o envío de facturas PDF: "+ee.toString());

			//////////// TRANSACCION ////////////////
			tx.begin();
			//////////// TRANSACCION ////////////////

			if (isGenerarEnvio) {
				//C_IDESTADOPDF(PDF_FINALIZADAERRORES),C_IDESTADOENVIO(ENVIO_FINALIZADAERRORES)
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES);
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES);
				facadm.updateDirect(hashFactura, claves, camposEnvioPdf);

			}else{
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES);
				//C_IDESTADOPDF(PDF_FINALIZADAERRORES)
				facadm.updateDirect(hashFactura, claves, camposPDF);
			}



			//////////// TRANSACCION ////////////////
			tx.commit();
			//////////// TRANSACCION ////////////////

			throw ee;
		}
		return msjAviso;
    	
    }

	/**
	 * Almacenar: genera y guarda las facturas relacionadas con una detreminada serie de facturacion ya confirmada
	 * <br> Se utiliza en las confirmaciones rápidas. 
	 * @param institucion - identificador de la institucion
	 * @param serieFacturacion - identificador de la serie de facturacion
	 * @param idProgramacion - identificador de progamacion de la serie
	 * @param usuario - identificador del usuario
	 * @param bGenerarEnvios: indica si se enviaran de forma automatica las facturas
	 * @param log Clase de log para mensajes
	 * @return int - Devuelve: - 0 si todo está correcto.
	 * 						   - 1 si ha existido un error en el procesado de la factura.
	 * 						   - 2 si ha existido un error en el procesado del envío. 
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public int generaryEnviarProgramacionFactura (HttpServletRequest request, UsrBean userbean, Integer institucion, Long serieFacturacion, 
			              Long idProgramacion, boolean bGenerarEnvios, SIGALogging log,UserTransaction tx)  throws ClsExceptions,SIGAException {

		
		Vector facturas=new Vector();
		Vector plantillas=new Vector();
		String plantilla="";
		File ficFOP=null;
		int salida = 0;
		boolean existeAlgunErrorPdf = false;
		boolean existeAlgunErrorEnvio = false;
		try {
			
		    FacFacturaAdm admF = new FacFacturaAdm(userbean);
		    // Obtengo las facturas a almacenar
			facturas=admF.getSerieFacturacionConfirmada(institucion.toString(),serieFacturacion.toString(),idProgramacion.toString());
			
			ClsLogging.writeFileLog("ALMACENAR >> "+institucion.toString()+" "+serieFacturacion.toString()+" "+idProgramacion.toString(),10);
			
			// Obtengo la plantilla a utilizar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(userbean);
			CenClienteAdm cliAdm = new CenClienteAdm(userbean);
			plantillas=plantillaAdm.getPlantillaSerieFacturacion(institucion.toString(),serieFacturacion.toString());
			plantilla=plantillas.firstElement().toString();
						
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");
			
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
    		rutaPlantilla += barraPlantilla+institucion.toString()+barraPlantilla+plantilla;
			File rutaModelo=new File(rutaPlantilla);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaModelo.exists()){

				// ESCRIBO EN EL LOG
				throw new SIGAException("messages.facturacion.almacenar.rutaPlantillaFacturaMal");					
			}
    		
			
			
			ClsLogging.writeFileLog("ALMACENAR >> TERMINA DE OBTENER PLANTILLAS Y DATOS GENERALES",10);

			// recorro todas las facturas para ir creando lo sinformes pertinentes
    		Enumeration listaFacturas = facturas.elements();
    		    		
			ClsLogging.writeFileLog("ALMACENAR >> NUMERO DE FACTURAS: "+facturas.size(),10);
    		
			String idFactura="";
			
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no COMPROBAMOS QUE HAYA SIDO CORRECTO EL CAMBIO ANTERIOR
			//while (correcto && listaFacturas.hasMoreElements()){
    		while (listaFacturas.hasMoreElements()){
    			boolean correcto=true;
    			try {

	    			if (tx!=null) {
	    				tx.begin();
	    			}
	    			
	    			Hashtable facturaHash=(Hashtable)listaFacturas.nextElement();
	    			idFactura=(String)facturaHash.get(FacFacturaBean.C_IDFACTURA);
	    			String idPersona=(String)facturaHash.get(FacFacturaBean.C_IDPERSONA);
	    			// Obtenemos el lenguaje del cliente 
	    			String lenguaje = cliAdm.getLenguaje(institucion.toString(),idPersona); 
	    			String numFactura=(String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA);
		   			CenColegiadoAdm admCol = new CenColegiadoAdm(userbean);
		  			Hashtable htCol = admCol.obtenerDatosColegiado(this.usrbean.getLocation(),idPersona,this.usrbean.getLanguage());
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
	    				    throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");				
	    				} else {
	    					correcto = true;
	    				}
						
		    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA GENERADA EN PDF",10);
	    				
		    			
    				} catch (SIGAException ee) {
    					correcto = false;
    					ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.getLiteral(userbean.getLanguage()),10);
	    				
	    				// ESCRIBO EN EL LOG
						log.writeLogFactura("PDF",idPersona,numFactura,"Error en el proceso de generación de facturas PDF: "+ee.getLiteral(userbean.getLanguage()));
	    	    		salida=1;
	    	    		//Aunque nos ha fallado esta factura es posible que la siguiente, no.
	    	    		//POR LO TANTO no cazamos la excepcion
	    	    		//throw ee;
	    	    		existeAlgunErrorPdf = true;

	    			} catch (Exception ee) {
	    				correcto = false;
	    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.toString(),10);
	    				
	    				// ESCRIBO EN EL LOG
						log.writeLogFactura("PDF",idPersona,numFactura,"Error en el proceso de generación de facturas PDF: "+ee.toString());
	    	    		salida=1;
	    	    		//Aunque nos ha fallado esta factura es posible que la siguiente, no.
	    	    		//POR LO TANTO no cazamos la excepcion
	    	    		//throw ee;
	    	    		existeAlgunErrorPdf = true;
	    			}
	    			
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> VAMOS A VER SI ENVIARMOS: ENVIAR:"+bGenerarEnvios+" CORRECTO:"+correcto,10);

		    	    	
	    			// Envio de facturas
	    			if (bGenerarEnvios && correcto){
	    				
	    				try {
	    					
		    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE ENVIO",10);

		    				//Obtenemos el bean del envio: 
		    				EnvEnviosAdm envAdm = new EnvEnviosAdm(userbean);
		    				CenPersonaAdm admPersona = new CenPersonaAdm(this.usrbean);
		    				String descripcion = "Envio facturas - " + admPersona.obtenerNombreApellidos((String)facturaHash.get(FacFacturaBean.C_IDPERSONA));
		    				Envio envio = new Envio(userbean,descripcion);
		
		    				// Bean envio
		    				EnvEnviosBean enviosBean = envio.enviosBean;
		    				
		    				// RGG
		    				GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
		    				String preferencia = paramAdm.getValor(institucion.toString(),"ENV","TIPO_ENVIO_PREFERENTE","1");
		    				Integer valorPreferencia = Envio.calculaTipoEnvio(preferencia);
		    	            enviosBean.setIdTipoEnvios(valorPreferencia);
		
		    				// Preferencia del tipo de envio si el usuario tiene uno:
		    				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
		    				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),preferencia);
		    				
		    				if (direccion==null || direccion.size()==0) {
		    					 direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),"3");// si no hay direccion preferente mail, buscamos la de correo
		    					 if (direccion==null || direccion.size()==0) {
		    					 	direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),"2");// si no hay direccion de despacho, buscamos la de despacho
		    					 	if (direccion==null || direccion.size()==0) {
		    					 		direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),"");// si no hay direccion de despacho, buscamos cualquier dirección.
		    					 		if (direccion==null || direccion.size()==0) {
		    					 			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> NO TIENE DIRECCION PREFERENTE "+preferencia,10);
		    		    					throw new ClsExceptions("No se ha encontrado dirección de la persona para el tipo de envio preferente: "+preferencia); 			
		    					 		}
		    					 	}
		    					 }
		    					
		    				}
		    				
		    				//SE SELECCIONA LA PLANTILLA MAIL
		    				FacFacturacionProgramadaAdm facProgAdm = new FacFacturacionProgramadaAdm(userbean);
		    				Hashtable hashProg = new Hashtable();
		    				hashProg.put(FacFacturacionProgramadaBean.C_IDINSTITUCION, institucion);
		    				hashProg.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, serieFacturacion);
		    				hashProg.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
		    				Vector vFacProg = facProgAdm.select(hashProg);
		    				if(vFacProg != null && vFacProg.size()>0){
		    					FacFacturacionProgramadaBean facProgBean = (FacFacturacionProgramadaBean) vFacProg.get(0);
		    					if(facProgBean.getIdTipoPlantillaMail() != null){
		    						int plantillaMail = facProgBean.getIdTipoPlantillaMail();
		    						enviosBean.setIdPlantillaEnvios(plantillaMail);
		    						
			        				// Creacion documentos
			         				Documento documento = new Documento(rutaAlmacen+barraAlmacen+nColegiado+"-"+UtilidadesString.validarNombreFichero((String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA))+".pdf","Factura "+nColegiado+"-"+UtilidadesString.validarNombreFichero((String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA))+".pdf");
			        				if(UtilidadesHash.getString(facturaHash,FacFacturaBean.C_NUMEROFACTURA)==null ||UtilidadesHash.getString(facturaHash,FacFacturaBean.C_NUMEROFACTURA).equals("")){
			        					documento = new Documento(rutaAlmacen+barraAlmacen+nColegiado+"-"+(String)facturaHash.get(FacFacturaBean.C_IDFACTURA)+".pdf","Factura "+nColegiado+"-"+(String)facturaHash.get(FacFacturaBean.C_IDFACTURA)+".pdf");	
			        				}
			        				Vector documentos = new Vector(1);
			        				documentos.add(documento);
			        				
			        				// Genera el envio:
		        					envio.generarEnvio((String)facturaHash.get(FacFacturaBean.C_IDPERSONA), EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,documentos);
		    	    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ENVIO GENERADO OK",10);
	
			    					}else{
			    						throw new SIGAException("messages.facturacion.almacenar.plantillasEnvioMal");		
			    					}
			    					
		    				}else{
		    					throw new SIGAException("messages.facturacion.almacenar.plantillasEnvioMal");		
		    				}
	
	    				} catch (SIGAException eee) {
	    		    		
		    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+eee.getLiteral(userbean.getLanguage()),10);
	    		    		// ESCRIBO EN EL LOG
	    					log.writeLogFactura("ENVIO",idPersona,numFactura,"Error en el proceso de envío de facturas: "+eee.getLiteral(userbean.getLanguage()));
	    					salida=2;
	    					//Aunque nos ha fallado esta factura es posible que la siguiente, no.
		    	    		//POR LO TANTO no cazamos la excepcion
	    					//throw eee;
	    					existeAlgunErrorEnvio = true;
	        	    		
	    				} catch (Exception eee) {
	    		    		
		    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+eee.toString(),10);
	    		    		// ESCRIBO EN EL LOG
	    					log.writeLogFactura("ENVIO",idPersona,numFactura,"Error en el proceso de envío de facturas: "+eee.toString());
	    					salida=2;
	    					//Aunque nos ha fallado esta factura es posible que la siguiente, no.
		    	    		//POR LO TANTO no cazamos la excepcion
	    					//throw eee;
	    					existeAlgunErrorEnvio = true;
	        	    		
	    				}
	    			}
	    			if (tx!=null) {
	    				tx.commit();
	    			}

    			} catch (Exception tot) {
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> CATCH GENERAL",10);
    				if (tx!=null) {
    					try {
    						tx.rollback();
    					} catch (Exception ee) {}
    				}
    				//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    	    		//POR LO TANTO no cazamos la excepcion
    				//throw tot;
    			}
    			
				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE FACTURA OK ",10);

    		} // bucle
    		 
    		// inc6666 - Si es correcto generamos el ZIP con todas las facturas
			generarZip(institucion.toString(), serieFacturacion.toString(), idProgramacion.toString());
    				
		}catch (SIGAException e) {
			
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.getLiteral(userbean.getLanguage()),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			log.writeLogFactura("PDF/ENVIO","N/A","N/A","Error general en el proceso de generación/envío de facturas PDF: "+e.getLiteral(userbean.getLanguage()));

			
			if (ficFOP!=null && ficFOP.exists()){
				ficFOP.delete();
			}
			existeAlgunErrorEnvio = true;
			existeAlgunErrorPdf = true;
			//throw e;
		}catch (Exception e) {
			
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.toString(),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			log.writeLogFactura("PDF/ENVIO","N/A","N/A","Error general en el proceso de generación/envío de facturas PDF: "+e.toString());

			
			if (ficFOP!=null && ficFOP.exists()){
				ficFOP.delete();
			}
			
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

    public FacSerieFacturacionBean procesarFacturacionRapidaCompras(PysPeticionCompraSuscripcionBean beanPeticion, FacSerieFacturacionBean beanSerieCandidata) throws ClsExceptions 
	{
        PysCompraAdm admCompra = new PysCompraAdm(this.usrbean);
        return procesarFacturacionRapidaCompras(beanPeticion, admCompra.obtenerComprasPorPeticion(beanPeticion), beanSerieCandidata);
	}
    
    public FacSerieFacturacionBean procesarFacturacionRapidaCompras(PysPeticionCompraSuscripcionBean beanPeticion, Vector compras, FacSerieFacturacionBean beanSerieCandidata) throws ClsExceptions 
	{
        FacSerieFacturacionBean salida = null;
    	try {
    	    FacSerieFacturacionAdm admSerie = new FacSerieFacturacionAdm(this.usrbean);
    	    PysCompraAdm admCompra = new PysCompraAdm(this.usrbean); 
    	    
    	    // ACTUALIZA EL IMPORTE ANTICIPADO PARA QUE QUEDEN PAGADAS
    	    // RGG 05/05/2009 Cambio para que queden solo anticipadas con el dinero que estaba anticipado (ya no se pagan a la totalidad)
//		    for (int h=0;h<compras.size();h++) {
//		        PysCompraBean beanCompra = (PysCompraBean)compras.get(h);
//			    double importe = (beanCompra.getCantidad().intValue() * beanCompra.getImporteUnitario().doubleValue()) * (1+(beanCompra.getIva().doubleValue()/100));
//			    beanCompra.setImporteAnticipado(new Double(importe));
//			    if (!admCompra.updateDirect(beanCompra)) {
//			        throw new ClsExceptions("Error al actualizar el importe anticipado: "+admCompra.getError());
//			    }
//		    }
		    

    	    
    	    // alta de la serie de facturacion temporal
    	    salida = admSerie.obtenerSerieTemporalDesdeOtra(beanSerieCandidata, beanPeticion);
    	    if (salida==null) {
    	        throw new ClsExceptions("No se ha podido crear la serie de facturacion temporal");
    	    }

    	    FacTiposProduIncluEnFactuAdm admTipoProd = new FacTiposProduIncluEnFactuAdm(this.usrbean);
            int tipoProductoAux=0;
            int idProductoAux = -1;
            
            Date fechaMin=null;
            Date fechaMax=null;
                
    	    // inserto los tipos de producto
    	    for (int t=0;t<compras.size();t++) {
    	        PysCompraBean compra = (PysCompraBean) compras.get(t);
    	        
    	        Date fechaAux = GstDate.convertirFecha(compra.getFecha());
    	        if (fechaMin==null) fechaMin=fechaAux;
    	        if (fechaMax==null) fechaMax=fechaAux;
    	        if (fechaAux.before(fechaMin)) fechaMin=fechaAux;
    	        if (fechaAux.after(fechaMax)) fechaMax=fechaAux;
        	        
    	        FacTiposProduIncluEnFactuBean beanTipoProd = new FacTiposProduIncluEnFactuBean();
	    	    beanTipoProd.setIdInstitucion(compra.getIdInstitucion());
	    	    beanTipoProd.setIdProducto(new Integer(compra.getIdProducto().toString()));
	    	    beanTipoProd.setIdTipoProducto(compra.getIdTipoProducto());
	    	    beanTipoProd.setIdSerieFacturacion(salida.getIdSerieFacturacion());
	    	   //hacemos previamente la comprobacion de si los tipos de productos son el mismo
	    	    
	    	    //Si el idtipoproducto es diferente, se inserta
	    	    if (tipoProductoAux!=compra.getIdTipoProducto().intValue()){
	    	    	if (!admTipoProd.insert(beanTipoProd)) {
	    	    		throw new ClsExceptions("Error al insertar producto incluido en serie: "+admTipoProd.getError());
	    	    	}
	    	    } else{
	    	    	//Si el idtipoproducto es el mismo, se inserta solo si el idprudcuto es diferente
	    	    	if(idProductoAux!=compra.getIdProducto()){
	    	    		if (!admTipoProd.insert(beanTipoProd)) {
	    	    			throw new ClsExceptions("Error al insertar producto incluido en serie: "+admTipoProd.getError());
	    	    		}
	    	    	}
	    	    }
	    	    tipoProductoAux=compra.getIdTipoProducto().intValue();
	    	    idProductoAux = compra.getIdProducto().intValue();
    	    }
    	    
    	    // inserto el destinatario individual
    	    FacClienIncluidoEnSerieFacturAdm admCli = new FacClienIncluidoEnSerieFacturAdm(this.usrbean);
    	    FacClienIncluidoEnSerieFacturBean beanCli = new FacClienIncluidoEnSerieFacturBean();
    	    beanCli.setIdInstitucion(beanPeticion.getIdInstitucion());
    	    beanCli.setIdPersona(beanPeticion.getIdPersona());
    	    beanCli.setIdSerieFacturacion(salida.getIdSerieFacturacion());
    	    if (!admCli.insert(beanCli)) {
    	        throw new ClsExceptions("Error al insertar cliente incluido en serie: "+admCli.getError());
    	    }
    	    
    	    // RGG 06/05/2009 Cambio para crear un rango de fechas de la programación que incluya las fechas de compra y no la de la petición.
    	    String fechaini =GstDate.convertirFechaHora(fechaMin);
    	    String fechafin =GstDate.convertirFechaHora(fechaMax);
    	    
    	    //String fecha = GstDate.quitaHora(beanPeticion.getFecha());

    	    // creo la programación de la facturacion
    	    FacFacturacionProgramadaAdm admPro = new FacFacturacionProgramadaAdm(this.usrbean);
    	    FacFacturacionProgramadaBean beanPro = new FacFacturacionProgramadaBean();
    	    beanPro.setIdInstitucion(beanPeticion.getIdInstitucion());
    	    beanPro.setArchivarFact("0");
    	    beanPro.setConfDeudor(salida.getConfigDeudor());
    	    beanPro.setConfIngresos(salida.getConfigIngresos());
    	    beanPro.setCtaClientes(salida.getCuentaClientes());
    	    beanPro.setCtaIngresos(salida.getCuentaIngresos());
    	    beanPro.setFechaCargo("sysdate");
    	    beanPro.setFechaInicioProductos(fechaini);
    	    beanPro.setFechaFinProductos(fechafin);
    	    beanPro.setFechaInicioServicios(fechaini);
    	    beanPro.setFechaFinServicios(fechafin);
    	    beanPro.setFechaProgramacion("sysdate");
    	    beanPro.setFechaPrevistaGeneracion("sysdate");
    	    beanPro.setFechaRealGeneracion("sysdate");
    	    beanPro.setFechaPrevistaConfirmacion(null);
    	    beanPro.setFechaConfirmacion(null);
    	    beanPro.setGenerarPDF("1"); 
    	    beanPro.setEnvio(beanSerieCandidata.getEnvioFactura());
    	    beanPro.setIdEstadoConfirmacion(FacEstadoConfirmFactBean.CONFIRM_PENDIENTE);
    	    beanPro.setIdEstadoEnvio(FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
    	    beanPro.setIdEstadoPDF(FacEstadoConfirmFactBean.PDF_NOAPLICA);
    	    beanPro.setIdPrevision(null);
    	    beanPro.setIdSerieFacturacion(salida.getIdSerieFacturacion());
    	    beanPro.setLocked("1");
    	    beanPro.setIdProgramacion(admPro.getNuevoID(beanPro));
    	    beanPro.setIdTipoPlantillaMail(salida.getIdTipoPlantillaMail());

    	    // Ultimo campo a setear necesita idprogramacion, idinstitucion, idseriefacturacion
    	    beanPro.setDescripcion(admPro.getDescripcionPorDefecto(beanPro));
    	    
    	    if (!admPro.insert(beanPro)) {
    	        throw new ClsExceptions("Error al insertar cliente incluido en serie: "+admPro.getError());
    	    }
    	    
    	    
    	    
    	    // generamos la facturacion programada
			Object[] param_in = new Object[6];
			param_in[0] = beanPro.getIdInstitucion().toString();
        	param_in[1] = beanPro.getIdSerieFacturacion().toString();
        	param_in[2] = beanPro.getIdProgramacion().toString();
        	param_in[3] = this.usrbean.getLanguageInstitucion();		// idioma
        	param_in[4] = beanPeticion.getIdPeticion().toString();
        	param_in[5] = this.usrbean.getUserName();
        	String resultado[] = new String[2];
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?)}", 2, param_in);
        	String codretorno = resultado[0];
        	if (!codretorno.equals("0")){
        		throw new ClsExceptions ("Error al generar la Facturación rapida: "+resultado[1]);
        	}
        	beanPro.setLocked("0");
			if (!admPro.updateDirect(beanPro)) {
    	        throw new ClsExceptions("Error al actualizar locked de programacion: "+admPro.getError());
    	    }
    	    
    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al realizar generacion de facturacion rápida.");
    	}

    	return salida;
    }
    /**
     * Devuelve nul
     * @param idInstitucion
     * @param idPersona
     * @param idCuenta
     * @return null si no encuentra cuenta Bancaria
     * @throws SIGAException
     * @throws ClsExceptions
     */
    public CenCuentasBancariasBean getCuentaRenegociacionManual(Integer idInstitucion,Long idPersona,Integer idCuenta)throws SIGAException, ClsExceptions{
    	
    	CenCuentasBancariasBean cuentaBancaria = null;
		CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(this.usrbean);
		Hashtable cuentaBancariaHashtable = cuentasBancariasAdm.selectCuentas(idPersona, idInstitucion, idCuenta);
		
		String fechaBajaCuenta = (String) cuentaBancariaHashtable.get(CenCuentasBancariasBean.C_FECHABAJA);
		if(fechaBajaCuenta!=null && !fechaBajaCuenta.equals("") && GstDate.compararFechas(fechaBajaCuenta, GstDate.getHoyJava()) != 1 )
			return null;
		else
			cuentaBancaria = (CenCuentasBancariasBean) cuentasBancariasAdm.hashTableToBean(cuentaBancariaHashtable);
    	
		return cuentaBancaria;
		
    }
    /**
     * 
     * @param idInstitucion
     * if solo productos
		 if cuenta no baja
			renegociamos
		 else 
			if solo una cuenta cargo 
				renegociamos
			else
				NO renegociamos
	 else
		if misma cuanta todos servicios y no de baja
			renegociamos
		else
			NO renegociamos
     * @param idFactura
     * @return null  si no encuentra cuenta Bancaria
     * @throws SIGAException
     * @throws ClsExceptions
     */
    public CenCuentasBancariasBean getCuentaRenegociacionAutomatica(Integer idInstitucion,String idFactura,Long idPersona,Integer idCuenta)throws SIGAException, ClsExceptions{
    	
			
    	
    	
    	CenCuentasBancariasBean cuentaBancaria = null;
    	Hashtable claves = new Hashtable();
		UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
		FacFacturaAdm facturaAdm = new FacFacturaAdm (this.usrbean);
//		Vector vFacturas = facturaAdm.selectByPK(claves);
//		FacFacturaBean facturaBean = (FacFacturaBean) vFacturas.get(0);
		FacFacturacionSuscripcionAdm facFacturacionSuscripcionAdm = new FacFacturacionSuscripcionAdm(this.usrbean);
		int numServicios = facFacturacionSuscripcionAdm.getNumeroServiciosFactura(idInstitucion,idFactura);
		CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(this.usrbean);
		if(numServicios == 0){
			
			Hashtable cuentaBancariaHashtable = cuentasBancariasAdm.selectCuentas(idPersona, idInstitucion, idCuenta);
			if (cuentaBancariaHashtable != null) {
			String fechaBajaCuenta = (String) cuentaBancariaHashtable.get(CenCuentasBancariasBean.C_FECHABAJA);
			if(fechaBajaCuenta!=null && !fechaBajaCuenta.equals("") ){
				List listaCuentasCargo = cuentasBancariasAdm.getCuentasCargo(idPersona, idInstitucion);
				if(listaCuentasCargo!=null &&listaCuentasCargo.size()==1){
					cuentaBancaria = (CenCuentasBancariasBean) listaCuentasCargo.get(0);
				}else{
					//no renegociamos
					return null;
				}
			}else{
				//la cuenta es OK
				cuentaBancaria = (CenCuentasBancariasBean) cuentasBancariasAdm.hashTableToBean(cuentaBancariaHashtable);
				return cuentaBancaria;
				
			}
		}
		}else{
			//Este nmetodo devuelve si no es lamisma cuenta para todos los servicios
			cuentaBancaria = cuentasBancariasAdm.getCuentaUnicaServiciosFactura(idInstitucion, idFactura);
		}
		
		return cuentaBancaria;
    	
		
		
    }
    
    public void insertarRenegociar(Integer idInstitucion,String idFactura,Integer estadoFactura, String formaPago,String idCuenta,Double importePtePagar,String observaciones, String fechaRenegociar, boolean isTransaccionCreada,boolean isAutomatica,Hashtable htCuenta) throws SIGAException, Exception{
    	UserTransaction t = this.usrbean.getTransaction();

    	try {
    		int idFormaPago			= 0;
    		int nuevoEstado         = 0;
    		boolean actualizarFacturaEnDisco = false;
    		boolean cambioFormaPago = false;
    		// Recuperamos la factura asociada a la renegociacion
    		Hashtable claves = new Hashtable();
    		UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
    		UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
    		FacFacturaAdm facturaAdm = new FacFacturaAdm (this.usrbean);
    		Vector vFacturas = facturaAdm.selectByPK(claves);
    		FacFacturaBean facturaBean = (FacFacturaBean) vFacturas.get(0);
    		CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(this.usrbean);
    		String resultado = ClsConstants.ERROR_RENEGOCIAR_EXITO;
    		
    		do {
    			// Si la factura se encuentra Pendiente Cobro por Caja
    			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) { // 2
    				if (formaPago.equalsIgnoreCase("porCaja")) {
    					throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_PORCAJA);
    				}
    				if (formaPago.equalsIgnoreCase("mismaCuenta")) {
    					idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
    					if (facturaBean.getIdCuenta() != null) {
    						CenCuentasBancariasBean cuentaBancaria;
    						if(isAutomatica){
    							cuentaBancaria = getCuentaRenegociacionAutomatica(idInstitucion,idFactura,facturaBean.getIdPersona(),facturaBean.getIdCuenta());
    						} else {
    							cuentaBancaria = getCuentaRenegociacionManual(idInstitucion,facturaBean.getIdPersona(), facturaBean.getIdCuenta());
    						}
	    					 
	    					if(cuentaBancaria!=null && cuentaBancaria.getIdCuenta()!=null) {
	    						idCuenta = cuentaBancaria.getIdCuenta().toString();
	    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
	    					} else {
	    						throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTABAJA);
	    					}
    					} else {
    						throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_FORMAPAGO); 
    					}
    					break;
    				}
    				if ((formaPago.equalsIgnoreCase("porBanco")) || (formaPago.equalsIgnoreCase("porOtroBanco"))) {
    					if ((idCuenta == null) || (idCuenta.equals(""))){
    						//throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTANOEXISTE);
    						CenCuentasBancariasBean cuentaBancaria;
    						String idCuentaActiva = null;
    						if(isAutomatica){
    							idCuenta = (String) facturaAdm.getCuentaPersona(idInstitucion, facturaBean.getIdPersona()).toString();
    							if ((idCuenta != null) && (!idCuenta.equals("0"))&& (!idCuenta.equals(""))){
    								cuentaBancaria = getCuentaRenegociacionAutomatica(idInstitucion,idFactura,facturaBean.getIdPersona(),Integer.parseInt(idCuenta));
    	    						idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
    	    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
    							}
    						} 
    					}
    					else{
    						idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
    					}
    					break;
    				}    				
    			} else {
    				// El estado de la factura es Pendiente Cobro por Banco
	    			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO)) {
	    				if (formaPago.equalsIgnoreCase("mismaCuenta")) {
    						CenCuentasBancariasBean cuentaBancaria;
    						if(isAutomatica){
    							cuentaBancaria = getCuentaRenegociacionAutomatica(idInstitucion,idFactura,facturaBean.getIdPersona(),facturaBean.getIdCuenta());
    						} else {
    							cuentaBancaria = getCuentaRenegociacionManual(idInstitucion,facturaBean.getIdPersona(), facturaBean.getIdCuenta());
    						}
	    					if(cuentaBancaria!=null && cuentaBancaria.getIdCuenta()!=null) {
	    						idCuenta = cuentaBancaria.getIdCuenta().toString();
	    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
	    					} else {
	    						if (isAutomatica) {
	    							idCuenta = null;
	    	    					idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
	    	    					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA);
	    	    					cambioFormaPago = true;
	    						} else {
	    							throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTABAJA);
	    						}
	    					}
	    					break;
	    				}
	    				if (formaPago.equalsIgnoreCase("porCaja")) {
	    					idCuenta = null;
	    					idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
	    					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA);
	    					break;
	    				}
	    				if ((formaPago.equalsIgnoreCase("porBanco")) || (formaPago.equalsIgnoreCase("porOtroBanco"))) {
	    					if ((idCuenta == null) || (idCuenta == "")){
	    						CenCuentasBancariasBean cuentaBancaria;
	    						String idCuentaActiva = null;
	    						if(isAutomatica){
	    							idCuenta = (String) facturaAdm.getCuentaPersona(idInstitucion, facturaBean.getIdPersona()).toString();
	    							if ((idCuenta != null) && (!idCuenta.equals("0"))&& (!idCuenta.equals(""))){
	    								cuentaBancaria = getCuentaRenegociacionAutomatica(idInstitucion,idFactura,facturaBean.getIdPersona(),Integer.parseInt(idCuenta));
	    	    						idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
	    	    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
	    							} else{
		    							idCuenta = null;
		    	    					idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
		    	    					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA);
		    	    					cambioFormaPago = true;
	    							}
	    						} else throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTANOEXISTE);
	    					} else{
	    						idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
	    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
	    					}
	    					break;
	    				}
	    			} else {
	    				//El estado de la factura es devuelta
		    			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA)) {
		    				actualizarFacturaEnDisco = true;
		    				
		    				if (formaPago.equalsIgnoreCase("mismaCuenta")) {
		    					
		    					CenCuentasBancariasBean cuentaBancaria = null;
		    					if(isAutomatica){
		        					cuentaBancaria = getCuentaRenegociacionAutomatica(idInstitucion,idFactura,facturaBean.getIdPersona(),facturaBean.getIdCuenta());
		        					if(cuentaBancaria!=null && cuentaBancaria.getIdCuenta()!=null){
		        						idCuenta = cuentaBancaria.getIdCuenta().toString();
		        						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
			    						idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
		        					}else{
		        						throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTABAJA);
		        					}
		        				}else{
		        					
		        					idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
		        					cuentaBancaria = getCuentaRenegociacionManual(idInstitucion,facturaBean.getIdPersona(), facturaBean.getIdCuenta());
		        					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
		        					if(cuentaBancaria!=null && cuentaBancaria.getIdCuenta()!=null)
		        						idCuenta = cuentaBancaria.getIdCuenta().toString();
		        					else{
		        						throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTABAJA);
		        					}	
		        				}
		    					break;
		    				}
		    				if (formaPago.equalsIgnoreCase("porCaja")) {
		    					idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
		    					idCuenta = null;
		    					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA);
		    					break;
		    				}
		    				if (formaPago.equalsIgnoreCase("porBanco") || (formaPago.equalsIgnoreCase("porOtroBanco"))) {
		    					idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
		    					if(idCuenta == null){
		    						CenCuentasBancariasBean cuentaBancaria;
		    						String idCuentaActiva = null;
		    						if(isAutomatica){
		    							idCuenta = (String) facturaAdm.getCuentaPersona(idInstitucion, facturaBean.getIdPersona()).toString();
		    							if ((idCuenta != null) && (!idCuenta.equals("0"))&& (!idCuenta.equals(""))){
		    								cuentaBancaria = getCuentaRenegociacionAutomatica(idInstitucion,idFactura,facturaBean.getIdPersona(),Integer.parseInt(idCuenta));
		    	    						idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
		    	    						nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
		    							} else{
			    							idCuenta = null;
			    	    					idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
			    	    					nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA);
			    	    					cambioFormaPago = true;
		    							}
		    						} else throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTANOEXISTE);
		    					}
		    					else nuevoEstado = Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO);
		    					break;
		    				}
		    			}
	    			}
    			}			

    		} while (false);

    		if ((idCuenta==null || idCuenta.equals("") || idCuenta.equals("0")) && (idFormaPago!=ClsConstants.TIPO_FORMAPAGO_METALICO))
    			throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_CUENTANOEXISTE);
    			
    		if (htCuenta!=null && idCuenta!=null)
    			htCuenta.put("idCuenta", idCuenta);
    		
    		// Insertamos un nuevo registro en Fac_Renegociacion
    		FacRenegociacionAdm renegociacionAdm = new FacRenegociacionAdm (this.usrbean);
    		FacRenegociacionBean renegociacionBean = new FacRenegociacionBean ();
    		renegociacionBean.setComentario(observaciones);
    		if ((fechaRenegociar == "") || (fechaRenegociar == null)) {
    			renegociacionBean.setFechaRenegociacion("sysdate");
    		} else {
    			renegociacionBean.setFechaRenegociacion(fechaRenegociar);
    		}
    		if (isAutomatica) {
    			renegociacionBean.setComentario(ClsConstants.TIPO_RENEGOCIA_MASIVA);
    		} else {
    			renegociacionBean.setComentario(ClsConstants.TIPO_RENEGOCIA_MANUAL);
    		}
    		if(idCuenta!=null)
    			renegociacionBean.setIdCuenta(new Integer(idCuenta));
    		
    		renegociacionBean.setIdFactura(idFactura);
    		renegociacionBean.setIdInstitucion(idInstitucion);
    		renegociacionBean.setIdPersona(facturaBean.getIdPersona());
    		renegociacionBean.setIdRenegociacion(renegociacionAdm.getNuevoID(idInstitucion, idFactura));
    		renegociacionBean.setImporte(importePtePagar);
    		if(!isTransaccionCreada)
    			t.begin();
    		if(!renegociacionAdm.insert(renegociacionBean)) {
    			throw new ClsExceptions("Error al insertar la renegociación: "+renegociacionAdm.getError()); 
    		}

    		// Actualizamos la tabla FacFacturaInclidaEnDisquete
    		if (actualizarFacturaEnDisco ) {
    			FacFacturaIncluidaEnDisqueteAdm facturaDiscoAdm = new FacFacturaIncluidaEnDisqueteAdm (this.usrbean); 
    			if (!facturaDiscoAdm.updateRenegociacion(renegociacionBean.getIdInstitucion(), renegociacionBean.getIdFactura(), renegociacionBean.getIdRenegociacion())) {
    				throw new ClsExceptions("Error al actualizar la factura incluida en disquete: "+facturaDiscoAdm.getError()); 
    			}
    		}

    		// Aquí hay que actualizar el estado de la factura según haya quedado finalmente la factura
    		// Actualizamos el idCuenta de la tabla FacFactura
    		if(idCuenta!=null)
    			facturaBean.setIdCuenta(new Integer(idCuenta));
    		else
    			facturaBean.setIdCuenta(null);

    		// AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
    		// Esto no paga nada por lo que no cambia nada
//  		facturaBean.setImpTotalPagadoPorBanco(new Double(facturaBean.getImpTotalPagadoPorCaja().doubleValue()+miForm.getDatosPagosRenegociarImportePendiente().doubleValue()));
//  		facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()+miForm.getDatosPagosRenegociarImportePendiente().doubleValue()));
//  		facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()-miForm.getDatosPagosRenegociarImportePendiente().doubleValue()));
    		// vamos a poner a pelo el estado renegociada.

    		facturaBean.setEstado(new Integer(nuevoEstado)); // renegociada
    		if (idFormaPago!=0) {
    			facturaBean.setIdFormaPago(new Integer(idFormaPago)); 
    		}

    		if (facturaAdm.update(facturaBean)) {
    			// AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
    			//facturaAdm.actualizarEstadoFactura(facturaBean, this.getUserName(request));
    			// Atencion, por un problema de la capa basica, los valores nulos no se actualizan.
    			// actualizo ahora la cuenta.
    			if (idCuenta==null) {
    				if (!facturaAdm.insertSQL("UPDATE FAC_FACTURA SET IDCUENTA=NULL WHERE IDINSTITUCION="+idInstitucion+" AND IDFACTURA="+idFactura)) {
    					throw new ClsExceptions("Error al actualizar la cuenta a nulo: "+facturaAdm.getError());        
    				}
    			}

    		} else {
    			throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
    		}
    		
    		if(!isTransaccionCreada) {
    			t.commit();
    		}
    		
			if (cambioFormaPago){
				throw new SIGAException(ClsConstants.ERROR_RENEGOCIAR_NORENEGOCIADAS);
			}
    	}

    	catch (Exception e) {
    		if(!isTransaccionCreada)
    			t.rollback();
    		throw e;
    	}	    	
    }
    
    /**
     * Aplica comisiones correspondientes a un determinado disco de devoluciones
     * @param institucion
     * @param idDisqDevoluciones
     * @param aplicaComisionesCliente
     * @param userBean
     * @return
     * @throws SIGAException
     * @throws ClsExceptions
     */
    
    public boolean aplicarComisiones(String institucion, String idDisqDevoluciones, String aplicaComisionesCliente, UsrBean userBean) throws SIGAException, ClsExceptions {
    	boolean resultado = false;
    	
		// Identificamos los disquetes devueltos asociados al fichero de devoluciones
		FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(userBean);
		Vector vDevoluciones = admLDDB.obtenerDevolucionesFichero(institucion, idDisqDevoluciones);
		
		// Aplicamos la comision a cada devolusion
		for (int d=0; d<vDevoluciones.size(); d++) {
			FacLineaDevoluDisqBancoBean lineaDevolucion = (FacLineaDevoluDisqBancoBean)vDevoluciones.get(d);
			resultado = aplicarComisionAFactura (institucion, lineaDevolucion, aplicaComisionesCliente, null, userBean, resultado);
			if(!resultado)
				break;			
		}
		
		return resultado;
	}
	
	public boolean aplicarComisionAFactura (String institucion, FacLineaDevoluDisqBancoBean lineaDevolucion, String aplicaComisionesCliente, String idCuenta, UsrBean userBean, boolean resultado) throws SIGAException, ClsExceptions {
		Hashtable criteriosDevolucion = new Hashtable();
		FacLineaDevoluDisqBancoAdm admLDDB= new FacLineaDevoluDisqBancoAdm(userBean);
		criteriosDevolucion.put(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,institucion);
		criteriosDevolucion.put(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,lineaDevolucion.getIdDisqueteDevoluciones());
		
		Vector productos = new Vector();
		Hashtable productoComision = new Hashtable();
		
		// Identificamos los productos con comisiones asociadas
		PysProductosInstitucionAdm admPI= new PysProductosInstitucionAdm(userBean);			
		productos=admPI.getProductosComisiones(institucion);
		
		// RGG por si no tiene ningun producto con conmision
		productoComision=((Row)productos.firstElement()).getRow();		

		// Obtenemos el cliente al que aplicar la comision
		FacFacturaIncluidaEnDisqueteAdm admFIED= new FacFacturaIncluidaEnDisqueteAdm(userBean);
		FacFacturaIncluidaEnDisqueteBean facturaDisquete = new FacFacturaIncluidaEnDisqueteBean();
		Vector clientes = new Vector();
		Hashtable criteriosFactura = new Hashtable();
		if(lineaDevolucion.getIdInstitucion()!= null)
			criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,lineaDevolucion.getIdInstitucion().toString());
		if(lineaDevolucion.getIdDisqueteCargos()!= null)
			criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,lineaDevolucion.getIdDisqueteCargos().toString());
		if(lineaDevolucion.getIdFacturaIncluidaEnDisquete()!= null)
			criteriosFactura.put(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,lineaDevolucion.getIdFacturaIncluidaEnDisquete().toString());
		clientes=admFIED.selectByPKForUpdate(criteriosFactura);
		facturaDisquete=(FacFacturaIncluidaEnDisqueteBean)clientes.firstElement();
		
		// Averiguamos si se le aplica la comision a dicho cliente
		Vector comisionesClientes = new Vector();
		CenClienteAdm admCliente= new CenClienteAdm(userBean);
		CenClienteBean clienteBean = new CenClienteBean();
		Hashtable criteriosCliente = new Hashtable();
		if(facturaDisquete.getIdInstitucion()!= null)
			criteriosCliente.put(CenClienteBean.C_IDINSTITUCION,facturaDisquete.getIdInstitucion().toString());
		if(facturaDisquete.getIdPersona()!= null)
			criteriosCliente.put(CenClienteBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
		comisionesClientes=admCliente.selectForUpdate(criteriosCliente);
		clienteBean=(CenClienteBean)comisionesClientes.firstElement();
		
		if (aplicaComisionesCliente!=null && 
			aplicaComisionesCliente.equalsIgnoreCase(ClsConstants.DB_TRUE) &&
			clienteBean.getComisiones().equalsIgnoreCase(ClsConstants.DB_TRUE)){

			// RGG 15/09/2006 COMISIONES A CARGO DEL CLIENTE
			
			// Deteccion banco CGAE
			Vector bancos = new Vector();
			FacDisqueteDevolucionesAdm admDD= new FacDisqueteDevolucionesAdm(userBean);
			FacDisqueteDevolucionesBean disqueteDevolucion = new FacDisqueteDevolucionesBean();
			bancos=admDD.selectByPKForUpdate(criteriosDevolucion);
			disqueteDevolucion=(FacDisqueteDevolucionesBean)bancos.firstElement();
			// Comision banco a aplicar (ajena o propia)
			Vector comisiones = new Vector();
			
			Hashtable criteriosBanco = new Hashtable();
			FacBancoInstitucionAdm admBI= new FacBancoInstitucionAdm(userBean);
			FacBancoInstitucionBean comision = new FacBancoInstitucionBean();
			if(disqueteDevolucion.getIdInstitucion()!= null)
				criteriosBanco.put(FacBancoInstitucionBean.C_IDINSTITUCION,disqueteDevolucion.getIdInstitucion().toString());
			criteriosBanco.put(FacBancoInstitucionBean.C_BANCOS_CODIGO,disqueteDevolucion.getBancosCodigo());
			comisiones=admBI.selectByPKForUpdate(criteriosBanco);
			comision=(FacBancoInstitucionBean)comisiones.firstElement();
			// Deteccion banco del cliente
			CenCuentasBancariasAdm admCB= new CenCuentasBancariasAdm(userBean);
			CenCuentasBancariasBean cuentaCliente = new CenCuentasBancariasBean();
			Vector bancosCliente = new Vector();
			Hashtable criteriosCC = new Hashtable();
			if(facturaDisquete.getIdInstitucion()!= null)
				criteriosCC.put(CenCuentasBancariasBean.C_IDINSTITUCION,facturaDisquete.getIdInstitucion().toString());
			if(facturaDisquete.getIdPersona()!= null)
				criteriosCC.put(CenCuentasBancariasBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
			if(facturaDisquete.getIdCuenta()!= null)
				criteriosCC.put(CenCuentasBancariasBean.C_IDCUENTA,facturaDisquete.getIdCuenta().toString());						
			bancosCliente=admCB.selectByPKForUpdate(criteriosCC);
			cuentaCliente=(CenCuentasBancariasBean)bancosCliente.firstElement();
			
			// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
			Hashtable original = new Hashtable();
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
			lineaDevolucion.setCargarCliente("S");
			if (disqueteDevolucion.getBancosCodigo().equalsIgnoreCase(cuentaCliente.getCbo_Codigo())){
				lineaDevolucion.setGastosDevolucion(comision.getImpComisionPropiaCargo());
			}
			else{
				lineaDevolucion.setGastosDevolucion(comision.getImpComisionAjenaCargo());
			}
			
			resultado=admLDDB.update(lineaDevolucion);

			if (resultado){
				String formaPago="";
				// Inserciones relacionadas previa obtencion forma de pago
				PysFormaPagoProductoAdm admFPP= new PysFormaPagoProductoAdm(userBean);

				//aalg: INC_09853_SIGA. Hay que buscar la forma de pago que tenga la comisión bancaria
			
				Vector formasPagoProductoComision = admFPP.getFormasPagoProducto((String)productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION),(String)productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO), (String)productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO), (String)productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
				if (formasPagoProductoComision == null || formasPagoProductoComision.isEmpty()){
					// No existe forma de pago por banco para el producto comision.
					formaPago=String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA);
					idCuenta=facturaDisquete.getIdCuenta().toString();
				} else {
					// SI existe forma de pago por banco para el producto comision.
					formaPago=((Row)formasPagoProductoComision.firstElement()).getRow().get(PysFormaPagoProductoBean.C_IDFORMAPAGO).toString();
					if (formaPago.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA)))
						idCuenta=facturaDisquete.getIdCuenta().toString();
					else
						idCuenta = "";

				}
				
				
				// Insercion PYS_COMPRASUSCRIPCION						
				PysPeticionCompraSuscripcionAdm admPCS= new PysPeticionCompraSuscripcionAdm(userBean);
				String idPeticion="";
				idPeticion=admPCS.getNuevoID(new Integer((String)productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION))).toString();
				Hashtable compraSuscripcion=new Hashtable();
				compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION));
				compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDPETICION,idPeticion);
				compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_TIPOPETICION,"A");
				compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
				compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_FECHA,"sysdate");
				compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDESTADOPETICION,"20");
		//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_IDPETICIONALTA,null);
		//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_NUM_AUT,null);
		//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_NUM_OPERACION,null);
		//		compraSuscripcion.put(PysPeticionCompraSuscripcionBean.C_REFERENCIA,null);
				resultado=admPCS.insert(compraSuscripcion);
				
				// Insercion PYS_PRODUCTOSSOLICITADOS
				if (resultado){
					PysProductosSolicitadosAdm admPS= new PysProductosSolicitadosAdm(userBean);
					Hashtable productosSolicitados=new Hashtable();
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION));
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDPETICION,idPeticion);
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO));
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
					productosSolicitados.put(PysProductosSolicitadosBean.C_IDFORMAPAGO,formaPago);
					productosSolicitados.put(PysProductosSolicitadosBean.C_CANTIDAD,"1");
					productosSolicitados.put(PysProductosSolicitadosBean.C_ACEPTADO,"A");
					productosSolicitados.put(PysProductosSolicitadosBean.C_VALOR,lineaDevolucion.getGastosDevolucion());
					productosSolicitados.put(PysProductosSolicitadosBean.C_PORCENTAJEIVA,productoComision.get(PysProductosInstitucionBean.C_PORCENTAJEIVA));
					
					productosSolicitados.put(PysProductosSolicitadosBean.C_NOFACTURABLE,productoComision.get(PysProductosInstitucionBean.C_NOFACTURABLE));
					
					if(idCuenta != null)
						productosSolicitados.put(PysProductosSolicitadosBean.C_IDCUENTA,idCuenta);
		//			productosSolicitados.put(PysProductosSolicitadosBean.C_IDTIPOENVIOS,null);
		//			productosSolicitados.put(PysProductosSolicitadosBean.C_IDDIRECCION,null);
					resultado=admPS.insert(productosSolicitados);
					
					// Insercion PYS_PRODUCTOSSOLICITADOS
					if (resultado){
						PysCompraAdm admCompra= new PysCompraAdm(userBean);
						Hashtable compra=new Hashtable();
						compra.put(PysCompraBean.C_IDINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDINSTITUCION));
						compra.put(PysCompraBean.C_IDPETICION,idPeticion);
						compra.put(PysCompraBean.C_IDTIPOPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
						compra.put(PysCompraBean.C_IDPRODUCTO,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTO));
						compra.put(PysCompraBean.C_IDPRODUCTOINSTITUCION,productoComision.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
						compra.put(PysCompraBean.C_FECHA,"sysdate");
						compra.put(PysCompraBean.C_CANTIDAD,"1");
						compra.put(PysCompraBean.C_IMPORTEUNITARIO,lineaDevolucion.getGastosDevolucion());
						compra.put(PysCompraBean.C_PORCENTAJEIVA,productoComision.get(PysProductosInstitucionBean.C_PORCENTAJEIVA));
						compra.put(PysCompraBean.C_IDFORMAPAGO,formaPago);
						compra.put(PysCompraBean.C_DESCRIPCION,"Comisión bancaria por devolucion factura el día "+disqueteDevolucion.getFechaGeneracion());
						compra.put(PysCompraBean.C_IMPORTEANTICIPADO,"0");
						compra.put(PysCompraBean.C_ACEPTADO,"A");
						
						compra.put(PysCompraBean.C_NOFACTURABLE,productoComision.get(PysProductosInstitucionBean.C_NOFACTURABLE));
						
		//				compra.put(PysCompraBean.C_NUMEROLINEA,null);									
		//				compra.put(PysCompraBean.C_IDFACTURA,null);
		//				compra.put(PysCompraBean.C_FECHABAJA,null);
						compra.put(PysCompraBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
						if(idCuenta != null)
							compra.put(PysCompraBean.C_IDCUENTA,idCuenta);
						resultado=admCompra.insert(compra);
					}	
				}
			}
		} else {
			
			// RGG 15/09/2006 COMISIONES A CARGO DE LA INSTITUCION
			
			// Deteccion banco CGAE
			Vector bancos = new Vector();
			FacDisqueteDevolucionesAdm admDD= new FacDisqueteDevolucionesAdm(userBean);
			FacDisqueteDevolucionesBean disqueteDevolucion = new FacDisqueteDevolucionesBean();
			bancos=admDD.selectByPKForUpdate(criteriosDevolucion);
			disqueteDevolucion=(FacDisqueteDevolucionesBean)bancos.firstElement();
			// Comision banco a aplicar (ajena o propia)
			Vector comisiones = new Vector();
			Hashtable criteriosBanco = new Hashtable();
			FacBancoInstitucionAdm admBI= new FacBancoInstitucionAdm(userBean);
			FacBancoInstitucionBean comision = new FacBancoInstitucionBean();
			if(disqueteDevolucion.getIdInstitucion()!= null)
				criteriosBanco.put(FacBancoInstitucionBean.C_IDINSTITUCION,disqueteDevolucion.getIdInstitucion().toString());
			criteriosBanco.put(FacBancoInstitucionBean.C_BANCOS_CODIGO,disqueteDevolucion.getBancosCodigo());
			comisiones=admBI.selectByPKForUpdate(criteriosBanco);
			comision=(FacBancoInstitucionBean)comisiones.firstElement();
			// Deteccion banco del cliente
			CenCuentasBancariasAdm admCB= new CenCuentasBancariasAdm(userBean);
			CenCuentasBancariasBean cuentaCliente = new CenCuentasBancariasBean();
			Vector bancosCliente = new Vector();
			Hashtable criteriosCC = new Hashtable();
			if(facturaDisquete.getIdInstitucion()!= null)
				criteriosCC.put(CenCuentasBancariasBean.C_IDINSTITUCION,facturaDisquete.getIdInstitucion().toString());
			if(facturaDisquete.getIdPersona()!= null)
				criteriosCC.put(CenCuentasBancariasBean.C_IDPERSONA,facturaDisquete.getIdPersona().toString());
			if(facturaDisquete.getIdCuenta()!= null)
				criteriosCC.put(CenCuentasBancariasBean.C_IDCUENTA,facturaDisquete.getIdCuenta().toString());						
			bancosCliente=admCB.selectByPKForUpdate(criteriosCC);
			cuentaCliente=(CenCuentasBancariasBean)bancosCliente.firstElement();
			
			// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
			Hashtable original = new Hashtable();
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
			// RGG 15/09/2006 DIFERENCIA CON LO DE ARRIBA
			lineaDevolucion.setCargarCliente("N");
			if (disqueteDevolucion.getBancosCodigo().equalsIgnoreCase(cuentaCliente.getCbo_Codigo())){
				lineaDevolucion.setGastosDevolucion(comision.getImpComisionPropiaCargo());
			}
			else{
				lineaDevolucion.setGastosDevolucion(comision.getImpComisionAjenaCargo());
			}
			resultado=admLDDB.update(lineaDevolucion);
			
			// RGG 15/09/2006 NO DUPLICO LO SIGUIENTE PORQUE NO ES NECESARIO
			
		} // ELSE
		
		return resultado;
	}
	
}