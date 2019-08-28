package com.sample.sample;

import com.mobilize.jwebmap.nameManager.ILibraryNameManagerProvider;
import java.util.Map;
import java.util.Hashtable;
import org.springframework.stereotype.Component;

@Component
public class SampleLibraryNameManagerProvider implements ILibraryNameManagerProvider
{
  @Override
  public String getName() {
    return "sample";
  }
  @Override
  public Map<String, String> getDataManagersByName() {
    Hashtable<String, String> sampleTable = new Hashtable<String, String>();
    sampleTable.put("d_sample_list", "sample");
    return sampleTable;
  }
  @Override
  public Map<String, Class<?>> getGlobalFunctions() {
    Hashtable<String, Class<?>> sampleGlobalFunctions = new Hashtable<String, Class<?>>();
    return sampleGlobalFunctions;
  }
  @Override
  public boolean isDefault() {
    return true;
  }
}