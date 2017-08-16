package com.topsci.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topsci.bean.Project;
import com.topsci.service.IProjectService;
import com.topsci.utils.Pager;
import com.topsci.utils.StringTools;

/**
 * @dateTime 2013-7-2 上午9:48:49
 * @author FengQing
 * @description 项目维护controller
 */
@Controller
@RequestMapping("/project/")
public class ProjectController {

    @Resource
    private IProjectService proService;

    /**
     * 此处可在spring中设置为全局默认绑定
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
            dateFormat, true));
    }

    @RequestMapping("initProjectForm.do")
    public ModelAndView initForm(HttpServletRequest request, ModelMap modelMap) {
        String type = StringTools.replaceStringtoNull(request
            .getParameter("type"));
        //////////////////////
        String query_projectName = StringTools.replaceStringtoNull(request
            .getParameter("query_projectName"));
        query_projectName = query_projectName != null ? query_projectName
            : StringTools.replaceStringtoNull(request
                .getAttribute("query_projectName"));
        String query_projectCode = StringTools.replaceStringtoNull(request
            .getParameter("query_projectCode"));
        query_projectCode = query_projectCode != null ? query_projectCode
            : StringTools.replaceStringtoNull(request
                .getAttribute("query_projectCode"));
        String pageNum = StringTools.replaceStringtoNull(request
            .getParameter("pageNum"));
        pageNum = pageNum != null ? pageNum : StringTools
            .replaceStringtoNull(request.getAttribute("pageNum"));
        //////////////////////////
        Project project = new Project();
        Pager pager = new Pager();
        if (pageNum != null) {
            pager.setPageNumber(Integer.valueOf(pageNum));
        } else {
            pager.setPageNumber(1);
        }
        pager = this.proService.findRecordsByNameOrCodeAndParamProject(project,
            query_projectName, query_projectCode, pager);
        modelMap.put("type", type);
        modelMap.put("pager", pager);
        modelMap.put("currProject", project);
        modelMap.put("query_projectCode", query_projectCode);
        modelMap.put("query_projectName", query_projectName);
        return new ModelAndView("forward:../jsp/Project.jsp")
            .addObject(project);
    }

    @RequestMapping(value = "saveOrUpdate.do", method = RequestMethod.POST)
    public String saveOrUpdate(HttpServletRequest request, ModelMap modelMap,
            @ModelAttribute("currProject") Project project) {
        String processType = request.getParameter("process_type");
        String processStatus = null;
        try {
            processStatus = this.proService.saveOrUpdate(project, processType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String query_projectCode = StringTools.replaceStringtoNull(request
            .getParameter("query_projectCode"));
        String query_projectName = StringTools.replaceStringtoNull(request
            .getParameter("query_projectName"));
        String pageNum = StringTools.replaceStringtoNull(request
            .getParameter("pageNum"));

        modelMap.put("currProject", project);
        modelMap.put("processStatus", processStatus);
        request.setAttribute("query_projectCode", query_projectCode);
        request.setAttribute("query_projectName", query_projectName);
        request.setAttribute("pageNum", pageNum);
        return "forward:initProjectForm.do";
    }

    @RequestMapping(value = "findProjectNameOrProjectCode.do")
    public void findProjectNameOrProjectCode(HttpServletRequest request,
            HttpServletResponse response) {
        String array_JS = this.proService
            .getAllProjectNameOrProjectCodeByType_RETURN_ARRAY_JS(
                request.getParameter("type"), request.getParameter("param"));

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

    @RequestMapping(value = "findProjectByName_JS.do")
    public void findProjectByName_JS(@RequestParam(value = "projectName",
            required = false) String projectName, HttpServletResponse response) {
        String array_JS = this.proService.findProjectByName_JS(projectName);

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
                out.close();
                out.flush();
            }
        }
    }

    @RequestMapping("/QueryProject.do")
    public String queryRecords(
            ModelMap modelMap,
            @RequestParam(value = "query_projectCode", required = false) String query_projectCode,
            @RequestParam(value = "query_projectName", required = false) String query_projectName,
            @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        Pager pager = new Pager();
        Project project = new Project();
        if (pageNum != null) {
            pager.setPageNumber(pageNum);
        } else {
            pager.setPageNumber(1);
        }
        query_projectName = StringTools.replaceStringtoNull(query_projectName);
        query_projectCode = StringTools.replaceStringtoNull(query_projectCode);
        pager = this.proService.findRecordsByNameOrCodeAndParamProject(project,
            query_projectName, query_projectCode, pager);

        modelMap.put("pager", pager);
        modelMap.put("currProject", new Project());
        modelMap.put("query_projectCode", query_projectCode);
        modelMap.put("query_projectName", query_projectName);
        return "../../jsp/Project";
    }

}
