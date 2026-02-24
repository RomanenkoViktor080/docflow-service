package com.example.docflow_service.controller.document;

import com.example.docflow_service.dto.document.test.DocumentApproveResponseDto;
import com.example.docflow_service.dto.document.test.DocumentApproveTestDto;
import com.example.docflow_service.service.document.DocumentTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tests")
@Tag(name = "Concurrency testing")
public class DocumentTestController {
    private final DocumentTestService testService;

    @Operation(
            summary = "Тест конкурентного утверждения",
            description = "Сценарий: \n"
                    + "1. Генерируется один документ.\n"
                    + "2. Отправляет 1 раз на согласование.\n"
                    + "3. Запускается N параллельных потоков.\n"
                    + "4. Каждый поток пытается одновременно вызвать метод 'approve' для этого документа.\n"
                    + "5. Система проверяет, что только 1 поток смог успешно завершить операцию и возвращает сводку."
    )
    @PostMapping("/approve")
    public DocumentApproveResponseDto submit(
            @RequestBody @Valid DocumentApproveTestDto dto
    ) {
        return testService.approve(dto);
    }
}
