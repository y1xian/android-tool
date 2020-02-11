# Widget
组件
## 使用方式

#### 下载或添加依赖
[最新版本](https://github.com/y1xian/Widget/releases) [![](https://jitpack.io/v/y1xian/Widget.svg)](https://jitpack.io/#y1xian/Widget)

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
    // Jitpack架构，mvvm 基类封装
    implementation 'com.github.y1xian.Widget:arch:+'
    // 网络框架 Retrofit2 + Okhttp3 ,Gson
    implementation 'com.github.y1xian.Widget:http:+'
    // 常用自定义
    implementation 'com.github.y1xian.Widget:view:+'
    // 常用工具分离，kt扩展 (arch已包括此包)
    implementation 'com.github.y1xian.Widget:utils:+'
    // 适配器
    implementation 'com.github.y1xian.Widget:adapter:+'
}
```
