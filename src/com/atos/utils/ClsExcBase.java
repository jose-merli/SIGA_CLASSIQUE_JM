/*
 * Created on 19-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.atos.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClsExcBase extends Exception {

	protected Exception next=null;
	protected boolean hiddenFrame=true;
	protected String user="";
	protected long timeGeneration=0;
	protected String proceso="";
	protected String errorCode=null;
	
	/**
	 * 
	 */
	public ClsExcBase() {
		super();
	}
	
	public ClsExcBase(String msg) {
		super(msg);
	}
	
	public void setHiddenFrame(boolean hidd) {
		hiddenFrame=hidd;
	}
	
	public boolean getHiddenFrame() {
		return hiddenFrame;
	}
	
	 public void prepare(HttpServletRequest req) {
	 	if (hiddenFrame) {
	 		req.setAttribute("hiddenFrame", "1");
	 	} else {
	 		req.setAttribute("hiddenFrame", "0");
	 	}
		if (req.getParameter("exceptionTarget") != null) {
		  req.setAttribute("exceptionTarget", req.getParameter("exceptionTarget"));
		}
		try {
			req.setAttribute("exception", this);
			UsrBean bean=(UsrBean)req.getSession().getAttribute(ClsConstants.USERBEAN);
			if (bean!=null) {
				user=bean.getUserName() + " " +  bean.getUserDescription();
			}
		} catch (Exception e) {}
		
		try {
			String uri=req.getRequestURI();
			if (uri==null) throw new ClsExceptions("URL no reconocida por SIGA");
			int idexofdo=uri.indexOf(".do");
			if (idexofdo==-1) throw new ClsExceptions("URL no reconocida por SIGA ("+uri+")");
			int initindex=uri.length();
			int indexofslash=uri.indexOf("/",1);
			if (indexofslash==-1) throw new ClsExceptions("URL no reconocida por SIGA ("+uri+")");
			proceso=uri.substring(indexofslash+1,idexofdo);
		} catch (Exception e) {}
	  }
	 
	  public Exception getNextException() {
	    return next;
	  }

	  public void setNextException(Exception e) {
	    next=e;
	  }
	  
	  public String getUser() {
	  	return user; 
	  }
	  
	  public String getProceso() {
	  	return proceso; 
	  }
	  
	  public String getErrorCode() {
	  	return errorCode;
	  }
	  
	  public void setErrorCode(String _errorCode) {
	  	errorCode=_errorCode;
	  }
}
