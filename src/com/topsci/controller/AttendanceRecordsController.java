package com.topsci.controller;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.topsci.bean.AttendanceRecords;
import com.topsci.bean.User;
import com.topsci.infoBean.AttendanceRecordsInfoBean;
import com.topsci.infoBean.AttendanceRecordsStatisInfoBean;
import com.topsci.service.IAttendanceRecordsService;
import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.IP_Filter;
import com.topsci.utils.OA_Config;
import com.topsci.utils.Pager;
import com.topsci.utils.StringTools;

/**
 * @date 2013-6-26 下午01:55:36
 * @author FengQing
 * @description 考勤Controller
 */

@Controller
@RequestMapping("/ar/")
@SessionAttributes(value = { "user" })
public class AttendanceRecordsController {

    @Autowired
    private IAttendanceRecordsService arService;

    @RequestMapping("initAR.do")
    public String initAR(ModelMap modelMap, @ModelAttribute("user") User user,
            @ModelAttribute("isRemote") String isRemote) {

        List<AttendanceRecords> list = new ArrayList<AttendanceRecords>();
        if (user != null) {
            list = (List<AttendanceRecords>) this.arService.findRecords(user,
                null, null, null).getList();
        }
        if (list.size() == 0) {
            list.add(new AttendanceRecords(UUID.randomUUID().toString(),
                DateTimeUtils.validateDate(DateTimeUtils
                    .validateDate(new Date()))));
        }
        modelMap.put("AttendanceRecordsList", list);
        return "forward:../jsp/AttendanceRecords.jsp";
    }

    @RequestMapping("Register.do")
    public String registered(ModelMap modelMap,
            @RequestParam("type") String type,
            @ModelAttribute("user") User user, HttpServletRequest request,
            HttpServletResponse response) {
        if (user != null) {
            List<AttendanceRecords> list = new ArrayList<AttendanceRecords>();
            try {
                list.add(this.arService.registered(user, type,
                    request.getRemoteAddr()));
            } catch (Exception e) {
                e.printStackTrace();
                modelMap.put("processStatus", e.getMessage());
            }
            modelMap.put("AttendanceRecordsList", list);
        }
        return "forward:../jsp/AttendanceRecords.jsp";
    }

    @RequestMapping("QueryAR.do")
    public String queryRecords(ModelMap modelMap,
            @ModelAttribute("user") User user, @RequestParam(
                    value = "starDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        Pager pager = new Pager();
        if (pageNum != null) {
            pager.setPageNumber(pageNum);
        } else {
            pager.setPageNumber(1);
        }
        pager = this.arService.findRecords(user, startDate, endDate, pager);
        modelMap.put("pager", pager);
        modelMap.put("starDate", startDate);
        modelMap.put("endDate", endDate);
        return "forward:../jsp/query/QueryAttendanceRecords.jsp";
    }

    @RequestMapping(value = "addRemark.do", method = RequestMethod.POST)
    public void addRemark(@RequestParam("ar_id") String ar_id,
            @RequestParam("remark") String remark,
            @RequestParam("type") String type,
            @ModelAttribute("user") User user, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            AttendanceRecords ar = this.arService.get(ar_id);
            if (ar == null) {
                ar = new AttendanceRecords(UUID.randomUUID().toString(),
                    DateTimeUtils.validateDate(DateTimeUtils
                        .validateDate(new Date())));
                ar.setUser(user);
                this.arService.saveOrUpdate(ar);
            }
            if (ar != null) {
                if (type.equals("ON")) {
                    ar.setOn_description(remark);
                } else {
                    ar.setOff_description(remark);
                }
                if (this.arService.update(ar)) {
                    out = response.getWriter();
                    out.print(remark);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    @RequestMapping(value = "mandatoryUpdate.do")
    public void mandatoryUpdate(@RequestParam("userName") String userName,
            @RequestParam("userPwd") String userPwd,
            @RequestParam("type") String type,
            @RequestParam("date") String date,
            @RequestParam("dateTime") String dateTime,
            HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String status = this.arService.mandatoryUpdate(userName, userPwd,
                type, date, dateTime);
            out.print(status);
        } catch (Exception e) {
            if (out != null) {

                out.print("修改错误!");
            }
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    @RequestMapping("IP_Filter.do")
    public void IP_Filter(HttpServletRequest request,
            HttpServletResponse response) {
        PrintWriter out = null;
        String status = null;
        try {
            status = new IP_Filter(OA_Config.ALLOW_IP_ADDRESS).filter(request
                .getRemoteAddr());
            if (status != null) {
                out = response.getWriter();
                out.print(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//			out.flush();
//			out.close();
        }
    }

    @RequestMapping("QueryARStatis.do")
    public String QueryARStatis(
            ModelMap modelMap,
            @RequestParam(value = "query_userName", required = false) String query_userName,
            @RequestParam(value = "starDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "processType", required = false) String processType,
            HttpServletResponse response) {
        try {
            Pager pager = new Pager();
            pager.setPageSize(100);
            if (pageNum != null) {
                pager.setPageNumber(pageNum);
            } else {
                pager.setPageNumber(1);
            }
            AttendanceRecordsStatisInfoBean statisInfoBean = this.arService
                .queryARStatis(query_userName, startDate, endDate, pager);
            modelMap.put("statisInfoBean", statisInfoBean);
            modelMap.put("query_userName", query_userName);
            modelMap.put("starDate", startDate);
            modelMap.put("endDate", endDate);
            modelMap.put("pager", statisInfoBean.getPager());
            //导出报表
            if (processType != null && processType.equals("reportExcel")) {
                OutputStream out = null;
                String title = "工时统计";
                if (StringTools.replaceStringtoNull(startDate) != null
                    && StringTools.replaceStringtoNull(endDate) != null) {
                    title = startDate + "~~" + endDate + "  工时统计";
                }

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

                List<Date> columnList = statisInfoBean.getColumnList();
                List<AttendanceRecordsInfoBean> valueList = statisInfoBean
                    .getValueList();

                WritableWorkbook workBook = Workbook.createWorkbook(out);
                WritableSheet sheet = workBook.createSheet(title, 0);

                //主标题样式
                WritableCellFormat format = new WritableCellFormat(
                    new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
                format.setAlignment(jxl.format.Alignment.CENTRE);
                format
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                sheet.addCell(new Label(0, 0, title, format));
                sheet.mergeCells(0, 0, 20, 0);
                //列标题样式
                WritableCellFormat columnformat = new WritableCellFormat(
                    new WritableFont(WritableFont.TIMES,
                        WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD));
                columnformat.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
                columnformat.setAlignment(jxl.format.Alignment.CENTRE);
                columnformat
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                columnformat.setWrap(true);

                sheet.setRowView(0, 800);
                sheet.setRowView(1, 650);
                sheet.mergeCells(0, 1, 0, 2);
                sheet.addCell(new Label(0, 1, "姓名", columnformat));
                ////////////////
                List<Date> tempColumnList = new ArrayList<Date>(
                    columnList.size() * 2);
                for (Date date : columnList) {
                    tempColumnList.add(date);
                    tempColumnList.add(null);
                }
                for (int i = 0; i < tempColumnList.size(); i++) {
                    Date date = tempColumnList.get(i);
                    if (date != null) {
                        sheet.mergeCells(i + 1, 1, i + 2, 1);
                        sheet
                            .addCell(new Label(i + 1, 1, DateTimeUtils
                                .validateDate(date)
                                + "                "
                                + DateTimeUtils.EFormat.format(date),
                                columnformat));
                    }
                }
                for (int i = 0; i < tempColumnList.size(); i++) {
                    if (i % 2 == 0) {
                        sheet.addCell(new Label(i + 1, 2, "上", columnformat));
                    } else {
                        sheet.addCell(new Label(i + 1, 2, "下", columnformat));
                    }
                }
                //明细列样式
                format = new WritableCellFormat();
                format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
                format.setAlignment(jxl.format.Alignment.CENTRE);
                format
                    .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

                for (int i = 0; i < valueList.size(); i++) {
                    sheet.setRowView(i + 3, 400);
                    AttendanceRecordsInfoBean infoBean = valueList.get(i);
                    sheet.addCell(new Label(0, i + 3, infoBean.getUser()
                        .getUser_name(), columnformat));
                    List<Object[]> tempArList = new ArrayList<Object[]>(
                        infoBean.getArList().size() * 2);
                    for (AttendanceRecords ar : infoBean.getArList()) {
                        if (ar != null) {
                            tempArList.add(new Object[] { ar.getOn_Date(),
                                ar.getOn_description() });
                            tempArList.add(new Object[] { ar.getOff_Date(),
                                ar.getOff_description() });
                        } else {
                            tempArList.add(new Object[] { null, null });
                            tempArList.add(new Object[] { null, null });
                        }
                    }

                    for (int j = 0; j < tempArList.size(); j++) {
                        Object[] obj = tempArList.get(j);
                        if (obj[0] != null) {
                            Label lable = new Label(j + 1, i + 3,
                                DateTimeUtils.HHmmssFormat.format(obj[0]),
                                format);
                            if (obj[1] != null) {
                                WritableCellFeatures cellFeatures = new WritableCellFeatures();
                                cellFeatures.setComment((String) obj[1]);
                                lable.setCellFeatures(cellFeatures);
                            }
                            sheet.addCell(lable);
                        } else {
                            sheet.addCell(new Label(j + 1, i + 3, "×", format));
                        }
                    }
                }
                SheetSettings sheetset = sheet.getSettings(); //设置冻结窗口
                sheetset.setHorizontalFreeze(1);
                sheetset.setVerticalFreeze(3);

                workBook.write();
                workBook.close();
                out.close();
                modelMap.put("processType", "reportExcel");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forward:../jsp/query/QueryAttendanceRecordsStatis.jsp";
    }
}
