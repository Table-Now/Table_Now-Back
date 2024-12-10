//package zerobase.tableNow.domain.user.service.impl;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import zerobase.tableNow.domain.constant.Status;
//import zerobase.tableNow.domain.store.entity.StoreEntity;
//import zerobase.tableNow.domain.store.repository.StoreRepository;
//import zerobase.tableNow.domain.user.dto.*;
//import zerobase.tableNow.domain.user.entity.EmailEntity;
//import zerobase.tableNow.domain.user.entity.UsersEntity;
//import zerobase.tableNow.domain.user.repository.EmailRepository;
//import zerobase.tableNow.domain.user.repository.UserRepository;
//import zerobase.tableNow.domain.user.service.UserService;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final StoreRepository storeRepository;
//    private final EmailRepository emailRepository;
//
//    //회원가입
////    @Override
////    public RegisterDto register(RegisterDto registerDto) {
////        // 중복 체크
////        Optional<UsersEntity> optionalUsers = userRepository.findByUser(registerDto.getUser());
////        if (optionalUsers.isPresent()) {
////            UsersEntity existingUser = optionalUsers.get();
////            if (existingUser.getUserStatus() == Status.STOP) {
////                log.info("회원탈퇴한 ID 입니다. 다른 ID를 사용해주세요.");
////                throw new RuntimeException("회원탈퇴한 ID 입니다. 다른 ID를 사용해주세요.");
////            } else {
////                log.info("이미 존재하는 아이디 입니다.");
////                throw new RuntimeException("이미 존재하는 아이디 입니다.");
////            }
////        }
////
////        long emailCount = userRepository.countByEmail(registerDto.getEmail());
////        if (emailCount > 0) {
////            log.info("이미 존재하는 이메일 입니다.");
////            throw new RuntimeException("이미 존재하는 이메일 입니다.");
////        }
////
////        // DTO -> Entity 변환 및 저장
////        UsersEntity userEntity = userMapper.toEntity(registerDto);
////        UsersEntity savedEntity = userRepository.save(userEntity);
////
////        // EmailEntity 저장 (이메일 인증 관련)
////        EmailEntity emailEntity = new EmailEntity();
////        emailEntity.setEmail(savedEntity);
////        emailEntity.setEmailAuthKey(UUID.randomUUID().toString());
////        emailRepository.save(emailEntity);
////
////        // 인증 메일 발송
////        String email = registerDto.getEmail();
////        String subject = "TableNow 이메일 인증";
////        String text = mailComponents.getEmailAuthTemplate(savedEntity.getUser(), emailEntity.getEmailAuthKey());
////
////        boolean sendResult = mailComponents.sendMail(email, subject, text);
////        if (!sendResult) {
////            log.error("회원가입 인증 메일 발송 실패");
////        }
////
////        return userMapper.toDto(savedEntity);
////    }
//
//    //이메일 인증
//    //  @Transactional
////    public boolean emailAuth(String user, String emailAuthKey) {
////        Optional<UsersEntity> optionalUser = userRepository.findByUser(user);
////        if(optionalUser.isEmpty()) {
////            return false;
////        }
////        UsersEntity users = optionalUser.get();
////
////        Optional<EmailEntity> optionalEmail = emailRepository.findByEmailAuthKey(emailAuthKey);
////        if (optionalEmail.isEmpty()) {
////            return false;
////        }
////        EmailEntity emailUser = optionalEmail.get();
////        if(emailUser.isEmailAuthYn()) {
////            return false;
////        }
////
////        emailUser.setEmailAuthYn(true);
////        emailUser.setEmailAuthDt(LocalDateTime.now());
////        emailRepository.save(emailUser);
////
////        users.setUserStatus(Status.ING);
////        userRepository.save(users);
////
////        return true;
////    }
//
//    //로그인
////    @Override
////    public LoginDto login(LoginDto loginDto) {
////        // 1. 먼저 사용자 찾기
////        UsersEntity user = findUserByIdOrThrow(loginDto.getUser());
////
////        // 2. 비밀번호 확인
////        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
////            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
////        }
////
////        // 3. 사용자 상태 확인
////        if(user.getUserStatus() == Status.STOP) {
////            throw new RuntimeException("해당 ID가 없습니다.");
////        }
////
////        EmailEntity emailAuth = emailRepository.findByEmailEmail(user.getEmail())
////                .orElseThrow(() -> new RuntimeException("해당 ID가 없습니다."));
////        if(!emailAuth.isEmailAuthYn()) {
////            throw new RuntimeException("가입 하신 이메일로 인증을 완료해주세요.");
////        }
////
////        // 5. 로그인 성공 처리
////        LoginDto responseDto = userMapper.toLoginDto(user);
////        String accessToken = tokenProvider.generateAccessToken(responseDto);
////        responseDto.setToken(accessToken);
////        return responseDto;
////    }
//
//    // 회원 수정
////    @Override
////    public InfoUpdateDto infoUpdate(InfoUpdateDto infoUpdateDto) {
////        // 현재 로그인한 사용자 정보 가져오기
////        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
////
////        UsersEntity users = userRepository.findByUser(userId)
////                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));
////
////        // 비밀번호 수정
////        if (infoUpdateDto.getPassword() != null && !infoUpdateDto.getPassword().isEmpty()) {
////            String encodedPassword = passwordEncoder.encode(infoUpdateDto.getPassword());
////            users.setPassword(encodedPassword);
////        }
////
////        // 입력된 필드만 수정
////        if (infoUpdateDto.getPassword() != null && !infoUpdateDto.getPassword().isEmpty()) {
////            String encodedPassword = passwordEncoder.encode(infoUpdateDto.getPassword());
////            users.setPassword(encodedPassword);
////        }
////
////        // 이메일 수정
////        if (infoUpdateDto.getEmail() != null && !infoUpdateDto.getEmail().isEmpty()) {
////            if (!infoUpdateDto.getEmail().equals(users.getEmail())) { // 이메일 변경 시만 처리
////                users.setEmail(infoUpdateDto.getEmail());
////
////                // 기존 이메일 정보 삭제
////                Optional<EmailEntity> emailEntity = emailRepository.findByEmail(users);
////                emailEntity.ifPresent(emailRepository::delete);
////
////                // 새로운 이메일 인증 정보 생성
////                EmailEntity newEmailEntity = new EmailEntity();
////                newEmailEntity.setEmail(users);
////                newEmailEntity.setEmailAuthKey(UUID.randomUUID().toString());
////                newEmailEntity.setEmailAuthYn(false); // 초기값: 인증되지 않음
////                newEmailEntity.setUserStatus(Status.STOP);
////                emailRepository.save(newEmailEntity);
////
////                // 인증 메일 발송
////                String subject = "Commerce 이메일 인증 - 내정보 수정";
////                String text = mailComponents.getEmailAuthTemplate(users.getUser(), newEmailEntity.getEmailAuthKey());
////                boolean sendResult = mailComponents.sendMail(infoUpdateDto.getEmail(), subject, text);
////
////                if (!sendResult) {
////                    log.error("내정보 수정 후 인증 메일 발송 실패");
////                    throw new TableException(ErrorCode.EMAIL_SEND_FAILED);
////                }
////            }
////        }
//
//        // 핸드폰 번호 수정
////        if (infoUpdateDto.getPhone() != null && !infoUpdateDto.getPhone().isEmpty()) {
////            users.setPhone(infoUpdateDto.getPhone());
////        }
////
////        // 사용자 정보 저장
////        userRepository.save(users);
////
////        return userMapper.myInfoDto(users);
////    }
//}