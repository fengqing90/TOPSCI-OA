package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.topsci.bean.User;
import com.topsci.service.IUserService;
import com.topsci.utils.IP_Filter;
import com.topsci.utils.OA_Config;
import com.topsci.utils.SpringUtil;

public class TEST {
    @Before
    public void beforeTest() {
        SpringUtil.applicationContext = new ClassPathXmlApplicationContext(
            "applicationContext.xml");
    }

//	@Test
    public void test() {
        IUserService userService = (IUserService) SpringUtil
            .getBean("userServiceImpl");
        User entity = new User();
        entity.setUser_id(UUID.randomUUID().toString());
        entity.setUser_name("qfeng");
        userService.save(entity);
    }

    @Test
    public void testIP_fliter() {
        IP_Filter filter = new IP_Filter(
            (String) OA_Config.getConfig(OA_Config.ALLOW_IP_ADDRESS));
        try {
            System.out.println(filter.filter("192.168.1.1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//		LDAPConnection lc = new LDAPConnection();
//		try {
//			lc.connect("192.168.1.98", 389);
//			lc.bind(LDAPConnection.LDAP_V3, "cn=admin", "nidayeshiwo");
//			LDAPSearchResults rs = lc.search("dc=topsci,dc=com,dc=cn,uid=qfeng", LDAPConnection.SCOPE_SUB, "objectClass=*", null,false);
//			int count = 0;
//			while (rs.hasMore()) {
//				LDAPEntry entry = rs.next();
//				System.out.println(entry.getDN());
//				count++;
//			}
//			System.out.println("共有" + count + "条记录。");
//		} catch (LDAPException e) {
//
//			System.err.print("连接异常！   ");
//			e.printStackTrace();
//		}
//		int i=3;
//		for (int j = i-1; j >=0; j--) {
//			System.out.println(j);
//		}
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        System.out.println(sdf.format(new Date()));
        System.out.println(2 % 2);
    }
}
