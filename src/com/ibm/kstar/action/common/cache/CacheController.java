package com.ibm.kstar.action.common.cache;

import com.ibm.kstar.cache.CacheData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangchao on 2017/4/19.
 */
@RequestMapping("/cache")
@Controller
public class CacheController {

    @RequestMapping(value = "/reload",method = RequestMethod.GET)
    @ResponseBody
    public String reload() {

        System.out.println("刷新缓存======》");
        CacheData.getInstance().init();

        return "success";
    }
}
