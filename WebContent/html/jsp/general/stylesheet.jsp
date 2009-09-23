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
	margin:0px;
	padding:0px;
}

iframe {
	margin-top:3px;
}

h1 {
	font-family: <%=src.get("font.style")%>;
	color:#<%=src.get("color.labelText")%>;
}

td {
	vertical-align: top;
}

textarea {
	width:300px;
}

form {
	margin:0px;
	padding:0px;
	height:0px;
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
	height: 25px;
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
	width:30px;
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
	width:30px;
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
	color:#<%=src.get("color.labelText")%>;
//	background-color:  #<%=src.get("color.button.BG")%>;
	vertical-align: top;
	margin-top: -3px;
	margin-left: 0px;
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
	width: 500px;
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
	border: thin solid #<%=src.get("color.button.border")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	cursor: hand;
	clear: right;
	margin:0px;
	padding:0px;
	height:22px;
}
.buttonEnTabla {
	font-family: <%=src.get("font.style")%>;
	color: #<%=src.get("color.button.font")%>;
	background-color: #<%=src.get("color.button.BG")%>;
	border: thin solid #<%=src.get("color.button.border")%>;
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
	vertical-align:middle;
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
	width:100%;
	font:bold;
	vertical-align:middle;
}

.titulitos {
	FONT-WEIGHT: normal; 
	font-size: <%=fontSize %>; 
	color:#<%=src.get("color.titleBar.font")%>;
	FONT-FAMILY: <%=src.get("font.style")%>;
	text-decoration: none;
	margin: 0px;
	height: 26px;
	padding: 2px;
	width:100%;
	font:bold;
	vertical-align:middle;
}

A.titulitos:link {
	CURSOR: hand; 
	COLOR: #<%=src.get("color.titulos")%>; 
	FONT-STYLE: normal; 
	TEXT-DECORATION: none
}
A.titulitos:active {
	COLOR: #<%=src.get("color.titulos")%>
	TEXT-DECORATION: none
}
A.titulitos:hover {
	COLOR: #<%=src.get("color.titulos")%>
	TEXT-DECORATION: none
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
.clLevel0,.clLevel0over,.clLevel0NoLinkOver{position:absolute; padding: 2px;font-family:<%=src.get("font.style")%>; font-size:<%=fontSize%>; font-weight:normal; text-align:center; 	border-bottom: 1px solid #FFFFFF;border-left: 1px solid #FFFFFF;border-right: 1px solid #FFFFFF;}
.clLevel0 {background-color : #<%=src.get("color.menu.level0.BG")%>;layer-background-color:#<%=src.get("color.menu.level0.BG")%>;color:#<%=src.get("color.menu.level0.font")%>;}
.clLevel0over{background-color:#<%=src.get("color.menu.level0.activo.BG")%>; layer-background-color:#<%=src.get("color.menu.level0.activo.BG")%>; color:#<%=src.get("color.menu.level0.activo.font")%>; cursor:pointer; cursor:hand; border-bottom: 1px solid #000000;border-left: 1px solid #000000;border-right: 1px solid #000000;}
.clLevel0NoLinkOver{background-color:#<%=src.get("color.menu.level0.activo.BG")%>; layer-background-color:#<%=src.get("color.menu.level0.activo.BG")%>; color:#<%=src.get("color.menu.level0.activo.font")%>; cursor:default; }
.clLevel0border{position:absolute; visibility:hidden; background-color:transparent; layer-background-color:transparent}

/*Styles for level 1*/
.clLevel1, .clLevel1over, .clLevel1NoLinkOver{position:absolute; padding:2px; font-family:<%=src.get("font.style")%>; font-size:<%=fontSize%>;font-weight:normal}
.clLevel1{background-color:#<%=src.get("color.menu.level1.BG")%>; layer-background-color:#<%=src.get("color.menu.level1.BG")%>; color:#<%=src.get("color.menu.level1.font")%> ;	border-bottom: 1px solid #FFFFFF;border-left: 1px solid #FFFFFF;;border-right: 1px solid #FFFFFF;}
.clLevel1over{background-color:#<%=src.get("color.menu.level1.activo.BG")%>; layer-background-color:#<%=src.get("color.menu.level1.activo.BG")%>; color:#<%=src.get("color.menu.level1.activo.font")%>; cursor:pointer; cursor:hand;border-bottom: 1px solid #000000;border-left: 1px solid #000000;border-right: 1px solid #000000; }
.clLevel1NoLinkOver{background-color:#<%=src.get("color.menu.level1.activo.BG")%>; layer-background-color:#<%=src.get("color.menu.level1.activo.BG")%>; color:#<%=src.get("color.menu.level1.activo.font")%>;}
.clLevel1border{position:absolute; visibility:hidden; layer-background-color:#006699}

/*Styles for level 2*/
.clLevel2, .clLevel2over{position:absolute; padding:2px; font-family:<%=src.get("font.style")%>; font-size:<%=fontSize%>;font-weight:normal}
.clLevel2{background-color:#<%=src.get("color.menu.level2.BG")%>; layer-background-color:#<%=src.get("color.menu.level2.BG")%>; color:#<%=src.get("color.menu.level2.font")%>;		border-bottom: 1px solid #FFFFFF;border-left: 1px solid #FFFFFF;border-right: 1px solid #FFFFFF;}
.clLevel2over{background-color:#<%=src.get("color.menu.level2.activo.BG")%>; layer-background-color:#<%=src.get("color.menu.level2.activo.BG")%>; color:#<%=src.get("color.menu.level2.activo.font")%>; cursor:pointer; cursor:hand; border-bottom: 1px solid #000000;border-left: 1px solid #000000;border-right: 1px solid #000000;}
.clLevel2border{position:absolute; visibility:hidden; }


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
	text-decoration:none
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
	text-decoration:none
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
	text-decoration:none
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
	text-decoration:none
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
	width: 670;
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
	margin:0px;
	padding:0px;	
	border: 1px solid #<%=src.get("color.labelText")%>;
	
}

.tdBotones {
	text-align: right;
	color:#<%=src.get("color.labelText")%>;
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
	position:absolute; width:1000px; height:600; z-index:3; top: 102px; left: 0px  ;
	border-bottom: 0px solid #<%=src.get("color.labeltable")%>;
	border-left: 0px solid #<%=src.get("color.labeltable")%>;
	border-right: 0px solid #<%=src.get("color.labeltable")%>;
	border-top: 0px solid #<%=src.get("color.labeltable")%>;
	margin:0px;
	margin-top:3px; 
	
}

.posicionTitulo { 
	position:absolute; left:0px; width=1000; top:80px; z-index: 5;
}
	

.posicionBusquedaSolo {
	background-color: #<%=src.get("color.background")%>;
	margin: 0px;
	padding: 0px;
	width:100%;
	height:600px;
}
.posicionBusquedaSoloImp {
	background-color: #<%=src.get("color.background")%>;
	left:0px; width=670px; top:0px; 
	
}
.posicionFramePrincipal {
	position:absolute; width:100%; height:87%; z-index:3; top: 80px; left: 0px;
}


	
.pest {
	<!--font-family: Arial, Helvetica, sans-serif;-->
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #000000;
	text-decoration:none;
	height: 18px;
	padding: 4px 0px;

	
}

.pest a{
	background: url(<%=app%>/html/imagenes/<%=src.get("color.fondo.pesatana")%>) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 5px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src.get("color.background")%>;
	
}
.pest a:hover {
	background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 5px;
	border-left-width: 2px;
	border-left-style: solid;
	border-left-color: #<%=src.get("color.background")%>;
}
.pest a.here {
	background: url(<%=app%>/html/imagenes/pestahover.gif) no-repeat right;
<!--	font-family: Arial, Helvetica, sans-serif; -->
	font-family: <%=src.get("font.style")%>;
	font-size: <%=fontSize %>;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration:none;
	padding: 4px 5px;
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
	height=20px;
	width="100%";

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
	CURSOR: hand; 
	COLOR: #<%=src.get("color.titulos")%>; 
	FONT-STYLE: normal; 
	TEXT-DECORATION: none
}
a.paginator:visited {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none
}
a.paginator:hover {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none
}
a.paginator:active {
	COLOR: #<%=src.get("color.titulos")%>;
	TEXT-DECORATION: none
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
	font-size: 12;
	color: #<%=src.get("color.tableTitle.font")%>;
}

#iconoboton {
	margin-bottom:-3px;
}