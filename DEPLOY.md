# 项目 Docker 部署指南

## 环境要求

- Docker 20.10+
- Docker Compose 2.0+

## 项目结构

```
e:\AAAAA/
├── backend/              # 后端项目
│   ├── Dockerfile
│   └── .dockerignore
├── front/                # 前端项目
│   ├── Dockerfile
│   ├── nginx.conf
│   └── .dockerignore
├── docker-compose.yml    # 核心部署配置
└── DEPLOY.md             # 本文档
```

## 部署步骤

### 1. 准备阶段（Windows 本地打包）

#### 1.1 打包后端项目

```powershell
cd e:\AAAAA\backend
mvn clean package -DskipTests
```

打包成功后，会生成 `ruoyi-admin/target/ruoyi-admin.jar`

#### 1.2 打包前端项目

```powershell
cd e:\AAAAA\front
npm install pnpm -g
pnpm install
npm run build:prod
```

打包成功后，会生成 `dist` 目录

### 2. 上传文件到 Linux 服务器

将以下文件/目录上传到 Linux 服务器：

```
e:\AAAAA/
├── backend/
│   ├── Dockerfile
│   ├── .dockerignore
│   └── ruoyi-admin/target/ruoyi-admin.jar
├── front/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── .dockerignore
│   └── dist/  # 前端打包产物
├── docker-compose.yml
└── backend/sql/  # 数据库初始化脚本
```

### 3. Linux 服务器部署

#### 3.1 安装 Docker 和 Docker Compose

```bash
# 安装 Docker（Ubuntu/Debian）
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 启动 Docker
sudo systemctl start docker
sudo systemctl enable docker

# 验证安装
docker --version
docker compose version
```

#### 3.2 开始部署

```bash
# 进入项目目录
cd /path/to/your/project

# 启动所有服务
docker compose up -d

# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f
```

### 4. 访问项目

部署成功后，访问：

- 前端：http://服务器IP
- 后端：http://服务器IP:18080

默认账号密码（如项目配置）：
- 账号：admin
- 密码：admin123

## 常用命令

```bash
# 启动服务
docker compose up -d

# 停止服务
docker compose stop

# 重启服务
docker compose restart

# 查看日志
docker compose logs -f
docker compose logs -f backend
docker compose logs -f frontend

# 进入容器
docker exec -it ruoyi-backend /bin/bash
docker exec -it ruoyi-mysql /bin/bash

# 删除所有容器和数据（谨慎使用）
docker compose down -v

# 重新构建镜像
docker compose build --no-cache
docker compose up -d --force-recreate
```

## 数据持久化

- MySQL 数据：`mysql-data` volume
- Redis 数据：`redis-data` volume
- 上传文件：`upload-data` volume

## 配置说明

### 修改数据库密码

编辑 `docker-compose.yml`：
```yaml
environment:
  MYSQL_ROOT_PASSWORD: 你的新密码
  SPRING_DATASOURCE_PASSWORD: 你的新密码
```

### 修改端口

编辑 `docker-compose.yml` 中的 `ports` 配置。

### 修改后端配置

可以挂载配置文件，或者在 `docker-compose.yml` 中通过环境变量覆盖配置。

## 故障排查

### 查看服务状态
```bash
docker compose ps
```

### 查看日志
```bash
# 查看所有日志
docker compose logs

# 查看特定服务日志
docker compose logs backend
docker compose logs mysql
```

### 重启某个服务
```bash
docker compose restart backend
```

## PDF 转换功能注意事项

本项目使用 documents4j 进行 Word/Excel 转 PDF，需要额外注意：

1. **Windows 服务器**：需要安装 Microsoft Office
2. **Linux 服务器**：需要安装 LibreOffice

如果无法安装，可以修改为其他转换方案（如 Spire.Doc/Spire.XLS）。
