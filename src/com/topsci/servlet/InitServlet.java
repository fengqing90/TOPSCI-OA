package com.topsci.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.topsci.utils.OA_Config;

public class InitServlet extends HttpServlet {
    private static final long serialVersionUID = -2045270875245578067L;

    @Override
    public void init() throws ServletException {
        OA_Config OA_Config = new OA_Config();
        OA_Config.initParam();
        OA_Config.monitorConfigProperties();
        super.init();
    }
}
