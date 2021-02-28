Cadastro de pessoas com nome e sobrenome

Cadastro das salas do evento, com nome e lotacao
Cadstro do espacos de cafe com lotacao
consulta de cada pessoa
consulta de cada sala e espaco

não funcional

perssistencia de dados
testes unitarios
readme.md


# Backend for Quotas System

![Julius Workflow](https://github.com/dlombello/julius/workflows/Julius%20Workflow/badge.svg?branch=master)

## Propóstio
API para controle de investimentos pessoais com base em sistema de cotização

## Excutando o Banco de Dados

### Criando o disco virtual Docker do Mysql
* **Esse passo deve ser feito apenas uma vez:**
* Primeiro, devemos criar o volume lógico do docker para manter os dados do sistema;

```
docker volume create julius-mysql
```

### Subindo o servidor Mysql no Docker
* Em seguida, devemos executar o container, utilizando o volume criado como referência;
* O comando abaixo, executa o container docker na porta **3306** e com a senha de *root* = **123456**;

```
docker run -d --rm --name julius-mysql-server -v julius-mysql:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=julius mysql:8.0
```

## API Documentation
http://localhost/swagger-ui.html
(REALIZAR LOGIN COM HEADER PRIMEIRO)

```
./gradlew build
docker image rm -f julius && docker build -t julius .
docker stack deploy -c docker-compose.yml julius
```

Para ver os logs:
```
docker service logs julius
```

Parar os serviços:
```
docker stack rm julius
```

```
docker volume create julius-test && 
docker run -d --rm --name julius-mysql-test -v julius-test:/var/lib/mysql -p 3310:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=julius mysql:8.0

docker rm -f julius-mysql-test
docker volume rm julius-test
```