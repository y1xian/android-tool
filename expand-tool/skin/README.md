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
		mSkinCompat = new SkinCompat(this, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		View view = mSkinCompat.createView(parent, name, context, attrs);
		if (view == null) {
			return super.onCreateView(parent, name, context, attrs);
		}
		return view;
    }

    // 这里单独的配置自定义View的换肤功能
    @Override
    public void onSkinChange(View view, SkinResource skinResource) {
    }

    @Override
    protected void onDestroy() {
		// 释放内存
		mSkinCompat.release(this);
		mSkinCompat = null;
        super.onDestroy();
    }
}
```

2. 调用换肤/恢复默认皮肤的方法，如果是SD卡中的皮肤读取需要配置读取本地文件的权限:

```java
// Application初始化一下
SkinManager.getInstance().init(this);
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
