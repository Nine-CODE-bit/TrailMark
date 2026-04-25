# TrailMark - 登山者的多维度户外日志应用

## 📱 项目概述

TrailMark 是一个为户外爱好者量身打造的原生 Android 应用，深度整合 GPS 轨迹追踪、富媒体日志记录和个人身体状态监测，为用户生成高度个性化的多维度户外档案。

### 背景 & 核心价值

随着户外活动的增加，年轻一代正在转向登山和登山探索自然。虽然 Strava 等成熟的路线追踪应用在市场上占据主导地位，但它们主要关注竞技运动指标（如配速、心率、心肺功能），往往忽视了普通户外爱好者记录"登山体验"和"个人身体反馈"的需求。

特别是在高海拔艰难跋涉期间（如玉龙雪山、梅里雪山、野马海子等路线），极端环境将登山者的身体状态（如高反严重程度、体力耗尽、体感温度）与其地理位置和海拔息息相关。传统的打卡拍照或纯 GPS 轨迹绘制无法建立"时间-空间-身体-环境"的四维连接。

TrailMark 的目标是填补这一空白，提供真正意义上的多维度户外体验记录平台。

## ✨ 核心功能模块

### 1. 活跃登山追踪 (Active Trek Tracking)
- **实时仪表板**：显示运动状态、累计距离、当前海拔、实时配速
- **后台保活机制**：使用前台服务 + 持久化通知确保长时间定位
- **高精度定位**：利用 FusedLocationProviderClient 进行低功耗高精度定位

### 2. 富媒体打卡 & 日志记录 (Rich-Media Check-in & Journaling)
- **即时拍照**：集成 CameraX 进行应用内拍照
- **多维度状态记录**：
  - 能量等级：1-5 级评分
  - 身体状况：冷、缺氧、疲惫、放松等标签
  - 日志文本：记录想法和风景描述

### 3. 交互式地图回放 (Interactive Map Playback)
- **轨迹恢复**：使用 Google Maps SDK 的 Polyline 绘制完整登山路线
- **智能标记**：根据能量等级/身体状况动态改变标记样式
- **信息窗口**：点击标记显示照片、时间、海拔、日志和身体状态数据

### 4. 历史 & 数据管理 (History & Data Management)
- **登山记录列表**：按时间或距离排序的卡片列表展示
- **详细路线回顾**：单次登山记录的完整数据展示

## 🛠️ 技术栈

### 开发框架
- **语言**：Kotlin
- **架构模式**：MVVM (Model-View-ViewModel)
- **UI 框架**：Android Jetpack + Material Design 3

### 核心库
- **异步处理**：Kotlin Coroutines + Flow
- **数据持久化**：Room Database (SQLite)
- **地图与定位**：
  - Google Maps SDK for Android
  - FusedLocationProviderClient
- **多媒体处理**：
  - CameraX（相机集成）
  - Glide / Coil（图片加载与缓存）
- **依赖注入**：Hilt (推荐)
- **UI 响应式**：LiveData / StateFlow

## 🗄️ 数据库设计

使用 Jetpack Room 构建本地 SQLite 数据库，包含三个核心表：

### 表结构

#### 1. TrekSession（登山会话）
```
- id: Long (主键)
- routeName: String (路线名称)
- startTime: Long (开始时间戳)
- endTime: Long (结束时间戳)
- totalDistance: Double (总距离，单位：米)
- createdAt: Long (记录创建时间)
```

#### 2. Waypoint（路径点）
```
- id: Long (主键)
- trekSessionId: Long (外键，关联 TrekSession)
- timestamp: Long (时间戳)
- latitude: Double (纬度)
- longitude: Double (经度)
- altitude: Double (海拔，单位：米)
```

#### 3. CheckInLog（打卡日志）
```
- id: Long (主键)
- trekSessionId: Long (外键，关联 TrekSession)
- checkInTime: Long (打卡时间戳)
- latitude: Double (纬度)
- longitude: Double (经度)
- altitude: Double (海拔)
- photoUri: String (照片本地路径)
- journalText: String (日志文本)
- energyLevel: Int (能量等级 1-5)
- bodyCondition: String (身体状况标签，JSON 格式)
- mood: String (心情/情绪记录)
```

## 🚀 开发环境设置

### 前置需求
- Android Studio Flamingo 或更高版本
- JDK 11 或更高版本
- Android SDK 最低版本：API 26
- Android SDK 目标版本：API 34 或以上
- Gradle 8.0 或更高版本

### 仓库克隆
```bash
git clone https://github.com/Nine-CODE-bit/TrailMark.git
cd TrailMark
```

### 构建步骤
1. 在 Android Studio 中打开项目
2. 等待 Gradle 同步完成
3. 连接 Android 设备或启动模拟器（API 26+）
4. 点击"Run"或按 Shift + F10 构建和运行

## 🔑 环境变量和配置

### Google Maps API 密钥设置

#### Step 1: 获取 Google Maps API 密钥
1. 访问 [Google Cloud Console](https://console.cloud.google.com/)
2. 创建或选择一个项目
3. 启用 Maps SDK for Android
4. 在"凭证"中创建新的 API 密钥
5. 配置 API 密钥的限制（选择 Android 应用）

#### Step 2: 配置应用签名指纹
获取应用的 SHA-1 指纹：
```bash
# 开发调试密钥（位于 ~/.android/debug.keystore）
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

#### Step 3: 在项目中配置密钥
在 `AndroidManifest.xml` 中添加：
```xml
<application>
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE" />
</application>
```

或在 `local.properties` 中创建环境变量：
```properties
GOOGLE_MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY_HERE
```

### 权限配置 (AndroidManifest.xml)

```xml
<!-- 定位权限 -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />

<!-- 相机权限 -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- 存储权限 -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- 网络权限 -->
<uses-permission android:name="android.permission.INTERNET" />
```

### 运行时权限请求

应用在首次启动时会请求必要的权限：
- 精准定位
- 后台定位（仅在 Android 11+ 上需要单独请求）
- 相机
- 存储读写

用户需要在权限对话框中授予权限以使用应用功能。

## 📋 gradle 依赖配置

在 `build.gradle` 中添加以下依赖：

```gradle
dependencies {
    // Android Jetpack 基础
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // MVVM & LiveData
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
    
    // Coroutines & Flow
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
    
    // Room Database
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'
    
    // Google Maps
    implementation 'com.google.android.gms:play-services-maps:18.2.0'
    implementation 'com.google.android.gms:play-services-location:21.1.0'
    
    // CameraX
    implementation 'androidx.camera:camera-core:1.3.1'
    implementation 'androidx.camera:camera-camera2:1.3.1'
    implementation 'androidx.camera:camera-lifecycle:1.3.1'
    implementation 'androidx.camera:camera-view:1.3.1'
    
    // Glide 图片加载
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    kapt 'com.github.bumptech.glide:compiler:4.16.0'
    
    // Hilt 依赖注入
    implementation 'com.google.dagger:hilt-android:2.48'
    kapt 'com.google.dagger:hilt-compiler:2.48'
    
    // JSON 解析
    implementation 'com.squareup.moshi:moshi-kotlin:1.15.0'
    kapt 'com.squareup.moshi:moshi-kotlin-codegen:1.15.0'
    
    // 测试
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## 🎯 开发计划 (5 周期)

### 第 1 周：环境搭建 & 基础设施
- ✅ 需求分析和技术评审
- ✅ 注册 Google Cloud API Maps 密钥
- ✅ 配置 Room 数据库实体和 DAOs
- ✅ 实现动态权限请求逻辑

### 第 2 周：核心定位服务 & 数据收集
- ⏳ 使用前台服务实现后台连续定位
- ⏳ 将 FusedLocationProviderClient 与 Room 数据库集成
- ⏳ 确保稳定的 Waypoint 数据插入

### 第 3 周：相机集成 & 富媒体打卡
- ⏳ 集成 CameraX 设计自定义拍照界面
- ⏳ 开发打卡表单页面
- ⏳ 多维度数据写入 CheckInLog 表

### 第 4 周：地图渲染 & 交互层 (核心挑战)
- ⏳ 加载 Google Maps 在详情页面
- ⏳ 从 Room 读取数据绘制 Polyline
- ⏳ 生成自定义 Markers 和点击监听
- ⏳ 实现自定义 InfoWindow 显示

### 第 5 周：UI 优化、测试 & 发布
- ⏳ Material Design 3 视觉统一
- ⏳ 地图渲染性能优化
- ⏳ 单元测试和集成测试
- ⏳ 演示视频录制和文档完成

## 🔍 预期挑战与解决方案

### 1. 后台定位严格限制
**挑战**：现代 Android 系统对后台定位访问限制严格
**解决方案**：
- 使用前台服务与持久化通知
- 明确向用户说明权限的必要性
- 严格遵循 Android 后台限制指南

### 2. 图片加载与内存管理
**挑战**：地图上可能有数十个 Markers，高分辨率图片易导致内存泄漏
**解决方案**：
- 使用 Glide 的图片压缩策略
- 在 InfoWindow 中仅加载缩略图
- 提供全屏原图查看功能

### 3. 长轨迹性能优化
**挑战**：超长登山路线可能包含数千个路径点，导致地图卡顿
**解决方案**：
- 实现简单的轨迹简化算法
- 使用分页加载大量数据
- 优化 Polyline 绘制算法

## 📄 许可证
MIT License

## 👤 开发者
- Nine-CODE-bit

## 🤝 贡献指南
欢迎提交 Issue 和 Pull Request！请确保代码符合 Kotlin 规范和项目架构要求。

---

**最后更新时间**：2026-04-25