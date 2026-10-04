# Arquitecturas Web

Repositorio de entregas de la materia **Arquitecturas Web** (TUDAI / UNICEN).

Cada carpeta de primer nivel corresponde a un **trabajo práctico independiente**, con su propio código, dependencias, base de datos y documentación.

| Módulo                            | Tema                                           | Documentación                                   |
| --------------------------------- | ---------------------------------------------- | ----------------------------------------------- |
| [00-JDBC](00-JDBC/00-jdbc/)       | CSV, JDBC, DAO y Factory                       | [Ver JDBC](00-JDBC/00-jdbc/README.md)           |
| [01-JPA](01-JPA/01-jpa/)          | CSV, JPA, Repository y Factory                 | [Ver JPA](01-JPA/01-jpa/README.md)              |
| [02-SPRING_REST](02-SPRING_REST/) | CSV, Spring Boot, REST, JPA y capas de servicio | [Ver Spring REST](02-SPRING_REST/README.md)    |

---

## 00-JDBC — Integrador JDBC

**Ubicación:** [`00-JDBC/00-jdbc/`](00-JDBC/00-jdbc/)

Trabajo práctico **enfocado en el acceso y persistencia de datos mediante JDBC**, utilizando archivos **CSV** como fuente de datos.

### Cómo está resuelto

La implementación utiliza los patrones **DAO** y **Factory** para separar el acceso a datos del resto de la aplicación y reducir el acoplamiento con el motor de base de datos. Actualmente, la implementación está realizada para **MySQL**.

También se utiliza **Singleton** para centralizar la gestión de las instancias de Factory y de la conexión.

**Podés ver la solución [acá](00-JDBC/00-jdbc/README.md).**

---

## 01-JPA — Integrador JPA

**Ubicación:** [`01-JPA/01-jpa/`](01-JPA/01-jpa/)

Trabajo práctico **enfocado en el mapeo objeto-relacional con JPA**, registro de estudiantes/carreras, carga desde **CSV** y consultas del enunciado (alta, matrícula, listados y reporte por año).

### Cómo está resuelto

La implementación utiliza los patrones **Repository** y **Factory** para separar el acceso a datos del resto de la aplicación y reducir el acoplamiento con el proveedor JPA / MySQL.

También se utiliza **Singleton** para centralizar el `EntityManagerFactory` (`MySQLFactory`).

La carga inicial de CSV la coordina `DataLoader`; las consultas y el reporte se resuelven en los `*RepositoryImpl` vía JPQL.

**Podés ver la solución [acá](01-JPA/01-jpa/README.md).**

---

## 02-SPRING_REST — Integrador Spring REST

**Ubicación:** [`02-SPRING_REST/`](02-SPRING_REST/)

Trabajo práctico **enfocado en una API REST con Spring Boot**, mismo dominio estudiantes/carreras, carga desde **CSV** y endpoints del enunciado (alta, matrícula, listados, reporte por año).

### Cómo está resuelto

La implementación usa **Controller → Service → Repository** (Spring Data JPA), **DTO + Mapper** hacia la API y **H2** en memoria para la demo local.

La carga inicial de CSV la coordina `DataLoader` (`ApplicationRunner`); las consultas f/g/h viven en los repositorios (JPQL / native) y se exponen por HTTP.

**Podés ver la solución [acá](02-SPRING_REST/README.md).**
