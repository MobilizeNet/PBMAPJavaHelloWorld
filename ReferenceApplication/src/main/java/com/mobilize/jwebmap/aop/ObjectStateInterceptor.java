package com.mobilize.jwebmap.aop;

import com.mobilize.jwebmap.aop.manager.IObjectInitManager;
import com.mobilize.jwebmap.aop.manager.ObjectStateManager;
import com.mobilize.jwebmap.exceptions.WebMapException;
import com.mobilize.jwebmap.mvc.ViewManager;
import com.mobilize.jwebmap.persistence.Persistable;
import com.mobilize.jwebmap.utils.CustomArray;
import org.aspectj.lang.ProceedingJoinPoint;
import com.mobilize.jwebmap.models.BaseModel;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import java.lang.reflect.InvocationTargetException;

@Aspect
@Configurable
public class ObjectStateInterceptor
{
   private static boolean testEnvironment = false;
   @Autowired
   private ViewManager viewManager;
   @Autowired
   private ObjectStateManager objectStateManager;
   @Autowired
   private IObjectInitManager initManager;
   @Pointcut (
   "within(@com.mobilize.jwebmap.aop.annotations.WebMAPStateManagement *)")
   public void webMAPStateWindowManagement() {
   }
   @Pointcut ("call(com.mobilize.jwebmap.models.ApplicationModel +.new (..))")
   public void applicationModelConstructor() {
   }
   @Pointcut ("call(com.mobilize.jwebmap.models.BaseModel+.new(..))")
   public void baseModelConstructor() {
   }
   @Pointcut ("call(com.mobilize.jwebmap.datamanager.IDataManager+.new(..))")
   public void dataManagerConstructor() {
   }
   @Pointcut ("call(* java.lang.Class.newInstance())")
   public void reflectionClassNewInstance() {
   }
   @Pointcut (
   "call(* java.lang.reflect.Constructor.newInstance(java.lang.Object[]))")
   public void reflectionConstructorInstance() {
   }
   @Pointcut ("execution(void com.mobilize..set*(..))")
   public void interceptSetter() {
   }
   @Pointcut ("execution(void com.helloworld..set*(..))")
   public void projectInterceptSetter() {
   }
   @Pointcut ("webMAPStateWindowManagement() "
   + "&& (interceptSetter() || projectInterceptSetter()) " +
   "&& !@annotation(com.mobilize.jwebmap.aop.annotations.WebMAPIgnoreStateManagement)"
   )
   public void annotationInterceptor() {
   }
   @Pointcut (
   "applicationModelConstructor() || baseModelConstructor() || dataManagerConstructor() "
   + "|| reflectionClassNewInstance() || reflectionConstructorInstance()")
   public void newObjectInterceptor() {
   }
   @Around ("newObjectInterceptor()")
   public Object aroundObjectInstantiation(ProceedingJoinPoint jp) {
      Object result;
      try{
         result = jp.proceed(jp.getArgs());
      }
      catch (Throwable throwable){
         throw new WebMapException
         ("There was an error while intercepting new operator", throwable);
      }
      {
         result = this.initManager.processInit(result, jp.getThis());
      }
      return result;
   }
   @Around ("annotationInterceptor()")
   public void aroundStateObjectSetter(ProceedingJoinPoint jp) {
      Object [] args = jp.getArgs();
      if (!testEnvironment && isInterceptionEnabled() && args
      .length == 1 && !jp.getSignature().getName().equals("set")){
            Object value = args[0];
            try{
               args[0] = objectStateManager.processDirtyObject
               (jp.getSignature().getName(), value, jp.getThis());
            }
            catch (NoSuchMethodException |
            IllegalAccessException | InvocationTargetException e){
               throw new WebMapException
               ("there was an error while intercepting property setter", e);
            }
         }
      try{
         jp.proceed(args);
      }
      catch (Throwable throwable){
         throw new WebMapException
         ("theere was an error while intercepting property setter", throwable);
      }
   }
   @Pointcut (
   "execution(* com.mobilize.jwebmap.utils.DynamicCustomArray.set(..))")
   public void customArraySetterInterceptor() {
   }
   @Around ("customArraySetterInterceptor()")
   public void aroundCustomArrayStateObjectSetter(ProceedingJoinPoint jp) {
      Object [] args = jp.getArgs();
      boolean propagate = false;
      if (!testEnvironment){
            if (args[0] instanceof Persistable && isInterceptionEnabled()){
                  CustomArray<?> array = (CustomArray<?>) jp.getTarget();
                  Persistable oldValue = null;
                  if (args.length > 1 && args[1] instanceof Persistable){
                        oldValue = (Persistable) array.get((int) args[1]);
                     }
                  args[0] = objectStateManager.
                  processCollectionEntry(array, (Persistable) args
                  [0], oldValue);
               }
            else
               propagate = true;
         }
      try{
         jp.proceed(args);
         if (isInterceptionEnabled() && propagate &&
         args[0] instanceof BaseModel && args[1] instanceof
         int [] && ((int []) args[1]).length > 1){
               objectStateManager.propagateToListElementIfRequired(true
               , jp.getTarget(), args[0], ((int []) args[1])[0]);
            }
      }
      catch (Throwable throwable){
         throw new WebMapException
         ("There was an error while interception custom array setter",
         throwable);
      }
   }
   public static void enableTestEnvironment() {
      ObjectStateInterceptor.testEnvironment = true;
   }
   private boolean isInterceptionEnabled() {
      return viewManager != null && objectStateManager !=
      null && !viewManager.isDeserializing(
      ) && objectStateManager.isStateManagementTrackingEnabled();
   }
}