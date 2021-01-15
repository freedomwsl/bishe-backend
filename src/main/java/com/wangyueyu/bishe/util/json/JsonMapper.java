//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wangyueyu.bishe.util.json;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapper {
    public JsonMapper() {
    }

    public Map<String, Object> convertToMap(String json) {
        return (Map)JSON.parseObject(json, Map.class);
    }

    public Map<String, Object> convertToMap(Object obj) {
        return this.convertToMap(this.convertToJson(obj));
    }

    public String convertToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public List<Map<String, Object>> convertToList(Collection<String> jsons) {
        List<Map<String, Object>> list = new ArrayList();
        Iterator var3 = jsons.iterator();

        while(var3.hasNext()) {
            String json = (String)var3.next();
            if (json != null) {
                list.add(this.convertToMap(json));
            }
        }

        return list;
    }
}
