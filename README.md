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
    //Jitpack架构，mvvm
    implementation 'com.github.y1xian.Widget:arch:+'
    //网络框架
    implementation 'com.github.y1xian.Widget:http:+'
    //view
    implementation 'com.github.y1xian.Widget:view:+'
}
```
