request.getParameter() 和 request.getAttribute()。它们都从 HttpServletRequest 中获取数据，但来源和用途完全不同。

# ✅ 1. request.getParameter(String name) 是什么？
* 来源：请求中传来的参数，通常来自：
* URL 查询参数（GET 请求中的 ?key=value）
* 表单提交的数据（POST 请求中带的 application/x-www-form-urlencoded）
* 作用：用于获取客户端发送到服务器的参数。

🌰 示例：
```
// URL: /login?username=alice
String username = request.getParameter("username"); // 结果是 "alice"
```
# ✅ 2. request.getAttribute(String name) 是什么？
* 来源：应用服务器或开发者在代码中主动设置的属性。
* 作用：在不同组件（如 Servlet、Filter、Controller）之间传递对象，不会通过网络传输。

🌰 示例：
```
request.setAttribute("user", new User("alice")); // 手动放进去
User user = (User) request.getAttribute("user"); // 再从同一个请求对象里取出来
```
这种属性通常用于在请求链中（如 Filter → Servlet → JSP）传递数据，而不是客户端传来的数据。

# 📌 总结对比：
|方法|来源|是否来自客户端|典型用途|
|----|---|---|----|
|getParameter("key")|请求的 URL 或表单|✅ 是|获取表单字段、查询参数等|
|getAttribute("key")|服务器端 setAttribute()|❌ 否|在请求处理中共享临时对象数据|

# 🧠 一句话记忆：
* getParameter() 取的是客户端给你的值；
* getAttribute() 取的是你自己（或服务器）设置的值。
