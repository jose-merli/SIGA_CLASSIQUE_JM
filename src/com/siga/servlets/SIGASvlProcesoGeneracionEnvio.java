package com.siga.servlets;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.EnvDestProgramInformesAdm;
import com.siga.beans.EnvDestProgramInformesBean;
import com.siga.beans.EnvInformesGenericosAdm;
import com.siga.beans.EnvProgramIRPFAdm;
import com.siga.beans.EnvProgramIRPFBean;
import com.siga.beans.EnvProgramInformesAdm;
import com.siga.beans.EnvProgramInformesBean;
import com.siga.beans.EnvProgramPagosAdm;
import com.siga.beans.EnvProgramPagosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.informes.InformeCertificadoIRPF;
import com.siga.informes.InformeColegiadosPagos;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoGeneracionEnvio extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sNombreProceso = "ProcesoGeneracionEnvio";

    
// version de una sola llamada


    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        //ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    	//ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX (LLAMADA DIRECTA).", 3);


    	//ClsLogging.writeFileLogWithoutSession(" ", 3);
        //ClsLogging.writeFileLogWithoutSession(" - INVOCANDO... >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);

   		try
   		{
			EnvProgramIRPFAdm admProgramiRPF = new EnvProgramIRPFAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor
            // Sacamos los datos de los datos programados pendientes(ClsConstants.DB_FALSE) de
   			//todas las instituciones(null)
   			Vector vCertificadosIRPFProgramados = admProgramiRPF.getCertificadosIRPFProgramados(ClsConstants.DB_FALSE, null);
   			
   			InformeCertificadoIRPF informeCertificadoIRPF = null;
   			if (vCertificadosIRPFProgramados!=null && vCertificadosIRPFProgramados.size()>0)
   			{
   				ClsLogging.writeFileLogWithoutSession(" ---------- ENVIOS PROGRAMADOS DE CERTIFICADOS IRPF PENDIENTES:"+vCertificadosIRPFProgramados.size(), 3);
   				EnvProgramIRPFBean programIRPFBean = null;
   				informeCertificadoIRPF = new InformeCertificadoIRPF();
   				for (int i=0; i<vCertificadosIRPFProgramados.size(); i++)
   				{
   					
   					try {
   						programIRPFBean = (EnvProgramIRPFBean)vCertificadosIRPFProgramados.get(i);
   	   					UsrBean usr = UsrBean.UsrBeanAutomatico(programIRPFBean.getIdInstitucion().toString());
   	   					informeCertificadoIRPF.enviarCertificadoIRPFColegiado(usr, programIRPFBean, programIRPFBean.getEnvioProgramado());
   	   					
   	   				programIRPFBean.setOriginalHash(admProgramiRPF.beanToHashTable(programIRPFBean));
   	   				programIRPFBean.setEstado(ClsConstants.DB_TRUE);
   	   					
   	   				admProgramiRPF.update(programIRPFBean);
   	   					
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- OK ENVIO DE CERTIFICADOS IRPF PENDIENTES IDPERSONA: "+programIRPFBean.getIdPersona(), 3);

					} catch (Exception e) {
						if(programIRPFBean != null && programIRPFBean.getIdInstitucion()!=null){
							ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES IDINSTITUCION: "+programIRPFBean.getIdInstitucion(), 3);
							if(programIRPFBean.getIdPersona()!=null)
								ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES IDPERSONA: "+programIRPFBean.getIdPersona(), 3);
						}
						else
							ClsLogging.writeFileLogWithoutSession(" ---------- ERROR ENVIO DE CERTIFICADOS IRPF PENDIENTES.", 3);
					}
   				}
   			}
   	        
   			ClsLogging.writeFileLogWithoutSession(" FIN ENVIOS PROGRAMADOS DE CERTIFICADOS IRPF A COLEGIADOS ", 3);
   			
   			
   			ClsLogging.writeFileLogWithoutSession(" ---------- INICIO ENVIOS PROGRAMADOS DE INFORMES GENERICOS ", 3);
   			EnvProgramInformesAdm admProgramInfGenericos = new EnvProgramInformesAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor
   			// Sacamos los datos de los datos programados pendientes(ClsConstants.DB_FALSE) de
   			//todas las instituciones(null)
   			Vector vInfGenericosProgramados = admProgramInfGenericos.getInformesGenericosProgramados(ClsConstants.DB_FALSE, null);
   			
   			EnvioInformesGenericos envioInformeGenerico = new EnvioInformesGenericos();
   			if (vInfGenericosProgramados!=null && vInfGenericosProgramados.size()>0)
   			{
   				ClsLogging.writeFileLogWithoutSession(" ---------- ENVIOS PROGRAMADOS DE INFORMES GENERICOS PENDIENTES:"+vInfGenericosProgramados.size(), 3);
   				EnvProgramInformesBean programInfGenericoBean =  null;
   				EnvDestProgramInformesAdm admDestProgram =   new EnvDestProgramInformesAdm(new UsrBean());
   				EnvInformesGenericosAdm admInformesGenericos =   new EnvInformesGenericosAdm(new UsrBean());
   				for (int i=0; i<vInfGenericosProgramados.size(); i++)
   				{

   					try {
   						programInfGenericoBean = (EnvProgramInformesBean)vInfGenericosProgramados.get(i);
   						
   						Integer idTipoEnvio = programInfGenericoBean.getEnvioProgramado().getIdTipoEnvios();
   						if(idTipoEnvio.toString().equals(EnvTipoEnviosAdm.K_CORREO_ORDINARIO)){
   							
   							UsrBean usr = UsrBean.UsrBeanAutomatico(programInfGenericoBean.getIdInstitucion().toString());
	   						Vector vDestinatarios = admDestProgram.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
	   						Vector vPlantillas = admInformesGenericos.getPlantillasInformesGenericosProgramados(programInfGenericoBean);
	   						
							try {
								envioInformeGenerico.enviarInformeGenericoOrdinario(usr, vDestinatarios,  programInfGenericoBean, vPlantillas, programInfGenericoBean.getEnvioProgramado());
							} catch (Exception e) {
								//if(destProgramInfBean.getIdPersona()!=null)
		   						ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES CORREO ORDINARIO: ", 3);
							}
	   							
	   						
	   						
	   						
	   						
	   						//}
   							
   							
   						}else{							
   							
   							UsrBean usr = UsrBean.UsrBeanAutomatico(programInfGenericoBean.getIdInstitucion().toString());
	   						Vector vDestinatarios = admDestProgram.getDestinatariosInformesGenericosProgramados(programInfGenericoBean);
	   						Vector vPlantillas = admInformesGenericos.getPlantillasInformesGenericosProgramados(programInfGenericoBean);
	   						for (int j = 0; j < vDestinatarios.size(); j++) {
	   							EnvDestProgramInformesBean destProgramInfBean = (EnvDestProgramInformesBean)vDestinatarios.get(j);
	   							try {
	   								envioInformeGenerico.enviarInformeGenerico(usr, destProgramInfBean,  programInfGenericoBean, vPlantillas, programInfGenericoBean.getEnvioProgramado());
								} catch (Exception e) {
									if(destProgramInfBean.getIdPersona()!=null)
		   								ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES IDPERSONA: "+destProgramInfBean.getIdPersona() + " "+e.toString(), 3);
								}
	   						}
							
						}
   						
   						

   						programInfGenericoBean.setOriginalHash(admProgramInfGenericos.beanToHashTable(programInfGenericoBean));
   						programInfGenericoBean.setEstado(ClsConstants.DB_TRUE);

   						admProgramInfGenericos.update(programInfGenericoBean);

   						ClsLogging.writeFileLogWithoutSession(" ---------- OK ENVIO DE INFORMES GENERICOS PENDIENTES. TIPO DE INFORME : "+programInfGenericoBean.getIdTipoInforme(), 3);

   					} catch (Exception e) {
   						if(programInfGenericoBean != null && programInfGenericoBean.getIdInstitucion()!=null){
   							ClsLogging.writeFileLogWithoutSession(" ----------ERROR ENVIO DE INFORMES GENERICOS PENDIENTES. TIPO DE INFORME: "+programInfGenericoBean.getIdTipoInforme() + " "+e.toString(), 3);
   							
   						}
   						else
   							ClsLogging.writeFileLogWithoutSession(" ---------- ERROR ENVIO DE INFORMES GENERICOS PENDIENTESS." + " "+e.toString(), 3);
   					}
   				}
   			}
   	        
   			ClsLogging.writeFileLogWithoutSession(" FIN ENVIOS PROGRAMADOS DE INFORMES GENERICOS ", 3);
   			
   			
   			
   			
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("FIN PROCESO AUTOMATICO DE ENVIOS");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   			
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("ERROR en proceso automático rápido");
   	        out.close();

   			
   			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString() , 3);
   		    e.printStackTrace();
   		}

    }

}


