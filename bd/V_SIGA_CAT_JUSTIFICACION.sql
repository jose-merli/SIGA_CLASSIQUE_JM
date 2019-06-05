create or replace view v_siga_cat_justificacion as
Select CodActuacion,
       CodExpediente,
       TrimestreAny,
       ICAColgiado,
       NumColegiado,
       NombreAbogado,
       PrimerApellidoAbogado,
       SegundoApellidoAbogado,
       AsistenciaDefensa,
       Turno,
       AreaPartitJudicial,
       TipoIniciacion,
       NumDesignacionAbogado,
       FechaDesignacionAbogado,
       CodTipoIdentificacion,
       Identificacion,
       Nombre,
       PrimerApellido,
       SegundoApellido,
       Sexo,
       FechaActuacion,
       Festivo,
       FueraPlazo,
       CausaNuevaDesigna,
       TipoDelito,
       TipoProcedimiento,
       CentroDetencionOrganJudicial,
       Modulo,
       GardiaDoble,
       Importe,
       CantidadAsistencias,
       idinstitucion,
       IdFacturacion
 From (
Select Fac.IdInstitucion,
       Fac.IdFacturacion,
       Trim(To_Char(Fac.IdInstitucion, '0000')) -- Institución
          || Trim(To_Char(Fad.Anio, '0000')) -- Año
          || '1' -- Tipo: Actuaciones de oficio
          || Trim(To_Char(Fad.IdTurno, '0000')) -- Turno
          || Des.Codigo -- Número designación
          || Trim(To_Char(Fad.NumeroAsunto, '00')) CodActuacion, -- Actuación
       Ins.IdComision -- Código comisión
          || '-'
          || Ejg.NumEJG
          || '/'
          || Ejg.Anio CodExpediente,
       To_Char(Fac.FechaDesde, 'Q"T"YYYY') TrimestreAny, -- Trimestre
       Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColgiado, -- Institución
       Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
       Per.Nombre NombreAbogado,
       Per.Apellidos1 PrimerApellidoAbogado,
       Per.Apellidos2 SegundoApellidoAbogado,
       'D' AsistenciaDefensa,
       Decode(Pro.IdJurisdiccion,
          1, 'PEN',
          2, 'CIV',
          4, 'SOC',
          5, 'ADM',
          7, 'CAS',
          8, 'GEN',
             'err') Turno,
       Upper(SubStr(Suz.Nombre, 1, 3)) AreaPartitJudicial,
       Decode(Nvl(Asi.Comisaria, 0), 0, 'O', 'A') TipoIniciacion,
       'O/0'
          || Des.Codigo
          || '/'
          || SubStr(Des.Anio, 3, 2)
          || '/'
          || Fac.IdInstitucion NumDesignacionAbogado,
       (Select Max(To_Char(FechaDesigna, 'YYYY-MM-DD'))
          From Scs_DesignasLetrado
         Where IdInstitucion = Fad.IdInstitucion
           And IdTurno       = Fad.IdTurno
           And Anio          = Fad.Anio
           And Numero        = Fad.Numero
           And IdPersona     = Fad.IdPersona) FechaDesignacionAbogado,
       Decode(Pjg.IdTipoIdentificacion,
          10, 'NIF',
          20, 'CIF',
          30, 'PAS',
          40, 'NIE',
          50, 'NID') CodTipoIdentificacion,
       Pjg.NIF Identificacion,
       Pjg.Nombre Nombre,
       Pjg.Apellido1 PrimerApellido,
       Pjg.Apellido2 SegundoApellido,
       Pjg.Sexo Sexo,
       To_Char(Fad.FechaActuacion, 'YYYY-MM-DD') FechaActuacion,
       To_Char(Fad.FechaJustificacion, 'YYYY-MM-DD') FechaJustificacionActuacion,
       Case When Cal.Fecha Is Null
            Then Case To_Char(Fad.FechaActuacion, 'D')
                      When '6' Then 'DS'
                      When '7' Then 'DG'
                               Else 'N'
                  End
            Else Case When Cal.IdPartido Is Null
                      Then 'F'
                      Else Case When Cal.IdPartido In(Select IdPartido
                                                        From Scs_SubZonaPartido
                                                       Where IdInstitucion = Suz.IdInstitucion
                                                         And IdZona        = Suz.IdZona
                                                         And IdSubZona     = Suz.IdSubZona)
                                Then 'F'
                                Else 'N'
                            End
                  End
         End Festivo,
       'N' FueraPlazo,
       Case When Fac.IdInstitucion In(Select IdInstitucion
                                        From Fcs_Fact_ActuacionDesigna
                                       Where IdInstitucion  = Fad.IdInstitucion
                                         And IdFacturacion <= Fad.IdFacturacion
                                         And IdTurno        = Fad.IdTurno
                                         And Anio           = Fad.Anio
                                         And Numero         = Fad.Numero
                                         And IdPersona     <> Fad.IdPersona)
            Then 'NA'
            Else Null
        End CausaNuevaDesigna,
       (Select Min(Del.CodigoExt)
          From Scs_DelitosDesigna Dde
               Inner Join Scs_Delito Del
                  On Del.IdInstitucion = Dde.IdInstitucion
                 And Del.IdDelito      = Dde.IdDelito
         Where Dde.IdInstitucion = Fad.IdInstitucion
           And Dde.IdTurno       = Fad.IdTurno
           And Dde.Anio          = Fad.Anio
           And Dde.Numero        = Fad.Numero) TipoDelito,
       Pre.CodigoExt TipoProcedimiento,
       'O' || Juz.CodigoExt CentroDetencionOrganJudicial,
       Pro.Codigo Modulo,
       'N' GardiaDoble,
       Round(Fad.PrecioAplicado * Fad.PorcentajeFacturado / 100, 2) Importe,
       0 CantidadAsistencias
  From Fcs_FacturacionJG Fac
       Inner Join Cen_Institucion Ins
          On Ins.IdInstitucion = Fac.IdInstitucion
       Inner Join Fcs_Fact_ActuacionDesigna Fad
          On Fad.IdInstitucion = Fac.IdInstitucion
         And Fad.IdFacturacion = Fac.IdFacturacion
             Inner Join Scs_Designa Des
                On Des.IdInstitucion = Fad.IdInstitucion
               And Des.IdTurno       = Fad.IdTurno
               And Des.Anio          = Fad.Anio
               And Des.Numero        = Fad.Numero
              Left Outer Join (Select Ejd.IdInstitucion,
                                      Ejd.IdTurno,
                                      Ejd.AnioDesigna Anio,
                                      Ejd.NumeroDesigna Numero,
                                      Ejd.AnioEJG,
                                      Min(Ejg.NumEJG) NumEJG
                                 From Scs_EJGDesigna Ejd
                                      Inner Join Scs_Ejg Ejg
                                         On Ejg.IdInstitucion = Ejd.IdInstitucion
                                        And Ejg.IdTipoEJG     = Ejd.IdTipoEJG
                                        And Ejg.Anio          = Ejd.AnioEJG
                                        And Ejg.Numero        = Ejd.NumeroEJG
                                Group By Ejd.IdInstitucion,
                                         Ejd.IdTurno,
                                         Ejd.AnioDesigna,
                                         Ejd.NumeroDesigna,
                                         Ejd.AnioEJG
                               Having Ejd.AnioEJG = Min(Ejd.AnioEJG)) Ejg
                On Ejg.IdInstitucion = Fad.IdInstitucion
               And Ejg.IdTurno       = Fad.IdTurno
               And Ejg.Anio          = Fad.Anio
               And Ejg.Numero        = Fad.Numero
             Inner Join Cen_Colegiado Col
                On Col.IdInstitucion = Fad.IdInstitucion
               And Col.IdPersona     = Fad.IdPersona
             Inner Join Cen_Persona Per
                On Per.IdPersona = Fad.IdPersona
             Inner Join Scs_Procedimientos Pro
                On Pro.IdInstitucion   = Fad.IdInstitucion
               And Pro.IdProcedimiento = Fad.CodigoProcedimiento
             Inner Join Scs_Turno Tur
                On Tur.IdInstitucion = Fad.IdInstitucion
               And Tur.IdTurno       = Fad.IdTurno
                   Inner Join Scs_SubZona Suz
                      On Suz.IdInstitucion = Tur.IdInstitucion
                     And Suz.IdZona        = Tur.IdZona
                     And Suz.IdSubZona     = Tur.IdSubZona
              Left Outer Join Scs_Asistencia Asi
                On Asi.IdInstitucion  = Fad.IdInstitucion
               And Asi.Designa_Turno  = Fad.IdTurno
               And Asi.Designa_Anio   = Fad.Anio
               And Asi.Designa_Numero = Fad.Numero
              Left Outer Join (Select IdInstitucion,
                                      IdTurno,
                                      Anio,
                                      Numero,
                                      Min(IdPersona) IdPersona
                                 From Scs_DefendidosDesigna
                                Group By IdInstitucion,
                                         IdTurno,
                                         Anio,
                                         Numero) Def
                On Def.IdInstitucion = Fad.IdInstitucion
               And Def.IdTurno       = Fad.IdTurno
               And Def.Anio          = Fad.Anio
               And Def.Numero        = Fad.Numero
                   Left Outer Join Scs_PersonaJG Pjg
                     On Pjg.IdInstitucion = Def.IdInstitucion
                    And Pjg.IdPersona     = Def.IdPersona
              Left Outer Join (Select IdInstitucion,
                                      Fecha,
                                      Max(IdPartido) IdPartido
                                 From Scs_CalendarioLaboral
                                Group By IdInstitucion,
                                         Fecha) Cal
                      On Cal.IdInstitucion = Fad.IdInstitucion
                     And Cal.Fecha         = Fad.FechaActuacion
             Inner Join Scs_Procedimientos Pro
                On Pro.IdInstitucion   = Fad.IdInstitucion
               And Pro.IdProcedimiento = Fad.CodigoProcedimiento
             Inner Join Scs_ActuacionDesigna Act
                On Act.IdInstitucion = Fad.IdInstitucion
               And Act.IdTurno       = Fad.IdTurno
               And Act.Anio          = Fad.Anio
               And Act.Numero        = Fad.Numero
               And Act.NumeroAsunto  = Fad.NumeroAsunto
                    Left Outer Join Scs_Pretension Pre
                      On Pre.Idinstitucion = Act.IdInstitucion
                     And Pre.IdPretension  = Act.IdPretension
                    Left Outer Join Scs_Juzgado Juz
                      On Juz.IdInstitucion = Act.IdInstitucion_Juzg
                     And Juz.IdJuzgado     = Act.IdJuzgado
Union All
Select Fac.IdInstitucion,
       Fac.IdFacturacion,
       Trim(To_Char(Fac.IdInstitucion, '0000')) -- Institución
          || Trim(To_Char(Fac.FechaDesde, 'YYYY')) -- Año
          || '2' -- Tipo: Guardias sin actuaciones
          || Trim(To_Char(Fac.IdFacturacion, '0000')) -- Facturacion
          || Trim(To_Char(Apu.IdApunte, '0000000')) CodActuacion, -- Apunte
       Null CodExpediente,
       To_Char(Fac.FechaDesde, 'Q"T"YYYY') TrimestreAny, -- Trimestre
       Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColegiado, -- Institución
       Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
       Per.Nombre NombreAbogado,
       Per.Apellidos1 PrimerApellidoAbogado,
       Per.Apellidos2 SegundoApellidoAbogado,
       'A' AsistenciaDefensa,
       Null Turno,
       Upper(SubStr(Suz.Nombre, 1, 3)) AreaPartitJudicial,
       Null TipoIniciacion,
       'A/999999/'
          || To_Char(Apu.FechaInicio, 'YY')
          || '/'
          || Fac.IdInstitucion NumDesignacionAbogado,
       Null FechaDesignacionAbogado,
       'NID' CodTipoIdentificacion,
       Null Identificacion,
       'NID' Nombre,
       'NID' PrimerApellido,
       Null SegundoApellido,
       Null Sexo,
       To_Char(Apu.FechaInicio, 'YYYY-MM-DD') FechaActuacion,
       To_Char(Apu.FechaInicio, 'YYYY-MM-DD') FechaJustificacionActuacion,
       Case When Cal.Fecha Is Null
            Then Case To_Char(Apu.FechaInicio, 'D')
                      When '6' Then 'DS'
                      When '7' Then 'DG'
                               Else 'N'
                  End
            Else Case When Cal.IdPartido Is Null
                      Then 'F'
                      Else Case When Cal.IdPartido In(Select IdPartido
                                                        From Scs_SubZonaPartido
                                                       Where IdInstitucion = Suz.IdInstitucion
                                                         And IdZona        = Suz.IdZona
                                                         And IdSubZona     = Suz.IdSubZona)
                                Then 'F'
                                Else 'N'
                            End
                  End
         End Festivo,
       'N' FueraPlazo,
       Null CausaNuevaDesigna,
       Null TipoDelito,
       Null TipoProcedimiento,
       Null CentroDetencionOrganJudicial,
       Decode(Gua.IdTipoGuardia, 1, '40d', '31') Modulo,
       'N' GardiaDoble,
       Apu.PrecioAplicado Importe,
       0 CantidadAsistencias
  From Fcs_FacturacionJG Fac
       Inner Join Fcs_Fact_Apunte Apu
          On Apu.IdInstitucion = Fac.IdInstitucion
         And Apu.IdFacturacion = Fac.IdFacturacion
              Left Outer Join Fcs_Fact_ActuacionAsistencia Faa
                On Faa.IdInstitucion = Apu.IdInstitucion
               And Faa.IdFacturacion = Apu.IdFacturacion
               And Faa.IdApunte      = Apu.IdApunte
             Inner Join Cen_Colegiado Col
                On Col.IdInstitucion = Apu.IdInstitucion
               And Col.IdPersona     = Apu.IdPersona
             Inner Join Cen_Persona Per
                On Per.IdPersona = Apu.IdPersona
             Inner Join Scs_Turno Tur
                On Tur.IdInstitucion = Apu.IdInstitucion
               And Tur.IdTurno       = Apu.IdTurno
                   Inner Join Scs_SubZona Suz
                      On Suz.IdInstitucion = Tur.IdInstitucion
                     And Suz.IdZona        = Tur.IdZona
                     And Suz.IdSubZona     = Tur.IdSubZona
             Inner Join Scs_GuardiasTurno Gua
                On Gua.IdInstitucion = Apu.IdInstitucion
               And Gua.IdTurno       = Apu.IdTurno
               And Gua.IdGuardia     = Apu.IdGuardia
              Left Outer Join (Select IdInstitucion,
                                      Fecha,
                                      Max(IdPartido) IdPartido
                                 From Scs_CalendarioLaboral
                                Group By IdInstitucion,
                                         Fecha) Cal
                      On Cal.IdInstitucion = Apu.IdInstitucion
                     And Cal.Fecha         = Apu.FechaInicio
 Where Apu.PrecioAplicado > 0
   And Faa.IdInstitucion Is Null
Union All
Select Fac.IdInstitucion,
       Fac.IdFacturacion,
       Trim(To_Char(Fac.IdInstitucion, '0000')) -- Institución
          || Trim(To_Char(Fac.FechaDesde, 'YYYY')) -- Año
          || '3' -- Tipo: Actuaciones de asistencia
          || Trim(To_Char(Faa.IdTipo, '0000')) -- Tipo actuación
          || Trim(To_Char(Faa.Numero, '00000')) -- Numero asistencia
          || Trim(To_Char(Faa.IdActuacion, '00')) CodActuacion, -- Numero actuacion
       Ins.IdComision -- Código comisión
          || '-'
          || Ejg.NumEJG
          || '/'
          || Ejg.Anio CodExpediente,
       To_Char(Fac.FechaDesde, 'Q"T"YYYY') TrimestreAny, -- Trimestre
       Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColegiado, -- Institución
       Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
       Per.Nombre NombreAbogado,
       Per.Apellidos1 PrimerApellidoAbogado,
       Per.Apellidos2 SegundoApellidoAbogado,
       'A' AsistenciaDefensa,
       Decode(Gua.IdTipoGuardia,
          1, 'VID',
          5, 'MEN',
          6, 'EST',
             'GEN') Turno,
       Upper(SubStr(Suz.Nombre, 1, 3)) AreaPartitJudicial,
       Decode(Asi.Comisaria, Null, 'O', 'A') TipoIniciacion,
       'A/'
          || To_Char(Faa.Numero, '000000')
          || '/'
          || SubStr(Faa.Anio, 3, 2)
          || '/'
          || Fac.IdInstitucion NumDesignacionAbogado,
       To_Char(Asi.FechaHora, 'YYYY-MM-DD') FechaDesignacionAbogado,
       Decode(Pjg.IdTipoIdentificacion,
          10, 'NIF',
          20, 'CIF',
          30, 'PAS',
          40, 'NIE',
          50, 'NID') CodTipoIdentificacion,
       Pjg.NIF Identificacion,
       Pjg.Nombre Nombre,
       Pjg.Apellido1 PrimerApellido,
       Pjg.Apellido2 SegundoApellido,
       Pjg.Sexo Sexo,
       To_Char(Faa.FechaActuacion, 'YYYY-MM-DD') FechaActuacion,
       To_Char(Faa.FechaJustificacion, 'YYYY-MM-DD') FechaJustificacionActuacion,
       Case When Cal.Fecha Is Null
            Then Case To_Char(Faa.FechaActuacion, 'D')
                      When '6' Then 'DS'
                      When '7' Then 'DG'
                               Else 'N'
                  End
            Else Case When Cal.IdPartido Is Null
                      Then 'F'
                      Else Case When Cal.IdPartido In(Select IdPartido
                                                        From Scs_SubZonaPartido
                                                       Where IdInstitucion = Suz.IdInstitucion
                                                         And IdZona        = Suz.IdZona
                                                         And IdSubZona     = Suz.IdSubZona)
                                Then 'F'
                                Else 'N'
                            End
                  End
         End Festivo,
       'N' FueraPlazo,
       Null CausaNuevaDesigna,
       (Select Min(Del.CodigoExt)
          From Scs_DelitosAsistencia Das
               Inner Join Scs_Delito Del
                  On Del.IdInstitucion = Das.IdInstitucion
                 And Del.IdDelito      = Das.IdDelito
         Where Das.IdInstitucion = Faa.IdInstitucion
           And Das.Anio          = Faa.Anio
           And Das.Numero        = Faa.Numero) TipoDelito,
       Null TipoProcedimiento,
       Decode(Act.IdComisaria, Null, 'O' || Juz.CodigoExt, 'C' || Com.CodigoExt) CentroDetencionOrganJudicial,
        Case Apu.IdHito
             When  4 Then Case When Gua.IdTipoGuardia = 1
                               Then Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                                         Then '40c'
                                         Else 'D40c'
                                     End
                               Else Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                                         Then '32'
                                         Else 'D32'
                                     End
                           End
             When  7 Then Case When Gua.IdTipoGuardia = 1 --Violencia
                               Then '40'
                               Else Case When Upper(Gua.Nombre) Like '%COMPL%' -- Complemento
                                         Then '30c'
                                         Else '30'
                                     End
                           End
             When  9 Then Case When Gua.IdTipoGuardia = 1 --Violencia
                               Then '40'
                               Else Case When Upper(Gua.Nombre) Like '%COMPL%' -- Complemento
                                         Then '30cb'
                                         Else '30b'
                                     End
                           End
             When 29 Then Case When Gua.IdTipoGuardia = 1 --Violencia
                               Then '40'
                               Else Case When Upper(Gua.Nombre) Like '%COMPL%' -- Complemento
                                         Then '30c'
                                         Else '30'
                                     End
                           End
             When 38 Then Case When Gua.IdTipoGuardia = 1
                               Then Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                                         Then '40b'
                                         Else 'S40b'
                                     End
                               Else Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                                         Then '31'
                                         Else 'S31'
                                     End
                           End
             When 44 Then Case When Gua.IdTipoGuardia = 1
                               Then Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                                         Then '40b'
                                         Else 'S40b'
                                     End
                               Else Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                                         Then '31'
                                         Else 'S31'
                                     End
                           End
             When 49 Then Case When Gua.IdTipoGuardia = 1
                               Then 'S40b'
                               Else 'S31'
                           End
                     Else 'Error'
         End Modulo,
        Decode(Apu.IdHito, 44, '', '') GuardiaDoble,
        Case When Apu.IdHito In (7)
             Then Case When Apu.PrecioAplicado < Faa.PrecioAplicado
                       Then Apu.PrecioAplicado
                       Else Faa.PrecioAplicado
                   End
             Else Case When Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion = Tot.Primera
                       Then Apu.PrecioAplicado
                       Else 0
                   End
         End Importe,
       0 CantidadAsistencias
  From Fcs_FacturacionJG Fac
       Inner Join Cen_Institucion Ins
          On Ins.IdInstitucion = Fac.IdInstitucion
       Inner Join Fcs_Fact_Apunte Apu
          On Apu.IdInstitucion = Fac.IdInstitucion
         And Apu.IdFacturacion = Fac.IdFacturacion
             Inner Join (Select IdInstitucion,
                                IdFacturacion,
                                IdApunte,
                                Min(Anio * 10000000 + Numero * 100 + IdActuacion) Primera
                           From Fcs_Fact_ActuacionAsistencia
                          Group By IdInstitucion,
                                   IdFacturacion,
                                   IdApunte) Tot
                On Tot.IdInstitucion = Apu.IdInstitucion
               And Tot.IdFacturacion = Apu.IdFacturacion
               And Tot.IdApunte      = Apu.IdApunte
             Inner Join Fcs_Fact_ActuacionAsistencia Faa
                On Faa.IdInstitucion = Apu.IdInstitucion
               And Faa.IdFacturacion = Apu.IdFacturacion
               And Faa.IdApunte      = Apu.IdApunte
                   Inner Join Scs_ActuacionAsistencia Act
                      On Act.IdInstitucion = Faa.IdInstitucion
                     And Act.Anio          = Faa.Anio
                     And Act.Numero        = Faa.Numero
                     And Act.Idactuacion   = Faa.IdActuacion
                          Left Outer Join Scs_Comisaria Com
                            On Com.IdInstitucion = Act.IdInstitucion
                           And Com.IdComisaria   = Act.IdComisaria
                          Left Outer Join Scs_Juzgado Juz
                            On Juz.IdInstitucion = Act.IdInstitucion
                           And Juz.IdJuzgado     = Act.IdJuzgado
                   Inner Join Scs_Asistencia Asi
                      On Asi.IdInstitucion = Faa.IdInstitucion
                     And Asi.Anio          = Faa.Anio
                     And Asi.Numero        = Faa.Numero
                          Left Outer Join Scs_EJG Ejg
                            On Ejg.IdInstitucion = Asi.IdInstitucion
                           And Ejg.IdTipoEJG     = Asi.EJGIdTipoEJG
                           And Ejg.Anio          = Asi.EJGAnio
                           And Ejg.Numero        = Asi.EJGNumero
                         Inner Join Scs_Turno Tur
                            On Tur.IdInstitucion = Asi.IdInstitucion
                           And Tur.IdTurno       = Asi.IdTurno
                               Inner Join Scs_SubZona Suz
                                  On Suz.IdInstitucion = Tur.IdInstitucion
                                 And Suz.IdZona        = Tur.IdZona
                                 And Suz.IdSubZona     = Tur.IdSubZona
                         Inner Join Scs_GuardiasTurno Gua
                            On Gua.IdInstitucion = Asi.IdInstitucion
                           And Gua.IdTurno       = Asi.IdTurno
                           And Gua.IdGuardia     = Asi.IdGuardia
                         Inner Join Scs_PersonaJG Pjg
                            On Pjg.IdInstitucion = Asi.IdInstitucion
                           And Pjg.IdPersona     = Asi.IdPersonaJG
                   Inner Join Cen_Colegiado Col
                      On Col.IdInstitucion = Faa.IdInstitucion
                     And Col.IdPersona     = Faa.IdPersona
                   Inner Join Cen_Persona Per
                      On Per.IdPersona = Faa.IdPersona
                    Left Outer Join (Select IdInstitucion,
                                            Fecha,
                                            Max(IdPartido) IdPartido
                                       From Scs_CalendarioLaboral
                                      Group By IdInstitucion,
                                               Fecha) Cal
                            On Cal.IdInstitucion = Faa.IdInstitucion
                           And Cal.Fecha         = Faa.FechaActuacion
)

