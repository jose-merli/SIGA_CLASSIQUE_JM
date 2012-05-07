<!-- consultaCartaFacturacionCenso.jsp -->
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
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.beans.CenColegiadoBean"%>
<%@ page import="com.siga.beans.FcsFacturacionJGBean"%>


<bean:define id="datosPaginador" name="CartaFacturacionCensoForm" property="datosPaginador" type="java.util.HashMap" />
<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();

	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	String idiomaLabel = userBean.getLanguage().toUpperCase();
	Vector resultado = null;
	String informeUnico =(String) request.getAttribute("informeUnico");
	if (datosPaginador != null) {

		if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			resultado = (Vector) datosPaginador.get("datos");
		} else {
			resultado = new Vector();
		}
		
	} else {
		resultado = new Vector();
	}
	String action = app + "/JGR_PestanaRetencionesFacturacion.do?noReset=false";
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
<siga:Titulo titulo="censo.fichaCliente.sjcs.to.facturacion.titulo"
	localizacion="censo.fichaCliente.sjcs.to.facturacion.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">

<!-- INICIO: LISTA DE VALORES -->
<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
<html:form action="/JGR_PestanaRetencionesFacturacion?noReset=false" method="POST" target="mainWorkArea"
	style="display:none">
	<html:hidden property="modo" value="" />
	<html:hidden property="hiddenFrame" value="1" />
	<html:hidden property="idInstitucion" />
	<html:hidden property="idioma" />
	<html:hidden property="idPersona" />
	<html:hidden property="datosPaginador" />


	<!-- RGG: cambio a formularios ligeros -->
	<input type="hidden" name="filaSelD">
	<input type="hidden" name="tablaDatosDinamicosD" value="">
	<input type="hidden" name="actionModal" value="">


</html:form>


<siga:TablaCabecerasFijas nombre="tablaDatos" borde="1"
	clase="tableTitle"
	nombreCol="	Fecha,
				Nombre Facturación,
				informes.sjcs.pagos.literal.turnos,
				informes.sjcs.pagos.literal.guardias,
				informes.sjcs.pagos.literal.ejg,
				informes.sjcs.pagos.literal.soj,
		   		factSJCS.detalleFacturacion.literal.importe,"
	tamanoCol="18,38,8,8,8,8,8,4" alto="70%" ajusteBotonera="true"
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
					Hashtable fila = (Hashtable) resultado.elementAt(i);
					FilaExtElement[] elemento = new FilaExtElement[1];
					elemento[0] = new FilaExtElement("download","download",SIGAConstants.ACCESS_READ); 	
					String importeOficio =(String)fila.get("IMPORTEOFICIO");
					String importeGuardia =(String)fila.get("IMPORTEGUARDIA");
					String importeSOJ =(String)fila.get("IMPORTESOJ");
					String importeEJG =(String)fila.get("IMPORTEEJG");
					String importeTotal =(String)fila.get("IMPORTETOTAL");
										
	%>
	<siga:FilaConIconos fila='<%=""+(i+1)%>' botones=""
		visibleConsulta="no" visibleEdicion="no" visibleBorrado="no"
		elementos='<%=elemento%>' pintarEspacio="no" clase="listaNonEdit">
		
		<input type="hidden" name="idPersona<%="" + (i + 1)%>"
			value="<%=(String)fila.get("IDPERSONA")%>">

		<input type="hidden" name="idFacturacion<%="" + (i + 1)%>"
			value="<%=(String)fila.get("IDFACTURACION")%>">

		<td><%=UtilidadesString.mostrarDatoJSP((String)fila.get("FECHADESDE"))+" - "+UtilidadesString.mostrarDatoJSP((String)fila.get("FECHAHASTA"))%></td>
		<td><%=UtilidadesString.mostrarDatoJSP((String)fila.get("NOMBREFACT"))%></td>
		<td style="text-align:right"><%=UtilidadesNumero.formatoCampo(importeOficio)%>&nbsp;&euro;</td>
		<td style="text-align:right"><%=UtilidadesNumero.formatoCampo(importeGuardia)%>&nbsp;&euro;</td>
		<td style="text-align:right"><%=UtilidadesNumero.formatoCampo(importeSOJ)%>&nbsp;&euro;</td>
		<td style="text-align:right"><%=UtilidadesNumero.formatoCampo(importeEJG)%>&nbsp;&euro;</td>
		<td style="text-align:right"><%=UtilidadesNumero.formatoCampo(importeTotal)%>&nbsp;&euro;</td>

	</siga:FilaConIconos>
	<%
		}
			}
	%>


	<!-- FIN: ZONA DE REGISTROS -->
</siga:TablaCabecerasFijas>
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="CFACT"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>
<!-- FIN: LISTA DE VALORES -->


<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>

<script language="JavaScript">
	ObjArray = new Array();
		
	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}

	function download(fila) {
		var idPers = "idPersona"+fila;
		var idFacturacion = "idFacturacion"+fila;
		
		idPersona = document.getElementById(idPers).value;
		idFacturacion = document.getElementById(idFacturacion).value;
		idInstitucion = document.CartaFacturacionCensoForm.idInstitucion.value;
		datos = "idInstitucion=="+idInstitucion +"##idFacturacion=="+idFacturacion+"##idPersona=="+idPersona +"##idTipoInforme==CFACT"+"%%%";
		document.InformesGenericosForm.datosInforme.value =datos;
		if(document.getElementById("informeUnico").value=='1'){
			document.InformesGenericosForm.submit();
		}else{
		
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		
		   	}
		}
	 }
	
	function refrescarLocal() {
		//parent.buscarTodos();
	}
	function accionCerrar() 
	{		
		window.close();
	}
   	

	</script>

</body>
</html>