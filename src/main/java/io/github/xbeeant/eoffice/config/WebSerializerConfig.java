package io.github.xbeeant.eoffice.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局WEB序列化配置
 *
 * @author xiaobiao
 * @version 2021/11/3
 */
@Configuration
public class WebSerializerConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 转成字符串类型防止js溢出
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Long.class, ToStringSerializer.class);
        // 加入的locadatetime序列化，也可以不加（但是要用@JSONField(format = "yyyy-MM-dd HH:mm:ss")）格式化
        serializeConfig.put(LocalDateTime.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((LocalDateTime) object));
        });
        serializeConfig.put(LocalDate.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) object));
        });
        serializeConfig.put(LocalTime.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format((LocalTime) object));
        });

        // 配置序列化方式
        fastJsonConfig.setSerializeConfig(serializeConfig);


        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> httpMessageConverter = fastConverter;
        //4、将convert添加到converters中
        // 修改StringHttpMessageConverter默认配置
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);

        return new HttpMessageConverters(httpMessageConverter, converter);
    }
}
