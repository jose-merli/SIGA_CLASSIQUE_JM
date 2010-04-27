/*
 * Created on 19-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

import java.sql.SQLException;
import java.util.Hashtable;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExcBase;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesString;

import es.satec.businessManager.BusinessException;

/**
 * @author 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SIGAException extends ClsExcBase  {
	
	
	private String literal[]=null;
	private String literal_2=null;
	protected Hashtable errorLit=null;
	protected String[][] reemplazar=null;
	public final String CARACTER_REMPLZAR_INIT="{";
	public final String CARACTER_REMPLZAR_FIN="}";
	
	private boolean clsException = false;
	
	public SIGAException (String _literal) {
		literal=new String[1];
		literal[0]=_literal;
	}
	
	public SIGAException (String _literal,String[] reem) {
		literal=new String[1];
		literal[0]=_literal;
		if (reem!=null) {
			reemplazar=new String[1][];
			reemplazar[0]=reem;
		}
	}
	
	public String getMessage(String dd) {
		return getMsg(ClsConstants.LENGUAJE_ESP);
	}
	
	public boolean getClsException() {
		return clsException;
	}
	
	public void setClsException(boolean valor) {
		clsException=valor;
	}
	
	public SIGAException (Exception e,String[] reem) {
		if (reem!=null) {
			reemplazar=new String[1][];
			reemplazar[0]=reem;
		}
		next=e;
		parseException(e);
	}
	
	public SIGAException (Exception e) {
		next=e;
		parseException(e);
	}
	
	public SIGAException (String _literal,Exception e) {
		literal=new String[1];
		literal[0]=_literal;
		next=e;
		parseException(e);
	}

	public SIGAException (String _literal,Exception e,String[] reem) {
		if (reem!=null) {
			reemplazar=new String[1][];
			reemplazar[0]=reem;
		}
		literal=new String[1];
		literal[0]=_literal;
		next=e;
		parseException(e);
	}
	
	public void setParams(String[] params) {
		if (params==null) {
			reemplazar=null;
			return;
		}
		if (reemplazar==null) {
			reemplazar=new String[1][];
			reemplazar[0]=params;
		}
	}
	
	public void setLiteral(String _literal) {
		literal=new String[1];
		literal[0]=_literal;
	}
	
	public String getLiteral() {
		return literal[0];
	}
	
	public void setSubLiteral(String _literal) {
		literal_2=_literal;
	}
	
	public String getSubLiteral(String idioma) {
		if (literal_2!=null)
			return UtilidadesString.getMensajeIdioma(idioma,literal_2);
		return null;
	}
	
	public String getLiteral(String idioma) {
		return getLiteral(idioma,0);
	}
	
	// RGG TRATAMIENTO DE SIGA EXCEPTION COMO SI FUERA CLS EXCEPTION (SIGA EXCEPTION CON MENSAJE GENERAL)
	protected String getLiteral(String idioma,int column) {
		String msg = "";
		if (literal!=null && literal.length>column && column>-1) {
		    if (literal[column]==null) {
				literal[column]="messages.general.error";
		    }
			if (!literal[column].equals("messages.general.error") && !literal[column].equals("error.messages.application")) {
				// SI EL MENSAJE DE LA SIGAEXCEPTION ES EL GENERICO --> SE MUESTRA EL CODIGO (COMO CLSEXCEPTION)
				this.setClsException(false);
			}
			msg=UtilidadesString.getMensajeIdioma(idioma,literal[column]);
			if (reemplazar!=null && msg.indexOf(CARACTER_REMPLZAR_INIT)!=-1) {
				for (int i=0;i<reemplazar.length;i++) {
					msg=reemplazarTexto(idioma,msg,i,column);
				}
			}
		}
		return msg;		
	}
	
	protected String reemplazarTexto(String idioma,String msg,int i,int column) {
		String valor=CARACTER_REMPLZAR_INIT+i+CARACTER_REMPLZAR_FIN;
		int index=msg.indexOf(valor);
		while (index!=-1) {
			String ret=msg.substring(0,index);
			ret+=UtilidadesString.getMensajeIdioma(idioma,reemplazar[i][column]);
				//reemplazar[i][column];
			ret+=msg.substring(index+valor.length());
			msg=ret;
			index=msg.indexOf(valor);
		}
		return msg;
	}
	
	public String getMsg(String idioma) {
		
		if (getSubLiteral(idioma)!=null) {
			return getLiteral(idioma) + " : "  + getSubLiteral(idioma);
		} else {
			return getLiteral(idioma);
		}
		
	}
	

	// RGG
	protected void parseException(Exception e) {
		Exception el=e;
		while (el!=null) {
			if (el instanceof SQLException) {
				//String state=((SQLException)el).getSQLState();
				String mensaje = ((SQLException)el).getMessage();
				String state="";
				String cod="";
				
//				if (mensaje.indexOf("ORA-")>=0) {
//					state = mensaje.substring(mensaje.indexOf("ORA-"),mensaje.length());
//					state = state.substring(0,mensaje.indexOf(" ")-1);
//					ClsLogging.writeFileLog("******SQLState : " + state,3);
//					cod=SIGAExcConstants.getErrorDescription(state);
//					setLiteral(cod);
//				} else {
//					cod = mensaje;
//				}

				
				if (mensaje.indexOf("ORA-00001")>=0 ||
				    mensaje.indexOf("ORA-02292")>=0) {
					state = mensaje.substring(mensaje.indexOf("ORA-"),mensaje.length());
					state = state.substring(0,mensaje.indexOf(" ")-1);
					ClsLogging.writeFileLog("******SQLState : " + state,3);
					cod=SIGAExcConstants.getErrorDescription(state);
					setLiteral(cod);
				}
				else {
					cod = mensaje;
				}

				//cod = mensaje;
				if (cod!=null && getLiteral(ClsConstants.LENGUAJE_ESP)==null) {
					// RGG setSubLiteral(cod);
					//setSubLiteral(null);
					if (cod!=null && !cod.trim().equals("")) setLiteral(cod);
				}
				break;
			} else if (el instanceof ClsExceptions) {
				String cod=SIGAExcConstants.getClsExceptionErrorDescription(((ClsExceptions)el).getErrorCode());
				if (cod!=null && getLiteral(ClsConstants.LENGUAJE_ESP)!=null) {
					// rgg setSubLiteral(cod);
					setLiteral(cod);
					break;
				}
		    }else if (el instanceof BusinessException){
				setLiteral(el.getMessage());
				break;
			} 
			if (el instanceof ClsExcBase) {
				el=((ClsExcBase)el).getNextException();
			} 
			else {
				el=null;
			}
		}
	}
	
/*	public void setSubLiteral(Hashtable hash) {
		String value=(String)hash.get(""+getTipo());
		if (value!=null && value.length()!=0) {
			setSubLiteral(value);
		}
	}  */
}
