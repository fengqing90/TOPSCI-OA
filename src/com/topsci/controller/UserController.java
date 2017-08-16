package com.topsci.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.topsci.bean.User;
import com.topsci.service.IUserService;
import com.topsci.utils.CustomStringEditor;
import com.topsci.utils.IP_Filter;
import com.topsci.utils.LdapHelper;
import com.topsci.utils.OA_Config;
import com.topsci.utils.Pager;
import com.topsci.utils.StringTools;

/**
 * @date 2013-6-25 下午04:56:17
 * @author FengQing
 * @description 用户Controller
 */

@Controller
@RequestMapping("/user/")
//根部过滤
@SessionAttributes(value = { "user", "isRemote" })
//获取自动获取session中user对象放到ModelMap中
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    /**
     * form表单中Data类型的处理
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
            dateFormat, true));
        binder.registerCustomEditor(String.class, new CustomStringEditor());
    }

    @RequestMapping("userLogin.do")
    public String login(HttpServletRequest request, ModelMap modelMap) {
        String url = "../../Error";
        String errorMsg = null;
        try {
            String userName = StringTools.replaceStringtoNull(request
                .getParameter("userName"));
            String userPwd = StringTools.replaceStringtoNull(request
                .getParameter("userPwd"));
            User entity = new User(userName, userPwd);
            entity = this.userService.login(entity);
//			if(entity!=null){ //只用输入帐号不用密码
//				userPwd = userPwd==null?entity.getUser_pwd():userPwd;
//			}
            //TODO 测试代码
            Boolean isValid = LdapHelper.validationUser(userName, userPwd);
//			Boolean isValid = true;
            if (isValid) {
                if (entity == null) {
                    entity = new User();
                    entity.setAlias_name(userName);
                    entity.setCreate_date(new Date());
                    entity.setUser_id(UUID.randomUUID().toString());
                    if (userName.equals("ADMIN") && userPwd.equals("ADMIN")) {
                        entity.setUser_type("ADMIN");
                    } else {
                        entity.setUser_type("EMPLOYE");
                    }
                    this.userService.save(entity);
                }
                modelMap.put("user", entity);
                modelMap.put("session_id", request.getSession().getId());
//				modelMap.addAttribute("session_id", request.getSession().getId());
//				System.out.println(request.getSession().getId()+"初始");
                modelMap.addAttribute("user", entity);
//				modelMap.addAttribute("isRemote",new IP_Filter(OA_Config.ALLOW_IP_ADDRESS).filter(request.getRemoteAddr()));
                url = "../../Index";
            } else {
                errorMsg = "<账号>或<密码>不正确。";
                if (userName.equals("ADMIN") && userPwd.equals("ADMIN")) {
                    entity = new User();
                    entity.setAlias_name(userName);
                    entity.setCreate_date(new Date());
                    entity.setUser_id(UUID.randomUUID().toString());
                    entity.setUser_type("ADMIN");
                    modelMap.put("user", entity);
                    modelMap.put("session_id", request.getSession().getId());
                    modelMap.addAttribute("user", entity);
                    url = "../../Index";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = "<账号>或<密码>不正确。";
        }
        modelMap.put("errorMsg", errorMsg);
        return url;
    }

    @RequestMapping("initUserForm.do")
    public ModelAndView initForm(HttpServletRequest request, ModelMap modelMap) {
        String type = StringTools.replaceStringtoNull(request
            .getParameter("type"));
        Object sessionUser = request.getSession().getAttribute("user");
        User user = sessionUser == null ? null : (User) sessionUser;
        if (user == null || type != null && type.equals("reg")) {
            user = new User();
        }
        modelMap.put("currUser", this.userService.get(user.getUser_id()));//如果页面用到 Spring MVC的form绑定数据必须传递一个User对象
        modelMap.put("type", type);
        return new ModelAndView("forward:../jsp/User.jsp").addObject(user);
    }

    @RequestMapping(value = "saveOrUpdate.do", method = RequestMethod.POST)
    public String saveOrUpdate(HttpServletRequest request, ModelMap modelMap,
            @ModelAttribute("currUser") User user) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        String processType = request.getParameter("processType");
        String type = StringTools.replaceStringtoNull(request
            .getParameter("type"));

        String processStatus = null;

        try {
            processStatus = this.userService.saveOrUpdate(processType, user);
        } catch (DataIntegrityViolationException e) {
            modelMap.put("errorMsg", "用户名可能已经存在，请尝试用其他名称！");
            return "forward:../Error.jsp";
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (sessionUser.getUser_id().equals(user.getUser_id())) {
            modelMap.addAttribute("user", user);
        }

        modelMap.put("currUser", user);
        modelMap.put("processStatus", processStatus);
        if (type != null && type.equals("reg")) {
            return "forward:../Index.jsp";
        } else if (type != null && type.equals("QueryUser")) {
            return "forward:QueryUser.do";
        }
        return "forward:../jsp/User.jsp";
    }

    @RequestMapping("QueryUser.do")
    public String queryUser(ModelMap modelMap, @RequestParam(
            value = "query_userName", required = false) String query_userName,
            @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        Pager pager = new Pager();
        if (pageNum != null) {
            pager.setPageNumber(pageNum);
        } else {
            pager.setPageNumber(1);
        }
        pager = this.userService.queryUserByName(query_userName, pager);
        modelMap.put("currUser", new User());
        modelMap.put("pager", pager);
        modelMap.put("query_userName", query_userName);
        return "forward:../jsp/query/QueryUser.jsp";
    }

    @RequestMapping("QueryUserName.do")
    public void queryUser(
            @RequestParam(value = "userName", required = false) String userName,
            HttpServletResponse response) {
        String array_JS = this.userService.findUserByName_JS(userName);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (array_JS != null) {
                out.print(array_JS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    @RequestMapping("testIP.do")
    public void testIP() {
        IP_Filter filter = new IP_Filter(OA_Config.ALLOW_IP_ADDRESS);
        try {
//			 [192.168.1.18~192.168.1.100]
//			 [192.168.2.150~192.168.3.20]
//			 [192.168.4.11~192.168.5.*]
//			 [192.168.6.*~192.168.7.*]
//			 [192.168.8.*]
//			System.out.println("**************************************************************************");
            String ip = "192.168.8.1";
            System.err
                .println("***********************************************" + ip
                    + ":" + filter.filter(ip));
            System.out.println("过滤类型：" + filter.getFilterType());
            System.out.println("规则集合：");
            for (String[] array : filter.getIp_address()) {
                System.out.println(Arrays.toString(array));
            }
            System.out
                .println("**************************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "updateUser.do")
    public void updateUser(@RequestParam("userName") String userName,
            @RequestParam("userPwd") String userPwd,
            @RequestParam("user_type") String user_type,
            HttpServletResponse response) {
        PrintWriter out = null;
        String status = null;
        Boolean isValid = LdapHelper.validationUser(userName, userPwd);
        try {
            out = response.getWriter();
            if (isValid) {
                User user = new User();
                user.setAlias_name(userName);
                List<User> userList = this.userService
                    .findEntityByEntityParams_equals(user, null);
                if (userList != null && userList.size() == 1) {
                    user = userList.get(0);
                    user.setUser_type(user_type);
                    status = this.userService.saveOrUpdate(user);
                    out.print(status);
                }
            } else {
                out.print("用户名或密码错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (out != null) {
                out.print("操作失败！");
            }
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
