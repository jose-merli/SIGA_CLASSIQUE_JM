<!-- menu.jsp -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Menu"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsLogging"%>
<%
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
    String menuPos=(String)request.getSession().getAttribute(SIGAConstants.MENU_POSITION_REF);
	String[] profile=usr.getProfile();
	String institucion=usr.getLocation();
%>
<script>
//Menu object creation
oCMenu=new makeCM("oCMenu") //Making the menu object. Argument: menuname

oCMenu.frames=0

//Menu properties
oCMenu.pxBetween=0
oCMenu.fromLeft=174
oCMenu.fromTop=0
oCMenu.rows=1
oCMenu.menuPlacement="0"

oCMenu.onlineRoot="<%=app%>"
oCMenu.resizeCheck=1
oCMenu.wait=0
oCMenu.fillImg="cm_fill.gif"
oCMenu.zIndex=0

//Background bar properties
oCMenu.useBar=1
oCMenu.barWidth="menu"
oCMenu.barHeight="menu"
oCMenu.barClass="clBar"
oCMenu.barX="menu"
oCMenu.barY="menu"
oCMenu.barBorderX=0
oCMenu.barBorderY=0
oCMenu.barBorderClass=""

//Level properties - ALL properties have to be specified in level 0
oCMenu.level[0]=new cm_makeLevel() //Add this for each new level
oCMenu.level[0].width=85
oCMenu.level[0].height=38
oCMenu.level[0].regClass="clLevel0"
oCMenu.level[0].overClass="clLevel0over"
oCMenu.level[0].borderX=1
oCMenu.level[0].borderY=0
oCMenu.level[0].borderClass="clLevel0border"
oCMenu.level[0].offsetX=0
oCMenu.level[0].offsetY=0
oCMenu.level[0].rows=0
oCMenu.level[0].arrow=0
oCMenu.level[0].arrowWidth=0
oCMenu.level[0].arrowHeight=0
oCMenu.level[0].align="bottom"

//RGG
oCMenu.velo=0

//dynamic effect (controllable for each level)
oCMenu.level[0].clippx=2
oCMenu.level[0].cliptim=2
//special animation filters (IE5.5+ only, controllable for each level)
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Fade(duration=0.5)"

//Other special animation filters (IE5.5+ only, controllable for each level)
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Wheel(duration=0.5,spokes=5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Barn(duration=0.5,orientation=horizontal)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Blinds(duration=0.5,bands=5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.CheckerBoard(duration=0.5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Fade(duration=0.5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.GradientWipe(duration=0.5,wipeStyle=0)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Iris(duration=0.5,irisStyle=STAR)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Iris(duration=0.5,irisStyle=CIRCLE)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Pixelate(duration=0.5,maxSquare=40)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Wheel(duration=0.5,spokes=5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.RandomDissolve(duration=0.5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Spiral(duration=0.5)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Stretch(duration=0.5,stretchStyle=push)"
//oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Strips(duration=0.5,motion=rightdown)"


//EXAMPLE SUB LEVEL[1] PROPERTIES - You have to specify the properties you want different from LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[1]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[1].width=oCMenu.level[0].width
oCMenu.level[1].height=38
oCMenu.level[1].regClass="clLevel1"
oCMenu.level[1].overClass="clLevel1over"
oCMenu.level[1].borderX=0
oCMenu.level[1].borderY=0
oCMenu.level[1].align="right"
oCMenu.level[1].offsetX=0
oCMenu.level[1].offsetY=0
oCMenu.level[1].borderClass="clLevel1border"
oCMenu.level[1].arrow="/html/imagenes/icoMenuDesplegar.gif"
oCMenu.level[1].arrowWidth=10
oCMenu.level[1].arrowHeight=10
//dynamic effect
oCMenu.level[1].clippx=2
oCMenu.level[1].cliptim=2
//special animation filters
//oCMenu.level[1].filter="progid:DXImageTransform.Microsoft.Fade(duration=0.5)"


//EXAMPLE SUB LEVEL[2] PROPERTIES - You have to specify the properties you want different from LEVEL[1] OR LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[2]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[2].width=150
oCMenu.level[2].height=38
oCMenu.level[2].offsetX=0
oCMenu.level[2].offsetY=0
oCMenu.level[2].regClass="clLevel2"
oCMenu.level[2].overClass="clLevel2over"
oCMenu.level[2].borderClass="clLevel2border"


/******************************************
Menu item creation:
myCoolMenu.makeMenu(name, parent_name, text, link, target, width, height, regImage, overImage, regClass, overClass , align, rows, nolink, onclick, onmouseover, onmouseout)
*************************************/
<%
	try
	{
		Menu menu = new Menu(request);
		menu.setMenuIsAtTop(menuPos.equals(SIGAConstants.MENU_TOP));
		menu.searchMenu(profile, institucion);
		Vector v = menu.getAllRecords();

		StringBuffer buf=new StringBuffer();
		menu.paintMenu(v, buf, "menu");
		out.println(buf.toString());
	}

	catch(Exception e)
	{
		// inc6961 // No hace falta escribir esto en el log
		//ClsLogging.writeFileLogError("ERROR EN MENU:"+e.toString(),e,1);
	}
%>

//Leave this line - it constructs the menu
oCMenu.construct()

</script>