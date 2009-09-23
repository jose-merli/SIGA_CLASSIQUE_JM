<!-- listarGuardiasLetrado.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");

	//Datos del Colegiado si procede:
	String nombrePestanha=null, numeroPestanha=null;
	try {
		Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
	} catch (Exception e){
		nombrePestanha = "";
		numeroPestanha = "";
	}
	
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	String alto = "345";
	if (entrada!=null && entrada.equals("2"))
		alto = "275";
		
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	

	
		
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.guardias.cabecera" 
			localizacion="censo.fichaCliente.sjcs.guardias.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->	

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>

</head>

<body class="tablaCentralCampos">
	<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { %>
			<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.guardiasInscrito.pestana.titulito"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					<% } else { %>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
			</table>
	<% } %>

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="DefinirGuardiasTurnosAction.do" method="post" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<!-- Datos del Colegiado seleccionado -->
		<html:hidden property = "nombreColegiadoPestanha" value = "<%=nombrePestanha%>"/>
		<html:hidden property = "numeroColegiadoPestanha" value = "<%=numeroPestanha%>"/>
		<html:hidden property = "actionModal" value = "M"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
	</html:form>	

		<div class="nonEdit" style="text-align:center;width:100%">
			
			<table>
			<tr><td>&nbsp;</td></tr>
				<tr>
					<td  class="labelText"><siga:Idioma key="gratuita.listarGuardiasLetrado.literal.textoInscripcion"/>
				</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			</table>
			
		</div>
		
	<% if (obj.size()>0) { %>		
	
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="gratuita.listarGuardias.literal.turno,gratuita.listarGuardias.literal.guardia,gratuita.listarGuardias.literal.obligatoriedad,gratuita.listarGuardias.literal.tipodia,gratuita.listarGuardias.literal.duracion,gratuita.listarGuardias.literal.fechainscripcion,gratuita.listarGuardias.literal.fecha,"
		   tamanoCol="20,15,11,8,7,11,8,15"
		   alto="100%"
		   ajusteBotonera="true"
		  >
		  <%
	    	int recordNumber=1;
			while ((recordNumber) <= obj.size())
			{	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				
				String tipoDia = (String)hash.get("TIPODIASGUARDIA");
				String literalValidar="";
				FilaExtElement[] elems=new FilaExtElement[1];
				
				
				if (hash.get("VALIDACIONTURNO")==null || ((String)hash.get("VALIDACIONTURNO")).equals("")){
				  literalValidar="gratuita.altaTurnos.literal.validarTurno";
				}else{
				   if (!usr.isLetrado())
             		  elems[0]=new FilaExtElement("sustituir","sustituir",SIGAConstants.ACCESS_FULL);
				}

				String literalDuracion ="gratuita.altaTurnos_2.literal.dias";
				if(tipoDia.equalsIgnoreCase("D"))
					literalDuracion = "gratuita.altaTurnos_2.literal.dias";
				else if(tipoDia.equalsIgnoreCase("S"))
						literalDuracion = "gratuita.altaTurnos_2.literal.semanas";
					 else if(tipoDia.equalsIgnoreCase("M"))
							literalDuracion = "gratuita.altaTurnos_2.literal.meses";
						  else if(tipoDia.equalsIgnoreCase("Q"))
								 literalDuracion = "gratuita.altaTurnos_2.literal.quincenas";
		 	%>	
			  	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C" clase="listaNonEdit" elementos='<%=elems%>'>
					<td><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDTURNO")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGUARDIA")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDPERSONA")%>'><%=hash.get("TURNO")%></td>
					<td><%=hash.get("GUARDIA")%></td>
					<td><%if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("0")){%>Obligatorias<%}else if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("1")){%>Todas o Ninguna<%}else{%>A elección<%}%></td>
					<td>
						<%=ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr)%>
					</td>
					<td><%=hash.get("DURACION")%>&nbsp;<siga:Idioma key="<%=literalDuracion%>"/></td>
					<td>&nbsp<%=GstDate.getFormatedDateShort("",(String)hash.get("FECHAINSCRIPCION"))%><siga:Idioma key="<%=literalValidar%>"/></td>
					<td>&nbsp<%=GstDate.getFormatedDateShort("",(String)hash.get("FECHABAJA"))%></td>
				</siga:FilaConIconos>	
			<%recordNumber++;%>
			<%}%>	
		</siga:TablaCabecerasFijas>
	<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } %>
	
	
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		//Guardo los campos seleccionados
		function seleccionarFila(fila){
		    var idturno = 'oculto' + fila + '_' + 1;
		    var idguardia = 'oculto' + fila + '_' + 2;
		    var idpersona = 'oculto' + fila + '_' + 3;
		    
			//Datos del elemento seleccionado:
			document.forms[0].idTurno.value = document.getElementById(idturno).value;
			document.forms[0].idGuardia.value = document.getElementById(idguardia).value;
				
		}
		
		function sustituir(fila) 
		{		
			//Datos del elemento seleccionado:
			//seleccionarFila(fila)			
			selectRow(fila); 
			consultar2(fila, document.forms[0]);
			document.forms[0].modo.value = "sustituir";
			//document.forms[0].target = "_blank";
			//document.forms[0].submit();
			var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();			
		}
		

		
function consultar2(fila, formulario) {

 var datos;
   datos = formulario.tablaDatosDinamicosD;
   datos.value = ""; 
   var i, j;
   for (i = 0; i < 7; i++) {
      var tabla;
      tabla = document.getElementById('tablaDatos');
      if (i == 0) {
        var flag = true;
        j = 1;
        while (flag) {
          var aux = 'oculto' + fila + '_' + j;
          var oculto = document.getElementById(aux);
          if (oculto == null)  { flag = false; }
          else { datos.value = datos.value + oculto.value + ','; }
          j++;
        }
        datos.value = datos.value + "%"
      } else { j = 2; }
      if ((tabla.rows[fila].cells)[i].innerText == "")
        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
      else
        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
   }
   
 }
 
 function refrescarLocal() 
		{			
			document.forms[0].target = "_self";	
			document.forms[0].action="JGR_ListarGuardiasLetrado.do";	
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}
		
	</script>

<!-- FIN: LISTA DE VALORES -->
		
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
<% if (!busquedaVolver.equals("volverNo")) { %>
		<siga:ConjBotonesAccion botones="V"  />	
<%  } else { %>
		<siga:ConjBotonesAccion botones="" />	
<%  } %>

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	
	</body>
</html>