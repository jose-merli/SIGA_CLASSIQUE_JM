<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
-->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
 
<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.form.BusquedaPorTipoSJCSForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<%@ page import="com.siga.tlds.FilaExtElement"%>
 <%@ page import="java.util.Properties"%>
 <%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	Vector vResultado = (Vector) request.getAttribute("resultado");
	
	BusquedaPorTipoSJCSForm formulario = (BusquedaPorTipoSJCSForm)request.getAttribute("BusquedaPorTipoSJCSForm");

	java.util.ResourceBundle rp=java.util.ResourceBundle.getBundle("SIGA");
	String idordinario = rp.getString("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	

	String tipo = formulario.getTipo();
	if (tipo == null) {
		tipo = new String ("");
	}

%>
 
<html>

<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaPorTipoSJCSForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	function seleccionar(fila) {
		var datos;
		datos = document.getElementById('tablaDatosDinamicosD');
		datos.value = ""; 
		var j;
		var tabla;
		tabla = document.getElementById('tablaDatos');
		var flag = true;
		j = 1;
		while (flag) {
		  var aux = 'oculto' + fila + '_' + j;
		  var oculto = document.getElementById(aux);
		  if (oculto == null)  { flag = false; }
		  else { 
		  	datos.value = datos.value + oculto.value + ','; 
		  }
		  j++;
		}
		
		datos.value = datos.value + "%";

    	document.BusquedaPorTipoSJCSForm.modo.value = "enviar";
	   	document.BusquedaPorTipoSJCSForm.submit();
	}
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista  de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea"  style="display:none">
			<html:hidden name="BusquedaPorTipoSJCSForm" property = "modo" value = ""/>
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<html:hidden name="BusquedaPorTipoSJCSForm" property="idInstitucion" />
			<html:hidden name="BusquedaPorTipoSJCSForm" property="tipo" />
		</html:form>	


<%
	String tamanosCol="";
	String nombresCol="";
	
	tamanosCol="40,27,27,6";

	if (tipo.equalsIgnoreCase("EJG")) {
		nombresCol="gratuita.busquedaPorTipoSJCS.literal.tipo,gratuita.busquedaPorTipoSJCS.literal.anio,gratuita.busquedaPorTipoSJCS.literal.numero,";
	}
	if (tipo.equalsIgnoreCase("DESIGNA")) {
		nombresCol="gratuita.busquedaPorTipoSJCS.literal.turno,gratuita.busquedaPorTipoSJCS.literal.anio,gratuita.busquedaPorTipoSJCS.literal.numero,";
	}
	if (tipo.equalsIgnoreCase("SOJ")) {
		nombresCol="gratuita.busquedaSOJ.literal.tipoSOJ,gratuita.busquedaPorTipoSJCS.literal.anio,gratuita.busquedaPorTipoSJCS.literal.numero,";
	}
%>
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol%>"
		   tamanoCol="<%=tamanosCol%>"
		   alto="100%"
		  >
			
<%	if (vResultado == null || vResultado.size() == 0) { %>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
<%	} 
	else { 

		FilaExtElement[] elems=new FilaExtElement[1];
		elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	

		String modo = "", anio = "", numero = "", idTipoEJG = "", idTipoSOJ = "", codigo = "", turno= "";
		String idInstitucion = usrbean.getLocation();

		// recorro el resultado
		for (int i=0;i<vResultado.size();i++) {

			String cont = new Integer(i+1).toString();
		
			ArrayList elementoSel = new ArrayList();
			
			if (tipo.equalsIgnoreCase("EJG")) {
				ScsEJGBean registro	= (ScsEJGBean) vResultado.get(i);
				anio   = UtilidadesString.mostrarDatoJSP(registro.getAnio());
				numero = UtilidadesString.mostrarDatoJSP(registro.getNumero());
				codigo = UtilidadesString.mostrarDatoJSP(registro.getNumEJG());
				idTipoEJG = "" + registro.getIdTipoEJG();
				elementoSel.add(idTipoEJG);
			}
			
			if (tipo.equalsIgnoreCase("DESIGNA")) {
				ScsDesignaBean registro	= (ScsDesignaBean) vResultado.get(i);
				anio   = UtilidadesString.mostrarDatoJSP(registro.getAnio());
				numero = UtilidadesString.mostrarDatoJSP(registro.getNumero());
				codigo = UtilidadesString.mostrarDatoJSP(registro.getCodigo());
				turno  = "" + registro.getIdTurno();
				elementoSel.add(idInstitucion+","+turno);
			}
			if (tipo.equalsIgnoreCase("SOJ")) {
				ScsSOJBean registro	= (ScsSOJBean) vResultado.get(i);
				anio   = UtilidadesString.mostrarDatoJSP(registro.getAnio());
				numero = UtilidadesString.mostrarDatoJSP(registro.getNumero());
				codigo = UtilidadesString.mostrarDatoJSP(registro.getNumSOJ());
				idTipoSOJ = "" + registro.getIdTipoSOJ();
				elementoSel.add(idTipoSOJ);
			}
%>
			<siga:FilaConIconos fila="<%=cont %>" botones="" modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="no">
			
				<td>
				  	<% if (tipo.equalsIgnoreCase("EJG")) { %>
						<input type="hidden" name="oculto<%=cont%>_1" value="<%=anio%>">
						<input type="hidden" name="oculto<%=cont%>_2" value="<%=numero%>">
						<input type="hidden" name="oculto<%=cont%>_3" value="<%=idTipoEJG%>">
						<siga:ComboBD nombre="" tipo="tipoEJG" parametro="<%=datoTipoOrdinario%>"  ancho="350" clase="boxComboEnTabla" readOnly="true" elementoSel="<%=elementoSel%>"/>
					<% } %>
				  	<% if (tipo.equalsIgnoreCase("DESIGNA")) { 
				  		String[] dato = {idInstitucion};
				  	%>
						<input type="hidden" name="oculto<%=cont%>_1" value="<%=anio%>">
						<input type="hidden" name="oculto<%=cont%>_2" value="<%=numero%>">
						<input type="hidden" name="oculto<%=cont%>_3" value="<%=turno%>">
						<siga:ComboBD nombre="" tipo="turnos" ancho="350" clase="boxComboEnTabla" readOnly="true" elementoSel="<%=elementoSel%>" parametro="<%=dato%>" />
					<% } %>
				 	<% if (tipo.equalsIgnoreCase("SOJ")) { %>
						<input type="hidden" name="oculto<%=cont%>_1" value="<%=anio%>">
						<input type="hidden" name="oculto<%=cont%>_2" value="<%=numero%>">
						<input type="hidden" name="oculto<%=cont%>_3" value="<%=idTipoSOJ%>">
						<siga:ComboBD nombre="" tipo="tipoSOJ" ancho="350" clase="boxComboEnTabla" readOnly="true" elementoSel="<%=elementoSel%>"/>
					<% } %>
				  	
				</td>
				<td>
					<%=anio%>
				</td>
				<td>
					<%=codigo%>
				</td>
			</siga:FilaConIconos>		

			<!-- FIN REGISTRO -->
<%		} // del for %>			

<%	} // del if %>			

		</siga:TablaCabecerasFijas>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
</html>
