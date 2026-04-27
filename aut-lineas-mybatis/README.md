# aut-lineas-mybatis

Reimplementacion base de `aut-lineas-pruebas` usando MyBatis para SQL e HikariCP para pools de conexion.

## Estructura principal

- `application/creation`: caso de uso para crear lineas.
- `application/migration`: caso de uso separado para migrar lineas.
- `controller`: fachada publica del proyecto.
- `domain`: modelos de entrada/salida y conceptos del dominio.
- `mapper`: interfaces MyBatis separadas por base `prod`, `ccard` y migracion.
- `repository`: adaptadores que coordinan sesiones MyBatis.
- `config`: configuracion de Hikari y MyBatis.
- `integration/tecnotree`: puerto para conectar el cliente real de Tecnotree.
- `resources/mappers`: XML de MyBatis con las queries y bloques PL/SQL.

## Creacion de lineas

`MyBatisLineCreator` implementa el flujo de alta:

1. Valida request y aplica defaults por pais.
2. Genera o valida NIM y bill number.
3. Genera handle desde `CCARD.SEQ_PCE_ID`.
4. Inserta datos base en PROD y CCARD con MyBatis.
5. Genera/asigna SIM si `generateSim=true`.
6. Inserta estados, plan, packs basicos y limite de credito para lineas CR.
7. Invoca `TecnoTreeClient` para crear/actualizar subscriber.
8. Ejecuta rollback de PROD/CCARD y Tecnotree si falla el alta.

El cliente real de Tecnotree debe reemplazar `NoOpTecnoTreeClient` implementando `TecnoTreeClient`.

## Configuracion

Completar `src/main/resources/application.properties`:

```properties
lineas.prod.jdbcUrl=jdbc:oracle:thin:@//host:1521/service
lineas.prod.username=USER_PROD
lineas.prod.password=PASSWORD_PROD
lineas.prod.driverClassName=oracle.jdbc.OracleDriver
lineas.prod.maximumPoolSize=5

lineas.ccard.jdbcUrl=jdbc:oracle:thin:@//host:1521/service
lineas.ccard.username=USER_CCARD
lineas.ccard.password=PASSWORD_CCARD
lineas.ccard.driverClassName=oracle.jdbc.OracleDriver
lineas.ccard.maximumPoolSize=5
```

## Uso base

```java
try (LineasFactory factory = new LineasFactory(clienteTecnotreeReal)) {
    LineCreationRequest request = new LineCreationRequest();
    request.setCountryCode(CountryCode.AR);
    LineResult result = factory.lineController().createLine(request);
}
```

## Pendiente deliberado

La migracion de lineas queda separada en `application/migration` y `mapper/migration`, lista para implementar sin mezclarla con el flujo de creacion.
