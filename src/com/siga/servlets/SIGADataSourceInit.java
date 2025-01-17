package com.siga.servlets;

// VERSIONES:
// raul.ggonzalez 28-12-2004 Se anhade la inicializacion de visibilidad al init de la clase
// Luis Miguel S�nchez PI�A - 24/02/2005 - Se apanhan un poco los mensajes.

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngProperties;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.general.CenVisibilidad;

import es.satec.businessManager.BusinessManager;


public class SIGADataSourceInit extends ActionServlet {
	private static final long serialVersionUID = 1770505503324002123L;
	static ModuleConfig moduleConfig = null;
	
    public SIGADataSourceInit() { }

    public void init() throws javax.servlet.ServletException {
    	SIGAReferences.initialize(this.getServletContext());
    	
    	long seg_first =0;
		long seg_last =0;
		seg_first=System.currentTimeMillis();
		
		//ClsLogging.writeFileLogWithoutSession("");
        //ClsLogging.writeFileLogWithoutSession("Inicializando DataSource(s)",1);
        ClsLogging.writeFileLogWithoutSession("",1);
        //initDataBase(cfg);
        
        ClsLogging.writeFileLogWithoutSession("**************************",1);
        ClsLogging.writeFileLogWithoutSession("* INICIALIZANDO SERVIDOR *",1);
        ClsLogging.writeFileLogWithoutSession("**************************",1);
        ClsLogging.writeFileLogWithoutSession("",1);
        ClsLogging.writeFileLogWithoutSession(" > Generando ficheros Properties de Idiomas.",1);
        
        try
        {
            ClsMngProperties.initProperties();
            ClsLogging.writeFileLogWithoutSession(" > Ficheros Properties de Idiomas generados OK.",1);
        }
        
        catch(ClsExceptions e) 
        {
            ClsLogging.writeFileLogWithoutSession(" > Ficheros Properties de Idiomas generados ERROR.\r\n" + e,1);
            throw new ServletException("Error al generar los ficheros de Idiomas: "+ e.toString(), e);
        }

        // RGG 28-12-2004
        // para cargar la visibilidad de las instituciones
        ClsLogging.writeFileLogWithoutSession("",1);
        ClsLogging.writeFileLogWithoutSession(" > Cargando visibilidad Instituciones.",1);
        
        try
        {
            CenVisibilidad.init();
            ClsLogging.writeFileLogWithoutSession(" > Visibilidad Instituciones cargada OK.",1);
            ClsLogging.writeFileLogWithoutSession("",1);
        }
        
        catch(ClsExceptions e) 
        {
            ClsLogging.writeFileLogWithoutSession(" > Visibilidad Instituciones cargada ERROR.\r\n" + e,1);
            ClsLogging.writeFileLogWithoutSession("",1);
            throw new ServletException("Error al cargar la visibilidad Instituciones: "+ e.toString());
        }
        
        // BEGIN BNS INC_10644_SIGA 16/04/2013
        // Relanzar procesos de facturaci�n en ejecuci�n
        ClsLogging.writeFileLogWithoutSession("",1);
        ClsLogging.writeFileLogWithoutSession(" > Relanzando procesos de facturaci�n en ejecuci�n.",1);
        
        try {
        	FcsFacturacionJGAdm fcsFacturacionJGAdm = new FcsFacturacionJGAdm(null);
        	fcsFacturacionJGAdm.relanzarFacturacion();
            ClsLogging.writeFileLogWithoutSession(" > Procesos de facturaci�n relanzados OK.",1);
            ClsLogging.writeFileLogWithoutSession("",1);
        } catch(ClsExceptions e) {
            ClsLogging.writeFileLogWithoutSession(" > Procesos de facturaci�n relanzados ERROR.\r\n" + e,1);
            ClsLogging.writeFileLogWithoutSession("",1);
            throw new ServletException("Error al relanzar procesos de facturaci�n en ejecuci�n: "+ e.toString());
        }
        //END BNS INC_10389_SIGA
        
        // BEGIN BNS INC_10389_SIGA 22/02/2013
        // Relanzar env�os en estado procesando
        ClsLogging.writeFileLogWithoutSession("",1);
        ClsLogging.writeFileLogWithoutSession(" > Relanzando env�os en estado procesando.",1);
        
        try {            
        	EnvEnviosAdm.relanzarEnviosProcesando();
            ClsLogging.writeFileLogWithoutSession(" > Env�os relanzados OK.",1);
            ClsLogging.writeFileLogWithoutSession("",1);
        } catch(ClsExceptions e) {
            ClsLogging.writeFileLogWithoutSession(" > Env�os relanzados ERROR.\r\n" + e,1);
            ClsLogging.writeFileLogWithoutSession("",1);
            throw new ServletException("Error al relanzar env�os en estado procesando: "+ e.toString());
        }
        //END BNS INC_10389_SIGA
        
        // CENSO-124
        ClsLogging.writeFileLogWithoutSession("Actualizando par�metros proxy censoWS", 3);
		initCensoWSProxyParam();
        
        seg_last=System.currentTimeMillis();
        //ClsLogging.writeFileLogWithoutSession(" ",1);
        ClsLogging.writeFileLogWithoutSession(" > Inicializaci�n finalizada...: "+ ((seg_last-seg_first)/1000)+" segundos",1);
		ClsLogging.writeFileLogWithoutSession("",1);
		ClsLogging.writeFileLogWithoutSession("",1);
		ClsLogging.writeFileLogWithoutSession("**************************",1);
		ClsLogging.writeFileLogWithoutSession("* SERVIDOR INICIALIZADO *",1);
		ClsLogging.writeFileLogWithoutSession("**************************",1);
		ClsLogging.writeFileLogWithoutSession("");
		
//		String config = "/WEB-INF/struts-config.xml";
//		moduleConfig = initModuleConfig("", config);
//		this.initRecursosModuleMessageResources();
		
//		this.initInternal();
    }
    
/*
    protected void initRecursosModuleMessageResources() throws ServletException 
	{
    	MessageResourcesConfig mrcs[] = moduleConfig.findMessageResourcesConfigs();
    	for (int i = 0; i < mrcs.length; i++) {
    		if ((mrcs[i].getFactory() == null) || (mrcs[i].getParameter() == null)) {
    			continue;
    		}
     
    		String factory = mrcs[i].getFactory();
    		MessageResourcesFactory.setFactoryClass(factory);
    		MessageResourcesFactory factoryObject = MessageResourcesFactory.createFactory();
     
    		MessageResources resources = factoryObject.createResources(mrcs[i].getParameter());
    		resources.setReturnNull(mrcs[i].getNull());
    		getServletContext().setAttribute(mrcs[i].getKey() + moduleConfig.getPrefix(), resources);
    	}
	}

*/    

    
	public void destroy()
	{
		ClsLogging.writeFileLogWithoutSession("Destruyendo servlet DataSourceInit",1);
	}

    /*private void initDataBase(ServletConfig cfg) {

        String poolName = cfg.getInitParameter("poolName");
        String datasourcename = cfg.getInitParameter("datasourcenamelist");

//		Parameters for other pools to be used in the future are retrieved
//		as seen below. Note that these params need to be specified in the
//		web.xml	section for this servlet

        String driver = 	cfg.getInitParameter("driver");
        String dbUrl = 		cfg.getInitParameter("dbUrl");
        String user = 		cfg.getInitParameter("user");
        String password =	cfg.getInitParameter("password");

        Properties prop = new Properties();
        prop.setProperty("driver", driver);
        prop.setProperty("dbUrl", dbUrl);
        prop.setProperty("user", user);
        prop.setProperty("password", password);
        prop.setProperty("poolName", poolName);


        try{
            if(poolName.equalsIgnoreCase("struts")){
				com.atos.utils.ReadProperties dsProperties=new ReadProperties("pool.properties");
				String POOLWR = dsProperties.returnProperty("POOL.WRITE");
            //Struts' pool automagically retrieves the params from struts-config.xml
                javax.sql.DataSource dataSource = findDataSource(poolName);
                ClsMngBBDD.setDataSource(poolName, dataSource);

            }else {
                // Pool de Weblogic u otro
                ClsMngBBDD.setDataSource(poolName);

            }

            ClsLogging.writeFileLogWithoutSession("Using datasource: " +
                    datasourcename + " with pool: " + poolName,1);

        } catch(Exception e) {
            ClsLogging.writeFileLogError("DATASOURCE ERROR: "+datasourcename
                + " with pool: "+ poolName + " " + e.toString(), 3);
        }
    }*/
	
	private void initCensoWSProxyParam() {
		GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		String activo = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.CEN_WS_PROXY_ACTIVO, MODULO.CEN);
		
		AppConstants.CEN_WS_PROXY_ACTIVO = AppConstants.DB_TRUE.equals(activo);  
		AppConstants.CEN_WS_PROXY_URL = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.CEN_WS_PROXY_URL, MODULO.CEN);
	}
}