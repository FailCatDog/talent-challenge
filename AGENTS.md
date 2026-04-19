# Repository Guidelines

## 项目结构与模块划分
本仓库是基于 Java 17 的多模块 Maven 项目，整体按 DDD 分层组织：

- `TalentChallenge-app`：Spring Boot 启动入口、运行配置、MyBatis XML、测试代码。
- `TalentChallenge-trigger`：HTTP 控制器、拦截器、MQ 消费逻辑。
- `TalentChallenge-domain`：领域实体、值对象、仓储接口、核心服务。
- `TalentChallenge-infrastructure`：DAO、持久化对象、仓储实现、文件存储、JWT。
- `TalentChallenge-api`：对外接口定义、DTO、统一响应对象。
- `TalentChallenge-types`：共享枚举、常量、事件、异常。

运维相关文件位于 `docs/dev-ops/`，数据库脚本位于 `sql/` 与根目录 `talent_challenge.sql`，需求与改造说明集中在 `project_document/`。

## 构建、测试与开发命令
- `mvn clean install`：构建全部模块并执行测试。
- `mvn clean install -DskipTests`：跳过测试执行完整构建。
- `mvn test`：在仓库根目录运行现有测试。
- `mvn test -pl TalentChallenge-app`：仅运行应用模块测试。
- `mvn -pl TalentChallenge-app spring-boot:run -Dspring-boot.run.profiles=dev`：本地启动服务。
- `cd TalentChallenge-app && ./build.sh`：按 `TalentChallenge-app/Dockerfile` 构建镜像。

## 代码风格与命名规范
统一使用 4 空格缩进，包名保持在 `cn.hgy` 下。命名遵循现有约定：`*Controller`、`*Service`、`*Repository`、`*Dao`、`*Entity`、`*DTO`、`*Enum`。HTTP 入口放在 `trigger`，业务规则放在 `domain`，持久化与外部依赖实现放在 `infrastructure`。项目已使用 Lombok，当前未发现统一格式化或 Checkstyle 配置，提交前请保持 import、有序分层和现有代码风格一致。

## 测试规范
当前测试主要位于 `TalentChallenge-app/src/test/java`，使用 Spring Boot Test 和 JUnit。测试类命名为 `*Test.java`，Maven Surefire 已按该模式执行。涉及项目、用户、里程碑、揭榜等核心流程变更时，应同步补充或更新测试，并在提交前执行 `mvn test`。

## 提交与合并请求规范
近期提交以简短、聚焦的消息为主，常见前缀为 `feat:`、`fix:`。建议使用 `类型: 简要说明`，例如 `feat: 新增里程碑自动启动流程`。每次提交只处理一个明确变更。PR 需说明问题背景、影响模块、测试结果、关联文档或 issue；若接口发生变化，补充示例请求/响应或截图。

## 配置与数据说明
环境配置位于 `TalentChallenge-app/src/main/resources/application-{dev,test,prod}.yml`。不要提交密钥、账号或环境专属凭据。若涉及表结构变更，请同步更新 `sql/` 下脚本，或在 `project_document/` 中补充迁移说明与上线注意事项。
