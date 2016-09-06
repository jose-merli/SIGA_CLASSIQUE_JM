CREATE OR REPLACE PACKAGE PKG_SIGA_CARGOS IS

    TYPE REG_DIRECCION IS RECORD(
        DOMICILIO CEN_DIRECCIONES.DOMICILIO%TYPE,
        CODIGOPOSTAL CEN_DIRECCIONES.CODIGOPOSTAL%TYPE,
        PAIS_NOMBRE CEN_PAIS.NOMBRE%TYPE,
        PAIS_ISO CEN_PAIS.COD_ISO%TYPE,
        PAIS_ID CEN_PAIS.IDPAIS%TYPE,
        PROVINCIA_NOMBRE CEN_PROVINCIAS.NOMBRE%TYPE,
        PROVINCIA_ID CEN_PROVINCIAS.IDPROVINCIA%TYPE,
        POBLACION_NOMBRE CEN_POBLACIONES.NOMBRE%TYPE,
        POBLACION_ID CEN_POBLACIONES.IDPOBLACION%TYPE
    );

    TYPE REG_ACREEDOR IS RECORD(
        NOMBRE CEN_INSTITUCION.NOMBRE%TYPE,
        IDPERSONA CEN_INSTITUCION.IDPERSONA%TYPE,
        NIF CEN_PERSONA.NIFCIF%TYPE,
        CODIGOBANCO FAC_BANCOINSTITUCION.BANCOS_CODIGO%TYPE, -- CODIGOBANCO
        ENTIDADBANCO FAC_BANCOINSTITUCION.COD_BANCO%TYPE, -- ENTIDADBANCO
        OFICINABANCO FAC_BANCOINSTITUCION.COD_SUCURSAL%TYPE,
        BANCO_IBAN FAC_BANCOINSTITUCION.IBAN%TYPE,
        BANCO_BIC CEN_BANCOS.BIC%TYPE,
        SUFIJO FAC_SUFIJO.SUFIJO%TYPE,
        IDSUFIJO FAC_SUFIJO.IDSUFIJO%TYPE,
        CONFIGFICHEROSESQUEMA FAC_BANCOINSTITUCION.CONFIGFICHEROSESQUEMA%TYPE, -- 0=ficheroCORE+COR1+B2B; 1=ficheroCORE+COR1 + ficheroB2B; 2=ficheroCORE + ficheroCOR1 + ficheroB2B
        CONFIGFICHEROSSECUENCIA FAC_BANCOINSTITUCION.CONFIGFICHEROSSECUENCIA%TYPE, -- 0=ficheroFRST+RCUR; 1=ficheroFRST + ficheroRCUR; 2=ficheroTodoRCUR
        CONFIGLUGARESQUEMASECUENCIA FAC_BANCOINSTITUCION.CONFIGLUGARESQUEMASECUENCIA%TYPE, -- 0=bloqueAcreedor; 1=registrosIndividuales
        CONFIGCONCEPTOAMPLIADO FAC_BANCOINSTITUCION.CONFIGCONCEPTOAMPLIADO%TYPE,-- 0=Concepto140; 1=concepto640
        TIPOSFICHEROS NUMBER,-- -1=Error; 0=Txt; 1=Txt+Xml; 2=Xml
        IDPERSONASEPAINSTITUCION VARCHAR2(35), -- Identificador SEPA de la institucion
        DIRECCION REG_DIRECCION
    );

    TYPE REG_FACTURA IS RECORD(
        IDINSTITUCION CEN_MANDATOS_CUENTASBANCARIAS.IDINSTITUCION%TYPE,
        IDMANDATO CEN_MANDATOS_CUENTASBANCARIAS.IDMANDATO%TYPE,
        IDPERSONA FAC_FACTURA.IDPERSONA%TYPE,
        IDCUENTA FAC_FACTURA.IDCUENTA%TYPE,
        IDFACTURA FAC_FACTURA.IDFACTURA%TYPE,
        BANCO_IBAN CEN_CUENTASBANCARIAS.IBAN%TYPE,
        BANCO_BIC CEN_BANCOS.BIC%TYPE,
        CODIGOREFERENCIA FAC_FACTURA.IDFACTURA%TYPE,
        NUMEROFACTURA FAC_FACTURA.NUMEROFACTURA%TYPE,
        IMPORTE NUMBER,
        REFMANDATOSEPA CEN_MANDATOS_CUENTASBANCARIAS.REFMANDATOSEPA%TYPE,
        ESQUEMA CEN_MANDATOS_CUENTASBANCARIAS.ESQUEMA%TYPE, -- 0:CORE; 1:COR1; 2:B2B
        FECHAUSO CEN_MANDATOS_CUENTASBANCARIAS.FECHAUSO%TYPE,
        AUTORIZACIONB2B CEN_MANDATOS_CUENTASBANCARIAS.AUTORIZACIONB2B%TYPE,
        DEUDOR_ID CEN_MANDATOS_CUENTASBANCARIAS.DEUDOR_ID%TYPE,
        DEUDOR_NOMBRE CEN_CUENTASBANCARIAS.TITULAR%TYPE,
        DEUDOR_DIRECCION REG_DIRECCION,
        FIRMA_FECHA CEN_MANDATOS_CUENTASBANCARIAS.FIRMA_FECHA%TYPE,
        SECUENCIA NUMBER, -- 0:FRST; 1:RCUR
        SECUENCIAREAL NUMBER -- 0:FRST; 1:RCUR
    );
    TYPE TAB_FACTURA IS TABLE OF REG_FACTURA INDEX BY BINARY_INTEGER;

    TYPE REG_FACTURAS IS RECORD(
        CONTADORFACTURASCOREFRST NUMBER := 0,
        CONTADORFACTURASCORERCUR NUMBER := 0,
        CONTADORFACTURASCOR1FRST NUMBER := 0,
        CONTADORFACTURASCOR1RCUR NUMBER := 0,
        CONTADORFACTURASB2BFRST NUMBER := 0,
        CONTADORFACTURASB2BRCUR NUMBER := 0,
        M_FACTURASCOREFRST TAB_FACTURA,
        M_FACTURASCORERCUR TAB_FACTURA,
        M_FACTURASCOR1FRST TAB_FACTURA,
        M_FACTURASCOR1RCUR TAB_FACTURA,
        M_FACTURASB2BFRST TAB_FACTURA,
        M_FACTURASB2BRCUR TAB_FACTURA,
        FECHAPRESENTACION DATE,
        FECHACARGO_COREFRST DATE,
        FECHACARGO_CORERCUR DATE,
        FECHACARGO_COR1 DATE,
        FECHACARGO_B2B DATE,
        IDDISQUETECARGOS FAC_DISQUETECARGOS.IDDISQUETECARGOS%TYPE
    );

    TYPE REG_BLOQUEACREEDOR IS RECORD(
        CONTADORFACTURAS NUMBER := 0,
        IMPORTETOTAL NUMBER := 0,
        CONTADORFACTURASTXT NUMBER := 0,
        IMPORTETOTALTXT NUMBER := 0,
        M_FACTURAS TAB_FACTURA,
        IDDISQUETECARGOS FAC_DISQUETECARGOS.IDDISQUETECARGOS%TYPE,
        FECHAPRESENTACION VARCHAR2(8), -- YYYYMMDD
        FECHACARGO VARCHAR2(8) -- YYYYMMDD
    );
    TYPE TAB_BLOQUEACREEDOR IS TABLE OF REG_BLOQUEACREEDOR INDEX BY BINARY_INTEGER;

    TYPE REG_FICHERO IS RECORD(
        FECHAPRESENTACION VARCHAR2(8), -- YYYYMMDD
        IDFICHERO VARCHAR2(35),
        IDDISQUETECARGOS FAC_DISQUETECARGOS.IDDISQUETECARGOS%TYPE,
        NOMBREFICHEROTXT FAC_DISQUETECARGOS.NOMBREFICHERO%TYPE,
        NOMBREFICHEROXML FAC_DISQUETECARGOS.NOMBREFICHERO%TYPE,
        CONTADORBLOQUESACREEDOR NUMBER := 0,
        M_BLOQUEACREEDOR TAB_BLOQUEACREEDOR,
        CONTADORBLOQUESACREEDORTXT NUMBER := 0,
        M_BLOQUEACREEDORTXT TAB_BLOQUEACREEDOR
    );
    TYPE TAB_FICHERO IS TABLE OF REG_FICHERO INDEX BY BINARY_INTEGER;

    TYPE REG_FIED_DEVO IS RECORD(
        ID_FACTURA FAC_FACTURA.IDFACTURA%TYPE,
        ID_RECIBO FAC_FACTURAINCLUIDAENDISQUETE.IDRECIBO%TYPE,
        IMPORTE_DEVO FAC_FACTURAINCLUIDAENDISQUETE.IMPORTE%TYPE,
        MOTIVO_DEVO FAC_MOTIVODEVOLUCION.CODIGO%TYPE,
        IDDISQUETECARGOS FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS%TYPE,
        IDFACTURAINCLUIDAENDISQUETE FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURAINCLUIDAENDISQUETE%TYPE
    );
    TYPE TAB_FIED_DEVO IS TABLE OF REG_FIED_DEVO INDEX BY BINARY_INTEGER;

    TYPE REG_DISQ_DEVO IS RECORD(
        CODIGO_BANCO FAC_DISQUETEDEVOLUCIONES.BANCOS_CODIGO%TYPE,
        ID_DISQUETE_DEVO FAC_DISQUETEDEVOLUCIONES.IDDISQUETEDEVOLUCIONES%TYPE,
        M_FIED_DEVO TAB_FIED_DEVO,
        CONT_M_FIED_DEVO NUMBER
    );
  TYPE TAB_DISQ_DEVO IS TABLE OF REG_DISQ_DEVO INDEX BY BINARY_INTEGER;

  TYPE TAB_REGISTROS IS TABLE OF VARCHAR2(1000) INDEX BY BINARY_INTEGER;

    /****************************************************************************************************************/
    /* Nombre: Presentacion */
    /* Descripcion: Generacion del fichero para Adeudo de Domiciliaciones */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN - Identificador de la programacion - NUMBER */
    /* P_FECHAPRESENTACION - IN - Fecha de presentacion del fichero en el banco (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOFRST - IN - Fecha de cargo del fichero para facturas FRST (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGORCUR - IN - Fecha de cargo del fichero para facturas RCUR (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOCOR1 - IN - Fecha de cargo del fichero para facturas COR1 (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOB2B - IN - Fecha de cargo del fichero para facturas B2B (YYYYMMDD) - VARCHAR2 (8) */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_NFICHEROS - OUT - Devuelve el numero de ficheros bancarios generados - VARCHAR2(10)   */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 18/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 24/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: 4.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
     PROCEDURE Presentacion(
        p_Idinstitucion IN Number,
        p_Idseriefacturacion IN Number,
        p_Idprogramacion IN Number,
        p_FechaPresentacion IN Varchar2,
        p_FechaCargoFRST IN Varchar2,
        p_FechaCargoRCUR IN Varchar2,
        p_FechaCargoCOR1 IN Varchar2,
        p_FechaCargoB2B IN Varchar2,
        p_Pathfichero IN Varchar2,
        p_Usumodificacion IN Number,
        p_Idioma IN Number,
        p_Nficheros OUT Varchar2,
        p_Codretorno OUT Varchar2,
        p_Datoserror OUT Varchar2);

 /****************************************************************************************************************/
    /* Nombre: Regenerar_Presentacion */
    /* Descripcion: Regeneracion de los ficheros de adeudo (SEPA) */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDDISQUETECARGOS - IN - Identificador del disquete de cargos - NUMBER */
    /* P_FECHAPRESENTACION - IN - Fecha de presentacion del fichero en el banco (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOFRST - IN - Fecha de cargo del fichero para facturas FRST (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGORCUR - IN - Fecha de cargo del fichero para facturas RCUR (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOCOR1 - IN - Fecha de cargo del fichero para facturas COR1 (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOB2B - IN - Fecha de cargo del fichero para facturas B2B (YYYYMMDD) - VARCHAR2 (8) */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
     PROCEDURE Regenerar_Presentacion(
        p_Idinstitucion IN NUMBER,
        p_IdDisqueteCargos IN NUMBER,
        p_FechaPresentacion IN VARCHAR2,
        p_FechaCargoFRST IN VARCHAR2,
        p_FechaCargoRCUR IN VARCHAR2,
        p_FechaCargoCOR1 IN VARCHAR2,
        p_FechaCargoB2B IN VARCHAR2,
        p_Pathfichero IN VARCHAR2,
        p_Idioma IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2);

    /****************************************************************************************************************/
    /* Nombre: DEVOLUCIONES */
    /* Descripcion: Generacion del fichero para Presentacion de Devoluciones */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - DATE */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_NOMBREFICHERO - IN - Nombre del fichero - VARCHAR2(80) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* P_FECHADEVOLUCION - OUT - Devuelve la fecha de devolucion con formato YYYY/MM/DD HH24:MI:SS - VARCHAR2(19) */
    /* */
    /* Version: 1.0 - Fecha Creacion: 25/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 17/01/2014 - Autor: Jorge Paez Trivino */
  /****************************************************************************************************************/
  Procedure Devoluciones(p_Idinstitucion   In Number,
                         p_Pathfichero     In Varchar2,
                         p_Nombrefichero   In Varchar2,
                         p_Idioma          In Number,
                         p_Usumodificacion In Number,
                         p_Codretorno      Out Varchar2,
                         p_Datoserror      Out Varchar2,
                        p_FechaDevolucion Out VARCHAR2);

    /****************************************************************************************************************/
    /* Nombre: InsertarMandatos */
    /* Descripcion: Procedimiento que inserta dos mandatos nuevos para una cuenta de cargo, uno para productos y otro para servicios */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_IDCUENTA - IN - Identificador de la cuenta - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 07/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    Procedure InsertarMandatos (
        p_idInstitucion IN NUMBER,
        p_idPersona IN NUMBER,
        p_idCuenta IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2);

    /****************************************************************************************************************/
    /* Nombre: RevisarAnexos */
    /* Descripcion: Procedimiento que revisa los anexos y mandatos de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 07/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    Procedure RevisarAnexos (
        p_idInstitucion IN NUMBER,
        p_idPersona IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_idioma IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2);

    /****************************************************************************************************************/
    /* Nombre: RevisarErroresSEPA */
    /* Descripcion: Procedimiento que revisa todos los posibles errores de facturacion de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_TOTALERROR - OUT - Retorno con todos los mensajes de error - VARCHAR2(4000) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)   */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    PROCEDURE RevisarErroresSEPA (
        p_idInstitucion IN NUMBER,
        p_idPersona IN NUMBER,
        p_Pathfichero IN VARCHAR2,
        p_totalError OUT VARCHAR2,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2);

    /****************************************************************************************************************/
    /* Nombre: F_RevisarErroresPersonaSEPA */
    /* Descripcion: Funcion que revisa todos los posibles errores de facturacion de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 30/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    FUNCTION F_RevisarErroresPersonaSEPA (
            p_idInstitucion IN NUMBER,
            p_idPersona IN NUMBER
        ) RETURN VARCHAR2;

    /****************************************************************************************************************/
    /* Nombre: F_RevisarCaracteresSEPA*/
    /* Descripcion: Revisar y transformar los caracteres del registro, para mostrar solo los caracteres permitidos por SEPA */
    /* */
    /* P_REGISTRO - IN - Registro que se va a insertar en el fichero - VARCHAR2(601) */
    /* */
    /* Version: 1.0 - Fecha Creacion: 27/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    Function F_RevisarCaracteresSEPA(
        p_Registro IN VARCHAR2
    ) RETURN VARCHAR2;


    /****************************************************************************************************************/
    /* Nombre: DevolucionesManuales */
    /* Descripcion: Generacion de devoluciones manuales */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - DATE */
    /* P_LISTAFACTURAS - IN - Lista de facturas devueltas manualmente (idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...) - CLOB */
    /* P_FECHADEVOLUCION - IN - Fecha de devolucion manual (YYYYMMDD) - VARCHAR2(8) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* P_LISTAIDDISQUETESDEVOLUCION - OUT - Lista de identificadores de los disquetes de devoluciones generados - VARCHAR2(4000) */
    /* */
    /* Version: 1.0 - Fecha Modificacion: 23/06/2014 - Autor: Jorge Paez Trivino */
  /****************************************************************************************************************/
    Procedure DevolucionesManuales (
        p_Idinstitucion IN NUMBER,
        p_ListaFacturas IN CLOB,
        p_FechaDevolucion IN VARCHAR2,
        p_Idioma IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2,
        p_ListaIdDisquetesDevolucion OUT VARCHAR2);

    FUNCTION ObtenerDireccionFacturacion(
        p_IdInstitucion IN CEN_DIRECCIONES.IDINSTITUCION%type,
        p_idPersona IN CEN_DIRECCIONES.IDPERSONA%type
    ) RETURN REG_DIRECCION;

END PKG_SIGA_CARGOS;
/
CREATE OR REPLACE PACKAGE BODY PKG_SIGA_CARGOS IS

    /****************************************************************************************************************/
    /* Nombre: F_RevisarCaracteresSEPA*/
    /* Descripcion: Revisar y transformar los caracteres del registro, para mostrar solo los caracteres permitidos por SEPA */
    /* */
    /* P_REGISTRO - IN - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* */
    /* Version: 1.0 - Fecha Creacion: 27/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
        - Se elimina la comilla simple, porque en la gestion del xml se traduce automaticamente y hay problemas de dimension en los campos.
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    Function F_RevisarCaracteresSEPA(
        p_Registro IN VARCHAR2
    ) RETURN VARCHAR2 IS

        v_Registro VARCHAR2(4000) := p_Registro;

    BEGIN
        -- Transformacion de tildes y diereis
        v_Registro := REGEXP_REPLACE(v_Registro, '[ÁÀÄ]', 'A');
        v_Registro := REGEXP_REPLACE(v_Registro, '[ÉÈË]', 'E');
        v_Registro := REGEXP_REPLACE(v_Registro, '[ÍÌÏ]', 'I');
        v_Registro := REGEXP_REPLACE(v_Registro, '[ÓÒÖ]', 'O');
        v_Registro := REGEXP_REPLACE(v_Registro, '[ÚÙÜ]', 'U');
        v_Registro := REGEXP_REPLACE(v_Registro, '[áàä]', 'a');
        v_Registro := REGEXP_REPLACE(v_Registro, '[éèë]', 'e');
        v_Registro := REGEXP_REPLACE(v_Registro, '[íìï]', 'i');
        v_Registro := REGEXP_REPLACE(v_Registro, '[óòö]', 'o');
        v_Registro := REGEXP_REPLACE(v_Registro, '[úùü]', 'u');

        -- Transformacion de eñes y cediña
        v_Registro := REPLACE(v_Registro, 'Ñ', 'N');
        v_Registro := REPLACE(v_Registro, 'ñ', 'n');
        v_Registro := REPLACE(v_Registro, 'Ç', 'C');
        v_Registro := REPLACE(v_Registro, 'ç', 'c');

        -- Transformacion de caracteres especiales
        v_Registro := REGEXP_REPLACE(v_Registro, '[^-ABCDEFGHIJ-NOP-TUV-Zabcdefghij-nop-tuv-z0-9/?:().,+]', ' ');

        RETURN v_Registro;
    END F_RevisarCaracteresSEPA;

    /****************************************************************************************************************/
    /* Nombre: ObtenerDireccionFacturacion*/
    /* Descripcion: Obtiene la direccion de facturacion de la persona para SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion de la persona - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 27/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    FUNCTION ObtenerDireccionFacturacion(
        p_IdInstitucion IN CEN_DIRECCIONES.IDINSTITUCION%type,
        p_idPersona IN CEN_DIRECCIONES.IDPERSONA%type
    ) RETURN REG_DIRECCION IS

        v_idDireccion CEN_DIRECCIONES.IDDIRECCION%type;
        r_Direccion CEN_DIRECCIONES%rowtype;
        v_poblacion CEN_POBLACIONES.NOMBRE%type;
        v_provincia CEN_PROVINCIAS.NOMBRE%type;
        v_pais CEN_PAIS.NOMBRE%type;
        v_codigoPaisISO CEN_PAIS.COD_ISO%type;
        v_Direccion REG_DIRECCION;

    BEGIN

        -- Obtengo el identificador de la direccion de facturacion
        v_idDireccion := F_SIGA_GETIDDIRECCION_TIPOS2(p_IdInstitucion, p_idPersona, 8, NULL);

        -- Compruebo si tiene direccion de facturacion
        IF (v_idDireccion IS NOT NULL) THEN

            -- Obtengo los datos de la direccion
            SELECT *
                INTO r_Direccion
            FROM CEN_DIRECCIONES
            WHERE IDINSTITUCION = p_IdInstitucion
                AND IDPERSONA = p_idPersona
                AND IDDIRECCION = v_idDireccion;

            -- Compruebo si tiene poblacion extranjera
            IF (r_Direccion.poblacionextranjera IS NULL) THEN

                -- Compruebo si tiene poblacion
                IF (r_Direccion.idpoblacion IS NULL) THEN
                    v_poblacion := NULL;

                ELSE -- Tiene poblacion
                    SELECT nombre
                        INTO v_poblacion
                    FROM CEN_POBLACIONES
                    WHERE idpoblacion = r_Direccion.idpoblacion;
                END IF;

                -- Compruebo si tiene provincia
                IF (r_Direccion.idprovincia IS NULL) THEN
                    v_provincia := NULL;

                ELSE -- Tiene provincia
                    SELECT nombre
                        INTO v_provincia
                    FROM CEN_PROVINCIAS
                    WHERE idprovincia = r_Direccion.idprovincia;
                END IF;

            ELSE -- Tiene direccion extranjera
                v_provincia := NULL;
                v_poblacion := r_Direccion.poblacionextranjera;
            END IF;

            -- Compruebo si tiene pais
            IF (r_Direccion.idPais IS NULL) THEN
                SELECT F_SIGA_GETRECURSO(NOMBRE, 1), COD_ISO
                    INTO v_pais, v_codigoPaisISO
                FROM CEN_PAIS
                WHERE idPais = '191'; -- JPT (09-10-2014): Con comillas entra por indice

            ELSE -- Tiene pais
                SELECT F_SIGA_GETRECURSO(NOMBRE, 1), COD_ISO
                    INTO v_pais, v_codigoPaisISO
                FROM CEN_PAIS
                WHERE idPais = r_Direccion.idPais;
            END IF;

            -- Cargo los datos obtenidos en el registro
            v_Direccion.DOMICILIO := r_direccion.domicilio;
            v_Direccion.CODIGOPOSTAL := r_direccion.codigopostal;
            v_Direccion.PAIS_NOMBRE := v_pais;
            v_Direccion.PAIS_ISO := v_codigoPaisISO;
            v_Direccion.PAIS_ID := r_direccion.idPais;
            v_Direccion.PROVINCIA_NOMBRE := v_provincia;
            v_Direccion.PROVINCIA_ID := r_direccion.idprovincia;
            v_Direccion.POBLACION_NOMBRE := v_poblacion;
            v_Direccion.POBLACION_ID := r_direccion.idpoblacion;

        ELSE
            -- Cargo los datos obtenidos en el registro
            v_Direccion.DOMICILIO := NULL;
            v_Direccion.CODIGOPOSTAL := NULL;
            v_Direccion.PAIS_NOMBRE := NULL;
            v_Direccion.PAIS_ISO := NULL;
            v_Direccion.PAIS_ID := NULL;
            v_Direccion.PROVINCIA_NOMBRE := NULL;
            v_Direccion.PROVINCIA_ID := NULL;
            v_Direccion.POBLACION_NOMBRE := NULL;
            v_Direccion.POBLACION_ID := NULL;
        END IF;

        RETURN v_Direccion;
    END ObtenerDireccionFacturacion;

    /****************************************************************************************************************/
    /* Nombre: CalcularDCPersonaSepa*/
    /* Descripcion: Calcula el digito de control de una persona para SEPA */
    /* */
    /* P_CODPAIS - IN - Codigo del pais de la persona (ISO 3166) - VARCHAR2(2) */
    /* P_NIFINSTITUCION - IN - Nif de la institucion - VARCHAR2(28) */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    FUNCTION CalcularDCPersonaSepa (
        p_CodPais IN VARCHAR2,
        p_NifInstitucion IN VARCHAR2
    ) RETURN VARCHAR2 IS

    v_AuxCaracter NUMBER;
    v_Letra VARCHAR2(1);
    v_CadenaNumeros VARCHAR2(35) := '';
    v_resultado NUMBER;

  BEGIN

        -- Recorro todos los caracteres del nif
        FOR v_AuxCaracter IN 1..LENGTH(p_NifInstitucion) LOOP

            -- Obtengo el caracter
            v_Letra := SUBSTR(p_NifInstitucion, v_AuxCaracter, 1);

            -- Compruebo si es un numero
            IF (REGEXP_LIKE(v_Letra, '[0-9]')) THEN

                -- Guardo el numero en una variable
                v_CadenaNumeros := v_CadenaNumeros || v_Letra;

            -- Compruebo si es una letra
            ELSIF (REGEXP_LIKE(v_Letra, '[A-Z]')) THEN

                -- Realizo una transformacion de la letra y la guardo en una variable
                v_CadenaNumeros := v_CadenaNumeros || ASCII(v_Letra) - 55;
            END IF;
        END LOOP;

        -- Recorro todos los caracteres del codigo ISO del pais
        FOR v_AuxCaracter IN 1..LENGTH(p_CodPais) LOOP

            -- Obtengo el caracter
            v_Letra := UPPER(SUBSTR(p_CodPais, v_AuxCaracter, 1));

            -- Compruebo si es una letra
            IF (REGEXP_LIKE(v_Letra, '[A-Z]')) THEN

                -- Realizo una transformacion de la letra y la guardo en una variable
                v_CadenaNumeros := v_CadenaNumeros || ASCII(v_Letra) - 55;
            END IF;
        END LOOP;

        -- Concateno dos ceros a la variable
        v_CadenaNumeros := v_CadenaNumeros || '00';

        -- Resto a 98 el modulo 97 de la variable
        v_resultado := 98 - MOD(v_CadenaNumeros, 97);

        -- Devuelvo el resultado con dos digitos
        RETURN LPAD(v_resultado, 2, 0);
  END CalcularDCPersonaSepa;
  
    /****************************************************************************************************************/
    /* Nombre: CargarFechasSEPA */
    /* Descripcion: Carga las fechas de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN - Identificador de la programacion - NUMBER */
    /* P_FECHAPRESENTACION - IN - Fecha de presentacion del fichero en el banco (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOFRST - IN - Fecha de cargo del fichero para facturas FRST (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGORCUR - IN - Fecha de cargo del fichero para facturas RCUR (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOCOR1 - IN - Fecha de cargo del fichero para facturas COR1 (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOB2B - IN - Fecha de cargo del fichero para facturas B2B (YYYYMMDD) - VARCHAR2 (8) */
    /* P_REGFACTURAS - IN OUT - Registro con los datos de las facturas - REG_FACTURAS */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 11/03/2015 - Autor: Jorge Paez Trivino */
    /****************************************************************************************************************/    
    PROCEDURE CargarFechasSEPA(
        p_Idinstitucion IN NUMBER,
        p_IdSerieFacturacion IN NUMBER,
        p_IdProgramacion IN NUMBER,
        p_FechaPresentacion IN VARCHAR2,
        p_FechaCargoFRST IN VARCHAR2,
        p_FechaCargoRCUR IN VARCHAR2,
        p_FechaCargoCOR1 IN VARCHAR2,
        p_FechaCargoB2B IN VARCHAR2,
        p_RegFacturas IN OUT REG_FACTURAS,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS
        
        v_Datoserror VARCHAR2(4000) := Null;
        
    BEGIN            
        -- Compruebo si es una facturacion programada
        IF (p_IdSerieFacturacion IS NOT NULL AND p_IdProgramacion IS NOT NULL) THEN
            v_Datoserror := 'CargarFechasSEPA: Obtiene datos de FAC_FACTURACIONPROGRAMADA';
            SELECT FECHAPRESENTACION,
                    FECHARECIBOSPRIMEROS,
                    FECHARECIBOSRECURRENTES,
                    FECHARECIBOSCOR1,
                    FECHARECIBOSB2B
                INTO p_RegFacturas.FECHAPRESENTACION,
                    p_RegFacturas.FECHACARGO_COREFRST,
                    p_RegFacturas.FECHACARGO_CORERCUR,
                    p_RegFacturas.FECHACARGO_COR1,
                    p_RegFacturas.FECHACARGO_B2B
            FROM FAC_FACTURACIONPROGRAMADA
            WHERE IDINSTITUCION = p_Idinstitucion
                AND IDSERIEFACTURACION = p_IdSerieFacturacion
                AND IDPROGRAMACION = p_IdProgramacion;          

        ELSE
            v_Datoserror := 'CargarFechasSEPA: Transformacion de fechas a To_Date';
            IF (p_FechaPresentacion IS NULL) THEN
                p_RegFacturas.FECHAPRESENTACION := NULL;
            ELSE
                p_RegFacturas.FECHAPRESENTACION := To_Date(p_FechaPresentacion, 'YYYYMMDD');
            END IF;

            IF (p_FechaCargoFRST IS NULL) THEN
                p_RegFacturas.FECHACARGO_COREFRST := NULL;
            ELSE
                p_RegFacturas.FECHACARGO_COREFRST := To_Date(p_FechaCargoFRST, 'YYYYMMDD');
            END IF;

            IF (p_FechaCargoRCUR IS NULL) THEN
                p_RegFacturas.FECHACARGO_CORERCUR := NULL;
            ELSE
                p_RegFacturas.FECHACARGO_CORERCUR := To_Date(p_FechaCargoRCUR, 'YYYYMMDD');
            END IF;

            IF (p_FechaCargoCOR1 IS NULL) THEN
                p_RegFacturas.FECHACARGO_COR1 := NULL;
            ELSE
                p_RegFacturas.FECHACARGO_COR1 := To_Date(p_FechaCargoCOR1, 'YYYYMMDD');
            END IF;

            IF (p_FechaCargoB2B IS NULL) THEN
                p_RegFacturas.FECHACARGO_B2B := NULL;
            ELSE
                p_RegFacturas.FECHACARGO_B2B := To_Date(p_FechaCargoB2B, 'YYYYMMDD');
            END IF;
        END IF;

        v_Datoserror := 'CargarFechasSEPA: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CargarFechasSEPA;

    /****************************************************************************************************************/
    /* Nombre: CargarFacturaEnArrayDeudor*/
    /* Descripcion: Carga los datos de una factura en los arrays del deudor */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGFACTURA - IN OUT - Registro con los datos de la factura - REG_FACTURA */
    /* P_REGFACTURAS - IN OUT - Registro con los datos de las facturas - REG_FACTURAS */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 27/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
    PROCEDURE CargarFacturaEnArrayDeudor (
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegFactura IN OUT REG_FACTURA,
        p_RegFacturas IN OUT REG_FACTURAS,
        p_Idioma IN NUMBER,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        v_tieneMandato BOOLEAN := FALSE;
        v_idMandato CEN_MANDATOS_CUENTASBANCARIAS.IDMANDATO%TYPE := NULL;
        v_IdFacturaOriginal FAC_FACTURA.IDFACTURA%TYPE;
        contadorFactura NUMBER;
        
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION;

    BEGIN

         IF (p_RegFactura.IDMANDATO IS NOT NULL) THEN
            v_idMandato := p_RegFactura.IDMANDATO;

        ELSE
            v_Datoserror := 'CargarFacturaEnArrayDeudor: Obtiene la factura original';
            v_IdFacturaOriginal := F_SIGA_GETLISTFACTCOMISION(p_RegFactura.IDINSTITUCION, p_RegFactura.IDFACTURA, 3);

            v_Datoserror := 'CargarFacturaEnArrayDeudor: Consulta si tiene servicios o productos';
            /* JPT (09-10-2014): La actual sentencia consulta si una factura tiene productos o servicios
            * Mejora el rendimiento de la anterior:
            * - Si existen servicios, no consulta productos
            * - No necesita odernar
            */

            SELECT CASE WHEN (
                    EXISTS (
                        SELECT 1 -- Servicio
                        FROM FAC_FACTURACIONSUSCRIPCION
                        WHERE IDINSTITUCION = p_RegFactura.IDINSTITUCION
                            AND IDFACTURA = v_IdFacturaOriginal
                    )
                ) THEN 1

                WHEN (
                    EXISTS (
                        SELECT 1 -- Producto
                        FROM PYS_COMPRA
                        WHERE IDINSTITUCION = p_RegFactura.IDINSTITUCION
                            AND IDFACTURA = v_IdFacturaOriginal
                    )
                ) THEN
                2

                ELSE
                    NULL
                END INTO v_idMandato
            FROM DUAL;
        END IF;

        IF (v_idMandato IS NOT NULL) THEN
            BEGIN
                v_Datoserror := 'CargarFacturaEnArrayDeudor: Consulta el mandato en CEN_MANDATOS_CUENTASBANCARIAS';
                SELECT REFMANDATOSEPA,
                    ESQUEMA,
                    FECHAUSO,
                    AUTORIZACIONB2B,
                    DEUDOR_ID,
                    FIRMA_FECHA
                INTO p_RegFactura.REFMANDATOSEPA,
                    p_RegFactura.ESQUEMA,
                    p_RegFactura.FECHAUSO,
                    p_RegFactura.AUTORIZACIONB2B,
                    p_RegFactura.DEUDOR_ID,
                    p_RegFactura.FIRMA_FECHA
                FROM CEN_MANDATOS_CUENTASBANCARIAS
                WHERE IDINSTITUCION = p_RegFactura.IDINSTITUCION
                    AND IDPERSONA = p_RegFactura.IDPERSONA
                    AND IDCUENTA = p_RegFactura.IDCUENTA
                    AND IDMANDATO = v_idMandato;

               v_tieneMandato := TRUE;

                EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                        v_tieneMandato := FALSE;
            END;
        END IF;

        -- Compruebo si tiene mandato asociado
        IF (v_tieneMandato) THEN

            -- Compruebo si el mandato esta firmado
            IF (p_RegFactura.FIRMA_FECHA IS NOT NULL) THEN
            
                -- JPT (19-02-2015): Comprueba la vigencia del mandato
                IF (F_ADD_YEARS(NVL(p_RegFactura.FECHAUSO, p_RegFactura.FIRMA_FECHA), 3) < p_RegFacturas.FECHAPRESENTACION) THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                                            ' ' || p_RegFactura.DEUDOR_NOMBRE ||
                                            ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                                            ' ' || p_RegFactura.DEUDOR_ID ||
                                            ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeMandatoSinVigencia', p_Idioma) ||
                                            ' ' || p_RegFactura.REFMANDATOSEPA;
                    v_Codretorno := To_Char(5418);
                    RAISE e_Error;
                END IF;

            ELSE -- no tiene el mandato firmado
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                                        ' ' || p_RegFactura.DEUDOR_NOMBRE ||
                                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                                        ' ' || p_RegFactura.DEUDOR_ID ||
                                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeMandatoSinFirmar', p_Idioma) ||
                                        ' ' || p_RegFactura.REFMANDATOSEPA;
                v_Codretorno := To_Char(5413); -- Indico que para una fecha posterior a 01/02/2014 debe tener el mandato firmado
                RAISE e_Error;
            END IF;

        ELSE -- no tiene mandato asociado
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                                    ' ' || p_RegFactura.DEUDOR_NOMBRE ||
                                    ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                                    ' ' || p_RegFactura.DEUDOR_ID ||
                                    ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeMandatoSinAsociar', p_Idioma);
            v_Codretorno := To_Char(5412); -- No encuentra el mandato
            RAISE e_Error;
        END IF;

         -- Obtiene la direccion SEPA del Deudor
        v_Datoserror := 'PresentacionGeneral: Llamada a la funcion ObtenerDireccionFacturacion';
        p_RegFactura.DEUDOR_DIRECCION := ObtenerDireccionFacturacion(p_RegFactura.IDINSTITUCION, p_RegFactura.IDPERSONA);

       -- Compruebo que existe la direccion de facturacion del deudor
        IF (p_RegFactura.DEUDOR_DIRECCION.DOMICILIO IS NULL) THEN
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                        ' ' || p_RegFactura.DEUDOR_NOMBRE ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                        ' ' || p_RegFactura.DEUDOR_ID ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeDireccionFacturacionDeudor', p_Idioma);
            v_Codretorno := To_Char(5415);
            RAISE e_Error;
        END IF;
        
        -- En caso de regenerar una factura con secuencia FRST, se considera que no tiene fecha de uso
        IF (p_RegFactura.SECUENCIAREAL = 0) THEN -- 0:FRST; 1:RCUR
            p_RegFactura.FECHAUSO := NULL;
        END IF;

        -- Calculo si es una factura FRST o RCUR
        IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA<2 AND p_RegFactura.FECHAUSO IS NULL) THEN -- 0=ficheroFRST+RCUR; 1=ficheroFRST + ficheroRCUR
            p_RegFactura.SECUENCIA := 0; -- 0:FRST; 1:RCUR
            p_RegFactura.SECUENCIAREAL := 0; -- 0:FRST; 1:RCUR
                
            -- Recorro todas las facturas FRST del esquema correspondiente, para ver si se ha usado el mandato
            IF (p_RegFactura.ESQUEMA = 0) THEN -- 0:CORE; 1:COR1; 2:B2B
                FOR contadorFactura IN 1..p_RegFacturas.CONTADORFACTURASCOREFRST LOOP 
                    IF (p_RegFacturas.M_FACTURASCOREFRST(contadorFactura).REFMANDATOSEPA = p_RegFactura.REFMANDATOSEPA) THEN
                        p_RegFactura.SECUENCIA := 1; -- 0:FRST; 1:RCUR
                        p_RegFactura.SECUENCIAREAL := 1; -- 0:FRST; 1:RCUR
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;
                    
            ELSIF (p_RegFactura.ESQUEMA = 1) THEN -- 0:CORE; 1:COR1; 2:B2B    
                FOR contadorFactura IN 1..p_RegFacturas.CONTADORFACTURASCOR1FRST LOOP
                    IF (p_RegFacturas.M_FACTURASCOR1FRST(contadorFactura).REFMANDATOSEPA = p_RegFactura.REFMANDATOSEPA) THEN
                        p_RegFactura.SECUENCIA := 1; -- 0:FRST; 1:RCUR
                        p_RegFactura.SECUENCIAREAL := 1; -- 0:FRST; 1:RCUR
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;
                    
            ELSIF (p_RegFactura.ESQUEMA = 2) THEN -- 0:CORE; 1:COR1; 2:B2B     
                FOR contadorFactura IN 1..p_RegFacturas.CONTADORFACTURASB2BFRST LOOP
                    IF (p_RegFacturas.M_FACTURASB2BFRST(contadorFactura).REFMANDATOSEPA = p_RegFactura.REFMANDATOSEPA) THEN
                        p_RegFactura.SECUENCIA := 1; -- 0:FRST; 1:RCUR
                        p_RegFactura.SECUENCIAREAL := 1; -- 0:FRST; 1:RCUR
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;
            END IF;

        ELSE
            p_RegFactura.SECUENCIA := 1; -- 0:FRST; 1:RCUR                        
            IF (p_RegFactura.FECHAUSO IS NULL) THEN
                p_RegFactura.SECUENCIAREAL := 0; -- 0:FRST; 1:RCUR
            ELSE
                p_RegFactura.SECUENCIAREAL := 1; -- 0:FRST; 1:RCUR
            END IF;
        END IF;

        v_Datoserror := 'CargarFacturaEnArrayDeudor: Carga los datos de la factura en el array correspondiente del deudor';
        IF (p_RegFactura.ESQUEMA = 0 -- 0:CORE; 1:COR1; 2:B2B
                AND p_RegFactura.SECUENCIA = 0) THEN -- 0:FRST; 1:RCUR     
            p_RegFacturas.CONTADORFACTURASCOREFRST := p_RegFacturas.CONTADORFACTURASCOREFRST + 1;
            p_RegFacturas.M_FACTURASCOREFRST(p_RegFacturas.CONTADORFACTURASCOREFRST) := p_RegFactura;

        ELSIF (p_RegFactura.ESQUEMA = 0 -- 0:CORE; 1:COR1; 2:B2B
                    AND p_RegFactura.SECUENCIA = 1) THEN -- 0:FRST; 1:RCUR     
            p_RegFacturas.CONTADORFACTURASCORERCUR := p_RegFacturas.CONTADORFACTURASCORERCUR + 1;
            p_RegFacturas.M_FACTURASCORERCUR(p_RegFacturas.CONTADORFACTURASCORERCUR) := p_RegFactura;
            
        ELSIF (p_RegAcreedor.TIPOSFICHEROS>0) THEN -- 1=Txt+Xml; 2=Xml
            IF (p_RegFactura.ESQUEMA = 1 -- 0:CORE; 1:COR1; 2:B2B
                    AND p_RegFactura.SECUENCIA = 0) THEN -- 0:FRST; 1:RCUR      
                p_RegFacturas.CONTADORFACTURASCOR1FRST := p_RegFacturas.CONTADORFACTURASCOR1FRST + 1;
                p_RegFacturas.M_FACTURASCOR1FRST(p_RegFacturas.CONTADORFACTURASCOR1FRST) := p_RegFactura;
            
            ELSIF (p_RegFactura.ESQUEMA = 1 -- 0:CORE; 1:COR1; 2:B2B
                        AND p_RegFactura.SECUENCIA = 1) THEN -- 0:FRST; 1:RCUR
                p_RegFacturas.CONTADORFACTURASCOR1RCUR := p_RegFacturas.CONTADORFACTURASCOR1RCUR + 1;
                p_RegFacturas.M_FACTURASCOR1RCUR(p_RegFacturas.CONTADORFACTURASCOR1RCUR) := p_RegFactura;

            ELSIF (p_RegFactura.ESQUEMA = 2) THEN -- 0:CORE; 1:COR1; 2:B2B    
                IF (p_RegFactura.AUTORIZACIONB2B=0) THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                                            ' ' || p_RegFactura.DEUDOR_NOMBRE ||
                                            ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                                            ' ' || p_RegFactura.DEUDOR_ID ||
                                            ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeMandatoB2BNoAutorizado', p_Idioma) ||
                                            ' ' || p_RegFactura.REFMANDATOSEPA;
                    v_Codretorno := To_Char(5416);
                    RAISE e_Error;
                END IF;
            
                IF (p_RegFactura.SECUENCIA = 0) THEN -- 0:FRST; 1:RCUR
                    p_RegFacturas.CONTADORFACTURASB2BFRST := p_RegFacturas.CONTADORFACTURASB2BFRST + 1;
                    p_RegFacturas.M_FACTURASB2BFRST(p_RegFacturas.CONTADORFACTURASB2BFRST) := p_RegFactura;
            
                ELSIF (p_RegFactura.SECUENCIA = 1) THEN -- 0:FRST; 1:RCUR
                    p_RegFacturas.CONTADORFACTURASB2BRCUR := p_RegFacturas.CONTADORFACTURASB2BRCUR + 1;
                    p_RegFacturas.M_FACTURASB2BRCUR(p_RegFacturas.CONTADORFACTURASB2BRCUR) := p_RegFactura;
                END IF;
            END IF;
        END IF;

        v_Datoserror := 'CargarFacturaEnArrayDeudor: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

        EXCEPTION
            WHEN e_Error THEN
                p_CodRetorno := v_Codretorno;
                p_DatosError := v_Datoserror;

            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CargarFacturaEnArrayDeudor;
    
    /****************************************************************************************************************/
    /* Nombre: CalcularBloqueAcreedor */
    /* Descripcion: Calcula los bloques de acreedor */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_CONTADORFACTURAS - IN - Contador de facturas en el array - NUMBER */
    /* M_FACTURAS - IN - Array con los datos de las facturas - TAB_FACTURA */
    /* P_FECHACARGO - IN - Fecha de Cargo - DATE */
    /* P_TXT - IN - Indica si es para fichero txt - BOOLEAN */
    /* P_REGFICHERO - IN OUT - Registro con los datos del fichero - REG_FICHERO */
    /* */
    /* Version: 1.0 - Fecha Creacion: 11/03/2015 - Autor: Jorge Paez Trivino */
    /****************************************************************************************************************/          
    PROCEDURE CalcularBloqueAcreedor(
        p_RegAcreedor IN REG_ACREEDOR,
        p_ContadorFacturas IN NUMBER,
        M_Facturas IN TAB_FACTURA, 
        p_FechaCargo IN DATE,
        p_Txt IN BOOLEAN,
        p_RegFichero IN OUT REG_FICHERO
    ) IS        
        
        v_ContadorBloques NUMBER;
        v_BloqueActual NUMBER;
        v_contadorFacturas NUMBER;
        v_fechaCargo VARCHAR2(8);
    
    BEGIN
        
        -- Miro que tenga facturas 
        IF (p_ContadorFacturas > 0) THEN
        
            -- Cargo la fecha de cargo en formato YYYYMMDD
            v_fechaCargo := TO_CHAR(p_FechaCargo, 'YYYYMMDD');
        
            -- Calculos de bloques para fichero enformato n1914
            IF (p_Txt = TRUE AND p_RegAcreedor.TIPOSFICHEROS<2) THEN  -- 0=Txt; 1=Txt+Xml
                
                -- Miro si ya existe la fecha de cargo
                v_BloqueActual := NULL;
                FOR v_ContadorBloques IN 1..p_RegFichero.CONTADORBLOQUESACREEDORTXT LOOP
                    IF (p_RegFichero.M_BLOQUEACREEDORTXT(v_ContadorBloques).FECHACARGO = v_fechaCargo) THEN 
                        v_BloqueActual := v_ContadorBloques;
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;
                
                -- Si existe la fecha de cargo, hay que incluir las nuevas facturas
                IF (v_BloqueActual IS NOT NULL) THEN
                    FOR v_contadorFacturas IN 1..p_ContadorFacturas LOOP                        
                        p_RegFichero.M_BLOQUEACREEDORTXT(v_BloqueActual).CONTADORFACTURASTXT := p_RegFichero.M_BLOQUEACREEDORTXT(v_BloqueActual).CONTADORFACTURASTXT + 1;
                        p_RegFichero.M_BLOQUEACREEDORTXT(v_BloqueActual).IMPORTETOTALTXT := p_RegFichero.M_BLOQUEACREEDORTXT(v_BloqueActual).IMPORTETOTALTXT + M_Facturas(v_contadorFacturas).IMPORTE;
                        p_RegFichero.M_BLOQUEACREEDORTXT(v_BloqueActual).M_FACTURAS(p_RegFichero.M_BLOQUEACREEDORTXT(v_BloqueActual).CONTADORFACTURASTXT) := M_Facturas(v_contadorFacturas);
                    END LOOP;
                
                ELSE -- No existe la fecha de cargo, con lo que hay que crear un nuevo bloque de acreedor
                    p_RegFichero.CONTADORBLOQUESACREEDORTXT := p_RegFichero.CONTADORBLOQUESACREEDORTXT + 1;
                    p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).FECHACARGO := v_fechaCargo;
                
                    FOR v_contadorFacturas IN 1..p_ContadorFacturas LOOP
                        p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).CONTADORFACTURASTXT := p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).CONTADORFACTURASTXT + 1;
                        p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).IMPORTETOTALTXT := p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).IMPORTETOTALTXT + M_Facturas(v_contadorFacturas).IMPORTE;
                        p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).M_FACTURAS(p_RegFichero.M_BLOQUEACREEDORTXT(p_RegFichero.CONTADORBLOQUESACREEDORTXT).CONTADORFACTURASTXT) := M_Facturas(v_contadorFacturas);
                    END LOOP;
                END IF;
            END IF;
            
            -- Calculos de bloques para fichero en formato xml
            IF (p_RegAcreedor.TIPOSFICHEROS>0) THEN -- 1=Txt+Xml; 2=Xml            
            
                -- Miro si ya existe la fecha de cargo
                v_BloqueActual := NULL;
                FOR v_ContadorBloques IN 1..p_RegFichero.CONTADORBLOQUESACREEDOR LOOP
                    IF (p_RegFichero.M_BLOQUEACREEDOR(v_ContadorBloques).FECHACARGO = v_fechaCargo) THEN 
                        v_BloqueActual := v_ContadorBloques;
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;
            
                -- Si existe la fecha de cargo, hay que incluir las nuevas facturas
                IF (v_BloqueActual IS NOT NULL AND p_RegAcreedor.CONFIGLUGARESQUEMASECUENCIA=1) THEN -- 0=bloqueAcreedor; 1=registrosIndividuales
                    FOR v_contadorFacturas IN 1..p_ContadorFacturas LOOP                                                     
                        p_RegFichero.M_BLOQUEACREEDOR(v_BloqueActual).CONTADORFACTURAS := p_RegFichero.M_BLOQUEACREEDOR(v_BloqueActual).CONTADORFACTURAS + 1;
                        p_RegFichero.M_BLOQUEACREEDOR(v_BloqueActual).IMPORTETOTAL := p_RegFichero.M_BLOQUEACREEDOR(v_BloqueActual).IMPORTETOTAL + M_Facturas(v_contadorFacturas).IMPORTE;
                        p_RegFichero.M_BLOQUEACREEDOR(v_BloqueActual).M_FACTURAS(p_RegFichero.M_BLOQUEACREEDOR(v_BloqueActual).CONTADORFACTURAS) := M_Facturas(v_contadorFacturas);
                    END LOOP;
                
                ELSE -- No existe la fecha de cargo, con lo que hay que crear un nuevo bloque de acreedor                                         
                    p_RegFichero.CONTADORBLOQUESACREEDOR := p_RegFichero.CONTADORBLOQUESACREEDOR + 1;
                    p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).FECHACARGO := v_fechaCargo;
                                
                    FOR v_contadorFacturas IN 1..p_ContadorFacturas LOOP                                    
                        p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).CONTADORFACTURAS := p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).CONTADORFACTURAS + 1;
                        p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).IMPORTETOTAL := p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).IMPORTETOTAL + M_Facturas(v_contadorFacturas).IMPORTE;
                        p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).M_FACTURAS(p_RegFichero.M_BLOQUEACREEDOR(p_RegFichero.CONTADORBLOQUESACREEDOR).CONTADORFACTURAS) := M_Facturas(v_contadorFacturas);
                    END LOOP;
                END IF;
            END IF;
        END IF;
    END CalcularBloqueAcreedor;
    
    /****************************************************************************************************************/
    /* Nombre: CalcularFicherosGenerados */
    /* Descripcion: Calcula los fichero a generar */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGFACTURAS - IN - Registro con los datos de las facturas - REG_FACTURAS */
    /* P_NUMFICHEROS - IN OUT - Numero de ficheros a generar - NUMBER */
    /* M_FICHEROS - IN OUT - Array con los ficheros a generar - TAB_FICHERO */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 11/03/2015 - Autor: Jorge Paez Trivino */
    /****************************************************************************************************************/    
    PROCEDURE CalcularFicherosGenerados(
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegFacturas IN REG_FACTURAS, 
        p_numFicheros IN OUT NUMBER,
        M_Ficheros IN OUT TAB_FICHERO, 
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS
        
        v_Datoserror VARCHAR2(4000) := Null;
        
    BEGIN                
         -- Obtengo todos los disquetes de cargos
        IF (p_RegAcreedor.CONFIGFICHEROSESQUEMA=0) THEN -- 0=ficheroCORE+COR1+B2B
            IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA=0) THEN -- 0=ficheroFRST+RCUR
            
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCOREFRST + p_RegFacturas.CONTADORFACTURASCORERCUR + 
                    p_RegFacturas.CONTADORFACTURASCOR1FRST + p_RegFacturas.CONTADORFACTURASCOR1RCUR +
                    p_RegFacturas.CONTADORFACTURASB2BFRST + p_RegFacturas.CONTADORFACTURASB2BRCUR > 0) THEN                    
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE_COR1_B2B(FRST_RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(FRST_RCUR)';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor'; 
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOREFRST, p_RegFacturas.M_FACTURASCOREFRST, p_RegFacturas.FECHACARGO_COREFRST, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCORERCUR, p_RegFacturas.M_FACTURASCORERCUR, p_RegFacturas.FECHACARGO_CORERCUR, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1FRST, p_RegFacturas.M_FACTURASCOR1FRST, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1RCUR, p_RegFacturas.M_FACTURASCOR1RCUR, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BFRST, p_RegFacturas.M_FACTURASB2BFRST, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BRCUR, p_RegFacturas.M_FACTURASB2BRCUR, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                END IF;

            ELSE -- 1=ficheroCORE+COR1 + ficheroB2B; 2=ficheroCORE + ficheroCOR1 + ficheroB2B
                IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA=1) THEN -- 1=ficheroFRST + ficheroRCUR
                
                    -- Compruebo que tiene facturas
                    IF (p_RegFacturas.CONTADORFACTURASCOREFRST + p_RegFacturas.CONTADORFACTURASCOR1FRST + p_RegFacturas.CONTADORFACTURASB2BFRST > 0) THEN                    
                        p_numFicheros := p_numFicheros + 1;
                        M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE_COR1_B2B(FRST)';
                        M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(FRST)';
                        v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOREFRST, p_RegFacturas.M_FACTURASCOREFRST, p_RegFacturas.FECHACARGO_COREFRST, TRUE, M_Ficheros(p_numFicheros));
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1FRST, p_RegFacturas.M_FACTURASCOR1FRST, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BFRST, p_RegFacturas.M_FACTURASB2BFRST, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                    END IF;
                END IF;
                
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCORERCUR + p_RegFacturas.CONTADORFACTURASCOR1RCUR + p_RegFacturas.CONTADORFACTURASB2BRCUR > 0) THEN                
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE_COR1_B2B(RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(RCUR)';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCORERCUR, p_RegFacturas.M_FACTURASCORERCUR, p_RegFacturas.FECHACARGO_CORERCUR, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1RCUR, p_RegFacturas.M_FACTURASCOR1RCUR, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BRCUR, p_RegFacturas.M_FACTURASB2BRCUR, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                END IF;
            END IF;
                                                
        ELSIF (p_RegAcreedor.CONFIGFICHEROSESQUEMA=1) THEN -- 1=ficheroCORE+COR1 + ficheroB2B
            IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA=0) THEN -- 0=ficheroFRST+RCUR
            
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCOREFRST + p_RegFacturas.CONTADORFACTURASCORERCUR + 
                    p_RegFacturas.CONTADORFACTURASCOR1FRST + p_RegFacturas.CONTADORFACTURASCOR1RCUR > 0) THEN                                        
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE_COR1(FRST_RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(FRST_RCUR)';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOREFRST, p_RegFacturas.M_FACTURASCOREFRST, p_RegFacturas.FECHACARGO_COREFRST, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCORERCUR, p_RegFacturas.M_FACTURASCORERCUR, p_RegFacturas.FECHACARGO_CORERCUR, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1FRST, p_RegFacturas.M_FACTURASCOR1FRST, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1RCUR, p_RegFacturas.M_FACTURASCOR1RCUR, p_RegFacturas.FECHACARGO_COR1,FALSE, M_Ficheros(p_numFicheros));
                END IF;
                
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASB2BFRST + p_RegFacturas.CONTADORFACTURASB2BRCUR > 0) THEN                                    
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'B2B(FRST_RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BFRST, p_RegFacturas.M_FACTURASB2BFRST, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BRCUR, p_RegFacturas.M_FACTURASB2BRCUR, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                END IF;
            
            ELSE -- 1=ficheroFRST + ficheroRCUR; 2=ficheroTodoRCUR
                IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA=1) THEN -- 1=ficheroFRST + ficheroRCUR
                
                    -- Compruebo que tiene facturas
                    IF (p_RegFacturas.CONTADORFACTURASCOREFRST + p_RegFacturas.CONTADORFACTURASCOR1FRST > 0) THEN                                            
                        p_numFicheros := p_numFicheros + 1;
                        M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE_COR1(FRST)';
                        M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(FRST)';
                        v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOREFRST, p_RegFacturas.M_FACTURASCOREFRST, p_RegFacturas.FECHACARGO_COREFRST, TRUE, M_Ficheros(p_numFicheros));
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1FRST, p_RegFacturas.M_FACTURASCOR1FRST, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    END IF;
                    
                    -- Compruebo que tiene facturas
                    IF (p_RegFacturas.CONTADORFACTURASB2BFRST > 0) THEN                    
                        p_numFicheros := p_numFicheros + 1;
                        M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'B2B(FRST)';
                        M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                        v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BFRST, p_RegFacturas.M_FACTURASB2BFRST, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                    END IF;
                END IF;
            
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCORERCUR + p_RegFacturas.CONTADORFACTURASCOR1RCUR > 0) THEN                                    
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE_COR1(RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(RCUR)';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCORERCUR, p_RegFacturas.M_FACTURASCORERCUR, p_RegFacturas.FECHACARGO_CORERCUR, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1RCUR, p_RegFacturas.M_FACTURASCOR1RCUR, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                END IF;
                
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASB2BRCUR > 0) THEN                    
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'B2B(RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BRCUR, p_RegFacturas.M_FACTURASB2BRCUR, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                END IF;
            END IF;
                        
        ELSIF (p_RegAcreedor.CONFIGFICHEROSESQUEMA=2) THEN -- 2=ficheroCORE + ficheroCOR1 + ficheroB2B
            IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA=0) THEN -- 0=ficheroFRST+RCUR
            
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCOREFRST + p_RegFacturas.CONTADORFACTURASCORERCUR > 0) THEN                                            
                    p_numFicheros := p_numFicheros + 1;
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE(FRST_RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(FRST_RCUR)';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOREFRST, p_RegFacturas.M_FACTURASCOREFRST, p_RegFacturas.FECHACARGO_COREFRST, TRUE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCORERCUR, p_RegFacturas.M_FACTURASCORERCUR, p_RegFacturas.FECHACARGO_CORERCUR, TRUE, M_Ficheros(p_numFicheros));
                END IF;
                
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCOR1FRST + p_RegFacturas.CONTADORFACTURASCOR1RCUR > 0) THEN                                            
                    p_numFicheros := p_numFicheros + 1; 
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'COR1(FRST_RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1FRST, p_RegFacturas.M_FACTURASCOR1FRST, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1RCUR, p_RegFacturas.M_FACTURASCOR1RCUR, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                END IF;
                
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASB2BFRST + p_RegFacturas.CONTADORFACTURASB2BRCUR > 0) THEN                                            
                    p_numFicheros := p_numFicheros + 1; 
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'B2B(FRST_RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := ''; 
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BFRST, p_RegFacturas.M_FACTURASB2BFRST, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BRCUR, p_RegFacturas.M_FACTURASB2BRCUR, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros)); 
                END IF;
            
            ELSE -- 1=ficheroFRST + ficheroRCUR; 2=ficheroTodoRCUR
                IF (p_RegAcreedor.CONFIGFICHEROSSECUENCIA=1) THEN -- 1=ficheroFRST + ficheroRCUR
                
                    -- Compruebo que tiene facturas
                    IF (p_RegFacturas.CONTADORFACTURASCOREFRST > 0) THEN                                            
                        p_numFicheros := p_numFicheros + 1; 
                        M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE(FRST)';
                        M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(FRST)';
                        v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOREFRST, p_RegFacturas.M_FACTURASCOREFRST, p_RegFacturas.FECHACARGO_COREFRST, TRUE, M_Ficheros(p_numFicheros));
                    END IF; 
                    
                    -- Compruebo que tiene facturas
                    IF (p_RegFacturas.CONTADORFACTURASCOR1FRST > 0) THEN                                            
                        p_numFicheros := p_numFicheros + 1; 
                        M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'COR1(FRST)';
                        M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                        v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1FRST, p_RegFacturas.M_FACTURASCOR1FRST, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                    END IF; 
                    
                    -- Compruebo que tiene facturas
                    IF (p_RegFacturas.CONTADORFACTURASB2BFRST > 0) THEN                    
                        p_numFicheros := p_numFicheros + 1; 
                        M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'B2B(FRST)';
                        M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                        v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                        CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BFRST, p_RegFacturas.M_FACTURASB2BFRST, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                    END IF; 
                END IF; 
                
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCORERCUR > 0) THEN                                            
                    p_numFicheros := p_numFicheros + 1; 
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'CORE(RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := 'CORE(RCUR)';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCORERCUR, p_RegFacturas.M_FACTURASCORERCUR, p_RegFacturas.FECHACARGO_CORERCUR, TRUE, M_Ficheros(p_numFicheros));
                END IF; 
                    
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASCOR1RCUR > 0) THEN                                            
                    p_numFicheros := p_numFicheros + 1; 
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'COR1(RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASCOR1RCUR, p_RegFacturas.M_FACTURASCOR1RCUR, p_RegFacturas.FECHACARGO_COR1, FALSE, M_Ficheros(p_numFicheros));
                END IF; 
                    
                -- Compruebo que tiene facturas
                IF (p_RegFacturas.CONTADORFACTURASB2BRCUR > 0) THEN                    
                    p_numFicheros := p_numFicheros + 1; 
                    M_Ficheros(p_numFicheros).NOMBREFICHEROXML := 'B2B(RCUR)';
                    M_Ficheros(p_numFicheros).NOMBREFICHEROTXT := '';
                    v_Datoserror := 'CalcularFicherosGenerados: Llamadas a la funcion CalcularBloqueAcreedor';
                    CalcularBloqueAcreedor(p_RegAcreedor, p_RegFacturas.CONTADORFACTURASB2BRCUR, p_RegFacturas.M_FACTURASB2BRCUR, p_RegFacturas.FECHACARGO_B2B, FALSE, M_Ficheros(p_numFicheros));
                END IF;
            END IF; 
        END IF;

        v_Datoserror := 'CalcularFicherosGenerados: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm; 
    END CalcularFicherosGenerados;

    /****************************************************************************************************************/
    /* Nombre: InsertarDisqueteCargos */
    /* Descripcion: Inserta un registro en FAC_DISQUETECARGOS */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN - Identificador de la programacion - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGFACTURAS - IN - Registro con los datos de las facturas - REG_FACTURAS */
    /* P_IDDISQUETECARGOS - OUT - Identificador del disquete de cargos - NUMBER(10) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 18/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
    /****************************************************************************************************************/
    PROCEDURE InsertarDisqueteCargos(
        p_Idinstitucion IN NUMBER,
        p_IdSerieFacturacion IN NUMBER,
        p_IdProgramacion IN NUMBER,
        p_UsuModificacion IN NUMBER,
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegFacturas IN REG_FACTURAS,
        p_IdDisqueteCargos OUT FAC_DISQUETECARGOS.IDDISQUETECARGOS%TYPE,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        /* Obtenemos el numero de disquete, necesario para el nombre del fichero */
        v_Datoserror := 'InsertarDisqueteCargos: Obtencion del secuencial de la tabla FAC_DISQUETECARGOS';
        SELECT NVL(MAX(IDDISQUETECARGOS), 0) + 1
            INTO p_IdDisqueteCargos
        FROM FAC_DISQUETECARGOS
        WHERE IDINSTITUCION = p_Idinstitucion; 

        v_Datoserror := 'InsertarDisqueteCargos: Insercion en FAC_DISQUETECARGOS';
        INSERT INTO FAC_DISQUETECARGOS (
            IDINSTITUCION,
            IDDISQUETECARGOS,
            IDSERIEFACTURACION,
            IDPROGRAMACION,
            BANCOS_CODIGO,
            FECHACREACION,
            NOMBREFICHERO,
            FECHACARGO,
            FECHAPRESENTACION,
            FECHARECIBOSPRIMEROS,
            FECHARECIBOSRECURRENTES,
            FECHARECIBOSCOR1,
            FECHARECIBOSB2B,
            ESSEPA,
            FECHAMODIFICACION,
            USUMODIFICACION,
            IDSUFIJO
        ) VALUES (
            p_Idinstitucion,
            p_IdDisqueteCargos,
            p_Idseriefacturacion,
            p_Idprogramacion,
            p_RegAcreedor.CODIGOBANCO,
            SYSDATE,
            TO_CHAR(p_IdDisqueteCargos),
            NULL,
            p_RegFacturas.FECHAPRESENTACION,
            p_RegFacturas.FECHACARGO_COREFRST,
            p_RegFacturas.FECHACARGO_CORERCUR,
            p_RegFacturas.FECHACARGO_COR1,
            p_RegFacturas.FECHACARGO_B2B,
            '1', -- Indica que es fichero SEPA
            SYSDATE,
            p_Usumodificacion,
            p_RegAcreedor.IDSUFIJO);

        v_Datoserror := 'InsertarDisqueteCargos: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END InsertarDisqueteCargos;

    /****************************************************************************************************************/
    /* Nombre: insertarFIED*/
    /* Descripcion: Inserta un registro en FAC_FACTURAINCLUIDAENDISQUETE */
    /* */
    /* P_IDDISQUETECARGOS - IN - Identificador del disquete de cargos - NUMBER(10) */    
    /* P_REGFACTURA - IN - Registro con los datos de la factura - REG_FACTURA */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 18/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 27/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
    /****************************************************************************************************************/
    PROCEDURE insertarFIED(
        p_idDisqueteCargos IN FAC_DISQUETECARGOS.IDDISQUETECARGOS%TYPE,
        p_RegFactura IN REG_FACTURA,
        p_Usumodificacion IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2
    ) IS

        v_Idfacturaincluidaendisquete FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURAINCLUIDAENDISQUETE%TYPE;
        v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        v_Datoserror := 'insertarFIED: Obtencion del secuencial de la tabla FAC_FACTURAINCLUIDAENDISQUETE';
        SELECT NVL(MAX(IDFACTURAINCLUIDAENDISQUETE), 0) + 1
            INTO v_Idfacturaincluidaendisquete
        FROM FAC_FACTURAINCLUIDAENDISQUETE
        WHERE IDINSTITUCION = p_RegFactura.IDINSTITUCION
            AND IDDISQUETECARGOS = p_idDisqueteCargos;

        v_Datoserror := 'insertarFIED: Insercion en la tabla FAC_FACTURAINCLUIDAENDISQUETE';
        INSERT INTO FAC_FACTURAINCLUIDAENDISQUETE(
            IDINSTITUCION,
            IDDISQUETECARGOS,
            IDFACTURAINCLUIDAENDISQUETE,
            IDFACTURA,
            IDRECIBO,
            IDRENEGOCIACION,
            DEVUELTA,
            FECHADEVOLUCION,
            CONTABILIZADA,
            IDPERSONA,
            IDCUENTA,
            IMPORTE,
            FECHAMODIFICACION,
            USUMODIFICACION,
            ESQUEMA,
            SECUENCIA
        ) VALUES (
            p_RegFactura.IDINSTITUCION,
            p_idDisqueteCargos,
            v_Idfacturaincluidaendisquete,
            p_RegFactura.IDFACTURA,
            p_RegFactura.CODIGOREFERENCIA,
            NULL,
            'N',
            NULL,
            'N',
            p_RegFactura.IDPERSONA,
            p_RegFactura.IDCUENTA,
            p_RegFactura.IMPORTE / 100,
            SYSDATE,
            p_Usumodificacion,
            p_RegFactura.ESQUEMA,
            p_RegFactura.SECUENCIAREAL);

        v_Datoserror := 'insertarFIED: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END insertarFIED;

    /****************************************************************************************************************/
    /* Nombre: CrearCabeceraPresentador*/
    /* Descripcion: Crea un registro de cabecera del presentador (SEPA) */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGFICHERO - IN - Registro con los datos del fichero - REG_FICHERO */
    /* P_REGISTRO - OUT - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
  PROCEDURE CrearCabeceraPresentador(
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegFichero IN REG_FICHERO,
        p_Registro OUT VARCHAR2,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        c_CodRegistro Constant NUMBER := 1; /* Codigo del Registro */
        c_CodDato Constant NUMBER := 1; /* Codigo del Dato */
        v_Datoserror VARCHAR2(4000) := Null;

    BEGIN

        v_Datoserror := 'CrearCabeceraPresentador: Actualizacion del registro';
        p_Registro :=
            Lpad(Nvl(c_CodRegistro, 0), 2, 0) || -- 1. CodigoRegistro NUMBER (2)
            Lpad(Nvl(PKG_SIGA_CONSTANTES.c_IdCuaderno, 0), 4, 0) || MOD(PKG_SIGA_CONSTANTES.c_IdCuaderno, 7) ||-- 2. VersionCuaderno NUMBER (5)
            Lpad(Nvl(c_CodDato, 0), 3, 0) || -- 3. CodigoDato NUMBER (3)
            Rpad(Nvl(p_RegAcreedor.IDPERSONASEPAINSTITUCION , ' '), 35, ' ') || -- 4. IdentificadorPresentador VARCHAR2(35)
            Rpad(Nvl(p_RegAcreedor.NOMBRE, ' '), 70, ' ') ||-- 5. NombrePresentador VARCHAR2(70)
            Rpad(Nvl(p_RegFichero.FECHAPRESENTACION, '0'), 8, '0') ||-- 6. FechaCreacion NUMBER(8)
            Rpad(Nvl(p_RegFichero.IDFICHERO, ' '), 35, ' ') || -- 7. IdentificacionFichero VARCHAR2(35)
            Lpad(Nvl(p_RegAcreedor.ENTIDADBANCO, '0'), 4, '0') || -- 8. EntidadReceptora NUMBER(4)
            Lpad(Nvl(p_RegAcreedor.OFICINABANCO, '0'), 4, '0') || -- 9. OficinaReceptora NUMBER(4)
            Rpad(' ', 434, ' '); -- 10. Libre VARCHAR2(434)

        v_Datoserror := 'CrearCabeceraPresentador: Llamada a la funcion F_RevisarCaracteresSEPA';
        p_Registro := F_RevisarCaracteresSEPA(p_Registro);

        v_Datoserror := 'CrearCabeceraPresentador: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CrearCabeceraPresentador;

    /****************************************************************************************************************/
    /* Nombre: CrearCabeceraAcreedorFecha */
    /* Descripcion: Crea un registro de cabecera del acreedor en una fecha de cargo (SEPA) */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGBLOQUEACREEDOR - IN - Registro con los datos del bloque de acreedor por fecha de cargo - REG_BLOQUEACREEDOR */
    /* P_REGISTRO - OUT - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
    PROCEDURE CrearCabeceraAcreedorFecha(
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegBloqueAcreedor IN REG_BLOQUEACREEDOR,
        p_Registro OUT VARCHAR2,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2
    ) IS

        c_CodRegistro CONSTANT NUMBER := 2; /* Codigo del Registro */
        c_CodDato CONSTANT NUMBER := 2; /* Codigo del Dato */

        v_Direccion1 VARCHAR2(1000);
        v_Direccion2 VARCHAR2(1000);
        v_Direccion3 VARCHAR2(1000);
        v_Pais CEN_PAIS.COD_ISO%TYPE;
        
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION;

    BEGIN
        v_Datoserror := 'CrearCabeceraAcreedorFecha: Compruebo si no ha obtenido la fecha de cargo para el fichero';
        IF (p_RegBloqueAcreedor.FECHACARGO IS NULL) THEN
            RAISE e_Error;
        END IF;

        v_Datoserror := 'CrearCabeceraAcreedorFecha: Transformo las variables de direccion de facturacion del acreedor';
        v_Direccion1 := NVL(p_RegAcreedor.DIRECCION.DOMICILIO, '');
        v_Direccion2 := NVL(p_RegAcreedor.DIRECCION.CODIGOPOSTAL, '') || ' ' || NVL(p_RegAcreedor.DIRECCION.POBLACION_NOMBRE, '');
        v_Direccion3 := NVL(p_RegAcreedor.DIRECCION.PROVINCIA_NOMBRE, '');
        v_Pais := NVL(p_RegAcreedor.DIRECCION.PAIS_ISO, '');

        v_Datoserror := 'CrearCabeceraAcreedorFecha: Actualizacion del registro';
        p_Registro :=
            Lpad(Nvl(c_CodRegistro, 0), 2, 0) || -- 1. CodigoRegistro NUMBER (2)
            Lpad(Nvl(PKG_SIGA_CONSTANTES.c_IdCuaderno, 0), 4, 0) || MOD(PKG_SIGA_CONSTANTES.c_IdCuaderno, 7) ||-- 2. VersionCuaderno NUMBER (5)
            Lpad(Nvl(c_CodDato, 0), 3, 0) || -- 3. CodigoDato NUMBER (3)
            Rpad(Nvl(p_RegAcreedor.IDPERSONASEPAINSTITUCION, ' '), 35, ' ') || -- 4. IdentificadorAcreedor VARCHAR2(35)
            Rpad(Nvl(p_RegBloqueAcreedor.FECHACARGO, '0'), 8, '0') ||-- 5. FechaCobro NUMBER(8)
            Rpad(Nvl(p_RegAcreedor.NOMBRE, ' '), 70, ' ') ||-- 6. NombreAcreedor VARCHAR2(70)
            Rpad(Nvl(v_Direccion1, ' '), 50, ' ') || -- 7. DireccionAcreedor [tipo vía + nombre vía + número + piso] VARCHAR2(50) OPCIONAL
            Rpad(Nvl(v_Direccion2, ' '), 50, ' ') || -- 8. DireccionAcreedor [código postal + nombre localidad] VARCHAR2(50) OPCIONAL
            Rpad(Nvl(v_Direccion3, ' '), 40, ' ') || -- 9. DireccionAcreedor [nombre provincia] VARCHAR2(40) OPCIONAL
            Rpad(Nvl(v_Pais, ' '), 2, ' ') || -- 10. PaisDireccionAcreedor [ISO 3166] VARCHAR2(2) OPCIONAL
            Rpad(Nvl(p_RegAcreedor.BANCO_IBAN, ' '), 34, ' ') || -- 11. IBANAcreedor VARCHAR2(34)
            Rpad(' ', 301, ' '); -- 12. Libre VARCHAR2(301)

        v_Datoserror := 'CrearCabeceraAcreedorFecha: Llamada a la funcion RevisarCaracteresRegistro';
        p_Registro := F_RevisarCaracteresSEPA(p_Registro);

        v_Datoserror := 'CrearCabeceraAcreedorFecha: Actualizacion de los parametros de error';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CrearCabeceraAcreedorFecha;

    /****************************************************************************************************************/
    /* Nombre: CrearRegistroIndividual*/
    /* Descripcion: Crea un registro individual (SEPA) */
    /* */
    /* P_REGFACTURA - IN - Registro con los datos de la factura - REG_FACTURA */
    /* P_REGISTRO - OUT - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
    PROCEDURE CrearRegistroIndividual(
        p_RegFactura IN REG_FACTURA,
        p_Registro OUT VARCHAR2,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        CURSOR C_DatosLineaFactura(P_IDINSTITUCION FAC_LINEAFACTURA.IDINSTITUCION%TYPE, P_IDFACTURA FAC_LINEAFACTURA.IDFACTURA%TYPE) IS
                SELECT REPLACE(LF.DESCRIPCION, CHR(13) || CHR(10), '') AS Descripcion,
                    ROUND(LF.CANTIDAD * LF.PRECIOUNITARIO * (1 + (LF.IVA / 100)), 2) AS ImporteTotal,
                    LF.CANTIDAD * LF.PRECIOUNITARIO AS ImporteSinIVA,
                    ROUND(LF.CANTIDAD * LF.PRECIOUNITARIO * (LF.IVA / 100), 2) AS ImporteIVA,
                    LF.CANTIDAD,
                    TI.DESCRIPCIONTIPO
                FROM FAC_LINEAFACTURA LF,
                    PYS_TIPOIVA TI
                WHERE LF.IDINSTITUCION = P_IDINSTITUCION
                    AND LF.IDFACTURA = P_IDFACTURA
                    AND TI.IDTIPOIVA = LF.IDTIPOIVA
            ORDER BY LF.NUMEROORDEN ASC, ImporteTotal DESC, Descripcion ASC;

        c_CodRegistro Constant NUMBER := 3; /* Codigo del Registro */
        c_CodDato Constant NUMBER := 3; /* Codigo del Dato */

        v_EsColegiado NUMBER(1);
        v_LetraIdentificacionPersona VARCHAR2(1);
        v_TipoIdentificacionPersona NUMBER(1);
        v_Direccion1 VARCHAR2(4000);
        v_Direccion2 VARCHAR2(4000);
        v_Direccion3 VARCHAR2(4000);
        v_Pais CEN_PAIS.COD_ISO%TYPE;
        v_conceptoFinal VARCHAR2(4000);
        v_conceptoActual VARCHAR2(4000);
        v_Datoserror Varchar2(4000) := Null;
        v_Secuencia VARCHAR2(4);

    BEGIN
        v_DatosError := 'CrearRegistroIndividual: Consulto si es colegiado';
        SELECT COUNT(*)
             INTO v_EsColegiado
        FROM CEN_COLEGIADO
        WHERE IDINSTITUCION = p_RegFactura.IDINSTITUCION
            AND IDPERSONA = p_RegFactura.IDPERSONA;

        -- Calculo la letra y el tipo de identificacion de la persona
        IF (v_EsColegiado > 0) THEN
            v_LetraIdentificacionPersona := 'J';
            v_TipoIdentificacionPersona := 2;
        ELSE
            v_LetraIdentificacionPersona := 'I';
            v_TipoIdentificacionPersona := 1;
        END IF;

        v_Datoserror := 'CrearRegistroIndividual: Transformo las variables de direccion de facturacion del deudor';
        v_Direccion1 := NVL(p_RegFactura.DEUDOR_DIRECCION.DOMICILIO, '');
        v_Direccion2 := NVL(p_RegFactura.DEUDOR_DIRECCION.CODIGOPOSTAL, '') || ' ' || NVL(p_RegFactura.DEUDOR_DIRECCION.POBLACION_NOMBRE, '');
        v_Direccion3 := NVL(p_RegFactura.DEUDOR_DIRECCION.PROVINCIA_NOMBRE, '');
        v_Pais := NVL(p_RegFactura.DEUDOR_DIRECCION.PAIS_ISO, '');

        v_Datoserror := 'CrearRegistroIndividual: Calculo el concepto';
        v_conceptoFinal := 'Factura ' || p_RegFactura.NUMEROFACTURA;
        FOR v_DatosLineaFactura IN C_DatosLineaFactura(p_RegFactura.IDINSTITUCION, p_RegFactura.IDFACTURA) LOOP
            v_conceptoActual := ' - ' || v_DatosLineaFactura.Descripcion ||
                                            ' (' || v_DatosLineaFactura.Cantidad || ')' ||
                                            ' ' || F_SIGA_FORMATONUMERO(v_DatosLineaFactura.ImporteSinIVA, 2) ||
                                            ' ' || v_DatosLineaFactura.DESCRIPCIONTIPO ||
                                            ' ' || F_SIGA_FORMATONUMERO(v_DatosLineaFactura.ImporteIVA, 2);
            v_conceptoFinal := v_conceptoFinal || v_conceptoActual;

            IF (LENGTH(v_conceptoFinal) > 140) THEN
                v_conceptoFinal := SUBSTR(v_conceptoFinal, 1, 137) || '...';
                EXIT; --Salgo del bucle:
            END IF;
        END LOOP;
        
        IF (p_RegFactura.SECUENCIA = 0) THEN -- 0:FRST; 1:RCUR
            v_Secuencia := 'FRST';
        ELSE
            v_Secuencia := 'RCUR';
        END IF;

        v_DatosError := 'CrearRegistroIndividual: Actualizacion del registro';
        p_Registro :=
            Lpad(Nvl(c_CodRegistro, 0), 2, 0) || -- 1. CodigoRegistro NUMBER (2)
            Lpad(Nvl(PKG_SIGA_CONSTANTES.c_IdCuaderno, 0), 4, 0) || MOD(PKG_SIGA_CONSTANTES.c_IdCuaderno, 7) ||-- 2. VersionCuaderno NUMBER (5)
            Lpad(Nvl(c_CodDato, 0), 3, 0) || -- 3.  CodigoDato NUMBER (3)
            Rpad(Nvl(p_RegFactura.CODIGOREFERENCIA, ' '), 35, ' ') || -- 4. ReferenciaAdeudo VARCHAR2(35)
            Rpad(Nvl(p_RegFactura.REFMANDATOSEPA, ' '), 35, ' ') || -- 5. ReferenciaMandato VARCHAR2(35)
            Rpad(Nvl(v_Secuencia, ' '), 4, ' ') || -- 6. SecuenciaAdeudo VARCHAR2(4)
            Rpad(' ', 4, ' ') || -- 7. CategoriaProposito VARCHAR2(4) OPCIONAL -- Lo había puesto 'OTHR' pero da error en el validador
            Lpad(Nvl(p_RegFactura.IMPORTE, 0), 11, 0) || -- 8. ImporteAdeudo NUMBER(11)
            Lpad(Nvl(To_Char(p_RegFactura.FIRMA_FECHA, 'YYYYMMDD'), '0'), 8, '0') ||-- 9. FechaFirmaMandato NUMBER(8)
            Rpad(Nvl(p_RegFactura.BANCO_BIC, ' '), 11, ' ') || -- 10. BICDeudor VARCHAR2(11) OPCIONAL
            Rpad(Nvl(p_RegFactura.DEUDOR_NOMBRE, ' '), 70, ' ') || -- 11. NombreDeudor VARCHAR2(70)
            Rpad(Nvl(v_Direccion1, ' '), 50, ' ') || -- 12. DireccionDeudor [tipo vía + nombre vía + número + piso] VARCHAR2(50) OPCIONAL
            Rpad(Nvl(v_Direccion2, ' '), 50, ' ') || -- 13. DireccionDeudor [código postal + nombre localidad] VARCHAR2(50) OPCIONAL
            Rpad(Nvl(v_Direccion3, ' '), 40, ' ') || -- 14. DireccionDeudor [nombre provincia] VARCHAR2(40) OPCIONAL
            Rpad(Nvl(v_Pais, ' '), 2, ' ') || -- 15. PaisDireccionDeudor [ISO 3166] VARCHAR2(2) OPCIONAL
            v_TipoIdentificacionPersona || -- 16. TipoIdentificacionDeudor NUMBER(1) OPCIONAL
            v_LetraIdentificacionPersona || Rpad(Nvl(p_RegFactura.DEUDOR_ID, ' '), 35, ' ') || -- 17. IdentificadorDeudor VARCHAR2(36) OPCIONAL
            Rpad(' ', 35, ' ') || -- 18. IdentificadorDeudorEmisorCodigo VARCHAR2(35) OPCIONAL -- Antes Adri comento poner 'ES', pero al dar errores y ser opcional lo pongo con blancos
            'A' || -- 19. IdentificacionCuentaDeudor VARCHAR2(1)
            Rpad(Nvl(p_RegFactura.BANCO_IBAN, ' '), 34, ' ') || -- 20. IBANDeudor VARCHAR2(34)
            Rpad(' ', 4, ' ') || -- 21. PropositoAdeudo VARCHAR2(4) OPCIONAL -- Lo había puesto 'OTHR' pero da error en el validador
            Rpad(v_conceptoFinal, 140, ' ') ||-- 22. Concepto VARCHAR2(140) OPCIONAL
            Rpad(' ', 19, ' '); -- 23. Libre VARCHAR2(19)

        v_Datoserror := 'CrearRegistroIndividual: Llamada a la funcion RevisarCaracteresRegistro';
        p_Registro := F_RevisarCaracteresSEPA(p_Registro);

        v_DatosError := 'CrearRegistroIndividual: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CrearRegistroIndividual;

  /****************************************************************************************************************/
  /* Nombre: CrearTotalAcreedorFecha */
  /* Descripcion: Crea un registro de total del acreedor en una fecha de cargo (SEPA) */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGBLOQUEACREEDOR - IN - Registro con los datos del bloque de acreedor por fecha de cargo - REG_BLOQUEACREEDOR */
    /* P_SUMAIMPORTES - IN - Suma de los importes de los registros individuales - NUMBER  */
    /* P_NUMREGINDIVIDUALES - IN - Numero de registros individuales - NUMBER */
    /* P_NUMREGTOTAL - IN - Numero de registros totales del acreedor - NUMBER */
    /* P_REGISTRO - OUT - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
  Procedure CrearTotalAcreedorFecha(
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegBloqueAcreedor IN REG_BLOQUEACREEDOR,
        p_SumaImportes IN NUMBER,
        p_NumRegIndividuales IN NUMBER,
        p_NumRegTotal IN NUMBER,
        p_Registro OUT VARCHAR2,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2
    ) IS

    c_CodRegistro Constant NUMBER := 4; /* Codigo del Registro */
    v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        v_Datoserror := 'CrearTotalAcreedorFecha: Actualizacion del registro';
        p_Registro :=
            Lpad(Nvl(c_CodRegistro, 0), 2, 0) || -- 1. CodigoRegistro NUMBER (2)
            Rpad(Nvl(p_RegAcreedor.IDPERSONASEPAINSTITUCION, ' '), 35, ' ') || -- 2. IdentificadorAcreedor VARCHAR2(35)
            Rpad(Nvl(p_RegBloqueAcreedor.FECHACARGO, '0'), 8, '0') ||-- 3. FechaCobro NUMBER(8)
            Lpad(Nvl(p_SumaImportes, 0), 17, 0) || -- 4. SumaImportesIndividualesObligatorioas NUMBER(17)
            Lpad(Nvl(p_NumRegIndividuales, 0), 8, 0) || -- 5. NumeroRegistrosIndividualesObligatorios NUMBER(8)
            Lpad(Nvl(p_NumRegTotal, 0), 10, 0) || -- 6. NumeroTotalRegistros NUMBER(10)
            Rpad(' ', 520, ' '); -- 7. Libre VARCHAR2(520)

        v_Datoserror := 'CrearTotalAcreedorFecha: Llamada a la funcion RevisarCaracteresRegistro';
        p_Registro := F_RevisarCaracteresSEPA(p_Registro);

        v_Datoserror := 'CrearTotalAcreedorFecha: Actualizacion de los parametros de error';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CrearTotalAcreedorFecha;

  /****************************************************************************************************************/
  /* Nombre: CrearTotalAcreedor */
  /* Descripcion: Crea un registro de total del acreedor (SEPA) */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_SUMAIMPORTES - IN - Suma de los importes de los registros individuales - NUMBER  */
    /* P_NUMREGINDIVIDUALES - IN - Numero de registros individuales - NUMBER */
    /* P_NUMREGTOTAL - IN - Numero de registros totales del acreedor - NUMBER */
    /* P_REGISTRO - OUT - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
  Procedure CrearTotalAcreedor(
        p_RegAcreedor IN REG_ACREEDOR,
        p_SumaImportes IN NUMBER,
        p_NumRegIndividuales IN NUMBER,
        p_NumRegTotal IN NUMBER,
        p_Registro OUT VARCHAR2,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2
    ) IS

    c_CodRegistro Constant NUMBER := 5; /* Codigo del Registro */
    v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        v_Datoserror := 'CrearTotalAcreedor: Actualizacion del registro';
        p_Registro :=
            Lpad(Nvl(c_CodRegistro, 0), 2, 0) || -- 1. CodigoRegistro NUMBER (2)
            Rpad(Nvl(p_RegAcreedor.IDPERSONASEPAINSTITUCION, ' '), 35, ' ') || -- 2. IdentificadorAcreedor VARCHAR2(35)
            Lpad(Nvl(p_SumaImportes, 0), 17, 0) || -- 3. SumaImportesIndividualesObligatorioas NUMBER(17)
            Lpad(Nvl(p_NumRegIndividuales, 0), 8, 0) || -- 4. NumeroRegistrosIndividualesObligatorios NUMBER(8)
            Lpad(Nvl(p_NumRegTotal, 0), 10, 0) || -- 5. NumeroTotalRegistros NUMBER(10)
            Rpad(' ', 528, ' '); -- 6. Libre VARCHAR2(528)

        v_Datoserror := 'CrearTotalAcreedor: Llamada a la funcion RevisarCaracteresRegistro';
        p_Registro := F_RevisarCaracteresSEPA(p_Registro);

        v_Datoserror := 'CrearTotalAcreedor: Actualizacion de los parametros de error';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        Exception
            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CrearTotalAcreedor;

  /****************************************************************************************************************/
  /* Nombre: CrearTotalFichero */
  /* Descripcion: Crea un registro de total del fichero (SEPA) */
    /* */
    /* P_SUMAIMPORTES - IN - Suma de los importes de los registros individuales obligatorios - NUMBER  */
    /* P_NUMREGINDIVIDUALES - IN - Numero de registros individuales obligatorios - NUMBER */
    /* P_NUMREGTOTAL - IN - Numero de registros totales del ordenante - NUMBER */
    /* P_REGISTRO - OUT - Registro que se va a insertar en el fichero - VARCHAR2(600) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
  Procedure CrearTotalFichero(
        p_SumaImportes IN NUMBER,
        p_NumRegIndividuales IN NUMBER,
        p_NumRegTotal IN NUMBER,
        p_Registro OUT VARCHAR2,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2
    ) IS

    c_CodRegistro Constant NUMBER := 99; /* Codigo del Registro */
    v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        v_Datoserror := 'CrearTotalFichero: Actualizacion del registro';
        p_Registro :=
            Lpad(Nvl(c_CodRegistro, 0), 2, 0) || -- 1. CodigoRegistro NUMBER (2)
            Lpad(Nvl(p_SumaImportes, 0), 17, 0) || -- 2. SumaImportesIndividualesObligatorioas NUMBER(17)
            Lpad(Nvl(p_NumRegIndividuales, 0), 8, 0) || -- 3. NumeroRegistrosIndividualesObligatorios NUMBER(8)
            Lpad(Nvl(p_NumRegTotal, 0), 10, 0) || -- 4. NumeroTotalRegistros NUMBER(10)
            Rpad(' ', 563, ' '); -- 5. Libre VARCHAR2(563)

        v_Datoserror := 'CrearTotalFichero: Llamada a la funcion RevisarCaracteresRegistro';
        p_Registro := F_RevisarCaracteresSEPA(p_Registro);

        v_Datoserror := 'CrearTotalFichero: Actualizacion de los parametros de error';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CrearTotalFichero;
    
    /****************************************************************************************************************/
    /* Nombre: CabPresentadorXML */
    /* Descripcion: Crea un bloque de registros del Presentador en formato xml (SEPA) */
    /* l_sepa - IN OUT - Contenedor del documento xml a generar */
    /* l_CstmrDrctDbtInitn_node - IN - Nodo raiz del mensaje apartir del cual sigue la Cabezera Presentador */  
    /* P_REGACREEDOR - IN - Registro con los datos del Presentador-acreedor - REG_ACREEDOR */
    /* P_REGFICHERO - IN - Registro con los datos del fichero - REG_FICHERO */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
    - Revision de codigo XML SEPA */
    /****************************************************************************************************************/

    PROCEDURE CabPresentadorXML(
        l_sepa IN OUT DBMS_XMLDOM.DOMDocument,
        l_CstmrDrctDbtInitn_node IN DBMS_XMLDOM.DomNode,
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegFichero IN REG_FICHERO,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        l_GrpHdr_node DBMS_XMLDOM.DomNode; 
        l_MsgId_node DBMS_XMLDOM.DomNode;
        l_CreDtTm_node DBMS_XMLDOM.DomNode;
        l_NbOfTxs_node DBMS_XMLDOM.DomNode;
        l_CtrlSum_node DBMS_XMLDOM.DomNode;
        l_InitgPty_node DBMS_XMLDOM.DomNode;
        l_Nm_node DBMS_XMLDOM.DomNode;
        l_Id_node DBMS_XMLDOM.DomNode;
        l_Id2_node DBMS_XMLDOM.DomNode;
        l_OrgId_node DBMS_XMLDOM.DomNode;
        l_Othr_node DBMS_XMLDOM.DomNode; 
        l_node DBMS_XMLDOM.DomNode; 
        v_IdMensaje VARCHAR2(35); --MsgId - Identificacion del mensaje
        v_FechaCreacion VARCHAR2(19); --CreDtTm - Fecha y hora de creacion
        v_NombrePresentador VARCHAR2(70); --Nm - Nombre
        v_IdPresentador VARCHAR2(35); --Id - Identificacion
        v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        v_Datoserror := 'CabPresentadorXML: Cargar variables de la cabecera del presentador';
        v_IdMensaje := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFichero.IDFICHERO), ' ')); --MsgId - Identificacion del mensaje
        v_FechaCreacion := NVL(p_RegFichero.FECHAPRESENTACION, '0'); --CreDtTm - Fecha y hora de creacion
        v_NombrePresentador := F_RevisarCaracteresSEPA(NVL(SUBSTR(TRIM(p_RegAcreedor.NOMBRE),1,70), ' '));--Nm - Nombre
        v_IdPresentador := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegAcreedor.IDPERSONASEPAINSTITUCION) , ' '));--Id - Identificacion

        -- 1.0 [1..1] + Cabecera <GrpHdr> - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr
        v_Datoserror := 'CabPresentadorXML: Creo Nodo GrpHdr (Cabecera) - Localización /Document/CstmrDrctDbtInitn/GrpHdr';
        l_GrpHdr_node := DBMS_XMLDOM.appendChild(l_CstmrDrctDbtInitn_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'GrpHdr')));

        -- 1.1 [1..1] ++ Identificacion del mensaje <MsgId> 35 - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/MsgId
        v_Datoserror := 'CabPresentadorXML: Creo Nodo MsgId (35 - Identificación del mensaje) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/MsgId';
        l_MsgId_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'MsgId')));
        l_node := DBMS_XMLDOM.appendChild(l_MsgId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdMensaje)));

        -- 1.2 [1..1] ++ Fecha y hora de creacion <CreDtTm> 19 - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/CreDtTm
        v_Datoserror := 'CabPresentadorXML: Creo Nodo CreDtTm (19 - Fecha y hora de creación) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/CreDtTm';
        l_CreDtTm_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CreDtTm')));
        l_node := DBMS_XMLDOM.appendChild(l_CreDtTm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, TO_CHAR(TO_DATE(v_FechaCreacion,'YYYYMMDD'),'YYYY-MM-DD')||'T00:00:00')));

        -- 1.6 [1..1] ++ Numero de operaciones <NbOfTxs> 15 - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/NbOfTxs
        v_Datoserror := 'CabPresentadorXML: Creo Nodo NbOfTxs (15 - Número de operaciones) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/NbOfTxs';
        l_NbOfTxs_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'NbOfTxs')));
        l_node := DBMS_XMLDOM.appendChild(l_NbOfTxs_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, '0')));

        -- 1.7 [0..1] ++ Control de suma <CtrlSum> 19 - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/CtrlSum
        v_Datoserror := 'CabPresentadorXML: Creo Nodo CtrlSum (19 - Control de suma) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/CtrlSum';
        l_CtrlSum_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CtrlSum')));
        l_node := DBMS_XMLDOM.appendChild(l_CtrlSum_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, '0')));

        -- 1.8 [1..1] ++ Parte iniciadora <InitgPty> - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty
        v_Datoserror := 'CabPresentadorXML: Creo Nodo InitgPty (Parte iniciadora) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty';
        l_InitgPty_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'InitgPty')));

        -- 1.8 [0..1] +++ Nombre <Nm> 70 - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Nm
        v_Datoserror := 'CabPresentadorXML: Creo Nodo Nm (70 - Nombre) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Nm';
        l_Nm_node := DBMS_XMLDOM.appendChild(l_InitgPty_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Nm')));
        l_node := DBMS_XMLDOM.appendChild(l_Nm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_NombrePresentador)));

        -- 1.8 [0..1] +++ Identificacion <Id> - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id
        v_Datoserror := 'CabPresentadorXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id';
        l_Id_node := DBMS_XMLDOM.appendChild(l_InitgPty_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- 1.8 [1..1]{Or ++++ Persona juridica <OrgId> - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id/OrgId
        v_Datoserror := 'CabPresentadorXML: Creo Nodo OrgId (Persona jurídica) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id/OrgId';
        l_OrgId_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'OrgId')));

        -- [0..n] +++++ Otra <Othr> - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id/OrgId/Othr
        v_Datoserror := 'CabPresentadorXML: Creo Nodo Othr (Otra) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id/OrgId/Othr';
        l_Othr_node := DBMS_XMLDOM.appendChild(l_OrgId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Othr')));

        -- [1..1] ++++++ Identificacion <Id> 35 - Localizacion /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id/OrgId/Othr/Id
        v_Datoserror := 'CabPresentadorXML: Creo Nodo Id (35 - Identificación) - Localización /Document/CstmrDrctDbtInitn/GrpHdr/InitgPty/Id/OrgId/Othr/Id';
        l_Id2_node := DBMS_XMLDOM.appendChild (l_Othr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));
        l_node := DBMS_XMLDOM.appendChild(l_Id2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdPresentador)));

        v_Datoserror := 'CabPresentadorXML: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CabPresentadorXML;

    /****************************************************************************************************************/
    /* Nombre: CabAcreedorXML */
    /* Descripcion: Crea un bloque de registros del Acreedor en formato xml (SEPA) */
    /* l_sepa - IN OUT - Contenedor del documento xml a generar */
    /* l_CstmrDrctDbtInitn_node - IN - Nodo raiz del mensaje apartir del cual sigue la Cabezera Presentador */
    /* l_PmtInf_node - IN - Nodo del bloque; informacion del pago de los adeudos (Acreedor) */
    /* P_REGACREEDOR - IN - Registro con los datos del Presentador-acreedor - REG_ACREEDOR */
    /* P_REGBLOQUEACREEDOR - IN - Registro con los datos del bloque de acreedor por fecha de cargo - REG_BLOQUEACREEDOR */
    /* P_CONTADORBLOQUE - IN - Contador del bloque - NUMBER(1) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
    - Revision de codigo XML SEPA */
    /****************************************************************************************************************/
    PROCEDURE CabAcreedorXML(
        l_sepa IN OUT DBMS_XMLDOM.DOMDocument,
        l_CstmrDrctDbtInitn_node IN DBMS_XMLDOM.DomNode,
        l_PmtInf_node OUT DBMS_XMLDOM.DomNode,
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegBloqueAcreedor IN REG_BLOQUEACREEDOR,
        p_contadorBloque IN NUMBER,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        l_PmtInfId_node DBMS_XMLDOM.DomNode;
        l_PmtMtd_node DBMS_XMLDOM.DomNode; 
        l_NbOfTxs_node DBMS_XMLDOM.DomNode;
        l_CtrlSum_node DBMS_XMLDOM.DomNode;
        l_PmtTpInf_node DBMS_XMLDOM.DomNode;
        l_SvcLvl_node DBMS_XMLDOM.DomNode;
        l_Cd_node DBMS_XMLDOM.DomNode;
        l_Cd2_node DBMS_XMLDOM.DomNode;
        l_LclInstrm_node DBMS_XMLDOM.DomNode;
        l_SeqTp_node DBMS_XMLDOM.DomNode;
        l_ReqdColltnDt_node DBMS_XMLDOM.DomNode;
        l_Cdtr_node DBMS_XMLDOM.DomNode;
        l_Nm_node DBMS_XMLDOM.DomNode;
        l_PstlAdr_node DBMS_XMLDOM.DomNode;
        l_Ctry_node DBMS_XMLDOM.DomNode;
        l_AdrLine_node DBMS_XMLDOM.DomNode;
        l_AdrLine2_node DBMS_XMLDOM.DomNode;
        l_CdtrAcct_node DBMS_XMLDOM.DomNode;
        l_Id_node DBMS_XMLDOM.DomNode;
        l_Id2_node DBMS_XMLDOM.DomNode;
        l_Id3_node DBMS_XMLDOM.DomNode;
        l_IBAN_node DBMS_XMLDOM.DomNode;
        l_CdtrAgt_node DBMS_XMLDOM.DomNode;
        l_FinInstnId_node DBMS_XMLDOM.DomNode;
        l_BIC_node DBMS_XMLDOM.DomNode;
        l_CdtrSchmeId_node DBMS_XMLDOM.DomNode;
        l_PrvtId_node DBMS_XMLDOM.DomNode;
        l_Othr_node DBMS_XMLDOM.DomNode; 
        l_node DBMS_XMLDOM.DomNode;
        l_MsgId DBMS_XMLDOM.DomNode;
        l_child_MsgId DBMS_XMLDOM.DomNode;
        v_MetodoPago CONSTANT VARCHAR2(2) := 'DD'; --PmtMtd - Metodo de pago
        v_CodMensaje CONSTANT VARCHAR2(4) := 'SEPA'; --Cd - Codigo del mensaje
        v_MsgId VARCHAR2(35); --MsgId - Identificacion del mensaje
        v_IdPago VARCHAR2(35); --PmtInfId - Identificacion de la informacion del pago        
        v_SumaFacturas VARCHAR2(19); --CtrlSum - Suma de importe de facturas        
        v_Esquema VARCHAR2(4); --Cd - Codigo esquema
        v_Secuencia VARCHAR2(4); --SeqTv - Secuencia adeudo
        v_NomAcreedor VARCHAR2(70); --Nm - Nombre
        v_CodPais VARCHAR2(2); --Ctry - Pais
        v_Direccion_Total VARCHAR2(140); --AdrLine - Direccion
        v_Direccion VARCHAR2(70); --AdrLine - Direccion1
        v_DireccionResto VARCHAR2(70); --AdrLine - Direccion2
        v_Iban VARCHAR2(34); --IBAN - IBAN banco acreedor
        v_Bic VARCHAR2(11); --BIC - BIC banco acreedor
        v_Identificacion VARCHAR2(35); --Id - Identificacion del acreedor        
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION;

    BEGIN
        v_Datoserror := 'CabAcreedorXML: Compruebo si no ha obtenido la fecha de cargo para el fichero';
        IF (p_RegBloqueAcreedor.FECHACARGO IS NULL) THEN
            RAISE e_Error;
        END IF;

        -- Busco y extraigo la identificacion del mensaje
        v_Datoserror := 'CabAcreedorXML: Extraer la identificación del mensaje';
        l_MsgId := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'MsgId'), 0);
        l_child_MsgId := DBMS_XMLDOM.getFirstChild(l_MsgId);
        v_MsgId := DBMS_XMLDOM.getNodeValue(l_child_MsgId);

        v_Datoserror := 'CabAcreedorXML: Cargar variables del bloque del acreedor';
        v_IdPago := NVL(SUBSTR(v_MsgId,1,22) || LPAD(NVL(p_contadorBloque, 0), 2, 0) || SUBSTR(v_MsgId,25,11), ' '); --PmtInfId - Identificacion de la informacion del pago
        v_SumaFacturas := TO_CHAR(NVL(p_RegBloqueAcreedor.IMPORTETOTAL, 0) / 100, 'FM9999999999999999.00'); --CtrlSum - Suma de importe de facturas

        CASE p_RegBloqueAcreedor.M_FACTURAS(1).ESQUEMA
            WHEN 0 THEN v_Esquema := 'CORE';
            WHEN 1 THEN v_Esquema := 'COR1';
            WHEN 2 THEN v_Esquema := 'B2B';
        END CASE; --Cd - Codigo esquema

        IF (p_RegBloqueAcreedor.M_FACTURAS(1).SECUENCIA = 0) THEN -- 0:FRST; 1:RCUR
            v_Secuencia := 'FRST';
        ELSE
            v_Secuencia := 'RCUR';
        END IF; --SeqTv - Secuencia adeudo

        v_NomAcreedor := F_RevisarCaracteresSEPA(NVL(p_RegAcreedor.NOMBRE, ' ')); --Nm - Nombre
        v_CodPais := F_RevisarCaracteresSEPA(NVL(p_RegAcreedor.DIRECCION.PAIS_ISO, ' ')); --Ctry - Pais

        v_Datoserror := 'CabAcreedorXML: Transformo las variables de direccion de facturacion del acreedor';
        v_Direccion_Total := SUBSTR(TRIM(NVL(p_RegAcreedor.DIRECCION.DOMICILIO, '')) || ' ' ||
                             TRIM(NVL(p_RegAcreedor.DIRECCION.CODIGOPOSTAL, ''))  || ' ' ||
                             TRIM(NVL(p_RegAcreedor.DIRECCION.POBLACION_NOMBRE, '')) || ' ' ||
                             TRIM(NVL(p_RegAcreedor.DIRECCION.PROVINCIA_NOMBRE, '')),1,140); --AdrLine - Direccion

        v_Direccion := F_RevisarCaracteresSEPA(NVL(SUBSTR(v_Direccion_Total,1,70), ' ')); --AdrLine - Direccion1
        v_DireccionResto := F_RevisarCaracteresSEPA(SUBSTR(v_Direccion_Total,71,70)); --AdrLine - Direccion2
        v_Bic := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegAcreedor.BANCO_BIC), ' ')); --BIC - BIC banco acreedor
        v_Iban := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegAcreedor.BANCO_IBAN), ' ')); --IBAN - IBAN banco acreedor
        v_Identificacion := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegAcreedor.IDPERSONASEPAINSTITUCION), ' ')); --Id - Identificacion del acreedor
        
        -- 2.0 [1..n] + Informacion del pago <PmtInf> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf
        v_Datoserror := 'CabAcreedorXML: Creo Nodo PmtInf (Información del pago) - Localización /Document/CstmrDrctDbtInitn/PmtInf';
        l_PmtInf_node := DBMS_XMLDOM.appendChild(l_CstmrDrctDbtInitn_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtInf')));

        -- 2.1 [1..1] ++ Identificacion de la informacion del pago <PmtInfId> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtInfId
        v_Datoserror := 'CabAcreedorXML: Creo Nodo PmtInfId (35 - Identificación de la información del pago) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtInfId';
        l_PmtInfId_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtInfId')));
        l_node := DBMS_XMLDOM.appendChild(l_PmtInfId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdPago))); 

        -- 2.2 [1..1] ++ Metodo de pago <PmtMtd> 2 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtMtd
        v_Datoserror := 'CabAcreedorXML: Creo Nodo PmtMtd (2 - Método de pago) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtMtd';
        l_PmtMtd_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtMtd'))); 
        l_node := DBMS_XMLDOM.appendChild(l_PmtMtd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_MetodoPago)));

        -- 2.4 [0..1] ++ Numero de operaciones <NbOfTxs> 15 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/NbOfTxs
        v_Datoserror := 'CabAcreedorXML: Creo Nodo NbOfTxs (15 - Número de operaciones) - Localización /Document/CstmrDrctDbtInitn/PmtInf/NbOfTxs';
        l_NbOfTxs_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'NbOfTxs')));
        l_node := DBMS_XMLDOM.appendChild(l_NbOfTxs_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, p_RegBloqueAcreedor.CONTADORFACTURAS)));

        -- 2.5 [0..1] ++ Control de suma <CtrlSum> 19 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CtrlSum
        v_Datoserror := 'CabAcreedorXML: Creo Nodo CtrlSum (19 - Control de suma) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CtrlSum';
        l_CtrlSum_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CtrlSum')));
        l_node := DBMS_XMLDOM.appendChild(l_CtrlSum_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_SumaFacturas)));

        -- Si se quiere que los datos del esquema y secuencia se muestre en el bloque del acreedor
        v_Datoserror := 'CabAcreedorXML: Información del tipo de pago en bloque del acreedor';
        IF p_RegAcreedor.CONFIGLUGARESQUEMASECUENCIA = 0 THEN  -- 0=bloqueAcreedor; 1=registrosIndividuales

            -- 2.6 [0..1] ++ Informacion del tipo de pago <PmtTpInf> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf
            v_Datoserror := 'CabAcreedorXML: Creo Nodo PmtTpInf (Información del tipo de pago) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf';
            l_PmtTpInf_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtTpInf')));

            -- 2.8 [0..1] +++ Nivel de servicio <SvcLvl> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/SvcLvl
            v_Datoserror := 'CabAcreedorXML: Creo Nodo PmtInfId (Nivel de servicio) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/SvcLvl';
            l_SvcLvl_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'SvcLvl')));

            -- 2.9 [0..1] ++++ Codigo <Cd> 4 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/SvcLvl/Cd
            v_Datoserror := 'CabAcreedorXML: Creo Nodo Cd (4 - Código) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/SvcLvl/Cd';
            l_Cd_node := DBMS_XMLDOM.appendChild(l_SvcLvl_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cd')));
            l_node := DBMS_XMLDOM.appendChild(l_Cd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodMensaje)));

            -- 2.11 [0..1] +++ Instrumento local <LclInstrm> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/LclInstrm
            v_Datoserror := 'CabAcreedorXML: Creo Nodo LclInstrm (Instrumento local) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/LclInstrm';
            l_LclInstrm_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'LclInstrm')));

            -- 2.12 [0..1] ++++ Codigo <Cd> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/LclInstrm/Cd
            v_Datoserror := 'CabAcreedorXML: Creo Nodo Cd (35 - Código) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/LclInstrm/Cd';
            l_Cd2_node := DBMS_XMLDOM.appendChild(l_LclInstrm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cd')));
            l_node := DBMS_XMLDOM.appendChild(l_Cd2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Esquema)));

            -- 2.14 [0..1] +++ Tipo de secuencia <SeqTp> 4 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/SeqTp
            v_Datoserror := 'CabAcreedorXML: Creo Nodo SeqTp (4 - Tipo de secuencia) - Localización /Document/CstmrDrctDbtInitn/PmtInf/PmtTpInf/SeqTp';
            l_SeqTp_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'SeqTp')));
            l_node := DBMS_XMLDOM.appendChild(l_SeqTp_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Secuencia)));
        END IF;

        -- 2.18 [1..1] ++ Fecha de cobro <ReqdColltnDt> 10 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/ReqdColltnDt
        v_Datoserror := 'CabAcreedorXML: Creo Nodo ReqdColltnDt (10 - Fecha de cobro) - Localización /Document/CstmrDrctDbtInitn/PmtInf/ReqdColltnDt';
        l_ReqdColltnDt_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'ReqdColltnDt')));
        l_node := DBMS_XMLDOM.appendChild(l_ReqdColltnDt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, TO_CHAR(TO_DATE(p_RegBloqueAcreedor.FECHACARGO,'YYYYMMDD'),'YYYY-MM-DD'))));

        -- 2.19 [1..1] ++ Acreedor <Cdtr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/Cdtr
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Cdtr (Acreedor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/Cdtr';
        l_Cdtr_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cdtr')));

        -- 2.19 [0..1] +++ Nombre <Nm> 70 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/Cdtr/Nm
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Nm (70 - Nombre) - Localización /Document/CstmrDrctDbtInitn/PmtInf/Cdtr/Nm';
        l_Nm_node := DBMS_XMLDOM.appendChild(l_Cdtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Nm')));
        l_node := DBMS_XMLDOM.appendChild(l_Nm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_NomAcreedor)));

        -- 2.19 [0..1] +++ Direccion postal <PstlAdr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/Acreedor/PstlAdr
        v_Datoserror := 'CabAcreedorXML: Creo Nodo PstlAdr (Dirección postal) - Localización /Document/CstmrDrctDbtInitn/PmtInf/Cdtr/PstlAdr';
        l_PstlAdr_node := DBMS_XMLDOM.appendChild(l_Cdtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PstlAdr')));

        -- 2.19 [0..1] ++++ Pais <Ctry> 2 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/Acreedor/PstlAdr/Ctry
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Ctry (2 - País) - Localización /Document/CstmrDrctDbtInitn/PmtInf/Cdtr/PstlAdr/Ctry';
        l_Ctry_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ctry')));
        l_node := DBMS_XMLDOM.appendChild(l_Ctry_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodPais)));

        -- 2.19 [0..2] ++++ Direccion en texto libre <AdrLine> 70 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/Acreedor/PstlAdr/AdrLine
        v_Datoserror := 'CabAcreedorXML: Creo Nodo AdrLine (70 - Dirección en texto libre) - Localización /Document/CstmrDrctDbtInitn/PmtInf/Cdtr/PstlAdr/AdrLine[1]';
        l_AdrLine_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
        l_node := DBMS_XMLDOM.appendChild(l_AdrLine_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Direccion)));
        
        IF v_DireccionResto IS NOT NULL THEN
            v_Datoserror := 'CabAcreedorXML: Creo Nodo AdrLine (70 - Dirección en texto libre) - Localización /Document/CstmrDrctDbtInitn/PmtInf/Cdtr/PstlAdr/AdrLine[2]';
            l_AdrLine2_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
            l_node := DBMS_XMLDOM.appendChild(l_AdrLine2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_DireccionResto)));
        END IF;

        -- 2.20 [1..1] ++ Cuenta del acreedor <CdtrAcct> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrAcct
        v_Datoserror := 'CabAcreedorXML: Creo Nodo CdtrAcct (Cuenta del acreedor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrAcct';
        l_CdtrAcct_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CdtrAcct')));

        --2    2.20 [1..1] +++ Identificacion <Id> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrAcct/Id
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrAcct/Id';
        l_Id_node := DBMS_XMLDOM.appendChild(l_CdtrAcct_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- [1..1] ++++ IBAN <IBAN> 34 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrAcct/Id/IBAN
        v_Datoserror := 'CabAcreedorXML: Creo Nodo IBAN (34 - IBAN) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrAcct/Id/IBAN';
        l_IBAN_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'IBAN')));
        l_node := DBMS_XMLDOM.appendChild(l_IBAN_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Iban)));

        -- 2.21 [1..1] ++ Entidad del acreedor <CdtrAgt> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrAgt
        v_Datoserror := 'CabAcreedorXML: Creo Nodo CdtrAgt (Entidad del acreedor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrAgt';
        l_CdtrAgt_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CdtrAgt')));

        -- [1..1] +++ Identificacion de la entidad <FinInstnId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrAgt/FinInstnId
        v_Datoserror := 'CabAcreedorXML: Creo Nodo FinInstnId (Identificación de la entidad) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrAgt/FinInstnId';
        l_FinInstnId_node := DBMS_XMLDOM.appendChild(l_CdtrAgt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'FinInstnId')));

        -- [0..1] ++++ BIC <BIC> 11 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrAgt/FinInstnId/BIC
        v_Datoserror := 'CabAcreedorXML: Creo Nodo BIC (11 - BIC) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrAgt/FinInstnId/BIC';
        l_BIC_node := DBMS_XMLDOM.appendChild(l_FinInstnId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'BIC')));
        l_node := DBMS_XMLDOM.appendChild(l_BIC_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Bic)));

        -- 2.27 [0..1] ++ Identificacion del acreedor <CdtrSchmeId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId
        v_Datoserror := 'CabAcreedorXML: Creo Nodo CdtrSchmeId (Identificación del acreedor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId';
        l_CdtrSchmeId_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CdtrSchmeId')));

        -- 2.27 [0..1] +++ Identificacion <Id> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id';
        l_Id2_node := DBMS_XMLDOM.appendChild(l_CdtrSchmeId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- 2.27 [1..1] ++++ Identificacion privada <PrvtId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id/PrvtId
        v_Datoserror := 'CabAcreedorXML: Creo Nodo PrvtId (Identificación privada) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id/PrvtId';
        l_PrvtId_node := DBMS_XMLDOM.appendChild(l_Id2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PrvtId')));

        -- 2.27 [0..n] +++++ Otra <Othr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id/PrvtId/Othr
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Othr (Otra) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id/PrvtId/Othr';
        l_Othr_node := DBMS_XMLDOM.appendChild(l_PrvtId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Othr')));

        -- [1..1] ++++++ Identificacion <Id> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id/PrvtId/Othr/Id
        v_Datoserror := 'CabAcreedorXML: Creo Nodo Id (35 - Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/CdtrSchmeId/Id/PrvtId/Othr/Id';
        l_Id3_node := DBMS_XMLDOM.appendChild(l_Othr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));
        l_node := DBMS_XMLDOM.appendChild(l_Id3_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Identificacion)));

        v_Datoserror := 'CabAcreedorXML: Actualizacion de los parametros de error';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CabAcreedorXML;

    /****************************************************************************************************************/
    /* Nombre: DeudorIndividualXML*/
    /* Descripcion: Crea un bloque de la informacion de la operacion de adeudo directo xml (SEPA) */
    /* l_sepa - IN OUT - Contenedor del documento xml a generar */
    /* l_PmtInf_node - IN - Nodo raiz apartir del cual siguen los bloques individuales deudor */
    /* P_REGFACTURA - IN - Registro con los datos de la factura - REG_FACTURA */
    /* P_REGACREEDOR - IN - Registro con los datos del Presentador-acreedor - REG_ACREEDOR */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 10/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
    - Revision de codigo XML SEPA */
    /****************************************************************************************************************/
   PROCEDURE DeudorIndividualXML(
        l_sepa IN OUT DBMS_XMLDOM.DOMDocument,
        l_PmtInf_node IN DBMS_XMLDOM.DomNode,
        p_RegFactura IN REG_FACTURA,
        p_RegAcreedor IN REG_ACREEDOR,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS
    
        l_DrctDbtTxInf_node DBMS_XMLDOM.DomNode;
        l_PmtId_node DBMS_XMLDOM.DomNode;
        l_EndToEndId_node DBMS_XMLDOM.DomNode;
        l_PmtTpInf_node DBMS_XMLDOM.DomNode;
        l_SvcLvl_node DBMS_XMLDOM.DomNode;
        l_Cd_node DBMS_XMLDOM.DomNode;
        l_Cd2_node DBMS_XMLDOM.DomNode;
        l_LclInstrm_node DBMS_XMLDOM.DomNode;
        l_SeqTp_node DBMS_XMLDOM.DomNode;
        l_InstdAmt_node DBMS_XMLDOM.DomNode;
        l_InstdAmt_element DBMS_XMLDOM.DOMElement;
        l_DrctDbtTx_node DBMS_XMLDOM.DomNode;
        l_MndtRltdInf_node DBMS_XMLDOM.DomNode;
        l_MndtId_node DBMS_XMLDOM.DomNode;
        l_DtOfSgntr_node DBMS_XMLDOM.DomNode;
        l_DbtrAgt_node DBMS_XMLDOM.DomNode;
        l_FinInstnId_node DBMS_XMLDOM.DomNode;
        l_BIC_node DBMS_XMLDOM.DomNode;
        l_Dbtr_node DBMS_XMLDOM.DomNode;
        l_Nm_node DBMS_XMLDOM.DomNode;
        l_PstlAdr_node DBMS_XMLDOM.DomNode;
        l_Ctry_node DBMS_XMLDOM.DomNode;
        l_AdrLine_node DBMS_XMLDOM.DomNode;
        l_AdrLine2_node DBMS_XMLDOM.DomNode;
        l_Id_node DBMS_XMLDOM.DomNode;
        l_Id2_node DBMS_XMLDOM.DomNode;
        l_Id3_node DBMS_XMLDOM.DomNode;
        l_OrgId_node DBMS_XMLDOM.DomNode;
        l_PrvtId_node DBMS_XMLDOM.DomNode;
        l_Othr_node DBMS_XMLDOM.DomNode;
        l_DbtrAcct_node DBMS_XMLDOM.DomNode;
        l_IBAN_node DBMS_XMLDOM.DomNode;
        l_RmtInf_node DBMS_XMLDOM.DomNode;
        l_Ustrd_node DBMS_XMLDOM.DomNode;
        l_Ustrd_node2 DBMS_XMLDOM.DomNode;
        l_Ustrd_node3 DBMS_XMLDOM.DomNode;
        l_Ustrd_node4 DBMS_XMLDOM.DomNode;
        l_Ustrd_node5 DBMS_XMLDOM.DomNode;
        l_node DBMS_XMLDOM.DomNode;
        v_CodMensaje CONSTANT Varchar2(4) := 'SEPA'; --Cd - Codigo de mensaje
        v_EsColegiado NUMBER(1);
        v_IdenExtremo VARCHAR2(35); --EndToEndId - Identificacion de extremo a extremo        
        v_Esquema VARCHAR2(4); --Cd - Codigo de esquema
        v_Secuencia VARCHAR2(4); --SeqTp - Secuencia adeudo
        v_Importe VARCHAR2(19); --InstdAmt - Importe ordenado
        v_IdemMandato VARCHAR2(35); --MndtId - Identificacion del mandato
        v_FechaFirma VARCHAR2(10); --DtOfSgntr - Fecha firma
        v_Bic VARCHAR2(11); --NbOfTxs - BIC
        v_NomDeudor VARCHAR2(70); --Nm - Nombre
        v_CodPais VARCHAR2(2); --Ctry - Pais
        v_Direccion_Total VARCHAR2(140); --AdrLine - Direccion
        v_Direccion VARCHAR2(70); --AdrLine - Direccion1
        v_DireccionResto VARCHAR2(70); --AdrLine - Direccion2
        v_IdenDeudor VARCHAR2(35); --Id - Identificacion deudor
        v_Iban VARCHAR2(34); --IBAN - IBAN
        v_Concepto VARCHAR2(640); --Ustrd - Concepto
        v_conceptoFinal VARCHAR2(4000);
        v_conceptoActual VARCHAR2(4000);
        v_Datoserror VARCHAR2(4000) := NULL; 

        CURSOR C_DatosLineaFactura(P_IDINSTITUCION FAC_LINEAFACTURA.IDINSTITUCION%TYPE, P_IDFACTURA FAC_LINEAFACTURA.IDFACTURA%TYPE) IS
                SELECT REPLACE(LF.DESCRIPCION, CHR(13) || CHR(10), '') AS Descripcion,
                    ROUND(LF.CANTIDAD * LF.PRECIOUNITARIO * (1 + (LF.IVA / 100)), 2) AS ImporteTotal,
                    LF.CANTIDAD * LF.PRECIOUNITARIO AS ImporteSinIVA,
                    ROUND(LF.CANTIDAD * LF.PRECIOUNITARIO * (LF.IVA / 100), 2) AS ImporteIVA,
                    LF.CANTIDAD,
                    TI.DESCRIPCIONTIPO
                FROM FAC_LINEAFACTURA LF,
                    PYS_TIPOIVA TI
                WHERE LF.IDINSTITUCION = P_IDINSTITUCION
                    AND LF.IDFACTURA = P_IDFACTURA
                    AND TI.IDTIPOIVA = LF.IDTIPOIVA
            ORDER BY LF.NUMEROORDEN ASC, ImporteTotal DESC, Descripcion ASC;

    BEGIN
    
        v_DatosError := 'DeudorIndividualXML: Consulto si es colegiado';
        SELECT COUNT(*)
             INTO v_EsColegiado
        FROM CEN_COLEGIADO
        WHERE IDINSTITUCION = p_RegFactura.IDINSTITUCION
            AND IDPERSONA = p_RegFactura.IDPERSONA;
    
        v_Datoserror := 'DeudorIndividualXML: Calculo el concepto';
        v_conceptoFinal := 'Factura ' || p_RegFactura.NUMEROFACTURA;
        FOR v_DatosLineaFactura IN C_DatosLineaFactura(p_RegFactura.IDINSTITUCION, p_RegFactura.IDFACTURA) LOOP
            v_conceptoActual := ' - ' || v_DatosLineaFactura.Descripcion ||
                                            ' (' || v_DatosLineaFactura.Cantidad || ')' ||
                                            ' ' || F_SIGA_FORMATONUMERO(v_DatosLineaFactura.ImporteSinIVA, 2) ||
                                            ' ' || v_DatosLineaFactura.DESCRIPCIONTIPO ||
                                            ' ' || F_SIGA_FORMATONUMERO(v_DatosLineaFactura.ImporteIVA, 2);
            v_conceptoFinal := v_conceptoFinal || v_conceptoActual;
            
            IF p_RegAcreedor.CONFIGCONCEPTOAMPLIADO = 1 THEN -- 0=Concepto140; 1=concepto640 
                IF (LENGTH(v_conceptoFinal) > 640) THEN
                    v_conceptoFinal := SUBSTR(v_conceptoFinal, 1, 637) || '...';
                    EXIT; --Salgo del bucle:
                END IF;
            
            ELSE
                IF (LENGTH(v_conceptoFinal) > 140) THEN
                    v_conceptoFinal := SUBSTR(v_conceptoFinal, 1, 137) || '...';
                    EXIT; --Salgo del bucle:
                END IF;
            END IF;                            
        END LOOP;

        v_Datoserror := 'DeudorIndividualXML: Extraer codigo referencia';
        v_IdenExtremo := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFactura.CODIGOREFERENCIA), ' ')); --EndToEndId - Identificacion de extremo a extremo

        v_Datoserror := 'DeudorIndividualXML: Extraer secuencia';
        CASE p_RegFactura.esquema
            WHEN 0 THEN v_Esquema := 'CORE';
            WHEN 1 THEN v_Esquema := 'COR1';
            WHEN 2 THEN v_Esquema := 'B2B';
        END CASE; --Cd - Codigo

        v_Datoserror := 'DeudorIndividualXML: Calcular secuencia';
        IF (p_RegFactura.SECUENCIA = 0) THEN  -- 0:FRST; 1:RCUR
            v_Secuencia := 'FRST';
        ELSE
            v_Secuencia := 'RCUR';
        END IF; --SeqTp - Secuencia adeudo

        v_Datoserror := 'DeudorIndividualXML: Cargar variables del bloque deudor';
        v_Importe := NVL(TRIM(p_RegFactura.IMPORTE), 0) / 100; --InstdAmt - Importe ordenado
        v_IdemMandato := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFactura.REFMANDATOSEPA), ' ')); --MndtId - Identificacion del mandato
        v_FechaFirma := NVL(TO_CHAR(p_RegFactura.FIRMA_FECHA, 'YYYY-MM-DD'), '0'); --DtOfSgntr - Fecha firma
        v_Bic := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFactura.BANCO_BIC), ' ')); --NbOfTxs - BIC
        v_NomDeudor := F_RevisarCaracteresSEPA(NVL(SUBSTR(TRIM(p_RegFactura.DEUDOR_NOMBRE),1,70), ' ')); --Nm - Nombre deudor
        v_CodPais := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFactura.DEUDOR_DIRECCION.PAIS_ISO), ' ')); --Ctry - Pais

        v_Datoserror := 'DeudorIndividualXML: Transformo las variables de direccion de facturacion del deudor';
        v_Direccion_Total := SUBSTR(TRIM(NVL(p_RegFactura.DEUDOR_DIRECCION.DOMICILIO, '')) || ' ' ||
                             TRIM(NVL(p_RegFactura.DEUDOR_DIRECCION.CODIGOPOSTAL, ''))  || ' ' ||
                             TRIM(NVL(p_RegFactura.DEUDOR_DIRECCION.POBLACION_NOMBRE, '')) || ' ' ||
                             TRIM(NVL(p_RegFactura.DEUDOR_DIRECCION.PROVINCIA_NOMBRE, '')), 1, 140); --AdrLine - Direccion

        v_Direccion := F_RevisarCaracteresSEPA(NVL(SUBSTR(v_Direccion_Total, 1, 70), ' ')); --AdrLine - Direccion1
        v_DireccionResto := F_RevisarCaracteresSEPA(SUBSTR(TRIM(v_Direccion_Total), 71, 70)); --AdrLine - Direccion2
        v_IdenDeudor := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFactura.DEUDOR_ID), ' ')); --Id - Identificacion deudor
        v_Iban := F_RevisarCaracteresSEPA(NVL(TRIM(p_RegFactura.BANCO_IBAN), ' ')); --IBAN - IBAN       
        v_Concepto := F_RevisarCaracteresSEPA(v_conceptoFinal); --Ustrd - Concepto                 
        
        -- 2.28 [1..n] ++ Informacion de la operacion de adeudo directo <DrctDbtTxInf> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo DrctDbtTxInf (Información de la operación de adeudo directo) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf'; 
        l_DrctDbtTxInf_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DrctDbtTxInf')));

        -- 2.29 [1..1] +++ Identificacion del pago <PmtId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtId
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo PmtId (Identificacion del pago) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtId'; 
        l_PmtId_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtId')));

        -- 2.31 [1..1] ++++ Identificacion de extremo a extremo <EndToEndId> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtId/EndToEndId
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo EndToEndId (35 - Identificación de extremo a extremo) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtId/EndToEndId';
        l_EndToEndId_node := DBMS_XMLDOM.appendChild(l_PmtId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'EndToEndId')));
        l_node := DBMS_XMLDOM.appendChild(l_EndToEndId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdenExtremo)));

        -- Si se quiere que los datos del esquema y secuencia se muestre en el bloque Deudor individual
        v_Datoserror := 'DeudorIndividualXML: Informacion del tipo de pago en bloque deudor';
        IF p_RegAcreedor.CONFIGLUGARESQUEMASECUENCIA = 1 THEN -- 0=bloqueAcreedor; 1=registrosIndividuales

            -- 2.32 [0..1] +++ Informacion del tipo de pago <PmtTpInf> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo PmtTpInf (Información del tipo de pago) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf';
            l_PmtTpInf_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtTpInf')));

            -- 2.33 [0..1] ++++ Nivel de servicio <SvcLvl> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/SvcLvl
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo SvcLvl (Nivel de servicio) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/SvcLvl';
            l_SvcLvl_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'SvcLvl')));

            -- 2.34 [0..1] +++++ Codigo <Cd> 4 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/SvcLvl/Cd
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo Cd (4 - Código) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/SvcLvl/Cd';
            l_Cd_node := DBMS_XMLDOM.appendChild(l_SvcLvl_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cd')));
            l_node := DBMS_XMLDOM.appendChild(l_Cd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodMensaje)));

            -- 2.35 [0..1] ++++ Instrumento local  <LclInstrm> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/LclInstrm
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo LclInstrm (Instrumento local) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/LclInstrm';
            l_LclInstrm_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'LclInstrm')));

            -- 2.36 [0..1] +++++ Codigo <Cd> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/LclInstrm/Cd
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo Cd (35 - Código) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/LclInstrm/Cd';
            l_Cd2_node := DBMS_XMLDOM.appendChild(l_LclInstrm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cd')));
            l_node := DBMS_XMLDOM.appendChild(l_Cd2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Esquema)));

            -- 2.37 [0..1] ++++ Tipo de secuencia <SeqTp> 4 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/SeqTp
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo SeqTp (4 - Tipo de secuencia) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/PmtTpInf/SeqTp';
            l_SeqTp_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'SeqTp')));
            l_node := DBMS_XMLDOM.appendChild(l_SeqTp_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Secuencia)));
        END IF;

        -- 2.44 [1..1] +++ Importe ordenado <InstdAmt> 13 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/InstdAmt
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo InstdAmt (13 - Importe ordenado) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/InstdAmt';
        l_InstdAmt_element := DBMS_XMLDOM.createElement(l_sepa, 'InstdAmt');
        l_InstdAmt_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(l_InstdAmt_element));
        l_node := DBMS_XMLDOM.appendChild(l_InstdAmt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, TO_CHAR(v_Importe, 'FM99999999.00'))));
        DBMS_XMLDOM.setAttribute(l_InstdAmt_element, 'Ccy', 'EUR');

        -- 2.46 [0..1] +++ Operacion de adeudo directo <DrctDbtTx> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo DrctDbtTx (Operación de adeudo directo) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx';
        l_DrctDbtTx_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DrctDbtTx')));

        -- 2.47 [0..1] ++++ Informacion del mandato <MndtRltdInf> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx/MndtRltdInf
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo MndtRltdInf (Información del mandato) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx/MndtRltdInf';
        l_MndtRltdInf_node := DBMS_XMLDOM.appendChild(l_DrctDbtTx_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'MndtRltdInf')));
        
        -- 2.48 [0..1] +++++ Identificacion del mandato <MndtId> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx/MndtRltdInf/MndtId
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo MndtId (Identificación del mandato) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx/MndtRltdInf/MndtId';
        l_MndtId_node := DBMS_XMLDOM.appendChild(l_MndtRltdInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'MndtId')));
        l_node := DBMS_XMLDOM.appendChild(l_MndtId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdemMandato)));

        -- 2.49 [0..1] +++++ Fecha de firma <DtOfSgntr> 10 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx/MndtRltdInf/DtOfSgntr
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo DtOfSgntr (Fecha firma mandato) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DrctDbtTx/MndtRltdInf/DtOfSgntr';
        l_DtOfSgntr_node := DBMS_XMLDOM.appendChild(l_MndtRltdInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DtOfSgntr')));
        l_node := DBMS_XMLDOM.appendChild(l_DtOfSgntr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_FechaFirma)));

        -- 2.70 [1..1] +++ Entidad del deudor <DbtrAgt> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAgt
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo DbtrAgt (Entidad del deudor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAgt';
        l_DbtrAgt_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DbtrAgt')));

        -- [1..1] ++++ Identificacion de la entidad <FinInstnId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAgt/FinInstnId
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo FinInstnId (Identificación de la entidad) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAgt/FinInstnId';
        l_FinInstnId_node := DBMS_XMLDOM.appendChild(l_DbtrAgt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'FinInstnId')));

        -- [0..1] +++++ BIC <BIC> 11 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAgt/FinInstnId/BIC
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo BIC (11 - BIC) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAgt/FinInstnId/BIC';
        l_BIC_node := DBMS_XMLDOM.appendChild(l_FinInstnId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'BIC')));
        l_node := DBMS_XMLDOM.appendChild(l_BIC_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Bic)));

        -- 2.72 [1..1] +++ Deudor <Dbtr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo Dbtr (Deudor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr';
        l_Dbtr_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Dbtr')));
 
        -- 2.72 [0..1] ++++ Nombre <Nm> 70 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Nm
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo Nm (70 - Nombre) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Nm';
        l_Nm_node := DBMS_XMLDOM.appendChild(l_Dbtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Nm')));
        l_node := DBMS_XMLDOM.appendChild(l_Nm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_NomDeudor)));

        -- 2.72 [0..1] ++++ Direccion postal <PstlAdr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo PstlAdr (Dirección postal) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr';
        l_PstlAdr_node := DBMS_XMLDOM.appendChild(l_Dbtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PstlAdr')));
        
        -- 2.72 [0..1] +++++ Pais <Ctry> 2 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr/Ctry
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo Ctry (2 - País) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr/Ctry';
        l_Ctry_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ctry')));
        l_node := DBMS_XMLDOM.appendChild(l_Ctry_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodPais)));
        
        -- 2.72 [0..2] +++++ Direccion en texto libre <AdrLine> 70 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr/AdrLine
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo AdrLine (70 - Dirección en texto libre) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr/AdrLine[1]';
        l_AdrLine_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
        l_node := DBMS_XMLDOM.appendChild(l_AdrLine_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Direccion)));

        IF v_DireccionResto IS NOT NULL THEN
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo AdrLine (70 - Dirección en texto libre) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/PstlAdr/AdrLine[2]';
            l_AdrLine2_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
            l_node := DBMS_XMLDOM.appendChild(l_AdrLine2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_DireccionResto)));
        END IF;
        
        IF v_IdenDeudor IS NOT NULL THEN
        
            -- 2.72 [0..1] ++++ Identificacion <Id> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id';
            l_Id_node := DBMS_XMLDOM.appendChild(l_Dbtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

            IF (v_EsColegiado > 0) THEN
                -- 2.72 [1..1]{Or +++++ Persona juridica <OrgId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/OrgId
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo OrgId (Persona jurídica) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/OrgId';
                l_OrgId_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'OrgId')));
                
                -- [0..1] ++++++ Otra <Othr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/OrgId/Othr
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo Othr (Otra) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/OrgId/Othr';
                l_Othr_node := DBMS_XMLDOM.appendChild(l_OrgId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Othr')));

                -- [1..1] +++++++ Identificacion <Id> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/OrgId/Othr/Id
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo Id (35 - Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/OrgId/Othr/Id';
                l_Id2_node := DBMS_XMLDOM.appendChild(l_Othr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));
                l_node := DBMS_XMLDOM.appendChild(l_Id2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdenDeudor)));
                
            ELSE
                -- 2.72 [1..1]{Or +++++ Persona fisica <PrvtId> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/PrvtId
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo PrvtId (Persona física) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/PrvtId';
                l_PrvtId_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PrvtId')));
                
                -- [0..1] ++++++ Otra <Othr> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/PrvtId/Othr
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo Othr (Otra) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/PrvtId/Othr';
                l_Othr_node := DBMS_XMLDOM.appendChild(l_PrvtId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Othr')));

                -- [1..1] +++++++ Identificacion <Id> 35 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/PrvtId/Othr/Id
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo Id (35 - Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/Dbtr/Id/PrvtId/Othr/Id';
                l_Id2_node := DBMS_XMLDOM.appendChild(l_Othr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));
                l_node := DBMS_XMLDOM.appendChild(l_Id2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdenDeudor)));
            END IF;
        END IF;

        -- 2.73 [1..1] +++ Cuenta del deudor <DbtrAcct> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAcct
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo DbtrAcct (Cuenta del deudor) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAcct';
        l_DbtrAcct_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DbtrAcct')));

        -- [1..1] ++++ Identificacion <Id> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAcct/Id
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAcct/Id';
        l_Id3_node := DBMS_XMLDOM.appendChild(l_DbtrAcct_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- [1..1] +++++ IBAN <IBAN> 34 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAcct/Id/IBAN
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo IBAN (34 - IBAN) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/DbtrAcct/Id/IBAN';
        l_IBAN_node := DBMS_XMLDOM.appendChild(l_Id3_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'IBAN')));
        l_node := DBMS_XMLDOM.appendChild(l_IBAN_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Iban)));

        -- 2.88 [0..1] +++ Concepto <RmtInf> - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo RmtInf (Concepto) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf';
        l_RmtInf_node := DBMS_XMLDOM.appendChild(l_DrctDbtTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'RmtInf')));
        
        -- 2.89 [1..1]{Or ++++ No estructurado <Ustrd> 140 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[1]
        v_Datoserror := 'DeudorIndividualXML: Creo Nodo Ustrd (140 - No estructurado) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[1]';
        l_Ustrd_node := DBMS_XMLDOM.appendChild(l_RmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ustrd')));
        l_node := DBMS_XMLDOM.appendChild(l_Ustrd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, SUBSTR(v_Concepto, 1, 140))));
        
        IF (LENGTH(v_Concepto) > 140) THEN
            -- 2.89 [1..1]{Or ++++ No estructurado <Ustrd> 140 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[2]
            v_Datoserror := 'DeudorIndividualXML: Creo Nodo Ustrd (140 - No estructurado) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[2]';
            l_Ustrd_node2 := DBMS_XMLDOM.appendChild(l_RmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ustrd')));
            l_node := DBMS_XMLDOM.appendChild(l_Ustrd_node2, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, SUBSTR(v_Concepto, 140, 140)))); -- Concepto Ampliado (Ustrd = 1..640 caracteres)
            
            IF (LENGTH(v_Concepto) > 280) THEN
                -- 2.89 [1..1]{Or ++++ No estructurado <Ustrd> 140 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[3]
                v_Datoserror := 'DeudorIndividualXML: Creo Nodo Ustrd (140 - No estructurado) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[3]';
                l_Ustrd_node3 := DBMS_XMLDOM.appendChild(l_RmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ustrd')));
                l_node := DBMS_XMLDOM.appendChild(l_Ustrd_node3, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, SUBSTR(v_Concepto, 280, 140)))); -- Concepto Ampliado (Ustrd = 1..640 caracteres)
                
                IF (LENGTH(v_Concepto) > 420) THEN
                    -- 2.89 [1..1]{Or ++++ No estructurado <Ustrd> 140 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[4]
                    v_Datoserror := 'DeudorIndividualXML: Creo Nodo Ustrd (140 - No estructurado) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[4]';
                    l_Ustrd_node4 := DBMS_XMLDOM.appendChild(l_RmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ustrd')));
                    l_node := DBMS_XMLDOM.appendChild(l_Ustrd_node4, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, SUBSTR(v_Concepto, 420, 140)))); -- Concepto Ampliado (Ustrd = 1..640 caracteres)
                    
                    IF (LENGTH(v_Concepto) > 560) THEN
                        -- 2.89 [1..1]{Or ++++ No estructurado <Ustrd> 140 - Localizacion /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[5]
                        v_Datoserror := 'DeudorIndividualXML: Creo Nodo Ustrd (140 - No estructurado) - Localización /Document/CstmrDrctDbtInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd[5]';
                        l_Ustrd_node5 := DBMS_XMLDOM.appendChild(l_RmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ustrd')));
                        l_node := DBMS_XMLDOM.appendChild(l_Ustrd_node5, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, SUBSTR(v_Concepto, 560, 80)))); -- Concepto Ampliado (Ustrd = 1..640 caracteres)
                    END IF;
                END IF;
            END IF;
        END IF;

        v_DatosError := 'DeudorIndividualXML: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

        EXCEPTION
            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END DeudorIndividualXML;

    /****************************************************************************************************************/
    /* Nombre: TratarFacturasAcreedor*/
    /* Descripcion: Tratamiento del acreedor y sus facturas asociadas */
    /* */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGBLOQUEACREEDOR - IN - Registro con los datos del bloque de acreedor por fecha de cargo - REG_BLOQUEACREEDOR */
    /* F_SALIDA - IN - Fichero para guarda los registros txt - FILE_TYPE */
    /* l_sepa IN OUT - Documento xml adeudos en contruccion - Documento xml */
    /* l_CstmrDrctDbtInitn_node IN - Raiz del mensaje adeudos - Nodo */
    /* p_contadorBloque IN - Contador bloques acreedo-fecha - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 25/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
    /* Version: 3.0 - Fecha Modificacion: 10/06/2015 - Autor: Oscar de la Torre Noheda - Cambios: Adaptacion a XML */
    /* Version: 4.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
    - Revision de codigo XML SEPA */
  /****************************************************************************************************************/
    Procedure TratarFacturasAcreedor(
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegBloqueAcreedor IN REG_BLOQUEACREEDOR,
        f_Salida IN UTL_FILE.FILE_TYPE,
        -------------------------------XML-------------------------------------
        l_sepa IN OUT DBMS_XMLDOM.DOMDocument,
        l_CstmrDrctDbtInitn_node IN DBMS_XMLDOM.DomNode,
        p_contadorBloque IN NUMBER,
        -------------------------------XML-------------------------------------
        p_UsuModificacion IN NUMBER,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS 
    
        -- variable nodo bloque acreedor
        l_PmtInf_node DBMS_XMLDOM.DomNode;

        v_Registro VARCHAR2(600);
        v_contadorFactura NUMBER;
        v_estado FAC_FACTURA.ESTADO%TYPE;
        
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION;

    BEGIN
        /**************************** CABECERA DEL ACREEDOR ******************************************/
        -- Realizo un tratamiento del fichero txt
        IF (p_RegBloqueAcreedor.CONTADORFACTURASTXT>0) THEN 
       
            v_Datoserror := 'TratarFacturasAcreedor: Llamada al procedimiento CrearCabeceraAcreedorFecha';
            CrearCabeceraAcreedorFecha(
                p_RegAcreedor,
                p_RegBloqueAcreedor,
                v_Registro,
                v_Codretorno,
                v_Datoserror);
            IF v_Codretorno <> To_Char(0) THEN
                RAISE e_Error;
            END IF; 

            /* Grabar registro en el fichero */
            v_Datoserror := 'TratarFacturasAcreedor: Grabacion de la cabecera del acreedor SEPA';
            Utl_File.Putf(f_Salida, Substr(Rpad(v_Registro, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA), 1, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);
        END IF; 
        
        -- Realizo un tratamiento del fichero xml
        IF (p_RegBloqueAcreedor.CONTADORFACTURAS > 0) THEN
            -------------------------------XML-------------------------------------
            v_Datoserror := 'TratarFacturasAcreedor: Llamada al procedimiento CabAcreedorXML';
            CabAcreedorXML(
                l_sepa,
                l_CstmrDrctDbtInitn_node,
                l_PmtInf_node,
                p_RegAcreedor,
                p_RegBloqueAcreedor,
                p_contadorBloque,
                v_Codretorno,
                v_Datoserror);
            IF v_Codretorno <> To_Char(0) THEN
                RAISE e_Error;
            END IF;
            -------------------------------XML-------------------------------------
        END IF; 

        /****************************************** INDIVIDUAL OBLIGATORIO ******************************************/
        FOR v_contadorFactura IN 1..p_RegBloqueAcreedor.CONTADORFACTURAS+p_RegBloqueAcreedor.CONTADORFACTURASTXT LOOP
        
            -- Realizo un tratamiento del fichero txt
            IF (p_RegBloqueAcreedor.CONTADORFACTURASTXT>0 AND p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).ESQUEMA = 0) THEN -- 0:CORE; 1:COR1; 2:B2B    
                v_Datoserror := 'TratarFacturasAcreedor: Llamada al procedimiento CrearRegistroIndividual';
                CrearRegistroIndividual(
                    p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura),
                    v_Registro,
                    v_Codretorno,
                    v_Datoserror);
                IF v_Codretorno <> To_Char(0) THEN
                    RAISE e_Error;
                END IF;

                /* Grabar registro en el fichero */
                v_Datoserror := 'TratarFacturasAcreedor: Grabacion del registro individual';
                Utl_File.Putf(f_Salida, Substr(Rpad(v_Registro, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA), 1, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) || Chr(13) || Chr(10));
                Utl_File.Fflush(f_Salida);
            END IF; 
            
            -- Realizo un tratamiento del fichero xml
            IF (p_RegBloqueAcreedor.CONTADORFACTURAS > 0) THEN
                -------------------------------XML-------------------------------------
                v_Datoserror := 'TratarFacturasAcreedor: Llamada al procedimiento DeudorIndividualXML';
                DeudorIndividualXML(
                    l_sepa,
                    l_PmtInf_node,
                    p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura),
                    p_RegAcreedor,
                    v_Codretorno,
                    v_Datoserror);
                IF v_Codretorno <> To_Char(0) THEN
                    RAISE e_Error;
                END IF;
                -------------------------------XML-------------------------------------
            END IF; 

            -- p_UsuModificacion es nulo cuando se regenera el disquete de cargos (ya existen los datos FIED)
            IF (p_UsuModificacion IS NOT NULL) THEN
            
                -- Compruebo el estado actual de la factura
                SELECT ESTADO
                    INTO v_estado
                FROM FAC_FACTURA    
                WHERE IDINSTITUCION = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDINSTITUCION
                    AND IDFACTURA = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDFACTURA;
                
                -- Comprueba que no esta pagada la factura     
                IF (v_estado > 1) THEN                     
                           
                    /* Carga en BBDD de la factura a incluir en el disquete */
                    v_Datoserror := 'TratarFacturasAcreedor: Llamada al procedimiento insertarFIED';
                    insertarFIED(
                        p_RegBloqueAcreedor.IDDISQUETECARGOS,
                        p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura),
                        p_UsuModificacion,
                        v_Codretorno,
                        v_Datoserror);
                    IF v_Codretorno <> To_Char(0) THEN
                        RAISE e_Error;
                    END IF;

                    v_Datoserror := 'TratarFacturasAcreedor: Actualiza los importes de FAC_FACTURA';
                    UPDATE FAC_FACTURA
                    SET IMPTOTALPAGADOPORBANCO = IMPTOTALPAGADOPORBANCO + (p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IMPORTE / 100),
                        IMPTOTALPAGADO = IMPTOTALPAGADO + (p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IMPORTE / 100),
                        IMPTOTALPORPAGAR = IMPTOTALPORPAGAR - (p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IMPORTE / 100),
                        ESTADO = 1, -- Pagado
                        IDMANDATO = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDMANDATO, -- Identificador del mandato para sepa
                        REFMANDATOSEPA = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).REFMANDATOSEPA, -- Referencia del mandato para sepa
                        FECHAMODIFICACION = SYSDATE,
                        USUMODIFICACION = p_Usumodificacion
                    WHERE IDINSTITUCION = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDINSTITUCION
                        AND IDFACTURA = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDFACTURA;
                            
                    v_Datoserror := 'TratarFacturasAcreedor: Actualiza la fecha de uso de CEN_MANDATOS_CUENTASBANCARIAS';
                    UPDATE CEN_MANDATOS_CUENTASBANCARIAS
                    SET FECHAUSO = TO_DATE(p_RegBloqueAcreedor.FECHAPRESENTACION, 'YYYYMMDD'), -- JPT (19-02-2015): Actualiza la fecha de uso con la fecha de presentacion
                        FECHAMODIFICACION = SYSDATE,
                        USUMODIFICACION = p_Usumodificacion
                    WHERE IDINSTITUCION = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDINSTITUCION
                        AND IDPERSONA = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDPERSONA
                        AND IDCUENTA = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).IDCUENTA
                        AND REFMANDATOSEPA = p_RegBloqueAcreedor.M_FACTURAS(v_contadorFactura).REFMANDATOSEPA;                             
                END IF; 
            END IF; 
        END LOOP;

         /******************************************* TOTAL ACREEDOR Y FECHA ************************************************/
         -- Realizo un tratamiento del fichero txt
        IF (p_RegBloqueAcreedor.CONTADORFACTURASTXT>0) THEN
                    
            v_Datoserror := 'TratarFacturasAcreedor: Llamada al procedimiento CrearTotalAcreedorFecha';
            CrearTotalAcreedorFecha(
                p_RegAcreedor,
                p_RegBloqueAcreedor,
                p_RegBloqueAcreedor.IMPORTETOTALTXT,
                p_RegBloqueAcreedor.CONTADORFACTURASTXT,
                p_RegBloqueAcreedor.CONTADORFACTURASTXT + 2,
                v_Registro,
                v_Codretorno,
                v_Datoserror);
            IF v_Codretorno <> To_Char(0) THEN
                RAISE e_Error;
            END IF;

            /* Grabar registro en el fichero */
            v_Datoserror := 'TratarFacturasAcreedor: Grabacion del total del acreedor';
            Utl_File.Putf(f_Salida, Substr(Rpad(v_Registro, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA), 1, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);
        END IF; 

        v_Datoserror := 'TratarFacturasAcreedor: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN e_Error THEN
                p_CodRetorno := v_Codretorno;
                p_DatosError := v_Datoserror;

            WHEN OTHERS THEN
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END TratarFacturasAcreedor;
    
    /****************************************************************************************************************/
    /* Nombre: GestionarFicheros */
    /* Descripcion: Gestiona los ficheros de adeudos a generar en formato n1914 y/o xml */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN - Identificador de la programacion - NUMBER */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_REGACREEDOR - IN - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGFACTURAS - IN - Registro con los datos de las facturas - REG_FACTURAS */
    /* P_NUMFICHEROS - IN OUT - Numero de ficheros generados - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 25/03/2015 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 10/06/2015 - Autor: Oscar de la Torre Noheda - Cambios: Adaptacion a XML */
    /* Version: 3.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
    - Revision de codigo XML SEPA */
  /****************************************************************************************************************/    
    Procedure GestionarFicheros (
        p_IdInstitucion IN NUMBER,
        p_IdSerieFacturacion IN NUMBER,
        p_IdProgramacion IN NUMBER,
        p_PathFichero IN VARCHAR2,
        p_UsuModificacion IN NUMBER,
        p_RegAcreedor IN REG_ACREEDOR,
        p_RegFacturas IN REG_FACTURAS,
        p_NumFicheros IN OUT VARCHAR2,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2                    
    ) IS
    
        f_SalidaTxt Utl_File.File_Type; -- Fichero de salida
        v_Registro Varchar2(600); -- Registro para los ficheros de salida
        v_SumaImportesTxt NUMBER; -- Suma de los importes de los registros individuales txt
        v_SumaImportesXml NUMBER; -- Suma de los importes de los registros individuales xml
        v_NumRegIndividualesTxt NUMBER; -- Numero de registros individuales txt
        v_NumRegIndividualesXml NUMBER; -- Numero de registros individuales xml
        v_NumRegTotalTxt NUMBER; -- Numero de registros totales txt
        v_NumRegTotalXml NUMBER; -- Numero de registros totales xml         
        v_contadorFichero NUMBER;
        v_contadorBloque NUMBER;
        v_Nombrefichero FAC_DISQUETECARGOS.NOMBREFICHERO%TYPE; -- Nombre del fichero
        v_IdDisqueteCargos FAC_DISQUETECARGOS.IDDISQUETECARGOS%TYPE; 
        v_numFicheros NUMBER := 0;
        M_Ficheros TAB_FICHERO;
        -------------------------------XML-------------------------------------
        l_sepa DBMS_XMLDOM.DOMDocument; -- variable tipo documento xml
        l_Document_element DBMS_XMLDOM.DOMElement; -- variable tipo elemento nodo

        -- Variables para nodos a crear
        l_root_node DBMS_XMLDOM.DomNode; -- variable tipo nodo a insertar
        l_Document_node DBMS_XMLDOM.DomNode;
        l_CstmrDrctDbtInitn_node DBMS_XMLDOM.DomNode;

        -- Total numero facturas y total importes
        l_NbOfTxs_node DBMS_XMLDOM.DomNode;
        l_CtrlSum_node DBMS_XMLDOM.DomNode;
        l_NbOfTxs_child DBMS_XMLDOM.DomNode;
        l_CtrlSum_child DBMS_XMLDOM.DomNode;
        -------------------------------XML-------------------------------------
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION; 
    
    BEGIN
        v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CalcularFicherosGenerados';
        CalcularFicherosGenerados(
            p_RegAcreedor,
            p_RegFacturas,
            v_numFicheros,
            M_Ficheros,
            v_Codretorno,
            v_Datoserror);
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF; 
    
        -- Comprueba que tiene que generar ficheros
        IF (v_numFicheros > 0) THEN   
        
            -- p_UsuModificacion es nulo cuando se regenera el disquete de cargos (ya existen los datos)
            IF (p_UsuModificacion IS NOT NULL) THEN
                -- Solo se va a generar un disquete de cargos por cada banco + sufijo (independientemente del numero de ficheros que se generen con la configuracion)
                v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CargaDisqueteCargos';
                InsertarDisqueteCargos(
                    p_IdInstitucion,
                    p_IdSerieFacturacion,
                    p_IdProgramacion,
                    p_UsuModificacion,
                    p_RegAcreedor,
                    p_RegFacturas,
                    v_IdDisqueteCargos,
                    v_Codretorno,
                    v_Datoserror);
                IF v_Codretorno <> To_Char(0) THEN
                    RAISE e_Error;
                END IF; 
                
            ELSE
                v_IdDisqueteCargos := p_RegFacturas.IDDISQUETECARGOS; 
            END IF; 
            
            -- Recorre los ficheros a generar
            FOR v_contadorFichero IN 1..v_numFicheros LOOP
            
                v_SumaImportesTxt := 0; -- Inicializo los importes de los registros individuales txt
                v_SumaImportesXml := 0; -- Inicializo los importes de los registros individuales xml
                v_NumRegIndividualesTxt := 0; -- Inicializo el numero de registros individuales txt
                v_NumRegIndividualesXml := 0; -- Inicializo el numero de registros individuales xml
                v_NumRegTotalTxt := 0; -- Inicializo el numero de registros totales txt
                v_NumRegTotalXml := 0; -- Inicializo el numero de registros totales xml                                   
            
                v_Datoserror := 'GestionarFicheros: Cargo el identificador del disquete de cargos al fichero';
                M_Ficheros(v_contadorFichero).IDDISQUETECARGOS := v_IdDisqueteCargos; 
                
                v_Datoserror := 'GestionarFicheros: Cargo la fecha de presentacion al fichero';
                M_Ficheros(v_contadorFichero).FECHAPRESENTACION := TO_CHAR(p_RegFacturas.FECHAPRESENTACION, 'YYYYMMDD');
                
                v_Datoserror := 'GestionarFicheros: Calculo el identificador del fichero';
                M_Ficheros(v_contadorFichero).IDFICHERO := 'PRE' ||  RPAD(NVL(M_Ficheros(v_contadorFichero).FECHAPRESENTACION, '0'), 19, '0') || LPAD(v_contadorFichero, 3, 0) || LPAD(NVL(v_IdDisqueteCargos, 0), 10, 0); 
                                                               
                -- Realizo un tratamiento para los ficheros en formato n1914
                IF (p_RegAcreedor.TIPOSFICHEROS<2 AND M_Ficheros(v_contadorFichero).CONTADORBLOQUESACREEDORTXT > 0) THEN -- 0=Txt; 1=Txt+Xml 
                
                    v_Datoserror := 'GestionarFicheros: Calculo el nombre del fichero txt'; 
                    v_Nombrefichero := v_IdDisqueteCargos || '.' || M_Ficheros(v_contadorFichero).NOMBREFICHEROTXT || '.n' || LPAD(NVL(PKG_SIGA_CONSTANTES.c_IdCuaderno, 0), 4, 0);
                
                    v_Datoserror := 'GestionarFicheros: Apertura del fichero txt ' || v_Nombrefichero;
                    f_SalidaTxt := Utl_File.Fopen(p_PathFichero, v_Nombrefichero, 'W');
                    
                     /**************************************** CABECERA DEL PRESENTADOR ******************************************/
                    v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CrearCabeceraPresentador txt';
                    CrearCabeceraPresentador(
                        p_RegAcreedor,
                        M_Ficheros(v_contadorFichero),
                        v_Registro,
                        v_Codretorno,
                        v_Datoserror);
                    IF v_Codretorno <> To_Char(0) THEN
                        RAISE e_Error;
                    END IF; 

                    -- Grabar registro en el fichero
                    v_Datoserror := 'GestionarFicheros: Grabacion de la cabecera del presentador SEPA en el fichero txt';
                    Utl_File.Putf(f_SalidaTxt, Substr(Rpad(v_Registro, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA, ' '), 1, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) || Chr(13) || Chr(10));
                    Utl_File.Fflush(f_SalidaTxt);
                    
                    /**************************************** DATOS ACREEDOR ******************************************/
                    -- Recorre todos los bloques de acreedor por fecha de cargo    
                    FOR v_contadorBloque IN 1..M_Ficheros(v_contadorFichero).CONTADORBLOQUESACREEDORTXT LOOP
                    
                        -- Cargo el disquete de cargos y la fecha de presentacion
                        M_Ficheros(v_contadorFichero).M_BLOQUEACREEDORTXT(v_contadorBloque).IDDISQUETECARGOS := M_Ficheros(v_contadorFichero).IDDISQUETECARGOS;
                        M_Ficheros(v_contadorFichero).M_BLOQUEACREEDORTXT(v_contadorBloque).FECHAPRESENTACION := M_Ficheros(v_contadorFichero).FECHAPRESENTACION; 
                    
                        v_Datoserror := 'GestionarFicheros: Llamada al procedimiento TratarFacturasAcreedor';
                        TratarFacturasAcreedor(
                            p_RegAcreedor,
                            M_Ficheros(v_contadorFichero).M_BLOQUEACREEDORTXT(v_contadorBloque),
                            f_SalidaTxt, 
                        -------------------------------XML-------------------------------------
                            l_sepa, -- Contenedor documento xml
                            null, --l_CstmrDrctDbtInitn_node (Raiz del mensaje adeudos xml)
                            null, --v_contadorBloque (contar bloques acreedor xml)
                        -------------------------------XML-------------------------------------
                            p_UsuModificacion, 
                            v_Codretorno, 
                            v_Datoserror);
                        IF v_Codretorno <> To_Char(0) THEN
                            RAISE e_Error;
                        END IF; 
                        
                        v_SumaImportesTxt := v_SumaImportesTxt + M_Ficheros(v_contadorFichero).M_BLOQUEACREEDORTXT(v_contadorBloque).IMPORTETOTALTXT;
                        v_NumRegIndividualesTxt := v_NumRegIndividualesTxt + M_Ficheros(v_contadorFichero).M_BLOQUEACREEDORTXT(v_contadorBloque).CONTADORFACTURASTXT; 
                        v_NumRegTotalTxt := v_NumRegTotalTxt + M_Ficheros(v_contadorFichero).M_BLOQUEACREEDORTXT(v_contadorBloque).CONTADORFACTURASTXT + 2; -- Registros individuales +  cabecera + total
                    END LOOP; 
                    
                     /******************************************* TOTAL ACREEDOR ************************************************/
                    -- Realizo un tratamiento del fichero txt
                    v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CrearTotalAcreedor txt';
                    CrearTotalAcreedor(
                        p_RegAcreedor,
                        v_SumaImportesTxt,
                        v_NumRegIndividualesTxt,
                        v_NumRegTotalTxt + 1,
                        v_Registro,
                        v_Codretorno,
                        v_Datoserror);
                    IF v_Codretorno <> To_Char(0) THEN
                        RAISE e_Error;
                    END IF; 

                    /* Grabar registro en el fichero */
                    v_Datoserror := 'GestionarFicheros: Grabacion del total del ordenante SEPA en el fichero txt';
                    Utl_File.Putf(f_SalidaTxt, Substr(Rpad(v_Registro, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA, ' '), 1, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) || Chr(13) || Chr(10));
                    Utl_File.Fflush(f_SalidaTxt); 
                    
                    /********************************************** TOTAL FICHERO ***********************************************/
                    v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CrearTotalFichero txt';
                    CrearTotalFichero(
                        v_SumaImportesTxt,
                        v_NumRegIndividualesTxt,
                        v_NumRegTotalTxt + 3, -- + cabeceraPresentador + totalAcreedor + totalFichero
                        v_Registro,
                        v_Codretorno,
                        v_Datoserror);
                    IF v_Codretorno <> To_Char(0) THEN
                        RAISE e_Error;
                    END IF; 

                    /* Grabar registro en el fichero */
                    v_Datoserror := 'GestionarFicheros: Grabacion del total del ordenante SEPA en el fichero txt';
                    Utl_File.Putf(f_SalidaTxt, Substr(Rpad(v_Registro, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA, ' '), 1, PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) || Chr(13) || Chr(10));
                    Utl_File.Fflush(f_SalidaTxt);
                    
                    /* Cerrar ficheros de salida */
                    v_Datoserror := 'GestionarFicheros: Cierre del fichero txt';
                    Utl_File.Fclose(f_SalidaTxt);
                END IF; 
                
                -------------------------------XML-------------------------------------
                -- Realizo un tratamiento del fichero xml
                IF (p_RegAcreedor.TIPOSFICHEROS>0 AND M_Ficheros(v_contadorFichero).CONTADORBLOQUESACREEDOR > 0) THEN -- 1=Txt+Xml; 2=Xml
                
                    v_Datoserror := 'GestionarFicheros: Calculo el nombre del fichero xml'; 
                    v_Nombrefichero := v_IdDisqueteCargos || '.' || M_Ficheros(v_contadorFichero).NOMBREFICHEROXML || '.xml';
                                                   
                    v_Datoserror := 'GestionarFicheros: Creacion del documento - sepa xml';
                    l_sepa := DBMS_XMLDOM.newDomDocument;

                    v_Datoserror := 'GestionarFicheros: Creacion del nodo raiz del documento - sepa xml';
                    l_root_node := DBMS_XMLDOM.makeNode(l_sepa);

                    v_Datoserror := 'GestionarFicheros: Insertar Nodo Document (documento) - sepa xml';
                    l_Document_element := DBMS_XMLDOM.createElement(l_sepa, 'Document');
                    l_Document_node := DBMS_XMLDOM.appendChild(l_root_node, DBMS_XMLDOM.makeNode(l_Document_element));

                    v_Datoserror := 'GestionarFicheros: Insertar Atributos del nodo Document - sepa xml';
                    DBMS_XMLDOM.setAttribute(l_Document_element, 'xmlns', 'urn:iso:std:iso:20022:tech:xsd:pain.008.001.02');

                    --[1..1] + Raiz del mensaje adeudos  <CstmrDrctDbtInitn>
                    v_Datoserror := 'GestionarFicheros: Insertar Nodo CstmrDrctDbtInitn (Raiz del mensaje) - sepa xml';
                    l_CstmrDrctDbtInitn_node := DBMS_XMLDOM.appendChild(l_Document_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CstmrDrctDbtInitn')));
                    
                     /**************************************** CABECERA DEL PRESENTADOR ******************************************/
                    v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CabPresentadorXML';
                    CabPresentadorXML(
                        l_sepa, -- Contenedor documento xml
                        l_CstmrDrctDbtInitn_node, -- Raiz del mensaje adeudos
                        p_RegAcreedor,
                        M_Ficheros(v_contadorFichero),
                        v_Codretorno,
                        v_Datoserror);
                    IF v_Codretorno <> To_Char(0) THEN
                        RAISE e_Error;
                    END IF; 
                    
                    /**************************************** DATOS ACREEDOR ******************************************/
                    -- Recorre todos los bloques de acreedor por fecha de cargo    
                    FOR v_contadorBloque IN 1..M_Ficheros(v_contadorFichero).CONTADORBLOQUESACREEDOR LOOP
                    
                        -- Cargo el disquete de cargos y la fecha de presentacion
                        M_Ficheros(v_contadorFichero).M_BLOQUEACREEDOR(v_contadorBloque).IDDISQUETECARGOS := M_Ficheros(v_contadorFichero).IDDISQUETECARGOS;
                        M_Ficheros(v_contadorFichero).M_BLOQUEACREEDOR(v_contadorBloque).FECHAPRESENTACION := M_Ficheros(v_contadorFichero).FECHAPRESENTACION; 
                    
                        v_Datoserror := 'GestionarFicheros: Llamada al procedimiento TratarFacturasAcreedor';
                        TratarFacturasAcreedor(
                            p_RegAcreedor,
                            M_Ficheros(v_contadorFichero).M_BLOQUEACREEDOR(v_contadorBloque),
                            null, --f_Salida - para xml no necesitamos fichero fisico
                            l_sepa, -- Contenedor documento xml
                            l_CstmrDrctDbtInitn_node, -- Raiz del mensaje adeudos
                            v_contadorBloque, -- contar bloques acreedor
                            p_UsuModificacion, 
                            v_Codretorno, 
                            v_Datoserror);
                        IF v_Codretorno <> To_Char(0) THEN
                            RAISE e_Error;
                        END IF; 
                        v_SumaImportesXml := v_SumaImportesXml + M_Ficheros(v_contadorFichero).M_BLOQUEACREEDOR(v_contadorBloque).IMPORTETOTAL;
                        v_NumRegIndividualesXml := v_NumRegIndividualesXml + M_Ficheros(v_contadorFichero).M_BLOQUEACREEDOR(v_contadorBloque).CONTADORFACTURAS; 
                        v_NumRegTotalXml := v_NumRegTotalXml + M_Ficheros(v_contadorFichero).M_BLOQUEACREEDOR(v_contadorBloque).CONTADORFACTURAS + 2; -- Registros individuales +  cabecera + total            
                    END LOOP; 
                    
                    /******************** TOTALES DOCUMENTO XML **********************/
                    
                    v_Datoserror := 'GestionarFicheros: Insertar total numero facturas y total importes  - sepa xml';
                    l_NbOfTxs_node := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'NbOfTxs'), 0);
                    l_CtrlSum_node := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'CtrlSum'), 0);
                    l_NbOfTxs_child := DBMS_XMLDOM.getFirstChild(l_NbOfTxs_node);
                    l_CtrlSum_child := DBMS_XMLDOM.getFirstChild(l_CtrlSum_node);

                    DBMS_XMLDOM.setNodeValue(l_NbOfTxs_child, v_NumRegIndividualesXml);
                    DBMS_XMLDOM.setNodeValue(l_CtrlSum_child, TO_CHAR(v_SumaImportesXml / 100, 'FM9999999999999999.00'));

                    v_Datoserror := 'GestionarFicheros: Grabacion documento l_sepa en el fichero xml';
                    DBMS_XMLDOM.setversion(l_sepa, '1.0" encoding = "UTF-8');
                    DBMS_XMLDOM.setCharset(l_sepa, 'UTF-8');
                    DBMS_XMLDOM.writeToFile(l_sepa, p_PathFichero||'/'||v_Nombrefichero);
                END IF;
                -------------------------------XML-------------------------------------
            END LOOP;

            p_NumFicheros := p_NumFicheros + 1;
        END IF;

        v_Datoserror := 'GestionarFicheros: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN e_Error THEN
                p_NumFicheros := To_Char(0);
                p_CodRetorno := v_Codretorno;
                p_DatosError := v_Datoserror;
                    
                -- Cerrar fichero de salida txt
                BEGIN
                    Utl_File.Fclose(f_SalidaTxt);
                        
                    EXCEPTION
                        WHEN Utl_File.Invalid_Filehandle THEN
                            -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
                            p_CodRetorno := To_Char(5396);
                            p_DatosError := v_Datoserror || ', EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

                        WHEN Utl_File.Write_Error THEN
                            -- ERROR DEL S.O. DURANTE LA ESCRITURA
                            p_CodRetorno := To_Char(5398);
                            p_DatosError := v_Datoserror || ', ERROR DEL S.O. DURANTE LA ESCRITURA'; 
                END;

            -- posibles errores que se pueden producir al operar con ficheros.
            WHEN Utl_File.Invalid_Path THEN
                -- LA RUTA NO ES CORRECTA.
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5394);
                p_DatosError := v_Datoserror || ', LA RUTA NO ES CORRECTA';

            WHEN Utl_File.Invalid_Mode THEN
                -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5395);
                p_DatosError := v_Datoserror || ', LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

            WHEN Utl_File.Invalid_Filehandle THEN
                -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5396);
                p_DatosError := v_Datoserror || ', EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

            WHEN Utl_File.Invalid_Operation THEN
                -- OPERACION ERRONEA EN EL FICHERO
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5397);
                p_DatosError := v_Datoserror || ', OPERACION ERRONEA EN EL FICHERO, ' || Sqlerrm;

            WHEN Utl_File.Write_Error THEN
                -- ERROR DEL S.O. DURANTE LA ESCRITURA
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5398);
                p_DatosError := v_Datoserror || ', ERROR DEL S.O. DURANTE LA ESCRITURA';

            WHEN NO_DATA_FOUND THEN
                -- FINAL DE ARCHIVO EN UNA LECTURA
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5399);
                p_DatosError := v_Datoserror || ', FINAL DE ARCHIVO EN UNA LECTURA';

            WHEN Utl_File.Read_Error THEN
                -- ERROR DEL S.O. EN UNA LECTURA
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5400);
                p_DatosError := v_Datoserror || ', ERROR DEL S.O. EN UNA LECTURA';

            WHEN Utl_File.Internal_Error THEN
                -- ERROR INTERNO NO ESPECIFICADO
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5401);
                p_DatosError := v_Datoserror || ', ERROR INTERNO NO ESPECIFICADO, ' || Sqlerrm;

            WHEN VALUE_ERROR THEN
                -- LINEA DE ENTRADA DEMASIADO LARGA
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(5402);
                p_DatosError := v_Datoserror || ', LINEA DE ENTRADA DEMASIADO LARGA';

            WHEN OTHERS THEN
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(Sqlcode);
                p_DatosError := v_Datoserror || ', ' || Sqlerrm;

                -- Cerrar fichero de salida txt
                BEGIN
                    Utl_File.Fclose(f_SalidaTxt);
                            
                    EXCEPTION
                        WHEN Utl_File.Invalid_Filehandle THEN
                            -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
                            p_CodRetorno := To_Char(5396);
                            p_DatosError := v_Datoserror || ', EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

                        WHEN Utl_File.Write_Error THEN
                            -- ERROR DEL S.O. DURANTE LA ESCRITURA
                            p_CodRetorno := To_Char(5398);
                            p_DatosError := v_Datoserror || ', ERROR DEL S.O. DURANTE LA ESCRITURA'; 
                END;
    END GestionarFicheros;

    /****************************************************************************************************************/
    /* Nombre: PresentacionGeneral */
    /* Descripcion: Codigo generico para la presentacion de los ficheros de adeudo (SEPA) */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN - Identificador de la programacion - NUMBER */
    /* P_FECHAPRESENTACION - IN - Fecha de presentacion del fichero en el banco (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOFRST - IN - Fecha de cargo del fichero para facturas FRST (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGORCUR - IN - Fecha de cargo del fichero para facturas RCUR (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOCOR1 - IN - Fecha de cargo del fichero para facturas COR1 (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOB2B - IN - Fecha de cargo del fichero para facturas B2B (YYYYMMDD) - VARCHAR2 (8) */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_REGACREEDOR - IN OUT - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_NUMFICHEROS - IN OUT - Devuelve el numero de ficheros bancarios generados - VARCHAR2(10) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 31/07/2014 - Autor: Maria Jimenez */
    /* Version: 3.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
    Procedure PresentacionGeneral (
        p_IdInstitucion IN NUMBER,
        p_IdSerieFacturacion IN NUMBER,
        p_IdProgramacion IN NUMBER,
        p_FechaPresentacion IN VARCHAR2,
        p_FechaCargoFRST IN VARCHAR2,
        p_FechaCargoRCUR IN VARCHAR2,
        p_FechaCargoCOR1 IN VARCHAR2,
        p_FechaCargoB2B IN VARCHAR2,
        p_PathFichero IN VARCHAR2,
        p_UsuModificacion IN NUMBER,
        p_Idioma IN NUMBER,
        p_RegAcreedor IN OUT REG_ACREEDOR,
        p_NumFicheros IN OUT VARCHAR2,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS

        CURSOR c_Facturas (v_idInstitucion number, v_codigoBanco number, v_idSerieFacturacion number, v_idProgramacion number, v_sufijo VARCHAR2) IS
            SELECT TABLA.*
            FROM (
                 SELECT FAC_FACTURA.IDFACTURA,
                    LPAD(FAC_FACTURA.IDFACTURA, 8, 0) ||
                        LPAD(
                            (
                                SELECT COUNT(*)
                                FROM FAC_RENEGOCIACION
                                WHERE FAC_RENEGOCIACION.IDINSTITUCION = FAC_FACTURA.IDINSTITUCION
                                    AND FAC_RENEGOCIACION.IDFACTURA = FAC_FACTURA.IDFACTURA
                                    AND (FAC_RENEGOCIACION.IDINSTITUCION, FAC_RENEGOCIACION.IDFACTURA, NVL(FAC_RENEGOCIACION.IDRENEGOCIACION,'')) NOT IN (
                                        SELECT FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION,
                                            FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURA,
                                            NVL(FAC_FACTURAINCLUIDAENDISQUETE.IDRENEGOCIACION,'')
                                        FROM FAC_FACTURAINCLUIDAENDISQUETE
                                        WHERE FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION = FAC_RENEGOCIACION.IDINSTITUCION
                                            AND FAC_FACTURAINCLUIDAENDISQUETE.DEVUELTA = 'N'
                                    )
                            ), 2, 0) AS CODIGOREFERENCIA,
                    FAC_FACTURA.IMPTOTALPORPAGAR * 100 AS IMPORTE,
                    FAC_FACTURA.NUMEROFACTURA,
                    FAC_FACTURA.IDMANDATO,
                    CEN_CUENTASBANCARIAS.IDPERSONA,
                    CEN_CUENTASBANCARIAS.IDCUENTA,
                    CEN_CUENTASBANCARIAS.IBAN AS BANCO_IBAN,
                    F_RevisarCaracteresSEPA(CEN_CUENTASBANCARIAS.TITULAR) AS DEUDOR_NOMBRE, -- Se revisan los caracteres SEPA porque hay que compararlos con su resultado final
                    CEN_BANCOS.BIC AS BANCO_BIC,
                    SUBSTR(CEN_PERSONA.NIFCIF, 1, 9) AS DEUDOR_ID,
                    F_RevisarCaracteresSEPA(CEN_PERSONA.NOMBRE || ' ' ||CEN_PERSONA.APELLIDOS1 || ' ' ||  CEN_PERSONA.APELLIDOS2) AS DEUDOR_NOMBRE2 -- Se revisan los caracteres SEPA porque hay que compararlos con su resultado final
                FROM FAC_FACTURA,
                    CEN_CUENTASBANCARIAS,
                    CEN_BANCOS,
                    CEN_PERSONA
                WHERE FAC_FACTURA.IDINSTITUCION = v_idInstitucion
                    AND CEN_CUENTASBANCARIAS.IDINSTITUCION = FAC_FACTURA.IDINSTITUCION
                    AND CEN_CUENTASBANCARIAS.IDPERSONA = DECODE(FAC_FACTURA.IDCUENTADEUDOR, NULL, FAC_FACTURA.IDPERSONA, FAC_FACTURA.IDPERSONADEUDOR)
                    AND CEN_CUENTASBANCARIAS.IDCUENTA = NVL(FAC_FACTURA.IDCUENTADEUDOR, FAC_FACTURA.IDCUENTA)
                    AND CEN_CUENTASBANCARIAS.FECHABAJA IS NULL
                    AND CEN_BANCOS.CODIGO = CEN_CUENTASBANCARIAS.CBO_CODIGO
                    AND CEN_PERSONA.IDPERSONA = CEN_CUENTASBANCARIAS.IDPERSONA

                     --En estado pendiente de cobrar por banco
                    AND FAC_FACTURA.IMPTOTALPORPAGAR > 0
                    AND FAC_FACTURA.ESTADO = 5
                    AND FAC_FACTURA.NUMEROFACTURA IS NOT NULL -- JPT: Estaba dando ficheros sin numero de factura, cuando debe estar siempre indicado

                    -- Obtengo las facturas sueltas o confirmadas                    
                    AND (v_idProgramacion IS NULL OR FAC_FACTURA.IDPROGRAMACION = v_idProgramacion)

                    -- Obtengo las facturas cuya serie de facturacion coincida con el banco y sufijo actual
                    AND EXISTS (
                        SELECT 1
                        FROM FAC_SERIEFACTURACION_BANCO,
                            FAC_SUFIJO
                        WHERE FAC_SERIEFACTURACION_BANCO.IDINSTITUCION = FAC_FACTURA.IDINSTITUCION
                            AND FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION = FAC_FACTURA.IDSERIEFACTURACION
                            AND FAC_SUFIJO.IDINSTITUCION = FAC_SERIEFACTURACION_BANCO.IDINSTITUCION
                            AND FAC_SUFIJO.IDSUFIJO = FAC_SERIEFACTURACION_BANCO.IDSUFIJO
                            AND FAC_SERIEFACTURACION_BANCO.BANCOS_CODIGO = v_codigoBanco
                            AND FAC_SUFIJO.SUFIJO = v_sufijo
                            AND (v_idSerieFacturacion IS NULL OR FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION = v_idSerieFacturacion)
                    )
            ) TABLA
            WHERE (v_idProgramacion IS NULL OR TABLA.CODIGOREFERENCIA LIKE '%00') -- JPT: Esto sirve para garantizar que las programaciones cogen los recibos sin renegociacion
            ORDER BY TABLA.CODIGOREFERENCIA;

    v_nombreDeudor VARCHAR2(4000);
    v_nombreDeudor2 VARCHAR2(4000); 
    
    v_RegFactura REG_FACTURA; -- Registro con datos de la factura
    v_RegFacturas REG_FACTURAS; -- Registro con todos los datos de las facturas    
    
    v_Codretorno VARCHAR2(10) := To_Char(0);
    v_Datoserror VARCHAR2(4000) := Null;
    e_Error EXCEPTION; 

    BEGIN
        -- Calcular el identificador de persona SEPA de la institucion (la institucion siempre tiene 'ES' como codigo ISO del pais)
        v_Datoserror := 'PresentacionGeneral: Llamada a la funcion CalcularDCPersonaSepa';
        p_RegAcreedor.IDPERSONASEPAINSTITUCION := 'ES' || CalcularDCPersonaSepa('ES', p_RegAcreedor.NIF) || LPAD(NVL(p_RegAcreedor.SUFIJO, '0'), 3, '0') || RPAD(NVL(p_RegAcreedor.NIF, ' '), 28, ' ');
        
        -- Obtiene la direccion SEPA del Acreedor
        v_Datoserror := 'PresentacionGeneral: Llamada a la funcion ObtenerDireccionFacturacion';
        p_RegAcreedor.DIRECCION := ObtenerDireccionFacturacion(p_IdInstitucion, p_RegAcreedor.IDPERSONA); 
                     
        -- Compruebo que existe la direccion de facturacion del acreedor
        IF (p_RegAcreedor.DIRECCION.DOMICILIO IS NULL) THEN 
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                        ' ' ||  p_RegAcreedor.NOMBRE ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                        ' ' ||  p_RegAcreedor.NIF ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeDireccionFacturacionAcreedor', p_Idioma);
            v_Codretorno := To_Char(5414);
            RAISE e_Error;
        END IF; 
        
        v_Datoserror := 'PresentacionGeneral: Llamada al procedimiento CargarFechasSEPA';
        CargarFechasSEPA(
            p_IdInstitucion,
            p_IdSerieFacturacion,
            p_IdProgramacion,
            p_FechaPresentacion,
            p_FechaCargoFRST,
            p_FechaCargoRCUR,
            p_FechaCargoCOR1,
            p_FechaCargoB2B,
            v_RegFacturas,
            v_Codretorno,
            v_Datoserror);
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF; 

        v_Datoserror := 'PresentacionGeneral: Bucle del cursor de facturas (' || p_IdInstitucion || ',' || p_RegAcreedor.CODIGOBANCO || ', ' || p_IdSerieFacturacion || ', ' || p_IdProgramacion || ', ' || p_RegAcreedor.SUFIJO || ')';
        FOR v_CursorFacturas IN c_Facturas (p_IdInstitucion, p_RegAcreedor.CODIGOBANCO, p_IdSerieFacturacion, p_IdProgramacion, p_RegAcreedor.SUFIJO) LOOP

            -- Cargo los datos del registro de presentacion
            v_RegFactura.IDINSTITUCION := p_IdInstitucion;
            v_RegFactura.IDPERSONA := v_CursorFacturas.IDPERSONA;
            v_RegFactura.IDCUENTA := v_CursorFacturas.IDCUENTA;
            v_RegFactura.IDFACTURA := v_CursorFacturas.IDFACTURA;
            v_RegFactura.BANCO_IBAN := v_CursorFacturas.BANCO_IBAN;
            v_RegFactura.BANCO_BIC := v_CursorFacturas.BANCO_BIC;
            v_RegFactura.CODIGOREFERENCIA := v_CursorFacturas.CODIGOREFERENCIA;
            v_RegFactura.NUMEROFACTURA := v_CursorFacturas.NUMEROFACTURA;
            v_RegFactura.IMPORTE := v_CursorFacturas.IMPORTE;
            v_RegFactura.DEUDOR_NOMBRE := v_CursorFacturas.DEUDOR_NOMBRE;
            v_RegFactura.IDMANDATO := v_CursorFacturas.IDMANDATO;
            v_RegFactura.DEUDOR_ID := v_CursorFacturas.DEUDOR_ID;
            v_RegFactura.SECUENCIAREAL := NULL;
            
            v_nombreDeudor := RPAD(REGEXP_REPLACE(UPPER(v_CursorFacturas.DEUDOR_NOMBRE), '[^ABCDEFGHIJ-NOP-TUV-Z0-9]', ''), 70, ' ');
            v_nombreDeudor2 := RPAD(REGEXP_REPLACE(UPPER(v_CursorFacturas.DEUDOR_NOMBRE2), '[^ABCDEFGHIJ-NOP-TUV-Z0-9]', ''), 70, ' '); 

            IF (LENGTH(TRIM(v_CursorFacturas.DEUDOR_NOMBRE2)) > 0 -- Tiene que tener nombre de deudor
                AND NVL(LENGTH(v_RegFactura.DEUDOR_NOMBRE), 0) <= 66 -- MAXIMO 70 CARACTERES Y MINIMO ESPACIO + DOS PARENTESIS + UNA LETRA
                AND v_nombreDeudor  <> v_nombreDeudor2) THEN -- Tiene que tener dos deudores (pero solo muestra 70 letras)
                    v_nombreDeudor := v_RegFactura.DEUDOR_NOMBRE || ' (';
                    v_nombreDeudor := v_nombreDeudor ||  SUBSTR(TRIM(v_CursorFacturas.DEUDOR_NOMBRE2), 1, 67 - NVL(LENGTH(v_RegFactura.DEUDOR_NOMBRE), 0));
                    v_nombreDeudor := v_nombreDeudor || ')';
                    v_RegFactura.DEUDOR_NOMBRE := v_nombreDeudor;
            END IF;
            
            v_Datoserror := 'PresentacionGeneral: Llamada al procedimiento CargarFacturaEnArrayDeudor';
            CargarFacturaEnArrayDeudor(
                p_RegAcreedor,
                v_RegFactura,
                v_RegFacturas,
                p_Idioma,
                v_Codretorno,
                v_Datoserror);
            IF v_Codretorno <> To_Char(0) THEN
                RAISE e_Error;
            END IF; 
        END LOOP; 
        
        v_Datoserror := 'PresentacionGeneral: Llamada al procedimiento GestionarFicheros';
        GestionarFicheros (
            p_IdInstitucion,
            p_IdSerieFacturacion,
            p_IdProgramacion,
            p_PathFichero,
            p_UsuModificacion,
            p_RegAcreedor,
            v_RegFacturas,
            p_NumFicheros,
            v_Codretorno,
            v_Datoserror                    
        );
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF; 

        v_Datoserror := 'PresentacionGeneral: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN e_Error THEN
                p_NumFicheros := To_Char(0);
                p_CodRetorno := v_Codretorno;
                p_DatosError := v_Datoserror;

            WHEN OTHERS THEN
                p_NumFicheros := To_Char(0);
                p_CodRetorno := To_Char(Sqlcode);
                p_DatosError := v_Datoserror || ', ' || Sqlerrm;
    END PresentacionGeneral;

    /****************************************************************************************************************/
    /* Nombre: Presentacion */
    /* Descripcion: Generacion de los ficheros de adeudo (SEPA) */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDSERIEFACTURACION - IN - Identificador de la serie de facturacion - NUMBER */
    /* P_IDPROGRAMACION - IN - Identificador de la programacion - NUMBER */
    /* P_FECHAPRESENTACION - IN - Fecha de presentacion del fichero en el banco (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOFRST - IN - Fecha de cargo del fichero para facturas FRST (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGORCUR - IN - Fecha de cargo del fichero para facturas RCUR (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOCOR1 - IN - Fecha de cargo del fichero para facturas COR1 (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOB2B - IN - Fecha de cargo del fichero para facturas B2B (YYYYMMDD) - VARCHAR2 (8) */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_NFICHEROS - OUT - Devuelve el numero de ficheros bancarios generados - VARCHAR2(10) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 18/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 20/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 24/03/2014 - Autor: Jorge Paez Trivino */
    /* Version: 4.0 - Fecha Modificacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
     PROCEDURE Presentacion(
        p_Idinstitucion IN Number,
        p_Idseriefacturacion IN Number,
        p_Idprogramacion IN Number,
        p_FechaPresentacion IN Varchar2,
        p_FechaCargoFRST IN Varchar2,
        p_FechaCargoRCUR IN Varchar2,
        p_FechaCargoCOR1 IN Varchar2,
        p_FechaCargoB2B IN Varchar2,
        p_Pathfichero IN Varchar2,
        p_Usumodificacion IN Number,
        p_Idioma IN Number,
        p_Nficheros OUT Varchar2,
        p_Codretorno OUT Varchar2,
        p_Datoserror OUT Varchar2) IS

        -- Cursor con los bancos activos y sus series de facturacion asociadas
        -- No puede tener en la select FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION porque podria salir varias veces un banco + sufijo con diferentes series
        CURSOR C_BANCOS (p_idSerieFacturacion number) IS
            SELECT DISTINCT FAC_BANCOINSTITUCION.BANCOS_CODIGO AS CODIGOBANCO,
                FAC_BANCOINSTITUCION.COD_BANCO AS ENTIDADBANCO,
                FAC_BANCOINSTITUCION.COD_SUCURSAL AS OFICINABANCO,
                FAC_BANCOINSTITUCION.IBAN,
                FAC_BANCOINSTITUCION.CONFIGFICHEROSESQUEMA, -- 0=ficheroCORE+COR1+B2B; 1=ficheroCORE+COR1 + ficheroB2B; 2=ficheroCORE + ficheroCOR1 + ficheroB2B
                FAC_BANCOINSTITUCION.CONFIGFICHEROSSECUENCIA, -- 0=ficheroFRST+RCUR; 1=ficheroFRST + ficheroRCUR; 2=ficheroTodoRCUR
                FAC_BANCOINSTITUCION.CONFIGLUGARESQUEMASECUENCIA, -- 0=bloqueAcreedor; 1=registrosIndividuales
                FAC_BANCOINSTITUCION.CONFIGCONCEPTOAMPLIADO,-- 0=Concepto140; 1=concepto640
                CEN_BANCOS.NOMBRE AS NOMBRE_BANCO,
                CEN_BANCOS.BIC,
                FAC_SERIEFACTURACION_BANCO.IDSUFIJO,
                (
                    SELECT FAC_SUFIJO.SUFIJO
                    FROM FAC_SUFIJO
                    WHERE FAC_SUFIJO.IDINSTITUCION = FAC_SERIEFACTURACION_BANCO.IDINSTITUCION
                        AND FAC_SUFIJO.IDSUFIJO = FAC_SERIEFACTURACION_BANCO.IDSUFIJO
                ) AS SUFIJO               
            FROM FAC_BANCOINSTITUCION,
                FAC_SERIEFACTURACION_BANCO,
                CEN_BANCOS
            WHERE FAC_BANCOINSTITUCION.IDINSTITUCION = P_IDINSTITUCION
                AND FAC_SERIEFACTURACION_BANCO.IDINSTITUCION = FAC_BANCOINSTITUCION.IDINSTITUCION
                AND FAC_SERIEFACTURACION_BANCO.BANCOS_CODIGO = FAC_BANCOINSTITUCION.BANCOS_CODIGO
                AND (p_idSerieFacturacion IS NULL OR FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION = p_idSerieFacturacion)
                AND FAC_BANCOINSTITUCION.FECHABAJA IS NULL
                AND CEN_BANCOS.CODIGO = FAC_BANCOINSTITUCION.COD_BANCO;

        v_Nficheros NUMBER := 0; /* Numero de ficheros de adeudos generados */
        v_RegAcreedor REG_ACREEDOR;
        v_Descripcion FAC_SERIEFACTURACION.DESCRIPCION%TYPE;
        
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION;

    Begin
        /* Obtenemos el nombre de la institucion y su Idpersona asociado */
        v_Datoserror := 'Presentacion: Obtencion del nombre y la persona de la institucion';
        Select NOMBRE, IDPERSONA
            Into v_RegAcreedor.NOMBRE, v_RegAcreedor.IDPERSONA
        From CEN_INSTITUCION
        Where IDINSTITUCION = p_Idinstitucion;

        /* Obtenemos el NIF de la institucion */
        v_Datoserror := 'Presentacion: Obtencion del NIF de la institucion';
        Select Substr(NIFCIF, 1, 9)
            Into v_RegAcreedor.NIF
        From CEN_PERSONA
        Where IDPERSONA = v_RegAcreedor.IDPERSONA;
        
        -- -1=Error; 0=Txt; 1=Txt+Xml; 2=Xml
        v_Datoserror := 'Presentacion: Obtiene el parametro FAC.SEPA_TIPO_FICHEROS';
        v_RegAcreedor.TIPOSFICHEROS := F_SIGA_GETPARAMETRO('FAC', 'SEPA_TIPO_FICHEROS', p_Idinstitucion); 
         IF (v_RegAcreedor.TIPOSFICHEROS = '-1') THEN             
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorParamSepaTipoFicheros.mensajeCondicionesIncumplidas', p_Idioma);
            v_Codretorno := To_Char(5417);
            Raise e_Error;
         END IF;

        v_Datoserror := 'Presentacion: Bucle del cursor de bancos';
        For v_Bancos In C_BANCOS(p_Idseriefacturacion) Loop
        
            -- Control que comprueba que se ha indicado el BIC del banco
            IF (v_Bancos.BIC IS NULL) THEN            
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorBIC.mensajeCondicionesIncumplidas', p_Idioma) || ' ' || v_Bancos.NOMBRE_BANCO;
                v_Codretorno := To_Char(5422);
                Raise e_Error;
            END IF; 

            -- Control que comprueba que se ha indicado el sufijo
            IF (v_Bancos.SUFIJO IS NULL) THEN
                SELECT FAC_SERIEFACTURACION.DESCRIPCION
                    INTO v_Descripcion
                FROM FAC_SERIEFACTURACION_BANCO,
                    FAC_SERIEFACTURACION
                WHERE FAC_SERIEFACTURACION_BANCO.IDINSTITUCION = P_IDINSTITUCION
                    AND FAC_SERIEFACTURACION_BANCO.BANCOS_CODIGO = v_Bancos.CODIGOBANCO
                    AND (p_idSerieFacturacion IS NULL OR FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION = p_idSerieFacturacion) 
                    AND FAC_SERIEFACTURACION.IDINSTITUCION = FAC_SERIEFACTURACION_BANCO.IDINSTITUCION
                    AND FAC_SERIEFACTURACION.IDSERIEFACTURACION = FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION
                    AND NOT EXISTS(
                        SELECT 1
                        FROM FAC_SUFIJO
                        WHERE FAC_SUFIJO.IDINSTITUCION = FAC_SERIEFACTURACION_BANCO.IDINSTITUCION
                            AND FAC_SUFIJO.IDSUFIJO = FAC_SERIEFACTURACION_BANCO.IDSUFIJO
                    )
                    AND ROWNUM = 1;
            
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorSufijos.mensajeCondicionesIncumplidas', p_Idioma) ||
                        ' ' || v_Descripcion ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorSufijos.mensajeCuentaBancaria', p_Idioma) ||
                        ' ' || v_Bancos.IBAN ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorSufijos.mensajeSinSufijo', p_Idioma);
                v_Codretorno := To_Char(5421);
                Raise e_Error;
            END IF;
            
            v_RegAcreedor.CODIGOBANCO := v_Bancos.CODIGOBANCO;
            v_RegAcreedor.ENTIDADBANCO := v_Bancos.ENTIDADBANCO;
            v_RegAcreedor.OFICINABANCO := v_Bancos.OFICINABANCO;
            v_RegAcreedor.BANCO_IBAN := v_Bancos.IBAN;
            v_RegAcreedor.BANCO_BIC := v_Bancos.BIC;
            v_RegAcreedor.SUFIJO := v_Bancos.SUFIJO;
            v_RegAcreedor.CONFIGFICHEROSESQUEMA := v_Bancos.CONFIGFICHEROSESQUEMA;
            v_RegAcreedor.CONFIGFICHEROSSECUENCIA := v_Bancos.CONFIGFICHEROSSECUENCIA;
            v_RegAcreedor.CONFIGLUGARESQUEMASECUENCIA := v_Bancos.CONFIGLUGARESQUEMASECUENCIA;
            v_RegAcreedor.CONFIGCONCEPTOAMPLIADO := v_Bancos.CONFIGCONCEPTOAMPLIADO;            
            v_RegAcreedor.IDSUFIJO := v_Bancos.IDSUFIJO;

            v_Datoserror := 'Presentacion: Llamada al procedimiento PresentacionGeneral';
            PresentacionGeneral (
                p_Idinstitucion,
                p_Idseriefacturacion,
                p_Idprogramacion,
                p_FechaPresentacion,
                p_FechaCargoFRST,
                p_FechaCargoRCUR,
                p_FechaCargoCOR1,
                p_FechaCargoB2B,
                p_Pathfichero,
                p_Usumodificacion,
                p_Idioma,
                v_RegAcreedor,
                v_Nficheros,
                v_Codretorno,
                v_Datoserror);
            If v_Codretorno <> To_Char(0) Then
                Raise e_Error;
            End If;
        End Loop;

        v_Datoserror := 'Presentacion: Actualizacion de los parametros de salida';
        p_Nficheros := To_Char(v_Nficheros);
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

    Exception
        When e_Error Then
            Rollback;
            p_Nficheros := To_Char(0);
            p_Codretorno := v_Codretorno;
            p_Datoserror := v_Datoserror;

        When Others Then
            Rollback;
            p_Nficheros := To_Char(0);
            p_Codretorno := To_Char(Sqlcode);
            p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    End Presentacion;
    
/****************************************************************************************************************/
    /* Nombre: PresentacionGeneral */
    /* Descripcion: Codigo generico para la presentacion de los ficheros de adeudo (SEPA) */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_REGACREEDOR - IN OUT - Registro con los datos del acreedor - REG_ACREEDOR */
    /* P_REGFACTURAS - IN OUT - Registro con los datos de las facturas - REG_FACTURAS */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 11/03/2015 - Autor: Jorge Paez Trivino */
  /****************************************************************************************************************/
    Procedure Regenerar_PresentacionGeneral (
        p_IdInstitucion IN NUMBER,
        p_PathFichero IN VARCHAR2,
        p_Idioma IN NUMBER,
        p_RegAcreedor IN OUT REG_ACREEDOR,
        p_RegFacturas IN OUT REG_FACTURAS,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS
    
        CURSOR c_FIED (v_idInstitucion FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION%TYPE, v_idDisqueteCargos FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS%TYPE) IS
            SELECT FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURA,
                FAC_FACTURAINCLUIDAENDISQUETE.IDRECIBO AS CODIGOREFERENCIA,
                FAC_FACTURAINCLUIDAENDISQUETE.IMPORTE * 100 AS IMPORTE,
                FAC_FACTURA.NUMEROFACTURA,
                FAC_FACTURA.IDMANDATO,
                FAC_FACTURAINCLUIDAENDISQUETE.IDPERSONA,
                FAC_FACTURAINCLUIDAENDISQUETE.IDCUENTA,
                CEN_CUENTASBANCARIAS.IBAN AS BANCO_IBAN,
                F_RevisarCaracteresSEPA(CEN_CUENTASBANCARIAS.TITULAR) AS DEUDOR_NOMBRE, -- Se revisan los caracteres SEPA porque hay que compararlos con su resultado final
                CEN_BANCOS.BIC AS BANCO_BIC,
                SUBSTR(CEN_PERSONA.NIFCIF, 1, 9) AS DEUDOR_ID,
                F_RevisarCaracteresSEPA(CEN_PERSONA.NOMBRE || ' ' ||CEN_PERSONA.APELLIDOS1 || ' ' ||  CEN_PERSONA.APELLIDOS2) AS DEUDOR_NOMBRE2, -- Se revisan los caracteres SEPA porque hay que compararlos con su resultado final
                FAC_FACTURAINCLUIDAENDISQUETE.SECUENCIA                           
            FROM FAC_FACTURAINCLUIDAENDISQUETE,
                 FAC_FACTURA,
                CEN_CUENTASBANCARIAS,
                CEN_BANCOS,
                CEN_PERSONA     
            WHERE FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION = v_idInstitucion
                AND FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS = v_idDisqueteCargos
                AND FAC_FACTURA.IDINSTITUCION = FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION
                AND FAC_FACTURA.IDFACTURA = FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURA
                AND CEN_CUENTASBANCARIAS.IDINSTITUCION = FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION
                AND CEN_CUENTASBANCARIAS.IDPERSONA = FAC_FACTURAINCLUIDAENDISQUETE.IDPERSONA
                AND CEN_CUENTASBANCARIAS.IDCUENTA = FAC_FACTURAINCLUIDAENDISQUETE.IDCUENTA
                --AND CEN_CUENTASBANCARIAS.FECHABAJA IS NULL -- Deben regenerarse aunque ahora esten de baja
                AND CEN_BANCOS.CODIGO = CEN_CUENTASBANCARIAS.CBO_CODIGO
                AND CEN_PERSONA.IDPERSONA = CEN_CUENTASBANCARIAS.IDPERSONA
            ORDER BY CODIGOREFERENCIA; 

    v_nombreDeudor VARCHAR2(4000);
    v_nombreDeudor2 VARCHAR2(4000); 
    
    v_RegFactura REG_FACTURA; -- Registro con datos de la factura   
    v_numFicheros NUMBER := 0; -- Esta variable es necesaria pero no la usamos
    
    v_Codretorno VARCHAR2(10) := To_Char(0);
    v_Datoserror VARCHAR2(4000) := Null;
    e_Error EXCEPTION; 

    BEGIN
        -- Calcular el identificador de persona SEPA de la institucion (la institucion siempre tiene 'ES' como codigo ISO del pais)
        v_Datoserror := 'Regenerar_PresentacionGeneral: Llamada a la funcion CalcularDCPersonaSepa';
        p_RegAcreedor.IDPERSONASEPAINSTITUCION := 'ES' || CalcularDCPersonaSepa('ES', p_RegAcreedor.NIF) || LPAD(NVL(p_RegAcreedor.SUFIJO, '0'), 3, '0') || RPAD(NVL(p_RegAcreedor.NIF, ' '), 28, ' ');
        
        -- Obtiene la direccion SEPA del Acreedor
        v_Datoserror := 'Regenerar_PresentacionGeneral: Llamada a la funcion ObtenerDireccionFacturacion';
        p_RegAcreedor.DIRECCION := ObtenerDireccionFacturacion(p_IdInstitucion, p_RegAcreedor.IDPERSONA); 
                     
        -- Compruebo que existe la direccion de facturacion del acreedor
        IF (p_RegAcreedor.DIRECCION.DOMICILIO IS NULL) THEN 
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeCondicionesIncumplidas', p_Idioma) ||
                        ' ' ||  p_RegAcreedor.NOMBRE ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeIdentificacion', p_Idioma) ||
                        ' ' ||  p_RegAcreedor.NIF ||
                        ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorMandatos.mensajeDireccionFacturacionAcreedor', p_Idioma);
            v_Codretorno := To_Char(5414);
            RAISE e_Error;
        END IF; 
        
        /* NO NECESITA CargarFechasSEPA() */ 

        v_Datoserror := 'Regenerar_PresentacionGeneral: Bucle del cursor de facturas (' || p_IdInstitucion || ',' || p_RegFacturas.IDDISQUETECARGOS || ')';
        FOR v_CursorFacturas IN c_FIED (p_IdInstitucion, p_RegFacturas.IDDISQUETECARGOS) LOOP

            -- Cargo los datos del registro de presentacion
            v_RegFactura.IDINSTITUCION := p_IdInstitucion;
            v_RegFactura.IDPERSONA := v_CursorFacturas.IDPERSONA;
            v_RegFactura.IDCUENTA := v_CursorFacturas.IDCUENTA;
            v_RegFactura.IDFACTURA := v_CursorFacturas.IDFACTURA;
            v_RegFactura.BANCO_IBAN := v_CursorFacturas.BANCO_IBAN;
            v_RegFactura.BANCO_BIC := v_CursorFacturas.BANCO_BIC;
            v_RegFactura.CODIGOREFERENCIA := v_CursorFacturas.CODIGOREFERENCIA;
            v_RegFactura.NUMEROFACTURA := v_CursorFacturas.NUMEROFACTURA;
            v_RegFactura.IMPORTE := v_CursorFacturas.IMPORTE;
            v_RegFactura.DEUDOR_NOMBRE := v_CursorFacturas.DEUDOR_NOMBRE;
            v_RegFactura.IDMANDATO := v_CursorFacturas.IDMANDATO;
            v_RegFactura.DEUDOR_ID := v_CursorFacturas.DEUDOR_ID;
            v_RegFactura.SECUENCIAREAL := v_CursorFacturas.SECUENCIA;
            
            v_nombreDeudor := RPAD(REGEXP_REPLACE(UPPER(v_CursorFacturas.DEUDOR_NOMBRE), '[^ABCDEFGHIJ-NOP-TUV-Z0-9]', ''), 70, ' ');
            v_nombreDeudor2 := RPAD(REGEXP_REPLACE(UPPER(v_CursorFacturas.DEUDOR_NOMBRE2), '[^ABCDEFGHIJ-NOP-TUV-Z0-9]', ''), 70, ' '); 

            IF (LENGTH(TRIM(v_CursorFacturas.DEUDOR_NOMBRE2)) > 0 -- Tiene que tener nombre de deudor
                AND NVL(LENGTH(v_RegFactura.DEUDOR_NOMBRE), 0) <= 66 -- MAXIMO 70 CARACTERES Y MINIMO ESPACIO + DOS PARENTESIS + UNA LETRA
                AND v_nombreDeudor  <> v_nombreDeudor2) THEN -- Tiene que tener dos deudores (pero solo muestra 70 letras)
                    v_nombreDeudor := v_RegFactura.DEUDOR_NOMBRE || ' (';
                    v_nombreDeudor := v_nombreDeudor ||  SUBSTR(TRIM(v_CursorFacturas.DEUDOR_NOMBRE2), 1, 67 - NVL(LENGTH(v_RegFactura.DEUDOR_NOMBRE), 0));
                    v_nombreDeudor := v_nombreDeudor || ')';
                    v_RegFactura.DEUDOR_NOMBRE := v_nombreDeudor;
            END IF;
            
            v_Datoserror := 'Regenerar_PresentacionGeneral: Llamada al procedimiento CargarFacturaEnArrayDeudor';
            CargarFacturaEnArrayDeudor(
                p_RegAcreedor,
                v_RegFactura,
                p_RegFacturas,
                p_Idioma,
                v_Codretorno,
                v_Datoserror);
            IF v_Codretorno <> To_Char(0) THEN
                RAISE e_Error;
            END IF; 
        END LOOP; 
        
        v_Datoserror := 'Regenerar_PresentacionGeneral: Llamada al procedimiento GestionarFicheros';
        /* NO NECESITA:
            - InsertarDisqueteCargos()
            - insertarFIED()
            - UPDATE FAC_FACTURA
            - UPDATE CEN_MANDATOS_CUENTASBANCARIAS */        
        GestionarFicheros (
            p_IdInstitucion,
            NULL,
            NULL,
            p_PathFichero,
            NULL,
            p_RegAcreedor,
            p_RegFacturas,
            v_numFicheros,
            v_Codretorno,
            v_Datoserror                    
        );
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF; 

        v_Datoserror := 'Regenerar_PresentacionGeneral: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN e_Error THEN
                p_CodRetorno := v_Codretorno;
                p_DatosError := v_Datoserror;

            WHEN OTHERS THEN
                p_CodRetorno := To_Char(Sqlcode);
                p_DatosError := v_Datoserror || ', ' || Sqlerrm; 
    END Regenerar_PresentacionGeneral; 
    
 /****************************************************************************************************************/
    /* Nombre: Regenerar_Presentacion */
    /* Descripcion: Regeneracion de los ficheros de adeudo (SEPA) */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDDISQUETECARGOS - IN - Identificador del disquete de cargos - NUMBER */
    /* P_FECHAPRESENTACION - IN - Fecha de presentacion del fichero en el banco (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOFRST - IN - Fecha de cargo del fichero para facturas FRST (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGORCUR - IN - Fecha de cargo del fichero para facturas RCUR (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOCOR1 - IN - Fecha de cargo del fichero para facturas COR1 (YYYYMMDD) - VARCHAR2 (8) */
    /* P_FECHACARGOB2B - IN - Fecha de cargo del fichero para facturas B2B (YYYYMMDD) - VARCHAR2 (8) */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 11/03/2015 - Autor: Jorge Paez Trivino - Cambios: Adaptacion a XML */
  /****************************************************************************************************************/
     PROCEDURE Regenerar_Presentacion(
        p_Idinstitucion IN NUMBER,
        p_IdDisqueteCargos IN NUMBER,
        p_FechaPresentacion IN VARCHAR2,
        p_FechaCargoFRST IN VARCHAR2,
        p_FechaCargoRCUR IN VARCHAR2,
        p_FechaCargoCOR1 IN VARCHAR2,
        p_FechaCargoB2B IN VARCHAR2,
        p_Pathfichero IN VARCHAR2,
        p_Idioma IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2) IS

        v_RegAcreedor REG_ACREEDOR;
        v_RegFacturas REG_FACTURAS;
        v_NombreBanco CEN_BANCOS.NOMBRE%TYPE;
        
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error EXCEPTION;

    Begin
        v_Datoserror := 'Regenerar_Presentacion: Obtiene los datos del acreedor, con el banco y sufijo utilizado';
        SELECT FAC_DISQUETECARGOS.FECHAPRESENTACION,
                FAC_DISQUETECARGOS.FECHARECIBOSPRIMEROS,
                FAC_DISQUETECARGOS.FECHARECIBOSRECURRENTES,
                FAC_DISQUETECARGOS.FECHARECIBOSCOR1,
                FAC_DISQUETECARGOS.FECHARECIBOSB2B,
                FAC_DISQUETECARGOS.IDSUFIJO,
                (
                    SELECT FAC_SUFIJO.SUFIJO
                    FROM FAC_SUFIJO
                    WHERE FAC_SUFIJO.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION
                        AND FAC_SUFIJO.IDSUFIJO = FAC_DISQUETECARGOS.IDSUFIJO
                ) AS SUFIJO,
                FAC_BANCOINSTITUCION.BANCOS_CODIGO,
                FAC_BANCOINSTITUCION.COD_BANCO,
                FAC_BANCOINSTITUCION.COD_SUCURSAL,
                FAC_BANCOINSTITUCION.IBAN,
                FAC_BANCOINSTITUCION.CONFIGFICHEROSESQUEMA, -- 0=ficheroCORE+COR1+B2B; 1=ficheroCORE+COR1 + ficheroB2B; 2=ficheroCORE + ficheroCOR1 + ficheroB2B
                FAC_BANCOINSTITUCION.CONFIGFICHEROSSECUENCIA, -- 0=ficheroFRST+RCUR; 1=ficheroFRST + ficheroRCUR; 2=ficheroTodoRCUR
                FAC_BANCOINSTITUCION.CONFIGLUGARESQUEMASECUENCIA, -- 0=bloqueAcreedor; 1=registrosIndividuales
                FAC_BANCOINSTITUCION.CONFIGCONCEPTOAMPLIADO,-- 0=Concepto140; 1=concepto640
                CEN_BANCOS.NOMBRE AS NOMBRE_BANCO,
                CEN_BANCOS.BIC,                
                CEN_INSTITUCION.NOMBRE,
                CEN_INSTITUCION.IDPERSONA,
                SUBSTR(CEN_PERSONA.NIFCIF, 1, 9)
            INTO
                v_RegFacturas.FECHAPRESENTACION,
                v_RegFacturas.FECHACARGO_COREFRST,
                v_RegFacturas.FECHACARGO_CORERCUR,
                v_RegFacturas.FECHACARGO_COR1,
                v_RegFacturas.FECHACARGO_B2B,
                v_RegAcreedor.IDSUFIJO,
                v_RegAcreedor.SUFIJO,
                v_RegAcreedor.CODIGOBANCO,
                v_RegAcreedor.ENTIDADBANCO,
                v_RegAcreedor.OFICINABANCO,
                v_RegAcreedor.BANCO_IBAN,
                v_RegAcreedor.CONFIGFICHEROSESQUEMA,
                v_RegAcreedor.CONFIGFICHEROSSECUENCIA,
                v_RegAcreedor.CONFIGLUGARESQUEMASECUENCIA,
                v_RegAcreedor.CONFIGCONCEPTOAMPLIADO,
                v_NombreBanco,
                v_RegAcreedor.BANCO_BIC,                
                v_RegAcreedor.NOMBRE,
                v_RegAcreedor.IDPERSONA,
                v_RegAcreedor.NIF
        FROM FAC_DISQUETECARGOS,
            FAC_BANCOINSTITUCION,
            CEN_BANCOS,
            CEN_INSTITUCION,
            CEN_PERSONA
        WHERE FAC_DISQUETECARGOS.IDINSTITUCION = p_IdInstitucion
            AND FAC_DISQUETECARGOS.IDDISQUETECARGOS = p_IdDisqueteCargos
            AND FAC_BANCOINSTITUCION.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION
            AND FAC_BANCOINSTITUCION.BANCOS_CODIGO = FAC_DISQUETECARGOS.BANCOS_CODIGO             
            --AND FAC_BANCOINSTITUCION.FECHABAJA IS NULL  -- Deben regenerarse aunque ahora esten de baja
            AND CEN_BANCOS.CODIGO = FAC_BANCOINSTITUCION.COD_BANCO            
            AND CEN_INSTITUCION.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION
            AND CEN_PERSONA.IDPERSONA = CEN_INSTITUCION.IDPERSONA;
            
        v_Datoserror := 'Regenerar_Presentacion: Cargar fechas';
        IF (p_FechaPresentacion IS NOT NULL) THEN   
            v_RegFacturas.FECHAPRESENTACION := TO_DATE(p_FechaPresentacion, 'YYYYMMDD');
            v_RegFacturas.FECHACARGO_COREFRST := TO_DATE(p_FechaCargoFRST, 'YYYYMMDD');
            v_RegFacturas.FECHACARGO_CORERCUR := TO_DATE(p_FechaCargoRCUR, 'YYYYMMDD');
            v_RegFacturas.FECHACARGO_COR1 := TO_DATE(p_FechaCargoCOR1, 'YYYYMMDD');
            v_RegFacturas.FECHACARGO_B2B := TO_DATE(p_FechaCargoB2B, 'YYYYMMDD');
        END IF;
        
        -- -1=Error; 0=Txt; 1=Txt+Xml; 2=Xml
        v_Datoserror := 'Regenerar_Presentacion: Obtiene el parametro FAC.SEPA_TIPO_FICHEROS';
        v_RegAcreedor.TIPOSFICHEROS := F_SIGA_GETPARAMETRO('FAC', 'SEPA_TIPO_FICHEROS', p_Idinstitucion); 
         IF (v_RegAcreedor.TIPOSFICHEROS = '-1') THEN             
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorParamSepaTipoFicheros.mensajeCondicionesIncumplidas', p_Idioma);
            v_Codretorno := To_Char(5417);
            Raise e_Error;
         END IF;
         
        -- Control que comprueba que se ha indicado el BIC del banco
        IF (v_RegAcreedor.BANCO_BIC IS NULL) THEN            
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorBIC.mensajeCondicionesIncumplidas', p_Idioma) || ' ' || v_NombreBanco;
            v_Codretorno := To_Char(5422);
            Raise e_Error;
        END IF; 
        
        -- Si es antiguo, intento calcular el sufijo actual
        IF (v_RegAcreedor.IDSUFIJO IS NULL) THEN        
            BEGIN        
                v_Datoserror := 'Regenerar_Presentacion: Calcula el sufijo utilizado por el disquete de cargos actual';
                SELECT TABLA.SUFIJO, TABLA.IDSUFIJO
                    INTO v_RegAcreedor.SUFIJO, v_RegAcreedor.IDSUFIJO
                FROM (
                        SELECT FAC_SUFIJO.SUFIJO, FAC_SUFIJO.IDSUFIJO, COUNT(*) AS CONTADOR
                        FROM FAC_DISQUETECARGOS,
                            FAC_FACTURAINCLUIDAENDISQUETE,
                            FAC_FACTURA,
                            FAC_SERIEFACTURACION_BANCO,
                            FAC_SUFIJO
                        WHERE FAC_DISQUETECARGOS.IDINSTITUCION = p_IdInstitucion
                            AND FAC_DISQUETECARGOS.IDDISQUETECARGOS = p_IdDisqueteCargos   
                            AND FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION
                            AND FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS = FAC_DISQUETECARGOS.IDDISQUETECARGOS
                            AND FAC_FACTURA.IDINSTITUCION = FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION
                            AND FAC_FACTURA.IDFACTURA = FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURA
                            AND FAC_SERIEFACTURACION_BANCO.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION
                            AND FAC_SERIEFACTURACION_BANCO.BANCOS_CODIGO = FAC_DISQUETECARGOS.BANCOS_CODIGO
                            AND FAC_SERIEFACTURACION_BANCO.IDSERIEFACTURACION = FAC_FACTURA.IDSERIEFACTURACION
                            AND FAC_SUFIJO.IDINSTITUCION = FAC_SERIEFACTURACION_BANCO.IDINSTITUCION
                            AND FAC_SUFIJO.IDSUFIJO = FAC_SERIEFACTURACION_BANCO.IDSUFIJO     
                        GROUP BY FAC_SUFIJO.SUFIJO, FAC_SUFIJO.IDSUFIJO
                        ORDER BY CONTADOR DESC 
                    ) TABLA    
                WHERE ROWNUM = 1;
            
                EXCEPTION WHEN NO_DATA_FOUND THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorSufijos.mensajeCondicionesIncumplidas', p_Idioma) ||
                            ' ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorSufijos.mensajeSufijoDisqueteCargos', p_Idioma) ||
                            ' ' || p_IdDisqueteCargos;
                    v_Codretorno := To_Char(5421);
                    Raise e_Error;
            END;
        END IF; 
        
        v_RegFacturas.IDDISQUETECARGOS := p_IdDisqueteCargos;

        v_Datoserror := 'Regenerar_Presentacion: Llamada al procedimiento Regenerar_PresentacionGeneral';
        Regenerar_PresentacionGeneral (
            p_Idinstitucion,
            p_Pathfichero,
            p_Idioma,
            v_RegAcreedor,
            v_RegFacturas,
            v_Codretorno,
            v_Datoserror);
        IF v_Codretorno <> TO_CHAR(0) THEN
            Raise e_Error;
        END IF;

        v_Datoserror := 'Regenerar_Presentacion: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

    EXCEPTION
        WHEN e_Error THEN
            ROLLBACK;
            p_Codretorno := v_Codretorno;
            p_Datoserror := v_Datoserror; 

        WHEN OTHERS THEN
            ROLLBACK;
            p_Codretorno := To_Char(Sqlcode);
            p_Datoserror := v_Datoserror || ', ' || Sqlerrm; 
    END Regenerar_Presentacion; 
    
    /****************************************************************************************************************/
    /* Nombre: CARGADISQUETEDEVOLUCIONES */
    /* Descripcion: Carga en la tabla de la base de datos FAC_DISQUETEDEVOLUCIONES */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_CODIGOBANCO - IN - Codigo del banco - VARCHAR2(4) */
    /* P_FECHAFICHERO - IN - Fecha de confeccion del fichero - DATE */
    /* P_NOMBREFICHERO - IN - Nombre del fichero - VARCHAR2(80) */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_IDDISQUETEDEVOLUCIONES - OUT - Identificador del disquete devuelto - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* */
    /* Version: 1.0 - Fecha Creacion: 18/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 17/01/2014 - Autor: Jorge Paez Trivino */        
  /****************************************************************************************************************/
    Procedure Cargadisquetedevoluciones(
        p_Idinstitucion In Number,
        p_Codigobanco In Varchar2,
        p_Fechafichero In Date,
        p_Nombrefichero In Varchar2,
        p_Usumodificacion In Number,
        p_Iddisquetedevoluciones Out Number,
        p_Codretorno Out Varchar2,
        p_Datoserror Out Varchar2) Is

        v_Iddisquetedevoluciones Fac_Disquetedevoluciones.Iddisquetedevoluciones%Type; /* Secuencial */
        v_Datoserror Varchar2(4000) := Null;

    Begin
        v_Datoserror := 'CARGADISQUETEDEVOLUCIONES: Obtencion del secuencial de la tabla FAC_DISQUETEDEVOLUCIONES';
        Select Nvl(Max(Iddisquetedevoluciones), 0) + 1
            Into v_Iddisquetedevoluciones
        From Fac_Disquetedevoluciones
        Where Idinstitucion = p_Idinstitucion;

        v_Datoserror := 'CARGADISQUETEDEVOLUCIONES: Insercion en la tabla FAC_DISQUETEDEVOLUCIONES del disquete ' || v_Iddisquetedevoluciones;
        INSERT INTO FAC_DISQUETEDEVOLUCIONES (
            IDINSTITUCION,
            IDDISQUETEDEVOLUCIONES,
            BANCOS_CODIGO,
            FECHAGENERACION,
            NOMBREFICHERO,
            FECHAMODIFICACION,
            USUMODIFICACION
        ) VALUES (
            p_Idinstitucion,
            v_Iddisquetedevoluciones,
            p_Codigobanco,
            p_Fechafichero,
            p_Nombrefichero,
            SYSDATE,
            p_Usumodificacion);

        v_Datoserror := 'CARGADISQUETEDEVOLUCIONES: Actualizacion de los parametros de salida';
        p_Iddisquetedevoluciones := v_Iddisquetedevoluciones;
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        Exception
            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    End Cargadisquetedevoluciones;

    /****************************************************************************************************************/
    /* Nombre: CARGALINEASDEVOLUCIONES */
    /* Descripcion: Carga en la tabla de la base de datos FAC_LINEADEVOLUDISQBANCO */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDDISQUETEDEVOLUCIONES - IN - Identificador del disquete devuelto - NUMBER */
    /* P_IDDISQUETECARGOS - IN - Identificador del disquete de cargos - VARCHAR2(10) */
    /* P_IDFACTURAINCLUIDAENDISQUETE - IN - Identificador de la factura incluida en disquete - VARCHAR2(5) */
    /* P_CODIGOREFERENCIA - IN - Codigo de la referencia : IDRECIBO - VARCHAR2(12) */
    /* P_MOTIVODEVOLUCION - IN - Motivo de la devolucion - VARCHAR2(4) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* */
    /* Version: 1.0 - Fecha Creacion: 18/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 17/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 17/01/2014 - Autor: Jorge Paez Trivino 
        - Ahora viene como datos de entrada IDDISQUETECARGOS y IDFACTURAINCLUIDAENDISQUETE */
    /* Version: 4.0 - Fecha Modificacion: 23/06/2015 - Autor: Jorge Paez Trivino 
    - Cuando no encuentra el motivo que devuelva un error */        
  /****************************************************************************************************************/
    Procedure Cargalineasdevoluciones(
        p_Idinstitucion In Number,
        p_Iddisquetedevoluciones In Number,
        p_IdDisqueteCargos In Varchar2,
        p_IdFacturaIncluidaEnDisquete In Varchar2,
        p_CodigoReferencia IN VARCHAR2,
        p_MotivoDevolucion In Varchar2,
        p_Idioma In Number,
        p_Usumodificacion In Number,
        p_Codretorno Out Varchar2,
        p_Datoserror Out Varchar2) Is

        v_Descripcionmotivos Fac_Lineadevoludisqbanco.Descripcionmotivos%Type; /* Motivo de la devolucion concatenado con el concepto */
        v_Codigomotivo Fac_Motivodevolucion.Codigo%Type;
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;
        e_Error Exception; 

    Begin
        v_Datoserror := 'CARGALINEASDEVOLUCIONES: Obtencion del motivo';
        v_Codigomotivo := p_Motivodevolucion;

        If (Trim(v_Codigomotivo) Is Null OR UPPER(v_Codigomotivo) = 'NULL') Then
            Select Valor
                Into v_Codigomotivo
            From Gen_Parametros
            Where Idinstitucion = 0
                And Modulo = 'FAC'
                And Parametro = 'MOTIVO_DEVOLUCION';
        End If;

        BEGIN
            SELECT F_SIGA_GETRECURSO(Nombre, p_Idioma)
                Into v_Descripcionmotivos
            FROM FAC_MOTIVODEVOLUCION
            WHERE CODIGO = v_Codigomotivo;
            
            EXCEPTION 
                WHEN OTHERS THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.motivoIncorrecto', p_Idioma) || ' ' || v_Codigomotivo;
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
        END;            

        v_Datoserror := 'CARGALINEASDEVOLUCIONES: Insercion en la tabla FAC_LINEADEVOLUDISQBANCO del disquete ' || p_Iddisquetedevoluciones;
        INSERT INTO FAC_LINEADEVOLUDISQBANCO (
            IDINSTITUCION,
            IDDISQUETEDEVOLUCIONES,
            IDRECIBO,
            IDDISQUETECARGOS,
            IDFACTURAINCLUIDAENDISQUETE,
            DESCRIPCIONMOTIVOS,
            GASTOSDEVOLUCION,
            CARGARCLIENTE,
            CONTABILIZADA,
            FECHAMODIFICACION,
            USUMODIFICACION
        ) VALUES (
            p_Idinstitucion,
            p_Iddisquetedevoluciones,
            p_Codigoreferencia,
            p_IdDisqueteCargos,
            p_IdFacturaIncluidaEnDisquete,
            v_Descripcionmotivos,
            NULL,
            'N',
            NULL,
            SYSDATE,
            p_Usumodificacion);

        v_Datoserror := 'CARGALINEASDEVOLUCIONES: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        Exception
            When e_Error Then
                    p_Codretorno := v_Codretorno;
                    p_Datoserror := v_Datoserror;        
        
            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    End Cargalineasdevoluciones;

/****************************************************************************************************************/
    /* Nombre: DevolucionesFin */
    /* Descripcion: Tratamiento final de las devoluciones */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - DATE */
    /* P_NOMBREFICHERO - IN - Nombre del fichero - VARCHAR2(80) */    
    /* M_DISQ_DEVO - IN OUT - Matriz de los disquetes de devoluciones - TAB_DISQ_DEVO */
    /* P_CONTADORDISQDEVO - IN OUT - Contador de disquetes de las devoluciones a gestionar - NUMBER */
    /* P_FECHADEVOLUCION - IN - Fecha de la devolucion - DATE */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* */
    /* Version: 1.0 - Fecha Creacion: 23/06/2014 - Autor: Jorge Paez Trivino */
  /****************************************************************************************************************/
    PROCEDURE DevolucionesFin (
        p_Idinstitucion IN NUMBER,
        p_Nombrefichero IN VARCHAR2,
        M_DISQ_DEVO IN OUT TAB_DISQ_DEVO,
        p_ContadorDisqDevo IN OUT NUMBER,
        p_FechaDevolucion IN DATE,
        p_Idioma IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2) IS

        v_Observaciones VARCHAR2(4000);
        v_Codretorno Varchar2(10) := To_Char(0);
        v_Datoserror Varchar2(4000) := Null;
        v_Datoserror_2 Varchar2(4000) := Null;
        v_EstadoFactura FAC_FACTURA.ESTADO%TYPE;
        v_numFicherosSinFacturas NUMBER := 0;
        
        /* Declaracion de excepciones */
        e_Error Exception;

    Begin

        v_Datoserror := 'DevolucionesFin: Comprobar existencia disquetes devoluciones - Tratamiento Devoluciones';
        IF (p_ContadorDisqDevo = 0) THEN
            v_Datoserror := v_Datoserror || ', Secuencia de registros incorrecta';
            v_Codretorno := To_Char(20000);
            RAISE e_Error;
        END IF;

        -- Si ha llegado hasta aqui el fichero tiene una estructura correcta y pasamos a tratar los datos

         v_Datoserror := 'DevolucionesFin: Recorro todos los disquetes de devoluciones - Tratamiento Devoluciones';
        FOR contadorDisq IN 1..p_ContadorDisqDevo LOOP

            -- Controlo que el fichero tenga ficheros incluidos registros individuales que devolver
            v_Datoserror := 'DevolucionesFin: Comprobar existencia ficheros individuales en disquete de devoluciones - Tratamiento Devoluciones';
            IF (M_DISQ_DEVO(contadorDisq).CONT_M_FIED_DEVO=0) THEN
                v_numFicherosSinFacturas := v_numFicherosSinFacturas + 1;
            
            ELSE                           
                -- Insercion tabla FAC_DISQUETEDEVOLUCIONES
                v_Datoserror := 'DevolucionesFin: Llamada al procedimiento CargaDisqueteDevoluciones - Tratamiento Devoluciones';
                Cargadisquetedevoluciones(
                    p_Idinstitucion,
                    M_DISQ_DEVO(contadorDisq).CODIGO_BANCO,
                    p_FechaDevolucion,
                    p_Nombrefichero,
                    p_Usumodificacion,
                    M_DISQ_DEVO(contadorDisq).ID_DISQUETE_DEVO,
                    v_Codretorno,
                    v_Datoserror_2);
                IF (v_Codretorno <> TO_CHAR(0)) THEN
                    RAISE e_Error;
                ELSE
                    v_Datoserror_2 := '';
                END IF;

                v_Datoserror := 'DevolucionesFin: Recorro todos los ficheros incluidos en disquete - Tratamiento Devoluciones';
                FOR contadorFied IN 1..M_DISQ_DEVO(contadorDisq).CONT_M_FIED_DEVO LOOP
                
                    v_Datoserror := 'DevolucionesFin: Se comprueba obtiene el estado actual de la factura';
                    SELECT ESTADO
                        INTO v_EstadoFactura
                    FROM FAC_FACTURA
                    WHERE IDINSTITUCION = p_Idinstitucion
                        AND IDFACTURA = M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).ID_FACTURA; 

                    -- Compruebo que la factura tiene el estado pagada
                    IF (v_EstadoFactura = 1) THEN
                        
                        -- JPT (17-02-2015): Actualizo FAC_FACTURAINCLUIDAENDISQUETE por su PK
                        v_Datoserror := 'DevolucionesFin: Actualizacion tabla FAC_FACTURAINCLUIDAENDISQUETE - Tratamiento Devoluciones';
                        UPDATE FAC_FACTURAINCLUIDAENDISQUETE
                        SET DEVUELTA = 'S',
                            FECHADEVOLUCION = p_FechaDevolucion,
                            FECHAMODIFICACION = SYSDATE,
                            USUMODIFICACION = p_Usumodificacion
                        WHERE IDINSTITUCION = p_Idinstitucion
                            AND IDDISQUETECARGOS = M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IDDISQUETECARGOS
                            AND IDFACTURAINCLUIDAENDISQUETE = M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IDFACTURAINCLUIDAENDISQUETE;
                            
                        v_Datoserror := 'DevolucionesFin: Actualizacion tabla FAC_FACTURA - Tratamiento Devoluciones';
                        UPDATE FAC_FACTURA
                        SET IMPTOTALPAGADOPORBANCO = IMPTOTALPAGADOPORBANCO - M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IMPORTE_DEVO,
                            IMPTOTALPAGADO = IMPTOTALPAGADO - M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IMPORTE_DEVO,
                            IMPTOTALPORPAGAR = IMPTOTALPORPAGAR + M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IMPORTE_DEVO,
                            ESTADO = 4, -- devuelta
                            FECHAMODIFICACION = SYSDATE,
                            USUMODIFICACION = p_Usumodificacion
                        Where IDINSTITUCION = p_Idinstitucion
                            And IDFACTURA = M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).ID_FACTURA;

                        -- Insercion tabla FAC_LINEADEVOLUDISQBANCO
                        v_Datoserror := 'DevolucionesFin: Llamada al procedimiento CargaLineasDevoluciones - Tratamiento Devoluciones';
                        Cargalineasdevoluciones(
                            p_Idinstitucion,
                            M_DISQ_DEVO(contadorDisq).ID_DISQUETE_DEVO,
                            M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IDDISQUETECARGOS,
                            M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).IDFACTURAINCLUIDAENDISQUETE,
                            M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).ID_RECIBO,
                            M_DISQ_DEVO(contadorDisq).M_FIED_DEVO(contadorFied).MOTIVO_DEVO,
                            p_Idioma,
                            p_Usumodificacion,
                            v_Codretorno,
                            v_Datoserror_2);
                        IF (v_Codretorno = TO_CHAR(0) AND v_Datoserror_2 IS NOT NULL) THEN
                            v_Observaciones := v_Datoserror_2;
                        ELSIF (v_Codretorno <> TO_CHAR(0)) THEN
                            RAISE e_Error;
                        ELSE
                            v_Datoserror_2 := '';
                        END IF;
                    END IF;                    
                END LOOP;
            END IF;
        END LOOP;
        
        -- Hay que restar los ficheros sin facturas
        p_ContadorDisqDevo := p_ContadorDisqDevo - v_numFicherosSinFacturas;

        v_Datoserror := 'DevolucionesFin: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);

        IF (v_Observaciones IS NOT NULL) THEN
            p_Datoserror := v_Observaciones;
        ELSE
            p_Datoserror := NULL;
        END IF;

        Exception
            When e_Error Then
                p_Codretorno := v_Codretorno;
                IF (v_Codretorno = TO_CHAR(5420)) THEN
                    p_Datoserror := v_Datoserror_2;
                ELSE
                    p_Datoserror := v_Datoserror || ', ' || v_Datoserror_2;
                END IF;                    

            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || v_Datoserror_2 || ', ' || Sqlerrm; 
      End DevolucionesFin;

    /****************************************************************************************************************/
    /* Nombre: DEVOLUCIONES */
    /* Descripcion: Generacion del fichero para Presentacion de Devoluciones */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - DATE */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_NOMBREFICHERO - IN - Nombre del fichero - VARCHAR2(80) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* P_FECHADEVOLUCION - OUT - Devuelve la fecha de devolucion con formato YYYY/MM/DD HH24:MI:SS - VARCHAR2(19) */
    /* */
    /* Version: 1.0 - Fecha Creacion: 25/10/2004 - Autor: Yolanda Garcia Espino */
    /* Version: 2.0 - Fecha Modificacion: 17/01/2014 - Autor: Jorge Paez Trivino */
    /* Version: 3.0 - Fecha Modificacion: 17/02/2015 - Autor: Jorge Paez Trivino 
        - Elimino devoluciones y rechazos con N19.
        - Quitar una comprobacion que miraba si existia la factura dentro del array (era para cuando habia varios registros individuales con la misma factura)
        - Obtengo y guardo en array IDDISQUETECARGOS, pero no IDFACTURAINCLUIDAENDISQUETE*/
    /* Version: 4.0 - Fecha Modificacion: 10/06/2015 - Autor: Oscar de la Torre Noheda 
        - Se integra el procesado de ficheros SEPA XML de rechazos y devoluciones */    
    /* Version: 5.0 - Fecha Modificacion: 24/06/2015 - Autor: Jorge Paez Trivino 
    - Revision de codigo XML SEPA */        
    /****************************************************************************************************************/
    Procedure Devoluciones (
        p_Idinstitucion In Number,
        p_Pathfichero In Varchar2,
        p_Nombrefichero In Varchar2,
        p_Idioma In Number,
        p_Usumodificacion In Number,
        p_Codretorno Out Varchar2,
        p_Datoserror Out Varchar2,
        p_FechaDevolucion Out VARCHAR2) Is

        v_FechaDevolucion DATE; /* Fecha confeccion del fichero */
        v_Codigoreferencia VARCHAR2(12); /* Codigo de referencia */
        v_Idfactura VARCHAR2(10); /* Identificador de la factura */
        v_FechaEmision FAC_FACTURA.FECHAEMISION%TYPE; /* FECHA DE EMISION DE LA FACTURA */
        v_MotivoDevolucion VARCHAR2(4); /* Motivo de la devolucion */
        v_Importe NUMBER; /* Importe de la factura */
        v_ImporteFIED FAC_FACTURAINCLUIDAENDISQUETE.IMPORTE%TYPE;
        v_Codregistro NUMBER; /* Codigo del Registro */
        v_Coddato NUMBER; /* Codigo del Dato */
        v_Bancos_Codigodev FAC_BANCOINSTITUCION.BANCOS_CODIGO%TYPE;
        f_Entrada  Utl_File.File_Type;
        v_Registro VARCHAR2(1000);
        v_IBAN Fac_Bancoinstitucion.IBAN%Type;
        v_contadorDisqDevo NUMBER := 0;
        v_contadorActualDisqDevo NUMBER;
        v_contadorFiedDevo NUMBER;
        v_tipoDevolucion NUMBER; -- 10:Rechazo; 20:Devolucion
        M_DISQ_DEVO TAB_DISQ_DEVO;
        v_IdFicheroOriginal VARCHAR2(35);
        v_IdDisqueteCargos FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS%TYPE;
        v_facturaDevuelta FAC_FACTURAINCLUIDAENDISQUETE.DEVUELTA%TYPE;
        v_IdFacturaIncluidaEnDisquete FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURAINCLUIDAENDISQUETE%TYPE;
        v_IdDisqueteCargos2 VARCHAR2(35);
        v_Codretorno VARCHAR2(10) := To_Char(0);
        v_Datoserror VARCHAR2(4000) := Null;

        -------------------------------XML-------------------------------------
        v_Tipofichero NUMBER := 0;
        v_documento CLOB;

        v_parse DBMS_XMLPARSER.Parser;
        l_sepa DBMS_XMLDOM.DOMDocument;
        l_CstmrPmtStsRpt DBMS_XMLDOM.DomNode;
        -- variables para fecha creacion
        l_CreDtTm DBMS_XMLDOM.DomNode;
        l_CreDtTm_child DBMS_XMLDOM.DomNode;
        v_CreDtTm VARCHAR2(19);
        -- variables para Identificacion de mensaje
        l_MsgId DBMS_XMLDOM.DomNode;
        l_MsgId_child DBMS_XMLDOM.DomNode;
        v_MsgId  VARCHAR2(35);
        -- variables para IBAN Acreedor
        l_IBAN DBMS_XMLDOM.DomNode;
        l_IBAN_child DBMS_XMLDOM.DomNode;
        -- variables para identificacion de factura
        l_OrgnlEndToEndId DBMS_XMLDOM.DomNode;
        l_OrgnlEndToEndId_child DBMS_XMLDOM.DomNode;
        -- variables para disquete de cargo
        l_OrgnlPmtInfId DBMS_XMLDOM.DomNode;
        l_OrgnlPmtInfId_child DBMS_XMLDOM.DomNode;
        -- variables para motivo devolucion o rechazo
        l_Cd DBMS_XMLDOM.DomNode;
        l_Cd_child DBMS_XMLDOM.DomNode;
        -- variables para importe factura
        l_InstdAmt DBMS_XMLDOM.DomNode;
        l_InstdAmt_child DBMS_XMLDOM.DomNode;

        v_Esquema FAC_FACTURAINCLUIDAENDISQUETE.IMPORTE%TYPE; -- tipo de esquema
        v_RegAcreedor REG_ACREEDOR; -- tipo de fichero
        v_contadorBloqueRemesa NUMBER := 1; -- contador de bloques remesas(OrgnPmtInfId)
        v_contadorBloqueIndividual NUMBER := 1; -- contador bloques individuales deudor facturas(TxInfAndSts)
        -------------------------------XML-------------------------------------

        /* Declaracion de excepciones */
        e_Ficheroincorrecto Exception;
        e_Error Exception;         

    BEGIN        
        -------------------------------XML-------------------------------------
        -- -1=Error; 0=Txt; 1=Txt+Xml; 2=Xml
        v_Datoserror := 'DEVOLUCIONES: Obtiene el parametro FAC.SEPA_TIPO_FICHEROS';
        v_RegAcreedor.TIPOSFICHEROS := F_SIGA_GETPARAMETRO('FAC', 'SEPA_TIPO_FICHEROS', p_Idinstitucion); 
        
        -- Compruebo el tipo de fichero
        IF (v_RegAcreedor.TIPOSFICHEROS = '-1') THEN             
            v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.errorParamSepaTipoFicheros.mensajeCondicionesIncumplidas', p_Idioma);
            v_Codretorno := To_Char(5420);
            Raise e_Error;            

        ELSIF v_RegAcreedor.TIPOSFICHEROS IN (1,2) THEN           
            BEGIN
                v_Datoserror := 'DEVOLUCIONES: Extraer contenido - fichero xml';
                v_documento := DBMS_XSLPROCESSOR.Read2Clob(p_Pathfichero, p_Nombrefichero, 0);            
                -- Se parsea todo el documento
                v_Datoserror := 'DEVOLUCIONES: Parsear datos y pasarlos a una variable tipo documento - fichero xml';
                v_parse:= DBMS_XMLPARSER.newparser();
                DBMS_XMLPARSER.parseclob(v_parse,v_documento);
                l_sepa:= DBMS_XMLPARSER.getdocument(v_parse);
                v_Tipofichero := 1; -- Devolucion fichero xml

                EXCEPTION
                    WHEN OTHERS THEN
                        v_Tipofichero := 0; -- Devolucion fichero texto  
            END;
        END IF;
        
        IF (v_Tipofichero = 0 AND v_RegAcreedor.TIPOSFICHEROS = 2) THEN            
            v_Datoserror :=  F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.ficheroXML', p_Idioma);
            v_Codretorno := To_Char(5420);
            RAISE e_Error; 
        END IF;

        -- Devolucion fichero texto
        v_Datoserror := 'DEVOLUCIONES: tipo fichero txt';
        IF v_Tipofichero = 0 THEN
        -------------------------------XML-------------------------------------

            /* Abrir fichero de entrada */
            v_Datoserror := 'DEVOLUCIONES: Apertura del fichero';
            f_Entrada := Utl_File.Fopen(p_Pathfichero, p_Nombrefichero, 'R'); 

            /******************************  LINEA CABECERA PRESENTADOR ***************************************/
            LOOP -- Empezamos a recorrer el fichero hasta encontrar el registro de cabecera del presentador de la devolucion
                v_Datoserror := 'DEVOLUCIONES: Lectura registro - Cabecera presentador';
                Utl_File.Get_Line(f_Entrada, v_Registro);
                IF (v_Registro IS NULL OR LENGTH(v_Registro) < PKG_SIGA_CONSTANTES.c_LongitudRegistroSEPA) THEN
                    RAISE e_Ficheroincorrecto;
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo registro - Cabecera presentador - SEPA';
                v_Codregistro := TO_NUMBER(SUBSTR(v_Registro, 1, 2));

                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo dato - Cabecera presentador - SEPA';
                v_Coddato := TO_NUMBER(SUBSTR(v_Registro, 8, 3));

                IF (v_Codregistro = 11) THEN
                    v_tipoDevolucion := 10;
                    EXIT WHEN v_Coddato = 1; -- Salida cuando sea un registro de cabecera de presentador

                ELSIF (v_Codregistro = 21) THEN
                    v_tipoDevolucion := 20;
                    EXIT WHEN v_Coddato = 1; -- Salida cuando sea un registro de cabecera de presentador
                END IF;
            END LOOP;

            v_Datoserror := 'DEVOLUCIONES: Obtencion fecha devolucion - Cabecera presentador';
            v_FechaDevolucion := TO_DATE(SUBSTR(v_Registro, 116, 8), 'YYYYMMDD');

            v_Datoserror := 'DEVOLUCIONES: Devuelvo la fecha devolucion';
            p_FechaDevolucion := TO_CHAR(v_FechaDevolucion, 'YYYY/MM/DD HH24:MI:SS');

            /****************************** LINEA CABECERA ORDENANTE ***************************************/
            v_Datoserror := 'DEVOLUCIONES: Lectura registro - Cabecera ordenante';
            Utl_File.Get_Line(f_Entrada, v_Registro);
            IF (v_Registro IS NULL) THEN
                RAISE e_Ficheroincorrecto;
            END IF;

            v_Datoserror := 'DEVOLUCIONES: Obtencion codigo registro - Cabecera ordenante - SEPA';
            v_Codregistro := TO_NUMBER(SUBSTR(v_Registro, 1, 2));

            v_Datoserror := 'DEVOLUCIONES: Comprobar codigo registro - Cabecera ordenante - SEPA';
            IF (v_Codregistro <> v_tipoDevolucion + 2) THEN
                RAISE e_Ficheroincorrecto;
            END IF;

            v_Datoserror := 'DEVOLUCIONES: Obtencion codigo dato - Cabecera ordenante - SEPA';
            v_Coddato := TO_NUMBER(SUBSTR(v_Registro, 8, 3));

            v_Datoserror := 'DEVOLUCIONES: Comprobar codigo dato - Cabecera ordenante - SEPA';
            IF (v_Coddato <> 2) THEN
                RAISE e_Ficheroincorrecto;
            END IF;

            LOOP
                v_IBAN := TRIM(SUBSTR(v_Registro, 266, 34));
                BEGIN
                    v_Datoserror := 'DEVOLUCIONES: Obtencion codigo interno banco - Cabecera ordenante - SEPA';
                    SELECT BANCOS_CODIGO
                        INTO v_Bancos_Codigodev
                    FROM FAC_BANCOINSTITUCION
                    WHERE IDINSTITUCION = p_Idinstitucion
                        AND TRIM(IBAN) = v_IBAN
                        AND FECHABAJA IS NULL
                        AND ROWNUM = 1;

                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            v_Codretorno := To_Char(5406);
                            RAISE e_Error;
                END;
                
                -- JPT (17-02-2015): Obtiene el identificador del disquete de cargos
                v_IdFicheroOriginal := TRIM(SUBSTR(v_Registro, 300, 35));
                v_IdDisqueteCargos2 := TRIM(SUBSTR(v_IdFicheroOriginal, 26, 10)); 

                v_Datoserror := 'DEVOLUCIONES: Compruebo si ya existe un disquete de devoluciones con ese codigo de banco - Cabecera ordenante';
                v_contadorActualDisqDevo := 0; -- Inicializo el contador actual
                FOR contadorDisq IN 1..v_contadorDisqDevo LOOP -- Recorro todos los disquetes de devoluciones

                    -- Compruebo si el disquete de devoluciones actual tiene el mismo codigo de banco
                    IF (M_DISQ_DEVO(contadorDisq).CODIGO_BANCO = v_Bancos_Codigodev) THEN
                        v_contadorActualDisqDevo := contadorDisq; -- Guardo el disquete de devoluciones encontrado
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;

                -- Si v_contadorActualDisqDevo es cero, es que no lo ha encontrado
                IF (v_contadorActualDisqDevo=0) THEN
                    v_contadorDisqDevo := v_contadorDisqDevo + 1;
                    v_contadorActualDisqDevo := v_contadorDisqDevo;

                    -- Guardo los datos en la matriz
                    v_Datoserror := 'DEVOLUCIONES: Guardo datos en matriz de disquetes de devoluciones - Cabecera ordenante';
                    M_DISQ_DEVO(v_contadorActualDisqDevo).CODIGO_BANCO := v_Bancos_Codigodev; -- Guarda el codigo del banco
                    M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO := 0; -- Inicialmente no tiene ningun fichero incluido en disquete
                END IF;

                /********************************* LINEA INDIVIDUAL *********************************************/
                v_Datoserror := 'DEVOLUCIONES: Lectura registro - Registro individual';
                Utl_File.Get_Line(f_Entrada, v_Registro);
                IF (v_Registro IS NULL) THEN
                    RAISE e_Ficheroincorrecto;
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo registro - Registro individual - SEPA';
                v_Codregistro := TO_NUMBER(SUBSTR(v_Registro, 1, 2));

                v_Datoserror := 'DEVOLUCIONES: Comprobar codigo registro - Registro individual - SEPA';
                IF (v_Codregistro <> v_tipoDevolucion + 3) THEN
                    RAISE e_Ficheroincorrecto;
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo dato - Registro individual - SEPA';
                v_Coddato := TO_NUMBER(SUBSTR(v_Registro, 8, 3));

                 v_Datoserror := 'DEVOLUCIONES: Comprobar codigo dato - Registro individual - SEPA';
                IF (v_Coddato <> 3) THEN
                    RAISE e_Ficheroincorrecto;
                END IF;

                LOOP
                    v_Datoserror := 'DEVOLUCIONES: Obtencion codigo referencia - Registro individual - SEPA';
                    v_Codigoreferencia := TRIM(SUBSTR(v_Registro, 11, 10));

                    v_Datoserror := 'DEVOLUCIONES: Obtencion identificador factura - Registro individual - SEPA';
                    v_Idfactura := LTRIM(SUBSTR(v_Codigoreferencia, 1, 8), 0);

                    v_Datoserror := 'DEVOLUCIONES: Obtencion motivo devolucion - Registro individual - SEPA';
                    v_Motivodevolucion := TRIM(SUBSTR(v_Registro, 582, 4));

                    v_Datoserror := 'DEVOLUCIONES: Obtencion importe - Registro individual - SEPA';
                    v_Importe := ROUND(TO_NUMBER(SUBSTR(v_Registro, 89, 11)) / 100, 2);

                    v_Datoserror := 'DEVOLUCIONES: Obtencion estado y fecha emision factura - Registro individual';
                    BEGIN 
                        SELECT FECHAEMISION
                            INTO v_FechaEmision
                        FROM FAC_FACTURA
                        WHERE IDINSTITUCION = p_Idinstitucion
                            AND IDFACTURA = v_Idfactura;
                            
                        EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                                v_Codretorno := To_Char(5407); -- Error, no encuentra la factura
                                RAISE e_Error; 
                    END;

                    v_Datoserror := 'DEVOLUCIONES: Comprobar fecha devolucion posterior a fecha emision - Registro individual';
                    IF (TRUNC(v_FechaDevolucion) < TRUNC(v_FechaEmision)) THEN
                        v_Codretorno := To_Char(5404);
                        RAISE e_Error;
                    END IF;
                    
                    -- JPT (17-02-2015): Obtengo IMPORTE, IDDISQUETECARGOS y IDFACTURAINCLUIDAENDISQUETE
                    BEGIN
                        v_Datoserror := 'DEVOLUCIONES: Obtengo IDFACTURAINCLUIDAENDISQUETE';
                        SELECT IMPORTE, 
                                IDDISQUETECARGOS, 
                                IDFACTURAINCLUIDAENDISQUETE, 
                                DEVUELTA
                            INTO v_ImporteFIED, 
                                v_IdDisqueteCargos, 
                                v_IdFacturaIncluidaEnDisquete,
                                v_facturaDevuelta
                        FROM FAC_FACTURAINCLUIDAENDISQUETE
                        WHERE IDINSTITUCION = p_Idinstitucion
                            AND IDDISQUETECARGOS = TO_NUMBER(v_IdDisqueteCargos2)
                            AND IDFACTURA = v_Idfactura
                            AND IDRECIBO = v_Codigoreferencia
                            AND ROWNUM = 1;

                        EXCEPTION
                            WHEN OTHERS THEN        
                                BEGIN  
                                    -- JPT (17-02-2015): Si no viene IDDISQUETECARGOS, intento buscarlo con IDFACTURA e IDRECIBO                   
                                    SELECT IMPORTE, 
                                            IDDISQUETECARGOS, 
                                            IDFACTURAINCLUIDAENDISQUETE,
                                            DEVUELTA
                                        INTO v_ImporteFIED, 
                                            v_IdDisqueteCargos, 
                                            v_IdFacturaIncluidaEnDisquete,
                                            v_facturaDevuelta
                                    FROM FAC_FACTURAINCLUIDAENDISQUETE
                                    WHERE IDINSTITUCION = p_Idinstitucion
                                        AND IDFACTURA = v_Idfactura
                                        AND IDRECIBO = v_Codigoreferencia                                    
                                        AND ROWNUM = 1; 
                                        
                                    EXCEPTION
                                        WHEN NO_DATA_FOUND THEN
                                            v_Codretorno := To_Char(5407); -- Error, no encuentra la factura
                                            RAISE e_Error; 
                                END;
                    END; 
                    
                    IF (v_facturaDevuelta = 'N') THEN
                        v_Datoserror := 'DEVOLUCIONES: Guardo datos en matriz de disquetes de devoluciones - Registro individual';
                        v_contadorFiedDevo := M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO + 1;
                        M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).ID_RECIBO := v_Codigoreferencia;
                        M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).ID_FACTURA := v_Idfactura;
                        M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IMPORTE_DEVO := v_Importe;
                        M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).MOTIVO_DEVO := v_Motivodevolucion;
                        M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IDFACTURAINCLUIDAENDISQUETE := v_IdFacturaIncluidaEnDisquete;
                        
                          v_Datoserror := 'DEVOLUCIONES: Comprobar importe FAC_FACTURAINCLUIDAENDISQUETE - Tratamiento Devoluciones';
                        IF (v_Importe <> v_ImporteFIED) THEN
                            v_Codretorno := To_Char(5405);
                            RAISE e_Error;
                        END IF;
                        
                        -- JPT (17-02-2015): Guarda el identificador del disquete de cargos
                        M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IDDISQUETECARGOS := v_IdDisqueteCargos; 
                        M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO := v_contadorFiedDevo; -- Tiene un fichero incluidos en disquete nuevo
                    END IF;                

                    /****************** LINEAS INDIVIDUALES O TOTALES ORDENANTE ****************************************/
                    LOOP
                        v_Datoserror := 'DEVOLUCIONES: Lectura registro - Registro individual o total ordenante';
                        Utl_File.Get_Line(f_Entrada, v_Registro);
                        IF (v_Registro IS NULL) THEN
                            RAISE e_Ficheroincorrecto;
                        END IF;

                        v_Datoserror := 'DEVOLUCIONES: Obtencion codigo referencia - Registro individual o total ordenante - SEPA';
                        v_Codregistro := TO_NUMBER(SUBSTR(v_Registro, 1, 2));

                        EXIT WHEN (v_Codregistro = v_tipoDevolucion + 4 OR v_Codregistro = v_tipoDevolucion + 5 OR v_Codregistro = 99); -- Salida cuando sea un registro de totales

                        v_Datoserror := 'DEVOLUCIONES: Obtencion codigo dato - Registro individual o total ordenante - SEPA';
                        v_Coddato := TO_NUMBER(SUBSTR(v_Registro, 8, 3));

                        EXIT WHEN (v_Codregistro = v_tipoDevolucion + 3 AND v_Coddato = 3); -- Salida cuando sea un registro individual obligatorio
                    END LOOP;

                    EXIT WHEN (v_Codregistro = v_tipoDevolucion + 4 OR v_Codregistro = v_tipoDevolucion + 5 OR v_Codregistro = 99); -- Salida cuando sea un registro de totales

                    v_Datoserror := 'DEVOLUCIONES: Comprobar codigo registro y codigo dato - Registro individual o total ordenante - SEPA';
                    IF NOT (v_Codregistro = v_tipoDevolucion + 3 AND v_Coddato = 3) THEN -- Error si no es un registro individual obligatorio
                        RAISE e_Ficheroincorrecto;
                    END IF;                    
                END LOOP;

                /******************** LINEAS TOTALES O CABECERA ORDENANTE *******************************/
                v_Datoserror := 'DEVOLUCIONES: Comprobar codigo registro - Total ordenante fecha - SEPA';
                IF NOT (v_Codregistro = v_tipoDevolucion + 4) THEN -- Error si no es un registro de totales del ordenante por fecha
                    RAISE e_Ficheroincorrecto;
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Lectura registro - Cabecera ordenante o total ordenante';
                Utl_File.Get_Line(f_Entrada, v_Registro);
                IF (v_Registro IS NULL) THEN
                    RAISE e_Ficheroincorrecto;
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo referencia - Cabecera ordenante o total ordenante - SEPA';
                v_Codregistro := TO_NUMBER(SUBSTR(v_Registro, 1, 2));

                IF (v_Codregistro = v_tipoDevolucion + 5) THEN -- Si la linea es un registro de totales del ordenante SEPA, leemos la siguiente linea
                    v_Datoserror := 'DEVOLUCIONES: Lectura registro - Total ordenante - SEPA';
                    Utl_File.Get_Line(f_Entrada, v_Registro);
                    IF (v_Registro IS NULL) THEN
                        RAISE e_Ficheroincorrecto;
                    END IF;

                    v_Datoserror := 'DEVOLUCIONES: Obtencion codigo referencia - Total ordenante - SEPA';
                    v_Codregistro := TO_NUMBER(SUBSTR(v_Registro, 1, 2));

                    EXIT WHEN (v_Codregistro = 99); -- Salida cuando sea un registro de total general
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Comprobar codigo registro - Cabecera ordenante o total ordenante - SEPA';
                IF (v_Codregistro <> v_tipoDevolucion + 2) THEN -- Error si no es un registro de cabecera del ordenante
                    RAISE e_Ficheroincorrecto;
                END IF;

                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo dato - Total ordenante - SEPA';
                v_Coddato := TO_NUMBER(SUBSTR(v_Registro, 8, 3));

                IF (v_Coddato <> 2) THEN -- Error si no es un registro de cabecera del ordenante
                    RAISE e_Ficheroincorrecto;
                END IF;
            END LOOP;

            v_Datoserror := 'DEVOLUCIONES: Cierre fichero';
            Utl_File.Fclose(f_Entrada);
        
        -------------------------------XML-------------------------------------
        -- Devolucion xml    
        ELSIF v_Tipofichero = 1 THEN
        
            /* SE HA ELIMINADO EL TRATAMIENTO QUE ELIMINABA LOS ATRIBUTOS xmlns de la etiqueta Document => Ahora se hacen desde java
            * Se han detectado dos problemas en pl que podemos evitar desde java:
             * - Cuando viene todo el xml en una linea muy grande (37.XXX), no funciona bien el pl, aunque trabaja con funciones CLOB, por debajo debe trabajar con VARCHAR2.
             * - Cuando crea un fichero desde java se crea con usuario root, y cuando se sobreescribe desde pl se utiliza un usuario de oracle, con lo que en pl no es el propietario del fichero.
             * - Al crear un fichero el propietario tiene permisos de lectura y escritura, pero para el resto tiene permisos de solo lectura. 
             */

            -- Comprobamos si es un fichero del esquema pain.002.001.03 correspondiente a devoluciones y rechazos
            v_Datoserror := 'DEVOLUCIONES: Comprobar si es un esquema basico pain.002.001.03 - fichero xml';
            l_CstmrPmtStsRpt := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'CstmrPmtStsRpt'), 0);

            IF DBMS_XMLDOM.isNull(l_CstmrPmtStsRpt) THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "CstmrPmtStsRpt" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            /************************** CABECERA *****************************/

            -- Busco y extraigo fecha devolucion
            v_Datoserror := 'DEVOLUCIONES: Buscar y extraer fecha devolucion - Cabecera - fichero xml';
            l_CreDtTm := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'CreDtTm'), 0);

            IF DBMS_XMLDOM.isNull(l_CreDtTm) THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "CreDtTm" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/GrpHdr/CreDtTm"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            l_CreDtTm_child := DBMS_XMLDOM.getFirstChild(l_CreDtTm);
            v_CreDtTm := DBMS_XMLDOM.getnodevalue(l_CreDtTm_child);

            IF v_CreDtTm IS NULL THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "CreDtTm" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/GrpHdr/CreDtTm"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            BEGIN
                v_Datoserror := 'DEVOLUCIONES: Obtencion fecha devolucion - Cabecera - fichero xml';
                v_FechaDevolucion := TO_DATE(SUBSTR(v_CreDtTm, 1, 10), 'YYYY-MM-DD');
                
                v_Datoserror := 'DEVOLUCIONES: Devuelvo la fecha devolucion';
                p_FechaDevolucion := TO_CHAR(v_FechaDevolucion, 'YYYY/MM/DD HH24:MI:SS');
                
                EXCEPTION
                    WHEN OTHERS THEN
                        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "CreDtTm" (YYYY-MM-DDThh:mm:ss) - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/GrpHdr/CreDtTm"';
                        v_Codretorno := To_Char(5420);
                        RAISE e_Error;
            END; 

            -- Buscar y extraer tipo devolucion
            v_Datoserror := 'DEVOLUCIONES: Buscar y extraer tipo de devolucion - Cabecera - fichero xml';
            l_MsgId := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'MsgId'), 0);

            IF DBMS_XMLDOM.isNull(l_MsgId) THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "MsgId" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/GrpHdr/MsgId"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            l_MsgId_child := DBMS_XMLDOM.getFirstChild(l_MsgId);
            v_MsgId := DBMS_XMLDOM.getnodevalue(l_MsgId_child);

            IF v_MsgId IS NULL THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "MsgId" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/GrpHdr/MsgId"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;

            ELSIF LENGTH(v_MsgId) NOT BETWEEN 1 AND 35 THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "MsgId" (35 ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.caracteresMaximo', p_Idioma) || ') - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/GrpHdr/MsgId"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            v_Datoserror := 'DEVOLUCIONES: Comprobar tipo de devolucion - Cabecera - fichero xml';
            IF UPPER(SUBSTR(v_MsgId, 1, 2)) LIKE 'DA' THEN
                v_tipoDevolucion := 10;
            ELSE
                v_tipoDevolucion := 20;
            END IF;

            /*********************** CABECERA ORDENANTE **************************/

            -- Busco y extraigo IBAN acreedor
            v_Datoserror := 'DEVOLUCIONES: Buscar IBAN acreedor - Cabecera ordenante - fichero xml';
            l_IBAN := DBMS_XSLPROCESSOR.selectSingleNode(DBMS_XMLDOM.makeNode(l_sepa), '/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/CdtrAcct/Id/IBAN');

            IF DBMS_XMLDOM.isNull(l_IBAN) THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "IBAN" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/CdtrAcct/Id/IBAN"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            l_IBAN_child := DBMS_XMLDOM.getFirstChild(l_IBAN);
            v_IBAN := DBMS_XMLDOM.getnodevalue(l_IBAN_child);

            IF v_IBAN IS NULL THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "IBAN" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/CdtrAcct/Id/IBAN"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;

            ELSIF LENGTH(v_IBAN) NOT BETWEEN 1 AND 34 THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "IBAN" (34 ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.caracteresMaximo', p_Idioma) || ') - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/CdtrAcct/Id/IBAN"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            BEGIN
                v_Datoserror := 'DEVOLUCIONES: Obtencion codigo interno banco - Cabecera ordenante - fichero xml';
                SELECT BANCOS_CODIGO
                    INTO v_Bancos_Codigodev
                FROM FAC_BANCOINSTITUCION
                WHERE IDINSTITUCION = p_Idinstitucion
                    AND TRIM(IBAN) = v_IBAN
                    AND FECHABAJA IS NULL
                    AND ROWNUM = 1;

                EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                        v_Codretorno := To_Char(5406);
                        RAISE e_Error;
            END;

            -- Busco y extraigo disquete de cargo
            v_Datoserror := 'DEVOLUCIONES: Busco y extraigo disquete de cargo - fichero xml';
            l_OrgnlPmtInfId := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'OrgnlPmtInfId'), 0); -- selecciono el primer nodo de la lista

            IF DBMS_XMLDOM.isNull(l_OrgnlPmtInfId) THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "OrgnlPmtInfId" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/OrgnlPmtInfId"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            l_OrgnlPmtInfId_child := DBMS_XMLDOM.getFirstChild(l_OrgnlPmtInfId); -- recojo los datos del nodo elegido
            v_IdFicheroOriginal := TRIM(DBMS_XMLDOM.getnodevalue(l_OrgnlPmtInfId_child));

            IF v_IdFicheroOriginal IS NULL THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "OrgnlPmtInfId" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/OrgnlPmtInfId"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;

            ELSIF LENGTH(v_IdFicheroOriginal) NOT BETWEEN 1 AND 35 THEN
                v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "OrgnlPmtInfId" (35 ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.caracteresMaximo', p_Idioma) || ') - ' ||  
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts/OrgnlPmtInfId"';
                v_Codretorno := To_Char(5420);
                RAISE e_Error;
            END IF;

            v_IdDisqueteCargos2 := SUBSTR(v_IdFicheroOriginal, 26, 10);
            v_contadorActualDisqDevo := 1;-- Inicializo el contador actual
            v_contadorDisqDevo := 1;

            -- Guardo los datos en la matriz
            v_Datoserror := 'DEVOLUCIONES: Guardo datos en matriz de disquetes de devoluciones - fichero xml';
            M_DISQ_DEVO(v_contadorActualDisqDevo).CODIGO_BANCO := v_Bancos_Codigodev; -- Guarda el codigo del banco
            M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO := 0; -- Inicialmente no tiene ningun fichero incluido en disquete

            /********************** BLOQUE REMESA (OrgnlPmtInfAndSts) **************************/
            LOOP           
                -- Busco y extraigo referencia factura
                v_Datoserror := 'DEVOLUCIONES: Buscar y extraer Referencia factura - fichero xml';
                l_OrgnlEndToEndId := DBMS_XSLPROCESSOR.selectSingleNode(DBMS_XMLDOM.makeNode(l_sepa), '/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlEndToEndId');

                l_OrgnlEndToEndId_child := DBMS_XMLDOM.getFirstChild(l_OrgnlEndToEndId); -- recojo los datos del nodo elegido
                v_Codigoreferencia := DBMS_XMLDOM.getnodevalue(l_OrgnlEndToEndId_child);

                IF DBMS_XMLDOM.isNull(l_OrgnlEndToEndId) AND v_contadorBloqueIndividual = 1 THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "OrgnlEndToEndId" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlEndToEndId"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;

                ELSIF DBMS_XMLDOM.isNull(l_OrgnlEndToEndId) AND v_contadorBloqueIndividual > 1 THEN -- Buscamos siguiente bloque Remesa (OrgnlPmtInfAndSts)
                    v_contadorBloqueRemesa := v_contadorBloqueRemesa + 1;
                    v_contadorBloqueIndividual := 1; -- Iniciamos contador de facturas (TxInfAndSts)
                    l_OrgnlEndToEndId := DBMS_XSLPROCESSOR.selectSingleNode(DBMS_XMLDOM.makeNode(l_sepa), '/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlEndToEndId');

                    l_OrgnlEndToEndId_child := DBMS_XMLDOM.getFirstChild(l_OrgnlEndToEndId); -- recojo los datos del nodo elegido
                    v_Codigoreferencia := DBMS_XMLDOM.getnodevalue(l_OrgnlEndToEndId_child);

                    IF DBMS_XMLDOM.isNull(l_OrgnlEndToEndId) THEN
                        EXIT; -- Cuando no se encuentran mas facturas
                    END IF;
                END IF;

                IF v_Codigoreferencia IS NULL THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "OrgnlEndToEndId" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlEndToEndId"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
                    
                ELSIF LENGTH(v_Codigoreferencia) <> 10 THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "OrgnlEndToEndId" (10 ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.digitos', p_Idioma) || ') - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlEndToEndId"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
                END IF;

                v_Idfactura := LTRIM(SUBSTR(v_Codigoreferencia, 1, 8), 0);

                -- Buscar y extraer motivo devolucion
                v_Datoserror := 'DEVOLUCIONES: Buscar y extraer motivo devolucion - fichero xml';
                l_Cd := DBMS_XSLPROCESSOR.selectSingleNode(DBMS_XMLDOM.makeNode(l_sepa), '/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/StsRsnInf/Rsn/Cd');

                IF DBMS_XMLDOM.isNull(l_Cd) THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "Cd" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/StsRsnInf/Rsn/Cd"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
                END IF;

                l_Cd_child := DBMS_XMLDOM.getFirstChild(l_Cd);
                v_Motivodevolucion := DBMS_XMLDOM.getnodevalue(l_Cd_child);

                IF v_Motivodevolucion IS NULL THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "Cd" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/StsRsnInf/Rsn/Cd"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;

                ELSIF LENGTH(v_Motivodevolucion) NOT BETWEEN 1 AND 4 THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "Cd" (4 ' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.caracteresMaximo', p_Idioma) || ') - ' ||  
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/StsRsnInf/Rsn/Cd"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
                END IF;

                -- Buscar y extraer importe factura
                v_Datoserror := 'DEVOLUCIONES: Buscar y extraer importe factura - fichero xml';
                l_InstdAmt := DBMS_XSLPROCESSOR.selectSingleNode(DBMS_XMLDOM.makeNode(l_sepa), '/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/Amt/InstdAmt');

                IF DBMS_XMLDOM.isNull(l_InstdAmt) THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinNodo', p_Idioma) || ' "InstdAmt" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/Amt/InstdAmt"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
                END IF;

                l_InstdAmt_child := DBMS_XMLDOM.getFirstChild(l_InstdAmt);
                v_Importe := TO_NUMBER(DBMS_XMLDOM.getnodevalue(l_InstdAmt_child),'9999999999999999.99');

                IF v_Importe IS NULL THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.sinDatos', p_Idioma) || ' "InstdAmt" - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/Amt/InstdAmt"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;

                ELSIF v_Importe NOT BETWEEN 0.01 AND 99999999.99 THEN
                    v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.formatoIncorrecto', p_Idioma) || ' "InstdAmt" (12345678.12) - ' || 
                                        F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlTxRef/Amt/InstdAmt"';
                    v_Codretorno := To_Char(5420);
                    RAISE e_Error;
                END IF;

                BEGIN
                    SELECT FECHAEMISION
                        INTO v_FechaEmision
                    FROM FAC_FACTURA
                    WHERE IDINSTITUCION = p_Idinstitucion
                        AND IDFACTURA = v_Idfactura;

                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            v_Codretorno := To_Char(5407); -- Error, no encuentra la factura
                            RAISE e_Error;
                END;

                v_Datoserror := 'DEVOLUCIONES: Comprobar fecha devolucion posterior a fecha emision - bloque remesa - fichero xml';
                IF (TRUNC(v_FechaDevolucion) < TRUNC(v_FechaEmision)) THEN
                    v_Codretorno := To_Char(5404);
                    RAISE e_Error;
                END IF;

                -- JPT (17-02-2015): Obtengo IMPORTE, IDDISQUETECARGOS y IDFACTURAINCLUIDAENDISQUETE
                BEGIN
                    v_Datoserror := 'DEVOLUCIONES: Obtengo IDFACTURAINCLUIDAENDISQUETE - bloque remesa - fichero xml';
                    SELECT IMPORTE, 
                            IDDISQUETECARGOS, 
                            IDFACTURAINCLUIDAENDISQUETE, 
                            ESQUEMA,
                            DEVUELTA
                        INTO v_ImporteFIED, 
                            v_IdDisqueteCargos, 
                            v_IdFacturaIncluidaEnDisquete, 
                            v_Esquema,
                            v_facturaDevuelta
                    FROM FAC_FACTURAINCLUIDAENDISQUETE
                    WHERE IDINSTITUCION = p_Idinstitucion
                        AND IDDISQUETECARGOS = TO_NUMBER(v_IdDisqueteCargos2)
                        AND IDFACTURA = v_Idfactura
                        AND IDRECIBO = v_Codigoreferencia
                        AND ROWNUM = 1;                     

                    EXCEPTION
                        WHEN OTHERS THEN
                            BEGIN
                                -- JPT (17-02-2015): Si no viene IDDISQUETECARGOS, intento buscarlo con IDFACTURA e IDRECIBO
                                SELECT IMPORTE, 
                                        IDDISQUETECARGOS, 
                                        IDFACTURAINCLUIDAENDISQUETE, 
                                        ESQUEMA,
                                        DEVUELTA
                                    INTO v_ImporteFIED, 
                                        v_IdDisqueteCargos, 
                                        v_IdFacturaIncluidaEnDisquete, 
                                        v_Esquema,
                                        v_facturaDevuelta
                                FROM FAC_FACTURAINCLUIDAENDISQUETE
                                WHERE IDINSTITUCION = p_Idinstitucion
                                    AND IDFACTURA = v_Idfactura
                                    AND IDRECIBO = v_Codigoreferencia
                                    AND ROWNUM = 1;

                                EXCEPTION
                                    WHEN NO_DATA_FOUND THEN
                                        v_Codretorno := To_Char(5407); -- Error, no encuentra la factura
                                        RAISE e_Error;
                            END;
                END;
                
                IF (v_facturaDevuelta = 'N') THEN                
                    v_Datoserror := 'DEVOLUCIONES: Guardo datos en matriz de disquetes de devoluciones - bloque remesa - fichero xml';
                    v_contadorFiedDevo := M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO + 1;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).ID_RECIBO := v_Codigoreferencia;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).ID_FACTURA := v_Idfactura;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IMPORTE_DEVO := v_Importe;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).MOTIVO_DEVO := v_Motivodevolucion;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IDFACTURAINCLUIDAENDISQUETE := v_IdFacturaIncluidaEnDisquete;

                    v_Datoserror := 'DEVOLUCIONES: Comprobar si hay una devolucion B2B';
                    IF v_tipoDevolucion = 10 AND v_Esquema = 2  THEN -- 2 esquema B2B
                        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.B2B', p_Idioma) || ' - ' || 
                                            F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioPagos.error.devoluciones.localizacion', p_Idioma) || ': "/Document/CstmrPmtStsRpt/OrgnlPmtInfAndSts['||v_contadorBloqueRemesa||']/TxInfAndSts['||v_contadorBloqueIndividual||']/OrgnlEndToEndId"';
                        v_Codretorno := To_Char(5420);
                        RAISE e_Error;
                    END IF;

                    v_Datoserror := 'DEVOLUCIONES: Comprobar importe FAC_FACTURAINCLUIDAENDISQUETE - bloque remesa - fichero xml';
                    IF (v_Importe <> v_ImporteFIED) THEN
                        v_Codretorno := To_Char(5405);
                        RAISE e_Error;
                    END IF;

                    -- JPT (17-02-2015): Guarda el identificador del disquete de cargos
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IDDISQUETECARGOS := v_IdDisqueteCargos;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO := v_contadorFiedDevo; -- Tiene un fichero incluidos en disquete nuevo
                END IF;
                v_contadorBloqueIndividual := v_contadorBloqueIndividual + 1;
            END LOOP; 
        END IF;
        -------------------------------XML-------------------------------------
                                                   
        /*********************************************************************/
        /******************** FIN LECTURA FICHERO *******************************/
        /********************************************************************/

        v_Datoserror := 'DEVOLUCIONES: Llamada al procedimiento DevolucionesFin - Tratamiento Devoluciones ';
        DevolucionesFin (
            p_Idinstitucion,
            p_Nombrefichero,
            M_DISQ_DEVO,
            v_contadorDisqDevo,
            v_FechaDevolucion,
            p_Idioma,
            p_Usumodificacion,
            v_Codretorno,
            v_Datoserror);
        IF (v_Codretorno <> TO_CHAR(0)) THEN
            RAISE e_Error;
        END IF;

        v_Datoserror := 'DEVOLUCIONES: Actualizacion de los parametros de salida ';
        p_Codretorno := To_Char(0);
        p_Datoserror := v_contadorDisqDevo;

        Exception
            When e_Ficheroincorrecto Then
                Begin
                    Rollback;
                    Utl_File.Fclose(f_Entrada); -- Cerrar fichero de entrada
                    p_Codretorno := To_Char(20000);
                    p_Datoserror := v_Datoserror || ', Contenido del fichero incorrecto ';

                    Exception
                        When Utl_File.Read_Error Then
                            p_Codretorno := To_Char(5400);
                            p_Datoserror := v_Datoserror || ', Error del sistema operativo en una lectura de fichero';

                        When Utl_File.Invalid_Filehandle Then
                            p_Codretorno := To_Char(5396);
                            p_Datoserror := v_Datoserror || ', El descriptor no es un archivo abierto';
                End;

            When e_Error Then
                Begin
                    Rollback;
                    Utl_File.Fclose(f_Entrada); -- Cerrar fichero de entrada
                    p_Codretorno := v_Codretorno;
                    p_Datoserror := v_Datoserror;

                    Exception
                        When Utl_File.Read_Error Then
                            p_Codretorno := To_Char(5400);
                            p_Datoserror := v_Datoserror || ', Error del sistema operativo en una lectura de fichero';

                        When Utl_File.Invalid_Filehandle Then
                            p_Codretorno := To_Char(5396);
                            p_Datoserror := v_Datoserror || ', El descriptor no es un archivo abierto';
                End;

            When Utl_File.Invalid_Path Then
                Rollback;
                p_Codretorno := To_Char(5394);
                p_Datoserror := v_Datoserror || ', La ruta del fichero no es correcta';

            When Utl_File.Invalid_Mode Then
                Rollback;
                p_Codretorno := To_Char(5395);
                p_Datoserror := v_Datoserror || ', La cadena no es valida para el modo de archivo';

            When Utl_File.Invalid_Operation Then
                Rollback;
                p_Codretorno := To_Char(5397);
                p_Datoserror := v_Datoserror || ', Operacion erronea en el fichero ' || Sqlerrm;

            When Utl_File.Read_Error Then
                Rollback;
                p_Codretorno := To_Char(5400);
                p_Datoserror := v_Datoserror || ', Error del sistema operativo en una lectura de fichero';

            When Utl_File.Invalid_Filehandle Then
                Rollback;
                p_Codretorno := To_Char(5396);
                p_Datoserror := v_Datoserror || ', El descriptor no es un archivo abierto';

            When Utl_File.Write_Error Then
                Rollback;
                p_Codretorno := To_Char(5398);
                p_Datoserror := v_Datoserror || ', Error del sistema operativo en una escritura de fichero';

            When No_Data_Found Then
                Rollback;
                p_Codretorno := To_Char(5399);
                p_Datoserror := v_Datoserror || ', Final de archivo en una lectura de fichero';

            When Utl_File.Internal_Error Then
                Rollback;
                p_Codretorno := To_Char(5401);
                p_Datoserror := v_Datoserror || ', Error interno no especificado ' || Sqlerrm;

            When Value_Error Then
                Rollback;
                p_Codretorno := To_Char(5402);
                p_Datoserror := v_Datoserror || ', Linea de entrada demasiado larga';

            When Others Then
                Begin
                    Rollback;
                    v_Datoserror := v_Datoserror || ', ' || Sqlerrm;
                    Utl_File.Fclose(f_Entrada); -- Cerrar fichero de entrada
                    p_Codretorno := To_Char(Sqlcode);
                    p_Datoserror := v_Datoserror; 

                    Exception
                        When Utl_File.Read_Error Then
                            p_Codretorno := To_Char(5400);
                            p_Datoserror := v_Datoserror || ', Error del sistema operativo en una lectura de fichero';

                        When Utl_File.Invalid_Filehandle Then
                            p_Codretorno := To_Char(5396);
                            p_Datoserror := v_Datoserror || ', El descriptor no es un archivo abierto';
                End;
      End Devoluciones;

    /****************************************************************************************************************/
    /* Nombre: DevolucionesManuales */
    /* Descripcion: Generacion de devoluciones manuales */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - DATE */
    /* P_LISTAFACTURAS - IN - Lista de facturas devueltas manualmente (idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...) - CLOB */
    /* P_FECHADEVOLUCION - IN - Fecha de devolucion manual (YYYYMMDD) - VARCHAR2(8) */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*    En caso de error devuelve el codigo de error Oracle correspondiente */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*    En caso de error devuelve el mensaje de error Oracle correspondiente */
    /* P_LISTAIDDISQUETESDEVOLUCION - OUT - Lista de identificadores de los disquetes de devoluciones generados - VARCHAR2(4000) */
    /* */
    /* Version: 1.0 - Fecha Modificacion: 23/06/2014 - Autor: Jorge Paez Trivino */
    /* Version: 2.0 - Fecha Modificacion: 17/02/2015 - Autor: Jorge Paez Trivino
        - Guardo en array IDDISQUETECARGOS y IDFACTURAINCLUIDAENDISQUETE */
  /****************************************************************************************************************/
    Procedure DevolucionesManuales (
        p_Idinstitucion IN NUMBER,
        p_ListaFacturas IN CLOB,
        p_FechaDevolucion IN VARCHAR2,
        p_Idioma IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2,
        p_ListaIdDisquetesDevolucion OUT VARCHAR2) Is
        
        v_Datoserror Varchar2(4000) := Null;
        v_Codretorno Varchar2(10) := To_Char(0);
        v_ArrayFacturas PKG_SIGA_UTIL.t_array;
        v_ArrayFactura PKG_SIGA_UTIL.t_array;
        v_cursorFactura Binary_Integer;
        v_Factura VARCHAR2(4000);
        v_IdDisqueteCargos FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS%Type;
        v_IdFIED FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURAINCLUIDAENDISQUETE%Type;
        v_MotivoDevolucion FAC_MOTIVODEVOLUCION.CODIGO%TYPE;
        v_IdFactura FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURA%Type;
        v_IdRecibo FAC_FACTURAINCLUIDAENDISQUETE.IDRECIBO%Type;
        v_Importe FAC_FACTURAINCLUIDAENDISQUETE.IMPORTE%Type;
        v_FechaEmision FAC_FACTURA.FECHAEMISION%Type;
        v_CodigoBanco FAC_BANCOINSTITUCION.BANCOS_CODIGO%Type;
        v_contadorActualDisqDevo NUMBER;
        v_contadorDisqDevo NUMBER := 0;
        M_DISQ_DEVO TAB_DISQ_DEVO;
        v_contadorFiedDevo NUMBER;
        v_FechaDevolucion DATE;
        
        /* Declaracion de excepciones */
        e_Error Exception;

    BEGIN

        -- Tiene que tener facturas y la fecha en formato adecuado
        IF (LENGTH(p_ListaFacturas) > 0 AND LENGTH(p_FechaDevolucion) = 8) THEN

            v_Datoserror := 'DevolucionesManuales: Transformo la fecha devolucion a tipo DATE';
            v_FechaDevolucion := TO_DATE(p_FechaDevolucion, 'YYYYMMDD');

            v_Datoserror := 'DevolucionesManuales: Divido la lista de facturas';
            v_ArrayFacturas := PKG_SIGA_UTIL.SPLIT_CLOB(p_ListaFacturas, ','); -- p_ListaFacturas = 'idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo, ...'

            v_Datoserror := 'DevolucionesManuales: Obtengo el indice de la primera factura';
            v_cursorFactura := v_ArrayFacturas.First;

            WHILE v_cursorFactura IS NOT NULL LOOP

                v_Datoserror := 'DevolucionesManuales: Obtengo la factura';
                v_Factura := v_ArrayFacturas(v_cursorFactura);

                BEGIN
                    v_Datoserror := 'DevolucionesManuales: Obtengo los datos de la factura';
                    v_ArrayFactura := PKG_SIGA_UTIL.SPLIT(v_Factura, '||'); -- v_Factura = 'idDisqueteCargos||idFacturaIncluidaEnDisquete||idFactura||idRecibo||idMotivo'
                
                    v_IdDisqueteCargos := v_ArrayFactura(1);
                    v_IdFIED := v_ArrayFactura(2);
                    v_IdFactura := v_ArrayFactura(3);
                    v_IdRecibo := v_ArrayFactura(4);
                    v_MotivoDevolucion := v_ArrayFactura(5);
                
                    v_Datoserror := 'DevolucionesManuales: Obtengo IMPORTE, BANCOS_CODIGO, ESTADO y FECHAEMISION de la factura';
                    SELECT FIED.IMPORTE, BI.BANCOS_CODIGO, FAC.FECHAEMISION
                        INTO v_Importe, v_CodigoBanco, v_FechaEmision
                    FROM FAC_FACTURAINCLUIDAENDISQUETE FIED,
                        FAC_DISQUETECARGOS DC,
                        FAC_BANCOINSTITUCION BI,
                        FAC_FACTURA FAC
                    WHERE FIED.IDINSTITUCION = p_Idinstitucion
                        AND FIED.IDDISQUETECARGOS = v_IdDisqueteCargos
                        AND FIED.IDFACTURAINCLUIDAENDISQUETE = v_IdFIED
                        AND FIED.IDFACTURA = v_IdFactura
                        AND FIED.IDRECIBO = v_IdRecibo                        
                        AND DC.IDINSTITUCION = FIED.IDINSTITUCION
                        AND DC.IDDISQUETECARGOS = FIED.IDDISQUETECARGOS
                        AND BI.IDINSTITUCION = DC.IDINSTITUCION
                        AND BI.BANCOS_CODIGO = DC.BANCOS_CODIGO
                        AND FAC.IDINSTITUCION = FIED.IDINSTITUCION
                        AND FAC.IDFACTURA = FIED.IDFACTURA
                        AND FIED.DEVUELTA = 'N' -- Faltaba controlar que no se haya devuelto previamente
                        AND FAC.ESTADO = 1;

                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            v_Codretorno := To_Char(5407); -- Error, no encuentra la factura
                            RAISE e_Error;
                END;

                v_Datoserror := 'DevolucionesManuales: Comprobar fecha devolucion posterior a fecha emision';
                IF (TRUNC(v_FechaDevolucion) < TRUNC(v_FechaEmision)) THEN
                    v_Codretorno := To_Char(5404);
                    RAISE e_Error;
                END IF;

                v_Datoserror := 'DevolucionesManuales: Compruebo si ya existe un disquete de devoluciones con ese codigo de banco';
                v_contadorActualDisqDevo := 0; -- Inicializo el contador actual
                FOR contadorDisq IN 1..v_contadorDisqDevo LOOP -- Recorro todos los disquetes de devoluciones

                    -- Compruebo si el disquete de devoluciones actual tiene el mismo codigo de banco
                    IF (M_DISQ_DEVO(contadorDisq).CODIGO_BANCO = v_CodigoBanco) THEN
                        v_contadorActualDisqDevo := contadorDisq; -- Guardo el disquete de devoluciones encontrado
                        EXIT; -- Salgo del bucle
                    END IF;
                END LOOP;

                -- Si v_contadorActualDisqDevo es cero, es que no lo ha encontrado
                IF (v_contadorActualDisqDevo=0) THEN
                    v_contadorDisqDevo := v_contadorDisqDevo + 1;
                    v_contadorActualDisqDevo := v_contadorDisqDevo;

                    -- Guardo los datos en la matriz
                    v_Datoserror := 'DevolucionesManuales: Guardo datos en matriz de disquetes de devoluciones - disquete nuevo';
                    M_DISQ_DEVO(v_contadorActualDisqDevo).CODIGO_BANCO := v_CodigoBanco;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO := 1;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(1).ID_FACTURA := v_IdFactura;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(1).ID_RECIBO := v_IdRecibo;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(1).IMPORTE_DEVO := v_Importe; 
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(1).MOTIVO_DEVO := v_MotivoDevolucion;
                    -- JPT (17-02-2015): Guarda IDDISQUETECARGOS y IDFACTURAINCLUIDAENDISQUETE
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(1).IDDISQUETECARGOS := v_IdDisqueteCargos;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(1).IDFACTURAINCLUIDAENDISQUETE := v_IdFIED;

                ELSE
                    v_Datoserror := 'DevolucionesManuales: Guardo datos en matriz de disquetes de devoluciones - fied nuevo';
                    v_contadorFiedDevo := M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO + 1;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).ID_FACTURA := v_IdFactura;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).ID_RECIBO := v_IdRecibo;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IMPORTE_DEVO := v_Importe;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).MOTIVO_DEVO := v_MotivoDevolucion;
                    -- JPT (17-02-2015): Guarda IDDISQUETECARGOS y IDFACTURAINCLUIDAENDISQUETE
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IDDISQUETECARGOS := v_IdDisqueteCargos;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).M_FIED_DEVO(v_contadorFiedDevo).IDFACTURAINCLUIDAENDISQUETE := v_IdFIED;
                    M_DISQ_DEVO(v_contadorActualDisqDevo).CONT_M_FIED_DEVO := v_contadorFiedDevo; 
                END IF;

                v_Datoserror := 'DevolucionesManuales: Obtengo la siguiente factura';
                v_cursorFactura := v_ArrayFacturas.Next(v_cursorFactura);
            END LOOP;
        END IF;

        /*********************************************************************/
        /******************** FIN TRATAMIENTO DATOS ****************************/
        /********************************************************************/

        v_Datoserror := 'DEVOLUCIONES: Llamada al procedimiento DevolucionesFin - Tratamiento Devoluciones';
        DevolucionesFin (
            p_Idinstitucion,
            NULL,
            M_DISQ_DEVO,
            v_contadorDisqDevo,
            v_FechaDevolucion,
            p_Idioma,
            p_Usumodificacion,
            v_Codretorno,
            v_Datoserror);
        IF (v_Codretorno <> TO_CHAR(0)) THEN
            RAISE e_Error;
        END IF;

        p_ListaIdDisquetesDevolucion := '';
        FOR contadorDisq IN 1..v_contadorDisqDevo LOOP -- Recorro todos los disquetes de devoluciones
            IF (LENGTH(p_ListaIdDisquetesDevolucion) > 0) THEN
                p_ListaIdDisquetesDevolucion := p_ListaIdDisquetesDevolucion || ';' || M_DISQ_DEVO(contadorDisq).ID_DISQUETE_DEVO;
            ELSE
                p_ListaIdDisquetesDevolucion := M_DISQ_DEVO(contadorDisq).ID_DISQUETE_DEVO;
            END IF;
        END LOOP;

        v_Datoserror := 'DevolucionesManuales: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        Exception
            When e_Error Then
                Rollback;
                p_Codretorno := v_Codretorno;
                p_Datoserror := v_Datoserror;

            When Others Then
                Rollback;
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
      End DevolucionesManuales;

    /****************************************************************************************************************/
    /* Nombre: InsertarMandatos */
    /* Descripcion: Procedimiento que inserta dos mandatos nuevos para una cuenta de cargo, uno para productos y otro para servicios */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_IDCUENTA - IN - Identificador de la cuenta - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 07/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    Procedure InsertarMandatos (
        p_idInstitucion IN NUMBER,
        p_idPersona IN NUMBER,
        p_idCuenta IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2) IS

        v_NombreInstitucion CEN_INSTITUCION.NOMBRE%TYPE;
        v_idPersonaInstitucion CEN_INSTITUCION.IDPERSONA%TYPE;

        v_IdTipoIdentificacionAcreedor CEN_PERSONA.IDTIPOIDENTIFICACION%TYPE;
        v_nifCifAcreedor CEN_PERSONA.NIFCIF%TYPE;

        v_IdTipoIdentificacionDeudor CEN_PERSONA.IDTIPOIDENTIFICACION%TYPE;
        v_nifCifDeudor CEN_PERSONA.NIFCIF%TYPE;

        v_DireccionAcreedor PKG_SIGA_CARGOS.REG_DIRECCION;
        v_DireccionDeudor PKG_SIGA_CARGOS.REG_DIRECCION;
        v_Datoserror Varchar2(4000) := Null;

    BEGIN
        v_Datoserror := 'InsertarMandatos: Obtego datos de la institucion del acreedor';
        SELECT NOMBRE, IDPERSONA
            INTO v_NombreInstitucion, v_idPersonaInstitucion
        FROM CEN_INSTITUCION
        WHERE IDINSTITUCION = p_idInstitucion;

        v_Datoserror := 'InsertarMandatos: Obtego datos de la persona del acreedor';
        SELECT IDTIPOIDENTIFICACION, NIFCIF
            INTO v_IdTipoIdentificacionAcreedor, v_nifCifAcreedor
        FROM CEN_PERSONA
        WHERE IDPERSONA = v_idPersonaInstitucion;

        v_Datoserror := 'InsertarMandatos: Obtego datos de la persona del deudor';
        SELECT IDTIPOIDENTIFICACION, NIFCIF
            INTO v_IdTipoIdentificacionDeudor, v_nifCifDeudor
        FROM CEN_PERSONA
        WHERE IDPERSONA = p_idPersona;

        v_Datoserror := 'InsertarMandatos: Consulto las direcciones de facturacion del acreedor y deudor';
        v_DireccionAcreedor := PKG_SIGA_CARGOS.ObtenerDireccionFacturacion(p_idInstitucion, v_idPersonaInstitucion);
        v_DireccionDeudor := PKG_SIGA_CARGOS.ObtenerDireccionFacturacion(p_idInstitucion, p_idPersona);

        v_Datoserror := 'InsertarMandatos: Compruebo que existe el domicilio del acreedor y deudor';
        IF (v_DireccionAcreedor.DOMICILIO IS NOT NULL AND v_DireccionDeudor.DOMICILIO IS NOT NULL) THEN

            v_Datoserror := 'InsertarMandatos: Inserto un nuevo mandato para los servicios';
            INSERT INTO CEN_MANDATOS_CUENTASBANCARIAS (
                IDINSTITUCION,
                IDPERSONA,
                IDCUENTA,
                IDMANDATO,
                TIPOMANDATO,
                REFMANDATOSEPA,
                TIPOPAGO,
                ESQUEMA,
                AUTORIZACIONB2B,
                ACREEDOR_TIPOID,
                ACREEDOR_ID,
                ACREEDOR_NOMBRE,
                ACREEDOR_DOMICILIO,
                ACREEDOR_CODIGOPOSTAL,
                ACREEDOR_IDPAIS,
                ACREEDOR_PAIS,
                ACREEDOR_IDPROVINCIA,
                ACREEDOR_PROVINCIA,
                ACREEDOR_IDPOBLACION,
                ACREEDOR_POBLACION,
                DEUDOR_TIPOID,
                DEUDOR_ID,
                DEUDOR_DOMICILIO,
                DEUDOR_CODIGOPOSTAL,
                DEUDOR_IDPAIS,
                DEUDOR_PAIS,
                DEUDOR_IDPROVINCIA,
                DEUDOR_PROVINCIA,
                DEUDOR_IDPOBLACION,
                DEUDOR_POBLACION,
                FIRMA_FECHA,
                FIRMA_LUGAR,
                FECHAUSO,
                FECHACREACION,
                USUCREACION,
                FECHAMODIFICACION,
                USUMODIFICACION,
                IDFICHEROFIRMA
            ) VALUES (
                p_idInstitucion,
                p_idPersona,
                p_idCuenta,
                1,
                0,
                p_idPersona || 'S' || TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'),
                0,
                0,
                0,
                v_IdTipoIdentificacionAcreedor,
                v_nifCifAcreedor,
                v_NombreInstitucion,
                v_DireccionAcreedor.DOMICILIO,
                v_DireccionAcreedor.CODIGOPOSTAL,
                v_DireccionAcreedor.PAIS_ID,
                v_DireccionAcreedor.PAIS_NOMBRE,
                v_DireccionAcreedor.PROVINCIA_ID,
                v_DireccionAcreedor.PROVINCIA_NOMBRE,
                v_DireccionAcreedor.POBLACION_ID,
                v_DireccionAcreedor.POBLACION_NOMBRE,
                v_IdTipoIdentificacionDeudor,
                v_nifCifDeudor,
                v_DireccionDeudor.DOMICILIO,
                v_DireccionDeudor.CODIGOPOSTAL,
                v_DireccionDeudor.PAIS_ID,
                v_DireccionDeudor.PAIS_NOMBRE,
                v_DireccionDeudor.PROVINCIA_ID,
                v_DireccionDeudor.PROVINCIA_NOMBRE,
                v_DireccionDeudor.POBLACION_ID,
                v_DireccionDeudor.POBLACION_NOMBRE,
                NULL,
                NULL,
                NULL,
                SYSDATE,
                p_Usumodificacion,
                SYSDATE,
                p_Usumodificacion,
                NULL);

            v_Datoserror := 'InsertarMandatos: Inserto un nuevo mandato para los productos';
            INSERT INTO CEN_MANDATOS_CUENTASBANCARIAS (
                IDINSTITUCION,
                IDPERSONA,
                IDCUENTA,
                IDMANDATO,
                TIPOMANDATO,
                REFMANDATOSEPA,
                TIPOPAGO,
                ESQUEMA,
                AUTORIZACIONB2B,
                ACREEDOR_TIPOID,
                ACREEDOR_ID,
                ACREEDOR_NOMBRE,
                ACREEDOR_DOMICILIO,
                ACREEDOR_CODIGOPOSTAL,
                ACREEDOR_IDPAIS,
                ACREEDOR_PAIS,
                ACREEDOR_IDPROVINCIA,
                ACREEDOR_PROVINCIA,
                ACREEDOR_IDPOBLACION,
                ACREEDOR_POBLACION,
                DEUDOR_TIPOID,
                DEUDOR_ID,
                DEUDOR_DOMICILIO,
                DEUDOR_CODIGOPOSTAL,
                DEUDOR_IDPAIS,
                DEUDOR_PAIS,
                DEUDOR_IDPROVINCIA,
                DEUDOR_PROVINCIA,
                DEUDOR_IDPOBLACION,
                DEUDOR_POBLACION,
                FIRMA_FECHA,
                FIRMA_LUGAR,
                FECHAUSO,
                FECHACREACION,
                USUCREACION,
                FECHAMODIFICACION,
                USUMODIFICACION,
                IDFICHEROFIRMA
            ) VALUES (
                p_idInstitucion,
                p_idPersona,
                p_idCuenta,
                2,
                1,
                p_idPersona || 'P' || TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'),
                0,
                0,
                0,
                v_IdTipoIdentificacionAcreedor,
                v_nifCifAcreedor,
                v_NombreInstitucion,
                v_DireccionAcreedor.DOMICILIO,
                v_DireccionAcreedor.CODIGOPOSTAL,
                v_DireccionAcreedor.PAIS_ID,
                v_DireccionAcreedor.PAIS_NOMBRE,
                v_DireccionAcreedor.PROVINCIA_ID,
                v_DireccionAcreedor.PROVINCIA_NOMBRE,
                v_DireccionAcreedor.POBLACION_ID,
                v_DireccionAcreedor.POBLACION_NOMBRE,
                v_IdTipoIdentificacionDeudor,
                v_nifCifDeudor,
                v_DireccionDeudor.DOMICILIO,
                v_DireccionDeudor.CODIGOPOSTAL,
                v_DireccionDeudor.PAIS_ID,
                v_DireccionDeudor.PAIS_NOMBRE,
                v_DireccionDeudor.PROVINCIA_ID,
                v_DireccionDeudor.PROVINCIA_NOMBRE,
                v_DireccionDeudor.POBLACION_ID,
                v_DireccionDeudor.POBLACION_NOMBRE,
                NULL,
                NULL,
                NULL,
                SYSDATE,
                p_Usumodificacion,
                SYSDATE,
                p_Usumodificacion,
                NULL);

            v_Datoserror := 'InsertarMandatos: Actualizacion de los parametros de salida';
            p_Codretorno := To_Char(0);
            p_Datoserror := Null;

        ELSIF (v_DireccionAcreedor.DOMICILIO IS NULL) THEN
            p_Codretorno := To_Char(1);
            p_Datoserror := 'InsertarMandatos: No encuentra direccion de facturacion del acreedor (IDINSTITUCION:' || p_idInstitucion || '; IDPERSONA:' || v_idPersonaInstitucion || ')';

        ELSE -- v_DireccionDeudor.DOMICILIO IS NULL
            p_Codretorno := To_Char(2);
            p_Datoserror := 'InsertarMandatos: No encuentra direccion de facturacion del deudor (IDINSTITUCION:' || p_idInstitucion || '; IDPERSONA:' || p_idPersona || ')';
        END IF;

        Exception
            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' ||Sqlerrm;
    END InsertarMandatos; 

    /****************************************************************************************************************/
    /* Nombre: RevisarAnexos */
    /* Descripcion: Procedimiento que revisa los anexos y mandatos de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER */
    /* P_IDIOMA - IN - Identificador del idioma - NUMBER */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 07/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    Procedure RevisarAnexos (
        p_idInstitucion IN NUMBER,
        p_idPersona IN NUMBER,
        p_Usumodificacion IN NUMBER,
        p_idioma IN NUMBER,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2) IS

        CURSOR C_CUENTAS(v_idInstitucion NUMBER, v_idPersona NUMBER) IS
            SELECT CUENTA.IDCUENTA,
                 (SELECT MANDATO.IDMANDATO
                 FROM CEN_MANDATOS_CUENTASBANCARIAS MANDATO
                WHERE MANDATO.IDINSTITUCION = CUENTA.IDINSTITUCION
                    AND MANDATO.IDPERSONA = CUENTA.IDPERSONA
                    AND MANDATO.IDCUENTA = CUENTA.IDCUENTA
                    AND TIPOMANDATO = 0 -- Mandato para servicios
                ) AS IdMandatoServicios
            FROM CEN_CUENTASBANCARIAS CUENTA
            WHERE CUENTA.IDINSTITUCION = v_idInstitucion
                AND CUENTA.IDPERSONA = v_idPersona
                AND CUENTA.FECHABAJA IS NULL
                AND ABONOCARGO IN ('C', 'T'); -- TIENE MARCADA CARGOS

        v_idAnexo CEN_ANEXOS_CUENTASBANCARIAS.IDANEXO%TYPE;
        v_idAnexoAutomatico CEN_ANEXOS_CUENTASBANCARIAS.IDANEXO%TYPE;
        v_origenAutomatico CEN_ANEXOS_CUENTASBANCARIAS.ORIGEN%TYPE;
        v_Direccion PKG_SIGA_CARGOS.REG_DIRECCION;
        v_origen CEN_ANEXOS_CUENTASBANCARIAS.ORIGEN%TYPE := '';
        v_origenCastellano CEN_ANEXOS_CUENTASBANCARIAS.ORIGEN%TYPE := '';
        v_mismaDireccion NUMBER;

        e_Error Exception;
        v_Codretorno Varchar2(10) := To_Char(0);
        v_Datoserror Varchar2(4000) := Null;

    BEGIN

        v_Datoserror := 'RevisarAnexos: Bucle que recorre las cuentas de la persona y obtiene sus mandatos';
        FOR v_cuentas IN C_CUENTAS(p_idInstitucion, p_idPersona) LOOP

            v_Datoserror := 'RevisarAnexos: Compruebo si tiene ya existentes los mandatos, ya que puede haber creado una direccion nueva';
            IF (v_cuentas.IdMandatoServicios IS NULL) THEN

                v_Datoserror := 'RevisarAnexos: Llamada al procedimiento InsertarMandatos para la institucion ' || p_idInstitucion || ', persona ' || p_idPersona || ' y cuenta ' || v_cuentas.IDCUENTA;
                InsertarMandatos (
                    p_idInstitucion,
                    p_idPersona,
                    v_cuentas.IDCUENTA,
                    p_Usumodificacion,
                    v_Codretorno,
                    v_Datoserror);
                If v_Codretorno <> To_Char(0) Then
                    Raise e_Error;
                End If;
            END IF;

            v_Datoserror := 'RevisarAnexos: Consulto las direcciones de facturacion de la persona';
            v_Direccion := PKG_SIGA_CARGOS.ObtenerDireccionFacturacion(p_idInstitucion, p_idPersona);

            v_Datoserror := 'RevisarAnexos: Compruebo que existe el domicilio';
            IF (LENGTH(v_Direccion.DOMICILIO) > 0) THEN
                v_Datoserror := 'RevisarAnexos: Construyo la variable para la descripcion';
                v_origen := F_SIGA_GETRECURSO_ETIQUETA('censo.fichaCliente.bancos.mandatos.anexos.cambioDireccionFacturacion', p_idioma) || ': ' || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.direccion', p_idioma) || ': ' || v_Direccion.DOMICILIO || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.cp', p_idioma) || ': ' || v_Direccion.CODIGOPOSTAL || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.pais2', p_idioma) || ': ' || v_Direccion.PAIS_NOMBRE || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.provincia', p_idioma) || ': ' || v_Direccion.PROVINCIA_NOMBRE || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.poblacion', p_idioma) || ': ' || v_Direccion.POBLACION_NOMBRE;

                v_origenCastellano := F_SIGA_GETRECURSO_ETIQUETA('censo.fichaCliente.bancos.mandatos.anexos.cambioDireccionFacturacion', 1) || ': ' || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.direccion', 1) || ': ' || v_Direccion.DOMICILIO || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.cp', 1) || ': ' || v_Direccion.CODIGOPOSTAL || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.pais2', 1) || ': ' || v_Direccion.PAIS_NOMBRE || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.provincia', 1) || ': ' || v_Direccion.PROVINCIA_NOMBRE || Chr(13) || Chr(10) ||
                                            ' - ' || F_SIGA_GETRECURSO_ETIQUETA('censo.datosDireccion.literal.poblacion', 1) || ': ' || v_Direccion.POBLACION_NOMBRE;

                v_Datoserror := 'RevisarAnexos: Calcula el nuevo identificador de anexo para el mandato de los servicios';
                SELECT NVL(MAX(IDANEXO), 0) + 1
                    INTO v_idAnexo
                FROM CEN_ANEXOS_CUENTASBANCARIAS
                WHERE IDINSTITUCION = p_idInstitucion
                    AND IDPERSONA = p_idPersona
                    AND IDCUENTA = v_cuentas.IDCUENTA
                    AND IDMANDATO = 1;

                v_Datoserror := 'RevisarAnexos: Comprueba el identificador de anexo automatico de los servicios';
                BEGIN
                    SELECT IDANEXO, ORIGEN
                        INTO v_idAnexoAutomatico, v_origenAutomatico
                    FROM CEN_ANEXOS_CUENTASBANCARIAS ANEXO1
                    WHERE IDINSTITUCION = p_idInstitucion
                        AND IDPERSONA = p_idPersona
                        AND IDCUENTA = v_cuentas.IDCUENTA
                        AND IDMANDATO = 1
                        AND ESAUTOMATICO = '1' -- Se considera ESAUTOMATICO = 1 cuando es un cambio por direccion de facturacion del deudor
                        AND IDANEXO = (
                            SELECT MAX(IDANEXO)
                            FROM CEN_ANEXOS_CUENTASBANCARIAS ANEXO2
                            WHERE ANEXO2.IDINSTITUCION = ANEXO1.IDINSTITUCION
                                AND ANEXO2.IDPERSONA = ANEXO1.IDPERSONA
                                AND ANEXO2.IDCUENTA = ANEXO1.IDCUENTA
                                AND ANEXO2.IDMANDATO = ANEXO1.IDMANDATO
                                AND ANEXO2.ESAUTOMATICO = ANEXO1.ESAUTOMATICO
                        );

                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            v_idAnexoAutomatico := 0;
                            v_origenAutomatico := NULL;
                END;

                IF (v_idAnexoAutomatico = 0) THEN
                    v_Datoserror := 'RevisarAnexos: Comprueba cambios de la direccion de facturacion en CEN_MANDATOS_CUENTASBANCARIAS de los servicios';
                    SELECT COUNT(*)
                        INTO v_mismaDireccion
                    FROM CEN_MANDATOS_CUENTASBANCARIAS MANDATO
                    WHERE MANDATO.IDINSTITUCION = p_idInstitucion
                        AND MANDATO.IDPERSONA = p_idPersona
                        AND MANDATO.IDCUENTA = v_cuentas.IDCUENTA
                        AND MANDATO.IDMANDATO = 1
                        AND MANDATO.DEUDOR_DOMICILIO = v_Direccion.DOMICILIO
                        AND MANDATO.DEUDOR_CODIGOPOSTAL = v_Direccion.CODIGOPOSTAL
                        AND MANDATO.DEUDOR_POBLACION = v_Direccion.POBLACION_NOMBRE
                        AND MANDATO.DEUDOR_PROVINCIA = v_Direccion.PROVINCIA_NOMBRE;

                ELSE
                    v_Datoserror := 'RevisarAnexos: Comprueba cambios de la direccion de facturacion en CEN_ANEXOS_CUENTASBANCARIAS de los servicios';
                    IF (TRIM(v_origenAutomatico) IN (TRIM(v_origen), TRIM(v_origenCastellano))) THEN
                        v_mismaDireccion := 1;
                    ELSE
                        v_mismaDireccion := 0;
                    END IF;
                END IF;

                -- Si hay cambios de direccion de facturacion, se crea un anexo
                IF (v_mismaDireccion=0) THEN
                    v_Datoserror := 'RevisarAnexos: Inserto un nuevo anexo para el mandato de los servicios';
                    INSERT INTO CEN_ANEXOS_CUENTASBANCARIAS (
                        IDINSTITUCION,
                        IDPERSONA,
                        IDCUENTA,
                        IDMANDATO,
                        IDANEXO,
                        ORIGEN,
                        DESCRIPCION,
                        FIRMA_FECHA,
                        FIRMA_LUGAR,
                        FECHACREACION,
                        USUCREACION,
                        FECHAMODIFICACION,
                        USUMODIFICACION,
                        IDFICHEROFIRMA,
                        ESAUTOMATICO
                    ) VALUES (
                        p_idInstitucion,
                        p_idPersona,
                        v_cuentas.IDCUENTA,
                        1,
                        v_idAnexo,
                        v_origen,
                        NULL,
                        NULL,
                        NULL,
                        SYSDATE,
                        p_Usumodificacion,
                        SYSDATE,
                        p_Usumodificacion,
                        NULL,
                        1); -- Se considera ESAUTOMATICO = 1 cuando es un cambio por direccion de facturacion del deudor
                END IF;

                v_Datoserror := 'RevisarAnexos: Calcula el nuevo identificador de anexo para el mandato de los productos';
                SELECT NVL(MAX(IDANEXO), 0) + 1
                    INTO v_idAnexo
                FROM CEN_ANEXOS_CUENTASBANCARIAS
                WHERE IDINSTITUCION = p_idInstitucion
                    AND IDPERSONA = p_idPersona
                    AND IDCUENTA = v_cuentas.IDCUENTA
                    AND IDMANDATO = 2;

                v_Datoserror := 'RevisarAnexos: Comprueba el identificador de anexo automatico de los productos';
                BEGIN
                    SELECT IDANEXO, ORIGEN
                        INTO v_idAnexoAutomatico, v_origenAutomatico
                    FROM CEN_ANEXOS_CUENTASBANCARIAS ANEXO1
                    WHERE IDINSTITUCION = p_idInstitucion
                        AND IDPERSONA = p_idPersona
                        AND IDCUENTA = v_cuentas.IDCUENTA
                        AND IDMANDATO = 2
                        AND ESAUTOMATICO = '1' -- Se considera ESAUTOMATICO = 1 cuando es un cambio por direccion de facturacion del deudor
                        AND IDANEXO = (
                            SELECT MAX(IDANEXO)
                            FROM CEN_ANEXOS_CUENTASBANCARIAS ANEXO2
                            WHERE ANEXO2.IDINSTITUCION = ANEXO1.IDINSTITUCION
                                AND ANEXO2.IDPERSONA = ANEXO1.IDPERSONA
                                AND ANEXO2.IDCUENTA = ANEXO1.IDCUENTA
                                AND ANEXO2.IDMANDATO = ANEXO1.IDMANDATO
                                AND ANEXO2.ESAUTOMATICO = ANEXO1.ESAUTOMATICO
                        );

                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            v_idAnexoAutomatico := 0;
                            v_origenAutomatico := NULL;
                END;

                IF (v_idAnexoAutomatico = 0) THEN
                    v_Datoserror := 'RevisarAnexos: Comprueba cambios de la direccion de facturacion en CEN_MANDATOS_CUENTASBANCARIAS de los productos';
                    SELECT COUNT(*)
                        INTO v_mismaDireccion
                    FROM CEN_MANDATOS_CUENTASBANCARIAS MANDATO
                    WHERE MANDATO.IDINSTITUCION = p_idInstitucion
                        AND MANDATO.IDPERSONA = p_idPersona
                        AND MANDATO.IDCUENTA = v_cuentas.IDCUENTA
                        AND MANDATO.IDMANDATO = 2
                        AND MANDATO.DEUDOR_DOMICILIO = v_Direccion.DOMICILIO
                        AND MANDATO.DEUDOR_CODIGOPOSTAL = v_Direccion.CODIGOPOSTAL
                        AND MANDATO.DEUDOR_POBLACION = v_Direccion.POBLACION_NOMBRE
                        AND MANDATO.DEUDOR_PROVINCIA = v_Direccion.PROVINCIA_NOMBRE;

                ELSE
                    v_Datoserror := 'RevisarAnexos: Comprueba cambios de la direccion de facturacion en CEN_ANEXOS_CUENTASBANCARIAS de los productos';
                    IF (TRIM(v_origenAutomatico) IN (TRIM(v_origen), TRIM(v_origenCastellano))) THEN
                        v_mismaDireccion := 1;
                    ELSE
                        v_mismaDireccion := 0;
                    END IF;
                END IF;

                -- Si hay cambios de direccion de facturacion, se crea un anexo
                IF (v_mismaDireccion=0) THEN
                    v_Datoserror := 'RevisarAnexos: Inserto un nuevo anexo para el mandato de los productos';
                    INSERT INTO CEN_ANEXOS_CUENTASBANCARIAS (
                        IDINSTITUCION,
                        IDPERSONA,
                        IDCUENTA,
                        IDMANDATO,
                        IDANEXO,
                        ORIGEN,
                        DESCRIPCION,
                        FIRMA_FECHA,
                        FIRMA_LUGAR,
                        FECHACREACION,
                        USUCREACION,
                        FECHAMODIFICACION,
                        USUMODIFICACION,
                        IDFICHEROFIRMA,
                        ESAUTOMATICO
                    ) VALUES (
                        p_idInstitucion,
                        p_idPersona,
                        v_cuentas.IDCUENTA,
                        2,
                        v_idAnexo,
                        v_origen,
                        NULL,
                        NULL,
                        NULL,
                        SYSDATE,
                        p_Usumodificacion,
                        SYSDATE,
                        p_Usumodificacion,
                        NULL,
                        1); -- Se considera ESAUTOMATICO = 1 cuando es un cambio por direccion de facturacion del deudor
                END IF;
            END IF;
        END LOOP;

        v_Datoserror := 'RevisarAnexos: Actualizacion de los parametros de salida';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        Exception
            When e_Error Then
                p_CodRetorno := v_Codretorno;
                p_DatosError := v_Datoserror;

            When Others Then
                p_Codretorno := To_Char(Sqlcode);
                p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END RevisarAnexos;

    /****************************************************************************************************************/
    /* Nombre: RevisarErroresSEPA */
    /* Descripcion: Procedimiento que revisa todos los posibles errores de facturacion de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* P_PATHFICHERO - IN - Localizacion del fichero - VARCHAR2(150) */
    /* P_TOTALERROR - OUT - Retorno con todos los mensajes de error - VARCHAR2(4000) */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    PROCEDURE RevisarErroresSEPA (
        p_idInstitucion IN NUMBER,
        p_idPersona IN NUMBER,
        p_Pathfichero IN VARCHAR2,
        p_totalError OUT VARCHAR2,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2) IS

        CURSOR C_SEPA(v_idInstitucion NUMBER, v_idPersona NUMBER) IS
            SELECT CEN_CUENTASBANCARIAS.IDINSTITUCION,
                CEN_INSTITUCION.IDPERSONA AS IDPERSONA_ACREEDOR,
                CEN_INSTITUCION.NOMBRE AS NOMBRE_ACREEDOR,
                CEN_CUENTASBANCARIAS.IDPERSONA,
                CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ', ' || CEN_PERSONA.NOMBRE AS NOMBRE,
                CEN_CUENTASBANCARIAS.IDCUENTA,
                CEN_CUENTASBANCARIAS.IBAN,
                FAC_FACTURA.IDFACTURA,
                FAC_FACTURA.IDMANDATO
            FROM FAC_FACTURA,
                CEN_CUENTASBANCARIAS,
                CEN_INSTITUCION,
                CEN_PERSONA
            WHERE (v_idInstitucion IS NULL OR v_idInstitucion = FAC_FACTURA.IDINSTITUCION)
                AND (v_idPersona IS NULL OR v_idPersona = NVL(FAC_FACTURA.IDPERSONADEUDOR, FAC_FACTURA.IDPERSONA))
                AND CEN_CUENTASBANCARIAS.IDINSTITUCION = FAC_FACTURA.IDINSTITUCION
                AND CEN_CUENTASBANCARIAS.IDPERSONA = DECODE(FAC_FACTURA.IDCUENTADEUDOR, NULL, FAC_FACTURA.IDPERSONA, FAC_FACTURA.IDPERSONADEUDOR)
                AND CEN_CUENTASBANCARIAS.IDCUENTA = NVL(FAC_FACTURA.IDCUENTADEUDOR, FAC_FACTURA.IDCUENTA)
                AND CEN_CUENTASBANCARIAS.FECHABAJA IS NULL
                AND CEN_CUENTASBANCARIAS.ABONOCARGO IN ('C', 'T')
                AND CEN_INSTITUCION.IDINSTITUCION = FAC_FACTURA.IDINSTITUCION
                AND CEN_PERSONA.IDPERSONA = CEN_CUENTASBANCARIAS.IDPERSONA

                --En estado pendiente de cobrar por banco
                AND FAC_FACTURA.IMPTOTALPORPAGAR > 0
                AND FAC_FACTURA.ESTADO = 5
                AND FAC_FACTURA.NUMEROFACTURA IS NOT NULL; -- JPT: Estaba dando ficheros sin numero de factura, cuando debe estar siempre indicado

        v_idMandato CEN_MANDATOS_CUENTASBANCARIAS.IDMANDATO%TYPE;
        v_firmaFecha CEN_MANDATOS_CUENTASBANCARIAS.FIRMA_FECHA%TYPE;
        v_tieneMandato BOOLEAN := FALSE;
        regDireccion REG_DIRECCION;

        v_textoError VARCHAR2(1000);
        f_Salida Utl_File.File_Type; -- Fichero de salida
        v_nombreFichero VARCHAR2(25) := TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') || p_idInstitucion || '.txt';
        v_cabeceraFichero VARCHAR2(1000);

        v_arrayErrores TAB_REGISTROS;
        v_contErrores NUMBER;
        v_IdFacturaOriginal FAC_FACTURA.IDFACTURA%TYPE;

    BEGIN
        -- Inicializo las variables
        v_contErrores := 0;
        v_arrayErrores.DELETE;

        -- Bucle que recorre todas las facturas a realizar en SEPA
        FOR  V_SEPA IN C_SEPA (p_idInstitucion, p_idPersona) LOOP

            -- Inicializo un texto con todas las variables posibles, para en caso de error, poder consultar los datos de los que procede
            v_textoError := ' (' ||
                'IDINSTITUCION=' || V_SEPA.IDINSTITUCION ||
                '; IDPERSONA=' || V_SEPA.IDPERSONA ||
                '; IDCUENTA=' || V_SEPA.IDCUENTA ||
                '; IDFACTURA=' || V_SEPA.IDFACTURA ||
                '; ACREEDOR=' || V_SEPA.NOMBRE_ACREEDOR ||
                '; DEUDOR=' || V_SEPA.NOMBRE ||
                '; IBAN=' || V_SEPA.IBAN ||')';

            IF (V_SEPA.IDMANDATO IS NOT NULL) THEN
                v_idMandato := V_SEPA.IDMANDATO;

            ELSE
                --Obtiene la factura original
                v_IdFacturaOriginal := F_SIGA_GETLISTFACTCOMISION(V_SEPA.IDINSTITUCION, V_SEPA.IDFACTURA, 3);

                /* JPT (09-10-2014): La actual sentencia consulta si una factura tiene productos o servicios
                * Mejora el rendimiento de la anterior:
                * - Si existen servicios, no consulta productos
                * - No necesita odernar
                */

                SELECT CASE WHEN (
                        EXISTS (
                            SELECT 1 -- Servicio
                            FROM FAC_FACTURACIONSUSCRIPCION
                            WHERE IDINSTITUCION = V_SEPA.IDINSTITUCION
                                AND IDFACTURA = v_IdFacturaOriginal
                        )
                    ) THEN 1

                    WHEN (
                        EXISTS (
                            SELECT 1 -- Producto
                            FROM PYS_COMPRA
                            WHERE IDINSTITUCION = V_SEPA.IDINSTITUCION
                                AND IDFACTURA = v_IdFacturaOriginal
                        )
                    ) THEN
                    2

                    ELSE
                        NULL
                    END INTO v_idMandato
                FROM DUAL;
            END IF;

            IF (v_idMandato IS NOT NULL) THEN
                BEGIN -- Busco el mandato
                    -- Consulta la firma del mandato en CEN_MANDATOS_CUENTASBANCARIAS
                    SELECT FIRMA_FECHA
                    INTO v_firmaFecha
                    FROM CEN_MANDATOS_CUENTASBANCARIAS
                    WHERE IDINSTITUCION = V_SEPA.IDINSTITUCION
                        AND IDPERSONA = V_SEPA.IDPERSONA
                        AND IDCUENTA = V_SEPA.IDCUENTA
                        AND IDMANDATO = v_idMandato;

                   v_tieneMandato := TRUE;

                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            v_tieneMandato := FALSE;
                END;
            END IF;

            -- Compruebo si tiene mandato asociado
            IF (v_tieneMandato) THEN

                IF (v_firmaFecha IS NULL) THEN -- No tiene firmado el mandato
                    v_contErrores := v_contErrores + 1;
                    IF (v_idMandato=1) THEN
                        v_arrayErrores(v_contErrores) := 'Error, es obligatoria la firma del mandato de los servicios' || v_textoError;
                    ELSE
                        v_arrayErrores(v_contErrores) := 'Error, es obligatoria la firma del mandato de los productos' || v_textoError;
                    END IF;
                END IF;

            ELSE -- no tiene mandato asociado
                v_contErrores := v_contErrores + 1;
                IF (v_idMandato=1) THEN
                    v_arrayErrores(v_contErrores) := 'Error, no existe el mandato de servicios' || v_textoError;
                ELSIF (v_idMandato=2) THEN
                    v_arrayErrores(v_contErrores) := 'Error, no existe el mandato de productos' || v_textoError;
                ELSE
                    v_arrayErrores(v_contErrores) := 'Error, no existe el mandato' || v_textoError;
                END IF;
            END IF;

             -- Obtiene la direccion  SEPA del acreedor
             regDireccion := ObtenerDireccionFacturacion(V_SEPA.IDINSTITUCION, V_SEPA.IDPERSONA_ACREEDOR);

            -- Compruebo que existe la direccion de facturacion del acreedor
            IF (regDireccion.DOMICILIO IS NULL) THEN
                v_contErrores := v_contErrores + 1;
                v_arrayErrores(v_contErrores) := 'Error, el acreedor no tiene direccion de facturacion' || v_textoError;
            END IF;

             -- Obtiene la direccion  SEPA del deudor
             regDireccion := ObtenerDireccionFacturacion(V_SEPA.IDINSTITUCION, V_SEPA.IDPERSONA);

            -- Compruebo que existe la direccion de facturacion del deudor
            IF (regDireccion.DOMICILIO IS NULL) THEN
                v_contErrores := v_contErrores + 1;
                v_arrayErrores(v_contErrores) := 'Error, el deudor no tiene direccion de facturacion' || v_textoError;
            END IF;
        END LOOP;

        /*** Finalizado la comprobacion de errores, pasamos a escribir un mensaje de maximo 4.000 caracteres ***/
        -- Calculo la linea de cabecera del fichero
        IF (v_contErrores > 0) THEN
            v_cabeceraFichero := 'Se han encontrado ' || v_contErrores || ' errores, que impiden realizar la generacion de ficheros de adeudos en SEPA:';
        ELSE
            v_cabeceraFichero := 'No se han encontrado errores en los datos relativos a la generacion de ficheros de adeudo en SEPA';
        END IF;

        IF (p_PathFichero IS NOT NULL) THEN
            -- Abro el fichero y pongo la cabecera
            f_Salida := Utl_File.Fopen(p_PathFichero, v_nombreFichero, 'W');
            Utl_File.Putf(f_Salida, v_cabeceraFichero);
            Utl_File.Fflush(f_Salida);

            -- Recorro los errores generados y los muestro
            FOR contError IN 1..v_contErrores LOOP
                Utl_File.Putf(f_Salida, Chr(13) || Chr(10) || contError || '. ' || v_arrayErrores(contError));
                Utl_File.Fflush(f_Salida);
            END LOOP;
            Utl_File.Fclose(f_Salida);
        END IF;

        -- Calculo la linea de cabecera del fichero
        IF (v_contErrores > 0) THEN
            p_totalError := 'Se han encontrado ' || v_contErrores || ' errores, que impiden realizar la generacion de ficheros de adeudos en SEPA:';
        ELSE
            p_totalError := 'No se han encontrado errores en los datos relativos a la generacion de ficheros de adeudo en SEPA';
        END IF;

        -- Recorro el numero de errores encontrados
        FOR contError IN 1..v_contErrores LOOP

            -- Obtengo el error
            v_textoError := Chr(13) || Chr(10) || contError || '. ' || v_arrayErrores(contError);

            -- Compruebo si tengo espacio suficiente para indicar el error
            IF (NVL(LENGTH(p_totalError),0) + LENGTH(v_textoError) <= 4000) THEN
                p_totalError := p_totalError || v_textoError;

            ELSE
                EXIT; --Salgo del bucle:
            END IF;
        END LOOP;

        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

        EXCEPTION
            WHEN OTHERS THEN
                Begin
                    IF (p_PathFichero IS NOT NULL) THEN
                        Utl_File.Fclose(f_Salida);
                    END IF;
                    p_CodRetorno := To_Char(Sqlcode);
                    p_DatosError := Sqlerrm;

                    EXCEPTION
                        WHEN OTHERS THEN
                            p_CodRetorno := To_Char(Sqlcode);
                            p_DatosError := Sqlerrm;
                End;
    END RevisarErroresSEPA;

    /****************************************************************************************************************/
    /* Nombre: F_RevisarErroresPersonaSEPA */
    /* Descripcion: Funcion que revisa todos los posibles errores de facturacion de SEPA */
    /* */
    /* P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER */
    /* P_IDPERSONA - IN - Identificador de la persona - NUMBER */
    /* */
    /* Version: 1.0 - Fecha Creacion: 30/04/2014 - Autor: Jorge Paez Trivino */
    /* Version: - Fecha Modificacion: - Autor: */
  /****************************************************************************************************************/
    FUNCTION F_RevisarErroresPersonaSEPA (
            p_idInstitucion IN NUMBER,
            p_idPersona IN NUMBER
        ) RETURN VARCHAR2 IS

        v_totalError VARCHAR2(4000) := ''; -- Indico el numero maximo de caracteres permitidos en una variable VARCHAR2 que son 4.000
        v_Codretorno Varchar2(10) := To_Char(0);
        v_Datoserror Varchar2(4000) := Null;

    BEGIN
        -- Reutilizo el procedimiento RevisarErroresSEPA
        RevisarErroresSEPA (
            p_idInstitucion,
            p_idPersona,
            NULL,
            v_totalError,
            v_Codretorno,
            v_Datoserror);
        IF (v_Codretorno <> TO_CHAR(0)) THEN
            RETURN 'Error general: ' || v_Datoserror;
        END IF;

        -- Devuelvo los errores encontrados
        RETURN v_totalError;
    END F_RevisarErroresPersonaSEPA;

END PKG_SIGA_CARGOS;
/