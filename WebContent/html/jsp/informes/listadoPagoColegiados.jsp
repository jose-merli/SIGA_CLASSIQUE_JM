<!-- listadoPagoColegiados.jsp -->
<!-- 
	 VERSIONES:
	 jtacosta 2009 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitiveBind"%>
<%@page import="com.siga.beans.CenColegiadoBean"%>


<bean:define id="registrosSeleccionados"
	name="mantenimientoInformesForm" property="registrosSeleccionados"
	type="java.util.ArrayList" />
<bean:define id="datosPaginador" name="mantenimientoInformesForm"
	property="datosPaginador" type="java.util.HashMap" />
<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();

	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	String idiomaLabel = userBean.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	String paginaSeleccionada = "";
	String totalRegistros = "";
	String registrosPorPagina = "";
	Vector resultado = null;
	String valorCheckPersona = "";
	if (datosPaginador != null) {

		if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			resultado = (Vector) datosPaginador.get("datos");

			PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind) datosPaginador.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new Vector();
		paginaSeleccionada = "0";

		totalRegistros = "0";

		registrosPorPagina = "0";
	}
	String action = app + "/INF_CartaPago.do";
	/**************/
%>

<html>

<!-- HEAD -->
<head>

<link id="default" rel="stylesheet" type="text/css"
	href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el título y localización en la barra de título del frame principal -->
<siga:Titulo titulo="informes.sjcs.pagos.literal.titulo"
	localizacion="facturacion.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="cargarChecks();checkTodos()" class="tablaCentralCampos">

<!-- INICIO: LISTA DE VALORES -->
<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

<html:form action="/INF_CartaPago" method="POST" target="submitArea"
	style="display:none">
	<html:hidden property="modo" value="" />
	<html:hidden property="hiddenFrame" value="1" />
	<html:hidden property="idInstitucion" />
	<html:hidden property="idPago" />
	<html:hidden property="idioma" />
	<html:hidden property="idPersona" />
	<html:hidden property="registrosSeleccionados" />
	<html:hidden property="datosPaginador" />
	<html:hidden property="seleccionarTodos" />


	<!-- RGG: cambio a formularios ligeros -->
	<input type="hidden" name="filaSelD">
	<input type="hidden" name="tablaDatosDinamicosD" value="">
	<input type="hidden" name="actionModal" value="">


</html:form>

<!-- Formulario para la creacion de envio -->
<html:form action="/ENV_DefinirEnvios" method="POST"
	target="mainWorkArea" style="display:none">
	<html:hidden property="actionModal" value="" />
	<html:hidden property="modo" value="" />
	<html:hidden property="tablaDatosDinamicosD" value="" />
	<html:hidden property="subModo" value="" />
	<html:hidden property="filaSelD" value="" />
	<html:hidden property="idPersona" value="" />
	<html:hidden property="descEnvio" value="" />
	<html:hidden property="datosEnvios" value="" />


</html:form>

<siga:TablaCabecerasFijas nombre="tablaDatos" borde="1"
	clase="tableTitle"
	nombreCol="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/> ,
		   		   	informes.sjcs.pagos.literal.ncolegiado,
				  	informes.sjcs.pagos.literal.colegiado,
				  	factSJCS.datosPagos.literal.importeSJCS,
		   		  	factSJCS.datosPagos.literal.importeMovimientosVarios,
		   		  	informes.sjcs.pagos.literal.importeBruto,
		   		  	informes.sjcs.pagos.literal.importeIRPF,
		   		  	factSJCS.datosPagos.literal.importeRetenciones,
		   		  	factSJCS.detalleFacturacion.literal.importe,"
	tamanoCol="5,9,20,10,10,10,10,10,10,6" alto="70%" ajusteBotonera="true"
	activarFilaSel="true" ajustePaginador="true">

	<!-- INICIO: ZONA DE REGISTROS -->
	<%
		if (resultado == null || resultado.size() == 0) {
	%>
	<br>
	<br>
	<p class="titulitos" style="text-align: center"><siga:Idioma
		key="messages.noRecordFound" /></p>
	<br>
	<br>
	<%
		} else {
				for (int i = 0; i < resultado.size(); i++) {
					Row fila = (Row) resultado.elementAt(i);
					FilaExtElement[] elemento = new FilaExtElement[1];
					elemento[0] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
	%>
	<siga:FilaConIconos fila='<%=""+(i+1)%>' botones=""
		visibleConsulta="no" visibleEdicion="no" visibleBorrado="no"
		elementos='<%=elemento%>' pintarEspacio="no" clase="listaNonEdit">


		<input type="hidden" name="idPersona<%="" + (i + 1)%>"
			value="<%=fila.getString("IDPERSONASJCS")%>">
			
		<input type="hidden" name="idPago<%="" + (i + 1)%>"
			value="<%=fila.getString("IDPAGOS")%>">

		<td align="center">
		<%
			String idInstitucionRow = fila.getString("IDINSTITUCION");
			String idPersonaRow = fila.getString("IDPERSONASJCS");
			String idPagosRow = fila.getString("IDPAGOS");
			String valorCheck = idInstitucionRow + "||" + idPersonaRow + "##" + idPagosRow;
			boolean isChecked = false;
			for (int z = 0; z < registrosSeleccionados.size(); z++) {

				Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);

				if (valorCheck.equals((String) clavesRegistro.get("CLAVE"))) {
					isChecked = true;
					break;
				}

			}

			if (isChecked) {
		%> <input type="checkbox" value="<%=valorCheck%>" name="chkPersona"
			checked onclick="pulsarCheck(this)"> <%
 	} else {
 %> <input type="checkbox" value="<%=valorCheck%>" name="chkPersona"
			onclick="pulsarCheck(this)"> <%
 	}
 %>
		</td>


		<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(CenColegiadoBean.C_NCOLEGIADO))%></td>
		<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRE"))%></td>
		<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("TOTALIMPORTESJCS"), 2))%>&nbsp;&euro;</td>
		<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("IMPORTETOTALMOVIMIENTOS"), 2))%>&nbsp;&euro;</td>
		<%
			float aux = Float.parseFloat(fila.getString("TOTALIMPORTESJCS"))
									+ Float.parseFloat(fila.getString("IMPORTETOTALMOVIMIENTOS"));
							String importe = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea((new Float(aux)).toString(), 2));
		%>
		<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(importe, 2))%>&nbsp;&euro;</td>
		<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("TOTALIMPORTEIRPF"), 2))%>&nbsp;&euro;</td>
		<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("IMPORTETOTALRETENCIONES"), 2))%>&nbsp;&euro;</td>
		<%
			aux = aux + Float.parseFloat(fila.getString("TOTALIMPORTEIRPF"))
									+ Float.parseFloat(fila.getString("IMPORTETOTALRETENCIONES"));
							importe = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea((new Float(aux)).toString(), 2));
		%>
		<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(importe, 2))%>&nbsp;&euro;</td>

	</siga:FilaConIconos>
	<%
		}
			}
	%>


	<!-- FIN: ZONA DE REGISTROS -->
</siga:TablaCabecerasFijas>
<siga:ConjBotonesAccion botones="COM" />
<%
	if (datosPaginador != null && datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
		String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));
%>

<siga:Paginador totalRegistros="<%=totalRegistros%>"
	registrosPorPagina="<%=registrosPorPagina%>"
	paginaSeleccionada="<%=paginaSeleccionada%>"
	registrosSeleccionados="<%=regSeleccionados%>"
	idioma="<%=idiomaLabel%>" modo="buscarPor" clase="paginator"
	divStyle="position:absolute; width:100%; height:20;  z-index:3; bottom:30px; left: 0px"
	distanciaPaginas="" action="<%=action%>" />
<%
	}
%>
<!-- FIN: LISTA DE VALORES -->

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>

<script language="JavaScript">
	ObjArray = new Array();
		
	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}
 
	   
	function pulsarCheck(obj){
		
		if (!obj.checked ){
		   		
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
			ObjArray.push(obj.value);
		   	seleccionados1=ObjArray;
		}
		  	
		  	
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		checkTodos();
		
		   
	}
	function cargarChecks(){
		<%if (registrosSeleccionados != null) {
				for (int p = 0; p < registrosSeleccionados.size(); p++) {
					Hashtable clavesEJG = (Hashtable) registrosSeleccionados.get(p);
					valorCheckPersona = (String) clavesEJG.get("CLAVE");
					String valorPago =(String) clavesEJG.get("IDPAGOS");%>
					var aux='<%=valorCheckPersona +"##" + valorPago%>';
					ObjArray.push(aux);
				<%}
			}%>
	   	
		ObjArray.toString();
		seleccionados1=ObjArray;
			
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		if(document.getElementById('registrosSeleccionadosPaginador'))
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			
	}
	function cargarChecksTodos(o){
  	   if (document.getElementById('registrosSeleccionadosPaginador')){ 	
  	   var conf = confirm("<siga:Idioma key="paginador.message.marcarDesmarcar"/>"); 
	   if (conf){
			ObjArray = new Array();
		   	if (o.checked){
		   		parent.seleccionarTodos('<%=paginaSeleccionada%>');
			}else{
				ObjArray1= new Array();
			 	ObjArray=ObjArray1;
			 	seleccionados1=ObjArray;
			 	if(seleccionados1){
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("chkPersona");
					for (i = 0; i < ele.length; i++) {
						if(!ele[i].disabled)	
							ele[i].checked = false; 
					}
				}
			}
	   	}else{
	   	  	if (!o.checked ){
		   	  		var ele = document.getElementsByName("chkPersona");
						
				  	for (i = 0; i < ele.length; i++) {
				  		if(!ele[i].disabled){
				  			if(ele[i].checked){	
		     					ele[i].checked = false;
		     				
								ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
							}
						}
				   	}
				   	seleccionados1=ObjArray;
			   }else{
				   	var ele = document.getElementsByName("chkPersona");
							
				  	for (i = 0; i < ele.length; i++) {
				  		if(!ele[i].disabled){
							if(!ele[i].checked){				  		
			    				ele[i].checked = true;
								ObjArray.push(ele[i].value);
							}
						}
				   	}
				   		
			   		seleccionados1=ObjArray;
			   }
			   document.forms[0].registrosSeleccionados.value=seleccionados1;
		   		
	   	  }
	   	 if (document.getElementById('registrosSeleccionadosPaginador')){ 		 
		  document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		 }
		} 
	 }
	   
	function checkTodos(){
	
	 	var ele = document.getElementsByName("chkPersona");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
   			if(!ele[i].checked){
   				todos=0;
   				break;
   			} 
   		}
	   if (todos==1){
			document.getElementById("chkGeneral").checked=true;
		}else{
			document.getElementById("chkGeneral").checked=false;
		}
	   	
   	}
   	
   	function accionComunicar()
		{
		sub();
		datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			idPersona = idRegistros.substring(index+2);
			index2 = idRegistros.indexOf('##');
			idPago = idRegistros.substring(index2+2);
			idioma = parent.document.mantenimientoInformesForm.idioma.value;
 		   	datos += "idPersona=="+idPersona + "##idPago==" +idPago + "##idInstitucion==" +idInstitucion + "##idioma==" +idioma +"%%%";
		}
		
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
		var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucion%>'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='CPAGO'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='clavesIteracion' value='IDPERSONASJCS'>"));
		
		if(numElementosSeleccionados>50){
			formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='0'>"));
		}
		else{
			
			formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
		}
		document.appendChild(formu);
		formu.datosInforme.value=datos;
		formu.submit();
	}
	function comunicar(fila)
		{
		var idPers = "idPersona"+fila;
		var idPago = "idPago"+fila;
		idPersona = document.getElementById(idPers).value;
		idPago = document.getElementById(idPago).value;
		idInstitucion = document.mantenimientoInformesForm.idInstitucion.value;
		datos = "idInstitucion=="+idInstitucion +"##idPago=="+idPago+"##idPersona=="+idPersona +"%%%";
		
		var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucion%>'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='CPAGO'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='clavesIteracion' value='IDPERSONASJCS'>"));
		document.appendChild(formu);
		formu.datosInforme.value=datos;
		formu.submit();
	}
	function refrescarLocal()
	{			
		parent.buscarPaginador() ;			
	}
	function accionCerrar() 
	{		
		window.close();
	}
   	

	</script>

</body>
</html>