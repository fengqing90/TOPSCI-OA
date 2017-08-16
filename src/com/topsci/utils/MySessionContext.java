package com.topsci.utils;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class MySessionContext {
    private static MySessionContext instance;
    private HashMap mymap;

    private MySessionContext() {
        this.mymap = new HashMap();
    }

    public static MySessionContext getInstance() {
        if (MySessionContext.instance == null) {
            MySessionContext.instance = new MySessionContext();
        }
        return MySessionContext.instance;
    }

    public synchronized void AddSession(HttpSession session) {
        if (session != null) {
            this.mymap.put(session.getId(), session);
        }
    }

    public synchronized void DelSession(HttpSession session) {
        if (session != null) {
            this.mymap.remove(session.getId());
        }
    }

    public synchronized HttpSession getSession(String session_id) {
        if (session_id == null) {
            return null;
        }
        return (HttpSession) this.mymap.get(session_id);
    }

}
