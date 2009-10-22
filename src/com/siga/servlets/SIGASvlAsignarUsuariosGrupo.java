package com.siga.servlets;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import com.atos.utils.*;

import javax.transaction.*;
import javax.servlet.http.*;
import com.siga.gui.processTree.*;
import com.siga.generalRequirements.accessControl.*;
import com.siga.generalRequirements.accessControl.asignarUsuariosGrupoRight.*;

public class SIGASvlAsignarUsuariosGrupo extends HttpServlet
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
			
			perfil = req.getParameter(SIGAPTConstants.PROFILE);
	        institucion = ((UsrBean)req.getSession().getAttribute("USRBEAN")).getLocation();
	        nivel = String.valueOf(((UsrBean)req.getSession().getAttribute("USRBEAN")).getLevel());
	        
	        Hashtable ht = (new SIGAAsignarUsuariosGrupoBase()).getLiterals(req);
	        
	        if (function.equalsIgnoreCase(SIGAPTConstants.LITERALES))
	        {
				ObjectOutputStream os = new ObjectOutputStream(res.getOutputStream());
				os.writeObject(ht);
				os.flush();
				os.close();
	        }
	        
	        else if (function.equalsIgnoreCase(SIGAPTConstants.LOAD_GRUPOS))
	        {
	            Vector result = null;
	            
	            try
	            {
	                result=(new SIGAAsignarUsuariosGrupoRight()).loadGrupos(req, perfil, institucion, nivel, ht);
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
	        
	        else 
	        {
    	        String nombreBusqueda = (String)req.getSession().getAttribute("nombreBusqueda");
    	        String idRolBusqueda = (String)req.getSession().getAttribute("idRolBusqueda");
    	        String idGrupoBusqueda = (String)req.getSession().getAttribute("idGrupoBusqueda");
    	        
    	        if (function.equalsIgnoreCase(SIGAPTConstants.LOAD_USUARIOS_GRUPO))
    	        {
		            Vector result = null;
	
		            try
		            {
		                result=(new SIGAAsignarUsuariosGrupoRight()).loadUsuariosGrupos(req, perfil, institucion, nivel, nombreBusqueda, idRolBusqueda, idGrupoBusqueda, ht);

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
    	        
    	        else if (function.equalsIgnoreCase(SIGAPTConstants.SAVE_USUARIOS_GRUPO))
    	        {
    	            Vector result=new Vector();
    	            
    	            //String[] params=req.getParameterValues(SIGAPTConstants.OIDMOVED);
    	            String[] aCrear=req.getParameterValues(SIGAPTConstants.CREAR_USUARIO_GRUPO);
    	            String[] aBorrar=req.getParameterValues(SIGAPTConstants.BORRAR_USUARIO_GRUPO);
    	            
    	            UserTransaction tx = null;

    	            try
    	            {
    	                //result=(new SIGAAsignarUsuariosGrupoRight()).modify(req, perfil, institucion, nivel, params);

						//(new SIGAAsignarUsuariosGrupoRight()).crear(req, aCrear);
						//(new SIGAAsignarUsuariosGrupoRight()).borrar(req, aBorrar);
    	                UsrBean userBean = (UsrBean)req.getSession().getAttribute("USRBEAN");
    	                
    	                tx = userBean.getTransaction();
    	                
    	                tx.begin();
    	                
   	                    SIGAAsignarUsuariosGrupoRight.crear(req, aCrear);
   	                    SIGAAsignarUsuariosGrupoRight.borrar(req, aBorrar);

						tx.commit();
						
						if (aCrear != null)
						{
					        for (int i=0; i<aCrear.length; i++)
					        {
					            String sElemento = aCrear[i];
					            
					            StringTokenizer st = new StringTokenizer(sElemento, "*");
					            
					            String sIdUsuario = st.nextToken();
					            st.nextToken();
					            String sIdRol = st.nextToken();
					            String sIdPerfil = st.nextToken();
					            
					            String mensaje = "El Usuario con ID=\"" + sIdUsuario + "\" y con IDROL=\"" + sIdRol + "\" ha sido asignado al Grupo con ID=\"" + sIdPerfil + "\"";
					            CLSAdminLog.escribirLogAdmin(req, mensaje);
					        }
				        }

						if (aBorrar != null)
						{
					        for (int i=0; i<aBorrar.length; i++)
					        {
					            String sElemento = aBorrar[i];
					            
					            StringTokenizer st = new StringTokenizer(sElemento, "*");
					            
					            String sIdUsuario = st.nextToken();
					            st.nextToken();
					            String sIdRol = st.nextToken();
					            String sIdPerfil = st.nextToken();
					            
					            String mensaje = "El Usuario con ID=\"" + sIdUsuario + "\" y con IDROL=\"" + sIdRol + "\" ha sido desasignado del Grupo con ID=\"" + sIdPerfil + "\"";
					            CLSAdminLog.escribirLogAdmin(req, mensaje);
					        }
						}

						result.add("OK");
						result.add((new SIGAAsignarUsuariosGrupoRight()).loadUsuariosGrupos(req, perfil, institucion, nivel, nombreBusqueda, idRolBusqueda, idGrupoBusqueda,ht));

    					ObjectOutputStream os = new ObjectOutputStream(res.getOutputStream());
    					os.writeObject(result);
    					os.flush();
    					os.close();
    					os.reset();
    	            }
    	            
    	            catch (Exception e)
    	            {
    	                tx.rollback();
    	                
    	                e.printStackTrace();
    	                
    	                result.clear();
    	                result.add("exception");
    	                result.add(e.getMessage());
    	            }
    	        }
	        }
	    }
	    
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public void init() throws javax.servlet.ServletException
	{
		//System.out.println("Init SIGASvlPermisosAplicacion");
    }


	public void destroy()
	{
		//System.out.println("Destroy SIGASvlPermisosAplicacion");
	}
}