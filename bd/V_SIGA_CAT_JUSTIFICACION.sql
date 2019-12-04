create or replace view v_siga_cat_justificacion as
Select CodActuacion,
       CodExpediente,
       Trimestre,
       ICAColegiado,
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
       GuardiaDoble,
       Importe,
       CantidadAsistencias,
       IdInstitucion
 From (Select Fac.IdInstitucion,
              Trim(To_Char(Fac.IdInstitucion, '0000')) -- Instituci�n
                 || Trim(To_Char(Fad.Anio, '0000')) -- A�o
                 || '1' -- Tipo: Actuaciones de oficio
                 || Trim(To_Char(Fad.IdTurno, '0000')) -- Turno
                 || Trim(To_Char(Des.Codigo, '00000')) -- N�mero designaci�n
                 || Trim(To_Char(Fad.NumeroAsunto, '00')) CodActuacion, -- Actuaci�n
              Decode(Ejg.Anio, Null, Null, Ins.IdComision -- C�digo comisi�n
                                              || '-'
                                              || SubStr(Ejg.NumEJG, 5, 5)
                                              || '/'
                                              || SubStr(Ejg.NumEJG, 1, 4)) CodExpediente,
              To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre, -- Trimestre
              Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColegiado, -- Instituci�n
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
              Decode(Juz.CodigoExt2, Null, Null, 'O' || Juz.CodigoExt2) CentroDetencionOrganJudicial,
              Pro.CodigoExt Modulo,
              Null GuardiaDoble,
              To_char(Fad.ImporteFacturado, '9999990D00') Importe,
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
                                             Min(To_Char(Ejg.Anio) || Trim(To_Char(Ejg.NumEJG, '00000'))) NumEJG
                                        From Scs_EJGDesigna Ejd
                                             Inner Join Scs_Ejg Ejg
                                                On Ejg.IdInstitucion = Ejd.IdInstitucion
                                               And Ejg.IdTipoEJG     = Ejd.IdTipoEJG
                                               And Ejg.Anio          = Ejd.AnioEJG
                                               And Ejg.Numero        = Ejd.NumeroEJG
                                       Group By Ejd.IdInstitucion,
                                                Ejd.IdTurno,
                                                Ejd.AnioDesigna,
                                                Ejd.NumeroDesigna) Ejg 
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
                     Left Outer Join (Select IdInstitucion,
                                             Designa_Turno,
                                             Designa_Anio,
                                             Designa_Numero,
                                             Max(Comisaria) Comisaria
                                        From Scs_Asistencia
                                       Group By IdInstitucion,
                                                Designa_Turno,
                                                Designa_Anio,
                                                Designa_Numero) Asi
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
        Where (Fac.IdInstitucion,
               Fac.IdFacturacion) In(Select IdInstitucion,
                                            IdFacturacion
                                       From Fcs_Fact_GrupoFact_Hito
                                      Where IdGrupoFacturacion = 1)
    Union All
       Select Fac.IdInstitucion,
              Trim(To_Char(Fac.IdInstitucion, '0000')) -- Instituci�n
                 || Trim(To_Char(Fac.FechaDesde, 'YYYY')) -- A�o
                 || Decode(Faa.IdInstitucion, Null, '2', '3') -- Tipo: 2- Guardias sin actuaciones, 3- Actuaciones de asistencia
                 || Decode(Faa.IdInstitucion, Null, Trim(To_Char(Fac.IdFacturacion, '0000')), Trim(To_Char(Faa.IdTipo, '0000'))) -- Tipo 2- Facturaci�n, 3- Tipo actuaci�n
                 || Decode(Faa.IdInstitucion, Null, '', Trim(To_Char(Faa.Numero, '00000'))) -- Tipo: 2 '', 3-Numero asistencia
                 || Decode(Faa.IdInstitucion, Null, Trim(To_Char(Apu.IdApunte, '0000000')), Trim(To_Char(Faa.IdActuacion, '00'))) CodActuacion, -- Tipo: 2- Apunte, 3- Numero actuacion
              Decode(Faa.IdInstitucion,
                 Null, Null,
                       Ins.IdComision -- C�digo comisi�n
                          || '-'
                          || Trim(To_Char(Ejg.NumEJG, '00000'))
                          || '/'
                          || Ejg.Anio) CodExpediente,
              To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre, -- Trimestre
              Trim(To_Char(Fac.IdInstitucion, '0000')) ICAColegiado, -- Instituci�n
              Nvl(Col.NComunitario, Col.NColegiado) NumColegiado,
              Per.Nombre NombreAbogado,
              Per.Apellidos1 PrimerApellidoAbogado,
              Per.Apellidos2 SegundoApellidoAbogado,
              'A' AsistenciaDefensa,
              Decode(Hfg.IdTipoGuardia,
                 1, 'VID',
                 5, 'MEN',
                 6, 'EST',
                    'GEN') Turno,
              Upper(SubStr(Suz.Nombre, 1, 3)) AreaPartitJudicial,
              Decode(Faa.IdInstitucion, Null, Null, Decode(Asi.Comisaria, Null, 'O', 'A')) TipoIniciacion,
              'A/'
                 || Trim(To_Char(Nvl(Faa.Numero, 999999), '000000'))
                 || '/'
                 || SubStr(Nvl(Faa.Anio, To_Char(Fac.FechaDesde, 'YYYY')), 3, 2)
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
              (Select Min(Del.CodigoExt)
                 From Scs_DelitosAsistencia Das
                      Inner Join Scs_Delito Del
                         On Del.IdInstitucion = Das.IdInstitucion
                        And Del.IdDelito      = Das.IdDelito
                Where Das.IdInstitucion = Faa.IdInstitucion
                  And Das.Anio          = Faa.Anio
                  And Das.Numero        = Faa.Numero) TipoDelito,
              Null TipoProcedimiento,
              Decode(Act.IdComisaria, Null, Decode(Juz.CodigoExt2, Null, Null, 'O' || Juz.CodigoExt2), 'C' || Com.CodigoExt) CentroDetencionOrganJudicial,
              Case Apu.PrecioAplicado
                   When To_Number(SubStr(Hfg.Hitos, InStr(Hfg.Hitos, '01-') + 3, InStr(Hfg.Hitos, ' ;') - 4))
                        Then Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                             Then Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then '31a'
                                                 Else '31'
                                             End
                                       Else '40b'
                                   End
                             Else Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then 'D31a'
                                                 Else 'S31'
                                             End
                                       Else 'S40b'
                                   End
                         End
                   When To_Number(SubStr(Hfg.Hitos, InStr(Hfg.Hitos, '04-') + 3, InStr(Hfg.Hitos, ' ;', 1, 2) - 16))
                        Then Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                             Then Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then '32a'
                                                 Else '32'
                                             End
                                       Else '40c'
                                   End
                             Else Case When Hfg.IdTipoGuardia <> 1
                                       Then Case When Asi.FechaHora < Fac.FechaDesde
                                                 Then 'D32a'
                                                 Else 'D32'
                                             End
                                       Else 'S40b'
                                   End
                         End
                   Else Case When Nvl(Tot.Importe ,0) = 0
                             Then Case When Nvl(Tot.Cantidad, 0) = 0
                                       Then Case When Hfg.IdTipoGuardia <> 1
                                                 Then 'Guardia sin actuaciones'
                                                 Else '40d'
                                             End
                                       Else Case When Cantidad > SubStr(Hfg.Hitos, InStr(Hfg.Hitos, '46') + 3, 1)
                                                 Then Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                                           Then Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Dob 1'
                                                                     Else '40c'
                                                                 End
                                                           Else Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Dob2'
                                                                     Else 'D40c'
                                                                 End
                                                       End
                                                 Else Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                                           Then Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Sim 1'
                                                                     Else '40b'
                                                                 End
                                                           Else Case When Hfg.IdTipoGuardia <> 1
                                                                     Then 'Res Gua Sim'
                                                                     Else 'S40b'
                                                                 End
                                                       End
                                             End
                                   End
                             Else Case When Apu.PrecioAplicado = Tot.Importe
                                       Then Case When Hfg.IdTipoGuardia <> 1
                                                 Then Case When Apu.IdHito = 9
                                                           Then Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then '30cb'
                                                                     Else '30b'
                                                                 End
                                                           Else Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then '30c'
                                                                     Else '30'
                                                                 End
                                                       End
                                                 Else '40'
                                             End
                                       Else Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                                 Then Case When Hfg.IdTipoGuardia <> 1
                                                           Then Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then '32ca'
                                                                     Else '32a'
                                                                 End
                                                           Else '40c'
                                                       End
                                                 Else Case When Hfg.IdTipoGuardia <> 1
                                                           Then Case When Upper(Hfg.Nombre) Like '%COMPL%'
                                                                     Then 'D32ca'
                                                                     Else 'D32a'
                                                                 End
                                                           Else 'D40c'
                                                       End
                                             End
                                   End
                         End
               End Modulo,
              Null GuardiaDoble,
              To_Char(Case When Apu.PrecioAplicado = Tot.Importe
                           Then Tot.Importe / Tot.Cantidad
                           Else Case When Nvl(Faa.Anio * 10000000 + Faa.Numero * 100 + Faa.IdActuacion, 0) = Nvl(Tot.Primera, 0)
                                     Then Apu.PrecioAplicado
                                     Else 0
                                 End
                       End, '9999990D00') Importe,
              0 CantidadAsistencias
         From Fcs_FacturacionJG Fac
              Inner Join Cen_Institucion Ins
                 On Ins.IdInstitucion = Fac.IdInstitucion
              Inner Join Fcs_Fact_Apunte Apu
                 On Apu.IdInstitucion = Fac.IdInstitucion
                And Apu.IdFacturacion = Fac.IdFacturacion
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
                    Inner Join (Select Gua.IdInstitucion,
                                       Gua.IdTurno,
                                       Gua.IdGuardia,
                                       Gua.Nombre,
                                       Gua.IdTipoGuardia,
                                       ListAgg(Trim(To_Char(Hit.Idhito, '00')) || '-' || Hit.PrecioHito, ' ; ') Within Group (Order By Hit.IdHito) Hitos
                                  From Scs_GuardiasTurno Gua
                                       Inner Join Scs_HitoFacturableGuardia Hit
                                          On Hit.IdInstitucion = Gua.IdInstitucion
                                         And Hit.IdTurno       = Gua.IdTurno
                                         And Hit.IdGuardia     = Gua.IdGuardia
                                 Where Hit.IdHito In(1, 2, 4, 7, 8, 9, 45, 46)
                                 Group By Gua.IdInstitucion,
                                          Gua.IdTurno,
                                          Gua.IdGuardia,
                                          Gua.Nombre,
                                          Gua.IdTipoGuardia) hfg
                       On Hfg.IdInstitucion = Apu.IdInstitucion
                      And Hfg.IdTurno       = Apu.IdTurno
                      And Hfg.IdGuardia     = Apu.IdGuardia
                     Left Outer Join (Select IdInstitucion,
                                             Fecha,
                                             Max(IdPartido) IdPartido
                                        From Scs_CalendarioLaboral
                                       Group By IdInstitucion,
                                                Fecha) Cal
                             On Cal.IdInstitucion = Apu.IdInstitucion
                            And Cal.Fecha         = Apu.FechaInicio
                     Left Outer Join (Select IdInstitucion,
                                             IdFacturacion,
                                             IdApunte,
                                             Count(*) Cantidad,
                                             Sum(PrecioAplicado) Importe,
                                             Min(Anio * 10000000 + Numero * 100 + IdActuacion) Primera
                                        From Fcs_Fact_ActuacionAsistencia
                                       Group By IdInstitucion,
                                                IdFacturacion,
                                                IdApunte) Tot
                       On Tot.IdInstitucion = Apu.IdInstitucion
                      And Tot.IdFacturacion = Apu.IdFacturacion
                      And Tot.IdApunte      = Apu.IdApunte
                     Left Outer Join Fcs_Fact_ActuacionAsistencia Faa
                       On Faa.IdInstitucion = Apu.IdInstitucion
                      And Faa.IdFacturacion = Apu.IdFacturacion
                      And Faa.IdApunte      = Apu.IdApunte
                           Left Outer Join Scs_ActuacionAsistencia Act
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
                           Left Outer Join Scs_Asistencia Asi
                             On Asi.IdInstitucion = Faa.IdInstitucion
                            And Asi.Anio          = Faa.Anio
                            And Asi.Numero        = Faa.Numero
                                 Left Outer Join Scs_PersonaJG Pjg
                                   On Pjg.IdInstitucion = Asi.IdInstitucion
                                  And Pjg.IdPersona     = Asi.IdPersonaJG
                                 Left Outer Join Scs_EJG Ejg
                                   On Ejg.IdInstitucion = Asi.IdInstitucion
                                  And Ejg.IdTipoEJG     = Asi.EJGIdTipoEJG
                                  And Ejg.Anio          = Asi.EJGAnio
                                  And Ejg.Numero        = Asi.EJGNumero
        Where (Fac.IdInstitucion,
               Fac.IdFacturacion) In(Select IdInstitucion,
                                            IdFacturacion
                                       From Fcs_Fact_GrupoFact_Hito
                                      Where IdGrupoFacturacion = 1)
          And Apu.PrecioAplicado > 0)