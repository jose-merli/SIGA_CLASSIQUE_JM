<!-- altaTurno_2.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<% 	
	String app		= request.getContextPath();
	HttpSession ses	= request.getSession();
	UsrBean usr		= (UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src	= (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole 	= (Colegio)ses.getAttribute("colegio");
	Vector obj 		= (Vector) request.getAttribute("resultado");
	// Obtenemos los datos del request.
	String paso						= String.valueOf(request.getAttribute("paso"));
  	String titulo					= String.valueOf(request.getAttribute("titulo"));
	String botones					= String.valueOf(request.getAttribute("botones"));
  	String action 					= String.valueOf(request.getAttribute("action"));
  	String origen 					= String.valueOf(request.getAttribute("origen"));
	String idretencion 				= String.valueOf(request.getAttribute("idretencion"));
	String guardias 				= String.valueOf(request.getAttribute("GUARDIAS"));
	String idpersona				= String.valueOf(request.getAttribute("IDPERSONA"));
	String idturno 					= String.valueOf(request.getAttribute("idturno"));
	String idinstitucion			= String.valueOf(request.getAttribute("IDINSTITUCION"));
	String fechasolicitud 			= String.valueOf(request.getAttribute("FECHASOLICITUD"));
	String fechabaja 			= String.valueOf(request.getAttribute("FECHABAJA"));
	String observacionessolicitud 	= String.valueOf(request.getAttribute("OBSERVACIONESSOLICITUD"));
	String fechasolicitudbaja 		= String.valueOf(request.getAttribute("FECHASOLICITUDBAJA"));
	String observacionesbaja 		= String.valueOf(request.getAttribute("OBSERVACIONESBAJA"));
	String fechavalidacion 			= String.valueOf(request.getAttribute("FECHAVALIDACION"));
	String observacionesvalidacion 	= String.valueOf(request.getAttribute("OBSERVACIONESVALIDACION"));
	String validarinscripciones 	= String.valueOf(request.getAttribute("VALIDARINSCRIPCIONES"));
	String solBaja = (String) request.getAttribute("solBaja"); 
     if (solBaja==null) solBaja="";
	String activarCheckGeneral=""; 
   
	
%>

<html>
	<head>
	<title><script>alert(top.title)</script></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
</head>

<body onload="activarDesactivarCheck();"> 

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
				<siga:Idioma key="<%=titulo%>"/>
		</td>
	</tr>
	</table>
	 

	<% 	String nC="";
		String tC="";
		String alto="";
		if (solBaja!=null && !solBaja.equals("")||(origen == null || (origen != null && !origen.equals("/JGR_AltaTurnosGuardias")))){
	     nC="gratuita.altaTurnos_2.literal.nombre,gratuita.altaTurnos_2.literal.nletrados,gratuita.altaTurnos_2.literal.tipodias,gratuita.altaTurnos_2.literal.diasguardia,";
		 tC="40,15,15,15,15";
		
		}else{
		 if(guardias.equalsIgnoreCase("0")){
		  nC="<input type='checkbox' name='chkGeneral' checked disabled onclick='marcarDesmarcarTodos(this)'/>,gratuita.altaTurnos_2.literal.nombre,gratuita.altaTurnos_2.literal.nletrados,gratuita.altaTurnos_2.literal.tipodias,gratuita.altaTurnos_2.literal.diasguardia,";
		 }else{
		  if(guardias.equalsIgnoreCase("2")){
		   nC="<input type='checkbox' name='chkGeneral'   onclick='marcarDesmarcarTodos(this)'/>,gratuita.altaTurnos_2.literal.nombre,gratuita.altaTurnos_2.literal.nletrados,gratuita.altaTurnos_2.literal.tipodias,gratuita.altaTurnos_2.literal.diasguardia,";
		  }else{
		   nC="<input type='checkbox' name='chkGeneral'  disabled onclick='marcarDesmarcarTodos(this)'/>,gratuita.altaTurnos_2.literal.nombre,gratuita.altaTurnos_2.literal.nletrados,gratuita.altaTurnos_2.literal.tipodias,gratuita.altaTurnos_2.literal.diasguardia,";
		  }
		 }
		 tC="5,35,15,15,15,15";
		}
		alto="370";
		
//		name='checkGeneral'
		
	%>
	
	<html:form action="JGR_AltaTurnosGuardias.do" method="post" target="submitArea">

	<input type="hidden" name="solBaja" value="<%=solBaja%>"/>
		<input type="hidden" name="paso" value="0">
		<input type="hidden" name="origen" value="altaTurno_2">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="guardias" value="<%=guardias%>">
		<input type="hidden" name="idPersona" value="<%=idpersona%>">
		<input type="hidden" name="idRetencion" value="<%=idretencion%>">
		<input type="hidden" name="idTurno" value="<%=idturno%>">
		<input type="hidden" name="idInstitucion" value="<%=idinstitucion%>">
		<input type="hidden" name="fechaSolicitud" value="<%=fechasolicitud%>">
		<input type="hidden" name="observacionesSolicitud" value="<%=observacionessolicitud%>">
		<input type="hidden" name="fechaSolicitudBaja" value="<%=fechasolicitudbaja%>">
		<input type="hidden" name="observacionesBaja" value="<%=observacionesbaja%>">
		<input type="hidden" name="fechaValidacion" value="<%=fechavalidacion%>">
		<input type="hidden" name="fechaBaja" value="<%=fechabaja%>">
		<input type="hidden" name="observacionesValidacion" value="<%=observacionesvalidacion%>">
		<input type="hidden" name="validarInscripciones" value="<%=validarinscripciones%>">
		<input type="hidden" name="guardiasSel" value="">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			
		</html:form>	
		

		<siga:TablaCabecerasFijas 
		   nombre="altaTurno"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   alto="<%=alto%>"
		   ajuste="120"
		  >
			<%
			if (obj.size()==0)
			{	 
				// no hay registros
			%>
		 		<br><br>
		   		 <p class="labelText" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 		<br><br>	 		
			<%
			} else {
				// hay registros
		    int recordNumber=1;
			while ((recordNumber) <= obj.size())
			{	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			%>
				<tr>
 			
				<% 
				String visibleConsulta="true";
				if (paso!=null && paso.equals("guardia")) { 
					visibleConsulta="false";
				}
				String tipoDia = (String)hash.get("TIPODIASGUARDIA");
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
 					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="" clase="listaNonEdit">
	               <%if ((solBaja==null|| solBaja.equals(""))&&(origen != null && origen.equals("/JGR_AltaTurnosGuardias"))){%> 				
			          <td align="center">
					   <input type="hidden" name="guardia_<%=String.valueOf(recordNumber)%>" value="<%=(String)hash.get("IDGUARDIA")%>">
					   <input type="hidden" name="limpiarFilaSeleccionada" value="">
					   <input type="checkbox" value="<%=String.valueOf(recordNumber)%>" name="chkGuardia"  >
					  </td>  
					<%}%>
					<td><%=hash.get("NOMBRE")%></td>
					<td><%=hash.get("NUMEROLETRADOSGUARDIA")%></td>
					<td><%=ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr)%>
						
					</td>
					<td><%=hash.get("DIASGUARDIA")%>&nbsp;<siga:Idioma key="<%=literalDuracion%>"/></td>
				</siga:FilaConIconos>
				</tr>
				
				<% recordNumber++;
			} %>
		<%}%>

		</siga:TablaCabecerasFijas>



	<div style="position:absolute;bottom:35px;left:0px;width:100%">
		<table width="100%">
		<tr>
		<%
			if(obj.size() >0 && (origen != null && origen.equals("/JGR_AltaTurnosGuardias")))
			{
				if(guardias.equalsIgnoreCase("0")){ %>
					<td class="labelText" colspan="6">		
						<INPUT NAME="gu" TYPE=RADIO VALUE="0" checked >
						<siga:Idioma key="gratuita.altaTurnos_2.literal.obligatorias"/>
					</td>
				<%}
				else if(guardias.equalsIgnoreCase("1")){%>
					<td class="labelText" colspan="6">		
						<INPUT NAME="gu" TYPE=RADIO VALUE="0" onClick="javascript:document.forms[0].guardias.value='0';marcarTodos()">
						<siga:Idioma key="gratuita.altaTurnos_2.literal.todas"/>
						&nbsp;
						<INPUT NAME="gu" TYPE=RADIO VALUE="1" checked  onClick="javascript:document.forms[0].guardias.value='1';desmarcarTodos()">
						<siga:Idioma key="gratuita.altaTurnos_2.literal.ninguna"/>
					</td>
				<%}
				else if(guardias.equalsIgnoreCase("2")){%>
					<td class="labelText" colspan="6">		
						<INPUT NAME="gu" TYPE=RADIO VALUE="2" checked disabled  > 
						<siga:Idioma key="gratuita.altaTurnos_2.literal.aeleccion"/>
					</td>
			<%}}%>
		</tr>
		</table>
	</div>

			<% 			
			if(action.equalsIgnoreCase("/JGR_ValidarTurnos"))
			{%>
	<div style="position:absolute;bottom:80px;left: 0px;width:100%;">
		<p class="labelText" style="text-align:center">
				<siga:Idioma key="gratuita.altaTurno_2.literal.todas" />
		</p>
	</div>
			<%}%>

	<siga:ConjBotonesAccion botones="<%=botones%>"  />	



	<!-- ******* BOTONES DE ACCIONES ****** -->

	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() 
		{		
			top.volver();
		}

		function accionCerrar() 
		{		
			window.close();
		}
		
		function accionCancelar() 
		{		
			window.close();
		}
		
		function accionGuardar() 
		{		
	
		}
		
		function accionGuardarCerrar() 
		{		
	
		}
        function marcarDesmarcarTodos(o) 
		{ 
				var ele = document.getElementsByName("chkGuardia");
				for (i = 0; i < ele.length; i++) {
					ele[i].checked = o.checked;
					
					<%if(guardias.equalsIgnoreCase("0")|| guardias.equalsIgnoreCase("1")){ %>
					  ele[i].disabled=true;
					 
					<%}else{%>
					  ele[i].disabled=false;
					 
					<%}%>
				}
				
	   }
	   function marcarTodos(){
	     var eleGeneral = document.getElementById("chkGeneral");
		  eleGeneral.checked=true;
		  marcarDesmarcarTodos(eleGeneral);
				
	   }
	    function desmarcarTodos(){
	      var eleGeneral = document.getElementById("chkGeneral");
		  eleGeneral.checked=false;
		  marcarDesmarcarTodos(eleGeneral);
	   }
	   function activarDesactivarCheck(){
	    <%if ((solBaja==null|| solBaja.equals(""))&&(origen != null && origen.equals("/JGR_AltaTurnosGuardias"))){%> // sólo cuando estamos en solicitud de alta en turno.	
	     <%if(guardias.equalsIgnoreCase("0")){ %>//guardias obligatorias
		   marcarTodos();
		 <%}else{%>
		 <%  if(guardias.equalsIgnoreCase("2")){ %>// guardias a elegir
		       desmarcarTodos();
		 <%   }else{%>//o todas o ninguna
		       
		       if(document.forms[0].guardias){
			   
			    if(document.forms[0].guardias.value=='1'){
			     desmarcarTodos();
			    }else{
			     marcarTodos();
			    } 
			   }
		 <%   }
		   }
		  }%>
		 
	   }
		<!-- Asociada al boton Siguiente -->
		/*function accionSiguiente() 
		{		
			//Submitimos en el propio frame:
			document.forms[0].target="_self";
			document.forms[0].action="<%=app%><%=action%>.do";
			document.forms[0].paso.value="<%=paso%>";
			document.forms[0].modo.value="nuevo";
			document.forms[0].submit();
		}*/
		
		function accionSiguiente() 
		{	sub();	
			//Submitimos en el propio frame:
			var accion="<%=action%>";
			// CASO EN QUE VENIMOS DE BAJA O DE VALIDACION
			if(accion != "null")
			{
				document.forms[0].target="_self";
				document.forms[0].action="<%=app%><%=action%>.do";
				document.forms[0].paso.value="<%=paso%>";
				document.forms[0].modo.value="nuevo";
				document.forms[0].submit();
			}
			//CASO EN QUE VENIMOS DE ALTA DE TURNO
			else
			{ var guardiasAlta="";
			    var ele = document.getElementsByName("chkGuardia");
				var guardia="";
				 var j;
				for (i = 0; i < ele.length; i++) {
				 j=i+1;
				
				  if(ele[i].checked){
				 
				   guardia=document.getElementById("guardia_"+j);
				   guardiasAlta+=guardia.value+"@";
				  
				   
				  }
				}
				document.forms[0].guardiasSel.value=guardiasAlta;
				document.forms[0].target="_self";
				document.forms[0].modo.value="editarTelefonoGuardia";
				document.forms[0].submit();
			}
		}
		
		<!-- Asociada al boton Finalizar -->
		function accionFinalizar() 
		{		
			sub();
			document.forms[0].modo.value="insertar";
			document.forms[0].submit();
			window.returnValue="INSERTADO";			
		}

	</script>


<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
	</body>
</html>
