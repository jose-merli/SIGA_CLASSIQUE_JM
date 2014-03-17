<!DOCTYPE html>
<%@page import="java.util.ArrayList"%>
<%@page import="org.redabogacia.sigaservices.app.vo.scs.DocumentacionEjgVo"%>
<html>
<head>
<!-- listadoDocumentacionEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->



<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- JSP -->
<% 
	ArrayList obj = (ArrayList) request.getAttribute("resultado");
	String accion = (String)request.getSession().getAttribute("accion");
	String botonesFila="";
	String	botones="V,N,i";
	if (accion.equalsIgnoreCase("ver")){
		botonesFila = "C";
	}
	else {
		botonesFila = "C,E,B";
	}
	
	
	String informeUnico =(String) request.getAttribute("informeUnico");
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="gratuita.listadoSubzonas.literal.listadoSubzonas"/></title>
	<script type="text/javascript">
		function refrescarLocal(){
			buscar();
		}
	</script>
	<siga:Titulo 
		titulo="gratuita.busquedaEJG.documentacion" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body class="tablaCentralCampos">	
	<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<html:form action="/JGR_DocumentacionEJG" method="post" target="mainPestanas" style="display:none">
		<html:hidden styleId="modo" property = "modo" value="<%=accion%>"/>
		<html:hidden styleId="idInstitucion" property = "idInstitucion" />
		<html:hidden styleId="idTipoEJG" property = "idTipoEJG"/>
		<html:hidden styleId="anio" property = "anio" />
		<html:hidden styleId="numero" property = "numero"  />
		<html:hidden styleId="numEjg" property = "numEjg" />
		
	</html:form>	
		
		<tr>				
	<td width="100%" align="center">

		<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<c:out value="${DefinirDocumentacionEJGForm.anio}"/>/<c:out
			 	value="${DefinirDocumentacionEJGForm.numEjg}"/>&nbsp;-&nbsp;<c:out 
			 	value="${DefinirDocumentacionEJGForm.solicitante}"/>
		</td>
	</tr>
</table>

		<siga:Table 		   
		   name="listadoDocumentacion"
		   border="1"
		   columnNames="gratuita.operarEJG.literal.fechaLimitePresentacion,sjcs.ejg.documentacion.presentador,expedientes.auditoria.literal.documento,gratuita.documentacionEJG.regentrada,gratuita.documentacionEJG.regsalida,gratuita.operarEJG.literal.fechaPresentacion,"
		   columnSizes="10,25,25,10,10,10"
		   modal="G">
		   
  	<% if (obj.size()>0){
	    	int recordNumber=1;
	    	while (recordNumber-1 < obj.size())
			{			
	    		DocumentacionEjgVo documentacionEjgVo = (DocumentacionEjgVo)obj.get(recordNumber-1);
					
				
										
			%>				
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botonesFila%>" clase="listaNonEdit" >
					
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=documentacionEjgVo.getIddocumentacion()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=documentacionEjgVo.getIdinstitucion()%>">
					
					
							<%=documentacionEjgVo.getFechaLimite()==null||documentacionEjgVo.getFechaLimite().equals("")?"&nbsp;":documentacionEjgVo.getFechaLimite()%>
					</td>
					<td>
						<%=documentacionEjgVo.getDescPresentador()==null||documentacionEjgVo.getDescPresentador().equals("")?"&nbsp;":documentacionEjgVo.getDescPresentador()%>
					</td>
					<td> <%=documentacionEjgVo.getDocumentoAbreviatura()==null||documentacionEjgVo.getDocumentoAbreviatura().equals("")?"&nbsp;":documentacionEjgVo.getDocumentoAbreviatura()%>
						
					</td>
					<td>
					<%=documentacionEjgVo.getRegentrada()==null||documentacionEjgVo.getRegentrada().equals("")?"&nbsp;":documentacionEjgVo.getRegentrada()%>
					
					</td>					
					<td>
					<%=documentacionEjgVo.getRegsalida()==null||documentacionEjgVo.getRegsalida().equals("")?"&nbsp;":documentacionEjgVo.getRegsalida()%>
					
					</td>
					<td>
					<%=documentacionEjgVo.getFechaEntrega()==null||documentacionEjgVo.getFechaEntrega().equals("")?"&nbsp;":documentacionEjgVo.getFechaEntrega()%>
					
					</td>
				</siga:FilaConIconos>		
		<% recordNumber++;		   
		} %>
	<%
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
	<siga:ConjBotonesAccion botones="<%=botones %>" clase="botonesDetalle" modo="<%=accion%>"/>

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value ="${DefinirDocumentacionEJGForm.idInstitucion}"/>
	<html:hidden property="idTipoInforme" value="DEJG"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>	

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
				
		//Asociada al boton Volver
		function accionVolver()
		{
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 

		}
		
		
		function accionNuevo()
		{
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"G");
			if(resultado=='MODIFICADO') buscar();
		}
		function buscar()
		{
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}
		
		function accionImprimir(){
			sub();
			var idInstitucion= document.getElementById ('idInstitucion').value;
			var idTipoEJG= document.getElementById ('idTipoEJG').value;
			var anio= document.getElementById ('anio').value;
			var numero= document.getElementById ('numero').value;
			var datos = "idInstitucion=="+idInstitucion + "##idtipo==" +idTipoEJG+"##anio=="+anio +"##numero==" +numero+"##idTipoInforme==DEJG%%%";
			document.InformesGenericosForm.datosInforme.value =datos;

			if(document.getElementById("informeUnico").value=='1'){
				document.InformesGenericosForm.submit();
			}else{
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
				fin();	
		   	} 
		   	else {
		   		fin();
		   	}			
			
			}
		}
		
	
	</script>

	<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->	

</body>	
</html>