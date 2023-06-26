# AccWe Hospital

## Como ejecutar los Dockerfiles

### Dockerfile.maven

```bash
docker build -t spring-container -f Dockerfile.maven . && docker run -d -p 8080:8080 spring-container
```

### Dockerfile.mysql

```bash
docker build -t mysql-container -f Dockerfile.mysql . &&  docker run -d -p 3306:3306 mysql-container
```

## Mejoras de la aplicación no contempladas en el ejercicio

Con esto me refiero a que se podría implementar pero no se puede debido a las normas.

- Separación de capas utilizando servicios casos de uso.
- Utilizar `DTOs` en los controladores.
- Los paquetes de los tests estructurados como los propios de las clases. (Así se prescindiría de tanto `public` en los
  controladores).
- Dejar de usar `@Autowired` y utilizar la injección por constructor.
- Añadir libería de `lombok` para prescindir tanto boilerplate de getters y setters.
- No usar la `database h2` para los tests, debería ser usando el container de mysql para buscar el máximo parecido al
  entorno real de ejecución.