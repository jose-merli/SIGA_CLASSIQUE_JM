package com.atos.utils;

/*
   Diferencias respecto a StringTokenizer:

   1.- Sólo un tipo de constructor se ha realizado.
       PSSCStringTokenizer recibirá siempre la cadena a
       dividir y la cadena a buscar para dividir. Nunca
       devolverá la cadena separadora.

   2.- La cadena separadora es tratada como un bloque,
       es decir, no se tratarán sus caracteres de forma
       individual.

   3.- En caso de que una cadena contenga dos veces
       seguidas el caracter separador, se devolverá la
       cadena vacía.

       Ejemplo: PSSCStringTokenizer ("a;;b;c", ";")
                1.- "a"
                2.- ""
                3.- "b"
                4.- "c"

       Ejemplo: PSSCStringTokenizer (";a;b;c;",";")
                1.- ""
                2.- "a"
                3.- "b"
                4.- "c"
                5.- ""
   4.- No se permite cambiar de separador al recorrer la
       cadena (netxToken (String delim))
*/

import java.util.Enumeration;
import java.util.NoSuchElementException;


public class GstStringTokenizer implements Enumeration {

    /** first character to start the search of the next
        token */
    private int start;

    /** True if the string has no more tokens */
    private boolean end;

    /** String to handle */
    private String str;

    /** The string that have to be used as a separator */
    private String token;


    /**
     * Constructs a string tokenizer for the specified string. All
     * characters in the <code>delim</code> argument are used as a
     * single token.
     * <p>
     *
     * @param   str            a string to be parsed.
     * @param   delim          the token.
     */

    public GstStringTokenizer(String str, String token) {
		start = 0;
		end = false;
		this.str = str;
		this.token = token;
    }


    /**
     * Tests if there are more tokens available from this tokenizer's string.
     * If this method returns <tt>true</tt>, then a subsequent call to
     * <tt>nextToken</tt> with no argument will successfully return a token.
     *
     * @return  <code>true</code> if and only if there is at least one token
     *          in the string after the current position; <code>false</code>
     *          otherwise.
     */
    public boolean hasMoreTokens() {
    	return (!end);
    }

    /**
     * Returns the next token from this string tokenizer.
     *
     * @return     the next token from this string tokenizer.
     *
     * @exception  NoSuchElementException  if there are no more tokens in this
     *               tokenizer's string.
     */

    public String nextToken() {
		String strAux;
		int pos;

		if (end) {
			throw new NoSuchElementException();
		}

		pos=str.indexOf (token, this.start);
		if (pos==-1 && !end) {
			strAux=str.substring (start);
			end=true;
		}
		else {
			strAux=str.substring (start,pos);
			start=pos+token.length();
		}


		return (strAux);
	}


    /**
     * Returns the same value as the <code>hasMoreTokens</code>
     * method. It exists so that this class can implement the
     * <code>Enumeration</code> interface.
     *
     * @return  <code>true</code> if there are more tokens;
     *          <code>false</code> otherwise.
     * @see     java.util.Enumeration
     * @see     XStringTokenizer#hasMoreTokens()
     */
    public boolean hasMoreElements() {
		return hasMoreTokens();
    }

    /**
     * Returns the same value as the <code>nextToken</code> method,
     * except that its declared return value is <code>Object</code> rather than
     * <code>String</code>. It exists so that this class can implement the
     * <code>Enumeration</code> interface.
     *
     * @return     the next token in the string.
     * @exception  NoSuchElementException  if there are no more tokens in this
     *               tokenizer's string.
     * @see        java.util.Enumeration
     * @see        XStringTokenizer#nextToken()
     */

    public Object nextElement() {
		return nextToken();
    }

    /**
     * Calculates the number of times that this tokenizer's
     * <code>nextToken</code> method can be called before it generates an
     * exception. The current position is not advanced.
     *
     * @return  the number of tokens remaining in the string using the current
     *          delimiter set.
     */

    public int countTokens() {
    	int pos=0;
    	int count=0;
    	String strAux=str;
    	while ((pos=strAux.indexOf (token))!=-1) {
    		count++;
    		strAux=strAux.substring (pos+token.length());
    	}
    	return (count+1);
    }

}
