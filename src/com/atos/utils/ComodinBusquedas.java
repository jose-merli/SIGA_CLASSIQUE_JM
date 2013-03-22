package com.atos.utils;

import java.util.Hashtable;
import java.util.Vector;

import com.siga.Utilidades.UtilidadesBDAdm;

//import com.siga.Utilidades.UtilidadesString;




/*
 * ComodinBusquedas.java
 * 
 * Clase con métodos estáticos utilizada para conversión valores obtenidos en las búsquedas
 * y transformarlos a valores útiles con comodines para busquedas con la sentencia LIKE
 */

/**
 *
 * Clase con métodos estáticos utilizada para conversión valores obtenidos en las búsquedas
 * y transformarlos a valores útiles con comodines para busquedas con la sentencia LIKE
 */
public class ComodinBusquedas {
	private static  String[] sustituirA = new String[]{"\u00C1","\u00C0","\u00C4"};
	private static  String[] sustituirE = new String[]{"\u00C9","\u00C8","\u00CB"};
	private static  String[] sustituirI = new String[]{"\u00CD","\u00CC","\u00CF"};
	private static  String[] sustituirO = new String[]{"\u00D3","\u00D2","\u00D6"};
	private static  String[] sustituirU = new String[]{"\u00DA","\u00D9","\u00DC"};
	
	private static  String A = "\u0041"; 
	private static  String E = "\u0045";
	private static  String I = "\u0049";
	private static  String O = "\u004F";
	private static  String U = "\u0055";
	
	public static  String vocalesSustituir1 = "'AEIOUAEIOUAEIOU'";
	public static  String vocalesSustituir ="'"+A+E+I+O+U+A+E+I+O+U+A+E+I+O+U+"'";
	public static  String vocalesBuscar ="'"+"\u00C1"+"\u00C9"+"\u00CD"+"\u00D3"+"\u00DA"+"\u00C0"+"\u00C8"+"\u00CC"+"\u00D2"+"\u00D9"+"\u00C4"+"\u00CB"+"\u00CF"+"\u00D6"+"\u00DC"+"'";
	  
  
    /** Crea una nueva instancia de ComodinBusquedas.
     *  No utilizar el constructor, usar en su lugar los métodos estáticos
     *  de esta clase.
     */
    public ComodinBusquedas() {
    	 
    	
    }
    
    /** Indica si la cadena de texto tiene comodín o no. Debe llamarse directamente con 
     *  el valor obtenido en la búsqueda sin ninguna transformación. Los valores de comodín 
     *  que se pueden incluir en la búsqueda son el asterico '*' y la interrogación '?'
     *  Este método debe utilizarse únicamente cuando la consulta necesita una preparación
     *  especial.
     *
     *  @params cadena  Cadena de texto obtenida directamente del formulario de búsqueda.
     *  @returns        <code>true</code> si encuentra un comodín en la cadena.
     *                  <code>false</code> si no hay comodín.
     *  
     */
    public static boolean hasComodin(String cadena ){
    	if (cadena == null)
    		return false;
        int posicion = 0;                                // posición del carácter en la cadena
        posicion = cadena.indexOf('*') + 1;              //  -1 + 1 si no está
        posicion = posicion + cadena.indexOf('?') + 1;   //  -1 + 1 si no está
        return (posicion != 0);                          //  si cero, falso
    }
    
    /** Devuelve una cadena de caracteres con los comodines reemplazados: 
     *  sustituye   *   por  % 
     *  sustituye   ?   por  _
     *  Este método debe utilizarse únicamente cuando la consulta necesita una preparación
     *  especial.
     *
     *  ejemplo:   ComodinBusquedas.convertir("Est? es_un* prueba");
     *                    devuelve:   "Est_ es_un% prueba"
     *
     *  @params cadena  Cadena de texto obtenida directamente del formulario de búsqueda.
     *  @returns        La cadena de texto recibida como parámetro con los comodines cambiados.
     *  
     */
    public static String convertir( String cadena ) {
        String temp = "";
        temp = cadena.replace('*', '%');
        temp =   temp.replace('?', '_');
        temp = temp.toUpperCase();
        return temp;
    }


    /** Prepara un campo con o sin comodines para ser utilizado en una búsqueda
     *  sustituye   *   por  % 
     *  sustituye   ?   por  _
     *  anhade = o LIKE según tenga o no comodines.
     *  anhade comillas para englobar el campo de texto.
     *  Este método debe utilizarse siempre que en una búsqueda aparezca un campo de texto
     *  susceptible de utilizar comodines (Todos menos los indicados a no usarlo).
     *
     *  ejemplo 1:    campo = "Mad*"    // obtenido de de un campo de búsqueda
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   "LIKE 'MAD%'"
     *  
     *  ejemplo 2:    campo = "Madrid"
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   "= 'MADRID'"
     *
     *
     *  @params cadena  Cadena de texto obtenida directamente del formulario de búsqueda.
     *  @returns        La cadena de texto transformada para ser concatenada a la select de 
     *                  búsqueda.
     *  
     */
    public static String prepararSentencia( String cadena ) {
        String temp = "";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        cadena = (cadena.toUpperCase()).trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
        if ( ComodinBusquedas.hasComodin( cadena ) ){
            temp = "LIKE '" + ComodinBusquedas.convertir(cadena) + "'";
        } else {
            temp = "LIKE '" + cadena + "'";
        }
        return temp;
    }
    
    public static String prepararSentenciaExacta( String cadena ) {
        String temp = "";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        
        cadena=UtilidadesBDAdm.validateChars(cadena);
            temp = " = '" + cadena+"'" ;
        
        return temp;
    }
    
    public static String prepararSentenciaExactaBind( String cadena, int contador, Hashtable codigos ) {
        String temp = "";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        
        cadena=UtilidadesBDAdm.validateChars(cadena);
        codigos.put(new Integer(contador),cadena);
            temp = " = :" + contador;
        
        return temp;
    }
    
    public static String prepararSentenciaExactaTranslateUpper( String cadena ) {
        String temp = "";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        
        cadena=UtilidadesBDAdm.validateChars(cadena);
            temp = " = translate(upper('" + cadena+"'),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")" ;
        
        return temp;
    }
    
    /** Prepara un campo con o sin comodines para ser utilizado en una búsqueda
     *  sustituye   *   por  % 
     *  sustituye   ?   por  _
     *  anhade tantos OR LIKE como 0 haya que completar para que la cadena sea de longitud 9 cuando el usuario no ha introducido comodines.
     *  anhade comillas para englobar el campo de texto.
     *  Este método (de momento) sólo se usa para las busquedas sobre el campo NIF/CIF
     *
     *  ejemplo 1:    campo = "111*"    // obtenido de de un campo de búsqueda
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   "LIKE '111%'"
     *  
     *  ejemplo 2:    campo = "111N"
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   " OR LIKE '111N%' OR LIKE '0111N%' OR LIKE '00111N%' OR LIKE '000111N%' OR LIKE '0000111N%' OR LIKE '00000111N%'"
     *                       es decir, tantos OR como 0 haya que completar para que la cadena sea de longitud 9
     *
     *
     *  @params cadena  Cadena de texto obtenida directamente del formulario de búsqueda.
     *  @returns        La cadena de texto transformada para ser concatenada a la select de 
     *                  búsqueda.
     *  
     */
    /*public static String prepararSentenciaNIF( String cadena ) {
        String temp = "";
        cadena = cadena.toUpperCase();
        
        if ( ComodinBusquedas.hasComodin( cadena ) ){
            temp = "LIKE '" + ComodinBusquedas.convertir(cadena) + "'";
        } else {
            temp = ComodinBusquedas.preparaCadenaSinComodin(cadena);
        }
        return temp;
    }*/

    /** Devuelve la cadena con la sentencia de busqueda que habria que utilizar cuando el usuario NO ha
     *  introducido comodines en la busqueda del NIF/CIF.
     *  Se ponen tantos like como 0 haya que añadir a la cadena hasta completar los 9 caracteres 
     * *  Este método (de momento) sólo se usa para las busquedas sobre el campo NIF/CIF
     *
     *  
     *  ejemplo 1:    campo = "111N"
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   " (UPPER(CEN_PERSONA.NIFCIF) LIKE '111N%' OR
     * 										UPPER(CEN_PERSONA.NIFCIF) LIKE '0111N%' OR
     * 										UPPER(CEN_PERSONA.NIFCIF) LIKE '00111N%' OR
     * 										UPPER(CEN_PERSONA.NIFCIF) LIKE '000111N%' OR
     * 										UPPER(CEN_PERSONA.NIFCIF) LIKE '0000111N%' OR
     * 										UPPER(CEN_PERSONA.NIFCIF) LIKE '00000111N%') 
     */
    public static String prepararSentenciaNIF( String cadena, String campo ) {
        String cadenaTemp = "";
        String ceros="0";
        String cerosAux="";
        String campoNIF=campo;
        cadena=cadena.trim();
        /*if (cadena.length()<9){
        	for (int i=0; i<9-cadena.length();i++){
        		cerosAux=cerosAux+ceros;        		
        		if (i==0){
        			cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";        	
        		}
        		cadenaTemp+=" OR "+campoNIF+" LIKE UPPER('"+cerosAux+cadena+"%')";	
        	}        		
        } else
      		 cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";
        cadenaTemp+=")";*/
        cadena=UtilidadesBDAdm.validateChars(cadena);
        cadenaTemp="regexp_like ("+campoNIF+",'^[0]{0,8}"+cadena+"\\w*$')";
        return cadenaTemp;
    }
    
    public static String prepararSentenciaNIFBind( String cadena, String campo, int contador, Hashtable codigos ) {
        String cadenaTemp = "";
        String ceros="0";
        String cerosAux="";
        String campoNIF=campo;
        cadena=cadena.trim();
        /*if (cadena.length()<9){
        	for (int i=0; i<9-cadena.length();i++){
        		cerosAux=cerosAux+ceros;        		
        		if (i==0){
        			cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";        	
        		}
        		cadenaTemp+=" OR "+campoNIF+" LIKE UPPER('"+cerosAux+cadena+"%')";	
        	}        		
        } else
      		 cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";
        cadenaTemp+=")";*/
        cadena=UtilidadesBDAdm.validateChars(cadena);
        
        codigos.put(new Integer(contador),cadena+"%");
        cadenaTemp +=" LTRIM(UPPER("+campoNIF+"),'0') LIKE LTRIM(UPPER(:"+contador+"),'0') ";
        //cadenaTemp="regexp_like ("+campoNIF+",'^[0]{0,8}:"+contador+"\\w*$')";
        
        return cadenaTemp;
    }
    
    public static String prepararSentenciaNIFExacta( String cadena, String campo ) {
        String cadenaTemp = "";
        String ceros="0";
        String cerosAux="";
        String campoNIF=campo;
        cadena=cadena.trim();
        /*if (cadena.length()<9){
        	for (int i=0; i<9-cadena.length();i++){
        		cerosAux=cerosAux+ceros;        		
        		if (i==0){
        			cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";        	
        		}
        		cadenaTemp+=" OR "+campoNIF+" LIKE UPPER('"+cerosAux+cadena+"%')";	
        	}        		
        } else
      		 cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";
        cadenaTemp+=")";*/
        cadena=UtilidadesBDAdm.validateChars(cadena);
        cadenaTemp="regexp_like ("+campoNIF+",'^[0]{0,8}"+cadena+"$')";
        return cadenaTemp;
    }

    public static String prepararSentenciaNIFExactaBind( String cadena, String campo, int contador, Hashtable campos ) {
        String cadenaTemp = "";
        String ceros="0";
        String cerosAux="";
        String campoNIF=campo;
        cadena=cadena.trim();

        cadena=UtilidadesBDAdm.validateChars(cadena);
        cadenaTemp="regexp_like (:"+contador+",'^[0]{0,8}"+cadena+"$')";
        
        campos.put (new Integer(contador), campoNIF);
        return cadenaTemp;
    }

    
    public static String prepararSentenciaNIFUpper( String cadena, String campo ) {
        String cadenaTemp = "";
        String ceros="0";
        String cerosAux="";
        String campoNIF=campo;
        cadena=cadena.trim().toUpperCase();
        /*if (cadena.length()<9){
        	for (int i=0; i<9-cadena.length();i++){
        		cerosAux=cerosAux+ceros;        		
        		if (i==0){
        			cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";        	
        		}
        		cadenaTemp+=" OR "+campoNIF+" LIKE UPPER('"+cerosAux+cadena+"%')";	
        	}        		
        } else
      		 cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";
        cadenaTemp+=")";*/
        cadena=UtilidadesBDAdm.validateChars(cadena);
        cadenaTemp="regexp_like (upper("+campoNIF+"),'^[0]{0,8}"+cadena+"\\w*$')";
        return cadenaTemp;
    }
    
    public static String prepararSentenciaNIFUpperExacta( String cadena, String campo ) {
        String cadenaTemp = "";
        String ceros="0";
        String cerosAux="";
        String campoNIF=campo;
        cadena=cadena.trim().toUpperCase();
        /*if (cadena.length()<9){
        	for (int i=0; i<9-cadena.length();i++){
        		cerosAux=cerosAux+ceros;        		
        		if (i==0){
        			cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";        	
        		}
        		cadenaTemp+=" OR "+campoNIF+" LIKE UPPER('"+cerosAux+cadena+"%')";	
        	}        		
        } else
      		 cadenaTemp=" ("+campoNIF+" LIKE UPPER('"+cadena+"%')";
        cadenaTemp+=")";*/
        cadena=UtilidadesBDAdm.validateChars(cadena);
        cadenaTemp="regexp_like (upper("+campoNIF+"),'^[0]{0,8}"+cadena+"$')";
        return cadenaTemp;
    }
    
    
    /** Prepara un campo con o sin comodines para ser utilizado en una búsqueda
     *  sustituye   *   por  % 
     *  sustituye   ?   por  _
     *  anhade = o LIKE según tenga o no comodines.
     *  anhade comillas para englobar el campo de texto.
     *  Este método debe utilizarse siempre que en una búsqueda aparezca un campo de texto
     *  susceptible de utilizar comodines (Todos menos los indicados a no usarlo).
     *
     *  ejemplo 1:    campo = "Mad*"    // obtenido de de un campo de búsqueda
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   "LIKE 'MAD%'"
     *  
     *  ejemplo 2:    campo = "Madrid"
     *                ComodinBusquedas.convertir(campo);
     *                       devuelve:   "LIKE '%MADRID%'"
     *
     *
     *  @params cadena  Cadena de texto obtenida directamente del formulario de búsqueda.
     *  @returns        La cadena de texto transformada para ser concatenada a la select de 
     *                  búsqueda.
     *  
     */
    public static String prepararSentenciaCompleta( String cadena, String Campo ) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
        if ( ComodinBusquedas.hasComodin( cadena ) ){
            temp = " LIKE '" + ComodinBusquedas.convertir(cadena) + "'";
            sentenciaCompleta=Campo+temp;
        } else {
            //temp = " LIKE '%" + cadena + "%'";
           
            sentenciaCompleta=" regexp_like("+Campo+",'"+cadena+"')";
        }
       
        return sentenciaCompleta;
    }
    public static String prepararSentenciaCompletaBind( String cadena, String Campo ) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
        if ( ComodinBusquedas.hasComodin( cadena ) ){
            temp = " LIKE " + ComodinBusquedas.convertir(cadena) + "";
            sentenciaCompleta=Campo+temp;
        } else {
            //temp = " LIKE '%" + cadena + "%'";
           
            sentenciaCompleta="regexp_like("+Campo+","+cadena+")";
        }
       
        return sentenciaCompleta;
    }
    
    public static String prepararSentenciaCompletaBind( String cadena, String Campo, int contador, Hashtable codigos ) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
       
        if ( ComodinBusquedas.hasComodin( cadena ) ){
        	
            codigos.put(new Integer(contador),ComodinBusquedas.convertir(cadena));
            temp = " LIKE :"+contador + " ";
            sentenciaCompleta=Campo+temp;
        } else {
        	
            codigos.put(new Integer(contador),cadena);
            sentenciaCompleta="regexp_like("+Campo+",:"+contador+")";
            
        }
       
        
        return sentenciaCompleta;
    }
    
    
    
    public static String prepararSentenciaCompletaTranslateUpper( String cadena, String Campo ) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
        if ( ComodinBusquedas.hasComodin( cadena ) ){ 
        	
            temp = " LIKE translate(upper('" + ComodinBusquedas.convertir(cadena) + "'),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")";
        } else {
            temp = " LIKE translate(upper('%" + cadena + "%'),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")";
        }
        sentenciaCompleta=" translate(upper("+Campo+"),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")"+temp;
        return sentenciaCompleta;
    }
    
    public static String prepararSentenciaCompletaTranslateUpperBind( String cadena, String Campo,int contador,Hashtable codigos ) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
        if ( ComodinBusquedas.hasComodin( cadena ) ){
        	codigos.put(new Integer(contador),ComodinBusquedas.convertir(cadena));
            temp = " LIKE translate(upper(:" + contador + "),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")";
        } else {
        	codigos.put(new Integer(contador),cadena+"%");
            temp = " LIKE translate(upper(:"+contador+"),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")";
        }
        sentenciaCompleta=" translate(upper("+Campo+"),"+ComodinBusquedas.vocalesBuscar+","+ComodinBusquedas.vocalesSustituir+")"+temp;
        return sentenciaCompleta;
    }
    
    public static String prepararSentenciaNLS( String cadena, String Campo ) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        cadena=UtilidadesBDAdm.validateChars(cadena);
        if ( ComodinBusquedas.hasComodin( cadena ) ){
            temp = " LIKE translate('" + ComodinBusquedas.convertir(cadena.toUpperCase()) + "','ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜ','AEIOUAEIOUAEIOU')";
        } else {
            temp = " LIKE translate('%" + cadena.toUpperCase() + "%','ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜ','AEIOUAEIOUAEIOU')";
        }
        //sentenciaCompleta="UPPER("+Campo+")"+temp;
        sentenciaCompleta="translate(upper(" + Campo + "),'ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜ','AEIOUAEIOUAEIOU')"+temp;
        return sentenciaCompleta;
    }
    
    public static String prepararSentenciaCompletaUPPER( String cadena, String Campo ) {
    	 return prepararSentenciaCompleta(  cadena.toUpperCase(),  "UPPER("+Campo+")" );
    }
    public static String prepararSentenciaCompletaUPPERBind( String cadena, String Campo ) {
   	 return prepararSentenciaCompletaBind(  cadena.toUpperCase(),  "UPPER("+Campo+")" );
   }
    /** Prepara el campo numero colegiado y numero comunitario con o sin comodines para ser utilizado en una búsqueda
     *  de tal manera que si no se emplean comodines de busqueda y se añaden 0 por la izquierda al numero introducido, 
     *  estos 0 son ignorados por la busqueda, luego si buscamos por '00002' o por '2' el resultado sera el mismo, 
     *  nos devolvera el numero de colegiado '2'
     *  sustituye   *   por  % 
     *  sustituye   ?   por  _
     *  anhade comillas para englobar el campo de texto.
     *
     *  @params cadena  Cadena de texto obtenida directamente del formulario de búsqueda.
     *  @params campo   Campo de base de datos.
     *  @returns        La cadena de texto transformada para ser concatenada a la select de 
     *                  búsqueda.
     *  
     */
    public static String tratarNumeroColegiado( String cadena, String Campo ) {
    	 String temp = "";
         String sentenciaCompleta="";
         String sentenciaAux="";
         String cadenaAux[]=cadena.trim().replaceAll("'","").split(",");
         for (int i=0;i<cadenaAux.length;i++){ 
	         if ( ComodinBusquedas.hasComodin( cadenaAux[i] ) ){
	             temp = " LIKE '" + ComodinBusquedas.convertir(cadenaAux[i].trim()) + "'";
	         } else {
	            
	         	 temp="regexp_like ("+Campo+",'^[0]{0,}"+cadenaAux[i].trim()+"$')";
	         }
	         
	         if (i==0){	
	         	 if ( ComodinBusquedas.hasComodin( cadenaAux[i].trim() ) ){
	          	    sentenciaAux=Campo+temp;
	         	 }else{
	         	 	sentenciaCompleta=temp;	
	         	 }
	         }else{
	         	if ( ComodinBusquedas.hasComodin( cadenaAux[i].trim() ) ){
	          	   sentenciaAux=" OR "+Campo+temp;
	         	}else{
	         	   sentenciaAux=" OR "+temp;
	         	}
	         }
	         sentenciaCompleta+=sentenciaAux;
         }    
        
         return "("+sentenciaCompleta+")";
     }
    
    public static Vector tratarNumeroColegiadoBind( String cadena, String Campo,int contador, Hashtable codigos ) {
   	 String temp = "";
        String sentenciaCompleta="";
        String sentenciaAux="";
        String result="";
       Vector resultV=new Vector();
        String cadenaAux[]=cadena.trim().replaceAll("'","").split(",");
        for (int i=0;i<cadenaAux.length;i++){ 
	         if ( ComodinBusquedas.hasComodin( cadenaAux[i] ) ){
	             temp = " LIKE '" + ComodinBusquedas.convertir(cadenaAux[i].trim()) + "'";
	         } else {
	            
	         	contador++; 
	         	temp +=" LTRIM(UPPER("+Campo+"),'0') LIKE LTRIM(UPPER(:"+contador+"),'0') ";
	         }
	         
	         if (i==0){	
	         	 if ( ComodinBusquedas.hasComodin( cadenaAux[i].trim() ) ){
	         	 	contador++;
	         	 	codigos.put(new Integer(contador),temp);
	          	    sentenciaAux=Campo+":"+contador;
	         	 }else{
	         	 	contador++;
	         	 	codigos.put(new Integer(contador),temp);
	         	 	sentenciaCompleta=":"+contador;	
	         	 }
	         }else{
	         	if ( ComodinBusquedas.hasComodin( cadenaAux[i].trim() ) ){
	         		contador++;
	         	 	codigos.put(new Integer(contador),temp);
	          	   sentenciaAux=" OR "+Campo+":"+contador;
	         	}else{
	         		contador++;
	         	 	codigos.put(new Integer(contador),temp);
	         	   sentenciaAux=" OR "+":"+contador;
	         	}
	         }
	         sentenciaCompleta+=sentenciaAux;
        }  
        result="("+sentenciaCompleta+")";
        resultV.add(new Integer(contador));
		resultV.add(result);
        return resultV;
    }
     
    public static String tratarNumeroColegiadoAproximado( String cadena, String Campo ) {
        String temp = "";
        String sentenciaCompleta="";
        String sentenciaAux="";
        String cadenaAux[]=cadena.trim().replaceAll("'","").split(",");
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        for (int i=0;i<cadenaAux.length;i++){
        	
        	if ( ComodinBusquedas.hasComodin( cadenaAux[i].trim() ) ){
	            temp= "  LIKE '" + ComodinBusquedas.convertir(cadenaAux[i].trim()) + "'";
	        } else {
	            temp= "  LIKE '%" + cadenaAux[i].trim() + "%'";
	        }
          if (i==0){	
	        
          	sentenciaAux=Campo+temp;
          }else{
          	
          	sentenciaAux=" OR "+Campo+temp;
          }
          sentenciaCompleta+=sentenciaAux;
        }    
        
        return "("+sentenciaCompleta+")";
    }
    public static Vector tratarNumeroColegiadoAproximadoBind( String cadena, String Campo, int contador, Hashtable campos ) 
    {
        String temp = "";
        String sentenciaCompleta="";
        String sentenciaAux="";
        String cadenaAux[]=cadena.trim().replaceAll("'","").split(",");
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        //cadena = cadena.trim();
        for (int i=0;i<cadenaAux.length;i++){
        	
        	if ( ComodinBusquedas.hasComodin( cadenaAux[i].trim() ) ){
	            temp= "  LIKE '" + ComodinBusquedas.convertir(cadenaAux[i].trim()) + "'";
	        } else {
	            temp= "  LIKE '" + cadenaAux[i].trim() + "%'";
	        }
          if (i==0){	
	        contador ++;
	        campos.put (new Integer(contador), Campo);
          	sentenciaAux=":"+contador+temp;
          }else{
          	
  	        contador ++;
	        campos.put (new Integer(contador), Campo);
          	sentenciaAux=" OR :"+contador+temp;
          }
          sentenciaCompleta+=sentenciaAux;
        }    
        
        Vector salida = new Vector ();
        salida.add(new Integer(contador));
        salida.add("("+sentenciaCompleta+")");
        return salida;
    }
    
    public static String sustituirVocales(String cadena) {
        String temp = "";
        String sentenciaCompleta="";
        /* La cadena introducida se pasa a mayusculas y se eliminan los blancos
         * por la derecha y por la izquierda
         */
        for (int i=0;i<sustituirA.length;i++){
        	String var=sustituirA[i];
        	String var1=A;
          cadena=cadena.replaceAll(sustituirA[i],A);
        }
        for (int i=0;i<sustituirE.length;i++){
            cadena=cadena.replaceAll(sustituirE[i],E);
        }
        for (int i=0;i<sustituirI.length;i++){
            cadena=cadena.replaceAll(sustituirI[i],I);
        }
        for (int i=0;i<sustituirO.length;i++){
            cadena=cadena.replaceAll(sustituirO[i],O);
        }
        for (int i=0;i<sustituirU.length;i++){
            cadena=cadena.replaceAll(sustituirU[i],U);
        }
       
       
        return cadena;
    }
    
 }
