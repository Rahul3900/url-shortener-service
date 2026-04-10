package com.backend.url_shortener_service.Service.ServiceImpl;

import com.backend.url_shortener_service.DTO.AnalyticsDTO;
import com.backend.url_shortener_service.DTO.UrlResponseDTO;
import com.backend.url_shortener_service.DTO.UrlUserDTO;
import com.backend.url_shortener_service.Entity.UrlEntity;
import com.backend.url_shortener_service.Exception.CustomAliasFound;
import com.backend.url_shortener_service.Exception.LimitExceedException;
import com.backend.url_shortener_service.Exception.UrlNotFound;
import com.backend.url_shortener_service.Mapper.UrlMapper;
import com.backend.url_shortener_service.Repository.UrlRepository;
import com.backend.url_shortener_service.Service.UrlService;
import com.backend.url_shortener_service.Util.Base32Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ServiceImplementation implements UrlService {

    private static final Logger log = LoggerFactory.getLogger(ServiceImplementation.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

   @Autowired
   private UrlRepository urlRepository;

   @Autowired
    private UrlMapper urlMapper;

   @Autowired
   private Base32Encoder encoder;

   private static int codeLen = 6;

    @Override
    public UrlResponseDTO saveUrl(UrlUserDTO urlUserDTO) {

        log.info("Creating short code for url: {}",urlUserDTO.getLongUrl());

        UrlEntity urlEntity = urlMapper.mapToUrlEntity(urlUserDTO);

        String code;

        if (urlUserDTO.getCustomAlias() != null && !urlUserDTO.getCustomAlias().isEmpty()) {
            if(urlRepository.existsByShortCode(urlUserDTO.getCustomAlias())) {
                log.error("Custom Alias already exists: {}", urlUserDTO.getCustomAlias());
                throw new CustomAliasFound("Alias already exists!!!");
            }
            code = urlUserDTO.getCustomAlias();
            log.debug("Custom Alias used for Short code: {}",code);
            urlEntity.setCustomAlias("True");
        } else {
            code = encoder.getShortCode(codeLen);
            log.debug("Short code created for url: {}",urlUserDTO.getLongUrl());
        }

        urlEntity.setShortCode(code);
        urlEntity.setCreatedAt(LocalDateTime.now());
        urlEntity.setExpiredAt(LocalDateTime.now().plusDays(7));
        
        UrlEntity saved = urlRepository.save(urlEntity);
        log.info("Mapping saved to database");

        return urlMapper.mapToUrlDTO(saved);
    }

    @Override
    public String getUrlFromCode(String code) {

        log.info("Fetching url from the code: {}",code);
        String longUrl = redisTemplate.opsForValue().get(code);



        if(longUrl!=null){
            log.info("Redis hit for code: {}",code);
            redisTemplate.opsForValue().increment("clicks:"+code);
            redisTemplate.opsForValue().set("last:"+code,String.valueOf(LocalDateTime.now()));
            return longUrl;
        }

        log.info("Redis miss for code: {}",code);

        UrlEntity urlEntity = urlRepository.findByShortCode(code)
                .orElseThrow(()->{
                    log.error("Url not found for code: {}",code);
                    return new UrlNotFound("Url Not Found");
                });

        redisTemplate.opsForValue().set(code,urlEntity.getLongUrl(),10, TimeUnit.MINUTES);

        if(urlEntity.getExpiredAt()!=null&&
            urlEntity.getExpiredAt().isBefore(LocalDateTime.now())){
            log.warn("code expired: {}",code);
            return "Expired";
        }

        urlEntity.setClickCount(urlEntity.getClickCount()+1);
        urlEntity.setLastUsed(LocalDateTime.now());
        urlRepository.save(urlEntity);
        log.info("Redirecting to url {} -> {}",code,urlEntity.getLongUrl());
        return urlEntity.getLongUrl();

    }

    @Override
    public void checkCount(String ip) {

        String key = "redis-key:"+ip;
        String value = redisTemplate.opsForValue().get(key);

        int count = (value==null)?0:Integer.parseInt(value);
        if(count<5){
            count+=1;
             redisTemplate.opsForValue().set(key,String.valueOf(count),1,TimeUnit.MINUTES);
         }else{
             log.error("Rate limit exceed for IP: {}",ip);
             throw new LimitExceedException("Too many attempts, please try again after 1 minute!!");
         }
    }

    @Override
    public AnalyticsDTO getAnalytics(String code) {

        log.info("Fetching Analytics for code: {}",code);

        UrlEntity urlEntity = urlRepository.findByShortCode(code)
                .orElseThrow(()->{
                    log.error("Url not found for code: {}",code);
                    return new UrlNotFound("Url Not Found");
                });

        AnalyticsDTO analyticsDTO = urlMapper.mapToAnalytics(urlEntity);

        String value = redisTemplate.opsForValue().get("clicks:"+code);
        Long count = value==null?0L:Long.parseLong(value);
        analyticsDTO.setCountClick(count+urlEntity.getClickCount());

        String time = redisTemplate.opsForValue().get("last:"+code);
        LocalDateTime time1 = time==null?null:LocalDateTime.parse(time);
        analyticsDTO.setLastUsed(time1);

        log.info("Analytics Report fetched successfully");
        return analyticsDTO;
    }

    @Override
    public String getUrlInfoFromCode(String code) {

        log.info("Fetching url from the code: {}",code);
        UrlEntity urlEntity = urlRepository.findByShortCode(code)
                .orElseThrow(()->{
                    log.error("Url not found for code: {}",code);
                    return new UrlNotFound("Url Not Found");
                });

        return urlEntity.getLongUrl();
    }
}
