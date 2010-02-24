/**
 * <p>Title: ExceptionManager </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.siga.Utilidades.UtilidadesString;
import com.siga.eejg.SchedulerException;
import com.siga.general.SIGAException;


public class ExceptionManager implements Persistible {
	private static final String LIT_NOT_FOUND="No se ha encontrado la Descripción para ";
	public static final String TN_ERRORS="GEN_ERRORES";
	public static final String FN_ID_ERROR="IDERROR";
	public static final String FN_ID_STREAM=ColumnConstants.FN_PROCESS_ID_STREAM;
	public static final String FN_DESC_ERROR="DESCRIPCION";
	public static final String FN_ID_ERROR_TYPE=ColumnConstants.FN_MERROR_TYPE_ID;
	public static final String FN_ID_ERROR_CATEGORY=ColumnConstants.FN_CATERROR_ID;
	public static final String FN_ID_USER="IDUSUARIO";
	public static final String FN_DATE_ERROR="FECHAERROR";
	//public static final String FN_WEB_USER="USUARIOWEB";
	public static final String FN_DATE_UPDATE="FECHAMODIFICACION";
	public static final String FN_ID_USER_UPDATE="USUMODIFICACION";
	public static final String FN_ID_INSTITUCION="IDINSTITUCION";

	public static final String SN_ERROR_SEQ="SIGA_SECUENCIA_ERRORES";

  //private Connection con;
  private Row row;
  private String param;
  private Exception next=null;
  private Exception except=null;
  private String idioma=null;
  private String codigoExterno="";
//  HttpServletRequest request=null;

  //public ExceptionManager(ClsExceptions exc, String langCode, String userCode, HttpServletRequest req, String institucion) 
  public ExceptionManager(Exception exc, String langCode, String userCode, 
  		HttpServletRequest req, String institucion)
         throws ClsExceptions {
    //con = conn;
  	idioma=langCode;
  	except=exc;
    row = new Row();
	row.setValue(FN_ID_USER, userCode==null?"":userCode);
	row.setValue(FN_ID_INSTITUCION, institucion==null?"":institucion);
	row.setValue(ColumnConstants.FN_LANG_ID_LANGUAGE,langCode==null?"":langCode.toUpperCase());
	
    if (exc instanceof SIGAException) {
    	param = ((SIGAException)exc).getLiteral(langCode); 
    	next=((SIGAException)exc).getNextException();
    } else if (exc instanceof ClsExceptions) {
    	param = ((ClsExceptions)exc).getParam(); 
    	next=((ClsExceptions)exc).getNextException();
    }
//    request=req;
    if (exc instanceof ClsExcBase) { 
    	Exception e=((ClsExcBase)exc).getNextException();
		while (e!=null) {
			if (e instanceof ClsExceptions) {

				row.setValue(FN_DESC_ERROR, ((ClsExceptions)e).getMsg()==null?"":((ClsExceptions)e).getMsg());
/*				row.setValue(FN_ID_ERROR_TYPE, ((ClsExceptions)e).getErrorType()==null?"":((ClsExceptions)e).getErrorType());
				row.setValue(FN_ID_ERROR_CATEGORY, ((ClsExceptions)e).getErrorCategory()==null?"":((ClsExceptions)e).getErrorCategory());
				row.setValue(ColumnConstants.FN_PROCESS_ID, ((ClsExceptions)e).getProcess()==null?"":((ClsExceptions)e).getProcess());  */
			    try {
			    	completeRecord(req);
			    } catch (Exception ele) {
			    	ClsLogging.writeFileLog("NO SE HA PODIDO ALMACENAR EL ERROR APLICATIVO EN BASE DE DATOS",1);
			    }
			    e=((ClsExcBase)e).getNextException();
			} else if (e instanceof ClsExcBase) {
				e=((ClsExcBase)e).getNextException();
			} else {
				e=null;
			}
		}
    }
  }

  public String getSigaErrMsg() {
  	if (except!=null && except instanceof SIGAException) {
  		SIGAException ee= (SIGAException) except;
  		if (ee.getClsException()) {
  			return ((SIGAException)except).getMsg(idioma) + "  COD:" + getCodigoExterno();
  		} else {
  			return ((SIGAException)except).getMsg(idioma);
  		}
  	}
  	if (except!=null && except instanceof ClsExceptions) {
  		return ((ClsExceptions)except).getMsg() + "  COD:" + getCodigoExterno();
  	}
  	if (except!=null) {
  		return except.getMessage() + "  COD:" + getCodigoExterno(); 
  	}
  	return UtilidadesString.getMensajeIdioma(idioma,"err.general.app")  + " COD=" + getCodigoExterno();
  }
  
  public String getStream() {
    return row.getString(FN_ID_STREAM);
  }

  public Exception getNextException() {
    return next;
  }

  public String getProcessId() {
    return row.getString(ColumnConstants.FN_PROCESS_ID);
  }

  public String getProcessDesc() {
    return row.getString(ColumnConstants.FN_PROCESS_DESC);
  }

  public String getErrorCategory() {
    return row.getString(FN_ID_ERROR_CATEGORY);
  }

  public String getDescErrorCategory() {
    return row.getString(ColumnConstants.FN_CATERROR_DESC);
  }

  public String getErrorType() {
    return row.getString(FN_ID_ERROR_TYPE);
  }

  public String getDescErrorType() {
    return row.getString(ColumnConstants.FN_DERROR_TYPE_DESC);
  }

  public String getMsg() {
  	String ErrorF=row.getString(FN_DESC_ERROR);
  	if (ErrorF!=null && !ErrorF.startsWith(LIT_NOT_FOUND))
  		return ErrorF;
  	return except!=null?except.getMessage():"Descripción no encontrada"; 
  }

  public String getUserId() {
    return row.getString(FN_ID_USER);
  }

  public String getInstitucion() {
      return row.getString(FN_ID_INSTITUCION);
    }

  public boolean isPersistent() {
    return (row.getString(ColumnConstants.FN_MERROR_TYPE_PERSISTENT) != null) ? "1".equals(row.getString(ColumnConstants.FN_MERROR_TYPE_PERSISTENT)) : false;
  }

  public void completeRecord(HttpServletRequest request) throws ClsExceptions {
    //copy values up not to loose them
    Hashtable aux = (Hashtable) row.getRow().clone();

    //get ID_STREAM, and PROCCES_DESCRIPTION
    String sql = "SELECT "+ColumnConstants.FN_PROCESS_ID_STREAM+", "+
    					   ColumnConstants.FN_PROCESS_DESC+", "+
    			 		   ColumnConstants.FN_PROCESS_TRACE+
                 " FROM "+TableConstants.TABLE_PROCESS+" " +
                 "WHERE "+ColumnConstants.FN_PROCESS_ID+
				"='"+row.getString(ColumnConstants.FN_PROCESS_ID)+"' ";
    if (!row.find(sql)) {
      row.setValue(FN_ID_STREAM, "UNKNOWN");
      row.setValue(ColumnConstants.FN_PROCESS_DESC, 
					"No se ha encontrado el proceso: " + row.getString(ColumnConstants.FN_PROCESS_ID));
    } else {  //found. Copy aux values into row
      row.getRow().putAll(aux);
    }
    //copy all values into aux
    aux.putAll(row.getRow());


    //get ERROR CATEGORY DESCRIPTION
    sql = "SELECT "+ColumnConstants.FN_CATERROR_DESC+
          " FROM "+TableConstants.TABLE_ERROR_CATEGORIES+
          //" WHERE "+ColumnConstants.FN_CATERROR_ID+"= '" + row.getString("ID_ERROR_CATEGORY") + "' " +
          " WHERE "+ColumnConstants.FN_CATERROR_ID+"= '" + row.getString(ColumnConstants.FN_CATERROR_ID) + "' " +
          " AND "+ColumnConstants.FN_CATERROR_DELETED+" = 0 ";
    if (!row.find(sql)) {
      row.setValue(ColumnConstants.FN_CATERROR_DESC, LIT_NOT_FOUND + "la Categoria Error: "+ row.getString("ID_ERROR_CATEGORY"));
    } else {
      row.getRow().putAll(aux);
    }
    aux.putAll(row.getRow());

    //get PERSISTENT (if not found, set to 0) and DESC_ERROR_TYPE
    sql = "SELECT M."+ColumnConstants.FN_MERROR_TYPE_PERSISTENT+", "+
    	  "D."+ColumnConstants.FN_DERROR_TYPE_DESC+
          " FROM "+TableConstants.TABLE_DERROR_TYPE+" D, " +
          TableConstants.TABLE_MERROR_TYPE+" M " +
          " WHERE M."+ColumnConstants.FN_MERROR_TYPE_CATERROR_ID+
			" = '" + row.getString(ColumnConstants.FN_CATERROR_ID) + "' " +
          " AND M."+ColumnConstants.FN_MERROR_TYPE_ID+
			" = '"+row.getString(ColumnConstants.FN_MERROR_TYPE_ID) + "' " +
          " AND D."+ColumnConstants.FN_DERROR_TYPE_CATERROR_ID+
			" = M."+ColumnConstants.FN_MERROR_TYPE_CATERROR_ID+
          " AND D."+ColumnConstants.FN_DERROR_TYPE_ID+
			" = M."+ColumnConstants.FN_MERROR_TYPE_ID+
          " AND D."+ColumnConstants.FN_DERROR_TYPE_ID_LANGUAGE+
			" = '" + row.getString(ColumnConstants.FN_LANG_ID_LANGUAGE) + "' " +
	 	  " AND M."+ColumnConstants.FN_MERROR_TYPE_DELETED+"= 0 " +
          " AND D."+ColumnConstants.FN_DERROR_TYPE_DELETED+"= 0 ";
    if (!row.find(sql)) {
      row.setValue(ColumnConstants.FN_MERROR_TYPE_PERSISTENT, "0");
      //row.setValue(ColumnConstants.FN_DERROR_TYPE_DESC, "No se ha encontrado la Descripción para el Tipo Error " + row.getString("ID_ERROR_TYPE") + " in Error Category " + row.getString("ID_ERROR_CATEGORY"));
      row.setValue(ColumnConstants.FN_DERROR_TYPE_DESC, LIT_NOT_FOUND + "el Tipo Error: " + row.getString(ColumnConstants.FN_MERROR_TYPE_ID) + " en la Categoría Error: " + row.getString(ColumnConstants.FN_CATERROR_ID));
    } else {
      StringBuffer descError = new StringBuffer(row.getString(ColumnConstants.FN_DERROR_TYPE_DESC));
      if (descError.toString().indexOf("$") != -1) {
        do {
          descError.replace(descError.toString().indexOf("$"),
                            descError.toString().indexOf("$") + 1,
                            param);
        } while (descError.toString().indexOf("$") != -1);
      }
      
      row.getRow().putAll(aux);
      row.setValue(ColumnConstants.FN_DERROR_TYPE_DESC, descError.toString());
    }
    if(isPersistent()) {

      //Load webuser from session
      HttpSession ses = request.getSession();
      UsrBean usrbean=(UsrBean)ses.getAttribute("USRBEAN");
      String webuser="NOT IDENTIFIED";

      if (usrbean!=null)
      {
        webuser=usrbean.getUserName();
      }
      
      UserTransaction tx = usrbean.getTransaction();
      try {
		tx.begin();
      } catch(Exception ex) {
      	throw new ClsExceptions(ex,ex.getMessage());
      }

      try { 
		ClsMngBBDD.writeError(getUserId(), getStream(), getMsg(), getErrorCategory(), getErrorType(),webuser, getInstitucion());
		try {
			tx.commit();
		} catch(Exception e) {
			throw new ClsExceptions(e,e.getMessage());
		}
	  } catch(ClsExceptions ex) {
	  	try {
			tx.rollback();
	  	} catch(Exception e) {
	  		ClsLogging.writeFileLogError("Error procesando la Excepción: "+e.getMessage(),e,1);
	  	}
			  		  	
		throw ex;
	  } 
    }
  }

  // RGG AQUI SE PONE EL CODIGO DE ERROR EN LA TRAZA DE LA EXCEPCION
  public String getLogCompleteMessage(String langCode) {
    StringBuffer b = new StringBuffer();
    Date d = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
    
    String codigoErrorExterno="SIGAE"+"-"+getInstitucion()+"-"+getUserId()+"-"+sdf2.format(d);
    setCodigoExterno(codigoErrorExterno);
    
    b.append("<----------------  Excepcion en tiempo de ejecucion: ---------------->\n");
/*    b.append("MODULO             --> " + getStream() + "\n");
    b.append("PROCESO            --> " + getProcessId() + " - " + getProcessDesc() + "\n");
    b.append("CATEGORIA ERROR    --> " + getErrorCategory() + " - " + getDescErrorCategory() + "\n");
    b.append("TIPO ERROR         --> " + getErrorType() + " - " + getDescErrorType() + "\n");
    b.append("DESCRIPCION ERROR  --> " + getMsg() + "\n");  */
    b.append("CODIGO EXTERNO     --> " + getCodigoExterno() + "\n");
    b.append("<-------------------------------------------------------------------->\n");
    b.append("CODIGO USUARIO     --> " + getUserId() + "\n");
    b.append("INSTITUCION        --> " + getInstitucion() + "\n");
    b.append("FECHA ERROR        --> " + sdf.format(d) + "\n\n");
    
    if (except!=null) {
      b.append( "EXCEPCIONES ANIDADAS (STACK TRACE)\n");
      Exception e=except;
      Exception last=except;
      while (e!=null) {
      	last=e;
		b.append("\t\t<---------------------------------------------------------------->\n");
        if (e instanceof ClsExceptions) {
          b.append("\t\tPROCESO            --> " + ((ClsExceptions)e).getProceso() + "\n");
          b.append("\t\tPARAMETRO ERROR    --> " + ((ClsExceptions)e).getParam() + "\n");
          b.append("\t\tDESCRIPCION ERROR  --> " + ((ClsExceptions)e).getMsg() + "\n");
          e=((ClsExceptions)e).getNextException();
        } else if (e instanceof SIGAException) {
          b.append("\t\tLITERAL            --> " + ((SIGAException)e).getLiteral(langCode)+"\n");
          b.append("\t\tSUBLITERAL         --> " + ((SIGAException)e).getSubLiteral(langCode)+"\n");
          e=((SIGAException)e).getNextException();
        } else {
          b.append("\t\tEXCEPCION          --> " + e.getMessage() + "\n");
          e=null;
        }
        b.append("\t\t<---------------------------------------------------------------->\n");
      }
      if (last!=null) {
      	StringWriter sw = new StringWriter();
      	PrintWriter pw = new PrintWriter(sw);
      	last.printStackTrace(pw);
      	b.append("  PILA DE EJECUCIÓN DE LA EXCEPCION ORIGINARIA\n");
      	b.append(sw.toString());
      }
    }
    b.append("<-------------------------------------------------------------------->\n");
    return b.toString();
  }

  public static String getCompleteMessageParaLogger(Exception e, String idInstitucion, String idUsuario) {
      StringBuffer b = new StringBuffer();
      Date d = new Date(System.currentTimeMillis());
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
      
      String codigoErrorExterno="SIGAE"+"-"+idInstitucion+"-"+idUsuario+"-"+sdf2.format(d);
      
      
      b.append("<----------------  Excepcion en tiempo de ejecucion: ---------------->\n");
  /*    b.append("MODULO             --> " + getStream() + "\n");
      b.append("PROCESO            --> " + getProcessId() + " - " + getProcessDesc() + "\n");
      b.append("CATEGORIA ERROR    --> " + getErrorCategory() + " - " + getDescErrorCategory() + "\n");
      b.append("TIPO ERROR         --> " + getErrorType() + " - " + getDescErrorType() + "\n");
      b.append("DESCRIPCION ERROR  --> " + getMsg() + "\n");  */
      b.append("CODIGO EXTERNO     --> " + codigoErrorExterno + "\n");
      b.append("<-------------------------------------------------------------------->\n");
      b.append("CODIGO USUARIO     --> " + idUsuario + "\n");
      b.append("INSTITUCION        --> " + idInstitucion + "\n");
      b.append("FECHA ERROR        --> " + sdf.format(d) + "\n\n");
      
      if (e!=null) {
        b.append( "EXCEPCIONES ANIDADAS (STACK TRACE)\n");
        Exception last=e;
        while (e!=null) {
            last=e;
          b.append("\t\t<---------------------------------------------------------------->\n");
          if (e instanceof ClsExceptions) {
            b.append("\t\tPROCESO            --> " + ((ClsExceptions)e).getProceso() + "\n");
            b.append("\t\tPARAMETRO ERROR    --> " + ((ClsExceptions)e).getParam() + "\n");
            b.append("\t\tDESCRIPCION ERROR  --> " + ((ClsExceptions)e).getMsg() + "\n");
            e=((ClsExceptions)e).getNextException();
          } else if (e instanceof SIGAException) {
            b.append("\t\tLITERAL            --> " + ((SIGAException)e).getLiteral("ES")+"\n");
            b.append("\t\tSUBLITERAL         --> " + ((SIGAException)e).getSubLiteral("ES")+"\n");
            e=((SIGAException)e).getNextException();
          }else if (e instanceof SchedulerException) {
              last=null;

            } else {
            b.append("\t\tEXCEPCION          --> " + e.getMessage() + "\n");
            e=null;
          }
          b.append("\t\t<---------------------------------------------------------------->\n");
        }
        if (last!=null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            last.printStackTrace(pw);
            b.append("  PILA DE EJECUCIÓN DE LA EXCEPCION ORIGINARIA\n");
            b.append(sw.toString());
        }
      }
      b.append("<-------------------------------------------------------------------->\n");
      return b.toString();
    }


  public boolean add() throws ClsExceptions {
  	return  false;
  }


  /**
   * Not implemented. May be used to Show a list of Application errors
   * @return NULL
   * @throws ClsExceptions
   */
  public Vector search() throws ClsExceptions {
  	return  null;
  } 

  /**
   * No functional. Needed to implement interface Persistible
   * @return FALSE
   * @throws ClsExceptions
   */
  public boolean update() throws ClsExceptions {
  	return  false;
  } 


  /**
   * No functional. Needed to implement interface Persistible
   * @return -1
   * @throws ClsExceptions
   */
  public int delete() throws ClsExceptions {
    return -1;
  } 
  
  public String getCodigoExterno() {
  	return codigoExterno;
  }
  public void setCodigoExterno(String valor) {
  	codigoExterno=valor;
  }
}