package com.topsci.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @describle 配置参数
 * @author FengQing
 * @dateTime 2013-7-5 下午5:56:13 还需配置 Config.properties 并加以监控,此项功能后期在加
 */
public class OA_Config {

    private static final Logger log = LoggerFactory.getLogger(OA_Config.class);
    private static Map<String, Object> MAP_CONFIG = new HashMap<String, Object>();

    private static Long lastModifyTime = null;

    static {
        OA_Config.MAP_CONFIG.put(OA_Config.ALLOW_IP_ADDRESS, "*");
        OA_Config.MAP_CONFIG.put(OA_Config.SYSTEM_CONFIG_PROPERTIES_PATH, null);
        //....................
    }

    public static Object getConfig(String key) {
        if (key == null) {
            return null;
        }
        return OA_Config.MAP_CONFIG.get(key);
    }

    public static void putConfig(String key, Object obj) {
        // 保护默认值不被null冲掉
        if (obj == null) {
            return;
        }
        if (key != null) {
            OA_Config.MAP_CONFIG.put(key, obj);
        } else {
            OA_Config.log.warn("未读值取到，采用默认配置：" + key + "="
                + OA_Config.getConfig(key).toString());
        }
    }

    public void initParam() {
        try {
            File Config_properties = null;
            if (OA_Config
                .getConfig(OA_Config.SYSTEM_CONFIG_PROPERTIES_PATH) == null) {
                String url = this.getClass().getResource("/Config.properties")
                    .toURI().getPath();
                OA_Config.putConfig(OA_Config.SYSTEM_CONFIG_PROPERTIES_PATH,
                    url);
            }
            Config_properties = new File((String) OA_Config
                .getConfig(OA_Config.SYSTEM_CONFIG_PROPERTIES_PATH));

            Map<String, Object> map = OA_Config
                .readProperties(Config_properties.getPath());
            for (Entry<String, Object> item : map.entrySet()) {
                OA_Config.putConfig(item.getKey(), item.getValue());
//				System.out.println(item.getKey()+" : "+item.getValue());
                OA_Config.log.info(item.getKey() + " : " + item.getValue());
            }
            OA_Config.lastModifyTime = Config_properties.lastModified();
            IP_Filter.clearCachDate();
        } catch (Exception e) {
            OA_Config.log.error("配置文件错误，" + e);
            e.printStackTrace();
        }
    }

    /**
     * 读取properties的全部信息
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Map<String, Object> readProperties(String filePath)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Properties props = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        props.load(in);
        Enumeration<?> en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            map.put(key.trim(), props.getProperty(key));
//				System.out.println("key:"+key +"-|-Property:"+ props.getProperty(key));
        }
        in.close();
        return map;
    }

    /**
     * 监视Config.properties是否被修改
     */
    public void monitorConfigProperties() {
        Timer timer = new Timer();
        timer.schedule(new OA_Config().new Monitor_Configproperties(), 0,
            60000);
        OA_Config.log.info("监视Config.properties成功！");
    }

    class Monitor_Configproperties extends TimerTask {
        @Override
        public void run() {
            File Config_properties = new File((String) OA_Config
                .getConfig(OA_Config.SYSTEM_CONFIG_PROPERTIES_PATH));
            if (Config_properties.lastModified() != OA_Config.lastModifyTime) {
                OA_Config.log.info("配置文件Config.properties已被修改！时间{}",
                    DateTimeUtils.validateDateTime(new Date()));
                OA_Config.this.initParam();
            }
//			System.out.println(Config_properties.lastModified());
        }
    }

    // ==============================================================系统配置关键字
    /** 系统配置：允许登记工时IP集 **/
    public static final String ALLOW_IP_ADDRESS = "ALLOW_IP_ADDRESS";

    /** 系统配置：项目路径 **/
    public static final String SYSTEM_PATH = "SYSTEM_PATH";
    /** 系统配置：配置文件路径 **/
    public static final String SYSTEM_CONFIG_PROPERTIES_PATH = "SYSTEM_CONFIG_PROPERTIES_PATH";

    /** 系统配置：与LADP连接配置 **/
    public static final String LDAP_INITIAL_CONTEXT_FACTORY = "LDAP_INITIAL_CONTEXT_FACTORY";
    public static final String LDAP_PROVIDER_URL = "LDAP_PROVIDER_URL";
    public static final String LDAP_SECURITY_AUTHENTICATION = "LDAP_SECURITY_AUTHENTICATION";
    public static final String LDAP_SECURITY_PRINCIPAL = "LDAP_SECURITY_PRINCIPAL";

    /** 判定工作日阀值 **/
    public static final String THRESHOLD_WORKADAY = "THRESHOLD_WORKADAY";

    /** 考勤配置:上班-考勤可用开始时间 **/
    public static final String ATTENDANCE_ON_STARTDATE = "ATTENDANCE_ON_STARTDATE";
    /** 考勤配置:上班-考勤可用结束时间 **/
    public static final String ATTENDANCE_ON_ENDDATE = "ATTENDANCE_ON_ENDDATE";

    /** 考勤配置:下班-考勤可用开始时间 **/
    public static final String ATTENDANCE_OFF_STARTDATE = "ATTENDANCE_OFF_STARTDATE";
    /** 考勤配置:下班-考勤可用结束时间 **/
    public static final String ATTENDANCE_OFF_ENDDATE = "ATTENDANCE_OFF_ENDDATE";
    /** 考勤配置:上班时间 **/
    public static final String ATTENDANCE_ON = "ATTENDANCE_ON";
    /** 考勤配置:下班时间 **/
    public static final String ATTENDANCE_OFF = "ATTENDANCE_OFF";

}
