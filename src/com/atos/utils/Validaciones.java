package com.atos.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


 /**
 * Implementa las funciones de validación para los formularios
 * @version 1.0
 * $date 29/01/03
 */

public final class Validaciones{

 /**
 * Variables y métodos get y set necesarios para realizar el control de los campos del formularios
 */
         /* Caracteres válidos */
        private String caracteresValidos = "[A-Z|a-z|º|ª|nh|Ñ|á|é|í|ó|ä|ë|ö|ü|ç|à|è|ì|ò|ù]";
        /* Caracteres válidos para control de usuarios*/
        private String caracteresValidosUsuario = "[A-Z|a-z|0-9|nh|Ñ|á|é|í|ó|ä|ë|ö|ü|ç|à|è|ì|ò|ù]";
        /* Caracteres inválidos */
        private String caracteresInvalidos = "[\\\\¨$´¤¾¼½|]";
        /* Caracteres numericos */
        private String caracteresNumericos = "[0-9]";
        /* Atributos para las validaciones */
        private Pattern patron = null;
        private Matcher matcher = null;
        /* Atributos para los mensajes de error */
        private String mensajeError = null;
        private String defineError = null;

        /*
        * ERRORES
        * La aplicacion que utiliza esta clase esta desarrollada con Struts, por eso utilizamos este sistema de mensajes de error.
        * Si hubiera un error en algún validación el método devolvería , por ejemplo: "nombreCliente.vacio".
        * "NombreCliente" sería la cadena que le pasamos al método y que nos indica que estamos validando el nombre del cliente
        * y ".vacio"  sería la parte del mensaje de error que nos permite identificar el tipo de error detectado al validar. En nuestro
        * ejemplo indicaría que el campo NombreCliente está vacio.
        */
        public void setMensajeError(String mensajeError){ this.mensajeError = mensajeError; }
        public String getMensajeError(){ return mensajeError; }
        public void setDefineError(String defineError){ this.defineError = defineError; }
        public String getDefineError(){ return defineError; }

 /**
 * Funciones que van a realizar las validaciones de los distintos campos de los formularios.
 *
 */
        /**
        * Función que valida si un campo de texto esta correctamente completado
        * @param nombre nombre de la función get asociada al campo
        * @param obligatorio determina si el campo es o no obligatorio
        * @param sinNumero indica si es obligatorio que este sin número el campo
        * @param cadena cadena de texto a la que se asocia el campo para sacar mensajes
        * @param maximo longitud máxima del texto
        * @param minimo cantidad mínima requerida
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
         public boolean validaCampoTexto(String nombre, boolean obligatorio, boolean sinNumero, String cadena, int maximo, int minimo){
                String campo = nombre.trim();
                if (obligatorio){
                        if (campo.length() <= 0){
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0){
                        if (campo.length() > maximo) {
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                        if (campo.length() < minimo) {
                                setMensajeError(cadena + ".minimo");
                                return false;
                        }else{
                                patron = Pattern.compile(caracteresInvalidos);
                                matcher = patron.matcher(campo);
                                if (matcher.find()){
                                        setMensajeError(cadena + ".caracter");
                                        return false;
                                }
                                patron = Pattern.compile(caracteresValidos);
                                matcher = patron.matcher(campo);
                                int encontrados = 0;
                                char[] caracteres = campo.toCharArray();
                                for (int i = 0; i < caracteres.length; i++){
                                        matcher = patron.matcher(Character.toString(caracteres[i]));
                                        if (matcher.find())
                                                encontrados++;
                                }
                                if (encontrados < 2){
                                        setMensajeError(cadena + ".formato");
                                        return false;
                                }
                        }//cierra else
                }
                return true;
        }//Fin del método validaCampoTexto()

        /**
        * Método que valida los campos select.
        * @param nombre valor del campo del formulario
        * @param cadena referencia necesaria para dar mensaje de error
         * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaCampoSelect(String nombre, String cadena){
                //OJO entiendo que en el desplegable, la opción vacío va a venir con 0. sino habría que mirarlo
                if (nombre.trim().compareTo("")==0){
                        setMensajeError(cadena + ".vacio");
                        return false;
                }
                return true;
        }// Fin del método validaCampoSelect()

        /**
        * Chequea que el valor que se le pasa sea un número sin signo y entero.
        * @param nombre cadena que va a ser controlada
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean isNotNumero(String nombre) {
                patron = Pattern.compile(caracteresValidos);
                matcher = patron.matcher(nombre);
                if (matcher.find()){
                        return true;
                }else{
                        return false;
                }
        }// Fin del método isNumero()


        /**
        * Valida que los formatos numéricos sean correctos.
        * @param numero valor del campo del formulario
        * @param obligatorio si debe rellenarse o no
        * @param sinNumero si no admite numeros,
        * @param cadena string que se muestra en caso de error
        * @param maximo longitud del campo
        * @param fijo si la longitud debe ser siempre la misma
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaCampoNumeros(String nombre, boolean obligatorio, String cadena, int maximo, boolean fijo){
                String campo = nombre.trim();
                if (obligatorio){
                        if (campo.length() <= 0){
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0){
                        if (isNotNumero(campo)) {
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                }
                if (fijo){
                        if (campo.length() != maximo) {
                                setMensajeError(cadena + ".fijo");
                                return false;
                        }
                }else{
                        if (campo.length() > maximo){
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                }
                return true;
        }// Fin del método validaCampoNumeros()


        /**
        * Valida el campo teléfono de cualquier formulario.
        * @param telefono valor del campo del formulario
        * @param obligatorio si debe rellenarse o no
        * @param contacto si puede ser un móvil
        * @param cadena string que se muestra en caso de error
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaTelefono(String telefono, boolean obligatorio, boolean contacto, String cadena){
                String campo = telefono.trim();
                if (obligatorio){
                        if (campo.length() <= 0) {
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0){
                        if (isNotNumero(campo)){
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                        if (campo.length() != 9){
                                setMensajeError(cadena + ".fijo");
                                return false;
                        }
                        char ch = campo.charAt(0);
                        if (contacto){
                                if ((ch != '6') && (ch != '9')) {
                                        setMensajeError(cadena + ".formato");
                                        return false;
                                }
                        }else{
                                if  (ch != '9'){
                                        setMensajeError(cadena + ".formato");
                                        return false;
                                }
                        }
                }
                return true;
        }//Fin del método validaTelefono()

        /**
        * Valida el campo teléfono movil de cualquier formulario.
        * @param telefono valor del campo del formulario
        * @param obligatorio si debe rellenarse o no
        * @param contacto si puede ser un móvil
        * @param cadena string que se muestra en caso de error
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaTelefonoMovil(String telefono, boolean obligatorio, boolean contacto, String cadena)
        {
                String campo = telefono.trim();

                if (obligatorio)
                {
                        if (campo.length() <= 0)
                        {
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0)
                {
                        if (isNotNumero(campo))
                        {
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                        if (campo.length() != 9)
                        {
                                setMensajeError(cadena + ".fijo");
                                return false;
                        }
                        char ch = campo.charAt(0);

                        if (ch != '6')
                        {
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                }

                return true;
        }//Fin del método validaTelefonoMovil

        /**
        * Valida el campo email de cualquier formulario.
        * @param email valor del campo del formulario,
        * @param cadena string que se muestra en caso de error
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaEmail(String email, String cadena){
                String campo = email.trim();
                if (campo.length() > 0){
                        if (campo.length() < 6){
                                setMensajeError(cadena + ".minimo");
                                return false;
                        }
                        if (campo.length() > 32){
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                        if ((campo.indexOf("@") < 1) || (campo.indexOf("@") > (campo.length() - 5))) {
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                        if (campo.indexOf(".") > (campo.length() - 3)){
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                }
                return true;
        }//Fin del método validaEmail()


        /**
        * Método que valida el formato de un NIF y lo formatea rellenandolo con ceros y guion
        * @param nif valor del campo del formulario
        * @param mensaje string que se muestra en caso de error
        * @return true o false, dependiendo de si el campo cumple todos los requisitos o no
        */
        public boolean validaNif(String nif, String mensaje)
        {
                // quito el guion si lo hay
                String nif_sin_guion = nif.replaceAll("-","");
                //quito espacios en blanco
                nif = nif_sin_guion.trim();
                //calculo la longitud
                int longitud_nif = nif.length();
                String expresion="[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]";
                Pattern patron = null;
                Matcher coincidencias_inicio = null;
                Matcher coincidencias_fin = null;
                patron = Pattern.compile(expresion);
                String inicio=nif.substring(0,1);
                String fin=nif.substring(longitud_nif - 1);
                coincidencias_inicio = patron.matcher(inicio);
                coincidencias_fin = patron.matcher(fin);
                /**************** nif x9999999-x y empieza por X o L ***************/
                if ((nif.startsWith("X") || nif.startsWith("x") || nif.startsWith("L") || nif.startsWith("l") ) && coincidencias_fin.find())
                {
                        //calcula numero de ceros a incluir
                        int num_ceros_relleno = 9 - longitud_nif;
                        //saco la letra izquerda
                        String primera_letra = nif.substring(0,1);
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                        {
                                 primera_letra = primera_letra + "0";
                        }
                        //quito la primera letra
                        String nif_sin_letra_primera = nif.substring(1);
                        //quito la ultima letra
                        String nif_sin_letra_ultima = nif_sin_letra_primera.substring(0,longitud_nif-2);
                        String nif_final = primera_letra.toUpperCase() + nif_sin_letra_ultima + "-" + nif.substring(longitud_nif-1).toUpperCase();

                        // si llamas al método set del atributo del nif original lo sustituye  por el nuevo nif formateado
                        //Ej: setDocOriginal(nif_final);
                        return true;
                }//cierra if primer caso
                /************** nif x-9999999 ************************/
                else if (coincidencias_inicio.find())
                {
                        //calcula numero de ceros a incluir
                        int num_ceros_relleno = 9 - longitud_nif;
                        //saco la letra izquerda
                        String primera_letra = nif.substring(0,1);
                        //quito la primera letra
                        String nif_sin_letra_primera = nif.substring(1);
                        String nif_con_ceros="";
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                        {
                                 nif_sin_letra_primera =  "0"+nif_sin_letra_primera;
                        }
                        String nif_final = primera_letra.toUpperCase() +"-"+ nif_sin_letra_primera;
                        // si llamas al método set del atributo del nif original lo sustituye  por el nuevo nif formateado
                        //Ej: setDocOriginal(nif_final);
                        return true;
                }
                /************** nif 9999999-x ************************/
                else if (coincidencias_fin.find())
                {
                        //calcula numero de ceros a incluir
                        int num_ceros_relleno = 9 - longitud_nif;
                        //quito la ultima letra
                        String nif_sin_letra_ultima = nif.substring(0,longitud_nif-1);
                        //saco la ultima letra
                        String ultima_letra = nif.substring(longitud_nif-1);
                        for (int i=0;i<num_ceros_relleno ;i++ )
                        {
                                 nif_sin_letra_ultima =  "0"+nif_sin_letra_ultima;
                        }
                        String nif_final =  nif_sin_letra_ultima +"-" +ultima_letra.toUpperCase();
                        // si llamas al método set del atributo del nif original lo sustituye  por el nuevo nif formateado
                        //Ej: setDocOriginal(nif_final);
                        return true;
                }
                else
                        setMensajeError(mensaje+".formato");
                        return 	false;
        }//fin metodo valida nif



        /**
        * Método que comprueba que el nif exista
        * @param nif dato recogido por el formulario en campo nif
        * @param string que se va a devolver en caso de error
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean compruebaNif(String nif, String mensaje) throws NumberFormatException{
                //letras correctas para un nif
                String caracteres = "TRWAGMYFPDXBNJZSQVHLCKE";
                // quito el guion si lo hay
                String nif_sin_guion = nif.replaceAll("-","");
                //quito espacios en blanco
                nif = nif_sin_guion.trim();
                //calculo la longitud
                int longitud_nif = nif.length();
                String expresion="[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]";
                Pattern patron = null;
                Matcher coincidencias_inicio = null;
                Matcher coincidencias_fin = null;
                patron = Pattern.compile(expresion);
                String inicio=nif.substring(0,1);
                String fin=nif.substring(longitud_nif - 1);
                coincidencias_inicio = patron.matcher(inicio);
                coincidencias_fin = patron.matcher(fin);
                /**************** nif x9999999-x y empieza por X o L ***************/
        if ((nif.startsWith("X") || nif.startsWith("x") || nif.startsWith("L") || nif.startsWith("l") ) && coincidencias_fin.find()){
                //guardo el nif sin la letra
                String nif1 = nif.substring(1,longitud_nif-1);
                //algoritmo
                int nif2 =  Integer.parseInt(nif1);
                int posicion = (nif2 % 23)+1;
                String caracter = caracteres.substring(posicion-1,posicion);
                if (caracter.compareToIgnoreCase(fin) == 0){
                        return true;
                }else
                setMensajeError(mensaje+".error");
                return false;
        }
        /************** nif x-9999999 ************************/
        else if (coincidencias_inicio.find()){
                //guardo el nif sin la letra
                String nif1 = nif.substring(1,longitud_nif);
                //algoritmo
                int nif2 =  Integer.parseInt(nif1);
                int posicion = (nif2 % 23)+1;
                String caracter = caracteres.substring(posicion-1,posicion);
                if (caracter.compareToIgnoreCase(inicio) == 0){
                        return true;
                }else
                setMensajeError(mensaje+".error");
                return false;
        }
        /************** nif 9999999-x ************************/
        else if (coincidencias_fin.find()){
                //guardo el nif sin la letra
                String nif1 = nif.substring(0,longitud_nif-1);
                //algoritmo
                int nif2 =  Integer.parseInt(nif1);
                int posicion = (nif2 % 23)+1;
                String caracter = caracteres.substring(posicion-1,posicion);
                if (caracter.compareToIgnoreCase(fin) == 0){
                        return true;
                }else
                setMensajeError(mensaje+".error");
                return false;
        }
                setMensajeError(mensaje+".error");
                return false;
        }//fin metodo comprueba nif


        /**
        * Método que valida el formato de un CIF y lo formatea rellenandolo con ceros y guion
        * @param cif valor del campo del formulario
        * @param mensaje string que se muestra en caso de error
        * @return true o false, dependiendo de si el campo cumple todos los requisitos o no
        * @throws NumberFormatException
        */
        public boolean format_cif(String cif, String mensaje) throws NumberFormatException{
                String resultado="";
                String cif_virgen = cif.replaceAll("-","").toUpperCase().trim();
                String cif_formateado="";
                int long_cif= cif_virgen.length();
                String inicio = cif_virgen.substring(0,1);
                String fin=cif_virgen.substring(long_cif-1);
                String expresion_izq="[ABCDEFGHPQSabcdefghpqs]";
                String expresion_dcha="[JABCDEFGHIjabcdefghi]";
                Pattern patron_izq = null;
                Pattern patron_dcha = null;
                Matcher coincidencias_inicio = null;
                Matcher coincidencias_fin = null;
                patron_izq = Pattern.compile(expresion_izq);
                patron_dcha = Pattern.compile(expresion_dcha);
                coincidencias_inicio = patron_izq.matcher(inicio);
                coincidencias_fin = patron_dcha.matcher(fin);
                //para comprobar si es nacional
                String numeros_validos = "[0-9]";
                Pattern patron_numeros = Pattern.compile(numeros_validos);
                Matcher coinc_numeros_fin =patron_numeros.matcher(fin);
                Matcher coinc_numeros_inicio =patron_numeros.matcher(inicio);
                boolean pinicio = coincidencias_inicio.find() ;
                boolean pfin = coincidencias_fin.find() ;
                boolean pnuminicio = coinc_numeros_inicio.find() ;
                boolean pnumfin = coinc_numeros_fin.find() ;
                //X-99999999
                if (pinicio == true && pfin == false && pnumfin==true)
                {
                        //calcula el numero de ceros a insertar
                        int num_ceros_relleno = 9 - long_cif;
                        //quito la primera letra
                        cif_formateado = cif_virgen.substring(1);
                        //saco la letra izquerda
                        String primera_letra = cif_virgen.substring(0,1);
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                                cif_formateado = "0" + cif_formateado;
                        cif_formateado = primera_letra.toUpperCase() +"-"+ cif_formateado ;
                        // si llamas al método set del atributo del cif original lo sustituye  por el nuevo cif formateado
                        //Ej: setDocOriginal(cif_formateado);
                        return true;
                }
                // cif 99999999-x
                else if (pnuminicio == true && pinicio == false && pfin == true )
                {
                        //calcula el numero de ceros a insertar
                        int num_ceros_relleno = 9 - long_cif;
                        //saco la ultima letra
                        String ultima_letra = cif_virgen.substring(long_cif-1);
                        //cif sin letra ultima
                        cif_formateado = cif_virgen.substring(0,long_cif-1);
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                                cif_formateado = "0" + cif_formateado;
                        cif_formateado = cif_formateado + "-" + ultima_letra.toUpperCase();
                        // si llamas al método set del atributo del cif original lo sustituye  por el nuevo cif formateado
                        //Ej: setDocOriginal(cif_formateado);
                        return true;
                }
                //X-9999999-Y o X-8888888Y
                else if (pinicio == true && pfin == true)
                {
                        //calcula el numero de ceros a insertar
                        int num_ceros_relleno = 9 - long_cif;
                        //quito la primera letra
                        cif_formateado = cif.substring(1);
                        //saco la letra izquerda
                        String primera_letra = cif_virgen.substring(0,1);
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                                cif_formateado = "0" + cif_formateado;
                        //quito la ultima letra
                        cif_formateado = primera_letra.toUpperCase() +"-"+ cif_formateado ;
                        // si llamas al método set del atributo del cif original lo sustituye  por el nuevo cif formateado
                        //Ej: setDocOriginal(cif_formateado);
                        return true;
                }
                setMensajeError(mensaje+".formato");
                return false;
        }// fin format_cif

        /**
        * Método que comprueba que el cif exista
        * @param pCif cif obtenido en el formulario
        * @param mensaje string con el mensaje que se ha de mostrar en caso de error
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean compruebaCIF(String pCif, String mensaje){
                //Se formatea
                String checkCif= pCif.replaceAll("-","").toUpperCase().trim();
                String cif= checkCif.trim();
                checkCif = pCif.trim(); //tiene guion
                //longitud del cif con guion
                int longCif = checkCif.length();
                //inicializo la longitud del cif limpio
                int longitud_cif =0;
                if (longCif>0){
                        //letras validas
                        String letras_validas = "[ABCDEFGHPQSabcdefghpqs]";
                        //primera letara
                        String inicio = checkCif.substring(0,1);
                        Pattern patron = null;
                        Matcher coincidencias = null;
                        patron = Pattern.compile(letras_validas);//expresion_izq
                        coincidencias = patron.matcher(inicio);
                        if (coincidencias.find()){
                                //saca ultimo caracter
                                String fin = checkCif.substring(longCif-1);
                                coincidencias = patron.matcher(fin);
                                //para comprobar si es nacional
                                String numeros_validos = "[0-9]";
                                Pattern patron_nacional = Pattern.compile(numeros_validos);
                                Matcher coinc_nacional =patron_nacional.matcher(fin);
                                //variable que controla si es nacional o extranjero
                                String control;
                                //compruebo si cif es extranjero
                                if (coincidencias.find()){
                                        control = "extranjero";
                                }
                                //comprueba si cif es nacional
                                else if (coinc_nacional.find()){
                                        control = "nacional";
                                }else{//si no es nacional ni extranjero{
                                        setMensajeError(mensaje+".error");
                                        return false;
                                }//cierra else nac o ext
                                if (control.equals("extranjero") || control.equals("nacional")){
                                        //comprueba si hay dos letras seguidas al principio X-Y99999999
                                        String letra_comprobar;
                                        String letras = "[a-zA-Z]";
                                        Pattern pat = Pattern.compile(letras);
                                        letra_comprobar = checkCif.substring(2,2);
                                        Matcher coinc = pat.matcher(letra_comprobar);
                                        if (coinc.find()){
                                                setMensajeError(mensaje+".error");
                                                return false;
                                        }else{
                                                String numeroCif="";
                                                checkCif = checkCif.replaceAll("-","").toUpperCase().trim();
                                                longitud_cif = checkCif.length();
                                                //si es extranjero (X-9999999X) X9999999X
                                                if (control.equals("extranjero")){
                                                        numeroCif = checkCif.substring(1,longitud_cif-1);
                                                }
                                            //si es nacional
                                                if (control.equals("nacional")){
                                                        numeroCif = checkCif.substring(1,longitud_cif-1);
                                                }
                                                longitud_cif = numeroCif.length();
                                                //ALGORITMO
                                                int total = 0;
                                                int indice = 15;
                                                int resto = 0;
                                                String aux=numeroCif;
                                                //desde
                                                for (int i=longitud_cif; i<= indice-1 ;i++ ){
                                                        aux = "0" + aux;
                                                }
                                                //hacemos un loop mientras el indice sea mayor que 0
                                                while (indice > 0){
                                                        //obtengo el valor entero de la posicion 15
                                                        int cadena = new Integer(aux.substring(indice-1,indice)).intValue();
                                                        int suma = cadena * 2;
                                                    if (suma >= 10){
                                                                resto = suma % 10;
                                                                int cociente = suma / 10;
                                                                suma = cociente + resto;
                                                        }
                                                        if (indice >1){
                                                                int indice2 = indice -1;
                                                                int caracter = (new Integer (aux.substring(indice2-1,indice2))).intValue();
                                                                total = total + suma + caracter;
                                                        }
                                                        indice = indice -2;
                                                }//cierra while
                                                resto = total % 10;
                                                // digito de control
                                                int digito_control = 10 -resto;
                                                //saco el ultimo caracter de digito de control
                                                String digito = (new Integer(digito_control).toString()).substring((new Integer(digito_control).toString()).length() - 1);
                                                //guardo en digito_control el digito pasado a entero
                                                digito_control = new Integer(digito).intValue();
                                                //si el cif es extranjero se saca del listado de letras la de la posicion del digito de control+1. Si coincide con la ultima letra es correcto
                                                if (control.equals("extranjero")){
                                                        String expresion = "JABCDEFGHI";
                                                        String comprueba = expresion.substring(digito_control,digito_control+1);
                                                        if (comprueba.equals(fin))//"letra_ultima"
                                                                return true;

                                                        setMensajeError(mensaje+".error");
                                                        return false;
                                                }
                                                //si el cif es nacional se comprueba que el digito de control coincida con el ultimo digito del cif. Si es asi el cif es correcto
                                                if (control.equals("nacional")){
                                                        //paso letra_ultima  a entero
                                                        int letra = new Integer(fin).intValue();//letra_ultima
                                                        if (digito_control == letra){
                                                                        return true;
                                                        }
                                                        setMensajeError(mensaje+".error");
                                                        return false;
                                                }
                                        }//cierra else
                                }//cierra if control
                                return true;
                        }//cierra if coincidencias find
                        else{
                                setMensajeError(mensaje+".error");
                                return false;
                        }
                }else{
                        setMensajeError(mensaje+".error");
                        return false;
                }

        }// fin comprueba cif

        /**
        * Método que valida los campos del número de la calle
        * @param nombre el valor del campo del formulario
        * @param obligatorio si debe rellenarse o no
        * @param sinNumero si no admite numeros
        * @param cadena string que se muestra en caso de error
        * @param maximo valor máximo de longitud del campo
        * @param minimo valor mínimo de longitud del campo
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaNumeroCalle(String nombre, boolean obligatorio, boolean sinNumero, String cadena, int maximo, int minimo){
                String campo = nombre.trim();
                if (obligatorio){
                        if (campo.length() <= 0){
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0){
                        if (campo.length() < minimo){
                                setMensajeError(cadena + ".minimo");
                                return false;
                        }
                        if (campo.length() > maximo) {
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                }
                return true;
        }// fin método calida numero calle

        /**
        * Método que valida el formato de la CCC
        * @param campo valor del campo del formulario
        * @param cadena string que se muestra en caso de error
        * @param obligatorio si es o no obligatorio el campo
        * @param maximo longitud maxima
        * @param fijo si tiene longitud fija
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaCCC(String campo, boolean obligatorio, String cadena, int maximo, boolean fijo){
                campo = campo.trim();
                if (obligatorio){
                        if (campo.length() <= 0){
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0){
                        if (isNotNumero(campo)){
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                }
                if (fijo){
                        if (campo.length() != maximo){
                                setMensajeError(cadena + ".fijo");
                                return false;
                        }
                }else{
                        if (campo.length() > maximo){
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                }
                return true;
        }// fin método valida CCC

        /**
        * Método que valida el número de la cuenta
        * @param idEntidad entidad
        * @param idSucursal sucursal
        * @param digControl digito de control
        * @param numCuenta número de cuenta
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaCuenta(String idEntidad,String idSucursal,String digControl,String numCuenta){
                /********** COMPROBAR EL DIGITO DE CONTROL ************/
                //concatena entidad y sucursal
                String banco_sucursal = idEntidad.trim() + idSucursal.trim();
                //saca longitud de la concatenacion anterior
                int longitud_banco = banco_sucursal.length();
                String numero;
                int primer_digito;
                int segundo_digito;
                int[] control = new int[11];
                        control[1] = 6;
                        control[2] = 3;
                        control[3] = 7;
                        control[4] = 9;
                        control[5] = 10;
                        control[6] = 5;
                        control[7] = 8;
                        control[8] = 4;
                        control[9] = 2;
                        control[10] = 1;
                // calcula el digito de control para el para banco-sucursal
                int suma = 0;
                for (int i=1;i < longitud_banco+1 ;i++ ){
                         if (i == longitud_banco){
                                //numero = new Integer(banco_sucursal.substring(0,1)).intValue();
                                numero = banco_sucursal.substring(0,1);
                         }else{
                                numero = banco_sucursal.substring(longitud_banco-i,longitud_banco-i+1);

                         }
                        int num = new Integer(numero).intValue();
                        suma = suma + (num * control[i]);
                }
                if (suma % 11 != 0){
                        primer_digito = 11 - (suma % 11);
                }else{
                        primer_digito = 0;
                }
                //calcula el digito de control para el numero de cuenta
                String numero_cuenta =numCuenta.trim();
                suma = 0;
                int longitud_cuenta = numero_cuenta.length();

                for (int i=1;i < longitud_cuenta+1 ;i++ ){
                        if (i == longitud_cuenta){
                                //numero = new Integer(numero_cuenta.substring(0,1)).intValue();
                                numero = numero_cuenta.substring(0,1);
                        }else{
                                //numero =	new Integer(numero_cuenta.substring(longitud_cuenta-i+1,longitud_cuenta-i)).intValue();
                                numero =	numero_cuenta.substring(longitud_cuenta-i,longitud_cuenta-i+1);
                        }
                        int num = new Integer(numero).intValue();
                        suma = suma + (num * control[i]);

                }
                if (suma % 11 != 0){
                        segundo_digito = 11 - (suma % 11);

                }else{
                        segundo_digito = 0;
                }
                //se construyen los digitos de control
                // si alguno de los digitos es 10 se coge solo el 1
                if (primer_digito == 10){
                        primer_digito = 1;
                }
                if (segundo_digito == 10){
                        segundo_digito = 1;
                }

                String digitos_control = new Integer(primer_digito).toString() + new Integer(segundo_digito).toString();

                if (!digitos_control.equals(digControl.trim())){
                        setMensajeError("alta.cuenta.incorrecto");
                        return false;
                }
                return true;
        }// cierra metodo validaCuenta

         /**
         * Método que valida todos los campos de una dirección
         * @param via tipo de via
          * @param calle dirección
         * @param numero número de la calle
         * @param piso piso
         * @param codigoPostal código postal
         * @param poblacion población
         * @param provincia provincia
         * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
         */
        public boolean validaDireccion(String via, String calle, String numero, String piso, String codigoPostal, String poblacion, String provincia){

                /* Llamamos al método que valida el tipo de vía de la dirección */
                if (!validaCampoSelect(via, "alta.tipoVia")){
                        setDefineError("alta.tipoVia");
                        return false;
                }
                /* Llamamos al método que valida la dirección */
                if (!validaCampoTexto(calle, true, true, "alta.direccion", 40, 2)){
                        setDefineError("alta.direccion");
                        return false;
                }
                /* Llamamos al método que valida el número */
                if (!validaNumeroCalle(numero, false, false, "alta.numero", 5, 0)){
                        setDefineError("alta.numero");
                        return false;
                }
                /* Llamamos al método que valida el piso */
                if (!validaNumeroCalle(piso, false, false, "alta.piso", 40, 0)){
                        setDefineError("alta.piso");
                        return false;
                }
                /* Llamamos al método que valida el código postal */
                if (!validaCampoNumeros(codigoPostal, true, "alta.codigoPostal", 5, true)){
                        setDefineError("alta.codigoPostal");
                        return false;
                }
                /* Llamamos al método que valida la población */
                if (!validaCampoTexto(poblacion, true, true, "alta.poblacion", 30, 2)){
                        setDefineError("alta.poblacion");
                        return false;
                }
                /* Llamamos al método que valida la provincia */
                if (!validaCampoSelect(provincia, "alta.provincia")){
                        setDefineError("alta.provincia");
                        return false;
                }


                return true;
        }//Fin del método validaDireccion()

        /**
        *Método que valida el formato de las fechas mm/yyyy
        *@param fecha valor de fecha introducido
        *@param cadena error que envia si no es correcto
        *@param obligatorio indica si el campo es o no obligatorio
        *@return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaFecha_tarjeta(String fecha,String cadena,boolean obligatorio){
                String campo=fecha.trim();
                if (obligatorio)
                {
                        if (campo.length() <= 0)
                        {
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0){
                        if (campo.length() != 7)
                        {
                                setMensajeError(cadena + ".longitud");
                                return false;
                        }
                        //saca de la fecha mes y anho
                        String[] fech1 = campo.split("[/]");
                        //comprueba que el array tenga dos elementos, lo que significaría que el formato es correcto mm/yyyy
                        if (fech1.length < 2){
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                        //extraemos los valores del array
                        int mes = new Integer (fech1[0]).intValue();
                        int anio = new Integer (fech1[1]).intValue();

                        if (mes < 1 || mes > 12){
                                setMensajeError(cadena + ".incorrecta");
                                return false;
                        }
                        //comprueba que el anho este entre 1900 y 2099
                        if (anio < 1900 || anio > 2099){
                                setMensajeError(cadena + ".incorrecta");
                                return false;
                        }

                }
                return true;
        }//Fin del método validaFecha_tarjeta()

        /**
        *Método que valida el formato de las fechas
        *@param fecha valor de fecha introducido
        *@param cadena error que envia si no es correcto
        *@param obligatorio indica si el campo es o no obligatorio
        *@return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaFecha(String fecha, String cadena,boolean obligatorio)
        {
                String campo = fecha.trim();
                if (obligatorio)
                {
                        if (campo.length() <= 0)
                        {
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0)
                {
                        if (campo.length() != 10)
                        {
                                setMensajeError(cadena + ".longitud");
                                return false;
                        }
                        //saca de la fecha dia, mes y anho
                        String[] fech1 = campo.split("[/]");
                        //comprueba que haya introducido el formato dd/mm/yyyy
                        if (fech1.length < 3)
                        {
                                setMensajeError(cadena + ".formato");
                                return false;
                        }
                        int dia = new Integer (fech1[0]).intValue();
                        int mes = new Integer (fech1[1]).intValue();
                        int anio = new Integer (fech1[2]).intValue();
                        //el mes debe estar entre 1 y 12
                        if (mes < 1 || mes > 12)
                        {
                                setMensajeError(cadena + ".incorrecta");
                                return false;
                        }
                        //comprueba que el anho este entre 1900 y 2099
                        if (anio < 1900 || anio > 2099)
                        {
                                setMensajeError(cadena + ".incorrecta");
                                return false;
                        }
                        //comprueba el numero de dias dependiendo del mes
                        if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12)
                        {
                                if (dia <=0 || dia > 31)
                                {
                                        setMensajeError(cadena + ".incorrecta");
                                        return false;
                                }
                        }
                        if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
                        {
                                if (dia <=0 || dia > 30)
                                {
                                        setMensajeError(cadena + ".incorrecta");
                                        return false;
                                }
                        }
                        if (mes == 2)
                        {
                                 if (anio % 4 > 0)
                                 {
                                         if (dia > 28)
                                         {
                                                setMensajeError(cadena + ".incorrecta");
                                                return false;
                                         }
                                 }else if (anio % 100 == 0 && anio % 400 >0)
                                 {
                                         if (dia > 28)
                                         {
                                                setMensajeError(cadena + ".incorrecta");
                                                return false;
                                         }
                                 }else
                                {
                                        if (dia > 29)
                                        {
                                                setMensajeError(cadena + ".incorrecta");
                                                return false;
                                        }
                                 }
                        }
                }
                return true;
        }//Fin del método validaFecha()

        /**
        * método que comprueba tarjetas de crédito (solo contempla las tarjetas American Express, Visa y MasterCard)
        * @param numeroTarjeta numero de la tarjeta que se va a comprobar
        * @param tipo tipo de tarjeta a la que se está haciendo referencia
        * @param cadena mensaje de error
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
        public boolean validaTarjeta(String numeroTarjeta,String tipo,String cadena)
        {
                String tarjeta = numeroTarjeta.trim();
                String tipoTarjeta = "";

                //comprueba que el número no venga a vacío
                if (tarjeta.compareTo("")==0){
                        setMensajeError(cadena + ".vacio");
                        return false;
                }
                //comprueba que el dato sea un número
                if(tarjeta.length()>0){
                        if (isNotNumero(tarjeta)){
                                setMensajeError(cadena+".formato");
                                return false;
                        }
                }

                String numero = tarjeta.substring(0,1);
                int suma =0;

                // comprueba que el tipo de tarjeta seleccionado sea correcto
                if (numero.equals("3"))
                {
                        tipoTarjeta = "American Express";
                }
                if (numero.equals("4"))
                {
                        tipoTarjeta = "Visa";
                }
                if (numero.equals("5"))
                {
                        tipoTarjeta = "MasterCard";
                }
                if (tipo.compareToIgnoreCase(tipoTarjeta) != 0)
                {
                        setMensajeError(cadena + ".tipo");
                        return false;
                }


                /**validación del número de la tarjeta**/
                for(int i=0;i<tarjeta.length();i++){
                        String numPosicion=""+tarjeta.charAt(i);
                        int digit = Integer.parseInt(numPosicion);
                        int numeroPar=0;
                        int numeroImpar=0;

                        if(i%2==0){
                                //multiplico los impares por 2
                                numeroImpar=digit*2;
                                // si el numero es mayor que 9 se le resta 9
                                if (numeroImpar > 9){
                                        numeroImpar = numeroImpar - 9;
                                }

                        // convierto los pares a integer

                        }else{
                        // multiplico los impares por 2
                                numeroPar=digit;
                        }


                        // se suman todos los numeros del nuevo numero generado (nuevo impar + par)
                        suma += numeroImpar+numeroPar;
                }
                // si el numero resultante es multiplo de 10 y menor de 150 es un numero de tarjeta valido

                                int resto = suma%10;
                        if ((resto > 0) || (suma > 150))
                        {
                                setMensajeError(cadena + ".error");
                                return false;
                        }
                return true;

        }

        /**
        * Método que permite validar los campos que, como en el caso del usuario, permiten la introducción de números
        * @param campo campo que se va comprobar
        * @param obligatorio indica si el campo es o no obligatorio
        * @param minimo tamanho mínimo de campo
        * @param maximo tamanho máximo del campo
        * @param cadena mensaje de error asociado
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
        */
         public boolean validaUsuario(String campo, boolean obligatorio, int minimo, int  maximo, String cadena){
                campo=campo.trim();
                if(obligatorio){
                        if(campo.length() <= 0){
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if(campo.length() > 0){
                        if(campo.length() > maximo){
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                        if(campo.length() < minimo){
                                setMensajeError(cadena + ".minimo");
                                return false;
                        }else{
                                patron = Pattern.compile(caracteresInvalidos);
                                matcher = patron.matcher(campo);
                                if (matcher.find()){
                                        setMensajeError(cadena + ".caracter");
                                        return false;
                                }
                                patron = Pattern.compile(caracteresValidosUsuario);
                                matcher = patron.matcher(campo);
                                int encontrados = 0;
                                char[] caracteres = campo.toCharArray();
                                for (int i = 0; i < caracteres.length; i++){
                                        matcher = patron.matcher(Character.toString(caracteres[i]));
                                        if (matcher.find())
                                                encontrados++;
                                }
                                if (encontrados <campo.length()){
                                        setMensajeError(cadena + ".formato");
                                        return false;
                                }
                        }//cierra else
                }
                return true;

    }

    /**
    * Método que compara la fecha introducida para la tarjeta con la fecha actual (unica validación posible sobre fecha)
    * @param fechaIntroducida fecha que ha introducido el usuario
        * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
    */
    public boolean compararFechas(String fechaIntroducida){
            String fechaSistema=null;

                   Timestamp tmstp= new Timestamp(System.currentTimeMillis());
                   SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
                   fechaSistema=sdf.format(tmstp);
                   Date sistema=null;
                   Date caducidad=null;

                   try{
                           sistema = sdf.parse(fechaSistema);
                           caducidad= sdf.parse(fechaIntroducida);
                   }catch (ParseException e){
                   		ClsLogging.writeFileLogWithoutSession("Fecha " + fechaIntroducida + " no presenta el formato esperado (dd/MM/yyyy)");
                   		return false;
                   }

                   if(sistema.compareTo(caducidad)>=0)
                           return false;

                   return true;
     }



    /**
    * Método que devuelve la fecha del sistema
    * @return fecha en formato dd/MM/yyyy
    */
    public String getFechaHoraActual(){
       Timestamp tmstp= new Timestamp(System.currentTimeMillis());
       SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
       return (sdf.format(tmstp));
    }

         /**
    * Método que valida un comentario
        *@param campo valor del campo
        *@param obligatorio indica si el campo es obligatorio o no
        *@param minimo longitud minima del campo
        *@param maximo longitud máxima del campo
        *@param cadena cadena de error
        *@return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
    */
    public boolean validaComentario(String campo, boolean obligatorio, int minimo, int  maximo, String cadena)
        {
                if (obligatorio){
                        if (campo.length() <= 0){
                                setMensajeError(cadena + ".vacio");
                                return false;
                        }
                }
                if (campo.length() > 0)
                {
                        if (campo.length() > maximo) {
                                setMensajeError(cadena + ".maximo");
                                return false;
                        }
                        if (campo.length() < minimo) {
                                setMensajeError(cadena + ".minimo");
                                return false;
                        }
                }
                return true;
    }


}
