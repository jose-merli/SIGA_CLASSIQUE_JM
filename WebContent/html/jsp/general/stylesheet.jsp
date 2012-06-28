	<!-- stylesheet.jsp -->
<%@ page contentType="text/css" language="java" errorPage=""%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import="java.util.Properties"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	if (src==null) {
	  SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz("2000");
	  src=interfazGestor.getInterfaceOptions();	  
	}
	// RGG 14/03/2007 cambio para dar un tamaño a la letra y en caso de Tiems darle otro
	
	String fontSize = "11px";
	if (((String)src.get("color.background")).equalsIgnoreCase("FFFFFF")) {
		fontSize="15px";
		if (((String)src.get("font.style")).indexOf("Times")!=-1) {
			fontSize="17px";
		}
	} else{
	
		if (((String)src.get("font.style")).indexOf("Arial")>=0) {
			fontSize="13px";
		}
		if (((String)src.get("font.style")).indexOf("Times")>=0) {
		    fontSize="14px";
			
		}
	} 
	
%>

div {
	margin: 0px;
	padding: 0px;
}

iframe {
	/* margin-top:3px; */
}

h1 {
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
}

td {
	vertical-align: top;
}

td.disabled {
	opacity: 0.5;
	color: <%=src.get("color.link.inactivo")%>;
}

textarea {
	width: 100%;
}

form {
	margin:0px;
	padding:0px;
	/*height:0px;*/
}

BODY {
	margin-top : 0px;
	margin-left : 0px;
	margin-bottom : 0px;
	margin-right : 0px;
	text-align: left;
	vertical-align: top;
	font-size: <%=fontSize%>;
	background-color : #<%=src.get("color.background")%>;
	font-size: <%=fontSize %>;
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
	border: 1px solid #<%=src.get("color.button.border")%>;
}

checkbox {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: -4px;
	margin-left: 0px;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.background")%>;	
}

radio {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: -4px;
	margin-left: 0px;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.background")%>;	
}

fieldset {
	border: 1px solid #<%=src.get("color.labelText")%>;	
	text-align: left;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	color: #<%=src.get("color.labelText")%>;
	padding: 1px;
	vertical-align: top;
	margin-left: 2px;
	margin-right: 2px;
	margin-top: 2px;
	margin-bottom: 1px;
}

legend {
	color:#<%=src.get("color.labelText")%>;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	padding-top:0px;
	margin-top:0px;
	margin-left: 6px;
	.margin-left: 0px;
	cursor:default;
}

a.disabled {
	opacity: 0.5;
  	pointer-events: none;
  	cursor: default;
}

.legendNoBorder {
	border-top:1px solid;
	border-right:1px solid;
	border-left:1px solid;
	border-bottom:0px solid;
	padding-right:0px;
}

.valorTop {
	text-align: left;
	margin: auto;
	padding-left: 2px;
	padding-right: 2px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
	background-color : #<%=src.get("color.background")%>;
}

.labelTextNum {
	text-align: right;
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
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
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
	font-size: <%=fontSize%>;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.txGrandeActivo {
	text-align: left;
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
	font-size: 14pt;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.txGrandeBaja {
	text-align: left;
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
	font-size: 14pt;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.labelTextValor {
	text-align: left;
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
	font-size: <%=fontSize%>;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
}

.labelText {
	text-align: left;
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
	font-weight: bold;
	cursor:default;
}

.labelTextArea{
	text-align: left;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.button.BG")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-left: 2px;
	padding-right: 2px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
} 

.labelTextCentro {
	text-align: center;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	color:#<%=src.get("color.labelText")%>;
	padding-left: 3px;
	padding-right: 3px;
	padding-top: 2px;
	padding-bottom: 1px;
	font-weight: bold;
	vertical-align: top;
}


.nonEdit {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	vertical-align: middle;
	text-align: left;
	padding-top: 2px;
	padding-bottom: 1px;
	color:#<%=src.get("color.labelText")%>;
}

.nonEditRed {
	font-family: <%=src.get("font.style")%>;
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
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #<%=src.get("color.tableTitle.font")%>;
	margin: auto;
	padding-left: 2px;
	vertical-align: middle;
	padding-top: 2px;
	text-align: center;
	background-color: #<%=src.get("color.labelTable")%>;
}

.tableTitlePrimero {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #<%=src.get("color.tableTitle.font")%>;
	margin: auto;
	padding-left: 2px;
	vertical-align: middle;
	padding-top: 2px;
	text-align: center;
	background-color: #<%=src.get("color.labelTable")%>;
}

#tablaDatos td {
	overflow: hidden;
}

.filaTablaPar {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left;
	padding-top: 0px;
	padding-bottom: 0px;
	color: #<%=src.get("color.nonEdit.selected.font")%>;
	width: 30px;
	background-color: #<%=src.get("color.fila.par.BG")%>;
}

.filaTablaImpar {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left;
	padding-top: 0px;
	padding-bottom: 0px;
	color: #<%=src.get("color.nonEdit.selected.font")%>;
	width: 30px;
	background-color: #<%=src.get("color.fila.impar.BG")%>;
}

.box {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.button.BG")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxNumber {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	text-align: right;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color: #<%=src.get("color.button.font")%>;
	background-color: #<%=src.get("color.button.BG")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxMayuscula {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	text-transform: uppercase;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color:#<%=src.get("color.labelText")%>;
	background-color:  #<%=src.get("color.button.BG")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxDisabled {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: 0px;
	margin-left: 0px;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.background")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxConsulta { 
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.background")%>;
	border: 0px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxComboConsulta { 
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	color:#<%=src.get("color.labelText")%>;
	background-color: transparent;
	border: 0px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxComboEnTabla { 
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	background-color: transparent;
	border: 0px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}


.boxConsultaNumber { 
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: right;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.background")%>;
	border: 0px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;	
}

.boxConsultaLabelText {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	vertical-align: top;
	text-align: left;
	//padding-top: 2px;
	//padding-bottom: 3px;
	background-color:transparent;
	background-color: #<%=src.get("color.background")%>;
	margin-top: -4px;
	border: 0px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}


.boxCabecera {
	color: #333333;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	width: 70%;
	background-color: #<%=src.get("color.button.BG")%>;
	color: #<%=src.get("color.button.font")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	vertical-align: top;
}

.boxDisabledNumber {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
//	background-color:  #<%=src.get("color.button.BG")%>;
	background-color: #<%=src.get("color.background")%>;
	text-align: right;
	vertical-align: top;
	margin-top: -3px;
	margin-left: 0px;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;	
}

.boxLogin {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	margin-top: -2px;
	margin-left: 0px;
	border: 1px solid #<%=src.get("color.button.border")%>;
	color: #<%=src.get("color.button.font")%>;
	background-color: #<%=src.get("color.button.BG")%>;	
	padding-top: 2px;
	padding-bottom: 1px;
}

.boxCombo {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	left: 25px;
	vertical-align: top;
	margin-top: 0px;	
	margin-left: 0px;	
	color: #<%=src.get("color.button.font")%>;
	background-color: #<%=src.get("color.button.BG")%>;
	border: 1px solid #<%=src.get("color.button.border")%>;
	padding-top: 2px;
	padding-bottom: 1px;
}

.button {
	font-family: <%=src.get("font.style")%>;
	color: #<%=src.get("color.button.font")%>;
	background-color: #<%=src.get("color.button.BG")%>;
	border: 2px solid  #<%=src.get("color.button.border")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	cursor: pointer;
	clear: right;
	margin :0px;
	padding-left: 14px;
	padding-right: 14px;
	.padding-left: 0px;
	.padding-right: 0px;
	height: 22px;
}

.buttonEnTabla {
	font-family:  <%=src.get("font.style")%>;
	color: white;
	background-color: #444444;
	border: thin solid white;
	font-size: <%=fontSize %>;
	font-weight: bold;
	cursor: pointer;
	clear: right;
	margin: 0px;
	padding: 0px;
	padding-top: 1px;
	padding-bottom: 0px;
	vertical-align: top;
	height: 22px;
}

.titlebar {
	font-family: <%=src.get("font.style")%>;
	background-color : #<%=src.get("color.titleBar.font")%>;
	font-size: <%=fontSize %>;
	text-align: center;
	font-weight: normal;
	color:#<%=src.get("color.titleBar.BG")%>;
}

.titulos {
	FONT-WEIGHT: normal;
	text-align:right;
	FONT-SIZE: 17px; 
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src.get("color.labelTable")%>;
	color:#<%=src.get("color.titleBar.font")%>;
	padding-top: 2px;
	padding-right: 50px;
	padding-bottom: 2px;
	padding-left: 5px;
	border-top-width: 0px;
	border-bottom-width: 0px;
	border-top-style: none;
	border-bottom-style: none;
	height: 25px;
	cursor: default;
}

.titulosPeq {
	FONT-WEIGHT: normal; 
	font-size: <%=fontSize %>; 
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src.get("color.labelTable")%>;
	color:#<%=src.get("color.titleBar.font")%>;
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
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src.get("color.labelTable")%>;
	color:#<%=src.get("color.titleBar.font")%>;
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
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	background-color : #<%=src.get("color.labelTable")%>;
	color:#<%=src.get("color.titleBar.font")%>;
	height: 22px;
	width:100%;
	padding: 2px;
	text-align:center;
	font:bold;
	vertical-align: middle;
}

.titulitosDatosImp {
	FONT-WEIGHT: normal; 
	font-size: <%=fontSize %>;	
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	padding: 2px;
	background-color : #<%=src.get("color.labelTable")%>;
	border: 1px solid #<%=src.get("color.labelText")%>;
	color:#<%=src.get("color.titleBar.font")%>;;
	height: 30px;
	width: 100%;
	font: bold;
	vertical-align: middle;
}

.titulitos {
	FONT-WEIGHT: bold; 
	font-size: <%=fontSize %>; 
	color:#<%=src.get("color.titleBar.font")%>;
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	height: 26px;
	padding: 2px;
	/* width: 100%; */
	font:bold;
	vertical-align:middle;
}

A.titulitos:link {
	CURSOR: pointer; 
	COLOR: #<%=src.get("color.titulos")%>; 
	FONT-STYLE: normal; 
	TEXT-DECORATION: none;
}
A.titulitos:active {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none;
}
A.titulitos:hover {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none;
}

A.section:link {
	color:#<%=src.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}

A.section:visited {
	color:#<%=src.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}

A.section:hover {
	color:#<%=src.get("color.link.activo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}

A.section:active {
	color:#<%=src.get("color.link.activo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 29px;
	width: 162px;
	text-align: center;
	vertical-align: text-bottom;
}


A.lb:link {
	color:#<%=src.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}


A.lb:visited {
	color:#<%=src.get("color.link.inactivo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}

A.lb:hover {
	color:#<%=src.get("color.link.activo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}

A.lb:active {
	color:#<%=src.get("color.link.activo")%>;
	font:bold 10,5px <%=src.get("font.style")%>;
	text-decoration: none;
	height: 15px;
	width: 90px;
	text-align: center;
	vertical-align: text-bottom;
}

.rayita { 
	 background-image:url(<%=app%>/html/imagenes/bgPpalS.gif); 
	 height: 1px;
}

/* CoolMenus 4 - default styles - do not edit */
.clCMAbs{
	position:absolute; 
	visibility:hidden; 
	left:0; 
	top:0;}
/* CoolMenus 4 - default styles - end */
  
/*Style for the background-bar*/
.clBar{
	position:absolute;
	width: 10px;
	height: 10px;
	background-color: transparent;
	layer-background-color: transparent;
	visibility: hidden;
}


/*Styles for level 0*/
.clLevel0,.clLevel0over,.clLevel0NoLinkOver {
	position: absolute;
	padding: 1px;
	font-family: <%= src.get ( "font.style") %>;
	font-size: 12px;
	font-weight: normal;
	text-align: center;
	border-bottom: 1px solid #FFFFFF;
	border-top: 1px solid #FFFFFF;
	border-left: 1px solid #FFFFFF;
	border-right: 1px solid #FFFFFF;
	border-style: solid;
}

.clLevel0 {
	background-color: #<%= src.get ( "color.menu.level0.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level0.BG" ) %>;
	color: #<%= src.get ( "color.menu.level0.font" ) %>;
}

.clLevel0over {
	background-color: #<%= src.get ( "color.menu.level0.activo.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level0.activo.BG" )%>;
	color: #<%= src.get ( "color.menu.level0.activo.font" ) %>;
	cursor: pointer;
	border-bottom: 1px solid #000000;
	border-left: 1px solid #000000;
	border-right: 1px solid #000000;
	border-top: 1px solid #000000;
	border-style: solid;
}

.clLevel0NoLinkOver {
	background-color: #<%= src.get ( "color.menu.level0.activo.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level0.activo.BG" )%>;
	color: #<%= src.get ( "color.menu.level0.activo.font" ) %>;
	cursor: default;
}

.clLevel0border {
	position: absolute;
	visibility: hidden;
	background-color: transparent;
	layer-background-color: transparent
}

/*Styles for level 1*/
.clLevel1,.clLevel1over,.clLevel1NoLinkOver {
	position: absolute;
	padding: 2px;
	font-family: <%= src.get ( "font.style") %>;
	font-size: <%= fontSize %>;
	font-weight: normal
}

.clLevel1 {
	background-color: #<%= src.get ( "color.menu.level1.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level1.BG" ) %>;
	color: #<%= src.get ( "color.menu.level1.font" ) %>;
	border-bottom: 1px solid #FFFFFF;
	border-top: 1px solid #FFFFFF;
	border-left: 1px solid #FFFFFF;
	border-right: 1px solid #FFFFFF;
	border-style: solid;
}

.clLevel1over {
	background-color: #<%= src.get ( "color.menu.level1.activo.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level1.activo.BG" )%>;
	color: #<%= src.get ( "color.menu.level1.activo.font" ) %>;
	cursor: pointer;
	border-bottom: 1px solid #000000;
	border-top: 1px solid #000000;
	border-left: 1px solid #000000;
	border-right: 1px solid #000000;
	border-style: solid;
}

.clLevel1NoLinkOver {
	background-color: #<%= src.get ( "color.menu.level1.activo.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level1.activo.BG" )
		%>;
	color: #<%= src.get ( "color.menu.level1.activo.font" ) %>;
}

.clLevel1border {
	position: absolute;
	visibility: hidden;
	layer-background-color: #006699
}

/*Styles for level 2*/
.clLevel2,.clLevel2over {
	position: absolute;
	padding: 2px;
	font-family: <%= src.get ( "font.style") %>;
	font-size: <%= fontSize %>;
	font-weight: normal
}

.clLevel2 {
	background-color: #<%= src.get ( "color.menu.level2.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level2.BG" ) %>;
	color: #<%= src.get ( "color.menu.level2.font" ) %>;
	//border-bottom: 1px solid #FFFFFF;
	//border-left: 1px solid #FFFFFF;
	//border-right: 1px solid #FFFFFF;
	border-bottom: 1px groove #FFFFFF;
	border-left: 1px groove #FFFFFF;
	border-right: 1px groove #FFFFFF;
	border-style: groove;
}

.clLevel2over {
	background-color: #<%= src.get ( "color.menu.level2.activo.BG" ) %>;
	layer-background-color: #<%= src.get ( "color.menu.level2.activo.BG" )
		%>;
	color: #<%= src.get ( "color.menu.level2.activo.font" ) %>;
	cursor: pointer;
	cursor: pointer;
	//border-bottom: 1px solid #000000;
	//border-left: 1px solid #000000;
	//border-right: 1px solid #000000;
	border-bottom: 1px groove #000000;
	border-left: 1px groove #000000;
	border-right: 1px groove #000000;
	border-style: groove;
}

.clLevel2border {
	position: absolute;
	visibility: hidden;
}


A.imageLink:link {
	color:#<%=src.get("color.labelText")%>;;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration: none;
}

A.imageLink:visited {
	color:#<%=src.get("color.labelText")%>;;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration: none;
}

A.imageLink:hover {
	color:#<%=src.get("color.labelText")%>;;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration: none;
}
	

A.imageLink:active {
	color:#<%=src.get("color.labelText")%>;;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-left: 5px;
	padding-top: 4px;
	padding-bottom: 4px;
	vertical-align: middle;	
	text-decoration: none;
}

.nonEditSelected {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: bold;
	color: #<%=src.get("color.nonEdit.selected.font")%>;
	margin: auto;
	padding-left: 5px;
	vertical-align: middle;
	text-align: left;
	padding-top: 3px;
	background-color: #<%=src.get("color.nonEdit.selected.BG")%>;
	padding-bottom: 3px;
}

.tablaCentralMedia {
	background-color : #<%=src.get("color.titleBar.BG")%>;
	width: 100%;
}
.tablaCentralPeque {
	background-color : #<%=src.get("color.titleBar.BG")%>;
	width: 100%;
}

.tablaCentralGrande {
	background-color : #<%=src.get("color.titleBar.BG")%>;
	width: 100%;
}

.tablaCentralCampos {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.tablaCentralCamposImp {
	background-color: #<%=src.get("color.background")%>;
	width: 670px;
}

.tablaCentralCamposMedia {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.tablaCentralCamposGrande {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.tablaCentralCamposPeque {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.tablaCampos {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.tablaTitulo {
	width: 100%;
	background-color : #<%=src.get("color.labelTable")%>;
	margin: 0px;
	padding: 0px;	
	border: 1px solid #<%=src.get("color.labelText")%>;	
}

.tdBotones {
	text-align: right;
	color:#<%=src.get("color.labelText")%>;
	padding-left: 7px;
	padding-right: 7px;
	padding-top: 1px;
	padding-bottom: 1px;
	margin: 0px;
	width: 10px;
	font: bold;
	vertical-align: middle;	
}

.tdMensaje {
	text-align: center;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	color:#<%=src.get("color.labelText")%>;
	padding-left: 5px;
	padding-right: 5px;
	padding-top: 5px;
	padding-bottom: 5px;
	width: 100%;
}

.tableCabecera {
	color: #<%=src.get("color.tableTitle.font")%>;
	padding-left: 0px;
	vertical-align: left;
	padding-top: 0px;
	text-align: left;
	
}

.posicionPestanas {

	position:relative; width:100%; height:32px; z-index:2; top: 0px; left: 0px;
}

.frameGeneral {
	position:relative; width:100%; height:100px; z-index:2; top:-3px; left: 0px;

	/*position:relative; 
	width:100%; 
	height:100px; 
	z-index:2; 
	top:-3px; 
	left: 0px;
	*/
}

.framePestanas {
	position:relative; width:100%; height:100px; z-index:2; top:-3px; left: 0px;
	margin-top:0px;
}


.botonesDetalleSinPosicionFija {
	background-color : #<%=src.get("color.tableTitle.BG")%>;
	width:100%; 
	height:26px; 
	z-index:2; 
	bottom: 0px; 
	left:0px;
	border: 0px solid #<%=src.get("color.labelText")%>;	
	margin:0px;
	padding:0px;
}

.botonesDetalle {
	background-color : #<%=src.get("color.tableTitle.BG")%>;
	position:absolute; 
	width:100%; 
	height:26px; 
	z-index:2; 
	bottom: 0px; 
	left:0px;
	border: 0px solid #<%=src.get("color.labelText")%>;	
	margin:0px;
	padding:0px;
}

.botonesSeguido {
	background-color : #<%=src.get("color.tableTitle.BG")%>;
	position:relative;
	width:100%; 
	height:26px; 
	z-index:2; 
	border: 0px solid #<%=src.get("color.labelText")%>;	
	margin:0px;
	padding:0px;
}

.posicionPrincipal {
	<%--position:absolute; width:1000px; height:600; z-index:3; top: 102px; left: 0px; --%>
	position:absolute; width:1000px; height: 600px; top: 102px;  
	border-bottom: 0px solid #<%=src.get("color.labeltable")%>;
	border-left: 0px solid #<%=src.get("color.labeltable")%>;
	border-right: 0px solid #<%=src.get("color.labeltable")%>;
	border-top: 0px solid #<%=src.get("color.labeltable")%>;
	margin :0px;
	margin-top: 3px;	
}

.posicionTitulo { 
	position:absolute; 
	left:0px; 
	width:1000px; 
	top:80px;
}	

.posicionBusquedaSolo {
	background-color: #<%=src.get("color.background")%>;
	margin: 0px;
	padding: 0px;
	width: 100%;
	height: 100%;
}
.posicionBusquedaSoloImp {
	background-color: #<%=src.get("color.background")%>;
	left: 0px; 
	width: 670px; 
	top: 0px; 
	
}
.posicionFramePrincipal {
	position:absolute; 
	width:100%;
	height:87%; 
	z-index:3; 
	top: 80px; 
	left: 0px;
}
	
.pest {	
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #000000;
	text-decoration:none;
	height: 18px;
	.padding: 4px 0px;	
	<!--font-family: Arial, Helvetica, sans-serif;-->
}

.pestanaTD {
white-space: nowrap;

}

.pestanaTD a {
	font-family: <%=src.get("font.style")%>;
	background: url(<%=app%>/html/imagenes/<%=src.get("color.fondo.pesatana")%>) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 10px 4px 4px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src.get("color.background")%>;	
}

.pestanaTD a:hover {
	background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 10px 4px 4px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src.get("color.background")%>;
}

.pestanaTD a.here {
	background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 10px 4px 4px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src.get("color.background")%>;
}
	
.listaNonEdit {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left;
	padding-top: 0px;
	padding-bottom: 0px;
	background-color : #<%=src.get("color.nonEdit")%>;
	color: #<%=src.get("color.nonEdit.selected.font")%>;
}

.posicionPest {
	position:absolute; left:0px; width=100%; top:10px; z-index: 5; 
}

.listaNonEditSelected {
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #<%=src.get("color.nonEdit.selected.font")%>;
	margin: auto;
	padding-right: 4px;
	padding-left: 3px;
	vertical-align: middle;
	text-align: left; 
	padding-top: 0px;
	background-color: #<%=src.get("color.nonEdit.selected.BG")%>;
	padding-bottom: 0px;
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
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.posicionModalMedia {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.posicionModalPeque {
	background-color: #<%=src.get("color.background")%>;
	width: 100%;
}

.detallePestanas {
	background-color: #<%=src.get("color.background")%>;
}

.paginator {
	font-size: <%=fontSize %>; 
	COLOR: #<%=src.get("color.tableTitle.font")%>; 
	background-color: #<%=src.get("color.tableTitle.BG")%>;
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	padding: 1px;
}

.tPaginator {
	font-size: <%=fontSize %>; 
	COLOR: #<%=src.get("color.tableTitle.font")%>; 
	background-color: #<%=src.get("color.tableTitle.BG")%>;
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	padding: 0px;
	height: 20px;
	width: 100%;
}

.boxConsultaPaginador { 
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: bold;
	margin-top: 0px;
	margin-left: 0px;
	vertical-align: top;
	text-align: left;
	color:#<%=src.get("color.labelText")%>;
	background-color: #<%=src.get("color.tableTitle.BG")%>;
	border: 0px solid #<%=src.get("color.button.border")%>;
	padding-top: 0px;
	padding-bottom: 0px;
}

a.paginator:link {
	CURSOR: pointer; 
	COLOR: #<%=src.get("color.titulos")%>; 
	FONT-STYLE: normal; 
	TEXT-DECORATION: none;
}
a.paginator:visited {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none;
}
a.paginator:hover {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none;
}
a.paginator:active {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none;
}

.tooltipo {
	background-color: #<%=src.get("color.tableTitle.BG")%>;
	border: solid 1px #999999;
	vertical-align: middle;
	padding-right: 5px;
	padding-left: 5px;
	padding-top: 1px;
	padding-bottom: 4px;
	
}
.tooltipoletra {
	font-size: 12px;
	color: #<%=src.get("color.tableTitle.font")%>;
}

#iconoboton {
	margin-bottom:-3px;
}


div#panel {
	position: relative;
	width:500px;
	height: 500px;
	left: 5px;
}


ul#tabs {
	position: absolute;
	left: 0px;
	top: 0px;
	margin: 0px;
	padding: 0px;
	width: 400px;
	height: 24px;
	z-index: 20;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #000000;
	text-decoration: none;
}

ul#tabs li {
		float: left;
		width: 80px;
		height: 23px;
		padding-left: 8px;
		list-style: none;
		margin-right: 1px;
		background-color: #<%=src.get("color.labelTable")%>;
		font-family: <%=src.get("font.style")%>;
		font-size: <%=fontSize %>;
		font-weight: normal;
		color: #FFFFFF;
}

ul#tabs li.actual {
		height: 24px;
		width: 80px;
		background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
		background-color: #<%=src.get("color.labelTable")%>;
		font-family: <%=src.get("font.style")%>;
		font-size: <%=fontSize %>;
		font-weight: normal;
		color: #FFFFFF;
		text-decoration: none;
		border-left-width: 2px;
		border-left-style: solid;
		border-left-color: #<%=src.get("color.background")%>;			
}

ul#tabs li a {	
	display: block;
	/* hack para ie6 */
	.display: inline-block;
	/* fin del hack */
	height: 23px;
	line-height: 23px;
	padding-right: 8px;
	outline: 0px none;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	text-decoration: none;
	color: #FFFFFF;
	background: url(tabs.png) right 0px;
}
		
ul#tabs li.actual a {		
	height: 24px;
	line-height: 24px;
	background: url(tabs.png) right -24px;
	cursor: default;
	color: #FFFFFF;	
}


div#panel #paneles {
	position:absolute;
	left: 1px;
	top: 23px;
	width: 950px;
	height: 350px;
	border: 1px solid #91a7b4;
	background: #<%=src.get("color.background")%>;
	overflow: hidden;
	z-index: 10px;
}

div#panel #paneles div {
	margin: 10px;
	width: 935px;
	height: 345px;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	text-decoration: none;
	color: #000;
	overflow: auto;
}

.sigaPopupTable {
	margin-top: 10px;
    padding-bottom: 10px;
    padding-left: 10px;
    padding-right: 10px;
    width: 100%;
    border: 1px;
}

.sigaPopupImageContainer {
    text-align: center;
    width: 30%;
}

.sigaPopupImage {
	width: 50px;
	height: 50px;
}

.sigaPopupTextContainer {
	text-align: left;
	vertical-align: middle;
}

.sigaPopupInputContainer {
	text-align: center;
}

.sigaPopupInput {
}

#barraNavegacion {
	width: 95%;
}

/*
 * jQuery UI CSS Framework 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 */

/* Layout helpers
----------------------------------*/
.ui-helper-hidden { display: none; }
.ui-helper-hidden-accessible { position: absolute !important; clip: rect(1px 1px 1px 1px); clip: rect(1px,1px,1px,1px); }
.ui-helper-reset { margin: 0; padding: 0; border: 0; outline: 0; line-height: 1.3; text-decoration: none; font-size: 100%; list-style: none; }
.ui-helper-clearfix:before, .ui-helper-clearfix:after { content: ""; display: table; }
.ui-helper-clearfix:after { clear: both; }
.ui-helper-clearfix { zoom: 1; }
.ui-helper-zfix { width: 100%; height: 100%; top: 0; left: 0; position: absolute; opacity: 0; filter:Alpha(Opacity=0); }


/* Interaction Cues
----------------------------------*/
.ui-state-disabled { cursor: default !important; }


/* Icons
----------------------------------*/

/* states and images */
.ui-icon { display: block; text-indent: -99999px; overflow: hidden; background-repeat: no-repeat; }


/* Misc visuals
----------------------------------*/

/* Overlays */
.ui-widget-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }


/*
 * jQuery UI CSS Framework 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 *
 * To view and modify this theme, visit http://jqueryui.com/themeroller/?ctl=themeroller
 */


/* Component containers
----------------------------------*/
.ui-widget { font-family: <%=src.get("font.style")%>; font-size: 1.1em; }
.ui-widget .ui-widget { font-size: 1em; }
.ui-widget input, .ui-widget select, .ui-widget textarea, .ui-widget button { font-family: <%=src.get("font.style")%>; font-size: 1em; }
.ui-widget-content { border: 1px solid #aaaaaa; background: #ffffff; color: #222222; }
.ui-widget-content a { color: #222222; }
.ui-widget-header { border: 1px solid #aaaaaa; background:#cccccc; color: #222222; font-weight: bold; }
.ui-widget-header a { color: #222222; }

/* Interaction states
----------------------------------*/
.ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default { border: 1px solid #d3d3d3; background: #ffffff; font-weight: normal; color: #000000; }
.ui-state-default a, .ui-state-default a:link, .ui-state-default a:visited { color: #555555; text-decoration: none; }
.ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus { border: 1px solid #999999; background: #ffffff; font-weight: normal; color: #212121; }
.ui-state-hover a, .ui-state-hover a:hover { color: #212121; text-decoration: none; }
.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active { border: 1px solid #aaaaaa; background: #000000 ; font-weight: normal; color: #ffffff; }
.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited { color: #212121; text-decoration: none; }
.ui-widget :active { outline: none; }

/* Interaction Cues
----------------------------------*/
.ui-state-highlight, .ui-widget-content .ui-state-highlight, .ui-widget-header .ui-state-highlight  {border: 1px solid #fcefa1; background: #fbf9ee url(<%=app%>/html/imagens/ui-bg_glass_55_fbf9ee_1x400.png) 50% 50% repeat-x; color: #363636; }
.ui-state-highlight a, .ui-widget-content .ui-state-highlight a,.ui-widget-header .ui-state-highlight a { color: #363636; }
.ui-state-error, .ui-widget-content .ui-state-error, .ui-widget-header .ui-state-error {border: 1px solid #cd0a0a; background: #fef1ec url(<%=app%>/html/imagens/ui-bg_inset-soft_95_fef1ec_1x100.png) 50% bottom repeat-x; color: #cd0a0a; }
.ui-state-error a, .ui-widget-content .ui-state-error a, .ui-widget-header .ui-state-error a { color: #cd0a0a; }
.ui-state-error-text, .ui-widget-content .ui-state-error-text, .ui-widget-header .ui-state-error-text { color: #cd0a0a; }
.ui-priority-primary, .ui-widget-content .ui-priority-primary, .ui-widget-header .ui-priority-primary { font-weight: bold; }
.ui-priority-secondary, .ui-widget-content .ui-priority-secondary,  .ui-widget-header .ui-priority-secondary { opacity: .7; filter:Alpha(Opacity=70); font-weight: normal; }
.ui-state-disabled, .ui-widget-content .ui-state-disabled, .ui-widget-header .ui-state-disabled { opacity: .35; filter:Alpha(Opacity=35); background-image: none; }

/* Icons
----------------------------------*/

/* states and images */
.ui-icon { width: 16px; height: 16px; background-image: url(<%=app%>/html/imagenes/ui-icons_222222_256x240.png); }
.ui-widget-content .ui-icon {background-image: url(<%=app%>/html/imagenes/ui-icons_222222_256x240.png); }
.ui-widget-header .ui-icon {background-image: url(<%=app%>/html/imagenes/ui-icons_222222_256x240.png); }
.ui-state-default .ui-icon { background-image: url(<%=app%>/html/imagenes/ui-icons_888888_256x240.png); }
.ui-state-hover .ui-icon, .ui-state-focus .ui-icon {background-image: url(<%=app%>/html/imagens/ui-icons_454545_256x240.png); }
.ui-state-active .ui-icon {background-image: url(<%=app%>/html/imagens/ui-icons_454545_256x240.png); }
.ui-state-highlight .ui-icon {background-image: url(<%=app%>/html/imagens/ui-icons_2e83ff_256x240.png); }
.ui-state-error .ui-icon, .ui-state-error-text .ui-icon {background-image: url(<%=app%>/html/imagens/ui-icons_cd0a0a_256x240.png); }

/* positioning */
.ui-icon-carat-1-n { background-position: 0 0; }
.ui-icon-carat-1-ne { background-position: -16px 0; }
.ui-icon-carat-1-e { background-position: -32px 0; }
.ui-icon-carat-1-se { background-position: -48px 0; }
.ui-icon-carat-1-s { background-position: -64px 0; }
.ui-icon-carat-1-sw { background-position: -80px 0; }
.ui-icon-carat-1-w { background-position: -96px 0; }
.ui-icon-carat-1-nw { background-position: -112px 0; }
.ui-icon-carat-2-n-s { background-position: -128px 0; }
.ui-icon-carat-2-e-w { background-position: -144px 0; }
.ui-icon-triangle-1-n { background-position: 0 -16px; }
.ui-icon-triangle-1-ne { background-position: -16px -16px; }
.ui-icon-triangle-1-e { background-position: -32px -16px; }
.ui-icon-triangle-1-se { background-position: -48px -16px; }
.ui-icon-triangle-1-s { background-position: -64px -16px; }
.ui-icon-triangle-1-sw { background-position: -80px -16px; }
.ui-icon-triangle-1-w { background-position: -96px -16px; }
.ui-icon-triangle-1-nw { background-position: -112px -16px; }
.ui-icon-triangle-2-n-s { background-position: -128px -16px; }
.ui-icon-triangle-2-e-w { background-position: -144px -16px; }
.ui-icon-arrow-1-n { background-position: 0 -32px; }
.ui-icon-arrow-1-ne { background-position: -16px -32px; }
.ui-icon-arrow-1-e { background-position: -32px -32px; }
.ui-icon-arrow-1-se { background-position: -48px -32px; }
.ui-icon-arrow-1-s { background-position: -64px -32px; }
.ui-icon-arrow-1-sw { background-position: -80px -32px; }
.ui-icon-arrow-1-w { background-position: -96px -32px; }
.ui-icon-arrow-1-nw { background-position: -112px -32px; }
.ui-icon-arrow-2-n-s { background-position: -128px -32px; }
.ui-icon-arrow-2-ne-sw { background-position: -144px -32px; }
.ui-icon-arrow-2-e-w { background-position: -160px -32px; }
.ui-icon-arrow-2-se-nw { background-position: -176px -32px; }
.ui-icon-arrowstop-1-n { background-position: -192px -32px; }
.ui-icon-arrowstop-1-e { background-position: -208px -32px; }
.ui-icon-arrowstop-1-s { background-position: -224px -32px; }
.ui-icon-arrowstop-1-w { background-position: -240px -32px; }
.ui-icon-arrowthick-1-n { background-position: 0 -48px; }
.ui-icon-arrowthick-1-ne { background-position: -16px -48px; }
.ui-icon-arrowthick-1-e { background-position: -32px -48px; }
.ui-icon-arrowthick-1-se { background-position: -48px -48px; }
.ui-icon-arrowthick-1-s { background-position: -64px -48px; }
.ui-icon-arrowthick-1-sw { background-position: -80px -48px; }
.ui-icon-arrowthick-1-w { background-position: -96px -48px; }
.ui-icon-arrowthick-1-nw { background-position: -112px -48px; }
.ui-icon-arrowthick-2-n-s { background-position: -128px -48px; }
.ui-icon-arrowthick-2-ne-sw { background-position: -144px -48px; }
.ui-icon-arrowthick-2-e-w { background-position: -160px -48px; }
.ui-icon-arrowthick-2-se-nw { background-position: -176px -48px; }
.ui-icon-arrowthickstop-1-n { background-position: -192px -48px; }
.ui-icon-arrowthickstop-1-e { background-position: -208px -48px; }
.ui-icon-arrowthickstop-1-s { background-position: -224px -48px; }
.ui-icon-arrowthickstop-1-w { background-position: -240px -48px; }
.ui-icon-arrowreturnthick-1-w { background-position: 0 -64px; }
.ui-icon-arrowreturnthick-1-n { background-position: -16px -64px; }
.ui-icon-arrowreturnthick-1-e { background-position: -32px -64px; }
.ui-icon-arrowreturnthick-1-s { background-position: -48px -64px; }
.ui-icon-arrowreturn-1-w { background-position: -64px -64px; }
.ui-icon-arrowreturn-1-n { background-position: -80px -64px; }
.ui-icon-arrowreturn-1-e { background-position: -96px -64px; }
.ui-icon-arrowreturn-1-s { background-position: -112px -64px; }
.ui-icon-arrowrefresh-1-w { background-position: -128px -64px; }
.ui-icon-arrowrefresh-1-n { background-position: -144px -64px; }
.ui-icon-arrowrefresh-1-e { background-position: -160px -64px; }
.ui-icon-arrowrefresh-1-s { background-position: -176px -64px; }
.ui-icon-arrow-4 { background-position: 0 -80px; }
.ui-icon-arrow-4-diag { background-position: -16px -80px; }
.ui-icon-extlink { background-position: -32px -80px; }
.ui-icon-newwin { background-position: -48px -80px; }
.ui-icon-refresh { background-position: -64px -80px; }
.ui-icon-shuffle { background-position: -80px -80px; }
.ui-icon-transfer-e-w { background-position: -96px -80px; }
.ui-icon-transferthick-e-w { background-position: -112px -80px; }
.ui-icon-folder-collapsed { background-position: 0 -96px; }
.ui-icon-folder-open { background-position: -16px -96px; }
.ui-icon-document { background-position: -32px -96px; }
.ui-icon-document-b { background-position: -48px -96px; }
.ui-icon-note { background-position: -64px -96px; }
.ui-icon-mail-closed { background-position: -80px -96px; }
.ui-icon-mail-open { background-position: -96px -96px; }
.ui-icon-suitcase { background-position: -112px -96px; }
.ui-icon-comment { background-position: -128px -96px; }
.ui-icon-person { background-position: -144px -96px; }
.ui-icon-print { background-position: -160px -96px; }
.ui-icon-trash { background-position: -176px -96px; }
.ui-icon-locked { background-position: -192px -96px; }
.ui-icon-unlocked { background-position: -208px -96px; }
.ui-icon-bookmark { background-position: -224px -96px; }
.ui-icon-tag { background-position: -240px -96px; }
.ui-icon-home { background-position: 0 -112px; }
.ui-icon-flag { background-position: -16px -112px; }
.ui-icon-calendar { background-position: -32px -112px; }
.ui-icon-cart { background-position: -48px -112px; }
.ui-icon-pencil { background-position: -64px -112px; }
.ui-icon-clock { background-position: -80px -112px; }
.ui-icon-disk { background-position: -96px -112px; }
.ui-icon-calculator { background-position: -112px -112px; }
.ui-icon-zoomin { background-position: -128px -112px; }
.ui-icon-zoomout { background-position: -144px -112px; }
.ui-icon-search { background-position: -160px -112px; }
.ui-icon-wrench { background-position: -176px -112px; }
.ui-icon-gear { background-position: -192px -112px; }
.ui-icon-heart { background-position: -208px -112px; }
.ui-icon-star { background-position: -224px -112px; }
.ui-icon-link { background-position: -240px -112px; }
.ui-icon-cancel { background-position: 0 -128px; }
.ui-icon-plus { background-position: -16px -128px; }
.ui-icon-plusthick { background-position: -32px -128px; }
.ui-icon-minus { background-position: -48px -128px; }
.ui-icon-minusthick { background-position: -64px -128px; }
.ui-icon-close { background-position: -80px -128px; }
.ui-icon-closethick { background-position: -96px -128px; }
.ui-icon-key { background-position: -112px -128px; }
.ui-icon-lightbulb { background-position: -128px -128px; }
.ui-icon-scissors { background-position: -144px -128px; }
.ui-icon-clipboard { background-position: -160px -128px; }
.ui-icon-copy { background-position: -176px -128px; }
.ui-icon-contact { background-position: -192px -128px; }
.ui-icon-image { background-position: -208px -128px; }
.ui-icon-video { background-position: -224px -128px; }
.ui-icon-script { background-position: -240px -128px; }
.ui-icon-alert { background-position: 0 -144px; }
.ui-icon-info { background-position: -16px -144px; }
.ui-icon-notice { background-position: -32px -144px; }
.ui-icon-help { background-position: -48px -144px; }
.ui-icon-check { background-position: -64px -144px; }
.ui-icon-bullet { background-position: -80px -144px; }
.ui-icon-radio-off { background-position: -96px -144px; }
.ui-icon-radio-on { background-position: -112px -144px; }
.ui-icon-pin-w { background-position: -128px -144px; }
.ui-icon-pin-s { background-position: -144px -144px; }
.ui-icon-play { background-position: 0 -160px; }
.ui-icon-pause { background-position: -16px -160px; }
.ui-icon-seek-next { background-position: -32px -160px; }
.ui-icon-seek-prev { background-position: -48px -160px; }
.ui-icon-seek-end { background-position: -64px -160px; }
.ui-icon-seek-start { background-position: -80px -160px; }
/* ui-icon-seek-first is deprecated, use ui-icon-seek-start instead */
.ui-icon-seek-first { background-position: -80px -160px; }
.ui-icon-stop { background-position: -96px -160px; }
.ui-icon-eject { background-position: -112px -160px; }
.ui-icon-volume-off { background-position: -128px -160px; }
.ui-icon-volume-on { background-position: -144px -160px; }
.ui-icon-power { background-position: 0 -176px; }
.ui-icon-signal-diag { background-position: -16px -176px; }
.ui-icon-signal { background-position: -32px -176px; }
.ui-icon-battery-0 { background-position: -48px -176px; }
.ui-icon-battery-1 { background-position: -64px -176px; }
.ui-icon-battery-2 { background-position: -80px -176px; }
.ui-icon-battery-3 { background-position: -96px -176px; }
.ui-icon-circle-plus { background-position: 0 -192px; }
.ui-icon-circle-minus { background-position: -16px -192px; }
.ui-icon-circle-close { background-position: -32px -192px; }
.ui-icon-circle-triangle-e { background-position: -48px -192px; }
.ui-icon-circle-triangle-s { background-position: -64px -192px; }
.ui-icon-circle-triangle-w { background-position: -80px -192px; }
.ui-icon-circle-triangle-n { background-position: -96px -192px; }
.ui-icon-circle-arrow-e { background-position: -112px -192px; }
.ui-icon-circle-arrow-s { background-position: -128px -192px; }
.ui-icon-circle-arrow-w { background-position: -144px -192px; }
.ui-icon-circle-arrow-n { background-position: -160px -192px; }
.ui-icon-circle-zoomin { background-position: -176px -192px; }
.ui-icon-circle-zoomout { background-position: -192px -192px; }
.ui-icon-circle-check { background-position: -208px -192px; }
.ui-icon-circlesmall-plus { background-position: 0 -208px; }
.ui-icon-circlesmall-minus { background-position: -16px -208px; }
.ui-icon-circlesmall-close { background-position: -32px -208px; }
.ui-icon-squaresmall-plus { background-position: -48px -208px; }
.ui-icon-squaresmall-minus { background-position: -64px -208px; }
.ui-icon-squaresmall-close { background-position: -80px -208px; }
.ui-icon-grip-dotted-vertical { background-position: 0 -224px; }
.ui-icon-grip-dotted-horizontal { background-position: -16px -224px; }
.ui-icon-grip-solid-vertical { background-position: -32px -224px; }
.ui-icon-grip-solid-horizontal { background-position: -48px -224px; }
.ui-icon-gripsmall-diagonal-se { background-position: -64px -224px; }
.ui-icon-grip-diagonal-se { background-position: -80px -224px; }


/* Misc visuals
----------------------------------*/

/* Corner radius */
.ui-corner-all, .ui-corner-top, .ui-corner-left, .ui-corner-tl { -moz-border-radius-topleft: 4px; -webkit-border-top-left-radius: 4px; -khtml-border-top-left-radius: 4px; border-top-left-radius: 4px;}
.ui-corner-all, .ui-corner-top, .ui-corner-right, .ui-corner-tr { -moz-border-radius-topright: 4px; -webkit-border-top-right-radius: 4px; -khtml-border-top-right-radius: 4px; border-top-right-radius: 4px; }
.ui-corner-all, .ui-corner-bottom, .ui-corner-left, .ui-corner-bl { -moz-border-radius-bottomleft: 4px; -webkit-border-bottom-left-radius: 4px; -khtml-border-bottom-left-radius: 4px; border-bottom-left-radius: 4px; }
.ui-corner-all, .ui-corner-bottom, .ui-corner-right, .ui-corner-br { -moz-border-radius-bottomright: 4px; -webkit-border-bottom-right-radius: 4px; -khtml-border-bottom-right-radius: 4px; border-bottom-right-radius: 4px; }

/* Overlays */
.ui-widget-overlay { background: #aaaaaa url(<%=app%>/html/imagens/ui-bg_flat_0_aaaaaa_40x100.png) 50% 50% repeat-x; opacity: .30;filter:Alpha(Opacity=30); }
.ui-widget-shadow { margin: -8px 0 0 -8px; padding: 8px; background: #aaaaaa url(<%=app%>/html/imagens/ui-bg_flat_0_aaaaaa_40x100.png) 50% 50% repeat-x; opacity: .30;filter:Alpha(Opacity=30); -moz-border-radius: 8px; -khtml-border-radius: 8px; -webkit-border-radius: 8px; border-radius: 8px; }/*
 * jQuery UI Resizable 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Resizable#theming
 */
.ui-resizable { position: relative;}
.ui-resizable-handle { position: absolute;font-size: 0.1px;z-index: 99999; display: block; }
.ui-resizable-disabled .ui-resizable-handle, .ui-resizable-autohide .ui-resizable-handle { display: none; }
.ui-resizable-n { cursor: n-resize; height: 7px; width: 100%; top: -5px; left: 0; }
.ui-resizable-s { cursor: s-resize; height: 7px; width: 100%; bottom: -5px; left: 0; }
.ui-resizable-e { cursor: e-resize; width: 7px; right: -5px; top: 0; height: 100%; }
.ui-resizable-w { cursor: w-resize; width: 7px; left: -5px; top: 0; height: 100%; }
.ui-resizable-se { cursor: se-resize; width: 12px; height: 12px; right: 1px; bottom: 1px; }
.ui-resizable-sw { cursor: sw-resize; width: 9px; height: 9px; left: -5px; bottom: -5px; }
.ui-resizable-nw { cursor: nw-resize; width: 9px; height: 9px; left: -5px; top: -5px; }
.ui-resizable-ne { cursor: ne-resize; width: 9px; height: 9px; right: -5px; top: -5px;}/*
 * jQuery UI Selectable 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Selectable#theming
 */
.ui-selectable-helper { position: absolute; z-index: 100; border:1px dotted black; }
/*
 * jQuery UI Accordion 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Accordion#theming
 */
/* IE/Win - Fix animation bug - #4615 */
.ui-accordion { width: 100%; }
.ui-accordion .ui-accordion-header { cursor: pointer; position: relative; margin-top: 1px; zoom: 1; }
.ui-accordion .ui-accordion-li-fix { display: inline; }
.ui-accordion .ui-accordion-header-active { border-bottom: 0 !important; }
.ui-accordion .ui-accordion-header a { display: block; font-size: 1em; padding: .5em .5em .5em .7em; }
.ui-accordion-icons .ui-accordion-header a { padding-left: 2.2em; }
.ui-accordion .ui-accordion-header .ui-icon { position: absolute; left: .5em; top: 50%; margin-top: -8px; }
.ui-accordion .ui-accordion-content { padding: 1em 2.2em; border-top: 0; margin-top: -2px; position: relative; top: 1px; margin-bottom: 2px; overflow: auto; display: none; zoom: 1; }
.ui-accordion .ui-accordion-content-active { display: block; }
/*
 * jQuery UI Autocomplete 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Autocomplete#theming
 */
.ui-autocomplete { position: absolute; cursor: default; }	

/* workarounds */
* html .ui-autocomplete { width:1px; } /* without this, the menu expands to 100% in IE6 */

/*
 * jQuery UI Menu 1.8.18
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Menu#theming
 */
.ui-menu {
	list-style:none;
	padding: 2px;
	margin: 0;
	display:block;
	float: left;
}
.ui-menu .ui-menu {
	margin-top: -3px;
}
.ui-menu .ui-menu-item {
	margin:0;
	padding: 0;
	zoom: 1;
	float: left;
	clear: left;
	width: 100%;
}
.ui-menu .ui-menu-item a {
	text-decoration:none;
	display:block;
	padding:.2em .4em;
	line-height:1.5;
	zoom:1;
}
.ui-menu .ui-menu-item a.ui-state-hover,
.ui-menu .ui-menu-item a.ui-state-active {
	font-weight: normal;
	margin: -1px;
}
/*
 * jQuery UI Button 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Button#theming
 */
.ui-button { display: inline-block; position: relative; padding: 0; margin-right: .1em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: hidden; *overflow: visible; } /* the overflow property removes extra width in IE */
.ui-button-icon-only { width: 2.2em; } /* to make room for the icon, a width needs to be set here */
button.ui-button-icon-only { width: 2.4em; } /* button elements seem to need a little more width */
.ui-button-icons-only { width: 3.4em; } 
button.ui-button-icons-only { width: 3.7em; } 

/*button text element */
.ui-button .ui-button-text { display: block; line-height: 1.4;  }
.ui-button-text-only .ui-button-text { padding: .4em 1em; }
.ui-button-icon-only .ui-button-text, .ui-button-icons-only .ui-button-text { padding: .4em; text-indent: -9999999px; }
.ui-button-text-icon-primary .ui-button-text, .ui-button-text-icons .ui-button-text { padding: .4em 1em .4em 2.1em; }
.ui-button-text-icon-secondary .ui-button-text, .ui-button-text-icons .ui-button-text { padding: .4em 2.1em .4em 1em; }
.ui-button-text-icons .ui-button-text { padding-left: 2.1em; padding-right: 2.1em; }
/* no icon support for input elements, provide padding by default */
input.ui-button { padding: .4em 1em; }

/*button icon element(s) */
.ui-button-icon-only .ui-icon, .ui-button-text-icon-primary .ui-icon, .ui-button-text-icon-secondary .ui-icon, .ui-button-text-icons .ui-icon, .ui-button-icons-only .ui-icon { position: absolute; top: 50%; margin-top: -8px; }
.ui-button-icon-only .ui-icon { left: 50%; margin-left: -8px; }
.ui-button-text-icon-primary .ui-button-icon-primary, .ui-button-text-icons .ui-button-icon-primary, .ui-button-icons-only .ui-button-icon-primary { left: .5em; }
.ui-button-text-icon-secondary .ui-button-icon-secondary, .ui-button-text-icons .ui-button-icon-secondary, .ui-button-icons-only .ui-button-icon-secondary { right: .5em; }
.ui-button-text-icons .ui-button-icon-secondary, .ui-button-icons-only .ui-button-icon-secondary { right: .5em; }

/*button sets*/
.ui-buttonset { margin-right: 7px; }
.ui-buttonset .ui-button { margin-left: 0; margin-right: -.3em; }

/* workarounds */
button.ui-button::-moz-focus-inner { border: 0; padding: 0; } /* reset extra padding in Firefox */
/*
 * jQuery UI Dialog 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Dialog#theming
 */
.ui-dialog { position: absolute; padding: .2em; width: 300px; overflow: hidden; }
.ui-dialog .ui-dialog-titlebar { padding: .4em 1em; position: relative; background-color : #<%=src.get("color.background")%>;  }
.ui-dialog .ui-dialog-title { float: left; margin: .1em 16px .1em 0; } 
.ui-dialog .ui-dialog-titlebar-close { position: absolute; right: .3em; top: 50%; width: 19px; margin: -10px 0 0 0; padding: 1px; height: 18px; }
.ui-dialog .ui-dialog-titlebar-close span { display: block; margin: 1px; }
.ui-dialog .ui-dialog-titlebar-close:hover, .ui-dialog .ui-dialog-titlebar-close:focus { padding: 0; }
.ui-dialog .ui-dialog-content { position: relative; border: 0; padding: .5em 1em; background: none; overflow: hidden; zoom: 1; }
.ui-dialog .ui-dialog-buttonpane { text-align: left; border-width: 1px 0 0 0; background-image: none; margin: .5em 0 0 0; padding: .3em 1em .5em .4em; }
.ui-dialog .ui-dialog-buttonpane .ui-dialog-buttonset { float: right; }
.ui-dialog .ui-dialog-buttonpane button { margin: .5em .4em .5em 0; cursor: pointer; }
.ui-dialog .ui-resizable-se { width: 14px; height: 14px; right: 3px; bottom: 3px; }
.ui-draggable .ui-dialog-titlebar { cursor: move; }
/*
 * jQuery UI Slider 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Slider#theming
 */
.ui-slider { position: relative; text-align: left; }
.ui-slider .ui-slider-handle { position: absolute; z-index: 2; width: 1.2em; height: 1.2em; cursor: default; }
.ui-slider .ui-slider-range { position: absolute; z-index: 1; font-size: .7em; display: block; border: 0; background-position: 0 0; }

.ui-slider-horizontal { height: .8em; }
.ui-slider-horizontal .ui-slider-handle { top: -.3em; margin-left: -.6em; }
.ui-slider-horizontal .ui-slider-range { top: 0; height: 100%; }
.ui-slider-horizontal .ui-slider-range-min { left: 0; }
.ui-slider-horizontal .ui-slider-range-max { right: 0; }

.ui-slider-vertical { width: .8em; height: 100px; }
.ui-slider-vertical .ui-slider-handle { left: -.3em; margin-left: 0; margin-bottom: -.6em; }
.ui-slider-vertical .ui-slider-range { left: 0; width: 100%; }
.ui-slider-vertical .ui-slider-range-min { bottom: 0; }
.ui-slider-vertical .ui-slider-range-max { top: 0; }/*
 * jQuery UI Tabs 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Tabs#theming
 */
.ui-tabs { position: relative; padding: .2em; zoom: 1; } /* position: relative prevents IE scroll bug (element with position: relative inside container with overflow: auto appear as "fixed") */
.ui-tabs .ui-tabs-nav { margin: 0; padding: .2em .2em 0; }
.ui-tabs .ui-tabs-nav li { list-style: none; float: left; position: relative; top: 1px; margin: 0 .2em 1px 0; border-bottom: 0 !important; padding: 0; white-space: nowrap; }
.ui-tabs .ui-tabs-nav li a { float: left; padding: .5em 1em; text-decoration: none; }
.ui-tabs .ui-tabs-nav li.ui-tabs-selected { margin-bottom: 0; padding-bottom: 1px; }
.ui-tabs .ui-tabs-nav li.ui-tabs-selected a, .ui-tabs .ui-tabs-nav li.ui-state-disabled a, .ui-tabs .ui-tabs-nav li.ui-state-processing a { cursor: text; }
.ui-tabs .ui-tabs-nav li a, .ui-tabs.ui-tabs-collapsible .ui-tabs-nav li.ui-tabs-selected a { cursor: pointer; } /* first selector in group seems obsolete, but required to overcome bug in Opera applying cursor: text overall if defined elsewhere... */
.ui-tabs .ui-tabs-panel { display: block; border-width: 0; padding: 1em 1.4em; background: none; }
.ui-tabs .ui-tabs-hide { display: none !important; }
/*
 * jQuery UI Datepicker 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Datepicker#theming
 .ui-datepicker { width: 17em; padding: .2em .2em 0; display: none; }
 */
 #ui-datepicker-div
    {
    	position: absolute; 
        z-index: 9999999;
    }

.ui-datepicker {overflow-x:visible; width: 17em; padding: .2em .2em 0; position: absolute;  z-index:9000 !important; font-family: <%=src.get("font.style")%>;} 
.ui-datepicker .ui-datepicker-header { position:relative; padding:.2em 0; background: #<%=src.get("color.background")%>;}
.ui-datepicker .ui-datepicker-prev, .ui-datepicker .ui-datepicker-next { position:absolute; top: 2px; width: 1.8em; height: 1.8em; }
.ui-datepicker .ui-datepicker-prev-hover, .ui-datepicker .ui-datepicker-next-hover { top: 1px; }
.ui-datepicker .ui-datepicker-prev { left:2px; }
.ui-datepicker .ui-datepicker-next { right:2px; }
.ui-datepicker .ui-datepicker-prev-hover { left:1px; }
.ui-datepicker .ui-datepicker-next-hover { right:1px; }
.ui-datepicker .ui-datepicker-prev span, .ui-datepicker .ui-datepicker-next span { display: block; position: absolute; left: 50%; margin-left: -8px; top: 50%; margin-top: -8px;  }
.ui-datepicker .ui-datepicker-title { margin: 0 2.3em; line-height: 1.8em; text-align: center; }
.ui-datepicker .ui-datepicker-title select { font-size:1em; margin:1px 0; }
.ui-datepicker select.ui-datepicker-month-year {width: 100%;}
.ui-datepicker select.ui-datepicker-month, 
.ui-datepicker select.ui-datepicker-year { width: 49%;}
.ui-datepicker table {width: 100%; font-size: .9em; border-collapse: collapse; margin:0 0 .4em; }
.ui-datepicker th { padding: .7em .3em; text-align: center; font-weight: bold; border: 0;  }
.ui-datepicker td { border: 0; padding: 1px; }
.ui-datepicker td span, .ui-datepicker td a { display: block; padding: .2em; text-align: right; text-decoration: none; }
.ui-datepicker .ui-datepicker-buttonpane { background-image: none; margin: .7em 0 0 0; padding:0 .2em; border-left: 0; border-right: 0; border-bottom: 0; }
.ui-datepicker .ui-datepicker-buttonpane button { float: right; margin: .5em .2em .4em; cursor: pointer; padding: .2em .6em .3em .6em; width:auto; overflow:visible; }
.ui-datepicker .ui-datepicker-buttonpane button.ui-datepicker-current { float:left; }

/* with multiple calendars */
.ui-datepicker.ui-datepicker-multi { width:auto; }
.ui-datepicker-multi .ui-datepicker-group { float:left; }
.ui-datepicker-multi .ui-datepicker-group table { width:95%; margin:0 auto .4em; }
.ui-datepicker-multi-2 .ui-datepicker-group { width:50%; }
.ui-datepicker-multi-3 .ui-datepicker-group { width:33.3%; }
.ui-datepicker-multi-4 .ui-datepicker-group { width:25%; }
.ui-datepicker-multi .ui-datepicker-group-last .ui-datepicker-header { border-left-width:0; }
.ui-datepicker-multi .ui-datepicker-group-middle .ui-datepicker-header { border-left-width:0; }
.ui-datepicker-multi .ui-datepicker-buttonpane { clear:left; }
.ui-datepicker-row-break { clear:both; width:100%; font-size:0em; }

/* RTL support */
.ui-datepicker-rtl { direction: rtl; }
.ui-datepicker-rtl .ui-datepicker-prev { right: 2px; left: auto; }
.ui-datepicker-rtl .ui-datepicker-next { left: 2px; right: auto; }
.ui-datepicker-rtl .ui-datepicker-prev:hover { right: 1px; left: auto; }
.ui-datepicker-rtl .ui-datepicker-next:hover { left: 1px; right: auto; }
.ui-datepicker-rtl .ui-datepicker-buttonpane { clear:right; }
.ui-datepicker-rtl .ui-datepicker-buttonpane button { float: left; }
.ui-datepicker-rtl .ui-datepicker-buttonpane button.ui-datepicker-current { float:right; }
.ui-datepicker-rtl .ui-datepicker-group { float:right; }
.ui-datepicker-rtl .ui-datepicker-group-last .ui-datepicker-header { border-right-width:0; border-left-width:1px; }
.ui-datepicker-rtl .ui-datepicker-group-middle .ui-datepicker-header { border-right-width:0; border-left-width:1px; }

/* IE6 IFRAME FIX (taken from datepicker 1.5.3 */
.ui-datepicker-cover {
    display: none; /*sorry for IE5*/
    display/**/: block; /*sorry for IE5*/
    position: absolute; /*must have*/
    z-index: -1; /*must have*/
    filter: mask(); /*must have*/
    top: -4px; /*must have*/
    left: -4px; /*must have*/
    width: 200px; /*must have*/
    height: 200px; /*must have*/
}


//Estilos para alerts
/*
 * jQuery UI Progressbar 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Progressbar#theming
 */
.ui-progressbar { height:2em; text-align: left; overflow: hidden; }
.ui-progressbar .ui-progressbar-value {margin: -1px; height:100%; }

#popup_container {
	font-family: Arial, sans-serif;
	font-size: 12px;
	min-width: 300px; /* Dialog will be no smaller than this */
	max-width: 600px; /* Dialog will wrap after this width */
	background: #FFF;
	border: solid 5px #999;
	color: #000;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}

#popup_title {
	font-size: 14px;
	font-weight: bold;
	text-align: center;
	line-height: 1.75em;
	color: #666;
	background: #CCC url(images/title.gif) top repeat-x;
	border: solid 1px #FFF;
	border-bottom: solid 1px #999;
	cursor: default;
	padding: 0em;
	margin: 0em;
}

#popup_content {
	background: 16px 16px no-repeat url(images/info.gif);
	padding: 1em 1.75em;
	margin: 0em;
}

#popup_content.alert {
	background-image: url(images/info.gif);
}

#popup_content.confirm {
	background-image: url(images/important.gif);
}

#popup_content.prompt {
	background-image: url(images/help.gif);
}

#popup_message {
	padding-left: 48px;
}

#popup_panel {
	text-align: center;
	margin: 1em 0em 0em 1em;
}

#popup_prompt {
	margin: .5em 0em;
}


.notice-wrap {
	top: 50px;
	width: 400px;
	float:left;
	clear:both;
	z-index: 9999;
	position: relative;
	left: 50%;
}

* html .notice-wrap {
	position: absolute;		
}
 
.notice-item {
	background: #F9EDBE;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
	color: #000;
	padding: 16px 16px 16px 16px;
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize%>;
	font-weight: bold;
	border: 2px solid #999;
	display: block;
	position: relative;
	margin: 0 0 12px 0;
	left:-175px;
}

.notice-item-close {
	position: absolute;
	font-family: Arial;
	font-size: 12px;
	font-weight: bold;
	right: 6px;
	top: 6px;
	cursor: pointer;
}
