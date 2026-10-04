# Plan: alinear caminito Inscripcion a la entity

> **For Hermes:** Solo plan. No implementar hasta que el usuario lo pida.
> Si se ejecuta después: task-by-task, entity = fuente de verdad.

**Goal:** Dejar DTO → mapper → repo → service → controller de Inscripcion coherentes con `model.Inscripcion` (PK, tipos, nombres, ctor), con el caso de uso `inscribirEstudiante…`, sin inventar Matricula/InscripcionId/LocalDate.

**Architecture:** Capas delgadas Spring Boot 4 + Data JPA. La entity manda. El service orquesta repos + mapper. Errores de negocio = excepciones unchecked + `@ControllerAdvice` (no try/catch en controller). OpenAPI/Swagger es opcional y no es requisito del dominio.

**Tech Stack:** Spring Web, Spring Data JPA, entity actual en `model`, H2/MySQL según props. Opcional: `spring-boot-starter-validation`. OpenAPI (springdoc) solo si se decide documentar.

---

## 1. Fuente de verdad (no negociable)

### Entity `model.Inscripcion`

| Campo | Tipo | Notas |
|-------|------|--------|
| `id` | `Long` | PK **IDENTITY** (autogen). No setear desde API create. |
| `estudiante` | `Estudiante` | FK `estudiante_lu` → PK estudiante = **`lu`** |
| `carrera` | `Carrera` | FK `carrera_id` → PK carrera = **`id`** |
| `anioInscripcion` | `int` | año calendario, no fecha |
| `anioGraduacion` | `Integer` | null si no egresó |
| `antiguedad` | `int` | |
| `graduado` | `boolean` | |

Ctor canónico:

```java
Inscripcion(Estudiante estudiante, Carrera carrera,
            int anioInscripcion, Integer anioGraduacion,
            int antiguedad, boolean graduado)
```

UK: `(estudiante_lu, carrera_id)` — **no** hay `@IdClass` / `InscripcionId`.

### PKs relacionadas

- Estudiante: buscar por **`lu`** → `estudianteRepository.findById(lu)` / `getLu()`
- Carrera: buscar por **`id`** → `carreraRepository.findById(id)` / `getId()`
- Inscripcion: PK repo = **`Long id`** surrogada; unicidad de matrícula = UK o query por par (lu, carreraId)

### Nombres de API / service

Mantener el verbo de negocio:

- método service: `inscribirEstudianteEnCarrera(...)` (o `inscribirEstudiante` si se acorta, pero **no** `matricular`/`Matricula*` en este módulo salvo decisión explícita)
- endpoint: p.ej. `POST /api/v1/inscripciones/inscribir`
- clase controller: `InscripcionController` (con s)
- package: `controller` (no default package)

---

## 2. Decisiones de diseño (antes de codear)

### 2.1 OpenAPI / Swagger vs no documentar

| | **Sin OpenAPI** | **Con OpenAPI (springdoc)** |
|--|------------------|-----------------------------|
| Qué es | Solo código Java + HTTP | Mismo código + annotations + UI `/swagger-ui` y spec `/v3/api-docs` |
| Swagger vs OpenAPI | “Swagger” es el nombre viejo/UI; el estándar actual es **OpenAPI 3**. springdoc genera OpenAPI y muestra Swagger UI. No son dos stacks distintos hoy: es **documentar o no**. |
| Dependencias | Ninguna extra de docs | `springdoc-openapi` (o equivalente Boot 4) en `pom` |
| Imports | No `@Tag`, `@Operation`, `@Schema`, `@ApiResponse` | Esos imports + clases reales |
| Riesgo actual | El código ya usa annotations **sin** dep ni imports → no compila | Hay que agregar dep **y** alinear schemas a tipos reales |
| Valor en TP | Menos ruido; el corrector mira REST + JPA | Útil si la consigna pide doc o Postman/Swagger |
| Recomendación para este arreglo | **Fase 1 sin OpenAPI** (sacar annotations rotas). | **Fase 2 opcional**: reintroducir springdoc cuando el caminito compile. |

**Diferencia práctica:** OpenAPI no cambia la lógica de inscripción ni los tipos de la entity. Solo afecta compilación, `pom`, y metadata de documentación. Si no lo usás, el endpoint funciona igual; si lo usás mal (como ahora), **ni compila**.

### 2.2 try/catch vs excepciones de negocio

| | **try/catch en controller/service “para levantar”** | **Excepciones + handler** |
|--|------------------------------------------------------|---------------------------|
| Flujo feliz | `return ResponseEntity.ok(...)` | igual |
| No encontrado / dup | `try { ... } catch (Exception e) { return 404/409; }` o if + return | `throw EstudianteNotFoundException` / `InscripcionDuplicadaException` |
| Dónde vive el HTTP status | Esparcido en cada método | Un `@ControllerAdvice` / `@ExceptionHandler` |
| Service | Puede devolver `String`/`Optional` y codificar errores en el string (frágil) | Service lanza; no conoce HTTP |
| Controller | Se llena de try/catch y if status | Delgado: llama service, devuelve 200/201 body |
| Consistencia | Fácil devolver mensajes distintos y olvidar un caso | Un formato de error (`ApiError` o body simple) para todos |
| Recomendación | Evitar try/catch genérico en controller | **Preferido:** excepciones unchecked de dominio + advice |

**Importante:** “Levantar excepción” **no requiere** try/catch en el mismo método.  
`throw new X()` propaga; el framework o un `@ExceptionHandler` la convierte en HTTP.  
try/catch solo si **recuperás** el error en el mismo lugar (casi nunca en este TP).

Patrón recomendado:

```text
Service:
  if (estudiante missing) throw EstudianteNotFoundException(lu)
  if (carrera missing) throw CarreraNotFoundException(id)
  if (ya existe par) throw InscripcionDuplicadaException(lu, carreraId)
  save(...); return Response DTO o void

Controller:
  ResponseEntity.ok(service.inscribirEstudianteEnCarrera(dto))
  // sin try/catch

@RestControllerAdvice:
  @ExceptionHandler(EstudianteNotFoundException.class) → 404
  @ExceptionHandler(CarreraNotFoundException.class) → 404
  @ExceptionHandler(InscripcionDuplicadaException.class) → 409
```

Nombres: **Inscripcion**Duplicada, no `MatriculaFoundException`.

### 2.3 Qué manda el Create DTO (mínimo viable)

Para “inscribir estudiante en carrera” alineado a entity:

**Opción A (recomendada, simple):** body solo FKs + defaults en service/mapper

```text
Create(Long estudianteLu, Long carreraId)
// anioInscripcion = Year.now().getValue()
// anioGraduacion = null
// antiguedad = 0
// graduado = false
```

**Opción B:** body completo de atributos de inscripción (sin PK)

```text
Create(Long estudianteLu, Long carreraId, int anioInscripcion,
       Integer anioGraduacion, int antiguedad, boolean graduado)
```

Plan por defecto: **A** (menos campos, calza con el Create actual de 2 IDs). Renombrar `estudianteId` → **`estudianteLu`** para no mentir sobre la PK.

### 2.4 Response DTO

Alinear tipos a entity:

```text
Response(
  Long id,                 // PK inscripción
  Long estudianteLu,       // getLu()
  Long carreraId,          // getId()
  int anioInscripcion,     // NO Long
  Integer anioGraduacion,  // NO LocalDate
  int antiguedad,
  boolean graduado
)
```

Si se quiere response mínimo al principio: al menos no usar `LocalDate` ni getters inventados.

### 2.5 Retorno del service

Hoy: `String` mensaje.  
Preferible para REST limpio:

- `InscripcionDTO.Response` o  
- `void` + 201 sin body  

El plan usa **`InscripcionDTO.Response`** y controller `ResponseEntity.ok` o `created`. Si se insiste en mensaje String, documentarlo y no mentir en OpenAPI.

---

## 3. Estado actual (deuda a borrar)

1. DTO: `@Schema`/`@NotNull`/`LocalDate` sin imports/deps; `anioInscripcion Long`; `anioGraduacion LocalDate`
2. Mapper: ctor 2-args inexistente; `getIdEstudiante`/`getIdCarrera`; no usado por service
3. ServiceImpl: `InscripcionId`, exceptions inexistentes, sin import entity, ctor 2-args, `existsById` con tipo malo, getters inventados
4. Repo: `JpaRepository<Inscripcion, Long>` OK; falta query de UK
5. Controller: default package; OpenAPI sin deps; `ApiError` inexistente; documenta Create como response 200
6. Nombre viejo Matricula / id compuesto: eliminar del diseño

---

## 4. Plan de implementación (orden)

No ejecutar en este documento; checklist para cuando se pida “implementá”.

### Task 1 — Congelar contrato mental

**Objective:** Dejar escrito el mapping final (ya en §1–2).

**Done when:** equipo acuerda Opción A Create + Response con tipos entity + excepciones + sin OpenAPI en fase 1.

---

### Task 2 — `InscripcionDTO` (tipos y nombres)

**Files:**
- Modify: `src/main/java/dto/InscripcionDTO.java`

**Steps:**
1. Quitar todas las annotations OpenAPI/validation **o** agregar deps + imports (fase 1: **quitar**).
2. Create:
   ```java
   public record Create(Long estudianteLu, Long carreraId) {}
   ```
3. Response:
   ```java
   public record Response(
       Long id,
       Long estudianteLu,
       Long carreraId,
       int anioInscripcion,
       Integer anioGraduacion,
       int antiguedad,
       boolean graduado
   ) {}
   ```
4. Sin `LocalDate`. Sin `estudianteId` genérico.

**Verify:** el archivo no referencia símbolos inexistentes.

---

### Task 3 — `InscripcionRepository` (UK)

**Files:**
- Modify: `src/main/java/repository/InscripcionRepository.java`

**Steps:**
1. Mantener `JpaRepository<Inscripcion, Long>`.
2. Agregar existencia por par real, p.ej.:
   ```java
   boolean existsByEstudianteLuAndCarreraId(Long estudianteLu, Long carreraId);
   ```
   (nombres de propiedades path = `estudiante.lu` + `carrera.id` en Spring Data).
3. **No** crear `InscripcionId`.
4. **No** usar `existsById` para detectar dup de matrícula.

**Verify:** compile del repo solo; nombres de propiedades matchean entity graph.

---

### Task 4 — Exceptions de dominio (mínimas)

**Files:**
- Create: `src/main/java/exception/EstudianteNotFoundException.java` (o paquete que usen)
- Create: `src/main/java/exception/CarreraNotFoundException.java`
- Create: `src/main/java/exception/InscripcionDuplicadaException.java`
- Create: `src/main/java/exception/ApiExceptionHandler.java` (`@RestControllerAdvice`)

**Steps:**
1. Runtime exceptions con mensaje claro (lu / carreraId).
2. Handler:
   - not found → 404
   - duplicada → 409
3. Body simple: record `ApiError(String message)` o Map; **no** hace falta OpenAPI.
4. Borrar idea de `MatriculaFoundException`.

**Verify:** clases resolubles por import desde service/controller.

---

### Task 5 — `InscripcionMapper` alineado al ctor

**Files:**
- Modify: `src/main/java/mapper/InscripcionMapper.java`

**Steps:**
1. `toEntity(Create in, Estudiante e, Carrera c)`:
   ```java
   int anio = java.time.Year.now().getValue();
   return new Inscripcion(e, c, anio, null, 0, false);
   ```
2. `toResponse(Inscripcion i)`:
   ```java
   return new InscripcionDTO.Response(
       i.getId(),
       i.getEstudiante().getLu(),
       i.getCarrera().getId(),
       i.getAnioInscripcion(),
       i.getAnioGraduacion(),
       i.getAntiguedad(),
       i.isGraduado()
   );
   ```
3. Eliminar `getIdEstudiante` / `getIdCarrera` / ctor 2-args / `LocalDate` en graduación.

**Verify:** solo usa API real de entity (Lombok getters).

---

### Task 6 — `InscripcionService` + `InscripcionServiceImpl`

**Files:**
- Modify: `src/main/java/service/InscripcionService.java`
- Modify: `src/main/java/service/InscripcionServiceImpl.java`

**Contrato:**

```java
InscripcionDTO.Response inscribirEstudianteEnCarrera(InscripcionDTO.Create in);
```

**Implementación (sin try/catch):**

```text
1. estudiante = estudianteRepository.findById(in.estudianteLu())
     .orElseThrow(() -> new EstudianteNotFoundException(...))
2. carrera = carreraRepository.findById(in.carreraId())
     .orElseThrow(() -> new CarreraNotFoundException(...))
3. if (inscripcionRepository.existsByEstudianteLuAndCarreraId(lu, carreraId))
     throw new InscripcionDuplicadaException(...)
4. entity = mapper.toEntity(in, estudiante, carrera)
5. saved = inscripcionRepository.save(entity)
6. return mapper.toResponse(saved)
```

**Imports obligatorios:** `model.Inscripcion` (si se usa), mapper, exceptions, repos.

**No:**
- `new InscripcionId`
- `existsById(compuesto)`
- `getIdEstudiante` / `getIdCarrera`
- try/catch vacío o que trague errores

**Verify:** service compila; un solo camino de creación (vía mapper).

---

### Task 7 — `InscripcionController`

**Files:**
- Modify: `src/main/java/controller/InscripcionController.java`

**Steps:**
1. `package controller;`
2. Quitar OpenAPI en fase 1 (`@Tag`, `@Operation`, `@ApiResponse`, `@Content`, `@Schema`, `ApiError` swagger).
3. Endpoint:
   ```java
   @PostMapping("/inscribir")
   public ResponseEntity<InscripcionDTO.Response> inscribir(
           @RequestBody InscripcionDTO.Create in) {
       return ResponseEntity.ok(
           inscripcionService.inscribirEstudianteEnCarrera(in));
   }
   ```
4. Sin try/catch; el advice pone 404/409.
5. Opcional después: `@Valid` + starter validation + `@NotNull` en Create.

**Verify:** bean scaneable (`package controller` + `scanBasePackages`).

---

### Task 8 — Compile y smoke

**Commands (Windows / mvn.cmd):**

```text
cd 02-SPRING_REST
mvn.cmd -DskipTests compile
mvn.cmd -DskipTests spring-boot:run -Dspring-boot.run.mainClass=Application
```

**Smoke HTTP (ejemplo):**

```text
POST /api/v1/inscripciones/inscribir
{"estudianteLu": <LU existente del CSV>, "carreraId": <id no inscripto>}
→ 200 + body Response con id generado, anios int/Integer

POST mismo par otra vez → 409
POST lu inexistente → 404
POST carrera inexistente → 404
```

DataLoader ya cargó CSV: usar LUs/carreras reales; evitar el par que ya existe.

---

### Task 9 — (Opcional) OpenAPI de verdad

Solo si se quiere doc:

1. Agregar springdoc compatible con Boot 4 al `pom`.
2. Reponer `@Tag` / `@Operation` / `@ApiResponse` **con imports**.
3. Response 200 schema = `InscripcionDTO.Response`, no `Create`.
4. 404/409 schema = `ApiError` propio (el del advice), no clase fantasma.
5. No cambiar tipos de entity para “quedar lindo” en Swagger.

---

### Task 10 — (Opcional) validation

1. `spring-boot-starter-validation`
2. `@NotNull` en Create + `@Valid` en controller
3. Handler de `MethodArgumentNotValidException` → 400

---

## 5. Archivos tocados (fase 1 mínima)

| Archivo | Acción |
|---------|--------|
| `dto/InscripcionDTO.java` | rewrite tipos/nombres; sin swagger |
| `mapper/InscripcionMapper.java` | ctor 6-args + getters reales |
| `repository/InscripcionRepository.java` | exists por lu+carreraId |
| `service/InscripcionService.java` | firma Response + `inscribirEstudiante…` |
| `service/InscripcionServiceImpl.java` | flujo §Task 6 |
| `controller/InscripcionController.java` | package + endpoint limpio |
| `exception/*` (nuevo) | 3 exceptions + advice |
| `pom.xml` | **no** openapi en fase 1; validation solo si Task 10 |
| `model/Inscripcion.java` | **no tocar** (fuente de verdad) |
| `loader/*` | **no tocar** en este plan |

---

## 6. Qué NO hacer

- No inventar `InscripcionId` / `@IdClass` solo para el exists.
- No renombrar entity a Matricula.
- No usar `LocalDate` para año de graduación.
- No `findById(dni)` para estudiante (PK = LU; DNI es otro campo).
- No try/catch en controller “para devolver string de error”.
- No dejar annotations OpenAPI sin dependencia.
- No cambiar DataLoader ni CSV en este plan.
- No “arreglar” Swagger antes de que compile el dominio.

---

## 7. Respuestas directas a tus preguntas

### ¿Qué diferencia hay si no usamos Swagger en vez de OpenAPI?

En la práctica del proyecto:

- **No usar documentación** = sin springdoc, sin `@Operation`/`@Schema`. Menos deps, compila más simple, el REST igual funciona.
- **Usar OpenAPI (springdoc + Swagger UI)** = misma API HTTP + spec generada + UI. Hay que mantener annotations y deps alineadas a los DTO reales.

“Swagger” vs “OpenAPI” no es una bifurcación de implementación del service: es **documentar o no** (y con qué librería). El error actual no es “elegimos mal entre Swagger y OpenAPI”; es **annotations de doc sin classpath**.

### ¿Y si usamos try/catch para levantar excepciones?

- **try/catch no levanta** excepciones: las **captura**. Para levantar se usa `throw`.
- try/catch en controller/service para armar `ResponseEntity` a mano:
  - funciona, pero ensucia cada endpoint;
  - el service se acopla a HTTP o el controller repite status;
  - fácil tragarse errores inesperados con `catch (Exception)`.
- Mejor: `throw` de negocio en service + `@ExceptionHandler` central.

---

## 8. Criterios de aceptación

- [ ] `mvn.cmd -DskipTests compile` SUCCESS
- [ ] Ninguna referencia a `InscripcionId`, `getIdEstudiante`, `getIdCarrera`, `MatriculaFoundException`, `LocalDate` en graduación
- [ ] Create usa `estudianteLu` + `carreraId`
- [ ] Response usa `int`/`Integer`/`boolean` como la entity + `id` Long
- [ ] Dup UK → 409; missing → 404
- [ ] Método público de negocio: `inscribirEstudiante…`
- [ ] Controller en `package controller`
- [ ] Entity `Inscripcion` sin cambios
- [ ] OpenAPI ausente o completo (nunca a medias)

---

## 9. Riesgos

| Riesgo | Mitigación |
|--------|------------|
| Spring Data method name `existsByEstudianteLuAndCarreraId` no resuelve path | Verificar propiedades; si falla, `@Query` JPQL explícita |
| Boot 4 + springdoc version mismatch en fase 2 | Probar dep con el parent 4.1.1; si duele, dejar doc afuera |
| Cliente Postman viejo manda `estudianteId` | Renombrar y avisar; o `@JsonAlias("estudianteId")` temporal |
| Inscribir en carrera ya cargada por CSV | tests con par libre o carrera nueva |
| Default package Application vs packages | ya hay scanBasePackages; controller debe estar en `controller` |

---

## 10. Open questions (solo si bloquean)

1. ¿Create opción A (defaults) o B (todos los campos)? → plan asume **A**.
2. ¿Response completo o mensaje String? → plan asume **Response DTO**.
3. ¿Fase 2 OpenAPI en este TP? → default **no**, salvo pedido.
4. ¿Paquete `exception` vs `error`? → irrelevante; elegir uno y unificar.

---

## 11. Orden resumido de un renglón

DTO tipos → repo exists UK → exceptions+advice → mapper ctor entity → service `inscribirEstudiante…` → controller package limpio → compile/smoke → (opc) OpenAPI/validation.
