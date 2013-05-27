<!-- listadoDocumentacionSOJ.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>



<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) request.getAttribute("resultado");
	String accion = (String)request.getSession().getAttribute("accion");
	String botones="", anio= "", numero="", idTipoSOJ = "" ;
	
	if (accion.equalsIgnoreCase("ver")){
		botones = "C";
	}
	else {
		botones = "C,E,B";
	}

	Hashtable fila = new Hashtable();
	
	try {		
		anio = request.getParameter("ANIO").toString();
		numero = request.getParameter("NUMERO").toString();
		idTipoSOJ = request.getParameter("IDTIPOSOJ").toString();
	}catch(Exception e){
		Hashtable miHash = (Hashtable)request.getAttribute("DATOSEJG");
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoSOJ = miHash.get("IDTIPOSOJ").toString();
	};
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="pestana.justiciagratuitasoj.documentacion"/></title>
	<script type="text/javascript">
		function refrescarLocal()
		{
			buscar();
		}
	</script>
	<siga:Titulo 
		titulo="gratuita.busquedaSOJ.documentacion" 
		localizacion="gratuita.busquedaSOJ.localizacion"/>
</head>

<body class="tablaCentralCampos">	
	

	<html:form action="/JGR_PestanaSOJDocumentacion" method="post" target="mainPestanas" style="display:none">
		<input type="hidden" name="modo" value="<%=accion%>">
		<input type="hidden" name="idTipoSOJ" value="<%=idTipoSOJ%>">
		<input type="hidden" name="anio" value="<%=anio%>">
		<input type="hidden" name="numero" value="<%=numero%>">
	</html:form>	
		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoSOJ="";
						ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaSOJ(usr.getLocation(), anio, numero,idTipoSOJ);
						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsSOJBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsSOJBean.C_NUMSOJ);
						    t_tipoSOJ   = (String)hTitulo.get("TIPOSOJ");
						}
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>  <%=UtilidadesString.mostrarDatoJSP(t_tipoSOJ)%> 
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
	
		<siga:Table 		   
		   name="listadoDocumentacion"
		   border="2"
		   columnNames="gratuita.operarEJG.literal.fechaLimitePresentacion,pestana.justiciagratuitaejg.documentacion,gratuita.documentacionSOJ.regentrada,gratuita.documentacionSOJ.regsalida,gratuita.operarEJG.literal.fechaPresentacion,"
		   columnSizes="10,50,10,10,10"
		   modal="P">
		   
      <%if (obj.size()>0)
      { 
  			
	    	int recordNumber=1;
	    	//String regentrada=null;
	    	//String regsalida=null;
			while (recordNumber-1 < obj.size())
			{			
				fila = (Hashtable) obj.get(recordNumber-1);
				
				/*if(fila.get("REGENTRADA").equals("")||fila.get("REGENTRADA")==null)
					regentrada="&nbsp";
				else
					regentrada=fila.get("REGENTRADA");
					
				if(fila.get("REGSALIDA").equals("")||fila.get("REGSALIDA")==null)
					regsalida="&nbsp";
				else
					regsalida=fila.get("REGSALIDA");*/
			%>				
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" >
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDDOCUMENTACION")%>"><%=GstDate.getFormatedDateShort("",fila.get("FECHALIMITE"))%>&nbsp;</td>
					<td><%=fila.get("DOCUMENTACION")%>&nbsp;</td>
					<td><%=fila.get("REGENTRADA")%>&nbsp;</td>					
					<td><%=fila.get("REGSALIDA")%>&nbsp;</td>
					<td><%=GstDate.getFormatedDateShort("",fila.get("FECHAENTREGA"))%>&nbsp;</td>
				</siga:FilaConIconos>		
		<% recordNumber++;		   
		   }
	}else {
	%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<%
	}
	%>
	</siga:Table>	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<siga:ConjBotonesAccion botones="V,N" modo="<%=accion%>"  clase="botonesDetalle" />

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
				
		//Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[0].action="./JGR_ExpedientesSOJ.do";	
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar()
		{
			document.forms[0].submit();

		}
		
		//Asociada al boton Nuevo -->
		function accionNuevo()
		{
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}
		
		//Asociada al boton Buscar -->
		function buscar()
		{
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();
	
		}
	</script>


<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>	
</html>
	