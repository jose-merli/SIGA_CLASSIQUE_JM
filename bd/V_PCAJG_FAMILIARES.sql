CREATE OR REPLACE VIEW V_PCAJG_FAMILIARES AS
SELECT UF.IDINSTITUCION -- PARA FILTRAR POR EJG
        , UF.IDTIPOEJG  -- PARA FILTRAR POR EJG
        , UF.ANIO       -- PARA FILTRAR POR EJG
        , UF.NUMERO     -- PARA FILTRAR POR EJG
        , ER.IDREMESA
        , UF.IDPERSONA
        , LEN.IDLENGUAJE
        , PAR.CODIGOEXT || '##' || F_SIGA_GETRECURSO(PAR.DESCRIPCION, LEN.IDLENGUAJE) AS F_F_PARENTESCO_CDA
        , (SELECT 1 FROM DUAL WHERE months_between(SYSDATE, VDP.FECHANACIMIENTO)<216) AS F_F_FAMILIARMENOR --12x18=216
        , VDP.TIPOPERSONA_CDA AS F_F_DP_TIPOPERSONA_CDA
        , VDP.TIPOIDENTIFICACION_CDA AS F_F_DP_TIPOIDENTIFICACION_CDA
        , VDP.TIPOIDENTIFICACION_EJIS AS F_F_DP_TIPOIDENTIFICACION_EJIS
        , VDP.IDENTIFICACION AS F_F_DP_IDENTIFICACION
        , VDP.NOMBRE AS F_F_DP_NOMBRE
        , VDP.PRIMERAPELLIDO AS F_F_DP_PRIMERAPELLIDO
        , VDP.SEGUNDOAPELLIDO AS F_F_DP_SEGUNDOAPELLIDO
        , VDP.RAZONSOCIAL AS F_F_DP_RAZONSOCIAL
        , VDP.FECHANACIMIENTO AS F_F_DP_FECHANACIMIENTO
        , VDP.NACIONALIDAD_CDA AS F_F_DP_NACIONALIDAD_CDA
        , (SELECT GL.CODIGOEXT || '##' || F_SIGA_GETRECURSO(GL.DESCRIPCION, LEN.IDLENGUAJE)
             FROM SCS_TIPOGRUPOLABORAL GL
             WHERE GL.IDTIPOGRUPOLAB = UF.IDTIPOGRUPOLAB
              AND GL.IDINSTITUCION = UF.IDINSTITUCION) AS F_F_DP_SITUACIONLABORAL_CDA
        , VDP.PROFESION_CDA AS F_F_DP_PROFESION_CDA
        , VDP.SEXO AS F_F_DP_SEXO
        , VDP.SEXO_EJIS AS F_F_DP_SEXO_EJIS
        , VDP.IDIOMACOMUNICACION AS F_F_DP_IDIOMACOMUNICACION
        , VDP.ESTADOCIVIL_CDA AS F_F_DP_ESTADOCIVIL_CDA
		, VDP.ESTADOCIVIL_EJIS AS F_F_DP_ESTADOCIVIL_EJIS
        , VDP.REGIMENECONOMICO_CDA AS F_F_DP_REGIMENECONOMICO_CDA
        , VDP.REGIMENECONOMICO_EJIS AS F_F_DP_REGIMENECONOMICO_EJIS
        , VDP.NUMHIJOS AS F_F_DP_NUMHIJOS
        , VDP.FECHAFORMALIZACION AS F_F_DP_FECHAFORMALIZACION
        , VDP.TIPOPERSONAJURIDICA_CDA AS F_F_DP_TIPOPERSONAJURIDICA_CDA
        , (SELECT DECODE(COUNT(1), 0, NULL, 1) 
                FROM SCS_EEJG_PETICIONES PET 
                WHERE PET.IDINSTITUCION = UF.IDINSTITUCION
                AND PET.ANIO = UF.ANIO
                AND PET.NUMERO = UF.NUMERO
                AND PET.IDTIPOEJG = UF.IDTIPOEJG
                AND PET.IDPERSONA = UF.IDPERSONA) AS F_F_CONSENTIMIENTO_DATOS
    FROM SCS_UNIDADFAMILIAREJG UF
     , SCS_PARENTESCO PAR
     , V_PCAJG_M_DPERSONA VDP
     , CAJG_EJGREMESA ER
     , ADM_LENGUAJES LEN
WHERE UF.IDINSTITUCION = PAR.IDINSTITUCION(+)
      AND UF.IDPARENTESCO = PAR.IDPARENTESCO(+)
      AND UF.IDINSTITUCION = VDP.IDINSTITUCION
      AND UF.IDPERSONA = VDP.IDPERSONA
      AND LEN.IDLENGUAJE = VDP.IDLENGUAJE
      AND (UF.IDINSTITUCION, UF.IDPERSONA) NOT IN (SELECT EJG.IDINSTITUCION, EJG.IDPERSONAJG
          FROM SCS_EJG EJG
          WHERE EJG.IDINSTITUCION = UF.IDINSTITUCION
          AND EJG.IDTIPOEJG = UF.IDTIPOEJG
          AND EJG.ANIO = UF.ANIO
          AND EJG.NUMERO = UF.NUMERO)
      AND UF.IDINSTITUCION = ER.IDINSTITUCION
      AND UF.ANIO = ER.ANIO
      AND UF.NUMERO = ER.NUMERO
      AND UF.IDTIPOEJG = ER.IDTIPOEJG
 