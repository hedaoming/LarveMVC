package zhku.mvc.servlet;

import zhku.mvc.annotation.Autowired;
import zhku.mvc.annotation.Controller;
import zhku.mvc.annotation.RequestMapping;
import zhku.mvc.annotation.Service;
import zhku.mvc.controller.MsgController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中心控制类
 * 1.  接收请求
 * 2.  指定到匹配的Handler上
 * 3.  调用Handler中的方法
 * 4.  返回view到response
 * Created by ipc on 2017/8/26.
 */
public class DispatcherServlet extends HttpServlet {

    //存储路劲下的所有文件路径名
    List<String> pathNames = new ArrayList<String>();

    //存储对象实例：key注解指定名称-value对象实例
    Map<String,Object> instanceMap = new HashMap<String,Object>();

    //存储映射关系：RequestMapping-Method
    Map<String,Object> handlerMap = new HashMap<String,Object>();

    @Override
    public void init() throws ServletException {
        //1. 扫描包:获得该包下的所有文件（类，接口，包）
        scanPackage("zhku.mvc");

        //2. 实例化类
        filterAndInstance();

        //3. 建立映射关系：RequestMapping-Handler
        handlerMap();

        //4. 实现注入:使用了Service注解的变量进行注入对象
        ioc();
    }

    /**
     * 扫描包中的所有文件，将文件名加入到集合中
     * 格式：zhku.mvc.controller.MsgController.class
     * @param packageName
     */
    private void scanPackage(String packageName) {
        //zhku.mvc-->.../zhku/mvc
        String packageNameChange = packageName.replace(".","/");
        URL url = this.getClass().getClassLoader().getResource("/"+packageNameChange);
        String filePath = url.getFile();
        File file = new File(filePath);
        String[] files = file.list();
        for(String fileName:files){
            File eachFile = new File(filePath+fileName);
            if(eachFile.isDirectory()){
                scanPackage(packageName+"."+eachFile.getName());
            }else{
                //格式：zhku.mvc.controller.MsgController.class
                pathNames.add(packageName+"."+eachFile.getName());
                System.out.println("扫描包所有文件："+packageName+"."+eachFile.getName());
            }
        }
    }

    /**
     * 实例化类，并存储到map中
     * @throws Exception
     */
    private void filterAndInstance(){
        if(pathNames.size()<=0){
            return ;
        }

        for(String pathName:pathNames){
            try{
                pathName = pathName.replace(".class","").trim();
                Class<?> clazz = Class.forName(pathName);
                if(clazz.isAnnotationPresent(Controller.class)){
                    Object instance = clazz.newInstance();
                    Controller controller = clazz.getAnnotation(Controller.class);
                    instanceMap.put(controller.value(),instance);
                    System.out.println("实例化对象Controller："+controller.value());
                }else if(clazz.isAnnotationPresent(Service.class)){
                    Object instance = clazz.newInstance();
                    Service service = clazz.getAnnotation(Service.class);
                    instanceMap.put(service.value(),instance);
                    System.out.println("实例化对象Service："+service.value());
                }else{
                    continue;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 建立映射关系：RequestMapping-Method
     */
    private void handlerMap() {
        if(instanceMap.size()<0){
            return;
        }
        for(Map.Entry<String,Object> entry:instanceMap.entrySet()){
            if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
                Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
                String controllerValue = controller.value();
                Method[] methods = entry.getValue().getClass().getMethods();
                for(Method method:methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String requestValue = requestMapping.value();
                        handlerMap.put("/"+controllerValue+"/"+requestValue,method);
                        System.out.println("建立映射关系："+"/"+controllerValue+"/"+requestValue);
                    }else{
                        continue;
                    }
                }
            }else{
                continue;
            }
        }
    }
    /**
     * 实现注入:使用了Service注解的变量进行注入对象
     */
    private void ioc() {
        if(instanceMap.size()<0){
            return ;
        }
        for(Map.Entry<String,Object> entry:instanceMap.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    field.setAccessible(true);
                    try {
                        //key:msgService,value:实例对象
                        field.set(entry.getValue(),instanceMap.get(autowiredValue));
                        System.out.println("注入对象："+entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    continue;
                }
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();
        Method method = (Method)handlerMap.get(path);
        MsgController controller = (MsgController) instanceMap.get(path.split("/")[1]);
        if(method!=null){
            try {
                method.invoke(controller,null);
                System.out.println("doPost()："+controller.toString()+","+method.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
