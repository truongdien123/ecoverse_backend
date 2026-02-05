package com.fpt.ecoverseuser.services.imp;

import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.NotFoundException;
import com.fpt.ecoversecommon.util.UploadFile;
import com.fpt.ecoverseuser.dtos.StatisticPartner;
import com.fpt.ecoverseuser.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverseuser.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverseuser.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverseuser.entities.Partner;
import com.fpt.ecoverseuser.mappers.PartnerMapper;
import com.fpt.ecoverseuser.repositories.ParentRepository;
import com.fpt.ecoverseuser.repositories.PartnerRepository;
import com.fpt.ecoverseuser.repositories.StudentRepository;
import com.fpt.ecoverseuser.services.PartnerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerServiceImp implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final PasswordEncoder passwordEncoder;
    private final UploadFile uploadFile;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    public PartnerServiceImp(PartnerRepository partnerRepository, PartnerMapper partnerMapper, PasswordEncoder passwordEncoder, UploadFile uploadFile, ParentRepository parentRepository, StudentRepository studentRepository) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
        this.passwordEncoder = passwordEncoder;
        this.uploadFile = uploadFile;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public PartnerResponseDto createPartner(PartnerRegisterRequestDto request) {
        Optional<Partner> checkingEmailPartner = partnerRepository.findByEmail(request.getEmail());
        if (checkingEmailPartner.isPresent()) {
            throw new BadRequestException("Email already exist");
        }
        Optional<Partner> checkingPhoneNumberPartner = partnerRepository.findByPhoneNumber(request.getPhoneNumber());
        if (checkingPhoneNumberPartner.isPresent()) {
            throw new BadRequestException("Phone number already exist");
        }
        Partner partner = partnerMapper.toPartner(request, uploadFile);
        partner.setPassword(passwordEncoder.encode(request.getPassword()));
        partnerRepository.save(partner);
        return partnerMapper.toPartnerResponse(partner);
    }

    @Override
    public PartnerResponseDto getDetailPartner(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponse(partner.get());
        StatisticPartner statisticPartner = new StatisticPartner();
        statisticPartner.setTotalParents(parentRepository.countParentsByPartnerId(partnerId));
        statisticPartner.setTotalStudents(studentRepository.countStudentByPartnerId(partnerId));
        statisticPartner.setTotalActiveGames(0);
        statisticPartner.setTotalActiveQuizzes(0);
        statisticPartner.setTotalPointDistributed(0);
        statisticPartner.setTotalRedemptions(0);
        partnerResponseDto.setStatistics(statisticPartner);
        return partnerResponseDto;
    }

    @Override
    public PartnerResponseDto updatePartner(String partnerId, PartnerUpdateRequestDto request) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        partnerMapper.toPartner(partner.get(), request, uploadFile);
        if (request.getAvatar() != null) {
            partner.get().setAvatarUrl(uploadFile.imageToUrl(request.getAvatar()));
        }
        partnerRepository.save(partner.get());
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponse(partner.get());
        StatisticPartner statisticPartner = new StatisticPartner();
        statisticPartner.setTotalParents(parentRepository.countParentsByPartnerId(partnerId));
        statisticPartner.setTotalStudents(studentRepository.countStudentByPartnerId(partnerId));
        statisticPartner.setTotalActiveGames(0);
        statisticPartner.setTotalActiveQuizzes(0);
        statisticPartner.setTotalPointDistributed(0);
        statisticPartner.setTotalRedemptions(0);
        partnerResponseDto.setStatistics(statisticPartner);
        return partnerResponseDto;
    }
}
