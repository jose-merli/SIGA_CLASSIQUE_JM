<!-- stylesheet -->
<%@ page contentType="text/css" language="java" errorPage=""%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import="java.util.Properties"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src2 = (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	if (src2==null) {
	  SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz("2000");
	  src2=interfazGestor.getInterfaceOptions();	  
	}
	// RGG 14/03/2007 cambio para dar un tama�o a la letra y en caso de Tiems darle otro
	
	String fontSize = "11px";
	if (((String)src2.get("color.background")).equalsIgnoreCase("FFFFFF")) {
		fontSize="15px";
		if (((String)src2.get("font.style")).indexOf("Times")!=-1) {
			fontSize="17px";
		}
	} else{
	
		if (((String)src2.get("font.style")).indexOf("Arial")>=0) {
			fontSize="13px";
		}
		if (((String)src2.get("font.style")).indexOf("Times")>=0) {
		    fontSize="14px";
			
		}
	} 
	
%>

div {
	margin:0px;
	padding:0px;
}

iframe {
	margin-top:3px;
}

h1 {
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
}

td {
	vertical-align: middle;
}

textarea {
	width:300px;
}



BODY {
	margin-top : 0px;
	margin-left : 0px;
	margin-bottom : 0px;
	margin-right : 0px;
	text-align: left;
	vertical-align: top;
	font-size: <%=fontSize%>;
	background-color : #<%=src2.get("color.background")%>;
	width: 100%;

/* color tinto
 scrollbar-track-color: #F9EDF1;
 scrollbar-arrow-color: #888888;
 
 scrollbar-shadow-color: E6BDC8;
 scrollbar-face-color:  E6BDC8;
 scrollbar-darkshadow-color: E6BDC8;	
 scrollbar-base-color: E6BDC8;
*/
}

select {
	border: 1px solid #<%=src2.get("color.button.border")%>;
}

checkbox {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: -4px;
	margin-left: 0px;
	color:#<%=src2.get("color.labelText")%>;
	background-color: #<%=src2.get("color.background")%>;
	
}
radio {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: -4px;
	margin-left: 0px;
	color:#<%=src2.get("color.labelText")%>;
	background-color: #<%=src2.get("color.background")%>;
	
}

fieldset {
	border: 1px solid #<%=src2.get("color.labelText")%>;	
	text-align: left;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	color: #<%=src2.get("color.labelText")%>;
	padding: 1px;
	vertical-align: top;
	margin-left: 2px;
	margin-right: 2px;
	margin-top: 2px;
	margin-bottom: 1px;
}

legend {
	color:#<%=src2.get("color.labelText")%>;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	padding-top:0px;
	margin-top:0px;
}

a.disabled {
	opacity: 0.5;
  	pointer-events: none;
  	cursor: default;
}

.valorTop {
	text-align: left;
	margin: auto;
	padding-left: 2px;
	padding-right: 2px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
	background-color : #<%=src2.get("color.background")%>;
}


.labelTextNum {
	text-align: right;
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.labelTextValue{
	text-align: left;
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
	font-size: <%=fontSize%>;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
} 
.txGrandeActivo {
	text-align: left;
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
	font-size: 14pt;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}
.txGrandeBaja {
	text-align: left;
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
	font-size: 14pt;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.labelTextValor {
	text-align: left;
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
	font-size: <%=fontSize%>;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.labelText {
	text-align: left;
	font-family: <%=src2.get("font.style")%>;
	color:#<%=src2.get("color.labelText")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
	font-weight: bold;
}


.labelTextCentro {
	text-align: center;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	color:#<%=src2.get("color.labelText")%>;
	padding-left: 3px;
	padding-right: 3px;
	padding-top: 2px;
	padding-bottom: 1px;
	font-weight: bold;
	vertical-align: top;
}


.nonEdit {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	vertical-align: middle;
	text-align: left;
	padding-top: 2px;
	padding-bottom: 1px;
	color:#<%=src2.get("color.labelText")%>;
}

.nonEditRed {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: bold;

	margin: auto;
	padding-left: 5px;
	vertical-align: middle;
	text-align: left;
	padding-top: 2px;
	padding-bottom: 1px;
}



.tableTitle {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #<%=src2.get("color.tableTitle.font")%>;
	height: 25px;
	margin: auto;
	padding-left: 2px;
	vertical-align: middle;
	padding-top: 2px;
	text-align: center;
	background-color: #<%=src2.get("color.labelTable")%>;
}

.tableTitlePrimero {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #<%=src2.get("color.tableTitle.font")%>;
	margin: auto;
	padding-left: 2px;
	vertical-align: middle;
	padding-top: 2px;
	text-align: center;
	background-color: #<%=src2.get("color.labelTable")%>;
}

.filaTablaPar, .even{
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left;
	padding-top: 0px;
	padding-bottom: 0px;
	color: #<%=src2.get("color.nonEdit.selected.font")%>;
	width:30px;
	background-color: #<%=src2.get("color.fila.par.BG")%>;
}

.filaTablaImpar, .odd {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left;
	padding-top: 0px;
	padding-bottom: 0px;
	color: #<%=src2.get("color.nonEdit.selected.font")%>;
	width:30px;
	background-color: #<%=src2.get("color.fila.impar.BG")%>;
}

.box {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color:#<%=src2.get("color.labelText")%>;
	background-color: #<%=src2.get("color.button.BG")%>;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}
.boxNumber {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	text-align: right;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color: #<%=src2.get("color.button.font")%>;
	background-color: #<%=src2.get("color.button.BG")%>;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}





.boxMayuscula {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	text-transform: uppercase;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color:#<%=src2.get("color.labelText")%>;
	background-color:  #<%=src2.get("color.button.BG")%>;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}




.boxDisabled {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	color:#<%=src2.get("color.labelText")%>;
//	background-color:  #<%=src2.get("color.button.BG")%>;
	vertical-align: top;
	margin-top: -3px;
	margin-left: 0px;
	background-color: #<%=src2.get("color.background")%>;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxConsulta { 
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	color:#<%=src2.get("color.labelText")%>;
	background-color: #<%=src2.get("color.background")%>;
	border: 0px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxComboConsulta { 
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	color:#<%=src2.get("color.labelText")%>;
	background-color: transparent;
	border: 0px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxComboEnTabla { 
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	background-color: transparent;
	border: 0px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}


.boxConsultaNumber { 
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: right;
	color:#<%=src2.get("color.labelText")%>;
	background-color: #<%=src2.get("color.background")%>;
	border: 0px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
	
}

.boxConsultaLabelText {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	vertical-align: top;
	text-align: left;
	//padding-top: 2px;
	//padding-bottom: 3px;
	background-color:transparent;
	background-color: #<%=src2.get("color.background")%>;
	margin-top: -4px;
	border: 0px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}


.boxCabecera {
	color: #333333;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	width: 500px;
	background-color: #<%=src2.get("color.button.BG")%>;
	color: #<%=src2.get("color.button.font")%>;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	vertical-align: top;
}

.boxDisabledNumber {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
//	background-color:  #<%=src2.get("color.button.BG")%>;
	background-color: #<%=src2.get("color.background")%>;
	text-align: right;
	vertical-align: top;
	margin-top: -3px;
	margin-left: 0px;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
	
}

.boxLogin {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	margin-top: -2px;
	margin-left: 0px;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	color: #<%=src2.get("color.button.font")%>;
	background-color: #<%=src2.get("color.button.BG")%>;	
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxCombo {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: 0px;	
	margin-left: 0px;	
	color: #<%=src2.get("color.button.font")%>;
	background-color: #<%=src2.get("color.button.BG")%>;
	border: 1px solid #<%=src2.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.button {
	font-family: <%=src2.get("font.style")%>;
	color: #<%=src2.get("color.button.font")%>;
	background-color: #<%=src2.get("color.button.BG")%>;
	border: thin solid #<%=src2.get("color.button.border")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	cursor: hand;
	clear: right;
	margin:0px;
	padding:0px;
	height:22px;
}
.buttonEnTabla {
	font-family: <%=src2.get("font.style")%>;
	color: #<%=src2.get("color.button.font")%>;
	background-color: #<%=src2.get("color.button.BG")%>;
	border: thin solid #<%=src2.get("color.button.border")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	cursor: hand;
	clear: right;
	margin:0px;
	padding:0px;
	padding-top: 1px;
	padding-bottom: 0px;
	vertical-align: top;
}




.titlebar {
	font-family: <%=src2.get("font.style")%>;
	background-color : #<%=src2.get("color.titleBar.font")%>;
	font-size: <%=fontSize %>;
	text-align: center;
	font-weight: normal;
	color:#<%=src2.get("color.titleBar.BG")%>;
}


.titulos {
	FONT-WEIGHT: normal;
	text-align:right;
	FONT-SIZE: 17px; 
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src2.get("color.labelTable")%>;
	color:#<%=src2.get("color.titleBar.font")%>;
	padding-top: 2px;
	padding-right: 50px;
	padding-bottom: 2px;
	padding-left: 5px;
	border-top-width: 0px;
	border-bottom-width: 0px;
	border-top-style: none;
	border-bottom-style: none;
	height: 25px;
	
}

.titulosPeq {
	FONT-WEIGHT: normal; 
	font-size: <%=fontSize %>; 
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src2.get("color.labelTable")%>;
	color:#<%=src2.get("color.titleBar.font")%>;
	height: 25px;
	width:100%;
	padding: 2px;
	text-align:center;
	font:bold;
	vertical-align:middle;
}

.titulosLeft {
	FONT-WEIGHT: normal;
	text-align:left;
	FONT-SIZE: 17px; 
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src2.get("color.labelTable")%>;
	color:#<%=src2.get("color.titleBar.font")%>;
	padding-top: 1px;
	padding-right: 10px;
	padding-bottom: 1px;
	padding-left: 10px;
	border-top-width: 0px;
	border-bottom-width: 0px;
	border-top-style: none;
	border-bottom-style: none;
}
.titulitosDatos {
	FONT-WEIGHT: normal; 
	font-size: <%=fontSize %>; 
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src2.get("color.labelTable")%>;
	color:#<%=src2.get("color.titleBar.font")%>;
	height: 22px;
	width:100%;
	padding: 2px;
	text-align:center;
	font:bold;
	vertical-align:middle;
}

.titulitosDatosImp {
	FONT-WEIGHT: normal; 
	font-size: <%=fontSize %>; 
	
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	padding: 2px;
	background-color : #<%=src2.get("color.labelTable")%>;
	border: 1px solid #<%=src2.get("color.labelText")%>;
	color:#<%=src2.get("color.titleBar.font")%>;;
	height: 30px;
	width:100%;
	font:bold;
	vertical-align:middle;
}

.titulitos {
	font-size: <%=fontSize %>; 
	color:#<%=src2.get("color.titleBar.font")%>;
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	height: 26px;
	padding: 2px;
	width:100%;
	font-weight:bold;
	vertical-align:middle;
}

A.titulitos:link {
	CURSOR: hand; 
	COLOR: #<%=src2.get("color.titulos")%>; 
	FONT-STYLE: normal; 
	TEXT-DECORATION: none
}
A.titulitos:active {
	COLOR: #<%=src2.get("color.titulos")%>
	TEXT-DECORATION: none
}
A.titulitos:hover {
	COLOR: #<%=src2.get("color.titulos")%>
	TEXT-DECORATION: none
}

A.section:link {
	color:#<%=src2.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}

A.section:visited {
	color:#<%=src2.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}

A.section:hover {
	color:#<%=src2.get("color.link.activo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}

A.section:active {
	color:#<%=src2.get("color.link.activo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}


A.lb:link {
	color:#<%=src2.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}


A.lb:visited {
	color:#<%=src2.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}

A.lb:hover {
	color:#<%=src2.get("color.link.activo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}

A.lb:active {
	color:#<%=src2.get("color.link.activo")%>;
	font:bold 10,5px <%=src2.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}

.rayita { 
	 background-image:url(<%=app%>/html/imagenes/bgPpalS.gif); height: 1px
}

/* CoolMenus 4 - default styles - do not edit */
.clCMAbs{position:absolute; visibility:hidden; left:0; top:0}
/* CoolMenus 4 - default styles - end */
  
/*Style for the background-bar*/
.clBar{
	position:absolute;
	width:10;
	height:10;
	background-color:transparent;
	layer-background-color:transparent;
	visibility:hidden;
}

/*Styles for level 0*/
.clLevel0,.clLevel0over,.clLevel0NoLinkOver{position:absolute; padding: 2px;font-family:<%=src2.get("font.style")%>; font-size:<%=fontSize%>; font-weight:normal; text-align:center; 	border-bottom: 1px solid #FFFFFF;border-left: 1px solid #FFFFFF;border-right: 1px solid #FFFFFF;}
.clLevel0 {background-color : #<%=src2.get("color.menu.level0.BG")%>;layer-background-color:#<%=src2.get("color.menu.level0.BG")%>;color:#<%=src2.get("color.menu.level0.font")%>;}
.clLevel0over{background-color:#<%=src2.get("color.menu.level0.activo.BG")%>; layer-background-color:#<%=src2.get("color.menu.level0.activo.BG")%>; color:#<%=src2.get("color.menu.level0.activo.font")%>; cursor:pointer; cursor:hand; border-bottom: 1px solid #000000;border-left: 1px solid #000000;border-right: 1px solid #000000;}
.clLevel0NoLinkOver{background-color:#<%=src2.get("color.menu.level0.activo.BG")%>; layer-background-color:#<%=src2.get("color.menu.level0.activo.BG")%>; color:#<%=src2.get("color.menu.level0.activo.font")%>; cursor:default; }
.clLevel0border{position:absolute; visibility:hidden; background-color:transparent; layer-background-color:transparent}

/*Styles for level 1*/
.clLevel1, .clLevel1over, .clLevel1NoLinkOver{position:absolute; padding:2px; font-family:<%=src2.get("font.style")%>; font-size:<%=fontSize%>;font-weight:normal}
.clLevel1{background-color:#<%=src2.get("color.menu.level1.BG")%>; layer-background-color:#<%=src2.get("color.menu.level1.BG")%>; color:#<%=src2.get("color.menu.level1.font")%> ;	border-bottom: 1px solid #FFFFFF;border-left: 1px solid #FFFFFF;;border-right: 1px solid #FFFFFF;}
.clLevel1over{background-color:#<%=src2.get("color.menu.level1.activo.BG")%>; layer-background-color:#<%=src2.get("color.menu.level1.activo.BG")%>; color:#<%=src2.get("color.menu.level1.activo.font")%>; cursor:pointer; cursor:hand;border-bottom: 1px solid #000000;border-left: 1px solid #000000;border-right: 1px solid #000000; }
.clLevel1NoLinkOver{background-color:#<%=src2.get("color.menu.level1.activo.BG")%>; layer-background-color:#<%=src2.get("color.menu.level1.activo.BG")%>; color:#<%=src2.get("color.menu.level1.activo.font")%>;}
.clLevel1border{position:absolute; visibility:hidden; layer-background-color:#006699}

/*Styles for level 2*/
.clLevel2, .clLevel2over{position:absolute; padding:2px; font-family:<%=src2.get("font.style")%>; font-size:<%=fontSize%>;font-weight:normal}
.clLevel2{background-color:#<%=src2.get("color.menu.level2.BG")%>; layer-background-color:#<%=src2.get("color.menu.level2.BG")%>; color:#<%=src2.get("color.menu.level2.font")%>;		border-bottom: 1px solid #FFFFFF;border-left: 1px solid #FFFFFF;border-right: 1px solid #FFFFFF;}
.clLevel2over{background-color:#<%=src2.get("color.menu.level2.activo.BG")%>; layer-background-color:#<%=src2.get("color.menu.level2.activo.BG")%>; color:#<%=src2.get("color.menu.level2.activo.font")%>; cursor:pointer; cursor:hand; border-bottom: 1px solid #000000;border-left: 1px solid #000000;border-right: 1px solid #000000;}
.clLevel2border{position:absolute; visibility:hidden; }


A.imageLink:link {
	color:#<%=src2.get("color.labelText")%>;;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration:none
}

A.imageLink:visited {
	color:#<%=src2.get("color.labelText")%>;;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration:none
}

A.imageLink:hover {
	color:#<%=src2.get("color.labelText")%>;;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration:none
}
	

A.imageLink:active {
	color:#<%=src2.get("color.labelText")%>;;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration:none
}

.nonEditSelected {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: bold;
	color: #<%=src2.get("color.nonEdit.selected.font")%>;
	margin: auto;
	padding-left: 5px;
	vertical-align: middle;
	text-align: left;
	padding-top: 3px;
	background-color: #<%=src2.get("color.nonEdit.selected.BG")%>;
	padding-bottom: 3px;
}

.tablaCentralMedia {
	background-color : #<%=src2.get("color.titleBar.BG")%>;
	width: 100%;
}
.tablaCentralPeque {
	background-color : #<%=src2.get("color.titleBar.BG")%>;
	width: 100%;
}

.tablaCentralGrande {
	background-color : #<%=src2.get("color.titleBar.BG")%>;
	width: 100%;
}

.tablaCentralCampos {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.tablaCentralCamposImp {
	background-color: #<%=src2.get("color.background")%>;
	width: 670;
}

.tablaCentralCamposMedia {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.tablaCentralCamposGrande {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.tablaCentralCamposPeque {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.tablaCampos {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.tablaTitulo {
	width: 100%;
	background-color : #<%=src2.get("color.labelTable")%>;
	margin:0px;
	padding:0px;	
	border: 1px solid #<%=src2.get("color.labelText")%>;
	
}

.tdBotones {
	text-align: right;
	color:#<%=src2.get("color.labelText")%>;
	padding-left: 7px;
	padding-right: 10px;
	padding-top: 1px;
	padding-bottom: 1px;
	margin:0px;
	width: 10px;
	font:bold;
	vertical-align:middle;	
	
}

.tdMensaje {
	text-align: center;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	color:#<%=src2.get("color.labelText")%>;
	padding-left: 5px;
	padding-right: 5px;
	padding-top: 5px;
	padding-bottom: 5px;
	width: 100%;
}

.tableCabecera {
	color: #<%=src2.get("color.tableTitle.font")%>;
	padding-left: 0px;
	vertical-align: left;
	padding-top: 0px;
	text-align: left;
	
}

.posicionPestanas {
	position:relative; width:100%; height:32; z-index:2; top: 0px; left: 0px
}

.frameGeneral {
	position:relative; width:100%; height:100px; z-index:2; top:-3px; left: 0px;
}

.framePestanas {
	position:relative; width:100%; height:100px; z-index:2; top:-3px; left: 0px;
	margin-top:0px;
}


.botonesDetalleSinPosicionFija {
	background-color : #<%=src2.get("color.tableTitle.BG")%>;
	width:100%; 
	height:26px; 
	z-index:2; 
	bottom: 0px; 
	left:0px;
	border: 0px solid #<%=src2.get("color.labelText")%>;	
	margin:0px;
	padding:0px;
}

.botonesDetalle {
	background-color : #<%=src2.get("color.tableTitle.BG")%>;
	position:absolute; 
	width:100%; 
	height:26px; 
	z-index:2; 
	bottom: 0px; 
	left:0px;
	border: 0px solid #<%=src2.get("color.labelText")%>;	
	margin:0px;
	padding:0px;
}

.botonesSeguido {
	background-color : #<%=src2.get("color.tableTitle.BG")%>;
	position:relative;
	width:100%; 
	height:26px; 
	border: 0px solid #<%=src2.get("color.labelText")%>;	
	margin:0px;
	padding:0px;
}


.posicionPrincipal {
	position:absolute; width:1000px; height:600px; z-index:3; top: 102px; left: 0px;
	border-bottom: 0px solid #<%=src2.get("color.labeltable")%>;
	border-left: 0px solid #<%=src2.get("color.labeltable")%>;
	border-right: 0px solid #<%=src2.get("color.labeltable")%>;
	border-top: 0px solid #<%=src2.get("color.labeltable")%>;
	margin:0px;
	margin-top:3px; 	
}

.posicionTitulo { 
	position:absolute; left:0px; width:1000px; top:80px; z-index: 5;
}
	

.posicionBusquedaSolo {
	background-color: #<%=src2.get("color.background")%>;
	margin: 0px;
	padding: 0px;
	width:100%;
	height:600px;
}
.posicionBusquedaSoloImp {
	background-color: #<%=src2.get("color.background")%>;
	left:0px; width=670px; top:0px; 
	
}
.posicionFramePrincipal {
	position:absolute; width:100%; height:87%; z-index:3; top: 80px; left: 0px;
}


	
.pest {
	<!--font-family: Arial, Helvetica, sans-serif;-->
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #000000;
	text-decoration:none;
	height: 18px;
	padding: 4px 0px;

	
}

.pest a{
	background: url(<%=app%>/html/imagenes/<%=src2.get("color.fondo.pesatana")%>) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 5px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src2.get("color.background")%>;
	
}
.pest a:hover {
	background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 5px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src2.get("color.background")%>;
}
.pest a.here {
	background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 5px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src2.get("color.background")%>;
}
	
.listaNonEdit {
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left;
	padding-top: 0px;
	padding-bottom: 0px;
	background-color : #<%=src2.get("color.nonEdit")%>;
	color: #<%=src2.get("color.nonEdit.selected.font")%>;
	

}

.posicionPest {
	position:absolute; left:0px; width=100%; top:10px; z-index: 5; 
}

.listaNonEditSelected {
	background-color: #<%=src2.get("color.nonEdit.selected.BG")%>;
}

.tablaLineaPestanas {
	background-color: #333333;
	width: 100%;
	height: 5px;
	
}
.tablaLineaPestanasArriba {
    width: 100%;
	height: 5px;
}

.posicionModalGrande {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.posicionModalMedia {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.posicionModalPeque {
	background-color: #<%=src2.get("color.background")%>;
	width: 100%;
}

.detallePestanas {
	background-color: #<%=src2.get("color.background")%>;
}

.paginator {
	font-size: <%=fontSize %>; 
	COLOR: #<%=src2.get("color.tableTitle.font")%>; 
	background-color: #<%=src2.get("color.tableTitle.BG")%>;
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	padding: 1px;
}

.tPaginator {
	font-size: <%=fontSize %>; 
	COLOR: #<%=src2.get("color.tableTitle.font")%>; 
	background-color: #<%=src2.get("color.tableTitle.BG")%>;
	FONT-FAMILY: <%=src2.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	padding: 0px;
	height=20px;
	width="100%";

}
.boxConsultaPaginador { 
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: bold;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	color:#<%=src2.get("color.labelText")%>;
	background-color: #<%=src2.get("color.tableTitle.BG")%>;
	border: 0px solid #<%=src2.get("color.button.border")%>;
	padding-top: 0px;
	padding-bottom: 0px;
}



a.paginator:link {
	CURSOR: hand; 
	COLOR: #<%=src2.get("color.titulos")%>; 
	FONT-STYLE: normal; 
	TEXT-DECORATION: none
}
a.paginator:visited {
	COLOR: #<%=src2.get("color.titulos")%>;
	TEXT-DECORATION: none
}
a.paginator:hover {
	COLOR: #<%=src2.get("color.titulos")%>;
	TEXT-DECORATION: none
}
a.paginator:active {
	COLOR: #<%=src2.get("color.titulos")%>;
	TEXT-DECORATION: none
}

.tooltipo {
	background-color: #<%=src2.get("color.tableTitle.BG")%>;
	border: solid 1px #999999;
	vertical-align: middle;
	padding-right: 5px;
	padding-left: 5px;
	padding-top: 1px;
	padding-bottom: 4px;
	
}
.tooltipoletra {
	font-size: 12;
	color: #<%=src2.get("color.tableTitle.font")%>;
}

#iconoboton {
	margin-bottom:-3px;
}
 

 
 

th.sorted a, th.sortable a {
	color: #<%=src2.get("color.tableTitle.font")%>;
	text-decoration:none;
	background-position: right;
	display: block;
}

th.sortable a, th.order1 a, th.order2 a   {
	background-repeat: no-repeat;
	background-position: left;
	text-align: center;
}

th.sortable a {
	background-image: url('/SIGA/html/imagenes/arrow_off.png');
}

th.order1 a {
	background-image: url('/SIGA/html/imagenes/arrow_up.png');
}

th.order2 a {
	background-image: url('/SIGA/html/imagenes/arrow_down.png');
}

th.sorted {
	background-color: #<%=src2.get("color.nonEdit.selected.BG")%>;
}

span.export {
	padding: 0 4px 1px 20px;
	display: inline;
	display: inline-block;
	cursor: pointer;
}

span.excel {
	background-image: url('/SIGA/html/imagenes/ico_file_excel.png');
	background-repeat:no-repeat;
	padding-right:10px;
}

span.csv {
	background-image: url('/SIGA/html/imagenes/ico_file_csv.png');
	background-repeat:no-repeat;
	padding-right:10px;
}

span.xml {
	background-image: url('/SIGA/html/imagenes/ico_file_xml.png');
	background-repeat:no-repeat;
	padding-right:10px;
}

span.pdf {
	background-image: url('/SIGA/html/imagenes/ico_file_pdf.png');
	background-repeat:no-repeat;
	padding-right:10px;
}

span.rtf {
	background-image: url('/SIGA/html/imagenes/ico_file_rtf.png');
	background-repeat:no-repeat;
	padding-right:10px;
}

div.banner{
	text-align:right;
	background-color: #<%=src2.get("color.labelTable")%>;
	height:20px;
}

span.pagebanner {
	float:left;
	font-size: <%=fontSize %>; 
	COLOR: #<%=src2.get("color.tableTitle.font")%>; 
	FONT-FAMILY: <%=src2.get("font.style")%>;
	font-weight: bold;
	padding-left:10px;
}

span.pagelinks {
	padding: 2px 5px 2px 5px;
	COLOR: #<%=src2.get("color.tableTitle.font")%>; 
	FONT-FAMILY: <%=src2.get("font.style")%>;
	padding-right:10px;
}

span.pagelinks a{
	COLOR: #<%=src2.get("color.tableTitle.font")%>; 
}


div.exportlinks {
	border: 1px dotted #999;
	padding: 2px 5px 2px 5px;
}

table.dataScroll, table.dataScroll td, table.dataScroll th{
	border-collapse: collapse;
	border-width: 1px 1px 1px 1px;
	border-style: solid solid solid solid;
}
table.dataScroll th{
	text-align:center;
}

table.dataScroll td{
	word-wrap: break-word;
}
	
table.dataScroll{
	border-top-style: none;
}
	
form {
	margin:0px;
}

thead {
	border: 0px 1px 1px 1px;
	font-family: <%=src2.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #<%=src2.get("color.tableTitle.font")%>;
	height: 25px;
	margin: auto;
	padding-left: 2px;
	vertical-align: middle;
	padding-top: 0px;
	margin-top: 0px
	text-align: center;
}
table.dataScroll th{
	background-color: #<%=src2.get("color.labelTable")%>;
	padding-top: 0px;
	margin-top: 0px;
	border-top: 1px;
}

.hidden{
	display: none;
}