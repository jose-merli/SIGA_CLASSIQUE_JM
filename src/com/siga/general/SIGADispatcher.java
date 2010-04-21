// VERSIONES
// raul.ggonzalez 22-12-2004  Cambio para aceptar parametros en las llamadas
//

package com.siga.general;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ColumnConstants;
import com.atos.utils.TableConstants;
import com.atos.utils.UsrBean;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public class SIGADispatcher extends SIGAActionBase {

	public SIGADispatcher() { }

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
	    ClsLogging.writeFileLog("Mi Dispatcher",request,10);
	    
		try {
			testSession(request,response,this.getServlet());
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLog("USRBEAN nulo",request,5);
	        return mapping.findForward("inicio");
		}
	    
	    HttpSession ses = request.getSession();
	    
	    if (ses.isNew())
	    {
			ClsLogging.writeFileLog("Sesión nueva",request,7);
			return mapping.findForward("inicio");
	    }
	    
	    UsrBean usrbean=(UsrBean)ses.getAttribute("USRBEAN");
	    
	    if (usrbean==null)
	    {
	    	 ClsLogging.writeFileLog("USRBEAN nulo",request,5);
  	         return mapping.findForward("inicio");
	    }
	    
	    String perfil[] = usrbean.getProfile();
	    
	    if (perfil==null || perfil.length==0)
	    {
	    	ClsLogging.writeFileLog("Perfil de USRBEAN nulo",request,5);
	        return mapping.findForward("inicio");
	    }
	    
	    String proceso = request.getParameter("proceso");
	    
	    if (proceso==null || proceso.trim().equals(""))
	    {
	        ClsLogging.writeFileLogError("Parámetro 'proceso' no válido",request,7);
	        return mapping.findForward("inicio");
	    }
	    
	    String institucion = usrbean.getLocation();
	    
	    if (institucion==null || institucion.trim().equals(""))
	    {
	        ClsLogging.writeFileLogError("Parámetro 'institucion' no válido",request,7);
	        return mapping.findForward("inicio");
	    }
	    
	    
	    int access_type=0;
	    String transaction="";
	    String desc="";
	    String transaction_aux="";
	  
	    /******************************/
	    /** CHK RIGHTS & TRANSACTION **/
	    /******************************/
//	    access_type=chkAccessType(proceso,request,perfil,access_type, institucion);
	    String access=usrbean.getAccessForProcessNumber(proceso);
	    if (!access.equals(SIGAPTConstants.ACCESS_READ) && 
	    	!access.equals(SIGAPTConstants.ACCESS_FULL)) {
	    	return mapping.findForward("accesodenegado");
	    }
	    transaction=chkTransaction(proceso,request);
	    
	    int n=transaction.indexOf("@@");
		transaction_aux=transaction.substring(0,n);
	    desc=transaction.substring(transaction_aux.length()+2,transaction.length());
	    ClsLogging.writeFileLog("SIGA Access_type: "+access_type+", Transaccion: "+transaction_aux+", Descripcion: "+desc,request,7);
	    /***************/
	    /*
	    String access="";
        
	    switch(access_type)
        {
        	case 1:
        	{
        	    //access="DENIED";
        	    access=SIGAPTConstants.ACCESS_DENY;
        	    break;
        	}
        	
        	case 2: 
        	{
        	    //access="READ";
        	    access=SIGAPTConstants.ACCESS_READ;
        	    break;
        	}
        	
        	case 3: 
        	{
        	    //access="FULL";
        	    access=SIGAPTConstants.ACCESS_FULL;
        	    break;
        	}
        	
        	default: 
        	{
        	    //access="DENIED";
        	    access=SIGAPTConstants.ACCESS_NONE;
        	    break;
        	}
        }
	    if (access_type!=2 && access_type!=3) {
			return mapping.findForward("accesodenegado");
	    }
*/
	    // RGG 22-12-2004 anhadido para aceptar parametros
	    int n2=transaction_aux.indexOf("?");
	    String llamada = "";
	    String parametros = "";
	    if (n2!=-1) {
	    	llamada=transaction_aux.substring(0,n2);
	    	parametros=transaction_aux.substring(llamada.length(),transaction_aux.length());
	    } else {
	    	llamada=transaction_aux;
	    }

		usrbean.setDescTrans(desc);
		usrbean.setStrutsTrans(transaction_aux);
		usrbean.setAccessType(access);

//	solucion original    return new ActionForward("/" + transaction_aux + ".do");

		// solucion 2
		ActionForward af = new ActionForward("/" + llamada + ".do"+parametros); 
	    return af;   

/* solucion 3
	    try {
			RequestDispatcher rd = request.getRequestDispatcher("/" + transaction_aux + ".do");
			rd.forward(request, response);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ServletException e2) {
			e2.printStackTrace();
		}  
		return new ActionForward();
*/
	}



	    private String chkTransaction(String proc, HttpServletRequest request)
	    {
	      String transaction="";
	      String desc="";
	  	Statement stmtTran=null;
	  	ResultSet rsTran=null;

	      String queryTran="select "+ColumnConstants.FN_PROCESS_TRANS+ ", " + ColumnConstants.FN_PROCESS_DESC +
	  					 " from "+TableConstants.TABLE_PROCESS+
	  					 " where "+ColumnConstants.FN_PROCESS_ID+"='"+proc+"'";

	      Connection con= null;

	      try{
	        con= ClsMngBBDD.getReadConnection();
	        stmtTran=con.createStatement();
   	  	  	//ClsLogging.writeFileLog("SQL TRANSACCION -->"+queryTran, 3);
	        rsTran=stmtTran.executeQuery(queryTran);

	        //while(rsTran.next()){
	        rsTran.next();
	        transaction=rsTran.getString(ColumnConstants.FN_PROCESS_TRANS);
	        desc=rsTran.getString(ColumnConstants.FN_PROCESS_DESC);
	        //ClsLogging.writeFileLog(ColumnConstants.FN_PROCESS_TRANS+" : "+transaction,request,3);
	  		//ClsLogging.writeFileLog(ColumnConstants.FN_PROCESS_DESC+" : "+desc,request,3);
	        //}
	        rsTran.close();
	        stmtTran.close();


	      }catch (Exception e){
	        //ORA-00942: tabla o vista no encontrada
	        //ORA-00923: palabra clave FROM no encontrada donde se esperaba
	        //ORA-00933: comando SQL no terminado correctamente
//	                   throw new ClsExceptions("Error en sentencia SQL, " + e.toString(), "","","","");
	        ClsLogging.writeFileLogError("ERROR: "+e.toString(),request,3);
	        ClsLogging.writeFileLogError("",e,3);
	        
	      }finally{

	  		try {
	  		  if (rsTran!=null)       rsTran.close();	
	  		  if (stmtTran!=null)     stmtTran.close();
	  		  ClsMngBBDD.closeConnection(con);
	  		}
	  		catch (Exception ex) {
	  			ClsLogging.writeFileLogError("ERROR cerrando la conexion: "+ex.toString(),request,3);
	       }
	      }

	      return transaction+"@@"+desc;

	    }

}