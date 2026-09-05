# Arquitecturas Web

Repositorio de entregas de la materia **Arquitecturas Web** (TUDAI / UNICEN).

Cada carpeta de primer nivel es un **trabajo práctico independiente**: código, dependencias, base de datos y documentación propias. No comparten runtime ni se ejecutan como una sola aplicación.

| Módulo | Tema | Documentación                                                |
|--------|------|--------------------------------------------------------------|
| [00-JDBC](00-JDBC/00-jdbc/README.md) | JDBC, CSV, DAO, Factory | [Documentacion del Práctico JDBC](00-JDBC/00-jdbc/README.md) |

---

## 00-JDBC — Integrador JDBC

**Ubicación:** [`00-JDBC/00-jdbc/`](00-JDBC/00-jdbc/)  
**Detalle completo:** 

### Consigna

1. Crear el esquema de la base de datos mediante **JDBC**.
2. Cargar los datos de los **CSV** con JDBC (lectura con Apache Commons CSV).
3. Obtener el **producto que más recaudó** (recaudación = cantidad vendida × valor).
4. Listar **clientes ordenados** por el monto total facturado a cada uno.

### Cómo está resuelto

Podes ver la solucion en [Acá](00-JDBC/00-jdbc/README.md) (arquitectura, diagrama de clases, patrones, Docker, cómo correr).

**Patrones:** Uso de patrón DAO · Abstract Factory · Singleton de factory por `DBType` y de `Connection`. 
Solo se implementó MySQL

**Entrar al Trabajo JDBC →** [00-JDBC/00-jdbc/README.md](00-JDBC/00-jdbc/README.md)

---

## Próximos módulos

A medida que se sumen trabajos (`01-…`, etc.), cada uno tendrá su carpeta, su README interno y una sección en este índice con consigna + enlace a la resolución.
