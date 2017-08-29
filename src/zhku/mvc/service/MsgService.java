package zhku.mvc.service;

import zhku.mvc.annotation.Service;

/**
 * Created by ipc on 2017/8/26.
 */
@Service("msgService")
public class MsgService {

    public String getMsgFromDB(){
        System.out.println("访问数据库");
        return "从数据库中获得到信息";
    }
}
