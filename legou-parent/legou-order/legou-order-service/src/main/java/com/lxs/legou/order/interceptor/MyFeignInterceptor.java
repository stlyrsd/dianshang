package com.lxs.legou.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class MyFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获得当前请求头，传递给商品微服务,获取的是当前线程的request信息，这时如果使用线程隔离，需要采用信号量隔离方案否则报错
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            //获取请求头，向下传
            HttpServletRequest request = requestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement(); //头名
                String value =request.getHeader(name); //头值
                System.out.println(name + " :::: " + value);
                //把请求头传递给feign
                requestTemplate.header(name, value);
            }
        }

    }
}
