package com.siga.Utilidades;

import java.util.Comparator;
import com.atos.utils.SearchButtonsConstants;

public class SearchButtonComparator implements Comparator<SearchButtonsConstants>{

	public int compare(SearchButtonsConstants o1, SearchButtonsConstants o2) {
		return o1.getOrden()<o2.getOrden()?-1:1;
	}

}
