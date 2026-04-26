# TrailMark - 离线 GPS 登山记录应用 | Offline GPS Hiking App

[中文](#中文版本) | [English](#english-version)

---

## 中文版本

### 📱 项目概述

TrailMark 是一款为户外爱好者量身打造的原生 Android 应用，完全离线运行，无需任何在线地图服务。通过自定义 Canvas 实现路径绘制，支持 GPS 轨迹追踪、多维度打卡记录和本地数据存储。

**适用场景：**
- 🏔️ 高海拔山区（如玉龙雪山、梅里雪山）
- 🌄 无网络覆盖区域
- 🚶 长时间户外徒步

### ✨ 核心功能

#### 1. 活跃登山追踪 📍
- 实时仪表板：距离、海拔、配速显示
- 后台保活：前台服务 + 持久化通知
- 高精度定位：低功耗 GPS 追踪

#### 2. Canvas 路径可视化 🎨
- 自定义路径绘制：蓝色路径、绿色起点、红色终点
- 打卡标记：根据能量等级改变颜色
- 完全离线：无需任何在线服务

#### 3. 多维打卡 & 日志 📸
- 拍照记录：CameraX 集成
- 能量等级：1-5 级评分
- 身体状况标签和日志文本

#### 4. 历史数据管理 📋
- 登山记录列表
- 详细路线回顾
- SQLite 本地存储

### 🛠️ 技术栈

**框架与语言**
- Kotlin + Jetpack Compose + Material Design 3
- MVVM 架构模式

**核心库**
- Room Database (SQLite)
- Kotlin Coroutines + Flow
- FusedLocationProviderClient
- CameraX + Coil
- Accompanist Permissions

### 🗄️ 数据库设计

```sql
-- 登山会话
CREATE TABLE trek_sessions (
  id INTEGER PRIMARY KEY,
  routeName TEXT,
  startTime LONG,
  endTime LONG,
  totalDistance DOUBLE
)

-- GPS 路径点
CREATE TABLE waypoints (
  id INTEGER PRIMARY KEY,
  trekSessionId LONG,
  latitude DOUBLE,
  longitude DOUBLE,
  altitude DOUBLE,
  timestamp LONG
)

-- 打卡日志
CREATE TABLE check_in_logs (
  id INTEGER PRIMARY KEY,
  trekSessionId LONG,
  latitude DOUBLE,
  longitude DOUBLE,
  photoUri TEXT,
  journalText TEXT,
  energyLevel INTEGER
)
```

### 🚀 快速开始

**前置需求**
- Android Studio Flamingo+
- JDK 11+
- Android SDK API 26+

**构建和运行**
```bash
git clone https://github.com/Nine-CODE-bit/TrailMark.git
cd TrailMark
./gradlew clean build
# 在 Android Studio 中打开并按 Shift + F10 运行
```

### ⚙️ 环境配置

**Java Home 设置** (gradle.properties)
```properties
org.gradle.java.home=E:\AndroidStudio\jbr
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
org.gradle.parallel=true
org.gradle.caching=true
```

**权限声明** (AndroidManifest.xml)
```xml
<!-- 定位权限 -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- 相机权限 -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- 存储权限 -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- 后台定位服务 -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
```

### 📋 依赖库

```gradle
dependencies {
  // Compose UI
  implementation("androidx.compose.ui:ui:1.6.0")
  implementation("androidx.compose.material3:material3:1.1.0")
  
  // Location Services
  implementation("com.google.android.gms:play-services-location:21.3.0")
  
  // Room Database
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  ksp("androidx.room:room-compiler:2.6.1")
  
  // CameraX
  implementation("androidx.camera:camera-core:1.3.1")
  implementation("androidx.camera:camera-camera2:1.3.1")
  implementation("androidx.camera:camera-lifecycle:1.3.1")
  
  // Image Loading
  implementation("io.coil-kt:coil-compose:2.7.0")
  
  // Permissions
  implementation("com.google.accompanist:accompanist-permissions:0.36.0")
  
  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}
```

### 🎯 开发计划

| 周数 | 任务 | 状态 |
|------|------|------|
| 第1周 | 数据库 + 后台定位 | ✅ 完成 |
| 第2周 | Canvas 绘制 + 实时更新 | 🔄 进行中 |
| 第3周 | CameraX + 打卡系统 | 📋 计划中 |
| 第4周 | 完整 UI + 历史记录 | 📋 计划中 |
| 第5周 | 测试 + 优化 | 📋 计划中 |

### 🔍 核心挑战

**1. 后台定位限制**
- 挑战：Android 系统对后台定位访问限制严格
- 解决：前台服务 + 持久化通知

**2. 图片内存管理**
- 挑战：打卡照片可能导致内存溢出
- 解决：Coil 压缩 + 缓存策略

**3. Canvas 绘制性能**
- 挑战：超长路线可能卡顿
- 解决：轨迹简化 + 分批渲染

### 📁 项目结构

```
TrailMark/
├── app/src/main/java/com/example/trailmark/
│   ├── data/
│   │   ├── entity/              # 数据实体
│   │   ├── dao/                 # DAO 接口
│   │   ├── database/            # 数据库配置
│   │   └── repository/          # 仓库模式
│   ├── ui/
│   │   ├── screens/             # Compose 屏幕
│   │   ├── components/          # UI 组件
│   │   ├── viewmodel/           # ViewModel
│   │   └── theme/               # 主题配置
│   ├── service/                 # 后台服务
│   ├── util/                    # 工具类
│   └── MainActivity.kt
├── gradle/
│   └── libs.versions.toml       # 依赖版本
├── build.gradle.kts             # Gradle 配置
└── README.md
```

### 📄 许可证
MIT License

### 👤 开发者
Nine-CODE-bit

---

## English Version

### 📱 Project Overview

TrailMark is a native Android application designed for outdoor enthusiasts, running completely offline without any online map services. It features custom Canvas-based path drawing, GPS trajectory tracking, multi-dimensional check-in recording, and local data storage.

**Use Cases:**
- 🏔️ High-altitude mountain regions
- 🌄 Areas without network coverage
- 🚶 Long-duration hiking trips

### ✨ Core Features

#### 1. Active Trek Tracking 📍
- Real-time dashboard with distance, altitude, pace
- Background service with persistent notification
- Low-power, high-precision GPS tracking

#### 2. Canvas Path Visualization 🎨
- Custom path drawing with blue lines, green start, red end
- Color-coded check-in markers based on energy level
- Completely offline operation

#### 3. Multi-Dimensional Check-ins & Journaling 📸
- Photo capture with CameraX
- Energy level: 1-5 rating
- Body condition tags and journal text

#### 4. History & Data Management 📋
- Trek record list view
- Detailed route review
- SQLite local storage

### 🛠️ Tech Stack

**Framework & Language**
- Kotlin + Jetpack Compose + Material Design 3
- MVVM Architecture

**Core Libraries**
- Room Database (SQLite)
- Kotlin Coroutines + Flow
- FusedLocationProviderClient
- CameraX + Coil
- Accompanist Permissions

### 🗄️ Database Design

```sql
-- Trek Session
CREATE TABLE trek_sessions (
  id INTEGER PRIMARY KEY,
  routeName TEXT,
  startTime LONG,
  endTime LONG,
  totalDistance DOUBLE
)

-- GPS Waypoint
CREATE TABLE waypoints (
  id INTEGER PRIMARY KEY,
  trekSessionId LONG,
  latitude DOUBLE,
  longitude DOUBLE,
  altitude DOUBLE,
  timestamp LONG
)

-- Check-in Log
CREATE TABLE check_in_logs (
  id INTEGER PRIMARY KEY,
  trekSessionId LONG,
  latitude DOUBLE,
  longitude DOUBLE,
  photoUri TEXT,
  journalText TEXT,
  energyLevel INTEGER
)
```

### 🚀 Quick Start

**Prerequisites**
- Android Studio Flamingo+
- JDK 11+
- Android SDK API 26+

**Build & Run**
```bash
git clone https://github.com/Nine-CODE-bit/TrailMark.git
cd TrailMark
./gradlew clean build
# Open in Android Studio and press Shift + F10
```

### ⚙️ Configuration

**Java Home Setup** (gradle.properties)
```properties
org.gradle.java.home=E:\AndroidStudio\jbr
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
org.gradle.parallel=true
org.gradle.caching=true
```

**Permissions** (AndroidManifest.xml)
```xml
<!-- Location Permissions -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Camera Permission -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- Storage Permissions -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Background Location Service -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
```

### 📋 Dependencies

```gradle
dependencies {
  // Compose UI
  implementation("androidx.compose.ui:ui:1.6.0")
  implementation("androidx.compose.material3:material3:1.1.0")
  
  // Location Services
  implementation("com.google.android.gms:play-services-location:21.3.0")
  
  // Room Database
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  ksp("androidx.room:room-compiler:2.6.1")
  
  // CameraX
  implementation("androidx.camera:camera-core:1.3.1")
  implementation("androidx.camera:camera-camera2:1.3.1")
  implementation("androidx.camera:camera-lifecycle:1.3.1")
  
  // Image Loading
  implementation("io.coil-kt:coil-compose:2.7.0")
  
  // Permissions
  implementation("com.google.accompanist:accompanist-permissions:0.36.0")
  
  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}
```

### 🎯 Development Plan

| Week | Tasks | Status |
|------|-------|--------|
| Week 1 | Database + Background Location | ✅ Complete |
| Week 2 | Canvas Drawing + Real-time Updates | 🔄 In Progress |
| Week 3 | CameraX + Check-in System | 📋 Planned |
| Week 4 | Complete UI + History | 📋 Planned |
| Week 5 | Testing + Optimization | 📋 Planned |

### 🔍 Core Challenges

**1. Background Location Restrictions**
- Challenge: Android system restricts background location access
- Solution: Foreground service + persistent notification

**2. Image Memory Management**
- Challenge: Check-in photos may cause memory overflow
- Solution: Coil compression + caching strategy

**3. Canvas Drawing Performance**
- Challenge: Long tracks may cause lag
- Solution: Trajectory simplification + batch rendering

### 📁 Project Structure

```
TrailMark/
├── app/src/main/java/com/example/trailmark/
│   ├── data/
│   │   ├── entity/              # Data Entities
│   │   ├── dao/                 # Data Access Objects
│   │   ├── database/            # Database Config
│   │   └── repository/          # Repository Pattern
│   ├── ui/
│   │   ├── screens/             # Compose Screens
│   │   ├── components/          # UI Components
│   │   ├── viewmodel/           # ViewModels
│   │   └── theme/               # Theme Config
│   ├── service/                 # Background Services
│   ├── util/                    # Utility Classes
│   └── MainActivity.kt
├── gradle/
│   └── libs.versions.toml       # Dependency Versions
├── build.gradle.kts             # Gradle Config
└── README.md
```

### 📄 License
MIT License

### 👤 Developer
Nine-CODE-bit

---

**Last Updated**: 2026-04-26
```

</file_block_syntax>

---

## 📋 使用说明

### **✅ 直接复制粘贴方法**

1. **复制上面的完整 README 内容**

2. **在 GitHub 网页上编辑：**
   - 访问 https://github.com/Nine-CODE-bit/TrailMark
   - 点击 README.md 文件
   - 点击 ✏️ 编辑按钮
   - 清空所有内容
   - 粘贴新的 README 内容
   - 点击 "Commit changes"

3. **或在本地编辑并推送：**
   ```bash
   cd TrailMark
   # 用编辑器打开 README.md
   nano README.md
   
   # 粘贴新内容，保存并退出
   
   # 提交到 GitHub
   git add README.md
   git commit -m "Update README with bilingual documentation"
   git push origin main
   ```

---
