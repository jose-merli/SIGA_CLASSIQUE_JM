package com.siga.tlds;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;


/**
 * Genera una tabla con los parámetros proporcionados:
 * - TODO: completar
 * 
 * @author borjans
 *
 */
@SuppressWarnings("serial")
public class TagTable extends TagSupport {
	
	private static final String 	DEFAULT_NAME 		= "tabla";
	private static final String 	DEFAULT_BORDER 		= "0";
	private static final String 	DEFAULT_WIDTH 		= "100%";
	private static final String 	DEFAULT_COLUMN_NAME	= "Columna";
	private static final String 	DEFAULT_COLUMN_SIZE	= "10";
	private static final String		DEFAULT_FIXED_HEIGHT= "";
	
	private String name 	= DEFAULT_NAME;
	private String border 	= DEFAULT_BORDER;
	private String width	= DEFAULT_WIDTH;
	
	private String [] 	columnNames 		= null;
	private String [] 	columnSizes 		= null;
	private int			columnNum 			= 0;
	private boolean		showHeader 			= true;
	private boolean		showFooter 			= false;
	private String		modal 				= "";
	private boolean		modalScroll 		= false;
	private String 		mensajeBorrado		= null;
	private boolean 	activateSelectedRow = true;
	private String		fixedHeight			= DEFAULT_FIXED_HEIGHT;
	
	private HttpSession session 			= null;
	private UsrBean		userBean			= null;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			PrintWriter out = pageContext.getResponse().getWriter();
			
			out.println("<!-- TagTable.doStartTag BEGIN -->");				
			
			if (this.width == null || "".equals(this.width)){
				this.width = "100%";
			}
			//out.println("<div id='"+this.name+"_div' style='width: "+this.width+"; heigh=100%'>");
			//out.println("<table id='"+this.name+"' name='"+this.name+"' border='" + this.border + "' width='"+this.width+"' class='fixedHeaderTable dataScroll' style='table-layout: fixed;'>");
			out.println("<table id='"+this.name+"' name='"+this.name+"' width='"+this.width+"' class='fixedHeaderTable dataScroll' style='table-layout: fixed; border-spacing: 0px; visibility:hidden;'>");
			
			if (this.columnNames != null || this.columnNum > 0){
				if (this.columnNum <= 0){
					this.columnNum = this.columnNames.length;
				}
				StringBuilder sbThead = new StringBuilder();
				StringBuilder sbTfoot = new StringBuilder();
				//StringBuilder sbTbody = new StringBuilder();
				sbThead.append("<tr class='tableTitle'>\n");
				sbTfoot.append("<tr>\n");
				//sbTbody.append("<tr>\n");
				
				for (int i = 0; i < this.columnNum; i++){
					String columnName = DEFAULT_COLUMN_NAME+i;
					String columnSize = "";
					
					if (this.columnNames != null && this.columnNames.length > i){
						columnName = this.columnNames[i];
					}					
					if (this.columnSizes != null && this.columnSizes.length > i){
						columnSize = "width: "+this.columnSizes[i]+"%;";
					}
					String columnStyle = "style='text-align:center; "+columnSize+"'";
					
					sbThead.append("<th "+columnStyle+">\n");
					sbThead.append((columnName.equalsIgnoreCase("&nbsp;")?"&nbsp;":UtilidadesString.getMensajeIdioma(getUserBean(), columnName.trim()))+"\n");
					sbThead.append("</th>\n");
					
					sbTfoot.append("<td "+columnStyle+">\n");
					sbTfoot.append("</td>\n");
					/*
					sbTbody.append("<td "+columnStyle+">\n");
					sbTbody.append("</td>\n");
					*/
				}
				
				sbThead.append("</tr>\n");
				sbTfoot.append("</tr>\n");
				//sbTbody.append("</tr>\n");
				
				if (this.showHeader){
					out.println();
					out.println("<thead class='Cabeceras' style='text-align:center; '>");
					out.print(sbThead.toString());
					out.println("</thead>");
					out.println();
				}
				
				if (this.showFooter){
					out.println();
					out.println("<tfoot style='text-align:center; '>");
					out.print(sbTfoot.toString());
					out.println("</tfoot>");
					out.println();
				}
				
				out.println();
				out.println("<tbody style='text-align:center; overflow-y: scroll; overflow-x: hidden; margin:0px;'>");
				//out.print(sbTbody.toString());
				out.println();
			} else
				throw new Exception("No column names or number of columns found");
			
			out.println("<!-- TagTable.doStartTag END -->");
		} catch (Exception e) {
			throw new JspException("TagTable.doStartTag ERROR", e);
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		try {
			PrintWriter out = pageContext.getResponse().getWriter();
			out.println("<!-- TagTable.doEndTag BEGIN -->");
			out.println("</tbody>");
			out.println("</table>");
			//out.println("</div>");
			out.println();
			out.println("<script type='text/javascript'>");
			out.println("jQuery(document).ready(function(){");
			out.print("    loadFixedHeaderTables ('"+this.name+"'");
			if (this.fixedHeight == DEFAULT_FIXED_HEIGHT)
				out.println(");");
			else
				out.println(", '"+this.fixedHeight+"');");
			out.println("    jQuery('form:first',document).append('<input type=\"hidden\" name=\"tablaDatosDinamicosD\" id=\"tablaDatosDinamicosD\" />');");
			out.println("    jQuery('form:first',document).append('<input type=\"hidden\" name=\"filaSelD\" id=\"filaSelD\" />');  ");
			if (!this.modal.equals("")) 
				out.println("    jQuery('form:first',document).append('<input type=\"hidden\" name=\"actionModal\" id=\"actionModal\">');  ");
			out.println("");
			int filaSeleccionada = this.getFilaSeleccionada();
			if (filaSeleccionada > -1) {
				out.println("  var tabla;");
				out.println("  tabla = document.getElementById('" + this.name + "');");
				out.println("  if ((tabla.rows.length - 1) >= "+ filaSeleccionada + ") {");
			    out.println("     selectRow(" + filaSeleccionada + "); ");
				out.println("     tabla.rows["+ filaSeleccionada + "].scrollIntoView(false);");
				out.println("  }");
			}
			out.println("});");
			// FUNCIONES
			// FUNCIÓN SELECTROW
			out.println(" function selectRow(fila, id) {");
			out.println("	if (typeof id == 'undefined')");
			out.println("		id='"+this.name+"';");
			out.println("   if(document.getElementById('filaSelD')) ");
			out.println("   	document.getElementById('filaSelD').value = fila;");
			out.println("	var iFila = fila -1;");
			out.println("	jQuery('#'+id+'_BodyDiv').find('tbody').find('.filaTablaPar').removeClass('filaTablaPar').addClass('filaTabla'); ");
			out.println("	jQuery('#'+id+'_BodyDiv').find('tbody').find('.filaTablaImpar').removeClass('filaTablaImpar').addClass('filaTabla'); ");
			out.println("   jQuery('#'+id+'_BodyDiv').find('tbody').find('.filaTabla:visible:odd').addClass('filaTablaPar'); ");
			out.println("   jQuery('#'+id+'_BodyDiv').find('tbody').find('.filaTabla:visible:even').addClass('filaTablaImpar'); ");
			out.println("	jQuery('#'+id+'_BodyDiv').find('tbody').find('.listaNonEditSelected').removeClass('listaNonEditSelected'); ");
			out.println("   if (iFila >= 0) jQuery('#'+id+'_BodyDiv').find('tbody').find('tr:eq('+iFila+')').addClass('listaNonEditSelected')");
//out.println("alert('Marco seleccionada ' + iFila + ' encontradas: ' + jQuery('#BodyDiv').find('tbody').find('tr:eq('+iFila+')'));");
			out.println(" }");
			out.println("");

			// FUNCIÓN AUXILIAR PARA PREPARAR DATOS
			out.println("	function preparaDatos(fila, tableId, datos){");
			out.println("   var iFila = fila -1;");
			out.println("   if (!datos)");
			out.println("   	datos = document.getElementById('tablaDatosDinamicosD');");
			out.println("   datos.value = \"\"; ");
			out.println("   var ocultos_tratados = false;");
//out.println("alert('consultar iFila: ' + iFila);");
			//out.println("	jQuery('#'+tableId+'_BodyDiv').find('tbody').find('tr:eq('+iFila+')').find('td').each(function(){");
			out.println("	jQuery('#'+tableId+'_BodyDiv').find('tbody').find('tr:eq('+iFila+')').find('td').each(function(){");
//out.println("alert('Tratando columna ' + jQuery(this).html());");
//out.println("alert('OCULTOS ENCONTRADOS: ' + jQuery(this).find('*[name^=\"oculto' + fila + '_\"]').length);");
			out.println("		var appendDatos = false;");
			out.println("       if (!ocultos_tratados){");
			out.println("		jQuery(this).parent().find('input[name^=\"oculto' + fila + '_\"], input[id^=\"oculto' + fila + '_\"]').each(function(){");
//out.println("alert('Encontrado campo oculto ' + jQuery(this).val());");
			out.println("			appendDatos = true;");
			out.println("			ocultos_tratados = true;");
			out.println("			if (jQuery(this).val() == '') jQuery(this).val(' ');");
			out.println("			datos.value = datos.value + jQuery(this).val() + ',';");
//out.println("alert('datos.value ' + datos.value);");
			out.println("		});");
			out.println("       }");
			out.println("		if (appendDatos) datos.value = datos.value + \"%\";");
			out.println("		var j = 2;");
			out.println("		if (jQuery(this)[0].cellIndex == 0) j = jQuery(this).find('*[name^=\"oculto' + fila + '_\"]').length;");
			out.println("		if (jQuery(this).html()== \"\")");
			out.println("       	datos.value = datos.value + jQuery(this)[0].childNodes[j-2].value + ',';");
			out.println("		else");
			out.println("        	datos.value = datos.value + jQuery(this).html().replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';");
//out.println("alert('datos.value fin iteración columna ' + datos.value);");
			out.println("	});");
			out.println("	}");
			out.println();
			
			// FUNCIÓN CONSULTAR
			out.println(" function "+ TagFila.accConsultar + "(fila, id) {");
			out.println("	if (typeof id == 'undefined')");
			out.println("		id='"+this.name+"';");
			out.println("	preparaDatos(fila,id);");
			out.println("   document.forms[0].modo.value = \"Ver\";");
			if (!this.modal.equals("")) {
//out.println("alert('llamando a ventaModalGeneral...');");
				if (this.modalScroll) {
					out.println("   ventaModalGeneralScrollAuto(document.forms[0].name,\""+this.modal+"\");");
				} else {
					out.println("   ventaModalGeneral(document.forms[0].name,\""+this.modal+"\");");
				}
			} else {
//out.println("alert('Haciendo submit a ' + document.forms[0].action);");
				out.println("   document.forms[0].submit();");
			}
			out.println(" }");
			out.println("");

			// FUNCIÓN EDITAR
			out.println(" function " + TagFila.accEditar + "(fila, id) {");
			out.println("	if (typeof id == 'undefined')");
			out.println("		id='"+this.name+"';");
			out.println("	preparaDatos(fila, id);");
			out.println("   document.forms[0].modo.value = \"Editar\";");
			if (!this.modal.equals("")) {
				if (this.modalScroll) {
					out.println("   var resultado = ventaModalGeneralScrollAuto(document.forms[0].name,\""+this.modal+"\");");
				} else {
					out.println("   var resultado = ventaModalGeneral(document.forms[0].name,\""+this.modal+"\");");
				}
				out.println("   if (resultado) {");
				out.println("  	 	if (resultado==\"MODIFICADO\") {");
				out.println("   	    alert(\""+UtilidadesString.getMensajeIdioma(getUserBean(),"messages.updated.success")+"\",'success');");
				out.println("   		refrescarLocal();");
				out.println("       } else if (resultado==\"NORMAL\") {");
				out.println("       } else if (resultado[0]) {");
				out.println("   	    alert(\""+UtilidadesString.getMensajeIdioma(getUserBean(),"messages.updated.success")+"\",'success');");
				out.println("      		refrescarLocalArray(resultado);");
				out.println("   	}");
				out.println("   }");
			} else { 
				out.println("   document.forms[0].submit();");
			}
			out.println(" }");
			out.println("");

			// FUNCIÓN BORRAR
			out.println(" function " + TagFila.accBorrar + "(fila, id) {");
			out.println("	if (typeof id == 'undefined')");
			out.println("		id='"+this.name+"';");
			out.println("   var datos;");
			String mensaje = UtilidadesString.getMensajeIdioma(getUserBean(),"messages.deleteConfirmation");
			if (getMensajeBorrado() != null) {
				mensaje = UtilidadesString.getMensajeIdioma(getUserBean(), getMensajeBorrado());
			}
			out.println("   if (confirm(\'"+ mensaje + "\')){");			
			out.println("		preparaDatos(fila, id);");
			out.println("   	var auxTarget = document.forms[0].target;");
			out.println("   	document.forms[0].target=\"submitArea\";");
			out.println("   	document.forms[0].modo.value = \"Borrar\";");
			out.println("   	document.forms[0].submit();");			
			out.println("   	document.forms[0].target=auxTarget;");
			out.println(" 	}");
			out.println(" }");
			out.println("</script>");
			out.println("<!-- TagTable.doEndTag END -->");
		} catch (Exception e) {
			throw new JspException("TagTable.doEndTag ERROR", e);
		}
		return EVAL_PAGE;
	}

	protected int getFilaSeleccionada () {
		try {
			if (this.activateSelectedRow) {				
				int fila = Integer.parseInt(((String)getSession().getAttribute("FILA_SELECCIONADA")));
				session.removeAttribute("FILA_SELECCIONADA");
				return fila;
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}
	
	protected HttpSession getSession(){
		if (this.session == null)
			this.session = pageContext.getSession();
		return this.session;
	}
	
	protected UsrBean getUserBean(){
		if (this.userBean == null)
			this.userBean = (UsrBean)getSession().getAttribute(ClsConstants.USERBEAN);
		return this.userBean;
	}
	
	//*** GETTERS && SETTERS ***//
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		if (columnNames.endsWith(",")){
			columnNames+="&nbsp;";
		}
		if (columnNames != null && !"".equals(columnNames))
			this.columnNames = columnNames.split(",");
	}

	public String[] getColumnSizes() {
		return columnSizes;
	}

	public void setColumnSizes(String columnSizes) {
		columnSizes = columnSizes.trim();
		StringTokenizer datos = new StringTokenizer(columnSizes, ",");
		ArrayList<String> arlTmp = new ArrayList<String>();
		while (datos.hasMoreElements()) {
			String tamano = datos.nextToken();
			try {
				arlTmp.add(String.valueOf(Integer.parseInt(tamano)));
			} catch (Exception e) {
				arlTmp.add(DEFAULT_COLUMN_SIZE);
			}
		}
		if(arlTmp.size() > 0){
			this.columnSizes = new String[arlTmp.size()];
			this.columnSizes = arlTmp.toArray(this.columnSizes);
		}
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(String showHeader) {
		if (showHeader != null && !"".equals(showHeader)){
			showHeader = showHeader.trim();
			this.showHeader = UtilidadesString.stringToBoolean(showHeader);
		}
	}

	public boolean isShowFooter() {
		return showFooter;
	}

	public void setShowFooter(String showFooter) {
		if (showFooter != null && !"".equals(showFooter)){
			showFooter = showFooter.trim();
			this.showFooter = UtilidadesString.stringToBoolean(showFooter);
		}
	}

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public boolean isModalScroll() {
		return modalScroll;
	}

	public void setModalScroll(String dato) {
		try {
			if (dato != null) 
				this.modalScroll = new Boolean(dato).booleanValue();
		} catch (Exception e) {
			this.modalScroll = false;
		}
	}

	public boolean isActivateSelectedRow() {
		return activateSelectedRow;
	}

	public void setActivateSelectedRow(String dato) {
		try {
			if (dato != null) 
				this.activateSelectedRow = new Boolean(dato).booleanValue();
		} catch (Exception e) {
			this.activateSelectedRow = false;
		}
	}

	public String getFixedHeight() {
		return fixedHeight;
	}

	public void setFixedHeight(String fixedHeight) {
		this.fixedHeight = fixedHeight;
	}

	/**
	 * @return mensajeBorrado (String): el mensaje a mostrar para la confirmación de borrado. Se espera una clave para obtener el mensaje del idioma
	 */
	public String getMensajeBorrado() {
		return mensajeBorrado;
	}
	/**
	 * @param mensajeBorrado (String): el mensaje a mostrar para la confirmación de borrado. Se espera una clave para obtener el mensaje del idioma
	 */
	public void setMensajeBorrado(String mensajeBorrado) {
		this.mensajeBorrado = mensajeBorrado;
	}
}