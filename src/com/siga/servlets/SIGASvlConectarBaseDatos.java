package com.siga.servlets;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.servlet.*;
import javax.servlet.http.*;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.GenParametrosBean;
import com.siga.beans.MasterBean;

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