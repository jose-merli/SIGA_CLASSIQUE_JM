CREATE OR REPLACE PACKAGE PKG_SIGA_PAGOS_SJCS IS

  -- Author  : Pilar Duran
  -- Created : 24/03/2006
  -- Purpose : Procedimientos para los Pagos de SJCS

  /* Definición del tipo de la matriz de pagos */
  TYPE MATRICE_PAGOS IS RECORD(
    IDPERSONA      FCS_PAGO_COLEGIADO.IDPERORIGEN%TYPE,
    TOTALFACTURADO FCS_PAGO_COLEGIADO.IMPOFICIO%TYPE,
    TOTALANTERIOR  FCS_PAGO_COLEGIADO.IMPOFICIO%TYPE,
    PAGOACTUAL     FCS_PAGO_COLEGIADO.IMPOFICIO%TYPE);
  TYPE TAB_PAGOS IS TABLE OF MATRICE_PAGOS INDEX BY BINARY_INTEGER;

 
  -- Public function and procedure declarations

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_IMPORTE_TURNOS                                                                        */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 10/05/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  /*PROCEDURE PROC_FCS_IMPORTE_TURNOS(P_IDINSTITUCION IN VARCHAR2,
                                    P_IDPAGO        IN VARCHAR2,

                                    P_TOTAL      OUT VARCHAR2,
                                    P_TOTALIRPF  OUT VARCHAR2,
                                    P_CODRETORNO OUT VARCHAR2,
                                    P_DATOSERROR OUT VARCHAR2);*/

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_IMPORTE_SOJ                                                                        */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 10/05/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_IMPORTES_PAGOS(P_IDINSTITUCION IN VARCHAR2,
                                    P_IDPAGO        IN VARCHAR2,

                                    P_TOTAL         OUT VARCHAR2,
                                    P_TOTALREPARTIR OUT VARCHAR2,
                                    P_CODRETORNO    OUT VARCHAR2,
                                    P_DATOSERROR    OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre: FUN_FCS_IMPORTES_PAGO                                                                                */
  /* Descripcion: devuelve el total pagado.                                                                       */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      VARCHAR2,      */
  /* P_IDFACTURACION       IN                                                                      VARCHAR2,      */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 12/05/2005                                                                                   */
  /* Autor:    David Sanchez Pina                                                                                 */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  FUNCTION FUN_FCS_IMPORTES_PAGO(P_IDINSTITUCION IN VARCHAR2,
                                 P_IDFACTURACION IN VARCHAR2) RETURN VARCHAR2;


  /****************************************************************************************************************/
  /* Nombre: FUN_FCS_FACTURACION_POR_PAGAR                                                                        */
  /* Descripcion: devuelve 0 si la facturacion ya se ha pagado completamente, 1 en caso contrario                 */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      VARCHAR2,      */
  /* P_IDFACTURACION       IN                                                                      VARCHAR2,      */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 03/09/2009                                                                                   */
  /* Autor:  Juan Antonio Saiz Usano                                                                              */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
 FUNCTION FUN_FCS_FACTURACION_POR_PAGAR (P_IDINSTITUCION IN VARCHAR2,
                                 P_IDFACTURACION IN VARCHAR2) RETURN VARCHAR2;
                                 
                                 
  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_IMPORTE_RETENC                                                                            */
  /* Descripcion: Calcula la suma del importe de las Retenciones Judiciales                                       */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 10/05/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_IMPORTE_RETENC(P_IDINSTITUCION IN VARCHAR2,
                                    P_IDPAGO        IN VARCHAR2,
                                    P_TOTAL         OUT VARCHAR2,
                                    P_CODRETORNO    OUT VARCHAR2,
                                    P_DATOSERROR    OUT VARCHAR2);

 
/****************************************************************************************************************/
  /* Nombre: EXISTE_PAGO_COLEGIADO                                                                              */
  /* Descripcion: Retorna 0 si no existe el pago, 1 en caso contrario                                                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      NUMBER,        */
  /* P_IDPAGO              IN                                                                      NUMBER,        */
  /*                                                                                                              */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 16/09/2009                                                                                  */
  /* Autor:    Juan Antonio Saiz                                                                                  */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  FUNCTION EXISTE_PAGO_COLEGIADO(P_IDINSTITUCION in VARCHAR2, 
                                 P_IDPAGO in VARCHAR2,  
                                 P_IDPERSONA in VARCHAR2) return number;
  

 
  /****************************************************************************************************************/
  /* Nombre: FUN_FCS_IMPORTES_PAGO                                                                                */
  /* Descripcion: devuelve el total pagado.                                                                       */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      VARCHAR2,      */
  /* P_IDFACTURACION       IN                                                                      VARCHAR2,      */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 12/05/2005                                                                                   */
  /* Autor:    David Sanchez Pina                                                                                 */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  FUNCTION FUN_FCS_OBTENERIDPERSONASOCIED(P_IDPERSONA     IN NUMBER,
                                          P_IDINSTITUCION IN NUMBER)
    RETURN NUMBER;

  /****************************************************************************************************************/
  /* Nombre: PROC_FCS_CALCULAR_IRPF                                                                               */
  /* Descripcion: Proceso que calcula el IRPF a aplicar.                                                          */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDPERSONA           IN       Identificador de la persona                                    NUMBER,        */
  /* P_ESSOCIEDAD          IN       variable que nos indica si la persona pertenece a
                                    una sociedad o no.                                             NUMBER,
  /* P_INSTITUCION         IN       Identificador de la institucion                                NUMBER,        */
  /* P_RETENCION           OUT      Devuelve la retencion a aplicar.
  /* P_CODRETORNO          OUT      Devuelve 0 en caso de que la ejecucion haya sido OK.           VARCHAR2(10)   */
  /*                                En caso de error devuelve el codigo de error Oracle                           */
  /*                                correspondiente.                                                              */
  /* P_DATOSERROR          OUT      Devuelve null en caso de que la ejecucion haya sido OK.        VARCHAR2(200)  */
  /*                                En caso de error devuelve el mensaje de error Oracle                          */
  /*                                correspondiente.                                                              */
  /*                                                                                                              */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 6/04/2006                                                                                   */
  /* Autor:    Pilar Duran Mu?oz                                                                                */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_CALCULAR_IRPF(P_IDPERSONA   IN NUMBER,
                                   P_ESSOCIEDAD  IN NUMBER,
                                   P_INSTITUCION IN NUMBER,
                                   P_RETENCION   OUT NUMBER,
                                   P_IDRETENCION   OUT NUMBER,
                                   P_CODRETORNO  OUT VARCHAR2,
                                   P_DATOSERROR  OUT VARCHAR2);
                                     
 PROCEDURE PROC_FCS_PAGO_TURNOS_OFI(P_IDINSTITUCION   IN VARCHAR2,
                                     P_IDPAGO          IN VARCHAR2,
                                     P_USUMODIFICACION IN VARCHAR2,
                                     P_TOTAL           OUT VARCHAR2,
                                     P_CODRETORNO      OUT VARCHAR2,
                                     P_DATOSERROR      OUT VARCHAR2);                                     

  FUNCTION FUNC_FCS_CARGA_TURNOSOFICIO(P_IDINSTITUCION IN NUMBER,
                                       P_IDPAGO        IN NUMBER,
                                       P_CODRETORNO    OUT VARCHAR2,
                                       P_DATOSERROR    OUT VARCHAR2)
                                       return tab_pagos;
                                   
  PROCEDURE PROC_FCS_PAGO_GUARDIAS(P_IDINSTITUCION   IN VARCHAR2,
                                   P_IDPAGO          IN VARCHAR2,
                                   P_USUMODIFICACION IN VARCHAR2,
                                   P_TOTAL           OUT VARCHAR2,
                                   P_CODRETORNO      OUT VARCHAR2,
                                   P_DATOSERROR      OUT VARCHAR2);

  FUNCTION FUNC_FCS_CARGA_GUARDIAS(P_IDINSTITUCION IN NUMBER,
                                   P_IDPAGO        IN NUMBER,
                                   P_CODRETORNO    OUT VARCHAR2,
                                   P_DATOSERROR    OUT VARCHAR2)
                                   return tab_pagos;

  PROCEDURE PROC_FCS_PAGO_SOJ(P_IDINSTITUCION   IN VARCHAR2,
                              P_IDPAGO          IN VARCHAR2,
                              P_USUMODIFICACION IN VARCHAR2,
                              P_TOTAL           OUT VARCHAR2,
                              P_CODRETORNO      OUT VARCHAR2,
                              P_DATOSERROR      OUT VARCHAR2);

  FUNCTION FUNC_FCS_CARGA_SOJ(P_IDINSTITUCION IN NUMBER,
                              P_IDPAGO        IN NUMBER,
                              P_CODRETORNO    OUT VARCHAR2,
                              P_DATOSERROR    OUT VARCHAR2) 
                              return tab_pagos;

  PROCEDURE PROC_FCS_PAGO_EJG(P_IDINSTITUCION   IN VARCHAR2,
                              P_IDPAGO          IN VARCHAR2,
                              P_USUMODIFICACION IN VARCHAR2,
                              P_TOTAL           OUT VARCHAR2,
                              P_CODRETORNO      OUT VARCHAR2,
                              P_DATOSERROR      OUT VARCHAR2);

  FUNCTION FUNC_FCS_CARGA_EJG(P_IDINSTITUCION IN NUMBER,
                              P_IDPAGO        IN NUMBER,
                              P_CODRETORNO    OUT VARCHAR2,
                              P_DATOSERROR    OUT VARCHAR2) 
                              return tab_pagos;

  PROCEDURE CALCULA_PAGO(M_PAGOS            IN OUT TAB_PAGOS,
                         v_importeARepartir IN OUT number,
                         total              OUT number);


  /****************************************************************************************************************/
  /* Nombre:        FUNC_PAGOS_INTERVALO                                                                          */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 27/04/2011                                                                                   */
  /* Autor:         Carlos Ruano Martínez                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Pagos_Intervalo(p_Idinstitucion  Number,
                                        p_Idpago1 Number,
                                        p_Idpago2 Number)
    Return Varchar2;

 /****************************************************************************************************************/
  /* Nombre:        FUNC_PAGOS_INTERVALO_GRUPOFACT                                                                         */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 13/07/2011                                                                                   */
  /* Autor:         Carlos Ruano Martínez                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Pagos_Intervalo_GrupoFact(p_Idinstitucion  Number,
                                        p_Idpago1 Number,
                                        p_Idpago2 Number,
                                        p_GrupoFact Number)
    Return Varchar2;


END PKG_SIGA_PAGOS_SJCS;
 
/
create or replace package body PKG_SIGA_PAGOS_SJCS is


  -- Public variable declarations:

  --CONTROL DE ERRORES:
  V_CODRETORNO                 VARCHAR2(10) := TO_CHAR(0);  /* Codigo de error Oracle */
  V_DATOSERROR                 VARCHAR2(400) := NULL;     /* Mensaje de error en los procedimientos */

  --EXCEPCIONES:
  E_ERROR                        EXCEPTION;
  CTE                            NUMBER:=0.01;


  -- Function and procedure implementations:

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_IMPORTE_TURNOS                                                                        */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 10/05/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  /*PROCEDURE PROC_FCS_IMPORTE_TURNOS(P_IDINSTITUCION               IN VARCHAR2,
                                    P_IDPAGO               IN VARCHAR2,

                                    P_TOTAL                OUT VARCHAR2,
                                    P_TOTALIRPF            OUT VARCHAR2,
                                    P_CODRETORNO           OUT VARCHAR2,
                                    P_DATOSERROR           OUT VARCHAR2) IS

    -- VARIABLES
    V_TOTAL       NUMBER(10,2) := 0;
    V_TOTALIRPF   NUMBER(10,2) := 0;

  BEGIN

    SELECT SUM(IMPORTEPAGADO), SUM(IMPORTEIRPF)
    INTO V_TOTAL, V_TOTALIRPF
    FROM FCS_PAGO_ACTUACIONDESIGNA
    WHERE IDINSTITUCION = P_IDINSTITUCION
    AND   IDPAGOSJG = P_IDPAGO;

    P_TOTAL := 0;
    IF (V_TOTAL IS NOT NULL) THEN
        P_TOTAL := TO_CHAR(V_TOTAL);
    END IF;
    P_TOTALIRPF := 0;
    IF (V_TOTALIRPF IS NOT NULL) THEN
        P_TOTALIRPF := TO_CHAR(V_TOTALIRPF);
    END IF;

    P_CODRETORNO := 0;
    P_DATOSERROR := NULL;

  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_TOTALIRPF := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; -- Procedure PROC_FCS_IMPORTE_TURNOS*/


  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_IMPORTE_SOJ                                                                        */
  /* Descripcion:
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 10/05/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  PROCEDURE PROC_FCS_IMPORTES_PAGOS (P_IDINSTITUCION         IN VARCHAR2,
                                     P_IDPAGO               IN VARCHAR2,
                                     P_TOTAL                OUT VARCHAR2,
                                     P_TOTALREPARTIR        OUT VARCHAR2,
                                     P_CODRETORNO           OUT VARCHAR2,
                                     P_DATOSERROR           OUT VARCHAR2) IS

    -- VARIABLES
    V_TOTAL   NUMBER(10,2) := 0;
    V_TOTALREPARTIR   NUMBER(10,2) := 0;

  BEGIN

    BEGIN
      SELECT IMPORTEPAGADO, IMPORTEREPARTIR
      INTO V_TOTAL, V_TOTALREPARTIR
      FROM FCS_PAGOSJG
      WHERE IDINSTITUCION = P_IDINSTITUCION
      AND   IDPAGOSJG = P_IDPAGO;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN NULL;
    END;

    P_TOTAL := 0;
    IF (V_TOTAL IS NOT NULL) THEN
        P_TOTAL := TO_CHAR(V_TOTAL);
    END IF;
    P_TOTALREPARTIR := 0;
    IF (V_TOTALREPARTIR IS NOT NULL) THEN
       P_TOTALREPARTIR := TO_CHAR(V_TOTALREPARTIR);
    END IF;

    P_CODRETORNO := 0;
    P_DATOSERROR := NULL;

  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_TOTALREPARTIR := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;


  END; -- Procedure PROC_FCS_IMPORTE_SOJ



  /****************************************************************************************************************/
  /* Nombre: FUN_FCS_IMPORTES_PAGO                                                                               */
  /* Descripcion: devuelve el total pagado.                                                                       */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      VARCHAR2,      */
  /* P_IDFACTURACION       IN                                                                      VARCHAR2,      */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 12/05/2005                                                                                   */
  /* Autor:    David Sanchez Pina                                                                                 */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /* 01/10/2009           Juan Antonio Saiz                   Obtener los importes y porcentajes parciales de un pago */
  /****************************************************************************************************************/
  FUNCTION FUN_FCS_IMPORTES_PAGO (P_IDINSTITUCION        IN VARCHAR2,
                                  P_IDFACTURACION        IN VARCHAR2) RETURN  VARCHAR2 IS

    -- VARIABLES
    V_TOTALPAGADO    VARCHAR2(400);

  BEGIN
    V_TOTALPAGADO := 0;

    BEGIN

      SELECT SUM (P.IMPORTEPAGADO) || '~' ||
             SUM (P.IMPORTEOFICIO) || '~' || SUM (P.IMPORTEGUARDIA) || '~' || SUM (P.IMPORTEEJG) || '~' || SUM (P.IMPORTESOJ) || '~' ||
             round( SUM(P.IMPORTEOFICIO)  * 100 / decode(max(F.IMPORTEOFICIO),0,1,max(F.IMPORTEOFICIO))  ,2) || '~' || 
             round( SUM(P.IMPORTEGUARDIA) * 100 / decode(max(F.IMPORTEGUARDIA),0,1,max(F.IMPORTEGUARDIA)) ,2) || '~' || 
             round( SUM(P.IMPORTEEJG)     * 100 / decode(max(F.IMPORTEEJG),0,1,max(F.IMPORTEEJG))     ,2) || '~' || 
             round( SUM(P.IMPORTESOJ)     * 100 / decode(max(F.IMPORTESOJ),0,1,max(F.IMPORTESOJ))     ,2)     
        INTO V_TOTALPAGADO            
        FROM FCS_PAGOSJG P, FCS_PAGOS_ESTADOSPAGOS E, FCS_FACTURACIONJG F
       WHERE P.IDINSTITUCION = P_IDINSTITUCION
         AND P.IDFACTURACION = P_IDFACTURACION
         AND E.IDESTADOPAGOSJG = (SELECT MAX(IDESTADOPAGOSJG)
                                    FROM FCS_PAGOS_ESTADOSPAGOS
                                   WHERE IDPAGOSJG = P.IDPAGOSJG
                                     AND IDINSTITUCION = P_IDINSTITUCION
                                     AND IDESTADOPAGOSJG > PKG_SIGA_CONSTANTES.ESTADO_PAGO_EJECUTADO)
         AND P.IDINSTITUCION = E.IDINSTITUCION
         AND P.IDPAGOSJG = E.IDPAGOSJG
         AND F.IDINSTITUCION = P.IDINSTITUCION
         AND F.IDFACTURACION = P.IDFACTURACION;
       
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
            V_TOTALPAGADO := '0~0~0~0~0~0~0~0~0';
      WHEN OTHERS THEN
            V_TOTALPAGADO := '0~0~0~0~0~0~0~0~0';
    END;

    IF (V_TOTALPAGADO = '~~~~~~~~') THEN
        V_TOTALPAGADO := '0~0~0~0~0~0~0~0~0';
    END IF;
    
    IF (V_TOTALPAGADO IS NULL) THEN
        V_TOTALPAGADO := '0~0~0~0~0~0~0~0~0';
    END IF;

    RETURN V_TOTALPAGADO;
  END; --FUN_FCS_IMPORTES_PAGO


 /****************************************************************************************************************/
  /* Nombre: FUN_FCS_FACTURACION_POR_PAGAR                                                                        */
  /* Descripcion: devuelve 0 si la facturacion ya se ha pagado completamente, 1 en caso contrario                 */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      VARCHAR2,      */
  /* P_IDFACTURACION       IN                                                                      VARCHAR2,      */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 03/09/2009                                                                                   */
  /* Autor:  Juan Antonio Saiz Usano                                                                              */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  FUNCTION FUN_FCS_FACTURACION_POR_PAGAR (P_IDINSTITUCION        IN VARCHAR2,
                                  P_IDFACTURACION        IN VARCHAR2) RETURN  VARCHAR2 IS

    -- VARIABLES
    sumOficio         number(10,2);
    sumGuardias       number(10,2);
    sumEJG            number(10,2);
    sumSOJ            number(10,2);
    
    factOficio        number(10,2);
    factGuardias      number(10,2);
    factEJG           number(10,2);
    factSOJ           number(10,2);
              
  BEGIN

    BEGIN
      SELECT SUM (P.IMPORTEOFICIO) , SUM (P.IMPORTEGUARDIA) ,
             SUM (P.IMPORTEEJG) , SUM (P.IMPORTESOJ)
      INTO sumOficio, sumGuardias, sumEJG, sumSOJ
      FROM FCS_PAGOSJG P, FCS_PAGOS_ESTADOSPAGOS E
      WHERE P.IDINSTITUCION = P_IDINSTITUCION
      AND   P.IDFACTURACION = P_IDFACTURACION
      AND   E.IDESTADOPAGOSJG = (SELECT MAX(IDESTADOPAGOSJG)
                                 FROM FCS_PAGOS_ESTADOSPAGOS
                                 WHERE IDPAGOSJG = P.IDPAGOSJG
                                 AND IDINSTITUCION = P_IDINSTITUCION
                                 AND IDESTADOPAGOSJG > PKG_SIGA_CONSTANTES.ESTADO_PAGO_EJECUTADO)
      AND P.IDINSTITUCION = E.IDINSTITUCION
      AND P.IDPAGOSJG = E.IDPAGOSJG;
      
    SELECT F.Importeoficio, F.Importeguardia, F.Importeejg, F.Importesoj 
      INTO factOficio, factGuardias, factEJG, factSOJ
      FROM FCS_FACTURACIONJG F, FCS_FACT_ESTADOSFACTURACION E
     WHERE F.IDINSTITUCION = E.IDINSTITUCION
       AND F.IDFACTURACION = E.IDFACTURACION
       AND F.IDINSTITUCION = P_IDINSTITUCION
       AND F.IDFACTURACION = P_IDFACTURACION
       AND E.IDESTADOFACTURACION = 30;


    EXCEPTION
      WHEN NO_DATA_FOUND THEN
            return 'no data';
      WHEN OTHERS THEN
            return 'others';
    END;
    
 
    IF ((factOficio is not null and factOficio > 0) and (sumOficio is null or sumOficio < factOficio)  or 
        (factGuardias is not null and factGuardias > 0) and (sumGuardias is null or sumGuardias < factGuardias)   or 
        (factEJG is not null and factEJG > 0) and (sumEJG is null or sumEJG < factEJG) or 
        (factSOJ is not null and factSOJ > 0) and (sumSOJ is null or sumSOJ < factSOJ) ) THEN
       return '1';
    END IF;
    
    RETURN 'final';
  END; --FUN_FCS_FACTURACION_POR_PAGAR

  /****************************************************************************************************************/
  /* Nombre:   PROC_FCS_IMPORTE_RETENC                                                                            */
  /* Descripcion: Calcula la suma del importe de las Retenciones Judiciales                                       */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 10/05/2005                                                                                   */
  /* Autor:          Raul Gonzalez Gonzalez
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/
  PROCEDURE PROC_FCS_IMPORTE_RETENC      (P_IDINSTITUCION        IN VARCHAR2,
                                          P_IDPAGO               IN VARCHAR2,
                                          P_TOTAL                OUT VARCHAR2,
                                          P_CODRETORNO           OUT VARCHAR2,
                                          P_DATOSERROR           OUT VARCHAR2) IS

    -- VARIABLES
    V_TOTAL   NUMBER(10,2) := 0;

  BEGIN

    SELECT SUM(IMPORTERETENIDO)
    INTO V_TOTAL
    FROM FCS_COBROS_RETENCIONJUDICIAL
    WHERE IDINSTITUCION = P_IDINSTITUCION
    AND   IDPAGOSJG = P_IDPAGO;

    P_TOTAL := 0;
    IF (V_TOTAL IS NOT NULL) THEN
        P_TOTAL := TO_CHAR(V_TOTAL);
    END IF;

    P_DATOSERROR := TO_CHAR('PROC_FCS_IMPORTE_RETENC:Fin Correcto');
    P_CODRETORNO := TO_CHAR('0');

  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; -- Procedure PROC_FCS_IMPORTE_RETENC




 

/****************************************************************************************************************/
  /* Nombre: EXISTE_PAGO_COLEGIADO                                                                              */
  /* Descripcion: Retorna 0 si no existe el pago, 1 en caso contrario                                                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /* P_IDINSTITUCION       IN                                                                      NUMBER,        */
  /* P_IDPAGO              IN                                                                      NUMBER,        */
  /*                                                                                                              */
  /* Version:         1.0                                                                                         */
  /* Fecha Creacion: 16/09/2009                                                                                  */
  /* Autor:    Juan Antonio Saiz                                                                                  */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

 

  FUNCTION EXISTE_PAGO_COLEGIADO (P_IDINSTITUCION in VARCHAR2, P_IDPAGO in VARCHAR2,  P_IDPERSONA in VARCHAR2) return number is
  
  existePagoColegiado                   number:=0;
  
  begin
    Select count(1)
      into existePagoColegiado
      from dual
     where exists (select 1
             from Fcs_Pago_Colegiado
            where IDINSTITUCION = P_IDINSTITUCION
              and IDPAGOSJG = P_IDPAGO
              and IDPERORIGEN = P_IDPERSONA);

     return existePagoColegiado;

  end EXISTE_PAGO_COLEGIADO;  
  
  
 


FUNCTION FUN_FCS_OBTENERIDPERSONASOCIED(P_IDPERSONA number, P_IDINSTITUCION NUMBER) return  NUMBER
is

V_IDPERSONASOCIEDAD NUMBER:=-1;

begin
  begin
    select idpersona INTO V_IDPERSONASOCIEDAD
      from cen_componentes
     where cen_componentes.cen_cliente_idinstitucion=P_IDINSTITUCION
       AND cen_componentes.cen_cliente_idpersona=P_IDPERSONA
       AND SOCIEDAD=1
       And (cen_componentes.fechabaja Is Null Or cen_componentes.fechabaja > Sysdate)
       AND ROWNUM=1
    ORDER BY IDINSTITUCION,IDPERSONA,IDCOMPONENTE;

  EXCEPTION
  WHEN NO_DATA_FOUND THEN
   V_IDPERSONASOCIEDAD:=-1;
  WHEN OTHERS then
   V_IDPERSONASOCIEDAD:=-1;
  end;

  return(V_IDPERSONASOCIEDAD);

end FUN_FCS_OBTENERIDPERSONASOCIED;

  -- Descripcion: Proceso que calcula el IRPF a aplicar.                                                          */
  Procedure PROC_FCS_CALCULAR_IRPF(p_Idpersona   In Number,
                                   p_Essociedad  In Number,
                                   p_Institucion In Number,
                                   p_Retencion   Out Number,
                                   p_Idretencion Out Number,
                                   p_Codretorno  Out Varchar2,
                                   p_Datoserror  Out Varchar2) Is
  
    v_Retencion   Scs_Maestroretenciones.Retencion%Type;
    v_Idretencion Scs_Maestroretenciones.Idretencion%Type;
  Begin
    Begin
      Select Scs_Maestroretenciones.Retencion, Scs_Maestroretenciones.Idretencion
        Into v_Retencion, v_Idretencion
        From Scs_Retencionesirpf, Scs_Maestroretenciones
       Where Scs_Retencionesirpf.Idretencion = Scs_Maestroretenciones.Idretencion
         And Scs_Retencionesirpf.Idinstitucion = p_Institucion
         And Scs_Retencionesirpf.Idpersona = p_Idpersona
         And Trunc(Sysdate) Between Trunc(Scs_Retencionesirpf.Fechainicio) And
             Nvl(Trunc(Scs_Retencionesirpf.Fechafin), '31/12/2999')
         /*And ((Scs_Retencionesirpf.Fechainicio <= Sysdate And
             Sysdate <= Scs_Retencionesirpf.Fechafin) Or
             (Scs_Retencionesirpf.Fechainicio <= Sysdate And
             Scs_Retencionesirpf.Fechafin Is Null))*/
       Order By Scs_Retencionesirpf.Fechainicio Asc;
    Exception
      When Others Then
        v_Retencion   := 0;
        v_Idretencion := 0;
        p_Codretorno  := To_Char(Sqlcode);
        p_Datoserror  := p_Datoserror || ' ' || Sqlerrm;
    End;
  
    If (v_Retencion < 0) Then
      p_Retencion   := 0;
      p_Idretencion := 0;
    Else
      p_Retencion   := v_Retencion;
      p_Idretencion := v_Idretencion;
    End If;
  
    --Actualizo para saber que el procedimiento ha finalizado correctamente:
    If (p_Codretorno Is Null) Then
      p_Datoserror := 'PROCEDURE PROC_FCS_CALCULAR_IRPF: ha finalizado correctamente.';
      p_Codretorno := To_Char(0);
    End If;
  
  Exception
    When Others Then
      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := p_Datoserror || ' ' || Sqlerrm;
  End PROC_FCS_CALCULAR_IRPF;


  PROCEDURE PROC_FCS_PAGO_TURNOS_OFI (P_IDINSTITUCION   IN VARCHAR2,
                                      P_IDPAGO          IN VARCHAR2,
                                      P_USUMODIFICACION IN VARCHAR2,
                                      P_TOTAL           OUT VARCHAR2,
                                      P_CODRETORNO      OUT VARCHAR2,
                                      P_DATOSERROR      OUT VARCHAR2)
  IS

    v_importeARepartir            number:=0;
    M_PAGOS TAB_PAGOS;

  BEGIN
       
    -- obtiene el importe a repartir
    select importeoficio
      into v_importeARepartir
      from FCS_PAGOSJG
     where idinstitucion = P_IDINSTITUCION
       and idpagosjg = P_IDPAGO;

    --si no hay dinero para repartir, mejor no seguir
    if (round(v_importeARepartir,2)<=cte) then
      P_TOTAL:=0.00;
    else
      --cargando la matriz de memoria
      M_PAGOS := FUNC_FCS_CARGA_TURNOSOFICIO(P_IDINSTITUCION,P_IDPAGO,V_CODRETORNO,V_DATOSERROR);
      CALCULA_PAGO(M_PAGOS, v_importeARepartir, P_TOTAL);

      --escribiendo en BD los pagos
      FOR I in 1..M_PAGOS.count LOOP
        if (round(M_PAGOS(I).pagoActual,2)>cte) then
           -- si el registro existe, actualiza, si no, inserta
           if (PKG_SIGA_PAGOS_SJCS.EXISTE_PAGO_COLEGIADO(P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA) = 0) then
              INSERT INTO FCS_PAGO_COLEGIADO
                          (IDINSTITUCION, IDPAGOSJG, IDPERORIGEN, IDPERDESTINO, 
                          IMPOFICIO, IMPASISTENCIA, IMPSOJ, IMPEJG,
                          IMPMOVVAR, IMPIRPF,PORCENTAJEIRPF, IMPRET,
                          FECHAMODIFICACION, USUMODIFICACION)
                   VALUES (P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA,  
                           decode(PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION),'-1',M_PAGOS(I).IDPERSONA, 
                           PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION)),   
                           M_PAGOS(I).pagoActual, 0, 0, 0,
                           0, 0, 0, 0,                          
                           SYSDATE, P_USUMODIFICACION);
           else
               UPDATE FCS_PAGO_COLEGIADO
                  SET IMPOFICIO =  IMPOFICIO + M_PAGOS(I).pagoActual,
                      FECHAMODIFICACION = SYSDATE, 
                      USUMODIFICACION = P_USUMODIFICACION
                WHERE IDINSTITUCION = P_IDINSTITUCION 
                  and IDPAGOSJG = P_IDPAGO
                  and IDPERORIGEN = M_PAGOS(I).IDPERSONA;
          end if;
        end if;
      end loop;
    end if;
    
    --salida correcta
    P_DATOSERROR := TO_CHAR('PROC_FCS_PAGO_TURNOS_OFI:Fin Correcto');
    P_CODRETORNO := TO_CHAR('0');
    
  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;
  END; --PROC_FCS_PAGO_TURNOS_OFI



  FUNCTION FUNC_FCS_CARGA_TURNOSOFICIO (P_IDINSTITUCION   IN NUMBER,
                                        P_IDPAGO          IN NUMBER,
                                        P_CODRETORNO      OUT VARCHAR2,
                                        P_DATOSERROR      OUT VARCHAR2) 
                                        return tab_pagos IS
    M_PAGOS TAB_PAGOS;
    v_idfacturacion NUMBER;
    indice number := 0;
  
    -- Obtiene el importe total facturado y el importe total pagado en pagos anteriores
    -- para un colegiado que tenga actuaciones de designas en una facturacion 
    CURSOR C_ACTUACIONES (P_IDFACTURACION NUMBER) IS
      select c.idpersona,
             sum(c.Importefacturado) totalFacturado,
             (select nvl(sum (col.impoficio), 0) 
                from fcs_pago_colegiado col, fcs_pagosjg pag
               where col.idpagosjg = pag.idpagosjg
                 and col.idinstitucion = pag.idinstitucion
                 and col.idinstitucion = c.idinstitucion
                 and pag.idfacturacion = c.idfacturacion
                 and col.idperorigen = c.idpersona
                 and pag.idpagosjg != P_IDPAGO) totalAnterior
        from FCS_FACT_ACTUACIONDESIGNA C
       where c.idfacturacion = P_IDFACTURACION
         and c.idinstitucion = P_IDINSTITUCION
      group by c.idpersona, c.idfacturacion, c.idinstitucion;

  BEGIN

      --log salida
      P_DATOSERROR := 'Antes de calcular idFacturacion';
      
      --Obtiene la facturacion correspondiente al pago
      select pag.idfacturacion
        into v_idfacturacion
        from FCS_PAGOSJG PAG
       where pag.idpagosjg = P_IDPAGO
         and pag.idinstitucion = P_IDINSTITUCION;
          
      --calculando importe facturado y pagado por cada actuacion
      FOR Elemento IN C_ACTUACIONES (v_idfacturacion) LOOP
        IF Elemento.totalFacturado>CTE AND Elemento.totalFacturado-Elemento.totalAnterior>CTE THEN
          /* Actualizo el contador de la matriz */
          indice := indice + 1;
          
          /* inserta los datos calculados en la matriz */
          M_PAGOS(indice).IDPERSONA       := Elemento.idpersona;
          M_PAGOS(indice).TOTALFACTURADO  := Elemento.totalFacturado;
          M_PAGOS(indice).TOTALANTERIOR   := Elemento.totalAnterior;
          M_PAGOS(indice).PAGOACTUAL      :=0;
        END IF;
      END LOOP;
        
    
    --salida correcta
    P_DATOSERROR := 'FUNCTION FUNC_FCS_CARGA_TURNOSOFICIO: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);
    
    return M_PAGOS;
    
  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR||' '||SQLERRM;
  END; --FUNC_FCS_CARGA_TURNOSOFICIO


  PROCEDURE PROC_FCS_PAGO_GUARDIAS       (P_IDINSTITUCION        IN VARCHAR2,
                                              P_IDPAGO               IN VARCHAR2,
                                              P_USUMODIFICACION      IN VARCHAR2,
                                              P_TOTAL                OUT VARCHAR2,
                                              P_CODRETORNO           OUT VARCHAR2,
                                              P_DATOSERROR           OUT VARCHAR2) 
  IS

    v_importeARepartir            number:=0;
    M_PAGOS TAB_PAGOS;
 

  BEGIN
     -- obtiene el importe a repartir
     select importeguardia
       into v_importeARepartir
       from FCS_PAGOSJG
      where idinstitucion = P_IDINSTITUCION
        and idpagosjg = P_IDPAGO;

      --si no hay dinero para repartir, mejor no seguir
      if (round(v_importeARepartir,2)<=cte) then
        P_TOTAL:=0.00;
      else
        --cargando la matriz de memoria
        M_PAGOS := FUNC_FCS_CARGA_GUARDIAS(P_IDINSTITUCION,P_IDPAGO,V_CODRETORNO,V_DATOSERROR);
        CALCULA_PAGO(M_PAGOS, v_importeARepartir, P_TOTAL);

        --escribiendo en BD los pagos
        FOR I in 1..M_PAGOS.count LOOP
          if (round(M_PAGOS(I).pagoActual,2)>cte) then
             -- si el registro existe, actualiza, si no, inserta
             if (PKG_SIGA_PAGOS_SJCS.EXISTE_PAGO_COLEGIADO(P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA) = 0) then
                INSERT INTO FCS_PAGO_COLEGIADO
                            (IDINSTITUCION, IDPAGOSJG, IDPERORIGEN, IDPERDESTINO, 
                            IMPOFICIO, IMPASISTENCIA, IMPSOJ, IMPEJG,
                            IMPMOVVAR, IMPIRPF, PORCENTAJEIRPF, IMPRET,
                            FECHAMODIFICACION, USUMODIFICACION)
                     VALUES (P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA,  
                             decode(PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION),'-1',M_PAGOS(I).IDPERSONA, 
                                    PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION)),   
                             0, M_PAGOS(I).pagoActual, 0, 0,
                             0, 0, 0, 0,                         
                             SYSDATE, P_USUMODIFICACION);
             else
                 UPDATE FCS_PAGO_COLEGIADO 
                    SET IMPASISTENCIA =  IMPASISTENCIA + M_PAGOS(I).pagoActual,
                        FECHAMODIFICACION = SYSDATE, 
                        USUMODIFICACION = P_USUMODIFICACION
                  WHERE IDINSTITUCION = P_IDINSTITUCION 
                    and IDPAGOSJG = P_IDPAGO
                    and IDPERORIGEN = M_PAGOS(I).IDPERSONA;
            end if;
          end if;
        end loop;
    end if;
    
    --salida correcta
    P_DATOSERROR := TO_CHAR('PROC_FCS_PAGO_GUARDIAS:Fin Correcto');
    P_CODRETORNO := TO_CHAR('0');

  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;

  END; -- Procedure PROC_FCS_PAGO_GUARDIAS

 FUNCTION FUNC_FCS_CARGA_GUARDIAS (P_IDINSTITUCION      IN NUMBER,
                                    P_IDPAGO             IN NUMBER,
                                    P_CODRETORNO         OUT VARCHAR2,
                                    P_DATOSERROR         OUT VARCHAR2) 
                                    return tab_pagos IS
    M_PAGOS TAB_PAGOS;
    v_idfacturacion NUMBER;
    indice number := 0;


    -- Obtiene el importe total facturado y el importe total pagado en pagos anteriores
    -- para un colegiado que tenga actuaciones de designas en una facturacion 
  CURSOR C_GUARDIAS (P_IDFACTURACION NUMBER) IS
       select c.idpersona,
              sum(c.precioaplicado + c.preciocostesfijos) totalFacturado,
              (select  nvl(sum(col.impasistencia),0) 
                from fcs_pago_colegiado col, fcs_pagosjg pag                
                where col.idpagosjg = pag.idpagosjg
                  and col.idinstitucion = pag.idinstitucion
                  and col.idinstitucion = c.idinstitucion
                  and pag.idfacturacion = c.idfacturacion
                  and col.idperorigen = c.idpersona
                  and pag.idpagosjg != P_IDPAGO) totalAnterior
         from FCS_FACT_APUNTE c
        where c.idfacturacion = P_IDFACTURACION
          and c.idinstitucion = P_IDINSTITUCION
      group by c.idpersona, c.idfacturacion, c.idinstitucion;


  BEGIN

      --log salida
      P_DATOSERROR := 'Antes de calcular idFacturacion';
      
      --Obtiene la facturacion correspondiente al pago
      select pag.idfacturacion
        into v_idfacturacion
        from FCS_PAGOSJG PAG
       where pag.idpagosjg = P_IDPAGO
         and pag.idinstitucion = P_IDINSTITUCION;
                   
      --calculando importe facturado y pagado por cada actuacion
      FOR Elemento IN C_GUARDIAS (v_idfacturacion) LOOP
        IF Elemento.totalFacturado>CTE AND Elemento.totalFacturado-Elemento.totalAnterior>CTE THEN
          /* Actualizo el contador de la matriz */
          indice := indice + 1;
          
          /* inserta los datos calculados en la matriz */
          M_PAGOS(indice).IDPERSONA       := Elemento.idpersona;
          M_PAGOS(indice).TOTALFACTURADO  := Elemento.totalFacturado;
          M_PAGOS(indice).TOTALANTERIOR   := Elemento.totalAnterior;
          M_PAGOS(indice).PAGOACTUAL      :=0;
        END IF;
      END LOOP;
        
    
    --salida correcta
    P_DATOSERROR := 'FUNCTION FUNC_FCS_CARGA_GUARDIAS: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);
    
    return M_PAGOS;

    EXCEPTION
    WHEN OTHERS THEN
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := P_DATOSERROR||' '||SQLERRM;
  END; --BEGIN FUNC_FCS_CARGA_GUARDIAS


  PROCEDURE PROC_FCS_PAGO_SOJ (P_IDINSTITUCION   IN VARCHAR2,
                                      P_IDPAGO          IN VARCHAR2,
                                      P_USUMODIFICACION IN VARCHAR2,
                                      P_TOTAL           OUT VARCHAR2,
                                      P_CODRETORNO      OUT VARCHAR2,
                                      P_DATOSERROR      OUT VARCHAR2)
  IS

    v_importeARepartir            number:=0;
    M_PAGOS TAB_PAGOS;

  BEGIN
       
    -- obtiene el importe a repartir
    select importesoj
      into v_importeARepartir
      from FCS_PAGOSJG
     where idinstitucion = P_IDINSTITUCION
       and idpagosjg = P_IDPAGO;

    --si no hay dinero para repartir, mejor no seguir
    if (round(v_importeARepartir,2)<=cte) then
      P_TOTAL:=0.00;
    else
      --cargando la matriz de memoria
      M_PAGOS := FUNC_FCS_CARGA_SOJ(P_IDINSTITUCION,P_IDPAGO,V_CODRETORNO,V_DATOSERROR);
      CALCULA_PAGO(M_PAGOS, v_importeARepartir, P_TOTAL);

      --escribiendo en BD los pagos
      FOR I in 1..M_PAGOS.count LOOP
        if (round(M_PAGOS(I).pagoActual,2)>cte) then
           -- si el registro existe, actualiza, si no, inserta
           if (PKG_SIGA_PAGOS_SJCS.EXISTE_PAGO_COLEGIADO(P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA) = 0) then
              INSERT INTO FCS_PAGO_COLEGIADO
                          (IDINSTITUCION, IDPAGOSJG, IDPERORIGEN, IDPERDESTINO, 
                          IMPOFICIO, IMPASISTENCIA, IMPSOJ, IMPEJG,
                          IMPMOVVAR, IMPIRPF, PORCENTAJEIRPF, IMPRET,
                          FECHAMODIFICACION, USUMODIFICACION)
                   VALUES (P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA,  
                           decode(PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION),'-1',M_PAGOS(I).IDPERSONA, 
                                  PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION)),   
                           0, 0, M_PAGOS(I).pagoActual, 0,
                           0, 0, 0, 0,                         
                           SYSDATE, P_USUMODIFICACION);
           else
               UPDATE FCS_PAGO_COLEGIADO 
                  SET IMPSOJ =  IMPSOJ + M_PAGOS(I).pagoActual,
                      FECHAMODIFICACION = SYSDATE, 
                      USUMODIFICACION = P_USUMODIFICACION
                WHERE IDINSTITUCION = P_IDINSTITUCION 
                  and IDPAGOSJG = P_IDPAGO
                  and IDPERORIGEN = M_PAGOS(I).IDPERSONA;
          end if;
        end if;
      end loop;
    end if;
    
    --salida correcta
    P_DATOSERROR := TO_CHAR('PROC_FCS_PAGO_TURNOS_OFI:Fin Correcto');
    P_CODRETORNO := TO_CHAR('0');
    
  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;
  END; --PROC_FCS_PAGO_SOJ


  FUNCTION FUNC_FCS_CARGA_SOJ (P_IDINSTITUCION   IN NUMBER,
                                        P_IDPAGO          IN NUMBER,
                                        P_CODRETORNO      OUT VARCHAR2,
                                        P_DATOSERROR      OUT VARCHAR2) 
                                        return tab_pagos IS
    M_PAGOS TAB_PAGOS;
    v_idfacturacion NUMBER;
    indice number := 0;
  
    -- Obtiene el importe total facturado y el importe total pagado en pagos anteriores
    -- para un colegiado que tenga actuaciones de designas en una facturacion 
    CURSOR C_SOJ (P_IDFACTURACION NUMBER) IS
      select c.idpersona,
             sum(c.precioaplicado) totalFacturado,
             (select nvl(sum (col.impsoj), 0) 
                from fcs_pago_colegiado col, fcs_pagosjg pag
               where col.idpagosjg = pag.idpagosjg
                 and col.idinstitucion = pag.idinstitucion
                 and col.idinstitucion = c.idinstitucion
                 and pag.idfacturacion = c.idfacturacion
                 and col.idperorigen = c.idpersona
                 and pag.idpagosjg != P_IDPAGO) totalAnterior
        from FCS_FACT_SOJ C
       where c.idfacturacion = P_IDFACTURACION
         and c.idinstitucion = P_IDINSTITUCION
      group by c.idpersona, c.idfacturacion, c.idinstitucion;

  BEGIN

      --log salida
      P_DATOSERROR := 'Antes de calcular idFacturacion';
      
      --Obtiene la facturacion correspondiente al pago
      select pag.idfacturacion
        into v_idfacturacion
        from FCS_PAGOSJG PAG
       where pag.idpagosjg = P_IDPAGO
         and pag.idinstitucion = P_IDINSTITUCION;
          
      --calculando importe facturado y pagado por cada actuacion
      FOR Elemento IN C_SOJ (v_idfacturacion) LOOP
        IF Elemento.totalFacturado>CTE AND Elemento.totalFacturado-Elemento.totalAnterior>CTE THEN
          /* Actualizo el contador de la matriz */
          indice := indice + 1;
          
          /* inserta los datos calculados en la matriz */
          M_PAGOS(indice).IDPERSONA       := Elemento.idpersona;
          M_PAGOS(indice).TOTALFACTURADO  := Elemento.totalFacturado;
          M_PAGOS(indice).TOTALANTERIOR   := Elemento.totalAnterior;
          M_PAGOS(indice).PAGOACTUAL      :=0;
        END IF;
      END LOOP;
        
    
    --salida correcta
    P_DATOSERROR := 'FUNCTION FUNC_FCS_CARGA_SOJ: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);
    
    return M_PAGOS;
    
  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR||' '||SQLERRM;
  END; --FUNC_FCS_CARGA_SOJ


  PROCEDURE PROC_FCS_PAGO_EJG (P_IDINSTITUCION   IN VARCHAR2,
                                      P_IDPAGO          IN VARCHAR2,
                                      P_USUMODIFICACION IN VARCHAR2,
                                      P_TOTAL           OUT VARCHAR2,
                                      P_CODRETORNO      OUT VARCHAR2,
                                      P_DATOSERROR      OUT VARCHAR2)
  IS

    v_importeARepartir            number:=0;
    M_PAGOS TAB_PAGOS;

  BEGIN
       
    -- obtiene el importe a repartir
    select importeejg
      into v_importeARepartir
      from FCS_PAGOSJG
     where idinstitucion = P_IDINSTITUCION
       and idpagosjg = P_IDPAGO;

    --si no hay dinero para repartir, mejor no seguir
    if (round(v_importeARepartir,2)<=cte) then
      P_TOTAL:=0.00;
    else
      --cargando la matriz de memoria
      M_PAGOS := FUNC_FCS_CARGA_EJG(P_IDINSTITUCION,P_IDPAGO,V_CODRETORNO,V_DATOSERROR);
      CALCULA_PAGO(M_PAGOS, v_importeARepartir, P_TOTAL);

      --escribiendo en BD los pagos
      FOR I in 1..M_PAGOS.count LOOP
        if (round(M_PAGOS(I).pagoActual,2)>cte) then
           -- si el registro existe, actualiza, si no, inserta
           if (PKG_SIGA_PAGOS_SJCS.EXISTE_PAGO_COLEGIADO(P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA) = 0) then
              INSERT INTO FCS_PAGO_COLEGIADO
                          (IDINSTITUCION, IDPAGOSJG, IDPERORIGEN, IDPERDESTINO, 
                          IMPOFICIO, IMPASISTENCIA, IMPSOJ, IMPEJG,
                          IMPMOVVAR, IMPIRPF,PORCENTAJEIRPF, IMPRET,
                          FECHAMODIFICACION, USUMODIFICACION)
                   VALUES (P_IDINSTITUCION, P_IDPAGO, M_PAGOS(I).IDPERSONA,  
                           decode(PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION),'-1',M_PAGOS(I).IDPERSONA, 
                                  PKG_SIGA_PAGOS_SJCS.FUN_FCS_OBTENERIDPERSONASOCIED(M_PAGOS(I).IDPERSONA, P_IDINSTITUCION)),   
                           0, 0, 0, M_PAGOS(I).pagoActual,
                           0, 0, 0, 0,                          
                           SYSDATE, P_USUMODIFICACION);
           else
               UPDATE FCS_PAGO_COLEGIADO 
                  SET IMPEJG =  IMPEJG + M_PAGOS(I).pagoActual,
                      FECHAMODIFICACION = SYSDATE, 
                      USUMODIFICACION = P_USUMODIFICACION
                WHERE IDINSTITUCION = P_IDINSTITUCION 
                  and IDPAGOSJG = P_IDPAGO
                  and IDPERORIGEN = M_PAGOS(I).IDPERSONA;
          end if;
        end if;
      end loop;
    end if;
    
    --salida correcta
    P_DATOSERROR := TO_CHAR('PROC_FCS_PAGO_TURNOS_OFI:Fin Correcto');
    P_CODRETORNO := TO_CHAR('0');
    
  EXCEPTION
    WHEN OTHERS THEN
      P_TOTAL := 0;
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := SQLERRM;
  END; --PROC_FCS_PAGO_EJG


  FUNCTION FUNC_FCS_CARGA_EJG (P_IDINSTITUCION   IN NUMBER,
                                        P_IDPAGO          IN NUMBER,
                                        P_CODRETORNO      OUT VARCHAR2,
                                        P_DATOSERROR      OUT VARCHAR2) 
                                        return tab_pagos IS
    M_PAGOS TAB_PAGOS;
    v_idfacturacion NUMBER;
    indice number := 0;
  
    -- Obtiene el importe total facturado y el importe total pagado en pagos anteriores
    -- para un colegiado que tenga actuaciones de designas en una facturacion 
    CURSOR C_EJG (P_IDFACTURACION NUMBER) IS
      select c.idpersona,
             sum(c.precioaplicado) totalFacturado,
             (select nvl(sum (col.impejg), 0) 
                from fcs_pago_colegiado col, fcs_pagosjg pag
               where col.idpagosjg = pag.idpagosjg
                 and col.idinstitucion = pag.idinstitucion
                 and col.idinstitucion = c.idinstitucion
                 and pag.idfacturacion = c.idfacturacion
                 and col.idperorigen = c.idpersona
                 and pag.idpagosjg != P_IDPAGO) totalAnterior
        from FCS_FACT_EJG C
       where c.idfacturacion = P_IDFACTURACION
         and c.idinstitucion = P_IDINSTITUCION
      group by c.idpersona, c.idfacturacion, c.idinstitucion;

  BEGIN

      --log salida
      P_DATOSERROR := 'Antes de calcular idFacturacion';
      
      --Obtiene la facturacion correspondiente al pago
      select pag.idfacturacion
        into v_idfacturacion
        from FCS_PAGOSJG PAG
       where pag.idpagosjg = P_IDPAGO
         and pag.idinstitucion = P_IDINSTITUCION;
          
      --calculando importe facturado y pagado por cada actuacion
      FOR Elemento IN C_EJG (v_idfacturacion) LOOP
        IF Elemento.totalFacturado>CTE AND Elemento.totalFacturado-Elemento.totalAnterior>CTE THEN
          /* Actualizo el contador de la matriz */
          indice := indice + 1;
          
          /* inserta los datos calculados en la matriz */
          M_PAGOS(indice).IDPERSONA       := Elemento.idpersona;
          M_PAGOS(indice).TOTALFACTURADO  := Elemento.totalFacturado;
          M_PAGOS(indice).TOTALANTERIOR   := Elemento.totalAnterior;
          M_PAGOS(indice).PAGOACTUAL      :=0;
        END IF;
      END LOOP;
        
    
    --salida correcta
    P_DATOSERROR := 'FUNCTION FUNC_FCS_CARGA_SOJ: ha finalizado correctamente.';
    P_CODRETORNO := TO_CHAR(0);
    
    return M_PAGOS;
    
  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := TO_CHAR(SQLCODE);
      P_DATOSERROR := P_DATOSERROR||' '||SQLERRM;
  END; --FUNC_FCS_CARGA_EJG




  /**
   Calcula el campo pagoActual de la matriz
  **/
  PROCEDURE CALCULA_PAGO(M_PAGOS IN OUT TAB_PAGOS, 
                         v_importeARepartir IN OUT number, 
                         total OUT number) is
                         
    v_importeTotalFacturado       number:=0;
    v_importeTotalPagoAnterior    number:=0;
    v_importeTotalPagoActual      number:=0;
    v_importeTotalPagoPendiente   number:=0;
    v_porcentajeAplicar           number:=0;
    v_importePagoPendiente        number:=0; -- importe del pago pendiente por actuacion
    v_importePagoParcialActual    number:=0; -- importe del pago parcial actual por actuacion
      
    begin
      --calculando importes totales: Facturado, Pagado y Pendiente
      FOR I in 1..M_PAGOS.count LOOP
        v_importeTotalFacturado := v_importeTotalFacturado+M_PAGOS(I).totalFacturado;
        v_importeTotalPagoAnterior:=v_importeTotalPagoAnterior+M_PAGOS(I).totalAnterior;
      end loop;
      v_importeTotalPagoPendiente := v_importeTotalFacturado-v_importeTotalPagoAnterior;
      
     
      if (round(v_importeTotalPagoPendiente,2)>cte) then
        --calcula el nuevo porcentaje a aplicar sobre el importe pendiente
        v_porcentajeAplicar := v_importeARepartir/v_importeTotalPagoPendiente;
        -- ESTO YA NO DEBERIA SER NECESARIO PORQUE SE CONTROLA AL INTRODUCIR LOS DATOS DE UN PAGO
        -- Comprueba que el porcentaje a aplicar no supere el 100%
        if (v_porcentajeAplicar > 1) then
          v_porcentajeAplicar:=1;
        end if;
        
        -- Para cada colegiado calcula el importe del pago actual
        FOR J in 1..M_PAGOS.count-1 LOOP
          -- obtiene el importe pendiente por pagar para un colegiado
          v_importePagoPendiente := M_PAGOS(J).totalFacturado - M_PAGOS(J).totalAnterior;
          -- obtiene el importe a pagar sobre el importe pendiente 
          v_importePagoParcialActual := v_importePagoPendiente*v_porcentajeAplicar;
          
          -- Comprueba si el importe a repartir restante supera al importe a pagar 
          -- Si no, sólo se paga lo que quede 
          if (v_importeARepartir < v_importePagoParcialActual) then
             v_importePagoParcialActual := v_importeARepartir;
          end if;

          -- Actualiza la matriz de memoria
          M_PAGOS(J).pagoActual := v_importePagoParcialActual;
          -- Descuenta el dinero a repartir despues de haber hecho la actualizacion
          v_importeARepartir := v_importeARepartir - M_PAGOS(J).pagoActual;
          v_importeTotalPagoActual := v_importeTotalPagoActual + round(v_importePagoParcialActual,2);
          v_importeTotalPagoPendiente := v_importeTotalPagoPendiente - v_importePagoParcialActual;
/*          dbms_output.put_line(M_PAGOS(J).idPersona || '  ' ||
                               M_PAGOS(J).totalFacturado || '  ' ||
                               M_PAGOS(J).totalAnterior || '  ' ||
                               M_PAGOS(J).pagoActual);*/
        END LOOP;
        
        -- Para la última guardia se paga lo que quede 
        M_PAGOS(M_PAGOS.count).pagoActual := v_importeARepartir;
        -- Actualiza el importe del total pagado
        v_importeTotalPagoActual := v_importeTotalPagoActual + round(v_importeTotalPagoPendiente,2);
/*         dbms_output.put_line(M_PAGOS(M_PAGOS.count).idPersona || '  ' ||
                               M_PAGOS(M_PAGOS.count).totalFacturado || '  ' ||
                               M_PAGOS(M_PAGOS.count).totalAnterior || '  ' ||
                               M_PAGOS(M_PAGOS.count).pagoActual || '  ' ||
                               v_importeARepartir || '  ' ||                               
                               v_importeTotalPagoActual);*/
        -- actualiza la variable de salida de la funcion
        TOTAL := v_importeTotalPagoActual;                               
        end if;
        
  end;

  /****************************************************************************************************************/
  /* Nombre:        FUNC_PAGOS_INTERVALO                                                                          */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 27/04/2011                                                                                   */
  /* Autor:         Carlos Ruano Martínez                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
  Function Func_Pagos_Intervalo(p_Idinstitucion  Number,
                                        p_Idpago1 Number,
                                        p_Idpago2 Number)
    Return Varchar2 Is
  
    Cursor c_Pagos(p_Fecha_Ini Fcs_Pagosjg.Fechadesde%Type, p_Fecha_Fin Fcs_Pagosjg.Fechadesde%Type) Is
      Select Pag.Idpagosjg, Pag.Idfacturacion
        From Fcs_Pagosjg Pag
       Where Pag.Idinstitucion = p_Idinstitucion
         And Pag.Fechadesde >= p_Fecha_Ini
         And Pag.Fechahasta <= p_Fecha_Fin
       Order By Pag.Fechadesde Asc;
  
    Fecha_1   Fcs_Pagosjg.Fechadesde%Type;
    Fecha_2   Fcs_Pagosjg.Fechadesde%Type;
    Fecha_3   Fcs_Pagosjg.Fechadesde%Type;
    Fecha_4   Fcs_Pagosjg.Fechadesde%Type;
    Fecha_Ini Fcs_Pagosjg.Fechadesde%Type;
    Fecha_Fin Fcs_Pagosjg.Fechadesde%Type;
    Rc        Varchar2(4000);
    
    v_Consejo cen_institucion.Cen_Inst_Idinstitucion%Type;
    v_Idgrupofacturacion Fcs_Fact_Grupofact_Hito.Idgrupofacturacion%Type;
  
  Begin
  
    -- Sacando solo un pago si solo se selecciona uno
    If (p_Idpago2 Is Null) Then
      Return p_Idpago1;
    Elsif (p_Idpago1 = p_Idpago2) Then
      Return p_Idpago1;
    End If;

    -- Obtenemos las fechas del primer pago
    Select Pag.Fechadesde, Pag.Fechahasta
      Into Fecha_1, Fecha_2
      From Fcs_Pagosjg Pag
     Where Idinstitucion = p_Idinstitucion
       And Idpagosjg = p_Idpago1;
    -- Obtenemos las fechas del segundo pago
    Select Pag.Fechadesde, Pag.Fechahasta
      Into Fecha_3, Fecha_4
      From Fcs_Pagosjg Pag
     Where Idinstitucion = p_Idinstitucion
       And Idpagosjg = p_Idpago2;
  
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
    
    Select Cen_Inst_Idinstitucion
      Into v_Consejo
      From Cen_Institucion
     Where Idinstitucion = p_Idinstitucion;
  
    For v_Pago In c_Pagos(Fecha_Ini, Fecha_Fin) Loop
      If (v_Consejo = 3001) Then --Para los catalanes se limita que los pagos sean solo de lo seleccionado (Guardia, Turno, etc)
        Select Idgrupofacturacion
          Into v_Idgrupofacturacion
          From Fcs_Fact_Grupofact_Hito
         Where Idinstitucion = p_Idinstitucion
           And Idfacturacion = v_Pago.Idfacturacion
         Group By Idgrupofacturacion;
        
        If (v_Idgrupofacturacion = 1) Then
          Rc := Rc || ', ' || v_Pago.IdPagosjg;
        End If;
      Else
        Rc := Rc || ', ' || v_Pago.IdPagosjg;
      End If;
    End Loop;
  
    Rc := Ltrim(Rc, ',');
    Return(Rc);
  
  Exception
    When No_Data_Found Then
      Return(1);
    When Others Then
      Return(-1);
    
  End Func_Pagos_Intervalo;

 /****************************************************************************************************************/
  /* Nombre:        FUNC_PAGOS_INTERVALO_GRUPOFACT                                                                         */
  /* Descripcion:   Funcion que obtiene un nuevo idpaunte para la tabla de apuntes CG                             */
  /*                                                                                                              */
  /* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
  /* -------------------   ------   ------------------------------------------------------------   -------------  */
  /*                                                                                                              */
  /* Version:        1.0                                                                                          */
  /* Fecha Creacion: 13/07/2011                                                                                   */
  /* Autor:         Carlos Ruano Martínez                                                                         */
  /* Fecha Modificacion Autor Modificacion                Descripcion Modificacion                                */
  /* ------------------ --------------------------------- ------------------------------------------------------- */
  /****************************************************************************************************************/
 FUNCTION Func_Pagos_Intervalo_GrupoFact(p_Idinstitucion NUMBER,
                                          p_Idpago1       NUMBER,
                                          p_Idpago2       NUMBER,
                                          p_GrupoFact     NUMBER)
    RETURN VARCHAR2 IS
  
    CURSOR c_Pagos(p_Fecha_Ini Fcs_Pagos_Estadospagos.Fechaestado%TYPE, p_Fecha_Fin Fcs_Pagos_Estadospagos.Fechaestado%TYPE) IS
      SELECT pag.Idpagosjg,gru.idgrupofacturacion
        FROM Fcs_Pagos_Estadospagos e, fcs_pagosjg pag, fcs_fact_grupofact_hito gru
       WHERE e.Idpagosjg = pag.Idpagosjg
         AND pag.Idinstitucion = e.Idinstitucion
         and pag.Idinstitucion = gru.Idinstitucion
         AND pag.Idfacturacion = gru.Idfacturacion
         
         AND e.Idinstitucion = p_Idinstitucion
          AND e.idestadopagosjg = 30
         AND e.Fechaestado >= p_Fecha_Ini
         AND e.Fechaestado <= p_Fecha_Fin
       ORDER BY e.Fechaestado ASC;
  
    Fecha_e1   Fcs_Pagos_Estadospagos.Fechaestado%TYPE;
    Fecha_e2   Fcs_Pagos_Estadospagos.Fechaestado%TYPE;
    Fecha_Ini  Fcs_Pagos_Estadospagos.Fechaestado%TYPE;
    Fecha_Fin  Fcs_Pagos_Estadospagos.Fechaestado%TYPE;
    Rc         VARCHAR2(4000);
    
    idpagosjg_anterior fcs_pagosjg.Idpagosjg%Type;
  
  BEGIN
  
    -- Sacando solo un pago si solo se selecciona uno
    IF (p_Idpago2 IS NULL) THEN
      RETURN p_Idpago1;
    ELSIF (p_Idpago1 = p_Idpago2) THEN
      RETURN p_Idpago1;
    END IF;
  
    -- Se obtiene la fecha de estado del pirmer idpago
    SELECT e.fechaestado
      INTO Fecha_e1
      FROM fcs_pagos_estadospagos e
     WHERE Idinstitucion = p_Idinstitucion
       AND Idpagosjg = p_Idpago1
       AND idestadopagosjg = 30;
       
    -- Se obtiene la fecha de estado del segundo idpago
    SELECT e.fechaestado
      INTO Fecha_e2
      FROM fcs_pagos_estadospagos e
     WHERE Idinstitucion = p_Idinstitucion
       AND Idpagosjg = p_Idpago2
       AND idestadopagosjg = 30;
  
    -- La primera fechadesde marca el inicio del intervalo
    IF (Fecha_e1 < Fecha_e2) THEN
      Fecha_Ini := Fecha_e1;
      Fecha_Fin := Fecha_e2;
    ELSE
      Fecha_Ini := Fecha_e2;
      Fecha_Fin := Fecha_e1;
    END IF;
  
    idpagosjg_anterior := -1;
    FOR v_Pago IN c_Pagos(Fecha_Ini, Fecha_Fin) LOOP
      IF (p_GrupoFact = -1 Or v_Pago.Idgrupofacturacion = p_GrupoFact) THEN
        If (v_Pago.Idpagosjg <> idpagosjg_anterior) Then
          -- Control para no duplicar el pago
          Rc := Rc || ', ' || v_Pago.IdPagosjg;
          idpagosjg_anterior := v_Pago.IdPagosjg;
        End If;
      END IF;
    END LOOP;
  
    Rc := Ltrim(Rc, ',');
    RETURN(Rc);
  
  EXCEPTION
    WHEN No_Data_Found THEN
      RETURN(1);
    WHEN OTHERS THEN
      RETURN(-1);
    
  END Func_Pagos_Intervalo_GrupoFact;

END PKG_SIGA_PAGOS_SJCS;
/
