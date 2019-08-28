package com.sample.sample.sample;

import com.mobilize.jwebmap.models.ApplicationModel;
import java.util.HashMap;

public interface Isample
{
  ApplicationModel getApplication();
  Integer open(String commandline);
  void doWMInit();
  HashMap<String, String> getWindowClassNames();
}