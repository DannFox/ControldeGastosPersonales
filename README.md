# API Gastos Personales

## Descripción general

Este repositorio contiene una solución desarrollada para `.NET 8`. Está pensada para abrirse y ejecutarse desde Visual Studio 2022 o con la CLI de .NET. La solución está organizada para separar responsabilidades (API, lógica de dominio, infraestructura y pruebas) y facilitar pruebas, mantenimiento y despliegue.

## Solución (#solution)

La solución agrupa uno o varios proyectos con responsabilidades claras. A continuación se explica la estructura típica y el rol de cada componente:

- `API / Web`  
  - Proyecto responsable de la exposición de la aplicación (por ejemplo, Web API o UI). Aquí se configuran controladores, rutas, middleware y la inyección de dependencias. Es el punto de entrada para peticiones HTTP.
- `Dominio / Core`  
  - Contiene entidades, reglas de negocio y contratos (interfaces). No debe depender de infraestructuras concretas; es el corazón de la aplicación.
- `Infraestructura`  
  - Implementaciones concretas de repositorios, acceso a datos (EF Core, Dapper, etc.), integración con servicios externos y adaptadores. Implementa los contratos definidos en `Dominio`.
- `Aplicación / Services` (opcional)  
  - Casos de uso, servicios de aplicación y orquestación entre dominio e infraestructura.
- `Tests`  
  - Proyecto(s) de pruebas unitarias e integración que validan la lógica de negocio y los puntos críticos.

Arquitectura y principios aplicados:
- Separación de responsabilidades (SoC) para facilitar pruebas y cambios.
- Inyección de dependencias para sustituir implementaciones en pruebas.
- Configuración por entorno mediante `appsettings.json` y variables de entorno.
- Compatibilidad con contenedores mediante un `Dockerfile` en el proyecto de inicio (opcional).

## Requisitos

- .NET SDK 8.x instalado (`dotnet --info` para verificar).  
- Visual Studio 2022 (actualizado) o cualquier editor que soporte `.NET 8`.  
- Opcional: Docker (si desea crear y ejecutar contenedores).

## Inicio rápido (CLI)

Desde la raíz de la solución:

1. Restaurar dependencias:
2. Compilar:
3. Ejecutar (si hay varios proyectos, indique el proyecto de inicio):
4. Ejecutar pruebas: