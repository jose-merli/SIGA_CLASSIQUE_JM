create or replace package PKG_SIGA_FACTURACION is

    TYPE MATRICE_FACTURA IS RECORD (
        IDFACTURA FAC_FACTURA.IDFACTURA%TYPE,
        IDPERSONA FAC_FACTURA.IDPERSONA%TYPE,
        IDFORMAPAGO FAC_FACTURA.IDFORMAPAGO%TYPE,
        IDCUENTA FAC_FACTURA.IDCUENTA%TYPE,
        IDPERSONADEUDOR FAC_FACTURA.IDPERSONADEUDOR%TYPE,
        IDCUENTADEUDOR FAC_FACTURA.IDCUENTADEUDOR%TYPE,
        OBSERVACIONES FAC_FACTURA.OBSERVACIONES%TYPE,
        CTACLIENTE FAC_FACTURA.CTACLIENTE%TYPE,
        IMPTOTALNETO FAC_FACTURA.IMPTOTALNETO%TYPE,
        IMPTOTALIVA FAC_FACTURA.IMPTOTALIVA%TYPE,
        IMPTOTAL FAC_FACTURA.IMPTOTAL%TYPE,
        IMPTOTALANTICIPADO FAC_FACTURA.IMPTOTALANTICIPADO%TYPE,
        IMPTOTALPORPAGAR FAC_FACTURA.IMPTOTALPORPAGAR%TYPE,
        IDMANDATO FAC_FACTURA.IDMANDATO%TYPE,
        DEUDOR_ID CEN_PERSONA.NIFCIF%TYPE,
        DEUDOR_NOMBRE VARCHAR2(302));
  TYPE TAB_FACTURA IS TABLE OF MATRICE_FACTURA INDEX BY BINARY_INTEGER;

    /* Matriz de Lineas de Facturas */
    TYPE MATRICE_LINEAFACTURA IS RECORD (
        IDFACTURA               FAC_LINEAFACTURA.IDFACTURA%TYPE,
        NUMEROLINEA             FAC_LINEAFACTURA.NUMEROLINEA%TYPE,
        NUMEROORDEN             FAC_LINEAFACTURA.NUMEROORDEN%TYPE,
        DESCRIPCION             FAC_LINEAFACTURA.DESCRIPCION%TYPE,
        CANTIDAD                FAC_LINEAFACTURA.CANTIDAD%TYPE,
        PRECIOUNITARIO          number(14,6),
        IMPORTEANTICIPADO       FAC_LINEAFACTURA.IMPORTEANTICIPADO%TYPE,
        IVA                     FAC_LINEAFACTURA.IVA%TYPE,
        IDTIPOIVA FAC_LINEAFACTURA.IDTIPOIVA%TYPE,
        IDPETICION              PYS_COMPRA.IDPETICION%TYPE,
        IDTIPOPRODUCTO          PYS_COMPRA.IDTIPOPRODUCTO%TYPE,
        IDPRODUCTO              PYS_COMPRA.IDPRODUCTO%TYPE,
        IDPRODUCTOINSTITUCION   PYS_COMPRA.IDPRODUCTOINSTITUCION%TYPE,
        CTAPRODUCTOSERVICIO     FAC_LINEAFACTURA.CTAPRODUCTOSERVICIO%TYPE,
        CTAIVA                  FAC_LINEAFACTURA.CTAIVA%TYPE,
        IDFORMAPAGO             FAC_LINEAFACTURA.IDFORMAPAGO%TYPE);
  TYPE TAB_LINEAFACTURA IS TABLE OF MATRICE_LINEAFACTURA INDEX BY BINARY_INTEGER;

  /* Matriz de Facturas Suscripciones */
  TYPE MATRICE_FACTURACIONSUSCRIPCION IS RECORD
   (IDFACTURA                 FAC_FACTURACIONSUSCRIPCION.IDFACTURA%TYPE,
    NUMEROLINEA               FAC_FACTURACIONSUSCRIPCION.NUMEROLINEA%TYPE,
    IDTIPOSERVICIOS           FAC_FACTURACIONSUSCRIPCION.IDTIPOSERVICIOS%TYPE,
    IDSERVICIO                FAC_FACTURACIONSUSCRIPCION.IDSERVICIO%TYPE,
    IDSERVICIOSINSTITUCION    FAC_FACTURACIONSUSCRIPCION.IDSERVICIOSINSTITUCION%TYPE,
    IDSUSCRIPCION             FAC_FACTURACIONSUSCRIPCION.IDSUSCRIPCION%TYPE,
    IDFACTURACIONSUSCRIPCION  FAC_FACTURACIONSUSCRIPCION.IDFACTURACIONSUSCRIPCION%TYPE,
    DESCRIPCION               FAC_FACTURACIONSUSCRIPCION.DESCRIPCION%TYPE,
    FECHAINICIO               FAC_FACTURACIONSUSCRIPCION.FECHAINICIO%TYPE,
    FECHAFIN                  FAC_FACTURACIONSUSCRIPCION.FECHAFIN%TYPE,
    IDPERSONA FAC_FACTURA.IDPERSONA%TYPE,
    IMPORTE NUMBER(10,2),
    CONT_ANTICIPOS NUMBER);
  TYPE TAB_FACTURACIONSUSCRIPCION IS TABLE OF MATRICE_FACTURACIONSUSCRIPCION INDEX BY BINARY_INTEGER;

    /****************************************************************************************************************/
    /* Nombre: OBTENCIONPOBLACIONCLIENTES */
    /* Descripcion: Obtencion de la poblacion de clientes incluidos en la serie de facturacion */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    FUNCTION OBTENCIONPOBLACIONCLIENTES(P_IDINSTITUCION IN NUMBER, P_IDSERIEFACTURACION IN NUMBER) RETURN PERSONA_TBL;

    /*****************************************************************************************************************************/
    /* Nombre: GENERACIONFACTURACION  */
    /* Descripcion:   Generacion de facturas */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN  Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 01/09/2015 - Autor Jorge Paez Trivino - Cambios encontrados al realizar la documentacion del proceso de facturacion */
    /*****************************************************************************************************************************/
    PROCEDURE GENERACIONFACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDPROGRAMACION IN NUMBER,
        P_IDIOMA IN NUMBER,
        P_IDPETICION IN NUMBER, -- Cuendo tiene dato es porque viene de facturacion rapida
        P_USUMODIFICACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);

    /****************************************************************************************************************/
    /* Nombre: CONFIRMACIONFACTURACION */
    /* Descripcion: Confirmacion de la factura */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN  Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 04/10/2005 - Autor: Yolanda Garcia Espino - Configuracion del numero de factura */
    /* Version: 3.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    PROCEDURE CONFIRMACIONFACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDPROGRAMACION IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);

    /*****************************************************************************************************************************/
    /* Nombre: ELIMINARFACTURACION  */
    /* Descripcion: Eliminacion de una facturacion */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN  Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Modificacion: 01/09/2015 - Autor Jorge Paez Trivino - Cambios encontrados al realizar la documentacion del proceso de facturacion */
    /*****************************************************************************************************************************/
    PROCEDURE ELIMINARFACTURACION   (
        P_IDINSTITUCION IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDPROGRAMACION IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);
END PKG_SIGA_FACTURACION;
/
CREATE OR REPLACE PACKAGE BODY PKG_SIGA_FACTURACION IS

    M_FACTURA TAB_FACTURA;
    IND_FACTURA BINARY_INTEGER;  /* Indicador de la matriz de facturas */
    M_LINEAFACTURA TAB_LINEAFACTURA;
    IND_LINEAFACTURA BINARY_INTEGER;  /* Indicador de la matriz de lineas de facturas */
    M_FACTURACIONSUSCRIPCION TAB_FACTURACIONSUSCRIPCION ;
    IND_FACTURACIONSUSCRIPCION BINARY_INTEGER;  /* Indicador de la matriz de facturas suscripciones */
    V_FECHAINICIOPRODUCTOS FAC_FACTURACIONPROGRAMADA.FECHAINICIOPRODUCTOS%TYPE; /* Fecha inicio del producto */
    V_FECHAFINPRODUCTOS FAC_FACTURACIONPROGRAMADA.FECHAFINPRODUCTOS%TYPE;   /* Fecha fin del producto */
    V_FECHAINICIOSERVICIOS FAC_FACTURACIONPROGRAMADA.FECHAINICIOSERVICIOS%TYPE; /* Fecha inicio del servicio */
    V_FECHAFINSERVICIOS FAC_FACTURACIONPROGRAMADA.FECHAFINSERVICIOS%TYPE;   /* Fecha fin del servicio */
    V_CODRETORNO VARCHAR2(10) := TO_CHAR(0); /* Codigo de error Oracle */
    V_DATOSERROR VARCHAR2(4000) := NULL; /* Mensaje de error en los procedimientos */
    E_ERROR EXCEPTION;

    /****************************************************************************************************************/
    /* Nombre: PROCESOANTICIPOSLETRADO */
    /* Descripcion: Proceso de anticio de letrados de suscripciones */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la Institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER */
    /* P_IDSERVICIO - IN - Identificador del servicio - NUMBER */
    /* P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER */
    /* P_IMPORTE - IN - Importe de la suscripcion - NUMBER */
    /* P_IDFACTURA - IN - Identificador de la factura - VARCHAR2 */
    /* P_NUMEROLINEA - IN OUT - Identificador del numero de la linea de la factura - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion:  - Autor:  */
    /* Version: 2.0 - Fecha Modificacion: 13/01/2015 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    PROCEDURE PROCESOANTICIPOSLETRADO(
        P_IDINSTITUCION IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_IDTIPOSERVICIOS IN NUMBER,
        P_IDSERVICIO IN NUMBER,
        P_IDSERVICIOSINSTITUCION IN NUMBER,
        P_IMPORTE IN NUMBER,
        P_IDFACTURA IN NUMBER,
        P_NUMEROLINEA IN NUMBER,
        P_USUMODIFICACION  IN  NUMBER,
        P_CODRETORNO  OUT VARCHAR2,
        P_DATOSERROR  OUT VARCHAR2) IS

        /* Declaracion de variables */
        V_IMPORTELINEA NUMBER(10,2) := P_IMPORTE;
        V_ImporteAnticipoUsado NUMBER(10,2);
        V_IDLINEA_ANTICIPO PYS_LINEAANTICIPO.IDLINEA%TYPE;


    CURSOR C_ANTICIPOS(PP_IDPERSONA IN NUMBER) IS
        SELECT *
        FROM (
                SELECT A.IDANTICIPO,
                     (
                        A.IMPORTEINICIAL - (
                            SELECT NVL(SUM(L.IMPORTEANTICIPADO),0)
                            FROM PYS_LINEAANTICIPO L
                            WHERE L.IDINSTITUCION = A.IDINSTITUCION
                                AND L.IDPERSONA = A.IDPERSONA
                                AND L.IDANTICIPO = A.IDANTICIPO
                                AND TRUNC(L.FECHAEFECTIVA) < SYSDATE
                        )
                    ) AS IMPORTERESTANTE,
                    FECHA
                FROM PYS_SERVICIOANTICIPO S,
                    PYS_ANTICIPOLETRADO A
                WHERE A.IDINSTITUCION = S.IDINSTITUCION
                    AND A.IDPERSONA = S.IDPERSONA
                    AND A.IDANTICIPO = S.IDANTICIPO
                    AND S.IDINSTITUCION = P_IDINSTITUCION
                    AND S.IDPERSONA = PP_IDPERSONA
                    AND S.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                    AND S.IDSERVICIO = P_IDSERVICIO
                    AND S.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
            )
        WHERE IMPORTERESTANTE > 0
        ORDER BY FECHA;


    BEGIN
        V_DATOSERROR := 'PROCESOANTICIPOSLETRADO: COMIENZA EL PROCESO POR LINEA';

        -- Consulto anticipos de la persona, del servicio y con importe restante
        FOR V_ANTICIPOS IN C_ANTICIPOS(P_IDPERSONA) LOOP

            -- Termina cuando no tenga mas importe la suscripcion que se pueda anticipar
            EXIT WHEN (V_IMPORTELINEA<=0);

            -- Si el anticipo es menor que el importe restante de la suscripcion, se coge todo el anticipo
            IF (V_ANTICIPOS.IMPORTERESTANTE < V_IMPORTELINEA) THEN
                V_ImporteAnticipoUsado := V_ANTICIPOS.IMPORTERESTANTE;
                V_IMPORTELINEA := V_IMPORTELINEA - V_ImporteAnticipoUsado;

            ELSE
                V_ImporteAnticipoUsado := V_IMPORTELINEA;
                V_IMPORTELINEA := 0;
            END IF;

            -- JPT (12-01-2015) - Control de anticipos: Garantiza que se ha usado algo del anticipo
            IF (V_ImporteAnticipoUsado > 0) THEN

                V_DATOSERROR := 'PROCESOANTICIPOSLETRADO: Actualiza FAC_LINEAFACTURA';
                UPDATE FAC_LINEAFACTURA
                SET IMPORTEANTICIPADO = NVL(IMPORTEANTICIPADO, 0) + V_ImporteAnticipoUsado
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDFACTURA = P_IDFACTURA
                    AND NUMEROLINEA = P_NUMEROLINEA;

                V_DATOSERROR := 'PROCESOANTICIPOSLETRADO: Actualiza FAC_FACTURA';

               UPDATE FAC_FACTURA
               SET IMPTOTALANTICIPADO = IMPTOTALANTICIPADO + V_ImporteAnticipoUsado,
                   IMPTOTALPAGADO = IMPTOTALPAGADO + V_ImporteAnticipoUsado,
                   IMPTOTALPORPAGAR = IMPTOTALPORPAGAR - V_ImporteAnticipoUsado
                WHERE IDINSTITUCION = P_IDINSTITUCION
                  AND IDFACTURA = P_IDFACTURA;

                -- CGP (09/10/2017) Insertamos en el histórico de la facturacion INICIO
                 
              INSERT INTO FAC_HISTORICOFACTURA (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO,
  						IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
  						IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO)
  						SELECT IDINSTITUCION, IDFACTURA,SEQ_FAC_HISTORIAL.NEXTVAL,SYSDATE,USUMODIFICACION,3,IDFORMAPAGO,
  						IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
  						IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO,IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO
  						FROM FAC_FACTURA WHERE IDINSTITUCION=P_IDINSTITUCION AND IDFACTURA=P_IDFACTURA;

                --CGP FIN

                -- CREO UNA LINEA EN PYS_LINEAANTICIPO CON LA LINEA DE FACTURA Y EL IMPORTE USADO
                V_DATOSERROR := 'PROCESOANTICIPOSLETRADO: Obtiene una nueva linea de anticipo';
                SELECT NVL(MAX(IDLINEA), 0) + 1
                    INTO V_IDLINEA_ANTICIPO
                FROM PYS_LINEAANTICIPO
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDPERSONA = P_IDPERSONA
                    AND IDANTICIPO = V_ANTICIPOS.IDANTICIPO;

                V_DATOSERROR := 'PROCESOANTICIPOSLETRADO: Inserta PYS_LINEAANTICIPO';
                INSERT INTO PYS_LINEAANTICIPO (IDINSTITUCION, IDPERSONA, IDANTICIPO, IDLINEA, IDFACTURA, NUMEROLINEA, IMPORTEANTICIPADO, LIQUIDACION, FECHAEFECTIVA, USUMODIFICACION, FECHAMODIFICACION)
                VALUES (
                    P_IDINSTITUCION,
                    P_IDPERSONA,
                    V_ANTICIPOS.IDANTICIPO,
                    V_IDLINEA_ANTICIPO,
                    P_IDFACTURA,
                    P_NUMEROLINEA,
                    V_ImporteAnticipoUsado,
                    '0',
                    SYSDATE,
                    P_USUMODIFICACION,
                    SYSDATE);
            END IF;
        END LOOP;

        IF (P_CODRETORNO = TO_CHAR(0) OR P_CODRETORNO IS NULL) THEN
            P_CODRETORNO := TO_CHAR(0);
            P_DATOSERROR := 'FIN CORRECTO';
        END IF;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := V_DATOSERROR || SQLERRM;
    END PROCESOANTICIPOSLETRADO;

    /****************************************************************************************************************/
    /* Nombre: CONSULTADINAMICA */
    /* Descripcion:  Usa DBMS_SQL para consultar la sentencia que se pasa por parametro */
    /* */
    /* P_SENTENCIA - IN - Sentencia que se va a ejecutar en PlSql Dinamico - VARCHAR2(4000) */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 01/07/2010 - Autor: Jorge Torres Acosta - Que coja el valor del porcentaje de iva, no su id */
    /* Version: 3.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    FUNCTION CONSULTADINAMICA(P_SENTENCIA IN VARCHAR2) RETURN PERSONA_TBL AS

        /* Declaracion de variables */
        V_CURSOR INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
        V_DUMMY INTEGER; /* Donde se guarda el valor devuelto de la consulta */
        V_IDINSTITUCION CEN_CLIENTE.IDINSTITUCION%TYPE; /* Identificador de la institucion */
        V_IDPERSONA CEN_CLIENTE.IDPERSONA%TYPE; /* Persona a la que se va a facturar */
        M_PoblacionSerieFacturacion PERSONA_TBL := PERSONA_TBL();

    BEGIN
        /* Se abre el cursor para procesamiento */
        V_DATOSERROR := 'CONSULTADINAMICA: Apertura del cursor para procesamiento';
        V_CURSOR := DBMS_SQL.OPEN_CURSOR;

        /* Se analiza la consulta */
        V_DATOSERROR := 'CONSULTADINAMICA: Analisis de la consulta';
        DBMS_SQL.PARSE(V_CURSOR, P_SENTENCIA, DBMS_SQL.V7);

        /* Define las variables de salida */
        V_DATOSERROR := 'CONSULTADINAMICA: Definicion de variables de salida';
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDPERSONA);

        /* Ejecuta la orden */
        V_DATOSERROR := 'CONSULTADINAMICA: Ejecucion de la orden';
        V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);

        /* Bucle de extraccion de filas */
        V_DATOSERROR := 'CONSULTADINAMICA: Bucle de extraccion de filas';
        LOOP
            /* Extrae las filas y las pone en el bufer, y tambien comprueba la condicion de salida del bucle         */
            V_DATOSERROR := 'CONSULTADINAMICA: Comprobacion de salida del bucle';
            IF DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 THEN
                EXIT;
            END IF;

            /* Recupera las filas del bufer en las variables PL/SQL */
            V_DATOSERROR := 'CONSULTADINAMICA: Recuperacion de las filas del bufer en las variables PL/SQL';
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDPERSONA);

            /* Indica una nueva poblacion */
            M_PoblacionSerieFacturacion.EXTEND;
            M_PoblacionSerieFacturacion(M_PoblacionSerieFacturacion.LAST) := PERSONA_T(V_IDINSTITUCION, V_IDPERSONA);
        END LOOP;

        V_DATOSERROR := 'CONSULTADINAMICA: Cierre del cursor';
        DBMS_SQL.CLOSE_CURSOR(V_CURSOR); /* Se cierra el cursor */

        RETURN M_PoblacionSerieFacturacion;

        EXCEPTION
            WHEN OTHERS THEN
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR); /* Se cierra el cursor */
                RETURN NULL;
  END CONSULTADINAMICA;

    /****************************************************************************************************************/
    /* Nombre: OBTENCIONPOBLACIONCLIENTES */
    /* Descripcion: Obtencion de la poblacion de clientes incluidos en la serie de facturacion */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    FUNCTION OBTENCIONPOBLACIONCLIENTES(P_IDINSTITUCION IN NUMBER, P_IDSERIEFACTURACION IN NUMBER) RETURN PERSONA_TBL AS

        CURSOR C_GRUPOSDINAMICOS IS
            SELECT CG.SENTENCIA
            FROM CEN_GRUPOSCRITERIOS CG,
                FAC_GRUPCRITINCLUIDOSENSERIE FG
            WHERE CG.IDINSTITUCION = FG.IDINSTITUCION_GRUP
                AND CG.IDGRUPOSCRITERIOS = FG.IDGRUPOSCRITERIOS
                AND FG.IDINSTITUCION = P_IDINSTITUCION
                AND FG.IDSERIEFACTURACION = P_IDSERIEFACTURACION;

        V_SENTENCIAGF CEN_GRUPOSCRITERIOS.SENTENCIA%TYPE; /* Sentencia SELECT de grupos fijos */
        V_SENTENCIAGD CEN_GRUPOSCRITERIOS.SENTENCIA%TYPE; /* Sentencia SELECT de grupos dinamicos */
        V_SENTENCIAIND CEN_GRUPOSCRITERIOS.SENTENCIA%TYPE; /* Sentencia SELECT de clientes individuales */
        V_SENTENCIA CEN_GRUPOSCRITERIOS.SENTENCIA%TYPE; /* Sentencia SELECT */
        V_SENTENCIA_AUX CEN_GRUPOSCRITERIOS.SENTENCIA%TYPE;

    BEGIN
        /* Grupos de Clientes Dinamicos */
        V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Sentencia Grupos de Clientes Dinamicos';
        FOR V_GRUPOSDINAMICOS IN C_GRUPOSDINAMICOS LOOP

            V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Sentencia Grupos de Clientes Dinamicos - Tratamiento de la sentencia';
            V_SENTENCIA_AUX := V_GRUPOSDINAMICOS.SENTENCIA;
            V_SENTENCIA_AUX := REPLACE(V_SENTENCIA_AUX, Chr(13) || Chr(10), ' ');
            WHILE (INSTR(V_SENTENCIA_AUX, '  ')>0) LOOP
                V_SENTENCIA_AUX := REPLACE(V_SENTENCIA_AUX, '  ', ' ');
            END LOOP;

            V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Sentencia Grupos de Clientes Dinamicos - Concateno sentencia final';
            IF V_SENTENCIAGD IS NULL THEN
                V_SENTENCIAGD := V_SENTENCIA_AUX;
            ELSE
                V_SENTENCIAGD := V_SENTENCIAGD || ' UNION ' || V_SENTENCIA_AUX;
            END IF;
        END LOOP;

        V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Sentencia Grupos de Clientes Dinamicos - Introduccion de parametros';
        IF V_SENTENCIAGD IS NOT NULL THEN
            V_SENTENCIAGD := REPLACE(V_SENTENCIAGD, '@FECHA@', SYSDATE);
            V_SENTENCIAGD := REPLACE(V_SENTENCIAGD, '%%IDINSTITUCION%%', P_IDINSTITUCION);
        END IF;

        /* Grupos de Clientes Fijos */
        V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Sentencia Grupos de Clientes Fijos';
        V_SENTENCIAGF := 'SELECT CG.IDINSTITUCION,' ||
                                        ' CG.IDPERSONA' ||
                                    ' FROM CEN_GRUPOSCLIENTE_CLIENTE CG,' ||
                                        ' FAC_TIPOCLIINCLUIDOENSERIEFAC FT' ||
                                    ' WHERE CG.IDINSTITUCION = FT.IDINSTITUCION' ||
                                        ' AND CG.IDGRUPO = FT.IDGRUPO' ||
                                        ' AND CG.IDINSTITUCION_GRUPO = FT.IDINSTITUCION_GRUPO' ||
                                        ' AND FT.IDINSTITUCION = ' || P_IDINSTITUCION ||
                                        ' AND FT.IDSERIEFACTURACION = ' || P_IDSERIEFACTURACION;

        /* Seleccion nominal de Clientes */
        V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Sentencia Seleccion nominal de Clientes';
        V_SENTENCIAIND := 'SELECT IDINSTITUCION, IDPERSONA' ||
                                      ' FROM FAC_CLIENINCLUIDOENSERIEFACTUR' ||
                                      ' WHERE IDINSTITUCION = ' || P_IDINSTITUCION||
                                      ' AND IDSERIEFACTURACION = ' || P_IDSERIEFACTURACION;

        V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Creacion de la sentencia que queremos ejecutar ' || V_SENTENCIAGD;
        IF V_SENTENCIAGD IS NULL THEN
            V_SENTENCIA := V_SENTENCIAGF || ' UNION ' || V_SENTENCIAIND;
        ELSE
            V_SENTENCIA := V_SENTENCIAGF || ' UNION ' || V_SENTENCIAGD || ' UNION ' || V_SENTENCIAIND;
        END IF;

        V_DATOSERROR := 'OBTENCIONPOBLACIONCLIENTES: Llamada al procedimiento CONSULTADINAMICA';
        RETURN CONSULTADINAMICA(V_SENTENCIA);

        EXCEPTION
            WHEN OTHERS THEN
                RETURN NULL;
    END OBTENCIONPOBLACIONCLIENTES;

    /***************************************************************************************************************/
    /* Nombre: GENERARPERIODO */
    /* Descripcion: Procedimiento que inserta un periodo de un servicio */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la Institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER */
    /* P_IDSERVICIO - IN - Identificador del servicio - NUMBER */
    /* P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER */
    /* P_IDSUSCRIPCION - IN - Identificador de la suscripcion - NUMBER */
    /* P_IDFACTURACIONSUSCRIPCION - IN - Identificador la facturacion suscripcion - NUMBER */
    /* P_FECHAINICIALPERIODO - IN - Fecha inicial del periodo - DATE */
    /* P_FECHAFINALPERIODO - IN - Fecha final del periodo - DATE */
    /* P_VALOR - IN - Valor del servicio en ese momento - NUMBER */
    /* P_PERIODO - IN - Periodo del servicio - NUMBER */
    /* P_IDFACTURA - IN - Identificador de la factura - VARCHAR2 */
    /* P_NUMEROLINEA - IN - Identificador del numero de la linea de la factura - NUMBER */
    /* P_ORDEN - IN - Orden del servicio en la compra - NUMBER */
    /* P_DESCRIPCION - IN - Descripcion del servicio - VARCHAR2 */
    /* P_DESCRIPCION_PRECIO - IN - Descripcion del precio - VARCHAR2 */
    /* P_CANTIDAD - IN - Cantidad de suscripciones del servicio - NUMBER */
    /* P_VALOR_TIPOIVA - IN - Porcentaje de iva del servicio - NUMBER */
    /* P_IDTIPOIVA - IN - Identificador del tipo de iva del servicio - NUMBER */
    /* P_CTACONTABLE - IN - Cuenta contable del servicio - VARCHAR2(20) */
    /* P_CTATIPOIVA - IN - Cuenta del iva del servicio - VARCHAR2(10) */
    /* P_IDFORMAPAGO_LINEA - IN - Identificador de la forma de pago de la linea - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    PROCEDURE GENERARPERIODO (
        P_IDINSTITUCION IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_IDTIPOSERVICIOS IN NUMBER,
        P_IDSERVICIO IN NUMBER,
        P_IDSERVICIOSINSTITUCION IN NUMBER,
        P_IDSUSCRIPCION IN NUMBER,
        P_IDFACTURACIONSUSCRIPCION IN NUMBER,
        P_FECHAINICIALPERIODO IN DATE,
        P_FECHAFINALPERIODO IN DATE,
        P_VALOR IN NUMBER,
        P_PERIODO IN NUMBER,
        P_IDFACTURA IN VARCHAR2,
        P_NUMEROLINEA IN NUMBER,
        P_ORDEN IN NUMBER,
        P_DESCRIPCION IN VARCHAR2,
        P_DESCRIPCION_PRECIO IN VARCHAR2,
        P_CANTIDAD IN NUMBER,
        P_VALOR_TIPOIVA IN NUMBER,
        P_IDTIPOIVA IN NUMBER,
        P_CTACONTABLE IN VARCHAR2,
        P_CTATIPOIVA IN VARCHAR2,
        P_IDFORMAPAGO_LINEA IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        /* Declaracion de variables */
        V_MESINICIAL VARCHAR2(2); /* Mes de la fecha inicial del periodo */
        V_ANIOINICIAL VARCHAR2(4); /* Anio de la fecha inicial del periodo */
        V_MESFINAL VARCHAR2(2); /* Mes de la fecha final del periodo */
        V_ANIOFINAL VARCHAR2(4); /* Anio de la fecha final del periodo */
        V_IMPORTEUNITARIO NUMBER(14,6); /* Importe unitario */
        v_ContadorFacturas Number;

    BEGIN
        V_DATOSERROR := 'GENERARPERIODO: Calculo importe unitario';
        -- Compruebo si el periodo empieza el primer dia del mes y acaba el ultimo dia de mes
        IF (TO_CHAR(P_FECHAINICIALPERIODO, 'DD') = '01' AND LAST_DAY(P_FECHAFINALPERIODO) = P_FECHAFINALPERIODO) THEN

            V_MESINICIAL := TO_CHAR(P_FECHAINICIALPERIODO, 'MM');
            V_ANIOINICIAL := TO_CHAR(P_FECHAINICIALPERIODO, 'YYYY');
            V_MESFINAL := TO_CHAR(P_FECHAFINALPERIODO, 'MM');
            V_ANIOFINAL := TO_CHAR(P_FECHAFINALPERIODO, 'YYYY');

            V_IMPORTEUNITARIO := ((12 * V_ANIOFINAL) + V_MESFINAL - (12 * V_ANIOINICIAL) - V_MESINICIAL + 1) * P_VALOR / P_PERIODO;

            -- Nos aseguramos que el calculo del precio unitario nunca supera el precio asignado al servicio
            IF (V_IMPORTEUNITARIO > P_VALOR) THEN
                V_IMPORTEUNITARIO := P_VALOR;
            END IF;

        ELSE
            V_IMPORTEUNITARIO := (TRUNC(P_FECHAFINALPERIODO) - TRUNC(P_FECHAINICIALPERIODO) + 1) * P_VALOR / F_SIGA_NDIAS_PERIODO(P_FECHAINICIALPERIODO, P_PERIODO);

            -- Nos aseguramos que el calculo del precio unitario nunca supera el precio asignado al servicio
            IF (V_IMPORTEUNITARIO > P_VALOR) THEN
                V_IMPORTEUNITARIO := P_VALOR;
            END IF;
        END IF;

        V_DATOSERROR := 'GENERARPERIODO: Control de existencia de factura del periodo del servicio '||P_IDFACTURA||' y la linea '||TO_CHAR(P_NUMEROLINEA);
        Select Count(*)
          Into v_ContadorFacturas
          From Fac_Facturacionsuscripcion
         Where Idinstitucion = P_IDINSTITUCION
           And Idtiposervicios = p_Idtiposervicios
           And Idservicio = p_Idservicio
           And Idserviciosinstitucion = p_Idserviciosinstitucion
           And Idsuscripcion = p_Idsuscripcion
           And Fechainicio = p_Fechainicialperiodo;

        If (v_ContadorFacturas = 0) Then
          V_DATOSERROR := 'GENERARPERIODO: Insercion en la matriz de Lineas de Facturas de la factura '||P_IDFACTURA||' y la linea '||TO_CHAR(P_NUMEROLINEA);
          IND_LINEAFACTURA := IND_LINEAFACTURA + 1;
          M_LINEAFACTURA(IND_LINEAFACTURA).IDFACTURA := P_IDFACTURA;
          M_LINEAFACTURA(IND_LINEAFACTURA).NUMEROLINEA := P_NUMEROLINEA;
          M_LINEAFACTURA(IND_LINEAFACTURA).NUMEROORDEN := P_ORDEN;

          IF P_DESCRIPCION IS NOT NULL THEN
              IF P_DESCRIPCION_PRECIO IS NOT NULL THEN
                  M_LINEAFACTURA(IND_LINEAFACTURA).DESCRIPCION := P_DESCRIPCION ||' '|| P_DESCRIPCION_PRECIO ||' ('||TO_CHAR(P_FECHAINICIALPERIODO, 'DD/MM/YY')||'-'||TO_CHAR(P_FECHAFINALPERIODO, 'DD/MM/YY') || ')';
              ELSE
                  M_LINEAFACTURA(IND_LINEAFACTURA).DESCRIPCION := P_DESCRIPCION ||' ('||TO_CHAR(P_FECHAINICIALPERIODO, 'DD/MM/YY')||'-'||TO_CHAR(P_FECHAFINALPERIODO, 'DD/MM/YY') || ')';
              END IF;
          ELSE
              M_LINEAFACTURA(IND_LINEAFACTURA).DESCRIPCION := NULL;
          END IF;

          M_LINEAFACTURA(IND_LINEAFACTURA).CANTIDAD := P_CANTIDAD;
          M_LINEAFACTURA(IND_LINEAFACTURA).IDFORMAPAGO := P_IDFORMAPAGO_LINEA;
          M_LINEAFACTURA(IND_LINEAFACTURA).PRECIOUNITARIO := TO_NUMBER(TO_CHAR(V_IMPORTEUNITARIO, '99999990D000000'));
          M_LINEAFACTURA(IND_LINEAFACTURA).IMPORTEANTICIPADO:=0;
          M_LINEAFACTURA(IND_LINEAFACTURA).IVA := TO_NUMBER(TO_CHAR(P_VALOR_TIPOIVA, '990D00'));
          M_LINEAFACTURA(IND_LINEAFACTURA).IDTIPOIVA := P_IDTIPOIVA;
          M_LINEAFACTURA(IND_LINEAFACTURA).IDPETICION := NULL;
          M_LINEAFACTURA(IND_LINEAFACTURA).IDTIPOPRODUCTO := NULL;
          M_LINEAFACTURA(IND_LINEAFACTURA).IDPRODUCTO := NULL;
          M_LINEAFACTURA(IND_LINEAFACTURA).IDPRODUCTOINSTITUCION := NULL;
          M_LINEAFACTURA(IND_LINEAFACTURA).CTAPRODUCTOSERVICIO := P_CTACONTABLE;
          M_LINEAFACTURA(IND_LINEAFACTURA).CTAIVA := F_SIGA_GETPARAMETRO('FAC', 'CONTABILIDAD_IVA', P_IDINSTITUCION) || P_CTATIPOIVA;

          /*********************** MATRIZ DE FACTURAS SUSCRIPCION ***************************/

          -- Insertamos los datos extraidos en la matriz de Facturas Suscripciones.
          V_DATOSERROR := 'GENERARPERIODO: Insercion en la matriz de Facturacion Suscripcion de la factura '||P_IDFACTURA||' y la linea '||TO_CHAR(P_NUMEROLINEA);
          IND_FACTURACIONSUSCRIPCION := IND_FACTURACIONSUSCRIPCION + 1;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDFACTURA := P_IDFACTURA;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).NUMEROLINEA := P_NUMEROLINEA;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDTIPOSERVICIOS := P_IDTIPOSERVICIOS;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDSERVICIO := P_IDSERVICIO;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDSERVICIOSINSTITUCION := P_IDSERVICIOSINSTITUCION;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDSUSCRIPCION := P_IDSUSCRIPCION;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDFACTURACIONSUSCRIPCION := P_IDFACTURACIONSUSCRIPCION;

          IF P_DESCRIPCION IS NOT NULL THEN
              M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).DESCRIPCION := P_DESCRIPCION ||' desde '||TO_CHAR(P_FECHAINICIALPERIODO, 'DD/MM/YYYY')||' hasta '||TO_CHAR(P_FECHAFINALPERIODO, 'DD/MM/YYYY');
          ELSE
              M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).DESCRIPCION := NULL;
          END IF;

          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).FECHAINICIO := P_FECHAINICIALPERIODO;
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).FECHAFIN := P_FECHAFINALPERIODO;

          -- JPT (12-01-2015) - Control de anticipos: Busco que esa persona tenga anticipos del servicio (el calculo del importe se hace despues)
          M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDPERSONA := P_IDPERSONA;
           M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IMPORTE := ROUND (M_LINEAFACTURA(IND_LINEAFACTURA).CANTIDAD * M_LINEAFACTURA(IND_LINEAFACTURA).PRECIOUNITARIO * (1 + (M_LINEAFACTURA(IND_LINEAFACTURA).IVA / 100)), 2);

          SELECT COUNT(*)
              INTO M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).CONT_ANTICIPOS
          FROM PYS_SERVICIOANTICIPO
          WHERE IDINSTITUCION = P_IDINSTITUCION
              AND IDPERSONA = P_IDPERSONA
              AND IDTIPOSERVICIOS = M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDTIPOSERVICIOS
              AND IDSERVICIO = M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDSERVICIO
              AND IDSERVICIOSINSTITUCION = M_FACTURACIONSUSCRIPCION(IND_FACTURACIONSUSCRIPCION).IDSERVICIOSINSTITUCION;
        End If;

        P_CODRETORNO := TO_CHAR(0);
        P_DATOSERROR := NULL;

    EXCEPTION
        WHEN OTHERS THEN
            P_CODRETORNO := TO_CHAR(SQLCODE);
            P_DATOSERROR := V_DATOSERROR;
    END GENERARPERIODO;

    /***************************************************************************************************************/
    /* Nombre: INCLUIRPERIODO */
    /* Descripcion: Acepta los periodos de servicios que van a ser facturables */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la Institucion - NUMBER */
    /* P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER */
    /* P_IDSERVICIO - IN - Identificador del servicio - NUMBER */
    /* P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER */
    /* P_IDSUSCRIPCION - IN - Identificador de la suscripcion - NUMBER */
    /* P_DESCRIPCION - IN - Descripcion del servicio - VARCHAR2 */
    /* P_CANTIDAD - IN - Cantidad de suscripciones del servicio - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_FECHAINICIALPERIODO - OUT - Fecha inicial del periodo - DATE */
    /* P_FECHAFINALPERIODO - OUT - Fecha final del periodo - DATE */
    /* P_IDFACTURA - IN - Identificador de la factura - VARCHAR2 */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_ORDEN - IN - Orden del servicio en la compra - NUMBER */
    /* P_IDFORMAPAGO_LINEA - IN - Identificador de la forma de pago de la linea - NUMBER */
    /* P_NUMEROLINEA - IN OUT - Identificador del numero de la linea de la factura - NUMBER */
    /* P_PRIMERALINEAFACTURA - IN OUT - Estamos insertando la primera linea de la factura - VARCHAR2(1) */
    /* P_IDFACTURACIONSUSCRIPCION - IN OUT - Identificador la facturacion suscripcion - NUMBER */
    /* P_FACTURACIONPROPORCIONAL - IN OUT - Indica si es una facturacion proporcional */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 19/06/2013 - Autor: Jorge Torres Acosta - Cambio para tener varios tramos de periodos */
    /* Version: 3.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /* Version: 4.0 - Fecha Modificacion: 01/09/2015 - Autor Jorge Paez Trivino - Cambios encontrados al realizar la documentacion del proceso de facturacion */
    /*****************************************************************************************************************************/

    PROCEDURE INCLUIRPERIODO (
        P_IDINSTITUCION IN NUMBER,
        P_IDTIPOSERVICIOS IN NUMBER,
        P_IDSERVICIO IN NUMBER,
        P_IDSERVICIOSINSTITUCION IN NUMBER,
        P_IDSUSCRIPCION IN NUMBER,
        P_DESCRIPCION IN VARCHAR2,
        P_CANTIDAD IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHAINICIALPERIODO IN DATE,
        P_FECHAFINALPERIODO IN DATE,
        P_IDFACTURA IN VARCHAR2,
        P_IDIOMA IN NUMBER,
        P_ORDEN IN NUMBER,
        P_IDFORMAPAGO_LINEA IN NUMBER,
        P_NUMEROLINEA IN OUT NUMBER,
        P_PRIMERALINEAFACTURA IN OUT VARCHAR2,
        P_IDFACTURACIONSUSCRIPCION IN OUT NUMBER,
        P_FACTURACIONPROPORCIONAL IN OUT VARCHAR2,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2)
    IS

    /* Declaracion de variables */
    V_IDPERIODICIDAD PYS_PRECIOSSERVICIOS.IDPERIODICIDAD%TYPE := 0; /* Identificador de la periodicidad */
    V_IDPRECIOSSERVICIOS PYS_PRECIOSSERVICIOS.IDPRECIOSSERVICIOS%TYPE := 0; /* Identificador del precio del servicio */
    V_VALORINICIO PYS_PRECIOSSERVICIOS.VALOR%TYPE := 0; /* Precio minimo del servicio */
    V_PERIODOINICIO PYS_PERIODICIDAD.PERIODOSMES%TYPE; /* Periodo */
    V_VALORFIN PYS_PRECIOSSERVICIOS.VALOR%TYPE := 0; /* Precio minimo del servicio */
    V_PERIODOFIN PYS_PERIODICIDAD.PERIODOSMES%TYPE; /* Periodo */
    V_VALOR_TIPOIVA_INICIO PYS_TIPOIVA.VALOR%TYPE := 0; /* Porcentaje IVA */
    V_VALOR_TIPOIVA_FIN PYS_TIPOIVA.VALOR%TYPE := 0; /* Porcentaje IVA */
    V_VALOR_TIPOIVA PYS_TIPOIVA.VALOR%TYPE := 0; /* Porcentaje IVA */
    V_DESCRIPCION PYS_PERIODICIDAD.DESCRIPCION%TYPE; /* Descripcion de la periodicidad */
    V_DESCRIPCIONPRECIOINICIO PYS_PRECIOSSERVICIOS.DESCRIPCION%TYPE; /* Descripcion del precio */
    V_DESCRIPCIONPRECIOFIN PYS_PRECIOSSERVICIOS.DESCRIPCION%TYPE; /* Descripcion del precio */
    V_DESCRIPCIONPRECIO PYS_PRECIOSSERVICIOS.DESCRIPCION%TYPE; /* Descripcion del precio */
    V_FECHAINICIO DATE; /* Fecha inicial del periodo : no lo necesitamos */
    V_FECHAFIN DATE; /* Fecha final del periodo : no lo necesitamos */
    V_FECHA DATE; /* Fecha en la que cambia el criterio */
    V_FECHAINICIALPERIODO DATE; /* Fecha inicial del periodo */
    V_FECHAFINALPERIODO DATE; /* Fecha final del periodo */
    V_INICIOFINALPONDERADO PYS_SERVICIOSINSTITUCION.INICIOFINALPONDERADO%TYPE; /* Inicio Final Ponderado */
    V_VALOR PYS_PRECIOSSERVICIOS.VALOR%TYPE := 0; /* Precio minimo del servicio */
    V_PERIODO PYS_PERIODICIDAD.PERIODOSMES%TYPE; /* Periodo */
    V_CTACONTABLE FAC_LINEAFACTURA.CTAPRODUCTOSERVICIO%TYPE;
    V_CTATIPOIVA FAC_LINEAFACTURA.CTAIVA%TYPE;
    V_IDTIPOIVA PYS_TIPOIVA.IDTIPOIVA%TYPE;

    BEGIN
        V_DATOSERROR := 'INCLUIRPERIODO: Obtencion de la linea de factura';
        IF P_PRIMERALINEAFACTURA = 'S' THEN
            SELECT NVL(MAX(NUMEROLINEA), 0) + 1
                INTO P_NUMEROLINEA
            FROM FAC_LINEAFACTURA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDFACTURA = P_IDFACTURA;

            P_PRIMERALINEAFACTURA := 'N';
        ELSE
            P_NUMEROLINEA := P_NUMEROLINEA + 1;
        END IF;

        -- Vamos a comprobar si el precio a aplicar a ese cliente es el mismo al principio de ese periodo que al final

        -- Obtenemos el precio del servicio correspondiente a la fecha inicial del periodo
        V_DATOSERROR := 'INCLUIRPERIODO(FechaInicialPeriodo): Llamada al procedimiento PROC_SIGA_CALCULARPERIODO';
        PROC_SIGA_CALCULARPERIODO(
            P_IDINSTITUCION,
            P_IDTIPOSERVICIOS,
            P_IDSERVICIO,
            P_IDSERVICIOSINSTITUCION,
            P_IDPERSONA,
            P_FECHAINICIALPERIODO,
            'N',
            P_IDIOMA,
            V_IDPERIODICIDAD,
            V_IDPRECIOSSERVICIOS,
            V_VALORINICIO,
            V_PERIODOINICIO,
            V_VALOR_TIPOIVA_INICIO,
            V_DESCRIPCION,
            V_FECHAINICIO,
            V_FECHAFIN,
            V_DESCRIPCIONPRECIOINICIO,
            V_CODRETORNO,
            V_DATOSERROR);
        IF V_CODRETORNO <> TO_CHAR(0) THEN
            RAISE E_ERROR;
        END IF;

        -- Obtenemos el precio del servicio correspondiente a la fecha final del periodo
        V_DATOSERROR := 'INCLUIRPERIODO(FechaFinalPeriodo): Llamada al procedimiento PROC_SIGA_CALCULARPERIODO';
        PROC_SIGA_CALCULARPERIODO(
            P_IDINSTITUCION,
            P_IDTIPOSERVICIOS,
            P_IDSERVICIO,
            P_IDSERVICIOSINSTITUCION,
            P_IDPERSONA,
            P_FECHAFINALPERIODO,
            'N',
            P_IDIOMA,
            V_IDPERIODICIDAD,
            V_IDPRECIOSSERVICIOS,
            V_VALORFIN,
            V_PERIODOFIN,
            V_VALOR_TIPOIVA_FIN,
            V_DESCRIPCION,
            V_FECHAINICIO,
            V_FECHAFIN,
            V_DESCRIPCIONPRECIOFIN,
            V_CODRETORNO,
            V_DATOSERROR);
        IF V_CODRETORNO <> TO_CHAR(0) THEN
            RAISE E_ERROR;
        END IF;

        V_DATOSERROR := 'INCLUIRPERIODO: Obtencion del IVA del servicio';
        SELECT S.INICIOFINALPONDERADO, S.CUENTACONTABLE, T.SUBCTATIPO, T.IDTIPOIVA
            INTO V_INICIOFINALPONDERADO, V_CTACONTABLE, V_CTATIPOIVA, V_IDTIPOIVA
        FROM PYS_SERVICIOSINSTITUCION S, PYS_TIPOIVA T
        WHERE S.IDTIPOIVA = T.IDTIPOIVA
            AND S.IDINSTITUCION = P_IDINSTITUCION
            AND S.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
            AND S.IDSERVICIO = P_IDSERVICIO
            AND S.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION;

        V_DATOSERROR := 'INCLUIRPERIODO: Asigno datos a variables segun inicioFinalPonderado del servicio';
        IF V_INICIOFINALPONDERADO = 'I' THEN -- Si es I vamos a considerar un periodo con el precio de la fecha inicial del periodo.
            V_VALOR := V_VALORINICIO;
            V_PERIODO := V_PERIODOINICIO;
            V_DESCRIPCIONPRECIO := V_DESCRIPCIONPRECIOINICIO;
            V_VALOR_TIPOIVA := V_VALOR_TIPOIVA_INICIO;

        ELSIF V_INICIOFINALPONDERADO = 'F' THEN -- Si es F vamos a considerar un periodo con el precio de la fecha final del periodo.
            V_VALOR := V_VALORFIN;
            V_PERIODO := V_PERIODOFIN;
            V_DESCRIPCIONPRECIO := V_DESCRIPCIONPRECIOFIN;
            V_VALOR_TIPOIVA := V_VALOR_TIPOIVA_FIN;

        ELSIF V_INICIOFINALPONDERADO = 'P' THEN -- Si es P vamos a considerar varios periodos con sus correspondientes precios
            V_VALOR := V_VALORINICIO;
            V_PERIODO := V_PERIODOINICIO;
            V_DESCRIPCIONPRECIO := V_DESCRIPCIONPRECIOINICIO;
            V_VALOR_TIPOIVA := V_VALOR_TIPOIVA_INICIO;
        END IF;

        V_DATOSERROR := 'INCLUIRPERIODO: Compruebo si es de tipo inicio o fin el servicio';
        IF (V_INICIOFINALPONDERADO = 'I' OR V_INICIOFINALPONDERADO = 'F') THEN

            V_DATOSERROR := 'INCLUIRPERIODO: Llamada al procedimiento GENERARPERIODO';
            GENERARPERIODO (
                P_IDINSTITUCION,
                P_IDPERSONA,
                P_IDTIPOSERVICIOS,
                P_IDSERVICIO,
                P_IDSERVICIOSINSTITUCION,
                P_IDSUSCRIPCION,
                P_IDFACTURACIONSUSCRIPCION,
                P_FECHAINICIALPERIODO,
                P_FECHAFINALPERIODO,
                V_VALOR,
                V_PERIODO,
                P_IDFACTURA,
                P_NUMEROLINEA,
                P_ORDEN,
                P_DESCRIPCION,
                V_DESCRIPCIONPRECIO,
                P_CANTIDAD,
                V_VALOR_TIPOIVA,
                V_IDTIPOIVA,
                V_CTACONTABLE,
                V_CTATIPOIVA,
                P_IDFORMAPAGO_LINEA,
                V_CODRETORNO,
                V_DATOSERROR);
            IF V_CODRETORNO <> TO_CHAR(0) THEN
                RAISE E_ERROR;
            END IF;

        ELSE -- INICIOFINALPONDERADO = 'P', tenemos que recorrer el periodo para ir sabiendo los diferentes periodos.
            V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Inicializacion de la fecha en la que cambia el periodo';
            V_FECHA := P_FECHAINICIALPERIODO;
            V_FECHAINICIALPERIODO := P_FECHAINICIALPERIODO;
            V_FECHAFINALPERIODO := P_FECHAFINALPERIODO;

            IF (V_FECHA < V_FECHAFINALPERIODO) THEN

                V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Bucle para recorrer todos los dias del periodo';
                LOOP

                    V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Actualizacion de la fecha para comprobar el siguiente dia';
                    V_FECHA := V_FECHA + 1;

                    -- Obtenemos el precio del servicio correspondiente a la fecha del periodo
                    V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Llamada al procedimiento PROC_SIGA_CALCULARPERIODO';
                    PROC_SIGA_CALCULARPERIODO(
                        P_IDINSTITUCION,
                        P_IDTIPOSERVICIOS,
                        P_IDSERVICIO,
                        P_IDSERVICIOSINSTITUCION,
                        P_IDPERSONA,
                        V_FECHA,
                        'N',
                        P_IDIOMA,
                        V_IDPERIODICIDAD,
                        V_IDPRECIOSSERVICIOS,
                        V_VALORFIN,
                        V_PERIODOFIN,
                        V_VALOR_TIPOIVA_FIN,
                        V_DESCRIPCION,
                        V_FECHAINICIO,
                        V_FECHAFIN,
                        V_DESCRIPCIONPRECIOFIN,
                        V_CODRETORNO,
                        V_DATOSERROR);
                    IF V_CODRETORNO <> TO_CHAR(0) THEN
                        RAISE E_ERROR;
                    END IF;

                    V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Compruebo si ha cambiado algun dato relevante del servicio';
                    IF (V_VALOR <> V_VALORFIN
                        OR V_PERIODO <> V_PERIODOFIN
                        OR V_VALOR_TIPOIVA <> V_VALOR_TIPOIVA_FIN) THEN

                        V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Actualizacion de la fecha final del periodo';
                        V_FECHAFINALPERIODO := V_FECHA - 1;

                        V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Llamada al procedimiento GENERARPERIODO';
                        GENERARPERIODO (
                            P_IDINSTITUCION,
                            P_IDPERSONA,
                            P_IDTIPOSERVICIOS,
                            P_IDSERVICIO,
                            P_IDSERVICIOSINSTITUCION,
                            P_IDSUSCRIPCION,
                            P_IDFACTURACIONSUSCRIPCION,
                            V_FECHAINICIALPERIODO,
                            V_FECHAFINALPERIODO,
                            V_VALOR,
                            V_PERIODO,
                            P_IDFACTURA,
                            P_NUMEROLINEA,
                            P_ORDEN,
                            P_DESCRIPCION,
                            V_DESCRIPCIONPRECIO,
                            P_CANTIDAD,
                            V_VALOR_TIPOIVA,
                            V_IDTIPOIVA,
                            V_CTACONTABLE,
                            V_CTATIPOIVA,
                            P_IDFORMAPAGO_LINEA,
                            V_CODRETORNO,
                            V_DATOSERROR);
                        IF V_CODRETORNO <> TO_CHAR(0) THEN
                            RAISE E_ERROR;
                        END IF;

                        V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Asigno variables para el siguiente periodo';
                        V_VALOR := V_VALORFIN;
                        V_PERIODO := V_PERIODOFIN;
                        V_DESCRIPCIONPRECIO := V_DESCRIPCIONPRECIOFIN;
                        V_VALOR_TIPOIVA := V_VALOR_TIPOIVA_FIN;
                        V_FECHAINICIALPERIODO := V_FECHA;
                        IF (P_FACTURACIONPROPORCIONAL='1') THEN
                            V_FECHAFINALPERIODO := P_FECHAFINALPERIODO;
                        ELSE
                            V_FECHAFINALPERIODO := V_FECHAFIN;
                        END IF;
                        P_NUMEROLINEA := P_NUMEROLINEA + 1;
                        P_IDFACTURACIONSUSCRIPCION := P_IDFACTURACIONSUSCRIPCION + 1;
                    END IF;

                    V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Condicion de salida del bucle para saber cuando ha terminado el periodo';
                    EXIT WHEN V_FECHA = V_FECHAFINALPERIODO;
                END LOOP;
            END IF;

            -- Llamamos al procedimiento final que inserta el periodo final del servicio
            V_DATOSERROR := 'INCLUIRPERIODO(Ponderado): Llamada al procedimiento GENERARPERIODO';
            GENERARPERIODO (
                P_IDINSTITUCION,
                P_IDPERSONA,
                P_IDTIPOSERVICIOS,
                P_IDSERVICIO,
                P_IDSERVICIOSINSTITUCION,
                P_IDSUSCRIPCION,
                P_IDFACTURACIONSUSCRIPCION,
                V_FECHAINICIALPERIODO,
                V_FECHAFINALPERIODO,
                V_VALOR,
                V_PERIODO,
                P_IDFACTURA,
                P_NUMEROLINEA,
                P_ORDEN,
                P_DESCRIPCION,
                V_DESCRIPCIONPRECIO,
                P_CANTIDAD,
                V_VALOR_TIPOIVA,
                V_IDTIPOIVA,
                V_CTACONTABLE,
                V_CTATIPOIVA,
                P_IDFORMAPAGO_LINEA,
                V_CODRETORNO,
                V_DATOSERROR);
            IF V_CODRETORNO <> TO_CHAR(0) THEN
                RAISE E_ERROR;
            END IF;
        END IF;

        V_DATOSERROR := 'INCLUIRPERIODO: Actualizacion de los parametros de salida';
        P_CODRETORNO := TO_CHAR(0);
        P_DATOSERROR := NULL;

        EXCEPTION
            WHEN E_ERROR THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;

            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := V_DATOSERROR;
    END INCLUIRPERIODO;

    /****************************************************************************************************************/
    /* Nombre: CARGATABLASMEMORIA */
    /* Descripcion: Carga en las tablas de memoria anteriormente definidas */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la Institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de Facturacion - NUMBER */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_IDPETICION - IN - Identificador de la peticion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 09/01/2008 - Autor: Pilar Duran - Se ha anadido como parametro el idPeticion que podra ser nulo o no. */
    /* Version: 3.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /* Version: 4.0 - Fecha Modificacion: 01/09/2015 - Autor Jorge Paez Trivino - Cambios encontrados al realizar la documentacion del proceso de facturacion */
    /*****************************************************************************************************************************/
    PROCEDURE CARGATABLASMEMORIA(
        P_IDINSTITUCION IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDIOMA IN NUMBER,
        P_IDPETICION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        /* Declaracion de constantes */
        METALICOOEFECTIVO CONSTANT NUMBER(2) := 30;
        DOMICILIACIONBANCARIA CONSTANT NUMBER(2) := 20;

        /* Declaracion de variables */
        V_PRIMERAFACTURA VARCHAR2(1) := 'S'; /* Estamos insertando la primera factura */
        V_PRIMERALINEAFACTURA VARCHAR2(1) := 'S'; /* Estamos insertando la primera linea de la factura */
        V_IDCUENTA PYS_PRODUCTOSSOLICITADOS.IDCUENTA%TYPE; /* Numero de la cuenta */
        V_IDPERSONA CEN_PERSONA.IDPERSONA%TYPE; /* Identificador de la persona */
        V_FACT_IDCUENTA PYS_PRODUCTOSSOLICITADOS.IDCUENTA%TYPE; /* Numero de la cuenta */
        V_FACT_IDCUENTADEUDOR PYS_COMPRA.IDCUENTADEUDOR%TYPE; /* Numero de la cuenta del deudor*/
        V_IDFACTURA fac_factura.IDFACTURA%TYPE := 0; /* Identificador de la factura */
        V_NUMEROLINEA FAC_LINEAFACTURA.NUMEROLINEA%TYPE := 0; /* Numero de linea */
        V_IDFACTURACIONSUSCRIPCION FAC_FACTURACIONSUSCRIPCION.IDFACTURACIONSUSCRIPCION%TYPE := 0; /* Identificador de la facturacion suscripcion */
        V_VALOR_TIPOIVA PYS_TIPOIVA.VALOR%TYPE := 0; /* Porcentaje IVA */
        V_IDPERIODICIDAD PYS_PRECIOSSERVICIOS.IDPERIODICIDAD%TYPE := 0; /* Identificador de la periodicidad */
        V_IDPRECIOSSERVICIOS PYS_PRECIOSSERVICIOS.IDPRECIOSSERVICIOS%TYPE := 0; /* Identificador del precio del servicio */
        V_VALOR PYS_PRECIOSSERVICIOS.VALOR%TYPE := 0; /* Precio minimo del servicio */
        V_PERIODO PYS_PERIODICIDAD.PERIODOSMES%TYPE; /* Periodo */
        V_DESCRIPCION PYS_PERIODICIDAD.DESCRIPCION%TYPE; /* Descripcion de la periodicidad */
        V_DESCRIPCION_PRECIO PYS_PRECIOSSERVICIOS.DESCRIPCION%TYPE; /* Descripcion del precio */
        V_FECHA DATE; /* Fecha para calcular el periodo */
        V_PRIMERAVEZ VARCHAR2(1) := 'S'; /* Indicador para el calculo de P_FECHAINICIALPERIODO */
        V_FECHAINICIALPERIODO DATE; /* Fecha inicial del periodo */
        V_FECHAFINALPERIODO DATE; /* Fecha final del periodo */
        V_FECHAINICIAL DATE; /* Fecha inicial del periodo */
        V_FECHAFINAL DATE; /* Fecha final del periodo */
        V_FECHAINICIO DATE; /* Fecha inicial del periodo facturado */
        V_FECHAFIN DATE; /* Fecha final del periodo facturado */
        V_FECHAFINSERV DATE;
        V_CTACONTABLEP FAC_LINEAFACTURA.CTAPRODUCTOSERVICIO%TYPE;
        V_CTATIPOIVAP FAC_LINEAFACTURA.CTAIVA%TYPE;
        V_IDTIPOIVA PYS_TIPOIVA.IDTIPOIVA%TYPE;
        v_tienePeriodosIntermedios BOOLEAN;
        M_PoblacionSerieFacturacion PERSONA_TBL;
        V_OBSERVACIONESSF FAC_SERIEFACTURACION.OBSERVACIONES%TYPE := '';
        IND_LINEAFACTURA_ANTERIOR BINARY_INTEGER;
        v_neto FAC_FACTURA.IMPTOTALNETO%TYPE;
        v_iva FAC_FACTURA.IMPTOTALIVA%TYPE;
        v_CambioProductosServicios BOOLEAN := FALSE;
        v_TipoSerieFacturacion FAC_SERIEFACTURACION.TIPOSERIE%TYPE;
        v_ComprobarPoblacion NUMBER := 0;

        CURSOR C_FacturacionProductos(
                v_idInstitucion NUMBER,
                v_idSerieFacturacion NUMBER,
                v_idPeticion NUMBER,
                v_fechaInicioProductos DATE,
                v_fechaFinProductos DATE,
                v_PoblacionSerieFacturacion PERSONA_TBL,
                v_ComprobarPoblacion NUMBER) IS
            SELECT PYS.*,
                CLIENTE.ASIENTOCONTABLE,
                DECODE(PYS.IDCUENTADEUDOR, NULL, DECODE(PYS.IDCUENTA, NULL, METALICOOEFECTIVO, DOMICILIACIONBANCARIA), DOMICILIACIONBANCARIA) AS IDFORMAPAGO
            FROM (
                SELECT COMPRAS.IDINSTITUCION,
                    COMPRAS.IDPERSONA,
                    COMPRAS.IDPERSONADEUDOR,
                    COMPRAS.IDPETICION,
                    COMPRAS.IDTIPOPRODUCTO,
                    COMPRAS.IDPRODUCTO,
                    COMPRAS.IDPRODUCTOINSTITUCION,
                    COMPRAS.IDFORMAPAGO AS IDFORMAPAGO_LINEA,
                    COMPRAS.DESCRIPCION,
                    COMPRAS.CANTIDAD,
                    COMPRAS.IMPORTEUNITARIO,
                    NVL(COMPRAS.IMPORTEANTICIPADO,0) AS IMPORTEANTICIPADO,
                    ( -- Solo tiene cuenta cuando no es de baja
                        SELECT COMPRAS.IDCUENTA
                        FROM CEN_CUENTASBANCARIAS CUENTA
                        WHERE CUENTA.IDINSTITUCION = COMPRAS.IDINSTITUCION
                            AND CUENTA.IDPERSONA = COMPRAS.IDPERSONA
                            AND CUENTA.IDCUENTA = COMPRAS.IDCUENTA
                            AND CUENTA.FECHABAJA IS NULL
                    ) AS IDCUENTA,
                    (-- Solo tiene cuenta cuando no es de baja
                        SELECT COMPRAS.IDCUENTADEUDOR
                        FROM CEN_CUENTASBANCARIAS CUENTA
                        WHERE CUENTA.IDINSTITUCION = COMPRAS.IDINSTITUCION
                            AND CUENTA.IDPERSONA = COMPRAS.IDPERSONADEUDOR
                            AND CUENTA.IDCUENTA = COMPRAS.IDCUENTADEUDOR
                            AND CUENTA.FECHABAJA IS NULL
                    ) AS IDCUENTADEUDOR,
                    PPS.ORDEN,
                    SUBSTR(PER.NIFCIF, 1, 9) AS DEUDOR_ID,
                    PER.NOMBRE || ' ' ||PER.APELLIDOS1 || ' ' ||  PER.APELLIDOS2 AS DEUDOR_NOMBRE
                FROM PYS_COMPRA COMPRAS,
                    FAC_TIPOSPRODUINCLUENFACTU TIPOPROD,
                    PYS_PRODUCTOSSOLICITADOS PPS,
                    CEN_PERSONA PER
                WHERE PER.IDPERSONA = COMPRAS.IDPERSONA
                    AND (v_ComprobarPoblacion = 0 -- Si tiene valor 0, no hay que comprobar la poblacion
                        OR EXISTS ( -- Se comprueba que la persona que hizo la compra se incluye dentro de la poblacion de la serie de facturacion
                            SELECT 1
                            FROM TABLE(v_PoblacionSerieFacturacion) CLI
                            WHERE CLI.IDINSTITUCION = COMPRAS.IDINSTITUCION
                                AND CLI.IDPERSONA = COMPRAS.IDPERSONA
                        )
                    )
                    AND TIPOPROD.IDINSTITUCION = COMPRAS.IDINSTITUCION
                    AND TIPOPROD.IDTIPOPRODUCTO = COMPRAS.IDTIPOPRODUCTO
                    AND TIPOPROD.IDPRODUCTO = COMPRAS.IDPRODUCTO
                    AND TIPOPROD.IDINSTITUCION = v_idInstitucion
                    AND TIPOPROD.IDSERIEFACTURACION = v_idSerieFacturacion
                    AND COMPRAS.IDPETICION=DECODE(v_idPeticion, NULL, COMPRAS.IDPETICION, v_idPeticion)
                    AND COMPRAS.IDFACTURA IS NULL
                    AND COMPRAS.NUMEROLINEA IS NULL
                    AND TRUNC(COMPRAS.FECHA) BETWEEN TRUNC(v_fechaInicioProductos) AND TRUNC(v_fechaFinProductos)
                    AND COMPRAS.NOFACTURABLE=0 -- solo productos facturables
                    AND PPS.IDINSTITUCION = COMPRAS.IDINSTITUCION
                    AND PPS.IDPETICION = COMPRAS.IDPETICION
                    AND PPS.IDTIPOPRODUCTO = COMPRAS.IDTIPOPRODUCTO
                    AND PPS.IDPRODUCTO = COMPRAS.IDPRODUCTO
                    AND PPS.IDPRODUCTOINSTITUCION = COMPRAS.IDPRODUCTOINSTITUCION
                    AND NOT EXISTS (
                        SELECT 1
                        FROM PYS_PETICIONCOMPRASUSCRIPCION PPCS
                        WHERE PPCS.IDINSTITUCION = PPS.IDINSTITUCION
                            AND PPCS.IDPETICIONALTA = PPS.IDPETICION
                            AND PPCS.TIPOPETICION = 'B'
                            AND PPS.ACEPTADO = 'B'
                    )
            ) PYS,
            CEN_CLIENTE CLIENTE
        WHERE  CLIENTE.IDINSTITUCION = PYS.IDINSTITUCION
            AND CLIENTE.IDPERSONA = NVL(PYS.IDPERSONADEUDOR, PYS.IDPERSONA)
        ORDER BY PYS.IDPERSONA, PYS.IDCUENTA, PYS.IDPETICION, PYS.ORDEN;

        CURSOR C_FacturacionServicios(
                v_idInstitucion NUMBER,
                v_idSerieFacturacion NUMBER,
                v_fechaInicioServicios DATE,
                v_fechaFinServicios DATE,
                v_PoblacionSerieFacturacion PERSONA_TBL,
                v_ComprobarPoblacion NUMBER) IS
            SELECT PYS.*,
                CLIENTE.ASIENTOCONTABLE,
               DECODE(PYS.IDCUENTA, NULL, METALICOOEFECTIVO, DOMICILIACIONBANCARIA) AS IDFORMAPAGO
            FROM (
                SELECT SUSCRIP.IDINSTITUCION,
                    SUSCRIP.IDPERSONA,
                    SUSCRIP.IDTIPOSERVICIOS,
                    SUSCRIP.IDSERVICIO,
                    SUSCRIP.IDSERVICIOSINSTITUCION,
                    SUSCRIP.IDSUSCRIPCION,
                    SUSCRIP.CANTIDAD,
                    SUSCRIP.FECHASUSCRIPCION,
                    SUSCRIP.FECHABAJAFACTURACION,
                    SUSCRIP.IDFORMAPAGO AS IDFORMAPAGO_LINEA,
                    SERVICIO.DESCRIPCION,
                    SERVICIO.FACTURACIONPONDERADA AS FACTURACIONPROPORCIONAL,
                    SUBSTR(PER.NIFCIF, 1, 9) AS DEUDOR_ID,
                    PER.NOMBRE || ' ' ||PER.APELLIDOS1 || ' ' ||  PER.APELLIDOS2 AS DEUDOR_NOMBRE,
                    PSS.ORDEN,
                    ( -- Solo tiene cuenta cuando no es de baja
                        SELECT SUSCRIP.IDCUENTA
                        FROM CEN_CUENTASBANCARIAS CUENTA
                        WHERE CUENTA.IDINSTITUCION = SUSCRIP.IDINSTITUCION
                            AND CUENTA.IDPERSONA = SUSCRIP.IDPERSONA
                            AND CUENTA.IDCUENTA = SUSCRIP.IDCUENTA
                            AND CUENTA.FECHABAJA IS NULL
                    ) AS IDCUENTA
                FROM PYS_SUSCRIPCION SUSCRIP,
                    FAC_TIPOSSERVINCLSENFACT TIPOSERV,
                    PYS_SERVICIOSINSTITUCION SERVICIO,
                    PYS_SERVICIOSSOLICITADOS PSS,
                    CEN_PERSONA PER
                WHERE PER.IDPERSONA = SUSCRIP.IDPERSONA
                    AND (v_ComprobarPoblacion = 0 -- Si tiene valor 0, no hay que comprobar la poblacion
                        OR EXISTS ( -- Se comprueba que la persona que hizo la compra se incluye dentro de la poblacion de la serie de facturacion
                            SELECT 1
                            FROM TABLE(v_PoblacionSerieFacturacion) CLI
                            WHERE CLI.IDINSTITUCION = SUSCRIP.IDINSTITUCION
                                AND CLI.IDPERSONA = SUSCRIP.IDPERSONA
                        )
                    )
                    AND TIPOSERV.IDINSTITUCION = SUSCRIP.IDINSTITUCION
                    AND TIPOSERV.IDTIPOSERVICIOS = SUSCRIP.IDTIPOSERVICIOS
                    AND TIPOSERV.IDSERVICIO = SUSCRIP.IDSERVICIO
                    AND SUSCRIP.IDINSTITUCION = SERVICIO.IDINSTITUCION
                    AND SUSCRIP.IDTIPOSERVICIOS = SERVICIO.IDTIPOSERVICIOS
                    AND SUSCRIP.IDSERVICIO = SERVICIO.IDSERVICIO
                    AND SUSCRIP.IDSERVICIOSINSTITUCION = SERVICIO.IDSERVICIOSINSTITUCION
                    AND TIPOSERV.IDINSTITUCION = v_idInstitucion
                    AND TIPOSERV.IDSERIEFACTURACION = v_idSerieFacturacion
                    AND TRUNC(SUSCRIP.FECHASUSCRIPCION) <= TRUNC(v_fechaFinServicios)
                    AND (SUSCRIP.FECHABAJAFACTURACION IS NULL OR (TRUNC(SUSCRIP.FECHABAJAFACTURACION) >= TRUNC(v_fechaInicioServicios) AND TRUNC(SUSCRIP.FECHABAJAFACTURACION) >= TRUNC(SUSCRIP.FECHASUSCRIPCION)))
                    AND PSS.IDINSTITUCION = SUSCRIP.IDINSTITUCION
                    AND PSS.IDPETICION = SUSCRIP.IDPETICION
                    AND PSS.IDTIPOSERVICIOS = SUSCRIP.IDTIPOSERVICIOS
                    AND PSS.IDSERVICIO = SUSCRIP.IDSERVICIO
                    AND PSS.IDSERVICIOSINSTITUCION = SUSCRIP.IDSERVICIOSINSTITUCION
            ) PYS,
            CEN_CLIENTE CLIENTE
        WHERE  CLIENTE.IDINSTITUCION = PYS.IDINSTITUCION
            AND CLIENTE.IDPERSONA = PYS.IDPERSONA
        ORDER BY PYS.IDPERSONA, PYS.IDCUENTA;

        CURSOR C_PERINTERMEDIOSFACT(
                    v_idInstitucion NUMBER,
                    v_idTipoServicios NUMBER,
                    v_idServicio NUMBER,
                    v_idServicioInstitucion NUMBER,
                    v_idSuscripcion NUMBER,
                    v_idPersona NUMBER,
                     v_fechaInicialPeriodo DATE,
                     v_fechaFinalPeriodo DATE
                ) IS
            SELECT FACSUS.FECHAINICIO,
                 FACSUS.FECHAFIN
            FROM FAC_FACTURACIONSUSCRIPCION FACSUS,
                FAC_LINEAFACTURA LINEAFACT,
                FAC_FACTURA FACT
            WHERE FACSUS.IDINSTITUCION = LINEAFACT.IDINSTITUCION
                AND FACSUS.IDFACTURA = LINEAFACT.IDFACTURA
                AND FACSUS.NUMEROLINEA = LINEAFACT.NUMEROLINEA
                AND LINEAFACT.IDINSTITUCION = FACT.IDINSTITUCION
                AND LINEAFACT.IDFACTURA = FACT.IDFACTURA
                AND FACT.IDPERSONA = v_idPersona
                AND FACSUS.IDINSTITUCION = v_idInstitucion
                AND FACSUS.IDTIPOSERVICIOS = v_idTipoServicios
                AND FACSUS.IDSERVICIO = v_idServicio
                AND FACSUS.IDSERVICIOSINSTITUCION = v_idServicioInstitucion
                AND FACSUS.IDSUSCRIPCION = v_idSuscripcion -- Solo se tienen en cuenta los periodos de la misma suscripcion
                AND (
                    (FACSUS.FECHAFIN > v_fechaInicialPeriodo AND FACSUS.FECHAINICIO < v_fechaFinalPeriodo)
                    OR
                    (v_fechaInicialPeriodo = v_fechaFinalPeriodo AND (FACSUS.FECHAINICIO = v_fechaInicialPeriodo OR FACSUS.FECHAFIN = v_fechaFinalPeriodo)))
           ORDER BY FACSUS.FECHAINICIO ASC; -- JPT: Es necesario tener las facturaciones ordenadas por fecha inicial

    BEGIN
        V_DATOSERROR := 'CARGATABLASMEMORIA: Obtencion de datos de la serie de la facturacion';
        -- Obtengo el mensaje de error de la serie de facturacion y su poblacion
        SELECT OBSERVACIONES,
                F_SIGA_GETRECURSO_ETIQUETA('facturacion.generarFacturacion.mensaje.errorPoblacionSerieFacturacion', p_Idioma) || '(' ||DESCRIPCION || ')',
                TIPOSERIE
            INTO V_OBSERVACIONESSF,
                V_DATOSERROR,
                v_TipoSerieFacturacion
        FROM FAC_SERIEFACTURACION
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDSERIEFACTURACION = P_IDSERIEFACTURACION;

         -- Cuando idPeticion tiene valor es una facturacion rapida, y por lo tanto, ya se ha comprobado la poblacion al obtener las series de facturacion de la peticion
         -- Cuando el tipo de la serie es generico, se considera que la serie de facturacion contiene a todo el censo como poblacion, y por lo tanto, no hay que comprobar la poblacion
        IF (P_IDPETICION IS NULL OR v_TipoSerieFacturacion <> 'G') THEN
            v_ComprobarPoblacion := 1;

            -- Seleccionamos los clientes que se van a facturar
            V_DATOSERROR := 'CARGATABLASMEMORIA: Llamada al procedimiento OBTENCIONPOBLACIONCLIENTES (' || P_IDINSTITUCION || ', '|| P_IDSERIEFACTURACION || ')';
            M_PoblacionSerieFacturacion := OBTENCIONPOBLACIONCLIENTES(P_IDINSTITUCION, P_IDSERIEFACTURACION);
            IF (M_PoblacionSerieFacturacion IS NULL OR M_PoblacionSerieFacturacion.COUNT = 0) THEN
                V_CODRETORNO:='-201';
                RAISE E_ERROR;
            END IF;
       END IF;

        -- Tratamiento de las matrices
        V_DATOSERROR := 'CARGATABLASMEMORIA: Borrado de las matrices';
        M_FACTURA.DELETE;
        M_LINEAFACTURA.DELETE;
        M_FACTURACIONSUSCRIPCION.DELETE;
        IND_FACTURA := 0;
        IND_LINEAFACTURA := 0;
        IND_FACTURACIONSUSCRIPCION := 0;

        IF (V_FECHAINICIOPRODUCTOS IS NOT NULL AND V_FECHAFINPRODUCTOS IS NOT NULL) THEN -- Solo se obtienen compras cuando se ha introducido el periodo de los productos

            V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Apertura del cursor de facturacion de productos';
            FOR V_FacturacionProductos IN C_FacturacionProductos(P_IDINSTITUCION, P_IDSERIEFACTURACION, P_IDPETICION, V_FECHAINICIOPRODUCTOS, V_FECHAFINPRODUCTOS, M_PoblacionSerieFacturacion, v_ComprobarPoblacion) LOOP
                V_PRIMERAVEZ := 'S';

                V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Paso la cuenta a NUMBER';
                IF V_FacturacionProductos.IDCUENTA IS NULL THEN
                    V_FACT_IDCUENTA := NULL;
                ELSE
                    V_FACT_IDCUENTA := TO_NUMBER(V_FacturacionProductos.IDCUENTA);
                END IF;

                V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Paso la cuenta del deudor a NUMBER';
                IF V_FacturacionProductos.IDCUENTADEUDOR IS NULL THEN
                    V_FACT_IDCUENTADEUDOR := NULL;
                ELSE
                    V_FACT_IDCUENTADEUDOR := TO_NUMBER(V_FacturacionProductos.IDCUENTADEUDOR);
                END IF;

                -- Calculamos IDFACTURA
                IF V_PRIMERAFACTURA = 'S' THEN
                    v_CambioProductosServicios := TRUE;

                    V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Obtencion de un nuevo identificador de factura';
                    SELECT NVL(MAX(TO_NUMBER(IDFACTURA)), 0) + 1
                        INTO V_IDFACTURA
                    FROM FAC_FACTURA
                    WHERE IDINSTITUCION = P_IDINSTITUCION;

                    -- Indicamos que se va a tratar la primera linea de la factura
                    V_PRIMERALINEAFACTURA := 'S';

                ELSIF (NVL(V_FACT_IDCUENTA, -1) <> NVL(V_IDCUENTA, -1) -- Cambio de cuenta
                        OR V_IDPERSONA <> V_FacturacionProductos.IDPERSONA) THEN -- Cambio de cliente

                    -- Obtencion del siguiente identificador de facturas
                    V_IDFACTURA := V_IDFACTURA + 1;

                    -- Indicamos que se va a tratar la primera linea de la factura
                    V_PRIMERALINEAFACTURA := 'S';
                END IF;

                V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Obtencion de un nuevo identificador de linea de factura';
                IF V_PRIMERALINEAFACTURA = 'S' THEN
                    SELECT NVL(MAX(NUMEROLINEA), 0) + 1
                        INTO V_NUMEROLINEA
                    FROM FAC_LINEAFACTURA
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                       AND IDFACTURA = V_IDFACTURA;

                    V_PRIMERALINEAFACTURA := 'N';
                ELSE
                    V_NUMEROLINEA := V_NUMEROLINEA + 1;
                END IF;

                V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Obtencion del IVA del producto';
                SELECT T.VALOR, P.CUENTACONTABLE, T.SUBCTATIPO, T.IDTIPOIVA
                    INTO V_VALOR_TIPOIVA, V_CTACONTABLEP, V_CTATIPOIVAP, V_IDTIPOIVA
                FROM PYS_PRODUCTOSINSTITUCION P, PYS_TIPOIVA T
                WHERE P.IDTIPOIVA = T.IDTIPOIVA
                    AND P.IDINSTITUCION = P_IDINSTITUCION
                    AND P.IDTIPOPRODUCTO = V_FacturacionProductos.IDTIPOPRODUCTO
                    AND P.IDPRODUCTO = V_FacturacionProductos.IDPRODUCTO
                    AND P.IDPRODUCTOINSTITUCION = V_FacturacionProductos.IDPRODUCTOINSTITUCION;

                /*********************** MATRIZ DE LINEAS DE FACTURAS *************************/
                V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Insercion en la matriz de Lineas de Facturas del producto';
                IND_LINEAFACTURA := IND_LINEAFACTURA + 1;
                M_LINEAFACTURA(IND_LINEAFACTURA).IDFACTURA := V_IDFACTURA;
                M_LINEAFACTURA(IND_LINEAFACTURA).NUMEROLINEA := V_NUMEROLINEA;
                M_LINEAFACTURA(IND_LINEAFACTURA).NUMEROORDEN := V_FacturacionProductos.ORDEN;
                M_LINEAFACTURA(IND_LINEAFACTURA).DESCRIPCION := V_FacturacionProductos.DESCRIPCION;
                M_LINEAFACTURA(IND_LINEAFACTURA).CANTIDAD := V_FacturacionProductos.CANTIDAD;
                M_LINEAFACTURA(IND_LINEAFACTURA).PRECIOUNITARIO := TO_NUMBER(TO_CHAR(V_FacturacionProductos.IMPORTEUNITARIO, '99999990D00'));
                M_LINEAFACTURA(IND_LINEAFACTURA).IMPORTEANTICIPADO := TO_NUMBER(TO_CHAR(NVL(V_FacturacionProductos.IMPORTEANTICIPADO, 0), '99999990D00'));
                M_LINEAFACTURA(IND_LINEAFACTURA).IDPETICION := V_FacturacionProductos.IDPETICION;
                M_LINEAFACTURA(IND_LINEAFACTURA).IDTIPOPRODUCTO := V_FacturacionProductos.IDTIPOPRODUCTO;
                M_LINEAFACTURA(IND_LINEAFACTURA).IDPRODUCTO := V_FacturacionProductos.IDPRODUCTO;
                M_LINEAFACTURA(IND_LINEAFACTURA).IDPRODUCTOINSTITUCION := V_FacturacionProductos.IDPRODUCTOINSTITUCION;
                M_LINEAFACTURA(IND_LINEAFACTURA).IDFORMAPAGO := V_FacturacionProductos.IDFORMAPAGO_LINEA;
                M_LINEAFACTURA(IND_LINEAFACTURA).IVA := TO_NUMBER(TO_CHAR(V_VALOR_TIPOIVA, '990D00'));
                M_LINEAFACTURA(IND_LINEAFACTURA).IDTIPOIVA := V_IDTIPOIVA;
                M_LINEAFACTURA(IND_LINEAFACTURA).CTAPRODUCTOSERVICIO := V_CTACONTABLEP;
                M_LINEAFACTURA(IND_LINEAFACTURA).CTAIVA := F_SIGA_GETPARAMETRO('FAC','CONTABILIDAD_IVA',P_IDINSTITUCION) || V_CTATIPOIVAP;

                /*********************** MATRIZ DE FACTURAS *************************/
                /* Inserta los datos extraidos en la matriz de facturas solo si se han insertado datos en la matriz de lineas de facturas para esa factura */
                V_DATOSERROR := 'CARGATABLASMEMORIA(Productos): Insercion en la matriz de Facturas de la factura '||V_IDFACTURA;
                IF (V_PRIMERAFACTURA = 'S'
                        OR NVL(V_IDCUENTA, -1) <> NVL(V_FACT_IDCUENTA, -1) -- Cambio de cuenta
                        OR V_IDPERSONA <> V_FacturacionProductos.IDPERSONA) THEN -- Cambio de cliente

                    IND_FACTURA := IND_FACTURA + 1;
                    M_FACTURA(IND_FACTURA).IDFACTURA := V_IDFACTURA;
                    M_FACTURA(IND_FACTURA).IDPERSONA := V_FacturacionProductos.IDPERSONA;
                    M_FACTURA(IND_FACTURA).IDPERSONADEUDOR := V_FacturacionProductos.IDPERSONADEUDOR;
                    M_FACTURA(IND_FACTURA).IDFORMAPAGO := V_FacturacionProductos.IDFORMAPAGO;
                    M_FACTURA(IND_FACTURA).IDCUENTA:= V_FACT_IDCUENTA;
                    M_FACTURA(IND_FACTURA).IDCUENTADEUDOR:= V_FACT_IDCUENTADEUDOR;
                    M_FACTURA(IND_FACTURA).OBSERVACIONES := V_OBSERVACIONESSF;
                    M_FACTURA(IND_FACTURA).CTACLIENTE := V_FacturacionProductos.ASIENTOCONTABLE;
                    M_FACTURA(IND_FACTURA).DEUDOR_ID := V_FacturacionProductos.DEUDOR_ID;
                    M_FACTURA(IND_FACTURA).DEUDOR_NOMBRE := V_FacturacionProductos.DEUDOR_NOMBRE;
                    M_FACTURA(IND_FACTURA).IDMANDATO := 2; -- Factura de productos

                    V_IDCUENTA := V_FACT_IDCUENTA;
                    V_IDPERSONA := V_FacturacionProductos.IDPERSONA;
                    V_PRIMERAFACTURA := 'N';
                END IF;

                -- Actualiza los importes de la factura, sumando los importes de sus lineas de factura
                v_neto := M_LINEAFACTURA(IND_LINEAFACTURA).PRECIOUNITARIO * M_LINEAFACTURA(IND_LINEAFACTURA).CANTIDAD;

                -- Hay que hacer una redondeo por cada pys con su iva
                v_iva := ROUND(M_LINEAFACTURA(IND_LINEAFACTURA).CANTIDAD * M_LINEAFACTURA(IND_LINEAFACTURA).PRECIOUNITARIO * M_LINEAFACTURA(IND_LINEAFACTURA).IVA / 100, 2);

                -- Calcula los importes finales de la factura
                M_FACTURA(IND_FACTURA).IMPTOTALNETO := NVL(M_FACTURA(IND_FACTURA).IMPTOTALNETO,0) + v_neto;
                M_FACTURA(IND_FACTURA).IMPTOTALIVA := NVL(M_FACTURA(IND_FACTURA).IMPTOTALIVA,0) + v_iva;
                M_FACTURA(IND_FACTURA).IMPTOTAL := NVL(M_FACTURA(IND_FACTURA).IMPTOTAL,0) + v_neto + v_iva;
                M_FACTURA(IND_FACTURA).IMPTOTALANTICIPADO := NVL(M_FACTURA(IND_FACTURA).IMPTOTALANTICIPADO,0) + M_LINEAFACTURA(IND_LINEAFACTURA).IMPORTEANTICIPADO;
                M_FACTURA(IND_FACTURA).IMPTOTALPORPAGAR := M_FACTURA(IND_FACTURA).IMPTOTAL - M_FACTURA(IND_FACTURA).IMPTOTALANTICIPADO;
            END LOOP; -- FOR V_FacturacionProductos IN C_FacturacionProductos
        END IF;

        IF (V_FECHAINICIOSERVICIOS IS NOT NULL -- Solo se obtienen suscripciones cuando se ha introducido el periodo de los servicios
                AND V_FECHAFINSERVICIOS IS NOT NULL -- Solo se obtienen suscripciones cuando se ha introducido el periodo de los servicios
                AND P_IDPETICION IS NULL -- Cuando idPeticion no es nulo es facturacion rapida y no se facturan servicios
            ) THEN

            V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Apertura del cursor de facturacion de servicios';
            FOR V_FacturacionServicios IN C_FacturacionServicios(P_IDINSTITUCION, P_IDSERIEFACTURACION, V_FECHAINICIOSERVICIOS, V_FECHAFINSERVICIOS, M_PoblacionSerieFacturacion, v_ComprobarPoblacion) LOOP
                V_PRIMERAVEZ := 'S';
                V_FECHAFINSERV := V_FECHAFINSERVICIOS; -- Copia la fecha final del servicio
                IND_LINEAFACTURA_ANTERIOR := IND_LINEAFACTURA; -- Copia el indice que indica hasta donde llegaba el PYS anterior

                V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Paso la cuenta a NUMBER';
                IF V_FacturacionServicios.IDCUENTA IS NULL THEN
                    V_FACT_IDCUENTA := NULL;
                ELSE
                    V_FACT_IDCUENTA := TO_NUMBER(V_FacturacionServicios.IDCUENTA);
                END IF;

                -- Calculamos IDFACTURA
                IF V_PRIMERAFACTURA = 'S' THEN

                    V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Obtencion de un nuevo identificador de factura';
                    SELECT NVL(MAX(TO_NUMBER(IDFACTURA)), 0) + 1
                        INTO V_IDFACTURA
                    FROM FAC_FACTURA
                    WHERE IDINSTITUCION = P_IDINSTITUCION;

                    -- Indicamos que se va a tratar la primera linea de la factura
                    V_PRIMERALINEAFACTURA := 'S';

                ELSIF (NVL(V_FACT_IDCUENTA, -1) <> NVL(V_IDCUENTA, -1) -- Cambio de cuenta
                        OR V_IDPERSONA <> V_FacturacionServicios.IDPERSONA -- Cambio de cliente
                        OR v_CambioProductosServicios = TRUE) THEN -- Cambio de productos a servicios

                    -- Obtencion del siguiente identificador de facturas
                    V_IDFACTURA := V_IDFACTURA + 1;

                    -- Indicamos que se va a tratar la primera linea de la factura
                    V_PRIMERALINEAFACTURA := 'S';
                END IF;

                V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Obtencion de un nuevo identificador de la facturacion de suscripcion';
                SELECT NVL(MAX(IDFACTURACIONSUSCRIPCION), 0) + 1
                    INTO V_IDFACTURACIONSUSCRIPCION
                FROM FAC_FACTURACIONSUSCRIPCION
                WHERE IDINSTITUCION = P_IDINSTITUCION
                       AND IDTIPOSERVICIOS = V_FacturacionServicios.IDTIPOSERVICIOS
                       AND IDSERVICIO = V_FacturacionServicios.IDSERVICIO
                       AND IDSERVICIOSINSTITUCION = V_FacturacionServicios.IDSERVICIOSINSTITUCION
                       AND IDSUSCRIPCION = V_FacturacionServicios.IDSUSCRIPCION;

                /* Si la fecha de inicio del servicio es anterior a la fecha de suscripcion, consideramos como fecha de inicio del servicio la fecha de suscripcion. */
                V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Obtencion de la fecha inicial del servicio';
                IF V_FECHAINICIOSERVICIOS < V_FacturacionServicios.FECHASUSCRIPCION THEN
                    V_FECHA := TRUNC(V_FacturacionServicios.FECHASUSCRIPCION);
                ELSE
                    V_FECHA := V_FECHAINICIOSERVICIOS;
                END IF;

                /* Si la fecha de fin del servicio es posterior o igual a la fecha de baja, consideramos como fecha de fin del servicio el dia anterior de la fecha de baja.*/
                V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Obtencion de la fecha final del servicio';
                IF V_FECHAFINSERV > TRUNC(V_FacturacionServicios.FECHABAJAFACTURACION) THEN
                    V_FECHAFINSERV := TRUNC(V_FacturacionServicios.FECHABAJAFACTURACION);
                END IF;

                V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Bucle para el calculo de los periodos del servicio';
                LOOP
                    /* Obtenemos el periodo del servicio correspondiente al cliente */
                    V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Llamada al procedimiento PROC_SIGA_CALCULARPERIODO';
                    -- Procedimiento para la obtencion del periodo asociado al minimo precio mensual del servicio
                    PROC_SIGA_CALCULARPERIODO(
                        P_IDINSTITUCION,
                        V_FacturacionServicios.IDTIPOSERVICIOS,
                        V_FacturacionServicios.IDSERVICIO,
                        V_FacturacionServicios.IDSERVICIOSINSTITUCION,
                        V_FacturacionServicios.IDPERSONA,
                        V_FECHA,
                        V_PRIMERAVEZ,  -- Solo calculamos V_FECHAINICIALPERIODO la primera vez, las demas veces V_FECHAINICIALPERIODO = V_FECHA
                        P_IDIOMA,
                        V_IDPERIODICIDAD, -- OUT : No usa este valor
                        V_IDPRECIOSSERVICIOS, -- OUT : No usa este valor
                        V_VALOR, -- OUT : No usa este valor
                        V_PERIODO, --- OUT : No usa este valor
                        V_VALOR_TIPOIVA, -- OUT : No usa este valor
                        V_DESCRIPCION, -- OUT : No usa este valor
                        V_FECHAINICIALPERIODO, -- OUT
                        V_FECHAFINALPERIODO, -- OUT
                        V_DESCRIPCION_PRECIO, -- OUT : No usa este valor
                        V_CODRETORNO, -- OUT
                        V_DATOSERROR); -- OUT
                    IF V_CODRETORNO <> TO_CHAR(0) THEN
                        RAISE E_ERROR;
                    END IF;

                    IF (V_FacturacionServicios.FACTURACIONPROPORCIONAL='1') THEN
                        /* Si la fecha de inicio del periodo es anterior a la fecha de suscripcion, consideramos como fecha de inicio del periodo la fecha de suscripcion. */
                        V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Obtencion de la fecha inicial del periodo';
                        IF (V_FECHAINICIALPERIODO < V_FacturacionServicios.FECHASUSCRIPCION) THEN
                            V_FECHAINICIALPERIODO := TRUNC(V_FacturacionServicios.FECHASUSCRIPCION);
                        END IF;

                        /* Si la fecha de fin del periodo es posterior o igual a la fecha de baja, consideramos como fecha de fin del periodo el dia anterior de la fecha de baja.*/
                        V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Obtencion de la fecha final del periodo';
                        IF (V_FECHAFINALPERIODO > TRUNC(V_FacturacionServicios.FECHABAJAFACTURACION)) THEN
                            V_FECHAFINALPERIODO := TRUNC(V_FacturacionServicios.FECHABAJAFACTURACION);
                        END IF;
                    END IF;

                    V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Apertura del cursor de periodos intermedios facturados';
                    v_tienePeriodosIntermedios := FALSE;
                    V_FECHAINICIAL := V_FECHAINICIALPERIODO;
                    FOR V_PERINTERMEDIOSFACT IN C_PERINTERMEDIOSFACT(
                        P_IDINSTITUCION,
                        V_FacturacionServicios.IDTIPOSERVICIOS,
                        V_FacturacionServicios.IDSERVICIO,
                        V_FacturacionServicios.IDSERVICIOSINSTITUCION,
                        V_FacturacionServicios.IDSUSCRIPCION,
                        V_FacturacionServicios.IDPERSONA,
                        V_FECHAINICIALPERIODO,
                        V_FECHAFINALPERIODO
                    ) LOOP
                        v_tienePeriodosIntermedios := TRUE;
                        V_FECHAINICIO := TRUNC(V_PERINTERMEDIOSFACT.FECHAINICIO);
                        V_FECHAFIN := TRUNC(V_PERINTERMEDIOSFACT.FECHAFIN);

                        V_DATOSERROR := 'CARGATABLASMEMORIA(PeriodosIntermedios): Actualizacion de la fecha final del periodo a incluir en la facturacion';
                        V_FECHAFINAL := V_FECHAINICIO - 1;

                        IF V_FECHAINICIAL < V_FECHAFINAL THEN --En caso de que V_FECHAINICIAL >= V_FECHAFINAL el proceso no hace nada

                            V_DATOSERROR := 'CARGATABLASMEMORIA(PeriodosIntermedios): Llamada al procedimiento INCLUIRPERIODO';
                            INCLUIRPERIODO(
                                P_IDINSTITUCION,
                                V_FacturacionServicios.IDTIPOSERVICIOS,
                                V_FacturacionServicios.IDSERVICIO,
                                V_FacturacionServicios.IDSERVICIOSINSTITUCION,
                                V_FacturacionServicios.IDSUSCRIPCION,
                                V_FacturacionServicios.DESCRIPCION,
                                V_FacturacionServicios.CANTIDAD,
                                V_FacturacionServicios.IDPERSONA,
                                V_FECHAINICIAL,
                                V_FECHAFINAL,
                                V_IDFACTURA,
                                P_IDIOMA,
                                V_FacturacionServicios.ORDEN,
                                V_FacturacionServicios.IDFORMAPAGO_LINEA,
                                V_NUMEROLINEA, -- IN OUT
                                V_PRIMERALINEAFACTURA, -- IN OUT
                                V_IDFACTURACIONSUSCRIPCION, -- IN OUT
                                V_FacturacionServicios.FACTURACIONPROPORCIONAL, -- IN OUT
                                V_CODRETORNO, -- OUT
                                V_DATOSERROR); -- OUT
                            IF V_CODRETORNO <> TO_CHAR(0) THEN
                                RAISE E_ERROR;
                            END IF;

                            V_IDFACTURACIONSUSCRIPCION := V_IDFACTURACIONSUSCRIPCION + 1;
                        END IF;

                        V_DATOSERROR := 'CARGATABLASMEMORIA(PeriodosIntermedios): Actualizacion de la fecha inicial del periodo a incluir en la facturacion';
                        V_FECHAINICIAL := NVL(V_FECHAFIN, V_FECHAFINALPERIODO) + 1;
                    END LOOP;

                    IF (NOT v_tienePeriodosIntermedios) THEN -- Si no hay periodos intermedios facturados, incluimos el periodo completo

                        V_DATOSERROR := 'CARGATABLASMEMORIA(SinPeriodosIntermedios): Llamada al procedimiento INCLUIRPERIODO';
                        INCLUIRPERIODO(
                            P_IDINSTITUCION,
                            V_FacturacionServicios.IDTIPOSERVICIOS,
                            V_FacturacionServicios.IDSERVICIO,
                            V_FacturacionServicios.IDSERVICIOSINSTITUCION,
                            V_FacturacionServicios.IDSUSCRIPCION,
                            V_FacturacionServicios.DESCRIPCION,
                            V_FacturacionServicios.CANTIDAD,
                            V_FacturacionServicios.IDPERSONA,
                            V_FECHAINICIALPERIODO,
                            V_FECHAFINALPERIODO,
                            V_IDFACTURA,
                            P_IDIOMA,
                            V_FacturacionServicios.ORDEN,
                            V_FacturacionServicios.IDFORMAPAGO_LINEA,
                            V_NUMEROLINEA, -- IN OUT
                            V_PRIMERALINEAFACTURA, -- IN OUT
                            V_IDFACTURACIONSUSCRIPCION, -- IN OUT
                            V_FacturacionServicios.FACTURACIONPROPORCIONAL, -- IN OUT
                            V_CODRETORNO, -- OUT
                            V_DATOSERROR); -- OUT
                        IF V_CODRETORNO <> TO_CHAR(0) THEN
                            RAISE E_ERROR;
                        END IF;

                        V_IDFACTURACIONSUSCRIPCION := V_IDFACTURACIONSUSCRIPCION + 1;

                    -- Hay que consultar si queda el ultimo tramo por facturar
                    -- Si el ultimo tramo esta facturado no entra ya que la V_FECHAINICIAL > V_FECHAFINALPERIODO
                    ELSIF (V_FECHAINICIAL<=V_FECHAFINALPERIODO) THEN
                        V_DATOSERROR := 'CARGATABLASMEMORIA(UltimoTramoSinFacturar): Llamada al procedimiento INCLUIRPERIODO';
                        INCLUIRPERIODO(
                            P_IDINSTITUCION,
                            V_FacturacionServicios.IDTIPOSERVICIOS,
                            V_FacturacionServicios.IDSERVICIO,
                            V_FacturacionServicios.IDSERVICIOSINSTITUCION,
                            V_FacturacionServicios.IDSUSCRIPCION,
                            V_FacturacionServicios.DESCRIPCION,
                            V_FacturacionServicios.CANTIDAD,
                            V_FacturacionServicios.IDPERSONA,
                            V_FECHAINICIAL,
                            V_FECHAFINALPERIODO, -- La fecha final no cambia ya que es el ultimo tramo del periodo
                            V_IDFACTURA,
                            P_IDIOMA,
                            V_FacturacionServicios.ORDEN,
                            V_FacturacionServicios.IDFORMAPAGO_LINEA,
                            V_NUMEROLINEA, -- IN OUT
                            V_PRIMERALINEAFACTURA, -- IN OUT
                            V_IDFACTURACIONSUSCRIPCION, -- IN OUT
                            V_FacturacionServicios.FACTURACIONPROPORCIONAL, -- IN OUT
                            V_CODRETORNO, -- OUT
                            V_DATOSERROR); -- OUT
                        IF V_CODRETORNO <> TO_CHAR(0) THEN
                            RAISE E_ERROR;
                        END IF;

                        V_IDFACTURACIONSUSCRIPCION := V_IDFACTURACIONSUSCRIPCION + 1;
                    END IF;

                    V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Inicializacion de la fecha para la que se va a calcular el periodo';
                    V_FECHA := V_FECHAFINALPERIODO + 1;
                    V_PRIMERAVEZ := 'N';

                    V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Comprobacion de salida del bucle para el calculo de los periodos del servicio';
                    EXIT WHEN V_FECHAFINSERV < V_FECHA;
                END LOOP;

                /*********************** MATRIZ DE FACTURAS *************************/
                /* Inserta los datos extraidos en la matriz de facturas solo si se han insertado datos en la matriz de lineas de facturas para esa factura */
                V_DATOSERROR := 'CARGATABLASMEMORIA(Servicios): Insercion en la matriz de Facturas de la factura '||V_IDFACTURA;
                IF IND_LINEAFACTURA <> 0 AND M_LINEAFACTURA(IND_LINEAFACTURA).IDFACTURA = V_IDFACTURA THEN
                    IF (
                            V_PRIMERAFACTURA = 'S'
                            OR NVL(V_IDCUENTA, -1) <> NVL(V_FACT_IDCUENTA, -1) -- Cambio de cuenta
                            OR V_IDPERSONA <> V_FacturacionServicios.IDPERSONA -- Cambio de cliente
                            OR v_CambioProductosServicios = TRUE) THEN -- Cambio de productos a servicios

                        IND_FACTURA := IND_FACTURA + 1;
                        M_FACTURA(IND_FACTURA).IDFACTURA := V_IDFACTURA;
                        M_FACTURA(IND_FACTURA).IDPERSONA := V_FacturacionServicios.IDPERSONA;
                        M_FACTURA(IND_FACTURA).IDPERSONADEUDOR := NULL;
                        M_FACTURA(IND_FACTURA).IDFORMAPAGO := V_FacturacionServicios.IDFORMAPAGO;
                        M_FACTURA(IND_FACTURA).IDCUENTA:= V_FACT_IDCUENTA;
                        M_FACTURA(IND_FACTURA).IDCUENTADEUDOR:= NULL;
                        M_FACTURA(IND_FACTURA).OBSERVACIONES := V_OBSERVACIONESSF;
                        M_FACTURA(IND_FACTURA).CTACLIENTE := V_FacturacionServicios.ASIENTOCONTABLE;
                        M_FACTURA(IND_FACTURA).DEUDOR_ID := V_FacturacionServicios.DEUDOR_ID;
                        M_FACTURA(IND_FACTURA).DEUDOR_NOMBRE := V_FacturacionServicios.DEUDOR_NOMBRE;
                        M_FACTURA(IND_FACTURA).IDMANDATO := 1; -- Factura de servicios

                        V_IDCUENTA := V_FACT_IDCUENTA;
                        V_IDPERSONA := V_FacturacionServicios.IDPERSONA;
                        V_PRIMERAFACTURA := 'N';
                        v_CambioProductosServicios := FALSE;
                    END IF;

                    -- Actualiza los importes de la factura, sumando los importes de sus lineas de factura
                    FOR ILF IN IND_LINEAFACTURA_ANTERIOR+1..IND_LINEAFACTURA LOOP

                        v_neto := M_LINEAFACTURA(ILF).PRECIOUNITARIO * M_LINEAFACTURA(ILF).CANTIDAD;

                        -- Hay que hacer una redondeo por cada pys con su iva
                        v_iva := ROUND(M_LINEAFACTURA(ILF).CANTIDAD * M_LINEAFACTURA(ILF).PRECIOUNITARIO * M_LINEAFACTURA(ILF).IVA / 100, 2);

                        -- Calcula los importes finales de la factura
                        M_FACTURA(IND_FACTURA).IMPTOTALNETO := NVL(M_FACTURA(IND_FACTURA).IMPTOTALNETO,0) + v_neto;
                        M_FACTURA(IND_FACTURA).IMPTOTALIVA := NVL(M_FACTURA(IND_FACTURA).IMPTOTALIVA,0) + v_iva;
                        M_FACTURA(IND_FACTURA).IMPTOTAL := NVL(M_FACTURA(IND_FACTURA).IMPTOTAL,0) + v_neto + v_iva;
                        M_FACTURA(IND_FACTURA).IMPTOTALANTICIPADO := NVL(M_FACTURA(IND_FACTURA).IMPTOTALANTICIPADO,0) + M_LINEAFACTURA(ILF).IMPORTEANTICIPADO;
                        M_FACTURA(IND_FACTURA).IMPTOTALPORPAGAR := M_FACTURA(IND_FACTURA).IMPTOTAL - M_FACTURA(IND_FACTURA).IMPTOTALANTICIPADO;
                    END LOOP;

                ELSE
                    -- Obtencion del anterior identificador de facturas
                    V_IDFACTURA := V_IDFACTURA - 1;

                    -- Indicamos que se ya se ha tratado anteriormente la primera linea de la factura
                    V_PRIMERALINEAFACTURA := 'N';
                END IF;
            END LOOP; -- FOR V_FacturacionServicios IN C_FacturacionServicios
        END IF;

        V_DATOSERROR := 'CARGATABLASMEMORIA: Actualizacion de los parametros de salida';
        P_CODRETORNO := TO_CHAR(0);
        P_DATOSERROR := NULL;

        EXCEPTION
            WHEN E_ERROR THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;

                /* Tratamiento de las matrices */
                M_FACTURA.DELETE;
                M_LINEAFACTURA.DELETE;
                M_FACTURACIONSUSCRIPCION.DELETE;
                IND_FACTURA := NULL;
                IND_LINEAFACTURA := NULL;
                IND_FACTURACIONSUSCRIPCION := NULL;

            WHEN OTHERS THEN
                /* Tratamiento de las matrices */
                M_FACTURA.DELETE;
                M_LINEAFACTURA.DELETE;
                M_FACTURACIONSUSCRIPCION.DELETE;
                IND_FACTURA := NULL;
                IND_LINEAFACTURA := NULL;
                IND_FACTURACIONSUSCRIPCION := NULL;
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := V_DATOSERROR;
    END CARGATABLASMEMORIA;

    /*****************************************************************************************************************************/
    /* Nombre: GENERACIONFACTURACION  */
    /* Descripcion:   Generacion de facturas */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN  Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 01/09/2015 - Autor Jorge Paez Trivino - Cambios encontrados al realizar la documentacion del proceso de facturacion */
    /*****************************************************************************************************************************/
    PROCEDURE GENERACIONFACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDPROGRAMACION IN NUMBER,
        P_IDIOMA IN NUMBER,
        P_IDPETICION IN NUMBER, -- Cuendo tiene dato es porque viene de facturacion rapida
        P_USUMODIFICACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        V_FECHAREALGENERACION FAC_FACTURACIONPROGRAMADA.FECHAREALGENERACION%TYPE;  /* Fecha real de generacion */
        v_controlAnticipos Number;

    BEGIN
        IF (P_IDPETICION IS NULL) THEN -- Si viene de facturacion programada se revisan los anticipos de los servicios
            -- JPT (12-01-2015) - Control de anticipos: Debe dar error cuando existan facturas de servicios con anticipo, sin linea de anticipo asociada
            SELECT COUNT(*)
                INTO v_controlAnticipos
            FROM FAC_LINEAFACTURA
            WHERE FAC_LINEAFACTURA.IMPORTEANTICIPADO > 0
                AND FAC_LINEAFACTURA.IDINSTITUCION = P_IDINSTITUCION
                AND EXISTS (
                    SELECT 1
                    FROM FAC_FACTURACIONSUSCRIPCION
                    WHERE FAC_FACTURACIONSUSCRIPCION.IDINSTITUCION = FAC_LINEAFACTURA.IDINSTITUCION
                        AND FAC_FACTURACIONSUSCRIPCION.IDFACTURA = FAC_LINEAFACTURA.IDFACTURA
                        AND FAC_FACTURACIONSUSCRIPCION.NUMEROLINEA = FAC_LINEAFACTURA.NUMEROLINEA
                )
                AND NOT EXISTS (
                    SELECT 1
                      FROM PYS_LINEAANTICIPO
                     WHERE PYS_LINEAANTICIPO.IDINSTITUCION = FAC_LINEAFACTURA.IDINSTITUCION
                       AND PYS_LINEAANTICIPO.IDFACTURA = FAC_LINEAFACTURA.IDFACTURA
                       AND PYS_LINEAANTICIPO.NUMEROLINEA = FAC_LINEAFACTURA.NUMEROLINEA);
            IF (v_controlAnticipos > 0) THEN
                V_CODRETORNO:='-202';
                P_DATOSERROR := F_SIGA_GETRECURSO_ETIQUETA('facturacion.generarFacturacion.mensaje.errorServiciosAnticipados', p_Idioma);
                RAISE E_ERROR;
            END IF;
        END IF;

        P_DATOSERROR := 'GENERACIONFACTURACION: Lectura de las fechas inicio y fin de productos y servicios';
        SELECT FECHAINICIOPRODUCTOS,
                FECHAFINPRODUCTOS,
                FECHAINICIOSERVICIOS,
                FECHAFINSERVICIOS
            INTO V_FECHAINICIOPRODUCTOS,
                V_FECHAFINPRODUCTOS,
                V_FECHAINICIOSERVICIOS,
                V_FECHAFINSERVICIOS
        FROM FAC_FACTURACIONPROGRAMADA
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDSERIEFACTURACION = P_IDSERIEFACTURACION
            AND IDPROGRAMACION = P_IDPROGRAMACION;

        P_DATOSERROR :=  'GENERACIONFACTURACION: Llamada al procedimiento CARGATABLASMEMORIA';
        CARGATABLASMEMORIA(
            P_IDINSTITUCION,
            P_IDSERIEFACTURACION,
            P_IDIOMA,
            P_IDPETICION,
            V_CODRETORNO,
            P_DATOSERROR);
        IF (V_CODRETORNO <> TO_CHAR(0)) THEN
            RAISE E_ERROR;
        END IF;

        V_FECHAREALGENERACION := SYSDATE; -- Actualizamos la fecha real de generacion

        /*****************************************/
        /** AQUI EMPIEZA EL BLOQUEO DE LA TRANSACCION **/
        /*****************************************/

        P_DATOSERROR := 'GENERACIONFACTURACION: Actualizacion de la tabla FAC_FACTURACIONPROGRAMADA';
        UPDATE FAC_FACTURACIONPROGRAMADA
        SET FECHAREALGENERACION = V_FECHAREALGENERACION,
            FECHAMODIFICACION = V_FECHAREALGENERACION,
            USUMODIFICACION = P_USUMODIFICACION,
            IDESTADOCONFIRMACION = 2
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDSERIEFACTURACION = P_IDSERIEFACTURACION
            AND IDPROGRAMACION = P_IDPROGRAMACION;

        P_DATOSERROR :=  'GENERACIONFACTURACION: Lectura de la matriz de facturas';
        FOR IND_F IN 1..IND_FACTURA LOOP

            -- Nuevo control que garantiza que el importe de una factura nunca puede ser negativo
            IF (M_FACTURA(IND_FACTURA).IMPTOTAL < 0) THEN
                V_CODRETORNO:='-203';
                P_DATOSERROR := F_SIGA_GETRECURSO_ETIQUETA('facturacion.generarFacturacion.mensaje.errorFacturaNegativa', p_Idioma) ||
                                                ' ' || M_FACTURA(IND_F).DEUDOR_NOMBRE ||
                                                ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                                                ' ' || M_FACTURA(IND_F).DEUDOR_ID;
                RAISE E_ERROR;
            END IF;

            BEGIN
                P_DATOSERROR :=  'GENERACIONFACTURACION: Insercion en FAC_FACTURA';
                INSERT INTO FAC_FACTURA (
                    IDINSTITUCION,
                    IDFACTURA,
                    FECHAEMISION,
                    NUMEROFACTURA,
                    CONTABILIZADA,
                    OBSERVACIONES,
                    IDSERIEFACTURACION,
                    IDPROGRAMACION,
                    IDFORMAPAGO,
                    IDPERSONA,
                    IDCUENTA,
                    FECHAMODIFICACION,
                    USUMODIFICACION,
                    IDPERSONADEUDOR,
                    IDCUENTADEUDOR,
                    IMPTOTALNETO,
                    IMPTOTALIVA,
                    IMPTOTAL,
                    IMPTOTALANTICIPADO,
                    IMPTOTALPAGADOPORCAJA,
                    IMPTOTALPAGADOPORBANCO,
                    IMPTOTALPAGADO,
                    IMPTOTALPORPAGAR,
                    IMPTOTALCOMPENSADO,
                    IMPTOTALPAGADOSOLOCAJA,
                    IMPTOTALPAGADOSOLOTARJETA,
                    CTACLIENTE,
                    ESTADO,
                    IDMANDATO
                ) VALUES (
                    P_IDINSTITUCION,
                    M_FACTURA(IND_F).IDFACTURA,
                    V_FECHAREALGENERACION,
                    NULL,
                    'N',
                     M_FACTURA(IND_F).OBSERVACIONES,
                    P_IDSERIEFACTURACION,
                    P_IDPROGRAMACION,
                    M_FACTURA(IND_F).IDFORMAPAGO,
                    M_FACTURA(IND_F).IDPERSONA,
                    M_FACTURA(IND_F).IDCUENTA,
                    SYSDATE,
                    P_USUMODIFICACION,
                    M_FACTURA(IND_F).IDPERSONADEUDOR,
                    M_FACTURA(IND_F).IDCUENTADEUDOR,
                    M_FACTURA(IND_F).IMPTOTALNETO, --IMPTOTALNETO
                    M_FACTURA(IND_F).IMPTOTALIVA, -- IMPTOTALIVA
                    M_FACTURA(IND_F).IMPTOTAL, --IMPTOTAL
                    M_FACTURA(IND_F).IMPTOTALANTICIPADO, --IMPTOTALANTICIPADO
                    M_FACTURA(IND_F).IMPTOTALANTICIPADO, --IMPTOTALPAGADOPORCAJA
                    0.0, --IMPTOTALPAGADOPORBANCO
                    M_FACTURA(IND_F).IMPTOTALANTICIPADO, --IMPTOTALPAGADO
                    M_FACTURA(IND_F).IMPTOTALPORPAGAR, --IMPTOTALPORPAGAR
                    0.0, --IMPTOTALCOMPENSADO
                    0.0, --IMPTOTALPAGADOSOLOCAJA
                    0.0, --IMPTOTALPAGADOSOLOTARJETA
                    M_FACTURA(IND_F).CTACLIENTE,
                    7, -- EN REVISION
                    M_FACTURA(IND_F).IDMANDATO); -- 1: FacturaServicios; 2: FacturaProductos

                   --CGP (06-10-2017): Añadimos histórico
                    P_DATOSERROR :=  'GENERACIONFACTURACION: Insercion en el histórico FAC_HISTORICOFACTURA';
                    INSERT INTO FAC_HISTORICOFACTURA (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO,
											  IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
											  IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO)
										  SELECT IDINSTITUCION, IDFACTURA,SEQ_FAC_HISTORIAL.NEXTVAL,SYSDATE,USUMODIFICACION,1,IDFORMAPAGO,
										  IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
										  IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO,IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO
										  FROM FAC_FACTURA WHERE IDINSTITUCION=P_IDINSTITUCION AND IDFACTURA=M_FACTURA(IND_F).IDFACTURA;
                      
                      --CGP (15-11-2017): Añadimos histórico de anticipos 
                      IF(M_FACTURA(IND_F).IMPTOTALANTICIPADO >0) THEN
                           INSERT INTO FAC_HISTORICOFACTURA (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO,
      										 IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
      										 IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO)
      										 SELECT IDINSTITUCION, IDFACTURA,SEQ_FAC_HISTORIAL.NEXTVAL,SYSDATE,USUMODIFICACION,3,IDFORMAPAGO,
      										 IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
      										 IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO,IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, 9
      										 FROM FAC_FACTURA WHERE IDINSTITUCION=P_IDINSTITUCION AND IDFACTURA=M_FACTURA(IND_F).IDFACTURA;
                      END IF;
                      
                    --CGP Fin

                    -- JPT (08-01-2014): Esta insercion puede fallar cuando no exista el mandato (ya que tiene una foreign)
                    EXCEPTION WHEN OTHERS THEN
                        V_CODRETORNO:='-204';
                        P_DATOSERROR := F_SIGA_GETRECURSO_ETIQUETA('facturacion.generarFacturacion.mensaje.errorMandatos', p_Idioma) ||
                                    ' ' || M_FACTURA(IND_F).DEUDOR_NOMBRE ||
                                    ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                                    ' ' || M_FACTURA(IND_F).DEUDOR_ID ||
                                    ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeMandatoSinAsociar', p_Idioma);
                        RAISE E_ERROR;
            END;
        END LOOP;

        P_DATOSERROR :=  'GENERACIONFACTURACION: Lectura de la matriz de lineas facturas';
        FOR IND_LF IN 1..IND_LINEAFACTURA LOOP

            P_DATOSERROR :=  'GENERACIONFACTURACION: Insercion en FAC_LINEAFACTURA';
            INSERT INTO FAC_LINEAFACTURA (
                IDINSTITUCION,
                IDFACTURA,
                NUMEROLINEA,
                NUMEROORDEN,
                DESCRIPCION,
                CANTIDAD,
                PRECIOUNITARIO,
                IMPORTEANTICIPADO,
                IVA,
                IDTIPOIVA,
                CTAPRODUCTOSERVICIO,
                CTAIVA,
                FECHAMODIFICACION,
                USUMODIFICACION,
                IDFORMAPAGO
            ) VALUES (
                P_IDINSTITUCION,
                M_LINEAFACTURA(IND_LF).IDFACTURA,
                M_LINEAFACTURA(IND_LF).NUMEROLINEA,
                NVL(M_LINEAFACTURA(IND_LF).NUMEROORDEN, M_LINEAFACTURA(IND_LF).NUMEROLINEA),
                M_LINEAFACTURA(IND_LF).DESCRIPCION,
                M_LINEAFACTURA(IND_LF).CANTIDAD,
                M_LINEAFACTURA(IND_LF).PRECIOUNITARIO,
                M_LINEAFACTURA(IND_LF).IMPORTEANTICIPADO,
                M_LINEAFACTURA(IND_LF).IVA,
                M_LINEAFACTURA(IND_LF).IDTIPOIVA,
                M_LINEAFACTURA(IND_LF).CTAPRODUCTOSERVICIO,
                M_LINEAFACTURA(IND_LF).CTAIVA,
                SYSDATE,
                P_USUMODIFICACION,
                M_LINEAFACTURA(IND_LF).IDFORMAPAGO);

            -- Si es producto actualiza PYS_COMPRA
            IF (M_LINEAFACTURA(IND_LF).IDPETICION IS NOT NULL
                AND M_LINEAFACTURA(IND_LF).IDTIPOPRODUCTO IS NOT NULL
                AND M_LINEAFACTURA(IND_LF).IDPRODUCTO IS NOT NULL
                AND M_LINEAFACTURA(IND_LF).IDPRODUCTOINSTITUCION IS NOT NULL) THEN

                P_DATOSERROR :=  'GENERACIONFACTURACION: Actualizacion de la tabla PYS_COMPRA';
                UPDATE PYS_COMPRA
                SET NUMEROLINEA = M_LINEAFACTURA(IND_LF).NUMEROLINEA,
                    IDFACTURA = M_LINEAFACTURA(IND_LF).IDFACTURA,
                    FECHAMODIFICACION = SYSDATE,
                    USUMODIFICACION = P_USUMODIFICACION
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDPETICION = M_LINEAFACTURA(IND_LF).IDPETICION
                    AND IDTIPOPRODUCTO = M_LINEAFACTURA(IND_LF).IDTIPOPRODUCTO
                    AND IDPRODUCTO = M_LINEAFACTURA(IND_LF).IDPRODUCTO
                    AND IDPRODUCTOINSTITUCION = M_LINEAFACTURA(IND_LF).IDPRODUCTOINSTITUCION;
            END IF;
        END LOOP;

        P_DATOSERROR :=  'GENERACIONFACTURACION: Lectura de la matriz de facturacion suscripcion';
        FOR IND_FS IN 1..IND_FACTURACIONSUSCRIPCION LOOP

            -- JPT (12-01-2015) - Control de anticipos: Compruebo que tenga anticipos la persona del servicio
            IF (M_FACTURACIONSUSCRIPCION(IND_FS).CONT_ANTICIPOS > 0) THEN
                PROCESOANTICIPOSLETRADO(
                    P_IDINSTITUCION,
                    M_FACTURACIONSUSCRIPCION(IND_FS).IDPERSONA,
                    M_FACTURACIONSUSCRIPCION(IND_FS).IDTIPOSERVICIOS,
                    M_FACTURACIONSUSCRIPCION(IND_FS).IDSERVICIO,
                    M_FACTURACIONSUSCRIPCION(IND_FS).IDSERVICIOSINSTITUCION,
                    M_FACTURACIONSUSCRIPCION(IND_FS).IMPORTE,
                    M_FACTURACIONSUSCRIPCION(IND_FS).IDFACTURA,
                    M_FACTURACIONSUSCRIPCION(IND_FS).NUMEROLINEA,
                    P_USUMODIFICACION,
                    V_CODRETORNO,
                    P_DATOSERROR);
                IF (V_CODRETORNO <> TO_CHAR(0)) THEN
                    RAISE E_ERROR;
                END IF;
            END IF;

            P_DATOSERROR :=  'GENERACIONFACTURACION: Insercion en FAC_FACTURACIONSUSCRIPCION';
            INSERT INTO FAC_FACTURACIONSUSCRIPCION (
                IDINSTITUCION,
                IDFACTURA,
                NUMEROLINEA,
                IDTIPOSERVICIOS,
                IDSERVICIO,
                IDSERVICIOSINSTITUCION,
                IDSUSCRIPCION,
                IDFACTURACIONSUSCRIPCION,
                DESCRIPCION,
                FECHAINICIO,
                FECHAFIN,
                FECHAMODIFICACION,
                USUMODIFICACION
            ) VALUES (
                P_IDINSTITUCION,
                M_FACTURACIONSUSCRIPCION(IND_FS).IDFACTURA,
                M_FACTURACIONSUSCRIPCION(IND_FS).NUMEROLINEA,
                M_FACTURACIONSUSCRIPCION(IND_FS).IDTIPOSERVICIOS,
                M_FACTURACIONSUSCRIPCION(IND_FS).IDSERVICIO,
                M_FACTURACIONSUSCRIPCION(IND_FS).IDSERVICIOSINSTITUCION,
                M_FACTURACIONSUSCRIPCION(IND_FS).IDSUSCRIPCION,
                M_FACTURACIONSUSCRIPCION(IND_FS).IDFACTURACIONSUSCRIPCION,
                M_FACTURACIONSUSCRIPCION(IND_FS).DESCRIPCION,
                M_FACTURACIONSUSCRIPCION(IND_FS).FECHAINICIO,
                M_FACTURACIONSUSCRIPCION(IND_FS).FECHAFIN,
                SYSDATE,
                P_USUMODIFICACION);
        END LOOP;

        /* Tratamiento de las matrices */
        P_DATOSERROR :=  'GENERACIONFACTURACION: Borrado de las matrices';
        M_FACTURA.DELETE;
        M_LINEAFACTURA.DELETE;
        M_FACTURACIONSUSCRIPCION.DELETE;
        IND_FACTURA := NULL;
        IND_LINEAFACTURA := NULL;
        IND_FACTURACIONSUSCRIPCION := NULL;

        P_DATOSERROR :=  'GENERACIONFACTURACION: Actualizacion de los parametros de salida';
        V_CODRETORNO := TO_CHAR(0);
        P_CODRETORNO := V_CODRETORNO;
        P_DATOSERROR := NULL;

        EXCEPTION
            WHEN E_ERROR THEN
                ROLLBACK;
                P_CODRETORNO := V_CODRETORNO;

                /* Tratamiento de las matrices */
                M_FACTURA.DELETE;
                M_LINEAFACTURA.DELETE;
                M_FACTURACIONSUSCRIPCION.DELETE;
                IND_FACTURA := NULL;
                IND_LINEAFACTURA := NULL;
                IND_FACTURACIONSUSCRIPCION := NULL;

            WHEN OTHERS THEN
                ROLLBACK;
                P_CODRETORNO := TO_CHAR(SQLCODE) ;
                P_DATOSERROR := P_DATOSERROR || '-' || SQLERRM;

                /* Tratamiento de las matrices */
                M_FACTURA.DELETE;
                M_LINEAFACTURA.DELETE;
                M_FACTURACIONSUSCRIPCION.DELETE;
                IND_FACTURA := NULL;
                IND_LINEAFACTURA := NULL;
                IND_FACTURACIONSUSCRIPCION := NULL;
    END GENERACIONFACTURACION;

    /****************************************************************************************************************/
    /* Nombre: CONFIRMACIONFACTURACION */
    /* Descripcion: Confirmacion de la factura */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN  Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/11/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 04/10/2005 - Autor: Yolanda Garcia Espino - Configuracion del numero de factura */
    /* Version: 3.0 - Fecha Modificacion: 01/11/2014 - Autor: Jorge Paez Trivino */
    /*****************************************************************************************************************************/
    PROCEDURE CONFIRMACIONFACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDPROGRAMACION IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        -- Cursor de Facturas que se van a confirmar
        CURSOR C_FACTURAS IS
            SELECT IDFACTURA, IDPERSONA, IMPTOTALPORPAGAR, IDCUENTA, FECHAEMISION,IDFORMAPAGO,IDPERSONADEUDOR,IDCUENTADEUDOR,IMPTOTALANTICIPADO,
            IMPTOTALPAGADOPORCAJA,IMPTOTALPAGADOSOLOCAJA,IMPTOTALPAGADO,IMPTOTAL
            FROM FAC_FACTURA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDSERIEFACTURACION = P_IDSERIEFACTURACION
                AND IDPROGRAMACION = P_IDPROGRAMACION
                AND NUMEROFACTURA IS NULL -- RGG cambio para que no vuelva a confirmar facturas confirmadas
            ORDER BY TO_NUMBER(IDFACTURA);

        CURSOR C_LINEASFACTURACION (V_IDFACTURA FAC_FACTURA.IDFACTURA%TYPE) IS
            SELECT IMPORTEANTICIPADO,
                ROUND(CANTIDAD * PRECIOUNITARIO * (1+(IVA/100)),2) AS IMPORTE,
                NUMEROLINEA
            FROM FAC_LINEAFACTURA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDFACTURA = V_IDFACTURA;

        /* Declaracion de variables */
        V_PREFIJO ADM_CONTADOR.PREFIJO%TYPE;
        V_CONTADOR ADM_CONTADOR.CONTADOR%TYPE;
        V_CONTADORFAC ADM_CONTADOR.CONTADOR%TYPE;
        V_IDCONTADORSERIE ADM_CONTADOR.IDCONTADOR%TYPE;
        V_SUFIJO ADM_CONTADOR.SUFIJO%TYPE;
        V_NUMEROFACTURA FAC_FACTURA.NUMEROFACTURA%TYPE;
        V_NUEVOIDABONO FAC_ABONO.IDABONO%TYPE;
        RESTO FAC_LINEAFACTURA.IMPORTEANTICIPADO%TYPE;
        V_PREFIJOABONO ADM_CONTADOR.PREFIJO%TYPE;
        V_CONTADORABONO ADM_CONTADOR.CONTADOR%TYPE;
        V_SUFIJOABONO ADM_CONTADOR.SUFIJO%TYPE;
        V_NUMEROABONO FAC_ABONO.NUMEROABONO%TYPE;
        V_LONGITUDCONTADORABONO ADM_CONTADOR.LONGITUDCONTADOR%TYPE;
        V_LONGITUDCONTADORFAC ADM_CONTADOR.LONGITUDCONTADOR%TYPE;
        V_AUX_ESTADO FAC_FACTURA.ESTADO%TYPE;
        V_IMPORTE_POR_CAJA FAC_FACTURA.IMPTOTALPAGADOPORCAJA%TYPE;

    BEGIN
        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Obtencion del identificador del contador de la serie de facturacion';
        SELECT IDCONTADOR
            INTO V_IDCONTADORSERIE
        FROM FAC_SERIEFACTURACION
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDSERIEFACTURACION = P_IDSERIEFACTURACION;

        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Obtencion de los datos del contador de la serie de facturacion';
        SELECT PREFIJO, SUFIJO, CONTADOR, LONGITUDCONTADOR
            INTO V_PREFIJO, V_SUFIJO, V_CONTADOR, V_LONGITUDCONTADORFAC
        FROM ADM_CONTADOR
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDCONTADOR = V_IDCONTADORSERIE;

        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Obtencion de los datos del contador de FAC_ABONOS';
        SELECT CONTADOR, PREFIJO, SUFIJO, LONGITUDCONTADOR
            INTO V_CONTADORABONO, V_PREFIJOABONO, V_SUFIJOABONO, V_LONGITUDCONTADORABONO
        FROM ADM_CONTADOR
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDCONTADOR = 'FAC_ABONOS';

        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Apertura del cursor';
        FOR V_FACTURAS IN C_FACTURAS LOOP

            V_DATOSERROR := 'CONFIRMACIONFACTURACION: Calculo del numero de factura';
            V_CONTADORFAC := V_CONTADOR + 1;
            V_NUMEROFACTURA := V_PREFIJO || LPAD(V_CONTADORFAC,V_LONGITUDCONTADORFAC,0) || V_SUFIJO;

            V_DATOSERROR := 'CONFIRMACIONFACTURACION: Obtiene el importe automatico por caja';
            SELECT SUM(ROUND(FAC_LINEAFACTURA.CANTIDAD * FAC_LINEAFACTURA.PRECIOUNITARIO * (1 + FAC_LINEAFACTURA.IVA / 100), 2) - FAC_LINEAFACTURA.IMPORTEANTICIPADO)
                INTO V_IMPORTE_POR_CAJA
            FROM FAC_LINEAFACTURA,
                FAC_FORMAPAGOSERIE
            WHERE FAC_LINEAFACTURA.IDINSTITUCION = P_IDINSTITUCION
                AND FAC_LINEAFACTURA.IDFACTURA = V_FACTURAS.IDFACTURA
                AND FAC_FORMAPAGOSERIE.IDSERIEFACTURACION = P_IDSERIEFACTURACION
                AND FAC_FORMAPAGOSERIE.IDINSTITUCION = FAC_LINEAFACTURA.IDINSTITUCION
                AND FAC_FORMAPAGOSERIE.IDFORMAPAGO = FAC_LINEAFACTURA.IDFORMAPAGO;

            BEGIN

                IF (V_IMPORTE_POR_CAJA > 0) THEN
                    V_AUX_ESTADO := 9; -- PENDIENTE COBRO
                ELSIF V_FACTURAS.IMPTOTALPORPAGAR <= NVL(V_IMPORTE_POR_CAJA,0) THEN
                    V_AUX_ESTADO := 1; -- LA FACTURA ESTA PAGADA

                ELSIF (V_FACTURAS.IDCUENTA IS NOT NULL) THEN
                    V_AUX_ESTADO := 5; --PENDIENTE PAGAR POR BANCO

                ELSE
                    V_AUX_ESTADO := 2; -- PENDIENTE PAGAR POR CAJA
                END IF;

                V_DATOSERROR := 'CONFIRMACIONFACTURACION: Actualiza la factura';
                UPDATE FAC_FACTURA
                SET NUMEROFACTURA = V_NUMEROFACTURA,
                    ESTADO = V_AUX_ESTADO,
                    IMPTOTALPORPAGAR = IMPTOTALPORPAGAR - NVL(V_IMPORTE_POR_CAJA,0),
                    IMPTOTALPAGADOPORCAJA = IMPTOTALPAGADOPORCAJA + NVL(V_IMPORTE_POR_CAJA,0),
                    IMPTOTALPAGADOSOLOCAJA = IMPTOTALPAGADOSOLOCAJA + NVL(V_IMPORTE_POR_CAJA,0),
                    IMPTOTALPAGADO = IMPTOTALPAGADO + NVL(V_IMPORTE_POR_CAJA,0), -- Puede tener anticipos
                    FECHAMODIFICACION = SYSDATE,
                    USUMODIFICACION = NVL(P_USUMODIFICACION, 1)
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDFACTURA = V_FACTURAS.IDFACTURA;
                  -- CGP-INICIO (16/10/2017) pagos por caja, añadimos el estado y el histórico.
                   P_DATOSERROR :=  'CONFIRMACIONFACTURACION: Insercion en el histórico FAC_HISTORICOFACTURA';

                  INSERT INTO FAC_HISTORICOFACTURA (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO,
							    IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
							    IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO)
						      SELECT IDINSTITUCION, IDFACTURA,SEQ_FAC_HISTORIAL.NEXTVAL,SYSDATE,USUMODIFICACION,2,IDFORMAPAGO,
						      IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
						      IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO,IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO
						      FROM FAC_FACTURA WHERE IDINSTITUCION=P_IDINSTITUCION AND IDFACTURA=V_FACTURAS.IDFACTURA;


                  IF (V_IMPORTE_POR_CAJA > 0) THEN
                    V_DATOSERROR := 'CONFIRMACIONFACTURACION: Inserta los pagos por caja (pagos por caja automatico)';
                    INSERT INTO FAC_PAGOSPORCAJA (IDINSTITUCION, IDFACTURA, IDPAGOPORCAJA, FECHA, CONTABILIZADO, FECHAMODIFICACION, USUMODIFICACION, IMPORTE, TARJETA)
                    VALUES (P_IDINSTITUCION, V_FACTURAS.IDFACTURA, 1, V_FACTURAS.FECHAEMISION, 'N', SYSDATE, 0, V_IMPORTE_POR_CAJA, 'N');

                        IF V_FACTURAS.IMPTOTALPORPAGAR <= NVL(V_IMPORTE_POR_CAJA,0) THEN
                           V_AUX_ESTADO := 1; -- LA FACTURA ESTA PAGADA
                        ELSE
                           V_AUX_ESTADO := 2; -- PENDIENTE PAGAR POR CAJA
                      END IF;

                       UPDATE FAC_FACTURA
                       SET NUMEROFACTURA = V_NUMEROFACTURA,
                       ESTADO = V_AUX_ESTADO
                      WHERE IDINSTITUCION = P_IDINSTITUCION
                          AND IDFACTURA = V_FACTURAS.IDFACTURA;

                      INSERT INTO FAC_HISTORICOFACTURA (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO,
									    IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
									    IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO,IDPAGOPORCAJA)
								      SELECT IDINSTITUCION, IDFACTURA,SEQ_FAC_HISTORIAL.NEXTVAL,SYSDATE,USUMODIFICACION,4,IDFORMAPAGO,
								      IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA,
								      IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO,IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO,1
								      FROM FAC_FACTURA WHERE IDINSTITUCION=P_IDINSTITUCION AND IDFACTURA=V_FACTURAS.IDFACTURA;

                END IF;
                       --CGP FIN
                EXCEPTION
                    WHEN OTHERS THEN
                        V_CODRETORNO:='-205';
                        V_DATOSERROR := SQLERRM;
                        RAISE E_ERROR;
            END;

            -- RGG 02/01/2007 cambio para importe anticipado, hay que ver si el anticipado es mayor que el pagado para cada linea y en tal caso hacer un abono.
            FOR V_LINEASFACTURACION IN C_LINEASFACTURACION(V_FACTURAS.IDFACTURA) LOOP

                 IF (ABS(V_LINEASFACTURACION.IMPORTEANTICIPADO) > ABS(V_LINEASFACTURACION.IMPORTE)) THEN

                    -- calculamos la diferencia
                    resto := V_LINEASFACTURACION.IMPORTEANTICIPADO - V_LINEASFACTURACION.IMPORTE;

                    V_DATOSERROR := 'CONFIRMACIONFACTURACION: Obtenemos un nuevo identificador de abono';
                    SELECT NVL(MAX(IDABONO) + 1,0) AS NUEVOIDABONO
                        INTO V_NUEVOIDABONO
                    FROM FAC_ABONO
                    WHERE IDINSTITUCION = P_IDINSTITUCION;

                    V_DATOSERROR := 'CONFIRMACIONFACTURACION: Calculo del numero de abono';
                    V_CONTADORABONO := V_CONTADORABONO + 1;
                    v_numeroAbono := V_PREFIJOABONO || LPAD(V_CONTADORABONO,v_longitudcontadorAbono,0) || V_SUFIJOABONO;

                    BEGIN
                        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Creamos abono';
                       INSERT INTO FAC_ABONO (IDINSTITUCION, IDABONO, MOTIVOS, FECHA, CONTABILIZADA,IDPERSONA, USUMODIFICACION, FECHAMODIFICACION,IDCUENTA,IDFACTURA,IDPAGOSJG, NUMEROABONO)
                       VALUES (
                        P_IDINSTITUCION,
                        V_NUEVOIDABONO,
                        'Devolucion de importe anticipado excesivo. Factura ' || V_NUMEROFACTURA || '.',
                        SYSDATE,
                        'N',
                        V_FACTURAS.IDPERSONA,
                        NVL(P_USUMODIFICACION,1),
                        SYSDATE,
                        NULL,
                        V_FACTURAS.IDFACTURA,
                        NULL,
                        v_numeroAbono);

                        EXCEPTION
                            WHEN OTHERS THEN
                                V_CODRETORNO:='-206';
                                V_DATOSERROR:='Error al realizar la inserccion de un abono';
                                RAISE E_ERROR;
                    END;

                    BEGIN
                        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Actualizamos el contador de FAC_ABONOS';
                        UPDATE ADM_CONTADOR
                        SET CONTADOR = V_CONTADORABONO
                        WHERE ADM_CONTADOR.IDINSTITUCION = P_IDINSTITUCION
                            AND ADM_CONTADOR.IDCONTADOR = 'FAC_ABONOS';

                        EXCEPTION
                            WHEN OTHERS THEN
                                V_DATOSERROR:='Error al actualizar el contador en ADM_CONTADOR';
                                V_CODRETORNO:='-207';
                                RAISE E_ERROR;
                    END;

                    V_DATOSERROR := 'CONFIRMACIONFACTURACION: Creamos la linea de abono';
                    INSERT INTO FAC_LINEAABONO (IDINSTITUCION, IDABONO, NUMEROLINEA, DESCRIPCIONLINEA,CANTIDAD, PRECIOUNITARIO, IVA, USUMODIFICACION, FECHAMODIFICACION, IDFACTURA, LINEAFACTURA)
                    VALUES (
                        P_IDINSTITUCION,
                        V_NUEVOIDABONO,
                        1,
                        'Devolucion de importe anticipado excesivo. Factura ' || V_NUMEROFACTURA || '.',
                        1,
                        resto,
                        0,
                        NVL(P_USUMODIFICACION,1),
                        SYSDATE,
                        V_FACTURAS.IDFACTURA,
                        V_LINEASFACTURACION.NUMEROLINEA);

                    V_DATOSERROR := 'CONFIRMACIONFACTURACION: Actualizamos la linea de factura, con el importe anticipado que se ha cobrado realmente';
                    UPDATE FAC_LINEAFACTURA
                    SET IMPORTEANTICIPADO = V_LINEASFACTURACION.IMPORTE,
                        FECHAMODIFICACION = SYSDATE,
                        USUMODIFICACION = NVL(P_USUMODIFICACION,1)
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDFACTURA = V_FACTURAS.IDFACTURA
                        AND NUMEROLINEA = V_LINEASFACTURACION.NUMEROLINEA;
                 END IF;
            END LOOP;
            -- fin cambios RGG 02/01/2007

            V_DATOSERROR := 'CONFIRMACIONFACTURACION: Calculo del nuevo contador';
            V_CONTADORFAC := LPAD(V_CONTADORFAC, V_LONGITUDCONTADORFAC, 0);
            V_CONTADOR := V_CONTADORFAC;
        END LOOP;

        -- RGG 11/09/2007 CAMBIO PARA ACTUALIZAR EL CONTADOR
        IF V_CONTADORFAC IS NOT NULL THEN
            V_DATOSERROR := 'CONFIRMACIONFACTURACION: Actualizamos el contador de la serie de facturación';
            UPDATE ADM_CONTADOR
            SET CONTADOR = V_CONTADORFAC
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDCONTADOR = V_IDCONTADORSERIE;

        ELSE
               V_DATOSERROR:='CONFIRMACIONFACTURACION: No tiene facturas que confirmar';
                V_CODRETORNO:='-208';
                RAISE E_ERROR;
        END IF;

        V_DATOSERROR := 'CONFIRMACIONFACTURACION: Actualizacion de los parametros de salida';
        P_CODRETORNO := 0;
        P_DATOSERROR := V_DATOSERROR;

        EXCEPTION
            WHEN E_ERROR THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;

            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := V_DATOSERROR;
    END CONFIRMACIONFACTURACION;

    /*****************************************************************************************************************************/
    /* Nombre: ELIMINARFACTURACION  */
    /* Descripcion: Eliminacion de una facturacion */
    /* */
    /* P_IDINSTITUCION - IN - Identificador del colegio - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN  Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario de modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Modificacion: 01/09/2015 - Autor Jorge Paez Trivino - Cambios encontrados al realizar la documentacion del proceso de facturacion */
    /*****************************************************************************************************************************/
    PROCEDURE ELIMINARFACTURACION(
        P_IDINSTITUCION      IN NUMBER,
        P_IDSERIEFACTURACION IN NUMBER,
        P_IDPROGRAMACION     IN NUMBER,
        P_USUMODIFICACION    IN NUMBER,
        P_CODRETORNO         OUT VARCHAR2,
        P_DATOSERROR         OUT VARCHAR2) IS

    estadoEjecucion Number;
    e_Error1 Exception;
    e_Error2 Exception;

  Begin
    v_Datoserror := 'ELIMINARFACTURACION: Comprobaciones de eliminacion para institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Begin
      Select idestadoconfirmacion
        Into estadoEjecucion
        From Fac_Facturacionprogramada
       Where Idinstitucion = p_Idinstitucion
         And Idseriefacturacion = p_Idseriefacturacion
         And Idprogramacion = p_Idprogramacion;
    Exception
      When No_Data_Found Then
        -- NO EXISTE LA FACTURACION
        Raise e_Error1;
    End;

    If estadoEjecucion = 19 OR estadoEjecucion =17 Then
      -- LA FACTURACION ESTA EN EJECUCION
      Raise e_Error2;
    End If;
     -- CGP - R1709_0035
     v_Datoserror := 'ELIMINARFACTURACION: Se elimina histórico de la factura institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    DELETE FAC_HISTORICOFACTURA 
           WHERE IDINSTITUCION=p_Idinstitucion 
                 AND  Idfactura In
                 (Select Idfactura
                    From Fac_Factura
                   Where Idinstitucion = p_Idinstitucion
                     And Idseriefacturacion = p_Idseriefacturacion
                     And Idprogramacion = p_Idprogramacion); 
    -- CGP -FIN                               
    v_Datoserror := 'ELIMINARFACTURACION: Liberar Compra de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Update Pys_Compra
       Set Idfactura         = Null,
           Numerolinea       = Null,
           Usumodificacion   = p_Usumodificacion,
           Fechamodificacion = Sysdate
     Where Idinstitucion = p_Idinstitucion
       And Idfactura In
           (Select Idfactura
              From Fac_Factura
             Where Idinstitucion = p_Idinstitucion
               And Idseriefacturacion = p_Idseriefacturacion
               And Idprogramacion = p_Idprogramacion);

    v_Datoserror := 'ELIMINARFACTURACION: Eliminar FacturacionSuscripcion de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Delete Fac_Facturacionsuscripcion
     Where Idinstitucion = p_Idinstitucion
       And Idfactura In
           (Select Idfactura
              From Fac_Factura
             Where Idinstitucion = p_Idinstitucion
               And Idseriefacturacion = p_Idseriefacturacion
               And Idprogramacion = p_Idprogramacion);

    v_Datoserror := 'ELIMINARFACTURACION: Eliminar LineaAnticipo de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Delete Pys_Lineaanticipo
     Where Idinstitucion = p_Idinstitucion
       And Idfactura In
           (Select Idfactura
              From Fac_Factura
             Where Idinstitucion = p_Idinstitucion
               And Idseriefacturacion = p_Idseriefacturacion
               And Idprogramacion = p_Idprogramacion);

    v_Datoserror := 'ELIMINARFACTURACION: Eliminar LineaFactura de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Delete Fac_Lineafactura
     Where Idinstitucion = p_Idinstitucion
       And Idfactura In
           (Select Idfactura
              From Fac_Factura
             Where Idinstitucion = p_Idinstitucion
               And Idseriefacturacion = p_Idseriefacturacion
               And Idprogramacion = p_Idprogramacion);

    v_Datoserror := 'ELIMINARFACTURACION: Eliminar Factura de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Delete Fac_Factura
     Where Idinstitucion = p_Idinstitucion
       And Idseriefacturacion = p_Idseriefacturacion
       And Idprogramacion = p_Idprogramacion;

    v_Datoserror := 'ELIMINARFACTURACION: Restaurar fecha generacion de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    Delete Fac_Facturacionprogramada
     Where Idinstitucion = p_Idinstitucion
       And Idseriefacturacion = p_Idseriefacturacion
       And Idprogramacion = p_Idprogramacion;

    v_Datoserror := 'ELIMINARFACTURACION: Fin del proceso de la institucion ' ||
                    To_Char(p_Idinstitucion) || ', serie de facturacion ' ||
                    To_Char(p_Idseriefacturacion) || ' y programacion ' ||
                    To_Char(p_Idprogramacion);
    p_Codretorno := To_Char(Sqlcode);
    p_Datoserror := v_Datoserror;

  Exception
    When e_Error1 Then
      p_Codretorno := '-1';
      p_Datoserror := 'NO EXISTE LA FACTURACION A ELIMINAR';

    When e_Error2 Then
      p_Codretorno := '-2';
      p_Datoserror := 'messages.facturacion.generacionEnProceso';

    When Others Then
      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := v_Datoserror;
  End; -- Procedure ELIMINARFACTURACION
END PKG_SIGA_FACTURACION;
/