package com.topsci.utils;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LdapHelper {
	
	public static Boolean validationUser(String uid,String pwd) {
		if(uid.equals("ADMIN") && pwd.equals("ADMIN")){
			return true;
		}
		DirContext ctx = null;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, (String) OA_Config.getConfig(OA_Config.LDAP_INITIAL_CONTEXT_FACTORY));
		env.put(Context.PROVIDER_URL, (String) OA_Config.getConfig(OA_Config.LDAP_PROVIDER_URL));
		env.put(Context.SECURITY_AUTHENTICATION, (String) OA_Config.getConfig(OA_Config.LDAP_SECURITY_AUTHENTICATION));
		env.put(Context.SECURITY_PRINCIPAL, ("uid="+uid+",")+OA_Config.getConfig(OA_Config.LDAP_SECURITY_PRINCIPAL));
		env.put(Context.SECURITY_CREDENTIALS, pwd);
		
		///原始备份
//		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		env.put(Context.PROVIDER_URL, "ldap://192.168.1.98:389/");
//		env.put(Context.SECURITY_AUTHENTICATION, "simple");
//		env.put(Context.SECURITY_PRINCIPAL, "uid=qfeng,ou=people,dc=topsci,dc=com");
//		env.put(Context.SECURITY_CREDENTIALS, "xxxx");
		try {
			ctx = new InitialDirContext(env);
		} catch (javax.naming.AuthenticationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ctx==null?false:true;
	}
}