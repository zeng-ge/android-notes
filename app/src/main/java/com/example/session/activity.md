# Activity的方法相关
  
  - onCreate
  - onStart
  - onResume
  - onPause
  - onStop
  - onDestroy
  - onSaveIntentState         state.putString("hello", "hello world")
  - startActivity(new Intent)
  - Intent.pushExtra()         利用extra来传递参数到下一个activity
  - getIntent()                获取传递下来的Intent，并通过它获取Extra里面的参数
  - startActivityForResult(Intent intent, int code)  
    用它打开第二个activity后，当回到第一个activity时可以通过setResult(int requestCode, Intent intent)
    来设置返回值，如果没有调用setResult，结果code会收到Activity.RESULT_CANCELED        
  - setResult(int resultCode, Intent data)    关闭当前activity，返回上前一个
  - onActivityResult(int requestCode, int resultCode, Intent intent)  
    setResult后，前面的activity会触发onActivityResult
  - finish()                    关闭当前activity
  
  
 # 版本
  - minSdkVersion                 如果底于手机系统版本则会被拒绝安装
  - targetSdkVersion              应用的目标就是在该级别API顺利执行，一般为最新API级别
  - compileSdkVersion             本地编译APP的SDK版本
  - BUILD.VERSION.SDK_INT         当前系统的android api level