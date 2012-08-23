package com.siga.general;

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
	    	codigosBind.put(1, '*');
	    } else {		    		    	
	    	sql+=", LENGTH(FILTRO.VALOR)-LENGTH("+campoTabla+"."+campoNombre+") AS SELECCIONADO ";
	    	codigosBind.put(1, valorFiltro);
	    }		    
	    		
	    sql+=" FROM " + campoTabla+
	    	", (SELECT :1 AS VALOR FROM DUAL) FILTRO "+
	    	" WHERE REGEXP_LIKE("+campoTabla+"."+campoNombre+", FILTRO.VALOR) ";

	    if (valorIdentificadorPadre!=null&&campoIdentificadorPadre!=null) {
	    	sql+=" AND "+campoTabla+"."+campoIdentificadorPadre+"=:2 ";
	    	codigosBind.put(2, valorIdentificadorPadre);
	    }
	    
	    if (tieneCampoPrioridad)
	    	sql+=" ORDER BY "+campoTabla+"."+campoPrioridad+" ASC, "+
	    		campoTabla+"."+campoNombre+" ASC ";
	    else
	    	sql+=" ORDER BY "+campoTabla+"."+campoNombre+" ASC ";	    
	    
	    rc = new RowsContainer();

        rc.findNLSBind(sql,codigosBind);    

		String htmlOptions="";
		Integer numOptions = 0;
		
		if (rc!=null) {
			if (rc.size()<=numeroMaximoOpciones) {				
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
			} else if (tieneCampoPrioridad) {
				Boolean bSeleccionado = false;
				Boolean bPrioridad = true;
				htmlOptions="<option value=''>"+UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar")+"</option>";
				numOptions++;
				
				for (int i = 0; i < rc.size()&&bPrioridad; i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					
					if (registro != null) {
						Integer prioridad = UtilidadesHash.getInteger(registro, campoPrioridad);
						
						if (prioridad!=null) {							
							if (!bSeleccionado && UtilidadesHash.getInteger(registro, "SELECCIONADO")==0) {
								bSeleccionado=true;
								htmlOptions+="<option selected value='"+UtilidadesHash.getString(registro, campoIdentificador)+"'>"+
										UtilidadesHash.getString(registro, campoNombre)+"</option>";
							}
							else {
								htmlOptions+="<option value='"+UtilidadesHash.getString(registro, campoIdentificador)+"'>"+
									UtilidadesHash.getString(registro, campoNombre)+"</option>";
							}
							numOptions++;
							
						}
						else {
							bPrioridad=false;
						}
					}
				}
			}
		}		
		
		JSONObject json = new JSONObject();		
		json.put("htmlOptions", htmlOptions);
		json.put("numOptions", numOptions);
		
		// json.
		 response.setContentType("text/x-json;charset=ISO-8859-15");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
	}	
}
