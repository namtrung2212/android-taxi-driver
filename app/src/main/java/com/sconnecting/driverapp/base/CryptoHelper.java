package com.sconnecting.driverapp.base;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;

import java.util.HashMap;
/**
 * Created by TrungDao on 8/23/16.
 */

public class CryptoHelper {

    public static  String HashSecretKey = "LoveOfMyLife";
    public static  String  HashIssuer = "namtrung2212@gmail.com";


    public static String generateHashKey (String valueToHash) {



        final long iat = System.currentTimeMillis() / 1000l; // issued at claim
        final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds

        final JWTSigner signer = new JWTSigner(HashSecretKey);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", HashIssuer);
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("hashedvalue", valueToHash);

        JWTSigner.Options option = new JWTSigner.Options();
        option.setAlgorithm(Algorithm.HS256);
        option.setExpirySeconds(60);

        return  signer.sign(claims);

    }



}
