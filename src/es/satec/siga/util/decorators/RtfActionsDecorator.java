package es.satec.siga.util.decorators;

import org.displaytag.decorator.TableDecorator;

public class RtfActionsDecorator extends TableDecorator {
	public String getAcciones(){
		StringBuffer strBuff=new StringBuffer();

		strBuff.append("Ver");
		strBuff.append(" | ");
		strBuff.append("Editar");
		strBuff.append(" | ");
		strBuff.append("Borrar");

		return strBuff.toString();
	}
}
