package com.topsci.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.propertyeditors.CustomDateEditor;
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

import com.topsci.bean.Project;
import com.topsci.bean.User;
import com.topsci.bean.WorkingHours;
import com.topsci.infoBean.WorkingHoursInfoBean;
import com.topsci.infoBean.WorkingHoursPercentageInfoBean;
import com.topsci.service.IWorkingHoursService;
import com.topsci.utils.CommonUtil;
import com.topsci.utils.CustomProjectEditor;
import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.Pager;
import com.topsci.utils.SpringUtil;
import com.topsci.utils.StringTools;

/**
 * @date 2013-6-28 下午15:55:36
 * @author FengQing
 * @description 工时Controller
 */
@Controller
@SessionAttributes("user")
public class WorkingHoursController {

    @Resource
    private IWorkingHoursService workService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
            dateFormat, true));
        binder.registerCustomEditor(Project.class, new CustomProjectEditor());
    }

    @RequestMapping("/work/initWorkForm.do")
    public ModelAndView initForm(HttpServletRequest request, ModelMap modelMap,
            @ModelAttribute("user") User sessionUser) {
        String modifyDate = StringTools.replaceStringtoNull(request
            .getParameter("modifyDate"));
        modifyDate = modifyDate != null ? modifyDate : StringTools
            .replaceStringtoNull(request.getAttribute("modifyDate"));
        WorkingHours work = new WorkingHours();
        if (modifyDate != null) {
            work.setWorkaday(DateTimeUtils.validateDate(modifyDate));
        }
        modelMap.put("currWork", work);
//		modelMap.put("userProjects", sessionUser.getProjectList());
        modelMap.put("page", this.workService.findRecords(sessionUser, null,
            null, modifyDate, null));
        return new ModelAndView("forward:../jsp/WorkingHours.jsp")
            .addObject(work);
    }

    @RequestMapping(value = "/work/saveOrUpdate.do",
            method = RequestMethod.POST)
    public String saveOrUpdate(HttpServletRequest request, ModelMap modelMap,
            @ModelAttribute("currWork") WorkingHours work,
            @ModelAttribute("user") User sessionUser) {
        String type = request.getParameter("type");
        String projectCode = request.getParameter("project");
        String processStatus = null;
        try {
            processStatus = this.workService.saveOrUpdate(work, sessionUser,
                type, projectCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelMap.put("currWork", work);
        modelMap.put("processStatus", processStatus);
        request.setAttribute("modifyDate",
            DateTimeUtils.validateDate(work.getWorkaday()));
        return "forward:initWorkForm.do";
    }

    @RequestMapping("/work/QueryWork.do")
    public String queryRecords(ModelMap modelMap,
            @ModelAttribute("user") User user, @RequestParam(
                    value = "starDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        try {
            Pager pager = new Pager();
            if (pageNum != null) {
                pager.setPageNumber(pageNum);
            } else {
                pager.setPageNumber(1);
            }
            pager = this.workService.findRecordsGroupByUser(user, startDate,
                endDate, pager);
            modelMap.put("pager", pager);
            modelMap.put("starDate", startDate);
            modelMap.put("endDate", endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forward:../jsp/query/QueryWorkingHours.jsp";
    }

    @RequestMapping("/work/QueryWorkPercentage.do")
    public String queryWorkPercentage(
            ModelMap modelMap,
            @ModelAttribute("user") User user,
            @RequestParam(value = "starDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "processType", required = false) String processType,
            HttpServletResponse response) {
        Pager pager = new Pager();
        if (pageNum != null) {
            pager.setPageNumber(pageNum);
            pager.setPageSize(Integer.MAX_VALUE);
        }
        WorkingHoursPercentageInfoBean percentageInfoBean = new WorkingHoursPercentageInfoBean();
        try {
            percentageInfoBean = this.workService.queryWorkPercentage(user,
                startDate, endDate, pager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelMap.put("percentageInfoBean", percentageInfoBean);
        modelMap.put("starDate", startDate);
        modelMap.put("endDate", endDate);
        if (percentageInfoBean != null) {
            modelMap.put("columnSize", percentageInfoBean.getColumnList()
                .size());
        }

        if (processType != null && processType.equals("reportExcel")) {
            try {
                String title = "工时统计";
                if (StringTools.replaceStringtoNull(startDate) != null
                    && StringTools.replaceStringtoNull(endDate) != null) {
                    title = startDate + "~~" + endDate + "  工时统计";
                } else {
                    title = DateTimeUtils.dateFormat.format(new Date())
                        + "  工时统计";
                }

                OutputStream out = null;
                /////////////
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/json; charset=utf-8");
                response.setHeader("Cache-Control", "no-cache");
                response.reset();// 清空输出流
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", new String(
                    ("attachment; filename=" + title + ".xls").getBytes("GBK"),
                    "ISO-8859-1"));
                out = response.getOutputStream();
                /////////////

                if (percentageInfoBean == null) {
                    return "";
                }
                List<Project> columnList = percentageInfoBean.getColumnList();
                List<WorkingHoursInfoBean> valueList = percentageInfoBean
                    .getValueList();
                Set<Map.Entry<String, Integer>> columnGroupEntry = percentageInfoBean
                    .getColumnListGroup().entrySet();
//				Map<K, V> columnGroupEntry = percentageInfoBean.getColumnListGroup();

                WritableWorkbook workBook = Workbook.createWorkbook(out);
                WritableSheet sheet = workBook.createSheet(title, 0);

                //主标题样式
                WritableCellFormat titleFormat = new WritableCellFormat(
                    new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
                titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
                titleFormat
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                sheet.addCell(new Label(0, 0, title, titleFormat));
                sheet.mergeCells(0, 0, columnList.size() + 2, 0);
                //列标题样式
                WritableCellFormat columnFormat = new WritableCellFormat(
                    new WritableFont(WritableFont.TIMES,
                        WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD));
                columnFormat.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
                columnFormat.setAlignment(jxl.format.Alignment.CENTRE);
                columnFormat
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                columnFormat.setWrap(true);

                sheet.setRowView(0, 850);
                sheet.setRowView(1, 750);
                sheet.setRowView(2, 500);
                sheet.mergeCells(0, 1, 0, 2);
                sheet.addCell(new Label(0, 1, "姓名", columnFormat));

                Integer groupIndex = 0;
                for (Entry<String, Integer> entry : columnGroupEntry) {
//					System.out.println((groupIndex+1)+"|"+entry.getKey()+":"+entry.getValue());
//					System.out.println((groupIndex+1)+"~"+(entry.getValue()+groupIndex));
                    sheet.mergeCells(groupIndex + 1, 1, entry.getValue()
                        + groupIndex, 1);
                    sheet.addCell(new Label(groupIndex + 1, 1, entry.getKey(),
                        columnFormat));
                    groupIndex = entry.getValue() + groupIndex;
                }
                sheet.mergeCells(groupIndex + 1, 1, groupIndex + 2, 1);
                sheet.addCell(new Label(groupIndex + 1, 1, "合计", columnFormat));

                for (int i = 0; i < columnList.size(); i++) {
                    Project project = columnList.get(i);
//					System.out.println(project.getProject_name()+":"+project.getProject_name().length()*2);
//					Integer width = project.getProject_name().length();
                    sheet.setColumnView(i + 1, project.getProject_name()
                        .length() + 5);
                    sheet.addCell(new Label(i + 1, 2,
                        project.getProject_name(), columnFormat));
                }
                sheet.addCell(new Label(columnList.size() + 1, 2, "已登记",
                    columnFormat));
                sheet.addCell(new Label(columnList.size() + 2, 2, "未登记",
                    columnFormat));
                //明细列样式
                WritableCellFormat dataFormat = new WritableCellFormat();
                dataFormat.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
                dataFormat.setAlignment(jxl.format.Alignment.CENTRE);
                dataFormat
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                dataFormat.setWrap(true);
                //小于100的样式
                WritableCellFormat colorformat = new WritableCellFormat();
                colorformat = new WritableCellFormat();
                colorformat.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
                colorformat.setAlignment(jxl.format.Alignment.CENTRE);
                colorformat
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                colorformat.setWrap(true);
                colorformat.setBackground(jxl.format.Colour.ORANGE);
                for (int i = 0; i < valueList.size(); i++) {
                    sheet.setRowView(i + 3, 400);
                    WorkingHoursInfoBean infoBean = valueList.get(i);
                    sheet.addCell(new Label(0, i + 3, infoBean.getUser()
                        .getUser_name(), columnFormat));
                    for (int j = 0; j < infoBean.getWorkList().size(); j++) {
                        WorkingHours work = infoBean.getWorkList().get(j);
                        if (work != null) {
                            sheet.addCell(new Label(j + 1, i + 3, CommonUtil
                                .formatDoubleToString(
                                    work.getWorking_hours_PCT(), null),
                                dataFormat));
                        } else {
                            sheet.addCell(new Label(j + 1, i + 3, "",
                                dataFormat));
                        }
                    }
                    sheet.addCell(new Label(infoBean.getWorkList().size() + 1,
                        i + 3, infoBean.getSum_PCT() == 0.0 ? "" : CommonUtil
                            .formatDoubleToString(infoBean.getSum_PCT(), null),
                        dataFormat));
                    Double pct = 100 - infoBean.getSum_PCT() == 0.0 ? 0.0
                        : 100 - infoBean.getSum_PCT();
                    if (pct > 0) {
                        sheet.addCell(new Label(
                            infoBean.getWorkList().size() + 2, i + 3,
                            CommonUtil.formatDoubleToString(pct, null),
                            colorformat));
                    } else {
                        sheet.addCell(new Label(
                            infoBean.getWorkList().size() + 2, i + 3,
                            pct == 0.0 ? "" : CommonUtil.formatDoubleToString(
                                pct, null), dataFormat));
                    }
                }
                SheetSettings sheetset = sheet.getSettings(); //设置冻结窗口
                sheetset.setHorizontalFreeze(1);
                sheetset.setVerticalFreeze(3);

                workBook.write();
                workBook.close();
                out.close();
                modelMap.put("processType", "reportExcel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "forward:../jsp/query/QueryWorkingHoursPercentage.jsp";
    }

    @RequestMapping("/work/checkWorkHorus.do")
    public void checkWorkHorus(ModelMap modelMap,
            @RequestParam("type") String type,
            @ModelAttribute("user") User user, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            Double work_PCT = null;
            if (type.equals("OFF")) {
                IWorkingHoursService workService = (IWorkingHoursService) SpringUtil
                    .getBean("workingHoursServiceImpl");
                work_PCT = workService.calculateWorkHoursByUserByDate(user,
                    new Date(), null);
            }
            out = response.getWriter();
            if (work_PCT != null && work_PCT > 0.0) {
                out.print(work_PCT);
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
}
