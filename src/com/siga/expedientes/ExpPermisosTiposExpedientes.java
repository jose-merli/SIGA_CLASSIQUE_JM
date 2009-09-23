/*
 * Created on Feb 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.siga.expedientes;

import java.util.Hashtable;
import java.util.StringTokenizer;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.beans.ExpPermisosTiposExpedientesBean;

/**
 * @author juan.grau
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpPermisosTiposExpedientes {
    
     Hashtable permisos = new Hashtable();
    
     public ExpPermisosTiposExpedientes(UsrBean user) throws ClsExceptions {
         // Hacer la select para recuperar los permisos       
         // Construir la estructura permisos
         // si el nivel de acceso=10, ponerlo a 1
         // [idinstitucion][idtipoexpediente]
         
         String[] perfiles = user.getProfile();
         String literalesPerfiles = "";
         for (int i=0;i<perfiles.length;i++){
             literalesPerfiles += "'" + perfiles[i] + "', ";
         }
         literalesPerfiles = literalesPerfiles.substring(0,literalesPerfiles.length()-2);
         
        //Tabla exp_permisos
        String T_EXP_PERMISOS = ExpPermisosTiposExpedientesBean.T_NOMBRETABLA;
 		
 		String P_IDPERFIL = ExpPermisosTiposExpedientesBean.C_IDPERFIL;
 		String P_IDINSTITUCION = ExpPermisosTiposExpedientesBean.C_IDINSTITUCION;
 		String P_IDINSTITUCIONTIPOEXPEDIENTE = ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
 		String P_DERECHOACCESO = ExpPermisosTiposExpedientesBean.C_DERECHOACCESO;
 		String P_IDTIPOEXPEDIENTE = ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE;
 		String TIPOACCESO = "max(decode(" + P_DERECHOACCESO + ",1,10," + P_DERECHOACCESO + ")) as TIPOACCESO";
 		
 		//Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
		
			String sql = "SELECT ";	        
	        
		    sql += P_IDINSTITUCIONTIPOEXPEDIENTE + ", ";
		    sql += P_IDTIPOEXPEDIENTE + ", ";		    
		    sql += TIPOACCESO;
		    
			sql += " FROM " + T_EXP_PERMISOS;
		    		    		
			sql += " WHERE " + P_IDINSTITUCION + " = " + user.getLocation();
			sql += " AND " + P_IDPERFIL + " IN (" + literalesPerfiles + ")";
			sql += " GROUP BY " + P_IDINSTITUCIONTIPOEXPEDIENTE + ", " + P_IDTIPOEXPEDIENTE;

//			ClsLogging.writeFileLog("ExpPermisosTiposExpedientes -> QUERY: " + sql,10);
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					String idInstitucion = fila.getString(P_IDINSTITUCIONTIPOEXPEDIENTE);
					String idTipoExp = fila.getString(P_IDTIPOEXPEDIENTE);
					String tipoAcceso = fila.getString("TIPOACCESO").equals("10")?"1":fila.getString("TIPOACCESO");
					if (!permisos.containsKey(idInstitucion)){
					    Hashtable htTipoExp = new Hashtable();
						htTipoExp.put(idTipoExp, tipoAcceso);
						permisos.put(idInstitucion,htTipoExp);
					} else {
					    ((Hashtable)permisos.get(idInstitucion)).put(idTipoExp, tipoAcceso);
					}									
				}
			}
		} catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
     }
     
     public int getPermiso(String idinstitucion,String idtipoexpediente){
         int iTipoAcceso=1;
         Object to1=permisos.get(idinstitucion);
         if(to1!=null){
             Hashtable htTipoExp = (Hashtable)to1;
             Object to2=htTipoExp.get(idtipoexpediente);
             if (to2!=null) iTipoAcceso=Integer.parseInt((String)to2);
         }
         return iTipoAcceso;
     }
     
     public String getBotones(String idinstitucion,String idtipoexpediente, String _botones){
         int TipoAcceso = getPermiso(idinstitucion,idtipoexpediente);
         String botones = "";
         if(_botones==null){
	         switch (TipoAcceso){
	         	case 2: botones = "C"; break;
	         	case 3: botones = "C,E,B"; break;
	         }
         }else{
             StringTokenizer tok=new StringTokenizer(_botones,","); 
             String st;
             while(tok.hasMoreTokens()){
             st=tok.nextToken();
             botones+=st.equalsIgnoreCase("C")&&(TipoAcceso==2||TipoAcceso==3)?st:"";
             botones+=st.equalsIgnoreCase("E")&&TipoAcceso==3?st:"";
             botones+=st.equalsIgnoreCase("B")&&TipoAcceso==3?st:"";
             if(tok.hasMoreTokens()) botones+=",";
          }
         }
         return botones;
     }         
}

     
    
