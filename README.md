# Akira Core (Kotlin)
这是 Akira 系列插件开发的核心基础，通过 `Kotlin` 重新实现。  
该插件旨在为开发者提供可扩展的基类和开箱即用的工具，提升开发效率。

## 依赖 - Dependency
该插件基于 `Paper Spigot` 的 `1.20.6-R0.1-SNAPSHOT` 核心。  
同时，打包了重定位后（Relocated）的 Kotlin 标准库用于提升兼容性。

## 使用方式 - Usage
当成普通的前置插件（API）一样使用即可，  
推荐使用 Kotlin 而不是 Java 开发，可体验到更多 Kotlin 相关特性。

### 1. 插件构建完能否自动部署到服务端里？
能，只需要一点小改动。  
把 `build.gradle.kts` 的 `serverFolder` 属性改成你的服务端路径，  
执行 Gradle 任务 `build` 的时候就会自动完成复制，即自动部署。

### 2. 子插件还需打包 Kotlin 标准库吗？
不需要。  
子插件可在构建中使用 `ShadowJar` 设置重定位（Relocate）：  
- `relocate("kotlin", "com.akira.shadow.kotlin")`

并在其 `plugin.yml` 的 `depend` 项中添加 `AkiraCore`，例如：
- `depend: [AkiraCore]`

即可使子插件在运行时复用已有的标准库。

### 3. 开发子插件时，我应该如何引用依赖？
使用本地 `Maven` 部署依赖，并在子插件 `build.gradle` 中引用。  
依赖的部署（对于 Akira Core）：
- 运行 Gradle 任务 `publishToMavenLocal` 自动部署

依赖的引用（对于子插件的 `build.gradle`）：
- `repositories` 添加 `mavenLocal()`
- `dependencies` 添加 `compileOnly("com.akira:AkiraCoreKotlin:+")`

## 关于 - About
本项目旨在服务 Akira 系列插件，尽管项目是公开的，  
但 API 随时可能根据插件的实际开发需求产生改动。