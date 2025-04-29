# 简介

* 回调地狱（Callback Hell）是指在异步编程中，由于多层嵌套的回调函数导致的代码难以理解和维护的情况。
* 在Java 8中，虽然回调地狱不像JavaScript中那么常见，但在使用异步API或回调接口时仍然可能出现。

# 回调地狱相关概念：

## 回调函数

当一个函数作为参数传入另一个参数中，并且它不会立即执行，只有当满足一定条件后该函数才可以执行，这种函数就称为回调函数。

## 异步任务

与之相对应的概念是“同步任务”，同步任务在主线程上排队执行，只有前一个任务执行完毕，才能执行下一个任务。异步任务不进入主线程，而是进入异步队列，前一个任务是否执行完毕不影响下一个任务的执行。

# Java 8中的回调地狱示例

```
userService.login("user", "pass", user -> {
    orderService.getOrders(user, orders -> {
        inventoryService.checkStock(orders, stock -> {
            paymentService.processPayment(user, orders, payment -> {
                // 处理支付结果
            });
        });
    });
});
```

* 回调地狱的问题
  * 代码可读性差：多层嵌套使代码难以阅读和理解
  * 错误处理困难：需要在每一层单独处理错误
  * 维护困难：修改或添加新操作会使嵌套更深
  * 调试困难：调用栈变得复杂

> 调用栈（Call Stack）是程序执行时用来跟踪函数调用的数据结构，它有 **后进先出（LIFO）** 的特性。当函数被调用时，它的信息被压入（push）栈顶；当函数返回时，它的信息被弹出（pop）栈。
>
> * 在同步编程中，调用栈是清晰且线性的
> * 在回调地狱中，由于异步回调的嵌套，调用栈变得复杂
>   【这块调用栈没完全看懂】

# Java 8中的解决方案

Java 8引入了一些特性来缓解回调地狱：

* CompletableFuture：提供了更优雅的异步编程方式

```
CompletableFuture.supplyAsync(() -> userService.login("user", "pass"))
    .thenCompose(user -> orderService.getOrders(user))
    .thenCompose(orders -> inventoryService.checkStock(orders))
    .thenAccept(stock -> paymentService.processPayment(stock))
    .exceptionally(ex -> {
        // 统一错误处理
        return null;
});
```

* Lambda表达式：虽然不能消除嵌套，但可以使回调代码更简洁
* 方法引用：进一步简化回调代码

## CompletableFuture-更优雅的异步编程方式

从一个简单的回调开始，逐步增加复杂度，并对比 传统回调写法 和 CompletableFuture 写法

### 单层回调（1个异步操作）

场景：用户登录（loginAsync），成功后打印用户信息。

**传统回调写法**

```
// 定义方法（3个参数：user, pass, callback）
void loginAsync(String user, String pass, Consumer<User> callback) {
    new Thread(() -> {
        User result = userService.login(user, pass); // 模拟登录
        callback.accept(result); // 回调
    }).start();
}

// 调用方式
loginAsync("admin", "123456", user -> {
    System.out.println("登录成功: " + user);
});
```

* 问题：回调直接嵌套在参数里，无法链式扩展。

**改用 CompletableFuture**

```
// 定义方法（2个参数，返回 Future）
CompletableFuture<User> loginAsync(String user, String pass) {
    return CompletableFuture.supplyAsync(() -> {
        return userService.login(user, pass); // 异步执行
    });
}

// 调用方式
loginAsync("admin", "123456")
    .thenAccept(user -> {
        System.out.println("登录成功: " + user);
    });
```

优势：

* 不再需要回调参数，而是通过 .thenAccept() 处理结果。
* 可以继续链式调用其他操作。
* 【callback.accept(result)发生在loginAsync的最后一步，可以通过CompletableFuture此步骤拆到下一个步骤链式执行】
* 【如callback.accept(result)不是loginAsync的最后一步，可考虑】

### 两层回调（2个异步操作）

* 场景：用户登录 → 获取订单（loginAsync → getOrdersAsync）。
  **(1)传统回调写法（嵌套回调）**

```
void getOrdersAsync(User user, Consumer<List<Order>> callback) {
    new Thread(() -> {
        List<Order> orders = orderService.getOrders(user);
        callback.accept(orders);
    }).start();
}

// 调用方式（开始出现嵌套）
loginAsync("admin", "123456", user -> {
    getOrdersAsync(user, orders -> {
        System.out.println("获取订单: " + orders);
    });
});
```

* 问题：两层嵌套，代码向右增长

**(2) 改用 CompletableFuture**

```
CompletableFuture<List<Order>> getOrdersAsync(User user) {
    return CompletableFuture.supplyAsync(() -> {
        return orderService.getOrders(user);
    });
}

// 调用方式（链式调用）
loginAsync("admin", "123456")
    .thenCompose(user -> getOrdersAsync(user)) // 连接异步操作
    .thenAccept(orders -> {
        System.out.println("获取订单: " + orders);
    });
```

关键点：

* thenCompose：用于连接两个 CompletableFuture（避免嵌套）。
* 代码保持扁平化，易于扩展。

### 异常传播

* 回调地狱中，错误无法自动向上传播，需要手动处理每一层的错误

**传统回调**

```
loginAsync("admin", "123456", user -> {
    getOrdersAsync(user, orders -> {
        checkStockAsync(orders, stock -> {
            System.out.println("库存状态: " + stock);
        }, stockError -> {
            System.err.println("检查库存失败: " + stockError);
        });
    }, ordersError -> {
        System.err.println("获取订单失败: " + ordersError);
    });
}, loginError -> {
    System.err.println("登录失败: " + loginError);
});
```

> 问题：错误处理分散，重复代码多。

**CompletableFuture（统一错误处理）**

```
loginAsync("admin", "123456")
    .thenCompose(user -> getOrdersAsync(user))
    .thenCompose(orders -> checkStockAsync(orders))
    .thenAccept(stock -> {
        System.out.println("库存状态: " + stock);
    })
    .exceptionally(ex -> {
        System.err.println("操作失败: " + ex.getMessage());
        return null;
    });
```

> 优势：
>
> * 只需一个 exceptionally 捕获所有异常。
> * 代码更简洁，维护更方便。

### 关键结论

1. CompletableFuture 的核心改进：

* 用 链式调用（thenApply/thenCompose）替代嵌套回调。
* 用 exceptionally 统一处理错误。
  2.适用场景：
* 任何需要多个异步操作顺序执行的场景（如：登录 → 查询 → 下单 → 支付）。
* 需要避免回调地狱的 Java 8+ 项目。
