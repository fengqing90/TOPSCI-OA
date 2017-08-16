package com.topsci.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @dateTime 2013-7-5 下午3:36:53
 * @author FengQing
 * @description 通用ip过滤器，可设置多个格式 。正确格式：
 *              <ol>
 *              <li>[192.168.1.18~192.168.1.100]
 *              <li>[192.168.2.150~192.168.3.20]
 *              <li>[192.168.4.11~192.168.5.*]
 *              <li>[192.168.6.*~192.168.7.*]
 *              <li>[192.168.8.*]
 *              <ol>
 */
public class IP_Filter {

    private List<String[]> ip_address = new ArrayList<String[]>(0);

    private String filterType = null;

    private static Map<String, List<String[]>> cachDate = new HashMap<String, List<String[]>>(
        0);

    public String filter(String ip) throws Exception {
        String status = "remote_networks";

        this.init();

        this.checkLength_IP(ip);

        String[] decomposes_filter_ip = ip.split("\\.");
        this.checkLength_IP(decomposes_filter_ip);

        if (this.ip_address.size() == 0) {
            return "local_networks";
        }

        for (String[] ips : this.ip_address) {
            status = "remote_networks";
            String str_from_ip = null;
            String str_to_ip = null;
            if (ips.length == 1) {
                str_from_ip = ips[0];
                str_to_ip = str_from_ip;
            } else {
                str_from_ip = ips[0].trim();
                str_to_ip = ips[1].trim();
            }

            String[] decomposes_from_ip = str_from_ip.split("\\.");
            this.checkLength_IP(decomposes_from_ip);
            String[] decomposes_to_ip = str_to_ip.split("\\.");
            this.checkLength_IP(decomposes_to_ip);

            StringBuffer str_from = new StringBuffer();
            StringBuffer str_to = new StringBuffer();
            StringBuffer str_filter = new StringBuffer();
            for (int i = 0; i < 4; i++) {
                str_from.append(decomposes_from_ip[i].equals("*") ? "000"
                    : this.transformPartIP(decomposes_from_ip[i]));
                str_filter.append(decomposes_filter_ip[i].equals("*") ? "000"
                    : this.transformPartIP(decomposes_filter_ip[i]));
                str_to.append(decomposes_to_ip[i].equals("*") ? "999"
                    : this.transformPartIP(decomposes_to_ip[i]));
            }

            Long long_from_IP = new Long(str_from.toString());
            Long long_to_IP = new Long(str_to.toString());
            Long long_filter_IP = new Long(str_filter.toString());
//			System.out.println("str_from_ip:"+str_from_ip);
//			System.out.println("str_filter_ip:"+str_filter);
//			System.out.println("str_to_ip:"+str_to_ip);
//			System.out.println("***********");
//			System.out.println(long_from_IP+"==>long_from_IP");
//			System.out.println(long_filter_IP+"==>long_filter_IP");
//			System.out.println(long_to_IP+"==>long_to_IP");
            if (long_from_IP <= long_filter_IP
                && long_filter_IP <= long_to_IP) {
                status = "local_networks";
                break;
            }
//			System.err.println("status======================================================>"+status);
//			System.out.println("**********************************************");
        }
        return status + "," + ip;
//		return "remote_networks"+","+ip;
    }

    public IP_Filter(String filterType) {
        super();
        this.filterType = filterType;
    }

    private void init() throws Exception {
        this.ip_address = IP_Filter.cachDate.get(this.filterType);
        if (this.ip_address == null) {
            String newAddress = (String) OA_Config.getConfig(this.filterType);
            this.ip_address = this.split_IP(newAddress);
            IP_Filter.cachDate.put(this.filterType, this.ip_address);
        }
    }

    private List<String[]> split_IP(String properties_ip_address)
            throws Exception {
        List<String[]> new_ip_address = new ArrayList<String[]>();
        properties_ip_address = properties_ip_address.replaceAll("\\[", "");
        properties_ip_address = properties_ip_address.replaceAll("\\]", "");
        String[] array_ip = properties_ip_address.split(",");
        for (String group : array_ip) {
            if (group != null) {
                String[] temp = group.split("~");
                if (temp.length > 2) {
                    throw new Exception(
                        "目前只支持 [xxx.xxx.xxx.xxx~xxx.xxx.xxx.xxx] 格式，x可用*代替");
                }
                new_ip_address.add(temp);
            }
        }
        return new_ip_address;
    }

    /**
     * 补填IP
     *
     * @param partIP
     * @return
     * @throws Exception
     */
    private String transformPartIP(String partIP) {
        if (partIP != null) {
            partIP = partIP.trim();
            if (partIP.length() == 3) {
                return partIP;
            } else if (partIP.length() == 2) {
                return "0" + partIP;
            } else if (partIP.length() == 1) {
                return "00" + partIP;
            }
        }
        return "";
    }

    /**
     * 验证长度
     *
     * @param object_ip
     *        String[] or String
     * @throws Exception
     */
    private void checkLength_IP(Object object_ip) throws Exception {
        String[] decomposes_ip = null;
        if (object_ip instanceof String[]) {
            decomposes_ip = (String[]) object_ip;
        } else if (object_ip instanceof String) {
            decomposes_ip = object_ip.toString().trim().split("\\.");
        }
        if (decomposes_ip != null && decomposes_ip.length > 4) {
            throw new Exception("IP：" + object_ip + " ,位数超过了4位？");
        }
    }

    public String getFilterType() {
        return this.filterType;
    }

    public List<String[]> getIp_address() {
        return this.ip_address;
    }

    public static void clearCachDate() {
        IP_Filter.cachDate.clear();
    }
}
