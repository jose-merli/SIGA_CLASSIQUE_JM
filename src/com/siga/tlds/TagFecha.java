package com.siga.tlds;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Tag que implementa un textBox para fechas, con funcion de validacion asociada
* @author JBD
*/
public class TagFecha extends TagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6112383609745590820L;
	/**
	 * <siga:FechaBox
	 *		valorInicial="<%=fechaDesde()%>" 
	 *		required="TRUE"
	 *		formularioDatos="SolicitudesCertificadosForm" 
	 *		nombreCampo="fechaDese"
	 *  	/>
	 */
	// OJO, LOS FORMATOS DE DATEPICKER SON DISTINTOS (ej: yy=yyyy) http://api.jqueryui.com/datepicker/#utility-formatDate
	// SI SE QUIERE CAMBIAR EL FORMATO HAY QUE CAMBIAR LOS DOS: 
	//	DATE_FORMAT => PARA EL SIMPLEDATEFORMAT (Y TEXTO DE ERROR)
	//	DATEPICKER_DATE_FORMAT => PARA EL DATEPICKER
	private static final String DATE_FORMAT = "dd/mm/yyyy";
	private static final String DATEPICKER_DATE_FORMAT = "dd/mm/yy";
	
	private String nombreCampo = ""; 
	private String valorInicial = ""; 
	private String anchoTextField = "10";
    private String necesario = ""; // Si fuese necesario no puede llevar el valor ""
    private String styleId;
    private String campoCargarFechaDesde;
    private String disabled;
	private String preFunction;
	private String postFunction;
	private String posicionX;
	private String posicionY;
	private String readOnly;
	private String atributos;
    
	@Override
	public int doStartTag() {
		try{
			pageContext.getResponse().setContentType("text/html");
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			if (usrbean==null){
				usrbean= new UsrBean();
				usrbean.setLanguage("1");
			}
			PrintWriter out = pageContext.getResponse().getWriter();
			
			out.println("<!-- input de fecha con validacion -->");
			out.println("<script language=\"JavaScript\">");
			
			out.println("function validaFecha"+ this.nombreCampo +"(field){ ");
			
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
			out.println("	if (campoFecha.value == \"\"){");
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
			out.println("		campoFecha.value='';");
			out.println("		campoFecha.select();");
			out.println("		campoFecha.focus();");
			out.println("	}");
			out.println("	return err;");
			out.println("}");
			out.println("</script>");
			out.println(""); // Linea vacia por legibilidad del codigo
			
			if (this.nombreCampo == null || "".equals(this.nombreCampo)){
				if (this.styleId == null || this.styleId.equals(""))
					this.nombreCampo = "datepicker";
				else
					this.nombreCampo = this.styleId;
			}
			if(this.styleId == null || this.styleId.equals("")){
				this.styleId = this.nombreCampo;
			}			
			String sDatepicker = "<input autocomplete='off' type='text' id='"+this.styleId+"' name='"+this.nombreCampo+"' maxlength='11'";
			if (this.atributos != null && !this.atributos.equals("")){
				sDatepicker += " " + this.atributos;
			}
			if(this.anchoTextField!=null && !this.anchoTextField.equals("")){
				sDatepicker += " size='"+anchoTextField+"'";
			} else {
				sDatepicker += " size='10'";
			}
			if (this.valorInicial != null && !this.valorInicial.equals("")){
				try{
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
					Date fechaValorInicial = dateFormat.parse(this.valorInicial);
					sDatepicker += " value='"+dateFormat.format(fechaValorInicial)+"'";
				} catch(Exception e){
					// No ponemos valor inicial
				}
			}
			String cssClass = " class='tcal";
			if (this.disabled != null && UtilidadesString.stringToBoolean(this.disabled)){
				//bEditable = false;
				cssClass += " boxConsulta noEditable";
				sDatepicker += " readonly = 'readonly'";				
			} else {
				cssClass += " box editable";
			}
			cssClass += "'";
			sDatepicker += cssClass;
						
			if(this.preFunction!=null && !this.preFunction.equals("")){
				sDatepicker += " onfocus=\"return "+	this.preFunction+"\"";
			}
			if(this.postFunction!=null && !this.postFunction.equals("")){
				sDatepicker += " onchange=\"return "+	this.postFunction+"\"";
			}
			if (this.campoCargarFechaDesde != null && !this.campoCargarFechaDesde.equals("")){
				sDatepicker += " data-cargarfechadesde=\""+	this.campoCargarFechaDesde+"\"";
			}
			
			//Validación fecha
			sDatepicker += " onblur=\" return validaFecha"+ this.nombreCampo +"(this)\"";
			sDatepicker += " onkeyup=\" f_tcalUpdate (this.value, true);\"";
			sDatepicker += " onkeydown=\" return isNumberKey(event);\"";
			
			sDatepicker += " data-format=\""+	DATE_FORMAT +"\"";
			sDatepicker += " data-datepickerformat=\""+	DATEPICKER_DATE_FORMAT +"\"";
			//TODO: SELECCIONAR IDIOMA DEL USUARIO DEFINIDO EN SIGA.JS
			String regional = "es";
			regional = usrbean.getLanguageExt().toLowerCase();
			sDatepicker += " data-regional=\""+	regional +"\"";
			sDatepicker += " />";
			sDatepicker += "<script>jQuery('#"+ this.styleId +"').parent().css('white-space','nowrap');</script>";
			out.print(sDatepicker);	
			
			// Pintamos las variables según el idioma
			out.println("<script language=\"JavaScript\">");
			
			if("ca".equalsIgnoreCase(regional)){
				out.println("var A_TCALCONF = {");
				out.println("	'cssprefix'  : 'tcal',");
				out.println("	'months'     : ['Gener', 'Febrer', 'Març', 'Abril', 'Maig', 'Juny', 'Juliol', 'Agost', 'Setembre', 'Octubre', 'Novembre', 'Decembre'],");
				out.println("	'weekdays'   : ['Dg','Dl','Dt','Dc','Dj','Dv','Ds'],");
				out.println("	'longwdays'  : ['Diumenge', 'Dilluns', 'Dimarts', 'Dimecres', 'Dijous', 'Divendres', 'Divendres'],");
				out.println("	'yearscroll' : true, // show year scroller");
				out.println("	'weekstart'  : 1, // first day of week: 0-Su or 1-Mo");
				out.println("	'prevyear'   : 'Any Previ',");
				out.println("	'nextyear'   : 'Any Següent',");
				out.println("	'prevmonth'  : 'Mes Previ',");
				out.println("	'nextmonth'  : 'Mes Següent',");
				out.println("	'format'     : 'd/m/Y' //'m/d/Y' // 'd-m-Y', Y-m-d', 'l, F jS Y'");
				out.println("};");
				out.println("var textoBorrar = 'Esborrar';var textoCerrar = 'Tancar';var textoHoy='Avui'");
			}else if("en".equalsIgnoreCase(regional)){
				out.println("var A_TCALCONF = {");
				out.println("	'cssprefix'  : 'tcal',");
				out.println("	'months'     : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],");
				out.println("	'weekdays'   : ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],");
				out.println("	'longwdays'  : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],");
				out.println("	'yearscroll' : true, // show year scroller");
				out.println("	'weekstart'  : 1, // first day of week: 0-Su or 1-Mo");
				out.println("	'prevyear'   : 'Previous Year',");
				out.println("	'nextyear'   : 'Next Year',");
				out.println("	'prevmonth'  : 'Previous Month',");
				out.println("	'nextmonth'  : 'Next Month',");
				out.println("	'format'     : 'd/m/Y' //'m/d/Y' // 'd-m-Y', Y-m-d', 'l, F jS Y'");
				out.println("};");
				out.println("var textoBorrar = 'Clean';var textoCerrar = 'Close';var textoHoy='Today'");
			}else{
				out.println("var A_TCALCONF = {");
				out.println("	'cssprefix'  : 'tcal',");
				out.println("	'months'     : ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],");
				out.println("	'weekdays'   : ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],");
				out.println("	'longwdays'  : ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sabado'],");
				out.println("	'yearscroll' : true, // show year scroller");
				out.println("	'weekstart'  : 1, // first day of week: 0-Su or 1-Mo");
				out.println("	'prevyear'   : 'Año Previo',");
				out.println("	'nextyear'   : 'Año Siguiente',");
				out.println("	'prevmonth'  : 'Mes Previo',");
				out.println("	'nextmonth'  : 'Mes Siguiente',");
				out.println("	'format'     : 'd/m/Y' //'m/d/Y' // 'd-m-Y', Y-m-d', 'l, F jS Y'");
				out.println("};");
				out.println("var textoBorrar = 'Borrar';var textoCerrar = 'Cerrar';var textoHoy='Hoy'");
			}
			

			out.println("var A_TCALTOKENS = [");
			out.println("	 // A full numeric representation of a year, 4 digits");
			out.println("	{'t': 'Y', 'r': '19\\\\d{2}|20\\\\d{2}', 'p': function (d_date, n_value) { d_date.setFullYear(Number(n_value)); return d_date; }, 'g': function (d_date) { var n_year = d_date.getFullYear(); return n_year; }},");
			out.println("	 // Numeric representation of a month, with leading zeros");
			out.println("	{'t': 'm', 'r': '0?[1-9]|1[0-2]', 'p': function (d_date, n_value) { d_date.setMonth(Number(n_value) - 1); return d_date; }, 'g': function (d_date) { var n_month = d_date.getMonth() + 1; return (n_month < 10 ? '0' : '') + n_month }},");
			out.println("	 // A full textual representation of a month, such as January or March");
			out.println("	{'t': 'F', 'r': A_TCALCONF.months.join('|'), 'p': function (d_date, s_value) { for (var m = 0; m < 12; m++) if (A_TCALCONF.months[m] == s_value) { d_date.setMonth(m); return d_date; }}, 'g': function (d_date) { return A_TCALCONF.months[d_date.getMonth()]; }},");
			out.println("	 // Day of the month, 2 digits with leading zeros");
			out.println("	{'t': 'd', 'r': '0?[1-9]|[12][0-9]|3[01]', 'p': function (d_date, n_value) { d_date.setDate(Number(n_value)); if (d_date.getDate() != n_value) d_date.setDate(0); return d_date }, 'g': function (d_date) { var n_date = d_date.getDate(); return (n_date < 10 ? '0' : '') + n_date; }},");
			out.println("	// Day of the month without leading zeros");
			out.println("	{'t': 'j', 'r': '0?[1-9]|[12][0-9]|3[01]', 'p': function (d_date, n_value) { d_date.setDate(Number(n_value)); if (d_date.getDate() != n_value) d_date.setDate(0); return d_date }, 'g': function (d_date) { var n_date = d_date.getDate(); return n_date; }},");
			out.println("	 // A full textual representation of the day of the week");
			out.println("	{'t': 'l', 'r': A_TCALCONF.longwdays.join('|'), 'p': function (d_date, s_value) { return d_date }, 'g': function (d_date) { return A_TCALCONF.longwdays[d_date.getDay()]; }},");
			out.println("	// English ordinal suffix for the day of the month, 2 characters");
			out.println("	{'t': 'S', 'r': 'st|nd|rd|th', 'p': function (d_date, s_value) { return d_date }, 'g': function (d_date) { n_date = d_date.getDate(); if (n_date % 10 == 1 && n_date != 11) return 'st'; if (n_date % 10 == 2 && n_date != 12) return 'nd'; if (n_date % 10 == 3 && n_date != 13) return 'rd'; return 'th'; }}");
			out.println("	");
			out.println("];");
			out.println("</script>");
			out.println(""); // Linea vacia por legibilidad del codigo
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE; 	
	}
	
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

	/**
	 * @return the atributos
	 */
	public String getAtributos() {
		return atributos;
	}

	/**
	 * @param atributos the atributos to set
	 */
	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	/**
	 * @return the nombreCampo
	 */
	public String getNombreCampo() {
		return nombreCampo;
	}

	/**
	 * @return the valorInicial
	 */
	public String getValorInicial() {
		return valorInicial;
	}

	/**
	 * @return the necesario
	 */
	public String getNecesario() {
		return necesario;
	}

	/**
	 * @return the campoCargarFechaDesde
	 */
	public String getCampoCargarFechaDesde() {
		return campoCargarFechaDesde;
	}

}