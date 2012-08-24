package com.siga.general;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

public class ComboAutocomplete {
	
	/**
	 * 
	 * @param response
	 * @param usuario
	 * @param campoTabla
	 * @param campoPrioridad ==> NULLABLE
	 * @param campoIdentificador
	 * @param campoIdentificadorPadre ==> NULLABLE
	 * @param campoNombre
	 * @param guiones
	 * @param valorFiltro ==> NULLABLE
	 * @param valorIdentificadorPadre ==> NULLABLE
	 * @param numeroMaximoOpciones
	 * @throws Exception
	 */
	public static void getAjaxPoblaciones (
			HttpServletResponse response,
			UsrBean usuario,			
			String campoTabla,
			String campoPrioridad,
			String campoIdentificador,
			String campoIdentificadorPadre,
			String campoNombre,			
			String guiones,
			String valorFiltro,
			String valorIdentificadorPadre,
			Integer numeroMaximoOpciones
			
		) throws Exception {
		
		if (response==null||usuario==null||campoTabla==null||campoIdentificador==null||campoNombre==null||guiones==null||numeroMaximoOpciones==null)
			throw new Exception("Faltan datos");
		
		Boolean tieneCampoPrioridad = (campoPrioridad!=null);
		Hashtable codigosBind = new Hashtable();
		RowsContainer rc = null;
		
	    String sql = " SELECT "+campoTabla+"."+campoIdentificador+", "+
	    	campoTabla+"."+campoNombre;
	    
	    if (tieneCampoPrioridad)
	    		sql+=", "+campoTabla+"."+campoPrioridad;
	    
	    if (valorFiltro==null||valorFiltro.trim().equalsIgnoreCase("")) {
	    	sql+=", -1 AS SELECCIONADO ";
	    	valorFiltro="*";
	    } else 	    		    	
	    	sql+=", LENGTH('"+valorFiltro+"')-LENGTH("+campoTabla+"."+campoNombre+") AS SELECCIONADO ";
	    		    
	    		
	    String fromSql=" FROM " + campoTabla+
	    	" WHERE REGEXP_LIKE("+campoTabla+"."+campoNombre+", '"+valorFiltro+"') ";

	    if (valorIdentificadorPadre!=null&&campoIdentificadorPadre!=null)
	    	fromSql+=" AND "+campoTabla+"."+campoIdentificadorPadre+"='"+valorIdentificadorPadre+"' ";
	        	    	    
	    sql+=fromSql;
	    String countSql="(SELECT COUNT(*) AS CONTADOR "+fromSql+") ";
	    
	    if (tieneCampoPrioridad) {
	    	sql+=" AND ("+numeroMaximoOpciones+">="+countSql+" OR "+campoTabla+"."+campoPrioridad+" IS NOT NULL) ";
	    	sql+=" ORDER BY "+campoTabla+"."+campoPrioridad+" ASC, "+
	    		campoTabla+"."+campoNombre+" ASC ";
	    }
	    else {
	    	sql+=" AND "+numeroMaximoOpciones+">="+countSql;
	    	sql+=" ORDER BY "+campoTabla+"."+campoNombre+" ASC ";
	    }
	    
	    rc = new RowsContainer();

        rc.findNLS(sql);    

		String htmlOptions="";
		Integer numOptions = 0;
		
		if (rc!=null) {			
			htmlOptions="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
			numOptions=rc.size()+1;
			
			Boolean bGuiones=true;
			Boolean bPrioridad = false;
			Boolean bSeleccionado = false;
			
			for (int i = 0; i < rc.size(); i++)	{
				Row fila = (Row) rc.get(i);
				Hashtable registro = (Hashtable)fila.getRow(); 
				
				if (registro != null) { 
					
					if (tieneCampoPrioridad)  {
						Integer prioridad = UtilidadesHash.getInteger(registro, campoPrioridad);
						
						if(bGuiones&&bPrioridad&&prioridad==null) {
							htmlOptions+="<option value=''>"+guiones+"</option>";
							bGuiones=false;
							numOptions++;
						}
						
						if (!bPrioridad)				 
	 						bPrioridad = (prioridad!=null);  	
					}
					
					if (!bSeleccionado && UtilidadesHash.getInteger(registro, "SELECCIONADO")==0) {
						bSeleccionado=true;
						htmlOptions+="<option selected value='"+UtilidadesHash.getString(registro, campoIdentificador)+"'>"+
								UtilidadesHash.getString(registro, campoNombre)+"</option>";
					}
					else {
						htmlOptions+="<option value='"+UtilidadesHash.getString(registro, campoIdentificador)+"'>"+
							UtilidadesHash.getString(registro, campoNombre)+"</option>";
					}
					  	   	
		   			  	   						
				}
			}
		}		
		
		ArrayList arrayHtml = new ArrayList(); 
		arrayHtml.add(htmlOptions);
		JSONObject json = new JSONObject();		
		json.put("htmlOptions", arrayHtml);
		json.put("numOptions", numOptions);
		
		// json.
		 response.setContentType("text/x-json;charset=ISO-8859-15");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
	}	
}
