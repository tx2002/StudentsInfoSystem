package com.example.studentsinfosystem.test;

import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author TX
 * @date 2022/10/8 9:18
 */
public class JwtTokenTest {

    public static void main(String[] args) {
        JwtToken jwtToken = new JwtToken();
        Account account = new Account(1, "B20030530", "123456", 1);
        String token = jwtToken.jwt(account);
        System.out.println(token);

        Claims claims = jwtToken.getClaimByToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IkIyMDAzMDUzMCIsInJvbGUiOjEsImV4cCI6MTY2NTE5OTQ4NywiaWF0IjoxNjY1MTkyMjg3fQ.g8hcSXCYd0cV2zcbi8CK8Lq8IQvgW8OmBnwdHI0YRw4");
        System.out.println(claims.get("role"));
    }
}
