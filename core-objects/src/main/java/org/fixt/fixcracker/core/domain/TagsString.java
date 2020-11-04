package org.fixt.fixcracker.core.domain;

import org.fixt.fixcracker.core.FIXCrackerConst;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TagsString {
    private final Map<Integer, String> map;

    public TagsString(String str) {
        this.map = new HashMap<>();
        if (str == null || "".equals(str.trim())) return;
        String[] pairs = str.split(FIXCrackerConst.SOH_STR);
        String[] ar;
        int tag;
        for (String entry : pairs) {
            ar = entry.split("=", 2);
            if (ar.length < 2) continue;
            try {
                tag = Integer.parseInt(ar[0]);
            } catch (NumberFormatException ex) {
                continue;
            }
            map.put(tag, ar[1]);
        }
    }

    public String getTagValue(int tag) {
        return map.get(tag);
    }

    public void setTagValue(int tag, String value){
        map.put(tag,value);
    }

    @Override
    public String toString() {
        return map.entrySet().stream().map(e -> e.getKey().toString() + "=" + e.getValue()).collect(Collectors.joining(FIXCrackerConst.SOH_STR));
    }
}
