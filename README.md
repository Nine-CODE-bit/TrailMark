# TrailMark Documentation

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

## Offline GPS Recording Version

### Overview
TrailMark is an application designed for effective offline GPS recording, allowing users to track their journeys seamlessly without needing an internet connection.

### Key Features
- **Canvas-based Path Drawing**: Instead of relying on Google Maps, TrailMark implements a robust canvas feature that enables users to visualize their paths dynamically.
- **Local Check-in System**: The app allows users to check in at specific points along their route, ensuring that all locations are saved for future reference.

### Tech Stack
- **Frontend**: HTML5, CSS3, JavaScript
- **Backend**: Node.js, Express
- **Database**: SQLite for local data storage
- **Mapping**: Custom canvas drawing solution

### Setup Instructions
1. Clone the repository: `git clone https://github.com/Nine-CODE-bit/TrailMark.git`
2. Navigate to the project directory: `cd TrailMark`
3. Install dependencies: `npm install`
4. Run the application: `npm start`
5. Access the application at `http://localhost:3000`

### Development Plan
- **Phase 1**: Implement basic GPS recording functionality.
- **Phase 2**: Develop the canvas-based path drawing feature.
- **Phase 3**: Establish the local check-in system.
- **Phase 4**: Testing and bug fixing.
- **Phase 5**: User feedback and final adjustments.

### Conclusion
The offline GPS recording version of TrailMark provides users with a comprehensive tool for tracking their journeys in a user-friendly manner, free from external map dependencies.
