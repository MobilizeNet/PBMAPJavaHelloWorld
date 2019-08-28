package com.sample.sample.sample;

import org.springframework.beans.factory.annotation.Configurable;
import com.mobilize.jwebmap.aop.annotations.WebMAPStateManagement;
import com.sample.sample.sample.Iw_sample;
import com.sample.datamanagers.sample.sample.d_sample_list.d_sample_list;
import static com.mobilize.jwebmap.datatypes.ShortHelper.shortOf;
import static com.mobilize.jwebmap.conditionals.ConditionalsHelper.isTrue;
import static com.mobilize.jwebmap.conditionals.ComparisonHelper.notEq;
import static com.mobilize.jwebmap.datatypes.IntegerHelper.integerOf;

@Configurable
@WebMAPStateManagement
public class w_sample extends com.mobilize.jwebmap.models.WindowModelImpl implements Iw_sample
{
  public dw_1 dw_1;
  public dw_1 getDw_1() {
    return this.dw_1;
  }
  public void setDw_1(dw_1 value) {
    this.dw_1 = value;
  }
  public class dw_1 extends com.mobilize.jwebmap.datamanager.DataManagerControl
  {
    public dw_1(){}
    public void doWMInit() {
      super.doWMInit();
      this.setX(shortOf(16));
      this.setY(shortOf(64));
      this.setWidth(shortOf(235));
      this.setHeight(shortOf(105));
      this.setTabOrder(shortOf(20));
      this.setTitle(getLocalizedText("none"));
      this.setDataManager(new d_sample_list());
    }
  }
  public sle_1 sle_1;
  public sle_1 getSle_1() {
    return this.sle_1;
  }
  public void setSle_1(sle_1 value) {
    this.sle_1 = value;
  }
  public class sle_1 extends com.mobilize.jwebmap.models.TextModel
  {
    public sle_1(){}
    public void doWMInit() {
      super.doWMInit();
      this.setX(shortOf(104));
      this.setY(shortOf(20));
      this.setWidth(shortOf(70));
      this.setHeight(shortOf(28));
      this.setTabOrder(shortOf(10));
    }
  }
  public st_1 st_1;
  public st_1 getSt_1() {
    return this.st_1;
  }
  public void setSt_1(st_1 value) {
    this.st_1 = value;
  }
  public class st_1 extends com.mobilize.jwebmap.models.LabelModel
  {
    public st_1(){}
    public void doWMInit() {
      super.doWMInit();
      this.setX(shortOf(21));
      this.setY(shortOf(20));
      this.setWidth(shortOf(70));
      this.setHeight(shortOf(28));
      this.setText(getLocalizedText("Test Dw"));
    }
  }
  public cb_1 cb_1;
  public cb_1 getCb_1() {
    return this.cb_1;
  }
  public void setCb_1(cb_1 value) {
    this.cb_1 = value;
  }
  public class cb_1 extends com.mobilize.jwebmap.models.ButtonModel
  {
    public cb_1(){}
    public Integer clicked() {
      Short li_row = 0;
      if (isTrue(notEq(getSle_1().getText(), ""))){
          li_row = shortOf(getDw_1().insertRow(0));
          getDw_1().setItem(integerOf(li_row), shortOf(1), getSle_1().getText(), "String");
        }
      return 0;
    }
    public void doWMInit() {
      super.doWMInit();
      this.setX(shortOf(182));
      this.setY(shortOf(20));
      this.setWidth(shortOf(70));
      this.setHeight(shortOf(28));
      this.setTabOrder(shortOf(10));
      this.setText(getLocalizedText("Add"));
      this.addToEventList("bnclicked", true);
      this.addToEventMapper("bnclicked", "clicked");
    }
  }
  @Override
  public void doConstructor() {
    this.constructor();
    this.dw_1.doConstructor();
    this.sle_1.doConstructor();
    this.st_1.doConstructor();
    this.cb_1.doConstructor();
  }
  public w_sample(){
    super ();
  }
  public void doWMInit() {
    super.doWMInit();
    this.dw_1 = new dw_1();
    this.dw_1.setName("dw_1");
    this.setSle_1(new sle_1());
    this.sle_1.setName("sle_1");
    this.setSt_1(new st_1());
    this.st_1.setName("st_1");
    this.setCb_1(new cb_1());
    this.cb_1.setName("cb_1");
    this.setTargetName("sample");
    this.setLibName("sample");
    this.setSimpleName("w_sample");
    this.setName("mobsample_sample_w_sample");
    this.setWidth(shortOf(279));
    this.setHeight(shortOf(211));
    this.setTitleBar(true);
    this.setTitle(getLocalizedText("Untitled"));
    this.setControlMenu(true);
    this.setMinBox(true);
    this.setMaxBox(true);
  }
}