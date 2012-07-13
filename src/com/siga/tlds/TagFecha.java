package com.siga.tlds;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

import java.io.PrintWriter;

/**
* Tag que implementa un textBox para fechas, con funcion de validacion asociada
* @author JBD
*/

public class TagFecha extends TagSupport {
	
	/**
	 * <siga:FechaBox
	 *		valorInicial="<%=fechaDesde()%>" 
	 *		required="TRUE"
	 *		formularioDatos="SolicitudesCertificadosForm" 
	 *		nombreCampo="fechaDese"
	 *  	/>
	 */
	
	private String nombreCampo = ""; 
	private String valorInicial = ""; 
	private String anchoTextField = "";
    private String necesario = ""; // Si fuese necesario no puede llevar el valor ""
    private String styleId;
    private String campoCargarFechaDesde;
    private String disabled;
	private String preFunction;
	private String postFunction;
	private String posicionX;
	private String posicionY;
	private String readOnly;
    
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	public void setNombreCampo(String nombre) {
		this.nombreCampo = nombre;
	}
	public void setNecesario(String necesario) {
		this.necesario = necesario;
	}
	public void setCampoCargarFechaDesde(String campoCargarFechaDesde) {
		this.campoCargarFechaDesde = campoCargarFechaDesde;
	}
    
	public int doStartTag() {
		try{

			pageContext.getResponse().setContentType("text/html");
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			PrintWriter out = pageContext.getResponse().getWriter();
			
			out.println("<!-- input de fecha con validacion -->");
			out.println("<script language=\"JavaScript\">");
			out.println("jQuery.noConflict(); ");
			out.println("jQuery(function() {");
			out.println("jQuery.maxZIndex = jQuery.fn.maxZIndex = function(opt) {");
			out.println("  ");
			out.println("	    var def = { inc: 10, group: \"*\" };");
			out.println("	    jQuery.extend(def, opt);");
			out.println("	    var zmax = 0;");
			out.println("	    jQuery(def.group).each(function() {");
			out.println("	        var cur = parseInt(jQuery(this).css('z-index'));");
			out.println("	        zmax = cur > zmax ? cur : zmax;");
			out.println("	     });");
			out.println("	    if (!this.jquery)");
			out.println("	         return zmax;");
			out.println("	    return this.each(function() {");
			out.println("	         zmax += def.inc;");
			out.println("	         jQuery(this).css(\"z-index\", zmax);");
			out.println("	      });");
			out.println("	  	}");
			out.println("	  ");
			out.println("jQuery(\"#invoke" + this.nombreCampo + "\").live(\"click\", function(){");
			out.println("	if(calendario==''){");
			out.println("    ");
			out.println("   	 calendar('#"+ this.nombreCampo +"');");
			out.println("		 calendario = '#"+ this.nombreCampo +"';");
			out.println("	}else{ ");
			out.println("	    ");
			out.println("		 if(calendario!='#"+ this.nombreCampo +"'){");
			out.println("		 	jQuery.datepicker._hideDatepicker();");
			out.println("	   		calendario = '';");
			out.println("	    }else{");			
			out.println("			calendario = '';}");
			out.println("} ");
			out.println("});");
						
			out.println("function calendar(a) {");	
			out.println("	jQuery( a ).datepicker(\"dialog\", \"\", updateDate,{");
			out.println("	showOn: 'focus',");			
			out.println("	buttonImage: '/SIGA/html/imagenes/calendar.gif',");		
			out.println("	buttonImageOnly: true,showOnFocus: false,");			
			out.println("	changeMonth: true,");	
			out.println("	yearRange: 'c-100:c+100',");	
			out.println("	showButtonPanel: true,");
			if ((this.valorInicial != "")&&(this.valorInicial != null))
				out.println("	defaultDate: '"+ this.valorInicial +"',");	
			out.println("	changeYear: true,");	
			out.println("	closeText: 'X', ");
			out.println("	showTrigger: '#calImg' ,");
			out.println("	alignment: 'bottomRight',");
			if ((this.disabled != null))
				if(this.disabled.equals("true"))
					out.println("	disabled: true,");
			out.println("		  currentText: 'Hoy',");
			out.println("		  firstDay: 1,");
			if ((this.campoCargarFechaDesde != "")&&(this.campoCargarFechaDesde != null)){
				out.println("//var lockDate = new Date(jQuery('#"+ this.campoCargarFechaDesde +"').datepicker('getDate'));");
				out.println("//		  minDate: lockDate,");
			}
			out.println("	dateFormat: 'dd/mm/yy',");
			out.println("	monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],");
			out.println("   dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],");
			out.println("	dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],");
			out.println("	dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sá'],");
		    out.println("		 	        ");
			out.println("	onSelect: function(dateText, inst) { calendario = '';  }, ");
		    out.println("		 	        ");
			out.println("	onClose:  function(dateText, inst) {");
			if ((this.getPostFunction() != "")&&(this.getPostFunction() != null)){
				out.println("	 "+this.getPostFunction()+"; ");
			}
			out.println("	},");
		    out.println("		 	        ");
			out.println("	beforeShow: function( input ) {");
			out.println("				jQuery('#ui-datepicker-div').maxZIndex();");
			if ((this.getPreFunction() != "")&&(this.getPreFunction() != null)){
				out.println("		 "+this.getPreFunction()+" ");
			}
			out.println("		 	  setTimeout(function() { ");  
			out.println("		 		var buttonPane = jQuery( input ).datepicker( \"widget\" ).find( \".ui-datepicker-buttonpane\" );   ");
		    out.println("		 	    var btn = jQuery('<BUTTON class=\"ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all\">Limpiar</BUTTON>');");   
		    out.println("		                  btn.unbind(\"click\").bind(\"click\", function () {  "); 
		    out.println("          				  jQuery.datepicker._clearDate( input ); });   ");
		    out.println("               btn.appendTo( buttonPane );");  
			out.println("          	  }, 1 );       ");
		    out.println("		 	  },        ");
		    out.println("		 	        ");
			out.println("	onChangeMonthYear: function( input ) {  ");
			out.println("		 	 setTimeout(function() { ");  
			out.println("		 		var buttonPane = jQuery( input ).datepicker( \"widget\" ).find( \".ui-datepicker-buttonpane\" );   ");
		    out.println("		 	    var btn = jQuery('<BUTTON class=\"ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all\">Limpiar</BUTTON>');");   
		    out.println("		                  btn.unbind(\"click\").bind(\"click\", function () {  "); 
		    out.println("                             jQuery.datepicker._clearDate( input );   });   ");
		    out.println("               btn.appendTo( buttonPane );");  
			out.println("          	  }, 1 );        ");
		    out.println("	} 	 	        ");			
			out.println("	},");
			out.println("		 	        ");
			if ((this.posicionX != "")&&(this.posicionX != null))
				out.println("["+this.posicionX+",");
			else
				out.println("[350,");
			out.println("		 	        ");
			if ((this.posicionY != "")&&(this.posicionY != null))
				out.println(this.posicionY+"]);");
			else
				out.println("150]);");
			
			out.println("function updateDate(date) {");
			out.println("	jQuery( '#"+ this.nombreCampo +"').val(date);");
			out.println("	calendario = '';");
			out.println("}");
			
			out.println("}");
			
			out.println("jQuery( '#"+ this.nombreCampo +"' ).hover(function() {     ");
			//out.println("	jQuery(this).datepicker(\"hide\"); ");
			out.println("});");		
			out.println("});");			


			out.println("function validaFecha"+ this.nombreCampo +"(field){ ");
			if (!((this.readOnly != null && this.readOnly.equals("true")) || (this.disabled != null  && this.disabled.equals("true")))){

			out.println("	var checkstr = \"0123456789\";");
			out.println("	var campoFecha = field;");
			out.println("	var fecha = \"\";");
			out.println("	var fechaTemp = \"\";");
			out.println("	var separador = \"/\";");
			out.println("	var day;");
			out.println("	var month;");
			out.println("	var year;");
			out.println("	var bisiesto = 0;");
			out.println("	var err = 0;");
			out.println("	var i;");
			out.println("	if (campoFecha!=null && campoFecha.value == \"\" ){");
			out.println("		err = 6;");
			out.println("	}else{");
			out.println("		err = 0;");
			out.println("		fecha = campoFecha.value;");
			out.println("		// Convierte los caracteres no numericos en separadores");
			out.println("		for (i = 0; i < fecha.length; i++) {");
			out.println("			if (checkstr.indexOf(fecha.substr(i,1)) >= 0) {");
			out.println("				fechaTemp = fechaTemp + fecha.substr(i,1);");
			out.println("			}else{");
			out.println("				fechaTemp = fechaTemp + separador;");
			out.println("			}");
			out.println("		}");
			out.println("		fecha = fechaTemp;");
			out.println("		var pos1=fecha.indexOf(separador);");
			out.println("		var pos2=fecha.indexOf(separador,pos1+1);");
			out.println("		var pos3=fecha.indexOf(separador,pos2+1); // No deberia existir.");
			out.println("		if ((pos1>pos2) || (pos3>0)){ err=5; }");
			out.println("		day=fecha.substring(0,pos1);");
			out.println("		month=fecha.substring(pos1+1,pos2)");
			out.println("		year=fecha.substring(pos2+1)");
			out.println("		if (year.length == 1) {");
			out.println("			year = '200' + year; } // Si el año es un solo digito se asume 200x");
			out.println("		if (year.length == 2) {");
			out.println("			year = '20' + year; } // Si el año son solo 2 digitos se asume 20xx");
			out.println("		if ((year < 1900) || (year > 2999)) {");
			out.println("			err = 1;");
			out.println("		} // Codigo de error 1 corresponde a año incorrecto");
			out.println("		if ((day==\"\") || (month==\"\") || (year==\"\")){ err=5;}");
			out.println("		// Validacion del campo mes");
			out.println("		if ((err == 0)&&((month < 1) || (month > 12))) { ");
			out.println("			err = 2; } // Codigo de error 2 corresponde a mes incorrecto");
			out.println("		// Validacion del campo dia");
			out.println("		if ((err == 0)&&((day < 1) || (day > 31))){ ");
			out.println("			err = 3; } // Codigo de error 3 corresponde a dia incorrecto");
			out.println("		// Validacion 29 febrero");
			out.println("		if ((year % 4 == 0) || (year % 100 == 0) || (year % 400 == 0)) { bisiesto = 1; }");
			out.println("		if ((err == 0)&&((month == 2) && (bisiesto == 1) && (day > 29))) { err = 4; } // Codigo de error 4 corresponde a fecha no valida");
			out.println("		if ((err == 0)&&((month == 2) && (bisiesto != 1) && (day > 28))) { ");
			out.println("			err = 4; }");
			out.println("		// Validacion del resto de meses");
			out.println("		if ((err == 0)&&((day > 31) && ((month == \"01\") || (month == \"03\") || (month == \"05\") || (month == \"07\") || (month == \"08\") || (month == \"10\") || (month == \"12\")))) {");
			out.println("			err = 4; }");
			out.println("		if ((err == 0)&&((day > 30) && ((month == \"04\") || (month == \"06\") || (month == \"09\") || (month == \"11\")))) {");
			out.println("			err = 4; }");
			out.println("	}");
			out.println("	// Si no hay error se deja la fecha correctamente formateada");
			out.println("	if (err == 0) {");
			out.println("		if (day.length==1) day=\"0\" + day;");
			out.println("		if (month.length==1) month=\"0\" + month;");
			out.println("		campoFecha.value = day + separador + month + separador + year;");
			out.println("	}else if (err == 6) {");
			// Si el campo se ha marcado como necesario se incluye la clausula que no permita que el campo quede vacio
			if(this.necesario.equalsIgnoreCase("true")){
				out.println("		alert(\"" + UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"fecha.error.campo.necesario") + "\");");
				out.println("		campoFecha.select();");
				out.println("		campoFecha.focus();");
			}
			out.println("	}else {");
			out.println("		// Si hay un error muestra mensaje de fecha erronea");
			out.println("		if (err ==5){alert(\"" + UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"fecha.error.formato") + "\") }");
			out.println("		else{");
			out.println("			if (day.length==1) day=\"0\" + day;");
			out.println("			if (month.length==1) month=\"0\" + month;");
			out.println("			campoFecha.value = day + separador + month + separador + year;");
			out.println("			if (err ==4){alert(\"" + UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"fecha.error.valida") + "\") }");
			out.println("			if (err ==2){alert(\"" + UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"fecha.error.mes") + "\") }");
			out.println("			if (err ==1){alert(\"" + UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"fecha.error.anio") + "\") }");
			out.println("			if (err ==3){alert(\"" + UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"fecha.error.dia") + "\") }");
			out.println("		}");
			out.println("		// Finalmente seleccion la fecha incorrecta y devuelve el foco al campo fecha");
			out.println("		campoFecha.select();");
			out.println("		campoFecha.focus();");
			out.println("	}");
			}
			out.println("}");
			out.println("</script>");

			out.println(""); // Linea vacia por legibilidad del codigo
			out.println("<input type=\"text\" name=\"" + this.nombreCampo + "\" id=\"" + this.nombreCampo + "\" ");
			// out.println("	property=\"" + this.nombreCampo + "\" ");
			if(anchoTextField!=null && !anchoTextField.equals("")){
				out.println("	size=\""+anchoTextField+"\""+" maxlength=\"10\" ");
			}else{
				out.println("	size=\"10\" maxlength=\"10\" ");
			}
			if ((this.valorInicial != "")&&(this.valorInicial != null)){
				out.println("		alert(this.valorInicial); ");
				out.println("	value=\"" + this.valorInicial + "\" ");
			}
			if ((this.styleId != null)&&(this.styleId.equals(""))){
				out.println("		styleId =\"" + this.styleId + "\" ");
			}
			if ((this.disabled != null)){
				if(this.disabled.equals("true"))
					out.println("		class = 'boxConsulta' ");
				else
					out.println("		class = 'box' ");
			}else{
				out.println("		class = 'box' ");
				
			}
			if ((this.readOnly != null && this.readOnly.equals("true")) || (this.disabled != null  && this.disabled.equals("true"))){
					out.println("readOnly='true' ");
			}
			if(this.getPreFunction()!=null && !this.getPreFunction().equals("")){
				out.println("   onfocus=\"return "+	this.getPreFunction()+"\"");
			}
			if(this.getPostFunction()!=null && !this.getPostFunction().equals("")){
				out.println("	onblur=\""+	this.getPostFunction()+"return validaFecha"+ this.nombreCampo +"(" + this.nombreCampo + ");\"/> ");
			}else{
				out.println("	onblur=\"return validaFecha"+ this.nombreCampo +"(" + this.nombreCampo + ");\"/>");
			}
			if ((this.disabled != null)){
				if(this.disabled.equals("true")){
					out.println("<input type=\"hidden\" name=\"" + this.nombreCampo + "\" id=\"" + this.nombreCampo + "\"" );
							if(this.valorInicial!=null && !this.valorInicial.equals(""))		
								out.println(" value=\"" +this.valorInicial + "\" />");
							else
								out.println("  />");
				}			
			}else{
				out.println("<a id=\"invoke" + this.nombreCampo + "\" title=\"\" href=\"#\"><img src=\"/SIGA/html/imagenes/calendar.gif\" border=\"0\"> </a>");
			}
			//out.println("/> ");
			out.println(""); // Linea vacia por legibilidad del codigo
		}catch (Exception e){
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	public String getAnchoTextField() {
		return anchoTextField;
	}
	public void setAnchoTextField(String anchoTextField) {
		this.anchoTextField = anchoTextField;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	public String getPreFunction() {
		return preFunction;
	}
	public void setPreFunction(String preFunction) {
		this.preFunction = preFunction;
	}
	public String getPostFunction() {
		return postFunction;
	}
	public void setPostFunction(String postFunction) {
		this.postFunction = postFunction;
	}
	public String getPosicionX() {
		return posicionX;
	}
	public void setPosicionX(String posicionX) {
		this.posicionX = posicionX;
	}
	public String getPosicionY() {
		return posicionY;
	}
	public void setPosicionY(String posicionY) {
		this.posicionY = posicionY;
	}

}