package com.project.helloworld.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class CertificationDto {
    private final String PREFIX = "certification:";  // Redis에 저장되는 Key값이 중복되지 않도록 상수 선언
    private final int LIMIT_TIME = 3 * 60;  // redis에서의 유효시간 설정

    private final StringRedisTemplate stringRedisTemplate;

    public void createSmsCertification(String phone, String certificationNumber) { // 사용자가 입력한 휴대폰 번호와 인증번호를 저장하고 TTL을 180초로 설정
        stringRedisTemplate.opsForValue()
                .set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phone) { // Redis에서 휴대폰번호(KEY)에 해당하는 인증번호를 리턴
        return stringRedisTemplate.opsForValue().get(PREFIX + phone);
    }

    public void removeSmsCertification(String phone) { // 인증이 완료되었을 경우 메모리 관리를 위해 Redis에 저장된 인증번호 삭제
        stringRedisTemplate.delete(PREFIX + phone);
    }

    public boolean hasKey(String phone) {  // Redis에 해당 휴대폰번호(KEY)로 저장된 인증번호(VALUE)가 존재하는지 확인
        return stringRedisTemplate.hasKey(PREFIX + phone);
    }
}
