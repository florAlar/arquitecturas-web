# Arquitecturas Web

Repositorio de entregas de la materia **Arquitecturas Web** (TUDAI / UNICEN).

Cada carpeta de primer nivel corresponde a un **trabajo práctico independiente**, con su propio código, dependencias, base de datos y documentación.

| Módulo                      | Tema                     | Documentación                         |
| --------------------------- |--------------------------|---------------------------------------|
| [00-JDBC](00-JDBC/00-jdbc/) | CSV, JDBC, DAO y Factory | [Ver JDBC](00-JDBC/00-jdbc/README.md) |

---

## 00-JDBC — Integrador JDBC

**Ubicación:** [`00-JDBC/00-jdbc/`](00-JDBC/00-jdbc/)

Trabajo práctico **enfocado en el acceso y persistencia de datos mediante JDBC**, utilizando archivos **CSV** como fuente de datos.

### Cómo está resuelto

La implementación utiliza los patrones **DAO** y **Factory** para separar el acceso a datos del resto de la aplicación y reducir el acoplamiento con el motor de base de datos. Actualmente, la implementación está realizada para **MySQL**.

También se utiliza **Singleton** para centralizar la gestión de las instancias de Factory y de la conexión.

**Podés ver la solución [acá](00-JDBC/00-jdbc/README.md).**

---

## Próximos módulos

A medida que se sumen nuevos trabajos (`01-…`, etc.), cada uno tendrá su propia carpeta y documentación, y se incorporará a este índice.
