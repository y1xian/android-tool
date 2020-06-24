# Widget
常用工具库 ， [Java版本JetPack框架](https://github.com/y1xian/Amazing) ， [Kotlin版本JetPack框架](https://github.com/y1xian/Awesome)
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
    // 常用自定义
    implementation 'com.github.y1xian.Widget:lib_view:+'
    // 公共工具 (都已包括此包)
    implementation 'com.github.y1xian.Widget:lib_common:+'
    // 各种工具 
    implementation 'com.github.y1xian.Widget:lib_tools:+'
    // 适配器
    implementation 'com.github.y1xian.Widget:lib_adapter:+'
    // 主题切换
    implementation 'com.github.y1xian.Widget:lib_skin:+'
    // 插件换肤
    implementation 'com.github.y1xian.Widget:lib_skinloader:+'
}
```
