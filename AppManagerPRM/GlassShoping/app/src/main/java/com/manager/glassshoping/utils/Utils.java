package com.manager.glassshoping.utils;

import com.manager.glassshoping.model.GioHang;
import com.manager.glassshoping.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.0.102/glassshop/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current= new User();
}
