/*
 * Created on 29-mar-2005
 *
 */

package com.siga.tlds;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


import org.apache.commons.lang.StringUtils;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.siga.Utilidades.UtilidadesString;


/**
 * 
 * @author emilio.grau
 *
 */

public class TagPaginador extends TagSupport {

	private String registrosSeleccionados;
	private String totalRegistros;
	private String registrosPorPagina;
	private String paginaSeleccionada;
	private String clase;
	private String action;
	private String modo;
	private String divStyle;
	private String distanciaPaginas;
	private String idioma;
	private String preFunction;
	private String postFunction;
	
	public static final String SPACE = "&nbsp;";
	public static final String YES = "yes";
	public static final String AND = "&";
	public static final String EQUALS = "=";
	public static final String ABEGIN = "<a href=\"#\" onClick=\"";// aplicamos la funcion paginador en el onclick porque en el href cuando la consulta
	                                                               // es muy costosa da error de paginacion
	public static final String AEND = "</a>";
	public static final String QUESTION = "?";
	public static final String QUOT = "\"";
	public static final String GT = ">";
	public static final String LT = "<";
	public static final String PIPE = "|";
	public static final String LB = "[";
	public static final String RB = "]";
	public static final String BOLDSTART = "<b>";
	public static final String BOLDEND = "</b>";
	public static final String PAGES = "pages";
	public static final String PAGINA = "pagina";
	public static final String MODO = "modo";
	public static final String INTRO = "\n";
	public static final String FORM = "formPaginador";
	public static final int DISTANCIA_PAG_PAGINADOR = 4;

	
	
	public String getDistanciaPaginas() {
		return distanciaPaginas;
	}
	public void setDistanciaPaginas(String distanciaPaginas) {
		this.distanciaPaginas = distanciaPaginas;
	}
	public String getDivStyle() {
		return divStyle;
	}
	public void setDivStyle(String divStyle) {
		this.divStyle = divStyle;
	}
	public String getModo() {
		return modo;
	}
	public void setModo(String modo) {
		this.modo = modo;
	}
	public String getPaginaSeleccionada() {
		return paginaSeleccionada;
	}
	public void setPaginaSeleccionada(String paginaSeleccionada) {
		this.paginaSeleccionada = paginaSeleccionada;
	}
	public String getRegistrosPorPagina() {
		return registrosPorPagina;
	}
	public void setRegistrosPorPagina(String registrosPorPagina) {
		this.registrosPorPagina = registrosPorPagina;
	}
	public String getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(String totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public String getIdionma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	

	public int doStartTag() {
		
		int total, results, pages, selected, i, ci, distance, pageUltima,registrosSeleccionados;
		boolean firstPage, hasPreviousPage, hasNextPage,conRegistrosSeleccionados;
		conRegistrosSeleccionados = this.registrosSeleccionados!=null && !this.registrosSeleccionados.equals("");
		if(conRegistrosSeleccionados)
			registrosSeleccionados = Integer.parseInt(this.registrosSeleccionados);
		else
			registrosSeleccionados = 0;
		total = Integer.parseInt(this.totalRegistros);
		results = Integer.parseInt(this.registrosPorPagina);
		selected = Integer.parseInt(this.paginaSeleccionada);
		String ultimo=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.ultimo");
		String primero=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.primero");
		String a=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.a");
		String mostrando=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.mostrando");
		String registro=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.registros");
		String anterior=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.anterior");
		String siguiente=UtilidadesString.getMensajeIdioma(this.idioma,"consultas.consultaslistas.literal.siguiente");
		String litRegistrosSeleccionados =UtilidadesString.getMensajeIdioma(this.idioma,"paginador.literal.registrosSeleccionados");
		
		if (this.distanciaPaginas!=null && !this.distanciaPaginas.equals("")){
			distance = Integer.parseInt(this.distanciaPaginas);
		}else{
		    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties properties=new ReadProperties("SIGA.properties");
			this.distanciaPaginas = properties.returnProperty("paginador.distanciaPaginasPaginador",true);
			distance = this.distanciaPaginas!=null?Integer.parseInt(this.distanciaPaginas):DISTANCIA_PAG_PAGINADOR;
		}
		
		pages = (total / results);
		pages = total % results == 0 ? pages : pages + 1;
		pageUltima=pages;
		
		if (pages > 1) {
			ci = selected; // current index
			hasPreviousPage = ci > 1 ? true : false;
			hasNextPage = ci < pages  ? true : false;
			
			StringBuffer sb = null;

			try {
				JspWriter out = pageContext.getOut();
				sb = new StringBuffer();
				
				
				//Anhadimos un formulario, para pasar al action el modo y el número de página a visualizar
				sb.append("<form id=\""+FORM+"\" name=\""+FORM+"\" action=\"").append(this.action).append("\" method=\"POST\">").append(INTRO);
				sb.append("<input type=\"hidden\" name = \"modo\" id = \"modo\" value = \"\"/>").append(INTRO);
				sb.append("<input type=\"hidden\" name = \"pagina\" id = \"pagina\" value = \"\"/>").append(INTRO);
				sb.append("<input type=\"hidden\" name = \"Seleccion\" id = \"Seleccion\" value = \"\"/>").append(INTRO);
				
				sb.append("</form>").append(INTRO).append(INTRO);
				
				
				//Anhadimos una función javascript para el set de la página y el submit del formulario
				sb.append("<script>").append(INTRO);
				sb.append(" var ejecutar=true;").append(INTRO);
				sb.append("function paginar(pagina){").append(INTRO);
				sb.append(" if(!ejecutar) return false;").append(INTRO);
				sb.append(FORM+".modo.value=\""+this.modo+"\";").append(INTRO);
				sb.append(FORM+".pagina.value=pagina;").append(INTRO);
				sb.append("try{var aux = (seleccionados1) ? true:false;");
				sb.append("if (aux==true){"+FORM+".Seleccion.value=seleccionados1;}}catch(e){}" ).append(INTRO);
				//sb.append(" if(SeleccionadosAux!=undefined)"+FORM+".Seleccion.value=Seleccionados;}").append(INTRO);
				sb.append("  ejecutar=false;").append(INTRO);
				sb.append(FORM+".submit();").append(INTRO);
				sb.append("}").append(INTRO);
				sb.append("</script>").append(INTRO).append(INTRO);
				
				//Anhado el div para posicionar el paginador
				sb.append("<div style=\""+this.divStyle+"\">").append(INTRO);
				
				//Anhado la tabla que contiene el paginador
				sb.append("<table class=\"tPaginator\" cellspacing=\"0\" border=\"0\"><tr>").append("<td width=\"50%\" align=\"left\"").append(" class=\"")
				                                                                 .append(this.clase)
																			     .append("\"")
																				 .append(">").append(INTRO);
				int extremoIni=(results*(selected-1)+1);
				int inicio=(selected==1?1:extremoIni);
				int fin=((extremoIni+results-1)>total?total:((extremoIni)+results-1));
				sb.append(BOLDSTART).append(" "+total+" "+registro+" "+mostrando+" "+inicio+" "+a+" "+fin).append(BOLDEND).append(SPACE).append(SPACE);
				if(conRegistrosSeleccionados){
					sb.append(BOLDSTART).append(litRegistrosSeleccionados).append(SPACE).append(BOLDEND);
					sb.append("<input type=\"text\" id = \"registrosSeleccionadosPaginador\" size=\"3\" class=\"boxConsultaPaginador\" readonly value = \"");
					sb.append(registrosSeleccionados);
					sb.append("\"/>");
					//sb.append(BOLDSTART).append("Registros seleccionados:").append(registrosSeleccionados).append(BOLDEND);
				}
				sb.append("</td>").append(INTRO);
				sb.append(this.getTdWithStyle()).append(INTRO);
				
				sb.append(BOLDSTART).append(LB).append(BOLDEND).append(SPACE);
				
				//sb.append(pages).append(SPACE).append(PAGES).append(SPACE).append(SPACE);

				if (hasPreviousPage) {
					sb.append(getAnchor(1,primero)).append(SPACE);
					sb.append(getAnchor((ci - 1) , anterior)).append(SPACE);
				}
				
				/**
				 * logic for the anchors,
				 * we should show 4 achors both on the left and right of the current index if possible
				 */
				i = ci - distance < 1 ? 1 : ci - distance;
				pages = ci + distance > pages ? pages : ci + distance;
				for (; i <= pages; i++) {
					if (i == ci) {
						sb.append(BOLDSTART).append(i).append(
							BOLDEND).append(
							SPACE).append(INTRO);
					} else {
						sb.append(getAnchor(i, String.valueOf(i))).append(SPACE);
					}
				}
				if (hasNextPage) {
					sb.append(getAnchor((ci + 1) , siguiente)).append(SPACE);
					sb.append(getAnchor(pageUltima,ultimo)).append(SPACE);
				}
				sb.append(SPACE).append(BOLDSTART).append(RB).append(BOLDEND).append(INTRO);
				sb.append("</td></tr></table>").append(INTRO);
				
				//Cierro el div
				sb.append("</div>");
				
				out.println(sb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// no pagina, pero debe mostrar los resultados.
			StringBuffer sb = null;

			try {
				JspWriter out = pageContext.getOut();
				sb = new StringBuffer();
				
				//Anhado el div para posicionar el paginador
				sb.append("<div style=\""+this.divStyle+"\">").append(INTRO);
				
				//Anhado la tabla que contiene el paginador
				sb.append("<table class=\"tPaginator\" border=\"0\" ><tr>").append("<td align=\"left\"").append(" class=\"")
				                                                                 .append(this.clase)
																			     .append("\"")
																				 .append(">").append(INTRO);
				sb.append(BOLDSTART).append(" "+total+" "+registro).append(BOLDEND).append(SPACE);
				if(conRegistrosSeleccionados){
					sb.append(BOLDSTART).append(litRegistrosSeleccionados).append(SPACE).append(BOLDEND);
					sb.append("<input type=\"text\" id = \"registrosSeleccionadosPaginador\" size=\"3\" class=\"boxConsultaPaginador\" readonly value = \"");
					sb.append(registrosSeleccionados);
					sb.append("\"/>");
					//sb.append(BOLDSTART).append("Registros seleccionados:").append(registrosSeleccionados).append(BOLDEND);
				}
				
				sb.append("</td></tr></table>").append(INTRO);
				
				//Cierro el div
				sb.append("</div>");
				
				out.println(sb);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return EVAL_BODY_INCLUDE;
	}

	
	private String getAnchor(int page, String label) {
		StringBuffer sb = new StringBuffer(ABEGIN);
		if(this.getPreFunction()!=null && !this.getPreFunction().equals("")){
			sb.append("var varReturn =");
			sb.append(this.getPreFunction());
			sb.append("(");
			sb.append(page);
			sb.append("); if(varReturn==false)return false;");
		}
		sb.append("paginar("+page+");");
		if(this.getPostFunction()!=null && !this.getPostFunction().equals("")){
			sb.append(this.getPostFunction());
			sb.append("(");
			sb.append(page);
			sb.append(");");
		}
		sb.append("return false;");
		sb.append(QUOT);
		sb.append(" class=\"");
		sb.append(this.clase);
		sb.append("\"");
		sb.append(GT);
		sb.append(label);
		sb.append(AEND);
		sb.append(INTRO);
		return sb.toString();
				
	}

	/**
	 * Returns the action.
	 * @return String
	 */
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	
	public String getTdWithStyle() {
		if (StringUtils.isNotEmpty(this.clase)) {
			return new StringBuffer("<td  width=\"50%\" align=\"right\"")
				.append(" class=\"")
				.append(this.clase)
				.append("\"")
				.append(">")
				.toString();
		}
		return "<td  align=\"center\">";
	}
	public String getRegistrosSeleccionados() {
		return registrosSeleccionados;
	}
	public void setRegistrosSeleccionados(String registrosSeleccionados) {
		this.registrosSeleccionados = registrosSeleccionados;
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
	

}
