create or replace package PKG_SIGA_REGULARIZACION_SJCS is

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_TURNOS_OFI
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDFACTURACION     NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2006-03-21 - Pilar: creacion
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_TURNOS_OFI (P_IDINSTITUCION    IN NUMBER,
                                         P_IDFACTURACION    IN NUMBER,
                                         P_USUMODIFICACION  IN NUMBER,
                                         P_IDIOMA           IN NUMBER,
                                         P_TOTAL            OUT VARCHAR2,
                                         P_CODRETORNO       OUT VARCHAR2,
                                         P_DATOSERROR       OUT VARCHAR2);

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_EJG
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDFACTURACION     NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2006-03-21 - Pilar: creacion
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_EJG (P_IDINSTITUCION    IN NUMBER,
                                  P_IDFACTURACION    IN NUMBER,
                                  P_USUMODIFICACION  IN NUMBER,
                                  P_IDIOMA           IN NUMBER,
                                  P_TOTAL            OUT VARCHAR2,
                                  P_CODRETORNO       OUT VARCHAR2,
                                  P_DATOSERROR       OUT VARCHAR2);

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_SOJ
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDFACTURACION     NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2006-03-21 - Pilar: creacion
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_SOJ (P_IDINSTITUCION    IN NUMBER,
                                  P_IDFACTURACION    IN NUMBER,
                                  P_USUMODIFICACION  IN NUMBER,
                                  P_IDIOMA           IN NUMBER,
                                  P_TOTAL            OUT VARCHAR2,
                                  P_CODRETORNO       OUT VARCHAR2,
                                  P_DATOSERROR       OUT VARCHAR2);

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_GUARDIAS
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDREGULARIZACION  NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2009-09-21 - Adrian: cambio total
  --
  -----------------------------------------------------------------------------
  FUNCTION FUNC_CALC_REGULARIZ_GUARDIAS (P_IDINSTITUCION     IN NUMBER,
                                         P_IDREGULARIZACION  IN NUMBER,
                                         P_IDAPUNTE          IN NUMBER,
                                         P_FECHAFIN          IN DATE,
                                         P_ANIO              IN NUMBER,
                                         P_NUMERO            IN NUMBER,
                                         P_IDACTUACION       IN Number)
    return NUMBER;
  PROCEDURE PROC_FCS_REGULAR_GUARDIAS (P_IDINSTITUCION     IN NUMBER,
                                       P_IDREGULARIZACION  IN NUMBER,
                                       P_USUMODIFICACION   IN NUMBER,
                                       P_IDIOMA            IN NUMBER,
                                       P_TOTAL             OUT VARCHAR2,
                                       P_CODRETORNO        OUT VARCHAR2,
                                       P_DATOSERROR        OUT VARCHAR2);

end PKG_SIGA_REGULARIZACION_SJCS;
/
create or replace package body PKG_SIGA_REGULARIZACION_SJCS is

  /* Variables */
  --Variables de gestion de errores Oracle
  V_CODRETORNO2 VARCHAR2(10)   := TO_CHAR(0);
  V_DATOSERROR2 VARCHAR2(2000) := NULL;


  /* Constantes */
  HITO_EJG constant number:=13;
  HITO_SOJ constant number:=12;


  /* Declaracion de excepciones */
  E_ERROR2 EXCEPTION; -- ERROR GENERAL


  /* Implementacion de procedimientos y funciones */

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_TURNOS_OFI
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDFACTURACION     NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2006-03-21 - Pilar: creacion
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_TURNOS_OFI (P_IDINSTITUCION    IN NUMBER,
                                         P_IDFACTURACION    IN NUMBER,
                                         P_USUMODIFICACION  IN NUMBER,
                                         P_IDIOMA           IN NUMBER,
                                         P_TOTAL            OUT VARCHAR2,
                                         P_CODRETORNO       OUT VARCHAR2,
                                         P_DATOSERROR       OUT VARCHAR2) IS
      -- VARIABLES
    V_PRECIO                  NUMBER := 0;
    V_DIFERENCIAPRECIO        NUMBER := 0;
    V_PORCENTAJEFACTURADO     NUMBER := 0;
    V_IDFACTURACIONREGULARIZACION NUMBER;
    V_PUNTOS       NUMBER := 0;
    V_TOTAL        NUMBER := 0; /* variable para sumar */

  BEGIN

    -- trazas
    V_DATOSERROR2 := 'Antes de ejecutar turnos';

    DECLARE
      /* Declaracon de cursores */
      CURSOR C_TURNOS (P_IDFACTURACIONREGULARIZACION number)IS
        SELECT FCS_FACT_ACTUACIONDESIGNA.IDINSTITUCION,
               FCS_FACT_ACTUACIONDESIGNA.IDFACTURACION,
               FCS_FACT_ACTUACIONDESIGNA.NUMEROASUNTO,
               FCS_FACT_ACTUACIONDESIGNA.NUMERO,
               FCS_FACT_ACTUACIONDESIGNA.ANIO,
               FCS_FACT_ACTUACIONDESIGNA.IDTURNO,
               FCS_FACT_ACTUACIONDESIGNA.IDPERSONA,
               FCS_FACT_ACTUACIONDESIGNA.PROCEDIMIENTO,
               FCS_FACT_ACTUACIONDESIGNA.PRECIOAPLICADO,
               FCS_FACT_ACTUACIONDESIGNA.FECHAACTUACION,
               FCS_FACT_ACTUACIONDESIGNA.FECHAJUSTIFICACION,
               FCS_FACT_ACTUACIONDESIGNA.PORCENTAJEFACTURADO,
               FCS_FACT_ACTUACIONDESIGNA.CODIGOPROCEDIMIENTO,
               FCS_FACT_ACTUACIONDESIGNA.ACREDITACION,
               SCS_ACTUACIONDESIGNA.IDINSTITUCION_PROC,
               SCS_ACTUACIONDESIGNA.IDPROCEDIMIENTO

          FROM FCS_FACT_ACTUACIONDESIGNA,SCS_ACTUACIONDESIGNA

         WHERE FCS_FACT_ACTUACIONDESIGNA.IDINSTITUCION = P_IDINSTITUCION AND
               FCS_FACT_ACTUACIONDESIGNA.IDFACTURACION = P_IDFACTURACIONREGULARIZACION and
               FCS_FACT_ACTUACIONDESIGNA.IDINSTITUCION=SCS_ACTUACIONDESIGNA.IDINSTITUCION and
               FCS_FACT_ACTUACIONDESIGNA.IDTURNO=SCS_ACTUACIONDESIGNA.IDTURNO and
               FCS_FACT_ACTUACIONDESIGNA.ANIO=SCS_ACTUACIONDESIGNA.ANIO and
               FCS_FACT_ACTUACIONDESIGNA.NUMERO=SCS_ACTUACIONDESIGNA.NUMERO and
               FCS_FACT_ACTUACIONDESIGNA.NUMEROASUNTO=SCS_ACTUACIONDESIGNA.NUMEROASUNTO;


    BEGIN
      -- Obtenemos el id_facturacion_regulariza
      BEGIN
          SELECT fcs_facturacionjg.idfacturacion_regulariza INTO V_IDFACTURACIONREGULARIZACION
          FROM fcs_facturacionjg
          WHERE fcs_facturacionjg.idinstitucion=P_IDINSTITUCION
            and fcs_facturacionjg.idfacturacion=P_IDFACTURACION;
      EXCEPTION
         WHEN OTHERS THEN
           V_DATOSERROR2 := 'Error al obtener id_facturacion_regulariza '||sqlerrm;
           V_CODRETORNO2 :=sqlcode;
           RAISE E_ERROR2;
      END;
      
      V_DATOSERROR2 := 'Antes de guardar historico';
      --Almacenamos en los historicos
      PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_TURNOS(P_IDINSTITUCION,
                                          P_IDFACTURACION,
                                          V_CODRETORNO2,
                                          V_DATOSERROR2);


      -- trazas
      V_DATOSERROR2 := 'Antes de ejecutar turnos';

      /* Grupos de facturacion */
      FOR V_TURNOS IN C_TURNOS (V_IDFACTURACIONREGULARIZACION) LOOP

               -- trazas
        V_DATOSERROR2 := 'Antes de ejecutar precios';

       BEGIN

        SELECT PRECIO INTO V_PRECIO
        FROM SCS_PROCEDIMIENTOS
        WHERE IDINSTITUCION = V_TURNOS.IDINSTITUCION_PROC AND
              IDPROCEDIMIENTO = V_TURNOS.IDPROCEDIMIENTO;
       EXCEPTION
        WHEN NO_DATA_FOUND THEN
          V_PRECIO:=0;
          V_PUNTOS:=0;
       END;

        -- trazas
        V_DATOSERROR2 := 'Despues de ejecutar precios';


        -- Regularizamos el precio
        V_DIFERENCIAPRECIO    := V_PRECIO - V_TURNOS.PRECIOAPLICADO;

        V_PORCENTAJEFACTURADO := V_TURNOS.PORCENTAJEFACTURADO;

        -- ACUMULAMOS EL TOTAL DE PRECIOS Y DE PUNTOS
        V_TOTAL  := V_TOTAL + round(V_DIFERENCIAPRECIO * V_TURNOS.PORCENTAJEFACTURADO / 100, 2);

        -- INSERTAR EL APUNTE
        INSERT INTO FCS_FACT_ACTUACIONDESIGNA
          (IDINSTITUCION,
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
           CODIGOPROCEDIMIENTO,
           IMPORTEFACTURADO)
        VALUES
          (P_IDINSTITUCION,
           P_IDFACTURACION,
           V_TURNOS.NUMEROASUNTO,
           V_TURNOS.NUMERO,
           V_TURNOS.ANIO,
           V_TURNOS.IDTURNO,
           V_TURNOS.IDPERSONA,
           V_TURNOS.PROCEDIMIENTO,
           V_TURNOS.ACREDITACION,
           V_TURNOS.FECHAACTUACION,
           V_TURNOS.FECHAJUSTIFICACION,
           V_DIFERENCIAPRECIO,
           V_TURNOS.PORCENTAJEFACTURADO,
           SYSDATE,
           P_USUMODIFICACION,
           V_TURNOS.CODIGOPROCEDIMIENTO,
           round(V_DIFERENCIAPRECIO * V_TURNOS.PORCENTAJEFACTURADO / 100, 2));




      END LOOP; /* PARA TURNOS */

      -- trazas
      V_DATOSERROR2 := 'Fin correcto';

      P_CODRETORNO := V_CODRETORNO2;
      P_DATOSERROR := V_DATOSERROR2;
      P_TOTAL      := TO_CHAR(V_TOTAL);



    EXCEPTION
      WHEN E_ERROR2 THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
      WHEN OTHERS THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';

    END;
  END PROC_FCS_REGULAR_TURNOS_OFI;

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_EJG
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDFACTURACION     NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2006-03-21 - Pilar: creacion
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_EJG (P_IDINSTITUCION    IN NUMBER,
                                  P_IDFACTURACION    IN NUMBER,
                                  P_USUMODIFICACION  IN NUMBER,
                                  P_IDIOMA           IN NUMBER,
                                  P_TOTAL            OUT VARCHAR2,
                                  P_CODRETORNO       OUT VARCHAR2,
                                  P_DATOSERROR       OUT VARCHAR2) IS
    -- VARIABLES
    V_PRECIOHITO            NUMBER := 0;
    V_TOTAL                 NUMBER := 0; /* variable para sumar */
    V_DIFERENCIAPRECIO      NUMBER := 0;
    V_IDFACTURACIONREGULARIZACION NUMBER;

  BEGIN


    -- trazas
    V_DATOSERROR2 := 'Antes de ejecutar guardias';

    DECLARE
      /* Declaracon de cursores */
      CURSOR C_EJG (P_IDFACTURACIONREGULARIZACION number)IS
        SELECT IDINSTITUCION,
               IDFACTURACION,
               IDTIPOEJG,
               ANIO,
               NUMERO,
               IDPERSONA,
               IDTURNO,
               IDGUARDIA,
               FECHAAPERTURA,
               PRECIOAPLICADO
          FROM FCS_FACT_EJG
         WHERE FCS_FACT_EJG.IDINSTITUCION = P_IDINSTITUCION AND
               FCS_FACT_EJG.IDFACTURACION = P_IDFACTURACIONREGULARIZACION;

    BEGIN
       BEGIN
          SELECT fcs_facturacionjg.idfacturacion_regulariza INTO V_IDFACTURACIONREGULARIZACION
          FROM fcs_facturacionjg
          WHERE fcs_facturacionjg.idinstitucion=P_IDINSTITUCION
            and fcs_facturacionjg.idfacturacion=P_IDFACTURACION;
      EXCEPTION
         WHEN OTHERS THEN
           V_DATOSERROR2 := 'Error al obtener id_facturacion_regulariza '||sqlerrm;
           V_CODRETORNO2 :=sqlcode;
           RAISE E_ERROR2;
      END;
      -- trazas
      V_DATOSERROR2 := 'Antes de recorrer el cursor';

      /* Recorremos el cursor */
      FOR V_EJG  IN C_EJG (V_IDFACTURACIONREGULARIZACION) LOOP
        -- trazas
        V_DATOSERROR2 := 'Antes de ejecutar precios';

        /* Declaracon de cursores */
        begin
        SELECT nvl(PRECIOHITO,0)  INTO V_PRECIOHITO
        FROM SCS_HITOFACTURABLEGUARDIA
        WHERE IDINSTITUCION = P_IDINSTITUCION AND IDTURNO = V_EJG.IDTURNO AND
               IDGUARDIA = V_EJG.IDGUARDIA AND
               IDHITO = HITO_EJG;
       exception
       when others then
         V_PRECIOHITO:=0;
       end;

        -- trazas
        V_DATOSERROR2 := 'Despues de ejecutar precios';

        -- trazas
        V_DATOSERROR2 := 'Regularizamos el precio';

         -- Regularizamos el precio y los puntos
        V_DIFERENCIAPRECIO    := V_PRECIOHITO - V_EJG.PRECIOAPLICADO;



        -- ACUMULAMOS EL TOTAL DE PRECIOS
        V_TOTAL  := V_TOTAL + V_DIFERENCIAPRECIO;

        --Guardamos el historico de los hitos de la facturacion
        V_DATOSERROR2 := 'Antes de ejecutar el historico de los hitos';
        PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
            P_IDINSTITUCION,
            P_IDFACTURACION,
            V_EJG.IDTURNO,
            V_EJG.IDGUARDIA,
            HITO_EJG,
            V_CODRETORNO2,
            V_DATOSERROR2);
                                        
        -- INSERTAMOS EL APUNTE
        INSERT INTO FCS_FACT_EJG
              (IDINSTITUCION,
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
               USUMODIFICACION)
        VALUES
          (P_IDINSTITUCION,
           P_IDFACTURACION,
           V_EJG.IDTIPOEJG,
           V_EJG.ANIO,
           V_EJG.NUMERO,
           V_EJG.IDPERSONA,
           V_EJG.IDTURNO,
           V_EJG.IDGUARDIA,
           V_EJG.FECHAAPERTURA,
           V_DIFERENCIAPRECIO,
           SYSDATE,
           P_USUMODIFICACION
           );




      END LOOP; /* PARA GUARDIAS */

      -- trazas
      V_DATOSERROR2 := 'Fin correcto';

      P_CODRETORNO := V_CODRETORNO2;
      P_DATOSERROR := V_DATOSERROR2;
      P_TOTAL      := TO_CHAR(V_TOTAL);



    EXCEPTION
      WHEN E_ERROR2 THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
      WHEN OTHERS THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';

    END;
  END PROC_FCS_REGULAR_EJG;

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_SOJ
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDFACTURACION     NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2006-03-21 - Pilar: creacion
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_SOJ (P_IDINSTITUCION    IN NUMBER,
                                  P_IDFACTURACION    IN NUMBER,
                                  P_USUMODIFICACION  IN NUMBER,
                                  P_IDIOMA           IN NUMBER,
                                  P_TOTAL            OUT VARCHAR2,
                                  P_CODRETORNO       OUT VARCHAR2,
                                  P_DATOSERROR       OUT VARCHAR2) IS
   -- VARIABLES
    V_PRECIOHITO            NUMBER := 0;
    V_TOTAL                 NUMBER := 0; /* variable para sumar */
    V_DIFERENCIAPRECIO      NUMBER := 0;
    V_IDFACTURACIONREGULARIZACION NUMBER;


  BEGIN


    -- trazas
    V_DATOSERROR2 := 'Antes de ejecutar guardias';

    DECLARE
      /* Declaracon de cursores */
      CURSOR C_SOJ (P_IDFACTURACIONREGULARIZACION number)IS
        SELECT IDINSTITUCION,
               IDFACTURACION,
               IDTIPOSOJ,
               ANIO,
               NUMERO,
               IDPERSONA,
               IDTURNO,
               IDGUARDIA,
               PRECIOAPLICADO,
               FECHAAPERTURA
          FROM FCS_FACT_SOJ
         WHERE FCS_FACT_SOJ.IDINSTITUCION = P_IDINSTITUCION AND
               FCS_FACT_SOJ.IDFACTURACION = P_IDFACTURACIONREGULARIZACION;

    BEGIN
       BEGIN
          SELECT fcs_facturacionjg.idfacturacion_regulariza INTO V_IDFACTURACIONREGULARIZACION
          FROM fcs_facturacionjg
          WHERE fcs_facturacionjg.idinstitucion=P_IDINSTITUCION
            and fcs_facturacionjg.idfacturacion=P_IDFACTURACION;
      EXCEPTION
         WHEN OTHERS THEN
           V_DATOSERROR2 := 'Error al obtener id_facturacion_regulariza '||sqlerrm;
           V_CODRETORNO2 :=sqlcode;
           RAISE E_ERROR2;
      END;
      -- trazas
      V_DATOSERROR2 := 'Despues de ejecutar guardias';

      /* Recorremos el cursor */
      FOR V_SOJ  IN C_SOJ (V_IDFACTURACIONREGULARIZACION) LOOP
        -- trazas
        V_DATOSERROR2 := 'Antes de ejecutar precios';

        /* Declaracon de cursores */
        begin
        SELECT nvl(PRECIOHITO,0)  INTO V_PRECIOHITO
        FROM SCS_HITOFACTURABLEGUARDIA
        WHERE IDINSTITUCION = P_IDINSTITUCION AND IDTURNO = V_SOJ.IDTURNO AND
               IDGUARDIA = V_SOJ.IDGUARDIA AND
               IDHITO = HITO_SOJ;
        exception
        when others then
         V_PRECIOHITO:=0;
        end;

        -- trazas
        V_DATOSERROR2 := 'Despues de ejecutar precios';

        -- trazas
        V_DATOSERROR2 := 'Regularizamos el precio';

         -- Regularizamos el precio y los puntos
        V_DIFERENCIAPRECIO    := V_PRECIOHITO - V_SOJ.PRECIOAPLICADO;



        -- ACUMULAMOS EL TOTAL DE PRECIOS
        V_TOTAL  := V_TOTAL + V_DIFERENCIAPRECIO;

        --Guardamos el historico de los hitos de la facturacion
        V_DATOSERROR2 := 'Antes de ejecutar el historico de los hitos';
        PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICO_HITOFACT(
            P_IDINSTITUCION,
            P_IDFACTURACION,
            V_SOJ.IDTURNO,
            V_SOJ.IDGUARDIA,
            HITO_SOJ,
            V_CODRETORNO2,
            V_DATOSERROR2);

        -- INSERTAMOS EL APUNTE
        INSERT INTO FCS_FACT_SOJ
              (IDINSTITUCION,
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
               USUMODIFICACION)
        VALUES
          (P_IDINSTITUCION,
           P_IDFACTURACION,
           V_SOJ.IDTIPOSOJ,
           V_SOJ.ANIO,
           V_SOJ.NUMERO,
           V_SOJ.IDPERSONA,
           V_SOJ.IDTURNO,
           V_SOJ.IDGUARDIA,
           V_DIFERENCIAPRECIO,
           V_SOJ.FECHAAPERTURA,
           SYSDATE,
           P_USUMODIFICACION
           );




      END LOOP; /* PARA GUARDIAS */

      -- trazas
      V_DATOSERROR2 := 'Fin correcto';

      P_CODRETORNO := V_CODRETORNO2;
      P_DATOSERROR := V_DATOSERROR2;
      P_TOTAL      := TO_CHAR(V_TOTAL);



    EXCEPTION
      WHEN E_ERROR2 THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := V_CODRETORNO2;
        P_DATOSERROR := V_DATOSERROR2;
      WHEN OTHERS THEN
        P_TOTAL      := TO_CHAR(0);
        P_CODRETORNO := TO_CHAR(SQLCODE);
        P_DATOSERROR := SQLERRM || ' (' || V_DATOSERROR2 || ')';

    END;
  END PROC_FCS_REGULAR_SOJ;

  PROCEDURE PROC_CARGAR_REGULARIZ_GUARDIAS (P_IDINSTITUCION     IN NUMBER,
                                            P_IDFACTURACION     IN NUMBER,
                                            P_IDREGULARIZACION  IN NUMBER,
                                            P_CODRETORNO        OUT VARCHAR2,
                                            P_DATOSERROR        OUT VARCHAR2) IS

  BEGIN

    --copiando registros de FCS_FACT_APUNTE
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_CARGAR_REGULARIZ_GUARDIAS: ' ||
                    'copiando registros de FCS_FACT_APUNTE';
    insert into FCS_FACT_APUNTE
      (idinstitucion, idfacturacion, idapunte, idpersona, idturno, idguardia, fechainicio, idhito, motivo, precioaplicado, fechamodificacion, usumodificacion, preciocostesfijos, idtipoapunte)
      (select idinstitucion, P_IDREGULARIZACION, idapunte, idpersona, idturno, idguardia, fechainicio, idhito, motivo, precioaplicado, fechamodificacion, usumodificacion, 0, idtipoapunte
         from FCS_FACT_APUNTE
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = P_IDFACTURACION
      );

    --copiando registros de FCS_FACT_GUARDIASCOLEGIADO
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_CARGAR_REGULARIZ_GUARDIAS: ' ||
                    'copiando registros de FCS_FACT_GUARDIASCOLEGIADO';
    insert into FCS_FACT_GUARDIASCOLEGIADO
      (idinstitucion, idfacturacion, idapunte, idturno, idguardia, fechainicio, fechafin, idpersona, precioaplicado, fechamodificacion, usumodificacion, idtipo, preciocostesfijos, idhito, motivo)
      (select idinstitucion, P_IDREGULARIZACION, idapunte, idturno, idguardia, fechainicio, fechafin, idpersona, precioaplicado, fechamodificacion, usumodificacion, idtipo, 0, idhito, motivo
         from FCS_FACT_GUARDIASCOLEGIADO
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = P_IDFACTURACION
      );

    --copiando registros de FCS_FACT_ASISTENCIA
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_CARGAR_REGULARIZ_GUARDIAS: ' ||
                    'copiando registros de FCS_FACT_ASISTENCIA';
    insert into FCS_FACT_ASISTENCIA
      (idinstitucion, idfacturacion, idapunte, anio, numero, idpersona, fechahora, fechajustificacion, precioaplicado, fechamodificacion, usumodificacion, idhito, motivo, idtipo)
      (select idinstitucion, P_IDREGULARIZACION, idapunte, anio, numero, idpersona, fechahora, fechajustificacion, precioaplicado, fechamodificacion, usumodificacion, idhito, motivo, idtipo
         from FCS_FACT_ASISTENCIA
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = P_IDFACTURACION
      );

    --copiando registros de FCS_FACT_ACTUACIONASISTENCIA
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_CARGAR_REGULARIZ_GUARDIAS: ' ||
                    'copiando registros de FCS_FACT_ACTUACIONASISTENCIA';
    insert into FCS_FACT_ACTUACIONASISTENCIA
      (idinstitucion, idfacturacion, fechamodificacion, idapunte, idactuacion, anio, numero, idpersona, precioaplicado, fechaactuacion, fechajustificacion, usumodificacion, preciocostesfijos, idhito, motivo, idtipo)
      (select idinstitucion, P_IDREGULARIZACION, fechamodificacion, idapunte, idactuacion, anio, numero, idpersona, precioaplicado, fechaactuacion, fechajustificacion, usumodificacion, 0, idhito, motivo, idtipo
         from FCS_FACT_ACTUACIONASISTENCIA
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = P_IDFACTURACION
      );

    --finalizando
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_CARGAR_REGULARIZACION: Finalizado';

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := sqlcode;
      P_DATOSERROR := P_DATOSERROR || ': ' || sqlerrm;
  END PROC_CARGAR_REGULARIZ_GUARDIAS;

  FUNCTION FUNC_CALC_REGULARIZ_GUARDIAS (P_IDINSTITUCION     IN NUMBER,
                                         P_IDREGULARIZACION  IN NUMBER,
                                         P_IDAPUNTE          IN NUMBER,
                                         P_FECHAFIN          IN DATE,
                                         P_ANIO              IN NUMBER,
                                         P_NUMERO            IN NUMBER,
                                         P_IDACTUACION       IN Number)
    return NUMBER IS

    v_importe_regularizado     number;
    v_aux_importe_regularizado number;

  BEGIN

    v_importe_regularizado := 0;
    v_aux_importe_regularizado := 0;

    --calculo para actuaciones
    begin
    --se aplica a actuaciones, dias y cabeceras
    if not (P_NUMERO is not null and P_IDACTUACION is null) then

      --Ac por cab
      select sum(decode(act.precioaplicado,
                        0,
                        0,
                        hit.preciohito - act.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_ACTUACIONASISTENCIA ACT,
             FCS_FACT_APUNTE              APU,
             SCS_HITOFACTURABLEGUARDIA    HIT
       where act.idinstitucion = apu.idinstitucion
         and act.idfacturacion = apu.idfacturacion
         and act.idapunte = apu.idapunte
         and hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and act.anio = nvl(P_ANIO, act.anio)
         and act.numero = nvl(P_NUMERO, act.numero)
         and act.idactuacion = nvl(P_IDACTUACION, act.idactuacion)
         and act.idhito in (7, 29, 35, 37)
         and apu.idhito in (7, 29, 35, 37)
         and hit.idhito = 7
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --Ac por dia
      select sum(decode(act.precioaplicado,
                        0,
                        0,
                        hit.preciohito - act.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_ACTUACIONASISTENCIA ACT,
             SCS_HITOFACTURABLEGUARDIA    HIT
       where exists (select *
                       from FCS_FACT_ASISTENCIA        ASI,
                            FCS_FACT_GUARDIASCOLEGIADO APU
                      where act.idinstitucion = asi.idinstitucion
                        and act.anio = asi.anio
                        and act.numero = asi.numero
                        and act.idfacturacion = asi.idfacturacion
                        and act.idapunte = asi.idapunte
                        and asi.idinstitucion = apu.idinstitucion
                        and asi.idfacturacion = apu.idfacturacion
                        and asi.idapunte = apu.idapunte
                        and trunc(asi.fechahora) = apu.fechafin
                        and hit.idinstitucion = apu.idinstitucion
                        and hit.idturno = apu.idturno
                        and hit.idguardia = apu.idguardia

                        and apu.idinstitucion = P_IDINSTITUCION
                        and apu.idfacturacion = P_IDREGULARIZACION
                        and apu.idapunte = P_IDAPUNTE
                        and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
                        and apu.idhito in (7, 29, 35, 37))
         and act.anio = nvl(P_ANIO, act.anio)
         and act.numero = nvl(P_NUMERO, act.numero)
         and act.idactuacion = nvl(P_IDACTUACION, act.idactuacion)
         and act.idhito in (7, 29, 35, 37)
         and hit.idhito = 7
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

    end if;
    end;


    --calculo para actuaciones FG
    begin
    --se aplica a actuaciones, dias y cabeceras
    if not (P_NUMERO is not null and P_IDACTUACION is null) then

      --AcFG por cab
      select sum(decode(act.precioaplicado,
                        0,
                        0,
                        hit.preciohito - act.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_ACTUACIONASISTENCIA ACT,
             FCS_FACT_APUNTE              APU,
             SCS_HITOFACTURABLEGUARDIA    HIT
       where act.idinstitucion = apu.idinstitucion
         and act.idfacturacion = apu.idfacturacion
         and act.idapunte = apu.idapunte
         and hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and act.anio = nvl(P_ANIO, act.anio)
         and act.numero = nvl(P_NUMERO, act.numero)
         and act.idactuacion = nvl(P_IDACTUACION, act.idactuacion)
         and act.idhito in (9, 31)
         and apu.idhito in (9, 31)
         and hit.idhito = 9
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcFG por dia
      select sum(decode(act.precioaplicado,
                        0,
                        0,
                        hit.preciohito - act.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_ACTUACIONASISTENCIA ACT,
             SCS_HITOFACTURABLEGUARDIA    HIT
       where exists (select *
                       from FCS_FACT_GUARDIASCOLEGIADO APU
                      where act.idinstitucion = apu.idinstitucion
                        and act.idfacturacion = apu.idfacturacion
                        and act.idapunte = apu.idapunte
                        and hit.idinstitucion = apu.idinstitucion
                        and hit.idturno = apu.idturno
                        and hit.idguardia = apu.idguardia

                        and apu.idinstitucion = P_IDINSTITUCION
                        and apu.idfacturacion = P_IDREGULARIZACION
                        and apu.idapunte = P_IDAPUNTE
                        and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
                        and apu.idhito in (9, 31))
         and act.anio = nvl(P_ANIO, act.anio)
         and act.numero = nvl(P_NUMERO, act.numero)
         and act.idactuacion = nvl(P_IDACTUACION, act.idactuacion)
         and act.idhito in (9, 31)
         and hit.idhito = 9
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

    end if;
    end;


    --calculo para asistencias
    begin
    --se aplica a asistencias, dias y cabeceras
    if P_IDACTUACION is null then

      --As por cab
      select sum(decode(asi.precioaplicado,
                        0,
                        0,
                        hit.preciohito - asi.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_ASISTENCIA       ASI,
             FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where asi.idinstitucion = apu.idinstitucion
         and asi.idfacturacion = apu.idfacturacion
         and asi.idapunte = apu.idapunte
         and hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and asi.anio = nvl(P_ANIO, asi.anio)
         and asi.numero = nvl(P_NUMERO, asi.numero)
         and asi.idhito in (5, 27, 11, 36)
         and apu.idhito in (5, 27, 11, 36)
         and hit.idhito = 5
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --As por dia
      select sum(decode(asi.precioaplicado,
                        0,
                        0,
                        hit.preciohito - asi.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_ASISTENCIA        ASI,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where exists (select *
                       from FCS_FACT_GUARDIASCOLEGIADO APU
                      where asi.idinstitucion = apu.idinstitucion
                        and asi.idfacturacion = apu.idfacturacion
                        and asi.idapunte = apu.idapunte
                        and trunc(asi.fechahora) = apu.fechafin
                        and hit.idinstitucion = apu.idinstitucion
                        and hit.idturno = apu.idturno
                        and hit.idguardia = apu.idguardia

                        and apu.idinstitucion = P_IDINSTITUCION
                        and apu.idfacturacion = P_IDREGULARIZACION
                        and apu.idapunte = P_IDAPUNTE
                        and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
                        and apu.idhito in (5, 27, 11, 36))
         and asi.anio = nvl(P_ANIO, asi.anio)
         and asi.numero = nvl(P_NUMERO, asi.numero)
         and asi.idhito in (5, 27, 11, 36)
         and hit.idhito = 5
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

    end if;
    end;


    --calculo para dias y cabeceras
    begin
    --se aplica a dias y cabeceras
    if P_NUMERO is null then

      --GSAs por cabecera
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (1, 38, 39)
         and hit.idhito = 1
         and nvl(hit.agrupar, '0') = '1'
         and not exists (select *
                from SCS_HITOFACTURABLEGUARDIA HIT
               where hit.idinstitucion = apu.idinstitucion
                 and hit.idturno = apu.idturno
                 and hit.idguardia = apu.idguardia
                 and hit.idhito = 4
                 and nvl(hit.agrupar, '0') = '1');

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GSAs por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (1, 38, 39)
         and hit.idhito = 1
         and nvl(hit.agrupar, '0') = '0'
         and not exists (select *
                from SCS_HITOFACTURABLEGUARDIA HIT
               where hit.idinstitucion = apu.idinstitucion
                 and hit.idturno = apu.idturno
                 and hit.idguardia = apu.idguardia
                 and hit.idhito = 4
                 and nvl(hit.agrupar, '0') = '0');

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GSAc por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (44, 38, 40)
         and hit.idhito = 1
         and nvl(hit.agrupar, '0') = '1'
         and (exists (select *
                        from SCS_HITOFACTURABLEGUARDIA HIT
                       where hit.idinstitucion = apu.idinstitucion
                         and hit.idturno = apu.idturno
                         and hit.idguardia = apu.idguardia
                         and hit.idhito = 44
                         and nvl(hit.agrupar, '0') = '1') or exists
              (select *
                 from SCS_HITOFACTURABLEGUARDIA HIT
                where hit.idinstitucion = apu.idinstitucion
                  and hit.idturno = apu.idturno
                  and hit.idguardia = apu.idguardia
                  and hit.idhito = 4
                  and nvl(hit.agrupar, '0') = '1'));

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GSAc por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (44, 38, 40)
         and hit.idhito = 1
         and nvl(hit.agrupar, '0') = '0'
         and (exists (select *
                        from SCS_HITOFACTURABLEGUARDIA HIT
                       where hit.idinstitucion = apu.idinstitucion
                         and hit.idturno = apu.idturno
                         and hit.idguardia = apu.idguardia
                         and hit.idhito = 44
                         and nvl(hit.agrupar, '0') = '0') or exists
              (select *
                 from SCS_HITOFACTURABLEGUARDIA HIT
                where hit.idinstitucion = apu.idinstitucion
                  and hit.idturno = apu.idturno
                  and hit.idguardia = apu.idguardia
                  and hit.idhito = 4
                  and nvl(hit.agrupar, '0') = '0'));

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GDAs por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (2, 14, 15)
         and hit.idhito = 2
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GDAs por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (2, 14, 15)
         and hit.idhito = 2
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GDAc por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (4, 16, 40)
         and hit.idhito = 4
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --GDAc por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (4, 16, 40)
         and hit.idhito = 4
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AsMax por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (3, 17)
         and hit.idhito = 3
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AsMax por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (3, 17)
         and hit.idhito = 3
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcMax por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (8, 18)
         and hit.idhito = 8
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcMax por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (8, 18)
         and hit.idhito = 8
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcFGMax por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (6, 26)
         and hit.idhito = 6
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcFGMax por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - nvl((Select Hit_Anterior.Preciohito
                                         From Fcs_Historico_Hitofact Hit_Anterior
                                        Where Hit_Anterior.Idinstitucion = Apu.Idinstitucion
                                          And Hit_Anterior.Idturno = Apu.Idturno
                                          And Hit_Anterior.Idguardia = Apu.Idguardia
                                          And Hit_Anterior.Idfacturacion = Apu.Idfacturacion
                                          And Hit_Anterior.Idhito = Hit.Idhito), apu.precioaplicado)))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (6, 26)
         and hit.idhito = 6
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AsMin por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (10, 33)
         and hit.idhito = 10
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AsMin por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (10, 33)
         and hit.idhito = 10
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcMin por cab
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_APUNTE           APU,
             SCS_HITOFACTURABLEGUARDIA HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.idapunte = decode(P_FECHAFIN, null, apu.idapunte, -1)
         and apu.idhito in (19, 34)
         and hit.idhito = 19
         and nvl(hit.agrupar, '0') = '1';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

      --AcMin por dia
      select sum(decode(apu.precioaplicado,
                        0,
                        0,
                        hit.preciohito - apu.precioaplicado))
        into v_aux_importe_regularizado
        from FCS_FACT_GUARDIASCOLEGIADO APU,
             SCS_HITOFACTURABLEGUARDIA  HIT
       where hit.idinstitucion = apu.idinstitucion
         and hit.idturno = apu.idturno
         and hit.idguardia = apu.idguardia

         and apu.idinstitucion = P_IDINSTITUCION
         and apu.idfacturacion = P_IDREGULARIZACION
         and apu.idapunte = P_IDAPUNTE
         and apu.fechafin = nvl(P_FECHAFIN, apu.fechafin)
         and apu.idhito in (19, 34)
         and hit.idhito = 19
         and nvl(hit.agrupar, '0') = '0';

      if v_aux_importe_regularizado is not null then
        v_importe_regularizado := v_importe_regularizado + v_aux_importe_regularizado;
      end if;

    end if;
    end;


    if (v_importe_regularizado < 0) then
      v_importe_regularizado := 0;
    end if;
    return v_importe_regularizado;

  EXCEPTION
    WHEN OTHERS THEN
      return -1;
  END FUNC_CALC_REGULARIZ_GUARDIAS;

  -----------------------------------------------------------------------------
  -- PROCEDURE PROC_FCS_REGULAR_GUARDIAS
  --
  -- Descripcion: ejecuta la regularizacion de una facturacion
  -- Parametros de entrada:
  --   * P_IDINSTITUCION     NUMBER: identificador de la Institucion
  --   * P_IDREGULARIZACION  NUMBER: identificador de la Regularizacion
  --   * P_USUMODIFICACION   NUMBER: usuario que realiza la llamada
  --   * P_IDIOMA            NUMBER: idioma a usar en los recursos
  -- Parametros de salida:
  --   * P_TOTAL       VARCHAR2: importe total obtenido en la Regularizacion
  --   * P_CODRETORNO  VARCHAR2: codigo indicador del resultado de la
  --     Regularizacion. Si es distinto de 0, indica error
  --   * P_DATOSERROR  VARCHAR2: descripcion del error obtenido en la
  --     Regularizacion si P_CODRETORNO <> 0
  -- Historico:
  --   * 2009-09-21 - Adrian: cambio total
  --
  -----------------------------------------------------------------------------
  PROCEDURE PROC_FCS_REGULAR_GUARDIAS2 (P_IDINSTITUCION     IN NUMBER,
                                       P_IDREGULARIZACION  IN NUMBER,
                                       P_USUMODIFICACION   IN NUMBER,
                                       P_IDIOMA            IN NUMBER,
                                       P_TOTAL             OUT VARCHAR2,
                                       P_CODRETORNO        OUT VARCHAR2,
                                       P_DATOSERROR        OUT VARCHAR2) IS

    cursor C_APUNTES (P_IDINSTITUCION  NUMBER,
                      P_IDFACTURACION  NUMBER) is
      select *
        from FCS_FACT_APUNTE
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;
    cursor C_DIAS (P_IDINSTITUCION  NUMBER,
                   P_IDFACTURACION  NUMBER) is
      select *
        from FCS_FACT_GUARDIASCOLEGIADO
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;
    cursor C_ASIS (P_IDINSTITUCION  NUMBER,
                   P_IDFACTURACION  NUMBER) is
      select *
        from FCS_FACT_ASISTENCIA
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;
    cursor C_ACTS (P_IDINSTITUCION  NUMBER,
                   P_IDFACTURACION  NUMBER) is
      select *
        from FCS_FACT_ACTUACIONASISTENCIA
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;

    v_codretorno varchar2(10);
    v_datoserror varchar2(2000);

    v_idfacturacion_regulariza number;

    v_totalregularizacion    Number := 0;
    v_importe_regularizacion number;

  BEGIN
    --
    --
    --
    -- Actualmente no funciona bien la regularizacion de guardias
    -- y ademas bloquea la BD, 
    -- asi que de momento la eliminamos de funcionalidad
    --
    --
    --

    --inicializando valor para retorno correcto
    P_CODRETORNO := 0;
    P_DATOSERROR := '';
               
     -- Se almacenan los historicos a nivel de institucion
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'Almacenando los datos historicos de la regularizacion'; 
                    
    PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_GUARDIAS(P_IDINSTITUCION,
                        P_IDREGULARIZACION,
                        V_CODRETORNO2,
                        V_DATOSERROR2);
    
    PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTO_HITOFACT_REGU(P_IDINSTITUCION,
                        P_IDREGULARIZACION,
                        V_CODRETORNO2,
                        V_DATOSERROR2);

    --obteniendo idfacturacion origen de la regularizacion
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'obteniendo idfacturacion origen de la regularizacion';                                   
    select idfacturacion_regulariza
      into v_idfacturacion_regulariza
      from FCS_FACTURACIONJG
     where idinstitucion = P_IDINSTITUCION
       and idfacturacion = P_IDREGULARIZACION;

    --copiando todos los apuntes completos de la facturacion en la regularizacion
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'copiando todos los apuntes completos de la facturacion en la regularizacion';
    PROC_CARGAR_REGULARIZ_GUARDIAS (P_IDINSTITUCION,
                                    v_idfacturacion_regulariza,
                                    P_IDREGULARIZACION,
                                    v_codretorno,
                                    v_datoserror);
    if (v_codretorno <> 0) then
      P_CODRETORNO := v_codretorno;
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      v_datoserror;
      return;
    end if;

    --inicializando el importe total de la regularizacion
    v_totalregularizacion := 0;

    --recorriendo las actuaciones
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo las actuaciones';
    for r_actuacion in C_ACTS(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      --calculando importe regularizacion de la actuacion
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'calculando importe regularizacion de la actuacion ' || r_actuacion.idapunte;
      v_importe_regularizacion := FUNC_CALC_REGULARIZ_GUARDIAS (r_actuacion.idinstitucion,
                                                                r_actuacion.idfacturacion,
                                                                r_actuacion.idapunte,
                                                                null,
                                                                r_actuacion.anio,
                                                                r_actuacion.numero,
                                                                r_actuacion.idactuacion);
      if (v_importe_regularizacion < 0) then
        v_importe_regularizacion := 0;
      end if;

      --actualizando la regularizacion de la actuacion
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion de la actuacion ' || r_actuacion.idapunte;
      update FCS_FACT_ACTUACIONASISTENCIA
         set precioaplicado = v_importe_regularizacion,
             fechamodificacion = sysdate,
             usumodificacion = P_USUMODIFICACION
       where idinstitucion = r_actuacion.idinstitucion
         and idfacturacion = P_IDREGULARIZACION
         and idapunte = r_actuacion.idapunte
         and anio = r_actuacion.anio
         and numero = r_actuacion.numero
         and idactuacion = r_actuacion.idactuacion;

    end loop;

    --recorriendo las asistencias
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo las asistencias';
    for r_asistencia in C_ASIS(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      --calculando importe regularizacion de la asistencia
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'calculando importe regularizacion de la asistencia ' || r_asistencia.idapunte;
      v_importe_regularizacion := FUNC_CALC_REGULARIZ_GUARDIAS (r_asistencia.idinstitucion,
                                                                r_asistencia.idfacturacion,
                                                                r_asistencia.idapunte,
                                                                null,
                                                                r_asistencia.anio,
                                                                r_asistencia.numero,
                                                                Null);
      if (v_importe_regularizacion < 0) then
        v_importe_regularizacion := 0;
      end if;

      --actualizando la regularizacion de la asistencia
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion de la asistencia ' || r_asistencia.idapunte;
      update FCS_FACT_ASISTENCIA
         set precioaplicado = v_importe_regularizacion,
             fechamodificacion = sysdate,
             usumodificacion = P_USUMODIFICACION
       where idinstitucion = r_asistencia.idinstitucion
         and idfacturacion = P_IDREGULARIZACION
         and idapunte = r_asistencia.idapunte
         and anio = r_asistencia.anio
         and numero = r_asistencia.numero;

    end loop;

    --recorriendo los dias
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo los dias';
    for r_dia in C_DIAS(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      --calculando importe regularizacion del dia
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'calculando importe regularizacion del dia ' || r_dia.idapunte;
      v_importe_regularizacion := FUNC_CALC_REGULARIZ_GUARDIAS (r_dia.idinstitucion,
                                                                r_dia.idfacturacion,
                                                                r_dia.idapunte,
                                                                r_dia.fechafin,
                                                                null,
                                                                null,
                                                                Null);
      if (v_importe_regularizacion < 0) then
        v_importe_regularizacion := 0;
      end if;

      --actualizando la regularizacion del dia
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion del dia ' || r_dia.idapunte;
      update FCS_FACT_GUARDIASCOLEGIADO
         set precioaplicado = v_importe_regularizacion,
             fechamodificacion = sysdate,
             usumodificacion = P_USUMODIFICACION
       where idinstitucion = r_dia.idinstitucion
         and idfacturacion = P_IDREGULARIZACION
         and idapunte = r_dia.idapunte
         and fechafin = r_dia.fechafin;

    end loop;

    --recorriendo los apuntes
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo los apuntes';
    for r_apunte in C_APUNTES(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      --calculando importe regularizacion del apunte
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'calculando importe regularizacion del apunte ' || r_apunte.idapunte;
      v_importe_regularizacion := FUNC_CALC_REGULARIZ_GUARDIAS (r_apunte.idinstitucion,
                                                                r_apunte.idfacturacion,
                                                                r_apunte.idapunte,
                                                                null,
                                                                null,
                                                                null,
                                                                Null);
      if (v_importe_regularizacion < 0) then
        v_importe_regularizacion := 0;
      end if;

      --actualizando la regularizacion del apunte
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion del apunte ' || r_apunte.idapunte;
      update FCS_FACT_APUNTE
         set precioaplicado = v_importe_regularizacion,
             fechamodificacion = sysdate,
             usumodificacion = P_USUMODIFICACION
       where idinstitucion = r_apunte.idinstitucion
         and idfacturacion = P_IDREGULARIZACION
         and idapunte = r_apunte.idapunte;

      --acumulando importe para total
      v_totalregularizacion := v_totalregularizacion + v_importe_regularizacion;

    end loop;

    --finalizando
    P_TOTAL      := v_totalregularizacion;
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: Finalizado';

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := sqlcode;
      P_DATOSERROR := P_DATOSERROR || ': ' || sqlerrm;
  END PROC_FCS_REGULAR_GUARDIAS2;

  PROCEDURE PROC_FCS_REGULAR_GUARDIAS (P_IDINSTITUCION     IN NUMBER,
                                       P_IDREGULARIZACION  IN NUMBER,
                                       P_USUMODIFICACION   IN NUMBER,
                                       P_IDIOMA            IN NUMBER,
                                       P_TOTAL             OUT VARCHAR2,
                                       P_CODRETORNO        OUT VARCHAR2,
                                       P_DATOSERROR        OUT VARCHAR2) IS

    cursor C_APUNTES (P_IDINSTITUCION  NUMBER,
                      P_IDFACTURACION  NUMBER) is
      select apu.*, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              null,
                                              null,
                                              null,
                                              Null) As importe_regularizacion
        from FCS_FACT_APUNTE apu
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;
    cursor C_DIAS (P_IDINSTITUCION  NUMBER,
                   P_IDFACTURACION  NUMBER) is
      select apu.*, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              fechafin,
                                              null,
                                              null,
                                              Null) As importe_regularizacion
        from FCS_FACT_GUARDIASCOLEGIADO apu
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;
    cursor C_ASIS (P_IDINSTITUCION  NUMBER,
                   P_IDFACTURACION  NUMBER) is
      select apu.*, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              null,
                                              anio,
                                              numero,
                                              Null) As importe_regularizacion
        from FCS_FACT_ASISTENCIA apu
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;
    cursor C_ACTS (P_IDINSTITUCION  NUMBER,
                   P_IDFACTURACION  NUMBER) is
      select apu.*, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              null,
                                              anio,
                                              numero,
                                              idactuacion) As importe_regularizacion
        from FCS_FACT_ACTUACIONASISTENCIA apu
       where idinstitucion = P_IDINSTITUCION
         and idfacturacion = P_IDFACTURACION;

    v_codretorno varchar2(10);
    v_datoserror varchar2(2000);

    v_idfacturacion_regulariza number;

    v_totalregularizacion    Number := 0;

  BEGIN

    --inicializando valor para retorno correcto
    P_CODRETORNO := 0;
    P_DATOSERROR := '';
               
     -- Se almacenan los historicos a nivel de institucion
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'Almacenando los datos historicos de la regularizacion'; 
                    
    PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTORICOS_GUARDIAS(P_IDINSTITUCION,
                        P_IDREGULARIZACION,
                        V_CODRETORNO2,
                        V_DATOSERROR2);
    
    PKG_SIGA_FCS_HISTORICO.PROC_FCS_HISTO_HITOFACT_REGU(P_IDINSTITUCION,
                        P_IDREGULARIZACION,
                        V_CODRETORNO2,
                        V_DATOSERROR2);

    --obteniendo idfacturacion origen de la regularizacion
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'obteniendo idfacturacion origen de la regularizacion';                                   
    select idfacturacion_regulariza
      into v_idfacturacion_regulariza
      from FCS_FACTURACIONJG
     where idinstitucion = P_IDINSTITUCION
       and idfacturacion = P_IDREGULARIZACION;

    --inicializando el importe total de la regularizacion
    v_totalregularizacion := 0;

    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo los apuntes';
    /*
    insert into FCS_FACT_APUNTE
      (idinstitucion, idfacturacion, idapunte, idpersona, idturno, idguardia, fechainicio, idhito, motivo, precioaplicado, fechamodificacion, usumodificacion, preciocostesfijos, idtipoapunte)
      (select P_IDINSTITUCION, P_IDREGULARIZACION, idapunte, idpersona, idturno, idguardia, fechainicio, idhito, motivo, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              null,
                                              null,
                                              null,
                                              Null), Sysdate, P_USUMODIFICACION, 0, idtipoapunte
         from FCS_FACT_APUNTE
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = v_idfacturacion_regulariza
      );
    select Sum(precioaplicado)
      into v_totalregularizacion
      from FCS_FACT_APUNTE
     where idinstitucion = P_IDINSTITUCION
       and idfacturacion = P_IDREGULARIZACION;
    
    */
    for reg in C_APUNTES(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion del apunte ' || reg.idapunte;
      insert into FCS_FACT_APUNTE
        (idinstitucion, idfacturacion, idapunte, idpersona, idturno, idguardia, fechainicio, idhito, motivo, precioaplicado, fechamodificacion, usumodificacion, preciocostesfijos, idtipoapunte)
      Values
        (P_IDINSTITUCION, P_IDREGULARIZACION, reg.idapunte, reg.idpersona, reg.idturno, reg.idguardia, reg.fechainicio, reg.idhito, reg.motivo, reg.importe_regularizacion, Sysdate, P_USUMODIFICACION, 0, reg.idtipoapunte);
      
      --acumulando importe para total
      if (reg.importe_regularizacion > 0) then
         v_totalregularizacion := v_totalregularizacion + reg.importe_regularizacion;
      end if;

    end loop;
    

    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo los dias';
    /*
    insert into FCS_FACT_GUARDIASCOLEGIADO
      (idinstitucion, idfacturacion, idapunte, idturno, idguardia, fechainicio, fechafin, idpersona, precioaplicado, fechamodificacion, usumodificacion, idtipo, preciocostesfijos, idhito, motivo)
      (select P_IDINSTITUCION, P_IDREGULARIZACION, idapunte, idturno, idguardia, fechainicio, fechafin, idpersona, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              fechafin,
                                              null,
                                              null,
                                              Null), Sysdate, P_USUMODIFICACION, idtipo, 0, idhito, motivo
         from FCS_FACT_GUARDIASCOLEGIADO
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = v_idfacturacion_regulariza
      );
    */
    for reg in C_DIAS(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion del dia ' || reg.idapunte;
      insert into FCS_FACT_GUARDIASCOLEGIADO
        (idinstitucion, idfacturacion, idapunte, idturno, idguardia, fechainicio, fechafin, idpersona, precioaplicado, fechamodificacion, usumodificacion, idtipo, preciocostesfijos, idhito, motivo)
      Values
        (P_IDINSTITUCION, P_IDREGULARIZACION, reg.idapunte, reg.idturno, reg.idguardia, reg.fechainicio, reg.fechafin, reg.idpersona, reg.importe_regularizacion, Sysdate, P_USUMODIFICACION, reg.idtipo, 0, reg.idhito, reg.motivo);

    end loop;
    

    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo las asistencias';
    /*
    insert into FCS_FACT_ASISTENCIA
      (idinstitucion, idfacturacion, idapunte, anio, numero, idpersona, fechahora, fechajustificacion, precioaplicado, fechamodificacion, usumodificacion, idhito, motivo, idtipo)
      (select P_IDINSTITUCION, P_IDREGULARIZACION, idapunte, anio, numero, idpersona, fechahora, fechajustificacion, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              null,
                                              anio,
                                              numero,
                                              Null), Sysdate, P_USUMODIFICACION, idhito, motivo, idtipo
         from FCS_FACT_ASISTENCIA
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = v_idfacturacion_regulariza
      );
    */
    for reg in C_ASIS(P_IDINSTITUCION, v_idfacturacion_regulariza) loop

      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion de la asistencia ' || reg.idapunte;
      insert into FCS_FACT_ASISTENCIA
        (idinstitucion, idfacturacion, idapunte, anio, numero, idpersona, fechahora, fechajustificacion, precioaplicado, fechamodificacion, usumodificacion, idhito, motivo, idtipo)
      Values
        (P_IDINSTITUCION, P_IDREGULARIZACION, reg.idapunte, reg.anio, reg.numero, reg.idpersona, reg.fechahora, reg.fechajustificacion, reg.importe_regularizacion, Sysdate, P_USUMODIFICACION, reg.idhito, reg.motivo, reg.idtipo);

    end loop;
    

    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                    'recorriendo las actuaciones';
    /*
    insert into FCS_FACT_ACTUACIONASISTENCIA
      (idinstitucion, idfacturacion, fechamodificacion, idapunte, idactuacion, anio, numero, idpersona, precioaplicado, fechaactuacion, fechajustificacion, usumodificacion, preciocostesfijos, idhito, motivo, idtipo)
      (select P_IDINSTITUCION, P_IDREGULARIZACION, Sysdate, idapunte, idactuacion, anio, numero, idpersona, FUNC_CALC_REGULARIZ_GUARDIAS (idinstitucion,
                                              idfacturacion,
                                              idapunte,
                                              null,
                                              anio,
                                              numero,
                                              idactuacion), fechaactuacion, fechajustificacion, P_USUMODIFICACION, 0, idhito, motivo, idtipo
         from FCS_FACT_ACTUACIONASISTENCIA
        where idinstitucion = P_IDINSTITUCION
          and idfacturacion = v_idfacturacion_regulariza
      );
    */
    for reg in C_ACTS(P_IDINSTITUCION, v_idfacturacion_regulariza) Loop
    
      P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: ' ||
                      'actualizando la regularizacion de la actuacion ' || reg.idapunte;

      insert into FCS_FACT_ACTUACIONASISTENCIA
        (idinstitucion, idfacturacion, fechamodificacion, idapunte, idactuacion, anio, numero, idpersona, precioaplicado, fechaactuacion, fechajustificacion, usumodificacion, preciocostesfijos, idhito, motivo, idtipo)
      Values
        (P_IDINSTITUCION, P_IDREGULARIZACION, Sysdate, reg.idapunte, reg.idactuacion, reg.anio, reg.numero, reg.idpersona, reg.importe_regularizacion, reg.fechaactuacion, reg.fechajustificacion, P_USUMODIFICACION, 0, reg.idhito, reg.motivo, reg.idtipo);

    end loop;

    --finalizando
    P_TOTAL      := v_totalregularizacion;
    P_CODRETORNO := 0;
    P_DATOSERROR := 'PROC_FCS_REGULAR_GUARDIAS: Finalizado';

  EXCEPTION
    WHEN OTHERS THEN
      P_CODRETORNO := sqlcode;
      P_DATOSERROR := P_DATOSERROR || ': ' || sqlerrm;
  END PROC_FCS_REGULAR_GUARDIAS;

END PKG_SIGA_REGULARIZACION_SJCS;
/
