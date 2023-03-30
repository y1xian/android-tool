### 插件式换肤

默认支持 `textColor`、`background`、`src` 三个属性换肤，支持其它属性扩展为默认属性

<br/>

### 使用方式

1.放置 `BaseActivity`

```java
// 这里需要实现 OnSkinChangeCallback 接口用来扩展自定义View的换肤
public abstract class BaseActivity extends AppCompatActivity implements OnSkinChangeCallback {
    // 拦截View创建代码的封装对象
    protected SkinCompat mSkinCompat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 配置 Factory 拦截View的创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater, this);
        mSkinCompat = new SkinCompat(this, getWindow());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 拦截View的创建
        View view = mSkinCompat.createView(parent, name, context, attrs);
        // 解析View声明的属性
        if (view != null) {
            // 获取当前View可以换肤的属性列表
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttr(context, attrs);
            // 将获取到的属性和对应的 View 封装
            SkinAttrHolder skinAttrHolder = new SkinAttrHolder(view, skinAttrs);
            // 统一交给 SkinManager 管理
            mSkinCompat.managerSkinView(this, skinAttrHolder);
            // 判断是否需要换肤
            SkinManager.getInstance().checkSkin(this, view, skinAttrHolder);
        }
        return view;
    }

    // 这里单独的配置自定义View的换肤功能
    @Override
    public void onSkinChange(View view, SkinResource skinResource) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存
        mSkinCompat.release(this);
    }
}
```

2. 调用换肤/恢复默认皮肤的方法，如果是SD卡中的皮肤读取需要配置读取本地文件的权限:

```java
// 换肤
SkinManager.getInstance().loadSkin(本地皮肤路径);
// 恢复默认皮肤
SkinManager.getInstance().restoreSkin();
```

3. 扩展自定义View的属性换肤只需要重写`onSkinChange()` 方法即可:

```java
 @Override
public void onSkinChange(View view, SkinResource skinResource) {
    super.onSkinChange(view, skinResource);
    ColorStateList color = null;
    switch (view.getId()) {
        case R.id.my_view:
            // 这里传入的名称为资源的名称而不是属性的名称
            color = skinResource.getColorByName("main_color");
            ((MyView) view).setColor(color.getDefaultColor());
            break;
    }
}
```
