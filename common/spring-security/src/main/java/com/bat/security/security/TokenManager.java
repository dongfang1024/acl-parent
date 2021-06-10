package com.bat.security.security;


import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public final class TokenManager {

    private static final Long EXPIRATION_MILLISECONDS = 1000 * 60 * 2L; // 1000 * 60 * 60 * 24L;

    private static final String salt = "844846AE-D149-BDFD-7EDC-B2F8B7B08862";

    public static String createToken(String username) {
        return Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLISECONDS))
                .signWith(SignatureAlgorithm.HS512, salt) //HS512
                .compressWith(CompressionCodecs.GZIP).compact();
    }

    //2 解析
    public static String getUserInfoFromToken(String token) {
        String userinfo = Jwts.parser().setSigningKey(salt).parseClaimsJws(token).getBody().getSubject();
        return userinfo;
    }

    public void invalidateToken(String token){
    }



    public static void main(String[] args) {
//        String name = "guojing";
//        String result = createToken(name);
//        System.out.println(result);
//        //解析token
//        String str = getUserInfoFromToken(result);
        String str = getUserInfoFromToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSSi_Nz8rMS1fSUUqtKFCyMjQzMrK0NDAzs6gFADcqEvQiAAAA.Yl3wFNa9zL_1X9bcmWhFJdBCKlPRpRLJMudhma_Vbr2vOQvBK9Q7gYskkJmx1OTM0jdzufz5jJu9nxPCkq1cFw");
        System.out.println("加密前的name：" + str);
    }

}
