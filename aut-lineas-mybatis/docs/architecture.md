# Decisiones de estructura

El proyecto original mezcla orquestacion, reglas por pais, SQL crudo, conexiones y llamadas a Tecnotree dentro de managers. Este esqueleto deja esos roles separados:

- La orquestacion vive en `application`.
- Las reglas por pais deben vivir en estrategias/validadores dentro de `domain/country` o `application/common`.
- El acceso a PROD, CCARD y migracion vive en mappers distintos.
- Las llamadas externas viven detras de interfaces en `integration`.

Esta forma permite tener un creador y un migrador compartiendo modelos y repositorios sin que uno dependa internamente del otro.
