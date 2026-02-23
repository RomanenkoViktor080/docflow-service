package com.example.docflow_service.controller.document;

import com.example.docflow_service.dto.document.test.DocumentApproveResponseDto;
import com.example.docflow_service.service.document.DocumentTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tests")
@Tag(name = "Test")
public class DocumentTestController {
    private final DocumentTestService testService;

    @PostMapping("/approve")
    public DocumentApproveResponseDto submit() {
        return testService.approve();
    }
}
