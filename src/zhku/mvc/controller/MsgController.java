package zhku.mvc.controller;

import zhku.mvc.annotation.Autowired;
import zhku.mvc.annotation.Controller;
import zhku.mvc.annotation.RequestMapping;
import zhku.mvc.service.MsgService;

import java.io.PrintWriter;

/**
 * Created by ipc on 2017/8/26.
 */
@Controller("msg")
public class MsgController {

    @Autowired("msgService")
    MsgService msgService;

    @RequestMapping("msg")
    public void getMsg(){
        String value = msgService.getMsgFromDB();
        System.out.println("Controller方法getMsg");
    }
}
