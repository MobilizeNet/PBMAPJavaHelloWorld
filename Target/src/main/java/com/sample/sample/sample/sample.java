package com.sample.sample.sample;

import org.springframework.beans.factory.annotation.Configurable;
import com.mobilize.jwebmap.aop.annotations.WebMAPStateManagement;
import com.sample.sample.sample.Isample;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobilize.jwebmap.models.ApplicationModel;
import com.mobilize.jwebmap.utils.WebMapAtomicReference;
import com.mobilize.jwebmap.models.WindowModel;
import com.sample.sample.sample.w_sample;
import java.util.HashMap;

@Configurable
@WebMAPStateManagement
public class sample extends com.mobilize.jwebmap.models.ApplicationModelImpl implements Isample
{
  @JsonIgnore
  public ApplicationModel getApplication() {
    return getViewManager().getCurrentApplication();
  }
  public Integer open(String commandline) {
    getViewManager().showWindowWithParent(new WebMapAtomicReference<WindowModel>(getApplication().getDefautlInstance(w_sample.class), x -> getApplication().setDefaultInstance(w_sample.class, x)), w_sample.class);
    return 0;
  }
  public sample(){
    super ();
  }
  public void doWMInit() {
    super.doWMInit();
    this.setTargetName("sample");
    this.setLibName("sample");
    this.setSimpleName("sample");
    this.setName("sample");
  }
  @Override
  public HashMap<String, String> getWindowClassNames() {
    HashMap<String, String> windows = new HashMap<String, String>();
    windows.put("w_sample", "com.sample.sample.sample.w_sample");
    return windows;
  }
}