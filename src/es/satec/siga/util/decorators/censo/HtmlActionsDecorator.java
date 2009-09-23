package es.satec.siga.util.decorators.censo;

import org.displaytag.decorator.TableDecorator;

import es.satec.siga.util.vos.impl.LetradoVo;

public class HtmlActionsDecorator extends TableDecorator {
	public String getNoAparecerRedAbogacia(){
		StringBuffer strBuff=new StringBuffer();
		LetradoVo letrado=(LetradoVo)getCurrentRowObject();
		long id = Long.parseLong(letrado.getId());

		strBuff.append("<a href=\"details.jsp?id=");
		strBuff.append(id);
		strBuff.append("&amp;action=view\"><img alt=\"Ver\" src=\"/SIGA/html/imagenes/bconsultar_on.gif\"/></a>");
		strBuff.append(" ");
		strBuff.append("<a href=\"details.jsp?id=");
		strBuff.append(id);
		strBuff.append("&amp;action=edit\"><img alt=\"Editar\" src=\"/SIGA/html/imagenes/beditar_on.gif\"/></a>");
		strBuff.append(" ");
		if (letrado.getNoAparecerRedAbogacia()!=null && letrado.getNoAparecerRedAbogacia().booleanValue()){
			strBuff.append("<img alt=\"No aparece RedAbogacia\" src=\"/SIGA/html/imagenes/bborrar_on.gif\"/>");
		} else {
			strBuff.append("<a href=\"details.jsp?id=");
			strBuff.append(id);
			strBuff.append("&amp;action=send\"><img alt=\"Enviar\" src=\"/SIGA/html/imagenes/benviar_on.gif\"/></a>");
		}

		return strBuff.toString();
	}
}
