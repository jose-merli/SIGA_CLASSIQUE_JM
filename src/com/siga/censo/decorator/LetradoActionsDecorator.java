package com.siga.censo.decorator;

import java.util.ArrayList;
import java.util.List;

import com.atos.utils.AccessConstants;
import com.atos.utils.ModoConstants;
import com.atos.utils.RowButton;
import com.atos.utils.RowButtonsConstants;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.vos.LetradoVO;
import com.siga.comun.decorator.HtmlActionsDecorator;
import com.siga.comun.vos.Vo;

public class LetradoActionsDecorator extends HtmlActionsDecorator {


	public String getActions(){
		Vo vo = (Vo)getCurrentRowObject();
		String id = vo.getId();

		String modo = (String) this.getPageContext().getAttribute("modo");
		String accessType = (String) this.getPageContext().getAttribute("accessType");
		ModoConstants modoConstant = ModoConstants.getEnum(modo);
		AccessConstants accessConstant = AccessConstants.getEnum(accessType);

		List<RowButton> botones = getBotones(vo,modoConstant);
		setBotones(botones, modoConstant, accessConstant);
		return printButtons(id, botones);
	}


	private List<RowButton> getBotones(Vo vo, ModoConstants modo) {
		List<RowButton> botones = new ArrayList<RowButton> ();
		LetradoVO letrado = (LetradoVO) vo;

		botones.add(new RowButton(RowButtonsConstants.CONSULTAR));

		//			if (letrado.getIdInstitucion().equals(userBean.getLocation()))
		botones.add(new RowButton(RowButtonsConstants.EDITAR));
		//			else
		//				botones.add(new RowButton(RowButtonsConstants.EDITAR, RowButton.Estado.DISABLED));

		if (UtilidadesString.stringToBoolean(letrado.getNoAparecerRedAbogacia())) {
			botones.add((new RowButton(RowButtonsConstants.LOPD))); 
		}
		else{
			RowButton rb = new RowButton(RowButtonsConstants.COMUNICAR);
			botones.add(rb);
		}

		return botones;
	}

}
