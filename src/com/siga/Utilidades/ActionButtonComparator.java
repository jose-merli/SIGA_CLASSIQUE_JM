package com.siga.Utilidades;

import java.util.Comparator;
import com.atos.utils.ActionButtonsConstants;

public class ActionButtonComparator implements Comparator<ActionButtonsConstants>{

	public int compare(ActionButtonsConstants o1, ActionButtonsConstants o2) {
		return o1.compareTo(o2);
	}

}
