# bfc-http
中间件项目：网络请求库，支持反劫持处理test1
# Feedback
**网络请求库目前仍处于开发版本，还有很多需求和功能为完善，有任何新的需求或使用不爽可在GitLab上吐槽。[Issues传送门](http://172.28.2.93/bfc/BfcHttp/issues)**

**生命在于折腾（各版本升级，可能存在较大改动，使用前请尽可能查看最新Readme文档的[版本更新日志](#update-list)及[使用说明](#using)）**

**附 RTX：20252365**
# Design
* [框架设计](http://172.28.2.93/bfc/BfcHttp/raw//doc/screenshots/structure_design.png)
* [详细设计](http://172.28.2.93/bfc/BfcHttp/raw//doc/screenshots/inteface_design.png)

# Gradle
添加私有maven配置

gradle.properties
```properties
    #本地库URL
    MAVEN_URL= http://172.28.1.147:8081/nexus/content/repositories/thirdparty/
```
project build.gradle
```groovy
    allprojects {
        repositories {
            jcenter()
            maven { url MAVEN_URL }
        }
    }
```
module build.gradle
```groovy
    compile 'com.eebbk.bfc:bfc-http:0.9.2+'
```
# Dependence
内部依赖项目
```groovy
    compile 'com.android.volley:volley:1.0.0'
    compile 'com.eebbk.bfc:bfc-log:+'
```
# Build info
构建信息
```groovy
    compileSdkVersion 23
    buildToolsVersion "24.0.0"
    minSdkVersion 15
    targetSdkVersion 24
```
# Inner Permission
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
# Update List
* V0.9.0 基本网络请求封装
    * get/post
    * 支持反劫持实现（通过BfcHttpConfigure全局配置反劫持域名）
* V0.9.1-beta 添加gzip支持
    * 设置请求头{"Accept-Encoding", "gzip"}请求参数，内部会对返回数据gzip解压，返回接口不变
    * BfcHttpConfigure.setRequestHead() 全局请求头设置方法已废弃。每个请求管理所属请求有头设置 BfcHttp.get(Context, url, params, **header**, StringCallBack, IBfcErrorListener, tag);
* V0.9.3-beta
    * 中间件统一gradle配置：服务器统一配置文件引用
* V0.9.4-beta
    * 添加请求自定义配置接口，通过BfcRequestConfigure.Builder实现
    * 添加gzip、cache请求的支持，需要添加相关请求头及BfcRequestConfigure.Builder相关配置实现响应请求，详细配置见使用说明
* V0.9.5-beta
    * 反劫持流程升级旧版本普通字符数据请求，允许自定义配置，旧版本虽然开放设置入口，但底层实现仍是旧的请求配置（对应[issues](http://172.28.2.93/bfc/BfcHttp/issues/2)）
    * code inspect
* V0.9.1
    * V0.9.1-beta~V0.9.5-beta修改同步
    * 升级须知（升级异常[Issues](http://172.28.2.93/bfc/BfcHttp/issues/3)）：
        * BfcHttpConfigure.setRequestHead()方法已删除，请求头通过单个请求参数设置
        * BfcHttpError 包名修改com.eebbk.bfc.http.toolbox.BfcHttpError;  ->  com.eebbk.bfc.http.error.BfcHttpError;
* V0.9.2
    * 添加调试模式下网络请求详情的相关日志显示，输出样式示例可见[debug_mode](http://172.28.2.93/bfc/BfcHttp/raw/master/doc/screenshots/debug_mode.png)
    * 修改服务器设置禁止缓存：{"Cache-Control":"no-cache"}时发生空指针的异常
    * 修改Type下STRING、CACHE_ABLE、GZIP三者关系，三者不应再同一级别，缓存应当是允许与其他模式配合使用。新版本CACHE_ABLE设置已废弃，并在接下来几个版本删除，请注意
    * 添加遗漏javadoc文档
* V0.9.3
    * 修复一些集成中发现的问题
* V0.9.4
    * 添加SDKVersion用于显示库信息
* V0.9.5
    * 添加缓存清理接口，支持全部清理和针对URL请求
    * 添加获取缓存目录的接口
* V2.0.0
    * BFC大版本发布，统一版本号
* V2.0.1
    * 允许强制缓存
* V2.1.0-beta
    * 添加设置请求body接口
    
# Using
### 全局配置
```java
    //初始化配置
    BfcHttpConfigure.init(this);
    //开启调试模式，打印详细日志信息
    BfcHttpConfigure.openDebug();
    //全局设置是否开启缓存，允许每个请求通过BfcRequestConfigure单独设置
    BfcHttpConfigure.setShouldCache(true);
    //超时时长配置，默认时长设置为5000ms
    BfcHttpConfigure.setTimeout(10000);
    //提供根据不同网络状态设置不同超时时长的配置，不配置默认使用单个时长设置，默认5000ms
    BfcHttpConfigure.setTimeout(5000, 7500, 1000);
    //设置需要执行反劫持的域名，目前仅支持设置单个域名
    BfcHttpConfigure.setDomain("test.eebbk.net");
    //实现IBfcHttpRequest接口允许自定义网络请求实现类
    BfcHttpConfigure.setCustomHttpRequest(new CustomHttpRequest());
    //网络请求过程监听类，仅实现监听开始请求，请求结束可以再callback中监听
    BfcHttpConfigure.setBfcHttpStatusWatch(new CustomHttpStatusWatch());
```
### 网络请求库提供两种方式创建网络请求
#### 1. 提供常用参数列表静态调用函数
完整参数列表如下
```java
    /**
     * get
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param header        current request head
     * @param callBack      request success callback
     * @param errorListener request fail callback
     * @param tag           set tag to current request, use to cancel this request
     */
    public static void get(Context context, String url, Map<String, String> params, Map<String, String> header, @NonNull IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        //...
    }
```
同时提供几种常用重载方法调用，详细接口见BfcHttp类
```java
     public static void get(Context context, String url, Map<String, String> params, IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        //...
     }

     public static void get(Context context, String url, Map<String, String> params, Map<String, String> header, IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        //...
     }

     public static void get(Context context, String url, Map<String, String> params, IBfcHttpCallBack callBack, IBfcErrorListener errorListener, Object tag) {
        //...
     }
```
#### 2. 自定义配置接口
```java
    /**
     * get
     *
     * @param context       context
     * @param url           request url
     * @param params        request params
     * @param conf          request configure {@link BfcRequestConfigure}
     * @param callBack      request success callback
     * @param errorListener request fail callback
     */
    public static void get(Context context, String url, Map<String, String> params, @NonNull BfcRequestConfigure conf, @NonNull IBfcHttpCallBack callBack, IBfcErrorListener errorListener) {
        //...
    }

    //BfcRequestConfigure 提供配置参数
    BfcRequestConfigure conf = new BfcRequestConfigure.Builder()
            .setHeader(header)
            .setType(BfcRequestConfigure.Type.GZIP)
            .setTag("BfcRequestConfigure")
            .setCacheAble(true)
            .setCacheKey("...")
            .setExpiredTime(5 * 60 * 60 * 1000)
            .setRetryTimes(1)
            .setTimeout(10 * 1000).build();
```
BfcRequestConfigure类通过Builder创建，允许对每个请求做定制化的配置，目前支持对**缓存时间**、**自定义cacheKey**、**标签**、**超时时长**、**重试次数**、**是否允许缓存**、**请求头**属性的配置
##### Tips
需要是实现Gzip请求需要配置请求头添加{"Accept-Encoding":"gzip"}属性，同时配置
```java
    BfcRequestConfigure conf = new BfcRequestConfigure.Builder()
        .setHeader(header)
        .setType(BfcRequestConfigure.Type.GZIP)
        .build();
```
具体使用封装可参考Demo中[网络请求工具类](http://172.28.2.93/bfc/BfcHttp/blob/master/http-sample/src/main/java/com/example/http/toolbox/NetWorkUtils.java)
#### 具体调用
```java
    BfcHttp.get(this, getUrl, params, header, new StringCallBack() {
        @Override
        public void onResponse(String response) {
            //...
        }
    }, new IBfcErrorListener() {
        @Override
        public void onError(BfcHttpError error) {
            //...
        }
    }, "tag");

    BfcHttp.post(this, postUrl, params, header, new StringCallBack() {
        @Override
        public void onResponse(String response) {
            //...
        }
    }, new IBfcErrorListener() {
        @Override
        public void onError(BfcHttpError error) {
            //...
        }
    }, "tag");
```
使用lambda调用方式
```java
    BfcHttp.get(getApplicationContext(), getUrl, params, header, response -> doOnResponse(response), error -> doOnError(error), "tag");
    BfcHttp.post(getApplicationContext(), postUrl, params, header, response -> doOnResponse(response), error -> doOnError(error), "tag");
```
or
```java
    BfcHttp.get(getApplicationContext(), getUrl, params, header, this::doOnResponse, this::doOnError, "tag");
    BfcHttp.post(getApplicationContext(), postUrl, params, header, this::doOnResponse, this::doOnError, "tag");
```
# User Custom Request

#### 当需要自定义请求实现时，可以实现IBfcHttpRequest接口在全局配置中设置
```java
    BfcHttpConfigure.setCustomHttpRequest(new CustomHttpRequest());
```
CustomHttpRequest创建可参考Demo中[CustomHttpRequest.java](http://172.28.2.93/bfc/BfcHttp/blob/master/http-sample/src/main/java/com/example/http/toolbox/CustomHttpRequest.java)

#### 当需要使用其他网络请求框架实现底层网络请求，可以创建关联类继承HttpStack并在BfcHttpConfigure的全局配置中设置
例如：使用OkHttp实现底层请求
```java
    BfcHttpConfigure.init(this, new OkHttp3Stack());
```
OkHttp3Stack类可参考Demo中[OkHttp3Stack.java](http://172.28.2.93/bfc/BfcHttp/blob/master/http-sample/src/main/java/com/example/http/toolbox/OkHttp3Stack.java)

# AntiHijack
* 反劫持原理
![antihijack](http://172.28.2.93/bfc/BfcHttp/raw/master/doc/screenshots/antihijack.png)
相关文档  [./doc/其他/BFC-反劫持.pptx](./doc/其他/BFC-反劫持.pptx)

# ErrorCode
错误码设计：模块码+错误码

网络请求模块：07
* 070001 无网络连接
* 070300 网络重定向对应网络请求3XX
* 070400 客户端异常对应网络请求4XX
* 070500 服务端异常对应网络请求5XX
* 071001 httpDns请求失败