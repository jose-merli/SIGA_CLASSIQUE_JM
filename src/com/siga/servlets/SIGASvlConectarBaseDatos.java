package com.siga.servlets;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

public class SIGASvlConectarBaseDatos extends HttpServlet
{  
	
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws  javax.servlet.ServletException, java.io.IOException
    {
    	doWork(req, res);
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException, java.io.IOException
    {
    	doWork(req, res);
    }
    
    public void doWork (HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException, java.io.IOException
    {
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT IDINSTITUCION FROM CEN_INSTITUCION";
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
				}
			}
			res.setHeader("content-type", "text/html");
			PrintWriter out = res.getWriter();
			out.println("OK");
		} 
		catch (Exception e) { 	
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			 
		}
		

    }
    
    
}