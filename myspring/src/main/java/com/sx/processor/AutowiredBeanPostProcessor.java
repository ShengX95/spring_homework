package com.sx.processor;

import com.sx.anno.Autowired;
import com.sx.context.ApplicationContext;
import com.sx.context.BeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shengx
 * @date 2020/3/3 12:19
 */
public class AutowiredBeanPostProcessor implements BeanPostProcessor {
    BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        ApplicationContext applicationContext = (ApplicationContext) beanFactory;
        Map<String, Object> singletonBeans = applicationContext.getSingletonBeans();
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            Annotation annotation = field.getAnnotation(Autowired.class);
            if(annotation != null){
                field.setAccessible(true);
                Class diClass = field.getType();
                for (HashMap.Entry<String, Object> entry2 : singletonBeans.entrySet()) {
                    String key2 = entry2.getKey();
                    Object obj2 = entry2.getValue();
                    if (diClass.isInstance(obj2)) {
                        field.setAccessible(true);
                        try {
                            field.set(bean, obj2);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return bean;
    }
}
