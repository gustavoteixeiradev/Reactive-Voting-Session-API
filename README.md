[![CI](https://github.com/gustavoteixeiradev/Reactive-Voting-Session-API/actions/workflows/devops.yml/badge.svg)](https://github.com/gustavoteixeiradev/Reactive-Voting-Session-API/actions/workflows/devops.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gustavoteixeiradev_Reactive-Voting-Session-API&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=gustavoteixeiradev_Reactive-Voting-Session-API)
[![GitHub license](https://img.shields.io/github/license/gustavoteixeiradev/Reactive-Voting-Session-API)](https://github.com/gustavoteixeiradev/Reactive-Voting-Session-API/blob/main/LICENSE)
[![Docker Hub](https://img.shields.io/badge/DockerHub-image-007396.svg)](https://hub.docker.com/repository/docker/teixeira963/reactive-voting-session-api/general)

# Reactive Voting Session API

## 🙋‍♂️🙋‍♀️🙋💁💁‍♂️💁‍♀️

### 🧙 API para Sessão de Votação

Esse código é a minha implementação utilizando programação Reativa de um desafio técnico para uma oportunidade como
Desenvolvedor Back-End Java.

<br>

### ✍️ O Desafio

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso,
você precisa criar uma solução back-end para gerenciar essas sessões de votação.

#### 📝 Requisitos

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

✅ 1. Cadastrar uma nova pauta;
<br>
✅ 2. Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada
de abertura ou 1 minutos por default);
<br>
✅ 3. Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id
único e pode votar apenas uma vez por pauta);
<br>
✅ 4. Contabilizar os votos e dar o resultado da votação na pauta.

<br>

### 🌐 Acesse a API

#### 📚🔗 Documentação

[![Swagger](https://img.shields.io/badge/Swagger-link-007396.svg)](https://reactive-voting-session-api.herokuapp.com/swagger-ui.html)

#### 💙🔗 Para acessar o Health Check:

[![Health Check](https://img.shields.io/badge/Actuator-link-007396.svg)](https://reactive-voting-session-api.herokuapp.com/actuator/health)

#### 🚢🔗 Para acessar o relatório do Sonarcloud:

[![Health Check](https://img.shields.io/badge/Sonarcloud-link-007396.svg)](https://sonarcloud.io/summary/overall?id=gustavoteixeiradev_Reactive-Voting-Session-API)

<br>

### 🟦🔸🟡 DevOps 🟢🔸🔶

#### ✅ Pipeline com Github Actions

Sou um grande fã de CI/CD. Por isso, adicionei nesse projeto uma pipeline, utilizando o GitHub Actions, para executar
ações automatizadas que facilitam a minha vida.

![img.png](img.png)

#### #️⃣ A pipeline é constituída das seguintes etapas:

1️⃣ Build: onde é executado o build da aplicação, para validar que ela gera o executável com sucesso;

2️⃣ Unit Tests: onde são executados os testes unitários da aplicação;

3️⃣ Code Quality on Sonar: onde o Sonarcloud faz a análise da qualidade e cobertura do código;

4️⃣ Publish on Docker Hub: nessa etapa cria o container da aplicação e então publica ela no Docker Hub;

5️⃣ Deploy to Heroku: nessa etapa acontece o deploy da aplicação no Heroku;
