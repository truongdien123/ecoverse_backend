package com.fpt.ecoverseauth.security;

import com.fpt.ecoverseauth.enums.UserType;
import com.fpt.ecoverseauth.repositories.AdminRepository;
import com.fpt.ecoverseauth.repositories.ParentRepository;
import com.fpt.ecoverseauth.repositories.PartnershipRepository;
import com.fpt.ecoverseauth.repositories.StudentRepository;
import com.fpt.ecoversecommon.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService that loads user by email and user type
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Qualifier("authAdminRepository")
    private AdminRepository adminRepository;

    @Autowired
    @Qualifier("authParentRepository")
    private ParentRepository parentRepository;

    @Autowired
    private PartnershipRepository partnershipRepository;

    @Autowired
    @Qualifier("authStudentRepository")
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Use loadUserByEmailAndType instead");
    }

    /**
     * Load user by email and user type
     */
    public UserDetails loadUserByEmailAndType(String email, UserType userType) {
        return switch (userType) {
            case ADMIN -> loadAdmin(email);
            case PARENT -> loadParent(email);
            case PARTNERSHIP -> loadPartnership(email);
            case STUDENT -> throw new NotFoundException("Students should use student code to login");
        };
    }

    /**
     * Load student by student code
     */
    public UserDetails loadStudentByCode(String studentCode) {
        var student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new NotFoundException("Student not found with code: " + studentCode));

        return CustomUserDetails.builder()
                .id(student.getId())
                .email(null)
                .password(null)
                .fullName(student.getFullName())
                .avatarUrl(student.getAvatarUrl())
                .userType(UserType.STUDENT)
                .active(student.getActive() != null ? student.getActive() : true)
                .build();
    }

    private UserDetails loadAdmin(String email) {
        var admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Admin not found with email: " + email));

        return CustomUserDetails.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .password(admin.getPassword())
                .fullName(admin.getFullName())
                .avatarUrl(admin.getAvatarUrl())
                .userType(UserType.ADMIN)
                .active(true)
                .build();
    }

    private UserDetails loadParent(String email) {
        var parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent not found with email: " + email));

        return CustomUserDetails.builder()
                .id(parent.getId())
                .email(parent.getEmail())
                .password(parent.getPassword())
                .fullName(parent.getFullName())
                .avatarUrl(parent.getAvatarUrl())
                .userType(UserType.PARENT)
                .active(parent.getActive() != null ? parent.getActive() : true)
                .build();
    }

    private UserDetails loadPartnership(String email) {
        var partnership = partnershipRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Partnership not found with email: " + email));

        return CustomUserDetails.builder()
                .id(partnership.getId())
                .email(partnership.getEmail())
                .password(partnership.getPassword())
                .fullName(partnership.getOrganizationName())
                .avatarUrl(partnership.getAvatarUrl())
                .userType(UserType.PARTNERSHIP)
                .active(true)
                .build();
    }
}
