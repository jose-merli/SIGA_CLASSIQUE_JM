package es.satec.siga.util.decorators;

import org.displaytag.decorator.TableDecorator;

import es.satec.siga.util.vos.impl.LetradoVo;

public class HtmlActionsDecorator extends TableDecorator {
	public String getAcciones(){
		StringBuffer strBuff=new StringBuffer();
		int id = Integer.parseInt(((LetradoVo)getCurrentRowObject()).getId());

		strBuff.append("<a href=\"details.jsp?id=");
		strBuff.append(id);
		strBuff.append("&amp;action=view\"><img alt=\"Ver\" src=\"/SIGA/html/imagenes/bconsultar_on.gif\"/></a>");
		strBuff.append(" ");
		strBuff.append("<a href=\"details.jsp?id=");
		strBuff.append(id);
		strBuff.append("&amp;action=edit\"><img alt=\"Editar\" src=\"/SIGA/html/imagenes/beditar_on.gif\"/></a>");
		strBuff.append(" ");
		strBuff.append("<a href=\"details.jsp?id=");
		strBuff.append(id);
		strBuff.append("&amp;action=delete\"><img alt=\"Borrar\" src=\"/SIGA/html/imagenes/bborrar_on.gif\"/></a>");

		return strBuff.toString();
	}
}
