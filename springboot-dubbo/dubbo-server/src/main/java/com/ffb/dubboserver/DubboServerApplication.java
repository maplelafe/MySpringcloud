package com.ffb.dubboserver;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;

/**
 * 1������������
 * 		1��������dubbo-starter
 * 		2��������dubbo����������
 * @author lfy
 *
 * SpringBoot��dubbo���ϵ����ַ�ʽ��
 * 1��������dubbo-starter��@EnableDubbo,��application.properties�������ԣ�ʹ��@Service����¶����ʹ��@Reference�����÷���
 * 2��������dubbo.xml�����ļ�;
 * 		����dubbo-starter��ʹ��@ImportResource����dubbo�������ļ�����
 * 3����ʹ��ע��API�ķ�ʽ����provider.xmlת����MyDubboConfig ��@EnableDubbo
 * 		��ÿһ������ֶ�������������,��dubbo��ɨ�����������
 */
//@EnableDubbo ��������ע���dubbo����
//@ImportResource(locations = "classpath:provider.xml")
@SpringBootApplication
@EnableHystrix ////���������ݴ�
@EnableDubbo(scanBasePackages = "com.ffb.dubboserver")
public class DubboServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboServerApplication.class, args);
    }
}
