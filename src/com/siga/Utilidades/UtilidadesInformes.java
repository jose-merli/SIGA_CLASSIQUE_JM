
/*
 * Created on 13-ene-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UtilidadesInformes {

	public static String sustituirPosiciones (String cadena, double pos) {
		
		String [][] comodin = {{"%%CONTADORCM%%",       "0"  },
							   {"%%CONTADORCMMAS%%",    "0.5"},
							   {"%%CONTADORCMMASMAS%%", "1"  },
							   {"%%CONTADORC_0.5",      "0.5"},
							   {"%%CONTADORC_1",        "1"  },
							   {"%%CONTADORC_1.5",      "1.5"},
							   {"%%CONTADORC_2",        "2"  },
							   {"%%CONTADORC_2.5",      "2.5"},
							   {"%%CONTADORC_3",        "3"  },
							   {"%%CONTADORC_3.5",      "3.5"},
							   {"%%CONTADORC_4",        "4"  },
							   {"%%CONTADORC_4.5",      "4.5"},
							   {"%%CONTADORC_5",        "5"  }};
		
		if ((cadena == null) || (cadena.length() < 1)) return cadena;
		
		for (int i = 0; i < comodin.length; i++) {
			if (cadena.indexOf(comodin[i][0]) != -1) {
				Double d = new Double(comodin[i][1]);
				double aux = d.doubleValue() + pos;
				cadena = cadena.replaceAll(comodin[i][0], new Double(aux).toString());
			}
		}
		return cadena;
	}
}
