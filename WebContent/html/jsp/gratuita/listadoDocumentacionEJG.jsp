<!DOCTYPE html>
<%@page import="com.siga.administracion.SIGAConstants"%>
<%@page import="com.siga.tlds.FilaExtElement"%>
<%@page import="com.atos.utils.UsrBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.siga.gratuita.vos.SIGADocumentacionEjgVo"%>
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
	String idExpedienteExt = (String) request.getAttribute("idExpedienteExt");
	

	String accion = (String)request.getSession().getAttribute("accion");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
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
		<html:hidden styleId="idDocumentacion" property = "idDocumentacion" />
		<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
		
	</html:form>	
		
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos">
				<c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<c:out value="${DefinirDocumentacionEJGForm.anio}"/>/<c:out
					 	value="${DefinirDocumentacionEJGForm.numEjg}"/>&nbsp;-&nbsp;<c:out 
					 	value="${DefinirDocumentacionEJGForm.solicitante}"/>
				</td>
			</tr>
		</table>
<!-- columnSizes="8,8,25,18,25,8,8" -->
		<siga:Table 		   
		   name="listadoDocumentacion"
		   border="1"
		   columnNames="gratuita.operarEJG.literal.fechaLimitePresentacion,
		   gratuita.operarEJG.literal.fechaPresentacion,
		   sjcs.ejg.documentacion.presentador,
		   expedientes.auditoria.literal.documento,
		   pestana.justiciagratuitaejg.documentacion,
		   gratuita.documentacionEJG.regentrada,
		   Prop.,Adjunto,"
		   
		   columnSizes="8,8,24,17,17,6,4,5,10"
		   modal="M">
		   
  	<% if (obj.size()>0){
	    	int recordNumber=1;
	    	while (recordNumber-1 < obj.size())	{
	    		
	    		SIGADocumentacionEjgVo documentacionEjgVo = (SIGADocumentacionEjgVo)obj.get(recordNumber-1);
	    		FilaExtElement[] elems = new FilaExtElement[1];
				if(idExpedienteExt !=null && !idExpedienteExt.equals("") && (documentacionEjgVo.getNumIntercambiosOk()!=null && documentacionEjgVo.getNumIntercambiosOk()==0 && documentacionEjgVo.getIdFichero()!=null)){
					elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
				}
	    		String botonFila = botonesFila;
	    		if(!botonesFila.equals("C") &&
	    				( 
	    						(documentacionEjgVo.getComisionAJG()!=null && documentacionEjgVo.getComisionAJG().toString().equals("1")&&!usr.isComision())
	    						|| (documentacionEjgVo.getComisionAJG()!=null && documentacionEjgVo.getComisionAJG().toString().equals("0")&&usr.isComision()) 
	    						|| (documentacionEjgVo.getNumIntercambiosOk()!=null && documentacionEjgVo.getNumIntercambiosOk()>0)
	    						)
	    				)
	    			botonFila = "C";
			%>				
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botonFila%>" elementos="<%=elems%>" clase="listaNonEdit" >
					
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=documentacionEjgVo.getIdDocumentacion()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=documentacionEjgVo.getIdInstitucion()%>">
						<%=documentacionEjgVo.getFechaLimite()==null||documentacionEjgVo.getFechaLimite().equals("")?"&nbsp;":documentacionEjgVo.getFechaLimite()%>
					</td>
					<td>
						<%=documentacionEjgVo.getFechaEntrega()==null||documentacionEjgVo.getFechaEntrega().equals("")?"&nbsp;":documentacionEjgVo.getFechaEntrega()%>
					</td>
					<td>
						<%=documentacionEjgVo.getDescPresentador()==null||documentacionEjgVo.getDescPresentador().equals("")?"&nbsp;":documentacionEjgVo.getDescPresentador()%>
					</td>
					<td> 
						<%=documentacionEjgVo.getDocumentoAbreviatura()==null||documentacionEjgVo.getDocumentoAbreviatura().equals("")?"&nbsp;":documentacionEjgVo.getDocumentoAbreviatura()%>
					</td>
					<td>
						<%=documentacionEjgVo.getDocumentacion()==null||documentacionEjgVo.getDocumentacion().equals("")?"&nbsp;":documentacionEjgVo.getDocumentacion()%>
					</td>
					<td>
						<%=documentacionEjgVo.getRegEntrada()==null||documentacionEjgVo.getRegEntrada().equals("")?"&nbsp;":documentacionEjgVo.getRegEntrada()%>
					</td>					
					<td align="center">
						<%=documentacionEjgVo.getComisionAJG()==null||documentacionEjgVo.getComisionAJG()==0?"ICA":"CAJG"%>
					</td>
					<td align="center">
						<%= documentacionEjgVo.getNumIntercambiosOk()==null?(documentacionEjgVo.getIdFichero()!=null?"Sí":"No"):(documentacionEjgVo.getIdFichero()!=null?( documentacionEjgVo.getNumIntercambiosOk()==0?"Pte. envio CAJG":"Enviado CAJG"):"No")%>
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
	<html:hidden property="idTipoInforme" value='<%= usr.isComision() ?"DCAJG":"DEJG"%>'/>
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
			if(document.forms[0].jsonVolver && document.forms[0].jsonVolver.value!=''){
				
				jSonVolverValue = document.forms[0].jsonVolver.value;
				jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
				var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
				nombreFormulario = jSonVolverObject.nombreformulario;
				if(nombreFormulario != ''){
					parent.document.forms[nombreFormulario].idRemesa.value =  jSonVolverObject.idremesa;
					parent.document.forms[nombreFormulario].idinstitucion.value = jSonVolverObject.idinstitucion;
					parent.document.forms[nombreFormulario].modo.value="editar";
					parent.document.forms[nombreFormulario].target = "mainWorkArea";
					parent.document.forms[nombreFormulario].submit();
					
				}
			}else{
				document.forms[0].action="./JGR_EJG.do";	
				document.forms[0].modo.value="buscar";
				document.forms[0].target="mainWorkArea"; 
				document.forms[0].submit(); 
			}

		}
		
		
		function accionNuevo()
		{
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO') buscar();
		}
		function buscar()
		{
			document.forms[0].target = "mainPestanas";
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
			if (arrayResultado==undefined||arrayResultado[0]==undefined|| typeof(arrayResultado) == "string"){
				fin();	
		   	} 
		   	else {
		   		fin();
		   	}			
			
			}
		}
		function enviar(fila)
		{
			var idOculto1= 'oculto'+fila+'_1';
			var idDocumentacion = document.getElementById(idOculto1).value;
			var idOculto2= 'oculto'+fila+'_2';
			var idInstitucion = document.getElementById(idOculto2).value;
			document.forms[0].idDocumentacion.value = idDocumentacion;
			document.forms[0].idInstitucion.value = idInstitucion;
			document.forms[0].target = "submitArea";
			document.forms[0].modo.value = "envioDocumento";
			document.forms[0].submit();
		    
		}
		function refrescarLocal(){
			buscar();
		}
		function borrar(fila, id) {
			sub();
			if (typeof id == 'undefined')
				id = 'listadoDocumentacion';
			var datos;
			if (confirm('¿Está seguro de que desea eliminar el registro?')) {
				preparaDatos(fila, id);
				var auxTarget = document.forms[0].target;
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "Borrar";
				document.forms[0].submit();
				document.forms[0].target = auxTarget;
			} else {
				fin();
			}
		}
		fin();
	</script>

	<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->	

</body>	
</html>