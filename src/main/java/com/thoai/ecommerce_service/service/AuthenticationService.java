package com.thoai.ecommerce_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.thoai.ecommerce_service.dto.request.AuthenticationRequest;
import com.thoai.ecommerce_service.dto.request.IntrospectRequest;
import com.thoai.ecommerce_service.dto.response.AuthenticationResponse;
import com.thoai.ecommerce_service.dto.response.IntrospectResponse;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    //
    // Phương thức này sẽ nhận vào một đối tượng AuthenticationRequest và trả về true nếu thông tin xác thực hợp lệ, ngược lại trả về false.
//    public boolean authenticate(AuthenticationRequest request){
//        // var là từ khóa trong Java 10, cho phép khai báo biến mà không cần chỉ định kiểu dữ liệu
//        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
//        // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu người dùng nhập vào hay không
//        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return true;
//        } else {
//            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
//        }
//    }

    // Triển khai JWT để xác thực người dùng
    // Generate SIGNING_KEY từ trang https://generate-random.org
    protected static final String SIGNING_KEY = "7OCl+qqZCfQqdQijYq1xiA6mu15k8Ho66tsGBYHmrsEMWnTzEIl7L/ECLUOyLltu";

    // Login
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // var là từ khóa trong Java 10, cho phép khai báo biến mà không cần chỉ định kiểu dữ liệu
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
        // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu người dùng nhập vào hay không
        // Nếu mật khẩu khớp, set authenticated = true, để nhận biết đăng nhập thành công
        // Nếu không khớp, ném ra ngoại lệ PASSWORD_NOT_MATCH
        AuthenticationResponse response = new AuthenticationResponse();
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setAuthenticated(true);
        } else {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        response.setToken(generateToken(request.getUsername()));
        return response;
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("thoai.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("roles", "user")
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    // Triển khai instropection token
    // kiểm tra xem token có hợp lệ hay không
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse response = new IntrospectResponse();

        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(request.getToken());

        var verified = signedJWT.verify(verifier);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if(verified &&  expirationTime.after(new Date())){
            response.setValid(true);
        } else {
            response.setValid(false);
        }
        return response;
    }

}
