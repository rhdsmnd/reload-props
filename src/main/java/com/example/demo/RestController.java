package com.example.demo;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Value("${sample.prop}")
    private String sampleProp;

    private ApplicationContext context;
    private AbstractEnvironment env;
    private String appPropertySourceName = null;

    @Autowired
    public RestController(ApplicationContext context) {
        this.context = context;
        this.env = (AbstractEnvironment) context.getEnvironment();

        Iterator<PropertySource<?>> propSources = this.env.getPropertySources().iterator();
        while (propSources.hasNext()) {
            PropertySource<?> propSource = propSources.next();
            if (propSource.getName().contains("application.properties")) {
                this.appPropertySourceName = propSource.getName();
            }
        }
    }

    @RequestMapping("/")
    public String home() {
        System.out.println(this.env.getProperty("sample.prop"));
        if (this.appPropertySourceName != null) {
            Properties updatedAppProps = new Properties();
            try {
                updatedAppProps.load(this.context.getResource("classpath:/application.properties").getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return sampleProp;
            }
            this.env.getPropertySources().replace(this.appPropertySourceName, new PropertiesPropertySource(this.appPropertySourceName, updatedAppProps));
            context.getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        }
        System.out.println(this.env.getProperty("sample.prop"));
        return sampleProp;
    }

}
