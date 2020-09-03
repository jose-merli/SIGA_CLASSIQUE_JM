CREATE OR REPLACE Procedure PROC_SIGA_RESOL_2013(P_IDINSTITUCION      In CAJG_REMESARESOLUCION.IDINSTITUCION%Type,
                                                 P_IDREMESARESOLUCION In CAJG_REMESARESOLUCION.IDREMESARESOLUCION%Type,
                                                 P_DELIMITADOR        In Varchar2,
                                                 P_NOMBREFICHERO      In Varchar2,
                                                 P_USUMODIFICACION    In ADM_USUARIOS.IDUSUARIO%Type)

  /*   function f_siga_get_substr_delimitador(P_STR IN VARCHAR2, P_DELIMITADOR IN VARCHAR2, P_POSICION IN NUMBER);*/
 Is

  v_idEstado      Number;
  v_Numero_Cajg   Varchar2(8);
  v_Anio          Varchar2(5);
  v_Numejg        Varchar2(8);
  v_Ref_Ejg       Varchar2(15);
  v_Fechacomision Varchar2(10);
  --v_Fechaapertura Varchar2(10);
  V_IDFUNDAMENTO     SCS_TIPOFUNDAMENTOS.IDFUNDAMENTO%TYPE;
  v_Idtiporesolucion SCS_TIPORESOLUCION.IDTIPORESOLUCION%Type;
  v_TipoResolucion   Varchar2(2);
  v_Codigocajg    Number;
  v_Textoresolucion    Varchar2(255);
  v_Iddenegacion       Number;
  v_Textodenegacion    Varchar2(255);
  v_NumRegistro        Number;
  v_docresolucion Varchar2(15);



  Err_Num Number;
  Err_Msg Varchar2(255);

  EJG_NOT_FOUND Exception;
  EJG_TOO_MANY_ROWS Exception;
  FUNDAMENTO_NOT_FOUND Exception;
  TIPORESOLUCION_NOT_FOUND Exception;
  FORMATO_LINEA_INVALIDO Exception;

  Ejg_Anio      SCS_EJG.ANIO%Type;
  Ejg_Numero    SCS_EJG.NUMERO%Type;
  Ejg_Idtipoejg SCS_EJG.IDTIPOEJG%Type;



  -- Comprobación de Línea ----------------------------
  comp_Resolucion varchar(200) := '^(1|2|7|9)\|\d{1,7}\|\d{4}/\d{1,5}\|(1|2|3|4|5|6|7|8|9|10|11|13)\|[^\|]+\|(1|2|3|4|5|6|7|8)\|[^\|]+\|([0-2][0-9]|3[0-1])(\/)(0[1-9]|1[0-2])(\/)(\d{4})$';


  Cursor C_DATOS Is
    Select Rf.Idremesaresolucionfichero, Rf.Linea
      From Cajg_Remesaresolucionfichero Rf
     Where Rf.Idinstitucion = P_IDINSTITUCION
       And Rf.Idremesaresolucion = P_IDREMESARESOLUCION
     Order By Rf.Idremesaresolucionfichero;

Begin
    v_NumRegistro := 1;

    FOR REG IN C_DATOS LOOP

      BEGIN
        v_Ref_Ejg       := 'Indefinido';

      IF (v_NumRegistro > 0) THEN
        IF( REGEXP_INSTR(REG.LINEA, comp_Resolucion) = 0) THEN
            RAISE FORMATO_LINEA_INVALIDO;
            --NULL;
        END IF;
      END IF;

      v_NumRegistro := v_NumRegistro + 1;

          /* Obtenemos el dato de la columna 1 (idEstado)
            1 = En buen fin y por tanto terminados.
            2 =
            7 = devueltos al Colegio de Abogados --> 6 Devuelto al Colegio.
            9 = archivados --> 5 Archivo.
          */
          v_idEstado := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 1);

        /* AÑO Y MUMERO EXPEDIENTE CAJ */
          /* Obtenemos el dato de la columna 2 (idExpediente) */
          v_numero_cajg := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 2);
          v_docresolucion := 'CAJG'||v_numero_cajg;
          -- v_aniocajg
        /* AÑO Y NUMERO EXPEDIENTE SIGA */
          /* Obtenemos el dato de la columna 3 (id_EJG) */
          v_Ref_Ejg := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 3);
          v_anio := substr(v_Ref_Ejg, 1, instr(v_Ref_Ejg, '/') - 1);
          v_numejg := substr(v_Ref_Ejg, instr(v_Ref_Ejg, '/') + 1);
        /* TIPO RESOLUCIÓN DE LA COMISIÓN */
          /* Obtenemos el dato de la columna 4 (idResolucion) */
          v_TipoResolucion := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 4);

          IF (v_idEstado = 7) THEN
             v_TipoResolucion := 6; -- Devuelto al Colegio.
          ELSIF (v_idEstado = 9) THEN
            v_TipoResolucion := 5; -- Archivo.
          END IF;

        /* Obtenemos el dato de la columna 5 (TextoResolucion) */
        v_Textoresolucion := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 5);
        /* Obtenemos el dato de la columna 6 (IdDenegacion) */
        v_Iddenegacion := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 6);
        /* Obtenemos el dato de la columna 7 (TextoDenegacion) */
        v_Textodenegacion := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 7);

    /* FECHA RESOLUCIÓN */
    /* Obtenemos el dato de la columna 18 (F_Comision) */
        v_fechaComision := pkg_siga_trataresoluciones.f_siga_get_substr_delimitador(REG.LINEA, P_DELIMITADOR, 8);


    -- LOCALIZAR EJG
        BEGIN
          --Buscamos primero el EJG
          SELECT EJG.ANIO, EJG.NUMERO, EJG.IDTIPOEJG
            INTO Ejg_Anio, Ejg_Numero, Ejg_Idtipoejg
            FROM SCS_EJG EJG
           WHERE EJG.IDINSTITUCION = P_IDINSTITUCION
             AND EJG.NUMEJG = to_number(v_numejg)
             AND EJG.ANIO = v_anio;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            RAISE EJG_NOT_FOUND;
          WHEN TOO_MANY_ROWS THEN
            RAISE EJG_TOO_MANY_ROWS;
        END;
/*
      --Buscamos el IDTIPORESOLUCION a partir del mapeo de datos con las tablas del PCAJG
      SELECT S2.IDTIPORESOLUCION INTO V_IDTIPORESOLUCION
        FROM SCS_TIPORESOLUCION S2, PCAJG_TIPO_RESOLUCION P2, PCAJG_TIPO_RESOLUCION_SCSTIPOR SP2
      WHERE S2.IDTIPORESOLUCION = SP2.IDTIPORESOLUCION
        AND SP2.IDINSTITUCION = P2.IDINSTITUCION
        AND SP2.IDENTIFICADOR = P2.IDENTIFICADOR
        AND P2.IDINSTITUCION = P_IDINSTITUCION
        AND P2.CODIGO = V_CODIGOCAJG;

      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          RAISE TIPORESOLUCION_NOT_FOUND;
      END; */

        BEGIN
          --Buscamos el IDTIPORESOLUCION a partir del mapeo de datos con las tablas del PCAJG
          SELECT S2.IDTIPORESOLUCION INTO V_IDTIPORESOLUCION
            FROM SCS_TIPORESOLUCION S2
          WHERE S2.IDTIPORESOLUCION = v_TipoResolucion;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            RAISE TIPORESOLUCION_NOT_FOUND;
        END;

/*
       BEGIN
        --Buscamos el IDFUNDAMENTO a partir del mapeo de datos con las tablas del PCAJG
          SELECT S.IDFUNDAMENTO INTO V_IDFUNDAMENTO
            FROM SCS_TIPOFUNDAMENTOS S, PCAJG_MOTIVO_RESOLUCION P, PCAJG_MOTIVO_RESOLUCION_SCSTIP SP
           WHERE S.IDINSTITUCION = SP.IDINSTITUCION
             AND S.IDFUNDAMENTO = SP.IDFUNDAMENTO
             AND SP.IDINSTITUCION = P.IDINSTITUCION
             AND SP.IDENTIFICADOR = P.IDENTIFICADOR
             AND P.IDINSTITUCION = P_IDINSTITUCION
             AND P.CODIGO = V_CODIGOCAJG;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            RAISE FUNDAMENTO_NOT_FOUND;
        END; */

       BEGIN
        --Buscamos el IDFUNDAMENTO a partir del mapeo de datos con las tablas del PCAJG
          SELECT S.IDFUNDAMENTO INTO V_IDFUNDAMENTO
            FROM SCS_TIPOFUNDAMENTOS S
           WHERE S.IDINSTITUCION = P_IDINSTITUCION
             AND S.IDFUNDAMENTO = v_Iddenegacion;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            RAISE FUNDAMENTO_NOT_FOUND;
        END;

      /* ACTUALIZAMOS LOS DATOS DEL EJG */
        UPDATE SCS_EJG EJG
           SET EJG.NUMERO_CAJG = v_numero_cajg,
               EJG.IDTIPORATIFICACIONEJG = v_Idtiporesolucion,
               EJG.IDFUNDAMENTOJURIDICO  = v_Iddenegacion,
               EJG.FECHANOTIFICACION     = TRUNC(SYSDATE),
               EJG.REFAUTO = P_NOMBREFICHERO,
               EJG.FECHARESOLUCIONCAJG = TO_DATE(v_fechaComision, 'DD/MM/YYYY'),
               EJG.FECHAMODIFICACION = SYSDATE,
               EJG.USUMODIFICACION = P_USUMODIFICACION,
               EJG.IDORIGENCAJG = 14,
               EJG.TURNADORATIFICACION = 1,
               EJG.RATIFICACIONDICTAMEN = v_Idtiporesolucion || ' -- ' || v_Textoresolucion || CHR(13) || CHR(10) || v_Iddenegacion || ' -- ' || v_Textodenegacion,
               EJG.DOCRESOLUCION = v_docresolucion
          WHERE EJG.IDINSTITUCION = P_IDINSTITUCION
           AND EJG.IDTIPOEJG = Ejg_Idtipoejg
           AND EJG.ANIO = Ejg_Anio
           AND EJG.NUMERO = Ejg_Numero;

      EXCEPTION
         WHEN EJG_NOT_FOUND THEN
          --Si no encontramos el EJG.
          UPDATE CAJG_REMESARESOLUCIONFICHERO
             SET IDERRORESREMESARESOL = 1,
             PARAMETROSERROR = v_Ref_Ejg
           WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;

        WHEN EJG_TOO_MANY_ROWS THEN
          --Si encuentro más de un EJG.
          UPDATE CAJG_REMESARESOLUCIONFICHERO
             SET IDERRORESREMESARESOL = 2,
             PARAMETROSERROR = v_Ref_Ejg
           WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;

        WHEN FUNDAMENTO_NOT_FOUND THEN
          --Si no encuentro el motivo de resolución.
          UPDATE CAJG_REMESARESOLUCIONFICHERO
             SET IDERRORESREMESARESOL = 3,
                 PARAMETROSERROR      = v_Codigocajg || ',' || v_Ref_Ejg
           WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;

        WHEN TIPORESOLUCION_NOT_FOUND THEN
          --Si no encuentro el tipo de resolución.
          UPDATE CAJG_REMESARESOLUCIONFICHERO
             SET IDERRORESREMESARESOL = 4,
                 PARAMETROSERROR      = v_Codigocajg || ',' || v_Ref_Ejg
           WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;

       WHEN FORMATO_LINEA_INVALIDO THEN
         --Si el formato de la línea es inválido.
         UPDATE CAJG_REMESARESOLUCIONFICHERO
            SET IDERRORESREMESARESOL = 20,
               PARAMETROSERROR = v_Ref_Ejg
          WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;

        WHEN OTHERS THEN
          --Si ocurre cualquier otro error.
          err_num := SQLCODE;
          err_msg := SQLERRM;
          UPDATE CAJG_REMESARESOLUCIONFICHERO
             SET IDERRORESREMESARESOL = -2,
                 PARAMETROSERROR      = v_Ref_Ejg || ',' || err_num || ',' ||err_msg
           WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
      END;

    END LOOP;

End PROC_SIGA_RESOL_2013;
