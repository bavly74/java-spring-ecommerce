package com.example.ecommerce.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY="3fe1dda78187f27ea5c8fe67cce1b943a468223ac27e3261c69f42feb509408f" ;

    public String extractUserName(String token){
        return  extractClaim(token,Claims::getSubject) ;
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                ;
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        var userName = userDetails.getUsername() ;
        return userName.equals(extractUserName(token)) && ! isTokenExpired(token) ;
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date()) ;
    }

    private Date getExpirationDate(String token) {
        return extractClaim(token , Claims::getExpiration) ;
    }

    public String generateToken(
            UserDetails userDetails
    ){
        return generateToken(new HashMap<>() , userDetails );
    }

    public String generateToken(
            Map<String,Object> extraClaims ,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey())
                .compact();
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims) ;
    }

    private SecretKey getSignKey(){
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
