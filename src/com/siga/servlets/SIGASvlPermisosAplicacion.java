package com.siga.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.generalRequirements.accessControl.SIGAProcessBase;
import com.siga.generalRequirements.accessControl.accessRight.SIGAAccessRight;
//import com.siga.generalRequirements.accessControl.processMngt.SIGAProcessMngt;
import com.siga.gui.processTree.SIGAPTConstants;

public class SIGASvlPermisosAplicacion extends HttpServlet
{
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
    	doWork(req, res);
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
    	doWork(req, res);
    }

	private void doWork(HttpServletRequest req, HttpServletResponse res)
	{
		String perfil = "";	        
        String institucion = "";
        String nivel = "";

	    try
	    {
	        String function=req.getParameter(SIGAPTConstants.SERVLET_FUNCTION);
	        
	        ClsLogging.writeFileLog("---------------------",10);
	        ClsLogging.writeFileLog("PERMISOS APLICACION > funcion: "+function,10);
	        
			perfil = req.getParameter(SIGAPTConstants.PROFILE);	        
	        
			ClsLogging.writeFileLog("PERMISOS APLICACION > perfil: "+perfil,10);
	        
			institucion = ((UsrBean)req.getSession().getAttribute("USRBEAN")).getLocation();
	        nivel = String.valueOf(((UsrBean)req.getSession().getAttribute("USRBEAN")).getLevel());
	        
	        ClsLogging.writeFileLog("PERMISOS APLICACION > nivel: "+nivel,10);
	        	        	        
	        if (function.equalsIgnoreCase(SIGAPTConstants.LITERALES))
	        {
				Hashtable ht = new Hashtable();
				
				ht=(new SIGAProcessBase()).getLiterals(req);

				ObjectOutputStream os = new ObjectOutputStream(res.getOutputStream());
				os.writeObject(ht);
				os.flush();
				os.close();
	        }
	        
	        else if (function.equalsIgnoreCase(SIGAPTConstants.LOAD_PROCESSES))
	        {
	            Vector result = null;
	            
	            try
	            {
		            result=(new SIGAAccessRight()).loadProcess(req, perfil, institucion, nivel);
	
					ObjectOutputStream os = new ObjectOutputStream(res.getOutputStream());
					os.writeObject(result);
					os.flush();
					os.close();
					os.reset();
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                result.clear();
	                result.add("exception");
	                result.add(e.getMessage());
	            }
	        }
	        
	        else if (function.equalsIgnoreCase(SIGAPTConstants.MODIFYACCESS))
	        {
	            Vector result=new Vector();
	            
	            String[] params=req.getParameterValues(SIGAPTConstants.OIDMOVED);
	            
	            ClsLogging.writeFileLog("PERMISOS APLICACION > params: "+params.toString(),10);
	            for (int j=0;j<params.length;j++) {
	            	ClsLogging.writeFileLog("   params["+j+"]="+params[j],10);
	            }
	            
	            try
	            {
	                result=(new SIGAAccessRight()).modify(req, perfil, institucion, nivel, params);

					ObjectOutputStream os = new ObjectOutputStream(res.getOutputStream());
					os.writeObject(result);
					os.flush();
					os.close();
					os.reset();
	            }
	            
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                result.clear();
	                result.add("exception");
	                result.add(e.getMessage());
	            }
	        }
	    }
	    
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	}
}