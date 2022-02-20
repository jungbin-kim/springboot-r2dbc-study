package kim.jungbin.springboot.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SpringUtil {
    private static SpringUtil self;

    private AutowireCapableBeanFactory factory;
    @Autowired
    private ApplicationContext context;


    public static void injectDependency(Object bean) {
        self.factory.autowireBean(bean);
        self.factory.initializeBean(bean, "");
    }


    public static <T> T getBean(Class<T> clazz) {
        return self.context.getBean(clazz);
    }


    public static <T> T getBean(String name, Class<T> requiredType) {
        return self.context.getBean(name, requiredType);
    }


    @PostConstruct
    private void init() {
        self = this;

        factory = context.getAutowireCapableBeanFactory();
    }
}
