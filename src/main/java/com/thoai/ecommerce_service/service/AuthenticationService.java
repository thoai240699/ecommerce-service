package com.thoai.ecommerce_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.thoai.ecommerce_service.dto.request.AuthenticationRequest;
import com.thoai.ecommerce_service.dto.request.IntrospectRequest;
import com.thoai.ecommerce_service.dto.request.LogoutRequest;
import com.thoai.ecommerce_service.dto.response.AuthenticationResponse;
import com.thoai.ecommerce_service.dto.response.IntrospectResponse;
import com.thoai.ecommerce_service.entity.InvalidatedToken;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.repository.InvalidatedTokenRepository;
import com.thoai.ecommerce_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    //
    // Phương thức này sẽ nhận vào một đối tượng AuthenticationRequest và trả về
    // true nếu thông tin xác thực hợp lệ, ngược lại trả về false.
    // public boolean authenticate(AuthenticationRequest request){
    // // var là từ khóa trong Java 10, cho phép khai báo biến mà không cần chỉ định
    // kiểu dữ liệu
    // var user =
    // userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new
    // AppException(ErrorCode.USERNAME_NOT_FOUND));
    //
    // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
    // // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu người dùng nhập vào
    // hay không
    // if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    // return true;
    // } else {
    // throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
    // }
    // }

    // Triển khai JWT để xác thực người dùng
    // Generate SIGNING_KEY từ trang https://generate-random.org
    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNING_KEY;
    // Lấy SIGNING_KEY từ file application.yaml
    // protected static final String SIGNING_KEY =
    // "7OCl+qqZCfQqdQijYq1xiA6mu15k8Ho66tsGBYHmrsEMWnTzEIl7L/ECLUOyLltu";

    // Login
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // var là từ khóa trong Java 10, cho phép khai báo biến mà không cần chỉ định
        // kiểu dữ liệu
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        // Mã hóa mật khẩu theo thuật toán hash BCrypt, strength là độ mạnh của thuật
        // toán, từ 4 đến 31, mặc định là 10
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu người dùng nhập vào hay
        // không
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        return AuthenticationResponse.builder().token(generateToken(user)).build();
    }

    // Triển khai instropection token
    // kiểm tra xem token có hợp lệ hay không
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {

        try {
            verifyToken(request.getToken());
        } catch (AppException e) {
            return IntrospectResponse.builder().valid(false).build();
        }
        return IntrospectResponse.builder().valid(true).build();
    }

    // Logout
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        // Trong trường hợp này, chúng ta không cần làm gì cả token sẽ hết hạn sau một
        // khoảng thời gian nhất định
        // Hoặc có thể xóa token khỏi bộ nhớ cache nếu sử dụng
        SignedJWT signedJWT = verifyToken(request.getToken());
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiration(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return signedJWT;
    }

    // Phương thức này sẽ tạo một token JWT cho người dùng đã đăng nhập
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("thoai.com") // Thay thế bằng tên miền
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(4, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString()) // Tạo một ID duy nhất cho token
                .claim("scope", buildScope(user)) // Thêm thông tin về quyền hạn của người dùng
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not create token", e);
            throw new RuntimeException(e);
        }
    }

    // Xây dựng chuỗi quyền hạn (scope) từ danh sách vai trò của người dùng
    private String buildScope(User user) {
        StringJoiner stringJoiner =
                new StringJoiner(" "); // Khoang trong này sẽ được sử dụng để phân tách các quyền hạn
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName()); // Thêm tiền tố "ROLE_" vào tên vai trò
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }
}
