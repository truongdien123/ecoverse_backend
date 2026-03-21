package com.fpt.ecoversequiz.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoversequiz.dto.request.QuestionRequest;
import com.fpt.ecoversequiz.dto.request.QuizTemplateRequest;
import com.fpt.ecoversequiz.dto.request.SubmitQuizRequest;
import com.fpt.ecoversequiz.dto.response.QuestionResponse;
import com.fpt.ecoversequiz.dto.response.QuizResultResponse;
import com.fpt.ecoversequiz.dto.response.QuizTemplateResponse;
import com.fpt.ecoversequiz.entities.Question;
import com.fpt.ecoversequiz.entities.QuizAttempt;
import com.fpt.ecoversequiz.entities.QuizPlacement;
import com.fpt.ecoversequiz.entities.QuizTemplate;
import com.fpt.ecoversequiz.repositories.QuestionRepository;
import com.fpt.ecoversequiz.repositories.QuizAttemptRepository;
import com.fpt.ecoversequiz.repositories.QuizPlacementRepository;
import com.fpt.ecoversequiz.repositories.QuizTemplateRepository;
import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizTemplateRepository templateRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Autowired
    private QuizPlacementRepository placementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // ======================== QUIZ TEMPLATE ========================

    @Transactional
    public QuizTemplateResponse createTemplate(QuizTemplateRequest request) {
        QuizTemplate template = new QuizTemplate();
        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());
        template.setCreatedBy("PARTNERSHIP"); // Only partnerships can create quizzes
        template.setPartnerId(request.getPartnerId());
        template.setActive(true);
        QuizTemplate saved = templateRepository.save(template);

        // Save questions
        List<Question> questions = saveQuestions(saved.getId(), request.getQuestions());

        return mapToTemplateResponse(saved, questions);
    }

    public List<QuizTemplateResponse> getAllActiveTemplates() {
        return templateRepository.findByActiveTrue()
                .stream()
                .map(t -> mapToTemplateResponse(t, null))
                .collect(Collectors.toList());
    }

    public List<QuizTemplateResponse> getTemplatesByPartner(String partnerId) {
        return templateRepository.findByPartnerIdAndActiveTrue(partnerId)
                .stream()
                .map(t -> mapToTemplateResponse(t, null))
                .collect(Collectors.toList());
    }

    public QuizTemplateResponse getTemplateDetail(String templateId) {
        QuizTemplate template = findTemplateById(templateId);
        List<Question> questions = questionRepository.findByQuizTemplateId(templateId);
        return mapToTemplateResponse(template, questions);
    }

    @Transactional
    public QuizTemplateResponse updateTemplate(String templateId, QuizTemplateRequest request) {
        QuizTemplate template = findTemplateById(templateId);
        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());
        QuizTemplate saved = templateRepository.save(template);

        // Replace questions
        questionRepository.deleteByQuizTemplateId(templateId);
        List<Question> questions = saveQuestions(templateId, request.getQuestions());

        return mapToTemplateResponse(saved, questions);
    }

    @Transactional
    public void deleteTemplate(String templateId) {
        QuizTemplate template = findTemplateById(templateId);
        template.setActive(false); // Soft delete
        templateRepository.save(template);
    }

    // ======================== QUIZ ATTEMPT (STUDENT) ========================

    @Transactional
    public QuizResultResponse submitQuiz(SubmitQuizRequest request) {
        QuizTemplate template = findTemplateById(request.getQuizTemplateId());
        List<Question> questions = questionRepository.findByQuizTemplateId(template.getId());

        if (questions.isEmpty()) {
            throw new BadRequestException("This quiz template has no questions");
        }
        if (request.getAnswers().size() != questions.size()) {
            throw new BadRequestException("Number of answers (" + request.getAnswers().size()
                    + ") must match number of questions (" + questions.size() + ")");
        }

        // Calculate attempt number
        long previousAttempts = attemptRepository.countByQuizTemplateIdAndStudentId(
                template.getId(), request.getStudentId());
        int attemptNumber = (int) previousAttempts + 1;

        // Grade the answers
        int correctCount = 0;
        List<QuizResultResponse.PlacementDetail> details = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String studentAnswer = request.getAnswers().get(i);
            boolean isCorrect = q.getCorrectAnswer().equalsIgnoreCase(studentAnswer);
            if (isCorrect) correctCount++;

            // Save placement
            QuizPlacement placement = new QuizPlacement();
            placement.setQuizAttemptId(null); // will update after saving attempt
            placement.setQuestionId(q.getId());
            placement.setSelectedAnswer(studentAnswer);
            placement.setIsCorrect(isCorrect);

            details.add(QuizResultResponse.PlacementDetail.builder()
                    .questionId(q.getId())
                    .questionText(q.getText())
                    .options(deserializeOptions(q.getOptionsJson()))
                    .selectedAnswer(studentAnswer)
                    .correctAnswer(q.getCorrectAnswer())
                    .isCorrect(isCorrect)
                    .explanation(q.getExplanation())
                    .build());
        }

        int wrongCount = questions.size() - correctCount;
        int score = (int) Math.round((double) correctCount / questions.size() * 100);

        // Save attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizTemplateId(template.getId());
        attempt.setStudentId(request.getStudentId());
        attempt.setSelectedAnswersJson(serializeAnswers(request.getAnswers()));
        attempt.setScore(score);
        attempt.setCorrectAmount(correctCount);
        attempt.setWrongAmount(wrongCount);
        attempt.setCompleted(true);
        attempt.setDuration(request.getDuration());
        attempt.setAttemptNumber(attemptNumber);
        QuizAttempt savedAttempt = attemptRepository.save(attempt);

        // Save placements
        for (int i = 0; i < questions.size(); i++) {
            QuizPlacement placement = new QuizPlacement();
            placement.setQuizAttemptId(savedAttempt.getId());
            placement.setQuestionId(questions.get(i).getId());
            placement.setSelectedAnswer(request.getAnswers().get(i));
            placement.setIsCorrect(details.get(i).getIsCorrect());
            placementRepository.save(placement);
        }

        return QuizResultResponse.builder()
                .attemptId(savedAttempt.getId())
                .quizTemplateId(template.getId())
                .studentId(request.getStudentId())
                .score(score)
                .correctAmount(correctCount)
                .wrongAmount(wrongCount)
                .totalQuestions(questions.size())
                .duration(request.getDuration())
                .attemptNumber(attemptNumber)
                .completed(true)
                .details(details)
                .completedAt(savedAttempt.getUpdatedAt())
                .build();
    }

    public List<QuizResultResponse> getStudentHistory(String studentId) {
        return attemptRepository.findByStudentId(studentId)
                .stream()
                .map(a -> QuizResultResponse.builder()
                        .attemptId(a.getId())
                        .quizTemplateId(a.getQuizTemplateId())
                        .studentId(a.getStudentId())
                        .score(a.getScore())
                        .correctAmount(a.getCorrectAmount())
                        .wrongAmount(a.getWrongAmount())
                        .duration(a.getDuration())
                        .attemptNumber(a.getAttemptNumber())
                        .completed(a.getCompleted())
                        .completedAt(a.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // ======================== HELPERS ========================

    private QuizTemplate findTemplateById(String id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz template not found: " + id));
    }

    private List<Question> saveQuestions(String templateId, List<QuestionRequest> requests) {
        List<Question> saved = new ArrayList<>();
        for (QuestionRequest req : requests) {
            Question q = new Question();
            q.setQuizTemplateId(templateId);
            q.setText(req.getText());
            q.setOptionsJson(serializeOptions(req.getOptions()));
            q.setCorrectAnswer(req.getCorrectAnswer());
            q.setExplanation(req.getExplanation());
            saved.add(questionRepository.save(q));
        }
        return saved;
    }

    private QuizTemplateResponse mapToTemplateResponse(QuizTemplate t, List<Question> questions) {
        List<QuestionResponse> questionResponses = null;
        if (questions != null) {
            questionResponses = questions.stream()
                    .map(q -> QuestionResponse.builder()
                            .id(q.getId())
                            .quizTemplateId(q.getQuizTemplateId())
                            .text(q.getText())
                            .options(deserializeOptions(q.getOptionsJson()))
                            .correctAnswer(q.getCorrectAnswer())
                            .explanation(q.getExplanation())
                            .build())
                    .collect(Collectors.toList());
        }

        return QuizTemplateResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .createdBy(t.getCreatedBy())
                .partnerId(t.getPartnerId())
                .active(t.getActive())
                .questionCount(questions != null ? questions.size()
                        : (int) questionRepository.countByQuizTemplateId(t.getId()))
                .questions(questionResponses)
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }

    private String serializeOptions(List<String> options) {
        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid options format");
        }
    }

    private List<String> deserializeOptions(String json) {
        if (json == null) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    private String serializeAnswers(List<String> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid answers format");
        }
    }
}
