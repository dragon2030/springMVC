# 登录鉴权

## 鉴权相关核心概念
### 什么是认证（Authentication）
* 通俗地讲就是验证当前用户的身份，证明“你是你自己”（比如：你每天上下班打卡，都需要通过指纹打卡，当你的指纹和系统里录入的指纹相匹配时，就打卡成功）
* 互联网中的认证：
  * 用户名密码登录
  * 邮箱发送登录链接
  * 手机号接收验证码
  * 只要你能收到邮箱/验证码，就默认你是账号的主人
### 什么是授权（Authorization）
* 用户授予第三方应用访问该用户某些资源的权限
  * 你在安装手机应用的时候，APP 会询问是否允许授予权限（访问相册、地理位置等权限）
  * 你在访问微信小程序时，当登录时，小程序会询问是否允许授予权限（获取昵称、头像、地区、性别等个人信息）
* 实现授权的方式有：cookie、session、token、OAuth
### 什么是凭证（Credentials）
* 实现认证和授权的前提是需要一种媒介（证书） 来标记访问者的身份
  * 在战国时期，商鞅变法，发明了照身帖。照身帖由官府发放，是一块打磨光滑细密的竹板，上面刻有持有人的头像和籍贯信息。国人必须持有，如若没有就被认为是黑户，或者间谍之类的。
  * 在现实生活中，每个人都会有一张专属的居民身份证，是用于证明持有人身份的一种法定证件。通过身份证，我们可以办理手机卡/银行卡/个人贷款/交通出行等等，这就是认证的凭证。
  * 在互联网应用中，一般网站（如掘金）会有两种模式，游客模式和登录模式。游客模式下，可以正常浏览网站上面的文章，一旦想要点赞/收藏/分享文章，就需要登录或者注册账号。当用户登录成功后，服务器会给该用户使用的浏览器颁发一个令牌（token），这个令牌用来表明你的身份，每次浏览器发送请求时会带上这个令牌，就可以使用游客模式下无法使用的功能。

## Java Web 中的登录鉴权组成部分

登录鉴权是 Web 应用程序安全的重要组成部分，主要包括登录、登出和请求拦截等功能

# 鉴权核心技术

鉴权核心技术：Cookie、Session、Token、JWT
## Cookie

Cookie鉴权方式通过在客户端存储包含用户身份信息的Cookie，并在每次请求时自动携带该Cookie，服务器验证Cookie内容来实现用户身份认证和状态保持。

* HTTP 是无状态的协议（对于事务处理没有记忆能力，每次客户端和服务端会话完成时，服务端不会保存任何会话信息）：每个请求都是完全独立的，服务端无法确认当前访问者的身份信息，无法分辨上一次的请求发送者和这一次的发送者是不是同一个人。所以服务器与浏览器为了进行会话跟踪（知道是谁在访问我），就必须主动的去维护一个状态，这个状态用于告知服务端前后两个请求是否来自同一浏览器。而这个状态需要通过 cookie 或者 session 去实现。
* cookie 存储在客户端： cookie 是服务器发送到用户浏览器,是请求头（Request Header）的一部分,并保存在本地的一小块数据，它会在浏览器下次向同一服务器再发起请求时被携带并发送到服务器上。
* cookie 是不可跨域的： 每个 cookie 都会绑定单一的域名，无法在别的域名下获取使用，一级域名和二级域名之间是允许共享使用的（靠的是 domain）。

### cookie 重要的属性

|属性|说明|
|----|----|
|name=value|键值对，设置 Cookie 的名称及相对应的值，都必须是字符串类型。如果值为 Unicode 字符，需要为字符编码；如果值为二进制数据，则需要使用 BASE64 编码。|
|domain|指定 cookie 所属域名，默认是当前域名|
|path|指定 cookie 在哪个路径（路由）下生效，默认是 '/'。如果设置为 /abc，则只有 /abc 下的路由可以访问到该 cookie，如：/abc/read。|
|maxAge|cookie 失效的时间，单位秒。如果为整数，则该 cookie 在 maxAge 秒后失效。如果为负数，该 cookie 为临时 cookie ，关闭浏览器即失效，浏览器也不会以任何形式保存该 cookie 。如果为 0，表示删除该 cookie 。默认为 -1。- 比 expires 好用。|
|expires|过期时间，在设置的某个时间点后该 cookie 就会失效。一般浏览器的 cookie 都是默认储存的，当关闭浏览器结束这个会话的时候，这个 cookie 也就会被删除|
|secure|该 cookie 是否仅被使用安全协议传输。安全协议有 HTTPS，SSL等，在网络上传输数据之前先将数据加密。默认为false。当 secure 值为 true 时，cookie 在 HTTP 中是无效，在 HTTPS 中才有效。|
|httpOnly|如果给某个 cookie 设置了 httpOnly 属性，则无法通过 JS 脚本 读取到该 cookie 的信息，但还是能通过 Application 中手动修改 cookie，所以只是在一定程度上可以防止 XSS 攻击，不是绝对的安全|
|SameSite|用于防止 **跨站请求伪造（CSRF）攻击**，通过限制浏览器在跨站请求中是否发送 Cookie 来提高安全性。<br />1、Strict。**最严格**。只在**同站请求**中携带 Cookie，跨站请求完全不会带 Cookie。<br />2、Lax。**适度安全**。大多数**GET 的跨站导航请求**（如点击链接）会带上 Cookie，但不包括 POST、iframe、fetch 等复杂请求。<br />3、None。**最开放**。无论跨站或同站请求，都会带上 Cookie。**但必须同时设置 `Secure` 属性**，即 Cookie 只能通过 HTTPS 发送。|
### Cookie的位置
* HTTP请求头：Cookie通过Cookie请求头字段发送到服务器。例如：
```
http
GET /example HTTP/1.1
Host: example.com
Cookie: name=value; sessionId=abc123
```
服务器通过解析Cookie头来获取客户端发送的键值对。

* HTTP响应头：服务器通过Set-Cookie响应头向客户端（浏览器）设置Cookie：
```
http
HTTP/1.1 200 OK
Set-Cookie: sessionId=abc123; Path=/; Expires=Wed, 21 May 2025 07:28:00 GMT
```

### Java中如何操作Cookie
**服务端（接收/发送Cookie）**
* Servlet API：
```
// 读取客户端发送的Cookie
Cookie[] cookies = request.getCookies(); // 从HttpServletRequest获取
if (cookies != null) {
	for (Cookie cookie : cookies) {
		System.out.println(cookie.getName() + " = " + cookie.getValue());
	}
}

// 向客户端设置Cookie
Cookie newCookie = new Cookie("user", "Alice");
newCookie.setMaxAge(3600); // 有效期（秒）
response.addCookie(newCookie); // 通过HttpServletResponse添加
```
* Spring框架：
```
@GetMapping("/demo")
public String demo(HttpServletRequest request, HttpServletResponse response) {
// 读取Cookie
Cookie[] cookies = request.getCookies();

    // 设置Cookie
    ResponseCookie cookie = ResponseCookie.from("token", "xyz123")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(3600)
        .build();
    response.addHeader("Set-Cookie", cookie.toString());
    return "OK";
}
```
**客户端（发送Cookie）**
使用HttpURLConnection或HttpClient时，需手动设置Cookie头：
```
URL url = new URL("http://example.com");
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setRequestProperty("Cookie", "name=value; sessionId=abc123");
```
### Cookie实现鉴权的基本原理
* 服务器验证：用户登录时，服务器验证凭据(用户名/密码等)
* 设置Cookie：验证成功后，服务器在响应中设置包含认证信息的Cookie
* 后续请求：浏览器会自动在后续请求中携带该Cookie
* 服务器验证Cookie：服务器检查Cookie有效性，决定是否授权访问
致命的弱点：cookie保存在客户端，对于一些敏感信息，用户名，密码，身份证。 不安全
### 纯Cookie鉴权方案（带签名）
**Java Servlet 实现示例**

* 1. 登录处理（设置认证Cookie）
```
   @WebServlet("/login")
   public class ClassicCookieAuthServlet extends HttpServlet {
   private static final String SECRET_KEY = "your-secret-key-12345";

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 模拟用户验证
        if ("admin".equals(username) && "123456".equals(password)) {
            // 1. 准备Cookie数据
            String cookieData = username + "|" + System.currentTimeMillis();
            
            // 2. 创建签名防止篡改
            String signature = hmacSha1(SECRET_KEY, cookieData);
            String cookieValue = cookieData + "|" + signature;
            
            // 3. 设置Cookie
            Cookie authCookie = new Cookie("userAuth", cookieValue);
            authCookie.setMaxAge(24 * 60 * 60); // 24小时有效
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            authCookie.setPath("/");
            
            response.addCookie(authCookie);
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/login?error=1");
        }
   }

   // 简单的HMAC-SHA1签名（实际应用应使用更安全的算法）
   private String hmacSha1(String key, String value) {
       try {
           Mac mac = Mac.getInstance("HmacSHA1");
           SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSHA1");
           mac.init(secret);
           byte[] digest = mac.doFinal(value.getBytes());
           return Base64.getEncoder().encodeToString(digest);
       } catch (Exception e) {
       	throw new RuntimeException(e);
       }
   }
}
```
* 2. 认证过滤器（验证Cookie）
```
   @WebFilter("/*")
   public class ClassicCookieAuthFilter implements Filter {
   private static final String SECRET_KEY = "your-secret-key-12345";

   @Override
   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
   throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        // 排除登录和公开页面
        if (request.getRequestURI().endsWith("/login") || 
            request.getRequestURI().endsWith("/public")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 1. 获取Cookie
        Cookie[] cookies = request.getCookies();
        String cookieValue = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userAuth".equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        
        // 2. 验证Cookie
        if (cookieValue != null && isValidAuthCookie(cookieValue)) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/login");
        }
   }

   private boolean isValidAuthCookie(String cookieValue) {
   try {
   String[] parts = cookieValue.split("\\|");
   if (parts.length != 3) return false;

            String username = parts[0];
            String timestamp = parts[1];
            String signature = parts[2];
            
            // 验证签名
            String expectedSignature = hmacSha1(SECRET_KEY, username + "|" + timestamp);
            if (!expectedSignature.equals(signature)) {
                return false;
            }
            
            // 验证时间戳（示例：30分钟内有效）
            long authTime = Long.parseLong(timestamp);
            return System.currentTimeMillis() - authTime < 30 * 60 * 1000;
            
        } catch (Exception e) {
            return false;
        }
   }

   // 同上相同的hmacSha1方法...
}
```
### 纯正Cookie模式的特点

* 完全无状态：服务端不存储任何会话信息
* 自包含：所有认证信息都在Cookie中
* 签名验证：确保Cookie内容不被篡改
* 简单有效：适合小型应用或传统系统

### 纯Cookie鉴权方案的主要问题
**1. 安全性问题**

| 问题             | 说明                                                   | 示例                           |
| :--------------- | :----------------------------------------------------- | :----------------------------- |
| **密钥泄露风险** | 签名使用对称加密，密钥一旦泄露，攻击者可伪造任意Cookie | 服务器配置文件中密钥被意外公开 |
| **重放攻击**     | 即使有签名，攻击者可以复制有效Cookie在过期前重复使用   | 截获合法Cookie后重复发送       |
| **信息泄露**     | Cookie中直接存储用户信息，可能暴露敏感数据             | Cookie中包含`username=admin`   |
| **XSS风险**      | 如果未正确设置HttpOnly，可能被JS窃取                   | 恶意脚本读取`document.cookie`  |

**2. 功能性问题**

| 问题             | 说明                                   |
| :--------------- | :------------------------------------- |
| **无状态管理**   | 无法主动使特定Cookie失效（除非改密钥） |
| **权限变更延迟** | 用户权限变更后，必须等待Cookie过期     |
| **跨域限制**     | 严格SameSite策略下跨域使用困难         |
| **移动端不友好** | 原生App处理Cookie不如Web方便           |

**3. 扩展性问题**

| 问题               | 说明                     |
| :----------------- | :----------------------- |
| **分布式系统困难** | 需要所有节点共享相同密钥 |
| **微服务不友好**   | 服务间传递用户信息困难   |
| **性能瓶颈**       | 每次请求都需要验签计算   |

## Session

Session鉴权方式通过在服务端存储用户会话信息（Session），客户端仅保存Session ID（通常通过Cookie传递），服务器根据Session ID验证用户身份并维持登录状态。

* session 是另一种记录服务器和客户端会话状态的机制
* session 是基于 cookie 实现的，session 存储在服务器端，sessionId 会被存储到客户端的cookie 中

### session 认证流程
![](media/session鉴权工作原理.png)
* 用户第一次请求服务器的时候，服务器根据用户提交的相关信息，创建对应的 Session
* 请求返回时将此 Session 的唯一标识信息 SessionID 返回给浏览器
* 浏览器接收到服务器返回的 SessionID 信息后，会将此信息存入到 Cookie 中，同时 Cookie 记录此 SessionID 属于哪个域名
* 当用户第二次访问服务器的时候，请求会自动判断此域名下是否存在 Cookie 信息，如果存在自动将 Cookie 信息也发送给服务端，服务端会从 Cookie 中获取 SessionID，再根据 SessionID 查找对应的 Session 信息，如果没有找到说明用户没有登录或者登录失效，如果找到 Session 证明用户已经登录可执行后面操作。

根据以上流程可知，SessionID 是连接 Cookie 和 Session 的一道桥梁，大部分系统也是根据此原理来验证用户登录状态。

### session的由来
Java Web 应用中的 request、response 和 session 对象，是由像 Tomcat 这样的 Servlet 容器（也称为应用服务器）封装和管理的

**1. Servlet 容器的作用（例如 Tomcat）**

* Tomcat 是 Servlet 的实现容器。当客户端（如浏览器）发起一个 HTTP 请求时：
* Tomcat 接收到请求（通过监听端口）
* Tomcat 会解析这个请求并封装为一个 HttpServletRequest 对象
* 同时，它会创建一个 HttpServletResponse 对象用于返回结果然后 Tomcat 调用你写的 Servlet 或 Controller（比如 Spring MVC 中的 @Controller 方法），并把 request 和 response 传进去

**2. HttpServletRequest（封装请求）**

* 这个对象包含了客户端请求的所有信息，比如：
  * 请求参数（GET、POST）
  * Header 信息
  * Cookies
  * URI/URL
  * 请求方法等
  

String username = request.getParameter("username");

**3. HttpServletResponse（封装响应）**

* 这个对象用于向客户端返回内容，例如：
  * 设置响应状态码
  * 写入返回内容（HTML、JSON等）
  * 设置 cookie
  * 设置 header
```
response.setContentType("application/json");
response.getWriter().write("{\"msg\":\"ok\"}");
```
**4. HttpSession（封装会话）**

* Session 是跟某个客户端浏览器之间的“会话数据”，用于在多个请求之间保持状态：
* Tomcat 通过 request.getSession() 提供 session 对象 会在服务端维护 session 数据，通常通过 Cookie（如 JSESSIONID）在客户端标识唯一 session

```
HttpSession session = request.getSession();
session.setAttribute("user", userObject);
```
**Tomcat 处理请求时的 Session 绑定流程**

在一次新的 HTTP 请求到达时，Tomcat 会通过 Cookie 中的 JSESSIONID 来查找是否已有对应的 Session，然后将其绑定到新的 HttpServletRequest 对象上
* 客户端请求到达 Tomcat
  * 浏览器发起请求，请求中可能带有 Cookie: JSESSIONID=xxxxxxx
* Tomcat 解析 Cookie
  * Tomcat 从请求头中解析出 JSESSIONID
  * 如果存在这个 Cookie，并且服务器中有匹配的 Session 对象（即：服务器端的 session 存储中有对应的 ID）
* 绑定 Session 到 Request
  * Tomcat 就会把这个 session 绑定到新创建的 HttpServletRequest 对象中
  * 所以当你在 Servlet 中调用：
```
HttpSession session = request.getSession();
```
就会拿到之前同一个浏览器（会话）对应的 session 对象，而不是新建
* 没有 JSESSIONID 或找不到对应 Session
  * 如果请求中没有 JSESSIONID，或者服务端找不到这个 ID 对应的 session（可能是超时销毁了），Tomcat 就会创建一个新的 HttpSession
  * 然后在响应中设置新的 Cookie：
```
Set-Cookie: JSESSIONID=newid; Path=/; HttpOnly
```
**简化流程图：**

```
浏览器请求：
Cookie: JSESSIONID=abc123  ──► Tomcat
查找内存中是否有 abc123 的 Session
┌─────────────────────────────────────────┬
│ 有                                      │ 没有         
▼                                         ▼
绑定到 request                          创建新 Session
                            返回新的 Set-Cookie: JSESSIONID=xxx
```

### 传统Cookie-Session鉴权方案

#### 用户登录处理

```
// LoginServlet.java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 验证用户名密码（实际项目中应从数据库验证）
        if ("admin".equals(username) && "123456".equals(password)) {
            // 获取Session，如果不存在则创建
            HttpSession session = request.getSession();
            
            // 设置Session属性
            session.setAttribute("username", username);
            session.setAttribute("loginTime", System.currentTimeMillis());
            
            // 设置Session过期时间（可选，单位：秒）
            session.setMaxInactiveInterval(30 * 60); // 30分钟
            
            // 登录成功，重定向到主页
            response.sendRedirect("home.jsp");
        } else {
            // 登录失败
            request.setAttribute("errorMsg", "用户名或密码错误");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
```

#### 鉴权过滤器

```
// AuthFilter.java
@WebFilter("/*")
public class AuthFilter implements Filter {
    
    // 不需要鉴权的路径
    private static final String[] WHITE_LIST = {"/login", "/logout", "/register", "/static/"};
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // 检查是否在白名单中
        if (isWhiteList(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        
        // 检查Session
        HttpSession session = req.getSession(false); // 不创建新Session
        
        if (session == null || session.getAttribute("username") == null) {
            // 未登录，重定向到登录页面
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        
        // 已登录，继续处理请求
        chain.doFilter(request, response);
    }
    
    private boolean isWhiteList(String uri) {
        for (String whitePath : WHITE_LIST) {
            if (uri.startsWith(whitePath)) {
                return true;
            }
        }
        return false;
    }
    
    // 其他Filter方法...
}
```

#### 用户注销处理

```
// LogoutServlet.java
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 使当前Session失效
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // 重定向到登录页面
        response.sendRedirect("login.jsp");
    }
}
```

### 其他的seesion注意点

* 因为

### Session鉴权相比纯Cookie鉴权解决的问题

**1. 敏感信息安全性**

- **解决**：Session只在服务端存储用户信息，客户端仅保存无意义的Session ID
- **对比**：纯Cookie鉴权可能直接将用户信息存储在客户端Cookie中，存在泄露风险

**2. 数据篡改防护**

- **解决**：Session数据存储在服务端，客户端无法篡改用户信息
- **对比**：纯Cookie鉴权如果将用户角色/权限等信息直接存储在Cookie中，可能被篡改

**3. 存储容量**

- **解决**：Session可存储较大数据（用户对象、权限列表等）
- **对比**：Cookie有大小限制（通常4KB左右）

**4. 服务端控制能力**

- **解决**：服务端可以主动使Session失效（如强制下线/踢人）
- **对比**：纯Cookie鉴权只能等待Cookie过期

**5. 访问控制灵活性**

- **解决**：Session可存储复杂状态（如登录设备、IP绑定等）
- **对比**：纯Cookie鉴权状态管理能力有限

#### Session鉴权仍存在的未解决问题

**1. 扩展性问题**

- **问题**：集群环境下需要Session共享方案（如Redis）还有一种解决方案是 采用 Ngnix 中的 ip_hash 机制，将某个 ip的所有请求都定向到同一台服务器上，即将用户与服务器绑定。
- **影响**：增加系统复杂度，可能成为性能瓶颈

> ❗ 问题：Session 无法共享
>
> 每个服务器默认将用户的 `HttpSession` 保存在**本地内存**中：
>
> - 用户第一次访问：被分发到服务器 A，Session 存在 A 的内存。
> - 第二次访问：被分发到服务器 B，B 没有这个用户的 Session，用户“看起来”就像重新登录。
>
> ------
>
> ✅ 解决方案：**分布式 Session 存储**（用 Redis 代替本地内存）
>
> 将 Session 数据统一存入一个**中心存储**（如 Redis），所有服务节点都从 Redis 中读取/写入用户 Session 数据。

**2. CSRF攻击**

- **问题**：Session自动携带Cookie特性使其易受CSRF攻击
- **缓解**：仍需配合CSRF Token等防护措施

**3. 移动端适配**

- **问题**：原生移动应用对Cookie/Session支持不友好
- **影响**：需要额外处理（如显式传递Session ID）

**4. RESTful API兼容性**

- **问题**：无状态API设计中Session不符合REST原则
- **影响**：适合传统Web应用，但对纯API服务不够理想

> RESTful “无状态” 的真正含义
>
> 在 REST 架构风格中，“无状态”指的是：
>
> > ✅ **每一次请求，服务器都不能依赖于上一次的请求结果**，必须**自足**地处理完当前请求。
>
> 具体来说：
>
> - 客户端每次请求都要提供完整的信息（比如认证 Token）
> - 服务端不会将用户会话或登录状态保存到某个内存中，也**不会依赖会话来做逻辑判断**

**5. 性能开销**

- **问题**：每次请求都需要查询Session存储
- **影响**：相比无状态鉴权（如JWT）有额外性能消耗

**6. 跨域限制**

- **问题**：Cookie的SameOrigin策略限制跨域访问
- **影响**：微服务架构或前后端分离项目需要额外配置

#### 对比表格：Session鉴权 vs 纯Cookie鉴权

| 问题维度     | 纯Cookie鉴权       | Session鉴权        | 解决情况 |
| :----------- | :----------------- | :----------------- | :------- |
| 敏感信息存储 | 客户端存储，不安全 | 服务端存储，仅传ID | ✅ 解决   |
| 数据篡改     | 易被篡改           | 无法篡改服务端数据 | ✅ 解决   |
| 存储容量     | 有限（约4KB）      | 理论上无限制       | ✅ 解决   |
| 服务端控制   | 只能依赖过期时间   | 可主动销毁         | ✅ 解决   |
| 集群扩展     | 无状态，天然支持   | 需要共享存储       | ❌ 未解决 |
| CSRF防护     | 同样脆弱           | 同样脆弱           | ❌ 未解决 |
| 移动端兼容   | 需要额外处理       | 需要额外处理       | ❌ 未解决 |
| 无状态API    | 可设计为无状态     | 违背无状态原则     | ❌ 未解决 |
| 性能开销     | 低                 | 需要Session查询    | ❌ 未解决 |
| 跨域支持     | 受限               | 受限               | ❌ 未解决 |

## Token（令牌）

*  Token 鉴权有两种标准方式
  * UUID + Redis（传统方式）
  * JWT Token

* `HTTP标准的认证方式`

  *  `Authorization: Bearer <token>` 是 HTTP 标准中表示“我持有某个访问凭证”的方式，是现代 Web API 身份认证最推荐的做法。

  * `Authorization` 是一个 **HTTP 请求头字段**

  * `Bearer` 是一种 **认证类型（认证方式）**， 意为“持有者”。

  * `<token>` 是 **访问令牌**

  * 为什么大家都用 `Authorization: Bearer`：

    > 因为它是标准、安全、通用：
    >
    > 1. **HTTP标准定义的头部**
    >     它是符合 RFC 6750（OAuth2 标准） 的通用格式。
    > 2. **前后端分离标准做法**
    >     在 Web API 调用中，你无法用 Cookie，所以用 Header 传 Token 是最佳方式。
    > 3. **框架兼容性好**
    >     Spring Security、Express、Django、NestJS 等几乎所有框架都自动支持这个头。
    > 4. **与 JWT 完美配合**
    >     Bearer Token 天生就是为 JWT 设计的标准容器。

### 传统 UUID Token + Redis

服务端生成随机 UUID 作为 Token 并存储在 Redis 中，客户端携带该 Token 访问时，服务端通过查询 Redis 验证其有效性。

#### 传统 UUID Token + Redis示例

**实现思路：**

1. 登录成功时，不仅生成 Token，还要把 Token 和 用户绑定。
2. Redis 中 **保存关系：用户名 → Token**。
3. 每次请求拦截时，除了校验 Token 还需校验该 Token 是否是“该用户当前有效Token”。
4. 登录时如已存在旧 Token，**清除旧Token映射（踢人）**。

------

**登录时保存双向绑定**

```
@PostMapping("/login")
public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
    if (!verifyUser(username, password)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
    }

    // 先生成一个新 Token
    String token = UUID.randomUUID().toString();

    // 删除旧 Token（如果存在）
    String oldTokenKey = "LOGIN_USER_TOKEN:" + username;
    String oldToken = redisTemplate.opsForValue().get(oldTokenKey);
    if (oldToken != null) {
        redisTemplate.delete("TOKEN:" + oldToken); // 删除旧Token
    }

    // 保存：Token -> 用户
    redisTemplate.opsForValue().set("TOKEN:" + token, username, Duration.ofHours(1));
    // 保存：用户 -> Token
    redisTemplate.opsForValue().set("LOGIN_USER_TOKEN:" + username, token, Duration.ofHours(1));

    Map<String, String> result = new HashMap<>();
    result.put("token", token);
    return ResponseEntity.ok(result);
}
```

------

**拦截器中进行“双向校验”**

```
java复制编辑public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("请登录后访问");
            return false;
        }

        String token = authHeader.substring(7);
        String username = redisTemplate.opsForValue().get("TOKEN:" + token);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token无效或已过期");
            return false;
        }

        // 再校验用户名当前有效 Token 是否就是这个
        String latestToken = redisTemplate.opsForValue().get("LOGIN_USER_TOKEN:" + username);
        if (!token.equals(latestToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("此账号已在其他设备登录");
            return false;
        }

        request.setAttribute("currentUser", username);
        return true;
    }
}
```

------

**登出时清理双向绑定**

```
java复制编辑@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("Token格式错误");
    }

    String token = authHeader.substring(7);
    String username = redisTemplate.opsForValue().get("TOKEN:" + token);
    if (username != null) {
        redisTemplate.delete("LOGIN_USER_TOKEN:" + username);
    }

    redisTemplate.delete("TOKEN:" + token);

    return ResponseEntity.ok("登出成功");
}
```

重点解答：

* 和长亮项目中我写的登录极相似，不同点在于

1. 本示例中在登录时候会删除用户名绑定的token，而我的做法是拦截器中判断用户名绑定的token是否过期

2. 都用了双向绑定，在token绑定用户时候我做了扩展，绑定要用户对象而不单是用户名

* 如果不需要考虑踢人的业务，可以直接用一个redis的键值对管理token（token->userName）。而当需要踢人业务时，必须确定同一个系统用户userName对应的是哪个合法的token，这个映射关系可以存到数据库，也可以由缓存管理



### JWT Token：

服务端生成自包含的 JWT（JSON Web Token），客户端存储该 Token 并在请求时携带，服务端通过签名验证其合法性而无需存储 Token 本身。

**它是“无状态”的 token 认证方式的代表**，核心是：

* 将用户数据（如 id、角色、过期时间）放进 Token 的 payload 中

* 用私钥/密钥对整个数据做签名，防止篡改

* 客户端保存这个 Token，服务端只需用密钥验证签名即可，**无需数据库/Redis 存储**

举例：

JWT = `Base64(header).Base64(payload).Signature(secret)`

#### JWT基本介绍

JWT（JSON Web Token）是一种 **无状态认证机制**，将用户信息签名编码后返回给前端，后端无需存储会话数据即可验证身份。

结构如下（3段）：

```
Header.Payload.Signature
```

举例：

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjQzNDQ2NTYyLCJleHAiOjE2NDM0NDkxNjJ9.HjZ-RHOnu_Xg_qnJbE9RO_gv77FVw9BeO9KpmSe7R2U
```

详见：https://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html

### JWT Token鉴权示例

**依赖配置（使用 jjwt）**

```
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
  <version>0.9.1</version>
</dependency>
```

------

**JWT 工具类（生成 + 校验）**

```
public class JwtUtil {

    private static final String SECRET_KEY = "mySecretKey123456";

    // 生成 Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1小时过期
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 解析 Token
    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
```

------

**登录接口**

```
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        if (!"user".equals(username) || !"123456".equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }

        String token = JwtUtil.generateToken(username);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return ResponseEntity.ok(result);
    }
}
```

------

**JWT 拦截器实现（自定义认证拦截）**

```
java复制编辑public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("缺少或非法Token");
            return false;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = JwtUtil.validateToken(token);
            request.setAttribute("currentUser", claims.getSubject());
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token无效或已过期");
            return false;
        }
    }
}
```

------

**注册拦截器**

```
java复制编辑@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")      // 鉴权接口
                .excludePathPatterns("/auth/**"); // 放行登录注册
    }
}
```

------

**在控制器中获取当前用户**

```
java复制编辑@GetMapping("/api/user")
public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
    String user = (String) request.getAttribute("currentUser");
    return ResponseEntity.ok("当前用户是：" + user);
}
```

注意：JWT 是无状态的，**无法主动让服务端“删除”Token**，如果要实现“登出/踢人”，推荐：

** 添加黑名单机制：**

- 登录成功时记录 JWT 到白名单（Redis 或数据库）
- 登出时将 Token 加入黑名单
- 拦截器中不仅验证 Token 合法，还要检查是否在黑名单中

### JWT Token和UUID + Redis Token  鉴权方式对比

| 对比维度          | JWT Token（自包含）           | UUID + Redis（传统方式） |
| ----------------- | ----------------------------- | ------------------------ |
| 服务端是否存Token | ❌ 不存                        | ✅ 存                     |
| 安全性依赖        | 签名机制                      | Redis 访问控制           |
| 支持踢人机制      | ❌ 不支持（要额外做黑名单）    | ✅ 支持                   |
| 适合系统          | 分布式、微服务                | 单体应用、权限集中控制   |
| 过期控制          | 通过 payload 中的 `exp` 控制  | Redis 设定过期时间       |
| 登出实现          | 需黑名单支持                  | 直接删除Redis Key        |
| 实现复杂度        | 较高（需加密/解密、处理刷新） | 较低，容易理解           |

* 说明：Redis + UUID 并不是只能用于单体应用

  本质原因：**它是“有状态”方案**

  **Redis + UUID 必须在服务端存 Token → 用户映射状态**，所以：

  | 对比维度   | Redis + UUID         | JWT               |
  | ---------- | -------------------- | ----------------- |
  | 服务端状态 | 有（Token存在Redis） | 无（Token自包含） |
  | 分布式部署 | 要统一访问 Redis     | 无需共享存储      |
  | 容灾/限流  | Redis挂了会影响登录  | 不依赖 Redis      |

  

  因此，有些人在讲解时会简单地说：

  > Redis 有状态，分布式环境下更复杂，不如 JWT 无状态来得轻便 —— 所以更适合单体应用。

  ⚠️ **这是一种“实现管理复杂度”的考虑，不是技术限制。**

### RefreshToken 

**Refresh Token** 是一种用于 **安全延长用户会话** 的特殊令牌，通常与 **Access Token（访问令牌）** 配合使用，常见于 **OAuth 2.0** 和 **JWT（JSON Web Token）** 认证流程中。

------

**核心作用**

1. **解决 Access Token 的短期有效性问题**
   - Access Token 通常有效期较短（如 15 分钟~1 小时），用于直接访问受保护资源。
   - Refresh Token 有效期较长（如几天~数月），用于在 Access Token 过期后 **重新获取新的 Access Token**，无需用户重新登录。
2. **提升安全性**
   - 即使 Access Token 被泄露，攻击者也只能在短时间内使用（因有效期短）。
   - Refresh Token 一般存储于服务端或安全的客户端（如 HTTP-Only Cookie），且可被服务端主动撤销（如用户登出时）。

### 实战方案：JWT + RefreshToken + Redis 黑名单

**背景目标：**

1. **JWT** 实现无状态认证，性能高、无需查数据库
2. **RefreshToken** 支持 Token 刷新，延长登录周期
3. **Redis 黑名单** 实现登出、踢人、Token 主动失效

------

**整体流程架构图**

```
[客户端]
   |
   |--- 登录 ---> [认证服务]
   |              |
   |              |--- 生成 AccessToken + RefreshToken
   |              |--- AccessToken：1h过期（放Header）
   |              |--- RefreshToken：7d过期（存Redis）
   |
   |<-- AccessToken + RefreshToken
   |
   |--- 调用API（携带AccessToken）
   |
   |--- 过期时用 RefreshToken 换新 AccessToken
```

------

**技术选型**

- `jjwt`（用于签发、解析 JWT）
- `Spring Boot + Redis`（存储 RefreshToken、黑名单）
- `Interceptor`（JWT 拦截）
- `Scheduled`（定时清除过期黑名单）

------

**依赖配置（Maven）**

```
xml复制编辑<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
  <version>0.9.1</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

------

**JWT 工具类**

```
java复制编辑public class JwtUtil {
    private static final String SECRET_KEY = "yourSuperSecretKey";

    public static String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1小时
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
```

------

**登录接口（生成双Token）**

```
java复制编辑@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        // 模拟验证
        if (!"user".equals(username) || !"123456".equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }

        String accessToken = JwtUtil.generateAccessToken(username);
        String refreshToken = UUID.randomUUID().toString();

        // RefreshToken → 用户映射，7天有效
        redisTemplate.opsForValue().set("REFRESH_TOKEN:" + refreshToken, username, Duration.ofDays(7));

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        return ResponseEntity.ok(map);
    }
}
```

------

**拦截器验证 + 黑名单机制**

```
java复制编辑public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        String token = auth.substring(7);

        // 黑名单校验
        if (Boolean.TRUE.equals(redisTemplate.hasKey("JWT_BLACKLIST:" + token))) {
            response.setStatus(401);
            response.getWriter().write("Token已失效，请重新登录");
            return false;
        }

        try {
            Claims claims = JwtUtil.parseToken(token);
            request.setAttribute("currentUser", claims.getSubject());
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
```

------

**Token刷新接口（前端用RefreshToken换新AccessToken）**

```
java复制编辑@PostMapping("/refresh")
public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
    String username = redisTemplate.opsForValue().get("REFRESH_TOKEN:" + refreshToken);
    if (username == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken无效");
    }

    String newAccessToken = JwtUtil.generateAccessToken(username);
    return ResponseEntity.ok(Collections.singletonMap("accessToken", newAccessToken));
}
```

------

**登出接口（加黑名单 + 删除 RefreshToken）**

```
java复制编辑@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request, @RequestParam String refreshToken) {
    String auth = request.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("无效Token");
    }

    String token = auth.substring(7);

    // 加入黑名单（有效期等于剩余时间）
    try {
        Date expiration = JwtUtil.parseToken(token).getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set("JWT_BLACKLIST:" + token, "1", Duration.ofMillis(ttl));
    } catch (Exception ignored) {}

    redisTemplate.delete("REFRESH_TOKEN:" + refreshToken);
    return ResponseEntity.ok("登出成功");
}
```

------

**使用建议与安全提示**

| 项目         | 建议                                                     |
| ------------ | -------------------------------------------------------- |
| AccessToken  | 建议设置短期（如1小时），放 Header                       |
| RefreshToken | 长期（如7天），放 HttpOnly Cookie 或 localStorage        |
| 黑名单       | 放入 Redis，设置剩余有效期 TTL                           |
| 加密密钥     | 放配置文件中，生产环境使用非对称加密                     |
| 防刷机制     | 可给 RefreshToken 添加客户端信息校验（如IP、User-Agent） |







* 先锋ibs3.0项目，jwt，配合redis和数据源做双向绑定以达到t人效果

## 各方案的区别

|维度|纯Cookie|Session|Token|JWT|
|----|---|---|---|----|
|服务端状态|无|有|可选|无|
|安全性|中|高|高|高|
|扩展性|差|中|好|优|
|跨域支持|差|差|好|优|
|移动端支持|差|中|好|优|
|权限实时性|差|优|中|中|
|实现复杂度|简单|中|中|中|

**演进路线示例**

```
纯Cookie--添加服务端状态-->Session--去除状态管理-->Token--标准化-->JWT--增强安全-->短期JWT+刷新令牌
```
# 主要参考博客
* https://juejin.cn/post/6844904034181070861?searchId=20250520164202E0121AB8679ADF1243BE
  * 傻傻分不清之 Cookie、Session、Token、JWT

* https://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
  * JSON Web Token 入门教程（阮一峰 JWT）
