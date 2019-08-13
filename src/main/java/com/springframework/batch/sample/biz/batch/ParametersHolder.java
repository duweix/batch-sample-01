package com.springframework.batch.sample.biz.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ParametersHolder {

    private ThreadLocal<Map<String, Object>> data = new ThreadLocal<>();

    public Map<String, Object> getData() {
        Map<String, Object> mapData = (Map<String, Object>) data.get();
        if (mapData == null) {
            mapData = new HashMap<String, Object>();
            data.set(mapData);
        }
        return mapData;
    }

    public Object getDataItem(String key) {
        Map<String>
        if (mapData == null) {
            mapData = new HashMap<String, Object>();
            data.set(mapData);
            return null;
        }
        return mapData.get(key);
    }

    public void setDataItem(String key, Object item) {
        Map<String, Object> mapData = (Map<String, Object>) data.get();
        if (mapData == null) {
            mapData = new HashMap<String, Object>();
            data.set(mapData);
        }
        mapData.put(key, item);
    }
}
