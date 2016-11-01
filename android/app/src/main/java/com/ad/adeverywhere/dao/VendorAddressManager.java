package com.ad.adeverywhere.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cwang on 9/26/16.
 */
public class VendorAddressManager {
    private static VendorAddressManager ourInstance = new VendorAddressManager();
    public static VendorAddressManager getInstance() {
        return ourInstance;
    }
    private VendorAddressManager() {}

    private Map<String, List<String>> mAddressMap = new HashMap<String, List<String>>();

    public void loadAddress(String province){
        loadAddressDummy();
        //TODO load real address from server
    }

    private void loadAddressDummy(){
        List<String> yangpu = new ArrayList<String>();
        yangpu.add("国泰路11号xxxx大厦");
        yangpu.add("五角场万达广场2001号");
        yangpu.add("国定路600弄xxxx号");
        yangpu.add("黄兴路xxxxxx");
        yangpu.add("武川路吴东路xxxx号");
        List<String> pudong = new ArrayList<String>();
        pudong.add("浦东陆家嘴xxxx号");
        pudong.add("浦东张江高科");
        pudong.add("浦东xxxxxx");
        pudong.add("浦东金融中心500号");
        pudong.add("东方体育中心");
        mAddressMap.put("杨浦", yangpu);
        mAddressMap.put("浦东", pudong);
    }

    public List<String> getAddressOfDistrict(String disctrict){
        if(disctrict == null || disctrict.length() == 0)
        {
            //return all
            ArrayList<String> addresses = new ArrayList<>();
            for(List<String> address: mAddressMap.values()){
                addresses.addAll(address);
            }
            return addresses;
        }
        else
            return mAddressMap.get(disctrict);
    }

    public List<String> getDistrictsOfProvince(String province){
        List<String> shanghai = new ArrayList<>();
        shanghai.add("杨浦");
        shanghai.add("浦东");
        shanghai.add("嘉定");
        return shanghai;
    }
}
