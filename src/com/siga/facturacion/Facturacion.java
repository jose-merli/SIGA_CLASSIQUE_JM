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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.GEN_PROPERTIES;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.FacFactura;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.services.helper.SigaServiceHelperService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.FileHelper;
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
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenInstitucionBean;
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
import com.siga.beans.FacFicherosDescargaBean;
import com.siga.beans.FacHistoricoFacturaAdm;
import com.siga.beans.FacHistoricoFacturaBean;
import com.siga.beans.FacLineaDevoluDisqBancoAdm;
import com.siga.beans.FacLineaDevoluDisqBancoBean;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.FacLineaFacturaBean;
import com.siga.beans.FacPlantillaFacturacionAdm;
import com.siga.beans.FacRenegociacionAdm;
import com.siga.beans.FacRenegociacionBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysCompraAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysTipoIvaAdm;
import com.siga.beans.PysTipoIvaBean;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.facturacion.action.FicheroBancarioPagosAction;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;
import com.siga.informes.InformePersonalizable;

import es.satec.businessManager.BusinessManager;

public class Facturacion {
    private UsrBean usrbean=null;
    private String consulta=null;

	public void setConsulta(String consulta) {
		this.consulta=consulta;
	}
	
	public String getConsulta() {
		return this.consulta;
	}
	
    public Facturacion(UsrBean usr) {        
        this.usrbean = usr;
    }    	
	
    private Vector<CenInstitucionBean>  getInstitucionesConFacturasPendientes(UsrBean userBean, String tiempoMaximoEjecucionBloqueada,int estado ) {	    		
		Vector<CenInstitucionBean> cenInstitucionBeans = null;
		try {				
			Hashtable<Integer,Object> codigos = new Hashtable<Integer,Object>();
			
			
			String sWhere = null;
			String[] orden = new String[1]; 
			
			
			switch (estado) {
				//FacEstadoConfirmFactBean.GENERADA
				case 2:
					sWhere=" WHERE " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NULL " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION + " <= SYSDATE " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDINSTITUCION+" = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND (" + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.GENERACION_PROGRAMADA +
								   " OR (" + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.EJECUTANDO_GENERACION +
								           " AND SYSDATE - " +tiempoMaximoEjecucionBloqueada+ " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" )) ";
			
					orden[0] = FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION;
				break;
				//FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA
				case 1:
					sWhere=" WHERE "+
							//+ FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
							"  " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_VISIBLE + " = 'S' " +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " IN (" + FacEstadoConfirmFactBean.GENERADA + ", " + FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA + ") " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
   							" AND NOT EXISTS (SELECT 1 " + 
						         " FROM " + FacFacturacionProgramadaBean.T_NOMBRETABLA + " PREVIA " + 
						         " WHERE PREVIA." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION  +
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA +
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_VISIBLE + " = 'S' " + 
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " <> " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + ") ";
					orden[0] = FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM;
					
				break;
				
				//FacEstadoConfirmFactBean.PDF_PROGRAMADA
				case 6:
					sWhere=" WHERE " +
   							"  " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDINSTITUCION+" = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " + // Solo las que estan generadas
   							" AND " + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NOT NULL " + // Para fechas previstas de confirmacion adecuadas 
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
							" AND (" + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROGRAMADA +
								   " OR (" + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROCESANDO +
								           " AND SYSDATE - " +tiempoMaximoEjecucionBloqueada+ " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" )) ";			
			
					orden[0] = FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION;
					
				break;
				//FacEstadoConfirmFactBean.TRASPASO_PROGRAMADA

				case 23:
					sWhere = " WHERE " +
							" " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
						" AND " + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NOT NULL " + // Solo las que estan confirmadas 
						" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
						
						" AND " + FacFacturacionProgramadaBean.C_IDESTADOTRASPASO + " = " + FacEstadoConfirmFactBean.TRASPASO_PROCESANDO +
						
						" AND (not exists (select 1 from " + FacFacturaBean.T_NOMBRETABLA +
								" where " + FacFacturaBean.T_NOMBRETABLA + ".idinstitucion = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".idinstitucion " +
										"and " + FacFacturaBean.T_NOMBRETABLA + ".idseriefacturacion = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".idseriefacturacion " +
										"and " + FacFacturaBean.T_NOMBRETABLA + ".idprogramacion = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".idprogramacion " +
										"and " + FacFacturaBean.T_NOMBRETABLA + ".traspasada is null)" + 
						
						" OR SYSDATE - " + tiempoMaximoEjecucionBloqueada + " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" ) ";
		
					orden[0] = FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION;
					//FacEstadoConfirmFactBean.ENVIO_PROGRAMADA
				case 12:
					
					sWhere=" WHERE " +
   							" " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDINSTITUCION+" = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+" =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM + " IS NOT NULL " + // Para fechas previstas de confirmacion adecuadas 
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " + // Solo las que estan generadas 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_FINALIZADA + // Para los estados de confirmacion adecuados
							
							" AND (" + FacFacturacionProgramadaBean.C_IDESTADOENVIO + " = " + FacEstadoConfirmFactBean.ENVIO_PROGRAMADA +
								   " OR (" + FacFacturacionProgramadaBean.C_IDESTADOENVIO + " = " + FacEstadoConfirmFactBean.ENVIO_PROCESANDO +
								           " AND SYSDATE - " +tiempoMaximoEjecucionBloqueada+ " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" )) ";			
			
					
					
					orden[0] = FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION;
					
				break;
				

			default:
				break;
			}
		    cenInstitucionBeans = getInstitucionesConFacturas(userBean, sWhere, orden);
		    
		} catch (Exception e) { 
			ClsLogging.writeFileLogError("### Error general al obtener instituciones con Facturas)", e, 3);
		}
		return cenInstitucionBeans;
	}
    
    public Vector<CenInstitucionBean>  getInstitucionesConFacturasPendientes(UsrBean userBean, boolean incluirPdfs) {	    		
    	ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		// Obtencion de la propiedad que contiene el tiempo de espera que se les da a las facturaciones en ejcucion no generadas por alguna anomal�a
    	String maxMinutosEnEjecucion = rp.returnProperty("facturacion.programacionAutomatica.maxMinutosEnEjecucion");
    	
	    Long tiempoEsperaBloqueosProperty = Long.valueOf(maxMinutosEnEjecucion!=null?maxMinutosEnEjecucion:"2");
		String tiempoMaximoEjecucionBloqueada = String.valueOf(tiempoEsperaBloqueosProperty/(24.0*60.0));
    	
    	Vector<CenInstitucionBean> institucionesFacturacionPte = new Vector<CenInstitucionBean>();
    	
    	ClsLogging.writeFileLog("### Obteniendo instituciones con facturaciones pendientes de generar" ,7);
    	Vector<CenInstitucionBean> institucionesGeneracionPte = getInstitucionesConFacturasPendientes(userBean,tiempoMaximoEjecucionBloqueada, FacEstadoConfirmFactBean.GENERADA);
    	if(institucionesGeneracionPte!=null && institucionesGeneracionPte.size()>0) {
    		institucionesFacturacionPte.addAll(institucionesGeneracionPte);
    		ClsLogging.writeFileLog("####### "+institucionesGeneracionPte.size()+" instituciones con facturas pendientes de generar" ,7);
    	}
    	ClsLogging.writeFileLog("### Obteniendo instituciones con facturaciones programadas pendientes" ,7);
    	Vector<CenInstitucionBean> institucionesProgramacionPte = getInstitucionesConFacturasPendientes(userBean,tiempoMaximoEjecucionBloqueada, FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA);
    	
    	if(institucionesProgramacionPte!=null && institucionesProgramacionPte.size()>0) {
    		institucionesFacturacionPte.addAll(institucionesProgramacionPte);
    		ClsLogging.writeFileLog("####### "+institucionesProgramacionPte.size()+" instituciones con facturas programadas pendientes" ,7);
    	}
    	if(incluirPdfs) {
    		ClsLogging.writeFileLog("### Obteniendo instituciones con facturas pendientes de crear" ,7);
	    	Vector<CenInstitucionBean> institucionesPdfFacturaGeneracionPte = getInstitucionesConFacturasPendientes(userBean,tiempoMaximoEjecucionBloqueada, FacEstadoConfirmFactBean.PDF_PROGRAMADA);
	    	if(institucionesPdfFacturaGeneracionPte!=null && institucionesPdfFacturaGeneracionPte.size()>0) {
	    		institucionesFacturacionPte.addAll(institucionesPdfFacturaGeneracionPte);
	    		ClsLogging.writeFileLog("####### "+institucionesPdfFacturaGeneracionPte.size()+" instituciones con facturas pendientes de crear" ,7);
	    	}
	    	ClsLogging.writeFileLog("### Obteniendo instituciones con facturas pendientes de enviar" ,7);
	    	Vector<CenInstitucionBean> institucionesEnvioFacturaPte = getInstitucionesConFacturasPendientes(userBean,tiempoMaximoEjecucionBloqueada, FacEstadoConfirmFactBean.ENVIO_PROGRAMADA);
	    	if(institucionesEnvioFacturaPte!=null && institucionesEnvioFacturaPte.size()>0) {
	    		institucionesFacturacionPte.addAll(institucionesEnvioFacturaPte);
	    		ClsLogging.writeFileLog("####### "+institucionesEnvioFacturaPte.size()+" instituciones con facturas pendientes de enviar" ,7);
	    	}
	    	ClsLogging.writeFileLog("### Obteniendo instituciones con facturas pendientes de validar el traspaso" ,7);
	    	Vector<CenInstitucionBean> institucionesTraspasoFacturaPte = getInstitucionesConFacturasPendientes(userBean,tiempoMaximoEjecucionBloqueada, FacEstadoConfirmFactBean.TRASPASO_PROGRAMADA);
	    	if(institucionesTraspasoFacturaPte!=null && institucionesTraspasoFacturaPte.size()>0) {
	    		institucionesFacturacionPte.addAll(institucionesTraspasoFacturaPte);
	    		ClsLogging.writeFileLog("####### "+institucionesTraspasoFacturaPte.size()+" instituciones con facturas pendientes de validar el traspaso" ,7);
	    	}
	    	
    	}
		
		return institucionesFacturacionPte;
	}
    
    
    private Vector<CenInstitucionBean>  getInstitucionesConFacturas(UsrBean userBean,String sWhere, String[]orden) {	    		
		boolean alMenosUnafacturacionProgramadaEncontrada = false;
		Vector<CenInstitucionBean> cenInstitucionBeans = null;
		try {				
			Hashtable<Integer,Object> codigos = new Hashtable<Integer,Object>();
			FacFacturacionProgramadaAdm admFacturacionProgramada = new FacFacturacionProgramadaAdm(userBean);
			Vector<FacFacturacionProgramadaBean> vDatos = admFacturacionProgramada.selectDatosFacturacionBean(sWhere,codigos, orden);
			CenInstitucionBean cenInstitucionBean = null;
		    if(vDatos != null && vDatos.size()>0){
		    	cenInstitucionBeans = new Vector<CenInstitucionBean>();
		    	for (FacFacturacionProgramadaBean facturacionProgramadaBean : vDatos) {
		    		cenInstitucionBean = new  CenInstitucionBean();
		    		cenInstitucionBean.setIdInstitucion(facturacionProgramadaBean.getIdInstitucion());
		    		cenInstitucionBeans.add(cenInstitucionBean);
				}
		    }		
		    
		} catch (Exception e) { 
			ClsLogging.writeFileLogError("### Error general al obtener instituciones con Facturas)", e, 3);
		}
		return cenInstitucionBeans;
	}
    
    
	/**
	 * M�todo para el procesado autom�tico de facturacion (SIGASvlProcesoFacturacion)
	 * 
	 * @param idInstitucion
	 * @param idUsuario
	 * @throws SIGAException
	 */
	public boolean procesarFacturas(String idInstitucion, UsrBean userBean) {	    		
		UserTransaction tx = (UserTransaction) userBean.getTransactionPesada();		
		boolean alMenosUnafacturacionProgramadaEncontrada = false;
		
		try {				
			Hashtable<Integer,Object> codigos = new Hashtable<Integer,Object>();
			codigos.put(new Integer("1"), idInstitucion);
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
			// Obtencion de la propiedad que contiene el tiempo de espera que se les da a las facturaciones en ejcucion no generadas por alguna anomal�a			
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
					this.generandoFacturacion(idInstitucion, beanFacturacionProgramada.getIdSerieFacturacion().toString(), beanFacturacionProgramada.getIdProgramacion().toString());

					ClsLogging.writeFileLog("### PROCESADO facturaci�n AUTOMATICA " ,7);
		    		
		    	} catch (Exception e) {
					try { // Tratamiento rollback
						if (Status.STATUS_ACTIVE  == tx.getStatus()){
							tx.rollback();
						}
					} catch (Exception e2) {}		
		    		ClsLogging.writeFileLogError("### Error procesando facturaci�n AUTOMATICA " ,e,3);
		    	}
				
		    	vDatos = admFacturacionProgramada.selectDatosFacturacionBean(sWhere,codigos, orden);
		    }// del WHILE		
		    
		} catch (Exception e) { 
			ClsLogging.writeFileLogError("### Error general al procesar facturas (INSTITUCION:" + idInstitucion + ")", e, 3);
		}
		
		return alMenosUnafacturacionProgramadaEncontrada;
	}
	
	/**
	 * Confirmaci�n autom�tica de facturacion programada (SIGASvlProcesoFacturacion)
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
			
			// obtenci�n de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
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
						         " WHERE PREVIA." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION  +
						           " AND PREVIA." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACIONPREVIA + 
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
					this.confirmarProgramacionFactura(beanFacturacionProgramada, request, false, log, true, false, 1, false);
					
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso autom�tico):" + e.getMsg(), e, 3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso autom�tico) Programaci�n:", e, 3);
				}
				
				vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
		    }// del WHILE

		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso autom�tico) INSTITUCION:"+idInstitucion ,e,3);
		}
		
		return alMenosUnafacturacionProgramadaEncontrada;
	}

	/**
	 * Notas Jorge PT 118:
	 * Genera el zip con los pdf de las facturas de las series de facturaci�n programadas para la institucion
	 * <code>idinstitucion</code>. Se buscan series de facturaci�n con los siguientes criterios:
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
			// obtenci�n de las facturaciones programadas y pendientes con fecha de prevista confirmacion pasada a ahora
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
					this.confirmarProgramacionFactura(factBean, request, false, log, true, true, 1, false);
					this.generarZip(factBean.getIdInstitucion().toString(), factBean.getIdSerieFacturacion().toString() + "_" + factBean.getIdProgramacion().toString());
					
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso autom�tico):"+e.getMsg(),e,3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso autom�tico) Programaci�n:" ,e,3);
				}
		    }// del for

		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso autom�tico) INSTITUCION:"+idInstitucion ,e,3);
		}
	}
	
	public void generarPDFsYenviarFacturasProgramacion(HttpServletRequest request, String idInstitucion) {
		UsrBean userBean = this.usrbean;
		
		try {
			ClsLogging.writeFileLog("GENERAR PDF DE FACTURAS POR INSTITUCION: "+idInstitucion,10);			
			
   			// fichero de log
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    
			// Obtencion de la propiedad que contiene el tiempo de espera que se les da a las facturaciones en ejcucion no generadas por alguna anomal�a			
		    Long tiempoEsperaBloqueosProperty = Long.valueOf(p.returnProperty("facturacion.programacionAutomatica.maxMinutosEnEjecucion"));
			String tiempoMaximoEjecucionBloqueada = String.valueOf(tiempoEsperaBloqueosProperty/(24.0*60.0));		    
			
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
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " + // Solo las que estan generadas 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
							" AND (" + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROGRAMADA +
								   " OR (" + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_PROCESANDO +
								           " AND SYSDATE - " +tiempoMaximoEjecucionBloqueada+ " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" )) ";			
			
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
		    		this.generarPdfEnvioProgramacionFactura(factBean, request, log, factBean.getIdSerieFacturacion(), factBean.getIdProgramacion(), claves, hashNew, false, tx);
				
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso autom�tico):"+e.getMsg(),e,3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso autom�tico) Programaci�n:" ,e,3);
				}
		    }// del for
		    
		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso autom�tico) INSTITUCION:"+idInstitucion ,e,3);
		}
	}
	
	public void generarEnviosFacturasPendientes(HttpServletRequest request, String idInstitucion) {
		UsrBean userBean = this.usrbean;
		
		try {
			ClsLogging.writeFileLog("GENERAR ENVIOS DE FACTURAS POR INSTITUCION: " + idInstitucion, 10);
			
   			// fichero de log
		    ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    
			// Obtencion de la propiedad que contiene el tiempo de espera que se les da a las facturaciones en ejcucion no generadas por alguna anomal�a			
		    Long tiempoEsperaBloqueosProperty = Long.valueOf(p.returnProperty("facturacion.programacionAutomatica.maxMinutosEnEjecucion"));
			String tiempoMaximoEjecucionBloqueada = String.valueOf(tiempoEsperaBloqueosProperty/(24.0*60.0));		    
			
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
							" AND " + FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO + " <= SYSDATE " +
							" AND " + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " IS NOT NULL " + // Solo las que estan generadas 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOPDF + " = " + FacEstadoConfirmFactBean.PDF_FINALIZADA + // Para los estados de confirmacion adecuados
							
							" AND (" + FacFacturacionProgramadaBean.C_IDESTADOENVIO + " = " + FacEstadoConfirmFactBean.ENVIO_PROGRAMADA +
								   " OR (" + FacFacturacionProgramadaBean.C_IDESTADOENVIO + " = " + FacEstadoConfirmFactBean.ENVIO_PROCESANDO +
								           " AND SYSDATE - " +tiempoMaximoEjecucionBloqueada+ " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" )) ";			
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Vector<?> vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0;i<vDatos.size();i++){

				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("ENVIAR FACTURACION PROGRAMADA: " + idInstitucion + " " + factBean.getIdSerieFacturacion() + " " + factBean.getIdProgramacion(), 10);

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
		    		this.generarPdfEnvioProgramacionFactura(factBean, request, log, factBean.getIdSerieFacturacion(), factBean.getIdProgramacion(), claves, hashNew, true, tx);
				
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al confirmar facturas (Proceso autom�tico):"+e.getMsg(),e,3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al confirmar facturas (Proceso autom�tico) Programaci�n:" ,e,3);
				}
		    }// del for
		    
		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al confirmar facturas (Proceso autom�tico) INSTITUCION: " + idInstitucion, e, 3);
		}
	}
	
	
	private void generarZip(String idInstitucion, String nombreFichero) throws SIGAException, ClsExceptions{
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);		
	    
	    FacFacturaAdm factAdm = new FacFacturaAdm(usrbean);
	    FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usrbean);
	    CenPersonaAdm personaAdm = new CenPersonaAdm(usrbean);
	    
	    String sRutaTemporal = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava") + 
								rp.returnProperty("facturacion.directorioFacturaPDFJava") +
								File.separator + idInstitucion + File.separator;
		String sRutaJava = sRutaTemporal + nombreFichero + File.separator;

		ArrayList<FacFicherosDescargaBean> lista = new ArrayList<FacFicherosDescargaBean>();
		FacFicherosDescargaBean facFicherosDescargaBean = null;
		//Control de que no exista el fichero a descargar:
		File directorio = new File(sRutaJava);

		String[] ficheros = directorio.list();
		if (ficheros == null || ficheros.length==0){
			directorio.delete();
			throw new SIGAException("messages.facturacion.descargaFacturas");
			
		} else {
			for (int x=0;x<ficheros.length;x++){
				facFicherosDescargaBean = new FacFicherosDescargaBean();
				File fichero = new File(sRutaJava+File.separator+ficheros[x]);
				String[] nombreFicherosarrays = fichero.getName().split("-");
				if (nombreFicherosarrays.length < 2) throw new SIGAException("El nombre del fichero no contiene '-' y eso da error: " + fichero.getName());
				String[] separacionExtensionDelFichero = nombreFicherosarrays[1].split(Pattern.quote("."));
				
				//Obtenemos las facturas del fichero para obtener el tipo de formato de salida del pdf 
				Vector<?> factura = factAdm.getFactura(idInstitucion, separacionExtensionDelFichero[0]);
				Hashtable<String, Object> hFactura = (Hashtable<String, Object>) factura.get(0);
				
				String idPersona = (String)hFactura.get(FacFacturaBean.C_IDPERSONA);
				String idSerieFacturacion = (String)hFactura.get(FacFacturaBean.C_IDSERIEFACTURACION);
				String idInstitucionFac= (String)hFactura.get(FacFacturaBean.C_IDINSTITUCION);
				
				// Obtenemos el nombre de la persona de la factura
				String nombreColegiado ="";
				if(idPersona != null && !"".equalsIgnoreCase(idPersona)){
					 nombreColegiado = personaAdm.obtenerNombreApellidos(idPersona);
					if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
						nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
					}else{
						nombreColegiado="";
					}
				}
				String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion +
						" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " + idInstitucionFac;
				Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
				
				if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
					FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
					facFicherosDescargaBean.setFormatoDescarga(beanSerieFacturacion.getIdNombreDescargaPDF() );
					facFicherosDescargaBean.setFichero(fichero);
					facFicherosDescargaBean.setNombreFacturaFichero(nombreColegiado);
					lista.add(facFicherosDescargaBean);
				}
			}
			
			this.doZip(sRutaTemporal, nombreFichero, lista);
		}
	}	
	
	/**
	 * @param rutaServidorDescargasZip
	 * @param nombreFichero
	 * @param ficherosPDF
	 * @throws ClsExceptions
	 */
	public void doZip(String rutaServidorDescargasZip, String nombreFichero, ArrayList<FacFicherosDescargaBean> ficherosPDF) throws ClsExceptions	{
		doZipGeneracionRapida(rutaServidorDescargasZip, nombreFichero, ficherosPDF);
	}
    
	
	/**
	 * 
	 * @param rutaServidorDescargasZip
	 * @param nombreFichero
	 * @param ficherosPDF
	 * @throws ClsExceptions
	 */
	public void doZipGeneracionRapida(String rutaServidorDescargasZip, String nombreFichero, ArrayList<FacFicherosDescargaBean> ficherosPDF) throws ClsExceptions {
		// Generar Zip
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;
		
		try {
		    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: numero de facturas = "+ficherosPDF.size(),10);

			if ((ficherosPDF!=null) && (ficherosPDF.size()>0)) {
				
				ficZip = new File(rutaServidorDescargasZip +  nombreFichero + ".zip");

				// RGG 
				if (ficZip.exists()) {
				    ficZip.delete();
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: el fichero zip ya existia. Se elimina",10);
				}
				
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<ficherosPDF.size(); i++)
				{

				    File auxFile = (File)ficherosPDF.get(i).getFichero();
				    ClsLogging.writeFileLog("DESCARGA DE FACTURAS: fichero numero "+i+" longitud="+auxFile.length(),10);
					if (auxFile.exists() && ! auxFile.getAbsolutePath().equalsIgnoreCase(ficZip.getAbsolutePath())) {
						ZipEntry ze = null;
						String[] nombreFicherosarrays;
						
						switch (ficherosPDF.get(i).getFormatoDescarga()) {
						case 1:
								 nombreFicherosarrays = auxFile.getName().split("-",2);
								 ze = new ZipEntry(nombreFicherosarrays[1]);
							break;
						case 2:
							//Quitamos la extensi�n y a�adimos el nombre m�s la extensi�n
								String[] separacionExtensionDelFichero = auxFile.getName().split(Pattern.quote("."));
								String[] separacionNombreColegiado = ficherosPDF.get(i).getNombreFacturaFichero().split("-");
								nombreFicherosarrays = separacionExtensionDelFichero[0].split("-",2);
								
								ze = new ZipEntry(nombreFicherosarrays[1] + "-"+separacionNombreColegiado[0]+"."+separacionExtensionDelFichero[1]);
							break;
						case 3:
								nombreFicherosarrays = auxFile.getName().split("-",2);
								ze = new ZipEntry(ficherosPDF.get(i).getNombreFacturaFichero()+ nombreFicherosarrays[1]);
							break;
						case -1: //Tipos de ficheros especiales cuyo nombre no se ha de modificar
							ze = new ZipEntry(auxFile.getName());
						break;
	
						default:
							nombreFicherosarrays = auxFile.getName().split("-",2);
							ze = new ZipEntry(ficherosPDF.get(i).getNombreFacturaFichero()+  nombreFicherosarrays[1]);
							break;
						}
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
			ClsLogging.writeFileLogError("FileNotFoundExceptione.Error al crear fichero zip",e,10);
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} catch (IOException e) {
			ClsLogging.writeFileLogError("ClsExceptions.Error al crear fichero zip",e,10);
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
     * - Productos y Servicios > Solicitudes > Compra/Subscripci�n (facturacion rapida)
     * - Productos y Servicios > Gesti�n Solicitudes (facturacion rapida)
     * - Certificados > Gesti�n de solicitudes (facturacion rapida)
     * - Confirmaci�n autom�tica de facturacion programada (SIGASvlProcesoFacturacion)
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

    		ClsLogging.writeFileLog("CONFIRMAR FACTURACION : "+beanP.getIdInstitucion()+" " +beanP.getIdSerieFacturacion()+" " +beanP.getIdProgramacion(),10);

    		// fichero de log
    		nombreFichero = "LOG_FAC_CONFIRMACION_" + beanP.getIdSerieFacturacion() + "_" + beanP.getIdProgramacion() + ".log.xls"; 
    		if(log==null)
    			log = new SIGALogging(pathFichero2+sBarra2+beanP.getIdInstitucion()+sBarra2+nombreFichero);

    		Long idSerieFacturacion = beanP.getIdSerieFacturacion();			
    		Long idProgramacion 	= beanP.getIdProgramacion();
    		String usuMod			= this.usrbean.getUserName();

    		// Se confirma la facturaci�n
    		FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
    		Hashtable<String,Object> hashNew = new Hashtable<String,Object>();				
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, beanP.getIdInstitucion());
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,idSerieFacturacion );
//    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
    		if (archivarFacturacion) {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_ARCHIVARFACT, "1");
    		} else {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_ARCHIVARFACT, "0");
    		}

    		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION,FacFacturacionProgramadaBean.C_IDPROGRAMACION,FacFacturacionProgramadaBean.C_IDSERIEFACTURACION};
    		String [] camposFactura = {FacFacturacionProgramadaBean.C_FECHACONFIRMACION, 
    				FacFacturacionProgramadaBean.C_ARCHIVARFACT,
    				FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION,
    				FacFacturacionProgramadaBean.C_LOGERROR,
    				FacFacturacionProgramadaBean.C_NOMBREFICHERO
    				,FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO,
    				FacFacturacionProgramadaBean.C_IDESTADOPDF,
    				FacFacturacionProgramadaBean.C_IDESTADOENVIO,
    				FacFacturacionProgramadaBean.C_GENERAPDF,
    				FacFacturacionProgramadaBean.C_ENVIO,
    				FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL};

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
	    			
	    			if (codretorno.equals("-205")){
	    				throw new SIGAException ("messages.facturacion.confirmar.contadorRepetido");
	    			}
	    			
	    			if (codretorno.equals("-208")){
	    				throw new SIGAException("messages.facturacion.confirmacion.errorPdf");	
	    			}	
	    			
	    			if (!codretorno.equals("0")){
	    				throw new ClsExceptions (UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.confirmarFacturacion.mensaje.generacionDisquetesERROR") +resultadoConfirmar[1]);
	    			}
	
	    			// RGG 05/05/2009 Cambio (solo se generan los pagos por banco cuando se indica por par�metro)
	    			if (generarPagosBanco) {
		    		
	    				// preparando llamada al paquete para la generacion del fichero
	    				ArrayList<String> param_in = FicheroBancarioPagosAction.prepararParametrosParaGenerarFichero(null, this.usrbean);
	    				param_in.add(idSerieFacturacion.toString()); //p_Idseriefacturacion
	    				param_in.add(idProgramacion.toString()); //p_Idprogramacion
	    				Object[] param_in_banco = param_in.toArray();
	    				String[] resultado = new String[3];
	    				
	    				// ejecutando el PL que generara los ficheros
		    			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CARGOS.PRESENTACION(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 3, param_in_banco);
		    			
		    			codretorno = resultado[1];
		    			if (!codretorno.equals("0")){
		    				throw new ClsExceptions (UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.confirmarFacturacion.mensaje.generacionDisquetesERROR") + resultado[2]);
		    			}
	    			}
	    			
	    			// JPT 17/11/2016: Se realiza el informe de confirmacion
	    			if (!esFacturacionRapida) {
	    				/**************  CREAMOS EL INFORME DE CONFIRMACION DE FACTURACION    ****************/
	    				AdmInformeAdm datosInforme = new AdmInformeAdm(this.usrbean);
	    				Hashtable<String,Object> hashWhere = new Hashtable<String,Object>();			
	    				UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDTIPOINFORME, "FACT");
	    				UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDINSTITUCION, "0");
	    				
	    				ClsLogging.writeFileLog("### Inicio datosInforme CONFIRMACION",7);
	    				
	    				Vector<AdmInformeBean> vInforme = datosInforme.select(hashWhere);
	    	
	    				if (vInforme!=null && vInforme.size()>0) {
	    					
    						ArrayList<HashMap<String, String>> filtrosInforme = new ArrayList<HashMap<String, String>>();
    						HashMap<String, String> filtro = new HashMap<String, String>();
    						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDIOMA");
    						filtro.put("VALOR", this.usrbean.getLanguageInstitucion().toString());
    						filtrosInforme.add(filtro);	
    						
    						filtro = new HashMap<String, String>();
    						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDSERIEFACTURACION");
    						filtro.put("VALOR", idSerieFacturacion.toString());
    						filtrosInforme.add(filtro);	
    						
    						filtro = new HashMap<String, String>();
    						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDPROGRAMACION"); 
    						filtro.put("VALOR", idProgramacion.toString());
    						filtrosInforme.add(filtro);
    						
    						filtro = new HashMap<String, String>();
    						filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDINSTITUCION");
    						filtro.put("VALOR", beanP.getIdInstitucion().toString());
    						filtrosInforme.add(filtro);

    						String ruta = p.returnProperty("facturacion.directorioFisicoFacturaPDFJava") + 
    								p.returnProperty("facturacion.directorioFacturaPDFJava") +
    								ClsConstants.FILE_SEP + beanP.getIdInstitucion().toString() + 
    								ClsConstants.FILE_SEP + idSerieFacturacion.toString() + 
    								"_" + idProgramacion.toString();
    						FileHelper.mkdirs(ruta);
    						File rutaPDF = new File(ruta);
    						if (!rutaPDF.exists()) {
    							throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
    						} else {
    							if (!rutaPDF.canWrite()) {
    								throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
    							}
    						}    						
	    					
	    					for (int iInforme = 0; iInforme < vInforme.size(); iInforme++){				
	    						AdmInformeBean informe = vInforme.get(iInforme);
	    						
	    						informe.setNombreSalida("CONFIRMACION_" + idSerieFacturacion + "_" + idProgramacion); 
	    						
	    						ClsLogging.writeFileLog("### Inicio generaci�n fichero excel CONFIRMACION",7);
	    						
	    						ArrayList<File> listaFicherosConfirmacion = InformePersonalizable.generarInformeXLS(informe, filtrosInforme, ruta, this.usrbean);
	    						
	    						ClsLogging.writeFileLog("### Fin generaci�n fichero excel CONFIRMACION",7);
	    		
	    						// Si no se generan los informes de confirmacion
	    						if (listaFicherosConfirmacion==null || listaFicherosConfirmacion.size()==0) {
	    							ClsLogging.writeFileLog("### Error al generar el informe de la confirmacion. Inicio creaci�n fichero log CONFIRMACION sin datos",7);
	    							throw new SIGAException("message.facturacion.error.fichero.nulo");						
	    						}
	    						
	    						File ficheroXls = listaFicherosConfirmacion.get(0);
	    						UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_NOMBREFICHERO, ficheroXls.getName());
	    					}	
	    				}
	    			}
	    			
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA);
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "sysdate");
	    			
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_LOGERROR,"");	   
	    			//si hay que generar el pdf lo hacemos
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_GENERAPDF,beanP.getGenerarPDF());
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_ENVIO,beanP.getEnvio());
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL,beanP.getIdTipoPlantillaMail());
	    			if(!esFacturacionRapida && beanP.getGenerarPDF()!=null && beanP.getGenerarPDF().equalsIgnoreCase(AppConstants.DB_TRUE)) {
	    				GenParametrosAdm param = new GenParametrosAdm(usrbean);
		    			//Metemos 15 minuto de realy por defecto
		    			String horapdfyenvio = param.getValor(""+beanP.getIdInstitucion(), "FAC", "HORA_PDF_Y_ENVIO", "15");
		    			SimpleDateFormat sdf = new SimpleDateFormat();
		    			
		    			String fechaPrevistaPdfYEnvio = null;
		    			if(horapdfyenvio!=null && horapdfyenvio.indexOf(":")>0) {
			    			sdf.applyPattern("yyyy/MM/dd");
			    			StringBuilder fecha = new StringBuilder();
			    			fecha.append(sdf.format(new Date()));
			    			fecha.append(" ");
			    			fecha.append(horapdfyenvio);
			    			fecha.append(":00");
			    			sdf.applyPattern(ClsConstants.DATE_FORMAT_JAVA);
			    			fechaPrevistaPdfYEnvio = fecha.toString();
	//	    				date = sdf.parse(fecha.toString());
		    			}else if(horapdfyenvio!=null && horapdfyenvio.indexOf(":")<0) {
		    				Date date = new Date();
		    				int milisegundosRelay = 0;
		    				try {
		    					milisegundosRelay = Integer.valueOf(horapdfyenvio)*60000;	
		    				} catch (Exception e) {
		    					milisegundosRelay = 900000;
		    				}
		    				date.setTime(date.getTime() + milisegundosRelay);
		    				sdf.applyPattern(ClsConstants.DATE_FORMAT_JAVA);
		    				fechaPrevistaPdfYEnvio = sdf.format(date);
		    			}else {
		    				Date date = new Date();
		    				int milisegundosRelay = 900000;
		    				date.setTime(date.getTime() + milisegundosRelay);
		    				sdf.applyPattern(ClsConstants.DATE_FORMAT_JAVA);
		    				fechaPrevistaPdfYEnvio = sdf.format(date);
		    				
		    			}
		    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO,fechaPrevistaPdfYEnvio);
		    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDESTADOPDF,FacEstadoConfirmFactBean.PDF_PROGRAMADA);
		    			if(beanP.getEnvio()!=null && beanP.getEnvio().equalsIgnoreCase(AppConstants.DB_TRUE)){
		    				UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacEstadoConfirmFactBean.ENVIO_PROGRAMADA);
		    				UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL,beanP.getIdTipoPlantillaMail());
		    			}else {
		    				UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
		    			}
		    				
	    			}else {
	    				UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_FECHAPREVISTAPDFYENVIO,"");
		    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDESTADOPDF,FacEstadoConfirmFactBean.PDF_NOAPLICA);
		    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacEstadoConfirmFactBean.ENVIO_NOAPLICA);
	    				
	    			}
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
	
	    			String sms;
	    			if (e instanceof SIGAException) {
	    				SIGAException e2 = (SIGAException) e;
	    				sms = UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(), e2.getLiteral());	    				
	    				
	    			} else {
	    				if(e.getMessage()!=null)
	    					sms = e.getMessage();
	    				else
	    					sms = e.toString();
	    			}
	    			ClsLogging.writeFileLog("ERROR AL CONFIRMAR Y PRESENTAR: " + sms, 10);
    				log.writeLogFactura("CONFIRMACION","N/A","N/A","Error en proceso de confirmaci�n: " + sms);
	
	    			//////////// INICIO TRANSACCION ////////////////
	    			if (tx!=null)
	    				tx.begin();
	    			
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.ERROR_CONFIRMACION);
	    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHACONFIRMACION, "");
	    			UtilidadesHash.set(hashNew,FacFacturacionProgramadaBean.C_LOGERROR, nombreFichero);
	    			facadm.updateDirect(hashNew, claves, camposFactura);
	    			
	    			if (tx!=null)
	    				tx.commit();
	    			//////////// FIN TRANSACCION ////////////////
	
	    			ClsLogging.writeFileLog("CAMBIA ESTADO A FINALIZADA ERRORES.",10);
	    			throw new ClsExceptions(UtilidadesString.getMensajeIdioma(this.usrbean, "messages.facturacion.confirmacion.error") + sms);
	    		}
	    		
	    		//INSERTAMOS EN LA COLA LA OPERACI�N CREARCLIENTE(): (NO HAY PROBLEMA PORQUE SI EL CLIENTE YA EXISTE LO ACTUALIZA, Y HACE UN INTENTO DE TRASPASAR �NICAMENTE LAS FACTURAS QUE TENGA SIN TRASPASAR, QUE ES LO QUE NOS INTERESA).
	    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, FacEstadoConfirmFactBean.TRASPASO_PROGRAMADA);
	    		Short idInstitucion = Short.valueOf(String.valueOf(beanP.getIdInstitucion()));
	    		encolarTraspasoFacturas(idInstitucion, idSerieFacturacion, idProgramacion);

    		} else {
    			UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, FacEstadoConfirmFactBean.CONFIRM_FINALIZADA); //Si entramos por aqui es que ya hemos confirmado previamente
    		}// FIN IF EJECUTAR CONFIRMACION

//    		ClsLogging.writeFileLog("ENTRA A GENERAR Y ENVIAR",10);
    		
    		
//    		boolean isGenerarPdf = beanP.getGenerarPDF() != null && beanP.getGenerarPDF().trim().equals("1") && !esFacturacionRapida;
//    			
//    		if(isGenerarPdf){
//    			boolean isGenerarEnvio = beanP.getEnvio() != null && beanP.getEnvio().trim().equals("1") && (beanP.getRealizarEnvio()==null || beanP.getRealizarEnvio().toString().equalsIgnoreCase("1"));
//    			msjAviso = this.generarPdfEnvioProgramacionFactura(beanP,req,log,idSerieFacturacion, idProgramacion, claves, hashNew, isGenerarEnvio, tx);
//    		}
			
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
			
    		throw new SIGAException("messages.facturacion.confirmacion.error");
    	}

    	if(msjAviso!=null)
    		throw new ClsExceptions(msjAviso);
    }
    
    /*private boolean isServicioTraspasoFacturasActivo(Short idInstitucion)
	{
    	GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);	
		GenParametros genParametros = new GenParametros();
		genParametros.setModulo(AppConstants.MODULO.ECOM.toString());
		genParametros.setIdinstitucion(idInstitucion);
		genParametros.setParametro(AppConstants.PARAMETRO.TRASPASO_FACTURAS_WS_ACTIVO.toString());
		GenParametros parametro = genParametrosService.get(genParametros);
		String permiso = AppConstants.DB_FALSE;
		if(parametro!=null && parametro.getValor()!=null){
			permiso = parametro.getValor();
		}
		if(permiso.equals(AppConstants.DB_FALSE))
			return false;
		
		return true;
	}*/
    
    private String getServicioTraspasoFacturasActivo(Short idInstitucion)
	{
    	String valor = "0";
    	GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);	
		GenParametros genParametros = new GenParametros();
		genParametros.setModulo(AppConstants.MODULO.ECOM.toString());
		genParametros.setIdinstitucion(idInstitucion);
		genParametros.setParametro(AppConstants.PARAMETRO.TRASPASO_FACTURAS_WS_ACTIVO.toString());
		GenParametros parametro = genParametrosService.get(genParametros);
		if(parametro!=null && parametro.getValor()!=null){
			valor = parametro.getValor();
		}
		
		return valor;
	}
    
    private boolean isSerieFacturacionActiva(Short idInstitucion, Long idSerieFacturacion, Long idProgramacion)
    {
    	boolean bResultado = false;
    	
    	try
    	{
    		Hashtable<Integer, Object> codigos = new Hashtable<Integer, Object>();
			codigos.put(new Integer("1"), idInstitucion);
			codigos.put(new Integer("2"), idSerieFacturacion);
			codigos.put(new Integer("3"), idProgramacion);
			
			String sWhere = " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
						" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = :2 " +
						" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = :3 " +
						" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_TRASPASOFACTURAS + " = '1'";
	
			FacFacturacionProgramadaAdm facturacionProgramadaAdm = new FacFacturacionProgramadaAdm(this.usrbean);
			Vector<?> vDatos = facturacionProgramadaAdm.selectBind(sWhere, codigos);
			
			if(vDatos != null && vDatos.size()>0)
				bResultado = true;
    	}
    	catch (Exception e)
    	{
    		ClsLogging.writeFileLogError("@@@ Error al tratar de recuperar si la Serie de Facturaci�n " + idSerieFacturacion + " est� activa.", e, 3);
    	}
    	
    	return bResultado;
    }
    
    public void encolarTraspasoFacturas(Short idInstitucion, Long idSerieFacturacion, Long idProgramacion) throws ClsExceptions
    {
		FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
		
    	int estado = 0;
    	//CONSULTA DE LA SERIE DE FACTURACI�N (idinstitucion e idseriefacturacion) PARA VER SI HAY QUE TRASPASARLA O NO: FAC_FACTURACION.TRASPASOFACTURAS (1, 0):
    	//SI LA TRANSFERENCIA DE FACTURAS ACTIVA ES NAVISION Y EST� ACTIVA LA SERIE DE FACTURACI�N: 
    	if (this.getServicioTraspasoFacturasActivo(idInstitucion).equals("1") && isSerieFacturacionActiva(idInstitucion, idSerieFacturacion, idProgramacion))
    	{
	    	//INSERTAMOS EN LA COLA LA OPERACI�N CREARCLIENTE(): (NO HAY PROBLEMA PORQUE SI EL CLIENTE YA EXISTE LO ACTUALIZA, Y HACE UN INTENTO DE TRASPASAR �NICAMENTE LAS FACTURAS QUE TENGA SIN TRASPASAR, QUE ES LO QUE NOS INTERESA).
			HashMap map = new HashMap<String, String>();
			map.put(FacFactura.C_IDINSTITUCION, String.valueOf(idInstitucion));
			map.put(FacFactura.C_IDSERIEFACTURACION, String.valueOf(idSerieFacturacion));
			map.put(FacFactura.C_IDPROGRAMACION, String.valueOf(idProgramacion));
			
			EcomCola ecomColaCrearCliente = new EcomCola();
			ecomColaCrearCliente.setIdinstitucion(idInstitucion);
			ecomColaCrearCliente.setIdoperacion(AppConstants.OPERACION.TRASPASAR_FACTURAS_CREARCLIENTE_NAVISION.getId());
			 
			estado = FacEstadoConfirmFactBean.TRASPASO_PROGRAMADA;
			
			EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
			ecomColaService.insertaColaConParametros(ecomColaCrearCliente, map);
    	}
    	else //EN CASO DE A�ADIR NUEVOS M�TODOS DE TRASPASO, IR A�ADIENDO LLAMADAS AQU�.
    	{
    		estado = FacEstadoConfirmFactBean.TRASPASO_NOAPLICA;
    	}
    	
		//CAMBIO DE ESTADO DE TRASPASO DE LA FACTURACI�N: 
		Hashtable<String,Object> hashNew = new Hashtable<String,Object>();
		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion);
		
		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_FECHAMODIFICACION, "sysdate");
		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, estado);
		String [] camposParaActualizar = {FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, FacFacturacionProgramadaBean.C_FECHAMODIFICACION};
		facadm.updateDirect(hashNew, null, camposParaActualizar);
    }
    
    public void comprobacionTraspasoFacturas(HttpServletRequest request, String idInstitucion)
    {
		UsrBean userBean = this.usrbean;
		
		try {
			ClsLogging.writeFileLog("FACTURAS: " + idInstitucion, 10);
			
   			ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    
			// Obtencion de la propiedad que contiene el tiempo de espera
		    Long tiempoEsperaBloqueosProperty = Long.valueOf(p.returnProperty("facturacion.programacionAutomatica.maxMinutosEnEjecucion"));
			String tiempoMaximoEjecucionBloqueada = String.valueOf(tiempoEsperaBloqueosProperty/(24.0*60.0));
			
			// OBTENCI�N DE LAS FACTURACIONES CON ESTADO PROCESANDO Y TODAS SUS FACTURAS HAN SIDO TRASPASADAS
			Hashtable<Integer, Object> codigos = new Hashtable<Integer, Object>();
			codigos.put(new Integer("1"), idInstitucion);
			
			String sWhere = " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDINSTITUCION +
   							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " =  FAC_SERIEFACTURACION." + FacSerieFacturacionBean.C_IDSERIEFACTURACION +							
							" AND " + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + " IS NOT NULL " + // Solo las que estan confirmadas 
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION + " = " + FacEstadoConfirmFactBean.CONFIRM_FINALIZADA + // Para los estados de confirmacion adecuados 
							
							" AND " + FacFacturacionProgramadaBean.C_IDESTADOTRASPASO + " = " + FacEstadoConfirmFactBean.TRASPASO_PROCESANDO +
							
							" AND (not exists (select 1 from " + FacFacturaBean.T_NOMBRETABLA +
									" where " + FacFacturaBean.T_NOMBRETABLA + ".idinstitucion = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".idinstitucion " +
											"and " + FacFacturaBean.T_NOMBRETABLA + ".idseriefacturacion = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".idseriefacturacion " +
											"and " + FacFacturaBean.T_NOMBRETABLA + ".idprogramacion = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".idprogramacion " +
											"and " + FacFacturaBean.T_NOMBRETABLA + ".traspasada is null)" + 
							
							" OR SYSDATE - " + tiempoMaximoEjecucionBloqueada + " > " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAMODIFICACION +" ) ";
			
			String[] orden = {FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION};
			
			FacFacturacionProgramadaAdm factAdm = new FacFacturacionProgramadaAdm(userBean);
			Vector<?> vDatos = factAdm.selectDatosFacturacionBean(sWhere, codigos, orden);
			
		    for (int i=0; i<vDatos.size(); i++)
		    {
				// PROCESO PARA CADA FACTURACION PROGRAMADA
		    	FacFacturacionProgramadaBean factBean = (FacFacturacionProgramadaBean)vDatos.elementAt(i);
		    	
				ClsLogging.writeFileLog("ENVIAR FACTURACION PROGRAMADA: " + idInstitucion + " " + factBean.getIdSerieFacturacion() + " " + factBean.getIdProgramacion(), 10);

		    	try {
		    		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION, FacFacturacionProgramadaBean.C_IDPROGRAMACION, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION};
		    		Hashtable<String,Object> hashNew = new Hashtable<String,Object>();	
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDINSTITUCION, factBean.getIdInstitucion());
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDPROGRAMACION, factBean.getIdProgramacion());
		    		UtilidadesHash.set(hashNew, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION,factBean.getIdSerieFacturacion() );

		    		UserTransaction tx = (UserTransaction) this.usrbean.getTransactionPesada();
		    		 
		    		this.cambiarEstadoTraspasoFacturacion(factBean, claves, hashNew, tx);
		    		
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("@@@ Error controlado al traspasar facturas pendientes (Proceso autom�tico): " + e.getMsg(), e, 3);
					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("@@@ Error al traspasar facturas pendientes (Proceso autom�tico) Programaci�n: ", e, 3);
				}
		    }// del for
		    
		} catch (Exception e) {
			// Error general (No hacemos nada, para que continue con la siguiente institucion
			ClsLogging.writeFileLogError("@@@ Error general al traspasar facturas pendientes (Proceso autom�tico) INSTITUCION: " + idInstitucion, e, 3);
		}
	}
    
    //M�todo que busca si hay alguna factura que ha sido traspasada con error: 
    private String cambiarEstadoTraspasoFacturacion(
    		FacFacturacionProgramadaBean beanP, 
    		String [] claves, 
    		Hashtable<String,Object> hashFactura, 
    		UserTransaction tx)throws ClsExceptions, SIGAException, Exception {
    	
    	String msjAviso = null;
    	String [] camposTraspaso = { FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, FacFacturacionProgramadaBean.C_LOGTRASPASO };
    	
    	// fichero de log: 
    	ReadProperties p = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
    	String pathFicheroLog = p.returnProperty("facturacion.directorioFisicoLogProgramacion");
		String sBarra2 = "";
		if (pathFicheroLog.indexOf("/") > -1) sBarra2 = "/"; 
		if (pathFicheroLog.indexOf("\\") > -1) sBarra2 = "\\";
		String nombreFichero = "LOG_FAC_TRASPASO_" + beanP.getIdSerieFacturacion() + "_" + beanP.getIdProgramacion() + ".log.xls";
    	String rutaFichero = pathFicheroLog + sBarra2 + beanP.getIdInstitucion() + sBarra2;
    	File ficheroLogTraspaso = new File(rutaFichero + nombreFichero);
    	if(ficheroLogTraspaso.exists())
    		ficheroLogTraspaso.delete(); //Borramos el fichero de traspaso de logs si ya existe previamente.
		SIGALogging log = new SIGALogging(rutaFichero + nombreFichero);
		
		// fichero de log confirmaci�n: 
	    String nombreFicheroLogConfirmacion = "LOG_FAC_CONFIRMACION_" + beanP.getIdSerieFacturacion() + "_" + beanP.getIdProgramacion() + ".log.xls";
	    SIGALogging logConfirmacion = new SIGALogging(rutaFichero + nombreFicheroLogConfirmacion);
    	
    	try{
    		ClsLogging.writeFileLog("TIENE QUE TRASPASAR FACTURA", 10);
    		
    		
    		//SELECT QUE COMPRUEBA SI HAY ALGUNA FACTURA QUE HA SIDO TRASPADA CON ERROR: 
	    	Hashtable<Integer, Object> codigos = new Hashtable<Integer, Object>();
			codigos.put(new Integer("1"), beanP.getIdInstitucion());
			codigos.put(new Integer("2"), beanP.getIdSerieFacturacion());
			codigos.put(new Integer("3"), beanP.getIdProgramacion());
			
			String sWhere = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :1 " +
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = :2 " +
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + " = :3 " +
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_TRASPASADA + " = '" + FacFacturaBean.ESTADO_TRASPASADA_ERROR + "'";
			
			FacFacturaAdm fc = new FacFacturaAdm(this.usrbean);
			Vector<?> vDatos = fc.selectBind(sWhere, codigos);
			
			if (tx!=null) ////////////INICIO TRANSACCION //////////////// 
				tx.begin();
			
			FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
			
			if(vDatos.size()>0) { //HA HABIDO ALGUNA FACTURA CON ERROR.
				msjAviso = "messages.facturacion.confirmacion.errorPdf"; //ME VALE ESTE MISMO MENSAJE DE AVISO.
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, FacEstadoConfirmFactBean.TRASPASO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRORES.
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_LOGTRASPASO, nombreFichero);
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_LOGERROR, nombreFicheroLogConfirmacion);
				String [] camposTraspaso2 = { FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, FacFacturacionProgramadaBean.C_LOGTRASPASO, FacFacturacionProgramadaBean.C_LOGERROR };
				facadm.updateDirect(hashFactura, claves, camposTraspaso2);
				logConfirmacion.writeLogFactura("Traspaso", "", "", "Error en el proceso de Traspaso de Facturas");
				ClsLogging.writeFileLog("ERROR GENERAL TRASPASO FACTURA. CAMBIO DE ESTADOS", 10);
			} else { //SI NO HAY RESULTADOS ES QUE NO HA HABIDO NINGUNA FACTURA CON ERROR. SE ENV�A MAIL CON EL FICHERO DE LOG.
				msjAviso = "messages.facturacion.confirmacion.errorPdf"; //ME VALE ESTE MISMO MENSAJE DE AVISO.
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOTRASPASO, FacEstadoConfirmFactBean.TRASPASO_FINALIZADA); // cambio de estado TRASPASO a FINALIZADO.
				//UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_LOGTRASPASO, nombreFichero);
				facadm.updateDirect(hashFactura, claves, camposTraspaso);
				ClsLogging.writeFileLog("OK TODO. CAMBIO DE ESTADOS", 10);
			}
	    	
			if (tx!=null) //////////// FIN TRANSACCION //////////////// 
				tx.commit();
			
			
			//GENERACI�N LOG FACTURAS: 
			//BUSCAMOS TODAS LAS FACTURAS DE LA FACTURACI�N: 
			sWhere = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :1 " +
					 " AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = :2 " +
					 " AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + " = :3 ";
			
			vDatos.clear();
			vDatos = (Vector)fc.selectBind(sWhere, codigos);
			FacFacturaBean factBean;
			for(int i=0; i<vDatos.size(); i++)
			{
				factBean = (FacFacturaBean)vDatos.elementAt(i);
				log.writeLogTraspasoFactura(factBean.getNumeroFactura(), factBean.getTraspasada(), (factBean.getErrorTraspaso()!=null)?factBean.getErrorTraspaso():"");	
			}
			
			
			//ENV�O DE CORREO ELECTR�NICO CON EL LOG: 
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
    	    String from = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.TRASPASO_FACTURAS_MAILRESUMEN_FROM, MODULO.ECOM);
    	    String bcc = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.TRASPASO_FACTURAS_MAILRESUMEN_BCC, MODULO.ECOM);
    	    String body = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.TRASPASO_FACTURAS_MAILRESUMEN_BODY, MODULO.ECOM);
    	    
    	    //SELECT PARA OBTENER EL NOMBRE Y LA DESCRIPCI�N DE LA SERIE DE FACTURACI�N: 
	    	codigos.clear();
			codigos.put(new Integer("1"), beanP.getIdInstitucion());
			codigos.put(new Integer("2"), beanP.getIdSerieFacturacion());
			
			sWhere = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = :1 " +
						" AND "   + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = :2 ";
			
			FacSerieFacturacionAdm fcSerieFac = new FacSerieFacturacionAdm(this.usrbean);
			String descripcionSerieFacturacion = "";
			vDatos.clear();
			vDatos = fcSerieFac.selectBind(sWhere, codigos);
			if(vDatos!=null && vDatos.size()>0)
			{
				FacSerieFacturacionBean fAux = (FacSerieFacturacionBean)vDatos.get(0);
				descripcionSerieFacturacion = fAux.getNombreAbreviado() + " - " + fAux.getDescripcion();
			}
			
			//SELECT PARA OBTENER LA DESCRIPCI�N DE LA PROGRAMACI�N: 
	    	codigos.clear();
			codigos.put(new Integer("1"), beanP.getIdInstitucion());
			codigos.put(new Integer("2"), beanP.getIdSerieFacturacion());
			codigos.put(new Integer("3"), beanP.getIdProgramacion());
			
			sWhere = " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = :1 " + 
						" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = :2 " + 
						" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = :3 ";
			
			FacFacturacionProgramadaAdm fcFacProg = new FacFacturacionProgramadaAdm(this.usrbean);
			String descripcionProgramacion = "";
			vDatos.clear();
			vDatos = fcFacProg.selectBind(sWhere, codigos);
			if(vDatos!=null && vDatos.size()>0)
			{
				FacFacturacionProgramadaBean fAux = (FacFacturacionProgramadaBean)vDatos.get(0);
				descripcionProgramacion = fAux.getDescripcion();
			}
    	    
			//CONSTRUIMOS EL CUERPO DEL MENSAJE: 
    	    StringBuilder sb = new StringBuilder();
    	    sb.append(String.format(body, descripcionSerieFacturacion, descripcionProgramacion));
    	    body = sb.toString();
    	    String[] bccArray = bcc.split(";");
    	    
    	    String asunto = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.TRASPASO_FACTURAS_MAILRESUMEN_ASUNTO, MODULO.ECOM);
    	    SigaServiceHelperService serviceHelperService = (SigaServiceHelperService) BusinessManager.getInstance().getService(SigaServiceHelperService.class);
    	    serviceHelperService.enviarCorreo(from, bccArray, asunto, body, new File(rutaFichero + nombreFichero),GEN_PROPERTIES.mail_smtp_actualizacioncenso_sesion, GEN_PROPERTIES.mail_smtp_actualizacioncenso_host, GEN_PROPERTIES.mail_smtp_actualizacioncenso_port, GEN_PROPERTIES.mail_smtp_actualizacioncenso_user, GEN_PROPERTIES.mail_smtp_actualizacioncenso_pwd);
			
    	} catch(Exception e) {
    		ClsLogging.writeFileLog("ERROR GENERAL EN TRY TRASPASO FACTURAS.", 10);
			if (tx!=null && Status.STATUS_ACTIVE  == tx.getStatus())
				tx.rollback();
			
			logConfirmacion.writeLogFactura("TRASPASO", "N/A", "N/A", "Error general en el proceso de Traspaso de Facturas: " + e.toString());
			
			throw e;
    	}
    	
		return msjAviso;
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
	    		UserTransaction tx)throws ClsExceptions, SIGAException, Exception {
    	
    	FacFacturacionProgramadaAdm facadm = new FacFacturacionProgramadaAdm(this.usrbean);
    	
    	String msjAviso = null;
		String [] camposPDF = {FacFacturacionProgramadaBean.C_IDESTADOPDF};
		String [] camposPDFError = {FacFacturacionProgramadaBean.C_IDESTADOPDF,FacFacturacionProgramadaBean.C_LOGERROR};
		String [] camposEnvioPdf = {FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacFacturacionProgramadaBean.C_IDESTADOPDF};
		String [] camposEnvioPdfError = {FacFacturacionProgramadaBean.C_IDESTADOENVIO,FacFacturacionProgramadaBean.C_IDESTADOPDF,FacFacturacionProgramadaBean.C_LOGERROR};
		
		String nombreFichero = "LOG_FAC_CONFIRMACION_" + beanP.getIdSerieFacturacion() + "_" + beanP.getIdProgramacion() + ".log.xls"; 
    	
		try {

			// Se guardan las facturas impresas.
			ClsLogging.writeFileLog("TIENE QUE GENERAR PDF", 10);
	
			//////////// INICIO TRANSACCION ////////////////
			if (tx!=null) 
				tx.begin();
			
			UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_PROCESANDO); // cambio de estado PDF a PROCESANDO
			facadm.updateDirect(hashFactura, claves, camposPDF);
			
			if (tx!=null) 
				tx.commit();
			//////////// FIN TRANSACCION ////////////////
	
			//////////// ALMACENAR RAPIDA ////////////////
			//En facturaciones r�pidas, en compra de PYS no hay que generar el excel con el log
			int errorAlmacenar = this.generaryEnviarProgramacionFactura(req, beanP.getIdInstitucion(), idSerieFacturacion, idProgramacion, isGenerarEnvio, log, tx);
			
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
						UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
						facadm.updateDirect(hashFactura, claves, camposEnvioPdfError);
					} else {
						UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
						facadm.updateDirect(hashFactura, claves, camposPDFError);
					}
					
					break;
					
				case 2: // ERROR EN ENVIO FACTURA
					ClsLogging.writeFileLog("ERROR AL ALMACENAR FACTURA. RETORNO="+errorAlmacenar,3);					
					msjAviso = "messages.facturacion.confirmacion.errorEnvio";
					if (isGenerarEnvio) {
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADA); // cambio de estado PDF a FINALIZADA
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRRORES
						UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
						facadm.updateDirect(hashFactura, claves, camposEnvioPdfError);
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
						UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
						facadm.updateDirect(hashFactura, claves, camposEnvioPdfError);
					} else {
						UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
						UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
						facadm.updateDirect(hashFactura, claves, camposPDFError);
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
			log.writeLogFactura("PDF/ENVIO","N/A","N/A","Error general en el proceso de generaci�n o env�o de facturas PDF: "+e.toString());

			//////////// INICIO TRANSACCION ////////////////
			if (tx!=null)
				tx.begin();
			
			if (isGenerarEnvio) {
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOENVIO, FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES); // cambio de estado ENVIO a FINALIZADO CON ERRRORES
				UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
				facadm.updateDirect(hashFactura, claves, camposEnvioPdfError);

			} else {
				UtilidadesHash.set(hashFactura, FacFacturacionProgramadaBean.C_IDESTADOPDF, FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES); // cambio de estado PDF a FINALIZADA CON ERRRORES
				UtilidadesHash.set(hashFactura,FacFacturacionProgramadaBean.C_LOGERROR,nombreFichero);
				facadm.updateDirect(hashFactura, claves, camposPDFError);
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
     * @param tx
	 * @return int - Devuelve: - 0 si todo est� correcto.
	 * 						   - 1 si ha existido un error en el procesado de la factura.
	 * 						   - 2 si ha existido un error en el procesado del env�o. 
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
			UserTransaction tx
		)  throws ClsExceptions, SIGAException {
		
		UsrBean userbean = this.usrbean;
		
		int salida = 0;
		boolean existeAlgunErrorPdf = false;
		boolean existeAlgunErrorEnvio = false;
		
		try {
			FacFacturaAdm admF = new FacFacturaAdm(userbean);
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(userbean);
			FacFacturacionProgramadaAdm facProgAdm = new FacFacturacionProgramadaAdm(userbean);
			InformeFactura inf = new InformeFactura(userbean);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(userbean);
		    CenPersonaAdm personaAdm = new CenPersonaAdm(userbean);
			
			// Obtengo las facturas a almacenar		    		    
		    Vector<?> vFacturas = admF.getFacturasDeFacturacionProgramada(institucion.toString(), serieFacturacion.toString(), idProgramacion.toString());
		    
		    /** CR - Si no se ha generado ninguna factura, se lanza una excepcion ya que no se puede generar PDF **/
		    if(vFacturas == null || vFacturas.size() < 1){
		    	throw new SIGAException("messages.facturacion.confirmacion.errorPdf");	
		    }
			
			ClsLogging.writeFileLog("ALMACENAR >> "+institucion.toString()+" "+serieFacturacion.toString()+" "+idProgramacion.toString(),10);
			
			// Obtengo la plantilla a utilizar
			Vector<?> vPlantillas = plantillaAdm.getPlantillaSerieFacturacion(institucion.toString(),serieFacturacion.toString());
			String sPlantilla = vPlantillas.firstElement().toString();
						
		    ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);

			//TODO Esta comprobacion de rutas no me parece correcta: deberia pasarse la ruta de o a algun metodo y no hacerlo aqui como en una isla sin relacion con nada a la vista
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
    		FileHelper.mkdirs(rutaTemporal);
			
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
			
			/** CR7 - Se saca fuera ya que siempre se usa la misma plantilla para tdas las facturas **/
			//SE SELECCIONA LA PLANTILLA MAIL			
			Hashtable<String,Object> hashProg = new Hashtable<String,Object>();
			hashProg.put(FacFacturacionProgramadaBean.C_IDINSTITUCION, institucion);
			hashProg.put(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, serieFacturacion);
			hashProg.put(FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
			
			Vector<?> vFacProg = facProgAdm.select(hashProg);
			
			FacFacturacionProgramadaBean facProgBean = new FacFacturacionProgramadaBean();
			Integer plantillaMail = null;
			if(vFacProg != null && vFacProg.size()>0){
				facProgBean = (FacFacturacionProgramadaBean) vFacProg.get(0);
				if(facProgBean.getIdTipoPlantillaMail() != null){
					plantillaMail = facProgBean.getIdTipoPlantillaMail();
				}
			}
			
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no COMPROBAMOS QUE HAYA SIDO CORRECTO EL CAMBIO ANTERIOR
			//while (correcto && listaFacturas.hasMoreElements()){
			ArrayList<FacFicherosDescargaBean> listaFicherosPDFDescarga = new ArrayList<FacFicherosDescargaBean> ();
			ArrayList<File> listaFicheros = new ArrayList<File>();
			FacFicherosDescargaBean facFicherosDescargaBean = null;
    		while (listaFacturas.hasMoreElements()){
    			facFicherosDescargaBean = new FacFicherosDescargaBean();
    			try {
	    			Hashtable<String,Object> facturaHash = (Hashtable<String,Object>)listaFacturas.nextElement();
	    			idFactura = (String)facturaHash.get(FacFacturaBean.C_IDFACTURA);
	    			String idPersona = (String)facturaHash.get(FacFacturaBean.C_IDPERSONA);
	    			String numFactura = (String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA);
	    				    			
	    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA: "+idFactura,10);
	    			
	    			// TRY del proceso de generacion de la factura en PDF
	    			File filePDF = null;
	    			try {
	    				// PROCESO DE CREAR EL PDF
	    				// RGG 15/02/2007 CAMBIOS PARA INFORME MASTER REPOR	    				
	    				filePDF = inf.generarPdfFacturaFirmada(request, facturaHash);

	    				if (filePDF==null) {
	    				    throw new ClsExceptions("message.facturacion.error.fichero.nulo");				
	    				} else {
	    					
	    					// Obtenemos el nombre de la persona de la factura
	    					String nombreColegiado ="";
	    				    nombreColegiado ="";
	    					if(idPersona != null && !"".equalsIgnoreCase(idPersona)){
	    						 nombreColegiado = personaAdm.obtenerNombreApellidos(idPersona);
	    						if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
	    							nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
	    						}else{
	    							nombreColegiado="";
	    						}
	    					}
	    					String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + serieFacturacion.toString()+
	    							" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " + (String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION);
	    					Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
	    					
	    					if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
	    						FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
	    						facFicherosDescargaBean.setFormatoDescarga(beanSerieFacturacion.getIdNombreDescargaPDF() );
	    						facFicherosDescargaBean.setFichero(filePDF);
		    					facFicherosDescargaBean.setNombreFacturaFichero(nombreColegiado);
		    					listaFicherosPDFDescarga.add(facFicherosDescargaBean);
	    					}
		
	    					listaFicheros.add(filePDF);
	    				}
						
		    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA GENERADA EN PDF",10);
	    				
		    			
    				} catch (SIGAException ee) {
    					ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.getLiteral(userbean.getLanguage()),10);
	    				
	    				// ESCRIBO EN EL LOG
   						log.writeLogFactura("PDF",idPersona,numFactura,"Error en el proceso de generaci�n de facturas PDF: "+ee.getLiteral(userbean.getLanguage()));
    					salida=1;
	    	    		//Aunque nos ha fallado esta factura es posible que la siguiente, no.
	    	    		//POR LO TANTO no cazamos la excepcion
	    	    		//throw ee;
	    	    		existeAlgunErrorPdf = true;

	    			} catch (Exception ee) {
	    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.toString(),10);
	    				
	    				// ESCRIBO EN EL LOG
						log.writeLogFactura("PDF",idPersona,numFactura,"message.facturacion.error.generacion.factura.pdf"+ee.toString());
						salida=1;
	    	    		//Aunque nos ha fallado esta factura es posible que la siguiente, no.
	    	    		//POR LO TANTO no cazamos la excepcion
	    	    		//throw ee;
	    	    		existeAlgunErrorPdf = true;
	    			}
	    			
    				ClsLogging.writeFileLog("ALMACENAR " + idFactura + " >> VAMOS A VER SI ENVIARMOS: ENVIAR:" + bGenerarEnvios + " CORRECTO:" + (filePDF!=null),10);

		    	    	
	    			/***************    ENVIO FACTURAS *****************/
					if (bGenerarEnvios && filePDF!=null) {
						this.enviarProgramacionFactura(idPersona, institucion.toString(), idFactura, plantillaMail, numFactura, filePDF, log, salida, existeAlgunErrorEnvio, tx);
					}

    			} catch (SIGAException se){
    				throw se;
    				
    			} catch (Exception tot) {
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> CATCH GENERAL",10);
    				throw tot;
   				}
    			
				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE FACTURA OK ",10);
    		} // bucle
    		
    		// Si tiene log es que esta generando y por tanto hay que crear los documentos excel y el zip
    		if (log!=null && !existeAlgunErrorPdf && listaFicheros.size()>0){
    			File ficheroPdfFirmado = listaFicheros.get(0);
    			String ruta = ficheroPdfFirmado.getParentFile().getParentFile().getPath(); // �\Datos\SIGADES\ficheros\facturas_emitidas\" + idInstitucion + "\� + idSerieFacturacion + �_�  + idProgramacion
    			
    			String rutaFicheroInformeConfirmacion = ruta +  ClsConstants.FILE_SEP + facProgBean.getNombrefichero();
    			File ficheroInformeConfirmacion = new File(rutaFicheroInformeConfirmacion);
    			if (ficheroInformeConfirmacion.exists()) {
    				FacFicherosDescargaBean facFicherosDescargaBeanXls = new FacFicherosDescargaBean();
	    			facFicherosDescargaBeanXls.setFichero(ficheroInformeConfirmacion);
	    			facFicherosDescargaBeanXls.setFormatoDescarga(-1);     //Ponemos -4 para indicar que el nombre de este ficho en la descarga no se debe de modificar
	    			listaFicherosPDFDescarga.add(facFicherosDescargaBeanXls);
    				
    			} else {    			    		
		    		/**************  CREAMOS EL INFORME DE CONFIRMACION DE FACTURA QUE SE A�ADIR� AL ZIP DE FACTURAS EMITIDAS    ****************/
					AdmInformeAdm datosInforme = new AdmInformeAdm(this.usrbean);
					Hashtable<String,Object> hashWhere = new Hashtable<String,Object>();			
					UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDTIPOINFORME, "FACT");
					UtilidadesHash.set(hashWhere, AdmInformeBean.C_IDINSTITUCION, "0");
					
					ClsLogging.writeFileLog("### Inicio datosInforme CONFIRMACION",7);
					
					Vector<AdmInformeBean> v = datosInforme.select(hashWhere);
		
					if (v!=null && v.size()>0) {
						
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
						
						for (int dv = 0; dv < v.size(); dv++){				
							AdmInformeBean informe = v.get(dv);
							
							informe.setNombreSalida("CONFIRMACION_" + serieFacturacion + "_" + idProgramacion); 
							
							ClsLogging.writeFileLog("### Inicio generaci�n fichero excel CONFIRMACION",7);
							
							ArrayList<File> listaFicherosConfirmacion = InformePersonalizable.generarInformeXLS(informe, filtrosInforme, ruta, this.usrbean);
							
							ClsLogging.writeFileLog("### Fin generaci�n fichero excel CONFIRMACION",7);
			
							// Si no se generan los informes de confirmacion
							if (listaFicherosConfirmacion==null || listaFicherosConfirmacion.size()==0) {
								ClsLogging.writeFileLog("### Error al generar el informe de la confirmacion. Inicio creaci�n fichero log CONFIRMACION sin datos",7);
								throw new ClsExceptions("message.facturacion.error.fichero.nulo");						
							} 
							
							for (int i=0; i<listaFicherosConfirmacion.size(); i++) {
				    			File ficheroXls = listaFicherosConfirmacion.get(0);
				    			// listaFicheros.add(ficheroXls); Se debe mantener el fichero xls
				    			
				    			if (tx!=null) 
				    				tx.begin();				    			
				    			facProgBean.setNombrefichero(ficheroXls.getName());
				    			facProgAdm.update(facProgBean);
				    			if (tx!=null) 
				    				tx.commit();

				    			
				    			FacFicherosDescargaBean facFicherosDescargaBeanXls = new FacFicherosDescargaBean();
				    			facFicherosDescargaBeanXls.setFichero(ficheroXls);
				    			facFicherosDescargaBeanXls.setFormatoDescarga(-1);     //Ponemos -4 para indicar que el nombre de este ficho en la descarga no se debe de modificar
				    			listaFicherosPDFDescarga.add(facFicherosDescargaBeanXls);
				    		}
						}	
					}
		    		/********************************************************************************************************/
    			}

	    		// inc6666 - Si es correcto generamos el ZIP con los pdfs firmados y el documento excel de facturacion
				ruta = ficheroPdfFirmado.getParentFile().getParentFile().getParentFile().getPath() + File.separator; // "\Datos\SIGADES\ficheros\facturas_emitidas\" + idInstitucion + "\"
				this.doZip(ruta, serieFacturacion.toString() + "_" + idProgramacion.toString(), listaFicherosPDFDescarga);
    		}
    		
    		// Eliminacion de los pdfs firmados, el documento excel de la facturacion y su carpeta
			File directorio = null;
    		for (int i=0; i<listaFicheros.size(); i++) {
    			File ficheroPdfFirmado = listaFicheros.get(i);
    			directorio = ficheroPdfFirmado.getParentFile();
    			ficheroPdfFirmado.delete(); // Elimina los pdfs firmados y los documentos excel de la facturacion
    		}
    		if (directorio!=null && directorio.isDirectory() && directorio.list().length==0) {
				directorio.delete(); // borra el directorio de las firmas
			}
    				
		}catch (SIGAException e) {
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.getLiteral(userbean.getLanguage()),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			log.writeLogFactura("PDF/ENVIO","N/A","N/A",UtilidadesString.getMensajeIdioma(userbean,"message.facturacion.error.generacion.envio.factura")+e.getLiteral(userbean.getLanguage()));
			existeAlgunErrorEnvio = true;
			existeAlgunErrorPdf = true;
			
		}catch (Exception e) {
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.toString(),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			log.writeLogFactura("PDF/ENVIO","N/A","N/A",UtilidadesString.getMensajeIdioma(userbean,"message.facturacion.error.generacion.envio.factura")+e.toString());
			
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
		}else {
			salida = 2;
		}
		
		return salida;
	}
    
	/**
	 * 
	 * @param idPersona
	 * @param idInstitucion
	 * @param idFactura
	 * @param plantillaMail
	 * @param numeroFactura
	 * @param ficheroPdf
	 * @param log
	 * @param salida
	 * @param existeAlgunErrorEnvio
	 */
	private void enviarProgramacionFactura(
			String idPersona, 
			String idInstitucion, 
			String idFactura, 
			Integer plantillaMail, 
			String numeroFactura, 
			File ficheroPdf,
			SIGALogging log,
			int salida, 
			boolean existeAlgunErrorEnvio,
			UserTransaction tx
		) throws SIGAException {	  

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
			
			// TODO ��Obtiene una direccion y no la utiliza luego!! Deberiamos arreglarlo?
			if (direccion==null || direccion.size()==0) {
				 direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"3");// si no hay direccion preferente mail, buscamos la de correo
				 if (direccion==null || direccion.size()==0) {
				 	direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"2");// si no hay direccion de despacho, buscamos la de despacho
				 	if (direccion==null || direccion.size()==0) {
				 		direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"");// si no hay direccion de despacho, buscamos cualquier direcci�n.
				 		if (direccion==null || direccion.size()==0) {
				 			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> NO TIENE DIRECCION PREFERENTE "+preferencia,10);
	    					throw new ClsExceptions("No se ha encontrado direcci�n de la persona para el tipo de envio preferente: "+preferencia); 			
				 		}
				 	}
				 }
			}
			
			if(plantillaMail != null){
				enviosBean.setIdPlantillaEnvios(plantillaMail);
				// Creacion documentos
 				Documento documento = new Documento(ficheroPdf, "Factura " + ficheroPdf.getName());
				Vector<Documento> documentos = new Vector<Documento>(1);
				documentos.add(documento);
				
				/*************** INICIO TRANSACCION ***************/
				if (tx!=null)
					tx.begin();
				
				// Genera el envio:
				envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,documentos,null,null);
				if (tx!=null)
					tx.commit();
				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ENVIO GENERADO OK",10);
				/*************** FIN TRANSACCION ***************/
					
			}else{
				throw new SIGAException("messages.facturacion.almacenar.plantillasEnvioMal");		
			}

		} catch (SIGAException eee) {
			try { // Tratamiento rollback
				if (tx!=null && Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e2) {}
			
			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+eee.getLiteral(userbean.getLanguage()),10);
    		// ESCRIBO EN EL LOG
			log.writeLogFactura("ENVIO",idPersona,numeroFactura,"message.facturacion.error.envio.factura"+eee.getLiteral(userbean.getLanguage()));
			salida=2;
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no cazamos la excepcion
			//throw eee;
			existeAlgunErrorEnvio = true;
    		
		} catch (Exception eee) {
			try { // Tratamiento rollback
				if (tx!=null && Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e2) {}
    		
			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+eee.toString(),10);
    		// ESCRIBO EN EL LOG
			log.writeLogFactura("ENVIO",idPersona,numeroFactura,"message.facturacion.error.envio.factura"+eee.toString());
			salida=2;
			//Aunque nos ha fallado esta factura es posible que la siguiente, no.
    		//POR LO TANTO no cazamos la excepcion
			//throw eee;
			existeAlgunErrorEnvio = true;
		}
	}

	/**
     * Notas Jorge PT 118: Generacion de la facturacion rapida de compras y certificados (SolicitudCompraAction y SIGASolicitudesCertificadosAction):
     * - Productos y Servicios > Solicitudes > Compra/Subscripci�n
     * - Productos y Servicios > Gesti�n Solicitudes
     * - Certificados > Gesti�n de solicitudes
     * 
     * @param beanPeticionCompraSuscripcion
     * @param compras
     * @param beanSerieCandidata
     * @return
     * @throws ClsExceptions
     * @throws SIGAException
     */
    private FacFacturacionProgramadaBean procesarFacturacionRapidaCompras(PysPeticionCompraSuscripcionBean beanPeticionCompraSuscripcion, Vector<PysCompraBean> compras, FacSerieFacturacionBean beanSerieCandidata) throws ClsExceptions, SIGAException {
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
			Object[] param_in = new Object[6];
			param_in[0] = beanFacturacionProgramada.getIdInstitucion().toString();
        	param_in[1] = beanFacturacionProgramada.getIdSerieFacturacion().toString();
        	param_in[2] = beanFacturacionProgramada.getIdProgramacion().toString();
        	param_in[3] = this.usrbean.getLanguageInstitucion(); // Idioma
        	param_in[4] = beanPeticionCompraSuscripcion.getIdPeticion().toString();
        	param_in[5] = this.usrbean.getUserName();
        	
        	// Genera la facturacion
        	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?)}", 2, param_in);
        	
        	// Compruebo que ha finalizado correctamente
        	String[] codigosErrorFormato = {"-201", "-202", "-203", "-204"};
        	String codretorno = resultado[0];
        	if (Arrays.asList(codigosErrorFormato).contains(codretorno)){		
        		throw new SIGAException (resultado[1]);
        		
        	} else if (!codretorno.equals("0")){
        		throw new ClsExceptions ("Error al generar la Facturaci�n rapida: " + resultado[1]);
        	}
        	
        	// Desbloquea la facturacion programada
			if (!admFacturacionProgramada.updateDirect(beanFacturacionProgramada)) {
    	        throw new ClsExceptions("Error al actualizar la programacion: " + admFacturacionProgramada.getError());
    	    }
			
		} catch (SIGAException e) {
			throw e;				
    	    
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al realizar generacion de facturacion r�pida.");
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
		FacHistoricoFacturaAdm historicoFacturaAdm = new FacHistoricoFacturaAdm (this.usrbean);
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
			throw new ClsExceptions("Error al insertar la renegociaci�n: " + renegociacionAdm.getError()); 
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
		
		// CGP (20/10/2017 - R1709_0035) -- A�adimos al hist�rico de facturas. --

		boolean resultado = Boolean.FALSE;
		try{
			
			resultado=historicoFacturaAdm.insertarHistoricoFacParametros(String.valueOf(facturaBean.getIdInstitucion()),facturaBean.getIdFactura(), 7,null, 
							null, null,null,null, idNuevaRenegociacion, null, null);

			 if(!resultado){
					ClsLogging.writeFileLog("### No se ha insertado en el hist�rico de la facturaci�n ", 7);
			 }
		} catch (Exception e) {
			ClsLogging.writeFileLogError("@@@ ERROR: No se ha insertado el hist�rico de la facturaci�n",e,3);
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
		FacHistoricoFacturaAdm historicoFacturaAdm = new FacHistoricoFacturaAdm (userBean);
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
				
		Hashtable<String,Object> hTipoIva = new Hashtable<String,Object>();
		hTipoIva.put(PysTipoIvaBean.C_IDTIPOIVA, beanBancoInstitucion.getIdTipoIva());
		
		PysTipoIvaAdm admTipoIva = new PysTipoIvaAdm(userBean);
		Vector<?> vTipoIva = admTipoIva.selectByPK(hTipoIva);
		PysTipoIvaBean beanTipoIva = (PysTipoIvaBean) vTipoIva.firstElement();
		
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
				throw new ClsExceptions("Error porque no actualiza la l�nea de devoluciones");
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
			// CGP - (06/11/2017) INICIO A�adimos al hist�rico de la facturaci�n
	        
	    	boolean resultado = Boolean.FALSE;
			try{
				resultado=historicoFacturaAdm.insertarHistoricoFacParametros(String.valueOf(beanFacFactura.getIdInstitucion()),beanFacFactura.getIdFactura(), 9,null, 
						null, null,null,null, null, null, beanFacFactura.getIdFactura());
				 if(!resultado){
						ClsLogging.writeFileLog("### No se ha insertado en el hist�rico de la facturaci�n ", 7);
				 }
			} catch (Exception e) {
				ClsLogging.writeFileLogError("@@@ ERROR: No se ha insertado el hist�rico de la facturaci�n",e,3);
			}
		
	        // CGP (06/11/2017) FIN
			try {
		        //TODO: Si no se produce error regeneramos el pdf con la informaci�n de la factura
		        	InformeFactura infFactura = new InformeFactura(userBean);
		        	infFactura.generarPdfFacturaFirmada(null, beanFacFactura, Boolean.TRUE);
    		}catch(Exception e){}
			
			// JPT - Devoluciones 117 - Indico la fecha de devolucion
			beanFacFactura.setFechaEmision(fechaDevolucion);

			// JPT - Devoluciones 117 - Indico el estado de la factura calculado anteriormente
			beanFacFactura.setEstado(new Integer(ClsConstants.ESTADO_FACTURA_DEVUELTA)); // Inicialmente tiene estado devuelta
			
			// JPT - Devoluciones 117 - Pone en FAC_FACTURA.COMISIONIDFACTURA el identificador de la factura original  
			beanFacFactura.setComisionIdFactura(beanFacFactura.getIdFactura());
			String idFacturaAnterior=beanFacFactura.getIdFactura();  //Necesitamos esta variable porque sino perdemos el valor de la factura anterior para el hist�rico
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
			double importeIvaComision = UtilidadesNumero.redondea(beanBancoInstitucion.getComisionImporte() * Float.valueOf(beanTipoIva.getValor()) / 100, 2);						
			
			// JPT - Devoluciones 117 - Calcula los importes de la factura final con los importes de la comision
			beanFacFactura.setImpTotalPorPagar(beanFacFactura.getImpTotalPorPagar() + importeComision + importeIvaComision);														
			beanFacFactura.setImpTotalNeto(beanFacFactura.getImpTotalNeto() + importeComision);
			beanFacFactura.setImpTotalIva(beanFacFactura.getImpTotalIva() + importeIvaComision);
			beanFacFactura.setImpTotal(beanFacFactura.getImpTotal() + importeComision + importeIvaComision);						
			
			// JPT - Devoluciones 117 - Inserta la nueva factura
			if (!admFacFactura.insert(beanFacFactura)) {
				throw new ClsExceptions("Error porque no inserta la nueva factura con la comisi�n");
			}			
			// CGP - (06/11/2017) INICIO A�adimos al hist�rico de la facturaci�n
	        // Rellenamos los datos para insercci�n EMISI�N Y CONFIRMACI�N
	        FacHistoricoFacturaBean facHistoricoFacturaDevuelta;
	        facHistoricoFacturaDevuelta = Facturacion.rellenarHistoricoFactura(beanFacFactura,userBean);
			facHistoricoFacturaDevuelta.setEstado(7);
			facHistoricoFacturaDevuelta.setIdTipoAccion(1);
			facHistoricoFacturaDevuelta.setComisionIdFactura(idFacturaAnterior);
	      //Insertamos en el hist�rico.
			boolean resultadoFacturaDevuelta = Boolean.FALSE;
			try{
				resultadoFacturaDevuelta= historicoFacturaAdm.insertarHistoricoFacturacion(facHistoricoFacturaDevuelta);
				 //Ponemos la informaci�n de la confirmaci�n	
				 facHistoricoFacturaDevuelta.setEstado(9);
					facHistoricoFacturaDevuelta.setIdTipoAccion(2);
				resultadoFacturaDevuelta= historicoFacturaAdm.insertarHistoricoFacturacion(facHistoricoFacturaDevuelta);
				 if(!resultadoFacturaDevuelta){
						ClsLogging.writeFileLog("### No se ha insertado en el hist�rico de la facturaci�n ", 7);
				 }
			} catch (Exception e) {
				ClsLogging.writeFileLogError("@@@ ERROR: No se ha insertado el hist�rico de la facturaci�n",e,3);
			}
		
	        // CGP (06/11/2017) FIN
				
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
					throw new ClsExceptions("Error porque no inserta la nueva l�nea de factura de devoluciones con comisi�n");
				}		
				
				// JPT - Devoluciones 117 - Calculos para obtener el mayor numero de linea y orden
				if (beanFacLineaFactura.getNumeroLinea() > maximoNumeroLinea)
					maximoNumeroLinea = beanFacLineaFactura.getNumeroLinea();																		
				if (beanFacLineaFactura.getNumeroOrden() > maximoNumeroOrden)
					maximoNumeroOrden = beanFacLineaFactura.getNumeroOrden();
			}						
			
			// JPT - Devoluciones 117 - Calculo el campo CTAIVA
			String sCTAIVA = admTipoIva.obtenerCTAIVA(beanFacFactura.getIdInstitucion().toString(), beanBancoInstitucion.getIdTipoIva().toString());
			
			// JPT - Devoluciones 117 - Genero un objeto para la nueva linea con la comision
			beanFacLineaFactura = new FacLineaFacturaBean();
			beanFacLineaFactura.setIdInstitucion(beanFacFactura.getIdInstitucion());
			beanFacLineaFactura.setIdFactura(sNuevoIdFactura); // Asigna el nuevo identificador de factura a la linea de la factura							
			beanFacLineaFactura.setNumeroLinea(maximoNumeroLinea + 1); // Asigno el siguiente numero de linea
			beanFacLineaFactura.setNumeroOrden(maximoNumeroOrden + 1); // Asigno el siguient numero de orden
			beanFacLineaFactura.setCantidad(1); // Se indica que es una unidad de comisi�n
			beanFacLineaFactura.setImporteAnticipado(0.0); // Se indica que no tiene importe anticipado
			beanFacLineaFactura.setDescripcion(beanBancoInstitucion.getComisionDescripcion()); // Obtiene la descripcion de la comision del banco del acreedor
			beanFacLineaFactura.setPrecioUnitario(beanBancoInstitucion.getComisionImporte()); // Obtiene el importe de la comision del banco del acreedor
			beanFacLineaFactura.setIva(Float.valueOf(beanTipoIva.getValor())); // Obtiene el iva de la comision del banco del acreedor
			beanFacLineaFactura.setIdTipoIva(beanTipoIva.getIdTipoIva()); // Asigna el identificador del tipo de iva
			beanFacLineaFactura.setCtaProductoServicio(beanBancoInstitucion.getComisionCuentaContable()); // Obtiene la cuenta contable del banco del acreedor
			beanFacLineaFactura.setCtaIva(sCTAIVA);
			beanFacLineaFactura.setIdFormaPago(idFormaPago); // Indica la forma de pago de la factura	                  
			
			// JPT - Devoluciones 117 - Inserto la nueva linea de la factura
			if (!admLineaFactura.insert(beanFacLineaFactura)) {
				throw new ClsExceptions("Error porque no inserta la nueva l�nea de factura de devoluciones con comisi�n");
			}			
			
			// JPT: Devuelve los resultados de la nueva factura
			return beanFacFactura; 		
			
		} else {			
			// Se actualiza los campos CARGARCLIENTE y GASTOSDEVOLUCION
			lineaDevolucion.setCargarCliente("N"); // Diferencia con lo de arriba
			
			if (!admLDDB.update(lineaDevolucion)) {
				throw new ClsExceptions("Error porque no actualiza la l�nea de devoluciones");
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
		String[] codigosErrorFormato = {"-201", "-202", "-203", "-204"};
		
		ClsLogging.writeFileLog("### Inicio generarFicheroPrevisiones instituci�n: " + idInstitucion, 7);
		
		UserTransaction tx = this.usrbean.getTransactionPesada(); 
		FacFacturacionProgramadaAdm admFacturacionProgramada = new FacFacturacionProgramadaAdm(this.usrbean);		
		
		String [] claves = {FacFacturacionProgramadaBean.C_IDINSTITUCION, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, FacFacturacionProgramadaBean.C_IDPROGRAMACION};
		String nombreFichero = "GENERACION_" + idSerieFacturacion + "_" + idProgramacion;    
		Hashtable<String,Object> hashEstado = new Hashtable<String,Object>();
		UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDINSTITUCION, idInstitucion);
    	UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDPROGRAMACION, idProgramacion);
    	UtilidadesHash.set(hashEstado, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION, idSerieFacturacion);
    	
    	String resultado[] = new String[2];
		try {			
			ClsLogging.writeFileLog("### Procesando GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + ")", 7);
			
			// Carga los parametros			
			Object[] param_in = new Object[6];
			param_in[0] = idInstitucion;
        	param_in[1] = idSerieFacturacion;
        	param_in[2] = idProgramacion;
        	param_in[3] = this.usrbean.getLanguageInstitucion(); // Idioma
        	param_in[4] = ""; // IdPeticion
        	param_in[5] = this.usrbean.getUserName();
        	
        	try {
        		ClsLogging.writeFileLog("### Inicio GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + ")",7);  
        		tx.begin();
        		resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION.GENERACIONFACTURACION(?,?,?,?,?,?,?,?)}", 2, param_in);

        	} catch (Exception e) {        			
			    if (e.getMessage().indexOf("TimedOutException")!=-1 || e.getMessage().indexOf("timed out")!=-1) {
			    	ClsLogging.writeFileLog("### Fin GENERACION - ERROR TIMEOUT (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), excepcion " + e.getMessage(), 7);
			        throw new ClsExceptions("TimedOutException al generar una Facturacion (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "; CodigoError:" + e.getMessage() + ")");
			    
			    }else{
        			ClsLogging.writeFileLog("### Fin GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), excepcion " + e.getMessage(), 7);	     		
        			throw new ClsExceptions(UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"facturacion.nuevaPrevisionFacturacion.mensaje.procesoPlSQLERROR") +
        					"(Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "; CodigoError:" + e.getMessage() + ")");
			    }
			}

			String codretorno = resultado[0];
						
        	if (Arrays.asList(codigosErrorFormato).contains(codretorno)) {
        		ClsLogging.writeFileLog("### Fin GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), finalizada con errores", 7);				
				throw new ClsExceptions(resultado[1]);
			
        	} else if (!codretorno.equals("0")) {				
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
					// Si la previsi�n est� vac�a
					if (nameFile == null || nameFile.length() == 0) {
						ClsLogging.writeFileLog("### Inicio creaci�n fichero log GENERACION sin datos", 7);
						controlarEstadoErrorGeneracion(tx, admFacturacionProgramada, claves, hashEstado, nombreFichero, FacEstadoConfirmFactBean.GENERADA, null);
						ClsLogging.writeFileLog("### Fin creaci�n fichero log GENERACION sin datos", 7);
	
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
			ClsLogging.writeFileLog("### ERROR GLOBAL GENERACION (Serie:" + idSerieFacturacion + "; IdProgramacion:" + idProgramacion + "), empezamos la gestion del error", 7);
			try { // Tratamiento rollback
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e2) {}		
			
			// Le cambio el estado a error
			try { 
				String sMensaje = null;
				if (resultado[0]!=null && Arrays.asList(codigosErrorFormato).contains(resultado[0])) {
					sMensaje = resultado[1];
				} else if (e.getMessage().indexOf("TimedOutException")!=-1 || e.getMessage().indexOf("timed out")!=-1) {
					sMensaje = UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"messages.error.generacionFacturacion.timeout");
				} else {
					sMensaje = UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"messages.error.generacionFacturacion.general");
				}				
				
				controlarEstadoErrorGeneracion(tx,admFacturacionProgramada,claves,hashEstado,nombreFichero, FacEstadoConfirmFactBean.ERROR_GENERACION, sMensaje);	
				
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
	
	public File generarZIPLogYFacturacion(File ficheroLog, File ficheroTraspasos, String idInstitucion, String idSerieFacturacion, String idProgramacion, String rutaFichero) throws Exception
	{
		File fichero = null;
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		
		String nombreFichero = "TRASPASO_Y_GENERACION_" + idSerieFacturacion + "_" + idProgramacion;
		try
		{
			if (ficheroLog.exists() && ficheroTraspasos.exists())
			{
				ArrayList<FacFicherosDescargaBean> lista = new ArrayList<FacFicherosDescargaBean>();
				FacFicherosDescargaBean facFicherosDescargaBean1 = new FacFicherosDescargaBean();
				facFicherosDescargaBean1.setFormatoDescarga(-1);
				facFicherosDescargaBean1.setFichero(ficheroLog);
				facFicherosDescargaBean1.setNombreFacturaFichero("");
				lista.add(facFicherosDescargaBean1);
				
				FacFicherosDescargaBean facFicherosDescargaBean2 = new FacFicherosDescargaBean();
				facFicherosDescargaBean2.setFormatoDescarga(-1);
				facFicherosDescargaBean2.setFichero(ficheroTraspasos);
				facFicherosDescargaBean2.setNombreFacturaFichero("");
				lista.add(facFicherosDescargaBean2);
				
				this.doZip(rutaFichero, nombreFichero, lista);
				
				fichero = new File(rutaFichero + nombreFichero + ".zip");
			}
		} catch (Exception e) {
			throw e;
		}
		
		return fichero;
	}
	
	
	//SOBRA PORQUE YA NO SE CREA EL FICHERO DE LOG DE TRASPASOS DESDE AQU�: 
	/*public String generarFicheroTraspasos(String idInstitucion, String idSerieFacturacion, String idProgramacion) throws Exception
	{
		String nameFile = null;
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sRutaJava = rp.returnProperty("facturacion.directorioPrevisionesJava");
		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
		sRutaJava = sRutaFisicaJava + File.separator + sRutaJava + File.separator + idInstitucion;
		try
		{
			nameFile = "TRASPASOFACTURAS_" + idSerieFacturacion + "_" + idProgramacion + ".xls";
			
		} catch (Exception e) {
			throw e;
		}
		
		return nameFile;
	}*/
	
	public String generarInformeGeneracion(String idInstitucion, String idSerieFacturacion, String idProgramacion) throws Exception {
		String nameFile = null;
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
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

					ClsLogging.writeFileLog("### Inicio generaci�n fichero excel GENERACION", 7);

					ArrayList<File> fichPrev = InformePersonalizable.generarInformeXLS(beanInforme, filtrosInforme, sRutaJava, this.usrbean);

					ClsLogging.writeFileLog("### Fin generaci�n fichero excel GENERACION", 7);

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
	private void controlarEstadoErrorGeneracion(UserTransaction tx, FacFacturacionProgramadaAdm admProg,String [] claves,Hashtable<String,Object> hashEstado, String nombreFichero, Integer estadoFin, String sMensaje) throws Exception {
		try {
			ClsLogging.writeFileLog("### GESTION ERROR GENERACION  ####", 7);
			String [] campos = {FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION,FacFacturacionProgramadaBean.C_LOGERROR};
			UtilidadesHash.set(hashEstado,FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION, estadoFin); //ESTADO ERROR GENERAION 
			UtilidadesHash.set(hashEstado,FacFacturacionProgramadaBean.C_LOGERROR,"LOG_FAC_" + nombreFichero + ".log.xls");
			
			ClsLogging.writeFileLog("El estado de la transaccion es: "+tx.getStatus(), 7);
			if (tx == null || tx.getStatus() != Status.STATUS_ACTIVE) {
				ClsLogging.writeFileLog("Dentro del if antes de crear la transaccion", 7);
				tx = this.usrbean.getTransaction();
				ClsLogging.writeFileLog("Dentro del if antes del begin de la transaccion", 7);
				tx.begin();
			}
			
			ClsLogging.writeFileLog("### CAMBIANDO A ESTADO: "+estadoFin,7);
			if (!admProg.updateDirect(hashEstado,claves,campos)) {
			      throw new ClsExceptions("Error al actualizar el estado de la generaci�n. finalizada con errores.");
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
			if (sMensaje!=null && !sMensaje.equals("")) {
				out.write(sMensaje);
			} else {
				out.write("No se ha podido facturar nada. Compruebe la configuracion y el periodo indicado\t");
			}
			out.close();			
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
	
	/**
	 * Notas Jorge PT 118:
     * - Productos y Servicios > Solicitudes > Compra/Subscripci�n (facturacion rapida)
     * - Productos y Servicios > Gesti�n Solicitudes (facturacion rapida)
     * - Certificados > Gesti�n de solicitudes (facturacion rapida)
	 *  
	 * Facturacion rapida de productos y certificados
	 * 
	 * @param idInstitucion
	 * @param idPeticion
	 * @param idSerieSeleccionada
	 * @param idSolicitudCertificado
	 * @param request
	 * @throws Exception
	 */
	public void facturacionRapidaProductosCertificados(String idInstitucion, String idPeticion, String idSerieSeleccionada, String idSolicitudCertificado, HttpServletRequest request) throws Exception {	
	    UserTransaction tx = null;
	    try {
			UsrBean usr = this.usrbean;	
			tx = usr.getTransaction();
			
		    // administradores
			PysCompraAdm admCompra = new PysCompraAdm(usr);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usr);
			PysPeticionCompraSuscripcionAdm admPeticionCompraSuscripcion = new PysPeticionCompraSuscripcionAdm(usr);		    
		    FacFacturaAdm admFactura = new FacFacturaAdm(usr);
		    InformeFactura informe = new InformeFactura(usr);	
		    CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
		    
		    // Vectores necesarios para el proceso
		    Vector<PysCompraBean> vCompras = new Vector<PysCompraBean>();
		    Vector<Hashtable<String,Object>> vFacturas = new Vector<Hashtable<String,Object>>();	
		    
		    if (idSolicitudCertificado!=null) { // CERTIFICADO
		    	
		    	// Obtiene las facturas de una solicitud de certificado
	        	vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, null, idSolicitudCertificado);
	        	
	        	if (vFacturas==null ||  vFacturas.size()==0) { // No esta facturado => vCompras => Tx
	        		// BLOQUEAMOS LA TABLA DE COMPRAS EN ESTA TRANSACCI�N PARA CONTROLAR LAS PETICIONES SIMULTANEAS		    		
				    tx.begin();
				    admCompra.lockTable();
				    
				    // Obtengo la peticion de compra
				    PysCompraBean beanCompra = admCompra.obtenerCompraCertificado(idInstitucion, idSolicitudCertificado);
				    
					Hashtable<String,Object> hTipoIva = new Hashtable<String,Object>();
					hTipoIva.put(PysTipoIvaBean.C_IDTIPOIVA, beanCompra.getIdTipoIva());
					
					PysTipoIvaAdm admTipoIva = new PysTipoIvaAdm(usr);
					Vector<?> vTipoIva = admTipoIva.selectByPK(hTipoIva);
					PysTipoIvaBean beanTipoIva = (PysTipoIvaBean) vTipoIva.firstElement();	
				    
				    // ANTES DE FACTURAR APUNTO EL IMPORTE TOTAL COMO IMPORTE ANTICIPADO
				    double importe = UtilidadesNumero.redondea(beanCompra.getCantidad().intValue() * beanCompra.getImporteUnitario().doubleValue() * (1 + (Float.valueOf(beanTipoIva.getValor()).doubleValue() / 100)), 2);
				    beanCompra.setImporteAnticipado(new Double(importe));
				    if (!admCompra.updateDirect(beanCompra)) {
				        throw new ClsExceptions("Error al actualizar el importe anticipado: " + admCompra.getError());
				    }
				    
				    vCompras.add(beanCompra);
				    
				    // Obtengo la peticion del certificado
				    idPeticion = beanCompra.getIdPeticion().toString();
	        		
	        	} else { // Esta facturado => vFacturas => No Tx
	        		
	        		// Obtengo los datos de la factura
	        		Hashtable<String,Object> hFactura = vFacturas.get(0);
	        		
	        		// Obtengo la peticion del certificado
	        		idPeticion = UtilidadesHash.getString(hFactura, PysCompraBean.C_IDPETICION);
	        	} 
		    	
	        } else { // PRODUCTOS NO CERTIFICADOS
	        	
	        	// Obtiene las facturas de una peticion de una solicitud de compra de productos
	        	vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, idPeticion, null);
	        	
	        	if (vFacturas==null ||  vFacturas.size()==0) { // No esta facturado => vCompras => Tx
	        		
	        		// BLOQUEAMOS LA TABLA DE COMPRAS EN ESTA TRANSACCI�N PARA CONTROLAR LAS PETICIONES SIMULTANEAS	        		
	        		tx.begin();
	        		admCompra.lockTable();
	        		
	        		vCompras = admCompra.obtenerComprasPeticion(idInstitucion, idPeticion);    			   
		        	if (vCompras.size()==0) {
		        		throw new SIGAException("messages.facturacionRapidaCompra.noElementosFacturables");
		        	}
		        	
	        	} // else {} // Esta facturado => vFacturas => No Tx
	        }
	        
	        if (vFacturas==null || vFacturas.size()==0) { // Compruebo si no tiene facturas asociadas a la peticion => vCompras => Tx
	        		        	
	        	// Obtiene la serie candidata
			    FacSerieFacturacionBean beanSerieCandidata = null;
	        	if (idSerieSeleccionada==null || idSerieSeleccionada.equals("")) {
				    Vector<?> series =  admSerieFacturacion.obtenerSeriesAdecuadas(vCompras);
				    if (series==null || series.size()!=1) {
				    	// LIBERAMOS EL BLOQUEO DE LAS TABLAS Y LA TRANSACCI�N
				        throw new SIGAException("messages.facturacionRapidaCompra.noSerieAdecuada");
				        
				    } else if (series.size()==1) {
				    	beanSerieCandidata = (FacSerieFacturacionBean)series.get(0);
				    }
				    
		        } else { // Se ha seleccionado una serie			        			           
			        Hashtable<String,String> hSerieFacturacion = new Hashtable<String,String>();
			        hSerieFacturacion.put("IDINSTITUCION", idInstitucion);
			        hSerieFacturacion.put("IDSERIEFACTURACION", idSerieSeleccionada);
			        
		            Vector<?> vSerieFacturacion = admSerieFacturacion.selectByPK(hSerieFacturacion);
		            if (vSerieFacturacion!=null && vSerieFacturacion.size()>0) {
		            	beanSerieCandidata = (FacSerieFacturacionBean)vSerieFacturacion.get(0);
		            }
		        }
	        	
	        	// Obtengo la peticion de compra
			    Hashtable<String, Object> hPeticionCompraSuscripcion = new Hashtable<String, Object>(); 
			    hPeticionCompraSuscripcion.put("IDINSTITUCION",idInstitucion);
			    hPeticionCompraSuscripcion.put("IDPETICION",idPeticion);		
			    
			    Vector<PysPeticionCompraSuscripcionBean> vPeticionCompraSuscripcion = admPeticionCompraSuscripcion.selectByPK(hPeticionCompraSuscripcion);		    
			    PysPeticionCompraSuscripcionBean beanPeticionCompraSuscripcion = null;
			    if (vPeticionCompraSuscripcion!=null && vPeticionCompraSuscripcion.size()>0) {
			    	beanPeticionCompraSuscripcion = vPeticionCompraSuscripcion.get(0);
			    }		    			
			    
			    if (idSolicitudCertificado!=null) { // CERTIFICADO
			    	if (beanPeticionCompraSuscripcion.getIdEstadoPeticion().equals(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE))) { // Esta en estado pendiente. Hay que aprobarla
			    		beanPeticionCompraSuscripcion.setIdEstadoPeticion(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PROCESADA));
					    if (!admPeticionCompraSuscripcion.update(beanPeticionCompraSuscripcion)) {
					        throw new ClsExceptions("Error al actualizar el estado de la peticion de compra del certificado: " + admPeticionCompraSuscripcion.getError());
					    }						
			    	}
			    	
			    } else {
			    	
			    	// LOCALIZO LAS COMPRAS (SI NO EXISTEN LAS GENERO)
			    	if (beanPeticionCompraSuscripcion.getIdEstadoPeticion().equals(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_BAJA))) { // Esta en estado baja		        
			    		throw new SIGAException("messages.facturacionRapidaCompra.estadoBaja");		   
			        
			    	} else if (beanPeticionCompraSuscripcion.getIdEstadoPeticion().equals(new Integer(ClsConstants.ESTADO_PETICION_COMPRA_PENDIENTE))) { // Esta en estado pendiente. Hay que aprobarla		        
			    		beanPeticionCompraSuscripcion = admPeticionCompraSuscripcion.aprobarCompras(vCompras);
			    	}
			    }

			    // FACTURACION RAPIDA DESDE SERIE CANDIDATA (GENERACION)
	        	FacFacturacionProgramadaBean programacion = this.procesarFacturacionRapidaCompras(beanPeticionCompraSuscripcion, vCompras, beanSerieCandidata);
	       
	        	
	        	// CONFIRMACION RAPIDA (en este caso la transacci�n se gestiona dentro la transaccion)
			    this.confirmarProgramacionFactura(programacion, request, false, null, false, false, 0, true);
			  	
			    
			    if (idSolicitudCertificado!=null) { // CERTIFICADO
			    	
			    	// Obtiene las facturas de una solicitud de certificado
		        	vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, null, idSolicitudCertificado);
			    	
			    } else { // PRODUCTOS NO CERTIFICADOS
			    	
			    	// Obtiene las facturas de una peticion de una solicitud de compra de productos
			    	vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, idPeticion, null);
			    }
			    
			    if (Status.STATUS_ACTIVE  == tx.getStatus())
	        		tx.commit();			    
			    
	        } else {} // Esta facturado => vFacturas => No Tx
				
	        // GENERAR FICHERO: Siempre elimina el zip con los pdfs firmads o el pdf firmado 	 
	        try{
	        	File fichero = informe.generarInformeFacturacionRapida(request, idInstitucion, idPeticion, vFacturas);
	        	
	        	if (fichero == null) {
					throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");
				}
	        	
	    		// DESCARGAR FICHERO
				String nombreColegiado ="";
				if(vFacturas != null &&  vFacturas.size()>0){
				Hashtable<String,Object> obj = vFacturas.get(0);
				String idPersona = (String)obj.get("IDPERSONA");
			    nombreColegiado ="";
				if(idPersona != null && !"".equalsIgnoreCase(idPersona)){
					 nombreColegiado = personaAdm.obtenerNombreApellidos(idPersona);
					if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
						nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
					}else{
						nombreColegiado="";
					}
				}
				}
				
				int inicio = fichero.getName().indexOf(".zip");
				 //Si se llama a este m�todo desde el demonio de: acciones masivas, la request viene null
			    if(request != null){
				//Si es -1 no es un fichero zip
					if(inicio == -1){
						String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + vFacturas.get(0).get("IDSERIEFACTURACION") +
								" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " + vFacturas.get(0).get("IDINSTITUCION");
									
						Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
												
						String[] nombreFicherosarrays;
						if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
							FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
							switch (beanSerieFacturacion.getIdNombreDescargaPDF()) {
							case 1:
									 nombreFicherosarrays = fichero.getName().split("-",2);
									 request.setAttribute("nombreFichero",nombreFicherosarrays[1]);
								break;
							case 2:
								//Quitamos la extensi�n y a�adimos el nombre m�s la extensi�n
								String[] separacionExtensionDelFichero = fichero.getName().split(Pattern.quote("."));
								String[] separacionNombreColegiado = nombreColegiado.split("-");
								nombreFicherosarrays = separacionExtensionDelFichero[0].split("-",2);
								
								request.setAttribute("nombreFichero",nombreFicherosarrays[1] + "-"+separacionNombreColegiado[0]+"."+separacionExtensionDelFichero[1]);
								break;
							case 3:
								nombreFicherosarrays = fichero.getName().split("-",2);
								request.setAttribute("nombreFichero",nombreColegiado+ nombreFicherosarrays[1]);
								break;
		
							default:
								nombreFicherosarrays = fichero.getName().split("-",2);
								request.setAttribute("nombreFichero",nombreColegiado+  nombreFicherosarrays[1]);
								break;
							}
						}else{
							nombreFicherosarrays = fichero.getName().split("-",2);
							request.setAttribute("nombreFichero",nombreColegiado+ nombreFicherosarrays[1]);
						}
					}
					String path =  UtilidadesString.replaceAllIgnoreCase( fichero.getPath(), "\\", "/");
					request.setAttribute("rutaFichero", path);
					request.setAttribute("generacionOK", "OK");
			    }
	        	
	        } catch (SIGAException se) {
	       	
				throw se;
				
				
		    } catch (ClsExceptions se) {
		       	
				throw se;
				
	        }

	    }catch (SIGAException e) { 
			try { // Tratamiento rollback
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e3) {}	
	    	
				throw e; 	
	    }catch (ClsExceptions e) { 
			try { // Tratamiento rollback
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e3) {}	
	    	
				throw e; 	
	    } catch (Exception e) { 
			try { // Tratamiento rollback
				if (Status.STATUS_ACTIVE  == tx.getStatus()){
					tx.rollback();
				}
			} catch (Exception e3) {}	
	    	
			if (e instanceof ArrayIndexOutOfBoundsException)
				throw new SIGAException("messages.facturacionRapida.error.Array"); 
			else
				throw e; 
		}			
	}
	
	public static FacHistoricoFacturaBean rellenarHistoricoFactura(FacFacturaBean facturaBean, UsrBean usr){
		FacHistoricoFacturaBean facHistoricofactura = new FacHistoricoFacturaBean();
		
		if(facturaBean.getIdInstitucion() != null)
			facHistoricofactura.setIdInstitucion(facturaBean.getIdInstitucion());

		if(facturaBean.getIdFactura() != null)
			facHistoricofactura.setIdFactura(facturaBean.getIdFactura());

		facHistoricofactura.setFechaModificacion("SYSDATE");
		facHistoricofactura.setUsuModificacion(Integer.valueOf(usr.getUserName()));
		
		
		if(facturaBean.getIdFormaPago() != null)
			facHistoricofactura.setIdFormaPago(Integer.valueOf(facturaBean.getIdFormaPago()));
		if(facturaBean.getIdPersona() != null)
			facHistoricofactura.setIdPersona(facturaBean.getIdPersona().longValue());
		if(facturaBean.getIdCuenta() != null)
			facHistoricofactura.setIdCuenta(facturaBean.getIdCuenta());
		if(facturaBean.getIdPersonaDeudor() != null)
			facHistoricofactura.setIdPersonaDeudor(facturaBean.getIdPersonaDeudor().longValue());
		if(facturaBean.getIdCuentaDeudor() != null)
			facHistoricofactura.setIdCuentaDeudor(facturaBean.getIdCuentaDeudor());
		if(facturaBean.getImpTotalAnticipado() != null)
			facHistoricofactura.setImpTotalAnticipado(facturaBean.getImpTotalAnticipado());
		if(facturaBean.getImpTotalPagadoPorCaja() != null)
			facHistoricofactura.setImpTotalPagadoPorCaja(facturaBean.getImpTotalPagadoPorCaja());
		if(facturaBean.getImpTotalPagadoSoloCaja() != null)	
			facHistoricofactura.setImpTotalPagadoSoloCaja(facturaBean.getImpTotalPagadoSoloCaja());
		if(facturaBean.getImpTotalPagadoSoloTarjeta() != null)	
			facHistoricofactura.setImpTotalPagadoSoloTarjeta(facturaBean.getImpTotalPagadoSoloTarjeta());
		if(facturaBean.getImpTotalPagadoPorBanco() != null)	
			facHistoricofactura.setImpTotalPagadoPorBanco(facturaBean.getImpTotalPagadoPorBanco());
		if(facturaBean.getImpTotalPagado() != null)	
			facHistoricofactura.setImpTotalPagado(facturaBean.getImpTotalPagado());
		if(facturaBean.getImpTotalPorPagar() != null)	
			facHistoricofactura.setImpTotalPorPagar(facturaBean.getImpTotalPorPagar());
		if(facturaBean.getImpTotalCompensado() != null)	
			facHistoricofactura.setImpTotalCompensado(facturaBean.getImpTotalCompensado());
	    
		return facHistoricofactura;
	}
}