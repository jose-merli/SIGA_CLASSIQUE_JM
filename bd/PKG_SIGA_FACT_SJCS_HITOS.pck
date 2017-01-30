CREATE OR REPLACE package PKG_SIGA_FACT_SJCS_HITOS is

    -- Gestion de excepciones
    E_ERROR EXCEPTION;
    E_ERROR2 EXCEPTION;
    V_CODRETORNO VARCHAR2(10) := TO_CHAR(0); -- Codigo de error Oracle
    V_CODRETORNO2 VARCHAR2(10) := TO_CHAR(0); -- Codigo de error Oracle
    V_DATOSERROR VARCHAR2(200) := NULL; -- Mensaje de error Oracle        
    V_DATOSERROR2 VARCHAR2(4000) := NULL; -- Mensaje de error Oracle
  
    -- Constantes para la gestion de motivos        
    C_GAs CONSTANT NUMBER := 1;
    C_GDAs CONSTANT NUMBER := 2;
    C_AsMax CONSTANT NUMBER := 3;
    C_GDAc CONSTANT NUMBER := 4;
    C_As CONSTANT NUMBER := 5;
    C_AcFGMax CONSTANT NUMBER := 6;
    C_Ac CONSTANT NUMBER := 7;
    C_AcMax CONSTANT NUMBER := 8;    
    C_AcFG CONSTANT NUMBER := 9;
    C_AsMin CONSTANT NUMBER := 10;
    C_SOJ CONSTANT NUMBER := 12;    
    C_EJG CONSTANT NUMBER := 13;
    C_GDNoDevAsMas CONSTANT NUMBER := 14;
    C_GDAsMas CONSTANT NUMBER := 15;
    C_GDAcMas CONSTANT NUMBER := 16;
    C_AsMaxMas CONSTANT NUMBER := 17;
    C_AcMaxMas CONSTANT NUMBER := 18;
    C_AcMin CONSTANT NUMBER := 19;
    C_AsTp CONSTANT NUMBER := 20;
    C_AsTpMax CONSTANT NUMBER := 21;
    C_AcTp CONSTANT NUMBER := 22;
    C_AcTpMax CONSTANT NUMBER := 23;  
    C_AcFGTpMax CONSTANT NUMBER := 24;
    C_AcFGTp CONSTANT NUMBER := 25;
    C_AcFGMaxMas CONSTANT NUMBER := 26;
    C_AsMas CONSTANT NUMBER := 27;
    C_AsTpMas CONSTANT NUMBER := 28;
    C_AcMas CONSTANT NUMBER := 29;
    C_AcTpMas CONSTANT NUMBER := 30;
    C_AcFGMas CONSTANT NUMBER := 31;
    C_AcFGTpMas CONSTANT NUMBER := 32;
    C_AsMinMas CONSTANT NUMBER := 33;
    C_AcMinMas CONSTANT NUMBER := 34;
    C_GSNoDevMas CONSTANT NUMBER := 38;
    C_GSNoDevAsMas CONSTANT NUMBER := 39;
    C_GDNoDevAcMas CONSTANT NUMBER := 40;
    C_AsTpMaxMas CONSTANT NUMBER := 41;
    C_AcTpMaxMas CONSTANT NUMBER := 42;
    C_AcFGTpMaxMas CONSTANT NUMBER := 43;
    C_GAc CONSTANT NUMBER := 44;
    C_NDAs CONSTANT NUMBER := 45;
    C_NDAc CONSTANT NUMBER := 46;
    C_AcNoJudDeriv CONSTANT NUMBER := 47;
    C_AcJudDeriv CONSTANT NUMBER := 48;
    C_GAcMas CONSTANT NUMBER := 49;
    C_GAsMas CONSTANT NUMBER := 50;
    C_GAsMin CONSTANT NUMBER := 53;
    C_GAcMin CONSTANT NUMBER := 54;
    C_GAsMinNoDevMas CONSTANT NUMBER := 57;
    C_GAcMinNoDevMas CONSTANT NUMBER := 58;
    C_GAsMinMas CONSTANT NUMBER := 59;
    C_GAcMinMas CONSTANT NUMBER := 60;
    C_FacConfig CONSTANT NUMBER := 61;
    C_FacConfigFg CONSTANT NUMBER := 62;
    
    -- Colegio de guardias inactivas
    C_CATALAN CONSTANT CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE := 3001;
    
    -- Constantes para la gestion de colegios que derivan
    C_ID_ALBACETE CONSTANT SCS_ASISTENCIA.IDINSTITUCION%TYPE := 2002;
    C_ID_CIUDAD_REAL CONSTANT SCS_ASISTENCIA.IDINSTITUCION%TYPE := 2020;
    C_ID_GIJON CONSTANT SCS_ASISTENCIA.IDINSTITUCION%TYPE := 2027;    
    C_ID_LA_RIOJA CONSTANT SCS_ASISTENCIA.IDINSTITUCION%TYPE := 2058;
    C_ID_VALLADOLID CONSTANT SCS_ASISTENCIA.IDINSTITUCION%TYPE := 2078;    

    -- Matriz de apuntes de CABECERA DE GUARDIA
    TYPE MATRICE_CG IS RECORD(
        IDTURNO         SCS_CABECERAGUARDIAS.IDTURNO%TYPE,
        IDGUARDIA       SCS_CABECERAGUARDIAS.IDGUARDIA%TYPE,
        IDPERSONA       SCS_CABECERAGUARDIAS.IDPERSONA%TYPE,
        FECHAINICIO     SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE,
        IDTIPOAPUNTE    FCS_FACT_APUNTE.IDTIPOAPUNTE%TYPE,
        MOTIVO          FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
        IMPORTE         FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
        COSTEFIJO       SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE%TYPE,
        IDAPUNTE        FCS_FACT_APUNTE.IDAPUNTE%TYPE);
    TYPE TAB_CG IS TABLE OF MATRICE_CG INDEX BY BINARY_INTEGER;

    -- Matriz de apuntes de UNIDAD DE GUARDIA
    TYPE MATRICE_UG IS RECORD(
        IDTURNO                 SCS_GUARDIASCOLEGIADO.IDTURNO%TYPE,
        IDGUARDIA               SCS_GUARDIASCOLEGIADO.IDGUARDIA%TYPE,
        IDPERSONA               SCS_GUARDIASCOLEGIADO.IDPERSONA%TYPE,
        FECHAINICIO             SCS_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
        FECHAFIN                SCS_GUARDIASCOLEGIADO.FECHAFIN%TYPE,
        MOTIVO                  FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
        IMPORTE                 FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
        CONTADOR                NUMBER,
        COSTEFIJO               SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE%TYPE,
        IDTIPOASISTENCIACOLEGIO SCS_TIPOASISTENCIACOLEGIO.IDTIPOASISTENCIACOLEGIO%TYPE,
        IDTIPOACTUACION         SCS_TIPOACTUACION.IDTIPOACTUACION%TYPE,
        IDAPUNTE                FCS_FACT_APUNTE.IDAPUNTE%TYPE);
    TYPE TAB_UG IS TABLE OF MATRICE_UG INDEX BY BINARY_INTEGER;

    -- Matriz de apuntes de ASISTENCIAS
    TYPE MATRICE_AS IS RECORD(
        ANIO                SCS_ASISTENCIA.ANIO%TYPE,
        NUMERO              SCS_ASISTENCIA.NUMERO%TYPE,
        MOTIVO              FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
        IMPORTE             FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
        CONTADOR            NUMBER,
        IDPERSONACOLEGIADO  SCS_ASISTENCIA.IDPERSONACOLEGIADO%TYPE,
        FECHAHORA           SCS_ASISTENCIA.FECHAHORA%TYPE,
        IDAPUNTE            FCS_FACT_APUNTE.IDAPUNTE%TYPE);
    TYPE TAB_AS IS TABLE OF MATRICE_AS INDEX BY BINARY_INTEGER;

    -- Matriz de los GUARDIAS
    TYPE MATRICE_AC IS RECORD(
        ANIO                SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        NUMERO              SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        IDACTUACION         SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        MOTIVO              FCS_FACT_GUARDIASCOLEGIADO.MOTIVO%TYPE,
        IMPORTE             FCS_FACT_GUARDIASCOLEGIADO.PRECIOAPLICADO%TYPE,
        CONTADOR            NUMBER,
        COSTEFIJO           SCS_TIPOACTUACIONCOSTEFIJO.IMPORTE%TYPE,
        IDPERSONA           FCS_FACT_ACTUACIONASISTENCIA.IDPERSONA%TYPE,
        FECHAACTUACION      FCS_FACT_ACTUACIONASISTENCIA.FECHAACTUACION%TYPE,
        FECHAJUSTIFICACION  FCS_FACT_ACTUACIONASISTENCIA.FECHAJUSTIFICACION%TYPE,
        IDAPUNTE            FCS_FACT_APUNTE.IDAPUNTE%TYPE);
    TYPE TAB_AC IS TABLE OF MATRICE_AC INDEX BY BINARY_INTEGER;

    -- RECORD de FACTURACION
    TYPE DATOS_FACTURACION IS RECORD(
        IDINSTITUCION FCS_FACTURACIONJG.IDINSTITUCION%TYPE,
        FECHADESDE FCS_FACTURACIONJG.FECHADESDE%TYPE,
        FECHAHASTA FCS_FACTURACIONJG.FECHAHASTA%TYPE,
        IDAPUNTE FCS_FACT_APUNTE.IDAPUNTE%TYPE,
        CONSEJO CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE);    

    TYPE TAB_HITOS IS TABLE OF SCS_HITOFACTURABLEGUARDIA.IDHITO%TYPE INDEX BY BINARY_INTEGER;
    
    TYPE TIPO_MOTIVO IS RECORD(
        NOFACTURADO NUMBER,
        FACTURADO NUMBER,
        MAXIMONOFACTURADO NUMBER,
        MAXIMOFACTURADO NUMBER,
        MINIMONOFACTURADO NUMBER,
        MINIMOFACTURADO NUMBER);
    
    TYPE TIPO_FACTURACION IS RECORD(
        CONFIGURACION NUMBER, -- FUERA GUARDIA = 0:Actual; 1:AcModerna; 2:AcAntigua -- OTROS = 0:Actual; 1:Antiguo
        IMPORTE NUMBER,
        MINIMO NUMBER,
        MAXIMO NUMBER,
        DIAS SCS_HITOFACTURABLEGUARDIA.DIASAPLICABLES%TYPE, -- 'LMXJVSD'
        MAX_MIN_UG_CG VARCHAR2(1), -- '0'=UG; '1'=CG       
        DOBLA NUMBER,
        IMPORTEDOBLA NUMBER,
        MOTIVO TIPO_MOTIVO,
        MOTIVO_TP TIPO_MOTIVO);

    -- RECORD de CONFIG_GUARDIA
    TYPE CONFIG_GUARDIA IS RECORD(
        IMPORTESOJ NUMBER,
        IMPORTEEJG NUMBER,
        IDFACTURACION FCS_HISTORICO_HITOFACT.IDFACTURACION%TYPE,
        ESGUARDIAVG SCS_GUARDIASTURNO.IDTIPOGUARDIA%TYPE, -- Cambio facturacion guardias inactivas catalanes de VG
        CONFIG_FUERAGUARDIA  TIPO_FACTURACION,        
        CONFIG_NOPAGAGUARDIA  TIPO_FACTURACION,
        CONFIG_PAGAGUARDIA  TIPO_FACTURACION);    

    -- Matriz de los GUARDIAS
    TYPE MATRICE_CG_FACTURABLE IS RECORD(
        IDINSTITUCION           SCS_CABECERAGUARDIAS.IDINSTITUCION%TYPE,
        IDTURNO                 SCS_CABECERAGUARDIAS.IDTURNO%TYPE,
        IDGUARDIA               SCS_CABECERAGUARDIAS.IDGUARDIA%TYPE,
        IDPERSONA               SCS_CABECERAGUARDIAS.IDPERSONA%TYPE,
        FECHAINICIO             SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE,
        FECHAFIN                SCS_CABECERAGUARDIAS.FECHA_FIN%TYPE,
        IDFACTURACION           FCS_HISTORICO_HITOFACT.IDFACTURACION%TYPE,
        FECHAACT                SCS_ACTUACIONASISTENCIA.FECHA%TYPE,
        IMPORTEFACTURADO        NUMBER);
    TYPE TAB_CG_FACTURABLE IS TABLE OF MATRICE_CG_FACTURABLE INDEX BY BINARY_INTEGER;

    -- MATRICES:
    M_APUNTE_CG     TAB_CG;
    M_APUNTE_UG     TAB_UG;
    M_APUNTE_AS     TAB_AS;
    M_APUNTE_AC     TAB_AC;
    M_CG_FACTURABLE TAB_CG_FACTURABLE;

    -- INDICES PARA RECORRER LAS MATRICES:
    IND_CG            BINARY_INTEGER := 0; /* Indicador de la matriz de Cabecera de Guardias */
    IND_UG            BINARY_INTEGER := 0; /* Indicador de la matriz de Unidad de Guardia */
    IND_AS            BINARY_INTEGER := 0; /* Indicador de la matriz de Asistencias */
    IND_AC            BINARY_INTEGER := 0; /* Indicador de la matriz de Actuaciones */
    IND_CG_FACTURABLE BINARY_INTEGER := 0; /* Indicador de la matriz de Cabecera de Guardias Facturable*/
    
  /****************************************************************************************************************
    Nombre: FUN_DESCRIPCION_IDHITO
    Descripcion: Obtiene la descripcion del hito

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_IDHITO - IN - Identificador del hito - NUMBER

    Versiones (Fecha - Autor - Datos):    
    - 1.0 - 28/04/2006 - Raul Gonzalez Gonzalez
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUN_DESCRIPCION_IDHITO(
        P_IDHITO IN SCS_HITOFACTURABLE.IDHITO%TYPE
    ) RETURN SCS_HITOFACTURABLE.NOMBRE%TYPE;
    
  /****************************************************************************************************************
    Nombre: FUNC_OBTENER_IDFACTURACION
    Descripcion: Funcion que busca el IDFACTURACION de una cabecera de guardia (incluye regularizaciones)

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_FECHA - IN - Fecha donde buscar la facturacion - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUNC_OBTENER_IDFACTURACION(
        P_IDINSTITUCION IN FCS_HISTORICO_HITOFACT.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_HISTORICO_HITOFACT.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_HISTORICO_HITOFACT.IDGUARDIA%TYPE,
        P_FECHA IN FCS_FACTURACIONJG.FECHADESDE%TYPE
    ) RETURN NUMBER;    
    
    /****************************************************************************************************************
    Nombre: FUNC_ESDIAAPLICABLE
    Descripcion: Indica si tiene marcada la fecha como aplicable

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_FECHA - IN - Fecha a confirmar - DATE
    - P_DIASAPLICABLES - IN - Dias aplicables 'LMXJVSD' - VARCHAR2(7)

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/
    FUNCTION FUNC_ESDIAAPLICABLE(
        P_FECHA DATE, 
        P_DIASAPLICABLES VARCHAR2
    ) RETURN NUMBER;
    
  /****************************************************************************************************************
    Nombre: FUNC_COSTEFIJO
    Descripcion: Funcion que nos devuelve el coste fijo de una actuacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_IDACTUACION - IN - Identificador de la actuacion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 24/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 3.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUNC_COSTEFIJO(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO IN SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION IN SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        P_IDFACTURACION IN FCS_HISTO_TIPOACTCOSTEFIJO.IDFACTURACION%TYPE
    ) RETURN NUMBER;
    
  /****************************************************************************************************************
    Nombre: F_ES_TIPO_DERIVACION
    Descripcion: Indica el tipo de derivada

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_IDACTUACION - IN - Identificador de la actuacion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/ 
    FUNCTION F_ES_TIPO_DERIVACION(
        P_IDINSTITUCION SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO          SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO        SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION   SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE
    ) RETURN NUMBER;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_FACTURAR_TURNOS_OFICIO                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPARTIDAPRESUPUESTARIA       IN  Identificador de partida presupestaria                    NUMBER,
  /* P_FECHADESDE          IN       fecha desde                                                    DATE,
  /* P_FECHAHASTA          IN       fecha hasta                                                    DATE,
  /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
  /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*    07/03/2006          Pilar Duran Mu?oz                   Se obtiene ademas del precio total, el valor del  */
  /*                                                            Punto.
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_FACTURAR_TURNOS_OFI(P_IDINSTITUCION   IN NUMBER,
                                         P_IDFACTURACION   IN NUMBER,
                                         P_USUMODIFICACION IN NUMBER,
                                         P_TOTAL           OUT VARCHAR2,
                                         P_CODRETORNO      OUT VARCHAR2,
                                         P_DATOSERROR      OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:  PROC_FCS_FACTURAR_EJG                                                                      */
  /* Descripcion:  Calcula los precios para la facturacion de los SOJ.
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /* P_USUMODIFICACION     IN       Usuario                                                        NUMBER,
  /* P_TOTAL               IN       Importe total                                                  VARCHAR2(10)
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/03/2006                                                                                   */
  /* Autor:          Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_FACTURAR_EJG(P_IDINSTITUCION   IN NUMBER,
                                  P_IDFACTURACION   IN NUMBER,
                                  P_USUMODIFICACION IN NUMBER,
                                  P_TOTAL           OUT VARCHAR2,
                                  P_CODRETORNO      OUT VARCHAR2,
                                  P_DATOSERROR      OUT VARCHAR2);

    /****************************************************************************************************************
    Nombre: PROC_FCS_FACTURAR_GUARDIAS
    Descripcion: Procedimiento que realiza la facturacion de guardias SJCS

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_USUMODIFICACION - IN - Usuario modificador - NUMBER
    - P_TOTAL - OUT - Acumulador del total de la facturacion - NUMBER    
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 14/03/2005 - Pilar Duran Munoz
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/      
    PROCEDURE PROC_FCS_FACTURAR_GUARDIAS(
        P_IDINSTITUCION   IN NUMBER,
        P_IDFACTURACION   IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_TOTAL           OUT VARCHAR2,
        P_CODRETORNO      OUT VARCHAR2,
        P_DATOSERROR      OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:  PROC_FCS_FACTURAR_SOJ                                                                      */
  /* Descripcion:  Calcula los precios para la facturacion de los SOJ.
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER
  /* P_USUMODIFICACION     IN       Usuario                                                        NUMBER,
  /* P_TOTAL               IN       Importe total                                                  VARCHAR2(10)
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/03/2006                                                                                   */
  /* Autor:          Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_FACTURAR_SOJ(P_IDINSTITUCION   IN NUMBER,
                                  P_IDFACTURACION   IN NUMBER,
                                  P_USUMODIFICACION IN NUMBER,
                                  P_TOTAL           OUT VARCHAR2,
                                  P_CODRETORNO      OUT VARCHAR2,
                                  P_DATOSERROR      OUT VARCHAR2);


  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_BORRAR_FACTURACION                                                                                    */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 16/03/2005                                                                                   */
  /* Autor:          Ruben Fernandez Cano                                                                         */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_BORRAR_FACTURACION(P_IDINSTITUCION IN NUMBER,
                                        P_IDFACTURACION IN NUMBER,
                                        P_CODRETORNO    OUT VARCHAR2,
                                        P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_SOJ                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_SOJ(P_IDINSTITUCION IN NUMBER,
                                  P_IDFACTURACION IN NUMBER,
                                  P_IDPAGO        IN NUMBER,
                                  P_IDPERSONA     IN NUMBER,
                                  P_PATHFICHERO   IN VARCHAR2,
                                  P_FICHERO       IN VARCHAR2,
                                  P_CABECERAS     IN VARCHAR2,
                                  P_CODRETORNO    OUT VARCHAR2,
                                  P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_EJG                                                                       */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_EJG(P_IDINSTITUCION IN NUMBER,
                                  P_IDFACTURACION IN NUMBER,
                                  P_IDPAGO        IN NUMBER,
                                  P_IDPERSONA     IN NUMBER,
                                  P_PATHFICHERO   IN VARCHAR2,
                                  P_FICHERO       IN VARCHAR2,
                                  P_CABECERAS     IN VARCHAR2,
                                  P_CODRETORNO    OUT VARCHAR2,
                                  P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_TURNOS_OFI                                                                       */
  /* Descripcion: Exporta los datos facturados de actuaciones de designaciones                                    */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_TURNOS_OFI(P_IDINSTITUCION  IN NUMBER,
                                         P_IDFACTURACION1 IN NUMBER,
                                         P_IDFACTURACION2 IN NUMBER,
                                         P_IDPERSONA      IN NUMBER,
                                         P_PATHFICHERO    IN VARCHAR2,
                                         P_FICHERO        IN VARCHAR2,
                                         P_IDIOMA         IN NUMBER,
                                         P_CODRETORNO     OUT VARCHAR2,
                                         P_DATOSERROR     OUT VARCHAR2);
  PROCEDURE PROC_EXPORTAR_TURNOS_OTRO(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_IDPERSONA     IN NUMBER,
                                      P_PATHFICHERO   IN VARCHAR2,
                                      P_FICHERO       IN VARCHAR2,
                                      P_IDIOMA        IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2);

  Function F_GET_LISTA_DEF_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                        p_Idturno        In Scs_Designa.Idturno%Type,
                                        p_Anio           In Scs_Designa.Anio%Type,
                                        p_Numero         In Scs_Designa.Numero%Type,
                                        p_Tipodevolucion In Number,
                                        p_Idioma         In Number)
    Return Varchar2;
  Function F_GET_LISTA_EJGS_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                         p_Idturno        In Scs_Designa.Idturno%Type,
                                         p_Anio           In Scs_Designa.Anio%Type,
                                         p_Numero         In Scs_Designa.Numero%Type)
    Return Varchar2;
  Function F_GET_LISTA_ASI_DESIGNA_3001(p_Idinstitucion  In Scs_Designa.Idinstitucion%Type,
                                        p_Idturno        In Scs_Designa.Idturno%Type,
                                        p_Anio           In Scs_Designa.Anio%Type,
                                        p_Numero         In Scs_Designa.Numero%Type,
                                        p_Prefijo        In Varchar2)
    Return Varchar2;
  PROCEDURE PROC_EXPORTAR_TURNOS_3001(P_IDINSTITUCION  IN NUMBER,
                                      P_IDFACTURACION1 IN NUMBER,
                                      P_IDFACTURACION2 IN NUMBER,
                                      P_IDPERSONA      IN NUMBER,
                                      P_PATHFICHERO    IN VARCHAR2,
                                      P_FICHERO        IN VARCHAR2,
                                      P_IDIOMA         IN NUMBER,
                                      P_CODRETORNO     OUT VARCHAR2,
                                      P_DATOSERROR     OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_GUARDIAS                                                                         */
  /* Descripcion: Exporta los datos facturados de guardias                                                        */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 17/05/2006                                                                                   */
  /* Autor: David Sanchez Pina
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION  IN NUMBER,
                                       P_IDFACTURACION1 IN NUMBER,
                                       P_IDFACTURACION2 IN NUMBER,
                                       P_IDPERSONA      IN NUMBER,
                                       P_PATHFICHERO    IN VARCHAR2,
                                       P_FICHERO        IN VARCHAR2,
                                       P_IDIOMA         IN NUMBER,
                                       P_CODRETORNO     OUT VARCHAR2,
                                       P_DATOSERROR     OUT VARCHAR2);
  PROCEDURE PROC_EXPORTAR_GUARDIAS_OTRO(P_IDINSTITUCION IN NUMBER,
                                        P_IDFACTURACION IN NUMBER,
                                        P_IDPERSONA     IN NUMBER,
                                        P_PATHFICHERO   IN VARCHAR2,
                                        P_FICHERO       IN VARCHAR2,
                                        P_IDIOMA        IN NUMBER,
                                        P_CODRETORNO    OUT VARCHAR2,
                                        P_DATOSERROR    OUT VARCHAR2);
  PROCEDURE PROC_EXPORTAR_GUARDIAS_3001(P_IDINSTITUCION  IN NUMBER,
                                        P_IDFACTURACION1 IN NUMBER,
                                        P_IDFACTURACION2 IN NUMBER,
                                        P_IDPERSONA      IN NUMBER,
                                        P_PATHFICHERO    IN VARCHAR2,
                                        P_FICHERO        IN VARCHAR2,
                                        P_IDIOMA         IN NUMBER,
                                        P_CODRETORNO     OUT VARCHAR2,
                                        P_DATOSERROR     OUT VARCHAR2);

  /*
   *  Funciones que calculan los numeros de actuaciones y asistencias
   * para la exportacion del fichero de guardias en PROC_FCS_EXPORTAR_GUARDIAS
   *
   *  Si se pasa P_FECHAINICIO nulo, solo buscara en el dia exacto P_FECHAFIN
   *
   * INICIO
   */
  Function FUNC_NUM_ACTUAC_FACTURAC(p_Idinstitucion  Number,
                                    p_Idfacturacion1 Number,
                                    p_Idfacturacion2 Number,
                                    p_Idturno        Number,
                                    p_Idguardia      Number,
                                    p_Idpersona      Number,
                                    p_Fechainicio    Date,
                                    p_Fechafin       Date,
                                    p_Esfg           Varchar2) return number;
  Function FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion  Number,
                                   p_Idfacturacion1 Number,
                                   p_Idfacturacion2 Number,
                                   p_Idturno        Number,
                                   p_Idguardia      Number,
                                   p_Idpersona      Number,
                                   p_Fechainicio    Date,
                                   p_Fechafin       Date,
                                   p_Esfg           Varchar2) Return Number;
  function FUNC_NUM_ASIST_FACTURAC(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE) return number;
  function FUNC_NUM_ASIST_TOTALES(P_IDINSTITUCION NUMBER,
                                  P_IDFACTURACION NUMBER,
                                  P_IDTURNO       NUMBER,
                                  P_IDGUARDIA     NUMBER,
                                  P_IDPERSONA     NUMBER,
                                  P_FECHAINICIO   DATE,
                                  P_FECHAFIN      DATE) return number;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_GUARDIAS                                                                     */
  /* Descripcion: Exporta los datos facturados de actuaciones de designaciones                                    */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_PATHFICHERO   IN VARCHAR2,
                                       P_FICHERO       IN VARCHAR2,
                                       P_IDIOMA        IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURACIONES_INTERVALO                                                                  */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 22/06/2009                                                                                   */
  /* Autor:         Jose Barrientos Diaz                                                                          */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

 FUNCTION FUNC_FACTURACIONES_INTERVALO(P_IDINSTITUCION  NUMBER,
                                       P_IDFACTURACION1 NUMBER,
                                       P_IDFACTURACION2 NUMBER)
                                       return VARCHAR2;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURA_INTER_GRUPOS                                                                     */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG filtrado por grupos         */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 08/02/2012                                                                                   */
  /* Autor:         Susana Rubio Lopez                                                                            */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/

 FUNCTION FUNC_FACTURA_INTER_GRUPOS(P_IDINSTITUCION  NUMBER,
                                       P_IDFACTURACION1 NUMBER,
                                       P_IDFACTURACION2 NUMBER,
                                       p_GrupoFact Number)
                                       return VARCHAR2;
END PKG_SIGA_FACT_SJCS_HITOS;
/
CREATE OR REPLACE PACKAGE BODY PKG_SIGA_FACT_SJCS_HITOS IS

    -- Cursor para obtener los grupos de facturacion
    CURSOR CURSOR_GRUPOSFACTURACION (
            V_IDINSTITUCION NUMBER, 
            V_IDFACTURACION NUMBER,
            V_IDHITOGENERAL NUMBER
    ) IS
        SELECT IDGRUPOFACTURACION
        FROM FCS_FACT_GRUPOFACT_HITO
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDFACTURACION = V_IDFACTURACION 
            AND IDHITOGENERAL = V_IDHITOGENERAL;  

    -- Cursor para obtener el idturno
    CURSOR CURSOR_TURNO (
        V_IDINSTITUCION NUMBER, 
        V_IDGRUPOFACTURACION NUMBER
    ) IS
        SELECT IDTURNO
        FROM SCS_TURNO
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDGRUPOFACTURACION = V_IDGRUPOFACTURACION;

    -- Cursor para obtener el idguardia
    CURSOR CURSOR_GUARDIASTURNO (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER
    ) IS
        SELECT IDGUARDIA, 
            IDTIPOGUARDIA            
        FROM SCS_GUARDIASTURNO
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND IDTURNO = V_IDTURNO;
            
    -- Cursor para obtener la configuracion de la facturacion de una guardia
    CURSOR CURSOR_CONFIG_FACTURACION (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER, 
        V_IDFACTURACION NUMBER -- IDFACTURACION actual o historico
    ) IS
        SELECT HHTG.IDHITO,
            NVL(HHTG.PRECIOHITO, 0) AS PRECIO,
            HHTG.DIASAPLICABLES, -- 'LMXJVSD'
            HHTG.AGRUPAR --  '0'=UG; '1'=CG
        FROM FCS_HISTORICO_HITOFACT HHTG
        WHERE HHTG.IDINSTITUCION = V_IDINSTITUCION
            AND HHTG.IDTURNO = V_IDTURNO
            AND HHTG.IDGUARDIA = V_IDGUARDIA
            AND HHTG.IDFACTURACION = V_IDFACTURACION
        ORDER BY IDHITO; -- El orden es importante para obtener antes As y Ac, que los tipos            
        
    -- Cursor utilizado para cargar la matriz de cabecera de guardia
    -- Aunque el IDTIPOAPUNTE nuevo son 'CG+' y 'CG-', antiguamente para GAs y GAc se usaba 'CG'
    CURSOR CURSOR_CG (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER,
        V_FECHAFACTURACIONDESDE DATE,
        V_FECHAFACTURACIONHASTA DATE,
        V_CONFIGURACION NUMBER, -- 0:Actual; 1:Antiguo
        V_IDFACTURACION_ACTUAL NUMBER
    ) IS
        SELECT IDPERSONA,
            FECHAINICIO,
            FECHA_FIN,
            MAX(IDFACTURACION) AS IDFACTURACION,
            SUM(IMPORTEFACTURADO_CG) AS IMPORTEFACTURADO_CG
        FROM (
            --CG REALIZADAS EN EL RANGO DEL CICLO DE FACTURACION
            SELECT IDPERSONA,
                FECHAINICIO,
                FECHA_FIN,
                -- JPT: Puede tener IDFACTURACION aunque no haya facturado (porque quiero obtener la facturacion de la fecha de la cabecera de guardias)
                NVL(DECODE(V_CONFIGURACION, 0, NULL, FUNC_OBTENER_IDFACTURACION(IDINSTITUCION, IDTURNO, IDGUARDIA, FECHAINICIO)),V_IDFACTURACION_ACTUAL) AS IDFACTURACION,
                0 AS IMPORTEFACTURADO_CG
            FROM SCS_CABECERAGUARDIAS CG
            WHERE IDINSTITUCION = V_IDINSTITUCION
                AND IDTURNO = V_IDTURNO
                AND IDGUARDIA = V_IDGUARDIA
                AND NVL(VALIDADO, '0') = '1' -- validadas
                AND TRUNC(NVL(FECHAVALIDACION, FECHAINICIO)) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA) -- dentro del rango de fechas de la facturacion
                AND NOT EXISTS ( -- NO FACTURADO
                    SELECT 1
                    FROM FCS_FACT_APUNTE FAC_CG
                    WHERE CG.IDINSTITUCION = FAC_CG.IDINSTITUCION
                        AND CG.IDTURNO = FAC_CG.IDTURNO
                        AND CG.IDGUARDIA = FAC_CG.IDGUARDIA
                        AND CG.IDPERSONA = FAC_CG.IDPERSONA
                        AND CG.FECHAINICIO = FAC_CG.FECHAINICIO
                )
            UNION -- ALL - Puede darse el caso de tener cabeceras no facturadas iguales, que es necesario por si no se facturo algo, pero si en el futuro se justifico
            -- CG con actuaciones (que no son de fuera de guardia) justificadas en el rango del ciclo de facturacion
            SELECT IDPERSONA,
                   FECHAINICIO,
                   FECHA_FIN,
                   NVL(DECODE(V_CONFIGURACION, 0, NULL, FUNC_OBTENER_IDFACTURACION(IDINSTITUCION, IDTURNO, IDGUARDIA, FECHAINICIO)),V_IDFACTURACION_ACTUAL) AS IDFACTURACION,
                   (
                    SELECT NVL(SUM(NVL(FAC_CG.PRECIOAPLICADO,0)),0)
                    FROM FCS_FACT_APUNTE FAC_CG
                    WHERE FAC_CG.IDINSTITUCION = CG.IDINSTITUCION
                        AND FAC_CG.IDTURNO = CG.IDTURNO
                        AND FAC_CG.IDGUARDIA = CG.IDGUARDIA
                        AND FAC_CG.IDPERSONA = CG.IDPERSONA
                        AND TRUNC(FAC_CG.FECHAINICIO) = CG.FECHAINICIO
                        AND FAC_CG.IDTIPOAPUNTE IN  ('CG', 'CG+', 'CG-') -- No incluyo los apuntes de fuera de guardia
                    ) AS IMPORTEFACTURADO_CG
            FROM SCS_CABECERAGUARDIAS CG
            WHERE IDINSTITUCION = V_IDINSTITUCION
                AND IDTURNO = V_IDTURNO
                AND IDGUARDIA = V_IDGUARDIA
                AND NVL(VALIDADO, '0') = '1' -- validadas
                AND EXISTS ( -- JPT: que tengan al menos una actuacion que no sea fuera de guardia...
                    SELECT 1
                    FROM SCS_GUARDIASCOLEGIADO UG,
                        SCS_ASISTENCIA ASI,
                        SCS_ACTUACIONASISTENCIA ACT
                    WHERE CG.IDINSTITUCION = UG.IDINSTITUCION
                        AND CG.IDTURNO = UG.IDTURNO
                        AND CG.IDGUARDIA = UG.IDGUARDIA
                        AND CG.FECHAINICIO = UG.FECHAINICIO
                        AND CG.IDPERSONA = UG.IDPERSONA
                        AND UG.IDINSTITUCION = ASI.IDINSTITUCION
                        AND UG.IDTURNO = ASI.IDTURNO
                        AND UG.IDGUARDIA = ASI.IDGUARDIA
                        AND UG.IDPERSONA = ASI.IDPERSONACOLEGIADO
                        AND UG.FECHAFIN = TRUNC(ASI.FECHAHORA)
                        AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                        AND ASI.ANIO = ACT.ANIO
                        AND ASI.NUMERO = ACT.NUMERO
                        AND ACT.VALIDADA = '1' -- Validada
                        AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                        AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA) -- con fecha de justificacion en el rango de fechas de la facturacion
                        AND ACT.DIADESPUES = 'N' -- actuacion que NO es del dia despues (NO es fuera de guardia)
                        AND NOT EXISTS ( -- sin facturar
                            SELECT 1
                            FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                            WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                                AND FAC.ANIO = ACT.ANIO
                                AND FAC.NUMERO = ACT.NUMERO
                                AND FAC.IDACTUACION = ACT.IDACTUACION
                        )
                )
        )
        GROUP BY IDPERSONA,
              FECHAINICIO,
              FECHA_FIN
        ORDER BY IDFACTURACION DESC,  -- JPT: Se ordena DESC para que salgan primero los actuales
             FECHAINICIO,
             IDPERSONA;
             
        -- Cursor utilizado para cargar la matriz de cabecera de guardia (de las actuaciones Fuera de Guardia)
        -- Aunque el IDTIPOAPUNTE nuevo solo es 'FG', antiguamente para AcFGTp se usaba 'FGTp'
        CURSOR CURSOR_CG_FG(
            V_IDINSTITUCION NUMBER, 
            V_IDTURNO NUMBER, 
            V_IDGUARDIA NUMBER,
            V_FECHAFACTURACIONDESDE DATE,
            V_FECHAFACTURACIONHASTA DATE
        ) IS
            -- CG con actuaciones validadas y de fuera de guardia, justificadas en el rango de fechas de la facturacion
            SELECT CG.IDPERSONA,
                CG.FECHAINICIO,
                CG.FECHA_FIN,
                (
                    SELECT NVL(SUM(NVL(FAC_CG.PRECIOAPLICADO,0)),0)
                    FROM FCS_FACT_APUNTE FAC_CG
                    WHERE FAC_CG.IDINSTITUCION = V_IDINSTITUCION
                        AND FAC_CG.IDTURNO = V_IDTURNO
                        AND FAC_CG.IDGUARDIA = V_IDGUARDIA
                        AND FAC_CG.IDPERSONA = CG.IDPERSONA
                        AND TRUNC(FAC_CG.FECHAINICIO) = CG.FECHAINICIO
                        AND FAC_CG.IDTIPOAPUNTE IN ('FG', 'FGTp') -- Solo consulto los apuntes de fuera de guardia
                ) AS IMPORTEFACTURADO_CG
            FROM SCS_CABECERAGUARDIAS CG,
                SCS_GUARDIASCOLEGIADO UG,
                SCS_ASISTENCIA ASI,
                SCS_ACTUACIONASISTENCIA ACT
            WHERE CG.IDINSTITUCION = V_IDINSTITUCION
                AND CG.IDTURNO = V_IDTURNO
                AND CG.IDGUARDIA = V_IDGUARDIA
                AND CG.VALIDADO = '1' -- Validada
                AND CG.IDINSTITUCION = UG.IDINSTITUCION
                AND CG.IDTURNO = UG.IDTURNO
                AND CG.IDGUARDIA = UG.IDGUARDIA
                AND CG.FECHAINICIO = UG.FECHAINICIO
                AND CG.IDPERSONA = UG.IDPERSONA
                AND UG.IDINSTITUCION = ASI.IDINSTITUCION
                AND UG.IDTURNO = ASI.IDTURNO
                AND UG.IDGUARDIA = ASI.IDGUARDIA
                AND UG.IDPERSONA = ASI.IDPERSONACOLEGIADO
                AND UG.FECHAFIN = TRUNC(ASI.FECHAHORA)
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO
                AND ACT.VALIDADA = '1' -- Validada
                AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA) -- con fecha de justificacion en el rango de fechas de la facturacion
                AND ACT.DIADESPUES = 'S' -- actuacion del dia despues (fuera de guardia)
                AND NOT EXISTS ( -- sin facturar
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                    WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION)                            
            GROUP BY CG.IDPERSONA,
                  CG.FECHAINICIO,
                  CG.FECHA_FIN
            ORDER BY CG.FECHAINICIO,
                CG.IDPERSONA;
            
        -- Cursor utilizado para cargar la matriz de cabecera de guardia (de las actuaciones Fuera de Guardia)
        CURSOR CURSOR_CG_FG_PARTIDA(
            V_IDINSTITUCION NUMBER, 
            V_IDTURNO NUMBER, 
            V_IDGUARDIA NUMBER,
            V_FECHAFACTURACIONDESDE DATE,
            V_FECHAFACTURACIONHASTA DATE
        ) IS
            -- CG con actuaciones validadas y de fuera de guardia, justificadas en el rango de fechas de la facturacion
            SELECT DISTINCT ASI.IDPERSONACOLEGIADO,
                TRUNC(ASI.FECHAHORA) AS FECHA_ASI
            FROM SCS_ASISTENCIA ASI,
                SCS_ACTUACIONASISTENCIA ACT
            WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
                AND ASI.IDTURNO = V_IDTURNO
                AND ASI.IDGUARDIA = V_IDGUARDIA
                AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
                AND ASI.ANIO = ACT.ANIO
                AND ASI.NUMERO = ACT.NUMERO                
                AND ACT.VALIDADA = '1' -- Validada
                AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA) -- con fecha de justificacion en el rango de fechas de la facturacion
                AND ACT.DIADESPUES = 'S' -- actuacion del dia despues (fuera de guardia)
                AND NOT EXISTS ( -- sin facturar
                    SELECT 1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC
                    WHERE FAC.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC.ANIO = ACT.ANIO
                        AND FAC.NUMERO = ACT.NUMERO
                        AND FAC.IDACTUACION = ACT.IDACTUACION)
            ORDER BY FECHA_ASI,
                ASI.IDPERSONACOLEGIADO;                      
        
    -- Cursor para obtener las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin de apuntes que pagan guardia
    -- C_GSNoDevAsMas + C_GAsMinNoDevMas + C_GDNoDevAsMas ... son motivos antiguos de GAs
    -- C_GSNoDevMas + C_GAcMinNoDevMas + C_GDNoDevAcMas ... son motivos antiguos de GAc
    CURSOR CURSOR_UG_PG (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER, 
        V_IDPERSONA NUMBER, 
        V_FECHAINICIO DATE, 
        V_DIASAPLICABLES VARCHAR2,
        V_TIPO NUMBER
    ) IS
        SELECT DISTINCT GC.FECHAINICIO,
            GC.FECHAFIN,
            (
                SELECT NVL(SUM(NVL(FGC.PRECIOAPLICADO, 0)), -1) -- El -1 es para saber si no se ha facturado y no tener q lanzar la sentencia con un count otra vez
                FROM FCS_FACT_GUARDIASCOLEGIADO FGC
                WHERE (
                        (V_TIPO = C_GAs AND FGC.IDHITO IN (C_GAs, C_GAsMas, C_GDAs, C_GDAsMas, C_GAsMin, C_GAsMinMas, C_GSNoDevAsMas, C_GAsMinNoDevMas, C_GDNoDevAsMas))
                        OR (V_TIPO = C_GAc AND FGC.IDHITO IN (C_GAc, C_GAcMas, C_GDAc, C_GDAcMas, C_GAcMin, C_GAcMinMas, C_GSNoDevMas, C_GAcMinNoDevMas, C_GDNoDevAcMas))   
                    )                        
                    AND FGC.IDINSTITUCION = GC.IDINSTITUCION
                    AND FGC.IDTURNO = GC.IDTURNO
                    AND FGC.IDGUARDIA = GC.IDGUARDIA
                    AND FGC.IDPERSONA = GC.IDPERSONA
                    AND FGC.FECHAINICIO = GC.FECHAINICIO
                    AND FGC.FECHAFIN = GC.FECHAFIN    
            ) AS IMPORTE_FACTURADO_UG
        FROM SCS_GUARDIASCOLEGIADO GC
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND GC.IDTURNO = V_IDTURNO
            AND GC.IDGUARDIA = V_IDGUARDIA
            AND GC.IDPERSONA = V_IDPERSONA
            AND TRUNC(GC.FECHAINICIO) = V_FECHAINICIO
            AND FUNC_ESDIAAPLICABLE(GC.FECHAFIN, V_DIASAPLICABLES) = 1
        ORDER BY GC.FECHAFIN;
        
    -- Cursor para obtener las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin buscando solo los apuntes que no pagan guardia (ignorando tipos y fuera de guardia)
    CURSOR CURSOR_UG_NPG (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER, 
        V_IDPERSONA NUMBER, 
        V_FECHAINICIO DATE, 
        V_DIASAPLICABLES VARCHAR2,
        V_TIPO NUMBER
    ) IS
        SELECT DISTINCT GC.FECHAINICIO,
            GC.FECHAFIN,
            (
                SELECT NVL(SUM(NVL(FGC.PRECIOAPLICADO, 0)), -1) -- El -1 es para saber si no se ha facturado y no tener q lanzar la sentencia con un count otra vez
                FROM FCS_FACT_GUARDIASCOLEGIADO FGC
                WHERE (
                        (V_TIPO = C_As AND FGC.IDHITO IN (C_As, C_AsMas, C_AsMax, C_AsMaxMas, C_AsMin, C_AsMinMas))
                        OR (V_TIPO = C_Ac AND FGC.IDHITO IN (C_Ac, C_AcMas, C_AcMax, C_AcMaxMas, C_AcMin, C_AcMinMas))   
                    )
                    AND FGC.IDINSTITUCION = GC.IDINSTITUCION
                    AND FGC.IDTURNO = GC.IDTURNO
                    AND FGC.IDGUARDIA = GC.IDGUARDIA
                    AND FGC.IDPERSONA = GC.IDPERSONA
                    AND FGC.FECHAINICIO = GC.FECHAINICIO
                    AND FGC.FECHAFIN = GC.FECHAFIN
            ) AS IMPORTE_FACTURADO_UG 
        FROM SCS_GUARDIASCOLEGIADO GC
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND GC.IDTURNO = V_IDTURNO
            AND GC.IDGUARDIA = V_IDGUARDIA
            AND GC.IDPERSONA = V_IDPERSONA
            AND TRUNC(GC.FECHAINICIO) = V_FECHAINICIO
            AND FUNC_ESDIAAPLICABLE(GC.FECHAFIN, V_DIASAPLICABLES) = 1
        ORDER BY GC.FECHAFIN;
        
    -- Cursor para obtener las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin buscando solo los apuntes fuera de guardia (ignorando tipos)
    -- No existia en AcFGTp para las UG antiguas ... pero no pasa nada porque no existe maximo por UG, siempre se acumula 
    CURSOR CURSOR_UG_FG (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER, 
        V_IDPERSONA NUMBER, 
        V_FECHAINICIO DATE
    ) IS
        SELECT DISTINCT GC.FECHAINICIO,
            GC.FECHAFIN,
            (
                SELECT NVL(SUM(NVL(FGC.PRECIOAPLICADO, 0)), -1) -- El -1 es para saber si no se ha facturado y no tener q lanzar la sentencia con un count otra vez
                FROM FCS_FACT_GUARDIASCOLEGIADO FGC
                WHERE FGC.IDHITO IN (C_AcFg, C_AcFgMas, C_AcFgMax, C_AcFgMaxMas)   
                    AND FGC.IDINSTITUCION = GC.IDINSTITUCION
                    AND FGC.IDTURNO = GC.IDTURNO
                    AND FGC.IDGUARDIA = GC.IDGUARDIA
                    AND FGC.IDPERSONA = GC.IDPERSONA
                    AND FGC.FECHAINICIO = GC.FECHAINICIO
                    AND FGC.FECHAFIN = GC.FECHAFIN
            ) AS IMPORTE_FACTURADO_UG 
        FROM SCS_GUARDIASCOLEGIADO GC
        WHERE IDINSTITUCION = V_IDINSTITUCION
            AND GC.IDTURNO = V_IDTURNO
            AND GC.IDGUARDIA = V_IDGUARDIA
            AND GC.IDPERSONA = V_IDPERSONA
            AND TRUNC(GC.FECHAINICIO) = V_FECHAINICIO
        ORDER BY GC.FECHAFIN;                
            
    -- Obtenemos los tipos de asistencias del colegio
    CURSOR CURSOR_TIPOS_AS (
        V_IDINSTITUCION NUMBER,
        V_IDTURNO NUMBER,
        V_IDGUARDIA NUMBER,
        V_IDPERSONA NUMBER,
        V_FECHAINICIO DATE,
        V_FECHAFIN DATE
    ) IS
        SELECT TIPO.IDTIPOASISTENCIACOLEGIO,
            (
                SELECT NVL(SUM(NVL(FGC.PRECIOAPLICADO, 0)), -1) -- El -1 es para saber si no se ha facturado y no tener q lanzar la sentencia con un count otra vez
                FROM FCS_FACT_GUARDIASCOLEGIADO FGC
                WHERE IDHITO IN (C_AsTp, C_AsTpMas, C_AsTpMax, C_AsTpMaxMas) -- apuntes de tipo As
                    AND FGC.IDINSTITUCION = TIPO.IDINSTITUCION
                    AND FGC.IDTURNO = V_IDTURNO
                    AND FGC.IDGUARDIA = V_IDGUARDIA
                    AND FGC.IDPERSONA = V_IDPERSONA
                    AND FGC.FECHAINICIO = V_FECHAINICIO
                    AND FGC.FECHAFIN = V_FECHAFIN
                    AND FGC.IDTIPOASISTENCIACOLEGIO = TIPO.IDTIPOASISTENCIACOLEGIO    
            ) AS IMPORTE_FACTURADO_TP
        FROM SCS_TIPOASISTENCIACOLEGIO TIPO
        WHERE TIPO.IDINSTITUCION = V_IDINSTITUCION;
        
    -- Cursor para obtener los tipos de actuaciones del colegio
    CURSOR CURSOR_TIPOS_AC (
        V_IDINSTITUCION NUMBER,
        V_IDTURNO NUMBER,
        V_IDGUARDIA NUMBER,
        V_IDPERSONA NUMBER,
        V_FECHAINICIO DATE,
        V_FECHAFIN DATE,
        V_FUERAGUARDIA VARCHAR2 --  N:NoFueraGuardia; S:FueraGuardia
    ) IS                
        SELECT TIPO.IDTIPOASISTENCIA,
            TIPO.IDTIPOACTUACION,
            (
                SELECT NVL(SUM(NVL(FGC.PRECIOAPLICADO, 0)), -1) -- El -1 es para saber si no se ha facturado y no tener q lanzar la sentencia con un count otra vez
                FROM FCS_FACT_GUARDIASCOLEGIADO FGC
                WHERE (
                        (V_FUERAGUARDIA = 'N' AND IDHITO IN (C_AcTp, C_AcTpMas, C_AcTpMax, C_AcTpMaxMas)) -- apuntes de tipo Ac
                        OR (V_FUERAGUARDIA = 'S' AND IDHITO IN (C_AcFGTp, C_AcFGTpMas, C_AcFGTpMax, C_AcFGTpMaxMas)) -- apuntes de tipo AcFG 
                    )
                    AND FGC.IDINSTITUCION = TIPO.IDINSTITUCION
                    AND FGC.IDTURNO = V_IDTURNO
                    AND FGC.IDGUARDIA = V_IDGUARDIA
                    AND FGC.IDPERSONA = V_IDPERSONA
                    AND FGC.FECHAINICIO = V_FECHAINICIO
                    AND FGC.FECHAFIN = V_FECHAFIN
                    AND FGC.IDTIPOASISTENCIACOLEGIO = TIPO.IDTIPOASISTENCIA
                    AND FGC.IDTIPOACTUACION = TIPO.IDTIPOACTUACION
            ) AS IMPORTE_FACTURADO_TP
        FROM SCS_TIPOACTUACION TIPO
        WHERE TIPO.IDINSTITUCION = V_IDINSTITUCION;        
        
    -- Cursor para obtener las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
    CURSOR CURSOR_AS (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER, 
        V_IDPERSONA NUMBER, 
        V_FECHAFIN DATE, 
        V_DIASAPLICABLES VARCHAR2,
        V_FECHAFACTURACIONDESDE DATE,
        V_FECHAFACTURACIONHASTA DATE,
        V_IDTIPOASISTENCIACOLEGIO NUMBER, -- Si no aplica tipos es NULL
        V_IDTIPOACTUACION NUMBER, -- Si no aplica tipos por actuaciones es NULL
        V_HITO_NOFACTURADO NUMBER
    ) IS 
        SELECT ASI.ANIO,
            ASI.NUMERO,
            ASI.FECHAHORA,
            SUM(NVL(FAC_ASI.PRECIOAPLICADO,0)) AS IMPORTE_FACTURADO_AS,
            1 AS FACTURADO
        FROM SCS_ASISTENCIA ASI,
            FCS_FACT_ASISTENCIA FAC_ASI -- FACTURAD0
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND (V_IDTIPOASISTENCIACOLEGIO IS NULL OR ASI.IDTIPOASISTENCIACOLEGIO = V_IDTIPOASISTENCIACOLEGIO) -- Para tratamiento con tipos
            AND FUNC_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1            
            AND FAC_ASI.IDINSTITUCION = ASI.IDINSTITUCION -- FACTURAD0
            AND FAC_ASI.ANIO = ASI.ANIO -- FACTURAD0
            AND FAC_ASI.NUMERO = ASI.NUMERO -- FACTURAD0
            AND FAC_ASI.IDHITO = V_HITO_NOFACTURADO -- Solo nos interesa el hito con el que se facturo por primera vez
        GROUP BY ASI.IDINSTITUCION,
            ASI.ANIO,
            ASI.NUMERO,
            ASI.IDTIPOASISTENCIACOLEGIO,
            ASI.IDPERSONACOLEGIADO,
            ASI.FECHAHORA
        UNION ALL -- Da mejor rendimiento UNION ALL q UNION 
        -- Incluyo las asistencias sin facturar con actuaciones sin facturar dentro del periodo de la facturacion
        SELECT ASI.ANIO,
            ASI.NUMERO,
            ASI.FECHAHORA,
            0 AS IMPORTE_FACTURADO_AS,
            0 AS FACTURADO
        FROM SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND ASI.FECHAANULACION IS NULL -- Sin anular
            AND (V_IDTIPOASISTENCIACOLEGIO IS NULL OR ASI.IDTIPOASISTENCIACOLEGIO = V_IDTIPOASISTENCIACOLEGIO) -- Para tratamiento con tipos
            AND FUNC_ESDIAAPLICABLE(ASI.FECHAHORA, V_DIASAPLICABLES) = 1
            AND NOT EXISTS ( -- NO FACTURADO
                SELECT  1
                FROM FCS_FACT_ASISTENCIA FAC_ASI
                WHERE FAC_ASI.IDINSTITUCION = ASI.IDINSTITUCION
                    AND FAC_ASI.ANIO = ASI.ANIO
                    AND FAC_ASI.NUMERO = ASI.NUMERO
            )
            AND EXISTS ( -- Comprueba que la asistencia tenga actuaciones sin facturar dentro del periodo de la facturacion
                SELECT 1
                FROM SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = ASI.IDINSTITUCION
                    AND ACT.ANIO = ASI.ANIO
                    AND ACT.NUMERO = ASI.NUMERO
                    AND (V_IDTIPOASISTENCIACOLEGIO IS NULL OR ACT.IDTIPOASISTENCIA = V_IDTIPOASISTENCIACOLEGIO) -- Para tratamiento con tipos
                    AND (V_IDTIPOACTUACION IS NULL OR ACT.IDTIPOACTUACION = V_IDTIPOACTUACION) -- Para tratamiento con tipos
                    AND NOT EXISTS ( -- NO FACTURADO
                        SELECT  1
                        FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                        WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                            AND FAC_ACT.ANIO = ACT.ANIO
                            AND FAC_ACT.NUMERO = ACT.NUMERO
                            AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                    AND ACT.VALIDADA = '1' -- Validada
                    AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                    AND ACT.FECHAJUSTIFICACION IS NOT NULL
                    AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA)
                    AND ACT.DIADESPUES = 'N'
            );
        
    -- Cursor para obtener las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
    -- No usa el importe facturado de AS, ya que siempre se factura por actuaciones
    CURSOR CURSOR_ASFG (
        V_IDINSTITUCION NUMBER, 
        V_IDTURNO NUMBER, 
        V_IDGUARDIA NUMBER, 
        V_IDPERSONA NUMBER, 
        V_FECHAFIN DATE, 
        V_FECHAFACTURACIONDESDE DATE,
        V_FECHAFACTURACIONHASTA DATE,
        V_IDTIPOASISTENCIACOLEGIO NUMBER, -- Si no aplica tipos es NULL
        V_IDTIPOACTUACION NUMBER, -- Si no aplica tipos es NULL
        V_HITO_NOFACTURADO NUMBER
    ) IS 
    SELECT ASI.ANIO,
            ASI.NUMERO,
            ASI.FECHAHORA,
            1 AS FACTURADO
        FROM SCS_ASISTENCIA ASI,
            FCS_FACT_ASISTENCIA FAC_ASI -- FACTURAD0
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND (V_IDTIPOASISTENCIACOLEGIO IS NULL OR ASI.IDTIPOASISTENCIACOLEGIO = V_IDTIPOASISTENCIACOLEGIO) -- Para tratamiento con tipos        
            AND FAC_ASI.IDINSTITUCION = ASI.IDINSTITUCION -- FACTURAD0
            AND FAC_ASI.ANIO = ASI.ANIO -- FACTURAD0
            AND FAC_ASI.NUMERO = ASI.NUMERO -- FACTURAD0
            AND FAC_ASI.IDHITO = V_HITO_NOFACTURADO -- Solo nos interesa el hito con el que se facturo por primera vez
        GROUP BY ASI.IDINSTITUCION,
            ASI.ANIO,
            ASI.NUMERO,
            ASI.IDTIPOASISTENCIACOLEGIO,
            ASI.IDPERSONACOLEGIADO,
            ASI.FECHAHORA
        UNION ALL -- Da mejor rendimiento UNION ALL q UNION 
        -- Incluyo las asistencias sin facturar con actuaciones sin facturar dentro del periodo de la facturacion
        SELECT ASI.ANIO,
            ASI.NUMERO,
            ASI.FECHAHORA,
            0 AS FACTURADO
        FROM SCS_ASISTENCIA ASI
        WHERE ASI.IDINSTITUCION = V_IDINSTITUCION
            AND ASI.IDTURNO = V_IDTURNO
            AND ASI.IDGUARDIA = V_IDGUARDIA
            AND ASI.IDPERSONACOLEGIADO = V_IDPERSONA
            AND TRUNC(ASI.FECHAHORA) = V_FECHAFIN
            AND ASI.FECHAANULACION IS NULL -- Sin anular
            AND (V_IDTIPOASISTENCIACOLEGIO IS NULL OR ASI.IDTIPOASISTENCIACOLEGIO = V_IDTIPOASISTENCIACOLEGIO) -- Para tratamiento con tipos
            AND NOT EXISTS ( -- NO FACTURADO
                SELECT  1
                FROM FCS_FACT_ASISTENCIA FAC_ASI
                WHERE FAC_ASI.IDINSTITUCION = ASI.IDINSTITUCION
                    AND FAC_ASI.ANIO = ASI.ANIO
                    AND FAC_ASI.NUMERO = ASI.NUMERO
            )
            AND EXISTS ( -- Comprueba que la asistencia tenga actuaciones sin facturar dentro del periodo de la facturacion
                SELECT 1
                FROM SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = ASI.IDINSTITUCION
                    AND ACT.ANIO = ASI.ANIO
                    AND ACT.NUMERO = ASI.NUMERO
                    AND (V_IDTIPOASISTENCIACOLEGIO IS NULL OR ACT.IDTIPOASISTENCIA = V_IDTIPOASISTENCIACOLEGIO) -- Para tratamiento con tipos
                    AND (V_IDTIPOACTUACION IS NULL OR ACT.IDTIPOACTUACION = V_IDTIPOACTUACION) -- Para tratamiento con tipos
                    AND NOT EXISTS ( -- NO FACTURADO
                        SELECT  1
                        FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                        WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                            AND FAC_ACT.ANIO = ACT.ANIO
                            AND FAC_ACT.NUMERO = ACT.NUMERO
                            AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                    AND ACT.VALIDADA = '1' -- Validada
                    AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                    AND ACT.FECHAJUSTIFICACION IS NOT NULL
                    AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA)
                    AND ACT.DIADESPUES = 'S'
            );
            
    -- Cursor para obtener las actuaciones sin facturar justificadas en el periodo de facturacion    
    CURSOR CURSOR_ACT_AS (
        V_IDINSTITUCION NUMBER,            
        V_ANIO NUMBER,
        V_NUMERO NUMBER,
        V_IDFACTURACION NUMBER, -- Puede no ser el actual
        V_FECHAFACTURACIONDESDE DATE,
        V_FECHAFACTURACIONHASTA DATE
    ) IS 
        SELECT ACT.IDACTUACION,
            ACT.FECHA,
            ACT.FECHAJUSTIFICACION,
            NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_IDFACTURACION), 0) AS COSTEFIJO
        FROM SCS_ACTUACIONASISTENCIA ACT
        WHERE ACT.IDINSTITUCION = V_IDINSTITUCION
            AND ACT.ANIO = V_ANIO
            AND ACT.NUMERO = V_NUMERO
            AND NOT EXISTS ( -- NO FACTURADO
                SELECT  1
                FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                    AND FAC_ACT.ANIO = ACT.ANIO
                    AND FAC_ACT.NUMERO = ACT.NUMERO
                    AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
            AND ACT.VALIDADA = '1' -- Validada
            AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
            AND ACT.FECHAJUSTIFICACION IS NOT NULL
            AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA)
            AND ACT.DIADESPUES = 'N';
        
        -- Cursor para obtener las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
        CURSOR CURSOR_ACT_AC (
            V_IDINSTITUCION NUMBER, 
            V_ANIO NUMBER,
            V_NUMERO NUMBER,
            V_IDFACTURACION NUMBER, -- Puede no ser el actual
            V_FECHAFACTURACIONDESDE DATE,
            V_FECHAFACTURACIONHASTA DATE,
            V_IDTIPOASISTENCIA NUMBER, -- Si no aplica tipos es NULL
            V_IDTIPOACTUACION NUMBER -- Si no aplica tipos es NULL
        ) IS 
            SELECT ACT.IDACTUACION,
                ACT.FECHA,
                ACT.FECHAJUSTIFICACION,             
                NVL(FAC_ACT.PRECIOAPLICADO,0) AS IMPORTE_FACTURADO_ACT,
                0 AS COSTEFIJO,
                1 AS FACTURADO
            FROM SCS_ACTUACIONASISTENCIA ACT,
                FCS_FACT_ACTUACIONASISTENCIA FAC_ACT -- FACTURAD0
            WHERE ACT.IDINSTITUCION = V_IDINSTITUCION
                AND ACT.ANIO = V_ANIO
                AND ACT.NUMERO = V_NUMERO 
                AND (V_IDTIPOASISTENCIA IS NULL OR ACT.IDTIPOASISTENCIA = V_IDTIPOASISTENCIA) -- Para tratamiento con tipos 
                AND (V_IDTIPOACTUACION IS NULL OR ACT.IDTIPOACTUACION = V_IDTIPOACTUACION) -- Para tratamiento con tipos              
                AND FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION -- FACTURAD0
                AND FAC_ACT.ANIO = ACT.ANIO -- FACTURAD0
                AND FAC_ACT.NUMERO = ACT.NUMERO -- FACTURAD0
                AND FAC_ACT.IDACTUACION = ACT.IDACTUACION -- FACTURAD0                    
                AND ACT.DIADESPUES = 'N' -- No es fuera de guardia
            UNION ALL -- Da mejor rendimiento UNION ALL q UNION
            SELECT ACT.IDACTUACION,
                ACT.FECHA,
                ACT.FECHAJUSTIFICACION,
                0 AS IMPORTE_FACTURADO_ACT,
                NVL(FUNC_COSTEFIJO(ACT.IDINSTITUCION, ACT.ANIO, ACT.NUMERO, ACT.IDACTUACION, V_IDFACTURACION), 0) AS COSTEFIJO,
                0 AS FACTURADO
            FROM SCS_ACTUACIONASISTENCIA ACT
            WHERE ACT.IDINSTITUCION = V_IDINSTITUCION
                AND ACT.ANIO = V_ANIO
                AND ACT.NUMERO = V_NUMERO
                AND (V_IDTIPOASISTENCIA IS NULL OR ACT.IDTIPOASISTENCIA = V_IDTIPOASISTENCIA) -- Para tratamiento con tipos
                AND (V_IDTIPOACTUACION IS NULL OR ACT.IDTIPOACTUACION = V_IDTIPOACTUACION) -- Para tratamiento con tipos
                AND NOT EXISTS ( -- NO FACTURADO
                    SELECT  1
                    FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                    WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                        AND FAC_ACT.ANIO = ACT.ANIO
                        AND FAC_ACT.NUMERO = ACT.NUMERO
                        AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                AND ACT.VALIDADA = '1' -- Validada
                AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                AND ACT.FECHAJUSTIFICACION IS NOT NULL
                AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA)
                AND ACT.DIADESPUES = 'N'; -- No es fuera de guardia
                
        -- Cursor para obtener las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
        CURSOR CURSOR_ACT_ACFG (
            V_IDINSTITUCION NUMBER, 
            V_IDTURNO NUMBER,
            V_IDGUARDIA NUMBER,
            V_ANIO NUMBER,
            V_NUMERO NUMBER,
            V_FECHAFACTURACIONDESDE DATE,
            V_FECHAFACTURACIONHASTA DATE,
            V_IDTIPOASISTENCIA NUMBER, -- Si no aplica tipos es NULL
            V_IDTIPOACTUACION NUMBER, -- Si no aplica tipos es NULL
            V_CONFIGURACION NUMBER, -- 0:Actual; 1:AcModerna; 2:AcAntigua 
            V_IDFACTURACION_ACTUAL NUMBER
        ) IS 
            SELECT ACT.IDACTUACION,
                ACT.FECHA,
                ACT.FECHAJUSTIFICACION,             
                NVL(FAC_ACT.PRECIOAPLICADO,0) AS IMPORTE_FACTURADO_ACT,
                NULL AS IDFACTURACION,
                1 AS FACTURADO,
                0 AS COSTEFIJO
            FROM SCS_ACTUACIONASISTENCIA ACT,
                FCS_FACT_ACTUACIONASISTENCIA FAC_ACT -- FACTURAD0
            WHERE ACT.IDINSTITUCION = V_IDINSTITUCION
                AND ACT.ANIO = V_ANIO
                AND ACT.NUMERO = V_NUMERO 
                AND (V_IDTIPOASISTENCIA IS NULL OR ACT.IDTIPOASISTENCIA = V_IDTIPOASISTENCIA) -- Para tratamiento con tipos 
                AND (V_IDTIPOACTUACION IS NULL OR ACT.IDTIPOACTUACION = V_IDTIPOACTUACION) -- Para tratamiento con tipos              
                AND FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION -- FACTURAD0
                AND FAC_ACT.ANIO = ACT.ANIO -- FACTURAD0
                AND FAC_ACT.NUMERO = ACT.NUMERO -- FACTURAD0
                AND FAC_ACT.IDACTUACION = ACT.IDACTUACION -- FACTURAD0                    
                AND ACT.DIADESPUES = 'S' -- Fuera de guardia
            UNION ALL -- Da mejor rendimiento UNION ALL q UNION
            SELECT DATOS.IDACTUACION,
                DATOS.FECHA,
                DATOS.FECHAJUSTIFICACION,
                DATOS.IMPORTE_FACTURADO_ACT,
                DATOS.IDFACTURACION,
                DATOS.FACTURADO,
                NVL(FUNC_COSTEFIJO(V_IDINSTITUCION, V_ANIO, V_NUMERO, DATOS.IDACTUACION, DATOS.IDFACTURACION), 0) AS COSTEFIJO
            FROM (
                SELECT ACT.IDACTUACION,
                    ACT.FECHA,
                    ACT.FECHAJUSTIFICACION,
                    0 AS IMPORTE_FACTURADO_ACT,
                    NVL(DECODE(V_CONFIGURACION, 0, NULL, FUNC_OBTENER_IDFACTURACION(ACT.IDINSTITUCION, V_IDTURNO, V_IDGUARDIA, ACT.FECHA)), V_IDFACTURACION_ACTUAL) AS IDFACTURACION,
                    0 AS FACTURADO
                FROM SCS_ACTUACIONASISTENCIA ACT
                WHERE ACT.IDINSTITUCION = V_IDINSTITUCION
                    AND ACT.ANIO = V_ANIO
                    AND ACT.NUMERO = V_NUMERO
                    AND (V_IDTIPOASISTENCIA IS NULL OR ACT.IDTIPOASISTENCIA = V_IDTIPOASISTENCIA) -- Para tratamiento con tipos
                    AND (V_IDTIPOACTUACION IS NULL OR ACT.IDTIPOACTUACION = V_IDTIPOACTUACION) -- Para tratamiento con tipos
                    AND NOT EXISTS ( -- NO FACTURADO
                        SELECT  1
                        FROM FCS_FACT_ACTUACIONASISTENCIA FAC_ACT
                        WHERE FAC_ACT.IDINSTITUCION = ACT.IDINSTITUCION
                            AND FAC_ACT.ANIO = ACT.ANIO
                            AND FAC_ACT.NUMERO = ACT.NUMERO
                            AND FAC_ACT.IDACTUACION = ACT.IDACTUACION)
                    AND ACT.VALIDADA = '1' -- Validada
                    AND NVL(ACT.ANULACION, '0') = '0' -- NO anulada
                    AND ACT.FECHAJUSTIFICACION IS NOT NULL
                    AND TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(V_FECHAFACTURACIONDESDE) AND TRUNC(V_FECHAFACTURACIONHASTA)
                    AND ACT.DIADESPUES = 'S' -- Fuera de guardia
                ) DATOS
                ORDER BY 5 DESC;  -- JPT: Se ordena IDFACTURACION DESC para que salgan primero los actuales                
                
  /****************************************************************************************************************
    Nombre: FUN_DESCRIPCION_IDHITO
    Descripcion: Obtiene la descripcion del hito

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_IDHITO - IN - Identificador del hito - NUMBER

    Versiones (Fecha - Autor - Datos):    
    - 1.0 - 28/04/2006 - Raul Gonzalez Gonzalez
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUN_DESCRIPCION_IDHITO(
        P_IDHITO IN SCS_HITOFACTURABLE.IDHITO%TYPE
    ) RETURN SCS_HITOFACTURABLE.NOMBRE%TYPE IS

        V_SALIDA SCS_HITOFACTURABLE.NOMBRE%TYPE;

    BEGIN

        SELECT NOMBRE
            INTO V_SALIDA
        FROM SCS_HITOFACTURABLE
        WHERE IDHITO = P_IDHITO;

        IF (V_SALIDA IS NULL) THEN
            RETURN('');
        ELSE
            RETURN(V_SALIDA);
        END IF;

        EXCEPTION
            WHEN OTHERS THEN
                RETURN('');
    END FUN_DESCRIPCION_IDHITO;                
                
  /****************************************************************************************************************
    Nombre: FUNC_CONSEJO_COLEGIO
    Descripcion: Obtiene el consejo del colegio

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUNC_CONSEJO_COLEGIO (
        P_IDINSTITUCION IN CEN_INSTITUCION.IDINSTITUCION%TYPE
    ) RETURN NUMBER IS
    
        V_CONSEJO CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE;
        
    BEGIN
        SELECT CEN_INST_IDINSTITUCION
            INTO V_CONSEJO
        FROM CEN_INSTITUCION
        WHERE IDINSTITUCION = P_IDINSTITUCION;
        
        RETURN V_CONSEJO;
    END FUNC_CONSEJO_COLEGIO;                

  /****************************************************************************************************************
    Nombre: FUNC_OBTENER_IDFACTURACION
    Descripcion: Funcion que busca el IDFACTURACION de una cabecera de guardia (incluye regularizaciones)

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_FECHA - IN - Fecha donde buscar la facturacion - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUNC_OBTENER_IDFACTURACION(
        P_IDINSTITUCION IN FCS_HISTORICO_HITOFACT.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_HISTORICO_HITOFACT.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_HISTORICO_HITOFACT.IDGUARDIA%TYPE,
        P_FECHA IN FCS_FACTURACIONJG.FECHADESDE%TYPE
    ) RETURN NUMBER IS

        V_IDFACTURACION FCS_FACT_APUNTE.IDFACTURACION%TYPE := NULL;

    BEGIN
        BEGIN
            -- JPT:  Obtiene el mayor de los IDFACTURACION que se han regularizado                   
            SELECT MAX(FAC.IDFACTURACION)
                INTO V_IDFACTURACION
            FROM  FCS_FACTURACIONJG FAC
            WHERE FAC.IDINSTITUCION = P_IDINSTITUCION
                AND P_FECHA BETWEEN FAC.FECHADESDE AND FAC.FECHAHASTA
                AND FAC.REGULARIZACION = 1 -- JPT: Indica que es una facturacion de regularizacion
                AND EXISTS (
                    SELECT 1
                    FROM FCS_FACT_GRUPOFACT_HITO GF
                    WHERE GF.IDINSTITUCION = FAC.IDINSTITUCION
                        AND GF.IDFACTURACION = FAC.IDFACTURACION
                        AND GF.IDHITOGENERAL = PKG_SIGA_CONSTANTES.HITO_GENERAL_GUARDIA
                )
                AND EXISTS (
                    SELECT 1
                    FROM FCS_HISTORICO_HITOFACT HIST
                    WHERE HIST.IDINSTITUCION = FAC.IDINSTITUCION
                        AND HIST.IDFACTURACION = FAC.IDFACTURACION
                        AND HIST.IDTURNO = P_IDTURNO
                        AND HIST.IDGUARDIA = P_IDGUARDIA
                );                     
                    
            EXCEPTION
              WHEN OTHERS THEN
                V_IDFACTURACION := NULL;
        END;

        IF (V_IDFACTURACION IS NULL) THEN
            BEGIN
                /* JPT: En caso de no haber regularizaciones, obtiene el IDFACTURACION inicial
                    - Puede no tener FCS_FACT_APUNTE, si se hizo una facturacion sin las cabeceras de guardias validadas ... y luego se validaron y se pusieron en una facturacion futura*/
                SELECT MIN(FAC.IDFACTURACION)
                    INTO V_IDFACTURACION
                FROM  FCS_FACTURACIONJG FAC
                WHERE FAC.IDINSTITUCION = P_IDINSTITUCION
                    AND P_FECHA BETWEEN FAC.FECHADESDE AND FAC.FECHAHASTA
                    AND EXISTS (
                        SELECT 1
                        FROM FCS_FACT_GRUPOFACT_HITO GF
                        WHERE GF.IDINSTITUCION = FAC.IDINSTITUCION
                            AND GF.IDFACTURACION = FAC.IDFACTURACION
                            AND GF.IDHITOGENERAL = PKG_SIGA_CONSTANTES.HITO_GENERAL_GUARDIA
                    )
                    AND EXISTS (
                        SELECT 1
                        FROM FCS_HISTORICO_HITOFACT HIST
                        WHERE HIST.IDINSTITUCION = FAC.IDINSTITUCION
                            AND HIST.IDFACTURACION = FAC.IDFACTURACION
                            AND HIST.IDTURNO = P_IDTURNO
                            AND HIST.IDGUARDIA = P_IDGUARDIA
                );

                EXCEPTION
                  WHEN OTHERS THEN
                    V_IDFACTURACION := NULL;
            END;
        END IF;

        RETURN V_IDFACTURACION;

        EXCEPTION
            WHEN OTHERS THEN
                RETURN NULL;
    END FUNC_OBTENER_IDFACTURACION;
    
    /****************************************************************************************************************
    Nombre: FUNC_ESDIAAPLICABLE
    Descripcion: Indica si tiene marcada la fecha como aplicable

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_FECHA - IN - Fecha a confirmar - DATE
    - P_DIASAPLICABLES - IN - Dias aplicables 'LMXJVSD' - VARCHAR2(7)

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/
    FUNCTION FUNC_ESDIAAPLICABLE(
        P_FECHA DATE, 
        P_DIASAPLICABLES VARCHAR2
    ) RETURN NUMBER IS
    
        V_RETORNO NUMBER;

    BEGIN
        BEGIN
            SELECT 1
                INTO V_RETORNO
            FROM GEN_DIASLETRA
            WHERE INSTR(P_DIASAPLICABLES, DIALETRA) <> 0
                AND DIANUMERO = TO_CHAR(P_FECHA, 'D');

        EXCEPTION
          WHEN OTHERS THEN
            V_RETORNO := 0;
        END;
    
        RETURN V_RETORNO;
    END FUNC_ESDIAAPLICABLE;    

  /****************************************************************************************************************
    Nombre: FUNC_COSTEFIJO
    Descripcion: Funcion que nos devuelve el coste fijo de una actuacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_IDACTUACION - IN - Identificador de la actuacion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 24/04/2006 - Pilar Duran Munoz
    - 2.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 3.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    FUNCTION FUNC_COSTEFIJO(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO IN SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO IN SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION IN SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE,
        P_IDFACTURACION IN FCS_HISTO_TIPOACTCOSTEFIJO.IDFACTURACION%TYPE
    ) RETURN NUMBER IS

        V_COSTEFIJO FCS_HISTO_TIPOACTCOSTEFIJO.IMPORTE%TYPE := NULL;
        V_TIENEHISTORICO BOOLEAN := FALSE;

    BEGIN
        BEGIN
            SELECT HTACT.IMPORTE
                INTO V_COSTEFIJO
            FROM SCS_ACTUACIONASISTCOSTEFIJO ACTCF,
                FCS_HISTO_TIPOACTCOSTEFIJO  HTACT
            WHERE HTACT.IDINSTITUCION = ACTCF.IDINSTITUCION
                AND HTACT.IDTIPOASISTENCIA = ACTCF.IDTIPOASISTENCIA
                AND HTACT.IDTIPOACTUACION = ACTCF.IDTIPOACTUACION
                AND HTACT.IDCOSTEFIJO = ACTCF.IDCOSTEFIJO
                AND HTACT.IDFACTURACION = P_IDFACTURACION
                AND ACTCF.IDINSTITUCION = P_IDINSTITUCION
                AND ACTCF.ANIO = P_ANIO
                AND ACTCF.NUMERO = P_NUMERO
                AND ACTCF.IDACTUACION = P_IDACTUACION;

                V_TIENEHISTORICO := TRUE;

            EXCEPTION
                WHEN OTHERS THEN
                    V_TIENEHISTORICO := FALSE;
        END;

        -- Si no obtenemos un idFacturacion o no tenia historico, hay que obtener el coste fijo del tipo de actuacion actual
        IF (V_TIENEHISTORICO = FALSE) THEN
            SELECT TACT.IMPORTE
                INTO V_COSTEFIJO
            FROM SCS_ACTUACIONASISTCOSTEFIJO ACTCF,
                SCS_TIPOACTUACIONCOSTEFIJO  TACT
            WHERE TACT.IDINSTITUCION = ACTCF.IDINSTITUCION
                AND TACT.IDTIPOASISTENCIA = ACTCF.IDTIPOASISTENCIA
                AND TACT.IDTIPOACTUACION = ACTCF.IDTIPOACTUACION
                AND TACT.IDCOSTEFIJO = ACTCF.IDCOSTEFIJO
                AND ACTCF.IDINSTITUCION = P_IDINSTITUCION
                AND ACTCF.ANIO = P_ANIO
                AND ACTCF.NUMERO = P_NUMERO
                AND ACTCF.IDACTUACION = P_IDACTUACION;
        END IF;

        RETURN V_COSTEFIJO;

        EXCEPTION
            WHEN OTHERS THEN
                RETURN 0;
    END FUNC_COSTEFIJO;                    
                
    --INI: Cambio para Asistencias que derivan en Designacion
  /****************************************************************************************************************
    Nombre: F_ES_ASISTENCIA_DERIVADA_GIJON
    Descripcion: Indica si es una asistencia derivada en Gijon

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_FECHADESDE - IN - Fecha desde - DATE
    - P_FECHAHASTA - IN - Fecha hasta - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/    
    FUNCTION F_ES_ASISTENCIA_DERIVADA_GIJON(
        P_IDINSTITUCION IN SCS_ASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO          IN SCS_ASISTENCIA.ANIO%TYPE,
        P_NUMERO        IN SCS_ASISTENCIA.NUMERO%TYPE,
        P_FECHADESDE    IN SCS_DESIGNA.FECHAENTRADA%TYPE,
        P_FECHAHASTA    IN SCS_DESIGNA.FECHAENTRADA%TYPE
    ) RETURN NUMBER IS

        C_ESTADO_ASISTENCIA_ANULADA  CONSTANT SCS_ASISTENCIA.IDESTADOASISTENCIA%TYPE := 2;
        C_ESTADO_DESIGNACION_ANULADA CONSTANT SCS_DESIGNA.ESTADO%TYPE := 'A';

        V_ESDERIVADA NUMBER;

    BEGIN
        SELECT COUNT(1)
            INTO V_ESDERIVADA
        FROM SCS_ASISTENCIA ASI, 
            SCS_ACTUACIONASISTENCIA ACT
        WHERE ASI.IDINSTITUCION = ACT.IDINSTITUCION
            AND ASI.ANIO = ACT.ANIO
            AND ASI.NUMERO = ACT.NUMERO
            AND ASI.IDINSTITUCION = P_IDINSTITUCION
            AND ASI.ANIO = P_ANIO
            AND ASI.NUMERO = P_NUMERO
            AND TRUNC(ACT.FECHAJUSTIFICACION) <= P_FECHAHASTA -- Justificada
            AND ASI.IDESTADOASISTENCIA <> C_ESTADO_ASISTENCIA_ANULADA -- No anulada
            AND (
                EXISTS ( -- Tiene una designacion directa
                    SELECT 1
                    FROM SCS_DESIGNA DES
                    WHERE ASI.IDINSTITUCION = DES.IDINSTITUCION
                        AND ASI.DESIGNA_TURNO = DES.IDTURNO
                        AND ASI.DESIGNA_ANIO = DES.ANIO
                        AND ASI.DESIGNA_NUMERO = DES.NUMERO
                        AND DES.ESTADO <> C_ESTADO_DESIGNACION_ANULADA -- No anulada
                        AND TRUNC(DES.FECHAENTRADA) BETWEEN P_FECHADESDE AND P_FECHAHASTA -- En el periodo de facturacion
                        AND ( 
                            NOT EXISTS( -- Sin actuaciones de designacion
                                SELECT 1
                                FROM SCS_ACTUACIONDESIGNA ACTDES
                                WHERE ACTDES.IDINSTITUCION = DES.IDINSTITUCION
                                    AND ACTDES.IDTURNO = DES.IDTURNO
                                    AND ACTDES.ANIO = DES.ANIO
                                    AND ACTDES.NUMERO = DES.NUMERO
                                    AND TRUNC(ACTDES.FECHAJUSTIFICACION) BETWEEN P_FECHADESDE AND P_FECHAHASTA -- Justificada dentro del periodo de facturacion
                            )
                            -- o con actuaciones de designacion que no sean:
                            -- ni "Asistencia a la comparecencia de la orden de proteccion"
                            -- ni "Juicio de faltas"
                            OR EXISTS( 
                                SELECT 1
                                FROM SCS_ACTUACIONDESIGNA ACTDES
                                WHERE ACTDES.IDINSTITUCION = DES.IDINSTITUCION
                                    AND ACTDES.IDTURNO = DES.IDTURNO
                                    AND ACTDES.ANIO = DES.ANIO
                                    AND ACTDES.NUMERO = DES.NUMERO
                                    AND TRUNC(ACTDES.FECHAJUSTIFICACION) BETWEEN P_FECHADESDE AND P_FECHAHASTA -- Justificada dentro del periodo de facturacion
                                    AND ACTDES.IDPROCEDIMIENTO NOT IN (20, 90, 21, 108)
                            )
                        )
                )
                OR EXISTS ( -- Tiene una designacion indirecta (a traves de EJG)
                    SELECT 1
                    FROM SCS_EJGDESIGNA EJGDES, 
                        SCS_DESIGNA DES
                    WHERE EJGDES.IDINSTITUCION = DES.IDINSTITUCION
                        AND EJGDES.IDTURNO = DES.IDTURNO
                        AND EJGDES.ANIODESIGNA = DES.ANIO
                        AND EJGDES.NUMERODESIGNA = DES.NUMERO
                        AND ASI.IDINSTITUCION = EJGDES.IDINSTITUCION
                        AND ASI.EJGIDTIPOEJG = EJGDES.IDTIPOEJG
                        AND ASI.EJGANIO = EJGDES.ANIOEJG
                        AND ASI.EJGNUMERO = EJGDES.NUMEROEJG
                        AND DES.ESTADO <> C_ESTADO_DESIGNACION_ANULADA -- No anulada
                        AND TRUNC(DES.FECHAENTRADA) BETWEEN P_FECHADESDE AND P_FECHAHASTA -- En el periodo de facturacion
                        AND ( 
                            NOT EXISTS( -- Sin actuaciones de designacion
                                SELECT 1
                                FROM SCS_ACTUACIONDESIGNA ACTDES
                                WHERE ACTDES.IDINSTITUCION = DES.IDINSTITUCION
                                    AND ACTDES.IDTURNO = DES.IDTURNO
                                    AND ACTDES.ANIO = DES.ANIO
                                    AND ACTDES.NUMERO = DES.NUMERO
                                    AND TRUNC(ACTDES.FECHAJUSTIFICACION) BETWEEN P_FECHADESDE AND P_FECHAHASTA -- Justificada dentro del periodo de facturacion
                            )
                            -- o con actuaciones de designacion que no sean:
                            -- ni "Asistencia a la comparecencia de la orden de proteccion"
                            -- ni "Juicio de faltas"
                            OR EXISTS( 
                                SELECT 1
                                FROM SCS_ACTUACIONDESIGNA ACTDES
                                WHERE ACTDES.IDINSTITUCION = DES.IDINSTITUCION
                                    AND ACTDES.IDTURNO = DES.IDTURNO
                                    AND ACTDES.ANIO = DES.ANIO
                                    AND ACTDES.NUMERO = DES.NUMERO
                                    AND TRUNC(ACTDES.FECHAJUSTIFICACION) BETWEEN P_FECHADESDE AND P_FECHAHASTA -- Justificada dentro del periodo de facturacion
                                    AND ACTDES.IDPROCEDIMIENTO NOT IN (20, 90, 21, 108)
                            )
                        )                    
                )
            );

        IF V_ESDERIVADA > 0 THEN
            RETURN PKG_SIGA_CONSTANTES.DB_TRUE_N;
        ELSE
            RETURN PKG_SIGA_CONSTANTES.DB_FALSE_N;
        END IF;
    END F_ES_ASISTENCIA_DERIVADA_GIJON;

  /****************************************************************************************************************
    Nombre: F_ES_ASISTENCIA_DERIVADA_CR
    Descripcion: Indica si es una asistencia derivada en Ciudad Real

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_FECHAHASTA - IN - Fecha hasta - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/ 
    FUNCTION F_ES_ASISTENCIA_DERIVADA_CR(
        P_IDINSTITUCION IN SCS_ASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO          IN SCS_ASISTENCIA.ANIO%TYPE,
        P_NUMERO        IN SCS_ASISTENCIA.NUMERO%TYPE,
        P_FECHAHASTA    IN SCS_DESIGNA.FECHAENTRADA%TYPE
    ) RETURN NUMBER IS

        C_ESTADO_ASISTENCIA_ANULADA  CONSTANT SCS_ASISTENCIA.IDESTADOASISTENCIA%TYPE := 2;
        C_ESTADO_DESIGNACION_ANULADA CONSTANT SCS_DESIGNA.ESTADO%TYPE := 'A';

        V_ESDERIVADA NUMBER;

    BEGIN
        SELECT COUNT(1)
            INTO V_ESDERIVADA
        FROM SCS_ASISTENCIA ASI, 
            SCS_ACTUACIONASISTENCIA ACT
        WHERE ASI.IDINSTITUCION = ACT.IDINSTITUCION
            AND ASI.ANIO = ACT.ANIO
            AND ASI.NUMERO = ACT.NUMERO
            AND ASI.IDINSTITUCION = P_IDINSTITUCION
            AND ASI.ANIO = P_ANIO
            AND ASI.NUMERO = P_NUMERO
            AND TRUNC(ACT.FECHAJUSTIFICACION) <= P_FECHAHASTA -- Justificado
            AND ASI.IDESTADOASISTENCIA <> C_ESTADO_ASISTENCIA_ANULADA -- Sin anular          
            AND F_ES_TIPO_DERIVACION( -- Es de tipo derivacion 
                    ASI.IDINSTITUCION,
                    ASI.ANIO,
                    ASI.NUMERO,
                    NULL
                ) = PKG_SIGA_CONSTANTES.DB_TRUE_N
            AND (
                EXISTS( -- Tiene una designacion directa
                    SELECT 1
                    FROM SCS_DESIGNA DES
                    WHERE ASI.IDINSTITUCION = DES.IDINSTITUCION
                        AND ASI.DESIGNA_TURNO = DES.IDTURNO
                        AND ASI.DESIGNA_ANIO = DES.ANIO
                        AND ASI.DESIGNA_NUMERO = DES.NUMERO
                        AND DES.ESTADO <> C_ESTADO_DESIGNACION_ANULADA -- Sin anular
                )
                OR EXISTS ( -- Tiene una designacion indirecta (a traves de EJG)
                    SELECT 1
                    FROM SCS_EJGDESIGNA EJGDES, 
                        SCS_DESIGNA DES
                    WHERE EJGDES.IDINSTITUCION = DES.IDINSTITUCION
                        AND EJGDES.IDTURNO = DES.IDTURNO
                        AND EJGDES.ANIODESIGNA = DES.ANIO
                        AND EJGDES.NUMERODESIGNA = DES.NUMERO
                        AND ASI.IDINSTITUCION = EJGDES.IDINSTITUCION
                        AND ASI.EJGIDTIPOEJG = EJGDES.IDTIPOEJG
                        AND ASI.EJGANIO = EJGDES.ANIOEJG
                        AND ASI.EJGNUMERO = EJGDES.NUMEROEJG
                        AND DES.ESTADO <> C_ESTADO_DESIGNACION_ANULADA -- Sin anular
                )
            );

        IF V_ESDERIVADA > 0 THEN
            RETURN PKG_SIGA_CONSTANTES.DB_TRUE_N;
        ELSE
            RETURN PKG_SIGA_CONSTANTES.DB_FALSE_N;
        END IF;
    END F_ES_ASISTENCIA_DERIVADA_CR;  

  /****************************************************************************************************************
    Nombre: F_ES_ASISTENCIA_DERIVADA
    Descripcion: Indica si es una asistencia derivada

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_FECHADESDE - IN - Fecha desde - DATE
    - P_FECHAHASTA - IN - Fecha hasta - DATE

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/ 
    FUNCTION F_ES_ASISTENCIA_DERIVADA(
        P_IDINSTITUCION IN SCS_ASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO          IN SCS_ASISTENCIA.ANIO%TYPE,
        P_NUMERO        IN SCS_ASISTENCIA.NUMERO%TYPE,
        P_FECHADESDE    IN SCS_DESIGNA.FECHAENTRADA%TYPE,
        P_FECHAHASTA    IN SCS_DESIGNA.FECHAENTRADA%TYPE
    ) RETURN NUMBER IS

    BEGIN
        IF P_IDINSTITUCION = C_ID_GIJON THEN
            RETURN F_ES_ASISTENCIA_DERIVADA_GIJON(
                P_IDINSTITUCION,
                P_ANIO,
                P_NUMERO,
                P_FECHADESDE,
                P_FECHAHASTA);
        ELSIF P_IDINSTITUCION IN (C_ID_ALBACETE, C_ID_CIUDAD_REAL, C_ID_LA_RIOJA, C_ID_VALLADOLID) THEN
            RETURN F_ES_ASISTENCIA_DERIVADA_CR(
                P_IDINSTITUCION,
                P_ANIO,
                P_NUMERO,
                P_FECHAHASTA);
        ELSE
            RETURN PKG_SIGA_CONSTANTES.DB_FALSE_N;
        END IF;
    END F_ES_ASISTENCIA_DERIVADA;

  /****************************************************************************************************************
    Nombre: F_ES_TIPO_DERIVACION
    Descripcion: Indica el tipo de derivada

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_ANIO - IN - Ano de la asistencia - NUMBER
    - P_NUMERO - IN - Numero de la asistencia - NUMBER
    - P_IDACTUACION - IN - Identificador de la actuacion - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/ 
    FUNCTION F_ES_TIPO_DERIVACION(
        P_IDINSTITUCION SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_ANIO          SCS_ACTUACIONASISTENCIA.ANIO%TYPE,
        P_NUMERO        SCS_ACTUACIONASISTENCIA.NUMERO%TYPE,
        P_IDACTUACION   SCS_ACTUACIONASISTENCIA.IDACTUACION%TYPE
    ) RETURN NUMBER IS

        C_TIPOACTUACION_JUDICIAL CONSTANT SCS_ACTUACIONASISTENCIA.IDTIPOACTUACION%TYPE := 2;
        C_TIPOASISTENCIA_JR      CONSTANT SCS_ASISTENCIA.IDTIPOASISTENCIACOLEGIO%TYPE := 3;
        V_IDTIPO_ACTUACION  SCS_ACTUACIONASISTENCIA.IDTIPOACTUACION%TYPE;
        V_IDTIPO_ASISTENCIA SCS_ASISTENCIA.IDTIPOASISTENCIACOLEGIO%TYPE;

    BEGIN
        IF P_IDINSTITUCION = C_ID_GIJON THEN
            SELECT IDTIPOACTUACION
                INTO V_IDTIPO_ACTUACION
            FROM SCS_ACTUACIONASISTENCIA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND ANIO = P_ANIO
                AND NUMERO = P_NUMERO
                AND IDACTUACION = P_IDACTUACION;

            IF V_IDTIPO_ACTUACION = C_TIPOACTUACION_JUDICIAL THEN
                RETURN PKG_SIGA_CONSTANTES.DB_TRUE_N;
            ELSE
                RETURN PKG_SIGA_CONSTANTES.DB_FALSE_N;
            END IF;

        ELSIF P_IDINSTITUCION IN (C_ID_ALBACETE, C_ID_CIUDAD_REAL, C_ID_LA_RIOJA, C_ID_VALLADOLID) THEN
            SELECT IDTIPOASISTENCIACOLEGIO
                INTO V_IDTIPO_ASISTENCIA
            FROM SCS_ASISTENCIA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND ANIO = P_ANIO
                AND NUMERO = P_NUMERO;

            IF V_IDTIPO_ASISTENCIA = C_TIPOASISTENCIA_JR THEN
                RETURN PKG_SIGA_CONSTANTES.DB_TRUE_N;
            ELSE
                RETURN PKG_SIGA_CONSTANTES.DB_FALSE_N;
            END IF;

        ELSE
            RETURN PKG_SIGA_CONSTANTES.DB_FALSE_N;
        END IF;
    END F_ES_TIPO_DERIVACION;
    --FIN: Cambio para Asistencias que derivan en Designacion
    
  /****************************************************************************************************************
    Nombre: PROC_FCS_CARGA_FACTURACION
    Descripcion: Carga el RECORD V_DATOS_FACTURACION con los datos de la facturacion

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_DATOSFACTURACION - IN OUT - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 19/04/2006 - Pilar Duran Munoz
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FCS_CARGA_FACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_DATOSFACTURACION IN OUT DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2
    ) IS

    BEGIN
        P_DATOSERROR := 'PROCEDURE PROC_FCS_CARGA_FACTURACION: Inicio';
    
        SELECT 
            IDINSTITUCION,
            FECHADESDE,
            FECHAHASTA
        INTO
            P_DATOSFACTURACION.IDINSTITUCION,
            P_DATOSFACTURACION.FECHADESDE,
            P_DATOSFACTURACION.FECHAHASTA                
        FROM FCS_FACTURACIONJG
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDFACTURACION = P_IDFACTURACION;
            
        -- Obtiene el IdApunte de la facturacion
        SELECT NVL(MAX(IDAPUNTE),0) + 1
            INTO P_DATOSFACTURACION.IDAPUNTE
        FROM FCS_FACT_APUNTE
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDFACTURACION = P_IDFACTURACION;
            
        -- JPT: Busco si el consejo del colegio
        P_DATOSFACTURACION.CONSEJO := FUNC_CONSEJO_COLEGIO(P_IDINSTITUCION);                           

        P_DATOSERROR := 'PROCEDURE PROC_FCS_CARGA_FACTURACION: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_CARGA_FACTURACION;
  
  /****************************************************************************************************************
    Nombre: PROC_FCS_CARGA_FACTURACION
    Descripcion: Carga el RECORD V_CONFIG_GUARDIA con los datos iniciales

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_CONFIG_GUARDIA - OUT - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 20/04/2006 - Pilar Duran Munoz
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/    
    PROCEDURE PROC_FCS_INI_CONFIG_GUARDIA (
        P_CONFIG_GUARDIA OUT CONFIG_GUARDIA, --V_CONFIG_GUARDIA
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

    BEGIN
        /* Inicializamos los datos de la matriz de configuracion de Guardias*/
        P_CONFIG_GUARDIA.IMPORTESOJ := 0;
        P_CONFIG_GUARDIA.IMPORTEEJG := 0;
           
        -- Configuracion inicial paga guardia
        P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DIAS := ''; -- Los dias ('LMXJVSD'), lo usamos para saber si se factura por este tipo de facturacion
        P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTE := 0; -- Hay que inicializarlo porque igual no tiene valor
        P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DOBLA := 0; -- Hay que inicializarlo porque igual no tiene valor
        P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTEDOBLA := 0; -- Hay que inicializarlo porque igual no tiene valor
                
        -- Configuracion inicial no paga guardia
        P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS := ''; -- Los dias ('LMXJVSD'), lo usamos para saber si se factura por este tipo de facturacion
        P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.IMPORTE := 0; -- Hay que inicializarlo porque igual no tiene valor
        P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MINIMO := 0; -- Hay que inicializarlo porque igual no tiene valor
        P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAXIMO := 0;  -- Hay que inicializarlo porque igual no tiene valor
        
        -- Configuracion inicial fuera de guardia (por actuaciones)
        P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO := NULL; -- Sirve para saber si acepta Fuera de Guardia
        P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.IMPORTE := 0; -- Hay que inicializarlo porque igual no tiene valor
        P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MAXIMO := 0;  -- Hay que inicializarlo porque igual no tiene valor               

        P_DATOSERROR := 'PROCEDURE PROC_FCS_INI_CONFIG_GUARDIA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_INI_CONFIG_GUARDIA;

    /****************************************************************************************************************
        Nombre: PROC_FCS_CARGA_CONFIG_GUARDIA
        Descripcion: Este procedimiento se encarga de inicializar la matriz de configuracion de guardias

        Parametros:
        - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
        - P_IDTURNO - IN - Identificador del turno - NUMBER
        - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
        - P_IDTIPOGUARDIA - IN - Identificador del tipo de guardia - NUMBER(2)
        - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
        - P_CONFIG_GUARDIA - OUT - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
        - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
            En caso de error devuelve el codigo de error Oracle correspondiente.
        - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
            En caso de error devuelve el mensaje de error Oracle correspondiente.

        Versiones:
            - 1.0 - 20/04/2006  - Autor: Pilar Duran Munoz
            - 2.0 - 26/02/2016  - Autor: Jorge Paez Trivino
                Cambios realizados para facturacion de colegios catalanes, de cabeceras de guardias facturadas, con la configuracion de la primera facturacion
            - 3.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_CARGA_CONFIG_GUARDIA (
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDTIPOGUARDIA IN SCS_GUARDIASTURNO.IDTIPOGUARDIA%TYPE,            
        P_IDFACTURACION IN FCS_FACT_APUNTE.IDFACTURACION%TYPE,
        P_CONFIG_GUARDIA OUT CONFIG_GUARDIA, --V_CONFIG_GUARDIA
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

    BEGIN
        P_DATOSERROR:= 'PROC_FCS_CARGA_CONFIG_GUARDIA: Llamada a PROC_FCS_INI_CONFIG_GUARDIA';
        -- Inicializamos el RECORD de Configuracion de Guardias
        PROC_FCS_INI_CONFIG_GUARDIA(P_CONFIG_GUARDIA, P_CODRETORNO, P_DATOSERROR);
        IF (P_CODRETORNO <> '0') THEN
            RAISE E_ERROR2;
        END IF;
        
        --INI: Cambio facturacion guardias inactivas catalanes de VG --            
        P_CONFIG_GUARDIA.ESGUARDIAVG := P_IDTIPOGUARDIA;
        --FIN: Cambio facturacion guardias inactivas catalanes de VG --
        
        -- JPT: Indico que esta configuracion corresponde a un facturacion
        P_CONFIG_GUARDIA.IDFACTURACION := P_IDFACTURACION;          

        P_DATOSERROR := 'PROC_FCS_CARGA_CONFIG_GUARDIA: Cursor C_IDHITO';
        -- Para cada Hito
        -- Contienen DIASAPLICABLES y AGRUPAR = 1:GAs; 44:GAc; 5:As; 7:Ac
        FOR V_IDHITO IN CURSOR_CONFIG_FACTURACION (
            P_IDINSTITUCION, 
            P_IDTURNO, 
            P_IDGUARDIA,
            P_IDFACTURACION
        ) LOOP

            -- Actualizamos el RECORD de Configuracion de Guardias
            CASE (V_IDHITO.IDHITO)
                WHEN C_GAs THEN -- 1
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTE := V_IDHITO.PRECIO;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DIAS := V_IDHITO.DIASAPLICABLES; 
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MAX_MIN_UG_CG := V_IDHITO.AGRUPAR;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO := C_GAs;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO := C_GAsMas;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_GDAs;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_GDAsMas;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MINIMONOFACTURADO := C_GAsMin;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MINIMOFACTURADO := C_GAsMinMas;

                WHEN C_GDAs THEN -- 2
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTEDOBLA := V_IDHITO.PRECIO;                                         

                WHEN C_AsMax THEN -- 3
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAXIMO := V_IDHITO.PRECIO;

                WHEN C_GDAc THEN -- 4
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTEDOBLA := V_IDHITO.PRECIO;

                WHEN C_As THEN -- 5
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.IMPORTE := V_IDHITO.PRECIO;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS := V_IDHITO.DIASAPLICABLES; 
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAX_MIN_UG_CG := V_IDHITO.AGRUPAR;                  
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO := C_As;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO := C_AsMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_AsMax;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_AsMaxMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMONOFACTURADO := C_AsMin;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMOFACTURADO := C_AsMinMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO := NULL; -- Es importante para indicar que no aplica tipos

                WHEN C_AcFGMax THEN -- 6
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MAXIMO := V_IDHITO.PRECIO;

                WHEN C_Ac THEN -- 7
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.IMPORTE := V_IDHITO.PRECIO;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS := V_IDHITO.DIASAPLICABLES; 
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAX_MIN_UG_CG := V_IDHITO.AGRUPAR;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO := C_Ac;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO := C_AcMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_AcMax;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_AcMaxMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMONOFACTURADO := C_AcMin;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMOFACTURADO := C_AcMinMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO := NULL; -- Es importante para indicar que no aplica tipos 

                WHEN C_AcMax THEN -- 8
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAXIMO := V_IDHITO.PRECIO;

                WHEN C_AcFG THEN -- 9                  
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.IMPORTE := V_IDHITO.PRECIO;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO := C_AcFG;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.FACTURADO := C_AcFGMas;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_AcFGMax;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_AcFGMaxMas;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO_TP.NOFACTURADO := NULL; -- Es importante para indicar que no aplica tipos

                WHEN C_AsMin THEN -- 10
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MINIMO := V_IDHITO.PRECIO;

                WHEN C_SOJ THEN -- 12
                    P_CONFIG_GUARDIA.IMPORTESOJ := V_IDHITO.PRECIO;

                WHEN C_EJG THEN -- 13
                    P_CONFIG_GUARDIA.IMPORTEEJG := V_IDHITO.PRECIO;

               WHEN C_AcMin THEN -- 19
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MINIMO := V_IDHITO.PRECIO;

                WHEN C_AsTp THEN -- 20
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO := C_As;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO := C_AsMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_AsMax;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_AsMaxMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMONOFACTURADO := C_AsMin;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMOFACTURADO := C_AsMinMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO := C_AsTp; --  Es importante para indicar que aplica tipos
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.FACTURADO := C_AsTpMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMONOFACTURADO := C_AsTpMax;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMOFACTURADO := C_AsTpMaxMas;  

                WHEN C_AcTp THEN -- 22
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO := C_Ac;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO := C_AcMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_AcMax;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_AcMaxMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMONOFACTURADO := C_AcMin;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMOFACTURADO := C_AcMinMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO := C_AcTp; --  Es importante para indicar que aplica tipos
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.FACTURADO := C_AcTpMas;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMONOFACTURADO := C_AcTpMax;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMOFACTURADO := C_AcTpMaxMas;

                WHEN C_AcFGTp THEN -- 25
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO := C_AcFG;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.FACTURADO := C_AcFGMas;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_AcFGMax;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_AcFGMaxMas;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO_TP.NOFACTURADO := C_AcFGTp; --  Es importante para indicar que aplica tipos
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO_TP.FACTURADO := C_AcFGTpMas;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO_TP.MAXIMONOFACTURADO := C_AcFGTpMax;
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MOTIVO_TP.MAXIMOFACTURADO := C_AcFGTpMaxMas;

                WHEN C_GAc THEN -- 44
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTE := V_IDHITO.PRECIO;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DIAS := V_IDHITO.DIASAPLICABLES; 
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MAX_MIN_UG_CG := V_IDHITO.AGRUPAR;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO := C_GAc;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO := C_GAcMas;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO := C_GDAc;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMOFACTURADO := C_GDAcMas;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MINIMONOFACTURADO := C_GAcMin;
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MINIMOFACTURADO := C_GAcMinMas;                    
                    
                WHEN C_NDAs THEN -- 45 
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DOBLA := V_IDHITO.PRECIO;
                    
                WHEN C_NDAc THEN -- 46 
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DOBLA := V_IDHITO.PRECIO;
                    
                WHEN C_GAsMin THEN -- 53 
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MINIMO := V_IDHITO.PRECIO;
                    
                WHEN C_GAcMin THEN -- 54 
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MINIMO := V_IDHITO.PRECIO;
                    
                WHEN C_FacConfig THEN -- 61 - 0:Actual; 1:Antiguo
                    P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.CONFIGURACION := V_IDHITO.PRECIO;
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.CONFIGURACION := V_IDHITO.PRECIO;
                    
                WHEN C_FacConfigFg THEN -- 62 - 0:Actual; 1:AcModerna; 2:AcAntigua 
                    P_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.CONFIGURACION := V_IDHITO.PRECIO;

                ELSE
                    P_DATOSERROR := 'PROCEDURE PROC_FCS_CARGA_CONFIG_GUARDIA: No recupera el hito';
            END CASE;
        END LOOP; -- Fin C_CONFIGURACION_FACTURACION

        P_DATOSERROR := 'PROCEDURE PROC_FCS_CARGA_CONFIG_GUARDIA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR2 THEN
                P_CODRETORNO := '-1';
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;        
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
            P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_FCS_CARGA_CONFIG_GUARDIA;

  /****************************************************************************************************************
    Nombre: PROC_CARGA_CONFIG_GUARDIA
    Descripcion: Procedimiento para catalanes que carga V_CONFIG_GUARDIA

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDTIPOGUARDIA - IN - Identificador del tipo de guardia - NUMBER(2)
    - P_CONFIG_GUARDIA - IN OUT - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA    
    - P_CONFIG_GUARDIA_ACTUAL - IN - RECORD que contiene la configuracion actual de la guardia - CONFIG_GUARDIA
    - P_IDFACTURACIONCG - IN - Identificador de la facturacion de la que se quiere obtener la configuracion - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 02/03/2016 - Jorge Paez Trivino - Adaptacion a los colegios catalanes (R1602_0089)
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    PROCEDURE PROC_CARGA_CONFIG_GUARDIA(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDTIPOGUARDIA IN SCS_GUARDIASTURNO.IDTIPOGUARDIA%TYPE,
        P_CONFIG_GUARDIA IN OUT CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_CONFIG_GUARDIA_ACTUAL IN CONFIG_GUARDIA,
        P_IDFACTURACIONCG IN NUMBER, -- DEBE TENER SIEMPRE VALOR (identificador de la CG actual)
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

    BEGIN
        IF (P_IDFACTURACIONCG IS NULL OR P_CONFIG_GUARDIA.IDFACTURACION IS NULL OR P_CONFIG_GUARDIA_ACTUAL.IDFACTURACION IS NULL) THEN
            P_DATOSERROR := 'PROC_CARGA_CONFIG_GUARDIA: No se encuentra el valor de IDFACTURACION';
            RAISE E_ERROR2;
        END IF;

        IF (P_IDFACTURACIONCG = P_CONFIG_GUARDIA_ACTUAL.IDFACTURACION) THEN
            -- JPT: Es una cabecera de guardia, configurado con la guardia actual
            -- Adri: Se trata de una guardia que se factura en el periodo actual de la facturacion y por tanto, se coge la configuracion del periodo actual, nada de historico.
                P_CONFIG_GUARDIA := P_CONFIG_GUARDIA_ACTUAL;

        ELSE -- JPT: Como es una cabecera de guardia ya facturada, hay que obtener la configuracion de la primera facturacion de esa cabecera de guardia
            /* JPT: Cargamos el RECORD de la Configuracion de Guardia FACTURADA (V_CONFIG_GUARDIA)
                - Obtiene los hitos de la facturacion (tabla FCS_HISTORICO_HITOFACT) y carga los datos en V_CONFIG_GUARDIA
                - Para los catalanes hay que consultar SCS_GUARDIASTURNO.ESGUARDIAVG para la facturacion por guardias*/
            P_DATOSERROR:= 'PROC_CARGA_CONFIG_GUARDIA: Llamada a PROC_FCS_CARGA_CONFIG_GUARDIA';
            PROC_FCS_CARGA_CONFIG_GUARDIA(
                P_IDINSTITUCION,
                P_IDTURNO,
                P_IDGUARDIA,
                P_IDTIPOGUARDIA,
                P_IDFACTURACIONCG,
                P_CONFIG_GUARDIA,
                P_CODRETORNO,
                P_DATOSERROR);
            IF (P_CODRETORNO <> '0') THEN
                RAISE E_ERROR2;
            END IF;
        END IF;

        P_DATOSERROR := 'PROC_CARGA_CONFIG_GUARDIA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;
    END PROC_CARGA_CONFIG_GUARDIA;    
    
    /****************************************************************************************************************
    Nombre: PROC_FACT_CONTROL_HITO
    Descripcion: Controla si el hito se ha realizado previamente, en cuyo caso pone importe cero

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de persona - NUMBER    
    - P_FECHAINICIO - IN - Fecha inicial de la unidad de guardia - DATE
    - P_FECHAFIN - IN - Fecha final de la unidad de guardia - DATE
    - P_IDTIPOASISTENCIACOLEGIO - IN - Identificador del tipo de asistencias - NUMBER
    - P_IDTIPOACTUACION - IN - Identificador del tipo de actuacion - NUMBER
    - P_IMPORTE - IN OUT - Importe calculado - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/     
    PROCEDURE PROC_FACT_CONTROL_HITO(
        P_IDINSTITUCION IN FCS_FACT_GUARDIASCOLEGIADO.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_FACT_GUARDIASCOLEGIADO.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_FACT_GUARDIASCOLEGIADO.IDGUARDIA%TYPE,
        P_IDPERSONA IN FCS_FACT_GUARDIASCOLEGIADO.IDPERSONA%TYPE,
        P_FECHAINICIO IN FCS_FACT_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
        P_FECHAFIN IN FCS_FACT_GUARDIASCOLEGIADO.FECHAFIN%TYPE, -- Para CG es NULL
        P_IDTIPOASISTENCIACOLEGIO IN FCS_FACT_GUARDIASCOLEGIADO.IDTIPOASISTENCIACOLEGIO%TYPE, -- Si no aplica tipos es NULL
        P_IDTIPOACTUACION IN FCS_FACT_GUARDIASCOLEGIADO.IDTIPOACTUACION%TYPE, -- Si no aplica tipos es NULL
        P_HITO IN FCS_FACT_GUARDIASCOLEGIADO.IDHITO%TYPE,
        P_HITO_MAS IN FCS_FACT_GUARDIASCOLEGIADO.IDHITO%TYPE,
        P_IMPORTE IN OUT NUMBER        
    ) IS
    
        V_TIENE_HITO NUMBER(1);          
    
    BEGIN       
        IF (P_FECHAFIN IS NOT NULL) THEN -- Vale para TP y UG
            SELECT COUNT(1)
                INTO V_TIENE_HITO
            FROM FCS_FACT_GUARDIASCOLEGIADO FGC
            WHERE FGC.IDINSTITUCION = P_IDINSTITUCION
                AND FGC.IDTURNO = P_IDTURNO
                AND FGC.IDGUARDIA = P_IDGUARDIA
                AND FGC.IDPERSONA = P_IDPERSONA
                AND FGC.FECHAINICIO = P_FECHAINICIO
                AND FGC.FECHAFIN = P_FECHAFIN
                AND (P_IDTIPOASISTENCIACOLEGIO IS NULL OR FGC.IDTIPOASISTENCIACOLEGIO = P_IDTIPOASISTENCIACOLEGIO)
                AND (P_IDTIPOACTUACION IS NULL OR FGC.IDTIPOACTUACION = P_IDTIPOACTUACION)           
                AND FGC.IDHITO IN (P_HITO, P_HITO_MAS);
              
        ELSE -- Vale para CG
          SELECT COUNT(1)
              INTO V_TIENE_HITO
          FROM FCS_FACT_APUNTE
          WHERE IDINSTITUCION = P_IDINSTITUCION
              AND IDTURNO = P_IDTURNO
              AND IDGUARDIA = P_IDGUARDIA
              AND IDPERSONA = P_IDPERSONA
              AND FECHAINICIO = P_FECHAINICIO
              AND IDHITO IN (P_HITO, P_HITO_MAS);
        END IF;        
      
        IF (V_TIENE_HITO > 0) THEN
            P_IMPORTE := 0;
        END IF;  
      
      EXCEPTION
        WHEN OTHERS THEN
            P_IMPORTE := P_IMPORTE;      
    END PROC_FACT_CONTROL_HITO;    
            
  /****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia_Tipo
    Descripcion: Obtiene el importe y el maximo de un tipo de actuacion o asistencia

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTIPOASISTENCIA - IN - Identificador del tipo de asistencia - NUMBER
    - P_IDTIPOACTUACION - IN - Identificador del tipo de actuacion - NUMBER
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_IDFACTURACION - IN OUT - Identificador de la facturacion - NUMBER
    - P_IMPORTE - OUT - Importe del tipo - NUMBER
    - P_MAXIMO - OUT - Importe maximo del tipo - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/
    PROCEDURE PROC_FACT_NOPAGAGUARDIA_TIPO(
        P_IDINSTITUCION IN SCS_ACTUACIONASISTENCIA.IDINSTITUCION%TYPE,
        P_IDTIPOASISTENCIA IN SCS_ACTUACIONASISTENCIA.IDTIPOASISTENCIA%TYPE,
        P_IDTIPOACTUACION IN SCS_ACTUACIONASISTENCIA.IDTIPOACTUACION%TYPE,-- Si no aplica tipos por actuaciones es NULL
        P_CONFIG_NOPAGAGUARDIA IN TIPO_FACTURACION, -- V_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA o CONFIG_FUERAGUARDIA
        P_IDFACTURACION IN FCS_HISTO_TIPOACTCOSTEFIJO.IDFACTURACION%TYPE, -- Si es NULL (solo en Fuera Guardias con cofiguracion no actual) se vuelve a calcular el IDFACTURACION
        P_IMPORTE OUT NUMBER,
        P_MAXIMO OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        V_TIENEHISTORICO BOOLEAN := FALSE;

    BEGIN
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_TIPO: Busca FCS_HISTORICO_TIPO';
        -- Hay que obtener el tipo de actuacion del historico
        BEGIN            
            IF (P_IDTIPOACTUACION IS NULL) THEN
                SELECT IMPORTE, IMPORTEMAXIMO
                    INTO P_IMPORTE, P_MAXIMO
                FROM FCS_HISTORICO_TIPOASISTCOLEGIO
                WHERE  IDINSTITUCION = P_IDINSTITUCION
                    AND IDTIPOASISTENCIACOLEGIO = P_IDTIPOASISTENCIA
                    AND IDFACTURACION = P_IDFACTURACION;
                        
            ELSE                
                SELECT IMPORTE, IMPORTEMAXIMO
                    INTO P_IMPORTE, P_MAXIMO
                FROM FCS_HISTORICO_TIPOACTUACION
                WHERE  IDINSTITUCION = P_IDINSTITUCION
                    AND IDTIPOASISTENCIA = P_IDTIPOASISTENCIA
                    AND IDTIPOACTUACION = P_IDTIPOACTUACION
                    AND IDFACTURACION = P_IDFACTURACION;
            END IF;

            V_TIENEHISTORICO := TRUE;

        EXCEPTION
            WHEN OTHERS THEN
                V_TIENEHISTORICO := FALSE;
        END;

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_TIPO: Busca SCS_TIPO';
        -- Si no obtenemos un idFacturacion o no tenia historico, hay que obtener el tipo de actuacion actual
        IF (V_TIENEHISTORICO = FALSE) THEN
        
            IF (P_IDTIPOACTUACION IS NULL) THEN
                SELECT TASI.IMPORTE, TASI.IMPORTEMAXIMO
                    INTO P_IMPORTE, P_MAXIMO
                FROM SCS_TIPOASISTENCIACOLEGIO TASI
                WHERE TASI.IDINSTITUCION = P_IDINSTITUCION
                    AND TASI.IDTIPOASISTENCIACOLEGIO = P_IDTIPOASISTENCIA;
                    
            ELSE
                SELECT TACT.IMPORTE, TACT.IMPORTEMAXIMO
                    INTO P_IMPORTE, P_MAXIMO
                FROM SCS_TIPOACTUACION TACT
                WHERE TACT.IDINSTITUCION = P_IDINSTITUCION
                    AND TACT.IDTIPOASISTENCIA = P_IDTIPOASISTENCIA
                    AND TACT.IDTIPOACTUACION = P_IDTIPOACTUACION;
            END IF;
        END IF;

        P_IMPORTE := NVL(P_IMPORTE, NVL(P_CONFIG_NOPAGAGUARDIA.IMPORTE, 0));

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_TIPO: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_NOPAGAGUARDIA_TIPO;
            
    /****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia_MotImp
    Descripcion: Calcula el motivo y el importe (CG + UG + TP)

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_CONFIG_NOPAGAGUARDIA - IN - Configuracion de la guardia cuando no paga guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA o V_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA)
    - P_IMPORTEFACTURADO - IN - Importe facturado - NUMBER (FCS_FACT_XXX)
    - P_IMPORTEACUMULADOFACTURADO - IN - Importe acumulado facturado - NUMBER
    - P_IMPORTEACUMULADONOFACTURADO - IN - Importe acumulado sin facturar - NUMBER
    - P_MAXIMO - IN - Importe maximo - NUMBER
    - P_MINIMO - IN - Importe minimo - NUMBER
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de persona - NUMBER    
    - P_FECHAINICIO - IN - Fecha inicial de la unidad de guardia - DATE
    - P_FECHAFIN - IN - Fecha final de la unidad de guardia - DATE
    - P_IDTIPOASISTENCIACOLEGIO - IN - Identificador del tipo de asistencias - NUMBER
    - P_IDTIPOACTUACION - IN - Identificador del tipo de actuacion - NUMBER    
    - P_MOTIVO - OUT - Motivo calculado - NUMBER
    - P_IMPORTE - OUT - Importe calculado - NUMBER

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/      
    PROCEDURE PROC_FACT_NOPAGAGUARDIA_MOTIMP(
        P_CONFIG_NOPAGAGUARDIA IN TIPO_FACTURACION, -- V_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA o CONFIG_FUERAGUARDIA
        P_IMPORTEFACTURADO IN NUMBER,
        P_IMPORTEACUMULADOFACTURADO IN NUMBER,
        P_IMPORTEACUMULADONOFACTURADO IN NUMBER,
        P_MAXIMO IN NUMBER,
        P_MINIMO IN NUMBER,
        P_IDINSTITUCION IN FCS_FACT_GUARDIASCOLEGIADO.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_FACT_GUARDIASCOLEGIADO.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_FACT_GUARDIASCOLEGIADO.IDGUARDIA%TYPE,
        P_IDPERSONA IN FCS_FACT_GUARDIASCOLEGIADO.IDPERSONA%TYPE,
        P_FECHAINICIO IN FCS_FACT_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
        P_FECHAFIN IN FCS_FACT_GUARDIASCOLEGIADO.FECHAFIN%TYPE, -- Para CG es NULL
        P_IDTIPOASISTENCIACOLEGIO IN FCS_FACT_GUARDIASCOLEGIADO.IDTIPOASISTENCIACOLEGIO%TYPE, -- Si no aplica tipos es NULL
        P_IDTIPOACTUACION IN FCS_FACT_GUARDIASCOLEGIADO.IDTIPOACTUACION%TYPE,-- Si no aplica tipos por actuaciones es NULL
        P_MOTIVO OUT NUMBER,
        P_IMPORTE OUT NUMBER) IS
        
    BEGIN
        -- Sin Facturar
        IF (P_IMPORTEFACTURADO <= 0.0) THEN

            -- SinFacturar + SuperaMaximo
            IF (P_MAXIMO > 0 AND P_IMPORTEACUMULADONOFACTURADO > P_MAXIMO) THEN                
                P_IMPORTE := P_MAXIMO;
                IF (P_IDTIPOASISTENCIACOLEGIO IS NOT NULL) THEN -- Si tiene dato es que aplica tipos
                    P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMONOFACTURADO;
                ELSE
                    P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO;
                END IF;

            -- SinFacturar + SinMaximo + InferiorMinimo
            ELSIF (P_MINIMO > 0 AND P_IMPORTEACUMULADONOFACTURADO < P_MINIMO) THEN                 
                P_IMPORTE := P_MINIMO;
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMONOFACTURADO; -- Los tipos no tiene minimo

            -- SinFacturar + SinMaximo + SinMinimo
            ELSE                
                P_IMPORTE := P_IMPORTEACUMULADONOFACTURADO;
                IF (P_IDTIPOASISTENCIACOLEGIO IS NOT NULL) THEN -- Si tiene dato es que aplica tipos
                    P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO;                    
                    
                ELSE
                    P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
                END IF;
            END IF;

        -- Facturado + SuperaMaximo
        ELSIF (P_MAXIMO > 0 AND P_IMPORTEACUMULADONOFACTURADO + P_IMPORTEACUMULADOFACTURADO > P_MAXIMO)  THEN            
            P_IMPORTE := P_MAXIMO - P_IMPORTEFACTURADO;
            IF (P_IDTIPOASISTENCIACOLEGIO IS NOT NULL) THEN -- Si tiene dato es que aplica tipos
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMOFACTURADO;
                PROC_FACT_CONTROL_HITO(
                  P_IDINSTITUCION,
                  P_IDTURNO,
                  P_IDGUARDIA,
                  P_IDPERSONA,
                  P_FECHAINICIO,
                  P_FECHAFIN,
                  P_IDTIPOASISTENCIACOLEGIO,
                  P_IDTIPOACTUACION,
                  P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMONOFACTURADO,
                  P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.MAXIMOFACTURADO,
                  P_IMPORTE);                                  
                
            ELSE
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMOFACTURADO;                
                PROC_FACT_CONTROL_HITO(
                  P_IDINSTITUCION,
                  P_IDTURNO,
                  P_IDGUARDIA,
                  P_IDPERSONA,
                  P_FECHAINICIO,
                  P_FECHAFIN,
                  NULL, -- IDTIPOASISTENCIACOLEGIO
                  NULL, -- IDTIPOACTUACION
                  P_CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO,
                  P_CONFIG_NOPAGAGUARDIA.MOTIVO.MAXIMOFACTURADO,
                  P_IMPORTE);                 
            END IF;

        -- Facturado + SinMaximo + InferiorMinimo
        ELSIF (P_MINIMO > 0 AND P_IMPORTEACUMULADONOFACTURADO + P_IMPORTEACUMULADOFACTURADO < P_MINIMO)  THEN            
            P_IMPORTE := P_MINIMO - P_IMPORTEFACTURADO;
            P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMOFACTURADO; -- Los tipos no tiene minimo            
            PROC_FACT_CONTROL_HITO(
              P_IDINSTITUCION,
              P_IDTURNO,
              P_IDGUARDIA,
              P_IDPERSONA,
              P_FECHAINICIO,
              P_FECHAFIN,
              NULL, -- IDTIPOASISTENCIACOLEGIO
              NULL, -- IDTIPOACTUACION
              P_CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMONOFACTURADO,
              P_CONFIG_NOPAGAGUARDIA.MOTIVO.MINIMOFACTURADO,
              P_IMPORTE);             

        -- Facturado + SinMaximo + SinMinimo + MinimoPrevio
        ELSIF (P_IMPORTEACUMULADOFACTURADO < P_IMPORTEFACTURADO)  THEN            
            P_IMPORTE := P_IMPORTEACUMULADONOFACTURADO  + P_IMPORTEACUMULADOFACTURADO - P_IMPORTEFACTURADO;
            IF (P_IDTIPOASISTENCIACOLEGIO IS NOT NULL) THEN -- Si tiene dato es que aplica tipos
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.FACTURADO;
            ELSE
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO;
            END IF;

        -- Facturado + SinMaximo + SinMinimo + SinMinimoPrevio
        ELSE            
            P_IMPORTE := P_IMPORTEACUMULADONOFACTURADO;
            IF (P_IDTIPOASISTENCIACOLEGIO IS NOT NULL) THEN -- Si tiene dato es que aplica tipos
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO_TP.FACTURADO;
            ELSE
                P_MOTIVO := P_CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO;
            END IF;
        END IF;
        
        -- Control de importes negativos
        IF (P_IMPORTE < 0) THEN
            P_IMPORTE := 0;
        END IF;
    END PROC_FACT_NOPAGAGUARDIA_MOTIMP;   
    
  /****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia_As
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por asistencias sin pagar guardia y sin tipo

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN - Contador de la unidad de guardia actual - NUMBER
    - P_IMPORTENOFACTURADO_UG - OUT - Importe acumulado no facturado por la unidad de guardia - NUMBER
    - P_IMPORTEFACTURADO_UG - OUT - Importe acumulado facturado por la unidad de guardia - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_NOPAGAGUARDIA_As(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, --  V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN NUMBER,
        P_IMPORTENOFACTURADO_UG OUT NUMBER,
        P_IMPORTEFACTURADO_UG OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        -- Registros para iterar
        R_ACT CURSOR_ACT_AS%ROWTYPE;
        R_AS CURSOR_AS%ROWTYPE;        

        -- Variables AS
        V_COSTEFIJONOFACTURADO_AS NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la asistencia
        V_IMPORTE_AS NUMBER; -- Contiene el importe configurado para cada asistencia
        V_TIENEACTNOFACTURADAS_AS BOOLEAN; -- Indica si la asistencia tiene actuaciones sin facturar

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_As: Inicio';
        
        -- Variables de configuracion de la guardia
        V_IMPORTE_AS := NVL(P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.IMPORTE, 0);
            
        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        P_IMPORTENOFACTURADO_UG := 0;
        P_IMPORTEFACTURADO_UG := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;                    
        
        -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion    
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_As: FOR CURSOR_AS';
        FOR R_AS IN CURSOR_AS (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAFIN_UG,
            P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS,
            P_DATOSFACTURACION.FECHADESDE,
            P_DATOSFACTURACION.FECHAHASTA,
            NULL, -- IDTIPOASISTENCIACOLEGIO
            NULL, -- IDTIPOACTUACION
            P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO
        ) LOOP
            
            -- Inicializo las variables acumuladoras de las asistencias (AS)
            V_COSTEFIJONOFACTURADO_AS := 0;
            V_TIENEACTNOFACTURADAS_AS := FALSE;

            -- Obtenemos las actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_As: FOR CURSOR_ACT_AS';
            FOR R_ACT IN CURSOR_ACT_AS (
                P_MATRIZFACTURABLE.IDINSTITUCION,        
                R_AS.ANIO,
                R_AS.NUMERO,
                P_MATRIZFACTURABLE.IDFACTURACION,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA
            ) LOOP  
                
                -- Actualizo las variables acumuladoras de las asistencias (AS)
                V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;
                V_TIENEACTNOFACTURADAS_AS := TRUE;                   
                
                -- Genero un apunte de la actuacion (AC)
                IND_AC := IND_AC + 1;
                M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;
                M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;
                M_APUNTE_AC(IND_AC).IMPORTE := 0;
                M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
                M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
            END LOOP; -- Fin CURSOR_ACT_AS
            
            /*** TRATAMIENTO ASISTENCIA ***/
            
            -- Actualizo las variables acumuladoras de las unidades de guardia (UG)
            P_IMPORTEFACTURADO_UG := P_IMPORTEFACTURADO_UG + R_AS.IMPORTE_FACTURADO_AS;
            
            -- Compruebo si la asistencia tiene actuaciones sin facturar
            IF (V_TIENEACTNOFACTURADAS_AS = TRUE) THEN
            
                -- Actualizo las variables acumuladoras de las unidades de guardia (UG)
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_AS;
            
                -- Genero un apunte de la asistencia (AS)
                IND_AS := IND_AS + 1;
                M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;
                M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;                      
            
                -- Comprueba si NO se ha facturado la asistencia (AS)
                IF (R_AS.FACTURADO = 0) THEN
                    M_APUNTE_AS(IND_AS).IMPORTE := V_IMPORTE_AS;
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
                    -- Actualizo las variables acumuladoras de las unidades de guardia (UG)
                    P_IMPORTENOFACTURADO_UG := P_IMPORTENOFACTURADO_UG + V_IMPORTE_AS;                    
                            
                ELSE -- Asistencia facturada previamente (AS)                    
                    M_APUNTE_AS(IND_AS).IMPORTE := 0;
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO;
                END IF;
            END IF;
        END LOOP; -- Fin CURSOR_AS

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_As: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_NOPAGAGUARDIA_As;
    
 /****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia_Ac
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por actuaciones sin pagar guardia y sin tipo

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN - Contador de la unidad de guardia actual - NUMBER    
    - P_IMPORTENOFACTURADO_UG - OUT - Importe acumulado no facturado por la unidad de guardia - NUMBER
    - P_IMPORTEFACTURADO_UG - OUT - Importe acumulado facturado por la unidad de guardia - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_DERIVADA_UG - OUT - Devuelve si tiene alguna asistencia derivada la unidad de guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_NOPAGAGUARDIA_Ac(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN NUMBER,        
        P_IMPORTENOFACTURADO_UG OUT NUMBER,
        P_IMPORTEFACTURADO_UG OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_DERIVADA_UG OUT BOOLEAN,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        -- Registros para iterar
        R_ACT CURSOR_ACT_AC%ROWTYPE;
        R_AS CURSOR_AS%ROWTYPE;        

        -- Variables AS
        V_COSTEFIJONOFACTURADO_AS NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la asistencia
        V_DERIVA_AS NUMBER; -- Contiene si deriva la asistencia                        
        V_IMPORTEFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones facturadas de la asistencia
        V_IMPORTENOFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones sin facturar de la asistencia
        V_TIENEACTNOFACTURADAS_AS BOOLEAN; -- Indica si la asistencia tiene actuaciones sin facturar
        
        -- Variables AC
        V_IMPORTE_AC NUMBER; -- Contiene el importe configurado para cada actuacion        
        V_TIPODERIVACION_AC NUMBER; -- Contiene el tipo de derivacion de la actuacion

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_Ac: Inicio';
        
        -- Variables de configuracion de la guardia
        V_IMPORTE_AC := NVL(P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.IMPORTE, 0);                

        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        P_IMPORTENOFACTURADO_UG := 0;
        P_IMPORTEFACTURADO_UG := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;
        P_DERIVADA_UG := FALSE; -- Inicialmente no es derivada UG

        -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_Ac: FOR CURSOR_AS';
        FOR R_AS IN CURSOR_AS (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAFIN_UG,
            P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS,
            P_DATOSFACTURACION.FECHADESDE,
            P_DATOSFACTURACION.FECHAHASTA,
            NULL, -- IDTIPOASISTENCIACOLEGIO
            NULL, -- IDTIPOACTUACION
            P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO
        ) LOOP
        
            -- ** Cambio para Asistencias que derivan en Designacion **
            -- Comprueba si deriva la asistencia
            V_DERIVA_AS := F_ES_ASISTENCIA_DERIVADA(
                P_MATRIZFACTURABLE.IDINSTITUCION, 
                R_AS.ANIO, 
                R_AS.NUMERO, 
                P_DATOSFACTURACION.FECHADESDE, 
                P_DATOSFACTURACION.FECHAHASTA);                
            IF (P_DERIVADA_UG = FALSE AND V_DERIVA_AS = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN
               P_DERIVADA_UG := TRUE; 
            END IF;
            
            -- Inicializo las variables acumuladoras de las asistencias (AS)
            V_IMPORTENOFACTURADO_AS := 0;
            V_IMPORTEFACTURADO_AS := 0;
            V_COSTEFIJONOFACTURADO_AS := 0;  
            V_TIENEACTNOFACTURADAS_AS := FALSE;

            -- Obtenemos las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_Ac: FOR CURSOR_ACT_AC';
            FOR R_ACT IN CURSOR_ACT_AC (
                P_MATRIZFACTURABLE.IDINSTITUCION,                    
                R_AS.ANIO,
                R_AS.NUMERO,
                P_MATRIZFACTURABLE.IDFACTURACION,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA,
                NULL, -- IDTIPOASISTENCIA
                NULL -- IDTIPOACTUACION
            ) LOOP  

                -- Comprueba si NO se ha facturado la actuacion (AC)
                IF (R_ACT.FACTURADO = 0) THEN                    
                
                    -- Actualizo las variables acumuladoras de las asistencias (AS)                     
                    V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;
                    V_TIENEACTNOFACTURADAS_AS := TRUE;
                    
                    -- Genero un apunte de la actuacion (AC)
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                    M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                    M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;                                        
                    M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;
                    M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                                
                    -- ** Cambio para Asistencias que derivan en Designacion **
                    -- Comprueba si la asistencia de la actuacion es derivada
                    IF (V_DERIVA_AS = PKG_SIGA_CONSTANTES.DB_TRUE_N) THEN
                    
                        -- Obtiene el tipo de derivacion de la actuacion
                        V_TIPODERIVACION_AC := F_ES_TIPO_DERIVACION(
                            P_MATRIZFACTURABLE.IDINSTITUCION, 
                            R_AS.ANIO, 
                            R_AS.NUMERO, 
                            R_ACT.IDACTUACION);
                        
                        -- Comprueba si es un tipo de derivacion la actuacion
                        IF (V_TIPODERIVACION_AC = PKG_SIGA_CONSTANTES.DB_FALSE_N) THEN
                            M_APUNTE_AC(IND_AC).IMPORTE := V_IMPORTE_AC;                            
                            M_APUNTE_AC(IND_AC).MOTIVO := C_AcNoJudDeriv;
                            
                            -- Actualizo las variables acumuladoras de las asistencias (AS)
                            V_IMPORTENOFACTURADO_AS := V_IMPORTENOFACTURADO_AS + V_IMPORTE_AC;

                        ELSE -- Deriva la asistencia y es otro tipo distinto de derivada la actuacion
                            M_APUNTE_AC(IND_AC).IMPORTE := 0;
                            M_APUNTE_AC(IND_AC).MOTIVO := C_AcJudDeriv;
                        END IF;
                        
                    ELSE -- Tratamiento para actuaciones sin asistencia derivada (tratamiento normal)
                        M_APUNTE_AC(IND_AC).IMPORTE := V_IMPORTE_AC;
                        M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
                        
                        -- Actualizo las variables acumuladoras de las asistencias (AS)
                        V_IMPORTENOFACTURADO_AS := V_IMPORTENOFACTURADO_AS + V_IMPORTE_AC;
                    END IF;

                ELSE -- Actuacion facturada previamente (AC)
                    -- Actualizo las variables acumuladoras de las asistencias (AS)
                    V_IMPORTEFACTURADO_AS := V_IMPORTEFACTURADO_AS + R_ACT.IMPORTE_FACTURADO_ACT;
                END IF;
            END LOOP; -- Fin CURSOR_ACT_AC
            
            /*** TRATAMIENTO ASISTENCIA ***/
            
            -- Actualizo las variables acumuladoras de las unidades de guardia (UG)
            P_IMPORTEFACTURADO_UG := P_IMPORTEFACTURADO_UG + V_IMPORTEFACTURADO_AS;
            
            -- Compruebo si la asistencia tiene actuaciones sin facturar
            IF (V_TIENEACTNOFACTURADAS_AS = TRUE) THEN  
            
                -- Actualizo las variables acumuladoras de las unidades de guardia (UG)            
                P_IMPORTENOFACTURADO_UG := P_IMPORTENOFACTURADO_UG + V_IMPORTENOFACTURADO_AS;
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_AS;
                
                -- Genero un apunte de la asistencia (AS)
                IND_AS := IND_AS + 1;
                M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;                
                M_APUNTE_AS(IND_AS).IMPORTE := 0; -- V_IMPORTENOFACTURADO_AS;
                M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                                
                -- Comprueba si NO se ha facturado la asistencia (AS)
                IF (R_AS.FACTURADO = 0) THEN
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
                    
                ELSE -- Asistencia facturada previamente (AS)
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO;
                END IF;
            END IF; 
        END LOOP; -- Fin CURSOR_AS

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_Ac: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_NOPAGAGUARDIA_Ac;
    
/****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia_AsTp
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por asistencias sin pagar guardia y con tipo

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAINICIO_UG - IN - Fecha inicial de la unidad de guardia - DATE
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN OUT - Contador de la unidad de guardia actual - NUMBER
    - P_IMPORTENOFACTURADO_UG - OUT - Importe acumulado no facturado por la unidad de guardia - NUMBER
    - P_IMPORTEFACTURADO_UG - OUT - Importe acumulado facturado por la unidad de guardia - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAINICIO_UG IN DATE,
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN OUT NUMBER,
        P_IMPORTENOFACTURADO_UG OUT NUMBER,
        P_IMPORTEFACTURADO_UG OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS              

        -- Registros para iterar        
        R_ACT CURSOR_ACT_AS%ROWTYPE;
        R_AS CURSOR_AS%ROWTYPE;
        R_TP CURSOR_TIPOS_AS%ROWTYPE;
        
        -- Variables TP
        V_COSTEFIJONOFACTURADO_TP NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar del tipo
        V_IMPORTE_TP NUMBER; -- Contiene el importe del tipo
        V_IMPORTEFACTURADO_AS_TP NUMBER; -- Contiene el importe de las asistencias facturadas del tipo
        V_IMPORTEFACTURADO_TP_TP NUMBER; -- Contiene el importe acumulado del tipo
        V_IMPORTENOFACTURADO_TP NUMBER; -- Contiene el importe de las asistencias sin facturar del tipo
        V_MAXIMO_TP NUMBER; -- Contiene el importe maximo configurado para cada tipo de asistencia
        V_MOTIVO_TP NUMBER; -- Contiene el motivo del tipo
        
        -- Variables AS
        V_COSTEFIJONOFACTURADO_AS NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la asistencia
        V_IMPORTE_AS NUMBER; -- Contiene el importe configurado para cada tipo de asistencia 
        V_TIENEACTNOFACTURADAS_AS BOOLEAN; -- Indica si la asistencia tiene actuaciones sin facturar
        
        -- Variables para el tratamiento de errores
        V_CODRETORNO VARCHAR2(10) := '0';
        V_DATOSERROR VARCHAR2(4000) := NULL;
        E_ERROR_HEREDADO EXCEPTION;

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: Inicio';
            
        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        P_IMPORTENOFACTURADO_UG := 0;
        P_IMPORTEFACTURADO_UG := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;     
        
        -- Obtenemos los tipos de asistencias del colegio
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: FOR CURSOR_TIPOS_AS';
        FOR R_TP IN CURSOR_TIPOS_AS (  
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAINICIO_UG,
            P_FECHAFIN_UG
        ) LOOP
        
            -- Inicializo las variables acumuladoras de los tipos (TP)
            V_IMPORTENOFACTURADO_TP := 0;
            V_IMPORTEFACTURADO_AS_TP := 0;
            V_COSTEFIJONOFACTURADO_TP := 0;           
        
            -- Incremento variable para tipos
            P_CONTADOR_UG := P_CONTADOR_UG + 1;
        
            -- Obtengo el importe y maximo del tipo
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_TIPO';
            PROC_FACT_NOPAGAGUARDIA_TIPO(
                P_MATRIZFACTURABLE.IDINSTITUCION,                                
                R_TP.IDTIPOASISTENCIACOLEGIO,
                NULL, -- IDTIPOACTUACION
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA,
                P_MATRIZFACTURABLE.IDFACTURACION,
                V_IMPORTE_AS,
                V_MAXIMO_TP,
                V_CODRETORNO,
                V_DATOSERROR);                        
            IF V_CODRETORNO <> '0' THEN
                RAISE E_ERROR_HEREDADO;
            END IF;                
            
            -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: FOR CURSOR_AS';
            FOR R_AS IN CURSOR_AS (
                P_MATRIZFACTURABLE.IDINSTITUCION,
                P_MATRIZFACTURABLE.IDTURNO,
                P_MATRIZFACTURABLE.IDGUARDIA,
                P_MATRIZFACTURABLE.IDPERSONA,
                P_FECHAFIN_UG,
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA,
                R_TP.IDTIPOASISTENCIACOLEGIO,
                NULL, -- IDTIPOACTUACION
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO
            ) LOOP                         
                
                -- Inicializo las variables acumuladoras de las asistencias (AS)
                V_COSTEFIJONOFACTURADO_AS := 0;
                V_TIENEACTNOFACTURADAS_AS := FALSE;  

                -- Obtenemos las actuaciones sin facturar justificadas en el periodo de facturacion
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: FOR CURSOR_ACT_AS';
                FOR R_ACT IN CURSOR_ACT_AS (
                    P_MATRIZFACTURABLE.IDINSTITUCION,                    
                    R_AS.ANIO,
                    R_AS.NUMERO,
                    P_MATRIZFACTURABLE.IDFACTURACION,
                    P_DATOSFACTURACION.FECHADESDE,
                    P_DATOSFACTURACION.FECHAHASTA
                ) LOOP  
                    
                    -- Actualizo las variables acumuladoras de las asistencias (AS) 
                    V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;
                    V_TIENEACTNOFACTURADAS_AS := TRUE;                   
                    
                    -- Genero un apunte de la actuacion (AC)
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                    M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                    M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;
                    M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;
                    M_APUNTE_AC(IND_AC).IMPORTE := 0;
                    M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO;
                    M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;                    
                END LOOP; -- Fin CURSOR_ACT_AS
                
                /*** TRATAMIENTO ASISTENCIA ***/
                
                -- Actualizo las variables acumuladoras de los tipos (TP)
                V_IMPORTEFACTURADO_AS_TP := V_IMPORTEFACTURADO_AS_TP + R_AS.IMPORTE_FACTURADO_AS;
                
                -- Comprueba que la asistencia tiene actuaciones sin facturar dentro del periodo de la facturacion
                IF (V_TIENEACTNOFACTURADAS_AS = TRUE) THEN
                                
                    -- Genero un apunte de la asistencia (AS)
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                    M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;
                    M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                    
                    -- Actualizo las variables acumuladoras de los tipos (TP)
                    V_COSTEFIJONOFACTURADO_TP := V_COSTEFIJONOFACTURADO_TP + V_COSTEFIJONOFACTURADO_AS;
                    
                    -- Comprueba si NO se ha facturado la asistencia (AS)
                    IF (R_AS.FACTURADO = 0) THEN                        
                        M_APUNTE_AS(IND_AS).IMPORTE := V_IMPORTE_AS;
                        M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO;
                    
                        -- Actualizo las variables acumuladoras de los tipos (TP)
                        V_IMPORTENOFACTURADO_TP := V_IMPORTENOFACTURADO_TP + V_IMPORTE_AS;                    
                        
                    ELSE -- Asistencia facturada previamente (AS)
                        M_APUNTE_AS(IND_AS).IMPORTE := 0;
                        M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.FACTURADO;
                    END IF;
                END IF;
            END LOOP; -- Fin CURSOR_AS
            
            /*** TRATAMIENTO TIPOS ***/
            
            -- Comprueba que no existe el valor previo
            IF (R_TP.IMPORTE_FACTURADO_TP = -1) THEN            
                -- Como no tengo el valor, le pongo el actual facturado por las asistencias
                -- En la facturacion antigua no tenia TP los identificadores necesarios para encontrar el importe de cada tipo
                -- Con esto lo unico que podria no funcionar es el maximo del tipo, si ya facturo antes, podria volver a facturarlo porque no encuentra el hito
                V_IMPORTEFACTURADO_TP_TP := V_IMPORTEFACTURADO_AS_TP; 
            ELSE
                V_IMPORTEFACTURADO_TP_TP := R_TP.IMPORTE_FACTURADO_TP;
            END IF;
            
            -- Actualizo las variables acumuladoras de las unidades de guardia (UG)            
            P_IMPORTEFACTURADO_UG := P_IMPORTEFACTURADO_UG + V_IMPORTEFACTURADO_TP_TP;
            
            -- Comprueba que haya algo que facturar
            IF (V_IMPORTENOFACTURADO_TP > 0) THEN
            
                -- Obtiene el motivo y el importe del tipo (TP)
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_MOTIMP para unidades de guardia';
                PROC_FACT_NOPAGAGUARDIA_MOTIMP(
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA,
                    V_IMPORTEFACTURADO_TP_TP,
                    V_IMPORTEFACTURADO_AS_TP,
                    V_IMPORTENOFACTURADO_TP,
                    V_MAXIMO_TP,
                    0, -- MINIMO_TP = 0
                    P_MATRIZFACTURABLE.IDINSTITUCION,
                    P_MATRIZFACTURABLE.IDTURNO,
                    P_MATRIZFACTURABLE.IDGUARDIA,
                    P_MATRIZFACTURABLE.IDPERSONA,
                    P_MATRIZFACTURABLE.FECHAINICIO,
                    P_FECHAINICIO_UG,
                    R_TP.IDTIPOASISTENCIACOLEGIO,
                    NULL, -- IDTIPOACTUACION
                    V_MOTIVO_TP,
                    V_IMPORTE_TP);                    
                    
                -- Actualizo las variables acumuladoras de las unidades de guardia (UG)            
                P_IMPORTENOFACTURADO_UG := P_IMPORTENOFACTURADO_UG + V_IMPORTE_TP;
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_TP;
                
                IF (R_TP.IMPORTE_FACTURADO_TP = -1) THEN
                    -- En la facturacion antigua no tenia TP los identificadores necesarios para encontrar el importe de cada tipo
                    -- Hay que anyadir al importe actual, el acumulado pasado, para que funcione para el futuro
                    -- Saldra un valor raro en TP, pero no se debe acumular para CG 
                    V_IMPORTE_TP := V_IMPORTE_TP + V_IMPORTEFACTURADO_AS_TP;
                END IF;                                                         
                    
                -- Genero un apunte del tipo (TP)
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := P_MATRIZFACTURABLE.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := P_FECHAINICIO_UG;
                M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJONOFACTURADO_TP;
                M_APUNTE_UG(IND_UG).CONTADOR := P_CONTADOR_UG;
                M_APUNTE_UG(IND_UG).IMPORTE := V_IMPORTE_TP;
                M_APUNTE_UG(IND_UG).MOTIVO := V_MOTIVO_TP;
                M_APUNTE_UG(IND_UG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                M_APUNTE_UG(IND_UG).IDTIPOASISTENCIACOLEGIO := R_TP.IDTIPOASISTENCIACOLEGIO;
            END IF;
        END LOOP; -- Fin CURSOR_TIPOS_AS                              

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AsTp: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR_HEREDADO THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;          
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_NOPAGAGUARDIA_AsTp;    
    
/****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia_AcTp
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por actuaciones sin pagar guardia y con tipo

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAINICIO_UG - IN - Fecha inicial de la unidad de guardia - DATE
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN OUT - Contador de la unidad de guardia actual - NUMBER
    - P_IMPORTENOFACTURADO_UG - OUT - Importe acumulado no facturado por la unidad de guardia - NUMBER
    - P_IMPORTEFACTURADO_UG - OUT - Importe acumulado facturado por la unidad de guardia - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAINICIO_UG IN DATE,
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN OUT NUMBER,
        P_IMPORTENOFACTURADO_UG OUT NUMBER,
        P_IMPORTEFACTURADO_UG OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS             
        
        --Registros para iterar        
        R_ACT CURSOR_ACT_AC%ROWTYPE;
        R_AS CURSOR_AS%ROWTYPE;
        R_TP CURSOR_TIPOS_AC%ROWTYPE;
        
        -- Variables TP
        V_COSTEFIJONOFACTURADO_TP NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar del tipo
        V_IMPORTE_TP NUMBER; -- Contiene el importe del tipo 
        V_IMPORTEFACTURADO_AC_TP NUMBER; -- Contiene el importe de las actuaciones facturadas del tipo
        V_IMPORTEFACTURADO_TP_TP NUMBER; -- Contiene el importe acumulado del tipo
        V_IMPORTENOFACTURADO_TP NUMBER; -- Contiene el importe de las actuaciones sin facturar del tipo
        V_MAXIMO_TP NUMBER; -- Contiene el importe maximo configurado para cada tipo de actuacion 
        V_MOTIVO_TP NUMBER; -- Contiene el motivo del tipo                        
        
        -- Variables AS
        V_COSTEFIJONOFACTURADO_AS NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la asistencia        
        V_IMPORTEFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones facturadas de la asistencia
        V_IMPORTENOFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones sin facturar de la asistencia
        V_TIENEACTNOFACTURADAS_AS BOOLEAN; -- Indica si la asistencia tiene actuaciones sin facturar        
        
        -- Variables AC
        V_IMPORTE_AC NUMBER; -- Contiene el importe configurado para cada tipo de actuacion         
        
        -- Variables para el tratamiento de errores
        V_CODRETORNO VARCHAR2(10) := '0';
        V_DATOSERROR VARCHAR2(4000) := NULL;
        E_ERROR_HEREDADO EXCEPTION;

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: Inicio';
            
        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        P_IMPORTENOFACTURADO_UG := 0;
        P_IMPORTEFACTURADO_UG := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;          
        
        -- Obtenemos los tipos de actuaciones del colegio
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: FOR CURSOR_TIPOS_AC';
        FOR R_TP IN CURSOR_TIPOS_AC (  
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAINICIO_UG,
            P_FECHAFIN_UG,
            'N' -- V_FUERAGUARDIA
        ) LOOP    
        
            -- Inicializo las variables acumuladoras de los tipos (TP)
            V_IMPORTENOFACTURADO_TP := 0;
            V_IMPORTEFACTURADO_AC_TP := 0;
            V_COSTEFIJONOFACTURADO_TP := 0;
            P_CONTADOR_UG := P_CONTADOR_UG + 1;
            
            -- Obtengo el importe y maximo del tipo
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_TIPO';
            PROC_FACT_NOPAGAGUARDIA_TIPO(
                P_MATRIZFACTURABLE.IDINSTITUCION,                            
                R_TP.IDTIPOASISTENCIA,
                R_TP.IDTIPOACTUACION,                      
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA,
                P_MATRIZFACTURABLE.IDFACTURACION,
                V_IMPORTE_AC,
                V_MAXIMO_TP,
                V_CODRETORNO,
                V_DATOSERROR);                        
            IF V_CODRETORNO <> '0' THEN
                RAISE E_ERROR_HEREDADO;
            END IF;            
            
            -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: FOR CURSOR_AS';
            FOR R_AS IN CURSOR_AS (
                P_MATRIZFACTURABLE.IDINSTITUCION,
                P_MATRIZFACTURABLE.IDTURNO,
                P_MATRIZFACTURABLE.IDGUARDIA,
                P_MATRIZFACTURABLE.IDPERSONA,
                P_FECHAFIN_UG,
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA,
                R_TP.IDTIPOASISTENCIA,
                R_TP.IDTIPOACTUACION,
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO
            ) LOOP
            
                V_TIENEACTNOFACTURADAS_AS := FALSE;                      
                
                -- Inicializo las variables acumuladoras de las asistencias (AS)
                V_IMPORTENOFACTURADO_AS := 0;
                V_IMPORTEFACTURADO_AS := 0;
                V_COSTEFIJONOFACTURADO_AS := 0;

                -- Obtenemos las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: FOR CURSOR_ACT_AC';
                FOR R_ACT IN CURSOR_ACT_AC (
                    P_MATRIZFACTURABLE.IDINSTITUCION,                    
                    R_AS.ANIO,
                    R_AS.NUMERO,
                    P_MATRIZFACTURABLE.IDFACTURACION,
                    P_DATOSFACTURACION.FECHADESDE,
                    P_DATOSFACTURACION.FECHAHASTA,
                    R_TP.IDTIPOASISTENCIA,
                    R_TP.IDTIPOACTUACION
                ) LOOP  

                    -- Comprueba si NO se ha facturado la actuacion (AC)
                    IF (R_ACT.FACTURADO = 0) THEN
                    
                        V_TIENEACTNOFACTURADAS_AS := TRUE;
                    
                        -- Actualizo las variables acumuladoras de las asistencias (AS)
                        V_IMPORTENOFACTURADO_AS := V_IMPORTENOFACTURADO_AS + V_IMPORTE_AC; 
                        V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;                   
                        
                        -- Genero un apunte de la actuacion (AC)
                        IND_AC := IND_AC + 1;
                        M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                        M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                        M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                        M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                        M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                        M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                        M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;
                        M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;
                        M_APUNTE_AC(IND_AC).IMPORTE := V_IMPORTE_AC;
                        M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO;
                        M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;                    

                    ELSE -- Actuacion facturada previamente (AC)
                        -- Actualizo las variables acumuladoras de las asistencias (AS)
                        V_IMPORTEFACTURADO_AS := V_IMPORTEFACTURADO_AS + R_ACT.IMPORTE_FACTURADO_ACT;
                    END IF;
                END LOOP; -- Fin CURSOR_ACT_AC
                
                /*** TRATAMIENTO ASISTENCIA ***/
                
                -- Actualizo las variables acumuladoras de los tipos (TP)
                V_IMPORTEFACTURADO_AC_TP := V_IMPORTEFACTURADO_AC_TP + V_IMPORTEFACTURADO_AS;
                
                -- Comprueba que la asistencia tiene actuaciones sin facturar dentro del periodo de la facturacion
                IF (V_TIENEACTNOFACTURADAS_AS = TRUE) THEN
            
                    -- Actualizo las variables acumuladoras de los tipos (TP)            
                    V_IMPORTENOFACTURADO_TP := V_IMPORTENOFACTURADO_TP + V_IMPORTENOFACTURADO_AS;
                    V_COSTEFIJONOFACTURADO_TP := V_COSTEFIJONOFACTURADO_TP + V_COSTEFIJONOFACTURADO_AS;
                    
                    -- Genero un apunte de la asistencia (AS)
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                    M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;                
                    M_APUNTE_AS(IND_AS).IMPORTE := 0; -- V_IMPORTENOFACTURADO_AS;
                    M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                                    
                    -- Comprueba si NO se ha facturado la asistencia (AS)
                    IF (R_AS.FACTURADO = 0) THEN
                        M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO;
                        
                    ELSE -- Asistencia facturada previamente (AS)
                        M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.FACTURADO;
                    END IF;
                END IF;
            END LOOP; -- Fin CURSOR_AS
            
            /*** TRATAMIENTO TIPOS ***/
            
            -- Comprueba que no existe el valor previo
            IF (R_TP.IMPORTE_FACTURADO_TP = -1) THEN
                -- Como no tengo el valor, le pongo el actual facturado por las actuaciones
                -- En la facturacion antigua no tenia TP los identificadores necesarios para encontrar el importe de cada tipo
                -- Con esto lo unico que podria no funcionar es el maximo del tipo, si ya facturo antes, podria volver a facturarlo porque no encuentra el hito
                V_IMPORTEFACTURADO_TP_TP := V_IMPORTEFACTURADO_AC_TP;
            ELSE
                V_IMPORTEFACTURADO_TP_TP := R_TP.IMPORTE_FACTURADO_TP;
            END IF;
            
            -- Actualizo las variables acumuladoras de la unidad de guardia (UG)            
            P_IMPORTEFACTURADO_UG := P_IMPORTEFACTURADO_UG + V_IMPORTEFACTURADO_TP_TP;
            
            -- Comprueba que haya algo que facturar
            IF (V_IMPORTENOFACTURADO_TP > 0) THEN
            
                -- Obtiene el motivo y el importe del tipo (TP)
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_MOTIMP para unidades de guardia';
                PROC_FACT_NOPAGAGUARDIA_MOTIMP(
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA,
                    V_IMPORTEFACTURADO_TP_TP,
                    V_IMPORTEFACTURADO_AC_TP,
                    V_IMPORTENOFACTURADO_TP,
                    V_MAXIMO_TP,
                    0, -- MINIMO_TP = 0
                    P_MATRIZFACTURABLE.IDINSTITUCION,
                    P_MATRIZFACTURABLE.IDTURNO,
                    P_MATRIZFACTURABLE.IDGUARDIA,
                    P_MATRIZFACTURABLE.IDPERSONA,
                    P_MATRIZFACTURABLE.FECHAINICIO,
                    P_FECHAINICIO_UG,
                    R_TP.IDTIPOASISTENCIA,
                    R_TP.IDTIPOACTUACION,
                    V_MOTIVO_TP,
                    V_IMPORTE_TP);  
                    
                -- Actualizo las variables acumuladoras de la unidad de guardia (UG)            
                P_IMPORTENOFACTURADO_UG := P_IMPORTENOFACTURADO_UG + V_IMPORTE_TP;
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_TP;
                
                IF (R_TP.IMPORTE_FACTURADO_TP = -1) THEN
                    -- En la facturacion antigua no tenia TP los identificadores necesarios para encontrar el importe de cada tipo
                    -- Hay que anyadir al importe actual, el acumulado pasado, para que funcione para el futuro
                    -- Saldra un valor raro en TP, pero no se debe acumular para CG 
                    V_IMPORTE_TP := V_IMPORTE_TP + V_IMPORTEFACTURADO_AC_TP;
                END IF;                  
                    
                -- Genero un apunte del tipo (TP)
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := P_MATRIZFACTURABLE.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := P_FECHAINICIO_UG;
                M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJONOFACTURADO_TP;
                M_APUNTE_UG(IND_UG).CONTADOR := P_CONTADOR_UG;
                M_APUNTE_UG(IND_UG).IMPORTE := V_IMPORTE_TP;
                M_APUNTE_UG(IND_UG).MOTIVO := V_MOTIVO_TP;
                M_APUNTE_UG(IND_UG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                M_APUNTE_UG(IND_UG).IDTIPOASISTENCIACOLEGIO := R_TP.IDTIPOASISTENCIA;
                M_APUNTE_UG(IND_UG).IDTIPOACTUACION := R_TP.IDTIPOACTUACION;
            END IF;                                     
        END LOOP; -- Fin CURSOR_TIPOS_AC                              

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA_AcTp: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR_HEREDADO THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;          
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_NOPAGAGUARDIA_AcTp;        
    
  /****************************************************************************************************************
    Nombre: PROC_FACT_NoPagaGuardia
    Descripcion: Apuntes de cabeceras y unidades de guardia - Facturacion sin pagar guardia

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/     
    PROCEDURE PROC_FACT_NOPAGAGUARDIA(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS                       

        --Registros para iterar
        R_UG_NPG CURSOR_UG_NPG%ROWTYPE;
        
        -- Variables UG
        V_COSTEFIJONOFACTURADO_UG NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la unidad de guardia
        V_DERIVADA_UG BOOLEAN; -- Contiene si deriva la unidad de guardia
        V_FACTURADA_UG VARCHAR2(1); -- Contiene si esta facturado la unidad de guardia (0=NoFacturado; 1=Facturado)
        V_IMPORTEFACTURADO_UG NUMBER; -- Contiene el importe de las actuaciones o asistencias facturadas de la unidad de guardia
        V_IMPORTEMINIMO_UG NUMBER; -- Contiene el importe minimo de la unidad de guardia
        V_IMPORTENOFACTURADO_UG NUMBER; -- Contiene el importe de las actuaciones o asistencias sin facturar de la unidad de guardia
        V_CONTADOR_UG NUMBER; -- Contiene el contador de la unidad de guardia
        V_CONTADOR_UG_TP NUMBER; -- Contiene el contador de la unidad de guardia y tipos
        
        -- Variables CG        
        V_COSTEFIJONOFACTURADO_CG NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la cabecera de guardia
        V_IMPORTEFACTURADO_CG NUMBER; -- Contiene el importe de las actuaciones o asistencias facturadas de la cabecera de guardia
        V_IMPORTEMINIMO_CG NUMBER; -- Contiene el importe minimo de la cabecera de guardia
        V_IMPORTENOFACTURADO_CG NUMBER; -- Contiene el importe de las actuaciones o asistencias sin facturar de la cabecera de guardia

        -- Otras variables
        V_AGRUPACABECERA VARCHAR2(1); -- Contiene el valor configurado que indica si se aplica maximos y minimos por unidad de guardia o por cabecera de guardia (0=UG; 1=CG)                
        V_IMPORTE NUMBER; -- Importe calculado para UG y CG
        V_IMPORTEMAXIMO NUMBER; -- Importe maximo configurado para las asistencias o actuaciones
        V_MOTIVO NUMBER; -- Motivo calculado para UG y CG
        
        -- Variables para el tratamiento de errores
        V_CODRETORNO VARCHAR2(10) := '0';
        V_DATOSERROR VARCHAR2(4000) := NULL;
        E_ERROR_HEREDADO EXCEPTION;

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Inicio';

        -- Obtengo valores de configuracion de la guardia
        V_AGRUPACABECERA := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAX_MIN_UG_CG;
        V_IMPORTEMAXIMO := NVL(P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MAXIMO, 0);
        V_IMPORTEMINIMO_CG := NVL(P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MINIMO, 0);
        
        -- Inicializo el contador de las unidades de guardia y tipos (UG + TP)
        V_CONTADOR_UG_TP := 0;

        -- Inicializo las variables acumuladoras de la cabecera de guardia (CG)        
        V_IMPORTENOFACTURADO_CG := 0;
        V_IMPORTEFACTURADO_CG := 0;
        V_COSTEFIJONOFACTURADO_CG := 0; 

        -- Obtenemos las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin buscando solo los apuntes que no pagan guardia (ignorando tipos y fuera de guardia)
        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: FOR CURSOR_UG_NPG';
        FOR R_UG_NPG IN CURSOR_UG_NPG (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_MATRIZFACTURABLE.FECHAINICIO,
            P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS,
            P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO
        ) LOOP
            
            -- Cambio para saber si estaba facturada la unidad de guardia (UG)
            V_FACTURADA_UG := '1'; -- Por defecto indico que esta facturada
            IF (R_UG_NPG.IMPORTE_FACTURADO_UG = -1) THEN -- El -1 indica que no se ha facturado la unidad de guardia (UG)
                R_UG_NPG.IMPORTE_FACTURADO_UG := 0; -- Modifico el importe por el cero
                V_FACTURADA_UG := '0'; -- Indico que no esta facturada
            END IF;
            
            -- Es necesario poner este minimo aqui para el tratamiento de las derivadas y sus minimos
            -- Ya que puede ser actualizado en cada unidad de guardia, y entonces hay que volver a obtener el valor
            V_IMPORTEMINIMO_UG := NVL(P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MINIMO, 0);                  
            
            -- Incremento el contador de las unidades de guardia y tipos (UG + TP)            
            V_CONTADOR_UG_TP := V_CONTADOR_UG_TP + 1;
            V_CONTADOR_UG := V_CONTADOR_UG_TP; -- Se necesita este contador para guardar el valor de la unidad de guardia con tipos
            
            -- Comprueba si esta configurado la facturacion de la guardia por asistencias aplicando tipos
            IF (P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO = C_AsTp) THEN
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_AsTp';
                PROC_FACT_NOPAGAGUARDIA_AsTp(
                    P_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA,
                    P_DATOSFACTURACION,
                    R_UG_NPG.FECHAINICIO,
                    R_UG_NPG.FECHAFIN,
                    V_CONTADOR_UG_TP,
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_COSTEFIJONOFACTURADO_UG,
                    V_CODRETORNO,
                    V_DATOSERROR); 
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF;                
            
            -- Comprueba si esta configurado la facturacion de la guardia por actuaciones aplicando tipos    
            ELSIF (P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO_TP.NOFACTURADO = C_AcTp) THEN
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_AcTp';
                PROC_FACT_NOPAGAGUARDIA_AcTp(
                    P_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA,
                    P_DATOSFACTURACION,
                    R_UG_NPG.FECHAINICIO,
                    R_UG_NPG.FECHAFIN,
                    V_CONTADOR_UG_TP,
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_COSTEFIJONOFACTURADO_UG,
                    V_CODRETORNO,
                    V_DATOSERROR); 
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF; 
            
            -- Comprueba si esta configurado la facturacion de la guardia por asistencias
            ELSIF (P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO = C_As) THEN
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_As';
                PROC_FACT_NOPAGAGUARDIA_As(
                    P_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA,
                    P_DATOSFACTURACION,
                    R_UG_NPG.FECHAFIN,
                    V_CONTADOR_UG_TP,
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_COSTEFIJONOFACTURADO_UG,
                    V_CODRETORNO,
                    V_DATOSERROR); 
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF;
            
            ELSE -- Esta configurado la facturacion de la guardia por actuaciones (Ac)
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_Ac';
                PROC_FACT_NOPAGAGUARDIA_Ac(
                    P_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA,
                    P_DATOSFACTURACION,
                    R_UG_NPG.FECHAFIN,
                    V_CONTADOR_UG_TP,                    
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_COSTEFIJONOFACTURADO_UG,
                    V_DERIVADA_UG,
                    V_CODRETORNO,
                    V_DATOSERROR); 
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF;
                
                -- ** Cambio para Asistencias que derivan en Designacion **
                -- Cuando un dia tenga asistencias derivadas no se aplica el importe minimo
                IF (V_DERIVADA_UG) THEN 
                    V_IMPORTEMINIMO_UG := 0;
                    V_IMPORTEMINIMO_CG := 0;
                END IF;                                   
            END IF;
            
            /*** TRATAMIENTO UNIDAD DE GUARDIA ***/                                               

            -- Aplican maximos y minimos por unidad de guardia (UG)
            IF (V_AGRUPACABECERA = '0') THEN
            
                -- Obtiene el motivo y el importe de la unidad de guardia (UG)
                P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_MOTIMP para unidades de guardia';
                PROC_FACT_NOPAGAGUARDIA_MOTIMP(
                    P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA,
                    R_UG_NPG.IMPORTE_FACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEMAXIMO,
                    V_IMPORTEMINIMO_UG,
                    P_MATRIZFACTURABLE.IDINSTITUCION,
                    P_MATRIZFACTURABLE.IDTURNO,
                    P_MATRIZFACTURABLE.IDGUARDIA,
                    P_MATRIZFACTURABLE.IDPERSONA,
                    R_UG_NPG.FECHAINICIO,
                    R_UG_NPG.FECHAFIN,
                    NULL, -- IDTIPOASISTENCIACOLEGIO
                    NULL, -- IDTIPOACTUACION
                    V_MOTIVO,
                    V_IMPORTE);                                        
                    
            -- Aplican maximos y minimos por cabecera de guardia (CG)
            ELSIF (V_FACTURADA_UG = '0') THEN -- Unidad de guardia sin facturar (UG)
                V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
                V_IMPORTE := V_IMPORTENOFACTURADO_UG;
            
            ELSE -- Unidad de guardia facturada previamente (UG)
                V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO;
                V_IMPORTE := V_IMPORTENOFACTURADO_UG;
            END IF;
            
            -- Actualizo las variables acumuladoras de las cabeceras de guardia (CG)
            V_IMPORTENOFACTURADO_CG := V_IMPORTENOFACTURADO_CG + V_IMPORTE; 
            V_COSTEFIJONOFACTURADO_CG := V_COSTEFIJONOFACTURADO_CG + V_COSTEFIJONOFACTURADO_UG;
            V_IMPORTEFACTURADO_CG := V_IMPORTEFACTURADO_CG + V_IMPORTEFACTURADO_UG;            

            -- Genero un apunte de la unidad de guardia (UG)
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := R_UG_NPG.FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := R_UG_NPG.FECHAFIN;
            M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJONOFACTURADO_UG;
            M_APUNTE_UG(IND_UG).CONTADOR := V_CONTADOR_UG;
            M_APUNTE_UG(IND_UG).IMPORTE := V_IMPORTE;
            M_APUNTE_UG(IND_UG).MOTIVO := V_MOTIVO;
            M_APUNTE_UG(IND_UG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
        END LOOP; -- Fin CURSOR_UG_NPG
        
        /*** TRATAMIENTO CABECERA DE GUARDIA ***/
        
        -- Aplican maximos y minimos por cabecera de guardia (CG)
        IF (V_AGRUPACABECERA = '1') THEN  
        
            -- Obtiene el motivo y el importe de la cabecera de guardia (UG)
            P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_MOTIMP para cabeceras de guardia';
            PROC_FACT_NOPAGAGUARDIA_MOTIMP(
                P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA,
                P_MATRIZFACTURABLE.IMPORTEFACTURADO, 
                V_IMPORTEFACTURADO_CG,
                V_IMPORTENOFACTURADO_CG,
                V_IMPORTEMAXIMO,
                V_IMPORTEMINIMO_CG,
                P_MATRIZFACTURABLE.IDINSTITUCION,
                P_MATRIZFACTURABLE.IDTURNO,
                P_MATRIZFACTURABLE.IDGUARDIA,
                P_MATRIZFACTURABLE.IDPERSONA,
                P_MATRIZFACTURABLE.FECHAINICIO,
                NULL, -- FECHAFIN
                NULL, -- IDTIPOASISTENCIACOLEGIO
                NULL, -- IDTIPOACTUACION
                V_MOTIVO,
                V_IMPORTE);               

        -- Aplican maximos y minimos por unidad de guardia (UG)
        ELSIF (P_MATRIZFACTURABLE.IMPORTEFACTURADO = 0) THEN -- Cabecera de guardia sin facturar (CG)
            V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.NOFACTURADO;
            V_IMPORTE := V_IMPORTENOFACTURADO_CG;
        
        ELSE -- Cabecera de guardia facturada previamente (CG)
            V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.MOTIVO.FACTURADO;
            V_IMPORTE := V_IMPORTENOFACTURADO_CG;
        END IF;

        -- Genero un apunte de la cabecera de guardia (CG)
        IND_CG := IND_CG + 1;
        M_APUNTE_CG(IND_CG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
        M_APUNTE_CG(IND_CG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
        M_APUNTE_CG(IND_CG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
        M_APUNTE_CG(IND_CG).FECHAINICIO := P_MATRIZFACTURABLE.FECHAINICIO;
        M_APUNTE_CG(IND_CG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG - 1;
        M_APUNTE_CG(IND_CG).COSTEFIJO := V_COSTEFIJONOFACTURADO_CG;
        M_APUNTE_CG(IND_CG).IMPORTE := V_IMPORTE;
        M_APUNTE_CG(IND_CG).MOTIVO := V_MOTIVO;               

        IF (V_AGRUPACABECERA = '1') THEN -- Aplican maximos y minimos por Cabecera de Guardia
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG-';
        ELSE -- Aplican maximos y minimos por unidad de guardia (UG)
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';
        END IF;        

        P_DATOSERROR := 'PROCEDURE PROC_FACT_NOPAGAGUARDIA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR_HEREDADO THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;        
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_NOPAGAGUARDIA;
    
    /****************************************************************************************************************
    Nombre: PROC_FACT_PagaGuardia_MotImp
    Descripcion: Calcula el motivo y el importe (CG + UG + TP)

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_CONTADOR - IN - Contador de asistencias o actuaciones facturadas y sin facturar - NUMBER
    - P_IMPORTEFACTURADO - IN - Importe doblado - NUMBER
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de persona - NUMBER    
    - P_FECHAINICIO - IN - Fecha inicial de la unidad de guardia - DATE
    - P_FECHAFIN - IN - Fecha final de la unidad de guardia - DATE    
    - P_MOTIVO - OUT - Motivo calculado - NUMBER
    - P_IMPORTE - OUT - Importe calculado - NUMBER    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/      
    PROCEDURE PROC_FACT_PAGAGUARDIA_MOTIMP(
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_CONTADOR IN NUMBER,
        P_IMPORTEFACTURADO IN NUMBER,
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_IDINSTITUCION IN FCS_FACT_GUARDIASCOLEGIADO.IDINSTITUCION%TYPE,
        P_IDTURNO IN FCS_FACT_GUARDIASCOLEGIADO.IDTURNO%TYPE,
        P_IDGUARDIA IN FCS_FACT_GUARDIASCOLEGIADO.IDGUARDIA%TYPE,
        P_IDPERSONA IN FCS_FACT_GUARDIASCOLEGIADO.IDPERSONA%TYPE,
        P_FECHAINICIO IN FCS_FACT_GUARDIASCOLEGIADO.FECHAINICIO%TYPE,
        P_FECHAFIN IN FCS_FACT_GUARDIASCOLEGIADO.FECHAFIN%TYPE, -- Para CG es NULL
        P_MOTIVO OUT NUMBER,
        P_IMPORTE OUT NUMBER) IS
        
    BEGIN    
        -- Control para guardias inactivas y que no contienen asistencias o actuaciones 
        IF (P_CONTADOR = 0 -- No tiene As o Ac
            AND P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MINIMO > 0 -- Tiene importe de guardia inactiva
            AND (
                P_DATOSFACTURACION.CONSEJO <> C_CATALAN -- Para los colegios no catalanes no hay que comprobar ESGUARDIAVG 
                OR P_CONFIG_GUARDIA.ESGUARDIAVG = '1' -- Para los catalanes hay que hacer esta comprobacion
            )
        ) THEN        
           P_IMPORTE := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MINIMO - P_IMPORTEFACTURADO;
            
            IF (P_IMPORTEFACTURADO = 0) THEN
                P_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MINIMONOFACTURADO;                
            ELSE
                P_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MINIMOFACTURADO;
            End If;
        
        -- El numero de asistencias o actuaciones NO se dobla     
        ELSIF (P_CONTADOR <= P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DOBLA) THEN
            P_IMPORTE := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTE - P_IMPORTEFACTURADO;

            IF (P_IMPORTEFACTURADO = 0) THEN
                P_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO; -- GAc o GAs
            ELSE
                P_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO; -- GAc+ o GAs+
                
                PROC_FACT_CONTROL_HITO(
                  P_IDINSTITUCION,
                  P_IDTURNO,
                  P_IDGUARDIA,
                  P_IDPERSONA,
                  P_FECHAINICIO,
                  P_FECHAFIN,
                  NULL, -- IDTIPOASISTENCIACOLEGIO
                  NULL, -- IDTIPOACTUACION
                  P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO,
                  P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO,
                  P_IMPORTE);                 
            END IF;                         

        -- El numero de asistencias o actuaciones se dobla
        ELSE
            P_IMPORTE := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.IMPORTEDOBLA - P_IMPORTEFACTURADO;

            IF (P_IMPORTEFACTURADO = 0) THEN
                P_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO; -- GDAc o GDAs              
            ELSE
                P_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMOFACTURADO; -- GDAc+ o GDAs+  
                
                PROC_FACT_CONTROL_HITO(
                  P_IDINSTITUCION,
                  P_IDTURNO,
                  P_IDGUARDIA,
                  P_IDPERSONA,
                  P_FECHAINICIO,
                  P_FECHAFIN,
                  NULL, -- IDTIPOASISTENCIACOLEGIO,
                  NULL, -- IDTIPOACTUACION,
                  P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMONOFACTURADO,
                  P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.MAXIMOFACTURADO,
                  P_IMPORTE);                
            END IF;                         
        END IF;

        -- Control de importes negativos
        IF (P_IMPORTE < 0) THEN
            P_IMPORTE := 0;
        END IF;   
    END PROC_FACT_PAGAGUARDIA_MOTIMP;
    
/****************************************************************************************************************
    Nombre: PROC_FACT_PagaGuardia_AcAs
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por actuaciones o asistencias que paga guardia

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN - Contador de la unidad de guardia actual - NUMBER    
    - P_CONTADOR_AS_AC - OUT - Contador de asistencias o actuaciones facturadas y sin facturar - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_PAGAGUARDIA_AcAs(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN NUMBER,        
        P_CONTADOR_AS_AC OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS                   

        -- Registros para iterar
        R_ACT CURSOR_ACT_AC%ROWTYPE;
        R_AS CURSOR_AS%ROWTYPE;        

        -- Variables AS        
        V_CONTADOR_AS NUMBER;
        V_COSTEFIJONOFACTURADO_AS NUMBER;
        
        -- Variables ACT
        V_CONTADOR_AC NUMBER;
        V_CONTADOR_ACT_FACT NUMBER;
        V_CONTADOR_ACT_NOFACT NUMBER;        

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA_AcAs: Inicio';         

        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        V_CONTADOR_AS := 0;
        V_CONTADOR_AC := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;

        -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
        P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA_AcAs: FOR CURSOR_AS';
        FOR R_AS IN CURSOR_AS (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAFIN_UG,
            P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DIAS,
            P_DATOSFACTURACION.FECHADESDE,
            P_DATOSFACTURACION.FECHAHASTA,
            NULL, -- IDTIPOASISTENCIACOLEGIO
            NULL, -- IDTIPOACTUACION
            P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO
        ) LOOP
        
            -- Inicializo las variables acumuladoras de las asistencias (AS)
            V_COSTEFIJONOFACTURADO_AS := 0; 
            V_CONTADOR_ACT_FACT := 0;
            V_CONTADOR_ACT_NOFACT := 0;           

            -- Obtenemos las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA_AcAs: FOR CURSOR_ACT_AC';
            FOR R_ACT IN CURSOR_ACT_AC (
                P_MATRIZFACTURABLE.IDINSTITUCION,         
                R_AS.ANIO,
                R_AS.NUMERO,
                P_MATRIZFACTURABLE.IDFACTURACION,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA,
                NULL, -- IDTIPOASISTENCIA
                NULL -- IDTIPOACTUACION
            ) LOOP
            
                -- Incremento el contador de actuaciones 
                V_CONTADOR_AC := V_CONTADOR_AC + 1;

                -- Comprueba si NO se ha facturado la actuacion (AC)
                IF (R_ACT.FACTURADO = 0) THEN
                
                    -- Incremento el contador de actuaciaciones sin facturar
                    V_CONTADOR_ACT_NOFACT := V_CONTADOR_ACT_NOFACT + 1;
                
                    -- Actualizo las variables acumuladoras de las asistencias (AS)                     
                    V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;                   
                    
                    -- Genero un apunte de la actuacion (AC)
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                    M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                    M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;                    
                    M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;                    
                    M_APUNTE_AC(IND_AC).IMPORTE := 0;
                    M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO;
                    M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                    
                ELSE -- Actuacion facturada (AC)
                    -- Incremento el contador de actuaciones facturadas
                    V_CONTADOR_ACT_FACT := V_CONTADOR_ACT_FACT + 1;
                END IF;
            END LOOP; -- Fin CURSOR_ACT_AC
            
            /*** TRATAMIENTO ASISTENCIA ***/
            
            -- Incremento el contador de asistencias
            V_CONTADOR_AS := V_CONTADOR_AS + 1;
            
            -- SI (R_AS.FACTURADO = 0) ... es para incluir las asitencias sin facturar (que tienen segun la condicion del cursor actuaciones sin facturar justificadas en el periodo de la facturacion)
            -- SI V_CONTADOR_ACT_NOFACT>0 ... es para incluir las asistencias facturadas con actuaciones sin facturar justificadas en el periodo de la facturacion
            -- Solo no se hace nada para las asistencias facturadas sin actuaciones sin facturar justificadas dentro del periodo de la facturacion
            IF (R_AS.FACTURADO = 0 OR V_CONTADOR_ACT_NOFACT>0) THEN            
            
                -- Actualizo las variables acumuladoras de las unidades de guardia (UG)            
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_AS;            
                
                -- Genero un apunte de la asistencia (AS)
                IND_AS := IND_AS + 1;
                M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;                
                M_APUNTE_AS(IND_AS).IMPORTE := 0;
                M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                                
                -- Comprueba si NO se ha facturado la asistencia (AS)
                IF (R_AS.FACTURADO = 0) THEN
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO;
                    
                ELSE -- Asistencia facturada previamente (AS)
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO;
                END IF;
            END IF;
        END LOOP; -- Fin CURSOR_AS
        
        -- Comprueba si esta configurado la facturacion de la guardia por asistencias
        IF (P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO = C_GAs) THEN
            P_CONTADOR_AS_AC := V_CONTADOR_AS;
        ELSE
            P_CONTADOR_AS_AC := V_CONTADOR_AC;
        END IF;

        P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA_AcAs: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_PAGAGUARDIA_AcAs;
    
  /****************************************************************************************************************
    Nombre: PROC_FACT_PagaGuardia
    Descripcion: Apuntes de cabeceras y unidades de guardia - Facturacion sin pagar guardia

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA - IN - Configuracion de la guardia - CONFIG_GUARDIA (V_CONFIG_GUARDIA)
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/     
    PROCEDURE PROC_FACT_PAGAGUARDIA(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS                    

        --Registros para iterar
        R_UG_PG CURSOR_UG_PG%ROWTYPE;
        
        -- Variables UG
        V_CONTADOR_AS_AC_UG NUMBER; -- Contador asistencias o actuaciones de la unidad de guardia
        V_CONTADOR_UG NUMBER; -- Contiene el contador de la unidad de guardia
        V_COSTEFIJONOFACTURADO_UG NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la unidad de guardia
        V_FACTURADA_UG VARCHAR2(1); -- Contiene si esta facturado la unidad de guardia (0=NoFacturado; 1=Facturado)
        
        -- Variables CG
        V_CONTADOR_AS_AC_CG NUMBER; -- Contador asistencias o actuaciones de la cabecera de guardia         
        V_COSTEFIJONOFACTURADO_CG NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la cabecera de guardia
        V_IMPORTENOFACTURADO_CG NUMBER; -- Contiene el importe de las unidades de guardia de la cabecera de guardia

        -- Otras variables
        V_AGRUPACABECERA VARCHAR2(1); -- Contiene el valor configurado que indica si se aplica maximos y minimos por unidad de guardia o por cabecera de guardia (0=UG; 1=CG)
        V_MOTIVO NUMBER; -- Motivo calculado para UG y CG
        V_IMPORTE NUMBER; -- Importe calculado para UG y CG        
        
        -- Variables para el tratamiento de errores
        V_CODRETORNO VARCHAR2(10) := '0';
        V_DATOSERROR VARCHAR2(4000) := NULL;
        E_ERROR_HEREDADO EXCEPTION;

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA: Inicio';

        -- Obtengo valores de configuracion de la guardia
        V_AGRUPACABECERA := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MAX_MIN_UG_CG;
        
        -- Inicializazo el contador de las unidades de guardia (UG)
        V_CONTADOR_UG := 0;

        -- Inicializo las variables acumuladoras de la cabecera de guardia (CG)        
        V_IMPORTENOFACTURADO_CG := 0;
        V_COSTEFIJONOFACTURADO_CG := 0;
        V_CONTADOR_AS_AC_CG := 0; 

        -- Obtenemos las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin de apuntes que pagan guardia
        P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA: FOR CURSOR_UG_PG';
        FOR R_UG_PG IN CURSOR_UG_PG (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_MATRIZFACTURABLE.FECHAINICIO,
            P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DIAS,
            P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO
        ) LOOP
            
             -- Cambio para saber si estaba facturada UG
            V_FACTURADA_UG := '1';
            IF (R_UG_PG.IMPORTE_FACTURADO_UG = -1) THEN -- -1 indica que no se ha facturado UG
                R_UG_PG.IMPORTE_FACTURADO_UG := 0;
                V_FACTURADA_UG := '0';
            END IF;
            
            -- Incremento el contador de las unidades de guardia (UG)            
            V_CONTADOR_UG := V_CONTADOR_UG + 1;
            
            -- Genera apuntes As + Ac, y gestiona sus contadores
            P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA: Invoco al procedimiento PROC_FACT_PAGAGUARDIA_AcAs';
            PROC_FACT_PAGAGUARDIA_AcAs(
                P_MATRIZFACTURABLE,
                P_CONFIG_GUARDIA,
                P_DATOSFACTURACION,
                R_UG_PG.FECHAFIN,
                V_CONTADOR_UG,
                V_CONTADOR_AS_AC_UG,
                V_COSTEFIJONOFACTURADO_UG,
                V_CODRETORNO,
                V_DATOSERROR); 
            IF V_CODRETORNO <> '0' THEN
                RAISE E_ERROR_HEREDADO;
            END IF;
            
            /*** TRATAMIENTO UNIDAD DE GUARDIA ***/

            -- Actualizo las variables acumuladoras de las cabeceras de guardia (CG)             
            V_COSTEFIJONOFACTURADO_CG := V_COSTEFIJONOFACTURADO_CG + V_COSTEFIJONOFACTURADO_UG;
            V_CONTADOR_AS_AC_CG := V_CONTADOR_AS_AC_CG + V_CONTADOR_AS_AC_UG;                 

            -- Aplican maximos y minimos por unidad de guardia (UG)
            IF (V_AGRUPACABECERA = '0') THEN
            
                P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA: Invoco al procedimiento PROC_FACT_PAGAGUARDIA_MOTIMP para unidades de guardia por asistencias';
                PROC_FACT_PAGAGUARDIA_MOTIMP(
                    P_CONFIG_GUARDIA,
                    V_CONTADOR_AS_AC_UG,
                    R_UG_PG.IMPORTE_FACTURADO_UG,
                    P_DATOSFACTURACION,
                    P_MATRIZFACTURABLE.IDINSTITUCION,
                    P_MATRIZFACTURABLE.IDTURNO,
                    P_MATRIZFACTURABLE.IDGUARDIA,
                    P_MATRIZFACTURABLE.IDPERSONA,
                    R_UG_PG.FECHAINICIO,
                    R_UG_PG.FECHAFIN,                     
                    V_MOTIVO,
                    V_IMPORTE);
                
                -- Actualizo las variables acumuladoras de las cabeceras de guardia (CG)
                V_IMPORTENOFACTURADO_CG := V_IMPORTENOFACTURADO_CG + V_IMPORTE;
                    
            -- Aplican maximos y minimos por cabecera de guardia (CG)
            ELSIF (V_FACTURADA_UG = '0') THEN -- Unidad de guardia sin facturar (UG)
                V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO;
                V_IMPORTE := 0;
            
            ELSE -- Unidad de guardia facturada previamente (UG)
                V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO;
                V_IMPORTE := 0;
            END IF;

            -- Genero un apunte de la unidad de guardia (UG)
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := R_UG_PG.FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := R_UG_PG.FECHAFIN;
            M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJONOFACTURADO_UG;
            M_APUNTE_UG(IND_UG).CONTADOR := V_CONTADOR_UG;
            M_APUNTE_UG(IND_UG).IMPORTE := V_IMPORTE;
            M_APUNTE_UG(IND_UG).MOTIVO := V_MOTIVO;
            M_APUNTE_UG(IND_UG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
        END LOOP; -- Fin CURSOR_UG_PG       
        
        /*** TRATAMIENTO CABECERA DE GUARDIA ***/
        
        -- Aplican maximos y minimos por cabecera de guardia (CG)
        IF (V_AGRUPACABECERA = '1') THEN 
                        
            P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA: Invoco al procedimiento PROC_FACT_PAGAGUARDIA_MOTIMP para unidades de guardia por asistencias';
            PROC_FACT_PAGAGUARDIA_MOTIMP(
                P_CONFIG_GUARDIA,
                V_CONTADOR_AS_AC_UG,
                P_MATRIZFACTURABLE.IMPORTEFACTURADO,
                P_DATOSFACTURACION,
                P_MATRIZFACTURABLE.IDINSTITUCION,
                P_MATRIZFACTURABLE.IDTURNO,
                P_MATRIZFACTURABLE.IDGUARDIA,
                P_MATRIZFACTURABLE.IDPERSONA,
                P_MATRIZFACTURABLE.FECHAINICIO,
                NULL, -- FECHAFIN
                V_MOTIVO,
                V_IMPORTE);

        -- Aplican maximos y minimos por unidad de guardia (UG)
        ELSIF (P_MATRIZFACTURABLE.IMPORTEFACTURADO = 0) THEN -- Cabecera de guardia sin facturar (CG)
            V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.NOFACTURADO;
            V_IMPORTE := V_IMPORTENOFACTURADO_CG;
        
        ELSE -- Cabecera de guardia facturada previamente (CG)
            V_MOTIVO := P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.MOTIVO.FACTURADO;
            V_IMPORTE := V_IMPORTENOFACTURADO_CG;
        END IF;

        -- Genero un apunte de la cabecera de guardia (CG)
        IND_CG := IND_CG + 1;
        M_APUNTE_CG(IND_CG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
        M_APUNTE_CG(IND_CG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
        M_APUNTE_CG(IND_CG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
        M_APUNTE_CG(IND_CG).FECHAINICIO := P_MATRIZFACTURABLE.FECHAINICIO;
        M_APUNTE_CG(IND_CG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG - 1;
        M_APUNTE_CG(IND_CG).COSTEFIJO := V_COSTEFIJONOFACTURADO_CG;
        M_APUNTE_CG(IND_CG).IMPORTE := V_IMPORTE;
        M_APUNTE_CG(IND_CG).MOTIVO := V_MOTIVO;               

        -- Aplican maximos y minimos por Cabecera de Guardia (CG)
        IF (V_AGRUPACABECERA = '1') THEN 
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG-';
        ELSE -- Aplican maximos y minimos por unidad de guardia (UG)
            M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'CG+';
        END IF;

        P_DATOSERROR := 'PROCEDURE PROC_FACT_PAGAGUARDIA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR_HEREDADO THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;        
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_PAGAGUARDIA;
    
 /****************************************************************************************************************
    Nombre: PROC_CARGAR_CABECERA_GUARDIAS
    Descripcion: Este procedimiento se encarga de Cargar la matriz de Cabecera de Guardias

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER    
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CONFIG_GUARDIA - IN - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 20/04/2006 - Autor: Pilar Duran Munoz
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/    
    PROCEDURE PROC_CARGAR_CABECERA_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO       IN NUMBER,
        P_IDGUARDIA     IN NUMBER,
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CONFIG_GUARDIA IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS
        
        --Registros para iterar
        R_CG CURSOR_CG%ROWTYPE;
    
    BEGIN
        P_DATOSERROR := 'PROCEDURE PROC_CARGAR_CABECERA_GUARDIAS: Inicio';
    
        --Inicializamos la matriz de cabecera de guardias
        M_CG_FACTURABLE.DELETE;
        IND_CG_FACTURABLE := 0;

        P_DATOSERROR := 'PROC_CARGAR_CABECERA_GUARDIAS: Cursor CURSOR_CG';
        -- Cargamos en memoria la matriz de cabecera de guardias m_CG_facturables
        FOR R_CG IN CURSOR_CG (
            P_IDINSTITUCION, 
            P_IDTURNO, 
            P_IDGUARDIA,
            P_DATOSFACTURACION.FECHADESDE,
            P_DATOSFACTURACION.FECHAHASTA,
            P_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.CONFIGURACION, -- 0:Actual; 1:Antiguo
            P_CONFIG_GUARDIA.IDFACTURACION
        ) LOOP
            IND_CG_FACTURABLE := IND_CG_FACTURABLE + 1;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDINSTITUCION := P_IDINSTITUCION;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDTURNO := P_IDTURNO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDGUARDIA := P_IDGUARDIA;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDPERSONA := R_CG.IDPERSONA;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FECHAINICIO := R_CG.FECHAINICIO;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).FECHAFIN := R_CG.FECHA_FIN;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IDFACTURACION := R_CG.IDFACTURACION;
            M_CG_FACTURABLE(IND_CG_FACTURABLE).IMPORTEFACTURADO := R_CG.IMPORTEFACTURADO_CG;
        END LOOP;

        P_DATOSERROR := 'PROCEDURE PROC_CARGAR_CABECERA_GUARDIAS: Finalizado correctamente';
        P_CODRETORNO := '0';
    END PROC_CARGAR_CABECERA_GUARDIAS;    
    
 /****************************************************************************************************************
    Nombre: PROC_FACT_FUERAGUARDIA_Ac
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por actuaciones fuera de guardia y sin tipo

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA_ACTUAL - IN - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN - Contador de la unidad de guardia actual - NUMBER    
    - P_IMPORTENOFACTURADO_UG - OUT - Importe acumulado no facturado por la unidad de guardia - NUMBER
    - P_IMPORTEFACTURADO_UG - OUT - Importe acumulado facturado por la unidad de guardia - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_IDFACTURACION_CG - IN OUT - Identificacion de la facturacion de la cabecera de guardias - NUMBER
    - P_IMPORTEMAX_CG - IN OUT - Importe maximo de la cabecera de guardias - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_FUERAGUARDIA_Ac(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA_ACTUAL IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA_ACTUAL
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN NUMBER,        
        P_IMPORTENOFACTURADO_UG OUT NUMBER,
        P_IMPORTEFACTURADO_UG OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_IDFACTURACION_CG IN OUT NUMBER,
        P_IMPORTEMAX_CG IN OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        -- Registros para iterar
        R_ACT CURSOR_ACT_ACFG%ROWTYPE;
        R_AS CURSOR_ASFG%ROWTYPE;        
        
        V_CONFIG_GUARDIA CONFIG_GUARDIA;

        -- Variables AS
        V_COSTEFIJONOFACTURADO_AS NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la asistencia                        
        V_IMPORTEFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones facturadas de la asistencia
        V_IMPORTENOFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones sin facturar de la asistencia
        V_TIENEACTNOFACTURADAS_AS BOOLEAN; -- Indica si la asistencia tiene actuaciones sin facturar
        
        -- Variables AC
        V_IMPORTE_AC NUMBER; -- Contiene el importe configurado para cada actuacion

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_Ac: Inicio';
        
        -- Variables de configuracion de la guardia
        V_IMPORTE_AC := NVL(P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.IMPORTE, 0);
        V_CONFIG_GUARDIA := P_CONFIG_GUARDIA_ACTUAL; -- Inicialmente pongo la configuracion actual

        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        P_IMPORTENOFACTURADO_UG := 0;
        P_IMPORTEFACTURADO_UG := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;

        -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_Ac: FOR CURSOR_ASFG';
        FOR R_AS IN CURSOR_ASFG (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAFIN_UG,
            P_DATOSFACTURACION.FECHADESDE,
            P_DATOSFACTURACION.FECHAHASTA,
            NULL, -- IDTIPOASISTENCIACOLEGIO
            NULL, -- IDTIPOACTUACION
            P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO
        ) LOOP
            
            -- Inicializo las variables acumuladoras de las asistencias (AS)
            V_IMPORTENOFACTURADO_AS := 0;
            V_IMPORTEFACTURADO_AS := 0;
            V_COSTEFIJONOFACTURADO_AS := 0;  
            V_TIENEACTNOFACTURADAS_AS := FALSE;            

            -- Obtenemos las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_Ac: FOR CURSOR_ACT_ACFG';
            FOR R_ACT IN CURSOR_ACT_ACFG (
                P_MATRIZFACTURABLE.IDINSTITUCION,
                P_MATRIZFACTURABLE.IDTURNO,
                P_MATRIZFACTURABLE.IDGUARDIA,
                R_AS.ANIO,
                R_AS.NUMERO,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA,
                NULL, -- IDTIPOASISTENCIA
                NULL, -- IDTIPOACTUACION
                P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION, -- 0:Actual; 1:AcModerna; 2:AcAntigua
                P_MATRIZFACTURABLE.IDFACTURACION -- Es el IDFACTURACION actual (NO el IDFACTURACION de la actuacion)
            ) LOOP          

                -- Comprueba si NO se ha facturado la actuacion (AC)
                IF (R_ACT.FACTURADO = 0) THEN                  
                
                    -- Si ha cambiado la facturacion, tenemos que volver a configurar V_CONFIG_GUARDIA
                    IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION IN (1, 2) -- 0:Actual; 1:AcModerna; 2:AcAntigua
                        AND R_ACT.IDFACTURACION <> V_CONFIG_GUARDIA.IDFACTURACION) THEN                    

                        -- JPT: Procedimiento para configuraciones antiguas que carga V_CONFIG_GUARDIA
                        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a  PROC_CARGA_CONFIG_GUARDIA';
                        PROC_CARGA_CONFIG_GUARDIA(
                            P_MATRIZFACTURABLE.IDINSTITUCION,
                            P_MATRIZFACTURABLE.IDTURNO,
                            P_MATRIZFACTURABLE.IDGUARDIA,
                            P_CONFIG_GUARDIA_ACTUAL.ESGUARDIAVG, -- P_IDTIPOGUARDIA
                            V_CONFIG_GUARDIA,
                            P_CONFIG_GUARDIA_ACTUAL,
                            R_ACT.IDFACTURACION,
                            V_CODRETORNO2,
                            V_DATOSERROR2);
                        IF (V_CODRETORNO2 <> '0') THEN
                            RAISE E_ERROR2;
                        END IF;                   
                        
                        -- Variables de configuracion de la guardia
                        V_IMPORTE_AC := NVL(V_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.IMPORTE, 0);
                        
                        IF (P_IDFACTURACION_CG IS NULL 
                            OR (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 1 AND R_ACT.IDFACTURACION > P_IDFACTURACION_CG)
                            OR (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 2 AND R_ACT.IDFACTURACION < P_IDFACTURACION_CG)) THEN 
                            P_IDFACTURACION_CG := R_ACT.IDFACTURACION;
                            P_IMPORTEMAX_CG := V_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MAXIMO;
                        END IF;
                    END IF;
                
                    -- Actualizo las variables acumuladoras de las asistencias (AS)                     
                    V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;
                    V_TIENEACTNOFACTURADAS_AS := TRUE;
                    
                    -- Genero un apunte de la actuacion (AC)
                    IND_AC := IND_AC + 1;
                    M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                    M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                    M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                    M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                    M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;                                        
                    M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;
                    M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;                                
                    M_APUNTE_AC(IND_AC).IMPORTE := V_IMPORTE_AC;
                    M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO;
                        
                    -- Actualizo las variables acumuladoras de las asistencias (AS)
                    V_IMPORTENOFACTURADO_AS := V_IMPORTENOFACTURADO_AS + V_IMPORTE_AC;

                ELSE -- Actuacion facturada previamente (AC)
                    -- Actualizo las variables acumuladoras de las asistencias (AS)
                    V_IMPORTEFACTURADO_AS := V_IMPORTEFACTURADO_AS + R_ACT.IMPORTE_FACTURADO_ACT;
                END IF;
            END LOOP; -- Fin CURSOR_ACT_ACFG
            
            /*** TRATAMIENTO ASISTENCIA ***/
            
            -- Actualizo las variables acumuladoras de las unidades de guardia (UG)
            P_IMPORTEFACTURADO_UG := P_IMPORTEFACTURADO_UG + V_IMPORTEFACTURADO_AS;
            
            -- Compruebo si la asistencia tiene actuaciones sin facturar
            IF (V_TIENEACTNOFACTURADAS_AS = TRUE) THEN  
            
                -- Actualizo las variables acumuladoras de las unidades de guardia (UG)            
                P_IMPORTENOFACTURADO_UG := P_IMPORTENOFACTURADO_UG + V_IMPORTENOFACTURADO_AS;
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_AS;
                
                -- Genero un apunte de la asistencia (AS)
                IND_AS := IND_AS + 1;
                M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;                
                M_APUNTE_AS(IND_AS).IMPORTE := 0; -- V_IMPORTENOFACTURADO_AS;
                M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                                
                -- Comprueba si NO se ha facturado la asistencia (AS)
                IF (R_AS.FACTURADO = 0) THEN
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO;
                    
                ELSE -- Asistencia facturada previamente (AS)
                    M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.FACTURADO;
                END IF;
            END IF; 
        END LOOP; -- Fin CURSOR_ASFG

        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_Ac: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_FUERAGUARDIA_Ac;
    
/****************************************************************************************************************
    Nombre: PROC_FACT_FUERAGUARDIA_AcTp
    Descripcion: Apuntes de asistencias y actuaciones - Facturacion por actuaciones fuera de guardia y con tipo

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA_ACTUAL - IN - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_FECHAINICIO_UG - IN - Fecha inicial de la unidad de guardia - DATE
    - P_FECHAFIN_UG - IN - Fecha final de la unidad de guardia - DATE
    - P_CONTADOR_UG - IN OUT - Contador de la unidad de guardia actual - NUMBER
    - P_IMPORTENOFACTURADO_UG - OUT - Importe acumulado no facturado por la unidad de guardia - NUMBER
    - P_IMPORTEFACTURADO_UG - OUT - Importe acumulado facturado por la unidad de guardia - NUMBER
    - P_COSTEFIJONOFACTURADO_UG - OUT - Coste fijo acumulado no facturado por la unidad de guardia - NUMBER
    - P_IDFACTURACION_CG - IN OUT - Identificacion de la facturacion de la cabecera de guardias - NUMBER
    - P_IMPORTEMAX_CG - IN OUT - Importe maximo de la cabecera de guardias - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)
        P_CONFIG_GUARDIA_ACTUAL IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA_ACTUAL
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_FECHAINICIO_UG IN DATE,
        P_FECHAFIN_UG IN DATE,
        P_CONTADOR_UG IN OUT NUMBER,
        P_IMPORTENOFACTURADO_UG OUT NUMBER,
        P_IMPORTEFACTURADO_UG OUT NUMBER,
        P_COSTEFIJONOFACTURADO_UG OUT NUMBER,
        P_IDFACTURACION_CG IN OUT NUMBER,
        P_IMPORTEMAX_CG IN OUT NUMBER,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS             
        
        --Registros para iterar        
        R_ACT CURSOR_ACT_ACFG%ROWTYPE;
        R_AS CURSOR_ASFG%ROWTYPE;
        R_TP CURSOR_TIPOS_AC%ROWTYPE;
        
        V_CONFIG_GUARDIA CONFIG_GUARDIA;
        
        -- Variables TP
        V_COSTEFIJONOFACTURADO_TP NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar del tipo
        V_IMPORTE_TP NUMBER; -- Contiene el importe del tipo 
        V_IMPORTEFACTURADO_AC_TP NUMBER; -- Contiene el importe de las actuaciones facturadas del tipo
        V_IMPORTEFACTURADO_TP_TP NUMBER; -- Contiene el importe acumulado del tipo
        V_IMPORTENOFACTURADO_TP NUMBER; -- Contiene el importe de las actuaciones sin facturar del tipo
        V_MAXIMO_TP NUMBER; -- Contiene el importe maximo configurado para cada tipo de actuacion
        V_MAXIMO_TP_AUX NUMBER := NULL; -- Contiene el importe maximo configurado para cada tipo de actuacion 
        V_MOTIVO_TP NUMBER; -- Contiene el motivo del tipo
        V_IDFACTURACION_TP NUMBER; -- Identificador de la facturacion para el tipo
        V_IDFACTURACION_TP_AUX NUMBER; -- Identificador de la facturacion para el tipo
        
        -- Variables AS
        V_COSTEFIJONOFACTURADO_AS NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la asistencia        
        V_IMPORTEFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones facturadas de la asistencia
        V_IMPORTENOFACTURADO_AS NUMBER; -- Contiene el importe de las actuaciones sin facturar de la asistencia
        V_TIENEACTNOFACTURADAS_AS BOOLEAN; -- Indica si la asistencia tiene actuaciones sin facturar        
        
        -- Variables AC
        V_IMPORTE_AC NUMBER; -- Contiene el importe configurado para cada tipo de actuacion        
        
        -- Variables para el tratamiento de errores
        V_CODRETORNO VARCHAR2(10) := '0';
        V_DATOSERROR VARCHAR2(4000) := NULL;
        E_ERROR_HEREDADO EXCEPTION;

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: Inicio';
        
        V_CONFIG_GUARDIA := P_CONFIG_GUARDIA_ACTUAL; -- Inicialmente pongo la configuracion actual
            
        -- Inicializo las variables acumuladoras de las unidades de guardia (UG)
        P_IMPORTENOFACTURADO_UG := 0;
        P_IMPORTEFACTURADO_UG := 0;
        P_COSTEFIJONOFACTURADO_UG := 0;
        
        -- Obtenemos los tipos de actuaciones del colegio
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: FOR CURSOR_TIPOS_AC';
        FOR R_TP IN CURSOR_TIPOS_AC (  
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_FECHAINICIO_UG,
            P_FECHAFIN_UG,
            'S' -- V_FUERAGUARDIA
        ) LOOP    
        
            -- Inicializo las variables acumuladoras de los tipos (TP)
            V_IMPORTENOFACTURADO_TP := 0;
            V_IMPORTEFACTURADO_AC_TP := 0;
            V_COSTEFIJONOFACTURADO_TP := 0;
            P_CONTADOR_UG := P_CONTADOR_UG + 1;
            
            -- Si la configuracion es la actual, solo hay que calcularlo una vez
            IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 0) THEN -- 0:Actual; 1:AcModerna; 2:AcAntigua 
                -- Obtengo el importe y maximo del tipo
                P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_TIPO';
                PROC_FACT_NOPAGAGUARDIA_TIPO(
                    P_MATRIZFACTURABLE.IDINSTITUCION,                        
                    R_TP.IDTIPOASISTENCIA,
                    R_TP.IDTIPOACTUACION,
                    P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA,
                    P_MATRIZFACTURABLE.IDFACTURACION,
                    V_IMPORTE_AC,
                    V_MAXIMO_TP,
                    V_CODRETORNO,
                    V_DATOSERROR);                        
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF;
                
            ELSE
                V_IDFACTURACION_TP := NULL; -- Inicialmente el tipo no tiene IDFACTURACION
                V_IDFACTURACION_TP_AUX := NULL; -- Inicialmente el tipo no tiene IDFACTURACION
                V_MAXIMO_TP := 0; -- Inicialmente no se obtiene el maximo del tipo
            END IF;                            
            
            -- Obtenemos las asistencias facturadas + las asistencias sin facturar con actuaciones sin facturar justificadas en el periodo de facturacion
            P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: FOR CURSOR_ASFG';
            FOR R_AS IN CURSOR_ASFG (
                P_MATRIZFACTURABLE.IDINSTITUCION,
                P_MATRIZFACTURABLE.IDTURNO,
                P_MATRIZFACTURABLE.IDGUARDIA,
                P_MATRIZFACTURABLE.IDPERSONA,
                P_FECHAFIN_UG,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA,
                R_TP.IDTIPOASISTENCIA,
                R_TP.IDTIPOACTUACION,
                P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO_TP.NOFACTURADO
            ) LOOP
            
                V_TIENEACTNOFACTURADAS_AS := FALSE;                   
                
                -- Inicializo las variables acumuladoras de las asistencias (AS)
                V_IMPORTENOFACTURADO_AS := 0;
                V_IMPORTEFACTURADO_AS := 0;
                V_COSTEFIJONOFACTURADO_AS := 0;            

                -- Obtenemos las actuaciones facturadas + las actuaciones sin facturar justificadas en el periodo de facturacion
                P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: FOR CURSOR_ACT_ACFG';
                FOR R_ACT IN CURSOR_ACT_ACFG (
                    P_MATRIZFACTURABLE.IDINSTITUCION,
                    P_MATRIZFACTURABLE.IDTURNO,
                    P_MATRIZFACTURABLE.IDGUARDIA,
                    R_AS.ANIO,
                    R_AS.NUMERO,
                    P_DATOSFACTURACION.FECHADESDE,
                    P_DATOSFACTURACION.FECHAHASTA,
                    R_TP.IDTIPOASISTENCIA,
                    R_TP.IDTIPOACTUACION,
                    P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION, -- 0:Actual; 1:AcModerna; 2:AcAntigua
                    P_MATRIZFACTURABLE.IDFACTURACION -- Es el IDFACTURACION actual
                ) LOOP                                 

                    -- Comprueba si NO se ha facturado la actuacion (AC)
                    IF (R_ACT.FACTURADO = 0) THEN
                    
                        -- Si la configuracion es la actual, solo hay que calcularlo una vez
                        IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION IN (1, 2) -- 0:Actual; 1:AcModerna; 2:AcAntigua
                            AND (V_IDFACTURACION_TP_AUX IS NULL OR V_IDFACTURACION_TP_AUX <> R_ACT.IDFACTURACION)) THEN -- Esto vale para calcular el tipo solo cuando cambia IDFACTURACION
                            
                            V_IDFACTURACION_TP_AUX := R_ACT.IDFACTURACION;
                             
                            -- Obtengo el importe y maximo del tipo
                            P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_TIPO';
                            PROC_FACT_NOPAGAGUARDIA_TIPO(
                                P_MATRIZFACTURABLE.IDINSTITUCION,                           
                                R_TP.IDTIPOASISTENCIA,
                                R_TP.IDTIPOACTUACION,
                                P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA,
                                R_ACT.IDFACTURACION,
                                V_IMPORTE_AC,
                                V_MAXIMO_TP_AUX,
                                V_CODRETORNO,
                                V_DATOSERROR);                        
                            IF V_CODRETORNO <> '0' THEN
                                RAISE E_ERROR_HEREDADO;
                            END IF;
                            
                            /* Si es la primera vez
                                O es 1:AcModerna y el IDFACTURACION obtenido es mayor que el actual
                                O es 2:AcAntigua y el IDFACTURACION obtenido es menor que el actual
                                ... Entonces cambiamos el IDFacturacion actual y el maximo por tipos*/ 
                            IF (V_IDFACTURACION_TP IS NULL 
                                OR (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 1 AND R_ACT.IDFACTURACION > V_IDFACTURACION_TP)
                                OR (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 2 AND R_ACT.IDFACTURACION < V_IDFACTURACION_TP)) THEN
                                V_IDFACTURACION_TP := R_ACT.IDFACTURACION; -- Actualiza el IDFACTURACION del tipo                                 
                                V_MAXIMO_TP := V_MAXIMO_TP_AUX;
                            END IF;                                                        
                        END IF;                     
                    
                        -- Si ha cambiado la facturacion, tenemos que volver a configurar V_CONFIG_GUARDIA
                        IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION IN (1, 2) -- 0:Actual; 1:AcModerna; 2:AcAntigua
                            AND R_ACT.IDFACTURACION <> V_CONFIG_GUARDIA.IDFACTURACION) THEN -- Esto vale para cambiar la configuracion de la guardia cuando cambia el IDFACTURACION

                            -- JPT: Procedimiento para configuraciones antiguas que carga V_CONFIG_GUARDIA
                            V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a  PROC_CARGA_CONFIG_GUARDIA';
                            PROC_CARGA_CONFIG_GUARDIA(
                                P_MATRIZFACTURABLE.IDINSTITUCION,
                                P_MATRIZFACTURABLE.IDTURNO,
                                P_MATRIZFACTURABLE.IDGUARDIA,
                                P_CONFIG_GUARDIA_ACTUAL.ESGUARDIAVG, -- P_IDTIPOGUARDIA
                                V_CONFIG_GUARDIA,
                                P_CONFIG_GUARDIA_ACTUAL,
                                R_ACT.IDFACTURACION,
                                V_CODRETORNO2,
                                V_DATOSERROR2);
                            IF (V_CODRETORNO2 <> '0') THEN
                                RAISE E_ERROR2;
                            END IF;
                            
                            IF (P_IDFACTURACION_CG IS NULL 
                                OR (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 1 AND R_ACT.IDFACTURACION > P_IDFACTURACION_CG)
                                OR (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.CONFIGURACION = 2 AND R_ACT.IDFACTURACION < P_IDFACTURACION_CG)) THEN 
                                P_IDFACTURACION_CG := R_ACT.IDFACTURACION;
                                P_IMPORTEMAX_CG := V_CONFIG_GUARDIA.CONFIG_FUERAGUARDIA.MAXIMO;
                            END IF;                        
                        END IF;                    
                    
                        V_TIENEACTNOFACTURADAS_AS := TRUE;
                    
                        -- Actualizo las variables acumuladoras de las asistencias (AS)
                        V_IMPORTENOFACTURADO_AS := V_IMPORTENOFACTURADO_AS + V_IMPORTE_AC; 
                        V_COSTEFIJONOFACTURADO_AS := V_COSTEFIJONOFACTURADO_AS + R_ACT.COSTEFIJO;                   
                        
                        -- Genero un apunte de la actuacion (AC)
                        IND_AC := IND_AC + 1;
                        M_APUNTE_AC(IND_AC).ANIO := R_AS.ANIO;
                        M_APUNTE_AC(IND_AC).NUMERO := R_AS.NUMERO;
                        M_APUNTE_AC(IND_AC).IDACTUACION := R_ACT.IDACTUACION;
                        M_APUNTE_AC(IND_AC).COSTEFIJO := R_ACT.COSTEFIJO;
                        M_APUNTE_AC(IND_AC).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                        M_APUNTE_AC(IND_AC).FECHAACTUACION := R_ACT.FECHA;
                        M_APUNTE_AC(IND_AC).FECHAJUSTIFICACION := R_ACT.FECHAJUSTIFICACION;
                        M_APUNTE_AC(IND_AC).CONTADOR := P_CONTADOR_UG;
                        M_APUNTE_AC(IND_AC).IMPORTE := V_IMPORTE_AC;
                        M_APUNTE_AC(IND_AC).MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO_TP.NOFACTURADO;
                        M_APUNTE_AC(IND_AC).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;                    

                    ELSE -- Actuacion facturada previamente (AC)
                        -- Actualizo las variables acumuladoras de las asistencias (AS)
                        V_IMPORTEFACTURADO_AS := V_IMPORTEFACTURADO_AS + R_ACT.IMPORTE_FACTURADO_ACT;
                    END IF;
                END LOOP; -- Fin CURSOR_ACT_ACFG
                
                /*** TRATAMIENTO ASISTENCIA ***/
                
                -- Actualizo las variables acumuladoras de los tipos (TP)
                V_IMPORTEFACTURADO_AC_TP := V_IMPORTEFACTURADO_AC_TP + V_IMPORTEFACTURADO_AS;
                
                -- Comprueba que la asistencia tiene actuaciones sin facturar dentro del periodo de la facturacion
                IF (V_TIENEACTNOFACTURADAS_AS = TRUE) THEN
            
                    -- Actualizo las variables acumuladoras de los tipos (TP)            
                    V_IMPORTENOFACTURADO_TP := V_IMPORTENOFACTURADO_TP + V_IMPORTENOFACTURADO_AS;
                    V_COSTEFIJONOFACTURADO_TP := V_COSTEFIJONOFACTURADO_TP + V_COSTEFIJONOFACTURADO_AS;
                    
                    -- Genero un apunte de la asistencia (AS)
                    IND_AS := IND_AS + 1;
                    M_APUNTE_AS(IND_AS).ANIO := R_AS.ANIO;
                    M_APUNTE_AS(IND_AS).NUMERO := R_AS.NUMERO;
                    M_APUNTE_AS(IND_AS).IDPERSONACOLEGIADO := P_MATRIZFACTURABLE.IDPERSONA;
                    M_APUNTE_AS(IND_AS).FECHAHORA := R_AS.FECHAHORA;
                    M_APUNTE_AS(IND_AS).CONTADOR := P_CONTADOR_UG;                
                    M_APUNTE_AS(IND_AS).IMPORTE := 0; -- V_IMPORTENOFACTURADO_AS;
                    M_APUNTE_AS(IND_AS).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                                    
                    -- Comprueba si NO se ha facturado la asistencia (AS)
                    IF (R_AS.FACTURADO = 0) THEN
                        M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO_TP.NOFACTURADO;
                        
                    ELSE -- Asistencia facturada previamente (AS)
                        M_APUNTE_AS(IND_AS).MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO_TP.FACTURADO;
                    END IF;
                END IF;
            END LOOP; -- Fin CURSOR_ASFG
            
            /*** TRATAMIENTO TIPOS ***/
            
            -- Comprueba que no existe el valor previo
            IF (R_TP.IMPORTE_FACTURADO_TP = -1) THEN
                -- Como no tengo el valor, le pongo el actual facturado por las actuaciones
                -- En la facturacion antigua no tenia TP los identificadores necesarios para encontrar el importe de cada tipo
                -- Con esto lo unico que podria no funcionar es el maximo del tipo, si ya facturo antes, podria volver a facturarlo porque no encuentra el hito
                V_IMPORTEFACTURADO_TP_TP := V_IMPORTEFACTURADO_AC_TP;
            ELSE
                V_IMPORTEFACTURADO_TP_TP := R_TP.IMPORTE_FACTURADO_TP;
            END IF;
            
            -- Actualizo las variables acumuladoras de la unidad de guardia (UG)            
            P_IMPORTEFACTURADO_UG := P_IMPORTEFACTURADO_UG + V_IMPORTEFACTURADO_TP_TP;
            
            -- Comprueba que haya algo que facturar
            IF (V_IMPORTENOFACTURADO_TP > 0) THEN
            
                -- Obtiene el motivo y el importe del tipo (TP)
                P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_MOTIMP para unidades de guardia';
                PROC_FACT_NOPAGAGUARDIA_MOTIMP(
                    P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA,
                    V_IMPORTEFACTURADO_TP_TP,
                    V_IMPORTEFACTURADO_AC_TP,
                    V_IMPORTENOFACTURADO_TP,
                    V_MAXIMO_TP,
                    0, -- MINIMO_TP = 0
                    P_MATRIZFACTURABLE.IDINSTITUCION,
                    P_MATRIZFACTURABLE.IDTURNO,
                    P_MATRIZFACTURABLE.IDGUARDIA,
                    P_MATRIZFACTURABLE.IDPERSONA,
                    P_MATRIZFACTURABLE.FECHAINICIO,
                    P_FECHAINICIO_UG,
                    R_TP.IDTIPOASISTENCIA,
                    R_TP.IDTIPOACTUACION,
                    V_MOTIVO_TP,
                    V_IMPORTE_TP);  
                    
                -- Actualizo las variables acumuladoras de la unidad de guardia (UG)            
                P_IMPORTENOFACTURADO_UG := P_IMPORTENOFACTURADO_UG + V_IMPORTE_TP;
                P_COSTEFIJONOFACTURADO_UG := P_COSTEFIJONOFACTURADO_UG + V_COSTEFIJONOFACTURADO_TP;
                
                IF (R_TP.IMPORTE_FACTURADO_TP = -1) THEN
                    -- En la facturacion antigua no tenia TP los identificadores necesarios para encontrar el importe de cada tipo
                    -- Hay que anyadir al importe actual, el acumulado pasado, para que funcione para el futuro
                    -- Saldra un valor raro en TP, pero no se debe acumular para CG 
                    V_IMPORTE_TP := V_IMPORTE_TP + V_IMPORTEFACTURADO_AC_TP;
                END IF;  
                    
                -- Genero un apunte del tipo (TP)
                IND_UG := IND_UG + 1;
                M_APUNTE_UG(IND_UG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
                M_APUNTE_UG(IND_UG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
                M_APUNTE_UG(IND_UG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
                M_APUNTE_UG(IND_UG).FECHAINICIO := P_MATRIZFACTURABLE.FECHAINICIO;
                M_APUNTE_UG(IND_UG).FECHAFIN := P_FECHAINICIO_UG;
                M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJONOFACTURADO_TP;
                M_APUNTE_UG(IND_UG).CONTADOR := P_CONTADOR_UG;
                M_APUNTE_UG(IND_UG).IMPORTE := V_IMPORTE_TP;
                M_APUNTE_UG(IND_UG).MOTIVO := V_MOTIVO_TP;
                M_APUNTE_UG(IND_UG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
                M_APUNTE_UG(IND_UG).IDTIPOASISTENCIACOLEGIO := R_TP.IDTIPOASISTENCIA;
                M_APUNTE_UG(IND_UG).IDTIPOACTUACION := R_TP.IDTIPOACTUACION;
            END IF;                                     
        END LOOP; -- Fin CURSOR_TIPOS_AC                              

        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA_AcTp: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR_HEREDADO THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;          
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_FUERAGUARDIA_AcTp;
    
 /****************************************************************************************************************
    Nombre: PROC_FACT_FUERAGUARDIA
    Descripcion: Apuntes de cabeceras y unidades de guardia - Facturacion fuera de guardia

    Parametros (IN/OUT - Descripcion - Tipo de Datos)
    - P_MATRIZFACTURABLE - IN - Array de cabeceras de guardia facturables - MATRICE_CG_FACTURABLE [M_CG_FACTURABLE(INDICEMATRIZFACTURABLE)]
    - P_CONFIG_GUARDIA_ACTUAL - IN - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FACT_FUERAGUARDIA(
        P_MATRIZFACTURABLE IN MATRICE_CG_FACTURABLE, -- V_MATRIZFACTURABLE
        P_CONFIG_GUARDIA_ACTUAL IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA_ACTUAL
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS                       

        --Registros para iterar
        R_UG_NPG CURSOR_UG_FG%ROWTYPE;
        
        -- Variables UG
        V_COSTEFIJONOFACTURADO_UG NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la unidad de guardia
        V_FACTURADA_UG VARCHAR2(1); -- Contiene si esta facturado la unidad de guardia (0=NoFacturado; 1=Facturado)
        V_IMPORTEFACTURADO_UG NUMBER; -- Contiene el importe de las actuaciones o asistencias facturadas de la unidad de guardia
        V_IMPORTENOFACTURADO_UG NUMBER; -- Contiene el importe de las actuaciones o asistencias sin facturar de la unidad de guardia
        V_CONTADOR_UG NUMBER; -- Contiene el contador de la unidad de guardia
        V_CONTADOR_UG_TP NUMBER; -- Contiene el contador de la unidad de guardia y tipos
        
        -- Variables CG        
        V_COSTEFIJONOFACTURADO_CG NUMBER; -- Contiene el coste fijo de las actuaciones sin facturar de la cabecera de guardia
        V_IMPORTEFACTURADO_CG NUMBER; -- Contiene el importe de las actuaciones o asistencias facturadas de la cabecera de guardia
        V_IMPORTENOFACTURADO_CG NUMBER; -- Contiene el importe de las actuaciones o asistencias sin facturar de la cabecera de guardia
        V_IDFACTURACION_CG NUMBER;
        V_IMPORTEMAX_CG NUMBER;

        -- Otras variables                
        V_IMPORTE NUMBER; -- Importe calculado para UG y CG
        V_MOTIVO NUMBER; -- Motivo calculado para UG y CG
        
        -- Variables para el tratamiento de errores
        V_CODRETORNO VARCHAR2(10) := '0';
        V_DATOSERROR VARCHAR2(4000) := NULL;
        E_ERROR_HEREDADO EXCEPTION;

    Begin
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA: Inicio';

        -- Obtengo valores de configuracion de la guardia
        V_IDFACTURACION_CG := NULL; -- Es importante el NULL para indicar que inicialmente no tiene IDFACTURACION, aunque esta solo sirve para configuraciones antiguas
        V_IMPORTEMAX_CG := NVL(P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MAXIMO, 0);
        
        -- Inicializo el contador de las unidades de guardia y tipos (UG + TP)
        V_CONTADOR_UG_TP := 0;

        -- Inicializo las variables acumuladoras de la cabecera de guardia (CG)        
        V_IMPORTENOFACTURADO_CG := 0;
        V_IMPORTEFACTURADO_CG := 0;
        V_COSTEFIJONOFACTURADO_CG := 0; 

        -- Cursor para obtener las unidades de guardia realizadas en la Cabecera de Guardia ordenadas por fecha_fin buscando solo los apuntes fuera de guardia (ignorando tipos)
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA: FOR CURSOR_UG_FG';
        FOR R_UG_NPG IN CURSOR_UG_FG (
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_MATRIZFACTURABLE.FECHAINICIO
        ) LOOP
            
            -- Cambio para saber si estaba facturada la unidad de guardia (UG)
            V_FACTURADA_UG := '1'; -- Por defecto indico que esta facturada
            IF (R_UG_NPG.IMPORTE_FACTURADO_UG = -1) THEN -- El -1 indica que no se ha facturado la unidad de guardia (UG)
                R_UG_NPG.IMPORTE_FACTURADO_UG := 0; -- Modifico el importe por el cero
                V_FACTURADA_UG := '0'; -- Indico que no esta facturada
            END IF;                  
            
            -- Incremento el contador de las unidades de guardia y tipos (UG + TP)            
            V_CONTADOR_UG_TP := V_CONTADOR_UG_TP + 1;
            V_CONTADOR_UG := V_CONTADOR_UG_TP; -- Se necesita este contador para guardar el valor de la unidad de guardia con tipos
            
            -- Comprueba si esta configurado la facturacion de la guardia por asistencias aplicando tipos
            IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO_TP.NOFACTURADO = C_AcFGTp) THEN
                P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA: Invoco al procedimiento PROC_FACT_FUERAGUARDIA_AcTp';
                PROC_FACT_FUERAGUARDIA_AcTp(
                    P_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA_ACTUAL,
                    P_DATOSFACTURACION,
                    R_UG_NPG.FECHAINICIO,
                    R_UG_NPG.FECHAFIN,
                    V_CONTADOR_UG_TP,
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_COSTEFIJONOFACTURADO_UG,
                    V_IDFACTURACION_CG,
                    V_IMPORTEMAX_CG,
                    V_CODRETORNO,
                    V_DATOSERROR); 
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF; 
            
            ELSE -- Esta configurado la facturacion de la guardia por actuaciones (Ac)
                P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA: Invoco al procedimiento PROC_FACT_FUERAGUARDIA_Ac';
                PROC_FACT_FUERAGUARDIA_Ac(
                    P_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA_ACTUAL,
                    P_DATOSFACTURACION,
                    R_UG_NPG.FECHAFIN,
                    V_CONTADOR_UG_TP,                    
                    V_IMPORTENOFACTURADO_UG,
                    V_IMPORTEFACTURADO_UG,
                    V_COSTEFIJONOFACTURADO_UG,
                    V_IDFACTURACION_CG,
                    V_IMPORTEMAX_CG,
                    V_CODRETORNO,
                    V_DATOSERROR); 
                IF V_CODRETORNO <> '0' THEN
                    RAISE E_ERROR_HEREDADO;
                END IF;                          
            END IF;
            
            /*** TRATAMIENTO UNIDAD DE GUARDIA ***/                                               

            -- En fuera de guardia solo aplica maximos por CG
            IF (V_FACTURADA_UG = '0') THEN -- Unidad de guardia sin facturar (UG)
                V_MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO;
                V_IMPORTE := V_IMPORTENOFACTURADO_UG;
            
            ELSE -- Unidad de guardia facturada previamente (UG)
                V_MOTIVO := P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.FACTURADO;
                V_IMPORTE := V_IMPORTENOFACTURADO_UG;
            END IF;
            
            -- Actualizo las variables acumuladoras de las cabeceras de guardia (CG)
            V_IMPORTENOFACTURADO_CG := V_IMPORTENOFACTURADO_CG + V_IMPORTE; 
            V_COSTEFIJONOFACTURADO_CG := V_COSTEFIJONOFACTURADO_CG + V_COSTEFIJONOFACTURADO_UG;
            V_IMPORTEFACTURADO_CG := V_IMPORTEFACTURADO_CG + V_IMPORTEFACTURADO_UG;            

            -- Genero un apunte de la unidad de guardia (UG)
            IND_UG := IND_UG + 1;
            M_APUNTE_UG(IND_UG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
            M_APUNTE_UG(IND_UG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
            M_APUNTE_UG(IND_UG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
            M_APUNTE_UG(IND_UG).FECHAINICIO := R_UG_NPG.FECHAINICIO;
            M_APUNTE_UG(IND_UG).FECHAFIN := R_UG_NPG.FECHAFIN;
            M_APUNTE_UG(IND_UG).COSTEFIJO := V_COSTEFIJONOFACTURADO_UG;
            M_APUNTE_UG(IND_UG).CONTADOR := V_CONTADOR_UG;
            M_APUNTE_UG(IND_UG).IMPORTE := V_IMPORTE;
            M_APUNTE_UG(IND_UG).MOTIVO := V_MOTIVO;
            M_APUNTE_UG(IND_UG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG;
        END LOOP; -- Fin CURSOR_UG_FG
        
        /*** TRATAMIENTO CABECERA DE GUARDIA ***/
        
        -- En fuera de guardia solo aplica maximos por CG  
        
        -- Obtiene el motivo y el importe de la cabecera de guardia (UG)
        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA: Invoco al procedimiento PROC_FACT_NOPAGAGUARDIA_MOTIMP para cabeceras de guardia';
        PROC_FACT_NOPAGAGUARDIA_MOTIMP(
            P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA,
            P_MATRIZFACTURABLE.IMPORTEFACTURADO, 
            V_IMPORTEFACTURADO_CG,
            V_IMPORTENOFACTURADO_CG,
            V_IMPORTEMAX_CG,
            0, -- P_MINIMO, no tiene minimo en fuera de guardia
            P_MATRIZFACTURABLE.IDINSTITUCION,
            P_MATRIZFACTURABLE.IDTURNO,
            P_MATRIZFACTURABLE.IDGUARDIA,
            P_MATRIZFACTURABLE.IDPERSONA,
            P_MATRIZFACTURABLE.FECHAINICIO,
            NULL, -- FECHAFIN,
            NULL, -- IDTIPOASISTENCIACOLEGIO
            NULL, -- IDTIPOACTUACION
            V_MOTIVO,
            V_IMPORTE);               

        -- Genero un apunte de la cabecera de guardia (CG)
        IND_CG := IND_CG + 1;
        M_APUNTE_CG(IND_CG).IDTURNO := P_MATRIZFACTURABLE.IDTURNO;
        M_APUNTE_CG(IND_CG).IDGUARDIA := P_MATRIZFACTURABLE.IDGUARDIA;
        M_APUNTE_CG(IND_CG).IDPERSONA := P_MATRIZFACTURABLE.IDPERSONA;
        M_APUNTE_CG(IND_CG).FECHAINICIO := P_MATRIZFACTURABLE.FECHAINICIO;
        M_APUNTE_CG(IND_CG).IDAPUNTE := P_DATOSFACTURACION.IDAPUNTE + IND_CG - 1;
        M_APUNTE_CG(IND_CG).COSTEFIJO := V_COSTEFIJONOFACTURADO_CG;
        M_APUNTE_CG(IND_CG).IMPORTE := V_IMPORTE;
        M_APUNTE_CG(IND_CG).MOTIVO := V_MOTIVO;               
        M_APUNTE_CG(IND_CG).IDTIPOAPUNTE := 'FG';

        P_DATOSERROR := 'PROCEDURE PROC_FACT_FUERAGUARDIA: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN E_ERROR_HEREDADO THEN
                P_CODRETORNO := V_CODRETORNO;
                P_DATOSERROR := V_DATOSERROR;        
        
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' - ' || SQLERRM;
    END PROC_FACT_FUERAGUARDIA;
    
  /****************************************************************************************************************
    Nombre: PROC_CARGAR_CABGUARDIASFG
    Descripcion: Procedimiento que obtiene las cabeceras de guardia de actuaciones fuera de guardia

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CONFIG_GUARDIA_ACTUAL - IN - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA    
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valenciano
  ****************************************************************************************************************/
    PROCEDURE PROC_FACT_FG_CG_TOTAL(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CONFIG_GUARDIA_ACTUAL IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA_ACTUAL
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        --Registros para iterar
        R_CG_FG CURSOR_CG_FG%ROWTYPE;
        
        V_MATRIZFACTURABLE MATRICE_CG_FACTURABLE; 

    BEGIN
        P_DATOSERROR := 'PROC_FACT_FG_CG_TOTAL: Inicio';
        -- Comprueba si esta configurado para facturar fuera de guardia
        IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO IS NOT NULL) THEN

            -- Datos iniciales del record que usaremos posteriormente        
            V_MATRIZFACTURABLE.IDINSTITUCION := P_IDINSTITUCION;
            V_MATRIZFACTURABLE.IDTURNO := P_IDTURNO;
            V_MATRIZFACTURABLE.IDGUARDIA := P_IDGUARDIA;
            V_MATRIZFACTURABLE.IDFACTURACION := P_CONFIG_GUARDIA_ACTUAL.IDFACTURACION; -- Es el IDFACTURACION actual
    
            P_DATOSERROR := 'PROC_FACT_FG_CG_TOTAL: Cursor CURSOR_CG_FG';    
            FOR R_CG_FG IN CURSOR_CG_FG(
                P_IDINSTITUCION, 
                P_IDTURNO, 
                P_IDGUARDIA,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA
            ) LOOP
                V_MATRIZFACTURABLE.IDPERSONA := R_CG_FG.IDPERSONA;
                V_MATRIZFACTURABLE.FECHAINICIO := R_CG_FG.FECHAINICIO;
                V_MATRIZFACTURABLE.FECHAFIN := R_CG_FG.FECHA_FIN;
                V_MATRIZFACTURABLE.IMPORTEFACTURADO := R_CG_FG.IMPORTEFACTURADO_CG;
                
                V_DATOSERROR2 := 'PROC_FACT_FG_CG_TOTAL: Llamada a PROC_FACT_FUERAGUARDIA';
                PROC_FACT_FUERAGUARDIA(
                    V_MATRIZFACTURABLE,
                    P_CONFIG_GUARDIA_ACTUAL,
                    P_DATOSFACTURACION,
                    V_CODRETORNO2,
                    V_DATOSERROR2);
                IF (V_CODRETORNO2 <> '0') THEN
                    RAISE E_ERROR2;
                END IF;                    
            END LOOP;
        END IF;

        P_DATOSERROR := 'PROCEDURE PROC_FACT_FG_CG_TOTAL: Finalizado correctamente';
        P_CODRETORNO := '0';
  END PROC_FACT_FG_CG_TOTAL;
  
    /****************************************************************************************************************
    Nombre: PROC_CARGAR_CAB_FG_PARTIDA
    Descripcion: Procedimiento que obtiene las cabeceras de guardia de actuaciones fuera de guardia

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_DATOSFACTURACION - IN - Datos de la facturacion de justicia gratuita - DATOS_FACTURACION (V_DATOS_FACTURACION)
    - P_CONFIG_GUARDIA_ACTUAL - IN - RECORD que contiene el valor en memoria de la configuracion de la guardia - CONFIG_GUARDIA
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valenciano
  ****************************************************************************************************************/
    PROCEDURE PROC_FACT_FG_CG_PARTIDA(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_DATOSFACTURACION IN DATOS_FACTURACION, -- V_DATOS_FACTURACION
        P_CONFIG_GUARDIA_ACTUAL IN CONFIG_GUARDIA, -- V_CONFIG_GUARDIA_ACTUAL
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        --Registros para iterar
        R_CG_FG_PARTIDA CURSOR_CG_FG_PARTIDA%ROWTYPE;
        
        V_MATRIZFACTURABLE MATRICE_CG_FACTURABLE;
         
        V_FECHAINICIO SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE;
        V_FECHA_FIN SCS_CABECERAGUARDIAS.FECHA_FIN%TYPE := NULL; -- Es importante este nulo para la primera vez del bucle
        V_IDPERSONA_AUX SCS_CABECERAGUARDIAS.IDPERSONA%TYPE := NULL;
        V_IMPORTE_FACTURADO_CG NUMBER;

    BEGIN
        P_DATOSERROR := 'PROC_FACT_FG_CG_PARTIDA: Inicio';      
        
        -- Comprueba si esta configurado para facturar fuera de guardia
        IF (P_CONFIG_GUARDIA_ACTUAL.CONFIG_FUERAGUARDIA.MOTIVO.NOFACTURADO IS NOT NULL) THEN            

            -- Datos iniciales del record que usaremos posteriormente        
            V_MATRIZFACTURABLE.IDINSTITUCION := P_IDINSTITUCION;
            V_MATRIZFACTURABLE.IDTURNO := P_IDTURNO;
            V_MATRIZFACTURABLE.IDGUARDIA := P_IDGUARDIA;
            V_MATRIZFACTURABLE.IDFACTURACION := P_CONFIG_GUARDIA_ACTUAL.IDFACTURACION; -- Es el IDFACTURACION actual

            P_DATOSERROR := 'PROC_FACT_FG_CG_PARTIDA: Cursor CURSOR_CG_FG_PARTIDA';
            FOR R_CG_FG_PARTIDA IN CURSOR_CG_FG_PARTIDA(
                P_IDINSTITUCION, 
                P_IDTURNO, 
                P_IDGUARDIA,
                P_DATOSFACTURACION.FECHADESDE,
                P_DATOSFACTURACION.FECHAHASTA
            ) LOOP            
            
                -- IS NULL indica que es la primera vez o que no ha encontrado la anterior
                -- O cambia de persona
                -- O cambia de fecha, como esta ordenado de forma ascendente solo puede ir a mayor
                IF (V_FECHA_FIN IS NULL 
                    OR V_IDPERSONA_AUX <> R_CG_FG_PARTIDA.IDPERSONACOLEGIADO
                    OR R_CG_FG_PARTIDA.FECHA_ASI > V_FECHA_FIN) THEN
                    BEGIN               
                    
                        V_IDPERSONA_AUX := R_CG_FG_PARTIDA.IDPERSONACOLEGIADO;
                    
                        -- Obtiene la cabecera validada
                        -- Aunque el IDTIPOAPUNTE nuevo solo es 'FG', antiguamente para AcFGTp se usaba 'FGTp'
                        SELECT CG.FECHAINICIO, 
                                CG.FECHA_FIN,
                                (
                                    SELECT NVL(SUM(NVL(FAC_CG.PRECIOAPLICADO,0)),0)
                                    FROM FCS_FACT_APUNTE FAC_CG
                                    WHERE FAC_CG.IDINSTITUCION = CG.IDINSTITUCION
                                        AND FAC_CG.IDTURNO = CG.IDTURNO
                                        AND FAC_CG.IDGUARDIA = CG.IDGUARDIA
                                        AND FAC_CG.IDPERSONA = CG.IDPERSONA
                                        AND TRUNC(FAC_CG.FECHAINICIO) = CG.FECHAINICIO
                                        AND FAC_CG.IDTIPOAPUNTE IN ('FG', 'FGTp') -- Solo consulto los apuntes de fuera de guardia
                                ) AS IMPORTEFACTURADO_CG
                            INTO V_FECHAINICIO, 
                                V_FECHA_FIN,
                                V_IMPORTE_FACTURADO_CG
                        FROM SCS_CABECERAGUARDIAS CG
                        WHERE CG.IDINSTITUCION = P_IDINSTITUCION
                            AND CG.IDTURNO = P_IDTURNO
                            AND CG.IDGUARDIA = P_IDGUARDIA
                            AND CG.IDPERSONA = V_IDPERSONA_AUX
                            AND R_CG_FG_PARTIDA.FECHA_ASI BETWEEN CG.FECHAINICIO AND CG.FECHA_FIN
                            AND CG.VALIDADO = '1'; -- Validada                        
                            
                            -- Compruebo si es la misma cabecera            
                        IF (V_MATRIZFACTURABLE.FECHAINICIO IS NULL 
                            OR V_MATRIZFACTURABLE.FECHAINICIO <> V_FECHAINICIO
                             OR V_MATRIZFACTURABLE.FECHAFIN <> V_FECHA_FIN
                            OR V_MATRIZFACTURABLE.IDPERSONA <> V_IDPERSONA_AUX) THEN                     
                            
                            -- Si entra aqui es una nueva cabecera                                                        
                            V_MATRIZFACTURABLE.FECHAINICIO := V_FECHAINICIO;
                            V_MATRIZFACTURABLE.FECHAFIN := V_FECHA_FIN;
                            V_MATRIZFACTURABLE.IDPERSONA := V_IDPERSONA_AUX;
                            V_MATRIZFACTURABLE.IMPORTEFACTURADO := V_IMPORTE_FACTURADO_CG;                            
                            
                            V_DATOSERROR2 := 'PROC_FACT_FG_CG_PARTIDA: Llamada a PROC_FACT_FUERAGUARDIA';
                            PROC_FACT_FUERAGUARDIA(
                                V_MATRIZFACTURABLE,
                                P_CONFIG_GUARDIA_ACTUAL,
                                P_DATOSFACTURACION,
                                V_CODRETORNO2,
                                V_DATOSERROR2);
                            IF (V_CODRETORNO2 <> '0') THEN
                                RAISE E_ERROR2;
                            END IF;                                                    
                        END IF;
                            
                        EXCEPTION
                            WHEN OTHERS THEN
                               V_FECHA_FIN := NULL; -- Paso a la siguiente porque no la encuentra
                    END;
                END IF;       
            END LOOP;
        END IF;

        P_DATOSERROR := 'PROCEDURE PROC_FACT_FG_CG_PARTIDA: Finalizado correctamente';
        P_CODRETORNO := '0';
  END PROC_FACT_FG_CG_PARTIDA;
  
  /****************************************************************************************************************
    Nombre: PROC_FCS_MARCAR_FACTURADOS
    Descripcion: Marca todos los registros de cabeceras guardias, asistencias y actuaciones utilizados en la facturacion como facturados

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 19/01/2010 - alvador Madrid-Salvador
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/  
    PROCEDURE PROC_FCS_MARCAR_FACTURADOS(
        P_IDINSTITUCION IN VARCHAR2,
        P_IDFACTURACION IN VARCHAR2,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2
    ) IS

    BEGIN
        P_DATOSERROR := 'PROCEDURE PROC_FCS_MARCAR_FACTURADOS: Inicio';
        
        UPDATE SCS_CABECERAGUARDIAS CAB
        SET CAB.FACTURADO = 1, 
            CAB.IDFACTURACION = P_IDFACTURACION            
        WHERE CAB.IDINSTITUCION = P_IDINSTITUCION
            AND CAB.IDFACTURACION IS NULL
            AND EXISTS (
                SELECT 1
                FROM FCS_FACT_APUNTE FCAB
                WHERE FCAB.IDINSTITUCION = CAB.IDINSTITUCION
                    AND FCAB.IDTURNO = CAB.IDTURNO
                    AND FCAB.IDGUARDIA = CAB.IDGUARDIA
                    AND FCAB.IDPERSONA = CAB.IDPERSONA
                    AND FCAB.FECHAINICIO = CAB.FECHAINICIO
                    AND FCAB.IDFACTURACION = P_IDFACTURACION
            );
            
        UPDATE SCS_GUARDIASCOLEGIADO GUA
        SET GUA.FACTURADO = 1, 
            GUA.IDFACTURACION = P_IDFACTURACION            
        WHERE GUA.IDINSTITUCION = P_IDINSTITUCION
            AND GUA.IDFACTURACION IS NULL
            AND EXISTS (
                SELECT 1
                FROM FCS_FACT_GUARDIASCOLEGIADO FGUA
                WHERE FGUA.IDINSTITUCION = GUA.IDINSTITUCION
                    AND FGUA.IDTURNO = GUA.IDTURNO
                    AND FGUA.IDGUARDIA = GUA.IDGUARDIA
                    AND FGUA.IDPERSONA = GUA.IDPERSONA
                    AND FGUA.FECHAINICIO = GUA.FECHAINICIO
                    AND FGUA.FECHAFIN = GUA.FECHAFIN
                    AND FGUA.IDFACTURACION = P_IDFACTURACION
            );
            
        UPDATE SCS_ASISTENCIA ASI
        SET ASI.FACTURADO = 1, 
            ASI.IDFACTURACION = P_IDFACTURACION            
        WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
            AND ASI.IDFACTURACION IS NULL
            AND EXISTS (
                SELECT 1
                FROM FCS_FACT_ASISTENCIA FASI
                WHERE FASI.IDINSTITUCION = ASI.IDINSTITUCION
                    AND FASI.ANIO = ASI.ANIO
                    AND FASI.NUMERO = ASI.NUMERO
                    AND FASI.IDFACTURACION = P_IDFACTURACION
            );
            
        UPDATE SCS_ACTUACIONASISTENCIA ACT
        SET ACT.FACTURADO = 1, 
            ACT.IDFACTURACION = P_IDFACTURACION            
        WHERE ACT.IDINSTITUCION = P_IDINSTITUCION
            AND ACT.IDFACTURACION IS NULL
            AND EXISTS (
                SELECT 1
                FROM FCS_FACT_ACTUACIONASISTENCIA FACTU
                WHERE FACTU.IDINSTITUCION = ACT.IDINSTITUCION
                    AND FACTU.ANIO = ACT.ANIO
                    AND FACTU.NUMERO = ACT.NUMERO
                    AND FACTU.IDACTUACION = ACT.IDACTUACION
                    AND FACTU.IDFACTURACION = P_IDFACTURACION                    
            );            

        P_DATOSERROR := 'PROCEDURE PROC_FCS_MARCAR_FACTURADOS: Finalizado correctamente';
        P_CODRETORNO := '0';
        
        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;        
    END PROC_FCS_MARCAR_FACTURADOS;   
  
  /****************************************************************************************************************
    Nombre: PROC_FACT_DESC_MATR_GUARDIA
    Descripcion: Descargar las tablas de apuntes las matrices de guardias y vaciar las matrices

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_TOTAL_FACTURACION - IN OUT - Acumulador del total de la facturacion - NUMBER
    - P_USUMODIFICACION - IN - Usuario modificador - NUMBER
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 27/04/2006 - Raul Gonzalez Gonzalez 
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
  ****************************************************************************************************************/   
    PROCEDURE PROC_FACT_DESC_MATR_GUARDIA(
        P_IDINSTITUCION   IN FCS_FACT_APUNTE.IDINSTITUCION%TYPE,
        P_IDFACTURACION   IN FCS_FACT_APUNTE.IDFACTURACION%TYPE,
        P_TOTAL_FACTURACION IN OUT NUMBER,
        P_USUMODIFICACION IN FCS_FACT_APUNTE.USUMODIFICACION%TYPE,        
        P_CODRETORNO      OUT VARCHAR2,
        P_DATOSERROR      OUT VARCHAR2) IS

        V_CODRETORNO VARCHAR2(10);
        V_DATOSERROR VARCHAR2(200);

    BEGIN  

        V_DATOSERROR := 'PROC_FACT_DESC_MATR_GUARDIA: Tratando cabeceras de guardia';
        FOR I IN 1 .. IND_CG LOOP

            INSERT INTO FCS_FACT_APUNTE (
                IDINSTITUCION,
                IDFACTURACION,
                IDAPUNTE,
                IDPERSONA,
                IDTURNO,
                IDGUARDIA,
                FECHAINICIO,
                IDHITO,
                MOTIVO,
                PRECIOAPLICADO,
                FECHAMODIFICACION,
                USUMODIFICACION,
                PRECIOCOSTESFIJOS,
                IDTIPOAPUNTE
            ) VALUES (
                P_IDINSTITUCION,
                P_IDFACTURACION,
                M_APUNTE_CG(I).IDAPUNTE,
                M_APUNTE_CG(I).IDPERSONA,
                M_APUNTE_CG(I).IDTURNO,
                M_APUNTE_CG(I).IDGUARDIA,
                M_APUNTE_CG(I).FECHAINICIO,
                M_APUNTE_CG(I).MOTIVO,
                FUN_DESCRIPCION_IDHITO(M_APUNTE_CG(I).MOTIVO),
                NVL(M_APUNTE_CG(I).IMPORTE, 0.0),
                SYSDATE,
                P_USUMODIFICACION,
                NVL(M_APUNTE_CG(I).COSTEFIJO, 0.0),
                M_APUNTE_CG(I).IDTIPOAPUNTE);

            P_TOTAL_FACTURACION := P_TOTAL_FACTURACION + NVL(M_APUNTE_CG(I).IMPORTE,0) + NVL(M_APUNTE_CG(I).COSTEFIJO,0);
        END LOOP;

        V_DATOSERROR := 'PROC_FACT_DESC_MATR_GUARDIA: Tratando dias de guardia';
        FOR I IN 1 .. IND_UG LOOP

            INSERT INTO FCS_FACT_GUARDIASCOLEGIADO(
                IDINSTITUCION,
                IDFACTURACION,
                IDAPUNTE,
                IDTURNO,
                IDGUARDIA,
                FECHAINICIO,
                IDPERSONA,
                PRECIOAPLICADO,
                FECHAMODIFICACION,
                USUMODIFICACION,
                IDTIPO,
                PRECIOCOSTESFIJOS,
                IDHITO,
                MOTIVO,
                FECHAFIN,
                IDTIPOASISTENCIACOLEGIO,
                IDTIPOACTUACION
            ) VALUES (
                P_IDINSTITUCION,
                P_IDFACTURACION,
                M_APUNTE_UG(I).IDAPUNTE,
                M_APUNTE_UG(I).IDTURNO,
                M_APUNTE_UG(I).IDGUARDIA,
                M_APUNTE_UG(I).FECHAINICIO,
                M_APUNTE_UG(I).IDPERSONA,
                NVL(M_APUNTE_UG(I).IMPORTE, 0.0),
                SYSDATE,
                P_USUMODIFICACION,
                M_APUNTE_UG(I).CONTADOR,
                NVL(M_APUNTE_UG(I).COSTEFIJO, 0.0),
                M_APUNTE_UG(I).MOTIVO,
                FUN_DESCRIPCION_IDHITO(M_APUNTE_UG(I).MOTIVO),
                M_APUNTE_UG(I).FECHAFIN,
                M_APUNTE_UG(I).IDTIPOASISTENCIACOLEGIO,
                M_APUNTE_UG(I).IDTIPOACTUACION);
        END LOOP;

        V_DATOSERROR := 'PROC_FACT_DESC_MATR_GUARDIA: Tratando asistencias';
        FOR I IN 1 .. IND_AS LOOP

            INSERT INTO FCS_FACT_ASISTENCIA (
                IDINSTITUCION,
                IDFACTURACION,
                IDAPUNTE,
                ANIO,
                NUMERO,
                IDPERSONA,
                FECHAHORA,
                FECHAJUSTIFICACION,
                PRECIOAPLICADO,
                FECHAMODIFICACION,
                USUMODIFICACION,
                IDHITO,
                MOTIVO,
                IDTIPO
            ) VALUES (
                P_IDINSTITUCION,
                P_IDFACTURACION,
                M_APUNTE_AS(I).IDAPUNTE,
                M_APUNTE_AS(I).ANIO,
                M_APUNTE_AS(I).NUMERO,
                M_APUNTE_AS(I).IDPERSONACOLEGIADO,
                M_APUNTE_AS(I).FECHAHORA,
                NULL,
                NVL(M_APUNTE_AS(I).IMPORTE, 0.0),
                SYSDATE,
                P_USUMODIFICACION,
                M_APUNTE_AS(I).MOTIVO,
                FUN_DESCRIPCION_IDHITO(M_APUNTE_AS(I).MOTIVO),
                M_APUNTE_AS(I).CONTADOR);
        END LOOP;

        V_DATOSERROR := 'PROC_FACT_DESC_MATR_GUARDIA: Tratando actuaciones';
        FOR I IN 1 .. IND_AC LOOP

            INSERT INTO FCS_FACT_ACTUACIONASISTENCIA(
                IDINSTITUCION,
                IDFACTURACION,
                FECHAMODIFICACION,
                IDAPUNTE,
                IDACTUACION,
                ANIO,
                NUMERO,
                IDPERSONA,
                PRECIOAPLICADO,
                FECHAACTUACION,
                FECHAJUSTIFICACION,
                USUMODIFICACION,
                PRECIOCOSTESFIJOS,
                IDHITO,
                MOTIVO,
                IDTIPO
            ) VALUES (
                P_IDINSTITUCION,
                P_IDFACTURACION,
                SYSDATE,
                M_APUNTE_AC(I).IDAPUNTE,
                M_APUNTE_AC(I).IDACTUACION,
                M_APUNTE_AC(I).ANIO,
                M_APUNTE_AC(I).NUMERO,
                M_APUNTE_AC(I).IDPERSONA,
                NVL(M_APUNTE_AC(I).IMPORTE, 0.0),
                M_APUNTE_AC(I).FECHAACTUACION,
                M_APUNTE_AC(I).FECHAJUSTIFICACION,
                P_USUMODIFICACION,
                NVL(M_APUNTE_AC(I).COSTEFIJO, 0.0),
                M_APUNTE_AC(I).MOTIVO,
                FUN_DESCRIPCION_IDHITO(M_APUNTE_AC(I).MOTIVO),
                M_APUNTE_AC(I).CONTADOR);
        END LOOP;

        M_APUNTE_AC.DELETE;
        M_APUNTE_AS.DELETE;
        M_APUNTE_UG.DELETE;
        M_APUNTE_CG.DELETE;
        IND_AC := 0;
        IND_AS := 0;
        IND_UG := 0;
        IND_CG := 0;

        V_DATOSERROR := 'PROC_FACT_DESC_MATR_GUARDIA: Finalizado correctamente';
        V_CODRETORNO := '0';

        P_CODRETORNO := V_CODRETORNO;
        P_DATOSERROR := V_DATOSERROR;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := V_DATOSERROR || ' ' || SQLERRM;
    END PROC_FACT_DESC_MATR_GUARDIA;        

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_FACTURAR_TURNOS_OFI                                                                       */
    /* Descripcion:
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
    /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 14/03/2005                                                                                   */
    /* Autor:          Raul Gonzalez Gonzalez
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /*    07/03/2006           Pilar Duran                      Se obtiene ademas del precio total                  */
    /*                                                          el valor del Punto.                                 */
    /*    01/10/2009           Juan antonio Saiz                Se elimina la facturacion por puntos                */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_FACTURAR_TURNOS_OFI(
        P_IDINSTITUCION   IN NUMBER,
        P_IDFACTURACION   IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_TOTAL           OUT VARCHAR2,
        P_CODRETORNO      OUT VARCHAR2,
        P_DATOSERROR      OUT VARCHAR2) IS

        V_TOTAL      NUMBER := 0; /* variable para sumar */
        V_FECHADESDE DATE;
        V_FECHAHASTA DATE;
        V_PRECIO     NUMBER := 0;
        
        CURSOR C_ACTDESIGNA(P_IDTURNO number, P_FECHADESDE DATE, P_FECHAHASTA DATE) IS
            SELECT ACT.IDINSTITUCION,
                   ACT.IDTURNO,
                   ACT.ANIO,
                   ACT.NUMERO,
                   ACT.NUMEROASUNTO,
                   ACT.IDPERSONACOLEGIADO,
                   ACT.FECHA,
                   ACT.IDACREDITACION,
                   ACT.FECHAJUSTIFICACION,
                   PROC.PRECIO,
                   PROC.NOMBRE,
                   PROC.IDPROCEDIMIENTO,
                   ACRE.DESCRIPCION,
                   ACREPRO.PORCENTAJE
              FROM SCS_ACTUACIONDESIGNA ACT,
                   SCS_ACREDITACIONPROCEDIMIENTO ACREPRO,
                   SCS_PROCEDIMIENTOS PROC,
                   SCS_ACREDITACION ACRE
             WHERE ACT.IDINSTITUCION_PROC = PROC.IDINSTITUCION
               AND ACT.IDPROCEDIMIENTO = PROC.IDPROCEDIMIENTO
               AND ACT.IDACREDITACION = ACREPRO.IDACREDITACION
               AND ACT.IDINSTITUCION_PROC = ACREPRO.IDINSTITUCION
               AND ACT.IDPROCEDIMIENTO = ACREPRO.IDPROCEDIMIENTO
               AND ACREPRO.IDACREDITACION = ACRE.IDACREDITACION
               AND ACT.IDINSTITUCION = P_IDINSTITUCION
               AND ACT.IDTURNO = P_IDTURNO
               AND ACT.FECHAJUSTIFICACION IS NOT NULL
               AND (TRUNC(ACT.FECHAJUSTIFICACION) BETWEEN TRUNC(P_FECHADESDE) AND TRUNC(P_FECHAHASTA))
               AND NVL(ACT.FACTURADO, 0) <> 1
               AND ACT.VALIDADA = '1' -- Validada
               AND (NVL(ACT.ANULACION, '0') = '0')
               AND ACT.IDPERSONACOLEGIADO IS NOT NULL;

    BEGIN
        V_CODRETORNO2 := 0;
        V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';
        V_TOTAL       := 0;
        V_PRECIO      := 0;

        --Almacenamos en los historicos
        PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_TURNOS(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
      
        -- 1. Hacemos una consulta sobre la tabla FCS_FACTURACIONJG para obtener la FECHADESDE y la FECHAHASTA del idinstitucion y del idfacturacion introducidos como parametros
        V_DATOSERROR2 := 'Antes de obtener las fechas desde y hasta de facturacion';
        SELECT FAC.FECHADESDE, FAC.FECHAHASTA
            INTO V_FECHADESDE, V_FECHAHASTA
        FROM FCS_FACTURACIONJG FAC
        WHERE FAC.IDFACTURACION = P_IDFACTURACION
            AND FAC.IDINSTITUCION = P_IDINSTITUCION;
        
        -- Grupos de facturacion
        V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';
        FOR V_GRUPOSFACTURACION IN CURSOR_GRUPOSFACTURACION (
            P_IDINSTITUCION,
            P_IDFACTURACION,
            PKG_SIGA_CONSTANTES.HITO_GENERAL_TURNO
        ) LOOP
            
            -- Turnos
            V_DATOSERROR2 := 'Antes de ejecutar turnos';
            FOR V_TURNO IN CURSOR_TURNO (
                P_IDINSTITUCION,
                V_GRUPOSFACTURACION.IDGRUPOFACTURACION
            ) LOOP
            
                -- Actuacion Designa
                V_DATOSERROR2 := 'Antes de ejecutar Actuacion Designa';
                FOR V_ACTDESIGNA IN C_ACTDESIGNA (
                    V_TURNO.IDTURNO,
                    V_FECHADESDE,
                    V_FECHAHASTA
                ) LOOP

                    V_TOTAL  := V_TOTAL + round (V_ACTDESIGNA.PRECIO * (V_ACTDESIGNA.PORCENTAJE / 100), 2);
                    V_PRECIO := V_PRECIO + V_ACTDESIGNA.PRECIO;

                    -- INSERTAMOS EL APUNTE: El IDPERSONA lo hemos sacado del SCS_ACTUACIONDESIGNA
                    V_DATOSERROR2 := 'Antes de insertar en FCS_FACT_ACTUACIONDESIGNA';
                    INSERT INTO FCS_FACT_ACTUACIONDESIGNA(
                        IDINSTITUCION,
                        IDFACTURACION,
                        NUMEROASUNTO,
                        NUMERO,
                        ANIO,
                        IDTURNO,
                        IDPERSONA,
                        PROCEDIMIENTO,
                        ACREDITACION,
                        FECHAACTUACION,
                        FECHAJUSTIFICACION,
                        PRECIOAPLICADO,
                        PORCENTAJEFACTURADO,
                        FECHAMODIFICACION,
                        USUMODIFICACION,
                        CODIGOPROCEDIMIENTO
                    ) VALUES (
                        P_IDINSTITUCION,
                        P_IDFACTURACION,
                        V_ACTDESIGNA.NUMEROASUNTO,
                        V_ACTDESIGNA.NUMERO,
                        V_ACTDESIGNA.ANIO,
                        V_ACTDESIGNA.IDTURNO,
                        V_ACTDESIGNA.IDPERSONACOLEGIADO,
                        V_ACTDESIGNA.NOMBRE,
                        V_ACTDESIGNA.DESCRIPCION,
                        V_ACTDESIGNA.FECHA,
                        V_ACTDESIGNA.FECHAJUSTIFICACION,
                        V_ACTDESIGNA.PRECIO,
                        V_ACTDESIGNA.PORCENTAJE,
                        SYSDATE,
                        P_USUMODIFICACION,
                        V_ACTDESIGNA.IDPROCEDIMIENTO);
            
                    V_DATOSERROR2 := 'Antes de actualizar en scs_actuaciondesigna';
                    -- INDICAMOS QUE LO HEMOS CALCULADO YA
                    UPDATE SCS_ACTUACIONDESIGNA
                    SET FACTURADO = PKG_SIGA_CONSTANTES.DB_TRUE_N,
                        IDFACTURACION = P_IDFACTURACION
                    WHERE IDINSTITUCION = V_ACTDESIGNA.IDINSTITUCION
                        AND IDTURNO = V_ACTDESIGNA.IDTURNO
                        AND ANIO = V_ACTDESIGNA.ANIO
                        AND NUMERO = V_ACTDESIGNA.NUMERO
                        AND NUMEROASUNTO = V_ACTDESIGNA.NUMEROASUNTO;
                END LOOP; -- Fin Actuacion Designa
            END LOOP; -- Fin Turnos
        END LOOP; -- Fin Grupos Facturacion

        V_DATOSERROR2 := 'Fin correcto ';
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
        P_TOTAL := TO_CHAR(V_TOTAL);

        EXCEPTION
          WHEN OTHERS THEN
            P_TOTAL      := '0';
            P_CODRETORNO := TO_CHAR(SQLCODE);
            P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';
    END PROC_FCS_FACTURAR_TURNOS_OFI;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_FACTURAR_EJG                                                                      */
    /* Descripcion:
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
    /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 14/03/2005                                                                                   */
    /* Autor:         Pilar Duran Mu?oz
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /*
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_FACTURAR_EJG(
        P_IDINSTITUCION   IN NUMBER,
        P_IDFACTURACION   IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_TOTAL           OUT VARCHAR2,
        P_CODRETORNO      OUT VARCHAR2,
        P_DATOSERROR      OUT VARCHAR2) IS
        
        V_TOTAL      NUMBER := 0; /* variable para sumar */
        V_FECHADESDE DATE;
        V_FECHAHASTA DATE;
        V_PRECIO     NUMBER := 0;
        
        CURSOR CURSOR_EJG(
            P_IDTURNO NUMBER, 
            P_IDGUARDIA NUMBER, 
            V_FECHADESDE DATE, 
            V_FECHAHASTA DATE) IS
            SELECT 
                IDTIPOEJG,
                ANIO,
                NUMERO,
                FECHAAPERTURA,
                IDPERSONA
            FROM SCS_EJG
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND GUARDIATURNO_IDTURNO = P_IDTURNO
                AND GUARDIATURNO_IDGUARDIA = P_IDGUARDIA              
                AND IDPERSONA IS NOT NULL -- Se pueden dar de alta EJG sin letrado asociado
                AND TRUNC(FECHAAPERTURA) BETWEEN TRUNC(V_FECHADESDE) AND TRUNC(V_FECHAHASTA)
                AND NVL(FACTURADO, 0) <> 1;
    BEGIN
        -- 1. Hacemos una consulta sobre la tabla FCS_FACTURACIONJG para obtener la FECHADESDE y la FECHAHASTA del idinstitucion y del idfacturacion introducidos como parametros
        V_DATOSERROR2 := 'Antes de obtener las fechas desde y hasta de facturacion';
        SELECT FAC.FECHADESDE, FAC.FECHAHASTA
            INTO V_FECHADESDE, V_FECHAHASTA
        FROM FCS_FACTURACIONJG FAC
        WHERE FAC.IDFACTURACION = P_IDFACTURACION
            AND FAC.IDINSTITUCION = P_IDINSTITUCION;

        -- Grupos de facturacion
        V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';        
        FOR V_GRUPOSFACTURACION IN CURSOR_GRUPOSFACTURACION (
            P_IDINSTITUCION,
            P_IDFACTURACION,
            PKG_SIGA_CONSTANTES.HITO_GENERAL_EJG
        ) LOOP

            -- Turnos
            V_DATOSERROR2 := 'Antes de ejecutar turnos';
            FOR V_TURNO IN CURSOR_TURNO(
                P_IDINSTITUCION,
                V_GRUPOSFACTURACION.IDGRUPOFACTURACION
            ) LOOP
            
                -- Guardias turno
                V_DATOSERROR2 := 'Antes de ejecutar Actuacion Designa';
                FOR V_GUARDIASTURNO IN CURSOR_GUARDIASTURNO(
                    P_IDINSTITUCION,
                    V_TURNO.IDTURNO
                ) LOOP

                    --Guardamos el historico de los hitos de la facturacion
                    V_DATOSERROR2 := 'Antes de ejecutar el historico de los hitos';
                    PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
                        P_IDINSTITUCION,
                        P_IDFACTURACION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        C_EJG,
                        V_CODRETORNO2,
                        V_DATOSERROR2);

                    -- Obtenemos el precio del EJG
                    BEGIN
                        SELECT NVL(PRECIOHITO, 0)
                            INTO V_PRECIO
                        FROM SCS_HITOFACTURABLEGUARDIA
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND IDTURNO = V_TURNO.IDTURNO
                            AND IDGUARDIA = V_GUARDIASTURNO.IDGUARDIA
                            AND IDHITO = C_EJG;
                        EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                                V_PRECIO := 0;
                    END;

                    FOR V_EJG IN Cursor_EJG(
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        V_FECHADESDE,
                        V_FECHAHASTA
                    ) LOOP
                    
                        -- Actualizamos la variable V_TOTAL antes de hacer el apunte en la tabla de facturacion de EJG
                        V_TOTAL := V_TOTAL + V_PRECIO;
              
                        -- INSERTAMOS EL APUNTE EN FCS_FACT_EJG
                        V_DATOSERROR2 := 'Antes de insertar en FCS_FACT_EJG';
                        INSERT INTO FCS_FACT_EJG(
                            IDINSTITUCION,
                            IDFACTURACION,
                            IDTIPOEJG,
                            ANIO,
                            NUMERO,
                            IDPERSONA,
                            IDTURNO,
                            IDGUARDIA,
                            FECHAAPERTURA,
                            PRECIOAPLICADO,
                            FECHAMODIFICACION,
                            USUMODIFICACION
                        ) VALUES (
                            P_IDINSTITUCION,
                            P_IDFACTURACION,
                            V_EJG.IDTIPOEJG,
                            V_EJG.ANIO,
                            V_EJG.NUMERO,
                            V_EJG.IDPERSONA,
                            V_TURNO.IDTURNO,
                            V_GUARDIASTURNO.IDGUARDIA,
                            V_EJG.FECHAAPERTURA,
                            V_PRECIO,
                            SYSDATE,
                            P_USUMODIFICACION);

                        V_DATOSERROR2 := 'Antes de actualizar en SCS_EJG';
                        -- INDICAMOS QUE LO HEMOS CALCULADO YA
                        UPDATE SCS_EJG
                        SET FACTURADO = PKG_SIGA_CONSTANTES.DB_TRUE_N,
                            IDFACTURACION = P_IDFACTURACION
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND ANIO = V_EJG.ANIO
                            AND NUMERO = V_EJG.NUMERO
                            AND IDTIPOEJG = V_EJG.IDTIPOEJG;
                    END LOOP;
                END LOOP; -- Fin Guardias turno
            END LOOP; -- Fin Turnos
        END LOOP; -- Fin Grupos Facturacion

        V_DATOSERROR2 := 'Fin correcto';
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
        P_TOTAL      := TO_CHAR(V_TOTAL);

        EXCEPTION
            WHEN E_ERROR2 THEN
                P_TOTAL      := '0';
                P_CODRETORNO := V_CODRETORNO2;
                P_DATOSERROR := V_DATOSERROR2;
            WHEN OTHERS THEN
                P_TOTAL      := '0';
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';
    END PROC_FCS_FACTURAR_EJG;
    
    /****************************************************************************************************************
    Nombre: PROC_FCS_FACTURAR_GUARDIAS
    Descripcion: Procedimiento que realiza la facturacion de guardias SJCS

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDFACTURACION - IN - Identificador de la facturacion - NUMBER
    - P_USUMODIFICACION - IN - Usuario modificador - NUMBER
    - P_TOTAL - OUT - Acumulador del total de la facturacion - NUMBER    
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.    

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 14/03/2005 - Pilar Duran Munoz
    - 2.0 - 01/01/2017 - Jorge Paez Trivino - SIGA_126 - Facturacion Hitos Valencianos
    ****************************************************************************************************************/     
    PROCEDURE PROC_FCS_FACTURAR_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_TOTAL OUT VARCHAR2,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        V_DATOS_FACTURACION DATOS_FACTURACION;
        V_CONFIG_GUARDIA CONFIG_GUARDIA;
        V_CONFIG_GUARDIA_ACTUAL CONFIG_GUARDIA;
        V_TOTAL_FACTURACION NUMBER := 0;

    BEGIN                        
        -- JPT: Obtiene los datos de la facturacion (tabla FCS_FACTURACIONJG) y carga los datos en V_DATOS_FACTURACION
        V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FCS_CARGA_FACTURACION';
        PROC_FCS_CARGA_FACTURACION (
            P_IDINSTITUCION,
            P_IDFACTURACION,
            V_DATOS_FACTURACION,
            V_CODRETORNO2,
            V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR2;
        END IF;            

        /* JPT: Se almacenan los historicos a nivel de institucion
            - FCS_HISTORICO_TIPOASISTCOLEGIO de SCS_TIPOASISTENCIACOLEGIO
            - FCS_HISTORICO_TIPOACTUACION de SCS_TIPOACTUACION
            - FCS_HISTO_TIPOACTCOSTEFIJO de SCS_TIPOACTUACIONCOSTEFIJO */
        V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_GUARDIAS';
        PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_GUARDIAS (
            P_IDINSTITUCION,
            P_IDFACTURACION,
            V_CODRETORNO2,
            V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR2;
        END IF;

        -- JPT: Obtiene los grupos de la facturacion (tabla FCS_FACT_GRUPOFACT_HITO)
        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Cursor CURSOR_GRUPOSFACTURACION';
        FOR V_GRUPOSFACTURACION IN CURSOR_GRUPOSFACTURACION (
            P_IDINSTITUCION, 
            P_IDFACTURACION,
            PKG_SIGA_CONSTANTES.HITO_GENERAL_GUARDIA
        ) LOOP

            -- JPT: Guarda el historico de la configuracion de todas las guardias del grupo de facturacion para esa facturacion (FCS_HISTORICO_HITOFACT de SCS_HITOFACTURABLEGUARDIA)
            V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTO_HITOFACT_GUA';
            PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTO_HITOFACT_GUA (
                P_IDINSTITUCION,
                P_IDFACTURACION,
                V_GRUPOSFACTURACION.IDGRUPOFACTURACION,
                V_CODRETORNO2,
                V_DATOSERROR2);
            IF (V_CODRETORNO2 <> '0') THEN
                RAISE E_ERROR2;
            END IF;

            -- JPT: Obtiene los turnos asociados al grupo de facturacion (tabla SCS_TURNO)
            V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Cursor CURSOR_TURNO';
            FOR V_TURNO IN CURSOR_TURNO (
                P_IDINSTITUCION, 
                V_GRUPOSFACTURACION.IDGRUPOFACTURACION
            ) LOOP

                -- JPT: Obtiene las guardias asociadas al turno (tabla SCS_GUARDIASTURNO)
                V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Cursor CURSOR_GUARDIASTURNO';
                FOR V_GUARDIASTURNO IN CURSOR_GUARDIASTURNO (
                    P_IDINSTITUCION, 
                    V_TURNO.IDTURNO
                ) LOOP

                    /* JPT: Cargamos el RECORD de la Configuracion de Guardia ACTUAL (V_CONFIG_GUARDIA)
                        - Obtiene los hitos de la facturacion (tabla FCS_HISTORICO_HITOFACT) y carga los datos en V_CONFIG_GUARDIA
                        - Para los catalanes hay que consultar SCS_GUARDIASTURNO.ESGUARDIAVG para la facturacion por guardias*/
                    V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FCS_CARGA_CONFIG_GUARDIA';
                    PROC_FCS_CARGA_CONFIG_GUARDIA (
                        P_IDINSTITUCION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        V_GUARDIASTURNO.IDTIPOGUARDIA,
                        P_IDFACTURACION, -- Obtenemos la configuracion de la guardia actual y la asociadamos a la facturacion actual
                        V_CONFIG_GUARDIA,
                        V_CODRETORNO2,
                        V_DATOSERROR2);
                    IF (V_CODRETORNO2 <> '0') THEN
                        RAISE E_ERROR2;
                    END IF;

                    -- JPT: Guardo una copia del RECORD de la Configuracion de Guardia ACTUAL (cambio facturacion guardias facturadas a precio facturado)
                    V_CONFIG_GUARDIA_ACTUAL := V_CONFIG_GUARDIA;
                    
                    -- Comprueba si esta configurado para facturar la guardia
                    IF (V_CONFIG_GUARDIA_ACTUAL.CONFIG_PAGAGUARDIA.DIAS IS NOT NULL 
                        OR V_CONFIG_GUARDIA_ACTUAL.CONFIG_NOPAGAGUARDIA.DIAS IS NOT NULL) THEN

                        /* JPT: Cargamos la MATRIZ de la Cabecera de Guardias a facturar [M_CG_FACTURABLE(++IND_CG_FACTURABLE)]
                            - Obtiene las cabeceras de guardia validadas dentro del periodo de la facturacion y sin facturar
                                (tabla SCS_CABECERAGUARDIAS + FCS_FACT_APUNTE ),
                                y carga los datos en M_CG_FACTURABLE(++IND_CG_FACTURABLE)
                            - Obtiene las cabeceras de guardia validadas, que tengan por lo menos una actuacion que no sea fuera de guardia sin anular, justificada dentro del periodo de la facturacion y sin facturar
                                (tabla SCS_CABECERAGUARDIAS + SCS_GUARDIASCOLEGIADO + SCS_ASISTENCIA + SCS_ACTUACIONASISTENCIA + FCS_FACT_ACTUACIONASISTENCIA),
                                y carga los datos en M_CG_FACTURABLE(++IND_CG_FACTURABLE)*/
                        V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_CARGAR_CABECERA_GUARDIAS';
                        PROC_CARGAR_CABECERA_GUARDIAS(
                            P_IDINSTITUCION,
                            V_TURNO.IDTURNO,
                            V_GUARDIASTURNO.IDGUARDIA,
                            V_DATOS_FACTURACION,
                            V_CONFIG_GUARDIA,
                            V_CODRETORNO2,
                            V_DATOSERROR2);
                        IF (V_CODRETORNO2 <> '0') THEN
                            RAISE E_ERROR2;
                        END IF;                    

                        -- JPT: Recorremos MATRIZ M_CG_FACTURABLE(1..IND_CG_FACTURABLE)
                        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Recorremos MATRIZ M_CG_FACTURABLE(1..IND_CG_FACTURABLE)';
                        FOR I IN 1 .. IND_CG_FACTURABLE LOOP

                            -- Si ha cambiado la facturacion, tenemos que volver a configurar V_CONFIG_GUARDIA
                            IF (M_CG_FACTURABLE(I).IDFACTURACION <> V_CONFIG_GUARDIA.IDFACTURACION) THEN

                                -- JPT: Procedimiento para configuraciones antiguas que carga V_CONFIG_GUARDIA
                                V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a  PROC_CARGA_CONFIG_GUARDIA';
                                PROC_CARGA_CONFIG_GUARDIA(
                                    P_IDINSTITUCION,
                                    V_TURNO.IDTURNO,
                                    V_GUARDIASTURNO.IDGUARDIA,
                                    V_GUARDIASTURNO.IDTIPOGUARDIA,
                                    V_CONFIG_GUARDIA,
                                    V_CONFIG_GUARDIA_ACTUAL,
                                    M_CG_FACTURABLE(I).IDFACTURACION,
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                IF (V_CODRETORNO2 <> '0') THEN
                                    RAISE E_ERROR2;
                                END IF;
                            END IF;                     

                            -- Si se factura pagando guardia
                            IF (V_CONFIG_GUARDIA.CONFIG_PAGAGUARDIA.DIAS IS NOT NULL) THEN
                                V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FACT_PAGAGUARDIA';
                                PROC_FACT_PAGAGUARDIA(
                                    M_CG_FACTURABLE(I),
                                    V_CONFIG_GUARDIA,
                                    V_DATOS_FACTURACION,
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                IF (V_CODRETORNO2 <> '0') THEN
                                    RAISE E_ERROR2;
                                END IF;                                
                            END IF;
                            
                            -- Si se factura sin pagar guardia
                            IF (V_CONFIG_GUARDIA.CONFIG_NOPAGAGUARDIA.DIAS IS NOT NULL) THEN
                                V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FACT_NOPAGAGUARDIA';
                                PROC_FACT_NOPAGAGUARDIA(
                                    M_CG_FACTURABLE(I),
                                    V_CONFIG_GUARDIA,
                                    V_DATOS_FACTURACION,
                                    V_CODRETORNO2,
                                    V_DATOSERROR2);
                                IF (V_CODRETORNO2 <> '0') THEN
                                    RAISE E_ERROR2;
                                END IF;                                 
                            END IF;                                                 
                        END LOOP; -- JPT: MATRIZ - M_CG_FACTURABLE(1..IND_CG_FACTURABLE)
                    END IF;                 

                    /******************* INICIO TRATAMIENTO FUERA DE GUARDIA *********************/
                    --V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FACT_FG_CG_TOTAL';
                    --PROC_FACT_FG_CG_TOTAL(
                    V_DATOSERROR2:= 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FACT_FG_CG_PARTIDA';
                    PROC_FACT_FG_CG_PARTIDA(
                        P_IDINSTITUCION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        V_DATOS_FACTURACION,
                        V_CONFIG_GUARDIA_ACTUAL, -- Carga por defecto la guardia actual
                        V_CODRETORNO2,
                        V_DATOSERROR2);
                    IF (V_CODRETORNO2 <> '0') THEN
                        RAISE E_ERROR2;
                    END IF;
                    /******************* FIN TRATAMIENTO FUERA DE GUARDIA *********************/
                    
                    -- Lo situo aqui, pq utilizo IND_CG como contador que hay que sumar a IDAPUNTE
                    -- Llamamos al procedimiento que realiza los apuntes en bb.dd y borra las matrices de memoria
                    V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Llamada a PROC_FACT_DESC_MATR_GUARDIA';
                    PROC_FACT_DESC_MATR_GUARDIA(
                        P_IDINSTITUCION,
                        P_IDFACTURACION,
                        V_TOTAL_FACTURACION,
                        P_USUMODIFICACION,
                        V_CODRETORNO,
                        V_DATOSERROR);
                    IF (V_CODRETORNO <> '0') THEN
                        V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
                        RAISE E_ERROR2;
                    END IF;
                    
                END LOOP; -- JPT: CURSOR - V_GUARDIASTURNO IN CURSOR_GUARDIASTURNO
            END LOOP; -- JPT: CURSOR - V_TURNO IN CURSOR_TURNO
        END LOOP; -- JPT: CURSOR - V_GRUPOSFACTURACION IN CURSOR_GRUPOSFACTURACION

        BEGIN
            P_TOTAL := TO_CHAR(V_TOTAL_FACTURACION);

            EXCEPTION
                WHEN OTHERS THEN
                    P_TOTAL := '0';
        END;

        --Todos los elementos utilizados para la facturacion se marcan como facturados.
        V_DATOSERROR2 := 'Marcado de datos facturados';
        PROC_FCS_MARCAR_FACTURADOS(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO, V_DATOSERROR);
        IF (V_CODRETORNO <> '0') THEN
            V_DATOSERROR2 := V_DATOSERROR2 || ': ' || V_DATOSERROR;
            RAISE E_ERROR2;
        END IF;

        V_CODRETORNO2 := '0';
        V_DATOSERROR2 := 'PROC_FCS_FACTURAR_GUARDIAS: Finalizado correctamente';
        P_CODRETORNO  := V_CODRETORNO2;
        P_DATOSERROR  := V_DATOSERROR2;

        EXCEPTION
            WHEN E_ERROR2 THEN
                P_CODRETORNO := '-1';
                P_DATOSERROR := V_DATOSERROR2;

            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := 'Error en el proceso:PROC_FCS_FACTURAR_GUARDIAS. ' || SQLERRM || V_DATOSERROR2;
    END PROC_FCS_FACTURAR_GUARDIAS;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_FACTURAR_SOJ                                                                       */
    /* Descripcion:
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_USUMODIFICACION     IN       usuario que realiza la llamada                                 NUMBER,
    /* P_TOTAL               OUT      importe total calculado                                        VARCHAR2,
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 14/03/2005                                                                                   */
    /* Autor:         Pilar Duran Mu?oz
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /*
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_FACTURAR_SOJ(
        P_IDINSTITUCION   IN NUMBER,
        P_IDFACTURACION   IN NUMBER,
        P_USUMODIFICACION IN NUMBER,
        P_TOTAL           OUT VARCHAR2,
        P_CODRETORNO      OUT VARCHAR2,
        P_DATOSERROR      OUT VARCHAR2) IS
        
        V_TOTAL      NUMBER := 0; /* variable para sumar */
        V_FECHADESDE DATE;
        V_FECHAHASTA DATE;
        V_PRECIO     NUMBER := 0;
        
        CURSOR Cursor_SOJ(
            P_IDTURNO NUMBER, 
            P_IDGUARDIA NUMBER, 
            V_FECHADESDE DATE, 
            V_FECHAHASTA DATE) IS
            SELECT IDTIPOSOJ,
                ANIO,
                NUMERO,
                FECHAAPERTURA,
                IDPERSONA
            FROM SCS_SOJ
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDGUARDIA = P_IDGUARDIA
                AND IDTURNO = P_IDTURNO            
                AND IDPERSONA IS NOT NULL -- Se pueden haber creado SOJ sin letrado asociado
                AND (TRUNC(FECHAAPERTURA) BETWEEN V_FECHADESDE AND V_FECHAHASTA)
                AND NVL(FACTURADO, 0) <> 1;
    BEGIN
        -- 1. Hacemos una consulta sobre la tabla FCS_FACTURACIONJG para obtener la FECHADESDE y la FECHAHASTA del idinstitucion y del idfacturacion introducidos como parametros
        V_DATOSERROR2 := 'Antes de obtener las fechas desde y hasta de facturacion';
        SELECT FAC.FECHADESDE, FAC.FECHAHASTA
            INTO V_FECHADESDE, V_FECHAHASTA
        FROM FCS_FACTURACIONJG FAC
        WHERE FAC.IDFACTURACION = P_IDFACTURACION
            AND FAC.IDINSTITUCION = P_IDINSTITUCION;

        -- Grupos de facturacion
        V_DATOSERROR2 := 'Antes de ejecutar grupos facturacion';      
        FOR V_GRUPOSFACTURACION IN CURSOR_GRUPOSFACTURACION(
            P_IDINSTITUCION,
            P_IDFACTURACION,
            PKG_SIGA_CONSTANTES.HITO_GENERAL_SOJ
        ) LOOP

            -- Turnos
            V_DATOSERROR2 := 'Antes de ejecutar turnos';
            FOR V_TURNO IN CURSOR_TURNO(
                P_IDINSTITUCION,
                V_GRUPOSFACTURACION.IDGRUPOFACTURACION
            ) LOOP
            
                -- Guardias turno
                V_DATOSERROR2 := 'Antes de ejecutar Actuacion Designa';
                FOR V_GUARDIASTURNO IN CURSOR_GUARDIASTURNO(
                    P_IDINSTITUCION,
                    V_TURNO.IDTURNO
                ) LOOP

                    --Guardamos el historico de los hitos de la facturacion
                    V_DATOSERROR2 := 'Antes de ejecutar el historico de los hitos';
                    PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
                        P_IDINSTITUCION,
                        P_IDFACTURACION,
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        C_SOJ,
                        V_CODRETORNO2,
                        V_DATOSERROR2);

                    -- Obtenemos el precio del EJG
                    BEGIN
                        SELECT NVL(PRECIOHITO, 0)
                            INTO V_PRECIO
                        FROM SCS_HITOFACTURABLEGUARDIA
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND IDTURNO = V_TURNO.IDTURNO
                            AND IDGUARDIA = V_GUARDIASTURNO.IDGUARDIA
                            AND IDHITO = C_SOJ;
                        EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                                V_PRECIO := 0;
                    END;

                    FOR V_SOJ IN Cursor_SOJ(
                        V_TURNO.IDTURNO,
                        V_GUARDIASTURNO.IDGUARDIA,
                        V_FECHADESDE,
                        V_FECHAHASTA) LOOP

                        -- Actualizamos la variable v_Total antes de hacer el apunte en la tabla de facturacion
                        V_TOTAL := V_TOTAL + V_PRECIO;

                        -- INSERTAMOS EL APUNTE EN FCS_FACT_EJG
                        V_DATOSERROR2 := 'Antes de insertar en FCS_FACT_SOJ';
                        INSERT INTO FCS_FACT_SOJ(
                            IDINSTITUCION,
                            IDFACTURACION,
                            IDTIPOSOJ,
                            ANIO,
                            NUMERO,
                            IDPERSONA,
                            IDTURNO,
                            IDGUARDIA,
                            PRECIOAPLICADO,
                            FECHAAPERTURA,
                            FECHAMODIFICACION,
                            USUMODIFICACION
                        ) VALUES (
                            P_IDINSTITUCION,
                            P_IDFACTURACION,
                            V_SOJ.IDTIPOSOJ,
                            V_SOJ.ANIO,
                            V_SOJ.NUMERO,
                            V_SOJ.IDPERSONA,
                            V_TURNO.IDTURNO,
                            V_GUARDIASTURNO.IDGUARDIA,
                            V_PRECIO,
                            V_SOJ.FECHAAPERTURA,
                            SYSDATE,
                            P_USUMODIFICACION);

                        V_DATOSERROR2 := 'Antes de actualizar en SCS_SOJ';
                        -- INDICAMOS QUE LO HEMOS CALCULADO YA
                        UPDATE SCS_SOJ
                        SET FACTURADO = PKG_SIGA_CONSTANTES.DB_TRUE_N,
                            IDFACTURACION = P_IDFACTURACION
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND ANIO = V_SOJ.ANIO
                            AND NUMERO = V_SOJ.NUMERO
                            AND IDTIPOSOJ = V_SOJ.IDTIPOSOJ;
                    END LOOP;
                END LOOP; -- Fin Guardias turno
            END LOOP; -- Fin Turnos
        END LOOP; -- Fin Grupos de facturacion

        V_DATOSERROR2 := 'Fin correcto';
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
        P_TOTAL      := TO_CHAR(V_TOTAL);

        EXCEPTION
            WHEN E_ERROR2 THEN
                P_TOTAL      := '0';
                P_CODRETORNO := V_CODRETORNO2;
                P_DATOSERROR := V_DATOSERROR2;
            WHEN OTHERS THEN
                P_TOTAL      := '0';
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';
    END PROC_FCS_FACTURAR_SOJ;
    
    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_ACTUACIONDESIGNA                                                                 */
    /* Descripcion:   Pone a no facturadas las actuaciones de designaciones                                         */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                        */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_ACT_DES(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_ACT_DES: Inicio';
        UPDATE SCS_ACTUACIONDESIGNA
        SET FACTURADO = NULL, 
            IDFACTURACION = NULL
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_ACT_DES: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_ACT_DES;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_ASISTENCIA                                                                       */
    /* Descripcion: Pone a no facturadas las asistencias                                                            */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                        */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_ASISTENCIA(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_ASISTENCIA: Inicio';
        --MODIFICAR EN SCS_ASISTENCIA
        UPDATE SCS_ASISTENCIA
        SET FACTURADO = NULL, 
            IDFACTURACION = NULL
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_ASISTENCIA: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_ASISTENCIA;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_ACTUACIONASISTENCIA                                                              */
    /* Descripcion: Pone a no facturado las actuaciones de Asistencia                                               */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                         */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_ACT_ASIS(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_ACT_ASIS: Inicio';
        --MODIFICAR EN SCS_ACTUACIONASISTENCIA
        UPDATE SCS_ACTUACIONASISTENCIA
        SET FACTURADO = NULL, 
            IDFACTURACION = NULL
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_ACT_ASIS: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_ACT_ASIS;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_GUARDIASCOLEGIADO                                                                */
    /* Descripcion: Pone a no facturado los dias de guardia                                                         */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                       */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_GUAR_COL(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_GUAR_COL: Inicio';    
        --MODIFICAR EN SCS_GUARDIASCOLEGIADO
        UPDATE SCS_GUARDIASCOLEGIADO
        SET FACTURADO = NULL, 
            IDFACTURACION = NULL
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_GUAR_COL: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_GUAR_COL;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_APUNTES                                                                          */
    /* Descripcion: Pone a no facturado la cabecera de guardias                                                     */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Nuevo                                            */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_APUNTES(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_APUNTES: Inicio';
        --MODIFICAR EN SCS_CABECERAGUARDIAS
        UPDATE SCS_CABECERAGUARDIAS
        SET FACTURADO = NULL, 
            IDFACTURACION = NULL
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_APUNTES: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_APUNTES;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_EXPEDIENTES_SOJ                                                                  */
    /* Descripcion: Pone a no facturado el expediente SOJ                                                           */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                       */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_EXP_SOJ(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS
        
        CURSOR Cursor_SOJ IS
            SELECT IDINSTITUCION, IDTIPOSOJ, ANIO, NUMERO
            FROM FCS_FACT_SOJ
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDFACTURACION = P_IDFACTURACION;

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_EXP_SOJ: Inicio';
        --PARA CADA FAC_SOJ
        FOR V_SOJ IN Cursor_SOJ LOOP

            --MODIFICAR EN SCS_SOJ
            UPDATE SCS_SOJ
            SET FACTURADO = NULL, 
                IDFACTURACION = NULL
            WHERE IDINSTITUCION = V_SOJ.IDINSTITUCION
               AND IDTIPOSOJ = V_SOJ.IDTIPOSOJ
               AND ANIO = V_SOJ.ANIO
               AND NUMERO = V_SOJ.NUMERO
               AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;
        END LOOP; --DEL LOOP

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_EXP_SOJ: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_EXP_SOJ;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_ELIMINAR_EXPEDIENTES_EJG                                                                  */
    /* Descripcion: Pone a no facturado expedientes EJG                                                             */
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /* 07/03/2006           ACG                                   Actualizacion nuevo modelo                       */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_ELIMINAR_EXP_EJG(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS
  
        CURSOR Cursor_EJG IS
            SELECT IDINSTITUCION, 
                IDTIPOEJG, 
                ANIO, 
                NUMERO
            FROM FCS_FACT_EJG
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDFACTURACION = P_IDFACTURACION;

    BEGIN
        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_EXP_EJG: Inicio';

        --PARA CADA FAC_EJG
        FOR V_EJG IN Cursor_EJG LOOP
            --MODIFICAR EN SCS_EJG
            UPDATE SCS_EJG
            SET FACTURADO = NULL, 
                IDFACTURACION = NULL
            WHERE IDINSTITUCION = V_EJG.IDINSTITUCION
                AND IDTIPOEJG = V_EJG.IDTIPOEJG
                AND ANIO = V_EJG.ANIO
                AND NUMERO = V_EJG.NUMERO
                AND NVL(IDFACTURACION, P_IDFACTURACION) = P_IDFACTURACION;
        END LOOP; --DEL LOOP

        V_DATOSERROR2 := 'PROC_FCS_ELIMINAR_EXP_EJG: Fin Correcto';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_ELIMINAR_EXP_EJG;    

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_BORRAR_FACTURACION                                                                        */
    /* Descripcion: Elimina una facturacion borrando la facturacion y sus apuntes (borrado en cascada) y actualizando*/
    /*              los conceptos de SJCS (facturado = null)                                                        */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 16/03/2005                                                                                   */
    /* Autor:          Ruben Fernandez Cano                                                                         */
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* 07/03/2006           ACG                                 Adaptacion al nuevo modelo de datos                 */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_BORRAR_FACTURACION(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

        -- VARIABLES
        V_ESTADO    VARCHAR2(50) := '0';
        V_PREVISION VARCHAR2(50) := '0';
        
        -- ERRORES CONTROLADOS DE LA FUNCION BORRAR FACTURACION
        E_ERROR_BF_1 EXCEPTION;
        E_ERROR_BF_2 EXCEPTION;
        E_ERROR_BF_3 EXCEPTION;
        E_ERROR_BF_4 EXCEPTION;
        E_ERROR_BF_5 EXCEPTION;
        E_ERROR_BF_6 EXCEPTION;
        E_ERROR_BF_7 EXCEPTION;    

    BEGIN
        V_DATOSERROR := 'Borrar: Antes de consultar estado';

        --Obtener el Estado de la Facturacion
        SELECT EST.IDESTADOFACTURACION
            INTO V_ESTADO
        FROM FCS_FACT_ESTADOSFACTURACION EST_FAC, FCS_ESTADOSFACTURACION EST
        WHERE EST_FAC.FECHAESTADO = (
            SELECT MAX(EST_FAC2.FECHAESTADO)
            FROM FCS_FACT_ESTADOSFACTURACION EST_FAC2
            WHERE EST_FAC2.IDFACTURACION = EST_FAC.IDFACTURACION
                AND EST_FAC2.IDINSTITUCION = EST_FAC.IDINSTITUCION)
           AND EST_FAC.IDFACTURACION = P_IDFACTURACION
           AND EST_FAC.IDINSTITUCION = P_IDINSTITUCION
           AND EST_FAC.IDESTADOFACTURACION = EST.IDESTADOFACTURACION;

        -- VER SI ES PREVISION
        SELECT Prevision
            INTO V_PREVISION
        FROM FCS_FACTURACIONJG
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDFACTURACION = P_IDFACTURACION;

        --ELIMINAR DE ACTUACION DESIGNA
        V_DATOSERROR2 := 'Borrar Factura antes de borrar en Actuacion Designa';        
        PROC_FCS_ELIMINAR_ACT_DES(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_2;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Actuacion Designa';

        --ELIMINAR DE ACTUACION ASISTENCIA              
        PROC_FCS_ELIMINAR_ACT_ASIS(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_4;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Actuacion Asistencia';
        
        --ELIMINAR DE ASISTENCIA
        PROC_FCS_ELIMINAR_ASISTENCIA(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_3;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Asistencia';

        --ELIMINAR DE GUARDIAS COLEGIADO
        PROC_FCS_ELIMINAR_GUAR_COL(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_5;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Guardias Colegiado';
        
        --ELIMINAR DE APUNTES (CABECERA DE GUARDIAS)        
        PROC_FCS_ELIMINAR_APUNTES(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_5;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Apuntes';
        
        --ELIMINAR DE EXPEDIENTES SOJ        
        PROC_FCS_ELIMINAR_EXP_SOJ(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_6;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Expedientes SOJ';
        
        --ELIMINAR DE EXPEDIENTES EJG        
        PROC_FCS_ELIMINAR_EXP_EJG(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        IF (V_CODRETORNO2 <> '0') THEN
            RAISE E_ERROR_BF_7;
        END IF;
        V_DATOSERROR2 := 'Borrar Factura despues de borrar en Expedientes EJG';
      
        --borrar los historicos              
        PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTO_BORRAR_FACT(P_IDINSTITUCION, P_IDFACTURACION, V_CODRETORNO2, V_DATOSERROR2);
        V_DATOSERROR2 := 'Borrar Factura despues de borrar historicos';

        -- BORRAR LA FACTURACION. POR BORRADO EN CASCADA SE BORRA EL ESTADO Y LOS APUNTES
        DELETE FROM FCS_FACTURACIONJG
        WHERE IDFACTURACION = P_IDFACTURACION
            AND IDINSTITUCION = P_IDINSTITUCION;
        V_DATOSERROR2 := 'Borrar: Factura estado ejecutada, despues de borrar la Facturacion';

        V_DATOSERROR2 := 'Fin correcto';
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;

        EXCEPTION
            WHEN E_ERROR_BF_1 THEN
                P_CODRETORNO := '1';
                P_DATOSERROR := 'messages.factSJCS.error.estadoNoCorrecto';
            WHEN E_ERROR_BF_2 THEN
                P_CODRETORNO := '2';
                P_DATOSERROR := 'messages.factSJCS.error.borrarActuacionDesignas';
            WHEN E_ERROR_BF_3 THEN
                P_CODRETORNO := '3';
                P_DATOSERROR := 'messages.factSJCS.error.borrarAsistencias';
            WHEN E_ERROR_BF_4 THEN
                P_CODRETORNO := '4';
                P_DATOSERROR := 'messages.factSJCS.error.borrarActuacionDesignas';
            WHEN E_ERROR_BF_5 THEN
                P_CODRETORNO := '5';
                P_DATOSERROR := 'messages.factSJCS.error.borrarGuardias';
            WHEN E_ERROR_BF_6 THEN
                P_CODRETORNO := '6';
                P_DATOSERROR := 'messages.factSJCS.error.borrarExpedientesSoj';
            WHEN E_ERROR_BF_7 THEN
                P_CODRETORNO := '7';
                P_DATOSERROR := 'messages.factSJCS.error.borrarExpedientesEjg';
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM;
    END PROC_FCS_BORRAR_FACTURACION;

    /* Nombre:      F_NUM2CHAR
      -- Descripcion: Funcion que comprueba si la persona trabaja para una sociedad
      --
      -- Parametros  IN/OUT  Descripcion                            Tipo de Datos
      -- ----------  ------  -------------------------------------  -------------
      -- P_CADENA    IN      cadena para reemplazar el '.' por ','  VARCHAR2
      --
      -- Historico
      -- ---------
      -- Version: 18/04/2006 - Pilar Duran Munoz: Creacion
      -- Version: 10/02/2008 - Adrian Ayala Gomez: Limpiada y quitada de la
      --          Especificacion*/
    FUNCTION F_NUM2CHAR(P_CADENA NUMBER) RETURN VARCHAR2 IS
        v_cadena varchar2(4000);
    BEGIN
        v_cadena := replace(p_cadena, '.', ',');
        return(v_cadena);
    END F_NUM2CHAR;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_EXPORTAR_SOJ                                                                       */
    /* Descripcion: Exporta los datos facturados, pagos o pagos por persona de los SOJ
    /*              segun esten rellenos el P_IDFACTURACION, P_IDPAGO, P_IDPERSONA
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
    /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
    /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
    /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 14/03/2005                                                                                   */
    /* Autor:         Pilar Duran Mu?oz
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /*
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_EXPORTAR_SOJ(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDPAGO        IN NUMBER,
        P_IDPERSONA     IN NUMBER,
        P_PATHFICHERO   IN VARCHAR2,
        P_FICHERO       IN VARCHAR2,
        P_CABECERAS     IN VARCHAR2,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

        V_PERSONA VARCHAR2(500) := '';
        V_TURNO   VARCHAR2(500) := '';
        F_SALIDA   UTL_FILE.FILE_TYPE;
        V_REGISTRO VARCHAR2(4000);
        ESPACIO CONSTANT VARCHAR2(6) := CHR(9);
        v_precio             number(10, 2) := 0;
        V_PERSONAOLD         varchar2(4000);
        V_PERSONANEW         varchar2(4000);
        V_POSICION           NUMBER := 0;
        v_cabletrado         varchar2(4000);
        v_cabturno           varchar2(4000);
        v_cabexped           varchar2(4000);
        v_cabfecha           varchar2(4000);
        v_cabbenef           varchar2(4000);
        v_cabimporte         varchar2(4000);
        v_cabtotal           varchar2(4000);
        v_cabeceras          varchar2(4000);
        v_cabtotalacum       varchar2(4000);
        v_cabImportePagReal  varchar2(4000);
        --v_cabImporteIRPFReal varchar2(4000);
        v_cabImportePagAct   varchar2(4000);
        --v_cabImporteIRPFAct  varchar2(4000);
        v_cabTotalPagoReal   varchar2(4000);
        --v_cabTotalIRPFReal   varchar2(4000);
        v_cabTotalPagoAct    varchar2(4000);
        --v_cabTotalIRPFAct    varchar2(4000);
        V_TOTALPAGOSREAL     NUMBER := 0; -- acumulado de v_cabTotalPagoReal
        V_TOTALIRPFREAL      NUMBER := 0; -- acumulado de v_cabTotalIRPFReal
        V_TOTALPAGOACTUAL    NUMBER := 0; -- acumulado de v_cabTotalPagoAct
        V_TOTALIRPFACTUAL    NUMBER := 0; -- acumulado de v_cabTotalIRPFAct
        v_cabguardia         varchar2(4000);
        CONT                 NUMBER := 0;

        /* Cursor Facturacion SOJ*/
        CURSOR C_FACTSOJ IS
            SELECT FCS_FACT_SOJ.IDPERSONA,
                FCS_FACT_SOJ.IDINSTITUCION,
                ' ' || FCS_FACT_SOJ.ANIO || '/' || SCS_SOJ.NUMSOJ AS NEXPEDIENTE,
                TO_CHAR(FCS_FACT_SOJ.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA,
                FCS_FACT_SOJ.IDTURNO,
                SCS_PERSONAJG.NOMBRE || ' ' || SCS_PERSONAJG.APELLIDO1 || ' ' ||
                SCS_PERSONAJG.APELLIDO2 BENEFICIARIO,
                FCS_FACT_SOJ.PRECIOAPLICADO,
                CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ',' ||
                CEN_PERSONA.NOMBRE V_PERSONA,
                SCS_TURNO.ABREVIATURA V_TURNO,
                SCS_GUARDIASTURNO.NOMBRE GUARDIA
            FROM FCS_FACT_SOJ,
                SCS_SOJ,
                SCS_GUARDIASTURNO,
                SCS_TURNO,
                SCS_PERSONAJG,
                CEN_PERSONA
            WHERE FCS_FACT_SOJ.IDINSTITUCION = P_IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = P_IDFACTURACION
                AND FCS_FACT_SOJ.IDINSTITUCION = SCS_SOJ.IDINSTITUCION
                AND FCS_FACT_SOJ.IDTIPOSOJ = SCS_SOJ.IDTIPOSOJ
                AND FCS_FACT_SOJ.ANIO = SCS_SOJ.ANIO
                AND FCS_FACT_SOJ.NUMERO = SCS_SOJ.NUMERO
                AND SCS_SOJ.IDTURNO = SCS_TURNO.IDTURNO
                AND SCS_SOJ.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
                AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
                AND SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO
                AND SCS_GUARDIASTURNO.IDTURNO = SCS_SOJ.IDTURNO
                AND SCS_GUARDIASTURNO.IDGUARDIA = SCS_SOJ.IDGUARDIA
                AND SCS_PERSONAJG.IDPERSONA(+) = SCS_SOJ.IDPERSONAJG
                AND SCS_PERSONAJG.IDINSTITUCION(+) = SCS_SOJ.IDINSTITUCION
                AND CEN_PERSONA.IDPERSONA = SCS_SOJ.IDPERSONA
            ORDER BY CEN_PERSONA.APELLIDOS1 || CEN_PERSONA.APELLIDOS2 || CEN_PERSONA.NOMBRE ASC,
                FCS_FACT_SOJ.FECHAAPERTURA ASC;

        /* Cursor de Pagos SOJ*/
        CURSOR C_PAGOSOJ IS
            SELECT FCS_FACT_SOJ.IDPERSONA,
                FCS_FACT_SOJ.IDINSTITUCION,
                ' ' || FCS_FACT_SOJ.ANIO || '/' || SCS_SOJ.NUMSOJ AS NEXPEDIENTE,
                TO_CHAR(FCS_FACT_SOJ.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA,
                FCS_FACT_SOJ.IDTURNO,
                SCS_PERSONAJG.NOMBRE || ' ' || SCS_PERSONAJG.APELLIDO1 || ' ' || SCS_PERSONAJG.APELLIDO2 AS BENEFICIARIO,
                FCS_FACT_SOJ.PRECIOAPLICADO,
                CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ',' || CEN_PERSONA.NOMBRE AS V_PERSONA,
                SCS_TURNO.ABREVIATURA AS V_TURNO,
                SCS_GUARDIASTURNO.NOMBRE AS GUARDIA,
                /* Diferencia con el cursor C_FACTSOJ*/
                NVL((
                    SELECT NVL(COL.IMPSOJ, 0)
                    FROM FCS_PAGO_COLEGIADO COL, 
                        FCS_PAGOSJG JG
                    WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                        AND COL.IDPAGOSJG = P_IDPAGO
                        AND JG.IDFACTURACION = P_IDFACTURACION
                        AND COL.IDINSTITUCION = JG.IDINSTITUCION
                        AND COL.IDPAGOSJG = JG.IDPAGOSJG
                        AND JG.IDINSTITUCION = FCS_FACT_SOJ.IDINSTITUCION
                        AND JG.IDFACTURACION = FCS_FACT_SOJ.IDFACTURACION
                        AND COL.IDPERORIGEN = FCS_FACT_SOJ.IDPERSONA
                    ),0) AS IMPORTEACTUALPAGADO,
                0 AS IMPORTEACTUALIRPF,
                NVL((
                    SELECT NVL(COL.IMPSOJ, 0)
                    FROM FCS_PAGO_COLEGIADO COL, 
                        FCS_PAGOSJG JG
                    WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                        AND COL.IDPAGOSJG = P_IDPAGO
                        AND JG.IDFACTURACION = P_IDFACTURACION
                        AND COL.IDINSTITUCION = JG.IDINSTITUCION
                        AND COL.IDPAGOSJG = JG.IDPAGOSJG
                        AND JG.IDINSTITUCION = FCS_FACT_SOJ.IDINSTITUCION
                        AND JG.IDFACTURACION = FCS_FACT_SOJ.IDFACTURACION
                        AND COL.IDPERORIGEN = FCS_FACT_SOJ.IDPERSONA
                    ),0) AS IMPORTETOTALPAGADO,
                0 AS IMPORTETOTALIRPF
                /* FIN Diferencia con el cursor C_FACTSOJ*/
            FROM FCS_FACT_SOJ,
                SCS_SOJ,
                SCS_GUARDIASTURNO,
                SCS_TURNO,
                SCS_PERSONAJG,
                CEN_PERSONA
            WHERE FCS_FACT_SOJ.IDINSTITUCION = P_IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = P_IDFACTURACION
                AND FCS_FACT_SOJ.IDINSTITUCION = SCS_SOJ.IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = SCS_SOJ.IDFACTURACION
                AND FCS_FACT_SOJ.IDINSTITUCION = SCS_SOJ.IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = SCS_SOJ.IDFACTURACION
                AND FCS_FACT_SOJ.IDTIPOSOJ = SCS_SOJ.IDTIPOSOJ
                AND FCS_FACT_SOJ.ANIO = SCS_SOJ.ANIO
                AND FCS_FACT_SOJ.NUMERO = SCS_SOJ.NUMERO
                AND SCS_SOJ.IDTURNO = SCS_TURNO.IDTURNO
                AND SCS_SOJ.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
                AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
                AND SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO
                AND SCS_GUARDIASTURNO.IDTURNO = SCS_SOJ.IDTURNO
                AND SCS_GUARDIASTURNO.IDGUARDIA = SCS_SOJ.IDGUARDIA
                AND SCS_PERSONAJG.IDPERSONA(+) = SCS_SOJ.IDPERSONAJG
                AND SCS_PERSONAJG.IDINSTITUCION(+) = SCS_SOJ.IDINSTITUCION
                AND CEN_PERSONA.IDPERSONA = SCS_SOJ.IDPERSONA
            ORDER BY CEN_PERSONA.APELLIDOS1 || CEN_PERSONA.APELLIDOS2 || CEN_PERSONA.NOMBRE ASC,
                FCS_FACT_SOJ.FECHAAPERTURA ASC;

        /* Cursor de Pagos realizados por persona SOJ*/
        CURSOR C_PAGOPERSONASOJ IS
            SELECT FCS_FACT_SOJ.IDPERSONA,
                FCS_FACT_SOJ.IDINSTITUCION,
                ' ' || FCS_FACT_SOJ.ANIO || '/' || SCS_SOJ.NUMSOJ AS NEXPEDIENTE,
                TO_CHAR(FCS_FACT_SOJ.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA,
                FCS_FACT_SOJ.IDTURNO,
                SCS_PERSONAJG.NOMBRE || ' ' || SCS_PERSONAJG.APELLIDO1 || ' ' || SCS_PERSONAJG.APELLIDO2 AS BENEFICIARIO,
                FCS_FACT_SOJ.PRECIOAPLICADO,
                CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ',' || CEN_PERSONA.NOMBRE AS V_PERSONA,
                SCS_TURNO.ABREVIATURA AS V_TURNO,
                SCS_GUARDIASTURNO.NOMBRE AS GUARDIA,
                /* Diferencia con el cursor C_FACTSOJ*/
                NVL((
                    SELECT NVL(COL.IMPSOJ, 0)
                    FROM FCS_PAGO_COLEGIADO COL, 
                        FCS_PAGOSJG JG
                    WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                        AND COL.IDPAGOSJG = P_IDPAGO
                        AND JG.IDFACTURACION = P_IDFACTURACION
                        AND COL.IDINSTITUCION = JG.IDINSTITUCION
                        AND COL.IDPAGOSJG = JG.IDPAGOSJG
                        AND JG.IDINSTITUCION = FCS_FACT_SOJ.IDINSTITUCION
                        AND JG.IDFACTURACION = FCS_FACT_SOJ.IDFACTURACION
                        AND COL.IDPERORIGEN = FCS_FACT_SOJ.IDPERSONA
                    ), 0) AS IMPORTEACTUALPAGADO,
                0 AS IMPORTEACTUALIRPF,
                NVL((
                    SELECT NVL(COL.IMPSOJ, 0)
                    FROM FCS_PAGO_COLEGIADO COL, 
                        FCS_PAGOSJG JG
                    WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                        AND COL.IDPAGOSJG = P_IDPAGO
                        AND JG.IDFACTURACION = P_IDFACTURACION
                        AND COL.IDINSTITUCION = JG.IDINSTITUCION
                        AND COL.IDPAGOSJG = JG.IDPAGOSJG
                        AND JG.IDINSTITUCION = FCS_FACT_SOJ.IDINSTITUCION
                        AND JG.IDFACTURACION = FCS_FACT_SOJ.IDFACTURACION
                        AND COL.IDPERORIGEN = FCS_FACT_SOJ.IDPERSONA
                    ),0) AS IMPORTETOTALPAGADO,
                0 AS IMPORTETOTALIRPF
            FROM FCS_FACT_SOJ,
                SCS_SOJ,
                SCS_GUARDIASTURNO,
                SCS_TURNO,
                SCS_PERSONAJG,
                CEN_PERSONA
            WHERE FCS_FACT_SOJ.IDINSTITUCION = P_IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = P_IDFACTURACION
                AND FCS_FACT_SOJ.IDPERSONA = P_IDPERSONA
                -- Diferencia con el cursor C_PAGOSOJ
                AND FCS_FACT_SOJ.IDINSTITUCION = SCS_SOJ.IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = SCS_SOJ.IDFACTURACION
                AND FCS_FACT_SOJ.IDINSTITUCION = SCS_SOJ.IDINSTITUCION
                AND FCS_FACT_SOJ.IDFACTURACION = SCS_SOJ.IDFACTURACION
                AND FCS_FACT_SOJ.IDTIPOSOJ = SCS_SOJ.IDTIPOSOJ
                AND FCS_FACT_SOJ.ANIO = SCS_SOJ.ANIO
                AND FCS_FACT_SOJ.NUMERO = SCS_SOJ.NUMERO
                AND SCS_SOJ.IDTURNO = SCS_TURNO.IDTURNO
                AND SCS_SOJ.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
                AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
                AND SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO
                AND SCS_GUARDIASTURNO.IDTURNO = SCS_SOJ.IDTURNO
                AND SCS_GUARDIASTURNO.IDGUARDIA = SCS_SOJ.IDGUARDIA
                AND SCS_PERSONAJG.IDPERSONA(+) = SCS_SOJ.IDPERSONAJG
                AND SCS_PERSONAJG.IDINSTITUCION(+) = SCS_SOJ.IDINSTITUCION
                AND CEN_PERSONA.IDPERSONA = SCS_SOJ.IDPERSONA
            ORDER BY CEN_PERSONA.APELLIDOS1 || CEN_PERSONA.APELLIDOS2 || CEN_PERSONA.NOMBRE ASC,
                FCS_FACT_SOJ.FECHAAPERTURA ASC;

  Begin

    V_DATOSERROR := 'Comprobamos si el fichero esta abierto';
    IF UTL_FILE.IS_OPEN(F_SALIDA) THEN
      UTL_FILE.FCLOSE(F_SALIDA);
    END IF;
    /* Borramos el fichero si existiera*/
    BEGIN
      UTL_FILE.fremove(P_PATHFICHERO, P_FICHERO);
    EXCEPTION
      WHEN OTHERS THEN
        NULL;
    END;
    begin
      v_cabeceras := p_cabeceras;

      V_POSICION := instr(v_cabeceras, '#');
      IF (V_POSICION = 0) THEN

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error en cabecera.' || sqlerrm;
        RAISE E_ERROR;
      END IF;
      v_cabletrado := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabturno   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabguardia := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabexped   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabfecha   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabbenef   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabimporte := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);
      V_POSICION   := instr(v_cabeceras, '#');
      v_cabtotal   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras  := substr(v_cabeceras, v_posicion + 1);

      IF (p_idpago IS null or p_idpago = 0) then
        v_cabtotalacum := v_cabeceras;
      ELSE
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabtotalacum       := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagReal  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFReal := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagAct   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFAct  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabTotalIRPFReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoAct    := substr(v_cabeceras, 1, v_posicion - 1);
        --v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        --v_cabTotalIRPFAct    := v_cabeceras;
      END IF;

    exception
      when others then

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error en cabecera.' || sqlerrm;
        RAISE E_ERROR;
    end;
    V_DATOSERROR := 'Abrimos fichero';

    begin
      V_REGISTRO := v_cabletrado || ESPACIO || v_cabturno || ESPACIO ||
                    v_cabguardia || ESPACIO || v_cabexped || ESPACIO ||
                    v_cabfecha || ESPACIO || v_cabbenef || ESPACIO ||
                    v_cabimporte || ESPACIO;

      if (p_idpago IS NOT null) then
        V_REGISTRO := V_REGISTRO || v_cabImportePagReal || ESPACIO ||
                     --v_cabImporteIRPFReal||ESPACIO||
                      v_cabImportePagAct || ESPACIO ||
                     --v_cabImporteIRPFAct||ESPACIO||
                      v_cabtotalacum || ESPACIO || v_cabTotalPagoReal ||
                      ESPACIO ||
                     --v_cabTotalIRPFReal||ESPACIO||
                      v_cabTotalPagoAct; --||ESPACIO||
        --v_cabTotalIRPFAct||ESPACIO;
      else
        V_REGISTRO := V_REGISTRO || v_cabtotalacum || ESPACIO;
      end if;
    exception
      when others then

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error al escribir la cabecera.' || sqlerrm;
        raise E_ERROR;
    end;
    V_DATOSERROR := 'Escribimos cabeceras';

    if (P_IDPAGO is null) then
      -- exportamos la facturacion de SOJ
      FOR V_FACTSOJ IN C_FACTSOJ LOOP
        IF (CONT = 0) THEN
          /* Antes de escribir el primer registro, escribimos la cabecera*/
          F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
        END IF;
        V_DATOSERROR := 'Escribimos el cuerpo del fichero';
        CONT         := CONT + 1;
        V_PERSONANEW := V_FACTSOJ.V_PERSONA;
        IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
          V_REGISTRO   := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || F_NUM2CHAR(v_precio);
          V_DATOSERROR := 'Se produce un cambio de persona';
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          v_precio := 0;
        end if;
        v_precio := v_precio + V_FACTSOJ.Precioaplicado;

        V_REGISTRO := V_FACTSOJ.V_PERSONA || ESPACIO || V_FACTSOJ.V_TURNO ||
                      ESPACIO || V_FACTSOJ.GUARDIA || ESPACIO ||
                      replace(V_FACTSOJ.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                      V_FACTSOJ.FECHAAPERTURA || ESPACIO ||
                      replace(V_FACTSOJ.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                      F_NUM2CHAR(V_FACTSOJ.Precioaplicado) || ESPACIO;

        /* Grabar registro en el fichero */

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);

        V_PERSONAOLD := V_FACTSOJ.V_PERSONA;
      END LOOP;
    else
      if (P_IDPERSONA is not null) then
        -- exportamos la los Pagos por Persona de SOJ
        FOR V_PAGOPERSONASOJ IN C_PAGOPERSONASOJ LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          V_DATOSERROR := 'Escribimos el cuerpo del fichero';
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOPERSONASOJ.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            V_DATOSERROR := 'Se produce un cambio de persona';
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOPERSONASOJ.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOPERSONASOJ.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOPERSONASOJ.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOPERSONASOJ.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOPERSONASOJ.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF

          V_REGISTRO := V_PAGOPERSONASOJ.V_PERSONA || ESPACIO ||
                        V_PAGOPERSONASOJ.V_TURNO || ESPACIO ||
                        V_PAGOPERSONASOJ.GUARDIA || ESPACIO ||
                        replace(V_PAGOPERSONASOJ.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                        V_PAGOPERSONASOJ.FECHAAPERTURA || ESPACIO ||
                        replace(V_PAGOPERSONASOJ.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                        F_NUM2CHAR(V_PAGOPERSONASOJ.Precioaplicado) ||
                        ESPACIO ||
                        F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTEACTUALPAGADO) ||
                        ESPACIO ||
                       --F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTEACTUALIRPF)||ESPACIO||
                        F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOPERSONASOJ.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);

          V_PERSONAOLD := V_PAGOPERSONASOJ.V_PERSONA;
        END LOOP;
      else
        -- exportamos los Pagos de SOJ
        FOR V_PAGOSOJ IN C_PAGOSOJ LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          V_DATOSERROR := 'Escribimos el cuerpo del fichero';
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOSOJ.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            V_DATOSERROR := 'Se produce un cambio de persona';
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOSOJ.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOSOJ.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOSOJ.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOSOJ.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOSOJ.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF

          V_REGISTRO := V_PAGOSOJ.V_PERSONA || ESPACIO || V_PAGOSOJ.V_TURNO ||
                        ESPACIO || V_PAGOSOJ.GUARDIA || ESPACIO ||
                        replace(V_PAGOSOJ.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                        V_PAGOSOJ.FECHAAPERTURA || ESPACIO ||
                        replace(V_PAGOSOJ.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                        F_NUM2CHAR(V_PAGOSOJ.Precioaplicado) || ESPACIO ||
                        F_NUM2CHAR(V_PAGOSOJ.IMPORTEACTUALPAGADO) ||
                        ESPACIO ||
                       --F_NUM2CHAR(V_PAGOSOJ.IMPORTEACTUALIRPF)||ESPACIO||
                        F_NUM2CHAR(V_PAGOSOJ.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOSOJ.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);

          V_PERSONAOLD := V_PAGOSOJ.V_PERSONA;
        END LOOP;
      end if;
    end if;
    if (cont > 1) then
      V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO || ESPACIO ||
                    ESPACIO || ESPACIO || ESPACIO || ESPACIO || ESPACIO;
      if (p_idpago IS NOT null) then
        V_REGISTRO := V_REGISTRO || ESPACIO || ESPACIO ||
                     --ESPACIO||
                     --ESPACIO||
                      F_NUM2CHAR(v_precio) || ESPACIO ||
                      F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                     --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                      F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
        --F_NUM2CHAR(V_TOTALIRPFACTUAL);
      else
        V_REGISTRO := V_REGISTRO || F_NUM2CHAR(v_precio);
      end if;
      UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
      UTL_FILE.FFLUSH(F_SALIDA);
    end if;
    UTL_FILE.fclose(F_SALIDA);
    P_CODRETORNO := '0';
    P_DATOSERROR := 'Fin correcto';

  EXCEPTION
    WHEN E_ERROR THEN
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := V_CODRETORNO;
      P_DATOSERROR := V_DATOSERROR;
    WHEN UTL_FILE.WRITE_ERROR THEN
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5398';
      P_DATOSERROR := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    WHEN UTL_FILE.INVALID_FILEHANDLE THEN
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5396';
      P_DATOSERROR := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    WHEN UTL_FILE.INVALID_PATH THEN
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5394';
      P_DATOSERROR := 'LA RUTA NO ES CORRECTA';
    WHEN UTL_FILE.INVALID_MODE THEN
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5395';
      P_DATOSERROR := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    WHEN UTL_FILE.INVALID_OPERATION THEN
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := sqlcode || ' 5397';
      P_DATOSERROR := sqlerrm || ' OPERACION ERRONEA EN EL FICHERO';

    WHEN UTL_FILE.INTERNAL_ERROR THEN
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      /* Tratamiento de las matrices */
      P_CODRETORNO := '5401';
      P_DATOSERROR := 'ERROR INTERNO NO ESPECIFICADO';

    END PROC_FCS_EXPORTAR_SOJ;

    /****************************************************************************************************************/
    /* Nombre:   PROC_FCS_EXPORTAR_EJG                                                                       */
    /* Descripcion:
    /*                                                                                                              */
    /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
    /* -------------------   ------   ------------------------------------------------------------   -------------  */
    /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
    /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
    /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
    /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
    /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
    /*                                En caso de error devuelve el codigo de error Oracle                           */
    /*                                correspondiente.                                                              */
    /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
    /*                                En caso de error devuelve el mensaje de error Oracle                          */
    /*                                correspondiente.                                                              */
    /*                                                                                                              */
    /* Version:        1.0                                                                                          */
    /* Fecha Creacion: 14/03/2005                                                                                   */
    /* Autor:         Pilar Duran Mu?oz
    /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
    /* ------------------   ---------------------------------   --------------------------------------------------- */
    /*
    /****************************************************************************************************************/
    PROCEDURE PROC_FCS_EXPORTAR_EJG(
        P_IDINSTITUCION IN NUMBER,
        P_IDFACTURACION IN NUMBER,
        P_IDPAGO        IN NUMBER,
        P_IDPERSONA     IN NUMBER,
        P_PATHFICHERO   IN VARCHAR2,
        P_FICHERO       IN VARCHAR2,
        P_CABECERAS     IN VARCHAR2,
        P_CODRETORNO    OUT VARCHAR2,
        P_DATOSERROR    OUT VARCHAR2) IS

        V_PERSONA VARCHAR2(500) := '';
        V_TURNO   VARCHAR2(500) := '';
        F_SALIDA   UTL_FILE.FILE_TYPE;
        V_REGISTRO VARCHAR2(4000);
        ESPACIO CONSTANT VARCHAR2(6) := CHR(9);
        v_precio             number(10, 2) := 0;
        V_PERSONAOLD         varchar2(4000);
        V_PERSONANEW         varchar2(4000);
        CONT                 NUMBER := 0;
        V_POSICION           NUMBER := 0;
        v_cabletrado         varchar2(4000);
        v_cabturno           varchar2(4000);
        v_cabexped           varchar2(4000);
        v_cabfecha           varchar2(4000);
        v_cabbenef           varchar2(4000);
        v_cabimporte         varchar2(4000);
        v_cabtotal           varchar2(4000);
        v_cabeceras          varchar2(4000);
        v_cabtotalacum       varchar2(4000);
        v_cabguardia         varchar2(4000);
        v_cabCAJG            varchar2(4000);
        v_cabFechaCAJG       varchar2(4000);
        v_cabImportePagReal  varchar2(4000);
        --v_cabImporteIRPFReal varchar2(4000);
        v_cabImportePagAct   varchar2(4000);
        --v_cabImporteIRPFAct  varchar2(4000);
        v_cabTotalPagoReal   varchar2(4000);
        --v_cabTotalIRPFReal   varchar2(4000);
        v_cabTotalPagoAct    varchar2(4000);
        --v_cabTotalIRPFAct    varchar2(4000);
        V_TOTALPAGOSREAL     NUMBER := 0; -- acumulado de v_cabTotalPagoReal
        V_TOTALIRPFREAL      NUMBER := 0; -- acumulado de v_cabTotalIRPFReal
        V_TOTALPAGOACTUAL    NUMBER := 0; -- acumulado de v_cabTotalPagoAct
        V_TOTALIRPFACTUAL    NUMBER := 0; -- acumulado de v_cabTotalIRPFAct

    CURSOR C_FACTEJG IS
        SELECT FCS_FACT_EJG.IDPERSONA,
            FCS_FACT_EJG.IDINSTITUCION,
            ' ' || FCS_FACT_EJG.ANIO || '/' || SCS_EJG.NUMEJG AS NEXPEDIENTE,
            TO_CHAR(FCS_FACT_EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA,
            FCS_FACT_EJG.IDTURNO,
            SCS_PERSONAJG.NOMBRE || ' ' || SCS_PERSONAJG.APELLIDO1 || ' ' || SCS_PERSONAJG.APELLIDO2 AS BENEFICIARIO,
            FCS_FACT_EJG.PRECIOAPLICADO,
            CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ',' || CEN_PERSONA.NOMBRE AS V_PERSONA,
            SCS_TURNO.ABREVIATURA AS V_TURNO,
            SCS_GUARDIASTURNO.NOMBRE AS GUARDIA,
            SCS_EJG.NUMERO_CAJG,
            SCS_EJG.ANIOCAJG
        FROM FCS_FACT_EJG,
            SCS_EJG,
            SCS_GUARDIASTURNO,
            SCS_TURNO,
            SCS_PERSONAJG,
            CEN_PERSONA
        WHERE FCS_FACT_EJG.IDINSTITUCION = P_IDINSTITUCION
            AND FCS_FACT_EJG.IDFACTURACION = P_IDFACTURACION
            AND FCS_FACT_EJG.IDINSTITUCION = SCS_EJG.IDINSTITUCION
            AND FCS_FACT_EJG.IDTIPOEJG = SCS_EJG.IDTIPOEJG
            AND FCS_FACT_EJG.ANIO = SCS_EJG.ANIO
            AND FCS_FACT_EJG.NUMERO = SCS_EJG.NUMERO
            AND SCS_EJG.GUARDIATURNO_IDTURNO = SCS_TURNO.IDTURNO
            AND SCS_EJG.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
            AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
            AND SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO
            AND SCS_GUARDIASTURNO.IDTURNO = SCS_EJG.GUARDIATURNO_IDTURNO
            AND SCS_GUARDIASTURNO.IDGUARDIA = SCS_EJG.GUARDIATURNO_IDGUARDIA
            AND SCS_PERSONAJG.IDPERSONA(+) = SCS_EJG.IDPERSONAJG
            AND SCS_PERSONAJG.IDINSTITUCION(+) = SCS_EJG.IDINSTITUCION
            AND CEN_PERSONA.IDPERSONA = SCS_EJG.IDPERSONA
        ORDER BY CEN_PERSONA.APELLIDOS1 || CEN_PERSONA.APELLIDOS2 || CEN_PERSONA.NOMBRE ASC,
            FCS_FACT_EJG.FECHAAPERTURA ASC;

    CURSOR C_PAGOEJG IS
        SELECT FCS_FACT_EJG.IDPERSONA,
            FCS_FACT_EJG.IDINSTITUCION,
            ' ' || FCS_FACT_EJG.ANIO || '/' || SCS_EJG.NUMEJG AS NEXPEDIENTE,
            TO_CHAR(FCS_FACT_EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA,
            FCS_FACT_EJG.IDTURNO,
            SCS_PERSONAJG.NOMBRE || ' ' || SCS_PERSONAJG.APELLIDO1 || ' ' || SCS_PERSONAJG.APELLIDO2 AS BENEFICIARIO,
            FCS_FACT_EJG.PRECIOAPLICADO,
            CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ',' || CEN_PERSONA.NOMBRE AS V_PERSONA,
            SCS_TURNO.ABREVIATURA AS V_TURNO,
            SCS_GUARDIASTURNO.NOMBRE AS GUARDIA,
            SCS_EJG.NUMERO_CAJG,
            SCS_EJG.ANIOCAJG,
            NVL((
                SELECT NVL(COL.IMPEJG, 0)
                FROM FCS_PAGO_COLEGIADO COL, 
                    FCS_PAGOSJG JG
                WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                    AND COL.IDPAGOSJG = P_IDPAGO
                    AND JG.IDFACTURACION = P_IDFACTURACION
                    AND COL.IDINSTITUCION = JG.IDINSTITUCION
                    AND COL.IDPAGOSJG = JG.IDPAGOSJG
                    AND JG.IDINSTITUCION = FCS_FACT_EJG.IDINSTITUCION
                    AND JG.IDFACTURACION = FCS_FACT_EJG.IDFACTURACION
                    AND COL.IDPERORIGEN = FCS_FACT_EJG.IDPERSONA
                ),0) AS IMPORTEACTUALPAGADO,
            0 AS IMPORTEACTUALIRPF,
            NVL((
                SELECT NVL(COL.IMPEJG, 0)
                FROM FCS_PAGO_COLEGIADO COL, 
                    FCS_PAGOSJG JG
                WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                    AND COL.IDPAGOSJG = P_IDPAGO
                    AND JG.IDFACTURACION = P_IDFACTURACION
                    AND COL.IDINSTITUCION = JG.IDINSTITUCION
                    AND COL.IDPAGOSJG = JG.IDPAGOSJG
                    AND JG.IDINSTITUCION = FCS_FACT_EJG.IDINSTITUCION
                    AND JG.IDFACTURACION = FCS_FACT_EJG.IDFACTURACION
                    AND COL.IDPERORIGEN = FCS_FACT_EJG.IDPERSONA
                ),0) AS IMPORTETOTALPAGADO,
            0 AS IMPORTETOTALIRPF
        FROM FCS_FACT_EJG,
            SCS_EJG,
            SCS_GUARDIASTURNO,
            SCS_TURNO,
            SCS_PERSONAJG,
            CEN_PERSONA
        WHERE FCS_FACT_EJG.IDINSTITUCION = P_IDINSTITUCION
            AND FCS_FACT_EJG.IDFACTURACION = P_IDFACTURACION
            AND FCS_FACT_EJG.IDINSTITUCION = SCS_EJG.IDINSTITUCION
            AND FCS_FACT_EJG.IDFACTURACION = SCS_EJG.IDFACTURACION
            AND FCS_FACT_EJG.IDTIPOEJG = SCS_EJG.IDTIPOEJG
            AND FCS_FACT_EJG.ANIO = SCS_EJG.ANIO
            AND FCS_FACT_EJG.NUMERO = SCS_EJG.NUMERO
            AND SCS_EJG.GUARDIATURNO_IDTURNO = SCS_TURNO.IDTURNO
            AND SCS_EJG.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
            AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
            AND SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO
            AND SCS_GUARDIASTURNO.IDTURNO = SCS_EJG.GUARDIATURNO_IDTURNO
            AND SCS_GUARDIASTURNO.IDGUARDIA = SCS_EJG.GUARDIATURNO_IDGUARDIA
            AND SCS_PERSONAJG.IDPERSONA(+) = SCS_EJG.IDPERSONAJG
            AND SCS_PERSONAJG.IDINSTITUCION(+) = SCS_EJG.IDINSTITUCION
            AND CEN_PERSONA.IDPERSONA = SCS_EJG.IDPERSONA
        ORDER BY CEN_PERSONA.APELLIDOS1 || CEN_PERSONA.APELLIDOS2 || CEN_PERSONA.NOMBRE ASC,
            FCS_FACT_EJG.FECHAAPERTURA ASC;

    CURSOR C_PAGOPERSONAEJG IS
        SELECT FCS_FACT_EJG.IDPERSONA,
            FCS_FACT_EJG.IDINSTITUCION,
            ' ' || FCS_FACT_EJG.ANIO || '/' || SCS_EJG.NUMEJG AS NEXPEDIENTE,
            TO_CHAR(FCS_FACT_EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA,
            FCS_FACT_EJG.IDTURNO,
            SCS_PERSONAJG.NOMBRE || ' ' || SCS_PERSONAJG.APELLIDO1 || ' ' || SCS_PERSONAJG.APELLIDO2 AS BENEFICIARIO,
            FCS_FACT_EJG.PRECIOAPLICADO,
            CEN_PERSONA.APELLIDOS1 || ' ' || CEN_PERSONA.APELLIDOS2 || ',' || CEN_PERSONA.NOMBRE AS V_PERSONA,
            SCS_TURNO.ABREVIATURA AS V_TURNO,
            SCS_GUARDIASTURNO.NOMBRE AS GUARDIA,
            SCS_EJG.NUMERO_CAJG,
            SCS_EJG.ANIOCAJG,
            NVL((
                SELECT NVL(COL.IMPEJG, 0)
                FROM FCS_PAGO_COLEGIADO COL, 
                    FCS_PAGOSJG JG
                WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                    AND COL.IDPAGOSJG = P_IDPAGO
                    AND JG.IDFACTURACION = P_IDFACTURACION
                    AND COL.IDINSTITUCION = JG.IDINSTITUCION
                    AND COL.IDPAGOSJG = JG.IDPAGOSJG
                    AND JG.IDINSTITUCION = FCS_FACT_EJG.IDINSTITUCION
                    AND JG.IDFACTURACION = FCS_FACT_EJG.IDFACTURACION
                    AND COL.IDPERORIGEN = FCS_FACT_EJG.IDPERSONA
                ),0) AS IMPORTEACTUALPAGADO,
            0 AS IMPORTEACTUALIRPF,
            NVL((
                SELECT NVL(COL.IMPEJG, 0)
                FROM FCS_PAGO_COLEGIADO COL, 
                    FCS_PAGOSJG JG
                WHERE COL.IDINSTITUCION = P_IDINSTITUCION
                    AND COL.IDPAGOSJG = P_IDPAGO
                    AND JG.IDFACTURACION = P_IDFACTURACION
                    AND COL.IDINSTITUCION = JG.IDINSTITUCION
                    AND COL.IDPAGOSJG = JG.IDPAGOSJG
                    AND JG.IDINSTITUCION = FCS_FACT_EJG.IDINSTITUCION
                    AND JG.IDFACTURACION = FCS_FACT_EJG.IDFACTURACION
                    AND COL.IDPERORIGEN = FCS_FACT_EJG.IDPERSONA
                ),0) AS IMPORTETOTALPAGADO,
            0 IMPORTETOTALIRPF
        FROM FCS_FACT_EJG,
            SCS_EJG,
            SCS_GUARDIASTURNO,
            SCS_TURNO,
            SCS_PERSONAJG,
            CEN_PERSONA
        WHERE FCS_FACT_EJG.IDINSTITUCION = P_IDINSTITUCION
            AND FCS_FACT_EJG.IDFACTURACION = P_IDFACTURACION
            AND FCS_FACT_EJG.IDPERSONA = P_IDPERSONA
            AND FCS_FACT_EJG.IDINSTITUCION = SCS_EJG.IDINSTITUCION
            AND FCS_FACT_EJG.IDFACTURACION = SCS_EJG.IDFACTURACION
            AND FCS_FACT_EJG.IDTIPOEJG = SCS_EJG.IDTIPOEJG
            AND FCS_FACT_EJG.ANIO = SCS_EJG.ANIO
            AND FCS_FACT_EJG.NUMERO = SCS_EJG.NUMERO
            AND SCS_EJG.GUARDIATURNO_IDTURNO = SCS_TURNO.IDTURNO
            AND SCS_EJG.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
            AND SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION
            AND SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO
            AND SCS_GUARDIASTURNO.IDTURNO = SCS_EJG.GUARDIATURNO_IDTURNO
            AND SCS_GUARDIASTURNO.IDGUARDIA = SCS_EJG.GUARDIATURNO_IDGUARDIA
            AND SCS_PERSONAJG.IDPERSONA(+) = SCS_EJG.IDPERSONAJG
            AND SCS_PERSONAJG.IDINSTITUCION(+) = SCS_EJG.IDINSTITUCION
            AND CEN_PERSONA.IDPERSONA = SCS_EJG.IDPERSONA
        ORDER BY CEN_PERSONA.APELLIDOS1 || CEN_PERSONA.APELLIDOS2 || CEN_PERSONA.NOMBRE ASC,
            FCS_FACT_EJG.FECHAAPERTURA ASC;

  Begin

    /* Borramos el fichero si existiera*/
    IF UTL_FILE.IS_OPEN(F_SALIDA) THEN
      UTL_FILE.FCLOSE(F_SALIDA);
    END IF;
    BEGIN
      UTL_FILE.fremove(P_PATHFICHERO, P_FICHERO);
    EXCEPTION
      WHEN OTHERS THEN
        NULL;
    END;

    BEGIN
      v_cabeceras := p_cabeceras;
      V_POSICION  := instr(v_cabeceras, '#');
      IF (V_POSICION = 0) THEN

        V_CODRETORNO := sqlcode;
        V_DATOSERROR := 'Error en cabecera.';
        RAISE E_ERROR;
      END IF;

      v_cabletrado   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabturno     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabguardia   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabexped     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabfecha     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabCAJG      := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabFechaCAJG := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabbenef     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabimporte   := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      V_POSICION     := instr(v_cabeceras, '#');
      v_cabtotal     := substr(v_cabeceras, 1, v_posicion - 1);
      v_cabeceras    := substr(v_cabeceras, v_posicion + 1);
      IF (p_idpago IS null or p_idpago = 0) then
        v_cabtotalacum := v_cabeceras;
      ELSE
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabtotalacum       := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagReal  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFReal := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabImportePagAct   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabImporteIRPFAct  := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        --v_cabTotalIRPFReal   := substr(v_cabeceras, 1, v_posicion - 1);
        v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        V_POSICION           := instr(v_cabeceras, '#');
        v_cabTotalPagoAct    := substr(v_cabeceras, 1, v_posicion - 1);
        --v_cabeceras          := substr(v_cabeceras, v_posicion + 1);
        --v_cabTotalIRPFAct    := v_cabeceras;
      END IF;
    EXCEPTION
      WHEN OTHERS THEN

        V_CODRETORNO := SQLCODE;
        V_DATOSERROR := 'Error en la cabecera ' || sqlerrm;
        raise E_ERROR;
    end;

    V_REGISTRO := v_cabletrado || ESPACIO || v_cabturno || ESPACIO ||
                  v_cabguardia || ESPACIO || v_cabexped || ESPACIO ||
                  v_cabfecha || ESPACIO || v_cabCAJG || ESPACIO ||
                  v_cabFechaCAJG || ESPACIO || v_cabbenef || ESPACIO ||
                  v_cabimporte || ESPACIO;

    if (p_idpago IS NOT null) then
      V_REGISTRO := V_REGISTRO || v_cabImportePagReal || ESPACIO ||
                   --v_cabImporteIRPFReal||ESPACIO||
                    v_cabImportePagAct || ESPACIO ||
                   --v_cabImporteIRPFAct||ESPACIO||
                    v_cabtotalacum || ESPACIO || v_cabTotalPagoReal ||
                    ESPACIO ||
                   --v_cabTotalIRPFReal||ESPACIO||
                    v_cabTotalPagoAct; --||ESPACIO||
      --v_cabTotalIRPFAct||ESPACIO;
    else
      V_REGISTRO := V_REGISTRO || v_cabtotalacum || ESPACIO;
    end if;

    V_PERSONAOLD := '';
    IF (P_IDPAGO IS NULL OR P_IDPAGO = 0) THEN
      FOR V_FACTEJG IN C_FACTEJG LOOP

        IF (CONT = 0) THEN
          /* Antes de escribir el primer registro, escribimos la cabecer*/
          F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
        END IF;
        CONT         := CONT + 1;
        V_PERSONANEW := V_FACTEJG.V_PERSONA;
        IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
          V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                        ESPACIO || ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                        ESPACIO || ESPACIO || ESPACIO ||
                        F_NUM2CHAR(v_precio);
          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          v_precio := 0;
        end if;
        v_precio     := v_precio + V_FACTEJG.Precioaplicado;
        V_DATOSERROR := 'Antes de escribir en el fichero';
        V_REGISTRO   := V_FACTEJG.V_PERSONA || ESPACIO || V_FACTEJG.V_TURNO ||
                        ESPACIO || V_FACTEJG.GUARDIA || ESPACIO ||
                        replace(V_FACTEJG.NEXPEDIENTE,chr(9),' ')  || ESPACIO ||
                        V_FACTEJG.FECHAAPERTURA || ESPACIO ||
                        replace(V_FACTEJG.NUMERO_CAJG,chr(9),' ')  || ESPACIO ||
                        V_FACTEJG.ANIOCAJG || ESPACIO ||
                        replace(V_FACTEJG.BENEFICIARIO,chr(9),' ')  || ESPACIO ||
                        F_NUM2CHAR(V_FACTEJG.Precioaplicado);

        /* Grabar registro en el fichero */

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);
        V_PERSONAOLD := V_FACTEJG.V_PERSONA;

      END LOOP;
    ELSE
      IF (P_IDPERSONA IS NOT NULL) THEN
        FOR V_PAGOPERSONAEJG IN C_PAGOPERSONAEJG LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOPERSONAEJG.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOPERSONAEJG.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOPERSONAEJG.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOPERSONAEJG.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOPERSONAEJG.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOPERSONAEJG.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF

          V_DATOSERROR := 'Antes de escribir en el fichero';
          V_REGISTRO   := V_PAGOPERSONAEJG.V_PERSONA || ESPACIO ||
                          V_PAGOPERSONAEJG.V_TURNO || ESPACIO ||
                          V_PAGOPERSONAEJG.GUARDIA || ESPACIO ||
                          replace(V_PAGOPERSONAEJG.NEXPEDIENTE,chr(9),' ')  || ESPACIO ||
                          V_PAGOPERSONAEJG.FECHAAPERTURA || ESPACIO ||
                          replace(V_PAGOPERSONAEJG.NUMERO_CAJG,chr(9),' ')  || ESPACIO ||
                          V_PAGOPERSONAEJG.ANIOCAJG || ESPACIO ||
                          replace(V_PAGOPERSONAEJG.BENEFICIARIO,chr(9),' ')  || ESPACIO ||
                          F_NUM2CHAR(V_PAGOPERSONAEJG.Precioaplicado) ||
                          ESPACIO ||
                          F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTEACTUALPAGADO) ||
                          ESPACIO ||
                         --F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTEACTUALIRPF)||ESPACIO||
                          F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOPERSONAEJG.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          V_PERSONAOLD := V_PAGOPERSONAEJG.V_PERSONA;

        END LOOP;

      ELSE

        FOR V_PAGOEJG IN C_PAGOEJG LOOP
          IF (CONT = 0) THEN
            /* Antes de escribir el primer registro, escribimos la cabecera*/
            F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
          END IF;
          CONT         := CONT + 1;
          V_PERSONANEW := V_PAGOEJG.V_PERSONA;
          IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
            V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                          ESPACIO || ESPACIO ||
                         --ESPACIO||
                         --ESPACIO||
                          F_NUM2CHAR(v_precio) || ESPACIO ||
                          F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                         --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                          F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
            --F_NUM2CHAR(V_TOTALIRPFACTUAL);
            UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
            UTL_FILE.FFLUSH(F_SALIDA);
            v_precio          := 0;
            V_TOTALPAGOSREAL  := 0;
            V_TOTALIRPFREAL   := 0;
            V_TOTALPAGOACTUAL := 0;
            V_TOTALIRPFACTUAL := 0;
          end if;
          v_precio          := v_precio + V_PAGOEJG.Precioaplicado;
          V_TOTALPAGOSREAL  := V_TOTALPAGOSREAL +
                               V_PAGOEJG.IMPORTEACTUALPAGADO; -- guardamos el acumulado del importe actual pagado
          V_TOTALIRPFREAL   := V_TOTALIRPFREAL +
                               V_PAGOEJG.IMPORTEACTUALIRPF; -- guardamos el acumulado del importe IRPF realizado
          V_TOTALPAGOACTUAL := V_TOTALPAGOACTUAL +
                               V_PAGOEJG.IMPORTETOTALPAGADO; -- guardamos el acumulado del importe Total pagado
          V_TOTALIRPFACTUAL := V_TOTALIRPFACTUAL +
                               V_PAGOEJG.IMPORTETOTALIRPF; -- guardamos el acumulado del importe Total IRPF
          V_DATOSERROR      := 'Antes de escribir en el fichero';
          V_REGISTRO        := V_PAGOEJG.V_PERSONA || ESPACIO ||
                               V_PAGOEJG.V_TURNO || ESPACIO ||
                               V_PAGOEJG.GUARDIA || ESPACIO ||
                               replace(V_PAGOEJG.NEXPEDIENTE,chr(9),' ') || ESPACIO ||
                               V_PAGOEJG.FECHAAPERTURA || ESPACIO ||
                               replace(V_PAGOEJG.NUMERO_CAJG,chr(9),' ') || ESPACIO ||
                               V_PAGOEJG.ANIOCAJG || ESPACIO ||
                               replace(V_PAGOEJG.BENEFICIARIO,chr(9),' ') || ESPACIO ||
                               F_NUM2CHAR(V_PAGOEJG.Precioaplicado) ||
                               ESPACIO ||
                               F_NUM2CHAR(V_PAGOEJG.IMPORTEACTUALPAGADO) ||
                               ESPACIO ||
                              --F_NUM2CHAR(V_PAGOEJG.IMPORTEACTUALIRPF)||ESPACIO||
                               F_NUM2CHAR(V_PAGOEJG.IMPORTETOTALPAGADO); --||ESPACIO||
          --F_NUM2CHAR(V_PAGOEJG.IMPORTETOTALIRPF);

          /* Grabar registro en el fichero */

          UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
          UTL_FILE.FFLUSH(F_SALIDA);
          V_PERSONAOLD := V_PAGOEJG.V_PERSONA;

        END LOOP;

      END IF;
    END IF;
    if (cont > 0) then
      V_REGISTRO := v_cabtotal || ' ' || V_PERSONAOLD || ESPACIO || ESPACIO ||
                    ESPACIO || ESPACIO || ESPACIO || ESPACIO || ESPACIO ||
                    ESPACIO || ESPACIO;
      if (p_idpago IS NOT null) then
        V_REGISTRO := V_REGISTRO || ESPACIO || ESPACIO ||
                     --ESPACIO||
                     --ESPACIO||
                      F_NUM2CHAR(v_precio) || ESPACIO ||
                      F_NUM2CHAR(V_TOTALPAGOSREAL) || ESPACIO ||
                     --F_NUM2CHAR(V_TOTALIRPFREAL)||ESPACIO||
                      F_NUM2CHAR(V_TOTALPAGOACTUAL); --||ESPACIO||
        --F_NUM2CHAR(V_TOTALIRPFACTUAL);
      else
        V_REGISTRO := V_REGISTRO || F_NUM2CHAR(v_precio);
      end if;
      UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
      UTL_FILE.FFLUSH(F_SALIDA);
    end if;
    UTL_FILE.fclose(F_SALIDA);
    P_CODRETORNO := '0';
    P_DATOSERROR := 'Fin correcto';
  EXCEPTION
    when E_ERROR then
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := TO_CHAR(V_CODRETORNO);
      P_DATOSERROR := V_DATOSERROR;
    WHEN UTL_FILE.WRITE_ERROR THEN
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5398';
      P_DATOSERROR := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    WHEN UTL_FILE.INVALID_FILEHANDLE THEN
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5396';
      P_DATOSERROR := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    WHEN UTL_FILE.INVALID_PATH THEN
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5394';
      P_DATOSERROR := 'LA RUTA NO ES CORRECTA';
    WHEN UTL_FILE.INVALID_MODE THEN
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5395';
      P_DATOSERROR := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    WHEN UTL_FILE.INVALID_OPERATION THEN
      -- OPERACION ERRONEA EN EL FICHERO

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);

      P_CODRETORNO := sqlcode || ' 5397';
      P_DATOSERROR := sqlerrm || ' OPERACION ERRONEA EN EL FICHERO';

    WHEN UTL_FILE.INTERNAL_ERROR THEN
      -- ERROR INTERNO NO ESPECIFICADO

      -- Cerrar fichero de salida
      UTL_FILE.FCLOSE(F_SALIDA);

      /* Tratamiento de las matrices */
      P_CODRETORNO := '5401';
      P_DATOSERROR := 'ERROR INTERNO NO ESPECIFICADO';

  END; --PROC_FCS_EXPORTAR_EJG

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_TURNOS_OFI                                                                      */
  /* Descripcion: Exporta los datos facturados, pagos o pagos por persona de actuaciones de designaciones
  /*              segun esten rellenos el P_IDFACTURACION, P_IDPAGO, P_IDPERSONA                                */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO            IN        Nombre del fichero                                             VARCHAR2,
  /* P_CABECERAS            IN      cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 14/03/2005                                                                                   */
  /* Autor:         Pilar Duran Mu?oz
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/
    PROCEDURE PROC_FCS_EXPORTAR_TURNOS_OFI(
        P_IDINSTITUCION  IN NUMBER,
        P_IDFACTURACION1 IN NUMBER,
        P_IDFACTURACION2 IN NUMBER,
        P_IDPERSONA      IN NUMBER,
        P_PATHFICHERO    IN VARCHAR2,
        P_FICHERO        IN VARCHAR2,
        P_IDIOMA         IN NUMBER,
        P_CODRETORNO     OUT VARCHAR2,
        P_DATOSERROR     OUT VARCHAR2) IS
        
        V_CONSEJO CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE;
    
    BEGIN
        -- Obtiene el consejo del colegio 
        V_CONSEJO := FUNC_CONSEJO_COLEGIO(P_IDINSTITUCION);

        IF (V_CONSEJO = C_CATALAN) THEN
            PROC_EXPORTAR_TURNOS_3001(
                P_IDINSTITUCION,
                P_IDFACTURACION1,
                P_IDFACTURACION2,
                P_IDPERSONA,
                P_PATHFICHERO,
                P_FICHERO,
                P_IDIOMA,
                P_CODRETORNO,
                P_DATOSERROR);
        ELSE
            PROC_EXPORTAR_TURNOS_OTRO(
                P_IDINSTITUCION,
                P_IDFACTURACION1,
                P_IDPERSONA,
                P_PATHFICHERO,
                P_FICHERO,
                P_IDIOMA,
                P_CODRETORNO,
                P_DATOSERROR);
        END IF;
    END PROC_FCS_EXPORTAR_TURNOS_OFI;

  PROCEDURE PROC_EXPORTAR_TURNOS_OTRO(P_IDINSTITUCION IN NUMBER,
                                      P_IDFACTURACION IN NUMBER,
                                      P_IDPERSONA     IN NUMBER,
                                      P_PATHFICHERO   IN VARCHAR2,
                                      P_FICHERO       IN VARCHAR2,
                                      P_IDIOMA        IN NUMBER,
                                      P_CODRETORNO    OUT VARCHAR2,
                                      P_DATOSERROR    OUT VARCHAR2) IS

    --Variables generales
    F_SALIDA   UTL_FILE.FILE_TYPE;
    V_REGISTRO VARCHAR2(4000);
    TABULADOR CONSTANT VARCHAR2(6) := CHR(9);
    V_PERSONAOLD         varchar2(4000);
    V_PERSONANEW         varchar2(4000);
    CONT                 NUMBER := 0;

    --Variables de acumulados
    v_TOTAL              NUMBER := 0;

    /* Declaracion del cursor para la facturacion de turnos de oficio*/
    CURSOR C_TURNOSOFI (P_IDINSTITUCION FCS_FACT_ACTUACIONDESIGNA.IDINSTITUCION%type,
                        P_IDFACTURACION FCS_FACT_ACTUACIONDESIGNA.IDFACTURACION%type,
                        P_IDPERSONA FCS_FACT_ACTUACIONASISTENCIA.IDPERSONA%type) IS
      Select Fact.Idinstitucion As Id_Idinstitucion,
             Fact.Anio As Id_Anio,
             Fact.Idturno As Id_Idturno,
             Fact.Numero As Id_Numero,
             Fact.Numeroasunto As Id_Numeroasunto,
             Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) As Numcolletrado,
             Per.Apellidos1 ||
             Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
             Per.Nombre As Letrado,
             Tur.Abreviatura As Turno,
             To_Char(Des.Fechaentrada, 'dd/mm/yyyy') As Fechadesigna,
             To_Char(Act.Fecha, 'dd/mm/yyyy') As Fechaactuacion,
             To_Char(Act.Fechajustificacion, 'dd/mm/yyyy') As Fechajustificacion,
             Des.Anio || '/' || Lpad(Des.Codigo, 5, '0') As Numdesigna,
             Act.Numeroasunto As Numactuacion,
             (Select Pro.Codigo
                From Scs_Procedimientos Pro
               Where Pro.Idinstitucion = Act.Idinstitucion
                 And Pro.Idprocedimiento = Act.Idprocedimiento) As Codprocedim,
             Fact.Procedimiento As Procedimientoactuacion,
             (Select f_Siga_Getrecurso(Pre.Descripcion, p_Idioma)
                From Scs_Pretension Pre
               Where Pre.Idinstitucion = Act.Idinstitucion
                 And Pre.Idpretension = Act.Idpretension) As Pretensionactuacion,
             Fact.Precioaplicado As Precioprocedim,
             Fact.Porcentajefacturado || '% - ' || Fact.Acreditacion As Acreditacion,
             Round(Fact.Precioaplicado * Fact.Porcentajefacturado / 100, 2) As Importe,
             Nvl(Des.Numprocedimiento, Des.Resumenasunto) As Numprocedimoasunto,
             f_Siga_Nif_Litigante(Fact.Idturno,
                                  Fact.Idinstitucion,
                                  Fact.Anio,
                                  Fact.Numero) As Niflitigante,
             f_Siga_Litigante(Fact.Idturno,
                              Fact.Idinstitucion,
                              Fact.Anio,
                              Fact.Numero) As Litigante,
             (Select Juz.Nombre
                From Scs_Juzgado Juz
               Where Juz.Idinstitucion = Act.Idinstitucion
                 And Juz.Idjuzgado = Act.Idjuzgado) As Juzgado,
             (Select Pob.Nombre
                From Scs_Juzgado Juz, Cen_Poblaciones Pob
               Where Juz.Idinstitucion = Act.Idinstitucion
                 And Juz.Idjuzgado = Act.Idjuzgado
                 And Juz.Idpoblacion = Pob.Idpoblacion) As Poblacion,
             (Select Tur.Nombre
                From Scs_Asistencia Asi, Scs_Turno Tur
               Where Asi.Idinstitucion = Tur.Idinstitucion
                 And Asi.Idturno = Tur.Idturno
                 And Asi.Idinstitucion = Fact.Idinstitucion
                 And Asi.Designa_Turno = Fact.Idturno
                 And Asi.Designa_Anio = Fact.Anio
                 And Asi.Designa_Numero = Fact.Numero
                 And Rownum = 1) As Turnoasistencia,
             (Select Gua.Nombre
                From Scs_Asistencia Asi, Scs_Guardiasturno Gua
               Where Asi.Idinstitucion = Gua.Idinstitucion
                 And Asi.Idturno = Gua.Idturno
                 And Asi.Idguardia = Gua.Idguardia
                 And Asi.Idinstitucion = Fact.Idinstitucion
                 And Asi.Designa_Turno = Fact.Idturno
                 And Asi.Designa_Anio = Fact.Anio
                 And Asi.Designa_Numero = Fact.Numero
                 And Rownum = 1) As Guardiaasistencia,
             (Select Asi.Anio || '/' || Lpad(Asi.Numero, 5, '0')
                From Scs_Asistencia Asi
               Where Asi.Idinstitucion = Fact.Idinstitucion
                 And Asi.Designa_Turno = Fact.Idturno
                 And Asi.Designa_Anio = Fact.Anio
                 And Asi.Designa_Numero = Fact.Numero
                 And Rownum = 1) As Numasistencia,
             (Select Ejg.Anio || '/' || Lpad(Ejg.Numejg, 5, '0')
                From Scs_Ejg Ejg, Scs_Ejgdesigna Ejgdes
               Where Ejg.Idinstitucion = Ejgdes.Idinstitucion
                 And Ejg.Idtipoejg = Ejgdes.Idtipoejg
                 And Ejg.Anio = Ejgdes.Anioejg
                 And Ejg.Numero = Ejgdes.Numeroejg
                 And Ejgdes.Idinstitucion = Fact.Idinstitucion
                 And Ejgdes.Idturno = Fact.Idturno
                 And Ejgdes.Aniodesigna = Fact.Anio
                 And Ejgdes.Numerodesigna = Fact.Numero
                 And Rownum = 1) As Nejg,
             (Select Case
                       When (Ejg.Aniocajg Is Not Null And
                            Ejg.Numero_Cajg Is Not Null) Then
                        Ejg.Aniocajg || '/' || Lpad(Ejg.Numero_Cajg, 5, '0')
                       When (Ejg.Aniocajg Is Null And
                            Ejg.Numero_Cajg Is Not Null) Then
                        f_Siga_Getrecurso('fcs.ficheroFacturacion.cabecera.sinAnyo',
                                          p_Idioma) || ' /' ||
                        Lpad(Ejg.Numero_Cajg, 5, '0')
                       When (Ejg.Aniocajg Is Not Null And
                            Ejg.Numero_Cajg Is Null) Then
                        Ejg.Aniocajg || '/ ' ||
                        f_Siga_Getrecurso('fcs.ficheroFacturacion.cabecera.sinNumero',
                                          p_Idioma)
                       Else
                        ''
                     End Case
                From Scs_Ejg Ejg, Scs_Ejgdesigna Ejgdes
               Where Ejg.Idinstitucion = Ejgdes.Idinstitucion
                 And Ejg.Idtipoejg = Ejgdes.Idtipoejg
                 And Ejg.Anio = Ejgdes.Anioejg
                 And Ejg.Numero = Ejgdes.Numeroejg
                 And Ejgdes.Idinstitucion = Fact.Idinstitucion
                 And Ejgdes.Idturno = Fact.Idturno
                 And Ejgdes.Aniodesigna = Fact.Anio
                 And Ejgdes.Numerodesigna = Fact.Numero
                 And Rownum = 1) As Ncajg

        From Fcs_Fact_Actuaciondesigna Fact,
             Scs_Actuaciondesigna      Act,
             Scs_Designa               Des,
             Cen_Colegiado             Col,
             Cen_Persona               Per,
             Scs_Turno                 Tur
       Where Fact.Idinstitucion = Act.Idinstitucion
         And Fact.Idturno = Act.Idturno
         And Fact.Anio = Act.Anio
         And Fact.Numero = Act.Numero
         And Fact.Numeroasunto = Act.Numeroasunto
         And Fact.Idinstitucion = Des.Idinstitucion
         And Fact.Idturno = Des.Idturno
         And Fact.Anio = Des.Anio
         And Fact.Numero = Des.Numero
         And Fact.Idinstitucion = Col.Idinstitucion
         And Fact.Idpersona = Col.Idpersona
         And Fact.Idpersona = Per.Idpersona
         And Fact.Idinstitucion = Tur.Idinstitucion
         And Fact.Idturno = Tur.Idturno

         And Fact.Idinstitucion = p_Idinstitucion
         And Fact.Idfacturacion = p_Idfacturacion
         And Fact.Idpersona = Nvl(p_Idpersona, Fact.Idpersona)

       Order By Per.Apellidos1 ||
                Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
                Per.Nombre Asc,
                Des.Anio || '/' || Lpad(Des.Codigo, 5, '0'),
                Act.Numeroasunto;

  BEGIN

    --Recuperacion de cabeceras
    Select Replace(descripcion, '##', Tabulador) Into v_Registro
      From Gen_Recursos
     Where idrecurso = 'fcs.ficheroFacturacion.cabecera.turnos'
       And idlenguaje = p_idioma;

    --Comprobacion de fichero
    IF UTL_FILE.IS_OPEN(F_SALIDA) THEN
      UTL_FILE.FCLOSE(F_SALIDA);
    END IF;

    V_PERSONAOLD := '';
    FOR V_PAGO IN C_TURNOSOFI(P_IDINSTITUCION, P_IDFACTURACION, P_IDPERSONA) LOOP

      IF (CONT = 0) THEN
         /* Antes de escribir el primer registro, escribimos la cabecera*/
         F_SALIDA := UTL_FILE.FOPEN(P_PATHFICHERO, P_FICHERO, 'w');
         UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
         UTL_FILE.FFLUSH(F_SALIDA);
      END IF;

      CONT         := CONT + 1;
      V_PERSONANEW := V_PAGO.LETRADO;
      IF (CONT > 1 AND V_PERSONAOLD <> V_PERSONANEW) THEN
        BEGIN
          V_REGISTRO :=
                     'TOTAL:' || TABULADOR ||
                     V_PERSONAOLD || TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR || F_NUM2CHAR(V_TOTAL);

        EXCEPTION
          WHEN OTHERS THEN
            V_CODRETORNO := SQLCODE;
            V_DATOSERROR := SQLERRM || ' Error en la cabecera';
            raise E_ERROR;
        END;

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);
        V_TOTAL           := 0;
      end if;

      V_DATOSERROR := 'Antes de escribir el registro en el fichero';
      BEGIN
        V_REGISTRO :=
                   V_PAGO.NUMCOLLETRADO || TABULADOR ||
                   replace(V_PAGO.LETRADO,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.TURNO,chr(9),' ') || TABULADOR ||
                   V_PAGO.FECHADESIGNA || TABULADOR ||
                   V_PAGO.FECHAACTUACION || TABULADOR ||
                   V_PAGO.FECHAJUSTIFICACION || TABULADOR ||
                   V_PAGO.NUMDESIGNA || TABULADOR ||
                   V_PAGO.NUMACTUACION || TABULADOR ||
                   replace(V_PAGO.CODPROCEDIM,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.PROCEDIMIENTOACTUACION,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.PRETENSIONACTUACION,chr(9),' ') || TABULADOR ||
                   f_num2char(V_PAGO.PRECIOPROCEDIM) || TABULADOR ||
                   replace(V_PAGO.ACREDITACION,chr(9),' ') || TABULADOR ||
                   f_num2char(V_PAGO.IMPORTE) || TABULADOR ||
                   TABULADOR || --separador
                   replace(V_PAGO.NUMPROCEDIMOASUNTO,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.NIFLITIGANTE,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.LITIGANTE,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.JUZGADO,chr(9),' ') || TABULADOR ||
                   V_PAGO.POBLACION || TABULADOR ||
                   replace(V_PAGO.TURNOASISTENCIA,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.GUARDIAASISTENCIA,chr(9),' ') || TABULADOR ||
                   V_PAGO.NUMASISTENCIA || TABULADOR ||
                   replace(V_PAGO.NEJG,chr(9),' ') || TABULADOR ||
                   replace(V_PAGO.NCAJG,chr(9),' ') || TABULADOR;

      EXCEPTION
        WHEN OTHERS THEN
          V_CODRETORNO := SQLCODE;
          V_DATOSERROR := SQLERRM || ' ' || v_datoserror;
          raise E_ERROR;
      END;

      UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
      UTL_FILE.FFLUSH(F_SALIDA);
      BEGIN
        -- Para cada letrado calculamos el precio total aplicado
        V_DATOSERROR      := 'Antes de calcular el total';
        V_TOTAL           := V_TOTAL +
                             V_PAGO.IMPORTE;
      EXCEPTION
        WHEN OTHERS THEN
          V_CODRETORNO := SQLCODE;
          V_DATOSERROR := SQLERRM || ' Error en la cabecera';
          raise E_ERROR;
      END;

      V_PERSONAOLD := V_PAGO.LETRADO;
    END LOOP;

    begin --escribiendo ultimo registro
      V_DATOSERROR := 'El ultimo registro debe ser el total acumulado para el letrado';
      if (cont > 0) then
        V_REGISTRO :=
                     'TOTAL:' || TABULADOR ||
                     V_PERSONAOLD || TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR ||
                     TABULADOR || F_NUM2CHAR(V_TOTAL);

        UTL_FILE.PUTF(F_SALIDA, V_REGISTRO || CHR(13) || CHR(10));
        UTL_FILE.FFLUSH(F_SALIDA);
      end if;
    exception
      WHEN OTHERS THEN
        V_CODRETORNO := SQLCODE;
        V_DATOSERROR := SQLERRM || ' ' || v_datoserror;
        raise E_ERROR;
    end;

    begin --cerrando fichero
      UTL_FILE.fclose(F_SALIDA);
      P_CODRETORNO := '0';
      P_DATOSERROR := 'Fin correcto';
    end;

  EXCEPTION
    when e_error then
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := to_char(v_codretorno);
      P_DATOSERROR := v_datoserror;
    WHEN UTL_FILE.WRITE_ERROR THEN
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5398';
      P_DATOSERROR := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    WHEN UTL_FILE.INVALID_FILEHANDLE THEN
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5396';
      P_DATOSERROR := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    WHEN UTL_FILE.INVALID_PATH THEN
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5394';
      P_DATOSERROR := 'LA RUTA NO ES CORRECTA';
    WHEN UTL_FILE.INVALID_MODE THEN
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := '5395';
      P_DATOSERROR := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    WHEN UTL_FILE.INVALID_OPERATION THEN
      -- OPERACION ERRONEA EN EL FICHERO

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);

      P_CODRETORNO := sqlcode || ' 5397';
      P_DATOSERROR := sqlerrm || ' OPERACION ERRONEA EN EL FICHERO';

    WHEN UTL_FILE.INTERNAL_ERROR THEN
      -- ERROR INTERNO NO ESPECIFICADO

      -- Cerrar fichero de salida

      UTL_FILE.FCLOSE(F_SALIDA);

      /* Tratamiento de las matrices */
      P_CODRETORNO := '5401';
      P_DATOSERROR := 'ERROR INTERNO NO ESPECIFICADO';
    when others then
      UTL_FILE.FCLOSE(F_SALIDA);
      P_CODRETORNO := to_Char(sqlcode);
      P_DATOSERROR := sqlerrm || ' ' ||
                      'ERROR EN LA EXPORTACION DE TURNOS DE OFICIO: ' ||
                      v_datoserror;

  End PROC_EXPORTAR_TURNOS_OTRO;

    Function F_GET_LISTA_DEF_DESIGNA_3001(
        P_IDINSTITUCION  IN SCS_DESIGNA.IDINSTITUCION%TYPE,
        P_IDTURNO        IN SCS_DESIGNA.IDTURNO%TYPE,
        P_ANIO           IN SCS_DESIGNA.ANIO%TYPE,
        P_NUMERO         IN SCS_DESIGNA.NUMERO%TYPE,
        P_TIPODEVOLUCION IN NUMBER,
        P_IDIOMA         IN NUMBER) RETURN VARCHAR2 IS

        CURSOR C_DEFENDIDOS IS
            SELECT PJG.NIF AS NIF,
                NVL(PJG.NOMBRE, '') ||
                DECODE(PJG.APELLIDO1, NULL, '', ' ' || PJG.APELLIDO1) ||
                DECODE(PJG.APELLIDO2, NULL, '', ' ' || PJG.APELLIDO2) AS NOMBRE,
                DECODE(P_IDIOMA, 2, DECODE(PJG.SEXO, 'H', 'H', 'M', 'D', NULL), PJG.SEXO) AS SEXO
            FROM SCS_DEFENDIDOSDESIGNA DEFDES, 
                SCS_PERSONAJG PJG
            WHERE DEFDES.IDINSTITUCION = PJG.IDINSTITUCION
                AND DEFDES.IDPERSONA = PJG.IDPERSONA
                AND DEFDES.IDINSTITUCION = P_IDINSTITUCION
                AND DEFDES.IDTURNO = P_IDTURNO
                AND DEFDES.ANIO = P_ANIO
                AND DEFDES.NUMERO = P_NUMERO
            ORDER BY 1, 2, 3, PJG.IDPERSONA;

        RESULTADO VARCHAR2(4000);

    BEGIN
        RESULTADO := '';

        FOR R_DEFENDIDO IN C_DEFENDIDOS LOOP
            IF RESULTADO IS NOT NULL THEN
                RESULTADO := RESULTADO || ', ';
            END IF;

            CASE P_TIPODEVOLUCION
                WHEN 1 THEN --NIF
                    RESULTADO := RESULTADO || R_DEFENDIDO.NIF;
                WHEN 2 THEN --Nombre
                    RESULTADO := RESULTADO || R_DEFENDIDO.NOMBRE;
                WHEN 3 THEN --Sexo
                    RESULTADO := RESULTADO || R_DEFENDIDO.SEXO;
                ELSE
                    RESULTADO := RESULTADO || R_DEFENDIDO.NIF || ' - ' || R_DEFENDIDO.NOMBRE || ' - ' || R_DEFENDIDO.SEXO;
            END CASE; 
        END LOOP;

        RETURN RESULTADO;
    END F_GET_LISTA_DEF_DESIGNA_3001;

    FUNCTION F_GET_LISTA_EJGS_DESIGNA_3001(
        P_IDINSTITUCION IN SCS_DESIGNA.IDINSTITUCION%TYPE,
        P_IDTURNO       IN SCS_DESIGNA.IDTURNO%TYPE,
        P_ANIO          IN SCS_DESIGNA.ANIO%TYPE,
        P_NUMERO        IN SCS_DESIGNA.NUMERO%TYPE) RETURN VARCHAR2 IS

        CURSOR CURSOR_EJG IS
            SELECT NVL(TIP.CODIGOEXT, ' ') || 
                '-' || LPAD(EJG.NUMEJG, 5, '0') || 
                '/' || SUBSTR(TO_CHAR(EJG.ANIO), 3, 2) AS NUMEJG
            FROM (
                    SELECT IDINSTITUCION AS IDINSTITUCION,
                        EJGIDTIPOEJG  AS IDTIPOEJG,
                        EJGANIO       AS ANIO,
                        EJGNUMERO     AS NUMERO
                    FROM SCS_ASISTENCIA ASI
                    WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
                        AND ASI.DESIGNA_TURNO = P_IDTURNO
                        AND ASI.DESIGNA_ANIO = P_ANIO
                        AND ASI.DESIGNA_NUMERO = P_NUMERO
                    UNION
                    SELECT IDINSTITUCION, 
                        IDTIPOEJG, 
                        ANIOEJG, 
                        NUMEROEJG
                    FROM SCS_EJGDESIGNA EJGDES
                    WHERE EJGDES.IDINSTITUCION = P_IDINSTITUCION
                        AND EJGDES.IDTURNO = P_IDTURNO
                        AND EJGDES.ANIODESIGNA = P_ANIO
                        AND EJGDES.NUMERODESIGNA = P_NUMERO
                ) RELACION,
                SCS_EJG EJG,
                SCS_TIPOEJGCOLEGIO TIP
            WHERE RELACION.IDINSTITUCION = EJG.IDINSTITUCION
                AND RELACION.IDTIPOEJG = EJG.IDTIPOEJG
                AND RELACION.ANIO = EJG.ANIO
                AND RELACION.NUMERO = EJG.NUMERO
                AND EJG.IDINSTITUCION = TIP.IDINSTITUCION(+)
                AND EJG.IDTIPOEJGCOLEGIO = TIP.IDTIPOEJGCOLEGIO(+)
            ORDER BY 1;

        RESULTADO VARCHAR2(4000);

    BEGIN
        RESULTADO := '';

        FOR R_EJG IN CURSOR_EJG LOOP
            IF RESULTADO IS NOT NULL THEN
                RESULTADO := RESULTADO || ', ';
            END IF;
            RESULTADO := RESULTADO || R_EJG.NUMEJG;
        END LOOP;

        RETURN RESULTADO;
    END F_GET_LISTA_EJGS_DESIGNA_3001;

    Function F_GET_LISTA_ASI_DESIGNA_3001(
        P_IDINSTITUCION IN SCS_DESIGNA.IDINSTITUCION%TYPE,
        P_IDTURNO       IN SCS_DESIGNA.IDTURNO%TYPE,
        P_ANIO          IN SCS_DESIGNA.ANIO%TYPE,
        P_NUMERO        IN SCS_DESIGNA.NUMERO%TYPE,
        P_PREFIJO       IN VARCHAR2) RETURN VARCHAR2 IS

        CURSOR C_ASISTENCIAS IS
            SELECT P_PREFIJO || LPAD(ASI.NUMERO, 5, '0') || '/' || SUBSTR(TO_CHAR(ASI.ANIO), 3, 2) AS NUMASI
            FROM SCS_ASISTENCIA ASI
            WHERE ASI.IDINSTITUCION = P_IDINSTITUCION
                AND ASI.DESIGNA_TURNO = P_IDTURNO
                AND ASI.DESIGNA_ANIO = P_ANIO
                AND ASI.DESIGNA_NUMERO = P_NUMERO
            ORDER BY 1;

        RESULTADO VARCHAR2(4000);

    BEGIN
        RESULTADO := '';

        FOR R_ASISTENCIA IN C_ASISTENCIAS LOOP
            IF RESULTADO IS NOT NULL THEN
                RESULTADO := RESULTADO || ', ';
            END IF;

            RESULTADO := RESULTADO || R_ASISTENCIA.NUMASI;
        END LOOP;

        RETURN RESULTADO;
    END F_GET_LISTA_ASI_DESIGNA_3001;

  Procedure PROC_EXPORTAR_TURNOS_3001(p_Idinstitucion  In Number,
                                      p_Idfacturacion1 In Number,
                                      p_Idfacturacion2 In Number,
                                      p_Idpersona      In Number,
                                      p_Pathfichero    In Varchar2,
                                      p_Fichero        In Varchar2,
                                      p_Idioma         In Number,
                                      p_Codretorno     Out Varchar2,
                                      p_Datoserror     Out Varchar2) Is

    -- VARIABLES
    f_Salida   Utl_File.File_Type; --fichero para escribir
    v_Registro Varchar2(4000); --linea a escribir
    Tabulador Constant Varchar2(6) := Chr(9); --separador de los campos
    Cont Number; --contador de lineas

    Cursor c_Export(v_Idioma Adm_Lenguajes.Idlenguaje%Type, v_Idinstitucion Fcs_Fact_Apunte.Idinstitucion%Type, v_Idfacturacion1 Fcs_Facturacionjg.Idfacturacion%Type, v_Idfacturacion2 Fcs_Facturacionjg.Idfacturacion%Type, v_Idpersona Fcs_Fact_Apunte.Idpersona%Type) Is
      Select To_Number(Nvl(Col.Ncomunitario, Col.Ncolegiado)) As Numcol,
             Per.Apellidos1 ||
             Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
             Per.Nombre As Letrado,
             Tur.Abreviatura As Turno,
             nvl(f_Get_Lista_Asi_Designa_3001(Des.Idinstitucion,
                                              Des.Idturno,
                                              Des.Anio,
                                              Des.Numero,
                                              'A-'),
                 'O-' || Lpad(Des.Codigo, 5, '0') || '/' ||
                 Substr(Des.Anio, 3, 2)) As Expediente,
             f_Get_Lista_Ejgs_Designa_3001(Des.Idinstitucion,
                                           Des.Idturno,
                                           Des.Anio,
                                           Des.Numero) As Ejg,
             Fad.Fechaactuacion,
             f_Get_Lista_Def_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          1,
                                          v_idioma) As Litnif,
             f_Get_Lista_Def_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          2,
                                          v_idioma) As Litnombre,
             f_Get_Lista_Def_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          3,
                                          v_idioma) As Litsexo,
             Nvl(f_Siga_Getrecurso(Pra.Descripcion, 2),
                 f_Siga_Getrecurso(Prd.Descripcion, 2)) As Pretension,
             Pro.Nombre As Modulo,
             Round(Fad.Precioaplicado * Fad.Porcentajefacturado / 100, 2) As Importe,
             To_Char(Fad.Porcentajefacturado) || '%' As Acreditacion,
             Fad.Precioaplicado As Importemodulo,
             f_Get_Lista_Asi_Designa_3001(Des.Idinstitucion,
                                          Des.Idturno,
                                          Des.Anio,
                                          Des.Numero,
                                          Null) As Numasistencia,
             Lpad(Des.Codigo, 5, '0') || '/' || Substr(Des.Anio, 3, 2) As Numdesigna,
             Fad.Numeroasunto As Actuacion,
             Des.Fechaentrada As Fechadesigna,
             Fad.Fechajustificacion,
             Des.Numprocedimiento As Procedimiento,
             Juz.Nombre As Juzgado
        From Fcs_Facturacionjg         Fac,
             Fcs_Fact_Actuaciondesigna Fad,
             Cen_Colegiado             Col,
             Cen_Persona               Per,
             Scs_Turno                 Tur,
             Scs_Actuaciondesigna      Act,
             Scs_Procedimientos        Pro,
             Scs_Pretension            Pra,
             Scs_Juzgado               Juz,
             Scs_Designa               Des,
             Scs_Pretension            Prd
       Where Fac.Idinstitucion = Fad.Idinstitucion
         And Fac.Idfacturacion = Fad.Idfacturacion
         And Fad.Idinstitucion = Col.Idinstitucion
         And Fad.Idpersona = Col.Idpersona
         And Fad.Idpersona = Per.Idpersona
         And Fad.Idinstitucion = Tur.Idinstitucion
         And Fad.Idturno = Tur.Idturno
         And Fad.Idinstitucion = Act.Idinstitucion
         And Fad.Idturno = Act.Idturno
         And Fad.Anio = Act.Anio
         And Fad.Numero = Act.Numero
         And Fad.Numeroasunto = Act.Numeroasunto
         And Act.Idinstitucion = Pro.Idinstitucion
         And Act.Idprocedimiento = Pro.Idprocedimiento
         And Act.Idinstitucion = Pra.Idinstitucion(+)
         And Act.Idpretension = Pra.Idpretension(+)
         And Act.Idinstitucion_Juzg = Juz.Idinstitucion(+)
         And Act.Idjuzgado = Juz.Idjuzgado(+)
         And Fad.Idinstitucion = Des.Idinstitucion
         And Fad.Idturno = Des.Idturno
         And Fad.Anio = Des.Anio
         And Fad.Numero = Des.Numero
         And Des.Idinstitucion = Prd.Idinstitucion(+)
         And Des.Idpretension = Prd.Idpretension(+)

         And Fac.Idinstitucion = v_Idinstitucion

         --Si no hay 2a, se escoge una unica facturacion:
         -- es necesario hacerlo asi para que el Excel de 1 facturacion
         -- no obtenga otras facturaciones de las mismas fechas
         -- pero de otros grupos de facturacion
         And ((v_Idfacturacion2 Is Null And
               Fac.Idfacturacion = v_Idfacturacion1) Or

             (v_Idfacturacion2 Is Not Null And
             Fac.Fechadesde Between
             (Select Fac1.Fechadesde
                From Fcs_Facturacionjg Fac1
               Where Fac1.Idinstitucion = v_Idinstitucion
                 And Fac1.Idfacturacion = v_Idfacturacion1) And
             (Select Fac2.Fechahasta
                From Fcs_Facturacionjg Fac2
               Where Fac2.Idinstitucion = v_Idinstitucion
                 And Fac2.Idfacturacion = v_Idfacturacion2)))

         And Per.Idpersona = nvl(v_Idpersona, Per.Idpersona)

       Order By Numcol;

  Begin

    --Recuperacion de cabeceras
    Select Replace(descripcion, '##', Tabulador) Into v_Registro
      From Gen_Recursos
     Where idrecurso = 'fcs.ficheroFacturacion.cabecera.turnos.3001'
       And idlenguaje = p_idioma;

    --Comprobacion de fichero de salida
    If Utl_File.Is_Open(f_Salida) Then
      Utl_File.Fclose(f_Salida);
    End If;

    Cont := 0;

    For v_Linea In c_Export(p_Idioma,
                            p_Idinstitucion,
                            p_Idfacturacion1,
                            p_Idfacturacion2,
                            p_Idpersona) Loop

      If Cont = 0 Then
        /* Antes de escribir el primer registro, escribimos la cabecera*/
        f_Salida := Utl_File.Fopen(p_Pathfichero, p_Fichero, 'w');
        Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      End If;
      Cont := Cont + 1;

      v_Registro := v_Linea.Numcol || Tabulador ||
                    v_Linea.Letrado || Tabulador ||
                    v_Linea.Turno || Tabulador ||
                    v_Linea.Expediente || Tabulador ||
                    v_Linea.Ejg || Tabulador ||
                    v_Linea.Fechaactuacion || Tabulador ||
                    v_Linea.Litnif || Tabulador ||
                    v_Linea.Litnombre || Tabulador ||
                    v_Linea.Litsexo || Tabulador ||
                    v_Linea.Juzgado || Tabulador ||
                    v_Linea.Pretension || Tabulador ||
                    v_Linea.Modulo || Tabulador ||
                    f_num2char(v_Linea.Importe) || Tabulador ||
                    v_Linea.Acreditacion || Tabulador ||
                    f_num2char(v_Linea.Importemodulo) || Tabulador ||
                    v_Linea.Numasistencia || Tabulador ||
                    v_Linea.Numdesigna || Tabulador ||
                    v_Linea.Actuacion || Tabulador ||
                    v_Linea.Fechadesigna || Tabulador ||
                    v_Linea.Fechajustificacion || Tabulador ||
                    v_Linea.Procedimiento;

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);

    End Loop;

    --cerrando fichero
    Utl_File.Fclose(f_Salida);
    p_Codretorno := '0';
    p_Datoserror := 'Fin correcto';

  Exception
    When Utl_File.Write_Error Then
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '5398';
      p_Datoserror := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    When Utl_File.Invalid_Filehandle Then
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '5396';
      p_Datoserror := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    When Utl_File.Invalid_Path Then
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      p_Codretorno := '5394';
      p_Datoserror := 'LA RUTA NO ES CORRECTA';
    When Utl_File.Invalid_Mode Then
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      p_Codretorno := '5395';
      p_Datoserror := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    When Utl_File.Invalid_Operation Then
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := Sqlcode || ' 5397';
      p_Datoserror := Sqlerrm || ' OPERACION ERRONEA EN EL FICHERO';

    When Utl_File.Internal_Error Then
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      /* Tratamiento de las matrices */
      p_Codretorno := '5401';
      p_Datoserror := 'ERROR INTERNO NO ESPECIFICADO';
    When Others Then

      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm;
  End PROC_EXPORTAR_TURNOS_3001;

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_EXPORTAR_GUARDIAS                                                                         */
  /* Descripcion: Exporta los datos facturados de guardias                                                        */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN       Identificador de la Institucion                                NUMBER         */
  /* P_IDFACTURACION       IN       Identificador de la Facturacion                                NUMBER         */
  /* P_IDPAGO              IN       Identificador del Pago                                         NUMBER,
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,
  /* P_PATHFICHERO         IN       ruta donde se encuentra el fichero                             VARCHAR2
  /* P_FICHERO             IN       nombre del  fichero                                            VARCHAR2
  /* P_CABECERA            IN       cabecera del fichero                                           VARCHAR2,
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 17/05/2006                                                                                   */
  /* Autor: David Sanchez Pina
  /* Fecha Modificacion   Autor Mdoificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /*
  /****************************************************************************************************************/
    PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(
        P_IDINSTITUCION  IN NUMBER,
        P_IDFACTURACION1 IN NUMBER,
        P_IDFACTURACION2 IN NUMBER,
        P_IDPERSONA      IN NUMBER,
        P_PATHFICHERO    IN VARCHAR2,
        P_FICHERO        IN VARCHAR2,
        P_IDIOMA         IN NUMBER,
        P_CODRETORNO     OUT VARCHAR2,
        P_DATOSERROR     OUT VARCHAR2) IS
                                       
        V_CONSEJO CEN_INSTITUCION.CEN_INST_IDINSTITUCION%TYPE;
    
    BEGIN
        -- Obtiene el consejo del colegio 
        V_CONSEJO := FUNC_CONSEJO_COLEGIO(P_IDINSTITUCION);

        IF (V_CONSEJO = C_CATALAN) THEN                                       
            PROC_EXPORTAR_GUARDIAS_3001(
                P_IDINSTITUCION,
                P_IDFACTURACION1,
                P_IDFACTURACION2,
                P_IDPERSONA,
                P_PATHFICHERO,
                P_FICHERO,
                P_IDIOMA,
                P_CODRETORNO,
                P_DATOSERROR);
        ELSE
            PROC_EXPORTAR_GUARDIAS_OTRO(
                P_IDINSTITUCION,
                P_IDFACTURACION1,
                P_IDPERSONA,
                P_PATHFICHERO,
                P_FICHERO,
                P_IDIOMA,
                P_CODRETORNO,
                P_DATOSERROR);
        END IF;
    END PROC_FCS_EXPORTAR_GUARDIAS;

  Procedure PROC_EXPORTAR_GUARDIAS_OTRO(p_Idinstitucion In Number,
                                        p_Idfacturacion In Number,
                                        p_Idpersona     In Number,
                                        p_Pathfichero   In Varchar2,
                                        p_Fichero       In Varchar2,
                                        p_Idioma        In Number,
                                        p_Codretorno    Out Varchar2,
                                        p_Datoserror    Out Varchar2) Is

    -- VARIABLES
    f_Salida                Utl_File.File_Type;
    v_Nombrepersonaanterior Varchar2(4000) := '';
    v_Nombreturnoanterior   Varchar2(4000) := '';
    v_Nombreguardiaanterior Varchar2(4000) := '';
    v_Registro              Varchar2(4000);
    v_Letradoactual         Fcs_Fact_Apunte.Idpersona%Type;
    v_Turnoactual           Fcs_Fact_Apunte.Idturno%Type;
    v_Guardiaactual         Fcs_Fact_Apunte.Idguardia%Type;
    v_Letradoanterior       Fcs_Fact_Apunte.Idpersona%Type;
    v_Turnoanterior         Fcs_Fact_Apunte.Idturno%Type;
    v_Guardiaanterior       Fcs_Fact_Apunte.Idguardia%Type;
    Totalpersona            Fcs_Fact_Apunte.Precioaplicado%Type;

    Tabulador Constant Varchar2(6) := Chr(9);
    Cont       Number;
    Fechaant   Date;
    Fechaact   Date;
    Tipoapunte Varchar2(100);

    Cursor c_Cg(p_Idioma Adm_Lenguajes.Idlenguaje%Type, p_Idinstitucion Fcs_Fact_Apunte.Idinstitucion%Type, p_Idfacturacion Fcs_Fact_Apunte.Idfacturacion%Type, p_Idpersona Fcs_Fact_Apunte.Idpersona%Type) Is
      Select Fcg.Idturno,
             Fcg.Idguardia,
             Fcg.Fechainicio,
             Fcg.Idinstitucion,

             --Datos de la Guardia
             Tt.Abreviatura Nomturno,
             Gt.Nombre      Nomguardia,
             Cg.Fechainicio Fechainiguardia,
             Cg.Fecha_Fin   Fechafinguardia,

             --Datos del Letrado
             p.Idpersona,
             (p.Apellidos1 || ' ' || p.Apellidos2 || ', ' || p.Nombre) Nompersona,
             Decode(Col.Comunitario, '1', Col.Ncomunitario, Col.Ncolegiado) Numcol,

             --Datos de Facturacion
             (Select Descripcion
                From Gen_Recursos
               Where Idrecurso =
                     Decode(Fcg.Idtipoapunte,
                            'FGTp',
                            'fcs.ficheroFacturacion.tipoApunte.cabeceraFG',
                            'FG',
                            'fcs.ficheroFacturacion.tipoApunte.cabeceraFG',
                            'fcs.ficheroFacturacion.tipoApunte.cabecera')
                 And Idlenguaje = p_Idioma) Tipoapunte,
             f_Siga_Getrecurso(h.Nombre, p_Idioma) Nombrehito,
             Fcg.Precioaplicado,
             Fcg.Preciocostesfijos,
             (Fcg.Precioaplicado + Fcg.Preciocostesfijos) Total,
             h.Descripcion Deschito,
             Fcg.Idtipoapunte,
             Fcg.Idapunte

      --Tablas usadas
        From Fcs_Fact_Apunte      Fcg,
             Scs_Cabeceraguardias Cg,
             Cen_Persona          p,
             Cen_Colegiado        Col,
             Scs_Hitofacturable   h,
             Scs_Guardiasturno    Gt,
             Scs_Turno            Tt
       Where Fcg.Idpersona = p.Idpersona
         And Fcg.Idpersona = Col.Idpersona
         And Fcg.Idinstitucion = Col.Idinstitucion
         And Fcg.Idinstitucion = Cg.Idinstitucion
         And Fcg.Idturno = Cg.Idturno
         And Fcg.Idguardia = Cg.Idguardia
         And Fcg.Idpersona = Cg.Idpersona
         And Fcg.Fechainicio = Cg.Fechainicio
         And Fcg.Idhito = h.Idhito
         And Fcg.Idinstitucion = Gt.Idinstitucion
         And Fcg.Idturno = Gt.Idturno
         And Fcg.Idguardia = Gt.Idguardia
         And Fcg.Idinstitucion = Tt.Idinstitucion
         And Fcg.Idturno = Tt.Idturno

            --Filtros
         And Fcg.Idfacturacion = p_Idfacturacion
         And Fcg.Idinstitucion = p_Idinstitucion
         And p.Idpersona = Nvl(p_Idpersona, p.Idpersona)

      --Ordenacion
       Order By Fcg.Idinstitucion,
                Fcg.Idturno,
                Fcg.Idguardia,
                Fcg.Idpersona,
                Fcg.Fechainicio,
                Fcg.Idapunte,
                Fcg.Idtipoapunte;

    Cursor c_Ug(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fug.Fechafin Fechaini_Ug,
             Fug.Fechafin Fechafin_Ug,
             (Select '  ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso = 'fcs.ficheroFacturacion.tipoApunte.dia'
                 And Idlenguaje = p_Idioma) Tipoapuntedia,
             (Select '    ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso = 'fcs.ficheroFacturacion.tipoApunte.tipo'
                 And Idlenguaje = p_Idioma) /**/ Tipoapuntetipo,
             h.Nombre Nombrehito,
             Fug.Precioaplicado,
             Fug.Preciocostesfijos,
             (Fug.Precioaplicado + Fug.Preciocostesfijos) Total,
             h.Descripcion Deschito,
             Fug.Idtipo,
             Fug.Idapunte
        From Fcs_Fact_Guardiascolegiado Fug, Scs_Hitofacturable h
       Where Fug.Idhito = h.Idhito
         And Fug.Idfacturacion = p_Idfacturacion
         And Fug.Idinstitucion = p_Idinstitucion
         And Fug.Idapunte = Pp_Idapunte
       Order By Fug.Fechafin, Fug.Idtipo; -- ordenado por fecha y tipo (contador)

    Cursor c_Ugfg(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fug.Fechafin Fechaini_Ug,
             Fug.Fechafin Fechafin_Ug,
             (Select '    ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso = 'fcs.ficheroFacturacion.tipoApunte.tipoFG'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito,
             Fug.Precioaplicado,
             Fug.Preciocostesfijos,
             (Fug.Precioaplicado + Fug.Preciocostesfijos) Total,
             h.Descripcion Deschito,
             Fug.Idtipo,
             Fug.Idapunte
        From Fcs_Fact_Guardiascolegiado Fug, Scs_Hitofacturable h
       Where Fug.Idhito = h.Idhito
         And Fug.Idfacturacion = p_Idfacturacion
         And Fug.Idinstitucion = p_Idinstitucion
         And Fug.Idapunte = Pp_Idapunte
       Order By Fug.Fechafin, Fug.Idtipo; -- ordenado por fecha y tipo (contador)

    Cursor c_Ac(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type, Pp_Idtipo Fcs_Fact_Guardiascolegiado.Idtipo%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacion'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
         And Fac.Idtipo = Pp_Idtipo
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;
    Cursor c_Acfg(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type, Pp_Idtipo Fcs_Fact_Guardiascolegiado.Idtipo%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacionFG'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
         And Fac.Idtipo = Pp_Idtipo
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;

    Cursor c_Ac_Sintipo(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacion'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;
    Cursor c_Acfg_Sintipo(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacionFG'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;

    Cursor c_Ac_Sintipo_Ug(Pp_Idapunte Fcs_Fact_Guardiascolegiado.Idapunte%Type, Pp_Fecha Fcs_Fact_Guardiascolegiado.Fechafin%Type) Is
      Select Fac.Idtipo,
             Fac.Idfacturacion,
             Fac.Idinstitucion,
             Fac.Idapunte,
             Fac.Anio || '/' || Lpad(Fac.Numero, 5, '0') Numasistencia,
             Ac.Idactuacion Numactuacion,
             Fac.Fechaactuacion,
             Fac.Fechajustificacion,
             (Pjg.Nombre || ' ' || Pjg.Apellido1 || ' ' || Pjg.Apellido2) Nombreperjg,
             Pjg.Nif Numdocperjg,
             Pkg_Siga_Sjcs.Fun_Obtenerdelitos(a.Idinstitucion,
                                              a.Anio,
                                              a.Numero,
                                              p_Idioma) Delitos,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   0) Lugar,
             Pkg_Siga_Sjcs.Fun_Obtenerlugarloc_Act(Ac.Idinstitucion,
                                                   Ac.Anio,
                                                   Ac.Numero,
                                                   Ac.Idactuacion,
                                                   1) Localidad,
             (Select '      ' || Descripcion --con indentacion
                From Gen_Recursos
               Where Idrecurso =
                     'fcs.ficheroFacturacion.tipoApunte.actuacion'
                 And Idlenguaje = p_Idioma) Tipoapunte,
             h.Nombre Nombrehito
        From Fcs_Fact_Actuacionasistencia Fac,
             Scs_Asistencia               a,
             Scs_Personajg                Pjg,
             Scs_Actuacionasistencia      Ac,
             Scs_Hitofacturable           h
       Where Fac.Idinstitucion = a.Idinstitucion
         And Fac.Anio = a.Anio
         And Fac.Numero = a.Numero
         And a.Idinstitucion = Pjg.Idinstitucion(+)
         And a.Idpersonajg = Pjg.Idpersona(+)
         And Fac.Idinstitucion = Ac.Idinstitucion
         And Fac.Anio = Ac.Anio
         And Fac.Numero = Ac.Numero
         And Fac.Idactuacion = Ac.Idactuacion
         And Fac.Idhito = h.Idhito
         And Fac.Idfacturacion = p_Idfacturacion
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Idapunte = Pp_Idapunte
         And Trunc(a.Fechahora) = Trunc(Pp_Fecha)
       Order By Fac.Fechaactuacion, Fac.Anio, Fac.Numero;

  Begin

    --Recuperacion de cabeceras
    Select Replace(Descripcion, '##', Tabulador)
      Into v_Registro
      From Gen_Recursos
     Where Idrecurso = 'fcs.ficheroFacturacion.cabecera.guardias'
       And Idlenguaje = p_Idioma;

    Begin
      --preparando variables para bucles de escritura de registros
      Totalpersona     := 0;

      v_Letradoanterior := -1;
      v_Turnoanterior   := -1;
      v_Guardiaanterior := -1;
      v_Letradoactual   := -1;
      v_Turnoactual     := -1;
      v_Guardiaactual   := -1;

      v_Nombrepersonaanterior := Null;
    End;

    --Comprobacion de fichero de salida
    If Utl_File.Is_Open(f_Salida) Then
      Utl_File.Fclose(f_Salida);
    End If;

    Cont := 0;

    For v_Cgpago In c_Cg(p_Idioma,
                         p_Idinstitucion,
                         p_Idfacturacion,
                         p_Idpersona) Loop

      If Cont = 0 Then
        /* Antes de escribir el primer registro, escribimos la cabecera*/
        f_Salida := Utl_File.Fopen(p_Pathfichero, p_Fichero, 'w');
        Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      End If;
      Cont := Cont + 1;

      -- CONTROL DE LETRADO
      v_Letradoanterior := v_Letradoactual;
      v_Turnoanterior   := v_Turnoactual;
      v_Guardiaanterior := v_Guardiaactual;
      v_Letradoactual   := v_Cgpago.Idpersona;
      v_Turnoactual     := v_Cgpago.Idturno;
      v_Guardiaactual   := v_Cgpago.Idguardia;

      If v_Letradoactual <> v_Letradoanterior Or
         v_Turnoactual <> v_Turnoanterior Or
         v_Guardiaactual <> v_Guardiaanterior Then

        -- SI CAMBIA LA PERSONA SE ACTUALIZA
        If v_Nombrepersonaanterior Is Null Then
          --INICIO DE LA PRIMERA PERSONA
          v_Nombrepersonaanterior := v_Cgpago.Nompersona;
          v_Nombreturnoanterior   := v_Cgpago.Nomturno;
          v_Nombreguardiaanterior := v_Cgpago.Nomguardia;
        Else
          -- FIN DE UNA PERSONA
          -- ESCRIBO EL TOTAL EN UNA LINEA PARA EL USUARIO ANTERIOR
          v_Registro := 'TOTAL ' || ' ' || Tabulador || -- Num Col Letrado
                        v_Nombrepersonaanterior || Tabulador || -- nombre persona
                        v_Nombreturnoanterior || Tabulador || -- turno
                        v_Nombreguardiaanterior || Tabulador || -- guardia
                        ' ' || Tabulador || -- tipo apunte
                        ' ' || Tabulador || -- fecha inicio
                        ' ' || Tabulador || -- fecha fin
                        ' ' || Tabulador || -- Fecha Actuacion
                        ' ' || Tabulador || -- fechajustificacion
                        ' ' || Tabulador || -- numero asistencia
                        ' ' || Tabulador || -- numero actuacion
                        ' ' || Tabulador || -- motivo
                        ' ' || Tabulador || -- Num Actuaciones Facturadas
                        ' ' || Tabulador || -- Num Actuac FG Facturadas
                        ' ' || Tabulador || -- Num Asistencias Facturadas
                        ' ' || Tabulador || -- Num Actuaciones Totales
                        ' ' || Tabulador || -- Num Actuac FG Totales
                        ' ' || Tabulador || -- Num Asistencias Totales
                        ' ' || Tabulador || -- importe
                        ' ' || Tabulador || -- precio costes
                        ' ' || Tabulador || -- total

                        ' ' || Tabulador || -- Separador
                        ' ' || Tabulador || -- Num Doc Asistido
                        ' ' || Tabulador || -- asistido
                        ' ' || Tabulador || -- delitos
                        ' ' || Tabulador || -- lugar
                        ' ' || Tabulador || -- localidad
                        f_Num2char(Totalpersona); -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          --INICIO DE PERSONA
          v_Nombrepersonaanterior := v_Cgpago.Nompersona;
          v_Nombreturnoanterior   := v_Cgpago.Nomturno;
          v_Nombreguardiaanterior := v_Cgpago.Nomguardia;
        End If;

        -- INICIALIZO
        Totalpersona     := 0;

      End If;

      Totalpersona     := Totalpersona + v_Cgpago.Total;

      -- ESCRIBO CABECERA DE GUARDIA

      v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                    || Tabulador || v_Cgpago.Nompersona -- nombre persona
                    || Tabulador || Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                    || Tabulador ||
                    Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                    || Tabulador || v_Cgpago.Tipoapunte --tipo apunte
                    || Tabulador || v_Cgpago.Fechainiguardia -- fecha inicio
                    || Tabulador || v_Cgpago.Fechafinguardia -- fecha fin
                    || Tabulador || ' ' -- Fecha Actuacion
                    || Tabulador || ' ' -- Fecha Justificacion
                    || Tabulador || ' ' -- numero asistencia
                    || Tabulador || ' ' -- numero actuacion
                    || Tabulador ||
                    Replace(v_Cgpago.Nombrehito, Chr(9), ' '); -- motivo

      If v_Cgpago.Idtipoapunte = 'FGTp' Or v_Cgpago.Idtipoapunte = 'FG' Then
        v_Registro := v_Registro || Tabulador || '' --V_CGPAGO.N_ACTUACIONESFACTURADAS -- Num Actuaciones Facturadas
                      || Tabulador ||
                      Func_Num_Actuac_Facturac(p_Idinstitucion,
                                               p_Idfacturacion,
                                               Null,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               v_Cgpago.Fechainiguardia,
                                               v_Cgpago.Fechafinguardia,
                                               'S') -- Num Actuac FG Facturadas
                      || Tabulador || '' --V_CGPAGO.N_ASISTENCIASFACTURADAS -- Num Asistencias Facturadas
                      || Tabulador || '' --V_CGPAGO.N_ACTUACIONESTOTAL -- Num Actuaciones Totales
                      || Tabulador ||
                      Func_Num_Actuac_Totales(p_Idinstitucion,
                                              p_Idfacturacion,
                                              Null,
                                              v_Cgpago.Idturno,
                                              v_Cgpago.Idguardia,
                                              v_Cgpago.Idpersona,
                                              v_Cgpago.Fechainiguardia,
                                              v_Cgpago.Fechafinguardia,
                                              'S') -- Num Actuac FG Totales
                      || Tabulador || ''; --V_CGPAGO.N_ASISTENCIASTOTAL -- Num Asistencias Totales
      Else
        v_Registro := v_Registro || Tabulador ||
                      Func_Num_Actuac_Facturac(p_Idinstitucion,
                                               p_Idfacturacion,
                                               Null,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               v_Cgpago.Fechainiguardia,
                                               v_Cgpago.Fechafinguardia,
                                               'N') -- Num Actuaciones Facturadas
                      || Tabulador || '' --V_CGPAGO.N_ACTUACFGFACTURADAS -- Num Actuac FG Facturadas
                      || Tabulador ||
                      Func_Num_Asist_Facturac(p_Idinstitucion,
                                              p_Idfacturacion,
                                              v_Cgpago.Idturno,
                                              v_Cgpago.Idguardia,
                                              v_Cgpago.Idpersona,
                                              v_Cgpago.Fechainiguardia,
                                              v_Cgpago.Fechafinguardia) -- Num Asistencias Facturadas
                      || Tabulador ||
                      Func_Num_Actuac_Totales(p_Idinstitucion,
                                              p_Idfacturacion,
                                              Null,
                                              v_Cgpago.Idturno,
                                              v_Cgpago.Idguardia,
                                              v_Cgpago.Idpersona,
                                              v_Cgpago.Fechainiguardia,
                                              v_Cgpago.Fechafinguardia,
                                              'N') -- Num Actuaciones Totales
                      || Tabulador || '' --V_CGPAGO.N_ACTUACFGTOTAL -- Num Actuac FG Totales
                      || Tabulador ||
                      Func_Num_Asist_Totales(p_Idinstitucion,
                                             p_Idfacturacion,
                                             v_Cgpago.Idturno,
                                             v_Cgpago.Idguardia,
                                             v_Cgpago.Idpersona,
                                             v_Cgpago.Fechainiguardia,
                                             v_Cgpago.Fechafinguardia); -- Num Asistencias Totales
      End If;

      v_Registro := v_Registro || Tabulador ||
                    f_Num2char(v_Cgpago.Precioaplicado) -- importe
                    || Tabulador || f_Num2char(v_Cgpago.Preciocostesfijos) -- precio costes
                    || Tabulador || f_Num2char(v_Cgpago.Total) -- total

                    || Tabulador || ' ' -- Separador
                    || Tabulador || ' ' -- Num Doc Asistido
                    || Tabulador || ' ' -- asistido
                    || Tabulador || ' ' -- delitos
                    || Tabulador || ' ' -- lugar
                    || Tabulador || ' ' -- localidad
                    || Tabulador || ' '; -- total letrado

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);

      If v_Cgpago.Idtipoapunte = 'CG' Then
        -- RECORRIDO CG-ACTUACIONES

        For v_Ug In c_Ug(v_Cgpago.Idapunte) Loop

          -- RGG 17/06/2008 ES UN REGISTRO DE UNIDAD DE GUARDIA GENERAL

          -- ESCRIBO UNIDAD DE GUARDIA
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ug.Tipoapuntedia --tipo apunte
                        || Tabulador || v_Ug.Fechaini_Ug -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || ' ' -- Fecha Actuacion
                        || Tabulador || ' ' -- fechajustificacion
                        || Tabulador || ' ' -- numero asistencia
                        || Tabulador || ' ' -- numero actuacion
                        || Tabulador ||
                        Replace(v_Ug.Nombrehito, Chr(9), ' ') -- motivo
                        || Tabulador ||
                        Func_Num_Actuac_Facturac(p_Idinstitucion,
                                                 p_Idfacturacion,
                                                 Null,
                                                 v_Cgpago.Idturno,
                                                 v_Cgpago.Idguardia,
                                                 v_Cgpago.Idpersona,
                                                 Null,
                                                 v_Ug.Fechafin_Ug,
                                                 'N') -- Num Actuaciones Facturadas
                        || Tabulador || '' -- Num Actuac FG Facturadas
                        || Tabulador ||
                        Func_Num_Asist_Facturac(p_Idinstitucion,
                                                p_Idfacturacion,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug) -- Num Asistencias Facturadas
                        || Tabulador ||
                        Func_Num_Actuac_Totales(p_Idinstitucion,
                                                p_Idfacturacion,
                                                Null,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug,
                                                'N') -- Num Actuaciones Totales
                        || Tabulador || '' --V_UG.N_ACTUACFGTOTAL -- Num Actuac FG Totales
                        || Tabulador ||
                        Func_Num_Asist_Totales(p_Idinstitucion,
                                               p_Idfacturacion,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               Null,
                                               v_Ug.Fechafin_Ug) -- Num Asistencias Totales
                        || Tabulador || f_Num2char(v_Ug.Precioaplicado) -- importe
                        || Tabulador || f_Num2char(v_Ug.Preciocostesfijos) -- precio costes
                        || Tabulador || f_Num2char(v_Ug.Total) -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador || ' ' -- Num Doc Asistido
                        || Tabulador || ' ' -- asistido
                        || Tabulador || ' ' -- delitos
                        || Tabulador || ' ' -- lugar
                        || Tabulador || ' ' -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          For v_Ac In c_Ac_Sintipo_Ug(v_Cgpago.Idapunte, v_Ug.Fechafin_Ug) Loop

            -- ESCRIBO ACTUACION
            v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                          || Tabulador || v_Cgpago.Nompersona -- nombre persona
                          || Tabulador ||
                          Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                          || Tabulador ||
                          Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                          || Tabulador || v_Ac.Tipoapunte --tipo apunte
                          || Tabulador || ' ' -- fecha inicio
                          || Tabulador || ' ' -- fecha fin
                          || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                          || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                          || Tabulador || v_Ac.Numasistencia -- numero asistencia
                          || Tabulador || v_Ac.Numactuacion -- numero actuacion
                          || Tabulador || v_Ac.Nombrehito -- motivo
                          || Tabulador || ' ' -- Num Actuaciones Facturadas
                          || Tabulador || ' ' -- Num Actuac FG Facturadas
                          || Tabulador || ' ' -- Num Asistencias Facturadas
                          || Tabulador || ' ' -- Num Actuaciones Totales
                          || Tabulador || ' ' -- Num Actuac FG Totales
                          || Tabulador || ' ' -- Num Asistencias Totales
                          || Tabulador || ' ' -- importe
                          || Tabulador || ' ' -- precio costes
                          || Tabulador || ' ' -- total

                          || Tabulador || ' ' -- Separador
                          || Tabulador ||
                          Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                          || Tabulador ||
                          Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                          || Tabulador ||
                          Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                          || Tabulador || v_Ac.Lugar -- lugar
                          || Tabulador || v_Ac.Localidad -- localidad
                          || Tabulador || ' '; -- total letrado

            Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);

          End Loop;

        End Loop;

      Elsif v_Cgpago.Idtipoapunte = 'CG-' Then
        -- RECORRIDO CG-ACTUACIONES

        For v_Ac In c_Ac_Sintipo(v_Cgpago.Idapunte) Loop

          -- ESCRIBO ACTUACION
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ac.Tipoapunte --tipo apunte
                        || Tabulador || ' ' -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                        || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                        || Tabulador || v_Ac.Numasistencia -- numero asistencia
                        || Tabulador || v_Ac.Numactuacion -- numero actuacion
                        || Tabulador || v_Ac.Nombrehito -- motivo
                        || Tabulador || ' ' -- Num Actuaciones Facturadas
                        || Tabulador || ' ' -- Num Actuac FG Facturadas
                        || Tabulador || ' ' -- Num Asistencias Facturadas
                        || Tabulador || ' ' -- Num Actuaciones Totales
                        || Tabulador || ' ' -- Num Actuac FG Totales
                        || Tabulador || ' ' -- Num Asistencias Totales
                        || Tabulador || ' ' -- importe
                        || Tabulador || ' ' -- precio costes
                        || Tabulador || ' ' -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador ||
                        Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                        || Tabulador ||
                        Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                        || Tabulador || Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                        || Tabulador || v_Ac.Lugar -- lugar
                        || Tabulador || v_Ac.Localidad -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

        End Loop;

      Elsif v_Cgpago.Idtipoapunte = 'CG+' Then
        -- RECORRIDO CG-UG-ACTUACIONES (CUANDO CAMBIA LA UG) (QUIZA VALE CUANDO CAMBIA EL CONTADOR TAMBIEN)
        Fechaant := To_Date('01/01/1990', 'DD/MM/YYYY');
        Fechaact := Null;

        For v_Ug In c_Ug(v_Cgpago.Idapunte) Loop

          Begin
            --controlando para escribir registros de Tipo
            Fechaact := v_Ug.Fechafin_Ug;

            -- RGG 17/06/2008
            If (Fechaact <> Fechaant) Then
              Tipoapunte := v_Ug.Tipoapuntedia;
            Else
              Tipoapunte := v_Ug.Tipoapuntetipo;
            End If;

            Fechaant := Fechaact;
          End;

          -- ESCRIBO UNIDAD DE GUARDIA
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || Tipoapunte --tipo apunte
                        || Tabulador || v_Ug.Fechaini_Ug -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || ' ' -- Fecha Actuacion
                        || Tabulador || ' ' -- fechajustificacion
                        || Tabulador || ' ' -- numero asistencia
                        || Tabulador || ' ' -- numero actuacion
                        || Tabulador ||
                        Replace(v_Ug.Nombrehito, Chr(9), ' ') -- motivo
                        || Tabulador ||
                        Func_Num_Actuac_Facturac(p_Idinstitucion,
                                                 p_Idfacturacion,
                                                 Null,
                                                 v_Cgpago.Idturno,
                                                 v_Cgpago.Idguardia,
                                                 v_Cgpago.Idpersona,
                                                 Null,
                                                 v_Ug.Fechafin_Ug,
                                                 'N') -- Num Actuaciones Facturadas
                        || Tabulador || '' --V_UG.N_ACTUACFGFACTURADAS -- Num Actuac FG Facturadas
                        || Tabulador ||
                        Func_Num_Asist_Facturac(p_Idinstitucion,
                                                p_Idfacturacion,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug) --- Num Asistencias Facturadas
                        || Tabulador ||
                        Func_Num_Actuac_Totales(p_Idinstitucion,
                                                p_Idfacturacion,
                                                Null,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug,
                                                'N') -- Num Actuaciones Totales
                        || Tabulador || '' --V_UG.N_ACTUACFGTOTAL -- Num Actuac FG Totales
                        || Tabulador ||
                        Func_Num_Asist_Totales(p_Idinstitucion,
                                               p_Idfacturacion,
                                               v_Cgpago.Idturno,
                                               v_Cgpago.Idguardia,
                                               v_Cgpago.Idpersona,
                                               Null,
                                               v_Ug.Fechafin_Ug) -- Num Asistencias Totales
                        || Tabulador || f_Num2char(v_Ug.Precioaplicado) -- importe
                        || Tabulador || f_Num2char(v_Ug.Preciocostesfijos) -- precio costes
                        || Tabulador || f_Num2char(v_Ug.Total) -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador || ' ' -- Num Doc Asistido
                        || Tabulador || ' ' -- asistido
                        || Tabulador || ' ' -- delitos
                        || Tabulador || ' ' -- lugar
                        || Tabulador || ' ' -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          For v_Ac In c_Ac(v_Cgpago.Idapunte, v_Ug.Idtipo) Loop

            -- ESCRIBO ACTUACION
            v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                          || Tabulador || v_Cgpago.Nompersona -- nombre persona
                          || Tabulador ||
                          Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                          || Tabulador ||
                          Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                          || Tabulador || v_Ac.Tipoapunte --tipo apunte
                          || Tabulador || ' ' -- fecha inicio
                          || Tabulador || ' ' -- fecha fin
                          || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                          || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                          || Tabulador || v_Ac.Numasistencia -- numero asistencia
                          || Tabulador || v_Ac.Numactuacion -- numero actuacion
                          || Tabulador || v_Ac.Nombrehito -- motivo
                          || Tabulador || ' ' -- Num Actuaciones Facturadas
                          || Tabulador || ' ' -- Num Actuac FG Facturadas
                          || Tabulador || ' ' -- Num Asistencias Facturadas
                          || Tabulador || ' ' -- Num Actuaciones Totales
                          || Tabulador || ' ' -- Num Actuac FG Totales
                          || Tabulador || ' ' -- Num Asistencias Totales
                          || Tabulador || ' ' -- importe
                          || Tabulador || ' ' -- precio costes
                          || Tabulador || ' ' -- total

                          || Tabulador || ' ' -- Separador
                          || Tabulador ||
                          Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                          || Tabulador ||
                          Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                          || Tabulador ||
                          Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                          || Tabulador || v_Ac.Lugar -- lugar
                          || Tabulador || v_Ac.Localidad -- localidad
                          || Tabulador || ' '; -- total letrado

            Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);

          End Loop;

        End Loop;

      Elsif v_Cgpago.Idtipoapunte = 'FG' Then
        For v_Ac In c_Acfg_Sintipo(v_Cgpago.Idapunte) Loop

          -- ESCRIBO ACTUACION
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ac.Tipoapunte --tipo apunte
                        || Tabulador || ' ' -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                        || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                        || Tabulador || v_Ac.Numasistencia -- numero asistencia
                        || Tabulador || v_Ac.Numactuacion -- numero actuacion
                        || Tabulador || v_Ac.Nombrehito -- motivo
                        || Tabulador || ' ' -- Num Actuaciones Facturadas
                        || Tabulador || ' ' -- Num Actuac FG Facturadas
                        || Tabulador || ' ' -- Num Asistencias Facturadas
                        || Tabulador || ' ' -- Num Actuaciones Totales
                        || Tabulador || ' ' -- Num Actuac FG Totales
                        || Tabulador || ' ' -- Num Asistencias Totales
                        || Tabulador || ' ' -- importe
                        || Tabulador || ' ' -- precio costes
                        || Tabulador || ' ' -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador ||
                        Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                        || Tabulador ||
                        Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                        || Tabulador || Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                        || Tabulador || v_Ac.Lugar -- lugar
                        || Tabulador || v_Ac.Localidad -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

        End Loop;

        --                end loop; -- UG

      Elsif v_Cgpago.Idtipoapunte = 'FGTp' Then
        -- RECORRIDO CG-UG-ACTUACIONES (CUANDO CAMBIA LA UG) (QUIZA VALE CUANDO CAMBIA EL CONTADOR TAMBIEN)

        For v_Ug In c_Ugfg(v_Cgpago.Idapunte) Loop

          -- RGG 17/06/2008 ES UN REGISTRO DE UNIDAD DE GUARDIA POR TIPO

          -- ESCRIBO UNIDAD DE GUARDIA
          v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                        || Tabulador || v_Cgpago.Nompersona -- nombre persona
                        || Tabulador ||
                        Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                        || Tabulador ||
                        Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                        || Tabulador || v_Ug.Tipoapunte --tipo apunte
                        || Tabulador || v_Ug.Fechaini_Ug -- fecha inicio
                        || Tabulador || ' ' -- fecha fin
                        || Tabulador || ' ' -- Fecha Actuacion
                        || Tabulador || ' ' -- fechajustificacion
                        || Tabulador || ' ' -- numero asistencia
                        || Tabulador || ' ' -- numero actuacion
                        || Tabulador ||
                        Replace(v_Ug.Nombrehito, Chr(9), ' ') -- motivo
                        || Tabulador || '' --V_UG.N_ACTUACIONESFACTURADAS -- Num Actuaciones Facturadas
                        || Tabulador ||
                        Func_Num_Actuac_Facturac(p_Idinstitucion,
                                                 p_Idfacturacion,
                                                 Null,
                                                 v_Cgpago.Idturno,
                                                 v_Cgpago.Idguardia,
                                                 v_Cgpago.Idpersona,
                                                 Null,
                                                 v_Ug.Fechafin_Ug,
                                                 'S') -- Num Actuac FG Facturadas
                        || Tabulador || '' --V_UG.N_ASISTENCIASFACTURADAS -- Num Asistencias Facturadas
                        || Tabulador || '' --V_UG.N_ACTUACIONESTOTAL -- Num Actuaciones Totales
                        || Tabulador ||
                        Func_Num_Actuac_Totales(p_Idinstitucion,
                                                p_Idfacturacion,
                                                Null,
                                                v_Cgpago.Idturno,
                                                v_Cgpago.Idguardia,
                                                v_Cgpago.Idpersona,
                                                Null,
                                                v_Ug.Fechafin_Ug,
                                                'S') -- Num Actuac FG Totales
                        || Tabulador || '' --V_UG.N_ASISTENCIASTOTAL -- Num Asistencias Totales
                        || Tabulador || f_Num2char(v_Ug.Precioaplicado) -- importe
                        || Tabulador || f_Num2char(v_Ug.Preciocostesfijos) -- precio costes
                        || Tabulador || f_Num2char(v_Ug.Total) -- total

                        || Tabulador || ' ' -- Separador
                        || Tabulador || ' ' -- Num Doc Asistido
                        || Tabulador || ' ' -- asistido
                        || Tabulador || ' ' -- delitos
                        || Tabulador || ' ' -- lugar
                        || Tabulador || ' ' -- localidad
                        || Tabulador || ' '; -- total letrado

          Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
          Utl_File.Fflush(f_Salida);

          For v_Ac In c_Acfg(v_Cgpago.Idapunte, v_Ug.Idtipo) Loop

            -- ESCRIBO ACTUACION
            v_Registro := v_Cgpago.Numcol -- Num Col Letrado
                          || Tabulador || v_Cgpago.Nompersona -- nombre persona
                          || Tabulador ||
                          Replace(v_Cgpago.Nomturno, Chr(9), ' ') -- turno
                          || Tabulador ||
                          Replace(v_Cgpago.Nomguardia, Chr(9), ' ') -- guardia
                          || Tabulador || v_Ac.Tipoapunte --tipo apunte
                          || Tabulador || ' ' -- fecha inicio
                          || Tabulador || ' ' -- fecha fin
                          || Tabulador || v_Ac.Fechaactuacion -- Fecha Actuacion
                          || Tabulador || v_Ac.Fechajustificacion -- fechajustificacion
                          || Tabulador || v_Ac.Numasistencia -- numero asistencia
                          || Tabulador || v_Ac.Numactuacion -- numero actuacion
                          || Tabulador || v_Ac.Nombrehito -- motivo
                          || Tabulador || ' ' -- Num Actuaciones Facturadas
                          || Tabulador || ' ' -- Num Actuac FG Facturadas
                          || Tabulador || ' ' -- Num Asistencias Facturadas
                          || Tabulador || ' ' -- Num Actuaciones Totales
                          || Tabulador || ' ' -- Num Actuac FG Totales
                          || Tabulador || ' ' -- Num Asistencias Totales
                          || Tabulador || ' ' -- importe
                          || Tabulador || ' ' -- precio costes
                          || Tabulador || ' ' -- total

                          || Tabulador || ' ' -- Separador
                          || Tabulador ||
                          Replace(v_Ac.Numdocperjg, Chr(9), ' ') -- Num Doc Asistido
                          || Tabulador ||
                          Replace(v_Ac.Nombreperjg, Chr(9), ' ') -- asistido
                          || Tabulador ||
                          Replace(v_Ac.Delitos, Chr(9), ' ') -- delitos
                          || Tabulador || v_Ac.Lugar -- lugar
                          || Tabulador || v_Ac.Localidad -- localidad
                          || Tabulador || ' '; -- total letrado

            Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
            Utl_File.Fflush(f_Salida);

          End Loop;

        End Loop; -- UG

      End If;

    End Loop;

    -- ULTIMA PERSONA
    If v_Nombrepersonaanterior Is Not Null Then
      -- FIN DE LA ULTIMA PERSONA
      -- ESCRIBO EL TOTAL EN UNA LINEA PARA EL USUARIO ANTERIOR

      v_Registro := 'TOTAL ' || ' ' -- Num Col Letrado
                    || Tabulador || v_Nombrepersonaanterior -- nombre persona
                    || Tabulador || v_Nombreturnoanterior -- turno
                    || Tabulador || v_Nombreguardiaanterior -- guardia
                    || Tabulador || ' ' --tipo apunte
                    || Tabulador || ' ' -- fecha inicio
                    || Tabulador || ' ' -- fecha fin
                    || Tabulador || ' ' -- Fecha Actuacion
                    || Tabulador || ' ' -- fechajustificacion
                    || Tabulador || ' ' -- numero asistencia
                    || Tabulador || ' ' -- numero actuacion
                    || Tabulador || ' ' -- motivo
                    || Tabulador || ' ' -- Num Actuaciones Facturadas
                    || Tabulador || ' ' -- Num Actuac FG Facturadas
                    || Tabulador || ' ' -- Num Asistencias Facturadas
                    || Tabulador || ' ' -- Num Actuaciones Totales
                    || Tabulador || ' ' -- Num Actuac FG Totales
                    || Tabulador || ' ' -- Num Asistencias Totales
                    || Tabulador || ' ' -- importe
                    || Tabulador || ' ' -- precio costes
                    || Tabulador || ' ' -- total

                    || Tabulador || ' ' -- Separador
                    || Tabulador || ' ' -- Num Doc Asistido
                    || Tabulador || ' ' -- asistido
                    || Tabulador || ' ' -- delitos
                    || Tabulador || ' ' -- lugar
                    || Tabulador || ' ' -- localidad
                    || Tabulador || f_Num2char(Totalpersona); -- total letrado

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);
    End If;

    Begin
      --cerrando fichero
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '0';
      p_Datoserror := 'Fin correcto';
    End; --cerrando fichero

  Exception
    When Utl_File.Write_Error Then
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '5398';
      p_Datoserror := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    When Utl_File.Invalid_Filehandle Then
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '5396';
      p_Datoserror := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    When Utl_File.Invalid_Path Then
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      p_Codretorno := '5394';
      p_Datoserror := 'LA RUTA NO ES CORRECTA';
    When Utl_File.Invalid_Mode Then
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      p_Codretorno := '5395';
      p_Datoserror := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    When Utl_File.Invalid_Operation Then
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := Sqlcode || ' 5397';
      p_Datoserror := Sqlerrm || ' OPERACION ERRONEA EN EL FICHERO';

    When Utl_File.Internal_Error Then
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      /* Tratamiento de las matrices */
      p_Codretorno := '5401';
      p_Datoserror := 'ERROR INTERNO NO ESPECIFICADO';
    When Others Then

      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm;
  End PROC_EXPORTAR_GUARDIAS_OTRO;

  Procedure PROC_EXPORTAR_GUARDIAS_3001(p_Idinstitucion  In Number,
                                        p_Idfacturacion1 In Number,
                                        p_Idfacturacion2 In Number,
                                        p_Idpersona      In Number,
                                        p_Pathfichero    In Varchar2,
                                        p_Fichero        In Varchar2,
                                        p_Idioma         In Number,
                                        p_Codretorno     Out Varchar2,
                                        p_Datoserror     Out Varchar2) Is

    -- VARIABLES
    f_Salida   Utl_File.File_Type; --fichero para escribir
    v_Registro Varchar2(4000); --linea a escribir
    Tabulador Constant Varchar2(6) := Chr(9); --separador de los campos
    Cont Number; --contador de lineas

    Cursor c_Export(v_Idioma Adm_Lenguajes.Idlenguaje%Type, v_Idinstitucion Fcs_Fact_Apunte.Idinstitucion%Type, v_Idfacturacion1 Fcs_Facturacionjg.Idfacturacion%Type, v_Idfacturacion2 Fcs_Facturacionjg.Idfacturacion%Type, v_Idpersona Fcs_Fact_Apunte.Idpersona%Type) Is
      Select Numcol As Numcol,
             Idpersona As Idpersona,
             Initcap(Letrado) As Letrado,
             Idturno As Idturno,
             Decode(Turno, 'COMPL', Guardia, Turno) As Turno,
             Idguardia As Idguardia,
             Guardia As Guardia,
             Tipoapunte As Tipoapunte,
             To_Char(Fechainicio, 'dd/mm/yyyy') As Fechainicio,
             To_Char(Fechafin, 'dd/mm/yyyy') As Fechafin,
             To_Char(Fechaactuacion, 'dd/mm/yyyy') As Fechaactuacion,
             To_Char(Fechajustificacion, 'dd/mm/yyyy') As Fechajustificacion,
             Asistencia As Asistencia,
             Ejg As Ejg,
             Nif As Asistido_Nif,
             Initcap(Asistido) As Asistido_Nombre,
             Sexo As Asistido_Sexo,
             Delitos As Delitos,
             Lugar As Lugar,
             Sum(Actfacgua) As Actfacgua,
             Sum(Actfacfue) As Actfacfue,
             Acttotgua As Acttotgua,
             Acttotfue As Acttotfue,
             Decode(Max(Modulo),
                    'P7', '32',
                    'P16', 'P16',
                    'P29', '32a',
                    'P44', Case
                              When Sum(Actfacgua) < 6 Then
                               '31'
                              Else
                               '32'
                            End,
                    'P49', Case
                              When Count(*) > 1 Then
                               '32'
                              Else
                               '31a'
                            End,
                    'V4', '40c',
                    'V16', Case
                              When Sum(Actfacgua) < 4 Then
                               '40ba'
                              Else
                               '40c'
                            End,
                    'V44', Case
                              When Sum(Actfacgua) < 4 Then
                               '40b'
                              Else
                               '40c'
                            End,
                    'V49', Case
                              When Sum(Actfacgua) < 4 Then
                               '40ba'
                              Else
                               '40c'
                            End,
                    Max(Modulo)) As Modulo,
             Sum(Importe) As Importe
        From (

              --A. Guardias: se componen de 2 grupos de datos:
              Select Dat.Idinstitucion,
                      Dat.Idfacturacion,
                      Dat.Fechadesde,
                      To_Number(Nvl(Col.Ncolegiado, Col.Ncomunitario)) As Numcol,
                      Dat.Idpersona,
                      Per.Apellidos1 ||
                      Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
                      Per.Nombre As Letrado,
                      Dat.Idturno As Idturno,
                      Tur.Abreviatura As Turno,
                      Dat.Idguardia As Idguardia,
                      Gua.Nombre As Guardia,
                      1 As Ordapunte,
                      Dat.Tipoapunte,
                      Dat.Fechainicio,
                      Dat.Fechafin,
                      Fechaactuacion,
                      Null As Fechajustificacion,
                      Null As Asistencia,
                      0 As Idactuacion, --Se necesita para agrupar al final
                      Null As Ejg,
                      Null As Nif,
                      Null As Asistido,
                      Null As Sexo,
                      Null As Delitos,
                      Null As Lugar,
                      Sum(Dat.Actfacgua) As Actfacgua,
                      Sum(Dat.Actfacfue) As Actfacfue,
                      0 As Acttotgua,
                      0 As Acttotfue,
                      Decode(Max(Dat.Modulo),
                             Null,
                             Null,
                             Decode(Gua.idTipoGuardia,
                                    1,
                                    'V' || Max(Dat.Modulo),
                                    'P' || Max(Dat.Modulo))) As Modulo,
                      Sum(Dat.Importe) As Importe

              --A.1. Datos de cada apunte
                From (Select Fac.Idinstitucion,
                              Fac.Idfacturacion,
                              Min(Fac.Fechadesde) As Fechadesde,
                              Apu.Idapunte,
                              Apu.Idpersona,
                              Apu.Idturno,
                              Apu.Idguardia,
                              1 As Ordapunte,
                              'G. ' || f_siga_getrecurso_etiqueta('fcs.ficheroFacturacion.tipoApunte.' ||
                              Decode(Cgu.Fecha_Fin - Cgu.Fechainicio + 1,
                                     1, 'dia',
                                     7, 'semana',
                                     13, 'quincena',
                                     14, 'quincena',
                                     15, 'quincena',
                                     16, 'quincena',
                                     28, 'mes',
                                     29, 'mes',
                                     30, 'mes',
                                     31, 'mes',
                                     Cgu.Fecha_Fin - Cgu.Fechainicio + 1 || 'dias'),
                              v_Idioma) As Tipoapunte,
                              Cgu.Fechainicio,
                              Cgu.Fecha_Fin As Fechafin,
                              Cgu.Fechainicio As Fechaactuacion,
                              Sum(Decode(Acf.Idhito, Null, 0, 9, 0, 1)) As Actfacgua,
                              Sum(Decode(Acf.Idhito, Null, 0, 9, 1, 0)) As Actfacfue,
                              Decode(Apu.Precioaplicado,
                                     0,
                                     Null,
                                     Decode(Apu.Precioaplicado -
                                            Sum(Nvl(Acf.Precioaplicado, 0)),
                                            0,
                                            Null,
                                            Apu.Idhito)) As Modulo,
                              Case
                                When Apu.Precioaplicado -
                                     Sum(Nvl(Acf.Precioaplicado, 0)) < 0 Then
                                 Apu.Precioaplicado
                                Else
                                 Apu.Precioaplicado - Sum(Nvl(Acf.Precioaplicado, 0))
                              End As Importe
                         From Fcs_Facturacionjg            Fac,
                              Fcs_Fact_Apunte              Apu,
                              Scs_Cabeceraguardias         Cgu,
                              Fcs_Fact_Actuacionasistencia Acf
                        Where Fac.Idinstitucion = Apu.Idinstitucion
                          And Fac.Idfacturacion = Apu.Idfacturacion
                          And Apu.Idinstitucion = Cgu.Idinstitucion
                          And Apu.Idturno = Cgu.Idturno
                          And Apu.Idguardia = Cgu.Idguardia
                          And Apu.Idpersona = Cgu.Idpersona
                          And Apu.Fechainicio = Cgu.Fechainicio
                          And Apu.Idinstitucion = Acf.Idinstitucion(+)
                          And Apu.Idfacturacion = Acf.Idfacturacion(+)
                          And Apu.Idapunte = Acf.Idapunte(+)
                        Group By Fac.Idinstitucion,
                                 Fac.Idfacturacion,
                                 Apu.Idapunte,
                                 Apu.Idpersona,
                                 Apu.Idturno,
                                 Apu.Idguardia,
                                 Cgu.Fechainicio,
                                 Cgu.Fecha_Fin,
                                 Apu.Idhito,
                                 Apu.Precioaplicado) Dat,

                      --A.2. Tablas de otros datos
                      Cen_Colegiado     Col,
                      Cen_Persona       Per,
                      Scs_Turno         Tur,
                      Scs_Guardiasturno Gua

               Where Dat.Idinstitucion = Col.Idinstitucion
                 And Dat.Idpersona = Col.Idpersona
                 And Dat.Idpersona = Per.Idpersona
                 And Dat.Idinstitucion = Tur.Idinstitucion
                 And Dat.Idturno = Tur.Idturno
                 And Dat.Idinstitucion = Gua.Idinstitucion
                 And Dat.Idturno = Gua.Idturno
                 And Dat.Idguardia = Gua.Idguardia
               Group By Dat.Idinstitucion,
                         Dat.Idfacturacion,
                         Dat.Idturno,
                         Dat.Idguardia,
                         Dat.Idpersona,
                         Dat.Fechadesde,
                         Col.Ncolegiado,
                         Col.Ncomunitario,
                         Per.Apellidos1,
                         Per.Apellidos2,
                         Per.Nombre,
                         Tur.Abreviatura,
                         Gua.Nombre,
                         Dat.Tipoapunte,
                         Dat.Fechainicio,
                         Dat.Fechafin,
                         Dat.Fechaactuacion,
                         Gua.idTipoGuardia

              Union All

              --B. Actuaciones: que se compone de 2 grupos de datos:
              Select Fac.Idinstitucion,
                     Fac.Idfacturacion,
                     Fac.Fechadesde,
                     To_Number(Nvl(Col.Ncolegiado, Col.Ncomunitario)) As Numcol,
                     Per.Idpersona,
                     Per.Apellidos1 ||
                     Decode(Per.Apellidos2, Null, '', ' ' || Per.Apellidos2) || ', ' ||
                     Per.Nombre As Letrado,
                     Tur.Idturno As Idturno,
                     Tur.Abreviatura As Turno,
                     Gua.Idguardia As Idguardia,
                     Gua.Nombre As Guardia,
                     Decode(Act.Idhito, 9, 3, 2) As Ordapunte,
                     '    ' || f_siga_getrecurso_etiqueta('fcs.ficheroFacturacion.tipoApunte.' ||
                     Decode(Act.Idhito, 9, 'actuacionFG', 'actuacion'), v_idioma) As Tipoapunte,
                     Cab.Fechainicio,
                     Cab.Fecha_Fin As Fechafin,
                     Trunc(Act.Fechaactuacion) As Fechaactuacion,
                     Trunc(Act.Fechajustificacion) As Fechajustificacion,
                     'A-' || lpad(Act.Numero, 5, '0') || '/' || Substr(Act.Anio, 3, 2) As Asistencia,
                     Idactuacion, --Se necesita para agrupar al final
                     f_Siga_Cat_Numejg(Asi.Idinstitucion, Asi.Ejgidtipoejg, Asi.Ejganio, Asi.Ejgnumero) As Ejg,
                     Pjg.Nif,
                     Nvl(Pjg.Nombre, '') ||
                     Decode(Pjg.Apellido1, Null, '', ' ' || Pjg.Apellido1) ||
                     Decode(Pjg.Apellido2, Null, '', ' ' || Pjg.Apellido2) As Asistido,
                     Decode(Pjg.Sexo, 'H', 'H', 'M', 'D', Null) As Sexo,
                     f_Siga_Getdelitos_Asistencia(Fac.Idinstitucion,
                                                  Act.Anio,
                                                  Act.Numero,
                                                  v_idioma) As Delitos,
                     f_Siga_Getlugar_Actasi(Fac.Idinstitucion,
                                            Act.Anio,
                                            Act.Numero,
                                            Act.Idactuacion) As Lugar,
                     Null As Actfacgua,
                     Null As Actfacfue,
                     Null As Acttotgua,
                     Null As Acttotfue,
                     Case
                       When Act.Precioaplicado = 0 Or
                            Apu.Precioaplicado - Nvl(Impact, 0) < 0 Then
                        Null
                       When Gua.idTipoGuardia = 1 Then
                        '40a'
                       When Act.Idhito = 9 And Upper(Tur.Abreviatura) Like '%COMPL%' Then
                        '30cb'
                       When Act.Idhito = 9 And
                            Upper(Tur.Abreviatura) Not Like '%COMPL%' Then
                        '30b'
                       When Act.Idhito <> 9 And
                            Upper(Tur.Abreviatura) Like '%COMPL%' Then
                        '30c'
                       When Act.Idhito <> 9 And
                            Upper(Tur.Abreviatura) Not Like '%COMPL%' Then
                        '30'
                     End As Modulo,
                     Case
                       When Apu.Precioaplicado - Nvl(Impact, 0) < 0 Then
                        0
                       Else
                        Act.Precioaplicado
                     End As Importe

              --B.1. Datos de las actuaciones
                From Fcs_Facturacionjg Fac,
                     Fcs_Fact_Apunte Apu,
                     Fcs_Fact_Actuacionasistencia Act,
                     (Select Idinstitucion,
                             Idfacturacion,
                             Idapunte,
                             Sum(Precioaplicado) As Impact
                        From Fcs_Fact_Actuacionasistencia
                       Group By Idinstitucion, Idfacturacion, Idapunte) Imp,

                     --B.2. Tablas de otros datos
                     Scs_Asistencia       Asi,
                     Scs_Personajg        Pjg,
                     Scs_Cabeceraguardias Cab,
                     Cen_Colegiado        Col,
                     Cen_Persona          Per,
                     Scs_Turno            Tur,
                     Scs_Guardiasturno    Gua
               Where Fac.Idinstitucion = Apu.Idinstitucion
                 And Fac.Idfacturacion = Apu.Idfacturacion
                 And Apu.Idinstitucion = Act.Idinstitucion
                 And Apu.Idfacturacion = Act.Idfacturacion
                 And Apu.Idapunte = Act.Idapunte
                 And Act.Idinstitucion = Asi.Idinstitucion
                 And Act.Anio = Asi.Anio
                 And Act.Numero = Asi.Numero
                 And Asi.Idinstitucion = Pjg.Idinstitucion(+)
                 And Asi.Idpersonajg = Pjg.Idpersona(+)
                 And Apu.Idinstitucion = Cab.Idinstitucion
                 And Apu.Idturno = Cab.Idturno
                 And Apu.Idguardia = Cab.Idguardia
                 And Apu.Idpersona = Cab.Idpersona
                 And Apu.Fechainicio = Cab.Fechainicio
                 And Apu.Idinstitucion = Col.Idinstitucion
                 And Apu.Idpersona = Col.Idpersona
                 And Apu.Idpersona = Per.Idpersona
                 And Apu.Idinstitucion = Tur.Idinstitucion
                 And Apu.Idturno = Tur.Idturno
                 And Apu.Idinstitucion = Gua.Idinstitucion
                 And Apu.Idturno = Gua.Idturno
                 And Apu.Idguardia = Gua.Idguardia
                 And Apu.Idinstitucion = Imp.Idinstitucion(+)
                 And Apu.Idfacturacion = Imp.Idfacturacion(+)
                 And Apu.Idapunte = Imp.Idapunte(+)

              )
       Where Idinstitucion = v_Idinstitucion

         --Si no hay 2a, se escoge una unica facturacion:
         -- es necesario hacerlo asi para que el Excel de 1 facturacion
         -- no obtenga otras facturaciones de las mismas fechas
         -- pero de otros grupos de facturacion
         And ((v_Idfacturacion2 Is Null And
               Idfacturacion = v_Idfacturacion1) Or

             (v_Idfacturacion2 Is Not Null And
             Fechadesde Between
             (Select Fac.Fechadesde
                From Fcs_Facturacionjg Fac
               Where Fac.Idinstitucion = v_Idinstitucion
                 And Fac.Idfacturacion = v_Idfacturacion1) And
             (Select Fac.Fechahasta
                From Fcs_Facturacionjg Fac
               Where Fac.Idinstitucion = v_Idinstitucion
                 And Fac.Idfacturacion = v_Idfacturacion2)))

         And Idpersona = nvl(v_Idpersona, Idpersona)

       Group By Numcol,
                Idpersona,
                Letrado,
                Idturno,
                Turno,
                Idguardia,
                Guardia,
                Ordapunte,
                Tipoapunte,
                Fechainicio,
                Fechafin,
                Fechaactuacion,
                Fechajustificacion,
                Asistencia,
                Idactuacion,
                Ejg,
                Nif,
                Asistido,
                Sexo,
                Delitos,
                Lugar,
                Acttotgua,
                Acttotfue
       Order By Numcol, Fechainicio, Ordapunte, Fechaactuacion;

  Begin

    --Recuperacion de cabeceras
    Select Replace(descripcion, '##', Tabulador) Into v_Registro
      From Gen_Recursos
     Where idrecurso = 'fcs.ficheroFacturacion.cabecera.guardias.3001'
       And idlenguaje = p_idioma;

    --Comprobacion de fichero de salida
    If Utl_File.Is_Open(f_Salida) Then
      Utl_File.Fclose(f_Salida);
    End If;

    Cont := 0;

    For v_Linea In c_Export(p_Idioma,
                            p_Idinstitucion,
                            p_Idfacturacion1,
                            p_Idfacturacion2,
                            p_Idpersona) Loop

      If Cont = 0 Then
        /* Antes de escribir el primer registro, escribimos la cabecera*/
        f_Salida := Utl_File.Fopen(p_Pathfichero, p_Fichero, 'w');
        Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      End If;
      Cont := Cont + 1;

      v_Registro := v_Linea.Numcol || Tabulador ||
                    v_Linea.Letrado || Tabulador ||
                    v_Linea.Turno || Tabulador ||
                    v_Linea.Guardia || Tabulador ||
                    v_Linea.Tipoapunte || Tabulador ||
                    v_Linea.Fechainicio || Tabulador ||
                    v_Linea.Fechafin || Tabulador ||
                    v_Linea.Fechaactuacion || Tabulador ||
                    v_Linea.Fechajustificacion || Tabulador ||
                    v_Linea.Asistencia || Tabulador ||
                    v_Linea.Ejg || Tabulador ||
                    v_Linea.Asistido_Nif || Tabulador ||
                    v_Linea.Asistido_Nombre || Tabulador ||
                    v_Linea.Asistido_Sexo || Tabulador ||
                    v_Linea.Delitos || Tabulador ||
                    v_Linea.Lugar || Tabulador ||
                    v_Linea.Actfacgua || Tabulador ||
                    v_Linea.Actfacfue || Tabulador;
      If v_linea.asistencia Is Null Then
        v_registro := v_registro ||
                    FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion,
                                            p_Idfacturacion1,
                                            p_Idfacturacion2,
                                            v_linea.idturno,
                                            v_linea.idguardia,
                                            v_linea.idpersona,
                                            v_linea.fechainicio,
                                            v_linea.fechafin, '0') || Tabulador ||
                    FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion,
                                            p_Idfacturacion1,
                                            p_Idfacturacion2,
                                            v_linea.idturno,
                                            v_linea.idguardia,
                                            v_linea.idpersona,
                                            v_linea.fechainicio,
                                            v_linea.fechafin, '1') || Tabulador;
      Else
        v_registro := v_registro ||
                    '' || Tabulador ||
                    '' || Tabulador;
      End If;
      v_registro := v_registro ||
                    v_Linea.Modulo || Tabulador ||
                    f_num2char(v_Linea.Importe);

      Utl_File.Putf(f_Salida, v_Registro || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);

    End Loop;

    --cerrando fichero
    Utl_File.Fclose(f_Salida);
    p_Codretorno := '0';
    p_Datoserror := 'Fin correcto';

  Exception
    When Utl_File.Write_Error Then
      -- ERROR DEL S.O. DURANTE LA ESCRITURA

      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '5398';
      p_Datoserror := 'ERROR DEL S.O. DURANTE LA ESCRITURA';
    When Utl_File.Invalid_Filehandle Then
      -- EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO.
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := '5396';
      p_Datoserror := 'EL DESCRIPTOR NO ES DE UN ARCHIVO ABIERTO';

    -- posibles errores que se pueden producir al operar con ficheros.
    When Utl_File.Invalid_Path Then
      -- LA RUTA NO ES CORRECTA.(FOPEN)
      p_Codretorno := '5394';
      p_Datoserror := 'LA RUTA NO ES CORRECTA';
    When Utl_File.Invalid_Mode Then
      -- LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO.(FOPEN)
      p_Codretorno := '5395';
      p_Datoserror := 'LA CADENA NO ES VALIDA PARA EL MODO DE ARCHIVO';

    When Utl_File.Invalid_Operation Then
      -- OPERACION ERRONEA EN EL FICHERO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      p_Codretorno := Sqlcode || ' 5397';
      p_Datoserror := Sqlerrm || ' OPERACION ERRONEA EN EL FICHERO';

    When Utl_File.Internal_Error Then
      -- ERROR INTERNO NO ESPECIFICADO
      -- Cerrar fichero de salida
      Utl_File.Fclose(f_Salida);
      /* Tratamiento de las matrices */
      p_Codretorno := '5401';
      p_Datoserror := 'ERROR INTERNO NO ESPECIFICADO';
    When Others Then

      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm;
  End PROC_EXPORTAR_GUARDIAS_3001;

  /*
   *  Funciones que calculan los numeros de actuaciones y asistencias
   * para la exportacion del fichero de guardias en PROC_FCS_EXPORTAR_GUARDIAS
   *
   *  Si se pasa P_FECHAINICIO nulo, solo buscara en el dia exacto P_FECHAFIN
   *
   * INICIO
   */
  Function FUNC_NUM_ACTUAC_FACTURAC(p_Idinstitucion  Number,
                                    p_Idfacturacion1 Number,
                                    p_Idfacturacion2 Number,
                                    p_Idturno        Number,
                                    p_Idguardia      Number,
                                    p_Idpersona      Number,
                                    p_Fechainicio    Date,
                                    p_Fechafin       Date,
                                    p_Esfg           Varchar2)
    Return Number Is
    Valor Number;

  Begin
    Select Count(*)
      Into Valor
      From Fcs_Facturacionjg            Fac,
           Fcs_Fact_Actuacionasistencia Fact,
           Scs_Actuacionasistencia      Act,
           Scs_Asistencia               Asi
     Where Fact.Idinstitucion = Act.Idinstitucion
       And Fact.Anio = Act.Anio
       And Fact.Numero = Act.Numero
       And Fact.Idactuacion = Act.Idactuacion
       And Act.Idinstitucion = Asi.Idinstitucion
       And Act.Anio = Asi.Anio
       And Act.Numero = Asi.Numero
       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Idturno = p_Idturno
       And Asi.Idguardia = p_Idguardia
       And Asi.Idpersonacolegiado = p_Idpersona
       And Trunc(Asi.Fechahora) Between
           Trunc(Nvl(p_Fechainicio, p_Fechafin)) And Trunc(p_Fechafin)
       And Act.Diadespues =
           Upper(Decode(p_Esfg, '0', 'N', '1', 'S', p_Esfg))

       And Fact.Idinstitucion = Fac.Idinstitucion
       And Fact.Idfacturacion = Fac.Idfacturacion
       And Fac.Fechadesde Between
           (Select Fechadesde
              From Fcs_Facturacionjg
             Where Idinstitucion = p_Idinstitucion
               And Idfacturacion = p_Idfacturacion1)
       And (Select Fechahasta
              From Fcs_Facturacionjg
             Where Idinstitucion = p_Idinstitucion
               And Idfacturacion = Nvl(p_Idfacturacion2, p_Idfacturacion1))
       And Fac.Prevision = 0;

    Return Valor;
  End FUNC_NUM_ACTUAC_FACTURAC;

  Function FUNC_NUM_ACTUAC_TOTALES(p_Idinstitucion  Number,
                                   p_Idfacturacion1 Number,
                                   p_Idfacturacion2 Number,
                                   p_Idturno        Number,
                                   p_Idguardia      Number,
                                   p_Idpersona      Number,
                                   p_Fechainicio    Date,
                                   p_Fechafin       Date,
                                   p_Esfg           Varchar2) Return Number Is
    Valor Number;

  Begin
    Select Count(*)
      Into Valor
      From Fcs_Facturacionjg            Fac,
           Fcs_Fact_Actuacionasistencia Fact,
           Scs_Actuacionasistencia      Act,
           Scs_Asistencia               Asi
     Where Fact.Idinstitucion = Act.Idinstitucion
       And Fact.Anio = Act.Anio
       And Fact.Numero = Act.Numero
       And Fact.Idactuacion = Act.Idactuacion
       And Act.Idinstitucion = Asi.Idinstitucion
       And Act.Anio = Asi.Anio
       And Act.Numero = Asi.Numero
       And Asi.Idinstitucion = p_Idinstitucion
       And Asi.Idturno = p_Idturno
       And Asi.Idguardia = p_Idguardia
       And Asi.Idpersonacolegiado = p_Idpersona
       And Trunc(Asi.Fechahora) Between
           Trunc(Nvl(p_Fechainicio, p_Fechafin)) And Trunc(p_Fechafin)
       And Act.Diadespues =
           Upper(Decode(p_Esfg, '0', 'N', '1', 'S', p_Esfg))

       And Fact.Idinstitucion = Fac.Idinstitucion
       And Fact.Idfacturacion = Fac.Idfacturacion
       And Fac.Fechadesde <=
           (Select Max(Fechadesde)
              From Fcs_Facturacionjg
             Where Idinstitucion = p_Idinstitucion
               And Idfacturacion In (nvl(p_Idfacturacion1, Idfacturacion),
                                     nvl(p_Idfacturacion2, Idfacturacion)))
       And Fac.Prevision = 0;

    Return Valor;
  End FUNC_NUM_ACTUAC_TOTALES;

  function FUNC_NUM_ASIST_FACTURAC(P_IDINSTITUCION NUMBER,
                                   P_IDFACTURACION NUMBER,
                                   P_IDTURNO       NUMBER,
                                   P_IDGUARDIA     NUMBER,
                                   P_IDPERSONA     NUMBER,
                                   P_FECHAINICIO   DATE,
                                   P_FECHAFIN      DATE) return number is
    valor number;

  begin
    select count(*)
      into valor
      from FCS_FACT_ASISTENCIA FASI, SCS_ASISTENCIA ASI
     where fasi.idinstitucion = asi.idinstitucion
       and fasi.anio = asi.anio
       and fasi.numero = asi.numero
       and asi.idinstitucion = P_IDINSTITUCION
       and asi.idturno = P_IDTURNO
       and asi.idguardia = P_IDGUARDIA
       and asi.idpersonacolegiado = P_IDPERSONA
       and trunc(asi.fechahora) between
           trunc(nvl(P_FECHAINICIO, P_FECHAFIN)) and trunc(P_FECHAFIN)
       and fasi.idinstitucion = P_IDINSTITUCION
       and fasi.idfacturacion = P_IDFACTURACION;

    return valor;
  end FUNC_NUM_ASIST_FACTURAC;

  function FUNC_NUM_ASIST_TOTALES(P_IDINSTITUCION NUMBER,
                                  P_IDFACTURACION NUMBER,
                                  P_IDTURNO       NUMBER,
                                  P_IDGUARDIA     NUMBER,
                                  P_IDPERSONA     NUMBER,
                                  P_FECHAINICIO   DATE,
                                  P_FECHAFIN      DATE) return number is
    valor number;
  begin
    select count(*)
      into valor
      from FCS_FACT_ASISTENCIA FASI, SCS_ASISTENCIA ASI
     where fasi.idinstitucion = asi.idinstitucion
       and fasi.anio = asi.anio
       and fasi.numero = asi.numero
       and asi.idinstitucion = P_IDINSTITUCION
       and asi.idturno = P_IDTURNO
       and asi.idguardia = P_IDGUARDIA
       and asi.idpersonacolegiado = P_IDPERSONA
       and trunc(asi.fechahora) between
           trunc(nvl(P_FECHAINICIO, P_FECHAFIN)) and trunc(P_FECHAFIN)
       and fasi.idinstitucion = P_IDINSTITUCION
       and fasi.idfacturacion <= P_IDFACTURACION;

    return valor;
  end FUNC_NUM_ASIST_TOTALES;
  
  PROCEDURE PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                       P_IDFACTURACION IN NUMBER,
                                       P_PATHFICHERO   IN VARCHAR2,
                                       P_FICHERO       IN VARCHAR2,
                                       P_IDIOMA        IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2) IS
  BEGIN
    PROC_FCS_EXPORTAR_GUARDIAS(P_IDINSTITUCION,
                               P_IDFACTURACION,
                               null,
                               null,
                               P_PATHFICHERO,
                               P_FICHERO,
                               P_IDIOMA,
                               P_CODRETORNO,
                               P_DATOSERROR);
  END; --PROC_FCS_EXPORTAR_GUARDIAS


  /****************************************************************************************************************/
  /* Nombre:        FUNC_FACTURACIONES_INTERVALO                                                                  */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 22/06/2009                                                                                   */
  /* Autor:         Jose Barrientos Diaz                                                                          */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Facturaciones_Intervalo(p_Idinstitucion  Number,
                                        p_Idfacturacion1 Number,
                                        p_Idfacturacion2 Number)
    Return Varchar2 Is

    Cursor c_Facturaciones(p_Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type, p_Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type) Is
      Select Fac.Idfacturacion
        From Fcs_Facturacionjg Fac
       Where Fac.Prevision = '0'
         And Fac.Regularizacion = '0'
         And Fac.Idinstitucion = p_Idinstitucion
         And Fac.Fechadesde >= p_Fecha_Ini
         And Fac.Fechahasta <= p_Fecha_Fin
       Order By Fac.Fechadesde Asc;

    Fecha_1   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_2   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_3   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_4   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type;
    Rc        Varchar2(4000);

    v_Consejo cen_institucion.Cen_Inst_Idinstitucion%Type;
    v_Idgrupofacturacion Fcs_Fact_Grupofact_Hito.Idgrupofacturacion%Type;

    idfact_anterior Fcs_Facturacionjg.Idfacturacion%Type;

  Begin

    -- Obtenemos las fechas de la primera facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_1, Fecha_2
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion1;
    -- Obtenemos las fechas de la segunda facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_3, Fecha_4
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion2;

    -- La primera fechadesde marca el inicio del intervalo
    If (Fecha_1 < Fecha_3) Then
      Fecha_Ini := Fecha_1;
    Else
      Fecha_Ini := Fecha_3;
    End If;
    -- La ultima fechahasta marca el final del intervalo
    If (Fecha_2 > Fecha_4) Then
      Fecha_Fin := Fecha_2;
    Else
      Fecha_Fin := Fecha_4;
    End If;

    -- Sacando solo una facturacion si solo se selecciona una
    If (p_Idfacturacion2 Is Null) Then
      Return p_Idfacturacion1;
    Elsif (p_Idfacturacion1 = p_Idfacturacion2) Then
      Return p_Idfacturacion1;
    End If;
    
    -- Obtiene el consejo del colegio 
    v_Consejo := FUNC_CONSEJO_COLEGIO(P_IDINSTITUCION);

    idfact_anterior := -1;
    If (v_Consejo = C_CATALAN) Then
      For v_Fact In c_Facturaciones(Fecha_Ini, Fecha_Fin) Loop
        Select Idgrupofacturacion
          Into v_Idgrupofacturacion
          From Fcs_Fact_Grupofact_Hito
         Where Idinstitucion = p_Idinstitucion
           And Idfacturacion = v_Fact.Idfacturacion
         Group By Idgrupofacturacion;

        If (v_Idgrupofacturacion = 1) Then
          If (v_Fact.Idfacturacion <> idfact_anterior) Then
            -- Control para no duplicar el pago
            Rc := Rc || ', ' || v_Fact.Idfacturacion;
            idfact_anterior := v_Fact.Idfacturacion;
          END IF;
        End If;
      End Loop;
    Else
      For v_Fact In c_Facturaciones(Fecha_Ini, Fecha_Fin) Loop
        IF (True) THEN
          If (v_Fact.Idfacturacion <> idfact_anterior) Then
            -- Control para no duplicar el pago
            Rc := Rc || ', ' || v_Fact.Idfacturacion;
            idfact_anterior := v_Fact.Idfacturacion;
          END IF;
        END IF;
      End Loop;
    End If;

    Rc := Ltrim(Rc, ',');
    Return(Rc);

  Exception
    When No_Data_Found Then
      Return(1);
    When Others Then
      Return(-1);

  End Func_Facturaciones_Intervalo;

  /****************************************************************************************************************/
  /* Nombre:        Func_Factura_Inter_Grupos                                                                  */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 22/06/2009                                                                                   */
  /* Autor:         Jose Barrientos Diaz                                                                          */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Factura_Inter_Grupos(p_Idinstitucion  Number,
                                        p_Idfacturacion1 Number,
                                        p_Idfacturacion2 Number,
                                        p_GrupoFact Number)
    Return Varchar2 Is

    Cursor c_Facturaciones(p_Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type, p_Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type) Is
      Select Fac.Idfacturacion, gru.idgrupofacturacion
        From Fcs_Facturacionjg Fac, fcs_fact_grupofact_hito gru
       Where Fac.Prevision = '0'
         And Fac.Regularizacion = '0'
         And Fac.Idinstitucion = p_Idinstitucion
         and Fac.Idinstitucion = gru.Idinstitucion
         AND Fac.Idfacturacion = gru.Idfacturacion
         And Fac.Fechadesde >= p_Fecha_Ini
         And Fac.Fechahasta <= p_Fecha_Fin
       Order By Fac.Fechadesde Asc;

    Fecha_1   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_2   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_3   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_4   Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Ini Fcs_Facturacionjg.Fechadesde%Type;
    Fecha_Fin Fcs_Facturacionjg.Fechadesde%Type;
    Rc        Varchar2(4000);

    idfact_anterior Fcs_Facturacionjg.Idfacturacion%Type;

  Begin

    -- Obtenemos las fechas de la primera facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_1, Fecha_2
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion1;
    -- Obtenemos las fechas de la segunda facturacion
    Select Fac.Fechadesde, Fac.Fechahasta
      Into Fecha_3, Fecha_4
      From Fcs_Facturacionjg Fac
     Where Idinstitucion = p_Idinstitucion
       And Idfacturacion = p_Idfacturacion2;

    -- La primera fechadesde marca el inicio del intervalo
    If (Fecha_1 < Fecha_3) Then
      Fecha_Ini := Fecha_1;
    Else
      Fecha_Ini := Fecha_3;
    End If;
    -- La ultima fechahasta marca el final del intervalo
    If (Fecha_2 > Fecha_4) Then
      Fecha_Fin := Fecha_2;
    Else
      Fecha_Fin := Fecha_4;
    End If;

    -- Sacando solo una facturacion si solo se selecciona una
    If (p_Idfacturacion2 Is Null) Then
      Return p_Idfacturacion1;
    Elsif (p_Idfacturacion1 = p_Idfacturacion2) Then
      Return p_Idfacturacion1;
    End If;

    idfact_anterior := -1;
    For v_Fact In c_Facturaciones(Fecha_Ini, Fecha_Fin) Loop
      IF (p_GrupoFact = -1 Or v_Fact.Idgrupofacturacion = p_GrupoFact) THEN
        If (v_Fact.Idfacturacion <> idfact_anterior) Then
          -- Control para no duplicar el pago
          Rc := Rc || ', ' || v_Fact.Idfacturacion;
          idfact_anterior := v_Fact.Idfacturacion;
        END IF;
      END IF;
    End Loop;

    Rc := Ltrim(Rc, ',');
    Return(Rc);

  Exception
    When No_Data_Found Then
      Return(1);
    When Others Then
      Return(-1);

  End Func_Factura_Inter_Grupos;
END PKG_SIGA_FACT_SJCS_HITOS;
/
