create or replace view v_siga_cat_devolucion as
Select Pag.IdInstitucion,
       Pag.IdpagosJG,
       Trim(To_Char(Pag.IdInstitucion, '0000')) -- Institución
          || Trim(To_Char(Pag.Fechamodificacion, 'YYYY')) -- Año
          || '4' -- Tipo: Movimientos varios
          || Trim(To_Char(Amv.IdMovimiento, '000000')) -- Numero movimiento
          || Trim(To_Char(Amv.IdAplicacion, '00000')) CodActuacion,-- Numero aplicacion
       Null CodExpediente,
       To_Char(Pag.FechaModificacion, 'Q"T"YYYY') TRIMESTRE, -- Trimestre
       To_Char(Mov.Fechaalta, 'Q"T"YYYY') TrimestreJustificacio,
       Trim(To_Char(Pag.IdInstitucion, '0000')) ICAColegiado, -- Institución
       Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
       Per.Nombre NombreAbogado,
       Per.Apellidos1 PrimerApellidoAbogado,
       Per.Apellidos2 SegundoApellidoAbogado,
       Decode(Asi.Comisaria, Null, 'O', 'A') TipoIniciacion,
       Decode(Upper(SubStr(Mov.Descripcion, 1, 1)),
          'A', 'A/'
                  || To_Char(Asi.Numero, '000000')
                  || '/'
                  || SubStr(Asi.Anio, 3, 2)
                  || '/'
                  || Asi.IdInstitucion,
          'O', 'O/0'
                  || Des.Codigo
                  || '/'
                  || SubStr(Des.Anio, 3, 2)
                  || '/'
                  || Pag.IdInstitucion,
                  'Error') NumDesignacionAbogado,
       Decode(Upper(SubStr(Mov.Descripcion, 1, 1)),
          'A', To_Char(Asi.FechaHora, 'YYYY-MM-DD'),
          'O', (Select Max(To_Char(FechaDesigna, 'YYYY-MM-DD'))
                  From Scs_DesignasLetrado
                 Where IdInstitucion = Des.IdInstitucion
                   And IdTurno       = Des.IdTurno
                   And Anio          = Des.Anio
                   And Numero        = Des.Numero
                   And IdPersona     = Mov.IdPersona),
               'Error') FechaDesignacionAbogado,
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
       Decode(Upper(SubStr(Mov.Descripcion, 1, 1)),
          'A', To_Char(Aca.Fecha, 'YYYY-MM-DD'),
          'O', To_Char(Acd.Fecha, 'YYYY-MM-DD'),
               'Error') FechaActuacion,
       To_Char(Mov.FechaAlta, 'YYYY-MM-DD') FechaDevolucion,
       Null Tipologia,
       Null MotivoMovimiento,
       Decode(Upper(SubStr(Mov.Descripcion, 1, 1)),
          'A', Decode(Gua.IdTipoGuardia,
                  1, Decode(Apu.IdHito,
                        4, '40c',
                        7, '40a',
                        9, '40a',
                       29, '40a',
                       38, '40b',
                       44, '40d'),
                     Decode(Apu.IdHito,
                        4, '32',
                        7, '30',
                        9, '30',
                       29, '30',
                       38, '31')),
          'O', Nvl(Pro.CodigoExt, Pro.Codigo),
               'Error') Modulo,
       To_Char(Amv.ImporteAplicado, '9999990D00') Importe,
       'I-'
          || Trim(To_Char(Mov.IdMovimiento, '000000'))
          || '-'
          || To_Char(Mov.FechaAlta, 'YYYY') CodigoIncidencia
  From Fcs_PagosJG Pag
       Inner Join Fcs_Aplica_MovimientosVarios Amv
          On Amv.IdInstitucion = Pag.IdInstitucion
         And Amv.IdPagosJG     = Pag.IdPagosJG
             Inner Join Fcs_MovimientosVarios Mov
                On Mov.IdInstitucion = Amv.IdInstitucion
               And Mov.IdMovimiento  = Amv.IdMovimiento
                   Inner Join Cen_Colegiado Col
                      On Col.IdInstitucion = Mov.IdInstitucion
                     And Col.IdPersona     = Mov.Idpersona
                   Inner Join Cen_Persona Per
                      On Per.IdPersona = Mov.IdPersona
              Left Outer Join Scs_Asistencia Asi
                On Upper(SubStr(Mov.Descripcion, 1, 1)) = 'A'
               And Asi.IdInstitucion = Mov.IdInstitucion
               And To_Char(Asi.Anio) = Decode(SubStr(Mov.Descripcion, 13, 1), '-', SubStr(Mov.Descripcion, 9, 4), Decode(SubStr(Mov.Descripcion, 14, 1), '/', SubStr(Mov.Descripcion, 4, 4), Null))
               And Trim(To_Char(Asi.Numero, '00000')) = Decode(SubStr(Mov.Descripcion, 13, 1), '-', SubStr(Mov.Descripcion, 3, 5), Decode(SubStr(Mov.Descripcion, 14, 1), '/', SubStr(Mov.Descripcion, 9, 5), Null))
                    Left Outer Join Scs_GuardiasTurno Gua
                      On Gua.IdInstitucion = Asi.IdInstitucion
                     And Gua.IdTurno       = Asi.IdTurno
                     And Gua.IdGuardia     = Asi.IdGuardia
                    Left Outer Join Scs_ActuacionAsistencia Aca
                      On Aca.IdInstitucion = Asi.IdInstitucion
                     And Aca.Anio          = Asi.Anio
                     And Aca.Numero        = Asi.Numero
                     And Aca.IdActuacion   = Decode(SubStr(Mov.Descripcion, 13, 1), '-', SubStr(Mov.Descripcion, 14, 2), Decode(SubStr(Mov.Descripcion, 14, 1), '/', SubStr(Mov.Descripcion, 15, 2), Null))
                         Left Outer Join Fcs_Fact_ActuacionAsistencia Faa
                           On Faa.IdInstitucion = Aca.IdInstitucion
                          And Faa.Anio          = Aca.Anio
                          And Faa.Numero        = Aca.Numero
                          And Faa.IdActuacion   = Aca.IdActuacion
                               Left Outer Join Fcs_Fact_Apunte Apu
                                 On Apu.IdInstitucion = Faa.IdInstitucion
                                And Apu.IdFacturacion = Faa.IdFacturacion
                                And Apu.IdApunte      = Faa.IdApunte
              Left Outer Join Scs_Designa Des
                On Upper(SubStr(Mov.Descripcion, 1, 1)) = 'O'
               And Des.IdInstitucion = Mov.IdInstitucion
               And To_Char(Des.Anio) = Decode(SubStr(Mov.Descripcion, 13, 1), '-', SubStr(Mov.Descripcion, 9, 4), Decode(Substr(Mov.Descripcion, 14, 1), '/', SubStr(Mov.Descripcion, 4, 4), Null))
               And Des.Codigo = Decode(SubStr(Mov.Descripcion, 13, 1), '-', SubStr(Mov.Descripcion, 3, 5), Decode(Substr(Mov.Descripcion, 14, 1), '/', SubStr(Mov.Descripcion, 9, 5), Null))
                    Left Outer Join Scs_ActuacionDesigna Acd
                      On Acd.IdInstitucion = Des.IdInstitucion
                     And Acd.IdTurno       = Des.IdTurno
                     And Acd.Anio          = Des.Anio
                     And Acd.Numero        = Des.Numero
                     And Decode(SubStr(Mov.Descripcion, 13, 1), '-', SubStr(Mov.Descripcion, 14, 2), Decode(SubStr(Mov.Descripcion, 14, 1), '/', SubStr(Mov.Descripcion, 15, 2), Null)) = Acd.NumeroAsunto
                          Left Outer Join Scs_Procedimientos Pro
                            On Pro.IdInstitucion   = Acd.IdInstitucion
                           And Pro.IdProcedimiento = Acd.IdProcedimiento
                    Left Outer Join (Select IdInstitucion,
                                            IdTurno,
                                            Anio,
                                            Numero,
                                            Min(IdPersona) IdPersonaJG
                                       From Scs_DefendidosDesigna
                                      Group By IdInstitucion,
                                               IdTurno,
                                               Anio,
                                               Numero) Def
                      On Def.IdInstitucion = Des.IdInstitucion
                     And Def.IdTurno       = Des.IdTurno
                     And Def.Anio          = Des.Anio
                     And Def.Numero        = Des.Numero
                    Left Outer Join Scs_PersonaJG Pjg
                      On Pjg.IdInstitucion = Nvl(Asi.IdInstitucion, Def.IdInstitucion)
                     And Pjg.IdPersona     = Nvl(Asi.IdPersonaJG, Def.IdPersonaJG)
 Where
   (Pag.IdInstitucion,
        Pag.IdFacturacion) In(Select IdInstitucion,
                                     IdFacturacion
                                From Fcs_Fact_GrupoFact_Hito
                               Where IdGrupoFacturacion = 1)

