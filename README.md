# Arquitecturas Web

Repositorio de entregas de la materia **Arquitecturas Web** (TUDAI / UNICEN).

Cada carpeta de primer nivel es un **trabajo práctico independiente**: código, dependencias, base de datos y documentación propias. No comparten runtime ni se ejecutan como una sola aplicación.

| Módulo | Tema | Documentación |
|--------|------|----------------|
| [00-JDBC](00-JDBC/00-jdbc/README.md) | JDBC, CSV, DAO, Factory | [README del módulo](00-JDBC/00-jdbc/README.md) |

---

## 00-JDBC — Integrador JDBC

**Ubicación:** [`00-JDBC/00-jdbc/`](00-JDBC/00-jdbc/)  
**Detalle completo:** [README interno](00-JDBC/00-jdbc/README.md) (arquitectura, diagrama de clases, patrones, Docker, cómo correr).

### Consigna

1. Crear el esquema de la base de datos mediante **JDBC**.
2. Cargar los datos de los **CSV** con JDBC (lectura con Apache Commons CSV).
3. Obtener el **producto que más recaudó** (recaudación = cantidad vendida × valor).
4. Listar **clientes ordenados** por el monto total facturado a cada uno.

### Cómo está resuelto

| Punto | Resolución (resumen) |
|-------|----------------------|
| 1. Esquema | `DBInitializer.initialize`: `DROP` + `CREATE` de `Cliente`, `Producto`, `Factura`, `Factura_Producto` vía JDBC. |
| 2. Carga CSV | `DBInitializer.loadData` + Commons CSV; inserts a través de los DAO MySQL. Datos en `src/main/resources/CSV/`. |
| 3. Producto top | `ProductoDAO.getProdMasRecaudado` → `MYSQLProductoDAO` (`SUM(cantidad × valor)`). |
| 4. Clientes | `ClienteDAO.getClientesPorFacturacion` → `MYSQLClienteDAO` (total facturado desc → `ClienteDTO`). |
| Orquestación | `Main` pide `DAOFactory` / `MySQLDAOFactory` (conexión + bootstrap) y ejecuta los reportes 3 y 4. |
| MySQL local | [`docker-compose.yml`](00-JDBC/00-jdbc/docker-compose.yml) (`localhost:3306`, DB `integrador1`). |

**Patrones:** DAO · Abstract Factory (`DAOFactory` / `MySQLDAOFactory`) · Singleton de factory por `DBType` y de `Connection`.

**Entrar al módulo →** [00-JDBC/00-jdbc/README.md](00-JDBC/00-jdbc/README.md)

---

## Próximos módulos

A medida que se sumen trabajos (`01-…`, etc.), cada uno tendrá su carpeta, su README interno y una sección en este índice con consigna + enlace a la resolución.
