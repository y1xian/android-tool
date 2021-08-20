# 流水的框架，铁打的工具
常用工具库,AndroidX的
## 使用方式

#### 下载或添加依赖
[最新版本](https://github.com/y1xian/Oh/releases) [![](https://jitpack.io/v/y1xian/Oh.svg)](https://jitpack.io/#y1xian/Oh)

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

//要求项目使用Java 8
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}

dependencies {
    implementation 'com.github.y1xian.Oh:lib-xx:+'
}
```
