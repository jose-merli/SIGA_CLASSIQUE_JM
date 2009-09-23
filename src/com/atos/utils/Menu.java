/**
 * <p>Title: Menu </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

//import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;

public class Menu implements Persistible {
	
	/*
	 * Mapeo de la tabla
	 */
	//public static final String TN_MENU="SIGA_MENU";
    public static final String TN_MENU="GEN_MENU";
	public static final String FN_ID_MENU="IDMENU";
	public static final String FN_ORDER="ORDEN";
	public static final String FN_TAG_WIDTH="TAGWIDTH";
	public static final String FN_ID_PARENT="IDPARENT";
	public static final String FN_URI_IMAGE="URI_IMAGEN";
	public static final String FN_ID_RESOURCE="IDRECURSO";
	public static final String FN_MENU_IDMENU="GEN_MENU_IDMENU";
	public static final String FN_ID_PROCESS="IDPROCESO";
	public static final String FN_ID_INSTITUCION="IDINSTITUCION";
	public static final String FN_USER_MODIFICATION="USUMODIFICACION";
	public static final String FN_DATE_MODIFICATION="FECHAMODIFICACION";
	

	
  	private RowsContainer container = null;
  	private int counter;
  	private HttpServletRequest req;
  	private boolean menuTop;
	private String align="";
	

  	public Menu(HttpServletRequest request) {
    	req = request;
  	}


  	public Vector getAllRecords() {
    	return (container == null) ? null : container.getAll();
  	}

  	public Hashtable getRecord() {
    	Hashtable rec = null;
    	if (container != null) {
        	Row r = (Row)container.get();
        	if (r != null) {
				rec = r.getRow();
        	}
      	}
    	return rec;
  	}

	public void searchMenu(String[] prof, String institucion) throws ClsExceptions {
	  	// RGG Cambio para Bind vbles
	    String perfiles="";
	    Hashtable codigos = new Hashtable();
	    int contador = 1;
	  	for (int i=0;i<prof.length;i++) {
	  	    //perfiles+=",'"+prof[i]+"'";
	  	    perfiles+=",:"+new Integer(contador).toString()+"";
	  	    codigos.put(new Integer(contador),prof[i]);
	  	    contador++;
	  	}
	  	if (perfiles.length()>0) perfiles=perfiles.substring(1);

		String sql=	" SELECT DISTINCT M."+FN_ORDER+", " +
				// RGG correccion para oracle 10G 
				// " LEVEL LVL, " +
				" 0 LVL, " +
	 				" M."+FN_ID_PARENT+", " +
	 				" M."+FN_ID_MENU+", " +
	 				" M."+FN_ID_RESOURCE+", " +
	 				" M."+FN_ID_PROCESS+", " +
	 				" M."+FN_TAG_WIDTH+" " +
	 				//" T."+FN_ID_INSTITUCION+", " +
	 				//" M."+FN_TAG_WIDTH+", " +
	 				//" T."+ColumnConstants.FN_ACCESS_RIGHT_PROFILE+
				" FROM "+TN_MENU+" M,"+
					TableConstants.TABLE_ACCESS_RIGHT+" T "+
				" WHERE T."+ColumnConstants.FN_ACCESS_RIGHT_PROCESS+
		 				"=M."+FN_ID_PROCESS+
				" AND T."+ColumnConstants.FN_ACCESS_RIGHT_PROFILE+
		 				" IN (" + perfiles + ")" +
				" AND T."+ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION+
 				"='" + institucion + "'" +
		 		" AND T."+ColumnConstants.FN_ACCESS_RIGHT_VALUE+" >1 " +
				//" START WITH M."+FN_ID_PARENT+" IS NULL " +
				//" CONNECT BY PRIOR M."+FN_ID_MENU+" = M."+FN_ID_PARENT +
				" ORDER BY M."+FN_ORDER;

    	ClsLogging.writeFileLog("Select Menu: "+sql,req,10);
		container = new RowsContainer();
    	if (!container.queryBind(sql,codigos)) {
      		throw new ClsExceptions("Requested Menu Option not found.", "","","","");
    	}
  	}

  	public Vector search() {
    	return null;
  	}
  	
  	public boolean add() {
    	return true;
  	}

  	public boolean update() {
    	return true;
  	}

  	public int  delete(){
    	int i = 0;
    	return i;
  	}

	public int paintMenu(Vector v, StringBuffer buf, String menuName) throws ClsExceptions {
		int i = -1;
    	boolean doAgain = true;
    	UsrBean usr = (UsrBean)req.getSession().getAttribute("USRBEAN");
		String lenguage=(String)usr.getLanguage();
		// RGG 26/02/2007 cambio de codigos de lenguaje
		AdmLenguajesAdm len = new AdmLenguajesAdm(usr);
		String lenguajeExt="es";
		try {
			lenguajeExt=len.getLenguajeExt(lenguage);
		} catch (Exception e) {
		}
			
    	for (;counter < v.size(); counter++) {
      		i++;
      		String auxMenuName = menuName + i;
      		Row r = (Row) v.get(counter);
      		int level = Integer.parseInt(r.getString("LVL"));
      		int children = 0;

      		//presentacion
      		int stringPosition = buf.length();
      		//String sInicio="oCMenu.makeMenu('"+auxMenuName+"',";
      		String sInicio="oCMenu.makeMenu('"+menuName+r.getString("IDMENU")+"',";
      		
      		//sInicio+="'"+menuName+r.getString(FN_ID_PARENT)+"',";
      		boolean bTienePadre = r.getString(FN_ID_PARENT)==null || r.getString(FN_ID_PARENT).trim().equals("") ? false : true;

      		if(!bTienePadre)
      		{
				sInicio+="'',";
      		}
      		
      		else
      		{
      		    sInicio+="'"+menuName+r.getString(FN_ID_PARENT)+"',";
      		}

//			String text="'"+MngStrutsText.getTextExt(r.getString(FN_ID_RESOURCE), lenguajeExt)+"',";
//			String text="'"+UtilidadesString.getMensajeIdiomaExt(lenguajeExt,r.getString(FN_ID_RESOURCE))+"',";
			String text="'"+UtilidadesString.getMensajeIdioma(usr.getLanguage(),r.getString(FN_ID_RESOURCE))+"',";

			//String link="'/Dispatcher.do?proceso=" + r.getString(FN_ID_PROCESS) + "',";
			//String link=!bTienePadre || (bTienePadre && r.getString(FN_ID_PROCESS).startsWith("0")) ? "''," : "'/Dispatcher.do?proceso=" + r.getString(FN_ID_PROCESS) + "',";
			String link=r.getString(FN_ID_PROCESS).startsWith("0") ? "''," : "'/Dispatcher.do?proceso=" + r.getString(FN_ID_PROCESS) + "',";
			String target="'mainWorkArea',";
			String width=r.getString(FN_TAG_WIDTH);
			String stuff=",'','','','','',";
			String sFinal=");";
			
			if(menuTop) {		
				//si hay muchas opciones en primer nivel se las opciones de submenu podrian salir de campo de vision
				if(level==1&&i>=8) {
					align=stuff+"'left'";
				} else {
					if(level==1&&i<8) {
						align="";
					}
				}
			} else { //menuLeft
				if(level==1)width="''";
			}
      		

	  		if (counter + 1 < v.size()) {
	  			Row next = (Row) v.get(counter + 1);
	  			int nextLevel = Integer.parseInt(next.getString("LVL"));
				if (level < nextLevel) {
					counter ++;
    				//si tiene submenu asociado, no lleva link
      				link="'',";
					children = paintMenu(v, buf, auxMenuName + "_");

		      		//next level could be higher
      				if (counter + 1 < v.size()) {
        				doAgain = (level == Integer.parseInt(((Row) v.get(counter + 1)).getString("LVL")));

      				}
    			} else {	  
	      			if (level > nextLevel) {
	        			doAgain = false;
	      			}
    			}
  			}
  			String aux="";
  			for(int k = 1; k<level;k++) {
  				aux+="\t";
  			} 
  			buf.insert(stringPosition, aux + sInicio + text + link + target + width + align + sFinal+ "\n");
  			if (!doAgain) break;
		}
		return i;
	}


	public boolean getMenuIsAtTop() {
		return menuTop;
	}

	public void setMenuIsAtTop(boolean is) {
		menuTop = is;
	}

}