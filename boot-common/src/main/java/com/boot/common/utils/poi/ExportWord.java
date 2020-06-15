package com.boot.common.utils.poi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

/**
 * ExportWord
 *
 * @author EPL
 * @date 2020/3/16 0016
 */
public class ExportWord {
    private XWPFHelperTable xwpfHelperTable;
    private XWPFHelper xwpfHelper;
    public ExportWord() {
        xwpfHelperTable = new XWPFHelperTable();
        xwpfHelper = new XWPFHelper();
    }
    /**
     * 创建好文档的基本 标题，表格  段落等部分
     * @return XWPFDocument
     */
    public XWPFDocument createXWPFDocument(int rows,int cols) {
        XWPFDocument doc = new XWPFDocument();
        createTitleParagraph(doc);
        createTableParagraph(doc, rows, cols);
        return doc;
    }

    /**
     * 创建表格的标题样式
     * @param document 文档
     */
    private void createTitleParagraph(XWPFDocument document) {
        //新建一个标题段落对象（就是一段文字）
        XWPFParagraph titleParagraph = document.createParagraph();
        //样式居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        //创建文本对象
        XWPFRun titleFun = titleParagraph.createRun();
        //设置标题的名字
//        titleFun.setText(titleName);
        //加粗
        titleFun.setBold(true);
        //设置颜色
        titleFun.setColor("000000");
        //字体大小
        titleFun.setFontSize(25);
        //设置字体
//        titleFun.setFontFamily("");
        //换行
        titleFun.addBreak();
    }
    /**
     * 设置表格
     * @param document 文档
     * @param rows 创建表格行数
     * @param cols 创建表格列数
     */
    private void createTableParagraph(XWPFDocument document, int rows, int cols) {
//        xwpfHelperTable.createTable(xdoc, rowSize, cellSize, isSetColWidth, colWidths)
        XWPFTable infoTable = document.createTable(rows, cols);
        xwpfHelperTable.setTableWidthAndHAlign(infoTable, "9072", STJc.CENTER);
        //合并表格
//        xwpfHelperTable.mergeCellsHorizontal(infoTable, 1, 1, 5);
//        xwpfHelperTable.mergeCellsVertically(infoTable, 0, 3, 6);
//        for(int col = 3; col < 7; col++) {
//            xwpfHelperTable.mergeCellsHorizontal(infoTable, col, 1, 5);
//        }
        //设置表格样式
        List<XWPFTableRow> rowList = infoTable.getRows();
        for (XWPFTableRow infoTableRow : rowList) {
            List<XWPFTableCell> cellList = infoTableRow.getTableCells();
            for (XWPFTableCell xwpfTableCell : cellList) {
                XWPFParagraph cellParagraph = xwpfTableCell.getParagraphArray(0);
                //水平居中
                cellParagraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun cellParagraphRun = cellParagraph.createRun();
                //设置字号
                cellParagraphRun.setFontSize(12);
                //偶数行加粗
//                if (i % 2 != 0) {
//                    cellParagraphRun.setBold(true);
//                }
            }
        }
        xwpfHelperTable.setTableHeight(infoTable, 560, STVerticalJc.CENTER);
    }


    /**
     * 往表格中填充数据
     * @param dataList 数据
     * @param document 文档
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    public void exportCheckWord(Map<String, Object> dataList, XWPFDocument document, String savePath) throws IOException {
        XWPFParagraph paragraph = document.getParagraphArray(0);
        XWPFRun titleFun = paragraph.getRuns().get(0);
        titleFun.setText(String.valueOf(dataList.get("TITLE")));
        List<List<Object>> tableData = (List<List<Object>>) dataList.get("TABLEDATA");
        XWPFTable table = document.getTableArray(0);
        fillTableData(table, tableData);
        xwpfHelper.saveDocument(document, savePath);
    }
    /**
     * 往表格中填充数据
     * @param table 表格
     * @param tableData 表格数据
     */
    private void fillTableData(XWPFTable table, List<List<Object>> tableData) {
        List<XWPFTableRow> rowList = table.getRows();
        for(int i = 0; i < tableData.size(); i++) {
            List<Object> list = tableData.get(i);
            List<XWPFTableCell> cellList = rowList.get(i).getTableCells();
            for(int j = 0; j < list.size(); j++) {
                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);
                XWPFRun cellParagraphRun = cellParagraph.getRuns().get(0);
                cellParagraphRun.setText(String.valueOf(list.get(j)));
            }
        }
    }
}