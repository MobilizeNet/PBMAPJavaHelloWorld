package com.sample.datamanagers.sample.sample.d_sample_list;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.mobilize.jwebmap.aop.annotations.WebMAPStateManagement;
import com.mobilize.jwebmap.models.TextModel;
import com.mobilize.jwebmap.models.ColumnModel;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static com.mobilize.jwebmap.datatypes.StringHelper.stringOf;
import static com.mobilize.jwebmap.datatypes.ShortHelper.shortOf;
import static com.mobilize.jwebmap.enums.MaskDataType.StringMask;
import static com.mobilize.jwebmap.enums.Processing.GRID;

@Configurable
@Lazy(
  value = true
)
@Scope(
  value = SCOPE_PROTOTYPE
)
@Component(
  value = "samplesampled_sample_list"
)
@WebMAPStateManagement
public class d_sample_list extends com.mobilize.jwebmap.datamanager.DataManager
{
  public TextModel list_t;
  public TextModel getList_t() {
    return this.list_t;
  }
  public void setList_t(TextModel value) {
    this.list_t = value;
  }
  public ColumnModel list;
  public ColumnModel getList() {
    return this.list;
  }
  public void setList(ColumnModel value) {
    this.list = value;
  }
  public void doInit() {
    init();
  }
  public d_sample_list(){
    super ();
  }
  private void doList_tInit() {
    this.setList_t(new TextModel());
    this.getList_t().setBand(stringOf("header"));
    this.getList_t().setTextAlignment(shortOf(2));
    this.getList_t().setText("List");
    this.getList_t().setBorder(shortOf(0));
    this.getList_t().setColor("33554432");
    this.getList_t().setX(shortOf(3));
    this.getList_t().setY(shortOf(2));
    this.getList_t().setHeight(shortOf(16));
    this.getList_t().setWidth(shortOf(97));
    this.getList_t().getHtml().setValueIsHtml(false);
    this.getList_t().setName(stringOf("list_t"));
    this.getList_t().setVisible(true);
    this.getList_t().getFont().setFace("Tahoma");
    this.getList_t().getFont().setHeight(shortOf(-10));
    this.getList_t().getFont().setWeight(shortOf(700));
    this.getList_t().getFont().setCharset("0");
    this.getHeader().addDataControl(this.getList_t());
  }
  private void doListInit() {
    this.setList(new ColumnModel());
    this.getList().setColType(stringOf("char(10)"));
    this.getList().setBand(stringOf("detail"));
    this.getList().setColumnId(shortOf(1));
    this.getList().setTextAlignment(shortOf(0));
    this.getList().setTabSequence(shortOf(10));
    this.getList().setBorder(shortOf(0));
    this.getList().setColor("33554432");
    this.getList().setX(shortOf(3));
    this.getList().setY(shortOf(2));
    this.getList().setHeight(shortOf(19));
    this.getList().setWidth(shortOf(97));
    this.getList().setFormat("[general]");
    this.getList().setName(stringOf("list"));
    this.getList().setVisible(true);
    this.getList().getEdit().setLimit(shortOf(0));
    this.getList().getEdit().setFontCase("any");
    this.getList().getEdit().setFocusRectangle(false);
    this.getList().getEdit().setAutoSelect(true);
    this.getList().getEdit().setAutohScroll(true);
    this.getList().getFont().setFace("Tahoma");
    this.getList().getFont().setHeight(shortOf(-10));
    this.getList().getFont().setWeight(shortOf(400));
    this.getList().getFont().setFamily("2");
    this.getList().getFont().setPitch("2");
    this.getList().getFont().setCharset("0");
    this.getList().getBackground().setColor("536870912");
    this.getList().setMaskDataType(StringMask);
    this.getDetail().addDataControl(this.getList());
  }
  public void doWMInit() {
    super.doWMInit();
    this.setName("sample/sample/d_sample_list/d_sample_list");
    setUnits(stringOf("0"));
    setTimerInterval(shortOf(0));
    setColor(stringOf("1073741824"));
    setProcessing(GRID);
    setHTMLDW(false);
    getPrintOptions().setPrinterName("");
    getPrintOptions().setDocumentName("");
    getPrintOptions().setOrientation(stringOf("0"));
    getPrintOptions().getMargin().setLeft(shortOf(110));
    getPrintOptions().getMargin().setRight(shortOf(110));
    getPrintOptions().getMargin().setTop(shortOf(96));
    getPrintOptions().getMargin().setBottom(shortOf(96));
    getPrintOptions().getPaper().setSource(shortOf(0));
    getPrintOptions().getPaper().setSize(shortOf(0));
    getPrintOptions().setUseDefaultPrinter(true);
    getPrintOptions().setPrompt(false);
    getPrintOptions().setButtons(false);
    getPrintOptions().getPreview().setButtons(false);
    getPrintOptions().setCliptext(false);
    getPrintOptions().setOverridePrintJob(false);
    getPrintOptions().setCollate(true);
    getPrintOptions().getPreview().setOutline(true);
    setHideGrayLine(false);
    this.getHeader().setHeight(shortOf(20));
    this.getHeader().setColor("536870912");
    this.getSummary().setHeight(shortOf(0));
    this.getSummary().setColor("536870912");
    this.getFooter().setHeight(shortOf(0));
    this.getFooter().setColor("536870912");
    this.getDetail().setHeight(shortOf(23));
    this.getDetail().setColor("536870912");
    addColumn("list", "list", false, false, "char(10)", true);
    this.doList_tInit();
    this.doListInit();
  }
}