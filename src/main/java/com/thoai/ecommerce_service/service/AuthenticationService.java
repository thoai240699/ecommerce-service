package com.thoai.ecommerce_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.thoai.ecommerce_service.dto.request.AuthenticationRequest;
import com.thoai.ecommerce_service.dto.request.IntrospectRequest;
import com.thoai.ecommerce_service.dto.response.AuthenticationResponse;
import com.thoai.ecommerce_service.dto.response.IntrospectResponse;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.util.CollectionUtils;

import java.text.CollationElementIterator;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    UserRepository userRepository;

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
    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNING_KEY;
    //protected static final String SIGNING_KEY = "7OCl+qqZCfQqdQijYq1xiA6mu15k8Ho66tsGBYHmrsEMWnTzEIl7L/ECLUOyLltu";

    // Login
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // var là từ khóa trong Java 10, cho phép khai báo biến mà không cần chỉ định kiểu dữ liệu
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        // Mã hóa mật khẩu theo thuật toán hash BCrypt, strength là độ mạnh của thuật toán, từ 4 đến 31, mặc định là 10
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu người dùng nhập vào hay không
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("thoai.com") // Thay thế bằng tên miền
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(4, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user)) // Thêm thông tin về quyền hạn của người dùng
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không thể tạo token", e);
            throw new RuntimeException(e);
        }
    }

    // Xây dựng chuỗi quyền hạn (scope) từ danh sách vai trò của người dùng
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner("");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
    // Triển khai instropection token
    // kiểm tra xem token có hợp lệ hay không
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {

        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(request.getToken());

        boolean verified = signedJWT.verify(verifier);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .valid(verified && expirationTime.after(new Date()))
                .build();
    }

}
