package com.sx.context;

import com.sx.anno.Autowired;
import com.sx.anno.Component;
import com.sx.anno.Repository;
import com.sx.anno.Service;
import com.sx.processor.AutowiredBeanPostProcessor;
import com.sx.processor.BeanPostProcessor;
import com.sx.processor.DIBeanPostProcessor;
import com.sx.processor.TransactionalBeanPostProcessor;
import com.sx.utils.HQScanPackage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shengx
 * @date 2020/2/28 11:09
 */
public class ApplicationContext implements BeanFactory {
    private Map<String, Object> singletonBeans = new ConcurrentHashMap<>();
    private ArrayList<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    public ApplicationContext() {
        refresh("applicationContext.xml");
    }

    public ApplicationContext(String path) {
        refresh(path);
    }

    private void refresh(String path) {
        // 从xml配置或注解封装beanDefinition
        loadBeanDefinition(path);
        // 添加后置处理器
        registerBeanPostProcessor();
        // 根据beanDefinition 创建 bean
        createBean();
        // 后置处理器beanPostProcessor,其中AutowiredBeanPostProcessor做依赖注入
        finishBeanInit();
    }

    private void createBean() {
        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()){
            String id = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            try {
                Class clazz = Class.forName(beanDefinition.getBeanClass());
                Object obj = clazz.newInstance();
                singletonBeans.put(id, obj);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBeanDefinition(String path) {
        InputStream in = ApplicationContext.class.getClassLoader().getResourceAsStream(path);
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(in);
            Element element = document.getRootElement();
            List<Element> beanList = element.selectNodes("//bean");
            loadBeanDefinitionFromXML(beanList);
            List<Element> annoElem = element.selectNodes("//component-scan");
            loadBeanDefinitionFromAnno(annoElem);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void loadBeanDefinitionFromAnno(List<Element> annoElem) {
        if (annoElem.size() > 0) {
            Element elem = annoElem.get(0);
            String basePackage = elem.attributeValue("base-package");
            loadBeanDefinitionFromPackage(basePackage);
        }
    }

    private void loadBeanDefinitionFromXML(List<Element> beanList) {
        for (Element elem : beanList) {
            BeanDefinition beanDefinition = new BeanDefinition();
            String id = elem.attributeValue("id");
            String className = elem.attributeValue("class");
            beanDefinition.setId(id);
            beanDefinition.setBeanClass(className);
            List<Element> propertyElems = elem.selectNodes("property");
            Set<PropertyValue> propertyValues = new HashSet<>();
            for (Element propertyElem : propertyElems) {
                PropertyValue propertyValue = new PropertyValue();
                String propertyName = propertyElem.attributeValue("name");
                String ref = propertyElem.attributeValue("ref");
                propertyValue.setName(propertyName);
                propertyValue.setRef(ref);
                propertyValues.add(propertyValue);
            }
            beanDefinition.setPropertyValues(propertyValues);
            beanDefinitionMap.put(id, beanDefinition);
        }
    }

    //注册 内部的 beanPostProcessor
    private void registerBeanPostProcessor() {
        AutowiredBeanPostProcessor autowiredBeanPostProcessor = new AutowiredBeanPostProcessor();
        autowiredBeanPostProcessor.setBeanFactory(this);
        DIBeanPostProcessor diBeanPostProcessor = new DIBeanPostProcessor();
        diBeanPostProcessor.setBeanFactory(this);
        TransactionalBeanPostProcessor transactionalBeanPostProcessor = new TransactionalBeanPostProcessor();
//        beanPostProcessors.add(autowiredBeanPostProcessor);
        beanPostProcessors.add(diBeanPostProcessor);
        beanPostProcessors.add(transactionalBeanPostProcessor);
    }

    // 后置处理器beanPostProcessor调用
    private void finishBeanInit() {
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            for(Map.Entry<String, Object> entry : singletonBeans.entrySet()){
                String name = entry.getKey();
                Object instance = entry.getValue();
                beanPostProcessor.postProcessBeforeInitialization(instance, name);
                initBean(instance, name);
                Object bean = beanPostProcessor.postProcessAfterInitialization(instance, name);
                if(bean != null){
                    singletonBeans.put(name, bean);
                }
            }
        }
    }
    // 对实例化后的bean调用注册的init方法
    private void initBean(Object instance, String name) {
    }
    // 扫包，处理Component Repository Service注解
    private void loadBeanDefinitionFromPackage(String packageName) {
        // 扫包
        HQScanPackage hqScanPackage = new HQScanPackage();
        hqScanPackage.addPackage(packageName);
        hqScanPackage.setFilter(new HQScanPackage.HQScanPackageFilter() {
            @Override
            public boolean accept(Class<?> clazz) {
                return true;
            }
        });
        hqScanPackage.setListener(new HQScanPackage.HQScanPackageListener() {
            @Override
            public void onScanClass(Class<?> clazz) {
                Annotation[] annotations = clazz.getAnnotations();
                if (annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        String value = "";
                        if (annotation instanceof Repository) {
                            value = ((Repository) annotation).value();
                        } else if (annotation instanceof Service) {
                            value = ((Service) annotation).value();
                        } else if (annotation instanceof Component) {
                            value = ((Component) annotation).value();
                        } else {
                            return;
                        }
                        if (value.equals("")) {
                            value = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                        }
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setId(value);
                        beanDefinition.setBeanClass(clazz.getName());
                        Field[] fields = clazz.getDeclaredFields();
                        Set<PropertyValue> propertyValues = new HashSet<>();
                        for(Field field : fields){
                            Annotation autowiredAnno = field.getAnnotation(Autowired.class);
                            if(autowiredAnno != null){
                                PropertyValue propertyValue = new PropertyValue();
                                String name = field.getName();
                                propertyValue.setName(name);
                                propertyValue.setClazz(field.getType());
                                propertyValue.setAutowired(true);
                                propertyValues.add(propertyValue);
                            }
                        }
                        beanDefinition.setPropertyValues(propertyValues);
                        beanDefinitionMap.put(value, beanDefinition);
                    }
                }

            }
        });
        hqScanPackage.scan();
    }
    public Object getBean(String id) {
        return singletonBeans.get(id);
    }

    public Map<String, Object> getSingletonBeans() {
        return singletonBeans;
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

}
