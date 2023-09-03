package com.xuecheng.content.feignclient.Impl;

import com.xuecheng.content.feignclient.MediaServiceClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;

@Component
@Slf4j
public class MediaServiceFallBackFactory implements FallbackFactory<MediaServiceClient> {

    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient() {
            @Override
            public String upload(MultipartFile filedata, String objectName) throws IOException {
                log.debug("调用远程服务失败，异常信息{}",throwable.getMessage().toString());
                return null;
            }
        };
    }
}
