<!-- listadoActasComision.jsp -->
<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesFecha"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.siga.beans.ScsActaComisionBean"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>  
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>

<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<bean:define id="datosPaginador" name="ActaComisionForm" property="datosPaginador" type="java.util.HashMap"/>
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	boolean esComision = usr.isComision();
	
	String idioma=usr.getLanguage().toUpperCase();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	//Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	Hashtable registro;
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	String valorCheckPersona = "";
	if (datosPaginador!=null) {
	 if ( datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
	  	resultado = (Vector)datosPaginador.get("datos");
	    Paginador paginador = (Paginador)datosPaginador.get("paginador");
		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }else{
		resultado =new Vector();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	 }
	}else{
      	resultado =new Vector();
	  	paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}	 
		String action=app+"/JGR_ActasComision.do?noReset=true";
    /**************/

	
%>

<%@page import="com.siga.Utilidades.paginadores.Paginador"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	<script type="text/javascript">
		function refrescarLocal()
		{
			parent.buscar();
		}
	</script>
</head>

<body>

	<html:form action="/JGR_ActasComision.do?noReset=true" target="mainWorkArea" method="post">
		<input type="hidden" name="modo"  id="modo" value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<html:hidden styleId="registrosSeleccionados"  property="registrosSeleccionados" />
		<html:hidden styleId="datosPaginador"  property="datosPaginador" />
		<html:hidden styleId="seleccionarTodos"  property="seleccionarTodos" />
	</html:form>	
	
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoActas"
		   borde="1"
		   clase="tableTitle"		   
		   nombreCol="sjcs.actas.numeroActa, sjcs.actas.fechaResolucion, sjcs.actas.fechaReunion, sjcs.actas.presidente, sjcs.actas.secretario,"
		   tamanoCol="10,15,15,25,25,10"
		   alto="100%"
		   modal="G" 
		   ajustePaginador="true"
		   activarFilaSel="true" >
		   
		<%if (resultado == null || resultado.size() == 0) {%>
			<br>
			<br>
				<p class="titulitos" style="text-align: center">
					<siga:Idioma key="messages.noRecordFound" />
				</p>
			<br>
			<br>
		<%}else{%>
			<%String fila;%>
			<%for(int i=0;i<resultado.size();i++){ %>
			<%fila=""+(i+1);%>
			<%Row row = (Row)resultado.get(i);%>
			<siga:FilaConIconos fila="<%=fila%>" botones="C,E,B" clase="lista">
				<td>
						<input type='hidden' name='oculto<%=fila%>_1' value='<%=row.getString(ScsActaComisionBean.C_IDACTA)%>'>
						<input type='hidden' name='oculto<%=fila%>_2' value='<%=row.getString(ScsActaComisionBean.C_IDINSTITUCION)%>'>
						<input type='hidden' name='oculto<%=fila%>_3' value='<%=row.getString(ScsActaComisionBean.C_ANIOACTA)%>'>
					<%=row.getString(ScsActaComisionBean.C_ANIOACTA)%>/<%=row.getString(ScsActaComisionBean.C_NUMEROACTA)%>&nbsp;</td>
				<td><%=UtilidadesString.formatoFecha(row.getString(ScsActaComisionBean.C_FECHARESOLUCION),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH)%>&nbsp;</td>
				<td><%=UtilidadesString.formatoFecha(row.getString(ScsActaComisionBean.C_FECHAREUNION),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH)%>&nbsp;</td>
				<td><%=row.getString("NOMBREPRESIDENTE")%>&nbsp;</td>
				<td><%=row.getString("NOMBRESECRETARIO")%>&nbsp;</td>
		</siga:FilaConIconos>
			<%} %>
		<%} %>
		</siga:TablaCabecerasFijas>
<%
	
	if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){%>
	  
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
			registrosPorPagina="<%=registrosPorPagina%>" 
			paginaSeleccionada="<%=paginaSeleccionada%>"
			idioma="<%=idioma%>"
			modo="buscarPor"								
			clase="paginator" 
			divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
			distanciaPaginas=""
			action="<%=action%>" />
	
	 <%}%>	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
<!-- FIN: SUBMIT AREA -->	
	<script language="JavaScript">
	

		
	
	</script>
</body>	
</html>
	