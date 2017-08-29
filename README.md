# LarveMVC
实现简易版本的SpringMVC功能（注解形式）
具体博文地址：https://hedaoming.github.io/2017/08/29/%E5%AE%9E%E7%8E%B0%E7%AE%80%E6%98%93SpringMVC-%E6%B3%A8%E8%A7%A3%E5%BD%A2%E5%BC%8F/

# 步骤
## 1. 实现注解
@Controller：指定Controller类
@Service：加入IoC容器中
@Autowired：从IoC容器中获取对象
@RequestMapping：请求对应的处理方法
## 2. 实现DispatcherServlet
### 2.1 扫描当前项目下的所有文件
流程：将项目中的文件遍历出来，包括包和类文件，并将文件路径加入pathNames中，如 zhku.mvc.controller.MsgController转换成zhku/mvc/controller/MsgController
**目的：文件路径是反射时需要**
### 2.2 通过反射实例化对象
操作：实例化Controller类和Service类，并加入instanceMap中
**目的：封装映射关系时和为@Service注入对象需要**
### 2.3 封装request和method的映射关系
操作：取出instanceMap中的Controller对象，并获取其内的RequestMapping注解的值value，这就是匹配的request，将这个value和method对象加入handlerMap中
**目的：request请求匹配相关操作方法时需要**
### 2.4 IoC：为使用@Service注解的属性注入对象
操作：遍历instanceMap，找到所有Field，注解为@Autowired的Field则获取其注解value，通过value到instanceMap中获取对象，并注入到这个Field中，实现注入对象功能。
**目的：注入对象**
### 2.5 doPost():处理请求，匹配对应的method
操作：获取实际请求路径，并从handlerMap中匹配对应的method对象，获取method之后通过反射调用方法即可。
**目的：处理请求**

