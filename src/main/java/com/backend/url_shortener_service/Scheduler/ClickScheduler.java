package com.backend.url_shortener_service.Scheduler;

import com.backend.url_shortener_service.Entity.UrlEntity;
import com.backend.url_shortener_service.Exception.UrlNotFound;
import com.backend.url_shortener_service.Repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class ClickScheduler {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 60000)
    public void clickScheduler(){

        Set<String> keys = redisTemplate.keys("clicks:*");

        if(keys.isEmpty()){
            return;
        }


        for(String key: keys){

            String value = redisTemplate.opsForValue().get(key);

            if(value==null){
                continue;
            }

            String code = key.replace("clicks:","");
            long count = Long.parseLong(value);

            UrlEntity urlEntity = urlRepository.findByShortCode(code).orElseThrow(()->new UrlNotFound("Url not found"));
            urlEntity.setClickCount(urlEntity.getClickCount()+count);

            urlRepository.save(urlEntity);

            redisTemplate.delete(key);

        }


    }

    @Scheduled(fixedRate = 60000)
    public void timeScheduler(){

        Set<String> time = redisTemplate.keys("last:*");

        if(time.isEmpty()){
            return;
        }

        for(String key: time){

            String value = redisTemplate.opsForValue().get(key);

            if(value==null){
                continue;
            }

            String code = key.replace("last:","");
            LocalDateTime time1 = LocalDateTime.parse(value);

            UrlEntity urlEntity = urlRepository.findByShortCode(code).orElseThrow(()->new UrlNotFound("Url not found"));
            urlEntity.setLastUsed(time1);

            urlRepository.save(urlEntity);

            redisTemplate.delete(key);

        }
    }
}
