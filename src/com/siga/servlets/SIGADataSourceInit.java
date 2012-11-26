package com.siga.servlets;

// VERSIONES:
// raul.ggonzalez 28-12-2004 Se anhade la inicializacion de visibilidad al init de la clase
// Luis Miguel Sánchez PIÑA - 24/02/2005 - Se apanhan un poco los mensajes.

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngProperties;
import com.siga.general.CenVisibilidad;


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
        
        seg_last=System.currentTimeMillis();
        //ClsLogging.writeFileLogWithoutSession(" ",1);
        ClsLogging.writeFileLogWithoutSession(" > Inicialización finalizada...: "+ ((seg_last-seg_first)/1000)+" segundos",1);
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
}