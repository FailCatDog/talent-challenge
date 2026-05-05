# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 通用规则
1. 任何时候都需要阅读并遵守本节所有要求，并且在每次对话开头输出一遍。
2. 在所有流程、任务与对话过程中，必须使用 **`memory` MCP** 记录交互节点与关键决策，确保完整可追溯。
3. 所有回答均需使用简体中文。
4. 如有需要可查阅线上资料以辅助开发决策。
5. 非经明确要求禁止编写测试文件与说明文件；若确实需要测试文件，须放入新建的 `test/` 目录且仅允许保留一个长期测试文件，其余临时测试文件在验证通过后必须删除。
6. 当需要删除本地文件（或运行会执行删除操作的脚本）时，必须先说明“我想要删除[文件名]，这些文件原本用于[用途]，现在由于[原因]已经不需要了”或“我要运行[文件名]，这将删除[所有删除的文件名]，这些文件原本用于[用途]，现在由于[原因]已经不需要了”，并获得许可后才能执行。
7. 所有代码中的注释需使用中文进行讲解。
8. 每次工作流完成后，都要查看并更新 `[000]功能模块文档.md`，及时同步功能模块信息，包括简单说下做了什么事，产出的可以供参考查阅的文档有哪些等内容。
9. api文档参考：E:\Code\产教融合项目\揭榜挂帅后端\talent-challenge\project_document\[002]API文档.md，现有数据库表参考E:\Code\产教融合项目\揭榜挂帅后端\talent-challenge\talent_challenge.sql
10. 遵照时间戳原则，将最终确认的详细计划（具体到每一步的详细规划和操作而不止是总结） (todolist) 保存为独立文件至 `/project_document/` 目录中。文件名必须包含唯一标识和简要信息，格式为 `[编号]简要任务描述.md` (例如: `[001]用户登录功能开发.md`)。 需要注意的是，编号为000-099的文档为核心文档，例如api文档、数据库设计文档、项目架构文档等；编号为100-199的文档为阶段性文档，主要包括项目中各个阶段模块的实施方案、技术方案、实现报告之类的内容；编号为200-299的文档为测试修复完善阶段文档，主要为各个模块的功能测试内容或者是bug修复方案、模块完善升级方案、修复报告之类的内容；编号300-399为技术专题文档，包括对某些技术使用的说明教学等内容。对于开发文档内容，在开头你需要标注背景与目标方便翻阅，还要写上目录，如果是开发文档，你还需要将todolist写进去。
11. 每次需要新增/修改接口，都需要及时的修改到E:\Code\产教融合项目\揭榜挂帅后端\talent-challenge\project_document\[002]API文档.md文档中，每个api文档需要包括api说明（包括主体+功能，比如“学生查看项目揭榜详情”），url、请求参数说明、返回说明、请求返回示例等必要内容。
12. 每个完整任务结束后，如项目使用 Git 管理，需要创建提交记录以便追踪。


## 项目概述

TalentChallenge（揭榜挂帅）是一个基于 DDD（领域驱动设计）架构的 Spring Boot 3.4.3 后端项目，使用 Java 17 开发。项目实现了企业项目发布、团队投标、里程碑管理等核心业务功能。

## 技术栈

- **框架**: Spring Boot 3.4.3
- **Java 版本**: 17
- **构建工具**: Maven
- **数据库**: MySQL 8.x
- **ORM**: MyBatis 3.0.4
- **消息队列**: RabbitMQ
- **工具库**: Hutool 5.8.28, Guava, Apache Commons Lang3
- **认证**: JWT (jjwt 0.11.5)
- **JSON**: Fastjson 2.0.28

## 项目结构（DDD 分层架构）

```
TalentChallenge/
├── TalentChallenge-api/          # API 接口定义层
│   └── dto/                      # 数据传输对象
│   └── response/                 # 响应对象
├── TalentChallenge-domain/       # 领域层（核心业务逻辑）
│   ├── bid/                      # 投标领域
│   ├── project/                  # 项目领域
│   ├── team/                     # 团队领域
│   └── user/                     # 用户领域
│   └── [domain]/
│       ├── model/entity/         # 实体对象
│       ├── model/valobj/         # 值对象
│       ├── repository/           # 仓储接口
│       └── service/              # 领域服务
├── TalentChallenge-infrastructure/ # 基础设施层
│   ├── gateway/                  # 外部服务网关
│   ├── mq/                       # 消息队列实现
│   ├── persistent/               # 持久化实现
│   │   ├── dao/                  # MyBatis DAO
│   │   ├── po/                   # 持久化对象
│   │   └── repository/           # 仓储实现
│   └── storage/                  # 文件存储
├── TalentChallenge-trigger/      # 触发器层（应用入口）
│   ├── http/                     # HTTP 控制器
│   ├── job/                      # 定时任务
│   ├── listener/                 # 事件监听器
│   └── mq/                       # MQ 消费者
├── TalentChallenge-types/        # 通用类型定义
└── TalentChallenge-app/          # 应用启动模块
    └── Application.java          # 主启动类
```

## 常用命令

### 构建与运行

```bash
# 编译整个项目
mvn clean install

# 编译并跳过测试
mvn clean install -DskipTests

# 运行应用（开发环境）
cd TalentChallenge-app
mvn spring-boot:run

# 使用指定 profile 运行
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=test
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Docker 构建

```bash
cd TalentChallenge-app
# 构建 Docker 镜像
./build.sh
# 或手动构建
docker build -t system/TalentChallenge-app:1.0-SNAPSHOT -f ./Dockerfile .
```

### 测试

```bash
# 运行所有测试
mvn test

# 运行单个模块测试
mvn test -pl TalentChallenge-domain
```

## 核心领域模型

### 1. Project（项目领域）
- 企业发布项目
- 项目分类管理
- 项目状态流转

### 2. Bid（投标领域）
- 团队投标
- 投标审核
- 中标流程

### 3. Team（团队领域）
- 团队管理
- 团队成员

### 4. User（用户领域）
- 用户认证与授权
- 用户资料管理

### 5. Milestone（里程碑）
- 里程碑模板
- 里程碑截止时间
- 里程碑工作流

## 开发规范

### DDD 分层职责

1. **API 层**: 仅定义接口契约，不包含业务逻辑
2. **Domain 层**: 核心业务逻辑，保持纯粹性，不依赖外部框架
3. **Infrastructure 层**: 实现 Domain 层定义的仓储接口，处理技术细节
4. **Trigger 层**: 处理外部请求，调用 Domain 服务，不包含业务逻辑
5. **Types 层**: 定义通用类型、枚举、常量

### 代码组织原则

- 每个领域模块独立，包含 model、repository、service
- Repository 接口在 domain 层定义，在 infrastructure 层实现
- 使用 PO（Persistent Object）与 Entity 分离，避免领域对象污染
- HTTP 控制器统一放在 trigger/http 目录
- MyBatis Mapper XML 文件放在 TalentChallenge-app/src/main/resources/mybatis/mapper/

### 配置管理

- 主配置: `application.yml`（指定 active profile）
- 环境配置: `application-{profile}.yml` (dev/test/prod)
- 默认 profile: dev
- 生产环境使用 prod profile

### 数据库

- 使用 MyBatis 作为 ORM
- Mapper 扫描路径: `cn.hgy.infrastructure.persistent.dao`
- 连接池: HikariCP
- 数据库名: `talent_challenge`

### 消息队列

- 使用 RabbitMQ
- 启用手动确认模式（acknowledge-mode: manual）
- 配置发布确认和重试机制

## 注意事项

- 修改 Domain 层代码时，确保不引入 Spring 或其他框架依赖
- 新增领域模型时，遵循现有的目录结构（model/entity、model/valobj、repository、service）
- 数据库操作必须通过 Repository 接口，不要在 Service 中直接使用 DAO
- HTTP 接口统一使用 Response 对象包装返回结果
- 文件上传功能在 infrastructure/storage 中实现
- JWT 相关逻辑在 infrastructure/persistent/jwt 中实现
