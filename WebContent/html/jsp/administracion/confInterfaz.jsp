<!-- confInterfaz.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.*"%>
<%@ page import="java.util.Properties" %>
<!-- JSP -->

<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String color=(String)request.getAttribute(SIGAConstants.COLOR);
	String tipoLetra=(String)request.getAttribute(SIGAConstants.FONT);
	String aceso=((UsrBean)request.getSession().getAttribute("USRBEAN")).getAccessType();
	boolean acesoAdm=aceso.equals(SIGAPTConstants.ACCESS_FULL);
	//Si refrescar=SI avisamos con un alert:
	String refrescar = (String)request.getAttribute("refrescar");
%>

<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->		
		<siga:Titulo 
			  titulo="confInterfaz.title" 
			  localizacion="menu.administracion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->	

	<!-- SCRIPTS LOCALES -->

	<script language="JavaScript">
		function obtenerColor() {		
			if(<%=color%>!= null){
				indiceColor=parseInt(<%=color%>)-1;
				document.all.confInterfazForm.idColor[indiceColor].checked=true;
			}
		}
		
		function obtenerLetra() {
			if(<%=tipoLetra%>!= null){
				indiceLetra=(parseInt(<%=tipoLetra%>))-1;
				document.all.confInterfazForm.idTipoLetra.options[indiceLetra].selected=true;
			}
		}
		
		
		
		function accionGuardar() {
			if (TestFileType(document.forms[0].theFile.value, ['GIF', 'JPG', 'PNG', 'JPEG'])) {
			{
				if(confirm('<siga:Idioma key="messages.confirm.updateData"/>'))
					document.all.confInterfazForm.submit();
				}
			}
			return false;
		}	
		
	 	//Refresco del iframe 	
	 	function avisoRefresco()
		{
				var type = '<siga:Idioma key="messages.updated.success"/>';
				alert(type);
		}
		
		function accionRestablecer() {
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.confInterfazForm.reset();
				obtenerColor();
				obtenerLetra();			
			}
			return false;
		}	
	</script>	

</head>


<body onload = "<% if (refrescar!=null && refrescar.equalsIgnoreCase("SI")) { %>avisoRefresco();<% } %>">

<div id="camposRegistro" class="posicionBusquedaSolo">
<html:form action="/ADM_ConfigurarInterfazAplicacion.do" method="POST" target="submitArea" enctype="multipart/form-data">
	<input type="hidden" name="mode" value="update">
	<input type="hidden" name="hiddenFrame" value="1">
	
	<siga:ConjCampos leyenda="confInterfaz.esquemaColores">
	<br>
	<table WIDTH="100%" align="left" valign="top">
		<tr>

		<td align="left" valign="top" >


	<siga:ConjCampos leyenda="confInterfaz.esquemaColores.oscuros">
	<br>
		
		<table border="1" cellspacing="0" cellpadding="0" width="90%" align="center">
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
		  		  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="1"></html:radio></td>
				  <td width="13%" bgcolor="#303E54">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#446699">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#4477AA">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#CCCCDD">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#DDDDEE">&nbsp;<br>&nbsp;</td>
		  	    </tr>
		  	  </table>
		    </td>
		  </tr>
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
			  	  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="2" ></html:radio></td>
				  <td width="13%" bgcolor="#333333">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#555555">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#888888">&nbsp;<br>&nbsp;</td>
			      <td width="13%" bgcolor="#CCCCCC">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#EEEEEE">&nbsp;<br>&nbsp;</td>
		  		</tr>
		  	  </table>
		  	</td>
		  </tr>
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
			  	  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="3" ></html:radio></td>
 				  <td width="13%" bgcolor="#625148">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#726158">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#A1775C">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#E1B79C">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#EEEEEE">&nbsp;<br>&nbsp;</td>
		        </tr>
		      </table>
		    </td>
		  </tr>
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
			  	  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="4" ></html:radio></td>
				  <td width="13%" bgcolor="#444411">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#555522">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#777755">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#B7B7A5">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#DDE0DD">&nbsp;<br>&nbsp;</td>
		        </tr>
		      </table>
		    </td>
		  </tr>
		</table>
		<br>		
	
	</siga:ConjCampos>
	</td>
	
<!-- COLORES FORNDOS CLAROS -->

		<td align="left">

	<siga:ConjCampos leyenda="confInterfaz.esquemaColores.claros">
	<br>
		
		<table border="1" cellspacing="0" cellpadding="0" width="90%" align="center">
		  <tr>
		    <td valign="top">
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
				  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="5" ></html:radio></td>
				  <td width="13%" bgcolor="#222222">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#7A96B6">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#95AFCF">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#CED9E6">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#F5F8FB">&nbsp;<br>&nbsp;</td>
		  	    </tr>
		  	  </table>
		  	</td>
		  </tr>
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
				  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="6" ></html:radio></td>
				  <td width="13%" bgcolor="#333333">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#863545">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#C06F7F">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#D3AFB9">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#EADFE8">&nbsp;<br>&nbsp;</td>
		  	    </tr>
		  	  </table>
		  	</td>
		  </tr>
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
		  		  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="7"></html:radio></td>
				  <td width="13%" bgcolor="#333300">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#8C8235">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#B1A559">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#DBD39D">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#F2EFDC">&nbsp;<br>&nbsp;</td>
		  	    </tr>
		  	  </table>
		    </td>
		  </tr>
		  <tr>
		    <td>
		      <table border=0 cellspacing=0 cellpadding=0>
		        <tr>
			  	  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="8" ></html:radio></td>
				  <td width="13%" bgcolor="#222222">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#637832">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#839852">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#C6D1AB">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#FAFFFA">&nbsp;<br>&nbsp;</td>
		  		</tr>
		  	  </table>
		  	</td>
		  </tr>
		</table>
		<br>		
	
	</siga:ConjCampos>

	</td>
	
<!-- COLORES ALTO CONTRASTE -->

		<td align="left">

	<siga:ConjCampos leyenda="confInterfaz.esquemaColores.contraste">
	<br>
		
		<table border="1" cellspacing="0" cellpadding="0" width="65%" align="center">
		  <tr>
		    <td valign="top">
		      <table valign="top" border="0" width="100%" cellspacing=0 cellpadding=0>
		        <tr>
		  		  <td width="8%"><html:radio name="confInterfazForm" property="idColor" value="9"></html:radio></td>
				  <td width="13%" bgcolor="#000000">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#222222">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#BBBBBB">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#DDDDDD">&nbsp;<br>&nbsp;</td>
				  <td width="13%" bgcolor="#FFFFFF">&nbsp;<br>&nbsp;</td>
		  	    </tr>
		  	  </table>
		    </td>
		  </tr>
		</table>
		<br>		
	</siga:ConjCampos>

	</td>
	
	
		<script>obtenerColor()</script>				
	
	
	
	</tr></table>
	
	</siga:ConjCampos>

	
	<table align="left" border="0" width="100%">
		<tr>
			<td  align="left">
			<siga:ConjCampos leyenda="confInterfaz.tipoLetra">
				<table width="100%">
				<tr>
				<td  class="labelText"><siga:Idioma key="confInterfaz.muestra"/></td>
				<td>
				<html:select name="confInterfazForm" property="idTipoLetra" styleClass="boxCombo" >
					<html:option value="1">Arial</html:option>			
					<html:option value="2">Times </html:option>			
					<html:option value="3">Helvetica</html:option>
				</html:select>
				</td>
				<tr>
				<script>obtenerLetra()</script>	
				</table>
			</siga:ConjCampos>
			
			</td>
			<td nowrap align="left">
			<siga:ConjCampos leyenda="confInterfaz.logotipo">
				<table width="100%">
				<tr>
				<td  class="labelText"><siga:Idioma key="confInterfaz.fichero"/></td>
				<td>
					<html:file name="confInterfazForm" property="theFile" styleClass="box"/>
				</td>
				<tr>
				<script>obtenerLetra()</script>	
				</table>
			</siga:ConjCampos>
			</td>
			</tr>				
		</table>
		
		
		<siga:ConjBotonesAccion botones="G,R"  clase="botonesDetalle"/>	
	
	</html:form>

</div>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>


